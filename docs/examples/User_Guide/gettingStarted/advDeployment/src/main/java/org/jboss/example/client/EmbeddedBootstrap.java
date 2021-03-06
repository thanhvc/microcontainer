package org.jboss.example.client;

import java.net.URL;

import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;

/**
 * Add a BasicXMLDeployer to the BasicBootstrap so that we can parse
 * XML descriptors for the beans (*-beans.xml).
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class EmbeddedBootstrap extends BasicBootstrap {
	
	protected BasicXMLDeployer deployer;

	public EmbeddedBootstrap() throws Exception {
		super();
	}

	public void bootstrap() throws Throwable {
		super.bootstrap();
		deployer = new BasicXMLDeployer(getKernel());
		Runtime.getRuntime().addShutdownHook(new Shutdown());
	}

	public void deploy(URL url) {
		try {
			// Workaround the fact that the BasicXMLDeployer does not handle redeployment correctly
			if (deployer.getDeploymentNames().contains(url.toString())) {
				System.out.println("Aspectized deployers are already deployed.");
				return;
			}
			deployer.deploy(url);
		} catch (Throwable t) {
			log.warn("Error during deployment: " + url, t);
		}
	}

	public void undeploy(URL url) {
		if (!deployer.getDeploymentNames().contains(url.toString())) {
			System.out.println("Aspectized deployers are already undeployed.");
			return;
		}
		try {
			deployer.undeploy(url);
		} catch (Throwable t) {
			log.warn("Error during undeployment: " + url, t);
		}
	}

	protected class Shutdown extends Thread {
		public void run() {
			log.info("Shutting down");		
			deployer.shutdown();
		}
	}	
}
/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.kernel.plugins.deployment.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.dependency.spi.ControllerMode;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.BasicKernelDeployer;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.logging.Logger;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingResolver;
import org.jboss.xb.binding.sunday.unmarshalling.SingletonSchemaResolverFactory;

/**
 * BasicXMLDeployer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BasicXMLDeployer extends BasicKernelDeployer
{
   /** The log */
   private static final Logger log = Logger.getLogger(BasicXMLDeployer.class);

   /** Unmarshaller factory */
   private static final UnmarshallerFactory factory = UnmarshallerFactory.newInstance();

   /** The resolver */
   private static final SchemaBindingResolver resolver = SingletonSchemaResolverFactory.getInstance().getSchemaBindingResolver();

   /** The deployments by url or name */
   private Map<String, KernelDeployment> deploymentsByName = new ConcurrentHashMap<String, KernelDeployment>();

   /**
    * Create a new XML deployer
    * 
    * @param kernel the kernel
    */
   public BasicXMLDeployer(Kernel kernel)
   {
      this(kernel, null);
   }

   /**
    * Create a new XML deployer with mode.
    *
    * @param kernel the kernel
    * @param mode the controller mode
    */
   public BasicXMLDeployer(Kernel kernel, ControllerMode mode)
   {
      super(kernel, mode);
   }

   public Collection<String> getDeploymentNames()
   {
      return deploymentsByName.keySet();
   }
   
   public void deploy(KernelDeployment deployment) throws Throwable
   {
      super.deploy(deployment);
      deploymentsByName.put(deployment.getName(), deployment);
   }

   public void undeploy(KernelDeployment deployment)
   {
      deploymentsByName.remove(deployment.getName());
      super.undeploy(deployment);
   }

   /**
    * Undeploy a url
    * 
    * @param url the url to undeploy
    * @throws IllegalStateException if the url is unknown
    */
   public void undeploy(final URL url)
   {
      if (url == null)
         throw new IllegalArgumentException("Null url");
      undeploy(url.toString());
   }

   /**
    * Undeploy a name deployment
    * 
    * @param name the name of the deployment to undeploy
    * @throws IllegalStateException if the name is unknown
    */
   public void undeploy(final String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      KernelDeployment deployment = deploymentsByName.remove(name);
      if (deployment == null)
         throw new IllegalStateException("Unknown deployment " + name);
      undeploy(deployment);
   }

   /**
    * Deploy a url
    * 
    * @param url the url to deploy
    * @return the kernel deployment
    * @throws Throwable for any error
    */
   public KernelDeployment deploy(final URL url) throws Throwable
   {
      final boolean trace = log.isTraceEnabled();

      if (url == null)
         throw new IllegalArgumentException("Null url");

      if (trace)
         log.trace("Parsing " + url);

      long start = System.currentTimeMillis();

      Unmarshaller unmarshaller = factory.newUnmarshaller();
      KernelDeployment deployment = (KernelDeployment) unmarshaller.unmarshal(url.toString(), resolver);
      if (deployment == null)
         throw new RuntimeException("The xml " + url + " is not well formed!");
      deployment.setName(url.toString());

      if (trace)
      {
         long now = System.currentTimeMillis();
         log.trace("Parsing " + url + " took " + (now-start) + " milliseconds");
      }

      deploy(deployment);

      if (trace)
      {
         long now = System.currentTimeMillis();
         log.trace("Deploying " + url + " took " + (now-start) + " milliseconds");
      }

      return deployment;
   }

   /**
    * Deploy a stream.  We may be deploying XML fragments.
    *
    * @param deploymentName the deployment name
    * @param stream the stream
    * @return the kernel deployment
    * @throws Throwable for any error
    */
   public KernelDeployment deploy(String deploymentName, final InputStream stream) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (trace)
         log.trace("Parsing " + deploymentName);
      Unmarshaller unmarshaller = factory.newUnmarshaller();
      KernelDeployment deployment = (KernelDeployment) unmarshaller.unmarshal(stream, resolver);
      if (deployment == null)
         throw new RuntimeException("The deployment " + deploymentName + " is not well formed!");
      deployment.setName(deploymentName);

      deploy(deployment);

      return deployment;
   }
}

/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.aop.junit;

import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.aop.AspectXmlLoader;
import org.jboss.test.kernel.junit.MicrocontainerTestDelegate;

/**
 * 
 * An AOPMicrocontainerTestDelegate.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPMicrocontainerTestDelegate extends MicrocontainerTestDelegate
{
   /** The deployed urls */
   private static final CopyOnWriteArrayList<URL> urls = new CopyOnWriteArrayList<URL>();
   
   /**
    * Create a new AOPMicrocontainerTestDelegate.
    * 
    * @param clazz the class
    * @throws Exception for any error
    */
   public AOPMicrocontainerTestDelegate(Class<?> clazz) throws Exception
   {
      super(clazz);
   }

   public void setUp() throws Exception
   {
      super.setUp();
      log.debug("Security enabled: " + enableSecurity);
   }

   

   protected void deploy() throws Exception
   {
      String testName = clazz.getName();
      testName = testName.replace('.', '/') + "-aop.xml";
      URL url = clazz.getClassLoader().getResource(testName);
      if (url != null)
         deployAOP(url);
      else
         log.debug("No test specific deployment " + testName);

      super.deploy();
   }

   protected void undeploy()
   {

      super.undeploy();
      for (Iterator<URL> i = urls.iterator(); i.hasNext();)
      {
         URL url = i.next();
         undeployAOP(url);
      }
   }
   
   /**
    * Deploy the aop config
    *
    * @param url the url
    * @throws Exception for any error
    */
   protected void deployAOP(URL url) throws Exception
   {
      log.debug("Deploying " + url);
      urls.add(url);
      AspectXmlLoader.deployXML(url);
   }

   /**
    * Undeploy the aop config
    * 
    * @param url the url
    */
   protected void undeployAOP(URL url)
   {
      try
      {
         log.debug("Undeploying " + url);
         urls.remove(url);
         AspectXmlLoader.undeployXML(url);
      }
      catch (Exception e)
      {
         log.warn("Ignored error undeploying " + url, e);
      }
   }

//   @Override
//   protected BasicXMLDeployer createDeployer()
//   {
//      if (useJaxbDeployer)
//      {
//         return new JBossXBDeployer(kernel, defaultMode, clazz);
//      }
//      return super.createDeployer();
//   }
//   
//   protected KernelDeployment unmarshal(final URL url) throws Exception
//   {
//      if (!useJaxbDeployer)
//      {
//         throw new IllegalStateException("useJaxbDeployer needs to be true");
//      }
//      return ((JBossXBDeployer)deployer).unmarshal(url);
//   }
}

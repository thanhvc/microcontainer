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
package org.jboss.deployers.plugins.classloading;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.spi.deployer.helpers.AbstractOptionalRealDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.classloading.ClassLoaderMetaData;

/**
 * AbstractClassLoaderDescribeDeployer.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractClassLoaderDescribeDeployer extends AbstractOptionalRealDeployer<ClassLoaderMetaData>
{
   /** The classloading */
   private ClassLoading classLoading;

   /**
    * Create a new AbstractClassLoaderDescribeDeployer.
    */
   public AbstractClassLoaderDescribeDeployer()
   {
      super(ClassLoaderMetaData.class);
      setStage(DeploymentStages.DESCRIBE);
   }

   /**
    * Get the classLoading.
    *
    * @return the classLoading.
    */
   public ClassLoading getClassLoading()
   {
      return classLoading;
   }

   /**
    * Set the classLoading.
    *
    * @param classLoading the classLoading.
    */
   public void setClassLoading(ClassLoading classLoading)
   {
      this.classLoading = classLoading;
   }

   /**
    * Check the configuration
    *
    * @throws Exception for any error
    */
   public void create() throws Exception
   {
      if (classLoading == null)
         throw new DeploymentException("Classloading has not been configured");
   }

   public void deploy(DeploymentUnit unit, ClassLoaderMetaData deployment) throws DeploymentException
   {
      classLoading.addDeploymentUnit(unit);
   }

   public void undeploy(DeploymentUnit unit, ClassLoaderMetaData deployment)
   {
      classLoading.removeDeploymentUnit(unit);
   }
}
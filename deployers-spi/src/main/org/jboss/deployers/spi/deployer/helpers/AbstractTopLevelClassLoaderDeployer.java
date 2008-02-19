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
package org.jboss.deployers.spi.deployer.helpers;

import org.jboss.deployers.structure.spi.DeploymentUnit;

/**
 * AbstractTopLevelClassLoaderDeployer.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractTopLevelClassLoaderDeployer extends AbstractClassLoaderDeployer
{
   public ClassLoader createClassLoader(DeploymentUnit unit) throws Exception
   {
      if (unit.isTopLevel())
         return createTopLevelClassLoader(unit);
      
      return unit.getTopLevel().getClassLoader();
   }

   @Override
   public void removeClassLoader(DeploymentUnit unit) throws Exception
   {
      if (unit.isTopLevel())
         removeTopLevelClassLoader(unit);
   }

   /**
    * Create a top level classloader
    * 
    * @param unit the deployment unit
    * @return the classloader
    * @throws Exception for any error
    */
   protected abstract ClassLoader createTopLevelClassLoader(DeploymentUnit unit) throws Exception;

   /**
    * Remove a top level classloader
    * 
    * @param unit the deployment unit
    * @throws Exception for any error
    */
   protected void removeTopLevelClassLoader(DeploymentUnit unit) throws Exception
   {
   }
}

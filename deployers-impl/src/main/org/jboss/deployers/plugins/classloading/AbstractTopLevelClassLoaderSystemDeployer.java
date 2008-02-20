/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloading.spi.RealClassLoader;
import org.jboss.classloading.spi.dependency.ClassLoading;
import org.jboss.classloading.spi.dependency.Module;
import org.jboss.classloading.spi.dependency.policy.ClassLoaderPolicyModule;
import org.jboss.deployers.spi.deployer.helpers.AbstractTopLevelClassLoaderDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;

/**
 * AbstractTopLevelClassLoaderSystemDeployer.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractTopLevelClassLoaderSystemDeployer extends AbstractTopLevelClassLoaderDeployer
{
   /** The classloading */
   private ClassLoading classLoading;
   
   /** The classloader system */
   private ClassLoaderSystem system;
   
   /** The MBeanServer */
   private MBeanServer mbeanServer;
   
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
    * Get the system.
    * 
    * @return the system.
    */
   public ClassLoaderSystem getSystem()
   {
      return system;
   }

   /**
    * Set the system.
    * 
    * @param system the system.
    */
   public void setSystem(ClassLoaderSystem system)
   {
      this.system = system;
   }

   /**
    * Get the mbeanServer.
    * 
    * @return the mbeanServer.
    */
   public MBeanServer getMbeanServer()
   {
      return mbeanServer;
   }

   /**
    * Set the mbeanServer.
    * 
    * @param mbeanServer the mbeanServer.
    */
   public void setMbeanServer(MBeanServer mbeanServer)
   {
      this.mbeanServer = mbeanServer;
   }
   
   @Override
   protected ClassLoader createTopLevelClassLoader(DeploymentUnit unit) throws Exception
   {
      if (classLoading == null)
         throw new IllegalStateException("The classLoading has not been set");
      if (system == null)
         throw new IllegalStateException("The system has not been set");

      // No module means no classloader for the deployment, use the deployer's classloader
      Module module = unit.getAttachment(Module.class);
      if (module == null)
         return null;

      if (module instanceof ClassLoaderPolicyModule == false)
         throw new IllegalStateException("Module is not an instance of " + ClassLoaderPolicyModule.class.getName() + " actual=" + module.getClass().getName());
      ClassLoaderPolicyModule classLoaderPolicyModule = (ClassLoaderPolicyModule) module;
      
      ClassLoader classLoader = classLoaderPolicyModule.registerClassLoaderPolicy(system);
      try
      {
         registerClassLoaderWithMBeanServer(classLoader);
      }
      catch (Throwable t)
      {
         log.warn("Unable to register classloader with mbeanserver: " + classLoader, t);
      }
      return classLoader;
   }
   
   @Override
   protected void removeTopLevelClassLoader(DeploymentUnit unit) throws Exception
   {
      // No module means no for the deployment classloader
      Module module = unit.getAttachment(Module.class);
      if (module == null)
         return;

      ClassLoader classLoader = unit.getClassLoader();
      try
      {
         unregisterClassLoaderFromMBeanServer(classLoader);
      }
      catch (Throwable t)
      {
         log.warn("Unable to unregister classloader from mbeanserver: " + classLoader, t);
      }
      
      try
      {
         // Remove the classloader
         system.unregisterClassLoader(classLoader);
      }
      finally
      {
         cleanup(unit, module);
         module.reset();
      }
   }

   /**
    * Register the classloader with the mbeanserver
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   protected void registerClassLoaderWithMBeanServer(ClassLoader classLoader) throws Exception
   {
      if (mbeanServer == null)
         return;
      
      if (classLoader instanceof RealClassLoader == false)
         return;
      
      RealClassLoader jmxClassLoader = (RealClassLoader) classLoader;
      ObjectName name = jmxClassLoader.getObjectName();
      if (mbeanServer.isRegistered(name))
         return;
      
      mbeanServer.registerMBean(classLoader, name);
   }

   /**
    * Unregister the classloader from the mbeanserver
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   protected void unregisterClassLoaderFromMBeanServer(ClassLoader classLoader) throws Exception
   {
      if (mbeanServer == null)
         return;
      
      if (classLoader instanceof RealClassLoader == false)
         return;
      
      RealClassLoader jmxClassLoader = (RealClassLoader) classLoader;
      ObjectName name = jmxClassLoader.getObjectName();
      if (mbeanServer.isRegistered(name) == false)
         return;
      mbeanServer.unregisterMBean(name);
   }
   
   /**
    * Hook to perform cleanup on destruction of classloaader
    * 
    * @param unit the deployment unit
    * @param module the module
    * @throws Exception for any error
    */
   protected void cleanup(DeploymentUnit unit, Module module) throws Exception
   {
   }
}

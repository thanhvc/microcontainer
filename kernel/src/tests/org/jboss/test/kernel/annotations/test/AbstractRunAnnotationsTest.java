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
package org.jboss.test.kernel.annotations.test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.BaseTestCase;
import org.jboss.dependency.spi.ControllerState;

/**
 * Abstract annotation runner test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractRunAnnotationsTest extends BaseTestCase
{
   private KernelController controller;

   protected AbstractRunAnnotationsTest(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();

      controller = createController();
   }

   protected void tearDown() throws Exception
   {
      if (controller != null)
         controller.shutdown();
      controller = null;

      super.tearDown();
   }

   protected void runAnnotationsOnTarget(Object target) throws Throwable
   {
      assertNotNull("Target is null", target);
      runAnnotations(target.getClass(), target);
   }

   protected void runAnnotationsOnClass(Class<?> clazz) throws Throwable
   {
      runAnnotations(clazz, null);
   }

   protected void runAnnotations(Class<?> clazz, Object target) throws Throwable
   {
      KernelController controller = getController();
      String className = clazz.getName();
      String name = target != null ? target.toString() : (className + System.currentTimeMillis());
      AbstractBeanMetaData beanMetaData = new AbstractBeanMetaData(name, className);
      try
      {
         KernelControllerContext context = controller.install(beanMetaData, target);
         checkContextState(context);         
         doTestAfterInstall(clazz, target);
      }
      finally
      {
         controller.uninstall(name);
      }
   }

   protected void checkContextState(KernelControllerContext context)
   {
      assertEquals(ControllerState.INSTALLED, context.getState());
   }

   /**
    * Useful for single tests.
    * Else determine the test by parameters.
    *
    * @param clazz the class
    * @param target the target
    */
   protected void doTestAfterInstall(Class<?> clazz, Object target)
   {
   }

   protected KernelController createController() throws Exception
   {
      // bootstrap
      KernelConfig config = createKernelConfig();
      BasicBootstrap bootstrap = config != null ? new BasicBootstrap(config) : new BasicBootstrap();
      bootstrap.run();
      Kernel kernel = bootstrap.getKernel();
      return kernel.getController();
   }

   protected KernelConfig createKernelConfig()
   {
      return null;
   }

   protected KernelController getController()
   {
      return controller;
   }
}
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
package org.jboss.test.kernel.config.test;

import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.config.support.XMLUtil;

/**
 * An abstract kernel config test.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelConfigTest extends AbstractKernelTest
{
   /** Whether we are an xml test */
   protected boolean xmltest = false;

   public AbstractKernelConfigTest(String name)
   {
      super(name);
   }

   public AbstractKernelConfigTest(String name, boolean xmltest)
   {
      super(name);
      this.xmltest = xmltest;
   }

   protected Kernel bootstrap() throws Throwable
   {
      if (xmltest)
         throw new RuntimeException("Not to be invoked from an xml test");
      return super.bootstrap();
   }

   protected XMLUtil bootstrapXML(boolean validate) throws Throwable
   {
      Kernel kernel = super.bootstrap();
      return new XMLUtil(kernel, this, validate);
   }

   protected Object instantiate(KernelConfigurator configurator, BeanInfo info) throws Throwable
   {
      Joinpoint joinPoint = configurator.getConstructorJoinPoint(info);
      return joinPoint.dispatch();
   }

   protected Object instantiateAndConfigure(BeanMetaData metaData) throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      return instantiateAndConfigure(configurator, metaData);
   }

   protected Object instantiateAndConfigure(KernelConfigurator configurator, BeanMetaData metaData) throws Throwable
   {
      Object result = instantiate(configurator, metaData);
      configure(configurator, result, metaData);
      return result;
   }

   protected Object instantiate(BeanMetaData metaData) throws Throwable
   {
      return instantiate(bootstrap().getController(), metaData);
   }

   protected Object instantiate(BeanMetaData metaData, ControllerState expectedState) throws Throwable
   {
      return instantiate(bootstrap().getController(), metaData, expectedState);
   }

   protected Object instantiate(KernelController controller, BeanMetaData metaData) throws Throwable
   {
      return instantiate(controller, metaData, null);
   }

   protected Object instantiate(KernelController controller, BeanMetaData metaData, ControllerState expectedState) throws Throwable
   {
      metaData.setMode(ControllerMode.AUTOMATIC);
      KernelControllerContext kernelControllerContext = controller.install(metaData);
      if (expectedState != null)
         assertEquals(expectedState, kernelControllerContext.getState());
      afterInstall(controller, kernelControllerContext);
      return kernelControllerContext.getTarget();
   }

   protected void afterInstall(KernelController controller, KernelControllerContext context) throws Throwable
   {
   }

   protected Object instantiate(KernelConfigurator configurator, BeanMetaData metaData) throws Throwable
   {
      Joinpoint joinPoint = configurator.getConstructorJoinPoint(metaData);
      return joinPoint.dispatch();
   }

   protected void configure(KernelConfigurator configurator, Object bean, BeanMetaData metaData) throws Throwable
   {
      BeanInfo info = configurator.getBeanInfo(metaData);
      configure(bean, info, metaData);
   }

   protected void configure(Object bean, BeanInfo info, BeanMetaData metaData) throws Throwable
   {
      if (info == null)
         throw new IllegalArgumentException("Null bean info");
      if (metaData == null)
         throw new IllegalArgumentException("Null bean meta data");

      Set<PropertyMetaData> propertys = metaData.getProperties();
      if (propertys != null && propertys.isEmpty() == false)
      {
         for (PropertyMetaData pmd : propertys)
         {
            PropertyInfo pi = info.getProperty(pmd.getName());
            ValueMetaData value = pmd.getValue();
            pi.set(bean, value.getValue(pi.getType(), Configurator.getClassLoader(metaData)));
         }
      }
   }

   protected void configure(Object bean, BeanInfo info, PropertyMetaData metaData) throws Throwable
   {
      ClassLoader cl = getClass().getClassLoader();
      PropertyInfo pi = info.getProperty(metaData.getName());
      pi.set(bean, metaData.getValue().getValue(pi.getType(), cl));
   }

   protected void unconfigure(Object bean, BeanInfo info) throws Throwable
   {
      Set<PropertyInfo> properties = info.getProperties();
      for (PropertyInfo pi : properties)
      {
         try
         {
            pi.set(bean, null);
         }
         catch (Throwable ignored)
         {
         }
      }
   }

   protected void configureLoggingAfterBootstrap()
   {
      //enableTrace("org.jboss.beans");
      //enableTrace("org.jboss.joinpoint.plugins.config");
      //enableTrace("org.jboss.kernel.plugins.config");
      //if (xmltest)
      //   enableTrace("org.jboss.xb");
   }
}

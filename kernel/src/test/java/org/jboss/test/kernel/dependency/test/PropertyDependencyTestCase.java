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
package org.jboss.test.kernel.dependency.test;

import java.util.HashSet;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimplerBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;

/**
 * Property Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class PropertyDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(PropertyDependencyTestCase.class);
   }

   public PropertyDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public PropertyDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testPropertyDependencyCorrectOrder() throws Throwable
   {
      propertyDependencyCorrectOrder();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimplerBean bean1 = (SimplerBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimplerBean bean2 = (SimplerBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
   }

   public void propertyDependencyCorrectOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testPropertyDependencyWrongOrder() throws Throwable
   {
      propertyDependencyWrongOrder();
      
      ControllerContext context2 = assertInstall(1, "Name2", ControllerState.INSTANTIATED);
      ControllerContext context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      SimplerBean bean1 = (SimplerBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimplerBean bean2 = (SimplerBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
   }

   public void propertyDependencyWrongOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testPropertyDependencyReinstall() throws Throwable
   {
      propertyDependencyReinstall();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimplerBean bean1 = (SimplerBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimplerBean bean2 = (SimplerBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());

      assertUninstall("Name1");

      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(ControllerState.INSTANTIATED, context2.getState());
      assertNotInstalled("Name2");

      context2 = assertContext("Name2", ControllerState.INSTANTIATED);
      
      context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());

      bean1 = (SimplerBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      bean2 = (SimplerBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
      
      assertUninstall("Name2");
      
      context1 = assertContext("Name1");
      bean1 = (SimplerBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      context2 = assertInstall(1, "Name2");
      
      bean1 = (SimplerBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      bean2 = (SimplerBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
   }

   public void propertyDependencyReinstall() throws Throwable
   {
      buildMetaData();
   }

   protected void buildMetaData()
   {
      AbstractBeanMetaData metaData1 = createName1();
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);

      AbstractBeanMetaData metaData2 = createName2();
      HashSet<PropertyMetaData> attributes2 = new HashSet<PropertyMetaData>();
      attributes2.add(new AbstractPropertyMetaData("string", new AbstractDependencyValueMetaData("Name1", "string")));
      metaData2.setProperties(attributes2);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   protected AbstractBeanMetaData createName1()
   {
      return new AbstractBeanMetaData("Name1", SimpleBeanImpl.class.getName());
   }

   protected AbstractBeanMetaData createName2()
   {
      return new AbstractBeanMetaData("Name2", SimpleBeanImpl.class.getName());
   }
}
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
package org.jboss.test.kernel.config.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.SimpleBeanFactory;
import org.jboss.test.kernel.config.support.MyBeanFactory;

/**
 * GenericFactory Instantiation Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class GenericFactoryInstantiateTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(GenericFactoryInstantiateTestCase.class);
   }

   public GenericFactoryInstantiateTestCase(String name)
   {
      super(name);
   }

   public GenericFactoryInstantiateTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   protected void assertFactory(BeanFactory factory)
         throws Throwable
   {
      assertNotNull(factory);
      SimpleBean bean1 = (SimpleBean)factory.createBean();
      assertNotNull(bean1);
      assertEquals(123, bean1.getAnint());
      SimpleBean bean2 = (SimpleBean)factory.createBean();
      assertNotNull(bean2);
      assertEquals(123, bean2.getAnint());
   }

   public void testBeanFactoryFromBean() throws Throwable
   {
      BeanFactory factory = configureFromBean();
      assertFactory(factory);
   }

   protected BeanFactory configureFromBean() throws Throwable
   {
      GenericBeanFactoryMetaData factory = new GenericBeanFactoryMetaData("Factory", SimpleBean.class.getName());
      factory.addBeanProperty(new AbstractPropertyMetaData("anint", 123));
      return (BeanFactory)instantiate(factory);
   }

   public void testBeanFactoryFromFactory() throws Throwable
   {
      BeanFactory factory = configureFromFactory();
      assertFactory(factory);
   }

   protected BeanFactory configureFromFactory() throws Throwable
   {
      GenericBeanFactoryMetaData factory = new GenericBeanFactoryMetaData("Factory");
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      factory.setBeanConstructor(constructor);
      constructor.setFactory(new AbstractValueMetaData(new SimpleBeanFactory()));
      constructor.setFactoryMethod("createSimpleBean");
      factory.addBeanProperty(new AbstractPropertyMetaData("anint", 123));
      return (BeanFactory)instantiate(factory);
   }

   public void testBeanFactoryFromStaticFactory() throws Throwable
   {
      BeanFactory factory = configureFromStaticFactory();
      assertFactory(factory);
   }

   protected BeanFactory configureFromStaticFactory() throws Throwable
   {
      GenericBeanFactoryMetaData factory = new GenericBeanFactoryMetaData("Factory");
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      factory.setBeanConstructor(constructor);
      constructor.setFactoryClass(SimpleBeanFactory.class.getName());
      constructor.setFactoryMethod("staticCreateSimpleBean");
      factory.addBeanProperty(new AbstractPropertyMetaData("anint", 123));
      return (BeanFactory)instantiate(factory);
   }

   public void testBeanFactoryWithNonExistingClass() throws Throwable
   {
      BeanFactory factory = configureFromIllegalClass();
      assertNotNull(factory);
      try
      {
         factory.createBean();
      }
      catch(Exception ex)
      {
         assertInstanceOf(ex, ClassNotFoundException.class);
      }
   }

   protected BeanFactory configureFromIllegalClass() throws Throwable
   {
      GenericBeanFactoryMetaData factory = new GenericBeanFactoryMetaData("Factory", "org.jboss.test.NoSuchClass");
      return (BeanFactory)instantiate(factory);
   }

   public void testBeanFactoryDefinedFactoryClass() throws Throwable
   {
      BeanFactory factory = configureFromDefinedFactoryClass();
      assertNotNull(factory);
      assertInstanceOf(factory, MyBeanFactory.class);
      assertEquals("foobar", factory.createBean());
   }

   protected BeanFactory configureFromDefinedFactoryClass() throws Throwable
   {
      GenericBeanFactoryMetaData factory = new GenericBeanFactoryMetaData("Factory", SimpleBean.class.getName());
      factory.setFactoryClass(MyBeanFactory.class.getName());
      return (BeanFactory)instantiate(factory);
   }
}

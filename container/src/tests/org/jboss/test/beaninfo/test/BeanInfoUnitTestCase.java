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
package org.jboss.test.beaninfo.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.test.beaninfo.support.BeanInfoAnnotatedGetterAndSetter;
import org.jboss.test.beaninfo.support.BeanInfoAnnotatedGetterAndSetterSimpleMerge;
import org.jboss.test.beaninfo.support.BeanInfoAnnotatedGetterOnly;
import org.jboss.test.beaninfo.support.BeanInfoAnnotatedSetterOnly;
import org.jboss.test.beaninfo.support.BeanInfoBooleanProperties;
import org.jboss.test.beaninfo.support.BeanInfoConstructors;
import org.jboss.test.beaninfo.support.BeanInfoEmpty;
import org.jboss.test.beaninfo.support.BeanInfoGenericGetterAndSetter;
import org.jboss.test.beaninfo.support.BeanInfoGenericGetterOnly;
import org.jboss.test.beaninfo.support.BeanInfoGenericInconsistentTypes;
import org.jboss.test.beaninfo.support.BeanInfoGenericSetterOnly;
import org.jboss.test.beaninfo.support.BeanInfoGetterAndSetter;
import org.jboss.test.beaninfo.support.BeanInfoGetterOnly;
import org.jboss.test.beaninfo.support.BeanInfoInconsistentTypes;
import org.jboss.test.beaninfo.support.BeanInfoSetterOnly;
import org.jboss.test.beaninfo.support.BeanInfoUpperPropertyName;

/**
 * BeanInfo Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 45663 $
 */
public class BeanInfoUnitTestCase extends AbstractBeanInfoTest
{
   public static Test suite()
   {
      return suite(BeanInfoUnitTestCase.class);
   }
   
   public BeanInfoUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testEmptyBean() throws Throwable
   {
      testBean(BeanInfoEmpty.class, null);
   }
   
   public void testBeanConstructors() throws Throwable
   {
      testBean(BeanInfoConstructors.class, null);
   }
   
   public void testBeanMethods() throws Throwable
   {
      testBean(BeanInfoConstructors.class, null);
   }
   
   public void testBeanGetterOnly() throws Throwable
   {
      testBean(BeanInfoGetterOnly.class, new String[] { "something" });
   }
   
   public void testBeanSetterOnly() throws Throwable
   {
      testBean(BeanInfoSetterOnly.class, new String[] { "something" });
   }
   
   public void testBeanGetterAndSetter() throws Throwable
   {
      testBean(BeanInfoGetterAndSetter.class, new String[] { "something" });
   }
   
   public void testBeanBooleanProperties() throws Throwable
   {
      testBean(BeanInfoBooleanProperties.class, new String[] { "something", "somethingElse" });
   }
   
   public void testBeanUpperPropertyName() throws Throwable
   {
      testBean(BeanInfoUpperPropertyName.class, new String[] { "MBean" });
   }
   
   public void testBeanInconsistentTypes() throws Throwable
   {
      testBean(BeanInfoInconsistentTypes.class, new String[] { "something" });
   }
   
   public void testBeanGenericGetterOnly() throws Throwable
   {
      testBean(BeanInfoGenericGetterOnly.class, new String[] { "something" });
   }
   
   public void testBeanGenericSetterOnly() throws Throwable
   {
      testBean(BeanInfoGenericSetterOnly.class, new String[] { "something" });
   }
   
   public void testBeanGenericGetterAndSetter() throws Throwable
   {
      testBean(BeanInfoGenericGetterAndSetter.class, new String[] { "something" });
   }
   
   public void testBeanGenericInconsistentTypes() throws Throwable
   {
      testBean(BeanInfoGenericInconsistentTypes.class, new String[] { "something" });
   }
   
   public void testBeanAnnotatedGetterOnly() throws Throwable
   {
      testBean(BeanInfoAnnotatedGetterOnly.class, new String[] { "something" });
   }
   
   public void testBeanAnnotatedSetterOnly() throws Throwable
   {
      testBean(BeanInfoAnnotatedSetterOnly.class, new String[] { "something" });
   }
   
   public void testBeanAnnotatedGetterAndSetter() throws Throwable
   {
      testBean(BeanInfoAnnotatedGetterAndSetter.class, new String[] { "something" });
   }
   
   public void testBeanAnnotatedGetterAndSetterSimpleMerge() throws Throwable
   {
      testBean(BeanInfoAnnotatedGetterAndSetterSimpleMerge.class, new String[] { "something" });
   }

   protected void testBean(Class clazz, String[] beanNames) throws Throwable
   {
      BeanInfo beanInfo = getBeanInfo(clazz);
      assertBeanInfo(beanInfo, clazz);
      if (beanNames != null)
      {
         Set<PropertyInfo> properties = beanInfo.getProperties();
         Set<String> props = new HashSet<String>();
         for (PropertyInfo p : properties)
            props.add(p.getName());
         Set<String> expected = new HashSet<String>();
         for (String beanName : beanNames)
            expected.add(beanName);
         expected.add("class");
         assertEquals(expected, props);
      }
   }
}
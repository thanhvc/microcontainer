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
package org.jboss.test.classloader;

import junit.framework.AssertionFailedError;
import junit.framework.TestSuite;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.test.BaseTestCase;
import org.jboss.test.classloader.support.MockClassLoaderPolicy;

/**
 * AbstractClassLoaderTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractClassLoaderTest extends BaseTestCase
{
   /**
    * Create a new testsuite for the class
    * 
    * TODO move to BaseTestCase
    * @param clazz the class
    * @return the suite
    */
   public static TestSuite suite(Class<?> clazz)
   {
      return new TestSuite(clazz);
   }

   /**
    * Raise an assertion failed error for an error
    * 
    * TODO move to AbstractTestCase
    * @param reason the reason
    * @param cause the cause
    */
   protected void failure(String reason, Throwable cause)
   {
      log.error(reason, cause);
      if (cause instanceof AssertionFailedError)
         throw (AssertionFailedError) cause;
      Error error = new AssertionFailedError(reason);
      error.initCause(cause);
      throw error;
   }
   
   public AbstractClassLoaderTest(String name)
   {
      super(name);
   }
   
   protected ClassLoaderSystem createClassLoaderSystem()
   {
      // We always create a new one to avoid things in the default domain leaking across tests
      return new DefaultClassLoaderSystem();
   }
   
   protected ClassLoaderSystem createClassLoaderSystemWithModifiedBootstrap()
   {
      ClassLoaderSystem result = createClassLoaderSystem();
      result.getDefaultDomain().setParentPolicy(ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      return result;
   }
   
   protected ClassLoader createClassLoaderSystemWithModifiedBootstrapAndMockClassLoader()
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      return createMockClassLoader(system);
   }
   
   protected ClassLoader registerPolicyWithDefaultDomain(ClassLoaderPolicy policy, ClassLoaderSystem system)
   {
      return system.registerClassLoaderPolicy(policy);
   }
   
   protected ClassLoader createMockClassLoader(ClassLoaderSystem system)
   {
      return createMockClassLoader(system, "mock");
   }
   
   protected ClassLoader createMockClassLoader(ClassLoaderSystem system, String name)
   {
      MockClassLoaderPolicy policy = new MockClassLoaderPolicy(name);
      return system.registerClassLoaderPolicy(policy);
   }
   
   protected ClassLoader createMockClassLoader(ClassLoaderSystem system, ClassLoaderDomain domain)
   {
      return createMockClassLoader(system, domain, "mock");
   }
   
   protected ClassLoader createMockClassLoader(ClassLoaderSystem system, ClassLoaderDomain domain, String name)
   {
      MockClassLoaderPolicy policy = new MockClassLoaderPolicy(name);
      return system.registerClassLoaderPolicy(domain, policy);
   }
   
   protected void assertClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected == actual);
   }
   
   protected void assertNoClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should NOT be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected != actual);
   }
   
   protected void assertClassLoader(Class<?> clazz, ClassLoader expected)
   {
      log.debug("Should be the expected classloader expected=" + expected + " actual=" + clazz.getClassLoader());
      assertEquals("Should be the expected classloader", expected, clazz.getClassLoader());
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start)
   {
      return assertLoadClass(reference, start, start, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, boolean isReference)
   {
      return assertLoadClass(reference, start, start, isReference);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected)
   {
      return assertLoadClass(reference, start, expected, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected, boolean isReference)
   {
      Class<?> result = assertLoadClass(reference.getName(), start, expected);
      if (isReference)
         assertClassEquality(reference, result);
      else
         assertNoClassEquality(reference, result);
      return result;
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start)
   {
      return assertLoadClass(name, start, start);
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start, ClassLoader expected)
   {
      Class<?> result = null;
      try
      {
         result = start.loadClass(name);
         log.debug("Got class: " + ClassLoaderUtils.classToString(result) + " for " + name + " from " + start);
      }
      catch (ClassNotFoundException e)
      {
         failure("Did not expect CNFE for " + name + " from " + start, e);
      }
      assertClassLoader(result, expected);
      return result;
   }
   
   protected void assertLoadClassFail(Class<?> reference, ClassLoader start)
   {
      assertLoadClassFail(reference.getName(), start);
   }
      
   protected void assertLoadClassFail(String name, ClassLoader start)
   {
      try
      {
         start.loadClass(name);
      }
      catch (Exception expected)
      {
         checkThrowable(ClassNotFoundException.class, expected);
      }
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start)
   {
      return assertClassForName(reference, start, start, false);
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start, boolean isReference)
   {
      return assertClassForName(reference, start, start, isReference);
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start, ClassLoader expected)
   {
      return assertClassForName(reference, start, expected, false);
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start, ClassLoader expected, boolean isReference)
   {
      Class<?> result = assertClassForName(reference.getName(), start, expected);
      if (isReference)
         assertClassEquality(reference, result);
      else
         assertNoClassEquality(reference, result);
      return result;
   }
   
   protected Class<?> assertClassForName(String name, ClassLoader start)
   {
      return assertLoadClass(name, start, start);
   }
   
   protected Class<?> assertClassForName(String name, ClassLoader start, ClassLoader expected)
   {
      Class<?> result = null;
      try
      {
         result = Class.forName(name, true, start);
         log.debug("Got class: " + ClassLoaderUtils.classToString(result) + " for " + name + " from " + start);
      }
      catch (ClassNotFoundException e)
      {
         failure("Did not expect CNFE for " + name + " from " + start, e);
      }
      assertClassLoader(result, expected);
      return result;
   }
   
   protected void assertClassForNameFail(Class<?> reference, ClassLoader start)
   {
      assertClassForNameFail(reference.getName(), start);
   }
      
   protected void assertClassForNameFail(String name, ClassLoader start)
   {
      try
      {
         Class.forName(name, true, start);
      }
      catch (Exception expected)
      {
         checkThrowable(ClassNotFoundException.class, expected);
      }
   }
   
   protected void configureLogging()
   {
      //enableTrace("org.jboss.classloader");
   }
}
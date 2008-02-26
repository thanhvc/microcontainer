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
package org.jboss.test.microcontainer.matrix.mc.test;


import junit.framework.Test;

import org.jboss.aop.Advised;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.matrix.AnnotatedChild;
import org.jboss.test.microcontainer.matrix.Base;
import org.jboss.test.microcontainer.matrix.Child;
import org.jboss.test.microcontainer.matrix.TestInterceptor;

/**
 * Only tested for MC since setting up the metadatacontext manually for the proxyfactory test is too cumbersome
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class BaseOnlyAdvisedChildAdvisedAndProxyTestCase extends AOPMicrocontainerTest
{
   public void testAdvisedBaseClass() throws Exception
   {
      Base base = (Base)getBean("Base");
      assertTrue(base instanceof Advised);
      assertFalse(base instanceof AspectManaged);
      
      Child plainChild = (Child)getBean("PlainChild");
      assertTrue(plainChild instanceof Advised);
      assertFalse(plainChild instanceof AspectManaged);
      
      Child proxiedChild = (Child)getBean("ProxiedChild");
      assertTrue(proxiedChild instanceof Advised);
      assertTrue(proxiedChild instanceof AspectManaged);

      TestInterceptor.reset();
      base.baseOnly();
      assertEquals(1, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOnly", TestInterceptor.invoked.getName());
      assertNull(TestInterceptor.classAnnotation);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);

      TestInterceptor.reset();
      base.baseOverridden();
      assertEquals(0, TestInterceptor.interceptions);
   }
   
   public void testAdvisedChild() throws Exception
   {
      Child plainChild = (Child)getBean("PlainChild");
      assertTrue(plainChild instanceof Advised);
      assertFalse(plainChild instanceof AspectManaged);
      
      TestInterceptor.reset();
      plainChild.baseOnly();
      assertEquals(1, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOnly", TestInterceptor.invoked.getName());
      assertNull(TestInterceptor.classAnnotation);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);

      TestInterceptor.reset();
      plainChild.childOnly();
      assertEquals(0, TestInterceptor.interceptions);

      TestInterceptor.reset();
      plainChild.baseOverridden();
      assertEquals(0, TestInterceptor.interceptions);
   }
   
   private static final int EXPECTED_ANNNOTATED_CHILD_BASEONLY = 2;
   private static final int EXPECTED_ANNNOTATED_CHILD_ONLY = 1;
   private static final int EXPECTED_ANNNOTATED_CHILD_BASEOVERRIDDEN = 1;

   public void testProxiedAndAdvisedChild() throws Exception
   {
      Child proxiedChild = (Child)getBean("ProxiedChild");
      assertTrue(proxiedChild instanceof Advised);
      assertTrue(proxiedChild instanceof AspectManaged);

      TestInterceptor.reset();
      proxiedChild.baseOnly();
      //Two bindings will apply to this method
      assertEquals(EXPECTED_ANNNOTATED_CHILD_BASEONLY, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOnly", TestInterceptor.invoked.getName());
      assertNotNull(TestInterceptor.classAnnotation);
      assertTrue(TestInterceptor.classAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);

      TestInterceptor.reset();
      proxiedChild.childOnly();
      assertEquals(EXPECTED_ANNNOTATED_CHILD_ONLY, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("childOnly", TestInterceptor.invoked.getName());
      assertNotNull(TestInterceptor.classAnnotation);
      assertTrue(TestInterceptor.classAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);

      TestInterceptor.reset();
      proxiedChild.baseOverridden();
      assertEquals(EXPECTED_ANNNOTATED_CHILD_BASEOVERRIDDEN, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOverridden", TestInterceptor.invoked.getName());
      assertNotNull(TestInterceptor.classAnnotation);
      assertTrue(TestInterceptor.classAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);
   }
   
   public void testAnnotatedChild() throws Exception
   {
      AnnotatedChild annotatedChild = (AnnotatedChild)getBean("AnnotatedChild");
      assertTrue(annotatedChild instanceof Advised);
      assertFalse(annotatedChild instanceof AspectManaged);

      TestInterceptor.reset();
      annotatedChild.baseOnly();
      //Two bindings will apply to this method
      assertEquals(EXPECTED_ANNNOTATED_CHILD_BASEONLY, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOnly", TestInterceptor.invoked.getName());
      assertNotNull(TestInterceptor.classAnnotation);
      assertTrue(TestInterceptor.classAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);

      TestInterceptor.reset();
      annotatedChild.childOnly();
      assertEquals(EXPECTED_ANNNOTATED_CHILD_ONLY, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("childOnly", TestInterceptor.invoked.getName());
      assertNotNull(TestInterceptor.classAnnotation);
      assertTrue(TestInterceptor.classAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.methodAnnotation);

      assertNull(TestInterceptor.metadata);
      TestInterceptor.reset();
      annotatedChild.baseOverridden();
      assertEquals(EXPECTED_ANNNOTATED_CHILD_BASEOVERRIDDEN, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOverridden", TestInterceptor.invoked.getName());
      assertNotNull(TestInterceptor.classAnnotation);
      assertTrue(TestInterceptor.classAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);
   }
   
   public static Test suite()
   {
      return suite(BaseOnlyAdvisedChildAdvisedAndProxyTestCase.class);
   }

   public BaseOnlyAdvisedChildAdvisedAndProxyTestCase(String name)
   {
      super(name);
   }
}

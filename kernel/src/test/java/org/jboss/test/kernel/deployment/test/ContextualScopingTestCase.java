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
package org.jboss.test.kernel.deployment.test;

import junit.framework.Test;
import org.jboss.test.kernel.deployment.support.FromAppBean;
import org.jboss.test.kernel.deployment.support.FromBootBean;
import org.jboss.test.kernel.deployment.support.ObjectWithFromAppBean;
import org.jboss.test.kernel.deployment.support.ObjectWithFromBootBean;
import org.jboss.test.kernel.deployment.support.SimpleBean;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;

/**
 * Contextual scoping tests.
 * Test includes same named beans.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ContextualScopingTestCase extends ScopingDeploymentTest
{
   public ContextualScopingTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ContextualScopingTestCase.class);
   }

   // ---- tests

   public void testSimpleScoping() throws Throwable
   {
      ClassLoader cl = (ClassLoader) getBean("cl");
      assertNotNull(cl);

      SimpleObjectWithBean appScopeObject = (SimpleObjectWithBean) getBean("appScopeObject");
      assertNotNull(appScopeObject);

      SimpleObjectWithBean deploy1 = (SimpleObjectWithBean) getBean("deploy1");
      assertNotNull(deploy1);
      SimpleBean simple1 = deploy1.getSimpleBean();
      assertNotNull(simple1);
      assertEquals("deployment1", simple1.getConstructorString());

      SimpleObjectWithBean deploy2 = (SimpleObjectWithBean) getBean("deploy2");
      assertNotNull(deploy2);
      SimpleBean simple2 = deploy2.getSimpleBean();
      assertNotNull(simple2);
      assertEquals("deployment2", simple2.getConstructorString());

      ObjectWithFromBootBean deploy3 = (ObjectWithFromBootBean)getBean("deploy3");
      assertNotNull(deploy3);
      FromBootBean simple3 = deploy3.getSimpleBean();
      assertNotNull(simple3);
      assertEquals("fromBoot", simple3.getConstructorString());

      ObjectWithFromAppBean deploy4 = (ObjectWithFromAppBean)getBean("deploy4");
      assertNotNull(deploy4);
      FromAppBean simple4 = deploy4.getSimpleBean();
      assertNotNull(simple4);
      assertEquals("fromApp", simple4.getConstructorString());
   }

}

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
package org.jboss.test.kernel.deployment.xml.test;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.policy.PolicyMetaData;
import org.jboss.beans.metadata.spi.policy.ScopeMetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopeJaxbTestCase extends AbstractPolicyTest
{
   public ScopeJaxbTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ScopeJaxbTestCase.class);
   }

   public void testScope() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      ScopeMetaData scope = policy.getScope();
      assertNull(scope.getLevel());
      assertNull(scope.getQualifier());
   }

   public void testScopeWithLevel() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      ScopeMetaData scope = policy.getScope();
      assertNotNull(scope.getLevel());
      assertEquals("DefaultLevel", scope.getLevel());
      assertNull(scope.getQualifier());
   }

   public void testScopeWithQualifier() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      ScopeMetaData scope = policy.getScope();
      assertNull(scope.getLevel());
      assertNotNull(scope.getQualifier());
      assertEquals("SimpleQualifier", scope.getQualifier());
   }

}

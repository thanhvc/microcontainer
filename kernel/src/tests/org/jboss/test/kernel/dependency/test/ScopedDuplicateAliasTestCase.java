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
package org.jboss.test.kernel.dependency.test;

import java.util.Set;
import java.util.HashSet;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.metadata.plugins.scope.DeploymentScope;

/**
 * Duplicate alias tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopedDuplicateAliasTestCase extends DuplicateAliasTestCase
{
   public ScopedDuplicateAliasTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected ScopedDuplicateAliasTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(ScopedDuplicateAliasTestCase.class);
   }

   protected void addAnnotation(AbstractBeanMetaData bmd)
   {
      Set<AnnotationMetaData> annotations = new HashSet<AnnotationMetaData>();
      annotations.add(new AbstractAnnotationMetaData("@" + DeploymentScope.class.getName() + "(\"somedeployment\")"));
      bmd.setAnnotations(annotations);
   }
}
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
package org.jboss.test.kernel.annotations.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.kernel.plugins.annotations.FieldAnnotationPlugin;
import org.jboss.reflect.spi.FieldInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class MockInjectPlugin extends FieldAnnotationPlugin<MockInject>
{
   public static MockInjectPlugin INSTANCE = new MockInjectPlugin();

   private Set<String> fieldNames = new HashSet<String>();

   private MockInjectPlugin()
   {
      super(MockInject.class);
   }

   public ValueMetaData createValueMetaData(MockInject annotation)
   {
      return new AbstractValueMetaData();
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(FieldInfo info, MockInject annotation, BeanMetaData beanMetaData) throws Throwable
   {
      fieldNames.add(info.getName());
      return null;
   }

   public Set<String> getFieldNames()
   {
      return fieldNames;
   }

   public void clear()
   {
      fieldNames.clear();
   }
}

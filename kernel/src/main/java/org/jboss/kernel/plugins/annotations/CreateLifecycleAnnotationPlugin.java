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
package org.jboss.kernel.plugins.annotations;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.api.annotations.Create;

/**
 * Create annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CreateLifecycleAnnotationPlugin extends LifecycleParameterAnnotationPlugin<Create>
{
   public CreateLifecycleAnnotationPlugin(Set<Annotation2ValueMetaDataAdapter<? extends Annotation>> adapters)
   {
      super(Create.class, adapters);
   }

   protected boolean isLifecyclePresent(BeanMetaData beanMetaData)
   {
      return beanMetaData.getCreate() != null;
   }

   protected void applyLifecycleAnnotation(AbstractLifecycleMetaData lifecycle, Create annotation)
   {
      lifecycle.setIgnored(annotation.ignored());
   }

   protected void setLifecycleMetaData(AbstractBeanMetaData beanMetaData, AbstractLifecycleMetaData lifecycle)
   {
      lifecycle.setType("create");
      beanMetaData.setCreate(lifecycle);      
   }
}

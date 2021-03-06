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
package org.jboss.test.kernel.dependency.support;

import org.jboss.beans.metadata.api.annotations.Create;
import org.jboss.beans.metadata.api.annotations.Start;
import org.jboss.beans.metadata.api.annotations.Stop;
import org.jboss.beans.metadata.api.annotations.Destroy;
import org.jboss.beans.metadata.api.annotations.Inject;

/**
 * A simple bean with a complicated lifecycle
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotatedSimpleBeanWithComplicatedLifecycle extends SimpleBeanWithComplicatedLifecycle
{
   private static final long serialVersionUID = 3258132440433243443L;

   @Create
   public void notCreate(@Inject(bean = "Name1") SimpleBeanWithLifecycle bean)
   {
      super.notCreate(bean);
   }

   @Start
   public void notStart(@Inject(bean = "Name2")SimpleBeanWithLifecycle bean)
   {
      super.notStart(bean);
   }

   @Stop
   public void notStop(@Inject(bean = "Name3")SimpleBeanWithLifecycle bean)
   {
      super.notStop(bean);
   }

   @Destroy
   public void notDestroy(@Inject(bean = "Name4")SimpleBeanWithLifecycle bean)
   {
      super.notDestroy(bean);
   }
}

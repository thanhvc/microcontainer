/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.aop.microcontainer.beans;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.util.id.GUID;

/**
 * Abstract base class for things that can go inside bindings (stack refs, advice and interceptor-ref)
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class BindingEntry
{
   String name = new GUID().toString();
   AspectManager manager;
   Binding binding;

   public abstract InterceptorFactory[] getInterceptorFactories();
   
   public abstract void start();
   
   public abstract void stop();

   public Binding getBinding()
   {
      return binding;
   }

   public void setBinding(Binding binding)
   {
      this.binding = binding;
   }

   public AspectManager getManager()
   {
      return manager;
   }

   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   public String getName()
   {
      return name;
   }
   

}

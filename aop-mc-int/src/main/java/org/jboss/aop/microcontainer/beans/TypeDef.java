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
import org.jboss.aop.pointcut.Typedef;
import org.jboss.aop.pointcut.TypedefExpression;
import org.jboss.aop.pointcut.ast.ParseException;

/**
 * Installs a typedef into the underlying aspect manager
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class TypeDef
{
   private AspectManager manager;
   
   private String name;
   
   private String expr;

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

   public void setName(String name)
   {
      this.name = name;
   }

   public String getExpr()
   {
      return expr;
   }

   public void setExpr(String expr)
   {
      this.expr = expr;
   }
   
   public void start()
   {
      if (manager == null)
      {
         throw new IllegalArgumentException("No manager");
      }
      if (name == null || name.length() == 0)
      {
         throw new IllegalArgumentException("No name");
      }
      if (expr == null || expr.length() == 0)
      {
         throw new IllegalArgumentException("No expr");
      }
      
      try
      {
         Typedef typedef = new TypedefExpression(name, expr);
         manager.addTypedef(typedef);
      }
      catch(ParseException e)
      {
         throw new IllegalArgumentException("");
      }
      catch(Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   public void stop()
   {
      manager.removeTypedef(name);
   }
}

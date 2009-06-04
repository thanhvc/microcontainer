/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.spi;

import java.util.Iterator;

import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * A metadata vistor node. Each type of metadata in this package implements
 * this interface and knows how to augment the {@link KernelControllerContext}
 * with the information contained.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public interface MetaDataVisitorNode
{
   /**
    * Visit the node before any classloading has been set up.
    * 
    * @param vistor the visitor
    */
   public void initialVisit(MetaDataVisitor vistor);
   
   /**
    * Revisit the node once classloading has been set up.
    *
    * @param vistor the visitor
    */
   public void describeVisit(MetaDataVisitor vistor);

   /**
    * Return the child nodes
    * 
    * @return Iterator<MetaDataVisitorNode> or null if there aren't any
    */
   public Iterator<? extends MetaDataVisitorNode> getChildren();

   /**
    * Clone the object
    *
    * @return a clone of the object
    */
   Object clone();
}

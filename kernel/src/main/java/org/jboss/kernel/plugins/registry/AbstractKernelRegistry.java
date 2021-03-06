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
package org.jboss.kernel.plugins.registry;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.kernel.plugins.AbstractKernelObject;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntryNotFoundException;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;

/**
 * Abstract Kernel registry.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public abstract class AbstractKernelRegistry extends AbstractKernelObject implements org.jboss.kernel.spi.registry.KernelRegistry
{
   /** The registry factories */
   protected List<KernelRegistryPlugin> factories = new CopyOnWriteArrayList<KernelRegistryPlugin>();

   /**
    * Create an abstract kernel registry
    * 
    * @throws Exception for any error
    */
   public AbstractKernelRegistry() throws Exception
   {
   }

   /**
    * Add a kernel registry factory
    * 
    * @param factory the factory to add
    */
   public void addKernelRegistryFactory(KernelRegistryPlugin factory)
   {
      factories.add(factory);
      if (log.isTraceEnabled())
         log.trace("Registry " + this + " added registry factory " + factory);
   }

   /**
    * Remove a kernel registry factory
    * 
    * @param factory the factory to remove
    */
   public void removeKernelRegistryFactory(KernelRegistryPlugin factory)
   {
      factories.remove(factory);
      if (log.isTraceEnabled())
         log.trace("Registry " + this + " removed registry factory " + factory);
   }

   public KernelRegistryEntry findEntry(Object name)
   {
      for (ListIterator<KernelRegistryPlugin> i = factories.listIterator(); i.hasNext();)
      {
         KernelRegistryPlugin factory = i.next();
         KernelRegistryEntry entry = factory.getEntry(name);
         if (entry != null)
            return entry;
      }
      return null;
   }

   public KernelRegistryEntry getEntry(Object name)
   {
      KernelRegistryEntry entry = findEntry(name);
      if (entry == null)
         throw new KernelRegistryEntryNotFoundException("Entry not found with name: " + name);

      return entry;
   }

   public boolean containsEntry(Object name)
   {
      return findEntry(name) != null;
   }
}

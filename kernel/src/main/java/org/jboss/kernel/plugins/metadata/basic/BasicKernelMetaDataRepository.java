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
package org.jboss.kernel.plugins.metadata.basic;

import java.util.List;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.kernel.plugins.metadata.AbstractKernelMetaDataRepository;
import org.jboss.metadata.plugins.repository.basic.BasicMetaDataRepository;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * BasicKernelMetaDataRepository.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BasicKernelMetaDataRepository extends AbstractKernelMetaDataRepository
{
   /**
    * Create a new BasicKernelMetaDataRepository.
    */
   public BasicKernelMetaDataRepository()
   {
      super(new BasicMetaDataRepository());
   }

   public MetaData getMetaData(ControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeKey scope = context.getScopeInfo().getScope();
      MetaData metaData = repository.getMetaData(scope);
      if (metaData == null)
      {
         initMetaDataRetrieval(context);
         metaData = repository.getMetaData(scope);
         if (metaData == null)
            throw new IllegalStateException("Error initialising metadata state: " + scope);
      }
      return metaData;
   }

   public void addMetaData(ControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeInfo scopeInfo = context.getScopeInfo();
      scopeInfo.addMetaData(repository, context);
   }

   public void removeMetaData(ControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeInfo scopeInfo = context.getScopeInfo();
      scopeInfo.removeMetaData(repository, context);
   }

   public ScopeKey getFullScope(ControllerContext context)
   {
      return context.getScopeInfo().getScope();
   }

   public ScopeKey getMutableScope(ControllerContext context)
   {
      return context.getScopeInfo().getMutableScope();
   }

   public MetaDataRetrieval createMetaDataRetrieval(ControllerContext context, List<MetaDataRetrieval> retrievals)
   {
      return createMetaDataRetrieval(retrievals);
   }

   protected MetaDataRetrieval createMetaDataRetrieval(List<MetaDataRetrieval> retrievals)
   {
      return null;
   }

   /**
    * Initialise metadata retrieval
    * 
    * @param context the context
    * @return the retrieval
    */
   protected MetaDataRetrieval initMetaDataRetrieval(ControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeInfo scopeInfo = context.getScopeInfo();
      return scopeInfo.initMetaDataRetrieval(repository, context);
   }
}

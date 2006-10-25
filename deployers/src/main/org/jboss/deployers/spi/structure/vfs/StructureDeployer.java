/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.deployers.spi.structure.vfs;

import org.jboss.deployers.spi.OrderedDeployer;
import org.jboss.virtual.VirtualFile;

/**
 * A StructureDeployer translates a deployment virtual file root into
 * StructureMetaData representing the deployment contexts.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface StructureDeployer extends OrderedDeployer
{
   /**
    * Determine the structure of a deployment
    * 
    * @param file - the candidate root file of the deployment
    * @param metaData - the structure metadata to build
    * @param deployers - the available structure deployers
    * @return true when it is recongnised
    */
   boolean determineStructure(VirtualFile file, StructureMetaData metaData, StructuredDeployers deployers);

}

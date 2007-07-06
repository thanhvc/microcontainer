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
package org.jboss.deployers.vfs.plugins.structure.war;

import java.io.IOException;
import java.util.List;

import org.jboss.deployers.spi.structure.ContextInfo;
import org.jboss.deployers.spi.structure.StructureMetaData;
import org.jboss.deployers.vfs.spi.structure.VFSStructuralDeployers;
import org.jboss.deployers.vfs.spi.structure.helpers.AbstractStructureDeployer;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VirtualFileFilter;
import org.jboss.virtual.VisitorAttributes;
import org.jboss.virtual.plugins.vfs.helpers.SuffixMatchFilter;

/**
 * WARStructure.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class WARStructure extends AbstractStructureDeployer
{
   /** The default filter which allows jars/jar directories */
   public static final VirtualFileFilter DEFAULT_WEB_INF_LIB_FILTER = new SuffixMatchFilter(".jar", VisitorAttributes.DEFAULT);
   
   /** The web-inf/lib filter */
   private VirtualFileFilter webInfLibFilter = DEFAULT_WEB_INF_LIB_FILTER;

   /**
    * Sets the default relative order 1000.
    *
    */
   public WARStructure()
   {
      setRelativeOrder(1000);
   }

   /**
    * Get the webInfLibFilter.
    * 
    * @return the webInfLibFilter.
    */
   public VirtualFileFilter getWebInfLibFilter()
   {
      return webInfLibFilter;
   }

   /**
    * Set the webInfLibFilter.
    * 
    * @param webInfLibFilter the webInfLibFilter.
    * @throws IllegalArgumentException for a null filter
    */
   public void setWebInfLibFilter(VirtualFileFilter webInfLibFilter)
   {
      if (webInfLibFilter == null)
         throw new IllegalArgumentException("Null filter");
      this.webInfLibFilter = webInfLibFilter;
   }

   public boolean determineStructure(VirtualFile root, VirtualFile parent, VirtualFile file, StructureMetaData metaData, VFSStructuralDeployers deployers)
   {
      ContextInfo context = null;
      try
      {
         if (isLeaf(file) == false)
         {
            // We require either a WEB-INF or the name ends in .war
            if (file.getName().endsWith(".war") == false)
            {
               try
               {
                  file.findChild("WEB-INF");
                  log.trace("... ok - directory has a WEB-INF subdirectory");
               }
               catch (IOException e)
               {
                  log.trace("... no - doesn't look like a war and no WEB-INF subdirectory.");
                  return false;
               }
            }
            else
            {
               log.trace("... ok - name ends in .war.");
            }

            // Create a context for this war file with WEB-INF as the location for metadata  
            context = createContext(file, "WEB-INF", metaData);

            // Add the war manifest classpath entries
            addClassPath(root, file, false, true, context);
            try
            {
               // The classpath is WEB-INF/classes
               VirtualFile classes = file.findChild("WEB-INF/classes");
               // Add the war manifest classpath entries
               addClassPath(root, classes, true, false, context);               
            }
            catch(IOException e)
            {
               log.trace("No WEB-INF/classes for: " + file.getPathName());               
            }
            // and the top level jars in WEB-INF/lib
            try
            {
               VirtualFile webinfLib = file.findChild("WEB-INF/lib");
               List<VirtualFile> archives = webinfLib.getChildren(webInfLibFilter);
               for (VirtualFile jar : archives)
                  addClassPath(root, jar, true, true, context);
            }
            catch (IOException ignored)
            {
               log.trace("No WEB-INF/lib for: " + file.getPathName());
            }

            // There are no subdeployments for wars
            return true;
         }
         else
         {
            log.trace("... no - not a directory or an archive.");
            return false;
         }
      }
      catch (Exception e)
      {
         log.warn("Error determining structure: " + file.getName(), e);
         // Remove the invalid context
         if (context != null)
            metaData.removeContext(context);
         return false;
      }
   }
}
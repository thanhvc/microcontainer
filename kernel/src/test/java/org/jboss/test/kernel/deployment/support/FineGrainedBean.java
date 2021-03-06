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
package org.jboss.test.kernel.deployment.support;

import org.jboss.kernel.spi.dependency.ConfigureKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.CreateKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.InstallKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.InstantiateKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.StartKernelControllerContextAware;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
@SuppressWarnings("deprecation")
public class FineGrainedBean implements
      org.jboss.kernel.spi.dependency.DescribeKernelControllerContextAware, InstantiateKernelControllerContextAware,
      ConfigureKernelControllerContextAware, CreateKernelControllerContextAware,
      StartKernelControllerContextAware, InstallKernelControllerContextAware
{
   private int index;

   public static String[] states = {
         "NOT_INSTALLED (DESCRIBED)",
         "INSTANTIATED",
         "CONFIGURED",
         "CREATE",
         "START",
         "INSTALLED",
   };

   public String getStateString()
   {
      return states[index];
   }

   public void setKernelControllerContext(KernelControllerContext context) throws Exception
   {
      ++index;
   }

   public void unsetKernelControllerContext(KernelControllerContext context) throws Exception
   {
      --index;
   }

}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.classloader.spi.base;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.jboss.classloader.spi.Loader;

/**
 * Base DelegateLoader.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class BaseDelegateLoader implements Loader
{
   /** The delegate loader policy */
   private BaseClassLoaderPolicy delegate;

   /**
    * Create a new BaseDelegateLoader.
    * 
    * @param delegate the delegate
    * @throws IllegalArgumentException for a null delegate
    */
   public BaseDelegateLoader(BaseClassLoaderPolicy delegate)
   {
      if (delegate == null)
         throw new IllegalArgumentException("Null delegate");
      this.delegate = delegate;
   }
   
   BaseClassLoaderPolicy getPolicy()
   {
      return delegate;
   }
   
   public Class<?> loadClass(String className)
   {
      BaseClassLoader classLoader = delegate.getClassLoader();
      // REVIEW: Should probably add some kind of warning here
      if (classLoader == null)
         return null;
      return classLoader.loadClassLocally(className);
   }
   
   public URL getResource(String name, String resourceName)
   {
      BaseClassLoader classLoader = delegate.getClassLoader();
      // REVIEW: Should probably add some kind of warning here
      if (classLoader == null)
         return null;
      return classLoader.getResourceLocally(name, resourceName);
   }

   public void getResources(String name, String resourceName, Set<URL> urls) throws IOException
   {
      BaseClassLoader classLoader = delegate.getClassLoader();
      // REVIEW: Should probably add some kind of warning here
      if (classLoader == null)
         return;
      classLoader.getResourcesLocally(name, resourceName, urls);
   }

   /**
    * A long version of toString()
    * 
    * @return the long string
    */
   public String toLongString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@").append(Integer.toHexString(System.identityHashCode(this)));
      builder.append("{delegate=").append(delegate.toLongString());
      toLongString(builder);
      builder.append('}');
      return builder.toString();
   }
   
   /**
    * For subclasses to add information for toLongString()
    * 
    * @param builder the builder
    */
   protected void toLongString(StringBuilder builder)
   {
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@").append(Integer.toHexString(System.identityHashCode(this)));
      builder.append("{delegate=").append(delegate);
      builder.append('}');
      return builder.toString();
   }
}
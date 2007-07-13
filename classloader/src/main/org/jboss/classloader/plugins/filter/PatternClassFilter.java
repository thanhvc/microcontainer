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
package org.jboss.classloader.plugins.filter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.classloader.spi.filter.ClassFilter;

/**
 * A class filter using regular expressions
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PatternClassFilter implements ClassFilter
{
   /** The class patterns as regular expressions */
   private Pattern[] classPatterns;

   /** The resource patterns as regular expressions */
   private Pattern[] resourcePatterns;
   
   /**
    * Create a new PatternClassFilter.
    * 
    * @param classPatterns the class patterns
    * @param resourcePatterns the resource patterns
    * @throws IllegalArgumentException for a null pattern
    */
   public PatternClassFilter(String[] classPatterns, String[] resourcePatterns)
   {
      if (classPatterns == null)
         throw new IllegalArgumentException("Null patterns");
      
      this.classPatterns = new Pattern[classPatterns.length];
      for (int i = 0; i < classPatterns.length; ++i)
      {
         if (classPatterns[i] == null)
            throw new IllegalArgumentException("Null pattern in " + Arrays.asList(classPatterns));
         this.classPatterns[i] = Pattern.compile(classPatterns[i]);
      }

      if (resourcePatterns == null)
      {
         this.resourcePatterns = this.classPatterns;
         return;
      }
      
      this.resourcePatterns = new Pattern[resourcePatterns.length];
      for (int i = 0; i < resourcePatterns.length; ++i)
      {
         if (resourcePatterns[i] == null)
            throw new IllegalArgumentException("Null pattern in " + Arrays.asList(resourcePatterns));
         this.resourcePatterns[i] = Pattern.compile(resourcePatterns[i]);
      }
   }

   public boolean matchesClassName(String className)
   {
      if (className == null)
         return false;
      
      for (int i = 0; i < classPatterns.length; ++i)
      {
         Matcher matcher = classPatterns[i].matcher(className);
         if (matcher.matches())
            return true;
      }
      return false;
   }

   public boolean matchesResourcePath(String resourcePath)
   {
      if (resourcePath == null)
         return false;
      
      for (int i = 0; i < resourcePatterns.length; ++i)
      {
         Matcher matcher = resourcePatterns[i].matcher(resourcePath);
         if (matcher.matches())
            return true;
      }
      return false;
   }

   @Override
   public String toString()
   {
      return Arrays.asList(classPatterns).toString();
   }
}

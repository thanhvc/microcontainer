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
package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * The parameter.
 * We must distingush between @Value and @Parameter,
 * since annotations don't allow cyclic dependencies.
 * ValueFactory uses @Parameter to break the cycle.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Parameter
{
   /**
    * Get they parameter type.
    *
    * @return the type
    */
   Class<?> type() default void.class;

   /**
    * Get the string value.
    *
    * @return the string value
    */
   StringValue string() default @StringValue(value="");

   /**
    * Get inject value.
    *
    * @return the inject value
    */
   Inject inject() default @Inject(valid=false);

   /**
    * Get this value.
    *
    * @return this value
    */
   ThisValue thisValue() default @ThisValue(valid = false);

   /**
    * Get null value.
    *
    * @return null value
    */
   NullValue nullValue() default @NullValue(valid = false);

   /**
    * Get java bean value.
    *
    * @return java bean value
    */
   JavaBeanValue javabean() default @JavaBeanValue;
}

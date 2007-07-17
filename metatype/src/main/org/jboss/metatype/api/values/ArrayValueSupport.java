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
package org.jboss.metatype.api.values;

import java.util.Arrays;

import org.jboss.metatype.api.types.ArrayMetaType;

/**
 * ArrayValue.
 * 
 * TODO tests
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ArrayValueSupport extends AbstractMetaValue implements ArrayValue
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1131827130033538066L;

   /** The array meta type */
   private ArrayMetaType metaType;
   
   /** The value */
   private Object[] value;
   
   /**
    * Create a new ArrayValueSupport.
    * 
    * @param metaType the array meta type
    * @throws IllegalArgumentException for a null array MetaType
    */
   public ArrayValueSupport(ArrayMetaType metaType)
   {
      if (metaType == null)
         throw new IllegalArgumentException("Null array meta type");
      this.metaType = metaType;
   }
   
   /**
    * Create a new ArrayValueSupport.
    * 
    * @param metaType the array meta type
    * @param value the value
    * @throws IllegalArgumentException for a null array MetaType
    */
   public ArrayValueSupport(ArrayMetaType metaType, Object[] value)
   {
      this(metaType);
      this.value = value;
   }

   public ArrayMetaType getMetaType()
   {
      return metaType;
   }

   /**
    * Get the value.
    * 
    * @return the value.
    */
   public Object[] getValue()
   {
      return value;
   }

   /**
    * Set the value.
    * 
    * @param value the value.
    */
   public void setValue(Object[] value)
   {
      this.value = value;
   } 

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      
      if (obj == null || obj instanceof ArrayValue == false)
         return false;

      ArrayValue other = (ArrayValue) obj;
      if (metaType.equals(other.getMetaType()) == false)
         return false;

      Object[] otherValue = other.getValue();
      if (value == null && otherValue == null)
         return true;
      if (value == null && otherValue != null)
         return false;
      return Arrays.deepEquals(value, otherValue);
   }
   
   @Override
   public int hashCode()
   {
      if (value == null)
         return 0;
      return value.hashCode();
   }

   @Override
   public String toString()
   {
      return metaType + ":" + Arrays.deepToString(value);
   }

   @Override
   public MetaValue clone()
   {
      ArrayValueSupport result = (ArrayValueSupport) super.clone();

      if (value != null && value.length > 0)
      {
         result.value = new Object[value.length];
         System.arraycopy(value, 0, value, 0, value.length);
      }
      return result;
   }
}
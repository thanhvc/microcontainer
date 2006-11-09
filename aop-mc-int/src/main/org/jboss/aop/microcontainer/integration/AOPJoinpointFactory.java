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
package org.jboss.aop.microcontainer.integration;

import org.jboss.joinpoint.plugins.BasicConstructorJoinPoint;
import org.jboss.joinpoint.plugins.BasicJoinpointFactory;
import org.jboss.joinpoint.spi.ConstructorJoinpoint;
import org.jboss.joinpoint.spi.JoinpointException;
import org.jboss.reflect.plugins.introspection.IntrospectionTypeInfoFactory;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.TypeInfoFactory;
import org.jboss.repository.spi.MetaDataContext;

/**
 * The existence of this class is the signal to the kernel that we want to use the aop-mc integration.
 * When deployed in jboss the AOPConstructorJoinpoint will be deployed as part of the AspectDeployer,
 * so we use "normal" constructor joinpoints until that has been deployed
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPJoinpointFactory extends BasicJoinpointFactory
{
   /** The AOPConstructorJoinpoint constructor one it has been loaded */
   static volatile ConstructorInfo ctorInfo; 
   
   /**
    * Create a new AOPJoinpointFactory.
    * 
    * @param classInfo
    */
   public AOPJoinpointFactory(ClassInfo classInfo, MetaDataContext metaDataContext)
   {
      super(classInfo, metaDataContext);
   }

   public ConstructorJoinpoint getConstructorJoinpoint(ConstructorInfo constructorInfo) throws JoinpointException
   {
      ConstructorInfo info = getAOPJoinpointConstructorInfo(constructorInfo);
      
      if (info != null)
      {
         return createAOPConstructorJoinpoint(info, constructorInfo);
      }
      else
      {
         return super.getConstructorJoinpoint(constructorInfo);
      }
   }
   
   private synchronized ConstructorInfo getAOPJoinpointConstructorInfo(ConstructorInfo currentConstructorInfo) throws JoinpointException
   {
      if (ctorInfo != null)
      {
         return ctorInfo;
      }
      
      Class clazz = AOPDeployedChecker.getClassIfExists(
            classInfo.getType().getClassLoader(), 
            "org.jboss.aop.microcontainer.integration.AOPConstructorJoinpoint");
      
      if (clazz == null)
      {
         return null;
      }
      
      TypeInfoFactory factory = new IntrospectionTypeInfoFactory();
      ClassInfo info = (ClassInfo)factory.getTypeInfo(clazz);
      ConstructorInfo[] ctors = info.getDeclaredConstructors();
      for (int i = 0 ; i < ctors.length ; i++)
      {
         if (ctors[i].getParameterTypes().length == 2)
         {
            if (ctors[i].getParameterTypes()[0].getName().equals(ConstructorInfo.class.getName()) == false)
            {
               continue;
            }
            
            if (ctors[i].getParameterTypes()[1].getName().equals(MetaDataContext.class.getName()) == false)
            {
               continue;
            }
            ctorInfo = ctors[i];
            break;
         }
      }
      
      if (ctorInfo == null)
      {
         throw new JoinpointException("No constructor found with the reqiured signature AOPConstructorJoinpoint(ConstructorInfo, MetadataContext)");
      }
      return ctorInfo;
   }
   
   private ConstructorJoinpoint createAOPConstructorJoinpoint(ConstructorInfo info, ConstructorInfo aopCtorInfo) throws JoinpointException
   {
      ConstructorJoinpoint jp = new BasicConstructorJoinPoint(info);
      jp.setArguments(new Object[] {aopCtorInfo, metaDataContext});
      try
      {
         return (ConstructorJoinpoint)jp.dispatch();
      }
      catch (Throwable e)
      {
         throw new JoinpointException("Error calling AOPConstructorJoinpoint constructor", e);
      }
      
   }
}

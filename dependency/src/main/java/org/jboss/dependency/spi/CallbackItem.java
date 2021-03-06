/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
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
package org.jboss.dependency.spi;

/**
 * Callback information.
 *
 * @param <T> expected name type - Class, String, ...
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface CallbackItem<T>
{
   /**
    * Get the object name i depend on
    *
    * @return the name
    */
   T getIDependOn();

   /**
    * Get when the dependency is required
    *
    * @return the state when required
    */
   ControllerState getWhenRequired();

   /**
    * Get the dependent's state
    *
    * @return the state of the required of the dependent
    */
   ControllerState getDependentState();

   /**
    * Get the method/attribute/property name
    *
    * @return the name
    */
   String getAttributeName();

   /**
    * Execute callback when item added to controller.
    *
    * @param controller the controller
    * @param isInstallPhase install or uninstall
    * @throws Throwable for any error
    */
   void ownerCallback(Controller controller, boolean isInstallPhase) throws Throwable;

   /**
    * Execute callback with current changed context.
    *
    * @param controller the controller
    * @param context the new context
    * @param isInstallPhase install or uninstall
    * @throws Throwable for any error
    */
   void changeCallback(Controller controller, ControllerContext context, boolean isInstallPhase) throws Throwable;
}

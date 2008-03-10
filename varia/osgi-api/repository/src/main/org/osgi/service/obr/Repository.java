/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.osgi.service.obr;

import java.net.URL;

/**
 * Represents a repository.
 * 
 * Copyright (c) OSGi Alliance (2006). All Rights Reserved.
 * 
 * @version $Revision$
 */
public interface Repository
{
    /**
     * Return the associated URL for the repository.
     *
     * @return url
     */
    URL getURL();

    /**
     * Return the resources for this repository.
     *
     * @return resource
     */
    Resource[] getResources();

    /**
     * Return the name of this reposotory.
     * 
     * @return a non-null name
     */
    String getName();

    long getLastModified();

}
/*
 * JOCL - Java bindings for OpenCL
 * 
 * Copyright 2009 Marco Hutter - http://www.jocl.org/
 * 
 * 
 * This file is part of JOCL. 
 * 
 * JOCL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JOCL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License
 * along with JOCL.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jocl;

/**
 * Emulation of a function pointer for functions that may be passed to the
 * {@link CL#clCreateContext(cl_context_properties, int, cl_device_id[], CreateContextFunction, Object, int[]) clCreateContext} and 
 * {@link CL#clCreateContextFromType(cl_context_properties, long, CreateContextFunction, Object, int[]) clCreateContextFromType} methods. 
 * 
 * @see CL#clCreateContext(cl_context_properties, int, cl_device_id[], CreateContextFunction, Object, int[]) 
 * @see CL#clCreateContextFromType(cl_context_properties, long, CreateContextFunction, Object, int[])
 */
public interface CreateContextFunction
{
    /**
     * The function that will be called.
     * 
     * @param errinfo The error info.
     * @param private_info The private info data.
     * @param cb The The size of the private info data.
     * @param user_data The user data.
     */
    void function(String errinfo, Pointer private_info, long cb, Object user_data);
}

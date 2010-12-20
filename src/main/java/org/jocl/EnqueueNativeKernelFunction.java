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
 * {@link CL#clEnqueueNativeKernel(cl_command_queue, EnqueueNativeKernelFunction, 
 * Object, long, int, cl_mem[], Pointer[], int, cl_event[], cl_event) clEnqueueNativeKernel}
 * method.
 * 
 * @see CL#clEnqueueNativeKernel(cl_command_queue, EnqueueNativeKernelFunction, Object, 
 * long, int, cl_mem[], Pointer[], int, cl_event[], cl_event)
 */
public interface EnqueueNativeKernelFunction
{
    /**
     * The function that will be called
     * 
     * @param args The arguments
     */
    void function(Object args);
}

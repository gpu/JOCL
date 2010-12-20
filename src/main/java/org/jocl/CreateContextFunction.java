/*
 * JOCL - Java bindings for OpenCL - http://www.jocl.org
 * 
 * DISCLAIMER: THIS SOFTWARE IS PROVIDED WITHOUT WARRANTY OF ANY KIND
 * If you find any bugs or errors, report them at http://www.jocl.org 
 * 
 * LICENSE: THIS SOFTWARE IS FREE FOR NON-COMMERCIAL USE ONLY
 * For non-commercial applications, you may use this software without
 * any restrictions. If you wish to use it for commercial purposes, 
 * contact me at http://www.jocl.org 
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

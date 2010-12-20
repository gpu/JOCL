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

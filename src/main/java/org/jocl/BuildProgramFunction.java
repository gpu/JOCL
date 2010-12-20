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

import org.jocl.cl_program;

/**
 * Emulation of a function pointer for functions that may be passed to the
 * {@link CL#clBuildProgram(cl_program, int, cl_device_id[], String, BuildProgramFunction, Object)
 *  clBuildProgram} method. 
 * 
 * @see CL#clBuildProgram(cl_program, int, cl_device_id[], String, BuildProgramFunction, Object)
 */
public interface BuildProgramFunction
{
    /**
     * The function that will be called.
     * 
     * @param program The program.
     * @param user_data The user data.
     */
    void function(cl_program program, Object user_data);
}

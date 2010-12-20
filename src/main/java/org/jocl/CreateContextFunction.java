/*
 * JOCL - Java bindings for OpenCL
 *
 * Copyright (c) 2009 Marco Hutter - http://www.jocl.org
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
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

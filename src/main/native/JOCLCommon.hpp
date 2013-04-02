/*
 * JOCL - Java bindings for OpenCL
 *
 * Copyright (c) 2009-2012 Marco Hutter - http://www.jocl.org
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

#ifndef JOCL_COMMON_HPP
#define JOCL_COMMON_HPP

// The deprecated API is still available, although
// it may or may not be supported
#define CL_USE_DEPRECATED_OPENCL_1_0_APIS
#define CL_USE_DEPRECATED_OPENCL_1_1_APIS

#define CL_GL_INTEROP_ENABLED

// CL includes
#if defined(__APPLE__) || defined(__MACOSX)
    #include <OpenCL/opencl.h>
#else
    #include <CL/opencl.h>
    #ifdef _WIN32
        #define WINDOWS_LEAN_AND_MEAN
        #define NOMINMAX
        // Disable "unreferenced formal parameter"
        // warning (for the JNIEnv parameter)
        #pragma warning (disable : 4100)
        #include <windows.h>
    #endif // _WIN32
    #include <GL/gl.h>
#endif // __APPLE__


#endif // JOCL_COMMON_HPP

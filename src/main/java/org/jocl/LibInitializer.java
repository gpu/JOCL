/*
 * JOCL - Java bindings for OpenCL
 *
 * Copyright (c) 2009-2015 Marco Hutter - http://www.jocl.org
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

import org.jocl.LibUtils.OSType;

/**
 * Utility class for initializing the OpenCL implementation library
 */
class LibInitializer
{
    /**
     * Initialize the native library by passing the name of the OpenCL
     * implementation to the {@link CL#initNativeLibrary(String)} 
     * method.
     * 
     * @throws UnsatisfiedLinkError If the implementation library 
     * could not be loaded.
     */
    static void initNativeLibrary()
    {
        String implementationName = createImplementationName();
        boolean initialized = 
            CL.initNativeLibrary(implementationName);
        if (!initialized)
        {
            throw new UnsatisfiedLinkError(
                "Could not initialize native library. Implementation " +
                "library '"+implementationName+"' could not be loaded");
        }
    }
    
    /**
     * Create the name for the OpenCL implementation that will be passed 
     * to the dlopen/LoadLibrary call on native side. For Windows and
     * Linux, this will be the name of the OpenCL library itself.
     * For MacOS, it will be the path to the OpenCL framework.
     * 
     * @return The name of the implementation library
     */
    private static String createImplementationName()
    {
        OSType osType = LibUtils.calculateOS();
        if (OSType.APPLE.equals(osType))
        {
            return "/System/Library/Frameworks/OpenCL.framework/" +
                    "Versions/Current/OpenCL";
        }
        return LibUtils.createLibraryFileName("OpenCL");
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private LibInitializer()
    {
        // Private constructor to prevent instantiation
    }

}

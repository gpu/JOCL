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
        String[] libCandidates = openCLLibraryCandidates();

        boolean initialized = false;
        for (int i = 0; i < libCandidates.length && !initialized; i++) {
          System.out.println("Trying library candidate: " + libCandidates[i]);
          initialized = CL.initNativeLibrary(libCandidates[i]);
        }

        if (!initialized)
        {
            throw new UnsatisfiedLinkError(
                "Could not initialize native OpenCL library. Implementation " +
                "library could not be loaded");
        }
    }

    /**
     * Create a list of OpenCL shared library candidates that will be
     * passed to the dlopen/LoadLibrary call on native side. For Windows
     * and Linux, this will be the name of the OpenCL library itself.
     * For MacOS, it will be the path to the OpenCL framework. For Android,
     * this will be an absolute path to the shared library.
     *
     * @return {String[]} A list of candidate paths / names.
     */
    private static String[] openCLLibraryCandidates()
    {
        String defaultLibName = LibUtils.createLibraryFileName("OpenCL");
        OSType osType = LibUtils.calculateOS();
        if (OSType.APPLE.equals(osType))
        {
            return new String[] {
                "/System/Library/Frameworks/OpenCL.framework/Versions/Current/OpenCL",
                defaultLibName
            };
        } else if (OSType.ANDROID.equals(osType))
        {
            return new String[]
            {
                "/system/vendor/lib/libOpenCL.so", // Qualcomm
                "/system/vendor/lib/egl/libGLES_mali.so", // ARM MALI SDK
                "/system/vendor/lib/libPVROCL.so", // PowerVR SDK
                "/system/lib/libOpenCL.so",
                "/system/lib/egl/libGLES_mali.so",
                "/system/lib/libPVROCL.so",
                defaultLibName
            };
        }
        return new String[]
        {
            defaultLibName
        };
    }

    /**
     * Private constructor to prevent instantiation
     */
    private LibInitializer()
    {
        // Private constructor to prevent instantiation
    }

}

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

import java.io.*;
import java.util.*;

/**
 * Utility class for detecting the operating system and architecture
 * types, and automatically loading the matching native library
 * as a resource or from a file. <br>
 * <br>
 * The architecture and OS detection has been adapted from 
 * http://javablog.co.uk/2007/05/19/making-jni-cross-platform/
 * and extended with http://lopica.sourceforge.net/os.html 
 */
final class LibUtils
{
    /**
     * Enumeration of common operating systems, independent of version 
     * or architecture. 
     */
    public static enum OSType
    {
        APPLE, LINUX, SUN, WINDOWS, UNKNOWN
    }

    /**
     * Enumeration of common CPU architectures.
     */
    public static enum ARCHType
    {
        PPC, PPC_64, SPARC, X86, X86_64, ARM, MIPS, RISC, UNKNOWN
    }

    /**
     * Loads the specified library. The full name of the library
     * is created by calling {@link LibUtils#createLibName(String)}
     * with the given argument. The method will attempt to load
     * the library using the usual System.loadLibrary call,
     * and, if this fails, it will try to load it as a as a 
     * resource (for usage within a JAR).
     *    
     * @param baseName The base name of the library
     * @throws UnsatisfiedLinkError if the native library 
     * could not be loaded.
     */
    public static void loadLibrary(String baseName)
    {
        String libName = createLibName(baseName);
        
        log("Loading library");
        log("    Base name   : "+baseName);
        log("    Library name: "+libName);
        

        // First, try to load the specified library as a file 
        // that is visible in the default search path
        Throwable throwableFromFile = null;
        try
        {
            log("Loading library as a file");
            System.loadLibrary(libName);
            log("Loading library as a file DONE");
            return;
        }
        catch (Throwable t) 
        {
            log("Loading library as a file FAILED");
            throwableFromFile = t;
        }

        // Now try to load the library by extracting the
        // corresponding resource from the JAR file
        try
        {
            log("Loading library as a resource");
            loadLibraryResource(libName);
            log("Loading library as a resource DONE");
            return;
        }
        catch (Throwable throwableFromResource)
        {
            log("Loading library as a resource FAILED");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            pw.println("Error while loading native library \"" +
                libName + "\" with base name \""+baseName+"\"");
            pw.println("Operating system name: "+
                System.getProperty("os.name"));
            pw.println("Architecture         : "+
                System.getProperty("os.arch"));
            pw.println("Architecture bit size: "+
                System.getProperty("sun.arch.data.model"));

            pw.println("---(start of nested stack traces)---");
            
            pw.println("Stack trace from the attempt to " +
                "load the library as a file:");
            throwableFromFile.printStackTrace(pw);

            pw.println("Stack trace from the attempt to " +
                "load the library as a resource:");
            throwableFromResource.printStackTrace(pw);
            
            pw.println("---(end of nested stack traces)---");

            pw.close();
            throw new UnsatisfiedLinkError(sw.toString());
        }
    }




    /**
     * Load the library with the given name from a resource. 
     * The extension for the current OS will be appended.
     * 
     * @param libName The library name
     * @throws Throwable If the library could not be loaded
     */
    private static void loadLibraryResource(String libName) throws Throwable
    {
        // Build the full name of the library 
        String libraryFileName = createLibraryFileName(libName);

        // If a temporary file with the resulting name
        // already exists, it can simply be loaded
        String tempDirName = System.getProperty("java.io.tmpdir");
        String tempFileName = 
            tempDirName + File.separator + libraryFileName;
        
        boolean useUniqueNativeLibraries = useUniqueNativeLibraries();
        if (useUniqueNativeLibraries)
        {
            tempFileName = 
                tempDirName + File.separator + 
                createLibraryFileName(libName + "-" + UUID.randomUUID());
        }
        File tempFile = new File(tempFileName);
        
        log("Temp file for resource: "+tempFile);
        
        if (tempFile.exists())
        {
            log("Loading from existing file: "+tempFile);
            System.load(tempFile.toString());
            return;
        }

        // No file with the resulting name exists yet. Try to write
        // the library data from the JAR into the temporary file, 
        // and load the newly created file.
        String resourceName = "/lib/" + libraryFileName;
        InputStream inputStream = 
            LibUtils.class.getResourceAsStream(resourceName);
        if (inputStream == null)
        {
            throw new NullPointerException(
                "No resource found with name '"+resourceName+"'");
        }
        
        log("Creating temp file for resource: "+tempFile);
        
        OutputStream outputStream = null;
        try
        {
            outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[8192];
            while (true)
            {
                int read = inputStream.read(buffer);
                if (read < 0)
                {
                    break;
                }
                outputStream.write(buffer, 0, read);    
            }
            outputStream.flush();
            outputStream.close();
            outputStream = null;

            if (useUniqueNativeLibraries)
            {
                LibTracker.track(tempFile);
            }

            log("Loading from created file: "+tempFile);
            System.load(tempFile.toString());
            
        }
        finally 
        {
            if (outputStream != null)
            {
                outputStream.close();
            }
            inputStream.close();
        }
    }

    /**
     * Returns whether the "uniqueLibaryNames" property was set,
     * and the temporary native libraries should be tracked and
     * deleted when the application exits.<br>
     * <br>
     * PRELIMINARY! 
     * 
     * @return Whether the flag was set 
     */
    private static boolean useUniqueNativeLibraries()
    {
        String uniqueLibraryNames = 
            System.getProperty("uniqueLibraryNames");
        return "true".equals(uniqueLibraryNames);
    }


    /**
     * Create the full library file name, including the extension
     * and prefix, for the given library name. For example, the
     * name 'JOCL' will become <br>
     * JOCL.dll on Windows <br>
     * libJOCL.so on Linux <br>
     * JOCL.dylib on MacOS <br>
     * 
     * @param libraryName The library name
     * @return The full library name, with extension
     */
    static String createLibraryFileName(String libraryName)
    {
        String libPrefix = createLibPrefix();
        String libExtension = createLibExtension();
        String fullName = libPrefix + libraryName + "." + libExtension;
        return fullName;
    }


    /**
     * Returns the extension for dynamically linked libraries on the
     * current OS. That is, returns "dylib" on Apple, "so" on Linux
     * and Sun, and "dll" on Windows.
     * 
     * @return The library extension
     */
    private static String createLibExtension()
    {
        OSType osType = calculateOS();
        switch (osType) 
        {
            case APPLE:
                return "dylib";
            case LINUX:
                return "so";
            case SUN:
                return "so";
            case WINDOWS:
                return "dll";
            default:
                break;
        }
        return "";
    }

    /**
     * Returns the prefix for dynamically linked libraries on the
     * current OS. That is, returns "lib" on Apple, Linux and Sun, 
     * and the empty String on Windows.
     * 
     * @return The library prefix
     */
    private static String createLibPrefix()
    {
        OSType osType = calculateOS();
        switch (osType) 
        {
            case APPLE:
            case LINUX:
            case SUN:
                return "lib";
            case WINDOWS:
                return "";
            default:
                break;
        }
        return "";
    }


    /**
     * Creates the name for the native library with the given base
     * name for the current operating system and architecture.
     * The resulting name will be of the form<br>
     * baseName-OSType-ARCHType<br>
     * where OSType and ARCHType are the <strong>lower case</strong> Strings
     * of the respective enum constants. Example: <br>
     * JOCL-windows-x86<br> 
     * 
     * @param baseName The base name of the library
     * @return The library name
     */
    public static String createLibName(String baseName)
    {
        OSType osType = calculateOS();
        ARCHType archType = calculateArch();
        String libName = baseName;
        libName += "-" + osType.toString().toLowerCase(Locale.ENGLISH);
        libName += "-" + archType.toString().toLowerCase(Locale.ENGLISH);
        return libName;
    }

    /**
     * Calculates the current OSType
     * 
     * @return The current OSType
     */
    public static OSType calculateOS()
    {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase(Locale.ENGLISH);
        if (osName.startsWith("mac os"))
        {
            return OSType.APPLE;
        }
        if (osName.startsWith("windows"))
        {
            return OSType.WINDOWS;
        }
        if (osName.startsWith("linux"))
        {
            return OSType.LINUX;
        }
        if (osName.startsWith("sun"))
        {
            return OSType.SUN;
        }
        return OSType.UNKNOWN;
    }


    /**
     * Calculates the current ARCHType
     * 
     * @return The current ARCHType
     */
    public static ARCHType calculateArch()
    {
        String osArch = System.getProperty("os.arch");
        osArch = osArch.toLowerCase(Locale.ENGLISH);
        if (osArch.equals("i386") || 
            osArch.equals("x86")  || 
            osArch.equals("i686"))
        {
            return ARCHType.X86; 
        }
        if (osArch.startsWith("amd64") || osArch.startsWith("x86_64"))
        {
            return ARCHType.X86_64;
        }
        if (osArch.equals("ppc") || osArch.equals("powerpc"))
        {
            return ARCHType.PPC;
        }
        if (osArch.startsWith("ppc"))
        {
            return ARCHType.PPC_64;
        }
        if (osArch.startsWith("sparc"))
        {
            return ARCHType.SPARC;
        }
        if (osArch.startsWith("arm"))
        {
            return ARCHType.ARM;
        }
        if (osArch.startsWith("mips"))
        {
            return ARCHType.MIPS;
        }
        if (osArch.contains("risc"))
        {
            return ARCHType.RISC;
        }
        return ARCHType.UNKNOWN;
    }
    
    /**
     * Logging method 
     * 
     * @param message The log message
     */
    private static void log(Object message)
    {
        //System.out.println(message);
    }
    

    /**
     * Private constructor to prevent instantiation.
     */
    private LibUtils()
    {
    }
}

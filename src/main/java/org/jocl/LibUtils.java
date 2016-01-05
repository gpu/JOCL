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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for detecting the operating system and architecture
 * types, and automatically loading the matching native library
 * as a resource or from a file. <br>
 * <br>
 * This class is not intended to be used by clients.<br>
 * <br>
 */
public final class LibUtils
{
    // The architecture and OS detection has been adapted from 
    // http://javablog.co.uk/2007/05/19/making-jni-cross-platform/
    // and extended with http://lopica.sourceforge.net/os.html 
    
    /**
     * The logger used in this class
     */
    private final static Logger logger = 
        Logger.getLogger(LibUtils.class.getName());
    
    /**
     * The default log level
     */
    private static final Level level = Level.FINE;

    /**
     * The directory where libraries are expected in JAR files,
     * when they are loaded as resources
     */
    private static final String LIBRARY_PATH_IN_JAR = "/lib/";
    
    
    /**
     * Enumeration of common operating systems, independent of version 
     * or architecture. 
     */
    static enum OSType
    {
        APPLE, LINUX, SUN, WINDOWS, UNKNOWN
    }

    /**
     * Enumeration of common CPU architectures.
     */
    static enum ARCHType
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
     * @param dependentLibraryNames The names of libraries that the library
     * to load depends on. If the library is loaded as a resource, then 
     * it will be attempted to also load these libraries as resources. 
     * @throws UnsatisfiedLinkError if the native library 
     * could not be loaded.
     */
    public static void loadLibrary(
        String libraryName, String ... dependentLibraryNames)
    {
        logger.log(level, "Loading library");
        logger.log(level, "    Library name: "+libraryName);
        

        // First, try to load the specified library as a file 
        // that is visible in the default search path
        Throwable throwableFromFile = null;
        try
        {
            logger.log(level, "Loading library as a file");
            System.loadLibrary(libraryName);
            logger.log(level, "Loading library as a file DONE");
            return;
        }
        catch (Throwable t) 
        {
            logger.log(level, "Loading library as a file FAILED");
            throwableFromFile = t;
        }

        // Now try to load the library by extracting the
        // corresponding resource from the JAR file
        try
        {
            logger.log(level, "Loading library as a resource");
            loadLibraryResource(libraryName, dependentLibraryNames);
            logger.log(level, "Loading library as a resource DONE");
            return;
        }
        catch (Throwable throwableFromResource)
        {
            logger.log(level, "Loading library as a resource FAILED");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            pw.println("Error while loading native library \"" +
                libraryName + "\"");
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
     * 
     * @param libraryName The library name, e.g. "EXAMPLE-windows-x86"
     * @param dependentLibraryNames The names of libraries that the library
     * to load depends on, and that may have to be loaded as resources and
     * stored as temporary files as well 
     * @throws Throwable If the library could not be loaded
     */
    private static void loadLibraryResource(
        String libraryName, String ... dependentLibraryNames) throws Throwable
    {
        boolean useUniqueNativeLibraries = useUniqueNativeLibraries();
        boolean trackCreatedTempFiles = useUniqueNativeLibraries; 

        // First try to load all dependent libraries
        for (String dependentLibraryName : dependentLibraryNames)
        {
            // Determine the temporary file for the dependent library
            String dependentLibraryFileName = 
                createLibraryFileName(dependentLibraryName);
            File dependentLibraryTempFile = 
                createTempFile(dependentLibraryFileName);
            
            // If the temporary file for the dependent library does 
            // not exist, create it
            if (!dependentLibraryTempFile.exists())
            {
                String dependentLibraryResourceName = 
                    LIBRARY_PATH_IN_JAR + dependentLibraryFileName;
                logger.log(level, 
                    "Creating temp file for dependent library resource: "+
                    dependentLibraryTempFile);
                writeResourceToFile(
                    dependentLibraryResourceName, 
                    dependentLibraryTempFile);
                if (trackCreatedTempFiles)
                {
                    LibTracker.track(dependentLibraryTempFile);
                }
            }
            logger.log(level, 
                "Loading dependent library "+
                dependentLibraryTempFile.toString());
            System.load(dependentLibraryTempFile.toString());
            logger.log(level, 
                "Loading dependent library "+
                dependentLibraryTempFile.toString()+" DONE");
        }
        
        // Now, load the actual library
        String libraryFileName = createLibraryFileName(libraryName);
        File libraryTempFile = null;
        if (useUniqueNativeLibraries)
        {
            String uniqueLibraryFileName = 
                createLibraryFileName(libraryName + "-" + UUID.randomUUID());
            libraryTempFile = createTempFile(uniqueLibraryFileName);
        }
        else
        {
            libraryTempFile = createTempFile(libraryFileName);
        }
        
        // If the temporary file for the library does not exist, create it
        if (!libraryTempFile.exists())
        {
            String libraryResourceName = 
                LIBRARY_PATH_IN_JAR + libraryFileName;
            logger.log(level, 
                "Creating temp file for library resource: "+
                libraryTempFile);
            writeResourceToFile(libraryResourceName, libraryTempFile);
            if (useUniqueNativeLibraries)
            {
                LibTracker.track(libraryTempFile);
            }
        }
        System.load(libraryTempFile.toString());
    }

    
    /**
     * Create a file object representing the file with the given name
     * in the default "temp" directory
     * 
     * @param name The file name
     * @return The file
     */
    private static File createTempFile(String name)
    {
        String tempDirName = System.getProperty("java.io.tmpdir");
        String tempFileName = tempDirName + File.separator + name;
        File tempFile = new File(tempFileName);
        return tempFile;
    }
    
    
    /**
     * Obtain an input stream to the resource with the given name, and write 
     * it to the specified file (which may not be <code>null</code>, and 
     * may not exist yet)
     * 
     * @param resourceName The name of the resource
     * @param file The file to write to
     * @throws NullPointerException If the given file is <code>null</code>
     * @throws IllegalArgumentException If the given file already exists
     * @throws IOException If an IO error occurs
     */
    private static void writeResourceToFile(
        String resourceName, File file) throws IOException
    {
        if (file == null)
        {
            throw new NullPointerException("The file may not be null");
        }
        if (file.exists())
        {
            throw new IllegalArgumentException("File already exists: "+file);
        }
        InputStream inputStream = 
            LibUtils.class.getResourceAsStream(resourceName);
        if (inputStream == null)
        {
            throw new IOException(
                "No resource found with name '"+resourceName+"'");
        }
        OutputStream outputStream = null;
        try
        {
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[32768];
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
        }
        finally 
        {
            if (outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException e)
                {
                    logger.warning(e.getMessage());
                }
            }
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                logger.warning(e.getMessage());
            }
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
     * name "EXAMPLE" will become <br>
     * EXAMPLE.dll on Windows <br>
     * libEXAMPLE.so on Linux <br>
     * EXAMPLE.dylib on MacOS <br>
     * 
     * @param libraryName The library name
     * @return The full library name, with extension
     */
    public static String createLibraryFileName(String libraryName)
    {
        String libPrefix = createLibraryPrefix();
        String libExtension = createLibraryExtension();
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
    private static String createLibraryExtension()
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
    private static String createLibraryPrefix()
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
     * Creates the name for the native library with the given base name for 
     * the current platform, by appending strings that indicate the current 
     * operating system and architecture.<br>
     * <br>
     * The resulting name will be of the form<br>
     * <code>baseName-OSType-ARCHType</code><br>
     * where OSType and ARCHType are the <strong>lower case</strong> Strings
     * of the respective {@link OSType} and {@link ARCHType} enum constants.<br>
     * <br> 
     * For example, the library name with the base name "EXAMPLE" may be<br>
     * <code>EXAMPLE-windows-x86</code><br>
     * <br>
     * Note that the resulting name will not include any platform specific
     * prefixes or extensions for the actual name.  
     * 
     * @param baseName The base name of the library
     * @return The library name
     */
    public static String createPlatformLibraryName(String baseName)
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
    static OSType calculateOS()
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
    private static ARCHType calculateArch()
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
     * Private constructor to prevent instantiation.
     */
    private LibUtils()
    {
        // Private constructor to prevent instantiation.
    }
}

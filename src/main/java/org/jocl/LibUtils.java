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

import java.util.Locale;

/**
 * Utility class for detecting the operating system and architecture
 * types, and automatically loading the matching native library. <br />
 * <br />
 * Adapted from http://javablog.co.uk/2007/05/19/making-jni-cross-platform/
 * <br />
 * Extended with http://lopica.sourceforge.net/os.html 
 */
final class LibUtils
{
    /**
     * Enumeration of common operating systems, independent of version or architecture. 
     */
    public static enum OSType
    {
        APPLE, LINUX, SUN, WINDOWS, UNKNOWN
    };
    
    /**
     * Enumeration of common CPU architectures.
     */
    public static enum ARCHType
    {
        PPC, PPC_64, SPARC, X86, X86_64, ARM, MIPS, RISC, UNKNOWN
    };
    
    /**
     * Loads the specified library. The full name of the library
     * is created by calling {@link LibUtils#createLibName(String)}
     * with the given argument.
     *    
     * @param baseName The base name of the library
     * @throws UnsatisfiedLinkError if the native library 
     * could not be loaded.
     */
    public static void loadLibrary(String baseName)
    {
        String libName = LibUtils.createLibName(baseName);
        try
        {
            System.loadLibrary(libName);
        }
        catch (Throwable t) 
        {
            System.err.println("Error while loading native library \"" +
            		libName + "\" with base name \""+baseName+"\"");
            System.err.println("Operating system name: "+System.getProperty("os.name"));
            System.err.println("Architecture         : "+System.getProperty("os.arch"));
            System.err.println("Architecture bit size: "+System.getProperty("sun.arch.data.model"));
            System.err.println("Stack trace:");
            t.printStackTrace();
            throw new UnsatisfiedLinkError("Could not load the native library");
        }
    }
    
    /**
     * Creates the name for the native library with the given base
     * name for the current operating system and architecture.
     * The resulting name will be of the form<br />
     * baseName-OSType-ARCHType<br />
     * where OSType and ARCHType are the <strong>lower case</strong> Strings
     * of the respective enum constants. Example: <br />
     * JOCL-windows-x86<br /> 
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
        String osName = System.getProperty("os.name").toLowerCase();
        assert osName != null;
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
        String osArch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
        assert osArch != null;
        if (osArch.equals("i386") || osArch.equals("x86") || osArch.equals("i686"))
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
    }
}

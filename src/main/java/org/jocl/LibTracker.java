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
import java.lang.reflect.*;
import java.util.*;

/**
 * Utility class for tracking a set of loaded libraries and
 * deleting them at program exit.<br>
 * <br>
 * <b>Note:</b> The current default setting in JOCL is that 
 * this class is NOT used.<br>
 * <br>
 * This class contains some ugly reflection hacks, attempting
 * to alleviate the problem that (temporary) native library 
 * files on Windows can not be deleted when they have been 
 * loaded.<br> 
 * <br>
 * However, the default setting in JOCL is that it assumes that 
 * the name of the native library is fixed, and it will not be 
 * necessary to create temporary files.
 */
class LibTracker
{
    /**
     * The tracked library files that will be unloaded
     * and deleted when the application exits
     */
    private static final Set<File> libraryFiles = new LinkedHashSet<File>();

    /**
     * The shutdown hook thread that will attempt to unload
     * and delete the library files
     */
    private static Thread shutdownHook = null;

    /**
     * Add the given native library file to be tracked by this class. 
     * When the application exits, the native library will be unloaded
     * and the given file will be deleted.
     *  
     * @param libraryFile The library file
     */
    static synchronized void track(File libraryFile)
    {
        if (libraryFile == null)
        {
            return;
        }
        if (shutdownHook == null)
        {
            log("Initializing library shutdown hook");
            shutdownHook = new Thread()
            {
                public void run()
                {
                    shutdown();
                }
            };
            try
            {
                Runtime.getRuntime().addShutdownHook(shutdownHook);
            }
            catch (SecurityException e)
            {
                // Ignored
            }
        }
        log("Tracking library file "+libraryFile);
        libraryFiles.add(libraryFile);
    }

    /**
     * Will be called by the shutdown hook
     */
    private static void shutdown()
    {
        freeNativeLibraries();
        deleteNativeLibraries();
    }

    /**
     * Free the native libraries that have been registered in this class.
     * Any errors will be ignored.
     */
    private static void freeNativeLibraries()
    {
        ClassLoader classLoader = LibTracker.class.getClassLoader();
        Object nativeLibrariesObject = 
            getFieldValueOptional(classLoader, "nativeLibraries");
        if (nativeLibrariesObject == null)
        {
            return;
        }
        if (!(nativeLibrariesObject instanceof List<?>))
        {
            return;
        }
        List<?> nativeLibraries = (List<?>)nativeLibrariesObject;
        for (Object nativeLibrary : nativeLibraries)
        {
            freeNativeLibrary(nativeLibrary);
        }
    }

    /**
     * Free the given library object by invoking its "finalize(Class<?>)"
     * method. Any errors will be ignored.
     * 
     * @param library The library
     */
    private static void freeNativeLibrary(final Object library)
    {
        Object nameObject = getFieldValueOptional(library, "name");
        if (nameObject == null)
        {
            return;
        }
        String name = String.valueOf(nameObject);
        File file = new File(name);
        if (libraryFiles.contains(file))
        {
            invokeFinalizeOptional(library);
        }
    }

    /**
     * Try to delete all library files that are tracked by this class.
     * Any errors will be ignored.
     */
    private static void deleteNativeLibraries()
    {
        for (File libraryFile : libraryFiles)
        {
            if (libraryFile.exists())
            {
                try
                {
                    boolean deleted = libraryFile.delete();
                    log("Deleting " + libraryFile + " " + 
                        (deleted ? "DONE" : "FAILED"));
                }
                catch (SecurityException e)
                {
                    // Ignored
                }
            }
        }
        libraryFiles.clear();

    }

    
    /**
     * Returns the value of the (potentially private) field with the  
     * given name from the given object. If there is any error, then 
     * <code>null</code> will be returned.
     * 
     * @param object The object
     * @param fieldName The name of the field whose value to obtain
     * @return The field value, or <code>null</code> if the value 
     * could not be obtained
     */
    private static Object getFieldValueOptional(
        Object object, String fieldName)
    {
        if (object == null)
        {
            return null;
        }
        Class<?> c = object.getClass();
        
        Field field = getDeclaredField(c, fieldName);
        if (field == null)
        {
            return null;
        }

        boolean wasAccessible = field.isAccessible();
        try
        {
            field.setAccessible(true);
            try
            {
                return field.get(object);
            }
            catch (Exception e)
            {
                return null;
            }
        }
        finally
        {
            try
            {
                field.setAccessible(wasAccessible);
            }
            catch (SecurityException e)
            {
                // Ignored
            }
        }
    }
    
    /**
     * Returns the declared field with the given name in the given
     * class, or any of its superclasses. Returns <code>null</code>
     * if no such field could be found.
     * 
     * @param c The class
     * @param fieldName The field name
     * @return The field
     */
    private static Field getDeclaredField(Class<?> c, String fieldName)
    {
        if (c == null)
        {
            return null;
        }
        try
        {
            Field field = c.getDeclaredField(fieldName);
            return field;
        }
        catch (Exception e)
        {
            return getDeclaredField(c.getSuperclass(), fieldName);
        }
    }

    /**
     * Tries to invoke a "finalize(Class<?>)" method on the given object.
     * Any error will be ignored.
     * 
     * @param object The object
     */
    private static void invokeFinalizeOptional(Object object)
    {
        if (object == null)
        {
            return;
        }
        Method method = null;
        boolean wasAccessible = false;
        try
        {
            Class<?> c = object.getClass();
            method = c.getDeclaredMethod("finalize", new Class[0]);
            wasAccessible = method.isAccessible();
            method.setAccessible(true);
            method.invoke(object, new Object[0]);
        }
        catch (Exception e)
        {
            // Ignored
        }
        finally
        {
            if (method != null)
            {
                try
                {
                    method.setAccessible(wasAccessible);
                }
                catch (SecurityException e)
                {
                    // Ignored
                }
            }
        }
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
     * Private constructor to prevent instantiation
     */
    private LibTracker()
    {
        // Private constructor to prevent instantiation
    }

}

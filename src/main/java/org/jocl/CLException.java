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
 * An exception that may be thrown due to a OpenCL error. <br />
 * <br />
 * Exceptions may be enabled or disabled using
 * {@link org.jocl.CL#setExceptionsEnabled(boolean) CL#setExceptionsEnabled(boolean)}.
 * If exceptions are enabled, the JOCL methods will throw a 
 * CLException if the OpenCL function did not return CL_SUCCESS.<br />
 */
public class CLException extends RuntimeException
{
    /**
     * The serial version UID
     */
    private static final long serialVersionUID = 1587809813906124159L;

    /**
     * Creates a new CLException with the given error message.
     * 
     * @param message The error message for this CLException
     */
    public CLException(String message)
    {
        super(message);
    }
} 

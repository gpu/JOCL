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

    /**
     * Creates a new CLException with the given error message.
     * 
     * @param message The error message for this CLException
     * @param cuase The throwable that caused this exception
     */
    CLException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
} 

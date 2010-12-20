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
 * Java port of a cl_platform_id.
 */
public class cl_platform_id extends NativePointerObject
{
    /**
     * Creates a new, uninitialized cl_platform_id 
     */
    public cl_platform_id()
    {
    }
    
    /**
     * Returns a String representation of this object.
     * 
     * @return A String representation of this object.
     */
    @Override
    public String toString()
    {
        return "cl_platform_id[0x"+Long.toHexString(getNativePointer())+"]";
    }
}

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

import java.nio.LongBuffer;

/**
 * Java port of cl_context_properties.
 */
public class cl_context_properties extends NativePointerObject
{
    /**
     * Creates new, empty cl_context_properties 
     */
    public cl_context_properties()
    {
        super(LongBuffer.wrap(new long[]{0}));
    }
    
    /**
     * Add the specified property to these properties
     * 
     * @param id The property ID
     * @param value The property value
     */
    private void addProperty(long id, long value)
    {
        LongBuffer oldBuffer = (LongBuffer)getBuffer();
        long newArray[] = new long[oldBuffer.capacity()+2];
        newArray[oldBuffer.capacity()-1] = id;
        newArray[oldBuffer.capacity()+0] = value;
        newArray[oldBuffer.capacity()+1] = 0;
        setBuffer(LongBuffer.wrap(newArray));
    }

    /**
     * Add the specified property to these properties
     * 
     * @param id The property ID
     * @param value The property value
     */
    public void addProperty(long id, cl_platform_id value)
    {
        addProperty(id, value.getNativePointer());
    }
    
    
    /**
     * Returns a String representation of this object.
     * 
     * @return A String representation of this object.
     */
    @Override
    public String toString()
    {
        return "cl_context_properties[0x"+Long.toHexString(getNativePointer())+"]";
    }
}

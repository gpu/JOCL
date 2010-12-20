/*
 * JOCL - Java bindings for OpenCL
 *
 * Copyright (c) 2009 Marco Hutter - http://www.jocl.org
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

import java.nio.LongBuffer;

//TODO: This probably does not have to be a NativePointerObject

/**
 * Java port of cl_context_properties.
 */
public final class cl_context_properties extends NativePointerObject
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
    public void addProperty(long id, long value)
    {
        LongBuffer oldBuffer = (LongBuffer)getBuffer();
        long newArray[] = new long[oldBuffer.capacity()+2];
        oldBuffer.get(newArray, 0, oldBuffer.capacity());
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
        //return "cl_context_properties[0x"+Long.toHexString(getNativePointer())+"]";
        
        StringBuilder result = new StringBuilder("cl_context_properties[");
        LongBuffer buffer = (LongBuffer)getBuffer();
        int entries = buffer.capacity() / 2;
        for (int i=0; i<entries; i++)
        {
            int n0 = (int)buffer.get(i*2+0);
            int n1 = (int)buffer.get(i*2+1);
            result.append(CL.stringFor_cl_context_properties(n0));
            result.append("=");
            result.append(String.valueOf(n1));
            if (i<entries-1)
            {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }
}

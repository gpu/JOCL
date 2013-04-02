/*
 * JOCL - Java bindings for OpenCL
 *
 * Copyright (c) 2009-2012 Marco Hutter - http://www.jocl.org
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

/**
 * Abstract base class for CL properties, like cl_context_properties
 * and cl_device_partition_property
 */
abstract class cl_abstract_properties extends NativePointerObject
{
    /**
     * Creates new, empty cl_abstract_properties 
     */
    cl_abstract_properties()
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
     * Returns the string identifying the given property
     * @param value The property value
     * @return The string representation
     */
    protected abstract String propertyString(long value);
    
    /**
     * Returns a String containing the contents of these properties
     * 
     * @return A String representation of the contents of these properties
     */
    protected String buildString()
    {
        StringBuilder result = new StringBuilder();
        LongBuffer buffer = (LongBuffer)getBuffer();
        int entries = buffer.capacity() / 2;
        for (int i=0; i<entries; i++)
        {
            int n0 = (int)buffer.get(i*2+0);
            int n1 = (int)buffer.get(i*2+1);
            result.append(propertyString(n0));
            result.append("=");
            result.append(String.valueOf(n1));
            if (i<entries-1)
            {
                result.append(",");
            }
        }
        return result.toString();
    }
}

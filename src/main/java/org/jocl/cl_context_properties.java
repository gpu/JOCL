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

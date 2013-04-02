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

/**
 * Java port of cl_device_partition_property.
 */
public final class cl_device_partition_property extends cl_abstract_properties
{
    /**
     * Creates new, empty cl_device_partition_property 
     */
    public cl_device_partition_property()
    {
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
    
    @Override
    protected String propertyString(long value)
    {
        return CL.stringFor_cl_device_partition_property((int)value);
    }
    
    /**
     * Returns a String representation of this object.
     * 
     * @return A String representation of this object.
     */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("cl_device_partition_property[");
        result.append(buildString());
        result.append("]");
        return result.toString();
    }

}

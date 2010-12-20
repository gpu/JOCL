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
 * Java port of a cl_image_format
 */
public final class cl_image_format
{
    public int image_channel_order;
    public int image_channel_data_type;

    /**
     * Creates a new, uninitialized cl_image_format 
     */
    public cl_image_format()
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
        return "cl_image_format["+
            "image_channel_order="+CL.stringFor_cl_channel_order(image_channel_order)+","+
            "image_channel_data_type="+CL.stringFor_cl_channel_type(image_channel_data_type)+
            "]";
    }
    
}

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
 * Java port of a cl_image_format
 */
public class cl_image_format
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

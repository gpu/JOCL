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
 * Java port of a cl_image_desc
 */
public class cl_image_desc
{
    /**
     * Describes the image type. This is a <code>cl_mem_object_type</code>
     * and must be either CL_MEM_OBJECT_IMAGE1D, 
     * CL_MEM_OBJECT_IMAGE1D_BUFFER, CL_MEM_OBJECT_IMAGE1D_ARRAY,
     * CL_MEM_OBJECT_IMAGE2D, CL_MEM_OBJECT_IMAGE2D_ARRAY or 
     * CL_MEM_OBJECT_IMAGE3D.
     */
    public int image_type;
    
    /**
     * The width of the image in pixels. For a 2D image and image array, 
     * the image width must be &lt;= CL_DEVICE_IMAGE2D_MAX_WIDTH. For a 3D 
     * image, the image width must be &lt;= CL_DEVICE_IMAGE3D_MAX_WIDTH. 
     * For a 1D image buffer, the image width must be &lt;= 
     * CL_DEVICE_IMAGE_MAX_BUFFER_SIZE. For a 1D image and 1D image array,
     * the image width must be &lt;= CL_DEVICE_IMAGE2D_MAX_WIDTH.
     */
    public long image_width;
    
    /**
     * Height of the image in pixels. This is only used if the image is a 
     * 2D, 3D or 2D image array. For a 2D image or image array, the image 
     * height must be &lt;= CL_DEVICE_IMAGE2D_MAX_HEIGHT. For a 3D image, 
     * the image height must be &lt;= CL_DEVICE_IMAGE3D_MAX_HEIGHT.
     */ 
    public long image_height;
    
    /**
     * The depth of the image in pixels. This is only used if the image 
     * is a 3D image and must be a value &gt;= 1 and &lt;= 
     * CL_DEVICE_IMAGE3D_MAX_DEPTH. 
     */
    public long image_depth;
    
    /**
     * The number of images in the image array. This is only used if the
     * image is a 1D or 2D image array. The values for image_array_size, 
     * if specified, must be a value &gt;= 1 and &lt;= 
     * CL_DEVICE_IMAGE_MAX_ARRAY_SIZE.
     */
    public long image_array_size;
    
    /**
     * The scan-line pitch in bytes. This must be 0 if host_ptr is 
     * NULL and can be either 0 or &gt;= image_width * size of element 
     * in bytes if host_ptr is not NULL. If host_ptr is not NULL and 
     * image_row_pitch = 0, image_row_pitch is calculated as 
     * image_width * size of element in bytes. If image_row_pitch is 
     * not 0, it must be a multiple of the image element size in bytes
     */
    public long image_row_pitch;
    
    /**
     * The size in bytes of each 2D slice in the 3D image or the size 
     * in bytes of each image in a 1D or 2D image array. This must be 
     * 0 if host_ptr is NULL. If host_ptr is not NULL, image_slice_pitch 
     * can be either 0 or >= image_row_pitch * image_height for a 2D 
     * image array or 3D image and can be either 0 or &gt;= 
     * image_row_pitch for a 1D image array. If host_ptr is not NULL and
     * image_slice_pitch = 0, image_slice_pitch is calculated as 
     * image_row_pitch * image_height for a 2D image array or 3D image 
     * and image_row_pitch for a 1D image array. If image_slice_pitch 
     * is not 0, it must be a multiple of the image_row_pitch
     */
    public long image_slice_pitch;
    
    /**
     * Must be 0.
     */
    public int num_mip_levels;

    /**
     * Must be 0.
     */
    public int num_samples;
    
    /**
     * buffer refers to a valid buffer memory object if image_type is
     * CL_MEM_OBJECT_IMAGE1D_BUFFER. Otherwise it must be NULL. For a 
     * 1D image buffer object, the image pixels are taken from the 
     * buffer object’s data store. When the contents of a buffer 
     * object’s data store are modified, those changes are reflected 
     * in the contents of the 1D image buffer object and vice-versa 
     * at corresponding synchronization points. The image_width * size 
     * of element in bytes must be &lt;= size of buffer object data store.
     */
    public cl_mem buffer;
    
    /**
     * Creates a new, uninitialized cl_image_desc 
     */
    public cl_image_desc()
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
        return "cl_image_desc["+
            "image_type="+CL.stringFor_cl_mem_object_type(image_type)+","+
            "image_width="+image_width+","+
            "image_height="+image_height+","+
            "image_depth="+image_depth+","+
            "image_array_size="+image_array_size+","+
            "image_row_pitch="+image_row_pitch+","+
            "image_slice_pitch="+image_slice_pitch+","+
            "num_mip_levels="+num_mip_levels+","+
            "num_samples="+num_samples+","+
            "buffer="+buffer+
            "]";
    }
}

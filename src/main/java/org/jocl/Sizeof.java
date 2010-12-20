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
 * Size constants for scalar and vector data types.
 */
public class Sizeof 
{
	/**
	 * Size of a cl_char, in bytes.
	 */
	public static final int cl_char = 8 / 8;

	/**
	 * Size of a cl_uchar, in bytes.
	 */
	public static final int cl_uchar = 8 / 8;

	/**
	 * Size of a cl_short, in bytes.
	 */
	public static final int cl_short = 16 / 8;

	/**
	 * Size of a cl_ushort, in bytes.
	 */
	public static final int cl_ushort = 16 / 8;

	/**
	 * Size of a cl_int, in bytes.
	 */
	public static final int cl_int = 32 / 8;

	/**
	 * Size of a cl_uint, in bytes.
	 */
	public static final int cl_uint = 32 / 8;

	/**
	 * Size of a cl_long, in bytes.
	 */
	public static final int cl_long = 64 / 8;

	/**
	 * Size of a cl_ulong, in bytes.
	 */
	public static final int cl_ulong = 64 / 8;

	/**
	 * Size of a cl_half, in bytes.
	 */
	public static final int cl_half = 16 / 8;

	/**
	 * Size of a cl_float, in bytes.
	 */
	public static final int cl_float = 32 / 8;

	/**
	 * Size of a cl_double, in bytes.
	 */
	public static final int cl_double = 64 / 8;

	/**
	 * Size of a cl_char2, in bytes
	 */
	public static final int cl_char2 = 8 * 2 / 8;

	/**
	 * Size of a cl_char4, in bytes
	 */
	public static final int cl_char4 = 8 * 4 / 8;

	/**
	 * Size of a cl_char8, in bytes
	 */
	public static final int cl_char8 = 8 * 8 / 8;

	/**
	 * Size of a cl_char16, in bytes
	 */
	public static final int cl_char16 = 8 * 16 / 8;

	/**
	 * Size of a cl_uchar2, in bytes
	 */
	public static final int cl_uchar2 = 8 * 2 / 8;

	/**
	 * Size of a cl_uchar4, in bytes
	 */
	public static final int cl_uchar4 = 8 * 4 / 8;

	/**
	 * Size of a cl_uchar8, in bytes
	 */
	public static final int cl_uchar8 = 8 * 8 / 8;

	/**
	 * Size of a cl_uchar16, in bytes
	 */
	public static final int cl_uchar16 = 8 * 16 / 8;

	/**
	 * Size of a cl_short2, in bytes
	 */
	public static final int cl_short2 = 16 * 2 / 8;

	/**
	 * Size of a cl_short4, in bytes
	 */
	public static final int cl_short4 = 16 * 4 / 8;

	/**
	 * Size of a cl_short8, in bytes
	 */
	public static final int cl_short8 = 16 * 8 / 8;

	/**
	 * Size of a cl_short16, in bytes
	 */
	public static final int cl_short16 = 16 * 16 / 8;

	/**
	 * Size of a cl_ushort2, in bytes
	 */
	public static final int cl_ushort2 = 16 * 2 / 8;

	/**
	 * Size of a cl_ushort4, in bytes
	 */
	public static final int cl_ushort4 = 16 * 4 / 8;

	/**
	 * Size of a cl_ushort8, in bytes
	 */
	public static final int cl_ushort8 = 16 * 8 / 8;

	/**
	 * Size of a cl_ushort16, in bytes
	 */
	public static final int cl_ushort16 = 16 * 16 / 8;

	/**
	 * Size of a cl_int2, in bytes
	 */
	public static final int cl_int2 = 32 * 2 / 8;

	/**
	 * Size of a cl_int4, in bytes
	 */
	public static final int cl_int4 = 32 * 4 / 8;

	/**
	 * Size of a cl_int8, in bytes
	 */
	public static final int cl_int8 = 32 * 8 / 8;

	/**
	 * Size of a cl_int16, in bytes
	 */
	public static final int cl_int16 = 32 * 16 / 8;

	/**
	 * Size of a cl_uint2, in bytes
	 */
	public static final int cl_uint2 = 32 * 2 / 8;

	/**
	 * Size of a cl_uint4, in bytes
	 */
	public static final int cl_uint4 = 32 * 4 / 8;

	/**
	 * Size of a cl_uint8, in bytes
	 */
	public static final int cl_uint8 = 32 * 8 / 8;

	/**
	 * Size of a cl_uint16, in bytes
	 */
	public static final int cl_uint16 = 32 * 16 / 8;

	/**
	 * Size of a cl_long2, in bytes
	 */
	public static final int cl_long2 = 64 * 2 / 8;

	/**
	 * Size of a cl_long4, in bytes
	 */
	public static final int cl_long4 = 64 * 4 / 8;

	/**
	 * Size of a cl_long8, in bytes
	 */
	public static final int cl_long8 = 64 * 8 / 8;

	/**
	 * Size of a cl_long16, in bytes
	 */
	public static final int cl_long16 = 64 * 16 / 8;

	/**
	 * Size of a cl_ulong2, in bytes
	 */
	public static final int cl_ulong2 = 64 * 2 / 8;

	/**
	 * Size of a cl_ulong4, in bytes
	 */
	public static final int cl_ulong4 = 64 * 4 / 8;

	/**
	 * Size of a cl_ulong8, in bytes
	 */
	public static final int cl_ulong8 = 64 * 8 / 8;

	/**
	 * Size of a cl_ulong16, in bytes
	 */
	public static final int cl_ulong16 = 64 * 16 / 8;

	/**
	 * Size of a cl_float2, in bytes
	 */
	public static final int cl_float2 = 32 * 2 / 8;

	/**
	 * Size of a cl_float4, in bytes
	 */
	public static final int cl_float4 = 32 * 4 / 8;

	/**
	 * Size of a cl_float8, in bytes
	 */
	public static final int cl_float8 = 32 * 8 / 8;

	/**
	 * Size of a cl_float16, in bytes
	 */
	public static final int cl_float16 = 32 * 16 / 8;

	/**
	 * Size of a cl_double2, in bytes
	 */
	public static final int cl_double2 = 64 * 2 / 8;

	/**
	 * Size of a cl_double4, in bytes
	 */
	public static final int cl_double4 = 64 * 4 / 8;

	/**
	 * Size of a cl_double8, in bytes
	 */
	public static final int cl_double8 = 64 * 8 / 8;

	/**
	 * Size of a cl_double16, in bytes
	 */
	public static final int cl_double16 = 64 * 16 / 8;

    /**
     * Size of a native pointer, in bytes
     */
    public static final int POINTER = computePointerSize();

    /**
     * Size of a size_t, in bytes
     */
    // TODO: This is assumed to be the same as the pointer size,
    // although this is not clearly specified in the C standard.
    public static final int size_t = POINTER;
    
    /**
     * Size of a cl_platform_id, in bytes
     */
    public static final int cl_platform_id = POINTER;
    
    /**
     * Size of a cl_device_id, in bytes
     */
    public static final int cl_device_id = POINTER;
    
    /**
     * Size of a cl_context, in bytes
     */
    public static final int cl_context = POINTER;
    
    /**
     * Size of a cl_command_queue, in bytes
     */
    public static final int cl_command_queue = POINTER;
    
    /**
     * Size of a cl_mem, in bytes
     */
    public static final int cl_mem = POINTER;
    
    /**
     * Size of a cl_program, in bytes
     */
    public static final int cl_program = POINTER;
    
    /**
     * Size of a cl_kernel, in bytes
     */
    public static final int cl_kernel = POINTER;
    
    /**
     * Size of a cl_event, in bytes
     */
    public static final int cl_event = POINTER;
    
    /**
     * Size of a cl_sampler, in bytes
     */
    public static final int cl_sampler = POINTER;
    

	
    /**
     * Computes the size of a pointer, in bytes
     * 
     * @return The size of a pointer, in bytes
     */
    private static int computePointerSize()
    {
        String bits = System.getProperty("sun.arch.data.model");
        if (bits.equals("32"))
        {
            return 4;
        }
        else if (bits.equals("64"))
        {
            return 8;
        }
        else
        {
            System.err.println(
                "Unknown value for sun.arch.data.model - assuming 32 bits");
            return 4;
        }
    }
	

	/**
	 * Private constructor to prevent instantiation
	 */
	private Sizeof()
	{
	}
}

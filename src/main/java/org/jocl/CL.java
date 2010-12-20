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

import java.lang.ref.*;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * JOCL - Java bindings for OpenCL.<br />
 * <br />
 * The documentation has been extracted from the OpenCL specification.
 * References to tables and sections refer to version 1.0.43 of the
 * OpenCL specification.
 */
public class CL
{
    /**
     * OpenCL version
     */
    public static final int CL_VERSION_1_0 = 1;

    
    // cl_platform.h constants
    public static final int CL_CHAR_BIT         = 8;
    public static final int CL_SCHAR_MAX        = 127;
    public static final int CL_SCHAR_MIN        = (-127-1);
    public static final int CL_CHAR_MAX         = CL_SCHAR_MAX;
    public static final int CL_CHAR_MIN         = CL_SCHAR_MIN;
    public static final int CL_UCHAR_MAX        = 255;
    public static final int CL_SHRT_MAX         = 32767;
    public static final int CL_SHRT_MIN         = (-32767-1);
    public static final int CL_USHRT_MAX        = 65535;
    public static final int CL_INT_MAX          = 2147483647;
    public static final int CL_INT_MIN          = (-2147483647-1);
    public static final long CL_UINT_MAX        = 0xffffffff;
    public static final long CL_LONG_MAX        = 0x7FFFFFFFFFFFFFFFL;
    public static final long CL_LONG_MIN        = -0x7FFFFFFFFFFFFFFFL - 1L;
    public static final long CL_ULONG_MAX       = 0xFFFFFFFFFFFFFFFFL;

    public static final int CL_FLT_DIG          = 6;
    public static final int CL_FLT_MANT_DIG     = 24;
    public static final int CL_FLT_MAX_10_EXP   = +38;
    public static final int CL_FLT_MAX_EXP      = +128;
    public static final int CL_FLT_MIN_10_EXP   = -37;
    public static final int CL_FLT_MIN_EXP      = -125;
    public static final int CL_FLT_RADIX        = 2;
    public static final float CL_FLT_MAX        = 0x1.fffffep127f;
    public static final float CL_FLT_MIN        = 0x1.0p-126f;
    public static final float CL_FLT_EPSILON    = 0x1.0p-23f;

    public static final int CL_DBL_DIG          = 15;
    public static final int CL_DBL_MANT_DIG     = 53;
    public static final int CL_DBL_MAX_10_EXP   = +308;
    public static final int CL_DBL_MAX_EXP      = +1024;
    public static final int CL_DBL_MIN_10_EXP   = -307;
    public static final int CL_DBL_MIN_EXP      = -1021;
    public static final int CL_DBL_RADIX        = 2;
    public static final double CL_DBL_MAX       = 0x1.fffffffffffffp1023;
    public static final double CL_DBL_MIN       = 0x1.0p-1022;
    public static final double CL_DBL_EPSILON   = 0x1.0p-52;
    
    
    
    //=== Constants ==========================================================

    // Error codes
    public static final int CL_SUCCESS                                  = 0;
    public static final int CL_DEVICE_NOT_FOUND                         = -1;
    public static final int CL_DEVICE_NOT_AVAILABLE                     = -2;
    public static final int CL_COMPILER_NOT_AVAILABLE                   = -3;
    public static final int CL_MEM_OBJECT_ALLOCATION_FAILURE            = -4;
    public static final int CL_OUT_OF_RESOURCES                         = -5;
    public static final int CL_OUT_OF_HOST_MEMORY                       = -6;
    public static final int CL_PROFILING_INFO_NOT_AVAILABLE             = -7;
    public static final int CL_MEM_COPY_OVERLAP                         = -8;
    public static final int CL_IMAGE_FORMAT_MISMATCH                    = -9;
    public static final int CL_IMAGE_FORMAT_NOT_SUPPORTED               = -10;
    public static final int CL_BUILD_PROGRAM_FAILURE                    = -11;
    public static final int CL_MAP_FAILURE                              = -12;
    public static final int CL_INVALID_VALUE                            = -30;
    public static final int CL_INVALID_DEVICE_TYPE                      = -31;
    public static final int CL_INVALID_PLATFORM                         = -32;
    public static final int CL_INVALID_DEVICE                           = -33;
    public static final int CL_INVALID_CONTEXT                          = -34;
    public static final int CL_INVALID_QUEUE_PROPERTIES                 = -35;
    public static final int CL_INVALID_COMMAND_QUEUE                    = -36;
    public static final int CL_INVALID_HOST_PTR                         = -37;
    public static final int CL_INVALID_MEM_OBJECT                       = -38;
    public static final int CL_INVALID_IMAGE_FORMAT_DESCRIPTOR          = -39;
    public static final int CL_INVALID_IMAGE_SIZE                       = -40;
    public static final int CL_INVALID_SAMPLER                          = -41;
    public static final int CL_INVALID_BINARY                           = -42;
    public static final int CL_INVALID_BUILD_OPTIONS                    = -43;
    public static final int CL_INVALID_PROGRAM                          = -44;
    public static final int CL_INVALID_PROGRAM_EXECUTABLE               = -45;
    public static final int CL_INVALID_KERNEL_NAME                      = -46;
    public static final int CL_INVALID_KERNEL_DEFINITION                = -47;
    public static final int CL_INVALID_KERNEL                           = -48;
    public static final int CL_INVALID_ARG_INDEX                        = -49;
    public static final int CL_INVALID_ARG_VALUE                        = -50;
    public static final int CL_INVALID_ARG_SIZE                         = -51;
    public static final int CL_INVALID_KERNEL_ARGS                      = -52;
    public static final int CL_INVALID_WORK_DIMENSION                   = -53;
    public static final int CL_INVALID_WORK_GROUP_SIZE                  = -54;
    public static final int CL_INVALID_WORK_ITEM_SIZE                   = -55;
    public static final int CL_INVALID_GLOBAL_OFFSET                    = -56;
    public static final int CL_INVALID_EVENT_WAIT_LIST                  = -57;
    public static final int CL_INVALID_EVENT                            = -58;
    public static final int CL_INVALID_OPERATION                        = -59;
    public static final int CL_INVALID_GL_OBJECT                        = -60;
    public static final int CL_INVALID_BUFFER_SIZE                      = -61;
    public static final int CL_INVALID_MIP_LEVEL                        = -62;
    public static final int CL_INVALID_GLOBAL_WORK_SIZE                 = -63;
    public static final int CL_JOCL_INTERNAL_ERROR                      = -64;
    
    
    // cl_bool
    public static final boolean CL_TRUE = true;
    public static final boolean CL_FALSE = false;
    
    // cl_platform_info
    public static final int CL_PLATFORM_PROFILE = 0x0900;
    public static final int CL_PLATFORM_VERSION = 0x0901;
    public static final int CL_PLATFORM_NAME = 0x0902;
    public static final int CL_PLATFORM_VENDOR = 0x0903;
    public static final int CL_PLATFORM_EXTENSIONS = 0x0904;

    // cl_device_type - bitfield
    public static final long CL_DEVICE_TYPE_DEFAULT = (1 << 0);
    public static final long CL_DEVICE_TYPE_CPU = (1 << 1);
    public static final long CL_DEVICE_TYPE_GPU = (1 << 2);
    public static final long CL_DEVICE_TYPE_ACCELERATOR = (1 << 3);
    public static final long CL_DEVICE_TYPE_ALL = 0xFFFFFFFF;

    // cl_device_info
    public static final int CL_DEVICE_TYPE = 0x1000;
    public static final int CL_DEVICE_VENDOR_ID = 0x1001;
    public static final int CL_DEVICE_MAX_COMPUTE_UNITS = 0x1002;
    public static final int CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS = 0x1003;
    public static final int CL_DEVICE_MAX_WORK_GROUP_SIZE = 0x1004;
    public static final int CL_DEVICE_MAX_WORK_ITEM_SIZES = 0x1005;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR = 0x1006;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT = 0x1007;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT = 0x1008;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG = 0x1009;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT = 0x100A;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE = 0x100B;
    public static final int CL_DEVICE_MAX_CLOCK_FREQUENCY = 0x100C;
    public static final int CL_DEVICE_ADDRESS_BITS = 0x100D;
    public static final int CL_DEVICE_MAX_READ_IMAGE_ARGS = 0x100E;
    public static final int CL_DEVICE_MAX_WRITE_IMAGE_ARGS = 0x100F;
    public static final int CL_DEVICE_MAX_MEM_ALLOC_SIZE = 0x1010;
    public static final int CL_DEVICE_IMAGE2D_MAX_WIDTH = 0x1011;
    public static final int CL_DEVICE_IMAGE2D_MAX_HEIGHT = 0x1012;
    public static final int CL_DEVICE_IMAGE3D_MAX_WIDTH = 0x1013;
    public static final int CL_DEVICE_IMAGE3D_MAX_HEIGHT = 0x1014;
    public static final int CL_DEVICE_IMAGE3D_MAX_DEPTH = 0x1015;
    public static final int CL_DEVICE_IMAGE_SUPPORT = 0x1016;
    public static final int CL_DEVICE_MAX_PARAMETER_SIZE = 0x1017;
    public static final int CL_DEVICE_MAX_SAMPLERS = 0x1018;
    public static final int CL_DEVICE_MEM_BASE_ADDR_ALIGN = 0x1019;
    public static final int CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE = 0x101A;
    public static final int CL_DEVICE_SINGLE_FP_CONFIG = 0x101B;
    public static final int CL_DEVICE_GLOBAL_MEM_CACHE_TYPE = 0x101C;
    public static final int CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE = 0x101D;
    public static final int CL_DEVICE_GLOBAL_MEM_CACHE_SIZE = 0x101E;
    public static final int CL_DEVICE_GLOBAL_MEM_SIZE = 0x101F;
    public static final int CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE = 0x1020;
    public static final int CL_DEVICE_MAX_CONSTANT_ARGS = 0x1021;
    public static final int CL_DEVICE_LOCAL_MEM_TYPE = 0x1022;
    public static final int CL_DEVICE_LOCAL_MEM_SIZE = 0x1023;
    public static final int CL_DEVICE_ERROR_CORRECTION_SUPPORT = 0x1024;
    public static final int CL_DEVICE_PROFILING_TIMER_RESOLUTION = 0x1025;
    public static final int CL_DEVICE_ENDIAN_LITTLE = 0x1026;
    public static final int CL_DEVICE_AVAILABLE = 0x1027;
    public static final int CL_DEVICE_COMPILER_AVAILABLE = 0x1028;
    public static final int CL_DEVICE_EXECUTION_CAPABILITIES = 0x1029;
    public static final int CL_DEVICE_QUEUE_PROPERTIES = 0x102A;
    public static final int CL_DEVICE_NAME = 0x102B;
    public static final int CL_DEVICE_VENDOR = 0x102C;
    public static final int CL_DRIVER_VERSION = 0x102D;
    public static final int CL_DEVICE_PROFILE = 0x102E;
    public static final int CL_DEVICE_VERSION = 0x102F;
    public static final int CL_DEVICE_EXTENSIONS = 0x1030;
    public static final int CL_DEVICE_PLATFORM = 0x1031;

    // cl_device_address_info - bitfield
    public static final long CL_DEVICE_ADDRESS_32_BITS = (1 << 0);
    public static final long CL_DEVICE_ADDRESS_64_BITS = (1 << 1);

    // cl_device_fp_config - bitfield
    public static final long CL_FP_DENORM = (1 << 0);
    public static final long CL_FP_INF_NAN = (1 << 1);
    public static final long CL_FP_ROUND_TO_NEAREST = (1 << 2);
    public static final long CL_FP_ROUND_TO_ZERO = (1 << 3);
    public static final long CL_FP_ROUND_TO_INF = (1 << 4);
    public static final long CL_FP_FMA = (1 << 5);

    // cl_device_mem_cache_type
    public static final int CL_NONE = 0x0;
    public static final int CL_READ_ONLY_CACHE = 0x1;
    public static final int CL_READ_WRITE_CACHE = 0x2;

    // cl_device_local_mem_type
    public static final int CL_LOCAL = 0x1;
    public static final int CL_GLOBAL = 0x2;

    // cl_device_exec_capabilities - bitfield
    public static final long CL_EXEC_KERNEL = (1 << 0);
    public static final long CL_EXEC_NATIVE_KERNEL = (1 << 1);

    // cl_command_queue_properties - bitfield
    public static final long CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE = (1 << 0);
    public static final long CL_QUEUE_PROFILING_ENABLE = (1 << 1);

    // cl_context_info
    public static final int CL_CONTEXT_REFERENCE_COUNT = 0x1080;
    public static final int CL_CONTEXT_DEVICES         = 0x1081;
    public static final int CL_CONTEXT_PROPERTIES      = 0x1082;

    // cl_context_properties
    public static final int CL_CONTEXT_PLATFORM        = 0x1084;

    // cl_command_queue_info
    public static final int CL_QUEUE_CONTEXT = 0x1090;
    public static final int CL_QUEUE_DEVICE = 0x1091;
    public static final int CL_QUEUE_REFERENCE_COUNT = 0x1092;
    public static final int CL_QUEUE_PROPERTIES = 0x1093;

    // cl_mem_flags - bitfield
    public static final long CL_MEM_READ_WRITE = (1 << 0);
    public static final long CL_MEM_WRITE_ONLY = (1 << 1);
    public static final long CL_MEM_READ_ONLY = (1 << 2);
    public static final long CL_MEM_USE_HOST_PTR = (1 << 3);
    public static final long CL_MEM_ALLOC_HOST_PTR = (1 << 4);
    public static final long CL_MEM_COPY_HOST_PTR = (1 << 5);

    // cl_channel_order
    public static final int CL_R = 0x10B0;
    public static final int CL_A = 0x10B1;
    public static final int CL_RG = 0x10B2;
    public static final int CL_RA = 0x10B3;
    public static final int CL_RGB = 0x10B4;
    public static final int CL_RGBA = 0x10B5;
    public static final int CL_BGRA = 0x10B6;
    public static final int CL_ARGB = 0x10B7;
    public static final int CL_INTENSITY = 0x10B8;
    public static final int CL_LUMINANCE = 0x10B9;

    // cl_channel_type
    public static final int CL_SNORM_INT8 = 0x10D0;
    public static final int CL_SNORM_INT16 = 0x10D1;
    public static final int CL_UNORM_INT8 = 0x10D2;
    public static final int CL_UNORM_INT16 = 0x10D3;
    public static final int CL_UNORM_SHORT_565 = 0x10D4;
    public static final int CL_UNORM_SHORT_555 = 0x10D5;
    public static final int CL_UNORM_INT_101010 = 0x10D6;
    public static final int CL_SIGNED_INT8 = 0x10D7;
    public static final int CL_SIGNED_INT16 = 0x10D8;
    public static final int CL_SIGNED_INT32 = 0x10D9;
    public static final int CL_UNSIGNED_INT8 = 0x10DA;
    public static final int CL_UNSIGNED_INT16 = 0x10DB;
    public static final int CL_UNSIGNED_INT32 = 0x10DC;
    public static final int CL_HALF_FLOAT = 0x10DD;
    public static final int CL_FLOAT = 0x10DE;

    // cl_mem_object_type
    public static final int CL_MEM_OBJECT_BUFFER = 0x10F0;
    public static final int CL_MEM_OBJECT_IMAGE2D = 0x10F1;
    public static final int CL_MEM_OBJECT_IMAGE3D = 0x10F2;

    // cl_mem_info
    public static final int CL_MEM_TYPE = 0x1100;
    public static final int CL_MEM_FLAGS = 0x1101;
    public static final int CL_MEM_SIZE = 0x1102;
    public static final int CL_MEM_HOST_PTR = 0x1103;
    public static final int CL_MEM_MAP_COUNT = 0x1104;
    public static final int CL_MEM_REFERENCE_COUNT = 0x1105;
    public static final int CL_MEM_CONTEXT = 0x1106;

    // cl_image_info
    public static final int CL_IMAGE_FORMAT = 0x1110;
    public static final int CL_IMAGE_ELEMENT_SIZE = 0x1111;
    public static final int CL_IMAGE_ROW_PITCH = 0x1112;
    public static final int CL_IMAGE_SLICE_PITCH = 0x1113;
    public static final int CL_IMAGE_WIDTH = 0x1114;
    public static final int CL_IMAGE_HEIGHT = 0x1115;
    public static final int CL_IMAGE_DEPTH = 0x1116;

    // cl_addressing_mode
    public static final int CL_ADDRESS_NONE = 0x1130;
    public static final int CL_ADDRESS_CLAMP_TO_EDGE = 0x1131;
    public static final int CL_ADDRESS_CLAMP = 0x1132;
    public static final int CL_ADDRESS_REPEAT = 0x1133;

    // cl_filter_mode
    public static final int CL_FILTER_NEAREST = 0x1140;
    public static final int CL_FILTER_LINEAR = 0x1141;

    // cl_sampler_info
    public static final int CL_SAMPLER_REFERENCE_COUNT = 0x1150;
    public static final int CL_SAMPLER_CONTEXT = 0x1151;
    public static final int CL_SAMPLER_NORMALIZED_COORDS = 0x1152;
    public static final int CL_SAMPLER_ADDRESSING_MODE = 0x1153;
    public static final int CL_SAMPLER_FILTER_MODE = 0x1154;

    // cl_map_flags - bitfield
    public static final long CL_MAP_READ = (1 << 0);
    public static final long CL_MAP_WRITE = (1 << 1);

    // cl_program_info
    public static final int CL_PROGRAM_REFERENCE_COUNT = 0x1160;
    public static final int CL_PROGRAM_CONTEXT = 0x1161;
    public static final int CL_PROGRAM_NUM_DEVICES = 0x1162;
    public static final int CL_PROGRAM_DEVICES = 0x1163;
    public static final int CL_PROGRAM_SOURCE = 0x1164;
    public static final int CL_PROGRAM_BINARY_SIZES = 0x1165;
    public static final int CL_PROGRAM_BINARIES = 0x1166;

    // cl_program_build_info
    public static final int CL_PROGRAM_BUILD_STATUS = 0x1181;
    public static final int CL_PROGRAM_BUILD_OPTIONS = 0x1182;
    public static final int CL_PROGRAM_BUILD_LOG = 0x1183;

    // cl_build_status
    public static final int CL_BUILD_SUCCESS = 0;
    public static final int CL_BUILD_NONE = -1;
    public static final int CL_BUILD_ERROR = -2;
    public static final int CL_BUILD_IN_PROGRESS = -3;

    // cl_kernel_info
    public static final int CL_KERNEL_FUNCTION_NAME = 0x1190;
    public static final int CL_KERNEL_NUM_ARGS = 0x1191;
    public static final int CL_KERNEL_REFERENCE_COUNT = 0x1192;
    public static final int CL_KERNEL_CONTEXT = 0x1193;
    public static final int CL_KERNEL_PROGRAM = 0x1194;

    // cl_kernel_work_group_info
    public static final int CL_KERNEL_WORK_GROUP_SIZE = 0x11B0;
    public static final int CL_KERNEL_COMPILE_WORK_GROUP_SIZE = 0x11B1;
    public static final int CL_KERNEL_LOCAL_MEM_SIZE = 0x11B2;

    // cl_event_info
    public static final int CL_EVENT_COMMAND_QUEUE = 0x11D0;
    public static final int CL_EVENT_COMMAND_TYPE = 0x11D1;
    public static final int CL_EVENT_REFERENCE_COUNT = 0x11D2;
    public static final int CL_EVENT_COMMAND_EXECUTION_STATUS = 0x11D3;

    // cl_command_type
    public static final int CL_COMMAND_NDRANGE_KERNEL = 0x11F0;
    public static final int CL_COMMAND_TASK = 0x11F1;
    public static final int CL_COMMAND_NATIVE_KERNEL = 0x11F2;
    public static final int CL_COMMAND_READ_BUFFER = 0x11F3;
    public static final int CL_COMMAND_WRITE_BUFFER = 0x11F4;
    public static final int CL_COMMAND_COPY_BUFFER = 0x11F5;
    public static final int CL_COMMAND_READ_IMAGE = 0x11F6;
    public static final int CL_COMMAND_WRITE_IMAGE = 0x11F7;
    public static final int CL_COMMAND_COPY_IMAGE = 0x11F8;
    public static final int CL_COMMAND_COPY_IMAGE_TO_BUFFER = 0x11F9;
    public static final int CL_COMMAND_COPY_BUFFER_TO_IMAGE = 0x11FA;
    public static final int CL_COMMAND_MAP_BUFFER = 0x11FB;
    public static final int CL_COMMAND_MAP_IMAGE = 0x11FC;
    public static final int CL_COMMAND_UNMAP_MEM_OBJECT = 0x11FD;
    public static final int CL_COMMAND_MARKER = 0x11FE;
    public static final int CL_COMMAND_ACQUIRE_GL_OBJECTS = 0x11FF;
    public static final int CL_COMMAND_RELEASE_GL_OBJECTS = 0x1200;

    // command execution status
    public static final int CL_COMPLETE = 0x0;
    public static final int CL_RUNNING = 0x1;
    public static final int CL_SUBMITTED = 0x2;
    public static final int CL_QUEUED = 0x3;

    // cl_profiling_info
    public static final int CL_PROFILING_COMMAND_QUEUED = 0x1280;
    public static final int CL_PROFILING_COMMAND_SUBMIT = 0x1281;
    public static final int CL_PROFILING_COMMAND_START = 0x1282;
    public static final int CL_PROFILING_COMMAND_END = 0x1283;

    public static final int CL_GL_CONTEXT                   = 0x1071;

    // cl_gl_object_type
    public static final int CL_GL_OBJECT_BUFFER             = 0x2000;
    public static final int CL_GL_OBJECT_TEXTURE2D          = 0x2001;
    public static final int CL_GL_OBJECT_TEXTURE3D          = 0x2002;
    public static final int CL_GL_OBJECT_RENDERBUFFER       = 0x2003;

     // cl_gl_texture_info
    public static final int CL_GL_TEXTURE_TARGET            = 0x2004;
    public static final int CL_GL_MIPMAP_LEVEL              = 0x2005;
    
    /**
     * Whether JOCL was already initialized and the native library
     * was already loaded
     */
    private static boolean initialized = false;
    
    /**
     * Indicates whether exceptions are enabled. When exceptions are
     * enabled, CLException is thrown if a method is about to return 
     * a result code that is not CL.CL_SUCCESS
     */
    private static boolean exceptionsEnabled = false;
    
    /**
     * A map from Weak references to aligned ByteBuffers that have been 
     * allocated with {@link CL#allocateAligned(int, int)} to 
     * pointers that contain the native memory address of the 
     * respective buffer.
     */
    private static Map<WeakReference<ByteBuffer>, Pointer> alignedByteBufferMap = 
        new HashMap<WeakReference<ByteBuffer>, Pointer>();
    
    /**
     * The reference queue for the weak references to aligned ByteBuffers
     * that have been allocated with {@link CL#allocateAligned(int, int)}
     * and which have detected to be unreachable by the garbage collector. 
     */
    private static ReferenceQueue<ByteBuffer> alignedByteBufferReferenceQueue = 
        new ReferenceQueue<ByteBuffer>();    
    
    
    /**
     * Enables or disables exceptions. By default, the methods of this class
     * only return the error code from the underlying OpenCL function.
     * If exceptions are enabled, a CLException with a detailed error 
     * message will be thrown if a method is about to return a result code 
     * that is not CL_SUCCESS
     * 
     * @param enabled Whether exceptions are enabled
     */
    public static void setExceptionsEnabled(boolean enabled)
    {
        exceptionsEnabled = enabled;
    }

    /**
     * If the given result is different to CL_SUCCESS and
     * exceptions have been enabled, this method will throw a 
     * CLException with an error message that corresponds to the
     * given result code. Otherwise, the given result is simply
     * returned.
     * 
     * @param result The result to check
     * @return The result that was given as the parameter
     * @throws CLException If exceptions have been enabled and
     * the given result code is not CL_SUCCESS
     */
    private static int checkResult(int result)
    {
        if (exceptionsEnabled && result != CL_SUCCESS)
        {
            throw new CLException(stringFor_errorCode(result));
        }
        return result;
    }

    
    
    
    //=== String methods for constants =======================================

    
    /**
     * Returns the String identifying the given error code
     * 
     * @param n The error code
     * @return The String identifying the given error code
     */
    public static String stringFor_errorCode(int n)
    {
        switch (n)
        {
            case CL_SUCCESS: return "CL_SUCCESS";
            case CL_DEVICE_NOT_FOUND: return "CL_DEVICE_NOT_FOUND";
            case CL_DEVICE_NOT_AVAILABLE: return "CL_DEVICE_NOT_AVAILABLE";
            case CL_COMPILER_NOT_AVAILABLE: return "CL_COMPILER_NOT_AVAILABLE";
            case CL_MEM_OBJECT_ALLOCATION_FAILURE: return "CL_MEM_OBJECT_ALLOCATION_FAILURE";
            case CL_OUT_OF_RESOURCES: return "CL_OUT_OF_RESOURCES";
            case CL_OUT_OF_HOST_MEMORY: return "CL_OUT_OF_HOST_MEMORY";
            case CL_PROFILING_INFO_NOT_AVAILABLE: return "CL_PROFILING_INFO_NOT_AVAILABLE";
            case CL_MEM_COPY_OVERLAP: return "CL_MEM_COPY_OVERLAP";
            case CL_IMAGE_FORMAT_MISMATCH: return "CL_IMAGE_FORMAT_MISMATCH";
            case CL_IMAGE_FORMAT_NOT_SUPPORTED: return "CL_IMAGE_FORMAT_NOT_SUPPORTED";
            case CL_BUILD_PROGRAM_FAILURE: return "CL_BUILD_PROGRAM_FAILURE";
            case CL_MAP_FAILURE: return "CL_MAP_FAILURE";
            case CL_INVALID_VALUE: return "CL_INVALID_VALUE";
            case CL_INVALID_DEVICE_TYPE: return "CL_INVALID_DEVICE_TYPE";
            case CL_INVALID_PLATFORM: return "CL_INVALID_PLATFORM";
            case CL_INVALID_DEVICE: return "CL_INVALID_DEVICE";
            case CL_INVALID_CONTEXT: return "CL_INVALID_CONTEXT";
            case CL_INVALID_QUEUE_PROPERTIES: return "CL_INVALID_QUEUE_PROPERTIES";
            case CL_INVALID_COMMAND_QUEUE: return "CL_INVALID_COMMAND_QUEUE";
            case CL_INVALID_HOST_PTR: return "CL_INVALID_HOST_PTR";
            case CL_INVALID_MEM_OBJECT: return "CL_INVALID_MEM_OBJECT";
            case CL_INVALID_IMAGE_FORMAT_DESCRIPTOR: return "CL_INVALID_IMAGE_FORMAT_DESCRIPTOR";
            case CL_INVALID_IMAGE_SIZE: return "CL_INVALID_IMAGE_SIZE";
            case CL_INVALID_SAMPLER: return "CL_INVALID_SAMPLER";
            case CL_INVALID_BINARY: return "CL_INVALID_BINARY";
            case CL_INVALID_BUILD_OPTIONS: return "CL_INVALID_BUILD_OPTIONS";
            case CL_INVALID_PROGRAM: return "CL_INVALID_PROGRAM";
            case CL_INVALID_PROGRAM_EXECUTABLE: return "CL_INVALID_PROGRAM_EXECUTABLE";
            case CL_INVALID_KERNEL_NAME: return "CL_INVALID_KERNEL_NAME";
            case CL_INVALID_KERNEL_DEFINITION: return "CL_INVALID_KERNEL_DEFINITION";
            case CL_INVALID_KERNEL: return "CL_INVALID_KERNEL";
            case CL_INVALID_ARG_INDEX: return "CL_INVALID_ARG_INDEX";
            case CL_INVALID_ARG_VALUE: return "CL_INVALID_ARG_VALUE";
            case CL_INVALID_ARG_SIZE: return "CL_INVALID_ARG_SIZE";
            case CL_INVALID_KERNEL_ARGS: return "CL_INVALID_KERNEL_ARGS";
            case CL_INVALID_WORK_DIMENSION: return "CL_INVALID_WORK_DIMENSION";
            case CL_INVALID_WORK_GROUP_SIZE: return "CL_INVALID_WORK_GROUP_SIZE";
            case CL_INVALID_WORK_ITEM_SIZE: return "CL_INVALID_WORK_ITEM_SIZE";
            case CL_INVALID_GLOBAL_OFFSET: return "CL_INVALID_GLOBAL_OFFSET";
            case CL_INVALID_EVENT_WAIT_LIST: return "CL_INVALID_EVENT_WAIT_LIST";
            case CL_INVALID_EVENT: return "CL_INVALID_EVENT";
            case CL_INVALID_OPERATION: return "CL_INVALID_OPERATION";
            case CL_INVALID_GL_OBJECT: return "CL_INVALID_GL_OBJECT";
            case CL_INVALID_BUFFER_SIZE: return "CL_INVALID_BUFFER_SIZE";
            case CL_INVALID_MIP_LEVEL: return "CL_INVALID_MIP_LEVEL";
            case CL_INVALID_GLOBAL_WORK_SIZE: return "CL_INVALID_GLOBAL_WORK_SIZE";
            case CL_JOCL_INTERNAL_ERROR: return "CL_JOCL_INTERNAL_ERROR";
            
            // Some OpenCL implementation return 1 for glBuildProgram
            // if the source code contains errors...
            case 1: return "Error in program source code";
        }
        return "INVALID error code: "+n;
    }
    
    /**
     * Returns the String identifying the given cl_platform_info
     *
     * @param n A cl_platform_info value
     * @return The String for the given cl_platform_info
     */
    public static String stringFor_cl_platform_info(int n)
    {
        switch (n)
        {
            case CL_PLATFORM_PROFILE: return "CL_PLATFORM_PROFILE";
            case CL_PLATFORM_VERSION: return "CL_PLATFORM_VERSION";
            case CL_PLATFORM_NAME: return "CL_PLATFORM_NAME";
            case CL_PLATFORM_VENDOR: return "CL_PLATFORM_VENDOR";
            case CL_PLATFORM_EXTENSIONS: return "CL_PLATFORM_EXTENSIONS";
        }
        return "INVALID cl_platform_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_device_info
     *
     * @param n A cl_device_info value
     * @return The String for the given cl_device_info
     */
    public static String stringFor_cl_device_info(int n)
    {
        switch (n)
        {
            case CL_DEVICE_TYPE: return "CL_DEVICE_TYPE";
            case CL_DEVICE_VENDOR_ID: return "CL_DEVICE_VENDOR_ID";
            case CL_DEVICE_MAX_COMPUTE_UNITS: return "CL_DEVICE_MAX_COMPUTE_UNITS";
            case CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS: return "CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS";
            case CL_DEVICE_MAX_WORK_GROUP_SIZE: return "CL_DEVICE_MAX_WORK_GROUP_SIZE";
            case CL_DEVICE_MAX_WORK_ITEM_SIZES: return "CL_DEVICE_MAX_WORK_ITEM_SIZES";
            case CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR: return "CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR";
            case CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT: return "CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT";
            case CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT: return "CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT";
            case CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG: return "CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG";
            case CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT: return "CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT";
            case CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE: return "CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE";
            case CL_DEVICE_MAX_CLOCK_FREQUENCY: return "CL_DEVICE_MAX_CLOCK_FREQUENCY";
            case CL_DEVICE_ADDRESS_BITS: return "CL_DEVICE_ADDRESS_BITS";
            case CL_DEVICE_MAX_READ_IMAGE_ARGS: return "CL_DEVICE_MAX_READ_IMAGE_ARGS";
            case CL_DEVICE_MAX_WRITE_IMAGE_ARGS: return "CL_DEVICE_MAX_WRITE_IMAGE_ARGS";
            case CL_DEVICE_MAX_MEM_ALLOC_SIZE: return "CL_DEVICE_MAX_MEM_ALLOC_SIZE";
            case CL_DEVICE_IMAGE2D_MAX_WIDTH: return "CL_DEVICE_IMAGE2D_MAX_WIDTH";
            case CL_DEVICE_IMAGE2D_MAX_HEIGHT: return "CL_DEVICE_IMAGE2D_MAX_HEIGHT";
            case CL_DEVICE_IMAGE3D_MAX_WIDTH: return "CL_DEVICE_IMAGE3D_MAX_WIDTH";
            case CL_DEVICE_IMAGE3D_MAX_HEIGHT: return "CL_DEVICE_IMAGE3D_MAX_HEIGHT";
            case CL_DEVICE_IMAGE3D_MAX_DEPTH: return "CL_DEVICE_IMAGE3D_MAX_DEPTH";
            case CL_DEVICE_IMAGE_SUPPORT: return "CL_DEVICE_IMAGE_SUPPORT";
            case CL_DEVICE_MAX_PARAMETER_SIZE: return "CL_DEVICE_MAX_PARAMETER_SIZE";
            case CL_DEVICE_MAX_SAMPLERS: return "CL_DEVICE_MAX_SAMPLERS";
            case CL_DEVICE_MEM_BASE_ADDR_ALIGN: return "CL_DEVICE_MEM_BASE_ADDR_ALIGN";
            case CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE: return "CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE";
            case CL_DEVICE_SINGLE_FP_CONFIG: return "CL_DEVICE_SINGLE_FP_CONFIG";
            case CL_DEVICE_GLOBAL_MEM_CACHE_TYPE: return "CL_DEVICE_GLOBAL_MEM_CACHE_TYPE";
            case CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE: return "CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE";
            case CL_DEVICE_GLOBAL_MEM_CACHE_SIZE: return "CL_DEVICE_GLOBAL_MEM_CACHE_SIZE";
            case CL_DEVICE_GLOBAL_MEM_SIZE: return "CL_DEVICE_GLOBAL_MEM_SIZE";
            case CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE: return "CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE";
            case CL_DEVICE_MAX_CONSTANT_ARGS: return "CL_DEVICE_MAX_CONSTANT_ARGS";
            case CL_DEVICE_LOCAL_MEM_TYPE: return "CL_DEVICE_LOCAL_MEM_TYPE";
            case CL_DEVICE_LOCAL_MEM_SIZE: return "CL_DEVICE_LOCAL_MEM_SIZE";
            case CL_DEVICE_ERROR_CORRECTION_SUPPORT: return "CL_DEVICE_ERROR_CORRECTION_SUPPORT";
            case CL_DEVICE_PROFILING_TIMER_RESOLUTION: return "CL_DEVICE_PROFILING_TIMER_RESOLUTION";
            case CL_DEVICE_ENDIAN_LITTLE: return "CL_DEVICE_ENDIAN_LITTLE";
            case CL_DEVICE_AVAILABLE: return "CL_DEVICE_AVAILABLE";
            case CL_DEVICE_COMPILER_AVAILABLE: return "CL_DEVICE_COMPILER_AVAILABLE";
            case CL_DEVICE_EXECUTION_CAPABILITIES: return "CL_DEVICE_EXECUTION_CAPABILITIES";
            case CL_DEVICE_QUEUE_PROPERTIES: return "CL_DEVICE_QUEUE_PROPERTIES";
            case CL_DEVICE_NAME: return "CL_DEVICE_NAME";
            case CL_DEVICE_VENDOR: return "CL_DEVICE_VENDOR";
            case CL_DRIVER_VERSION: return "CL_DRIVER_VERSION";
            case CL_DEVICE_PROFILE: return "CL_DEVICE_PROFILE";
            case CL_DEVICE_VERSION: return "CL_DEVICE_VERSION";
            case CL_DEVICE_EXTENSIONS: return "CL_DEVICE_EXTENSIONS";
            case CL_DEVICE_PLATFORM: return "CL_DEVICE_PLATFORM";
        }
        return "INVALID cl_device_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_device_mem_cache_type
     *
     * @param n A cl_device_mem_cache_type value
     * @return The String for the given cl_device_mem_cache_type
     */
    public static String stringFor_cl_device_mem_cache_type(int n)
    {
        switch (n)
        {
            case CL_NONE: return "CL_NONE";
            case CL_READ_ONLY_CACHE: return "CL_READ_ONLY_CACHE";
            case CL_READ_WRITE_CACHE: return "CL_READ_WRITE_CACHE";
        }
        return "INVALID cl_device_mem_cache_type: " + n;
    }

    /**
     * Returns the String identifying the given cl_device_local_mem_type
     *
     * @param n A cl_device_local_mem_type value
     * @return The String for the given cl_device_local_mem_type
     */
    public static String stringFor_cl_device_local_mem_type(int n)
    {
        switch (n)
        {
            case CL_LOCAL: return "CL_LOCAL";
            case CL_GLOBAL: return "CL_GLOBAL";
        }
        return "INVALID cl_device_local_mem_type: " + n;
    }

    /**
     * Returns the String identifying the given cl_context_info
     *
     * @param n A cl_context_info value
     * @return The String for the given cl_context_info
     */
    public static String stringFor_cl_context_info(int n)
    {
        switch (n)
        {
            case CL_CONTEXT_REFERENCE_COUNT: return "CL_CONTEXT_REFERENCE_COUNT";
            case CL_CONTEXT_DEVICES: return "CL_CONTEXT_DEVICES";
            case CL_CONTEXT_PROPERTIES: return "CL_CONTEXT_PROPERTIES";
        }
        return "INVALID cl_context_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_context_properties
     *
     * @param n A cl_context_properties value
     * @return The String for the given cl_context_properties
     */
    public static String stringFor_cl_context_properties(int n)
    {
        switch (n)
        {
            case CL_CONTEXT_PLATFORM: return "CL_CONTEXT_PLATFORM";
        }
        return "INVALID cl_context_properties: " + n;
    }
    
    

    /**
     * Returns the String identifying the given cl_command_queue_info
     *
     * @param n A cl_command_queue_info value
     * @return The String for the given cl_command_queue_info
     */
    public static String stringFor_cl_command_queue_info(int n)
    {
        switch (n)
        {
            case CL_QUEUE_CONTEXT: return "CL_QUEUE_CONTEXT";
            case CL_QUEUE_DEVICE: return "CL_QUEUE_DEVICE";
            case CL_QUEUE_REFERENCE_COUNT: return "CL_QUEUE_REFERENCE_COUNT";
            case CL_QUEUE_PROPERTIES: return "CL_QUEUE_PROPERTIES";
        }
        return "INVALID cl_command_queue_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_channel_order
     *
     * @param n A cl_channel_order value
     * @return The String for the given cl_channel_order
     */
    public static String stringFor_cl_channel_order(int n)
    {
        switch (n)
        {
            case CL_R: return "CL_R";
            case CL_A: return "CL_A";
            case CL_RG: return "CL_RG";
            case CL_RA: return "CL_RA";
            case CL_RGB: return "CL_RGB";
            case CL_RGBA: return "CL_RGBA";
            case CL_BGRA: return "CL_BGRA";
            case CL_ARGB: return "CL_ARGB";
            case CL_INTENSITY: return "CL_INTENSITY";
            case CL_LUMINANCE: return "CL_LUMINANCE";
        }
        return "INVALID cl_channel_order: " + n;
    }

    /**
     * Returns the String identifying the given cl_channel_type
     *
     * @param n A cl_channel_type value
     * @return The String for the given cl_channel_type
     */
    public static String stringFor_cl_channel_type(int n)
    {
        switch (n)
        {
            case CL_SNORM_INT8: return "CL_SNORM_INT8";
            case CL_SNORM_INT16: return "CL_SNORM_INT16";
            case CL_UNORM_INT8: return "CL_UNORM_INT8";
            case CL_UNORM_INT16: return "CL_UNORM_INT16";
            case CL_UNORM_SHORT_565: return "CL_UNORM_SHORT_565";
            case CL_UNORM_SHORT_555: return "CL_UNORM_SHORT_555";
            case CL_UNORM_INT_101010: return "CL_UNORM_INT_101010";
            case CL_SIGNED_INT8: return "CL_SIGNED_INT8";
            case CL_SIGNED_INT16: return "CL_SIGNED_INT16";
            case CL_SIGNED_INT32: return "CL_SIGNED_INT32";
            case CL_UNSIGNED_INT8: return "CL_UNSIGNED_INT8";
            case CL_UNSIGNED_INT16: return "CL_UNSIGNED_INT16";
            case CL_UNSIGNED_INT32: return "CL_UNSIGNED_INT32";
            case CL_HALF_FLOAT: return "CL_HALF_FLOAT";
            case CL_FLOAT: return "CL_FLOAT";
        }
        return "INVALID cl_channel_type: " + n;
    }

    /**
     * Returns the String identifying the given cl_mem_object_type
     *
     * @param n A cl_mem_object_type value
     * @return The String for the given cl_mem_object_type
     */
    public static String stringFor_cl_mem_object_type(int n)
    {
        switch (n)
        {
            case CL_MEM_OBJECT_BUFFER: return "CL_MEM_OBJECT_BUFFER";
            case CL_MEM_OBJECT_IMAGE2D: return "CL_MEM_OBJECT_IMAGE2D";
            case CL_MEM_OBJECT_IMAGE3D: return "CL_MEM_OBJECT_IMAGE3D";
        }
        return "INVALID cl_mem_object_type: " + n;
    }

    /**
     * Returns the String identifying the given cl_mem_info
     *
     * @param n A cl_mem_info value
     * @return The String for the given cl_mem_info
     */
    public static String stringFor_cl_mem_info(int n)
    {
        switch (n)
        {
            case CL_MEM_TYPE: return "CL_MEM_TYPE";
            case CL_MEM_FLAGS: return "CL_MEM_FLAGS";
            case CL_MEM_SIZE: return "CL_MEM_SIZE";
            case CL_MEM_HOST_PTR: return "CL_MEM_HOST_PTR";
            case CL_MEM_MAP_COUNT: return "CL_MEM_MAP_COUNT";
            case CL_MEM_REFERENCE_COUNT: return "CL_MEM_REFERENCE_COUNT";
            case CL_MEM_CONTEXT: return "CL_MEM_CONTEXT";
        }
        return "INVALID cl_mem_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_image_info
     *
     * @param n A cl_image_info value
     * @return The String for the given cl_image_info
     */
    public static String stringFor_cl_image_info(int n)
    {
        switch (n)
        {
            case CL_IMAGE_FORMAT: return "CL_IMAGE_FORMAT";
            case CL_IMAGE_ELEMENT_SIZE: return "CL_IMAGE_ELEMENT_SIZE";
            case CL_IMAGE_ROW_PITCH: return "CL_IMAGE_ROW_PITCH";
            case CL_IMAGE_SLICE_PITCH: return "CL_IMAGE_SLICE_PITCH";
            case CL_IMAGE_WIDTH: return "CL_IMAGE_WIDTH";
            case CL_IMAGE_HEIGHT: return "CL_IMAGE_HEIGHT";
            case CL_IMAGE_DEPTH: return "CL_IMAGE_DEPTH";
        }
        return "INVALID cl_image_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_addressing_mode
     *
     * @param n A cl_addressing_mode value
     * @return The String for the given cl_addressing_mode
     */
    public static String stringFor_cl_addressing_mode(int n)
    {
        switch (n)
        {
            case CL_ADDRESS_NONE: return "CL_ADDRESS_NONE";
            case CL_ADDRESS_CLAMP_TO_EDGE: return "CL_ADDRESS_CLAMP_TO_EDGE";
            case CL_ADDRESS_CLAMP: return "CL_ADDRESS_CLAMP";
            case CL_ADDRESS_REPEAT: return "CL_ADDRESS_REPEAT";
        }
        return "INVALID cl_addressing_mode: " + n;
    }

    /**
     * Returns the String identifying the given cl_filter_mode
     *
     * @param n A cl_filter_mode value
     * @return The String for the given cl_filter_mode
     */
    public static String stringFor_cl_filter_mode(int n)
    {
        switch (n)
        {
            case CL_FILTER_NEAREST: return "CL_FILTER_NEAREST";
            case CL_FILTER_LINEAR: return "CL_FILTER_LINEAR";
        }
        return "INVALID cl_filter_mode: " + n;
    }

    /**
     * Returns the String identifying the given cl_sampler_info
     *
     * @param n A cl_sampler_info value
     * @return The String for the given cl_sampler_info
     */
    public static String stringFor_cl_sampler_info(int n)
    {
        switch (n)
        {
            case CL_SAMPLER_REFERENCE_COUNT: return "CL_SAMPLER_REFERENCE_COUNT";
            case CL_SAMPLER_CONTEXT: return "CL_SAMPLER_CONTEXT";
            case CL_SAMPLER_NORMALIZED_COORDS: return "CL_SAMPLER_NORMALIZED_COORDS";
            case CL_SAMPLER_ADDRESSING_MODE: return "CL_SAMPLER_ADDRESSING_MODE";
            case CL_SAMPLER_FILTER_MODE: return "CL_SAMPLER_FILTER_MODE";
        }
        return "INVALID cl_sampler_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_program_info
     *
     * @param n A cl_program_info value
     * @return The String for the given cl_program_info
     */
    public static String stringFor_cl_program_info(int n)
    {
        switch (n)
        {
            case CL_PROGRAM_REFERENCE_COUNT: return "CL_PROGRAM_REFERENCE_COUNT";
            case CL_PROGRAM_CONTEXT: return "CL_PROGRAM_CONTEXT";
            case CL_PROGRAM_NUM_DEVICES: return "CL_PROGRAM_NUM_DEVICES";
            case CL_PROGRAM_DEVICES: return "CL_PROGRAM_DEVICES";
            case CL_PROGRAM_SOURCE: return "CL_PROGRAM_SOURCE";
            case CL_PROGRAM_BINARY_SIZES: return "CL_PROGRAM_BINARY_SIZES";
            case CL_PROGRAM_BINARIES: return "CL_PROGRAM_BINARIES";
        }
        return "INVALID cl_program_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_program_build_info
     *
     * @param n A cl_program_build_info value
     * @return The String for the given cl_program_build_info
     */
    public static String stringFor_cl_program_build_info(int n)
    {
        switch (n)
        {
            case CL_PROGRAM_BUILD_STATUS: return "CL_PROGRAM_BUILD_STATUS";
            case CL_PROGRAM_BUILD_OPTIONS: return "CL_PROGRAM_BUILD_OPTIONS";
            case CL_PROGRAM_BUILD_LOG: return "CL_PROGRAM_BUILD_LOG";
        }
        return "INVALID cl_program_build_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_build_status
     *
     * @param n A cl_build_status value
     * @return The String for the given cl_build_status
     */
    public static String stringFor_cl_build_status(int n)
    {
        switch (n)
        {
            case CL_BUILD_SUCCESS: return "CL_BUILD_SUCCESS";
            case CL_BUILD_NONE: return "CL_BUILD_NONE";
            case CL_BUILD_ERROR: return "CL_BUILD_ERROR";
            case CL_BUILD_IN_PROGRESS: return "CL_BUILD_IN_PROGRESS";
        }
        return "INVALID cl_build_status: " + n;
    }

    /**
     * Returns the String identifying the given cl_kernel_info
     *
     * @param n A cl_kernel_info value
     * @return The String for the given cl_kernel_info
     */
    public static String stringFor_cl_kernel_info(int n)
    {
        switch (n)
        {
            case CL_KERNEL_FUNCTION_NAME: return "CL_KERNEL_FUNCTION_NAME";
            case CL_KERNEL_NUM_ARGS: return "CL_KERNEL_NUM_ARGS";
            case CL_KERNEL_REFERENCE_COUNT: return "CL_KERNEL_REFERENCE_COUNT";
            case CL_KERNEL_CONTEXT: return "CL_KERNEL_CONTEXT";
            case CL_KERNEL_PROGRAM: return "CL_KERNEL_PROGRAM";
        }
        return "INVALID cl_kernel_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_kernel_work_group_info
     *
     * @param n A cl_kernel_work_group_info value
     * @return The String for the given cl_kernel_work_group_info
     */
    public static String stringFor_cl_kernel_work_group_info(int n)
    {
        switch (n)
        {
            case CL_KERNEL_WORK_GROUP_SIZE: return "CL_KERNEL_WORK_GROUP_SIZE";
            case CL_KERNEL_COMPILE_WORK_GROUP_SIZE: return "CL_KERNEL_COMPILE_WORK_GROUP_SIZE";
            case CL_KERNEL_LOCAL_MEM_SIZE: return "CL_KERNEL_LOCAL_MEM_SIZE";
        }
        return "INVALID cl_kernel_work_group_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_event_info
     *
     * @param n A cl_event_info value
     * @return The String for the given cl_event_info
     */
    public static String stringFor_cl_event_info(int n)
    {
        switch (n)
        {
            case CL_EVENT_COMMAND_QUEUE: return "CL_EVENT_COMMAND_QUEUE";
            case CL_EVENT_COMMAND_TYPE: return "CL_EVENT_COMMAND_TYPE";
            case CL_EVENT_REFERENCE_COUNT: return "CL_EVENT_REFERENCE_COUNT";
            case CL_EVENT_COMMAND_EXECUTION_STATUS: return "CL_EVENT_COMMAND_EXECUTION_STATUS";
        }
        return "INVALID cl_event_info: " + n;
    }

    /**
     * Returns the String identifying the given cl_command_type
     *
     * @param n A cl_command_type value
     * @return The String for the given cl_command_type
     */
    public static String stringFor_cl_command_type(int n)
    {
        switch (n)
        {
            case CL_COMMAND_NDRANGE_KERNEL: return "CL_COMMAND_NDRANGE_KERNEL";
            case CL_COMMAND_TASK: return "CL_COMMAND_TASK";
            case CL_COMMAND_NATIVE_KERNEL: return "CL_COMMAND_NATIVE_KERNEL";
            case CL_COMMAND_READ_BUFFER: return "CL_COMMAND_READ_BUFFER";
            case CL_COMMAND_WRITE_BUFFER: return "CL_COMMAND_WRITE_BUFFER";
            case CL_COMMAND_COPY_BUFFER: return "CL_COMMAND_COPY_BUFFER";
            case CL_COMMAND_READ_IMAGE: return "CL_COMMAND_READ_IMAGE";
            case CL_COMMAND_WRITE_IMAGE: return "CL_COMMAND_WRITE_IMAGE";
            case CL_COMMAND_COPY_IMAGE: return "CL_COMMAND_COPY_IMAGE";
            case CL_COMMAND_COPY_IMAGE_TO_BUFFER: return "CL_COMMAND_COPY_IMAGE_TO_BUFFER";
            case CL_COMMAND_COPY_BUFFER_TO_IMAGE: return "CL_COMMAND_COPY_BUFFER_TO_IMAGE";
            case CL_COMMAND_MAP_BUFFER: return "CL_COMMAND_MAP_BUFFER";
            case CL_COMMAND_MAP_IMAGE: return "CL_COMMAND_MAP_IMAGE";
            case CL_COMMAND_UNMAP_MEM_OBJECT: return "CL_COMMAND_UNMAP_MEM_OBJECT";
            case CL_COMMAND_MARKER: return "CL_COMMAND_MARKER";
            case CL_COMMAND_ACQUIRE_GL_OBJECTS: return "CL_COMMAND_ACQUIRE_GL_OBJECTS";
            case CL_COMMAND_RELEASE_GL_OBJECTS: return "CL_COMMAND_RELEASE_GL_OBJECTS";
        }
        return "INVALID cl_command_type: " + n;
    }

    /**
     * Returns the command execution String identifying the given command_execution_status
     *
     * @param n A command_execution_status value
     * @return The String for the given command_execution_status
     */
    public static String stringFor_command_execution_status(int n)
    {
        switch (n)
        {
            case CL_COMPLETE: return "CL_COMPLETE";
            case CL_RUNNING: return "CL_RUNNING";
            case CL_SUBMITTED: return "CL_SUBMITTED";
            case CL_QUEUED: return "CL_QUEUED";
        }
        return "INVALID command_execution_status: " + n;

    }

    /**
     * Returns the String identifying the given cl_profiling_info
     *
     * @param n A cl_profiling_info value
     * @return The String for the given cl_profiling_info
     */
    public static String stringFor_cl_profiling_info(int n)
    {
        switch (n)
        {
            case CL_PROFILING_COMMAND_QUEUED: return "CL_PROFILING_COMMAND_QUEUED";
            case CL_PROFILING_COMMAND_SUBMIT: return "CL_PROFILING_COMMAND_SUBMIT";
            case CL_PROFILING_COMMAND_START: return "CL_PROFILING_COMMAND_START";
            case CL_PROFILING_COMMAND_END: return "CL_PROFILING_COMMAND_END";
        }
        return "INVALID cl_profiling_info: " + n;
    }

    
    /**
     * Returns the String identifying the given cl_gl_object_type
     *
     * @param n A cl_gl_object_type value
     * @return The String for the given cl_gl_object_type
     */
    public static String stringFor_cl_gl_object_type(int n)
    {
        switch (n)
        {
            case CL_GL_OBJECT_BUFFER: return "CL_GL_OBJECT_BUFFER";
            case CL_GL_OBJECT_TEXTURE2D: return "CL_GL_OBJECT_TEXTURE2D";
            case CL_GL_OBJECT_TEXTURE3D: return "CL_GL_OBJECT_TEXTURE3D";
            case CL_GL_OBJECT_RENDERBUFFER: return "CL_GL_OBJECT_RENDERBUFFER";
        }
        return "INVALID cl_gl_object_type: " + n;
    }
    
    /**
     * Returns the String identifying the given cl_gl_texture_info
     *
     * @param n A cl_gl_texture_info value
     * @return The String for the given cl_gl_texture_info
     */
    public static String stringFor_cl_gl_texture_info(int n)
    {
        switch (n)
        {
            case CL_GL_TEXTURE_TARGET: return "CL_GL_TEXTURE_TARGET";
            case CL_GL_MIPMAP_LEVEL: return "CL_GL_MIPMAP_LEVEL";
            case CL_GL_OBJECT_TEXTURE3D: return "CL_GL_OBJECT_TEXTURE3D";
            case CL_GL_OBJECT_RENDERBUFFER: return "CL_GL_OBJECT_RENDERBUFFER";
        }
        return "INVALID cl_gl_texture_info: " + n;
    }
    
    
    //=== String methods for constants of bitfields ==========================
    
    /**
     * Returns the string describing the given cl_device_type - bitfield
     *
     * @param n The cl_device_type - bitfield
     * @return The string describing the cl_device_type - bitfield
     */
    public static String stringFor_cl_device_type(long n)
    {
        if (n == 0)
        {
            return "(none)";
        }
        String result = "";
        if ((n & CL_DEVICE_TYPE_ALL) != CL_DEVICE_TYPE_ALL) result += "CL_DEVICE_TYPE_ALL ";
        if ((n & CL_DEVICE_TYPE_DEFAULT) != 0) result += "CL_DEVICE_TYPE_DEFAULT ";
        if ((n & CL_DEVICE_TYPE_CPU) != 0) result += "CL_DEVICE_TYPE_CPU ";
        if ((n & CL_DEVICE_TYPE_GPU) != 0) result += "CL_DEVICE_TYPE_GPU ";
        if ((n & CL_DEVICE_TYPE_ACCELERATOR) != 0) result += "CL_DEVICE_TYPE_ACCELERATOR ";
        return result;
    }


    /**
     * Returns the string describing the given cl_device_address_info - bitfield
     *
     * @param n The cl_device_address_info - bitfield
     * @return The string describing the cl_device_address_info - bitfield
     */
    public static String stringFor_cl_device_address_info(long n)
    {
        if (n == 0)
        {
            return "(none)";
        }  
        String result = "";
        if ((n & CL_DEVICE_ADDRESS_32_BITS) != 0) result += "CL_DEVICE_ADDRESS_32_BITS ";
        if ((n & CL_DEVICE_ADDRESS_64_BITS) != 0) result += "CL_DEVICE_ADDRESS_64_BITS ";
        return result;
    }


    /**
     * Returns the string describing the given cl_device_fp_config - bitfield
     *
     * @param n The cl_device_fp_config - bitfield
     * @return The string describing the cl_device_fp_config - bitfield
     */
    public static String stringFor_cl_device_fp_config(long n)
    {
        if (n == 0)
        {
            return "(none)";
        }
        String result = "";
        if ((n & CL_FP_DENORM) != 0) result += "CL_FP_DENORM ";
        if ((n & CL_FP_INF_NAN) != 0) result += "CL_FP_INF_NAN ";
        if ((n & CL_FP_ROUND_TO_NEAREST) != 0) result += "CL_FP_ROUND_TO_NEAREST ";
        if ((n & CL_FP_ROUND_TO_ZERO) != 0) result += "CL_FP_ROUND_TO_ZERO ";
        if ((n & CL_FP_ROUND_TO_INF) != 0) result += "CL_FP_ROUND_TO_INF ";
        if ((n & CL_FP_FMA) != 0) result += "CL_FP_FMA ";
        return result;
    }


    /**
     * Returns the string describing the given cl_device_exec_capabilities - bitfield
     *
     * @param n The cl_device_exec_capabilities - bitfield
     * @return The string describing the cl_device_exec_capabilities - bitfield
     */
    public static String stringFor_cl_device_exec_capabilities(long n)
    {
        if (n == 0)
        {
            return "(none)";
        }
        String result = "";
        if ((n & CL_EXEC_KERNEL) != 0) result += "CL_EXEC_KERNEL ";
        if ((n & CL_EXEC_NATIVE_KERNEL) != 0) result += "CL_EXEC_NATIVE_KERNEL ";
        return result;
    }


    /**
     * Returns the string describing the given cl_command_queue_properties - bitfield
     *
     * @param n The cl_command_queue_properties - bitfield
     * @return The string describing the cl_command_queue_properties - bitfield
     */
    public static String stringFor_cl_command_queue_properties(long n)
    {
        if (n == 0)
        {
            return "(none)";
        }
        String result = "";
        if ((n & CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE) != 0) result += "CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE ";
        if ((n & CL_QUEUE_PROFILING_ENABLE) != 0) result += "CL_QUEUE_PROFILING_ENABLE ";
        return result;
    }


    /**
     * Returns the string describing the given cl_mem_flags - bitfield
     *
     * @param n The cl_mem_flags - bitfield
     * @return The string describing the cl_mem_flags - bitfield
     */
    public static String stringFor_cl_mem_flags(long n)
    {
        if (n == 0)
        {
            return "(none)";
        }
        String result = "";
        if ((n & CL_MEM_READ_WRITE) != 0) result += "CL_MEM_READ_WRITE ";
        if ((n & CL_MEM_WRITE_ONLY) != 0) result += "CL_MEM_WRITE_ONLY ";
        if ((n & CL_MEM_READ_ONLY) != 0) result += "CL_MEM_READ_ONLY ";
        if ((n & CL_MEM_USE_HOST_PTR) != 0) result += "CL_MEM_USE_HOST_PTR ";
        if ((n & CL_MEM_ALLOC_HOST_PTR) != 0) result += "CL_MEM_ALLOC_HOST_PTR ";
        if ((n & CL_MEM_COPY_HOST_PTR) != 0) result += "CL_MEM_COPY_HOST_PTR ";
        return result;
    }


    /**
     * Returns the string describing the given cl_map_flags - bitfield
     *
     * @param n The cl_map_flags - bitfield
     * @return The string describing the cl_map_flags - bitfield
     */
    public static String stringFor_cl_map_flags(long n)
    {
        if (n == 0)
        {
            return "(none)";
        }
        String result = "";
        if ((n & CL_MAP_READ) != 0) result += "CL_MAP_READ ";
        if ((n & CL_MAP_WRITE) != 0) result += "CL_MAP_WRITE ";
        return result;
    }

     
    /**
     * The log levels which may be used to control the internal
     * logging of the native JOCL library
     */
    public static enum LogLevel
    {
        /**
         * Never print anything
         */
        LOG_QUIET, 
         
        /**
         * Only print error messages
         */
        LOG_ERROR,
        
        /**
         * Print warnings
         */
        LOG_WARNING, 
         
        /**
         * Print info messages
         */
        LOG_INFO, 
         
        /**
         * Print debug information
         */
        LOG_DEBUG, 
         
        /**
         * Trace function calls
         */
        LOG_TRACE, 
         
        /**
         * Print fine-grained debug information
         */
        LOG_DEBUGTRACE
    }
     
    /**
     * Set the specified log level for the library.<br />
     * <br />
     * Currently supported log levels:
     * <br />
     * LOG_QUIET: Never print anything <br />
     * LOG_ERROR: Print error messages <br />
     * LOG_TRACE: Print a trace of all native function calls <br />
     *
     * @param logLevel The log level to use.
     */
    public static void setLogLevel(LogLevel logLevel)
    {
        assertInit();
        setLogLevelNative(logLevel.ordinal());
    }
    private static native void setLogLevelNative(int logLevel);
     

    /**
     * Creates an returns a new direct ByteBuffer with the given size,
     * whose memory has the given alignment, in bytes. The memory 
     * associated with the returned buffer will be freed automatically
     * soon after the ByteBuffer has been marked for garbage collection.
     * 
     * @param size The size of the buffer
     * @param alignment The alignment, in bytes
     * @return A new direct ByteBuffer
     */
    // This is still private and not officially available. When it is 
    // enabled, remember to call CL#initMemoryManagementThread()
    // during initialization!
    private static synchronized ByteBuffer allocateAligned(int size, int alignment)
    {
        expungeStaleAlignedByteBuffers();
        Pointer pointer = new Pointer();
        ByteBuffer byteBuffer = allocateAlignedNative(size, alignment, pointer);
        if (byteBuffer == null)
        {
            return null;
        }
        WeakReference<ByteBuffer> reference = 
            new WeakReference<ByteBuffer>(byteBuffer, alignedByteBufferReferenceQueue);
        alignedByteBufferMap.put(reference, pointer);
        return byteBuffer;
    }
    private static native ByteBuffer allocateAlignedNative(int size, int alignment, Pointer pointer);

    
    /**
     * This method may be used to manually free an aligned ByteBuffer which
     * was allocated with {@link CL#allocateAligned(int, int)}.
     * 
     * @param byteBuffer The byte buffer
     */
    // This is not required, and might be error-prone because of 
    // making the given Buffer invalid.
    /*
    private static synchronized void freeAligned(ByteBuffer byteBuffer)
    {
        if (byteBuffer == null)
        {
            return;
        }
        for (Map.Entry<WeakReference<ByteBuffer>, Pointer> entry : alignedByteBufferMap.entrySet())
        {
            WeakReference<ByteBuffer> reference = entry.getKey();
            if (reference.get() == byteBuffer)
            {
                Pointer pointer = entry.getValue();
                if (pointer != null)
                {
                    freeAlignedNative(pointer);
                }
                alignedByteBufferMap.remove(reference);
                return;
            }
        }
    }
    */
    private static native void freeAlignedNative(Pointer pointer);

    /**
     * Frees the memory of all aligned ByteBuffers that had been allocated
     * with {@link CL#allocateAligned(int, int)} and which are already
     * marked for garbage collection.
     */
    private static synchronized void expungeStaleAlignedByteBuffers() 
    {
        while (true)
        {
            Reference<? extends ByteBuffer> reference = 
                alignedByteBufferReferenceQueue.poll();
            if (reference == null)
            {
                break;
            }
            Pointer pointer = alignedByteBufferMap.get(reference);
            freeAlignedNative(pointer);
            alignedByteBufferMap.remove(reference);
        }
    }

    /**
     * Creates and starts the daemon thread which will regularly call 
     * {@link CL#expungeStaleAlignedByteBuffers()}
     */
    // Currently not used - only required when CL#allocateAligned is available
    @SuppressWarnings("unused")
    private static void initMemoryManagementThread()
    {
        Thread memoryManagementThread = new Thread(new Runnable()
        {
            public void run()
            {
                while (true)
                {
                    expungeStaleAlignedByteBuffers();
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }, "memoryManagementThread");
        memoryManagementThread.setPriority(Thread.MIN_PRIORITY);
        memoryManagementThread.setDaemon(true);
        memoryManagementThread.start();
    }
    
    
     
    /**
     * Assert that the native library is loaded
     */
    private static void assertInit()
    {
        if (!initialized)
        {
            LibUtils.loadLibrary("JOCL");
            //initMemoryManagementThread();
            
            initialized = true;
        }
    }


     
     
    //=== OpenCL methods =====================================================

    /**
     * Returns a list of OpenCL platforms found.
     * <p>
     * cl_int <b>clGetPlatformIDs</b> (cl_uint_<i>num_entries</i>,
     * cl_platform_id *<i>platforms</i>,
     * cl_uint *<i>num_platforms</i>)
     * </p>
     * <p>
     * <i>num_entries</i> is the number of cl_platform_id entries that can be
     * added to <i>platforms. </i>If <i>platforms </i>is not NULL, the
     * <i>num_entries</i> must be greater than zero.
     * </p>
     * <p>
     * <i>platforms</i> returns a list of OpenCL platforms found. The
     * cl_platform_id values returned in <i>platforms </i>can be used to
     * identify a specific OpenCL platform. If <i>platforms</i> argument is
     * NULL, this argument is ignored. The number of OpenCL platforms returned
     * is the minimum of the value specified by <i>num_entries</i> or the
     * number of OpenCL platforms available.
     * </p>
     * <p>
     * <i>num_platforms</i> returns the number of OpenCL platforms available.
     * If <i>num_platforms</i> is NULL, this argument is ignored.
     * </p>
     * <p>
     * <b>clGetPlatformsIDs</b> returns CL_INVALID_VALUE if <i>num_entries</i>
     * is equal to zero and <i>platforms </i>is not NULL or if both
     * <i>num_platforms</i> and <i>platforms</i> are NULL, and returns
     * CL_SUCCESS if the function is executed successfully.
     * </p>
     * <b>For JOCL:</b><br />
     * If the given array already contains objects, these will be filled
     * with the values obtained from the native method. If the array 
     * contains 'null' objects, the new array elements will be created
     * by the native method.
     */
    public static synchronized int clGetPlatformIDs(int num_entries, cl_platform_id platforms[], int num_platforms[])
    {
        assertInit();
        return checkResult(clGetPlatformIDsNative(num_entries, platforms, num_platforms));
    }

    private static native int clGetPlatformIDsNative(int num_entries, cl_platform_id platforms[], int num_platforms[]);

    /**
     * Gets specific information about the OpenCL platform.
     * <p>
     * cl_int <b>clGetPlatformInfo</b> (cl_platform_id <i>platform</i>,
     * cl_platform_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * The information that
     * can be queried using <b>clGetPlatformInfo</b> is specified in the table 
     * below.
     * </p>
     * <p>
     * <i>platform</i> refers to the platform ID returned by
     * <b>clGetPlatformsIDs</b> or can be NULL. If <i>platform </i>is NULL, the
     * behavior is implementation-defined.
     * </p>
     * <p>
     * <i>param_name</i> is an enumeration constant that identifies the
     * platform information being queried. It can be one of the following values
     * as specified in the table.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory location where appropriate
     * values for a given <i>param_name </i>as specified in the table 
     * will be returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> specifies the size in bytes of memory pointed to
     * by <i>param_value</i>. This size in bytes must be &gt;= size of return
     * type specified in the table.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * being queried by <i>param_value</i>. If <i>param_value_size_ret</i> is
     * NULL, it is ignored.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>cl_platform_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Description </b> </th>
     * </tr>
     * <tr>
     * <th> <b>CL_PLATFORM_PROFILE </b> </th>
     * <td> char[] </td>
     * <td> OpenCL profile string. Returns the profile name
     * supported by the implementation. The profile name returned can be one of
     * the following strings: FULL_PROFILE - if the
     * implementation supports the OpenCL specification (functionality defined
     * as part of the core specification and does not require any extensions to
     * be supported). EMBEDDED_PROFILE - if the implementation supports the
     * OpenCL embedded profile. The embedded profile is defined to be a subset
     * for each version of OpenCL. The embedded profile for OpenCL 1.0 is
     * described in the OpenCL documentation, section 10. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_PLATFORM_VERSION </b> </th>
     * <td> char[] </td>
     * <td> OpenCL version string. Returns the OpenCL version
     * supported by the implementation. This version string has the following
     * format: <i>OpenCL&lt;space&gt;&lt;major_version.min
     * or_version&gt;&lt;space&gt;&lt;platform</i><i>specific information&gt;
     * </i> The <i>major_version.minor_version </i>value returned will be 1.0.
     * </td>
     * </tr>
     * <tr>
     * <td > </td>
     * </tr>
     * <tr>
     * <th > <b>CL_PLATFORM_NAME </b> </th>
     * <td > char[] </td>
     * <td > Platform name string. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_PLATFORM_VENDOR </b> </th>
     * <td > char[] </td>
     * <td > Platform vendor string. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_PLATFORM_EXTENSIONS </b> </th>
     * <td > char[] </td>
     * <td > Returns a space separated list of extension names (the extension
     * names themselves do not contain any spaces) supported by the platform.
     * Extensions defined here must be supported by all devices associated with
     * this platform. </td>
     * </tr>
     * </table>
     * <p>
     * <b>clGetPlatformInfo</b> returns CL_SUCCESS if the function is executed
     * successfully. It returns CL_INVALID_PLATFORM_if <i>platform</i>
     * is not a valid platform, returns CL_INVALID_VALUE if <i>param_name</i>
     * is not one of the supported values or if size in bytes specified by
     * <i>param_value_size </i>is &lt; size of return type as specified in
     * the table and <i>param_value</i> is not a NULL value.
     * </p>
     */
    public static synchronized int clGetPlatformInfo(cl_platform_id platform, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetPlatformInfoNative(platform, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetPlatformInfoNative(cl_platform_id platform, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     * Obtains the list of devices available on a platform.
     * </p>
     * <p>
     * cl_int <b>clGetDeviceIDs</b><b><sup>3</sup></b> (cl_platform_id
     * <i>platform</i>, cl_device_type <i>device_type</i><i>, </i>cl_uint
     * <i>num_entries</i>, cl_device_id *<i>devices</i>, cl_uint *<i>num_devices</i>)
     * </p>
     * <p>
     * <i>platform</i> refers to the platform ID returned by <b>clGetPlatformsIDs</b>
     * or can be NULL. If <i>platform </i>is NULL, the behavior is
     * implementation-defined.
     * </p>
     * <p>
     * <i>device_type</i> is a bitfield that identifies the type of OpenCL device.
     * The <i>device_type</i> can be used to query specific OpenCL devices or all
     * OpenCL devices available. The valid values for <i>device_type</i> are
     * specified in the table.
     * </p>
     * <table border="1">
     * <tr>
     * <th> <b>CL_DEVICE_TYPE_CPU </b> </th>
     * <td> An OpenCL device that is the host processor. The host
     * processor runs the OpenCL implementations and is a
     * single or multi-core CPU.</td>
     * </tr> 
     * <tr>
     * <th > <b>CL_DEVICE_TYPE_GPU </b> </th>
     * <td > An OpenCL device that is a GPU. By this we mean that the device can
     * also be used to accelerate a 3D API such as OpenGL or DirectX. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_TYPE_ACCELERATOR </b> </th>
     * <td > Dedicated OpenCL accelerators (for example the IBM CELL Blade). These
     * devices communicate with the host processor using a peripheral interconnect
     * such as PCIe. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_TYPE_DEFAULT </b> </th>
     * <td > The default OpenCL device in the system. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_TYPE_ALL </b> </th>
     * <td > All OpenCL devices available in the system. </td>
     * </tr>
     * </table>
     * <p>
     * <i>num_entries</i> is the number of cl_device entries that can be added to
     * <i>devices. </i>If <i>devices</i> is not NULL, the <i>num_entries</i> must
     * be greater than zero.
     * </p>
     * <p>
     * <i>devices</i> returns a list of OpenCL devices found. The cl_device_id
     * values returned in <i>devices </i>can be used to identify a specific OpenCL
     * device. If <i>devices</i> argument is NULL, this argument is ignored. The
     * number of OpenCL devices returned is the mininum of the value specified by
     * <i>num_entries</i> or the number of OpenCL devices whose type matches
     * <i>device_type</i>.
     * </p>
     * <p>
     * <i>num_devices</i> returns the number of OpenCL devices available that match
     * <i>device_type</i>. If <i>num_devices</i> is NULL, this argument is
     * ignored.
     * </p>
     * <p>
     * <b>clGetDeviceIDs </b>returns CL_INVALID_PLATFORM_if <i>platform</i> is not
     * a valid platform, returns CL_INVALID_DEVICE_TYPE if <i>device_type</i> is
     * not a valid value, returns CL_INVALID_VALUE if <i>num_entries</i> is equal
     * to zero and <i>devices</i> is not NULL or if both <i>num_devices</i> and
     * <i>devices</i> are NULL, returns CL_DEVICE_NOT_FOUND if no OpenCL devices
     * that matched <i>device_type</i> were found, and returns CL_SUCCESS if the
     * function is executed successfully.
     * </p>
     * <p>
     * The application can query specific capabilities of the OpenCL device(s)
     * returned by <b>clGetDeviceIDs</b>. This can be used by the application to
     * determine which device(s) to use.
     * </p>
     * <b>For JOCL:</b><br />
     * If the given array already contains objects, these will be filled
     * with the values obtained from the native method. If the array 
     * contains 'null' objects, the new array elements will be created
     * by the native method.
     */
    public static synchronized int clGetDeviceIDs(cl_platform_id platform, long device_type, int num_entries, cl_device_id devices[], int num_devices[])
    {
        assertInit();
        return checkResult(clGetDeviceIDsNative(platform, device_type, num_entries, devices, num_devices));
    }

    private static native int clGetDeviceIDsNative(cl_platform_id platform, long device_type, int num_entries, cl_device_id devices[], int num_devices[]);

    /**
     * Gets specific information about an OpenCL device.
     * <p>
     * cl_int <b>clGetDeviceInfo</b> (cl_device_id <i>device</i>,
     * cl_device_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * The information that can be queried using <b>clGetDeviceInfo</b> is specified in the table.
     * </p>
     * <p>
     * <i>device</i> is a device returned by <b>clGetDeviceIDs</b>.
     * </p>
     * <p>
     * <i>param_name</i> is an enumeration constant that identifies the device
     * information being queried. It can be one of the following values as
     * specified in the table.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory location where appropriate
     * values for a given <i>param_name </i>as specified in the table
     * will be returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> specifies the size in bytes of memory pointed to
     * by <i>param_value</i>. This size in bytes must be &gt;= size of return
     * type specified in the table.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * being queried by <i>param_value</i>. If <i>param_value_size_ret</i> is
     * NULL, it is ignored.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>cl_device_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Description </b> </th>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_TYPE </b> </td>
     * <td > cl_device_type </td>
     * <td > The OpenCL device type. Currently supported values are:
     * CL_DEVICE_TYPE_CPU, CL_DEVICE_TYPE_GPU, CL_DEVICE_TYPE_ACCELERATOR,
     * CL_DEVICE_TYPE_DEFAULT or a combination of the above. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_VENDOR_ID </b> </td>
     * <td > cl_uint </td>
     * <td > A unique device vendor identifier. An example of a unique device
     * identifier could be the PCIe ID. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_MAX_COMPUTE_UNITS </b> </td>
     * <td > cl_uint </td>
     * <td > The number of parallel compute cores on the OpenCL device. The
     * minimum value is 1. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS </b> </td>
     * <td > cl_uint </td>
     * <td > Maximum dimensions that specify the global and local work-item IDs
     * used by the data parallel execution model. (Refer to
     * <b>clEnqueueNDRangeKernel</b>). The minimum value is 3. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_MAX_WORK_ITEM_SIZES </b> </td>
     * <td > size_t [] </td>
     * <td > Maximum number of work-items that can be specified in each
     * dimension of the work-group to <b>clEnqueueNDRangeKernel</b>. Returns
     * <i>n</i> size_t entries, where <i>n</i> is the value returned by the
     * query for CL_DEVICE_MAX_WORK_ITEM_DI MENSIONS. The minimum value is (1,
     * 1, 1). </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_MAX_WORK_GROUP_SIZE </b> </td>
     * <td > size_t </td>
     * <td > Maximum number of work-items in a work-group executing a kernel
     * using the data parallel execution model. (Refer to <b>clEnqueueNDRangeKernel</b>). 
     * The minimum value is 1. </td>
     * </tr>
     * <tr>
     * <th > <b>
     * CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR <br />
     * CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT <br />
     * CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT <br />
     * CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG <br />
     * CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT <br />
     * CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE </b> </th>
     * <td > cl_uint </td>
     * <td > Preferred native vector width size for built-in scalar types that
     * can be put into vectors. The vector width is defined as the number of
     * scalar elements that can be stored in the vector. If the <b>cl_khr_fp64</b>
     * extension is not supported, CL_DEVICE_PREFERRED_VECTOR_WID TH_DOUBLE must
     * return 0. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_CLOCK_FREQUENCY </b> </th>
     * <td > cl_uint </td>
     * <td > Maximum configured clock frequency of the device in MHz. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_ADDRESS_BITS </b> </th>
     * <td > cl_uint </td>
     * <td > The default compute device address space size specified as an
     * unsigned integer value in bits. Currently supported values are 32 or 64
     * bits. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_MEM_ALLOC_SIZE </b> </th>
     * <td > cl_ulong </td>
     * <td > Max size of memory object allocation in bytes. The minimum value is
     * max (1/4th of <b>CL_DEVICE_GLOBAL_MEM_SIZE</b> , 128*1024*1024) </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_IMAGE_SUPPORT </b> </th>
     * <td > cl_bool </td>
     * <td > Is CL_TRUE if images are supported by the OpenCL device and
     * CL_FALSE otherwise. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_READ_IMAGE_ARGS </b> </th>
     * <td > cl_uint </td>
     * <td > Max number of simultaneous image objects that can be read by a
     * kernel. The minimum value is 128 if CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     * </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_WRITE_IMAGE_ARGS </b> </th>
     * <td > cl_uint </td>
     * <td > Max number of simultaneous image objects that can be written to by
     * a kernel. The minimum value is 8 if CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     * </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_IMAGE2D_MAX_WIDTH </b> </th>
     * <td > size_t </td>
     * <td > Max width of 2D image in pixels. The minimum value is 8192 if
     * CL_DEVICE_IMAGE_SUPPORT is CL_TRUE. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_IMAGE2D_MAX_HEIGHT </b> </th>
     * <td > size_t </td>
     * <td > Max height of 2D image in pixels. The 
     * minimum value is 8192 if CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.</td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_IMAGE3D_MAX_WIDTH </b> </th>
     * <td > size_t </td>
     * <td > Max width of 3D image in pixels. The minimum value is 2048 if
     * CL_DEVICE_IMAGE_SUPPORT is CL_TRUE. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_IMAGE3D_MAX_HEIGHT </b> </th>
     * <td > size_t </td>
     * <td > Max height of 3D image in pixels. The minimum value is 2048 if
     * CL_DEVICE_IMAGE_SUPPORT is CL_TRUE. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_IMAGE3D_MAX_DEPTH </b> </th>
     * <td > size_t </td>
     * <td > Max depth of 3D image in pixels. The minimum value is 2048 if
     * CL_DEVICE_IMAGE_SUPPORT is CL_TRUE. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_SAMPLERS </b> </th>
     * <td > cl_uint </td>
     * <td > Maximum number of samplers that can be used in a kernel. Refer to
     * <i>section 6.11.8</i> for a detailed description on samplers. The
     * minimum value is 16 if CL_DEVICE_IMAGE_SUPPORT is CL_TRUE. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_PARAMETER_SIZE </b> </th>
     * <td > size_t </td>
     * <td > Max size in bytes of the arguments that can be passed to a kernel.
     * The minimum value is 256. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MEM_BASE_ADDR_ALIGN </b> </th>
     * <td > cl_uint </td>
     * <td > Describes the alignment in bits of the base address of any
     * allocated memory object. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE </b> </th>
     * <td > cl_uint </td>
     * <td > The smallest alignment in bytes which can be used for any data
     * type. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_SINGLE_FP_CONFIG </b> </th>
     * <td > cl_device_ fp_config </td>
     * <td > Describes single precision floating point capability of the device.
     * This is a bit-field that describes one or more of the following values:<br />
     * <br />
     * CL_FP_DENORM : denorms are supported <br />
     * CL_FP_INF_NAN : INF and quiet NaNs are supported.<br />
     * CL_FP_ROUND_TO_NEAREST : round to nearest even rounding mode supported <br />
     * CL_FP_ROUND_TO_ZERO : round to zero rounding mode supported <br />
     * CL_FP_ROUND_TO_INF : round to +ve and -ve infinity rounding modes supported <br />
     * CL_FP_FMA : IEEE754-2008 fused multiply-add is supported. <br />
     * <br />
     * The mandated minimum floating-point capability is: CL_FP_ROUND_TO_NEAREST |
     * CL_FP_INF_NAN. </th>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_GLOBAL_MEM_CACHE_TYPE </b> </th>
     * <td > cl_device_mem_ cache_type </td>
     * <td > Type of global memory cache supported. Valid values are: CL_NONE,
     * CL_READ_ONLY_CACHE and CL_READ_WRITE_CACHE. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE </b> </th>
     * <td > cl_uint </td>
     * <td > Size of global memory cache line in bytes. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_GLOBAL_MEM_CACHE_SIZE </b> </th>
     * <td > cl_ulong </td>
     * <td > Size of global memory cache in bytes. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_GLOBAL_MEM_SIZE </b> </th>
     * <td > cl_ulong </td>
     * <td > Size of global device memory in bytes. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE </b> </th>
     * <td > cl_ulong </td>
     * <td > Max size in bytes of a constant buffer allocation. The minimum
     * value is 64 KB. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_MAX_CONSTANT_ARGS </b> </th>
     * <td > cl_uint </td>
     * <td > Max number of arguments declared with the __constant qualifier in a
     * kernel. The minimum value is 8. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_LOCAL_MEM_TYPE </b> </th>
     * <td > cl_device_ local_mem_type </td>
     * <td > Type of local memory supported. This can be set to CL_LOCAL
     * implying dedicated local memory storage such as SRAM, or CL_GLOBAL. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_LOCAL_MEM_SIZE </b> </th>
     * <td > cl_ulong </td>
     * <td > Size of local memory arena in bytes. The minimum value is 16 KB.
     * </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_ERROR_CORRECTION_SUPPORT </b> </th>
     * <td > cl_bool </td>
     * <td > Is CL_TRUE if the device implements error correction for the
     * memories, caches, registers etc. in the device. Is CL_FALSE if the device
     * does not implement error correction. This can be a requirement for
     * certain clients of OpenCL. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_PROFILING_TIMER_RESOLUTION </b> </th>
     * <td > size_t </td>
     * <td > Describes the resolution of device timer. This is measured in
     * nanoseconds. Refer to section 5.9 of the OpenCL documentation for details.</td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_ENDIAN_LITTLE </b> </td>
     * <td > cl_bool </td>
     * <td > Is CL_TRUE if the OpenCL device is a little endian device and
     * CL_FALSE otherwise. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_AVAILABLE </b> </td>
     * <td > cl_bool </td>
     * <td > Is CL_TRUE if the device is available and CL_FALSE if the device is
     * not available. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_COMPILER_AVAILABLE </b> </td>
     * <td > cl_bool </td>
     * <td > Is CL_FALSE if the implementation does not have a compiler
     * available to compile the program source. Is CL_TRUE if the compiler is
     * available. This can be CL_FALSE for the embedded platform profile only.
     * </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_EXECUTION_CAPABILITIES </b> </td>
     * <td > cl_device_exec_ capabilities </td>
     * <td > Describes the execution capabilities of the device. This is a
     * bit-field that describes one or more of the following values: <br />
     * <br />
     * CL_EXEC_KERNEL : The OpenCL device can execute OpenCL kernels.<br /> 
     * CL_EXEC_NATIVE_KERNEL : The OpenCL device can execute native kernels. <br />
     * <br />
     * The mandated minimum capability is:
     * CL_EXEC_KERNEL. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_DEVICE_QUEUE_PROPERTIES </b> </td>
     * <td > cl_command_queue_properties </td>
     * <td > Describes the command-queue properties supported by the device.
     * This is a bit-field that describes one or more of the following values:
     * CL_QUEUE_OUT_OF_ORDER_EXEC_ MODE_ENABLE CL_QUEUE_PROFILING_ENABLE These
     * properties are described in a separate table. The mandated minimum 
     * capability is: CL_QUEUE_PROFILING_ENABLE.
     * </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_PLATFORM </b> </th>
     * <td > cl_platform_id </td>
     * <td > The platform associated with this device. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_NAME </b> </th>
     * <td > char[] </td>
     * <td > Device name string. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_VENDOR </b> </th>
     * <td > char[] </td>
     * <td > Vendor name string. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DRIVER_VERSION </b> </th>
     * <td > char[] </td>
     * <td > OpenCL software driver version string in the form <i>major_number</i>.<i>minor_number</i>.
     * </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_PROFILE</b><b>4 </b> </th>
     * <td > char[] </td>
     * <td > OpenCL profile string. Returns the profile name supported by the
     * device. The profile name returned can be one of the following strings:<br />
     * <br />
     * FULL_PROFILE : if the device supports the OpenCL specification 
     * (functionality defined as part of the core specification
     * and does not require any extensions to be supported). <br />
     * EMBEDDED_PROFILE : if the device supports the OpenCL embedded profile. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_VERSION </b> </th>
     * <td > char[] </td>
     * <td > OpenCL version string. Returns the OpenCL version supported by the
     * device. This version string has the following format:
     * <i>OpenCL&lt;space&gt;&lt;major_version.min
     * or_version&gt;&lt;space&gt;&lt;vendor-specific information&gt; </i>The
     * <i>major_version.minor_version </i>value returned will be 1.0. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_DEVICE_EXTENSIONS </b> </th>
     * <td > char[] </td>
     * <td > Returns a space separated list of extension names (the extension
     * names themselves do not contain any spaces). The list of extension names
     * returned currently can include one or more of the following approved extension 
     * names: <br />
     * <b>
     * cl_khr_fp64 <br />
     * cl_khr_select_fprounding_mode <br />
     * cl_khr_global_int32_base_atomics <br />
     * cl_khr_global_int32_extended_atomics <br />
     * cl_khr_local_int32_base_atomics <br />
     * cl_khr_local_int32_extended_atomics <br />  
     * cl_khr_int64_base_atomics <br />
     * cl_khr_int64_extended_atomics <br />
     * cl_khr_3d_image_writes <br />
     * cl_khr_byte_addressable_store <br />
     * cl_khr_fp16 </b><br />
     * <br />Please refer to section 9 of the OpenCL documentation for a detailed 
     * description of these extensions. </th>
     * </tr>
     * </table>
     * <p>
     * <b>clGetDeviceInfo</b> returns CL_SUCCESS if the function is executed
     * successfully. It returns CL_INVALID_DEVICE if <i>device</i> is not
     * valid, returns CL_INVALID_VALUE if <i>param_name</i> is not one of the
     * supported values or if size in bytes specified by <i>param_value_size
     * </i>is &lt; size of return type as specified in the table and
     * <i>param_value</i> is not a NULL value.
     * </p>
     */
    public static synchronized int clGetDeviceInfo(cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetDeviceInfoNative(device, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetDeviceInfoNative(cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Creates an OpenCL context.
     * <p>
     * cl_context <b>clCreateContext</b> (cl_context_properties *<i>properties</i>,
     * cl_uint <i>num_devices</i>,
     * const cl_device_id *<i>devices</i>,
     * void (*<i>pfn_notify</i>)(<i>const char *errinfo, </i>
     * <i>const void *private_info, size_t cb, </i>
     * <i>void *user_data</i>),
     * void *<i>user_data</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p align="left" >
     * An OpenCL context is created with one or more
     * devices. Contexts are used by the OpenCL runtime for managing objects
     * such as command-queues, memory, program and kernel objects and for
     * executing kernels on one or more devices specified in the context.
     * </p>
     * <p>
     * <i>properties </i>specifies a list of context property names and their
     * corresponding values. Each property name is immediately followed by the
     * corresponding desired value. The list is terminated with 0. The list of
     * supported properties is described in the table. <i>properties</i>
     * can be NULL in which case the platform that is selected is
     * implementation-defined.
     * </p>
     * <p>
     * <table border="1">
     * <tr>
     *     <td>cl_context_properties enum</td>
     *     <td>Property value</td>
     *     <td>Description</td>
     * </tr>
     * <tr>
     *     <td>CL_CONTEXT_PLATFORM</td>
     *     <td>cl_platform_id </td>
     *     <td>Specifies the platform to use.</td>
     * </tr>
     * </table>
     * </p>
     * <p>
     * <i>num_devices</i> is the number of devices specified in the <i>devices</i>
     * argument.
     * </p>
     * <p>
     * <i>devices</i> is a pointer to a list of unique devices returned by
     * <b>clGetDeviceIDs </b>for a platform.
     * </p>
     * <p>
     * <i>pfn_notify</i> is a callback function that can be registered by the
     * application. This callback function will be used by the OpenCL
     * implementation to report information on errors that occur in this
     * context. This callback function may be called asynchronously by the
     * OpenCL implementation. It is the application's
     * responsibility to ensure that the callback function is thread-safe. The
     * parameters to this callback function are:
     * </p>
     * <ul>
     * <li>
     * <i>errinfo</i> is a pointer to an error string. 
     * </li>
     * <li>
     * <i>private_info</i> and
     * <i>cb</i> represent a pointer to binary data that is returned by the
     * OpenCL implementation that can be used to log additional information
     * helpful in debugging the error.
     * </li>
     * <li>
     * <i>user_data</i> is a pointer to user supplied data. If <i>pfn_notify</i>
     * is NULL, no callback function is registered. <i>user_data</i> will be
     * passed as the <i>user_data</i> argument when <i>pfn_notify</i> is
     * called. <i>user_data</i> can be NULL.
     * </li>
     * </ul>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * <b>clCreateContext</b> returns a valid non-zero context and
     * <i>errcode_ret</i> is set to CL_SUCCESS if the
     * context is created successfully. Otherwise, it returns a NULL value with
     * the following error
     * </p>
     * <p>
     * values returned in <i>errcode_ret</i>:
     * </p>
     * <p>CL_INVALID_PLATFORM if
     * <i>properties </i>is NULL and no platform could be selected or if
     * platform value specified in <i>properties</i> is not a valid platform.
     * </p>
     * <p>
     * CL_INVALID_VALUE if context property name in <i>properties</i> is not a
     * supported property name. CL_INVALID_VALUE if <i>devices </i>is NULL.
     * CL_INVALID_VALUE if <i>num_devices </i>is equal to zero.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>pfn_notify</i> is NULL but <i>user_data</i> is
     * not NULL. CL_INVALID_DEVICE if <i>devices</i> contains an invalid device
     * or are not associated with the specified platform.
     * </p>
     * <p>
     * CL_DEVICE_NOT_AVAILABLE if a device in <i>devices</i> is currently not
     * available even though the device was returned by <b>clGetDeviceIDs</b>.
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_context clCreateContext(cl_context_properties properties, int num_devices, cl_device_id devices[], CreateContextFunction pfn_notify, Object user_data, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_context result = clCreateContextNative(properties, num_devices, devices, pfn_notify, user_data, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_context clCreateContextNative(cl_context_properties properties, int num_devices, cl_device_id devices[], CreateContextFunction pfn_notify, Object user_data, int errcode_ret[]);

    /**
     * Creates an OpenCL context from a device type that identifies the specific
     * device(s) to use.
     * <p>
     * <b>clCreateContextFromType</b>
     * (cl_context_properties *<i>properties, </i>cl_device_type <i>device_type</i>,
     * void (*<i>pfn_notify</i>)(<i>const char *errinfo, </i>
     * <i>const void *private_info, size_t cb, void *user_data</i>), void *<i>user_data</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>properties</i> specifies a list of context property names and their
     * corresponding values. Each property name is immediately followed by the
     * corresponding desired value. The list is terminated with 0. The list of
     * supported properties is described in the table in the documentation of
     * clCreateContext. <i>properties</i>
     * can also be NULL in which case the platform that is selected is
     * implementation-defined.
     * </p>
     * <p>
     * <i>device_type</i> is a bit-field that identifies the type of device and
     * is described in the table in the documentation of clGetDeviceIDs.
     * </p>
     * <p align="left" >
     * <i>pfn_notify</i> and <i>user_data</i> are described in
     * <b>clCreateContext</b>.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateContextFromType</b> returns a valid non-zero context and
     * <i>errcode_ret</i> is set to CL_SUCCESS if the context is created
     * successfully. Otherwise, it returns a NULL value with the following error
     * values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_PLATFORM if <i>properties </i>is NULL and no platform could be
     * selected or if platform value specified in <i>properties</i> is not a
     * valid platform.
     * </p>
     * <p>
     * CL_INVALID_VALUE if context property name in <i>properties</i> is not a
     * supported property name.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>pfn_notify</i> is NULL but <i>user_data</i> is
     * not NULL.
     * </p>
     * <p>
     * CL_INVALID_DEVICE_TYPE if <i>device_type</i> is not a valid value.
     * </p>
     * <p>
     * CL_DEVICE_NOT_AVAILABLE if no devices that match <i>device_type</i> are
     * currently available.
     * </p>
     * <p>
     * <b>clCreateContextfromType</b> may return all or a subset of the actual
     * physical devices present in the platform and that match <i>device_type</i>.
     * </p>
     * <p>
     * CL_DEVICE_NOT_FOUND if no devices that match <i>device_type</i> were
     * found.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_context clCreateContextFromType(cl_context_properties properties, long device_type, CreateContextFunction pfn_notify, Object user_data, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_context result = clCreateContextFromTypeNative(properties, device_type, pfn_notify, user_data, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_context clCreateContextFromTypeNative(cl_context_properties properties, long device_type, CreateContextFunction pfn_notify, Object user_data, int errcode_ret[]);

    /**
     * Increments the <i>context</i> reference count.
     * <p align="left" >
     * cl_int <b>clRetainContext </b>(cl_context <i>context</i>)
     * </p>
     * <p>
     * <b>clRetainContext</b>
     * returns CL_SUCCESS if the function is executed successfully. It returns
     * CL_INVALID_CONTEXT if <i>context</i> is not a valid OpenCL context.
     * </p>
     * <p>
     * <b>clCreateContext </b>and<b> clCreateContextFromType</b> perform an
     * implicit retain. This is very helpful for 3<sup>rd</sup> party libraries,
     * which typically get a context passed to them by the application. However, it
     * is possible that the application may delete the context without informing the
     * library. Allowing functions to attach to (i.e. retain) and release a context
     * solves the problem of a context being used by a library no longer being
     * valid.
     * </p>
     */
    public static synchronized int clRetainContext(cl_context context)
    {
        assertInit();
        return checkResult(clRetainContextNative(context));
    }

    private static native int clRetainContextNative(cl_context context);

    /**
     * Decrements the <i>context</i> reference count.
     * <p>
     * cl_int <b>clReleaseContext</b> (cl_context <i>context</i>)
     * </p>
     * <p>
     * decrements the <i>context</i> reference count. <b>clReleaseContext</b>
     * returns CL_SUCCESS if the function is executed successfully. It returns
     * CL_INVALID_CONTEXT if <i>context</i> is not a valid OpenCL context.
     * </p>
     * <p>
     * After the <i>context</i> reference count becomes zero and all the
     * objects attached to <i>context</i> (such as memory objects,
     * command-queues) are released, the <i>context</i> is deleted.
     * </p>
     */
    public static synchronized int clReleaseContext(cl_context context)
    {
        assertInit();
        return checkResult(clReleaseContextNative(context));
    }

    private static native int clReleaseContextNative(cl_context context);

    /**
     * Can be used to query information about a context.
     * <p>
     * cl_int <b>clGetContextInfo</b> (cl_context <i>context</i>,
     * cl_context_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * can be used to query information about a context.
     * </p>
     * <p>
     * <i>context</i> specifies the OpenCL context being queried.
     * </p>
     * <p>
     * <i>param_name</i> is an enumeration constant that specifies the
     * information to query.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> specifies the size in bytes of memory pointed to
     * by <i>param_value</i>. This size must be greater than or equal to the
     * size of return type as described in the table.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * being queried by <i>param_value</i>. If <i>param_value_size_ret</i> is
     * NULL, it is ignored.
     * </p>
     * <p>
     * The list of supported <i>param_name </i>values and the information
     * returned in <i>param_value</i> by <b>clGetContextInfo</b> is described
     * in table.
     * </p>
     * <table border="1">
     * <tr>
     * <td>cl_context_info</td> <td>Return Type</td> <td>Information returned in param_value</td>
     * </tr>
     * <tr>
     * <td>CL_CONTEXT_REFERENCE_COUNT</td> <td>cl_uint</td> <td>Return the context reference count.</td>
     * </tr>
     * <tr>
     * <td>CL_CONTEXT_DEVICES</td> <td>cl_device_id[]</td> <td>Return the list of devices in context.</td>
     * </tr>
     * <tr>
     * <td>CL_CONTEXT_PROPERTIES</td> <td>cl_context_properties[]</td> <td>Return the properties argument specified in clCreateContext.</td>
     * </tr>
     * </table>
     * <p>
     * <b>clGetContextInfo</b> returns CL_SUCCESS if the function is executed
     * successfully. It returns CL_INVALID_CONTEXT if <i>context</i> is not a
     * valid context, returns CL_INVALID_VALUE if <i>param_name</i> is not one
     * of the supported values or if size in bytes specified by
     * <i>param_value_size </i>is &lt; size of return type as specified in
     * the table, and <i>param_value</i> 
     * is not a NULL value.
     * </p>
     */
    public static synchronized int clGetContextInfo(cl_context context, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetContextInfoNative(context, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetContextInfoNative(cl_context context, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Creates a command-queue on a specific device.
     * <p align="left" >
     * cl_command_queue <b>clCreateCommandQueue</b> (cl_context <i>context</i>,
     * cl_device_id <i>device</i>,
     * cl_command_queue_properties <i>properties</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>context</i> must be a valid OpenCL context.
     * </p>
     * <p>
     * The table lists of supported cl_command_queue_property values and
     * description. </i>
     * 
     * <table border="1">
     * <tr>
     * <td>Command-Queue Properties</td>
     * <td>Description</td>
     * </tr>
     * <tr>
     * <td>CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE</td>
     * <td>Determines whether the commands queued in the
     * command-queue are executed in-order or out-oforder.
     * If set, the commands in the command-queue
     * are executed out-of-order. Otherwise, commands
     * are executed in-order.
     * For a detailed description about
     * CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE,
     * refer to section 5.8 of the OpenCL documentation.</td>
     * </tr>
     * <tr>
     * <td>CL_QUEUE_PROFILING_ENABLE </td>
     * <td>Enable or disable profiling of commands in the
     * command-queue. If set, the profiling of commands
     * is enabled. Otherwise profiling of commands is
     * disabled.
     * For a detailed description, refer to section 5.9
     * of the OpenCL documentation.
     * </td>
     * </tr>
     * </table> 
     * </p>
     * <p>
     * <i>device</i> must be a device associated with <i>context</i>. It can
     * either be in the list of devices specified when <i>context</i> is created
     * using <b>clCreateContext</b> or have the same device type as the device type
     * specified when the <i>context</i> is created using
     * <b>clCreateContextFromType</b>.
     * </p>
     * <p>
     * <i>properties</i> specifies a list of properties for the command-queue. This
     * is a bit-field and is described in <i>table 5.1</i>. Only command-queue
     * properties specified in <i>table 5.1</i> can be set in <i>properties</i>;
     * otherwise the value specified in <i>properties</i> is considered to be not
     * valid.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If <i>errcode_ret</i>
     * is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateCommandQueue</b> returns a valid non-zero command-queue and
     * <i>errcode_ret</i> is set to CL_SUCCESS if the command-queue is created
     * successfully. Otherwise, it returns a NULL value with one of the following
     * error values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if <i>context </i>is not a valid context.
     * </p>
     * <p>
     * CL_INVALID_DEVICE if <i>device </i>is not a valid device or is not associated
     * with <i>context</i>.
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in <i>properties</i> are not valid.
     * </p>
     * <p>
     * CL_INVALID_QUEUE_PROPERTIES if values specified in <i>properties</i> are
     * valid but are not supported by the device.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources required by
     * the OpenCL implementation on the host.
     */
    public static synchronized cl_command_queue clCreateCommandQueue(cl_context context, cl_device_id device, long properties, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_command_queue result = clCreateCommandQueueNative(context, device, properties, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_command_queue clCreateCommandQueueNative(cl_context context, cl_device_id device, long properties, int errcode_ret[]);

    /**
     * Increments the <i>command_queue</i> reference count.
     * <p>
     * cl_int <b>clRetainCommandQueue</b> (cl_command_queue <i>command_queue</i>)
     * </p>
     * <p>
     * <b>clRetainCommandQueue</b> returns CL_SUCCESS if the function is
     * executed successfully. It returns CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i> is not a valid command-queue.
     * </p>
     */
    public static synchronized int clRetainCommandQueue(cl_command_queue command_queue)
    {
        assertInit();
        return checkResult(clRetainCommandQueueNative(command_queue));
    }

    private static native int clRetainCommandQueueNative(cl_command_queue command_queue);

    /**
     * Decrements the <i>command_queue</i> reference count.
     * <p>
     * cl_int <b>clReleaseCommandQueue</b> (cl_command_queue <i>command_queue</i>)
     * </p>
     * <p>
     * <b>clReleaseCommandQueue</b> returns CL_SUCCESS if the function is
     * executed successfully. It returns CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i> is not a valid command-queue.
     * </p>
     * <p>
     * After the <i>command_queue</i> reference count becomes zero and all
     * commands queued to <i>command_queue</i> have finished (e.g., kernel
     * executions, memory object updates, etc.), the command-queue is deleted.
     * </p>
     */
    public static synchronized int clReleaseCommandQueue(cl_command_queue command_queue)
    {
        assertInit();
        return checkResult(clReleaseCommandQueueNative(command_queue));
    }

    private static native int clReleaseCommandQueueNative(cl_command_queue command_queue);

    /**
     * Can be used to query information about a command-queue.
     * <p>
     * cl_int <b>clGetCommandQueueInfo</b> (cl_command_queue <i>command_queue</i>,
     * cl_command_queue_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>command_queue</i> specifies the command-queue being queried.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.2</i>. If <i>param_value</i> is NULL,
     * it is ignored.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * being queried by <i>param_value</i>. If <i>param_value_size_ret</i> is
     * NULL, it is ignored.
     * </p>
     * <p>
     * The list of supported <i>param_name </i>values and the information
     * returned in <i>param_value</i> by <b>clGetCommandQueueInfo</b> is
     * described in <i>table 5.2</i>.
     * </p>
     * <table border="1"> 
     * <tr>
     * <td>cl_command_queue_info </td>
     * <td>Return Type</td>
     * <td> Information returned in param_value</td>
     * </tr>
     * <tr>
     * <td><b>CL_QUEUE_CONTEXT </b></td>
     * <td>cl_context</td>
     * <td> Return the context specified when command-queue is created. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_QUEUE_REFERENCE_COUNT</b></th>
     * <td > cl_uint </td>
     * <td > Return the command-queue reference count. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_QUEUE_PROPERTIES </b> </th>
     * <td > cl_command_ queue_properties </td>
     * <td > Return the currently specified properties for the command-queue.
     * These properties are specified by the <i>properties </i>argument in
     * <b>clCreateCommandQueue</b>, and can be changed by
     * <b>clSetCommandQueueProperty</b>. </td>
     * </tr>
     * </table>
     * <p>
     * <b>clGetCommandQueueInfo</b> returns CL_SUCCESS if the function is
     * executed successfully. It returns CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i> is not a valid command-queue, returns
     * CL_INVALID_VALUE if <i>param_name</i> is not one of the supported values
     * or if size in bytes specified by <i>param_value_size </i>is &lt; size of
     * return type as specified in the table and <i>param_value</i> is
     * not a NULL value.
     * </p>
     */
    public static synchronized int clGetCommandQueueInfo(cl_command_queue command_queue, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetCommandQueueInfoNative(command_queue, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetCommandQueueInfoNative(cl_command_queue command_queue, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Can be used to enable or disable the properties of a command-queue.
     * <p>
     * cl_int <b>clSetCommandQueueProperty</b> (cl_command_queue <i>command_queue</i>,
     * cl_command_queue_properties <i>properties</i>,
     * cl_bool <i>enable</i>,
     * cl_command_queue_properties *<i>old_properties</i>)
     * </p>
     * <p>
     * <i>properties</i> specifies the new command-queue properties to be applied
     * to <i>command_queue</i>. Only command-queue properties specified in the
     * table in the documentation of clCreateCommandQueue can be set in 
     * <i>properties</i>; otherwise the value specified in
     * <i>properties</i> is considered to be not valid.
     * </p>
     * <p>
     * <i>enable</i> determines whether the values specified by <i>properties</i>
     * are enabled (if <i>enable</i> is CL_TRUE) or disabled (if <i>enable</i> is
     * CL_FALSE) for the command-queue. The property values are described in
     * the table in the documentation of clCreateCommandQueue
     * </p>
     * <p>
     * <i>old_properties</i> returns the command-queue properties before they were
     * changed by <b>clSetCommandQueueProperty</b>. If <i>old_properties</i> is
     * NULL, it is ignored.
     * </p>
     * <p>
     * As specified in the table in the documentation of clCreateCommandQueue, 
     * the <b>CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE
     * </b>command-queue property determines whether the commands in a command-queue
     * are executed in-order or out-of-order. Changing this command-queue property
     * will cause the OpenCL implementation to block until all previously queued commands in
     * <i>command_queue</i> have completed. This can be an expensive operation and
     * therefore changes to the <b>CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE</b>
     * property should be only done when absolutely necessary.
     * </p>
     * <p>
     * <b>clSetCommandQueueProperty</b> returns CL_SUCCESS if the command-queue
     * properties are successfully updated. It returns CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i> is not a valid command-queue, returns CL_INVALID_VALUE
     * if the values specified in <i>properties</i> are not valid and returns
     * CL_INVALID_QUEUE_PROPERTIES if values specified in <i>properties</i> are not
     * supported by the device.
     * </p>
     */
    public static synchronized int clSetCommandQueueProperty(cl_command_queue command_queue, long properties, boolean enable, long old_properties[])
    {
        assertInit();
        return checkResult(clSetCommandQueuePropertyNative(command_queue, properties, enable, old_properties));
    }

    private static native int clSetCommandQueuePropertyNative(cl_command_queue command_queue, long properties, boolean enable, long old_properties[]);

    /**
     * Used to create a <b>buffer object</b>.
     * <p>
     * cl_mem <b>clCreateBuffer </b>(cl_context <i>context</i>, cl_mem_flags
     * <i>flags</i>, size_t <i>size</i>, void *<i>host_ptr</i>, cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>context</i> is a valid OpenCL context used to create the buffer object.
     * </p>
     * <p>
     * <i>flags</i> is a bit-field that is used to specify allocation and usage
     * information such as the memory arena that should be used to allocate the
     * buffer object and how it will be used. <i>Table 5.3 </i>describes the
     * possible values for <i>flags</i>: <i>size</i> is the size in bytes of the
     * buffer memory object to be allocated.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>CL_MEM_READ_WRITE </b> </th>
     * <td > This flag specifies that the memory object will be read and written by
     * a kernel. This is the default. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_MEM_WRITE_ONLY </b> </th>
     * <td > This flags specifies that the memory object will be written but not
     * read by a kernel. Reading from a buffer or image object created with
     * CL_MEM_WRITE_ONLY inside a kernel is undefined. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_MEM_READ_ONLY </b> </th>
     * <td > This flag specifies that the memory object is a read-only memory object
     * when used inside a kernel. Writing to a buffer or image object created with
     * CL_MEM_READ_ONLY inside a kernel is undefined. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_MEM_USE_HOST_PTR </b> </th>
     * <td > This flag is valid only if <i>host_ptr</i> is not NULL. If specified,
     * it indicates that the application wants the OpenCL implementation to use
     * memory referenced by <i>host_ptr</i> as the storage bits for the memory
     * object. OpenCL implementations are allowed to cache the buffer contents
     * pointed to by <i>host_ptr</i> in device memory. This cached copy can be used
     * when kernels are executed on a device. The result of OpenCL commands that
     * operate on multiple </td>
     * </tr>
     * <tr>
     * <th > </th>
     * <td > buffer objects created with the same <i>host_ptr</i> or overlapping
     * host regions is considered to be undefined. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_MEM_ALLOC_HOST_PTR </b> </th>
     * <td > This flag specifies that the application wants the OpenCL
     * implementation to allocate memory from host accessible memory.
     * CL_MEM_ALLOC_HOST_PTR and CL_MEM_USE_HOST_PTR are mutually exclusive. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_MEM_COPY_HOST_PTR </b> </th>
     * <td > This flag is valid only if <i>host_ptr</i> is not NULL. If specified,
     * it indicates that the application wants the OpenCL implementation to allocate
     * memory for the memory object and copy the data from memory referenced by
     * <i>host_ptr</i>. CL_MEM_COPY_HOST_PTR and CL_MEM_USE_HOST_PTR are mutually
     * exclusive. CL_MEM_COPY_HOST_PTR can be used with CL_MEM_ALLOC_HOST_PTR to
     * initialize the contents of the cl_mem object allocated using host-accessible
     * (e.g. PCIe) memory. </td>
     * </tr>
     * </table>
     * <p>
     * <i>host_ptr</i> is a pointer to the buffer data that may already be
     * allocated by the application. The size of the buffer that <i>host_ptr</i>
     * points to must be &gt;= <i>size</i> bytes.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If <i>errcode_ret</i>
     * is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateBuffer</b> returns a valid non-zero buffer object and
     * <i>errcode_ret</i> is set to CL_SUCCESS if the buffer object is created
     * successfully. Otherwise, it returns a NULL value with one of the following
     * error values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if <i>context </i>is not a valid context.
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in <i>flags </i>are not valid.
     * </p>
     * <p>
     * CL_INVALID_BUFFER_SIZE if <i>size</i> is 0 or is greater than
     * </p>
     * <p>
     * CL_DEVICE_MAX_MEM_ALLOC_SIZE value specified in <i>table 4.3 </i>for all
     * devices in <i>context</i>.
     * </p>
     * <p>
     * CL_INVALID_HOST_PTR if <i>host_ptr</i> is NULL and CL_MEM_USE_HOST_PTR or
     * CL_MEM_COPY_HOST_PTR are set in <i>flags</i> or if <i>host_ptr</i> is not
     * NULL but CL_MEM_COPY_HOST_PTR or CL_MEM_USE_HOST_PTR are not set in <i>flags</i>.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory for
     * buffer object.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources required by
     * the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_mem clCreateBuffer(cl_context context, long flags, long size, Pointer host_ptr, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_mem result = clCreateBufferNative(context, flags, size, host_ptr, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_mem clCreateBufferNative(cl_context context, long flags, long size, Pointer host_ptr, int errcode_ret[]);

    /**
     * Used to crate an <b>image </b>(1D, or 2D) object.
     * <p>
     * cl_mem <b>clCreateImage2D (</b>cl_context <i>context</i>,
     * cl_mem_flags <i>flags</i>,
     * const cl_image_format *<i>image_format</i>,
     * size_t <i>image_width</i>,
     * size_t <i>image_height</i>,
     * size_t <i>image_row_pitch</i>,
     * void *<i>host_ptr</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>context</i> is a valid OpenCL context on which the image object is to be
     * created.
     * </p>
     * <p>
     * <i>flags</i> is a bit-field that is used to specify allocation and usage
     * information about the image memory object being created and is described in
     * <i>table 5.3</i>.
     * </p>
     * <p>
     * <i>image_format</i> is a pointer to a structure that describes format
     * properties of the image to be allocated. Refer to <i>section 5.2.4.1</i> for
     * a detailed description of the image format descriptor.
     * </p>
     * <p>
     * <i>image_width</i>, and <i>image_height</i> are the width and height of the
     * image in pixels. These must be values greater than or equal to 1.
     * </p>
     * <p>
     * <i>image_row_pitch</i> is the scan-line pitch in bytes. This must be 0 if
     * <i>host_ptr</i> is NULL and can be either 0 or &gt;= <i>image_width</i> *
     * size of element in bytes if <i>host_ptr</i> is not NULL. If <i>host_ptr</i>
     * is not NULL and <i>image_row_pitch </i>= 0, <i>image_row_pitch</i> is
     * calculated as <i>image_width</i> * size of element in bytes. If
     * <i>image_row_pitch</i> is not 0, it must be a multiple of the image element
     * size in bytes.
     * </p>
     * <p>
     * <i>host_ptr</i> is a pointer to the image data that may already be allocated
     * by the application. The size of the buffer that <i>host_ptr</i> points to
     * must be &gt;= <i>image_row_pitch</i> * <i>image_height</i>. The size of
     * each element in bytes must be a power of 2. The image data specified by
     * <i>host_ptr</i> is stored as a linear sequence of adjacent scanlines. Each
     * scanline is stored as a linear sequence of image elements.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If <i>errcode_ret</i>
     * is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateImage2D</b> returns a valid non-zero image object and
     * <i>errcode_ret</i> is set to CL_SUCCESS if the image object is created
     * successfully. Otherwise, it returns a NULL value with one of the following
     * error values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if <i>context </i>is not a valid context.
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in <i>flags </i>are not valid.
     * </p>
     * <p>
     * CL_INVALID_IMAGE_FORMAT_DESCRIPTOR if values specified in <i>image_format
     * </i>are not valid or if <i>image_format</i> is NULL.
     * </p>
     * <p>
     * CL_INVALID_IMAGE_SIZE if <i>image_width</i> or <i>image_height</i> are 0 or
     * if they exceed values specified in CL_DEVICE_IMAGE2D_MAX_WIDTH or
     * CL_DEVICE_IMAGE2D_MAX_HEIGHT respectively for all devices in<i> context</i>
     * or if values specified by <i>image_row_pitch</i> do not follow rules
     * described in the argument description above.
     * </p>
     * <p>
     * CL_INVALID_HOST_PTR if <i>host_ptr</i> is NULL and CL_MEM_USE_HOST_PTR or
     * CL_MEM_COPY_HOST_PTR are set in <i>flags</i> or if <i>host_ptr</i> is not
     * NULL but CL_MEM_COPY_HOST_PTR or CL_MEM_USE_HOST_PTR are not set in <i>flags</i>.
     * </p>
     * <p>
     * CL_IMAGE_FORMAT_NOT_SUPPORTED if the <i>image_format</i> is not supported.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory for
     * image object.
     * </p>
     * <p>
     * CL_INVALID_OPERATION if there are no devices in <i>context</i> that support
     * images (i.e. CL_DEVICE_IMAGE_SUPPORT specified in <i>table 4.3</i> is
     * CL_FALSE).
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources required by
     * the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_mem clCreateImage2D(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_row_pitch, Pointer host_ptr, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_mem result = clCreateImage2DNative(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_mem clCreateImage2DNative(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_row_pitch, Pointer host_ptr, int errcode_ret[]);

    /**
     * Used to create a 3D <b>image object </b>.
     * <p>
     * cl_mem <b>clCreateImage3D (</b>cl_context <i>context</i>,
     * cl_mem_flags <i>flags</i>, const cl_image_format *<i>image_format</i>,
     * size_t <i>image_width</i>, size_t <i>image_height</i>, size_t
     * <i>image_depth</i>, size_t <i>image_row_pitch</i>, size_t
     * <i>image_slice_pitch</i>, void *<i>host_ptr</i>, cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>context</i> is a valid OpenCL context on which the image object
     * is to be created.
     * </p>
     * <p>
     * <i>flags</i> is a bit-field that is used to specify allocation and
     * usage information about the image memory object being created and is
     * described in <i>table 5.3</i>.
     * </p>
     * <p>
     * <i>image_format</i> is a pointer to a structure that describes
     * format properties of the image to be allocated. Refer to <i>section
     * 5.2.4.1</i> for a detailed description of the image format
     * descriptor.
     * </p>
     * <p>
     * <i>image_width</i>, and <i>image_height</i> are the width and
     * height of the image in pixels. These must be values greater than or
     * equal to 1.
     * </p>
     * <p>
     * <i>image_depth</i> is the depth of the image in pixels. This must be
     * a value &gt; 1.
     * </p>
     * <p>
     * <i>image_row_pitch</i> is the scan-line pitch in bytes. This must be
     * 0 if <i>host_ptr</i> is NULL and can be either 0 or &gt;=
     * <i>image_width</i> * size of element in bytes if <i>host_ptr</i> is
     * not NULL. If <i>host_ptr</i> is not NULL and <i>image_row_pitch
     * </i>= 0, <i>image_row_pitch</i> is calculated as <i>image_width</i> *
     * size of element in bytes. If <i>image_row_pitch</i> is not 0, it
     * must be a multiple of the image element size in bytes.
     * </p>
     * <p>
     * <i>image_slice_pitch</i> is the size in bytes of each 2D slice in
     * the 3D image. This must be 0 if <i>host_ptr</i> is NULL and can be
     * either 0 or &gt;= <i>image_row_pitch</i> * <i>image_height</i> if
     * <i>host_ptr</i> is not NULL. If <i>host_ptr</i> is not NULL and
     * <i>image_slice_pitch </i>= 0, <i>image_slice_pitch</i> is calculated
     * as <i>image_row_pitch</i> * <i>image_height</i>. If
     * <i>image_slice_pitch</i> is not 0, it must be a multiple of the
     * <i>image_row_pitch</i>.
     * </p>
     * <p>
     * <i>host_ptr</i> is a pointer to the image data that may already be
     * allocated by the application. The size of the buffer that <i>host_ptr</i>
     * points to must be &gt;= <i>image_slice_pitch * image_depth</i>. The
     * size of each element in bytes must be a power of 2. The image data
     * specified by <i>host_ptr</i> is stored as a linear sequence of
     * adjacent 2D slices. Each 2D slice is a linear sequence of adjacent
     * scanlines. Each scanline is a linear sequence of image elements.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateImage3D</b> returns a valid non-zero image object created
     * and the <i>errcode_ret</i> is set to CL_SUCCESS if the image object
     * is created successfully. Otherwise, it returns a NULL value with one
     * of the following error values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if <i>context </i>is not a valid context.
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in <i>flags </i>are not valid.
     * </p>
     * <p>
     * CL_INVALID_IMAGE_FORMAT_DESCRIPTOR if values specified in
     * <i>image_format </i>are not valid or if <i>image_format</i> is NULL.
     * </p>
     * <p>
     * CL_INVALID_IMAGE_SIZE if <i>image_width</i>, <i>image_height</i>
     * are 0 or if <i>image_depth</i> &lt;= 1 or if they exceed values
     * specified in CL_DEVICE_IMAGE3D_MAX_WIDTH,
     * CL_DEVICE_IMAGE3D_MAX_HEIGHT or CL_DEVICE_IMAGE3D_MAX_DEPTH
     * respectively for all devices in<i> context</i> or if values
     * specified by <i>image_row_pitch</i> and <i>image_slice_pitch</i> do
     * not follow rules described in the argument description above.
     * </p>
     * <p>
     * CL_INVALID_HOST_PTR if <i>host_ptr</i> is NULL and
     * CL_MEM_USE_HOST_PTR or CL_MEM_COPY_HOST_PTR are set in <i>flags</i>
     * or if <i>host_ptr</i> is not NULL but
     * </p>
     * <p>
     * CL_MEM_COPY_HOST_PTR or CL_MEM_USE_HOST_PTR are not set in <i>flags</i>.
     * CL_IMAGE_FORMAT_NOT_SUPPORTED if the <i>image_format</i> is not
     * supported. CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to
     * allocate memory for image object.
     * </p>
     * <p align="left" >
     * CL_INVALID_OPERATION if there are no devices in <i>context</i> that
     * support images (i.e. CL_DEVICE_IMAGE_SUPPORT specified in <i>table
     * 4.3</i> is CL_FALSE). CL_OUT_OF_HOST_MEMORY if there is a failure to
     * allocate resources required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_mem clCreateImage3D(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Pointer host_ptr, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_mem result = clCreateImage3DNative(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_mem clCreateImage3DNative(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Pointer host_ptr, int errcode_ret[]);

    /**
     * Increments the <i>memobj</i> reference count.
     * <p>
     * cl_int <b>clRetainMemObject</b> (cl_mem <i>memobj</i>)
     * </p>
     * <p>
     * increments the <i>memobj</i> reference count. <b>clRetainMemObject</b>
     * returns CL_SUCCESS if the function is executed successfully. It returns
     * CL_INVALID_MEM_OBJECT if <i>memobj</i> is not a valid memory object.
     * <b>clCreateBuffer</b> and <b>clCreateImage{2D|3D}</b> perform an
     * implicit retain.
     * </p>
     */
    public static synchronized int clRetainMemObject(cl_mem memobj)
    {
        assertInit();
        return checkResult(clRetainMemObjectNative(memobj));
    }

    private static native int clRetainMemObjectNative(cl_mem memobj);

    /**
     * Decrements the <i>memobj</i> reference count.
     * <p>
     * cl_int <b>clReleaseMemObject</b> (cl_mem <i>memobj</i>)
     * </p>
     * <p>
     * decrements the <i>memobj</i> reference count. After the <i>memobj</i>
     * reference count becomes zero and commands queued for execution on a
     * command-queue(s) that use <i>memobj</i> have finished, the memory object
     * is deleted. <b>clReleaseMemObject</b> returns CL_SUCCESS if the function
     * is executed successfully. It returns CL_INVALID_MEM_OBJECT if <i>memobj</i>
     * is not a valid memory object.
     * </p>
     */
    public static synchronized int clReleaseMemObject(cl_mem memobj)
    {
        assertInit();
        return checkResult(clReleaseMemObjectNative(memobj));
    }

    private static native int clReleaseMemObjectNative(cl_mem memobj);

    /**
     * Get the list of image formats supported by an OpenCL
     * implementation.
     * <p>
     * cl_int <b>clGetSupportedImageFormats</b> (cl_context <i>context</i>,
     * cl_mem_flags <i>flags</i>, cl_mem_object_type <i>image_type</i>,
     * cl_uint <i>num_entries</i>, cl_image_format *<i>image_formats</i>,
     * cl_uint *<i>num_image_formats</i>)
     * </p>
     * <p>
     * <i>context</i> is a valid OpenCL context on which the image object(s)
     * will be created.
     * </p>
     * <p>
     * <i>flags</i> is a bit-field that is used to specify allocation and usage
     * information about the image memory object being created and is described
     * in <i>table 5.3</i>.
     * </p>
     * <p>
     * <i>image_type</i> describes the image type and must be either
     * CL_MEM_OBJECT_IMAGE2D or CL_MEM_OBJECT_IMAGE3D.
     * </p>
     * <p>
     * <i>num_entries</i> specifies the number of entries that can be returned
     * in the memory location given by <i>image_formats</i>.
     * </p>
     * <p>
     * <i>image_formats</i> is a pointer to a memory location where the list of
     * supported image formats are returned. Each entry describes a
     * <i>cl_image_format</i> structure supported by the OpenCL implementation.
     * If <i>image_formats</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>num_image_formats</i> is the actual number of supported image formats
     * for a specific <i>context</i> and values specified by <i>flags</i>. If
     * <i>num_image_formats</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <b>clGetSupportedImageFormats</b> returns CL_SUCCESS if the function is
     * executed successfully, returns CL_INVALID_CONTEXT if <i>context</i> is
     * not a valid context, returns CL_INVALID_VALUE if <i>flags</i> or
     * <i>image_type</i> are not valid, or if <i>num_entries</i> is 0 and
     * <i>image_formats</i> is not NULL.
     */
    public static synchronized int clGetSupportedImageFormats(cl_context context, long flags, int image_type, int num_entries, cl_image_format image_formats[], int num_image_formats[])
    {
        assertInit();
        return checkResult(clGetSupportedImageFormatsNative(context, flags, image_type, num_entries, image_formats, num_image_formats));
    }

    private static native int clGetSupportedImageFormatsNative(cl_context context, long flags, int image_type, int num_entries, cl_image_format image_formats[], int num_image_formats[]);

    /**
     * Get information that is common to all memory objects (buffer and image objects).
     * <p>
     * cl_int <b>clGetMemObjectInfo</b> (cl_mem <i>memobj</i>,
     * cl_mem_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t<i> *param_value_size_ret</i>)
     * <i>memobj</i> specifies the memory object being queried.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetMemObjectInfo</b> is described in
     * <i>table 5.8</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.8</i>.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * being queried by <i>param_value</i>. If <i>param_value_size_ret</i> is
     * NULL, it is ignored.
     * </p>
     * <p>
     * <b>clGetMemObjectInfo</b> returns CL_SUCCESS if the function is executed
     * successfully, returns CL_INVALID_VALUE if <i>param_name</i> is not
     * valid, or if size in bytes specified by <i>param_value_size</i> is &lt;
     * size of return type as described in <i>table 5.8</i> and <i>param_value</i>
     * is not NULL, and returns CL_INVALID_MEM_OBJECT if <i>memobj</i> is a not
     * a valid memory object.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>cl_mem_info </b> </th>
     * <th > <b>Return type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <th> <b>CL_MEM_TYPE </b> </th>
     * <td> cl_mem_object_type </td>
     * <td> Returns CL_MEM_OBJECT_BUFFER if <i>memobj</i> is
     * created with <b>clCreateBuffer</b>, CL_MEM_OBJECT_IMAGE2D if <i>memobj</i>
     * is created with <b>clCreateImage2D</b> or CL_MEM_OBJECT_IMAGE3D if
     * <i>memobj</i> is created with <b>clCreateImage3D</b>. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_MEM_FLAGS </b> </th>
     * <td> cl_mem_flags </td>
     * <td> Return the <i>flags</i> argument value specified when
     * <i>memobj</i> is created with <b>clCreateBuffer</b> or
     * <b>clCreateImage{2D|3D}</b>. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_MEM_SIZE </b> </th>
     * <td> size_t </td>
     * <td> Return actual size of <i>memobj</i> in bytes. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_MEM_HOST_PTR </b> </th>
     * <td> void * </td>
     * <td> Return the <i>host_ptr</i> argument value specified
     * when <i>memobj</i> is created. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_MEM_MAP_COUNT</b><b>8 </b> </th>
     * <td> cl_uint </td>
     * <td> Map count. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_MEM_REFERENCE_COUNT</b><b>9 </b> </th>
     * <td> cl_uint </td>
     * <td> Return <i>memobj</i> reference count. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_MEM_CONTEXT </b> </th>
     * <td> cl_context </td>
     * <td> Return context specified when memory object is created
     * </td>
     * </tr>
     * </table>
     */
    public static synchronized int clGetMemObjectInfo(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetMemObjectInfoNative(memobj, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetMemObjectInfoNative(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Get information specific to an image object created with
     * <b>clCreateImage{2D|3D}</b>.
     * <p>
     * cl_int <b>clGetImageInfo</b> (cl_mem <i>image</i>, cl_image_info
     * <i>param_name</i>, size_t <i>param_value_size</i>, void *<i>param_value</i>,
     * size_t<i> *param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>image</i> specifies the image object being queried.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetImageInfo</b> is described in <i>table
     * 5.9</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.9</i>.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * being queried by <i>param_value</i>. If <i>param_value_size_ret</i> is
     * NULL, it is ignored.
     * </p>
     * <p>
     * <b>clGetImageInfo</b> returns CL_SUCCESS if the function is executed
     * successfully, returns CL_INVALID_VALUE if <i>param_name</i> is not
     * valid, or if size in bytes specified by <i>param_value_size</i> is &lt;
     * size of return type as described in <i>table 5.9 </i>and <i>param_value</i>
     * is not NULL, and returns CL_INVALID_MEM_OBJECT if <i>image</i> is a not
     * a valid image object.
     * </p>
     * <table border="1"> 
     * <tr>
     * <th > <b>cl_image_info </b> </th>
     * <th > <b>Return type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <th> <b>CL_IMAGE_FORMAT </b> </th>
     * <td> cl_image_format </td>
     * <td> Return image format descriptor specified when <i>image</i>
     * is created with <b>clCreateImage{2D|3D}</b>. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_IMAGE_ELEMENT_SIZE </b> </th>
     * <td> size_t </td>
     * <td> Return size of each element of the image memory object
     * given by <i>image</i>. An element is made up of <i>n</i> channels. The
     * value of <i>n</i> is given in <i>cl_image_format</i> descriptor. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_IMAGE_ROW_PITCH </b> </th>
     * <td> size_t </td>
     * <td> Return size in bytes of a row of elements of the image
     * object given by <i>image</i>. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_IMAGE_SLICE_PITCH </b> </th>
     * <td> size_t </td>
     * <td> Return size in bytes of a 2D slice for the 3D image
     * object given by <i>image</i>. For a 2D image object this value will be
     * 0. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_IMAGE_WIDTH </b> </th>
     * <td> size_t </td>
     * <td> Return width of the image in pixels </td>
     * </tr>
     * <tr>
     * <th> <b>CL_IMAGE_HEIGHT </b> </th>
     * <td> size_t </td>
     * <td> Return height of the image in pixels </td>
     * </tr>
     * <tr>
     * <th> <b>CL_IMAGE_DEPTH </b> </th>
     * <td> size_t </td>
     * <td> Return depth of the image in pixels. For a 2D image,
     * depth = 0. </td>
     * </tr>
     * </table>
     */
    public static synchronized int clGetImageInfo(cl_mem image, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetImageInfoNative(image, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetImageInfoNative(cl_mem image, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Creates a sampler object.
     * <p>
     * cl_sampler <b>clCreateSampler</b> (cl_context <i>context</i>,
     * cl_bool <i>normalized_coords</i>,
     * cl_addressing_mode <i>addressing_mode</i>,
     * cl_filter_mode <i>filter_mode</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p align="left" >
     * Refer to <i>section 6.11.8.1</i> for a
     * detailed description of how samplers work.
     * </p>
     * <p>
     * <i>context</i> must be a valid OpenCL context.
     * </p>
     * <p>
     * <i>normalized_coords</i> determines if the image coordinates specified
     * are normalized (if <i>normalized_coords </i>is CL_TRUE) or not (if
     * <i>normalized_coords</i> is CL_FALSE).
     * </p>
     * <p>
     * <i>addressing_mode</i> specifies how out-of-range image coordinates are
     * handled when reading from an image. This can be set to CL_ADDRESS_REPEAT,
     * CL_ADDRESS_CLAMP_TO_EDGE, CL_ADDRESS_CLAMP and CL_ADDRESS_NONE.
     * </p>
     * <p>
     * <i>filtering_mode</i> specifies the type of filter that must be applied
     * when reading an image. This can be CL_FILTER_NEAREST or CL_FILTER_LINEAR.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateSampler </b>returns a valid non-zero sampler object and
     * <i>errcode_ret</i> is set to CL_SUCCESS if the sampler object is created
     * successfully. Otherwise, it returns a NULL value with one of the
     * following error values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if <i>context </i>is not a valid context.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>addressing_mode, filter_mode </i>or<i>
     * normalized_coords </i>or combination of these argument values are not
     * valid.
     * </p>
     * <p>
     * CL_INVALID_OPERATION if images are not supported by any device associated
     * with
     * </p>
     * <p>
     * <i>context</i> (i.e. CL_DEVICE_IMAGE_SUPPORT specified in <i>table 4.3</i>
     * is CL_FALSE).
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_sampler clCreateSampler(cl_context context, boolean normalized_coords, int addressing_mode, int filter_mode, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_sampler result = clCreateSamplerNative(context, normalized_coords, addressing_mode, filter_mode, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_sampler clCreateSamplerNative(cl_context context, boolean normalized_coords, int addressing_mode, int filter_mode, int errcode_ret[]);

    /**
     * Increments the <i>sampler</i> reference count.
     * <p>
     * cl_int <b>clRetainSampler</b> (cl_sampler <i>sampler</i>)
     * </p>
     * <p>
     * <b>clCreateSampler</b>
     * does an implicit retain. <b>clRetainSampler</b> returns CL_SUCCESS if
     * the function is executed successfully. It returns CL_INVALID_SAMPLER if
     * <i>sampler</i> is not a valid sampler object.
     * </p>
     * <p>
     * The function
     * </p>
     */
    public static synchronized int clRetainSampler(cl_sampler sampler)
    {
        assertInit();
        return checkResult(clRetainSamplerNative(sampler));
    }

    private static native int clRetainSamplerNative(cl_sampler sampler);

    /**
     * Decrements the <i>sampler</i> reference count.
     * <p align="left" >
     * cl_int <b>clReleaseSampler</b> (cl_sampler <i>sampler</i>)
     * </p>
     * <p>
     * The sampler object is
     * deleted after the reference count becomes zero and commands queued for
     * execution on a command-queue(s) that use <i>sampler </i>have finished.
     * <b>clReleaseSampler</b> returns CL_SUCCESS if the function is executed
     * successfully. It returns CL_INVALID_SAMPLER if <i>sampler</i> is not a
     * valid sampler object.
     * </p>
     */
    public static synchronized int clReleaseSampler(cl_sampler sampler)
    {
        assertInit();
        return checkResult(clReleaseSamplerNative(sampler));
    }

    private static native int clReleaseSamplerNative(cl_sampler sampler);

    /**
     * Returns information about the sampler object.
     * <p>
     * cl_int <b>clGetSamplerInfo</b> (cl_sampler <i>sampler</i>,
     * cl_sampler_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * <p>
     * <i>sampler</i> specifies the sampler being queried.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetSamplerInfo</b> is described in <i>table
     * 5.10</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.10. </i>
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * copied to <i>param_value</i>. If <i>param_value_size_ret</i> is NULL,
     * it is ignored.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>cl_sampler_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <th> <b>CL_SAMPLER_REFERENCE_ COUNT</b><b>10 </b> </th>
     * <td> cl_uint </td>
     * <td> Return the <i>sampler</i> reference count. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_SAMPLER_CONTEXT </b> </th>
     * <td> cl_context </td>
     * <td> Return the context specified when the sampler is
     * created. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_SAMPLER_ADDRESSING_ MODE </b> </th>
     * <td> cl_addressing_ mode </td>
     * <td> Return the value specified by <i>addressing_mode</i>
     * argument to <b>clCreateSampler</b>. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_SAMPLER_FILTER_MODE </b> </th>
     * <td> cl_filter_mode </td>
     * <td> Return the value specified by <i>filter_mode</i>
     * argument to <b>clCreateSampler</b>. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_SAMPLER_NORMALIZED_ COORDS </b> </th>
     * <td> cl_bool </td>
     * <td> Return the value specified by <i>normalized_coords</i>
     * argument to <b>clCreateSampler</b>. </td>
     * </tr>
     * </table>
     * <p>
     * <b>clGetSamplerInfo</b> returns CL_SUCCESS if the function is executed
     * successfully, returns CL_INVALID_VALUE if <i>param_name</i> is not
     * valid, or if size in bytes specified by <i>param_value_size</i> is &lt;
     * size of return type as described in <i>table 5.10 </i>and <i>param_value</i>
     * is not NULL, and returns CL_INVALID_SAMPLER if <i>sampler</i> is a not a
     * valid sampler object.
     * </p>
     */
    public static synchronized int clGetSamplerInfo(cl_sampler sampler, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetSamplerInfoNative(sampler, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetSamplerInfoNative(cl_sampler sampler, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Creates a program object for a context.
     * <p>
     * cl_program <b>clCreateProgramWithSource</b> (cl_context<i> context</i>,
     * cl_uint <i>count</i>,
     * const char **<i>strings</i>,
     * const size_t *<i>lengths</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p align="left" >
     * creates a program object for a context, and loads the source code
     * specified by the text strings in the <i>strings</i> array into the
     * program object. The devices associated with the program object are the
     * devices associated with <i>context</i>.
     * </p>
     * <p>
     * <i>context</i> must be a valid OpenCL context.
     * </p>
     * <p>
     * <i>strings</i> is an array of <i>count</i> pointers to optionally
     * null-terminated character strings that make up the source code.
     * </p>
     * <p>
     * The <i>lengths</i> argument is an array with the number of chars in each
     * string (the string length). If an element in <i>lengths</i> is zero, its
     * accompanying string is null-terminated. If <i>lengths</i> is NULL, all
     * strings in the <i>strings</i> argument are considered null-terminated.
     * Any length value passed in that is greater than zero excludes the null
     * terminator in its count.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateProgramWithSource</b> returns a valid non-zero program object
     * and <i>errcode_ret</i> is set to CL_SUCCESS if the program object is
     * created successfully. Otherwise, it returns a NULL value with one of the
     * following error values returned in <i>errcode_ret</i>:
     * CL_INVALID_CONTEXT if <i>context </i>is not a valid context.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>count</i> is zero or if <i>strings</i> or any
     * entry in <i>strings</i> is NULL.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_program clCreateProgramWithSource(cl_context context, int count, String strings[], long lengths[], int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_program result = clCreateProgramWithSourceNative(context, count, strings, lengths, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_program clCreateProgramWithSourceNative(cl_context context, int count, String strings[], long lengths[], int errcode_ret[]);

    /**
     * Creates a program object for a context.
     * <p>
     * cl_program <b>clCreateProgramWithBinary</b> (cl_context <i>context</i>,
     * cl_uint <i>num_devices</i>,
     * const cl_device_id *<i>device_list</i>,
     * const size_t *<i>lengths</i>,
     * const unsigned char **<i>binaries</i>,
     * cl_int *<i>binary_status</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * creates a program object for a context, and loads the binary bits
     * specified by <i>binary</i> into the program object.
     * </p>
     * <p>
     * <i>context</i> must be a valid OpenCL context.
     * </p>
     * <p>
     * <i>device_list</i> is a pointer to a list of devices that are in
     * <i>context</i>. <i>device_list</i> must be a non-NULL value. The
     * binaries are loaded for devices specified in this list.
     * </p>
     * <p>
     * <i>num_devices </i>is the number of devices listed in <i>device_list</i>.
     * </p>
     * <p>
     * The devices associated with the program object will be the list of
     * devices specified by <i>device_list</i>. The list of devices specified
     * by <i>device_list</i> must be devices associated with <i>context</i>.
     * </p>
     * <p>
     * <i>lengths</i> is an array of the size in bytes of the program binaries
     * to be loaded for devices specified by <i>device_list</i>.
     * </p>
     * <p>
     * <i>binaries</i> is an array of pointers to program binaries to be loaded
     * for devices specified by <i>device_list</i>. For each device given by
     * <i>device_list</i>[i], the pointer to the program binary for that device
     * is given by <i>binaries</i>[i] and the length of this corresponding
     * binary is given by <i>lengths</i>[i]. <i>lengths</i>[i] cannot be zero
     * and <i>binaries</i>[i] cannot be a NULL pointer.
     * </p>
     * <p>
     * The program binaries specified by <i>binaries</i> contain the bits that
     * describe the program executable that will be run on the device(s)
     * associated with <i>context</i>. The program binary can consist of either
     * or both:
     * </p>
     * <p>
     * Device-specific executable(s), and/or,
     * </p>
     * <p align="left" >
     * Implementation-specific intermediate representation (IR) which will be
     * converted to the device-specific executable.
     * </p>
     * <p>
     * <i>binary_status</i> returns whether the program binary for each device
     * specified in <i>device_list</i> was loaded successfully or not. It is an
     * array of <i>num_devices</i> entries and returns CL_SUCCESS in
     * <i>binary_status[i]</i> if binary was successfully loaded for device
     * specified by <i>device_list[i]</i>; otherwise returns CL_INVALID_VALUE
     * if <i>lengths[i]</i> is zero or if <i>binaries[i]</i> is a NULL value
     * or CL_INVALID_BINARY in <i>binary_status[i]</i> if program binary is not
     * a valid binary for the specified device. If <i>binary_status</i> is
     * NULL, it is ignored.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clCreateProgramWithBinary</b> returns a valid non-zero program object
     * and <i>errcode_ret</i> is set to CL_SUCCESS if the program object is
     * created successfully. Otherwise, it returns a NULL value with one of the
     * following error values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if <i>context </i>is not a valid context.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>device_list</i> is NULL or <i>num_devices</i> is
     * zero.
     * </p>
     * <p>
     * CL_INVALID_DEVICE if OpenCL devices listed in <i>device_list</i> are not
     * in the list of devices associated with <i>context</i>.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>lengths</i> or <i>binaries</i> are NULL or if
     * any entry in <i>lengths</i>[i] is zero or <i>binaries</i>[i] is NULL.
     * </p>
     * <p>
     * CL_INVALID_BINARY if an invalid program binary was encountered for any
     * device. <i>binary_status</i> will return specific status for each
     * device.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * OpenCL allows applications to create a program object using the program
     * source or binary and build appropriate program executables. This allows
     * applications to determine whether they want to use the pre-built offline
     * binary or load and compile the program source and use the executable
     * compiled/linked online as the program executable. This can be very useful
     * as it allows applications to load and build program executables online on
     * its first instance for appropriate OpenCL devices in the system. These
     * executables can now be queried and cached by the application. Future
     * instances of the application launching will no longer need to compile and
     * build the program executables. The cached executables can be read and
     * loaded by the application, which can help significantly reduce the
     * application initialization time.
     * </p>
     */
    public static synchronized cl_program clCreateProgramWithBinary(cl_context context, int num_devices, cl_device_id device_list[], long lengths[], byte binaries[][], int binary_status[], int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_program result = clCreateProgramWithBinaryNative(context, num_devices, device_list, lengths, binaries, binary_status, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_program clCreateProgramWithBinaryNative(cl_context context, int num_devices, cl_device_id device_list[], long lengths[], byte binaries[][], int binary_status[], int errcode_ret[]);

    /**
     * Increments the <i>program</i> reference count. 
     * <p>
     * cl_int <b>clRetainProgram</b> (cl_program <i>program</i>)
     * </p>
     * <p>
     * <b>clCreateProgram</b>
     * does an implicit retain. <b>clRetainProgram</b> returns CL_SUCCESS if
     * the function is executed successfully. It returns CL_INVALID_PROGRAM if
     * <i>program</i> is not a valid program object.
     * </p>
     * <p>
     * The function
     * </p>
     */
    public static synchronized int clRetainProgram(cl_program program)
    {
        assertInit();
        return checkResult(clRetainProgramNative(program));
    }

    private static native int clRetainProgramNative(cl_program program);

    /**
     * Decrements the <i>program</i> reference count. 
     * <p>
     * cl_int <b>clReleaseProgram</b> (cl_program <i>program</i>)
     * </p>
     * <p>
     * The program object is
     * deleted after all kernel objects associated with <i>program</i> have
     * been deleted and the <i>program</i> reference count becomes zero.
     * <b>clReleaseProgram</b> returns CL_SUCCESS if the function is executed
     * successfully. It returns CL_INVALID_PROGRAM if <i>program</i> is not a
     * valid program object.
     * </p>
     */
    public static synchronized int clReleaseProgram(cl_program program)
    {
        assertInit();
        return checkResult(clReleaseProgramNative(program));
    }

    private static native int clReleaseProgramNative(cl_program program);

    /**
     * Builds (compiles &amp; links) a program executable.
     * <p align="left" >
     * cl_int <b>clBuildProgram</b> (cl_program <i>program</i>,
     * cl_uint <i>num_devices</i>,
     * const cl_device_id *<i>device_list</i>,
     * const char *<i>options</i>,
     * void <i>(*pfn_notify)(cl_program, void *user_data</i>),
     * void *<i>user_data</i>)
     * </p>
     * <p>
     * builds (compiles &amp; links) a program executable from the program
     * source or binary for all the devices or a specific device(s) in the
     * OpenCL context associated with <i>program</i>. OpenCL allows program
     * executables to be built using the source or the binary.
     * </p>
     * <p>
     * <i>program</i> is the program object.
     * </p>
     * <p>
     * <i>device_list</i> is a pointer to a list of devices associated with
     * <i>program</i>. If <i>device_list</i> is a NULL value, the program
     * executable is built for all devices associated with <i>program</i> for
     * which a source or binary has been loaded. If <i>device_list</i> is a
     * non-NULL value, the program executable is built for devices specified in
     * this list for which a source or binary has been loaded.
     * </p>
     * <p>
     * <i>num_devices </i>is the number of devices listed in <i>device_list</i>.
     * </p>
     * <p>
     * <i>options</i> is a pointer to a string that describes the build options
     * to be used for building the program executable. The list of supported
     * options is described <i>in section 5.4.3</i>.
     * </p>
     * <p>
     * <i>pfn_notify</i> is a function pointer to a notification routine. The
     * notification routine is a callback
     * </p>
     * <p>
     * function that an application can register and which will be called when
     * the program executable has been built (successfully or unsuccessfully).
     * If <i>pfn_notify</i> is not NULL, <b>clBuildProgram</b> does not need
     * to wait for the build to complete and can return immediately. If
     * <i>pfn_notify</i> is NULL, <b>clBuildProgram</b> does not return until
     * the build has completed. This callback function may be called
     * asynchronously by the OpenCL implementation. It is the
     * application&#226;&#8364;&#8482;s responsibility to ensure that the
     * callback function is thread-safe.
     * </p>
     * <p>
     * <i>user_data</i> will be passed as an argument when <i>pfn_notify</i>
     * is called. <i>user_data</i> can be NULL.
     * </p>
     * <p>
     * <b>clBuildProgram</b> returns CL_SUCCESS if the function is executed
     * successfully. Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_PROGRAM if <i>program</i> is not a valid program object.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>device_list</i> is NULL and <i>num_devices</i>
     * is greater than zero, or if <i>device_list</i> is not NULL and
     * <i>num_devices</i> is zero.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>pfn_notify</i> is NULL but <i>user_data</i> is
     * not NULL.
     * </p>
     * <p>
     * CL_INVALID_DEVICE if OpenCL devices listed in <i>device_list</i> are not
     * in the list of devices associated with <i>program</i>.
     * </p>
     * <p>
     * CL_INVALID_BINARY if <i>program</i> is created with
     * <b>clCreateWithProgramBinary</b> and devices listed in <i>device_list</i>
     * do not have a valid program binary loaded.
     * </p>
     * <p>
     * CL_INVALID_BUILD_OPTIONS if the build options specified by <i>options</i>
     * are invalid.
     * </p>
     * <p>
     * CL_INVALID_OPERATION if the build of a program executable for any of the
     * devices listed in <i>device_list</i> by a previous call to
     * <b>clBuildProgram</b> for <i>program</i> has not completed.
     * </p>
     * <p>
     * CL_COMPILER_NOT_AVAILABLE if <i>program</i> is created with
     * <b>clCreateProgramWithSource</b> and a compiler is not available i.e.
     * CL_DEVICE_COMPILER_AVAILABLE specified in <i>table 4.3</i> is set to
     * CL_FALSE.
     * </p>
     * <p>
     * CL_BUILD_PROGRAM_FAILURE if there is a failure to build the program
     * executable. This error will be returned if <b>clBuildProgram</b> does
     * not return until the build has completed.
     * </p>
     * <p>
     * CL_INVALID_OPERATION if there are kernel objects attached to <i>program</i>.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized int clBuildProgram(cl_program program, int num_devices, cl_device_id device_list[], String options, BuildProgramFunction pfn_notify, Object user_data)
    {
        assertInit();
        return checkResult(clBuildProgramNative(program, num_devices, device_list, options, pfn_notify, user_data));
    }

    private static native int clBuildProgramNative(cl_program program, int num_devices, cl_device_id device_list[], String options, BuildProgramFunction pfn_notify, Object user_data);

    /**
     * Allows to release the resources allocated by the OpenCL compiler.
     * <p>
     * cl_int <b>clUnloadCompiler</b> (void)
     * </p>
     * <p>
     * allows the implementation to release the resources allocated by the
     * OpenCL compiler. This is a hint from the application and does not
     * guarantee that the compiler will not be used in the future or that the
     * compiler will actually be unloaded by the implementation. Calls to
     * <b>clBuildProgram </b>after <b>clUnloadCompiler</b> will reload the
     * compiler, if necessary, to build the appropriate program executable. This
     * call currently always returns CL_SUCCESS.
     * </p>
     */
    public static synchronized int clUnloadCompiler()
    {
        assertInit();
        return checkResult(clUnloadCompilerNative());
    }

    private static native int clUnloadCompilerNative();

    /**
     * Returns information about the program object.
     * <p>
     * cl_int <b>clGetProgramInfo</b> (cl_program <i>program</i>,
     * cl_program_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <i>program</i> specifies the program object being queried.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetProgramInfo</b> is described in <i>table
     * 5.11</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.11. </i>
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * copied to <i>param_value</i>. If <i>param_value_size_ret</i> is NULL,
     * it is ignored.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>cl_program_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <th> <b>CL_PROGRAM_REFERENCE_ COUNT</b><b>11 </b> </th>
     * <td> cl_uint </td>
     * <td> Return the <i>program</i> reference count. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_PROGRAM_CONTEXT </b> </th>
     * <td> cl_context </td>
     * <td> Return the context specified when the program object is
     * created </td>
     * </tr>
     * <tr>
     * <th> <b>CL_PROGRAM_NUM_DEVICES </b> </th>
     * <td> cl_uint </td>
     * <td> Return the number of devices associated with <i>program</i>.
     * </td>
     * </tr>
     * <tr>
     * <th> <b>CL_PROGRAM_DEVICES </b> </th>
     * <td> cl_device_id[] </td>
     * <td> Return the list of devices associated with the program
     * object. This can be the devices associated with context on which the
     * program object has been created or can be a subset of devices that are
     * specified when a program object is created using
     * <b>clCreateProgramWithBinary</b>. </td>
     * </tr>
     * <tr>
     * <th> <b>CL_PROGRAM_SOURCE </b> </th>
     * <td> char[] </td>
     * <td> Return the program source code specified by <b>clCreateProgramWithSource</b>. 
     * The source string
     * returned is a concatenation of all source strings specified to
     * <b>clCreateProgramWithSource</b> with a null terminator. The
     * concatenation strips any nulls in the original source strings. The actual
     * number of characters that represents the program source code including
     * the null terminator is returned in <i>param_value_size_ret</i>. </td>
     * </tr>
     * <tr>
     * <th > <b>CL_PROGRAM_BINARY_SIZES </b> </th>
     * <td > size_t[] </td>
     * <td > Returns an array that contains the size in bytes of the program
     * binary for each device associated with <i>program</i>. The size of the
     * array is the number of devices associated with <i>program</i>. If a
     * binary is not available for a device(s), a size of zero is returned.
     * </td>
     * </tr>
     * <tr>
     * <th > <b>CL_PROGRAM_BINARIES </b> </th>
     * <td > unsigned char *[] </td>
     * <td > Return the program binaries for all devices associated with
     * <i>program</i>. For each device in <i>program</i>, the binary returned
     * can be the binary specified for the device when <i>program</i> is
     * created with <b>clCreateProgramWithBinary</b> or it can be the
     * executable binary generated by <b>clBuildProgram</b>. If <i>program</i>
     * is created with <b>clCreateProgramWithSource</b>, the binary returned is
     * the binary generated by <b>clBuildProgram</b>. The bits returned can be
     * an implementation-specific intermediate representation (a.k.a. IR) or
     * device specific executable bits or both. The decision on which
     * information is returned in the binary is up to the OpenCL implementation.
     * <i>param_value</i> points to an array of n pointers where nis the number
     * of devices associated with program. The buffer sizes needed to allocate
     * the memory that these n pointers refer to can be queried using the 
     * CL_PROGRAM_BINARY_SIZES query as described in this table. Each
     * entry in this array is used by the implementation as the location in
     * memory where to copy the program binary for a specific device, if there
     * is a binary available. To find out which device the program binary in the
     * array refers to, use the CL_PROGRAM_DEVICES query to get the list of
     * devices. There is a one-toone correspondence between the array of
     * npointers returned by CL_PROGRAM_BINARIES and array of devices returned
     * by CL_PROGRAM_DEVICES. </th>
     * </tr>
     * </table>
     * <p>
     * <b>clGetProgramInfo</b> returns CL_SUCCESS if the function is executed
     * successfully, returns CL_INVALID_VALUE if <i>param_name</i> is not
     * valid, or if size in bytes specified by <i>param_value_size</i> is &lt;
     * size of return type as described in <i>table 5.11 </i>and <i>param_value</i>
     * is not NULL, and returns CL_INVALID_PROGRAM if <i>program</i> is a not a
     * valid program object.
     * </p>
     */
    public static synchronized int clGetProgramInfo(cl_program program, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetProgramInfoNative(program, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetProgramInfoNative(cl_program program, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Returns build information for each device in the program object.
     * <p>
     * cl_int <b>clGetProgramBuildInfo</b> (cl_program <i>program</i>,
     * cl_device_id <i>device</i>,
     * cl_program_build_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>program</i> specifies the program object being queried.
     * </p>
     * <p>
     * <i>device</i> specifies the device for which build information is being
     * queried. <i>device</i> must be a valid device associated with <i>program</i>.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetProgramBuildInfo</b> is described in
     * <i>table 5.12</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.12. </i>
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * copied to <i>param_value</i>. If <i>param_value_size_ret</i> is NULL,
     * it is ignored.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>cl_program_buid_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <td > <b>CL_PROGRAM_BUILD_STATUS </b> </td>
     * <td > cl_build_status </td>
     * <td > Returns the build status of <i>program</i> for a specific device
     * as given by <i>device</i>. This can be one of the following:
     * CL_BUILD_NONE. The build status returned if no build has been performed
     * on the specified program object for <i>device</i>. CL_BUILD_ERROR. The
     * build status returned if the last call to <b>clBuildProgram</b> on the
     * specified program object for <i>device</i> generated an error.
     * CL_BUILD_SUCCESS. The build status retrned if the last call to
     * <b>clBuildProgram</b> on the specified program object for <i>device</i>
     * was successful. CL_BUILD_IN_PROGRESS. The build status returned if the
     * last call to <b>clBuildProgram</b> on the specified program object for
     * <i>device</i> has not finished. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_PROGRAM_BUILD_OPTIONS </b> </td>
     * <td > char[] </td>
     * <td > Return the build options specified by the <i>options</i> argument
     * in <b>clBuildProgram</b> for <i>device</i>. If build status of
     * <i>program</i> for <i>device</i> is CL_BUILD_NONE, an empty string is
     * returned. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_PROGRAM_BUILD_LOG </b> </td>
     * <td > char[] </td>
     * <td > Return the build log when <b>clBuildProgram</b> was called for
     * <i>device</i>. </td>
     * </tr>
     * </table>
     * <p>
     * If build status of <i>program</i> for <i>device</i> is CL_BUILD_NONE,
     * an empty string is returned.
     * </p>
     * <p>
     * <b>clGetProgramBuildInfo</b> returns CL_SUCCESS if the function is
     * executed successfully, returns CL_INVALID_DEVICE if <i>device</i> is not
     * in the list of devices associated with <i>program</i>, returns
     * CL_INVALID_VALUE if <i>param_name</i> is not valid, or if size in bytes
     * specified by <i>param_value_size</i> is &lt; size of return type as
     * described in <i>table 5.12</i> and <i>param_value</i> is not NULL, and
     * returns CL_INVALID_PROGRAM if <i>program</i> is a not a valid program
     * object.
     * </p>
     */
    public static synchronized int clGetProgramBuildInfo(cl_program program, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetProgramBuildInfoNative(program, device, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetProgramBuildInfoNative(cl_program program, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Create a kernel.
     * <p>
     * cl_kernel <b>clCreateKernel</b>
     * (cl_program <i>program,</i> const char *<i>kernel_name</i>,
     * cl_int *<i>errcode_ret</i>) 
     * </p>
     * <i>program</i> is a program object with a
     * successfully built executable. <i>kernel_name</i> is a function name in
     * the program declared with the __kernelqualifier. <i>errcode_ret</i> will
     * return an appropriate error code. If <i>errcode_ret</i> is NULL, no
     * error code is returned. <b>clCreateKernel</b> returns a valid non-zero kernel object
     * and <i>errcode_ret</i> is set to CL_SUCCESS if the kernel object is
     * created successfully. Otherwise, it returns a NULL value with one of the following 
     * error values returned in <i>errcode_ret</i>:
     * <p>
     * CL_INVALID_PROGRAM if <i>program </i>is not a valid program object.
     * </p>
     * <p>
     * CL_INVALID_PROGRAM_EXECUTABLE if there is no successfully built
     * executable for <i>program</i>. 
     * </p>
     * <p>
     * CL_INVALID_KERNEL_NAME if <i>kernel_name</i> is not
     * found in <i>program</i>. CL_INVALID_KERNEL_DEFINITION if the function
     * definition for __kernel function given by <i>kernel_name</i> such as the 
     * number of arguments, the argument types are not the same for all devices 
     * for which the <i>program</i> executable has been built.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>kernel_name</i> is NULL. CL_OUT_OF_HOST_MEMORY if
     * there is a failure to allocate resources required by the OpenCL
     * implementation on the host.
     * </p>
     */
    public static synchronized cl_kernel clCreateKernel(cl_program program, String kernel_name, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_kernel result = clCreateKernelNative(program, kernel_name, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_kernel clCreateKernelNative(cl_program program, String kernel_name, int errcode_ret[]);

    /**
     * Ccreates kernel objects for all kernel functions in <i>program</i>.
     * <p>
     * cl_int <b>clCreateKernelsInProgram</b> (cl_program<i> program</i>,
     * cl_uint <i>num_kernels</i>,
     * cl_kernel *<i>kernels</i>,
     * cl_uint *<i>num_kernels_ret</i>)
     * </p>
     * <p>
     * Kernel objects are not created for any __kernelfunctions in <i>program</i>
     * that do not have the same function definition across all devices for
     * which a program executable has been successfully built.
     * </p>
     * <p>
     * <i>program</i> is a program object with a successfully built executable.
     * </p>
     * <p>
     * <i>num_kernels</i> is the size of memory pointed to by <i>kernels</i>
     * specified as the number of cl_kernel entries.
     * </p>
     * <p>
     * <i>kernels</i> is the buffer where the kernel objects for kernels in
     * <i>program</i> will be returned. If <i>kernels </i>is NULL, it is
     * ignored. If <i>kernels</i> is not NULL, <i>num_kernels</i> must be
     * greater than or equal to the number of kernels in <i>program</i>.
     * </p>
     * <p>
     * <i>num_kernels_ret</i> is the number of kernels in <i>program</i>. If
     * <i>num_kernels_ret</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <b>clCreateKernelsInProgram</b> will return CL_SUCCESS if the kernel
     * objects were successfully allocated, returns CL_INVALID_PROGRAM if
     * <i>program</i> is not a valid program object, returns
     * CL_INVALID_PROGRAM_EXECUTABLE if there is no successfully built
     * executable for any device in <i>program</i>, returns CL_INVALID_VALUE if
     * <i>kernels</i> is not NULL and <i>num_kernels</i> is less than the
     * number of kernels in <i>program</i> and returns CL_OUT_OF_HOST_MEMORY if
     * there is a failure to allocate resources required by the OpenCL
     * implementation on the host.
     * </p>
     * <p>
     * Kernel objects can only be created once you have a program object with a
     * valid program source or binary loaded into the program object and the
     * program executable has been successfully built for one or more devices
     * associated with program. No changes to the program executable are allowed
     * while there are kernel objects associated with a program object. This
     * means that calls to <b>clBuildProgram</b> return CL_INVALID_OPERATION if
     * there are kernel objects attached to a program object. The OpenCL context
     * associated with <i>program</i> will be the context associated with
     * <i>kernel</i>. The list of devices associated with <i>program</i> are
     * the devices associated with <i>kernel</i>. Devices associated with a
     * program object for which a valid program executable has been built can be
     * used to execute kernels declared in the program object.
     * </p>
     */
    public static synchronized int clCreateKernelsInProgram(cl_program program, int num_kernels, cl_kernel kernels[], int num_kernels_ret[])
    {
        assertInit();
        return checkResult(clCreateKernelsInProgramNative(program, num_kernels, kernels, num_kernels_ret));
    }

    private static native int clCreateKernelsInProgramNative(cl_program program, int num_kernels, cl_kernel kernels[], int num_kernels_ret[]);

    /**
     * Increments the <i>kernel</i> reference count.
     * <p>
     * cl_int <b>clRetainKernel</b> (cl_kernel <i>kernel</i>)
     * </p>
     * <p> 
     * <b>clRetainKernel</b> returns CL_SUCCESS
     * if the function is executed successfully. It returns CL_INVALID_KERNEL if
     * <i>kernel</i> is not a valid kernel object. <b>clCreateKernel</b> or
     * <b>clCreateKernelsInProgram</b> do an implicit retain.
     * </p>
     */
    public static synchronized int clRetainKernel(cl_kernel kernel)
    {
        assertInit();
        return checkResult(clRetainKernelNative(kernel));
    }

    private static native int clRetainKernelNative(cl_kernel kernel);

    /**
     * Decrements the <i>kernel</i> reference count.
     * <p>
     * cl_int <b>clReleaseKernel</b> (cl_kernel <i>kernel</i>)
     * </p>
     * <p>
     * <b>clReleaseKernel</b>
     * returns CL_SUCCESS if the function is executed successfully. It returns
     * CL_INVALID_KERNEL if <i>kernel</i> is not a valid kernel object. The
     * kernel object is deleted once the number of instances that are retained
     * to <i>kernel</i> become zero and the kernel object is no longer needed
     * by any enqueued commands that use <i>kernel</i>.
     * </p>
     */
    public static synchronized int clReleaseKernel(cl_kernel kernel)
    {
        assertInit();
        return checkResult(clReleaseKernelNative(kernel));
    }

    private static native int clReleaseKernelNative(cl_kernel kernel);

    /**
     * Set the argument value for a specific argument of a kernel.
     * <p align="left" >
     * cl_int <b>clSetKernelArg </b>(cl_kernel <i>kernel</i>, cl_uint
     * <i>arg_index</i>, size_t <i>arg_size</i>, const void *<i>arg_value</i>)
     * </p>
     * <p>
     * To execute a kernel, the kernel arguments must be set.
     * </p>
     * <p>
     * <i>kernel</i> is a valid kernel object.
     * </p>
     * <p>
     * <i>arg_index</i> is the argument index. Arguments to the kernel are
     * referred by indices that go from 0 for the leftmost argument to <i>n</i> -
     * 1, where <i>n</i> is the total number of arguments declared by a kernel.
     * </p>
     * <p>
     * For example, consider the following kernel:
     * </p>
     * <pre>
     * __kernel void image_filter (
     *     int n, int m,
     *     __constant float *filter_weights,
     *     __read_only image2d_t src_image,
     *     __write_only image2d_t dst_image)
     * { ... }
     * </pre>
     * <p align="left" >
     * Argument index values for image_filterwill be 0for n, 1for m, 2for
     * filter_weights, 3for src_imageand 4for dst_image.
     * </p>
     * <p>
     * <i>arg_value</i> is a pointer to data that should be used as the
     * argument value for argument specified by <i>arg_index</i>. The argument
     * data pointed to by<i> arg_value</i> is copied and the <i>arg_value</i>
     * pointer can therefore be reused by the application after
     * <b>clSetKernelArg</b> returns. The argument value specified is the value
     * used by all API calls that enqueue <i>kernel</i> (<b>clEnqueueNDRangeKernel</b>
     * and <b>clEnqueueTask</b>) until the argument value is changed by a call
     * to <b>clSetKernelArg</b> for <i>kernel</i>.
     * </p>
     * <p>
     * If the argument is a memory object (buffer or image), the <i>arg_value</i>
     * entry will be a pointer to the appropriate buffer or image object. The
     * memory object must be created with the context associated with the kernel
     * object. A NULL value can also be specified if the argument is a buffer
     * object in which case a NULL value will be used as the value for the
     * argument declared as a pointer to <i>__global</i> or <i>__constant</i>
     * memory in the kernel. If the argument is declared with the <i>__local</i>
     * qualifier, the <i>arg_value</i> entry must be NULL. If the argument is
     * of type <i>sampler_t</i>, the <i>arg_value</i> entry must be a pointer
     * to the sampler object. For all other kernel arguments, the <i>arg_value</i>
     * entry must be a pointer to the actual data to be used as argument value.
     * </p>
     * <p>
     * The memory object specified as argument value must be a buffer object (or
     * NULL) if the argument is declared to be a pointer of a built-in or user
     * defined type with the <i>__global</i> or <i>__constant</i> qualifier.
     * If the argument is declared with the <i>__constant</i> qualifier, the
     * size in bytes of the memory object cannot exceed
     * CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE and the number of arguments declared
     * with the <i>__constant</i> qualifier cannot exceed
     * CL_DEVICE_MAX_CONSTANT_ARGS.
     * </p>
     * <p>
     * The memory object specified as argument value must be a 2D image object
     * if the argument is declared to be of type <i>image2d_t</i>. The memory
     * object specified as argument value must be a 3D image object if argument
     * is declared to be of type <i>image3d_t</i>.
     * </p>
     * <p>
     * <i>arg_size</i> specifies the size of the argument value. If the
     * argument is a memory object, the size is the size of the buffer or image
     * object type. For arguments declared with the <i>__local</i> qualifier,
     * the size specified will be the size in bytes of the buffer that must be
     * allocated for the <i>__local </i>argument. If the argument is of type
     * <i>sampler_t</i>, the <i>arg_size</i> value must be equal to
     * sizeof(cl_sampler). For all other arguments, the size will be the size of
     * argument type.
     * </p>
     * <p>
     * NOTE: A kernel object does not update the reference count for objects
     * such as memory, sampler objects specified as argument values by
     * <b>clSetKernelArg</b>, Users may not rely on a kernel object to retain
     * objects specified as argument values to the kernel<sup>12</sup>.
     * </p>
     * <p>
     * <b>clSetKernelArg</b> returns CL_SUCCESS if the function was executed
     * successfully. Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * Implementations shall not allow cl_kernel objects to hold reference
     * counts to cl_kernel arguments, because no mechanism is provided for the
     * user to tell the kernel to release that ownership right. If the kernel
     * holds ownership rights on kernel args, that would make it impossible for
     * the user to tell with certainty when he may safely release user allocated
     * resources associated with OpenCL objects such as the cl_mem backing store
     * used with CL_MEM_USE_HOST_PTR.
     * </p>
     * <p>
     * CL_INVALID_KERNEL if <i>kernel</i> is not a valid kernel object.
     * </p>
     * <p>
     * CL_INVALID_ARG_INDEX if <i>arg_index</i> is not a valid argument index.
     * </p>
     * <p>
     * CL_INVALID_ARG_VALUE if <i>arg_value</i> specified is NULL for an
     * argument that is not declared with the <i>__local</i> qualifier or
     * vice-versa.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT for an argument declared to be a memory object when
     * the specified <i>arg_value</i> is not a valid memory object.
     * </p>
     * <p>
     * CL_INVALID_SAMPLER for an argument declared to be of type sampler_t when
     * the specified <i>arg_value</i> is not a valid sampler object.
     * </p>
     * <p>
     * CL_INVALID_ARG_SIZE if <i>arg_size</i> does not match the size of the
     * data type for an argument that is not a memory object or if the argument
     * is a memory object and <i>arg_size </i>!= sizeof(cl_mem)or if <i>arg_size</i>
     * is zero and the argument is declared with the <i>__local</i> qualifier or 
     * if the argument is a sampler and <i>arg_size</i> !=
     * sizeof(cl_sampler).
     * </p>
     */
    public static synchronized int clSetKernelArg(cl_kernel kernel, int arg_index, long arg_size, Pointer arg_value)
    {
        assertInit();
        return checkResult(clSetKernelArgNative(kernel, arg_index, arg_size, arg_value));
    }

    private static native int clSetKernelArgNative(cl_kernel kernel, int arg_index, long arg_size, Pointer arg_value);

    /**
     * Returns information about the kernel object.
     * <p>
     * cl_int <b>clGetKernelInfo</b> (cl_kernel <i>kernel</i>, cl_kernel_info
     * <i>param_name</i>, size_t <i>param_value_size</i>, void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>kernel</i> specifies the kernel object being queried.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetKernelInfo</b> is described in <i>table
     * 5.13</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.13</i>.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * copied to <i>param_value</i>. If <i>param_value_size_ret</i> is NULL,
     * it is ignored.
     * </p>
     * <table border="1"> 
     * <tr>
     * <th > <b>cl_kernel_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_FUNCTION_NAME </b> </td>
     * <td > char[] </td>
     * <td > Return the kernel function name. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_NUM_ARGS </b> </td>
     * <td > cl_uint </td>
     * <td > Return the number of arguments to <i>kernel</i>. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_REFERENCE_ COUNT</b><b>13 </b> </td>
     * <td > cl_uint </td>
     * <td > Return the <i>kernel</i> reference count. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_CONTEXT </b> </td>
     * <td > cl_context </td>
     * <td > Return the context associated with <i>kernel</i>. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_PROGRAM </b> </td>
     * <td > cl_program </td>
     * <td > Return the program object associated with <i>kernel</i>. </td>
     * </tr>
     * </table>
     * <p>
     * <b>clGetKernelInfo</b> returns CL_SUCCESS if the function is executed
     * successfully, returns CL_INVALID_VALUE if <i>param_name</i> is not
     * valid, or if size in bytes specified by <i>param_value_size</i> is &lt;
     * size of return type as described in <i>table 5.13 </i>and <i>param_value</i>
     * is not NULL, and returns CL_INVALID_KERNEL if <i>kernel</i> is a not a
     * valid kernel object.
     * </p>
     */
    public static synchronized int clGetKernelInfo(cl_kernel kernel, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetKernelInfoNative(kernel, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetKernelInfoNative(cl_kernel kernel, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Rreturns information about the kernel object that may be specific to a
     * device.
     * <p>
     * cl_int <b>clGetKernelWorkGroupInfo</b> (cl_kernel <i>kernel</i>,
     * cl_device_id <i>device</i>,
     * cl_kernel_work_group_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>kernel</i> specifies the kernel object being queried.
     * </p>
     * <p>
     * <i>device</i> identifies a specific device in the list of devices
     * associated with <i>kernel</i>. The list of devices is the list of
     * devices in the OpenCL context that is associated with <i>kernel</i>. If
     * the list of devices associated with <i>kernel</i> is a single device,
     * <i>device</i> can be a NULL value.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetKernelWorkGroupInfo</b> is described in
     * <i>table 5.14</i>.
     * </p>
     * <p align="left" >
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.14</i>.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * copied to <i>param_value</i>. If <i>param_value_size_ret</i> is NULL,
     * it is ignored.
     * </p>
     * <table border="1"> 
     * <tr>
     * <th > <b>cl_kernel_work_group_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_WORK_GROUP_SIZE </b> </td>
     * <td > size_t </td>
     * <td > This provides a mechanism for the application to query the
     * work-group size that can be used to execute a kernel on a specific device
     * given by <i>device</i>. The OpenCL implementation uses the resource
     * requirements of the kernel (register usage etc.) to determine what this
     * work-group size should be. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_COMPILE_ </b> </td>
     * <td > size_t[3] </td>
     * <td > Returns the work-group size specified </td>
     * </tr>
     * <tr>
     * <td > <b>WORK_GROUP_SIZE </b> </td>
     * <td > </td>
     * <td > by the __attribute__((reqd_work_gr oup_size(X, Y, Z)))qualifier.
     * Refer to <i>section 6.7.2</i>. If the work-group size is not specified
     * using the above attribute qualifier (0, 0, 0) is returned. </td>
     * </tr>
     * <tr>
     * <td > <b>CL_KERNEL_LOCAL_MEM_SIZE </b> </td>
     * <td > cl_ulong </td>
     * <td > Returns the amount of local memory in bytes being used by a kernel.
     * This includes local memory that may be needed by an implementation to
     * execute the kernel, variables declared inside the kernel with the
     * __localaddress qualifier and local memory to be allocated for arguments
     * to the kernel declared as pointers with the __local address qualifier and
     * whose size is specified with <b>clSetKernelArg</b>. If the local memory
     * size, for any pointer argument to the kernel declared with the
     * __localaddress qualifier, is not specified, its size is assumed to be 0.
     * </td>
     * </tr>
     * </table>
     * <p>
     * <b>clGetKernelWorkGroupInfo</b> returns CL_SUCCESS if the function is
     * executed successfully, returns CL_INVALID_DEVICE if <i>device</i> is not
     * in the list of devices associated with <i>kernel</i> or if <i>device</i>
     * is NULL but there is more than one device associated with <i>kernel</i>,
     * returns CL_INVALID_VALUE if <i>param_name</i> is not valid, or if size
     * in bytes specified by <i>param_value_size</i> is &lt; size of return
     * type as described in <i>table 5.14 </i>and <i>param_value</i> is not
     * NULL, and returns CL_INVALID_KERNEL if <i>kernel</i> is a not a valid
     * kernel object.
     * </p>
     */
    public static synchronized int clGetKernelWorkGroupInfo(cl_kernel kernel, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetKernelWorkGroupInfoNative(kernel, device, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetKernelWorkGroupInfoNative(cl_kernel kernel, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Waits on the host thread for commands.
     * <p>
     * cl_int <b>clWaitForEvents</b> (cl_uint <i>num_events</i>, const
     * cl_event *<i>event_list</i>)
     * </p>
     * <p>
     * waits on the host thread for commands identified by event objects in
     * <i>event_list</i> to complete. A command is considered complete if its
     * execution status is CL_COMPLETE or a negative value. The events specified
     * in <i>event_list</i> act as synchronization points.
     * </p>
     * <p>
     * <b>clWaitForEvents</b> returns CL_SUCCESS if the function was executed
     * successfully. It returns CL_INVALID_VALUE if num_events is zero, returns
     * CL_INVALID_CONTEXT if events specified in <i>event_list</i> do not
     * belong to the same context, and returns CL_INVALID_EVENT if event objects
     * specified in <i>event_list</i> are not valid event objects.
     * </p>
     */
    public static synchronized int clWaitForEvents(int num_events, cl_event event_list[])
    {
        assertInit();
        return checkResult(clWaitForEventsNative(num_events, event_list));
    }

    private static native int clWaitForEventsNative(int num_events, cl_event event_list[]);

    /**
     * Returns information about the event object.
     * <p align="justify" >
     * cl_int <b>clGetEventInfo</b> (cl_event <i>event</i>,
     * cl_event_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>event</i> specifies the event object being queried.
     * </p>
     * <p>
     * <i>param_name</i> specifies the information to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetEventInfo</b> is described in <i>table
     * 5.15</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.15</i>.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * copied to <i>param_value</i>. If <i>param_value_size_ret</i> is NULL,
     * it is ignored.
     * </p>
     * <table border="1">
     * <tr>
     * <th > <b>cl_event_info </b> </th>
     * <th > <b>Return Type </b> </th>
     * <th > <b>Info. returned in </b><b><i>param_value </i></b> </th>
     * </tr>
     * <tr>
     * <th> <b>CL_EVENT_COMMAND_ QUEUE </b> </th>
     * <td> cl_command_ queue </td>
     * <td> Return the command-queue associated with <i>event</i>.
     * </td>
     * </tr>
     * <tr>
     * <th> <b>CL_EVENT_COMMAND_ TYPE </b> </th>
     * <td> cl_command_type </td>
     * <td> Return the command associated with event. Can be one of
     * the following values: CL_COMMAND_NDRANGE_KERNEL CL_COMMAND_TASK
     * CL_COMMAND_NATIVE_KERNEL CL_COMMAND_READ_BUFFER CL_COMMAND_WRITE_BUFFER
     * CL_COMMAND_COPY_BUFFER CL_COMMAND_READ_IMAGE CL_COMMAND_WRITE_IMAGE
     * CL_COMMAND_COPY_IMAGE CL_COMMAND_COPY_BUFFER_TO_IMAGE
     * CL_COMMAND_COPY_IMAGE_TO_BUFFER CL_COMMAND_MAP_BUFFER
     * CL_COMMAND_MAP_IMAGE CL_COMMAND_UNMAP_MEM_OBJECT CL_COMMAND_MARKER
     * CL_COMMAND_ACQUIRE_GL_OBJECTS CL_COMMAND_RELEASE_GL_OBJECTS </td>
     * </tr>
     * <tr>
     * <th> <b>CL_EVENT_COMMAND_EXECUTION_STATUS</b> </th>
     * <td> cl_int </td>
     * <td> Return the execution status of the command identified by <i>event</i>. 
     * Valid values are: CL_QUEUED (command
     * has been enqueued in the command-queue), CL_SUBMITTED (enqueued command
     * has been submitted by the host to the device associated with the
     * command-queue), CL_RUNNING (device is currently executing this command),
     * CL_COMPLETE (the command has completed), or Error code given by a
     * negative integer value. (command was abnormally terminated
     * - this may be caused by a bad memory access etc.).
     * </td>
     * </tr>
     * <tr>
     * <td > <b>CL_EVENT_REFERENCE_ COUNT</b><b>15 </b> </td>
     * <td > cl_uint </td>
     * <td > Return the <i>event</i> reference count. </td>
     * </tr>
     * </table>
     * <p>
     * Using <b>clGetEventInfo</b> to determine if a command identified by
     * <i>event</i> has finished execution (i.e.
     * CL_EVENT_COMMAND_EXECUTION_STATUS returns CL_COMPLETE) is not a
     * synchronization point. There are no guarantees that the memory objects
     * being modified by command associated with <i>event</i> will be visible
     * to other enqueued commands.
     * </p>
     * <p>
     * <b>clGetEventInfo</b> returns CL_SUCCESS if the function is executed
     * successfully, returns CL_INVALID_VALUE if <i>param_name</i> is not
     * valid, or if size in bytes specified by <i>param_value_size</i> is &lt;
     * size of return type as described in <i>table 5.15</i> and <i>param_value</i>
     * is not NULL, and returns CL_INVALID_EVENT if <i>event</i> is a not a
     * valid event object.
     * </p>
     */
    public static synchronized int clGetEventInfo(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetEventInfoNative(event, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetEventInfoNative(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Increments the <i>event</i> reference count.
     * <p>
     * cl_int <b>clRetainEvent</b> (cl_event <i>event</i>)
     * </p>
     * <p>
     * <b>clRetainEvent</b>
     * returns CL_SUCCESS if the function is executed successfully. It returns
     * CL_INVALID_EVENT if <i>event</i> is not a valid event object. The OpenCL
     * commands that return an event perform an implicit retain.
     * </p>
     * <p>
     * <sup>15</sup> The reference count returned should be considered
     * immediately stale. It is unsuitable for general use in applications. This
     * feature is provided for identifying memory leaks.
     * </p>
     * <p>
     * To release an event, use the following function
     * </p>
     */
    public static synchronized int clRetainEvent(cl_event event)
    {
        assertInit();
        return checkResult(clRetainEventNative(event));
    }

    private static native int clRetainEventNative(cl_event event);

    /**
     * Decrements the <i>event</i> reference count.
     * <p>
     * cl_int <b>clReleaseEvent</b> (cl_event <i>event</i>)
     * </p>
     * <p>
     *  <b>clReleaseEvent</b>
     * returns CL_SUCCESS if the function is executed successfully. It returns
     * CL_INVALID_EVENT if <i>event</i> is not a valid event object. The event
     * object is deleted once the reference count becomes zero, the specific
     * command identified by this event has completed (or terminated) and there
     * are no commands in the command-queues of a context that require a wait
     * for this event to complete.
     * </p>
     */
    public static synchronized int clReleaseEvent(cl_event event)
    {
        assertInit();
        return checkResult(clReleaseEventNative(event));
    }

    private static native int clReleaseEventNative(cl_event event);

    /**
     * Returns profiling information for the command associated with event if profiling is enabled.
     * <p align="left" >
     * cl_int <b>clGetEventProfilingInfo</b> (cl_event <i>event</i>,
     * cl_profiling_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void *<i>param_value</i>,
     * size_t *<i>param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>event</i> specifies the event object.
     * </p>
     * <p>
     * <i>param_name</i> specifies the profiling data to query. The list of
     * supported <i>param_name</i> types and the information returned in
     * <i>param_value</i> by <b>clGetEventProfilingInfo</b> is described in
     * <i>table 5.16</i>.
     * </p>
     * <p>
     * <i>param_value</i> is a pointer to memory where the appropriate result
     * being queried is returned. If <i>param_value</i> is NULL, it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i> is used to specify the size in bytes of memory
     * pointed to by <i>param_value</i>. This size must be &gt;= size of return
     * type as described in <i>table 5.16</i>.
     * </p>
     * <p>
     * <i>param_value_size_ret</i> returns the actual size in bytes of data
     * copied to <i>param_value</i>. If <i>param_value_size_ret</i> is NULL,
     * it is ignored.
     * </p>
     * <table border="1">
     * <tr>
     * <td>cl_profiling_info</td> <td>Return Type</td> <td>Info. returned in param_value</td>
     * </tr>
     * <tr>
     * <td>CL_PROFILING_COMMAND_QUEUED</td> <td>cl_ulong</td> <td>A 64-bit value that describes the current
     * device time counter in nanoseconds when
     * the command identified by event is
     * enqueued in a command-queue by the
     * host.</td>
     * </tr>
     * <tr>
     * <td>CL_PROFILING_COMMAND_SUBMIT</td> <td>cl_ulong</td> <td>A 64-bit value that describes the current
     * device time counter in nanoseconds when
     * the command identified by event that has
     * been enqueued is submitted by the host to
     * the device associated with the commandqueue.</td>
     * </tr>
     * <tr>
     * <td>CL_PROFILING_COMMAND_START</td> <td>cl_ulong</td> <td>A 64-bit value that describes the current
     * device time counter in nanoseconds when
     * the command identified by event starts
     * execution on the device.</td>
     * </tr>
     * <tr>
     * <td>CL_PROFILING_COMMAND_END</td> <td>cl_ulong</td> <td>A 64-bit value that describes the current
     * device time counter in nanoseconds when
     * the command identified by event has
     * finished execution on the device</td>
     * </tr>
     * </table>  
     * <p>
     * The unsigned 64-bit values returned can be used to measure the time in
     * nano-seconds consumed by OpenCL commands.
     * </p>
     * <p>
     * OpenCL devices are required to correctly track time across changes in
     * device frequency and power states. The
     * CL_DEVICE_PROFILING_TIMER_RESOLUTION specifies the resolution of the
     * timer i.e. the number of nanoseconds elapsed before the timer is
     * incremented.
     * </p>
     * <p>
     * <b>clGetEventProfilingInfo</b> returns CL_SUCCESS if the function is
     * executed successfully and the profiling information has been recorded,
     * returns CL_PROFILING_INFO_NOT_AVAILABLE if the CL_QUEUE_PROFILING_ENABLE
     * flag is not set for the command-queue and if the profiling information is
     * currently not available (because the command identified by <i>event</i>
     * has not completed), returns CL_INVALID_VALUE if <i>param_name</i> is not
     * valid, or if size in bytes specified by <i>param_value_size</i> is &lt;
     * size of return type as described in <i>table 5.16</i> and <i>param_value</i>
     * is not NULL, and returns CL_INVALID_EVENT if <i>event</i> is a not a
     * valid event object.
     * </p>
     */
    public static synchronized int clGetEventProfilingInfo(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetEventProfilingInfoNative(event, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetEventProfilingInfoNative(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Issues all previously queued OpenCL commands.
     * <p>
     * cl_int <b>clFlush</b> (cl_command_queue <i>command_queue</i>)
     * </p>
     * <p>
     * issues all previously queued OpenCL commands in <i>command_queue </i>to
     * the device associated with<i> command_queue</i>. <b>clFlush</b> only
     * guarantees that all queued commands to <i>command_queue </i>get issued to
     * the appropriate device. There is no guarantee that they will be complete
     * after <b>clFlush</b> returns.
     * </p>
     * <p>
     * <b>clFlush</b> returns CL_SUCCESS if the function call was executed
     * successfully. It returns CL_INVALID_COMMAND_QUEUE if <i>command_queue</i>
     * is not a valid command-queue and returns CL_OUT_OF_HOST_MEMORY if there
     * is a failure to allocate resources required by the OpenCL implementation
     * on the host.
     * </p>
     * <p>
     * Any blocking commands queued in a command-queue such as
     * <b>clEnqueueRead{Image|Buffer} </b>with <i>blocking_read</i> set to
     * CL_TRUE, <b>clEnqueueWrite{Image|Buffer}</b> with <i>blocking_write</i>
     * set to CL_TRUE, <b>clEnqueueMap{Buffer|Image}</b> with <i>blocking_map</i>
     * set to CL_TRUE or <b>clWaitForEvents</b> perform an implicit flush of
     * the command-queue.
     * </p>
     * <p>
     * To use event objects that refer to commands enqueued in a command-queue
     * as event objects to wait on by commands enqueued in a different
     * command-queue, the application must call a <b>clFlush</b> or any
     * blocking commands that perform an implicit flush of the command-queue
     * where the commands that refer to these event objects are enqueued.
     * </p>
     */
    public static synchronized int clFlush(cl_command_queue command_queue)
    {
        assertInit();
        return checkResult(clFlushNative(command_queue));
    }

    private static native int clFlushNative(cl_command_queue command_queue);

    /**
     * Blocks until all previously queued OpenCL commands have completed.
     * <p>
     * cl_int <b>clFinish</b> (cl_command_queue <i>command_queue</i>)
     * </p>
     * <p>
     * blocks until all previously queued OpenCL commands in <i>command_queue</i>
     * are issued to the associated device and have completed. <b>clFinish</b>
     * does not return until all queued commands in <i>command_queue</i> have
     * been processed and completed. <b>clFinish</b> is also a synchronization
     * point.
     * </p>
     * <p>
     * <b>clFinish</b> returns CL_SUCCESS if the function call was executed
     * successfully. It returns CL_INVALID_COMMAND_QUEUE if <i>command_queue</i>
     * is not a valid command-queue and returns CL_OUT_OF_HOST_MEMORY if there
     * is a failure to allocate resources required by the OpenCL implementation
     * on the host.
     * </p>
     */
    public static synchronized int clFinish(cl_command_queue command_queue)
    {
        assertInit();
        return checkResult(clFinishNative(command_queue));
    }

    private static native int clFinishNative(cl_command_queue command_queue);

    /**
     * Enqueue commands to read from a buffer object to host memory.
     * 
     * </p>
     * <p>
     * cl_int <b>clEnqueueReadBuffer</b> (cl_command_queue <i>command_queue</i>,
     * cl_mem <i>buffer</i>, cl_bool <i>blocking_read</i>, size_t <i>offset</i>,
     * size_t <i>cb</i>, void *<i>ptr</i>, cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event *<i>event_wait_list</i>, cl_event *<i>event</i>)
     * </p>
     * 
     * @see CL#clEnqueueWriteBuffer(cl_command_queue, cl_mem, boolean, long, long, Pointer, int, cl_event[], cl_event)
     */
    public static synchronized int clEnqueueReadBuffer(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueReadBufferNative(command_queue, buffer, blocking_read, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueReadBufferNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueue commands to write from a buffer object from host memory.
     * <p>
     * cl_int <b>clEnqueueWriteBuffer</b> (cl_command_queue <i>command_queue</i>,
     * cl_mem <i>buffer</i>, cl_bool <i>blocking_write</i>, size_t <i>offset</i>,
     * size_t <i>cb</i>, const void *<i>ptr</i>, cl_uint
     * <i>num_events_in_wait_list</i>, const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * <i>command_queue</i> refers to the command-queue in which the read /
     * write command will be queued. <i>command_queue</i> and <i>buffer</i>
     * must be created with the same OpenCL context.
     * </p>
     * <p>
     * <i>buffer</i> refers to a valid buffer object.
     * </p>
     * <p>
     * <i>blocking_read</i> and <i>blocking_write</i> indicate if the read and
     * write operations are <i>blocking</i> or <i>non</i><i>blocking</i>.
     * </p>
     * <p>
     * If <i>blocking_read</i> is CL_TRUE i.e. the read command is blocking,
     * <b>clEnqueueReadBuffer</b> does not return until the buffer data has
     * been read and copied into memory pointed to by <i>ptr</i>.
     * </p>
     * <p>
     * If <i>blocking_read</i> is CL_FALSE i.e. the read command is
     * non-blocking, <b>clEnqueueReadBuffer </b>queues a non-blocking read
     * command and returns. The contents of the buffer that <i>ptr</i> points
     * to cannot be used until the read command has completed. The <i>event</i>
     * argument returns an event object which can be used to query the execution
     * status of the read command. When the read command has completed, the
     * contents of the buffer that <i>ptr </i>points to can be used by the
     * application.
     * </p>
     * <p>
     * If <i>blocking_write </i>is CL_TRUE, the OpenCL implementation copies the
     * data referred to by <i>ptr </i>and enqueues the write operation in the
     * command-queue. The memory pointed to by <i>ptr</i> can be reused by the
     * application after the <b>clEnqueueWriteBuffer</b> call returns.
     * </p>
     * <p>
     * If <i>blocking_write</i> is CL_FALSE, the OpenCL implementation will use
     * <i>ptr</i> to perform a nonblocking write. As the write is non-blocking
     * the implementation can return immediately. The memory pointed to by
     * <i>ptr</i> cannot be reused by the application after the call returns.
     * The <i>event </i>argument returns an event object which can be used to
     * query the execution status of the write command. When the write command
     * has completed, the memory pointed to by <i>ptr</i> can then be reused by
     * the application.
     * </p>
     * <p>
     * <i>offset</i> is the offset in bytes in the buffer object to read from
     * or write to.
     * </p>
     * <p>
     * <i>cb</i> is the size in bytes of data being read or written.
     * </p>
     * <p>
     * <i>ptr</i> is the pointer to buffer in host memory where data is to be
     * read into or to be written from.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * read / write command and can be used to query or queue a wait for this
     * particular command to complete. <i>event</i> can be NULL in which case
     * it will not be possible for the application to query the status of this
     * command or queue a wait for this command to complete.
     * </p>
     * <p>
     * <b>clEnqueueReadBuffer</b> and <b>clEnqueueWriteBuffer</b> return
     * CL_SUCCESS if the function is executed successfully. Otherwise, it
     * returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if the context associated with <i>command_queue</i>
     * and <i>buffer</i> are not the same or if the context associated with
     * <i>command_queue</i> and events in <i>event_wait_list</i> are not the
     * same.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>buffer</i> is not a valid buffer object.
     * </p>
     * <p>
     * CL_INVALID_VALUE if the region being read or written specified by (<i>offset</i>,
     * <i>cb</i>) is out of bounds or if <i>ptr</i> is a NULL value.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with <i>buffer</i>.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * NOTE:
     * </p>
     * <p align="left" >
     * Calling <b>clEnqueueReadBuffer</b> to read a region of the buffer object
     * with the <i>ptr</i> argument value set to <i>host_ptr</i> + <i>offset</i>,
     * where <i>host_ptr</i> is a pointer to the memory region specified when
     * the buffer object being read is created with CL_MEM_USE_HOST_PTR, must
     * meet the following requirements in order to avoid undefined behavior:
     * </p>
     * <ul >
     * <li > All commands that use this buffer object have finished execution
     * before the read command begins execution. </li>
     * <li > The buffer object is not mapped. </li>
     * <li > The buffer object is not used by any command-queue until the read
     * command has finished execution. </li>
     * </ul>
     * <p>
     * Calling<b> clEnqueueWriteBuffer</b> to update the latest bits in a
     * region of the buffer object with the <i>ptr</i> argument value set to
     * <i>host_ptr</i> + <i>offset</i>, where <i>host_ptr</i> is a pointer to
     * the memory region specified when the buffer object being written is
     * created with CL_MEM_USE_HOST_PTR, must meet the following requirements in
     * order to avoid undefined behavior:
     * </p>
     * <ul >
     * <li > The host memory region given by (<i>host_ptr</i> + <i>offset</i>,
     * <i>cb</i>) contains the latest bits when the enqueued write command
     * begins execution. </li>
     * <li > The buffer object is not mapped. </li>
     * <li > The buffer object is not used by any command-queue until the write
     * command has finished execution. </li>
     * </ul>
     */
    public static synchronized int clEnqueueWriteBuffer(cl_command_queue command_queue, cl_mem buffer, boolean blocking_write, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueWriteBufferNative(command_queue, buffer, blocking_write, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueWriteBufferNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_write, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to copy a buffer object.
     * <p>
     * cl_int <b>clEnqueueCopyBuffer</b> (cl_command_queue <i>command_queue</i>,
     * cl_mem <i>src_buffer</i>,
     * cl_mem <i>dst_buffer</i>,
     * size_t <i>src_offset</i>,
     * size_t <i>dst_offset</i>,
     * size_t <i>cb</i>,
     * cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * enqueues a command to copy a buffer object identified by <i>src_buffer
     * </i>to another buffer object identified by <i>dst_buffer</i>.
     * </p>
     * <p>
     * <i>command_queue</i> refers to the command-queue in which the copy
     * command will be queued. The OpenCL context associated with
     * <i>command_queue</i>, <i>src_buffer</i> and <i>dst_buffer</i> must be
     * the same.
     * </p>
     * <p>
     * <i>src_offset</i> refers to the offset where to begin copying data from
     * <i>src_buffer</i>.
     * </p>
     * <p>
     * <i>dst_offset</i> refers to the offset where to begin copying data into
     * <i>dst_buffer</i>.
     * </p>
     * <p>
     * <i>cb</i> refers to the size in bytes to copy.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * copy command and can be used to query or queue a wait for this particular
     * command to complete. <i>event</i> can be NULL in which case it will not
     * be possible for the application to query the status of this command or
     * queue a wait for this command to complete. <b>clEnqueueBarrier</b> can
     * be used instead.
     * </p>
     * <p>
     * <b>clEnqueueCopyBuffer</b> returns CL_SUCCESS if the function is
     * executed successfully. Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if the context associated with <i>command_queue</i>,
     * <i>src_buffer</i> and <i>dst_buffer</i> are not the same or if the
     * context associated with <i>command_queue</i> and events in
     * <i>event_wait_list</i> are not the same.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>src_buffer</i> and <i>dst_buffer</i> are
     * not valid buffer objects.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>src_offset</i>, <i>dst_offset</i>, <i>cb</i>,
     * <i>src_offset</i> + <i>cb</i> or <i>dst_offset</i> + <i>cb </i>require
     * accessing elements outside the buffer memory objects.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_MEM_COPY_OVERLAP if <i>src_buffer</i> and <i>dst_buffer</i> are the
     * same buffer object and the source and destination regions overlap.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with <i>src_buffer</i> or <i>dst_buffer</i>.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized int clEnqueueCopyBuffer(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_buffer, long src_offset, long dst_offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueCopyBufferNative(command_queue, src_buffer, dst_buffer, src_offset, dst_offset, cb, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyBufferNative(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_buffer, long src_offset, long dst_offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueue commands to read from a 2D or 3D image object to host memory.
     * 
     * <p>
     * cl_int <b>clEnqueueReadImage</b> (cl_command_queue <i>command_queue</i>,
     * cl_mem <i>image</i>, cl_bool <i>blocking_read</i>, const size_t
     * <i>origin</i>[3], const size_t <i>region</i>[3], size_t <i>row_pitch</i>,
     * size_t <i>slice_pitch</i>, void *<i>ptr</i><i>, </i>cl_uint
     * <i>num_events_in_wait_list</i>, const cl_event *<i>event_wait_list</i>,
     * cl_event <i>*event</i>)
     * </p>
     * 
     * @see CL#clEnqueueWriteImage(cl_command_queue, cl_mem, boolean, long[], long[], long, long, Pointer, int, cl_event[], cl_event)
     */
    public static synchronized int clEnqueueReadImage(cl_command_queue command_queue, cl_mem image, boolean blocking_read, long origin[], long region[], long row_pitch, long slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueReadImageNative(command_queue, image, blocking_read, origin, region, row_pitch, slice_pitch, ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueReadImageNative(cl_command_queue command_queue, cl_mem image, boolean blocking_read, long origin[], long region[], long row_pitch, long slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueue commands to write to a 2D or 3D image object from host memory.
     * <p>
     * cl_int <b>clEnqueueWriteImage</b> (cl_command_queue <i>command_queue</i>,
     * cl_mem <i>image</i>, cl_bool <i>blocking_write</i>, const size_t
     * <i>origin</i>[3], const size_t <i>region</i>[3], size_t
     * <i>input_row_pitch</i>, size_t <i>input_slice_pitch</i>, const void *<i>
     * ptr</i>, cl_uint <i>num_events_in_wait_list</i>, const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * <i>command_queue</i> refers to the command-queue in which the read /
     * write command will be queued. <i>command_queue</i> and <i>image</i>
     * must be created with the same OpenCL context.
     * </p>
     * <p>
     * <i>image</i> refers to a valid 2D or 3D image object.
     * </p>
     * <p>
     * <i>blocking_read</i> and <i>blocking_write</i> indicate if the read and
     * write operations are <i>blocking</i> or <i>non</i><i>blocking</i>.
     * </p>
     * <p>
     * If <i>blocking_read</i> is CL_TRUE i.e. the read command is blocking,
     * <b>clEnqueueReadImage</b> does not return until the buffer data has been
     * read and copied into memory pointed to by <i>ptr</i>.
     * </p>
     * <p>
     * If <i>blocking_read</i> is CL_FALSE i.e. the read command is
     * non-blocking, <b>clEnqueueReadImage </b>queues a non-blocking read
     * command and returns. The contents of the buffer that <i>ptr</i> points
     * to cannot be used until the read command has completed. The <i>event</i>
     * argument returns an event object which can be used to query the execution
     * status of the read command. When the read command has completed, the
     * contents of the buffer that <i>ptr </i>points to can be used by the
     * application.
     * </p>
     * <p>
     * If <i>blocking_write </i>is CL_TRUE, the OpenCL implementation copies the
     * data referred to by <i>ptr </i>and enqueues the write command in the
     * command-queue. The memory pointed to by <i>ptr</i> can be reused by the
     * application after the <b>clEnqueueWriteImage</b> call returns.
     * </p>
     * <p>
     * If <i>blocking_write</i> is CL_FALSE, the OpenCL implementation will use
     * <i>ptr</i> to perform a nonblocking write. As the write is non-blocking
     * the implementation can return immediately. The memory pointed to by
     * <i>ptr</i> cannot be reused by the application after the call returns.
     * The <i>event </i>argument returns an event object which can be used to
     * query the execution status of the write command. When the write command
     * has completed, the memory pointed to by <i>ptr</i> can then be reused by
     * the application.
     * </p>
     * <p>
     * <i>origin</i> defines the (<i>x</i>, <i>y</i>, <i>z</i>) offset in
     * pixels in the image from where to read or write. If <i>image</i> is a 2D
     * image object, the <i>z</i> value given by <i>origin</i>[2] must be 0.
     * </p>
     * <p>
     * <i>region </i>defines the (<i>width</i>, <i>height, depth</i>) in
     * pixels of the 2D or 3D rectangle being read or written. If <i>image</i>
     * is a 2D image object, the <i>depth</i> value given by <i>region</i>[2]
     * must be 1.
     * </p>
     * <p>
     * <i>row_pitch</i> in <b>clEnqueueReadImage</b> and <i>input_row_pitch</i>
     * in <b>clEnqueueWriteImage</b> is the length of each row in bytes. This
     * value must be greater than or equal to the element size in bytes
     * * <i>width</i>. If <i>row_pitch</i> (or <i>input_row_pitch</i>) is
     * set to 0, the appropriate row pitch is calculated based on the size of
     * each element in bytes multiplied by <i>width</i>.
     * </p>
     * <p>
     * Calling <b>clEnqueueReadImage</b> to read a region of the image object
     * with the <i>ptr</i> argument value set to <i>host_ptr</i> + (<i>origin[2]
     * </i>*<i> image slice pitch + origin[1] </i>*<i> image row pitch +
     * origin[0] </i>* <i>bytes per pixel</i>)<i>,</i> where <i>host_ptr</i>
     * is a pointer to the memory region specified when the image object being
     * read is created with CL_MEM_USE_HOST_PTR, must meet the following
     * requirements in order to avoid undefined behavior:
     * </p>
     * <ul >
     * <li > All commands that use this image object have finished execution
     * before the read command begins execution. </li>
     * <li > The <i>row_pitch</i> and <i>slice_pitch</i> argument values in
     * <b>clEnqueueReadImage</b> must be set to the image row pitch and slice
     * pitch. </li>
     * <li > The image object is not mapped. </li>
     * <li > The image object is not used by any command-queue until the read
     * command has finished execution. </li>
     * </ul>
     * <p>
     * Calling<b> clEnqueueWriteImage</b> to update the latest bits in a
     * region of the image object with the <i>ptr</i> argument value set to
     * <i>host_ptr</i> + (<i>origin[2] </i>*<i> image slice pitch + origin[1]
     * </i>*<i> image row pitch + origin[0] </i>*<i> bytes per pixel</i>),
     * where <i>host_ptr</i> is a pointer to the memory region specified when
     * the image object being written is created with CL_MEM_USE_HOST_PTR, must
     * meet the following requirements in order to avoid undefined behavior:
     * </p>
     * <ul >
     * <li > The host memory region being written contains the latest bits when
     * the enqueued write command begins execution. </li>
     * <li > The <i>input_row_pitch</i> and <i>input_slice_pitch</i> argument
     * values in <b>clEnqueueWriteImage </b>must be set to the image row pitch
     * and slice pitch. </li>
     * <li > The image object is not mapped. </li>
     * <li > The image object is not used by any command-queue until the write
     * command has finished execution. </li>
     * </ul>
     * 
     */
    public static synchronized int clEnqueueWriteImage(cl_command_queue command_queue, cl_mem image, boolean blocking_write, long origin[], long region[], long input_row_pitch, long input_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueWriteImageNative(command_queue, image, blocking_write, origin, region, input_row_pitch, input_slice_pitch, ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueWriteImageNative(cl_command_queue command_queue, cl_mem image, boolean blocking_write, long origin[], long region[], long input_row_pitch, long input_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to copy image objects. 
     * <p align="left" >
     * cl_int <b>clEnqueueCopyImage</b> (cl_command_queue <i>command_queue</i>,
     * cl_mem <i>src_image</i>, cl_mem <i>dst_image</i>, const size_t
     * <i>src_origin</i>[3], const size_t <i>dst_origin</i>[3], const size_t
     * <i>region</i>[3], cl_uint <i>num_events_in_wait_list</i>, const
     * cl_event *<i>event_wait_list</i>, cl_event *<i>event</i>)
     * </p>
     * <p>
     * <i>src_image</i> and
     * <i>dst_image</i> can be 2D or 3D image objects allowing us to perform
     * the following actions:
     * </p>
     * <p>
     * Copy a 2D image object to a 2D image object. Copy a 2D image object to a
     * 2D slice of a 3D image object. Copy a 2D slice of a 3D image object to a
     * 2D image object. Copy a 3D image object to a 3D image object.
     * <i>command_queue</i> refers to the command-queue in which the copy
     * command will be queued. The OpenCL context associated with
     * <i>command_queue</i>, <i>src_image</i> and <i>dst_image</i> must be
     * the same.
     * </p>
     * <p>
     * <i>src_origin</i> defines the starting (<i>x</i>, <i>y</i>, <i>z</i>)
     * location in pixels in <i>src_image</i> from where to start the data
     * copy. If <i>src_image</i> is a 2D image object, the <i>z</i> value
     * given by <i>src_origin</i>[2] must be 0.
     * </p>
     * <p>
     * <i>dst_origin</i> defines the starting (<i>x</i>, <i>y</i>, <i>z</i>)
     * location in pixels in <i>dst_image</i> from where to start the data
     * copy. If <i>dst_image</i> is a 2D image object, the <i>z</i> value
     * given by <i>dst_origin</i>[2] must be 0.
     * </p>
     * <p>
     * <i>region </i>defines the (<i>width</i>, <i>height, depth</i>) in
     * pixels of the 2D or 3D rectangle to copy. If <i>src_image</i> or
     * <i>dst_image</i> is a 2D image object, the <i>depth</i> value given by
     * <i>region</i>[2] must be 1.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * copy command and can be used to query or queue a wait for this particular
     * command to complete. <i>event</i> can be NULL in which case it will not
     * be possible for the application to query the status of this command or
     * queue a wait for this command to complete. <b>clEnqueueBarrier</b> can
     * be used instead.
     * </p>
     * <p>
     * It is currently a requirement that the <i>src_image</i> and <i>dst_image</i>
     * image memory objects for <b>clEnqueueCopyImage</b> must have the exact
     * same image format (i.e. the cl_image_format descriptor specified when
     * <i>src_image</i> and <i>dst_image</i> are created must match).
     * </p>
     * <p>
     * <b>clEnqueueCopyImage</b> returns CL_SUCCESS if the function is executed
     * successfully. Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if the context associated with <i>command_queue</i>,
     * <i>src_image</i> and <i>dst_image</i> are not the same or if the
     * context associated with <i>command_queue</i> and events in
     * <i>event_wait_list</i> are not the same.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>src_image</i> and <i>dst_image</i> are not
     * valid image objects.
     * </p>
     * <p>
     * CL_IMAGE_FORMAT_MISMATCH if <i>src_image</i> and <i>dst_image</i> do
     * not use the same image format.
     * </p>
     * <p>
     * CL_INVALID_VALUE if the 2D or 3D rectangular region specified by
     * <i>src_origin</i> and <i>src_origin</i> + <i>region</i> refers to a
     * region outside <i>src_image</i>, or if the 2D or 3D rectangular region
     * specified by <i>dst_origin</i> and <i>dst_origin</i> + <i>region</i>
     * refers to a region outside <i>dst_image</i>.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>src_image</i> is a 2D image object and <i>origin</i>[2]
     * is not equal to 0 or region[2] is not equal to 1.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>dst_image</i> is a 2D image object and
     * <i>dst_origin</i>[2] is not equal to 0 or <i>region</i>[2] is not equal
     * to 1.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with <i>src_image</i> or <i>dst_image</i>.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * CL_MEM_COPY_OVERLAP if <i>src_image</i> and <i>dst_image</i> are the
     * same image object and the source and destination regions overlap.
     * </p>
     */
    public static synchronized int clEnqueueCopyImage(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_image, long src_origin[], long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueCopyImageNative(command_queue, src_image, dst_image, src_origin, dst_origin, region, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyImageNative(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_image, long src_origin[], long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to copy an image object to a buffer object.
     * <p>
     * cl_int <b>clEnqueueCopyImageToBuffer</b> (cl_command_queue
     * <i>command_queue</i>, cl_mem <i>src_image</i>, cl_mem <i>dst_buffer</i>,
     * const size_t <i>src_origin</i>[3], const size_t <i>region</i>[3],
     * size_t <i>dst_offset</i>, cl_uint <i>num_events_in_wait_list</i>, const
     * cl_event *<i>event_wait_list</i>, cl_event *<i>event</i>)
     * </p>
     * <p>
     * <i>command_queue</i> must be a valid command-queue. The OpenCL context
     * associated with <i>command_queue</i>, <i>src_image</i> and
     * <i>dst_buffer</i> must be the same.
     * </p>
     * <p>
     * <i>src_image</i> is a valid image object.
     * </p>
     * <p>
     * <i>dst_buffer</i> is a valid buffer object.
     * </p>
     * <p>
     * <i>src_origin</i> defines the (<i>x</i>, <i>y</i>, <i>z</i>) offset
     * in pixels in the image from where to copy. If <i>src_image</i> is a 2D
     * image object, the <i>z</i> value given by <i>src_origin</i>[2] must be
     * 0.
     * </p>
     * <p>
     * <i>region </i>defines the (<i>width</i>, <i>height, depth</i>) in
     * pixels of the 2D or 3D rectangle to copy. If <i>src_image</i> is a 2D
     * image object, the <i>depth</i> value given by <i>region</i>[2] must be
     * 1.
     * </p>
     * <p>
     * <i>dst_offset</i> refers to the offset where to begin copying data into
     * <i>dst_buffer</i>. The size in bytes of the region to be copied referred
     * to as <i>dst_cb</i> is computed as <i>width</i> * <i>height</i> *
     * <i>depth</i> * <i>bytes/image </i><i>element</i> if <i>src_image</i>
     * is a 3D image object and is computed as <i>width</i> * <i>height</i> *
     * <i>bytes/image element</i> if <i>src_image</i> is a 2D image object.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * copy command and can be used to query or queue a wait for this particular
     * command to complete. <i>event</i> can be NULL in which case it will not
     * be possible for the application to query the status of this command or
     * queue a wait for this command to complete. <b>clEnqueueBarrier</b> can
     * be used instead.
     * </p>
     * <p>
     * <b>clEnqueueCopyImageToBuffer</b> returns CL_SUCCESS if the function is
     * executed successfully. Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if the context associated with <i>command_queue</i>,
     * <i>src_image</i> and <i>dst_buffer</i> are not the same or if the
     * context associated with <i>command_queue</i> and events in
     * <i>event_wait_list</i> are not the same.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>src_image</i> is not a valid image object or
     * <i>dst_buffer</i> is not a valid buffer object.
     * </p>
     * <p>
     * CL_INVALID_VALUE if the 2D or 3D rectangular region specified by
     * <i>src_origin</i> and <i>src_origin</i> + <i>region</i> refers to a
     * region outside <i>src_image</i>, or if the region specified by
     * <i>dst_offset</i> and <i>dst_offset</i> + <i>dst_cb</i> to a region
     * outside <i>dst_buffer</i>.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>src_image</i> is a 2D image object and
     * <i>src_origin</i>[2] is not equal to 0 or <i>region</i>[2] is not equal
     * to 1.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with <i>src_image</i> or <i>dst_buffer</i>.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * The function
     * </p>
     */
    public static synchronized int clEnqueueCopyImageToBuffer(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_buffer, long src_origin[], long region[], long dst_offset, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueCopyImageToBufferNative(command_queue, src_image, dst_buffer, src_origin, region, dst_offset, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyImageToBufferNative(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_buffer, long src_origin[], long region[], long dst_offset, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to copy a buffer object to an image object.
     * <p>
     * cl_int <b>clEnqueueCopyBufferToImage</b> (cl_command_queue
     * <i>command_queue</i>,
     * cl_mem <i>src_buffer</i>,
     * cl_mem <i>dst_image</i>,
     * size_t <i>src_offset</i>,
     * const size_t <i>dst_origin</i>[3],
     * const size_t <i>region</i>[3],
     * cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * <i>command_queue</i> must be a valid command-queue. The OpenCL context
     * associated with <i>command_queue</i>, <i>src_buffer</i> and
     * <i>dst_image</i> must be the same.
     * </p>
     * <p>
     * <i>src_buffer</i> is a valid buffer object.
     * </p>
     * <p>
     * <i>dst_image</i> is a valid image object.
     * </p>
     * <p>
     * <i>src_offset</i> refers to the offset where to begin copying data from
     * <i>src_buffer</i>.
     * </p>
     * <p>
     * <i>dst_origin</i> refers to the (<i>x</i>, <i>y</i>, <i>z</i>)
     * offset in pixels where to begin copying data to <i>dst_image</i>. If
     * <i>dst_image</i> is a 2D image object, the <i>z</i> value given by
     * <i>dst_origin</i>[2] must be 0.
     * </p>
     * <p>
     * <i>region </i>defines the (<i>width</i>, <i>height, depth</i>) in
     * pixels of the 2D or 3D rectangle to copy. If <i>dst_image </i>is a 2D
     * image object, the <i>depth</i> value given by <i>region</i>[2] must be
     * 1.
     * </p>
     * <p>
     * The size in bytes of the region to be copied from <i>src_buffer</i>
     * referred to as <i>src_cb</i> is computed as <i>width</i> * <i>height</i> *
     * <i>depth</i> * <i>bytes/image element</i> if <i>dst_image</i> is a 3D
     * image object and is computed as <i>width</i> * <i>height</i> *
     * <i>bytes/image element</i> if <i>dst_image</i> is a 2D image object.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * copy command and can be used to query or queue a wait for this particular
     * command to complete. <i>event</i> can be NULL in which case it will not
     * be possible for the application to query the status of this command or
     * queue a wait for this command to complete. <b>clEnqueueBarrier</b> can
     * be used instead.
     * </p>
     * <p>
     * <b>clEnqueueCopyBufferToImage</b> returns CL_SUCCESS if the function is
     * executed successfully. Otherwise, it returns one of the following errors:
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if the context associated with <i>command_queue</i>,
     * <i>src_buffer</i> and <i>dst_image</i> are not the same or if the
     * context associated with <i>command_queue</i> and events in
     * <i>event_wait_list</i> are not the same.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>src_buffer</i> is not a valid buffer object
     * or <i>dst_image</i> is not a valid image object.
     * </p>
     * <p>
     * CL_INVALID_VALUE if the 2D or 3D rectangular region specified by
     * <i>dst_origin</i> and <i>dst_origin</i> + <i>region</i> refer to a
     * region outside <i>dst_image</i>, or if the region specified by
     * <i>src_offset</i> and <i>src_offset</i> + <i>src_cb</i> refer to a
     * region outside <i>src_buffer</i>.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>dst_image</i> is a 2D image object and
     * <i>dst_origin</i>[2] is not equal to 0 or <i>region</i>[2] is not equal
     * to 1.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with <i>src_buffer</i> or <i>dst_image</i>.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized int clEnqueueCopyBufferToImage(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_image, long src_offset, long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueCopyBufferToImageNative(command_queue, src_buffer, dst_image, src_offset, dst_origin, region, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyBufferToImageNative(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_image, long src_offset, long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to map a region of the buffer object. 
     * <p align="left" >
     * void * <b>clEnqueueMapBuffer </b>(cl_command_queue <i>command_queue</i>,
     * cl_mem <i>buffer,</i>
     * cl_bool <i>blocking_map</i>,
     * cl_map_flags <i>map_flags</i>,
     * size_t <i>offset</i>,
     * size_t <i>cb</i>,
     * cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * enqueues a command to map a region of the buffer object given by
     * <i>buffer</i> into the host address space and returns a pointer to this
     * mapped region.
     * </p>
     * <p>
     * <i>command_queue</i> must be a valid command-queue.
     * </p>
     * <p>
     * <i>blocking_map</i> indicates if the map operation is <i>blocking</i>
     * or <i>non-blocking</i>.
     * </p>
     * <p>
     * If <i>blocking_map</i> is CL_TRUE, <b>clEnqueueMapBuffer</b> does not
     * return until the specified region in <i>buffer</i> can be mapped.
     * </p>
     * <p>
     * If <i>blocking_map</i> is CL_FALSE i.e. map operation is non-blocking,
     * the pointer to the mapped region returned by <b>clEnqueueMapBuffer</b>
     * cannot be used until the map command has completed. The <i>event</i>
     * argument returns an event object which can be used to query the execution
     * status of the map command. When the map command is completed, the
     * application can access the contents of the mapped region using the
     * pointer returned by <b>clEnqueueMapBuffer</b>.
     * </p>
     * <p>
     * <i>map_flags</i> is a bit-field and can be set to CL_MAP_READ to
     * indicate that the region specified by (<i>offset</i>, <i>cb</i>) in
     * the buffer object is being mapped for reading, and/or CL_MAP_WRITE to
     * indicate that the region specified by (<i>offset</i>, <i>cb</i>) in
     * the buffer object is being mapped for writing.
     * </p>
     * <p>
     * <i>buffer</i> is a valid buffer object. The OpenCL context associated
     * with <i>command_queue</i> and <i>buffer </i>must be the same.
     * </p>
     * <p>
     * <i>offset</i> and <i>cb</i> are the offset in bytes and the size of the
     * region in the buffer object that is being mapped.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * command and can be used to query or queue a wait for this particular
     * command to complete. <i>event</i> can be NULL in which case it will not
     * be possible for the application to query the status of this command or
     * queue a wait for this command to complete.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clEnqueueMapBuffer </b>will return a pointer to the mapped region if
     * the function is executed successfully. The <i>errcode_ret</i> is set to
     * CL_SUCCESS.
     * </p>
     * <p>
     * A NULL pointer is returned otherwise with one of the following error
     * values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue </i>is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if context associated with <i>command_queue </i>and
     * <i>buffer</i> are not the same or if the context associated with
     * <i>command_queue</i> and events in <i>event_wait_list </i>are not the
     * same.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>buffer</i> is not a valid buffer object.
     * </p>
     * <p>
     * CL_INVALID_VALUE if region being mapped given by (<i>offset</i>, <i>cb</i>)
     * is out of bounds or if values specified in <i>map_flags </i>are not
     * valid.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_MAP_FAILURE if there is a failure to map the requested region into the
     * host address space. This error cannot occur for buffer objects created
     * with CL_MEM_USE_HOST_PTR or CL_MEM_ALLOC_HOST_PTR.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with <i>buffer</i>.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * The pointer returned maps a region starting at <i>offset</i> and is
     * atleast <i>cb</i> bytes in size. The result of a memory access outside
     * this region is undefined.
     * </p>
     */
    public static synchronized ByteBuffer clEnqueueMapBuffer(cl_command_queue command_queue, cl_mem buffer, boolean blocking_map, long map_flags, long offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        ByteBuffer result = clEnqueueMapBufferNative(command_queue, buffer, blocking_map, map_flags, offset, cb, num_events_in_wait_list, event_wait_list, event, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native ByteBuffer clEnqueueMapBufferNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_map, long map_flags, long offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[]);

    /**
     * Enqueues a command to map a region in the image object.
     * <p align="left" >
     * void * <b>clEnqueueMapImage </b>(cl_command_queue <i>command_queue</i>,
     * cl_mem <i>image, </i>
     * cl_bool <i>blocking_map</i>,
     * cl_map_flags <i>map_flags</i>,
     * const size_t <i>origin</i>[3],
     * const size_t <i>region</i>[3],
     * size_t *<i>image_row_pitch</i>,
     * size_t *<i>image_slice_pitch</i>,
     * cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * enqueues a command to map a region in the image object given by
     * <i>image</i> into the host address space and returns a pointer to
     * this mapped region.
     * </p>
     * <p>
     * <i>command_queue</i> must be a valid command-queue.
     * </p>
     * <p>
     * <i>image</i> is a valid image object. The OpenCL context associated
     * with <i>command_queue</i> and <i>image </i>must be the same.
     * </p>
     * <p>
     * <i>blocking_map</i> indicates if the map operation is <i>blocking</i>
     * or <i>non-blocking</i>.
     * </p>
     * <p>
     * If <i>blocking_map</i> is CL_TRUE, <b>clEnqueueMapImage</b> does
     * not return until the specified region in <i>image</i> is mapped.
     * </p>
     * <p>
     * If <i>blocking_map</i> is CL_FALSE i.e. map operation is
     * non-blocking, the pointer to the mapped region returned by
     * <b>clEnqueueMapImage</b> cannot be used until the map command has
     * completed. The <i>event</i> argument returns an event object which
     * can be used to query the execution status of the map command. When
     * the map command is completed, the application can access the contents
     * of the mapped region using the pointer returned by
     * <b>clEnqueueMapImage</b>.
     * </p>
     * <p>
     * <i>map_flags</i> is a bit-field and can be set to CL_MAP_READ to
     * indicate that the region specified by (<i>origin</i>, <i>region</i>)
     * in the image object is being mapped for reading, and/or CL_MAP_WRITE
     * to indicate that the region specified by (<i>origin</i>, <i>region</i>)
     * in the image object is being mapped for writing.
     * </p>
     * <p>
     * <i>origin</i> and <i>region</i> define the (<i>x</i>, <i>y</i>,
     * <i>z</i>) offset in pixels and (<i>width</i>, <i>height, depth</i>)
     * in pixels of the 2D or 3D rectangle region that is to be mapped. If
     * <i>image</i> is a 2D image object, the <i>z</i> value given by
     * <i>origin</i>[2] must be 0 and the <i>depth</i> value given by
     * <i>region</i>[2] must be 1.
     * </p>
     * <p>
     * <i>image_row_pitch</i> returns the scan-line pitch in bytes for the
     * mapped region. This must be a non-NULL value.
     * </p>
     * <p>
     * <i>image_slice_pitch</i> returns the size in bytes of each 2D slice
     * for the mapped region. For a 2D image, zero is returned if this
     * argument is not NULL. For a 3D image, <i>image_slice_pitch</i> must
     * be a non-NULL value.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before <b>clEnqueueMapImage</b> can be
     * executed. If <i>event_wait_list</i> is NULL, then
     * <b>clEnqueueMapImage </b>does not wait on any event to complete. If
     * <i>event_wait_list</i> is NULL, <i>num_events_in_wait_list </i>must
     * be 0. If <i>event_wait_list</i> is not NULL, the list of events
     * pointed to by <i>event_wait_list</i> must be valid and
     * <i>num_events_in_wait_list</i> must be greater than 0. The events
     * specified in <i>event_wait_list</i> act as synchronization points.
     * The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * command and can be used to query or queue a wait for this particular
     * command to complete. <i>event</i> can be NULL in which case it will
     * not be possible for the application to query the status of this
     * command or queue a wait for this command to complete.
     * </p>
     * <p>
     * <i>errcode_ret</i> will return an appropriate error code. If
     * <i>errcode_ret</i> is NULL, no error code is returned.
     * </p>
     * <p>
     * <b>clEnqueueMapImage </b>will return a pointer to the mapped region
     * if the function is executed successfully. The <i>errcode_ret</i> is
     * set to CL_SUCCESS.
     * </p>
     * <p>
     * A NULL pointer is returned otherwise with one of the following error
     * values returned in <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue </i>is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if context associated with <i>command_queue
     * </i>and <i>image</i> are not the same or if the context associated
     * with <i>command_queue</i> and events in <i>event_wait_list </i>are
     * not the same.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>image</i> is not a valid image object.
     * </p>
     * <p>
     * CL_INVALID_VALUE if region being mapped given by (<i>origin</i>,
     * <i>origin+region</i>) is out of bounds or if values specified in
     * <i>map_flags </i>are not valid.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>image</i> is a 2D image object and <i>origin</i>[2]
     * is not equal to 0 or <i>region</i>[2] is not equal to 1.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>image_row_pitch</i> is NULL.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>image</i> is a 3D image object and
     * <i>image_slice_pitch</i> is NULL.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is
     * not NULL and <i>num_events_in_wait_list</i> is 0, or if event
     * objects in <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_MAP_FAILURE if there is a failure to map the requested region into
     * the host address space. This error cannot occur for image objects
     * created with CL_MEM_USE_HOST_PTR or CL_MEM_ALLOC_HOST_PTR.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate
     * memory for data store associated with <i>image</i>.
     * </p>
     * <p>
     * <i>errcode_ret</i> returns CL_OUT_OF_HOST_MEMORY if there is a
     * failure to allocate resources required by the OpenCL implementation
     * on the host.
     * </p>
     * <p>
     * The pointer returned maps a 2D or 3D region starting at <i>origin</i>
     * and is atleast (<i>image_row_pitch * region[1] + region[0])</i>
     * pixels in size for a 2D image, and is atleast (<i>image_slice_pitch *
     * region[2] + image_row_pitch * region[1] + region[0])</i> pixels in
     * size for a 3D image. The result of a memory access outside this
     * region is undefined.
     * </p>
     * <p>
     * If the buffer or image object is created with CL_MEM_USE_HOST_PTR set
     * in <i>mem_flags</i>, the following will be true:
     * </p>
     * <ol >
     * <li> The <i>host_ptr</i> specified in <b>clCreateBuffer </b>or<b>
     * clCreateImage{2D|3D}</b> is guaranteed to contain the latest bits in
     * the region being mapped when the <b>clEnqueueMapBuffer</b> or
     * <b>clEnqueueMapImage</b> command has completed. </li>
     * <li > The pointer value returned by <b>clEnqueueMapBuffer</b> or
     * <b>clEnqueueMapImage</b> will be derived from the <i>host_ptr</i>
     * specified when the buffer or image object is created. </li>
     * </ol>
     */
    public static synchronized ByteBuffer clEnqueueMapImage(cl_command_queue command_queue, cl_mem image, boolean blocking_map, long map_flags, long origin[], long region[], long image_row_pitch[], long image_slice_pitch[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        ByteBuffer result = clEnqueueMapImageNative(command_queue, image, blocking_map, map_flags, origin, region, image_row_pitch, image_slice_pitch, num_events_in_wait_list, event_wait_list, event, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native ByteBuffer clEnqueueMapImageNative(cl_command_queue command_queue, cl_mem image, boolean blocking_map, long map_flags, long origin[], long region[], long image_row_pitch[], long image_slice_pitch[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[]);

    /**
     * Enqueues a command to unmap a previously mapped region of a memory
     * object.
     * <p>
     * cl_int <b>clEnqueueUnmapMemObject</b> (cl_command_queue <i>command_queue</i>,
     * cl_mem <i>memobj</i>, void *<i>mapped_ptr</i>, cl_uint
     * <i>num_events_in_wait_list</i>, const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * Reads or writes from the host using the pointer returned by
     * <b>clEnqueueMapBuffer or clEnqueueMapImage </b>are considered to be
     * complete.
     * </p>
     * <p>
     * <i>command_queue</i> must be a valid command-queue.
     * </p>
     * <p>
     * <i>memobj</i> is a valid memory object. The OpenCL context associated
     * with <i>command_queue</i> and <i>memobj</i> must be the same.
     * </p>
     * <p>
     * <i>mapped_ptr</i> is the host address returned by a previous call to
     * <b>clEnqueueMapBuffer</b> or <b>clEnqueueMapImage</b> for <i>memobj</i>.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before <b>clEnqueueUnmapMemObject</b> can
     * be executed. If <i>event_wait_list</i> is NULL, then
     * <b>clEnqueueUnmapMemObject</b> does not wait on any event to complete.
     * If <i>event_wait_list</i> is NULL, <i>num_events_in_wait_list</i> must
     * be 0. If <i>event_wait_list</i> is not NULL, the list of events pointed
     * to by <i>event_wait_list</i> must be valid and
     * <i>num_events_in_wait_list</i> must be greater than 0. The events
     * specified in <i>event_wait_list</i> act as synchronization points. The
     * context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * command and can be used to query or queue a wait for this particular
     * command to complete. <i>event</i> can be NULL in which case it will not
     * be possible for the application to query the status of this command or
     * queue a wait for this command to complete. <b>clEnqueueBarrier</b> can
     * be used instead.
     * </p>
     * <p>
     * <b>clEnqueueUnmapMemObject</b> returns CL_SUCCESS if the function is
     * executed successfully. Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if <i>memobj</i> is not a valid memory object.
     * </p>
     * <p>
     * CL_INVALID_VALUE if <i>mapped_ptr</i> is not a valid pointer returned by
     * <b>clEnqueueMapBuffer</b> or <b>clEnqueueMapImage</b> for <i>memobj</i>.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or if <i>event_wait_list</i> is
     * not NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if context associated with <i>command_queue</i> and
     * <i>memobj</i> are not the same or if the context associated with
     * <i>command_queue</i> and events in <i>event_wait_list </i>are not the
     * same.
     * </p>
     * <p>
     * <b>clEnqueueMapBuffer </b>and<b> clEnqueueMapImage </b>increments the
     * mapped count of the memory object. The initial mapped count value of a
     * memory object is zero. Multiple calls to <b>clEnqueueMapBuffer </b>or<b>
     * clEnqueueMapImage </b>on the same memory object will increment this
     * mapped count by appropriate number of calls. <b>clEnqueueUnmapMemObject
     * </b>decrements the mapped count of the memory object.
     * </p>
     * <p>
     * <b>clEnqueueMapBuffer</b> and <b>clEnqueueMapImage</b> act as
     * synchronization points for a region of the memory object being mapped.
     * </p>
     */
    public static synchronized int clEnqueueUnmapMemObject(cl_command_queue command_queue, cl_mem memobj, ByteBuffer mapped_ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueUnmapMemObjectNative(command_queue, memobj, mapped_ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueUnmapMemObjectNative(cl_command_queue command_queue, cl_mem memobj, ByteBuffer mapped_ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to execute a kernel on a device.
     * <p>
     * cl_int <b>clEnqueueNDRangeKernel</b> (cl_command_queue <i>command_queue</i>,
     * cl_kernel <i>kernel</i>,
     * cl_uint <i>work_dim</i>,
     * const size_t *<i>global_work_offset</i>,
     * const size_t *<i>global_work_size</i>,
     * const size_t <i>*local_work_size</i>,
     * cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * <i>command_queue</i> is a valid command-queue. The kernel will be queued
     * for execution on the device associated with <i>command_queue</i>.
     * </p>
     * <p>
     * <i>kernel</i> is a valid kernel object. The OpenCL context associated
     * with <i>kernel</i> and <i>command-queue </i>must be the same.
     * </p>
     * <p>
     * <i>work_dim</i> is the number of dimensions used to specify the global
     * work-items and work-items in the work-group. <i>work_dim</i> must be
     * greater than zero and less than or equal to three.
     * </p>
     * <p>
     * <i>global_work_offset</i> must currently be a NULL value. In a future
     * revision of OpenCL, <i>global_work_offset</i> can be used to specify an
     * array of <i>work_dim</i> unsigned values that describe the offset used
     * to calculate the global ID of a work-item instead of having the global
     * IDs always start at offset (0, 0, ... 0).
     * </p>
     * <p>
     * <i>global_work_size</i><i><sup>14</sup></i> points to an array of
     * <i>work_dim</i> unsigned values that describe the number of global
     * work-items in <i>work_dim</i> dimensions that will execute the kernel
     * function. The total number of global work-items is computed as
     * <i>global_work_size</i>[0] * ... * <i>global_work_size</i>[<i>work_dim</i>
     * - 1].
     * </p>
     * <p>
     * <i>local_work_size</i> points to an array of <i>work_dim</i> unsigned
     * values that describe the number of work-items that make up a work-group
     * (also referred to as the size of the work-group) that will
     * execute the kernel specified by <i>kernel</i>. The total number of
     * work-items in a work-group is computed as <i>local_work_size</i>[0] *
     * ... * <i>local_work_size</i>[<i>work_dim</i>
     * - 1]. The total number of work-items in the work-group
     * must be less than or equal to the CL_DEVICE_MAX_WORK_GROUP_SIZE value
     * specified in <i>table 4.3</i> and the number of workitems specified in
     * <i>local_work_size</i>[0], ... <i>local_work_size</i>[<i>work_dim</i>
     * - 1] must be less than or equal to the corresponding
     * values specified by CL_DEVICE_MAX_WORK_ITEM_SIZES[0],
     * .... CL_DEVICE_MAX_WORK_ITEM_SIZES[<i>work_dim
     * </i>- 1]. The explicitly specified <i>local_work_size</i>
     * will be used to determine how to break the global work-items specified by
     * <i>global_work_size</i> into appropriate work-group instances. If
     * <i>local_work_size</i> is specified, the values specified in
     * <i>global_work_size</i>[0], ... <i>global_work_size</i>[<i>work_dim</i> -
     * 1] must be evenly divisable by the corresponding values specified in
     * <i>local_work_size</i>[0], ... <i>local_work_size</i>[<i>work_dim</i>
     * - 1].
     * </p>
     * <p>
     * The work-group size to be used for <i>kernel</i> can also be specified
     * in the program source using the __attribute__((reqd_work_group_size(X, Y,
     * Z)))qualifier (refer to <i>section 6.7.2</i>). In this case the size of
     * work group specified by <i>local_work_size</i> must match the value
     * specified by the reqd_work_group_size attribute qualifier.
     * </p>
     * <p>
     * <i>local_work_size</i> can also be a NULL value in which case the OpenCL
     * implementation will determine how to be break the global work-items into
     * appropriate work-group instances.
     * </p>
     * <p>
     * These work-group instances are executed in parallel across multiple
     * compute units or concurrently on the same compute unit.
     * </p>
     * <p>
     * Each work-item is uniquely identified by a global identifier. The global
     * ID, which can be read inside the kernel, is computed using the value
     * given by <i>global_work_size </i>and <i>global_work_offset</i>. In
     * OpenCL 1.0, the starting global ID is always (0, 0, ...
     * 0). In addition, a work-item is also identified within a work-group by a
     * unique local ID. The local ID, which can also be read by the kernel, is
     * computed using the value given by <i>local_work_size</i>. The starting
     * local ID is always (0, 0, ... 0).
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * kernel execution instance. Event objects are unique and can be used to
     * identify a particular kernel execution instance later on. If <i>event</i>
     * is NULL, no event will be created for this kernel execution instance and
     * therefore it will not be possible for the application to query or queue a
     * wait for this particular kernel execution instance.
     * </p>
     * <p>
     * <b>clEnqueueNDRangeKernel</b> returns CL_SUCCESS if the kernel execution
     * was successfully queued. Otherwise, it returns one of the following
     * errors: CL_INVALID_PROGRAM_EXECUTABLE if there is no successfully built
     * program executable available for device associated with <i>command_queue</i>.
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_KERNEL if <i>kernel</i> is not a valid kernel object.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if context associated with <i>command_queue </i>and
     * <i>kernel</i> is not the same or if the context associated with
     * <i>command_queue</i> and events in <i>event_wait_list </i>are not the
     * same.
     * </p>
     * <p>
     * CL_INVALID_KERNEL_ARGS if the kernel argument values have not been
     * specified.
     * </p>
     * <p>
     * CL_INVALID_WORK_DIMENSION if <i>work_dim</i> is not a valid value (i.e.
     * a value between 1 and 3).
     * </p>
     * <p>
     * CL_INVALID_WORK_GROUP_SIZE if <i>local_work_size</i> is specified and
     * number of workitems specified by <i>global_work_size</i> is not evenly
     * divisable by size of work-group given by <i>local_work_size</i> or does
     * not match the work-group size specified for <i>kernel</i> using the
     * __attribute__((reqd_work_group_size(X, Y, Z)))qualifier in program
     * </p>
     * <p>
     * source.
     * </p>
     * <dl>
     * </dl>
     * CL_INVALID_WORK_GROUP_SIZE if <i>local_work_size</i> is specified and
     * the total number of work-items in the work-group computed as
     * <i>local_work_size</i>[0] * ... <i>local_work_size</i>[<i>work_dim</i>
     * - 1] is greater than the value specified by
     * CL_DEVICE_MAX_WORK_GROUP_SIZE in <i>table 4.3</i>.
     * CL_INVALID_WORK_GROUP_SIZE if <i>local_work_size</i> is NULL and the
     * __attribute__((reqd_work_group_size(X, Y, Z)))qualifier is used to
     * declare the work-group size for <i>kernel</i> in the program source.
     * <p>
     * CL_INVALID_WORK_ITEM_SIZE if the number of work-items specified in any of
     * <i>local_work_size</i>[0], ... <i>local_work_size</i>[<i>work_dim</i>
     * - 1] is greater than the corresponding values
     * specified by CL_DEVICE_MAX_WORK_ITEM_SIZES[0], ...
     * CL_DEVICE_MAX_WORK_ITEM_SIZES[<i>work_dim </i>- 1].
     * </p>
     * <p>
     * CL_INVALID_GLOBAL_OFFSET if <i>global_work_offset</i> is not NULL.
     * </p>
     * <p>
     * CL_OUT_OF_RESOURCES if there is a failure to queue the execution instance
     * of <i>kernel </i>on the command-queue because of insufficient resources
     * needed to execute the kernel. For example, the explicitly specified
     * <i>local_work_size</i> causes a failure to execute the kernel because of
     * insufficient resources such as registers or local memory. Another example
     * would be the number of read-only image args used in <i>kernel</i> exceed
     * the CL_DEVICE_MAX_READ_IMAGE_ARGS value for device or the number of
     * write-only image args used in <i>kernel</i> exceed the
     * CL_DEVICE_MAX_WRITE_IMAGE_ARGS value for device or the number of samplers
     * used in <i>kernel</i> exceed CL_DEVICE_MAX_SAMPLERS for device.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with image or buffer objects specified as
     * arguments to <i>kernel</i>.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized int clEnqueueNDRangeKernel(cl_command_queue command_queue, cl_kernel kernel, int work_dim, long global_work_offset[], long global_work_size[], long local_work_size[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueNDRangeKernelNative(command_queue, kernel, work_dim, global_work_offset, global_work_size, local_work_size, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueNDRangeKernelNative(cl_command_queue command_queue, cl_kernel kernel, int work_dim, long global_work_offset[], long global_work_size[], long local_work_size[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to execute a kernel on a device.
     * <p>
     * cl_int <b>clEnqueueTask</b> (cl_command_queue <i>command_queue</i>,
     * cl_kernel <i>kernel</i>, cl_uint <i>num_events_in_wait_list</i>, const
     * cl_event *<i>event_wait_list</i>, cl_event *<i>event</i>)
     * </p>
     * <p>
     * The kernel is executed using a single work-item.
     * </p>
     * <p>
     * <i>command_queue</i> is a valid command-queue. The kernel will be queued
     * for execution on the device associated with <i>command_queue</i>.
     * </p>
     * <p>
     * <i>kernel</i> is a valid kernel object. The OpenCL context associated
     * with <i>kernel</i> and <i>command-queue </i>must be the same.
     * </p>
     * <p>
     * <i>event_wait_list</i> and <i>num_events_in_wait_list</i> specify
     * events that need to complete before this particular command can be
     * executed. If <i>event_wait_list</i> is NULL, then this particular
     * command does not wait on any event to complete. If <i>event_wait_list</i>
     * is NULL, <i>num_events_in_wait_list </i>must be 0. If <i>event_wait_list</i>
     * is not NULL, the list of events pointed to by <i>event_wait_list</i>
     * must be valid and <i>num_events_in_wait_list</i> must be greater than 0.
     * The events specified in <i>event_wait_list</i> act as synchronization
     * points. The context associated with events in <i>event_wait_list</i> and
     * <i>command_queue</i> must be the same.
     * </p>
     * <p>
     * <i>event</i> returns an event object that identifies this particular
     * kernel execution instance. Event objects are unique and can be used to
     * identify a particular kernel execution instance later on. If <i>event</i>
     * is NULL, no event will be created for this kernel execution instance and
     * therefore it will not be possible for the application to query or queue a
     * wait for this particular kernel execution instance.
     * </p>
     * <p>
     * <b>clEnqueueTask</b> is equivalent to calling <b>clEnqueueNDRangeKernel</b>
     * with <i>work_dim</i> = 1, <i>global_work_offset</i> = NULL,
     * <i>global_work_size</i>[0] set to 1 and <i>local_work_size</i>[0] set
     * to 1.
     * </p>
     * <p>
     * <b>clEnqueueTask</b> returns CL_SUCCESS if the kernel execution was
     * successfully queued. Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_PROGRAM_EXECUTABLE if there is no successfully built program
     * executable available for device associated with <i>command_queue</i>.
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if <i>command_queue</i> is not a valid
     * command-queue.
     * </p>
     * <p>
     * CL_INVALID_KERNEL if <i>kernel</i> is not a valid kernel object.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if context associated with <i>command_queue </i>and
     * <i>kernel</i> is not the same or if the context associated with
     * <i>command_queue</i> and events in <i>event_wait_list </i>are not the
     * same.
     * </p>
     * <p>
     * CL_INVALID_KERNEL_ARGS if the kernel argument values have not been
     * specified.
     * </p>
     * <p>
     * CL_INVALID_WORK_GROUP_SIZE if a work-group size is specified for
     * <i>kernel</i> using the __attribute__((reqd_work_group_size(X, Y,
     * Z)))qualifier in program source and is not (1, 1, 1).
     * </p>
     * <p>
     * CL_OUT_OF_RESOURCES if there is a failure to queue the execution instance
     * of <i>kernel </i>on the command-queue because of insufficient resources
     * needed to execute the kernel.
     * </p>
     * <p>
     * CL_MEM_OBJECT_ALLOCATION_FAILURE if there is a failure to allocate memory
     * for data store associated with image or buffer objects specified as
     * arguments to <i>kernel</i>.
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if <i>event_wait_list</i> is NULL and
     * <i>num_events_in_wait_list</i> &gt; 0, or <i>event_wait_list</i> is not
     * NULL and <i>num_events_in_wait_list</i> is 0, or if event objects in
     * <i>event_wait_list</i> are not valid events.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * The function
     * </p>
     */
    public static synchronized int clEnqueueTask(cl_command_queue command_queue, cl_kernel kernel, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueTaskNative(command_queue, kernel, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueTaskNative(cl_command_queue command_queue, cl_kernel kernel, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a command to execute a native C/C++ function not compiled using
     * the OpenCL compiler.
     * <p>
     * cl_int <b>clEnqueueNativeKernel</b> (cl_command_queue <i>command_queue</i>,
     * void (*<i>user_func</i>)(void *)
     * void *<i>args</i>,
     * size_t <i>cb_args</i>,
     * cl_uint <i>num_mem_objects</i>,
     * const cl_mem *<i>mem_list</i>,
     * const void **<i>args_mem_loc</i>,
     * cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event *<i>event_wait_list</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * <i>command_queue</i> is a valid command-queue. A native user function
     * can only be executed on a command-queue created on a device that has
     * CL_EXEC_NATIVE_KERNEL capability set in CL_DEVICE_EXECUTION_CAPABILITIES
     * as specified in <i>table 4.3</i>.
     * </p>
     * <p>
     * <i>user_func</i> is a pointer to a host-callable user function.
     * </p>
     * <p>
     * <i>args</i> is a pointer to the args list that <i>user_func</i> should
     * be called with.
     * </p>
     * <p>
     * <i>cb_args</i> is the size in bytes of the args list that <i>args</i>
     * points to.
     * </p>
     * <p>
     * The data pointed to by <i>args</i> and <i>cb_args</i> bytes in size
     * will be copied and a pointer to this copied region will be passed to
     * <i>user_func</i>. The copy needs to be done because the memory objects
     * (cl_memvalues) that <i>args</i> may contain need to be modified and
     * replaced by appropriate
     * pointers to global memory. When <b>clEnqueueNativeKernel</b> returns,
     * the memory region pointed to by <i>args</i> can be reused by the
     * application.
     * </p>
     * <p>
     * <i>num_mem_objects</i> is the number of buffer objects that are passed
     * in <i>args</i>.
     * </p>
     */
    public static synchronized int clEnqueueNativeKernel(cl_command_queue command_queue, EnqueueNativeKernelFunction user_func, Object args, long cb_args, int num_mem_objects, cl_mem mem_list[], Pointer args_mem_loc[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueNativeKernelNative(command_queue, user_func, args, cb_args, num_mem_objects, mem_list, args_mem_loc, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueNativeKernelNative(cl_command_queue command_queue, EnqueueNativeKernelFunction user_func, Object args, long cb_args, int num_mem_objects, cl_mem mem_list[], Pointer args_mem_loc[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Enqueues a marker command to <i>command_queue</i>.
     * <p>
     * cl_int <b>clEnqueueMarker</b> (cl_command_queue <i>command_queue</i>,
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * The marker command
     * returns an <i>event</i> which can be used by to queue a wait on this
     * marker event i.e. wait for all commands queued before the marker command
     * to complete.
     * </p>
     * <p>
     * <b>clEnqueueMarker</b> returns CL_SUCCESS if the function is
     * successfully executed. It returns CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i> is not a valid command-queue, returns
     * CL_INVALID_VALUE if <i>event </i>is a NULL value and returns
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate resources
     * required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized int clEnqueueMarker(cl_command_queue command_queue, cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueMarkerNative(command_queue, event));
    }

    private static native int clEnqueueMarkerNative(cl_command_queue command_queue, cl_event event);

    /**
     * Enqueues a wait for a specific event or a list of events.
     * <p>
     * cl_int <b>clEnqueueWaitForEvents</b> (cl_command_queue <i>command_queue</i>,
     * cl_uint <i>num_events</i>,
     * const cl_event *<i>event_list</i>)
     * </p>
     * <p>
     * enqueues a wait for a specific event or a list of events to complete
     * before any future commands queued in the command-queue are executed.
     * <i>num_events</i> specifies the number of events given by <i>event_list</i>.
     * Each event in <i>event_list</i> must be a valid event object returned by
     * a previous call to <b>clEnqueueNDRangeKernel</b>, <b>clEnqueueTask</b>,
     * <b>clEnqueueNativeKernel</b>, <b>clEnqueue{Read|Write|Map}{Buffer|Image}</b>,
     * <b>clEnqueueCopy{Buffer|Image}</b>, <b>clEnqueueCopyBufferToImage,
     * clEnqueueCopyImageToBuffer </b>or<b> clEnqueueMarker</b>. The context
     * associated with events in <i>event_list</i> and <i>command_queue</i>
     * must be the same.
     * </p>
     * <p>
     * The events specified in <i>event_list</i> act as synchronization points.
     * </p>
     * <p>
     * <b>clEnqueueWaitForEvents</b> returns CL_SUCCESS if the function was
     * successfully executed. It returns CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i> is not a valid command-queue, returns
     * CL_INVALID_CONTEXT if the context associated with <i>command_queue</i>
     * and events in <i>event_list</i> are not the same, returns
     * CL_INVALID_VALUE if <i>num_events</i> is zero or <i>event_list</i> is
     * NULL, returns CL_INVALID_EVENT if event objects specified in
     * <i>event_list</i> are not valid events and returns CL_OUT_OF_HOST_MEMORY
     * if there is a failure to allocate resources required by the OpenCL
     * implementation on the host.
     * </p>
     */
    public static synchronized int clEnqueueWaitForEvents(cl_command_queue command_queue, int num_events, cl_event event_list[])
    {
        assertInit();
        return checkResult(clEnqueueWaitForEventsNative(command_queue, num_events, event_list));
    }

    private static native int clEnqueueWaitForEventsNative(cl_command_queue command_queue, int num_events, cl_event event_list[]);

    /**
     * Enqueues a barrier operation.
     * <p align="left" >
     * cl_int <b>clEnqueueBarrier</b> (cl_command_queue <i>command_queue</i>)
     * </p>
     * <p>
     * The <b>clEnqueueBarrier</b> command
     * ensures that all queued commands in <i>command_queue</i> have finished
     * execution before the next batch of commands can begin execution.
     * <b>clEnqueueBarrier</b> is a synchronization point.
     * </p>
     * <p>
     * <b>clEnqueueBarrier</b> returns CL_SUCCESS if the function was executed
     * successfully. It returns CL_INVALID_COMMAND_QUEUE if <i>command_queue</i>
     * is not a valid command-queue and returns CL_OUT_OF_HOST_MEMORY if there
     * is a failure to allocate resources required by the OpenCL implementation
     * on the host.
     * </p>
     */
    public static synchronized int clEnqueueBarrier(cl_command_queue command_queue)
    {
        assertInit();
        return checkResult(clEnqueueBarrierNative(command_queue));
    }

    private static native int clEnqueueBarrierNative(cl_command_queue command_queue);

    /**
     * Creates an OpenCL buffer object from an OpenGL buffer
     * object.
     * <p>
     * cl_mem
     * <b>clCreateFromGLBuffer</b>
     * (cl_context <i>context</i>, 
     * cl_mem_flags <i>flags</i>, 
     * GLuint <i>bufobj</i>, 
     * cl_int * <i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>context</i>
     * is a valid OpenCL context created from an OpenGL context.
     * </p>
     * <p>
     * <i>flags</i>
     * is a bit-field that is used to specify usage information.
     * Refer to
     * <i>table 5.3</i>
     * for a description of
     * <i>flags</i>
     * . Only CL_MEM_READ_ONLY , CL_MEM_WRITE_ONLY and
     * CL_MEM_READ_WRITE values specified in
     * <i>table 5.3</i>
     * can be used.
     * </p>
     * <p>
     * <i>bufobj</i>
     * is the name of a GL buffer object. The data store of the GL
     * buffer object must have have been previously created by
     * calling
     * <b>glBufferData</b>
     * , although its contents need not be initialized. The size of
     * the data store will be used to determine the size of the CL
     * buffer object.
     * </p>
     * <p>
     * <i>errcode_ret</i>
     * will return an appropriate error code as described below. If
     * <i>errcode_ret</i>
     * is NULL , no error code is returned.
     * </p>
     * <p>
     * <b>clCreateFromGLBuffer</b>
     * returns a valid non-zero OpenCL buffer object and
     * <i>errcode_ret</i>
     * is set to CL_SUCCESS if the buffer object is created
     * successfully. Otherwise, it returns a NULL value with one of
     * the following error values returned in
     * <i>errcode_ret</i>:
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if
     * <i>context</i>
     * is not a valid context or was not created from a GL context.
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in
     * <i>flags</i>
     * are not valid.
     * </p>
     * <p>
     * CL_INVALID_GL_OBJECT if
     * <i>bufobj</i>
     * is not a GL buffer object or is a GL buffer object but does
     * not have an existing data store.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate
     * resources required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * The size of the GL buffer object data store at the time
     * <b>clCreateFromGLBuffer</b>
     * is called will be used as the size of buffer object returned
     * by
     * <b>clCreateFromGLBuffer</b>
     * . If the state of a GL buffer object is modified through the
     * GL API (e.g.
     * <b>glBufferData</b>
     * ) while there exists a corresponding CL buffer object,
     * subsequent use of the CL buffer object will result in
     * undefined behavior.
     * </p>
     * <p>
     * The
     * <b>clRetainMemObject</b>
     * and
     * <b>clReleaseMemObject</b>
     * functions can be used to retain and release the buffer
     * object.
     * </p>
     */
    public static synchronized cl_mem clCreateFromGLBuffer(cl_context context, long flags, int bufobj, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_mem result = clCreateFromGLBufferNative(context, flags, bufobj, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_mem clCreateFromGLBufferNative(cl_context context, long flags, int bufobj, int errcode_ret[]);

    /**
     * Creates an OpenCL 2D image object from an OpenGL 2D texture
     * object, or a single face of an OpenGL cubemap texture
     * object.      
     * <p>
     * cl_mem
     * <b>clCreateFromGLTexture2D</b>
     * (cl_context <i>context</i>,
     * cl_mem_flags <i>flags</i>,
     * GLenum <i>texture_target</i>,
     * GLint <i>miplevel</i>,
     * GLuint <i>texture</i>,
     * cl_int * <i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>context</i>
     * is a valid OpenCL context created from an OpenGL context.
     * </p>
     * <p>
     * <i>flags</i>
     * is a bit-field that is used to specify usage information.
     * Refer to
     * <i>table 5.3</i>
     * for a description of
     * <i>flags</i>
     * . Only CL_MEM_READ_ONLY , CL_MEM_WRITE_ONLY and
     * CL_MEM_READ_WRITE values specified in
     * <i>table 5.3</i>
     * may be used.
     * </p>
     * <p>
     * <i>texture_target</i>
     * must be one of GL_TEXTURE_2D ,
     * GL_TEXTURE_CUBE_MAP_POSITIVE_X ,
     * GL_TEXTURE_CUBE_MAP_POSITIVE_Y ,
     * GL_TEXTURE_CUBE_MAP_POSITIVE_Z ,
     * GL_TEXTURE_CUBE_MAP_NEGATIVE_X ,
     * GL_TEXTURE_CUBE_MAP_NEGATIVE_Y ,
     * GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, or GL_TEXTURE_RECTANGLE.
     * <i>texture_target</i>
     * is used only to define the image type of
     * <i>texture</i>
     * . No reference to a bound GL texture object is made or
     * implied by this parameter.
     * </p>
     * <p>
     * <i>miplevel</i>
     * is the mipmap level to be used.
     * </p>
     * <p>
     * <i>texture</i>
     * is the name of a GL 2D, cubemap or rectangle texture object.
     * The texture object must be a complete texture as per OpenGL
     * rules on texture completeness. The
     * <i>texture</i>
     * format and dimensions defined by OpenGL for the specified
     * <i>miplevel</i>
     * of the texture will be used to create the 2D image object.
     * Only GL texture objects with an internal format that maps to
     * appropriate image channel order and data type specified in
     * <i>tables 5.4</i>
     * and
     * <i>5.5</i>
     * may be used to create a 2D image object.
     * </p>
     * <p>
     * <i>errcode_ret</i>
     * will return an appropriate error code as described below. If
     * <i>errcode_ret</i>
     * is NULL , no error code is returned.
     * </p>
     * <p>
     * <b>clCreateFromGLTexture2D</b>
     * returns a valid non-zero OpenCL image object and
     * <i>errcode_ret</i>
     * is set to CL_SUCCESS if the image object is created
     * successfully. Otherwise, it returns a NULL value with one of
     * the following error values returned in
     * <i>errcode_ret</i>
     * :
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if
     * <i>context</i>
     * is not a valid context or was not created from a GL context
     * .
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in
     * <i>flags</i>
     * are not valid or if value specified in
     * <i>texture_target</i>
     * is not one of the values specified in the description of
     * <i>texture_target</i>
     * .
     * </p>
     * <p>
     * CL_INVALID_MIPLEVEL if
     * <i>miplevel</i>
     * is less than the value of
     * <i>level</i>
     * <i>base</i>
     * (for OpenGL implementations) or zero (for OpenGL ES
     * implementations); or greater than the value of
     * <i>q</i>
     * (for both OpenGL and OpenGL ES).
     * <i>level</i>
     * <i>base</i>
     * and
     * <i>q</i>
     * are defined for the texture in section 3.8.10 (Texture
     * Completeness) of the OpenGL 2.1 specification and section
     * </p>
     * <p>3.7.10 of the OpenGL ES 2.0.</p>
     * <p>
     * CL_INVALID_MIPLEVEL if
     * <i>miplevel</i>
     * is greather than zero and the OpenGL implementation does not
     * support creating from non-zero mipmap levels.
     * </p>
     * <p>
     * CL_INVALID_GL_OBJECT if
     * <i>texture</i>
     * is not a GL texture object whose type matches
     * <i>texture_target</i>
     * , if the specified
     * <i>miplevel</i>
     * of
     * <i>texture</i>
     * is not defined, or if the width or height of the specified
     * <i>miplevel</i>
     * is zero.
     * </p>
     * <p>
     * CL_INVALID_IMAGE_FORMAT_DESCRIPTOR if the OpenGL texture
     * internal format does not map to a supported OpenCL image
     * format.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate
     * resources required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized cl_mem clCreateFromGLTexture2D(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_mem result = clCreateFromGLTexture2DNative(context, flags, target, miplevel, texture, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_mem clCreateFromGLTexture2DNative(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[]);

    /**
     * Creates an OpenCL 3D image object from an OpenGL 3D texture
     * object.
     * <p>
     * cl_mem
     * <b>clCreateFromGLTexture3D</b>
     * (cl_context <i>context</i>, 
     * cl_mem_flags <i>flags</i>, 
     * GLenum <i>texture_target</i>, 
     * GLint <i>miplevel</i>, 
     * GLuint <i>texture</i>,
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * <i>context</i>
     * is a valid OpenCL context created from an OpenGL context.
     * </p>
     * <p>
     * <i>flags</i>
     * is a bit-field that is used to specify usage information.
     * Refer to
     * <i>table 5.3</i>
     * for a description of
     * <i>flags</i>
     * . Only CL_MEM_READ_ONLY , CL_MEM_WRITE_ONLY and
     * CL_MEM_READ_WRITE values specified in
     * <i>table 5.3</i>
     * can be used.
     * </p>
     * <p>
     * <i>texture_target</i>
     * must be GL_TEXTURE_3D .
     * <i>texture_target</i>
     * is used only to define the image type of
     * <i>texture</i>
     * . No reference to a bound GL texture object is made or
     * implied by this parameter.
     * </p>
     * <p>
     * <i>miplevel</i>
     * is the mipmap level to be used.
     * </p>
     * <p>
     * <i>texture</i>
     * is the name of a GL 3D texture object. The texture object
     * must be a complete texture as per OpenGL rules on texture
     * completeness. The
     * <i>texture</i>
     * format and dimensions defined by OpenGL for the specified
     * <i>miplevel</i>
     * of the texture will be used to create the 3D image object.
     * Only GL texture objects with an internal format that maps to
     * appropriate image channel order and data type specified in
     * <i>tables 5.4</i>
     * and
     * <i>5.5</i>
     * can be used to create the 3D image object.
     * </p>
     * <p>
     * <i>errcode_ret</i>
     * will return an appropriate error code as described below. If
     * <i>errcode_ret</i>
     * is NULL , no error code is returned.
     * </p>
     * <p>
     * <b>clCreateFromGLTexture3D</b>
     * returns a valid non-zero image object and
     * <i>errcode_ret</i>
     * is set to CL_SUCCESS if the image object is created
     * successfully. Otherwise, it returns a NULL value with one of
     * the following error values returned in
     * <i>errcode_ret</i>
     * :
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if
     * <i>context</i>
     * is not a valid context or was not created from a GL context
     * .
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in
     * <i>flags</i>
     * are not valid or if value specified in
     * <i>texture_target</i>
     * is not one of the values specified in the description of
     * <i>texture_target</i>
     * .
     * </p>
     * <p>
     * CL_INVALID_MIPLEVEL if
     * <i>miplevel</i>
     * is less than the value of
     * <i>level</i>
     * <i>base</i>
     * (for OpenGL implementations) or zero (for OpenGL ES
     * implementations); or greater than the value of
     * <i>q</i>
     * (for both OpenGL and OpenGL ES).
     * <i>level</i>
     * <i>base</i>
     * and
     * <i>q</i>
     * are defined for the texture in section 3.8.10 (Texture
     * Completeness) of the OpenGL 2.1 specification and section
     * </p>
     * <p>3.7.10 of the OpenGL ES 2.0.</p>
     * <p>
     * CL_INVALID_MIPLEVEL if
     * <i>miplevel</i>
     * is greather than zero and the OpenGL implementation does not
     * support creating from non-zero mipmap levels.
     * </p>
     * <p>
     * CL_INVALID_GL_OBJECT if
     * <i>texture</i>
     * is not a GL texture object whose type matches
     * <i>texture_target</i>
     * , if the specified
     * <i>miplevel</i>
     * of
     * <i>texture</i>
     * is not defined, or if the width, height or depth of the
     * specified
     * <i>miplevel</i>
     * is zero.
     * </p>
     * <p>
     * CL_INVALID_IMAGE_FORMAT_DESCRIPTOR if the OpenGL texture
     * internal format
     * </p>
     * <p>does not map to a supported OpenCL image format.</p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate
     * resources required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * If the state of a GL texture object is modified through the
     * GL API (e.g.
     * <b>glTexImage2D</b>
     * ,
     * <b>glTexImage3D</b>
     * or the values of the texture parameters
     * GL_TEXTURE_BASE_LEVEL or GL_TEXTURE_MAX_LEVEL are modified)
     * while there exists a corresponding CL image object,
     * subsequent use of the CL image object will result in
     * undefined behavior.
     * </p>
     * <p>
     * The
     * <b>clRetainMemObject</b>
     * and
     * <b>clReleaseMemObject</b>
     * functions can be used to retain and release the image
     * objects.
     * </p>
     */
    public static synchronized cl_mem clCreateFromGLTexture3D(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_mem result = clCreateFromGLTexture3DNative(context, flags, target, miplevel, texture, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_mem clCreateFromGLTexture3DNative(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[]);

    /**
     * Creates an OpenCL 2D image object from an OpenGL
     * renderbuffer object.
     * <p>
     * cl_mem
     * <b>clCreateFromGLRenderbuffer</b>
     * (cl_context <i>context</i>, 
     * cl_mem_flags <i>flags</i>, 
     * GLuint <i>renderbuffer</i>, 
     * cl_int *<i>errcode_ret</i>)
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * <i>context</i>
     * is a valid OpenCL context created from an OpenGL context.
     * </p>
     * <p>
     * <i>flags</i>
     * is a bit-field that is used to specify usage information.
     * Refer to
     * <i>table 5.3</i>
     * for a description of
     * <i>flags</i>
     * . Only CL_MEM_READ_ONLY , CL_MEM_WRITE_ONLY and
     * CL_MEM_READ_WRITE values specified in
     * <i>table 5.3</i>
     * can be used.
     * </p>
     * <p>
     * <i>renderbuffer</i>
     * is the name of a GL renderbuffer object. The renderbuffer
     * storage must be specified before the image object can be
     * created. The
     * <i>renderbuffer</i>
     * format and dimensions defined by OpenGL will be used to
     * create the 2D image object. Only GL renderbuffers with
     * internal formats that maps to appropriate image channel
     * order and data type specified in
     * <i>tables</i>
     * </p>
     * <p>
     * <i>5.4</i>
     * and
     * <i>5.5</i>
     * can be used to create the 2D image object.
     * </p>
     * <p>
     * <i>errcode_ret</i>
     * will return an appropriate error code as described below. If
     * <i>errcode_ret</i>
     * is NULL , no error code is returned.
     * </p>
     * <p>
     * <b>clCreateFromGLRenderbuffer</b>
     * returns a valid non-zero OpenCL image object and
     * <i>errcode_ret</i>
     * is set to CL_SUCCESS if the image object is created
     * successfully. Otherwise, it returns a NULL value with one of
     * the following error values returned in
     * <i>errcode_ret</i>
     * :
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if
     * <i>context</i>
     * is not a valid context or was not created from a GL context
     * .
     * </p>
     * <p>
     * CL_INVALID_VALUE if values specified in
     * <i>flags</i>
     * are not valid.
     * </p>
     * <p>
     * CL_INVALID_GL_OBJECT if
     * <i>renderbuffer</i>
     * is not a GL renderbuffer object or if the width or height of
     * <i>renderbuffer</i>
     * is zero.
     * </p>
     * <p>
     * CL_INVALID_IMAGE_FORMAT_DESCRIPTOR if the OpenGL
     * renderbuffer internal format does not map to a supported
     * OpenCL image format.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate
     * resources required by the OpenCL implementation on the host.
     * </p>
     * <p>
     * If the state of a GL renderbuffer object is modified through
     * the GL API (i.e. changes to the dimensions or format used to
     * represent pixels of the GL renderbuffer using appropriate GL
     * API calls such as
     * <b>glRenderbufferStorage</b>
     * ) while there exists a corresponding CL image object,
     * subsequent use of the CL image object will result in
     * undefined behavior.
     * </p>
     * <p>
     * The
     * <b>clRetainMemObject</b>
     * and
     * <b>clReleaseMemObject</b>
     * functions can be used to retain and release the image
     * objects.
     * </p>
     */
    public static synchronized cl_mem clCreateFromGLRenderbuffer(cl_context context, long flags, int renderbuffer, int errcode_ret[])
    {
        assertInit();
        if (errcode_ret == null)
        {
            errcode_ret = new int[1];
        }
        cl_mem result = clCreateFromGLRenderbufferNative(context, flags, renderbuffer, errcode_ret);
        checkResult(errcode_ret[0]);
        return result;
    }

    private static native cl_mem clCreateFromGLRenderbufferNative(cl_context context, long flags, int renderbuffer, int errcode_ret[]);

    /**
     * Query the OpenGL object used to create the OpenCL memory object
     * and information about the object type i.e. whether it is a
     * texture, renderbuffer or buffer object.
     * <p>
     * cl_int <b>clGetGLObjectInfo</b>
     * (cl_mem <i>memobj</i>,
     * cl_gl_object_type * <i>gl_object_type</i>,
     * GLuint * <i>gl_object_name</i>)
     * </p>
     * <p>
     * <i>gl_object_type</i>
     * returns the type of GL object attached to
     * <i>memobj</i>
     * and can be CL_GL_OBJECT_BUFFER, CL_GL_OBJECT_TEXTURE2D ,
     * CL_GL_OBJECT_TEXTURE3D , or CL_GL_OBJECT_RENDERBUFFER . If
     * <i>gl_object_type</i>
     * is NULL , it is ignored
     * </p>
     * <p>
     * <i>gl_object_name</i>
     * returns the GL object name used to create
     * <i>memobj</i>
     * . If
     * <i>gl_object_name</i>
     * is NULL , it is ignored.
     * </p>
     * <p>
     * <b>clGetGLObjectInfo</b>
     * returns CL_SUCCESS if the call was executed successfully. It
     * returns CL_INVALID_MEM_OBJECT if
     * <i>memobj</i>
     * is not a valid OpenCL memory object, or CL_INVALID_GL_OBJECT
     * if there is no GL object associated with
     * <i>memobj</i>
     * .
     * </p>
     */
    public static synchronized int clGetGLObjectInfo(cl_mem memobj, int gl_object_type[], int gl_object_name[])
    {
        assertInit();
        return checkResult(clGetGLObjectInfoNative(memobj, gl_object_type, gl_object_name));
    }

    private static native int clGetGLObjectInfoNative(cl_mem memobj, int gl_object_type[], int gl_object_name[]);

    /**
     * Returns additional information about the GL texture object
     * associated with <i>memobj</i>.
     * <p>
     * cl_int
     * <b>clGetGLTextureInfo</b>
     * (cl_mem <i>memobj</i>,
     * cl_gl_texture_info <i>param_name</i>,
     * size_t <i>param_value_size</i>,
     * void * <i>param_value</i>,
     * size_t * <i>param_value_size_ret</i>)
     * </p>
     * <p>
     * <i>param_name</i>
     * specifies what additional information about the GL texture
     * object associated with
     * <i>memobj</i>
     * to query. The list of supported
     * <i>param_name</i>
     * types and the information returned in
     * <i>param_value</i>
     * by
     * <b>clGetGLTextureInfo</b>
     * is described in
     * <i>table B.2</i>
     * below.
     * </p>
     * <p>
     * <i>param_value</i>
     * is a pointer to memory where the result being queried is
     * returned. If
     * <i>param_value</i>
     * is NULL , it is ignored.
     * </p>
     * <p>
     * <i>param_value_size</i>
     * is used to specify the size in bytes of memory pointed to by
     * <i>param_value</i>
     * . This size must be &gt;= size of return type as described
     * in
     * <i>table B.2</i>
     * below.
     * </p>
     * <p>
     * <i>param_value_size_ret</i>
     * returns the actual size in bytes of data copied to
     * <i>param_value</i>
     * . If
     * <i>param_value_size_ret</i>
     * is NULL , it is ignored.
     * </p>
     * <TABLE border="1">
     * <CAPTION>
     * <p>
     * <b>Table B.2</b>
     * <i>List of supported param_names by</i>
     * clGetGLTextureInfo
     * </p>
     * </CAPTION>
     * <TR>
     * <TH />
     * <TH>
     * <b>cl_gl_texture_info</b>
     * </TH>
     * <TH />
     * <TH />
     * <TH>
     * <b>Return Type</b>
     * </TH>
     * <TH />
     * <TH />
     * <TH>
     * <b>Info. returned in</b>
     * <b>
     * <i>param_value</i>
     * i>
     * </b>
     * </TH>
     * <TH />
     * </TR>
     * <TR>
     * <TH colspan="2">
     * <b>CL_GL_TEXTURE_TARGET</b>
     * </TH>
     * <TD />
     * <TD colspan="2">GLenum</TD>
     * <TD />
     * <TD colspan="3">
     * The
     * <i>texture_target</i>
     * argument specified in
     * <b>clCreateGLTexture2D</b>
     * , or
     * <b>clCreateGLTexture3D</b>
     * .
     * </TD>
     * </TR>
     * <TR>
     * <TH colspan="2">
     * <b>CL_GL_MIPMAP_LEVEL</b>
     * </TH>
     * <TD />
     * <TD colspan="2">GLint</TD>
     * <TD />
     * <TD colspan="3">
     * The
     * <i>miplevel</i>
     * argument specified in
     * <b>clCreateGLTexture2D</b>
     * , or
     * <b>clCreateGLTexture3D</b>
     * .
     * </TD>
     * </TR>
     * </TABLE>
     * <p>
     * <b>clGetGLTextureInfo</b>
     * returns CL_SUCCESS if the function is executed successfully.
     * It returns CL_INVALID_MEM_OBJECT if
     * <i>memobj</i>
     * is not a valid OpenCL memory object, CL_INVALID_GL_OBJECT if
     * there is no GL texture object associated with
     * <i>memobj</i>
     * , or CL_INVALID_VALUE if
     * <i>param_name</i>
     * is not valid, or if size in bytes specified by
     * <i>param_value_size</i>
     * is &lt; size of return type as described in
     * <i>table B.2</i>
     * and
     * <i>param_value</i>
     * is not NULL , or if
     * <i>param_value</i>
     * and
     * <i>param_value_size_ret</i>
     * are NULL .
     * </p>
     */
    public static synchronized int clGetGLTextureInfo(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        assertInit();
        return checkResult(clGetGLTextureInfoNative(memobj, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetGLTextureInfoNative(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * Acquire OpenCL memory objects that have been
     * created from OpenGL objects.
     * <p>
     * cl_int
     * <b>clEnqueueAcquireGLObjects</b>
     * (cl_command_queue <i>command_queue</i>,
     * cl_uint <i>num_objects</i>,
     * const cl_mem * <i>mem_objects,</i>
     * cl_uint <i>num_events_in_wait_list</i>,
     * const cl_event * <i>event_wait_list</i>,
     * cl_event * <i>event</i>)
     * </p>
     * <p>
     * Acquire OpenCL memory objects that have been
     * created from OpenGL objects. These objects need to be
     * acquired before they can be used by any OpenCL commands
     * queued to a command-queue. The OpenGL objects are acquired
     * by the OpenCL context associated with
     * <i>command_queue</i>
     * and can therefore be used by all command-queues associated
     * with the OpenCL context.
     * </p>
     * <p>
     * <i>command_queue</i>
     * is a valid command-queue. All devices used to create the
     * OpenCL context associated with
     * <i>command_queue</i>
     * must support acquiring shared CL/GL objects. This constraint
     * is enforced at context creation time.
     * </p>
     * <p>
     * <i>num_objects</i>
     * is the number of memory objects to be acquired in
     * <i>mem_objects</i>
     * .
     * </p>
     * <p>
     * <i>mem_objects</i>
     * is a pointer to a list of CL memory objects that correspond
     * to GL objects.
     * </p>
     * <p>
     * <i>event_wait_list</i>
     * and
     * <i>num_events_in_wait_list</i>
     * specify events that need to complete before this particular
     * command can be executed. If
     * <i>event_wait_list</i>
     * is NULL , then this particular command does not wait on any
     * event to complete. If
     * <i>event_wait_list</i>
     * is NULL ,
     * <i>num_events_in_wait_list</i>
     * must be 0. If
     * <i>event_wait_list</i>
     * is not NULL , the list of events pointed to by
     * <i>event_wait_list</i>
     * must be valid and
     * <i>num_events_in_wait_list</i>
     * must be greater than 0. The events specified in
     * <i>event_wait_list</i>
     * act as synchronization points.
     * </p>
     * <p>
     * <i>event</i>
     * returns an event object that identifies this command and can
     * be used to query or queue a wait for the command to
     * complete.
     * <i>event</i>
     * can be NULL in which case it will not be possible for the
     * application to query the status of this command or queue a
     * wait for this command to complete.
     * </p>
     * <p>
     * <b>clEnqueueAcquireGLObjects</b>
     * returns CL_SUCCESS if the function is executed successfully.
     * If
     * <i>num_objects</i>
     * is 0 and
     * <i>mem_objects</i>
     * is NULL the function does nothing and returns CL_SUCCESS .
     * Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_VALUE if
     * <i>num_objects</i>
     * is zero and
     * <i>mem_objects</i>
     * is not a NULL value or if
     * <i>num_objects</i>
     * &gt; 0 and
     * <i>mem_objects</i>
     * is NULL.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if memory objects in
     * <i>mem_objects</i>
     * are not valid OpenCL memory objects.
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i>
     * is not a valid command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if context associated with
     * <i>command_queue</i>
     * was not created from an OpenGL context
     * </p>
     * <p>
     * CL_INVALID_GL_OBJECT if memory objects in
     * <i>mem_objects</i>
     * have not been created from a GL object(s).
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if
     * <i>event_wait_list</i>
     * is NULL and
     * <i>num_events_in_wait_list</i>
     * &gt; 0, or
     * <i>event_wait_list</i>
     * is not NULL and
     * <i>num_events_in_wait_list</i>
     * is 0, or if event objects in
     * <i>event_wait_list</i>
     * are not valid events.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate
     * resources required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized int clEnqueueAcquireGLObjects(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueAcquireGLObjectsNative(command_queue, num_objects, mem_objects, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueAcquireGLObjectsNative(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Release OpenCL memory objects that have been
     * created from OpenGL objects.
     * <p>
     * cl_int
     * <b>clEnqueueReleaseGLObjects</b>
     * (cl_command_queue <i>command_queue</i>, 
     * cl_uint <i>num_objects</i>,
     * const cl_mem * <i>mem_objects</i>, 
     * cl_uint <i>num_events_in_wait_list</i>, 
     * const cl_event * <i>event_wait_list</i>, 
     * cl_event *<i>event</i>)
     * </p>
     * <p>
     * Release OpenCL memory objects that have been
     * created from OpenGL objects. These objects need to be
     * released before they can be used by OpenGL. The OpenGL
     * objects are released by the OpenCL context associated with
     * <i>command_queue</i>
     * .
     * </p>
     * <p>
     * <i>num_objects</i>
     * is the number of memory objects to be released in
     * <i>mem_objects</i>
     * .
     * </p>
     * <p>
     * <i>mem_objects</i>
     * is a pointer to a list of CL memory objects that correpond
     * to GL objects.
     * </p>
     * <p>
     * <i>event_wait_list</i>
     * and
     * <i>num_events_in_wait_list</i>
     * specify events that need to complete before this command can
     * be executed. If
     * <i>event_wait_list</i>
     * is NULL , then this particular command does not wait on any
     * event to complete. If
     * <i>event_wait_list</i>
     * is NULL ,
     * <i>num_events_in_wait_list</i>
     * must be 0. If
     * <i>event_wait_list</i>
     * is not NULL , the list of events pointed to by
     * <i>event_wait_list</i>
     * must be valid and
     * <i>num_events_in_wait_list</i>
     * must be greater than 0. The events specified in
     * <i>event_wait_list</i>
     * act as synchronization points.
     * </p>
     * <p>
     * <i>event</i>
     * returns an event object that identifies this particular read
     * / write command and can be used to query or queue a wait for
     * the command to complete.
     * <i>event</i>
     * can be NULL in which case it will not be possible for the
     * application to query the status of this command or queue a
     * wait for this command to complete.
     * </p>
     * <p>
     * <b>clEnqueueReleaseGLObjects</b>
     * returns CL_SUCCESS if the function is executed successfully.
     * If
     * <i>num_objects</i>
     * is 0 and
     * <i>mem_objects</i>
     * is NULL the function does nothing and returns CL_SUCCESS .
     * Otherwise, it returns one of the following errors:
     * </p>
     * <p>
     * CL_INVALID_VALUE if
     * <i>num_objects</i>
     * is zero and
     * <i>mem_objects</i>
     * is not a NULL value or if
     * <i>num_objects</i>
     * &gt; 0 and
     * <i>mem_objects</i>
     * is NULL.
     * </p>
     * <p>
     * CL_INVALID_MEM_OBJECT if memory objects in
     * <i>mem_objects</i>
     * are not valid OpenCL memory objects.
     * </p>
     * <p>
     * CL_INVALID_COMMAND_QUEUE if
     * <i>command_queue</i>
     * is not a valid command-queue.
     * </p>
     * <p>
     * CL_INVALID_CONTEXT if context associated with
     * <i>command_queue</i>
     * was not created from an OpenGL context
     * </p>
     * <p>
     * CL_INVALID_GL_OBJECT if memory objects in
     * <i>mem_objects</i>
     * have not been created from a GL object(s).
     * </p>
     * <p>
     * CL_INVALID_EVENT_WAIT_LIST if
     * <i>event_wait_list</i>
     * is NULL and
     * <i>num_events_in_wait_list</i>
     * &gt; 0, or
     * <i>event_wait_list</i>
     * is not NULL and
     * <i>num_events_in_wait_list</i>
     * is 0, or if event objects in
     * <i>event_wait_list</i>
     * are not valid events.
     * </p>
     * <p>
     * CL_OUT_OF_HOST_MEMORY if there is a failure to allocate
     * resources required by the OpenCL implementation on the host.
     * </p>
     */
    public static synchronized int clEnqueueReleaseGLObjects(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        assertInit();
        return checkResult(clEnqueueReleaseGLObjectsNative(command_queue, num_objects, mem_objects, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueReleaseGLObjectsNative(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * Private constructor to prevent instantiation
     */
    private CL()
    {}

}

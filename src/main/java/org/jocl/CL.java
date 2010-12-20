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

import java.lang.ref.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;

/**
 * JOCL - Java bindings for OpenCL.<br />
 * <br />
 * The documentation of the OpenCL methods has been extracted from the OpenCL 
 * registry at http://www.khronos.org/registry/cl/sdk/1.1/docs/man/xhtml/
 * and is copyright (c) 2007-2010 by The Khronos Group Inc. 
 */
public final class CL
{
    // Initialization of the native library
    static
    {
        LibUtils.loadLibrary("JOCL");
    }

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
    public static final int CL_MISALIGNED_SUB_BUFFER_OFFSET             = -13;
    public static final int CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST= -14;

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
    public static final int CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR      = -1000;

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
    // OPENCL_1_1
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF       = 0x1034;
    public static final int CL_DEVICE_HOST_UNIFIED_MEMORY               = 0x1035;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR          = 0x1036;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT         = 0x1037;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_INT           = 0x1038;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG          = 0x1039;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT         = 0x103A;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE        = 0x103B;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF          = 0x103C;
    public static final int CL_DEVICE_OPENCL_C_VERSION                  = 0x103D;


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
    public static final long CL_FP_SOFT_FLOAT = (1 << 6);

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
    public static final int CL_CONTEXT_NUM_DEVICES     = 0x1083;

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
    // OPENCL_1_1
    public static final int CL_Rx                                       = 0x10BA;
    public static final int CL_RGx                                      = 0x10BB;
    public static final int CL_RGBx                                     = 0x10BC;

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
    // OPENCL_1_1
    public static final int CL_MEM_ASSOCIATED_MEMOBJECT                 = 0x1107;
    public static final int CL_MEM_OFFSET                               = 0x1108;

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
    // OPENCL_1_1
    public static final int CL_ADDRESS_MIRRORED_REPEAT                  = 0x1134;

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
    // OPENCL_1_1
    public static final int CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE = 0x11B3;
    public static final int CL_KERNEL_PRIVATE_MEM_SIZE                  = 0x11B4;

    // cl_event_info
    public static final int CL_EVENT_COMMAND_QUEUE = 0x11D0;
    public static final int CL_EVENT_COMMAND_TYPE = 0x11D1;
    public static final int CL_EVENT_REFERENCE_COUNT = 0x11D2;
    public static final int CL_EVENT_COMMAND_EXECUTION_STATUS = 0x11D3;
    // OPENCL_1_1
    public static final int CL_EVENT_CONTEXT                            = 0x11D4;

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
    // OPENCL_1_1
    public static final int CL_COMMAND_READ_BUFFER_RECT                 = 0x1201;
    public static final int CL_COMMAND_WRITE_BUFFER_RECT                = 0x1202;
    public static final int CL_COMMAND_COPY_BUFFER_RECT                 = 0x1203;
    public static final int CL_COMMAND_USER                             = 0x1204;

    // command execution status
    public static final int CL_COMPLETE = 0x0;
    public static final int CL_RUNNING = 0x1;
    public static final int CL_SUBMITTED = 0x2;
    public static final int CL_QUEUED = 0x3;

    // cl_buffer_create_type
    // OPENCL_1_1
    public static final int CL_BUFFER_CREATE_TYPE_REGION                = 0x1220;

    // cl_profiling_info
    public static final int CL_PROFILING_COMMAND_QUEUED = 0x1280;
    public static final int CL_PROFILING_COMMAND_SUBMIT = 0x1281;
    public static final int CL_PROFILING_COMMAND_START = 0x1282;
    public static final int CL_PROFILING_COMMAND_END = 0x1283;

    // cl_gl_object_type
    public static final int CL_GL_OBJECT_BUFFER             = 0x2000;
    public static final int CL_GL_OBJECT_TEXTURE2D          = 0x2001;
    public static final int CL_GL_OBJECT_TEXTURE3D          = 0x2002;
    public static final int CL_GL_OBJECT_RENDERBUFFER       = 0x2003;

     // cl_gl_texture_info
    public static final int CL_GL_TEXTURE_TARGET            = 0x2004;
    public static final int CL_GL_MIPMAP_LEVEL              = 0x2005;


    // cl_khr_gl_sharing
    public static final int CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR  =  0x2006;
    public static final int CL_DEVICES_FOR_GL_CONTEXT_KHR         =  0x2007;
    public static final int CL_GL_CONTEXT_KHR               = 0x2008;
    public static final int CL_EGL_DISPLAY_KHR              = 0x2009;
    public static final int CL_GLX_DISPLAY_KHR              = 0x200A;
    public static final int CL_WGL_HDC_KHR                  = 0x200B;
    public static final int CL_CGL_SHAREGROUP_KHR           = 0x200C;

    /**
     * Indicates whether exceptions are enabled. When exceptions are
     * enabled, CLException is thrown if a method is about to return
     * a result code that is not CL.CL_SUCCESS
     */
    private static boolean exceptionsEnabled = false;


    /**
     * The thread that frees aligned byte buffers which are no longer
     * referenced
     */
    private static Thread memoryManagementThread = null;

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
     * An implementation of a ThreadFactory that creates low-priority
     * daemon threads
     */
    private static ThreadFactory daemonThreadFactory = new ThreadFactory()
    {
        private long currentID = 0;

        @Override
        public Thread newThread(Runnable r)
        {
            Thread thread = new Thread(r, "AsyncOpThread-"+(currentID++));
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setDaemon(true);
            return thread;
        }

    };

    /**
     * The executor which will manage the threads that prevent
     * objects from being garbage collected during non-blocking
     * write operations
     */
    private static Executor referenceReleaseExecutor =
        new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
        daemonThreadFactory);





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
            case CL_MISALIGNED_SUB_BUFFER_OFFSET: return "CL_MISALIGNED_SUB_BUFFER_OFFSET";
            case CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST: return "CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST";
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
            case CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR: return "CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR";

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
            case CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF: return "CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF";
            case CL_DEVICE_HOST_UNIFIED_MEMORY: return "CL_DEVICE_HOST_UNIFIED_MEMORY";
            case CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR: return "CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR";
            case CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT: return "CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT";
            case CL_DEVICE_NATIVE_VECTOR_WIDTH_INT: return "CL_DEVICE_NATIVE_VECTOR_WIDTH_INT";
            case CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG: return "CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG";
            case CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT: return "CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT";
            case CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE: return "CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE";
            case CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF: return "CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF";
            case CL_DEVICE_OPENCL_C_VERSION: return "CL_DEVICE_OPENCL_C_VERSION";
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
            case CL_CONTEXT_NUM_DEVICES: return "CL_CONTEXT_NUM_DEVICES";
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
            case CL_GL_CONTEXT_KHR: return "CL_GL_CONTEXT_KHR";
            case CL_EGL_DISPLAY_KHR: return "CL_EGL_DISPLAY_KHR";
            case CL_GLX_DISPLAY_KHR: return "CL_GLX_DISPLAY_KHR";
            case CL_WGL_HDC_KHR: return "CL_WGL_HDC_KHR";
            case CL_CGL_SHAREGROUP_KHR: return "CL_CGL_SHAREGROUP_KHR";
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
            case CL_Rx: return "CL_Rx";
            case CL_RGx: return "CL_RGx";
            case CL_RGBx: return "CL_RGBx";
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
            case CL_MEM_ASSOCIATED_MEMOBJECT: return "CL_MEM_ASSOCIATED_MEMOBJECT";
            case CL_MEM_OFFSET: return "CL_MEM_OFFSET";

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
            case CL_ADDRESS_MIRRORED_REPEAT: return "CL_ADDRESS_MIRRORED_REPEAT";
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
            case CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE: return "CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE";
            case CL_KERNEL_PRIVATE_MEM_SIZE: return "CL_KERNEL_PRIVATE_MEM_SIZE";
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
            case CL_EVENT_CONTEXT: return "CL_EVENT_CONTEXT";
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
            case CL_COMMAND_READ_BUFFER_RECT: return "CL_COMMAND_READ_BUFFER_RECT";
            case CL_COMMAND_WRITE_BUFFER_RECT: return "CL_COMMAND_WRITE_BUFFER_RECT";
            case CL_COMMAND_COPY_BUFFER_RECT: return "CL_COMMAND_COPY_BUFFER_RECT";
            case CL_COMMAND_USER: return "CL_COMMAND_USER";
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

    /**
     * Returns the String identifying the given cl_khr_gl_sharing
     *
     * @param n A cl_khr_gl_sharing value
     * @return The String for the given cl_khr_gl_sharing
     */
    public static String stringFor_cl_khr_gl_sharing(int n)
    {
        switch (n)
        {
            case CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR: return "CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR";
            case CL_DEVICES_FOR_GL_CONTEXT_KHR: return "CL_DEVICES_FOR_GL_CONTEXT_KHR";
            case CL_GL_CONTEXT_KHR: return "CL_GL_CONTEXT_KHR";
            case CL_EGL_DISPLAY_KHR: return "CL_EGL_DISPLAY_KHR";
            case CL_GLX_DISPLAY_KHR: return "CL_GLX_DISPLAY_KHR";
            case CL_WGL_HDC_KHR: return "CL_WGL_HDC_KHR";
            case CL_CGL_SHAREGROUP_KHR: return "CL_CGL_SHAREGROUP_KHR";
        }
        return "INVALID cl_khr_gl_sharing: " + n;
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
        if (n == CL_DEVICE_TYPE_ALL)
        {
            return "CL_DEVICE_TYPE_ALL";
        }
        String result = "";
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
        if ((n & CL_FP_SOFT_FLOAT) != 0) result += "CL_FP_SOFT_FLOAT ";
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
    public static synchronized ByteBuffer allocateAligned(int size, int alignment)
    {
        if (memoryManagementThread == null)
        {
            initMemoryManagementThread();
        }

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
     * Creates and starts the daemon thread which will fetch the
     * references to ByteBuffers that have been marked for
     * garbage collection from the alignedByteBufferReferenceQueue,
     * and free the associated aligned native pointers.
     */
    private static void initMemoryManagementThread()
    {
        memoryManagementThread = new Thread(new Runnable()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        Reference<? extends ByteBuffer> reference =
                            alignedByteBufferReferenceQueue.remove();

                        //System.out.println("Free, before  "+alignedByteBufferMap);

                        Pointer pointer = alignedByteBufferMap.get(reference);
                        freeAlignedNative(pointer);
                        alignedByteBufferMap.remove(reference);

                        //System.out.println("Freed, after  "+alignedByteBufferMap);

                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }, "memoryManagementThread");
        memoryManagementThread.setPriority(Thread.MIN_PRIORITY);
        memoryManagementThread.setDaemon(true);
        memoryManagementThread.start();
    }



    // NON_BLOCKING_READ
    /*
     * Implementation note concerning non-blocking reads:
     *
     * Currently, there is no proper way of synchronizing calls like a
     * non-blocking clEnqueueReadBuffer/Image. The pointer data that is
     * passed in may be associated with the respective event. An own
     * thread may be spawned that only waits for the event, and
     * releases the pointer data as soon as possible. In most cases
     * this will work - namely, if the pointer data contained a direct
     * buffer or an array that could be pinned on native side. But if
     * the array was copied, as indicated by the MemoryType ARRAY_COPY
     * in the JNI part, then there is no way to assert that the pointer
     * data is actually released and written back before any other thread
     * accesses the data on Java side.
     *
     * Possible solutions:
     * - It might be possible to use a native kernel for writing back the
     *   pointer data. This could, however, not be transparent for the user,
     *   and most devices probably don't support native kernels at all.
     * - The native implementation may check whether the pointer data
     *   has been copied. In this case, it might throw an Exception.
     *   But the behavior would then be completely unpredictable, since
     *   the decision about copying or pinning is left to the VM.
     * - The native implementation may check whether the pointer data
     *   for a non-blocking call had been copied. It might then enforce
     *   a blocking call to the actual CL function. But this would give
     *   the user to feedback and no information why the call behaves
     *   like a blocking call, although it was enqueued as non-blocking.
     * - One of the two behaviors described above may be controlled via
     *   additional (optional) parameters for the clEnqueueRead* methods.
     *
     * Current solution:
     * - On Java side, it is made sure that for non-blocking read operations,
     *   only Pointers that are pointing to direct buffers are used.
     */
    
    /*
     * TODO:
     * Update for OpenCL 1.1: The event callback mechanism may offer
     * some new options here. One option could be to register an 
     * event callback that acquires a monitor (lock) on the array
     * as soon as the OpenCL command for reading the buffer has been
     * completed, and releases the monitor as soon as the data has been
     * written back to Java by ReleasePrimitiveArrayCritical. This way,
     * it might be possible to avoid that any Java thread accesses the
     * array between the end of the OpenCL command and the time when 
     * the array has been written back to Java. These options have to 
     * be evaluated for future releases. 
     */
    /*
    private static void releasePendingPointerData(cl_event event)
    {
        System.out.println("Releasing pointer data for "+event);
        releasePendingPointerDataNative(event);
    }
    private static native void releasePendingPointerDataNative(cl_event event);

    private static Executor pendingPointerDataExecutor =
        new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
        daemonThreadFactory);

    private static void schedulePointerDataRelease(final cl_event event)
    {
        System.out.println("Scheduling release of pointer data for "+event);
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                clWaitForEvents(1, new cl_event[]{event});
                releasePendingPointerData(event);
            }
        };
        pendingPointerDataExecutor.execute(runnable);
    }
    //*/





    /*
     * Implementation note concerning non-blocking writes:
     *
     * When a non-blocking write operation is scheduled, it might happen that
     * the source data (which is about to be written to an OpenCL memory
     * object) is garbage collected before the write operation is complete.
     * This could cause highly undeterministic errors, possibly crashes or
     * function calls that silently work on bogus data.
     *
     * To avoid this, for each non-blocking write operation, a Runnable is
     * started (in an own thread), which only contains a reference to the
     * data and waits for the OpenCL event that is associated with the write
     * operation. Thus, it keeps the reference alive and prevents the data
     * from being garbage collected until the write operation is finished.
     */
    
    /*
     * TODO:
     * Update for OpenCL 1.1: The event callback mechanism may offer
     * some new options here. One option could be to register an
     * event callback for the write command. A reference to the data 
     * that is about to be written could be kept, and this reference 
     * could be released as soon as the callback has been called. 
     * These options have to be evaluated for future releases. 
     */

    /**
     * Keep a reference to the given object, to prevent it from
     * being garbage collected, until waiting for the given
     * event on a separate thread has completed.
     *
     * @param event The event to wait for
     * @param object The object to which a reference should be kept
     */
    private static void scheduleReferenceRelease(final cl_event event, final Object object)
    {
        Runnable runnable = new Runnable()
        {
            // Could the compiler detect that the object
            // is not really required and perform optimizations
            // that cause the object to be GC'ed too early?

            @SuppressWarnings("unused")
            private Object localObject = object;

            public void run()
            {
                clWaitForEvents(1, new cl_event[]{event});
            }
        };
        referenceReleaseExecutor.execute(runnable);

    }


    /*
     * Implementation note concerning non-blocking writes:
     *
     * One drawback of the current implementation of non-blocking writes
     * using the scheduleReferenceRelease method could be that in case of
     * a sequence of many small non-blocking write operations, a large
     * number of threads could be created, although the threads are
     * maintained using a cached thread pool. First tests have shown that
     * this is not the case, but if this turns out to be a problem later,
     * an alternative implementation might be used (see commented-out
     * method "scheduleReferenceReleasePhantom" below): In this approach,
     * phantom references to the data are kept. When the garbage
     * collector finds out that the data is only reachable via this
     * phantom reference, the reference is enqueued into a reference
     * queue. A single thread is polling this queue, and if it finds
     * the reference to some data, then it waits for the associated
     * event to be completed. If the event already has completed, then
     * the reference may be discarded and the data may be freed.
     * Otherwise, the single thread will wait for the respective
     * event, deferring the release of subsequently scheduled references
     * until the pending write operation is finished.
     */
//   private static Map<PhantomReference<Object>, cl_event> dataReferenceMap =
//       new HashMap<PhantomReference<Object>, cl_event>();
//   private static ReferenceQueue<Object> dataReferenceQueue =
//       new ReferenceQueue<Object>();
//   private static Thread referenceManagementThread = null;
//
//   private static void scheduleReferenceReleasePhantom(final cl_event event, final Object object)
//   {
//       if (referenceManagementThread == null)
//       {
//           initReferenceManagementThread();
//       }
//       PhantomReference<Object> phantomReference =
//           new PhantomReference<Object>(object, dataReferenceQueue);
//       dataReferenceMap.put(phantomReference, event);
//   }
//
//   private static void initReferenceManagementThread()
//   {
//       Thread referenceManagementThread = new Thread(new Runnable()
//       {
//           public void run()
//           {
//               while (true)
//               {
//                   try
//                   {
//                       Reference<? extends Object> reference =
//                           dataReferenceQueue.remove();
//
//                       cl_event event = dataReferenceMap.get(reference);
//
//                       clWaitForEvents(1, new cl_event[]{event});
//                       //scheduleReferenceRelease(event, reference);
//
//                       dataReferenceMap.remove(reference);
//                   }
//                   catch (InterruptedException e)
//                   {
//                       Thread.currentThread().interrupt();
//                       break;
//                   }
//               }
//           }
//       }, "referenceManagementThread");
//       referenceManagementThread.setPriority(Thread.MIN_PRIORITY);
//       referenceManagementThread.setDaemon(true);
//       referenceManagementThread.start();
//
//   }




    //=== OpenCL methods =====================================================

    /**
     * <p>Obtain the list of platforms available.</p>
     *
     * <div title="clGetPlatformIDs">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clGetPlatformIDs</b>(</code>
     *           <td>cl_uint <var>num_entries</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_platform_id <var>*platforms</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>*num_platforms</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>num_entries</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of cl_platform_id entries that can be added to <code>platforms</code>. If <code>platforms</code> is not NULL, the <code>num_entries</code> must be greater than zero.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>platforms</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns a list of OpenCL platforms found. The cl_platform_id values returned in
     *             <code>platforms</code> can be used to identify a specific OpenCL platform. If <code>platforms</code> argument is NULL, this argument is ignored. The number of OpenCL platforms returned is the mininum of the value
     *             specified by <code>num_entries</code> or the number of OpenCL platforms available.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>num_platforms</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the number of OpenCL platforms available. If <code>num_platforms</code> is NULL, this argument is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise it returns  <span>CL_INVALID_VALUE</span> if <code>num_entries</code> is equal to zero and <code>platforms</code> is not NULL, or if both <code>num_platforms</code> and <code>platforms</code> are NULL.
     *     </p>
     *     <p>
     *       Returns <span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *     </p>
     *   </div>
     * </div>
     */
    public static int clGetPlatformIDs(int num_entries, cl_platform_id platforms[], int num_platforms[])
    {
        return checkResult(clGetPlatformIDsNative(num_entries, platforms, num_platforms));
    }

    private static native int clGetPlatformIDsNative(int num_entries, cl_platform_id platforms[], int num_platforms[]);

    /**
     * <p>Get specific information about the OpenCL platform.</p>
     *
     * <div title="clGetPlatformInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clGetPlatformInfo</b>(</code>
     *           <td>cl_platform_id <var>platform</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_platform_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>platform</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The platform ID returned by <span><span>clGetPlatformIDs</span></span> or can be NULL. If <code>platform</code> is NULL, the behavior is implementation-defined.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_name</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>An enumeration constant that identifies the platform information being queried. It can be one of the following values as specified in the table below.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the size in bytes of memory pointed to by <code>param_value</code>. This size in bytes must be greater than or equal to size of return type specified in the table below.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A pointer to memory location where appropriate values for a given <code>param_value</code> will be returned. Possible <code>param_value</code> values returned are listed in the table below. If <code>param_value</code> is NULL, it is ignored.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Returns the actual size in bytes of data being queried by <code>param_value</code>. If <code>param_value_size_ret</code> is NULL, it is ignored</p>
     *         </dd>
     *         <dt>
     *           <span></span>
     *         </dt>
     *         <dd>
     *           <p>
     *             OpenCL Platform Queries
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_platform_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PLATFORM_PROFILE</code>
     *                   </td>
     *                   <td align="left">char[]</td>
     *                   <td align="left">
     *                     <p>OpenCL profile string. Returns the profile name supported by the
     *                       implementation. The profile name returned can be one of the following strings:
     *                     </p>
     *                     <p>FULL_PROFILE - if the implementation supports the OpenCL specification (functionality defined as part of the core specification and does not require any extensions to be
     *                       supported).
     *                     </p>
     *                     <p>EMBEDDED_PROFILE - if the implementation supports the OpenCL embedded profile. The embedded profile is defined to be a subset for each version of OpenCL.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PLATFORM_VERSION</code>
     *                   </td>
     *                   <td align="left">char[]</td>
     *                   <td align="left">
     *                     <p>OpenCL version string. Returns the OpenCL version supported by the
     *                       implementation. This version string has the following format:
     *                     </p>
     *                     <p><code>OpenCL&lt;space&gt;&lt;major_version.minor_version&gt;&lt;space&gt;&lt;platform-specific information&gt;</code></p>
     *                     <p>The <code>major_version.minor_version</code> value returned will be 1.1.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PLATFORM_NAME</code>
     *                   </td>
     *                   <td align="left">char[]</td>
     *                   <td align="left">Platform name string.</td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PLATFORM_VENDOR</code>
     *                   </td>
     *                   <td align="left">char[]</td>
     *                   <td align="left">Platform vendor string.</td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PLATFORM_EXTENSIONS</code>
     *                   </td>
     *                   <td align="left">char[]</td>
     *                   <td align="left">Returns a space-separated list of extension names (the extension names
     *                     themselves do not contain any spaces) supported by the platform. Extensions defined here must be supported by all devices associated with this platform.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       A null terminated string is returned by OpenCL query function calls if the return type of the information being queried is a char[].
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns the following:</p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PLATFORM</span> if <code>platform</code> is not a valid platform.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not one of the supported values or if size in bytes specified by <code>param_value_size</code> is less than size of return type and <code>param_value</code> is not a NULL value.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *     <p>
     *       The OpenCL specification does not describe the order of precedence for error codes returned by API calls.
     *     </p>
     *   </div>
     * </div>
     */
    public static int clGetPlatformInfo(cl_platform_id platform, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetPlatformInfoNative(platform, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetPlatformInfoNative(cl_platform_id platform, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>Obtain the list of devices available on a platform.</p>
     *
     * <div title="clGetDeviceIDs">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clGetDeviceIDs</b>(</code>
     *           <td>cl_platform_id <var>platform</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_device_type <var>device_type</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_entries</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_device_id <var>*devices</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>*num_devices</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>platform</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the platform ID returned by <span><span>clGetPlatformIDs</span></span> or can be NULL. If <code>platform</code> is NULL, the behavior is implementation-defined.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>device_type</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bitfield that identifies the type of OpenCL device. The <code>device_type</code> can be used to query specific OpenCL devices or all OpenCL devices available. The valid values for <code>device_type</code> are specified in the following table.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_device_type</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_CPU</code>
     *                   </td>
     *                   <td align="left">
     *                     An OpenCL device that is the host processor. The host processor runs
     *                     the OpenCL implementations and is a single or multi-core CPU.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_GPU</code>
     *                   </td>
     *                   <td align="left">
     *                     An OpenCL device that is a GPU. By this we mean that the device can also be used
     *                     to accelerate a 3D API such as OpenGL or DirectX.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_ACCELERATOR</code>
     *                   </td>
     *                   <td align="left">
     *                     Dedicated OpenCL accelerators (for example the IBM CELL Blade). These devices
     *                     communicate with the host processor using a peripheral interconnect such as PCIe.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_DEFAULT</code>
     *                   </td>
     *                   <td align="left">The default OpenCL device in the system.</td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_ALL</code>
     *                   </td>
     *                   <td align="left">All OpenCL devices available in the system.</td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>num_entries</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of cl_device entries that can be added to <code>devices</code>. If <code>devices</code> is not NULL, the <code>num_entries</code> must be greater than zero.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>devices</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A list of OpenCL devices found. The cl_device_id values returned in <code>devices</code> can be used to identify a specific OpenCL device. If <code>devices</code> argument is NULL, this argument is ignored. The number of OpenCL devices returned is the mininum of the value specified by <code>num_entries</code> or the number of OpenCL devices whose type matches <code>device_type</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>num_devices</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of OpenCL devices available that match <code>device_type</code>. If <code>num_devices</code> is NULL, this argument is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p><code>clGetDeviceIDs</code> may return all or a subset of the actual physical devices present in the platform and that match <code>device_type</code>.</p>
     *     <p>
     *       The application can query specific capabilities of the OpenCL device(s) returned by <code>clGetDeviceIDs</code>. This can be used by the application to determine which device(s) to use.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p><code>clGetDeviceIDs</code> returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise it returns one of the following:</p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PLATFORM</span> if <code>platform</code> is not a valid platform.
     *         </li>
     *         <li><span>CL_INVALID_DEVICE_TYPE</span> if <code>device_type</code> is not a valid value.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>num_entries</code> is equal to zero and <code>devices</code> is not NULL or if both <code>num_devices</code> and <code>devices</code> are NULL.
     *         </li>
     *         <li><span>CL_DEVICE_NOT_FOUND</span> if no OpenCL devices that matched <code>device_type</code> were found.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetDeviceIDs(cl_platform_id platform, long device_type, int num_entries, cl_device_id devices[], int num_devices[])
    {
        return checkResult(clGetDeviceIDsNative(platform, device_type, num_entries, devices, num_devices));
    }

    private static native int clGetDeviceIDsNative(cl_platform_id platform, long device_type, int num_entries, cl_device_id devices[], int num_devices[]);

    /**
     * <p>Get information about an OpenCL device.</p>
     *
     * <div title="clGetDeviceInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clGetDeviceInfo</b>(</code>
     *           <td>cl_device_id <var>device</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_device_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>device</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Refers to the device returned by <span><span>clGetDeviceIDs</span></span>.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_name</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>An enumeration constant that identifies the device information being queried. It can be one of the values as specified in the table below.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A pointer to memory location where appropriate values for a given <code>param_name</code> as specified in the table below will be returned. If <code>param_value</code> is NULL, it is ignored.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the size in bytes of memory pointed to by <code>param_value</code>. This size in bytes must be greater than or equal to size of return type specified in the table below.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Returns the actual size in bytes of data being queried by <code>param_value</code>. If <code>param_value_size_ret</code> is NULL, it is ignored</p>
     *         </dd>
     *         <dt>
     *           <span></span>
     *         </dt>
     *         <dd>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_device_info</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_ADDRESS_BITS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>The default compute device address space size specified as an unsigned integer value in bits. Currently supported values are 32 or 64 bits.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_AVAILABLE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_bool</p>
     *                     <p>Is CL_TRUE if the device is available and CL_FALSE if the device is not available.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_COMPILER_AVAILABLE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_bool</p>
     *                     <p>Is CL_FALSE if the implementation does not have a compiler available to compile the program source. Is CL_TRUE if the compiler is
     *                       available. This can be CL_FALSE for the embedded platform profile only.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_DOUBLE_FP_CONFIG</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_device_fp_config</p>
     *                     <p>Describes the OPTIONAL double precision floating-point capability of the OpenCL device. This is a bit-field that describes one or more of the following values:</p>
     *                     <div>
     *                       <ul type="disc">
     *                         <li>CL_FP_DENORM - denorms are supported.</li>
     *                         <li>CL_FP_INF_NAN - INF and NaNs are supported.</li>
     *                         <li>CL_FP_ROUND_TO_NEAREST - round to nearest even rounding mode supported.</li>
     *                         <li>CL_FP_ROUND_TO_ZERO - round to zero rounding mode supported.</li>
     *                         <li>CL_FP_ROUND_TO_INF - round to +ve and -ve infinity rounding modes supported.</li>
     *                         <li>CP_FP_FMA - IEEE754-2008 fused multiply-add is supported. </li>
     *                       </ul>
     *                     </div>
     *                     <p>The mandated minimum double precision floating-point capability is CL_FP_FMA |  CL_FP_ROUND_TO_NEAREST | CL_FP_ROUND_TO_ZERO | CL_FP_ROUND_TO_INF | CL_FP_INF_NAN | CL_FP_DENORM.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_ENDIAN_LITTLE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_bool</p>
     *                     <p>Is CL_TRUE if the OpenCL device is a little endian device and CL_FALSE otherwise.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_ERROR_CORRECTION_SUPPORT</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_bool</p>
     *                     <p>Is CL_TRUE if the device implements error correction for all accesses to compute device memory (global and constant).
     *                       Is CL_FALSE if the device does not implement such error correction.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_EXECUTION_CAPABILITIES</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_device_exec_capabilities</p>
     *                     <p>Describes the execution capabilities of the device. This is a bit-field that describes one or more of the following values:</p>
     *                     <p>CL_EXEC_KERNEL - The OpenCL device can execute OpenCL kernels.</p>
     *                     <p>CL_EXEC_NATIVE_KERNEL - The OpenCL device can execute native kernels.</p>
     *                     <p>The mandated minimum capability is CL_EXEC_KERNEL.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_EXTENSIONS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: char[]</p>
     *                     <p>Returns a space-separated list of extension names (the extension names themselves do not contain any spaces). The list of extension names returned currently can include one or more of the following approved extension names:</p>
     *                     <p></p>
     *                     <div>
     *                       <p><span><span>cl_khr_fp64</span></span><br />
     *                         <span><span>cl_khr_int64_base_atomics</span></span><br />
     *                         <span><span>cl_khr_int64_extended_atomics</span></span><br />
     *                         <span><span>cl_khr_fp16</span></span><br />
     *                         <span><span>cl_khr_gl_sharing</span></span><br />
     *                         <span><span>cl_khr_gl_event</span></span><br />
     *                         <span><span>cl_khr_d3d10_sharing</span></span><br />
     *                       </p>
     *                     </div>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_GLOBAL_MEM_CACHE_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_ulong</p>
     *                     <p>Size of global memory cache in bytes.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_GLOBAL_MEM_CACHE_TYPE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_device_mem_cache_type</p>
     *                     <p>Type of global memory cache supported. Valid values are: CL_NONE,
     *                       CL_READ_ONLY_CACHE, and CL_READ_WRITE_CACHE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Size of global memory cache line in bytes.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_GLOBAL_MEM_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_ulong</p>
     *                     <p>Size of global device memory in bytes.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_HALF_FP_CONFIG</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_device_fp_config</p>
     *                     <p>Describes the OPTIONAL half precision floating-point capability of the OpenCL device. This is a bit-field that describes one or more of the following values:</p>
     *                     <div>
     *                       <ul type="disc">
     *                         <li>CL_FP_DENORM - denorms are supported.</li>
     *                         <li>CL_FP_INF_NAN - INF and NaNs are supported.</li>
     *                         <li>CL_FP_ROUND_TO_NEAREST - round to nearest even rounding mode supported.</li>
     *                         <li>CL_FP_ROUND_TO_ZERO - round to zero rounding mode supported.</li>
     *                         <li>CL_FP_ROUND_TO_INF - round to +ve and -ve infinity rounding modes supported.</li>
     *                         <li>CP_FP_FMA - IEEE754-2008 fused multiply-add is supported. </li>
     *                         <li>CL_FP_SOFT_FLOAT - Basic floating-point operations (such as addition, subtraction, multiplication) are implemented in software. </li>
     *                       </ul>
     *                     </div>
     *                     <p>The required minimum half precision floating-point capability as implemented by this extension is CL_FP_ROUND_TO_ZERO or
     *                       CL_FP_ROUND_TO_INF | CL_FP_INF_NAN.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_HOST_UNIFIED_MEMORY</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_bool</p>
     *                     <p>Is CL_TRUE if the device and the host have a unified memory subsystem and is CL_FALSE otherwise.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_IMAGE_SUPPORT</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_bool</p>
     *                     <p>Is CL_TRUE if images are supported by the OpenCL device and CL_FALSE
     *                       otherwise.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_IMAGE2D_MAX_HEIGHT</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Max height of 2D image in pixels. The minimum value is 8192 if
     *                       CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_IMAGE2D_MAX_WIDTH</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Max width of 2D image in pixels. The minimum value is 8192 if
     *                       CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_IMAGE3D_MAX_DEPTH</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Max depth of 3D image in pixels. The minimum value is 2048 if
     *                       CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_IMAGE3D_MAX_HEIGHT</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Max height of 3D image in pixels. The minimum value is 2048 if
     *                       CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_IMAGE3D_MAX_WIDTH</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Max width of 3D image in pixels. The minimum value is 2048 if
     *                       CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_LOCAL_MEM_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_ulong</p>
     *                     <p>Size of local memory arena in bytes. The minimum value is 32 KB.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_LOCAL_MEM_TYPE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_device_local_mem_type</p>
     *                     <p>Type of local memory supported. This can be set to CL_LOCAL implying
     *                       dedicated local memory storage such as SRAM, or CL_GLOBAL.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_CLOCK_FREQUENCY</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Maximum configured clock frequency of the device in MHz.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_COMPUTE_UNITS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>The number of parallel compute cores on the OpenCL device. The minimum value is 1.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_CONSTANT_ARGS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Max number of arguments declared with the <span><span>__constant</span></span> qualifier in a
     *                       kernel. The minimum value is 8.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_ulong</p>
     *                     <p>Max size in bytes of a constant buffer allocation. The minimum value is 64 KB.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_MEM_ALLOC_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_ulong</p>
     *                     <p>Max size of memory object allocation in bytes. The minimum value is max (1/4th of CL_DEVICE_GLOBAL_MEM_SIZE, 128*1024*1024)</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_PARAMETER_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Max size in bytes of the arguments that can be passed to a kernel. The minimum value is 1024. For this minimum value, only a maximum of 128 arguments can be passed to a kernel.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_READ_IMAGE_ARGS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Max number of simultaneous image objects that can be read by a kernel. The minimum value is 128 if CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_SAMPLERS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Maximum number of samplers that can be used in a kernel. The minimum value is 16 if CL_DEVICE_IMAGE_SUPPORT is CL_TRUE. (Also see <span><span>sampler_t</span></span>.)</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_WORK_GROUP_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Maximum number of work-items in a work-group executing a kernel using
     *                       the data parallel execution model. (Refer to <span><span>clEnqueueNDRangeKernel</span></span>). The minimum value is 1.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Maximum dimensions that specify the global and local work-item IDs used by the data parallel execution model. (Refer to <span><span>clEnqueueNDRangeKernel</span></span>). The minimum value is 3.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_WORK_ITEM_SIZES</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t[]</p>
     *                     <p>Maximum number of work-items that can be specified in each dimension of the work-group to <span><span>clEnqueueNDRangeKernel</span></span>.</p>
     *                     <p>Returns <code>n</code> size_t entries, where <code>n</code> is the value returned by the query for CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS. The minimum value is (1, 1, 1).</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MAX_WRITE_IMAGE_ARGS</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Max number of simultaneous image objects that can be written to by a
     *                       kernel. The minimum value is 8 if CL_DEVICE_IMAGE_SUPPORT is CL_TRUE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MEM_BASE_ADDR_ALIGN</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Describes the alignment in bits of the base address of any allocated memory object.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>The smallest alignment in bytes which can be used for any data type.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_NAME</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: char[]</p>
     *                     <p>Device name string.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR</code>
     *                     <p><code>CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT</code></p>
     *                     <p><code>CL_DEVICE_NATIVE_VECTOR_WIDTH_INT</code></p>
     *                     <p><code>CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG</code></p>
     *                     <p><code>CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT</code></p>
     *                     <p><code>CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE</code></p>
     *                     <p><code>CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF</code></p>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Returns the native ISA vector width. The vector width is defined as the number of scalar elements that can be
     *                       stored in the vector.
     *                     </p>
     *                     <p>If the <span><span>cl_khr_fp64</span></span> extension is not supported,
     *                       CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE must return 0.
     *                     </p>
     *                     <p>If the <span><span>cl_khr_fp16</span></span> extension is not supported,
     *                       CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF must return 0.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_OPENCL_C_VERSION</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: char[]</p>
     *                     <p>OpenCL C version string. Returns the highest OpenCL C version supported by the compiler for this device.
     *                       This version string has the following format:
     *                     </p>
     *                     <p><code>OpenCL&lt;space&gt;C&lt;space&gt;&lt;major_version.minor_version&gt;&lt;space&gt;&lt;vendor-specific information&gt;</code></p>
     *                     <p>The <code>major_version.minor_version</code> value must be 1.1 if CL_DEVICE_VERSION is OpenCL 1.1.</p>
     *                     <p>The <code>major_version.minor_version</code> value returned can be 1.0 or 1.1 if CL_DEVICE_VERSION
     *                       is OpenCL 1.0. If OpenCL C 1.1 is returned, this implies that the language feature set defined in
     *                       section 6 of the OpenCL 1.1 specification is supported by the OpenCL 1.0 device.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_PLATFORM</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_platform_id</p>
     *                     <p>The platform associated with this device.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR</code>
     *                     <p><code>CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT</code></p>
     *                     <p><code>CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT</code></p>
     *                     <p><code>CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG</code></p>
     *                     <p><code>CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT</code></p>
     *                     <p><code>CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE</code></p>
     *                     <p><code>CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF</code></p>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>Preferred native vector width size for built-in scalar types that can be put into vectors. The vector width is defined as the number of scalar elements that can be stored in the vector.</p>
     *                     <p>If the <span><span>cl_khr_fp64</span></span> extension is not supported, CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE must return 0.</p>
     *                     <p>
     *                       If the <span><span>cl_khr_fp16</span></span>
     *                       extension is not supported, CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF must return 0.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_PROFILE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: char[]</p>
     *                     <p>OpenCL profile string. Returns the profile name supported by the device (see note). The profile name returned can be one of the following strings:</p>
     *                     <p>FULL_PROFILE - if the device supports the OpenCL specification (functionality defined as part of the core specification and does not require any extensions to be supported).</p>
     *                     <p>EMBEDDED_PROFILE - if the device supports the OpenCL embedded profile.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_PROFILING_TIMER_RESOLUTION</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: size_t</p>
     *                     <p>Describes the resolution of device timer. This is measured in nanoseconds.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_QUEUE_PROPERTIES</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_command_queue_properties</p>
     *                     <p>Describes the command-queue properties supported by the device.
     *                       This is a bit-field that describes one or more of the following values:
     *                     </p>
     *                     <p>CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE</p>
     *                     <p>CL_QUEUE_PROFILING_ENABLE</p>
     *                     <p>These properties are described in the table for
     *                       <span><span>clCreateCommandQueue</span></span>. The mandated minimum capability is CL_QUEUE_PROFILING_ENABLE.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_SINGLE_FP_CONFIG</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_device_fp_config</p>
     *                     <p>Describes single precision floating-point capability of the device. This is a bit-field that describes one or more of the following values:</p>
     *                     <p>CL_FP_DENORM - denorms are supported</p>
     *                     <p>CL_FP_INF_NAN - INF and quiet NaNs are supported</p>
     *                     <p>CL_FP_ROUND_TO_NEAREST - round to nearest even rounding mode supported</p>
     *                     <p>CL_FP_ROUND_TO_ZERO - round to zero rounding mode supported</p>
     *                     <p>CL_FP_ROUND_TO_INF - round to +ve and -ve infinity rounding modes supported</p>
     *                     <p>CL_FP_FMA - IEEE754-2008 fused multiply-add is supported</p>
     *                     <p>CL_FP_SOFT_FLOAT - Basic floating-point operations (such as addition, subtraction, multiplication) are implemented in software.</p>
     *                     <p>The mandated minimum floating-point capability is CL_FP_ROUND_TO_NEAREST | CL_FP_INF_NAN.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_device_type</p>
     *                     <p>The OpenCL device type. Currently supported values are one of or a combination of: CL_DEVICE_TYPE_CPU, CL_DEVICE_TYPE_GPU, CL_DEVICE_TYPE_ACCELERATOR, or CL_DEVICE_TYPE_DEFAULT.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_VENDOR</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: char[]</p>
     *                     <p>Vendor name string.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_VENDOR_ID</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: cl_uint</p>
     *                     <p>A unique device vendor identifier. An example of a unique device identifier could be the PCIe ID.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_VERSION</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: char[]</p>
     *                     <p>OpenCL version string. Returns the OpenCL version supported by the
     *                       device. This version string has the following format:
     *                     </p>
     *                     <p><code>OpenCL&lt;space&gt;&lt;major_version.minor_version&gt;&lt;space&gt;&lt;vendor-specific information&gt;</code></p>
     *                     <p>The <code>major_version.minor_version</code> value returned will be 1.1.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DRIVER_VERSION</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>Return type: char[]</p>
     *                     <p>OpenCL software driver version string in the form  <code>major_number.minor_number</code>.</p>
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       CL_DEVICE_PROFILE: The platform profile returns the profile that is implemented by the OpenCL framework. If the platform profile returned is FULL_PROFILE, the OpenCL framework will support devices that are FULL_PROFILE and may also support devices that are EMBEDDED_PROFILE. The compiler must be available for all devices i.e. CL_DEVICE_COMPILER_AVAILABLE is CL_TRUE. If the platform profile returned is EMBEDDED_PROFILE, then devices that are only EMBEDDED_PROFILE are supported.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p><code>clGetDeviceInfo</code> returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns the following:</p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_DEVICE</span> if <code>device</code> is not valid.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not one of the supported values or if size in
     *           bytes specified by <code>param_value_size</code> is less than size of return type as shown in the table above and
     *           <code>param_value</code> is not a <span>NULL</span> value.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetDeviceInfo(cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetDeviceInfoNative(device, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetDeviceInfoNative(cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>Creates an OpenCL context.</p>
     *
     * <div title="clCreateContext">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_context <b>clCreateContext</b>(</code>
     *           <td>const cl_context_properties <var>*properties</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_devices</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_device_id <var>*devices</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>
     *             (voidCL_CALLBACK  <var>*pfn_notify)</var>
     *             <code>(</code>
     *             <div>
     *               <p>const char *errinfo, <br />
     *                                         const void *private_info, size_t cb, <br />
     *                                         void *user_data
     *               </p>
     *             </div>
     *             <code>)</code>,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*user_data</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Description">
     *     <h2>Description</h2>
     *     <p>
     *       An OpenCL context is created with one or more devices. Contexts
     *       are used by the OpenCL runtime for managing objects such as command-queues, memory,
     *       program and kernel objects and for executing kernels on one or more devices specified in the
     *       context.
     *     </p>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>properties</span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies a list of context property names and their corresponding values. Each property name is
     *             immediately followed by the corresponding desired value. The list is terminated with 0.
     *             <code>properties</code> can be NULL in which case the platform that is selected is
     *             implementation-defined. The list of supported <code>properties</code> is described in the table below.
     *           </p>
     *           <p>
     *             If the extension <span><span>cl_khr_d3d10_sharing</span></span>
     *             is enabled, then if a property is not specified in <code>properties</code>, then its default value is used
     *             (it is said to be specified implicitly). If <code>properties</code> is NULL or empty (points to a list
     *             whose first value is zero), all attributes take on their default value.
     *           </p>
     *           <p>
     *             If the extension <span><span>cl_khr_gl_sharing</span></span>
     *             is enabled, then <code>properties</code> points to an attribute list, which is a array of ordered &lt;attribute name, value&gt; pairs terminated with zero. If an attribute is not specified in <code>properties</code>, then its default value is used (it is said to be specified implicitly). If <code>properties</code> is NULL or empty (points to a list whose first value is zero), all attributes take on their default values..
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_context_properties enum</th>
     *                   <th align="left">Property value</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_PLATFORM</code>
     *                   </td>
     *                   <td align="left">
     *                     cl_platform_id
     *                   </td>
     *                   <td align="left">Specifies the platform to use.</td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_D3D10_DEVICE_KHR</code>
     *                   </td>
     *                   <td align="left">ID3D10Device*</td>
     *                   <td align="left">If the <span><span>cl_khr_d3d10_sharing</span></span>
     *                     extension is enabled, specifies the ID3D10Device* to use for Direct3D 10 interoperability. The default value is NULL.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <div>
     *                       <p><code>CL_GL_CONTEXT_KHR</code><br />
     *                         <code>CL_EGL_DISPLAY_KHR</code><br />
     *                         <code>CL_GLX_DISPLAY_KHR</code><br />
     *                         <code>CL_WGL_HDC_KHR</code><br />
     *                         <code>CL_CGL_SHAREGROUP_KHR</code>
     *                       </p>
     *                     </div>
     *                   </td>
     *                   <td align="left"> </td>
     *                   <td align="left">Available if the <span><span>cl_khr_gl_sharing</span></span>
     *                     extension is enabled.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>num_devices</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of devices specified in the <code>devices</code> argument.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>devices</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a list of unique devices returned by <span><span>clGetDeviceIDs</span></span>
     *             for a platform. Duplicate devices specified in <code>devices</code> are ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>pfn_notify</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A callback function that can be registered by the application. This callback function will be used by the OpenCL implementation to report information on errors that occur in this context. This callback function may be called asynchronously by the OpenCL implementation.
     *             It is the application's responsibility to ensure that the callback function is thread-safe. If <code>pfn_notify</code> is NULL, no callback function is registered. The parameters to this callback function are:
     *           </p>
     *           <p><code>errinfo</code> is a pointer to an error string.</p>
     *           <p><code>private_info</code> and <code>cb</code> represent a pointer to binary data that is returned by the OpenCL implementation that can be used to log additional information helpful in debugging the error.</p>
     *           <p><code>user_data</code> is a pointer to user supplied data.</p>
     *           <p>NOTE: There are a number of cases where error notifications need to be delivered due to an error that occurs outside a context. Such notifications may not be delivered through the <code>pfn_notify</code> callback. Where these notifications go is implementation-defined.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>user_data</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Passed as the <code>user_data</code> argument when <code>pfn_notify</code> is called. <code>user_data</code> can be NULL.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>errcode_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <code>clCreateContext</code> and <span><span>clCreateContextFromType</span></span> perform an implicit retain. This is very helpful for 3rd party libraries, which typically get a context passed to them by the application.
     *       However, it is possible that the application may delete the context without informing the library.
     *       Allowing functions to attach to (i.e. retain) and release a context solves the problem of a context
     *       being used by a library no longer being valid.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p><code>clCreateContext</code> returns a valid non-zero context and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span> if the context is created  successfully. Otherwise, it returns NULL value with the following error
     *       values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PLATFORM</span> if <code>properties</code> is NULL and no platform could be selected or if platform value specified in <code>properties</code> is not a valid platform. (If the extension <span><span>cl_khr_gl_sharing</span></span> is enabled, then see error "CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR" below.)
     *         </li>
     *         <li><span>CL_INVALID_PROPERTY</span> if context property name in <code>properties</code> is not a supported
     *           property name, if the value specified for a supported property name is not valid, or if the
     *           same property name is specified more than once. However if the extension <span><span>cl_khr_gl_sharing</span></span> is enabled, then CL_INVALID_PROPERTY is returned if an attribute name other than those listed in the table for <code>properties</code> above is specified in <code>properties</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>devices</code> is NULL; if <code>num_devices</code> is equal to zero; or
     *           if <code>pfn_notify</code> is NULL but <code>user_data</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_DEVICE</span> if <code>devices</code> contains an invalid device.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if Direct3D 10 interoperability is specified by setting CL_INVALID_D3D10_DEVICE_KHR
     *           to a non-NULL value, and interoperability with another graphics API is also specified (if the
     *           <span><span>cl_khr_d3d10_sharing</span></span> extension is enabled).
     *         </li>
     *         <li><span>CL_DEVICE_NOT_AVAILABLE</span> if a device in <code>devices</code> is currently not available
     *           even though the device was returned by <span><span>clGetDeviceIDs</span></span>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the OpenCL
     *           implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL
     *           implementation on the host.
     *         </li>
     *         <li><span>CL_INVALID_D3D10_DEVICE_KHR</span> if the Direct3D 10 device specified for interoperability is not
     *           compatible with the devices against which the context is to be created (if the
     *           <span><span>cl_khr_d3d10_sharing</span></span> extension is enabled).
     *         </li>
     *         <li><span>CL_INVALID_D3D10_DEVICE_KHR</span> if the value of the property CL_CONTEXT_D3D10_DEVICE_KHR is non-NULL
     *           and does not specify a valid Direct3D 10 device with which the <code>cl_device_ids</code> against which this context
     *           is to be created may interoperate (if the
     *           <span><span>cl_khr_d3d10_sharing</span></span> extension is enabled).
     *         </li>
     *         <li><span>CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR</span> when an invalid OpenGL context or share group
     *           object handle is specified in <code>properties</code> (only if the
     *           <span><span>cl_khr_gl_sharing</span></span> extension is enabled).
     *         </li>
     *         <li>
     *           <span>CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR</span> if the
     *           <span><span>cl_khr_gl_sharing</span></span> extension is enabled and if a context was specified by any of the following means:
     *           <div>
     *             <ul type="disc">
     *               <li>
     *                 Context specified for an EGL-based OpenGL ES or OpenGL implementation by
     *                 setting the attributes CL_GL_CONTEXT_KHR and CL_EGL_DISPLAY_KHR.
     *               </li>
     *               <li>
     *                 Context was specified for a GLX-based OpenGL implementation by setting the
     *                 attributes CL_GL_CONTEXT_KHR and CL_GLX_DISPLAY_KHR.
     *               </li>
     *               <li>
     *                 Context was specified for a WGL-based OpenGL implementation by setting the
     *                 attributes CL_GL_CONTEXT_KHR and CL_WGL_HDC_KHR.
     *               </li>
     *             </ul>
     *           </div>
     *           and any of the following conditions hold:
     *           <div>
     *             <ul type="disc">
     *               <li>
     *                 The specified display and context attributes do not identify a valid OpenGL or
     *                 OpenGL ES context.
     *               </li>
     *               <li>
     *                 The specified context does not support buffer and renderbuffer objects.
     *               </li>
     *               <li>
     *                 The specified context is not compatible with the OpenCL context being
     *                 created (for example, it exists in a physically distinct address space,
     *                 such as another hardware device, or does not support sharing data with OpenCL
     *                 due to implementation restrictions).
     *               </li>
     *             </ul>
     *           </div>
     *         </li>
     *         <li><span>CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR</span> if a share group was specified
     *           for a CGL-based OpenGL implementation by setting the attribute CL_CGL_SHAREGROUP_KHR, and the
     *           specified share group does not identify a valid CGL share group object (only if the
     *           <span><span>cl_khr_gl_sharing</span></span> extension is enabled).
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_context clCreateContext(cl_context_properties properties, int num_devices, cl_device_id devices[], CreateContextFunction pfn_notify, Object user_data, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_context result = clCreateContextNative(properties, num_devices, devices, pfn_notify, user_data, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_context result = clCreateContextNative(properties, num_devices, devices, pfn_notify, user_data, errcode_ret);
            return result;
        }
    }

    private static native cl_context clCreateContextNative(cl_context_properties properties, int num_devices, cl_device_id devices[], CreateContextFunction pfn_notify, Object user_data, int errcode_ret[]);

    /**
     * <p>
     *       Create an OpenCL context from a device type that identifies the specific device(s) to use.
     * </p>
     *
     * <div title="clCreateContextFromType">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_context
     *             <b>
     *             clCreateContextFromType
     *             </b>
     *             (</code>
     *           <td>const cl_context_properties
     *              <var> *properties</var>,
     *           </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_device_type
     *              <var>device_type</var>,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void
     *              <var>(CL_CALLBACK *pfn_notify)
     *             (const char *errinfo</var>,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const void
     *              <var>*private_info</var>,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t
     *              <var>cb</var>,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void
     *              <var>*user_data)</var>,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void
     *              <var>*user_data</var>,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int
     *              <var>*errcode_ret</var><code>)</code>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>properties</span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies a list of context property names and their corresponding values. Each property name is immediately followed by the corresponding desired value. The list is terminated with 0. <code>properties</code> can be NULL in which case the platform that is selected is implementation-defined. The list of supported properties is described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_context_properties enum</th>
     *                   <th align="left">Property value</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_PLATFORM</code>
     *                   </td>
     *                   <td align="left">
     *                     cl_platform_id
     *                   </td>
     *                   <td align="left">Specifies the platform to use.</td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_D3D10_DEVICE_KHR</code>
     *                   </td>
     *                   <td align="left">ID3D10Device*</td>
     *                   <td align="left">If the <span><span>cl_khr_d3d10_sharing</span></span>
     *                     extension is enabled, specifies the ID3D10Device* to use for Direct3D 10 interoperability. The default value is NULL.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <div>
     *                       <p><code>CL_GL_CONTEXT_KHR</code><br />
     *                         <code>CL_EGL_DISPLAY_KHR</code><br />
     *                         <code>CL_GLX_DISPLAY_KHR</code><br />
     *                         <code>CL_WGL_HDC_KHR</code><br />
     *                         <code>CL_CGL_SHAREGROUP_KHR</code>
     *                       </p>
     *                     </div>
     *                   </td>
     *                   <td align="left"> </td>
     *                   <td align="left">Available if the <span><span>cl_khr_gl_sharing</span></span>
     *                     extension is enabled.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>device_type</span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that identifies the type of device and is described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_device_type</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_CPU</code>
     *                   </td>
     *                   <td align="left">An OpenCL device that is the host processor. The host
     *                     processor runs the OpenCL implementations and is a
     *                     single or multi-core CPU.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_GPU</code>
     *                   </td>
     *                   <td align="left">An OpenCL device that is a GPU. By this we mean that
     *                     the device can also be used to accelerate a 3D API such
     *                     as OpenGL or DirectX.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_ACCELERATOR</code>
     *                   </td>
     *                   <td align="left">Dedicated OpenCL accelerators (for example the IBM
     *                     CELL Blade). These devices communicate with the host
     *                     processor using a peripheral interconnect such as PCIe.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_DEFAULT</code>
     *                   </td>
     *                   <td align="left">The default OpenCL device in the system.</td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_DEVICE_TYPE_ALL</code>
     *                   </td>
     *                   <td align="left">All OpenCL devices available in the system.</td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>pfn_notify</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A callback function that can be registered by the application. This callback function will be used by the OpenCL implementation to report information on errors that occur in this context. This callback function may be called asynchronously by the OpenCL implementation.
     *             It is the application's responsibility to ensure that the callback function is thread-safe. If <code>pfn_notify</code> is NULL, no callback function is registered. The parameters to this callback function are:
     *           </p>
     *           <p><code>errinfo</code> is a pointer to an error string.</p>
     *           <p><code>private_info</code> and <code>cb</code> represent a pointer to binary data that is returned by the OpenCL implementation that can be used to log additional information helpful in debugging the error.</p>
     *           <p><code>user_data</code> is a pointer to user supplied data.</p>
     *           <p>
     *             There are a number of cases where error notifications need to be delivered due to an error that occurs outside a context. Such notifications may not be delivered through the <code>pfn_notify</code> callback. Where these notifications go is implementation-defined.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>user_data</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Passed as the <code>user_data</code> argument when <code>pfn_notify</code> is called. user_data can be NULL.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Return an appropriate error code. If errcode_ret is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <code>clCreateContextFromType</code> may return all or a subset of the actual physical devices present in the platform and that match <code>device_type</code>.
     *     </p>
     *     <p>
     *       <code>clCreateContextFromType</code> and <span><span>clCreateContext</span></span> perform an implicit retain. This is very helpful for 3rd party libraries, which typically get a context passed to them by the application.
     *       However, it is possible that the application may delete the context without informing the library.
     *       Allowing functions to attach to (i.e. retain) and release a context solves the problem of a context
     *       being used by a library no longer being valid.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clCreateContextFromType</code> returns a valid non-zero context and <code>errcode_ret</code> is set
     *       to <span>CL_SUCCESS</span> if the context is created successfully. Otherwise, it returns a NULL value with the following
     *       error vlaues returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PLATFORM</span> if <code>properties</code> is NULL and no platform could be selected or
     *           if platform value specified in <code>properties</code> is not a valid platform. (If the extension <span><span>cl_khr_gl_sharing</span></span> is enabled, then see error "CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR" below.)
     *         </li>
     *         <li><span>CL_INVALID_PROPERTY</span> if context property name in <code>properties</code> is not a supported property
     *           name, or if the value specified for a supported property name is not valid, or if the same property name is specified more than once.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>pfn_notify</code> is NULL but <code>user_data</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_DEVICE_TYPE</span> if <code>device_type</code> is not a valid value.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if Direct3D 10 interoperability is specified by setting CL_INVALID_D3D10_DEVICE_KHR
     *           to a non-NULL value, and interoperability with another graphics API is also specified (if the
     *           <span><span>cl_khr_d3d10_sharing</span></span> extension is enabled).
     *         </li>
     *         <li><span>CL_DEVICE_NOT_AVAILABLE</span> if no devices that match <code>device_type</code> and property values
     *           specified in <code>properties</code> are currently available.
     *         </li>
     *         <li><span>CL_DEVICE_NOT_FOUND</span> if no devices that match <code>device_type</code> and property values
     *           specified in <code>properties</code> were found.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the OpenCL
     *           implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *         <li><span>CL_INVALID_D3D10_DEVICE_KHR</span> if the Direct3D 10 device specified for interoperability is not
     *           compatible with the devices against which the context is to be created (if the
     *           <span><span>cl_khr_d3d10_sharing</span></span> extension is enabled).
     *         </li>
     *         <li><span>CL_INVALID_D3D10_DEVICE_KHR</span> if the value of the property CL_CONTEXT_D3D10_DEVICE_KHR is non-NULL
     *           and does not specify a valid Direct3D 10 device with which the <code>cl_device_ids</code> against which this context
     *           is to be created may interoperate (if the
     *           <span><span>cl_khr_d3d10_sharing</span></span> extension is enabled).
     *         </li>
     *         <li><span>CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR</span> when an invalid OpenGL context or share group
     *           object handle is specified in <code>properties</code> if the <span><span>cl_khr_gl_sharing</span></span> extension is enabled.
     *         </li>
     *         <li>
     *           <span>CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR</span> if the
     *           <span><span>cl_khr_gl_sharing</span></span> extension is enabled and if a context was specified by any of the following means:
     *           <div>
     *             <ul type="disc">
     *               <li>
     *                 Context specified for an EGL-based OpenGL ES or OpenGL implementation by
     *                 setting the attributes CL_GL_CONTEXT_KHR and CL_EGL_DISPLAY_KHR.
     *               </li>
     *               <li>
     *                 Context was specified for a GLX-based OpenGL implementation by setting the
     *                 attributes CL_GL_CONTEXT_KHR and CL_GLX_DISPLAY_KHR.
     *               </li>
     *               <li>
     *                 Context was specified for a WGL-based OpenGL implementation by setting the
     *                 attributes CL_GL_CONTEXT_KHR and CL_WGL_HDC_KHR.
     *               </li>
     *             </ul>
     *           </div>
     *           and any of the following conditions hold:
     *           <div>
     *             <ul type="disc">
     *               <li>
     *                 The specified display and context attributes do not identify a valid OpenGL or
     *                 OpenGL ES context.
     *               </li>
     *               <li>
     *                 The specified context does not support buffer and renderbuffer objects.
     *               </li>
     *               <li>
     *                 The specified context is not compatible with the OpenCL context being
     *                 created (for example, it exists in a physically distinct address space,
     *                 such as another hardware device, or does not support sharing data with OpenCL
     *                 due to implementation restrictions).
     *               </li>
     *             </ul>
     *           </div>
     *         </li>
     *         <li><span>CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR</span> if a share group was specified
     *           for a CGL-based OpenGL implementation by setting the attribute CL_CGL_SHAREGROUP_KHR, and the
     *           specified share group does not identify a valid CGL share group object (only if the
     *           <span><span>cl_khr_gl_sharing</span></span> extension is enabled).
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Example">
     *     <h3>
     *       Example
     *     </h3>
     *     <div>
     *       <table border="0">
     *         <colgroup>
     *           <col align="left" />
     *         </colgroup>
     *         <tbody>
     *           <tr>
     *             <td align="left">Example goes here - it will be set in "code" type with white space preserved.</td>
     *           </tr>
     *         </tbody>
     *       </table>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_context clCreateContextFromType(cl_context_properties properties, long device_type, CreateContextFunction pfn_notify, Object user_data, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_context result = clCreateContextFromTypeNative(properties, device_type, pfn_notify, user_data, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_context result = clCreateContextFromTypeNative(properties, device_type, pfn_notify, user_data, errcode_ret);
            return result;
        }
    }

    private static native cl_context clCreateContextFromTypeNative(cl_context_properties properties, long device_type, CreateContextFunction pfn_notify, Object user_data, int errcode_ret[]);

    /**
     * <p>
     *     Increment the context reference count.
     * </p>
     *
     * <div title="clRetainContext">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>
     *             clRetainContext
     *             </b>
     *             (</code>
     *           <td>cl_context <var>context</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The context to retain.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <span><span>clCreateContext</span></span> and
     *       <span><span>clCreateContextFromType</span></span> perform an implicit retain. This is very
     *       helpful for 3rd party libraries, which typically get a context passed to them by the application.
     *       However, it is possible that the application may delete the context without informing the library.
     *       Allowing functions to attach to (i.e. retain) and release a context solves the problem of a context
     *       being used by a library no longer being valid.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following values:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid OpenCL context.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clRetainContext(cl_context context)
    {
        return checkResult(clRetainContextNative(context));
    }

    private static native int clRetainContextNative(cl_context context);

    /**
     * <p>
     *     Decrement the context reference count.
     * </p>
     *
     * <div title="clReleaseContext">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>
     *             clReleaseContext
     *             </b>
     *             (</code>
     *           <td>cl_context <var>context</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The context to release.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       After the <code>context</code> reference count becomes zero and all the objects attached to <code>context</code> (such as
     *       memory objects, command-queues) are released, the <code>context</code> is deleted.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following values:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid OpenCL context.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clReleaseContext(cl_context context)
    {
        return checkResult(clReleaseContextNative(context));
    }

    private static native int clReleaseContextNative(cl_context context);

    /**
     * <p>
     *       Query information about a context.
     *   </p>
     *
     * <div title="clGetContextInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>
     *             clGetContextInfo
     *             </b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_context_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t * <var>param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the OpenCL context being queried.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             An enumeration constant that specifies the information to query.
     *             The valid values for <code>param_name</code> are:
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_context_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Information returned in param_value</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_REFERENCE_COUNT</code>
     *                   </td>
     *                   <td align="left">cl_uint</td>
     *                   <td align="left">
     *                     Return the <code>context</code> reference count. The reference count returned should be
     *                     considered immediately stale. It is unsuitable for general use in applications. This feature is provided
     *                     for identifying memory leaks.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_NUM_DEVICES</code>
     *                   </td>
     *                   <td align="left">cl_uint</td>
     *                   <td align="left">
     *                     Return the number of devices in <code>context</code>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_DEVICES</code>
     *                   </td>
     *                   <td align="left">cl_device_id[]</td>
     *                   <td align="left">
     *                     Return the list of devices in <code>context</code>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_PROPERTIES</code>
     *                   </td>
     *                   <td align="left">cl_context_properties[]</td>
     *                   <td align="left">
     *                     Return the <code>properties</code> argument specified
     *                     in <span><span>clCreateContext</span></span> or
     *                     <span><span>clCreateContextFromType</span></span>.
     *                     <p>If the <code>properties</code> argument specified in
     *                       <span><span>clCreateContext</span></span> or
     *                       <span><span>clCreateContextFromType</span></span> used
     *                       to create <code>context</code> is not NULL, the implementation must return the values
     *                       specified in the <code>properties</code> argument.
     *                     </p>
     *                     <p>
     *                       If the <code>properties</code> argument specified in
     *                       <span><span>clCreateContext</span></span> or
     *                       <span><span>clCreateContextFromType</span></span> used to create
     *                       <code>context</code> is NULL, the implementation may return either a
     *                       <code>param_value_size_ret</code>
     *                       of 0, i.e. there is no context property value to be returned or can return a context property
     *                       value of 0 (where 0 is used to terminate the context properties list) in the memory that
     *                       <code>param_value</code> points to.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_CONTEXT_D3D10_PREFER_SHARED_RESOURCES_KHR</code>
     *                   </td>
     *                   <td align="left">cl_bool</td>
     *                   <td align="left">
     *                     If the
     *                     <span><span>cl_khr_d3d10_sharing</span></span>
     *                     extension is enabled, returns <span>CL_TRUE</span> if Direct3D 10 resources created as
     *                     shared by setting <code>MiscFlags</code> to include D3D10_RESOURCE_MISC_SHARED will perform if
     *                     aster when shared with OpenCL, compared with resources which have not set this flag. Otherwise
     *                     returns <span>CL_FALSE</span>.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned. If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the size in bytes of memory pointed to by <code>param_value</code>. This size must be greater than or equal to the size of return type as described in the table above.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data being queried by <code>param_value</code>. If
     *             <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function executed successfully, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code>
     *           is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not one of the supported values or if size in
     *           bytes specified by <code>param_value_size</code> is less than size of return type as specified in the table above
     *           and <code>param_value</code> is not a <span>NULL</span> value.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetContextInfo(cl_context context, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetContextInfoNative(context, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetContextInfoNative(cl_context context, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>Create a command-queue on a specific device.</p>
     *
     * <div title="clCreateCommandQueue">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_command_queue <b>clCreateCommandQueue</b>(</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_device_id <var>device</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_command_queue_properties <var>properties</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>context</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>device</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a device associated with <code>context</code>. It can either be in the list of devices specified when <code>context</code> is created using <span><span>clCreateContext</span></span> or have the same device type as the device type specified when the <code>context</code> is created using <span><span>clCreateContextFromType</span></span>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>properties</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies a list of properties for the command-queue. This is a bit-field. Only command-queue properties specified in the table below can be set in
     *             <code>properties</code>; otherwise the value specified in <code>properties</code> is considered to be not valid.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">Command-Queue Properties</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE</code>
     *                   </td>
     *                   <td align="left">Determines whether the commands queued in the command-queue are executed in-order or out-of-order. If set, the commands in the command-queue are executed out-of-order. Otherwise, commands are executed in-order.</td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_QUEUE_PROFILING_ENABLE</code>
     *                   </td>
     *                   <td align="left">Enable or disable profiling of commands in the command-queue. If set, the profiling of commands is enabled. Otherwise profiling of commands is disabled. See  <span><span>clGetEventProfilingInfo</span></span> for more information.</td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>errcode_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is <code>NULL</code>, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>The OpenCL functions that are submitted to a command-queue are enqueued in the order the
     *       calls are made but can be configured to execute in-order or out-of-order. The <code>properties</code>
     *       argument in <code>clCreateCommandQueue</code> can be used to specify the execution order.
     *     </p>
     *     <p>
     *       If the CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE property of a command-queue is not set, the commands enqueued to a command-queue execute in order. For example, if an application calls <span><span>clEnqueueNDRangeKernel</span></span> to execute kernel A followed by a <span><span>clEnqueueNDRangeKernel</span></span> to execute kernel B, the application can assume that kernel A finishes first and then kernel B is executed. If the memory objects output by kernel A are inputs to kernel B then kernel B will see the correct data in memory objects produced by execution of kernel A. If the CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE property of a commandqueue is set, then there is no guarantee that kernel A will finish before kernel B starts execution.
     *     </p>
     *     <p>
     *       Applications can configure the commands enqueued to a command-queue to execute out-of-order by setting the CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE property of the command-queue. This can be specified when the command-queue is created. In out-of-order execution mode there is no guarantee that the enqueued commands will finish execution in the order they were queued. As there is no guarantee that kernels will be executed in order, that is based on when the <span><span>clEnqueueNDRangeKernel</span></span> calls are made within a command-queue, it is therefore possible that an earlier <span><span>clEnqueueNDRangeKernel</span></span> call to execute kernel A identified by event A may execute and/or finish later than a <span><span>clEnqueueNDRangeKernel</span></span> call to execute kernel B which was called by the application at a later point in time. To guarantee a specific order of execution of kernels, a wait on a particular event (in this case event A) can be used. The wait for event A can be specified in the <code>event_wait_list</code> argument to clEnqueueNDRangeKernel for kernel B.
     *     </p>
     *     <p>
     *       In addition, a wait for events or a barrier command can be enqueued to the command-queue. The wait for events command ensures that previously enqueued commands identified by the list of events to wait for have finished before the next batch of commands is executed. The barrier command ensures that all previously enqueued commands in a command-queue have finished execution before the next batch of commands is executed.
     *     </p>
     *     <p>
     *       Similarly, commands to read, write, copy or map memory objects that are enqueued after
     *       <span><span>clEnqueueNDRangeKernel</span></span>, <span><span>clEnqueueTask</span></span> or <span><span>clEnqueueNativeKernel</span></span> commands are not
     *       guaranteed to wait for kernels scheduled for execution to have completed (if the
     *       CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE property is set). To ensure correct
     *       ordering of commands, the event object returned by  <span><span>clEnqueueNDRangeKernel</span></span>,
     *       clEnqueueTask or <span><span>clEnqueueNativeKernel</span></span> can be used to enqueue a wait for event or a barrier command can be enqueued that must complete before reads or writes to the memory object(s) occur.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p><code>clCreateCommandQueue</code> returns a valid non-zero command-queue and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span> if the command-queue is created successfully. Otherwise, it returns a NULL value with one of the following error values returned in <code>errcode_ret</code>:</p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_DEVICE</span> if <code>device</code> is not a valid device or is
     *           not associated with <code>context</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>properties</code>
     *           are not valid.
     *         </li>
     *         <li><span>CL_INVALID_QUEUE_PROPERTIES</span> if values specified in <code>properties</code>
     *           are valid but are not supported by the device.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_command_queue clCreateCommandQueue(cl_context context, cl_device_id device, long properties, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_command_queue result = clCreateCommandQueueNative(context, device, properties, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_command_queue result = clCreateCommandQueueNative(context, device, properties, errcode_ret);
            return result;
        }
    }

    private static native cl_command_queue clCreateCommandQueueNative(cl_context context, cl_device_id device, long properties, int errcode_ret[]);

    /**
     * <p>Increments the <code>command_queue</code> reference count.</p>
     *
     * <div title="clRetainCommandQueue">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clRetainCommandQueue</b>(</code>
     *           <td>cl_command_queue <var>command_queue</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>command_queue</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the command-queue to retain.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <code>clCreateCommandQueue</code> performs an implicit retain. This is very helpful for 3rd party libraries, which typically get a command-queue passed to them by the application. However, it is possible that the application may delete the command-queue without informing the library. Allowing functions to attach to (i.e. retain) and release a command-queue solves the problem of a command-queue being used by a library no longer being valid.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function executed successfully, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clRetainCommandQueue(cl_command_queue command_queue)
    {
        return checkResult(clRetainCommandQueueNative(command_queue));
    }

    private static native int clRetainCommandQueueNative(cl_command_queue command_queue);

    /**
     * <p>Decrements the <code>command_queue</code> reference count.</p>
     *
     * <div title="clReleaseCommandQueue">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clReleaseCommandQueue</b>(</code>
     *           <td>cl_command_queue <var>command_queue</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>command_queue</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the command-queue to release.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       After the <code>command_queue</code> reference count becomes zero and all commands queued to
     *       <code>command_queue</code> have finished (e.g., kernel executions, memory object updates, etc.), the command-queue is deleted.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command queue.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the function is executed successfully. It returns <span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.</p>
     *   </div>
     * </div>
     */
    public static int clReleaseCommandQueue(cl_command_queue command_queue)
    {
        return checkResult(clReleaseCommandQueueNative(command_queue));
    }

    private static native int clReleaseCommandQueueNative(cl_command_queue command_queue);

    /**
     * <p>Query information about a command-queue.</p>
     *
     * <div title="clGetCommandQueueInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clGetCommandQueueInfo</b>(</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_command_queue_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>command_queue</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the command-queue being queried.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_name</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the information to query.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the size in bytes of memory pointed to by <code>param_value</code>. This size must be greater than or equal to the size of return type as described in the table below. If <code>param_value</code> is NULL, it is ignored.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A pointer to memory where the appropriate result being queried is returned. If <code>param_value</code> is NULL, it is ignored.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Returns the actual size in bytes of data being queried by <code>param_value</code>. If <code>param_value_size_ret</code> is NULL, it is ignored</p>
     *         </dd>
     *         <dt>
     *           <span></span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The list of supported <code>param_name</code> values and the information returned in <code>param_value</code> by <code>clGetCommandQueueInfo</code> is described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_command_queue_info</th>
     *                   <th align="left">Return Type and Information returned in param_value</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_QUEUE_CONTEXT</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_context
     *                     <p>Return the context specified when the command-queue is created.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_QUEUE_DEVICE</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_device_id
     *                     <p>Return the device specified when the command-queue is created.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_QUEUE_REFERENCE_COUNT</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_uint
     *                     <p>Return the command-queue reference count.</p>
     *                     <p>
     *                       The reference count returned with <code>CL_QUEUE_REFERENCE_COUNT</code> should be considered immediately stale. It is unsuitable for general use in applications. This feature is provided for identifying memory leaks.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_QUEUE_PROPERTIES</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_command_queue_properties
     *                     <p>Return the currently specified properties for the command-queue. These properties are specified by the properties argument in <span><span>clCreateCommandQueue</span></span>.</p>
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       It is possible that a device(s) becomes unavailable after a context and command-queues that use
     *       this device(s) have been created and commands have been queued to command-queues. In this
     *       case the behavior of OpenCL API calls that use this context (and command-queues) are
     *       considered to be implementation-defined. The user callback function, if specified, when the
     *       context is created can be used to record appropriate information in the <code>errinfo</code>, <code>private_info</code> arguments passed to the callback function when the device becomes unavailable.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns the following:</p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           Returns <span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li>
     *           Returns <span>CL_INVALID_VALUE</span> if <code>param_name</code> is not one of the supported values or if size
     *           in bytes specified by <code>param_value_size</code> is less than size of return type and <code>param_value</code> is not a NULL value.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetCommandQueueInfo(cl_command_queue command_queue, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetCommandQueueInfoNative(command_queue, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetCommandQueueInfoNative(cl_command_queue command_queue, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>Enable or disable the properties of a command-queue.</p>
     *
     * <div title="clSetCommandQueueProperty">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int <b>clSetCommandQueueProperty</b>(</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_command_queue_properties <var>properties</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>enable</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_command_queue_properties <var>*old_properties</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <p>
     *       THIS FUNCITON IS DEPRECATED.
     *     </p>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>command_queue</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the command-queue being queried.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>properties</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the new command-queue properties to be applied to <code>command_queue</code>. Only command-queue properties specified for <span><span>clCreateCommandQueue</span></span> can be set in <code>properties</code>; otherwise the value specified in <code>properties</code> is considered to be not valid.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>enable</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Determines whether the values specified by <code>properties</code> are enabled (if <code>enable</code> is <code>CL_TRUE</code>) or disabled (if <code>enable</code> is <code>CL_FALSE</code>) for the command-queue. The allowed property values are the same as those specified for <span><span>clCreateCommandQueue</span></span>.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>old_properties</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Returns the command-queue properties before they were changed by
     *             <code>clSetCommandQueueProperty</code>. If <code>old_properties</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       As specified for  <span><span>clCreateCommandQueue</span></span>, the <code>CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE</code> command-queue property determines whether the commands in a command-queue are executed in-order or out-of-order. Changing this command-queue property will cause the OpenCL implementation to block until all previously queued commands in <code>command_queue</code> have completed. This can be an expensive operation and therefore changes to the <code>CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE</code> property should be only done when absolutely necessary.
     *     </p>
     *     <p>
     *       It is possible that a device(s) becomes unavailable after a context and command-queues that use this device(s) have been created and commands have been queued to command-queues. In this
     *       case the behavior of OpenCL API calls that use this context (and command-queues) are
     *       considered to be implementation-defined. The user callback function, if specified when the
     *       context is created, can be used to record appropriate information in the <code>errinfo</code>, <code>private_info</code> arguments passed to the callback function when the device becomes unavailable.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the command-queue properties are successfully updated. Otherwise, it returns the following:</p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           Returns <span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li>
     *           Returns <span>CL_INVALID_VALUE</span> if the values specified in  <code>properties</code> are not valid.
     *         </li>
     *         <li>
     *           Returns <span>CL_INVALID_QUEUE_PROPERTIES</span> if the values specified in  <code>properties</code> are not supported by the device.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     *
     * @deprecated This API introduces mutable state into the OpenCL implementation.
     * It has been REMOVED to better facilitate thread safety.  The 1.0 API is not
     * thread safe. It is not tested by the OpenCL 1.1 conformance test, and consequently
     * may not work or may not work dependably. <br />
     *  It is likely to be non-performant. Use of this API is not advised.
     * Use at your own risk.<br />
     * <br />
     * Software developers previously relying on this API are instructed to set the
     * command queue properties when creating the queue, instead.
     */
    public static int clSetCommandQueueProperty(cl_command_queue command_queue, long properties, boolean enable, long old_properties[])
    {
        return checkResult(clSetCommandQueuePropertyNative(command_queue, properties, enable, old_properties));
    }

    private static native int clSetCommandQueuePropertyNative(cl_command_queue command_queue, long properties, boolean enable, long old_properties[]);

    /**
     * <p>
     *       Creates a buffer object.
     * </p>
     *
     * <div title="clCreateBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_mem <b>clCreateBuffer</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*host_ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>context</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A valid OpenCL context used to create the buffer object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>flags</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify allocation and usage information such as the memory
     *             arena that should be used to allocate the buffer object and how it will be used. The
     *             following table describes the possible values for <code>flags</code>:
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_mem_flags</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_READ_WRITE</code>
     *                   </td>
     *                   <td align="left">
     *                     This flag specifies that the memory object will be read and
     *                     written by a kernel. This is the default.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_WRITE_ONLY</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flags specifies that the memory object will be written
     *                       but not read by a kernel.
     *                     </p>
     *                     <p>Reading from a buffer or image object created with
     *                       <code>CL_MEM_WRITE_ONLY</code> inside a kernel is undefined.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_READ_ONLY</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flag specifies that the memory object is a read-only
     *                       memory object when used inside a kernel.
     *                     </p>
     *                     <p>Writing to a buffer or image object created with
     *                       <code>CL_MEM_READ_ONLY</code> inside a kernel is undefined.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_USE_HOST_PTR</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flag is valid only if <code>host_ptr</code> is not NULL. If
     *                       specified, it indicates that the application wants the
     *                       OpenCL implementation to use memory referenced by
     *                       <code>host_ptr</code> as the storage bits for the memory object.
     *                     </p>
     *                     <p>OpenCL implementations are allowed to cache the buffer
     *                       contents pointed to by <code>host_ptr</code> in device memory. This
     *                       cached copy can be used when kernels are executed on a
     *                       device.
     *                     </p>
     *                     <p>The result of OpenCL commands that operate on multiple
     *                       buffer objects created with the same <code>host_ptr</code> or
     *                       overlapping host regions is considered to be undefined.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_ALLOC_HOST_PTR</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flag specifies that the application wants the OpenCL
     *                       implementation to allocate memory from host accessible
     *                       memory.
     *                     </p>
     *                     <p>
     *                       <code>CL_MEM_ALLOC_HOST_PTR</code> and
     *                       <code>CL_MEM_USE_HOST_PTR</code> are mutually exclusive.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_COPY_HOST_PTR</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>
     *                       This flag is valid only if <code>host_ptr</code> is not NULL. If
     *                       specified, it indicates that the application wants the
     *                       OpenCL implementation to allocate memory for the
     *                       memory object and copy the data from memory referenced
     *                       by <code>host_ptr</code>.
     *                     </p>
     *                     <p>
     *                       <code>CL_MEM_COPY_HOST_PTR</code> and
     *                       <code>CL_MEM_USE_HOST_PTR</code> are mutually exclusive.
     *                     </p>
     *                     <p>
     *                       <code>CL_MEM_COPY_HOST_PTR</code> can be used with
     *                       <code>CL_MEM_ALLOC_HOST_PTR</code> to initialize the contents of
     *                       the cl_mem object allocated using host-accessible (e.g.
     *                       PCIe) memory.
     *                     </p>
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>size</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The size in bytes of the buffer memory object to be allocated.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>host_ptr</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to the buffer data that may already be allocated by the application. The size
     *             of the buffer that <code>host_ptr</code> points to must be greater than or equal to the <code>size</code> bytes.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>errcode_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero buffer object and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span> if
     *       the buffer object is created successfully. Otherwise, it returns a NULL value with one of the
     *       following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context..
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>flags</code> are not valid.
     *         </li>
     *         <li><span>CL_INVALID_BUFFER_SIZE</span> if <code>size</code> is 0 or is greater than
     *           <span>CL_DEVICE_MAX_MEM_ALLOC_SIZE</span> value specified in table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span> for all devices in <code>context</code>.
     *         </li>
     *         <li><span>CL_INVALID_HOST_PTR</span> if <code>host_ptr</code> is NULL and <span>CL_MEM_USE_HOST_PTR</span> or
     *           <span>CL_MEM_COPY_HOST_PTR</span> are set in <code> flags</code> or if <code> host_ptr</code> is not NULL but
     *           <span>CL_MEM_COPY_HOST_PTR</span> or <span>CL_MEM_USE_HOST_PTR</span> are not set in <code>flags</code>.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           buffer object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateBuffer(cl_context context, long flags, long size, Pointer host_ptr, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateBufferNative(context, flags, size, host_ptr, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateBufferNative(context, flags, size, host_ptr, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateBufferNative(cl_context context, long flags, long size, Pointer host_ptr, int errcode_ret[]);



    /**
     * <p>
     *       Creates a buffer object (referred to as a sub-buffer object) from an existing buffer object.
     *   </p>
     *
     * <div title="clCreateSubBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_mem <b>clCreateSubBuffer</b>
     *             (</code>
     *           <td>cl_mem <var>buffer</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_buffer_create_type <var>buffer_create_type</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const void <var>*buffer_create_info</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>buffer</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A valid object. Cannot be a sub-buffer object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>flags</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify allocation and usage information about the image memory
     *             object being created. The
     *             following table describes the possible values for <code>flags</code>:
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_mem_flags</th>
     *                   <th align="left">Description</th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_READ_WRITE</code>
     *                   </td>
     *                   <td align="left">
     *                     This flag specifies that the memory object will be read and
     *                     written by a kernel. This is the default.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_WRITE_ONLY</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flags specifies that the memory object will be written
     *                       but not read by a kernel.
     *                     </p>
     *                     <p>Reading from a buffer or image object created with
     *                       <code>CL_MEM_WRITE_ONLY</code> inside a kernel is undefined.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_READ_ONLY</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flag specifies that the memory object is a read-only
     *                       memory object when used inside a kernel.
     *                     </p>
     *                     <p>Writing to a buffer or image object created with
     *                       <code>CL_MEM_READ_ONLY</code> inside a kernel is undefined.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_USE_HOST_PTR</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flag is valid only if <code>host_ptr</code> is not NULL. If
     *                       specified, it indicates that the application wants the
     *                       OpenCL implementation to use memory referenced by
     *                       <code>host_ptr</code> as the storage bits for the memory object.
     *                     </p>
     *                     <p>OpenCL implementations are allowed to cache the buffer
     *                       contents pointed to by <code>host_ptr</code> in device memory. This
     *                       cached copy can be used when kernels are executed on a
     *                       device.
     *                     </p>
     *                     <p>The result of OpenCL commands that operate on multiple
     *                       buffer objects created with the same <code>host_ptr</code> or
     *                       overlapping host regions is considered to be undefined.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_ALLOC_HOST_PTR</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>This flag specifies that the application wants the OpenCL
     *                       implementation to allocate memory from host accessible
     *                       memory.
     *                     </p>
     *                     <p>
     *                       <code>CL_MEM_ALLOC_HOST_PTR</code> and
     *                       <code>CL_MEM_USE_HOST_PTR</code> are mutually exclusive.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_COPY_HOST_PTR</code>
     *                   </td>
     *                   <td align="left">
     *                     <p>
     *                       This flag is valid only if <code>host_ptr</code> is not NULL. If
     *                       specified, it indicates that the application wants the
     *                       OpenCL implementation to allocate memory for the
     *                       memory object and copy the data from memory referenced
     *                       by <code>host_ptr</code>.
     *                     </p>
     *                     <p>
     *                       <code>CL_MEM_COPY_HOST_PTR</code> and
     *                       <code>CL_MEM_USE_HOST_PTR</code> are mutually exclusive.
     *                     </p>
     *                     <p>
     *                       <code>CL_MEM_COPY_HOST_PTR</code> can be used with
     *                       <code>CL_MEM_ALLOC_HOST_PTR</code> to initialize the contents of
     *                       the cl_mem object allocated using host-accessible (e.g.
     *                       PCIe) memory.
     *                     </p>
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span><code>buffer_create_type</code> and <code>buffer_create_info</code></span>
     *         </dt>
     *         <dd>
     *           <p>The type of buffer object to be created. The supported value for <code>buffer_create_type</code> is CL_BUFFER_CREATE_TYPE_REGION, which create a buffer object that represents a specific region in <code>buffer</code>. <code>buffer_create_info</code> is a pointer to the following structure:</p>
     *           <div>
     *             <p><br />
     *               typedef struct _cl_buffer_region {<br />
     *                   size_t origin;<br />
     *                   size_t size;<br />
     *               } cl_buffer_region;<br />
     *             </p>
     *           </div>
     *           <p>(<code>origin, size</code>) defines the offset and size in bytes in <code>buffer</code>.</p>
     *           <p>If <code>buffer</code> is created with CL_MEM_USE_HOST_PTR, the <code>host_ptr</code> associated with the buffer object returned is <code>host_ptr</code> + <code>origin</code>.</p>
     *           <p>The buffer object returned references the data store allocated for <code>buffer</code> and points to a specific region given by (<code>origin, size</code>) in this data store.</p>
     *           <p>CL_INVALID_VALUE is returned in <code>errcode_ret</code> if the region specified by (<code>origin, size</code>) is out of bounds in <code>buffer</code>.</p>
     *           <p>CL_MISALIGNED_SUB_BUFFER_OFFSET is returned in <code>errcode_ret</code> if there are no devices in context associated with <code>buffer</code> for which the <code>origin</code> value is aligned to the CL_DEVICE_MEM_BASE_ADDR_ALIGN value.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>errcode_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The implementation may return the same <span><span>cl_mem</span></span> object with the reference count incremented
     *       appropriately for multiple calls to <code>clCreateSubBuffer</code> that use the same values for <code>buffer</code>, <code>flags</code>, <code>buffer_create_type</code> and <code>buffer_create_info</code> points to the same descriptor or descriptors that
     *       describe values that are exactly the same.
     *     </p>
     *     <p>
     *       The result of OpenCL commands that read from and write to multiple sub-buffer objects created
     *       using <code>clCreateSubBuffer</code> with the same buffer object but represent overlapping regions in the
     *       buffer object is undefined. The result of OpenCL commands that read from and write to a buffer
     *       object and its sub-buffer object(s) created using <code>clCreateSubBuffer</code> with the same buffer object
     *       is undefined. OpenCL commands that only read from multiple sub-buffer objects created using
     *       <code>clCreateSubBuffer</code> with the same buffer object but represent overlapping regions in the buffer
     *       object or read from a buffer object and its sub-buffer objects should work as defined.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero buffer object and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span> if
     *       the buffer object is created successfully. Otherwise, it returns one of the following error in  <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>buffer</code> is not a valid buffer object or is a sub-buffer object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>flags</code> are not valid; or if value specified in <code>buffer_create_type</code> is not valid; or if value(s) specified in <code>buffer_create_info</code> (for a given <code>buffer_create_type</code>) is not valid or if <code>buffer_create_info</code> is NULL.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateSubBuffer(cl_mem buffer, /*cl_mem_flags*/ int flags, /*cl_buffer_create_type*/ int buffer_create_type, Pointer buffer_create_info, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateSubBufferNative(buffer, flags, buffer_create_type, buffer_create_info, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateSubBufferNative(buffer, flags, buffer_create_type, buffer_create_info, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateSubBufferNative(cl_mem buffer, int flags, int buffer_create_type, Pointer buffer_create_info, int errcode_ret[]);




    /**
     * <p>
     *       Creates a 2D image object.
     *   </p>
     *
     * <div title="clCreateImage2D">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_mem <b>clCreateImage2D</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_image_format <var>*image_format</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_width</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_height</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*host_ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid OpenCL context on which the image object is to be created.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify allocation and usage information about the image
     *             memory object being created and is described in the table List of supported <span>cl_mem_flags</span> values for <span><span>clCreateBuffer</span></span>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_format
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a structure that describes format properties of the image to be
     *             allocated. See <span><span>cl_image_format</span></span> for a detailed description of the image format descriptor.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_width
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           image_height
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The width and height of the image in pixels. These must be
     *             values greater than or equal to 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_row_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The scan-line pitch in bytes. This must be 0 if <code>host_ptr</code> is NULL and can be
     *             either 0 or greater than or equal to <code>image_width</code> * size of element in bytes
     *             if <code>host_ptr</code> is not NULL. If <code>host_ptr</code> is not
     *             NULL and <code>image_row_pitch</code> is equal to 0, <code>image_row_pitch</code> is calculated as <code>image_width</code> * size of
     *             element in bytes. If <code>image_row_pitch</code> is not 0, it must be a multiple of the image element size in
     *             bytes.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           host_ptr
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to the image data that may already be allocated by the application. The size
     *             of the buffer that <code>host_ptr</code> points to must be greater than or equal to <code>image_row_pitch</code> * <code>image_height</code>. The size of
     *             each element in bytes must be a power of 2. The image data specified by <code>host_ptr</code> is stored as a
     *             linear sequence of adjacent scanlines. Each scanline is stored as a linear sequence of image elements.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Will return an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clCreateImage2D</code> returns a valid non-zero image object
     *       and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span>
     *       if the image object is created successfully. Otherwise, it returns a NULL value
     *       with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>flags</code> are not valid.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_FORMAT_DESCRIPTOR</span> if values specified in
     *           <code>image_format</code> are not valid or if <code>image_format</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if <code>image_width</code> or
     *           <code>image_height</code> are 0 or if they exceed values specified in
     *           <code>CL_DEVICE_IMAGE2D_MAX_WIDTH</code> or <code>CL_DEVICE_IMAGE2D_MAX_HEIGHT</code>
     *           respectively for all devices in <code>context</code> or if values specified by
     *           <code>image_row_pitch</code> do not follow rules
     *           described in the argument description above.
     *         </li>
     *         <li><span>CL_INVALID_HOST_PTR</span> if <code>host_ptr</code> is NULL and
     *           <code>CL_MEM_USE_HOST_PTR</code> or <code>CL_MEM_COPY_HOST_PTR</code>
     *           are set in <code>flags</code> or if <code>host_ptr</code> is not NULL but
     *           <code>CL_MEM_COPY_HOST_PTR</code> or <code>CL_MEM_USE_HOST_PTR</code>
     *           are not set in <code>flags</code>.
     *         </li>
     *         <li><span>CL_IMAGE_FORMAT_NOT_SUPPORTED</span> if the <code>image_format</code> is not supported.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           image object.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if there are no devices in <code>context</code> that support images
     *           (i.e. <code>CL_DEVICE_IMAGE_SUPPORT</code> (specified in the table of OpenCL Device Queries for
     *           <span><span>clGetDeviceInfo</span></span>) is <span>CL_FALSE</span>).
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateImage2D(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_row_pitch, Pointer host_ptr, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateImage2DNative(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateImage2DNative(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateImage2DNative(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_row_pitch, Pointer host_ptr, int errcode_ret[]);

    /**
     * <p>
     *       Creates a 3D image object.
     *   </p>
     *
     * <div title="clCreateImage3D">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_mem <b>clCreateImage3D</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_image_format <var>*image_format</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_width</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_height</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_depth</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>image_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*host_ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid OpenCL context on which the image object is to be created.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify allocation and usage information about the image
     *             memory object being created and is described in the table List of supported <span>cl_mem_flags</span> values for <span><span>clCreateBuffer</span></span>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_format
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a structure that describes format properties of the image to be
     *             allocated. See <span><span>cl_image_format</span></span> for a detailed description of the image format descriptor.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_width
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           image_height
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The width and height of the image in pixels. These must be
     *             values greater than or equal to 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image depth
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The depth of the image in pixels. This must be a value greater than 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_row_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The scan-line pitch in bytes. This must be 0 if <code>host_ptr</code> is NULL and can be
     *             either 0 or greater than or equal to <code>image_width</code> * size of element in bytes
     *             if <code>host_ptr</code> is not NULL. If <code>host_ptr</code> is not
     *             NULL and <code>image_row_pitch</code> is equal to 0, <code>image_row_pitch</code> is calculated as <code>image_width</code> * size of
     *             element in bytes. If <code>image_row_pitch</code> is not 0, it must be a multiple of the image element size in
     *             bytes.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_slice_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The size in bytes of each 2D slice in the 3D image. This must be 0 if
     *             <code>host_ptr</code> is NULL and can be either 0 or greater than or equal to <code>image_row_pitch</code> * <code>image_height</code> if <code>host_ptr</code> is not
     *             NULL. If <code>host_ptr</code> is not NULL and <code>image_slice_pitch</code> equal to 0, <code>image_slice_pitch</code> is calculated as
     *             <code>image_row_pitch</code> * <code>image_height</code>. If <code>image_slice_pitch</code> is not 0, it must be a multiple of the
     *             <code>image_row_pitch</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           host_ptr
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to the image data that may already be allocated by the application. The size
     *             of the buffer that <code>host_ptr</code> points to must be greater than or equal to <code>image_slice_pitch</code> * <code>image_depth</code>. The size of
     *             each element in bytes must be a power of 2. The image data specified by <code>host_ptr</code> is stored as a
     *             linear sequence of adjacent 2D slices. Each 2D slice is a linear sequence of adjacent scanlines.
     *             Each scanline is a linear sequence of image elements.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Will return an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero image object created and the <code>errcode_ret</code> is set to
     *       <span>CL_SUCCESS</span> if the image object is created successfully. Otherwise, it
     *       returns a NULL value with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>flags</code> are not valid.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_FORMAT_DESCRIPTOR</span> if values specified in <code>image_format</code> are
     *           not valid or if <code>image_format</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if <code>image_width</code>, <code>image_height</code> are 0 or if <code>image_depth</code> less than or equal to 1
     *           or if they exceed values specified in <code>CL_DEVICE_IMAGE3D_MAX_WIDTH</code>,
     *           <code>CL_DEVICE_IMAGE3D_MAX_HEIGHT</code> or <code>CL_DEVICE_IMAGE3D_MAX_DEPTH</code>
     *           respectively for all devices in <code>context</code> or if values specified by <code>image_row_pitch</code> and
     *           <code>image_slice_pitch</code> do not follow rules described in the argument description above.
     *         </li>
     *         <li><span>CL_INVALID_HOST_PTR</span> if <code>host_ptr</code> is NULL and <code>CL_MEM_USE_HOST_PTR</code> or <code>CL_MEM_COPY_HOST_PTR</code> are set in <code>flags</code> or if <code>host_ptr</code> is not NULL but
     *           <code>CL_MEM_COPY_HOST_PTR</code> or <code>CL_MEM_USE_HOST_PTR</code> are not set in <code>flags</code>.
     *         </li>
     *         <li><span>CL_IMAGE_FORMAT_NOT_SUPPORTED</span> if the <code>image_format</code> is not supported.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           image object.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if there are no devices in <code>context</code> that support images (i.e.
     *           <code>CL_DEVICE_IMAGE_SUPPORT</code> (specified in the table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span>) is <span>CL_FALSE</span>).
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateImage3D(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Pointer host_ptr, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateImage3DNative(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateImage3DNative(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateImage3DNative(cl_context context, long flags, cl_image_format image_format[], long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Pointer host_ptr, int errcode_ret[]);

    /**
     * <p>
     *       Increments the memory object reference count.
     * </p>
     *
     * <div title="clRetainMemObject">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clRetainMemObject</b>
     *             (</code>
     *           <td>cl_mem <var>memobj</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       After the <code>memobj</code> reference count becomes zero and
     *       commands queued for execution on a command-queue(s) that use <code>memobj</code> have finished, the
     *       memory object is deleted.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>memobj</code> is a not a valid memory object (buffer or image object).
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *     <p>
     *       <span><span>clCreateBuffer</span></span>,
     *       <span><span>clCreateSubBuffer</span></span>,  <span><span>clCreateImage2D</span></span>, and  <span><span>clCreateImage3D</span></span> perform an implicit retain.
     *       <span><span>clCreateSubBuffer</span></span> also performs an implicit retain on the memory object used to create the sub-buffer or image object.
     *     </p>
     *   </div>
     * </div>
     */
    public static int clRetainMemObject(cl_mem memobj)
    {
        return checkResult(clRetainMemObjectNative(memobj));
    }

    private static native int clRetainMemObjectNative(cl_mem memobj);

    /**
     * <p>
     *       Decrements the memory object reference count.
     *   </p>
     *
     * <div title="clReleaseMemObject">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clReleaseMemObject</b>
     *             (</code>
     *           <td>cl_mem <var>memobj</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       After the <code>memobj</code> reference count becomes zero and
     *       commands queued for execution on a command-queue(s) that use <code>memobj</code> have finished, the
     *       memory object is deleted.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>memobj</code> is a not a valid memory object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *     <p>
     *       After the <code>memobj</code> reference count becomes zero and commands queued for execution on a
     *       command-queue(s) that use <code>memobj</code> have finished, the memory object is deleted.
     *     </p>
     *   </div>
     * </div>
     */
    public static int clReleaseMemObject(cl_mem memobj)
    {
        return checkResult(clReleaseMemObjectNative(memobj));
    }

    private static native int clReleaseMemObjectNative(cl_mem memobj);

    /**
     * <p>
     *       Get the list of image formats supported by an OpenCL implementation.
     *   </p>
     *
     * <div title="clGetSupportedImageFormats">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetSupportedImageFormats</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_object_type <var>image_type</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_entries</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_image_format <var>*image_formats</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>*num_image_formats</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid OpenCL context on which the image object(s) will be created.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify allocation and usage information about the image
     *             memory object being created and is described in the List of supported <span>cl_mem_flags</span> values for <span><span>clCreateBuffer</span></span>
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_type
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Describes the image type and must be either <code>CL_MEM_OBJECT_IMAGE2D</code> or
     *             <code>CL_MEM_OBJECT_IMAGE3D</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_entries
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the number of entries that can be returned in the memory location given by
     *             <code>image_formats</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_formats
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a memory location where the list of supported image formats are
     *             returned. Each entry describes a <span><span>cl_image_format</span></span> structure supported
     *             by the OpenCL implementation. If <code>image_formats</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_image_formats
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The actual number of supported image formats for a specific <code>context</code> and
     *             values specified by <code>flags</code>. If <code>num_image_formats</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <code>clGetSupportedImageFormats</code> can be used to get the list of image formats supported by an OpenCL
     *       implementation when the following information about an image memory object is specified:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           Context
     *         </li>
     *         <li>
     *           Image type - 2D or 3D image
     *         </li>
     *         <li>
     *           Image object allocation information
     *         </li>
     *       </ul>
     *     </div>
     *     <p>
     *       The minimum list of supported image formats is given by the table below:
     *     </p>
     *     <div>
     *       <table border="1">
     *         <colgroup>
     *           <col align="left" />
     *           <col align="left" />
     *           <col align="left" />
     *         </colgroup>
     *         <thead>
     *           <tr>
     *             <th align="left">
     *               <div>
     *                 <p>image_num_channels</p>
     *               </div>
     *             </th>
     *             <th align="left">
     *               <div>
     *                 <p>image_channel_order</p>
     *               </div>
     *             </th>
     *             <th align="left">
     *               <div>
     *                 <p>image_channel_data_type</p>
     *               </div>
     *             </th>
     *           </tr>
     *         </thead>
     *         <tbody>
     *           <tr>
     *             <td align="left">
     *               <div>
     *                 <p>4</p>
     *               </div>
     *             </td>
     *             <td align="left">
     *               <div>
     *                 <p>CL_RGBA</p>
     *               </div>
     *             </td>
     *             <td align="left">
     *               <div>
     *                 <p>CL_UNORM_INT8<br />
     *                   CL_UNORM_INT16<br />
     *                   <br />
     *                   CL_SIGNED_INT8<br />
     *                   CL_SIGNED_INT16<br />
     *                   CL_SIGNED_INT32<br />
     *                   <br />
     *                   CL_UNSIGNED_INT8<br />
     *                   CL_UNSIGNED_INT16<br />
     *                   CL_UNSIGNED_INT32<br />
     *                   <br />
     *                   CL_HALF_FLOAT<br />
     *                   CL_FLOAT<br />
     *                 </p>
     *               </div>
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               <div>
     *                 <p>4</p>
     *               </div>
     *             </td>
     *             <td align="left">
     *               <div>
     *                 <p>CL_BGRA</p>
     *               </div>
     *             </td>
     *             <td align="left">
     *               <div>
     *                 <p>CL_UNORM_INT8</p>
     *               </div>
     *             </td>
     *           </tr>
     *         </tbody>
     *       </table>
     *     </div>
     *     <p>
     *       If CL_DEVICE_IMAGE_SUPPORT specified in the table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span> is CL_TRUE, the values assigned to the following constants by the implementation must be greater than or equal to the minimum values specified in the table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span>:
     *       CL_DEVICE_MAX_READ_IMAGE_ARGS, CL_DEVICE_MAX_WRITE_IMAGE_ARGS,
     *       CL_DEVICE_IMAGE2D_MAX_WIDTH, CL_DEVICE_IMAGE2D_MAX_HEIGHT,
     *       CL_DEVICE_IMAGE3D_MAX_WIDTH, CL_DEVICE_IMAGE3D_MAX_HEIGHT,
     *       CL_DEVICE_IMAGE3D_MAX_DEPTH, CL_DEVICE_MAX_SAMPLERS.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns a NULL value
     *       with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>flags</code> or <code>image_type</code> are not valid, or
     *           if <code>num_entries</code> is 0 and <code>image_formats</code> is not NULL.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetSupportedImageFormats(cl_context context, long flags, int image_type, int num_entries, cl_image_format image_formats[], int num_image_formats[])
    {
        return checkResult(clGetSupportedImageFormatsNative(context, flags, image_type, num_entries, image_formats, num_image_formats));
    }

    private static native int clGetSupportedImageFormatsNative(cl_context context, long flags, int image_type, int num_entries, cl_image_format image_formats[], int num_image_formats[]);

    /**
     * <p>
     *       Used to get information that is common to all memory objects (buffer and image objects).
     *   </p>
     *
     * <div title="clGetMemObjectInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetMemObjectInfo</b>
     *             (</code>
     *           <td>cl_mem <var>memobj</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           memobj
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the memory object being queried.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported <code>param_name</code> types and the
     *             information returned in <code>param_value</code> by <code>clGetMemObjectInfo</code> is described
     *             in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_mem_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Info. returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_TYPE</code>
     *                   </td>
     *                   <td align="left">cl_mem_object_type</td>
     *                   <td align="left">
     *                     Returns one of the following values:
     *                     <p><code>CL_MEM_OBJECT_BUFFER</code> if
     *                       <code>memobj</code> is created with
     *                       <span><span>clCreateBuffer</span></span> or
     *                       <span><span>clCreateSubBuffer</span></span>,
     *                     </p>
     *                     <p><code>CL_MEM_OBJECT_IMAGE2D</code> if
     *                       <code>memobj</code> is a 2D image object, or
     *                     </p>
     *                     <p><code>CL_MEM_OBJECT_IMAGE3D</code> if
     *                       <code>memobj</code> is a 3D image object.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_FLAGS</code>
     *                   </td>
     *                   <td align="left">cl_mem_flags</td>
     *                   <td align="left">
     *                     Returns the <code>flags</code> argument value
     *                     specified when <code>memobj</code> is created with                                        <span><span>clCreateBuffer</span></span>,
     *                     <span><span>clCreateSubBuffer</span></span>,                                  <span><span>clCreateImage2D</span></span>, or <span><span>clCreateImage3D</span></span>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_SIZE</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return actual size of the data store associated with <code>memobj</code> in bytes.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_HOST_PTR</code>
     *                   </td>
     *                   <td align="left">void *</td>
     *                   <td align="left">
     *                     Return the <code>host_ptr</code> argument value specified when <code>memobj</code> is created.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_MAP_COUNT</code>
     *                   </td>
     *                   <td align="left">cl_uint</td>
     *                   <td align="left">
     *                     Map count. The map count returned should be considered immediately stale. It is unsuitable for general use in applications. This feature is provided for debugging.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_REFERENCE_COUNT</code>
     *                   </td>
     *                   <td align="left">cl_uint</td>
     *                   <td align="left">
     *                     Return <code>memobj</code> reference count. The reference count returned should be considered immediately stale. It is unsuitable for general use in
     *                     applications. This feature is provided for identifying memory leaks.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_CONTEXT</code>
     *                   </td>
     *                   <td align="left">cl_context</td>
     *                   <td align="left">
     *                     Return context specified when memory object is created.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_ASSOCIATED_MEMOBJECT</code>
     *                   </td>
     *                   <td align="left">cl_mem</td>
     *                   <td align="left">
     *                     Return memory object from which <code>memobj</code> is created.
     *                     <p> This returns the memory object specified as <code>buffer</code>
     *                       argument to <span><span>clCreateSubBuffer</span></span>.
     *                     </p>
     *                     <p>Otherwise a NULL value is returned. </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_OFFSET</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return offset if <code>memobj</code> is a sub-buffer object created using
     *                     <span><span>clCreateSubBuffer</span></span>.
     *                     <p>This return 0 if <code>memobj</code> is not a subbuffer object.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_MEM_D3D10_RESOURCE_KHR</code>
     *                   </td>
     *                   <td align="left">ID3D10Resource *</td>
     *                   <td align="left">
     *                     The propery being queried (if the
     *                     <span><span>cl_khr_d3d10_sharing</span></span>
     *                     extension is enabled). If <code>memobj</code> was created using
     *                     <span><span>clCreateFromD3D10BufferKHR</span></span>,
     *                     <span><span>clCreateFromD3D10Texture2DKHR</span></span>,
     *                     or <span><span>clCreateFromD3D10Texture3DKHR</span></span>,
     *                     returns the resource argument specified
     *                     when <code>memobj</code> was created.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned. If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Used to specify the size in bytes of memory pointed to by <code>param_value</code>.
     *             This size must be greater than or equal to size of return type as described in the table above.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data being queried by <code>param_value</code>. If <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size
     *           in bytes specified by <code>param_value_size</code> is less than the size of return type as
     *           described in the table above and <code>param_value</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_D3D10_RESOURCE_KHR</span> If the
     *           <span><span>cl_khr_d3d10_sharing</span></span>
     *           extension is enabled and if <code>param_name</code> is
     *           CL_MEM_D3D10_RESOURCE_KHR and <code>memobj</code> was not created by the function
     *           <span><span>clCreateFromD3D10BufferKHR</span></span>,
     *           <span><span>clCreateFromD3D10Texture2DKHR</span></span>, or
     *           <span><span>clCreateFromD3D10Texture3DKHR</span></span>.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>memobj</code> is a not a valid memory object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetMemObjectInfo(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetMemObjectInfoNative(memobj, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetMemObjectInfoNative(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Get information specific to an image object.
     *   </p>
     *
     * <div title="clGetImageInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int * <b>clGetImageInfo</b>
     *             (</code>
     *           <td>cl_mem <var>image</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_image_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           image
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the image object being queried.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported <code>param_name</code> types and the
     *             information returned in <code>param_value</code> by <code>clGetImageInfo</code> is described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned. If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Used to specify the size in bytes of memory pointed to by <code>param_value</code>.
     *             This size must be greater than or equal to size of return type as described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_image_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Info. returned in  <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_FORMAT</code>
     *                   </td>
     *                   <td align="left">cl_image_format</td>
     *                   <td align="left">
     *                     Return image format descriptor specified when
     *                     <code>image</code> is created with <span><span>clCreateImage2D</span></span> or <span><span>clCreateImage3D</span></span>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_ELEMENT_SIZE</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return size of each element of the image
     *                     memory object given by <code>image</code>. An element is
     *                     made up of <code>n</code> channels. The value of <code>n</code> is given
     *                     in <code>cl_image_format</code> descriptor.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_ROW_PITCH</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return size in bytes of a row of elements of the
     *                     image object given by <code>image</code>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_SLICE_PITCH</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return size in bytes of a 2D slice for the 3D
     *                     image object given by <code>image</code>. For a 2D image
     *                     object this value will be 0.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_WIDTH</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return width of image in pixels.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_HEIGHT</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return height of image in pixels.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_DEPTH</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Return depth of the image in pixels. For a 2D
     *                     image, depth equals 0.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_IMAGE_D3D10_SUBRESOURCE_KHR</code>
     *                   </td>
     *                   <td align="left">ID3D10Resource *</td>
     *                   <td align="left">
     *                     The propery being queried (if the
     *                     <span><span>cl_khr_d3d10_sharing</span></span>
     *                     extension is enabled). If <code>image</code> was created using
     *                     <span><span>clCreateFromD3D10Texture2DKHR</span></span> or
     *                     <span><span>clCreateFromD3D10Texture3DKHR</span></span>,
     *                     returns the <code>subresource</code> argument
     *                     specified when <code>image</code> was created.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             the actual size in bytes of data being queried by <code>param_value</code>. If <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       To get information that is common to all memory objects (buffer and image objects), use the
     *       <span><span>clGetMemObjectInfo</span></span> function.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>image</code> is a not a valid image object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if
     *           size in bytes specified by <code>param_value_size</code> is less than the size of return
     *           type as described in the table above and <code>param_value</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_D3D10_RESOURCE_KHR</span> if <code>param_name</code> is
     *           CL_MEM_D3D10_SUBRESOURCE_KHR and <code>image</code> was not created by the function
     *           <span><span>clCreateFromD3D10Texture2DKHR</span></span> or
     *           <span><span>clCreateFromD3D10Texture3DKHR</span></span>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetImageInfo(cl_mem image, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetImageInfoNative(image, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetImageInfoNative(cl_mem image, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);



    /**
     * <p>
     *       Registers a user callback function that will be called when the memory object is
     *       deleted and its resources freed.
     *   </p>
     *
     * <div title="clSetMemObjectDestructorCallback">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int
     *             <b>clSetMemObjectDestructorCallback</b>
     *             (</code>
     *           <td>cl_mem <var>memobj</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void (CL_CALLBACK  <var>*pfn_notify</var>) (cl_mem memobj, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*user_data</var>), </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*user_data</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>memobj</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A valid memory object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>pfn_notify</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The callback function that can be registered by the application. This
     *             callback function may be called asynchronously by the OpenCL implementation.
     *             It is the application's responsibility to ensure that the callback function
     *             is thread-safe. The parameters to this callback function are:
     *           </p>
     *           <div>
     *             <ul type="disc">
     *               <li><code>memobj</code>: the memory object being deleted.
     *               </li>
     *               <li><code>user_data</code>: a pointer to user supplied data.
     *               </li>
     *             </ul>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>user_data</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Data which will be passed as the <code>user_data</code> argument when
     *             <code>pfn_notify</code> is called. <code>user_data</code> can be NULL.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Each call to <code>clSetMemObjectDestructorCallback</code> registers the specified user
     *       callback function on a callback stack associated with <code>memobj</code>. The registered user
     *       callback functions are called in the reverse order in which they were registered. The user callback
     *       functions are called and then the memory object's resources are freed and the memory object is
     *       deleted. This provides a mechanism for the application (and libraries) using <code>memobj</code>
     *       to be notified when the memory referenced by <code>host_ptr</code>, specified when the memory object
     *       is created and used as the storage bits for the memory object, can be reused or freed.
     *     </p>
     *     <p>
     *       When the user callback function is called by the implementation, the contents of the
     *       memory region pointed to by <code>host_ptr</code> (if the memory object is created with
     *       CL_MEM_USE_HOST_PTR) are undefined. The callback function is typically used by the application to
     *       either free or reuse the memory region pointed to by <code>host_ptr</code>.
     *     </p>
     *     <p>
     *       The behavior of calling expensive system routines, OpenCL API calls to create contexts or
     *       command-queues, or blocking OpenCL operations from the following list below, in a callback is
     *       undefined.
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           <span>
     *           <span>clFinish</span>
     *           </span>
     *         </li>
     *         <li>
     *           <span>
     *           <span>clWaitForEvents</span>
     *           </span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clEnqueueReadBuffer</span></span>,
     *           <span><span>clEnqueueReadBufferRect</span></span>,
     *           <span><span>clEnqueueWriteBuffer</span></span>,
     *           <span><span>clEnqueueWriteBufferRect</span></span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clEnqueueReadImage</span></span> and
     *           <span><span>clEnqueueWriteImage</span></span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clEnqueueMapBuffer</span></span> and
     *           <span><span>clEnqueueMapImage</span></span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clBuildProgram</span></span>
     *         </li>
     *       </ul>
     *     </div>
     *     <p>
     *       If an application needs to wait for completion of a routine from the above list in a callback,
     *       please use the non-blocking form of the function, and assign a completion callback to it to do the
     *       remainder of your work. Note that when a callback (or other code) enqueues commands to a
     *       command-queue, the commands are not required to begin execution until the queue is flushed.
     *       In standard usage, blocking enqueue calls serve this role by implicitly flushing the queue. Since
     *       blocking calls are not permitted in callbacks, those callbacks that enqueue commands on a
     *       command queue should either call <span><span>clFlush</span></span>
     *       on the queue before returning or arrange for <span><span>clFlush</span></span>
     *       to be called later on another thread.
     *     </p>
     *     <p>
     *       The user callback function may not call OpenCL APIs with the memory object for which the
     *       callback function is invoked and for such cases the behavior of OpenCL APIs is considered to be
     *       undefined.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the command-queue properties are successfully updated.
     *       Otherwise, it returns the following:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>memobj</code> is not a valid memory object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>pfn_notify</code> is NULL.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clSetMemObjectDestructorCallback(cl_mem memobj, MemObjectDestructorCallbackFunction pfn_notify, Object user_data)
    {
        return checkResult(clSetMemObjectDestructorCallbackNative(memobj, pfn_notify, user_data));
    }
    private static native int clSetMemObjectDestructorCallbackNative(cl_mem memobj, MemObjectDestructorCallbackFunction pfn_notify, Object user_data);



    /**
     * <p>
     *       Creates a sampler object.
     *   </p>
     *
     * <div title="clCreateSampler">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_sampler <b>clCreateSampler</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>normalized_coords</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_addressing_mode <var>addressing_mode</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_filter_mode <var>filter_mode</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           normalized_coords
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Determines if the image coordinates specified are normalized (if
     *             <code>normalized_coords</code> is <code>CL_TRUE</code>) or not (if <code>normalized_coords</code> is <code>CL_FALSE</code>).
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           addressing_mode
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies how out-of-range image coordinates are handled when reading from
     *             an image. This can be set to <code>CL_ADDRESS_MIRRORED_REPEAT</code>, <code>CL_ADDRESS_REPEAT</code>, <code>CL_ADDRESS_CLAMP_TO_EDGE</code>, <code>CL_ADDRESS_CLAMP</code>, and <code>CL_ADDRESS_NONE</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           filtering_mode
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the type of filter that must be applied when reading an image. This can be <code>CL_FILTER_NEAREST</code> or <code>CL_FILTER_LINEAR</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       A sampler object describes how to sample an image when the image is read in the kernel. The built-in functions to read from an image in a kernel take a sampler as an argument. The sampler arguments to the image read function can be sampler objects created using OpenCL functions and passed as argument values to the kernel or can be samplers declared inside a kernel. In this section we discuss how sampler objects are created using OpenCL functions.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero sampler object and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span> if the sampler object is created successfully. Otherwise, it returns a NULL value with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code>
     *           is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>addressing_mode</code>,
     *           <code>filter_mode</code>, or <code>normalized_coords</code> or a  combination of these argument values are not valid.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if images are not supported by any device associated with <code>context</code> (i.e. <span>CL_DEVICE_IMAGE_SUPPORT</span> specified in the table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span> is <span>CL_FALSE</span>).
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_sampler clCreateSampler(cl_context context, boolean normalized_coords, int addressing_mode, int filter_mode, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_sampler result = clCreateSamplerNative(context, normalized_coords, addressing_mode, filter_mode, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_sampler result = clCreateSamplerNative(context, normalized_coords, addressing_mode, filter_mode, errcode_ret);
            return result;
        }
    }

    private static native cl_sampler clCreateSamplerNative(cl_context context, boolean normalized_coords, int addressing_mode, int filter_mode, int errcode_ret[]);

    /**
     * <p>
     *       Increments the sampler reference count.
     *   </p>
     *
     * <div title="clRetainSampler">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clRetainSampler</b>(</code>
     *           <td>cl_sampler <var>sampler</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>sampler
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           Specifies the <code>sampler</code> being retained.
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <span><span>clCreateSampler</span></span> performs an
     *       implicit retain.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_SAMPLER</span> if <code>sampler</code> is not a valid sampler object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clRetainSampler(cl_sampler sampler)
    {
        return checkResult(clRetainSamplerNative(sampler));
    }

    private static native int clRetainSamplerNative(cl_sampler sampler);

    /**
     * <p>
     *       Decrements the sampler reference count.
     *   </p>
     *
     * <div title="clReleaseSampler">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clReleaseSampler</b>
     *             (</code>
     *           <td>cl_sampler <var>sampler</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The sampler object is deleted after the reference count
     *       becomes zero and commands queued for execution on a command-queue(s) that use
     *       <code>sampler</code> have finished.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_SAMPLER</span> if <code>sampler</code> is not a valid sampler object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clReleaseSampler(cl_sampler sampler)
    {
        return checkResult(clReleaseSamplerNative(sampler));
    }

    private static native int clReleaseSamplerNative(cl_sampler sampler);

    /**
     * <p>
     *       Returns information about the sampler object.
     *   </p>
     *
     * <div title="clGetSamplerInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetSamplerInfo</b>
     *             (</code>
     *           <td>cl_sampler <var>sampler</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_sampler_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>sampler</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the sampler being queried.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_name</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported
     *             <code>param_name</code> types and the information returned in
     *             <code>param_value</code> by <code>clGetSamplerInfo</code> is described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is
     *             returned. If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the size in bytes of memory pointed to by
     *             <code>param_value</code>. This size must be greater than or equal to size of
     *             return type as described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data copied to <code>param_value</code>.
     *             If <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt></dt>
     *         <dd>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_sampler_info</th>
     *                   <th align="left">Return Type and Info. returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_SAMPLER_REFERENCE_COUNT</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_uint
     *                     <p>Return the <code>sampler</code> reference count. The reference
     *                       count returned should be considered immediately stale. It is unsuitable for
     *                       general use in applications. This feature is provided for identifying memory leaks.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_SAMPLER_CONTEXT</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_context
     *                     <p>Return the context specified when the sampler is created.</p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_SAMPLER_ADDRESSING_MODE</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_addressing_mode
     *                     <p>Return the addressing mode value associated with
     *                       <code>sampler</code>.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_SAMPLER_FILTER_MODE</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_filter_mode
     *                     <p>Return the filter mode value associated with
     *                       <code>sampler</code>.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_SAMPLER_NORMALIZED_COORDS</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_bool
     *                     <p>Return the normalized coords value associated with
     *                       <code>sampler</code>.
     *                     </p>
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise,
     *       it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size
     *           in bytes specified by <code>param_value_size</code> is less than return type as described in
     *           the table above and <code>param_value</code> is not NULL
     *         </li>
     *         <li><span>CL_INVALID_SAMPLER</span> if   <code>sampler</code> is a not a valid sampler object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetSamplerInfo(cl_sampler sampler, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetSamplerInfoNative(sampler, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetSamplerInfoNative(cl_sampler sampler, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Creates a program object for a context, and loads the source code specified by the text strings in
     *       the <code>strings</code> array into the program object.
     *   </p>
     *
     * <div title="clCreateProgramWithSource">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_program <b>clCreateProgramWithSource</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>count</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const char <var>**strings</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>*lengths</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           strings
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             An array of <code>count</code> pointers to optionally null-terminated character strings that make up the source code.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           lengths
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             An array with the number of chars in each string (the string length). If
     *             an element in <code>lengths</code> is zero, its accompanying string is null-terminated. If <code>lengths</code> is NULL, all strings in the <code>strings</code> argument are considered null-terminated. Any length value passed in that is greater than zero excludes the null terminator in its count.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The devices associated with the program object are the devices associated with <code>context</code>.
     *     </p>
     *     <p>
     *       OpenCL allows applications to create a program object using the program source or binary and build appropriate program executables. This allows applications to determine whether they want
     *       to use the pre-built offline binary or load and compile the program source and use the executable
     *       compiled/linked online as the program executable. This can be very useful as it allows
     *       applications to load and build program executables online on its first instance for appropriate
     *       OpenCL devices in the system. These executables can now be queried and cached by the
     *       application. Future instances of the application launching will no longer need to compile and
     *       build the program executables. The cached executables can be read and loaded by the
     *       application, which can help significantly reduce the application initialization time.
     *     </p>
     *     <p>
     *       An OpenCL program consists of a set of kernels that are identified as functions declared with the <span><span>__kernel</span></span> qualifier in the program source. OpenCL programs may also contain auxiliary functions and constant data that can be used by <span><span>__kernel</span></span> functions. The program executable can be generated <span><em>online</em></span> or <span><em>offline</em></span> by the OpenCL compiler for the appropriate target device(s).
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clCreateProgramWithSource</code> returns a valid non-zero program object and <code>errcode_ret</code> is set to
     *       <span>CL_SUCCESS</span> if the program object is created successfully. Otherwise, it returns a NULL value
     *       with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>count</code> is zero or if <code>strings</code>
     *           or any entry in <code>strings</code> is NULL.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_program clCreateProgramWithSource(cl_context context, int count, String strings[], long lengths[], int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_program result = clCreateProgramWithSourceNative(context, count, strings, lengths, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_program result = clCreateProgramWithSourceNative(context, count, strings, lengths, errcode_ret);
            return result;
        }
    }

    private static native cl_program clCreateProgramWithSourceNative(cl_context context, int count, String strings[], long lengths[], int errcode_ret[]);

    /**
     * <p>
     *       Creates a program object for a context, and loads the binary bits specified by
     *       binary into the program object.
     *   </p>
     *
     * <div title="clCreateProgramWithBinary">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_program <b>clCreateProgramWithBinary</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_devices</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_device_id <var>*device_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>*lengths</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const unsigned char <var>**binaries</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*binary_status</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           device_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a list of devices that are in <code>context</code>. <code>device_list</code> must be a non-NULL
     *             value. The binaries are loaded for devices specified in this list.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_devices
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of devices listed in <code>device_list</code>.
     *           </p>
     *           <p>
     *             The devices associated with the program object will be the list of devices specified by
     *             <code>device_list</code>. The list of devices specified by <code>device_list</code> must be devices associated with <code>context</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           lengths
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             An array of the size in bytes of the program binaries to be loaded for devices specified
     *             by <code>device_list</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           binaries
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             An array of pointers to program binaries to be loaded for devices specified by <code>device_list</code>. For each device given by <code>device_list</code>[i], the pointer to the program binary for that device is given by <code>binaries</code>[i] and the length of this corresponding binary is given by <code>lengths</code>[i]. <code>lengths</code>[i] cannot be zero and <code>binaries</code>[i] cannot be a NULL pointer.
     *           </p>
     *           <p>
     *             The program binaries specified by <code>binaries</code> contain the bits that describe the program executable that will be run on the device(s) associated with <code>context</code>. The program binary can consist of either or both of device-specific executable(s), and/or implementation-specific intermediate representation (IR) which will be converted to the device-specific executable.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           binary_status
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns whether the program binary for each device specified in <code>device_list</code>
     *             was loaded successfully or not. It is an array of <code>num_devices</code> entries and
     *             returns <span>CL_SUCCESS</span> in <code>binary_status</code>[i] if binary
     *             was successfully loaded for device specified by <code>device_list</code>[i];
     *             otherwise returns <span>CL_INVALID_VALUE</span> if <code>lengths</code>[i]
     *             is zero or if <code>binaries</code>[i] is a NULL value or
     *             <span>CL_INVALID_BINARY</span> in <code>binary_status</code>[i] if program
     *             binary is not a valid binary for the
     *             specified device. If <code>binary_status</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is
     *             returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       OpenCL allows applications to create a program object using the program source or binary and
     *       build appropriate program executables. This allows applications to determine whether they want
     *       to use the pre-built offline binary or load and compile the program source and use the executable
     *       compiled/linked online as the program executable. This can be very useful as it allows
     *       applications to load and build program executables online on its first instance for appropriate
     *       OpenCL devices in the system. These executables can now be queried and cached by the
     *       application. Future instances of the application launching will no longer need to compile and
     *       build the program executables. The cached executables can be read and loaded by the
     *       application, which can help significantly reduce the application initialization time.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero program object and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span> if the program object is created successfully. Otherwise, it returns a NULL value with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>device_list</code> is NULL or <code>num_devices</code>
     *           is zero; or if <code>lengths</code> or <code>binaries</code> are NULL or if any entry
     *           in <code>lengths</code>[i] or <code>binaries</code>[i] is NULL.
     *         </li>
     *         <li><span>CL_INVALID_DEVICE</span> if OpenCL devices listed in <code>device_list</code> are not
     *           in the list of devices associated with <code>context</code>.
     *         </li>
     *         <li><span>CL_INVALID_BINARY</span> if an invalid program binary was encountered for any
     *           device. <code>binary_status</code> will return specific status for each device.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_program clCreateProgramWithBinary(cl_context context, int num_devices, cl_device_id device_list[], long lengths[], byte binaries[][], int binary_status[], int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_program result = clCreateProgramWithBinaryNative(context, num_devices, device_list, lengths, binaries, binary_status, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_program result = clCreateProgramWithBinaryNative(context, num_devices, device_list, lengths, binaries, binary_status, errcode_ret);
            return result;
        }
    }

    private static native cl_program clCreateProgramWithBinaryNative(cl_context context, int num_devices, cl_device_id device_list[], long lengths[], byte binaries[][], int binary_status[], int errcode_ret[]);

    /**
     * <p>
     *     Increments the <code>program</code> reference count.
     *   </p>
     *
     * <div title="clRetainProgram">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clRetainProgram</b>
     *             (</code>
     *           <td>cl_program <var>program</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PROGRAM</span> if <code>program</code> is not a valid program object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clRetainProgram(cl_program program)
    {
        return checkResult(clRetainProgramNative(program));
    }

    private static native int clRetainProgramNative(cl_program program);

    /**
     * <p>
     *       Decrements the <code>program</code> reference count.
     *   </p>
     *
     * <div title="clReleaseProgram">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int <b>clReleaseProgram</b>
     *             (</code>
     *           <td>cl_program <var>program</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The program object is deleted after all kernel objects
     *       associated with <code>program</code> have been deleted and the <code>program</code> reference count becomes zero.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PROGRAM</span> if <code>program</code> is not a valid program object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clReleaseProgram(cl_program program)
    {
        return checkResult(clReleaseProgramNative(program));
    }

    private static native int clReleaseProgramNative(cl_program program);

    /**
     * <p>
     *       Builds (compiles and links) a program executable from the program source or binary.
     *   </p>
     *
     * <div title="clBuildProgram">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clBuildProgram</b>
     *             (</code>
     *           <td>cl_program <var>program</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_devices</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_device_id <var>*device_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const char <var>*options</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>(CL_CALLBACK *pfn_notify)(cl_program program, void *user_data)</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*user_data</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           program
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The program object
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           device_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a list of devices associated with <code>program</code>. If <code>device_list</code> is NULL value, the program executable is built for all devices associated with <code>program</code> for which a source or binary has been loaded. If <code>device_list</code> is a non-NULL value, the program executable is built for devices specified in this list for which a source or binary has been loaded.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_devices
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of devices listed in <code>device_list</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>options</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a null-terminated string of characters that describes the build options to be used for building the program executable. The list of supported options is described in "Build Options" below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           pfn_notify
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A function pointer to a notification routine. The notification routine is a callback function that an application can register and which will be called when the program executable has been built (successfully or unsuccessfully). If <code>pfn_notify</code> is not NULL, <code>clBuildProgram</code> does not need to wait for the build to complete and can return immediately. If <code>pfn_notify</code> is NULL, <code>clBuildProgram</code> does not return until the build has completed. This callback function may be called asynchronously by the OpenCL implementation. It is the application's responsibility to ensure that the callback function is thread-safe.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           user_data
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Passed as an argument when <code>pfn_notify</code> is called. <code>user_data</code> can be NULL.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       OpenCL allows program executables to be built using the source or the binary. <code>clBuildProgram</code> must
     *       be called for <code>program</code> created using
     *       either <span><span>clCreateProgramWithSource</span></span>
     *       or <span><span>clCreateProgramWithBinary</span></span> to build the program executable
     *       for one or more devices associated with <code>program</code>.
     *     </p>
     *     <p>
     *       The build options are categorized as pre-processor options, options for math intrinsics, options that control
     *       optimization and miscellaneous options. This specification defines a standard set of options that must be
     *       supported by an OpenCL compiler when building program executables online or offline. These may be extended by a
     *       set of vendor- or platform-specific options.
     *     </p>
     *     <h4>Preprocessor Options</h4>
     *     These options control the OpenCL preprocessor which is run on each program source before
     *     actual compilation. -D options are processed in the order they are given in the options argument to <code>clBuildProgram</code>.
     *     <div>
     *       <dl>
     *         <dt><span>-D name</span></dt>
     *         <dd>
     *           <p>
     *             Predefine <code>name</code> as a macro, with definition 1.
     *           </p>
     *         </dd>
     *         <dt><span>-D name=definition</span></dt>
     *         <dd>
     *           <p>
     *             The contents of <code>definition</code> are tokenized and processed as if they appeared during translation phase three in a `#define' directive. In particular, the definition will be truncated by embedded newline characters.
     *           </p>
     *         </dd>
     *         <dt><span>-I dir</span></dt>
     *         <dd>
     *           <p>
     *             Add the directory <code>dir</code> to the list of directories to be searched for header files.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *     <div>
     *       <p><br /></p>
     *     </div>
     *     <h4>Math Intrinsics Options</h4>
     *     These options control compiler behavior regarding floating-point arithmetic. These options trade
     *     off between speed and correctness.
     *     <div>
     *       <dl>
     *         <dt><span>-cl-single-precision-constant</span></dt>
     *         <dd>
     *           <p>
     *             Treat double precision floating-point constant as single precision constant.
     *           </p>
     *         </dd>
     *         <dt><span>-cl-denorms-are-zero</span></dt>
     *         <dd>
     *           <p>
     *             This option controls how single precision and double precision denormalized numbers are handled. If specified
     *             as a build option, the single precision denormalized numbers may be flushed to zero; double precision
     *             denormalized numbers may also be flushed to zero if the optional extension for double precsion is supported.
     *             This is intended to be a performance hint and the OpenCL compiler can choose not to flush denorms to zero if
     *             the device supports single precision (or double precision) denormalized numbers.
     *           </p>
     *           <p>
     *             This option is ignored for single precision numbers if the device does not support single precision
     *             denormalized numbers i.e. CL_FP_DENORM bit is not set in CL_DEVICE_SINGLE_FP_CONFIG.
     *           </p>
     *           <p>
     *           </p>
     *           <p>
     *             This option is ignored for double precision numbers if the device does not support double precision or
     *             if it does support double precison but CL_FP_DENORM bit is not set in CL_DEVICE_DOUBLE_FP_CONFIG.
     *           </p>
     *           <p>
     *           </p>
     *           <p>
     *             This flag only applies for scalar and vector single precision floating-point variables and computations
     *             on these floating-point variables inside a program. It does not apply to reading from or writing to
     *             image objects.
     *           </p>
     *           <p>
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *     <div>
     *       <p><br /></p>
     *     </div>
     *     <h4>Optimization Options</h4>
     *     These options control various sorts of optimizations. Turning on optimization flags makes the
     *     compiler attempt to improve the performance and/or code size at the expense of compilation time
     *     and possibly the ability to debug the program.
     *     <div>
     *       <dl>
     *         <dt><span>-cl-opt-disable</span></dt>
     *         <dd>
     *           <p>
     *             This option disables all optimizations. The default is optimizations are enabled.
     *           </p>
     *         </dd>
     *         <dt><span>-cl-strict-aliasing</span></dt>
     *         <dd>
     *           <p>
     *             This option allows the compiler to assume the strictest aliasing rules.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *     <p>
     *       The following options control compiler behavior regarding floating-point arithmetic. These options trade off between performance and correctness and must be specifically enabled. These options are not turned on by default since it can result in incorrect output for programs which depend on an exact implementation of IEEE 754 rules/specifications for math functions.
     *     </p>
     *     <div>
     *       <dl>
     *         <dt><span>-cl-mad-enable</span></dt>
     *         <dd>
     *           <p>
     *             Allow <code>a * b + c</code> to be replaced by a <code>mad</code>. The <code>mad</code>
     *             computes <code>a * b + c</code> with reduced accuracy. For example, some OpenCL devices
     *             implement <code>mad</code> as truncate the result of <code>a * b</code> before adding it to <code>c</code>.
     *           </p>
     *         </dd>
     *         <dt><span>-cl-no-signed-zeros</span></dt>
     *         <dd>
     *           <p>
     *             Allow optimizations for floating-point arithmetic that ignore the signedness of zero. IEEE 754 arithmetic
     *             specifies the distinct behavior of  <code>+0.0</code> and <code>-0.0</code> values, which then prohibits
     *             simplification of expressions such as <code>x+0.0</code> or <code>0.0*x</code> (even with -clfinite-math
     *             only). This option implies that the sign of a zero result isn't significant.
     *           </p>
     *         </dd>
     *         <dt><span>-cl-unsafe-math-optimizations</span></dt>
     *         <dd>
     *           <p>
     *             Allow optimizations for floating-point arithmetic that (a) assume that arguments and results are
     *             valid, (b) may violate IEEE 754 standard and (c) may violate the OpenCL numerical compliance requirements
     *             as defined in section 7.4 for single-precision floating-point, section 9.3.9 for double-precision
     *             floating-point, and edge case behavior in section 7.5. This option includes the -cl-no-signed-zeros
     *             and -cl-mad-enable options.
     *           </p>
     *         </dd>
     *         <dt><span>-cl-finite-math-only</span></dt>
     *         <dd>
     *           <p>
     *             Allow optimizations for floating-point arithmetic that assume that arguments and results
     *             are not NaNs or ±âˆž. This option may violate the OpenCL numerical compliance
     *             requirements defined in section 7.4 for single-precision floating-point,
     *             section 9.3.9 for double-precision floating-point, and edge case behavior in section 7.5.
     *           </p>
     *         </dd>
     *         <dt><span>-cl-fast-relaxed-math</span></dt>
     *         <dd>
     *           <p>
     *             Sets the optimization options -cl-finite-math-only and -cl-unsafe-math-optimizations.
     *             This allows optimizations for floating-point arithmetic that may violate the IEEE 754
     *             standard and the OpenCL numerical compliance requirements defined in the specification in
     *             section 7.4 for single-precision floating-point, section 9.3.9 for double-precision
     *             floating-point, and edge case behavior in section 7.5. This option causes the preprocessor
     *             macro <code>__FAST_RELAXED_MATH__</code> to be defined in the OpenCL program.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *     <div>
     *       <p><br /></p>
     *     </div>
     *     <h4>Options to Request or Suppress Warnings</h4>
     *     Warnings are diagnostic messages that report constructions which are not inherently erroneous
     *     but which are risky or suggest there may have been an error. The following languageindependent
     *     options do not enable specific warnings but control the kinds of diagnostics
     *     produced by the OpenCL compiler.
     *     <div>
     *       <dl>
     *         <dt><span>-w</span></dt>
     *         <dd>
     *           <p>
     *             Inhibit all warning messages.
     *           </p>
     *         </dd>
     *         <dt><span>-Werror</span></dt>
     *         <dd>
     *           <p>
     *             Make all warnings into errors.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *     <div>
     *       <p><br /></p>
     *     </div>
     *     <h4>Options Controlling the OpenCL C Version</h4>
     *     The following option controls the version of OpenCL C that the compiler accepts.
     *     <div>
     *       <dl>
     *         <dt><span>-cl-std=</span></dt>
     *         <dd>
     *           <p>
     *             Determine the OpenCL C language version to use. A value for this option
     *             must be provided. Valid values are:
     *           </p>
     *           <p>
     *             CL1.1 - Support all OpenCL C programs that use the OpenCL C language
     *             features defined in section 6 of the OpenCL 1.1 specification.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *     <p>
     *       Calls to <code>clBuildProgram</code> with the -cl-std=CL1.1 option will fail
     *       to build the program executable for any devices with CL_DEVICE_OPENCL_C_VERSION = OpenCL C 1.0.
     *     </p>
     *     <p>
     *       If the -cl-std build option is not specified, the CL_DEVICE_OPENCL_C_VERSION is used to select the
     *       version of OpenCL C to be used when building the program executable for each device.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clBuildProgram</code> returns <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PROGRAM</span> if <code>program</code> is not a valid program object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>device_list</code> is NULL
     *           and <code>num_devices</code> is greater than zero, or if <code>device_list</code> is not NULL
     *           and <code>num_devices</code> is zero.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>pfn_notify</code> is NULL
     *           but <code>user_data</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_DEVICE</span> if OpenCL devices listed in <code>device_list</code>
     *           are not in the list of devices associated with <code>program</code>.
     *         </li>
     *         <li><span>CL_INVALID_BINARY</span> if <code>program</code> is created
     *           with <span><span>clCreateWithProgramWithBinary</span></span> and
     *           devices listed in <code>device_list</code> do not have a valid program binary loaded.
     *         </li>
     *         <li><span>CL_INVALID_BUILD_OPTIONS</span> if the build options specified by <code>options</code> are invalid.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the build of a program executable for any of the devices
     *           listed in <code>device_list</code> by a previous call to <code>clBuildProgram</code> for <code>program</code> has not
     *           completed.
     *         </li>
     *         <li><span>CL_COMPILER_NOT_AVAILABLE</span> if <code>program</code> is created with
     *           <span><span>clCreateProgramWithSource</span></span>
     *           and a compiler is not available i.e.
     *           <span>CL_DEVICE_COMPILER_AVAILABLE</span> specified in the table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span> is set
     *           to <span>CL_FALSE</span>.
     *         </li>
     *         <li><span>CL_BUILD_PROGRAM_FAILURE</span> if there is a failure to build the program executable.
     *           This error will be returned if <code>clBuildProgram</code> does not return until the build has
     *           completed.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if there are kernel objects attached to <code>program</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clBuildProgram(cl_program program, int num_devices, cl_device_id device_list[], String options, BuildProgramFunction pfn_notify, Object user_data)
    {
        int result = clBuildProgramNative(program, num_devices, device_list, options, pfn_notify, user_data);

        if (result != CL.CL_SUCCESS)
        {
            if (exceptionsEnabled)
            {
                if (result != 1 && result != CL_BUILD_PROGRAM_FAILURE)
                {
                    throw new CLException(stringFor_errorCode(result));
                }
                else
                {
                    throw new CLException(stringFor_errorCode(result)+"\n"+obtainBuildLogs(program));
                }
            }
        }
        return result;
    }

    /**
     * Obtain a single String containing the build logs of the given program for
     * all devices that are associated with the given program object.
     *
     * @param program The program object
     * @return The build logs, as a single string.
     */
    private static String obtainBuildLogs(cl_program program)
    {
        int numDevices[] = new int[1];
        CL.clGetProgramInfo(program, CL.CL_PROGRAM_NUM_DEVICES, Sizeof.cl_uint, Pointer.to(numDevices), null);
        cl_device_id devices[] = new cl_device_id[numDevices[0]];
        CL.clGetProgramInfo(program, CL.CL_PROGRAM_DEVICES, numDevices[0] * Sizeof.cl_device_id, Pointer.to(devices), null);

        StringBuffer sb = new StringBuffer();
        for (int i=0; i<devices.length; i++)
        {
            sb.append("Build log for device "+i+":\n");
            long logSize[] = new long[1];
            CL.clGetProgramBuildInfo(program, devices[i], CL.CL_PROGRAM_BUILD_LOG, 0, null, logSize);
            byte logData[] = new byte[(int)logSize[0]];
            CL.clGetProgramBuildInfo(program, devices[i], CL.CL_PROGRAM_BUILD_LOG, logSize[0], Pointer.to(logData), null);
            sb.append(new String(logData, 0, logData.length-1));
            sb.append("\n");
        }
        return sb.toString();
    }



    private static native int clBuildProgramNative(cl_program program, int num_devices, cl_device_id device_list[], String options, BuildProgramFunction pfn_notify, Object user_data);

    /**
     * <p>
     *       Allows the implementation to release the resources allocated by the OpenCL compiler.
     *   </p>
     *
     * <div title="clUnloadCompiler">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clUnloadCompiler</b>
     *             (</code>
     *           <td>void <var></var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       This is a hint from the application and does not guarantee that the compiler will not be used in the future or that the compiler will actually be unloaded by the implementation. Calls to <span><span>clBuildProgram</span></span> after <code>clUnloadCompiler</code> will reload the compiler, if necessary, to build the appropriate program executable.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       This call currently always returns <span>CL_SUCCESS</span>.
     *     </p>
     *   </div>
     * </div>
     */
    public static int clUnloadCompiler()
    {
        return checkResult(clUnloadCompilerNative());
    }

    private static native int clUnloadCompilerNative();

    /**
     * <p>
     *      Returns information about the program object.
     *  </p>
     *
     * <div title="clGetProgramInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetProgramInfo</b>
     *             (</code>
     *           <td>cl_program <var>program</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_program_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           program
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the program object being queried.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported <code>param_name</code> types and the information returned in <code>param_value</code> by <code>clGetProgramInfo</code> is described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_program_info</th>
     *                   <th align="left">Return Type and Info. returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_REFERENCE_COUNT</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_uint
     *                     <p>Return the <code>program</code> reference count.</p>
     *                     <p>The reference count returned should be considered immediately stale. It is
     *                       unsuitable for general use in applications. This feature is provided for identifying
     *                       memory leaks.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_CONTEXT</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_context
     *                     <p>
     *                       Return the context specified when the program object is created
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_NUM_DEVICES</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_uint
     *                     <p>Return the number of devices associated with <code>program</code>.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_DEVICES</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_device_id[]
     *                     <p>Return the list of devices associated
     *                       with the program object. This can be
     *                       the devices associated with context on
     *                       which the program object has been
     *                       created or can be a subset of devices
     *                       that are specified when a progam object
     *                       is created using
     *                       <span><span>clCreateProgramWithBinary</span></span>.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_SOURCE</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: char[]
     *                     <p>
     *                       Return the program source code specified by
     *                       <span><span>clCreateProgramWithSource</span></span>. The
     *                       source string returned is a concatenation of all source strings
     *                       specified to
     *                       <span><span>clCreateProgramWithSource</span></span> with a
     *                       null terminator. The concatenation
     *                       strips any nulls in the original source strings.
     *                     </p>
     *                     <p>
     *                       The actual number of characters that
     *                       represents the program source code
     *                       including the null terminator is returned
     *                       in <code>param_value_size_ret</code>.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_BINARY_SIZES</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: size_t[]
     *                     <p>Returns an array that contains the size
     *                       in bytes of the program binary for each
     *                       device associated with <code>program</code>. The
     *                       size of the array is the number of
     *                       devices associated with <code>program</code>. If a
     *                       binary is not available for a device(s),
     *                       a size of zero is returned.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_BINARIES</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: unsigned char *[]
     *                     <p>
     *                       Return the program binaries for all
     *                       devices associated with <code>program</code>. For each device in <code>program</code>, the binary returned can be the binary specified for the device when <code>program</code> is created with <span><span>clCreateProgramWithBinary</span></span> or it can be the executable binary generated by <span><span>clBuildProgram</span></span>. If program is created with          <span><span>clCreateProgramWithSource</span></span>, the binary returned is the binary generated by <span><span>clBuildProgram</span></span>. The bits returned can be an implementation-specific intermediate representation (a.k.a. IR) or device specific executable bits or both. The decision on which information is returned in the binary is up to the OpenCL implementation.
     *                     </p>
     *                     <p>
     *                       <code>param_value</code> points to an array of <code>n</code>
     *                       pointers allocated by the caller, where <code>n</code> is the number of
     *                       devices associated with program. The
     *                       buffer sizes needed to allocate the
     *                       memory that these <code>n</code> pointers refer to
     *                       can be queried using the
     *                       <code>CL_PROGRAM_BINARY_SIZES</code> query
     *                       as described in this table.
     *                     </p>
     *                     <p>
     *                       Each entry in this array is used by the
     *                       implementation as the location in
     *                       memory where to copy the program
     *                       binary for a specific device, if there is a
     *                       binary available. To find out which
     *                       device the program binary in the array
     *                       refers to, use the
     *                       <code>CL_PROGRAM_DEVICES</code> query to get
     *                       the list of devices. There is a one-to-one
     *                       correspondence between the array
     *                       of <code>n</code> pointers returned by
     *                       <code>CL_PROGRAM_BINARIES</code> and array of
     *                       devices returned by <code>CL_PROGRAM_DEVICES.</code>
     *                     </p>
     *                     <p>
     *                       If an entry value in the array is NULL, the implementation
     *                       skips copying the program binary for the specific device
     *                       identified by the array index.
     *                     </p>
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned.
     *             If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Used to specify the size in bytes of memory pointed to by <code>param_value</code>.
     *             This size must be greater than or equal to the size of return type as described in the table above.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data copied to <code>param_value</code>.
     *             If  <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size in
     *           bytes specified by <code>param_value_size</code> is less than the size of return type as described
     *           in the table above and <code>param_value</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_PROGRAM</span> if <code>program</code> is not a valid program object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetProgramInfo(cl_program program, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetProgramInfoNative(program, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetProgramInfoNative(cl_program program, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Returns build information for each device in the program object.
     *   </p>
     *
     * <div title="clGetProgramBuildInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetProgramBuildInfo</b>
     *             (</code>
     *           <td>cl_program  <var>program</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_device_id  <var>device</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_program_build_info  <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t  <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void  <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t  <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>program</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p> Specifies the program object being queried.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>device</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the device for which build information is being queried.
     *             <code>device</code> must be a valid device associated with <code>program</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported <code>param_name</code> types and the
     *             information returned in <code>param_value</code> by <code>clGetProgramBuildInfo</code> is
     *             described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_program_buid_info</th>
     *                   <th align="left">Return Type and Info. returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROGRAM_BUILD_STATUS</code>
     *                   </td>
     *                   <td align="left">
     *                     Return type: cl_build_status
     *                     <p>
     *                       Returns the build status of <code>program</code> for a specific device
     *                       as given by <code>device</code>. This can be one of the following:
     *                     </p>
     *                     <p>
     *                       <span>CL_BUILD_NONE</span>. The build status returned if no build has
     *                       been performed on the specified program object for <code>device</code>.
     *                     </p>
     *                     <p>
     *                       <span>CL_BUILD_ERROR</span>. The build status returned if the last call
     *                       to <span><span>clBuildProgram</span></span> on the specified program object for <code>device</code>
     *                       generated an error.
     *                     </p>
     *                     <p>
     *                       <span>CL_BUILD_SUCCESS</span>. The build status returned if the last call
     *                       to <span><span>clBuildProgram</span></span> on the
     *                       specified program object for <code>device</code> was successful.
     *                     </p>
     *                     <p>
     *                       <span>CL_BUILD_IN_PROGRESS</span>. The build status returned if the last call
     *                       to  <span><span>clBuildProgram</span></span> on the
     *                       specified program object for <code>device</code> has not finished.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <span>CL_PROGRAM_BUILD_OPTIONS</span>
     *                   </td>
     *                   <td align="left">
     *                     Return type: char[]
     *                     <p>
     *                       Return the build options specified by the <code>options</code> argument
     *                       in  <span><span>clBuildProgram</span></span>
     *                       for <code>device</code>.
     *                     </p>
     *                     <p>
     *                       If build status of <code>program</code> for <code>device</code>
     *                       is <span>CL_BUILD_NONE</span>, an empty string is returned.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <span>CL_PROGRAM_BUILD_LOG</span>
     *                   </td>
     *                   <td align="left">
     *                     Return type: char[]
     *                     <p>
     *                       Return the build log when
     *                       <span><span>clBuildProgram</span></span> was called
     *                       for <code>device</code>. If build status of <code>program</code>
     *                       for <code>device</code> is <span>CL_BUILD_NONE</span>, an empty string
     *                       is returned.
     *                     </p>
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned.
     *             If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the size in bytes of memory pointed to by <code>param_value</code>. This size
     *             must be greater than or equal to the size of return type as described in the table above.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data copied to <code>param_value</code>.
     *             If <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise it returns the following:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           Returns <span>CL_INVALID_DEVICE</span> if <code>device</code>
     *           is not in the list of devices associated with <code>program</code>.
     *         </li>
     *         <li>
     *           Returns <span>CL_INVALID_VALUE</span> if <code>param_name</code>
     *           is not valid, or if size in bytes specified by <code>param_value_size</code>
     *           is less than size of return type
     *           and <code>param_value</code> is not NULL.
     *         </li>
     *         <li>
     *           Returns <span>CL_INVALID_PROGRAM</span> if <code>program</code>
     *           is a not a valid program object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate
     *           resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate
     *           resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetProgramBuildInfo(cl_program program, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetProgramBuildInfoNative(program, device, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetProgramBuildInfoNative(cl_program program, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Creates a kernal object.
     *   </p>
     *
     * <div title="clCreateKernel">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_kernel <b>clCreateKernel</b>
     *             (</code>
     *           <td>cl_program  <var>program</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const char <var>*kernel_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           program
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A program object with a successfully built executable.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           kernel_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A function name in the program declared with the <code>__kernel</code> qualifier
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       A kernel is a function declared in a program. A kernel is identified by the <span><span>__kernel</span></span> qualifier applied to any function in a program. A kernel object encapsulates the specific <code>__kernel</code> function declared in a program and the argument values to be used when executing this <code>__kernel</code> function.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clCreateKernel</code> returns a valid non-zero kernel object and <code>errcode_ret</code>
     *       is set to <span>CL_SUCCESS</span> if
     *       the kernel object is created successfully. Otherwise, it returns a NULL value with one of the
     *       following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PROGRAM</span> if <code>program</code> is not a valid program object.
     *         </li>
     *         <li><span>CL_INVALID_PROGRAM_EXECUTABLE</span> if there is no successfully built executable for
     *           <code>program</code>.
     *         </li>
     *         <li><span>CL_INVALID_KERNEL_NAME</span> if <code>kernel_name</code> is not found in <code>program</code>.
     *         </li>
     *         <li><span>CL_INVALID_KERNEL_DEFINITION</span> if the function definition for <code>__kernel</code> function
     *           given by <code>kernel_name</code> such as the number of arguments, the argument types are not the
     *           same for all devices for which the <code>program</code> executable has been built.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>kernel_name</code> is NULL.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_kernel clCreateKernel(cl_program program, String kernel_name, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_kernel result = clCreateKernelNative(program, kernel_name, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_kernel result = clCreateKernelNative(program, kernel_name, errcode_ret);
            return result;
        }
    }

    private static native cl_kernel clCreateKernelNative(cl_program program, String kernel_name, int errcode_ret[]);

    /**
     * <p>
     *       Creates kernel objects for all kernel functions in a program object.
     *   </p>
     *
     * <div title="clCreateKernelsInProgram">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clCreateKernelsInProgram</b>
     *             (</code>
     *           <td>cl_program  <var>program</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_kernels</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_kernel <var>*kernels</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>*num_kernels_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           program
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A program object with a successfully built executable.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_kernels
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The size of memory pointed to by <code>kernels</code> specified as the number of cl_kernel entries.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           kernels
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The buffer where the kernel objects for kernels in <code>program</code> will be returned. If <code>kernels</code> is NULL, it is ignored. If <code>kernels</code> is not NULL, <code>num_kernels</code> must be greater than or equal to the number of kernels in <code>program</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_kernels_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of kernels in <code>program</code>. If <code>num_kernels_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Creates kernel objects for all kernel functions in <code>program</code>. Kernel objects are not created for any <span><span>__kernel</span></span> functions in <code>program</code> that do not have the same function definition across all devices
     *       for which a program executable has been successfully built.
     *     </p>
     *     <p>
     *       Kernel objects can only be created once you have a program object with a valid program source
     *       or binary loaded into the program object and the program executable has been successfully built
     *       for one or more devices associated with program. No changes to the program executable are
     *       allowed while there are kernel objects associated with a program object. This means that calls to
     *       <span><span>clBuildProgram</span></span> return <span>CL_INVALID_OPERATION</span> if there are kernel objects attached to a
     *       program object. The OpenCL context associated with <code>program</code> will be the context associated
     *       with <code>kernel</code>. The list of devices associated with <code>program</code> are the devices associated with kernel.
     *       Devices associated with a program object for which a valid program executable has been built
     *       can be used to execute kernels declared in the program object.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the kernel objects are successfully allocated.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PROGRAM</span> if <code>program</code> is not a valid program object.
     *         </li>
     *         <li><span>CL_INVALID_PROGRAM_EXECUTABLE</span> if there is no successfully built executable for
     *           any device in <code>program</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>kernels</code> is not NULL
     *           and <code>num_kernels</code> is less than the number of kernels in <code>program</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clCreateKernelsInProgram(cl_program program, int num_kernels, cl_kernel kernels[], int num_kernels_ret[])
    {
        return checkResult(clCreateKernelsInProgramNative(program, num_kernels, kernels, num_kernels_ret));
    }

    private static native int clCreateKernelsInProgramNative(cl_program program, int num_kernels, cl_kernel kernels[], int num_kernels_ret[]);

    /**
     * <p>
     *       Increments the kernel object reference count.
     *   </p>
     *
     * <div title="clRetainKernel">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clRetainKernel</b>
     *             (</code>
     *           <td>cl_kernel <var>kernel</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <span><span>clCreateKernel</span></span> or <span><span>clCreateKernelsInProgram</span></span> do an implicit retain.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_KERNEL</span> if <code>kernel</code> is not a valid kernel object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clRetainKernel(cl_kernel kernel)
    {
        return checkResult(clRetainKernelNative(kernel));
    }

    private static native int clRetainKernelNative(cl_kernel kernel);

    /**
     * <p>
     *       Decrements the kernel reference count.
     *   </p>
     *
     * <div title="clReleaseKernel">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clReleaseKernel</b>
     *             (</code>
     *           <td>cl_kernel <var>kernel</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The kernel object is deleted once the number of instances that are retained
     *       to <code>kernel</code> become zero and the kernel object is no longer needed by any
     *       enqueued commands that use <code>kernel</code>.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the kernel objects are successfully alloctaed. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_KERNEL</span> if <code>kernel</code> is not a valid kernel object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clReleaseKernel(cl_kernel kernel)
    {
        return checkResult(clReleaseKernelNative(kernel));
    }

    private static native int clReleaseKernelNative(cl_kernel kernel);

    /**
     * <p>
     *       Used to set the argument value for a specific argument of a kernel.
     *   </p>
     *
     * <div title="clSetKernelArg">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clSetKernelArg</b>
     *             (</code>
     *           <td>cl_kernel <var>kernel</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>arg_index</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>arg_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const void <var>*arg_value</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>kernel</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid kernel object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>arg_index</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The argument index. Arguments to the kernel are referred by indices that go from 0 for the leftmost argument to <span><em>n</em></span> - 1, where <span><em>n</em></span> is the total number of arguments declared by a kernel.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>arg_value</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to data that should be used as the argument value for argument specified
     *             by <code>arg_index</code>. The argument data pointed to by <code>arg_value</code> is copied and the <code>arg_value</code> pointer can therefore be reused by the application after <code>clSetKernelArg</code> returns. The argument value specified is the value used by all API calls that enqueue <code>kernel</code> (<span><span>clEnqueueNDRangeKernel</span></span> and
     *             <span><span>clEnqueueTask</span></span>) until the argument value is changed by a call to <code>clSetKernelArg</code> for <code>kernel</code>.
     *           </p>
     *           <p>
     *             If the argument is a memory object (buffer or image), the <code>arg_value</code> entry will be a
     *             pointer to the appropriate buffer or image object. The memory object must be created with the context
     *             associated with the kernel object. A NULL value can also be specified if the argument is a buffer
     *             object in which case a NULL value will be used as the value for the argument declared as a
     *             pointer to <code>__global</code> or <code>__constant</code> memory in the kernel. If the
     *             argument is declared with the <code>__local</code> qualifier, the <code>arg_value</code>
     *             entry must be NULL. If the argument is of type <code>sampler_t</code>, the <code>arg_value</code>
     *             entry must be a pointer to the sampler object.
     *           </p>
     *           <p>
     *             If the argument is declared to be a pointer of a built-in or user defined type with
     *             the <code>__global</code> or <code>__constant</code> qualifier, the memory object specified as
     *             argument value must be a buffer object (or NULL). If the argument is declared with the <code>__constant</code>
     *             qualifier, the size in bytes of the memory object cannot exceed CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE and the
     *             number of arguments declared with the <code>__constant</code> qualifier cannot exceed
     *             CL_DEVICE_MAX_CONSTANT_ARGS.
     *           </p>
     *           <p>
     *             The memory object specified as argument value must be a 2D image object if the argument is
     *             declared to be of type <code>image2d_t</code>. The memory object specified as argument value must
     *             be a 3D image object if argument is declared to be of type <code>image3d_t</code>.
     *           </p>
     *           <p>
     *             For all other kernel arguments, the <code>arg_value</code> entry must be a pointer to the actual
     *             data to be used as argument value.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           arg_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the size of the argument value. If the argument is a memory object, the size is
     *             the size of the buffer or image object type. For arguments declared with the <code>__local</code>
     *             qualifier, the size specified will be the size in bytes of the buffer that must be allocated
     *             for the <code>__local</code> argument. If the argument is of type <code>sampler_t</code>,
     *             the <code>arg_size</code> value must be equal to <code>sizeof(cl_sampler)</code>. For all other
     *             arguments, the size will be the size of argument type.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       A kernel object does not update the reference count for objects such as memory, sampler
     *       objects specified as argument values by <code>clSetKernelArg</code>. Users may not rely on a kernel object
     *       to retain objects specified as argument values to the kernel.
     *     </p>
     *     <p>
     *       Implementations shall not allow cl_kernel objects to hold reference counts to cl_kernel arguments, because no
     *       mechanism is provided for the user to tell the kernel to release that ownership right. If the kernel holds ownership
     *       rights on kernel args, that would make it impossible for the user to tell with certainty when he may safely release
     *       user allocated resources associated with OpenCL objects such as the cl_mem backing store used with
     *       <code>CL_MEM_USE_HOST_PTR</code>.
     *     </p>
     *     <p>
     *       An OpenCL API call is considered to be thread-safe if the internal state as
     *       managed by OpenCL remains consistent when called simultaneously by multiple host threads.
     *       OpenCL API calls that are thread-safe allow an application to call these functions in multiple
     *       host threads without having to implement mutual exclusion across these host threads i.e. they are
     *       also re-entrant-safe.
     *     </p>
     *     <p>
     *       All OpenCL API calls are thread-safe except <code>clSetKernelArg</code>, which is safe to call
     *       from any host thread, and is safe to call re-entrantly so long as concurrent calls operate on
     *       different cl_kernel objects. However, the behavior of the cl_kernel object is undefined if
     *       <code>clSetKernelArg</code> is called from multiple host threads on the same cl_kernel object at the same
     *       time.
     *     </p>
     *     <p>
     *       There is an inherent race condition in the design of OpenCL that occurs between setting a kernel argument and
     *       using the kernel with <span><span>clEnqueueNDRangeKernel</span></span> or <span><span>clEnqueueTask</span></span>. Another host thread might change the kernel
     *       arguments between when a host thread sets the kernel arguments and then enqueues the kernel, causing the wrong
     *       kernel arguments to be enqueued. Rather than attempt to share cl_kernel objects among multiple host threads,
     *       applications are strongly encouraged to make additional cl_kernel objects for kernel functions for each host thread.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clSetKernelArg</code> returns <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_KERNEL</span> if <code>kernel</code> is not a valid kernel object.
     *         </li>
     *         <li><span>CL_INVALID_ARG_INDEX</span> if <code>arg_index</code> is not a valid argument index.
     *         </li>
     *         <li><span>CL_INVALID_ARG_VALUE</span> if <code>arg_value</code> specified is NULL for an argument that is not
     *           declared with the <code>__local</code> qualifier or vice-versa.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> for an argument declared to be a memory object when the
     *           specified <code>arg_value</code> is not a valid memory object.
     *         </li>
     *         <li><span>CL_INVALID_SAMPLER</span> for an argument declared to be of type <code>sampler_t</code> when
     *           the specified <code>arg_value</code> is not a valid sampler object.
     *         </li>
     *         <li><span>CL_INVALID_ARG_SIZE</span> if <code>arg_size</code> does not match the size of the
     *           data type for an argument that is not a memory object or if the argument is a memory object
     *           and <code>arg_size</code> != <code>sizeof(cl_mem)</code> or if <code>arg_size</code> is zero
     *           and the argument is declared with the <span><span>__local</span></span>
     *           qualifier or if the argument is a sampler and <code>arg_size</code> != <code>sizeof(cl_sampler)</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Example">
     *     <h3>
     *       Example
     *     </h3>
     *     <div>
     *       <table border="0">
     *         <colgroup>
     *           <col align="left" />
     *         </colgroup>
     *         <tbody>
     *           <tr>
     *             <td align="left">
     *               __kernel void
     *               image_filter (int n, int m,
     *               __constant float *filter_weights,
     *               __read_only image2d_t src_image,
     *               __write_only image2d_t dst_image)
     *               {
     *               ...
     *               }
     *             </td>
     *           </tr>
     *         </tbody>
     *       </table>
     *     </div>
     *     <p>
     *       Argument index values for <code>image_filter</code> will be 0 for n, 1 for m, 2 for
     *       <code>filter_weights</code>, 3 for <code>src_image</code> and 4 for <code>dst_image</code>.
     *     </p>
     *   </div>
     * </div>
     */
    public static int clSetKernelArg(cl_kernel kernel, int arg_index, long arg_size, Pointer arg_value)
    {
        return checkResult(clSetKernelArgNative(kernel, arg_index, arg_size, arg_value));
    }

    private static native int clSetKernelArgNative(cl_kernel kernel, int arg_index, long arg_size, Pointer arg_value);

    /**
     * <p>
     *       Returns information about the kernel object.
     *   </p>
     *
     * <div title="clGetKernelInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetKernelInfo</b>
     *             (</code>
     *           <td>cl_kernel <var>kernel</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_kernel_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           kernel
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the kernel object being queried.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported <code>param_name</code> types and the
     *             information returned in <code>param_value</code> by <code>clGetKernelInfo</code> is described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned. If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Used to specify the size in bytes of memory pointed to by <code>param_value</code>.
     *             This size must be greater than or equal to size of return type as described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_kernel_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Info. returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_FUNCTION_NAME</code>
     *                   </td>
     *                   <td align="left">char[]</td>
     *                   <td align="left">
     *                     Return the kernel function name.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_NUM_ARGS</code>
     *                   </td>
     *                   <td align="left">cl_uint</td>
     *                   <td align="left">
     *                     Return the number of arguments to <code>kernel</code>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_REFERENCE_COUNT</code>
     *                   </td>
     *                   <td align="left">cl_uint</td>
     *                   <td align="left">
     *                     <p>
     *                       Return the <code>kernel</code> reference count.
     *                     </p>
     *                     <p>
     *                       The reference count returned should be considered immediately stale. It is unsuitable for general use in
     *                       applications. This feature is provided for identifying memory leaks.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_CONTEXT</code>
     *                   </td>
     *                   <td align="left">cl_context</td>
     *                   <td align="left">
     *                     Return the context associated with <code>kernel</code>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_PROGRAM</code>
     *                   </td>
     *                   <td align="left">cl_program</td>
     *                   <td align="left">
     *                     Return the program object associated with <code>kernel</code>.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             the actual size in bytes of data copied to <code>param_value</code>. If <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise,
     *       it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size
     *           in bytes specified by  <code>param_value_size</code> is less than the size of return type as
     *           described in the table above and <code>param_value</code> is not NULL
     *         </li>
     *         <li><span>CL_INVALID_KERNEL</span> if <code>kernel</code> is not a valid kernel object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetKernelInfo(cl_kernel kernel, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetKernelInfoNative(kernel, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetKernelInfoNative(cl_kernel kernel, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Returns information about the kernel object that may be specific to a device.
     *   </p>
     *
     * <div title="clGetKernelWorkGroupInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetKernelWorkGroupInfo</b>
     *             (</code>
     *           <td>cl_kernel <var>kernel</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_device_id <var>device</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_kernel_work_group_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           kernel
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the kernel object being queried.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           device
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Identifies a specific device in the list of devices associated with <code>kernel</code>. The list of devices
     *             is the list of devices in the OpenCL context that is associated with <code>kernel</code>. If the list of devices
     *             associated with <code>kernel</code> is a single device, <code>device</code> can be a NULL value.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported <code>param_name</code> types and the
     *             information returned in <code>param_value</code> by <code>clGetKernelWorkGroupInfo</code> is described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned. If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Used to specify the size in bytes of memory pointed to by <code>param_value</code>.
     *             This size must be greater than or equal to size of return type as described in the table below.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_kernel_work_group_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Info. returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_WORK_GROUP_SIZE</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     This provides a mechanism for the
     *                     application to query the maximum work-group size
     *                     that can be used to execute a kernel on a
     *                     specific device given by <code>device</code>. The
     *                     OpenCL implementation uses the
     *                     resource requirements of the kernel
     *                     (register usage etc.) to determine what
     *                     this work-group size should be.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_COMPILE_WORK_GROUP_SIZE</code>
     *                   </td>
     *                   <td align="left">size_t[3]</td>
     *                   <td align="left">
     *                     Returns the work-group size specified
     *                     by the <code>__attribute__((reqd_work_gr
     *                     oup_size(X, Y, Z)))</code> qualifier.
     *                     See <span><span>Function Qualifiers</span></span>.
     *                     If the work-group size is not specified
     *                     using the above attribute qualifier (0, 0,
     *                     0) is returned.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_LOCAL_MEM_SIZE</code>
     *                   </td>
     *                   <td align="left">cl_ulong</td>
     *                   <td align="left">
     *                     <p>
     *                       Returns the amount of local memory in
     *                       bytes being used by a kernel. This
     *                       includes local memory that may be
     *                       needed by an implementation to execute
     *                       the kernel, variables declared inside the
     *                       kernel with the <span><span>__local</span></span> address
     *                       qualifier and local memory to be
     *                       allocated for arguments to the kernel
     *                       declared as pointers with the <span><span>__local</span></span>
     *                       address qualifier and whose size is
     *                       specified with <span><span>clSetKernelArg</span></span>.
     *                     </p>
     *                     <p>
     *                       If the local memory size, for any pointer
     *                       argument to the kernel declared with the
     *                       <span><span>__local</span></span> address qualifier, is not
     *                       specified, its size is assumed to be 0.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE</code>
     *                   </td>
     *                   <td align="left">size_t</td>
     *                   <td align="left">
     *                     Returns the preferred multiple of workgroup size for launch. This is
     *                     a performance hint. Specifying a workgroup size that is not a multiple
     *                     of the value returned by this query as the value of the local work size argument
     *                     to <span><span>clEnqueueNDRangeKernel</span></span>will
     *                     not fail to enqueue the kernel for execution unless the work-group size specified is
     *                     larger than the device maximum.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_KERNEL_PRIVATE_MEM_SIZE</code>
     *                   </td>
     *                   <td align="left">cl_ulong</td>
     *                   <td align="left">
     *                     Returns the minimum amount of private memory, in bytes, used by each workitem in the
     *                     kernel. This value may include any private memory needed by an implementation to
     *                     execute the kernel, including that used by the language built-ins and variable declared
     *                     inside the kernel with the <span><span>__private</span></span> qualifier.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data copied to <code>param_value</code>. If <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns
     *       one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_DEVICE</span> if <code>device</code> is not in the list of devices
     *           associated with <code>kernel</code> or if <code>device</code> is NULL but there is more than
     *           one device associated with <code>kernel</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size in
     *           bytes specified by <code>param_value_size</code> is less than the size of return type as described in
     *           the table above and <code>param_value</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_KERNEL</span> if <code>kernel</code> is a not a valid kernel object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetKernelWorkGroupInfo(cl_kernel kernel, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetKernelWorkGroupInfoNative(kernel, device, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetKernelWorkGroupInfoNative(cl_kernel kernel, cl_device_id device, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Waits on the host thread for commands identified by event objects to complete.
     *   </p>
     *
     * <div title="clWaitForEvents">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clWaitForEvents</b>
     *             (</code>
     *           <td>cl_uint <var>num_events</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_list</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>event_list</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The events specified in <code>event_list</code> act as synchronization points.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Waits on the host thread for commands identified by event objects in <code>event_list</code> to
     *       complete. A command is considered complete if its execution status is <code>CL_COMPLETE</code>
     *       or a negative value.
     *     </p>
     *     <p>
     *       If the <span><span>cl_khr_gl_event</span></span> extension
     *       is enabled, event objects can also be used to reflect the status of an OpenGL sync object. The sync object
     *       in turn refers to a fence command executing in an OpenGL command stream. This provides another method of
     *       coordinating sharing of buffers and images between OpenGL and OpenCL.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the execution status of all
     *       events in <code>event_list</code> is CL_COMPLETE.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>num_events</code> is zero or <code>event_list</code> is NULL..
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if events specified in <code>event_list</code> do
     *           not belong to the same context.
     *         </li>
     *         <li><span>CL_INVALID_EVENT</span> if event objects specified in <code>event_list</code>
     *           are not valid event objects.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the execution status
     *           of any of the events in <code>event_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clWaitForEvents(int num_events, cl_event event_list[])
    {
        return checkResult(clWaitForEventsNative(num_events, event_list));
    }

    private static native int clWaitForEventsNative(int num_events, cl_event event_list[]);

    /**
     * <p>
     *       Returns information about the event object.
     *   </p>
     *
     * <div title="clGetEventInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>
     *             clGetEventInfo
     *             </b>
     *             (</code>
     *           <td>cl_event <var>event</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the event object being queried.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned. If <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the size in bytes of memory pointed to by <code>param_value</code>. This size must be greater than or equal to the size of the return type as described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data copied to <code>param_value</code>. If
     *             <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the information to query. The list of supported <code>param_name</code> types and the information returned in <code>param_value</code> by <code>clGetEventInfo</code> is described in the table below:
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_event_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Information returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_EVENT_COMMAND_QUEUE</code>
     *                   </td>
     *                   <td align="left">cl_command_queue</td>
     *                   <td align="left">
     *                     Return the command-queue associated with <code>event</code>.
     *                     For user event objects, a NULL value is returned.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_EVENT_CONTEXT</code>
     *                   </td>
     *                   <td align="left">cl_context</td>
     *                   <td align="left">
     *                     Return the context associated with <code>event</code>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_EVENT_COMMAND_TYPE</code>
     *                   </td>
     *                   <td align="left">cl_command_type</td>
     *                   <td align="left">
     *                     Return the command associated with <code>event</code>.
     *                     Can be one of the following values:
     *                     <div>
     *                       <p>CL_COMMAND_NDRANGE_KERNEL<br />
     *                         CL_COMMAND_TASK<br />
     *                         CL_COMMAND_NATIVE_KERNEL<br />
     *                         CL_COMMAND_READ_BUFFER<br />
     *                         CL_COMMAND_WRITE_BUFFER<br />
     *                         CL_COMMAND_COPY_BUFFER<br />
     *                         CL_COMMAND_READ_IMAGE<br />
     *                         CL_COMMAND_WRITE_IMAGE<br />
     *                         CL_COMMAND_COPY_IMAGE<br />
     *                         CL_COMMAND_COPY_BUFFER_TO_IMAGE<br />
     *                         CL_COMMAND_COPY_IMAGE_TO_BUFFER<br />
     *                         CL_COMMAND_MAP_BUFFER<br />
     *                         CL_COMMAND_MAP_IMAGE<br />
     *                         CL_COMMAND_UNMAP_MEM_OBJECT<br />
     *                         CL_COMMAND_MARKER<br />
     *                         CL_COMMAND_ACQUIRE_GL_OBJECTS<br />
     *                         CL_COMMAND_RELEASE_GL_OBJECTS<br />
     *                         CL_COMMAND_READ_BUFFER_RECT<br />
     *                         CL_COMMAND_WRITE_BUFFER_RECT<br />
     *                         CL_COMMAND_COPY_BUFFER_RECT<br />
     *                         CL_COMMAND_USER<br />
     *                         CL_COMMAND_GL_FENCE_SYNC_OBJECT_KHR <br />
     *                           (if cl_khr_gl_event is enabled)<br />
     *                         CL_COMMAND_ACQUIRE_D3D10_OBJECTS_KHR<br />
     *                           (if cl_khr_d3d10_sharing is enabled)<br />
     *                         CL_COMMAND_RELEASE_D3D10_OBJECTS_KHR  <br />
     *                           (if cl_khr_d3d10_sharing is enabled)
     *                       </p>
     *                     </div>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_EVENT_COMMAND_ EXECUTION_STATUS</code>
     *                   </td>
     *                   <td align="left">cl_int</td>
     *                   <td align="left">
     *                     Return the execution status of the command identified by <code>event</code>. The valid values are:
     *                     <p>CL_QUEUED (command has been enqueued in the command-queue),</p>
     *                     <p>CL_SUBMITTED (enqueued command has been submitted by the host to the device associated with the command-queue),</p>
     *                     <p>CL_RUNNING (device is currently executing this command),</p>
     *                     <p>CL_COMPLETE (the command has completed), or</p>
     *                     <p>Error code given by a negative integer value. (command was abnormally terminated â€“ this
     *                       may be caused by a bad memory access etc.)
     *                     </p>
     *                     <p>These error codes come from the same set of error codes that are returned from the platform or runtime API calls as return values or <code>errcode_ret</code> values.</p>
     *                     <p>
     *                       The error code values are negative, and event state values are positive. The event state values are ordered from the largest value (CL_QUEUED) for the first or initial state to the smallest value (CL_COMPLETE or negative integer value) for the last or complete state. The value of CL_COMPLETE and CL_SUCCESS are the same.
     *                     </p>
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_EVENT_REFERENCE_COUNT</code>
     *                   </td>
     *                   <td align="left">cl_uint</td>
     *                   <td align="left">
     *                     Return the <code>event</code> reference count. The reference count returned should be considered immediately stale. It is unsuitable for general use in applications. This feature is provided for identifying memory leaks.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Using <code>clGetEventInfo</code> to determine if a command identified by event has finished execution (i.e. <code>CL_EVENT_COMMAND_EXECUTION_STATUS</code> returns <code>CL_COMPLETE</code>) is not a synchronization point. There are no guarantees that the memory objects being modified by command associated with <code>event</code> will be visible to other enqueued commands.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function executed successfully, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size in
     *           bytes specified by <code>param_value_size</code> is less than size of return type as described
     *           in the table above and <code>param_value</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if information to query given in <code>param_name</code>
     *           cannot be queried for event.
     *         </li>
     *         <li><span>CL_INVALID_EVENT</span> if <code>event</code> is not a valid
     *           <code>event</code> object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetEventInfo(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetEventInfoNative(event, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetEventInfoNative(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);



    /**
     * <p>
     *       Creates a user event object.
     *   </p>
     *
     * <div title="clCreateUserEvent">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_event <b>clCreateUserEvent</b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>context</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A valid OpenCL context used to create the user event object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>errcode_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL,
     *             no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       User events allow applications to enqueue commands that wait on a user event to finish before the
     *       command is executed by the device.
     *     </p>
     *     <p>
     *       The execution status of the user event object created is set to CL_SUBMITTED.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero buffer object and <code>errcode_ret</code> is set to <span>CL_SUCCESS</span> if
     *       the user event object is created successfully. Otherwise, it returns a NULL value with one of the
     *       following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_event clCreateUserEvent(cl_context context, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_event result = clCreateUserEventNative(context, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_event result = clCreateUserEventNative(context, errcode_ret);
            return result;
        }
        
    }
    private static native cl_event clCreateUserEventNative(cl_context context, int errcode_ret[]);




    /**
     * <p>
     *       Increments the <code>event</code> reference count.
     *   </p>
     *
     * <div title="clRetainEvent">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clRetainEvent</b>
     *             (</code>
     *           <td>cl_event <var>event</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>event</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Event object being retained.</p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The OpenCL commands that return an event perform an implicit retain.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function executed successfully, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_EVENT</span> if <code>event</code> is not a valid event object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clRetainEvent(cl_event event)
    {
        return checkResult(clRetainEventNative(event));
    }

    private static native int clRetainEventNative(cl_event event);

    /**
     * <p>
     *       Decrements the <code>event</code> reference count.
     *   </p>
     *
     * <div title="clReleaseEvent">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clReleaseEvent</b>
     *             (</code>
     *           <td>cl_event <var>event</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>event</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Event object being released.</p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Decrements the <code>event</code> reference count. The event object is deleted once the reference count becomes zero, the specific command identified by this event has completed (or terminated) and there are no commands in the command-queues             of a context that require a wait for this event to complete.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function executed successfully, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_EVENT</span> if <code>event</code> is not a valid event object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clReleaseEvent(cl_event event)
    {
        return checkResult(clReleaseEventNative(event));
    }

    private static native int clReleaseEventNative(cl_event event);


    /**
     * <p>
     *       Sets the execution status of a user event object.
     *   </p>
     *
     * <div title="clSetUserEventStatus">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_mem <b>clSetUserEventStatus</b>
     *             (</code>
     *           <td>cl_event <var>event</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>execution_status</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>event</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A user event object created using
     *             <span><span>clCreateUserEvent</span></span>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>execution_status</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the new execution status to be set and can be CL_COMPLETE or a negative integer value
     *             to indicate an error. A negative integer value causes all enqueued commands that wait on this user event
     *             to be terminated. <code>clSetUserEventStatus</code> can only be called once to change the execution
     *             status of <code>event</code>.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Enqueued commands that specify user events in the <code>event_wait_list</code> argument of
     *       <code>clEnqueue***</code> commands must ensure that the status of these user events being waited on are
     *       set using <code>clSetUserEventStatus</code> before any OpenCL APIs that release OpenCL objects except for
     *       event objects are called; otherwise the behavior is undefined.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function was executed successfully. Otherwise, it
     *       returns one of the following errors
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_EVENT</span> if <code>event</code> is not a valid user event.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the <code>execution_status</code> is not CL_COMPLETE
     *           or a negative integer value.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the <code>execution_status</code> for
     *           <code>event</code> has already been changed by a previous call to
     *           <code>clSetUserEventStatus</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Example">
     *     <h3>
     *       Example
     *     </h3>
     *     <p>
     *       For example, the following code sequence will result in undefined behavior
     *       of <span><span>clReleaseMemObject</span></span>.
     *     </p>
     *     <div>
     *       <table border="0">
     *         <colgroup>
     *           <col align="left" />
     *         </colgroup>
     *         <tbody>
     *           <tr>
     *             <td align="left">
     *               ev1 = clCreateUserEvent(ctx, NULL);
     *               clEnqueueWriteBuffer(cq, buf1, CL_FALSE, ..., 1, &amp;ev1, NULL;
     *               clEnqueueWriteBuffer(cq, buf2, CL_FALSE,...);
     *               clReleaseMemObject(buf2);
     *               clSetUserEventStatus(ev1, CL_COMPLETE);
     *             </td>
     *           </tr>
     *         </tbody>
     *       </table>
     *     </div>
     *     <p>
     *       The following code sequence, however, works correctly.
     *     </p>
     *     <div>
     *       <table border="0">
     *         <colgroup>
     *           <col align="left" />
     *         </colgroup>
     *         <tbody>
     *           <tr>
     *             <td align="left">
     *               ev1 = clCreateUserEvent(ctx, NULL);
     *               clEnqueueWriteBuffer(cq, buf1, CL_FALSE, ..., 1, &amp;ev1, NULL;
     *               clEnqueueWriteBuffer(cq, buf2, CL_FALSE,...);
     *               clSetUserEventStatus(ev1, CL_COMPLETE);
     *               clReleaseMemObject(buf2);
     *             </td>
     *           </tr>
     *         </tbody>
     *       </table>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clSetUserEventStatus(cl_event event, int execution_status)
    {
        return checkResult(clSetUserEventStatusNative(event, execution_status));
    }
    private static native int clSetUserEventStatusNative(cl_event event, int execution_status);

    /**
     * <p>
     *       Registers a user callback function for a specific command execution status.
     *   </p>
     *
     * <div title="clSetEventCallback">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int <b>clSetEventCallback</b>
     *             (</code>
     *           <td>cl_event <var>event</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>
     *             cl_int
     *              <var>command_exec_callback_type</var>
     *             ,
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void (CL_CALLBACK  <var>*pfn_event_notify)
     *             (cl_event event, cl_int event_command_exec_status,
     *             void *user_data</var>),
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*user_data</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>event</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>A valid event object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>command_exec_callback_type</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The command execution status for which the callback is registered. The command execution callback
     *             value for which a callback can be registered is CL_COMPLETE. There is no guarantee that the callback
     *             functions registered for various execution status values for an event will be called in the exact
     *             order that the execution status of a command changes.
     *           </p>
     *           <p>
     *             The callback function registered for a <code>command_exec_callback_type</code> value of
     *             CL_COMPLETE will be called when the command has completed successfully or is abnormally terminated.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>pfn_event_notify</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The event callback function that can be registered by the application.
     *             This callback function may be called asynchronously by the OpenCL
     *             implementation. It is the application's responsibility to ensure
     *             that the callback function is thread-safe. The parameters to this callback function are:
     *           </p>
     *           <div>
     *             <ul type="disc">
     *               <li><code>event</code>: the event object for which the callback function is invoked.
     *               </li>
     *               <li><code>event_command_exec_status</code>: the execution status of command for which
     *                 this callback function is invoked. See the table of values for
     *                 <span><span>clGetEventInfo</span></span><code>param_value</code> for the command execution status values. If the callback
     *                 is
     *                 called as the result of the command associated with event being abnormally terminated, an
     *                 appropriate error code for the error that caused the termination will be passed to
     *                 <code>event_command_exec_status</code> instead.
     *               </li>
     *               <li><code>user_data</code>: a pointer to user supplied data.
     *               </li>
     *             </ul>
     *           </div>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>user_data</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Will be passed as the <code>user_data</code> argument when <code>pfn_notify</code>
     *             is called. <code>user_data</code> can be NULL.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The registered callback function will be called when the execution status of command associated with
     *       <code>event</code> changes to the execution status specified by <code>command_exec_status</code>.
     *     </p>
     *     <p>
     *       Each call to <code>clSetEventCallback</code> registers the specified user callback function on a
     *       callback stack associated with <code>event</code>. The order in which the registered user callback
     *       functions are called is undefined.
     *     </p>
     *     <p>
     *       All callbacks registered for an event object must be called. All enqueued
     *       callbacks shall be called before the event object is destroyed. Callbacks must
     *       return promptly. The behavior of calling expensive system routines, OpenCL API
     *       calls to create contexts or command-queues, or blocking OpenCL operations from
     *       the following list below, in a callback is undefined.
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           <span>
     *           <span>clFinish</span>
     *           </span>
     *         </li>
     *         <li>
     *           <span>
     *           <span>clWaitForEvents</span>
     *           </span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clEnqueueReadBuffer</span></span>,
     *           <span><span>clEnqueueReadBufferRect</span></span>, <span><span>clEnqueueWriteBuffer</span></span>, and  <span><span>clEnqueueWriteBufferRect</span></span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clEnqueueReadImage</span></span> and <span><span>clEnqueueWriteImage</span></span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clEnqueueMapBuffer</span></span> and <span><span>clEnqueueMapImage</span></span>
     *         </li>
     *         <li>
     *           blocking calls to <span><span>clBuildProgram</span></span>
     *         </li>
     *       </ul>
     *     </div>
     *     <p>
     *       If an application needs to wait for completion of a routine from the above l
     *       ist in a callback, please use the non-blocking form of the function, and assign
     *       a completion callback to it to do the remainder of your work. Note that when a
     *       callback (or other code) enqueues commands to a command-queue, the commands are
     *       not required to begin execution until the queue is flushed. In standard usage,
     *       blocking enqueue calls serve this role by implicitly flushing the queue. Since
     *       blocking calls are not permitted in callbacks, those callbacks that enqueue
     *       commands on a command queue should either call
     *       <span><span>clFlush</span></span> on the queue
     *       before returning or arrange for
     *       <span><span>clFlush</span></span> to be
     *       called later on another thread.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns
     *       one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_EVENT</span> if <code>event</code> is not a valid event object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>pfn_event_notify</code> is NULL or if
     *           <code>command_exec_callback_type</code> is not CL_COMPLETE.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clSetEventCallback(cl_event event, int command_exec_callback_type, EventCallbackFunction pfn_notify, Object user_data)
    {
        return checkResult(clSetEventCallbackNative(event, command_exec_callback_type, pfn_notify, user_data));
    }
    private static native int clSetEventCallbackNative(cl_event event, int command_exec_callback_type, EventCallbackFunction pfn_notify, Object user_data);




    /**
     * <p>
     *       Returns profiling information for the command associated with event if profiling is enabled.
     *   </p>
     *
     * <div title="clGetEventProfilingInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clGetEventProfilingInfo</b>
     *             (</code>
     *           <td>cl_event <var>event</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_profiling_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>event</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the event object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_name</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Specifies the profiling data to query. The list of supported <code>param_name</code> types and the information returned in <code>param_value</code> by <code>clGetEventProfilingInfo</code> is described in the table of parameter queries below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the size in bytes of memory pointed to by <code>param_value</code>.
     *             This size must be greater than or equal to the size of return type as described in the table bolow.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the appropriate result being queried is returned. If
     *             <code>param_value</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>param_value_size_ret</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data copied to <code>param_value</code>. If
     *             <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt></dt>
     *         <dd>
     *           <p>
     *             The following is a table of <code>clGetEventProfilingInfo</code> parameter queries
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_profiling_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Info. returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROFILING_COMMAND_QUEUED</code>
     *                   </td>
     *                   <td align="left">cl_ulong</td>
     *                   <td align="left">
     *                     A 64-bit value that describes the current
     *                     device time counter in nanoseconds when
     *                     the command identified by <code>event</code> is
     *                     enqueued in a command-queue by the
     *                     host.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROFILING_COMMAND_SUBMIT</code>
     *                   </td>
     *                   <td align="left">cl_ulong</td>
     *                   <td align="left">
     *                     A 64-bit value that describes the current
     *                     device time counter in nanoseconds when
     *                     the command identified by <code>event</code> that has
     *                     been enqueued is submitted by the host to
     *                     the device associated with the command-queue.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROFILING_COMMAND_START</code>
     *                   </td>
     *                   <td align="left">cl_ulong</td>
     *                   <td align="left">
     *                     A 64-bit value that describes the current
     *                     device time counter in nanoseconds when
     *                     the command identified by <code>event</code> starts
     *                     execution on the device.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_PROFILING_COMMAND_END</code>
     *                   </td>
     *                   <td align="left">cl_ulong</td>
     *                   <td align="left">
     *                     A 64-bit value that describes the current
     *                     device time counter in nanoseconds when
     *                     the command identified by <code>event</code> has
     *                     finished execution on the device.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The unsigned 64-bit values returned can be used to measure the time in nano-seconds consumed
     *       by OpenCL commands.
     *     </p>
     *     <p>
     *       OpenCL devices are required to correctly track time across changes in device frequency and
     *       power states. The <code>CL_DEVICE_PROFILING_TIMER_RESOLUTION</code> specifies the resolution of
     *       the timer i.e. the number of nanoseconds elapsed before the timer is incremented.
     *     </p>
     *     <p>
     *       Event objects can be used to capture profiling information that measure execution time of a
     *       command. Profiling of OpenCL commands can be enabled either by using a command-queue
     *       created with CL_QUEUE_PROFILING_ENABLE flag set in <code>properties</code> argument to
     *       <span><span>clCreateCommandQueue</span></span>.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the function is executed successfully and the profiling information has been recorded. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_PROFILING_INFO_NOT_AVAILABLE</span> if the CL_QUEUE_PROFILING_ENABLE
     *           flag is not set for the command-queue, if the execution status of the command identified
     *           by <code>event</code> is not CL_COMPLETE or if <code>event</code> is a user event
     *           object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size in bytes
     *           specified by <code>param_value_size</code> is less than the size of return type as described in the above
     *           table and <code>param_value</code> is not NULL.
     *         </li>
     *         <li><span>CL_INVALID_EVENT</span> if <code>event</code> is a not a valid event object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetEventProfilingInfo(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetEventProfilingInfoNative(event, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetEventProfilingInfoNative(cl_event event, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Issues all previously queued OpenCL commands in a command-queue to the device associated with the command-queue.
     *   </p>
     *
     * <div title="clFlush">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clFlush</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Issues all previously queued OpenCL commands in <code>command_queue</code> to the device associated
     *       with <code>command_queue</code>.
     *     </p>
     *     <p>
     *       <code>clFlush</code> only guarantees that all queued commands to <code>command_queue</code>
     *       get issued to the appropriate device. There is no guarantee that they will be complete after
     *       <code>clFlush</code> returns.
     *     </p>
     *     <p>
     *       Any blocking commands queued in a command-queue perform an implicit flush of the command-queue.
     *       These blocking commands are
     *       <span><span>clEnqueueReadBuffer</span></span>,
     *       <span><span>clEnqueueReadBufferRect</span></span>, or
     *       <span><span>clEnqueueReadImage</span></span> with
     *       <code>blocking_read</code> set to <span>CL_TRUE</span>;
     *       <span><span>clEnqueueWriteBuffer</span></span>,
     *       <span><span>clEnqueueWriteBufferRect</span></span>, or
     *       <span><span>clEnqueueWriteImage</span></span> with
     *       <code>blocking_write</code> set to <span>CL_TRUE</span>;
     *       <span><span>clEnqueueMapBuffer</span></span> or
     *       <span><span>clEnqueueMapImage</span></span> with
     *       <code>blocking_map</code> set to <span>CL_TRUE</span>;
     *       or <span><span>clWaitForEvents</span></span>.
     *     </p>
     *     <p>
     *       To use event objects that refer to commands enqueued in a command-queue as event objects to
     *       wait on by commands enqueued in a different command-queue, the application must call a
     *       <code>clFlush</code> or any blocking commands that perform an implicit flush of the command-queue where
     *       the commands that refer to these event objects are enqueued.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function call was executed successfully.
     *       Otherwise, it returns one of the following:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the device.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clFlush(cl_command_queue command_queue)
    {
        return checkResult(clFlushNative(command_queue));
    }

    private static native int clFlushNative(cl_command_queue command_queue);

    /**
     * <p>
     *       Blocks until all previously queued OpenCL commands in a command-queue are issued to the associated device and have completed.
     *   </p>
     *
     * <div title="clFinish">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clFinish</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Blocks until all previously queued OpenCL commands in <code>command_queue</code> are issued to the
     *       associated device and have completed.
     *     </p>
     *     <p>
     *       <code>clFinish</code> does not return until all queued commands in
     *       <code>command_queue</code> have been processed and completed. <code>clFinish</code> is also a synchronization point.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function call was executed successfully.
     *       Otherwise, it returns one of the following:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by
     *           the OpenCL implementation on the host.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the device.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clFinish(cl_command_queue command_queue)
    {
        return checkResult(clFinishNative(command_queue));
    }

    private static native int clFinishNative(cl_command_queue command_queue);

    /**
     * <p>
     *       Enqueue commands to read from a buffer object to host memory.
     *   </p>
     *
     * <div title="clEnqueueReadBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueReadBuffer</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_read</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>cb</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>command_queue</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the command-queue in which the read command will be
     *             queued. <code>command_queue</code> and <code>buffer</code> must be created with the same OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Refers to a valid buffer object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>blocking_read</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the read operations are <code>blocking</code> or non-blocking. If <code>blocking_read</code> is <code>CL_TRUE</code> i.e. the read command is blocking, <code>clEnqueueReadBuffer</code> does not return until the buffer data has been read and copied into memory pointed to by <code>ptr</code>.
     *           </p>
     *           <p>
     *             If <code>blocking_read</code> is <code>CL_FALSE</code> i.e. the read command is <code>non-blocking</code>, <code>clEnqueueReadBuffer</code>
     *             queues a <code>non-blocking</code> read command and returns. The contents of the buffer that <code>ptr</code> points to
     *             cannot be used until the read command has completed. The <code>event</code> argument returns an event
     *             object which can be used to query the execution status of the read command. When the read
     *             command has completed, the contents of the buffer that <code>ptr</code> points to can be used by the
     *             application.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>offset</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The offset in bytes in the buffer object to read from.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>cb </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The size in bytes of data being read.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>ptr </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The pointer to buffer in host memory where data is to be read into.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             <code>event_wait_list</code> and <code>num_events_in_wait_list</code> specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular read command and can be used
     *             to query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which
     *             case it will not be possible for the application to query the status of this command or queue a
     *             wait for this command to complete.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Calling <code>clEnqueueReadBuffer</code> to read a region of the buffer object with the <code>ptr</code> argument value
     *       set to <code>host_ptr</code> + <code>offset</code>, where <code>host_ptr</code> is a pointer to the memory region specified when the
     *       buffer object being read is created with <code>CL_MEM_USE_HOST_PTR</code>, must meet the following
     *       requirements in order to avoid undefined behavior:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>All commands that use this buffer object or a memory object (buffer or image) created
     *           from this buffer object have finished execution before the read command begins execution.
     *         </li>
     *         <li>The buffer object or memory objects created from this buffer object are not mapped.</li>
     *         <li>The buffer object or memory objects created from this buffer object are not used by any
     *           command-queue until the read command has finished execution.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueReadBuffer</code> returns <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code> and <code>buffer</code> are not the same or if the context associated with <code>command_queue</code> and events in
     *           <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>buffer</code> is not a valid buffer object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the region being read specified by (<code>offset</code>, <code>cb</code>) is out of bounds or if <code>ptr</code> is a NULL value.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if <code>buffer</code> is a sub-buffer object and <code>offset</code>
     *           specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with
     *           <code>queue</code>.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the
     *           read and write operations are blocking and the execution status of any of
     *           the events in <code>event_wait_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     * <br />
     * <u>Note:</u> For non-blocking read operations, the given Pointer must be a
     * Pointer to a direct buffer. Otherwise, an IllegalArgumentException will
     * be thrown.
     *
     * @throws IllegalArgumentException If <code>blocking_read==false</code> and
     * the given Pointer is <i>not</i> a Pointer to a direct buffer.
     */
    public static int clEnqueueReadBuffer(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {

        if (!blocking_read && !ptr.isDirectBufferPointer())
        {
            throw new IllegalArgumentException(
                "Non-blocking read operations may only be " +
                "performed using pointers to direct buffers");
        }

        return checkResult(clEnqueueReadBufferNative(command_queue, buffer, blocking_read, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event));

        // NON_BLOCKING_READ
        /*
        //blocking_read = true;

        if (blocking_read)
        {
            int result = clEnqueueReadBufferNative(command_queue, buffer, blocking_read, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event);
            return checkResult(result);
        }
        else
        {
            if (event == null)
            {
                event = new cl_event();
            }
            int result = clEnqueueReadBufferNative(command_queue, buffer, blocking_read, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event);
            clEnqueueMarker(command_queue, event);
            schedulePointerDataRelease(event);
            return checkResult(result);
        }
        */
    }

    private static native int clEnqueueReadBufferNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);



    /**
     * <p>
     *       Enqueue commands to read from a rectangular region from a buffer object to host memory.
     *   </p>
     *
     * <div title="clEnqueueReadBufferRect">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueReadBufferRect</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_read</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>buffer_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>host_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>buffer_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>buffer_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>host_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>host_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>command_queue</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the command-queue in which the read command will be
     *             queued. <code>command_queue</code> and <code>buffer</code> must be created with the same OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Refers to a valid buffer object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>blocking_read</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the read operations are <code>blocking</code> or non-blocking. If <code>blocking_read</code> is <code>CL_TRUE</code> i.e. the read command is blocking, <code>clEnqueueReadBufferRect</code> does not return until the buffer data has been read and copied into memory pointed to by <code>ptr</code>.
     *           </p>
     *           <p>
     *             If <code>blocking_read</code> is <code>CL_FALSE</code> i.e. the read command is <code>non-blocking</code>, <code>clEnqueueReadBufferRect</code>
     *             queues a <code>non-blocking</code> read command and returns. The contents of the buffer that <code>ptr</code> points to
     *             cannot be used until the read command has completed. The <code>event</code> argument argument returns an event object which can be used to query the execution status of the read command. When the read command has completed, the contents of the buffer that <code>ptr</code> points to can be used by the application.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer_origin</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The (<span><em>x, y, z</em></span>) offset in the memory region associated with <code>buffer</code>. For a 2D rectangle region, the <code>z</code> value given by <code>buffer_origin</code>[2] should be 0. The offset in bytes is computed as <code>buffer_origin</code>[2] * <code>buffer_slice_pitch</code> + <code>buffer_origin</code>[1] * <code>buffer_row_pitch</code> + <code>buffer_origin</code>[0].</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>host_origin</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The (<span><em>x, y, z</em></span>) offset in the memory region pointed to by <code>ptr</code>. For a 2D rectangle region, the <code>z</code> value given by <code>host_origin</code>[2] should be 0. The offset in bytes is computed as <code>host_origin</code>[2] * <code>host_slice_pitch</code> + <code>host_origin</code>[1] * <code>host_row_pitch</code> + <code>host_origin</code>[0].</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>region</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The (<span><em>width, height, depth</em></span>) in bytes of the 2D or 3D rectangle being read or written. For a 2D rectangle copy, the <code>depth</code> value given by <code>region</code>[2] should be 1. </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer_row_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each row in bytes to be used for the memory region associated with <code>buffer</code>. If <code>buffer_row_pitch</code> is 0, <code>buffer_row_pitch</code> is computed as <code>region</code>[0]. </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer_slice_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each 2D slice in bytes to be used for the memory region associated with <code>buffer</code>. If <code>buffer_slice_pitch</code> is 0, <code>buffer_slice_pitch</code> is computed as <code>region</code>[1] * <code>buffer_row_pitch</code>.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>host_row_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each row in bytes to be used for the memory region pointed to by <code>ptr</code>. If <code>host_row_pitch</code> is 0, <code>host_row_pitch</code> is computed as <code>region</code>[0].</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>host_slice_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each 2D slice in bytes to be used for the memory region pointed to by <code>ptr</code>. If <code>host_slice_pitch</code> is 0, <code>host_slice_pitch</code> is computed as <code>region</code>[1] * <code>host_row_pitch</code>. </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>ptr </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The pointer to buffer in host memory where data is to be read into.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             <code>event_wait_list</code> and <code>num_events_in_wait_list</code> specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular read command and can be used
     *             to query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which
     *             case it will not be possible for the application to query the status of this command or queue a
     *             wait for this command to complete.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Calling <code>clEnqueueReadBufferRect</code> to read a region of the buffer object with the
     *       <code>ptr</code> argument value set to <code>host_ptr</code> and <code>host_origin</code>,
     *       <code>buffer_origin</code> values are the same, where <code>host_ptr</code> is a pointer to the
     *       memory region specified when the buffer object being read is created with CL_MEM_USE_HOST_PTR, must meet the
     *       same requirements given for <span><span>clEnqueueReadBuffer</span></span>.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueReadBufferRect</code> returns <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code>
     *           and <code>buffer</code> are not the same or if the context associated with <code>command_queue</code>
     *           and events in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>buffer</code> is not a valid buffer object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the region being read specified by (<code>buffer_origin</code>, <code>region</code>) is out of bounds.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if <code>buffer</code> is a sub-buffer object and <code>offset</code> specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the read and write operations are blocking and the execution status of any of the events in <code>event_wait_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueReadBufferRect(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long[] buffer_offset, long[] host_offset, long[] region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        if (!blocking_read && !ptr.isDirectBufferPointer())
        {
            throw new IllegalArgumentException(
                "Non-blocking read operations may only be " +
                "performed using pointers to direct buffers");
        }
        return checkResult(clEnqueueReadBufferRectNative(command_queue, buffer, blocking_read, buffer_offset, host_offset, region, buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueReadBufferRectNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long[] buffer_offset, long[] host_offset, long[] region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);


    /**
     * <p>
     *       Enqueue commands to write to a buffer object from host memory.
     *   </p>
     *
     * <div title="clEnqueueWriteBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueWriteBuffer</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_write</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>cb</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const void <var>*ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the command-queue in which the write command will be
     *             queued. <code>command_queue</code> and <code>buffer</code> must be created with the same OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           buffer
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to a valid buffer object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           blocking_write
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the write operations are <code>blocking</code> or <code>nonblocking</code>.
     *           </p>
     *           <p>
     *             If <code>blocking_write</code> is <code>CL_TRUE</code>, the OpenCL implementation copies the data referred to by <code>ptr</code>
     *             and enqueues the write operation in the command-queue. The memory pointed to by <code>ptr</code> can be
     *             reused by the application after the <code>clEnqueueWriteBuffer</code> call returns.
     *           </p>
     *           <p>
     *             If <code>blocking_write</code> is <code>CL_FALSE</code>, the OpenCL implementation will use <code>ptr</code> to perform a nonblocking
     *             write. As the write is non-blocking the implementation can return immediately. The
     *             memory pointed to by <code>ptr</code> cannot be reused by the application after the call returns. The <code>event</code>
     *             argument returns an event object which can be used to query the execution status of the write
     *             command. When the write command has completed, the memory pointed to by <code>ptr</code> can then be
     *             reused by the application.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           offset
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The offset in bytes in the buffer object to write to.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           cb
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The size in bytes of data being written.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           ptr
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The pointer to buffer in host memory where data is to be written from.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             <code>event_wait_list</code> and <code>num_events_in_wait_list</code> specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code>
     *             must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must
     *             be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in
     *             <code>event_wait_list</code> act as synchronization points. The context associated with events in
     *             <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular write command and can be used
     *             to query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which
     *             case it will not be possible for the application to query the status of this command or queue a
     *             wait for this command to complete.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Calling <code>clEnqueueWriteBuffer</code> to update the latest bits in a region of the buffer object with
     *       the <code>ptr</code> argument value set to <code>host_ptr</code> + <code>offset</code>, where <code>host_ptr</code>
     *       is a pointer to the memory region specified when the buffer object being written is created with <code>CL_MEM_USE_HOST_PTR</code>,
     *       must meet the following requirements in order to avoid undefined behavior:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>All commands that use this buffer object or a memory object (buffer or image) created from this buffer
     *           object have finished execution before the read command begins execution.
     *         </li>
     *         <li>The buffer object or memory objects created from this buffer object are not mapped.</li>
     *         <li>The buffer object or memory objects created from this buffer object are not used by any command-queue until
     *           the read command has finished execution.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueWriteBuffer</code> returns <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code> and <code>buffer</code> are
     *           not the same or if the context associated with <code>command_queue</code> and events in
     *           <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>buffer</code> is not a valid buffer object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the region being written specified by (<code>offset</code>,
     *           <code>cb</code>) is out of bounds or if <code>ptr</code> is a NULL value.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL
     *           and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if <code>buffer</code> is a sub-buffer object and <code>offset</code>
     *           specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated
     *           with <code>queue</code>.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the read and write operations are blocking and the execution status of any of the events in <code>event_wait_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueWriteBuffer(cl_command_queue command_queue, cl_mem buffer, boolean blocking_write, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        if (blocking_write)
        {
            return checkResult(clEnqueueWriteBufferNative(command_queue, buffer, blocking_write, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event));
        }
        else
        {
            if (event == null)
            {
                event = new cl_event();
            }
            int result = clEnqueueWriteBufferNative(command_queue, buffer, blocking_write, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event);
            scheduleReferenceRelease(event, ptr);
            return checkResult(result);
        }
    }

    private static native int clEnqueueWriteBufferNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_write, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);


    /**
     * <p>
     *       Enqueue commands to write a rectangular region to a buffer object from host memory.
     *   </p>
     *
     * <div title="clEnqueueWriteBufferRect">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueWriteBufferRect</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_write</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>buffer_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>host_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>buffer_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>buffer_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>host_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>host_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>command_queue</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the command-queue in which the write command will be
     *             queued. <code>command_queue</code> and <code>buffer</code> must be created with the same OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>Refers to a valid buffer object.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>blocking_write</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the write operations are <code>blocking</code> or non-blocking. If <code>blocking_write</code> is <code>CL_TRUE</code>, the OpenCL implementation copies the data referred to by <code>ptr</code>
     *             and enqueues the write operation in the command-queue. The memory pointed to by <code>ptr</code> can be
     *             reused by the application after the <code>clEnqueueWriteBufferRect</code> call returns.
     *           </p>
     *           <p>If <code>blocking_write</code> is <code>CL_FALSE</code>, the OpenCL implementation will use <code>ptr</code> to perform a nonblocking write. As the write is non-blocking the implementation can return immediately. The memory pointed to by <code>ptr</code> cannot be reused by the application after the call returns. The <code>event</code> argument returns an event object which can be used to query the execution status of the write command. When the write command has completed, the memory pointed to by <code>ptr</code> can then be
     *             reused by the application.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer_origin</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The (<span><em>x, y, z</em></span>) offset in the memory region associated with <code>buffer</code>. For a 2D rectangle region, the <code>z</code> value given by <code>buffer_origin</code>[2] should be 0. The offset in bytes is computed as <code>buffer_origin</code>[2] * <code>buffer_slice_pitch</code> + <code>buffer_origin</code>[1] * <code>buffer_row_pitch</code> + <code>buffer_origin</code>[0].</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>host_origin</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The (<span><em>x, y, z</em></span>) offset in the memory region pointed to by <code>ptr</code>. For a 2D rectangle region, the <code>z</code> value given by <code>host_origin</code>[2] should be 0. The offset in bytes is computed as <code>host_origin</code>[2] * <code>host_slice_pitch</code> + <code>host_origin</code>[1] * <code>host_row_pitch</code> + <code>host_origin</code>[0].</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>region</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The (<span><em>width, height, depth</em></span>) in bytes of the 2D or 3D rectangle being read or written. For a 2D rectangle copy, the <code>depth</code> value given by <code>region</code>[2] should be 1. </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer_row_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each row in bytes to be used for the memory region associated with <code>buffer</code>. If <code>buffer_row_pitch</code> is 0, <code>buffer_row_pitch</code> is computed as <code>region</code>[0]. </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>buffer_slice_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each 2D slice in bytes to be used for the memory region associated with <code>buffer</code>. If <code>buffer_slice_pitch</code> is 0, <code>buffer_slice_pitch</code> is computed as <code>region</code>[1] * <code>buffer_row_pitch</code>.</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>host_row_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each row in bytes to be used for the memory region pointed to by <code>ptr</code>. If <code>host_row_pitch</code> is 0, <code>host_row_pitch</code> is computed as <code>region</code>[0].</p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>host_slice_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>The length of each 2D slice in bytes to be used for the memory region pointed to by <code>ptr</code>. If <code>host_slice_pitch</code> is 0, <code>host_slice_pitch</code> is computed as <code>region</code>[1] * <code>host_row_pitch</code>. </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>ptr</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The pointer to buffer in host memory where data is to be written from.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             <code>event_wait_list</code> and <code>num_events_in_wait_list</code> specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular write command
     *             and can be used to query or queue a wait for this particular command to
     *             complete. <code>event</code> can be NULL in which
     *             case it will not be possible for the application to query the status of
     *             this command or queue a wait for this command to complete.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Calling <code>clEnqueueWriteBufferRect</code> to update the latest bits in a region of the buffer object with the <code>ptr</code> argument value set to <code>host_ptr</code> and <code>host_origin</code>, <code>buffer_origin</code> values are the same, where <code>host_ptr</code> is a pointer to the memory region specified when the buffer object being written is created with CL_MEM_USE_HOST_PTR, must meet the following requirements in order to avoid undefined behavior:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           The host memory region given by (<code>buffer_origin region</code>) contains the latest bits when the enqueued write command begins execution.
     *         </li>
     *         <li>
     *           The buffer object or memory objects created from this buffer object are not mapped.
     *         </li>
     *         <li>
     *           The buffer object or memory objects created from this buffer object are not
     *           used by any command-queue until the write command has finished execution.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueWriteBufferRect</code> returns <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code> and <code>buffer</code> are not the same or if the context associated with <code>command_queue</code> and events in
     *           <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>buffer</code> is not a valid buffer object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the region being read specified by (<code>buffer_origin</code>, <code>region</code>) is out of bounds or if <code>ptr</code> is a NULL value.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if <code>buffer</code> is a sub-buffer object and <code>offset</code> specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span>
     *           if the read and write operations are blocking and the execution status of
     *           any of the events in <code>event_wait_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueWriteBufferRect(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long buffer_offset[], long host_offset[], long[] region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event[] event_wait_list, cl_event event)
    {
        return checkResult(clEnqueueWriteBufferRectNative(command_queue, buffer, blocking_read, buffer_offset, host_offset, region, buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueWriteBufferRectNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_read, long buffer_offset[], long host_offset[], long[] region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event[] event_wait_list, cl_event event);


    /**
     * <p>
     *       Enqueues a command to copy from one buffer object to another.
     *   </p>
     *
     * <div title="clEnqueueCopyBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int <b>clEnqueueCopyBuffer </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>src_buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>dst_buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>src_offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>dst_offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>cb</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The command-queue in which the copy command will be queued. The
     *             OpenCL context associated with <code>command_queue</code>, <code>src_buffer</code>, and <code>dst_buffer</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           src_offset
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The offset where to begin copying data from <code>src_buffer</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           dst_offset
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The offset where to begin copying data into <code>dst_buffer</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>cb</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the size in bytes to copy.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list,
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in
     *             <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to
     *             query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete. <span><span>clEnqueueBarrier</span></span> can be used instead.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code>, <code>src_buffer</code>, and
     *           <code>dst_buffer</code> are not the same or if the context associated with <code>command_queue</code> and events
     *           in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>src_buffer</code> and <code>dst_buffer</code> are not valid buffer objects.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>src_offset</code>, <code>dst_offset</code>, <code>cb</code>, <code>src_offset</code> + <code>cb</code>, or <code>dst_offset</code> + <code>cb</code>
     *           require accessing elements outside the <code>src_buffer</code> and <code>dst_buffer</code> buffer objects
     *           respectively.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> is greater than 0, or <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MEM_COPY_OVERLAP</span> if <code>src_buffer</code> and <code>dst_buffer</code> are the same
     *           buffer object and the source and destination regions overlap.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET </span> if <code>src_buffer</code> is a sub-buffer object
     *           and <code>offset</code> specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN
     *           value for device associated with queue.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET </span> if <code>dst_buffer</code> is a sub-buffer object
     *           and <code>offset</code> specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN
     *           value for device associated with queue.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for data store
     *           associated with <code>src_buffer</code> or <code>dst_buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueCopyBuffer(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_buffer, long src_offset, long dst_offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueCopyBufferNative(command_queue, src_buffer, dst_buffer, src_offset, dst_offset, cb, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyBufferNative(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_buffer, long src_offset, long dst_offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);


    /**
     * <p>
     *       Enqueues a command to copy a rectangular region from the buffer object to another buffer object.
     *   </p>
     *
     * <div title="clEnqueueCopyBufferRect">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int <b>clEnqueueCopyBufferRect </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>src_buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>dst_buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>src_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>dst_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>src_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>src_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>dst_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>dst_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The command-queue in which the copy command will be queued. The
     *             OpenCL context associated with <code>command_queue</code>, <code>src_buffer</code>, and <code>dst_buffer</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           src_origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The (<span><em>x, y, z</em></span>) offset in the memory region associated with <code>src_buffer</code>. For a 2D rectangle region, the <code>z</code> value given by <code>src_origin</code>[2] should be 0. The offset in bytes is computed as <code>src_origin</code>[2] * <code>src_slice_pitch</code> + <code>src_origin</code>[1] * <code>src_row_pitch</code> + <code>src_origin</code>[0].
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           dst_origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The (<span><em>x, y, z</em></span>) offset in the memory region associated with <code>dst_buffer</code>. For a 2D rectangle region, the <code>z</code> value given by <code>dst_origin</code>[2] should be 0. The offset in bytes is computed as <code>dst_origin</code>[2] * <code>dst_slice_pitch</code> + <code>dst_origin</code>[1] * <code>dst_row_pitch</code> + <code>dst_origin</code>[0].
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>region</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The (<span><em>width, height, depth</em></span>) in bytes of the 2D or 3D rectangle being copied. For a
     *             2D rectangle, the <code>depth</code> value given by <code>region</code>[2] should be 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>src_row_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The length of each row in bytes to be used for the memory region associated with <code>src_buffer</code>. If <code>src_row_pitch</code> is 0, <code>src_row_pitch</code> is computed as <code>region</code>[0].
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>src_slice_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The length of each 2D slice in bytes to be used for the memory region associated with <code>src_buffer</code>. If <code>src_slice_pitch</code> is 0, <code>src_slice_pitch</code> is computed as <code>region</code>[1] * <code>src_row_pitch</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>dst_row_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The length of each row in bytes to be used for the memory region associated with <code>dst_buffer</code>. If <code>dst_row_pitch</code> is 0, <code>dst_row_pitch</code> is computed as <code>region</code>[0].
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>dst_slice_pitch</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The length of each 2D slice in bytes to be used for the memory region associated with <code>dst_buffer</code>. If <code>dst_slice_pitch</code> is 0, <code>dst_slice_pitch</code> is computed as <code>region</code>[1] * <code>dst_row_pitch</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list,
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in
     *             <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to
     *             query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete. <span><span>clEnqueueBarrier</span></span> can be used instead.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>Returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code>, <code>src_buffer</code>, and
     *           <code>dst_buffer</code> are not the same or if the context associated with <code>command_queue</code> and events
     *           in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>src_buffer</code> and <code>dst_buffer</code> are not valid buffer objects.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if (<code>src_offset</code>, <code>region</code>) or (<code>dst_offset</code>, <code>region</code>) require accessing elements outside the <code>src_buffer</code> and <code>dst_buffer</code> objects respectively.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> is greater than 0, or <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MEM_COPY_OVERLAP</span> if <code>src_buffer</code> and <code>dst_buffer</code> are the same buffer object and the source and destination regions overlap.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET </span> if <code>src_buffer</code> is a sub-buffer object and <code>offset</code> specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET </span> if <code>dst_buffer</code> is a sub-buffer object and <code>offset</code> specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for data store associated with <code>src_buffer</code> or <code>dst_buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueCopyBufferRect(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_buffer, long[] src_origin, long[] dst_origin, long[] region, long src_row_pitch, long src_slice_pitch, long dst_row_pitch, long dst_slice_pitch, int num_events_in_wait_list, cl_event[] event_wait_list, cl_event event)
    {
        return checkResult(clEnqueueCopyBufferRectNative(command_queue, src_buffer, dst_buffer, src_origin, dst_origin, region, src_row_pitch, src_slice_pitch, dst_row_pitch, dst_slice_pitch, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyBufferRectNative(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_buffer, long[] src_origin, long[] dst_origin, long[] region, long src_row_pitch, long src_slice_pitch, long dst_row_pitch, long dst_slice_pitch, int num_events_in_wait_list, cl_event[] event_wait_list, cl_event event);


    /**
     * <p>
     *       Enqueues a command to read from a 2D or 3D image object to host memory.
     *   </p>
     *
     * <div title="clEnqueueReadImage">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueReadImage</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>image</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_read</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the command-queue in which the read command will be
     *             queued. <code>command_queue</code> and <code>image</code>
     *             must be created with the same OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to a valid 2D or 3D image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           blocking_read
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the read operations are <code>blocking</code> or <code>non-blocking</code>.
     *           </p>
     *           <p>
     *             If <code>blocking_read</code> is <code>CL_TRUE</code> i.e. the read command is blocking, <code>clEnqueueReadImage</code> does not return until the
     *             buffer data has been read and copied into memory pointed to by <code>ptr</code>.
     *           </p>
     *           <p>
     *             If <code>blocking_read</code> is <code>CL_FALSE</code> i.e. map operation is non-blocking, <code>clEnqueueReadImage</code> queues a non-blocking read command and returns. The contents of the buffer that <code>ptr</code> points to cannot be used until the read command has completed. The <code>event</code> argument returns an event object which can be used to query the execution status of the read command. When the read command has completed, the contents of the buffer that <code>ptr</code> points to can be used by the application.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<span><em>x, y, z</em></span>) offset in pixels in the image from where to read. If <code>image</code> is a 2D image object, the z value given by <code>origin</code>[2] must be 0.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           region
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<code>width</code>, <code>height</code>, <code>depth</code>) in pixels of the 2D or 3D rectangle being read. If <code>image</code> is a 2D image object, the <code>depth</code> value given by <code>region</code>[2] must be 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           row_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The length of each row in bytes. This value must be greater than or equal to the element size in bytes * <code>width</code>. If <code>row_pitch</code> is set to 0, the appropriate row pitch is calculated
     *             based on the size of each element in bytes multiplied by <code>width</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           slice_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Size in bytes of the 2D slice of the 3D region of a 3D image being read. This must be 0 if <code>image</code> is a 2D image. This value must be greater than or equal to <code>row_pitch</code> * <code>height</code>. If <code>slice_pitch</code> is set to 0, the appropriate slice pitch is calculated based on the <code>row_pitch</code> * <code>height</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           ptr
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The pointer to a buffer in host memory where image data is to be read from.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           ,</code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must
     *             be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular read command and can be used to
     *             query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Calling <code>clEnqueueReadImage</code> to read a region of the <code>image</code> with the <code>ptr</code> argument value set to <code>host_ptr</code> + (<code>origin</code>[2] * <code>image slice pitch</code> + <code>origin</code>[1] * <code>image row pitch</code> + <code>origin</code>[0] *
     *       <code>bytes per pixel</code>), where <code>host_ptr</code> is a pointer to the memory region specified when the <code>image</code> being read is created with <code>CL_MEM_USE_HOST_PTR</code>, must meet the following requirements in order to avoid undefined behavior:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           All commands that use this image object have finished execution before the read
     *           command begins execution.
     *         </li>
     *         <li>
     *           The <code>row_pitch</code> and <code>slice_pitch</code> argument values in <code>clEnqueueReadImage</code> must be set to
     *           the image row pitch and slice pitch.
     *         </li>
     *         <li>
     *           The image object is not mapped.
     *         </li>
     *         <li>
     *           The image object is not used by any command-queue until the read command has
     *           finished execution.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueReadImage</code> returns <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors.
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code>
     *           is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with
     *           <code>command_queue</code> and
     *           <code>image</code> are not the same or if the context associated with
     *           <code>command_queue</code>
     *           and events in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>image</code> is
     *           not a valid image object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the region being read specified by
     *           <code>origin</code> and <code>region</code> is out of bounds or if
     *           <code>ptr</code> is a NULL value.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>image</code> is a
     *           2D image object and <code>origin</code>[2]
     *           is not equal to 0 or <code>region</code>[2] is not equal to 1 or
     *           <code>slice_pitch</code> is not equal to 0.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code>
     *           is not NULL and <code>num_events_in_wait_list</code> is 0, or if event objects in
     *           <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if image dimensions (image width, height,
     *           specified or compute row and/or slice pitch) for <code>image</code> are not
     *           supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the read
     *           and write operations are blocking and the execution status of any of the events in
     *           <code>event_wait_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the device associated with
     *           <code>command_queue</code> does not support images (i.e. CL_DEVICE_IMAGE_SUPPORT
     *           specified in the table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span> is CL_FALSE).
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>image</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueReadImage(cl_command_queue command_queue, cl_mem image, boolean blocking_read, long origin[], long region[], long row_pitch, long slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {

        if (!blocking_read && !ptr.isDirectBufferPointer())
        {
            throw new IllegalArgumentException(
                "Non-blocking read operations may only be " +
                "performed using pointers to direct buffers");
        }
        // NON_BLOCKING_READ (see clEnqueueReadBuffer)

        return checkResult(clEnqueueReadImageNative(command_queue, image, blocking_read, origin, region, row_pitch, slice_pitch, ptr, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueReadImageNative(cl_command_queue command_queue, cl_mem image, boolean blocking_read, long origin[], long region[], long row_pitch, long slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to write to a 2D or 3D image object from host memory.
     *   </p>
     *
     * <div title="clEnqueueWriteImage">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueWriteImage</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>image</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_write</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>input_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>input_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const void <var>* ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the command-queue in which the write command will be
     *             queued. <code>command_queue</code> and <code>image</code>
     *             must be created with the same OpenCL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to a valid 2D or 3D image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           blocking_write
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the write operation is <code>blocking</code> or <code>non-blocking</code>.
     *           </p>
     *           <p>
     *             If <code>blocking_write</code> is <code>CL_TRUE</code> the OpenCL implementation copies the data referred to by <code>ptr</code> and enqueues the write command in the command-queue. The memory pointed to by <code>ptr</code> can be reused by the application after the <code>clEnqueueWriteImage</code> call returns.
     *           </p>
     *           <p>
     *             If <code>blocking_write</code> is <code>CL_FALSE</code> the OpenCL implementation will use <code>ptr</code> to perform a nonblocking write. As the write is non-blocking the implementation can return immediately. The memory pointed to by <code>ptr</code> cannot be reused by the application after the call returns. The <code>event</code> argument returns an event object which can be used to query the execution status of the write
     *             command. When the write command has completed, the memory pointed to by <code>ptr</code> can then be reused by the application.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<span><em>x, y, z</em></span>) offset in pixels in the image from where to write. If <code>image</code> is a 2D image object, the z value given by <code>origin</code>[2] must be 0.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           region
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<code>width</code>, <code>height</code>, <code>depth</code>) in pixels of the 2D or 3D rectangle being written. If <code>image</code> is a 2D image object, the <code>depth</code> value given by <code>region</code>[2] must be 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           input_row_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The length of each row in bytes. This value must be greater than or equal to the element size in bytes * <code>width</code>. If <code>input_row_pitch</code> is set to 0, the appropriate row pitch is calculated based on the size of each element in bytes multiplied by <code>width</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           input_slice_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Size in bytes of the 2D slice of the 3D region of a 3D image being written. This must be 0 if <code>image</code> is a 2D image. This value must be greater than or equal to <code>row_pitch</code> * <code>height</code>. If <code>input_slice_pitch</code> is set to 0, the appropriate slice pitch is calculated based on the <code>row_pitch</code> * <code>height</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           ptr
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The pointer to a buffer in host memory where image data is to be written to.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           ,</code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must
     *             be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular write command and can be used to
     *             query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which
     *             case it will not be possible for the application to query the status of this command or queue a
     *             wait for this command to complete.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Calling <code>clEnqueueWriteImage</code> to update the latest bits in a region of the <code>image</code> with the
     *       <code>ptr</code> argument value set to <code>host_ptr</code> + (<code>origin</code>[2] * <code>image slice pitch</code> + <code>origin</code>[1] * <code>image row pitch</code>
     *       + <code>origin</code>[0] * <code>bytes per pixel</code>), where <code>host_ptr</code> is a pointer to the memory region specified when
     *       the <code>image</code> being written is created with <code>CL_MEM_USE_HOST_PTR</code>, must meet the following requirements in order to avoid undefined behavior:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           The host memory region being written contains the latest bits when the enqueued write
     *           command begins execution.
     *         </li>
     *         <li>
     *           The <code>input_row_pitch</code> and <code>input_slice_pitch</code> argument values in <code>clEnqueueWriteImage</code>
     *           must be set to the image row pitch and slice pitch.
     *         </li>
     *         <li>
     *           The image object is not mapped.
     *         </li>
     *         <li>
     *           The image object is not used by any command-queue until the write command has
     *           finished execution.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueWriteImage</code> return <span>CL_SUCCESS</span> if the function is
     *       executed successfully. Otherwise, it returns one of the following errors.
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code> and <code>image</code>
     *           are not the same or if the context associated with <code>command_queue</code> and events
     *           in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>image</code> is not a valid image object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the region being written specified by <code>origin</code> and <code>region</code>
     *           is out of bounds or if <code>ptr</code> is a NULL value.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>image</code> is a 2D image object and <code>origin</code>[2] is not equal to 0 or
     *           <code>region</code>[2] is not equal to 1 or <code>slice_pitch</code> is not equal to 0.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if image dimensions (image width, height, specified or
     *           compute row and/or slice pitch) for <code>image</code> are not supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the device associated with <code>command_queue</code> does not
     *           support images (i.e. CL_DEVICE_IMAGE_SUPPORT specified in the table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span> is CL_FALSE).
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the
     *           read and write operations are blocking and the execution status of any of
     *           the events in <code>event_wait_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>image</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueWriteImage(cl_command_queue command_queue, cl_mem image, boolean blocking_write, long origin[], long region[], long input_row_pitch, long input_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        if (blocking_write)
        {
            return checkResult(clEnqueueWriteImageNative(command_queue, image, blocking_write, origin, region, input_row_pitch, input_slice_pitch, ptr, num_events_in_wait_list, event_wait_list, event));
        }
        else
        {
            if (event == null)
            {
                event = new cl_event();
            }
            int result = clEnqueueWriteImageNative(command_queue, image, blocking_write, origin, region, input_row_pitch, input_slice_pitch, ptr, num_events_in_wait_list, event_wait_list, event);
            scheduleReferenceRelease(event, ptr);
            return checkResult(result);
        }
    }

    private static native int clEnqueueWriteImageNative(cl_command_queue command_queue, cl_mem image, boolean blocking_write, long origin[], long region[], long input_row_pitch, long input_slice_pitch, Pointer ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to copy image objects.
     *   </p>
     *
     * <div title="clEnqueueCopyImage">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueCopyImage</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>src_image</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>dst_image</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>src_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>dst_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Refers to the command-queue in which the copy command will be queued. The
     *             OpenCL context associated with <code>command_queue</code>, <code>src_image</code> and <code>dst_image</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           src_origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the starting (<span><em>x, y, z</em></span>) location in pixels in <code>src_image</code> from where to start the data copy. If <code>src_image</code> is a 2D image object, the z value given by <code>src_origin</code>[2] must be 0.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           dst_origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the starting (<span><em>x, y, z</em></span>) location in pixels in <code>dst_image</code> from where to start the data copy. If <code>dst_image</code> is a 2D image object, the z value given by <code>dst_origin</code>[2] must be 0.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           region
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<code>width</code>, <code>height</code>, <code>depth</code>) in pixels of the 2D or 3D rectangle to copy. If <code>src_image</code> or <code>dst_image</code> is a 2D image object, the <code>depth</code> value given by <code>region</code>[2] must be 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           ,</code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete. <span><span>clEnqueueBarrier</span></span> can be used instead.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       It is currently a requirement that the <code>src_image</code> and <code>dst_image</code> image memory objects for <code>clEnqueueCopyImage</code> must have the exact same image format (i.e. the <span><span>cl_image_format</span></span>  descriptor specified when <code>src_image</code> and <code>dst_image</code> are created must match).
     *     </p>
     *     <p>
     *       <code>src_image</code> and <code>dst_image</code> can be 2D or 3D image objects
     *       allowing us to perform the following actions:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           Copy a 2D image object to a 2D image object.
     *         </li>
     *         <li>
     *           Copy a 2D image object to a 2D slice of a 3D image object.
     *         </li>
     *         <li>
     *           Copy a 2D slice of a 3D image object to a 2D image object.
     *         </li>
     *         <li>
     *           Copy a 3D image object to a 3D image object.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueCopyImage</code> returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code>, <code>src_image</code>
     *           and <code>dst_image</code> are not the same or if the context associated with <code>command_queue</code> and events
     *           in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>src_image</code> and <code>dst_image</code> are not valid image objects.
     *         </li>
     *         <li><span>CL_IMAGE_FORMAT_MISMATCH</span> if <code>src_image</code> and <code>dst_image</code> do not use the same
     *           image format.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the 2D or 3D rectangular region specified by <code>src_origin</code>
     *           and <code>src_origin</code> + <code>region</code> refers to a region outside <code>src_image</code>,
     *           or if the 2D or 3D rectangular region specified by <code>dst_origin</code> and <code>dst_origin</code> + <code>region</code>
     *           refers to a region outside <code>dst_image</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>src_image</code> is a 2D image object and <code>src_origin</code>[2]
     *           is not equal to 0 or <code>region</code>[2] is not equal to 1.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>dst_image</code> is a 2D image object and <code>dst_origin</code>[2]
     *           is not equal to 0 or <code>region</code>[2] is not equal to 1.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL
     *           and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if image dimensions (image width, height, specified or
     *           compute row and/or slice pitch) for <code>src_image</code> are not supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if image dimensions (image width, height, specified or
     *           compute row and/or slice pitch) for <code>dst_image</code> are not supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>src_image</code> or <code>dst_image</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the device associated with <code>command_queue</code> does not support images
     *           (i.e. CL_DEVICE_IMAGE_SUPPORT specified in the table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span> is CL_FALSE).
     *         </li>
     *         <li><span>CL_MEM_COPY_OVERLAP</span> if <code>src_image</code> and <code>dst_image</code> are the
     *           same image object and the source and destination regions overlap.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueCopyImage(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_image, long src_origin[], long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueCopyImageNative(command_queue, src_image, dst_image, src_origin, dst_origin, region, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyImageNative(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_image, long src_origin[], long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to copy an image object to a buffer object.
     *   </p>
     *
     * <div title="clEnqueueCopyImageToBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueCopyImageToBuffer</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>src_image</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem  <var>dst_buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>src_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>dst_offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid command-queue. The OpenCL context associated with  <code>command_queue</code>, <code>src_image</code>, and <code>dst_buffer</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           src_image
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           dst_buffer
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid buffer object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           src_origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<span><em>x, y, z</em></span>) offset in pixels in the image from where to copy. If <code>src_image</code> is a 2D image object, the z value given by <code>src_origin</code>[2] must be 0.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           region
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<code>width</code>, <code>height</code>, <code>depth</code>) in pixels of the 2D or 3D rectangle to copy. If <code>src_image</code> is a 2D image object, the <code>depth</code> value given by <code>region</code>[2] must be 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           dst_offset
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The offset where to begin copying data into <code>dst_buffer</code>. The size in bytes of the region to be copied referred to as <code>dst_cb</code> is computed as <code>width</code> * <code>height</code> * <code>depth</code> * <code>bytes</code>/<code>image element</code> if <code>src_image</code> is a 3D image object and is computed as <code>width</code> * <code>height</code> * <code>bytes</code>/<code>image element</code> if <code>src_image</code> is a 2D image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           ,</code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this
     *             particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to
     *             query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete. <span><span>clEnqueueBarrier</span></span> can be used instead.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueCopyImageToBuffer</code> returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code>, <code>src_image</code> and <code>dst_buffer</code> are not the same or if the context associated with <code>command_queue</code> and events in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>src_image</code> is not a valid image object and <code>dst_buffer</code> is not a valid buffer object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the 2D or 3D rectangular region specified by <code>src_origin</code> and <code>src_origin</code> + <code>region</code> refers to a region outside <code>src_image</code>, or if the region specified by <code>dst_offset</code> and <code>dst_offset</code> + <code>dst_cb</code> refers to a region outside <code>dst_buffer</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>src_image</code> is a 2D image object and <code>src_origin</code>[2] is not equal to 0 or <code>region</code>[2] is not equal to 1.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if <code>dst_buffer</code> is a sub-buffer object
     *           and <code>offset</code> specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN
     *           value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if image dimensions (image width, height, specified or
     *           compute row and/or slice pitch) for <code>src_image</code> are not supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the device associated with <code>command_queue</code> does not support images
     *           (i.e. CL_DEVICE_IMAGE_SUPPORT specified in the table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span> is CL_FALSE).
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>src_image</code> or <code>dst_buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueCopyImageToBuffer(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_buffer, long src_origin[], long region[], long dst_offset, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueCopyImageToBufferNative(command_queue, src_image, dst_buffer, src_origin, region, dst_offset, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyImageToBufferNative(cl_command_queue command_queue, cl_mem src_image, cl_mem dst_buffer, long src_origin[], long region[], long dst_offset, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to copy a buffer object to an image object.
     *   </p>
     *
     * <div title="clEnqueueCopyBufferToImage">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueCopyBufferToImage</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>src_buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem  <var>dst_image</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>src_offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>dst_origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid command-queue. The OpenCL context associated with
     *             <code>command_queue</code>, <code>src_buffer</code>, and <code>dst_image</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           src_buffer
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid buffer object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           dst_image
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           src_offset
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The offset where to begin copying data from <code>src_buffer</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           dst_origin
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The (<span><em>x, y, z</em></span>) offset in pixels where to begin copying data to <code>dst_image</code>. If <code>dst_image</code> is a 2D image object, the <span><em>z</em></span> value given by <code>dst_origin</code>[2] must be 0.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           region
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Defines the (<code>width</code>, <code>height</code>, <code>depth</code>) in pixels of the 2D or 3D rectangle to copy. If <code>dst_image</code> is a 2D image object, the <code>depth</code> value given by <code>region</code>[2] must be 1.
     *           </p>
     *           <p>
     *             The size in bytes of the region to be copied from <code>src_buffer</code> referred to as <code>src_cb</code> is computed as <code>width</code> * <code>height</code> * <code>depth</code> * <code>bytes</code>/<code>image element</code> if <code>dst_image</code> is a 3D image object and is computed as <code>width</code> * <code>height</code> * <code>bytes</code>/<code>image element</code> if <code>dst_image</code> is a 2D image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           ,</code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to
     *             query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete. <span><span>clEnqueueBarrier</span></span> can be used instead.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueCopyBufferToImage</code> returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code>, <code>src_buffer</code> and
     *           <code>dst_image</code> are not the same or if the context associated with <code>command_queue</code> and events
     *           in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>src_buffer</code> is not a valid buffer object and <code>dst_image</code> is not a valid image object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if the 2D or 3D rectangular region specified by <code>dst_origin</code> and
     *           <code>dst_origin</code> + <code>region</code> refers to a region outside <code>dst_origin</code>, or if the region specified
     *           by <code>src_offset</code> and <code>src_offset</code> + <code>src_cb</code> refers to a region outside
     *           <code>src_buffer</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>dst_image</code> is a 2D image object and <code>dst_origin</code>[2] is not equal to 0
     *           or <code>region</code>[2] is not equal to 1.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if <code>src_buffer</code> is a sub-buffer object and <code>offset</code>
     *           specified when the sub-buffer object is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with
     *           <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if image dimensions (image width, height, specified or
     *           compute row and/or slice pitch) for <code>dst_image</code> are not supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the device associated with <code>command_queue</code> does not support images
     *           (i.e. CL_DEVICE_IMAGE_SUPPORT specified in the table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span> is CL_FALSE).
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>src_buffer</code> or <code>dst_image</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueCopyBufferToImage(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_image, long src_offset, long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueCopyBufferToImageNative(command_queue, src_buffer, dst_image, src_offset, dst_origin, region, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueCopyBufferToImageNative(cl_command_queue command_queue, cl_mem src_buffer, cl_mem dst_image, long src_offset, long dst_origin[], long region[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to map a region of the buffer object given by <code>buffer</code> into the host address space and returns a pointer to this mapped region.
     *   </p>
     *
     * <div title="clEnqueueMapBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             void * <b>clEnqueueMapBuffer</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>buffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_map</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_map_flags <var>map_flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>cb</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid command-queue.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           blocking_map
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the map operation is <code>blocking</code> or <code>non-blocking</code>.
     *           </p>
     *           <p>
     *             If <code>blocking_map</code> is <code>CL_TRUE</code>, <code>clEnqueueMapBuffer</code> does not return until the specified region in <code>buffer</code> can be mapped.
     *           </p>
     *           <p>
     *             If <code>blocking_map</code> is <code>CL_FALSE</code> i.e. map operation is non-blocking, the pointer to the mapped region returned by <code>clEnqueueMapBuffer</code> cannot be used until the map command has completed. The <code>event</code> argument returns an event object which can be used to query the execution status of the map command. When the map command is completed, the application can access the contents of the mapped region using the pointer returned by <code>clEnqueueMapBuffer</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           map_flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Is a bit-field and can be set to <code>CL_MAP_READ</code> to indicate that the region specified by (<code>offset</code>, <code>cb</code>) in the buffer object is being mapped for reading, and/or <code>CL_MAP_WRITE</code> to indicate that the region specified by (<code>offset</code>, <code>cb</code>) in the buffer object is being mapped for writing.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           buffer
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid buffer object. The OpenCL context associated with <code>command_queue</code> and <code>buffer</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           offset,
     *           </code>
     *           <code>
     *           cb
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The offset in bytes and the size of the region in the buffer object that is being mapped.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list,
     *           </code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this
     *             particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code>
     *             must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must
     *             be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in
     *             <code>event_wait_list</code> act as synchronization points. The context associated with events in
     *             <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       If the buffer object is created with <code>CL_MEM_USE_HOST_PTR</code> set in <code>mem_flags</code>, the <code>host_ptr</code> specified in <span><span>clCreateBuffer</span></span> is guaranteed to contain the latest bits in the region being mapped when the <code>clEnqueueMapBuffer</code> command has completed; and the pointer value returned by <code>clEnqueueMapBuffer</code> will be derived from the <code>host_ptr</code> specified when the buffer object is created.
     *     </p>
     *     <p>Mapped buffer objects are unmapped using <span><span>clEnqueueUnmapMemObject</span></span>.
     *     </p>
     *     <p>
     *       The contents of the regions of a memory object mapped for writing (i.e. <code>CL_MAP_WRITE</code> is set in <code>map_flags</code> argument to <code>clEnqueueMapBuffer</code> or <span><span>clEnqueueMapImage</span></span>) are considered to be undefined until this region is unmapped. Reads and writes by a kernel executing on a device to a memory region(s) mapped for writing are undefined.
     *     </p>
     *     <p>
     *       Multiple command-queues can map a region or overlapping regions of a memory object for
     *       reading (i.e. <code>map_flags</code> = <code>CL_MAP_READ</code>). The contents of the regions of a memory object mapped for reading can also be read by kernels executing on a device(s). The behavior of writes by a kernel executing on a device to a mapped region of a memory object is undefined. Mapping (and unmapping) overlapped regions of a buffer or image memory object for writing is undefined.
     *     </p>
     *     <p>
     *       The behavior of OpenCL function calls that enqueue commands that write or copy to regions of a
     *       memory object that are mapped is undefined.
     *     </p>
     *     <p>
     *       The pointer returned maps a region starting at <code>offset</code> and is at least <code>cb</code> bytes in size. The result of a memory access outside this region is undefined.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueMapBuffer</code> will return a pointer to the mapped region if the function is executed successfully.  The <code>errcode_ret</code> is set to <span>CL_SUCCESS</span>.
     *     </p>
     *     <p>
     *       A NULL pointer is returned otherwise with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with
     *           <code>command_queue</code> and <code>buffer</code> are not the
     *           same or if the context associated with
     *           <code>command_queue</code> and events
     *           in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>buffer</code> is not a valid buffer object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if region being mapped given by (<code>offset</code>, <code>cb</code>) is out of bounds or if
     *           values specified in <code>map_flags</code> are not valid
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the map operation is blocking and the execution status of any of the events in <code>event_wait_list</code> is a negative integer value.
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if <code>buffer</code> is a sub-buffer object and
     *           <code>offset</code> specified when the sub-buffer object is created is not aligned to <code>CL_DEVICE_MEM_BASE_ADDR_ALIGN</code>
     *           value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_MAP_FAILURE</span> if there is a failure to map the requested region into the host address
     *           space. This error cannot occur for buffer objects created with <code>CL_MEM_USE_HOST_PTR</code>
     *           or <code>CL_MEM_ALLOC_HOST_PTR</code>.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>buffer</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static ByteBuffer clEnqueueMapBuffer(cl_command_queue command_queue, cl_mem buffer, boolean blocking_map, long map_flags, long offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            ByteBuffer result = clEnqueueMapBufferNative(command_queue, buffer, blocking_map, map_flags, offset, cb, num_events_in_wait_list, event_wait_list, event, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            ByteBuffer result = clEnqueueMapBufferNative(command_queue, buffer, blocking_map, map_flags, offset, cb, num_events_in_wait_list, event_wait_list, event, errcode_ret);
            return result;
        }
    }

    private static native ByteBuffer clEnqueueMapBufferNative(cl_command_queue command_queue, cl_mem buffer, boolean blocking_map, long map_flags, long offset, long cb, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[]);

    /**
     * <p>
     *       Enqueues a command to map a region of an image object into the host address
     *       space and returns a pointer to this mapped region.
     *   </p>
     *
     * <div title="clEnqueueMapImage">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             void * <b>clEnqueueMapImage </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>image</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_bool <var>blocking_map</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_map_flags <var>map_flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>origin</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>region</var>[3], </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*image_row_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*image_slice_pitch</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid command-queue.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid image object. The OpenCL context associated with <code>command_queue</code> and <code>image</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           blocking_map
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Indicates if the map operation is <code>blocking</code> or <code>non-blocking</code>. If <code>blocking_map</code> is <code>CL_TRUE</code>, <code>clEnqueueMapImage </code> does not return until the specified region in <code>image</code> can be mapped.
     *           </p>
     *           <p>
     *             If <code>blocking_map</code> is <code>CL_FALSE</code> i.e. map operation is non-blocking, the pointer to the mapped region returned by <code>clEnqueueMapImage</code> cannot be used until the map command has completed. The <code>event</code> argument returns an event object which can be used to query the execution status of the map command. When the map command is completed, the application can access the contents of the mapped region using the pointer returned by <code>clEnqueueMapImage</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           map_flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Is a bit-field and can be set to <code>CL_MAP_READ</code> to indicate that the region specified by (<code>origin</code>, <code>region</code>) in the image object is being mapped for reading, and/or <code>CL_MAP_WRITE</code> to indicate that the region specified by (<code>origin</code>, <code>region</code>) in the image object is being mapped for writing.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           origin,
     *           </code>
     *           <code>
     *           region
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Define the (<span><em>x, y, z</em></span>) offset in pixels and (<code>width</code>, <code>height</code>, <code>depth</code>) in pixels of the 2D or 3D rectangle region that is to be mapped. If <code>image</code> is a 2D image object, the <span><em>z</em></span> value given by <code>origin</code>[2] must be 0 and the <code>depth</code> value given by <code>region</code>[2] must be 1.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_row_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the scan-line pitch in bytes for the mapped region. This must be a non-NULL value.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           image_slice_pitch
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the size in bytes of each 2D slice for the mapped region. For a 2D image, zero is returned if this argument is not NULL. For a 3D image, <code>image_slice_pitch</code> must be a non-NULL value.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           ,</code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before <code>clEnqueueMapImage</code> can be executed. If <code>event_wait_list</code> is NULL, then <code>clEnqueueMapImage</code> does not wait on any event to complete. If <code>event_wait_list</code> is NULL,  <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which case it will not be possible for the application to query the status of this command or queue a wait for this command to complete.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       If the image object is created with <code>CL_MEM_USE_HOST_PTR</code> set in <code>mem_flags</code>, the
     *       following will be true:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li>
     *           The <code>host_ptr</code> specified in <span><span>clCreateImage2d</span></span>
     *           or <span><span>clCreateImage3d</span></span> is guaranteed to contain the latest bits
     *           in the region being mapped when the <code>clEnqueueMapImage</code> command has completed.
     *         </li>
     *         <li>
     *           The pointer value returned by <code>clEnqueueMapImage</code> will be derived from
     *           the <code>host_ptr</code> specified when the image object is created.
     *         </li>
     *       </ul>
     *     </div>
     *     <p>
     *       Mapped image objects are unmapped using <span><span>clEnqueueUnmapMemObject</span></span>.
     *     </p>
     *     <p>
     *       The contents of the regions of a memory object mapped for writing (i.e. <code>CL_MAP_WRITE</code> is set
     *       in <code>map_flags</code> argument to <span><span>clEnqueueMapBuffer</span></span> or <code>clEnqueueMapImage</code>) are considered to be
     *       undefined until this region is unmapped. Reads and writes by a kernel executing on a device to a
     *       memory region(s) mapped for writing are undefined.
     *     </p>
     *     <p>
     *       Multiple command-queues can map a region or overlapping regions of a memory object for
     *       reading (i.e. <code>map_flags</code> = <code>CL_MAP_READ</code>). The contents of the regions of a memory object
     *       mapped for reading can also be read by kernels executing on a device(s). The behavior of writes
     *       by a kernel executing on a device to a mapped region of a memory object is undefined.
     *       Mapping (and unmapping) overlapped regions of a memory object for writing is
     *       undefined.
     *     </p>
     *     <p>
     *       The behavior of OpenCL function calls that enqueue commands that write or copy to regions of a
     *       memory object that are mapped is undefined.
     *     </p>
     *     <p>
     *       The pointer returned maps a 2D or 3D region starting at <code>origin</code> and is at least (<code>image_row_pitch</code> * <code>region</code>[1]) pixels in size for a 2D image, and is at least (<code>image_slice_pitch</code> * <code>region</code>[2]) pixels in size for a 3D image. The result of a memory access outside this region is undefined.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueMapImage</code> will return a pointer to the mapped region if the function is executed successfully.
     *       The <code>errcode_ret</code> is set to <span>CL_SUCCESS</span>.
     *     </p>
     *     <p>
     *       A NULL pointer is returned otherwise with one of the following error values returned in
     *       <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code> and <code>image</code>
     *           are not the same or if the context associated with <code>command_queue</code> and events
     *           in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>image</code> is not a valid image object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if region being mapped given by (<code>origin</code>, <code>origin</code>+<code>region</code>) is out of bounds or if
     *           values specified in <code>map_flags</code> are not valid.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>image</code> is a 2D image object and <code>origin</code>[2] is not equal to 0 or
     *           <code>region</code>[2] is not equal to 1.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>image_row_pitch</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>image</code> is a 3D image object and <code>image_slice_pitch</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if image dimensions (image width, height, specified or
     *           compute row and/or slice pitch) for <code>image</code> are not supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_MAP_FAILURE</span> if there is a failure to map the requested region into the host address
     *           space. This error cannot occur for image objects created with <code>CL_MEM_USE_HOST_PTR</code>
     *           or <code>CL_MEM_ALLOC_HOST_PTR</code>.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with <code>buffer</code>.
     *         </li>
     *         <li><span>CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST</span> if the map operation
     *           is blocking and the execution status of any of the events in <code>event_wait_list</code>
     *           is a negative integer value.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the device associated with <code>command_queue</code> does not support
     *           images (i.e. CL_DEVICE_IMAGE_SUPPORT specified in the table of OpenCL Device Queries
     *           for <span><span>clGetDeviceInfo</span></span>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static ByteBuffer clEnqueueMapImage(cl_command_queue command_queue, cl_mem image, boolean blocking_map, long map_flags, long origin[], long region[], long image_row_pitch[], long image_slice_pitch[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            ByteBuffer result = clEnqueueMapImageNative(command_queue, image, blocking_map, map_flags, origin, region, image_row_pitch, image_slice_pitch, num_events_in_wait_list, event_wait_list, event, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            ByteBuffer result = clEnqueueMapImageNative(command_queue, image, blocking_map, map_flags, origin, region, image_row_pitch, image_slice_pitch, num_events_in_wait_list, event_wait_list, event, errcode_ret);
            return result;
        }
    }

    private static native ByteBuffer clEnqueueMapImageNative(cl_command_queue command_queue, cl_mem image, boolean blocking_map, long map_flags, long origin[], long region[], long image_row_pitch[], long image_slice_pitch[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event, int errcode_ret[]);

    /**
     * <p>
     *       Enqueues a command to unmap a previously mapped region of a memory object.
     *   </p>
     *
     * <div title="clEnqueueUnmapMemObject">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int  <b>clEnqueueUnmapMemObject </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem <var>memobj</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*mapped_ptr</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be a valid command-queue.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           memobj
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid memory object. The OpenCL context associated with <code>command_queue</code> and <code>memobj</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           mapped_ptr
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The host address returned by a previous call to <span><span>clEnqueueMapBuffer</span></span> or
     *             <span><span>clEnqueueMapImage</span></span> for <code>memobj</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           ,</code>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before  <code>clEnqueueUnmapMemObject</code> can be executed. If  <code>event_wait_list</code> is NULL, then <code>clEnqueueUnmapMemObject</code> does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular copy command and can be used to query or queue a wait for this particular command to complete. <code>event</code> can be NULL in which
     *             case it will not be possible for the application to query the status of this command or queue a
     *             wait for this command to complete. <span><span>clEnqueueBarrier</span></span> can be used instead.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Reads or
     *       writes from the host using the pointer returned by <span><span>clEnqueueMapBuffer</span></span> or
     *       <span><span>clEnqueueMapImage</span></span> are considered to be complete.
     *     </p>
     *     <p>
     *       <span><span>clEnqueueMapBuffer</span></span> and <span><span>clEnqueueMapImage</span></span> increment the mapped count of the memory
     *       object. The initial mapped count value of a memory object is zero. Multiple calls to
     *       <span><span>clEnqueueMapBuffer</span></span> or <span><span>clEnqueueMapImage</span></span> on the same memory object will increment this
     *       mapped count by appropriate number of calls. <code>clEnqueueUnmapMemObject</code> decrements the
     *       mapped count of the memory object.
     *     </p>
     *     <p>
     *       <span><span>clEnqueueMapBuffer</span></span> and <span><span>clEnqueueMapImage</span></span> act as synchronization points for a region of
     *       the buffer object being mapped.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueUnmapMemObject</code> returns <span>CL_SUCCESS</span> if the function is executed successfully.
     *       Otherwise, it returns one of the following errors:.
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>memobj</code> is not a valid memory object.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>mapped_ptr</code> is not a valid pointer returned by
     *           <span><span>clEnqueueMapBuffer</span></span> or
     *           <span><span>clEnqueueMapImage</span></span> for <code>memobj</code>.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL and
     *           <code>num_events_in_wait_list</code> greater than 0, or <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code> are
     *           not valid events.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with <code>command_queue</code>
     *           and <code>memobj</code> are not the same or if the context associated with <code>command_queue</code>
     *           and events in <code>event_wait_list</code> are not the same.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueUnmapMemObject(cl_command_queue command_queue, cl_mem memobj, ByteBuffer mapped_ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        if (event == null)
        {
            event = new cl_event();
        }
        int result = clEnqueueUnmapMemObjectNative(command_queue, memobj, mapped_ptr, num_events_in_wait_list, event_wait_list, event);
        scheduleReferenceRelease(event, mapped_ptr);
        return checkResult(result);
    }

    private static native int clEnqueueUnmapMemObjectNative(cl_command_queue command_queue, cl_mem memobj, ByteBuffer mapped_ptr, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to execute a kernel on a device.
     *   </p>
     *
     * <div title="clEnqueueNDRangeKernel">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>
     *             clEnqueueNDRangeKernel
     *             </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_kernel <var>kernel</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>work_dim</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>*global_work_offset</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>*global_work_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const size_t <var>*local_work_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid command-queue. The kernel will be queued for execution on the device associated with <code>command_queue</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           kernel
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid kernel object. The OpenCL context associated with <code>kernel</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           work_dim
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of dimensions used to specify the global work-items and work-items
     *             in the work-group. <code>work_dim</code> must be greater than zero and less than or
     *             equal to CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           global_work_offset
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             <code>global_work_offset</code> can be used to specify an array
     *             of <code>work_dim</code> unsigned values that describe the offset used to calculate
     *             the global ID of a work-item. If <code>global_work_offset</code> is NULL,
     *             the global IDs start at offset (0, 0, ... 0).
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           global_work_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Points to an array of <code>work_dim</code> unsigned values that describe the number of global work-items in <code>work_dim</code> dimensions that will execute the kernel function. The total number of global work-items is computed as <code>global_work_size</code>[0] *...* <code>global_work_size</code>[<code>work_dim</code> - 1].
     *           </p>
     *           <p>
     *             The values specified in <code>global_work_size</code> + corresponding values specified in <code>global_work_offset</code> cannot exceed the range given by the <code>sizeof(size_t)</code> for the device on which the kernel execution will be enqueued. The <code>sizeof(size_t)</code> for a device can be determined using <code>CL_DEVICE_ADDRESS_BITS</code> in the table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span>. If, for example, <code>CL_DEVICE_ADDRESS_BITS</code> = 32, i.e. the device uses a 32-bit address space, size_t is a 32-bit unsigned integer and <code>global_work_size</code> values must be in the range 1 .. 2^32 - 1. Values outside this range return a <code>CL_OUT_OF_RESOURCES</code> error.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           local_work_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Points to an array of <code>work_dim</code> unsigned values that describe the number of work-items that make up a work-group (also referred to as the size of the work-group) that will execute the kernel specified by <code>kernel</code>. The total number of work-items in a work-group is computed as <code>local_work_size</code>[0] *... * <code>local_work_size</code>[<code>work_dim</code> - 1]. The total number of work-items in the work-group must be less than or equal to the
     *             <code>CL_DEVICE_MAX_WORK_GROUP_SIZE</code> value specified in table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span> and the number of work-items specified in <code>local_work_size</code>[0],... <code>local_work_size</code>[<code>work_dim</code> - 1] must be less than or
     *             equal to the corresponding values specified by <code>CL_DEVICE_MAX_WORK_ITEM_SIZES</code>[0],....
     *             <code>CL_DEVICE_MAX_WORK_ITEM_SIZES</code>[<code>work_dim</code> - 1]. The explicitly specified <code>local_work_size</code> will be used to determine how to break the global work-items specified by <code>global_work_size</code> into appropriate work-group instances. If <code>local_work_size</code> is specified, the values specified in <code>global_work_size</code>[0],... <code>global_work_size</code>[<code>work_dim</code> - 1] must be evenly divisible by the corresponding values specified in <code>local_work_size</code>[0],... <code>local_work_size</code>[<code>work_dim</code> - 1].
     *           </p>
     *           <p>The work-group size to be used for <code>kernel</code> can also be specified in the program source using the <code><span><span>__attribute__</span></span>((reqd_work_group_size(X, Y, Z)))</code>qualifier. In this case the size of work group specified by <code>local_work_size</code> must match the value specified by the <code>reqd_work_group_size</code> <span><span>__attribute__</span></span> qualifier.</p>
     *           <p><code>local_work_size</code> can also be a NULL value in which case the OpenCL implementation will determine how to be break the global work-items into appropriate work-group instances.</p>
     *           <p>See note for more information.
     *           </p>
     *           <p>
     *             These work-group instances are executed in parallel across multiple compute units or concurrently on the same compute unit.
     *           </p>
     *           <p>
     *             Each work-item is uniquely identified by a global identifier. The global ID, which can be read inside the kernel, is computed using the value given by <code>global_work_size</code> and <code>global_work_offset</code>. In addition, a work-item is also identified within a work-group by a unique local ID. The local ID, which can also be read by the kernel, is computed using the value given by <code>local_work_size</code>. The starting local ID is always (0, 0, ... 0).
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code> and
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this
     *             particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code>
     *             must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in
     *             <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular kernel execution instance. Event objects are unique and can be used to identify a particular kernel execution instance later on. If <code>event</code> is NULL, no event will be created for this kernel execution instance and therefore it will not be possible for the application to query or queue a wait for this particular kernel execution instance.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the kernel execution was successfully queued.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PROGRAM_EXECUTABLE</span> if there is no successfully built program
     *           executable available for device associated with <code>command_queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a
     *           valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_KERNEL</span> if <code>kernel</code> is not a valid kernel object.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if context associated with <code>command_queue</code>
     *           and <code>kernel</code> is not the same or if the context associated
     *           with <code>command_queue</code> and events in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_KERNEL_ARGS</span> if the kernel argument values have not been specified.
     *         </li>
     *         <li><span>CL_INVALID_WORK_DIMENSION</span> if <code>work_dim</code> is not a valid value
     *           (i.e. a value between 1 and 3).
     *         </li>
     *         <li><span>CL_INVALID_GLOBAL_WORK_SIZE</span> if <code>global_work_size</code> is NULL, or
     *           if any of the values specified in <code>global_work_size</code>[0], ...<code>global_work_size</code>
     *           [<code>work_dim</code> - 1] are 0 or exceed the range given by the <code>sizeof(size_t)</code> for the
     *           device on which the kernel execution will be enqueued.
     *         </li>
     *         <li><span>CL_INVALID_GLOBAL_OFFSET</span> if the value specified in <code>global_work_size</code>
     *           + the corresponding values in <code>global_work_offset</code> for any dimensions is greater
     *           than the <code>sizeof(size_t)</code> for the device on which the kernel execution will be enqueued.
     *         </li>
     *         <li><span>CL_INVALID_WORK_GROUP_SIZE</span> if <code>local_work_size</code> is
     *           specified and number of work-items specified by <code>global_work_size</code> is not evenly
     *           divisable by size of work-group given by <code>local_work_size</code> or does not match the
     *           work-group size specified for <code>kernel</code> using
     *           the
     *           <span><span><span>__attribute__</span></span>
     *           ((reqd_work_group_size(X, Y, Z)))</span> qualifier in program source.
     *         </li>
     *         <li><span>CL_INVALID_WORK_GROUP_SIZE</span> if <code>local_work_size</code> is specified and the total number of work-items in the work-group computed as <code>local_work_size</code>[0] *... <code>local_work_size</code>[<code>work_dim</code> - 1] is greater than the value specified by <span>CL_DEVICE_MAX_WORK_GROUP_SIZE</span> in the table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span>.
     *         </li>
     *         <li><span>CL_INVALID_WORK_GROUP_SIZE</span> if <code>local_work_size</code> is NULL and the <span><span><span>__attribute__</span></span>((reqd_work_group_size(X, Y, Z)))</span> qualifier is used to declare the work-group size for <code>kernel</code> in the program source.
     *         </li>
     *         <li><span>CL_INVALID_WORK_ITEM_SIZE</span> if the number of work-items specified in any of <code>local_work_size</code>[0], ... <code>local_work_size</code>[<code>work_dim</code> - 1] is greater than the corresponding values specified by <span>CL_DEVICE_MAX_WORK_ITEM_SIZES</span>[0], ....
     *           <span>CL_DEVICE_MAX_WORK_ITEM_SIZES</span>[<code>work_dim</code> - 1].
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if a sub-buffer object is specified as the value
     *           for an argument that is a buffer object and the <code>offset</code> specified when the sub-buffer object
     *           is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if an image object is specified as an argument value
     *           and the image dimensions (image width, height, specified or compute row and/or slice pitch) are not
     *           supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to queue the execution instance
     *           of <code>kernel</code> on the command-queue because of insufficient resources needed to execute the kernel.
     *           For example, the explicitly specified <code>local_work_size</code> causes a failure to execute the kernel
     *           because of insufficient resources such as registers or local memory. Another example would be the number of
     *           read-only image args used in <code>kernel</code> exceed the <span>CL_DEVICE_MAX_READ_IMAGE_ARGS</span>
     *           value for device or the number of write-only image args used in <code>kernel</code> exceed
     *           the <span>CL_DEVICE_MAX_WRITE_IMAGE_ARGS</span> value for device or the number of samplers used
     *           in <code>kernel</code> exceed <span>CL_DEVICE_MAX_SAMPLERS</span> for device.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory
     *           for data store associated with image or buffer objects specified as arguments to <code>kernel</code>.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL
     *           and <code>num_events_in_wait_list</code> &gt; 0, or <code>event_wait_list</code> is not NULL
     *           and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code>
     *           are not valid events.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueNDRangeKernel(cl_command_queue command_queue, cl_kernel kernel, int work_dim, long global_work_offset[], long global_work_size[], long local_work_size[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueNDRangeKernelNative(command_queue, kernel, work_dim, global_work_offset, global_work_size, local_work_size, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueNDRangeKernelNative(cl_command_queue command_queue, cl_kernel kernel, int work_dim, long global_work_offset[], long global_work_size[], long local_work_size[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to execute a kernel on a device.
     *   </p>
     *
     * <div title="clEnqueueTask">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>
     *             clEnqueueTask
     *             </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_kernel <var>kernel</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid command-queue. The kernel will be queued for execution on the
     *             device associated with <code>command_queue</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           kernel
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid kernel object. The OpenCL context associated with <code>kernel</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code> and <code>event_wait_list</code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this
     *             particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code> must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and command_queue must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular kernel execution instance. Event objects are unique and can be used to identify a particular kernel execution instance later on. If <code>event</code> is NULL, no event will be created for this kernel execution instance and therefore it will not be possible for the application to query or queue a wait for this particular kernel execution instance.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The kernel is executed using a single work-item.
     *     </p>
     *     <p>
     *       <code>clEnqueueTask</code> is equivalent to calling <span><span>clEnqueueNDRangeKernel</span></span> with <code>work_dim</code> = 1,
     *       <code>global_work_offset</code> = NULL, <code>global_work_size</code>[0] set to 1, and <code>local_work_size</code>[0] set to 1.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the kernel execution was successfully queued, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_PROGRAM_EXECUTABLE</span> if there is no successfully built program
     *           executable available for device associated with <code>command_queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_KERNEL</span> if <code>kernel</code> is not a valid kernel object.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if context associated with <code>command_queue</code>
     *           and <code>kernel</code> is not the same or if the context associated with <code>command_queue</code>
     *           and events in <code>event_wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_KERNEL_ARGS</span> if the kernel argument values have not been specified.
     *         </li>
     *         <li><span>CL_INVALID_WORK_GROUP_SIZE</span> if a work-group size is specified for <code>kernel</code> using the <span><span><span>__attribute__</span></span>((reqd_work_group_size(X, Y, Z)))</span> qualifier in program source and is not (<span>1</span>, <span>1</span>, <span>1</span>).
     *         </li>
     *         <li><span>CL_MISALIGNED_SUB_BUFFER_OFFSET</span> if a sub-buffer object is specified as the value
     *           for an argument that is a buffer object and the <code>offset</code> specified when the sub-buffer object
     *           is created is not aligned to CL_DEVICE_MEM_BASE_ADDR_ALIGN value for device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_SIZE</span> if an image object is specified as an argument value
     *           and the image dimensions (image width, height, specified or compute row and/or slice pitch) are not
     *           supported by device associated with <code>queue</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to queue the execution instance of
     *           <code>kernel</code> on the command-queue because of insufficient resources needed to execute the kernel.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory for
     *           data store associated with image or buffer objects specified as arguments to <code>kernel</code>.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL
     *           and <code>num_events_in_wait_list</code> is greater than 0, or <code>event_wait_list</code> is not
     *           NULL and <code>num_events_in_wait_list</code> is 0, or if event objects in <code>event_wait_list</code>
     *           are not valid events.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueTask(cl_command_queue command_queue, cl_kernel kernel, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueTaskNative(command_queue, kernel, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueTaskNative(cl_command_queue command_queue, cl_kernel kernel, int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a command to execute a native C/C++ function not compiled using the OpenCL compiler.
     *   </p>
     *
     * <div title="clEnqueueNativeKernel">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>
     *             clEnqueueNativeKernel
     *             </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>(*user_func)(void *)</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*args</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>cb_args</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_mem_objects</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_mem <var>*mem_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const void <var>**args_mem_loc</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid command-queue. A native user function can only be executed on a
     *             command-queue created on a device that has CL_EXEC_NATIVE_KERNEL capability set in
     *             CL_DEVICE_EXECUTION_CAPABILITIES as specified in the table of OpenCL Device Queries for <span><span>clGetDeviceInfo</span></span>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           user_func
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a host-callable user function.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           args
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to the args list that <code>user_func</code> should be called with.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           cb_args
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The size in bytes of the args list that <code>args</code> points to.
     *           </p>
     *           <p>
     *             The data pointed to by <code>args</code> and <code>cb_args</code> bytes in size will be copied and a pointer to this copied region will be passed to <code>user_func</code>. The copy needs to be done because the memory objects (<code>cl_mem</code> values) that <code>args</code> may contain need to be modified and replaced by appropriate pointers to global memory. When <code>clEnqueueNativeKernel</code> returns, the memory region pointed to by <code>args</code> can be reused by the application.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_mem_objects
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of buffer objects that are passed in <code>args</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           mem_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A list of valid buffer objects, if <code>num_mem_objects</code> is greater than 0. The buffer object values
     *             specified in <code>mem_list</code> are memory object handles (<code>cl_mem</code> values) returned by <span><span>clCreateBuffer</span></span> or NULL.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           args_mem_loc
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to appropriate locations that <code>args</code> points to where memory object handles (<code>cl_mem</code> values) are stored. Before the user function is executed, the memory object handles are replaced by pointers to global memory.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code> and
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this
     *             particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command does not wait on any event to complete. If <code>event_wait_list</code> is NULL, <code>num_events_in_wait_list</code>
     *             must be 0. If <code>event_wait_list</code> is not NULL, the list of events pointed to by <code>event_wait_list</code> must be valid and <code>num_events_in_wait_list</code> must be greater than 0. The events specified in
     *             <code>event_wait_list</code> act as synchronization points. The context associated with events in <code>event_wait_list</code> and <code>command_queue</code> must be the same.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular kernel execution instance. Event objects are unique and can be used to identify a particular kernel execution instance later on. If <code>event</code> is NULL, no event will be created for this kernel execution instance and therefore it will not be possible for the application to query or queue a wait for this particular kernel execution instance.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The data pointed to by <code>args</code> and <code>cb_args</code> bytes in size will be copied and a pointer to this copied region will be passed to <code>user_func</code>. The copy needs to be done because the memory objects
     *       (<code>cl_mem</code> values) that <code>args</code> may contain need to be modified and replaced by appropriate pointers to global memory. When <code>clEnqueueNativeKernel</code> returns, the memory region pointed to by <code>args</code> can be reused by the application.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the user function execution instance was
     *       successfully queued, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if context associated with <code>command_queue</code>
     *           and events in <code>event-wait_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>user_func</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>args</code> is a NULL value and
     *           <code>cb_args</code> is greater than 0, or if <code>args</code> is a NULL
     *           value and <code>num_mem_objects</code> is greater than 0.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>args</code> is not NULL and
     *           <code>cb_args</code> is 0.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>num_mem_objects</code> is greater than 0
     *           and <code>mem_list</code> or <code>args_mem_loc</code> are NULL.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>num_mem_objects</code> = 0 and
     *           <code>mem_list</code> or <code>args_mem_loc</code> are not NULL.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if the device associated with <code>command_queue</code> cannot execute the native kernel.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if one or more memory objects specified
     *           in <code>mem_list</code> are not valid or are not buffer objects.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to queue the execution
     *           instance of <code>kernel</code> on the command-queue because of insufficient resources
     *           needed to execute the kernel.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_MEM_OBJECT_ALLOCATION_FAILURE</span> if there is a failure to allocate memory
     *           for data store associated with buffer objects specified as arguments to <code>kernel</code>.
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is NULL
     *           and <code>num_events_in_wait_list</code> is greater than 0, or <code>event_wait_list</code>
     *           is not NULL and <code>num_events_in_wait_list</code> is 0, or if event objects
     *           in <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueNativeKernel(cl_command_queue command_queue, EnqueueNativeKernelFunction user_func, Object args, long cb_args, int num_mem_objects, cl_mem mem_list[], Pointer args_mem_loc[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueNativeKernelNative(command_queue, user_func, args, cb_args, num_mem_objects, mem_list, args_mem_loc, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueNativeKernelNative(cl_command_queue command_queue, EnqueueNativeKernelFunction user_func, Object args, long cb_args, int num_mem_objects, cl_mem mem_list[], Pointer args_mem_loc[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Enqueues a marker command.
     *   </p>
     *
     * <div title="clEnqueueMarker">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueMarker</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       Enqueues a marker command to <code>command_queue</code>. The marker command is not completed until
     *       all commands enqueued before it have completed. The marker command returns an <code>event</code> which
     *       can be waited on, i.e. this event can be waited on to ensure that all commands which have been queued before the market command have been completed.
     *       complete.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function executed successfully, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>event</code> is a NULL value.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueMarker(cl_command_queue command_queue, cl_event event)
    {
        return checkResult(clEnqueueMarkerNative(command_queue, event));
    }

    private static native int clEnqueueMarkerNative(cl_command_queue command_queue, cl_event event);

    /**
     * <p>
     *       Enqueues a wait for a specific event or a list of events to complete before any future commands
     *       queued in the command-queue are executed.
     *   </p>
     *
     * <div title="clEnqueueWaitForEvents">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueWaitForEvents</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_list</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid command-queue.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_events
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the number of events given by <code>event_list</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Events specified in <code>event_list</code> act as synchronization points.  The context associated
     *             with events in <code>event_list</code> and <code>command_queue</code> must be the same.
     *             Each event in <code>event_list</code> must be a valid event object returned by a previous call
     *             to the following:
     *           </p>
     *           <div>
     *             <ul type="disc">
     *               <li>
     *                 <span>
     *                 <span>clEnqueueNDRangeKernel</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueTask</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueNativeKernel</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueReadImage</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueWriteImage</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueMapImage</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueReadBuffer</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueWriteBuffer</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueMapBuffer</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueUnmapMemObject</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueReadBufferRect</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueWriteBufferRect</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueCopyBuffer</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueCopyImage</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueCopyBufferRect</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueCopyBufferToImage</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueCopyImageToBuffer</span>
     *                 </span>
     *               </li>
     *               <li>
     *                 <span>
     *                 <span>clEnqueueMarker</span>
     *                 </span>
     *               </li>
     *             </ul>
     *           </div>
     *           <p>
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       The context associated with events in <code>event_list</code> and
     *       <code>command_queue</code> must be the same.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function was successfully executed, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code>
     *           is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if the context associated with
     *           <code>command_queue</code> and events in <code>event_list</code> are not the same.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>num_events</code> is zero or
     *           <code>event_list</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_EVENT</span> if event objects specified in
     *           <code>event_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueWaitForEvents(cl_command_queue command_queue, int num_events, cl_event event_list[])
    {
        return checkResult(clEnqueueWaitForEventsNative(command_queue, num_events, event_list));
    }

    private static native int clEnqueueWaitForEventsNative(cl_command_queue command_queue, int num_events, cl_event event_list[]);

    /**
     * <p>
     *       A synchronization point that enqueues a barrier operation.
     *   </p>
     *
     * <div title="clEnqueueBarrier">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>
     *             cl_int
     *             <b>clEnqueueBarrier</b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var><code>)</code></td>
     *           </td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       <code>clEnqueueBarrier</code> is a synchronization point that ensures that all
     *       queued commands in <code>command_queue</code> have finished execution before the next
     *       batch of commands can begin execution.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function was successfully executed, or one of the errors below:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code>
     *           is not a valid command-queue.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources required by the
     *           OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueBarrier(cl_command_queue command_queue)
    {
        return checkResult(clEnqueueBarrierNative(command_queue));
    }

    private static native int clEnqueueBarrierNative(cl_command_queue command_queue);

    /**
     * <p>
     *       Creates an OpenCL buffer object from an OpenGL buffer object.
     *   </p>
     *
     * <div title="clCreateFromGLBuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_mem
     *             <b>
     *             clCreateFromGLBuffer
     *             </b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLuint <var>bufobj</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>* errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid OpenCL context created from an OpenGL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify usage information.
     *             Refer to the table for
     *             <span><span>clCreateBuffer</span></span>
     *             for a description of <code>flags</code>. Only
     *             CL_MEM_READ_ONLY, CL_MEM_WRITE_ONLY and CL_MEM_READ_WRITE values specified in the table at  <span><span>clCreateBuffer</span></span>
     *             can be used.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           bufobj
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The name of a GL buffer object. The data store of the GL buffer object must have have been previously created by calling OpenGL function <code>glBufferData</code>, although its contents need not be initialized. The size of the data store will be used to determine the size of the CL buffer object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code as described below. If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Description">
     *     <h2>Description</h2>
     *     <p>
     *       The size of the GL buffer object data store at the time <code>clCreateFromGLBuffer</code> is called will be used as the size of buffer object returned by <code>clCreateFromGLBuffer</code>. If the state of a GL buffer object is modified through the GL API (e.g.  <code>glBufferData</code>) while there exists a corresponding CL buffer object, subsequent use of the CL buffer object will result in undefined behavior.
     *     </p>
     *     <p>
     *       The <span><span>clRetainMemObject</span></span> and <span><span>clReleaseMemObject</span></span> functions can be used to retain and release
     *       the buffer object.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero OpenCL buffer object and <code>errcode_ret</code> is set
     *       to CL_SUCCESS if the buffer object is created successfully. Otherwise, it returns a NULL value
     *       with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not a valid context or was
     *           not created from a GL context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>flags</code> are not valid.
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if <code>bufobj</code> is not a GL buffer object or is a
     *           GL buffer object but does not have an existing data store.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateFromGLBuffer(cl_context context, long flags, int bufobj, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateFromGLBufferNative(context, flags, bufobj, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateFromGLBufferNative(context, flags, bufobj, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateFromGLBufferNative(cl_context context, long flags, int bufobj, int errcode_ret[]);

    /**
     * <p>
     *       Creates an OpenCL 2D image object from an OpenGL 2D texture object, or a single face of an
     * OpenGL cubemap texture object.
     *   </p>
     *
     * <div title="clCreateFromGLTexture2D">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_mem
     *             <b>
     *             clCreateFromGLTexture2D
     *             </b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLenum <var>texture_target</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLint <var>miplevel</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLuint <var>texture</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>*errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid OpenCL context created from an OpenGL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify usage information. Refer to
     *             the table for
     *             <span><span><code>clCreateBuffer</code></span></span>
     *             for a description of <code>flags</code>. Only CL_MEM_READ_ONLY, CL_MEM_WRITE_ONLY and CL_MEM_READ_WRITE
     *             values specified in the table for <span><span><code>clCreateBuffer</code></span></span>
     *             may be used.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           texture_target
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Must be one of GL_TEXTURE_2D, GL_TEXTURE_CUBE_MAP_POSITIVE_X,
     *             GL_TEXTURE_CUBE_MAP_POSITIVE_Y, GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
     *             GL_TEXTURE_CUBE_MAP_NEGATIVE_X, GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
     *             GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, or GL_TEXTURE_RECTANGLE. <code>texture_target</code> is
     *             used only to define the image type of <code>texture</code>. No reference to a bound GL
     *             texture object is made or implied by this parameter.
     *           </p>
     *           <p>
     *             Using GL_TEXTURE_RECTANGLE for texture_target requires OpenGL 3.1. Alternatively,
     *             GL_TEXTURE_RECTANGLE_ARB may be specified if the OpenGL extension GL_ARB_texture_rectangle
     *             is supported.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           miplevel
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The mipmap level to be used. Implementations may return CL_INVALID_OPERATION for
     *             miplevel values greater than 0.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           texture
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The name of a GL 2D, cubemap or rectangle texture object. The
     *             texture object must be a complete texture as per OpenGL rules on
     *             texture completeness. The <code>texture</code> format and
     *             dimensions defined by OpenGL for the specified <code>miplevel</code>
     *             of the texture will be used to create the 2D image object. Only GL texture
     *             objects with an internal format that maps to appropriate image channel order
     *             and data type specified in the table of supported Image Channel Order
     *             Values and the table of supported Image Channel Data Types for
     *             <span><span><span>cl_image_format</span></span></span>
     *             may be used to create a 2D image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code as described below.
     *             If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero OpenCL image object and <code>errcode_ret</code> is
     *       set to <span>CL_SUCCESS</span> if the image object is
     *       created successfully. Otherwise, it returns a NULL
     *       value with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code>
     *           is not a valid context or was not created from a GL context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in
     *           <code>flags</code> are not valid or if value specified in
     *           <code>texture_target</code> is not one of the values specified
     *           in the description of <code>texture_target</code>.
     *         </li>
     *         <li><span>CL_INVALID_MIP_LEVEL</span> if <code>miplevel</code> is less
     *           than the value of <code>level<sub>base</sub></code> (for OpenGL
     *           implementations) or zero (for OpenGL ES implementations); or greater than the value of
     *           <code>q</code> (for both OpenGL and OpenGL ES).
     *           <code>level<sub>base</sub></code> and <code>q</code>
     *           are defined for the texture in section 3.8.10 (Texture Completeness) of the OpenGL 2.1 specification and section 3.7.10 of the OpenGL ES 2.0 specification.
     *         </li>
     *         <li><span>CL_INVALID_MIP_LEVEL</span> if <code>miplevel</code>
     *           is greater than zero and the OpenGL implementation does not support creating from non-zero mipmap levels.
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if <code>texture</code>
     *           is not a GL texture object whose type matches
     *           <code>texture_target</code>, if the specified <code>miplevel</code> of
     *           <code>texture</code> is not defined,
     *           or if the width or height
     *           of the specified <code>miplevel</code> is zero.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_FORMAT_DESCRIPTOR</span> if the
     *           OpenGL texture internal format does not map to a supported OpenCL image format.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if <code>texture</code>
     *           is a GL texture object created with a border width  value greater than zero.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate
     *           resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateFromGLTexture2D(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateFromGLTexture2DNative(context, flags, target, miplevel, texture, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateFromGLTexture2DNative(context, flags, target, miplevel, texture, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateFromGLTexture2DNative(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[]);

    /**
     * <p>
     *       Creates an OpenCL 3D image object from an OpenGL 3D texture object.
     *   </p>
     *
     * <div title="clCreateFromGLTexture3D">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_mem
     *             <b>
     *             clCreateFromGLTexture3D
     *             </b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLenum <var>texture_target</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLint <var>miplevel</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLuint <var>texture</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>* errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid OpenCL context created from an OpenGL 3D context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify usage information. Refer to
     *             the table for
     *             <code><span><span>clCreateBuffer</span></span></code>
     *             for a description of <code>flags</code>. Only the valuesCL_MEM_READ_ONLY, CL_MEM_WRITE_ONLY and CL_MEM_READ_WRITE  can be used.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           texture_target
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             <code>texture_target</code> is used only to define the image
     *             type of <code>texture</code>. Must be GL_TEXTURE_3D. No reference to
     *             a bound GL texture object is made or implied by this parameter.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           miplevel
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The mipmap level to be used.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           texture
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The name of a GL 3D texture object. The texture object must be a
     *             complete texture as per OpenGL rules on texture completeness. The
     *             <code>texture</code> format and dimensions defined by OpenGL
     *             for the specified <code>miplevel</code> of the texture will be
     *             used to create the 3D image object. Only GL texture objects with an
     *             internal format that maps to appropriate image channel order and data
     *             type specified in the table of supported Image Channel Order Values
     *             and the table of supported Image Channel Data Types at
     *             <span><span><span>cl_image_format</span></span></span> can be used to create the 3D image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code as described below.
     *             If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Description">
     *     <h2>Description</h2>
     *     <p>
     *       If the state of a GL texture object is modified through the GL API (e.g. the OpenGL functions
     *       <code>glTexImage2D</code>, <code>glTexImage3D</code>,
     *       or the values of the texture parameters GL_TEXTURE_BASE_LEVEL or
     *       GL_TEXTURE_MAX_LEVEL are modified) while there exists a corresponding CL image object,
     *       subsequent use of the CL image object will result in undefined behavior.
     *     </p>
     *     <p>
     *       The
     *       <span><span><code>clRetainMemObject</code></span></span>
     *       and
     *       <span><span><code>clReleaseMemObject</code></span></span>
     *       functions can be used to retain and release
     *       the image objects.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero OpenCL image object and <code>errcode_ret</code> is
     *       set to <span>CL_SUCCESS</span> if the image object is
     *       created successfully. Otherwise, it returns a NULL
     *       value with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code>
     *           is not a valid context or was not created from a GL context.
     *           .
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>flags</code>
     *           are not valid or if value specified in <code>texture_target</code> is not one of the
     *           values specified in the description of <code>texture_target</code>.
     *         </li>
     *         <li><span>CL_INVALID_MIP_LEVEL</span> if <code>miplevel</code> is less than the
     *           value of <code>level<sub>base</sub></code> (for OpenGL implementations) or zero
     *           (for OpenGL ES implementations); or greater than the value of <code>q</code> (for both OpenGL
     *           and OpenGL ES). <code>level<sub>base</sub></code> and <code>q</code> are
     *           defined for the texture in section 3.8.10 (Texture Completeness) of the OpenGL 2.1 specification
     *           and section 3.7.10 of the OpenGL ES 2.0.
     *         </li>
     *         <li><span>CL_INVALID_MIP_LEVEL</span> if <code>miplevel</code> is greater
     *           than zero and the OpenGL implementation does not support creating from non-zero mipmap levels.
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if <code>texture</code> is not a
     *           GL texture object whose type matches <code>texture_target</code>, if the
     *           specified <code>miplevel</code> of <code>texture</code> is not defined,
     *           or if the width or height of the specified <code>miplevel</code> is zero.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_FORMAT_DESCRIPTOR</span> if the OpenGL texture internal
     *           format does not map to a supported OpenCL image format.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if texture is a GL texture object created with a
     *           border width value greater than zero.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateFromGLTexture3D(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateFromGLTexture3DNative(context, flags, target, miplevel, texture, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateFromGLTexture3DNative(context, flags, target, miplevel, texture, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateFromGLTexture3DNative(cl_context context, long flags, int target, int miplevel, int texture, int errcode_ret[]);

    /**
     * <p>
     *       Creates an OpenCL 2D image object from an OpenGL renderbuffer object.
     *   </p>
     *
     * <div title="clCreateFromGLRenderbuffer">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_mem
     *             <b>
     *             clCreateFromGLRenderbuffer
     *             </b>
     *             (</code>
     *           <td>cl_context <var>context</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_mem_flags <var>flags</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLuint <var>renderbuffer</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_int <var>* errcode_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           context
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid OpenCL context created from an OpenGL context.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           flags
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A bit-field that is used to specify usage information. Refer to the table at <span><span>clCreateBuffer</span></span> for a description of <code>flags</code>. Only CL_MEM_READ_ONLY, CL_MEM_WRITE_ONLY, and CL_MEM_READ_WRITE values specified in the table at <span><span>clCreateBuffer</span></span> can be used.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           renderbuffer
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The name of a GL renderbuffer object. The renderbuffer storage must be
     *             specified before the image object can be created. The <code>renderbuffer</code>
     *             format and dimensions defined by OpenGL will be used to create the 2D image object. Only GL renderbuffers with
     *             internal formats that map to appropriate image channel order and data type specified in the table of supported Image Channel Order Values and
     *             the table of supported Image Channel Data Types for  <span><span><span>cl_image_format</span></span></span>
     *             can be used to create the 2D image object.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           errcode_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an appropriate error code as described below.
     *             If <code>errcode_ret</code> is NULL, no error code is returned.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Description">
     *     <h2>Description</h2>
     *     <p>
     *       If the state of a GL renderbuffer object is modified through the GL API (i.e. changes to the
     *       dimensions or format used to represent pixels of the GL renderbuffer using appropriate GL API
     *       calls such as <code>glRenderbufferStorage</code>) while there exists a corresponding CL image object,
     *       subsequent use of the CL image object will result in undefined behavior.
     *     </p>
     *     <p>
     *       The <span><span>clRetainMemObject</span></span>
     *       and <span><span>clReleaseMemObject</span></span> functions can be used to retain and release the image objects.
     *     </p>
     *     <p>
     *       The table below describes the list of GL renderbuffer internal formats and the corresponding CL image
     *       formats. If a GL renderbuffer object with an internal format from the table below is successfully
     *       created by OpenGL, then there is guaranteed to be a mapping to one of the corresponding CL
     *       image format(s) in that table. Renderbuffer objects created with other OpenGL internal formats
     *       may (but are not guaranteed to) have a mapping to a CL image format; if such mappings exist,
     *       they are guaranteed to preserve all color components, data types, and at least the number of
     *       bits/component actually allocated by OpenGL for that format.
     *     </p>
     *     <div>
     *       <table border="1">
     *         <colgroup>
     *           <col align="left" />
     *           <col align="left" />
     *         </colgroup>
     *         <thead>
     *           <tr>
     *             <th align="left">GL internal format</th>
     *             <th align="left">CL image format (channel order, channel data type)</th>
     *           </tr>
     *         </thead>
     *         <tbody>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA8
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_UNORM_INT8 or CL_BGRA, CL_UNORM_INT8
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left"> </td>
     *             <td align="left"> </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA16
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_UNORM_INT16
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left"> </td>
     *             <td align="left"> </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA8I, GL_RGBA8I_EXT
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_SIGNED_INT8
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA16I, GL_RGBA16I_EXT
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_SIGNED_INT16
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA32I, GL_RGBA32I_EXT
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_SIGNED_INT32
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left"> </td>
     *             <td align="left"> </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA8UI, GL_RGBA8UI_EXT
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_UNSIGNED_INT8
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA16UI, GL_RGBA16UI_EXT
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_UNSIGNED_INT16
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA32UI, GL_RGBA32UI_EXT
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_UNSIGNED_INT32
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left"> </td>
     *             <td align="left"> </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA16F, GL_RGBA16F_ARB
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_HALF_FLOAT
     *             </td>
     *           </tr>
     *           <tr>
     *             <td align="left"> </td>
     *             <td align="left"> </td>
     *           </tr>
     *           <tr>
     *             <td align="left">
     *               GL_RGBA32F, GL_RGBA32F_ARB
     *             </td>
     *             <td align="left">
     *               CL_RGBA, CL_FLOAT
     *             </td>
     *           </tr>
     *         </tbody>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns a valid non-zero OpenCL image object and <code>errcode_ret</code>
     *       is set to CL_SUCCESS if the image object is created successfully. Otherwise, it returns a NULL
     *       value with one of the following error values returned in <code>errcode_ret</code>:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_CONTEXT</span> if <code>context</code> is not
     *           a valid context or was not created from a GL context.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if values specified in <code>flags</code> are not valid.
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if <code>renderbuffer</code>
     *           is not a GL renderbuffer object or if the width or height of <code>renderbuffer</code> is zero.
     *         </li>
     *         <li><span>CL_INVALID_IMAGE_FORMAT_DESCRIPTOR</span> if the OpenGL
     *           renderbuffer internal format does not map to a supported OpenCL image format.
     *         </li>
     *         <li><span>CL_INVALID_OPERATION</span> if <code>renderbuffer</code> is a multi-sample
     *           GL renderbuffer object.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to
     *           allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static cl_mem clCreateFromGLRenderbuffer(cl_context context, long flags, int renderbuffer, int errcode_ret[])
    {
        if (exceptionsEnabled)
        {
            if (errcode_ret == null)
            {
                errcode_ret = new int[1];
            }
            cl_mem result = clCreateFromGLRenderbufferNative(context, flags, renderbuffer, errcode_ret);
            checkResult(errcode_ret[0]);
            return result;
        }
        else
        {
            cl_mem result = clCreateFromGLRenderbufferNative(context, flags, renderbuffer, errcode_ret);
            return result;
        }
    }

    private static native cl_mem clCreateFromGLRenderbufferNative(cl_context context, long flags, int renderbuffer, int errcode_ret[]);

    /**
     * <p>
     * Query an OpenGL memory object used to create an OpenCL memory object.
     *   </p>
     *
     * <div title="clGetGLObjectInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int
     *             <b>
     *             clGetGLObjectInfo
     *             </b>
     *             (</code>
     *           <td>cl_mem <var>memobj</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_gl_object_type <var>*gl_object_type</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>GLuint <var>*gl_object_name</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           gl_object_type
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the type of GL object attached to <code>memobj</code> and can be
     *             CL_GL_OBJECT_BUFFER,
     *             CL_GL_OBJECT_TEXTURE2D,
     *             CL_GL_OBJECT_TEXTURE3D, or
     *             CL_GL_OBJECT_RENDERBUFFER.
     *             If <code>gl_object_type</code> is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           gl_object_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the GL object name used to create <code>memobj</code>. If <code>gl_object_name</code> is NULL,
     *             it is ignored.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Description">
     *     <h2>Description</h2>
     *     <p>
     *       The OpenGL object used to create the OpenCL memory object and information about the object
     *       type i.e. whether it is a texture, renderbuffer, or buffer object can be queried using this function.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the call was executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>memobj</code> is not a valid OpenCL memory object
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if there is no GL object associated with <code>memobj</code>.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to
     *           allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetGLObjectInfo(cl_mem memobj, int gl_object_type[], int gl_object_name[])
    {
        return checkResult(clGetGLObjectInfoNative(memobj, gl_object_type, gl_object_name));
    }

    private static native int clGetGLObjectInfoNative(cl_mem memobj, int gl_object_type[], int gl_object_name[]);

    /**
     * <p>
     *       Returns additional information about the GL texture object associated with a memory object.
     *   </p>
     *
     * <div title="clGetGLTextureInfo">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int
     *             <b>
     *             clGetGLTextureInfo
     *             </b>
     *             (</code>
     *           <td>cl_mem <var>memobj</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_gl_texture_info <var>param_name</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>param_value_size</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>void <var>*param_value</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>size_t <var>*param_value_size_ret</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           param_name
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies what additional information about the GL texture object associated with
     *             <code>memobj</code> to query. The list of supported
     *             <code>param_name</code> types and the information returned in
     *             <code>param_value</code> by
     *             <code>clGetGLTextureInfo</code>
     *             is described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to memory where the result being queried is returned. If <code>param_value</code>
     *             is NULL, it is ignored.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specifies the size in bytes of memory pointed to by <code>param_value</code>.
     *             This size must be &gt;= size of return type as described in the table below.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           param_value_size_ret
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns the actual size in bytes of data copied to <code>param_value</code>. If
     *             <code>param_value_size_ret</code> is NULL, it is ignored.
     *           </p>
     *           <div>
     *             <table border="1">
     *               <colgroup>
     *                 <col align="left" />
     *                 <col align="left" />
     *                 <col align="left" />
     *               </colgroup>
     *               <thead>
     *                 <tr>
     *                   <th align="left">cl_gl_texture_info</th>
     *                   <th align="left">Return Type</th>
     *                   <th align="left">Information returned in <code>param_value</code></th>
     *                 </tr>
     *               </thead>
     *               <tbody>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_GL_TEXTURE_TARGET</code>
     *                   </td>
     *                   <td align="left">GLenum</td>
     *                   <td align="left">
     *                     The <code>texture_target</code> argument specified in
     *                     <span><span>clCreateGLTexture2D</span></span> or
     *                     <span><span>clCreateGLTexture3D</span></span>.
     *                   </td>
     *                 </tr>
     *                 <tr>
     *                   <td align="left">
     *                     <code>CL_GL_MIPMAP_LEVEL</code>
     *                   </td>
     *                   <td align="left">GLint</td>
     *                   <td align="left">
     *                     The <code>miplevel</code> argument specified in
     *                     <span><span>clCreateGLTexture2D</span></span> or
     *                     <span><span>clCreateGLTexture3D</span></span>.
     *                   </td>
     *                 </tr>
     *               </tbody>
     *             </table>
     *           </div>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if <code>memobj</code> is not a valid OpenCL
     *           memory object
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if there is no GL texture object associated with
     *           <code>memobj</code>.
     *         </li>
     *         <li><span>CL_INVALID_VALUE</span> if <code>param_name</code> is not valid, or if size
     *           in bytes specified by <code>param_value_size</code> is less than the size of return type as
     *           described in the table above and <code>param_value</code> is not NULL, or if
     *           <code>param_value</code> and <code>param_value_size_ret</code> are NULL.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to
     *           allocate resources required by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clGetGLTextureInfo(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetGLTextureInfoNative(memobj, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetGLTextureInfoNative(cl_mem memobj, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);

    /**
     * <p>
     *       Acquire OpenCL memory objects that have been created from OpenGL objects.
     *   </p>
     *
     * <div title="clEnqueueAcquireGLObjects">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int
     *             <b>
     *             clEnqueueAcquireGLObjects
     *             </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_objects</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_mem <var>*mem_objects</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid command-queue. All devices used to create the OpenCL context
     *             associated with <code>command_queue</code> must support acquiring shared CL/GL objects.
     *             This constraint is enforced at context creation time.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_objects
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of memory objects to be acquired in <code>mem_objects</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           mem_objects
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a list of CL memory objects that correspond to GL objects.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Specify events that need to complete before this
     *             particular command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL,
     *             <code>num_events_in_wait_list</code>
     *             must be 0. If <code>event_wait_list</code> is not NULL,
     *             the list of events pointed to by <code>event_wait_list</code> must
     *             be valid and <code>num_events_in_wait_list</code> must be greater than 0.
     *             The events specified in
     *             <code>event_wait_list</code> act as synchronization points.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this command and can be used to query or queue a
     *             wait for the command to complete. <code>event</code> can be NULL in which
     *             case it will not be possible for the application to query the status of this command or queue
     *             a wait for this command to complete.
     *           </p>
     *           <p>
     *             When the <span><span>cl_khr_gl_event</span></span>
     *             extension is enabled,
     *             If an OpenGL context is bound to the current thread, then any OpenGL commands which:
     *           </p>
     *           <div>
     *             <ul type="disc">
     *               <li>
     *                 affect or access the contents of a memory object listed in the <code>mem_objects</code> list, and
     *               </li>
     *               <li>
     *                 were issued on that OpenGL context prior to the call to
     *                 <span><span>clEnqueueAcquireGLObjects</span></span>
     *               </li>
     *             </ul>
     *           </div>
     *           <p>
     *             will complete before execution of any OpenCL commands following the
     *             <span><span>clEnqueueAcquireGLObjects</span></span>
     *             which affect or access any of those memory objects. If a non-NULL <code>event</code> object is
     *             returned, it will report completion only after completion of such OpenGL commands.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Description">
     *     <h2>Description</h2>
     *     <p>
     *       These
     *       objects need to be acquired before they can be used by any OpenCL commands queued to a
     *       command-queue. The OpenGL objects are acquired by the OpenCL context associated with
     *       <code>command_queue</code> and can therefore be used by all command-queues associated with the OpenCL
     *       context.
     *     </p>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       In order to ensure data integrity, the application is responsible for synchronizing access to shared
     *       CL/GL objects by their respective APIs. Failure to provide such synchronization may result in
     *       race conditions and other undefined behavior including non-portability between
     *       implementations.
     *     </p>
     *     <p>
     *       Prior to calling <code>clEnqueueAcquireGLObjects</code>, the application
     *       must ensure that any pending GL operations which access the objects specified in
     *       <code>mem_objects</code> have completed. This may be accomplished portably
     *       by issuing and waiting for completion of a
     *       <span><span>glFinish</span></span> command on
     *       all GL contexts with pending references to these objects. Implementations may
     *       offer more efficient synchronization methods; for example on some platforms
     *       calling <span><span>glFlush</span></span>
     *       may be sufficient, or synchronization may be implicit within a thread, or
     *       there may be vendor-specific extensions that enable placing a fence in the
     *       GL command stream and waiting for completion of that fence in the
     *       CL command queue. Note that no synchronization methods other than
     *       <span><span>glFinish</span></span> are
     *       portable between OpenGL implementations at this time.
     *     </p>
     *     <p>
     *       Similarly, after calling <code>clEnqueueReleaseGLObjects</code>,
     *       the application is responsible for ensuring that any pending OpenCL operations
     *       which access the objects specified in <code>mem_objects</code> have
     *       completed prior to executing subsequent GL commands which reference these objects. This
     *       may be accomplished portably by calling <span><span>clWaitForEvents</span></span> with the event object returned by
     *       <code>clEnqueueReleaseGLObjects</code>, or by calling <span><span>glFinish</span></span>. As above, some implementations may
     *       offer more efficient methods.
     *     </p>
     *     <p>
     *       The application is responsible for maintaining the proper order of operations if the CL and GL
     *       contexts are in separate threads.
     *     </p>
     *     <p>
     *       If a GL context is bound to a thread other than the one in which <code>clEnqueueReleaseGLObjects</code>
     *       is called, changes to any of the objects in <code>mem_objects</code> may not be visible to that context without
     *       additional steps being taken by the application. For an OpenGL 3.1 (or later) context, the
     *       requirements are described in Appendix G ("Shared Objects and Multiple Contexts") of the
     *       OpenGL 3.1 Specification. For prior versions of OpenGL, the requirements are implementationdependent.
     *     </p>
     *     <p>
     *       Attempting to access the data store of an OpenGL object after it has been acquired by OpenCL
     *       and before it has been released will result in undefined behavior. Similarly, attempting to access
     *       a shared CL/GL object from OpenCL before it has been acquired by the OpenCL command
     *       queue, or after it has been released, will result in undefined behavior.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       Returns <span>CL_SUCCESS</span> if the function is executed successfully. If
     *       <code>num_objects</code> is 0 and <code>mem_objects</code> is NULL
     *       the function does nothing and returns <span>CL_SUCCESS</span>.
     *       Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>num_objects</code> is zero and
     *           <code>mem_objects</code> is not a NULL value or if <code>num_objects</code> &gt; 0
     *           and <code>mem_objects</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if memory objects in <code>mem_objects</code>
     *           are not valid OpenCL memory objects.
     *         </li>
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code> is not a
     *           valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if context associated with <code>command_queue</code>
     *           was not created from an OpenGL context.
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if memory objects in <code>mem_objects</code>
     *           have not been created from a GL object(s).
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code>
     *           is NULL and <code>num_events_in_wait_list</code> &gt; 0, or
     *           <code>event_wait_list</code> is not NULL and
     *           <code>num_events_in_wait_list</code> is 0, or if event objects in
     *           <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueAcquireGLObjects(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueAcquireGLObjectsNative(command_queue, num_objects, mem_objects, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueAcquireGLObjectsNative(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);

    /**
     * <p>
     *       Release OpenCL memory objects that have been created from OpenGL objects.
     *   </p>
     *
     * <div title="clEnqueueReleaseGLObjects">
     *   <div>
     *     <h2></h2>
     *     <div>
     *       <table border="0" summary="Function synopsis" cellspacing="0" cellpadding="0">
     *         <tr valign="bottom">
     *           <td>
     *             <code>cl_int
     *             <b>
     *             clEnqueueReleaseGLObjects
     *             </b>
     *             (</code>
     *           <td>cl_command_queue <var>command_queue</var>, </td>
     *           </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_objects</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_mem <var>*mem_objects</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_uint <var>num_events_in_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>const cl_event <var>*event_wait_list</var>, </td>
     *         </tr>
     *         <tr valign="top">
     *           <td> </td>
     *           <td>cl_event <var>*event</var><code>)</code></td>
     *         </tr>
     *       </table>
     *     </div>
     *   </div>
     *   <div title="Parameters">
     *     <h2>Parameters</h2>
     *     <div>
     *       <dl>
     *         <dt>
     *           <span>
     *           <code>
     *           command_queue
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A valid command-queue.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           num_objects
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             The number of memory objects to be released in <code>mem_objects</code>.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           mem_objects
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             A pointer to a list of CL memory objects that correspond to GL objects.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event_wait_list
     *           </code>
     *           , </span>
     *           <span>
     *           <code>
     *           num_events_in_wait_list
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             These parameters specify events that need to complete before this
     *             command can be executed. If <code>event_wait_list</code> is NULL, then this particular command
     *             does not wait on any event to complete. If <code>event_wait_list</code> is NULL,
     *             <code>num_events_in_wait_list</code>
     *             must be 0. If <code>event_wait_list</code> is not NULL,
     *             the list of events pointed to by <code>event_wait_list</code> must
     *             be valid and <code>num_events_in_wait_list</code> must be greater than 0.
     *             The events specified in
     *             <code>event_wait_list</code> act as synchronization points.
     *           </p>
     *         </dd>
     *         <dt>
     *           <span>
     *           <code>
     *           event
     *           </code>
     *           </span>
     *         </dt>
     *         <dd>
     *           <p>
     *             Returns an event object that identifies this particular read/write
     *             command and can be used to query or queue a wait for the command to complete.
     *             <code>event</code> can be NULL in which case it will not be possible
     *             for the application to query the status of this command or queue a wait
     *             for this command to complete.
     *           </p>
     *           <p>
     *             When the <span><span>cl_khr_gl_event</span></span>
     *             extension is enabled,
     *             If an OpenGL context is bound to the current thread, then any OpenGL commands which:
     *           </p>
     *           <div>
     *             <ul type="disc">
     *               <li>
     *                 affect or access the contents of the memory objects listed in the <code>mem_objects</code> list, and
     *               </li>
     *               <li>
     *                 are issued on that context prior to the call to
     *                 <span><span>clEnqueueReleaseGLObjects</span></span>
     *               </li>
     *             </ul>
     *           </div>
     *           <p>
     *             will not execute until after execution of any OpenCL commands preceding the
     *             <span><span>clEnqueueReleaseGLObjects</span></span>
     *             which affect or access any of those memory objects. If a non-NULL <code>event</code> object is
     *             returned, it will report completion before execution of such OpenGL commands.
     *           </p>
     *         </dd>
     *       </dl>
     *     </div>
     *   </div>
     *   <div title="Description">
     *     <h2>Description</h2>
     *     <p>
     *       These objects need to be released before they can be used by OpenGL.
     *       The OpenGL objects are released by the OpenCL context associated with <code>command_queue</code>.
     *     </p>
     *   </div>
     *   <div title="Notes">
     *     <h2>Notes</h2>
     *     <p>
     *       In order to ensure data integrity, the application is responsible for synchronizing access to shared
     *       CL/GL objects by their respective APIs. Failure to provide such synchronization may result in
     *       race conditions and other undefined behavior including non-portability between
     *       implementations.
     *     </p>
     *     <p>
     *       Prior to calling <code>clEnqueueAcquireGLObjects</code>, the application
     *       must ensure that any pending GL operations which access the objects specified in
     *       <code>mem_objects</code> have completed. This may be accomplished portably
     *       by issuing and waiting for completion of a
     *       <span><span>glFinish</span></span> command on
     *       all GL contexts with pending references to these objects. Implementations may
     *       offer more efficient synchronization methods; for example on some platforms
     *       calling <span><span>glFlush</span></span>
     *       may be sufficient, or synchronization may be implicit within a thread, or
     *       there may be vendor-specific extensions that enable placing a fence in the
     *       GL command stream and waiting for completion of that fence in the
     *       CL command queue. Note that no synchronization methods other than
     *       <span><span>glFinish</span></span> are
     *       portable between OpenGL implementations at this time.
     *     </p>
     *     <p>
     *       Similarly, after calling <code>clEnqueueReleaseGLObjects</code>,
     *       the application is responsible for ensuring that any pending OpenCL operations
     *       which access the objects specified in <code>mem_objects</code> have
     *       completed prior to executing subsequent GL commands which reference these objects. This
     *       may be accomplished portably by calling <span><span>clWaitForEvents</span></span> with the event object returned by
     *       <code>clEnqueueReleaseGLObjects</code>, or by calling <span><span>glFinish</span></span>. As above, some implementations may
     *       offer more efficient methods.
     *     </p>
     *     <p>
     *       The application is responsible for maintaining the proper order of operations if the CL and GL
     *       contexts are in separate threads.
     *     </p>
     *     <p>
     *       If a GL context is bound to a thread other than the one in which <code>clEnqueueReleaseGLObjects</code>
     *       is called, changes to any of the objects in <code>mem_objects</code> may not be visible to that context without
     *       additional steps being taken by the application. For an OpenGL 3.1 (or later) context, the
     *       requirements are described in Appendix G ("Shared Objects and Multiple Contexts") of the
     *       OpenGL 3.1 Specification. For prior versions of OpenGL, the requirements are implementationdependent.
     *     </p>
     *     <p>
     *       Attempting to access the data store of an OpenGL object after it has been acquired by OpenCL
     *       and before it has been released will result in undefined behavior. Similarly, attempting to access
     *       a shared CL/GL object from OpenCL before it has been acquired by the OpenCL command
     *       queue, or after it has been released, will result in undefined behavior.
     *     </p>
     *   </div>
     *   <div title="Errors">
     *     <h2>Errors</h2>
     *     <p>
     *       <code>clEnqueueReleaseGLObjects</code> returns <span>CL_SUCCESS</span> if the function is executed successfully. If <code>num_objects</code> is 0 and <code>mem_objects</code> is NULL the function does nothing and returns <span>CL_SUCCESS</span>. Otherwise, it returns one of the following errors:
     *     </p>
     *     <div>
     *       <ul type="disc">
     *         <li><span>CL_INVALID_VALUE</span> if <code>num_objects</code> is zero
     *           and <code>mem_objects</code> is not a NULL value or if
     *           <code>num_objects</code> &gt; 0 and <code>mem_objects</code> is NULL.
     *         </li>
     *         <li><span>CL_INVALID_MEM_OBJECT</span> if memory objects in
     *           <code>mem_objects</code> are not valid OpenCL memory objects.
     *         </li>
     *         <li><span>CL_INVALID_COMMAND_QUEUE</span> if <code>command_queue</code>
     *           is not a valid command-queue.
     *         </li>
     *         <li><span>CL_INVALID_CONTEXT</span> if context associated with
     *           <code>command_queue</code> was not created from an OpenGL context.
     *         </li>
     *         <li><span>CL_INVALID_GL_OBJECT</span> if memory objects in <code>mem_objects</code>
     *           have not been created from a GL object(s).
     *         </li>
     *         <li><span>CL_INVALID_EVENT_WAIT_LIST</span> if <code>event_wait_list</code> is
     *           NULL and <code>num_events_in_wait_list</code> &gt; 0, or <code>event_wait_list</code>
     *           is not NULL and <code>num_events_in_wait_list</code> is 0, or if event objects in
     *           <code>event_wait_list</code> are not valid events.
     *         </li>
     *         <li><span>CL_OUT_OF_RESOURCES</span> if there is a failure to allocate resources
     *           required by the OpenCL implementation on the device.
     *         </li>
     *         <li><span>CL_OUT_OF_HOST_MEMORY</span> if there is a failure to allocate resources required
     *           by the OpenCL implementation on the host.
     *         </li>
     *       </ul>
     *     </div>
     *   </div>
     * </div>
     */
    public static int clEnqueueReleaseGLObjects(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event)
    {
        return checkResult(clEnqueueReleaseGLObjectsNative(command_queue, num_objects, mem_objects, num_events_in_wait_list, event_wait_list, event));
    }

    private static native int clEnqueueReleaseGLObjectsNative(cl_command_queue command_queue, int num_objects, cl_mem mem_objects[], int num_events_in_wait_list, cl_event event_wait_list[], cl_event event);



    /**
     * Can be used to query information about a CL/GL context.
     *
     * TODO: Add documentation
     */
    /*
    public static int clGetGLContextInfoKHR(cl_context_properties properties, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[])
    {
        return checkResult(clGetGLContextInfoKHRNative(properties, param_name, param_value_size, param_value, param_value_size_ret));
    }

    private static native int clGetGLContextInfoKHRNative(cl_context_properties properties, int param_name, long param_value_size, Pointer param_value, long param_value_size_ret[]);
    */

    /**
     * Private constructor to prevent instantiation
     */
    private CL()
    {}


    /*
     * XXX GL_INTEROPERABILITY - This function is ONLY for testing!
     */
    /*
    public static void initGLSharedContextProperties(cl_context_properties properties)
    {
        long propertiesArray[] = createGLSharedContextPropertiesArrayNative();
        for (int i=0; i<propertiesArray.length-1; i+=2)
        {
            properties.addProperty(propertiesArray[i+0], propertiesArray[i+1]);
        }
    }
    private static native long[] createGLSharedContextPropertiesArrayNative();
    */
}

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

// These typedefs for the types of pointers to CL functions are extracted 
// from the cl.h header file. The original header of this file:

/*******************************************************************************
 * Copyright (c) 2011 The Khronos Group Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and/or associated documentation files (the
 * "Materials"), to deal in the Materials without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Materials, and to
 * permit persons to whom the Materials are furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Materials.
 *
 * THE MATERIALS ARE PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * MATERIALS OR THE USE OR OTHER DEALINGS IN THE MATERIALS.
 ******************************************************************************/

#ifndef CL_FUNCTIONS_HPP
#define CL_FUNCTIONS_HPP

#include "JOCLCommon.hpp"

/* Platform API */
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetPlatformIDsFunctionPointerType)(cl_uint          /* num_entries */,
                 cl_platform_id * /* platforms */,
                 cl_uint *        /* num_platforms */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetPlatformInfoFunctionPointerType)(cl_platform_id   /* platform */,
                  cl_platform_info /* param_name */,
                  size_t           /* param_value_size */,
                  void *           /* param_value */,
                  size_t *         /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Device APIs */
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetDeviceIDsFunctionPointerType)(cl_platform_id   /* platform */,
               cl_device_type   /* device_type */,
               cl_uint          /* num_entries */,
               cl_device_id *   /* devices */,
               cl_uint *        /* num_devices */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetDeviceInfoFunctionPointerType)(cl_device_id    /* device */,
                cl_device_info  /* param_name */,
                size_t          /* param_value_size */,
                void *          /* param_value */,
                size_t *        /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clCreateSubDevicesFunctionPointerType)(cl_device_id                         /* in_device */,
                   const cl_device_partition_property * /* properties */,
                   cl_uint                              /* num_devices */,
                   cl_device_id *                       /* out_devices */,
                   cl_uint *                            /* num_devices_ret */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainDeviceFunctionPointerType)(cl_device_id /* device */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseDeviceFunctionPointerType)(cl_device_id /* device */) CL_API_SUFFIX__VERSION_1_2;

/* Context APIs  */
typedef CL_API_ENTRY cl_context (CL_API_CALL
*clCreateContextFunctionPointerType)(const cl_context_properties * /* properties */,
                cl_uint                 /* num_devices */,
                const cl_device_id *    /* devices */,
                void (CL_CALLBACK * /* pfn_notify */)(const char *, const void *, size_t, void *),
                void *                  /* user_data */,
                cl_int *                /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_context (CL_API_CALL
*clCreateContextFromTypeFunctionPointerType)(const cl_context_properties * /* properties */,
                        cl_device_type          /* device_type */,
                        void (CL_CALLBACK *     /* pfn_notify*/ )(const char *, const void *, size_t, void *),
                        void *                  /* user_data */,
                        cl_int *                /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainContextFunctionPointerType)(cl_context /* context */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseContextFunctionPointerType)(cl_context /* context */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetContextInfoFunctionPointerType)(cl_context         /* context */,
                 cl_context_info    /* param_name */,
                 size_t             /* param_value_size */,
                 void *             /* param_value */,
                 size_t *           /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Command Queue APIs */
typedef CL_API_ENTRY cl_command_queue (CL_API_CALL
*clCreateCommandQueueFunctionPointerType)(cl_context                     /* context */,
                     cl_device_id                   /* device */,
                     cl_command_queue_properties    /* properties */,
                     cl_int *                       /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainCommandQueueFunctionPointerType)(cl_command_queue /* command_queue */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseCommandQueueFunctionPointerType)(cl_command_queue /* command_queue */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetCommandQueueInfoFunctionPointerType)(cl_command_queue      /* command_queue */,
                      cl_command_queue_info /* param_name */,
                      size_t                /* param_value_size */,
                      void *                /* param_value */,
                      size_t *              /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Memory Object APIs */
typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateBufferFunctionPointerType)(cl_context   /* context */,
               cl_mem_flags /* flags */,
               size_t       /* size */,
               void *       /* host_ptr */,
               cl_int *     /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateSubBufferFunctionPointerType)(cl_mem                   /* buffer */,
                  cl_mem_flags             /* flags */,
                  cl_buffer_create_type    /* buffer_create_type */,
                  const void *             /* buffer_create_info */,
                  cl_int *                 /* errcode_ret */) CL_API_SUFFIX__VERSION_1_1;

typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateImageFunctionPointerType)(cl_context              /* context */,
              cl_mem_flags            /* flags */,
              const cl_image_format * /* image_format */,
              const cl_image_desc *   /* image_desc */,
              void *                  /* host_ptr */,
              cl_int *                /* errcode_ret */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainMemObjectFunctionPointerType)(cl_mem /* memobj */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseMemObjectFunctionPointerType)(cl_mem /* memobj */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetSupportedImageFormatsFunctionPointerType)(cl_context           /* context */,
                           cl_mem_flags         /* flags */,
                           cl_mem_object_type   /* image_type */,
                           cl_uint              /* num_entries */,
                           cl_image_format *    /* image_formats */,
                           cl_uint *            /* num_image_formats */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetMemObjectInfoFunctionPointerType)(cl_mem           /* memobj */,
                   cl_mem_info      /* param_name */,
                   size_t           /* param_value_size */,
                   void *           /* param_value */,
                   size_t *         /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetImageInfoFunctionPointerType)(cl_mem           /* image */,
               cl_image_info    /* param_name */,
               size_t           /* param_value_size */,
               void *           /* param_value */,
               size_t *         /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clSetMemObjectDestructorCallbackFunctionPointerType)(  cl_mem /* memobj */,
                                    void (CL_CALLBACK * /*pfn_notify*/)( cl_mem /* memobj */, void* /*user_data*/),
                                    void * /*user_data */ )             CL_API_SUFFIX__VERSION_1_1;

/* Sampler APIs */
typedef CL_API_ENTRY cl_sampler (CL_API_CALL
*clCreateSamplerFunctionPointerType)(cl_context          /* context */,
                cl_bool             /* normalized_coords */,
                cl_addressing_mode  /* addressing_mode */,
                cl_filter_mode      /* filter_mode */,
                cl_int *            /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainSamplerFunctionPointerType)(cl_sampler /* sampler */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseSamplerFunctionPointerType)(cl_sampler /* sampler */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetSamplerInfoFunctionPointerType)(cl_sampler         /* sampler */,
                 cl_sampler_info    /* param_name */,
                 size_t             /* param_value_size */,
                 void *             /* param_value */,
                 size_t *           /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Program Object APIs  */
typedef CL_API_ENTRY cl_program (CL_API_CALL
*clCreateProgramWithSourceFunctionPointerType)(cl_context        /* context */,
                          cl_uint           /* count */,
                          const char **     /* strings */,
                          const size_t *    /* lengths */,
                          cl_int *          /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_program (CL_API_CALL
*clCreateProgramWithBinaryFunctionPointerType)(cl_context                     /* context */,
                          cl_uint                        /* num_devices */,
                          const cl_device_id *           /* device_list */,
                          const size_t *                 /* lengths */,
                          const unsigned char **         /* binaries */,
                          cl_int *                       /* binary_status */,
                          cl_int *                       /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_program (CL_API_CALL
*clCreateProgramWithBuiltInKernelsFunctionPointerType)(cl_context            /* context */,
                                  cl_uint               /* num_devices */,
                                  const cl_device_id *  /* device_list */,
                                  const char *          /* kernel_names */,
                                  cl_int *              /* errcode_ret */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainProgramFunctionPointerType)(cl_program /* program */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseProgramFunctionPointerType)(cl_program /* program */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clBuildProgramFunctionPointerType)(cl_program           /* program */,
               cl_uint              /* num_devices */,
               const cl_device_id * /* device_list */,
               const char *         /* options */,
               void (CL_CALLBACK *  /* pfn_notify */)(cl_program /* program */, void * /* user_data */),
               void *               /* user_data */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clCompileProgramFunctionPointerType)(cl_program           /* program */,
                 cl_uint              /* num_devices */,
                 const cl_device_id * /* device_list */,
                 const char *         /* options */,
                 cl_uint              /* num_input_headers */,
                 const cl_program *   /* input_headers */,
                 const char **        /* header_include_names */,
                 void (CL_CALLBACK *  /* pfn_notify */)(cl_program /* program */, void * /* user_data */),
                 void *               /* user_data */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_program (CL_API_CALL
*clLinkProgramFunctionPointerType)(cl_context           /* context */,
              cl_uint              /* num_devices */,
              const cl_device_id * /* device_list */,
              const char *         /* options */,
              cl_uint              /* num_input_programs */,
              const cl_program *   /* input_programs */,
              void (CL_CALLBACK *  /* pfn_notify */)(cl_program /* program */, void * /* user_data */),
              void *               /* user_data */,
              cl_int *             /* errcode_ret */ ) CL_API_SUFFIX__VERSION_1_2;


typedef CL_API_ENTRY cl_int (CL_API_CALL
*clUnloadPlatformCompilerFunctionPointerType)(cl_platform_id /* platform */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetProgramInfoFunctionPointerType)(cl_program         /* program */,
                 cl_program_info    /* param_name */,
                 size_t             /* param_value_size */,
                 void *             /* param_value */,
                 size_t *           /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetProgramBuildInfoFunctionPointerType)(cl_program            /* program */,
                      cl_device_id          /* device */,
                      cl_program_build_info /* param_name */,
                      size_t                /* param_value_size */,
                      void *                /* param_value */,
                      size_t *              /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Kernel Object APIs */
typedef CL_API_ENTRY cl_kernel (CL_API_CALL
*clCreateKernelFunctionPointerType)(cl_program      /* program */,
               const char *    /* kernel_name */,
               cl_int *        /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clCreateKernelsInProgramFunctionPointerType)(cl_program     /* program */,
                         cl_uint        /* num_kernels */,
                         cl_kernel *    /* kernels */,
                         cl_uint *      /* num_kernels_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainKernelFunctionPointerType)(cl_kernel    /* kernel */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseKernelFunctionPointerType)(cl_kernel   /* kernel */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clSetKernelArgFunctionPointerType)(cl_kernel    /* kernel */,
               cl_uint      /* arg_index */,
               size_t       /* arg_size */,
               const void * /* arg_value */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetKernelInfoFunctionPointerType)(cl_kernel       /* kernel */,
                cl_kernel_info  /* param_name */,
                size_t          /* param_value_size */,
                void *          /* param_value */,
                size_t *        /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetKernelArgInfoFunctionPointerType)(cl_kernel       /* kernel */,
                   cl_uint         /* arg_indx */,
                   cl_kernel_arg_info  /* param_name */,
                   size_t          /* param_value_size */,
                   void *          /* param_value */,
                   size_t *        /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetKernelWorkGroupInfoFunctionPointerType)(cl_kernel                  /* kernel */,
                         cl_device_id               /* device */,
                         cl_kernel_work_group_info  /* param_name */,
                         size_t                     /* param_value_size */,
                         void *                     /* param_value */,
                         size_t *                   /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Event Object APIs */
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clWaitForEventsFunctionPointerType)(cl_uint             /* num_events */,
                const cl_event *    /* event_list */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetEventInfoFunctionPointerType)(cl_event         /* event */,
               cl_event_info    /* param_name */,
               size_t           /* param_value_size */,
               void *           /* param_value */,
               size_t *         /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_event (CL_API_CALL
*clCreateUserEventFunctionPointerType)(cl_context    /* context */,
                  cl_int *      /* errcode_ret */) CL_API_SUFFIX__VERSION_1_1;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clRetainEventFunctionPointerType)(cl_event /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clReleaseEventFunctionPointerType)(cl_event /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clSetUserEventStatusFunctionPointerType)(cl_event   /* event */,
                     cl_int     /* execution_status */) CL_API_SUFFIX__VERSION_1_1;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clSetEventCallbackFunctionPointerType)( cl_event    /* event */,
                    cl_int      /* command_exec_callback_type */,
                    void (CL_CALLBACK * /* pfn_notify */)(cl_event, cl_int, void *),
                    void *      /* user_data */) CL_API_SUFFIX__VERSION_1_1;

/* Profiling APIs */
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetEventProfilingInfoFunctionPointerType)(cl_event            /* event */,
                        cl_profiling_info   /* param_name */,
                        size_t              /* param_value_size */,
                        void *              /* param_value */,
                        size_t *            /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Flush and Finish APIs */
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clFlushFunctionPointerType)(cl_command_queue /* command_queue */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clFinishFunctionPointerType)(cl_command_queue /* command_queue */) CL_API_SUFFIX__VERSION_1_0;

/* Enqueued Commands APIs */
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueReadBufferFunctionPointerType)(cl_command_queue    /* command_queue */,
                    cl_mem              /* buffer */,
                    cl_bool             /* blocking_read */,
                    size_t              /* offset */,
                    size_t              /* size */,
                    void *              /* ptr */,
                    cl_uint             /* num_events_in_wait_list */,
                    const cl_event *    /* event_wait_list */,
                    cl_event *          /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueReadBufferRectFunctionPointerType)(cl_command_queue    /* command_queue */,
                        cl_mem              /* buffer */,
                        cl_bool             /* blocking_read */,
                        const size_t *      /* buffer_offset */,
                        const size_t *      /* host_offset */,
                        const size_t *      /* region */,
                        size_t              /* buffer_row_pitch */,
                        size_t              /* buffer_slice_pitch */,
                        size_t              /* host_row_pitch */,
                        size_t              /* host_slice_pitch */,
                        void *              /* ptr */,
                        cl_uint             /* num_events_in_wait_list */,
                        const cl_event *    /* event_wait_list */,
                        cl_event *          /* event */) CL_API_SUFFIX__VERSION_1_1;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueWriteBufferFunctionPointerType)(cl_command_queue   /* command_queue */,
                     cl_mem             /* buffer */,
                     cl_bool            /* blocking_write */,
                     size_t             /* offset */,
                     size_t             /* size */,
                     const void *       /* ptr */,
                     cl_uint            /* num_events_in_wait_list */,
                     const cl_event *   /* event_wait_list */,
                     cl_event *         /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueWriteBufferRectFunctionPointerType)(cl_command_queue    /* command_queue */,
                         cl_mem              /* buffer */,
                         cl_bool             /* blocking_write */,
                         const size_t *      /* buffer_offset */,
                         const size_t *      /* host_offset */,
                         const size_t *      /* region */,
                         size_t              /* buffer_row_pitch */,
                         size_t              /* buffer_slice_pitch */,
                         size_t              /* host_row_pitch */,
                         size_t              /* host_slice_pitch */,
                         const void *        /* ptr */,
                         cl_uint             /* num_events_in_wait_list */,
                         const cl_event *    /* event_wait_list */,
                         cl_event *          /* event */) CL_API_SUFFIX__VERSION_1_1;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueFillBufferFunctionPointerType)(cl_command_queue   /* command_queue */,
                    cl_mem             /* buffer */,
                    const void *       /* pattern */,
                    size_t             /* pattern_size */,
                    size_t             /* offset */,
                    size_t             /* size */,
                    cl_uint            /* num_events_in_wait_list */,
                    const cl_event *   /* event_wait_list */,
                    cl_event *         /* event */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueCopyBufferFunctionPointerType)(cl_command_queue    /* command_queue */,
                    cl_mem              /* src_buffer */,
                    cl_mem              /* dst_buffer */,
                    size_t              /* src_offset */,
                    size_t              /* dst_offset */,
                    size_t              /* size */,
                    cl_uint             /* num_events_in_wait_list */,
                    const cl_event *    /* event_wait_list */,
                    cl_event *          /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueCopyBufferRectFunctionPointerType)(cl_command_queue    /* command_queue */,
                        cl_mem              /* src_buffer */,
                        cl_mem              /* dst_buffer */,
                        const size_t *      /* src_origin */,
                        const size_t *      /* dst_origin */,
                        const size_t *      /* region */,
                        size_t              /* src_row_pitch */,
                        size_t              /* src_slice_pitch */,
                        size_t              /* dst_row_pitch */,
                        size_t              /* dst_slice_pitch */,
                        cl_uint             /* num_events_in_wait_list */,
                        const cl_event *    /* event_wait_list */,
                        cl_event *          /* event */) CL_API_SUFFIX__VERSION_1_1;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueReadImageFunctionPointerType)(cl_command_queue     /* command_queue */,
                   cl_mem               /* image */,
                   cl_bool              /* blocking_read */,
                   const size_t *       /* origin[3] */,
                   const size_t *       /* region[3] */,
                   size_t               /* row_pitch */,
                   size_t               /* slice_pitch */,
                   void *               /* ptr */,
                   cl_uint              /* num_events_in_wait_list */,
                   const cl_event *     /* event_wait_list */,
                   cl_event *           /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueWriteImageFunctionPointerType)(cl_command_queue    /* command_queue */,
                    cl_mem              /* image */,
                    cl_bool             /* blocking_write */,
                    const size_t *      /* origin[3] */,
                    const size_t *      /* region[3] */,
                    size_t              /* input_row_pitch */,
                    size_t              /* input_slice_pitch */,
                    const void *        /* ptr */,
                    cl_uint             /* num_events_in_wait_list */,
                    const cl_event *    /* event_wait_list */,
                    cl_event *          /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueFillImageFunctionPointerType)(cl_command_queue   /* command_queue */,
                   cl_mem             /* image */,
                   const void *       /* fill_color */,
                   const size_t *     /* origin[3] */,
                   const size_t *     /* region[3] */,
                   cl_uint            /* num_events_in_wait_list */,
                   const cl_event *   /* event_wait_list */,
                   cl_event *         /* event */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueCopyImageFunctionPointerType)(cl_command_queue     /* command_queue */,
                   cl_mem               /* src_image */,
                   cl_mem               /* dst_image */,
                   const size_t *       /* src_origin[3] */,
                   const size_t *       /* dst_origin[3] */,
                   const size_t *       /* region[3] */,
                   cl_uint              /* num_events_in_wait_list */,
                   const cl_event *     /* event_wait_list */,
                   cl_event *           /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueCopyImageToBufferFunctionPointerType)(cl_command_queue /* command_queue */,
                           cl_mem           /* src_image */,
                           cl_mem           /* dst_buffer */,
                           const size_t *   /* src_origin[3] */,
                           const size_t *   /* region[3] */,
                           size_t           /* dst_offset */,
                           cl_uint          /* num_events_in_wait_list */,
                           const cl_event * /* event_wait_list */,
                           cl_event *       /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueCopyBufferToImageFunctionPointerType)(cl_command_queue /* command_queue */,
                           cl_mem           /* src_buffer */,
                           cl_mem           /* dst_image */,
                           size_t           /* src_offset */,
                           const size_t *   /* dst_origin[3] */,
                           const size_t *   /* region[3] */,
                           cl_uint          /* num_events_in_wait_list */,
                           const cl_event * /* event_wait_list */,
                           cl_event *       /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY void * (CL_API_CALL
*clEnqueueMapBufferFunctionPointerType)(cl_command_queue /* command_queue */,
                   cl_mem           /* buffer */,
                   cl_bool          /* blocking_map */,
                   cl_map_flags     /* map_flags */,
                   size_t           /* offset */,
                   size_t           /* size */,
                   cl_uint          /* num_events_in_wait_list */,
                   const cl_event * /* event_wait_list */,
                   cl_event *       /* event */,
                   cl_int *         /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY void * (CL_API_CALL
*clEnqueueMapImageFunctionPointerType)(cl_command_queue  /* command_queue */,
                  cl_mem            /* image */,
                  cl_bool           /* blocking_map */,
                  cl_map_flags      /* map_flags */,
                  const size_t *    /* origin[3] */,
                  const size_t *    /* region[3] */,
                  size_t *          /* image_row_pitch */,
                  size_t *          /* image_slice_pitch */,
                  cl_uint           /* num_events_in_wait_list */,
                  const cl_event *  /* event_wait_list */,
                  cl_event *        /* event */,
                  cl_int *          /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueUnmapMemObjectFunctionPointerType)(cl_command_queue /* command_queue */,
                        cl_mem           /* memobj */,
                        void *           /* mapped_ptr */,
                        cl_uint          /* num_events_in_wait_list */,
                        const cl_event *  /* event_wait_list */,
                        cl_event *        /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueMigrateMemObjectsFunctionPointerType)(cl_command_queue       /* command_queue */,
                           cl_uint                /* num_mem_objects */,
                           const cl_mem *         /* mem_objects */,
                           cl_mem_migration_flags /* flags */,
                           cl_uint                /* num_events_in_wait_list */,
                           const cl_event *       /* event_wait_list */,
                           cl_event *             /* event */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueNDRangeKernelFunctionPointerType)(cl_command_queue /* command_queue */,
                       cl_kernel        /* kernel */,
                       cl_uint          /* work_dim */,
                       const size_t *   /* global_work_offset */,
                       const size_t *   /* global_work_size */,
                       const size_t *   /* local_work_size */,
                       cl_uint          /* num_events_in_wait_list */,
                       const cl_event * /* event_wait_list */,
                       cl_event *       /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueTaskFunctionPointerType)(cl_command_queue  /* command_queue */,
              cl_kernel         /* kernel */,
              cl_uint           /* num_events_in_wait_list */,
              const cl_event *  /* event_wait_list */,
              cl_event *        /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueNativeKernelFunctionPointerType)(cl_command_queue  /* command_queue */,
                      void (CL_CALLBACK * /*user_func*/)(void *),
                      void *            /* args */,
                      size_t            /* cb_args */,
                      cl_uint           /* num_mem_objects */,
                      const cl_mem *    /* mem_list */,
                      const void **     /* args_mem_loc */,
                      cl_uint           /* num_events_in_wait_list */,
                      const cl_event *  /* event_wait_list */,
                      cl_event *        /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueMarkerWithWaitListFunctionPointerType)(cl_command_queue /* command_queue */,
                            cl_uint           /* num_events_in_wait_list */,
                            const cl_event *  /* event_wait_list */,
                            cl_event *        /* event */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueBarrierWithWaitListFunctionPointerType)(cl_command_queue /* command_queue */,
                             cl_uint           /* num_events_in_wait_list */,
                             const cl_event *  /* event_wait_list */,
                             cl_event *        /* event */) CL_API_SUFFIX__VERSION_1_2;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clSetPrintfCallbackFunctionPointerType)(cl_context          /* context */,
                    void (CL_CALLBACK * /* pfn_notify */)(cl_context /* program */,
                                                          cl_uint /*printf_data_len */,
                                                          char * /* printf_data_ptr */,
                                                          void * /* user_data */),
                    void *              /* user_data */) CL_API_SUFFIX__VERSION_1_2;



/* Extension function access
 *
 * Returns the extension function address for the given function name,
 * or NULL if a valid function can not be found.  The client must
 * check to make sure the address is not NULL, before using or
 * calling the returned function address.
 */
typedef CL_API_ENTRY void * (CL_API_CALL
*clGetExtensionFunctionAddressForPlatformFunctionPointerType)(cl_platform_id /* platform */,
                                         const char *   /* func_name */) CL_API_SUFFIX__VERSION_1_2;


//#warning CL_USE_DEPRECATED_OPENCL_1_0_APIS is defined. These APIs are unsupported and untested in OpenCL 1.1!
/*
 *  WARNING:
 *     This API introduces mutable state into the OpenCL implementation. It has been REMOVED
 *  to better facilitate thread safety.  The 1.0 API is not thread safe. It is not tested by the
 *  OpenCL 1.1 conformance test, and consequently may not work or may not work dependably.
 *  It is likely to be non-performant. Use of this API is not advised. Use at your own risk.
 *
 *  Software developers previously relying on this API are instructed to set the command queue
 *  properties when creating the queue, instead.
 */
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clSetCommandQueuePropertyFunctionPointerType)(cl_command_queue              /* command_queue */,
                          cl_command_queue_properties   /* properties */,
                          cl_bool                        /* enable */,
                          cl_command_queue_properties * /* old_properties */) CL_EXT_SUFFIX__VERSION_1_0_DEPRECATED;


typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateImage2DFunctionPointerType)(cl_context              /* context */,
                cl_mem_flags            /* flags */,
                const cl_image_format * /* image_format */,
                size_t                  /* image_width */,
                size_t                  /* image_height */,
                size_t                  /* image_row_pitch */,
                void *                  /* host_ptr */,
                cl_int *                /* errcode_ret */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;

typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateImage3DFunctionPointerType)(cl_context              /* context */,
                cl_mem_flags            /* flags */,
                const cl_image_format * /* image_format */,
                size_t                  /* image_width */,
                size_t                  /* image_height */,
                size_t                  /* image_depth */,
                size_t                  /* image_row_pitch */,
                size_t                  /* image_slice_pitch */,
                void *                  /* host_ptr */,
                cl_int *                /* errcode_ret */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueMarkerFunctionPointerType)(cl_command_queue    /* command_queue */,
                cl_event *          /* event */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueWaitForEventsFunctionPointerType)(cl_command_queue /* command_queue */,
                       cl_uint          /* num_events */,
                       const cl_event * /* event_list */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueBarrierFunctionPointerType)(cl_command_queue /* command_queue */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clUnloadCompilerFunctionPointerType)(void) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;

typedef CL_API_ENTRY void * (CL_API_CALL
*clGetExtensionFunctionAddressFunctionPointerType)(const char * /* func_name */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;








// GL:







typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateFromGLBufferFunctionPointerType)(cl_context     /* context */,
                     cl_mem_flags   /* flags */,
                     cl_GLuint      /* bufobj */,
                     int *          /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateFromGLTextureFunctionPointerType)(cl_context      /* context */,
                      cl_mem_flags    /* flags */,
                      cl_GLenum       /* target */,
                      cl_GLint        /* miplevel */,
                      cl_GLuint       /* texture */,
                      cl_int *        /* errcode_ret */) CL_API_SUFFIX__VERSION_1_2;
    
typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateFromGLRenderbufferFunctionPointerType)(cl_context   /* context */,
                           cl_mem_flags /* flags */,
                           cl_GLuint    /* renderbuffer */,
                           cl_int *     /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetGLObjectInfoFunctionPointerType)(cl_mem                /* memobj */,
                  cl_gl_object_type *   /* gl_object_type */,
                  cl_GLuint *           /* gl_object_name */) CL_API_SUFFIX__VERSION_1_0;
                  
typedef CL_API_ENTRY cl_int (CL_API_CALL
*clGetGLTextureInfoFunctionPointerType)(cl_mem               /* memobj */,
                   cl_gl_texture_info   /* param_name */,
                   size_t               /* param_value_size */,
                   void *               /* param_value */,
                   size_t *             /* param_value_size_ret */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueAcquireGLObjectsFunctionPointerType)(cl_command_queue      /* command_queue */,
                          cl_uint               /* num_objects */,
                          const cl_mem *        /* mem_objects */,
                          cl_uint               /* num_events_in_wait_list */,
                          const cl_event *      /* event_wait_list */,
                          cl_event *            /* event */) CL_API_SUFFIX__VERSION_1_0;

typedef CL_API_ENTRY cl_int (CL_API_CALL
*clEnqueueReleaseGLObjectsFunctionPointerType)(cl_command_queue      /* command_queue */,
                          cl_uint               /* num_objects */,
                          const cl_mem *        /* mem_objects */,
                          cl_uint               /* num_events_in_wait_list */,
                          const cl_event *      /* event_wait_list */,
                          cl_event *            /* event */) CL_API_SUFFIX__VERSION_1_0;


//#warning CL_USE_DEPRECATED_OPENCL_1_1_APIS is defined. These APIs are unsupported and untested in OpenCL 1.2!
typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateFromGLTexture2DFunctionPointerType)(cl_context      /* context */,
                        cl_mem_flags    /* flags */,
                        cl_GLenum       /* target */,
                        cl_GLint        /* miplevel */,
                        cl_GLuint       /* texture */,
                        cl_int *        /* errcode_ret */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;

typedef CL_API_ENTRY cl_mem (CL_API_CALL
*clCreateFromGLTexture3DFunctionPointerType)(cl_context      /* context */,
                        cl_mem_flags    /* flags */,
                        cl_GLenum       /* target */,
                        cl_GLint        /* miplevel */,
                        cl_GLuint       /* texture */,
                        cl_int *        /* errcode_ret */) CL_EXT_SUFFIX__VERSION_1_1_DEPRECATED;



// The definitions of the function pointers, which are contained
// in the CLFunctions.cpp file:

extern clGetPlatformIDsFunctionPointerType clGetPlatformIDsFP;
extern clGetPlatformInfoFunctionPointerType clGetPlatformInfoFP;
extern clGetDeviceIDsFunctionPointerType clGetDeviceIDsFP;
extern clGetDeviceInfoFunctionPointerType clGetDeviceInfoFP;
extern clCreateSubDevicesFunctionPointerType clCreateSubDevicesFP;
extern clRetainDeviceFunctionPointerType clRetainDeviceFP;
extern clReleaseDeviceFunctionPointerType clReleaseDeviceFP;
extern clCreateContextFunctionPointerType clCreateContextFP;
extern clCreateContextFromTypeFunctionPointerType clCreateContextFromTypeFP;
extern clRetainContextFunctionPointerType clRetainContextFP;
extern clReleaseContextFunctionPointerType clReleaseContextFP;
extern clGetContextInfoFunctionPointerType clGetContextInfoFP;
extern clCreateCommandQueueFunctionPointerType clCreateCommandQueueFP;
extern clRetainCommandQueueFunctionPointerType clRetainCommandQueueFP;
extern clReleaseCommandQueueFunctionPointerType clReleaseCommandQueueFP;
extern clGetCommandQueueInfoFunctionPointerType clGetCommandQueueInfoFP;
extern clCreateBufferFunctionPointerType clCreateBufferFP;
extern clCreateSubBufferFunctionPointerType clCreateSubBufferFP;
extern clCreateImageFunctionPointerType clCreateImageFP;
extern clRetainMemObjectFunctionPointerType clRetainMemObjectFP;
extern clReleaseMemObjectFunctionPointerType clReleaseMemObjectFP;
extern clGetSupportedImageFormatsFunctionPointerType clGetSupportedImageFormatsFP;
extern clGetMemObjectInfoFunctionPointerType clGetMemObjectInfoFP;
extern clGetImageInfoFunctionPointerType clGetImageInfoFP;
extern clSetMemObjectDestructorCallbackFunctionPointerType clSetMemObjectDestructorCallbackFP;
extern clCreateSamplerFunctionPointerType clCreateSamplerFP;
extern clRetainSamplerFunctionPointerType clRetainSamplerFP;
extern clReleaseSamplerFunctionPointerType clReleaseSamplerFP;
extern clGetSamplerInfoFunctionPointerType clGetSamplerInfoFP;
extern clCreateProgramWithSourceFunctionPointerType clCreateProgramWithSourceFP;
extern clCreateProgramWithBinaryFunctionPointerType clCreateProgramWithBinaryFP;
extern clCreateProgramWithBuiltInKernelsFunctionPointerType clCreateProgramWithBuiltInKernelsFP;
extern clRetainProgramFunctionPointerType clRetainProgramFP;
extern clReleaseProgramFunctionPointerType clReleaseProgramFP;
extern clBuildProgramFunctionPointerType clBuildProgramFP;
extern clCompileProgramFunctionPointerType clCompileProgramFP;
extern clLinkProgramFunctionPointerType clLinkProgramFP;
extern clUnloadPlatformCompilerFunctionPointerType clUnloadPlatformCompilerFP;
extern clGetProgramInfoFunctionPointerType clGetProgramInfoFP;
extern clGetProgramBuildInfoFunctionPointerType clGetProgramBuildInfoFP;
extern clCreateKernelFunctionPointerType clCreateKernelFP;
extern clCreateKernelsInProgramFunctionPointerType clCreateKernelsInProgramFP;
extern clRetainKernelFunctionPointerType clRetainKernelFP;
extern clReleaseKernelFunctionPointerType clReleaseKernelFP;
extern clSetKernelArgFunctionPointerType clSetKernelArgFP;
extern clGetKernelInfoFunctionPointerType clGetKernelInfoFP;
extern clGetKernelArgInfoFunctionPointerType clGetKernelArgInfoFP;
extern clGetKernelWorkGroupInfoFunctionPointerType clGetKernelWorkGroupInfoFP;
extern clWaitForEventsFunctionPointerType clWaitForEventsFP;
extern clGetEventInfoFunctionPointerType clGetEventInfoFP;
extern clCreateUserEventFunctionPointerType clCreateUserEventFP;
extern clRetainEventFunctionPointerType clRetainEventFP;
extern clReleaseEventFunctionPointerType clReleaseEventFP;
extern clSetUserEventStatusFunctionPointerType clSetUserEventStatusFP;
extern clSetEventCallbackFunctionPointerType clSetEventCallbackFP;
extern clGetEventProfilingInfoFunctionPointerType clGetEventProfilingInfoFP;
extern clFlushFunctionPointerType clFlushFP;
extern clFinishFunctionPointerType clFinishFP;
extern clEnqueueReadBufferFunctionPointerType clEnqueueReadBufferFP;
extern clEnqueueReadBufferRectFunctionPointerType clEnqueueReadBufferRectFP;
extern clEnqueueWriteBufferFunctionPointerType clEnqueueWriteBufferFP;
extern clEnqueueWriteBufferRectFunctionPointerType clEnqueueWriteBufferRectFP;
extern clEnqueueFillBufferFunctionPointerType clEnqueueFillBufferFP;
extern clEnqueueCopyBufferFunctionPointerType clEnqueueCopyBufferFP;
extern clEnqueueCopyBufferRectFunctionPointerType clEnqueueCopyBufferRectFP;
extern clEnqueueReadImageFunctionPointerType clEnqueueReadImageFP;
extern clEnqueueWriteImageFunctionPointerType clEnqueueWriteImageFP;
extern clEnqueueFillImageFunctionPointerType clEnqueueFillImageFP;
extern clEnqueueCopyImageFunctionPointerType clEnqueueCopyImageFP;
extern clEnqueueCopyImageToBufferFunctionPointerType clEnqueueCopyImageToBufferFP;
extern clEnqueueCopyBufferToImageFunctionPointerType clEnqueueCopyBufferToImageFP;
extern clEnqueueMapBufferFunctionPointerType clEnqueueMapBufferFP;
extern clEnqueueMapImageFunctionPointerType clEnqueueMapImageFP;
extern clEnqueueUnmapMemObjectFunctionPointerType clEnqueueUnmapMemObjectFP;
extern clEnqueueMigrateMemObjectsFunctionPointerType clEnqueueMigrateMemObjectsFP;
extern clEnqueueNDRangeKernelFunctionPointerType clEnqueueNDRangeKernelFP;
extern clEnqueueTaskFunctionPointerType clEnqueueTaskFP;
extern clEnqueueNativeKernelFunctionPointerType clEnqueueNativeKernelFP;
extern clEnqueueMarkerWithWaitListFunctionPointerType clEnqueueMarkerWithWaitListFP;
extern clEnqueueBarrierWithWaitListFunctionPointerType clEnqueueBarrierWithWaitListFP;
extern clSetPrintfCallbackFunctionPointerType clSetPrintfCallbackFP;
extern clGetExtensionFunctionAddressForPlatformFunctionPointerType clGetExtensionFunctionAddressForPlatformFP;
extern clSetCommandQueuePropertyFunctionPointerType clSetCommandQueuePropertyFP;
extern clCreateImage2DFunctionPointerType clCreateImage2DFP;
extern clCreateImage3DFunctionPointerType clCreateImage3DFP;
extern clEnqueueMarkerFunctionPointerType clEnqueueMarkerFP;
extern clEnqueueWaitForEventsFunctionPointerType clEnqueueWaitForEventsFP;
extern clEnqueueBarrierFunctionPointerType clEnqueueBarrierFP;
extern clUnloadCompilerFunctionPointerType clUnloadCompilerFP;
extern clGetExtensionFunctionAddressFunctionPointerType clGetExtensionFunctionAddressFP;

extern clCreateFromGLBufferFunctionPointerType clCreateFromGLBufferFP;
extern clCreateFromGLTextureFunctionPointerType clCreateFromGLTextureFP;
extern clCreateFromGLRenderbufferFunctionPointerType clCreateFromGLRenderbufferFP;
extern clGetGLObjectInfoFunctionPointerType clGetGLObjectInfoFP;
extern clGetGLTextureInfoFunctionPointerType clGetGLTextureInfoFP;
extern clEnqueueAcquireGLObjectsFunctionPointerType clEnqueueAcquireGLObjectsFP;
extern clEnqueueReleaseGLObjectsFunctionPointerType clEnqueueReleaseGLObjectsFP;
extern clCreateFromGLTexture2DFunctionPointerType clCreateFromGLTexture2DFP;
extern clCreateFromGLTexture3DFunctionPointerType clCreateFromGLTexture3DFP;


#endif

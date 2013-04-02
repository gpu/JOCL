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

#include "CLFunctions.hpp"

// The pointers to the CL functions. The types of these pointers
// are defined in the CLFunction.hpp header. The values will be
// assigned to these pointers when the native library is 
// initialized with a call to Java_org_jocl_CL_initNativeLibrary,
// in the FunctionPointerUtils::initFunctionPointers function.

clGetPlatformIDsFunctionPointerType clGetPlatformIDsFP = NULL;
clGetPlatformInfoFunctionPointerType clGetPlatformInfoFP = NULL;
clGetDeviceIDsFunctionPointerType clGetDeviceIDsFP = NULL;
clGetDeviceInfoFunctionPointerType clGetDeviceInfoFP = NULL;
clCreateSubDevicesFunctionPointerType clCreateSubDevicesFP = NULL;
clRetainDeviceFunctionPointerType clRetainDeviceFP = NULL;
clReleaseDeviceFunctionPointerType clReleaseDeviceFP = NULL;
clCreateContextFunctionPointerType clCreateContextFP = NULL;
clCreateContextFromTypeFunctionPointerType clCreateContextFromTypeFP = NULL;
clRetainContextFunctionPointerType clRetainContextFP = NULL;
clReleaseContextFunctionPointerType clReleaseContextFP = NULL;
clGetContextInfoFunctionPointerType clGetContextInfoFP = NULL;
clCreateCommandQueueFunctionPointerType clCreateCommandQueueFP = NULL;
clRetainCommandQueueFunctionPointerType clRetainCommandQueueFP = NULL;
clReleaseCommandQueueFunctionPointerType clReleaseCommandQueueFP = NULL;
clGetCommandQueueInfoFunctionPointerType clGetCommandQueueInfoFP = NULL;
clCreateBufferFunctionPointerType clCreateBufferFP = NULL;
clCreateSubBufferFunctionPointerType clCreateSubBufferFP = NULL;
clCreateImageFunctionPointerType clCreateImageFP = NULL;
clRetainMemObjectFunctionPointerType clRetainMemObjectFP = NULL;
clReleaseMemObjectFunctionPointerType clReleaseMemObjectFP = NULL;
clGetSupportedImageFormatsFunctionPointerType clGetSupportedImageFormatsFP = NULL;
clGetMemObjectInfoFunctionPointerType clGetMemObjectInfoFP = NULL;
clGetImageInfoFunctionPointerType clGetImageInfoFP = NULL;
clSetMemObjectDestructorCallbackFunctionPointerType clSetMemObjectDestructorCallbackFP = NULL;
clCreateSamplerFunctionPointerType clCreateSamplerFP = NULL;
clRetainSamplerFunctionPointerType clRetainSamplerFP = NULL;
clReleaseSamplerFunctionPointerType clReleaseSamplerFP = NULL;
clGetSamplerInfoFunctionPointerType clGetSamplerInfoFP = NULL;
clCreateProgramWithSourceFunctionPointerType clCreateProgramWithSourceFP = NULL;
clCreateProgramWithBinaryFunctionPointerType clCreateProgramWithBinaryFP = NULL;
clCreateProgramWithBuiltInKernelsFunctionPointerType clCreateProgramWithBuiltInKernelsFP = NULL;
clRetainProgramFunctionPointerType clRetainProgramFP = NULL;
clReleaseProgramFunctionPointerType clReleaseProgramFP = NULL;
clBuildProgramFunctionPointerType clBuildProgramFP = NULL;
clCompileProgramFunctionPointerType clCompileProgramFP = NULL;
clLinkProgramFunctionPointerType clLinkProgramFP = NULL;
clUnloadPlatformCompilerFunctionPointerType clUnloadPlatformCompilerFP = NULL;
clGetProgramInfoFunctionPointerType clGetProgramInfoFP = NULL;
clGetProgramBuildInfoFunctionPointerType clGetProgramBuildInfoFP = NULL;
clCreateKernelFunctionPointerType clCreateKernelFP = NULL;
clCreateKernelsInProgramFunctionPointerType clCreateKernelsInProgramFP = NULL;
clRetainKernelFunctionPointerType clRetainKernelFP = NULL;
clReleaseKernelFunctionPointerType clReleaseKernelFP = NULL;
clSetKernelArgFunctionPointerType clSetKernelArgFP = NULL;
clGetKernelInfoFunctionPointerType clGetKernelInfoFP = NULL;
clGetKernelArgInfoFunctionPointerType clGetKernelArgInfoFP = NULL;
clGetKernelWorkGroupInfoFunctionPointerType clGetKernelWorkGroupInfoFP = NULL;
clWaitForEventsFunctionPointerType clWaitForEventsFP = NULL;
clGetEventInfoFunctionPointerType clGetEventInfoFP = NULL;
clCreateUserEventFunctionPointerType clCreateUserEventFP = NULL;
clRetainEventFunctionPointerType clRetainEventFP = NULL;
clReleaseEventFunctionPointerType clReleaseEventFP = NULL;
clSetUserEventStatusFunctionPointerType clSetUserEventStatusFP = NULL;
clSetEventCallbackFunctionPointerType clSetEventCallbackFP = NULL;
clGetEventProfilingInfoFunctionPointerType clGetEventProfilingInfoFP = NULL;
clFlushFunctionPointerType clFlushFP = NULL;
clFinishFunctionPointerType clFinishFP = NULL;
clEnqueueReadBufferFunctionPointerType clEnqueueReadBufferFP = NULL;
clEnqueueReadBufferRectFunctionPointerType clEnqueueReadBufferRectFP = NULL;
clEnqueueWriteBufferFunctionPointerType clEnqueueWriteBufferFP = NULL;
clEnqueueWriteBufferRectFunctionPointerType clEnqueueWriteBufferRectFP = NULL;
clEnqueueFillBufferFunctionPointerType clEnqueueFillBufferFP = NULL;
clEnqueueCopyBufferFunctionPointerType clEnqueueCopyBufferFP = NULL;
clEnqueueCopyBufferRectFunctionPointerType clEnqueueCopyBufferRectFP = NULL;
clEnqueueReadImageFunctionPointerType clEnqueueReadImageFP = NULL;
clEnqueueWriteImageFunctionPointerType clEnqueueWriteImageFP = NULL;
clEnqueueFillImageFunctionPointerType clEnqueueFillImageFP = NULL;
clEnqueueCopyImageFunctionPointerType clEnqueueCopyImageFP = NULL;
clEnqueueCopyImageToBufferFunctionPointerType clEnqueueCopyImageToBufferFP = NULL;
clEnqueueCopyBufferToImageFunctionPointerType clEnqueueCopyBufferToImageFP = NULL;
clEnqueueMapBufferFunctionPointerType clEnqueueMapBufferFP = NULL;
clEnqueueMapImageFunctionPointerType clEnqueueMapImageFP = NULL;
clEnqueueUnmapMemObjectFunctionPointerType clEnqueueUnmapMemObjectFP = NULL;
clEnqueueMigrateMemObjectsFunctionPointerType clEnqueueMigrateMemObjectsFP = NULL;
clEnqueueNDRangeKernelFunctionPointerType clEnqueueNDRangeKernelFP = NULL;
clEnqueueTaskFunctionPointerType clEnqueueTaskFP = NULL;
clEnqueueNativeKernelFunctionPointerType clEnqueueNativeKernelFP = NULL;
clEnqueueMarkerWithWaitListFunctionPointerType clEnqueueMarkerWithWaitListFP = NULL;
clEnqueueBarrierWithWaitListFunctionPointerType clEnqueueBarrierWithWaitListFP = NULL;
clSetPrintfCallbackFunctionPointerType clSetPrintfCallbackFP = NULL;
clGetExtensionFunctionAddressForPlatformFunctionPointerType clGetExtensionFunctionAddressForPlatformFP = NULL;
clSetCommandQueuePropertyFunctionPointerType clSetCommandQueuePropertyFP = NULL;
clCreateImage2DFunctionPointerType clCreateImage2DFP = NULL;
clCreateImage3DFunctionPointerType clCreateImage3DFP = NULL;
clEnqueueMarkerFunctionPointerType clEnqueueMarkerFP = NULL;
clEnqueueWaitForEventsFunctionPointerType clEnqueueWaitForEventsFP = NULL;
clEnqueueBarrierFunctionPointerType clEnqueueBarrierFP = NULL;
clUnloadCompilerFunctionPointerType clUnloadCompilerFP = NULL;
clGetExtensionFunctionAddressFunctionPointerType clGetExtensionFunctionAddressFP = NULL;

clCreateFromGLBufferFunctionPointerType clCreateFromGLBufferFP = NULL;
clCreateFromGLTextureFunctionPointerType clCreateFromGLTextureFP = NULL;
clCreateFromGLRenderbufferFunctionPointerType clCreateFromGLRenderbufferFP = NULL;
clGetGLObjectInfoFunctionPointerType clGetGLObjectInfoFP = NULL;
clGetGLTextureInfoFunctionPointerType clGetGLTextureInfoFP = NULL;
clEnqueueAcquireGLObjectsFunctionPointerType clEnqueueAcquireGLObjectsFP = NULL;
clEnqueueReleaseGLObjectsFunctionPointerType clEnqueueReleaseGLObjectsFP = NULL;
clCreateFromGLTexture2DFunctionPointerType clCreateFromGLTexture2DFP = NULL;
clCreateFromGLTexture3DFunctionPointerType clCreateFromGLTexture3DFP = NULL;

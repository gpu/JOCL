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

#include "FunctionPointerUtils.hpp"

#include "CLFunctions.hpp"

/**
 * Template method that obtains the pointer to the function
 * with the given name, and stores it in the given pointer.
 */
template <typename T> void initFunctionPointer(T* pointer, const char* name)
{
	T result =  (T)obtainFunctionPointer(name);
	*pointer = result;
}

/**
 * Initialize all pointers to OpenCL functions
 */
void initFunctionPointers()
{
    initFunctionPointer(&clGetPlatformIDsFP, "clGetPlatformIDs");
    initFunctionPointer(&clGetPlatformInfoFP, "clGetPlatformInfo");
    initFunctionPointer(&clGetDeviceIDsFP, "clGetDeviceIDs");
    initFunctionPointer(&clGetDeviceInfoFP, "clGetDeviceInfo");
    initFunctionPointer(&clCreateSubDevicesFP, "clCreateSubDevices");
    initFunctionPointer(&clRetainDeviceFP, "clRetainDevice");
    initFunctionPointer(&clReleaseDeviceFP, "clReleaseDevice");
    initFunctionPointer(&clCreateContextFP, "clCreateContext");
    initFunctionPointer(&clCreateContextFromTypeFP, "clCreateContextFromType");
    initFunctionPointer(&clRetainContextFP, "clRetainContext");
    initFunctionPointer(&clReleaseContextFP, "clReleaseContext");
    initFunctionPointer(&clGetContextInfoFP, "clGetContextInfo");
    initFunctionPointer(&clCreateCommandQueueFP, "clCreateCommandQueue");
    initFunctionPointer(&clRetainCommandQueueFP, "clRetainCommandQueue");
    initFunctionPointer(&clReleaseCommandQueueFP, "clReleaseCommandQueue");
    initFunctionPointer(&clGetCommandQueueInfoFP, "clGetCommandQueueInfo");
    initFunctionPointer(&clCreateBufferFP, "clCreateBuffer");
    initFunctionPointer(&clCreateSubBufferFP, "clCreateSubBuffer");
    initFunctionPointer(&clCreateSubBufferFP, "clCreateSubBuffer");
    initFunctionPointer(&clCreateImageFP, "clCreateImage");
    initFunctionPointer(&clRetainMemObjectFP, "clRetainMemObject");
    initFunctionPointer(&clReleaseMemObjectFP, "clReleaseMemObject");
    initFunctionPointer(&clGetSupportedImageFormatsFP, "clGetSupportedImageFormats");
    initFunctionPointer(&clGetMemObjectInfoFP, "clGetMemObjectInfo");
    initFunctionPointer(&clGetImageInfoFP, "clGetImageInfo");
    initFunctionPointer(&clSetMemObjectDestructorCallbackFP, "clSetMemObjectDestructorCallback");
    initFunctionPointer(&clCreateSamplerFP, "clCreateSampler");
    initFunctionPointer(&clRetainSamplerFP, "clRetainSampler");
    initFunctionPointer(&clReleaseSamplerFP, "clReleaseSampler");
    initFunctionPointer(&clGetSamplerInfoFP, "clGetSamplerInfo");
    initFunctionPointer(&clCreateProgramWithSourceFP, "clCreateProgramWithSource");
    initFunctionPointer(&clCreateProgramWithBinaryFP, "clCreateProgramWithBinary");
    initFunctionPointer(&clCreateProgramWithBuiltInKernelsFP, "clCreateProgramWithBuiltInKernels");
    initFunctionPointer(&clRetainProgramFP, "clRetainProgram");
    initFunctionPointer(&clReleaseProgramFP, "clReleaseProgram");
    initFunctionPointer(&clBuildProgramFP, "clBuildProgram");
    initFunctionPointer(&clCompileProgramFP, "clCompileProgram");
    initFunctionPointer(&clLinkProgramFP, "clLinkProgram");
    initFunctionPointer(&clUnloadPlatformCompilerFP, "clUnloadPlatformCompiler");
    initFunctionPointer(&clGetProgramInfoFP, "clGetProgramInfo");
    initFunctionPointer(&clGetProgramBuildInfoFP, "clGetProgramBuildInfo");
    initFunctionPointer(&clCreateKernelFP, "clCreateKernel");
    initFunctionPointer(&clCreateKernelsInProgramFP, "clCreateKernelsInProgram");
    initFunctionPointer(&clRetainKernelFP, "clRetainKernel");
    initFunctionPointer(&clReleaseKernelFP, "clReleaseKernel");
    initFunctionPointer(&clSetKernelArgFP, "clSetKernelArg");
    initFunctionPointer(&clGetKernelInfoFP, "clGetKernelInfo");
    initFunctionPointer(&clGetKernelArgInfoFP, "clGetKernelArgInfo");
    initFunctionPointer(&clGetKernelWorkGroupInfoFP, "clGetKernelWorkGroupInfo");
    initFunctionPointer(&clWaitForEventsFP, "clWaitForEvents");
    initFunctionPointer(&clGetEventInfoFP, "clGetEventInfo");
    initFunctionPointer(&clCreateUserEventFP, "clCreateUserEvent");
    initFunctionPointer(&clRetainEventFP, "clRetainEvent");
    initFunctionPointer(&clReleaseEventFP, "clReleaseEvent");
    initFunctionPointer(&clSetUserEventStatusFP, "clSetUserEventStatus");
    initFunctionPointer(&clSetEventCallbackFP, "clSetEventCallback");
    initFunctionPointer(&clGetEventProfilingInfoFP, "clGetEventProfilingInfo");
    initFunctionPointer(&clFlushFP, "clFlush");
    initFunctionPointer(&clFinishFP, "clFinish");
    initFunctionPointer(&clEnqueueReadBufferFP, "clEnqueueReadBuffer");
    initFunctionPointer(&clEnqueueReadBufferRectFP, "clEnqueueReadBufferRect");
    initFunctionPointer(&clEnqueueWriteBufferFP, "clEnqueueWriteBuffer");
    initFunctionPointer(&clEnqueueWriteBufferRectFP, "clEnqueueWriteBufferRect");
    initFunctionPointer(&clEnqueueFillBufferFP, "clEnqueueFillBuffer");
    initFunctionPointer(&clEnqueueCopyBufferFP, "clEnqueueCopyBuffer");
    initFunctionPointer(&clEnqueueCopyBufferRectFP, "clEnqueueCopyBufferRect");
    initFunctionPointer(&clEnqueueReadImageFP, "clEnqueueReadImage");
    initFunctionPointer(&clEnqueueWriteImageFP, "clEnqueueWriteImage");
    initFunctionPointer(&clEnqueueFillImageFP, "clEnqueueFillImage");
    initFunctionPointer(&clEnqueueCopyImageFP, "clEnqueueCopyImage");
    initFunctionPointer(&clEnqueueCopyImageToBufferFP, "clEnqueueCopyImageToBuffer");
    initFunctionPointer(&clEnqueueCopyBufferToImageFP, "clEnqueueCopyBufferToImage");
    initFunctionPointer(&clEnqueueMapBufferFP, "clEnqueueMapBuffer");
    initFunctionPointer(&clEnqueueMapImageFP, "clEnqueueMapImage");
    initFunctionPointer(&clEnqueueUnmapMemObjectFP, "clEnqueueUnmapMemObject");
    initFunctionPointer(&clEnqueueMigrateMemObjectsFP, "clEnqueueMigrateMemObjects");
    initFunctionPointer(&clEnqueueNDRangeKernelFP, "clEnqueueNDRangeKernel");
    initFunctionPointer(&clEnqueueTaskFP, "clEnqueueTask");
    initFunctionPointer(&clEnqueueNativeKernelFP, "clEnqueueNativeKernel");
    initFunctionPointer(&clEnqueueMarkerWithWaitListFP, "clEnqueueMarkerWithWaitList");
    initFunctionPointer(&clEnqueueBarrierWithWaitListFP, "clEnqueueBarrierWithWaitList");
    initFunctionPointer(&clSetPrintfCallbackFP, "clSetPrintfCallback");
    initFunctionPointer(&clGetExtensionFunctionAddressForPlatformFP, "clGetExtensionFunctionAddressForPlatform");
    initFunctionPointer(&clSetCommandQueuePropertyFP, "clSetCommandQueueProperty");
    initFunctionPointer(&clCreateImage2DFP, "clCreateImage2D");
    initFunctionPointer(&clCreateImage3DFP, "clCreateImage3D");
    initFunctionPointer(&clEnqueueMarkerFP, "clEnqueueMarker");
    initFunctionPointer(&clEnqueueWaitForEventsFP, "clEnqueueWaitForEvents");
    initFunctionPointer(&clEnqueueBarrierFP, "clEnqueueBarrier");
    initFunctionPointer(&clUnloadCompilerFP, "clUnloadCompiler");
    initFunctionPointer(&clGetExtensionFunctionAddressFP, "clGetExtensionFunctionAddress");
    initFunctionPointer(&clCreateFromGLBufferFP, "clCreateFromGLBuffer");
    initFunctionPointer(&clCreateFromGLTextureFP, "clCreateFromGLTexture");
    initFunctionPointer(&clCreateFromGLRenderbufferFP, "clCreateFromGLRenderbuffer");
    initFunctionPointer(&clGetGLObjectInfoFP, "clGetGLObjectInfo");
    initFunctionPointer(&clGetGLTextureInfoFP, "clGetGLTextureInfo");
    initFunctionPointer(&clEnqueueAcquireGLObjectsFP, "clEnqueueAcquireGLObjects");
    initFunctionPointer(&clEnqueueReleaseGLObjectsFP, "clEnqueueReleaseGLObjects");
    initFunctionPointer(&clCreateFromGLTexture2DFP, "clCreateFromGLTexture2D");
    initFunctionPointer(&clCreateFromGLTexture3DFP, "clCreateFromGLTexture3D");


}

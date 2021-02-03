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

#include "JOCL.hpp"

#include <string.h>
#include <string>
#include <map>

#include "Logger.hpp"
#include "JOCLCommon.hpp"
#include "JNIUtils.hpp"
#include "PointerUtils.hpp"
#include "CLJNIUtils.hpp"

#include "CLFunctions.hpp"
#include "FunctionPointerUtils.hpp"

// Static method IDs for the "function pointer" interfaces
static jmethodID CreateContextFunction_function; // (Ljava/lang/String;Lorg/jocl/Pointer;JLjava/lang/Object;)V
static jmethodID BuildProgramFunction_function; // (Lorg/jocl/cl_program;Ljava/lang/Object;)V
static jmethodID EnqueueNativeKernelFunction_function; // (Ljava/lang/Object;)V
static jmethodID MemObjectDestructorCallback_function; // (Lorg/jocl/cl_mem;Ljava/lang/Object;)V
static jmethodID EventCallback_function; // (Lorg/jocl/cl_event;ILjava/lang/Object;)V
static jmethodID PrintfCallbackFunction_function; // (Lorg/jocl/cl_program;Ljava/lang/Object;)V
static jmethodID SVMFreeFunction_function; // (Lorg/jocl/cl_command_queue;I[Lorg/jocl/Pointer;Ljava/lang/Object;)V



/**
 * Register all native functions of JOCL
 */
void registerAllNatives(JNIEnv *env, jclass cls);


/**
 * Called when the library is loaded. Will initialize all
 * required global class references, field and method IDs
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void**)&env, JNI_VERSION_1_4))
    {
        return JNI_ERR;
    }

    Logger::log(LOG_TRACE, "Initializing JOCL\n");

    // Initialize the utility methods
    if (initJNIUtils(env) == JNI_ERR) return JNI_ERR;
    if (initCLJNIUtils(env) == JNI_ERR) return JNI_ERR;
    if (initPointerUtils(env) == JNI_ERR) return JNI_ERR;

    globalJvm = jvm;

    jclass cls = NULL;

    if (!init(env, cls, "org/jocl/CL")) return JNI_ERR;
    registerAllNatives(env, cls);

    // Obtain the methodID for org.jocl.CreateContextFunction#function
    if (!init(env, cls, "org/jocl/CreateContextFunction")) return JNI_ERR;
    if (!init(env, cls, CreateContextFunction_function, "function", "(Ljava/lang/String;Lorg/jocl/Pointer;JLjava/lang/Object;)V")) return JNI_ERR;

    // Obtain the methodID for org.jocl.BuildProgramFunction#function
    if (!init(env, cls, "org/jocl/BuildProgramFunction")) return JNI_ERR;
    if (!init(env, cls, BuildProgramFunction_function, "function", "(Lorg/jocl/cl_program;Ljava/lang/Object;)V")) return JNI_ERR;

    // Obtain the methodID for org.jocl.EnqueueNativeKernelFunction#function
    if (!init(env, cls, "org/jocl/EnqueueNativeKernelFunction")) return JNI_ERR;
    if (!init(env, cls, EnqueueNativeKernelFunction_function, "function", "(Ljava/lang/Object;)V")) return JNI_ERR;

    // Obtain the methodID for org.jocl.MemObjectDestructorCallbackFunction#function
    if (!init(env, cls, "org/jocl/MemObjectDestructorCallbackFunction")) return JNI_ERR;
    if (!init(env, cls, MemObjectDestructorCallback_function, "function", "(Lorg/jocl/cl_mem;Ljava/lang/Object;)V")) return JNI_ERR;

    // Obtain the methodID for org.jocl.EventCallbackFunction#function
    if (!init(env, cls, "org/jocl/EventCallbackFunction")) return JNI_ERR;
    if (!init(env, cls, EventCallback_function, "function", "(Lorg/jocl/cl_event;ILjava/lang/Object;)V")) return JNI_ERR;

    // Obtain the methodID for org.jocl.PrintfCallbackFunction#function
    if (!init(env, cls, "org/jocl/PrintfCallbackFunction")) return JNI_ERR;
    if (!init(env, cls, PrintfCallbackFunction_function, "function", "(Lorg/jocl/cl_context;ILjava/lang/String;Ljava/lang/Object;)V")) return JNI_ERR;

    // Obtain the methodID for org.jocl.SVMFreeFunction#function
    if (!init(env, cls, "org/jocl/SVMFreeFunction")) return JNI_ERR;
    if (!init(env, cls, SVMFreeFunction_function, "function", "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/Pointer;Ljava/lang/Object;)V")) return JNI_ERR;

    return JNI_VERSION_1_4;
}

/**
 * Called when the library is unloaded.
 */
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
    unloadImplementationLibrary();
}

/**
 * Create a Java org.jocl.Pointer object that has the given native 
 * pointer. If the given size is greater than 0, the object will
 * contain a direct byte buffer with the given pointer address.
 * Returns NULL if any exception occurs.
 */
jobject createJavaPointerObject(JNIEnv *env, void *nativePointer, size_t nativeSize)
{
    jobject pointer = env->NewObject(Pointer_Class, Pointer_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }
    jobject buffer = NULL;
	if (nativeSize > 0)
	{
		buffer = env->NewDirectByteBuffer(nativePointer, (jlong)nativeSize);
		if (env->ExceptionCheck())
		{
			return NULL;
		}
	}
    env->SetObjectField(pointer, NativePointerObject_buffer, buffer);
    env->SetObjectField(pointer, NativePointerObject_pointers, NULL);
    env->SetLongField(pointer, NativePointerObject_byteOffset, 0);
    env->SetLongField(pointer, NativePointerObject_nativePointer, (jlong)nativePointer);
    return pointer;
}

// TODO The callbacks should perform as much cleanup as 
// possible, even when an error occurs, although these
// errors will be fatal (out of memory etc) in any case.

/**
 * A pointer to this function will be passed to clCreateContext* functions.
 * The user_dataInfo is a pointer to a CallbackInfo that was initialized
 * and is associated with the respective call to clCreateContext*.
 */
void CL_CALLBACK CreateContextFunction(const char *errinfo, const void *private_info, size_t cb, void *user_dataInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing CreateContextFunction\n");

    CallbackInfo *callbackInfo = (CallbackInfo*)user_dataInfo;

    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify == NULL)
    {
        return;
    }
    jobject user_data = callbackInfo->globalUser_data;

    JNIEnv *env = NULL;
    jint attached = globalJvm->GetEnv((void**)&env, JNI_VERSION_1_4);
    if (attached != JNI_OK)
    {
        globalJvm->AttachCurrentThread((void**)&env, NULL);
    }

    jstring errinfoString = env->NewStringUTF(errinfo);

    // TODO: This should actually be a Pointer to the private_info,
    // but since this can not be used on Java side, simply pass
    // a NULL object...
    jobject private_infoObject = NULL;
    env->CallVoidMethod(pfn_notify, CreateContextFunction_function, errinfoString, private_infoObject, cb, user_data);

    finishCallback(env);
    if (attached != JNI_OK)
    {
        globalJvm->DetachCurrentThread();
    }

}



/**
 * A pointer to this function will be passed to the clBuildProgram function
 * if a Java callback object was given. The user_dataInfo is a pointer to a
 * CallbackInfo that was initialized and is associated with the respective
 * call to clBuildProgram.
 */
void CL_CALLBACK BuildProgramFunction(cl_program program, void *user_dataInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing BuildProgramFunction\n");

    JNIEnv *env = NULL;
    jint attached = globalJvm->GetEnv((void**)&env, JNI_VERSION_1_4);
    if (attached != JNI_OK)
    {
        globalJvm->AttachCurrentThread((void**)&env, NULL);
    }

    CallbackInfo *callbackInfo = (CallbackInfo*)user_dataInfo;
    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify != NULL)
    {
        jobject user_data = callbackInfo->globalUser_data;

        // Create the program object which will be passed to the callback function
        jobject programObject = env->NewObject(cl_program_Class, cl_program_Constructor);
        if (env->ExceptionCheck())
        {
            return;
        }
        setNativePointer(env, programObject, (jlong)program);

        env->CallVoidMethod(pfn_notify, BuildProgramFunction_function, programObject, user_data);
    }
    deleteCallbackInfo(env, callbackInfo);
    finishCallback(env);
    if (attached != JNI_OK)
    {
        globalJvm->DetachCurrentThread();
    }
}


/**
 * A pointer to this function will be passed to the EnqueueNativeKernelFunction function
 * if a Java callback object was given. The argsInfo is a pointer to a
 * CallbackInfo that was initialized and is associated with the respective
 * call to clEnqueueNativeKernel.
 */
void CL_CALLBACK EnqueueNativeKernelFunction(void *argsInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing EnqueueNativeKernelFunction\n");

    JNIEnv *env = NULL;
    jint attached = globalJvm->GetEnv((void**)&env, JNI_VERSION_1_4);
    if (attached != JNI_OK)
    {
        globalJvm->AttachCurrentThread((void**)&env, NULL);
    }

    CallbackInfo *callbackInfo = (CallbackInfo*)argsInfo;
    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify != NULL)
    {
        jobject user_data = callbackInfo->globalUser_data;

        env->CallVoidMethod(pfn_notify, EnqueueNativeKernelFunction_function, user_data);
    }
    deleteCallbackInfo(env, callbackInfo);
    finishCallback(env);
    if (attached != JNI_OK)
    {
        globalJvm->DetachCurrentThread();
    }
}





/**
 * A pointer to this function will be passed to the clSetMemObjectDestructorCallback
 * function if a Java callback object was given. The argsInfo is a pointer to a
 * CallbackInfo that was initialized and is associated with the respective
 * call to clSetMemObjectDestructorCallback.
 */
void CL_CALLBACK MemObjectDestructorCallback(cl_mem memobj, void *user_dataInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing MemObjectDestructorCallback\n");

    JNIEnv *env = NULL;
    jint attached = globalJvm->GetEnv((void**)&env, JNI_VERSION_1_4);
    if (attached != JNI_OK)
    {
        globalJvm->AttachCurrentThread((void**)&env, NULL);
    }

    CallbackInfo *callbackInfo = (CallbackInfo*)user_dataInfo;
    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify != NULL)
    {
        jobject user_data = callbackInfo->globalUser_data;

        // Create the memory object which will be passed to the callback function
        jobject memobjObject = env->NewObject(cl_mem_Class, cl_mem_Constructor);
        if (env->ExceptionCheck())
        {
            return;
        }
        setNativePointer(env, memobjObject, (jlong)memobj);

        env->CallVoidMethod(pfn_notify, MemObjectDestructorCallback_function, memobjObject, user_data);
    }
    deleteCallbackInfo(env, callbackInfo);
    finishCallback(env);
    if (attached != JNI_OK)
    {
        globalJvm->DetachCurrentThread();
    }
}



/**
 * A pointer to this function will be passed to the clSetEventCallback
 * function if a Java callback object was given. The argsInfo is a pointer to a
 * CallbackInfo that was initialized and is associated with the respective
 * call to clSetEventCallback.
 */
void CL_CALLBACK EventCallback(cl_event event, cl_int command_exec_callback_type, void *user_dataInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing EventCallback\n");

    JNIEnv *env = NULL;
    jint attached = globalJvm->GetEnv((void**)&env, JNI_VERSION_1_4);
    if (attached != JNI_OK)
    {
        globalJvm->AttachCurrentThread((void**)&env, NULL);
    }

    CallbackInfo *callbackInfo = (CallbackInfo*)user_dataInfo;
    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify != NULL)
    {
        jobject user_data = callbackInfo->globalUser_data;

        // Create the event object which will be passed to the callback function
        jobject eventObject = env->NewObject(cl_event_Class, cl_event_Constructor);
        if (env->ExceptionCheck())
        {
            return;
        }
        setNativePointer(env, eventObject, (jlong)event);

        env->CallVoidMethod(pfn_notify, EventCallback_function, eventObject, command_exec_callback_type, user_data);
    }
    deleteCallbackInfo(env, callbackInfo);
    finishCallback(env);
    if (attached != JNI_OK)
    {
        globalJvm->DetachCurrentThread();
    }
}



/**
 * A pointer to this function will be passed to the clSetPrintfCallback function
 * if a Java callback object was given. The user_dataInfo is a pointer to a
 * CallbackInfo that was initialized and is associated with the respective
 * call to clSetPrintfCallback.
 */
void CL_CALLBACK PrintfCallbackFunction(cl_context context, cl_uint printf_data_len, char *printf_data_ptr, void *user_dataInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing PrintfCallbackFunction\n");

    JNIEnv *env = NULL;
    jint attached = globalJvm->GetEnv((void**)&env, JNI_VERSION_1_4);
    if (attached != JNI_OK)
    {
        globalJvm->AttachCurrentThread((void**)&env, NULL);
    }

    CallbackInfo *callbackInfo = (CallbackInfo*)user_dataInfo;
    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify != NULL)
    {
        jobject user_data = callbackInfo->globalUser_data;

        // Create the context object which will be passed to the callback function
        jobject contextObject = env->NewObject(cl_context_Class, cl_context_Constructor);
        if (env->ExceptionCheck())
        {
            return;
        }
        setNativePointer(env, contextObject, (jlong)context);

        jstring printfDataString = env->NewStringUTF(printf_data_ptr);
        if (printfDataString == NULL)
        {
            // OutOfMemoryError was already thrown
            return;
        }

        env->CallVoidMethod(pfn_notify, PrintfCallbackFunction_function, contextObject, (jint)printf_data_len, printfDataString, user_data);
    }
    deleteCallbackInfo(env, callbackInfo);
    finishCallback(env);
    if (attached != JNI_OK)
    {
        globalJvm->DetachCurrentThread();
    }
}


/**
 * A pointer to this function will be passed to the clEnqueueSVMFree function
 * if a Java callback object was given. The user_dataInfo is a pointer to a
 * CallbackInfo that was initialized and is associated with the respective
 * call to clEnqueueSVMFree.
 */
void CL_CALLBACK SVMFreeCallbackFunction(cl_command_queue queue, cl_uint num_svn_pointers, void** svm_pointers, void *user_dataInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing SVMFreeCallbackFunction\n");

    JNIEnv *env = NULL;
    jint attached = globalJvm->GetEnv((void**)&env, JNI_VERSION_1_4);
    if (attached != JNI_OK)
    {
        globalJvm->AttachCurrentThread((void**)&env, NULL);
    }

    CallbackInfo *callbackInfo = (CallbackInfo*)user_dataInfo;
    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify != NULL)
    {
        jobject user_data = callbackInfo->globalUser_data;

        // Create the command queue object which will be passed to the callback function
        jobject queueObject = env->NewObject(cl_command_queue_Class, cl_command_queue_Constructor);
        if (queueObject == NULL)
        {
            return;
        }
        setNativePointer(env, queueObject, (jlong)queue);

		jobjectArray svm_pointersObjectArray = env->NewObjectArray((jsize)num_svn_pointers, Pointer_Class, NULL);
        if (svm_pointersObjectArray == NULL)
        {
            // OutOfMemoryError was already thrown
            return;
        }
		for (size_t i=0; i<num_svn_pointers; i++)
		{
			void *svm_pointer = svm_pointers[i];
			jobject svm_pointerObject = createJavaPointerObject(env, svm_pointer, 0);
			if (svm_pointerObject == NULL)
			{
				return;
			}
            env->SetObjectArrayElement(svm_pointersObjectArray, (jsize)i, svm_pointerObject);
		}
        env->CallVoidMethod(pfn_notify, SVMFreeFunction_function, queueObject, (jint)num_svn_pointers, svm_pointersObjectArray, user_data);
    }
    deleteCallbackInfo(env, callbackInfo);
    finishCallback(env);
    if (attached != JNI_OK)
    {
        globalJvm->DetachCurrentThread();
    }
}






/*
 * Class:     org_jocl_CL
 * Method:    initNativeLibrary
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_jocl_CL_initNativeLibrary
  (JNIEnv *env, jclass UNUSED(cls), jstring fullName)
{
    Logger::log(LOG_TRACE, "Initializing JOCL native library\n");

    char *fullNameNative = convertString(env, fullName);
    if (fullNameNative == NULL)
    {
        return JNI_FALSE;
    }

    Logger::log(LOG_DEBUGTRACE, "    Native library name: '%s'\n", fullNameNative);
    bool loaded = loadImplementationLibrary(fullNameNative);
    delete[] fullNameNative;

    if (loaded)
    {
        Logger::log(LOG_DEBUGTRACE, "    Initializing function pointers\n");
        initFunctionPointers();
        return JNI_TRUE;
    }
    Logger::log(LOG_DEBUGTRACE, "    Could not load native library\n");
    return JNI_FALSE;
}


/*
 * Class:     org_jocl_CL
 * Method:    setLogLevelNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_jocl_CL_setLogLevelNative
  (JNIEnv *env, jclass UNUSED(cls), jint logLevel)
{
    Logger::setLogLevel((LogLevel)logLevel);
}



//=== CL functions ===========================================================



/*
 * Class:     org_jocl_CL
 * Method:    clGetPlatformIDsNative
 * Signature: (I[Lorg/jocl/cl_platform_id;[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetPlatformIDsNative
  (JNIEnv *env, jclass UNUSED(cls), jint num_entries, jobjectArray platforms, jintArray num_platforms)
{
    Logger::log(LOG_TRACE, "Executing clGetPlatformIDs\n");
    if (clGetPlatformIDsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetPlatformIDs is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_uint nativeNum_entries = 0;
    cl_platform_id *nativePlatforms = NULL;
    cl_uint nativeNum_platforms = 0;

    // Obtain native variable values
    nativeNum_entries = (cl_uint)num_entries;
    if (platforms != NULL)
    {
        jsize platformsLength = env->GetArrayLength(platforms);
        nativePlatforms = new cl_platform_id[(size_t)platformsLength];
        if (nativePlatforms == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during platforms array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = (clGetPlatformIDsFP)(nativeNum_entries, nativePlatforms, &nativeNum_platforms);

    // Write back native variable values and clean up
    if (platforms != NULL)
    {
        cl_uint n = nativeNum_entries < nativeNum_platforms ? nativeNum_entries : nativeNum_platforms;
        for (size_t i = 0; i<n; i++)
        {
            jobject platform = env->GetObjectArrayElement(platforms, (jsize)i);
            if (env->ExceptionCheck())
            {
                return CL_INVALID_HOST_PTR;
            }
            if (platform == NULL)
            {
                platform = env->NewObject(cl_platform_id_Class, cl_platform_id_Constructor);
                if (platform == NULL)
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(platforms, (jsize)i, platform);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setNativePointer(env, platform, (jlong)nativePlatforms[i]);

        }
        delete[] nativePlatforms;
    }
    if (!set(env, num_platforms, 0, (jint)nativeNum_platforms)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetPlatformInfoNative
 * Signature: (Lorg/jocl/cl_platform_id;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetPlatformInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject platform, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetPlatformInfo\n");
    if (clGetPlatformInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetPlatformInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_platform_id nativePlatform = NULL;
    cl_uint nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (platform != NULL)
    {
        nativePlatform = (cl_platform_id)env->GetLongField(platform, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_uint)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetPlatformInfoFP)(nativePlatform, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetDeviceIDsNative
 * Signature: (Lorg/jocl/cl_platform_id;JI[Lorg/jocl/cl_device_id;[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetDeviceIDsNative
  (JNIEnv *env, jclass UNUSED(cls), jobject platform, jlong device_type, jint num_entries, jobjectArray devices, jintArray num_devices)
{
    Logger::log(LOG_TRACE, "Executing clGetDeviceIDs\n");
    if (clGetDeviceIDsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetDeviceIDs is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_platform_id nativePlatform = NULL;
    cl_device_type nativeDevice_type = 0;
    cl_uint nativeNum_entries = 0;
    cl_device_id *nativeDevices = NULL;
    cl_uint nativeNum_devices;

    // Obtain native variable values
    if (platform != NULL)
    {
        nativePlatform = (cl_platform_id)env->GetLongField(platform, NativePointerObject_nativePointer);
    }
    nativeDevice_type = (cl_device_type)device_type;
    nativeNum_entries = (cl_uint)num_entries;
    if (devices != NULL)
    {
        jsize devicesLength = env->GetArrayLength(devices);
        nativeDevices = new cl_device_id[(size_t)devicesLength];
        if (nativeDevices == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during devices array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = (clGetDeviceIDsFP)(nativePlatform, nativeDevice_type, nativeNum_entries, nativeDevices, &nativeNum_devices);

    // Write back native variable values and clean up
    if (devices != NULL)
    {
        cl_uint n = nativeNum_entries < nativeNum_devices ? nativeNum_entries : nativeNum_devices;
        for (size_t i=0; i<n; i++)
        {
            jobject device = env->GetObjectArrayElement(devices, (jsize)i);
            if (device == NULL)
            {
                device = env->NewObject(cl_device_id_Class, cl_device_id_Constructor);
                if (env->ExceptionCheck())
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(devices, (jsize)i, device);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setNativePointer(env, device, (jlong)nativeDevices[i]);
        }
        delete[] nativeDevices;
    }
    if (!set(env, num_devices, 0, (jint)nativeNum_devices)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetDeviceInfoNative
 * Signature: (Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetDeviceInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject device, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetDeviceInfo\n");
    if (clGetDeviceInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetDeviceInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_device_id nativeDevice = NULL;
    cl_device_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (device != NULL)
    {
        nativeDevice = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_device_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetDeviceInfoFP)(nativeDevice, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}


//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateSubDevicesNative
 * Signature: (Lorg/jocl/cl_device_id;Lorg/jocl/cl_device_partition_property;I[Lorg/jocl/cl_device_id;[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clCreateSubDevicesNative
  (JNIEnv *env, jclass UNUSED(cls), jobject in_device, jobject properties, jint num_devices, jobjectArray out_devices, jintArray num_devices_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateSubDevices\n");
    if (clCreateSubDevicesFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateSubDevices is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_device_id nativeIn_device = NULL;
    cl_device_partition_property *nativeProperties;
    cl_uint nativeNum_devices = 0;
    cl_device_id *nativeOut_devices = NULL;
    cl_uint nativeNum_devices_ret;

    // Obtain native variable values
    if (in_device != NULL)
    {
        nativeIn_device = (cl_device_id)env->GetLongField(in_device, NativePointerObject_nativePointer);
    }
    nativeProperties = getCl_device_partition_property(env, properties);
    nativeNum_devices = (cl_uint)num_devices;
    if (out_devices != NULL)
    {
        jsize devicesLength = env->GetArrayLength(out_devices);
        nativeOut_devices = new cl_device_id[(size_t)devicesLength];
        if (nativeOut_devices == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during devices array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = (clCreateSubDevicesFP)(nativeIn_device, nativeProperties, nativeNum_devices, nativeOut_devices, &nativeNum_devices_ret);

    // Write back native variable values and clean up
    delete nativeProperties;
    if (out_devices != NULL)
    {
        cl_uint n = nativeNum_devices_ret < nativeNum_devices ? nativeNum_devices_ret : nativeNum_devices;
        for (size_t i=0; i<n; i++)
        {
            jobject device = env->GetObjectArrayElement(out_devices, (jsize)i);
            if (device == NULL)
            {
                device = env->NewObject(cl_device_id_Class, cl_device_id_Constructor);
                if (env->ExceptionCheck())
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(out_devices, (jsize)i, device);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setNativePointer(env, device, (jlong)nativeOut_devices[i]);
        }
        delete[] nativeOut_devices;
    }
    if (!set(env, num_devices_ret, 0, (long)nativeNum_devices_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}

//#endif // defined(CL_VERSION_1_2)


//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clRetainDeviceNative
 * Signature: (Lorg/jocl/cl_device_id;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainDeviceNative
  (JNIEnv *env, jclass UNUSED(cls), jobject device)
{
    Logger::log(LOG_TRACE, "Executing clRetainDevice\n");
    if (clRetainDeviceFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainDevice is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_device_id nativeDevice = NULL;

    // Obtain native variable values
    if (device != NULL)
    {
        nativeDevice = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }

    int result = (clRetainDeviceFP)(nativeDevice);
    return result;
}

// #endif // defined(CL_VERSION_1_2)


// #if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clReleaseDeviceNative
 * Signature: (Lorg/jocl/cl_device_id;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseDeviceNative
  (JNIEnv *env, jclass UNUSED(cls), jobject device)
{
    Logger::log(LOG_TRACE, "Executing clReleaseDevice\n");
    if (clReleaseDeviceFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseDevice is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_device_id nativeDevice = NULL;

    // Obtain native variable values
    if (device != NULL)
    {
        nativeDevice = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }

    int result = (clReleaseDeviceFP)(nativeDevice);
    return result;
}


// #endif // defined(CL_VERSION_1_2)




/*
 * Class:     org_jocl_CL
 * Method:    clCreateContextNative
 * Signature: (Lorg/jocl/cl_context_properties;I[Lorg/jocl/cl_device_id;Lorg/jocl/CreateContextFunction;Ljava/lang/Object;[I)Lorg/jocl/cl_context;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateContextNative
  (JNIEnv *env, jclass UNUSED(cls), jobject properties, jint num_devices, jobjectArray devices, jobject pfn_notify, jobject user_data, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateContext\n");
    if (clCreateContextFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateContext is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context_properties *nativeProperties = NULL;
    cl_uint nativeNum_devices = 0;
    cl_device_id *nativeDevices = NULL;
    CreateContextFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_context nativeContext = NULL;

    // Obtain native variable values
    if (properties != NULL)
    {
        nativeProperties = createContextPropertiesArray(env, properties);
        if (nativeProperties == NULL)
        {
            return NULL;
        }
    }
    nativeNum_devices = (cl_uint)num_devices;
    if (devices != NULL)
    {
        jsize devicesLength = env->GetArrayLength(devices);
        nativeDevices = new cl_device_id[(size_t)devicesLength];
        if (nativeDevices == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during devices array creation");
            return NULL;
        }

        for (int i=0; i<devicesLength; i++)
        {
            jobject device = env->GetObjectArrayElement(devices, i);
            if (env->ExceptionCheck())
            {
                return NULL;
            }
            if (device != NULL)
            {
                nativeDevices[i] = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
            }
        }
    }
    CallbackInfo *callbackInfo = NULL;
    if (pfn_notify != NULL)
    {
        nativePfn_notify = &CreateContextFunction;
        callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
        if (callbackInfo == NULL)
        {
            return NULL;
        }
        nativeUser_data = (void*)callbackInfo;
    }


    nativeContext = (clCreateContextFP)(nativeProperties, nativeNum_devices, nativeDevices, nativePfn_notify, nativeUser_data, &nativeErrcode_ret);
    if (nativeContext != NULL)
    {
        contextCallbackMap[nativeContext] = callbackInfo;
    }
    else
    {
        deleteCallbackInfo(env, callbackInfo);
    }

    // Write back native variable values and clean up
    delete[] nativeProperties;
    delete[] nativeDevices;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeContext == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_context object for the native context
    jobject context = env->NewObject(cl_context_Class, cl_context_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }
    setNativePointer(env, context, (jlong)nativeContext);
    return context;
}


/*
 * Class:     org_jocl_CL
 * Method:    clCreateContextFromTypeNative
 * Signature: (Lorg/jocl/cl_context_properties;JLorg/jocl/CreateContextFunction;Ljava/lang/Object;[I)Lorg/jocl/cl_context;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateContextFromTypeNative
  (JNIEnv *env, jclass UNUSED(cls), jobject properties, jlong device_type, jobject pfn_notify, jobject user_data, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateContextFromType\n");
    if (clCreateContextFromTypeFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateContextFromType is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context_properties *nativeProperties = NULL;
    cl_device_type nativeDevice_type = 0;
    CreateContextFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_context nativeContext = NULL;

    // Obtain native variable values
    nativeProperties = createContextPropertiesArray(env, properties);
    nativeDevice_type = (cl_device_type)device_type;
    CallbackInfo *callbackInfo = NULL;
    if (pfn_notify != NULL)
    {
        nativePfn_notify = &CreateContextFunction;
        callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
        if (callbackInfo == NULL)
        {
            return NULL;
        }
        nativeUser_data = (void*)callbackInfo;
    }


    nativeContext = (clCreateContextFromTypeFP)(nativeProperties, nativeDevice_type, nativePfn_notify, nativeUser_data, &nativeErrcode_ret);
    if (nativeContext != NULL)
    {
        contextCallbackMap[nativeContext] = callbackInfo;
    }
    else
    {
        deleteCallbackInfo(env, callbackInfo);
    }

    // Write back native variable values and clean up
    delete[] nativeProperties;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeContext == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_context object for the native context
    jobject context = env->NewObject(cl_context_Class, cl_context_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, context, (jlong)nativeContext);

    return context;

}


/*
 * Class:     org_jocl_CL
 * Method:    clRetainContextNative
 * Signature: (Lorg/jocl/cl_context;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainContextNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context)
{
    Logger::log(LOG_TRACE, "Executing clRetainContext\n");
    if (clRetainContextFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainContext is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_context nativeContext = NULL;
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    return (clRetainContextFP)(nativeContext);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseContextNative
 * Signature: (Lorg/jocl/cl_context;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseContextNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context)
{
    Logger::log(LOG_TRACE, "Executing clReleaseContext\n");
    if (clReleaseContextFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseContext is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_context nativeContext = NULL;
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    int result = (clReleaseContextFP)(nativeContext);
    destroyCallbackInfo(env, nativeContext);
    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetContextInfoNative
 * Signature: (Lorg/jocl/cl_context;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetContextInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetContextInfo\n");
    if (clGetContextInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetContextInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_context_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_context_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetContextInfoFP)(nativeContext, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clCreateCommandQueueNative
 * Signature: (Lorg/jocl/cl_context;Lorg/jocl/cl_device_id;J[I)Lorg/jocl/cl_command_queue;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateCommandQueueNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jobject device, jlong properties, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateCommandQueue\n");
    if (clCreateCommandQueueFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateCommandQueue is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_device_id nativeDevice = NULL;
    cl_command_queue_properties nativeProperties = 0;
    cl_int nativeErrcode_ret = 0;
    cl_command_queue nativeCommand_queue = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    if (device != NULL)
    {
        nativeDevice = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }
    nativeProperties = (cl_command_queue_properties)properties;

    nativeCommand_queue = (clCreateCommandQueueFP)(nativeContext, nativeDevice, nativeProperties, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeCommand_queue == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_command_queue object
    jobject command_queue = env->NewObject(cl_command_queue_Class, cl_command_queue_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, command_queue, (jlong)nativeCommand_queue);
    return command_queue;
}


// #if defined(CL_VERSION_2_0)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateCommandQueueWithPropertiesNative
 * Signature: (Lorg/jocl/cl_context;Lorg/jocl/cl_device_id;Lorg/jocl/cl_queue_properties;[I)Lorg/jocl/cl_command_queue;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateCommandQueueWithPropertiesNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jobject device, jobject properties, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateCommandQueueWithProperties\n");
    if (clCreateCommandQueueWithPropertiesFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateCommandQueueWithProperties is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_device_id nativeDevice = NULL;
    const cl_queue_properties* nativeProperties = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_command_queue nativeCommand_queue = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    if (device != NULL)
    {
        nativeDevice = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }
    if (properties != NULL)
    {
        nativeProperties = createQueuePropertiesArray(env, properties);
        if (nativeProperties == NULL)
        {
            return NULL;
        }
    }
    nativeCommand_queue = (clCreateCommandQueueWithPropertiesFP)(nativeContext, nativeDevice, nativeProperties, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeProperties;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeCommand_queue == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_command_queue object
    jobject command_queue = env->NewObject(cl_command_queue_Class, cl_command_queue_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, command_queue, (jlong)nativeCommand_queue);
    return command_queue;
}

// #endif // defined(CL_VERSION_2_0)


/*
 * Class:     org_jocl_CL
 * Method:    clRetainCommandQueueNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainCommandQueueNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clRetainCommandQueue\n");
    if (clRetainCommandQueueFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainCommandQueue is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return (clRetainCommandQueueFP)(nativeCommand_queue);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseCommandQueueNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseCommandQueueNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clReleaseCommandQueue\n");
    if (clReleaseCommandQueueFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseCommandQueue is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return (clReleaseCommandQueueFP)(nativeCommand_queue);
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetCommandQueueInfoNative
 * Signature: (Lorg/jocl/cl_command_queue;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetCommandQueueInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetCommandQueueInfo\n");
    if (clGetCommandQueueInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetCommandQueueInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_command_queue_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret = 0;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_command_queue_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetCommandQueueInfoFP)(nativeCommand_queue, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clSetCommandQueuePropertyNative
 * Signature: (Lorg/jocl/cl_command_queue;JZ[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetCommandQueuePropertyNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jlong properties, jboolean enable, jlongArray old_properties)
{
    Logger::log(LOG_TRACE, "Executing clSetCommandQueueProperty\n");
    if (clSetCommandQueuePropertyFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetCommandQueueProperty is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_command_queue_properties nativeProperties = 0;
    cl_bool nativeEnable = CL_FALSE;
    cl_command_queue_properties nativeOld_properties = 0;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeProperties = (cl_command_queue_properties)properties;
    nativeEnable = (cl_bool)enable;

    // TODO: Check this on Java side and remove this native method
    int result = CL_INVALID_OPERATION;
#if defined(CL_VERSION_1_1)
    Logger::log(LOG_ERROR, "clSetCommandQueueProperty is no longer supported in OpenCL 1.1\n");
#else
    result = clSetCommandQueueProperty(nativeCommand_queue, nativeProperties, nativeEnable, &nativeOld_properties);
#endif // defined(CL_VERSION_1_1)

    // Write back native variable values and clean up
    if (!set(env, old_properties, 0, (long)nativeOld_properties)) return CL_OUT_OF_HOST_MEMORY;

    return result;

}




/*
 * Class:     org_jocl_CL
 * Method:    clCreateBufferNative
 * Signature: (Lorg/jocl/cl_context;JJLorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jlong size, jobject host_ptr, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateBuffer\n");
    if (clCreateBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateBuffer is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    size_t nativeSize = 0;
    void *nativeHost_ptr = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_mem nativeMem = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeSize = (size_t)size;
    PointerData *host_ptrPointerData = initPointerData(env, host_ptr);
    if (host_ptrPointerData == NULL)
    {
        return NULL;
    }
    nativeHost_ptr = (void*)host_ptrPointerData->pointer;


    // TODO: Check if all flags are supported - does a global reference
    // to the host_ptr have to be created for CL_MEM_USE_HOST_PTR?
    // Otherwise, the host pointer data may be garbage collected!

    nativeMem = (clCreateBufferFP)(nativeContext, nativeFlags, nativeSize, nativeHost_ptr, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, host_ptrPointerData)) return NULL;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}






//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateSubBufferNative
 * Signature: (Lorg/jocl/cl_mem;JILorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateSubBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject buffer, jlong flags, jint buffer_create_type, jobject buffer_create_info, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateSubBuffer\n");
    if (clCreateSubBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateSubBuffer is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_mem nativeBuffer = NULL;
    cl_mem_flags nativeFlags = 0;
    cl_buffer_create_type nativeBuffer_create_type = 0;
    void *nativeBuffer_create_info = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_mem nativeMem = NULL;

    // Obtain native variable values
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeBuffer_create_type = (cl_buffer_create_type)buffer_create_type;
    PointerData *buffer_create_infoPointerData = initPointerData(env, buffer_create_info);
    if (buffer_create_infoPointerData == NULL)
    {
        return NULL;
    }
    nativeBuffer_create_info = (void*)buffer_create_infoPointerData->pointer;


    // TODO: Check if all flags are supported - does a global reference
    // to the host_ptr have to be created for CL_MEM_USE_HOST_PTR?
    // Otherwise, the host pointer data may be garbage collected!

    nativeMem = (clCreateSubBufferFP)(nativeBuffer, nativeFlags, nativeBuffer_create_type, nativeBuffer_create_info, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, buffer_create_infoPointerData)) return NULL;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}



/*
 * Class:     org_jocl_CL
 * Method:    clCreateSubBuffer2Native
 * Signature: (Lorg/jocl/cl_mem;JILorg/jocl/cl_buffer_region;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateSubBuffer2Native
  (JNIEnv *env, jclass UNUSED(cls), jobject buffer, jlong flags, jint buffer_create_type, jobject buffer_create_info, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateSubBuffer\n");
    if (clCreateSubBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateSubBuffer is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_mem nativeBuffer = NULL;
    cl_mem_flags nativeFlags = 0;
    cl_buffer_create_type nativeBuffer_create_type = 0;
    cl_buffer_region nativeBuffer_create_info;
    cl_int nativeErrcode_ret = 0;
    cl_mem nativeMem = NULL;

    // Obtain native variable values
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeBuffer_create_type = (cl_buffer_create_type)buffer_create_type;
    getCl_buffer_region(env, buffer_create_info, nativeBuffer_create_info);

    // TODO: Check if all flags are supported - does a global reference
    // to the host_ptr have to be created for CL_MEM_USE_HOST_PTR?
    // Otherwise, the host pointer data may be garbage collected!

    nativeMem = (clCreateSubBufferFP)(nativeBuffer, nativeFlags, nativeBuffer_create_type, &nativeBuffer_create_info, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}




//#endif // defined(CL_VERSION_1_1)



// #if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateImageNative
 * Signature: (Lorg/jocl/cl_context;JLorg/jocl/cl_image_format;Lorg/jocl/cl_image_desc;Lorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateImageNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jobject image_format, jobject image_desc, jobject host_ptr, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateImage\n");
    if (clCreateImageFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateImage is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    cl_image_format nativeImage_format;
    cl_image_desc nativeImage_desc;
    void *nativeHost_ptr = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_mem nativeMem = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    getCl_image_format(env, image_format, nativeImage_format);
    getCl_image_desc(env, image_desc, nativeImage_desc);
    PointerData *host_ptrPointerData = initPointerData(env, host_ptr);
    if (host_ptrPointerData == NULL)
    {
        return NULL;
    }
    nativeHost_ptr = (void*)host_ptrPointerData->pointer;

    // TODO: Check if all flags are supported - does a global reference
    // to the host_ptr have to be created for CL_MEM_USE_HOST_PTR?
    // Otherwise, the host pointer data may be garbage collected!
    nativeMem = (clCreateImageFP)(nativeContext, nativeFlags, &nativeImage_format, &nativeImage_desc, nativeHost_ptr, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, host_ptrPointerData)) return NULL;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;

}



//#endif // defined(CL_VERSION_1_2)


// #if defined(CL_VERSION_2_0)

/*
 * Class:     org_jocl_CL
 * Method:    clCreatePipeNative
 * Signature: (Lorg/jocl/cl_context;JIILorg/jocl/cl_pipe_properties;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreatePipeNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jint pipe_packet_size, jint pipe_max_packets, jobject properties, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreatePipe\n");
    if (clCreatePipeFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreatePipe is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    cl_uint nativePipe_packet_size = 0;
    cl_uint nativePipe_max_packets = 0;
    const cl_pipe_properties* nativeProperties = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_mem nativeMem = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativePipe_packet_size = (cl_uint)pipe_packet_size;
    nativePipe_max_packets = (cl_uint)pipe_max_packets;
	if (properties != NULL)
	{
		nativeProperties = createPipePropertiesArray(env, properties);
        if (nativeProperties == NULL)
        {
            return NULL;
        }
	}

    nativeMem = (clCreatePipeFP)(nativeContext, nativeFlags, nativePipe_packet_size, nativePipe_max_packets, nativeProperties, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeProperties;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;


}

// #endif // defined(CL_VERSION_2_0)


/*
 * Class:     org_jocl_CL
 * Method:    clCreateImage2DNative
 * Signature: (Lorg/jocl/cl_context;J[Lorg/jocl/cl_image_format;JJJLorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateImage2DNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jobjectArray image_format, jlong image_width, jlong image_height, jlong image_row_pitch, jobject host_ptr, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateImage2D\n");
    if (clCreateImage2DFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateImage2D is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    cl_image_format *nativeImage_format = NULL;
    size_t nativeImage_width = 0;
    size_t nativeImage_height = 0;
    size_t nativeImage_row_pitch = 0;
    void *nativeHost_ptr = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_mem nativeMem = NULL;


    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    if (image_format != NULL)
    {
        jsize image_formatLength = env->GetArrayLength(image_format);
        nativeImage_format = new cl_image_format[(size_t)image_formatLength];
        if (nativeImage_format == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during image format array creation");
            return NULL;
        }
        for (int i=0; i<image_formatLength; i++)
        {
            jobject format = env->GetObjectArrayElement(image_format, i);
            getCl_image_format(env, format, nativeImage_format[i]);
        }
    }
    nativeImage_width = (size_t)image_width;
    nativeImage_height = (size_t)image_height;
    nativeImage_row_pitch = (size_t)image_row_pitch;
    PointerData *host_ptrPointerData = initPointerData(env, host_ptr);
    if (host_ptrPointerData == NULL)
    {
        return NULL;
    }
    nativeHost_ptr = (void*)host_ptrPointerData->pointer;

    // TODO: Check if all flags are supported - does a global reference
    // to the host_ptr have to be created for CL_MEM_USE_HOST_PTR?
    // Otherwise, the host pointer data may be garbage collected!

    nativeMem = (clCreateImage2DFP)(nativeContext, nativeFlags, nativeImage_format, nativeImage_width, nativeImage_height, nativeImage_row_pitch, nativeHost_ptr, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeImage_format;
    if (!releasePointerData(env, host_ptrPointerData)) return NULL;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}




/*
 * Class:     org_jocl_CL
 * Method:    clCreateImage3DNative
 * Signature: (Lorg/jocl/cl_context;J[Lorg/jocl/cl_image_format;JJJJJLorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateImage3DNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jobjectArray image_format, jlong image_width, jlong image_height, jlong image_depth, jlong image_row_pitch, jlong image_slice_pitch, jobject host_ptr, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateImage3D\n");
    if (clCreateImage3DFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateImage3D is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    cl_image_format *nativeImage_format = NULL;
    size_t nativeImage_width = 0;
    size_t nativeImage_height = 0;
    size_t nativeImage_depth = 0;
    size_t nativeImage_row_pitch = 0;
    size_t nativeImage_slice_pitch = 0;
    void *nativeHost_ptr = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_mem nativeMem = NULL;


    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    if (image_format != NULL)
    {
        jsize image_formatLength = env->GetArrayLength(image_format);
        nativeImage_format = new cl_image_format[(size_t)image_formatLength];
        if (nativeImage_format == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during image format array creation");
            return NULL;
        }
        for (int i=0; i<image_formatLength; i++)
        {
            jobject format = env->GetObjectArrayElement(image_format, i);
            getCl_image_format(env, format, nativeImage_format[i]);
        }
    }
    nativeImage_width = (size_t)image_width;
    nativeImage_height = (size_t)image_height;
    nativeImage_depth = (size_t)image_depth;
    nativeImage_row_pitch = (size_t)image_row_pitch;
    nativeImage_slice_pitch = (size_t)image_slice_pitch;
    PointerData *host_ptrPointerData = initPointerData(env, host_ptr);
    if (host_ptrPointerData == NULL)
    {
        return NULL;
    }
    nativeHost_ptr = (void*)host_ptrPointerData->pointer;

    // TODO: Check if all flags are supported - does a global reference
    // to the host_ptr have to be created for CL_MEM_USE_HOST_PTR?
    // Otherwise, the host pointer data may be garbage collected!

    nativeMem = (clCreateImage3DFP)(nativeContext, nativeFlags, nativeImage_format, nativeImage_width, nativeImage_height, nativeImage_depth, nativeImage_row_pitch, nativeImage_slice_pitch, nativeHost_ptr, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeImage_format;
    if (!releasePointerData(env, host_ptrPointerData)) return NULL;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}



/*
 * Class:     org_jocl_CL
 * Method:    clRetainMemObjectNative
 * Signature: (Lorg/jocl/cl_mem;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainMemObjectNative
  (JNIEnv *env, jclass UNUSED(cls), jobject memobj)
{
    Logger::log(LOG_TRACE, "Executing clRetainMemObject\n");
    if (clRetainMemObjectFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainMemObject is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_mem nativeMemobj = NULL;
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    return (clRetainMemObjectFP)(nativeMemobj);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseMemObjectNative
 * Signature: (Lorg/jocl/cl_mem;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseMemObjectNative
  (JNIEnv *env, jclass UNUSED(cls), jobject memobj)
{
    Logger::log(LOG_TRACE, "Executing clReleaseMemObject\n");
    if (clReleaseMemObjectFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseMemObject is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_mem nativeMemobj = NULL;
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    return (clReleaseMemObjectFP)(nativeMemobj);
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetSupportedImageFormatsNative
 * Signature: (Lorg/jocl/cl_context;JII[Lorg/jocl/cl_image_format;[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetSupportedImageFormatsNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jint image_type, jint num_entries, jobjectArray image_formats, jintArray num_image_formats)
{
    Logger::log(LOG_TRACE, "Executing clGetSupportedImageFormats\n");
    if (clGetSupportedImageFormatsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetSupportedImageFormats is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    cl_mem_object_type nativeImage_type = 0;
    cl_uint nativeNum_entries = 0;
    cl_image_format *nativeImage_formats = NULL;
    cl_uint nativeNum_image_formats = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeImage_type = (cl_mem_object_type)image_type;
    nativeNum_entries = (cl_uint)num_entries;
    if (image_formats != NULL)
    {
        jsize image_formatsLength = env->GetArrayLength(image_formats);
        nativeImage_formats = new cl_image_format[(size_t)image_formatsLength];
        if (nativeImage_formats == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during image formats array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = (clGetSupportedImageFormatsFP)(nativeContext, nativeFlags, nativeImage_type, nativeNum_entries, nativeImage_formats, &nativeNum_image_formats);

    // Write back native variable values and clean up
    if (image_formats != NULL)
    {
        for (size_t i = 0; i<nativeNum_image_formats; i++)
        {
            jobject image_format = env->GetObjectArrayElement(image_formats, (jsize)i);
            if (image_format == NULL)
            {
                image_format = env->NewObject(cl_image_format_Class, cl_image_format_Constructor);
                if (env->ExceptionCheck())
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(image_formats, (jsize)i, image_format);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setCl_image_format(env, image_format, nativeImage_formats[i]);
        }
        delete nativeImage_formats;
    }
    if (!set(env, num_image_formats, 0, (jint)nativeNum_image_formats)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetMemObjectInfoNative
 * Signature: (Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetMemObjectInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject memobj, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetMemObjectInfo\n");
    if (clGetMemObjectInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetMemObjectInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_mem nativeMemobj = NULL;
    cl_mem_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_mem_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetMemObjectInfoFP)(nativeMemobj, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetImageInfoNative
 * Signature: (Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetImageInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject image, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetImageInfo\n");
    if (clGetImageInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetImageInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_mem nativeImage = NULL;
    cl_image_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (image != NULL)
    {
        nativeImage = (cl_mem)env->GetLongField(image, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_image_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetImageInfoFP)(nativeImage, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}


//#if defined(CL_VERSION_2_0)

/*
 * Class:     org_jocl_CL
 * Method:    clGetPipeInfoNative
 * Signature: (Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetPipeInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject pipe, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetPipeInfo\n");
    if (clGetPipeInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetPipeInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_mem nativePipe = NULL;
    cl_pipe_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (pipe != NULL)
    {
        nativePipe = (cl_mem)env->GetLongField(pipe, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_pipe_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetPipeInfoFP)(nativePipe, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}

//#endif // defined(CL_VERSION_2_0)


//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clSetMemObjectDestructorCallbackNative
 * Signature: (Lorg/jocl/cl_mem;Lorg/jocl/MemObjectDestructorCallbackFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetMemObjectDestructorCallbackNative
  (JNIEnv *env, jclass UNUSED(cls), jobject memobj, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clSetMemObjectDestructorCallback\n");
    if (clSetMemObjectDestructorCallbackFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetMemObjectDestructorCallback is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_mem nativeMemobj = NULL;
    MemObjectDestructorCallbackFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;

    // Obtain native variable values
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    if (pfn_notify != NULL)
    {
        nativePfn_notify = &MemObjectDestructorCallback;
        CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
        if (callbackInfo == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
        nativeUser_data = (void*)callbackInfo;
    }

    int result = (clSetMemObjectDestructorCallbackFP)(nativeMemobj, nativePfn_notify, nativeUser_data);

    return result;
}

//#endif // defined(CL_VERSION_1_1)



//#if defined(CL_VERSION_2_0)

/*
 * Class:     org_jocl_CL
 * Method:    clSVMAllocNative
 * Signature: (Lorg/jocl/cl_context;JJI)Lorg/jocl/Pointer;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clSVMAllocNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jlong size, jint alignment)
{
    Logger::log(LOG_TRACE, "Executing clSVMAlloc\n");
    if (clSVMAllocFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSVMAlloc is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    size_t nativeSize = 0;
    cl_uint nativeAlignment = 0;
    void *nativePointer = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeSize = (size_t)size;
    nativeAlignment = (cl_uint)alignment;

    nativePointer = (clSVMAllocFP)(nativeContext, nativeFlags, nativeSize, nativeAlignment);

    if (nativePointer == NULL)
    {
        return NULL;
    }

	return createJavaPointerObject(env, nativePointer, nativeSize);
}


/*
 * Class:     org_jocl_CL
 * Method:    clSVMFreeNative
 * Signature: (Lorg/jocl/cl_context;Lorg/jocl/Pointer;)V
 */
JNIEXPORT void JNICALL Java_org_jocl_CL_clSVMFreeNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jobject svm_pointer)
{
    Logger::log(LOG_TRACE, "Executing clSVMFree\n");
    if (clSVMFreeFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSVMFree is not supported");
        return;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
	void *nativeSvm_pointer = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
	if (svm_pointer != NULL)
	{
		nativeSvm_pointer = (void*)env->GetLongField(svm_pointer, NativePointerObject_nativePointer);
	}

    (clSVMFreeFP)(nativeContext, nativeSvm_pointer);

	if (svm_pointer != NULL)
	{
	    env->SetObjectField(svm_pointer, NativePointerObject_buffer, NULL);
	    env->SetObjectField(svm_pointer, NativePointerObject_pointers, NULL);
	    env->SetLongField(svm_pointer, NativePointerObject_byteOffset, 0);
	    env->SetLongField(svm_pointer, NativePointerObject_nativePointer, 0);
	}
}

/*
 * Class:     org_jocl_CL
 * Method:    clCreateSamplerWithPropertiesNative
 * Signature: (Lorg/jocl/cl_context;Lorg/jocl/cl_sampler_properties;[I)Lorg/jocl/cl_sampler;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateSamplerWithPropertiesNative
  (JNIEnv *env, jclass cls, jobject context, jobject properties, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateSamplerWithProperties\n");
    if (clCreateSamplerWithPropertiesFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateSamplerWithProperties is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    const cl_sampler_properties* nativeProperties = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_sampler nativeSampler = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    if (properties != NULL)
    {
        nativeProperties = createSamplerPropertiesArray(env, properties);
        if (nativeProperties == NULL)
        {
            return NULL;
        }
    }
    nativeSampler = (clCreateSamplerWithPropertiesFP)(nativeContext, nativeProperties, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeProperties;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeSampler == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_sampler object
    jobject sampler = env->NewObject(cl_sampler_Class, cl_sampler_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, sampler, (jlong)nativeSampler);
    return sampler;

}

//#endif // defined(CL_VERSION_2_0)






/*
 * Class:     org_jocl_CL
 * Method:    clCreateSamplerNative
 * Signature: (Lorg/jocl/cl_context;ZII[I)Lorg/jocl/cl_sampler;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateSamplerNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jboolean normalized_coords, jint addressing_mode, jint filter_mode, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateSampler\n");
    if (clCreateSamplerFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateSampler is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_bool nativeNormalized_coords = CL_FALSE;
    cl_addressing_mode nativeAddressing_mode = 0;
    cl_filter_mode nativeFilter_mode = 0;
    cl_int nativeErrcode_ret = 0;
    cl_sampler nativeSampler = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeNormalized_coords = (cl_bool)normalized_coords;
    nativeAddressing_mode = (cl_addressing_mode)addressing_mode;
    nativeFilter_mode = (cl_filter_mode)filter_mode;

    nativeSampler = (clCreateSamplerFP)(nativeContext, nativeNormalized_coords, nativeAddressing_mode, nativeFilter_mode, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeSampler == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_sampler object
    jobject sampler = env->NewObject(cl_sampler_Class, cl_sampler_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, sampler, (jlong)nativeSampler);
    return sampler;

}



/*
 * Class:     org_jocl_CL
 * Method:    clRetainSamplerNative
 * Signature: (Lorg/jocl/cl_sampler;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainSamplerNative
  (JNIEnv *env, jclass UNUSED(cls), jobject sampler)
{
    Logger::log(LOG_TRACE, "Executing clRetainSampler\n");
    if (clRetainSamplerFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainSampler is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_sampler nativeSampler = NULL;
    if (sampler != NULL)
    {
        nativeSampler = (cl_sampler)env->GetLongField(sampler, NativePointerObject_nativePointer);
    }
    return (clRetainSamplerFP)(nativeSampler);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseSamplerNative
 * Signature: (Lorg/jocl/cl_sampler;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseSamplerNative
  (JNIEnv *env, jclass UNUSED(cls), jobject sampler)
{
    Logger::log(LOG_TRACE, "Executing clReleaseSampler\n");
    if (clReleaseSamplerFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseSampler is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_sampler nativeSampler = NULL;
    if (sampler != NULL)
    {
        nativeSampler = (cl_sampler)env->GetLongField(sampler, NativePointerObject_nativePointer);
    }
    return (clReleaseSamplerFP)(nativeSampler);
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetSamplerInfoNative
 * Signature: (Lorg/jocl/cl_sampler;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetSamplerInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject sampler, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetSamplerInfo\n");
    if (clGetSamplerInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetSamplerInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_sampler nativeSampler = NULL;
    cl_sampler_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (sampler != NULL)
    {
        nativeSampler = (cl_sampler)env->GetLongField(sampler, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_sampler_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetSamplerInfoFP)(nativeSampler, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clCreateProgramWithSourceNative
 * Signature: (Lorg/jocl/cl_context;I[Ljava/lang/String;[J[I)Lorg/jocl/cl_program;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateProgramWithSourceNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jint count, jobjectArray strings, jlongArray lengths, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateProgramWithSource\n");
    if (clCreateProgramWithSourceFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateProgramWithSource is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_uint nativeCount = 0;
    char **nativeStrings = NULL;
    size_t *nativeLengths = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_program nativeProgram = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeCount = (cl_uint)count;
    if (strings != NULL)
    {
        jsize stringsLength = env->GetArrayLength(strings);
        nativeStrings = new char*[(size_t)stringsLength];
        if (nativeStrings == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during program strings array creation");
            return NULL;
        }

        for (int i=0; i<stringsLength; i++)
        {
            jstring js = (jstring)env->GetObjectArrayElement(strings, i);
            if (js != NULL)
            {
                char *s = convertString(env, js);
                if (s == NULL)
                {
                    return NULL;
                }
                nativeStrings[i] = s;
            }
            else
            {
                nativeStrings[i] = NULL;
            }
        }
    }
    if (lengths != NULL)
    {
        nativeLengths = convertArray(env, lengths);
        if (nativeLengths == NULL)
        {
            return NULL;
        }
    }

    nativeProgram = (clCreateProgramWithSourceFP)(nativeContext, nativeCount, (const char**)nativeStrings, nativeLengths, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (strings != NULL)
    {
        jsize stringsLength = env->GetArrayLength(strings);
        for (int i=0; i<stringsLength; i++)
        {
            delete[] nativeStrings[i];
        }
        delete[] nativeStrings;
    }
    delete[] nativeLengths;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeProgram == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_program object
    jobject program = env->NewObject(cl_program_Class, cl_program_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, program, (jlong)nativeProgram);
    return program;
}


/*
 * Class:     org_jocl_CL
 * Method:    clCreateProgramWithBinaryNative
 * Signature: (Lorg/jocl/cl_context;I[Lorg/jocl/cl_device_id;[J[[B[I[I)Lorg/jocl/cl_program;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateProgramWithBinaryNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jint num_devices, jobjectArray device_list, jlongArray lengths, jobjectArray binaries, jintArray binary_status, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateProgramWithBinary\n");
    if (clCreateProgramWithBinaryFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateProgramWithBinary is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_uint nativeNum_devices = 0;
    cl_device_id *nativeDevice_list = NULL;
    size_t *nativeLengths = NULL;
    unsigned char **nativeBinaries = NULL;
    cl_int nativeBinary_status = 0;
    cl_int nativeErrcode_ret = 0;
    cl_program nativeProgram = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeNum_devices = (cl_uint)num_devices;
    if (device_list != NULL)
    {
        nativeDevice_list = createDeviceList(env, device_list, nativeNum_devices);
        if (nativeDevice_list == NULL)
        {
            return NULL;
        }
    }
    if (lengths != NULL)
    {
        nativeLengths = convertArray(env, lengths);
        if (nativeLengths == NULL)
        {
            return NULL;
        }
    }

    if (binaries != NULL)
    {
        jsize binariesLength = env->GetArrayLength(binaries);
        nativeBinaries = new unsigned char*[(size_t)binariesLength];
        if (nativeBinaries == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during binaries array creation");
            return NULL;
        }

        for (int i=0; i<binariesLength; i++)
        {
            jbyteArray binary = (jbyteArray)env->GetObjectArrayElement(binaries, i);
            if (binary != NULL)
            {
                jsize binaryLength = env->GetArrayLength(binary);
                unsigned char *nativeBinary = new unsigned char[(size_t)binaryLength];
                if (nativeBinary == NULL)
                {
                    ThrowByName(env, "java/lang/OutOfMemoryError",
                        "Out of memory during binary array creation");
                    return NULL;
                }
                unsigned char *binaryArray = (unsigned char*)env->GetPrimitiveArrayCritical(binary, NULL);
                if (binaryArray == NULL)
                {
                    return NULL;
                }
                for (int j=0; j<binaryLength; j++)
                {
                    nativeBinary[j] = binaryArray[j];
                }
                env->ReleasePrimitiveArrayCritical(binary, binaryArray, JNI_ABORT);
                nativeBinaries[i] = nativeBinary;
            }
        }
    }

    nativeProgram = (clCreateProgramWithBinaryFP)(nativeContext, nativeNum_devices, nativeDevice_list, nativeLengths, (const unsigned char**)nativeBinaries, &nativeBinary_status, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeDevice_list;
    delete[] nativeLengths;
    if (binaries != NULL)
    {
        jsize binariesLength = env->GetArrayLength(binaries);
        for (int i=0; i<binariesLength; i++)
        {
            delete[] nativeBinaries[i];
        }
        delete[] nativeBinaries;
    }
    if (!set(env, binary_status, 0, nativeBinary_status)) return NULL;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeProgram == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_program object
    jobject program = env->NewObject(cl_program_Class, cl_program_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, program, (jlong)nativeProgram);
    return program;
}


//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateProgramWithBuiltInKernelsNative
 * Signature: (Lorg/jocl/cl_context;I[Lorg/jocl/cl_device_id;Ljava/lang/String;[I)Lorg/jocl/cl_program;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateProgramWithBuiltInKernelsNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jint num_devices, jobjectArray device_list, jstring kernel_names, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateProgramWithBuiltInKernels\n");
    if (clCreateProgramWithBuiltInKernelsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateProgramWithBuiltInKernels is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_uint nativeNum_devices = 0;
    cl_device_id *nativeDevice_list = NULL;
    char *nativeKernel_names = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_program nativeProgram = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeNum_devices = (cl_uint)num_devices;
    if (device_list != NULL)
    {
        nativeDevice_list = createDeviceList(env, device_list, nativeNum_devices);
        if (nativeDevice_list == NULL)
        {
            return NULL;
        }
    }
    if (kernel_names != NULL)
    {
        nativeKernel_names = convertString(env, kernel_names);
    }

    nativeProgram = (clCreateProgramWithBuiltInKernelsFP)(nativeContext, nativeNum_devices, nativeDevice_list, nativeKernel_names, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeDevice_list;
    delete[] nativeKernel_names;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeProgram == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_program object
    jobject program = env->NewObject(cl_program_Class, cl_program_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, program, (jlong)nativeProgram);
    return program;

}

//#endif // defined(CL_VERSION_1_2)



/*
 * Class:     org_jocl_CL
 * Method:    clRetainProgramNative
 * Signature: (Lorg/jocl/cl_program;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainProgramNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program)
{
    Logger::log(LOG_TRACE, "Executing clRetainProgram\n");
    if (clRetainProgramFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainProgram is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_program nativeProgram = NULL;
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    return (clRetainProgramFP)(nativeProgram);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseProgramNative
 * Signature: (Lorg/jocl/cl_program;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseProgramNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program)
{
    Logger::log(LOG_TRACE, "Executing clReleaseProgram\n");
    if (clReleaseProgramFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseProgram is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_program nativeProgram = NULL;
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    return (clReleaseProgramFP)(nativeProgram);
}




/*
 * Class:     org_jocl_CL
 * Method:    clBuildProgramNative
 * Signature: (Lorg/jocl/cl_program;I[Lorg/jocl/cl_device_id;Ljava/lang/String;Lorg/jocl/BuildProgramFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clBuildProgramNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program, jint num_devices, jobjectArray device_list, jstring options, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clBuildProgram\n");
    if (clBuildProgramFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clBuildProgram is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_program nativeProgram = NULL;
    cl_uint nativeNum_devices = 0;
    cl_device_id *nativeDevice_list = NULL;
    const char *nativeOptions = NULL;
    BuildProgramFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;

    // Obtain native variable values
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    nativeNum_devices = (cl_uint)num_devices;
    if (device_list != NULL)
    {
        nativeDevice_list = createDeviceList(env, device_list, nativeNum_devices);
        if (nativeDevice_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (options != NULL)
    {
        nativeOptions = convertString(env, options);
        if (nativeOptions == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (pfn_notify != NULL)
    {
        nativePfn_notify = &BuildProgramFunction;
        CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
        if (callbackInfo == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
        nativeUser_data = (void*)callbackInfo;
    }

    int result = (clBuildProgramFP)(nativeProgram, nativeNum_devices, nativeDevice_list, nativeOptions, nativePfn_notify, nativeUser_data);

    // Write back native variable values and clean up
    delete[] nativeDevice_list;
    delete[] nativeOptions;

    return result;
}




//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clCompileProgramNative
 * Signature: (Lorg/jocl/cl_program;I[Lorg/jocl/cl_device_id;Ljava/lang/String;I[Lorg/jocl/cl_program;[Ljava/lang/String;Lorg/jocl/BuildProgramFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clCompileProgramNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program, jint num_devices, jobjectArray device_list, jstring options, jint num_input_headers, jobjectArray input_headers, jobjectArray header_include_names, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clCompileProgram\n");
    if (clCompileProgramFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCompileProgram is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_program nativeProgram = NULL;
    cl_uint nativeNum_devices = 0;
    cl_device_id *nativeDevice_list = NULL;
    const char *nativeOptions = NULL;
    cl_uint nativeNum_input_headers;
    cl_program *nativeInput_headers = NULL;
    const char **nativeHeader_include_names = NULL;
    BuildProgramFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;

    // Obtain native variable values
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    nativeNum_devices = (cl_uint)num_devices;
    if (device_list != NULL)
    {
        nativeDevice_list = createDeviceList(env, device_list, nativeNum_devices);
        if (nativeDevice_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (options != NULL)
    {
        nativeOptions = convertString(env, options);
        if (nativeOptions == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeNum_input_headers = (cl_uint)num_input_headers;
    if (input_headers != NULL)
    {
        nativeInput_headers = createProgramList(env, input_headers, nativeNum_input_headers);
        if (nativeInput_headers == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (header_include_names != NULL)
    {
        jsize header_include_namesLength = env->GetArrayLength(header_include_names);
        nativeHeader_include_names = new const char*[(size_t)header_include_namesLength];
        if (nativeHeader_include_names == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during string array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }

        for (int i=0; i<header_include_namesLength; i++)
        {
            jobject header_include_name = env->GetObjectArrayElement(header_include_names, i);
            if (env->ExceptionCheck())
            {
                return CL_OUT_OF_HOST_MEMORY;
            }
            if (header_include_name != NULL)
            {
                nativeHeader_include_names[i] = convertString(env, (jstring)header_include_name);
            }
        }
    }
    if (pfn_notify != NULL)
    {
        nativePfn_notify = &BuildProgramFunction;
        CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
        if (callbackInfo == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
        nativeUser_data = (void*)callbackInfo;
    }

    int result = (clCompileProgramFP)(nativeProgram, nativeNum_devices, nativeDevice_list, nativeOptions, nativeNum_input_headers, nativeInput_headers, nativeHeader_include_names, nativePfn_notify, nativeUser_data);

    // Write back native variable values and clean up
    delete[] nativeDevice_list;
    delete[] nativeOptions;
    delete[] nativeInput_headers;
    if (header_include_names != NULL)
    {
        jsize header_include_namesLength = env->GetArrayLength(header_include_names);
        for (int i=0; i<header_include_namesLength; i++)
        {
            delete[] nativeHeader_include_names[i];
        }
    }

    return result;
}

//#endif // defined(CL_VERSION_1_2)



//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clLinkProgramNative
 * Signature: (Lorg/jocl/cl_context;I[Lorg/jocl/cl_device_id;Ljava/lang/String;I[Lorg/jocl/cl_program;Lorg/jocl/BuildProgramFunction;Ljava/lang/Object;[I)Lorg/jocl/cl_program;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clLinkProgramNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jint num_devices, jobjectArray devices_list, jstring options, jint num_input_programs, jobjectArray input_programs, jobject pfn_notify, jobject user_data, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clLinkProgram\n");
    if (clLinkProgramFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clLinkProgram is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_uint nativeNum_devices = 0;
    cl_device_id *nativeDevices_list = NULL;
    const char *nativeOptions = NULL;
    cl_uint nativeNum_input_programs;
    cl_program *nativeInput_programs = NULL;
    BuildProgramFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;
    cl_int nativeErrcode_ret;
    cl_program nativeProgram = NULL;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeNum_devices = (cl_uint)num_devices;
    if (devices_list != NULL)
    {
        nativeDevices_list = createDeviceList(env, devices_list, nativeNum_devices);
        if (nativeDevices_list == NULL)
        {
            return NULL;
        }
    }
    if (options != NULL)
    {
        nativeOptions = convertString(env, options);
        if (nativeOptions == NULL)
        {
            return NULL;
        }
    }
    nativeNum_input_programs = (cl_uint)num_input_programs;
    if (input_programs != NULL)
    {
        nativeInput_programs = createProgramList(env, input_programs, nativeNum_input_programs);
        if (nativeInput_programs == NULL)
        {
            return NULL;
        }
    }
    if (pfn_notify != NULL)
    {
        nativePfn_notify = &BuildProgramFunction;
        CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
        if (callbackInfo == NULL)
        {
            return NULL;
        }
        nativeUser_data = (void*)callbackInfo;
    }

    nativeProgram = (clLinkProgramFP)(nativeContext, nativeNum_devices, nativeDevices_list, nativeOptions, nativeNum_input_programs, nativeInput_programs, nativePfn_notify, nativeUser_data, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeDevices_list;
    delete[] nativeOptions;
    delete[] nativeInput_programs;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeProgram == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_program object
    jobject program = env->NewObject(cl_program_Class, cl_program_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, program, (jlong)nativeProgram);
    return program;
}


//#endif // defined(CL_VERSION_1_2)


//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clUnloadPlatformCompilerNative
 * Signature: (Lorg/jocl/cl_platform_id;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clUnloadPlatformCompilerNative
  (JNIEnv *env, jclass UNUSED(cls), jobject platform)
{
    Logger::log(LOG_TRACE, "Executing clUnloadPlatformCompiler\n");
    if (clUnloadPlatformCompilerFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clUnloadPlatformCompiler is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_platform_id nativePlatform = NULL;

    // Obtain native variable values
    if (platform != NULL)
    {
        nativePlatform = (cl_platform_id)env->GetLongField(platform, NativePointerObject_nativePointer);
    }

    return (clUnloadPlatformCompilerFP)(nativePlatform);
}

//#endif // defined(CL_VERSION_1_2)


/*
 * Class:     org_jocl_CL
 * Method:    clUnloadCompilerNative
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clUnloadCompilerNative
  (JNIEnv *env, jclass UNUSED(cls))
{
    Logger::log(LOG_TRACE, "Executing clUnloadCompiler\n");
    if (clUnloadCompilerFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clUnloadCompiler is not supported");
        return CL_INVALID_OPERATION;
    }

    // Somehow I like this method, but I don't know why...
    return (clUnloadCompilerFP)();
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetProgramInfoNative
 * Signature: (Lorg/jocl/cl_program;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetProgramInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetProgramInfo\n");
    if (clGetProgramInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetProgramInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_program nativeProgram = NULL;
    cl_program_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_program_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetProgramInfoFP)(nativeProgram, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetProgramBuildInfoNative
 * Signature: (Lorg/jocl/cl_program;Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetProgramBuildInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program, jobject device, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetProgramBuildInfo\n");
    if (clGetProgramBuildInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetProgramBuildInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_program nativeProgram = NULL;
    cl_device_id nativeDevice = NULL;
    cl_program_build_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    if (device != NULL)
    {
        nativeDevice = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_program_build_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetProgramBuildInfoFP)(nativeProgram, nativeDevice, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clCreateKernelNative
 * Signature: (Lorg/jocl/cl_program;Ljava/lang/String;[I)Lorg/jocl/cl_kernel;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateKernelNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program, jstring kernel_name, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateKernel\n");
    if (clCreateKernelFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateKernel is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_program nativeProgram = NULL;
    char *nativeKernel_name = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_kernel nativeKernel = NULL;

    // Obtain native variable values
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    if (kernel_name != NULL)
    {
        nativeKernel_name = convertString(env, kernel_name);
        if (nativeKernel_name == NULL)
        {
            return NULL;
        }
    }


    nativeKernel = (clCreateKernelFP)(nativeProgram, nativeKernel_name, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeKernel_name;
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeKernel == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_kernel object
    jobject kernel = env->NewObject(cl_kernel_Class, cl_kernel_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, kernel, (jlong)nativeKernel);
    return kernel;

}

/*
 * Class:     org_jocl_CL
 * Method:    clCreateKernelsInProgramNative
 * Signature: (Lorg/jocl/cl_program;I[Lorg/jocl/cl_kernel;[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clCreateKernelsInProgramNative
  (JNIEnv *env, jclass UNUSED(cls), jobject program, jint num_kernels, jobjectArray kernels, jintArray num_kernels_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateKernelsInProgram\n");
    if (clCreateKernelsInProgramFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateKernelsInProgram is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_program nativeProgram = NULL;
    cl_uint nativeNum_kernels = 0;
    cl_kernel *nativeKernels = NULL;
    cl_uint nativeNum_kernels_ret = 0;

    // Obtain native variable values
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    nativeNum_kernels = (cl_uint)num_kernels;
    if (kernels != NULL)
    {
        nativeKernels = new cl_kernel[nativeNum_kernels];
        if (nativeKernels == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during kernels array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = (clCreateKernelsInProgramFP)(nativeProgram, nativeNum_kernels, nativeKernels, &nativeNum_kernels_ret);

    // Write back native variable values and clean up
    if (kernels != NULL)
    {
        for (size_t i = 0; i<nativeNum_kernels_ret; i++)
        {
            jobject kernel = env->GetObjectArrayElement(kernels, (jsize)i);
            if (kernel == NULL)
            {
                kernel = env->NewObject(cl_kernel_Class, cl_kernel_Constructor);
                if (env->ExceptionCheck())
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(kernels, (jsize)i, kernel);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setNativePointer(env, kernel, (jlong)nativeKernels[i]);
        }
        delete[] nativeKernels;
    }
    if (!set(env, num_kernels_ret, 0, (jint)nativeNum_kernels_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clRetainKernelNative
 * Signature: (Lorg/jocl/cl_kernel;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainKernelNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel)
{
    Logger::log(LOG_TRACE, "Executing clRetainKernel\n");
    if (clRetainKernelFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainKernel is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_kernel nativeKernel = NULL;
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    return (clRetainKernelFP)(nativeKernel);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseKernelNative
 * Signature: (Lorg/jocl/cl_kernel;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseKernelNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel)
{
    Logger::log(LOG_TRACE, "Executing clReleaseKernel\n");
    if (clReleaseKernelFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseKernel is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_kernel nativeKernel = NULL;
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    return (clReleaseKernelFP)(nativeKernel);
}




/*
 * Class:     org_jocl_CL
 * Method:    clSetKernelArgNative
 * Signature: (Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetKernelArgNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel, jint arg_index, jlong arg_size, jobject arg_value)
{
    Logger::log(LOG_TRACE, "Executing clSetKernelArg\n");
    if (clSetKernelArgFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetKernelArg is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_kernel nativeKernel = NULL;
    cl_uint nativeArg_index = 0;
    size_t nativeArg_size = 0;
    void *nativeArg_value;

    // Obtain native variable values
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    nativeArg_index = (cl_uint)arg_index;
    nativeArg_size = (size_t)arg_size;
    PointerData *arg_valuePointerData = initPointerData(env, arg_value);
    if (arg_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeArg_value = (void*)arg_valuePointerData->pointer;

    int result = (clSetKernelArgFP)(nativeKernel, nativeArg_index, nativeArg_size, nativeArg_value);

    // Write back native variable values and clean up
    if (!releasePointerData(env, arg_valuePointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;

    return result;
}

//#if defined(CL_VERSION_2_0)

/*
 * Class:     org_jocl_CL
 * Method:    clSetKernelArgSVMPointerNative
 * Signature: (Lorg/jocl/cl_kernel;ILorg/jocl/Pointer;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetKernelArgSVMPointerNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel, jint arg_index, jobject arg_value)
{
    Logger::log(LOG_TRACE, "Executing clSetKernelArgSVMPointer\n");
    if (clSetKernelArgSVMPointerFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetKernelArgSVMPointer is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_kernel nativeKernel = NULL;
    cl_uint nativeArg_index = 0;
    void *nativeArg_value;

    // Obtain native variable values
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    nativeArg_index = (cl_uint)arg_index;
    PointerData *arg_valuePointerData = initPointerData(env, arg_value);
    if (arg_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeArg_value = (void*)arg_valuePointerData->pointer;

    int result = (clSetKernelArgSVMPointerFP)(nativeKernel, nativeArg_index, nativeArg_value);

    // Write back native variable values and clean up
    if (!releasePointerData(env, arg_valuePointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clSetKernelExecInfoNative
 * Signature: (Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetKernelExecInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel, jint param_name, jlong param_value_size, jobject param_value)
{
    Logger::log(LOG_TRACE, "Executing clSetKernelExecInfo\n");
    if (clSetKernelExecInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetKernelExecInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_kernel nativeKernel = NULL;
	cl_kernel_exec_info nativeParam_name = 0;
	size_t nativeParam_value_size;
    void *nativeParam_value;

    // Obtain native variable values
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_kernel_exec_info)param_name;
	nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clSetKernelExecInfoFP)(nativeKernel, nativeParam_name, nativeParam_value_size, nativeParam_value);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;

    return result;
}

//#endif // defined(CL_VERSION_2_0)


/*
 * Class:     org_jocl_CL
 * Method:    clGetKernelInfoNative
 * Signature: (Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetKernelInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetKernelInfo\n");
    if (clGetKernelInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetKernelInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_kernel nativeKernel = NULL;
    cl_kernel_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_kernel_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetKernelInfoFP)(nativeKernel, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}

//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clGetKernelArgInfoNative
 * Signature: (Lorg/jocl/cl_kernel;IIJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetKernelArgInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel, jint arg_idx, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetKernelArgInfo\n");
    if (clGetKernelArgInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetKernelArgInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_kernel nativeKernel = NULL;
    cl_uint nativeArg_idx = 0;
    cl_kernel_arg_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    nativeArg_idx = (cl_uint)arg_idx;
    nativeParam_name = (cl_kernel_arg_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetKernelArgInfoFP)(nativeKernel, nativeArg_idx, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}

//#endif // defined(CL_VERSION_1_2)



/*
 * Class:     org_jocl_CL
 * Method:    clGetKernelWorkGroupInfoNative
 * Signature: (Lorg/jocl/cl_kernel;Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetKernelWorkGroupInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject kernel, jobject device, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetKernelWorkGroupInfo\n");
    if (clGetKernelWorkGroupInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetKernelWorkGroupInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_kernel nativeKernel = NULL;
    cl_device_id nativeDevice = NULL;
    cl_kernel_work_group_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    if (device != NULL)
    {
        nativeDevice = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_kernel_work_group_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetKernelWorkGroupInfoFP)(nativeKernel, nativeDevice, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clWaitForEventsNative
 * Signature: (I[Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clWaitForEventsNative
  (JNIEnv *env, jclass UNUSED(cls), jint num_events, jobjectArray event_list)
{
    Logger::log(LOG_TRACE, "Executing clWaitForEvents\n");
    if (clWaitForEventsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clWaitForEvents is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_uint nativeNum_events = 0;
    cl_event *nativeEvent_list = NULL;

    // Obtain native variable values
    nativeNum_events = (cl_uint)num_events;
    if (event_list != NULL)
    {
        nativeEvent_list = createEventList(env, event_list, nativeNum_events);
        if (nativeEvent_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = (clWaitForEventsFP)(nativeNum_events, nativeEvent_list);

    // Write back native variable values and clean up
    delete[] nativeEvent_list;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetEventInfoNative
 * Signature: (Lorg/jocl/cl_event;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetEventInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject event, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetEventInfo\n");
    if (clGetEventInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetEventInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_event nativeEvent = NULL;
    cl_event_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_event_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetEventInfoFP)(nativeEvent, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}



//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateUserEventNative
 * Signature: (Lorg/jocl/cl_context;[I)Lorg/jocl/cl_event;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateUserEventNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateUserEvent\n");
    if (clCreateUserEventFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateUserEvent is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_int nativeErrcode_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }

    cl_event nativeEvent = (clCreateUserEventFP)(nativeContext, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    // Create the event object which will be returned
    jobject eventObject = env->NewObject(cl_event_Class, cl_event_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }
    setNativePointer(env, eventObject, (jlong)nativeEvent);

    return eventObject;
}

//#endif // defined(CL_VERSION_1_1)




/*
 * Class:     org_jocl_CL
 * Method:    clRetainEventNative
 * Signature: (Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainEventNative
  (JNIEnv *env, jclass UNUSED(cls), jobject event)
{
    Logger::log(LOG_TRACE, "Executing clRetainEvent\n");
    if (clRetainEventFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clRetainEvent is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_event nativeEvent = NULL;
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    return (clRetainEventFP)(nativeEvent);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseEventNative
 * Signature: (Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseEventNative
  (JNIEnv *env, jclass UNUSED(cls), jobject event)
{
    Logger::log(LOG_TRACE, "Executing clReleaseEvent\n");
    if (clReleaseEventFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clReleaseEvent is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_event nativeEvent = NULL;
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    return (clReleaseEventFP)(nativeEvent);
}


//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clSetUserEventStatusNative
 * Signature: (Lorg/jocl/cl_event;I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetUserEventStatusNative
  (JNIEnv *env, jclass UNUSED(cls), jobject event, jint execution_status)
{
    Logger::log(LOG_TRACE, "Executing clSetUserEventStatus\n");
    if (clSetUserEventStatusFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetUserEventStatus is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_event nativeEvent = NULL;
    cl_int nativeExecution_status = 0;

    // Obtain native variable values
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    nativeExecution_status = (cl_int)execution_status;

    return (clSetUserEventStatusFP)(nativeEvent, nativeExecution_status);
}

//#endif // defined(CL_VERSION_1_1)



//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clSetEventCallbackNative
 * Signature: (Lorg/jocl/cl_event;ILorg/jocl/EventCallbackFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetEventCallbackNative
  (JNIEnv *env, jclass UNUSED(cls), jobject event, jint command_exec_callback_type, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clSetEventCallback\n");
    if (clSetEventCallbackFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetEventCallback is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_event nativeEvent = NULL;
    cl_int nativeCommand_exec_callback_type = 0;
    EventCallbackFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;

    // Obtain native variable values
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    nativeCommand_exec_callback_type = (cl_int)command_exec_callback_type;
    if (pfn_notify != NULL)
    {
        nativePfn_notify = &EventCallback;
        CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
        if (callbackInfo == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
        nativeUser_data = (void*)callbackInfo;
    }

    int result = (clSetEventCallbackFP)(nativeEvent, nativeCommand_exec_callback_type, nativePfn_notify, nativeUser_data);

    return result;
}

//#endif // defined(CL_VERSION_1_1)







/*
 * Class:     org_jocl_CL
 * Method:    clGetEventProfilingInfoNative
 * Signature: (Lorg/jocl/cl_event;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetEventProfilingInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject event, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetEventProfilingInfo\n");
    if (clGetEventProfilingInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetEventProfilingInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_event nativeEvent = NULL;
    cl_profiling_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_profiling_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetEventProfilingInfoFP)(nativeEvent, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clFlushNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clFlushNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clFlush\n");
    if (clFlushFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clFlush is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return (clFlushFP)(nativeCommand_queue);
}




/*
 * Class:     org_jocl_CL
 * Method:    clFinishNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clFinishNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clFinish\n");
    if (clFinishFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clFinish is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return (clFinishFP)(nativeCommand_queue);
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueReadBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
//
// If blocking_read is true, event may not be null.
// This is usually asserted on Java side.
//
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueReadBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject buffer, jboolean blocking_read, jlong offset, jlong cb, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReadBuffer\n");
    if (clEnqueueReadBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueReadBuffer is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeBuffer = NULL;
    cl_bool nativeBlocking_read = CL_TRUE;
    size_t nativeOffset = 0;
    size_t nativeCb = 0;
    void *nativePtr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }
    nativeBlocking_read = (cl_bool)blocking_read;
    nativeOffset = (size_t)offset;
    nativeCb = (size_t)cb;
    PointerData *ptrPointerData = initPointerData(env, ptr);
    if (ptrPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativePtr = (void*)ptrPointerData->pointer;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueReadBufferFP)(nativeCommand_queue, nativeBuffer, nativeBlocking_read, nativeOffset, nativeCb, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    /* See notes about NON_BLOCKING_READ at end of file
    if (nativeBlocking_read)
    {
        if (!releasePointerData(env, ptrPointerData)) return CL_INVALID_HOST_PTR;
    }
    else
    {
        Logger::log(LOG_ERROR, "Storing pending release of pointer data for event %p\n", nativeEvent);
        pendingPointerDataMap[nativeEvent] = ptrPointerData;
    }
    */


    if (!releasePointerData(env, ptrPointerData)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}


/*
 * Class:     org_jocl_CL
 * Method:    releasePendingPointerDataNative
 * Signature: (Lorg/jocl/cl_event;)V
 */
//
// The event may not be null.
// This is usually asserted on Java side.
//
// See notes about NON_BLOCKING_READ at end of file
JNIEXPORT void JNICALL Java_org_jocl_CL_releasePendingPointerDataNative
  (JNIEnv *env, jclass UNUSED(cls), jobject event)
{
/*
    Logger::log(LOG_ERROR, "Executing releasePendingPointerData\n");
    if (releasePendingPointerDataFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function releasePendingPointerData is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_event nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);

    std::map<cl_event, PointerData*>::iterator iter =
        pendingPointerDataMap.find(nativeEvent);
    if (iter != pendingPointerDataMap.end())
    {
        pendingPointerDataMap.erase(iter);
        if (!releasePointerData(env, iter->second))
        {
            ThrowByName(env, "java/lang/RuntimeException", "Failed to release pointer data");
        }
    }
    else
    {
        // Should never happen
        Logger::log(LOG_ERROR, "Could not find pointer data for event %p\n", nativeEvent);
    }
*/
}





//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueReadBufferRectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueReadBufferRectNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject buffer, jboolean blocking_read, jlongArray buffer_offset, jlongArray host_offset, jlongArray region, jlong buffer_row_pitch, jlong buffer_slice_pitch, jlong host_row_pitch, jlong host_slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReadBufferRect\n");
    if (clEnqueueReadBufferRectFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueReadBufferRect is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeBuffer = NULL;
    cl_bool nativeBlocking_read = CL_TRUE;
    size_t *nativeBuffer_offset = NULL;
    size_t *nativeHost_offset = NULL;
    size_t *nativeRegion = NULL;
    size_t nativeBuffer_row_pitch = 0;
    size_t nativeBuffer_slice_pitch = 0;
    size_t nativeHost_row_pitch = 0;
    size_t nativeHost_slice_pitch = 0;
    void *nativePtr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }
    nativeBlocking_read = (cl_bool)blocking_read;
    if (buffer_offset != NULL)
    {
        nativeBuffer_offset = convertArray(env, buffer_offset);
        if (nativeBuffer_offset == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (host_offset != NULL)
    {
        nativeHost_offset = convertArray(env, host_offset);
        if (nativeHost_offset == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeBuffer_row_pitch = (size_t)buffer_row_pitch;
    nativeBuffer_slice_pitch = (size_t)buffer_slice_pitch;
    nativeHost_row_pitch = (size_t)host_row_pitch;
    nativeHost_slice_pitch = (size_t)host_slice_pitch;
    PointerData *ptrPointerData = initPointerData(env, ptr);
    if (ptrPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativePtr = (void*)ptrPointerData->pointer;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueReadBufferRectFP)(nativeCommand_queue, nativeBuffer, nativeBlocking_read, nativeBuffer_offset, nativeHost_offset, nativeRegion, nativeBuffer_row_pitch, nativeBuffer_slice_pitch, nativeHost_row_pitch, nativeHost_slice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    /* See notes about NON_BLOCKING_READ at end of file
    if (nativeBlocking_read)
    {
        if (!releasePointerData(env, ptrPointerData)) return CL_INVALID_HOST_PTR;
    }
    else
    {
        Logger::log(LOG_ERROR, "Storing pending release of pointer data for event %p\n", nativeEvent);
        pendingPointerDataMap[nativeEvent] = ptrPointerData;
    }
    */

    delete[] nativeBuffer_offset;
    delete[] nativeHost_offset;
    delete[] nativeRegion;
    if (!releasePointerData(env, ptrPointerData)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

//#endif // defined(CL_VERSION_1_1)













/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueWriteBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueWriteBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject buffer, jboolean blocking_write, jlong offset, jlong cb, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWriteBuffer\n");
    if (clEnqueueWriteBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueWriteBuffer is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeBuffer = NULL;
    cl_bool nativeBlocking_write = CL_TRUE;
    size_t nativeOffset = 0;
    size_t nativeCb = 0;
    void *nativePtr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }

    nativeBlocking_write = (cl_bool)blocking_write;

    nativeOffset = (size_t)offset;
    nativeCb = (size_t)cb;
    PointerData *ptrPointerData = initPointerData(env, ptr);
    if (ptrPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativePtr = (void*)ptrPointerData->pointer;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueWriteBufferFP)(nativeCommand_queue, nativeBuffer, nativeBlocking_write, nativeOffset, nativeCb, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    if (!releasePointerData(env, ptrPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}






//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueWriteBufferRectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueWriteBufferRectNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject buffer, jboolean blocking_write, jlongArray buffer_offset, jlongArray host_offset, jlongArray region, jlong buffer_row_pitch, jlong buffer_slice_pitch, jlong host_row_pitch, jlong host_slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWriteBufferRect\n");
    if (clEnqueueWriteBufferRectFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueWriteBufferRect is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeBuffer = NULL;
    cl_bool nativeBlocking_write = CL_TRUE;
    size_t *nativeBuffer_offset = NULL;
    size_t *nativeHost_offset = NULL;
    size_t *nativeRegion = NULL;
    size_t nativeBuffer_row_pitch = 0;
    size_t nativeBuffer_slice_pitch = 0;
    size_t nativeHost_row_pitch = 0;
    size_t nativeHost_slice_pitch = 0;
    void *nativePtr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }
    nativeBlocking_write = (cl_bool)blocking_write;
    if (buffer_offset != NULL)
    {
        nativeBuffer_offset = convertArray(env, buffer_offset);
        if (nativeBuffer_offset == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (host_offset != NULL)
    {
        nativeHost_offset = convertArray(env, host_offset);
        if (nativeHost_offset == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    nativeBuffer_row_pitch = (size_t)buffer_row_pitch;
    nativeBuffer_slice_pitch = (size_t)buffer_slice_pitch;
    nativeHost_row_pitch = (size_t)host_row_pitch;
    nativeHost_slice_pitch = (size_t)host_slice_pitch;
    PointerData *ptrPointerData = initPointerData(env, ptr);
    if (ptrPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativePtr = (void*)ptrPointerData->pointer;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueWriteBufferRectFP)(nativeCommand_queue, nativeBuffer, nativeBlocking_write, nativeBuffer_offset, nativeHost_offset, nativeRegion, nativeBuffer_row_pitch, nativeBuffer_slice_pitch, nativeHost_row_pitch, nativeHost_slice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeBuffer_offset;
    delete[] nativeHost_offset;
    delete[] nativeRegion;
    if (!releasePointerData(env, ptrPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

//#endif // defined(CL_VERSION_1_1)


//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueFillBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/Pointer;JJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueFillBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject buffer, jobject pattern, jlong pattern_size, jlong offset, jlong size, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueFillBuffer\n");
    if (clEnqueueFillBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueFillBuffer is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeBuffer = NULL;
    void *nativePattern = NULL;
    size_t nativePattern_size = 0;
    size_t nativeOffset = 0;
    size_t nativeSize = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }
    PointerData *patternPointerData = initPointerData(env, pattern);
    if (patternPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativePattern = (void*)patternPointerData->pointer;
    nativePattern_size = (size_t)pattern_size;
    nativeOffset = (size_t)offset;
    nativeSize = (size_t)size;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueFillBufferFP)(nativeCommand_queue, nativeBuffer, nativePattern, nativePattern_size, nativeOffset, nativeSize, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    if (!releasePointerData(env, patternPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}

//#endif // defined(CL_VERSION_1_2)



/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;JJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject src_buffer, jobject dst_buffer, jlong src_offset, jlong dst_offset, jlong cb, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyBuffer\n");
    if (clEnqueueCopyBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueCopyBuffer is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeSrc_buffer = NULL;
    cl_mem nativeDst_buffer = NULL;
    size_t nativeSrc_offset = 0;
    size_t nativeDst_offset = 0;
    size_t nativeCb = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (src_buffer != NULL)
    {
        nativeSrc_buffer = (cl_mem)env->GetLongField(src_buffer, NativePointerObject_nativePointer);
    }
    if (dst_buffer != NULL)
    {
        nativeDst_buffer = (cl_mem)env->GetLongField(dst_buffer, NativePointerObject_nativePointer);
    }
    nativeSrc_offset = (size_t)src_offset;
    nativeDst_offset = (size_t)dst_offset;
    nativeCb = (size_t)cb;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueCopyBufferFP)(nativeCommand_queue, nativeSrc_buffer, nativeDst_buffer, nativeSrc_offset, nativeDst_offset, nativeCb, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}


//#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyBufferRectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[J[JJJJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyBufferRectNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject src_buffer, jobject dst_buffer, jlongArray src_origin, jlongArray dst_origin, jlongArray region, jlong src_row_pitch, jlong src_slice_pitch, jlong dst_row_pitch, jlong dst_slice_pitch, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyBufferRect\n");
    if (clEnqueueCopyBufferRectFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueCopyBufferRect is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeSrc_buffer = NULL;
    cl_mem nativeDst_buffer = NULL;
    size_t *nativeSrc_origin = NULL;
    size_t *nativeDst_origin = NULL;
    size_t *nativeRegion = NULL;
    size_t nativeSrc_row_pitch = 0;
    size_t nativeSrc_slice_pitch = 0;
    size_t nativeDst_row_pitch = 0;
    size_t nativeDst_slice_pitch = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (src_buffer != NULL)
    {
        nativeSrc_buffer = (cl_mem)env->GetLongField(src_buffer, NativePointerObject_nativePointer);
    }
    if (dst_buffer != NULL)
    {
        nativeDst_buffer = (cl_mem)env->GetLongField(dst_buffer, NativePointerObject_nativePointer);
    }
    if (src_origin != NULL)
    {
        nativeSrc_origin = convertArray(env, src_origin);
        if (nativeSrc_origin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (dst_origin != NULL)
    {
        nativeDst_origin = convertArray(env, dst_origin);
        if (nativeDst_origin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeSrc_row_pitch = (size_t)src_row_pitch;
    nativeSrc_slice_pitch = (size_t)src_slice_pitch;
    nativeDst_row_pitch = (size_t)dst_row_pitch;
    nativeDst_slice_pitch = (size_t)dst_slice_pitch;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueCopyBufferRectFP)(nativeCommand_queue, nativeSrc_buffer, nativeDst_buffer, nativeSrc_origin, nativeDst_origin, nativeRegion, nativeSrc_row_pitch, nativeSrc_slice_pitch, nativeDst_row_pitch, nativeDst_slice_pitch, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeSrc_origin;
    delete[] nativeDst_origin;
    delete[] nativeRegion;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}

//#endif // defined(CL_VERSION_1_1)











/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueReadImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[JJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueReadImageNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject image, jboolean blocking_read, jlongArray origin, jlongArray region, jlong row_pitch, jlong slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReadImage\n");
    if (clEnqueueReadImageFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueReadImage is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeImage = NULL;
    cl_bool nativeBlocking_read = CL_TRUE;
    size_t *nativeOrigin = NULL;
    size_t *nativeRegion = NULL;
    size_t nativeRow_pitch = 0;
    size_t nativeSlice_pitch = 0;
    void *nativePtr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (image != NULL)
    {
        nativeImage = (cl_mem)env->GetLongField(image, NativePointerObject_nativePointer);
    }

    nativeBlocking_read = (cl_bool)blocking_read;

    if (origin != NULL)
    {
        nativeOrigin = convertArray(env, origin);
        if (nativeOrigin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeRow_pitch = (size_t)row_pitch;
    nativeSlice_pitch = (size_t)slice_pitch;
    PointerData *ptrPointerData = initPointerData(env, ptr);
    if (ptrPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativePtr = (void*)ptrPointerData->pointer;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }


    int result = (clEnqueueReadImageFP)(nativeCommand_queue, nativeImage, nativeBlocking_read, nativeOrigin, nativeRegion, nativeRow_pitch, nativeSlice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // See notes about NON_BLOCKING_READ at end of file

    // Write back native variable values and clean up
    delete[] nativeOrigin;
    delete[] nativeRegion;
    if (!releasePointerData(env, ptrPointerData)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueWriteImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[JJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueWriteImageNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject image, jboolean blocking_write, jlongArray origin, jlongArray region, jlong input_row_pitch, jlong input_slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWriteImage\n");
    if (clEnqueueWriteImageFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueWriteImage is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeImage = NULL;
    cl_bool nativeBlocking_write = CL_TRUE;
    size_t *nativeOrigin = NULL;
    size_t *nativeRegion = NULL;
    size_t nativeInput_row_pitch = 0;
    size_t nativeInput_slice_pitch = 0;
    void *nativePtr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (image != NULL)
    {
        nativeImage = (cl_mem)env->GetLongField(image, NativePointerObject_nativePointer);
    }

    nativeBlocking_write = (cl_bool)blocking_write;

    if (origin != NULL)
    {
        nativeOrigin = convertArray(env, origin);
        if (nativeOrigin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeInput_row_pitch = (size_t)input_row_pitch;
    nativeInput_slice_pitch = (size_t)input_slice_pitch;
    PointerData *ptrPointerData = initPointerData(env, ptr);
    if (ptrPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativePtr = (void*)ptrPointerData->pointer;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueWriteImageFP)(nativeCommand_queue, nativeImage, nativeBlocking_write, nativeOrigin, nativeRegion, nativeInput_row_pitch, nativeInput_slice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeOrigin;
    delete[] nativeRegion;
    if (!releasePointerData(env, ptrPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}



//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueFillImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/Pointer;[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueFillImageNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject image, jobject fill_color, jlongArray origin, jlongArray region, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueFillImage\n");
    if (clEnqueueFillImageFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueFillImage is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeImage = NULL;
    void *nativeFill_color = NULL;
    size_t *nativeOrigin = NULL;
    size_t *nativeRegion = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (image != NULL)
    {
        nativeImage = (cl_mem)env->GetLongField(image, NativePointerObject_nativePointer);
    }
    PointerData *fill_colorPointerData = initPointerData(env, fill_color);
    if (fill_colorPointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeFill_color = (void*)fill_colorPointerData->pointer;
    if (origin != NULL)
    {
        nativeOrigin = convertArray(env, origin);
        if (nativeOrigin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueFillImageFP)(nativeCommand_queue, nativeImage, nativeFill_color, nativeOrigin, nativeRegion, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeOrigin;
    delete[] nativeRegion;
    if (!releasePointerData(env, fill_colorPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}


//#endif // defined(CL_VERSION_1_2)


/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyImageNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject src_image, jobject dst_image, jlongArray src_origin, jlongArray dst_origin, jlongArray region, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyImage\n");
    if (clEnqueueCopyImageFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueCopyImage is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeSrc_image = NULL;
    cl_mem nativeDst_image = NULL;
    size_t *nativeSrc_origin = NULL;
    size_t *nativeDst_origin = NULL;
    size_t *nativeRegion = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (src_image != NULL)
    {
        nativeSrc_image = (cl_mem)env->GetLongField(src_image, NativePointerObject_nativePointer);
    }
    if (dst_image != NULL)
    {
        nativeDst_image = (cl_mem)env->GetLongField(dst_image, NativePointerObject_nativePointer);
    }
    if (src_origin != NULL)
    {
        nativeSrc_origin = convertArray(env, src_origin);
        if (nativeSrc_origin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (dst_origin != NULL)
    {
        nativeDst_origin = convertArray(env, dst_origin);
        if (nativeDst_origin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueCopyImageFP)(nativeCommand_queue, nativeSrc_image, nativeDst_image, nativeSrc_origin, nativeDst_origin, nativeRegion, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeSrc_origin;
    delete[] nativeDst_origin;
    delete[] nativeRegion;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyImageToBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[JJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyImageToBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject src_image, jobject dst_buffer, jlongArray src_origin, jlongArray region, jlong dst_offset, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyImageToBuffer\n");
    if (clEnqueueCopyImageToBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueCopyImageToBuffer is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeSrc_image = NULL;
    cl_mem nativeDst_buffer = NULL;
    size_t *nativeSrc_origin = NULL;
    size_t *nativeRegion = NULL;
    size_t nativeDst_offset = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (src_image != NULL)
    {
        nativeSrc_image = (cl_mem)env->GetLongField(src_image, NativePointerObject_nativePointer);
    }
    if (dst_buffer != NULL)
    {
        nativeDst_buffer = (cl_mem)env->GetLongField(dst_buffer, NativePointerObject_nativePointer);
    }
    if (src_origin != NULL)
    {
        nativeSrc_origin = convertArray(env, src_origin);
        if (nativeSrc_origin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeDst_offset = (size_t)dst_offset;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueCopyImageToBufferFP)(nativeCommand_queue, nativeSrc_image, nativeDst_buffer, nativeSrc_origin, nativeRegion, nativeDst_offset, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeSrc_origin;
    delete[] nativeRegion;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyBufferToImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyBufferToImageNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject src_buffer, jobject dst_image, jlong src_offset, jlongArray dst_origin, jlongArray region, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyBufferToImage\n");
    if (clEnqueueCopyBufferToImageFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueCopyBufferToImage is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeSrc_buffer = NULL;
    cl_mem nativeDst_image = NULL;
    size_t nativeSrc_offset = 0;
    size_t *nativeDst_origin = NULL;
    size_t *nativeRegion = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (src_buffer != NULL)
    {
        nativeSrc_buffer = (cl_mem)env->GetLongField(src_buffer, NativePointerObject_nativePointer);
    }
    if (dst_image != NULL)
    {
        nativeDst_image = (cl_mem)env->GetLongField(dst_image, NativePointerObject_nativePointer);
    }
    nativeSrc_offset = (size_t)src_offset;
    if (dst_origin != NULL)
    {
        nativeDst_origin = convertArray(env, dst_origin);
        if (nativeDst_origin == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueCopyBufferToImageFP)(nativeCommand_queue, nativeSrc_buffer, nativeDst_image, nativeSrc_offset, nativeDst_origin, nativeRegion, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeDst_origin;
    delete[] nativeRegion;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}



/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueMapBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;[I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clEnqueueMapBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject buffer, jboolean blocking_map, jlong map_flags, jlong offset, jlong cb, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMapBuffer\n");
    if (clEnqueueMapBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueMapBuffer is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeBuffer = NULL;
    cl_bool nativeBlocking_map = CL_TRUE;
    cl_map_flags nativeMap_flags = 0;
    size_t nativeOffset = 0;
    size_t nativeCb = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;
    cl_int nativeErrcode_ret = 0;
    void *nativeHostPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (buffer != NULL)
    {
        nativeBuffer = (cl_mem)env->GetLongField(buffer, NativePointerObject_nativePointer);
    }

    nativeBlocking_map = (cl_bool)blocking_map;

    nativeMap_flags = (cl_map_flags)map_flags;
    nativeOffset = (size_t)offset;
    nativeCb = (size_t)cb;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return NULL;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    nativeHostPointer = (clEnqueueMapBufferFP)(nativeCommand_queue, nativeBuffer, nativeBlocking_map, nativeMap_flags, nativeOffset, nativeCb, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    // Create and return a ByteBuffer for the mapped memory
    if (nativeHostPointer == NULL)
    {
        return NULL;
    }
    return env->NewDirectByteBuffer(nativeHostPointer, (jlong)nativeCb);
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueMapImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJ[J[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;[I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clEnqueueMapImageNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject image, jboolean blocking_map, jlong map_flags, jlongArray origin, jlongArray region, jlongArray image_row_pitch, jlongArray image_slice_pitch, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMapImage\n");
    if (clEnqueueMapImageFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueMapImage is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeImage = NULL;
    cl_bool nativeBlocking_map = CL_TRUE;
    cl_map_flags nativeMap_flags = 0;
    size_t *nativeOrigin = NULL;
    size_t *nativeRegion = NULL;
    size_t nativeImage_row_pitch = 0;
    size_t nativeImage_slice_pitch = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;
    cl_int nativeErrcode_ret = 0;
    void *nativeHostPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (image != NULL)
    {
        nativeImage = (cl_mem)env->GetLongField(image, NativePointerObject_nativePointer);
    }

    nativeBlocking_map = (cl_bool)blocking_map;

    nativeMap_flags = (cl_map_flags)map_flags;
    if (origin != NULL)
    {
        nativeOrigin = convertArray(env, origin);
        if (nativeOrigin == NULL)
        {
            return NULL;
        }
    }
    if (region != NULL)
    {
        nativeRegion = convertArray(env, region);
        if (nativeRegion == NULL)
        {
            return NULL;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return NULL;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    nativeHostPointer = (clEnqueueMapImageFP)(nativeCommand_queue, nativeImage, nativeBlocking_map, nativeMap_flags, nativeOrigin, nativeRegion, &nativeImage_row_pitch, &nativeImage_slice_pitch, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeOrigin;
    delete[] nativeRegion;
    if (!set(env, image_row_pitch, 0, (jlong)nativeImage_row_pitch)) return NULL;
    if (!set(env, image_slice_pitch, 0, (jlong)nativeImage_slice_pitch)) return NULL;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    // Create and return a ByteBuffer for the mapped memory
    size_t size = 0;
    if (nativeRegion != NULL)
    {
        size = nativeImage_row_pitch * nativeRegion[1] + nativeRegion[0];
        if (nativeRegion[2] != 0 && nativeImage_slice_pitch != 0)
        {
            size += nativeImage_slice_pitch * nativeRegion[2];
        }
    }
    if (nativeHostPointer == NULL)
    {
        return NULL;
    }
    return env->NewDirectByteBuffer(nativeHostPointer, (jlong)size);
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueUnmapMemObjectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Ljava/nio/ByteBuffer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueUnmapMemObjectNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject memobj, jobject mapped_ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueUnmapMemObject\n");
    if (clEnqueueUnmapMemObjectFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueUnmapMemObject is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeMemobj = NULL;
    void *nativeMapped_ptr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    if (mapped_ptr != NULL)
    {
        nativeMapped_ptr = (void*)env->GetDirectBufferAddress(mapped_ptr);
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueUnmapMemObjectFP)(nativeCommand_queue, nativeMemobj, nativeMapped_ptr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}



//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueMigrateMemObjectsNative
 * Signature: (Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_mem;JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueMigrateMemObjectsNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint num_mem_objects, jobjectArray mem_objects, jlong flags, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMigrateMemObjects\n");
    if (clEnqueueMigrateMemObjectsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueMigrateMemObjects is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_mem_objects = 0;
    cl_mem *nativeMem_objects = NULL;
    cl_mem_migration_flags nativeFlags;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_mem_objects = (cl_uint)num_mem_objects;
    if (mem_objects != NULL)
    {
        nativeMem_objects = createMemList(env, mem_objects, nativeNum_mem_objects);
        if (nativeMem_objects == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeFlags = (cl_mem_migration_flags)flags;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueMigrateMemObjectsFP)(nativeCommand_queue, nativeNum_mem_objects, nativeMem_objects, nativeFlags, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeMem_objects;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

//#endif // defined(CL_VERSION_1_2)



/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueNDRangeKernelNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_kernel;I[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueNDRangeKernelNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject kernel, jint work_dim, jlongArray global_work_offset, jlongArray global_work_size, jlongArray local_work_size, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueNDRangeKernel\n");
    if (clEnqueueNDRangeKernelFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueNDRangeKernel is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_kernel nativeKernel = NULL;
    cl_uint nativeWork_dim = 0;
    size_t *nativeGlobal_work_offset = NULL;
    size_t *nativeGlobal_work_size = NULL;
    size_t *nativeLocal_work_size = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    nativeWork_dim = (cl_uint)work_dim;
    if (global_work_offset != NULL)
    {
        nativeGlobal_work_offset = convertArray(env, global_work_offset);
        if (nativeGlobal_work_offset == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (global_work_size != NULL)
    {
        nativeGlobal_work_size = convertArray(env, global_work_size);
        if (nativeGlobal_work_size == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (local_work_size != NULL)
    {
        nativeLocal_work_size = convertArray(env, local_work_size);
        if (nativeLocal_work_size == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueNDRangeKernelFP)(nativeCommand_queue, nativeKernel, nativeWork_dim, nativeGlobal_work_offset, nativeGlobal_work_size, nativeLocal_work_size, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeGlobal_work_offset;
    delete[] nativeGlobal_work_size;
    delete[] nativeLocal_work_size;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueTaskNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_kernel;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueTaskNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject kernel, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueTask\n");
    if (clEnqueueTaskFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueTask is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_kernel nativeKernel = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueTaskFP)(nativeCommand_queue, nativeKernel, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueNativeKernelNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/EnqueueNativeKernelFunction;Ljava/lang/Object;JI[Lorg/jocl/cl_mem;[Lorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueNativeKernelNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject user_func, jobject args, jlong cb_args, jint num_mem_objects, jobjectArray mem_list, jobjectArray args_mem_loc, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueNativeKernel\n");
    if (clEnqueueNativeKernelFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueNativeKernel is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    EnqueueNativeKernelFunctionPointer nativeUser_func = NULL;
    void *nativeArgs = NULL;
    size_t nativeCb_args = 0;
    cl_uint nativeNum_mem_objects = 0;
    cl_mem *nativeMem_list = NULL;
    void **nativeArgs_mem_loc = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (user_func != NULL)
    {
        nativeUser_func = &EnqueueNativeKernelFunction;
        CallbackInfo *callbackInfo = initCallbackInfo(env, user_func, args);
        if (callbackInfo == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
        nativeArgs = (void*)callbackInfo;
    }
    nativeCb_args = (size_t)cb_args;
    nativeNum_mem_objects = (cl_uint)num_mem_objects;
    if (mem_list != NULL)
    {
        nativeMem_list = createMemList(env, mem_list, nativeNum_mem_objects);
        if (nativeMem_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (args_mem_loc != NULL)
    {
        jsize args_mem_locLength = env->GetArrayLength(args_mem_loc);
        nativeArgs_mem_loc = new void*[(size_t)args_mem_locLength];
        if (nativeArgs_mem_loc == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during args mem loc array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
        for (jsize i = 0; i<args_mem_locLength; i++)
        {
            jobject mem_loc = env->GetObjectArrayElement(args_mem_loc, i);
            if (mem_loc != NULL)
            {
                nativeArgs_mem_loc[(size_t)i] = (void*)env->GetLongField(mem_loc, NativePointerObject_nativePointer);
            }
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    //if (event != NULL) // Always use a non-NULL event here
    {
        nativeEventPointer = &nativeEvent;
    }


    // TODO: The call currently has to be blocking,
    // to prevent the nativeArgs from being deleted
    int result = (clEnqueueNativeKernelFP)(nativeCommand_queue, nativeUser_func, nativeArgs, nativeCb_args, nativeNum_mem_objects, nativeMem_list, (const void**)nativeArgs_mem_loc, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // TODO: Have to block in the current implementation
    (clWaitForEventsFP)(1, &nativeEvent);
    if (event == NULL)
    {
        (clReleaseEventFP)(nativeEvent);
    }

    // Write back native variable values and clean up
    delete[] nativeMem_list;
    delete[] nativeArgs_mem_loc;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueMarkerWithWaitListNative
 * Signature: (Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueMarkerWithWaitListNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMarkerWithWaitList\n");
    if (clEnqueueMarkerWithWaitListFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueMarkerWithWaitList is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueMarkerWithWaitListFP)(nativeCommand_queue, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}


//#endif // defined(CL_VERSION_1_2)




//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueBarrierWithWaitListNative
 * Signature: (Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueBarrierWithWaitListNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueBarrierWithWaitList\n");
    if (clEnqueueBarrierWithWaitListFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueBarrierWithWaitList is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueBarrierWithWaitListFP)(nativeCommand_queue, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}


//#endif // defined(CL_VERSION_1_2)



//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clSetPrintfCallbackNative
 * Signature: (Lorg/jocl/cl_context;Lorg/jocl/PrintfCallbackFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetPrintfCallbackNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clSetPrintfCallback\n");
    //if (TRUE) // As of OpenCL 2.0
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clSetPrintfCallback is not supported");
        return CL_INVALID_OPERATION;
    }
}


//#endif // defined(CL_VERSION_1_2)



//#if defined(CL_VERSION_2_0)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueSVMFreeNative
 * Signature: (Lorg/jocl/cl_command_queue;I[Lorg/jocl/Pointer;Lorg/jocl/SVMFreeFunction;Ljava/lang/Object;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueSVMFreeNative
(JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint num_svm_pointers, jobjectArray svm_pointers, jobject pfn_free_func, jobject user_data, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueSVMFree\n");
    if (clEnqueueSVMFreeFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueSVMFree is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_svm_pointers = 0;
    void **nativeSvm_pointers = NULL;
	SVMFreeCallbackFunctionPointer nativePfn_free_func = NULL;
    void *nativeUser_data = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_svm_pointers = (cl_uint)num_svm_pointers;
    if (svm_pointers != NULL)
    {
        nativeSvm_pointers = createSvmPointers(env, svm_pointers, nativeNum_svm_pointers);
        if (nativeSvm_pointers == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
	CallbackInfo *callbackInfo = NULL;
    if (pfn_free_func != NULL)
    {
		nativePfn_free_func = &SVMFreeCallbackFunction;
        callbackInfo = initCallbackInfo(env, pfn_free_func, user_data);
        if (callbackInfo == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
        nativeUser_data = (void*)callbackInfo;
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }
    int result = (clEnqueueSVMFreeFP)(nativeCommand_queue, nativeNum_svm_pointers, nativeSvm_pointers, nativePfn_free_func, nativeUser_data, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeSvm_pointers;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueSVMMemcpyNative
 * Signature: (Lorg/jocl/cl_command_queue;ZLorg/jocl/Pointer;Lorg/jocl/Pointer;JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueSVMMemcpyNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jboolean blocking_copy, jobject dst_ptr, jobject src_ptr, jlong size, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueSVMMemcpy\n");
    if (clEnqueueSVMMemcpyFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueSVMMemcpy is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
	cl_bool nativeBlocking_copy = CL_TRUE;
    void* nativeDst_ptr = NULL;
    void* nativeSrc_ptr = NULL;
    size_t nativeSize = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
	nativeBlocking_copy = (cl_bool)blocking_copy;
    if (dst_ptr != NULL)
    {
        nativeDst_ptr = (void*)env->GetLongField(dst_ptr, NativePointerObject_nativePointer);
    }
    if (src_ptr != NULL)
    {
        nativeSrc_ptr = (void*)env->GetLongField(src_ptr, NativePointerObject_nativePointer);
    }
    nativeSize = (size_t)size;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueSVMMemcpyFP)(nativeCommand_queue, nativeBlocking_copy, nativeDst_ptr, nativeSrc_ptr, nativeSize, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueSVMMemFillNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/Pointer;Lorg/jocl/Pointer;JJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueSVMMemFillNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject svm_ptr, jobject pattern, jlong pattern_size, jlong size, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueSVMMemFill\n");
    if (clEnqueueSVMMemFillFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueSVMMemFill is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    void* nativeSvm_ptr = NULL;
    void* nativePattern = NULL;
    size_t nativePattern_size = 0;
    size_t nativeSize = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (svm_ptr != NULL)
    {
        nativeSvm_ptr = (void*)env->GetLongField(svm_ptr, NativePointerObject_nativePointer);
    }
    if (pattern != NULL)
    {
        nativePattern = (void*)env->GetLongField(pattern, NativePointerObject_nativePointer);
    }
    nativePattern_size = (size_t)pattern_size;
    nativeSize = (size_t)size;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueSVMMemFillFP)(nativeCommand_queue, nativeSvm_ptr, nativePattern, nativePattern_size, nativeSize, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueSVMMapNative
 * Signature: (Lorg/jocl/cl_command_queue;ZJLorg/jocl/Pointer;JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueSVMMapNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jboolean blocking_map, jlong flags, jobject svm_ptr, jlong size, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueSVMMap\n");
    if (clEnqueueSVMMapFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueSVMMap is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
	cl_bool nativeBlocking_map = CL_TRUE;
	cl_map_flags nativeFlags = 0;
    void* nativeSvm_ptr = NULL;
    size_t nativeSize = 0;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
	nativeBlocking_map = (cl_bool)blocking_map;
	nativeFlags = (cl_map_flags)flags;
    if (svm_ptr != NULL)
    {
        nativeSvm_ptr = (void*)env->GetLongField(svm_ptr, NativePointerObject_nativePointer);
    }
    nativeSize = (size_t)size;
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueSVMMapFP)(nativeCommand_queue, nativeBlocking_map, nativeFlags, nativeSvm_ptr, nativeSize, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueSVMUnmapNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueSVMUnmapNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject svm_ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueSVMUnmap\n");
    if (clEnqueueSVMUnmapFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueSVMUnmap is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    void* nativeSvm_ptr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (svm_ptr != NULL)
    {
        nativeSvm_ptr = (void*)env->GetLongField(svm_ptr, NativePointerObject_nativePointer);
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueSVMUnmapFP)(nativeCommand_queue, nativeSvm_ptr, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}



//#endif // defined(CL_VERSION_2_0)


/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueMarkerNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueMarkerNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMarker\n");
    if (clEnqueueMarkerFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueMarker is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueMarkerFP)(nativeCommand_queue, nativeEventPointer);

    // Write back native variable values and clean up
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueWaitForEventsNative
 * Signature: (Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueWaitForEventsNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint num_events, jobjectArray event_list)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWaitForEvents\n");
    if (clEnqueueWaitForEventsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueWaitForEvents is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_events = 0;
    cl_event *nativeEvent_list = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_events = (cl_uint)num_events;
    if (event_list != NULL)
    {
        nativeEvent_list = createEventList(env, event_list, nativeNum_events);
        if (nativeEvent_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = (clEnqueueWaitForEventsFP)(nativeCommand_queue, nativeNum_events, nativeEvent_list);

    // Write back native variable values and clean up
    delete[] nativeEvent_list;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueBarrierNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueBarrierNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueBarrier\n");
    if (clEnqueueBarrierFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueBarrier is not supported");
        return CL_INVALID_OPERATION;
    }

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return (clEnqueueBarrierFP)(nativeCommand_queue);
}



//=== GL functions ===========================================================




/*
 * Class:     org_jocl_CL
 * Method:    clCreateFromGLBufferNative
 * Signature: (Lorg/jocl/cl_context;JI[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateFromGLBufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jint bufobj, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLBuffer\n");
    if (clCreateFromGLBufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateFromGLBuffer is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    GLuint nativeBufobj = 0;
    cl_int nativeErrcode_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeBufobj = (GLuint)bufobj;

    cl_mem nativeMem = (clCreateFromGLBufferFP)(nativeContext, nativeFlags, nativeBufobj, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}


//#if defined(CL_VERSION_1_2)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateFromGLTextureNative
 * Signature: (Lorg/jocl/cl_context;JIII[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateFromGLTextureNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jint target, jint miplevel, jint texture, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLTexture\n");
    if (clCreateFromGLTextureFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateFromGLTexture is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    GLenum nativeTarget = 0;
    GLint nativeMiplevel = 0;
    GLuint nativeTexture = 0;
    cl_int nativeErrcode_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeTarget = (GLenum)target;
    nativeMiplevel = (GLint)miplevel;
    nativeTexture = (GLuint)texture;

    cl_mem nativeMem = (clCreateFromGLTextureFP)(nativeContext, nativeFlags, nativeTarget, nativeMiplevel, nativeTexture, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;

}

//#endif // defined(CL_VERSION_1_2)


/*
 * Class:     org_jocl_CL
 * Method:    clCreateFromGLTexture2DNative
 * Signature: (Lorg/jocl/cl_context;JIII[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateFromGLTexture2DNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jint texture_target, jint miplevel, jint texture, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLTexture2D\n");
    if (clCreateFromGLTexture2DFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateFromGLTexture2D is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    GLenum nativeTexture_target = 0;
    GLint nativeMiplevel = 0;
    GLuint nativeTexture = 0;
    cl_int nativeErrcode_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeTexture_target = (GLenum)texture_target;
    nativeMiplevel = (GLint)miplevel;
    nativeTexture = (GLuint)texture;

    cl_mem nativeMem = (clCreateFromGLTexture2DFP)(nativeContext, nativeFlags, nativeTexture_target, nativeMiplevel, nativeTexture, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}



/*
 * Class:     org_jocl_CL
 * Method:    clCreateFromGLTexture3DNative
 * Signature: (Lorg/jocl/cl_context;JIII[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateFromGLTexture3DNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jint texture_target, jint miplevel, jint texture, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLTexture3D\n");
    if (clCreateFromGLTexture3DFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateFromGLTexture3D is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    GLenum nativeTexture_target = 0;
    GLint nativeMiplevel = 0;
    GLuint nativeTexture = 0;
    cl_int nativeErrcode_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeTexture_target = (GLenum)texture_target;
    nativeMiplevel = (GLint)miplevel;
    nativeTexture = (GLuint)texture;

    cl_mem nativeMem = (clCreateFromGLTexture3DFP)(nativeContext, nativeFlags, nativeTexture_target, nativeMiplevel, nativeTexture, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}

/*
 * Class:     org_jocl_CL
 * Method:    clCreateFromGLRenderbufferNative
 * Signature: (Lorg/jocl/cl_context;JI[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateFromGLRenderbufferNative
  (JNIEnv *env, jclass UNUSED(cls), jobject context, jlong flags, jint renderbuffer, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLRenderbuffer\n");
    if (clCreateFromGLRenderbufferFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clCreateFromGLRenderbuffer is not supported");
        return NULL;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = 0;
    GLuint nativeRenderbuffer = 0;
    cl_int nativeErrcode_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeRenderbuffer = (GLuint)renderbuffer;

    cl_mem nativeMem = (clCreateFromGLRenderbufferFP)(nativeContext, nativeFlags, nativeRenderbuffer, &nativeErrcode_ret);

    // Write back native variable values and clean up
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    if (nativeMem == NULL)
    {
        return NULL;
    }

    // Create and return the Java cl_mem object
    jobject mem = env->NewObject(cl_mem_Class, cl_mem_Constructor);
    if (env->ExceptionCheck())
    {
        return NULL;
    }

    setNativePointer(env, mem, (jlong)nativeMem);
    return mem;
}


/*
 * Class:     org_jocl_CL
 * Method:    clGetGLObjectInfoNative
 * Signature: (Lorg/jocl/cl_mem;[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetGLObjectInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject memobj, jintArray gl_object_type, jintArray gl_object_name)
{
    Logger::log(LOG_TRACE, "Executing clGetGLObjectInfo\n");
    if (clGetGLObjectInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetGLObjectInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_mem nativeMemobj = NULL;
    cl_gl_object_type nativeGl_object_type = 0;
    GLuint nativeGl_object_name = 0;

    // Obtain native variable values
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }

    int result = (clGetGLObjectInfoFP)(nativeMemobj, &nativeGl_object_type, &nativeGl_object_name);

    // Write back native variable values and clean up
    if (!set(env, gl_object_type, 0, (jint)nativeGl_object_type)) return CL_OUT_OF_HOST_MEMORY;
    if (!set(env, gl_object_name, 0, (jint)nativeGl_object_name)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clGetGLTextureInfoNative
 * Signature: (Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetGLTextureInfoNative
  (JNIEnv *env, jclass UNUSED(cls), jobject memobj, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetGLTextureInfo\n");
    if (clGetGLTextureInfoFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetGLTextureInfo is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_mem nativeMemobj = NULL;
    cl_gl_texture_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret;

    // Obtain native variable values
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    nativeParam_name = (cl_gl_texture_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetGLTextureInfoFP)(nativeMemobj, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}


/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueAcquireGLObjectsNative
 * Signature: (Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_mem;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueAcquireGLObjectsNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint num_objects, jobjectArray mem_objects, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueAcquireGLObjects\n");
    if (clEnqueueAcquireGLObjectsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueAcquireGLObjects is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_objects = 0;
    cl_mem *nativeMem_objects = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_objects = (cl_uint)num_objects;
    if (mem_objects != NULL)
    {
        nativeMem_objects = createMemList(env, mem_objects, nativeNum_objects);
        if (nativeMem_objects == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueAcquireGLObjectsFP)(nativeCommand_queue, nativeNum_objects, nativeMem_objects, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeMem_objects;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueReleaseGLObjectsNative
 * Signature: (Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_mem;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueReleaseGLObjectsNative
  (JNIEnv *env, jclass UNUSED(cls), jobject command_queue, jint num_objects, jobjectArray mem_objects, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReleaseGLObjects\n");
    if (clEnqueueReleaseGLObjectsFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clEnqueueReleaseGLObjects is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_objects = 0;
    cl_mem *nativeMem_objects = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
    cl_event *nativeEventPointer = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_objects = (cl_uint)num_objects;
    if (mem_objects != NULL)
    {
        nativeMem_objects = createMemList(env, mem_objects, nativeNum_objects);
        if (nativeMem_objects == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, nativeNum_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }
    if (event != NULL)
    {
        nativeEventPointer = &nativeEvent;
    }

    int result = (clEnqueueReleaseGLObjectsFP)(nativeCommand_queue, nativeNum_objects, nativeMem_objects, nativeNum_events_in_wait_list, nativeEvent_wait_list, nativeEventPointer);

    // Write back native variable values and clean up
    delete[] nativeMem_objects;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clGetGLContextInfoAPPLENative
 * Signature: (Lorg/jocl/cl_context;JIJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetGLContextInfoAPPLENative
(JNIEnv *env, jclass UNUSED(cls), jobject context, jlong glContext, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetGLContextInfoAPPLE\n");
    if (clGetGLContextInfoAPPLEFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetGLContextInfoAPPLE is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_context nativeContext = NULL;
    void *nativeParam_glContext = 0;
    cl_gl_platform_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeParam_glContext = (void *)glContext;
    nativeParam_name = (cl_gl_platform_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetGLContextInfoAPPLEFP)(nativeContext, nativeParam_glContext, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}


/**
 * Register all native methods that are used in JOCL
 */
void registerAllNatives(JNIEnv *env, jclass cls)
{
    JNINativeMethod nativeMethod;

    nativeMethod.name = "setLogLevelNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_setLogLevelNative;
    nativeMethod.signature = "(I)V";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "allocateAlignedNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_allocateAlignedNative;
    nativeMethod.signature = "(IILorg/jocl/Pointer;)Ljava/nio/ByteBuffer;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "freeAlignedNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_freeAlignedNative;
    nativeMethod.signature = "(Lorg/jocl/Pointer;)V";
    env->RegisterNatives(cls, &nativeMethod, 1);


    nativeMethod.name = "clGetPlatformIDsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetPlatformIDsNative;
    nativeMethod.signature = "(I[Lorg/jocl/cl_platform_id;[I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetPlatformInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetPlatformInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_platform_id;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetDeviceIDsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetDeviceIDsNative;
    nativeMethod.signature = "(Lorg/jocl/cl_platform_id;JI[Lorg/jocl/cl_device_id;[I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetDeviceInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetDeviceInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateSubDevicesNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateSubDevicesNative;
    nativeMethod.signature = "(Lorg/jocl/cl_device_id;Lorg/jocl/cl_device_partition_property;I[Lorg/jocl/cl_device_id;[I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainDeviceNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainDeviceNative;
    nativeMethod.signature = "(Lorg/jocl/cl_device_id;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseDeviceNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseDeviceNative;
    nativeMethod.signature = "(Lorg/jocl/cl_device_id;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateContextNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateContextNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context_properties;I[Lorg/jocl/cl_device_id;Lorg/jocl/CreateContextFunction;Ljava/lang/Object;[I)Lorg/jocl/cl_context;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateContextFromTypeNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateContextFromTypeNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context_properties;JLorg/jocl/CreateContextFunction;Ljava/lang/Object;[I)Lorg/jocl/cl_context;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainContextNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainContextNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseContextNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseContextNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetContextInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetContextInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateCommandQueueNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateCommandQueueNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;Lorg/jocl/cl_device_id;J[I)Lorg/jocl/cl_command_queue;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateCommandQueueWithPropertiesNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateCommandQueueWithPropertiesNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;Lorg/jocl/cl_device_id;Lorg/jocl/cl_queue_properties;[I)Lorg/jocl/cl_command_queue;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainCommandQueueNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainCommandQueueNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseCommandQueueNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseCommandQueueNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetCommandQueueInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetCommandQueueInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetCommandQueuePropertyNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetCommandQueuePropertyNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;JZ[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JJLorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateSubBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateSubBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;JILorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateSubBuffer2Native";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateSubBuffer2Native;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;JILorg/jocl/cl_buffer_region;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JLorg/jocl/cl_image_format;Lorg/jocl/cl_image_desc;Lorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreatePipeNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreatePipeNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JIILorg/jocl/cl_pipe_properties;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateImage2DNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateImage2DNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;J[Lorg/jocl/cl_image_format;JJJLorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateImage3DNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateImage3DNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;J[Lorg/jocl/cl_image_format;JJJJJLorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainMemObjectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainMemObjectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseMemObjectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseMemObjectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetSupportedImageFormatsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetSupportedImageFormatsNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JII[Lorg/jocl/cl_image_format;[I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetMemObjectInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetMemObjectInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetImageInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetImageInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetPipeInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetPipeInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetMemObjectDestructorCallbackNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetMemObjectDestructorCallbackNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;Lorg/jocl/MemObjectDestructorCallbackFunction;Ljava/lang/Object;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSVMAllocNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSVMAllocNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JJI)Lorg/jocl/Pointer;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSVMFreeNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSVMFreeNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;Lorg/jocl/Pointer;)V";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateSamplerWithPropertiesNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateSamplerWithPropertiesNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;Lorg/jocl/cl_sampler_properties;[I)Lorg/jocl/cl_sampler;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateSamplerNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateSamplerNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;ZII[I)Lorg/jocl/cl_sampler;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainSamplerNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainSamplerNative;
    nativeMethod.signature = "(Lorg/jocl/cl_sampler;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseSamplerNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseSamplerNative;
    nativeMethod.signature = "(Lorg/jocl/cl_sampler;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetSamplerInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetSamplerInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_sampler;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateProgramWithSourceNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateProgramWithSourceNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;I[Ljava/lang/String;[J[I)Lorg/jocl/cl_program;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateProgramWithBinaryNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateProgramWithBinaryNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;I[Lorg/jocl/cl_device_id;[J[[B[I[I)Lorg/jocl/cl_program;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateProgramWithBuiltInKernelsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateProgramWithBuiltInKernelsNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;I[Lorg/jocl/cl_device_id;Ljava/lang/String;[I)Lorg/jocl/cl_program;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainProgramNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainProgramNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseProgramNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseProgramNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clBuildProgramNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clBuildProgramNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;I[Lorg/jocl/cl_device_id;Ljava/lang/String;Lorg/jocl/BuildProgramFunction;Ljava/lang/Object;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCompileProgramNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCompileProgramNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;I[Lorg/jocl/cl_device_id;Ljava/lang/String;I[Lorg/jocl/cl_program;[Ljava/lang/String;Lorg/jocl/BuildProgramFunction;Ljava/lang/Object;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clLinkProgramNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clLinkProgramNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;I[Lorg/jocl/cl_device_id;Ljava/lang/String;I[Lorg/jocl/cl_program;Lorg/jocl/BuildProgramFunction;Ljava/lang/Object;[I)Lorg/jocl/cl_program;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clUnloadPlatformCompilerNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clUnloadPlatformCompilerNative;
    nativeMethod.signature = "(Lorg/jocl/cl_platform_id;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clUnloadCompilerNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clUnloadCompilerNative;
    nativeMethod.signature = "()I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetProgramInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetProgramInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetProgramBuildInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetProgramBuildInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateKernelNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateKernelNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;Ljava/lang/String;[I)Lorg/jocl/cl_kernel;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateKernelsInProgramNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateKernelsInProgramNative;
    nativeMethod.signature = "(Lorg/jocl/cl_program;I[Lorg/jocl/cl_kernel;[I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainKernelNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainKernelNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseKernelNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseKernelNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetKernelArgNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetKernelArgNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetKernelArgSVMPointerNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetKernelArgSVMPointerNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;ILorg/jocl/Pointer;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetKernelExecInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetKernelExecInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetKernelInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetKernelInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetKernelArgInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetKernelArgInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;IIJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetKernelWorkGroupInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetKernelWorkGroupInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clWaitForEventsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clWaitForEventsNative;
    nativeMethod.signature = "(I[Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetEventInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetEventInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clCreateUserEventNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateUserEventNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;[I)Lorg/jocl/cl_event;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clRetainEventNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainEventNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clReleaseEventNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseEventNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetUserEventStatusNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetUserEventStatusNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetEventCallbackNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetEventCallbackNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;ILorg/jocl/EventCallbackFunction;Ljava/lang/Object;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clGetEventProfilingInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetEventProfilingInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clFlushNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clFlushNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clFinishNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clFinishNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueReadBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueReadBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueReadBufferRectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueReadBufferRectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueWriteBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueWriteBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueWriteBufferRectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueWriteBufferRectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueFillBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueFillBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/Pointer;JJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueCopyBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueCopyBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;JJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueCopyBufferRectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueCopyBufferRectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[J[JJJJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueReadImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueReadImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[JJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueWriteImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueWriteImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[JJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueFillImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueFillImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/Pointer;[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueCopyImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueCopyImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueCopyImageToBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueCopyImageToBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[JJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueCopyBufferToImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueCopyBufferToImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueMapBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueMapBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;[I)Ljava/nio/ByteBuffer;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueMapImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueMapImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJ[J[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;[I)Ljava/nio/ByteBuffer;";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueUnmapMemObjectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueUnmapMemObjectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Ljava/nio/ByteBuffer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueMigrateMemObjectsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueMigrateMemObjectsNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_mem;JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueNDRangeKernelNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueNDRangeKernelNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_kernel;I[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueTaskNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueTaskNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_kernel;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueNativeKernelNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueNativeKernelNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/EnqueueNativeKernelFunction;Ljava/lang/Object;JI[Lorg/jocl/cl_mem;[Lorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueMarkerWithWaitListNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueMarkerWithWaitListNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueBarrierWithWaitListNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueBarrierWithWaitListNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clSetPrintfCallbackNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetPrintfCallbackNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;Lorg/jocl/PrintfCallbackFunction;Ljava/lang/Object;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueSVMFreeNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueSVMFreeNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/Pointer;Lorg/jocl/SVMFreeFunction;Ljava/lang/Object;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueSVMMemcpyNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueSVMMemcpyNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;ZLorg/jocl/Pointer;Lorg/jocl/Pointer;JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueSVMMemFillNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueSVMMemFillNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/Pointer;Lorg/jocl/Pointer;JJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueSVMMapNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueSVMMapNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;ZJLorg/jocl/Pointer;JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueSVMUnmapNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueSVMUnmapNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueMarkerNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueMarkerNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueWaitForEventsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueWaitForEventsNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);

    nativeMethod.name = "clEnqueueBarrierNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueBarrierNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);


#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clCreateFromGLBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateFromGLBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JI[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (CL_GL_INTEROP_ENABLED)
//#if defined(CL_VERSION_1_2)
    nativeMethod.name = "clCreateFromGLTextureNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateFromGLTextureNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JIII[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);
//#endif
#endif

#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clCreateFromGLTexture2DNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateFromGLTexture2DNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JIII[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clCreateFromGLTexture3DNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateFromGLTexture3DNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JIII[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clCreateFromGLRenderbufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateFromGLRenderbufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;JI[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clGetGLObjectInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetGLObjectInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;[I[I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clGetGLTextureInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetGLTextureInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clEnqueueAcquireGLObjectsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueAcquireGLObjectsNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_mem;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (CL_GL_INTEROP_ENABLED)
    nativeMethod.name = "clEnqueueReleaseGLObjectsNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueReleaseGLObjectsNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;I[Lorg/jocl/cl_mem;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

nativeMethod.name = "clGetGLContextInfoAPPLENative";
nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetGLContextInfoAPPLENative;
nativeMethod.signature = "(Lorg/jocl/cl_context;JIJLorg/jocl/Pointer;[J)I";
env->RegisterNatives(cls, &nativeMethod, 1);

}

//===========================================================================
// These are deprecated and will be omitted in future releases:

/*
 * Class:     org_jocl_CL
 * Method:    allocateAlignedNative
 * Signature: (IILorg/jocl/Pointer;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_allocateAlignedNative
  (JNIEnv *env, jclass UNUSED(cls), jint size, jint alignment, jobject pointer)
{
    void *memory = malloc(size + (alignment-1) + sizeof(void*));
    if (memory == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory while allocating aligned memory");
        return NULL;
    }

    char *alignedMemory = ((char*)memory) + sizeof(void*);
    alignedMemory += alignment - ((intptr_t)alignedMemory & (alignment-1));
    ((void**)alignedMemory)[-1] = memory;

    memset(alignedMemory, 0, (size_t)size);

    env->SetLongField(pointer, NativePointerObject_nativePointer, (jlong)alignedMemory);

    jobject result = env->NewDirectByteBuffer(alignedMemory, size);
    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    freeAlignedNative
 * Signature: (Lorg/jocl/Pointer;)V
 */
JNIEXPORT void JNICALL Java_org_jocl_CL_freeAlignedNative
  (JNIEnv *env, jclass UNUSED(cls), jobject pointer)
{
    void *alignedMemory = (void*)env->GetLongField(pointer, NativePointerObject_nativePointer);
    free( ((void**)alignedMemory)[-1] );
}





/*
 * Class:     org_jocl_CL
 * Method:    clGetGLContextInfoKHRNative
 * Signature: (Lorg/jocl/cl_context_properties;IJLorg/jocl/Pointer;[J)I
 */
/*
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetGLContextInfoKHRNative
  (JNIEnv *env, jclass UNUSED(cls), jobject properties, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetGLContextInfoKHR\n");
    if (clGetGLContextInfoKHRFP == NULL)
    {
        ThrowByName(env, "java/lang/UnsupportedOperationException",
            "The function clGetGLContextInfoKHR is not supported");
        return CL_INVALID_OPERATION;
    }

    // Native variables declaration
    cl_context_properties *nativeProperties = NULL;
    cl_context_info nativeParam_name = 0;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret = 0;

    // Obtain native variable values
    if (properties != NULL)
    {
        nativeProperties = createContextPropertiesArray(env, properties);
        if (nativeProperties == NULL)
        {
            return NULL;
        }
    }
    nativeParam_name = (cl_context_info)param_name;
    nativeParam_value_size = (size_t)param_value_size;
    PointerData *param_valuePointerData = initPointerData(env, param_value);
    if (param_valuePointerData == NULL)
    {
        return CL_INVALID_HOST_PTR;
    }
    nativeParam_value = (void*)param_valuePointerData->pointer;

    int result = (clGetGLContextInfoKHRFP)(nativeProperties, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    delete[] nativeProperties;
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}
*/

// Notes about NON_BLOCKING_READ:
// When a non-blocking read operation is enqueued, there are two options:
// 1. The memory may be read into a direct buffer
//    In this case, there is not really a problem, because
//    the address of this buffer, which is obtained with
//    GetDirectBufferAddress, will be vaild until the
//    buffer is garbage collected
// 2. The memory may be read into an array
//    In this case, the releasePointerData function will possibly have
//    to write back the data into the Java array (namely, if the array
//    could not be pinned). This means that the function should not
//    be called before the read operation has finished. But this can
//    not be detected without using event callbacks, which are only
//    available in OpenCL 1.1.
// Also see the notes about NON_BLOCKING_READ in CL.java



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

#include "JOCL.hpp"

// GL includes
/*
#include <GL/glew.h>
#if defined (__APPLE__) || defined(MACOSX)
    #include <GLUT/glut.h>
#else
    #include <GL/glut.h>
#endif

#ifdef UNIX
    #if defined (__APPLE__) || defined(MACOSX)
        #include <OpenGL/OpenGL.h>
        #include <GLUT/glut.h>
    #else
        #include <GL/glx.h>
    #endif
#endif
*/

// CL includes
#if defined(__APPLE__) || defined(__MACOSX)
    #include <OpenCL/opencl.h>
#else
    #include <CL/opencl.h>
    #ifdef _WIN32
        #define WINDOWS_LEAN_AND_MEAN
        #define NOMINMAX
        // Disable "unreferenced formal parameter"
        // warning (for the JNIEnv parameter)
        #pragma warning (disable : 4100) 
        #include <windows.h>
    #endif // _WIN32
    #include <GL/gl.h>
#endif // __APPLE__



#include <string.h>
#include "Logger.hpp"
#include <string>
#include <map>

// The JVM, used for attaching the calling thread in
// callback functions
static JavaVM *globalJvm;

// Static method ID for the java.lang.Object class
static jmethodID Object_getClass; // ()Ljava/lang/Class;

// Static method IDs for the java.lang.Class class
static jmethodID Class_getComponentType; // ()Ljava/lang/Class;
static jmethodID Class_newInstance; // ()Ljava/lang/Object;

// Static method ID for the java.lang.String class
static jmethodID String_getBytes; // [B

// Static method IDs for the java.nio.Buffer class
static jmethodID Buffer_isDirect; // ()Z
static jmethodID Buffer_hasArray; // ()Z
static jmethodID Buffer_array; // ()Ljava/lang/Object;

// Static field ID for the org.jocl.NativePointerObject fields
static jfieldID NativePointerObject_nativePointer; // long
static jfieldID NativePointerObject_buffer; // Ljava.nio.Buffer;
static jfieldID NativePointerObject_pointers; // [org.jocl.NativePointerObject;
static jfieldID NativePointerObject_byteOffset; // long

// Static class and method ID for org.jocl.Pointer and its constructor
static jclass Pointer_Class;
static jmethodID Pointer_Constructor;

// Static field IDs for the cl_image_format class
static jfieldID cl_image_format_image_channel_order; // cl_channel_order (cl_uint)
static jfieldID cl_image_format_image_channel_data_type; // cl_channel_type (cl_uint)


// Static method IDs for the "function pointer" interfaces
static jmethodID CreateContextFunction_function; // (Ljava/lang/String;Lorg/jocl/Pointer;JLjava/lang/Object;)V
static jmethodID BuildProgramFunction_function; // (Lorg/jocl/cl_program;Ljava/lang/Object;)V
static jmethodID EnqueueNativeKernelFunction_function; // (Ljava/lang/Object;)V
static jmethodID MemObjectDestructorCallback_function; // (Lorg/jocl/cl_mem;Ljava/lang/Object;)V
static jmethodID EventCallback_function; // (Lorg/jocl/cl_event;ILjava/lang/Object;)V

// Static class and method ID for cl_platform_id and its constructor
static jclass cl_platform_id_Class;
static jmethodID cl_platform_id_Constructor;

// Static class and method ID for cl_device_id and its constructor
static jclass cl_device_id_Class;
static jmethodID cl_device_id_Constructor;

// Static class and method ID for cl_context and its constructor
static jclass cl_context_Class;
static jmethodID cl_context_Constructor;

// Static class and method ID for cl_command_queue and its constructor
static jclass cl_command_queue_Class;
static jmethodID cl_command_queue_Constructor;

// Static class and method ID for cl_mem and its constructor
static jclass cl_mem_Class;
static jmethodID cl_mem_Constructor;

// Static class and method ID for cl_image_format and its constructor
static jclass cl_image_format_Class;
static jmethodID cl_image_format_Constructor;

// Static class and method ID for cl_sampler and its constructor
static jclass cl_sampler_Class;
static jmethodID cl_sampler_Constructor;

// Static class and method ID for cl_program and its constructor
static jclass cl_program_Class;
static jmethodID cl_program_Constructor;

// Static class and method ID for cl_kernel and its constructor
static jclass cl_kernel_Class;
static jmethodID cl_kernel_Constructor;

// Static class and method ID for cl_event and its constructor
static jclass cl_event_Class;
static jmethodID cl_event_Constructor;


/**
 * Typedef for function pointers that may be passed to the
 * clCreateContext* functions
 */
typedef void(CL_CALLBACK *CreateContextFunctionPointer)(const char *errinfo, const void *private_info, size_t cb, void *user_data);

/**
 * Typedef for function pointers that may be passed to the
 * clBuildProgram function
 */
typedef void(CL_CALLBACK *BuildProgramFunctionPointer)(cl_program program, void *user_data);

/**
 * Typedef for function pointers that may be passed to the
 * clEnqueueNativeKernelFunction function
 */
typedef void(*EnqueueNativeKernelFunctionPointer)(void *user_data);

/**
 * Typedef for function pointers that may be passed to the
 * clSetMemObjectDestructorCallback function
 */
typedef void(CL_CALLBACK *MemObjectDestructorCallbackFunctionPointer)(cl_mem memobj, void *user_data);

/**
 * Typedef for function pointers that may be passed to the
 * clSetEventCallback function
 */
typedef void(CL_CALLBACK *EventCallbackFunctionPointer)(cl_event event, cl_int event_command_exec_status, void *user_data);



/**
 * A structure containing the information about the arguments that have
 * been passed to establish a callback method. A pointer to this structure
 * will be passed as the *user_data to the respective function. The
 * function will then use the data from the given structure
 * to call the Java callback method.
 */
typedef struct
{
    /**
     * A global reference to the user_data that was given
     */
    jobject globalUser_data;

    /**
     * A global reference to the pfn_notify that was given
     */
    jobject globalPfn_notify;

} CallbackInfo;

/**
 * The CallbackInfo structures of all contexts that have
 * been created so far and not released yet
 */
std::map<cl_context, CallbackInfo*> contextCallbackMap;



/**
 * The type of a native host memory pointer. The initPointerData function
 * will set the MemoryType of the Pointer to the respective value, so
 * that during the corresponding call to releasePointerData the actions
 * appropriate for releasing the respective memory type can be performed
 */
enum MemoryType
{
    NATIVE, POINTERS, DIRECT, ARRAY, ARRAY_COPY
};

/**
 * A structure containing all information necessary for maintaining
 * a pointer to java memory, i.e. to a Java Pointer object.
 */
typedef struct PointerData
{
    /** A global reference to the Java Pointer object */
    jobject pointerObject;

    /** 
     * A global reference to the primitive array in the 
     * Buffer of the Java Pointer object 
     */
    jarray array;

    /** The starting address of the buffer or its array */
    jlong startPointer;

    /** The actual pointer to be used, including offsets */
    jlong pointer;

    /** The type of the memory the pointer points to */
    MemoryType memoryType;

    /** The data of pointers the pointer points to */
    PointerData **pointers;

} PointerData;


/**
 * A map from cl_event objects to the PointerDatas that have
 * to be released when the event has finished.
 */
// XXX NON_BLOCKING_READ std::map<cl_event, PointerData*> pendingPointerDataMap;


//=== JNI initialization helper functions ====================================


/**
 * Initialize the specified field ID, and return whether
 * the initialization succeeded
 */
bool init(JNIEnv *env, jclass cls, jfieldID& field, const char *name, const char *signature)
{
    field = env->GetFieldID(cls, name, signature);
    if (field == NULL)
    {
        Logger::log(LOG_ERROR, "Failed to access field '%s'\n", name);
        return false;
    }
    return true;
}

/**
 * Initialize the specified method ID, and return whether
 * the initialization succeeded
 */
bool init(JNIEnv *env, jclass cls, jmethodID& method, const char *name, const char *signature)
{
    method = env->GetMethodID(cls, name, signature);
    if (method == NULL)
    {
        Logger::log(LOG_ERROR, "Failed to access method '%s'\n", name);
        return false;
    }
    return true;
}

/**
 * Initialize the given jclass, and return whether
 * the initialization succeeded
 */
bool init(JNIEnv *env, jclass& cls, const char *name)
{
    cls = env->FindClass(name);
    if (cls == NULL)
    {
        Logger::log(LOG_ERROR, "Failed to access class '%s'\n", name);
        return false;
    }
    return true;
}



/**
 * Creates a global reference to the class with the given name and
 * stores it in the given jclass argument, and stores the no-args
 * constructor ID for this class in the given jmethodID.
 * Returns whether this initialization succeeded.
 */
bool init(JNIEnv *env, const char *className, jclass &globalCls, jmethodID &constructor)
{
    jclass cls = NULL;
    if (!init(env, cls, className)) return false;
    if (!init(env, cls, constructor, "<init>", "()V")) return false;

    globalCls = (jclass)env->NewGlobalRef(cls);
    if (globalCls == NULL)
    {
        Logger::log(LOG_ERROR, "Failed to create reference to class %s\n", className);
        return false;
    }
    return true;
}


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

    globalJvm = jvm;

    jclass cls = NULL;

    if (!init(env, cls, "org/jocl/CL")) return JNI_ERR;
    registerAllNatives(env, cls);

    // Obtain the methodID for Object#getClass
    if (!init(env, cls, "java/lang/Object")) return JNI_ERR;
    if (!init(env, cls, Object_getClass, "getClass", "()Ljava/lang/Class;")) return JNI_ERR;

    // Obtain the methodID for Class#getComponentType
    if (!init(env, cls, "java/lang/Class")) return JNI_ERR;
    if (!init(env, cls, Class_getComponentType, "getComponentType", "()Ljava/lang/Class;")) return JNI_ERR;
    if (!init(env, cls, Class_newInstance,      "newInstance",      "()Ljava/lang/Object;")) return JNI_ERR;

    // Obtain the methodID for String#getBytes
    if (!init(env, cls, "java/lang/String")) return JNI_ERR;
    if (!init(env, cls, String_getBytes, "getBytes", "()[B")) return JNI_ERR;

    // Obtain the methodIDs for Buffer#hasArray and Buffer#array
    if (!init(env, cls, "java/nio/Buffer")) return JNI_ERR;
    if (!init(env, cls, Buffer_isDirect, "isDirect", "()Z"                 )) return JNI_ERR;
    if (!init(env, cls, Buffer_hasArray, "hasArray", "()Z"                 )) return JNI_ERR;
    if (!init(env, cls, Buffer_array,    "array",    "()Ljava/lang/Object;")) return JNI_ERR;

    // Obtain the fieldIDs of the NativePointerObject class
    if (!init(env, cls, "org/jocl/NativePointerObject")) return JNI_ERR;
    if (!init(env, cls, NativePointerObject_nativePointer, "nativePointer", "J")) return JNI_ERR;
    if (!init(env, cls, NativePointerObject_buffer,        "buffer",        "Ljava/nio/Buffer;")) return JNI_ERR;
    if (!init(env, cls, NativePointerObject_pointers,      "pointers",      "[Lorg/jocl/NativePointerObject;" )) return JNI_ERR;
    if (!init(env, cls, NativePointerObject_byteOffset,    "byteOffset",    "J")) return JNI_ERR;

    // Obtain the fieldIDs of the cl_image_format class
    if (!init(env, cls, "org/jocl/cl_image_format")) return JNI_ERR;
    if (!init(env, cls, cl_image_format_image_channel_order,     "image_channel_order",     "I")) return JNI_ERR;
    if (!init(env, cls, cl_image_format_image_channel_data_type, "image_channel_data_type", "I")) return JNI_ERR;

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

    // Obtain the global class references and the constructor methodIDs
    // for classes which will have to be instantiated
    if (!init(env, "org/jocl/Pointer",           Pointer_Class,          Pointer_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_platform_id",    cl_platform_id_Class,   cl_platform_id_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_device_id",      cl_device_id_Class,     cl_device_id_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_context",        cl_context_Class,       cl_context_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_command_queue",  cl_command_queue_Class, cl_command_queue_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_mem",            cl_mem_Class,           cl_mem_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_image_format",   cl_image_format_Class,  cl_image_format_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_sampler",        cl_sampler_Class,       cl_sampler_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_program",        cl_program_Class,       cl_program_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_kernel",         cl_kernel_Class,        cl_kernel_Constructor)) return JNI_ERR;
    if (!init(env, "org/jocl/cl_event",          cl_event_Class,         cl_event_Constructor)) return JNI_ERR;

    return JNI_VERSION_1_4;
}


JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
    // Deleting global references here should not be necessary...
}





//=== JNI helper functions ===================================================


/**
 * Throws a new Java Exception that is identified by the given name, e.g.
 * "java/lang/IllegalArgumentException" and contains the given message,
 * if there is no pending exception.
 */
void ThrowByName(JNIEnv *env, const char *name, const char *msg)
{
    if (!env->ExceptionCheck())
    {
        jclass cls = env->FindClass(name);
        if (cls != NULL)
        {
            env->ThrowNew(cls, msg);
        }
    }
}


/**
 * Initializes a PointerData with the data from the given Java NativePointerObject.
 *
 * If the given pointerObject is NULL, the method simply sets the startPointer
 * and pointer of the pointerData to NULL and returns it.
 *
 * Otherwise, this method will initialize the startPointer of the PointerData,
 * and the pointer of the PointerData will be set to
 *     startPointer+byteOffset
 * where byteOffset is the byteOffset that is obtained from the Java Pointer
 * object.
 *
 * By default, the startPointer of the PointerData will be initialized with
 * the native pointer value from the given Java Pointer object. If this
 * startPointer is non-NULL, the method sets memoryType to NATIVE and
 * returns it.
 *
 * If the array of Java Pointers that the Pointer points to is non-NULL, then
 * the startPointer of the PointerData be set to point to an array of
 * void* pointers that correspond to the values of the Java Pointers
 * from the array. If this array can be created, the method sets the
 * memoryType to POINTERS and returns the PointerData.
 *
 * If the Buffer of the Pointer is non-null, then the startPointer will be
 * obtained from the buffer:
 * - If the Buffer is direct, this method sets the startPointer to the
 *   direct buffer address, sets memoryType to DIRECT and returns the
 *   PointerData
 * - If the buffer has an array, the method sets the startPointer to the
 *   array, sets memoryType to ARRAY or ARRAY_COPY, indicating whether
 *   the array was pinned or copied, and returns the PointerData.
 *
 * If none of these attempts of obtaining the startPointer was
 * successful, then the method returns the empty PointerData.
 *
 * If an Exception occurs, NULL is returned.
 */
PointerData* initPointerData(JNIEnv *env, jobject pointerObject)
{
    Logger::log(LOG_DEBUGTRACE, "Initializing pointer data for Java Pointer object %p\n", pointerObject);

    PointerData *pointerData = new PointerData();
    if (pointerData == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory while initializing pointer data");
        return NULL;
    }

    pointerData->startPointer = NULL;
    pointerData->pointer = NULL;
    pointerData->memoryType = NATIVE;

    if (pointerObject == NULL)
    {
        return pointerData;
    }
    else
    {
        pointerData->pointerObject = env->NewGlobalRef(pointerObject);
        if (pointerData->pointerObject == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory while creating reference to pointer object");
            return NULL;
        }
    }


    pointerData->startPointer = env->GetLongField(pointerData->pointerObject, NativePointerObject_nativePointer);

    // Set the actual pointer to be the startPointer + the byte offset
    long byteOffset = (long)env->GetLongField(pointerObject, NativePointerObject_byteOffset);
    pointerData->pointer = (jlong)(((char*)pointerData->startPointer)+byteOffset);

    if (pointerData->startPointer != NULL)
    {
        Logger::log(LOG_DEBUGTRACE, "Obtaining native pointer %p\n", (void*)pointerData->startPointer);

        pointerData->memoryType = NATIVE;
        return pointerData;
    }

    // Obtain the array of pointers the pointer points to
    jobjectArray pointersArray = (jobjectArray)env->GetObjectField(pointerObject, NativePointerObject_pointers);
    if (pointersArray != NULL)
    {
        Logger::log(LOG_DEBUGTRACE, "Obtaining pointers in host memory\n");

        // Create an array containing the native representations of the
        // pointers, and store them as the data of the pointerData
        jsize size = env->GetArrayLength(pointersArray);
        void **localPointer = new void*[size];
        PointerData **localPointerDatas = new PointerData*[size];

        if (localPointer == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory while obtaining native pointers");
            return NULL;
        }
        for (int i=0; i<size; i++)
        {
            jobject p = env->GetObjectArrayElement(pointersArray, i);
            if (env->ExceptionCheck())
            {
                return NULL;
            }
            if (p != NULL)
            {
                // Initialize a PointerData for the pointer object that
                // the pointer points to
                PointerData *localPointerData = initPointerData(env, p);
                if (localPointerData == NULL)
                {
                    return NULL;
                }
                localPointerDatas[i] = localPointerData;
                localPointer[i] = (void*)localPointerData->startPointer;
            }
            else
            {
                localPointerDatas[i] = NULL;
                localPointer[i] = NULL;
            }
        }
        pointerData->pointers = localPointerDatas;
        pointerData->startPointer = (jlong)localPointer;

        // Set the actual pointer to be the startPointer + the byte offset
        long byteOffset = (long)env->GetLongField(pointerObject, NativePointerObject_byteOffset);
        pointerData->pointer = (jlong)(((char*)pointerData->startPointer)+byteOffset);

        pointerData->memoryType = POINTERS;
        return pointerData;
    }

    jobject buffer = env->GetObjectField(pointerObject, NativePointerObject_buffer);
    if (buffer != NULL)
    {
        // Check if the buffer is direct
        jboolean isDirect = env->CallBooleanMethod(buffer, Buffer_isDirect);
        if (env->ExceptionCheck())
        {
            return NULL;
        }

        if (isDirect==JNI_TRUE)
        {
            Logger::log(LOG_DEBUGTRACE, "Obtaining host memory from direct java buffer\n");

            // Obtain the direct buffer address from the given buffer
            pointerData->startPointer = (jlong)env->GetDirectBufferAddress(buffer);
            if (pointerData->startPointer == 0)
            {
                ThrowByName(env, "java/lang/IllegalArgumentException",
                    "Failed to obtain direct buffer address");
                return NULL;
            }
            pointerData->memoryType = DIRECT;

            // Set the actual pointer to be the startPointer + the byte offset
            long byteOffset = (long)env->GetLongField(pointerObject, NativePointerObject_byteOffset);
            pointerData->pointer = (jlong)(((char*)pointerData->startPointer)+byteOffset);

            return pointerData;
        }

        // Check if the buffer has an array
        jboolean hasArray = env->CallBooleanMethod(buffer, Buffer_hasArray);
        if (env->ExceptionCheck())
        {
            return NULL;
        }

        if (hasArray==JNI_TRUE)
        {
            Logger::log(LOG_DEBUGTRACE, "Obtaining host memory from array in java buffer\n");

            long byteOffset = (long)env->GetLongField(pointerObject, NativePointerObject_byteOffset);

            jarray localArray = (jarray)env->CallObjectMethod(buffer, Buffer_array);
            if (env->ExceptionCheck())
            {
                return NULL;
            }
            jarray globalArray = (jarray)env->NewGlobalRef(localArray);
            if (globalArray == NULL)
            {
                return NULL;
            }
            pointerData->array = globalArray;

            jboolean isCopy = JNI_FALSE;
            pointerData->startPointer = (jlong)env->GetPrimitiveArrayCritical(globalArray, &isCopy); 
            if (pointerData->startPointer == 0)
            {
                return NULL;
            }

            if (isCopy)
            {
                pointerData->memoryType = ARRAY_COPY;
            }
            else
            {
                pointerData->memoryType = ARRAY;
            }

            // Set the actual pointer to be the startPointer + the byte offset
            pointerData->pointer = (jlong)(((char*)pointerData->startPointer)+byteOffset);

            return pointerData;
        }

        // The buffer is neither direct nor has an array - should have
        // been checked on Java side
        Logger::log(LOG_ERROR, "Buffer is neither direct nor has an array\n");
        ThrowByName(env, "java/lang/IllegalArgumentException",
            "Buffer is neither direct nor has an array");
        return NULL;
    }

    return pointerData;
}



/**
 * Tries to convert the given pointer into a Java NativePointerObject
 * of the type appropriate for the given array, and stores the object
 * in the given array at the given index.
 * Returns 'true' if this object could be created and stored, or
 * 'false' if an exception occurred.
 */
bool createPointerObject(JNIEnv *env, jobjectArray pointersArray, int index, void *pointer)
{
    Logger::log(LOG_DEBUGTRACE, "Creating result pointer object at index %d for native pointer %p\n", index, pointer);

    jobject pointersArrayClassObject = env->CallObjectMethod(pointersArray, Object_getClass);
    if (env->ExceptionCheck())
    {
        return false;
    }
    jobject pointersArrayComponentTypeClass = env->CallObjectMethod(pointersArrayClassObject, Class_getComponentType);
    if (env->ExceptionCheck())
    {
        return false;
    }
    if (pointersArrayComponentTypeClass != NULL)
    {
        jobject resultObject = env->CallObjectMethod(pointersArrayComponentTypeClass, Class_newInstance);
        if (env->ExceptionCheck())
        {
            return false;
        }
        env->SetObjectArrayElement(pointersArray, index, resultObject);
        if (env->ExceptionCheck())
        {
            return false;
        }
        env->SetLongField(resultObject, NativePointerObject_nativePointer, (jlong)pointer);
        env->SetLongField(resultObject, NativePointerObject_byteOffset, 0);
        return true;
    }
    return false;
}




/**
 * Release the given PointerData and deletes the PointerData object.
 *
 * If the pointerObject of the given pointerData is NULL, the method simply
 * deletes the pointerData, sets it to NULL and returns true.
 *
 * Otherwise, the actions are depending on the memoryType of the pointerData:
 *
 * - For NATIVE or DIRECT memory, only the global reference to the
 *   Java pointer object is released
 * - For ARRAY memory, the respective java primitive array will be released
 * - For ARRAY_COPY memory, the respective java primitive array will be
 *   released, and the memory will be copied back (except when the given
 *   mode is JNI_ABORT)
 * - For POINTERS memory, the values of the pointers will be written
 *   back into the nativePointer field of the corresponding Java pointers.
 *
 * The method returns whether the respective operation succeeded.
 */
bool releasePointerData(JNIEnv *env, PointerData* &pointerData, jint mode=0)
{
    if (pointerData->memoryType == NATIVE || pointerData->memoryType == DIRECT)
    {
        Logger::log(LOG_DEBUGTRACE, "Releasing pointer data for direct or native data\n");

        env->DeleteGlobalRef(pointerData->pointerObject);
        delete pointerData;
        pointerData = NULL;
        return true;
    }
    else if (pointerData->memoryType == ARRAY_COPY)
    {
        Logger::log(LOG_DEBUGTRACE, "Releasing host memory from copied array in java buffer\n");

        env->ReleasePrimitiveArrayCritical(pointerData->array, (void*)pointerData->startPointer, mode);

        env->DeleteGlobalRef(pointerData->pointerObject);
        env->DeleteGlobalRef(pointerData->array);
        delete pointerData;
        pointerData = NULL;
        return true;
    }
    else if (pointerData->memoryType == ARRAY)
    {
        Logger::log(LOG_DEBUGTRACE, "Releasing host memory from array in java buffer\n");

        env->ReleasePrimitiveArrayCritical(pointerData->array, (void*)pointerData->startPointer, JNI_ABORT);

        env->DeleteGlobalRef(pointerData->pointerObject);
        env->DeleteGlobalRef(pointerData->array);
        delete pointerData;
        pointerData = NULL;
        return true;
    }
    else if (pointerData->memoryType == POINTERS)
    {
        Logger::log(LOG_DEBUGTRACE, "Releasing host memory of pointers\n");

        // Write the data from the host pointer back into the
        // Java pointers in the pointer array
        jobjectArray pointersArray = (jobjectArray)env->GetObjectField(
            pointerData->pointerObject, NativePointerObject_pointers);
        jsize size = env->GetArrayLength(pointersArray);
        void **localPointer = (void**)pointerData->startPointer;
        for (int i=0; i<size; i++)
        {
            // Obtain the native pointer object at the current index,
            // and set its nativePointer value to the value from the
            // native array
            jobject p = env->GetObjectArrayElement(pointersArray, i);
            if (env->ExceptionCheck())
            {
                return false;
            }

            if (p != NULL)
            {
                env->SetLongField(p, NativePointerObject_nativePointer, (jlong)localPointer[i]);
                env->SetLongField(p, NativePointerObject_byteOffset, 0);
            }
            else if (localPointer[i] != NULL)
            {
                // If the object at the target position is 'null', but
                // the value in the native array is not NULL, then try
                // to create the appropriate pointer object for the
                // target array and store the non-NULL value in the
                // created object
                if (!createPointerObject(env, pointersArray, i, localPointer[i]))
                {
                    return false;
                }
            }
        }

        // Release the PointerDatas for the pointer objects that
        // the pointer points to
        PointerData **localPointerDatas = pointerData->pointers;
        if (localPointerDatas != NULL)
        {
            for (int i=0; i<size; i++)
            {
                if (localPointerDatas[i] != NULL)
                {
                    if (!releasePointerData(env, localPointerDatas[i])) return false;
                }
            }
            delete[] localPointerDatas;
        }
        delete[] (void**)pointerData->startPointer;
        env->DeleteGlobalRef(pointerData->pointerObject);
        delete pointerData;
        pointerData = NULL;
        return true;
    }
    return true;
}


/**
 * Set the nativePointer in the given Java NativePointerObject to the given
 * pointer. The byteOffset will be set to 0.
 */
void setNativePointer(JNIEnv *env, jobject pointerObject, jlong pointer)
{
    if (pointerObject == NULL)
    {
        return;
    }
    env->SetLongField(pointerObject, NativePointerObject_nativePointer, pointer);
    env->SetLongField(pointerObject, NativePointerObject_byteOffset, 0);
}




/**
 * Set the element at the given index in the given array to
 * the given value. If the array is NULL, nothing is done.
 * Returns 'false' if an OutOfMemoryError occurred.
 */
bool set(JNIEnv *env, jintArray ja, int index, long value)
{
    if (ja == NULL)
    {
        return true;
    }
    jint *a = (jint*)env->GetPrimitiveArrayCritical(ja, NULL);
    if (a == NULL)
    {
        return false;
    }
    a[index] = value;
    env->ReleasePrimitiveArrayCritical(ja, a, 0);
    return true;
}

/**
 * Set the element at the given index in the given array to
 * the given value. If the array is NULL, nothing is done.
 * Returns 'false' if an OutOfMemoryError occurred.
 */
bool set(JNIEnv *env, jlongArray ja, int index, long value)
{
    if (ja == NULL)
    {
        return true;
    }
    jlong *a = (jlong*)env->GetPrimitiveArrayCritical(ja, NULL);
    if (a == NULL)
    {
        return false;
    }
    a[index] = value;
    env->ReleasePrimitiveArrayCritical(ja, a, 0);
    return true;
}


/**
 * Set the element at the given index in the given array to
 * the given value. If the array is NULL, nothing is done.
 * Returns 'false' if an OutOfMemoryError occurred.
 */
bool set(JNIEnv *env, jfloatArray ja, int index, float value)
{
    if (ja == NULL)
    {
        return true;
    }
    jfloat *a = (jfloat*)env->GetPrimitiveArrayCritical(ja, NULL);
    if (a == NULL)
    {
        return false;
    }
    a[index] = value;
    env->ReleasePrimitiveArrayCritical(ja, a, 0);
    return true;
}






/**
 * Converts the given jstring into a 0-terminated char* and
 * returns it. To delete the char* is left to the caller.
 * The optional length pointer will store the length of
 * the converted string, WITHOUT the trailing 0. Returns
 * NULL if an arror occurs.
 */
char *convertString(JNIEnv *env, jstring js, int *length=NULL)
{
    jbyteArray bytes = 0;
    char *result = 0;
    if (env->EnsureLocalCapacity(2) < 0)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory during string reference creation");
        return NULL;
    }
    bytes = (jbyteArray)env->CallObjectMethod(js, String_getBytes);
    if (!env->ExceptionCheck())
    {
        jint len = env->GetArrayLength(bytes);
        if (length != NULL)
        {
            *length = (int)len;
        }
        result = new char[len + 1];
        if (result == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during string creation");
            return NULL;
        }
        env->GetByteArrayRegion(bytes, 0, len, (jbyte *)result);
        result[len] = 0;
    }
    return result;
}





/**
 * Converts the given jlongArray into a size_t* and returns it.
 * To delete the size_t* is left to the caller. Returns
 * NULL if an error occurs.
 */
size_t* convertArray(JNIEnv *env, jlongArray array)
{
    jsize arrayLength = env->GetArrayLength(array);
    size_t *result = new size_t[arrayLength];
    if (result == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory during array creation");
        return NULL;
    }
    jlong *jArray = (jlong*)env->GetPrimitiveArrayCritical(array, NULL);
    if (jArray == NULL)
    {
        return NULL;
    }
    for (int i=0; i<arrayLength; i++)
    {
        result[i] = (size_t)jArray[i];
    }
    env->ReleasePrimitiveArrayCritical(array, jArray, JNI_ABORT);
    return result;
}



/*
 * Class:     org_jocl_CL
 * Method:    setLogLevelNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_jocl_CL_setLogLevelNative
  (JNIEnv *env, jclass cls, jint logLevel)
{
    Logger::setLogLevel((LogLevel)logLevel);
}


/*
 * Class:     org_jocl_CL
 * Method:    allocateAlignedNative
 * Signature: (IILorg/jocl/Pointer;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_allocateAlignedNative
  (JNIEnv *env, jclass cls, jint size, jint alignment, jobject pointer)
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

    memset(alignedMemory, 0, size);

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
  (JNIEnv *env, jclass cls, jobject pointer)
{
    void *alignedMemory = (void*)env->GetLongField(pointer, NativePointerObject_nativePointer);
    free( ((void**)alignedMemory)[-1] );
}






//=== CL helper functions ====================================================


/**
 * Create the cl_context_properties array for the given properties
 * object. The given 'properties' object is a org.jocl.Pointer to
 * a long array (wrapped in a Buffer), which contains pairs of
 * identifiers and values, and is terminated with a 0. This array
 * is converted element-wise to a cl_context_properties array here.
 * The returned array must be freed by the caller! If an error
 * occurs, NULL is returned.
 */
cl_context_properties* createPropertiesArray(JNIEnv *env, jobject properties)
{
    if (properties == NULL)
    {
        return NULL;
    }
    PointerData *propertiesPointerData = initPointerData(env, properties);
    if (propertiesPointerData == NULL)
    {
        return NULL;
    }
    int javaPropertiesSize = 0;
    jlong *javaPropertyValues = (jlong*)propertiesPointerData->pointer;
    int MAX_PROPERTIES = 100;
    for (int i=0; i<MAX_PROPERTIES; i++)
    {
        if (javaPropertyValues[i] == 0)
        {
            break;
        }
        javaPropertiesSize++;
    }
    cl_context_properties *nativeProperties = new cl_context_properties[javaPropertiesSize + 1];
    if (nativeProperties == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory during property array creation");
        return NULL;
    }

    for (int i=0; i<javaPropertiesSize; i++)
    {
        nativeProperties[i] = (cl_context_properties)javaPropertyValues[i];
    }
    nativeProperties[javaPropertiesSize] = 0;
    if (!releasePointerData(env, propertiesPointerData, JNI_ABORT)) return NULL;
    return nativeProperties;
}



/**
 * Fills the native representation of the given Java object.
 */
void getCl_image_format(JNIEnv *env, jobject image_format, cl_image_format &nativeImage_format)
{
    nativeImage_format.image_channel_order = NULL;
    nativeImage_format.image_channel_data_type = NULL;
    if (image_format != NULL)
    {
        nativeImage_format.image_channel_order     = (cl_channel_order)env->GetIntField(image_format, cl_image_format_image_channel_order);
        nativeImage_format.image_channel_data_type = (cl_channel_type) env->GetIntField(image_format, cl_image_format_image_channel_data_type);
    }
}

/**
 * Assign the values of the given native object to the given
 * Java object.
 */
void setCl_image_format(JNIEnv *env, jobject image_format, cl_image_format &nativeImage_format)
{
    if (image_format != NULL)
    {
        env->SetIntField(image_format, cl_image_format_image_channel_order,     nativeImage_format.image_channel_order);
        env->SetIntField(image_format, cl_image_format_image_channel_data_type, nativeImage_format.image_channel_data_type);
    }
}


/**
 * Creates a list containing the native cl_event objects for the
 * java cl_event objects in the given java array. To delete the
 * returned array is left to the caller. The returned array will
 * have num_events entries. If one of the java objects is 'null',
 * then a NullPointerException will be thrown and the function
 * returns NULL. If the java array has less than num_events
 * elements, then and ArrayIndexOutOfBoundsException will be
 * thrown and the function returns NULL.
 * Returns NULL if an error occurs.
 */
cl_event* createEventList(JNIEnv *env, jobjectArray event_list, cl_uint num_events)
{
    cl_event* nativeEvent_list = new cl_event[num_events];
    if (nativeEvent_list == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory during event array creation");
        return NULL;
    }

    cl_uint event_listLength = (cl_uint)env->GetArrayLength(event_list);
    if (event_listLength < num_events)
    {
        ThrowByName(env, "java/lang/ArrayIndexOutOfBoundsException",
            "Event list size is smaller than specified number of events");
        return NULL;
    }
    for (unsigned int i=0; i<num_events; i++)
    {
        jobject ev = env->GetObjectArrayElement(event_list, i);
        if (env->ExceptionCheck())
        {
            delete[] nativeEvent_list;
            return NULL;
        }
        if (ev == NULL)
        {
            delete[] nativeEvent_list;
            ThrowByName(env, "java/lang/NullPointerException",
                "Event list contains 'null' elements");
            return NULL;
        }
        nativeEvent_list[i] = (cl_event)env->GetLongField(ev, NativePointerObject_nativePointer);
    }
    return nativeEvent_list;
}

/**
 * Creates a list containing the native cl_device_id objects for the
 * java cl_device_id objects in the given java array. To delete the
 * returned array is left to the caller. The returned array will
 * have num_devices entries. If one of the java objects is 'null',
 * then a NullPointerException will be thrown and the function
 * returns NULL. If the java array has less than num_devices
 * elements, then and ArrayIndexOutOfBoundsException will be
 * thrown and the function returns NULL.
 * Returns NULL if an error occurs.
 */
cl_device_id* createDeviceList(JNIEnv *env, jobjectArray device_list, cl_uint num_devices)
{
    cl_device_id *nativeDevice_list = new cl_device_id[num_devices];
    if (nativeDevice_list == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory during device array creation");
        return NULL;
    }

    cl_uint device_listLength = (cl_uint)env->GetArrayLength(device_list);
    if (device_listLength < num_devices)
    {
        ThrowByName(env, "java/lang/ArrayIndexOutOfBoundsException",
            "Device list size is smaller than specified number of devices");
        return NULL;
    }
    for (unsigned int i=0; i<num_devices; i++)
    {
        jobject device = env->GetObjectArrayElement(device_list, i);
        if (env->ExceptionCheck())
        {
            delete[] nativeDevice_list;
            return NULL;
        }
        if (device == NULL)
        {
            delete[] nativeDevice_list;
            ThrowByName(env, "java/lang/NullPointerException",
                "Device list contains 'null' elements");
            return NULL;
        }
        nativeDevice_list[i] = (cl_device_id)env->GetLongField(device, NativePointerObject_nativePointer);
    }
    return nativeDevice_list;
}


/**
 * Creates a list containing the native cl_mem objects for the
 * java cl_mem objects in the given java array. To delete the
 * returned array is left to the caller. The returned array will
 * have num_mems entries. If one of the java objects is 'null',
 * then a NullPointerException will be thrown and the function
 * returns NULL. If the java array has less than num_mems
 * elements, then and ArrayIndexOutOfBoundsException will be
 * thrown and the function returns NULL.
 * Returns NULL if an error occurs.
 */
cl_mem* createMemList(JNIEnv *env, jobjectArray mem_list, cl_uint num_mems)
{
    cl_mem *nativeMem_list = new cl_mem[num_mems];
    if (nativeMem_list == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory during mem array creation");
        return NULL;
    }

    cl_uint mem_listLength = (cl_uint)env->GetArrayLength(mem_list);
    if (mem_listLength < num_mems)
    {
        ThrowByName(env, "java/lang/ArrayIndexOutOfBoundsException",
            "Memory object list size is smaller than specified number of memory objects");
        return NULL;
    }
    for (unsigned int i=0; i<num_mems; i++)
    {
        jobject mem = env->GetObjectArrayElement(mem_list, i);
        if (env->ExceptionCheck())
        {
            delete[] nativeMem_list;
            return NULL;
        }
        if (mem == NULL)
        {
            delete[] nativeMem_list;
            ThrowByName(env, "java/lang/NullPointerException",
                "Memory object list contains 'null' elements");
            return NULL;
        }
        nativeMem_list[i] = (cl_mem)env->GetLongField(mem, NativePointerObject_nativePointer);
    }
    return nativeMem_list;
}




/**
 * Initializes and returns a CallbackInfo structure with the given
 * arguments. pfn_notify is the Java callback interface object
 * (may be NULL), and user_data is a java.lang.Object (may be NULL).
 * The returned CallbackInfo structure will contain global references
 * to the non-null arguments. Returns NULL if an arror occurs.
 */
CallbackInfo* initCallbackInfo(JNIEnv *env, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_DEBUGTRACE, "Executing initCallbackInfo\n");

    CallbackInfo *callbackInfo = new CallbackInfo();
    if (callbackInfo == NULL)
    {
        ThrowByName(env, "java/lang/OutOfMemoryError",
            "Out of memory when preparing callback function");
        return NULL;
    }
    callbackInfo->globalPfn_notify = NULL;
    callbackInfo->globalUser_data = NULL;
    if (pfn_notify != NULL)
    {
        callbackInfo->globalPfn_notify = env->NewGlobalRef(pfn_notify);
        if (env->ExceptionCheck())
        {
            delete callbackInfo;
            return NULL;
        }
    }
    if (user_data != NULL)
    {
        callbackInfo->globalUser_data = env->NewGlobalRef(user_data);
        if (env->ExceptionCheck())
        {
            delete callbackInfo;
            return NULL;
        }
    }
    return callbackInfo;
}


/**
 * Deletes the global references stored in the given CallbackInfo
 * object and deletes the object.
 */
void deleteCallbackInfo(JNIEnv *env, CallbackInfo* &callbackInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing deleteCallbackInfo\n");

    if (callbackInfo == NULL)
    {
        return;
    }
    if (callbackInfo->globalUser_data != NULL)
    {
        env->DeleteGlobalRef(callbackInfo->globalUser_data);
    }
    if (callbackInfo->globalPfn_notify != NULL)
    {
        env->DeleteGlobalRef(callbackInfo->globalPfn_notify);
    }
    delete callbackInfo;
    callbackInfo = NULL;
}


/**
 * Deletes the CallbackInfo that is associated with the
 * given cl_context and removes this mapping from the
 * contextCallbackMap
 */
void destroyCallbackInfo(JNIEnv *env, cl_context context)
{
    std::map<cl_context, CallbackInfo*>::iterator iter =
        contextCallbackMap.find(context);
    if (iter != contextCallbackMap.end())
    {
        contextCallbackMap.erase(iter);
        deleteCallbackInfo(env, iter->second);
    }
}



/**
 * TODO: Check how exceptions from callback functions may be handled
 * properly.
 *
 * This functions checks if an exception occurred, rethrows it
 * as a RuntimeExceptions if necessary, and detaches the current
 * thread from the JVM
 */
void finishCallback(JNIEnv *env)
{
    if (env->ExceptionCheck())
    {
        env->ExceptionClear();
        jclass newExceptionClass = env->FindClass("java/lang/RuntimeException");
        if (newExceptionClass == NULL)
        {
            globalJvm->DetachCurrentThread();
            return;
        }
        globalJvm->DetachCurrentThread();
        env->ThrowNew(newExceptionClass, "From CL callback");
        return;
    }
    globalJvm->DetachCurrentThread();
}



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
    globalJvm->AttachCurrentThread((void**)&env, NULL);

    jstring errinfoString = env->NewStringUTF(errinfo);

    // TODO: This should actually be a Pointer to the private_info,
    // but since this can not be used on Java side, simply pass
    // a NULL object...
    jobject private_infoObject = NULL;
    env->CallVoidMethod(pfn_notify, CreateContextFunction_function, errinfoString, private_infoObject, cb, user_data);

    finishCallback(env);
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
    globalJvm->AttachCurrentThread((void**)&env, NULL);

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
}


/**
 * A pointer to this function will be passed to the EnqueueNativeKernelFunction function
 * if a Java callback object was given. The argsInfo is a pointer to a
 * CallbackInfo that was initialized and is associated with the respective
 * call to clEnqueueNativeKernel.
 */
void EnqueueNativeKernelFunction(void *argsInfo)
{
    Logger::log(LOG_DEBUGTRACE, "Executing EnqueueNativeKernelFunction\n");

	JNIEnv *env = NULL;
    globalJvm->AttachCurrentThread((void**)&env, NULL);

    CallbackInfo *callbackInfo = (CallbackInfo*)argsInfo;
    jobject pfn_notify = callbackInfo->globalPfn_notify;
    if (pfn_notify != NULL)
    {
        jobject user_data = callbackInfo->globalUser_data;

        env->CallVoidMethod(pfn_notify, EnqueueNativeKernelFunction_function, user_data);
    }
	deleteCallbackInfo(env, callbackInfo);
    finishCallback(env);
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
    globalJvm->AttachCurrentThread((void**)&env, NULL);

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
    globalJvm->AttachCurrentThread((void**)&env, NULL);

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
}







//=== CL functions ===========================================================



/*
 * Class:     org_jocl_CL
 * Method:    clGetPlatformIDsNative
 * Signature: (I[Lorg/jocl/cl_platform_id;[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetPlatformIDsNative
  (JNIEnv *env, jclass cls, jint num_entries, jobjectArray platforms, jintArray num_platforms)
{
    Logger::log(LOG_TRACE, "Executing clGetPlatformIDs\n");

    // Native variables declaration
    cl_uint nativeNum_entries = 0;
    cl_platform_id *nativePlatforms = NULL;
    cl_uint nativeNum_platforms = 0;

    // Obtain native variable values
    nativeNum_entries = (cl_uint)num_entries;
    if (platforms != NULL)
    {
        jsize platformsLength = env->GetArrayLength(platforms);
        nativePlatforms = new cl_platform_id[platformsLength];
        if (nativePlatforms == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during platforms array creation");
            return NULL;
        }
    }

    int result = clGetPlatformIDs(nativeNum_entries, nativePlatforms, &nativeNum_platforms);

    // Write back native variable values and clean up
    if (platforms != NULL)
    {
        unsigned int n = nativeNum_entries < nativeNum_platforms ? nativeNum_entries : nativeNum_platforms;
        for (unsigned int i=0; i<n; i++)
        {
            jobject platform = env->GetObjectArrayElement(platforms, i);
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
                env->SetObjectArrayElement(platforms, i, platform);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setNativePointer(env, platform, (jlong)nativePlatforms[i]);

        }
        delete[] nativePlatforms;
    }
    if (!set(env, num_platforms, 0, nativeNum_platforms)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetPlatformInfoNative
 * Signature: (Lorg/jocl/cl_platform_id;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetPlatformInfoNative
  (JNIEnv *env, jclass cls, jobject platform, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetPlatformInfo\n");

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

    int result = clGetPlatformInfo(nativePlatform, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject platform, jlong device_type, jint num_entries, jobjectArray devices, jintArray num_devices)
{
    Logger::log(LOG_TRACE, "Executing clGetDeviceIDs\n");

    // Native variables declaration
    cl_platform_id nativePlatform = NULL;
    cl_device_type nativeDevice_type = NULL;
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
        nativeDevices = new cl_device_id[devicesLength];
        if (nativeDevices == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during devices array creation");
            return NULL;
        }
    }

    int result = clGetDeviceIDs(nativePlatform, nativeDevice_type, nativeNum_entries, nativeDevices, &nativeNum_devices);

    // Write back native variable values and clean up
    if (devices != NULL)
    {
        unsigned int n = nativeNum_entries < nativeNum_devices ? nativeNum_entries : nativeNum_devices;
        for (unsigned int i=0; i<n; i++)
        {
            jobject device = env->GetObjectArrayElement(devices, i);
            if (device == NULL)
            {
                device = env->NewObject(cl_device_id_Class, cl_device_id_Constructor);
                if (env->ExceptionCheck())
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(devices, i, device);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setNativePointer(env, device, (jlong)nativeDevices[i]);
        }
        delete[] nativeDevices;
    }
    if (!set(env, num_devices, 0, nativeNum_devices)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetDeviceInfoNative
 * Signature: (Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetDeviceInfoNative
  (JNIEnv *env, jclass cls, jobject device, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetDeviceInfo\n");

    // Native variables declaration
    cl_device_id nativeDevice = NULL;
    cl_device_info nativeParam_name = NULL;
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

    int result = clGetDeviceInfo(nativeDevice, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}






/*
 * Class:     org_jocl_CL
 * Method:    clCreateContextNative
 * Signature: (Lorg/jocl/cl_context_properties;I[Lorg/jocl/cl_device_id;Lorg/jocl/CreateContextFunction;Ljava/lang/Object;[I)Lorg/jocl/cl_context;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateContextNative
  (JNIEnv *env, jclass cls, jobject properties, jint num_devices, jobjectArray devices, jobject pfn_notify, jobject user_data, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateContext\n");

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
        nativeProperties = createPropertiesArray(env, properties);
        if (nativeProperties == NULL)
        {
            return NULL;
        }
    }
    nativeNum_devices = (cl_uint)num_devices;
    if (devices != NULL)
    {
        jsize devicesLength = env->GetArrayLength(devices);
        nativeDevices = new cl_device_id[devicesLength];
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
    nativePfn_notify = &CreateContextFunction;
    CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
    if (callbackInfo == NULL)
    {
        return NULL;
    }
    nativeUser_data = (void*)callbackInfo;


    nativeContext = clCreateContext(nativeProperties, nativeNum_devices, nativeDevices, nativePfn_notify, nativeUser_data, &nativeErrcode_ret);
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
  (JNIEnv *env, jclass cls, jobject properties, jlong device_type, jobject pfn_notify, jobject user_data, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateContextFromType\n");

    // Native variables declaration
    cl_context_properties *nativeProperties = NULL;
    cl_device_type nativeDevice_type = NULL;
    CreateContextFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;
    cl_int nativeErrcode_ret = 0;
    cl_context nativeContext = NULL;

    // Obtain native variable values
    nativeProperties = createPropertiesArray(env, properties);
    nativeDevice_type = (cl_device_type)device_type;
    nativePfn_notify = &CreateContextFunction;
    CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
    if (callbackInfo == NULL)
    {
        return NULL;
    }
    nativeUser_data = (void*)callbackInfo;


    nativeContext = clCreateContextFromType(nativeProperties, nativeDevice_type, nativePfn_notify, nativeUser_data, &nativeErrcode_ret);
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
  (JNIEnv *env, jclass cls, jobject context)
{
    Logger::log(LOG_TRACE, "Executing clRetainContext\n");

    cl_context nativeContext = NULL;
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    return clRetainContext(nativeContext);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseContextNative
 * Signature: (Lorg/jocl/cl_context;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseContextNative
  (JNIEnv *env, jclass cls, jobject context)
{
    Logger::log(LOG_TRACE, "Executing clReleaseContext\n");

    cl_context nativeContext = NULL;
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    int result = clReleaseContext(nativeContext);
    destroyCallbackInfo(env, nativeContext);
    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetContextInfoNative
 * Signature: (Lorg/jocl/cl_context;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetContextInfoNative
  (JNIEnv *env, jclass cls, jobject context, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetContextInfo\n");

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_context_info nativeParam_name = NULL;
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

    int result = clGetContextInfo(nativeContext, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject context, jobject device, jlong properties, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateCommandQueue\n");

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

    nativeCommand_queue = clCreateCommandQueue(nativeContext, nativeDevice, nativeProperties, &nativeErrcode_ret);

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


/*
 * Class:     org_jocl_CL
 * Method:    clRetainCommandQueueNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainCommandQueueNative
  (JNIEnv *env, jclass cls, jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clRetainCommandQueue\n");

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return clRetainCommandQueue(nativeCommand_queue);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseCommandQueueNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseCommandQueueNative
  (JNIEnv *env, jclass cls, jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clReleaseCommandQueue\n");

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return clReleaseCommandQueue(nativeCommand_queue);
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetCommandQueueInfoNative
 * Signature: (Lorg/jocl/cl_command_queue;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetCommandQueueInfoNative
  (JNIEnv *env, jclass cls, jobject command_queue, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetCommandQueueInfo\n");

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_command_queue_info nativeParam_name = NULL;
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

    int result = clGetCommandQueueInfo(nativeCommand_queue, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jlong properties, jboolean enable, jlongArray old_properties)
{
    Logger::log(LOG_TRACE, "Executing clSetCommandQueueProperty\n");

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
#elif
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
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jlong size, jobject host_ptr, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateBuffer\n");

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = NULL;
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

    nativeMem = clCreateBuffer(nativeContext, nativeFlags, nativeSize, nativeHost_ptr, &nativeErrcode_ret);

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






#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateSubBufferNative
 * Signature: (Lorg/jocl/cl_mem;IILorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateSubBufferNative
  (JNIEnv *env, jclass cls, jobject buffer, jlong flags, jint buffer_create_type, jobject buffer_create_info, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateSubBuffer\n");

    // Native variables declaration
    cl_mem nativeBuffer = NULL;
    cl_mem_flags nativeFlags = NULL;
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

    nativeMem = clCreateSubBuffer(nativeBuffer, nativeFlags, nativeBuffer_create_type, nativeBuffer_create_info, &nativeErrcode_ret);

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

#endif // defined(CL_VERSION_1_1)







/*
 * Class:     org_jocl_CL
 * Method:    clCreateImage2DNative
 * Signature: (Lorg/jocl/cl_context;J[Lorg/jocl/cl_image_format;JJJLorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateImage2DNative
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jobjectArray image_format, jlong image_width, jlong image_height, jlong image_row_pitch, jobject host_ptr, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateImage2D\n");

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = NULL;
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
        nativeImage_format = new cl_image_format[image_formatLength];
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

    nativeMem = clCreateImage2D(nativeContext, nativeFlags, nativeImage_format, nativeImage_width, nativeImage_height, nativeImage_row_pitch, nativeHost_ptr, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jobjectArray image_format, jlong image_width, jlong image_height, jlong image_depth, jlong image_row_pitch, jlong image_slice_pitch, jobject host_ptr, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateImage3D\n");

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = NULL;
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
        nativeImage_format = new cl_image_format[image_formatLength];
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

    nativeMem = clCreateImage3D(nativeContext, nativeFlags, nativeImage_format, nativeImage_width, nativeImage_height, nativeImage_depth, nativeImage_row_pitch, nativeImage_slice_pitch, nativeHost_ptr, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject memobj)
{
    Logger::log(LOG_TRACE, "Executing clRetainMemObject\n");

    cl_mem nativeMemobj = NULL;
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    return clRetainMemObject(nativeMemobj);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseMemObjectNative
 * Signature: (Lorg/jocl/cl_mem;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseMemObjectNative
  (JNIEnv *env, jclass cls, jobject memobj)
{
    Logger::log(LOG_TRACE, "Executing clReleaseMemObject\n");

    cl_mem nativeMemobj = NULL;
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    return clReleaseMemObject(nativeMemobj);
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetSupportedImageFormatsNative
 * Signature: (Lorg/jocl/cl_context;JII[Lorg/jocl/cl_image_format;[I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetSupportedImageFormatsNative
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jint image_type, jint num_entries, jobjectArray image_formats, jintArray num_image_formats)
{
    Logger::log(LOG_TRACE, "Executing clGetSupportedImageFormats\n");

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_mem_flags nativeFlags = NULL;
    cl_mem_object_type nativeImage_type = 0;
    cl_int nativeNum_entries = 0;
    cl_image_format *nativeImage_formats = NULL;
    cl_uint nativeNum_image_formats = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }
    nativeFlags = (cl_mem_flags)flags;
    nativeImage_type = (cl_mem_object_type)image_type;
    nativeNum_entries = (cl_int)num_entries;
    if (image_formats != NULL)
    {
        jsize image_formatsLength = env->GetArrayLength(image_formats);
        nativeImage_formats = new cl_image_format[image_formatsLength];
        if (nativeImage_formats == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during image formats array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = clGetSupportedImageFormats(nativeContext, nativeFlags, nativeImage_type, nativeNum_entries, nativeImage_formats, &nativeNum_image_formats);

    // Write back native variable values and clean up
    if (image_formats != NULL)
    {
        for (unsigned int i=0; i<nativeNum_image_formats; i++)
        {
            jobject image_format = env->GetObjectArrayElement(image_formats, i);
            if (image_format == NULL)
            {
                image_format = env->NewObject(cl_image_format_Class, cl_image_format_Constructor);
                if (env->ExceptionCheck())
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(image_formats, i, image_format);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setCl_image_format(env, image_format, nativeImage_formats[i]);
        }
        delete nativeImage_formats;
    }
    if (!set(env, num_image_formats, 0, nativeNum_image_formats)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetMemObjectInfoNative
 * Signature: (Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetMemObjectInfoNative
  (JNIEnv *env, jclass cls, jobject memobj, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetMemObjectInfo\n");

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

    int result = clGetMemObjectInfo(nativeMemobj, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject image, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetImageInfo\n");

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

    int result = clGetImageInfo(nativeImage, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}



#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clSetMemObjectDestructorCallbackNative
 * Signature: (Lorg/jocl/cl_mem;Lorg/jocl/MemObjectDestructorCallbackFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetMemObjectDestructorCallbackNative
  (JNIEnv *env, jclass cls, jobject memobj, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clSetMemObjectDestructorCallback\n");

    // Native variables declaration
    cl_mem nativeMemobj = NULL;
    MemObjectDestructorCallbackFunctionPointer nativePfn_notify = NULL;
    void *nativeUser_data = NULL;

    // Obtain native variable values
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }
    nativePfn_notify = &MemObjectDestructorCallback;
    CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
    if (callbackInfo == NULL)
    {
        return NULL;
    }
    nativeUser_data = (void*)callbackInfo;

    int result = clSetMemObjectDestructorCallback(nativeMemobj, nativePfn_notify, nativeUser_data);

    return result;
}

#endif // defined(CL_VERSION_1_1)


/*
 * Class:     org_jocl_CL
 * Method:    clCreateSamplerNative
 * Signature: (Lorg/jocl/cl_context;ZII[I)Lorg/jocl/cl_sampler;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateSamplerNative
  (JNIEnv *env, jclass cls, jobject context, jboolean normalized_coords, jint addressing_mode, jint filter_mode, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateSampler\n");

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

    nativeSampler = clCreateSampler(nativeContext, nativeNormalized_coords, nativeAddressing_mode, nativeFilter_mode, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject sampler)
{
    Logger::log(LOG_TRACE, "Executing clRetainSampler\n");

    cl_sampler nativeSampler = NULL;
    if (sampler != NULL)
    {
        nativeSampler = (cl_sampler)env->GetLongField(sampler, NativePointerObject_nativePointer);
    }
    return clRetainSampler(nativeSampler);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseSamplerNative
 * Signature: (Lorg/jocl/cl_sampler;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseSamplerNative
  (JNIEnv *env, jclass cls, jobject sampler)
{
    Logger::log(LOG_TRACE, "Executing clReleaseSampler\n");

    cl_sampler nativeSampler = NULL;
    if (sampler != NULL)
    {
        nativeSampler = (cl_sampler)env->GetLongField(sampler, NativePointerObject_nativePointer);
    }
    return clReleaseSampler(nativeSampler);
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetSamplerInfoNative
 * Signature: (Lorg/jocl/cl_sampler;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetSamplerInfoNative
  (JNIEnv *env, jclass cls, jobject sampler, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetSamplerInfo\n");

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

    int result = clGetSamplerInfo(nativeSampler, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject context, jint count, jobjectArray strings, jlongArray lengths, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateProgramWithSource\n");

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
        nativeStrings = new char*[stringsLength];
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

    nativeProgram = clCreateProgramWithSource(nativeContext, nativeCount, (const char**)nativeStrings, nativeLengths, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject context, jint num_devices, jobjectArray device_list, jlongArray lengths, jobjectArray binaries, jintArray binary_status, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateProgramWithBinary\n");

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
        nativeDevice_list = createDeviceList(env, device_list, num_devices);
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
        nativeBinaries = new unsigned char*[binariesLength];
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
                unsigned char *nativeBinary = new unsigned char[binaryLength];
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

    nativeProgram = clCreateProgramWithBinary(nativeContext, nativeNum_devices, nativeDevice_list, nativeLengths, (const unsigned char**)nativeBinaries, &nativeBinary_status, &nativeErrcode_ret);

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




/*
 * Class:     org_jocl_CL
 * Method:    clRetainProgramNative
 * Signature: (Lorg/jocl/cl_program;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainProgramNative
  (JNIEnv *env, jclass cls, jobject program)
{
    Logger::log(LOG_TRACE, "Executing clRetainProgram\n");

    cl_program nativeProgram = NULL;
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    return clRetainProgram(nativeProgram);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseProgramNative
 * Signature: (Lorg/jocl/cl_program;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseProgramNative
  (JNIEnv *env, jclass cls, jobject program)
{
    Logger::log(LOG_TRACE, "Executing clReleaseProgram\n");

    cl_program nativeProgram = NULL;
    if (program != NULL)
    {
        nativeProgram = (cl_program)env->GetLongField(program, NativePointerObject_nativePointer);
    }
    return clReleaseProgram(nativeProgram);
}




/*
 * Class:     org_jocl_CL
 * Method:    clBuildProgramNative
 * Signature: (Lorg/jocl/cl_program;I[Lorg/jocl/cl_device_id;Ljava/lang/String;Lorg/jocl/BuildProgramFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clBuildProgramNative
  (JNIEnv *env, jclass cls, jobject program, jint num_devices, jobjectArray device_list, jstring options, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clBuildProgram\n");

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
        nativeDevice_list = createDeviceList(env, device_list, num_devices);
        if (nativeDevice_list == NULL)
        {
            return NULL;
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
    nativePfn_notify = &BuildProgramFunction;
    CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
    if (callbackInfo == NULL)
    {
        return NULL;
    }
    nativeUser_data = (void*)callbackInfo;

    int result = clBuildProgram(nativeProgram, nativeNum_devices, nativeDevice_list, nativeOptions, nativePfn_notify, nativeUser_data);

    // Write back native variable values and clean up
    delete[] nativeDevice_list;
    delete[] nativeOptions;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clUnloadCompilerNative
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clUnloadCompilerNative
  (JNIEnv *env, jclass cls)
{
    Logger::log(LOG_TRACE, "Executing clUnloadCompiler\n");

    // Somehow I like this method, but I don't know why...
    return clUnloadCompiler();
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetProgramInfoNative
 * Signature: (Lorg/jocl/cl_program;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetProgramInfoNative
  (JNIEnv *env, jclass cls, jobject program, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetProgramInfo\n");

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

    int result = clGetProgramInfo(nativeProgram, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject program, jobject device, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetProgramBuildInfo\n");

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

    int result = clGetProgramBuildInfo(nativeProgram, nativeDevice, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject program, jstring kernel_name, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateKernel\n");

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


    nativeKernel = clCreateKernel(nativeProgram, nativeKernel_name, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject program, jint num_kernels, jobjectArray kernels, jintArray num_kernels_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateKernelsInProgram\n");

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
            return NULL;
        }
    }

    int result = clCreateKernelsInProgram(nativeProgram, nativeNum_kernels, nativeKernels, &nativeNum_kernels_ret);

    // Write back native variable values and clean up
    if (kernels != NULL)
    {
        for (unsigned int i=0; i<nativeNum_kernels_ret; i++)
        {
            jobject kernel = env->GetObjectArrayElement(kernels, i);
            if (kernel == NULL)
            {
                kernel = env->NewObject(cl_kernel_Class, cl_kernel_Constructor);
                if (env->ExceptionCheck())
                {
                    return CL_OUT_OF_HOST_MEMORY;
                }
                env->SetObjectArrayElement(kernels, i, kernel);
                if (env->ExceptionCheck())
                {
                    return CL_INVALID_HOST_PTR;
                }
            }
            setNativePointer(env, kernel, (jlong)nativeKernels[i]);
        }
        delete[] nativeKernels;
    }
    if (!set(env, num_kernels_ret, 0, nativeNum_kernels_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clRetainKernelNative
 * Signature: (Lorg/jocl/cl_kernel;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainKernelNative
  (JNIEnv *env, jclass cls, jobject kernel)
{
    Logger::log(LOG_TRACE, "Executing clRetainKernel\n");

    cl_kernel nativeKernel = NULL;
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    return clRetainKernel(nativeKernel);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseKernelNative
 * Signature: (Lorg/jocl/cl_kernel;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseKernelNative
  (JNIEnv *env, jclass cls, jobject kernel)
{
    Logger::log(LOG_TRACE, "Executing clReleaseKernel\n");

    cl_kernel nativeKernel = NULL;
    if (kernel != NULL)
    {
        nativeKernel = (cl_kernel)env->GetLongField(kernel, NativePointerObject_nativePointer);
    }
    return clReleaseKernel(nativeKernel);
}




/*
 * Class:     org_jocl_CL
 * Method:    clSetKernelArgNative
 * Signature: (Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetKernelArgNative
  (JNIEnv *env, jclass cls, jobject kernel, jint arg_index, jlong arg_size, jobject arg_value)
{
    Logger::log(LOG_TRACE, "Executing clSetKernelArg\n");

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

    int result = clSetKernelArg(nativeKernel, nativeArg_index, nativeArg_size, nativeArg_value);

    // Write back native variable values and clean up
    if (!releasePointerData(env, arg_valuePointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetKernelInfoNative
 * Signature: (Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetKernelInfoNative
  (JNIEnv *env, jclass cls, jobject kernel, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetKernelInfo\n");

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

    int result = clGetKernelInfo(nativeKernel, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetKernelWorkGroupInfoNative
 * Signature: (Lorg/jocl/cl_kernel;Lorg/jocl/cl_device_id;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetKernelWorkGroupInfoNative
  (JNIEnv *env, jclass cls, jobject kernel, jobject device, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetKernelWorkGroupInfo\n");

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

    int result = clGetKernelWorkGroupInfo(nativeKernel, nativeDevice, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jint num_events, jobjectArray event_list)
{
    Logger::log(LOG_TRACE, "Executing clWaitForEvents\n");

    // Native variables declaration
    cl_uint nativeNum_events = 0;
    cl_event *nativeEvent_list = NULL;

    // Obtain native variable values
    nativeNum_events = (cl_uint)num_events;
    if (event_list != NULL)
    {
        nativeEvent_list = createEventList(env, event_list, num_events);
        if (nativeEvent_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = result = clWaitForEvents(nativeNum_events, nativeEvent_list);

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
  (JNIEnv *env, jclass cls, jobject event, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetEventInfo\n");

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

    int result = clGetEventInfo(nativeEvent, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}



#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clCreateUserEventNative
 * Signature: (Lorg/jocl/cl_context;[I)Lorg/jocl/cl_event;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateUserEventNative
  (JNIEnv *env, jclass cls, jobject context, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateUserEvent\n");

    // Native variables declaration
    cl_context nativeContext = NULL;
    cl_int nativeErrcode_ret = 0;

    // Obtain native variable values
    if (context != NULL)
    {
        nativeContext = (cl_context)env->GetLongField(context, NativePointerObject_nativePointer);
    }

    cl_event nativeEvent = clCreateUserEvent(nativeContext, &nativeErrcode_ret);

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

#endif // defined(CL_VERSION_1_1)




/*
 * Class:     org_jocl_CL
 * Method:    clRetainEventNative
 * Signature: (Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clRetainEventNative
  (JNIEnv *env, jclass cls, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clRetainEvent\n");

    cl_event nativeEvent = NULL;
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    return clRetainEvent(nativeEvent);
}




/*
 * Class:     org_jocl_CL
 * Method:    clReleaseEventNative
 * Signature: (Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clReleaseEventNative
  (JNIEnv *env, jclass cls, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clReleaseEvent\n");

    cl_event nativeEvent = NULL;
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    return clReleaseEvent(nativeEvent);
}


#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clSetUserEventStatusNative
 * Signature: (Lorg/jocl/cl_event;I)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetUserEventStatusNative
  (JNIEnv *env, jclass cls, jobject event, jint execution_status)
{
    Logger::log(LOG_TRACE, "Executing clSetUserEventStatus\n");

    // Native variables declaration
    cl_event nativeEvent = NULL;
    cl_int nativeExecution_status = 0;

    // Obtain native variable values
    if (event != NULL)
    {
        nativeEvent = (cl_event)env->GetLongField(event, NativePointerObject_nativePointer);
    }
    nativeExecution_status = (cl_int)execution_status;

    return clSetUserEventStatus(nativeEvent, nativeExecution_status);
}

#endif // defined(CL_VERSION_1_1)



#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clSetEventCallbackNative
 * Signature: (Lorg/jocl/cl_event;ILorg/jocl/EventCallbackFunction;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clSetEventCallbackNative
  (JNIEnv *env, jclass cls, jobject event, jint command_exec_callback_type, jobject pfn_notify, jobject user_data)
{
    Logger::log(LOG_TRACE, "Executing clSetEventCallback\n");

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
    nativePfn_notify = &EventCallback;
    CallbackInfo *callbackInfo = initCallbackInfo(env, pfn_notify, user_data);
    if (callbackInfo == NULL)
    {
        return NULL;
    }
    nativeUser_data = (void*)callbackInfo;

    int result = clSetEventCallback(nativeEvent, nativeCommand_exec_callback_type, nativePfn_notify, nativeUser_data);

    return result;
}

#endif // defined(CL_VERSION_1_1)







/*
 * Class:     org_jocl_CL
 * Method:    clGetEventProfilingInfoNative
 * Signature: (Lorg/jocl/cl_event;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetEventProfilingInfoNative
  (JNIEnv *env, jclass cls, jobject event, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetEventProfilingInfo\n");

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

    int result = clGetEventProfilingInfo(nativeEvent, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clFlush\n");

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return clFlush(nativeCommand_queue);
}




/*
 * Class:     org_jocl_CL
 * Method:    clFinishNative
 * Signature: (Lorg/jocl/cl_command_queue;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clFinishNative
  (JNIEnv *env, jclass cls, jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clFinish\n");

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return clFinish(nativeCommand_queue);
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
  (JNIEnv *env, jclass cls, jobject command_queue, jobject buffer, jboolean blocking_read, jlong offset, jlong cb, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReadBuffer\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = clEnqueueReadBuffer(nativeCommand_queue, nativeBuffer, nativeBlocking_read, nativeOffset, nativeCb, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    /* // XXX NON_BLOCKING_READ
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
// XXX NON_BLOCKING_READ
JNIEXPORT void JNICALL Java_org_jocl_CL_releasePendingPointerDataNative
  (JNIEnv *env, jclass cls, jobject event)
{
/*
    Logger::log(LOG_ERROR, "Executing releasePendingPointerData\n");

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





#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueReadBufferRectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueReadBufferRectNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject buffer, jboolean blocking_read, jlongArray buffer_offset, jlongArray host_offset, jlongArray region, jlong buffer_row_pitch, jlong buffer_slice_pitch, jlong host_row_pitch, jlong host_slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReadBufferRect\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = clEnqueueReadBufferRect(nativeCommand_queue, nativeBuffer, nativeBlocking_read, nativeBuffer_offset, nativeHost_offset, nativeRegion, nativeBuffer_row_pitch, nativeBuffer_slice_pitch, nativeHost_row_pitch, nativeHost_slice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    /* // XXX NON_BLOCKING_READ
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

#endif // defined(CL_VERSION_1_1)













/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueWriteBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueWriteBufferNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject buffer, jboolean blocking_write, jlong offset, jlong cb, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWriteBuffer\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = clEnqueueWriteBuffer(nativeCommand_queue, nativeBuffer, nativeBlocking_write, nativeOffset, nativeCb, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    if (!releasePointerData(env, ptrPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}









#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueWriteBufferRectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueWriteBufferRectNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject buffer, jboolean blocking_write, jlongArray buffer_offset, jlongArray host_offset, jlongArray region, jlong buffer_row_pitch, jlong buffer_slice_pitch, jlong host_row_pitch, jlong host_slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWriteBufferRect\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = clEnqueueWriteBufferRect(nativeCommand_queue, nativeBuffer, nativeBlocking_write, nativeBuffer_offset, nativeHost_offset, nativeRegion, nativeBuffer_row_pitch, nativeBuffer_slice_pitch, nativeHost_row_pitch, nativeHost_slice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    delete[] nativeBuffer_offset;
    delete[] nativeHost_offset;
    delete[] nativeRegion;
    if (!releasePointerData(env, ptrPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}

#endif // defined(CL_VERSION_1_1)






/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyBufferNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;JJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyBufferNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject src_buffer, jobject dst_buffer, jlong src_offset, jlong dst_offset, jlong cb, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyBuffer\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }



    int result = clEnqueueCopyBuffer(nativeCommand_queue, nativeSrc_buffer, nativeDst_buffer, nativeSrc_offset, nativeDst_offset, nativeCb, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}


#if defined(CL_VERSION_1_1)

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyBufferRectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[J[JJJJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyBufferRectNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject src_buffer, jobject dst_buffer, jlongArray src_origin, jlongArray dst_origin, jlongArray region, jlong src_row_pitch, jlong src_slice_pitch, jlong dst_row_pitch, jlong dst_slice_pitch, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyBufferRect\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = clEnqueueCopyBufferRect(nativeCommand_queue, nativeSrc_buffer, nativeDst_buffer, nativeSrc_origin, nativeDst_origin, nativeRegion, nativeSrc_row_pitch, nativeSrc_slice_pitch, nativeDst_row_pitch, nativeDst_slice_pitch, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    delete[] nativeSrc_origin;
    delete[] nativeDst_origin;
    delete[] nativeRegion;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;

}

#endif // defined(CL_VERSION_1_1)











/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueReadImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[JJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueReadImageNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject image, jboolean blocking_read, jlongArray origin, jlongArray region, jlong row_pitch, jlong slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReadImage\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = clEnqueueReadImage(nativeCommand_queue, nativeImage, nativeBlocking_read, nativeOrigin, nativeRegion, nativeRow_pitch, nativeSlice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // XXX NON_BLOCKING_READ (see clEnqueueReadBuffer!)

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
  (JNIEnv *env, jclass cls, jobject command_queue, jobject image, jboolean blocking_write, jlongArray origin, jlongArray region, jlong input_row_pitch, jlong input_slice_pitch, jobject ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWriteImage\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = clEnqueueWriteImage(nativeCommand_queue, nativeImage, nativeBlocking_write, nativeOrigin, nativeRegion, nativeInput_row_pitch, nativeInput_slice_pitch, nativePtr, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    delete[] nativeOrigin;
    delete[] nativeRegion;
    if (!releasePointerData(env, ptrPointerData, JNI_ABORT)) return CL_INVALID_HOST_PTR;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueCopyImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueCopyImageNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject src_image, jobject dst_image, jlongArray src_origin, jlongArray dst_origin, jlongArray region, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyImage\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }



    int result = clEnqueueCopyImage(nativeCommand_queue, nativeSrc_image, nativeDst_image, nativeSrc_origin, nativeDst_origin, nativeRegion, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jobject src_image, jobject dst_buffer, jlongArray src_origin, jlongArray region, jlong dst_offset, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyImageToBuffer\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }



    int result = clEnqueueCopyImageToBuffer(nativeCommand_queue, nativeSrc_image, nativeDst_buffer, nativeSrc_origin, nativeRegion, nativeDst_offset, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jobject src_buffer, jobject dst_image, jlong src_offset, jlongArray dst_origin, jlongArray region, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueCopyBufferToImage\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }



    int result = clEnqueueCopyBufferToImage(nativeCommand_queue, nativeSrc_buffer, nativeDst_image, nativeSrc_offset, nativeDst_origin, nativeRegion, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jobject buffer, jboolean blocking_map, jlong map_flags, jlong offset, jlong cb, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMapBuffer\n");

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeBuffer = NULL;
    cl_bool nativeBlocking_map = CL_TRUE;
    cl_map_flags nativeMap_flags = 0;
    size_t nativeOffset = 0;
    size_t nativeCb = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;
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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return NULL;
        }
    }

    nativeHostPointer = clEnqueueMapBuffer(nativeCommand_queue, nativeBuffer, nativeBlocking_map, nativeMap_flags, nativeOffset, nativeCb, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    // Create and return a ByteBuffer for the mapped memory
    if (nativeHostPointer == NULL)
    {
        return NULL;
    }
    return env->NewDirectByteBuffer(nativeHostPointer, nativeCb);
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueMapImageNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJ[J[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;[I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clEnqueueMapImageNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject image, jboolean blocking_map, jlong map_flags, jlongArray origin, jlongArray region, jlongArray image_row_pitch, jlongArray image_slice_pitch, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMapImage\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return NULL;
        }
    }

    nativeHostPointer = clEnqueueMapImage(nativeCommand_queue, nativeImage, nativeBlocking_map, nativeMap_flags, nativeOrigin, nativeRegion, &nativeImage_row_pitch, &nativeImage_slice_pitch, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent, &nativeErrcode_ret);

    // Write back native variable values and clean up
    delete[] nativeOrigin;
    delete[] nativeRegion;
    if (!set(env, image_row_pitch, 0, nativeImage_row_pitch)) return NULL;
    if (!set(env, image_slice_pitch, 0, nativeImage_slice_pitch)) return NULL;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);
    if (!set(env, errcode_ret, 0, nativeErrcode_ret)) return NULL;

    // Create and return a ByteBuffer for the mapped memory
    jlong size = 0;
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
    return env->NewDirectByteBuffer(nativeHostPointer, size);
}

/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueUnmapMemObjectNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Ljava/nio/ByteBuffer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueUnmapMemObjectNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject memobj, jobject mapped_ptr, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueUnmapMemObject\n");

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_mem nativeMemobj = NULL;
    void *nativeMapped_ptr = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = clEnqueueUnmapMemObject(nativeCommand_queue, nativeMemobj, nativeMapped_ptr, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueNDRangeKernelNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_kernel;I[J[J[JI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueNDRangeKernelNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject kernel, jint work_dim, jlongArray global_work_offset, jlongArray global_work_size, jlongArray local_work_size, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueNDRangeKernel\n");

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }

    int result = clEnqueueNDRangeKernel(nativeCommand_queue, nativeKernel, nativeWork_dim, nativeGlobal_work_offset, nativeGlobal_work_size, nativeLocal_work_size, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jobject kernel, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueTask\n");

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_kernel nativeKernel = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;

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
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = clEnqueueTask(nativeCommand_queue, nativeKernel, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jobject user_func, jobject args, jlong cb_args, jint num_mem_objects, jobjectArray mem_list, jobjectArray args_mem_loc, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueNativeKernel\n");

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


    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeUser_func = &EnqueueNativeKernelFunction;
    CallbackInfo *callbackInfo = initCallbackInfo(env, user_func, args);
    if (callbackInfo == NULL)
    {
        return NULL;
    }
    nativeArgs = (void*)callbackInfo;
    nativeCb_args = (size_t)cb_args;
    nativeNum_mem_objects = (cl_uint)num_mem_objects;
    if (mem_list != NULL)
    {
        nativeMem_list = createMemList(env, mem_list, num_mem_objects);
        if (nativeMem_list == NULL)
        {
            return NULL;
        }
    }
    if (args_mem_loc != NULL)
    {
        jsize args_mem_locLength = env->GetArrayLength(args_mem_loc);
        nativeArgs_mem_loc = new void*[args_mem_locLength];
        if (nativeArgs_mem_loc == NULL)
        {
            ThrowByName(env, "java/lang/OutOfMemoryError",
                "Out of memory during args mem loc array creation");
            return CL_OUT_OF_HOST_MEMORY;
        }
        for (int i=0; i<args_mem_locLength; i++)
        {
            jobject mem_loc = env->GetObjectArrayElement(args_mem_loc, i);
            if (mem_loc != NULL)
            {
                nativeArgs_mem_loc[i] = (void*)env->GetLongField(mem_loc, NativePointerObject_nativePointer);
            }
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    // TODO: The call currently has to be blocking,
    // to prevent the nativeArgs from being deleted
    int result = clEnqueueNativeKernel(nativeCommand_queue, nativeUser_func, nativeArgs, nativeCb_args, nativeNum_mem_objects, nativeMem_list, (const void**)nativeArgs_mem_loc, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // TODO: Have to block in the current implementation
    clWaitForEvents(1, &nativeEvent);

    // Write back native variable values and clean up
    delete[] nativeMem_list;
    delete[] nativeArgs_mem_loc;
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clEnqueueMarkerNative
 * Signature: (Lorg/jocl/cl_command_queue;Lorg/jocl/cl_event;)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clEnqueueMarkerNative
  (JNIEnv *env, jclass cls, jobject command_queue, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueMarker\n");

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_event nativeEvent = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }

    int result = clEnqueueMarker(nativeCommand_queue, &nativeEvent);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jint num_events, jobjectArray event_list)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueWaitForEvents\n");

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
        nativeEvent_list = createEventList(env, event_list, num_events);
        if (nativeEvent_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = clEnqueueWaitForEvents(nativeCommand_queue, nativeNum_events, nativeEvent_list);

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
  (JNIEnv *env, jclass cls, jobject command_queue)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueBarrier\n");

    cl_command_queue nativeCommand_queue = NULL;
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    return clEnqueueBarrier(nativeCommand_queue);
}



//=== GL functions ===========================================================




/*
 * Class:     org_jocl_CL
 * Method:    clCreateFromGLBufferNative
 * Signature: (Lorg/jocl/cl_context;JI[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateFromGLBufferNative
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jint bufobj, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLBuffer\n");

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

    cl_mem nativeMem = clCreateFromGLBuffer(nativeContext, nativeFlags, nativeBufobj, &nativeErrcode_ret);

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
 * Method:    clCreateFromGLTexture2DNative
 * Signature: (Lorg/jocl/cl_context;JIII[I)Lorg/jocl/cl_mem;
 */
JNIEXPORT jobject JNICALL Java_org_jocl_CL_clCreateFromGLTexture2DNative
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jint texture_target, jint miplevel, jint texture, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLTexture2D\n");

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

    cl_mem nativeMem = clCreateFromGLTexture2D(nativeContext, nativeFlags, nativeTexture_target, nativeMiplevel, nativeTexture, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jint texture_target, jint miplevel, jint texture, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLTexture3D\n");

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

    cl_mem nativeMem = clCreateFromGLTexture3D(nativeContext, nativeFlags, nativeTexture_target, nativeMiplevel, nativeTexture, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject context, jlong flags, jint renderbuffer, jintArray errcode_ret)
{
    Logger::log(LOG_TRACE, "Executing clCreateFromGLRenderbuffer\n");

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

    cl_mem nativeMem = clCreateFromGLRenderbuffer(nativeContext, nativeFlags, nativeRenderbuffer, &nativeErrcode_ret);

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
  (JNIEnv *env, jclass cls, jobject memobj, jintArray gl_object_type, jintArray gl_object_name)
{
    Logger::log(LOG_TRACE, "Executing clGetGLObjectInfo\n");

    // Native variables declaration
    cl_mem nativeMemobj = NULL;
    cl_gl_object_type nativeGl_object_type = 0;
    GLuint nativeGl_object_name = 0;

    // Obtain native variable values
    if (memobj != NULL)
    {
        nativeMemobj = (cl_mem)env->GetLongField(memobj, NativePointerObject_nativePointer);
    }

    int result = clGetGLObjectInfo(nativeMemobj, &nativeGl_object_type, &nativeGl_object_name);

    // Write back native variable values and clean up
    if (!set(env, gl_object_type, 0, (jint)nativeGl_object_type)) return CL_OUT_OF_HOST_MEMORY;
    if (!set(env, gl_object_name, 0, nativeGl_object_name)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}

/*
 * Class:     org_jocl_CL
 * Method:    clGetGLTextureInfoNative
 * Signature: (Lorg/jocl/cl_mem;IJLorg/jocl/Pointer;[J)I
 */
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetGLTextureInfoNative
  (JNIEnv *env, jclass cls, jobject memobj, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetGLTextureInfo\n");

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

    int result = clGetGLTextureInfo(nativeMemobj, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

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
  (JNIEnv *env, jclass cls, jobject command_queue, jint num_objects, jobjectArray mem_objects, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueAcquireGLObjects\n");

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_objects = 0;
    cl_mem *nativeMem_objects = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_objects = (cl_uint)num_objects;
    if (mem_objects != NULL)
    {
        nativeMem_objects = createMemList(env, mem_objects, num_objects);
        if (nativeMem_objects == NULL)
        {
            return NULL;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = clEnqueueAcquireGLObjects(nativeCommand_queue, nativeNum_objects, nativeMem_objects, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
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
  (JNIEnv *env, jclass cls, jobject command_queue, jint num_objects, jobjectArray mem_objects, jint num_events_in_wait_list, jobjectArray event_wait_list, jobject event)
{
    Logger::log(LOG_TRACE, "Executing clEnqueueReleaseGLObjects\n");

    // Native variables declaration
    cl_command_queue nativeCommand_queue = NULL;
    cl_uint nativeNum_objects = 0;
    cl_mem *nativeMem_objects = NULL;
    cl_uint nativeNum_events_in_wait_list = 0;
    cl_event *nativeEvent_wait_list = NULL;
    cl_event nativeEvent = NULL;

    // Obtain native variable values
    if (command_queue != NULL)
    {
        nativeCommand_queue = (cl_command_queue)env->GetLongField(command_queue, NativePointerObject_nativePointer);
    }
    nativeNum_objects = (cl_uint)num_objects;
    if (mem_objects != NULL)
    {
        nativeMem_objects = createMemList(env, mem_objects, num_objects);
        if (nativeMem_objects == NULL)
        {
            return NULL;
        }
    }
    nativeNum_events_in_wait_list = (cl_uint)num_events_in_wait_list;
    if (event_wait_list != NULL)
    {
        nativeEvent_wait_list = createEventList(env, event_wait_list, num_events_in_wait_list);
        if (nativeEvent_wait_list == NULL)
        {
            return CL_OUT_OF_HOST_MEMORY;
        }
    }


    int result = clEnqueueReleaseGLObjects(nativeCommand_queue, nativeNum_objects, nativeMem_objects, nativeNum_events_in_wait_list, nativeEvent_wait_list, &nativeEvent);

    // Write back native variable values and clean up
    delete[] nativeEvent_wait_list;
    setNativePointer(env, event, (jlong)nativeEvent);

    return result;
}




/*
 * Class:     org_jocl_CL
 * Method:    clGetGLContextInfoKHRNative
 * Signature: (Lorg/jocl/cl_context_properties;IJLorg/jocl/Pointer;[J)I
 */
/*
JNIEXPORT jint JNICALL Java_org_jocl_CL_clGetGLContextInfoKHRNative
  (JNIEnv *env, jclass cls, jobject properties, jint param_name, jlong param_value_size, jobject param_value, jlongArray param_value_size_ret)
{
    Logger::log(LOG_TRACE, "Executing clGetGLContextInfoKHR\n");

    // Native variables declaration
    cl_context_properties *nativeProperties = NULL;
    cl_context_info nativeParam_name = NULL;
    size_t nativeParam_value_size = 0;
    void *nativeParam_value = NULL;
    size_t nativeParam_value_size_ret = 0;

    // Obtain native variable values
    if (properties != NULL)
    {
        nativeProperties = createPropertiesArray(env, properties);
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

    int result = clGetGLContextInfoKHR(nativeProperties, nativeParam_name, nativeParam_value_size, nativeParam_value, &nativeParam_value_size_ret);

    // Write back native variable values and clean up
    delete[] nativeProperties;
    if (!releasePointerData(env, param_valuePointerData)) return CL_INVALID_HOST_PTR;
    if (!set(env, param_value_size_ret, 0, (long)nativeParam_value_size_ret)) return CL_OUT_OF_HOST_MEMORY;

    return result;
}
*/



/*
 * XXX GL_INTEROPERABILITY - This function is ONLY for testing!
 */
/*
 * Class:     org_jocl_CL
 * Method:    createGLSharedContextPropertiesArrayNative
 * Signature: ()[J
 */
/*
JNIEXPORT jlongArray JNICALL Java_org_jocl_CL_createGLSharedContextPropertiesArrayNative
  (JNIEnv *env, jclass cls)
{
    #if defined (__APPLE__)
        CGLContextObj cglContext = CGLGetCurrentContext();
        CGLShareGroupObj cglShareGroup = CGLGetShareGroup(cglContext);
        cl_context_properties nativeProperties[] =
        {
            CL_CONTEXT_PROPERTY_USE_CGL_SHAREGROUP_APPLE, (cl_context_properties)cglShareGroup,
            0, 0,
            0
        };
    #else
        #ifdef UNIX
            cl_context_properties nativeProperties[] =
            {
                CL_GL_CONTEXT_KHR, (cl_context_properties)glXGetCurrentContext(),
                CL_GLX_DISPLAY_KHR, (cl_context_properties)glXGetCurrentDisplay(),
                0
            };
        #else // Win32
            cl_context_properties nativeProperties[] =
            {
                CL_GL_CONTEXT_KHR, (cl_context_properties)wglGetCurrentContext(),
                CL_WGL_HDC_KHR, (cl_context_properties)wglGetCurrentDC(),
                0
            };
        #endif
    #endif

    jlongArray array = env->NewLongArray(5);
    jlong *a = (jlong*)env->GetPrimitiveArrayCritical(array, NULL);
    if (env->ExceptionCheck())
    {
        return false;
    }
    for (int i=0; i<5; i++)
    {
        a[i] = (jlong)nativeProperties[i];
    }
    env->ReleasePrimitiveArrayCritical(array, a, 0);
    return array;
}
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


#if defined(OPENCL_1_1)
    nativeMethod.name = "clCreateSubBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateSubBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;IILorg/jocl/Pointer;[I)Lorg/jocl/cl_mem;";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif


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


#if defined(OPENCL_1_1)
    nativeMethod.name = "clSetMemObjectDestructorCallbackNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetMemObjectDestructorCallbackNative;
    nativeMethod.signature = "(Lorg/jocl/cl_mem;Lorg/jocl/MemObjectDestructorCallbackFunction;Ljava/lang/Object;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

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


    nativeMethod.name = "clGetKernelInfoNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clGetKernelInfoNative;
    nativeMethod.signature = "(Lorg/jocl/cl_kernel;IJLorg/jocl/Pointer;[J)I";
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


#if defined (OPENCL_1_1)
    nativeMethod.name = "clCreateUserEventNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clCreateUserEventNative;
    nativeMethod.signature = "(Lorg/jocl/cl_context;[I)Lorg/jocl/cl_event;";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

    nativeMethod.name = "clRetainEventNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clRetainEventNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);


    nativeMethod.name = "clReleaseEventNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clReleaseEventNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);


#if defined (OPENCL_1_1)
    nativeMethod.name = "clSetUserEventStatusNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetUserEventStatusNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;I)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

#if defined (OPENCL_1_1)
    nativeMethod.name = "clSetEventCallbackNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clSetEventCallbackNative;
    nativeMethod.signature = "(Lorg/jocl/cl_event;ILorg/jocl/EventCallbackFunction;Ljava/lang/Object;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

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


#if defined (OPENCL_1_1)
    nativeMethod.name = "clEnqueueReadBufferRectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueReadBufferRectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

    nativeMethod.name = "clEnqueueWriteBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueWriteBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;ZJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);


#if defined (OPENCL_1_1)
    nativeMethod.name = "clEnqueueWriteBufferRectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueWriteBufferRectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[J[JJJJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

    nativeMethod.name = "clEnqueueCopyBufferNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueCopyBufferNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;JJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);


#if defined (OPENCL_1_1)
    nativeMethod.name = "clEnqueueCopyBufferRectNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueCopyBufferRectNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Lorg/jocl/cl_mem;[J[J[JJJJJI[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);
#endif

    nativeMethod.name = "clEnqueueReadImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueReadImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[JJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
    env->RegisterNatives(cls, &nativeMethod, 1);


    nativeMethod.name = "clEnqueueWriteImageNative";
    nativeMethod.fnPtr = (void*)Java_org_jocl_CL_clEnqueueWriteImageNative;
    nativeMethod.signature = "(Lorg/jocl/cl_command_queue;Lorg/jocl/cl_mem;Z[J[JJJLorg/jocl/Pointer;I[Lorg/jocl/cl_event;Lorg/jocl/cl_event;)I";
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

}


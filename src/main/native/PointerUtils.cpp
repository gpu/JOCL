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

#include "PointerUtils.hpp"
#include "JNIUtils.hpp"
#include "Logger.hpp"


// Class and method ID for org.jocl.Pointer and its constructor
jclass Pointer_Class;
jmethodID Pointer_Constructor;

// Field ID for the org.jocl.NativePointerObject fields
jfieldID NativePointerObject_nativePointer; // long
jfieldID NativePointerObject_buffer; // Ljava.nio.Buffer;
jfieldID NativePointerObject_pointers; // [org.jocl.NativePointerObject;
jfieldID NativePointerObject_byteOffset; // long



/**
 * Initialize the field- and method IDs for the PointerUtils
 */
int initPointerUtils(JNIEnv *env)
{
    jclass cls = NULL;

    // Obtain the fieldIDs of the NativePointerObject class
    if (!init(env, cls, "org/jocl/NativePointerObject")) return JNI_ERR;
    if (!init(env, cls, NativePointerObject_nativePointer, "nativePointer", "J")) return JNI_ERR;

    if (!init(env, "org/jocl/Pointer", Pointer_Class, Pointer_Constructor)) return JNI_ERR;

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

    return JNI_VERSION_1_4;
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

    if (pointerData->startPointer != (jlong)NULL)
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

            if (isCopy==JNI_TRUE)
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
bool releasePointerData(JNIEnv *env, PointerData* &pointerData, jint mode)
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

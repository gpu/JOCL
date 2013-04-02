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

#include <jni.h>

#include "JNIUtils.hpp"
#include "Logger.hpp"

// Method ID for the java.lang.Object class
jmethodID Object_getClass; // ()Ljava/lang/Class;

// Method IDs for the java.lang.Class class
jmethodID Class_getComponentType; // ()Ljava/lang/Class;
jmethodID Class_newInstance; // ()Ljava/lang/Object;

// Method ID for java.lang.String#getBytes
jmethodID String_getBytes; // ()[B

// Method IDs for the java.nio.Buffer class
jmethodID Buffer_isDirect; // ()Z
jmethodID Buffer_hasArray; // ()Z
jmethodID Buffer_array; // ()Ljava/lang/Object;

/**
 * Initialize the method IDs for the JNIUtils
 */
int initJNIUtils(JNIEnv *env)
{
    jclass cls = NULL;

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

    // Obtain the methodIDs for Buffer: isDirect, hasArray and array
    if (!init(env, cls, "java/nio/Buffer")) return JNI_ERR;
    if (!init(env, cls, Buffer_isDirect, "isDirect", "()Z"                 )) return JNI_ERR;
    if (!init(env, cls, Buffer_hasArray, "hasArray", "()Z"                 )) return JNI_ERR;
    if (!init(env, cls, Buffer_array,    "array",    "()Ljava/lang/Object;")) return JNI_ERR;

    return JNI_VERSION_1_4;
}


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
char *convertString(JNIEnv *env, jstring js, int *length)
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

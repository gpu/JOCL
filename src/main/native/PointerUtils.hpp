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

#ifndef POINTER_UTILS_HPP
#define POINTER_UTILS_HPP

#include <jni.h>

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

// Class and method ID for org.jocl.Pointer and its constructor
extern jclass Pointer_Class;
extern jmethodID Pointer_Constructor;

// Field ID for the org.jocl.NativePointerObject fields
extern jfieldID NativePointerObject_nativePointer; // long
extern jfieldID NativePointerObject_buffer; // Ljava.nio.Buffer;
extern jfieldID NativePointerObject_pointers; // [org.jocl.NativePointerObject;
extern jfieldID NativePointerObject_byteOffset; // long

int initPointerUtils(JNIEnv *env);

PointerData* initPointerData(JNIEnv *env, jobject pointerObject);
bool createPointerObject(JNIEnv *env, jobjectArray pointersArray, int index, void *pointer);
bool releasePointerData(JNIEnv *env, PointerData* &pointerData, jint mode=0);

void setNativePointer(JNIEnv *env, jobject pointerObject, jlong pointer);

#endif // POINTER_UTILS_HPP
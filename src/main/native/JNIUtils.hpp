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

#ifndef JNI_UTILS_HPP
#define JNI_UTILS_HPP

// Method ID for the java.lang.Object class
extern jmethodID Object_getClass; // ()Ljava/lang/Class;

// Method IDs for the java.lang.Class class
extern jmethodID Class_getComponentType; // ()Ljava/lang/Class;
extern jmethodID Class_newInstance; // ()Ljava/lang/Object;

// Method ID for java.lang.String#getBytes
extern jmethodID String_getBytes; // ()[B

// Method IDs for the java.nio.Buffer class
extern jmethodID Buffer_isDirect; // ()Z
extern jmethodID Buffer_hasArray; // ()Z
extern jmethodID Buffer_array; // ()Ljava/lang/Object;



int initJNIUtils(JNIEnv *env);

bool init(JNIEnv *env, jclass cls, jfieldID& field, const char *name, const char *signature);
bool init(JNIEnv *env, jclass cls, jmethodID& method, const char *name, const char *signature);
bool init(JNIEnv *env, jclass& cls, const char *name);
bool init(JNIEnv *env, const char *className, jclass &globalCls, jmethodID &constructor);

void ThrowByName(JNIEnv *env, const char *name, const char *msg);

bool set(JNIEnv *env, jintArray ja, int index, long value);
bool set(JNIEnv *env, jlongArray ja, int index, long value);
bool set(JNIEnv *env, jfloatArray ja, int index, float value);
char *convertString(JNIEnv *env, jstring js, int *length=NULL);
size_t* convertArray(JNIEnv *env, jlongArray array);

#endif // JNI_UTILS_HPP
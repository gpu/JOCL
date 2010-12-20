/*
 * JOCL - Java bindings for OpenCL - http://www.jocl.org
 * 
 * DISCLAIMER: THIS SOFTWARE IS PROVIDED WITHOUT WARRANTY OF ANY KIND
 * If you find any bugs or errors, report them at http://www.jocl.org 
 * 
 * LICENSE: THIS SOFTWARE IS FREE FOR NON-COMMERCIAL USE ONLY
 * For non-commercial applications, you may use this software without
 * any restrictions. If you wish to use it for commercial purposes, 
 * contact me at http://www.jocl.org 
 */

package org.jocl;

import java.nio.Buffer;
import java.util.Arrays;

/**
 * Base class for all classes that store a native pointer
 */
public class NativePointerObject
{
    /**
     * The native pointer, written by native methods
     */
    private long nativePointer;

    
    // These fields are only for the use in the Pointer class, 
    // but stored here to treat NativePointerObjects and 
    // Pointers equally on JNI side:
    
    /**
     * The offset from the nativePointer, in bytes
     */
    private long byteOffset;
    
    /**
     * The buffer the pointer points to
     */
    private Buffer buffer;

    /**
     * The array of pointers this pointer points to
     */
    private NativePointerObject pointers[]; 
    
    
    /**
     * Creates a new (null) Pointer
     */
    NativePointerObject()
    {
        buffer = null;
        pointers = null;
        byteOffset = 0;
    }

    /**
     * Creates a Pointer to the given Buffer
     * 
     * @param buffer The buffer to point to
     */
    NativePointerObject(Buffer buffer)
    {
        this.buffer = buffer;
        pointers = null;
        byteOffset = 0;
    }

    /**
     * Creates a Pointer to the given array of pointers
     * 
     * @param pointers The array the pointer points to
     */
    NativePointerObject(NativePointerObject pointers[])
    {
        buffer = null;
        this.pointers = pointers;
        byteOffset = 0;
    }
    
    /**
     * Copy constructor
     * 
     * @param other The other Pointer
     */
    NativePointerObject(NativePointerObject other)
    {
        this.buffer = other.buffer;
        this.pointers = other.pointers;
        this.byteOffset = other.byteOffset;
    }

    /**
     * Creates a copy of the given pointer, with an 
     * additional byte offset
     * 
     * @param other The other pointer 
     * @param byteOffset The additional byte offset
     */
    NativePointerObject(NativePointerObject other, long byteOffset)
    {
        this(other);
        this.byteOffset += byteOffset;
    }

    
    
    /**
     * Package-private method to obtain the native
     * pointer value.
     * 
     * @return The native pointer value
     */
    long getNativePointer()
    {
        return nativePointer;
    }
    
    /**
     * Returns the byte offset
     * 
     * @return The byte offset
     */
    long getByteOffset()
    {
        return byteOffset;
    }

    // TODO: --> Only used for cl_context_properties
    /**
     * Set the given buffer as the contents of this Pointer
     * 
     * @param buffer The buffer to set
     */
    void setBuffer(Buffer buffer)
    {
        this.buffer = buffer;
        pointers = null;
        byteOffset = 0;
    }
    
    /**
     * Returns the Buffer of this Pointer
     * 
     * @return The Buffer of this Pointer
     */
    Buffer getBuffer()
    {
        return buffer;
    }
    // TODO: <-- Only used for cl_context_properties
    
    
    /**
     * Returns a new pointer with an offset of the given number
     * of bytes
     * 
     * @param byteOffset The byte offset for the pointer
     * @return The new pointer with the given byte offset
     */
    NativePointerObject withByteOffset(long byteOffset)
    {
        return new NativePointerObject(this, byteOffset);
    }
    
    
    
    /**
     * Returns a String representation of this object.
     * 
     * @return A String representation of this object.
     */
    @Override
    public String toString()
    {
        if (buffer != null) 
        {
            return "NativePointerObject["+
                "buffer="+buffer+","+
                "byteOffset="+byteOffset+"]";
            
        }
        else if (pointers != null)
        {
            return "NativePointerObject["+
                "pointers="+Arrays.toString(pointers)+","+
                "byteOffset="+byteOffset+"]";
        }
        else
        {
            return "NativePointerObject["+
                "nativePointer=0x"+Long.toHexString(getNativePointer())+","+
                "byteOffset="+byteOffset+"]";
        }
    }

    
}

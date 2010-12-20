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

package org.jocl;

import java.nio.*;

/**
 * A Java representation of a void pointer.
 */
public final class Pointer extends NativePointerObject
{
    /**
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(byte values[])
    {
        return new Pointer(ByteBuffer.wrap(values));
    }
    
    /**
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(char values[])
    {
        return new Pointer(CharBuffer.wrap(values));
    }

    /**
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(short values[])
    {
        return new Pointer(ShortBuffer.wrap(values));
    }

    /**
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(int values[])
    {
        return new Pointer(IntBuffer.wrap(values));
    }

    /**
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(float values[])
    {
        return new Pointer(FloatBuffer.wrap(values));
    }

    /**
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(long values[])
    {
        return new Pointer(LongBuffer.wrap(values));
    }

    /**
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(double values[])
    {
        return new Pointer(DoubleBuffer.wrap(values));
    }
    


    /**
     * Creates a new Pointer to the given Buffer. The buffer
     * must not be null, and either be a direct buffer, or 
     * have a backing array.  
     * 
     * @param buffer The buffer the pointer should point to 
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is null or is neither direct nor has a backing array
     */
    public static Pointer to(Buffer buffer)
    {
        if (buffer == null || (!buffer.isDirect() && !buffer.hasArray()))
        {
            throw new IllegalArgumentException(
                "Buffer may not be null and must have an array or be direct");
        }
        return new Pointer(buffer);
    }

    /**
     * Creates a new Pointer to the given Pointer. The pointer 
     * may not be null.
     * 
     * @param pointer The pointer the pointer should point to
     * @return The new pointer
     * @throws IllegalArgumentException If the given pointer
     * is null
     */
    public static Pointer to(NativePointerObject pointer)
    {
        if (pointer == null)
        {
            throw new IllegalArgumentException(
                "Pointer may not point to null objects");
        }
        return new Pointer(new NativePointerObject[]{pointer});
    }

    /**
     * Creates a new Pointer to the given Pointers. The array
     * of pointers may not be null, and may not contain null
     * elements. 
     * 
     * @param pointers The pointers the pointer should point to
     * @return The new pointer
     * @throws IllegalArgumentException If the given array
     * is null
     */
    public static Pointer to(NativePointerObject ... pointers)
    {
        if (pointers == null)
        {
            throw new IllegalArgumentException(
                "Pointer may not point to null objects");
        }
        return new Pointer(pointers);
    }
    
    
    /**
     * Creates a new (null) Pointer
     */
    public Pointer()
    {
        super();
    }

    /**
     * Creates a Pointer to the given Buffer
     * 
     * @param buffer The buffer to point to
     */
    protected Pointer(Buffer buffer)
    {
        super(buffer);
    }

    /**
     * Creates a Pointer to the given array of pointers
     * 
     * @param pointers The array the pointer points to
     */
    private Pointer(NativePointerObject pointers[])
    {
        super(pointers);
    }
    
    /**
     * Copy constructor
     * 
     * @param other The other Pointer
     */
    protected Pointer(Pointer other)
    {
        super(other);
    }

    /**
     * Creates a copy of the given pointer, with an 
     * additional byte offset
     * 
     * @param other The other pointer 
     * @param byteOffset The additional byte offset
     */
    protected Pointer(Pointer other, long byteOffset)
    {
        super(other, byteOffset);
    }
    
    /**
     * Returns whether this Pointer is a Pointer to a direct Buffer.
     * 
     * @return Whether this pointer is a Pointer to a direct Buffer
     */
    boolean isDirectBufferPointer()
    {
        return getBuffer() != null && getBuffer().isDirect();
    }
    
    
    /**
     * Returns a new pointer with an offset of the given number
     * of bytes
     * 
     * @param byteOffset The byte offset for the pointer
     * @return The new pointer with the given byte offset
     */
    public Pointer withByteOffset(long byteOffset)
    {
        return new Pointer(this, byteOffset);
    }
    
}

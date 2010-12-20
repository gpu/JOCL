/*
 * JOCL - Java bindings for OpenCL
 * 
 * Copyright 2009 Marco Hutter - http://www.jocl.org/
 * 
 * 
 * This file is part of JOCL. 
 * 
 * JOCL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JOCL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License
 * along with JOCL.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jocl;

import java.nio.*;

/**
 * A Java representation of a void pointer.
 */
public class Pointer extends NativePointerObject
{
    /**
     * Creates a new Pointer to the given values.
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

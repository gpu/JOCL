/*
 * JOCL - Java bindings for OpenCL
 *
 * Copyright (c) 2009-2015 Marco Hutter - http://www.jocl.org
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
     * The error message that will be part of the IllegalArgumentException
     * when a <code>null</code> buffer is passed to the {@link #to(Buffer)}
     * or {@link #toBuffer(Buffer)} method
     */
    private static final String BUFFER_MAY_NOT_BE_NULL = 
        "The buffer may not be null";

    /**
     * The error message that will be part of the IllegalArgumentException
     * when a buffer is passed to the {@link #toBuffer(Buffer)} method 
     * neither has an array nor is direct 
     */
    private static final String BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT = 
        "The buffer must have an array or be direct";

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
    private Pointer(NativePointerObject[] pointers)
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
     * Creates a new Pointer to the given values.
     * The values may not be null.
     * 
     * @param values The values the pointer should point to 
     * @return The pointer
     */
    public static Pointer to(byte[] values)
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
    public static Pointer to(char[] values)
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
    public static Pointer to(short[] values)
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
    public static Pointer to(int[] values)
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
    public static Pointer to(float[] values)
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
    public static Pointer to(long[] values)
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
    public static Pointer to(double[] values)
    {
        return new Pointer(DoubleBuffer.wrap(values));
    }
    


    /**
     * <b>NOTE:</b> This method does not take into account the position
     * and array offset of the given buffer. In order to create a 
     * pointer that takes the position and array offset into account, 
     * use the {@link #toBuffer(Buffer)} method. <br>
     * <br>
     * 
     * If the given buffer has a backing array, then the returned 
     * pointer will in any case point to the start of the array, 
     * even if the buffer has been created using the <code>slice</code> 
     * method (like {@link ByteBuffer#slice()}). If the buffer is 
     * direct, then this method will return a Pointer to the address 
     * of the direct buffer. If the buffer has been created using the 
     * <code>slice</code> method, then this will be the actual start 
     * of the slice. Although this implies a different treatment of 
     * direct- and non direct buffers, the method is kept for 
     * backward compatibility. <br> 
     * <br>
     * In both cases, for direct and array-based buffers, this method 
     * does not take into account the position of the given buffer. <br>
     * <br>   
     * The buffer must not be null, and either be a direct buffer, or 
     * have a backing array
     * 
     * @param buffer The buffer the pointer should point to 
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is null or is neither direct nor has a backing array
     */
    public static Pointer to(Buffer buffer)
    {
        if (buffer == null)
        {
            throw new IllegalArgumentException(BUFFER_MAY_NOT_BE_NULL);
        }
        if (!buffer.isDirect() && !buffer.hasArray())
        {
            throw new IllegalArgumentException(
                BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT);
        }
        return new Pointer(buffer);
    }

    /**
     * Creates a new Pointer to the given buffer.<br> 
     * <br>
     * Note that this method takes into account the array offset and position 
     * of the given buffer, in contrast to the {@link #to(Buffer)} method.  
     * 
     * @param buffer The buffer
     * @return The new pointer
     * @throws IllegalArgumentException If the given buffer
     * is null or is neither direct nor has a backing array
     */
    public static Pointer toBuffer(Buffer buffer)
    {
        if (buffer == null)
        {
            throw new IllegalArgumentException(BUFFER_MAY_NOT_BE_NULL);
        }
        if (buffer instanceof ByteBuffer) 
        {
            return computePointer((ByteBuffer)buffer);
        }
        if (buffer instanceof ShortBuffer) 
        {
            return computePointer((ShortBuffer)buffer);
        }
        if (buffer instanceof IntBuffer) 
        {
            return computePointer((IntBuffer)buffer);
        }
        if (buffer instanceof LongBuffer) 
        {
            return computePointer((LongBuffer)buffer);
        }
        if (buffer instanceof FloatBuffer) 
        {
            return computePointer((FloatBuffer)buffer);
        }
        if (buffer instanceof DoubleBuffer) 
        {
            return computePointer((DoubleBuffer)buffer);
        }
        throw new IllegalArgumentException(
            "Unknown buffer type: "+buffer);
        
    }
    
    /**
     * Creates a new Pointer to the given buffer, taking into
     * account the position and array offset of the given buffer.
     * 
     * @param buffer The buffer
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is neither direct nor has a backing array
     */
    private static Pointer computePointer(ByteBuffer buffer)
    {
        Pointer result;
        if (buffer.isDirect())
        {
            int oldPosition = buffer.position();
            buffer.position(0);
            result = Pointer.to(buffer.slice()).withByteOffset(
                oldPosition * Sizeof.cl_char);
            buffer.position(oldPosition);
        }
        else if (buffer.hasArray())
        {
            ByteBuffer t = ByteBuffer.wrap(buffer.array());
            int elementOffset = buffer.position() + buffer.arrayOffset();
            result = Pointer.to(t).withByteOffset(
                elementOffset * Sizeof.cl_char);
        }
        else
        {
            throw new IllegalArgumentException(
                BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT);
        }
        return result;
    }

    
    /**
     * Creates a new Pointer to the given buffer, taking into
     * account the position and array offset of the given buffer.
     * 
     * @param buffer The buffer
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is neither direct nor has a backing array
     */
    private static Pointer computePointer(ShortBuffer buffer)
    {
        Pointer result;
        if (buffer.isDirect())
        {
            int oldPosition = buffer.position();
            buffer.position(0);
            result = Pointer.to(buffer.slice()).withByteOffset(
                oldPosition * Sizeof.cl_short);
            buffer.position(oldPosition);
        }
        else if (buffer.hasArray())
        {
            ShortBuffer t = ShortBuffer.wrap(buffer.array());
            int elementOffset = buffer.position() + buffer.arrayOffset();
            result = Pointer.to(t).withByteOffset(
                elementOffset * Sizeof.cl_short);
        }
        else
        {
            throw new IllegalArgumentException(
                BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT);
        }
        return result;
    }

    
    /**
     * Creates a new Pointer to the given buffer, taking into
     * account the position and array offset of the given buffer.
     * 
     * @param buffer The buffer
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is neither direct nor has a backing array
     */
    private static Pointer computePointer(IntBuffer buffer)
    {
        Pointer result;
        if (buffer.isDirect())
        {
            int oldPosition = buffer.position();
            buffer.position(0);
            result = Pointer.to(buffer.slice()).withByteOffset(
                oldPosition * Sizeof.cl_int);
            buffer.position(oldPosition);
        }
        else if (buffer.hasArray())
        {
            IntBuffer t = IntBuffer.wrap(buffer.array());
            int elementOffset = buffer.position() + buffer.arrayOffset();
            result = Pointer.to(t).withByteOffset(
                elementOffset * Sizeof.cl_int);
        }
        else
        {
            throw new IllegalArgumentException(
                BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT);
        }
        return result;
    }

    
    /**
     * Creates a new Pointer to the given buffer, taking into
     * account the position and array offset of the given buffer.
     * 
     * @param buffer The buffer
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is neither direct nor has a backing array
     */
    private static Pointer computePointer(LongBuffer buffer)
    {
        Pointer result;
        if (buffer.isDirect())
        {
            int oldPosition = buffer.position();
            buffer.position(0);
            result = Pointer.to(buffer.slice()).withByteOffset(
                oldPosition * Sizeof.cl_long);
            buffer.position(oldPosition);
        }
        else if (buffer.hasArray())
        {
            LongBuffer t = LongBuffer.wrap(buffer.array());
            int elementOffset = buffer.position() + buffer.arrayOffset();
            result = Pointer.to(t).withByteOffset(
                elementOffset * Sizeof.cl_long);
        }
        else
        {
            throw new IllegalArgumentException(
                BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT);
        }
        return result;
    }

    
    /**
     * Creates a new Pointer to the given buffer, taking into
     * account the position and array offset of the given buffer.
     * 
     * @param buffer The buffer
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is neither direct nor has a backing array
     */
    private static Pointer computePointer(FloatBuffer buffer)
    {
        Pointer result;
        if (buffer.isDirect())
        {
            int oldPosition = buffer.position();
            buffer.position(0);
            result = Pointer.to(buffer.slice()).withByteOffset(
                oldPosition * Sizeof.cl_float);
            buffer.position(oldPosition);
        }
        else if (buffer.hasArray())
        {
            FloatBuffer t = FloatBuffer.wrap(buffer.array());
            int elementOffset = buffer.position() + buffer.arrayOffset();
            result = Pointer.to(t).withByteOffset(
                elementOffset * Sizeof.cl_float);
        }
        else
        {
            throw new IllegalArgumentException(
                BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT);
        }
        return result;
    }

    
    /**
     * Creates a new Pointer to the given buffer, taking into
     * account the position and array offset of the given buffer.
     * 
     * @param buffer The buffer
     * @return The pointer
     * @throws IllegalArgumentException If the given buffer
     * is neither direct nor has a backing array
     */
    private static Pointer computePointer(DoubleBuffer buffer)
    {
        Pointer result;
        if (buffer.isDirect())
        {
            int oldPosition = buffer.position();
            buffer.position(0);
            result = Pointer.to(buffer.slice()).withByteOffset(
                oldPosition * Sizeof.cl_double);
            buffer.position(oldPosition);
        }
        else if (buffer.hasArray())
        {
            DoubleBuffer t = DoubleBuffer.wrap(buffer.array());
            int elementOffset = buffer.position() + buffer.arrayOffset();
            result = Pointer.to(t).withByteOffset(
                elementOffset * Sizeof.cl_double);
        }
        else
        {
            throw new IllegalArgumentException(
                BUFFER_MUST_HAVE_ARRAY_OR_BE_DIRECT);
        }
        return result;
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
    
    

    /**
     * Returns a ByteBuffer that corresponds to the specified
     * segment of the memory that this pointer points to.<br>
     * <br>
     * This function is solely intended for pointers that that
     * have been allocated with {@link CL#clSVMAlloc}.
     * <br>
     * (It will work for all pointers to ByteBuffers, but for 
     * other pointer types, <code>null</code> will be returned)
     *
     * @param byteOffset The offset in bytes
     * @param byteSize The size of the byte buffer, in bytes
     * @return The byte buffer
     */
    public ByteBuffer getByteBuffer(long byteOffset, long byteSize)
    {
        Buffer buffer = getBuffer();
        if (buffer == null)
        {
            return null;
        }
        if (!(buffer instanceof ByteBuffer))
        {
            return null;
        }
        ByteBuffer byteBuffer = (ByteBuffer)buffer;
        byteBuffer.limit((int)(byteOffset + byteSize));
        byteBuffer.position((int)byteOffset);
        return byteBuffer.slice();
    }
    
    
}

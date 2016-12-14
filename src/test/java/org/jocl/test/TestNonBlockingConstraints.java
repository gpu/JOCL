package org.jocl.test;

import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_WRITE_ONLY;
import static org.jocl.CL.CL_NON_BLOCKING;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clEnqueueWriteBuffer;
import static org.jocl.CL.clFinish;
import static org.jocl.CL.clFlush;

import java.nio.ByteBuffer;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_mem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test the constraints for non-blocking operations, and whether the 
 * IllegalArgumentException is thrown accordingly: 
 * A non-blocking operation will
 * - throw for a pointer to a Java array
 * - throw for a pointer to a Java byte buffer
 * - NOT throw for a pointer to a DIRECT byte buffer
 */
public class TestNonBlockingConstraints extends JOCLAbstractTest
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testNonBlockingReadWithJavaArrayThrows()
    {
        initCL(defaultPlatformIndex, defaultDeviceType, defaultDeviceIndex);
        
        cl_mem mem = clCreateBuffer(context, CL_MEM_READ_ONLY, 
            10 * Sizeof.cl_float, null, null);
        
        Pointer pointer = Pointer.to(new float[10]);

        exception.expect(IllegalArgumentException.class);    
        clEnqueueReadBuffer(commandQueue, mem, CL_NON_BLOCKING, 0, 
            10 * Sizeof.cl_float, pointer, 0, null, null);

        clFlush(commandQueue);
        clFinish(commandQueue);
    }
    
    @Test
    public void testNonBlockingReadWithBufferThrows()
    {
        initCL(defaultPlatformIndex, defaultDeviceType, defaultDeviceIndex);
        
        cl_mem mem = clCreateBuffer(context, CL_MEM_READ_ONLY, 
            10 * Sizeof.cl_float, null, null);
        
        Pointer pointer = Pointer.to(ByteBuffer.allocate(10 * Sizeof.cl_float));

        exception.expect(IllegalArgumentException.class);    
        clEnqueueReadBuffer(commandQueue, mem, CL_NON_BLOCKING, 0, 
            10 * Sizeof.cl_float, pointer, 0, null, null);
        
        clFlush(commandQueue);
        clFinish(commandQueue);
    }
    
    @Test
    public void testNonBlockingReadWithDirectBuffer()
    {
        initCL(defaultPlatformIndex, defaultDeviceType, defaultDeviceIndex);
        
        cl_mem mem = clCreateBuffer(context, CL_MEM_READ_ONLY, 
            10 * Sizeof.cl_float, null, null);
        
        Pointer pointer = Pointer.to(
            ByteBuffer.allocateDirect(10 * Sizeof.cl_float));

        clEnqueueReadBuffer(commandQueue, mem, CL_NON_BLOCKING, 0, 
            10 * Sizeof.cl_float, pointer, 0, null, null);
        
        clFlush(commandQueue);
        clFinish(commandQueue);
    }
    
    
    @Test
    public void testNonBlockingWriteWithJavaArrayThrows()
    {
        initCL(defaultPlatformIndex, defaultDeviceType, defaultDeviceIndex);
        
        cl_mem mem = clCreateBuffer(context, CL_MEM_WRITE_ONLY, 
            10 * Sizeof.cl_float, null, null);
        
        Pointer pointer = Pointer.to(new float[10]);

        exception.expect(IllegalArgumentException.class);    
        clEnqueueWriteBuffer(commandQueue, mem, CL_NON_BLOCKING, 0, 
            10 * Sizeof.cl_float, pointer, 0, null, null);

        clFlush(commandQueue);
        clFinish(commandQueue);
    }
    
    @Test
    public void testNonBlockingWriteWithBufferThrows()
    {
        initCL(defaultPlatformIndex, defaultDeviceType, defaultDeviceIndex);
        
        cl_mem mem = clCreateBuffer(context, CL_MEM_WRITE_ONLY, 
            10 * Sizeof.cl_float, null, null);
        
        Pointer pointer = Pointer.to(ByteBuffer.allocate(10 * Sizeof.cl_float));

        exception.expect(IllegalArgumentException.class);    
        clEnqueueWriteBuffer(commandQueue, mem, CL_NON_BLOCKING, 0, 
            10 * Sizeof.cl_float, pointer, 0, null, null);
        
        clFlush(commandQueue);
        clFinish(commandQueue);
    }
    
    @Test
    public void testNonBlockingWriteWithDirectBuffer()
    {
        initCL(defaultPlatformIndex, defaultDeviceType, defaultDeviceIndex);
        
        cl_mem mem = clCreateBuffer(context, CL_MEM_READ_ONLY, 
            10 * Sizeof.cl_float, null, null);
        
        Pointer pointer = Pointer.to(
            ByteBuffer.allocateDirect(10 * Sizeof.cl_float));

        clEnqueueWriteBuffer(commandQueue, mem, CL_NON_BLOCKING, 0, 
            10 * Sizeof.cl_float, pointer, 0, null, null);
        
        clFlush(commandQueue);
        clFinish(commandQueue);
    }
    
}

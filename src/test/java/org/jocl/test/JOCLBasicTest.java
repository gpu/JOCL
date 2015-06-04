package org.jocl.test;

import static org.jocl.CL.*;
import static org.junit.Assert.assertTrue;

import org.jocl.*;
import org.junit.Test;

/**
 * A basic vector addition test
 */
public class JOCLBasicTest extends JOCLAbstractTest
{
    /**
     * The source code of the OpenCL program to execute
     */
    private static String programSource =
        "__kernel void "+
        "sampleKernel(__global const float *a,"+
        "             __global const float *b,"+
        "             __global float *c)"+
        "{"+
        "    int gid = get_global_id(0);"+
        "    c[gid] = a[gid] + b[gid];"+
        "}";
    
    @Test
    public void basicTest()
    {
        initCL(defaultPlatformIndex, defaultDeviceType, defaultDeviceIndex);
        initKernel("sampleKernel", programSource);
        
        
        // Create input- and output data 
        int n = 10;
        float srcArrayA[] = new float[n];
        float srcArrayB[] = new float[n];
        float dstArray[] = new float[n];
        for (int i=0; i<n; i++)
        {
            srcArrayA[i] = i;
            srcArrayB[i] = i;
        }

        // Allocate the memory objects for the input- and output data
        cl_mem srcMemA = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * n, Pointer.to(srcArrayA), null);
        cl_mem srcMemB = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_float * n, Pointer.to(srcArrayB), null);
        cl_mem dstMem = clCreateBuffer(context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_float * n, null, null);
        
        // Set the arguments for the kernel
        int a = 0;
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(srcMemA));
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(srcMemB));
        clSetKernelArg(kernel, a++, Sizeof.cl_mem, Pointer.to(dstMem));
        
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
            new long[] {n}, null, 0, null, null);
        
        // Read the output data
        clEnqueueReadBuffer(commandQueue, dstMem, CL_TRUE, 0,
            n * Sizeof.cl_float, Pointer.to(dstArray), 0, null, null);
        
        // Compute the reference result
        float expected[] = new float[n];
        for (int i=0; i<n; i++)
        {
            expected[i] = srcArrayA[i] + srcArrayB[i];
        }
        
        boolean passed = epsilonEqual(expected, dstArray);
        
        shutdownKernel();
        shutdownCL();
        
        assertTrue(passed);
    }
    
    
    /**
     * Returns whether the given arrays are equal up to a small epsilon
     * 
     * @param expected The expected results
     * @param actual The actual results
     * @return Whether the arrays are epsilon-equal
     */
    private static boolean epsilonEqual(float expected[], float actual[])
    {
        final float epsilon = 1e-7f;
        if (expected.length != actual.length)
        {
            return false;
        }
        for (int i=0; i<expected.length; i++)
        {
            float x = expected[i];
            float y = actual[i];
            boolean epsilonEqual = Math.abs(x - y) <= epsilon * Math.abs(x);
            if (!epsilonEqual)
            {
                return false;
            }
        }
        return true;
    }
    
}

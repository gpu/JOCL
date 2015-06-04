package org.jocl.test;

import static org.junit.Assert.*;

import org.jocl.CL;
import org.junit.Test;

/**
 * The most basic possible test: Checks whether there is at least
 * one OpenCL platform available
 */
public class JOCLMinimalPlatformTest
{
    @Test
    public void testGetPlatformIDs()
    {
        int numPlatforms[] = { 0 };
        CL.clGetPlatformIDs(0, null, numPlatforms);
        assertTrue(numPlatforms[0] > 0);
    }
}

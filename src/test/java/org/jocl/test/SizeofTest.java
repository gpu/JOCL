package org.jocl.test;

import static org.junit.Assert.assertTrue;

import org.jocl.Sizeof;
import org.junit.Test;

/**
 * Test whether the native methods in the Sizeof class can be called without 
 * using any of the CL functions first
 */
public class SizeofTest 
{
    @Test
    public void sizeofTest()
    {
        assertTrue(Sizeof.POINTER != 0);
    }
}
        

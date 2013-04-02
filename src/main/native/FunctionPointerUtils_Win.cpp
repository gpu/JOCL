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

#ifdef _WIN32

#include "FunctionPointerUtils.hpp"
#include "Logger.hpp"

#include <windows.h>

HMODULE libraryHandle = NULL;

/**
 * The address of the function with the given name is obtained from 
 * the library and returned
 */
intptr_t obtainFunctionPointer(const char* name)
{
    SetLastError(0);
    intptr_t result = (intptr_t)GetProcAddress(libraryHandle, name);
    DWORD lastError = GetLastError();
    if (lastError != 0)
    {
        //printf("Function pointer %p for %s - ERROR %d\n", result, name, lastError);
        SetLastError(0);
    }
    else
    {
        //printf("Function pointer %p for %s\n", result, name);
    }
	return result;
}

/**
 * Prepare the initialization of the function pointers by loading
 * the specified library. Returns whether this initialization 
 * succeeded.
 */
bool loadImplementationLibrary(const char *libraryName)
{
    SetLastError(0);
    libraryHandle = LoadLibrary(libraryName);
    if (libraryHandle == NULL)
    {
        DWORD lastError = GetLastError();
        SetLastError(0);
		Logger::log(LOG_ERROR, "Could not load %s, error %ld\n", libraryName, lastError);
		return false;
    }
	return true;
}

/**
 * Unload the OpenCL library
 */
bool unloadImplementationLibrary()
{
	if (libraryHandle != NULL)
	{
		SetLastError(0);
		FreeLibrary(libraryHandle);
	    DWORD lastError = GetLastError();
		if (lastError != 0)
		{
    		Logger::log(LOG_ERROR, "Could not unload implementation library, error %ld\n", lastError);
		    SetLastError(0);
			return false;
		}
	}
	return true;
}


#endif _WIN32
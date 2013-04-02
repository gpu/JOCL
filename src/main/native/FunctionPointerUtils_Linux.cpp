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

// The dlfcn.h header file should also be available on MacOS X >=10.3,
// according to https://developer.apple.com/library/mac/documentation/
// developertools/Reference/MachOReference/Reference/reference.html

#ifndef _WIN32

#include "FunctionPointerUtils.hpp"
#include "Logger.hpp"

// NOTE: Link with -ldl. 

#include <stdint.h>
#include <dlfcn.h>

void* libraryHandle;

/**
 * The address of the function with the given name is obtained from 
 * the library and returned
 */
intptr_t obtainFunctionPointer(const char* name)
{ 
	dlerror();
    intptr_t result = (intptr_t)dlsym(libraryHandle, name);
    char *lastError = dlerror();
    if (lastError != NULL)
    {
        //printf("Function pointer %p for %s - ERROR: %s\n", result, name, lastError);
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
	dlerror();
    libraryHandle = dlopen(libraryName, RTLD_LAZY);
    if (libraryHandle == NULL)
    {
        char *lastError = dlerror();
		Logger::log(LOG_ERROR, "Could not load %s, error %s\n", libraryName, lastError);
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
		int lastError = dlclose(libraryHandle);
		if (lastError != 0)
		{
		    Logger::log(LOG_ERROR, "Could not unload implementation library, error %d\n", lastError);
			return false;
		}
	}
	return true;
}

#endif // _WIN32
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

#include "Logger.hpp"

#include <cstdio>

LogLevel Logger::currentLogLevel = LOG_ERROR; 
//LogLevel Logger::currentLogLevel = LOG_DEBUGTRACE;; 

void Logger::log(LogLevel level, char *message, ...)
{
	if (level <= Logger::currentLogLevel)
	{
        va_list argp;
        va_start(argp, message);
        vfprintf(stdout, message, argp);
        va_end(argp);
	}
}

void Logger::setLogLevel(LogLevel level)
{
	Logger::currentLogLevel = level;
}

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

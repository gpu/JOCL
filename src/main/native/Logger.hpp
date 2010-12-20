#ifndef LOGGER
#define LOGGER

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

enum LogLevel {LOG_QUIET, LOG_ERROR, LOG_WARNING, LOG_INFO, LOG_DEBUG, LOG_TRACE, LOG_DEBUGTRACE};  

class Logger
{
    public:
	    static void log(LogLevel level, char* message, ...);
		static void setLogLevel(LogLevel level);

    private:
		static LogLevel currentLogLevel;

};

#endif
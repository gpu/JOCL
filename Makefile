# Makefile for JOCL


# Base name of the library (the parts that are specific 
# for OS and architecture will be appended)
DYNAMIC_LIB_BASE_NAME	:= JOCL

# C++ source files (compiled with c++)
CCFILES		:= JOCL.cpp Logger.cpp CLJNIUtils.cpp CLFunctions.cpp FunctionPointerUtils.cpp FunctionPointerUtils_Linux.cpp JNIUtils.cpp PointerUtils.cpp

# Root directory of the OpenCL installation
OCLROOTDIR := /usr/local/ati-stream-sdk-v2.3-lnx32/

#TODO_1_2: Omitting the library directory
#OCLLIBDIR     := $(OCLROOTDIR)/lib/x86

OCLINCDIR     := $(OCLROOTDIR)/include


# JNI includes
JNI_INCLUDES	:= -I/usr/java/jdk1.6.0_16/include -I/usr/java/jdk1.6.0_16/include/linux
# For MacOS it seems to be something like that:
#JNI_INCLUDES	:= -I/System/Library/Frameworks/JavaVM.framework/Headers

# Seems to be only required for MacOS: This should
# be the path to the directory containing "libGLEW.a"
GL_LIB_PATH := 
# For example:
#GL_LIB_PATH := "/Developer/GPU Computing/C/common/lib/darwin"


################################################################################
# Rules and targets

include Makefile_common.mk



# Makefile for JOCL


# Base name of the library (the parts that are specific 
# for OS and architecture will be appended)
DYNAMIC_LIB_BASE_NAME	:= JOCL

# C++ source files (compiled with c++)
CCFILES		:= JOCL.cpp Logger.cpp

# Root directory of the OpenCL installation
OCLROOTDIR    := /usr/local/ati-stream-sdk-v2.0-beta4-lnx32/
#OCLROOTDIR    := /opt/cuda
OCLLIBDIR     := $(OCLROOTDIR)/lib/x86
OCLINCDIR     := $(OCLROOTDIR)/include

JDK_HOME      := /usr/java/jdk1.6.0_16
#JDK_HOME      := $(JAVA_HOME)

# JNI includes

ifneq ($(DARWIN),)
	JNI_INCLUDES	:= -I/System/Library/Frameworks/JavaVM.framework/Headers
else
	JNI_INCLUDES	:= -I$(JDK_HOME)/include -I$(JDK_HOME)/include/linux
endif

# Seems to be only required for MacOS: This should
# be the path to the directory containing "libGLEW.a"
ifneq ($(DARWIN),)
	# For example:
	GL_LIB_PATH := "/Developer/GPU Computing/C/common/lib/darwin"
else
	JGL_LIB_PATH := 
endif


################################################################################
# Rules and targets

include Makefile_common.mk



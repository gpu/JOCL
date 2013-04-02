################################################################################
#
# This makefile was originally provided by NVIDIA for compiling the
# OpenCL samples. It has been adapted for compiling the JOCL library.
#
################################################################################


################################################################################
#
# Copyright 1993-2006 NVIDIA Corporation.  All rights reserved.
#
# NOTICE TO USER:   
#
# This source code is subject to NVIDIA ownership rights under U.S. and 
# international Copyright laws.  
#
# NVIDIA MAKES NO REPRESENTATION ABOUT THE SUITABILITY OF THIS SOURCE 
# CODE FOR ANY PURPOSE.  IT IS PROVIDED "AS IS" WITHOUT EXPRESS OR 
# IMPLIED WARRANTY OF ANY KIND.  NVIDIA DISCLAIMS ALL WARRANTIES WITH 
# REGARD TO THIS SOURCE CODE, INCLUDING ALL IMPLIED WARRANTIES OF 
# MERCHANTABILITY, NONINFRINGEMENT, AND FITNESS FOR A PARTICULAR PURPOSE.   
# IN NO EVENT SHALL NVIDIA BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL, 
# OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS 
# OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE 
# OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE 
# OR PERFORMANCE OF THIS SOURCE CODE.  
#
# U.S. Government End Users.  This source code is a "commercial item" as 
# that term is defined at 48 C.F.R. 2.101 (OCT 1995), consisting  of 
# "commercial computer software" and "commercial computer software 
# documentation" as such terms are used in 48 C.F.R. 12.212 (SEPT 1995) 
# and is provided to the U.S. Government only as a commercial end item.  
# Consistent with 48 C.F.R.12.212 and 48 C.F.R. 227.7202-1 through 
# 227.7202-4 (JUNE 1995), all U.S. Government End Users acquire the 
# source code with only those rights set forth herein.
#
################################################################################
#
# Common build script
#
################################################################################

.SUFFIXES : .cl

P4_ROOT ?= ${HOME}/perforce/



# detect OS
OSUPPER = $(shell uname -s 2>/dev/null | tr [:lower:] [:upper:])
OSLOWER = $(shell uname -s 2>/dev/null | tr [:upper:] [:lower:])
# 'linux' is output for Linux system, 'darwin' for OS X
DARWIN = $(strip $(findstring DARWIN, $(OSUPPER)))


# detect if 32 bit or 64 bit system
HP_64 =	$(shell uname -m | grep 64)


# Basic directory setup for SDK
# (override directories only if they are not already defined)
SRCDIR     ?= ./src/
ROOTDIR    ?= .
ROOTOBJDIR ?= ./obj
LIBDIR     := ./lib/$(OSLOWER)
SHAREDDIR  := $(ROOTDIR)/shared/
INCDIR	?= .



# Build the final library name for JOCL

DYNAMIC_LIB := lib$(DYNAMIC_LIB_BASE_NAME)

ifeq ($(emu), 1)
	DYNAMIC_LIB := $(DYNAMIC_LIB)Emu
endif

ifneq ($(DARWIN),)
	DYNAMIC_LIB := $(DYNAMIC_LIB)-apple
else
	DYNAMIC_LIB := $(DYNAMIC_LIB)-linux
endif

DYNAMIC_LIB := $(DYNAMIC_LIB)-x86

ifneq "$(strip $(HP_64))" ""
	DYNAMIC_LIB := $(DYNAMIC_LIB)_64
endif

LIB_EXTENSION := 
ifneq ($(DARWIN),)
	LIB_EXTENSION := .dylib
else
	LIB_EXTENSION := .so
endif
DYNAMIC_LIB := $(DYNAMIC_LIB)$(LIB_EXTENSION)



# Compilers
CXX        := g++
CC         := gcc
LINK       := g++ -fPIC

# Includes
INCLUDES  += -I$(INCDIR) -I$(OCLINCDIR) $(JNI_INCLUDES)

ifeq "$(strip $(HP_64))" ""
	MACHINE := 32
	USRLIBDIR := -L/usr/lib/
else
	MACHINE := 64
	USRLIBDIR := -L/usr/lib64/
endif


# Warning flags
CXXWARN_FLAGS := \
	-W -Wall \
	-Wimplicit \
	-Wswitch \
	-Wformat \
	-Wchar-subscripts \
	-Wparentheses \
	-Wmultichar \
	-Wtrigraphs \
	-Wpointer-arith \
	-Wcast-align \
	-Wreturn-type \
	-Wno-unused-function \
	$(SPACE)

CWARN_FLAGS := $(CXXWARN_FLAGS) \
	-Wstrict-prototypes \
	-Wmissing-prototypes \
	-Wmissing-declarations \
	-Wnested-externs \
	-Wmain \

# Compiler-specific flags
CXXFLAGS  := $(CXXWARN_FLAGS)
CFLAGS    := $(CWARN_FLAGS)

# Common flags
COMMONFLAGS += $(INCLUDES) -DUNIX

# Add Mac Flags
ifneq ($(DARWIN),)
	COMMONFLAGS += -DMAC
endif

# Debug/release configuration
ifeq ($(dbg),1)
	COMMONFLAGS += -g
	BINSUBDIR   := debug
	LIBSUFFIX   := D
else 
	COMMONFLAGS += -O3 
	BINSUBDIR   := release
	LIBSUFFIX   :=
	CXXFLAGS    += -fno-strict-aliasing
	CFLAGS      += -fno-strict-aliasing
endif


# OpenGL is used or not (if it is used, then it is necessary to include GLEW)
ifeq ($(USEGLLIB),1)

	ifneq ($(DARWIN),)
		OPENGLLIB := -L/System/Library/Frameworks/OpenGL.framework/Libraries -lGL -lGLU $(SHAREDDIR)/lib/$(OSLOWER)/libGLEW.a
	else
		OPENGLLIB := -lGL -lGLU -lX11 -lXmu
		ifeq "$(strip $(HP_64))" ""
			OPENGLLIB += -lGLEW -L/usr/X11R6/lib
		else
			OPENGLLIB += -lGLEW_x86_64 -L/usr/X11R6/lib64
		endif
	endif

	CUBIN_ARCH_FLAG := -m64
endif

ifeq ($(USEGLUT),1)
	ifneq ($(DARWIN),)
		OPENGLLIB += -framework GLUT
		INCLUDES += -I/System/Library/Frameworks/OpenGL.framework/Headers
	else
		OPENGLLIB += -lglut
	endif
endif

# Libs
ifneq ($(DARWIN),)
   LIB       := -L${OCLLIBDIR} -L$(LIBDIR) -L$(SHAREDDIR)/lib/$(OSLOWER) 

# TODO_1_2: Omitting the OpenCL library. Old line:
#   LIB += -framework OpenCL -framework OpenGL ${OPENGLLIB} -framework AppKit ${ATF} ${LIB} 
# TODO_1_2: Omitting the OpenCL library. New line:
   LIB += -framework OpenGL ${OPENGLLIB} -framework AppKit ${ATF} ${LIB}    
   
else
   LIB       := ${USRLIBDIR} -L${OCLLIBDIR} -L$(LIBDIR) -L$(SHAREDDIR)/lib/$(OSLOWER) 

# TODO_1_2: Omitting the OpenCL library. Old line:
#   LIB += -lOpenCL ${OPENGLLIB} ${LIB} 
# TODO_1_2: Omitting the OpenCL library. New line:
   LIB +=  ${OPENGLLIB} ${LIB} 

endif





# Target library configuration for JOCL
TARGETDIR := $(LIBDIR)
TARGET   := $(subst $(LIB_EXTENSION),$(LIBSUFFIX)$(LIB_EXTENSION),$(LIBDIR)/$(DYNAMIC_LIB))
# TODO_1_2: Adding -ldl linker flag. Old line:
#LINKLINE  = $(LINK) -o $(TARGET) --shared $(OBJS) $(LIB)
# TODO_1_2: Adding -ldl linker flag. New line:
LINKLINE  = $(LINK) -o $(TARGET) --shared $(OBJS) -ldl $(LIB)


# check if verbose 
ifeq ($(verbose), 1)
	VERBOSE :=
else
	VERBOSE := @
endif

# Add common flags
CXXFLAGS  += $(COMMONFLAGS)
CFLAGS    += $(COMMONFLAGS)


################################################################################
# Set up object files
################################################################################
OBJDIR := $(ROOTOBJDIR)/$(BINSUBDIR)
OBJS +=  $(patsubst %.cpp,$(OBJDIR)/%.cpp.o,$(notdir $(CCFILES)))
OBJS +=  $(patsubst %.c,$(OBJDIR)/%.c.o,$(notdir $(CFILES)))

################################################################################
# Rules
################################################################################
$(OBJDIR)/%.c.o : $(SRCDIR)%.c $(C_DEPS)
	$(VERBOSE)$(CC) $(CFLAGS) -o $@ -c $<

$(OBJDIR)/%.cpp.o : $(SRCDIR)%.cpp $(C_DEPS)
	$(VERBOSE)$(CXX) $(CXXFLAGS) -o $@ -c $<

$(TARGET): makedirectories $(OBJS) Makefile
	$(VERBOSE)$(LINKLINE)

makedirectories:
	$(VERBOSE)mkdir -p $(LIBDIR)
	$(VERBOSE)mkdir -p $(OBJDIR)
	$(VERBOSE)mkdir -p $(TARGETDIR)


tidy :
	$(VERBOSE)find . | egrep "#" | xargs rm -f
	$(VERBOSE)find . | egrep "\~" | xargs rm -f

clean : tidy
	$(VERBOSE)rm -f $(OBJS)
	$(VERBOSE)rm -f $(TARGET)

clobber : clean
	$(VERBOSE)rm -rf $(ROOTOBJDIR)
	$(VERBOSE)find $(TARGETDIR) | egrep "ptx" | xargs rm -f
	$(VERBOSE)find $(TARGETDIR) | egrep "txt" | xargs rm -f
	$(VERBOSE)rm -f $(TARGETDIR)/samples.list

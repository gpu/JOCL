
To compile JOCL for Windows, the following environment 
variables have to be set:

JNI_INC_PATH : Path to the Java Native Interface headers 
CL_INC_PATH  : Path to the OpenCL headers
CL_LIB_PATH  : Path to the OpenCL libraries
GL_INC_PATH  : Path to the OpenGL headers
GL_LIB_PATH  : Path to the OpenGL libraries


To compile JOCL for Linux or MacOS, the directories
in the 'Makefile' have to be set according to the
location where OpenCL is installed.
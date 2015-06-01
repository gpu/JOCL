cmake_minimum_required(VERSION 2.8)

# Overriding the CMake flags to use static runtime libraries
# See http://www.cmake.org/Wiki/CMake_FAQ#
#     How_can_I_build_my_MSVC_application_with_a_static_runtime.3F
set(CMAKE_USER_MAKE_RULES_OVERRIDE
   ${CMAKE_CURRENT_SOURCE_DIR}/CMake_c_flag_overrides.cmake)
set(CMAKE_USER_MAKE_RULES_OVERRIDE_CXX
   ${CMAKE_CURRENT_SOURCE_DIR}/CMake_cxx_flag_overrides.cmake)
   
project(JOCL)

set(JOCL_VERSION "0_2_0")

set(CMAKE_MODULE_PATH 
  "${CMAKE_SOURCE_DIR}/CMake"
  ${CMAKE_MODULE_PATH})

find_package(JNI REQUIRED)

if(CMAKE_HOST_WIN32)
  set(CMAKE_HOST "windows")
elseif(CMAKE_HOST_APPLE)
  set(CMAKE_HOST "apple")
  set(CMAKE_SKIP_RPATH FALSE)
elseif(CMAKE_HOST_UNIX)
  set(CMAKE_HOST "linux")
endif()

if(CMAKE_SIZEOF_VOID_P EQUAL 8)
  set(CMAKE_ARCH "x86_64")
else()
  set(CMAKE_ARCH "x86")
endif()

set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_DEBUG   ${PROJECT_SOURCE_DIR}/nativeLibs/Debug)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_RELEASE ${PROJECT_SOURCE_DIR}/nativeLibs)

set(BUILD_SHARED_LIBS ON)

include_directories(
  src/main/native
  src/main/include/
  ${JNI_INCLUDE_DIRS}
)
  
add_library(JOCL_${JOCL_VERSION}-${CMAKE_HOST}-${CMAKE_ARCH}
  src/main/native/JOCL.cpp 
  src/main/native/Logger.cpp 
  src/main/native/CLJNIUtils.cpp 
  src/main/native/CLFunctions.cpp 
  src/main/native/FunctionPointerUtils.cpp 
  src/main/native/FunctionPointerUtils_Linux.cpp 
  src/main/native/FunctionPointerUtils_Win.cpp 
  src/main/native/JNIUtils.cpp 
  src/main/native/PointerUtils.cpp
)

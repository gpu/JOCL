# JOCL

JOCL - Java Bindings for OpenCL - http://jocl.org

Build instructions
------------------

In order to build JOCL, create a local working directory, e.g.
`C:\JOCLRoot`, and clone the required repositories into this
directory:

    git clone https://github.com/gpu/JOCL.git
    git clone https://github.com/gpu/JOCLCommon.git
    
   
**Building the native libraries**

The native libraries of JOCL can be built with [CMake](http://www.cmake.org/)
and any compatible target compiler (e.g. Visual Studio or GCC):

* Start `cmake-gui`,
* Set the directory containing the sources of the `JOCL` project, e.g. `C:\JOCLRoot\JOCL`
* Set the directory for the build files: e.g. `C:\JOCLRoot\JOCL.build`
* Press "Configure" (and select the appropriate compiler)
* Press "Generate"

Then, `C:\JOCLRoot\JOCL.build` will contain the build files, e.g. the
GCC makefiles or the Visual Studio project files. Compiling the project
with these makefiles will place the binaries into a `nativeLibraries`
subdirectory of the project, e.g. into 
`C:\JOCLRoot\JOCL\nativeLibraries`.


**Building and packaging the external native library dependencies**

JOCL itself does not have external dependencies, except for the run-time
dependency to the OpenCL implementation. If you only want to compile 
JOCL, then you can skip this section.

This section only refers to libraries like 
[JOCLBLAS](https://github.com/gpu/JOCLBLAS) and
[JOCLBlast](https://github.com/gpu/JOCLBlast), which require installations of 
external libraries ([clBLAS](https://github.com/clMathLibraries/clBLAS/) and
[CLBlast](https://github.com/CNugteren/CLBlast/), respectively). The following 
JOCLBlast and CLBlast, but the same process applies to all other external 
dependencies.     

Compiling and installing CLBlast (as described in its documentation) will 
result in directories that contain the header files and the native libraries. 
In order to build JOCLBlast, the paths to these header directories and 
library files have to be entered in the CMake GUI, as the values of the
`CLBLAST_INSTALL_DIR` and `CLBlast_LIBRARY`. 

After this information has been entered, the JOCLBlast library may be 
compiled as described above, under **Building the native libraries**.
The resulting native JOCLBlast library will be placed into
the `nativeLibraries` directory of the project, for example, into
`C:\JOCLRoot\JOCLBlast\nativeLibraries`

This library is *dynamically* linked against the actual CLBlast library.
In order to be able to load this library, the CLBlast library must
be in a specific subdirectory of the `nativeLibraries` folder.
The name of this subdirectory is determined by the operating system and
architecture that the library is compiled for. The general pattern here is

`C:\JOCLRoot\JOCLBlast\nativeLibraries\OS\ARCHITECTURE` 

The `OS` here refers to the operating system, and will usually be `windows`, 
`linux` or `apple`. The `ARCHITECTURE` refers to the processor architecture 
of the target system, and will usually be `x86` (for 32 bit system) or 
`x86_64` (for 64 bit systems).

For example, on 64 bit Windows, the `clblast.dll` will have to be placed
into the directory

`C:\JOCLRoot\JOCLBlast\nativeLibraries\windows\x86_64\`

There, it will be picked up by the Maven and integrated into the JAR, 
as described in the next section.





**Building the Java libraries**

The actual Java libraries can be built with 
[Apache Maven](https://maven.apache.org/). After the native libraries
have been built as described above, change into the `JOCL` directory
and execute 

    mvn clean install

This will compile the Java libraries, run the unit tests, assemble the 
classes (together with the native libraries), sources and JavaDocs into 
JAR files, and finally place all libraries into the  
`C:\JOCLRoot\JOCL\target` directory.


**Building for Android**

Compiling native code for Android is a bit of a pain, so we use [android-cmake](https://github.com/taka-no-me/android-cmake)
to make our lives a bit easier. We first begin by installing the android-cmake
toolchain file into our cmake modules path. On Linux, this is likely
`/usr/share/cmake-3.2/Modules/`.

    cd /usr/share/cmake-3.2/Modules
    sudo wget https://github.com/taka-no-me/android-cmake/raw/master/android.toolchain.cmake

Next, we want to configure the build for our particular Android target.

    cd JOCL
    mkdir build
    cd build
    cmake -DCMAKE_TOOLCHAIN_FILE=android.toolchain \
          -DANDROID_ABI=armeabi-v7a \
          -DANDROID_NATIVE_API_LEVEL=21 \
          -DCMAKE_BUILD_TYPE=Release \
          ..

This should be enough to get you started. For more advanced configuration,
refer to the [android-cmake](https://github.com/taka-no-me/android-cmake)
documentation.

Finally, when building the final .jar file, we would like to avoid running the
local tests, as the Android native libraries won't run on your local machine.

    mvn clean install -DskipTests

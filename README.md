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
GCC makefiles or the Visual Studio project files. Compiling the
with these makefiles will place the binaries into a `nativeLibrary`
subdirectory of the project, e.g. into 
`C:\JOCLRoot\JOCL\nativeLibrary`.


**Building the Java libraries**

The actual Java libraries can be built with 
[Apache Maven](https://maven.apache.org/). After the native libraries
have been built as described above, change into the `JOCL` directory
and execute 

    mvn clean install

This will compile the Java libraries, run the unit tests, assemble the 
classes (together with the native library), sources and JavaDocs into 
JAR files, and finally place all libraries into the  
`C:\JOCLRoot\JOCL\target` directory.


   
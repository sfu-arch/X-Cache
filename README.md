# muIR - Library

[![CircleCI](https://circleci.com/gh/sfu-arch/muir-lib.svg?style=svg)](https://circleci.com/gh/sfu-arch/muir-lib)
[![](https://tokei.rs/b1/github/sfu-arch/muir-lib)](https://github.com/sfu-arch/muir-lib)


muIR is a library of hardware components for auto generating highly configurable parallel dataflow accelerator.
muIR provides the implementation of the following hardware units:

1. A set of highly configurable and parameterizable computation nodes.
2. A set of control units to support arbitrary control path.
3. A collection configurable Memory structures like Cache, Scratchpad memory, and such.
4. A set of standard flexible set of junctions and interfaces to connect different pieces of the design.


## Getting Started on a Local Ubuntu Machine

This quick start guide will walk you through installation of Chisel and necessary dependencies:

* **[sbt:](https://www.scala-sbt.org/)** which is the preferred Scala build system and what Chisel uses.

* **[Verilator:](https://www.veripool.org/wiki/verilator)**, which compiles Verilog down to C++ for simulation. The included unit testing infrastructure uses this.


## (Ubuntu-like) Linux

Install Java

```
sudo apt-get install default-jdk
```

Install sbt, which isn't available by default in the system package manager:

```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt
```

## Install Verilator.

We currently recommend Verilator version 4.016.

Follow these instructions to compile it from source.

1. Install prerequisites (if not installed already):

    ```bash
    sudo apt-get install git make autoconf g++ flex bison libfl2 libfl-dev zlibc zlib1g zlib1g-dev
    ```

2. Clone the Verilator repository:

    ```bash
    git clone http://git.veripool.org/git/verilator
    ```

3. In the Verilator repository directory, check out a known good version:

    ```bash
    git pull
    git checkout verilator_4_016
    ```

4. In the Verilator repository directory, build and install:

    ```bash
    unset VERILATOR_ROOT # For bash, unsetenv for csh
    autoconf # Create ./configure script
    ./configure
    make
    sudo make install
    ```
**Please remember that verialtor should be installed in the default system path, otherwise, chisel-iotesters won't find Verilator and the simulation can not be executed**

## Recursive cloning



``` git clone --recurse-submodules
```

## Defining Routines, Actions and Transitions

For defining the coroutine execution model of the targeted DSA, the next few steps should be taken:

1. Defining events:
```
src/main/scala/memory/cache/events.scala
```

2. Defining states:
```
src/main/scala/memory/cache/States.scala
```

3. Defining Routines:
```
src/main/scala/memory/cache/Routine.scala
```

4. Defining the mapping between (event,state) and the executed Routine:
```
src/main/scala/memory/cache/nextRoutine.scala
```

## Running:

Recursively clone:
 ```
 https://github.com/sfu-arch/muir-sim/tree/memGen
 ```

 In the ```build.sh``` script, change the cofigurations and build the hardware.

 Then in the ```test.sh```, change the configurations same as the previous script and run the scripts as below.

 ```
 ./test.sh [TRACE_FILE]
 ```
 










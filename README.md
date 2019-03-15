# Dandelion-lib

[![pipeline status](https://csil-git1.cs.surrey.sfu.ca/Dandelion/dandelion-lib/badges/master/pipeline.svg)](https://csil-git1.cs.surrey.sfu.ca/Dandelion/dandelion-lib/commits/master)


Dandelion is a library of hardware components **High Level Synthesis** tools.

## Getting Started on a Local Ubuntu Machine

This will walk you through installing Chisel and necessary dependencies:

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

Install Verilator. We currently recommend Verilator version 3.922. Follow these instructions to compile it from source.

Install prerequisites (if not installed already):

```
sudo apt-get install git make autoconf g++ flex bison
```

Clone the Verilator repository:

```
git clone http://git.veripool.org/git/verilator
```
In the Verilator repository directory, check out a known good version:

```
git pull
git checkout verilator_3_922
```

In the Verilator repository directory, build and install:

unset VERILATOR_ROOT # For bash, unsetenv for csh
```
autoconf # Create ./configure script
./configure
make
sudo make install
```


## Dandelion's dependencies
Dandelion depends on _Berkeley Hardware Floating-Point Units_ for floating nodes. Therefore, before building dandelion you need to clone hardfloat project, build it and then publish it locally on your system. Hardfloat repository has all the necessary information about how to build the project, here we only breifly mention how to build it and then publish it.

```
git clone git@csil-git1.cs.surrey.sfu.ca:Dandelion/dandelion-hardfloat-chisel3.git
cd dandelion-hardfloat-chisel3
sbt "+publishLocal"
```

## Compiling Dandelion Accelerator
We have seperated our harness test cases with the actuall dandelion library implementation. Therefore, for testing or adding your new HLS circuit you need to follow instructions within `dandelion-test` project.

Here we explain how to build dandelion-lib project, so you can use it in another projects. For publishing dandelion-lib you run the following commands:

```shell
git clone git@csil-git1.cs.surrey.sfu.ca:Dandelion/dandelion-lib.git
cd dandelion-lib
sbt "+publishLocal"
```

The following commands will build dandelion-lib, and then push the compiled version of dandelion-lib to `.ivy2/local` directory. Since, we are still developing dandelion our dandelion versioning finishes with SNAPSHOT keyword.
The version number you select must end with **SNAPSHOT**, or you must change the version number each time you publish.
Ivy maintains a cache, and it stores even local projects in that cache.
If Ivy already has a version cached, it will not check the local repository for updates, unless the version number matches a changing pattern, and SNAPSHOT is one such pattern.

And then for adding dandelion-lib as a dependency to other projects you can add the following line to the build.sbt file:
```scala
libraryDependencies += "edu.sfu.arch" %% "dandelion-lib" % "0.1-SNAPSHOT"
```

# Last status

[info] Run completed in 3 minutes, 17 seconds.
[info] Total number of tests run: 112
[info] Suites: completed 114, aborted 1
[info] Tests: succeeded 59, failed 53, canceled 0, ignored 0, pending 0
[info] *** 1 SUITE ABORTED ***
[info] *** 53 TESTS FAILED ***
[error] Error: Total 113, Failed 53, Errors 1, Passed 59
[error] Failed tests:
[error] 	dataflow.fuse.ComputeF02STests
[error] 	dataflow.cilk_saxpyTester1
[error] 	dataflow.cilk_for_test07Tester1
[error] 	memory.ReadTypMemoryControllerTester
[error] 	dataflow.graph.Compute03Tests
[error] 	dataflow.fuse.ComputeF04PTests
[error] 	dataflow.cilk_for_test06Tester1
[error] 	dataflow.cilk_for_test12Tester1
[error] 	dataflow.test13Tester
[error] 	accel.CacheTests
[error] 	dataflow.DecoupledTaskTests
[error] 	dataflow.test14Tester
[error] 	dataflow.cilk_for_test07Tester2
[error] 	dataflow.cilk_for_test08Tester2
[error] 	dataflow.fibTester1
[error] 	dataflow.fuse.ComputeF03CTests
[error] 	node.TypReduceCompTester
[error] 	dataflow.TypeMemDataflowTests
[error] 	dataflow.cilk_for_test09Tester2
[error] 	dataflow.gemmTester1
[error] 	dataflow.cilk_for_test04Tester1
[error] 	dataflow.test16Tester
[error] 	loop.LoopHeadTester
[error] 	dataflow.bgemmTester1
[error] 	dataflow.vector_scaleTester1
[error] 	dataflow.stencilTester1
[error] 	dataflow.cilk_for_test08Tester1
[error] 	dataflow.graph.Compute04Tests
[error] 	dataflow.mataddTester1
[error] 	node.FusedCompTests
[error] 	dataflow.TypeLoadDataflowTests
[error] 	dataflow.cilk_for_test01Tester1
[error] 	dataflow.mergesortTester1
[error] 	memory.UnifiedControllerTester
[error] 	FPU.FPResizeTests
[error] 	dataflow.cilk_for_test02Tester2
[error] 	dataflow.Sample01Tests
[error] 	memory.TypeStackFileTester
[error] 	dataflow.dedupTester1
[error] 	node.TypLoadTester
[error] 	dataflow.cilk_spawn_test02Tester
[error] 	dataflow.test20Tester1
[error] 	arbiters.ArbiterTests
[error] 	dataflow.UnTypMemDataflowTests
[error] 	dataflow.bgemmTester2
[error] 	dataflow.cilk_for_test05Tester1
[error] 	dataflow.cilk_spawn_test01Tester
[error] 	node.TypCompTester
[error] 	dataflow.cilk_for_test02Tester1
[error] 	node.TypStoreTester
[error] 	junctions.CombineDecoupledTests
[error] 	dataflow.covarianceTester
[error] Error during tests:
[error] 	dataflow.fuse.ComputeF01Tests

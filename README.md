Chisel Project Template
=======================

### List of available Nodes:

1.  Decoupled node (All the ALU)
2.  Alloca
3.  Branch
4.  GEP
5.  PHI
6.  ST
7.  LD
8.  Relay


### List of nodes need to build:

1.  CMP
2.  control/BasicBlock


### To Run individual test cases
```sh
sbt 
```
```sh
sbt "test-only <PACKAGE_NAME>.<TesterName>"
```


### To check the code's style

```
sbt scalastyle
```


```sh
sbt test
```
You should see a whole bunch of output that ends with something like the following lines

```
[info] [0.007] Elaborating design...
[info] [0.390] Done elaborating.
[info] [0.000] Elaborating design...
[info] [0.028] Done elaborating.
End of dependency graph
Circuit state created
SEED 1493571261933
io.out.bits.c: 44456, io.out.bits.valid: 0 state: 0 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 2 should be 4
io.out.bits.c: 4, io.out.bits.valid: 1 state: 0 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
io.out.bits.c: 4, io.out.bits.valid: 0 state: 1 should be 4
test DecoupledAdder Success: 0 tests passed in 26 cycles taking 0.164056 seconds
RAN 21 CYCLES PASSED
[info] DecoupledAdderTester:
[info] DecoupledAdderSpec
[info] - should compute gcd excellently
[info] ScalaCheck
[info] Passed: Total 0, Failed 0, Errors 0, Passed 0
[info] ScalaTest
[info] Run completed in 4 seconds, 271 milliseconds.
[info] Total number of tests run: 1
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```
If you see the above then...
### It worked!
You are ready to go. We have a few recommended practices and things to do.
* Use packages and following conventions for [structure](http://www.scala-sbt.org/0.13/docs/Directories.html) and [naming](http://docs.scala-lang.org/style/naming-conventions.html)
* Package names should be clearly reflected in the testing hierarchy
* Build tests for all your work.
 * This template includes a dependency on the Chisel3 IOTesters, this is a reasonable starting point for most tests
 * You can remove this dependency in the build.sbt file if necessary
* Change the name of your project in the build.sbt
* Change your README.md

## Development/Bug Fixes
This is the release version of chisel-template. If you have bug fixes or
changes you would like to see incorporated in this repo, please checkout
the master branch and submit pull requests against it.






<img align="right" src="https://www.dropbox.com/s/uqprojaqmqzcgym/dandy_lion_by_mutated.png?raw=1"  width="256">

__Simulation Tools__

* [Verilator](https://www.veripool.org/wiki/verilator)
* [GTKWave](http://gtkwave.sourceforge.net/)

Chisel currently uses the Verilator tool (free) to simulate the design.  It supports basic print functionality during simulation runs which can help debug and trace the more straight forward problems.  To solve larger problems where there is a great deal of activity taking place in parallel, a waveform viewer is useful. The GTKWave tool can display the `vcd` waveform file produced by a standard Chisel/Verilator simulation.  It is available as a package for most linux distros.  It can also be download for most OS at the [GTKWave](http://gtkwave.sourceforge.net/) website.

__Simulation__

Chisel doesn't seem to support much beyond it's 'Peek Poke' tester model at this point.  It may be worthwhile investigating if there are more sophisticated test models possible, perhaps using the Synopsys VCS simulator tool (not free) which seems to be the only other simulation tool that Chisel supports.  

There are numerous tutorials and discussions around on simulation and debugging approaches.  Describing general techniques to simulate and debug an RTL design is well beyond the scope of this wiki page, but all the usual rules apply to Chisel generated designs. [Doulos](https://www.doulos.com/knowhow/fpga/debugging/) lists some high level golden rules. 

For our purposes, the two main things to remember are:
1. **Use assertions.** We don't use this enough currently.  Have the dandylion-lib library logic check itself for any behaviour that is known to be "out of bounds".
2. **Write a self checking test bench.**  The test bench should always check that the **intended input produces the expected output**.  This can be a simple check against a known result, or a check against a 'golden file' if there are lots of results.  A more powerful approach is to check the DUT ("Device Under Test") results against results produced by a behavioral model (presumably written in Scala). The latter approach is preferred as it allows the test input to be varied and even randomized.

When debugging new library elements or simple designs, using print statements, assertions, and peeking at the odd waveform is often sufficient to isolate and correct faults.  However, when debugging a larger generated design, it can be difficult to determine *what's happening when* from a huge log file dump.  Typically the main fault found with generator designs is that they just **hang** and issue a timeout in the test bench.  In such cases, it is best to isolate the `ready` and `valid` strobes in a waveform trace to see what has stopped first.  Usually this is indicated by a `ready` flag getting stuck high and all signals pretty much going static after that.  A good place to start is to observe the `ready` flags that serve as enables to each basic block. This gives a top level indication of which code blocks are active and when.  The GTKWave Tips below give some hints on how to filter the signals for this purpose.

__GTKWave Tips__

The problem with looking at waveform traces is often one of *too much* information rather than too little.  The VCD waveform produced by Chisel simulations contains traces of all logic in a design which can be a bit overwhelming in larger designs.  However, GKTWave organizes the signals in a hierarchy by design block which helps.  Additionally, you can use `Insert Comment` (right click the signal pane) to `visually divide` signal waveforms into groups (see image).  It also has a really useful `regular expression` search to filter available signals down to the ones you actually want to see.  This can be really handy to cut down the noise and just extract the more useful signals like the ready/valid handshaking.

<img align="right" src="https://www.dropbox.com/s/2y67tnrtgo1egnr/GTKWave.png?raw=1">

Here are a few regular expression examples that may be helpful:

|Regexp               |Use                                                |
|---------------------|---------------------------------------------------|
|`.*valid$`           | Matches any signal ending in 'valid'              |
|`.*ready$`           | Matches any signal ending in 'ready'              |
|`.*[d,y]$`           | Matches any signal ending in 'd' or 'y' (usually ready,valid)|
|`.*predicate.*valid$`| Matches input predicates (enable) to basic blocks |
|---------------------|---------------------------------------------------|


**Test Fitting a Design on an FPGA**

Several scripts have been created to simplify synthesizing a XXX generated Chisel design for an Intel FPGA Cyclone V or Arria 10 chip. The scripts largely automate the following tasks:

1. Creating a Quartus project directory for the correct device family
2. Creating valid project file customized to the target RTL
3. Invoking the Quartus build process to fit the design to the target chip as an isolated design block (i.e. without connecting the design I/O to the device I/O).
4. Extracting summary data like maximum frequency and device resources.

The [scripts]() are checked in as part of the XXX-lib project.

**Required Tools**
1. Working copy of Chisel, Scala, sbt, the usual
2. Intel Quartus Prime software (v16.0 or greater)

*Note:* Quartus Prime is installed on the nml servers as a module under the name "altera". Load it by:
```
module load altera
```
**Design Requirements**

The scripts look for a scala class that describes a 'top' module to instantiate the dandylion generated function.  At the time of writing, the generator tools don't create this top level class automatically.  Typically this class would instantiate one or more generated functions and connect them to any memories and memory arbiters required.  The class may look very similar to ones created for test bench files.  The code below illustrates the cilk_saxpyTopIO and cilk_saxpyTop classes created for the saxpy benchmarks (shortened for brevity). The class should have the same name as the main design (e.g. cilk_saxpy becomes cilk_saxpyTop).

```scala
class cilk_saxpyTopIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_saxpyTop(children :Int)(implicit p: Parameters) extends cilk_saxpyTopIO  {

  // Wire up the cache, TM, and modules under test.
  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(), 1, children))
  val saxpy = Module(new cilk_saxpyDF())

  // Connect saxpy block to I/O
  saxpy.io.in <> io.in

  // saxpy block to Top
  io.out <> saxpy.io.out

  // Instantiate arbiters, memories, etc. here ...
  // ...

}
```

Additionally, the App class needs to be modified to instantiate the new *<design_name>Top* class. It should output the verilog file into a directory called *RTL/design_nameTop*.  An example from the cilk_saxpy benchmark is below.

```scala
import java.io.{File, FileWriter}
object cilk_saxpyMain extends App {
  val dir = new File("RTL/cilk_saxpyTop") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val testParams = p.alterPartial({
    case TLEN => 6
    case TRACE => false
  })
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_saxpyTop(3)(testParams)))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
```

**Running the Scripts**

The following commands should build the RTL for the `stencil` and `dedup` benchmark code.  The default compile_all.sh  compiles those designs.
```
cd <XXX repo>/XXX-lib
sbt "runMain dataflow.stencilMain"
sbt "runMain dataflow.dedupMain"
cd RTL
source ./compile_all.sh
```

The [compile_all.sh]() script calls the [make_quartus_prj.sh]() to build the needed Quartus project directories.  The **compile_all.sh** script should be edited to build the desired designs. This can be done by modifying the `APPS` variable.  The target device can be modified by modifying the `FAMILY` and `PART` variables.

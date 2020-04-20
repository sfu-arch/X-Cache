package test

import chisel3._
import chisel3.MultiIOModule
import dandelion.shell._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.generator._
import dandelion.accel.{DandelionAccelDCRModule, DandelionAccelDebugModule, DandelionAccelModule}
import sim.shell._
import dandelion.shell._

class DandelionSimDebugAccel(accelModule: () => DandelionAccelDCRModule, debugModule: () => DandelionAccelDebugModule)
                            (numPtrs: Int, numDbgs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                            (implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val sim_shell = Module(new AXISimShell)
  val shell = Module(new DandelionDebugShell(accelModule)(debugModule)
  (numPtrs = numPtrs, numDbgs = numDbgs, numVals = numVals,
    numRets = numRets, numEvents = numEvents, numCtrls = numCtrls))

  sim_shell.sim_clock := sim_clock
  sim_wait := sim_shell.sim_wait

  /**
   * @TODO: This is a bug from chisel otherwise, bulk connection should work here
   */
  sim_shell.mem.ar <> shell.io.mem.ar
  sim_shell.mem.aw <> shell.io.mem.aw
  sim_shell.mem.w <> shell.io.mem.w
  shell.io.mem.b <> sim_shell.mem.b
  shell.io.mem.r <> sim_shell.mem.r

  sim_shell.host.b <> shell.io.host.b
  sim_shell.host.r <> shell.io.host.r
  shell.io.host.ar <> sim_shell.host.ar
  shell.io.host.aw <> sim_shell.host.aw
  shell.io.host.w <> sim_shell.host.w
}


class DandelionSimDCRAccel(accelModule: () => DandelionAccelDCRModule)
                          (numPtrs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                          (implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val sim_shell = Module(new AXISimShell)
  val shell = Module(new DandelionDCRCacheShell(accelModule)(numPtrs = numPtrs, numVals = numVals, numRets = numRets, numEvents = numEvents, numCtrls = numCtrls))

  sim_shell.sim_clock := sim_clock
  sim_wait := sim_shell.sim_wait

  /**
   * @TODO: This is a bug from chisel otherwise, bulk connection should work here
   */
  sim_shell.mem.ar <> shell.io.mem.ar
  sim_shell.mem.aw <> shell.io.mem.aw
  sim_shell.mem.w <> shell.io.mem.w
  shell.io.mem.b <> sim_shell.mem.b
  shell.io.mem.r <> sim_shell.mem.r

  sim_shell.host.b <> shell.io.host.b
  sim_shell.host.r <> shell.io.host.r
  shell.io.host.ar <> sim_shell.host.ar
  shell.io.host.aw <> sim_shell.host.aw
  shell.io.host.w <> sim_shell.host.w
}


/**
 * Main object for compatible DCR accelerator with Dandelion generator
 *
 * @TODO: The option manager needs to be added instead of parsing input arguments
 *        this is not a clean way to pass arguments
 */
object DandelionSimDCRAccelMain extends App {

  //These are default values for DCR
  var num_ptrs = 0
  var num_vals = 0
  var num_returns = 1
  var num_events = 1
  var num_ctrls = 1

  /**
   * Make sure accel name is added to TestDCRAccel class
   */
  var accel_name = "test09"

  /**
   * Accel config values
   */
  var data_len = 64
  var print_log = true
  var cache_log = false

  args.sliding(2, 2).toList.collect {
    case Array("--accel-name", argAccel: String) => accel_name = argAccel
    case Array("--num-ptrs", argPtrs: String) => num_ptrs = argPtrs.toInt
    case Array("--num-vals", argVals: String) => num_vals = argVals.toInt
    case Array("--num-rets", argRets: String) => num_returns = argRets.toInt
    case Array("--num-events", argEvent: String) => num_events = argEvent.toInt
    case Array("--num-ctrls", argCtrl: String) => num_ctrls = argCtrl.toInt
    case Array("--data-len", dlen: String) => data_len = dlen.toInt
    case Array("--print-log", printLog: String) => print_log = printLog.toBoolean
    case Array("--cache-log", cacheLog: String) => cache_log = cacheLog.toBoolean
  }

  /**
   * @note make sure for simulation dataLen is equal to 64
   *       Pass generated accelerator to TestAccel
   */
  implicit val p =
    new WithSimShellConfig(dLen = data_len, pLog = print_log, cLog = cache_log)(
      nPtrs = num_ptrs, nVals = num_vals, nRets = num_returns, nEvents = num_events, nCtrls = num_ctrls)
  chisel3.Driver.execute(args.take(4),
    () => new DandelionSimDCRAccel(() => DandelionTestDCRAccel(accel_name))(
      numPtrs = num_ptrs, numVals = num_vals, numRets = num_returns, numEvents = num_events, numCtrls = num_ctrls))
}


/**
 * Main object for compatible DCR accelerator with Dandelion generator
 *
 * @TODO: The option manager needs to be added instead of parsing input arguments
 *        this is not a clean way to pass arguments
 */
object DandelionSimDebugAccelMain extends App {

  //These are default values for DCR
  var num_ptrs = 0
  var num_dbgs = 1
  var num_vals = 0
  var num_returns = 1
  var num_events = 1
  var num_ctrls = 1

  /**
   * Make sure accel name is added to TestDCRAccel class
   */
  var accel_name = "test09"

  /**
   * Accel config values
   */
  var data_len = 64
  var print_log = true
  var cache_log = false
  var bore_ids = List[Int]()

  args.sliding(2, 2).toList.collect {
    case Array("--accel-name", argAccel: String) => accel_name = argAccel
    case Array("--num-ptrs", argPtrs: String) => num_ptrs = argPtrs.toInt
    case Array("--num-dbgs", argDbgs: String) => num_dbgs = argDbgs.toInt
    case Array("--num-vals", argVals: String) => num_vals = argVals.toInt
    case Array("--num-rets", argRets: String) => num_returns = argRets.toInt
    case Array("--num-events", argEvent: String) => num_events = argEvent.toInt
    case Array("--num-ctrls", argCtrl: String) => num_ctrls = argCtrl.toInt
    case Array("--data-len", dlen: String) => data_len = dlen.toInt
    case Array("--print-log", printLog: String) => print_log = printLog.toBoolean
    case Array("--cache-log", cacheLog: String) => cache_log = cacheLog.toBoolean
    case Array("--bore-ids", boreids: String) => bore_ids = boreids.split(',').map(_.toInt).toList
  }

  /**
   * @note make sure for simulation dataLen is equal to 64
   *       Pass generated accelerator to TestAccel
   */
  implicit val p =
    new WithDebugSimShellConfig(dLen = data_len, pLog = print_log, cLog = cache_log)(
      nPtrs = num_ptrs, nVals = num_vals, nRets = num_returns, nEvents = num_events, nCtrls = num_ctrls, nDbgs = num_dbgs)

  lazy val accel_module = DandelionTestDebugDCRAccel(accel_name, num_dbgs, bore_ids)
  chisel3.Driver.execute(args.take(4) ++ List("--full-stacktrace"),
    () => new DandelionSimDebugAccel(accel_module._1, accel_module._2)
    (numPtrs = num_ptrs, numDbgs = num_dbgs, numVals = num_vals, numRets = num_returns, numEvents = num_events, numCtrls = num_ctrls))

}


/**
 * This is the F1 accel shell to run on AWS
 * The difference between F1 shell and the other shells is AXILite interface
 * For F1 we have used a custom interface instead of AXI lite and the memory
 * is mapped to sepecific addresses.
 * For more details please read Dandelion-Tutorial
 */
object DandelionF1AccelMain extends App {

  //These are default values for VCR
  var num_ptrs = 0
  var num_vals = 0
  var num_returns = 1
  var num_events = 1
  var num_ctrls = 1
  args.sliding(2, 2).toList.collect {
    case Array("--num-ptrs", argPtrs: String) => num_ptrs = argPtrs.toInt
    case Array("--num-vals", argVals: String) => num_vals = argVals.toInt
    case Array("--num-rets", argRets: String) => num_returns = argRets.toInt
    case Array("--num-events", argEvent: String) => num_events = argEvent.toInt
    case Array("--num-ctrls", argCtrl: String) => num_ctrls = argCtrl.toInt
  }


  /**
   * @note make sure for simulation dataLen is equal to 64
   *       Pass generated accelerator to TestAccel
   */
  implicit val p =
    new WithF1ShellConfig(dLen = 64, pLog = true)(nPtrs = num_ptrs, nVals = num_vals, nRets = num_returns, nEvents = num_events, nCtrls = num_ctrls)
  chisel3.Driver.execute(args.take(4),
    () => new DandelionF1Accel(() => new test05DF())(nPtrs = num_ptrs, nVals = num_vals, numRets = num_returns, numEvents = num_events, numCtrls = num_ctrls))

}


/**
 * Getting verilog file for SpAccel
 */

import java.io.{File, FileWriter}

object DCRAccelMain extends App {

  //These are default values for DCR
  var num_ptrs = 0
  var num_dbgs = 1
  var num_vals = 0
  var num_returns = 1
  var num_events = 1
  var num_ctrls = 1

  /**
   * Make sure accel name is added to TestDCRAccel class
   */
  var accel_name = "test09"

  /**
   * Accel config values
   */
  var data_len = 64
  var print_log = true
  var cache_log = false
  var bore_ids = List[Int]()

  args.sliding(2, 2).toList.collect {
    case Array("--accel-name", argAccel: String) => accel_name = argAccel
    case Array("--num-ptrs", argPtrs: String) => num_ptrs = argPtrs.toInt
    case Array("--num-dbgs", argDbgs: String) => num_dbgs = argDbgs.toInt
    case Array("--num-vals", argVals: String) => num_vals = argVals.toInt
    case Array("--num-rets", argRets: String) => num_returns = argRets.toInt
    case Array("--num-events", argEvent: String) => num_events = argEvent.toInt
    case Array("--num-ctrls", argCtrl: String) => num_ctrls = argCtrl.toInt
    case Array("--data-len", dlen: String) => data_len = dlen.toInt
    case Array("--print-log", printLog: String) => print_log = printLog.toBoolean
    case Array("--cache-log", cacheLog: String) => cache_log = cacheLog.toBoolean
    case Array("--bore-ids", boreids: String) => bore_ids = boreids.split(',').map(_.toInt).toList
  }

  val dir = new File("RTL/DCRAccel");
  dir.mkdirs
  /**
   * @note make sure for simulation dataLen is equal to 64
   *       Pass generated accelerator to TestAccel
   */
  implicit val p =
    new WithDe10ShellConfig(dLen = data_len, pLog = print_log)(
      nPtrs = num_ptrs, nVals = num_vals, nRets = num_returns, nEvents = num_events, nCtrls = num_ctrls, nDbgs = num_dbgs)

  lazy val accel_module = DandelionTestDebugDCRAccel(accel_name, num_dbgs, bore_ids)

  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(
    () => new DandelionDebugFPGAShell(accel_module._1)(accel_module._2)
    (numPtrs = num_ptrs, numDbgs = num_dbgs, numVals = num_vals, numRets = num_returns, numEvents = num_events, numCtrls = num_ctrls)))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

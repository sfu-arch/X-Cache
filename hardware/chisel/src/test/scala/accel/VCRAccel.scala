package test

import chisel3._
import chisel3.MultiIOModule
import dandelion.shell._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.generator._
import dandelion.accel.DandelionAccelModule
import sim.shell._
import vta.shell.DandelionF1Accel

/** Test. This generates a testbench file for simulation */
class DandelionSimAccel[T <: DandelionAccelModule](accelModule: () => T)
                                                  (numPtrs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                                                  (implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val sim_shell = Module(new AXISimShell)
  //val vta_shell = Module(new DandelionVTAShell())
  val vta_shell = Module(new DandelionCacheShell(accelModule)(numPtrs, numVals, numRets, numEvents, numCtrls))

  sim_shell.mem <> DontCare
  sim_shell.host <> DontCare
  sim_shell.sim_clock := sim_clock
  sim_wait := sim_shell.sim_wait

  /**
   * @TODO: This is a bug from chisel otherwise, bulk connection should work here
   */
  //  sim_shell.mem <> vta_shell.io.mem
  sim_shell.mem.ar <> vta_shell.io.mem.ar
  sim_shell.mem.aw <> vta_shell.io.mem.aw
  sim_shell.mem.w <> vta_shell.io.mem.w
  vta_shell.io.mem.b <> sim_shell.mem.b
  vta_shell.io.mem.r <> sim_shell.mem.r

  sim_shell.host.b <> vta_shell.io.host.b
  sim_shell.host.r <> vta_shell.io.host.r
  vta_shell.io.host.ar <> sim_shell.host.ar
  vta_shell.io.host.aw <> sim_shell.host.aw
  vta_shell.io.host.w <> sim_shell.host.w
}

/**
 * @todo Find the solution to define a new config here instead of dandelion library
 */


object DandelionSimAccelMain extends App {

  //These are default values for VCR
  var num_ptrs = 4
  var num_vals = 2
  var num_returns = 1
  var num_events = 1
  var num_ctrl = 1
  args.sliding(2, 2).toList.collect {
    case Array("--num-ptrs", argPtrs: String) => num_ptrs = argPtrs.toInt
    case Array("--num-vals", argVals: String) => num_vals = argVals.toInt
    case Array("--num-rets", argVals: String) => num_returns = argVals.toInt
    case Array("--num-event", argEvent: String) => num_vals = argEvent.toInt
    case Array("--num-ctrl", argCtrl: String) => num_vals = argCtrl.toInt
  }

  /**
   * @note make sure for simulation dataLen is equal to 64
   *       Pass generated accelerator to TestAccel
   */
  implicit val p =
    new WithSimShellConfig(dLen = 64, pLog = true)(nPtrs = num_ptrs, nVals = num_vals, nRets = num_returns, nEvent = num_events, nCtrl =  num_ctrl)
  chisel3.Driver.execute(args.take(4),
    () => new DandelionSimAccel(() => new test09DF())(num_ptrs, num_vals, num_returns, num_events, num_ctrl))
}


object DandelionF1AccelMain extends App {

  //These are default values for VCR
  var num_ptrs = 4
  var num_vals = 2
  var num_returns = 0
  var num_events = 1
  var num_ctrl = 1
  args.sliding(2, 2).toList.collect {
    case Array("--num-ptrs", argPtrs: String) => num_ptrs = argPtrs.toInt
    case Array("--num-vals", argVals: String) => num_vals = argVals.toInt
    case Array("--num-rets", argVals: String) => num_returns = argVals.toInt
    case Array("--num-event", argEvent: String) => num_vals = argEvent.toInt
    case Array("--num-ctrl", argCtrl: String) => num_vals = argCtrl.toInt
  }

  /**
    * @note make sure for simulation dataLen is equal to 64
    *       Pass generated accelerator to TestAccel
    */
  implicit val p =
    new WithF1ShellConfig(dLen = 64, pLog = true)(nPtrs = num_ptrs, nVals = num_vals, nRets = num_returns, nEvent = num_events, nCtrl =  num_ctrl)
  chisel3.Driver.execute(args.take(4),
    () => new DandelionF1Accel(() => new test05DF())(num_ptrs, num_vals, num_returns, num_events, num_ctrl))
}


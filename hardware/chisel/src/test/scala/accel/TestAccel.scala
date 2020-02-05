package test

import chisel3._
import chisel3.MultiIOModule
import dandelion.dpi._
import accel._

/** VTA simulation shell.
  *
  * Instantiate Host and Memory DPI modules.
  *
  */
class VTASimShell extends MultiIOModule {
  val host = IO(new VTAHostDPIMaster)
  val mem = IO(new VTAMemDPIClient)
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val mod_sim = Module(new VTASimDPI)
  val mod_host = Module(new VTAHostDPI)
  val mod_mem = Module(new VTAMemDPI)
  mod_mem.io.clock := clock
  mod_mem.io.reset := reset
  mod_mem.io.dpi <> mem
  mod_host.io.clock := clock
  mod_host.io.reset := reset
  host <> mod_host.io.dpi
  mod_sim.io.clock := sim_clock
  mod_sim.io.reset := reset
  sim_wait := mod_sim.io.dpi_wait
}

/** Test accelerator.
  *
  * Instantiate and connect the simulation-shell and the accelerator.
  *
  */
class TestAccel extends MultiIOModule {
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val sim_shell = Module(new VTASimShell)
  val vta_accel = Module(new Accel)
  sim_shell.sim_clock := sim_clock
  sim_wait := sim_shell.sim_wait
  sim_shell.mem <> vta_accel.io.mem
  vta_accel.io.host <> sim_shell.host
}

/** Generate TestAccel as top module */
object Elaborate extends App {
  chisel3.Driver.execute(args, () => new TestAccel)
}

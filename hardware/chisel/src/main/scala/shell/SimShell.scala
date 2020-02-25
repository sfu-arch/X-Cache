package dandelion.shell

import chisel3._
import chisel3.MultiIOModule
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces.axi._
import dandelion.dpi._

/** VTAHost.
  *
  * This module translate the DPI protocol into AXI. This is a simulation only
  * module and used to test host-to-VTA communication. This module should be updated
  * for testing hosts using a different bus protocol, other than AXI.
  */
class VTAHost(implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val axi = new AXILiteMaster(hostParams)
  })
  val host_dpi = Module(new VTAHostDPI)
  val host_axi = Module(new VTAHostDPIToAXI)
  host_dpi.io.reset := reset
  host_dpi.io.clock := clock
  host_axi.io.dpi <> host_dpi.io.dpi
  io.axi <> host_axi.io.axi
}

/** VTAMem.
  *
  * This module translate the DPI protocol into AXI. This is a simulation only
  * module and used to test VTA-to-memory communication. This module should be updated
  * for testing memories using a different bus protocol, other than AXI.
  */
class VTAMem(implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val axi = new AXIClient(memParams)
  })
  val mem_dpi = Module(new VTAMemDPI)
  val mem_axi = Module(new VTAMemDPIToAXI)
  mem_dpi.io.reset := reset
  mem_dpi.io.clock := clock
  mem_dpi.io.dpi <> mem_axi.io.dpi
  io.axi <> mem_axi.io.axi
}

/** VTASim.
  *
  * This module is used to handle hardware simulation thread, such as halting
  * or terminating the simulation thread. The sim_wait port is used to halt
  * the simulation thread when it is asserted and resume it when it is
  * de-asserted.
  */
class VTASim(implicit val p: Parameters) extends MultiIOModule {
  val sim_wait = IO(Output(Bool()))
  val sim = Module(new VTASimDPI)
  sim.io.reset := reset
  sim.io.clock := clock
  sim_wait := sim.io.dpi_wait
}

/** SimShell.
  *
  * The simulation shell instantiate the sim, host and memory DPI modules that
  * are connected to the VTAShell. An extra clock, sim_clock, is used to eval
  * the VTASim DPI function when the main simulation clock is on halt state.
  */
class SimShell(implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val mem = IO(new AXIClient(memParams))
  val host = IO(new AXILiteMaster(hostParams))
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val mod_sim = Module(new VTASim)
  val mod_host = Module(new VTAHost)
  val mod_mem = Module(new VTAMem)
  mem <> mod_mem.io.axi
  host <> mod_host.io.axi
  mod_sim.reset := reset
  mod_sim.clock := sim_clock
  sim_wait := mod_sim.sim_wait
}

/** SimShell.
  *
  * The simulation shell instantiate the sim, host and memory DPI modules that
  * are connected to the VTAShell. An extra clock, sim_clock, is used to eval
  * the VTASim DPI function when the main simulation clock is on halt state.
  */
class AXISimShell(implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val mem = IO(new AXIClient(memParams))
  val host = IO(new AXILiteMaster(hostParams))
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val mod_sim = Module(new VTASim)
  val mod_host = Module(new VTAHost)
  val mod_mem = Module(new VTAMem)
  mem <> mod_mem.io.axi
  host <> mod_host.io.axi
  mod_sim.reset := reset
  mod_sim.clock := sim_clock
  sim_wait := mod_sim.sim_wait
}

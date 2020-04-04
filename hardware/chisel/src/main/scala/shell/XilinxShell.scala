package dandelion.shell

import chisel3._
import chisel3.{RawModule, withClockAndReset}
import chipsalliance.rocketchip.config._
import dandelion.accel.{DandelionAccelDCRModule, DandelionAccelModule}
import dandelion.config._
import dandelion.interfaces.axi._

/** XilinxShell.
 *
 * This is a wrapper shell mostly used to match Xilinx convention naming,
 * therefore we can pack Dandelion as an IP for IPI based flows.
 */

class DandelionF1Accel[T <: DandelionAccelDCRModule](accelModule: () => T)
                                                 (nPtrs: Int, nVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                                                 (implicit val p: Parameters) extends RawModule with HasAccelShellParams {

  val hp = hostParams
  val mp = memParams

  val ap_clk = IO(Input(Clock()))
  val ap_rst_n = IO(Input(Bool()))
  val cl_axi_mstr_bus = IO(new XilinxAXIMaster(mp))
  val axi_mstr_cfg_bus = IO(new ConfigBusMaster(hp))

  val shell = withClockAndReset(clock = ap_clk, reset = ~ap_rst_n) {
    Module(new DandelionF1DTAShell())
  }

  // memory
  cl_axi_mstr_bus.AWVALID := shell.io.mem.aw.valid
  shell.io.mem.aw.ready := cl_axi_mstr_bus.AWREADY
  cl_axi_mstr_bus.AWADDR := shell.io.mem.aw.bits.addr
  cl_axi_mstr_bus.AWID := shell.io.mem.aw.bits.id
  cl_axi_mstr_bus.AWUSER := shell.io.mem.aw.bits.user
  cl_axi_mstr_bus.AWLEN := shell.io.mem.aw.bits.len
  cl_axi_mstr_bus.AWSIZE := shell.io.mem.aw.bits.size
  cl_axi_mstr_bus.AWBURST := shell.io.mem.aw.bits.burst
  cl_axi_mstr_bus.AWLOCK := shell.io.mem.aw.bits.lock
  cl_axi_mstr_bus.AWCACHE := shell.io.mem.aw.bits.cache
  cl_axi_mstr_bus.AWPROT := shell.io.mem.aw.bits.prot
  cl_axi_mstr_bus.AWQOS := shell.io.mem.aw.bits.qos
  cl_axi_mstr_bus.AWREGION := shell.io.mem.aw.bits.region

  cl_axi_mstr_bus.WVALID := shell.io.mem.w.valid
  shell.io.mem.w.ready := cl_axi_mstr_bus.WREADY
  cl_axi_mstr_bus.WDATA := shell.io.mem.w.bits.data
  cl_axi_mstr_bus.WSTRB := shell.io.mem.w.bits.strb
  cl_axi_mstr_bus.WLAST := shell.io.mem.w.bits.last
  cl_axi_mstr_bus.WID := shell.io.mem.w.bits.id
  cl_axi_mstr_bus.WUSER := shell.io.mem.w.bits.user

  shell.io.mem.b.valid := cl_axi_mstr_bus.BVALID
  cl_axi_mstr_bus.BREADY := shell.io.mem.b.valid
  shell.io.mem.b.bits.resp := cl_axi_mstr_bus.BRESP
  shell.io.mem.b.bits.id := cl_axi_mstr_bus.BID
  shell.io.mem.b.bits.user := cl_axi_mstr_bus.BUSER

  cl_axi_mstr_bus.ARVALID := shell.io.mem.ar.valid
  shell.io.mem.ar.ready := cl_axi_mstr_bus.ARREADY
  cl_axi_mstr_bus.ARADDR := shell.io.mem.ar.bits.addr
  cl_axi_mstr_bus.ARID := shell.io.mem.ar.bits.id
  cl_axi_mstr_bus.ARUSER := shell.io.mem.ar.bits.user
  cl_axi_mstr_bus.ARLEN := shell.io.mem.ar.bits.len
  cl_axi_mstr_bus.ARSIZE := shell.io.mem.ar.bits.size
  cl_axi_mstr_bus.ARBURST := shell.io.mem.ar.bits.burst
  cl_axi_mstr_bus.ARLOCK := shell.io.mem.ar.bits.lock
  cl_axi_mstr_bus.ARCACHE := shell.io.mem.ar.bits.cache
  cl_axi_mstr_bus.ARPROT := shell.io.mem.ar.bits.prot
  cl_axi_mstr_bus.ARQOS := shell.io.mem.ar.bits.qos
  cl_axi_mstr_bus.ARREGION := shell.io.mem.ar.bits.region

  shell.io.mem.r.valid := cl_axi_mstr_bus.RVALID
  cl_axi_mstr_bus.RREADY := shell.io.mem.r.ready
  shell.io.mem.r.bits.data := cl_axi_mstr_bus.RDATA
  shell.io.mem.r.bits.resp := cl_axi_mstr_bus.RRESP
  shell.io.mem.r.bits.last := cl_axi_mstr_bus.RLAST
  shell.io.mem.r.bits.id := cl_axi_mstr_bus.RID
  shell.io.mem.r.bits.user := cl_axi_mstr_bus.RUSER

  // host
  shell.io.host.addr := axi_mstr_cfg_bus.addr
  shell.io.host.wdata := axi_mstr_cfg_bus.wdata
  shell.io.host.wr := axi_mstr_cfg_bus.wr
  shell.io.host.rd := axi_mstr_cfg_bus.rd
  axi_mstr_cfg_bus.ack := shell.io.host.ack
  axi_mstr_cfg_bus.rdata := shell.io.host.rdata

}


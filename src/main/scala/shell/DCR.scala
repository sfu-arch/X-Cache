package dandelion.shell

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces.axi._


/** Register File.
 *
 * Six 32-bit register file.
 *
 * -------------------------------
 * Register description    | addr
 * -------------------------|-----
 * Control status register | 0x00
 * Cycle counter           | 0x04
 * Constant value          | 0x08
 * Vector length           | 0x0c
 * Input pointer lsb       | 0x10
 * Input pointer msb       | 0x14
 * Output pointer lsb      | 0x18
 * Output pointer msb      | 0x1c
 * -------------------------------
 *
 * ------------------------------
 * Control status register | bit
 * ------------------------------
 * Launch                  | 0
 * Finish                  | 1
 * ------------------------------
 */


/** VCRBase. Parametrize base class. */
abstract class DCRBase(implicit p: Parameters) extends DandelionParameterizedBundle()(p)

/** VCRMaster.
 *
 * This is the master interface used by VCR in the DandelionShell to control
 * the Core unit.
 */
class DCRMaster(implicit val p: Parameters) extends DCRBase with HasAccelShellParams {
  val vp = dcrParams
  val mp = memParams
  val launch = Output(Bool())
  val finish = Input(Bool())
  val ecnt = Vec(vp.nECnt, Flipped(ValidIO(UInt(vp.regBits.W))))
  val vals = Output(Vec(vp.nVals, UInt(vp.regBits.W)))
  val ptrs = Output(Vec(vp.nPtrs, UInt(mp.addrBits.W)))
}

/** VCRClient.
 *
 * This is the client interface used by the Core module to communicate
 * to the VCR in the DandelionShell.
 */
class DCRClient(implicit val p: Parameters) extends DCRBase with HasAccelShellParams {
  val vp = dcrParams
  val mp = memParams
  val launch = Input(Bool())
  val finish = Output(Bool())
  val ecnt = Vec(vp.nECnt, ValidIO(UInt(vp.regBits.W)))
  val vals = Input(Vec(vp.nVals, UInt(vp.regBits.W)))
  val ptrs = Input(Vec(vp.nPtrs, UInt(mp.addrBits.W)))
}

/** DTA Control Registers (DCR).
 *
 * This unit provides control registers (32 and 64 bits) to be used by a control'
 * unit, typically a host processor. These registers are read-only by the core
 * at the moment but this will likely change once we add support to general purpose
 * registers that could be used as event counters by the Core unit.
 */
class DCR(implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val dcr = new DCRMaster
  })

  val vp = dcrParams
  val mp = memParams
  val hp = hostParams

  // Write control (AW, W, B)
  val waddr = RegInit("h_ffff".U(hp.addrBits.W)) // init with invalid address
  val wdata = io.host.w.bits.data
  val sWriteAddress :: sWriteData :: sWriteResponse :: Nil = Enum(3)
  val wstate = RegInit(sWriteAddress)

  // read control (AR, R)
  val sReadAddress :: sReadData :: Nil = Enum(2)
  val rstate = RegInit(sReadAddress)
  val rdata = RegInit(0.U(vp.regBits.W))

  // registers
  val nPtrs = if (mp.addrBits == 32) vp.nPtrs else 2 * vp.nPtrs
  val nTotal = vp.nCtrl + vp.nECnt + vp.nVals + nPtrs

  val reg = Seq.fill(nTotal)(RegInit(0.U(vp.regBits.W)))
  val addr = Seq.tabulate(nTotal)(_ * 4)
  val reg_map = (addr zip reg) map { case (a, r) => a.U -> r }
  val eo = vp.nCtrl
  val vo = eo + vp.nECnt
  val po = vo + vp.nVals

  switch(wstate) {
    is(sWriteAddress) {
      when(io.host.aw.valid) {
        wstate := sWriteData
      }
    }
    is(sWriteData) {
      when(io.host.w.valid) {
        wstate := sWriteResponse
      }
    }
    is(sWriteResponse) {
      when(io.host.b.ready) {
        wstate := sWriteAddress
      }
    }
  }

  when(io.host.aw.fire()) {
    waddr := io.host.aw.bits.addr
  }

  io.host.aw.ready := wstate === sWriteAddress
  io.host.w.ready := wstate === sWriteData
  io.host.b.valid := wstate === sWriteResponse
  io.host.b.bits.resp := 0.U

  switch(rstate) {
    is(sReadAddress) {
      when(io.host.ar.valid) {
        rstate := sReadData
      }
    }
    is(sReadData) {
      when(io.host.r.ready) {
        rstate := sReadAddress
      }
    }
  }

  io.host.ar.ready := rstate === sReadAddress
  io.host.r.valid := rstate === sReadData
  io.host.r.bits.data := rdata
  io.host.r.bits.resp := 0.U

  when(io.dcr.finish) {
    reg(0) := "b_10".U
  }.elsewhen(io.host.w.fire() && addr(0).U === waddr) {
    reg(0) := wdata
  }

  for (i <- 0 until vp.nECnt) {
    when(io.dcr.ecnt(i).valid) {
      reg(eo + i) := io.dcr.ecnt(i).bits
    }.elsewhen(io.host.w.fire() && addr(eo + i).U === waddr) {
      reg(eo + i) := wdata
    }
  }

  for (i <- 0 until (vp.nVals + nPtrs)) {
    when(io.host.w.fire() && addr(vo + i).U === waddr) {
      reg(vo + i) := wdata
    }
  }

  when(io.host.ar.fire()) {
    rdata := MuxLookup(io.host.ar.bits.addr, 0.U, reg_map)
  }

  io.dcr.launch := reg(0)(0)

  for (i <- 0 until vp.nVals) {
    io.dcr.vals(i) := reg(vo + i)
  }

  if (mp.addrBits == 32) { // 32-bit pointers
    for (i <- 0 until nPtrs) {
      io.dcr.ptrs(i) := reg(po + i)
    }
  } else { // 64-bits pointers
    for (i <- 0 until (nPtrs / 2)) {
      io.dcr.ptrs(i) := Cat(reg(po + 2 * i + 1), reg(po + 2 * i))
    }
  }
}

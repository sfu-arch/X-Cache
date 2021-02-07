package memGen.shell

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.util._
import memGen.interfaces.axi._


/** DMEBase. Parametrize base class. */
abstract class DMEBase(implicit p: Parameters) extends DandelionParameterizedBundle()(p)

/** DMECmd.
 *
 * This interface is used for creating write and read requests to memory.
 */
class DMECmd(implicit val p: Parameters) extends DMEBase with HasAccelShellParams {
  val addrBits = memParams.addrBits
  val lenBits = memParams.lenBits
  val addr = UInt(addrBits.W)
  val len = UInt(lenBits.W)
}

/** DMEReadMaster.
 *
 * This interface is used by modules inside the core to generate read requests
 * and receive responses from DME.
 */
class DMEReadMaster(implicit val p: Parameters) extends Bundle with HasAccelShellParams {
  val dataBits = memParams.dataBits
  val cmd = Decoupled(new DMECmd)
  val data = Flipped(Decoupled(UInt(dataBits.W)))

  override def cloneType =
    new DMEReadMaster().asInstanceOf[this.type]
}

/** DMEReadClient.
 *
 * This interface is used by the DME to receive read requests and generate
 * responses to modules inside the core.
 */
class DMEReadClient(implicit val p: Parameters) extends Bundle with HasAccelShellParams {
  val dataBits = memParams.dataBits
  val cmd = Flipped(Decoupled(new DMECmd))
  val data = Decoupled(UInt(dataBits.W))

  override def cloneType =
    new DMEReadClient().asInstanceOf[this.type]
}

/** DMEWriteMaster.
 *
 * This interface is used by modules inside the core to generate write requests
 * to the DME.
 */
class DMEWriteMaster(implicit val p: Parameters) extends Bundle with HasAccelShellParams {
  val dataBits = memParams.dataBits
  val cmd = Decoupled(new DMECmd)
  val data = Decoupled(UInt(dataBits.W))
  val ack = Input(Bool())

  override def cloneType =
    new DMEWriteMaster().asInstanceOf[this.type]
}

/** DMEWriteClient.
 *
 * This interface is used by the DME to handle write requests from modules inside
 * the core.
 */
class DMEWriteClient(implicit val p: Parameters) extends Bundle with HasAccelShellParams {
  val dataBits = memParams.dataBits
  val cmd = Flipped(Decoupled(new DMECmd))
  val data = Flipped(Decoupled(UInt(dataBits.W)))
  val ack = Output(Bool())

  override def cloneType =
    new DMEWriteClient().asInstanceOf[this.type]
}

/** DMEMaster.
 *
 * Pack nRd number of DMEReadMaster interfaces and nWr number of DMEWriteMaster
 * interfaces.
 */
class DMEMaster(implicit val p: Parameters) extends Bundle with HasAccelShellParams {
  val rd = new DMEReadMaster
  val wr = new DMEWriteMaster
}

/** DMEMasterVector.
 *
 * Pack nRd number of DMEReadMaster interfaces and nWr number of DMEWriteMaster
 * interfaces.
 */
class DMEMasterVector(implicit val p: Parameters) extends Bundle with HasAccelShellParams {
  val nRd = dmeParams.nReadClients
  val nWr = dmeParams.nWriteClients
  val rd = Vec(nRd, new DMEReadMaster)
  val wr = Vec(nWr, new DMEWriteMaster)
}


/** DMEClient.
 *
 * Pack nRd number of DMEReadClient interfaces and nWr number of DMEWriteClient
 * interfaces.
 */
class DMEClientVector(implicit val p: Parameters) extends Bundle with HasAccelShellParams {
  val nRd = dmeParams.nReadClients
  val nWr = dmeParams.nWriteClients
  val rd = Vec(nRd, new DMEReadClient)
  val wr = Vec(nWr, new DMEWriteClient)
}

/** Dandelion Memory Engine (DME).
 *
 * This unit multiplexes the memory controller interface for the Core. Currently,
 * it supports single-writer and multiple-reader mode and it is also based on AXI.
 */
class DME(implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val mem = new AXIMaster(memParams)
    val dme = new DMEClientVector
  })

  val nReadClients = dmeParams.nReadClients
  val rd_arb = Module(new Arbiter(new DMECmd, nReadClients))
  val rd_arb_chosen = RegEnable(rd_arb.io.chosen, rd_arb.io.out.fire())

  for (i <- 0 until nReadClients) {
    rd_arb.io.in(i) <> io.dme.rd(i).cmd
  }

  val sReadIdle :: sReadAddr :: sReadData :: Nil = Enum(3)
  val rstate = RegInit(sReadIdle)

  switch(rstate) {
    is(sReadIdle) {
      when(rd_arb.io.out.valid) {
        rstate := sReadAddr
      }
    }
    is(sReadAddr) {
      when(io.mem.ar.ready) {
        rstate := sReadData
      }
    }
    is(sReadData) {
      when(io.mem.r.fire() && io.mem.r.bits.last) {
        rstate := sReadIdle
      }
    }
  }

  /* ----------------------------------------------------------------*/
  val nWriteClients = dmeParams.nWriteClients
  val wr_arb = Module(new Arbiter(new DMECmd, nWriteClients))
  val wr_arb_chosen = RegEnable(wr_arb.io.chosen, wr_arb.io.out.fire())

  for (i <- 0 until nWriteClients) {
    wr_arb.io.in(i) <> io.dme.wr(i).cmd
  }

  val sWriteIdle :: sWriteAddr :: sWriteData :: sWriteResp :: Nil = Enum(4)
  val wstate = RegInit(sWriteIdle)
  val addrBits = memParams.addrBits
  val lenBits = memParams.lenBits
  val wr_cnt = RegInit(0.U(lenBits.W))

  when(wstate === sWriteIdle) {
    wr_cnt := 0.U
  }.elsewhen(io.mem.w.fire()) {
    wr_cnt := wr_cnt + 1.U
  }

  switch(wstate) {
    is(sWriteIdle) {
      when(wr_arb.io.out.valid) {
        wstate := sWriteAddr
      }
    }
    is(sWriteAddr) {
      when(io.mem.aw.ready) {
        wstate := sWriteData
      }
    }
    is(sWriteData) {
      when(
        io.dme.wr(wr_arb_chosen).data.valid && io.mem.w.ready && wr_cnt === wr_arb.io.out.bits.len) {
        wstate := sWriteResp
      }
    }
    is(sWriteResp) {
      when(io.mem.b.valid) {
        wstate := sWriteIdle
      }
    }
  }

  // registers storing read/write cmds
  val rd_len = RegEnable(init = 0.U(lenBits.W), enable = rd_arb.io.out.fire, next = rd_arb.io.out.bits.len)
  val rd_addr = RegEnable(init = 0.U(addrBits.W), enable = rd_arb.io.out.fire, next = rd_arb.io.out.bits.addr)
  val wr_len = RegEnable(init = 0.U(lenBits.W), enable = wr_arb.io.out.fire, next = wr_arb.io.out.bits.len)
  val wr_addr = RegEnable(init = 0.U(addrBits.W), enable = wr_arb.io.out.fire, next = wr_arb.io.out.bits.addr)

  rd_arb.io.out.ready := rstate === sReadIdle
  wr_arb.io.out.ready := wstate === sWriteIdle

  // rd arb
  for (i <- 0 until nReadClients) {
    io.dme.rd(i).data.valid := rd_arb_chosen === i.U && io.mem.r.valid
    io.dme.rd(i).data.bits := io.mem.r.bits.data
  }

  // wr arb
  for (i <- 0 until nWriteClients) {
    io.dme.wr(i).ack := (wr_arb_chosen === i.U) && io.mem.b.fire()
    io.dme.wr(i).data.ready := (wr_arb_chosen === i.U) && (wstate === sWriteData) && (io.mem.w.ready)
  }

  // mem
  io.mem.aw.valid := wstate === sWriteAddr
  io.mem.aw.bits.addr := wr_addr
  io.mem.aw.bits.len := wr_len

  io.mem.w.valid := (wstate === sWriteData) && io.dme.wr(wr_arb_chosen).data.valid
  io.mem.w.bits.data := io.dme.wr(wr_arb_chosen).data.bits
  io.mem.w.bits.last := wr_cnt === io.dme.wr(wr_arb_chosen).cmd.bits.len

  io.mem.b.ready := wstate === sWriteResp

  io.mem.ar.valid := rstate === sReadAddr
  io.mem.ar.bits.addr := rd_addr
  io.mem.ar.bits.len := rd_len

  io.mem.r.ready := rstate === sReadData & io.dme.rd(rd_arb_chosen).data.ready

  // AXI constants - statically defined
  io.mem.setConst()
}



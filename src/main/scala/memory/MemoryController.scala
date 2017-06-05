package memory

/**
  * Created by vnaveen0 on 2/6/17.
  */

import chisel3._
import chisel3.Module
import config._
import util._
import interfaces._
import muxes._
import mmu._


class MmuTestIO[T <: Data](gen: T, n: Int) extends Bundle {

  override def cloneType = new MmuTestIO(gen,n).asInstanceOf[this.type]
  val ready = Output(Bool())
  val valid = Output(Bool())
  val bits = Output(gen)
  val chosen = Output(UInt(log2Ceil(n).W))
}

class MemoryController(NReads: Int, NWrites: Int)(implicit val p: Parameters) extends
  Module with CoreParams{
  val io = IO(new Bundle {
    val ReadIn    = Vec(NReads,Flipped(Decoupled(new ReadReq())))
    val ReadOut   = Vec(NReads,Output(new ReadResp()))
    val WriteIn   = Vec(NWrites,Flipped(Decoupled(new WriteReq())))
    val WriteOut  = Vec(NWrites,Output(new WriteResp()))

    //  Todo Create individual Test case for Demux
    //  Todo To Test Demux
    //    val testInput = Input(new ReadResp())
    //    val testSel = Input(UInt(log2Ceil(NReads).W))
    //    val testEn = Input(Bool())

    // To test MMU
    val testReadReq   = new MmuTestIO(new ReadReq(), NReads)
  })

  //----------------------------------------------------------------------------------------
  // Connections
  // ReadIn --> ReadArbiter -> mmu
  // mmu --> Demux --> ReadOut

  // Declaring Modules

  //---------------------------------------------------------------------------------------
  // Purpose of readInReady_R register
  //---------------------------------------------------------------------------------------
  // The Arbiter.In.Ready Signal is not always activated and
  // cannot be depended upon. So "readInReady_R" register is used.
  // This  register sends ready signal to ReadIn. The ReadIn will
  // not send valid data if its input ready signal is false.
  //
  // By default this ready signal is true. Once, the arbiter
  // selects a Node it is stored inside MMU. and the corresponding
  // arbiter channel is switched off (by sending "false" to the ready
  // signal of corresponding ReadIN)
  // TODO Do not send false to ReadIn untill MMU has received data from the arbiter
  // Todo By default Arbiter does not wait for the ready signal of MMU
  // The switched off node will not send any valid data to the arbiter
  // after this event.
  //
  // Once, the data/ack signal for the selected Node comes back from the memory
  // MMU will send the response to that node.
  // Note :Before the start of the next iteration a RESET signal should set all bits
  // Note :corresponding to this node to false.


  val readInReady_R = RegInit(Vec(Seq.fill(NReads)(false.B)))

  //---------------------------------------------------------------------------------------
  // MMU
  val mmu = Module(new MMU(NReads,NWrites))

  // ReadArbiter
  //TODO We do not need nodeid's for ReadReq or WriteReq
  // Since, Arbiters are already hardwired as of now to each Ld/St Node

  // TODO: Do not depend on RRArbiter.in.ready to be true
  val readArbiter  = Module(new RRArbiter(new ReadReq(),NReads))


  // Demux
  val readDemux   = Module(new Demux(new ReadResp(),NReads))

  //----------------------------------------------------------------------------------------
  // Connecting ReadIn with Read Arbiter

  for (i <- 0 until NReads) {
    //TODO readArbiter.io.in(i).ready signal is not always active
    // and gets valid randomly.
    // So make sure readInReady_R is used to connect to ReadIn(i).ready signal
    //io.ReadIn(i).ready := readInReady_R(i)

    readArbiter.io.in(i).bits := io.ReadIn(i).bits
    readArbiter.io.in(i).valid := io.ReadIn(i).valid
  }

  for (i <- 0 until NReads) {
    io.ReadIn(i).ready := ~readInReady_R(i)
  }


  //Signal Node that Data is received by the MMU only when
  // MMU is ready to receive, Since Arbiter does not wait
  // for any ready Signal.
  when(readArbiter.io.out.valid && mmu.io.readReq.in.ready) {
    readInReady_R(readArbiter.io.chosen) := true.B
  }

  //----------------------------------------------------------------------------------------
  // Connection between Read Arbiter and mmu

  mmu.io.readReq.in.bits <> readArbiter.io.out.bits
  mmu.io.readReq.chosen <> readArbiter.io.chosen
  mmu.io.readReq.in.ready <> readArbiter.io.out.ready
  mmu.io.readReq.in.valid <> readArbiter.io.out.valid

  //----------------------------------------------------------------------------------------
  //Read Arbiter Test Circuit
  io.testReadReq.bits := readArbiter.io.out.bits
  io.testReadReq.chosen <> readArbiter.io.chosen
  io.testReadReq.ready := mmu.io.readReq.in.ready
  io.testReadReq.valid <> readArbiter.io.out.valid
  //----------------------------------------------------------------------------------------
  // ReadResponse - Connection between mmu and readDemux
  // ToDo  Note Demux is not handshaking signal
  // ToDo mmu.io.readResponse.ready signal unused
  // Once decided --> need to fix ready

  readDemux.io.sel        := mmu.io.readResp.chosen
  // TODO ReadResponse valid is redundant
  // Todo mmu.io.readResp.out.bits.valid is unused
  readDemux.io.en         := mmu. io.readResp.out.valid
  //    readDemux.io.en := false.B
  readDemux.io.input:= mmu.io.readResp.out.bits
  mmu.io.readResp.out.ready := true.B

  //-----------------------------------------------------------------------------------------
  // Todo Create separate Test case for Demux
  // Testing Demux
  //      readDemux.io.sel  := io.testSel
  //      readDemux.io.en   := io.testEn
  //      readDemux.io.input := io.testInput



  //----------------------------------------------------------------------------------------
  // Connection between readDemux and ReadOut

  for (i <- 0 until NReads) {
    //    io.ReadOut(i) <> readDemux.io.outputs(i)
    io.ReadOut(i).valid <> readDemux.io.valids(i)
    io.ReadOut(i).data <> readDemux.io.outputs(i).data
  }


  //  //----------------------------------------------------------------------------------------
  //  // Write Arbiter
  //
  //  val WriteReqArbiter  = Module(new RRArbiter(new WriteReq(),NWrites));
  //
  //  // Connect up Write ins with arbiters
  //  for (i <- 0 until NWrites) {
  //    WriteReqArbiter.io.in(i) <> io.WriteIn(i)
  //  }
  //
  //  val writeInArbiterAddress_R = RegInit(0.U(xlen.W))
  //  val writeInArbiterId_R = RegInit(0.U(log2Up(NWrites).W))
  //  val writeInArbiterValid_R = RegInit(false.B)
  //
  //  WriteReqArbiter.io.out.ready := true.B
  //
  //  writeInArbiterAddress_R := WriteReqArbiter.io.out.bits
  //  writeInArbiterId_R := WriteReqArbiter.io.chosen
  //  writeInArbiterValid_R := WriteReqArbiter.io.out.valid
  //
  //  mmu.io.writeReq.chosen := writeInArbiterId_R
  //  mmu.io.write.valid  := writeInArbiterValid_R
  //  mmu.In.write.data   := writeInArbiterAddress_R


  //----------------------------------------------------------------------------------------
  //  // Demux WriteResp
  //
  //  val writeRespDemux   = Module(new Demux(new WriteResp(),NWrites));
  //
  //  val writeOutArbiter_R = RegInit(0.U(xlen.W))
  //  val writeOutChosen_R = RegInit(0.U(log2Up(NReads).W) )
  //  val writeOutputValid_R = RegInit(false.B)
  //
  //  writeOutputValid_R    := mmu.Out.write.valid
  //  writeOutChosen_R      := mmu.Out.write.chosen
  //  writeOutArbiter_R     := mmu.Out.write.data
  //
  //
  //
  //  writeRespDemux.io.en    := writeOutputValid_R
  //  writeRespDemux.io.sel   := writeOutChosen_R
  //  writeRespDemux.io.input := writeOutArbiter_R
  //
  //  for (i <- 0 until NWrites) {
  //    io.WriteOut(i) <> writeRespDemux.io.outputs(i)
  //  }

  // Feed arbiter output to Regfile input port.
  //  RegFile.io.raddr1 := ReadArbiterReg.address
  // Feed Regfile output port to Demux port
  //  ReadRespDemux.io.input.data   := RegFile.io.rdata1
  //  RegFile.io.wen := WriteInputValid
  //  RegFile.io.waddr := WriteArbiterReg.address
  //  RegFile.io.wdata := WriteArbiterReg.data
  //  RegFile.io.wmask := WriteArbiterReg.mask

}

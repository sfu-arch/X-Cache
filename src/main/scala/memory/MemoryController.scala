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


class MemoryController(NReads: Int, NWrites: Int)(implicit val p: Parameters) extends
  Module with CoreParams{
  val io = IO(new Bundle {
    val ReadIn    = Vec(NReads,Flipped(Decoupled(new ReadReq())))
    val ReadOut   = Vec(NReads,Output(new ReadResp()))
    val WriteIn   = Vec(NWrites,Flipped(Decoupled(new WriteReq())))
    val WriteOut  = Vec(NWrites,Output(new WriteResp()))

    val input = Input(new ReadResp())
    val sel = Input(UInt(log2Ceil(NReads).W))
    val en = Input(Bool())

  })

  //----------------------------------------------------------------------------------------
  // Connections
  // ReadIn --> ReadArbiter -> mmu
  // mmu --> Demux --> ReadOut

  // Declaring Modules

  // MMU
  val mmu = Module(new MMU(NReads,NWrites))

  // ReadArbiter
  //TODO We do not need nodeid's for ReadReq or WriteReq
  // Since, Arbiters are already hardwired as of now to each Ld/St Node

  val readArbiter  = Module(new RRArbiter(new ReadReq(),NReads))


  // Demux
  val readDemux   = Module(new Demux(new ReadResp(),NReads))

  //----------------------------------------------------------------------------------------
  // Connecting ReadIn with Read Arbiter

  for (i <- 0 until NReads) {
    readArbiter.io.in(i) <> io.ReadIn(i)
  }

  //----------------------------------------------------------------------------------------
  // Connection between Read Arbiter and mmu

  mmu.io.readReq.in.bits <> readArbiter.io.out.bits
  mmu.io.readReq.chosen <> readArbiter.io.chosen
  mmu.io.readReq.in.ready <> readArbiter.io.out.ready
  mmu.io.readReq.in.valid <> readArbiter.io.out.valid

//  //----------------------------------------------------------------------------------------
//  // ReadResponse - Connection between mmu and readDemux
//  // ToDo  Note Demux is not handshaking signal
//  // ToDo mmu.io.readResponse.ready signal unused
//  // Once decided --> need to fix ready
//
//  readDemux.io.sel        := mmu.io.readResp.chosen
//  // TODO ReadResponse valid is redundant
//  // Todo mmu.io.readResp.out.bits.valid is unused
//  readDemux.io.en         := mmu. io.readResp.out.valid
//  //    readDemux.io.en := false.B
//  readDemux.io.input:= mmu.io.readResp.out.bits
//  mmu.io.readResp.out.ready := true.B
//
//  //-----------------------------------------------------------------------------------------
  //  // Testing Demux
    readDemux.io.sel  := io.sel
    readDemux.io.en   := io.en
    readDemux.io.input := io.input



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

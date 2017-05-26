package arbiters

/**
  * Created by vnaveen0 on 26/5/17.
  */

import chisel3._
import chisel3.Module
import config._
import util._
import interfaces._
import muxes._

abstract class AbstractArbiter(NReads: Int, NWrites: Int)(implicit val p: Parameters) extends
  Module with CoreParams{
  val io = IO(new Bundle {
    val ReadIn    = Vec(NReads,Flipped(Decoupled(new WriteReq())))
    val ReadOut   = Vec(NReads,Output(new ReadResp()))
    val WriteIn   = Vec(NWrites,Flipped(Decoupled(new WriteReq())))
    val WriteOut  = Vec(NWrites,Output(new WriteResp()))
  })

  //----------------------------------------------------------------------------------------
  // Read Arbiter

  // Connect up Read ins with arbiters
  for (i <- 0 until NReads) {
    ReadReqArbiter.io.in(i) <> io.ReadIn(i)
  }

  val ReadReqArbiter  = Module(new RRArbiter(new ReadReq(),NReads))
   // Arbiter output latches
  val readInArbiter_reg = RegInit(0.U(xlen.W))
  val readInChosen_reg = RegInit(0.U(log2Up(NReads).W) )
  val readInValid_reg = RegInit(false.B)

  readInArbiter_reg := ReadReqArbiter.io.out.bits
  readInChosen_reg := ReadReqArbiter.io.chosen
  readInValid_reg := ReadReqArbiter.io.out.valid

  ReadReqArbiter.io.out.ready := true.B

  MMU.In.read.chosen := readInChosen_reg
  MMU.In.read.valid  := readInValid_reg
  MMU.In.read.data   := readInArbiter_reg

  //----------------------------------------------------------------------------------------
  // Write Arbiter

  val WriteReqArbiter  = Module(new RRArbiter(new WriteReq(),NWrites));

  // Connect up Write ins with arbiters
  for (i <- 0 until NWrites) {
    WriteReqArbiter.io.in(i) <> io.WriteIn(i)
  }

  val writeInArbiter_reg = RegInit(0.U(xlen.W))
  val writeInChosen_reg = RegInit(0.U(log2Up(NWrites).W))
  val writeInValid_reg = RegInit(false.B)

  WriteReqArbiter.io.out.ready := true.B

  writeInArbiter_reg := WriteReqArbiter.io.out.bits
  writeInChosen_reg := WriteReqArbiter.io.chosen
  writeInValid_reg := WriteReqArbiter.io.out.valid

  MMU.In.write.chosen := writeInChosen_reg
  MMU.In.write.valid  := writeInValid_reg
  MMU.In.write.data   := writeInArbiter_reg

  //----------------------------------------------------------------------------------------
  // DeMux readResp

  val readRespDeMux   = Module(new Demux(new ReadResp(),NReads))

  val readOutArbiter_reg = RegInit(0.U(xlen.W))
  val readOutChosen_reg = RegInit(0.U(log2Up(NReads).W) )
  val readOutputValid_reg = RegInit(false.B)



  readOutArbiter_reg  := MMU.Out.read.data
  readOutChosen_reg   := MMU.Out.read.chosen
  readOutputValid_reg := MMU.Out.read.valid

  readRespDeMux.io.sel        := readOutChosen_reg
  readRespDeMux.io.en         := readOutputValid_reg
  readRespDeMux.io.input.data := readOutArbiter_reg


  for (i <- 0 until NReads) {
    io.ReadOut(i) <> readRespDeMux.io.outputs(i)
  }

  //----------------------------------------------------------------------------------------
  // DeMux WriteResp

  val writeRespDeMux   = Module(new Demux(new WriteResp(),NReads));

  val writeOutArbiter_reg = RegInit(0.U(xlen.W))
  val writeOutChosen_reg = RegInit(0.U(log2Up(NReads).W) )
  val writeOutputValid_reg = RegInit(false.B)

  writeOutputValid_reg    := MMU.Out.write.valid
  writeOutChosen_reg      := MMU.Out.write.chosen
  writeOutArbiter_reg     := MMU.Out.write.data



  writeRespDeMux.io.en    := writeOutputValid_reg
  writeRespDeMux.io.sel   := writeOutChosen_reg
  writeRespDeMux.io.input := writeOutArbiter_reg

  for (i <- 0 until NWrites) {
    io.WriteOut(i) <> writeRespDeMux.io.outputs(i)
  }

  // Feed arbiter output to Regfile input port.
  //  RegFile.io.raddr1 := ReadArbiterReg.address
  // Feed Regfile output port to Demux port
  //  ReadRespDeMux.io.input.data   := RegFile.io.rdata1
  //  RegFile.io.wen := WriteInputValid
  //  RegFile.io.waddr := WriteArbiterReg.address
  //  RegFile.io.wdata := WriteArbiterReg.data
  //  RegFile.io.wmask := WriteArbiterReg.mask

}

package node

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec} 

import muxes._
import config._
import util._
import interfaces._
import alloca._



abstract class Alloca(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val sizeinput      = Flipped(Decoupled(UInt(xlen.W)))

    val allocareq   = Decoupled(new AllocaReq())
    val allocaresp = new Bundle{
      val allocaaddr  = Input(new AllocaResp())
      val valid       = Input(Bool())}

    val addressout  = Decoupled(UInt(xlen.W))
   })
}

class AllocaNode(val ID: Int)(implicit p: Parameters) extends Alloca()(p) {

  //XXX when know that __AllocaRequest__ ready is alwasy true
  //look at SP allocator

  // Extra information
  val token  = RegInit(0.U)
  val nodeID = RegInit(ID.U)

  //Input latches
  val size_reg      = RegInit(0.U(xlen.W))
  val sizevalid_reg = RegInit(false.B)

  val output_reg      = RegInit(0.U(xlen.W))
  val outputvalid_reg = RegNext(io.allocaresp.valid, init = false.B)

  //Connecting input ready signal to the valid latch
  io.sizeinput.ready := ~sizevalid_reg
  
  //Connecting send request valid to input valid data
  io.allocareq.valid := sizevalid_reg

  //Concecting alloca request data signals
  io.allocareq.bits.node := nodeID
  io.allocareq.bits.size := size_reg
  
  //Connecting output valid to response valid signal
  
  //Connecting output data reg

  //Latch input if it's fire
  when(io.sizeinput.fire()){
    sizevalid_reg := io.sizeinput.bits
    size_reg      := io.sizeinput.valid
  }

  when(io.allocaresp.valid){
    outputvalid_reg := io.allocaresp.valid
    output_reg      := io.addressout.bits
  }


  //Restart to initial stage
  when(io.addressout.ready && outputvalid_reg){
    output_reg      := 0.U
    outputvalid_reg := false.B

    size_reg      := 0.U
    sizevalid_reg := false.B

    token := token + 1.U
  }

}


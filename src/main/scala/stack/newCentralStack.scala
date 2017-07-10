package alloca

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec} 

//import examples._
import muxes._
import config._
import util._
import interfaces._


//
abstract class newStack(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
  val AllocaIn   = Vec(10,Flipped(Decoupled(new AllocaReq())))
  val AllocaOut  = Vec(10,Output(new AllocaResp()))
  val Valids     = Vec(10,Output(Bool()))
   })
}

class  newCentralStack(implicit p: Parameters) extends newStack()(p) {

  // All arbiters are in here since they need to connect up with the regfile.

  // Parameters. 10 is the number of alloca nodes in the ll file
  val allocaArbiter   = Module(new Arbiter(new AllocaReq,10));
 
  // Response Demuxes. Regfile decoupled output connects to it.
  val allocaRespDeMux    = Module(new Demux(new AllocaResp,10))
 
  // Regfile. Convert to vector if you want multiple stacks.
  // val RegfileIOs = new RegFile().io
  val rc = Wire(UInt(16.W))
  val SP = RegInit(1.U(16.W))

  // Connect up Ins with Arbiters and Outputs with Demux
  for (i <- 0 until 10) {
    io.AllocaIn(i)    <> allocaArbiter.io.in(i)
    io.AllocaOut(i)   <> allocaRespDeMux.io.outputs(i)
    io.Valids(i)      <> allocaRespDeMux.io.valids(i)
  }


  //Arbiter is always ready
  allocaArbiter.io.out.ready := true.B

  //Make demux always enable
  allocaRespDeMux.io.en := true.B

  //Put a mux for updating the SP register choosing between
  //either new value or old SP value
  val muxRes = Mux(allocaArbiter.io.out.valid, rc, SP) 

  SP := muxRes


  //Compute the new address
  rc  := SP + allocaArbiter.io.out.bits.size

  //Connect Arbitter's choosen signal to Demux's select signal
  allocaRespDeMux.io.sel := allocaArbiter.io.chosen

  //Pass the new address to demux
  allocaRespDeMux.io.input.ptr := SP
  //allocaRespDeMux.io.input.ptr := muxRes



}


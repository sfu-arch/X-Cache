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


//TODO uncomment if you remove StackCentral.scala file
//
//abstract class Stack(implicit val p: Parameters) extends Module with CoreParams {
  //val io = IO(new Bundle {
  //val AllocaIn   = Vec(10,Flipped(Decoupled(new AllocaReq())))
  //val AllocaOut  = Vec(10,new AllocaResp())
   //})
//}

class  newCentralStack(implicit p: Parameters) extends Stack()(p) {

  // All arbiters are in here since they need to connect up with the regfile.

  // Parameters. 10 is the number of alloca nodes in the ll file
  val allocaArbiter   = Module(new Arbiter(new AllocaReq,10));
 
  // Response Demuxes. Regfile decoupled output connects to it.
  val allocaRespDeMux    = Module(new Demux(new AllocaResp,10))
 
  // Regfile. Convert to vector if you want multiple stacks.
  // val RegfileIOs = new RegFile().io
  val rc = Wire(UInt(16.W))
  val SP = RegNext(rc, 1.U(16.W))

  // Connect up Ins with Arbiters and Outputs with Demux
  for (i <- 0 until 10) {
    io.AllocaIn(i) <> allocaArbiter.io.in(i)
    io.AllocaOut(i).ptr <> allocaRespDeMux.io.outputs(i)
    io.AllocaOut(i).valid <> allocaRespDeMux.io.valids(i)
  }


  //Arbiter is always ready
  allocaArbiter.io.out.ready := true.B

  allocaRespDeMux.io.sel := allocaArbiter.io.chosen
  rc  := SP + allocaArbiter.io.out.bits.size
  allocaRespDeMux.io.input.ptr := rc


}


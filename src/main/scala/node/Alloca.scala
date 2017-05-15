package node

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



abstract class Alloca(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val sizeIn      = Decoupled(new AllocaIn())
    val AllocaReq   = Decoupled(new AllocaReq())
    val AllocaResp  = Flipped(new AllocaResp())
    val addressOut  = Flipped(Decoupled(new AllocaOut()))
   })
}

class AllocaNode(implicit p: Parameters, val ID: Int) extends Alloca()(p) {

  // Input 
  val SizeReg = Reg(UInt((xlen-10).W), next=io.sizeIn.bits)

  // States of the combinatorial logic
  val s_init :: s_req :: s_valid :: Nil = Enum(3)
  val state = Reg(init = s_init)

  // Extra information
  val token  = Reg(init = 0.U)
  val nodeID = Reg(init = ID.asUInt())


  //Implimenting Alloca state machine
  when (state === s_init){
    io.sizeIn.ready      := true.B
    io.AllocaReq.valid   := false.B
    io.addressOut.valid  := false.B
  }
  when(state === s_req){
    io.sizeIn.ready      := false.B
    io.AllocaReq.valid   := true.B
    io.addressOut.valid  := false.B
  }
  when(state === s_valid){
    io.sizeIn.ready      := false.B
    io.AllocaReq.valid   := false.B
    io.addressOut.valid  := true.B
  }
  
  when (state === s_init){
    when(io.sizeIn.valid) { state := s_req}
    .otherwise{ state := s_init}
  }
  when (state === s_req){
    when(io.AllocaResp.valid){ state := s_valid }
  }
  when (state === s_valid){
    state := s_init
    token := token + 1.U
  }
 

}


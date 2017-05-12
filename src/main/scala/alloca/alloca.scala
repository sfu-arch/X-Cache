package alloca

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec} 

import examples._
import config._
import util._
import interface._



abstract class Stack(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
  val AllocaIn   = Vec(10,Flipped(Decoupled(new AllocaReq())))
  val AllocaOut  = Vec(10,new AllocaResp())
   })
}

class  CentralStack(implicit p: Parameters) extends Stack()(p) {
  // implicit val p = tileParams
  // All arbiters are in here since they need to connect up with the regfile.
  // Alternatively pass in as a parameter

  // Parameters. 10 is the number of alloca nodes in the ll file
  val allocaArbiter   = Module(new Arbiter(new AllocaReq,10));
 
  // Response Demuxes. Regfile decoupled output connects to it.
  val allocaRespDeMux    = Module(new Demux(new AllocaResp,10))
 
  // Regfile. Convert to vector if you want multiple stacks.
  // val RegfileIOs = new RegFile().io
  val SP     = RegInit(1.U(16.W))
  val allocaReg    = Reg(init  = 1.U, next=SP)
 
  val s_init :: s_input :: s_exe  :: Nil = Enum(3)
 
  // State bits for running state machines.
  // No alloc state as you dont need a state machine
  val alloca_state = Reg(init = s_init)

  // Connect up Ins with Arbiters and Outputs with Demux
  for (i <- 0 until 10) {
    io.AllocaIn(i) <> allocaArbiter.io.in(i)
    io.AllocaOut(i) <> allocaRespDeMux.io.outputs(i)
  }
  val x = SP
  printf(p"SP: $x")
  // Actions. When fire. set ready to false and then 
  // set it to true once the function unit completes
  // The SP should be available only a cycle later since 
  // the FU is only looking for it a cycle later. 
  // AllocA always ready to receive values
  // 
  allocaArbiter.io.out.ready := true.B
  allocaRespDeMux.io.input.ptr := allocaReg

  when (allocaArbiter.io.out.fire())
  {
    allocaRespDeMux.io.sel := allocaArbiter.io.chosen
    SP := SP + allocaArbiter.io.out.bits.size
  }
}

// Tester.
// class StackTester(stack: CentralStack)(implicit p: config.Parameters) extends PeekPokeTester(stack)  {

//     poke(stack.io.AllocaIn(0).valid,1.U)
//     poke(stack.io.AllocaIn(0).bits.size,10.U)
//     poke(stack.io.AllocaIn(1).valid,1.U)
//     poke(stack.io.AllocaIn(1).bits.size,20.U)

    
//     println(s"io.in.bits: io.out.bits: ${peek(stack.io.AllocaIn(0))} io.out.bits: ${peek(stack.io.AllocaOut(0))}")
//     step(1)
//     println(s"io.in.bits: io.out.bits: ${peek(stack.io.AllocaIn(0))} io.out.bits: ${peek(stack.io.AllocaOut(0))}")
//     poke(stack.io.AllocaIn(0).valid,0.U)
//     step(1)
//     println(s"io.in.bits: io.out.bits: ${peek(stack.io.AllocaIn(1))} io.out.bits: ${peek(stack.io.AllocaOut(1))}")
// }




// class StackTests extends  FlatSpec with Matchers {
//   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//  it should "compute gcd excellently" in {
//     chisel3.iotesters.Driver(() => new CentralStack) { c =>
//       new StackTester(c)
//     } should be(true)
//   }
// }

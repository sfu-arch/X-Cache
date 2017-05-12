package arbiters

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec} 

//import examples._
import config._
import util._
import interfaces._
import muxes._
import regfile._



abstract class AbstractBus(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
  val ReadIn    = Vec(10,Flipped(Decoupled(new ReadReq())))
  val ReadOut   = Vec(10,Output(new ReadResp()))
  })
}


class  CentralBus(implicit p: Parameters) extends AbstractBus()(p) {
  // implicit val p = tileParams
  // All arbiters are in here since they need to connect up with the regfile.
  // Alternatively pass in as a parameter

  // Parameters. 10 is the number of alloca nodes in the ll file
  val ReadReqArbiter  = Module(new RRArbiter(new ReadReq,10));
  val ReadRespDeMux   = Module(new Demux(new ReadResp,10));

  val s_init :: s_input :: s_exe  :: Nil = Enum(3)

  // Regfile. Convert to vector if you want multiple stacks.
  val ArbiterReg    = Reg(new ReadReq(), next = ReadReqArbiter.io.out.bits)

  // Latch for incoming arbiter value.
  val state = Reg(init = s_init)

  // Is read state valid?
  val validRead  = Reg(init  = false.B,next = ReadReqArbiter.io.out.valid)

  // Is Read complete
  val done   = Reg(init  = false.B)

  // Connect up Ins with Arbiters
  for (i <- 0 until 10) {
    io.ReadIn(i) <> ReadReqArbiter.io.in(i)
    io.ReadOut(i) <> ReadRespDeMux.io.outputs(i)
  }

  ReadReqArbiter.io.out.ready := !validRead

  val SMEM = Module(new RegFile(UInt(width=32),size=32))

  SMEM.io.raddr1 := ArbiterReg.address
  ReadRespDeMux.io.input.data   := SMEM.io.rdata1

  SMEM.io.wen := true.B
  SMEM.io.waddr := 10.U
  SMEM.io.wdata := 100.U

  val x = SMEM.io.rdata1
  val z =  ReadReqArbiter.io.out.ready
  printf(p"State : $state Z: $z \n")

  // RegFile.io.read := ReadReqArbiter.io.out.address

   switch (state) {
    is(s_init) {
      done := false.B
      state := s_input
    }
    is(s_input) {
    	when(validRead)
    	{
    		state := s_exe
    	}
    }
    is (s_exe){ 
       done  := true.B
       validRead := false.B
       state := s_init
      }
    }
}

// Tester

// class BusTester(bus: CentralBus)(implicit p: config.Parameters) extends PeekPokeTester(bus)  {
//     // val dut = Module(AbstractBus)

//     poke(bus.io.ReadIn(0).valid,1.U)

//     poke(bus.io.ReadIn(0).bits.address,10.U)
    
//     for (i <- 0 to 100) 
//     {

//     	step(1)
//     }

//     // // dut.io.AllocaIn(0).valid := true.B
//     //      println(s"io.in.bits, io.out.bits: ${peek(stack.io.AllocaOut(0))}")
//     // step(1)
//     //      println(s"io.out.bits: ${peek(stack.io.AllocaOut(0))}")
//     // step(1)
//     //          println(s"io.out.bits: ${peek(stack.io.AllocaOut(0))}")
//     // step(1)
//     //  println(s"io.out.bits: ${peek(stack.io.AllocaOut(0))}")
  
// }

// class BusTests extends  FlatSpec with Matchers {
//   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//  it should "compute gcd excellently" in {
//     chisel3.iotesters.Driver(() => new CentralBus) { c =>
//       new BusTester(c)
//     } should be(true)
//   }
// }


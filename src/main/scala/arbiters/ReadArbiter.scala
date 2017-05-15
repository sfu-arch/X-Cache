package arbiters

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec} 

import examples._
import regfile._
import config._
import util._
import interface._



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


  // Regfile. Convert to vector if you want multiple stacks.
  // val RegFile     = p(BuildRFile)(p)
  val RegFile     = Module(new RFile(32)(p))

  val ArbiterReg   = Reg(new ReadReq(), next = ReadReqArbiter.io.out.bits)
  val InputChosen  = Reg(UInt(width=4),next=ReadReqArbiter.io.chosen)
  val InputValid   = Reg(init  = false.B,next=ReadReqArbiter.io.out.valid)
  val OutputChosen = Reg(UInt(width=4), init = 0.U, next = InputChosen)
  val OutputValid  = Reg(init = false.B, next = InputValid)


  // Connect up Ins with Arbiters
  for (i <- 0 until 10) {
    io.ReadIn(i) <> ReadReqArbiter.io.in(i)
    io.ReadOut(i) <> ReadRespDeMux.io.outputs(i)
  }
  ReadReqArbiter.io.out.ready := true.B
  
  // Wire up inports to RegFile
  RegFile.io.raddr1 := ArbiterReg.address
  ReadRespDeMux.io.input.data   := RegFile.io.rdata1

  // Wire up outports to Regfile
  ReadRespDeMux.io.sel := OutputChosen
  ReadRespDeMux.io.en := OutputValid

  // RegFile.io.wen := true.B
  // RegFile.io.waddr := 20.U
  // RegFile.io.wdata := 100.U

  //  val x =  ReadRespDeMux.io.input.data
  //  val z =  ReadRespDeMux.io.valids

  //  printf(p"$ArbiterReg  data: $x $z \n")


   // switch (state) {
   //  is(s_init) {
   //    ReadRespDeMux.io.en := true.B
   //    done := false.B
   //    state := s_input
   //  }
   //  is(s_input) {
   //   ReadRespDeMux.io.en := false.B
   //   when(validRead)
   //   {   
   //     chosen := ReadReqArbiter.io.chosen
   //     state := s_exe
   //   }
   //  }
   //  is (s_exe){
   //   done  := true.B
   //   validRead := false.B
   //   state := s_init
   //  }
   //  }
}


// class BusTester(bus: CentralBus)(implicit p: config.Parameters) extends PeekPokeTester(bus)  {
//     // val dut = Module(AbstractBus)

    
//     poke(bus.io.ReadIn(0).valid,1.U)
//     poke(bus.io.ReadIn(0).bits.address,15.U)
//     poke(bus.io.ReadIn(1).valid,1.U)
//     poke(bus.io.ReadIn(1).bits.address,20.U)

//     for (i <- 2 to 9)
//     {
//       poke(bus.io.ReadIn(i).valid,0.U)
//     } 


//     for (i <- 0 to 10) 
//     {
//       println(s"io.out.bits[0]: io.out.bits[1]: ${peek(bus.io.ReadOut(0))} io.out.bits: ${peek(bus.io.ReadOut(1))}")
//               step(1)     
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
//   it should "compute gcd excellently" in {
//     chisel3.iotesters.Driver(() => new CentralBus(Write = false.B)) { c =>
//       new BusTester(c)
//     } should be(true)
//   }
// }


package arbiters

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec} 

import regfile._
import config._
import util._
import interfaces._
import muxes._


abstract class AbstractArbiter(NReads: Int, NWrites: Int)(implicit val p: Parameters) extends Module with CoreParams{
  val io = IO(new Bundle {
  val ReadIn    = Vec(NReads,Flipped(Decoupled(new ReadReq())))
  val ReadOut   = Vec(NReads,Output(new ReadResp()))
  val WriteIn   = Vec(NWrites,Flipped(Decoupled(new WriteReq())))
  val WriteOut  = Vec(NWrites,Output(new WriteResp()))
  })
}


// Size   : Size of Register file to be allocated and managed
// NReads  : Number of static reads to be connected. Controls size of arbiter and Demux
// NWrites : Number of static writes to be connected. Controls size of arbiter and Demux

class  CentralizedStackRegFile(Size: Int, NReads: Int, NWrites: Int)(implicit p: Parameters) extends AbstractArbiter(NReads,NWrites)(p) { 

  val RegFile     = Module(new RFile(Size)(p))

  // -------------------------- Read Arbiter Logic -----------------------------------------
  // Parameters. 10 is the number of loads assigned to the stack segment in the ll file
  val ReadReqArbiter  = Module(new RRArbiter(new ReadReq(),NReads));
  val ReadRespDeMux   = Module(new Demux(new ReadResp(),NReads));

  // Arbiter output latches
  val ReadArbiterReg   = Reg(new ReadReq(), next = ReadReqArbiter.io.out.bits)
  val ReadInputChosen  = Reg(UInt(width=log2Up(NReads)),next=ReadReqArbiter.io.chosen)
  val ReadInputValid   = Reg(init  = false.B,next=ReadReqArbiter.io.out.valid)
  
  // Demux input latches. chosen and valid delayed by 1 cycle for RFile read to return
  val ReadOutputChosen = Reg(UInt(width=log2Up(NReads)), init = 0.U, next = ReadInputChosen)
  val ReadOutputValid  = Reg(init = false.B, next = ReadInputValid)

  // Connect up Read ins with arbiters
  for (i <- 0 until NReads) {
    io.ReadIn(i) <> ReadReqArbiter.io.in(i)
    io.ReadOut(i) <> ReadRespDeMux.io.outputs(i)
  }

  // Activate arbiter
  ReadReqArbiter.io.out.ready := true.B

  // Feed arbiter output to Regfile input port. 
  RegFile.io.raddr1 := ReadArbiterReg.address
  // Feed Regfile output port to Demux port
  ReadRespDeMux.io.input.data   := RegFile.io.rdata1

  ReadRespDeMux.io.sel := ReadOutputChosen
  ReadRespDeMux.io.en := ReadOutputValid




  // -------------------------- Write Arbiter Logic -----------------------------------------
  // Parameters. 10 is the number of loads assigned to the stack segment in the ll file
  val WriteReqArbiter  = Module(new RRArbiter(new WriteReq(),NWrites));
  val WriteRespDeMux   = Module(new Demux(new WriteResp(),NWrites));

  // Arbiter output latches
  val WriteArbiterReg   = Reg(new WriteReq(), next = WriteReqArbiter.io.out.bits)
  val WriteInputChosen  = Reg(UInt(width=log2Up(NWrites)),next=WriteReqArbiter.io.chosen)
  val WriteInputValid   = Reg(init  = false.B,next=WriteReqArbiter.io.out.valid)
  
  // Demux input latches. chosen and valid delayed by 1 cycle for RFile Write to return
  val WriteOutputChosen = Reg(UInt(width=log2Up(NWrites)), init = 0.U, next = WriteInputChosen)
  val WriteOutputValid  = Reg(init = false.B, next = WriteInputValid)

  // Connect up Write ins with arbiters
  for (i <- 0 until NWrites) {
    io.WriteIn(i) <> WriteReqArbiter.io.in(i)
    io.WriteOut(i) <> WriteRespDeMux.io.outputs(i)
  }

  // Activate arbiter.   // Feed write  arbiter output to Regfile input port. 
  WriteReqArbiter.io.out.ready := true.B

  RegFile.io.wen := WriteInputValid
  RegFile.io.waddr := WriteArbiterReg.address
  RegFile.io.wdata := WriteArbiterReg.data
  RegFile.io.wmask := WriteArbiterReg.mask

  // Feed regfile output port to Write Demux port. Only need to send valid back to operation.
  // In reality redundant as arbiter ready signal already indicates write acquired write port.
  // This signal guarantees the data has propagated to the Registerfile
  //WriteRespDeMux.io.input.valid   := 1.U
  WriteRespDeMux.io.sel := WriteOutputChosen
  WriteRespDeMux.io.en := WriteOutputValid

}


// class ArbiterTester (bus: CentralizedStackRegFile)(implicit p: config.Parameters) extends PeekPokeTester(bus)  {
//     // val dut = Module(AbstractBus)


//     poke(bus.io.WriteIn(0).valid,0.U)

//     poke(bus.io.WriteIn(1).bits.address,15.U)
//     poke(bus.io.WriteIn(1).valid,1.U)
//     poke(bus.io.WriteIn(1).bits.data,1500.U)

//     poke(bus.io.ReadIn(0).valid,1.U)
//     poke(bus.io.ReadIn(0).bits.address,15.U)
//     poke(bus.io.ReadIn(1).valid,1.U)
//     poke(bus.io.ReadIn(1).bits.address,20.U)


//     for (i <- 2 to 9)
//     {
//       poke(bus.io.ReadIn(i).valid,0.U)
//       poke(bus.io.WriteIn(i).valid,0.U)
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

// class ArbiterTests extends  FlatSpec with Matchers {
//   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//   it should "compute gcd excellently" in {
//     chisel3.iotesters.Driver(() => new CentralizedStackRegFile(Size=32, NReads=32, NWrites=32)) { c =>
//       new ArbiterTester(c)
//     } should be(true)
//   }
// }

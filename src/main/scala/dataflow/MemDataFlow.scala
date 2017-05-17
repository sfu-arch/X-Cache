package dataflow

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._


abstract class MemDFIO()(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle{
    val gepAddr = Flipped(Decoupled(UInt(xlen.W)))
    val predMemOp = Flipped(Decoupled(UInt(1.W)))

    val memOpAck = Decoupled(UInt(1.W)) //TODO 0 bits

  })
}

class MemDataFlow(implicit p: Parameters) extends MemDFIO()(p){

  val m0 = Module(new LoadNode())

  val m1 = Module(new CentralizedStackRegFile(Size=32, NReads=1, NWrites=1))

  m0.io.memReq  <> m1.io.ReadIn(0)
  m0.io.memResp <> m1.io.ReadOut(0)

  m0.io.gepAddr   <> io.gepAddr
  m0.io.predMemOp <> io.predMemOp
  io.memOpAck     <> m0.io.memOpAck

}

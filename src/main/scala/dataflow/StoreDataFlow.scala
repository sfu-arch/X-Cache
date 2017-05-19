package dataflow

/**
  * Created by nvedula on 17/5/17.
  */

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._


//class RvIO (implicit p: Parameters) extends CoreBundle()(p) {
class RvIO(implicit  val p: Parameters) extends Bundle with CoreParams{
  override def cloneType = new RvIO().asInstanceOf[this.type]

  val ready = Input(Bool())
  val valid = Output(Bool())
  val bits  = Output(UInt(xlen.W))
}

abstract class StoreDFIO()(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle{
    val testReady = Output(Bool())
    //    val gepAddr   = Flipped(new RvIO())
    val gepAddr_ready = Output(Bool())
    val gepAddr_valid = Input(Bool())
    val gepAddr_bits = Input (UInt(xlen.W))

    val predMemOp = Flipped(new RvIO())
    val inData    = Flipped(new RvIO())

    val memOpAck  = Decoupled(UInt(1.W)) //TODO 0 bits

  })
}

class StoreDataFlow(implicit p: Parameters) extends StoreDFIO()(p){

  val m0 = Module(new StoreNode())

//  val m1 = Module(new CentralizedStackRegFile(Size=32, NReads=1, NWrites=1))

//  m1.io.WriteIn(0) <> m0.io.memReq
//  m0.io.memResp <> m1.io.WriteOut(0)

  io.gepAddr_ready      := m0.io.gepAddr.ready
  m0.io.gepAddr.valid   := io.gepAddr_valid
  m0.io.gepAddr.bits    := io.gepAddr_bits
  m0.io.predMemOp(0)  <> io.predMemOp
  m0.io.inData        <> io.inData
  io.memOpAck         <> m0.io.memOpAck

//  io.testReady := m0.io.gepT



when (io.gepAddr_ready) {
  printf("\n StDF IO gepReady \n")
}

  when (io.gepAddr_valid) {
  printf("\n StDF IO valid \n")
}

 when (m0.io.gepAddr.ready) {
  printf("\n StDF m0.IO gepReady \n")
}
  when (m0.io.gepAddr.valid) {
  printf("\n StDF m0.IO valid \n")
}






}

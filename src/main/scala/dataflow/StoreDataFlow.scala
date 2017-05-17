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
    val gepAddr   = Flipped(new RvIO())
    val predMemOp = Flipped(new RvIO())
    val inData    = Flipped(new RvIO())

    val memOpAck  = Decoupled(UInt(1.W)) //TODO 0 bits

  })
}

class StoreDataFlow(implicit p: Parameters) extends StoreDFIO()(p){

  val m0 = Module(new StoreNode())

  val m1 = Module(new CentralizedStackRegFile(Size=32, NReads=1, NWrites=1))

  m1.io.WriteIn(0) <> m0.io.memReq
  m0.io.memResp <> m1.io.WriteOut(0)

  m0.io.gepAddr       <> io.gepAddr
  m0.io.predMemOp(0)  <> io.predMemOp
  m0.io.inData        <> io.inData
  io.memOpAck         <> m0.io.memOpAck

//  io.gepAddr.ready := true.B
//  io.predMemOp.ready := true.B
//  io.inData.ready := true.B
//
//  when(io.gepAddr.fire() && m0.io.gepAddr.fire()) {
//    m0.io.gepAddr.valid  := io.gepAddr.valid
//    m0.io.gepAddr.bits := io.gepAddr.bits
//  }
//    .otherwise(  m0.io.gepAddr.valid := false.B )
//
//  when(io.inData.fire() && m0.io.inData.fire()) {
//    m0.io.inData.valid  := io.inData.valid
//    m0.io.inData.bits := io.inData.bits
//  }
//    .otherwise(  m0.io.inData.valid := false.B )
//
//   when(io.predMemOp.fire() && m0.io.predMemOp(0).fire()) {
//    m0.io.predMemOp(0).valid  := io.predMemOp.valid
//    m0.io.predMemOp(0).bits := io.predMemOp.bits
//  }
//    .otherwise(  m0.io.predMemOp(0).valid := false.B )








}

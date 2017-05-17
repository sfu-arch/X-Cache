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


abstract class StoreDFIO()(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle{
    val gepAddr = Flipped(Decoupled(UInt(xlen.W)))
    val predMemOp = Flipped(Decoupled(UInt(1.W)))
    val inData = Flipped(Decoupled(UInt(xlen.W)))

    val memOpAck = Decoupled(UInt(1.W)) //TODO 0 bits

  })
}

class StoreDataFlow(implicit p: Parameters) extends StoreDFIO()(p){

  val m0 = Module(new StoreNode())

  val m1 = Module(new CentralizedStackRegFile(Size=32, NReads=1, NWrites=1))

  m1.io.WriteIn(0) <> m0.io.memReq
  m0.io.memResp.valid := m1.io.WriteOut(0).valid
  m0.io.memResp.bits.done  := m1.io.WriteOut(0).done

  m0.io.gepAddr       <> io.gepAddr
  m0.io.predMemOp(0)  <> io.predMemOp
  m0.io.inData        <> io.inData
  io.memOpAck         <> m0.io.memOpAck

}

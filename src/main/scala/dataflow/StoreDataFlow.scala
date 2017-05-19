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

//  m0.io.predMemOp(0)  <> io.predMemOp
//  m0.io.inData        <> io.inData
//  io.memOpAck         <> m0.io.memOpAck

  // ----  GEP ADDR ------
  val gepAddr_ready_reg = RegInit(false.B)
  val gepAddr_valid_reg = RegInit(false.B)
  val gepAddr_bits_reg = RegInit(0.U(xlen.W))

  io.gepAddr.ready := gepAddr_ready_reg
  gepAddr_ready_reg := m0.io.gepAddr.ready

  gepAddr_valid_reg := io.gepAddr.valid
  m0.io.gepAddr.valid := gepAddr_valid_reg

  gepAddr_bits_reg := io.gepAddr.valid
  m0.io.gepAddr.valid := gepAddr_valid_reg

  //----- inDATA -----------
  val inData_ready_reg    = RegInit(false.B)
  val inData_valid_reg    = RegInit(false.B)
  val inData_bits_reg     = RegInit(0.U(xlen.W))


  io.inData.ready     := inData_ready_reg
  inData_ready_reg    := m0.io.inData.ready

  inData_valid_reg    := io.inData.valid
  m0.io.inData.valid  := inData_valid_reg

  inData_bits_reg     := io.inData.bits
  m0.io.inData.bits   := inData_bits_reg


  //----- predMemOP -----------
  val predMemOp_ready_reg    = RegInit(false.B)
  val predMemOp_valid_reg    = RegInit(false.B)
  val predMemOp_bits_reg     = RegInit(0.U(xlen.W))


  io.predMemOp.ready     := predMemOp_ready_reg
  predMemOp_ready_reg    := m0.io.predMemOp(0).ready

  predMemOp_valid_reg    := io.predMemOp.valid
  m0.io.predMemOp(0).valid  := predMemOp_valid_reg

  predMemOp_bits_reg     := io.predMemOp.bits
  m0.io.predMemOp(0).bits   := predMemOp_bits_reg







  when (io.gepAddr.ready) {
    printf("\n StDF IO gepReady \n")
  }

  when (io.gepAddr.valid) {
    printf("\n StDF IO valid \n")
  }

  when (m0.io.gepAddr.ready) {
    printf("\n StDF m0.IO gepReady \n")
  }
  when (m0.io.gepAddr.valid) {
    printf("\n StDF m0.IO valid \n")
  }


}

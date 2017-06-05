package dataflow

/**
  * Created by nvedula on 19/5/17.
  */

import chisel3._
import chisel3.util._

import node._
import config._
import arbiters._
import interfaces._



abstract class LoadDFIO()(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle{

    val gepAddr   = Flipped(new RvIO())
    val predMemOp = Flipped(new RvIO())
    val memOpAck  = new RvAckIO()
  })
}

class LoadDataFlow(implicit p: Parameters) extends LoadDFIO()(p){

  val m0 = Module(new LoadNode())
  val m1 = Module(new CentralizedStackRegFile(Size=32, NReads=1, NWrites=1))

  //TODO : Connect using Registers - else it makes a combinational loop
  m1.io.ReadIn(0) <> m0.io.memReq
  m0.io.memResp <> m1.io.ReadOut(0)


  // TODO : Figure out why does not work without Registers
  // ----  GEP ADDR ------
  val gepAddr_ready_reg = RegInit(false.B)
  val gepAddr_valid_reg = RegInit(false.B)
  val gepAddr_bits_reg = RegInit(0.U(xlen.W))

  io.gepAddr.ready := gepAddr_ready_reg
  gepAddr_ready_reg := m0.io.gepAddr.ready

  gepAddr_valid_reg := io.gepAddr.valid
  m0.io.gepAddr.valid := gepAddr_valid_reg

  gepAddr_bits_reg := io.gepAddr.bits
  m0.io.gepAddr.bits := gepAddr_bits_reg


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



    //val memOpAck  = (new RvAckIO()))
  //----- memOpAck -----------
  val memOpAck_ready_reg    = RegInit(false.B)
  val memOpAck_valid_reg    = RegInit(false.B)


  m0.io.memOpAck.ready := memOpAck_ready_reg
  memOpAck_ready_reg := io.memOpAck.ready


  io.memOpAck.valid := memOpAck_valid_reg
  memOpAck_valid_reg := m0.io.memOpAck.valid


  printf(p"ReadIn:  ${m1.io.ReadIn(0)}\n")
  printf( p"ReadOut: ${m1.io.ReadOut(0)} \n" )






//  when (io.gepAddr.ready) {
//    printf("\n LdDF IO gepReady \n")
//  }
//
//  when (io.gepAddr.valid) {
//    printf("\n LdDF IO valid \n")
//  }
//
//  when (m0.io.gepAddr.ready) {
//    printf("\n LdDF m0.IO gepReady \n")
//  }
//  when (m0.io.gepAddr.valid) {
//    printf("\n LdDF m0.IO valid \n")
//  }


}

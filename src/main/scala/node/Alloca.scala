package node

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec}

import config._
import interfaces._
import muxes._
import util._

class AllocaNodeIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts) {
  /**
    * @note requested size for address
    */
  val allocaInputIO = Flipped(Decoupled(new AllocaIO()))

  /**
    * @note Alloca interface to talk with stack
    */
  val allocaReqIO = Decoupled(new AllocaReq())
  val allocaRespIO = Flipped(Decoupled(new AllocaResp()))

}

class AllocaNode(NumOuts: Int, ID: Int)
                (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(p) {
  override lazy val io = IO(new AllocaNodeIO(NumOuts))
  // Printf debugging
  override val printfSigil = "Node ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val alloca_R = RegInit(AllocaIO.default)

  // Output register
  val data_R = RegInit(0.U(xlen.W))
  val pred_R = RegInit(false.B)

  // Alloca req
  val alloca_req_R = RegInit(AllocaReq.default)

  // Alloca resp
  val alloca_resp_R = RegInit(AllocaResp.default)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = alloca_R.predicate & IsEnable()
  val start = alloca_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register

  io.allocaInputIO.ready := ~alloca_R.valid

  when(io.allocaInputIO.fire()) {
    alloca_R.size := io.allocaInputIO.bits.size
    alloca_R.numByte := io.allocaInputIO.bits.numByte
    alloca_R.valid := io.allocaInputIO.bits.valid
    alloca_R.predicate := io.allocaInputIO.bits.predicate
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_R
    io.Out(i).bits.predicate := predicate
  }

  io.allocaReqIO.bits := AllocaReq.default

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & predicate) {

    /**
      * The send request is valid if only
      * the valid and predicate of the input is valid
      */
    //Adding alloca req signaling
    io.allocaReqIO.valid := alloca_R.valid & predicate
    io.allocaReqIO.bits.nodeID := nodeID_R
    io.allocaReqIO.bits.size := alloca_R.size
    io.allocaReqIO.bits.numByte := alloca_R.numByte
    io.allocaReqIO.bits.valid := alloca_R.valid

    //Adding alloca resp signaling
    io.allocaRespIO.ready := alloca_R.valid

    when(io.allocaRespIO.fire){
      alloca_R.valid := false.B
      data_R := io.allocaRespIO.bits.ptr
      pred_R := predicate
      ValidOut()
    }
    //      io.allocaReqIO.valid := false.B

  }.elsewhen(start & ~predicate) {
    /**
      * Send the request to the stack BUT make the predicate false
      */
    data_R := 0.U
    pred_R := predicate
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  when(out_ready_W & out_valid_W) {
    // Reset data
    alloca_R := AllocaIO.default
    // Reset output
    data_R := 0.U
    pred_R := false.B
    out_ready_R := Vec(Seq.fill(NumOuts) {
      false.B
    })
  }

  printf(p"Alloca reg: ${alloca_R}\n")
  printf(p"Alloca input: ${io.allocaInputIO}\n")
  printf(p"Alloca req:   ${io.allocaReqIO}\n")
  printf(p"Alloca res:   ${io.allocaRespIO}\n")

}

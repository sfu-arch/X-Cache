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
  extends HandShakingIONPS(NumOuts)(new DataBundle) {
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

class AllocaNode(NumOuts: Int, ID: Int, RouteID: Int)
                (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
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
    alloca_R.valid := true.B
    alloca_R.predicate := io.allocaInputIO.bits.predicate
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_R
    io.Out(i).bits.predicate := predicate
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  val s_IDLE :: s_SEND :: s_OUT :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  /**
    * State outputs
    */
  when(state === s_IDLE && !start) {
    io.allocaReqIO.bits := AllocaReq.default
    io.allocaRespIO.ready := true.B
    io.allocaReqIO.valid := false.B
    data_R := 0.U

    state := s_IDLE

  }.elsewhen(state === s_IDLE && start) {
    when(predicate) {
      io.allocaRespIO.ready := true.B
      io.allocaReqIO.valid := true.B
      data_R := 0.U
      /**
        * Send Alloca request
        */
      io.allocaReqIO.bits.RouteID := RouteID.U
      io.allocaReqIO.bits.size := alloca_R.size
      io.allocaReqIO.bits.numByte := alloca_R.numByte
     
      /**
        * Check if the response is ready in the same cycle
        */
      when(io.allocaReqIO.fire) {
        alloca_resp_R <> io.allocaRespIO.bits
        data_R := io.allocaRespIO.bits.ptr
        pred_R := predicate
        ValidOut()
        /**
          * Update the state
          */
        state := s_OUT
      }.elsewhen(!predicate){
        /**
          * if predicate is not activate
          * validate output and jump to last state
          */
        ValidOut
        state := s_OUT
      }


    }.otherwise {
      /**
        * Update the state
        */
      state := s_SEND

    }
  }

  when(state === s_SEND && io.allocaRespIO.fire) {

    alloca_resp_R <> io.allocaRespIO.bits
    data_R := io.allocaRespIO.bits.ptr
    pred_R := predicate
    ValidOut()
    /**
      * Update the state
      */
    state := s_OUT

  }

  /**
    * Output state
    */

  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR
  when(state === s_OUT) {
    when(out_ready_W & out_valid_W) {
      io.allocaRespIO.ready := false.B
      io.allocaReqIO.valid := false.B
      alloca_R := AllocaIO.default
      data_R := 0.U
      pred_R := false.B

      out_ready_R := Vec(Seq.fill(NumOuts) {
        false.B
      })

      state := s_IDLE

      Reset()
    }.otherwise {

      io.allocaRespIO.ready := false.B
      io.allocaReqIO.valid := false.B

      /**
        * Keep output valid
        */
      ValidOut()


      state := s_OUT
    }
  }

  printf(p"State: ${state}\n")
  printf(p"Alloca reg: ${alloca_R}\n")
  printf(p"Alloca input: ${io.allocaInputIO}\n")
  printf(p"Alloca req:   ${io.allocaReqIO}\n")
  printf(p"Alloca res:   ${io.allocaRespIO}\n")

}

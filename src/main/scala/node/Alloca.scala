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

class AllocaNodeIO(NumOuts: Int) (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {
  /**
    * @note requested size for address
    */
  val allocaInputIO = Flipped(Decoupled(new AllocaIO()))

  /**
    * @note Alloca interface to talk with stack
    */
  val allocaReqIO = Decoupled(new AllocaReq())
  val allocaRespIO = Input(Flipped(new AllocaResp()))

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

  val s_idle :: s_SENDING :: s_RECEIVING :: s_Done :: Nil = Enum(4)
  val state = RegInit(s_idle)

  io.allocaReqIO.valid := false.B

  /**
    * State outputs
    */
   
   /*=============================================
=            ACTIONS (possibly dangerous)     =
=============================================*/

  when(start & predicate) {
    when (state === s_idle) {
    // ACTION:  Memory request
    //  Check if address is valid and predecessors have completed.
    io.allocaReqIO.bits.size := alloca_R.size
    io.allocaReqIO.bits.numByte := alloca_R.numByte
    io.allocaReqIO.bits.node    := nodeID_R
    io.allocaReqIO.bits.RouteID := RouteID.U
    io.allocaReqIO.valid := true.B
    }

    //  ACTION: Arbitration ready
    //   <- Incoming memory arbitration
    when((state === s_idle) && (io.allocaReqIO.fire())) {
      // Set data output registers
      data_R       := io.allocaRespIO.ptr
      ValidOut()
      // Completion state.
      state := s_Done
    }
    }.elsewhen(start & ~predicate & state === s_idle) {
     ValidOut()
     state := s_Done
   }
  /**
    * Output state
    */

  when(state === s_Done) {
    when(IsOutReady()) {
      io.allocaReqIO.valid := false.B
      alloca_R := AllocaIO.default
      data_R := 0.U
      pred_R := false.B
      state := s_idle
      Reset()
    }
  }
  // printf(p"State: ${state}\n")
  // printf(p"Alloca reg: ${alloca_R}\n")
  // printf(p"Alloca input: ${io.allocaInputIO}\n")
  // printf(p"Alloca req:   ${io.allocaReqIO}\n")
  // printf(p"Alloca res:   ${io.allocaRespIO}\n")

  
}

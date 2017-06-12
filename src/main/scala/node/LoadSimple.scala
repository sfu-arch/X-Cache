package node

/**
 * Created by nvedula on 15/5/17.
 */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._
import utility.Constants._
import utility.UniformPrintfs



// Design Doc
//////////
/// DRIVER ///
/// 1. Memory response only available atleast 1 cycle after request
//  2. Handshaking has to be done with registers.
//////////

class LoadSimpleIO(NumPredOps: Int,
  NumSuccOps: Int,
  NumOuts: Int)(implicit p: Parameters)
  extends HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts) {
  // GepAddr: The calculated address comming from GEP node
  val GepAddr = Flipped(Decoupled(new DataBundle))
  // Memory request
  val memReq = Decoupled(new ReadReq())
  // Memory response.
  val memResp = Input(Flipped(new ReadResp()))
}

class LoadSimpleNode(NumPredOps: Int,
  NumSuccOps: Int,
  NumOuts: Int,
  Typ: UInt = MT_W, ID: Int)(implicit p: Parameters)
  extends HandShaking(NumPredOps, NumSuccOps, NumOuts, ID)(p) {

  override lazy val io = IO(new LoadSimpleIO(NumPredOps, NumSuccOps, NumOuts))
  // Printf debugging
  override val printfSigil = "Load ID: " + ID + " "

/*=============================================
=            Registers                        =
=============================================*/
  // OP Inputs
  val addr_R = RegInit(DataBundle.default)

  // Memory Response
  val data_R = RegInit(DataBundle.default)

  // State machine
  val s_idle :: s_RECEIVING :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)

/*============================================
=            Predicate Evaluation            =
============================================*/

  val predicate = addr_R.predicate & IsEnable()
  val start = addr_R.valid & IsPredValid & IsEnableValid()

/*================================================
=            Latch inputs. Wire up output            =
================================================*/

  io.GepAddr.ready := ~addr_R.valid
  when(io.GepAddr.fire()) {
    addr_R := io.GepAddr.bits
    addr_R.valid := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := data_R
    io.Out(i).bits.predicate := predicate
  }

  when(start & predicate) {

/*=============================================
=            ACTIONS (possibly dangerous)     =
=============================================*/

    // ACTION:  Memory request
    //  Check if address is valid and predecessors have completed.
    val mem_req_fire = addr_R.valid & IsPredValid()
    io.memReq.bits.address := addr_R.data
    io.memReq.bits.node := nodeID_R
    io.memReq.valid := false.B
    // ACTION: Outgoing Address Req ->
    when((state === s_idle) && (mem_req_fire)) {
      io.memReq.valid := true.B
    }

    //  ACTION: Arbitration ready
    //   <- Incoming memory arbitration
    when((state === s_idle) && (io.memReq.ready === true.B) && (io.memReq.valid === true.B)) {
      state := s_RECEIVING
    }

    // Data detected only one cycle later.
    // Memory should supply only one cycle after arbitration.
    //  ACTION:  <- Incoming Data
    when(state === s_RECEIVING && io.memResp.valid) {
      // Set data output registers
      data_R.data := io.memResp.data
      data_R.valid := true.B
      ValidSucc()
      ValidOut()
      // Completion state.
      state := s_Done
    }
  }.elsewhen(start & ~predicate) {
    ValidSucc()
    ValidOut()
    state := s_Done
  }
/*===========================================
}
=            Output Handshaking and Reset   =
===========================================*/

  //  ACTION: <- Check Out READY and Successors READY
  when(state === s_Done) {
    // When successors are complete and outputs are ready you can reset.
    // data already valid and would be latched in this cycle.
    val complete = IsSuccReady() & IsOutReady()
    when(complete) {
      // Clear all the valid states.
      // Reset address
      addr_R := DataBundle.default
      // Reset data
      data_R := DataBundle.default
      // Reset state.
      Reset()
      // Reset state.
      state := s_idle
    }
  }
  printfInfo("State : %x, Out: %x Succ Mem Op [", state, io.Out(0).bits.data)

  for (i <- 0 until NumSuccOps) {
    printf(p"${io.SuccOp(i).valid},")
  }
  printf("]\n")
}

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
//  2. Need registers for pipeline handshaking e.g., _valid,
// _ready need to latch ready and valid signals.
//////////

class TypLoadIO(NumPredOps: Int, NumSuccOps: Int, NumOuts: Int)(implicit p: Parameters) extends HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts)(new TypBundle) {
  // Node specific IO
  // GepAddr: The calculated address comming from GEP node
  val GepAddr = Flipped(Decoupled(new DataBundle))
  // Memory request
  val memReq = Decoupled(new ReadReq())
  // Memory response.
  val memResp = Input(Flipped(new ReadResp()))
}
/**
 * @brief Store Node. Implements store operations
 * @details [long description]
 *
 * @param NumPredOps [Number of predicate memory operations]
 */
class TypLoad(NumPredOps: Int,
  NumSuccOps: Int,
  NumOuts: Int,
  ID: Int,
  RouteID: Int)(implicit p: Parameters)
  extends HandShaking(NumPredOps, NumSuccOps, NumOuts, ID)(new TypBundle)(p) {

  // Set up StoreIO
  override lazy val io = IO(new TypLoadIO(NumPredOps, NumSuccOps, NumOuts))

/*=============================================
=            Register declarations            =
=============================================*/

  // OP Inputs
  val addr_R = RegInit(DataBundle.default)
  val addr_valid_R = RegInit(false.B)
  val data_R = RegInit(TypBundle.default)
  val data_valid_R = RegInit(false.B)


  // State machine
  val s_idle :: s_SENDING :: s_RECEIVING :: s_Done :: Nil = Enum(4)
  val state = RegInit(s_idle)
  val recvptr = RegInit(0.U(log2Ceil(Beats + 1).W))
  val ReqValid = RegInit(false.B)

  /*============================================
=            Predicate Evaluation            =
============================================*/

  val predicate = addr_R.predicate & IsEnable()
  val start = addr_valid_R & IsPredValid() & IsEnableValid()

  /*================================================
=            Latch inputs. Set output            =
================================================*/

  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.GepAddr.ready := ~addr_valid_R

  // ACTION: GepAddr
  io.GepAddr.ready := ~addr_valid_R
  when(io.GepAddr.fire()) {
    addr_R := io.GepAddr.bits
    addr_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := data_R
    io.Out(i).bits.predicate := predicate
  }
  // data_R.data     = buffer.
  val linebuffer = RegInit(Vec(Seq.fill(Beats)(0.U(xlen.W))))

  //  Check if address is valid and data has arrive and predecessors have completed.
  val mem_req_fire = addr_valid_R & IsPredValid()
  io.memReq.bits.address := addr_R.data
  io.memReq.bits.taskID  := addr_R.taskID | enable_R.taskID
  io.memReq.bits.RouteID := RouteID.U
  io.memReq.bits.Typ := MT_W
  io.memReq.valid := false.B

  when(start & predicate) {
    /*=============================================
  =            ACTIONS (possibly dangerous)     =
  =============================================*/

    // ACTION:  Memory request

    // ACTION: Memory Request
    // -> Send memory Requests
    when((state === s_idle) && (mem_req_fire)) {
      io.memReq.valid := true.B
      // Arbitration ready. Move on to the next word
    }

    //  ACTION: Arbitration ready
    //   <- Incoming memory arbitration
    when((state === s_idle) && io.memReq.fire()) {
      state := s_RECEIVING
    }

    //  ACTION:  <- Incoming Data
    when(state === s_RECEIVING && (io.memResp.valid === true.B) && (recvptr =/= (Beats).U)) {
      linebuffer(recvptr) := io.memResp.data
      recvptr := recvptr + 1.U
    }

    // Need to wait extra cycle for the last word to update the line buffer
    when((state === s_RECEIVING) && (recvptr === (Beats).U)) {
      data_R.data := linebuffer.asUInt
      data_R.predicate := predicate
      data_valid_R := true.B
      ValidSucc()
      ValidOut()
      state := s_Done
    }
  }.elsewhen(start & ~predicate) {
    ValidSucc()
    ValidOut()
    state := s_Done
  }
  /*===========================================
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
      // Reset data.
      data_R := TypBundle.default
      // Clear ptrs
      recvptr := 0.U
      // Clear all other state
      Reset()
      // Reset state.
      state := s_idle
    }
  }
  // Trace detail.
  override val printfSigil = "TYPLOAD" + Typ_SZ + "_" + ID + ":"
  if (log == true && (comp contains "TYPLOAD")) {
    val x = RegInit(0.U(xlen.W))
    x := x + 1.U

    verb match {
      case "high" => {}
      case "med" => {}
      case "low" => {
        printfInfo("Cycle %d : { \"Inputs\": {\"GepAddr\": %x},", x, (addr_valid_R))
        printf("\"State\": {\"State\": \"%x\", \"data_R(Valid,Data,Pred)\": \"%x,%x,%x\" },", state, data_valid_R, data_R.data, data_R.predicate)
        printf("\"Outputs\": {\"Out\": %x}", io.Out(0).fire())
        printf("}")
      }
      case everythingElse => {}
    }
  }
}

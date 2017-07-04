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

class TypStoreIO(NumPredOps: Int,
  NumSuccOps: Int,
  NumOuts: Int)(implicit p: Parameters)
  extends HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts)(new TypBundle) {
  // Node specific IO
  // GepAddr: The calculated address comming from GEP node
  val GepAddr = Flipped(Decoupled(new DataBundle))
  // Store data.
  val inData = Flipped(Decoupled(new TypBundle))
  // Memory request
  val memReq = Decoupled(new WriteReq())
  // Memory response.
  val memResp = Input(Flipped(new WriteResp()))
}

/**
 * @brief Store Node. Implements store operations
 * @details [long description]
 *
 * @param NumPredOps [Number of predicate memory operations]
 */
class TypStore(NumPredOps: Int,
  NumSuccOps: Int,
  NumOuts: Int,
  ID: Int,
  RouteID: Int)(implicit p: Parameters)
  extends HandShaking(NumPredOps, NumSuccOps, NumOuts, ID)(new TypBundle)(p) {

  // Set up StoreIO
  override lazy val io = IO(new TypStoreIO(NumPredOps, NumSuccOps, NumOuts))

  // Printf debugging
  // override val printfSigil = "Store ID: " + ID + " "

  /*=============================================
=            Register declarations            =
=============================================*/

  // OP Inputs
  val addr_R = RegInit(DataBundle.default)
  val data_R = RegInit(TypBundle.default)

  // State machine
  val s_idle :: s_SENDING :: s_RECEIVING :: s_Done :: Nil = Enum(4)
  val state = RegInit(s_idle)

  val sendptr = RegInit(0.U(log2Ceil(Beats + 1).W))
  val ReqValid = RegInit(false.B)

  /*============================================
=            Predicate Evaluation            =
============================================*/

  val predicate = addr_R.predicate & data_R.predicate & IsEnable()
  val start = addr_R.valid & data_R.valid & IsPredValid() & IsEnableValid()

  /*================================================
=            Latch inputs. Set output            =
================================================*/

  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.GepAddr.ready := ~addr_R.valid
  io.inData.ready := ~data_R.valid

  // ACTION: GepAddr
  io.GepAddr.ready := ~addr_R.valid
  when(io.GepAddr.fire()) {
    addr_R := io.GepAddr.bits
    addr_R.valid := true.B
  }

  // ACTION: inData
  when(io.inData.fire()) {
    // Latch the data
    data_R := io.inData.bits
    data_R.valid := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := data_R
    io.Out(i).bits.predicate := predicate
  }
  val buffer = data_R.data.asTypeOf(Vec(Beats, UInt(xlen.W)))
  io.memReq.valid := false.B
  when(start & predicate) {
    /*=============================================
=            ACTIONS (possibly dangerous)     =
=============================================*/

    // ACTION:  Memory request
    //  Check if address is valid and data has arrive and predecessors have completed.
    val mem_req_fire = addr_R.valid & IsPredValid() & data_R.valid
   

    // ACTION: Memory Request
    // -> Send memory Requests
    when((state === s_idle) && (mem_req_fire)) {
      io.memReq.bits.address := addr_R.data
      io.memReq.bits.node := nodeID_R
      io.memReq.bits.data := buffer(sendptr)
      io.memReq.bits.mask := 15.U
      io.memReq.bits.RouteID := RouteID.U
      io.memReq.valid := true.B
      // Arbitration ready. Move on to the next word
      when(io.memReq.fire()) {
        sendptr := sendptr + 1.U
        // If last word then move to next state. 
        when(sendptr === (Beats - 1).U) {
          state := s_RECEIVING
        }
      }
    }
    //  ACTION:  <- Incoming Data
    when(state === s_RECEIVING && io.memResp.valid) {
      // Set output to valid
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
      sendptr := 0.U
      // Clear all other state
      Reset()
      // Reset state.
      state := s_idle
    }
  }
  // Trace detail.
   override val printfSigil = "TYPSTORE" + Typ_SZ + "_" + ID + ":"
  if (log == true) {
    val x = RegInit(0.U(xlen.W))
    x     := x + 1.U
  

    verb match {
      case "high"  => { }
      case "med"   => { }
      case "low"   => {
        printfInfo("Cycle %d : { \"Inputs\": {\"GepAddr\": %x, \"inData\": %x },\n",x, (addr_R.valid),(data_R.valid))
        printf("\"State\": {\"State\": %x}",state)
        printf("}")
       }
    }
  }
}

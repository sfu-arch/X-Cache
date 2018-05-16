package node

import chisel3.{RegInit, _}
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

class StoreIO(NumPredOps: Int,
              NumSuccOps: Int,
              NumOuts: Int)(implicit p: Parameters)
            extends HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts)(new DataBundle) {
  // Node specific IO
  // GepAddr: The calculated address comming from GEP node
  val GepAddr = Flipped(Decoupled(new DataBundle))
  // Store data.
  val inData = Flipped(Decoupled(new DataBundle))
  // Memory request
  val memReq = Decoupled(new WriteReq())
  // Memory response.
  val memResp = Input(Flipped(new WriteResp()))
}

/**
  * @brief Store Node. Implements store operations
  * @details [long description]
  * @param NumPredOps [Number of predicate memory operations]
  */
class UnTypStore(NumPredOps: Int,
                 NumSuccOps: Int,
                 NumOuts: Int,
                 Typ: UInt = MT_W, ID: Int, RouteID: Int)
                (implicit p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File)
  extends HandShaking(NumPredOps, NumSuccOps, NumOuts, ID)(new DataBundle)(p) {

  // Set up StoreIO
  override lazy val io = IO(new StoreIO(NumPredOps, NumSuccOps, NumOuts))
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*=============================================
  =            Register declarations            =
  =============================================*/

  // OP Inputs
  val addr_R = RegInit(DataBundle.default)
  val data_R = RegInit(DataBundle.default)
  val addr_valid_R = RegInit(false.B)
  val data_valid_R = RegInit(false.B)

  // State machine
  val s_idle :: s_RECEIVING :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)

  val ReqValid = RegInit(false.B)

  /*============================================
  =            Predicate Evaluation            =
  ============================================*/

//  val predicate = IsEnable()
//  val start = addr_valid_R & data_valid_R & IsPredValid() & IsEnableValid()

  /*================================================
  =            Latch inputs. Set output            =
  ================================================*/

  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.GepAddr.ready := ~addr_valid_R
  io.inData.ready := ~data_valid_R

  // ACTION: GepAddr
  io.GepAddr.ready := ~addr_valid_R
  when(io.GepAddr.fire()) {
    addr_R := io.GepAddr.bits
    addr_valid_R := true.B
  }

  // ACTION: inData
  when(io.inData.fire()) {
    // Latch the data
    data_R := io.inData.bits
    data_valid_R := true.B
  }

  val predicate = addr_R.predicate & data_R.predicate

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := data_R
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := addr_R.taskID
  }
  // Outgoing Address Req ->
  io.memReq.bits.address := addr_R.data
  io.memReq.bits.data := data_R.data
  io.memReq.bits.Typ := Typ
  io.memReq.bits.RouteID := RouteID.U
  io.memReq.bits.mask := 15.U
  io.memReq.valid := false.B

  /*=============================================
  =            ACTIONS (possibly dangerous)     =
  =============================================*/
  val mem_req_fire = addr_valid_R & IsPredValid() & data_valid_R
  val complete = IsSuccReady() & IsOutReady()


  switch(state) {
    is(s_idle) {
      when(enable_valid_R) {
        when(enable_R) {
          when(mem_req_fire && data_valid_R && addr_valid_R && IsPredValid()) {
            when(predicate) {
              io.memReq.valid := true.B
              when(io.memReq.ready) {
                state := s_RECEIVING
              }
            }.otherwise {
              state := s_RECEIVING
            }
          }
        }.otherwise {

          addr_R := DataBundle.default
          addr_valid_R := false.B

          // Reset data.
          data_R := DataBundle.default
          data_valid_R := false.B

          // Clear all other state
          Reset()

          // Reset state.
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": restarted @ %d\n", cycleCount)

        }
      }
    }
    is(s_RECEIVING) {
      when(io.memResp.valid || ~predicate) {
        ValidSucc()
        ValidOut()
        state := s_Done
      }
    }
    is(s_Done) {
      when(complete) {
        // Clear all the valid states.
        // Reset address
        addr_R := DataBundle.default
        addr_valid_R := false.B
        // Reset data.
        data_R := DataBundle.default
        data_valid_R := false.B
        // Clear all other state
        Reset()
        // Reset state.
        state := s_idle
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
      }
    }
  }

}

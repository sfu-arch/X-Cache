package dandelion.node

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces._
import utility.Constants._
import utility.UniformPrintfs


// Design Doc
//////////
/// DRIVER ///
/// 1. Memory response only available atleast 1 cycle after request
//  2. Handshaking has to be done with registers.
// @todo : This node will only receive one word. To handle doubles. Change handshaking logic
//////////

class LoadIO(NumPredOps: Int,
             NumSuccOps: Int,
             NumOuts: Int)(implicit p: Parameters)
  extends HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts)(new DataBundle) {
  // GepAddr: The calculated address comming from GEP node
  val GepAddr = Flipped(Decoupled(new DataBundle))
  // Memory request
  val memReq = Decoupled(new ReadReq())
  // Memory response.
  val memResp = Input(Flipped(new ReadResp()))

  override def cloneType = new LoadIO(NumPredOps, NumSuccOps, NumOuts).asInstanceOf[this.type]
}

/**
  * @brief Load Node. Implements load operations
  * @note [load operations can either reference values in a scratchpad or cache]
  * @param NumPredOps [Number of predicate memory operations]
  */
class UnTypLoad(NumPredOps: Int,
                NumSuccOps: Int,
                NumOuts: Int,
                Typ: UInt = MT_D,
                ID: Int,
                RouteID: Int)
               (implicit p: Parameters,
                name: sourcecode.Name,
                file: sourcecode.File)
  extends HandShaking(NumPredOps, NumSuccOps, NumOuts, ID)(new DataBundle)(p) {

  override lazy val io = IO(new LoadIO(NumPredOps, NumSuccOps, NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "


  /*=============================================
  =            Registers                        =
  =============================================*/
  // OP Inputs
  val addr_R = RegInit(DataBundle.default)
  val addr_valid_R = RegInit(false.B)

  // Memory Response
  val data_R = RegInit(DataBundle.default)
  val data_valid_R = RegInit(false.B)

  // State machine
  val s_idle :: s_RECEIVING :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)


  /*================================================
  =            Latch inputs. Wire up output            =
  ================================================*/

  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.GepAddr.ready := ~addr_valid_R
  when(io.GepAddr.fire()) {
    addr_R := io.GepAddr.bits
    addr_valid_R := true.B
  }

  /*============================================
  =            Predicate Evaluation            =
  ============================================*/

  val complete = IsSuccReady() && IsOutReady()
  val predicate = addr_R.predicate && enable_R.control
  val mem_req_fire = addr_valid_R && IsPredValid()


  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := data_R
  }

  io.memReq.valid := false.B
  io.memReq.bits.address := addr_R.data
  io.memReq.bits.Typ := Typ
  io.memReq.bits.RouteID := RouteID.U
  io.memReq.bits.taskID := addr_R.taskID

  // Connect successors outputs to the enable status
  when(io.enable.fire()) {
    succ_bundle_R.foreach(_ := io.enable.bits)
  }
  /*=============================================
  =            ACTIONS (possibly dangerous)     =
  =============================================*/


  switch(state) {
    is(s_idle) {
      when(enable_valid_R && mem_req_fire) {
        when(enable_R.control && predicate) {

          io.memReq.valid := true.B

          when(io.memReq.fire) {
            state := s_RECEIVING

            if(log){
              printf("[LOG] " + "[" + module_name + "] [TID->%d] [LOAD] " + node_name + ": Memreq fired @ %d, Addr:%d\n",
                enable_R.taskID, cycleCount, io.memReq.bits.address)
            }
          }
        }.otherwise {
          data_R := DataBundle.deactivate()
          ValidSucc()
          ValidOut()
          // Completion state.
          state := s_Done
        }
      }
    }
    is(s_RECEIVING) {
      when(io.memResp.valid) {

        // Set data output registers
        data_R := DataBundle.active(io.memResp.data)

        ValidSucc()
        ValidOut()

        // Completion state.
        state := s_Done

        addr_valid_R := false.B

        if(log){
          printf("[LOG] " + "[" + module_name + "] [TID->%d] [LOAD] "
            + node_name + ": Memresp fired @ %d, Value: %d\n",
            enable_R.taskID, cycleCount, io.memResp.data)
        }

      }
    }
    is(s_Done) {
      when(complete) {
        Reset()
        state := s_idle
        if (log) {
          printf("[LOG] " + "[" + module_name + "] [TID->%d] [LOAD] " + node_name + ": Output fired @ %d, Address:%d, Value: %d\n",
            enable_R.taskID, cycleCount, addr_R.data, data_R.data)
        }
      }
    }
  }
}

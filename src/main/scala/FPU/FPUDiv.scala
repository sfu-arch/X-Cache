package FPU

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}
import config._
import interfaces._
import muxes._
import util._
import node._
import FType._
import chisel3.{RegInit, _}
import org.scalacheck.Prop.False
import utility.Constants._
import utility.UniformPrintfs

// Design Doc
//////////
/// DRIVER ///
/// 1. Memory response only available atleast 1 cycle after request
//  2. Need registers for pipeline handshaking e.g., _valid,
// _ready need to latch ready and valid signals.
//////////

class FPDivSqrtIO(NumOuts: Int, argTypes: Seq[Int])(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {
  // Divisor or Sqrt
  val a = Flipped(Decoupled(new DataBundle))
  // Dividend
  val b = Flipped(Decoupled(new DataBundle))
  // Memory request
  val FUReq = Decoupled(new FUReq(argTypes))
  // Memory response.
  val FUResp = Input(Flipped(new FUResp()))
}

/**
  * @brief Store Node. Implements store operations
  * @details [long description]
  * @param NumPredOps [Number of predicate memory operations]
  */
class FPDivSqrtNode(NumOuts: Int, ID: Int, opCode: String)
                 (t: FType)
                 (implicit p: Parameters,
                  name: sourcecode.Name,
                  file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new FPDivSqrtIO(NumOuts, List(xlen,xlen,1)))

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*=============================================
  =            Register declarations            =
  =============================================*/

  // Dividend or Sqrt
  val a_R = RegInit(DataBundle.default)
  val a_valid_R = RegInit(false.B)

  // Divisor
  val b_R = RegInit(DataBundle.default)
  val b_valid_R = RegInit(false.B)

  // FU Response
  val data_R = RegInit(DataBundle.default)
  val data_valid_R = RegInit(false.B)


  // State machine
  val s_idle :: s_RECEIVING :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)


  /*============================================
  =            Predicate Evaluation            =
  ============================================*/

//  val predicate = IsEnable()
//  val start = a_valid_R & data_valid_R & IsPredValid() & IsEnableValid()

/*   ================================================
  =            Latch inputs. Set output            =
  ================================================  */

  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.a.ready := ~a_valid_R
  io.b.ready := ~b_valid_R

  // ACTION: A
  when(io.a.fire()) {
    a_R := io.a.bits
    a_valid_R := true.B
  }

  // ACTION: B
  when(io.b.fire()) {
    // Latch the data
    b_R := io.b.bits
    b_valid_R := true.B
  }

  val predicate = a_R.predicate & b_R.predicate

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := data_R
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := a_R.taskID
  }

  // Outgoing FU Req ->
  io.FUReq.bits.data("field0").data  :=  a_R.data
  io.FUReq.bits.data("field1").data  :=  b_R.data
  io.FUReq.bits.data("field0").predicate  :=  true.B
  io.FUReq.bits.data("field1").predicate  :=  true.B
  io.FUReq.bits.data("field0").taskID  :=  a_R.taskID
  io.FUReq.bits.data("field1").taskID  :=  b_R.taskID


  require((opCode == "DIV" || opCode == "SQRT"), "DIV or SQRT required")
  val DivOrSqrt = opCode match {
  case "DIV" => false.B
  case "SQRT" => true.B
  }
  io.FUReq.bits.data("field2").data  := DivOrSqrt
  io.FUReq.bits.data("field2").predicate := true.B
  io.FUReq.bits.data("field2").taskID  := 1.U

  io.FUReq.bits.RouteID := ID.U
  io.FUReq.valid := false.B

  /*=============================================
  =            ACTIONS (possibly dangerous)     =
  =============================================*/
  val FU_req_fire = a_valid_R & b_valid_R
  val complete = IsOutReady()
  switch(state) {
    is(s_idle) {
      when(enable_valid_R) {
        when(enable_R) {
          when(FU_req_fire) {
            when(predicate){
              io.FUReq.valid := true.B
              when(io.FUReq.ready) {
                state := s_RECEIVING
                printf("[LOG] " + "[" + module_name + "] " + node_name + ": Receiving @ %d\n", cycleCount)
              }
            }.otherwise {
              state :=s_RECEIVING
              printf("[LOG] " + "[" + module_name + "] " + node_name + ": No ACTION @ %d\n", cycleCount)
            }
          }
        }.otherwise {

          // Reset a.
          a_R := DataBundle.default
          a_valid_R := false.B

          // Reset b.
          b_R := DataBundle.default
          b_valid_R := false.B

          // Reset data
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
      when(io.FUResp.valid || ~predicate) {
        // Set data output registers
        data_R.data := io.FUResp.data
        data_R.predicate := predicate
        ValidOut()
        state := s_Done
      }
    }
    is(s_Done) {
      when(complete) {
        // Clear all the valid states.

        // Reset data
        data_R := DataBundle.default
        data_valid_R := false.B

        // Reset address
        a_R := DataBundle.default
        a_valid_R := false.B

        // Reset b.
        b_R := DataBundle.default
        b_valid_R := false.B

        // Clear all other state
        Reset()

        // Reset state.
        state := s_idle
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
      }
    }
  }

}

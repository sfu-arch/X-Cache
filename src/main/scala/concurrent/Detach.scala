package dataflow



import chisel3._
import chisel3.util._
import chisel3.Module
import config._
import interfaces._
import config._
import interfaces._
import muxes._
import util._
import node._

class DetachIO()(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 3)(new ControlBundle)(p)
{
//  val incr = Decoupled(new AckBundle)
}

class Detach(ID: Int) (implicit p: Parameters)
  extends HandShakingCtrlNPS(NumOuts = 3, ID)(p) {
  override lazy val io = IO(new DetachIO()(p))
  // Printf debugging
  override val printfSigil = "Node (DET) ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  val s_idle :: s_OUTPUT :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val latchedEnable = RegInit(ControlBundle.default)
  val start = IsEnableValid() // Registered version of io.enable.fire

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/
  when (io.enable.fire()) {
    latchedEnable := io.enable.bits
  }
  // Wire up Outputs
  for (i <- 0 until 3) {
    io.Out(i).bits := latchedEnable  // current task path
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & (state === s_idle)) {
    state := s_OUTPUT
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  val out_ready_W = out_ready_R.asUInt.andR
  when(out_ready_W && (state === s_OUTPUT)) {
    Reset()
    //Reset state
    state := s_idle
    when(latchedEnable.control) {printfInfo("Output fired")}
  }

}

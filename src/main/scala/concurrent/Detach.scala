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
import utility.UniformPrintfs

class DetachIO()(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 3)(new ControlBundle)(p)
{
//  val incr = Decoupled(new AckBundle)
}

class Detach(ID: Int, Desc : String = "Detach") (implicit p: Parameters)
  extends HandShakingCtrlNPS(NumOuts = 3, ID)(p) {
  override lazy val io = IO(new DetachIO()(p))
  // Printf debugging
  override val printfSigil = "Node (DET) ID: " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)
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
    when (latchedEnable.control) {printf("[LOG] " + Desc+": Output fired @ %d\n",cycleCount)}
  }

}

class DetachFastIO()(implicit p: Parameters) extends CoreBundle {
  // Predicate enable
  val enable = Flipped(Decoupled(new ControlBundle))
  // Output IO
  val Out = Vec(3, Decoupled(new ControlBundle))
}

class DetachFast(ID: Int, Desc : String = "Detach") (implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

  val io = IO(new DetachFastIO()(p))
  // Printf debugging
  override val printfSigil = "Node (DET) ID: " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)
  /*===========================================*
   *            Registers                      *
   *===========================================*/

  io.Out(0).bits.control := io.enable.bits.control
  io.Out(1).bits.control := io.enable.bits.control
  io.Out(2).bits.control := io.enable.bits.control
  io.Out(0).bits.taskID := io.enable.bits.taskID
  io.Out(1).bits.taskID := io.enable.bits.taskID
  io.Out(2).bits.taskID := io.enable.bits.taskID
  when (io.Out(0).ready && io.Out(1).ready && io.Out(2).ready) {
    io.Out(0).valid := io.enable.valid
    io.Out(1).valid := io.enable.valid
    io.Out(2).valid := io.enable.valid
    io.enable.ready := true.B
  }.otherwise{
    io.Out(0).valid := false.B
    io.Out(1).valid := false.B
    io.Out(2).valid := false.B
    io.enable.ready := false.B
  }

}

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
  extends HandShakingIONPS(NumOuts = 3)(new ControlBundle)(p) {
  //  val incr = Decoupled(new AckBundle)
  override def cloneType = new DetachIO().asInstanceOf[this.type]
}

class Detach(ID: Int)
            (implicit p: Parameters,
             name: sourcecode.Name,
             file: sourcecode.File)
  extends HandShakingCtrlNPS(NumOuts = 3, ID)(p) {
  override lazy val io = IO(new DetachIO()(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
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
  when(io.enable.fire()) {
    latchedEnable <> io.enable.bits
  }
  // Wire up Outputs
  for (i <- 0 until 3) {
    io.Out(i).bits <> latchedEnable // current task path
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
    when(latchedEnable.control) {
      if(log){
        printf("[LOG] " + "[" + module_name + "] [TID->%d] [Detach]" +
          node_name + ": Output fired @ %d\n", latchedEnable.taskID,cycleCount)
      }
    }
  }

}

class DetachFastIO()(implicit p: Parameters) extends CoreBundle {
  // Predicate enable
  val enable = Flipped(Decoupled(new ControlBundle))
  // Output IO
  val Out = Vec(3, Decoupled(new ControlBundle))
}


@deprecated("DetachFast is deprecated. It doesn't handle all the corner cases. DetachFastNode", "dataflow-lib 1.0")
class DetachFast(ID: Int)(implicit val p: Parameters,
                          name: sourcecode.Name,
                          file: sourcecode.File)
  extends Module with CoreParams with UniformPrintfs {

  val io = IO(new DetachFastIO()(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  /*===========================================*
   *            Registers                      *
   *===========================================*/

  io.Out(0).bits.control := io.enable.bits.control
  io.Out(1).bits.control := io.enable.bits.control
  io.Out(2).bits.control := io.enable.bits.control
  io.Out(0).bits.taskID := io.enable.bits.taskID
  io.Out(1).bits.taskID := io.enable.bits.taskID
  io.Out(2).bits.taskID := io.enable.bits.taskID
  when(io.Out(0).ready && io.Out(1).ready && io.Out(2).ready) {
    io.Out(0).valid := io.enable.valid
    io.Out(1).valid := io.enable.valid
    io.Out(2).valid := io.enable.valid
    io.enable.ready := true.B
  }.otherwise {
    io.Out(0).valid := false.B
    io.Out(1).valid := false.B
    io.Out(2).valid := false.B
    io.enable.ready := false.B
  }

}


class DetachNode(ID: Int, NumOuts: Int = 1)
                (implicit p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File)
  extends HandShakingCtrlNPS(NumOuts = NumOuts, ID)(p) {
  override lazy val io = IO(new HandShakingIONPS(NumOuts = NumOuts)(new ControlBundle)(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_OUTPUT :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    *
    * @note data_R value is equale to predicate bit
    */
  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> enable_R
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(enable_valid_R & (state === s_idle)) {
    state := s_OUTPUT
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  when(out_ready_W & (state === s_OUTPUT)) {
    Reset()

    state := s_idle
    printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d\n",enable_R.taskID, cycleCount)


  }

}




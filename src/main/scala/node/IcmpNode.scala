package node

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
import utility.UniformPrintfs

class IcmpNodeIO(NumOuts: Int)
                (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {
  // LeftIO: Left input data for computation
  val LeftIO = Flipped(Decoupled(new DataBundle))

  // RightIO: Right input data for computation
  val RightIO = Flipped(Decoupled(new DataBundle))
}

class IcmpNode(NumOuts: Int, ID: Int, opCode: String)
              (sign: Boolean)
              (implicit p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File)

  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new ComputeNodeIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val left_R = RegInit(DataBundle.default)
  val left_valid_R = RegInit(false.B)

  // Right Input
  val right_R = RegInit(DataBundle.default)
  val right_valid_R = RegInit(false.B)

  val task_ID_R = RegNext(next = enable_R.taskID)

  // Output register
  val out_data_R = RegInit(DataBundle.default)

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = left_R.predicate & right_R.predicate //& IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  val FU = Module(new UCMP(xlen, opCode))
  FU.io.in1 := left_R.data
  FU.io.in2 := right_R.data

  io.LeftIO.ready := ~left_valid_R
  when(io.LeftIO.fire()) {
    left_R <> io.LeftIO.bits
    left_valid_R := true.B
  }

  io.RightIO.ready := ~right_valid_R
  when(io.RightIO.fire()) {
    right_R <> io.RightIO.bits
    right_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := out_data_R
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(left_valid_R && right_valid_R) {
          ValidOut()
          when(enable_R.control) {
            out_data_R.data := FU.io.out
            out_data_R.predicate := predicate
            out_data_R.taskID := left_R.taskID | right_R.taskID
          }
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset data
        left_R := DataBundle.default
        right_R := DataBundle.default
        left_valid_R := false.B
        right_valid_R := false.B
        //Reset state
        state := s_IDLE
        out_data_R.predicate := false.B
        //Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", task_ID_R, cycleCount, FU.io.out)
      }
    }
  }

}


class IcmpFastNode(NumOuts: Int, ID: Int, opCode: String)
                  (sign: Boolean)
                  (implicit val p: Parameters,
                   name: sourcecode.Name,
                   file: sourcecode.File)
  extends Module with CoreParams with UniformPrintfs {

  val io = IO(new Bundle {
    //Control Signal
    val enable = Flipped(Decoupled(new ControlBundle))

    //Input data
    val LeftIO = Flipped(Decoupled(new DataBundle()))
    val RightIO = Flipped(Decoupled(new DataBundle()))

    val Out = Decoupled(new DataBundle)
  })

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val left_R = RegInit(DataBundle.default)
  val left_valid_R = RegInit(false.B)

  // Right Input
  val right_R = RegInit(DataBundle.default)
  val right_valid_R = RegInit(false.B)

  val enable_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)

  val task_ID_R = RegNext(next = enable_R.taskID)

  val predicate = left_R.predicate & right_R.predicate & enable_R.control

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  val s_idle :: s_fire :: Nil = Enum(2)
  val state = RegInit(s_idle)

  val FU = Module(new UCMP(xlen, opCode))
  FU.io.in1 := left_R.data
  FU.io.in2 := right_R.data

  io.LeftIO.ready := ~left_valid_R
  when(io.LeftIO.fire()) {
    left_R <> io.LeftIO.bits
    left_valid_R := true.B
  }

  io.RightIO.ready := ~right_valid_R
  when(io.RightIO.fire()) {
    right_R <> io.RightIO.bits
    right_valid_R := true.B
  }

  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()){
    enable_R <> io.enable.bits
    enable_valid_R := true.B
  }

  // Defalut values
  io.Out.valid := false.B
  io.Out.bits.taskID := 0.U
  io.Out.bits.data := FU.io.out
  io.Out.bits.predicate := false.B


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  switch(state) {
    is(s_idle) {
      io.Out.bits.data := FU.io.out
      io.Out.bits.taskID := task_ID_R
      io.Out.bits.predicate := predicate

      when(enable_valid_R || io.enable.fire) {
        when((left_valid_R || io.LeftIO.fire) && (right_valid_R || io.RightIO.fire)) {

          io.Out.valid := true.B

          when(io.Out.fire) {

            left_R := DataBundle.default
            left_valid_R := false.B

            right_R := DataBundle.default
            right_valid_R := false.B

            enable_R := ControlBundle.default
            enable_valid_R := false.B

            state := s_idle
          }.otherwise {
            state := s_fire
          }

          printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] "
            + node_name + ": Output fired @ %d, Value: %d\n",
            task_ID_R, cycleCount, FU.io.out)

        }
      }
    }

    is(s_fire) {
      io.Out.valid := true.B
      when(io.Out.fire) {
        left_R := DataBundle.default
        left_valid_R := false.B

        right_R := DataBundle.default
        right_valid_R := false.B

        enable_R := ControlBundle.default
        enable_valid_R := false.B

        state := s_idle
      }
    }
  }

}


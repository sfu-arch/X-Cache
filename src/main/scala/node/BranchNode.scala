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


/**
  * @note
  * For Conditional Branch output is always equal to two!
  * Since your branch output wire to two different basic block only
  */

class CBranchNodeIO(NumOuts: Int = 2)
                   (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new ControlBundle) {

  // RightIO: Right input data for computation
  val CmpIO = Flipped(Decoupled(new DataBundle))
}

class CBranchNode(ID: Int)
                 (implicit p: Parameters,
                  name: sourcecode.Name,
                  file: sourcecode.File)
  extends HandShakingCtrlNPS(2, ID)(p) {
  override lazy val io = IO(new CBranchNodeIO())
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val cmp_R = RegInit(DataBundle.default)
  val cmp_valid_R = RegInit(false.B)


  // Output wire
  //  val data_out_w = WireInit(VecInit(Seq.fill(2)(false.B)))
  val data_out_R = RegInit(VecInit(Seq.fill(2)(false.B)))

  val s_IDLE :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val start = cmp_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register
  //val pred_R = RegInit(init = false.B)


  io.CmpIO.ready := ~cmp_valid_R
  when(io.CmpIO.fire()) {
    cmp_R <> io.CmpIO.bits
    cmp_valid_R := true.B
  }

  // Wire up Outputs
  io.Out(0).bits.control := data_out_R(0)
  io.Out(0).bits.taskID := enable_R.taskID
  io.Out(1).bits.control := data_out_R(1)
  io.Out(1).bits.taskID := enable_R.taskID

  /*============================================*
   *            STATE MACHINE                   *
   *============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    */

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R && (~enable_R.control).toBool) {
        state := s_COMPUTE
        ValidOut()
      }.elsewhen(io.CmpIO.fire()) {
        state := s_LATCH
      }
    }
    is(s_LATCH) {
      state := s_COMPUTE
      when(enable_valid_R) {
        when(enable_R.control) {
          data_out_R(0) := cmp_R.data.asUInt.orR
          data_out_R(1) := ~cmp_R.data.asUInt.orR
        }
        ValidOut()
      }
    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Restarting
        cmp_R := DataBundle.default
        cmp_valid_R := false.B

        // Reset output
        data_out_R := VecInit(Seq.fill(2)(false.B))
        //Reset state
        state := s_IDLE

        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n",enable_R.taskID, cycleCount, data_out_R.asUInt())
      }
    }
  }

}

class CBranchFastIO()(implicit p: Parameters) extends CoreBundle {
  // Predicate enable
  val enable = Flipped(Decoupled(new ControlBundle))
  // Comparator input
  val CmpIO = Flipped(Decoupled(new DataBundle))
  // Output IO
  val Out = Vec(2, Decoupled(new ControlBundle))
}

class CBranchFastNode(ID: Int)
                     (implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

  // Printf debugging
  override val printfSigil = "Node (UBR) ID: " + ID + " "
  val io = IO(new CBranchFastIO()(p))
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  io.Out(0).bits.control := io.enable.bits.control && io.CmpIO.bits.data(0)
  io.Out(1).bits.control := io.enable.bits.control && !io.CmpIO.bits.data(0)
  io.Out(0).bits.taskID := io.enable.bits.taskID
  io.Out(1).bits.taskID := io.enable.bits.taskID

  when(io.Out(0).ready && io.Out(1).ready && io.CmpIO.valid && io.enable.valid) {
    io.Out(0).valid := true.B
    io.Out(1).valid := true.B
    io.CmpIO.ready := true.B
    io.enable.ready := true.B
  }.otherwise {
    io.Out(0).valid := false.B
    io.Out(1).valid := false.B
    io.CmpIO.ready := false.B
    io.enable.ready := false.B
  }


}

class UBranchNode(ID: Int, NumOuts: Int = 1)
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

  val s_idle :: s_OUTPUT :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

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

  val out_ready_W = out_ready_R.asUInt.andR

  switch(state){
    is(s_idle){
      when(io.enable.fire()){
        state := s_OUTPUT
        ValidOut()
      }
    }
    is(s_OUTPUT){
      when(out_ready_W){
        state := s_idle
        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d\n", enable_R.taskID, cycleCount)
      }
    }
  }

}

class UBranchEndNode(ID: Int, NumOuts: Int = 1)
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

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start = IsEnableValid()

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
    io.Out(i).bits.control := predicate
    io.Out(i).bits.taskID := 0.U

  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & (state === s_idle)) {
    when(predicate) {
      state := s_OUTPUT
      ValidOut()
    }.otherwise {
      enable_valid_R := false.B
    }
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  when(out_ready_W & (state === s_OUTPUT)) {
    Reset()

    state := s_idle
    printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)


  }

}

class UBranchFastIO()(implicit p: Parameters) extends CoreBundle {
  // Predicate enable
  val enable = Flipped(Decoupled(new ControlBundle))
  // Output IO
  val Out = Vec(1, Decoupled(new ControlBundle))
}

class UBranchFastNode(ID: Int)
                     (implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

  // Printf debugging
  override val printfSigil = "Node (UBR) ID: " + ID + " "
  val io = IO(new UBranchFastIO()(p))
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  io.Out(0) <> io.enable


}


/**
  * @note
  * For Conditional Branch output is always equal to two!
  * Since your branch output wire to two different basic block only
  */

class CompareBranchIO()(implicit p: Parameters) extends CoreBundle {
  // Predicate enable
  val enable = Flipped(Decoupled(new ControlBundle))

  // LeftIO: Left input data for computation
  val LeftIO = Flipped(Decoupled(new DataBundle))
  // RightIO: Right input data for computation
  val RightIO = Flipped(Decoupled(new DataBundle))

  // Output IO
  val Out = Vec(2, Decoupled(new ControlBundle))
}

class CompareBranchNode(ID: Int, opCode: String)
                       (implicit val p: Parameters,
                        name: sourcecode.Name,
                        file: sourcecode.File) extends Module with CoreParams with UniformPrintfs {
  // Defining IOs
  val io = IO(new CompareBranchIO())
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // Enable Input
  val enable_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)

  // Left Input
  val left_R = RegInit(DataBundle.default)
  val left_valid_R = RegInit(false.B)

  // Right Input
  val right_R = RegInit(DataBundle.default)
  val right_valid_R = RegInit(false.B)

  // Output Handshaking
  val out_ready_R = RegInit(VecInit(Seq.fill(2)(false.B)))
  val out_valid_R = RegInit(VecInit(Seq.fill(2)(false.B)))


  val FU = Module(new UCMP(xlen, opCode))
  FU.io.in1 := left_R.data
  FU.io.in2 := right_R.data

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_R <> io.enable.bits
    enable_valid_R := true.B
  }

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
  when(enable_valid_R) {
    io.Out(0).bits.control := FU.io.out
    io.Out(1).bits.control := ~FU.io.out
  }.otherwise {
    io.Out(0).bits.control := false.B
    io.Out(1).bits.control := false.B
  }

  io.Out(0).bits.taskID := 0.U
  out_ready_R(0) := io.Out(0).ready
  io.Out(0).valid := out_valid_R(0)
  io.Out(1).bits.taskID := 0.U
  out_ready_R(1) := io.Out(1).ready
  io.Out(1).valid := out_valid_R(1)

  /*============================================*
   *            STATE MACHINE                   *
   *============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    */

  when(state === s_COMPUTE){
    assert((left_R.taskID === enable_R.taskID) && (right_R.taskID === enable_R.taskID), "Control channel should be in sync with data channel!")
  }

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when((~enable_R.control).toBool) {

          enable_R := ControlBundle.default
          enable_valid_R := false.B

          left_R := DataBundle.default
          left_valid_R := false.B

          right_R := DataBundle.default
          right_valid_R := false.B

          out_ready_R := VecInit(Seq.fill(2)(false.B))
          out_valid_R := VecInit(Seq.fill(2)(false.B))

          printf("[LOG] " + "[" + module_name + "] [TID-> %d] "
            + node_name + ": Not predicated value -> reset\n", enable_R.taskID)

        }.elsewhen((io.LeftIO.fire() || left_valid_R) && (io.RightIO.fire() || right_valid_R)) {
          out_valid_R := VecInit(Seq.fill(2)(true.B))
          state := s_COMPUTE
        }
      }
    }
    is(s_COMPUTE) {
      when(out_ready_R.asUInt.andR) {
        enable_R := ControlBundle.default
        enable_valid_R := false.B

        left_R := DataBundle.default
        left_valid_R := false.B

        right_R := DataBundle.default
        right_valid_R := false.B

        out_ready_R := VecInit(Seq.fill(2)(false.B))
        out_valid_R := VecInit(Seq.fill(2)(false.B))

        state := s_IDLE

        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name +
          ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, FU.io.out.asUInt())
      }
    }
  }

}





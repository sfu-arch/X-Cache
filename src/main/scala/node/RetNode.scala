package node

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec}
import utility.UniformPrintfs

import config._
import interfaces._
import muxes._
import util._

class RetNodeIO(val NumPredIn: Int, val retTypes: Seq[Int])(implicit p: Parameters)
  extends Bundle {
  val enable = Flipped(Decoupled(new ControlBundle()))
  val predicateIn = Flipped(Vec(NumPredIn, Decoupled(new ControlBundle()(p))))
  val In = Flipped(new VariableDecoupledData(retTypes)) // Data to be returned
  val Out = Decoupled(new Call(retTypes)) // Returns to calling block(s)
}

class RetNode(NumPredIn: Int = 0, retTypes: Seq[Int], ID: Int)
             (implicit val p: Parameters,
              name: sourcecode.Name,
              file: sourcecode.File) extends Module
  with CoreParams with UniformPrintfs {

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override lazy val io = IO(new RetNodeIO(NumPredIn, retTypes)(p))
  override val printfSigil = module_name + ": " + node_name + ID + " "
  //  override val printfSigil = "Node (Ret) ID: " + ID + " "


  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  // Defining states
  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  // Enable
  val enable_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)

  val task_ID_R = RegNext(next = enable_R.taskID)

  // Data Inputs
  val inputReady = RegInit(VecInit(Seq.fill(retTypes.length + NumPredIn + 1)(true.B)))
  val in_data_valid_R = RegInit(VecInit(Seq.fill(retTypes.length)(false.B)))

  // Output registers
  val outputReg = RegInit(0.U.asTypeOf(io.Out))
  val out_ready_R = RegInit(false.B)
  val out_valid_R = RegInit(false.B)


  // Latching enable signal
  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_valid_R := io.enable.valid
    enable_R <> io.enable.bits
  }

  // Latching input data
  for (i <- retTypes.indices) {
    io.In.data(s"field$i").ready := ~in_data_valid_R(i)
    when(io.In.data(s"field$i").fire()) {
      outputReg.bits.data(s"field$i") <> io.In.data(s"field$i").bits
      in_data_valid_R(i) := true.B
    }
  }

  for (i <- (retTypes.length + 1) until (retTypes.length + 1 + NumPredIn)) {
    io.predicateIn(i - (retTypes.length + 1)).ready := inputReady(i)
  }

  when(io.Out.fire()) {
    out_ready_R := io.Out.ready
    out_valid_R := false.B
  }

  io.Out.bits := outputReg.bits
  io.Out.valid := out_valid_R

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when((~enable_R.control).toBool) {

          out_ready_R := false.B
          enable_valid_R := false.B

          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Not predicated value -> reset\n")

          state := s_IDLE

          for (i <- retTypes.indices) {
            in_data_valid_R(i) := false.B
          }
          state := s_IDLE
        }.elsewhen(in_data_valid_R.asUInt.andR) {
          out_valid_R := true.B
          state := s_COMPUTE
        }
      }
    }
    is(s_COMPUTE) {
      when(out_ready_R) {
        for (i <- retTypes.indices) {
          in_data_valid_R(i) := false.B
        }

        out_valid_R := false.B
        enable_valid_R := false.B
        out_ready_R := false.B

        state := s_IDLE
        printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", task_ID_R, cycleCount, outputReg.bits.data(s"field0").data)
      }
    }
  }

}


class RetNodeNewIO(val retTypes: Seq[Int])(implicit p: Parameters)
  extends Bundle {
  val enable = Flipped(Decoupled(new ControlBundle()))
  val In = Flipped(new VariableDecoupledData(retTypes)) // Data to be returned
  val Out = Decoupled(new Call(retTypes)) // Returns to calling block(s)
}

class RetNodeNew(retTypes: Seq[Int], ID: Int)
                (implicit val p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File) extends Module
  with CoreParams with UniformPrintfs {

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override lazy val io = IO(new RetNodeNewIO(retTypes)(p))
  override val printfSigil = module_name + ": " + node_name + ID + " "


  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  // Defining states
  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  // Enable signals
  val enable_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)

  // Data Inputs
  val in_data_valid_R = RegInit(VecInit(Seq.fill(retTypes.length)(false.B)))

  // Output registers
  val output_R = RegInit(0.U.asTypeOf(io.Out))
  val out_ready_R = RegInit(false.B)
  val out_valid_R = RegInit(false.B)


  // Latching enable signal
  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_valid_R := io.enable.valid
    enable_R <> io.enable.bits
  }

  // Latching input data
  for (i <- retTypes.indices) {
    io.In.data(s"field$i").ready := ~in_data_valid_R(i)
    when(io.In.data(s"field$i").fire()) {
      output_R.bits.data(s"field$i") <> io.In.data(s"field$i").bits
      in_data_valid_R(i) := true.B
    }
  }

  // Connecting outputs
  io.Out.bits := output_R.bits
  io.Out.valid := out_valid_R

  when(io.Out.fire()) {
    out_ready_R := io.Out.ready
    out_valid_R := false.B
  }


  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when((~enable_R.control).toBool) {
          out_ready_R := false.B
          enable_valid_R := false.B

          state := s_IDLE

          for (i <- retTypes.indices) {
            in_data_valid_R(i) := false.B
          }
          state := s_IDLE

          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Not predicated value -> reset\n")

        }.elsewhen(in_data_valid_R.asUInt.andR) {
          out_valid_R := true.B
          state := s_COMPUTE
        }
      }
    }
    is(s_COMPUTE) {
      when(out_ready_R) {
        for (i <- retTypes.indices) {
          in_data_valid_R(i) := false.B
        }

        out_valid_R := false.B
        enable_valid_R := false.B
        out_ready_R := false.B

        state := s_IDLE
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d, Value: %d\n", cycleCount, output_R.bits.data(s"field0").data)
      }
    }
  }

}

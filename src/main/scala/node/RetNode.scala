package node

import chisel3._
import chisel3.Module
import junctions._

import config._
import interfaces._
import util._
import utility.UniformPrintfs

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

  val inputReady = RegInit(VecInit(Seq.fill(retTypes.length + NumPredIn + 1) {
    true.B
  }))


  val outputReg = RegInit(0.U.asTypeOf(io.Out))
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  // Latch Data
  for (i <- retTypes.indices) {
    when(io.Out.fire()) {
      inputReady(i) := true.B
    }.elsewhen(io.In.data(s"field$i").valid) {
      outputReg.bits.data(s"field$i") := io.In.data(s"field$i").bits
      inputReady(i) := false.B
    }
    io.In.data(s"field$i").ready := inputReady(i)
  }

  // Latch Enable
  // @todo logic need to be checked
  // @note I just added second level when statement
  when(io.Out.fire()) {
    inputReady(retTypes.length) := true.B
  }.elsewhen(io.enable.valid) {
    //@note new block of code
    when(io.enable.bits.control) {
      outputReg.bits.enable <> io.enable.bits
      inputReady(retTypes.length) := false.B
    }.otherwise {
      for (i <- retTypes.indices) {
        inputReady(i) := true.B
      }
    }
  }
  io.enable.ready := inputReady(retTypes.length)

  for (i <- (retTypes.length + 1) until (retTypes.length + 1 + NumPredIn)) {
    when(io.Out.fire()) {
      inputReady(i) := true.B
    }.elsewhen(io.predicateIn(i - (retTypes.length + 1)).valid) {
      inputReady(i) := false.B
    }
    io.predicateIn(i - (retTypes.length + 1)).ready := inputReady(i)
  }

  io.Out.valid := ~(inputReady.asUInt.orR)
  io.Out.bits := outputReg.bits

  when(io.Out.fire()) {
    when(outputReg.bits.enable.control) {
      printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d, Value: %d\n", cycleCount, outputReg.bits.data(s"field0").data)
    }
  }
}

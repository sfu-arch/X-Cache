package node

import chisel3._
import chisel3.Module
import junctions._

import config._
import interfaces._
import util._
import utility.UniformPrintfs

class CallOutNodeIO(val argTypes: Seq[Int] )(implicit p: Parameters)
  extends Bundle
{
  val In  = Flipped(new CallDecoupled(argTypes))   // Requests from calling block(s)
  val Out = Decoupled(new Call(argTypes))          // To task
}

class CallOutNode(ID: Int, val argTypes: Seq[Int])
                 (implicit p: Parameters,
                  name: sourcecode.Name,
                  file: sourcecode.File) extends Module with UniformPrintfs {
  override lazy val io = IO(new CallOutNodeIO(argTypes)(p))
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount,_) = Counter(true.B,32*1024)
  override val printfSigil = module_name + ": " + node_name + ID + " "

  val inputReady = RegInit(VecInit(Seq.fill(argTypes.length+1){true.B}))
  val outputReg  = RegInit(0.U.asTypeOf(io.Out.bits))

  for (i <- argTypes.indices) {
    when(io.Out.fire()){
      inputReady(i) := true.B
    }.elsewhen(io.In.data(s"field$i").fire()) {
      outputReg.data(s"field$i") := io.In.data(s"field$i").bits
      inputReady(i) := false.B
    }
    io.In.data(s"field$i").ready := inputReady(i)
  }

  when(io.Out.fire()){
    inputReady(argTypes.length) := true.B
  }.elsewhen(io.In.enable.fire()) {
    outputReg.enable := io.In.enable.bits
    inputReady (argTypes.length) := false.B
  }
  io.In.enable.ready := inputReady(argTypes.length)

  io.Out.valid := ~(inputReady.asUInt.orR)
  io.Out.bits := outputReg

}

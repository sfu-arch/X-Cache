package node

import chisel3._
import chisel3.Module
import junctions._

import config._
import interfaces._
import util._
import utility.UniformPrintfs

class CallInNodeIO(val argTypes: Seq[Int])(implicit p: Parameters)
  extends Bundle
{
  // Predicate enable
  val enable = Flipped(Decoupled(new ControlBundle))
  // Data I/O
  val In   = Flipped(Decoupled(new Call(argTypes))) // From task
  val Out  = new CallDecoupled(argTypes)            // Returns to calling block(s)
}

class CallInNode(ID: Int, argTypes: Seq[Int])
              (implicit p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File) extends Module
  with UniformPrintfs {
  override lazy val io = IO(new CallInNodeIO(argTypes)(p))

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount,_) = Counter(true.B,32*1024)
  override val printfSigil = module_name + ": " + node_name + ID + " "

  // Enable
  val enable_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)

  val inputReg  = RegInit(0.U.asTypeOf(io.In.bits))
  val inputReadyReg = RegInit(false.B)
  val outputValidReg = RegInit(VecInit(Seq.fill(argTypes.length + 1)(false.B)))

  val s_idle :: s_latched :: Nil = Enum(2)
  val state = RegInit(s_idle)

  io.In.ready := state === s_idle

  // Wire up enable READY and VALIDs
  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_valid_R := io.enable.valid
    enable_R <> io.enable.bits
  }

  val inFire_R = RegInit(false.B)
  when(io.In.fire()) {
    inFire_R := true.B
  }


  switch(state) {
    is(s_idle) {
      when (inFire_R && enable_valid_R && enable_R.control) {
        state := s_latched
        inputReg <> io.In.bits
      }
    }
    is(s_latched) {
      when (outputValidReg.asUInt.orR === false.B) {
        state := s_idle
        inFire_R := false.B
        enable_valid_R := false.B
      }
    }
  }

  for (i <- argTypes.indices) {
    when(io.Out.data(s"field$i").ready){
      outputValidReg(i) := false.B
    }
    when(io.In.valid && state === s_idle) {
      outputValidReg(i) := true.B
    }
    io.Out.data(s"field$i").valid := outputValidReg(i)
    io.Out.data(s"field$i").bits <> inputReg.data(s"field$i")
  }

  when(io.Out.enable.ready){
    outputValidReg(argTypes.length) := false.B
  }
  when(io.In.valid && state === s_idle) {
    outputValidReg(argTypes.length) := true.B
  }
  io.Out.enable.valid := outputValidReg(argTypes.length)
  io.Out.enable.bits <> inputReg.enable

  when(io.In.fire) {
    when (io.In.bits.enable.control)
    {
      printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name  + ": Inonse @ %d\n", io.In.bits.enable.taskID, cycleCount)
    }
  }
}

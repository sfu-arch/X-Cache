package node

import chisel3._
import chisel3.Module
import junctions._

import config._
import interfaces._
import util._
import utility.UniformPrintfs

class CallOutNodeIO(val argTypes: Seq[Int],
              NumPredOps: Int,
              NumSuccOps: Int,
              NumOuts: Int)(implicit p: Parameters)
  extends HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts)(new Call(argTypes))
{
  val In  = Flipped(new VariableDecoupledData(argTypes))   // Requests from calling block(s)
}

class CallOutNode(ID: Int, val argTypes: Seq[Int], NumSuccOps: Int=0)
                 (implicit p: Parameters,
                  name: sourcecode.Name,
                  file: sourcecode.File)
  extends HandShaking(0, NumSuccOps,1, ID)(new Call(argTypes))(p) with UniformPrintfs {

  override lazy val io = IO(new CallOutNodeIO(argTypes, 0, NumSuccOps, 1)(p))
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = module_name + ": " + node_name + ID + " "


  val s_idle :: s_Done :: Nil = Enum(2)
  val state = RegInit(s_idle)

  val data_R = Reg(new VariableData(argTypes))
  val data_valid_R = RegInit(VecInit(Seq.fill(argTypes.length) {
    false.B
  }))

  for (i <- argTypes.indices) {
    when(io.In(s"field$i").fire()) {
      data_R(s"field$i") := io.In(s"field$i").bits
      data_valid_R(i) := true.B
    }
    io.In.data(s"field$i").ready := !data_valid_R(i)
  }

  when(io.enable.fire()) {
    succ_bundle_R.foreach(_ := io.enable.bits)
  }
  io.Out(0).bits.data := data_R
  io.Out(0).bits.enable := enable_R

  switch(state) {
    is(s_idle) {
      when(enable_valid_R && data_valid_R.asUInt.andR) {
        ValidSucc()
        ValidOut()
        state := s_Done
      }
    }
    is(s_Done) {
      when(IsOutReady()) {
        // Clear all the data valid states.
        data_valid_R.foreach(_ := false.B)
        // Clear all other state
        Reset()
        // Reset state.
        state := s_idle
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d\n", enable_R.taskID, cycleCount)
      }
    }
  }
}
package node

import chisel3._
import chisel3.Module
import config._
import interfaces.{ControlBundle, DataBundle}
import util._

class ConstNode(value: Int, NumOuts: Int, ID: Int)
               (implicit p: Parameters,
                name: sourcecode.Name,
                file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {

  override lazy val io = IO(new HandShakingIONPS(NumOuts)(new DataBundle()))
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)


  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := value.U
    io.Out(i).bits.predicate := enable_R.control
    io.Out(i).bits.taskID := enable_R.taskID
  }

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        ValidOut()
        state := s_COMPUTE
      }
    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        //Reset state
        state := s_IDLE
        //Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, value.U)
      }
    }
  }


}


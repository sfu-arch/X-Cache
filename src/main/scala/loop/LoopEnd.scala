package dandelion.loop

import chisel3._
import chisel3.Module
import chisel3.util._
import dandelion.config._
import dandelion.interfaces._
import util._
import utility.UniformPrintfs


/**
  * @note Loop header IO
  * @param NumInputs Number of inputs
  */
class LoopEndIO(val NumInputs: Int, val NumOuts: Int)
                             (implicit p: Parameters) extends AccelBundle()(p) {

  val inputArg  = Vec(NumInputs, Flipped(Decoupled(new DataBundle())))
  val outputArg = Vec(NumOuts, Decoupled(new DataBundle()))

  val enableSignal = Vec(NumInputs, Flipped(Decoupled(new ControlBundle)))

}



class LoopEnd(val NumInputs: Int, val NumOuts: Int, val ID: Int)
                (implicit val p: Parameters) extends Module with HasAccelParams with UniformPrintfs {

  override lazy val io = IO(new LoopEndIO(NumInputs, NumOuts))

  val Args = for (i <- 0 until NumInputs) yield {
    val arg = Module(new LiveOutNode(NumOuts = 1, ID = i))
    arg
  }

  //Iterating over each loopelement and connect them to the IO
  for (i <- 0 until NumInputs) {
    Args(i).io.InData <> io.inputArg(i)
    Args(i).io.enable <> io.enableSignal(i)
  }

  for (i <- 0 until NumOuts) {
    io.outputArg(i) <> Args(i).io.Out(0)
  }

}

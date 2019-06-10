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
class LoopStartIO(val NumInputs: Int, val NumOuts: Int)
                             (implicit p: Parameters) extends CoreBundle()(p) {

  val inputArg  = Vec(NumInputs, Flipped(Decoupled(new DataBundle())))
  val outputArg = Vec(NumOuts, Decoupled(new DataBundle()))

  val enableSignal = Vec(NumInputs, Flipped(Decoupled(new ControlBundle)))

  /**
    * Finish signal comes from Ret instruction
    */
  val Finish = Vec(NumInputs, Flipped(Decoupled(new ControlBundle())))

  override def cloneType = new LoopStartIO(NumInputs, NumOuts).asInstanceOf[this.type]

}



class LoopStart(val NumInputs: Int, val NumOuts: Int, val ID: Int)
                (implicit val p: Parameters) extends Module with CoreParams with UniformPrintfs {

  override lazy val io = IO(new LoopStartIO(NumInputs, NumOuts))

  val Args = for (i <- 0 until NumInputs) yield {
    val arg = Module(new LiveInNode(NumOuts = 1, ID = i))
    arg
  }

  //Iterating over each loopelement and connect them to the IO
  for (i <- 0 until NumInputs) {
    Args(i).io.InData <> io.inputArg(i)
    //Args(i).io.Finish <> io.Finish(i)
    Args(i).io.enable <> io.enableSignal(i)
    io.Finish(i).ready := true.B // just to shut simulation up
  }

  for (i <- 0 until NumOuts) {
    io.outputArg(i) <> Args(i).io.Out(0)
  }

}

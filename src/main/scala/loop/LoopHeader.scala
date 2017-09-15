package loop

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
import node._
import utility.UniformPrintfs


/**
  * @note Loop header IO
  * @param NumInputs Number of inputs
  */
class LoopHeaderIO[T <: Data](val NumInputs: Int, val NumOuts: Int)(gen: T)(implicit p: Parameters) extends CoreBundle()(p) {

  val start = Flipped(Decoupled(Bool()))
  val end = Decoupled(Bool())

  val inputArg = Vec(NumInputs, Flipped(Decoupled(gen)))
  val inputVal = Vec(NumOuts, Decoupled(gen))
}

class LoopHeader[T <: Data](val NumInputs: Int,
                            val NumOuts: Int,
                            val ID: Int)
                           (val Blocking: Boolean)
                           (gen: T)(implicit val p: Parameters) extends Module with CoreParams with UniformPrintfs {

  lazy val io = IO(new LoopHeaderIO(NumInputs, NumOuts)(gen))

  val valids = 0.U

  //@todo get number of outputs for each arg and set it here
  val Args = for (i <- 0 until NumInputs) yield {
    //@todo NumOuts should be set
    val arg = Module(new LoopRegNode(NumOuts = 1, ID = i))
    arg
  }

  //Iterating over each loopReg and connect them to the IO
  for(i <- 0 until NumInputs){
    Args(i).io.inData <> io.inputArg(i)
  }

  //@todo fix base on each input output
  for(i <- 0 until NumOuts){
    io.inputVal(i) <> Args(i).io.Out(0)
  }

  for(i <- 0 until NumInputs){
    valids(0) := io.inputArg(i).valid
  }

  io.start.data := valids.asUInt().andR()
  io.start.valid := true.B


}
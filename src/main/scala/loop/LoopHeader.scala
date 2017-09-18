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

  val start = Output(Bool())
  val end = Input(Bool())
  val enable = Flipped(Decoupled(Bool()))
  val freeze = Input(Bool())

  val inputArg = Vec(NumInputs, Flipped(Decoupled(gen)))
  val outputVal = Vec(NumOuts, Decoupled(gen))
}

class LoopHeader(val NumInputs: Int,
                            val NumOuts: Int,
                            val ID: Int)
                           (val nodeOut: Seq[Int])
                           (implicit val p: Parameters) extends Module with CoreParams with UniformPrintfs {

  lazy val io = IO(new LoopHeaderIO(NumInputs, NumOuts)(new DataBundle()))

  val valids = WireInit(VecInit(Seq.fill(NumInputs){false.B}))

  //@note get number of outputs for each arg and set it here
  assert(nodeOut.size == NumInputs, "Size of nodeOut should be equal to NumInputs")

  val Args = for (i <- 0 until NumInputs) yield {
    val arg = Module(new LoopRegNode(NumOuts = nodeOut(i), ID = i))
    arg
  }

  //Iterating over each loopReg and connect them to the IO
  for (i <- 0 until NumInputs) {
    Args(i).io.inData <> io.inputArg(i)
  }

  for (i <- 0 until NumInputs){
    Args(i).io.enable <> io.enable
    Args(i).io.freeze <> io.freeze
  }

  for (i <- 0 until NumOuts) {
    for(j <- 0 until nodeOut(i)){
      io.outputVal(i) <> Args(i).io.Out(j)
    }
  }

  for (i <- 0 until NumInputs) {
    valids(i) := io.inputArg(i).valid
  }

  io.start := valids.asUInt().andR()


}

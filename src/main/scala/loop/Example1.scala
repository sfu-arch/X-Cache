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


///**
//  * Defining LoopOutputBundle
//  * @param gen Datatype
//  * @tparam T
//  */
//class LoopOutputBundleIO[+T <: Data](gen: T) extends Bundle(){
//  val bits  = Output(gen.cloneType)
//  val valid = Output(Bool())
//  override def cloneType: this.type = new LoopOutputBundleIO(gen).asInstanceOf[this.type]
//}
//
//object LoopOutputBundle{
//  def apply[T <: Data](gen: T): LoopOutputBundleIO[T] = new LoopOutputBundleIO(gen)
//}
//
///**
//  * @note Loop header IO
//  * @param NumInputs Number of inputs
//  */
//class LoopHeaderIO[T <: Data](val NumInputs: Int, val NumOuts: Int)
//                             (gen: T)(implicit p: Parameters) extends CoreBundle()(p) {
//
//  val inputArg  = Vec(NumInputs, Flipped(Decoupled(gen)))
//  val outputArg = Vec(NumOuts,LoopOutputBundle(gen))
//
//  /**
//    * Finish signal comes from Ret instruction
//    */
//  val Finish = Input(Bool())
//
//  /**
//    * @todo connect the START to entry basic block
//    */
//  val Start = Output(Bool())
//
//}

class LoopExampleIO[T <: Data](val ID: Int)(gen: T)(implicit p: Parameters) extends CoreBundle()(p){

  val Input1 = Flipped(Decoupled(gen))
  val Input2 = Flipped(Decoupled(gen))
  val Input3 = Flipped(Decoupled(gen))
  val Input4 = Flipped(Decoupled(gen))

  val Enable = Flipped(Decoupled(Bool()))

  val Finish = Input(Bool())

  val Result = Decoupled(gen)
}


class LoopExample(val NumInputs: Int, val ID: Int)
                (implicit val p: Parameters) extends Module with CoreParams with UniformPrintfs{

  lazy val io = IO(new LoopExampleIO(ID)(new DataBundle()))

  val head = Module(new LoopHeader(NumInputs = NumInputs, NumOuts = 4, ID = 0)(p))
  val comp1 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "Add")(sign = false)(p))
  val comp2 = Module(new ComputeNode(NumOuts = 1, ID = 2, opCode = "Add")(sign = false)(p))
  val comp3 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "Add")(sign = false)(p))

  comp1.io.enable <> io.Enable
  comp2.io.enable <> io.Enable
  comp3.io.enable <> io.Enable

  head.io.inputArg(0) <> io.Input1
  head.io.inputArg(1) <> io.Input2
  head.io.inputArg(2) <> io.Input3
  head.io.inputArg(3) <> io.Input4

  head.io.Finish <> io.Finish

  comp1.io.LeftIO <> head.io.outputArg(0)
  comp1.io.RightIO <> head.io.outputArg(1)

  comp2.io.LeftIO <> head.io.outputArg(2)
  comp2.io.RightIO <> head.io.outputArg(3)

  comp3.io.LeftIO <> comp1.io.Out(0)
  comp3.io.RightIO <> comp2.io.Out(0)

  io.Result <> comp3.io.Out(0)

}

//class LoopHeader(val NumInputs: Int, val NumOuts: Int, val ID: Int)
//                (implicit val p: Parameters) extends Module with CoreParams with UniformPrintfs {
//
//  lazy val io = IO(new LoopHeaderIO(NumInputs, NumOuts)(new DataBundle()))
//
//  val valids = WireInit(VecInit(Seq.fill(NumInputs){false.B}))
//
//  val Args = for (i <- 0 until NumInputs) yield {
//    val arg = Module(new LoopElement(ID = i))
//    arg
//  }
//
//  //Iterating over each loopelement and connect them to the IO
//  for (i <- 0 until NumInputs) {
//    Args(i).io.inData <> io.inputArg(i)
//    Args(i).io.Finish <> io.Finish
//  }
//
//  for (i <- 0 until NumOuts) {
//    io.outputArg(i).bits  <> Args(i).io.outData.data
//    io.outputArg(i).valid <> Args(i).io.outData.valid
//  }
//
//  for (i <- 0 until NumInputs) {
//    valids(i) <> io.inputArg(i).valid
//  }
//
//  io.Start := valids.asUInt().andR()
//
//
//}

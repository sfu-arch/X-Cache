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
  * Contain each loop input argument works like register file
  */
class LoopElementIO[T <: Data](gen: T)(implicit p: Parameters)
  extends CoreBundle()(p) {

  /**
    * Module input
    */
  val inData = Flipped(Decoupled(gen))
  val Finish = Input(Bool())

  /**
    * Module output
    */
  val outData = Output(gen)
  val outDataValid = Output(Bool())
}


class LoopElement(val ID: Int)(implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

  override lazy val io = IO(new LoopElementIO(new DataBundle())(p))

  // Printf debugging
  override val printfSigil = "Node ID: " + ID + " "

  /**
    * Always latch the input data
    */
  val data_R = RegNext(io.inData.bits)

  io.outData := data_R

  /**
    * Defining state machines
    */
  val s_INIT :: s_LATCH :: Nil = Enum(2)
  val state = RegInit(s_INIT)

  /**
    * State transision
    */
  when(state === s_INIT){
    io.inData.ready := true.B
    io.outDataValid := false.B

  }.elsewhen( state === s_LATCH){
    io.inData.ready := false.B
    io.outDataValid := true.B
  }

  when(io.inData.fire()){
    state := s_LATCH
  }

  when(io.Finish){
    state := s_INIT
  }

  /**
    * Debuging info
    */
  printfInfo(" State: %x\n", state)

}

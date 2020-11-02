package memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum


class TBE (implicit p: Parameters)  extends AXIAccelBundle with HasCacheAccelParams {
  //@todo lockbit should be added
  val state = new State()
  val data = UInt (xlen.W)
  val way = UInt (wayLen.W)
  val set = UInt (setLen.W)
  val cmdPtr = UInt (nCom.W)

}
object TBE {

  def default (implicit p: Parameters){
    val tbe = Wire(new TBE)
    tbe.state := State.default
    tbe.data := 0.U
    tbe.set := 0.U
    tbe.way := 0.U
    tbe.cmdPtr := 0.U
  }
}

class TBEControllerIO (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {

  val command = Input (UInt(nCom.W))
  val inputTBE= Input(new TBE)
  val outputTBE = Output(new TBE)

}

class   TBEController(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {


  val (idle::alloc :: dealloc :: read :: Nil) = Enum(4)

  val io = IO(new TBEControllerIO())

  val way = WireInit(io.inputTBE.way)
  val set = WireInit(io.inputTBE.set)
  val state = WireInit(io.inputTBE.state)

  val nextCmd = Wire(UInt(nCom.W))
  io.outputTBE.way := way
  io.outputTBE.set := set
  io.outputTBE.state := state
  io.outputTBE.data := io.inputTBE.data

  io.outputTBE.cmdPtr := nextCmd

  

}
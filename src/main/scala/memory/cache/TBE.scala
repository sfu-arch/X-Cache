package memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}

class TBE (implicit p: Parameters)  extends AXIAccelBundle with HasCacheAccelParams {
  val state = new State()
  val data = UInt (xlen.W)
}
object TBE {

  def default (implicit p: Parameters){
    val tbe = Wire(new TBE)
    tbe.state := State.default
    tbe.data := 0.U
  }
}

class TBETableIO (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {

  val inputValue = Input(new TBE)
  val output = Output(new TBE)

}

class   TBETable(Size: Int )(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val io = IO(new TBETableIO())
  val TBEMemory = VecInit(seq.fill(Size) ((TBE.default)))

}
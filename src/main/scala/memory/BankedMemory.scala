package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.axi._


trait HasBankedMemAccelParams extends HasCacheAccelParams {

  val dataLen = xlen
  override val addrLen: Int = setLen
  val banks = nWays
  val bankDepth = nSets
  val bankLen = wayLen

}

class MemBankIO[T <: Data] (D: T)(implicit val p: Parameters) extends Bundle with HasBankedMemAccelParams{

  val bank = Input(UInt(bankLen.W))
  val address = Input(UInt(addrLen.W))
  val isRead = Input(Bool())
  val inputValue = Input( D.cloneType)
  val outputValue = Output(Vec(banks, D.cloneType))
}


class MemBank[T <: Data] (D:T) (implicit val p: Parameters)
    extends Module
    with HasBankedMemAccelParams {
//with HasAccelShellParams{

  val mt = D.cloneType
  val io = IO (new MemBankIO(mt))

  val mems = Seq.fill(banks) { Mem(bankDepth, mt) }

  io.outputValue := DontCare

  when(io.isRead) {
    (0 until banks).foldLeft() {
      case (_, bankIndex) => {
          io.outputValue(bankIndex.U) := mems(bankIndex)(io.address)
        }
    }
  }.otherwise {
    (0 until banks).foldLeft(when(false.B) {}) {
      case (whenContext, bankIndex) =>
        whenContext.elsewhen(io.bank === bankIndex.U) {
          mems(bankIndex)(io.address) := io.inputValue
        }
    }
  }
}
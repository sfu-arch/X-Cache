package dandelion.memory

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.axi._

class MemBankIO (val banks: Int, val bankDepth: Int, val dataLen: Int, val addrLen: Int) extends Bundle{
  val bank = Input(UInt(addrLen.W))
  val address = Input(UInt(addrLen.W))
  val isRead = Input(Bool())
  val inputValue = Input(UInt(dataLen.W))
  val outputValue = Output(UInt(dataLen.W))
}


class MemBank(val banks: Int, val bankDepth: Int, val dataLen: Int, val addrLen: Int)(implicit  val p: Parameters) extends Module
with HasAccelParams
with HasAccelShellParams{

  val io = IO (new MemBankIO(banks, bankDepth, dataLen, addrLen))

  val mems = Seq.fill(banks) { Mem(bankDepth, UInt(dataLen.W)) }

  io.outputValue := DontCare

  when(io.isRead) {
    (0 until banks).foldLeft(when(false.B) {}) {
      case (whenContext, bankIndex) =>
        whenContext.elsewhen(io.bank === bankIndex.U) {
          io.outputValue := mems(bankIndex)(io.address)
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
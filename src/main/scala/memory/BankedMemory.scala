package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.axi._



class MemBankIO[T <: Data] (D: T)(val dataLen:Int, val addrLen : Int, val banks: Int, val bankDepth:Int, val bankLen:Int)(implicit val p: Parameters) extends Bundle{

  val bank = Input(UInt(bankLen.W))
  val address = Input(UInt(addrLen.W))
  val isRead = Input(Bool())
  val inputValue = Input( D.cloneType)
  val outputValue = Output(Vec(banks, D.cloneType))
  val valid = Output (Bool())
}


class MemBank[T <: Data] (D:T) (val dataLen:Int, val addrLen : Int, val banks: Int, val bankDepth:Int, val bankLen:Int)(implicit val p: Parameters)
    extends Module {
  //with HasAccelShellParams{

  val mt = D.cloneType
  val io = IO (new MemBankIO(mt)(dataLen, addrLen, banks, bankDepth, bankLen))

  val mems = Seq.fill(banks) { Mem(bankDepth, mt) }

  io.outputValue := DontCare
  io.valid := DontCare
  when(io.isRead) {
    (0 until banks).foldLeft() {
      case (_, bankIndex) => {
          io.outputValue(bankIndex.U) := mems(bankIndex)(io.address)
        }
        io.valid := true.B
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
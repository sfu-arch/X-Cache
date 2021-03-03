package memGen.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.util._
import memGen.interfaces._
import memGen.interfaces.axi._



class MemBankIO[T <: Data] (D: T)(val dataLen:Int, val addrLen : Int, val banks: Int, val bankDepth:Int, val bankLen:Int)(implicit val p: Parameters) extends Bundle{

  val read = new Bundle{
    val in = Flipped(Valid(new Bundle {
      val bank = (UInt(banks.W))
      val address = (UInt(addrLen.W))
    }))
    val outputValue =  Output(Vec(banks, D.cloneType))
  }

  val write = Flipped(Valid(new Bundle() {
    val bank = (UInt(banks.W))
    val address = (UInt(addrLen.W))
    val inputValue = (Vec(banks, D.cloneType))
  }))

  override def cloneType: this.type = new MemBankIO(D)(dataLen,addrLen,banks,bankDepth,bankLen).asInstanceOf[this.type ]
}


class MemBank[T <: Data] (D:T) (val dataLen:Int, val addrLen : Int, val banks: Int, val bankDepth:Int, val bankLen:Int)(implicit val p: Parameters)
    extends Module {

  val mt = D.cloneType
  val io = IO(new MemBankIO(mt)(dataLen, addrLen, banks, bankDepth, bankLen))

  val mems = Seq.fill(banks) {
    Mem(bankDepth, mt)
  }

  when(io.read.in.fire()) {
    (0 until banks).foldLeft() {
      case (_, bankIndex) => {
        io.read.outputValue(bankIndex.U) := mems(bankIndex)(io.read.in.bits.address)
      }
    }
  }.otherwise {
    io.read.outputValue := DontCare

  }

  when(io.write.fire()) {
    (0 until banks).foldLeft(when(false.B) {}) {
      case (whenContext, bankIndex) => {
        whenContext.elsewhen(io.write.bits.bank.asBools()(bankIndex) === true.B) {
          mems(bankIndex)(io.write.bits.address) := io.write.bits.inputValue(bankIndex)
        }
      }


    }
  }
}
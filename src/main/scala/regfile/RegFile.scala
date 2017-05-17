// See LICENSE for license details.

package regfile

import chisel3._
import chisel3.util._
import config._


class RegFileIO(implicit p: Parameters) extends CoreBundle()(p) {
  val raddr1 = Input(UInt(5.W))
  val rdata1 = Output(UInt(xlen.W))
  val wen    = Input(Bool())
  val waddr  = Input(UInt(5.W))
  val wdata  = Input(UInt(xlen.W))
  val wmask  = Input(UInt((xlen/8).W))
}


abstract class AbstractRFile(implicit val p: Parameters) extends Module with CoreParams {
   val io = IO(new RegFileIO)
}

class RFile(size: Int)(implicit p: Parameters) extends AbstractRFile()(p) { 
   val regs = SyncReadMem(size,Vec(xlen/8, Bits(width=8)))
   // SyncReadMem(size, UInt(xlen.W))
   // I am reading a vector of bytes and then converting to a UInt before returning it.
   io.rdata1 := Mux(io.raddr1.orR,regs.read(io.raddr1).toBits, 0.U)
   // io.rdata2 := Mux(io.raddr2.orR, regs(io.raddr2), 0.U)
   when(io.wen & io.waddr.orR) {
    // I am writing a vector of bytes. Need to also feed the bytemask. 
    regs.write(io.waddr, Vec.tabulate(xlen/8)(i => io.wdata(8*(i+1)-1,8*i)))
  }
}

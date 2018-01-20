// See LICENSE for license details.

package regfile
import scala.math._
import chisel3._
import chisel3.util._
import config._

/**
 * @brief IO interface to register file
 * @details
 * raddr1: Read address (word aligned)
 * rdata1: Read data (word granularity)
 * wen   : Write enable
 * waddr : write address (word aligned)
 * wdata : write data (word granularity)
 *
 * @param size: Number of registers
 *
 */
class RegFileIO(size: Int)(implicit p: Parameters) extends CoreBundle()(p) {
  val raddr1 = Input(UInt(max(1,log2Ceil(size)).W))
  val rdata1 = Output(UInt(xlen.W))
  val raddr2 = Input(UInt(max(1,log2Ceil(size)).W))
  val rdata2 = Output(UInt(xlen.W))
  val wen    = Input(Bool())
  val waddr  = Input(UInt(max(1,log2Ceil(size)).W))
  val wdata  = Input(UInt(xlen.W))
  val wmask  = Input(UInt((xlen/8).W))
}


abstract class AbstractRFile(size: Int)(implicit val p: Parameters) extends Module with CoreParams {
   val io = IO(new RegFileIO(size))
}
/**
 * @brief Scratchpad registerfile
 * @details [long description]
 *
 * @param size : Number of registers.
 * @return [description]
 */
class RFile(size: Int)(implicit p: Parameters) extends AbstractRFile(size)(p) {
  val regs = SyncReadMem(size,Vec(xlen/8, UInt(8.W)))
  // SyncReadMem(size, UInt(xlen.W))
  // I am reading a vector of bytes and then converting to a UInt before returning it.
  io.rdata1 := Mux(io.raddr1.orR,regs.read(io.raddr1).asUInt(), 0.U)
  io.rdata2 := Mux(io.raddr2.orR,regs.read(io.raddr2).asUInt(), 0.U)
  // io.rdata2 := Mux(io.raddr2.orR, regs(io.raddr2), 0.U)
  when(io.wen & io.waddr.orR) {
    // I am writing a vector of bytes. Need to also feed the bytemask.
    regs.write(io.waddr, VecInit.tabulate(xlen/8)(i => io.wdata(8*(i+1)-1,8*i)),io.wmask.toBools)
  }
}

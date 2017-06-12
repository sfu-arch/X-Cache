// See LICENSE for license details.

package accel

import chisel3._
import chisel3.util._

import config._
import junctions._

abstract class AcceleratorIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(
    new Bundle { 
      val h2f  = Flipped(new NastiIO)
      val f2h  = new NastiIO
    }
  )
}

class Accelerator(implicit p: Parameters) extends AcceleratorIO()(p) {

  val cNum = 3        // # Control registers 
  val sNum = 3        // # Status registers

  val regs    = Module(new SimpleReg(cNum, sNum))
  val core    = Module(new Core())
  val cache   = Module(new Cache)

  // Connect HPC AXI Master interface the control/status register block
  // AXI Slave interface
  regs.io.nasti <> io.h2f

  // Connect a revision number to the first status register
  regs.io.stat(0) <> 0x55AA0001.U

  // Connect the first three control registers and one of the status
  // registers to the core logic block
  core.io.ctrl <> regs.io.ctrl(0)
  core.io.addr <> regs.io.ctrl(1)
  core.io.len  <> regs.io.ctrl(2)
  core.io.stat <> regs.io.stat(1)
  // Connect the cache CPU interface to the core logic block
  core.io.cache <> cache.io.cpu
  cache.io.stat <> regs.io.stat(2)
  // Connect the Cache AXI Master to the HPC AXI Slave interface
  io.f2h <> cache.io.nasti

}

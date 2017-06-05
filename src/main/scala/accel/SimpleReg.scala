package accel

import chisel3._
import chisel3.util._

import junctions._
import config._

/**
  * The SimpleReg class creates a set of memory mapped configuration and status registers
  * accessible via a Nasti (AXI4) slave interface.  Each register is the native width of
  * the Nasti data bus.
  *
  * The control registers are read/write access.  The content of the control registers
  * is output on io.ctrl interface.
  *
  * The status registers are read only. The status registers are non-latching and reflect
  * the live state of the io.stat interface.
  *
  * The registers are mapped into two banks of 'N' size where N is the the nearest power
  * of two larger than the max(cNum, sNum).  The control register bank is mapped into
  * the lower address range and the status registers follow.  E.g. for 3 control registers
  * and 2 status registers:
  *
  * Base Address -> Ctrl Reg 0
  *                 Ctrl Reg 1
  *                 Ctrl Reg 2
  *                 Unused
  *                 Stat Reg 0
  *                 Stat Reg 1
  *                 Unused
  *                 Unused
  *
  *
  * @Note Since the control registers are readable, unused register bits will not be
  *      automatically removed.  Pack the registers efficiently to save logic.  Unused
  *      status register bits will automatically be removed from the final logic.
  *
  * @param cNum The number of configuration registers
  * @param sNum The number of status registers
  * @param p    Implicit project parameters
  * 
  * @note io.nasti A Nasti bus slave interface to a processor core
  * @note io.ctrl  A vector of 'cNum' control registers
  * @note io.stat  A vector of 'sNum' status registers
  */

abstract class SimpleRegIO(cNum : Int, sNum: Int)(implicit val p: Parameters) extends Module with CoreParams
{
  val io = IO(
    new Bundle {
      val nasti = Flipped(new NastiIO())
      val ctrl = Output(Vec(cNum,UInt(xlen.W)))
      val stat = Input(Vec(sNum,UInt(xlen.W)))
    }
  )
}

class SimpleReg(cNum : Int, sNum: Int)(implicit p: Parameters) extends SimpleRegIO(cNum,sNum)(p) {

  val ctrlBank = Vec(Seq.fill(cNum) {RegInit(0.U(xlen.W))})
  val statBank = Vec(Seq.fill(sNum) {RegInit(0.U(xlen.W))})
  val wordSelBits = log2Ceil(xlen/8)
  val regSelBits = log2Ceil(math.max(cNum, sNum))

  // register for write address channel ready signal
  val writeAddrReadyReg = RegInit(false.B)
  val canDoWrite = io.nasti.aw.valid && io.nasti.w.valid && !writeAddrReadyReg
  writeAddrReadyReg := canDoWrite
  io.nasti.aw.ready := writeAddrReadyReg
  
  // register for keeping write address
  val writeAddrReg = RegInit(0.U(32.W))
  when (canDoWrite) {writeAddrReg := io.nasti.aw.bits.addr}
  
  val writeReadyReg = RegNext(canDoWrite, false.B)
  io.nasti.w.ready := writeReadyReg
  
  // register bank write
  val doWrite = writeReadyReg && io.nasti.w.valid && writeAddrReadyReg && io.nasti.aw.valid
  val writeRegSelect = writeAddrReg(regSelBits+wordSelBits, wordSelBits)
  val writeBankSelect = writeAddrReg(1+regSelBits+wordSelBits, regSelBits+wordSelBits)

  // note that we write the entire word (no byte select using write strobe)
  when (writeBankSelect === 0.U && doWrite) { ctrlBank(writeRegSelect) := io.nasti.w.bits.data }
  
  // write response generation
  io.nasti.b.bits.resp   := 0.U        // always OK
  val writeRespValidReg = RegInit(false.B)
  writeRespValidReg  := doWrite && !writeRespValidReg
  io.nasti.b.valid   := writeRespValidReg
  io.nasti.b.bits.id := io.nasti.aw.bits.id
  
  // read address ready generation
  val readAddrReadyReg = RegInit(false.B)
  val canDoRead = !readAddrReadyReg && io.nasti.ar.valid
  readAddrReadyReg := canDoRead
  io.nasti.ar.ready := readAddrReadyReg
  
  // read address latching
  val readAddrReg = RegInit(0.U(32.W))
  when (canDoRead) { readAddrReg := io.nasti.ar.bits.addr }
  
  // read data valid and response generation
  val readValidReg = RegInit(false.B)
  val doRead = readAddrReadyReg && io.nasti.ar.valid && !readValidReg
  readValidReg := doRead
  
  io.nasti.r.valid := readValidReg
  io.nasti.r.bits.last  := readValidReg
  io.nasti.r.bits.resp := 0.U    // always OK
  io.nasti.r.bits.id := io.nasti.ar.bits.id
  
  // register bank read
  val readRegSelect = readAddrReg(regSelBits+wordSelBits, wordSelBits)
  val readBankSelect = readAddrReg(1+regSelBits+wordSelBits, regSelBits+wordSelBits)
  val outputReg = RegInit(0.U(32.W))
  statBank := io.stat
  when (readBankSelect === 0.U ) {
    outputReg := ctrlBank(readRegSelect)
  } .otherwise {
    outputReg := statBank(readRegSelect)
  }
  io.nasti.r.bits.data := outputReg

  // Connect registers to output bus
  io.ctrl := ctrlBank

}


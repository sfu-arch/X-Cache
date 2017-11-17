package accel

import chisel3._
import chisel3.util._

import junctions._
import config._

class NastiMemSlaveIO(implicit p: Parameters) extends CoreBundle()(p) {
  val nasti = Flipped(new NastiIO)
}

class NastiMemSlave()(implicit val p: Parameters) extends Module with CacheParams {

  val io = IO(new NastiMemSlaveIO()(p))

  /* Memory model interface */
  val dutMem = Wire(new NastiIO)

  // Connect DUT Cache I/O to a queue for the memory model logic
  dutMem.ar <> Queue(io.nasti.ar, 32)
  dutMem.aw <> Queue(io.nasti.aw, 32)
  dutMem.w <> Queue(io.nasti.w, 32)
  io.nasti.b <> Queue(dutMem.b, 32)
  io.nasti.r <> Queue(dutMem.r, 32)

  val size = log2Ceil(nastiXDataBits / 8).U
  val len = (dataBeats - 1).U

  /* Main Memory */
  val mem = Mem(1 << 20, UInt(nastiXDataBits.W))
  val sMemIdle :: sMemWrite :: sMemWrAck :: sMemRead :: Nil = Enum(4)
  val memState = RegInit(sMemIdle)
  val (wCnt, wDone) = Counter(memState === sMemWrite && dutMem.w.valid, dataBeats)
  val (rCnt, rDone) = Counter(memState === sMemRead && dutMem.r.ready, dataBeats)

  dutMem.ar.ready := false.B
  dutMem.aw.ready := false.B
  dutMem.w.ready := false.B
  dutMem.b.valid := memState === sMemWrAck
  dutMem.b.bits := NastiWriteResponseChannel(0.U)
  dutMem.r.valid := memState === sMemRead
  dutMem.r.bits := NastiReadDataChannel(0.U, mem.read((dutMem.ar.bits.addr >> size) + rCnt), rDone)

  switch(memState) {
    is(sMemIdle) {
      when(dutMem.aw.valid) {
        memState := sMemWrite
      }.elsewhen(dutMem.ar.valid) {
        memState := sMemRead
      }
    }
    is(sMemWrite) {
      assert(dutMem.aw.bits.size === size)
      assert(dutMem.aw.bits.len === len)
      when(dutMem.w.valid) {
        mem((dutMem.aw.bits.addr >> size) + wCnt) := dutMem.w.bits.data
        printf("[write] mem[%x] <= %x\n", (dutMem.aw.bits.addr >> size) + wCnt, dutMem.w.bits.data)
        dutMem.w.ready := true.B
      }
      when(wDone) {
        dutMem.aw.ready := true.B
        memState := sMemWrAck
      }
    }
    is(sMemWrAck) {
      when(dutMem.b.ready) {
        memState := sMemIdle
      }
    }
    is(sMemRead) {
      when(dutMem.r.ready) {
        printf("[read] mem[%x] => %x\n", (dutMem.ar.bits.addr >> size) + rCnt, dutMem.r.bits.data)
      }
      when(rDone) {
        dutMem.ar.ready := true.B
        memState := sMemIdle
      }
    }
  }
}
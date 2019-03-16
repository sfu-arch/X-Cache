// See LICENSE for license details.

package accel

import chisel3._
import chisel3.util._
import junctions._
import config._
import interfaces._
import NastiConstants._
import accel._

class CacheModel(implicit val p: Parameters) extends Module with CacheParams {
  val io = IO(new CacheModuleIO)

  val size = log2Ceil(nastiXDataBits / 8).U
  val len  = (dataBeats - 1).U

  val data = Mem(nSets, Vec(bBytes, UInt(8.W))) // nSets deep, block size wide
  val tags  = Mem(nSets, UInt(tlen.W))
  val valid = Mem(nSets, Bool( ))
  val dirty = Mem(nSets, Bool( ))

  val req    = io.cpu.req.bits
  val tag    = (req.addr >> (blen + slen).U) (tlen, 0)
  val setIdx = req.addr(blen + slen - 1, blen) // set index of cache (assumes ways = 1)
  val byteIdx = req.addr(blen - 1, 0) // byte index of block
  val wordIdx = req.addr(blen - 1, byteOffsetBits)

  val readData  = data(setIdx)
  val writeMask = Vec(bBytes, Bool( ))
  val writeData = Vec(bBytes, UInt(8.W))

  def extractUInt(src: Vec[UInt], len: Int, offset: UInt): UInt = {
    val result = WireInit(0.U)
    for (i <- 0 until len) {
      result((i + 1) * 8 - 1, i * 8) := src(i.U + offset)
    }
    result
  }

  def insertBytes(src: UInt, dst: Vec[UInt], len: Int, offset: UInt): Vec[UInt] = {
    val result = dst;
    for (i <- 0 until len) {
      result(i.U + offset) := src((i + 1) * 8 - 1, i * 8)
    }
    result
  }

  val sIdle :: sWrite :: sWrAck :: sRead :: Nil = Enum(4)
  val state                                     = RegInit(sIdle)
  val (wCnt, wDone)                             = Counter(state === sWrite, dataBeats)
  val (rCnt, rDone)                             = Counter(state === sRead && io.nasti.r.valid, dataBeats)

  io.cpu.resp.bits.data := extractUInt(readData, wBytes, wordIdx)
  io.cpu.resp.valid := false.B
  io.cpu.resp.bits.tag := tag
  io.cpu.resp.bits.iswrite := req.iswrite
  io.cpu.resp.bits.valid := false.B

  io.cpu.req.ready := false.B
  io.nasti.ar.bits := NastiReadAddressChannel(0.U, ((req.addr >> blen.U) << blen.U).asUInt( ), size, len)
  io.nasti.ar.valid := false.B
  io.nasti.aw.bits := NastiWriteAddressChannel(0.U, (Cat(tags(setIdx), setIdx) << blen.U).asUInt( ), size, len)
  io.nasti.aw.valid := false.B
  io.nasti.w.bits := NastiWriteDataChannel(wCnt, None, wDone)
  io.nasti.w.valid := state === sWrite
  io.nasti.b.ready := state === sWrAck
  io.nasti.r.ready := true.B

  // Dump state
  io.stat := state.asUInt( )

  writeData.foreach(_ := 0.U(8.W)) // Default
  switch(state) {
    is(sIdle) {
      when(io.cpu.req.valid) {
        when(valid(setIdx) && (tags(setIdx) === tag)) {
          when(req.mask.orR) {
            dirty(setIdx) := true.B
            writeData := insertBytes(io.nasti.r.bits.data, writeData, wBytes, (wCnt << byteOffsetBits).asUInt( ))
            data.write(setIdx, writeData, writeMask)
            printf("[cache] data[%x] <= %x, off: %x, req: %x, mask: %b\n",
              setIdx, extractUInt(writeData, bBytes, 0.U), byteIdx, io.cpu.req.bits.data, io.cpu.req.bits.mask)
          }.otherwise {
            printf("[cache] data[%x] => %x, off: %x, resp: %x\n",
              setIdx, extractUInt(readData, bBytes, 0.U), byteIdx, io.cpu.resp.bits.data)
          }
          io.cpu.req.ready := true.B
          io.cpu.resp.valid := true.B
        }.otherwise {
          when(dirty(setIdx)) {
            io.nasti.aw.valid := true.B
            state := sWrite
          }.otherwise {
            data(setIdx) := 0.U
            io.nasti.ar.valid := true.B
            state := sRead
          }
        }
      }
    }
    is(sWrite) {
      when(wDone) {
        state := sWrAck
      }
    }
    is(sWrAck) {
      when(io.nasti.b.valid) {
        data(setIdx) := 0.U
        io.nasti.ar.valid := true.B
        state := sRead
      }
    }
    is(sRead) {
      when(io.nasti.r.valid) {
        writeData := insertBytes(io.nasti.r.bits.data, readData, wBytes, (rCnt << byteOffsetBits).asUInt( ))
        data(setIdx) := writeData
      }
      when(rDone) {
        assert(io.nasti.r.bits.last)
        tags(setIdx) := tag
        valid(setIdx) := true.B
        state := sIdle
      }
    }
  }

}

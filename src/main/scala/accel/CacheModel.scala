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

  val size  = log2Ceil(nastiXDataBits / 8).U
  val len   = (dataBeats - 1).U

  val data = Mem(nSets, UInt(bBits.W))
  val tags = Mem(nSets, UInt(tlen.W))
  val v    = Mem(nSets, Bool())
  val d    = Mem(nSets, Bool())

  val req   = io.cpu.req.bits
  val tag   = req.addr >> (blen + slen).U
  val idx   = req.addr(blen + slen - 1, blen)
  val off   = req.addr(blen - 1, 0)
  val read  = data(idx)
  val write = (((0 until bBytes) foldLeft 0.U){ (write, i) => write | Mux(
    ((off / 4.U) === (i / 4).U) && (req.mask >> (i & 0x3).U)(0),
    ((req.data >> ((8 * (i & 0x3)).U)) & 0xff.U) << (8 * i).U, read & (BigInt(0xff) << (8 * i)).U)
  })(bBits - 1, 0)

  val sIdle :: sWrite :: sWrAck :: sRead :: Nil = Enum(4)
  val state = RegInit(sIdle)
  val (wCnt, wDone) = Counter(state === sWrite, dataBeats)
  val (rCnt, rDone) = Counter(state === sRead && io.nasti.r.valid, dataBeats)

  io.cpu.resp.bits.data := read >> ((off / 4.U) * xlen.U)
  io.cpu.resp.valid := false.B
  io.cpu.resp.bits.tag  := tag
  io.cpu.resp.bits.isSt := req.iswrite
  io.cpu.resp.bits.valid := false.B

  io.cpu.req.ready := false.B
  io.nasti.ar.bits := NastiReadAddressChannel(0.U, (req.addr >> blen.U) << blen.U, size, len)
  io.nasti.ar.valid := false.B
  io.nasti.aw.bits := NastiWriteAddressChannel(0.U, Cat(tags(idx), idx) << blen.U, size, len)
  io.nasti.aw.valid := false.B
  io.nasti.w.bits := NastiWriteDataChannel(read >> (wCnt * nastiXDataBits.U), None, wDone)
  io.nasti.w.valid := state === sWrite
  io.nasti.b.ready := state === sWrAck
  io.nasti.r.ready := true.B

  // Dump state
  io.stat := state.asUInt()

  switch(state) {
    is(sIdle) {
      when(io.cpu.req.valid) {
        when(v(idx) && (tags(idx) === tag)) {
          when(req.mask.orR) {
            d(idx)    := true.B
            data(idx) := write
            printf("[cache] data[%x] <= %x, off: %x, req: %x, mask: %b\n",
              idx, write, off, io.cpu.req.bits.data, io.cpu.req.bits.mask)
          }.otherwise {
            printf("[cache] data[%x] => %x, off: %x, resp: %x\n",
              idx, read, off, io.cpu.resp.bits.data)
          }
          io.cpu.req.ready := true.B
          io.cpu.resp.valid := true.B
        }.otherwise {
          when(d(idx)) {
            io.nasti.aw.valid := true.B
            state := sWrite
          }.otherwise {
            data(idx) := 0.U
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
        data(idx) := 0.U
        io.nasti.ar.valid := true.B
        state := sRead
      }
    }
    is(sRead) {
      when(io.nasti.r.valid) {
        data(idx) := read | (io.nasti.r.bits.data << (rCnt * nastiXDataBits.U))
      }
      when(rDone) {
        assert(io.nasti.r.bits.last)
        tags(idx) := tag
        v(idx) := true.B
        state := sIdle
      }
    }
  }

}

package accel

import chisel3._
import chisel3.util._
import utility.UniformPrintfs
import config._

/**
  * The Core class contains the dataflow logic for the accelerator.
  * This particular core file implements a simple memory test routine to
  * validate the register interface and the Nasti bus operation on an SoC FPGA.
  *
  * @param p Project parameters. Only xlen is used to specify register and
  *          data bus width.
  * @note io.ctrl  A control register (from SimpleReg block) to start test
  * @note io.addr  A control register containing the physical address for
  *       the test
  * @note io.len   A control register containing the length of the memory
  *       test (number of words)
  * @note io.stat  A status register containing the current state of the test
  * @note io.cache A Read/Write request interface to a memory cache block
  */

abstract class CoreDFIO(implicit val p: Parameters) extends Module with CoreParams with UniformPrintfs
{
  val io = IO(
    new Bundle {
      val start  = Input(Bool())
      val init   = Input(Bool())
      val ready  = Output(Bool())
      val done   = Output(Bool())
      val ctrl   = Input(UInt(xlen.W))
      val addr   = Input(UInt(xlen.W))
      val len    = Input(UInt(xlen.W))
      val stat   = Output(UInt(xlen.W))
//      val errstat   = Output(UInt(xlen.W)) TODO : Need to have a err signal with error codes
      val cache  = Flipped(new CacheIO)
    }
  )
}


abstract class CoreT(implicit p: Parameters) extends CoreDFIO()(p) {
}




class Core(implicit p: Parameters) extends CoreT()(p) {

  val dataBytes = xlen / 8
  val reqAddr = Reg(UInt(32.W))
  val reqTag = Reg(io.cache.req.bits.tag.cloneType)
  val wordCount = Reg(UInt(32.W))

  val writeData = wordCount
  val expectedData = wordCount

  val (sIdle :: sWriteReq :: sWriteResp :: sReadReq :: sReadResp :: sDone :: Nil) = Enum(6)
  val state = RegInit(init = sIdle)
  val stall = !io.cache.resp.valid
  val errorLatch = Reg(Bool())

  switch(state) {
   // Idle
    is(sIdle) {
      reqAddr := io.addr(31, 0)
      wordCount := 0.U
      when(io.start) {
        when(io.ctrl(0) === true.B) {
          state := sReadReq
          reqTag := 1.U
        }.otherwise {
          state := sWriteReq
        }
      }
    }
    // Write
    is(sWriteReq) {
      when(io.cache.req.ready) {
        state := sWriteResp
      }
    }
    is(sWriteResp) {
      when(!stall) {
        wordCount := wordCount + 1.U
        reqAddr := reqAddr + dataBytes.U
        when(wordCount < io.len) {
          state := sWriteReq
        }.otherwise {
          state := sDone
        }
      }
    }
    // Read
    is(sReadReq) {
      when(io.cache.req.ready) {
        state := sReadResp
      }
    }
    is(sReadResp) {
      when(!stall) {
        wordCount := wordCount + 1.U
        reqAddr := reqAddr + dataBytes.U
        when(wordCount < io.len) {
          state := sReadReq
        }.otherwise {
          state := sDone
        }
      }
    }
    // Done
    is(sDone) {
      when(io.init) {
        state := sIdle
      }
    }
  }

  io.cache.req.valid := (state === sReadReq || state === sWriteReq ||
    state === sReadResp || state === sWriteResp)
  io.cache.req.bits.addr := reqAddr
  io.cache.req.bits.data := writeData
  io.cache.req.bits.tag := reqTag
  io.cache.req.bits.iswrite := (state === sWriteReq || state === sWriteResp)

  when(state === sWriteReq || state === sWriteResp) {
    io.cache.req.bits.mask := ~0.U(dataBytes.W)
  }.otherwise {
    io.cache.req.bits.mask := 0.U(dataBytes.W)
  }

  when(state===sReadResp && io.cache.resp.valid && io.cache.resp.bits.data =/= expectedData) {
    errorLatch := true.B
  }.elsewhen(io.init){
    errorLatch := false.B
  }

  // Reflect state machine status to processor
  io.done := (state === sDone)
  io.ready := (state === sIdle)
  io.stat := Cat(errorLatch, state.asUInt())


}

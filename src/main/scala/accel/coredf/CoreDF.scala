package accel.coredf

/**
  * Created by nvedula on 28/6/17.
  */

import chisel3._
import chisel3.util._

import config._
import accel._
import dataflow.tests._

/**
  * The Core class creates contains the dataflow logic for the accelerator.
  * This particular core file implements a simple memory test routine to
  * validate the register interface and the Nasti bus operation on an SoC FPGA.
  *
  * @param p Project parameters. Only xlen is used to specify register and
  *          data bus width.
  *
  * @note io.ctrl  A control register (from SimpleReg block) to start test
  * @note io.addr  A control register containing the physical address for
  *                the test
  * @note io.len   A control register containing the length of the memory
  *                test (number of words)
  * @note io.stat  A status register containing the current state of the test
  * @note io.cache A Read/Write request interface to a memory cache block
  */

abstract class CoreIO(implicit val p: Parameters) extends Module with CoreParams
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
      val cache  = Flipped(new CacheIO)
    }
  )
}

class Core(implicit p: Parameters) extends CoreIO()(p) {

  val dataBytes  = xlen / 8
  val req_addr   = Reg(UInt(32.W))
  val word_count = Reg(UInt(32.W))

  val write_data    = word_count
  val expected_data = word_count

  val (s_idle :: s_write_req :: s_write_resp :: s_read_req :: s_read_resp :: s_done :: Nil) = Enum(6)
  val state = RegInit(init = s_idle)
  val stall = !io.cache.resp.valid
  val err_latch = Reg(Bool())

  val core = Module(new Add01DF())
  switch (state) {
    // Idle
    is (s_idle) {
      req_addr := io.addr(31,0)
      word_count := 0.U

      when(io.start ) {
        Add01DF.io.start := true.B
      }
//      // Start on a rising edge of start bit
//      when (io.start) {
//        when(io.ctrl(0) === true.B) {
//          state := s_read_req
//        } .otherwise {
//          state := s_write_req
//        }
//      }
    }

    // Done
    is (s_done) {
      when(io.init) {
        state := s_idle
      }
    }
  }

  //  io.cache.req.valid     := (state === s_read_req || state === s_write_req ||
  //    state === s_read_resp || state === s_write_resp)
  //  io.cache.req.bits.addr := req_addr
  //  io.cache.req.bits.data := write_data
  //  io.cache.req.bits.tag  := 1.U

  //  when (state === s_write_req || state === s_write_resp) {
  //    io.cache.req.bits.mask := ~0.U(dataBytes.W)
  //  } .otherwise {
  //    io.cache.req.bits.mask := 0.U(dataBytes.W)
  //  }

  //  val read_data = io.cache.resp.bits.data
  //  when (io.cache.resp.valid && io.cache.resp.bits.tag === 1.U && read_data =/= expected_data) {
  //    err_latch := true.B
  //  }

  // Reflect state machine status to processor
  io.done  := (state === s_done)
  io.ready := (state === s_idle)
  io.stat  := Cat(err_latch,state.asUInt())


}

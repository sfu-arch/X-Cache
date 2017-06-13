package accel

import chisel3._
import chisel3.util._

import config._

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

  val start_reg = RegNext(init=false.B,next=io.ctrl(0));

  switch (state) {
    // Idle
    is (s_idle) {
      req_addr := io.addr(31,0)
      word_count := 0.U
      // Start on a rising edge of start bit
      when (io.ctrl(0) === true.B && start_reg === false.B) {
        when(io.ctrl(1) === true.B) {
          state := s_read_req
        } .otherwise {
          state := s_write_req
        }
      }
    }
    // Write
    is (s_write_req) {
      state := s_write_resp
    }
    is (s_write_resp) {
      when(!stall) {
	word_count := word_count + 1.U
	req_addr := req_addr + dataBytes.U
	when (word_count < io.len) {
          state := s_write_req
	} .otherwise {
          state := s_done
	}
      }
    }
    // Read
    is (s_read_req) {
      state := s_read_resp
    }
    is (s_read_resp) {
      when(!stall) {
        word_count := word_count + 1.U
        req_addr := req_addr + dataBytes.U
        when (word_count < io.len) {
          state := s_read_req
        } .otherwise {
          state := s_done
        }
      }
    }
    // Done
    is (s_done) {
      state := s_idle
    }
  }

  io.cache.req.valid     := (state === s_read_req || state === s_write_req ||
                             state === s_read_resp || state === s_write_resp)
  io.cache.req.bits.addr := req_addr
  io.cache.req.bits.data := write_data

  when (state === s_write_req || state === s_write_resp) {
    io.cache.req.bits.mask := ~0.U(dataBytes.W)
  } .otherwise {
    io.cache.req.bits.mask := 0.U(dataBytes.W)
  }

  io.stat := state.asUInt()

//  val full_addr = req_addr + (word_count << log2Up(dataBytes).U)
//  val byteshift = full_addr(log2Up(dataBytes) - 1, 0)
//  val bitshift = Cat(byteshift, 0.U(3.W))
  val read_data = io.cache.resp.bits.data

  assert(!io.cache.resp.valid || read_data === expected_data, s"MemTest Core got wrong data")
}

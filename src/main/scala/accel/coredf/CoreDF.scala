package accel.coredf

/**
  * Created by nvedula on 28/6/17.
  */

import chisel3._
import chisel3.util._
import utility.UniformPrintfs
import interfaces._
import util._
import config._
import accel._
import dataflow.tests._
import node.HandShaking

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

class TestCore(implicit p: Parameters) extends CoreDFIO()(p) {


  val (s_idle :: s_busy :: s_done :: Nil) = Enum(3)
  val state = RegInit(init = s_idle)
  //  val err_latch = Reg(Bool())
  val add_result_reg = Reg(UInt(xlen.W))
  val start_reg = RegInit(false.B)

  val addDF = Module(new Add01DF())

  override val printfSigil = "CoreDF:  add_result_reg: " + add_result_reg.asUInt() + " state: " + state + " "

  //IO Connections
  addDF.io.start := start_reg
  addDF.io.Data0.bits.data      := io.ctrl(xlen-1,0)
  addDF.io.Data0.bits.predicate := true.B
  addDF.io.Data0.bits.valid     := true.B

  addDF.io.Data1.bits.data      := io.addr(xlen-1,0)
  addDF.io.Data1.bits.predicate := true.B
  addDF.io.Data1.bits.valid     := true.B
  //result is Decoupled
  io.stat <> add_result_reg

  //Switch OFF CacheIO
  io.cache.req.valid := false.B




  switch (state) {
    // Idle
    is(s_idle) {

      when(io.start) {
        start_reg := true.B
        state := s_busy
      }
    }

    is(s_busy) {

      when(addDF.io.result.valid) {
        state := s_done
        add_result_reg := addDF.io.result.bits.data(xlen-1,0)
      }

    }


    // Done
    is (s_done) {

      start_reg := false.B
      when(io.init) {
        add_result_reg := 0.U
        state := s_idle
      }
    }
  }

  // Reflect state machine status to processor
  io.done  := (state === s_done)
  io.ready := (state === s_idle)
//  io.stat  := Cat(err_latch,state.asUInt())

  // Intermediate
  //  addDF.io.result.ready  := (state === s_busy)
  addDF.io.result.ready  := true.B

  addDF.io.Data0.valid := (state === s_busy || (state === s_idle && io.start))
  addDF.io.Data1.valid := (state === s_busy || (state === s_idle && io.start))

  printf(p"-----------------------------------------------------\n")
  printf(p"add_result_reg: ${add_result_reg} ")
  printf(p"io.result.bits.data: ${addDF.io.result.bits.data} ")
  printf(p"io.result.bits.predicate: ${addDF.io.result.bits.predicate} ")
  printf(p"io.result.valid: ${addDF.io.result.valid} ")
  printf(p"io.result.ready: ${addDF.io.result.ready} \n")

  printfInfo(" State: %x\n", state)

}

package memory.unittest

/**
  * Created by vnaveen0 on 3/6/17.
  */


import chisel3._
import chisel3.Module
import config._
import util._
import interfaces._


class ArbiterTest(NReads: Int, NWrites: Int)(implicit val p: Parameters) extends
  Module with CoreParams {
  val io = IO(new Bundle {
    val ReadIn = Vec(NReads, Flipped(Decoupled(new ReadReq())))
    val ReadOut = Vec(NReads, Output(new ReadReq()))
    val chosen  = Output(UInt())
    val ready  = Input(Bool())
    val valid = Output(Bool())
  })

  //----------------------------------------------------------------------------------------

  // ReadArbiter

  val readArbiter = Module(new RRArbiter(new ReadReq(), NReads))
  //----------------------------------------------------------------------------------------
  // Connecting ReadIn with Read Arbiter

  for (i <- 0 until NReads) {
    readArbiter.io.in(i) <> io.ReadIn(i)
  }

  //   readArbiter.io.out.bits
  //  readArbiter.io.chosen

  for (i <- 0 until NReads) {
    io.ReadOut(i) <> readArbiter.io.out.bits
    io.chosen <> readArbiter.io.chosen
    readArbiter.io.out.ready := io.ready
    io.valid <> readArbiter.io.out.valid
  }

}



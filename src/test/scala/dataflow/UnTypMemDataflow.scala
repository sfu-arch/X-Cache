// // See LICENSE for license details.

package dataflow

import chisel3._
import chisel3.util._

import chisel3.testers._
import chisel3.iotesters._
import org.scalatest.{Matchers, FlatSpec}

import config._
import util._
import interfaces._
import accel._

class CacheWrapper(val ops:Int)(implicit val p: Parameters) extends Module with CacheParams {
  val io = IO(new Bundle{
    val Out = Vec(ops, Decoupled(new DataBundle()))
  })
  val c = Module(new UnTypMemDataFlow(ops)(p))

  // Instantiate the AXI Cache
  val cache = Module(new Cache)
  cache.io.cpu.req <> c.io.MemReq
  c.io.MemResp <> cache.io.cpu.resp
  cache.io.cpu.abort := false.B

  // Instantiate a memory model with AXI slave interface for cache
  val memModel = Module(new NastiMemSlave)
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B

  io.Out <> c.io.Out

}

class UnTypMemDataFlowTester(df: CacheWrapper)(implicit p: config.Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 200){
		step(1)

    for (i <- 0 until df.ops) {
      poke(df.io.Out(i).ready, true.B)
      if (peek(df.io.Out(i).valid) == 1)
      {
        // Check that the output sum is correct
        var e = 4*(i+1)-1
        var data = peek(df.io.Out(i).bits.data)
        expect(data == e, s"Out${i} = ${data}, should be ${e}, ")
      }
    }
	}
}

class UnTypMemDataflowTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val numOps = 4
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver.execute(Array(
       // "-ll", "Info",
       "-tbn", "verilator",
       "-td", "test_run_dir",
       "-tts", "0001"),
       () => new CacheWrapper(numOps)(p)) { c =>
       new UnTypMemDataFlowTester(c)
     } should be(true)
   }
 }




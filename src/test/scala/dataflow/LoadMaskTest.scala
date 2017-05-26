package dataflow

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import node._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._



class LoadDFTests (c: LoadMaskNode)(implicit p: config.Parameters) extends PeekPokeTester(c) {
  poke(c.io.PredMemOp(0).valid,false.B)
  poke(c.io.PredMemOp(1).valid,false.B)
  poke(c.io.Gep.valid,false.B)
  poke(c.io.MemReq.ready,false.B)
  poke(c.io.MemReq.valid,false.B)
  poke(c.io.MemResp.valid,false.B)

  for (t <- 0 until 12) {
    step(1)
    //IF ready is set
    // send address

    if (peek(c.io.MemReq.valid) == 1) {
      println("Setting Ready")
      poke(c.io.MemReq.ready,true.B)
    } 

    if (t == 6)
    {
      poke(c.io.MemResp.bits.data, 0x0eddbeef.U)
      poke(c.io.MemResp.valid,true.B)
    }

      println(s"\n rule1 fired ${peek(c.io.MemReq.valid)} \n")
      poke(c.io.Gep.valid,true.B)
      poke(c.io.PredMemOp(0).valid,true.B)
      poke(c.io.PredMemOp(1).valid,true.B)
      poke(c.io.Gep.bits,10.U)

  }
}

//class LoadNodeTester extends ChiselFlatSpec {
  //behavior of "LoadNode"
  //backends foreach {backend =>
    //it should s"correctly find decoupled behaviour -  $backend" in {
      //Driver(() => new LoadNode(32), backend)((c) => new LoadNodeTests(c)) should be (true)
    //}
  //}
//}

class LoadDFTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
 it should "Load Node tester" in {
    chisel3.iotesters.Driver(() => new LoadMaskNode(NumPredMemOps = 2)) { c =>
      new LoadDFTests(c)
    } should be(true)
  }
}

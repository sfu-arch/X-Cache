package dandelion.dataflow

import chisel3._
import chisel3.util._
import dandelion.node._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._


class LoadMaskTests (c: LoadMaskNode)(implicit p: Parameters) extends PeekPokeTester(c) {
  poke(c.io.MemReq.ready,false.B)
  poke(c.io.MemReq.valid,false.B)
  poke(c.io.MemResp.valid,false.B)
  poke(c.io.Gep.valid,true.B)
  poke(c.io.PredOp(0).valid,true.B)
  poke(c.io.PredOp(1).valid,true.B)
  poke(c.io.Gep.bits,10.U)



  for (t <- 0 until 10) {
    step(1)
    //IF ready is set
    // send address

    if (peek(c.io.MemReq.valid) == 1) {
      println("Setting Ready")
      poke(c.io.MemReq.ready,true.B)
    } 

    if (t > 6)
    {
      poke(c.io.MemResp.bits.data, (0xfeddbeeeL).U)
      poke(c.io.MemResp.valid,true.B)
    }


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

class LoadMaskTester extends  FlatSpec with Matchers {
  implicit val p = new WithAccelConfig ++ new WithTestConfig
/*
 it should "Load Node tester" in {

    chisel3.iotesters.Driver(() => new LoadMaskNode(NumPredOps = 2)) { c =>
      new LoadMaskTests(c)
    } should be(true)
  }
*/
}

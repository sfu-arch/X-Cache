package node

/**
  * Created by nvedula on 15/5/17.
  */


import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import utility._

class TypLoadTests(c: TypLoad) extends PeekPokeTester(c) {
    poke(c.io.GepAddr.valid,false)
    poke(c.io.enable.valid,false)
    poke(c.io.memReq.ready,false)
    poke(c.io.memResp.valid,false)
    poke(c.io.Out(0).ready,true)


    for (t <- 0 until 20) {

     step(1)

      //IF ready is set
      // send address
      if (peek(c.io.GepAddr.ready) == 1) {
        poke(c.io.GepAddr.valid, true)
        poke(c.io.GepAddr.bits.data, 12)
        poke(c.io.GepAddr.bits.predicate, true)
        poke(c.io.enable.bits,true)
        poke(c.io.enable.valid,true)
      }

      printf(s"t: ${t}  c.io.memReq: ${peek(c.io.memReq)} \n")
      if((peek(c.io.memReq.valid) == 1) && (t > 4))
      {
        poke(c.io.memReq.ready,true)
      }

     if (t > 8)
      {
        poke(c.io.memResp.valid, true)
        poke(c.io.memResp.data, 0x1eadbeef)
      }
  }
}


import Constants._

class TypLoadTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Load Node tester" in {
    chisel3.iotesters.Driver(() => new TypLoad(NumPredOps=0,NumSuccOps=0,NumOuts=1,ID=1,RouteID=0)) { c =>
      new TypLoadTests(c)
    } should be(true)
  }
}

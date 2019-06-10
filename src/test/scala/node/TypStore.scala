package node

/**
  * Created by nvedula on 15/5/17.
  */


import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._
import utility._

class TypStoreTests(c: TypStore) extends PeekPokeTester(c) {
    poke(c.io.GepAddr.valid,false)
    poke(c.io.enable.valid,false)
    poke(c.io.inData.valid,false)
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
        poke(c.io.inData.valid, true)
        poke(c.io.inData.bits.data, 0xbeef1eadbeeeL)
        poke(c.io.inData.bits.predicate,true)
// //         poke(c.io.inData.bits.valid,true)
        poke(c.io.enable.bits.control,true)
        poke(c.io.enable.valid,true)
      }

       printf(s"t: ${t}  c.io.memReq: ${peek(c.io.memReq)} \n")
      if((peek(c.io.memReq.valid) == 1) && (t > 4))
      {
        poke(c.io.memReq.ready,true)
      }

     if (t > 8)
      {
        // poke(c.io.memReq.ready,false)
        // poke(c.io.memResp.data,t)
        poke(c.io.memResp.valid,true)
      }


    // }
  }
}


import Constants._

class TypStoreTester extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Store Node tester" in {
    chisel3.iotesters.Driver(() => new TypStore(NumPredOps=0,NumSuccOps=0,NumOuts=1,ID=1,RouteID=0)) { c =>
      new TypStoreTests(c)
    } should be(true)
  }
}

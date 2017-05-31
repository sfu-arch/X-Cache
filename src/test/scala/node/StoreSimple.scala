package node

/**
  * Created by nvedula on 15/5/17.
  */


import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._


class StoreSimpleNodeTests(c: StoreSimpleNode) extends PeekPokeTester(c) {
    poke(c.io.GepAddr.valid,false)
    poke(c.io.inData.valid,false)
    poke(c.io.PredMemOp(0).valid,true)
    poke(c.io.memReq.ready,false)
    poke(c.io.memResp.valid,false)
    poke(c.io.SuccMemOp(0).ready,true)
    poke(c.io.Out(0).ready,true)
    poke(c.io.Out(1).ready,true)

    for (t <- 0 until 20) {

     step(1)

      //IF ready is set
      // send address
      if (peek(c.io.GepAddr.ready) == 1) {
       
        poke(c.io.GepAddr.valid, true)
        poke(c.io.GepAddr.bits, 12)
        poke(c.io.inData.valid, true)
        poke(c.io.inData.bits, t+1)
      }
      
      if((peek(c.io.memReq.valid) == 1) && (t > 4))
      {
        printf(s"\n t: ${t} gepAddr Fire \n")
        poke(c.io.memReq.ready,true)

      }

      if (t > 5 && peek(c.io.memReq.ready) == 1)
      {
        // poke(c.io.memReq.ready,false)
        // poke(c.io.memResp.data,t)
        poke(c.io.memResp.valid,true)
      }
    }
}

import Constants._

class StoreSimpleNodeTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Store Node tester" in {
    chisel3.iotesters.Driver(() => new StoreSimpleNode(NumPredMemOps=1,NumSuccMemOps=1,NumOuts=2,Typ=MT_W,ID=1)) { c =>
      new StoreSimpleNodeTests(c)
    } should be(true)
  }
}

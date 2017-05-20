package node

/**
  * Created by nvedula on 12/5/17.
  */
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._


class StoreNodeTests(c: StoreNode) extends PeekPokeTester(c) {
  for (t <- 0 until 12) {

    //IF ready is set
    // send address
    if(peek(c.io.gepAddr.ready) == 1) {
      //      printf("\n rule1 fired \n")
      poke(c.io.gepAddr.valid,true)
      poke(c.io.gepAddr.bits,12)
    }

    //IF ready is set
    // send data
    if(peek(c.io.inData.ready) == 1) {
      //      printf("\n rule2 fired \n")
      poke(c.io.inData.valid,true)
      poke(c.io.inData.bits,24)
    }


    //Memory is always ready to receive the memory requests
    //TODO make them as single signal
    poke(c.io.memReq.ready, true)
    //    poke(c.io.Memreq_data.ready, true)
    //When StoreNode requests the data print the contents

    if(peek(c.io.memReq.valid) == 1 ) {

      println(s"t: ${t}  io.Memreq_addr: ${peek(c.io.memReq.bits.address)} " +
        s"io.Memreq_data: ${peek(c.io.memReq.bits.data)}  io.memReq.bits.mask : ${peek(c.io.memReq.bits.node)}")



      //      step (1)
      //      step (1)
      step (1)
      //since response is available atleast next cycle onwards
      poke(c.io.memResp.valid, true)
      println(s"t: ${t}  io.Memresp_ack_valid: ${peek(c.io.memResp.valid)}")

    }



    //at some clock - send src mem-op is done executing
    if(t > 4) {
      if (peek(c.io.predMemOp(0).ready) == 1) {
        poke(c.io.predMemOp(0).valid, true)
        //        poke(c.io.predMemOp(0).bits, 24)
      }
      else
        poke(c.io.predMemOp(0).valid, false)

    }
    else {

      poke(c.io.predMemOp(0).valid, false)
    }


    //poke for output after clock 7
    if(t>7) {
      poke(c.io.memOpAck.ready, true)
      println(s"t: ${t} io.memOpAck.valid ${peek(c.io.memOpAck.valid)} io.memOpAck.ready: ${peek(c.io.memOpAck.ready)}")
    }

    //    println(s"t: ${t} io.gepAddr.bits: ${peek(c.io.gepAddr.bits)}, io.gepAddr.valid: ${peek(c.io.gepAddr.valid)} io.gepAddr.ready: " +
    //      s"${peek(c.io.gepAddr.ready)}")
    //
    //
    //    println(s"t: ${t} io.inData.bits: ${peek(c.io.inData.bits)}, io.inData.valid: ${peek(c.io.inData.valid)} io.inData.ready: " +
    //      s"${peek(c.io.inData.ready)}")

    //    println(s"t: ${t} io.predMemOp(0).valid: ${peek(c.io.predMemOp(0).valid)} io.predMemOp(0).ready: " +
    //      s"${peek(c.io.predMemOp(0).ready)}")


    if(peek(c.io.predMemOp(0).valid) ==1)
      println(s"t: ${t}  io.predMemOp(0).valid: ${peek(c.io.predMemOp(0).valid)}  io.predMemOp(0).ready: ${peek(c.io.predMemOp(0).ready)}  ")

    step(1)

  }
}

//class StoreNodeTester extends ChiselFlatSpec {
//  behavior of "StoreNode"
//  backends foreach {backend =>
//    it should s"correctly find decoupled behaviour -  $backend" in {
//      Driver(() => new StoreNode(32), backend)((c) => new StoreNodeTests(c)) should be (true)
//    }
//  }
//}

class StoreNodeTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Store Node tester" in {
    chisel3.iotesters.Driver(() => new StoreNode()) { c =>
      new StoreNodeTests(c)
    } should be(true)
  }
}

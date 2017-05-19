package dataflow

/**
  * Created by nvedula on 17/5/17.
  */


import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._


class StoreDFTests(c: StoreDataFlow) extends PeekPokeTester(c) {
  for (t <- 0 until 100) {

    //IF ready is set
    // send address


    println(s"t: ${t} GepAddr: ${peek(c.io.gepAddr_ready)}")
    //    println(s"GEPTETS: ${peek(c.io.testReady)}")
    println(s"t: ${t} GepValid: ${peek(c.io.gepAddr_valid)}")


    if(peek(c.io.gepAddr_ready) == 1 && t>2) {
      poke(c.io.gepAddr_valid,true)
      poke(c.io.gepAddr_bits,12)

      printf("\n rule1 fired \n")
    }

    if(peek(c.io.inData.ready) == 1 && t>2) {
      printf("\n rule2 fired \n")
      poke(c.io.inData.valid,true)
      poke(c.io.inData.bits,12)
    }



    //Memory is always ready to receive the memory requests
    //TODO make them as single signal
    //poke(c.io.memReq.ready, true)

    //if(peek(c.io.memReq.valid) == 1 ) {

    //println(s"t: ${t}  io.memLDIO.Memreq_addr: ${peek(c.io.memReq.bits.address)} ")


    ////      step (1)
    ////      step (1)
    //step (1)
    ////since response is available atleast next cycle onwards
    //if(peek(c.io.memResp.ready) == true ) {
    //poke(c.io.memResp.valid, true)

    //println(s"t: ${t}  io.Memresp_ack_ready: ${peek(c.io.memResp.ready)}")
    //}

    //}



    //at some clock - send src mem-op is done executing
    //    if(t > 4) {
    //      if (peek(c.io.predMemOp.ready) == 1) {
    //        poke(c.io.predMemOp.valid, true)
    //        println("\n predmemOP rule fired \n")
    //        //        poke(c.io.predMemOp(0).bits, 24)
    //      }
    //      else
    //        poke(c.io.predMemOp.valid, false)
    //
    //    }
    //    else {
    //
    //      poke(c.io.predMemOp.valid, false)
    //    }
    //
    //
    //    //poke for output after clock 7
    //    if(t>7) {
    //      poke(c.io.memOpAck.ready, true)
    //      println(s"t: ${t} io.memOpAck.valid ${peek(c.io.memOpAck.valid)} io.memOpAck.ready: ${peek(c.io.memOpAck.ready)}")
    //    }
    //
    //    //    println(s"t: ${t} io.gepAddr.bits: ${peek(c.io.gepAddr.bits)}, io.gepAddr.valid: ${peek(c.io.gepAddr.valid)} io.gepAddr.ready: " +
    //    //      s"${peek(c.io.In1.ready)}")
    //    //
    //    //
    //    //    println(s"t: ${t} io.In2.bits: ${peek(c.io.In2.bits)}, io.In2.valid: ${peek(c.io.In2.valid)} io.In2.ready: " +
    //    //      s"${peek(c.io.In2.ready)}")
    //
    //    //    println(s"t: ${t} io.predMemOp(0).valid: ${peek(c.io.predMemOp(0).valid)} io.predMemOp(0).ready: " +
    //    //      s"${peek(c.io.predMemOp(0).ready)}")
    //
    //
    //    if(peek(c.io.predMemOp.valid) ==1)
    //      println(s"t: ${t}  io.predMemOp(0).valid: ${peek(c.io.predMemOp.valid)}  io.predMemOp(0).ready: ${peek(c.io.predMemOp.ready)}  ")

    step(1)

  }
}


class StoreDFTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Store Node tester" in {
    chisel3.iotesters.Driver(() => new StoreDataFlow()) { c =>
      new StoreDFTests(c)
    } should be(true)
  }
}

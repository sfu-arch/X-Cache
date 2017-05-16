package node

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._


class LoadNodeTests(c: LoadNode) extends PeekPokeTester(c) {
  for (t <- 0 until 12) {

    //IF ready is set
    // send address
    if(peek(c.io.gepAddr.ready) == 1) {
      //      printf("\n rule1 fired \n")
      poke(c.io.gepAddr.valid,true)
      poke(c.io.gepAddr.bits,12)
    }



    //Memory is always ready to receive the memory requests
    //TODO make them as single signal
    poke(c.io.memReq.ready, true)

    if(peek(c.io.memReq.valid) == 1 ) {

      println(s"t: ${t}  io.memLDIO.Memreq_addr: ${peek(c.io.memReq.bits.address)} ")


      //      step (1)
      //      step (1)
      step (1)
      //since response is available atleast next cycle onwards
      if(peek(c.io.memResp.ready) == true ) {
        poke(c.io.memResp.valid, true)

        println(s"t: ${t}  io.Memresp_ack_ready: ${peek(c.io.memResp.ready)}")
      }

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
    //      s"${peek(c.io.In1.ready)}")
    //
    //
    //    println(s"t: ${t} io.In2.bits: ${peek(c.io.In2.bits)}, io.In2.valid: ${peek(c.io.In2.valid)} io.In2.ready: " +
    //      s"${peek(c.io.In2.ready)}")

    //    println(s"t: ${t} io.predMemOp(0).valid: ${peek(c.io.predMemOp(0).valid)} io.predMemOp(0).ready: " +
    //      s"${peek(c.io.predMemOp(0).ready)}")


    if(peek(c.io.predMemOp(0).valid) ==1)
      println(s"t: ${t}  io.predMemOp(0).valid: ${peek(c.io.predMemOp(0).valid)}  io.predMemOp(0).ready: ${peek(c.io.predMemOp(0).ready)}  ")

    step(1)

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

class LoadNodeTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
 it should "Load Node tester" in {
    chisel3.iotesters.Driver(() => new LoadNode()) { c =>
      new LoadNodeTests(c)
    } should be(true)
  }
}

package amin

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3.core.RegInit
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class LoadNodeTests(c: LoadNode) extends PeekPokeTester(c) {
  for (t <- 0 until 12) {

    //IF ready is set
    // send address
    if(peek(c.io.In1.ready) == 1) {
      //      printf("\n rule1 fired \n")
      poke(c.io.In1.valid,true)
      poke(c.io.In1.bits,12)
    }



    //Memory is always ready to receive the memory requests
    //TODO make them as single signal
    poke(c.io.Memreq_addr.ready, true)

    if(peek(c.io.Memreq_addr.valid) == 1 ) {

      println(s"t: ${t}  io.Memreq_addr: ${peek(c.io.Memreq_addr.bits)} ")


      //      step (1)
      //      step (1)
      step (1)
      //since response is available atleast next cycle onwards
      if(peek(c.io.Memresp_data.ready) == true ) {
        poke(c.io.Memresp_data.valid, true)

        println(s"t: ${t}  io.Memresp_ack_ready: ${peek(c.io.Memresp_data.ready)}")
      }

    }



    //at some clock - send src mem-op is done executing
    if(t > 4) {
      if (peek(c.io.In3.ready) == 1) {
        poke(c.io.In3.valid, true)
        //        poke(c.io.In3.bits, 24)
      }
      else
        poke(c.io.In3.valid, false)

    }
    else {

      poke(c.io.In3.valid, false)
    }


    //poke for output after clock 7
    if(t>7) {
      poke(c.io.Out1.ready, true)
      println(s"t: ${t} io.Out1.valid ${peek(c.io.Out1.valid)} io.Out1.ready: ${peek(c.io.Out1.ready)}")
    }

    //    println(s"t: ${t} io.In1.bits: ${peek(c.io.In1.bits)}, io.In1.valid: ${peek(c.io.In1.valid)} io.In1.ready: " +
    //      s"${peek(c.io.In1.ready)}")
    //
    //
    //    println(s"t: ${t} io.In2.bits: ${peek(c.io.In2.bits)}, io.In2.valid: ${peek(c.io.In2.valid)} io.In2.ready: " +
    //      s"${peek(c.io.In2.ready)}")

    //    println(s"t: ${t} io.In3.valid: ${peek(c.io.In3.valid)} io.In3.ready: " +
    //      s"${peek(c.io.In3.ready)}")


    if(peek(c.io.In3.valid) ==1)
      println(s"t: ${t}  io.In3.valid: ${peek(c.io.In3.valid)}  io.In3.ready: ${peek(c.io.In3.ready)}  ")

    step(1)

  }
}

class LoadNodeTester extends ChiselFlatSpec {
  behavior of "LoadNode"
  backends foreach {backend =>
    it should s"correctly find decoupled behaviour -  $backend" in {
      Driver(() => new LoadNode(32), backend)((c) => new LoadNodeTests(c)) should be (true)
    }
  }
}

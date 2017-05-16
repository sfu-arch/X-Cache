package amin

/**
  * Created by nvedula on 12/5/17.
  */

import chisel3.core.RegInit
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class StoreNodeTests(c: StoreNode) extends PeekPokeTester(c) {
  for (t <- 0 until 12) {

    //IF ready is set
    // send address
    if(peek(c.io.In1.ready) == 1) {
      //      printf("\n rule1 fired \n")
      poke(c.io.In1.valid,true)
      poke(c.io.In1.bits,12)
    }

    //IF ready is set
    // send data
    if(peek(c.io.In2.ready) == 1) {
      //      printf("\n rule2 fired \n")
      poke(c.io.In2.valid,true)
      poke(c.io.In2.bits,24)
    }


    //Memory is always ready to receive the memory requests
    //TODO make them as single signal
    poke(c.io.Memreq_addr.ready, true)
    poke(c.io.Memreq_data.ready, true)
    //When StoreNode requests the data print the contents

    if(peek(c.io.Memreq_addr.valid) == 1 && peek(c.io.Memreq_data.valid) == 1  ) {

      println(s"t: ${t}  io.Memreq_addr: ${peek(c.io.Memreq_addr.bits)} " +
        s"io.Memreq_data: ${peek(c.io.Memreq_data.bits)} ")



      //      step (1)
      //      step (1)
      step (1)
      //since response is available atleast next cycle onwards
      if(peek(c.io.Memresp_ack.ready) == true ) {
        poke(c.io.Memresp_ack.valid, true)

        println(s"t: ${t}  io.Memresp_ack_ready: ${peek(c.io.Memresp_ack.ready)}")
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

class StoreNodeTester extends ChiselFlatSpec {
  behavior of "StoreNode"
  backends foreach {backend =>
    it should s"correctly find decoupled behaviour -  $backend" in {
      Driver(() => new StoreNode(32), backend)((c) => new StoreNodeTests(c)) should be (true)
    }
  }
}

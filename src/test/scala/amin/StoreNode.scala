package amin

/**
  * Created by nvedula on 12/5/17.
  */

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class StoreNodeTests(c: StoreNode) extends PeekPokeTester(c) {
  for (t <- 0 until 9) {

    //IF ready is set
    // send address
    if(peek(c.io.In1.ready) == 1) {
//      printf("\n rule1 fired \n")
      poke(c.io.In1.valid,true)
      poke(c.io.In1.bits,12)

//      println(s"t: ${t}  init1_reg: ${peek(c.init1_reg)} init2_reg: ${peek(c.init2_reg)}   init3_reg: ${peek(c.init3_reg)} ")
    }

    //IF ready is set
    // send data
    if(peek(c.io.In2.ready) == 1) {
//      printf("\n rule2 fired \n")
      poke(c.io.In2.valid,true)
      poke(c.io.In2.bits,24)
    }

    //at some clock - send src mem-op is done executing
    if(t > 4) {
      if (peek(c.io.In3.ready) == 1) {
        poke(c.io.In3.valid, true)
//        poke(c.io.In3.bits, 24)
      }
    }
    else {

      poke(c.io.In3.valid, false)
    }


    //poke for output after clock 7
    if(t>7) {
      poke(c.io.Out1.ready, true)
    }

//    println(s"t: ${t} io.In1.bits: ${peek(c.io.In1.bits)}, io.In1.valid: ${peek(c.io.In1.valid)} io.In1.ready: " +
//      s"${peek(c.io.In1.ready)}")
//
//
//    println(s"t: ${t} io.In2.bits: ${peek(c.io.In2.bits)}, io.In2.valid: ${peek(c.io.In2.valid)} io.In2.ready: " +
//      s"${peek(c.io.In2.ready)}")

//    println(s"t: ${t} io.In3.valid: ${peek(c.io.In3.valid)} io.In3.ready: " +
//      s"${peek(c.io.In3.ready)}")


    println(s"t: ${t} io.Out1.bits: ${peek(c.io.Out1.bits)}, io.In3.valid: ${peek(c.io.Out1.valid)} io.Out1.ready: " +
      s"${peek(c.io.Out1.ready)}")
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

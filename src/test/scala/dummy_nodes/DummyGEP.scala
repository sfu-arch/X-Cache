package dummy_nodes

/**
  * Created by nvedula on 16/5/17.
  */


import chisel3.core.RegInit
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class DummyGEPTests(c: DummyGEP) extends PeekPokeTester(c) {
  for (t <- 0 until 12) {

    if(t>2) {
      poke(c.io.Out1.ready, true)
      poke(c.io.In1.valid, true)
      poke(c.io.In1.bits, 12)
    }

    println(s"t: ${t} io.Out1.valid ${peek(c.io.Out1.valid)} io.Out1.bits: ${peek(c.io.Out1.bits)}")
  }

  step(1)

}

class DummyGEPTester extends ChiselFlatSpec {
  behavior of "DummyGEP"
  backends foreach {backend =>
    it should s"correctly find decoupled behaviour -  $backend" in {
      Driver(() => new DummyGEP(32), backend)((c) => new DummyGEPTests(c)) should be (true)
    }
  }
}

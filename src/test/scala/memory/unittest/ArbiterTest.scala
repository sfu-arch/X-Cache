package memory.unittest

/**
  * Created by vnaveen0 on 3/6/17.
  */
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._

class ArbiterTestTests(c: ArbiterTest)(implicit p: config.Parameters) extends PeekPokeTester(c) {

  var readidx= 3

  for (t <- 0 until 12) {

    if(t==4) {
      poke(c.io.ReadIn(readidx).valid, true)
      poke(c.io.ReadIn(0).valid, false)
      poke(c.io.ReadIn(1).valid, false)
      poke(c.io.ReadIn(2).valid, false)
      poke(c.io.ReadIn(readidx).bits.address, 34)
      poke(c.io.ReadIn(readidx).bits.node, 4)
      // poke(c.io.ReadIn(readidx).bits.mask, 1)
//      poke(c.io.ready, true)
    }
    else {
      poke(c.io.ReadIn(readidx).valid, false)
      poke(c.io.ReadIn(0).valid, false)
      poke(c.io.ReadIn(1).valid, false)
      poke(c.io.ReadIn(2).valid, false)

    }



    printf(s"t: ${t} ---------------------------- \n")
    printf(s"t: ${t}  io.ReadOut: ${peek(c.io.ReadOut(readidx))} chosen: ${readidx} \n")
    printf(s"t: ${t}  Out.chosen: ${peek(c.io.chosen)} " +
      s"Out.ready: ${peek(c.io.ready)} Out.Valid: ${peek(c.io.valid)}  \n")

    if( c.io.ReadIn(readidx).valid == true) {

      printf(s"t: ${t}  io.ReadIn: ${peek(c.io.ReadIn(readidx))} chosen: ${readidx} \n")
    }
    step(1)

  }

}


class ArbiterTestTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new ArbiterTest(NReads = 4, NWrites = 1)(p)) {
      c => new ArbiterTestTests(c)
    } should be(true)
  }
}

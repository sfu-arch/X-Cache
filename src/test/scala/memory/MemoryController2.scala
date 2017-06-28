package memory

// /**
//   * Created by vnaveen0 on 2/6/17.
//   */

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import arbiters._

class TypeStackTests(c: TypeStackFile)(implicit p: config.Parameters) extends PeekPokeTester(c) {

	// var readidx = 0
	poke(c.io.WriteIn(0).bits.address, 9)
	poke(c.io.WriteIn(0).bits.data, 0xdeadbee1L)
	poke(c.io.WriteIn(0).bits.RouteID, 0)
	poke(c.io.WriteIn(0).bits.Typ,4)
	poke(c.io.WriteIn(0).bits.mask,3)
	poke(c.io.WriteIn(0).valid,1)
	for (t <- 0 until 20) {
		poke(c.io.WriteIn(0).bits.data, 0xdeadbee1L+t)
		step (1)

	}
}


class TypeStackFileTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new TypeStackFile(Size=32,NReads=1,NWrites=1)) {
      c => new TypeStackTests(c)
    } should be(true)
  }
}

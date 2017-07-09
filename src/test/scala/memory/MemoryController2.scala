package memory

// /**
//   * Created by vnaveen0 on 2/6/17.
//   */

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import arbiters._
import memory._

class TypeStackTests(c: TypeStackFile)(implicit p: config.Parameters) extends PeekPokeTester(c) {

	// var readidx = 0
	poke(c.io.WriteIn(0).bits.address, 12)
	poke(c.io.WriteIn(0).bits.RouteID, 0)
	poke(c.io.WriteIn(0).bits.Typ,4)
	poke(c.io.WriteIn(0).bits.mask,15)
	poke(c.io.WriteIn(0).valid,1)

	poke(c.io.ReadIn(0).bits.address, 12)
	poke(c.io.ReadIn(0).valid,1)
	poke(c.io.ReadIn(0).bits.RouteID, 0)

	for (t <- 0 until 20) {
		poke(c.io.WriteIn(0).bits.data, 0xdeadbeefL+t)
		poke(c.io.WriteIn(0).bits.mask,15)
		step (1)
		printf(s"Read out : ${peek(c.io.ReadOut(0))}")
	}
}


class TypeStackFileTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
  	chisel3.iotesters.Driver(() => new TypeStackFile(ID=10,Size=32,NReads=1,NWrites=1)(WControl=new WriteTypMemoryController(NumOps=1,BaseSize=2,NumEntries=2))(RControl=new ReadTypMemoryController(NumOps=1,BaseSize=2,NumEntries=2))) {
      c => new TypeStackTests(c)
    } should be(true)
  }
}

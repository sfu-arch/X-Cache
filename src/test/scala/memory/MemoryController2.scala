package dandelion.memory

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.arbiters._
import dandelion.memory._
import  utility._
import Constants._

class TypeStackTests(c: TypeStackFile)(implicit p: Parameters) extends PeekPokeTester(c) {

	// var readidx = 0
	poke(c.io.WriteIn(0).bits.address, 14)
	poke(c.io.WriteIn(0).bits.RouteID, 0)
	poke(c.io.WriteIn(0).bits.Typ, MT_WU)
	poke(c.io.WriteIn(0).bits.mask,15)
	poke(c.io.WriteIn(0).valid,1)

	poke(c.io.ReadIn(0).bits.address, 14)
	poke(c.io.ReadIn(0).valid,1)
	poke(c.io.ReadIn(0).bits.Typ, MT_WU)
	poke(c.io.ReadIn(0).bits.RouteID, 0)

	for (t <- 0 until 20) {
		poke(c.io.WriteIn(0).bits.data, 0xdeadbeefL+t)
		poke(c.io.WriteIn(0).bits.mask,15)
		step (1)
		printf(s"\n Read out : ${peek(c.io.ReadOut(0))}")
	}
}


class TypeStackFileTester extends  FlatSpec with Matchers {
  implicit val p = new WithAccelConfig ++ new WithTestConfig
  it should "Memory Controller tester" in {
  	chisel3.iotesters.Driver(() => new TypeStackFile(ID=10,Size=32,NReads=1,NWrites=1)(WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=1))(RControl=new ReadMemoryController(NumOps=1,BaseSize=2,NumEntries=1))) {
      c => new TypeStackTests(c)
    } should be(true)
  }
}

package memory

/**
	* Created by vnaveen0 on 10/7/17.
	*/



// /**
//   * Created by vnaveen0 on 2/6/17.
//   */

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import arbiters._
import memory._

class UnifiedControllerTests (c: UnifiedController)(implicit p: config.Parameters) extends PeekPokeTester(c) {

	//	// var readidx = 0
	poke(c.io.WriteIn(0).bits.address, 12)
	poke(c.io.WriteIn(0).bits.data, 45)
	poke(c.io.WriteIn(0).bits.node, 16)
	poke(c.io.WriteIn(0).bits.RouteID, 0)
	poke(c.io.WriteIn(0).bits.Typ,4)
	poke(c.io.WriteIn(0).bits.mask,15)
	//	poke(c.io.WriteIn(0).valid,1)

	poke(c.io.ReadIn(0).bits.address, 34)
	poke(c.io.ReadIn(0).bits.node, 17)
	poke(c.io.ReadIn(0).bits.Typ, 4)
	//		poke(c.io.ReadIn(0).valid,1)
	poke(c.io.ReadIn(0).bits.RouteID, 0)



	var time =  -10
	for (t <- 0 until 10) {
		println(s"t = ${t} ------------------------- ")

		if(t > 1 ) {
			poke(c.io.CacheReq.ready,1)
		}

		if(t > 3 && t < 8) {
			if (peek(c.io.WriteIn(0).ready) == 1) {
				println(s" WriteIn(0) is Ready ")
				poke(c.io.WriteIn(0).valid, 1)
			}
			else {
				poke(c.io.WriteIn(0).valid, 0)
			}
		}
		else {
			poke(c.io.WriteIn(0).valid, 0)
		}


		if(t== 4) {
			if (peek(c.io.ReadIn(0).ready) == 1) {
				println(s" ReadIn(0) is Ready ")
				poke(c.io.ReadIn(0).valid, 1)
			}
			else {
				poke(c.io.ReadIn(0).valid, 0)
			}
		}
		else {
			poke(c.io.ReadIn(0).valid, 0)
		}

		if(peek(c.io.CacheReq.valid) == 1) {

			println(s" IO CacheReq isWrite  ${peek(c.io.CacheReq.bits.iswrite)}")
			println(s" IO CacheReq data     ${peek(c.io.CacheReq.bits.data)}")
			println(s" IO CacheReq tag      ${peek(c.io.CacheReq.bits.tag)}")


			time = t+1

			println(s" Sending Response from Cache ")
			poke(c.io.CacheResp.bits.data, 45)
			poke(c.io.CacheResp.bits.isSt, peek(c.io.CacheReq.bits.iswrite))
			poke(c.io.CacheResp.bits.tag, peek(c.io.CacheReq.bits.tag))
			poke(c.io.CacheResp.valid, 1)
		}
		else {
			poke(c.io.CacheResp.valid, 0)
		}


		println(s" IO CacheReq Valid  ${peek(c.io.CacheReq.valid)}")
		if(peek(c.io.ReadOut(0).valid) == 1) {

			println(s"^^^^^^^^^^^^^^")
			println(s"ReadOut(0) Resp :  -------------")
			println(s" IO ReadResp Valid  ${peek(c.io.ReadOut(0))}")
		}


		if(peek(c.io.WriteOut(0).valid) == 1) {

			println(s"^^^^^^^^^^^^^^")
			println(s"WriteOut(0) Resp :  -------------")
			println(s" IO WriteOut Valid  ${peek(c.io.WriteOut(0))}")
		}



		step(1)
	}



}


class UnifiedControllerTester extends  FlatSpec with Matchers {
	implicit val p = config.Parameters.root((new MiniConfig).toInstance)
	it should "Memory Controller tester" in {
		chisel3.iotesters.Driver(() => new UnifiedController(ID=10,Size=32,NReads=1,NWrites=1)(
			WControl=new WriteTypMemoryController(NumOps=1,BaseSize=2,NumEntries=2))(
			RControl=new ReadTypMemoryController(NumOps=1,BaseSize=2,NumEntries=2))(
			RWArbiter = new ReadWriteArbiter())(p)) {
			c => new UnifiedControllerTests(c)
		} should be(true)
	}
}

package dandelion.memory

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._

class ReadMemoryControllerTests(c: ReadMemoryController)
	(implicit p: Parameters)
	extends PeekPokeTester(c) {

// 	var readidx = 0
	poke(c.io.ReadIn(0).bits.address, 9)
	poke(c.io.ReadIn(0).bits.RouteID, 0)
	poke(c.io.ReadIn(0).bits.Typ,3)
	poke(c.io.ReadIn(0).valid,1)
	poke(c.io.MemReq.ready,true)
	poke(c.io.MemResp.valid,false)

	var req  = false
	var tag  = peek(c.io.MemReq.bits.tag)
	var reqT = 0
    // in_arb.io.in(0).bits.RouteID := 0.U
    // in_arb.io.in(0).bits.Typ := MT_W
    // in_arb.io.in(0).valid := true.B
	poke(c.io.MemReq.ready,1)
	poke(c.io.MemResp.valid,false)
	for (t <- 0 until 12) {
		if((peek(c.io.MemReq.valid) == 1) && (peek(c.io.MemReq.ready) == 1)) {
			printf(s"t: ${t} ---------------------------- \n")
			req  = true
			tag  = peek(c.io.MemReq.bits.tag)
			reqT = t
		}
		if ((req == true) && (t > reqT))
		{
			poke(c.io.MemResp.valid,true)
			poke(c.io.MemResp.bits.data, 0xdeadbeefL)
			printf("Tag:%d",tag)
			poke(c.io.MemResp.bits.tag,peek(c.io.MemReq.bits.tag))
			req = false
		}
		if (req == true) {
			poke(c.io.MemReq.ready,false)
		} else {
			poke(c.io.MemReq.ready,true)
		}

//     if(t<11) {
//       poke(c.io.ReadIn(1).valid, false)
//       poke(c.io.ReadIn(2).valid, false)

//       //-------------------------------------------

//       poke(c.io.ReadIn(readidx).valid, true)
//       poke(c.io.ReadIn(readidx).bits.address, 34)
//       poke(c.io.ReadIn(readidx).bits.node, 3)
//       // poke(c.io.ReadIn(readidx).bits.mask, 3)

//       //-------------------------------------------
//       poke(c.io.ReadIn(0).valid, true)
//       poke(c.io.ReadIn(0).bits.address, 43)
//       poke(c.io.ReadIn(0).bits.node, 0)
//       // poke(c.io.ReadIn(0).bits.mask, 0)
//     }
//     else {
//       poke(c.io.ReadIn(readidx).valid, false)
//       poke(c.io.ReadIn(0).valid, false)
//       poke(c.io.ReadIn(1).valid, false)
//       poke(c.io.ReadIn(2).valid, false)

//     }

//     //    //TODO Create separate Test case for Demux
//     //    // To test Demux
//     //    if(t==1) {
//     //      poke(c.io.en,true)
//     //      poke(c.io.sel,readidx)
//     //      poke(c.io.input.data, 13)
//     //      poke(c.io.input.valid, 1)
//     //    }
//     //    else {
//     //      poke(c.io.en, false)
//     //    }

//     printf(s"t: ${t} ---------------------------- \n")
//
//     printf(s"t: ${t}  io.ReadOut: ${peek(c.io.ReadOut(0))} chosen: 0 \n")
// //    printf(s"t: ${t}  io.ReadIn: ${peek(c.io.ReadIn(readidx))} chosen: ${readidx} \n")
    step(1)

  }

}


class ReadMemoryControllerTester extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new ReadMemoryController(NumOps=1,BaseSize=2,NumEntries=2)(p)) {
      c => new ReadMemoryControllerTests(c)
    } should be(true)
  }
}

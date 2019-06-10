// // See LICENSE for license details.

// package dataflow

// import chisel3._
// import chisel3.util._

// import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
// import org.scalatest.{Matchers, FlatSpec}

// import node._
// import dataflow._
// import muxes._
// import dandelion.config._
// import util._
// import interfaces._
// import control._




// // Tester.
// class simpleLoadTester(df: LoadNode)(implicit p: Parameters) extends PeekPokeTester(df)  {

//   poke(df.io.GepAddr.valid, false.B)
//   poke(df.io.memResp.valid, false.B)
//   poke(df.io.memReq.ready, true.B)
//   poke(df.io.Out(0).ready, true.B)
//   println(s" ")
//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")

//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")

//   //Hanshaking Inputs:
//   poke(df.io.PredOp(0).valid, true.B)
//   poke(df.io.GepAddr.valid, true.B)
//   poke(df.io.GepAddr.bits.data, 10.U)
//   poke(df.io.GepAddr.bits.predicate, true.B)


//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")

// //  poke(df.io.memResp.data, 20.U)
// //  poke(df.io.memResp.valid, true.B)

//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")

//   //SEND MEMORY RESPONSE
//   poke(df.io.memResp.data, 10.U)
//   poke(df.io.memResp.valid, true.B)

//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")

//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")

//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")

//   step(1)
//   println(s" ")
//   println(s"GEP Addr: ${peek(df.io.GepAddr)}")
//   println(s"MEM REQ : ${peek(df.io.memReq)}")
//   println(s"MEM RESP: ${peek(df.io.memResp)}")
//   println(s"OUTPUT: ${peek(df.io.Out(0))}")
//   step(1)
//   println(s" ")

//   //poke(df.io.resultReady, false.B)
//   //println(s" ")
//   //println(s"Node output: ${peek(df.io.result)}")
//   //println(s"Node valid : ${peek(df.io.resultValid)}")
//   //println(s" ")
//   //step(1)
//   //poke(df.io.resultReady, true.B)
//   //println(s"Node output: ${peek(df.io.result)}")
//   //println(s"Node valid : ${peek(df.io.resultValid)}")
//   //println(s" ")
//   //step(1)
//   //println(s"Node output: ${peek(df.io.result)}")
//   //println(s"Node valid : ${peek(df.io.resultValid)}")
//   //println(s" ")
//   //step(1)
//   //println(s"Node output: ${peek(df.io.result)}")
//   //println(s"Node valid : ${peek(df.io.resultValid)}")
//   //println(s" ")

//  }




// class simpleLoadTests extends  FlatSpec with Matchers {
//    implicit val p = Parameters.root((new MiniConfig).toInstance)
//   it should "Simple load tester" in {
//      chisel3.iotesters.Driver(() => new LoadNode(1,1,1,ID=0,RouteID=0)(p)) { c =>
//        new simpleLoadTester(c)
//      } should be(true)
//    }
//  }




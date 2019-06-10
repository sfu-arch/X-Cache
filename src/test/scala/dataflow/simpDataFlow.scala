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
// import dandelion.interfaces._
// import control._




// // Tester.
// class simpleLDTester(df: MemDataFlow)(implicit p: Parameters) extends PeekPokeTester(df)  {

//   // poke(df.io.resultReady, false.B)
//   // println(s" ")
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   for (i <- 0 until 50)
//   {
//   step(1)
//   }
//   // poke(df.io.resultReady, true.B)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")

//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")

//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")

//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")

//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")

//   // poke(df.io.resultReady, false.B)
//   // println(s" ")
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")

//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // poke(df.io.resultReady, true.B)
//   //
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//   // println(s"Node output: ${peek(df.io.result)}")
//   // println(s"Node valid : ${peek(df.io.resultValid)}")
//   // println(s" ")
//   // step(1)
//  }




// class simpleLDTests extends  FlatSpec with Matchers {
//    implicit val p = Parameters.root((new MiniConfig).toInstance)
//   it should "Dataflow tester" in {
//      chisel3.iotesters.Driver(() => new MemDataFlow()(p)) { c =>
//        new simpleLDTester(c)
//      } should be(true)
//    }
//  }

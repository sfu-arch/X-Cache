// // See LICENSE for license details.

// package node

// import chisel3._
// import chisel3.util._

// import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
// import org.scalatest.{Matchers, FlatSpec}

// import node._
// import dataflow._
// import muxes._
// import config._
// import util._
// import interfaces._





// // Tester.
// class FusedcomputeTester(df: FusedComputeNode)
//                   (implicit p: config.Parameters) extends PeekPokeTester(df)  {

//   // poke(df.io.In(0).bits.data, 9.U)
//   // poke(df.io.In(0).valid, false.B)
//   // poke(df.io.In(0).bits.predicate, false.B)

//   // poke(df.io.In(1).bits.data, 5.U)
//   // poke(df.io.In(1).valid, false.B)
//   // poke(df.io.In(1).bits.predicate, false.B)

//   // poke(df.io.enable.bits , false.B)
//   // poke(df.io.enable.valid, false.B)
//   // poke(df.io.Out(0).ready, false.B)
//   // println(s"Output: ${peek(df.io.Out(0))}\n")


//   // step(1)

//   // poke(df.io.enable.bits , true.B)
//   // poke(df.io.enable.valid, true.B)
//   // poke(df.io.Out(0).ready, true.B)


//   // poke(df.io.In(0).valid, true.B)
//   // poke(df.io.In(1).valid, true.B)
//   // poke(df.io.In(0).bits.predicate, true.B)
//   // poke(df.io.In(1).bits.predicate, true.B)

//   // println(s"Output: ${peek(df.io.Out(0))}\n")

//   // println(s"t: -1\n -------------------------------------")
//   // step(1)


//   // for( i <- 0 until 10){
//   //   println(s"Output: ${peek(df.io.Out(0))}\n")

//   //   println(s"t: ${i}\n -------------------------------------")
//   //   step(1)
//   }
// //  poke(df.io.CmpIn.valid, false.B)
// //  poke(df.io.CmpIn.bits, false.B)
// //
// //  println(s"Node input : ${peek(df.io.CmpIn)}")
// //  println(s"Node output: ${peek(df.io.OutIO)}")
// //
// //  step(1)
// //
// //  poke(df.io.CmpIn.bits, true.B)
// //  println(s"Node input : ${peek(df.io.CmpIn)}")
// //  println(s"Node output: ${peek(df.io.OutIO)}")
// //
// //  poke(df.io.CmpIn.valid, true.B)
// //  step(1)
// //  println(s"Node input : ${peek(df.io.CmpIn)}")
// //  println(s"Node output: ${peek(df.io.OutIO)}")
// //
// //  step(1)
// //  println(s"Node input : ${peek(df.io.CmpIn)}")
// //  println(s"Node output: ${peek(df.io.OutIO)}")
// //  step(1)
// //
// //  poke(df.io.CmpIn.bits, false.B)
// //  println(s"Node input : ${peek(df.io.CmpIn)}")
// //  println(s"Node output: ${peek(df.io.OutIO)}")
// //  step(1)
// //  println(s"Node input : ${peek(df.io.CmpIn)}")
// //  println(s"Node output: ${peek(df.io.OutIO)}")
//  // }




// class FusedCompTests extends  FlatSpec with Matchers {
//    implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//   it should "Dataflow tester" in {
//      chisel3.iotesters.Driver(() => new FusedComputeNode(NumIns = 2, NumOuts = 1, ID = 0, opCode = Array("Add"))(sign = false)) {
//        c => new FusedcomputeTester(c)
//      } should be(true)
//    }
//  }




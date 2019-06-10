package switches.example

/**
  * Created by vnaveen0 on 11/9/17.
  */


import dandelion.interfaces._
import chisel3._
import chisel3.util._
import accel._
import dandelion.config._
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}

/*
// Tester.
class ex1Tests(c: Dyser1X1)
              (implicit p: Parameters) extends PeekPokeTester(c)  {

  //  poke(c.io.Data0.bits.data, 2.U)
// //   //  poke(c.io.Data0.bits.valid, true.B)
  //  poke(c.io.Data0.bits.predicate, true.B)
  //  poke(c.io.Data0.valid, true.B)
  //
  //
  //  poke(c.io.Data1.bits.data, 2.U)
// //   //  poke(c.io.Data1.bits.valid, true.B)
  //  poke(c.io.Data1.bits.predicate, true.B)
  //  poke(c.io.Data1.valid, true.B)
  //
  //
  //
  //
  //
  //  for( i <- 0 until 10){
  //
  //    if(t >1) {
  //      poke(c.io.start, true.B)
  //    }
  //    else {
  //      poke(c.io.start, false.B)
  //    }
  //
  //
  //    println(s"Output: ${peek(c.io.result)}\n")
  //    println(s"t: ${i}\n -------------------------------------")
  //    step(1)
  //  }

  //------------
  poke(c.io.Data0.valid, false.B)
  poke(c.io.Data0.bits.data, 2.U)
  poke(c.io.Data0.bits.predicate, false.B)
// //   poke(c.io.Data0.bits.valid, false.B)

  poke(c.io.Data1.valid, false.B)
  poke(c.io.Data1.bits.predicate, false.B)
// //   poke(c.io.Data1.bits.valid, false.B)
  poke(c.io.Data1.bits.data, 1.U)

  poke(c.io.start, false.B)
  poke(c.io.result.ready, false.B)
  println(s"Output: ${peek(c.io.result)}\n")


  step(1)

  poke(c.io.start, true.B)


  poke(c.io.Data0.valid, true.B)
  poke(c.io.Data1.valid, true.B)
  poke(c.io.Data0.bits.predicate, true.B)
  poke(c.io.Data1.bits.predicate, true.B)

// //   poke(c.io.Data0.bits.valid, true.B)
// //   poke(c.io.Data1.bits.valid, true.B)

  poke(c.io.Data0.bits.data, 2.U)
  poke(c.io.Data1.bits.data, 1.U)


  println(s"Output: ${peek(c.io.result)}\n")

  println(s"t: -1\n -------------------------------------")
  step(1)


  for( i <- 0 until 10){
    println(s"Output: ${peek(c.io.result)}\n")

    if(peek(c.io.result.valid) == 1){
      poke(c.io.result.ready, false.B)
      println(s"Result is valid \n")
    }
    else {
      poke(c.io.result.ready, true.B)
    }

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }

}



class ex1Tester extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Dyser1X1 tester" in {
    chisel3.iotesters.Driver(() => new Dyser1X1()(p)) { c => new ex1Tests(c)
    } should be(true)
  }
}

*/



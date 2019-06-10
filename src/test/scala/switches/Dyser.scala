package switches

/**
  * Created by vnaveen0 on 9/9/17.
  */


import interfaces._
import chisel3._
import chisel3.util._
import accel._
import dandelion.config._
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}

/*
// Tester.
class DyserTests(c: Dyser)
                          (implicit p: Parameters) extends PeekPokeTester(c)  {

  for (t <- 0 until 8) {
    if(t==2) {
      poke(c.io.inN.valid, 1)
      poke(c.io.inN.bits.data, 99)
// //       poke(c.io.inN.bits.valid, 1)

      poke(c.io.inE.valid, 1)
      poke(c.io.inE.bits.data, 11)
// //       poke(c.io.inE.bits.valid, 1)

      poke(c.io.inW.valid, 1)
      poke(c.io.inW.bits.data, 22)
// //       poke(c.io.inW.bits.valid, 1)

      poke(c.io.inS.valid, 1)
      poke(c.io.inS.bits.data, 33)
// //       poke(c.io.inS.bits.valid, 1)

      poke(c.io.inNE.valid, 1)
      poke(c.io.inNE.bits.data, 44)
// //       poke(c.io.inNE.bits.valid, 1)

    }
    else if(t==6) {

      poke(c.io.inN.valid, 1)
      poke(c.io.inN.bits.data, 999)
// //       poke(c.io.inN.bits.valid, 1)

      poke(c.io.inE.valid, 1)
      poke(c.io.inE.bits.data, 111)
// //       poke(c.io.inE.bits.valid, 1)

      poke(c.io.inW.valid, 1)
      poke(c.io.inW.bits.data, 222)
// //       poke(c.io.inW.bits.valid, 1)

      poke(c.io.inS.valid, 1)
      poke(c.io.inS.bits.data, 333)
// //       poke(c.io.inS.bits.valid, 1)

      poke(c.io.inNE.valid, 1)
      poke(c.io.inNE.bits.data, 444)
// //       poke(c.io.inNE.bits.valid, 1)

    }
    else {
      poke(c.io.inN.valid, 0)
// //       poke(c.io.inN.bits.valid, 0)

      poke(c.io.inE.valid, 0)
// //       poke(c.io.inE.bits.valid, 0)

      poke(c.io.inW.valid, 0)
// //       poke(c.io.inW.bits.valid, 0)

      poke(c.io.inS.valid, 0)
// //       poke(c.io.inS.bits.valid, 0)

      poke(c.io.inNE.valid, 0)
// //       poke(c.io.inNE.bits.valid, 0)

    }


    if(t ==4) {
      poke(c.io.outS.ready, 1)
    }
    else {

      poke(c.io.outS.ready, 0)

    }


    step(1)
    println(s"t: ${t} ---------------------------- \n")
    println(s"InN  : ${peek(c.io.inN)}")
    println(s"InE  : ${peek(c.io.inE)}")
    println(s"InW  : ${peek(c.io.inW)}")
    println(s"InS  : ${peek(c.io.inS)}")
    println(s"InNE  : ${peek(c.io.inNE)}")

    println(s"OutN  : ${peek(c.io.outN)}")
    println(s"OutE  : ${peek(c.io.outE)}")
    println(s"OutW  : ${peek(c.io.outW)}")
    println(s"OutS  : ${peek(c.io.outS)}")
    println(s"OutNE  : ${peek(c.io.outNE)}")
    println(s"OutNW  : ${peek(c.io.outNW)}")
    println(s"OutSE  : ${peek(c.io.outSE)}")
    println(s"OutSW  : ${peek(c.io.outSW)}")
  }

}



class DyserTester extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Mux tester" in {
    chisel3.iotesters.Driver(() => new Dyser(
      SelN = 1,
      SelE = 1,
      SelW = 1,
      SelS = 1,
      SelNE = 1,
      SelNW = 1,
      SelSE = 1,
      SelSW = 1,

      EnN = false.B,
      EnE = false.B,
      EnW = false.B,
      EnS = true.B,
      EnNE = false.B,
      EnNW = false.B,
      EnSE = false.B,
      EnSW = false.B
    )(p)) { c => new DyserTests(c)
    } should be(true)
  }
}
*/



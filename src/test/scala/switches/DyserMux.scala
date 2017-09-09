package switches

/**
  * Created by vnaveen0 on 9/9/17.
  */

import accel.CacheReq
import chisel3._
import chisel3.util._
import accel._
import config._
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}


// Tester.
class DyserMuxTests(c: DyserMux)
                          (implicit p: config.Parameters) extends PeekPokeTester(c)  {

  for (t <- 0 until 8) {
    if(t>2) {
      poke(c.io.in(0).valid, 1)
      poke(c.io.in(0).bits.data, 99)
      poke(c.io.in(0).bits.valid, 1)

      poke(c.io.in(1).valid, 1)
      poke(c.io.in(1).bits.data, 11)
      poke(c.io.in(1).bits.valid, 1)

      poke(c.io.in(2).valid, 1)
      poke(c.io.in(2).bits.data, 22)
      poke(c.io.in(2).bits.valid, 1)

      poke(c.io.in(3).valid, 1)
      poke(c.io.in(3).bits.data, 33)
      poke(c.io.in(3).bits.valid, 1)


    }
    else {
      poke(c.io.in(0).valid, 0)
      poke(c.io.in(0).bits.valid, 0)

      poke(c.io.in(1).valid, 0)
      poke(c.io.in(1).bits.valid, 0)

      poke(c.io.in(2).valid, 0)
      poke(c.io.in(2).bits.valid, 0)

      poke(c.io.in(3).valid, 0)
      poke(c.io.in(3).bits.valid, 0)

    }

//    if (peek(c.io.out.valid) == 1) {
//      println(s"Out is valid and ack is sent \n ")
//    }

    step(1)
    println(s"t: ${t} ---------------------------- \n")
    println(s"In  : ${peek(c.io.in)}")
    println(s"Out  : ${peek(c.io.out)}")
  }

}



class DyserMuxTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Mux tester" in {
    chisel3.iotesters.Driver(() => new DyserMux(NInputs = 4, Sel = 1, En = true.B)(p)) {
      c => new DyserMuxTests(c)
    } should be(true)
  }
}




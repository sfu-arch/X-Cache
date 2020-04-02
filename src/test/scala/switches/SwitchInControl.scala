package switches

/**
  * Created by vnaveen0 on 9/9/17.
  */

import dandelion.interfaces._
import chisel3._
import chisel3.util._
import dandelion.accel. _
import chipsalliance.rocketchip.config._
import dandelion.config._
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}

/*
// Tester.
class SwitchInControlTests(c: SwitchInControl)
                          (implicit p: Parameters) extends PeekPokeTester(c)  {

  for (t <- 0 until 8) {
    if(t==2) {
      poke(c.io.in.valid, 1)
      poke(c.io.in.bits.data, 23)
// //       poke(c.io.in.bits.valid, 1)
    }
    else {
      poke(c.io.in.valid, 0)
// //       poke(c.io.in.bits.valid, 0)
    }

    if (peek(c.io.out.valid) == 1) {
//      poke(c.io.ack.valid, 1)
//      println(s"Out is valid and ack is sent \n ")
    }
    else {
      poke(c.io.ack.valid, 0)
    }

    step(1)
    println(s"t: ${t} ---------------------------- \n")
    println(s"In  : ${peek(c.io.in)}")
    println(s"Out  : ${peek(c.io.out)}")
    println(s"Ack  : ${peek(c.io.ack)}")
  }

}



class SwitchInControlTester extends  FlatSpec with Matchers {
  implicit val p = new WithAccelConfig ++ new WithTestConfig
  it should "Mux tester" in {
    chisel3.iotesters.Driver(() => new SwitchInControl()(p)) {
      c => new SwitchInControlTests(c)
    } should be(true)
  }
}

*/


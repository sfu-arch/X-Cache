package muxes

/**
  * Created by vnaveen0 on 1/9/17.
  */
import dandelion.interfaces._
import chisel3._
import chisel3.util._
import dandelion.accel._
import dandelion.config._
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}


// Tester.
class TestMuxTests(df: TestMux)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {


  poke(df.io.EN, true.B)
  poke(df.io.SEL, 3.U)

  poke(df.io.ReadIn(0).data, 99.U)
  poke(df.io.ReadIn(1).data, 1.U)
  poke(df.io.ReadIn(2).data, 2.U)
  poke(df.io.ReadIn(3).data, 3.U)


  poke(df.io.ReadIn(0).valid, true.B)
  poke(df.io.ReadIn(1).valid, true.B)
  poke(df.io.ReadIn(2).valid, true.B)
  poke(df.io.ReadIn(3).valid, true.B)



  println(s"EN  : ${peek(df.io.EN)}")
  println(s"SEL  : ${peek(df.io.SEL)}\n")
  println(s"ReadIn(0)  : ${peek(df.io.ReadIn(0))}")
  println(s"ReadIn(1)  : ${peek(df.io.ReadIn(1))}")
  println(s"ReadIn(2)  : ${peek(df.io.ReadIn(2))}")
  println(s"ReadIn(3)  : ${peek(df.io.ReadIn(3))}")


  println(s"\nReadOut  : ${peek(df.io.ReadOut)}\n")



}



class TestMuxTester extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Mux tester" in {
    chisel3.iotesters.Driver(() => new TestMux(4)(p)) {
      c => new TestMuxTests(c)
    } should be(true)
  }
}




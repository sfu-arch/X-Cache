package memory

/**
  * Created by vnaveen0 on 8/7/17.
  */
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._

class ReadTableEntryTests(c: ReadTableEntry)
	(implicit p: config.Parameters)
	extends PeekPokeTester(c) {

  // -- IO ---
  //      val NodeReq = Flipped(Decoupled(Input(new ReadReq)))
  //    val NodeResp = Decoupled(new ReadResp)
  //
  //    //Memory interface
  //    val MemReq = Decoupled(new CacheReq)
  //    val MemResp = Input(new CacheResp)
  //
  //    // val Output
  //    val output = Decoupled(new ReadResp)
  //
  //    val free = Output(Bool())
  //    val done = Output(Bool())
  // -- --------------------------------------------------


  for (t <- 0 until 10) {

    poke(c.io.NodeReq.valid, 0)



    println(s"t : ${t} ----------------------------")
    println(s"NodeReq.R: ${peek(c.io.NodeReq.ready)}")
    println(s"free : ${peek(c.io.free)}")
    println(s"done : ${peek(c.io.done)}")


    //  println(s"Output 0: ${peek(df.io.Out(0))}")
    step(1)
  }

}




class ReadTableEntryTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new ReadTableEntry(0)(p)) {
      c => new ReadTableEntryTests(c)
    } should be(true)
  }
}

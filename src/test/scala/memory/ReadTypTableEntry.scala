package memory

/**
  * Created by vnaveen0 on 8/7/17.
  */

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._

class ReadTypTableEntryTests(c: ReadTypTableEntry)
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

    //    var tag : chisel3.core.UInt
    //    var tag : Int = 12




    //Send a Valid Node Request in Clock 3
    if(t==3) {
      poke(c.io.NodeReq.valid,1)
      poke(c.io.NodeReq.bits.address,123)
      poke(c.io.NodeReq.bits.Typ,4)
      poke(c.io.NodeReq.bits.node,2)
    }
    else {
      poke(c.io.NodeReq.valid, 0)
    }

    println(s"t : ${t} ----------------------------")
    println(s"NodeReq.ready: ${peek(c.io.NodeReq.ready)}")
    println(s"NodeReq.valid: ${peek(c.io.NodeReq.valid)}")
    println(s"free : ${peek(c.io.free)}")
    println(s"done : ${peek(c.io.done)}")

    if(t>2) {

      //Cache Req
      //      val addr    = UInt(xlen.W)
      //  val data    = UInt(xlen.W)
      //  val mask    = UInt((xlen/8).W)
      //  val tag     = UInt((List(1,rdmshrlen,wrmshrlen).max).W)
      //  val iswrite = Bool()
      println(s"MemReq.Valid: ${peek(c.io.MemReq.valid)}")
      println(s"MemReq.addr: ${peek(c.io.MemReq.bits.addr)}")
      println(s"MemReq.data: ${peek(c.io.MemReq.bits.data)}")
      println(s"MemReq.mask: ${peek(c.io.MemReq.bits.mask)}")
      println(s"MemReq.tag: ${peek(c.io.MemReq.bits.tag)}")
      println(s"MemReq.isWrite: ${peek(c.io.MemReq.bits.iswrite)}")



      println(s"MemResp.data: ${peek(c.io.MemResp.data)}")
      println(s"MemResp.tag: ${peek(c.io.MemResp.tag)}")
    }


    //    var tag  = c.io.MemReq.bits.tag
    // memory gets Ready at Clock 5 -> For First Address
    if(t==4) {
      poke(c.io.MemReq.ready, 1)
      println(s"Sending Valid Request to Memory")
      //      tag = c.io.MemReq.bits.tag
    }
    // Memory Responds with the data
    if(t==6) {

      println(s"Sending Valid Response from Memory")
      poke(c.io.MemResp.data, 34)
      poke(c.io.MemResp.tag, 0)
    }

    // memory again gets Ready at Clock 7 -> For First Address
    if(t > 6) {
      poke(c.io.MemReq.ready, 1)
      poke(c.io.MemReq.ready, 1)
    }

    step(1)

  }

}




class ReadTypTableEntryTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new ReadTypTableEntry(0)(p)) {
      c => new ReadTypTableEntryTests(c)
    } should be(true)
  }
}

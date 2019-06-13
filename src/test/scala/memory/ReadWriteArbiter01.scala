package memory

/**
  * Created by vnaveen0 on 9/7/17.
  */

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chisel3._
import dandelion.config._
import dandelion.arbiters._
import memory._


class ReadWriteArbiterTests01(c: => ReadWriteArbiter) (implicit p: Parameters)
  extends PeekPokeTester(c) {
  // -- IO ---
  //    val ReadMemReq = Decoupled(new MemReq)
  //    val ReadMemResp = Flipped(Valid(new MemResp))
  //    val WriteMemReq = Decoupled(new MemReq)
  //    val WriteMemResp = Flipped(Valid(new MemResp))
  //    val MemReq = Decoupled(new MemReq)
  //    val MemResp = Flipped(Valid(new MemResp))



  var time =  -10
  for (t <- 0 until 10) {
    println(s"t = ${t} ------------------------- ")

    if(t > 1 ) {
      poke(c.io.MemReq.ready,1)
    }

    if(t > 3 && t < 8) {
      if (peek(c.io.WriteMemReq.ready) == 1) {
        println(s" WriteMemReq Ready ")
        poke(c.io.WriteMemReq.valid, 1)
        poke(c.io.WriteMemReq.bits.addr, 23)
        poke(c.io.WriteMemReq.bits.data, 4)
        poke(c.io.WriteMemReq.bits.iswrite, 1)
        poke(c.io.WriteMemReq.bits.tag, 1)
      }
      else {
        poke(c.io.WriteMemReq.valid, 0)
      }
    }
    else {
      poke(c.io.WriteMemReq.valid, 0)
    }


    if(t== 4) {
      if (peek(c.io.ReadMemReq.ready) == 1) {
        println(s" ReadMemReq Ready ")
        poke(c.io.ReadMemReq.valid, 1)
        poke(c.io.ReadMemReq.bits.addr, 54)
        poke(c.io.ReadMemReq.bits.data, 434342432)
        poke(c.io.ReadMemReq.bits.iswrite, 0)
        poke(c.io.ReadMemReq.bits.tag, 1)
      }
      else {
        poke(c.io.ReadMemReq.valid, 0)
      }
    }
    else {
      poke(c.io.ReadMemReq.valid, 0)
    }

    if(peek(c.io.MemReq.valid) == 1) {

      println(s" IO MemReq isWrite  ${peek(c.io.MemReq.bits.iswrite)}")
      println(s" IO MemReq data     ${peek(c.io.MemReq.bits.data)}")
      println(s" IO MemReq tag      ${peek(c.io.MemReq.bits.tag)}")


      time = t+1

      println(s" Sending Response from Cache ")
      poke(c.io.MemResp.bits.data, 45)
      poke(c.io.MemResp.bits.iswrite, peek(c.io.MemReq.bits.iswrite))
      poke(c.io.MemResp.bits.tag, peek(c.io.MemReq.bits.tag))
      poke(c.io.MemResp.valid, 1)
    }
    else {
      poke(c.io.MemResp.valid, 0)
    }


    println(s" IO MemReq Valid  ${peek(c.io.MemReq.valid)}")
    if(peek(c.io.ReadMemResp.valid) == 1) {

      println(s"^^^^^^^^^^^^^^")
      println(s"ReadMemResp :  -------------")
      println(s" IO ReadResp Valid  ${peek(c.io.ReadMemResp)}")
    }


    if(peek(c.io.WriteMemResp.valid) == 1) {

      println(s"^^^^^^^^^^^^^^")
      println(s"WriteMemResp :  -------------")
      println(s" IO WriteResp Valid  ${peek(c.io.WriteMemResp)}")
    }



    step(1)
  }


}


class ReadWriteArbiterTester01 extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new ReadWriteArbiter()(p)) {
      c => new ReadWriteArbiterTests01(c)
    } should be(true)
  }
}


//    if(peek(c.io.ReadMemResp.valid) == 1) {
//      println(" ReadMemResp  Received ")
//    }
//
//
//    if(peek(c.io.WriteMemResp.valid) == 1) {
//      println(" WriteMemResp  Received ")
//    }


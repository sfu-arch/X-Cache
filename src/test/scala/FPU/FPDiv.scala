package FPU

import chisel3._
import chisel3.util._
import FPU._
import FType._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import dataflow._
import muxes._
import config._
import util._
import interfaces._

class FPDivNodeTester(c: FPDivSqrtNode) extends PeekPokeTester(c) {
    poke(c.io.a.valid,false)
    poke(c.io.b.valid,false)
    poke(c.io.FUReq.ready,false)
    poke(c.io.FUResp.valid,false)
    poke(c.io.Out(0).ready,true)

     step(1)


    poke(c.io.a.valid, true)
    poke(c.io.a.bits.data, 12)
    poke(c.io.a.bits.predicate, true)
    poke(c.io.b.valid, true)
    poke(c.io.b.bits.data, t+1)
    poke(c.io.b.bits.predicate,true)
    poke(c.io.enable.bits.control,true)
    poke(c.io.enable.valid,true)

    step(1)
    step(1)
    step(1)
    step(1)

    poke(c.io.FUReq.ready,true)

    step(1)
    step(1)
    step(1)

    poke(c.io.FUResp.data,100)
    poke(c.io.FUResp.valid,true)
          // printf(s"t: ${t}  io.Out: ${peek(c.io.Out(0))} \n")

    step(1)
    step(1)
    step(1)
    // }


}



class FPDivNodeTests extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "FPDivSqrt Node tester" in {
    chisel3.iotesters.Driver(() => new FPDivSqrtNode(NumOuts=1,ID=1,opCode = "DIV")(t = S)) { c =>
      new FPDivNodeTester(c)
    } should be(true)
  }
}

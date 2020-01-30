package dandelion.node

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._
import utility._

class TypLoadTests(c: TypLoad) extends PeekPokeTester(c) {
  poke(c.io.GepAddr.valid, false)
  poke(c.io.enable.valid, false)
  poke(c.io.memReq.ready, false)
  poke(c.io.memResp.valid, false)
  poke(c.io.Out(0).ready, true)


  for (t <- 0 until 20) {

    step(1)

    //IF ready is set
    // send address
    if (peek(c.io.GepAddr.ready) == 1) {
      poke(c.io.GepAddr.valid, true)
      poke(c.io.GepAddr.bits.data, 12)
      poke(c.io.GepAddr.bits.predicate, true)
      poke(c.io.enable.bits.control, true)
      poke(c.io.enable.valid, true)
    }

    printf(s"t: ${t}  c.io.memReq: ${peek(c.io.memReq)} \n")
    if ((peek(c.io.memReq.valid) == 1) && (t > 4)) {
      poke(c.io.memReq.ready, true)
    }

    if (t > 8) {
      poke(c.io.memResp.valid, true)
      poke(c.io.memResp.data, 0x1eadbeef + t)
    }
  }
}


import Constants._

class TypLoadTester extends FlatSpec with Matchers {
  implicit val p = new WithAccelConfig
  it should "Load Node tester" in {
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tn", "test03",
        "-tbn", "verilator",
        "-td", "test_run_dir/test03",
        "-tts", "0001"),
      () => new TypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 1, RouteID = 0)) { c =>
      new TypLoadTests(c)
    } should be(true)
  }
}

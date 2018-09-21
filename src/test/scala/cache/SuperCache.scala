package cache

import chisel3._
import chisel3.util._
import chisel3.testers._
import junctions._
import config._
import scala.math._
import memory._
import interfaces._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}


class SuperCacheUnitTests(c: NCache) extends PeekPokeTester(c) {
  poke(c.io.cpu.MemReq(0).valid, false.B)
  poke(c.io.nasti.ar.ready, false.B)
  poke(c.io.nasti.aw.ready, false.B)
  poke(c.io.nasti.w.ready, false.B)
  poke(c.io.nasti.r.ready, false.B)
  poke(c.io.nasti.b.ready, false.B)
  step(1)
}


class SuperCacheUnitTester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "SuperCache tester" in {
    chisel3.iotesters.Driver(() => new NCache(1, 1)) { c =>
      new SuperCacheUnitTests(c)
    } should be(true)
  }
}

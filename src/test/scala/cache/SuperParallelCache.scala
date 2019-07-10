package cache

import chisel3._
import chisel3.util._
import chisel3.testers._
import dandelion.junctions._
import dandelion.config._
import scala.math._
import dandelion.memory._
import dandelion.interfaces._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}


class SuperParallelCacheUnitTests(c: NParallelCache) extends PeekPokeTester(c) {
  for (j <- 0 until 2) {
    poke(c.io.cpu.MemReq(j).valid, false.B)
  }
  for (j <- 0 until 2) {
    poke(c.io.nasti(j).ar.ready, false.B)
    poke(c.io.nasti(j).aw.ready, false.B)
    poke(c.io.nasti(j).w.ready, false.B)
    poke(c.io.nasti(j).r.ready, false.B)
    poke(c.io.nasti(j).b.ready, false.B)
  }

  poke(c.io.cpu.MemReq(0).bits.tag, 10.U)
  poke(c.io.cpu.MemReq(0).bits.taskID, 10.U)
  step(1)

  def get_addr(tag: Int, setidx: Int, block: Int): Int = {
    (tag << 12) + (setidx << 4) + block
  }

  val tags    = Array(0x1ead1)
  val setidxs = Array(0x01)
  val blocks  = Array(0x9)

  print(get_addr(tags(0), setidxs(0), blocks(0)).toHexString)
  /*  Miniconfig set up. Block: 16 bytes. Word 32bit, Sets 256.
     Tag (-Bank id) | Bank id  |  Set (8bits)  |  Block (2 bits ) |  Word (2bits) | */
  poke(c.io.cpu.MemReq(0).bits.addr, get_addr(tags(0), setidxs(0), blocks(0)).U)
  poke(c.io.cpu.MemReq(0).bits.tile, 0.U)
  poke(c.io.cpu.MemReq(0).bits.iswrite, false.B)
  poke(c.io.cpu.MemReq(0).valid, true.B)
  poke(c.io.cpu.MemReq(1).bits.addr, get_addr(tags(0), setidxs(0), blocks(0)).U)
  poke(c.io.cpu.MemReq(1).bits.tile, 0.U)
  poke(c.io.cpu.MemReq(1).bits.iswrite, false.B)
  poke(c.io.cpu.MemReq(1).valid, true.B)
  step(1)
  poke(c.io.cpu.MemReq(0).valid, false.B)
  poke(c.io.cpu.MemReq(1).valid, true.B)
  step(10)
}

class SuperParallelCacheUnitTester extends FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "SuperCache tester" in {
    chisel3.iotesters.Driver(() => new NParallelCache(2, 2)) { c =>
      new SuperParallelCacheUnitTests(c)
    } should be(true)
  }
}

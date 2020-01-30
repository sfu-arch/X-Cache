package dandelion.fpu

import chisel3._
import chisel3.iotesters._
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import chipsalliance.rocketchip.config._
import dandelion.config._


class SharedFPUTests(c: SharedFPU)(implicit p: Parameters) extends PeekPokeTester(c) {

  poke(c.io.InData(0).bits.RouteID, 0.U)
  poke(c.io.InData(1).bits.RouteID, 1.U)
  poke(c.io.InData(0).bits.data("field0").data, 0x6C00.U)
  poke(c.io.InData(0).bits.data("field1").data, 0x4C00.U)
  poke(c.io.InData(0).bits.data("field2").data, 0.U)
  poke(c.io.InData(0).valid, 1.U)
  poke(c.io.InData(1).valid, 1.U)
  poke(c.io.InData(1).bits.data("field0").data, 0x6C00.U)
  poke(c.io.InData(1).bits.data("field1").data, 0x4C00.U)
  poke(c.io.InData(1).bits.data("field2").data, 1.U)
  poke(c.io.InData(1).valid, 0.U)
  step(100)
}


class SharedFPUTester extends FlatSpec with Matchers {
  implicit val p = new WithAccelConfig(DandelionAccelParams(dataLen = 16))
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new SharedFPU(NumOps = 2, PipeDepth = 5)(t = p(DandelionConfigKey).fType)) {
      c => new SharedFPUTests(c)
    } should be(true)
  }
}

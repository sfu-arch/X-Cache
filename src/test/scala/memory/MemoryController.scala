package memory

/**
  * Created by vnaveen0 on 2/6/17.
  */

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._


class MemoryControllerTests(c: MemoryController)(implicit p: config.Parameters) extends PeekPokeTester(c) {
  for (t <- 0 until 12) {
    printf("Memory Controller \n")
    step(1)
  }

}


class MemoryControllerTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new MemoryController(NReads = 1, NWrites = 1)(p)) { c =>
      new MemoryControllerTests(c)
    } should be(true)
  }
}

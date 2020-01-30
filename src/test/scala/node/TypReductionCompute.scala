// See LICENSE for license details.

package dandelion.node

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces._


// Tester.
class TypReduceCompTests(df: TypReduceCompute[matNxN])
                        (implicit p: Parameters) extends PeekPokeTester(df) {


  poke(df.io.enable.valid, true)
  poke(df.io.enable.bits.control, true)

  poke(df.io.LeftIO.bits.data, 0x0004000300020001L)
  poke(df.io.LeftIO.valid, true)
  poke(df.io.LeftIO.bits.predicate, true)

  poke(df.io.Out(0).ready, true)
  for (i <- 0 until 10) {
    step(1)
    step(1)
    step(1)
  }
}


class TypReduceCompTester extends FlatSpec with Matchers {
  implicit val p = new WithAccelConfig
  it should "Typ Compute Tester" in {
    chisel3.iotesters.Driver.execute(Array("--backend-name", "verilator", "--target-dir", "test_run_dir"),
      () => new TypReduceCompute(NumOuts = 1, ID = 0, opCode = "Mul")(sign = false)(new matNxN(2))) {
      c => new TypReduceCompTests(c)
    } should be(true)
  }
}


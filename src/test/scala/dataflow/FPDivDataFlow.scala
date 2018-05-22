// // See LICENSE for license details.

package dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import util._
import interfaces._





class FPDivDataFlowTester(df: FPDivDataFlow)(implicit p: config.Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 64){
		step(1)
	}
}



class FPDivDataflowTests extends ChiselFlatSpec {
  behavior of "Accumulator"
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  backends foreach {backend =>
    it should s"correctly accumulate randomly generated numbers in $backend" in {
      Driver(() => new FPDivDataFlow()(p), backend)(c => new FPDivDataFlowTester(c)) should be (true)
    }
  }
}

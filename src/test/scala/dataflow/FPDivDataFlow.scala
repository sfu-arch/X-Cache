// // See LICENSE for license details.

package dandelion.dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._
import util._
import dandelion.interfaces._





class FPDivDataFlowTester(df: FPDivDataFlow)(implicit p: Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 64){
		step(1)
	}
}



class FPDivDataflowTests extends ChiselFlatSpec {
  behavior of "Accumulator"
  implicit val p = new WithAccelConfig
  backends foreach {backend =>
    it should s"correctly accumulate randomly generated numbers in $backend" in {
      Driver(() => new FPDivDataFlow()(p), backend)(c => new FPDivDataFlowTester(c)) should be (true)
    }
  }
}

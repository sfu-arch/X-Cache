// // See LICENSE for license details.

package dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._
import util._
import dandelion.interfaces._





class TypeMemDataFlowTester(df: TypeMemDataFlow)(implicit p: Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 50){
		step(1)
	}
expect(0.U,0.U)
}



class TypeMemDataflowTests extends ChiselFlatSpec {
  behavior of "Accumulator"
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  backends foreach {backend =>
    it should s"correctly accumulate randomly generated numbers in $backend" in {
      Driver(() => new TypeMemDataFlow()(p), backend)(c => new TypeMemDataFlowTester(c)) should be (true)
    }
  }
}

// class TypeMemDataflowTests extends  FlatSpec with Matchers {
//    implicit val p = Parameters.root((new MiniConfig).toInstance)
//   it should "Dataflow tester" in {
//      chisel3.iotesters.Driver(() => new TypeMemDataFlow()(p)) { c =>
//        new TypeMemDataFlowTester(c)
//      } should be(true)
//    }
//  }




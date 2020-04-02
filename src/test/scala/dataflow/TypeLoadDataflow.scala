// // See LICENSE for license details.

package dandelion.dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._
import util._
import dandelion.interfaces._





class TypeLoadDataFlowTester(df: TypeLoadDataFlow)(implicit p: Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 500){
		step(1)
	}

}




class TypeLoadDataflowTests extends  FlatSpec with Matchers {
   implicit val p = new WithAccelConfig ++ new WithTestConfig
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new TypeLoadDataFlow()(p)) { c =>
       new TypeLoadDataFlowTester(c)
     } should be(true)
   }
 }




// // See LICENSE for license details.

package dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import util._
import interfaces._





class TypeLoadDataFlowTester(df: TypeLoadDataFlow)(implicit p: config.Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 500){
		step(1)
	}

}




class TypeLoadDataflowTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new TypeLoadDataFlow()(p)) { c =>
       new TypeLoadDataFlowTester(c)
     } should be(true)
   }
 }




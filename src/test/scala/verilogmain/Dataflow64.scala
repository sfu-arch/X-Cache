// // See LICENSE for license details.

package verilogmain


import dataflow._
import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._
import util._
import interfaces._



/*

class DataflowPeekPoker64(df: DataFlow64)(implicit p: Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 50){
		step(1)
	}
}



//object MixedDataflowVerilog32 extends App {
  //implicit val p = Parameters.root((new MixedDataflowConfig).toInstance)
  //chisel3.iotesters.Driver.execute(args, () => new MixedDataFlow32()(p))
  //{ c => new MixedDataflowPeekPoker(c)  }
//}

class DataflowVerilog64 extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Not fuse tester" in {
    chisel3.iotesters.Driver.execute(Array("--backend-name", "verilator", "--target-dir", "test_run_dir"),
      () => new DataFlow64()(p)) {
      c => new DataflowPeekPoker64(c)
    } should be(true)
  }

}

*/


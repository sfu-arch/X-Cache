// // See LICENSE for license details.

package verilogmain


import dataflow._
import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import util._
import interfaces._





class MixedDataflowPeekPoker(df: MixedDataFlow)(implicit p: config.Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 50){
		step(1)
	}
}



object MixedDataflowVerilog extends App {
  implicit val p = config.Parameters.root((new MixedDataflowConfig).toInstance)
  chisel3.iotesters.Driver.execute(args, () => new MixedDataFlow()(p))
  { c => new MixedDataflowPeekPoker(c)  }
}
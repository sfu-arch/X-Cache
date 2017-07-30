// // See LICENSE for license details.

package verilogmain


import dataflow._
import node._
import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import config._
import util._
import interfaces._





class ChainPeekPoker(df: Chain)(implicit p: config.Parameters) extends PeekPokeTester(df)  {
	for(t <- 0 until 10){
		step(1)
	}
}



object ChainVerilog extends App {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  chisel3.iotesters.Driver.execute(args, () => new Chain(NumOps = 3, ID = 0, OpCodes = Array("And","Xor","Add"))(sign = false))
  { c => new ChainPeekPoker(c)  }
}

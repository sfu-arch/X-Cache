// See LICENSE for license details.

package dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import dataflow._




class DataflowTests (DF: DataFlow) extends PeekPokeTester(DF)  {

  poke(DF.io.In1.bits,3)
  poke(DF.io.In2.bits,4)
  poke(DF.io.In3.bits,5)
  poke(DF.io.In1.valid,1)
  poke(DF.io.In2.valid,0)
  poke(DF.io.In3.valid,0)
  poke(DF.io.Out1.ready,1)


  for {
   i <- 0 to 20
 }
 {
  println(s"io.out.bits.c: ${peek(DF.io.Out1.bits)}, io.out.bits.valid: ${peek(DF.io.Out1.valid)} state: ${peek(DF.io.Out1.bits)} should be ${3+4-5}")
  step(1)    
  if (i == 3)
  {
    poke(DF.io.In2.valid,1)
  }
  if( i == 8)
  {
    poke(DF.io.In3.valid,1)
  }
  if (i == 10)
  {
    poke(DF.io.In1.valid,0)
    poke(DF.io.In2.valid,0)
    poke(DF.io.In3.valid,0)
  }
}
}


class DecoupledNodeTester extends FlatSpec with Matchers {
  behavior of "DecoupledNodeSpec"

  val xLen = 32

  it should "compute dataflow {(3+4) * 5} excellently" in {
    chisel3.iotesters.Driver(() => new DataFlow(xLen)) { c =>
      new DataflowTests(c)
      } should be(true)
    }
  }

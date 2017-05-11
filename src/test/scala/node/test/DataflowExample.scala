// See LICENSE for license details.

package dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import dataflow._




class DataflowTests (DF: DataFlow) extends PeekPokeTester(DF)  {


  for( t <- 0 to 10 ){

    val rdn1 = rnd.nextInt(32)
    val rdn2 = rnd.nextInt(32)
    val rdn3 = rnd.nextInt(32)
    println(s"In1: ${rdn1}, In2: ${rdn2}, In3: ${rdn3}")

    poke(DF.io.In1.bits,rdn1)
    poke(DF.io.In2.bits,rdn2)
    poke(DF.io.In3.bits,rdn3)
    poke(DF.io.In1.valid,0)
    poke(DF.io.In2.valid,0)
    poke(DF.io.In3.valid,0)
    poke(DF.io.Out1.ready,1)

    step(1)
    println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${rdn1+rdn2-rdn3}")

    poke(DF.io.In1.valid,1)
    poke(DF.io.In2.valid,1)
    poke(DF.io.In3.valid,1)

    step(1)
    println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    step(1)

    println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    step(1)
    println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    step(1)
    println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    
    expect(DF.io.Out1.bits, rdn1+rdn2-rdn3)
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

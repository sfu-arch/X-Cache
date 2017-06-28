// See LICENSE for license details.

package arbiters 

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import dataflow._
import muxes._
import config._
import util._
import interfaces._
import arbiters._


class ArbiterTester (bus: WordRegFile)(implicit p: config.Parameters) extends PeekPokeTester(bus)  {
    // val dut = Module(AbstractBus)


    poke(bus.io.WriteIn(0).valid,0.U)

    poke(bus.io.WriteIn(1).bits.address,15.U)
    poke(bus.io.WriteIn(1).valid,1.U)
    poke(bus.io.WriteIn(1).bits.data,1500.U)

    poke(bus.io.ReadIn(0).valid,1.U)
    poke(bus.io.ReadIn(0).bits.address,15.U)
    poke(bus.io.ReadIn(1).valid,1.U)
    poke(bus.io.ReadIn(1).bits.address,20.U)


    for (i <- 2 to 9)
    {
      poke(bus.io.ReadIn(i).valid,0.U)
      poke(bus.io.WriteIn(i).valid,0.U)
    } 


    for (i <- 0 to 10) 
    {
      println(s"io.out.bits[0]: io.out.bits[1]: ${peek(bus.io.ReadOut(0))} io.out.bits: ${peek(bus.io.ReadOut(1))}")
              step(1)     
    }

    // // dut.io.AllocaIn(0).valid := true.B
    //      println(s"io.in.bits, io.out.bits: ${peek(stack.io.AllocaOut(0))}")
    // step(1)
    //      println(s"io.out.bits: ${peek(stack.io.AllocaOut(0))}")
    // step(1)
    //          println(s"io.out.bits: ${peek(stack.io.AllocaOut(0))}")
    // step(1)
    //  println(s"io.out.bits: ${peek(stack.io.AllocaOut(0))}")
 
}

class ArbiterTests extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "compute gcd excellently" in {
    chisel3.iotesters.Driver(() => new WordRegFile(Size=32, NReads=10, NWrites=10)) { c =>
      new ArbiterTester(c)
    } should be(true)
  }
}



// See LICENSE for license details.

package node

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





// Tester.
class TypCompTests(df: TypCompute[mat2x2])
                  (implicit p: config.Parameters) extends PeekPokeTester(df)  {

  
    poke(df.io.enable.valid,true)
    poke(df.io.enable.bits.control,true)

    poke(df.io.LeftIO.bits.data,0x1111222233334444L)
    poke(df.io.LeftIO.valid,true)
    poke(df.io.LeftIO.bits.predicate,true)

    poke(df.io.RightIO.bits.data,0x1111222233334444L)
    poke(df.io.RightIO.valid,true)
    poke(df.io.RightIO.bits.predicate,true)

    poke(df.io.Out(0).ready,true)
    for (i <- 0 until 10) {
    step(1)
    step(1)
    step(1)
  }
 }




class TypCompTester extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Typ Compute Tester" in {
     chisel3.iotesters.Driver.execute(Array("--backend-name", "verilator", "--target-dir", "test_run_dir"),
       () => new TypCompute(NumOuts = 1, ID = 0, opCode = "Add")(sign = false)(new mat2x2)) {
       c => new TypCompTests(c)
     } should be(true)
   }
 }




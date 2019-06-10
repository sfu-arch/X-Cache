package dataflow.fuse

/**
  * Created by vnaveen0 on 26/6/17.
  */

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import dataflow._
import muxes._
import dandelion.config._
import util._
import interfaces._
import dataflow._




// Tester.
class computeFuse01Tester(df: ComputeFuse01DF)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.data0.bits.data, 4.U)
  poke(df.io.data0.valid, false.B)
  poke(df.io.data0.bits.predicate, true.B)

  poke(df.io.data1.bits.data, 5.U)
  poke(df.io.data1.valid, false.B)
  poke(df.io.data1.bits.predicate, true.B)

  poke(df.io.data2.bits.data, 6.U)
  poke(df.io.data2.valid, false.B)
  poke(df.io.data2.bits.predicate, true.B)

  poke(df.io.data3.bits.data, 7.U)
  poke(df.io.data3.valid, false.B)
  poke(df.io.data3.bits.predicate, true.B)

  poke(df.io.enable.bits.control, false.B)
  poke(df.io.enable.valid, false.B)

  poke(df.io.dataOut.ready, true.B)
  println(s"Output: ${peek(df.io.dataOut)}\n")

  step(1)

  poke(df.io.data0.valid, true.B)
  poke(df.io.data1.valid, true.B)
  poke(df.io.data2.valid, true.B)
  poke(df.io.data3.valid, true.B)
  poke(df.io.enable.bits.control, true.B)
  poke(df.io.enable.valid, true.B)

  println(s"Output: ${peek(df.io.dataOut)}\n")

  for(i <- 0 until 20){
    println(s"Output: ${peek(df.io.dataOut)}")

    println(s"t: ${i}\n ------------------------")
    step(1)
  }
}


class ComputeF01Tests extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val myargs = Array("--backend-name", "verilator", "--target-dir", "test_run_dir")
  chisel3.iotesters.Driver.execute(myargs, () => new ComputeFuse01DF()(p))
  { c => new computeFuse01Tester(c)  }
}

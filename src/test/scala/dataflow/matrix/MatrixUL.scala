package dataflow.matrix

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
import config._
import util._
import interfaces._


// Tester.
class MatrixULTester(df: __offload_func_3DF)
                  (implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.data_0.bits.data, 4.U)
  poke(df.io.data_0.valid, false.B)
  poke(df.io.data_0.bits.predicate, true.B)

  poke(df.io.data_1.bits.data, 5.U)
  poke(df.io.data_1.valid, false.B)
  poke(df.io.data_1.bits.predicate, true.B)

  poke(df.io.data_2.bits.data, 6.U)
  poke(df.io.data_2.valid, false.B)
  poke(df.io.data_2.bits.predicate, true.B)

  poke(df.io.data_3.bits.data, 7.U)
  poke(df.io.data_3.valid, false.B)
  poke(df.io.data_3.bits.predicate, true.B)

  poke(df.io.data_4.bits.data, 8.U)
  poke(df.io.data_4.valid, false.B)
  poke(df.io.data_4.bits.predicate, true.B)

  poke(df.io.data_5.bits.data, 9.U)
  poke(df.io.data_5.valid, false.B)
  poke(df.io.data_5.bits.predicate, true.B)

  poke(df.io.start, true.B)
  poke(df.io.pred.ready, true.B)
  poke(df.io.result.ready, true.B)

  poke(df.io.CacheReq.ready, false.B)

  println(s"Output: ${peek(df.io.result)}\n")

  step(1)

  poke(df.io.data_0.valid, true.B)
  poke(df.io.data_1.valid, true.B)
  poke(df.io.data_2.valid, true.B)
  poke(df.io.data_3.valid, true.B)
  poke(df.io.data_4.valid, true.B)
  poke(df.io.data_5.valid, true.B)
  poke(df.io.CacheReq.ready, true.B)

  println(s"Output: ${peek(df.io.result)}\n")

  for(i <- 0 until 20){
    println(s"Output: ${peek(df.io.result)}")

    println(s"t: ${i}\n ------------------------")
    step(1)
  }
}


class MatrixULTests extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Not fuse tester" in {
    chisel3.iotesters.Driver.execute(Array("--backend-name", "verilator", "--target-dir", "test_run_dir"),
      () => new __offload_func_3DF()) {
      c => new MatrixULTester(c)
    } should be(true)
  }

}

/*
object MatrixULVerilog extends App {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  chisel3.iotesters.Driver.execute(args, () => new __offload_func_3DF()(p))
  { c => new MatrixULTester(c)  }
}
*/
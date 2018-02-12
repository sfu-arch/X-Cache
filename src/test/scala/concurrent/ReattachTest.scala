package dataflow

import chisel3._
import chisel3.util._
import node._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import config._
import junctions.NastiIO


class ReattachTest01(c: Reattach) extends PeekPokeTester(c) {

  // Initialize
  poke(c.io.enable.valid, false.B)
  var idx = 0
  var count_in = Array(0,0,0)
  var count_back = 0

  poke(c.io.enable.valid, true.B)
  poke(c.io.enable.bits.control, true.B)

  for (t <- 0 until 36) {
    step(1)

    // Drop input ready flag 20% of the time
    if (t % 5 > 2) {
      poke(c.io.Out(0).ready, false.B)
    } else {
      poke(c.io.Out(0).ready, true.B)
    }

    for (i <- 0 until 3) {
     if (peek(c.io.InCtrl(i).ready) == 1) {
        poke(c.io.InCtrl(i).valid, true.B)
        poke(c.io.InCtrl(i).bits.control, true.B)
        poke(c.io.InCtrl(i).bits.taskID, count_in(i))
        count_in(i) += 1
      }
    }
    if (peek(c.io.Out(0).valid) == 1 && peek(c.io.Out(0).ready) == 1) {
      val taskID = peek(c.io.Out(0).bits.taskID)
      if (taskID != count_back) {
        println(s"Comparison failed. $taskID != $count_back ")
        fail
      }
      count_back += 1
    }
  }

  for (t <- 0 until 10) {
    step(1)
  }

  if (count_back == 0) {
    println(s"ERROR: Didn't see any valid responses from downstream block")
    fail
  }

}

class ReattachTest02(c: Reattach) extends PeekPokeTester(c) {

  // Initialize
  poke(c.io.enable.valid, false.B)
  var idx = 0
  var count_in = Array(0,0,0)
  var count_back = 0

  poke(c.io.enable.valid, true.B)
  poke(c.io.enable.bits.control, true.B)

  for (t <- 0 until 36) {
    step(1)

    // Drop input ready flag 20% of the time
    if (t % 5 > 2) {
      poke(c.io.Out(0).ready, false.B)
    } else {
      poke(c.io.Out(0).ready, true.B)
    }

    for (i <- 0 until 3) {
      if (peek(c.io.InData(i).ready) == 1) {
        poke(c.io.InData(i).valid, true.B)
        poke(c.io.InData(i).bits.predicate, true.B)
        poke(c.io.InData(i).bits.taskID, count_in(i))
        count_in(i) += 1
      }
    }
    if (peek(c.io.Out(0).valid) == 1 && peek(c.io.Out(0).ready) == 1) {
      val taskID = peek(c.io.Out(0).bits.taskID)
      if (taskID != count_back) {
        println(s"Comparison failed. $taskID != $count_back ")
        fail
      }
      count_back += 1
    }
  }

  for (t <- 0 until 10) {
    step(1)
  }

  if (count_back == 0) {
    println(s"ERROR: Didn't see any valid responses from downstream block")
    fail
  }

}

class ReattachTester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  var tbn = "verilator"
//  var tbn = "firrtl"

  it should "Pass reattach based on control inputs." in {
    // iotestester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(//"-ll", "Info",
        "-tbn", tbn,
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new Reattach(3,0,ID = 0)(p)) {
      c => new ReattachTest01(c)
    } should be(true)
  }
  it should "Pass reattach based on data inputs." in {
    // iotestester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(//"-ll", "Info",
        "-tbn", tbn,
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new Reattach(0,3,ID = 1)(p)) {
      c => new ReattachTest02(c)
    } should be(true)
  }
}

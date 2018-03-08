package dataflow


import chisel3._
import chisel3.util._
import node._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import config._
import junctions.NastiIO

class TaskControllerTester(c: TaskController, backpressure: Boolean) extends PeekPokeTester(c) {

  // Initialize parent interfaces
  for (d <- 0 until c.io.parentIn.length) {
    poke(c.io.parentIn(d).valid,     false.B)
    poke(c.io.parentIn(d).bits.data("field0").data, 0.U)
    poke(c.io.parentIn(d).bits.data("field0").predicate, true.B)
    poke(c.io.parentIn(d).bits.data("field0").taskID, 0.U)
    poke(c.io.parentIn(d).bits.data("field1").data, 0.U)
    poke(c.io.parentIn(d).bits.data("field1").predicate, true.B)
    poke(c.io.parentIn(d).bits.data("field1").taskID, 0.U)
    poke(c.io.parentIn(d).bits.enable.control, false.B)
  }
  for(d <- 0 until c.io.parentOut.length) {
    poke(c.io.parentOut(d).ready, true.B)
  }
  // Initialize child interface
  poke(c.io.childIn(0).valid, false.B)
  poke(c.io.childIn(0).bits.data("field0").data, 0.U)
  poke(c.io.childIn(0).bits.data("field0").predicate, 0.U)
  poke(c.io.childIn(0).bits.enable.control, false.B)

  var idx = 0
  var count_in = 0
  var count_out = 0
  var count_back = 0

  for (t <- 0 until 36) {
    step(1)
    //    println(s"c.io.in(idx).ready = ${peek(c.io.in(idx).ready)}")
    // By default clear all valid strobes
    for (k <- 0 until c.io.parentIn.length) {
      poke(c.io.parentIn(k).valid, false.B)
    }
    // Set each input in succession skipping the last one. Increment
    // the data values
    idx = t % (c.io.parentIn.length - 1)
    if (peek(c.io.parentIn(idx).ready) == 1) {
      poke(c.io.parentIn(idx).valid, true.B)
      poke(c.io.parentIn(idx).bits.data("field0").taskID, count_in)
      poke(c.io.parentIn(idx).bits.data("field0").data, count_in)
      poke(c.io.parentIn(idx).bits.data("field1").taskID, count_in)
      poke(c.io.parentIn(idx).bits.data("field1").data, count_in + 1)
      count_in += 1
    }
    // Drop input ready flag 20% of the time if backpressure is requested
    if ((t % 5 > 2) && backpressure) {
      poke(c.io.childOut(0).ready, false.B)
    } else {
      poke(c.io.childOut(0).ready, true.B)
    }

    // Provide response by just looping back arg1
    if (peek(c.io.childOut(0).valid) == 1 && peek(c.io.childOut(0).ready) == 1) {
      poke(c.io.childIn(0).valid, true.B)
      poke(c.io.childIn(0).bits.data("field0").data, peek(c.io.childOut(0).bits.data("field0").data))
      poke(c.io.childIn(0).bits.data("field0").taskID, peek(c.io.childOut(0).bits.data("field0").taskID))
    } else {
      poke(c.io.childIn(0).valid, false.B)
      poke(c.io.childIn(0).bits.data("field0").data, 0.U)
      poke(c.io.childIn(0).bits.data("field0").taskID, 0.U)
    }

    // Check that the request output is incrementing (i.e. inputs were serviced in round robin order.)
    if (peek(c.io.childOut(0).valid) == 1 && peek(c.io.childOut(0).ready) == 1) {
      val field0 = peek(c.io.childOut(0).bits.data("field0").data)
      if(field0 != count_out) {
        println(s"childOut 'field1' comparison failed. $field0 != $count_out")
        fail
      }
      val field1 = peek(c.io.childOut(0).bits.data("field1").data)
      if(field1 != count_out + 1) {
        println(s"childOut 'field1' comparison failed. $field1 != ${count_out + 1}")
        fail
      }
      count_out += 1
    }

    for (d <- 0 until c.io.parentOut.length) {
      if (peek(c.io.parentOut(d).valid) == 1 && peek(c.io.parentOut(d).ready) == 1) {
        val field0 = peek(c.io.parentOut(d).bits.data("field0").data)
        if (field0 != count_back) {
         println(s"parentOut 'field0' comparison failed. $field0 != $count_back ")
         fail
        }
        count_back += 1
      }
    }
  }

  for (t <- 0 until 10) {
    step(1)
  }

  if (count_out == 0) {
    println(s"ERROR: Didn't see any valid otput requests sent to downstream block.")
    fail
  }
  if (count_back == 0) {
    println(s"ERROR: Didn't see any valid responses from downstream block")
    fail
  }
}

class TaskControllerTests extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  var tbn = "verilator"
//  var tbn = "firrtl"

  it should "Arbitrate and queue all requests" in {
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
      () => new TaskController(List(8,16), List(16), 4, 1)) {
      c => new TaskControllerTester(c, false)
    } should be(true)
  }

  it should "Arbitrate and queue all requests with backpressure asserted" in {
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
      () => new TaskController(List(8,16), List(16), 4, 1)) {
      c => new TaskControllerTester(c, true)
    } should be(true)
  }

}

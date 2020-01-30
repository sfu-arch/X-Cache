package dandelion.concurrent


import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import chipsalliance.rocketchip.config._
import dandelion.config._


class DetachTests(c: Detach) extends PeekPokeTester(c) {

  val maxID = 10

  // Initialize
  poke(c.io.enable.bits.control, true.B)
  poke(c.io.enable.valid, false.B)

  var idx = 0
  var count_in = 0
  var count_out = Array(0,0,0)

  for (t <- 0 until 100) {
    step(1)
    poke(c.io.enable.valid, false.B) // default

    if (count_in < maxID) {
      // Modify backpressure
      if (t % 5 > 2) {
        poke(c.io.Out(0).ready, false.B)
      } else {
        poke(c.io.Out(0).ready, true.B)
      }
      if (t % 4 > 2) {
        poke(c.io.Out(1).ready, false.B)
      } else {
        poke(c.io.Out(1).ready, true.B)
      }
      if (t % 6 > 2) {
        poke(c.io.Out(2).ready, false.B)
      } else {
        poke(c.io.Out(2).ready, true.B)
      }

      if (peek(c.io.enable.ready) == 1) {
        poke(c.io.enable.valid, true.B)
        poke(c.io.enable.bits.control, true.B)
        poke(c.io.enable.bits.taskID, count_in.U)
        count_in += 1
      }

    }

    // Check that the output taskID is incrementing
    for (i <- 0 until 3) {
      if (peek(c.io.Out(i).valid) == 1 && peek(c.io.Out(i).ready) == 1
        && peek(c.io.Out(i).bits.control) == 1) {
        val taskID = peek(c.io.Out(i).bits.taskID)
        if (taskID != count_out(i)) {
          println(s"Unexpected taskID on output port $i. Expected ${count_out(i)}. Received $taskID\n")
          fail
        }
        count_out(i) += 1
      }
    }

  }

  for (t <- 0 until 10) {
    step(1)
  }

  for (i <- 0 until 3) {
    if (count_out(i) == (maxID-1)) {
      println(s"ERROR: Didn't see correct valid output sent to downstream blocks.")
      fail
    }
  }
}

class DetachTester extends FlatSpec with Matchers {
  implicit val p = new WithAccelConfig
  var tbn = "verilator"
//  var tbn = "firrtl"

  it should "Pass task requests and responses and generate sync increment pulse." in {
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
      () => new Detach(ID = 0)) {
      c => new DetachTests(c)
    } should be(true)
  }

}

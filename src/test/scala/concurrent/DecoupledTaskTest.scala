package dataflow

import chisel3._
import chisel3.util._
import node._
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}
import config._
import control.BasicBlockNoMaskNode
import interfaces._
import junctions._

import scala.util.Random

class DecoupledChildDF(val ID:Int)(implicit p: Parameters) extends Module {
  val io = IO(new Bundle {
   val In = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val Out = Decoupled(new Call(List(32)))
  })
  val bb = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 0))
  val splitIn = Module(new SplitCall(List(32,32,32,32)))
  val addModule1 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false))
  val addModule2 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "Add")(sign = false))
  val addModule3 = Module(new ComputeNode(NumOuts = 1, ID = 2, opCode = "Add")(sign = false))
  val ret4 = Module(new RetNode(ID=3,retTypes=List(32)))

  /* Wire task modules to controller */
  splitIn.io.In <> io.In
  bb.io.predicateIn <> splitIn.io.Out.enable

  addModule1.io.enable <> bb.io.Out(0)
  addModule1.io.LeftIO <> splitIn.io.Out.data.elements("field0")
  addModule1.io.RightIO <> splitIn.io.Out.data.elements("field1")

  addModule2.io.enable <> bb.io.Out(1)
  addModule2.io.LeftIO <> splitIn.io.Out.data.elements("field2")
  addModule2.io.RightIO <> splitIn.io.Out.data.elements("field3")

  addModule3.io.enable.bits.control := true.B
  addModule3.io.enable.valid := true.B
  addModule3.io.enable.bits.taskID := 0.U
  addModule3.io.LeftIO <> addModule1.io.Out(0)
  addModule3.io.RightIO <> addModule2.io.Out(0)

  ret4.io.enable.bits.control := true.B
  ret4.io.enable.valid := true.B
  ret4.io.enable.bits.taskID := 0.U
  ret4.io.In.elements("field0") <> addModule3.io.Out(0)
  io.Out <> ret4.io.Out

}

class DecoupledTaskDF()(implicit p: Parameters) extends Module {
  val io = IO(new Bundle {
    val In = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val Out = Decoupled(new Call(List(32)))
  })

  val numChildren = 4

  /* Instantiate modules */
  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(32), 1, numChildren)(p.alterPartial({case TLEN => 6})))

  /* Wire up task module to tester inputs */
  TaskControllerModule.io.parentIn(0) <> io.In

  val AdderChild = for (c <- 0 until numChildren) yield {
    val adder = Module(new DecoupledChildDF(c))
    adder.io.In <> TaskControllerModule.io.childOut(c)
    TaskControllerModule.io.childIn(c) <> adder.io.Out
  }

  /* wire up module to tester outputs */
  io.Out <> TaskControllerModule.io.parentOut(0)

}

class DecoupledTaskTester1(c: DecoupledTaskDF) extends PeekPokeTester(c) {
  val testVals = 50
  var timeOut = 1000
  val in = Array.fill(4,testVals)(Random.nextInt(1000))
  val out = Array((in(0), in(1)).zipped.map(_ + _),
                  (in(2), in(3)).zipped.map(_ + _))
  val out2 = Array((out(0), out(1)).zipped.map(_ + _))


  poke(c.io.Out.ready, true.B)
  poke(c.io.In.valid, false.B)

  for (i <- 0 until 4) {
    poke(c.io.In.bits.data(s"field$i").predicate, false.B)
    poke(c.io.In.bits.data(s"field$i").taskID, 0.U)
    poke(c.io.In.bits.data(s"field$i").data, 0.U)
  }
  step(1)

  var count = 0
  var countIn = 0
  var countOut = 0

  while (countOut < testVals && count < timeOut) {
    // Randomly fire inputs when ready is asserted.
    val gate = true //Random.nextBoolean

    if (peek(c.io.In.ready) == 1 && gate && countIn < testVals) {
      poke(c.io.In.valid, true.B)
      poke(c.io.In.bits.enable.control, true.B)
      for (i <- 0 until 4) {
        poke(c.io.In.bits.data(s"field$i").predicate, true.B)
        poke(c.io.In.bits.data(s"field$i").taskID, countIn)
        poke(c.io.In.bits.data(s"field$i").data, in(i)(countIn))
      }
      countIn += 1
    } else {
      poke(c.io.In.valid, false.B)
      poke(c.io.In.bits.enable.control, false.B)
      for (i <- 0 until 4) {
        poke(c.io.In.bits.data(s"field$i").predicate, false.B)
        poke(c.io.In.bits.data(s"field$i").data, 0.U)
      }
    }

    // Compare outputs against expected data when valid is asserted
    if (peek(c.io.Out.valid) == 1 && countOut < testVals) {
      for (i <- 0 until 1) {
        val field = peek(c.io.Out.bits.data(s"field$i").data)
        val taskID = peek(c.io.Out.bits.data(s"field$i").taskID)
        val expected = out2(i)(taskID.intValue())
        if(field != expected) {
          println(s"countOut $countOut: field$i error.  Expected $expected.  Received $field")
          fail
        }
      }
      countOut += 1
    }

    step(1)
    count += 1
  }

  if (count >= timeOut) {
    println(s"Timeout! countIn=$countIn countOut=$countOut")
    fail
  }

  step(5)

}

class DecoupledTaskTests extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  var tbn = "verilator"
  //var tbn = "firrtl"

  it should "Send random data at random intervals into adder tree." in {
    // iotestester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(//"-ll", "Info",
        "-tbn", tbn,
        "-td", "test_run_dir",
        "-tts", "0001")
      , () => new DecoupledTaskDF()(p)) {c =>
        new DecoupledTaskTester1(c)
    } should be(true)
  }
}

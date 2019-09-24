package dandelion.node


import chisel3.iotesters._
import org.scalatest.{Matchers, FlatSpec}
import chisel3.MultiIOModule
import dandelion.config._
import chisel3._
import chisel3.iotesters._
import utility._

class LoadAliasTests(c: UnTypLoadAlias) extends PeekPokeTester(c) {
  def N = false

  def Y = true

  val Control = Map(
    "Default" -> List(N, N, N, N, N, N, N, N),
    "Active" -> List(N, N, N, N, N, N, Y),
    "Input" -> List(Y, Y, Y, Y, N, N, Y),
    "~Input" -> List(Y, Y, Y, N, N, N, Y),
    "~Control" -> List(Y, N, Y, N, N, N, Y)
  ).withDefaultValue(List(N, N, N, N, N, N, N, N, N))

  val AliasControl = Map(
    "Default" -> List(N, N, N, N),
    "Active" -> List(Y, N, Y, N),
    "Arrive" -> List(N, Y, N, Y)
  ).withDefaultValue(List(N, N, N, N))


  val sigs = Seq(c.io.enable.valid, c.io.enable.bits.control,
    c.io.GepAddr.valid, c.io.GepAddr.bits.predicate,
    c.io.memReq.ready, c.io.memResp.valid, c.io.Out(0).ready
  )
  sigs zip Control("Default") map { case (s, d) => poke(s, d) }
  sigs zip Control("Active") map { case (s, d) => poke(s, d) }

  val aliassigs = Seq(c.io.InA.In(0).valid, c.io.InA.PredOp(0).valid, c.io.InA.In(1).valid, c.io.InA.PredOp(1).valid)
  aliassigs zip AliasControl("Default") map { case (s, d) => poke(s, d) }

  // Enable1.valid,
  // Enable1.control,
  // Input1.valid,
  // Input1.data,
  // Input1.predicate,
  // Pred op.valid,
  // memReq.valid,
  // memreq.ready,
  // Succ_op,
  // io.Out
  var str = "hello"
  for (t <- 0 until 40) {
    step(1)



    //IF ready is set
    // send address
    if (peek(c.io.GepAddr.ready) == 1) {
      sigs zip Control("Input") map { case (s, d) => poke(s, d) }
      poke(c.io.GepAddr.bits.data, 12)
    }

    if ((peek(c.io.InA.In(0).ready) == 1) && (t > 8)) {
      str = "active"
      aliassigs zip AliasControl("Active") map { case (s, d) => poke(s, d) }
      poke(c.io.InA.In(0).valid, 1)
      poke(c.io.InA.In(1).valid, 1)
      poke(c.io.InA.In(0).bits.data, 20)
      poke(c.io.InA.In(1).bits.data, 0)
    }

    if (t > 8) {
      poke(c.io.InA.PredOp(0).valid, N)
    }

    if ((peek(c.io.memReq.valid) == 1) && (t > 12)) {
      poke(c.io.memReq.ready, true)
    }

    if (t > 18 && peek(c.io.memReq.ready) == 1) {
      poke(c.io.memResp.data, t)
      poke(c.io.memResp.valid, true)
      str = "inactive"
    }

    if ((t > 25) && (str != "active")) {
      aliassigs zip AliasControl("Arrive") map { case (s, d) => poke(s, d) }
    }
    // printf(s"t: ${t}  io.Out: ${peek(c.io.Out(0))} \n")
    // //Response is before request -- so that it is true in the next cycle
    // //NOte: Response should be received atleast after a cycle of memory request
    // // Otherwise it is undefined behaviour

  }

}

import Constants._

class LoadAliasTester extends FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Load Node tester" in {
    chisel3.iotesters.Driver(() => new UnTypLoadAlias(NumPredOps = 0, NumSuccOps = 0, NumAliasPredOps = 2, NumAliasSuccOps = 0, NumOuts = 1, Typ = MT_W, ID = 1, RouteID = 0)) { c =>
      new LoadAliasTests(c)
    } should be(true)
  }
}

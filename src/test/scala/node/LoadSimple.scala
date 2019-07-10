package dandelion.node


import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}
import dandelion.config._
import utility._

class LoadNodeTests(c: UnTypLoad) extends PeekPokeTester(c) {
  def N = false
  def Y = true
  val Control = Map(
        "Default"  -> List(N,N,N,N,N,N,N,N,N,N),
        "Active"   -> List(N,N, N,N, Y, N,N, Y,Y),
        "Input"   -> List(Y,Y, Y,Y, Y, N,N, Y,Y),
        "~Input"    -> List(Y,Y, Y,N, Y, N,N, Y,Y),
        "~Control"    -> List(Y,N, Y,N, Y, N,N, Y,Y)
      ).withDefaultValue(List(N,N,N,N,N,N,N,N,N,N))

  val sigs = Seq(c.io.enable.valid, c.io.enable.bits.control,
    c.io.GepAddr.valid, c.io.GepAddr.bits.predicate,
    c.io.PredOp(0).valid,
    c.io.memReq.ready,c.io.memResp.valid,
    c.io.SuccOp(0).ready,c.io.Out(0).ready
    )

  sigs zip Control("Default") map {case(s,d) => poke(s,d)}
  sigs zip Control("Active") map {case(s,d) => poke(s,d)}

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

    for (t <- 0 until 20) {
             step(1)



      //IF ready is set
      // send address
      if (peek(c.io.GepAddr.ready) == 1) {
        sigs zip Control("~Control") map {case(s,d) => poke(s,d)}
        poke(c.io.GepAddr.bits.data, 12)
      }

       if((peek(c.io.memReq.valid) == 1) && (t > 4))
      {
        poke(c.io.memReq.ready,true)
      }

      if (t > 5 && peek(c.io.memReq.ready) == 1)
      {
        poke(c.io.memResp.data,t)
        poke(c.io.memResp.valid,true)
      }
       // printf(s"t: ${t}  io.Out: ${peek(c.io.Out(0))} \n")
      // //Response is before request -- so that it is true in the next cycle
      // //NOte: Response should be received atleast after a cycle of memory request
      // // Otherwise it is undefined behaviour

    }

}

import Constants._

class LoadNodeTester extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Load Node tester" in {
    chisel3.iotesters.Driver(() => new UnTypLoad(NumPredOps=1,NumSuccOps=1,NumOuts=1,Typ=MT_W,ID=1,RouteID=0)) { c =>
      new LoadNodeTests(c)
    } should be(true)
  }
}

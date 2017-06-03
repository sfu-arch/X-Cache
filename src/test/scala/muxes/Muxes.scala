package muxes

/**
  * Created by vnaveen0 on 2/6/17.
  */

//import chisel3.core.UInt
import chisel3.core.UInt
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import config._
import interfaces.ReadReq


class DemuxTests(c: Demux) extends PeekPokeTester(c) {
  var sel = 1
  for (t <- 0 until 12) {

    poke(c.io.en,true)
    poke(c.io.sel,sel)

    printf(s" sel: ${sel}  io.valids: ${c.io.valids(sel)}")

    step(1)
  }

}


class DemuxTester extends  FlatSpec with Matchers {

  var max = 4 : Int
  //  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Demux tester" in {
    chisel3.iotesters.Driver(() => new Demux(gen = new ReadReq(), n = 4) ){
      c => new DemuxTests(c)
    } should be(true)
  }
}


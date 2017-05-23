package dataflow

/**
  * Created by nvedula on 19/5/17.
  */

import chisel3.iotesters.{PeekPokeTester}
import org.scalatest.{Matchers, FlatSpec}

import config._


class LdDFTests(c: LoadDataFlow) extends PeekPokeTester(c) {
  for (t <- 0 until 12) {

    //IF ready is set
    // send address


    println(s"t: ${t} GepAddr: ${peek(c.io.gepAddr.ready)}")
    println(s"t: ${t} GepValid: ${peek(c.io.gepAddr.valid)}")


    if(peek(c.io.gepAddr.ready) == 1 && t>2) {
      poke(c.io.gepAddr.valid,true)
      poke(c.io.gepAddr.bits,12)

      printf("\n rule1 fired \n")
    }
    else
      poke(c.io.gepAddr.valid,false)


    //at some clock - send src mem-op is done executing
    if(t > 4) {
      if (peek(c.io.predMemOp.ready) == 1) {
        poke(c.io.predMemOp.valid, true)
        println("\n predmemOP rule fired \n")
        //        poke(c.io.predMemOp(0).bits, 24)
      }
      else
        poke(c.io.predMemOp.valid, false)

    }
    else {

      poke(c.io.predMemOp.valid, false)
    }

    step(1)


  }
}

//class LoadNodeTester extends ChiselFlatSpec {
  //behavior of "LoadNode"
  //backends foreach {backend =>
    //it should s"correctly find decoupled behaviour -  $backend" in {
      //Driver(() => new LoadNode(32), backend)((c) => new LoadNodeTests(c)) should be (true)
    //}
  //}
//}

class LdDFTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
 it should "Load Node tester" in {
    chisel3.iotesters.Driver(() => new LoadDataFlow()) { c =>
      new LdDFTests(c)
    } should be(true)
  }
}

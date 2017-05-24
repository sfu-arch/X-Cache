package dataflow

/**
  * Created by nvedula on 19/5/17.
  */

import chisel3.iotesters.{PeekPokeTester}
import org.scalatest.{Matchers, FlatSpec}

import config._


class LoadDFTests(c: LoadDataFlow) extends PeekPokeTester(c) {
  for (t <- 0 until 20) {

   //IF ready is set
    // send address


    if(peek(c.io.gepAddr.valid) == 1 && peek(c.io.gepAddr.ready) == 1 ) {
      printf(s"t: ${t} GepAddr.ready: ${peek(c.io.gepAddr.ready)} \n")
      printf(s"t: ${t} GepAddr.valid: ${peek(c.io.gepAddr.valid)} \n")
      printf(s"t: ${t} GepAddr.bits: ${peek(c.io.gepAddr.bits)} \n")
    }

    if(peek(c.io.memOpAck.valid) == 1) {
      printf(s"t: ${t} ACK:      ${peek(c.io.memOpAck)} \n")
    }

    //To run single Iteration
    poke(c.io.memOpAck.ready,false)

    if(peek(c.io.gepAddr.ready) == 1 && t>2) {
      poke(c.io.gepAddr.valid,true)
      poke(c.io.gepAddr.bits,12)

      printf(s"\n ---- GepAddr Fired --- \n")

    }
    else
    {
      poke(c.io.gepAddr.valid,false)
    }



    //at some clock - send src mem-op is done executing
    if(t > 4) {
      if (peek(c.io.predMemOp.ready) == 1) {
        poke(c.io.predMemOp.valid, true)
        printf("\n --- predmemOP Fired --- \n")
      }
      else {
        poke(c.io.predMemOp.valid, false)
      }

    }
    else {

      poke(c.io.predMemOp.valid, false)
    }

    step(1)
    printf(s"t: ${t} ----------------------------------- \n")


  }
}

class LoadDFTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
 it should "Load Node tester" in {
    chisel3.iotesters.Driver(() => new LoadDataFlow()) { c =>
      new LoadDFTests(c)
    } should be(true)
  }
}

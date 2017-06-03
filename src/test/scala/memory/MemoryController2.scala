package memory

/**
  * Created by vnaveen0 on 3/6/17.
  */

import chisel3.iotesters.PeekPokeTester
import config._
import org.scalatest.{FlatSpec, Matchers}

class MemoryControllerTests2(c: MemoryController)(implicit p: config.Parameters) extends PeekPokeTester(c) {


  for (t <- 0 until 12) {

    if(t>3) {

      for( i <- 0 until 4) {
        if(peek(c.io.ReadIn(i).ready) == 1 && (i==1 || i == 2 || i==3)) {
          poke(c.io.ReadIn(i).valid, true)
        }
        else {
          poke(c.io.ReadIn(i).valid, false)
        }
      }

      //-------------------------------------------

      poke(c.io.ReadIn(3).bits.address, 33)
      poke(c.io.ReadIn(3).bits.node, 3)
      // poke(c.io.ReadIn(3).bits.mask, 3)

      //-------------------------------------------
      poke(c.io.ReadIn(2).bits.address, 22)
      poke(c.io.ReadIn(2).bits.node, 2)
      // poke(c.io.ReadIn(2).bits.mask, 2)

      //-------------------------------------------
      poke(c.io.ReadIn(1).bits.address, 11)
      poke(c.io.ReadIn(1).bits.node, 1)
      // poke(c.io.ReadIn(1).bits.mask, 1)
    }
    else {
      poke(c.io.ReadIn(0).valid, false)
      poke(c.io.ReadIn(1).valid, false)
      poke(c.io.ReadIn(2).valid, false)
      poke(c.io.ReadIn(3).valid, false)

    }


    printf(s"t: ${t} ---------------------------- \n")

    printf(s"t: ${t}  io.ReadIn: ${peek(c.io.ReadIn(1))} chosen: 1 \n")
    printf(s"t: ${t}  io.ReadIn: ${peek(c.io.ReadIn(2))} chosen: 2 \n")
    printf(s"t: ${t}  io.ReadIn: ${peek(c.io.ReadIn(3))} chosen: 3 \n")



    printf(s"t: ${t}  io.ReadOut: ${peek(c.io.ReadOut(1))} chosen: 1 \n")
    printf(s"t: ${t}  io.ReadOut: ${peek(c.io.ReadOut(2))} chosen: 2 \n")
    printf(s"t: ${t}  io.ReadOut: ${peek(c.io.ReadOut(3))} chosen: 3 \n")

    printf(s"t: ${t} ######################### \n")
    printf(s"t: ${t}  io.testReadReq: ${peek(c.io.testReadReq)} \n")

    step(1)

  }

}


class MemoryControllerTester2 extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Memory Controller tester" in {
    chisel3.iotesters.Driver(() => new MemoryController(NReads = 4, NWrites = 1)(p)) {
      c => new MemoryControllerTests2(c)
    } should be(true)
  }
}

package helpers

import java.io.PrintWriter
import java.io.File
import chisel3._
import chisel3.Module
import chisel3.iotesters._
import chipsalliance.rocketchip.config._
import dandelion.config._
import util._
import dandelion.interfaces._
import dandelion.accel._


class AccelIO(Args: List[Int], Returns: List[Int])(implicit val p: Parameters) extends Module with HasAccelParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(Args)))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(Returns))
  })

  def cloneType = new AccelIO(Args, Returns).asInstanceOf[this.type]
}


class AccelTesterLocal[T <: AccelIO](c: T)
                                    (inAddrVec: List[Int], inDataVec: List[Int],
                                     outAddrVec: List[Int], outDataVec: List[Int]) extends PeekPokeTester(c) {

  def MemRead(addr: Int): BigInt = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.iswrite, 0)
    poke(c.io.req.bits.tag, 0)
    poke(c.io.req.bits.mask, (1 << (c.io.req.bits.mask.getWidth)) - 1)
    step(1)

    poke(c.io.req.valid, 0)
    while (peek(c.io.resp.valid) == 0) {
      step(1)
    }
    val result = peek(c.io.resp.bits.data)
    result
  }

  def MemWrite(addr: Int, data: Int): BigInt = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.data, data)
    poke(c.io.req.bits.iswrite, 1)
    poke(c.io.req.bits.tag, 0)
    poke(c.io.req.bits.mask, (1 << (c.io.req.bits.mask.getWidth)) - 1)
    step(1)
    poke(c.io.req.valid, 0)
    1
  }

  def dumpMemoryInit(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until inAddrVec.length) {
      val data = MemRead(inAddrVec(i))
      pw.write("0X" + inAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }


  def dumpMemoryFinal(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until outDataVec.length) {
      val data = MemRead(outAddrVec(i))
      pw.write("0X" + outAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }

  def initMemory() = {
    // Write initial contents to the memory model.
    for (i <- 0 until inAddrVec.length) {
      MemWrite(inAddrVec(i), inDataVec(i))
    }

    dumpMemoryInit("init.mem")
    step(200)

  }

  def checkMemory() = {
    step(10)

    var valid_data = true
    for (i <- 0 until outDataVec.length) {
      val data = MemRead(outAddrVec(i))
      if (data != outDataVec(i)) {
        println(Console.RED +
          s"*** Incorrect data received. Got MEM[${outAddrVec(i).toInt}] = " +
          s"$data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
        fail
        valid_data = false
      }
      else {
        println(Console.BLUE + s"[LOG] MEM[${outAddrVec(i).toInt}] :: $data" + Console.RESET)
      }
    }
    if (valid_data) {
      println(Console.BLUE + "*** Correct data written back." + Console.RESET)
    }

    dumpMemoryFinal("final.mem")

  }

}


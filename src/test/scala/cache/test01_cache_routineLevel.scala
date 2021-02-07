package dandelion.memory.cache

import java.io.PrintWriter
import java.io.File

import chisel3._
import chisel3.Module
import util._
import dandelion.interfaces._
import dandelion.memory._
import chipsalliance.rocketchip.config._
import dandelion.config._
import chisel3.iotesters._
import dandelion.memory.cache.{HasCacheAccelParams, Gem5Cache,programmableCache}
import org.scalatest.{FlatSpec, Matchers}


class test_cache01Main_routineLevel_IO(implicit val p: Parameters) extends Module with HasAccelParams
  with HasAccelShellParams with HasCacheAccelParams{
    val io = IO(new Bundle {
        val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
        val req = Flipped(Decoupled(new MemReq))
        val resp = Output(Valid(new MemResp))
        val out = Decoupled(new Call(List(32)))
    })

    def cloneType = new test_cache01Main_routineLevel_IO().asInstanceOf[this.type]
}

class test_cache01Main_routineLevel(implicit p: Parameters) extends test_cache01Main_routineLevel_IO {

    //val cache = Module(new ReferenceCache()) // Simple Nasti Cache
    val cacheNode = Module(new DUT())
    val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
    val memCopy = Mem(1014, UInt(32.W)) // Local memory just to keep track of writes to cache for validation


    // Connect the wrapper I/O to the memory model initialization interface so the
    // test bench can write contents at start.
//    memModel.io.nasti <> cache.io.mem
    memModel.io.init.bits.addr := 0.U
    memModel.io.init.bits.data := 0.U
    memModel.io.init.valid := false.B
//    cache.io.cpu.abort := false.B
//    cache.io.cpu.flush := false.B

    // Wire up the cache and modules under test.
    //  val test_cache01 = Module(new test_cache01DF())
    //
    //  //Put an arbiter infront of cache
    //  val CacheArbiter = Module(new MemArbiter(2))
    //
    //  // Connect input signals to cache
    //  CacheArbiter.io.cpu.MemReq(0) <> test_cache01.io.MemReq
    //  test_cache01.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)
    //
    //  //Connect main module to cache arbiter
    //  CacheArbiter.io.cpu.MemReq(1) <> io.req
    //  io.resp <> CacheArbiter.io.cpu.MemResp(1)
    //
    //  //Connect cache to the arbiter
    //  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
    //  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp
    //
    //  //Connect in/out ports
    //  test_cache01.io.in <> io.in
    //  io.out <> test_cache01.io.out

}


class test_cache01Test01_routineLevel(c: DUT)(implicit p: Parameters)  extends PeekPokeTester(c) {


    /*                  sigLoadWays | sigFindInSet | sigAddrToWay | sigPrepMDRead | sigPrepMDWrite | sigAllocate | sigDeallocate | sigWrite |  sigRead  | dataReq
  Probe= 0b0000001011          1             1               0              1                0              0            0               0          0   |   0
  Aloc = 0b0000110100          0             0               1              0                1              1            0               0          0   |   0
  DAloc= 0b0001000000          0             0               0              0                0              0            1               0          0   |   0
  WrInt= 0b0010000000          0             0               0              0                0              0            0               1          0   |   0
  RdInt= 0b0100000000          0             0               0              0                0              0            0               0          1   |   0
  DataRQ=0b1000000000          0             0               0              0                0              0            0               0          0   |   1

     */

//    def MemRead(addr: Int): BigInt = {
//        while (peek(c.io.cpu.req.ready) == 0) {
//            step(1)
//        }
//        poke(c.io.cpu.req.valid, 1.U)
//        poke(c.io.cpu.req.bits.addr, addr.U)
//        poke(c.io.cpu.req.bits.iswrite, 0.U)
//        poke(c.io.cpu.req.bits.tag, 0.U)
//        poke(c.io.cpu.req.bits.mask, 0.U)
//        step(1)
//        while (peek(c.io.cpu.resp.valid) == 0) {
//            step(1)
//        }
//        val result = peek(c.io.cpu.resp.bits.data)
//        result
//    }
//
//    def MemWrite(addr: Int, data: Int): BigInt = {
//        while (peek(c.io.cpu.req.ready) == 0) {
//            step(1)
//        }
//        poke(c.io.cpu.req.valid, 1.U)
//        poke(c.io.cpu.req.bits.addr, addr.U)
//        poke(c.io.cpu.req.bits.data, data.U)
//        poke(c.io.cpu.req.bits.iswrite, 1.U)
//        poke(c.io.cpu.req.bits.tag, 0)
//        poke(c.io.cpu.req.bits.mask, "hF".U((c.xlen/ 8).W))
//        step(1)
//        poke(c.io.cpu.req.valid, 0)
//        1
//    }
//
//    def dumpMemory(path: String) = {
//        //Writing mem states back to the file
//        val pw = new PrintWriter(new File(path))
//        for (i <- 0 until outDataVec.length) {
//            val data = MemRead(outAddrVec(i))
//            pw.write("0X" + outAddrVec(i).toHexString + " -> " + data + "\n")
//        }
//        pw.close
//
//    }
//    def testProbe(addr:UInt) = {
//        while (peek(c.io.cpu.req.ready) == 0) {
//            step(1)
//        }
//        poke(c.io.cpu.req.bits.addr, addr)
//        poke(c.io.cpu.req.bits.command, "b0001011".U)
//        poke(c.io.cpu.req.valid, 1.U)
//        step(1)
//        poke(c.io.cpu.req.valid, 0.U)
//        step(1)
//    }
//    def testAllocate(addr:UInt) = {
//        while (peek(c.io.cpu.req.ready) == 0) {
//            step(1)
//        }
//        poke(c.io.cpu.req.bits.addr, addr)
//        poke(c.io.cpu.req.bits.command, "b0110100".U )
//        poke(c.io.cpu.req.valid, 1.U)
//        step(1)
//        poke(c.io.cpu.req.valid, 0.U)
//        //        step(1)
//    }
//
//    def testDeAllocate(addr:UInt) = {
//        while (peek(c.io.cpu.req.ready) == 0) {
//            step(1)
//        }
//        poke(c.io.cpu.req.valid, 1.U)
//        poke(c.io.cpu.req.bits.addr, addr)
//        poke(c.io.cpu.req.bits.command, "b1000000".U)
//        step(1)
//        poke(c.io.cpu.req.valid, 0.U)
//        //        step(1)
//    }
//
//    def testReadInternal (addr:UInt) ={
//        while (peek(c.io.cpu.req.ready) == 0) {
//            step(1)
//        }
//        poke(c.io.cpu.req.valid, 1.U)
//        poke(c.io.cpu.req.bits.addr, addr)
//        poke(c.io.cpu.req.bits.command, "b100000000".U)
//        step(1)
//        poke(c.io.cpu.req.valid, 0.U)
//        //        step(1)
//    }
//    def testWriteInternal (addr:UInt, data: UInt) ={
//        while (peek(c.io.cpu.req.ready) == 0) {
//            step(1)
//        }
//        poke(c.io.cpu.req.valid, 1.U)
//        poke(c.io.cpu.req.bits.addr, addr)
//        poke(c.io.cpu.req.bits.command, "b010000000".U)
//        poke(c.io.cpu.req.bits.data, data)
//        step(1)
//        poke(c.io.cpu.req.valid, 0.U)
//        //        step(1)
//    }

    //  val data = c.metaMemory.io.outputValue
    //  val addr = Wire(UInt())
    //  addr := 0.U
    //  poke( c.metaMemory.io.address, 0.U)
    //  poke( c.metaMemory.io.isRead, true.B)


//    testProbe(1.U)
//    //    step(2)
//    testAllocate(1.U)
//    testAllocate(5.U)
//    testAllocate(1052676.U)
//    testProbe(1052676.U)
//    testDeAllocate(1052676.U)
//    testProbe(1052676.U)
//    testAllocate(1052676.U)
//    testProbe(1052676.U)
//    testWriteInternal(1052676.U , 100.U)
//    testReadInternal (1052676.U)

    // validation of connections
    while (peek(c.io.instruction.ready) == 0) {
        step(1)
    }
    poke(c.io.instruction.valid,true.B)
    poke(c.io.instruction.bits.event,0.U)
    poke(c.io.instruction.bits.addr,"b10000".U)
    step(1)
    poke(c.io.instruction.valid, false.B)
    step(1)

    while (peek(c.io.instruction.ready) == 0) {
        step(1)
    }
    poke(c.io.instruction.valid,true.B)
    poke(c.io.instruction.bits.event,1.U)
    poke(c.io.instruction.bits.addr,"b10".U)
    step(1)
    poke(c.io.instruction.valid, false.B)
    step(1)

    while (peek(c.io.instruction.ready) == 0) {
    step(1)
    }
        poke(c.io.instruction.valid,true.B)
        poke(c.io.instruction.bits.event,0.U)
        poke(c.io.instruction.bits.addr,"b10000".U)
        step(1)
        poke(c.io.instruction.valid, false.B)
        step(1)

    while (peek(c.io.instruction.ready) == 0) {
        step(1)
    }
    poke(c.io.instruction.valid,true.B)
    poke(c.io.instruction.bits.event,0.U)
    poke(c.io.instruction.bits.addr,"b1000010000".U)
    step(1)
    poke(c.io.instruction.valid, false.B)
    step(1)


    while (peek(c.io.instruction.ready) == 0) {
        step(1)
    }
    poke(c.io.instruction.valid,true.B)
    poke(c.io.instruction.bits.event,0.U)
    poke(c.io.instruction.bits.addr,"b11000010000".U)
    step(1)
    poke(c.io.instruction.valid, false.B)
    step(20)

    //    testAllocate(1.U)
    //    //  step(2)
    //    testAllocate(5.U)
    //    //  step(2)
    //    testAllocate(1052676.U)
    //    //  step(2)
    //    testAllocate(1052677.U)
    //    //  step(2)
    //    testDeAllocate(4100.U)
    //    //  step(2)
    //    testAllocate(4100.U)
    //    //  step(2)
    //    testDeAllocate(4100.U)
    //    //  step(2)
    //    testDeAllocate(1052676.U)
    //    //  step(2)
    //    testReadInternal(100.U)
    //    step(2)
    //    testReadInternal(1052676.U)
    //  val metaMem = c

    val inAddrVec = List.range(0, (4 * 8), 4)
    val inDataVec = List(10, 20, 30, 40, 50, 60, 70, 80)
    val outAddrVec = List.range(0, (4 * 8), 4)
    val outDataVec = List(10, 20, 30, 40, 50, 60, 70, 80)
    /*

      //   Write initial contents to the memory model.
      for (i <- 0 until inDataVec.length) {
        MemWrite(inAddrVec(i), inDataVec(i))
      }
      step(1)
      dumpMemory("init.mem")
      step(1)

      // Initializing the signals
      poke(c.io.in.bits.enable.control, false.B)
      poke(c.io.in.valid, false.B)
      poke(c.io.in.bits.data("field0").data, 0.U)
      poke(c.io.in.bits.data("field0").taskID, 0.U)
      poke(c.io.in.bits.data("field0").predicate, false.B)
      poke(c.io.in.bits.data("field1").data, 0.U)
      poke(c.io.in.bits.data("field1").taskID, 0.U)
      poke(c.io.in.bits.data("field1").predicate, false.B)
      poke(c.io.in.bits.data("field2").data, 0.U)
      poke(c.io.in.bits.data("field2").taskID, 0.U)
      poke(c.io.in.bits.data("field2").predicate, false.B)
      poke(c.io.out.ready, false.B)
      step(1)
      poke(c.io.in.bits.enable.control, true.B)
      poke(c.io.in.valid, true.B)
      poke(c.io.in.bits.data("field0").data, 0) // Array a[] base address
      poke(c.io.in.bits.data("field0").predicate, true.B)
      poke(c.io.in.bits.data("field1").data, 16) // Array b[] base address
      poke(c.io.in.bits.data("field1").predicate, true.B)
      poke(c.io.in.bits.data("field2").data, 1) // flag Read(1), Write(0)
      poke(c.io.in.bits.data("field2").predicate, true.B)
      poke(c.io.out.ready, true.B)
      step(1)
      poke(c.io.in.bits.enable.control, false.B)
      poke(c.io.in.valid, false.B)
      poke(c.io.in.bits.data("field0").data, 0)
      poke(c.io.in.bits.data("field0").predicate, false.B)
      poke(c.io.in.bits.data("field1").data, 0)
      poke(c.io.in.bits.data("field1").predicate, false.B)
      poke(c.io.in.bits.data("field2").data, 0)
      poke(c.io.in.bits.data("field2").predicate, false.B)

      step(1)

      // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
      // early (before the error) which makes debugging very difficult. Check results
      // using if() and fail command.
      var time = 0
      var result = false
      while (time < 500) {
        time += 1
        step(1)
        if (peek(c.io.out.valid) == 1 &&
          peek(c.io.out.bits.data("field0").predicate) == 1
        ) {
          result = true
          val data = peek(c.io.out.bits.data("field0").data)
          if (data != 2000) {
            println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 2000" + Console.RESET)
            fail
          } else {
            println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
          }
        }
      }

      step(100)

      //  Peek into the CopyMem to see if the expected data is written back to the Cache
      var valid_data = true
      for (i <- 0 until outAddrVec.length) {
        val data = MemRead(outAddrVec(i))
        if (data != outDataVec(i).toInt) {
          println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
          fail
          valid_data = false
        }
        else {
          println(Console.BLUE + s"[LOG] MEM[${outAddrVec(i).toInt}] :: $data" + Console.RESET)
        }
      }
      if (valid_data) {
        println(Console.BLUE + "*** Correct data written back." + Console.RESET)
        dumpMemory("final.mem")
      }


      if (!result) {
        println(Console.RED + "*** Timeout." + Console.RESET)
        dumpMemory("final.mem")
        fail
      }

     */

}


class test_cache01Tester1_routineLevel extends FlatSpec with Matchers {
    implicit val p = new WithAccelConfig ++ new WithTestConfig
    it should "Check that test_cache01 works correctly." in {
        // iotester flags:
        // -ll  = log level <Error|Warn|Info|Debug|Trace>
        // -tbn = backend <firrtl|verilator|vcs>
        // -td  = target directory
        // -tts = seed for RNG
        chisel3.iotesters.Driver.execute(
            Array(
                 "-ll", "Error",
                "-tn", "test_cache01Main_routineLevel",
                "-tbn", "verilator",
                "-td", "test_run_dir/test_cache01",
                "-tts", "0001",
                "--generate-vcd-output", "on"),
            () => new DUT()) {
            c => new test_cache01Test01_routineLevel(c)
        } should be(true)
    }
}


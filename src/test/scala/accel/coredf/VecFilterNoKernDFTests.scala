// See LICENSE for license details.
package accel.coredf

import dandelion.accel._
import chisel3._
import chisel3.util._
import chisel3.testers._
import dandelion.interfaces._
import dandelion.junctions._
import chipsalliance.rocketchip.config._
import dandelion.accel.coredf.VecFilterNoKernDFCore
import dandelion.config._
import dandelion.memory.cache.HasCacheAccelParams

class VecFilterNoKernDFTester(accel: => Accelerator)(implicit val p: Parameters) extends BasicTester
  with HasAccelParams
  with HasAccelShellParams
  with HasCacheAccelParams{

  /* NastiMaster block to emulate CPU */
  val hps = Module(new NastiMaster)
  /* Target Design */
  val dut = Module(accel)
  /* Memory model interface */
  val dutMem = Wire(new NastiIO)

  // Connect CPU to DUT
  dut.io.h2f <> hps.io.nasti

  // Connect DUT Cache I/O to a queue for the memory model logic
  dutMem.ar <> Queue(dut.io.f2h.ar, 32)
  dutMem.aw <> Queue(dut.io.f2h.aw, 32)
  dutMem.w <> Queue(dut.io.f2h.w, 32)
  dut.io.f2h.b <> Queue(dutMem.b, 32)
  dut.io.f2h.r <> Queue(dutMem.r, 32)

  val size = log2Ceil(nastiParams.dataBits/ 8).U
  val len = (dataBeats - 1).U

  /* Main Memory */
  val mem = Mem(1 << 20, UInt(nastiParams.dataBits.W))
  val sMemIdle :: sMemWrite :: sMemWrAck :: sMemRead :: Nil = Enum(4)
  val memState = RegInit(sMemIdle)
  val (wCnt, wDone) = Counter(memState === sMemWrite && dutMem.w.valid, dataBeats)
  val (rCnt, rDone) = Counter(memState === sMemRead && dutMem.r.ready, dataBeats)

  dutMem.ar.ready := false.B
  dutMem.aw.ready := false.B
  dutMem.w.ready := false.B
  dutMem.b.valid := memState === sMemWrAck
  dutMem.b.bits := NastiWriteResponseChannel(0.U)
  dutMem.r.valid := memState === sMemRead
  dutMem.r.bits := NastiReadDataChannel(0.U, mem.read((dutMem.ar.bits.addr >> size) + rCnt), rDone)

  switch(memState) {
    is(sMemIdle) {
      when(dutMem.aw.valid) {
        memState := sMemWrite
      }.elsewhen(dutMem.ar.valid) {
        memState := sMemRead
      }
    }
    is(sMemWrite) {
      assert(dutMem.aw.bits.size === size)
      assert(dutMem.aw.bits.len === len)
      when(dutMem.w.valid) {
        mem.read((dutMem.aw.bits.addr >> size) + wCnt) := dutMem.w.bits.data
        printf("[write] mem[%x] <= %x\n", (dutMem.aw.bits.addr >> size) + wCnt, dutMem.w.bits.data)
        dutMem.w.ready := true.B
      }
      when(wDone) {
        dutMem.aw.ready := true.B
        memState := sMemWrAck
      }
    }
    is(sMemWrAck) {
      when(dutMem.b.ready) {
        memState := sMemIdle
      }
    }
    is(sMemRead) {
      when(dutMem.r.ready) {
        printf("[read] mem[%x] => %x\n", (dutMem.ar.bits.addr >> size) + rCnt, dutMem.r.bits.data)
      }
      when(rDone) {
        dutMem.ar.ready := true.B
        memState := sMemIdle
      }
    }
  }

  /* Tests */
  val nopCmd :: rdCmd :: wrCmd :: pollCmd :: Nil = Enum(4) // OpCodes
  val testVec = Seq(
    // Write pointers to image data
    Command(wrCmd, "h_C000_0008".U, "h_0000_0000".U, "h_F".U),
    Command(wrCmd, "h_C000_000C".U, "h_0000_1000".U, "h_F".U),
    Command(wrCmd, "h_C000_0010".U, "h_0000_2000".U, "h_F".U),

    // Write pointers to filter kernel data
    Command(wrCmd, "h_C000_0014".U, "h_0000_4000".U, "h_F".U),
    Command(wrCmd, "h_C000_0018".U, "h_0000_4010".U, "h_F".U),
    Command(wrCmd, "h_C000_001C".U, "h_0000_4020".U, "h_F".U),
    Command(wrCmd, "h_C000_0020".U, "h_0000_4000".U, "h_F".U),
    Command(wrCmd, "h_C000_0024".U, "h_0000_4010".U, "h_F".U),
    Command(wrCmd, "h_C000_0028".U, "h_0000_4020".U, "h_F".U),
    Command(wrCmd, "h_C000_002C".U, "h_0000_4020".U, "h_F".U),
    Command(wrCmd, "h_C000_0030".U, "h_0000_4000".U, "h_F".U),
    Command(wrCmd, "h_C000_0034".U, "h_0000_4010".U, "h_F".U),

    Command(pollCmd, "h_C000_0800".U, "h_0000_0000".U, "h_0000_0004".U), // Poll until done bit set

    // Write pointers to image data
    Command(wrCmd, "h_C000_0008".U, "h_0000_0010".U, "h_F".U),
    Command(wrCmd, "h_C000_000C".U, "h_0000_1010".U, "h_F".U),
    Command(wrCmd, "h_C000_0010".U, "h_0000_2020".U, "h_F".U),

    Command(pollCmd, "h_C000_0800".U, "h_0000_0000".U, "h_0000_0004".U), // Poll until done bit set

    // Write pointers to image data
    Command(wrCmd, "h_C000_0008".U, "h_0000_0020".U, "h_F".U),
    Command(wrCmd, "h_C000_000C".U, "h_0000_1020".U, "h_F".U),
    Command(wrCmd, "h_C000_0010".U, "h_0000_2020".U, "h_F".U),

    Command(pollCmd, "h_C000_0800".U, "h_0000_0000".U, "h_0000_0004".U), // Poll until done bit set
    // Write pointers to image data
    Command(wrCmd, "h_C000_0008".U, "h_0000_0020".U, "h_F".U),
    Command(wrCmd, "h_C000_000C".U, "h_0000_1020".U, "h_F".U),
    Command(wrCmd, "h_C000_0010".U, "h_0000_2020".U, "h_F".U),

    Command(pollCmd, "h_C000_0800".U, "h_0000_0000".U, "h_0000_0004".U), // Poll until done bit set
    Command(nopCmd)
  )

  val sIdle :: sNastiReadReq :: sNastiReadResp :: sNastiWriteReq :: sDone :: Nil = Enum(5)
  val testState = RegInit(sIdle)
  val (testCnt, testDone) = Counter(testState === sDone, testVec.size)
  val req = RegInit(NastiMasterReq())
  val reqValid = RegInit(false.B)
  val pollingRead = RegInit(false.B)

  switch(testState) {
    is(sIdle) {
      switch(VecInit(testVec)(testCnt).opCode) {
        is(rdCmd) {
          req.read := true.B
          req.addr := VecInit(testVec)(testCnt).op0
          req.tag := testCnt % 16.U
          reqValid := true.B
          testState := sNastiReadReq
          pollingRead := false.B
        }
        is(wrCmd) {
          req.read := false.B
          req.addr := VecInit(testVec)(testCnt).op0
          req.data := VecInit(testVec)(testCnt).op1
          req.mask := VecInit(testVec)(testCnt).op2
          req.tag := testCnt % 16.U
          reqValid := true.B
          testState := sNastiWriteReq
        }
        is(pollCmd) {
          req.read := true.B
          req.addr := VecInit(testVec)(testCnt).op0
          req.tag := testCnt % 16.U
          reqValid := true.B
          testState := sNastiReadReq
          pollingRead := true.B
        }
        is(nopCmd) {
          testState := sDone
        }
      }
    }
    is(sNastiReadReq) {
      when(hps.io.req.ready) {
        reqValid := false.B
        testState := sNastiReadResp
      }
    }
    is(sNastiReadResp) {
      when(hps.io.resp.valid && (hps.io.resp.bits.tag === testCnt % 16.U)) {
        val expected = VecInit(testVec)(testCnt).op1
        val mask     = VecInit(testVec)(testCnt).op2
        when((hps.io.resp.bits.data & mask) =/= (expected & mask)) {
          when(!pollingRead) {
            printf("Read fail. Expected: 0x%x. Received: 0x%x.", expected, hps.io.resp.bits.data)
            assert(false.B)
          }.otherwise {
            testState := sIdle
          }
        }.otherwise {
          testState := sDone
          pollingRead := false.B
        }
      }
    }
    is(sNastiWriteReq) {
      reqValid := false.B
      testState := sDone
    }
    is(sDone) {
      testState := sIdle
    }
  }
  hps.io.req.bits := req;
  hps.io.req.valid := reqValid

  when(testDone) {
    stop();
    stop()
  }
}

class VecFilterNoKernDFTests extends org.scalatest.FlatSpec {
  implicit val p = new WithAccelConfig()
  "Accel" should "pass" in {
    assert(TesterDriver execute (() => new VecFilterNoKernDFTester(new Accelerator(12,4,new VecFilterNoKernDFCore(12,4)))))
  }
}

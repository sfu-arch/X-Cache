// See LICENSE for license details.
package dandelion.accel.coredf

import dandelion.accel._
import chisel3._
import chisel3.util._
import chisel3.testers._
import dandelion.interfaces._
import dandelion.junctions._
import chipsalliance.rocketchip.config._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.memory.cache.HasCacheAccelParams

class Command extends Bundle {
  val opCode = UInt()
  val op0 = UInt()
  val op1 = UInt()
  val op2 = UInt()
  val op3 = UInt()
}

object Command {
  def apply(opCode: UInt, op0: UInt = 0.U, op1: UInt = 0.U, op2: UInt = 0.U, op3: UInt = 0.U): Command = {
    val c = Wire(new Command)
    c.opCode := opCode
    c.op0 := op0
    c.op1 := op1
    c.op2 := op2
    c.op3 := op3
    c
  }
}

class AccelTester01(accel: => Accelerator)(implicit val p: Parameters) extends BasicTester
  with HasAccelParams
  with HasAccelShellParams
  with HasCacheAccelParams {

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
  val mem_internal = Mem(1 << 20, UInt(nastiParams.dataBits.W))
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
  dutMem.r.bits := NastiReadDataChannel(0.U, mem_internal((dutMem.ar.bits.addr >> size).asUInt + rCnt), rDone)

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
        mem_internal((dutMem.aw.bits.addr >> size).asUInt() + wCnt) := dutMem.w.bits.data
        printf("[write] mem[%x] <= %x\n", (dutMem.aw.bits.addr >> size).asUInt() + wCnt, dutMem.w.bits.data)
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
    //       Op,       Address,         Data,          Data Mask
    Command(rdCmd, "h_C000_0800".U, "h_0000_0002".U, "h_0000_0003".U), // Check Init/Done status reg
    Command(rdCmd, "h_C000_0804".U, "h_0000_0000".U, "h_FFFF_FFFF".U), // Read 'Unused' space
    Command(rdCmd, "h_C000_0808".U, "h_55AA_0001".U, "h_FFFF_0000".U), // Check Version status reg
    Command(rdCmd, "h_C000_080C".U, "h_0000_0000".U, "h_FFFF_FFFF".U), // Check Core status reg
    Command(rdCmd, "h_C000_0810".U, "h_0000_0000".U, "h_FFFF_FFFF".U), // Check Cache status reg
    // Start incrementing data write test
    Command(wrCmd, "h_C000_0000".U, "h_0000_0002".U, "h_F".U), // Set Init bit
    Command(wrCmd, "h_C000_0008".U, "h_0000_0004".U, "h_F".U), // Ctrl(0) := Data0
    Command(wrCmd, "h_C000_000C".U, "h_0000_0005".U, "h_F".U), // Ctrl(1) := Data1
    Command(rdCmd, "h_C000_000C".U, "h_0000_0005".U, "h_FFFF_FFFF".U), // ReadBack Ctrl(1)
    Command(wrCmd, "h_C000_0000".U, "h_0000_0001".U, "h_F".U), // Set start bit
    Command(pollCmd, "h_C000_0800".U, "h_0000_0001".U, "h_0000_0001".U), // Poll until done bit set
    Command(rdCmd, "h_C000_080C".U, "h_0000_0009".U, "h_0000_000F".U), // stat(1) Check Core status reg
    // Start read back test
    Command(wrCmd, "h_C000_0000".U, "h_0000_0002".U, "h_F".U), // Set Init bit
    Command(wrCmd, "h_C000_0008".U, "h_0000_0003".U, "h_F".U), // Ctrl(0) := Data0
    Command(wrCmd, "h_C000_0000".U, "h_0000_0001".U, "h_F".U), // Set start bit
    Command(pollCmd, "h_C000_0800".U, "h_0000_0001".U, "h_0000_0001".U), // Poll until done bit set
    Command(rdCmd, "h_C000_080C".U, "h_0000_0008".U, "h_0000_000F".U), // Stat(1) Check Core status reg
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
        val mask = VecInit(testVec)(testCnt).op2
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

class AccelTests01 extends org.scalatest.FlatSpec {
  implicit val p = new WithAccelConfig
  "Accel" should "pass" in {
      assert(TesterDriver execute (() => new AccelTester01(new Accelerator(3,3,new TestCore(3,3)))))
    }
}

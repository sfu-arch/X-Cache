// See LICENSE for license details.

package accel

import chisel3._
import chisel3.util._
import chisel3.testers._
import junctions._

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

class AccelTester(accel: => Accelerator)(implicit val p: config.Parameters) extends BasicTester with CacheParams {

  val nop_cmd :: rd_cmd :: wr_cmd :: poll_cmd :: Nil = Enum(4) // OpCodes

  /* NastiMaster block to emulate CPU */
  val hps = Module(new NastiMaster)
  /* Target Design */
  val dut = Module(accel)
  /* Memory model interface */
  val dut_mem = Wire(new NastiIO)

  // Connect CPU to DUT
  dut.io.h2f <> hps.io.nasti

  // Connect DUT Cache I/O to a queue for the memory model logic
  dut_mem.ar <> Queue(dut.io.f2h.ar, 32)
  dut_mem.aw <> Queue(dut.io.f2h.aw, 32)
  dut_mem.w <> Queue(dut.io.f2h.w, 32)
  dut.io.f2h.b <> Queue(dut_mem.b, 32)
  dut.io.f2h.r <> Queue(dut_mem.r, 32)

  val size = log2Ceil(nastiXDataBits / 8).U
  val len = (dataBeats - 1).U

  /* Main Memory */
  val mem = Mem(1 << 20, UInt(nastiXDataBits.W))
  val sMemIdle :: sMemWrite :: sMemWrAck :: sMemRead :: Nil = Enum(4)
  val memState = RegInit(sMemIdle)
  val (wCnt, wDone) = Counter(memState === sMemWrite && dut_mem.w.valid, dataBeats)
  val (rCnt, rDone) = Counter(memState === sMemRead && dut_mem.r.ready, dataBeats)

  dut_mem.ar.ready := false.B
  dut_mem.aw.ready := false.B
  dut_mem.w.ready := false.B
  dut_mem.b.valid := memState === sMemWrAck
  dut_mem.b.bits := NastiWriteResponseChannel(0.U)
  dut_mem.r.valid := memState === sMemRead
  dut_mem.r.bits := NastiReadDataChannel(0.U, mem((dut_mem.ar.bits.addr >> size) + rCnt), rDone)

  switch(memState) {
    is(sMemIdle) {
      when(dut_mem.aw.valid) {
        memState := sMemWrite
      }.elsewhen(dut_mem.ar.valid) {
        memState := sMemRead
      }
    }
    is(sMemWrite) {
      assert(dut_mem.aw.bits.size === size)
      assert(dut_mem.aw.bits.len === len)
      when(dut_mem.w.valid) {
        mem((dut_mem.aw.bits.addr >> size) + wCnt) := dut_mem.w.bits.data
        printf("[write] mem[%x] <= %x\n", (dut_mem.aw.bits.addr >> size) + wCnt, dut_mem.w.bits.data)
        dut_mem.w.ready := true.B
      }
      when(wDone) {
        dut_mem.aw.ready := true.B
        memState := sMemWrAck
      }
    }
    is(sMemWrAck) {
      when(dut_mem.b.ready) {
        memState := sMemIdle
      }
    }
    is(sMemRead) {
      when(dut_mem.r.ready) {
        printf("[read] mem[%x] => %x\n", (dut_mem.ar.bits.addr >> size) + rCnt, dut_mem.r.bits.data)
      }
      when(rDone) {
        dut_mem.ar.ready := true.B
        memState := sMemIdle
      }
    }
  }

  /* Tests */
  val testVec = Seq(
    Command(rd_cmd, "h_C000_0800".U, "h_0000_0002".U), // Read Init/Done status reg
    Command(rd_cmd, "h_C000_0804".U, "h_0000_0000".U), // Read Unused
    Command(rd_cmd, "h_C000_0808".U, "h_55AA_0001".U), // Read Version status reg
    Command(rd_cmd, "h_C000_080C".U, "h_0000_0000".U), // Read Core status reg
    Command(rd_cmd, "h_C000_0810".U, "h_0000_0000".U), // Read Cache status reg
    Command(wr_cmd, "h_C000_0000".U, "h_0000_0002".U, "h_FFFF_FFFF".U), // Set Init bit
    Command(wr_cmd, "h_C000_0008".U, "h_0000_0000".U, "h_FFFF_FFFF".U), // Set Read/Write bit to zero (write)
    Command(wr_cmd, "h_C000_000C".U, "h_2000_0000".U, "h_FFFF_FFFF".U), // Set address
    Command(rd_cmd, "h_C000_000C".U, "h_2000_0000".U), // Read back address
    Command(wr_cmd, "h_C000_0010".U, "h_0000_0400".U, "h_FFFF_FFFF".U), // Set test length
    Command(rd_cmd, "h_C000_0010".U, "h_0000_0400".U), // Read back length
    Command(wr_cmd, "h_C000_0000".U, "h_0000_0001".U, "h_FFFF_FFFF".U), // Set start bit
    Command(poll_cmd, "h_C000_0800".U, "h_0000_0001".U), // Poll until done bit set
    Command(nop_cmd)
  )

  val sIdle :: sNastiReadReq :: sNastiReadResp :: sNastiWriteReq :: sNastiPollReq :: sNastiPollResp :: sDone :: Nil = Enum(7)
  val testState = RegInit(sIdle)
  val (testCnt, testDone) = Counter(testState === sDone, testVec.size)
  val req_r = RegInit(NastiMasterReq())
  val req_valid_r = RegInit(false.B)

  switch(testState) {
    is(sIdle) {
      switch(Vec(testVec)(testCnt).opCode){
        is(rd_cmd) {
          req_r.read := true.B
          req_r.addr := Vec(testVec)(testCnt).op0
          req_r.tag := testCnt % 16.U
          req_valid_r := true.B
          testState := sNastiReadReq
        }
        is(wr_cmd) {
          req_r.read := false.B
          req_r.addr := Vec(testVec)(testCnt).op0
          req_r.data := Vec(testVec)(testCnt).op1
          req_r.mask := Vec(testVec)(testCnt).op2
          req_r.tag := testCnt % 16.U
          req_valid_r := true.B
          testState := sNastiWriteReq
        }
        is(poll_cmd) {
          req_r.read := true.B
          req_r.addr := Vec(testVec)(testCnt).op0
          req_r.tag := testCnt % 16.U
          req_valid_r := true.B
          testState := sNastiPollReq
        }
        is(nop_cmd) {
          testState := sDone
        }
      }
    }
    is(sNastiReadReq) {
      when (hps.io.req.ready) {
        req_valid_r := false.B
        testState := sNastiReadResp
      }
    }
    is(sNastiReadResp) {
      when (hps.io.resp.valid && (hps.io.resp.bits.tag === testCnt % 16.U)) {
        when (hps.io.resp.bits.data =/= Vec(testVec)(testCnt).op1) {
          printf("Read fail. Expected: 0x%x. Received: 0x%x.", Vec(testVec)(testCnt).op1, hps.io.resp.bits.data)
          assert(false.B)
        }
        testState := sDone
      }
    }
    is(sNastiPollReq) {
      when (hps.io.req.ready) {
        req_valid_r := false.B
        testState := sNastiPollResp
      }
    }
    is(sNastiPollResp) {
      when (hps.io.resp.valid && (hps.io.resp.bits.tag === testCnt % 16.U)) {
        when (hps.io.resp.bits.data =/= Vec(testVec)(testCnt).op1) {
          testState := sIdle
        } .otherwise {
          testState := sDone
        }
      }
    }
    is(sNastiWriteReq) {
      req_valid_r := false.B
      testState := sDone
    }
    is(sDone) {
      testState := sIdle
    }
  }
  hps.io.req.bits := req_r;
  hps.io.req.valid := req_valid_r

  when(testDone) {
    stop(); stop()
  }
}

class AccelTests extends org.scalatest.FlatSpec {
  implicit val p = config.Parameters.root((new AcceleratorConfig).toInstance)
  "Accel" should "pass" in {
    assert(TesterDriver execute (() => new AccelTester(new Accelerator)))
  }
}

// See LICENSE for license details.

package accel

import chisel3._
import chisel3.util._
import chisel3.testers._
import junctions._


class AccelTester(cache: => Cache)(implicit val p: config.Parameters) extends BasicTester with CacheParams {
  /* Target Design */
  val dut = Module(cache)
  val dut_mem = Wire(new NastiIO)
  // Throw nasti I/O into a queue (for some reason)
  dut_mem.ar <> Queue(dut.io.nasti.ar, 32)
  dut_mem.aw <> Queue(dut.io.nasti.aw, 32)
  dut_mem.w <> Queue(dut.io.nasti.w, 32)
  dut.io.nasti.b <> Queue(dut_mem.b, 32)
  dut.io.nasti.r <> Queue(dut_mem.r, 32)

  val size  = log2Up(nastiXDataBits / 8).U
  val len   = (dataBeats - 1).U

  /* Main Memory */
  val mem = Mem(1 << 20, UInt(nastiXDataBits.W))
  val sMemIdle :: sMemWrite :: sMemWrAck :: sMemRead :: Nil = Enum(UInt(), 4)
  val memState = RegInit(sMemIdle)
  val (wCnt, wDone) = Counter(memState === sMemWrite && dut_mem.w.valid, dataBeats)
  val (rCnt, rDone) = Counter(memState === sMemRead && dut_mem.r.ready, dataBeats)

  dut_mem.ar.ready  := false.B
  dut_mem.aw.ready  := false.B
  dut_mem.w.ready   := false.B
  dut_mem.b.valid   := memState === sMemWrAck
  dut_mem.b.bits    := NastiWriteResponseChannel(0.U)
  dut_mem.r.valid   := memState === sMemRead
  dut_mem.r.bits    := NastiReadDataChannel(0.U, mem((dut_mem.ar.bits.addr >> size) + rCnt), rDone)

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
      assert(dut_mem.aw.bits.len  === len)
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
  val rnd = new scala.util.Random
  def rand_tag = rnd.nextInt(1 << tlen).U(tlen.W)
  def rand_idx = rnd.nextInt(1 << slen).U(slen.W)
  def rand_off = (rnd.nextInt(1 << blen) & -4).U(blen.W)
  def rand_data = (((0 until (nastiXDataBits / 8)) foldLeft BigInt(0))((r, i) =>
  r | (BigInt(rnd.nextInt(0xff + 1)) << (8 * i)))).U(nastiXDataBits.W)
  def rand_mask = 3.U((xlen/8).W) //(rnd.nextInt((1 << (xlen / 8)) - 1) + 1).U((xlen / 8).W)
  def test(tag: UInt, idx: UInt, off: UInt, mask: UInt = 0.U((xlen / 8).W)) =
    Cat(mask, Cat(Seq.fill(bBits / nastiXDataBits)(rand_data)), tag, idx, off)

    val tags = Vector.fill(3)(rand_tag)
    val idxs = Vector.fill(2)(rand_idx)
    val offs = Vector.fill(6)(rand_off)

    val initAddr = for {
      tag <- tags
      idx <- idxs
      off <- 0 until dataBeats
    } yield Cat(tag, idx, off.U)
    val initData = Seq.fill(initAddr.size)(rand_data)
    val testVec  = Seq(
      test(tags(0), idxs(0), offs(0), rand_mask), // #5: write hit
      test(tags(0), idxs(0), offs(0)), // #0: read miss
      test(tags(0), idxs(0), offs(1)), // #1: read hit
      test(tags(1), idxs(0), offs(0)), // #2: read miss
      test(tags(1), idxs(0), offs(2)), // #3: read hit
      test(tags(1), idxs(0), offs(3)), // #4: read hit
      test(tags(1), idxs(0), offs(4), rand_mask), // #5: write hit
      test(tags(1), idxs(0), offs(4)), // #6: read hit
      test(tags(2), idxs(0), offs(5)), // #7: read miss & write back
      test(tags(0), idxs(1), offs(0), rand_mask), // #8: write miss
      test(tags(0), idxs(1), offs(0)), // #9: read hit
      test(tags(0), idxs(1), offs(1)), // #10: read hit
      test(tags(1), idxs(1), offs(2), rand_mask), // #11: write miss & write back
      test(tags(1), idxs(1), offs(3)), // #12: read hit
      test(tags(2), idxs(1), offs(4)), // #13: read write back
      test(tags(2), idxs(1), offs(5))  // #14: read hit
    )

    val sInit :: sStart :: sWait :: sDone :: Nil = Enum(UInt(), 4)
    val state = RegInit(sInit)
    val timeout = Reg(UInt(32.W))
    val (initCnt, initDone) = Counter(state === sInit, initAddr.size)
    val (testCnt, testDone) = Counter(state === sDone, testVec.size)
    val mask = (Vec(testVec)(testCnt) >> (blen + slen + tlen + bBits))
    val data = (Vec(testVec)(testCnt) >> (blen + slen + tlen))(bBits-1, 0)
    val tag  = (Vec(testVec)(testCnt) >> (blen + slen).U)(tlen - 1, 0)
    val idx  = (Vec(testVec)(testCnt) >> blen.U)(slen - 1, 0)
    val off  = (Vec(testVec)(testCnt))(blen - 1, 0)
    dut.io.cpu.req.bits.addr := Cat(tag, idx, off)
    dut.io.cpu.req.bits.data := data
    dut.io.cpu.req.bits.mask := mask
    dut.io.cpu.req.valid     := state === sWait

    switch(state) {
      is(sInit) {
        mem(Vec(initAddr)(initCnt)) := Vec(initData)(initCnt)
        printf("[init] mem[%x] <= %x\n", Vec(initAddr)(initCnt), Vec(initData)(initCnt))
        when(initDone) {
          state := sStart
        }
      }
      is(sStart) {
//        when(dut.io.cpu.req.ready) {
          timeout := 0.U
          state := sWait
//        }
      }
      is(sWait) {
        timeout := timeout + 1.U
        assert(timeout < 100.U)
        when(dut.io.cpu.resp.valid) {
          state := sDone
        }
      }
      is(sDone) {
        state := sStart
      }
    }

    when(testDone) { stop(); stop() } // from VendingMachine example...
  }

  class AccelTests extends org.scalatest.FlatSpec {
    implicit val p = config.Parameters.root((new AcceleratorConfig).toInstance)
    "Accel" should "pass" in {
      assert(TesterDriver execute (() => new AccelTester(new Cache)))
    }
  }

package dandelion.shell

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces.{ControlBundle, DataBundle}
import dandelion.interfaces.axi._
import dandelion.memory.cache._
import dandelion.accel._

/** Register File.
 *
 * Six 32-bit register file.
 *
 * -------------------------------
 * Register description    | addr
 * -------------------------|-----
 * Control status register | 0x00
 * Cycle counter           | 0x04
 * Constant value          | 0x08
 * Vector length           | 0x0c
 * Input pointer lsb       | 0x10
 * Input pointer msb       | 0x14
 * Output pointer lsb      | 0x18
 * Output pointer msb      | 0x1c
 * -------------------------------
 *
 * ------------------------------
 * Control status register | bit
 * ------------------------------
 * Launch                  | 0
 * Finish                  | 1
 * ------------------------------
 */


/*
+------------------+                          +-----------------+
|                  | f(bits)+--------+        |                 |
|   VMEReadMaster  +------->+Buffers +-------->VMEWriteMaster   |
|                  |        +--------+        |                 |
+------------------+                          +-----------------+

 */

/* Receives a counter value as input. Waits for N cycles and then returns N + const as output */

/**
 *
 * @param p
 */
class DandelionVTAShell(implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val vcr = Module(new VCR)
  val vmem = Module(new VME)

  val buffer = Module(new Queue(vmem.io.vme.rd(0).data.bits.cloneType, 40))

  val sIdle :: sReq :: sBusy :: Nil = Enum(3)
  val Rstate = RegInit(sIdle)
  val Wstate = RegInit(sIdle)

  val cycle_count = new Counter(200)

  when(Rstate =/= sIdle) {
    cycle_count.inc()
  }


  vcr.io.vcr.ecnt(0.U).bits := cycle_count.value

  // Read state machine
  switch(Rstate) {
    is(sIdle) {
      when(vcr.io.vcr.launch) {
        cycle_count.value := 0.U
        Rstate := sReq
      }
    }
    is(sReq) {
      when(vmem.io.vme.rd(0).cmd.fire()) {
        Rstate := sBusy
      }
    }
  }
  // Write state machine
  switch(Wstate) {
    is(sIdle) {
      when(vcr.io.vcr.launch) {
        Wstate := sReq
      }
    }
    is(sReq) {
      when(vmem.io.vme.wr(0).cmd.fire()) {
        Wstate := sBusy
      }
    }
  }

  vmem.io.vme.rd(0).cmd.bits.addr := vcr.io.vcr.ptrs(0)
  vmem.io.vme.rd(0).cmd.bits.len := vcr.io.vcr.vals(1)
  vmem.io.vme.rd(0).cmd.valid := false.B

  vmem.io.vme.wr(0).cmd.bits.addr := vcr.io.vcr.ptrs(2)
  vmem.io.vme.wr(0).cmd.bits.len := vcr.io.vcr.vals(1)
  vmem.io.vme.wr(0).cmd.valid := false.B

  when(Rstate === sReq) {
    vmem.io.vme.rd(0).cmd.valid := true.B
  }

  when(Wstate === sReq) {
    vmem.io.vme.wr(0).cmd.valid := true.B
  }

  // Final
  val last = Wstate === sBusy && vmem.io.vme.wr(0).ack
  vcr.io.vcr.finish := last
  vcr.io.vcr.ecnt(0).valid := last

  when(vmem.io.vme.wr(0).ack) {
    Rstate := sIdle
    Wstate := sIdle
  }


  buffer.io.enq <> vmem.io.vme.rd(0).data
  buffer.io.enq.bits := vmem.io.vme.rd(0).data.bits + vcr.io.vcr.vals(0)
  vmem.io.vme.wr(0).data <> buffer.io.deq

  io.mem <> vmem.io.mem
  io.host <> vcr.io.host

}

class DandelionF1DTAShell(implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new ConfigBusMaster(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val dcr = Module(new DCR)
  val vmem = Module(new VME)

  val buffer = Module(new Queue(vmem.io.vme.rd(0).data.bits.cloneType, 40))

  val sIdle :: sReq :: sBusy :: Nil = Enum(3)
  val Rstate = RegInit(sIdle)
  val Wstate = RegInit(sIdle)

  val cycle_count = new Counter(200)

  when(Rstate =/= sIdle) {
    cycle_count.inc()
  }


  dcr.io.dcr.ecnt(0.U).bits := cycle_count.value

  // Read state machine
  switch(Rstate) {
    is(sIdle) {
      when(dcr.io.dcr.launch) {
        cycle_count.value := 0.U
        Rstate := sReq
      }
    }
    is(sReq) {
      when(vmem.io.vme.rd(0).cmd.fire()) {
        Rstate := sBusy
      }
    }
  }
  // Write state machine
  switch(Wstate) {
    is(sIdle) {
      when(dcr.io.dcr.launch) {
        Wstate := sReq
      }
    }
    is(sReq) {
      when(vmem.io.vme.wr(0).cmd.fire()) {
        Wstate := sBusy
      }
    }
  }

  vmem.io.vme.rd(0).cmd.bits.addr := dcr.io.dcr.ptrs(0)
  vmem.io.vme.rd(0).cmd.bits.len := dcr.io.dcr.vals(1)
  vmem.io.vme.rd(0).cmd.valid := false.B

  vmem.io.vme.wr(0).cmd.bits.addr := dcr.io.dcr.ptrs(1)
  vmem.io.vme.wr(0).cmd.bits.len := dcr.io.dcr.vals(1)
  vmem.io.vme.wr(0).cmd.valid := false.B

  when(Rstate === sReq) {
    vmem.io.vme.rd(0).cmd.valid := true.B
  }

  when(Wstate === sReq) {
    vmem.io.vme.wr(0).cmd.valid := true.B
  }

  // Final
  val last = Wstate === sBusy && vmem.io.vme.wr(0).ack
  dcr.io.dcr.finish := last
  dcr.io.dcr.ecnt(0).valid := last

  when(vmem.io.vme.wr(0).ack) {
    Rstate := sIdle
    Wstate := sIdle
  }


  buffer.io.enq <> vmem.io.vme.rd(0).data
  buffer.io.enq.bits := vmem.io.vme.rd(0).data.bits + dcr.io.dcr.vals(0)
  vmem.io.vme.wr(0).data <> buffer.io.deq

  io.mem <> vmem.io.mem
  io.host <> dcr.io.host

}

/* Receives a counter value as input. Waits for N cycles and then returns N + const as output */
/**
 *
 * @param accelModule Testing module from dandelion-generator
 * @param numPtrs Number of input ptrs for the accelerator
 * @param numVals Number of input values to the accelerator
 * @param numRets Number of return values to the accelerator
 * @param numEvents Number of event values to the accelerator
 * @param numCtrls Number of control registers of the accelerator
 * @param p implicit parameters that contains all the accelerator configuration
 * @tparam T
 */
class DandelionCacheShell[T <: DandelionAccelModule](accelModule: () => T)
                                                    (numPtrs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                                                    (implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val regBits = vcrParams.regBits
  val ptrBits = regBits * 2

  val vcr = Module(new VCR)
  val cache = Module(new SimpleCache(debug = true))

  val accel = Module(accelModule())

  cache.io.cpu.req <> accel.io.MemReq
  accel.io.MemResp <> cache.io.cpu.resp

  val sIdle :: sBusy :: sFlush :: sDone :: Nil = Enum(4)

  val state = RegInit(sIdle)
  val cycles = RegInit(0.U(regBits.W))
  val cnt = RegInit(0.U(regBits.W))
  val last = state === sDone
  val is_busy = state === sBusy

  when(state === sIdle) {
    cycles := 0.U
  }.elsewhen(state =/= sFlush) {
    cycles := cycles + 1.U
  }

  /**
   * Connecting event controls and return values
   * Event zero always contains the cycle count
   */
  vcr.io.vcr.ecnt(0).valid := last
  vcr.io.vcr.ecnt(0).bits := cycles

  if(accel.Returns.size > 0){
    for (i <- 1 to accel.Returns.size) {
      vcr.io.vcr.ecnt(i).bits := accel.io.out.bits.data(s"field${i-1}").data
      vcr.io.vcr.ecnt(i).valid := accel.io.out.valid
    }
  }

  /**
   * @note This part needs to be changes for each function
   */

  val ptrs = Seq.tabulate(numPtrs) { i => RegEnable(next = vcr.io.vcr.ptrs(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }
  val vals = Seq.tabulate(numVals) { i => RegEnable(next = vcr.io.vcr.vals(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }

  /**
   * For now the rule is to first assign the pointers and then assign the vals
   */
  for (i <- 0 until numPtrs) {
    accel.io.in.bits.data(s"field${i}") := DataBundle(ptrs(i))
  }

  for (i <- numPtrs until numPtrs + numVals) {
    accel.io.in.bits.data(s"field${i}") := DataBundle(vals(i - numPtrs))
  }

  accel.io.in.bits.enable := ControlBundle.active()


  accel.io.in.valid := false.B
  accel.io.out.ready := is_busy

  cache.io.cpu.abort := false.B
  cache.io.cpu.flush := false.B

  switch(state) {
    is(sIdle) {
      when(vcr.io.vcr.launch) {
        printf(p"Ptrs: ")
        ptrs.zipWithIndex.foreach(t => printf(p"ptr(${t._2}): ${t._1}, "))
        printf(p"\nVals: ")
        vals.zipWithIndex.foreach(t => printf(p"val(${t._2}): ${t._1}, "))
        printf(p"\n")
        accel.io.in.valid := true.B
        when(accel.io.in.fire) {
          state := sBusy
        }
      }
    }
    is(sBusy) {
      when(accel.io.out.fire) {
        state := sFlush
      }
    }
    is(sFlush) {
      cache.io.cpu.flush := true.B
      when(cache.io.cpu.flush_done) {
        state := sDone
      }
    }
    is(sDone) {
      state := sIdle
    }
  }


  vcr.io.vcr.finish := last

  io.mem <> cache.io.mem
  io.host <> vcr.io.host

}


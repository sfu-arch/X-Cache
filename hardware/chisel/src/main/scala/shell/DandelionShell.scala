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
|   DMEReadMaster  +------->+Buffers +-------->DMEWriteMaster   |
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

  val vcr = Module(new DCR)
  val dmem = Module(new DME)

  val buffer = Module(new Queue(dmem.io.dme.rd(0).data.bits.cloneType, 40))

  val sIdle :: sReq :: sBusy :: Nil = Enum(3)
  val Rstate = RegInit(sIdle)
  val Wstate = RegInit(sIdle)

  val cycle_count = new Counter(2000)

  when(Rstate =/= sIdle) {
    cycle_count.inc()
  }


  vcr.io.dcr.ecnt(0.U).bits := cycle_count.value

  // Read state machine
  switch(Rstate) {
    is(sIdle) {
      when(vcr.io.dcr.launch) {
        cycle_count.value := 0.U
        Rstate := sReq
      }
    }
    is(sReq) {
      when(dmem.io.dme.rd(0).cmd.fire()) {
        Rstate := sBusy
      }
    }
  }
  // Write state machine
  switch(Wstate) {
    is(sIdle) {
      when(vcr.io.dcr.launch) {
        Wstate := sReq
      }
    }
    is(sReq) {
      when(dmem.io.dme.wr(0).cmd.fire()) {
        Wstate := sBusy
      }
    }
  }

  dmem.io.dme.rd(0).cmd.bits.addr := vcr.io.dcr.ptrs(0)
  dmem.io.dme.rd(0).cmd.bits.len := vcr.io.dcr.vals(1)
  dmem.io.dme.rd(0).cmd.valid := false.B

  dmem.io.dme.wr(0).cmd.bits.addr := vcr.io.dcr.ptrs(2)
  dmem.io.dme.wr(0).cmd.bits.len := vcr.io.dcr.vals(1)
  dmem.io.dme.wr(0).cmd.valid := false.B

  when(Rstate === sReq) {
    dmem.io.dme.rd(0).cmd.valid := true.B
  }

  when(Wstate === sReq) {
    dmem.io.dme.wr(0).cmd.valid := true.B
  }

  // Final
  val last = Wstate === sBusy && dmem.io.dme.wr(0).ack
  vcr.io.dcr.finish := last
  vcr.io.dcr.ecnt(0).valid := last

  when(dmem.io.dme.wr(0).ack) {
    Rstate := sIdle
    Wstate := sIdle
  }


  buffer.io.enq <> dmem.io.dme.rd(0).data
  buffer.io.enq.bits := dmem.io.dme.rd(0).data.bits + vcr.io.dcr.vals(0)
  dmem.io.dme.wr(0).data <> buffer.io.deq

  io.mem <> dmem.io.mem
  io.host <> vcr.io.host

}

class DandelionF1DTAShell(implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new ConfigBusMaster(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val dcr = Module(new DCRF1)
  val dmem = Module(new DME)

  val buffer = Module(new Queue(dmem.io.dme.rd(0).data.bits.cloneType, 40))

  val sIdle :: sReq :: sBusy :: Nil = Enum(3)
  val Rstate = RegInit(sIdle)
  val Wstate = RegInit(sIdle)

  val cycle_count = new Counter(2000)

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
      when(dmem.io.dme.rd(0).cmd.fire()) {
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
      when(dmem.io.dme.wr(0).cmd.fire()) {
        Wstate := sBusy
      }
    }
  }

  dmem.io.dme.rd(0).cmd.bits.addr := dcr.io.dcr.ptrs(0)
  dmem.io.dme.rd(0).cmd.bits.len := dcr.io.dcr.vals(1)
  dmem.io.dme.rd(0).cmd.valid := false.B

  dmem.io.dme.wr(0).cmd.bits.addr := dcr.io.dcr.ptrs(1)
  dmem.io.dme.wr(0).cmd.bits.len := dcr.io.dcr.vals(1)
  dmem.io.dme.wr(0).cmd.valid := false.B

  when(Rstate === sReq) {
    dmem.io.dme.rd(0).cmd.valid := true.B
  }

  when(Wstate === sReq) {
    dmem.io.dme.wr(0).cmd.valid := true.B
  }

  // Final
  val last = Wstate === sBusy && dmem.io.dme.wr(0).ack
  dcr.io.dcr.finish := last
  dcr.io.dcr.ecnt(0).valid := last

  when(dmem.io.dme.wr(0).ack) {
    Rstate := sIdle
    Wstate := sIdle
  }


  buffer.io.enq <> dmem.io.dme.rd(0).data
  buffer.io.enq.bits := dmem.io.dme.rd(0).data.bits + dcr.io.dcr.vals(0)
  dmem.io.dme.wr(0).data <> buffer.io.deq

  io.mem <> dmem.io.mem
  io.host <> dcr.io.host

}

/* Receives a counter value as input. Waits for N cycles and then returns N + const as output */
/**
  *
  * @param accelModule Testing module from dandelion-generator
  * @param numPtrs     Number of input Ptrs for the accelerator
  * @param numVals     Number of input Vals for the accelerator
  * @param numRets     Number of return values to the accelerator
  * @param numEvents   Number of event values to the accelerator
  * @param numCtrls    Number of control registers of the accelerator
  * @param p           implicit parameters that contains all the accelerator configuration
  * @tparam T
  */
class DandelionCacheShell[T <: DandelionAccelModule](accelModule: () => T)
                                                    (numPtrs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                                                    (implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val regBits = dcrParams.regBits
  val ptrBits = regBits * 2

  val vcr = Module(new DCR)
  //val cache = Module(new SimpleCache(debug = true))
  val cache = Module(new ReferenceCache())

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
  vcr.io.dcr.ecnt(0).valid := last
  vcr.io.dcr.ecnt(0).bits := cycles

  if (accel.Returns.size > 0) {
    for (i <- 1 to accel.Returns.size) {
      vcr.io.dcr.ecnt(i).bits := accel.io.out.bits.data(s"field${i - 1}").data
      vcr.io.dcr.ecnt(i).valid := accel.io.out.valid
    }
  }

  /**
    * @note This part needs to be changes for each function
    */

  val ptrs = Seq.tabulate(numPtrs) { i => RegEnable(next = vcr.io.dcr.ptrs(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }
  val vals = Seq.tabulate(numVals) { i => RegEnable(next = vcr.io.dcr.vals(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }

  for (i <- numPtrs until numPtrs + numVals) {
    accel.io.in.bits.data(s"field${i}") := DataBundle(ptrs(i))
  }

  accel.io.in.bits.enable := ControlBundle.active()


  accel.io.in.valid := false.B
  accel.io.out.ready := is_busy

  cache.io.cpu.abort := false.B
  cache.io.cpu.flush := state === sFlush

  switch(state) {
    is(sIdle) {
      when(vcr.io.dcr.launch) {
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
      when(cache.io.cpu.flush_done) {
        state := sDone
      }
    }
    is(sDone) {
      state := sIdle
    }
  }


  vcr.io.dcr.finish := last

  io.mem <> cache.io.mem
  io.host <> vcr.io.host

}


/* Receives a counter value as input. Waits for N cycles and then returns N + const as output */
/**
  *
  * @param accelModule Testing module from dandelion-generator
  * @param numPtrs     Number of input Ptrs for the accelerator
  * @param numVals     Number of input Vals for the accelerator
  * @param numRets     Number of return values to the accelerator
  * @param numEvents   Number of event values to the accelerator
  * @param numCtrls    Number of control registers of the accelerator
  * @param p           implicit parameters that contains all the accelerator configuration
  * @tparam T
  */
class DandelionDCRCacheShell[T <: DandelionAccelDCRModule](accelModule: () => T)
                                                          (numPtrs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                                                          (implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val regBits = dcrParams.regBits
  val ptrBits = regBits * 2

  val vcr = Module(new DCR)
  //val cache = Module(new SimpleCache(debug = true))
  val cache = Module(new ReferenceCache())

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
  vcr.io.dcr.ecnt(0).valid := last
  vcr.io.dcr.ecnt(0).bits := cycles

  if (accel.RetsOut.size > 0) {
    for (i <- 1 to accel.RetsOut.size) {
      vcr.io.dcr.ecnt(i).bits := accel.io.out.bits.data(s"field${i - 1}").data
      vcr.io.dcr.ecnt(i).valid := accel.io.out.valid
    }
  }

  /**
    * @note This part needs to be changes for each function
    */

  val ptrs = Seq.tabulate(numPtrs) { i => RegEnable(next = vcr.io.dcr.ptrs(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }
  val vals = Seq.tabulate(numVals) { i => RegEnable(next = vcr.io.dcr.vals(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }

  for (i <- 0 until numPtrs) {
    accel.io.in.bits.dataPtrs(s"field${i}") := DataBundle(ptrs(i))
  }
  for (i <- 0 until numVals) {
    accel.io.in.bits.dataVals(s"field${i}") := DataBundle(vals(i - numPtrs))
  }

  accel.io.in.bits.enable := ControlBundle.active()


  accel.io.in.valid := false.B
  accel.io.out.ready := is_busy

  cache.io.cpu.abort := false.B
  cache.io.cpu.flush := false.B

  switch(state) {
    is(sIdle) {
      when(vcr.io.dcr.launch) {
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


  vcr.io.dcr.finish := last

  io.mem <> cache.io.mem
  io.host <> vcr.io.host

}

/* Receives a counter value as input. Waits for N cycles and then returns N + const as output */
/**
  *
  * @param accelModule Testing module from dandelion-generator
  * @param numPtrs     Number of input Ptrs for the accelerator
  * @param numVals     Number of input Vals for the accelerator
  * @param numRets     Number of return values to the accelerator
  * @param numEvents   Number of event values to the accelerator
  * @param numCtrls    Number of control registers of the accelerator
  * @param p           implicit parameters that contains all the accelerator configuration
  * @tparam T
  */
class DandelionDCRShell[T <: DandelionAccelDCRModule](accelModule: () => T)
                                                     (numPtrs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                                                     (implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val regBits = dcrParams.regBits
  val ptrBits = regBits * 2

  val vcr = Module(new DCR)
  val dme = Module(new DME)
  val cache = Module(new DMECache())

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
  vcr.io.dcr.ecnt(0).valid := last
  vcr.io.dcr.ecnt(0).bits := cycles

  if (accel.RetsOut.size > 0) {
    for (i <- 1 to accel.RetsOut.size) {
      vcr.io.dcr.ecnt(i).bits := accel.io.out.bits.data(s"field${i - 1}").data
      vcr.io.dcr.ecnt(i).valid := accel.io.out.valid
    }
  }

  /**
    * @note This part needs to be changes for each function
    */

  val ptrs = Seq.tabulate(numPtrs) { i => RegEnable(next = vcr.io.dcr.ptrs(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }
  val vals = Seq.tabulate(numVals) { i => RegEnable(next = vcr.io.dcr.vals(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }

  for (i <- 0 until numPtrs) {
    accel.io.in.bits.dataPtrs(s"field${i}") := DataBundle(ptrs(i))
  }
  for (i <- 0 until numVals) {
    accel.io.in.bits.dataVals(s"field${i}") := DataBundle(vals(i - numPtrs))
  }

  accel.io.in.bits.enable := ControlBundle.active()


  accel.io.in.valid := false.B
  accel.io.out.ready := is_busy

  cache.io.cpu.abort := false.B
  cache.io.cpu.flush := state === sFlush

  switch(state) {
    is(sIdle) {
      when(vcr.io.dcr.launch) {
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
      when(cache.io.cpu.flush_done) {
        state := sDone
      }
    }
    is(sDone) {
      state := sIdle
    }
  }


  vcr.io.dcr.finish := last

  //io.mem <> cache.io.mem

  dme.io.dme.rd(0) <> cache.io.mem.rd
  dme.io.dme.wr(0) <> cache.io.mem.wr

  io.mem <> dme.io.mem
  io.host <> vcr.io.host

}


/* Receives a counter value as input. Waits for N cycles and then returns N + const as output */
/**
  *
  * @param accelModule Testing module from dandelion-generator
  * @param numPtrs     Number of input ptrs for the accelerator
  * @param numVals     Number of input values to the accelerator
  * @param numRets     Number of return values to the accelerator
  * @param numEvents   Number of event values to the accelerator
  * @param numCtrls    Number of control registers of the accelerator
  * @param p           implicit parameters that contains all the accelerator configuration
  */
class DandelionDebugShell(accelModule: () => DandelionAccelModule)
                         (debugModule: () =>  DandelionAccelDebugModule)
                         (numPtrs: Int, numDbgs: Int, numVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                         (implicit val p: Parameters) extends Module with HasAccelParams with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val regBits = dcrParams.regBits

  /**
    * If xlen is 64bit, and vcr registers are 32bits then
    * ptrs are lo and hi
    */
  val ptrBits =
    if(xlen / regBits == 2)
      regBits * 2
    else
      regBits


  val dcr = Module(new DCR)
  val dmem = Module(new DME())
  val cache = Module(new DMECache())


  val accel = Module(accelModule())
  val debug_module = Module(debugModule())

  cache.io.cpu.req <> accel.io.MemReq
  accel.io.MemResp <> cache.io.cpu.resp
  cache.io.cpu.abort := false.B

  /**
    * Connecting cache to DME
    */
  dmem.io.dme.rd(0) <> cache.io.mem.rd
  dmem.io.dme.wr(0) <> cache.io.mem.wr

  /**
    * TODO: make enable dependent on logic
    */
  debug_module.io.enableNode.foreach(_ := true.B)

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
  dcr.io.dcr.ecnt(0).valid := last
  dcr.io.dcr.ecnt(0).bits := cycles

  for (i <- 1 to numRets) {
    dcr.io.dcr.ecnt(i).bits := accel.io.out.bits.data(s"field${i - 1}").data
    dcr.io.dcr.ecnt(i).valid := accel.io.out.valid
  }

  /**
    * @note This part needs to be changes for each function
    */

  val ptrs = Seq.tabulate(numPtrs + numDbgs) { i => RegEnable(next = dcr.io.dcr.ptrs(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }
  val vals = Seq.tabulate(numVals) { i => RegEnable(next = dcr.io.dcr.vals(i), init = 0.U(ptrBits.W), enable = (state === sIdle)) }

  /**
    * For now the rule is to first assign the pointers and then assign the vals
    */

  for (i <- 0 until numPtrs) {
    accel.io.in.bits.data(s"field${i}") := DataBundle(ptrs(i))
  }

  for (i <- numPtrs until numPtrs + numVals) {
    accel.io.in.bits.data(s"field${i}") := DataBundle(vals(i - numPtrs))
  }

  /**
    * Connecting debug ptrs
    */
  for( i <- 0 until numDbgs){
    debug_module.io.addrDebug(i) := dcr.io.dcr.ptrs(numPtrs + i)
  }

  accel.io.in.bits.enable := ControlBundle.debug()

  accel.io.in.valid := false.B
  accel.io.out.ready := is_busy

  cache.io.cpu.flush := state === sFlush

  for(i <- 0 until numDbgs){
    dmem.io.dme.wr(i + 1).cmd.bits := debug_module.io.vmeOut(i).cmd.bits
    dmem.io.dme.wr(i + 1).cmd.valid := debug_module.io.vmeOut(i).cmd.valid
    debug_module.io.vmeOut(i).cmd.ready := dmem.io.dme.wr(i + 1).cmd.ready

    dmem.io.dme.wr(i + 1).data <> debug_module.io.vmeOut(i).data
    debug_module.io.vmeOut(i).ack := dmem.io.dme.wr(i + 1).ack
  }

  val dme_ack = RegInit(VecInit(Seq.fill(numDbgs)(false.B)))
  for(i <- 0 until numDbgs){
    when(dmem.io.dme.wr(i + 1).ack) {
      dme_ack(i) := true.B
    }
  }

  def isDMEAck(): Bool ={
    if(numDbgs == 0)
      true.B
    else{
      dme_ack.reduceLeft(_ && _)
    }
  }

  val cache_done = RegInit(false.B)
  when(state === sFlush) {
    when(cache.io.cpu.flush_done) {
      cache_done := true.B
    }
  }

  switch(state) {
    is(sIdle) {
      when(dcr.io.dcr.launch) {
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
      when(isDMEAck && cache_done) {
        state := sDone
      }
    }
    is(sDone) {
      state := sIdle
    }
  }


  dcr.io.dcr.finish := last

  io.mem <> dmem.io.mem
  io.host <> dcr.io.host

}

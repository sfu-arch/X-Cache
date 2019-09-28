/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package dandelion.shell

import chisel3._
import chisel3.util._
import dandelion.accel.Cache
import vta.interface.axi._
//import vta.shell.{VCR, VME, VMEParams}
import vta.shell._
import vta.util.config.{Field, Parameters => VTAParameters}
//import dandelion.accel.{Cache, CacheParams}
import vta.core.{CoreParams => VTACoreParams}
//import dandelion.cache._
import dandelion.config.{CoreParams => DandelionCoreParams, Parameters => DandelionParameters}
import dandelion.generator.test14DF
import dandelion.interfaces.{ControlBundle, DataBundle}

/** Shell parameters. */
//case class ShellParams(
//                           hostParams: AXIParams,
//                           memParams: AXIParams,
//                           vcrParams: VCRSimParams,
//                           vmeParams: VMEParams
//
//                         )
//
//case object ShellKey extends Field[ShellParams]


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
 * @todo define your own ShellKey
 */
class DandelionVTAShell(implicit p: VTAParameters) extends Module {
  val io = IO(new Bundle {
    val host = new AXILiteClient(p(ShellKey).hostParams)
    val mem = new AXIMaster(p(ShellKey).memParams)
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
  vcr.io.host <> io.host

}

/* Receives a counter value as input. Waits for N cycles and then returns N + const as output */
class DandelionCacheShell(implicit pvta: VTAParameters, pdandelion: DandelionParameters) extends Module {
  val io = IO(new Bundle {
    val host = new AXILiteClient(pvta(ShellKey).hostParams)
    val mem = new AXIMaster(pvta(ShellKey).memParams)
  })

  val regBits = pvta(ShellKey).vcrParams.regBits
  val ptrBits = regBits * 2

  val vcr = Module(new VCR)
  //val cache = Module(new Cache(0, pvta(ShellKey).memParams, new CacheParams(pvta(ShellKey).memParams)))
  val cache = Module(new Cache)

  //  cache.io.axi <> DontCare
  //  cache.io.cpu <> DontCare

  val test14 = Module(new test14DF())

  cache.io.cpu.req <> test14.io.MemReq
  test14.io.MemResp <> cache.io.cpu.resp

  val sIdle :: sBusy :: sDone :: Nil = Enum(3)

  val state = RegInit(sIdle)
  val constValue = vcr.io.vcr.vals(0)
  val lengthValue = vcr.io.vcr.vals(1)
  val ptr_a = RegInit(0.U(ptrBits.W))
  val ptr_b = RegInit(0.U(ptrBits.W))
  val cycles = RegInit(0.U(regBits.W))
  val cnt = RegInit(0.U(regBits.W))

  test14.io.in.bits.data("field0") := DataBundle(ptr_a)
  test14.io.in.bits.data("field1") := DataBundle(ptr_b)
  test14.io.in.bits.data("field2") := DataBundle(constValue)
  test14.io.in.bits.data("field3") := DataBundle(lengthValue)
  test14.io.in.bits.enable := ControlBundle.active()

  test14.io.in.valid := vcr.io.vcr.launch
  test14.io.out.ready := state === sBusy

  cache.io.cpu.abort := false.B


  switch(state) {
    is(sIdle) {
      when(vcr.io.vcr.launch) {
        state := sBusy
      }
    }
    is(sBusy) {
    }
  }

  val last = state === sDone


  when(state === sIdle) {
    cycles := 0.U
  }.otherwise {
    cycles := cycles + 1.U
  }

  vcr.io.vcr.ecnt(0).valid := last
  vcr.io.vcr.ecnt(0).bits := cycles

  when(state === sIdle) {
    ptr_a := vcr.io.vcr.ptrs(0)
    ptr_b := vcr.io.vcr.ptrs(1)
  }

  vcr.io.vcr.finish := last


  io.mem <> cache.io.nasti
  vcr.io.host <> io.host

}

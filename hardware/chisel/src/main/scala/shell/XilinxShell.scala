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

package vta.shell

import chisel3._
import chisel3.{RawModule, withClockAndReset}
import chipsalliance.rocketchip.config._
import dandelion.accel.DandelionAccelModule
import dandelion.config._
import dandelion.interfaces.axi._
import dandelion.dpi._
import dandelion.shell.{ConfigBusMaster, DandelionCacheShell, DandelionF1DTAShell, DandelionVTAShell, VTAShell}

/** XilinxShell.
 *
 * This is a wrapper shell mostly used to match Xilinx convention naming,
 * therefore we can pack Dandelion as an IP for IPI based flows.
 */

class DandelionF1Accel[T <: DandelionAccelModule](accelModule: () => T)
                                                 (nPtrs: Int, nVals: Int, numRets: Int, numEvents: Int, numCtrls: Int)
                                                 (implicit val p: Parameters) extends RawModule with HasAccelShellParams {

  val hp = hostParams
  val mp = memParams

  val ap_clk = IO(Input(Clock()))
  val ap_rst_n = IO(Input(Bool()))
  val cl_axi_mstr_bus = IO(new XilinxAXIMaster(mp))
  val axi_mstr_cfg_bus = IO(new ConfigBusMaster(hp))

  val shell = withClockAndReset(clock = ap_clk, reset = ~ap_rst_n) {
    Module(new DandelionF1DTAShell())
  }

  // memory
  cl_axi_mstr_bus.AWVALID := shell.io.mem.aw.valid
  shell.io.mem.aw.ready := cl_axi_mstr_bus.AWREADY
  cl_axi_mstr_bus.AWADDR := shell.io.mem.aw.bits.addr
  cl_axi_mstr_bus.AWID := shell.io.mem.aw.bits.id
  cl_axi_mstr_bus.AWUSER := shell.io.mem.aw.bits.user
  cl_axi_mstr_bus.AWLEN := shell.io.mem.aw.bits.len
  cl_axi_mstr_bus.AWSIZE := shell.io.mem.aw.bits.size
  cl_axi_mstr_bus.AWBURST := shell.io.mem.aw.bits.burst
  cl_axi_mstr_bus.AWLOCK := shell.io.mem.aw.bits.lock
  cl_axi_mstr_bus.AWCACHE := shell.io.mem.aw.bits.cache
  cl_axi_mstr_bus.AWPROT := shell.io.mem.aw.bits.prot
  cl_axi_mstr_bus.AWQOS := shell.io.mem.aw.bits.qos
  cl_axi_mstr_bus.AWREGION := shell.io.mem.aw.bits.region

  cl_axi_mstr_bus.WVALID := shell.io.mem.w.valid
  shell.io.mem.w.ready := cl_axi_mstr_bus.WREADY
  cl_axi_mstr_bus.WDATA := shell.io.mem.w.bits.data
  cl_axi_mstr_bus.WSTRB := shell.io.mem.w.bits.strb
  cl_axi_mstr_bus.WLAST := shell.io.mem.w.bits.last
  cl_axi_mstr_bus.WID := shell.io.mem.w.bits.id
  cl_axi_mstr_bus.WUSER := shell.io.mem.w.bits.user

  shell.io.mem.b.valid := cl_axi_mstr_bus.BVALID
  cl_axi_mstr_bus.BREADY := shell.io.mem.b.valid
  shell.io.mem.b.bits.resp := cl_axi_mstr_bus.BRESP
  shell.io.mem.b.bits.id := cl_axi_mstr_bus.BID
  shell.io.mem.b.bits.user := cl_axi_mstr_bus.BUSER

  cl_axi_mstr_bus.ARVALID := shell.io.mem.ar.valid
  shell.io.mem.ar.ready := cl_axi_mstr_bus.ARREADY
  cl_axi_mstr_bus.ARADDR := shell.io.mem.ar.bits.addr
  cl_axi_mstr_bus.ARID := shell.io.mem.ar.bits.id
  cl_axi_mstr_bus.ARUSER := shell.io.mem.ar.bits.user
  cl_axi_mstr_bus.ARLEN := shell.io.mem.ar.bits.len
  cl_axi_mstr_bus.ARSIZE := shell.io.mem.ar.bits.size
  cl_axi_mstr_bus.ARBURST := shell.io.mem.ar.bits.burst
  cl_axi_mstr_bus.ARLOCK := shell.io.mem.ar.bits.lock
  cl_axi_mstr_bus.ARCACHE := shell.io.mem.ar.bits.cache
  cl_axi_mstr_bus.ARPROT := shell.io.mem.ar.bits.prot
  cl_axi_mstr_bus.ARQOS := shell.io.mem.ar.bits.qos
  cl_axi_mstr_bus.ARREGION := shell.io.mem.ar.bits.region

  shell.io.mem.r.valid := cl_axi_mstr_bus.RVALID
  cl_axi_mstr_bus.RREADY := shell.io.mem.r.ready
  shell.io.mem.r.bits.data := cl_axi_mstr_bus.RDATA
  shell.io.mem.r.bits.resp := cl_axi_mstr_bus.RRESP
  shell.io.mem.r.bits.last := cl_axi_mstr_bus.RLAST
  shell.io.mem.r.bits.id := cl_axi_mstr_bus.RID
  shell.io.mem.r.bits.user := cl_axi_mstr_bus.RUSER

  // host
  shell.io.host.addr := axi_mstr_cfg_bus.addr
  shell.io.host.wdata := axi_mstr_cfg_bus.wdata
  shell.io.host.wr := axi_mstr_cfg_bus.wr
  shell.io.host.rd := axi_mstr_cfg_bus.rd
  axi_mstr_cfg_bus.ack := shell.io.host.ack
  axi_mstr_cfg_bus.rdata := shell.io.host.rdata

  //  shell.io.host.aw.valid := s_axi_control.AWVALID
  //  s_axi_control.AWREADY := shell.io.host.aw.ready
  //  shell.io.host.aw.bits.addr := s_axi_control.AWADDR
  //
  //  shell.io.host.w.valid := s_axi_control.WVALID
  //  s_axi_control.WREADY := shell.io.host.w.ready
  //  shell.io.host.w.bits.data := s_axi_control.WDATA
  //  shell.io.host.w.bits.strb := s_axi_control.WSTRB
  //
  //  s_axi_control.BVALID := shell.io.host.b.valid
  //  shell.io.host.b.ready := s_axi_control.BREADY
  //  s_axi_control.BRESP := shell.io.host.b.bits.resp
  //
  //  shell.io.host.ar.valid := s_axi_control.ARVALID
  //  s_axi_control.ARREADY := shell.io.host.ar.ready
  //  shell.io.host.ar.bits.addr := s_axi_control.ARADDR
  //
  //  s_axi_control.RVALID := shell.io.host.r.valid
  //  shell.io.host.r.ready := s_axi_control.RREADY
  //  s_axi_control.RDATA := shell.io.host.r.bits.data
  //  s_axi_control.RRESP := shell.io.host.r.bits.resp
}


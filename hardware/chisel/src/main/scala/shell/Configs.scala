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
import dandelion.config._
import dandelion.interfaces.axi._


class VCRSimParams(val num_ptrs: Int = 4, val num_vals: Int = 2,
                   val num_event: Int = 1, val num_ctrl: Int = 1) extends VCRParams {
  override val nCtrl = num_ctrl
  override val nECnt = num_event
  override val nVals = num_vals
  override val nPtrs = num_ptrs
  override val regBits = 32
  val ptrBits = regBits
  //val ptrBits = 2 * regBits
}

/** VME parameters.
 *
 * These parameters are used on VME interfaces and modules.
 */
class VMESimParams() extends VMEParams {
  override val nReadClients: Int = 1
  override val nWriteClients: Int = 1
  require(nReadClients > 0,
    s"\n\n[Dandelion] [VMEParams] nReadClients must be larger than 0\n\n")
  require(
    nWriteClients == 1,
    s"\n\n[Dandelion] [VMEParams] nWriteClients must be 1, only one-write-client support atm\n\n")
}

/** SimDefaultConfig. Shell configuration for simulation */
class WithDandelionSimConfig(val num_ptrs: Int = 4, val num_vals: Int = 2,
                      val num_event: Int = 1, val num_ctrl: Int = 1) extends Config((site, here, up) => {
  case ShellKey => ShellParams(
    hostParams = AXIParams(
      addrBits = 16, dataBits = 32, idBits = 13, lenBits = 4),
    memParams = AXIParams(
      addrBits = 32, dataBits = 32, userBits = 5,
      lenBits = 4, // limit to 16 beats, instead of 256 beats in AXI4
      coherent = true),
    vcrParams = new VCRSimParams(num_ptrs, num_vals, num_event, num_ctrl),
    vmeParams = new VMESimParams()
  )
})

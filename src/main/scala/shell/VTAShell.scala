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
import dandelion.interfaces.axi._
import chipsalliance.rocketchip.config._
import dandelion.config.HasAccelShellParams

/** VTAShell.
  *
  * The VTAShell is based on a VME, VCR and core. This creates a complete VTA
  * system that can be used for simulation or real hardware.
  */
class VTAShell(implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })

  val vcr = Module(new VCR)
  val vme = Module(new VME)

  /**
    * @todo: A template to provide core to talk with vcr and vme
    */

  //  val core = Module(new Core)

  //  core.io.vcr <> vcr.io.vcr
  //  vme.io.vme <> core.io.vme
  //
  //  vcr.io.host <> io.host
  //  io.mem <> vme.io.mem
}

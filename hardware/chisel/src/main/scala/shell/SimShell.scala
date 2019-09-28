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
import chisel3.MultiIOModule
import vta.util.config._
import vta.interface.axi._
import vta.shell._
import vta.dpi._


/** SimShell.
  *
  * The simulation shell instantiate the sim, host and memory DPI modules that
  * are connected to the VTAShell. An extra clock, sim_clock, is used to eval
  * the VTASim DPI function when the main simulation clock is on halt state.
  */
class AXISimShell(implicit p: Parameters) extends MultiIOModule {
  val mem = IO(new AXIClient(p(ShellKey).memParams))
  val host = IO(new AXILiteMaster(p(ShellKey).hostParams))
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val mod_sim = Module(new VTASim)
  val mod_host = Module(new VTAHost)
  val mod_mem = Module(new VTAMem)
  mem <> mod_mem.io.axi
  host <> mod_host.io.axi
  mod_sim.reset := reset
  mod_sim.clock := sim_clock
  sim_wait := mod_sim.sim_wait
}

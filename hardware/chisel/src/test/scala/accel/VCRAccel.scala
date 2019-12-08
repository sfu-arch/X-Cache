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

package test

import chisel3._
import chisel3.MultiIOModule
import dandelion.dpi._
import dandelion.shell._
import dandelion.config._
import accel._

/*
            +---------------------------+
            |   AXISimShell (DPI<->AXI) |
            |                           |
            | +-------------+           |
            | |  VTASim     |           |
            | |             |           |
            | +-------------+           |        TestAccel2
            |                           |     +-----------------+
driver_main.cc| +-------------+Master Client    |                 |
       +--->+ |  VTAHost    +-----------------------------------X
            | |             |   AXI-Lite|     || VCR Control RegX
            | +-------------+           |     +-----------------|
            |                           |     |                 |
            | +--------------+          |     |                 |
            | |   VTAMem     ^Client Master   |                 |
            | |              <----------+-----------------------+
            | +--------------+  AXI     |     ||  VMem Interface|
            +---------------------------+     +-----------------+
*/

/** Test. This generates a testbench file for simulation */
class TestAccel2(implicit p: Parameters) extends MultiIOModule {
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val sim_shell = Module(new AXISimShell)
  val vta_shell = Module(new DandelionVTAShell())
  //val vta_shell = Module(new DandelionCacheShell())

  sim_shell.mem <> DontCare
  sim_shell.host <> DontCare
  sim_shell.sim_clock := sim_clock
  sim_wait := sim_shell.sim_wait

  /**
   * @TODO: This is a bug from chisel otherwise, bulk connection should work here
   */
  //  sim_shell.mem <> vta_shell.io.mem
  sim_shell.mem.ar <> vta_shell.io.mem.ar
  sim_shell.mem.aw <> vta_shell.io.mem.aw
  sim_shell.mem.w <> vta_shell.io.mem.w
  vta_shell.io.mem.b <> sim_shell.mem.b
  vta_shell.io.mem.r <> sim_shell.mem.r

  sim_shell.host.b <> vta_shell.io.host.b
  sim_shell.host.r <> vta_shell.io.host.r
  vta_shell.io.host.ar <> sim_shell.host.ar
  vta_shell.io.host.aw <> sim_shell.host.aw
  vta_shell.io.host.w <> sim_shell.host.w
}

object TestAccel2Main extends App {
  
  //These are default values for VCR
  var num_ptrs = 4
  var num_vals = 2
  var num_event = 1
  var num_ctrl = 1
  args.sliding(2, 2).toList.collect {
    case Array("--num-ptrs", argPtrs: String) => num_ptrs = argPtrs.toInt
    case Array("--num-vals", argVals: String) => num_vals = argVals.toInt
    case Array("--num-event", argEvent: String) => num_vals = argEvent.toInt
    case Array("--num-ctrl", argCtrl: String) => num_vals = argCtrl.toInt
  }

  implicit val p: Parameters = 
    Parameters.root((new MiniConfig).toInstance) ++ new DandelionConfig(num_ptrs, num_vals, num_event, num_ctrl)
  chisel3.Driver.execute(args.take(4), () => new TestAccel2)
}


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
import vta.dpi._
import dandelion.shell._
import vta.shell._
import vta.util.config.{Parameters => VTAParameters, Config}
import dandelion.config.{MiniConfig, Parameters => DandelionParameters}
import accel._
import vta.TestDefaultDe10Config

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
class TestAccel2(implicit val pvta: VTAParameters, pdandelion : DandelionParameters ) extends MultiIOModule {
  val sim_clock = IO(Input(Clock()))
  val sim_wait = IO(Output(Bool()))
  val sim_shell = Module(new AXISimShell)
//  val vta_shell = Module(new DandelionVTAShell)
  val vta_shell = Module(new DandelionCacheShell())
  sim_shell.mem <> DontCare
  sim_shell.host <> DontCare
  sim_shell.sim_clock := sim_clock
  sim_wait := sim_shell.sim_wait
  sim_shell.mem <> vta_shell.io.mem
 vta_shell.io.host <> sim_shell.host
}

//class DefaultDe10Config extends Config(new De10Config)
class SimDefaultConfig extends Config(new DandelionConfig)

object TestAccel2Main extends App {
//  implicit val p: Parameters = new SimDefaultConfig
  implicit val pdandelion : DandelionParameters = DandelionParameters.root((new MiniConfig).toInstance)
  implicit val pvta : VTAParameters = new SimDefaultConfig
  chisel3.Driver.execute(args, () => new TestAccel2)
}


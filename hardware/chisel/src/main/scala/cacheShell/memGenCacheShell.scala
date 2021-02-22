package dandelion.shell

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces.{ControlBundle, DataBundle}
import dandelion.interfaces.axi._
import dandelion.memory.cache._
import dandelion.junctions._
import dandelion.accel._
import memGen.memory.cache._
import memGen.shell._




class memGenAccel ( PtrsIn: Seq[Int] = List(),
                               ValsIn: Seq[Int] = List(64, 64, 64),
                               RetsOut: Seq[Int] = List())
                             (implicit p:Parameters) extends  memGenModule (PtrsIn, ValsIn, RetsOut){

  val accel = Module (new memGenTopLevel())

//  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1, 1), valsArgTypes = List()))
//  ArgSplitter.io.In <> io.in

  accel.io.instruction.bits.event := io.in.bits.dataVals("field0").asUInt()
  accel.io.instruction.bits.addr := io.in.bits.dataVals("field1").asUInt()
  accel.io.instruction.bits.data :=io.in.bits.dataVals("field2").asUInt()

//  ArgSplitter.io.Out.enable.bits.debug := false.B
//  ArgSplitter.io.Out.enable.bits.taskID := 0.U
//  ArgSplitter.io.Out.enable.bits.control := accel.io.instruction.ready

  accel.io.instruction.valid := io.in.valid
  io.out <> DontCare

  io.in.ready := accel.io.instruction.ready
  io.mem <> accel.io.mem

}
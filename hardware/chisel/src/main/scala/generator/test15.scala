package dandelion.generator

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import dandelion.accel._
import dandelion.arbiters._
import dandelion.config._
import dandelion.control._
import dandelion.fpu._
import dandelion.interfaces._
import dandelion.junctions._
import dandelion.loop._
import dandelion.memory._
import dandelion.memory.stack._
import dandelion.node._
import muxes._
import org.scalatest._
import regfile._
import util._


class test15DF(PtrsIn: Seq[Int] = List(32, 32, 32), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List(32))
              (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {


  /**
    * Call Interfaces
    */
  val call_0_out_io = IO(Decoupled(new CallDCR(List(32, 32, 32), List())))
  val call_0_in_io = IO(Flipped(Decoupled(new Call(List()))))

  val call_1_out_io = IO(Decoupled(new CallDCR(List(32), List())))
  val call_1_in_io = IO(Flipped(Decoupled(new Call(List(32)))))

  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 2), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 0))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  tail call void @test15_mul(i32* %a, i32* %b, i32* %c), !dbg !23, !UID !24
  val call_0_out = Module(new CallOutDCRNode(ID = 0, NumSuccOps = 0, PtrsTypes = List(32, 32, 32), ValsTypes = List()))

  val call_0_in = Module(new CallInNode(ID = 0, argTypes = List()))

  //  %call = tail call i32 @test15_reduce(i32* %c), !dbg !25, !UID !26
  val call_1_out = Module(new CallOutDCRNode(ID = 1, NumSuccOps = 0, PtrsTypes = List(32), ValsTypes = List()))

  val call_1_in = Module(new CallInNode(ID = 1, argTypes = List(32)))

  //  ret i32 %call, !dbg !27, !UID !28, !BB_UID !29
  val ret_2 = Module(new RetNode2(retTypes = List(32), ID = 2))


  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */


  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */


  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  call_0_in.io.enable <> bb_entry0.io.Out(1)
  call_0_out.io.enable <> bb_entry0.io.Out(0)


  call_1_in.io.enable <> bb_entry0.io.Out(3)
  call_1_out.io.enable <> call_0_in.io.Out.enable

  //call_1_out.io.enable <> bb_entry0.io.Out(2)
  bb_entry0.io.Out(2).ready := true.B


  //ret_2.io.In.enable <> bb_entry0.io.Out(4)
  bb_entry0.io.Out(4).ready := true.B
  ret_2.io.In.enable <> call_1_in.io.Out.enable


  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */


  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */


  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  ret_2.io.In.data("field0") <> call_1_in.io.Out.data("field0")

  call_0_out.io.inPtrs.elements("field0") <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  call_0_out.io.inPtrs.elements("field1") <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  call_0_out.io.inPtrs.elements("field2") <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  call_1_out.io.inPtrs.elements("field0") <> ArgSplitter.io.Out.dataPtrs.elements("field2")(1)


  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_0_out_io <> call_0_out.io.Out(0)

  call_0_in.io.In <> call_0_in_io

//  call_0_in.io.Out.enable.ready := true.B

  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_1_in.io.In <> call_1_in_io

  call_1_out_io <> call_1_out.io.Out(0)

  call_1_in.io.Out.enable.ready := true.B

  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_2.io.Out

}


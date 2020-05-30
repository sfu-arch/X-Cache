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


class test10_addDF(PtrsIn: Seq[Int] = List(), ValsIn: Seq[Int] = List(64, 64), Returns: Seq[Int] = List(64))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(), valsArgTypes = List(1, 1)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 0))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %add = add i32 %b, %a, !dbg !18, !UID !19
  val binaryOp_add0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "add")(sign = false, Debug = false))

  //  ret i32 %add, !dbg !21, !UID !22, !BB_UID !23
  val ret_1 = Module(new RetNode2(retTypes = List(64), ID = 1))



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

  binaryOp_add0.io.enable <> bb_entry0.io.Out(0)


  ret_1.io.In.enable <> bb_entry0.io.Out(1)




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

  ret_1.io.In.data("field0") <> binaryOp_add0.io.Out(0)

  binaryOp_add0.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  binaryOp_add0.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field1")(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_1.io.Out

}


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


class test11_innerDF(PtrsIn: Seq[Int] = List(), ValsIn: Seq[Int] = List(64), Returns: Seq[Int] = List(64))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(), valsArgTypes = List(1)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_for_cond_cleanup0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = add i32 %j, 5, !dbg !20, !UID !21
  val binaryOp_0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "add")(sign = false, Debug = false))

  //  ret i32 %0, !dbg !22, !UID !23, !BB_UID !24
  val ret_1 = Module(new RetNode2(retTypes = List(32), ID = 1))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 5
  val const0 = Module(new ConstFastNode(value = 5, ID = 0))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_for_cond_cleanup0.io.predicateIn(0) <> ArgSplitter.io.Out.enable



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

  const0.io.enable <> bb_for_cond_cleanup0.io.Out(0)

  binaryOp_0.io.enable <> bb_for_cond_cleanup0.io.Out(1)


  ret_1.io.In.enable <> bb_for_cond_cleanup0.io.Out(2)




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

  binaryOp_0.io.RightIO <> const0.io.Out

  ret_1.io.In.data("field0") <> binaryOp_0.io.Out(0)

  binaryOp_0.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_1.io.Out

}


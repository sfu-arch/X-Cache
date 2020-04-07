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


class test02DF(PtrsIn: Seq[Int] = List(), ValsIn: Seq[Int] = List(32, 32), Returns: Seq[Int] = List(32))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(), valsArgTypes = List(2, 1)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 8, BID = 0))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %div.mask = and i32 %a, -2, !dbg !20, !UID !22
  val binaryOp_div_mask0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "and")(sign = false, Debug = false))

  //  %cmp = icmp eq i32 %div.mask, 8, !dbg !20, !UID !23
  val icmp_cmp1 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "eq")(sign = false, Debug = false))

  //  %add = add i32 %b, %a, !dbg !24, !UID !25
  val binaryOp_add2 = Module(new ComputeNode(NumOuts = 1, ID = 2, opCode = "add")(sign = false, Debug = false))

  //  %spec.select = select i1 %cmp, i32 %add, i32 0, !dbg !26, !UID !27
  val select_spec_select3 = Module(new SelectNode(NumOuts = 1, ID = 3)(fast = false))

  //  ret i32 %spec.select, !dbg !28, !UID !29, !BB_UID !30
  val ret_4 = Module(new RetNode2(retTypes = List(32), ID = 4))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 -2
  val const0 = Module(new ConstFastNode(value = -2, ID = 0))

  //i32 8
  val const1 = Module(new ConstFastNode(value = 8, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))



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

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  const2.io.enable <> bb_entry0.io.Out(2)

  binaryOp_div_mask0.io.enable <> bb_entry0.io.Out(3)


  icmp_cmp1.io.enable <> bb_entry0.io.Out(4)


  binaryOp_add2.io.enable <> bb_entry0.io.Out(5)


  select_spec_select3.io.enable <> bb_entry0.io.Out(6)


  ret_4.io.In.enable <> bb_entry0.io.Out(7)




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

  binaryOp_div_mask0.io.RightIO <> const0.io.Out

  icmp_cmp1.io.RightIO <> const1.io.Out

  select_spec_select3.io.InData2 <> const2.io.Out

  icmp_cmp1.io.LeftIO <> binaryOp_div_mask0.io.Out(0)

  select_spec_select3.io.Select <> icmp_cmp1.io.Out(0)

  select_spec_select3.io.InData1 <> binaryOp_add2.io.Out(0)

  ret_4.io.In.data("field0") <> select_spec_select3.io.Out(0)

  binaryOp_div_mask0.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  binaryOp_add2.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  binaryOp_add2.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field1")(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_4.io.Out

}


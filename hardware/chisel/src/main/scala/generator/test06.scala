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


class test06DF(PtrsIn: Seq[Int] = List(), ValsIn: Seq[Int] = List(32, 32), Returns: Seq[Int] = List(32))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(), valsArgTypes = List(3, 2)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1), NumOuts = List(1, 1), NumCarry = List(1, 1, 1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body_preheader1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 2, BID = 3))

  val bb_for_body4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 15, NumPhi = 3, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp15 = icmp sgt i32 %a, 0, !dbg !20, !UID !22
  val icmp_cmp150 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "sgt")(sign = true, Debug = false))

  //  br i1 %cmp15, label %for.body.preheader, label %for.cond.cleanup, !dbg !23, !UID !24, !BB_UID !25
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  br label %for.body, !dbg !26, !UID !29, !BB_UID !30
  val br_2 = Module(new UBranchNode(ID = 2))

  //  br label %for.cond.cleanup, !dbg !31
  val br_3 = Module(new UBranchNode(ID = 3))

  //  %a.addr.0.lcssa = phi i32 [ %a, %entry ], [ %a.addr.1, %for.cond.cleanup.loopexit ], !UID !32
  val phia_addr_0_lcssa4 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 4, Res = true))

  //  %b.addr.0.lcssa = phi i32 [ %b, %entry ], [ %b.addr.1, %for.cond.cleanup.loopexit ], !UID !33
  val phib_addr_0_lcssa5 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 5, Res = true))

  //  %mul = mul nsw i32 %b.addr.0.lcssa, %a.addr.0.lcssa, !dbg !35, !UID !36
  val binaryOp_mul6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign = false, Debug = false))

  //  ret i32 %mul, !dbg !37, !UID !38, !BB_UID !39
  val ret_7 = Module(new RetNode2(retTypes = List(32), ID = 7))

  //  %i.018 = phi i32 [ %inc, %for.body ], [ 0, %for.body.preheader ], !UID !40
  val phii_0188 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 8, Res = false))

  //  %b.addr.017 = phi i32 [ %b.addr.1, %for.body ], [ %b, %for.body.preheader ], !UID !41
  val phib_addr_0179 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 9, Res = false))

  //  %a.addr.016 = phi i32 [ %a.addr.1, %for.body ], [ %a, %for.body.preheader ], !UID !42
  val phia_addr_01610 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 10, Res = false))

  //  %cmp1 = icmp slt i32 %b.addr.017, %a.addr.016, !dbg !26, !UID !43
  val icmp_cmp111 = Module(new ComputeNode(NumOuts = 2, ID = 11, opCode = "slt")(sign = true, Debug = false))

  //  %sub = select i1 %cmp1, i32 %b.addr.017, i32 0, !dbg !44, !UID !45
  val select_sub12 = Module(new SelectNode(NumOuts = 1, ID = 12)(fast = false))

  //  %a.addr.1 = sub nsw i32 %a.addr.016, %sub, !dbg !44, !UID !46
  val binaryOp_a_addr_113 = Module(new ComputeNode(NumOuts = 3, ID = 13, opCode = "sub")(sign = false, Debug = false))

  //  %sub2 = select i1 %cmp1, i32 0, i32 %a.addr.016, !dbg !44, !UID !47
  val select_sub214 = Module(new SelectNode(NumOuts = 1, ID = 14)(fast = false))

  //  %b.addr.1 = sub nsw i32 %b.addr.017, %sub2, !dbg !44, !UID !48
  val binaryOp_b_addr_115 = Module(new ComputeNode(NumOuts = 2, ID = 15, opCode = "sub")(sign = false, Debug = false))

  //  %inc = add nuw nsw i32 %i.018, 1, !dbg !49, !UID !50
  val binaryOp_inc16 = Module(new ComputeNode(NumOuts = 2, ID = 16, opCode = "add")(sign = false, Debug = false))

  //  %cmp = icmp slt i32 %inc, %a.addr.1, !dbg !20, !UID !51
  val icmp_cmp17 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp, label %for.body, label %for.cond.cleanup.loopexit, !dbg !23, !llvm.loop !52, !UID !54, !BB_UID !55
  val br_18 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 18))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_preheader1.io.predicateIn(0) <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_3.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body4.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body4.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.loopBack(0) <> br_18.io.TrueOutput(0)

  Loop_0.io.loopFinish(0) <> br_18.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> ArgSplitter.io.Out.dataVals.elements("field1")(0)

  Loop_0.io.InLiveIn(1) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phib_addr_0179.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  phia_addr_01610.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_a_addr_113.io.Out(0)

  Loop_0.io.InLiveOut(1) <> binaryOp_b_addr_115.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phia_addr_0_lcssa4.io.InData(1) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  phib_addr_0_lcssa5.io.InData(1) <> Loop_0.io.OutLiveOut.elements("field1")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_a_addr_113.io.Out(1)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_inc16.io.Out(0)

  Loop_0.io.CarryDepenIn(2) <> binaryOp_b_addr_115.io.Out(1)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phia_addr_01610.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phii_0188.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phib_addr_0179.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field2")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp150.io.enable <> bb_entry0.io.Out(1)


  br_1.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_for_body_preheader1.io.Out(0)


  br_3.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  phia_addr_0_lcssa4.io.enable <> bb_for_cond_cleanup3.io.Out(0)


  phib_addr_0_lcssa5.io.enable <> bb_for_cond_cleanup3.io.Out(1)


  binaryOp_mul6.io.enable <> bb_for_cond_cleanup3.io.Out(2)


  ret_7.io.In.enable <> bb_for_cond_cleanup3.io.Out(3)


  const1.io.enable <> bb_for_body4.io.Out(0)

  const2.io.enable <> bb_for_body4.io.Out(1)

  const3.io.enable <> bb_for_body4.io.Out(2)

  const4.io.enable <> bb_for_body4.io.Out(3)

  phii_0188.io.enable <> bb_for_body4.io.Out(4)


  phib_addr_0179.io.enable <> bb_for_body4.io.Out(5)


  phia_addr_01610.io.enable <> bb_for_body4.io.Out(6)


  icmp_cmp111.io.enable <> bb_for_body4.io.Out(7)


  select_sub12.io.enable <> bb_for_body4.io.Out(8)


  binaryOp_a_addr_113.io.enable <> bb_for_body4.io.Out(9)


  select_sub214.io.enable <> bb_for_body4.io.Out(10)


  binaryOp_b_addr_115.io.enable <> bb_for_body4.io.Out(11)


  binaryOp_inc16.io.enable <> bb_for_body4.io.Out(12)


  icmp_cmp17.io.enable <> bb_for_body4.io.Out(13)


  br_18.io.enable <> bb_for_body4.io.Out(14)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phia_addr_0_lcssa4.io.Mask <> bb_for_cond_cleanup3.io.MaskBB(0)

  phib_addr_0_lcssa5.io.Mask <> bb_for_cond_cleanup3.io.MaskBB(1)

  phii_0188.io.Mask <> bb_for_body4.io.MaskBB(0)

  phib_addr_0179.io.Mask <> bb_for_body4.io.MaskBB(1)

  phia_addr_01610.io.Mask <> bb_for_body4.io.MaskBB(2)



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

  icmp_cmp150.io.RightIO <> const0.io.Out

  phii_0188.io.InData(1) <> const1.io.Out

  select_sub12.io.InData2 <> const2.io.Out

  select_sub214.io.InData1 <> const3.io.Out

  binaryOp_inc16.io.RightIO <> const4.io.Out

  br_1.io.CmpIO <> icmp_cmp150.io.Out(0)

  binaryOp_mul6.io.RightIO <> phia_addr_0_lcssa4.io.Out(0)

  binaryOp_mul6.io.LeftIO <> phib_addr_0_lcssa5.io.Out(0)

  ret_7.io.In.data("field0") <> binaryOp_mul6.io.Out(0)

  binaryOp_inc16.io.LeftIO <> phii_0188.io.Out(0)

  icmp_cmp111.io.LeftIO <> phib_addr_0179.io.Out(0)

  select_sub12.io.InData1 <> phib_addr_0179.io.Out(1)

  binaryOp_b_addr_115.io.LeftIO <> phib_addr_0179.io.Out(2)

  icmp_cmp111.io.RightIO <> phia_addr_01610.io.Out(0)

  binaryOp_a_addr_113.io.LeftIO <> phia_addr_01610.io.Out(1)

  select_sub214.io.InData2 <> phia_addr_01610.io.Out(2)

  select_sub12.io.Select <> icmp_cmp111.io.Out(0)

  select_sub214.io.Select <> icmp_cmp111.io.Out(1)

  binaryOp_a_addr_113.io.RightIO <> select_sub12.io.Out(0)

  icmp_cmp17.io.RightIO <> binaryOp_a_addr_113.io.Out(2)

  binaryOp_b_addr_115.io.RightIO <> select_sub214.io.Out(0)

  icmp_cmp17.io.LeftIO <> binaryOp_inc16.io.Out(1)

  br_18.io.CmpIO <> icmp_cmp17.io.Out(0)

  icmp_cmp150.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  phia_addr_0_lcssa4.io.InData(0) <> ArgSplitter.io.Out.dataVals.elements("field0")(2)

  phib_addr_0_lcssa5.io.InData(0) <> ArgSplitter.io.Out.dataVals.elements("field1")(1)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_7.io.Out

}


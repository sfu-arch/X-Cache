package dandelion.generator

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.control._
import dandelion.junctions._
import dandelion.loop._
import dandelion.node._


class test04DF(PtrsIn: Seq[Int] = List(), ValsIn: Seq[Int] = List(32, 32, 32), Returns: Seq[Int] = List(32))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(), valsArgTypes = List(2, 1, 2)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(2, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0, Debug = false))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body_preheader1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 9, NumPhi = 2, BID = 2))

  val bb_for_end_loopexit3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_for_end4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp5 = icmp sgt i32 %n, 0, !dbg !24, !UID !27
  val icmp_cmp50 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "sgt")(sign = true, Debug = false))

  //  br i1 %cmp5, label %for.body.preheader, label %for.end, !dbg !28, !UID !29, !BB_UID !30
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  br label %for.body, !dbg !31, !UID !32, !BB_UID !33
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %i.07 = phi i32 [ %inc, %for.body ], [ 0, %for.body.preheader ], !UID !34
  val phii_073 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 3, Res = false))

  //  %sum.06 = phi i32 [ %mul, %for.body ], [ %a, %for.body.preheader ], !UID !35
  /**
   * This is an injected bug for fourth iteration
   * Correct vals are: 5, 30, 105, 330, 1005
   */
  val phisum_064 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 4, Res = false, Debug = true,
    GuardVals = List(5, 30, 108, 330, 1005)))

  //  %add = add i32 %sum.06, %a, !dbg !31, !UID !36
  val binaryOp_add5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false, Debug = false))

  //  %mul = mul i32 %add, %b, !dbg !37, !UID !38
  val binaryOp_mul6 = Module(new ComputeNode(NumOuts = 2, ID = 6, opCode = "mul")(sign = false, Debug = false))

  //  %inc = add nuw nsw i32 %i.07, 1, !dbg !39, !UID !40
  val binaryOp_inc7 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, %n, !dbg !24, !UID !41
  val icmp_exitcond8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.end.loopexit, label %for.body, !dbg !28, !llvm.loop !42, !UID !44, !BB_UID !45
  val br_9 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 9))

  //  br label %for.end, !dbg !46
  val br_10 = Module(new UBranchNode(ID = 10))

  //  %sum.0.lcssa = phi i32 [ %a, %entry ], [ %mul, %for.end.loopexit ], !UID !47
  val phisum_0_lcssa11 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 11, Res = true))

  //  ret i32 %sum.0.lcssa, !dbg !46, !UID !48, !BB_UID !49
  val ret_12 = Module(new RetNode2(retTypes = List(32), ID = 12, NumBores = 1, Debug = true))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_preheader1.io.predicateIn(0) <> br_1.io.TrueOutput(0)

  bb_for_end4.io.predicateIn(1) <> br_1.io.FalseOutput(0)

  bb_for_end4.io.predicateIn(0) <> br_10.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_body2.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_end_loopexit3.io.predicateIn(0) <> Loop_0.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.loopBack(0) <> br_9.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_9.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> ArgSplitter.io.Out.dataVals.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> ArgSplitter.io.Out.dataVals.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phisum_064.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add5.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(1)

  binaryOp_mul6.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  icmp_exitcond8.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_mul6.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phisum_0_lcssa11.io.InData(1) <> Loop_0.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc7.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_mul6.io.Out(1)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phii_073.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phisum_064.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp50.io.enable <> bb_entry0.io.Out(1)


  br_1.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_for_body_preheader1.io.Out(0)


  const1.io.enable <> bb_for_body2.io.Out(0)

  const2.io.enable <> bb_for_body2.io.Out(1)

  phii_073.io.enable <> bb_for_body2.io.Out(2)


  phisum_064.io.enable <> bb_for_body2.io.Out(3)


  binaryOp_add5.io.enable <> bb_for_body2.io.Out(4)


  binaryOp_mul6.io.enable <> bb_for_body2.io.Out(5)


  binaryOp_inc7.io.enable <> bb_for_body2.io.Out(6)


  icmp_exitcond8.io.enable <> bb_for_body2.io.Out(7)


  br_9.io.enable <> bb_for_body2.io.Out(8)


  br_10.io.enable <> bb_for_end_loopexit3.io.Out(0)


  phisum_0_lcssa11.io.enable <> bb_for_end4.io.Out(0)


  ret_12.io.In.enable <> bb_for_end4.io.Out(1)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phii_073.io.Mask <> bb_for_body2.io.MaskBB(0)

  phisum_064.io.Mask <> bb_for_body2.io.MaskBB(1)

  phisum_0_lcssa11.io.Mask <> bb_for_end4.io.MaskBB(0)



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

  icmp_cmp50.io.RightIO <> const0.io.Out

  phii_073.io.InData(1) <> const1.io.Out

  binaryOp_inc7.io.RightIO <> const2.io.Out

  br_1.io.CmpIO <> icmp_cmp50.io.Out(0)

  binaryOp_inc7.io.LeftIO <> phii_073.io.Out(0)

  binaryOp_add5.io.LeftIO <> phisum_064.io.Out(0)

  binaryOp_mul6.io.LeftIO <> binaryOp_add5.io.Out(0)

  icmp_exitcond8.io.LeftIO <> binaryOp_inc7.io.Out(1)

  br_9.io.CmpIO <> icmp_exitcond8.io.Out(0)

  ret_12.io.In.data("field0") <> phisum_0_lcssa11.io.Out(0)

  phisum_0_lcssa11.io.InData(0) <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  icmp_cmp50.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(1)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_12.io.Out

}


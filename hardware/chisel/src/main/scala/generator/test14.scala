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


class test14DF(PtrsIn: Seq[Int] = List(32), ValsIn: Seq[Int] = List(32), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 0, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp

  val mem_ctrl = Module(new CacheMemoryEngine(ID = 1, NumRead = 1, NumWrite = 1))
  val memory_0 = Module(new ScratchPadMemory(24))

  memory_0.io.req <> mem_ctrl.io.cache.MemReq
  mem_ctrl.io.cache.MemResp <> memory_0.io.resp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1), valsArgTypes = List(1)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 11, NumPhi = 1, BID = 1))

  val bb_for_body5_preheader2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup43 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_for_body54 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 13, NumPhi = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %temp = alloca [24 x i32], align 16, !UID !25
  //val alloca_temp0 = Module(new AllocaNode(NumOuts=3, ID = 0, RouteID=0))

  //  %0 = bitcast [24 x i32]* %temp to i8*, !dbg !28, !UID !29
  //val bitcast_1 = Module(new BitCastNode(NumOuts = 0, ID = 1))

  //  br label %for.body, !dbg !32, !UID !33, !BB_UID !34
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %indvars.iv23 = phi i64 [ 0, %entry ], [ %indvars.iv.next24, %for.body ], !UID !35
  val phiindvars_iv233 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 3, Res = true))

  //  %arrayidx = getelementptr inbounds [24 x i32], [24 x i32]* %temp, i64 0, i64 %indvars.iv23, !dbg !36, !UID !38
  val Gep_arrayidx4 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 4)(ElementSize = 8, ArraySize = List(96)))

  //  %1 = trunc i64 %indvars.iv23 to i32, !dbg !39, !UID !40
  val trunc5 = Module(new TruncNode(NumOuts = 1))

  //  store i32 %1, i32* %arrayidx, align 4, !dbg !39, !tbaa !41, !UID !45
  val st_6 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 6, RouteID = 1))

  //  %indvars.iv.next24 = add nuw nsw i64 %indvars.iv23, 1, !dbg !46, !UID !47
  val binaryOp_indvars_iv_next247 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %exitcond25 = icmp eq i64 %indvars.iv.next24, 24, !dbg !48, !UID !49
  val icmp_exitcond258 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond25, label %for.body5.preheader, label %for.body, !dbg !32, !llvm.loop !50, !UID !52, !BB_UID !53
  val br_9 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 9))

  //  br label %for.body5, !dbg !54, !UID !56, !BB_UID !57
  val br_10 = Module(new UBranchNode(ID = 10))

  //  ret void, !dbg !58, !UID !59, !BB_UID !60
  val ret_11 = Module(new RetNode2(retTypes = List(), ID = 11))

  //  %indvars.iv = phi i64 [ %indvars.iv.next, %for.body5 ], [ 0, %for.body5.preheader ], !UID !61
  val phiindvars_iv12 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 12, Res = false))

  //  %arrayidx7 = getelementptr inbounds [24 x i32], [24 x i32]* %temp, i64 0, i64 %indvars.iv, !dbg !54, !UID !63
  val Gep_arrayidx713 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 13)(ElementSize = 8, ArraySize = List(96)))

  //  %2 = load i32, i32* %arrayidx7, align 4, !dbg !54, !tbaa !41, !UID !64
  val ld_14 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 14, RouteID = 0))

  //  %mul = mul i32 %2, %n, !dbg !65, !UID !66
  val binaryOp_mul15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign = false, Debug = false))

  //  %arrayidx9 = getelementptr inbounds i32, i32* %a, i64 %indvars.iv, !dbg !67, !UID !68
  val Gep_arrayidx916 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 16)(ElementSize = 8, ArraySize = List()))

  //  store i32 %mul, i32* %arrayidx9, align 4, !dbg !69, !tbaa !41, !UID !70
  val st_17 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 17, RouteID = 0))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !71, !UID !72
  val binaryOp_indvars_iv_next18 = Module(new ComputeNode(NumOuts = 2, ID = 18, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 24, !dbg !73, !UID !74
  val icmp_exitcond19 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup4, label %for.body5, !dbg !75, !llvm.loop !76, !UID !78, !BB_UID !79
  val br_20 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 20))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i64 24
  val const3 = Module(new ConstFastNode(value = 24, ID = 3))

  //i64 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i64 0
  val const5 = Module(new ConstFastNode(value = 0, ID = 5))

  //i64 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))

  //i64 24
  val const7 = Module(new ConstFastNode(value = 24, ID = 7))


  //i64 24
  val alloca_const_0 = Module(new ConstFastNode(value = 0, ID = 8))

  //i64 24
  val alloca_const_1 = Module(new ConstFastNode(value = 0, ID = 9))


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_body1.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body1.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body5_preheader2.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_cond_cleanup43.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body54.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body54.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_10.io.Out(0)

  Loop_0.io.loopBack(0) <> br_20.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_20.io.TrueOutput(0)

  Loop_1.io.enable <> br_2.io.Out(0)

  Loop_1.io.loopBack(0) <> br_9.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_9.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

//  Loop_0.io.InLiveIn(0) <> alloca_temp0.io.Out(0)
  Loop_0.io.InLiveIn(0) <> alloca_const_0.io.Out

  Loop_0.io.InLiveIn(1) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  Loop_0.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

//  Loop_1.io.InLiveIn(0) <> alloca_temp0.io.Out(1)
  Loop_1.io.InLiveIn(0) <> alloca_const_1.io.Out



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx713.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_mul15.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx916.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx4.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next18.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next247.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv12.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv233.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  alloca_const_0.io.enable <> bb_entry0.io.Out(0)


  alloca_const_1.io.enable <> bb_entry0.io.Out(1)


  br_2.io.enable <> bb_entry0.io.Out(2)


  const0.io.enable <> bb_for_body1.io.Out(0)

  const1.io.enable <> bb_for_body1.io.Out(1)

  const2.io.enable <> bb_for_body1.io.Out(2)

  const3.io.enable <> bb_for_body1.io.Out(3)

  phiindvars_iv233.io.enable <> bb_for_body1.io.Out(4)


  Gep_arrayidx4.io.enable <> bb_for_body1.io.Out(5)


  trunc5.io.enable <> bb_for_body1.io.Out(6)


  st_6.io.enable <> bb_for_body1.io.Out(7)


  binaryOp_indvars_iv_next247.io.enable <> bb_for_body1.io.Out(8)


  icmp_exitcond258.io.enable <> bb_for_body1.io.Out(9)


  br_9.io.enable <> bb_for_body1.io.Out(10)


  br_10.io.enable <> bb_for_body5_preheader2.io.Out(0)


  ret_11.io.In.enable <> bb_for_cond_cleanup43.io.Out(0)


  const4.io.enable <> bb_for_body54.io.Out(0)

  const5.io.enable <> bb_for_body54.io.Out(1)

  const6.io.enable <> bb_for_body54.io.Out(2)

  const7.io.enable <> bb_for_body54.io.Out(3)

  phiindvars_iv12.io.enable <> bb_for_body54.io.Out(4)


  Gep_arrayidx713.io.enable <> bb_for_body54.io.Out(5)


  ld_14.io.enable <> bb_for_body54.io.Out(6)


  binaryOp_mul15.io.enable <> bb_for_body54.io.Out(7)


  Gep_arrayidx916.io.enable <> bb_for_body54.io.Out(8)


  st_17.io.enable <> bb_for_body54.io.Out(9)


  binaryOp_indvars_iv_next18.io.enable <> bb_for_body54.io.Out(10)


  icmp_exitcond19.io.enable <> bb_for_body54.io.Out(11)


  br_20.io.enable <> bb_for_body54.io.Out(12)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv233.io.Mask <> bb_for_body1.io.MaskBB(0)

  phiindvars_iv12.io.Mask <> bb_for_body54.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */

//  alloca_temp0.io.allocaInputIO.bits.size      := 1.U
//  alloca_temp0.io.allocaInputIO.bits.numByte   := 96.U
//  alloca_temp0.io.allocaInputIO.bits.predicate := true.B
//  alloca_temp0.io.allocaInputIO.bits.valid     := true.B
//  alloca_temp0.io.allocaInputIO.valid          := true.B





  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

//  StackPointer.io.InData(0) <> alloca_temp0.io.allocaReqIO
//
//  alloca_temp0.io.allocaRespIO <> StackPointer.io.OutData(0)

  mem_ctrl.io.wr.mem(0).MemReq <> st_6.io.MemReq

  st_6.io.MemResp <> mem_ctrl.io.wr.mem(0).MemResp

  mem_ctrl.io.rd.mem(0).MemReq <> ld_14.io.MemReq

  ld_14.io.MemResp <> mem_ctrl.io.rd.mem(0).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_17.io.MemReq

  st_17.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv233.io.InData(0) <> const0.io.Out

  Gep_arrayidx4.io.idx(0) <> const1.io.Out

  binaryOp_indvars_iv_next247.io.RightIO <> const2.io.Out

  icmp_exitcond258.io.RightIO <> const3.io.Out

  phiindvars_iv12.io.InData(1) <> const4.io.Out

  Gep_arrayidx713.io.idx(0) <> const5.io.Out

  binaryOp_indvars_iv_next18.io.RightIO <> const6.io.Out

  icmp_exitcond19.io.RightIO <> const7.io.Out

//  bitcast_1.io.Input <> alloca_temp0.io.Out(2)

  Gep_arrayidx4.io.idx(1) <> phiindvars_iv233.io.Out(0)

  trunc5.io.Input <> phiindvars_iv233.io.Out(1)

  binaryOp_indvars_iv_next247.io.LeftIO <> phiindvars_iv233.io.Out(2)

  st_6.io.GepAddr <> Gep_arrayidx4.io.Out(0)

  st_6.io.inData <> trunc5.io.Out(0)

  icmp_exitcond258.io.LeftIO <> binaryOp_indvars_iv_next247.io.Out(1)

  br_9.io.CmpIO <> icmp_exitcond258.io.Out(0)

  Gep_arrayidx713.io.idx(1) <> phiindvars_iv12.io.Out(0)

  Gep_arrayidx916.io.idx(0) <> phiindvars_iv12.io.Out(1)

  binaryOp_indvars_iv_next18.io.LeftIO <> phiindvars_iv12.io.Out(2)

  ld_14.io.GepAddr <> Gep_arrayidx713.io.Out(0)

  binaryOp_mul15.io.LeftIO <> ld_14.io.Out(0)

  st_17.io.inData <> binaryOp_mul15.io.Out(0)

  st_17.io.GepAddr <> Gep_arrayidx916.io.Out(0)

  icmp_exitcond19.io.LeftIO <> binaryOp_indvars_iv_next18.io.Out(1)

  br_20.io.CmpIO <> icmp_exitcond19.io.Out(0)

  st_6.io.Out(0).ready := true.B

  st_17.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_9.io.PredOp(0) <> st_6.io.SuccOp(0)

  br_20.io.PredOp(0) <> st_17.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_11.io.Out

}


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


class test14DF(PtrsIn: Seq[Int] = List(64), ValsIn: Seq[Int] = List(64), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){

  /**
    * Memory Interfaces
    */
  val alloca_temp0_mem_req = IO(Decoupled(new MemReq))
  val alloca_temp0_mem_resp = IO(Flipped(Valid(new MemResp)))



  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 0, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  //buffer_memories_0
  val buffer_memories_0 = Module(new CacheMemoryEngine(ID = 0, NumRead = 1, NumWrite = 1))

  alloca_temp0_mem_req <> buffer_memories_0.io.cache.MemReq
  buffer_memories_0.io.cache.MemResp <> alloca_temp0_mem_resp

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

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 0))

  val bb_for_body1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 11, NumPhi = 1, BID = 1))

  val bb_for_body5_preheader2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup43 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_for_body54 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 13, NumPhi = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %temp = alloca [24 x i32], align 16, !UID !25
  val alloca_temp0 = Module(new AllocaConstNode(NumOuts=2, ID = 0))

  //  br label %for.body, !dbg !32, !UID !33, !BB_UID !34
  val br_1 = Module(new UBranchNode(ID = 1))

  //  %indvars.iv23 = phi i64 [ 0, %entry ], [ %indvars.iv.next24, %for.body ], !UID !35
  val phiindvars_iv232 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 2, Res = true))

  //  %arrayidx = getelementptr inbounds [24 x i32], [24 x i32]* %temp, i64 0, i64 %indvars.iv23, !dbg !36, !UID !38
  val Gep_arrayidx3 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 3)(ElementSize = 8, ArraySize = List(96)))

  //  %1 = trunc i64 %indvars.iv23 to i32, !dbg !39, !UID !40
  val trunc4 = Module(new TruncNode(NumOuts = 1))

  //  store i32 %1, i32* %arrayidx, align 4, !dbg !39, !tbaa !41, !UID !45
  val st_5 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 5, RouteID = 1))

  //  %indvars.iv.next24 = add nuw nsw i64 %indvars.iv23, 1, !dbg !46, !UID !47
  val binaryOp_indvars_iv_next246 = Module(new ComputeNode(NumOuts = 2, ID = 6, opCode = "add")(sign = false, Debug = false))

  //  %exitcond25 = icmp eq i64 %indvars.iv.next24, 24, !dbg !48, !UID !49
  val icmp_exitcond257 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond25, label %for.body5.preheader, label %for.body, !dbg !32, !llvm.loop !50, !UID !52, !BB_UID !53
  val br_8 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 8))

  //  br label %for.body5, !dbg !54, !UID !56, !BB_UID !57
  val br_9 = Module(new UBranchNode(ID = 9))

  //  ret void, !dbg !58, !UID !59, !BB_UID !60
  val ret_10 = Module(new RetNode2(retTypes = List(), ID = 10))

  //  %indvars.iv = phi i64 [ %indvars.iv.next, %for.body5 ], [ 0, %for.body5.preheader ], !UID !61
  val phiindvars_iv11 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 11, Res = false))

  //  %arrayidx7 = getelementptr inbounds [24 x i32], [24 x i32]* %temp, i64 0, i64 %indvars.iv, !dbg !54, !UID !63
  val Gep_arrayidx712 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 12)(ElementSize = 8, ArraySize = List(96)))

  //  %2 = load i32, i32* %arrayidx7, align 4, !dbg !54, !tbaa !41, !UID !64
  val ld_13 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 13, RouteID = 0))

  //  %mul = mul i32 %2, %n, !dbg !65, !UID !66
  val binaryOp_mul14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "mul")(sign = false, Debug = false))

  //  %arrayidx9 = getelementptr inbounds i32, i32* %a, i64 %indvars.iv, !dbg !67, !UID !68
  val Gep_arrayidx915 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 15)(ElementSize = 8, ArraySize = List()))

  //  store i32 %mul, i32* %arrayidx9, align 4, !dbg !69, !tbaa !41, !UID !70
  val st_16 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 16, RouteID = 0))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !71, !UID !72
  val binaryOp_indvars_iv_next17 = Module(new ComputeNode(NumOuts = 2, ID = 17, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 24, !dbg !73, !UID !74
  val icmp_exitcond18 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup4, label %for.body5, !dbg !75, !llvm.loop !76, !UID !78, !BB_UID !79
  val br_19 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 19))



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

  Loop_0.io.enable <> br_9.io.Out(0)

  Loop_0.io.loopBack(0) <> br_19.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_19.io.TrueOutput(0)

  Loop_1.io.enable <> br_1.io.Out(0)

  Loop_1.io.loopBack(0) <> br_8.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_8.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> alloca_temp0.io.Out(0)

  Loop_0.io.InLiveIn(1) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  Loop_0.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(0) <> alloca_temp0.io.Out(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx712.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_mul14.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx915.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx3.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next17.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next246.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv11.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv232.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  alloca_temp0.io.enable <> bb_entry0.io.Out(0)


  br_1.io.enable <> bb_entry0.io.Out(1)


  const0.io.enable <> bb_for_body1.io.Out(0)

  const1.io.enable <> bb_for_body1.io.Out(1)

  const2.io.enable <> bb_for_body1.io.Out(2)

  const3.io.enable <> bb_for_body1.io.Out(3)

  phiindvars_iv232.io.enable <> bb_for_body1.io.Out(4)


  Gep_arrayidx3.io.enable <> bb_for_body1.io.Out(5)


  trunc4.io.enable <> bb_for_body1.io.Out(6)


  st_5.io.enable <> bb_for_body1.io.Out(7)


  binaryOp_indvars_iv_next246.io.enable <> bb_for_body1.io.Out(8)


  icmp_exitcond257.io.enable <> bb_for_body1.io.Out(9)


  br_8.io.enable <> bb_for_body1.io.Out(10)


  br_9.io.enable <> bb_for_body5_preheader2.io.Out(0)


  ret_10.io.In.enable <> bb_for_cond_cleanup43.io.Out(0)


  const4.io.enable <> bb_for_body54.io.Out(0)

  const5.io.enable <> bb_for_body54.io.Out(1)

  const6.io.enable <> bb_for_body54.io.Out(2)

  const7.io.enable <> bb_for_body54.io.Out(3)

  phiindvars_iv11.io.enable <> bb_for_body54.io.Out(4)


  Gep_arrayidx712.io.enable <> bb_for_body54.io.Out(5)


  ld_13.io.enable <> bb_for_body54.io.Out(6)


  binaryOp_mul14.io.enable <> bb_for_body54.io.Out(7)


  Gep_arrayidx915.io.enable <> bb_for_body54.io.Out(8)


  st_16.io.enable <> bb_for_body54.io.Out(9)


  binaryOp_indvars_iv_next17.io.enable <> bb_for_body54.io.Out(10)


  icmp_exitcond18.io.enable <> bb_for_body54.io.Out(11)


  br_19.io.enable <> bb_for_body54.io.Out(12)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv232.io.Mask <> bb_for_body1.io.MaskBB(0)

  phiindvars_iv11.io.Mask <> bb_for_body54.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_16.io.MemReq
  st_16.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp

  buffer_memories_0.io.rd.mem(0).MemReq <> ld_13.io.MemReq
  ld_13.io.MemResp <> buffer_memories_0.io.rd.mem(0).MemResp

  buffer_memories_0.io.wr.mem(0).MemReq <> st_5.io.MemReq
  st_5.io.MemResp <> buffer_memories_0.io.wr.mem(0).MemResp


  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv232.io.InData(0) <> const0.io.Out

  Gep_arrayidx3.io.idx(0) <> const1.io.Out

  binaryOp_indvars_iv_next246.io.RightIO <> const2.io.Out

  icmp_exitcond257.io.RightIO <> const3.io.Out

  phiindvars_iv11.io.InData(1) <> const4.io.Out

  Gep_arrayidx712.io.idx(0) <> const5.io.Out

  binaryOp_indvars_iv_next17.io.RightIO <> const6.io.Out

  icmp_exitcond18.io.RightIO <> const7.io.Out

  Gep_arrayidx3.io.idx(1) <> phiindvars_iv232.io.Out(0)

  trunc4.io.Input <> phiindvars_iv232.io.Out(1)

  binaryOp_indvars_iv_next246.io.LeftIO <> phiindvars_iv232.io.Out(2)

  st_5.io.GepAddr <> Gep_arrayidx3.io.Out(0)

  st_5.io.inData <> trunc4.io.Out(0)

  icmp_exitcond257.io.LeftIO <> binaryOp_indvars_iv_next246.io.Out(1)

  br_8.io.CmpIO <> icmp_exitcond257.io.Out(0)

  Gep_arrayidx712.io.idx(1) <> phiindvars_iv11.io.Out(0)

  Gep_arrayidx915.io.idx(0) <> phiindvars_iv11.io.Out(1)

  binaryOp_indvars_iv_next17.io.LeftIO <> phiindvars_iv11.io.Out(2)

  ld_13.io.GepAddr <> Gep_arrayidx712.io.Out(0)

  binaryOp_mul14.io.LeftIO <> ld_13.io.Out(0)

  st_16.io.inData <> binaryOp_mul14.io.Out(0)

  st_16.io.GepAddr <> Gep_arrayidx915.io.Out(0)

  icmp_exitcond18.io.LeftIO <> binaryOp_indvars_iv_next17.io.Out(1)

  br_19.io.CmpIO <> icmp_exitcond18.io.Out(0)

  st_5.io.Out(0).ready := true.B

  st_16.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_8.io.PredOp(0) <> st_5.io.SuccOp(0)

  br_19.io.PredOp(0) <> st_16.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_10.io.Out

}


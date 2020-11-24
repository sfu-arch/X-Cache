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
import dandelion.GuardReader


class reluDF(PtrsIn: Seq[Int] = List(64, 64), ValsIn: Seq[Int] = List(64), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 1, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1), valsArgTypes = List(3)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(2, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 1))

  val bb_for_body_lr_ph4 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 4))

  val bb_for_cond_cleanup_loopexit7 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 7))

  val bb_for_cond_cleanup9 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 9))

  val bb_for_body4_lr_ph11 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 1, BID = 11))

  val bb_for_cond_cleanup315 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 15))

  val bb_for_body419 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 17, NumPhi = 1, BID = 19))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp31 = icmp eq i32 %N, 0, !dbg !36, !UID !37
  val icmp_cmp310 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %cmp31, label %for.cond.cleanup, label %for.body.lr.ph, !dbg !38, !UID !39, !BB_UID !40
  val br_3 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 3))

  //  %wide.trip.count = zext i32 %N to i64, !UID !41
  val sextwide_trip_count5 = Module(new ZextNode(NumOuts = 1))

  //  br label %for.body4.lr.ph, !dbg !38, !UID !42, !BB_UID !43
  val br_6 = Module(new UBranchNode(ID = 6))

  //  br label %for.cond.cleanup, !dbg !44, !UID !45, !BB_UID !46
  val br_8 = Module(new UBranchNode(ID = 8))

  //  ret void, !dbg !44, !UID !47, !BB_UID !48
  val ret_10 = Module(new RetNode2(retTypes = List(), ID = 10))

  //  %j.032 = phi i32 [ 0, %for.body.lr.ph ], [ %inc13, %for.cond.cleanup3 ], !UID !49
  val phij_03212 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 12, Res = true, Debug = true, GuardVals=Seq.tabulate(100)(n => n)))

  //  %mul = mul i32 %j.032, %N, !UID !51
  val binaryOp_mul13 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "mul")(sign = false, Debug = true, GuardVals=GuardReader("relu.dbg"){13}))

  //  br label %for.body4, !dbg !52, !UID !53, !BB_UID !54
  val br_14 = Module(new UBranchNode(ID = 14))

  //  %inc13 = add nuw i32 %j.032, 1, !dbg !55, !UID !56
  val binaryOp_inc1316 = Module(new ComputeNode(NumOuts = 2, ID = 16, opCode = "add")(sign = false, Debug = true, GuardVals=GuardReader("relu.dbg"){16}))

  //  %exitcond33 = icmp eq i32 %inc13, %N, !dbg !36, !UID !57
  val icmp_exitcond3310 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond33, label %for.cond.cleanup.loopexit, label %for.body4.lr.ph, !dbg !38, !llvm.loop !58, !UID !60, !BB_UID !61
  val br_18 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 18))

  //  %indvars.iv = phi i64 [ 0, %for.body4.lr.ph ], [ %indvars.iv.next, %for.body4 ], !UID !62
  val phiindvars_iv20 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 20, Res = true))

  //  %0 = trunc i64 %indvars.iv to i32, !dbg !63, !UID !64
  val trunc21 = Module(new TruncNode(NumOuts = 1))

  //  %add = add i32 %mul, %0, !dbg !63, !UID !65
  val binaryOp_add22 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "add")(sign = false, Debug = false))

  //  %idxprom = zext i32 %add to i64, !dbg !67, !UID !69
  val sextidxprom23 = Module(new ZextNode(NumOuts = 2))

  //  %arrayidx = getelementptr inbounds i32, i32* %in, i64 %idxprom, !dbg !67, !UID !70
  val Gep_arrayidx24 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 24)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !dbg !67, !tbaa !71, !UID !75
  val ld_25 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 25, RouteID = 0))

  //  %arrayidx7 = getelementptr inbounds i32, i32* %out, i64 %idxprom, !UID !76
  val Gep_arrayidx726 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 26)(ElementSize = 8, ArraySize = List()))

  //  %2 = icmp sgt i32 %1, 0, !dbg !77, !UID !78
  val icmp_19 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "sgt")(sign = true, Debug = false))

  //  %. = select i1 %2, i32 %1, i32 0, !dbg !77, !UID !79
  val select__28 = Module(new SelectNode(NumOuts = 1, ID = 28)(fast = false))

  //  store i32 %., i32* %arrayidx7, align 4, !tbaa !71, !UID !80
  val st_29 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 29, RouteID = 1))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !81, !UID !82
  val binaryOp_indvars_iv_next30 = Module(new ComputeNode(NumOuts = 2, ID = 30, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !dbg !83, !UID !84
  val icmp_exitcond23 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup3, label %for.body4, !dbg !52, !llvm.loop !85, !UID !87, !BB_UID !88
  val br_32 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 32))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i64 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 0
  val const5 = Module(new ConstFastNode(value = 0, ID = 5))

  //i64 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_lr_ph4.io.predicateIn(0) <> br_3.io.FalseOutput(0)

  bb_for_cond_cleanup9.io.predicateIn(1) <> br_3.io.TrueOutput(0)

  bb_for_cond_cleanup9.io.predicateIn(0) <> br_8.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit7.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body4_lr_ph11.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body4_lr_ph11.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup315.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body419.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body419.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_14.io.Out(0)

  Loop_0.io.loopBack(0) <> br_32.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_32.io.TrueOutput(0)

  Loop_1.io.enable <> br_6.io.Out(0)

  Loop_1.io.loopBack(0) <> br_18.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_18.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_mul13.io.Out(0)

  Loop_0.io.InLiveIn(1) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(0) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  Loop_1.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_1.io.InLiveIn(3) <> sextwide_trip_count5.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add22.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  icmp_exitcond23.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx24.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx726.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  binaryOp_mul13.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  icmp_exitcond3310.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next30.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc1316.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv20.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phij_03212.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry1.io.Out(0)

  icmp_cmp310.io.enable <> bb_entry1.io.Out(1)


  br_3.io.enable <> bb_entry1.io.Out(2)


  sextwide_trip_count5.io.enable <> bb_for_body_lr_ph4.io.Out(0)


  br_6.io.enable <> bb_for_body_lr_ph4.io.Out(1)


  br_8.io.enable <> bb_for_cond_cleanup_loopexit7.io.Out(0)


  ret_10.io.In.enable <> bb_for_cond_cleanup9.io.Out(0)


  const1.io.enable <> bb_for_body4_lr_ph11.io.Out(0)

  phij_03212.io.enable <> bb_for_body4_lr_ph11.io.Out(1)


  binaryOp_mul13.io.enable <> bb_for_body4_lr_ph11.io.Out(2)


  br_14.io.enable <> bb_for_body4_lr_ph11.io.Out(3)


  const2.io.enable <> bb_for_cond_cleanup315.io.Out(0)

  binaryOp_inc1316.io.enable <> bb_for_cond_cleanup315.io.Out(1)


  icmp_exitcond3310.io.enable <> bb_for_cond_cleanup315.io.Out(2)


  br_18.io.enable <> bb_for_cond_cleanup315.io.Out(3)


  const3.io.enable <> bb_for_body419.io.Out(0)

  const4.io.enable <> bb_for_body419.io.Out(1)

  const5.io.enable <> bb_for_body419.io.Out(2)

  const6.io.enable <> bb_for_body419.io.Out(3)

  phiindvars_iv20.io.enable <> bb_for_body419.io.Out(4)


  trunc21.io.enable <> bb_for_body419.io.Out(5)


  binaryOp_add22.io.enable <> bb_for_body419.io.Out(6)


  sextidxprom23.io.enable <> bb_for_body419.io.Out(7)


  Gep_arrayidx24.io.enable <> bb_for_body419.io.Out(8)


  ld_25.io.enable <> bb_for_body419.io.Out(9)


  Gep_arrayidx726.io.enable <> bb_for_body419.io.Out(10)


  icmp_19.io.enable <> bb_for_body419.io.Out(11)


  select__28.io.enable <> bb_for_body419.io.Out(12)


  st_29.io.enable <> bb_for_body419.io.Out(13)


  binaryOp_indvars_iv_next30.io.enable <> bb_for_body419.io.Out(14)


  icmp_exitcond23.io.enable <> bb_for_body419.io.Out(15)


  br_32.io.enable <> bb_for_body419.io.Out(16)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phij_03212.io.Mask <> bb_for_body4_lr_ph11.io.MaskBB(0)

  phiindvars_iv20.io.Mask <> bb_for_body419.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_25.io.MemReq
  ld_25.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_29.io.MemReq
  st_29.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp310.io.RightIO <> const0.io.Out

  phij_03212.io.InData(0) <> const1.io.Out

  binaryOp_inc1316.io.RightIO <> const2.io.Out

  phiindvars_iv20.io.InData(0) <> const3.io.Out

  icmp_19.io.RightIO <> const4.io.Out

  select__28.io.InData2 <> const5.io.Out

  binaryOp_indvars_iv_next30.io.RightIO <> const6.io.Out

  br_3.io.CmpIO <> icmp_cmp310.io.Out(0)

  binaryOp_mul13.io.LeftIO <> phij_03212.io.Out(0)

  binaryOp_inc1316.io.LeftIO <> phij_03212.io.Out(1)

  icmp_exitcond3310.io.LeftIO <> binaryOp_inc1316.io.Out(1)

  br_18.io.CmpIO <> icmp_exitcond3310.io.Out(0)

  trunc21.io.Input <> phiindvars_iv20.io.Out(0)

  binaryOp_indvars_iv_next30.io.LeftIO <> phiindvars_iv20.io.Out(1)

  binaryOp_add22.io.RightIO <> trunc21.io.Out(0)

  sextidxprom23.io.Input <> binaryOp_add22.io.Out(0)

  Gep_arrayidx24.io.idx(0) <> sextidxprom23.io.Out(0)

  Gep_arrayidx726.io.idx(0) <> sextidxprom23.io.Out(1)

  ld_25.io.GepAddr <> Gep_arrayidx24.io.Out(0)

  icmp_19.io.LeftIO <> ld_25.io.Out(0)

  select__28.io.InData1 <> ld_25.io.Out(1)

  st_29.io.GepAddr <> Gep_arrayidx726.io.Out(0)

  select__28.io.Select <> icmp_19.io.Out(0)

  st_29.io.inData <> select__28.io.Out(0)

  icmp_exitcond23.io.LeftIO <> binaryOp_indvars_iv_next30.io.Out(1)

  br_32.io.CmpIO <> icmp_exitcond23.io.Out(0)

  icmp_cmp310.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  sextwide_trip_count5.io.Input <> ArgSplitter.io.Out.dataVals.elements("field0")(2)

  st_29.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_32.io.PredOp(0) <> st_29.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_10.io.Out

}


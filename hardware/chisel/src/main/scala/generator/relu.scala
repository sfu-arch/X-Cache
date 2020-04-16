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


class reluDF(PtrsIn: Seq[Int] = List(32, 32), ValsIn: Seq[Int] = List(32), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 1, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
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

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body_lr_ph1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_for_body4_lr_ph4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup35 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_for_body46 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 17, NumPhi = 1, BID = 6))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp31 = icmp eq i32 %N, 0, !dbg !36, !UID !37
  val icmp_cmp310 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %cmp31, label %for.cond.cleanup, label %for.body.lr.ph, !dbg !38, !UID !39, !BB_UID !40
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  %wide.trip.count = zext i32 %N to i64, !UID !41
  val sextwide_trip_count2 = Module(new ZextNode(NumOuts = 1))

  //  br label %for.body4.lr.ph, !dbg !38, !UID !42, !BB_UID !43
  val br_3 = Module(new UBranchNode(ID = 3))

  //  br label %for.cond.cleanup, !dbg !44
  val br_4 = Module(new UBranchNode(ID = 4))

  //  ret void, !dbg !44, !UID !45, !BB_UID !46
  val ret_5 = Module(new RetNode2(retTypes = List(), ID = 5, NumBores = 1, Debug = true))

  //  %j.032 = phi i32 [ 0, %for.body.lr.ph ], [ %inc13, %for.cond.cleanup3 ], !UID !47
  val phij_0326 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 6, Res = true))

  //  %mul = mul i32 %j.032, %N, !UID !49
  val binaryOp_mul7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body4, !dbg !50, !UID !51, !BB_UID !52
  val br_8 = Module(new UBranchNode(ID = 8))

  //  %inc13 = add nuw i32 %j.032, 1, !dbg !53, !UID !54
  val binaryOp_inc139 = Module(new ComputeNode(NumOuts = 2, ID = 9, opCode = "add")(sign = false, Debug = false))

  //  %exitcond33 = icmp eq i32 %inc13, %N, !dbg !36, !UID !55
  val icmp_exitcond3310 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond33, label %for.cond.cleanup.loopexit, label %for.body4.lr.ph, !dbg !38, !llvm.loop !56, !UID !58, !BB_UID !59
  val br_11 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 11))

  //  %indvars.iv = phi i64 [ 0, %for.body4.lr.ph ], [ %indvars.iv.next, %for.body4 ], !UID !60
  val phiindvars_iv12 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 12, Res = true))

  //  %0 = trunc i64 %indvars.iv to i32, !dbg !61, !UID !62
  val trunc13 = Module(new TruncNode(NumOuts = 1))

  //  %add = add i32 %mul, %0, !dbg !61, !UID !63
  val binaryOp_add14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %idxprom = zext i32 %add to i64, !dbg !65, !UID !67
  val sextidxprom15 = Module(new ZextNode(NumOuts = 2))

  //  %arrayidx = getelementptr inbounds i32, i32* %in, i64 %idxprom, !dbg !65, !UID !68
  val Gep_arrayidx16 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 16)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !dbg !65, !tbaa !69, !UID !73
  val ld_17 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 17, RouteID = 0))

  //  %arrayidx7 = getelementptr inbounds i32, i32* %out, i64 %idxprom, !UID !74
  val Gep_arrayidx718 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 18)(ElementSize = 8, ArraySize = List()))

  //  %2 = icmp sgt i32 %1, 0, !dbg !75, !UID !76
  val icmp_19 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "sgt")(sign = true, Debug = false))

  //  %. = select i1 %2, i32 %1, i32 0, !dbg !75, !UID !77
  val select__20 = Module(new SelectNode(NumOuts = 1, ID = 20)(fast = false))

  //  store i32 %., i32* %arrayidx7, align 4, !tbaa !69, !UID !78
  val st_21 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 21, RouteID = 1, Debug = true,
    GuardAddress = List(8192, 8200, 8208, 8216, 8224, 8232, 8240, 8248, 8256, 8264,
      8272, 8280, 8288, 8296, 8304, 8312, 8320, 8328, 8336, 8344, 8352, 8360, 8368,
      8376, 8384, 8392, 8400, 8408, 8416, 8424, 8432, 8440, 8448, 8456, 8464, 8472,
      8480, 8488, 8496, 8504, 8512, 8520, 8528, 8536, 8544, 8552, 8560, 8568, 8576,
      8584, 8592, 8600, 8608, 8616, 8624, 8632, 8640, 8648, 8656, 8664, 8672, 8680, 8688, 8696)))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !79, !UID !80
  val binaryOp_indvars_iv_next22 = Module(new ComputeNode(NumOuts = 2, ID = 22, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !dbg !81, !UID !82
  val icmp_exitcond23 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup3, label %for.body4, !dbg !50, !llvm.loop !83, !UID !85, !BB_UID !86
  val br_24 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 24))



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

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_lr_ph1.io.predicateIn(0) <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_4.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body4_lr_ph4.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body4_lr_ph4.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup35.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body46.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body46.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_8.io.Out(0)

  Loop_0.io.loopBack(0) <> br_24.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_24.io.TrueOutput(0)

  Loop_1.io.enable <> br_3.io.Out(0)

  Loop_1.io.loopBack(0) <> br_11.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_11.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_mul7.io.Out(0)

  Loop_0.io.InLiveIn(1) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(0) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  Loop_1.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_1.io.InLiveIn(3) <> sextwide_trip_count2.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add14.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx16.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(0)

  icmp_exitcond23.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx718.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  binaryOp_mul7.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

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

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next22.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc139.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv12.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phij_0326.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp310.io.enable <> bb_entry0.io.Out(1)


  br_1.io.enable <> bb_entry0.io.Out(2)


  sextwide_trip_count2.io.enable <> bb_for_body_lr_ph1.io.Out(0)


  br_3.io.enable <> bb_for_body_lr_ph1.io.Out(1)


  br_4.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  ret_5.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  const1.io.enable <> bb_for_body4_lr_ph4.io.Out(0)

  phij_0326.io.enable <> bb_for_body4_lr_ph4.io.Out(1)


  binaryOp_mul7.io.enable <> bb_for_body4_lr_ph4.io.Out(2)


  br_8.io.enable <> bb_for_body4_lr_ph4.io.Out(3)


  const2.io.enable <> bb_for_cond_cleanup35.io.Out(0)

  binaryOp_inc139.io.enable <> bb_for_cond_cleanup35.io.Out(1)


  icmp_exitcond3310.io.enable <> bb_for_cond_cleanup35.io.Out(2)


  br_11.io.enable <> bb_for_cond_cleanup35.io.Out(3)


  const3.io.enable <> bb_for_body46.io.Out(0)

  const4.io.enable <> bb_for_body46.io.Out(1)

  const5.io.enable <> bb_for_body46.io.Out(2)

  const6.io.enable <> bb_for_body46.io.Out(3)

  phiindvars_iv12.io.enable <> bb_for_body46.io.Out(4)


  trunc13.io.enable <> bb_for_body46.io.Out(5)


  binaryOp_add14.io.enable <> bb_for_body46.io.Out(6)


  sextidxprom15.io.enable <> bb_for_body46.io.Out(7)


  Gep_arrayidx16.io.enable <> bb_for_body46.io.Out(8)


  ld_17.io.enable <> bb_for_body46.io.Out(9)


  Gep_arrayidx718.io.enable <> bb_for_body46.io.Out(10)


  icmp_19.io.enable <> bb_for_body46.io.Out(11)


  select__20.io.enable <> bb_for_body46.io.Out(12)


  st_21.io.enable <> bb_for_body46.io.Out(13)


  binaryOp_indvars_iv_next22.io.enable <> bb_for_body46.io.Out(14)


  icmp_exitcond23.io.enable <> bb_for_body46.io.Out(15)


  br_24.io.enable <> bb_for_body46.io.Out(16)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phij_0326.io.Mask <> bb_for_body4_lr_ph4.io.MaskBB(0)

  phiindvars_iv12.io.Mask <> bb_for_body46.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_17.io.MemReq

  ld_17.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_21.io.MemReq

  st_21.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp310.io.RightIO <> const0.io.Out

  phij_0326.io.InData(0) <> const1.io.Out

  binaryOp_inc139.io.RightIO <> const2.io.Out

  phiindvars_iv12.io.InData(0) <> const3.io.Out

  icmp_19.io.RightIO <> const4.io.Out

  select__20.io.InData2 <> const5.io.Out

  binaryOp_indvars_iv_next22.io.RightIO <> const6.io.Out

  br_1.io.CmpIO <> icmp_cmp310.io.Out(0)

  binaryOp_mul7.io.LeftIO <> phij_0326.io.Out(0)

  binaryOp_inc139.io.LeftIO <> phij_0326.io.Out(1)

  icmp_exitcond3310.io.LeftIO <> binaryOp_inc139.io.Out(1)

  br_11.io.CmpIO <> icmp_exitcond3310.io.Out(0)

  trunc13.io.Input <> phiindvars_iv12.io.Out(0)

  binaryOp_indvars_iv_next22.io.LeftIO <> phiindvars_iv12.io.Out(1)

  binaryOp_add14.io.RightIO <> trunc13.io.Out(0)

  sextidxprom15.io.Input <> binaryOp_add14.io.Out(0)

  Gep_arrayidx16.io.idx(0) <> sextidxprom15.io.Out(0)

  Gep_arrayidx718.io.idx(0) <> sextidxprom15.io.Out(1)

  ld_17.io.GepAddr <> Gep_arrayidx16.io.Out(0)

  icmp_19.io.LeftIO <> ld_17.io.Out(0)

  select__20.io.InData1 <> ld_17.io.Out(1)

  st_21.io.GepAddr <> Gep_arrayidx718.io.Out(0)

  select__20.io.Select <> icmp_19.io.Out(0)

  st_21.io.inData <> select__20.io.Out(0)

  icmp_exitcond23.io.LeftIO <> binaryOp_indvars_iv_next22.io.Out(1)

  br_24.io.CmpIO <> icmp_exitcond23.io.Out(0)

  icmp_cmp310.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  sextwide_trip_count2.io.Input <> ArgSplitter.io.Out.dataVals.elements("field0")(2)

  st_21.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_24.io.PredOp(0) <> st_21.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_5.io.Out

}


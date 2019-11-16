package dandelion.generator

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


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class extract_function_harris_f2DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32))))
    val call_78_out = Decoupled(new Call(List(32, 32)))
    val call_78_in = Flipped(Decoupled(new Call(List(32))))
    val call_81_out = Decoupled(new Call(List(32, 32)))
    val call_81_in = Flipped(Decoupled(new Call(List(32))))
    val call_100_out = Decoupled(new Call(List(32, 32)))
    val call_100_in = Flipped(Decoupled(new Call(List(32))))
    val call_102_out = Decoupled(new Call(List(32, 32)))
    val call_102_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val call_f3_out = Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32)))
    val call_f3_in = Flipped(Decoupled(new Call(List())))
    val out = Decoupled(new Call(List()))
  })
}

class extract_function_harris_f2DF(implicit p: Parameters) extends extract_function_harris_f2DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 9, NWrites = 7)
  (WControl = new WriteMemoryController(NumOps = 7, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 9, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val StackPointer = Module(new Stack(NumOps = 6))

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 2, 1, 1, 2, 1, 3, 1, 3, 2, 2, 8, 2, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 4))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 19, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 2))

  val bb_for_cond_cleanup83 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 3))

  val bb_for_body94 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup145 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_for_body156 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 6))

  val bb_for_cond_cleanup217 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 7))

  val bb_for_body228 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 84, NumPhi = 1, BID = 8))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %_344 = alloca i16, align 2
  val alloca__3440 = Module(new AllocaNode(NumOuts=2, ID = 0, RouteID=0))

  //  %_345 = alloca i16, align 2
  val alloca__3451 = Module(new AllocaNode(NumOuts=2, ID = 1, RouteID=1))

  //  %_346 = alloca i16, align 2
  val alloca__3462 = Module(new AllocaNode(NumOuts=2, ID = 2, RouteID=2))

  //  %_347 = alloca i16, align 2
  val alloca__3473 = Module(new AllocaNode(NumOuts=2, ID = 3, RouteID=3))

  //  %_362 = alloca i16, align 2
  val alloca__3624 = Module(new AllocaNode(NumOuts=2, ID = 4, RouteID=4))

  //  %_363 = alloca i16, align 2
  val alloca__3635 = Module(new AllocaNode(NumOuts=2, ID = 5, RouteID=5))

  //  %sub = sub i32 4, %_output_s0_x, !dbg !1223
  val binaryOp_sub6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "sub")(sign = false))

  //  %add = add nsw i32 %_output_s0_x, -1, !dbg !1226
  val binaryOp_add7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign = false))

  //  %add1 = add nsw i32 %_output_s0_y, -1, !dbg !1229
  val binaryOp_add18 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign = false))

  //  %0 = bitcast i16* %_344 to i8*
  val bitcast_9 = Module(new BitCastNode(NumOuts = 1, ID = 9))

  //  %1 = bitcast i16* %_345 to i8*
  val bitcast_10 = Module(new BitCastNode(NumOuts = 1, ID = 10))

  //  %2 = bitcast i16* %_346 to i8*
  val bitcast_11 = Module(new BitCastNode(NumOuts = 1, ID = 11))

  //  %3 = bitcast i16* %_347 to i8*
  val bitcast_12 = Module(new BitCastNode(NumOuts = 1, ID = 12))

  //  %4 = bitcast i16* %_362 to i8*
  val bitcast_13 = Module(new BitCastNode(NumOuts = 1, ID = 13))

  //  %5 = bitcast i16* %_363 to i8*
  val bitcast_14 = Module(new BitCastNode(NumOuts = 1, ID = 14))

  //  br label %for.body, !dbg !1234, !BB_UID !1235
  val br_15 = Module(new UBranchNode(ID = 15))

  //  ret void, !dbg !1236, !BB_UID !1237
  val ret_16 = Module(new RetNode2(retTypes = List(), ID = 16))

  //  %_lgxy_s1_y.0213 = phi i32 [ %add1, %entry ], [ %inc118, %for.cond.cleanup8 ]
  val phi_lgxy_s1_y_021317 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 17, Res = true))

  //  %sub3 = sub nsw i32 %_lgxy_s1_y.0213, %_output_s0_y, !dbg !1239
  val binaryOp_sub318 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "sub")(sign = false))

  //  %mul = mul nsw i32 %sub3, 3, !dbg !1242
  val binaryOp_mul19 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "mul")(sign = false))

  //  %add4 = add nsw i32 %sub, %mul, !dbg !1245
  val binaryOp_add420 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "add")(sign = false))

  //  br label %for.body9, !dbg !1250, !BB_UID !1251
  val br_21 = Module(new UBranchNode(ID = 21))

  //  %inc118 = add nsw i32 %_lgxy_s1_y.0213, 1, !dbg !1252
  val binaryOp_inc11822 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "add")(sign = false))

  //  %cmp = icmp sgt i32 %_lgxy_s1_y.0213, %_output_s0_y, !dbg !1254
  val icmp_cmp23 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "ugt")(sign = false))

  //  br i1 %cmp, label %for.cond.cleanup, label %for.body, !dbg !1234, !llvm.loop !1255, !BB_UID !1257
  val br_24 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 24))

  //  %_lgxy_s1_x.0212 = phi i32 [ %add, %for.body ], [ %inc115, %for.cond.cleanup14 ]
  val phi_lgxy_s1_x_021225 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 25, Res = true))

  //  %sub10 = sub i32 %_lgxy_s1_x.0212, %_243, !dbg !1259
  val binaryOp_sub1026 = Module(new ComputeNode(NumOuts = 1, ID = 26, opCode = "sub")(sign = false))

  //  %add11 = add nsw i32 %add4, %_lgxy_s1_x.0212, !dbg !1262
  val binaryOp_add1127 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "add")(sign = false))

  //  %arrayidx31 = getelementptr inbounds i32, i32* %_lgxy, i32 %add11
  val Gep_arrayidx3128 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 28)(ElementSize = 4, ArraySize = List()))

  //  br label %for.body15, !dbg !1267, !BB_UID !1268
  val br_29 = Module(new UBranchNode(ID = 29))

  //  %inc115 = add nsw i32 %_lgxy_s1_x.0212, 1, !dbg !1269
  val binaryOp_inc11530 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign = false))

  //  %cmp7 = icmp sgt i32 %_lgxy_s1_x.0212, %_output_s0_x, !dbg !1271
  val icmp_cmp731 = Module(new ComputeNode(NumOuts = 1, ID = 31, opCode = "ugt")(sign = false))

  //  br i1 %cmp7, label %for.cond.cleanup8, label %for.body9, !dbg !1250, !llvm.loop !1272, !BB_UID !1274
  val br_32 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 32))

  //  %_lgxy_s1_box__y.0211 = phi i32 [ -1, %for.body9 ], [ %inc112, %for.cond.cleanup21 ]
  val phi_lgxy_s1_box__y_021133 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 33, Res = true))

  //  %add16 = add nsw i32 %_lgxy_s1_box__y.0211, %_lgxy_s1_y.0213, !dbg !1276
  val binaryOp_add1634 = Module(new ComputeNode(NumOuts = 1, ID = 34, opCode = "add")(sign = false))

  //  %mul17 = mul nsw i32 %add16, %_18, !dbg !1279
  val binaryOp_mul1735 = Module(new ComputeNode(NumOuts = 1, ID = 35, opCode = "mul")(sign = false))

  //  %add18 = add nsw i32 %sub10, %mul17, !dbg !1282
  val binaryOp_add1836 = Module(new ComputeNode(NumOuts = 1, ID = 36, opCode = "add")(sign = false))

  //  br label %for.body22, !dbg !1287, !BB_UID !1288
  val br_37 = Module(new UBranchNode(ID = 37))

  //  %inc112 = add nsw i32 %_lgxy_s1_box__y.0211, 1, !dbg !1289
  val binaryOp_inc11238 = Module(new ComputeNode(NumOuts = 2, ID = 38, opCode = "add")(sign = false))

  //  %exitcond214 = icmp eq i32 %inc112, 2, !dbg !1291
  val icmp_exitcond21439 = Module(new ComputeNode(NumOuts = 1, ID = 39, opCode = "eq")(sign = false))

  //  br i1 %exitcond214, label %for.cond.cleanup14, label %for.body15, !dbg !1267, !llvm.loop !1292, !BB_UID !1294
  val br_40 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 40))

  //  %_lgxy_s1_box__x.0210 = phi i32 [ -1, %for.body15 ], [ %inc, %for.body22 ]
  val phi_lgxy_s1_box__x_021041 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 41, Res = true))

  //  %add23 = add nsw i32 %add18, %_lgxy_s1_box__x.0210, !dbg !1296
  val binaryOp_add2342 = Module(new ComputeNode(NumOuts = 8, ID = 42, opCode = "add")(sign = false))

  //  %add24 = add nsw i32 %add23, %_239, !dbg !1299
  val binaryOp_add2443 = Module(new ComputeNode(NumOuts = 1, ID = 43, opCode = "add")(sign = false))

  //  %arrayidx = getelementptr inbounds i8, i8* %_input, i32 %add24, !dbg !1302
  val Gep_arrayidx44 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 44)(ElementSize = 1, ArraySize = List()))

  //  %6 = load i8, i8* %arrayidx, align 1, !dbg !1302, !tbaa !1303
  val ld_45 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 45, RouteID = 0))

  //  %add25 = add nsw i32 %add23, %_241, !dbg !1308
  val binaryOp_add2546 = Module(new ComputeNode(NumOuts = 1, ID = 46, opCode = "add")(sign = false))

  //  %arrayidx26 = getelementptr inbounds i8, i8* %_input, i32 %add25, !dbg !1311
  val Gep_arrayidx2647 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 47)(ElementSize = 1, ArraySize = List()))

  //  %7 = load i8, i8* %arrayidx26, align 1, !dbg !1311, !tbaa !1303
  val ld_48 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 48, RouteID = 1))

  //  %add27 = add nsw i32 %add23, %_236, !dbg !1314
  val binaryOp_add2749 = Module(new ComputeNode(NumOuts = 1, ID = 49, opCode = "add")(sign = false))

  //  %arrayidx28 = getelementptr inbounds i8, i8* %_input, i32 %add27, !dbg !1317
  val Gep_arrayidx2850 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 50)(ElementSize = 1, ArraySize = List()))

  //  %8 = load i8, i8* %arrayidx28, align 1, !dbg !1317, !tbaa !1303
  val ld_51 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 51, RouteID = 2))

  //  %add29 = add nsw i32 %add23, %_234, !dbg !1320
  val binaryOp_add2952 = Module(new ComputeNode(NumOuts = 1, ID = 52, opCode = "add")(sign = false))

  //  %arrayidx30 = getelementptr inbounds i8, i8* %_input, i32 %add29, !dbg !1323
  val Gep_arrayidx3053 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 53)(ElementSize = 1, ArraySize = List()))

  //  %9 = load i8, i8* %arrayidx30, align 1, !dbg !1323, !tbaa !1303
  val ld_54 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 54, RouteID = 3))

  //  %10 = load i32, i32* %arrayidx31, align 4, !dbg !1326, !tbaa !1327
  val ld_55 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 55, RouteID = 4))

  //  %add32 = add nsw i32 %add23, %_237, !dbg !1331
  val binaryOp_add3256 = Module(new ComputeNode(NumOuts = 1, ID = 56, opCode = "add")(sign = false))

  //  %arrayidx33 = getelementptr inbounds i8, i8* %_input, i32 %add32, !dbg !1334
  val Gep_arrayidx3357 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 57)(ElementSize = 1, ArraySize = List()))

  //  %11 = load i8, i8* %arrayidx33, align 1, !dbg !1334, !tbaa !1303
  val ld_58 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 58, RouteID = 5))

  //  %conv34 = zext i8 %11 to i32, !dbg !1339
  val sextconv3459 = Module(new ZextNode(NumOuts = 1))

  //  %mul36 = shl nuw nsw i32 %conv34, 1, !dbg !1340
  val binaryOp_mul3660 = Module(new ComputeNode(NumOuts = 1, ID = 60, opCode = "shl")(sign = false))

  //  %conv40 = zext i8 %6 to i32, !dbg !1341
  val sextconv4061 = Module(new ZextNode(NumOuts = 2))

  //  %conv45 = zext i8 %7 to i32, !dbg !1342
  val sextconv4562 = Module(new ZextNode(NumOuts = 2))

  //  %add48 = add nsw i32 %add23, %_238, !dbg !1343
  val binaryOp_add4863 = Module(new ComputeNode(NumOuts = 1, ID = 63, opCode = "add")(sign = false))

  //  %arrayidx49 = getelementptr inbounds i8, i8* %_input, i32 %add48, !dbg !1346
  val Gep_arrayidx4964 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 64)(ElementSize = 1, ArraySize = List()))

  //  %12 = load i8, i8* %arrayidx49, align 1, !dbg !1346, !tbaa !1303
  val ld_65 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 65, RouteID = 6))

  //  %conv51 = zext i8 %12 to i32, !dbg !1349
  val sextconv5166 = Module(new ZextNode(NumOuts = 1))

  //  %mul53 = shl nuw nsw i32 %conv51, 1, !dbg !1350
  val binaryOp_mul5367 = Module(new ComputeNode(NumOuts = 1, ID = 67, opCode = "shl")(sign = false))

  //  %conv61 = zext i8 %8 to i32, !dbg !1351
  val sextconv6168 = Module(new ZextNode(NumOuts = 2))

  //  %conv66 = zext i8 %9 to i32, !dbg !1354
  val sextconv6669 = Module(new ZextNode(NumOuts = 2))

  //  %add41 = sub nsw i32 %conv40, %conv45, !dbg !1355
  val binaryOp_add4170 = Module(new ComputeNode(NumOuts = 1, ID = 70, opCode = "sub")(sign = false))

  //  %sub46 = sub nsw i32 %add41, %conv61, !dbg !1356
  val binaryOp_sub4671 = Module(new ComputeNode(NumOuts = 1, ID = 71, opCode = "sub")(sign = false))

  //  %sub57 = add nsw i32 %sub46, %conv66, !dbg !1357
  val binaryOp_sub5772 = Module(new ComputeNode(NumOuts = 1, ID = 72, opCode = "add")(sign = false))

  //  %sub62 = add nsw i32 %sub57, %mul36, !dbg !1358
  val binaryOp_sub6273 = Module(new ComputeNode(NumOuts = 1, ID = 73, opCode = "add")(sign = false))

  //  %add67 = sub nsw i32 %sub62, %mul53, !dbg !1359
  val binaryOp_add6774 = Module(new ComputeNode(NumOuts = 1, ID = 74, opCode = "sub")(sign = false))

  //  %conv68 = trunc i32 %add67 to i16, !dbg !1360
  val truncconv6875 = Module(new TruncNode(NumOuts = 1))

  //  store i16 %conv68, i16* %_344, align 2, !dbg !1361, !tbaa !1363
  val st_76 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 76, RouteID = 0))

  //  store i16 255, i16* %_345, align 2, !dbg !1367, !tbaa !1363
  val st_77 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 77, RouteID = 1))

  //  %call = call signext i16 @_Z14halide_cpp_minIsET_RKS0_S2_(i16* nonnull dereferenceable(2) %_344, i16* nonnull dereferenceable(2) %_345), !dbg !1373, !UID !1374
  val call_78_out = Module(new CallOutNode(ID = 78, NumSuccOps = 0, argTypes = List(32,32)))

  val call_78_in = Module(new CallInNode(ID = 78, argTypes = List(32)))

  //  store i16 %call, i16* %_346, align 2, !dbg !1375, !tbaa !1363
  val st_79 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 79, RouteID = 2))

  //  store i16 -255, i16* %_347, align 2, !dbg !1379, !tbaa !1363
  val st_80 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 80, RouteID = 3))

  //  %call69 = call signext i16 @_Z14halide_cpp_maxIsET_RKS0_S2_(i16* nonnull dereferenceable(2) %_346, i16* nonnull dereferenceable(2) %_347), !dbg !1383, !UID !1384
  val call_81_out = Module(new CallOutNode(ID = 81, NumSuccOps = 0, argTypes = List(32,32)))

  val call_81_in = Module(new CallInNode(ID = 81, argTypes = List(32)))

  //  %conv70 = sext i16 %call69 to i32, !dbg !1387
  val sextconv7082 = Module(new SextNode(NumOuts = 1))

  //  %add71 = add nsw i32 %add23, %_235, !dbg !1390
  val binaryOp_add7183 = Module(new ComputeNode(NumOuts = 1, ID = 83, opCode = "add")(sign = false))

  //  %arrayidx72 = getelementptr inbounds i8, i8* %_input, i32 %add71, !dbg !1393
  val Gep_arrayidx7284 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 84)(ElementSize = 1, ArraySize = List()))

  //  %13 = load i8, i8* %arrayidx72, align 1, !dbg !1393, !tbaa !1303
  val ld_85 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 85, RouteID = 7))

  //  %conv74 = zext i8 %13 to i32, !dbg !1396
  val sextconv7486 = Module(new ZextNode(NumOuts = 1))

  //  %mul76 = shl nuw nsw i32 %conv74, 1, !dbg !1397
  val binaryOp_mul7687 = Module(new ComputeNode(NumOuts = 1, ID = 87, opCode = "shl")(sign = false))

  //  %add86 = add nsw i32 %add23, %_240, !dbg !1398
  val binaryOp_add8688 = Module(new ComputeNode(NumOuts = 1, ID = 88, opCode = "add")(sign = false))

  //  %arrayidx87 = getelementptr inbounds i8, i8* %_input, i32 %add86, !dbg !1401
  val Gep_arrayidx8789 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 89)(ElementSize = 1, ArraySize = List()))

  //  %14 = load i8, i8* %arrayidx87, align 1, !dbg !1401, !tbaa !1303
  val ld_90 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 90, RouteID = 8))

  //  %conv89 = zext i8 %14 to i32, !dbg !1404
  val sextconv8991 = Module(new ZextNode(NumOuts = 1))

  //  %mul91 = shl nuw nsw i32 %conv89, 1, !dbg !1405
  val binaryOp_mul9192 = Module(new ComputeNode(NumOuts = 1, ID = 92, opCode = "shl")(sign = false))

  //  %15 = add nuw nsw i32 %conv45, %conv40, !dbg !1408
  val binaryOp_93 = Module(new ComputeNode(NumOuts = 1, ID = 93, opCode = "add")(sign = false))

  //  %sub84 = sub nsw i32 %conv61, %15, !dbg !1409
  val binaryOp_sub8494 = Module(new ComputeNode(NumOuts = 1, ID = 94, opCode = "sub")(sign = false))

  //  %sub95 = add nsw i32 %sub84, %conv66, !dbg !1410
  val binaryOp_sub9595 = Module(new ComputeNode(NumOuts = 1, ID = 95, opCode = "add")(sign = false))

  //  %add99 = add nsw i32 %sub95, %mul76, !dbg !1411
  val binaryOp_add9996 = Module(new ComputeNode(NumOuts = 1, ID = 96, opCode = "add")(sign = false))

  //  %sub103 = sub nsw i32 %add99, %mul91, !dbg !1412
  val binaryOp_sub10397 = Module(new ComputeNode(NumOuts = 1, ID = 97, opCode = "sub")(sign = false))

  //  %conv104 = trunc i32 %sub103 to i16, !dbg !1413
  val truncconv10498 = Module(new TruncNode(NumOuts = 1))

  //  store i16 %conv104, i16* %_362, align 2, !dbg !1414, !tbaa !1363
  val st_99 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 99, RouteID = 4))

  //  %call105 = call signext i16 @_Z14halide_cpp_minIsET_RKS0_S2_(i16* nonnull dereferenceable(2) %_362, i16* nonnull dereferenceable(2) %_345), !dbg !1420, !UID !1421
  val call_100_out = Module(new CallOutNode(ID = 100, NumSuccOps = 0, argTypes = List(32,32)))

  val call_100_in = Module(new CallInNode(ID = 100, argTypes = List(32)))

  //  store i16 %call105, i16* %_363, align 2, !dbg !1422, !tbaa !1363
  val st_101 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 101, RouteID = 5))

  //  %call106 = call signext i16 @_Z14halide_cpp_maxIsET_RKS0_S2_(i16* nonnull dereferenceable(2) %_363, i16* nonnull dereferenceable(2) %_347), !dbg !1426, !UID !1427
  val call_102_out = Module(new CallOutNode(ID = 102, NumSuccOps = 0, argTypes = List(32,32)))

  val call_102_in = Module(new CallInNode(ID = 102, argTypes = List(32)))

  //  %conv107 = sext i16 %call106 to i32, !dbg !1430
  val sextconv107103 = Module(new SextNode(NumOuts = 1))

  //  %mul108 = mul nsw i32 %conv107, %conv70, !dbg !1433
  val binaryOp_mul108104 = Module(new ComputeNode(NumOuts = 1, ID = 104, opCode = "mul")(sign = false))

  //  %shr = ashr i32 %mul108, 7, !dbg !1436
  val binaryOp_shr105 = Module(new ComputeNode(NumOuts = 1, ID = 105, opCode = "ashr")(sign = false))

  //  %add109 = add nsw i32 %shr, %10, !dbg !1439
  val binaryOp_add109106 = Module(new ComputeNode(NumOuts = 1, ID = 106, opCode = "add")(sign = false))

  //  store i32 %add109, i32* %arrayidx31, align 4, !dbg !1442, !tbaa !1327
  val st_107 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 107, RouteID = 6))

  //  %inc = add nsw i32 %_lgxy_s1_box__x.0210, 1, !dbg !1450
  val binaryOp_inc108 = Module(new ComputeNode(NumOuts = 2, ID = 108, opCode = "add")(sign = false))

  //  %exitcond = icmp eq i32 %inc, 2, !dbg !1452
  val icmp_exitcond109 = Module(new ComputeNode(NumOuts = 1, ID = 109, opCode = "eq")(sign = false))

  //  br i1 %exitcond, label %for.cond.cleanup21, label %for.body22, !dbg !1287, !llvm.loop !1453, !BB_UID !1455
  val br_110 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 4, ID = 110))


  val call_f3_out = Module(new CallOutNode(ID = 76, NumSuccOps = 0, argTypes = List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32)))
  val call_f3_in = Module(new CallInNode(ID = 76, argTypes = List()))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 4
  val const0 = Module(new ConstFastNode(value = 4, ID = 0))

  //i32 -1
  val const1 = Module(new ConstFastNode(value = -1, ID = 1))

  //i32 -1
  val const2 = Module(new ConstFastNode(value = -1, ID = 2))

  //i32 3
  val const3 = Module(new ConstFastNode(value = 3, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 -1
  val const6 = Module(new ConstFastNode(value = -1, ID = 6))

  //i32 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i32 2
  val const8 = Module(new ConstFastNode(value = 2, ID = 8))

  //i32 -1
  val const9 = Module(new ConstFastNode(value = -1, ID = 9))

  //i32 1
  val const10 = Module(new ConstFastNode(value = 1, ID = 10))

  //i32 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i16 255
  val const12 = Module(new ConstFastNode(value = 255, ID = 12))

  //i16 -255
  val const13 = Module(new ConstFastNode(value = -255, ID = 13))

  //i32 1
  val const14 = Module(new ConstFastNode(value = 1, ID = 14))

  //i32 1
  val const15 = Module(new ConstFastNode(value = 1, ID = 15))

  //i32 7
  val const16 = Module(new ConstFastNode(value = 7, ID = 16))

  //i32 1
  val const17 = Module(new ConstFastNode(value = 1, ID = 17))

  //i32 2
  val const18 = Module(new ConstFastNode(value = 2, ID = 18))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup1.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_body2.io.predicateIn(1) <> Loop_3.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_3.io.activate_loop_back

  bb_for_cond_cleanup83.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body94.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body94.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup145.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body156.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body156.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup217.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body228.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body228.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_37.io.Out(0)

  Loop_0.io.loopBack(0) <> br_110.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_110.io.TrueOutput(0)

  Loop_1.io.enable <> br_29.io.Out(0)

  Loop_1.io.loopBack(0) <> br_40.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_40.io.TrueOutput(0)

  Loop_2.io.enable <> br_21.io.Out(0)

  Loop_2.io.loopBack(0) <> br_32.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_32.io.TrueOutput(0)

  Loop_3.io.enable <> br_15.io.Out(0)

  Loop_3.io.loopBack(0) <> br_24.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_24.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_add1836.io.Out(0)

  Loop_0.io.InLiveIn(1) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field4")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field5")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field6")(0)

  Loop_0.io.InLiveIn(6) <> Loop_1.io.OutLiveIn.elements("field7")(0)

  Loop_0.io.InLiveIn(7) <> Loop_1.io.OutLiveIn.elements("field8")(0)

  Loop_0.io.InLiveIn(8) <> Loop_1.io.OutLiveIn.elements("field9")(0)

  Loop_0.io.InLiveIn(9) <> Loop_1.io.OutLiveIn.elements("field10")(0)

  Loop_0.io.InLiveIn(10) <> Loop_1.io.OutLiveIn.elements("field11")(0)

  Loop_0.io.InLiveIn(11) <> Loop_1.io.OutLiveIn.elements("field12")(0)

  Loop_0.io.InLiveIn(12) <> Loop_1.io.OutLiveIn.elements("field13")(0)

  Loop_0.io.InLiveIn(13) <> Loop_1.io.OutLiveIn.elements("field14")(0)

  Loop_0.io.InLiveIn(14) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(15) <> Loop_1.io.OutLiveIn.elements("field15")(0)

  Loop_0.io.InLiveIn(16) <> Loop_1.io.OutLiveIn.elements("field16")(0)

  Loop_0.io.InLiveIn(17) <> Loop_1.io.OutLiveIn.elements("field17")(0)

  Loop_0.io.InLiveIn(18) <> Loop_1.io.OutLiveIn.elements("field18")(0)

  Loop_0.io.InLiveIn(19) <> Loop_1.io.OutLiveIn.elements("field19")(0)

  Loop_0.io.InLiveIn(20) <> Loop_1.io.OutLiveIn.elements("field20")(0)

  Loop_0.io.InLiveIn(21) <> Loop_1.io.OutLiveIn.elements("field21")(0)

  Loop_0.io.InLiveIn(22) <> Loop_1.io.OutLiveIn.elements("field22")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_sub1026.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Gep_arrayidx3128.io.Out(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field5")(0)

  Loop_1.io.InLiveIn(6) <> Loop_2.io.OutLiveIn.elements("field6")(0)

  Loop_1.io.InLiveIn(7) <> Loop_2.io.OutLiveIn.elements("field7")(0)

  Loop_1.io.InLiveIn(8) <> Loop_2.io.OutLiveIn.elements("field8")(0)

  Loop_1.io.InLiveIn(9) <> Loop_2.io.OutLiveIn.elements("field9")(0)

  Loop_1.io.InLiveIn(10) <> Loop_2.io.OutLiveIn.elements("field10")(0)

  Loop_1.io.InLiveIn(11) <> Loop_2.io.OutLiveIn.elements("field11")(0)

  Loop_1.io.InLiveIn(12) <> Loop_2.io.OutLiveIn.elements("field12")(0)

  Loop_1.io.InLiveIn(13) <> Loop_2.io.OutLiveIn.elements("field13")(0)

  Loop_1.io.InLiveIn(14) <> Loop_2.io.OutLiveIn.elements("field14")(0)

  Loop_1.io.InLiveIn(15) <> Loop_2.io.OutLiveIn.elements("field15")(0)

  Loop_1.io.InLiveIn(16) <> Loop_2.io.OutLiveIn.elements("field16")(0)

  Loop_1.io.InLiveIn(17) <> Loop_2.io.OutLiveIn.elements("field17")(0)

  Loop_1.io.InLiveIn(18) <> Loop_2.io.OutLiveIn.elements("field19")(0)

  Loop_1.io.InLiveIn(19) <> Loop_2.io.OutLiveIn.elements("field20")(0)

  Loop_1.io.InLiveIn(20) <> Loop_2.io.OutLiveIn.elements("field21")(0)

  Loop_1.io.InLiveIn(21) <> Loop_2.io.OutLiveIn.elements("field22")(0)

  Loop_1.io.InLiveIn(22) <> Loop_2.io.OutLiveIn.elements("field23")(0)

  Loop_1.io.InLiveIn(23) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(24) <> Loop_2.io.OutLiveIn.elements("field18")(0)

  Loop_2.io.InLiveIn(0) <> binaryOp_add420.io.Out(0)

  Loop_2.io.InLiveIn(1) <> phi_lgxy_s1_y_021317.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field7")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field23")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field15")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field24")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field13")(0)

  Loop_2.io.InLiveIn(7) <> Loop_3.io.OutLiveIn.elements("field19")(0)

  Loop_2.io.InLiveIn(8) <> Loop_3.io.OutLiveIn.elements("field9")(0)

  Loop_2.io.InLiveIn(9) <> Loop_3.io.OutLiveIn.elements("field17")(0)

  Loop_2.io.InLiveIn(10) <> Loop_3.io.OutLiveIn.elements("field26")(0)

  Loop_2.io.InLiveIn(11) <> Loop_3.io.OutLiveIn.elements("field21")(0)

  Loop_2.io.InLiveIn(12) <> Loop_3.io.OutLiveIn.elements("field25")(0)

  Loop_2.io.InLiveIn(13) <> Loop_3.io.OutLiveIn.elements("field27")(0)

  Loop_2.io.InLiveIn(14) <> Loop_3.io.OutLiveIn.elements("field8")(0)

  Loop_2.io.InLiveIn(15) <> Loop_3.io.OutLiveIn.elements("field14")(0)

  Loop_2.io.InLiveIn(16) <> Loop_3.io.OutLiveIn.elements("field18")(0)

  Loop_2.io.InLiveIn(17) <> Loop_3.io.OutLiveIn.elements("field16")(0)

  Loop_2.io.InLiveIn(18) <> Loop_3.io.OutLiveIn.elements("field6")(0)

  Loop_2.io.InLiveIn(19) <> Loop_3.io.OutLiveIn.elements("field20")(0)

  Loop_2.io.InLiveIn(20) <> Loop_3.io.OutLiveIn.elements("field22")(0)

  Loop_2.io.InLiveIn(21) <> Loop_3.io.OutLiveIn.elements("field11")(0)

  Loop_2.io.InLiveIn(22) <> Loop_3.io.OutLiveIn.elements("field12")(0)

  Loop_2.io.InLiveIn(23) <> Loop_3.io.OutLiveIn.elements("field10")(0)

  Loop_2.io.InLiveIn(24) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(25) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_2.io.InLiveIn(26) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(27) <> Loop_3.io.OutLiveIn.elements("field28")(0)

  Loop_3.io.InLiveIn(0) <> binaryOp_add18.io.Out(0)

  Loop_3.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field4")(0)

  Loop_3.io.InLiveIn(2) <> binaryOp_sub6.io.Out(0)

  Loop_3.io.InLiveIn(3) <> binaryOp_add7.io.Out(0)

  Loop_3.io.InLiveIn(4) <> InputSplitter.io.Out.data.elements("field14")(0)

  Loop_3.io.InLiveIn(5) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_3.io.InLiveIn(6) <> InputSplitter.io.Out.data.elements("field5")(0)

  Loop_3.io.InLiveIn(7) <> InputSplitter.io.Out.data.elements("field11")(0)

  Loop_3.io.InLiveIn(8) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_3.io.InLiveIn(9) <> InputSplitter.io.Out.data.elements("field13")(0)

  Loop_3.io.InLiveIn(10) <> InputSplitter.io.Out.data.elements("field8")(0)

  Loop_3.io.InLiveIn(11) <> InputSplitter.io.Out.data.elements("field6")(0)

  Loop_3.io.InLiveIn(12) <> InputSplitter.io.Out.data.elements("field9")(0)

  Loop_3.io.InLiveIn(13) <> InputSplitter.io.Out.data.elements("field10")(0)

  Loop_3.io.InLiveIn(14) <> bitcast_9.io.Out(0)

  Loop_3.io.InLiveIn(15) <> alloca__3440.io.Out(0)

  Loop_3.io.InLiveIn(16) <> bitcast_10.io.Out(0)

  Loop_3.io.InLiveIn(17) <> alloca__3451.io.Out(0)

  Loop_3.io.InLiveIn(18) <> bitcast_11.io.Out(0)

  Loop_3.io.InLiveIn(19) <> alloca__3462.io.Out(0)

  Loop_3.io.InLiveIn(20) <> bitcast_12.io.Out(0)

  Loop_3.io.InLiveIn(21) <> alloca__3473.io.Out(0)

  Loop_3.io.InLiveIn(22) <> InputSplitter.io.Out.data.elements("field7")(0)

  Loop_3.io.InLiveIn(23) <> InputSplitter.io.Out.data.elements("field12")(0)

  Loop_3.io.InLiveIn(24) <> bitcast_13.io.Out(0)

  Loop_3.io.InLiveIn(25) <> alloca__3624.io.Out(0)

  Loop_3.io.InLiveIn(26) <> bitcast_14.io.Out(0)

  Loop_3.io.InLiveIn(27) <> alloca__3635.io.Out(0)

  Loop_3.io.InLiveIn(28) <> InputSplitter.io.Out.data.elements("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */
  InputSplitter.io.Out.data.elements("field0")(0).ready := true.B

  Loop_0.io.OutLiveIn.elements("field4")(0).ready := true.B
  Loop_0.io.OutLiveIn.elements("field9")(0).ready := true.B
  Loop_0.io.OutLiveIn.elements("field15")(0).ready := true.B
  Loop_0.io.OutLiveIn.elements("field16")(0).ready := true.B
  Loop_0.io.OutLiveIn.elements("field17")(0).ready := true.B
  Loop_0.io.OutLiveIn.elements("field18")(0).ready := true.B

  binaryOp_add2342.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add2443.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add8688.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  st_76.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field3")(0)

  call_78_out.io.In.elements("field0") <> Loop_0.io.OutLiveIn.elements("field3")(1)

  binaryOp_add4863.io.RightIO <> Loop_0.io.OutLiveIn.elements("field5")(0)

  st_79.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field6")(0)

  call_81_out.io.In.elements("field0") <> Loop_0.io.OutLiveIn.elements("field6")(1)

  binaryOp_add2546.io.RightIO <> Loop_0.io.OutLiveIn.elements("field7")(0)

  st_77.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field8")(0)

  call_78_out.io.In.elements("field1") <> Loop_0.io.OutLiveIn.elements("field8")(1)

  call_100_out.io.In.elements("field1") <> Loop_0.io.OutLiveIn.elements("field8")(2)

  st_80.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field10")(0)

  call_81_out.io.In.elements("field1") <> Loop_0.io.OutLiveIn.elements("field10")(1)

  call_102_out.io.In.elements("field1") <> Loop_0.io.OutLiveIn.elements("field10")(2)

  st_99.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field11")(0)

  call_100_out.io.In.elements("field0") <> Loop_0.io.OutLiveIn.elements("field11")(1)

  st_101.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field12")(0)

  call_102_out.io.In.elements("field0") <> Loop_0.io.OutLiveIn.elements("field12")(1)

  Gep_arrayidx44.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(0)

  Gep_arrayidx2647.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(1)

  Gep_arrayidx2850.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(2)

  Gep_arrayidx3053.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(3)

  Gep_arrayidx3357.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(4)

  Gep_arrayidx4964.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(5)

  Gep_arrayidx7284.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(6)

  Gep_arrayidx8789.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field13")(7)

  ld_55.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field14")(0)

  st_107.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field14")(1)

  binaryOp_add7183.io.RightIO <> Loop_0.io.OutLiveIn.elements("field19")(0)

  binaryOp_add2952.io.RightIO <> Loop_0.io.OutLiveIn.elements("field20")(0)

  binaryOp_add3256.io.RightIO <> Loop_0.io.OutLiveIn.elements("field21")(0)

  binaryOp_add2749.io.RightIO <> Loop_0.io.OutLiveIn.elements("field22")(0)

  binaryOp_add1836.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  binaryOp_add1634.io.RightIO <> Loop_1.io.OutLiveIn.elements("field23")(0)

  binaryOp_mul1735.io.RightIO <> Loop_1.io.OutLiveIn.elements("field24")(0)

  binaryOp_add1127.io.LeftIO <> Loop_2.io.OutLiveIn.elements("field0")(0)

  binaryOp_sub1026.io.RightIO <> Loop_2.io.OutLiveIn.elements("field24")(0)

  phi_lgxy_s1_x_021225.io.InData(0) <> Loop_2.io.OutLiveIn.elements("field25")(0)

  Gep_arrayidx3128.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field26")(0)

  icmp_cmp731.io.RightIO <> Loop_2.io.OutLiveIn.elements("field27")(0)

  phi_lgxy_s1_y_021317.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  binaryOp_sub318.io.RightIO <> Loop_3.io.OutLiveIn.elements("field1")(0)

  icmp_cmp23.io.RightIO <> Loop_3.io.OutLiveIn.elements("field1")(1)

  binaryOp_add420.io.LeftIO <> Loop_3.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc108.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc11238.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc11530.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_inc11822.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi_lgxy_s1_box__x_021041.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi_lgxy_s1_box__y_021133.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phi_lgxy_s1_x_021225.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phi_lgxy_s1_y_021317.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  const2.io.enable <> bb_entry0.io.Out(2)

  alloca__3440.io.enable <> bb_entry0.io.Out(3)


  alloca__3451.io.enable <> bb_entry0.io.Out(4)


  alloca__3462.io.enable <> bb_entry0.io.Out(5)


  alloca__3473.io.enable <> bb_entry0.io.Out(6)


  alloca__3624.io.enable <> bb_entry0.io.Out(7)


  alloca__3635.io.enable <> bb_entry0.io.Out(8)


  binaryOp_sub6.io.enable <> bb_entry0.io.Out(9)


  binaryOp_add7.io.enable <> bb_entry0.io.Out(10)


  binaryOp_add18.io.enable <> bb_entry0.io.Out(11)


  bitcast_9.io.enable <> bb_entry0.io.Out(12)


  bitcast_10.io.enable <> bb_entry0.io.Out(13)


  bitcast_11.io.enable <> bb_entry0.io.Out(14)


  bitcast_12.io.enable <> bb_entry0.io.Out(15)


  bitcast_13.io.enable <> bb_entry0.io.Out(16)


  bitcast_14.io.enable <> bb_entry0.io.Out(17)


  br_15.io.enable <> bb_entry0.io.Out(18)


  ret_16.io.In.enable <> bb_for_cond_cleanup1.io.Out(0)


  const3.io.enable <> bb_for_body2.io.Out(0)

  phi_lgxy_s1_y_021317.io.enable <> bb_for_body2.io.Out(1)


  binaryOp_sub318.io.enable <> bb_for_body2.io.Out(2)


  binaryOp_mul19.io.enable <> bb_for_body2.io.Out(3)


  binaryOp_add420.io.enable <> bb_for_body2.io.Out(4)


  br_21.io.enable <> bb_for_body2.io.Out(5)


  const4.io.enable <> bb_for_cond_cleanup83.io.Out(0)

  binaryOp_inc11822.io.enable <> bb_for_cond_cleanup83.io.Out(1)


  icmp_cmp23.io.enable <> bb_for_cond_cleanup83.io.Out(2)


  br_24.io.enable <> bb_for_cond_cleanup83.io.Out(3)


  phi_lgxy_s1_x_021225.io.enable <> bb_for_body94.io.Out(0)


  binaryOp_sub1026.io.enable <> bb_for_body94.io.Out(1)


  binaryOp_add1127.io.enable <> bb_for_body94.io.Out(2)


  Gep_arrayidx3128.io.enable <> bb_for_body94.io.Out(3)


  br_29.io.enable <> bb_for_body94.io.Out(4)


  const5.io.enable <> bb_for_cond_cleanup145.io.Out(0)

  binaryOp_inc11530.io.enable <> bb_for_cond_cleanup145.io.Out(1)


  icmp_cmp731.io.enable <> bb_for_cond_cleanup145.io.Out(2)


  br_32.io.enable <> bb_for_cond_cleanup145.io.Out(3)


  const6.io.enable <> bb_for_body156.io.Out(0)

  phi_lgxy_s1_box__y_021133.io.enable <> bb_for_body156.io.Out(1)


  binaryOp_add1634.io.enable <> bb_for_body156.io.Out(2)


  binaryOp_mul1735.io.enable <> bb_for_body156.io.Out(3)


  binaryOp_add1836.io.enable <> bb_for_body156.io.Out(4)


  br_37.io.enable <> bb_for_body156.io.Out(5)


  const7.io.enable <> bb_for_cond_cleanup217.io.Out(0)

  const8.io.enable <> bb_for_cond_cleanup217.io.Out(1)

  binaryOp_inc11238.io.enable <> bb_for_cond_cleanup217.io.Out(2)


  icmp_exitcond21439.io.enable <> bb_for_cond_cleanup217.io.Out(3)


  br_40.io.enable <> bb_for_cond_cleanup217.io.Out(4)


  const9.io.enable <> bb_for_body228.io.Out(0)

  const10.io.enable <> bb_for_body228.io.Out(1)

  const11.io.enable <> bb_for_body228.io.Out(2)

  const12.io.enable <> bb_for_body228.io.Out(3)

  const13.io.enable <> bb_for_body228.io.Out(4)

  const14.io.enable <> bb_for_body228.io.Out(5)

  const15.io.enable <> bb_for_body228.io.Out(6)

  const16.io.enable <> bb_for_body228.io.Out(7)

  const17.io.enable <> bb_for_body228.io.Out(8)

  const18.io.enable <> bb_for_body228.io.Out(9)

  phi_lgxy_s1_box__x_021041.io.enable <> bb_for_body228.io.Out(10)


  binaryOp_add2342.io.enable <> bb_for_body228.io.Out(11)


  binaryOp_add2443.io.enable <> bb_for_body228.io.Out(12)


  Gep_arrayidx44.io.enable <> bb_for_body228.io.Out(13)


  ld_45.io.enable <> bb_for_body228.io.Out(14)


  binaryOp_add2546.io.enable <> bb_for_body228.io.Out(15)


  Gep_arrayidx2647.io.enable <> bb_for_body228.io.Out(16)


  ld_48.io.enable <> bb_for_body228.io.Out(17)


  binaryOp_add2749.io.enable <> bb_for_body228.io.Out(18)


  Gep_arrayidx2850.io.enable <> bb_for_body228.io.Out(19)


  ld_51.io.enable <> bb_for_body228.io.Out(20)


  binaryOp_add2952.io.enable <> bb_for_body228.io.Out(21)


  Gep_arrayidx3053.io.enable <> bb_for_body228.io.Out(22)


  ld_54.io.enable <> bb_for_body228.io.Out(23)


  ld_55.io.enable <> bb_for_body228.io.Out(24)


  binaryOp_add3256.io.enable <> bb_for_body228.io.Out(25)


  Gep_arrayidx3357.io.enable <> bb_for_body228.io.Out(26)


  ld_58.io.enable <> bb_for_body228.io.Out(27)


  sextconv3459.io.enable <> bb_for_body228.io.Out(28)


  binaryOp_mul3660.io.enable <> bb_for_body228.io.Out(29)


  sextconv4061.io.enable <> bb_for_body228.io.Out(30)


  sextconv4562.io.enable <> bb_for_body228.io.Out(31)


  binaryOp_add4863.io.enable <> bb_for_body228.io.Out(32)


  Gep_arrayidx4964.io.enable <> bb_for_body228.io.Out(33)


  ld_65.io.enable <> bb_for_body228.io.Out(34)


  sextconv5166.io.enable <> bb_for_body228.io.Out(35)


  binaryOp_mul5367.io.enable <> bb_for_body228.io.Out(36)


  sextconv6168.io.enable <> bb_for_body228.io.Out(37)


  sextconv6669.io.enable <> bb_for_body228.io.Out(38)


  binaryOp_add4170.io.enable <> bb_for_body228.io.Out(39)


  binaryOp_sub4671.io.enable <> bb_for_body228.io.Out(40)


  binaryOp_sub5772.io.enable <> bb_for_body228.io.Out(41)


  binaryOp_sub6273.io.enable <> bb_for_body228.io.Out(42)


  binaryOp_add6774.io.enable <> bb_for_body228.io.Out(43)


  truncconv6875.io.enable <> bb_for_body228.io.Out(44)


  st_76.io.enable <> bb_for_body228.io.Out(45)


  st_77.io.enable <> bb_for_body228.io.Out(46)


  call_78_in.io.enable <> bb_for_body228.io.Out(48)

  call_78_out.io.enable <> bb_for_body228.io.Out(47)


  st_79.io.enable <> bb_for_body228.io.Out(49)


  st_80.io.enable <> bb_for_body228.io.Out(50)


  call_81_in.io.enable <> bb_for_body228.io.Out(52)

  call_81_out.io.enable <> bb_for_body228.io.Out(51)


  sextconv7082.io.enable <> bb_for_body228.io.Out(53)


  binaryOp_add7183.io.enable <> bb_for_body228.io.Out(54)


  Gep_arrayidx7284.io.enable <> bb_for_body228.io.Out(55)


  ld_85.io.enable <> bb_for_body228.io.Out(56)


  sextconv7486.io.enable <> bb_for_body228.io.Out(57)


  binaryOp_mul7687.io.enable <> bb_for_body228.io.Out(58)


  binaryOp_add8688.io.enable <> bb_for_body228.io.Out(59)


  Gep_arrayidx8789.io.enable <> bb_for_body228.io.Out(60)


  ld_90.io.enable <> bb_for_body228.io.Out(61)


  sextconv8991.io.enable <> bb_for_body228.io.Out(62)


  binaryOp_mul9192.io.enable <> bb_for_body228.io.Out(63)


  binaryOp_93.io.enable <> bb_for_body228.io.Out(64)


  binaryOp_sub8494.io.enable <> bb_for_body228.io.Out(65)


  binaryOp_sub9595.io.enable <> bb_for_body228.io.Out(66)


  binaryOp_add9996.io.enable <> bb_for_body228.io.Out(67)


  binaryOp_sub10397.io.enable <> bb_for_body228.io.Out(68)


  truncconv10498.io.enable <> bb_for_body228.io.Out(69)


  st_99.io.enable <> bb_for_body228.io.Out(70)


  call_100_in.io.enable <> bb_for_body228.io.Out(72)

  call_100_out.io.enable <> bb_for_body228.io.Out(71)


  st_101.io.enable <> bb_for_body228.io.Out(73)


  call_102_in.io.enable <> bb_for_body228.io.Out(75)

  call_102_out.io.enable <> bb_for_body228.io.Out(74)


  sextconv107103.io.enable <> bb_for_body228.io.Out(76)


  binaryOp_mul108104.io.enable <> bb_for_body228.io.Out(77)


  binaryOp_shr105.io.enable <> bb_for_body228.io.Out(78)


  binaryOp_add109106.io.enable <> bb_for_body228.io.Out(79)


  st_107.io.enable <> bb_for_body228.io.Out(80)


  binaryOp_inc108.io.enable <> bb_for_body228.io.Out(81)


  icmp_exitcond109.io.enable <> bb_for_body228.io.Out(82)


  br_110.io.enable <> bb_for_body228.io.Out(83)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_lgxy_s1_y_021317.io.Mask <> bb_for_body2.io.MaskBB(0)

  phi_lgxy_s1_x_021225.io.Mask <> bb_for_body94.io.MaskBB(0)

  phi_lgxy_s1_box__y_021133.io.Mask <> bb_for_body156.io.MaskBB(0)

  phi_lgxy_s1_box__x_021041.io.Mask <> bb_for_body228.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */

  alloca__3440.io.allocaInputIO.bits.size      := 1.U
  alloca__3440.io.allocaInputIO.bits.numByte   := 2.U
  alloca__3440.io.allocaInputIO.bits.predicate := true.B
  alloca__3440.io.allocaInputIO.bits.valid     := true.B
  alloca__3440.io.allocaInputIO.valid          := true.B



  alloca__3451.io.allocaInputIO.bits.size      := 1.U
  alloca__3451.io.allocaInputIO.bits.numByte   := 2.U
  alloca__3451.io.allocaInputIO.bits.predicate := true.B
  alloca__3451.io.allocaInputIO.bits.valid     := true.B
  alloca__3451.io.allocaInputIO.valid          := true.B



  alloca__3462.io.allocaInputIO.bits.size      := 1.U
  alloca__3462.io.allocaInputIO.bits.numByte   := 2.U
  alloca__3462.io.allocaInputIO.bits.predicate := true.B
  alloca__3462.io.allocaInputIO.bits.valid     := true.B
  alloca__3462.io.allocaInputIO.valid          := true.B



  alloca__3473.io.allocaInputIO.bits.size      := 1.U
  alloca__3473.io.allocaInputIO.bits.numByte   := 2.U
  alloca__3473.io.allocaInputIO.bits.predicate := true.B
  alloca__3473.io.allocaInputIO.bits.valid     := true.B
  alloca__3473.io.allocaInputIO.valid          := true.B



  alloca__3624.io.allocaInputIO.bits.size      := 1.U
  alloca__3624.io.allocaInputIO.bits.numByte   := 2.U
  alloca__3624.io.allocaInputIO.bits.predicate := true.B
  alloca__3624.io.allocaInputIO.bits.valid     := true.B
  alloca__3624.io.allocaInputIO.valid          := true.B



  alloca__3635.io.allocaInputIO.bits.size      := 1.U
  alloca__3635.io.allocaInputIO.bits.numByte   := 2.U
  alloca__3635.io.allocaInputIO.bits.predicate := true.B
  alloca__3635.io.allocaInputIO.bits.valid     := true.B
  alloca__3635.io.allocaInputIO.valid          := true.B





  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  StackPointer.io.InData(0) <> alloca__3440.io.allocaReqIO

  alloca__3440.io.allocaRespIO <> StackPointer.io.OutData(0)

  StackPointer.io.InData(1) <> alloca__3451.io.allocaReqIO

  alloca__3451.io.allocaRespIO <> StackPointer.io.OutData(1)

  StackPointer.io.InData(2) <> alloca__3462.io.allocaReqIO

  alloca__3462.io.allocaRespIO <> StackPointer.io.OutData(2)

  StackPointer.io.InData(3) <> alloca__3473.io.allocaReqIO

  alloca__3473.io.allocaRespIO <> StackPointer.io.OutData(3)

  StackPointer.io.InData(4) <> alloca__3624.io.allocaReqIO

  alloca__3624.io.allocaRespIO <> StackPointer.io.OutData(4)

  StackPointer.io.InData(5) <> alloca__3635.io.allocaReqIO

  alloca__3635.io.allocaRespIO <> StackPointer.io.OutData(5)

  MemCtrl.io.ReadIn(0) <> ld_45.io.memReq

  ld_45.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_48.io.memReq

  ld_48.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_51.io.memReq

  ld_51.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.ReadIn(3) <> ld_54.io.memReq

  ld_54.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.ReadIn(4) <> ld_55.io.memReq

  ld_55.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.ReadIn(5) <> ld_58.io.memReq

  ld_58.io.memResp <> MemCtrl.io.ReadOut(5)

  MemCtrl.io.ReadIn(6) <> ld_65.io.memReq

  ld_65.io.memResp <> MemCtrl.io.ReadOut(6)

  MemCtrl.io.WriteIn(0) <> st_76.io.memReq

  st_76.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.WriteIn(1) <> st_77.io.memReq

  st_77.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.WriteIn(2) <> st_79.io.memReq

  st_79.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.WriteIn(3) <> st_80.io.memReq

  st_80.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.ReadIn(7) <> ld_85.io.memReq

  ld_85.io.memResp <> MemCtrl.io.ReadOut(7)

  MemCtrl.io.ReadIn(8) <> ld_90.io.memReq

  ld_90.io.memResp <> MemCtrl.io.ReadOut(8)

  MemCtrl.io.WriteIn(4) <> st_99.io.memReq

  st_99.io.memResp <> MemCtrl.io.WriteOut(4)

  MemCtrl.io.WriteIn(5) <> st_101.io.memReq

  st_101.io.memResp <> MemCtrl.io.WriteOut(5)

  MemCtrl.io.WriteIn(6) <> st_107.io.memReq

  st_107.io.memResp <> MemCtrl.io.WriteOut(6)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_sub6.io.LeftIO <> const0.io.Out

  binaryOp_add7.io.RightIO <> const1.io.Out

  binaryOp_add18.io.RightIO <> const2.io.Out

  binaryOp_mul19.io.RightIO <> const3.io.Out

  binaryOp_inc11822.io.RightIO <> const4.io.Out

  binaryOp_inc11530.io.RightIO <> const5.io.Out

  phi_lgxy_s1_box__y_021133.io.InData(0) <> const6.io.Out

  binaryOp_inc11238.io.RightIO <> const7.io.Out

  icmp_exitcond21439.io.RightIO <> const8.io.Out

  phi_lgxy_s1_box__x_021041.io.InData(0) <> const9.io.Out

  binaryOp_mul3660.io.RightIO <> const10.io.Out

  binaryOp_mul5367.io.RightIO <> const11.io.Out

  st_77.io.inData <> const12.io.Out

  st_80.io.inData <> const13.io.Out

  binaryOp_mul7687.io.RightIO <> const14.io.Out

  binaryOp_mul9192.io.RightIO <> const15.io.Out

  binaryOp_shr105.io.RightIO <> const16.io.Out

  binaryOp_inc108.io.RightIO <> const17.io.Out

  icmp_exitcond109.io.RightIO <> const18.io.Out

  bitcast_9.io.Input <> alloca__3440.io.Out(1)

  bitcast_10.io.Input <> alloca__3451.io.Out(1)

  bitcast_11.io.Input <> alloca__3462.io.Out(1)

  bitcast_12.io.Input <> alloca__3473.io.Out(1)

  bitcast_13.io.Input <> alloca__3624.io.Out(1)

  bitcast_14.io.Input <> alloca__3635.io.Out(1)

  binaryOp_sub318.io.LeftIO <> phi_lgxy_s1_y_021317.io.Out(1)

  binaryOp_inc11822.io.LeftIO <> phi_lgxy_s1_y_021317.io.Out(2)

  icmp_cmp23.io.LeftIO <> phi_lgxy_s1_y_021317.io.Out(3)

  binaryOp_mul19.io.LeftIO <> binaryOp_sub318.io.Out(0)

  binaryOp_add420.io.RightIO <> binaryOp_mul19.io.Out(0)

  br_24.io.CmpIO <> icmp_cmp23.io.Out(0)

  binaryOp_sub1026.io.LeftIO <> phi_lgxy_s1_x_021225.io.Out(0)

  binaryOp_add1127.io.RightIO <> phi_lgxy_s1_x_021225.io.Out(1)

  binaryOp_inc11530.io.LeftIO <> phi_lgxy_s1_x_021225.io.Out(2)

  icmp_cmp731.io.LeftIO <> phi_lgxy_s1_x_021225.io.Out(3)

  Gep_arrayidx3128.io.idx(0) <> binaryOp_add1127.io.Out(0)

  br_32.io.CmpIO <> icmp_cmp731.io.Out(0)

  binaryOp_add1634.io.LeftIO <> phi_lgxy_s1_box__y_021133.io.Out(0)

  binaryOp_inc11238.io.LeftIO <> phi_lgxy_s1_box__y_021133.io.Out(1)

  binaryOp_mul1735.io.LeftIO <> binaryOp_add1634.io.Out(0)

  binaryOp_add1836.io.RightIO <> binaryOp_mul1735.io.Out(0)

  icmp_exitcond21439.io.LeftIO <> binaryOp_inc11238.io.Out(1)

  br_40.io.CmpIO <> icmp_exitcond21439.io.Out(0)

  binaryOp_add2342.io.RightIO <> phi_lgxy_s1_box__x_021041.io.Out(0)

  binaryOp_inc108.io.LeftIO <> phi_lgxy_s1_box__x_021041.io.Out(1)

  binaryOp_add2443.io.LeftIO <> binaryOp_add2342.io.Out(0)

  binaryOp_add2546.io.LeftIO <> binaryOp_add2342.io.Out(1)

  binaryOp_add2749.io.LeftIO <> binaryOp_add2342.io.Out(2)

  binaryOp_add2952.io.LeftIO <> binaryOp_add2342.io.Out(3)

  binaryOp_add3256.io.LeftIO <> binaryOp_add2342.io.Out(4)

  binaryOp_add4863.io.LeftIO <> binaryOp_add2342.io.Out(5)

  binaryOp_add7183.io.LeftIO <> binaryOp_add2342.io.Out(6)

  binaryOp_add8688.io.LeftIO <> binaryOp_add2342.io.Out(7)

  Gep_arrayidx44.io.idx(0) <> binaryOp_add2443.io.Out(0)

  ld_45.io.GepAddr <> Gep_arrayidx44.io.Out(0)

  sextconv4061.io.Input <> ld_45.io.Out(0)

  Gep_arrayidx2647.io.idx(0) <> binaryOp_add2546.io.Out(0)

  ld_48.io.GepAddr <> Gep_arrayidx2647.io.Out(0)

  sextconv4562.io.Input <> ld_48.io.Out(0)

  Gep_arrayidx2850.io.idx(0) <> binaryOp_add2749.io.Out(0)

  ld_51.io.GepAddr <> Gep_arrayidx2850.io.Out(0)

  sextconv6168.io.Input <> ld_51.io.Out(0)

  Gep_arrayidx3053.io.idx(0) <> binaryOp_add2952.io.Out(0)

  ld_54.io.GepAddr <> Gep_arrayidx3053.io.Out(0)

  sextconv6669.io.Input <> ld_54.io.Out(0)

  binaryOp_add109106.io.RightIO <> ld_55.io.Out(0)

  Gep_arrayidx3357.io.idx(0) <> binaryOp_add3256.io.Out(0)

  ld_58.io.GepAddr <> Gep_arrayidx3357.io.Out(0)

  sextconv3459.io.Input <> ld_58.io.Out(0)

  binaryOp_mul3660.io.LeftIO <> sextconv3459.io.Out(0)

  binaryOp_sub6273.io.RightIO <> binaryOp_mul3660.io.Out(0)

  binaryOp_add4170.io.LeftIO <> sextconv4061.io.Out(0)

  binaryOp_93.io.RightIO <> sextconv4061.io.Out(1)

  binaryOp_add4170.io.RightIO <> sextconv4562.io.Out(0)

  binaryOp_93.io.LeftIO <> sextconv4562.io.Out(1)

  Gep_arrayidx4964.io.idx(0) <> binaryOp_add4863.io.Out(0)

  ld_65.io.GepAddr <> Gep_arrayidx4964.io.Out(0)

  sextconv5166.io.Input <> ld_65.io.Out(0)

  binaryOp_mul5367.io.LeftIO <> sextconv5166.io.Out(0)

  binaryOp_add6774.io.RightIO <> binaryOp_mul5367.io.Out(0)

  binaryOp_sub4671.io.RightIO <> sextconv6168.io.Out(0)

  binaryOp_sub8494.io.LeftIO <> sextconv6168.io.Out(1)

  binaryOp_sub5772.io.RightIO <> sextconv6669.io.Out(0)

  binaryOp_sub9595.io.RightIO <> sextconv6669.io.Out(1)

  binaryOp_sub4671.io.LeftIO <> binaryOp_add4170.io.Out(0)

  binaryOp_sub5772.io.LeftIO <> binaryOp_sub4671.io.Out(0)

  binaryOp_sub6273.io.LeftIO <> binaryOp_sub5772.io.Out(0)

  binaryOp_add6774.io.LeftIO <> binaryOp_sub6273.io.Out(0)

  truncconv6875.io.Input <> binaryOp_add6774.io.Out(0)

  st_76.io.inData <> truncconv6875.io.Out(0)

  st_79.io.inData <> call_78_in.io.Out.data("field0")

  sextconv7082.io.Input <> call_81_in.io.Out.data("field0")

  binaryOp_mul108104.io.RightIO <> sextconv7082.io.Out(0)

  Gep_arrayidx7284.io.idx(0) <> binaryOp_add7183.io.Out(0)

  ld_85.io.GepAddr <> Gep_arrayidx7284.io.Out(0)

  sextconv7486.io.Input <> ld_85.io.Out(0)

  binaryOp_mul7687.io.LeftIO <> sextconv7486.io.Out(0)

  binaryOp_add9996.io.RightIO <> binaryOp_mul7687.io.Out(0)

  Gep_arrayidx8789.io.idx(0) <> binaryOp_add8688.io.Out(0)

  ld_90.io.GepAddr <> Gep_arrayidx8789.io.Out(0)

  sextconv8991.io.Input <> ld_90.io.Out(0)

  binaryOp_mul9192.io.LeftIO <> sextconv8991.io.Out(0)

  binaryOp_sub10397.io.RightIO <> binaryOp_mul9192.io.Out(0)

  binaryOp_sub8494.io.RightIO <> binaryOp_93.io.Out(0)

  binaryOp_sub9595.io.LeftIO <> binaryOp_sub8494.io.Out(0)

  binaryOp_add9996.io.LeftIO <> binaryOp_sub9595.io.Out(0)

  binaryOp_sub10397.io.LeftIO <> binaryOp_add9996.io.Out(0)

  truncconv10498.io.Input <> binaryOp_sub10397.io.Out(0)

  st_99.io.inData <> truncconv10498.io.Out(0)

  st_101.io.inData <> call_100_in.io.Out.data("field0")

  sextconv107103.io.Input <> call_102_in.io.Out.data("field0")

  binaryOp_mul108104.io.LeftIO <> sextconv107103.io.Out(0)

  binaryOp_shr105.io.LeftIO <> binaryOp_mul108104.io.Out(0)

  binaryOp_add109106.io.LeftIO <> binaryOp_shr105.io.Out(0)

  st_107.io.inData <> binaryOp_add109106.io.Out(0)

  icmp_exitcond109.io.LeftIO <> binaryOp_inc108.io.Out(1)

  br_110.io.CmpIO <> icmp_exitcond109.io.Out(0)

  binaryOp_sub6.io.RightIO <> InputSplitter.io.Out.data.elements("field3")(1)

  binaryOp_add7.io.LeftIO <> InputSplitter.io.Out.data.elements("field3")(2)

  binaryOp_add18.io.LeftIO <> InputSplitter.io.Out.data.elements("field4")(1)

  st_76.io.Out(0).ready := true.B

  st_77.io.Out(0).ready := true.B

  st_79.io.Out(0).ready := true.B

  st_80.io.Out(0).ready := true.B

  st_99.io.Out(0).ready := true.B

  st_101.io.Out(0).ready := true.B

  st_107.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_78_in.io.In <> io.call_78_in

  io.call_78_out <> call_78_out.io.Out(0)

  br_110.io.PredOp(0) <> call_78_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_81_in.io.In <> io.call_81_in

  io.call_81_out <> call_81_out.io.Out(0)

  br_110.io.PredOp(1) <> call_81_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_100_in.io.In <> io.call_100_in

  io.call_100_out <> call_100_out.io.Out(0)

  br_110.io.PredOp(2) <> call_100_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_102_in.io.In <> io.call_102_in

  io.call_102_out <> call_102_out.io.Out(0)

  br_110.io.PredOp(3) <> call_102_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  call_f3_in.io.enable <> bb_for_cond_cleanup1.io.Out(1)
  call_f3_out.io.enable <> bb_for_cond_cleanup1.io.Out(2)

  call_f3_in.io.In <> io.call_f3_in
  io.call_f3_out <> call_f3_out.io.Out(0)


  io.out <> ret_16.io.Out

}

import java.io.{File, FileWriter}

object extract_function_harris_f2Top extends App {
  val dir = new File("RTL/extract_function_harris_f2Top");
  dir.mkdirs
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new extract_function_harris_f2DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

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

abstract class extract_function_harrisDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32))))
    val call_73_out = Decoupled(new Call(List(32, 32)))
    val call_73_in = Flipped(Decoupled(new Call(List(32))))
    val call_76_out = Decoupled(new Call(List(32, 32)))
    val call_76_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class extract_function_harrisDF(implicit p: Parameters) extends extract_function_harrisDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 7, NWrites = 5)
  (WControl = new WriteMemoryController(NumOps = 5, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 7, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val StackPointer = Module(new Stack(NumOps = 4))

  val InputSplitter = Module(new SplitCallNew(List(0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 2, 2, 1, 2, 2, 2, 0, 0, 6, 0, 0, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 4))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 15, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 2))

  val bb_for_cond_cleanup83 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 3))

  val bb_for_body94 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup145 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_for_body156 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 6))

  val bb_for_cond_cleanup217 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 7))

  val bb_for_body228 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 58, NumPhi = 1, BID = 8))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %_289 = alloca i16, align 2
  val alloca__2890 = Module(new AllocaNode(NumOuts=2, ID = 0, RouteID=0))

  //  %_291 = alloca i16, align 2
  val alloca__2911 = Module(new AllocaNode(NumOuts=2, ID = 1, RouteID=1))

  //  %_292 = alloca i16, align 2
  val alloca__2922 = Module(new AllocaNode(NumOuts=2, ID = 2, RouteID=2))

  //  %_293 = alloca i16, align 2
  val alloca__2933 = Module(new AllocaNode(NumOuts=2, ID = 3, RouteID=3))

  //  %sub = sub i32 4, %_output_s0_x, !dbg !1200
  val binaryOp_sub4 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "sub")(sign = false))

  //  %add = add nsw i32 %_output_s0_x, -1, !dbg !1203
  val binaryOp_add5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false))

  //  %add1 = add nsw i32 %_output_s0_y, -1, !dbg !1206
  val binaryOp_add16 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign = false))

  //  %0 = bitcast i16* %_289 to i8*
  val bitcast_7 = Module(new BitCastNode(NumOuts = 1, ID = 7))

  //  %1 = bitcast i16* %_291 to i8*
  val bitcast_8 = Module(new BitCastNode(NumOuts = 1, ID = 8))

  //  %2 = bitcast i16* %_292 to i8*
  val bitcast_9 = Module(new BitCastNode(NumOuts = 1, ID = 9))

  //  %3 = bitcast i16* %_293 to i8*
  val bitcast_10 = Module(new BitCastNode(NumOuts = 1, ID = 10))

  //  br label %for.body, !dbg !1211, !BB_UID !1212
  val br_11 = Module(new UBranchNode(ID = 11))

  //  ret void, !dbg !1213, !BB_UID !1214
  val ret_12 = Module(new RetNode2(retTypes = List(), ID = 12))

  //  %_lgyy_s1_y.0158 = phi i32 [ %add1, %entry ], [ %inc81, %for.cond.cleanup8 ]
  val phi_lgyy_s1_y_015813 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 13, Res = true))

  //  %sub3 = sub nsw i32 %_lgyy_s1_y.0158, %_output_s0_y, !dbg !1216
  val binaryOp_sub314 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "sub")(sign = false))

  //  %mul = mul nsw i32 %sub3, 3, !dbg !1219
  val binaryOp_mul15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign = false))

  //  %add4 = add nsw i32 %sub, %mul, !dbg !1222
  val binaryOp_add416 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "add")(sign = false))

  //  br label %for.body9, !dbg !1227, !BB_UID !1228
  val br_17 = Module(new UBranchNode(ID = 17))

  //  %inc81 = add nsw i32 %_lgyy_s1_y.0158, 1, !dbg !1229
  val binaryOp_inc8118 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign = false))

  //  %cmp = icmp sgt i32 %_lgyy_s1_y.0158, %_output_s0_y, !dbg !1231
  val icmp_cmp19 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "ugt")(sign = false))

  //  br i1 %cmp, label %for.cond.cleanup, label %for.body, !dbg !1211, !llvm.loop !1232, !BB_UID !1234
  val br_20 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 20))

  //  %_lgyy_s1_x.0157 = phi i32 [ %add, %for.body ], [ %inc78, %for.cond.cleanup14 ]
  val phi_lgyy_s1_x_015721 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 21, Res = true))

  //  %sub10 = sub i32 %_lgyy_s1_x.0157, %_243, !dbg !1236
  val binaryOp_sub1022 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "sub")(sign = false))

  //  %add11 = add nsw i32 %add4, %_lgyy_s1_x.0157, !dbg !1239
  val binaryOp_add1123 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "add")(sign = false))

  //  %arrayidx68 = getelementptr inbounds i32, i32* %_lgyy, i32 %add11
  val Gep_arrayidx6824 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 24)(ElementSize = 4, ArraySize = List()))

  //  br label %for.body15, !dbg !1244, !BB_UID !1245
  val br_25 = Module(new UBranchNode(ID = 25))

  //  %inc78 = add nsw i32 %_lgyy_s1_x.0157, 1, !dbg !1246
  val binaryOp_inc7826 = Module(new ComputeNode(NumOuts = 1, ID = 26, opCode = "add")(sign = false))

  //  %cmp7 = icmp sgt i32 %_lgyy_s1_x.0157, %_output_s0_x, !dbg !1248
  val icmp_cmp727 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "ugt")(sign = false))

  //  br i1 %cmp7, label %for.cond.cleanup8, label %for.body9, !dbg !1227, !llvm.loop !1249, !BB_UID !1251
  val br_28 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 28))

  //  %_lgyy_s1_box__y.0156 = phi i32 [ -1, %for.body9 ], [ %inc75, %for.cond.cleanup21 ]
  val phi_lgyy_s1_box__y_015629 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 29, Res = true))

  //  %add16 = add nsw i32 %_lgyy_s1_box__y.0156, %_lgyy_s1_y.0158, !dbg !1253
  val binaryOp_add1630 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign = false))

  //  %mul17 = mul nsw i32 %add16, %_18, !dbg !1256
  val binaryOp_mul1731 = Module(new ComputeNode(NumOuts = 1, ID = 31, opCode = "mul")(sign = false))

  //  %add18 = add nsw i32 %sub10, %mul17, !dbg !1259
  val binaryOp_add1832 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "add")(sign = false))

  //  br label %for.body22, !dbg !1264, !BB_UID !1265
  val br_33 = Module(new UBranchNode(ID = 33))

  //  %inc75 = add nsw i32 %_lgyy_s1_box__y.0156, 1, !dbg !1266
  val binaryOp_inc7534 = Module(new ComputeNode(NumOuts = 2, ID = 34, opCode = "add")(sign = false))

  //  %exitcond159 = icmp eq i32 %inc75, 2, !dbg !1268
  val icmp_exitcond15935 = Module(new ComputeNode(NumOuts = 1, ID = 35, opCode = "eq")(sign = false))

  //  br i1 %exitcond159, label %for.cond.cleanup14, label %for.body15, !dbg !1244, !llvm.loop !1269, !BB_UID !1271
  val br_36 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 36))

  //  %_lgyy_s1_box__x.0155 = phi i32 [ -1, %for.body15 ], [ %inc, %for.body22 ]
  val phi_lgyy_s1_box__x_015537 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 37, Res = true))

  //  %add23 = add nsw i32 %add18, %_lgyy_s1_box__x.0155, !dbg !1273
  val binaryOp_add2338 = Module(new ComputeNode(NumOuts = 6, ID = 38, opCode = "add")(sign = false))

  //  %add24 = add nsw i32 %add23, %_235, !dbg !1276
  val binaryOp_add2439 = Module(new ComputeNode(NumOuts = 1, ID = 39, opCode = "add")(sign = false))

  //  %arrayidx = getelementptr inbounds i8, i8* %_input, i32 %add24, !dbg !1279
  val Gep_arrayidx40 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 40)(ElementSize = 1, ArraySize = List()))

  //  %4 = load i8, i8* %arrayidx, align 1, !dbg !1279, !tbaa !1280
  val ld_41 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 41, RouteID = 0))

  //  %conv25 = zext i8 %4 to i16, !dbg !1287
  val sextconv2542 = Module(new ZextNode(NumOuts = 1))

  //  %mul27 = shl nuw nsw i16 %conv25, 1, !dbg !1288
  val binaryOp_mul2743 = Module(new ComputeNode(NumOuts = 1, ID = 43, opCode = "shl")(sign = false))

  //  %add29 = add nsw i32 %add23, %_236, !dbg !1289
  val binaryOp_add2944 = Module(new ComputeNode(NumOuts = 1, ID = 44, opCode = "add")(sign = false))

  //  %arrayidx30 = getelementptr inbounds i8, i8* %_input, i32 %add29, !dbg !1292
  val Gep_arrayidx3045 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 45)(ElementSize = 1, ArraySize = List()))

  //  %5 = load i8, i8* %arrayidx30, align 1, !dbg !1292, !tbaa !1280
  val ld_46 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 46, RouteID = 1))

  //  %conv33 = zext i8 %5 to i16, !dbg !1295
  val sextconv3347 = Module(new ZextNode(NumOuts = 1))

  //  %add34 = add nuw nsw i16 %mul27, %conv33, !dbg !1296
  val binaryOp_add3448 = Module(new ComputeNode(NumOuts = 1, ID = 48, opCode = "add")(sign = false))

  //  %add36 = add nsw i32 %add23, %_241, !dbg !1297
  val binaryOp_add3649 = Module(new ComputeNode(NumOuts = 1, ID = 49, opCode = "add")(sign = false))

  //  %arrayidx37 = getelementptr inbounds i8, i8* %_input, i32 %add36, !dbg !1300
  val Gep_arrayidx3750 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 50)(ElementSize = 1, ArraySize = List()))

  //  %6 = load i8, i8* %arrayidx37, align 1, !dbg !1300, !tbaa !1280
  val ld_51 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 51, RouteID = 2))

  //  %conv40 = zext i8 %6 to i16, !dbg !1303
  val sextconv4052 = Module(new ZextNode(NumOuts = 1))

  //  %sub41 = sub nsw i16 %add34, %conv40, !dbg !1304
  val binaryOp_sub4153 = Module(new ComputeNode(NumOuts = 1, ID = 53, opCode = "sub")(sign = false))

  //  %add43 = add nsw i32 %add23, %_240, !dbg !1307
  val binaryOp_add4354 = Module(new ComputeNode(NumOuts = 1, ID = 54, opCode = "add")(sign = false))

  //  %arrayidx44 = getelementptr inbounds i8, i8* %_input, i32 %add43, !dbg !1310
  val Gep_arrayidx4455 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 55)(ElementSize = 1, ArraySize = List()))

  //  %7 = load i8, i8* %arrayidx44, align 1, !dbg !1310, !tbaa !1280
  val ld_56 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 56, RouteID = 3))

  //  %conv46 = zext i8 %7 to i16, !dbg !1313
  val sextconv4657 = Module(new ZextNode(NumOuts = 1))

  //  %mul48 = shl nuw nsw i16 %conv46, 1, !dbg !1314
  val binaryOp_mul4858 = Module(new ComputeNode(NumOuts = 1, ID = 58, opCode = "shl")(sign = false))

  //  %sub52 = sub nsw i16 %sub41, %mul48, !dbg !1315
  val binaryOp_sub5259 = Module(new ComputeNode(NumOuts = 1, ID = 59, opCode = "sub")(sign = false))

  //  %add54 = add nsw i32 %add23, %_234, !dbg !1318
  val binaryOp_add5460 = Module(new ComputeNode(NumOuts = 1, ID = 60, opCode = "add")(sign = false))

  //  %arrayidx55 = getelementptr inbounds i8, i8* %_input, i32 %add54, !dbg !1321
  val Gep_arrayidx5561 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 61)(ElementSize = 1, ArraySize = List()))

  //  %8 = load i8, i8* %arrayidx55, align 1, !dbg !1321, !tbaa !1280
  val ld_62 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 62, RouteID = 4))

  //  %conv58 = zext i8 %8 to i16, !dbg !1324
  val sextconv5863 = Module(new ZextNode(NumOuts = 1))

  //  %add59 = add nsw i16 %sub52, %conv58, !dbg !1325
  val binaryOp_add5964 = Module(new ComputeNode(NumOuts = 1, ID = 64, opCode = "add")(sign = false))

  //  %add61 = add nsw i32 %add23, %_239, !dbg !1328
  val binaryOp_add6165 = Module(new ComputeNode(NumOuts = 1, ID = 65, opCode = "add")(sign = false))

  //  %arrayidx62 = getelementptr inbounds i8, i8* %_input, i32 %add61, !dbg !1331
  val Gep_arrayidx6266 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 66)(ElementSize = 1, ArraySize = List()))

  //  %9 = load i8, i8* %arrayidx62, align 1, !dbg !1331, !tbaa !1280
  val ld_67 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 67, RouteID = 5))

  //  %conv65 = zext i8 %9 to i16, !dbg !1336
  val sextconv6568 = Module(new ZextNode(NumOuts = 1))

  //  %sub66 = sub nsw i16 %add59, %conv65, !dbg !1337
  val binaryOp_sub6669 = Module(new ComputeNode(NumOuts = 1, ID = 69, opCode = "sub")(sign = false))

  //  store i16 %sub66, i16* %_289, align 2, !dbg !1338, !tbaa !1340
  val st_70 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 70, RouteID = 0))

  //  %10 = load i32, i32* %arrayidx68, align 4, !dbg !1342, !tbaa !1343
  val ld_71 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 71, RouteID = 6))

  //  store i16 255, i16* %_291, align 2, !dbg !1349, !tbaa !1340
  val st_72 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 72, RouteID = 1))

  //  %call = call signext i16 @_Z14halide_cpp_minIsET_RKS0_S2_(i16* nonnull dereferenceable(2) %_289, i16* nonnull dereferenceable(2) %_291), !dbg !1355, !UID !1356
  val call_73_out = Module(new CallOutNode(ID = 73, NumSuccOps = 0, argTypes = List(32,32)))

  val call_73_in = Module(new CallInNode(ID = 73, argTypes = List(32)))

  //  store i16 %call, i16* %_292, align 2, !dbg !1357, !tbaa !1340
  val st_74 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 74, RouteID = 2))

  //  store i16 -255, i16* %_293, align 2, !dbg !1361, !tbaa !1340
  val st_75 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 75, RouteID = 3))

  //  %call69 = call signext i16 @_Z14halide_cpp_maxIsET_RKS0_S2_(i16* nonnull dereferenceable(2) %_292, i16* nonnull dereferenceable(2) %_293), !dbg !1365, !UID !1366
  val call_76_out = Module(new CallOutNode(ID = 76, NumSuccOps = 0, argTypes = List(32,32)))

  val call_76_in = Module(new CallInNode(ID = 76, argTypes = List(32)))

  //  %conv70 = sext i16 %call69 to i32, !dbg !1369
  val sextconv7077 = Module(new SextNode(NumOuts = 2))

  //  %mul71 = mul nsw i32 %conv70, %conv70, !dbg !1372
  val binaryOp_mul7178 = Module(new ComputeNode(NumOuts = 1, ID = 78, opCode = "mul")(sign = false))

  //  %11 = lshr i32 %mul71, 7, !dbg !1375
  val binaryOp_79 = Module(new ComputeNode(NumOuts = 1, ID = 79, opCode = "lshr")(sign = false))

  //  %add72 = add nsw i32 %11, %10, !dbg !1378
  val binaryOp_add7280 = Module(new ComputeNode(NumOuts = 1, ID = 80, opCode = "add")(sign = false))

  //  store i32 %add72, i32* %arrayidx68, align 4, !dbg !1381, !tbaa !1343
  val st_81 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 81, RouteID = 4))

  //  %inc = add nsw i32 %_lgyy_s1_box__x.0155, 1, !dbg !1387
  val binaryOp_inc82 = Module(new ComputeNode(NumOuts = 2, ID = 82, opCode = "add")(sign = false))

  //  %exitcond = icmp eq i32 %inc, 2, !dbg !1389
  val icmp_exitcond83 = Module(new ComputeNode(NumOuts = 1, ID = 83, opCode = "eq")(sign = false))

  //  br i1 %exitcond, label %for.cond.cleanup21, label %for.body22, !dbg !1264, !llvm.loop !1390, !BB_UID !1392
  val br_84 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 2, ID = 84))



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

  //i16 1
  val const10 = Module(new ConstFastNode(value = 1, ID = 10))

  //i16 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i16 255
  val const12 = Module(new ConstFastNode(value = 255, ID = 12))

  //i16 -255
  val const13 = Module(new ConstFastNode(value = -255, ID = 13))

  //i32 7
  val const14 = Module(new ConstFastNode(value = 7, ID = 14))

  //i32 1
  val const15 = Module(new ConstFastNode(value = 1, ID = 15))

  //i32 2
  val const16 = Module(new ConstFastNode(value = 2, ID = 16))



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

  Loop_0.io.enable <> br_33.io.Out(0)

  Loop_0.io.loopBack(0) <> br_84.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_84.io.TrueOutput(0)

  Loop_1.io.enable <> br_25.io.Out(0)

  Loop_1.io.loopBack(0) <> br_36.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_36.io.TrueOutput(0)

  Loop_2.io.enable <> br_17.io.Out(0)

  Loop_2.io.loopBack(0) <> br_28.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_28.io.TrueOutput(0)

  Loop_3.io.enable <> br_11.io.Out(0)

  Loop_3.io.loopBack(0) <> br_20.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_20.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_add1832.io.Out(0)

  Loop_0.io.InLiveIn(1) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field4")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field5")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(6) <> Loop_1.io.OutLiveIn.elements("field6")(0)

  Loop_0.io.InLiveIn(7) <> Loop_1.io.OutLiveIn.elements("field7")(0)

  Loop_0.io.InLiveIn(8) <> Loop_1.io.OutLiveIn.elements("field8")(0)

  Loop_0.io.InLiveIn(9) <> Loop_1.io.OutLiveIn.elements("field9")(0)

  Loop_0.io.InLiveIn(10) <> Loop_1.io.OutLiveIn.elements("field10")(0)

  Loop_0.io.InLiveIn(11) <> Loop_1.io.OutLiveIn.elements("field11")(0)

  Loop_0.io.InLiveIn(12) <> Loop_1.io.OutLiveIn.elements("field12")(0)

  Loop_0.io.InLiveIn(13) <> Loop_1.io.OutLiveIn.elements("field13")(0)

  Loop_0.io.InLiveIn(14) <> Loop_1.io.OutLiveIn.elements("field14")(0)

  Loop_0.io.InLiveIn(15) <> Loop_1.io.OutLiveIn.elements("field15")(0)

  Loop_0.io.InLiveIn(16) <> Loop_1.io.OutLiveIn.elements("field16")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_sub1022.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Gep_arrayidx6824.io.Out(0)

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

  Loop_1.io.InLiveIn(13) <> Loop_2.io.OutLiveIn.elements("field14")(0)

  Loop_1.io.InLiveIn(14) <> Loop_2.io.OutLiveIn.elements("field15")(0)

  Loop_1.io.InLiveIn(15) <> Loop_2.io.OutLiveIn.elements("field16")(0)

  Loop_1.io.InLiveIn(16) <> Loop_2.io.OutLiveIn.elements("field17")(0)

  Loop_1.io.InLiveIn(17) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(18) <> Loop_2.io.OutLiveIn.elements("field13")(0)

  Loop_2.io.InLiveIn(0) <> binaryOp_add416.io.Out(0)

  Loop_2.io.InLiveIn(1) <> phi_lgyy_s1_y_015813.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field11")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field17")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field15")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field10")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field19")(0)

  Loop_2.io.InLiveIn(7) <> Loop_3.io.OutLiveIn.elements("field21")(0)

  Loop_2.io.InLiveIn(8) <> Loop_3.io.OutLiveIn.elements("field14")(0)

  Loop_2.io.InLiveIn(9) <> Loop_3.io.OutLiveIn.elements("field16")(0)

  Loop_2.io.InLiveIn(10) <> Loop_3.io.OutLiveIn.elements("field8")(0)

  Loop_2.io.InLiveIn(11) <> Loop_3.io.OutLiveIn.elements("field18")(0)

  Loop_2.io.InLiveIn(12) <> Loop_3.io.OutLiveIn.elements("field20")(0)

  Loop_2.io.InLiveIn(13) <> Loop_3.io.OutLiveIn.elements("field6")(0)

  Loop_2.io.InLiveIn(14) <> Loop_3.io.OutLiveIn.elements("field7")(0)

  Loop_2.io.InLiveIn(15) <> Loop_3.io.OutLiveIn.elements("field12")(0)

  Loop_2.io.InLiveIn(16) <> Loop_3.io.OutLiveIn.elements("field13")(0)

  Loop_2.io.InLiveIn(17) <> Loop_3.io.OutLiveIn.elements("field9")(0)

  Loop_2.io.InLiveIn(18) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(19) <> Loop_3.io.OutLiveIn.elements("field22")(0)

  Loop_2.io.InLiveIn(20) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_2.io.InLiveIn(21) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_3.io.InLiveIn(0) <> binaryOp_add16.io.Out(0)

  Loop_3.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field12")(0)

  Loop_3.io.InLiveIn(2) <> binaryOp_sub4.io.Out(0)

  Loop_3.io.InLiveIn(3) <> binaryOp_add5.io.Out(0)

  Loop_3.io.InLiveIn(4) <> InputSplitter.io.Out.data.elements("field10")(0)

  Loop_3.io.InLiveIn(5) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_3.io.InLiveIn(6) <> InputSplitter.io.Out.data.elements("field3")(0)

  Loop_3.io.InLiveIn(7) <> InputSplitter.io.Out.data.elements("field5")(0)

  Loop_3.io.InLiveIn(8) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_3.io.InLiveIn(9) <> InputSplitter.io.Out.data.elements("field6")(0)

  Loop_3.io.InLiveIn(10) <> InputSplitter.io.Out.data.elements("field9")(0)

  Loop_3.io.InLiveIn(11) <> InputSplitter.io.Out.data.elements("field8")(0)

  Loop_3.io.InLiveIn(12) <> InputSplitter.io.Out.data.elements("field4")(0)

  Loop_3.io.InLiveIn(13) <> InputSplitter.io.Out.data.elements("field7")(0)

  Loop_3.io.InLiveIn(14) <> bitcast_7.io.Out(0)

  Loop_3.io.InLiveIn(15) <> alloca__2890.io.Out(0)

  Loop_3.io.InLiveIn(16) <> bitcast_8.io.Out(0)

  Loop_3.io.InLiveIn(17) <> alloca__2911.io.Out(0)

  Loop_3.io.InLiveIn(18) <> bitcast_9.io.Out(0)

  Loop_3.io.InLiveIn(19) <> alloca__2922.io.Out(0)

  Loop_3.io.InLiveIn(20) <> bitcast_10.io.Out(0)

  Loop_3.io.InLiveIn(21) <> alloca__2933.io.Out(0)

  Loop_3.io.InLiveIn(22) <> InputSplitter.io.Out.data.elements("field11")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add2338.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add4354.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  st_72.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field2")(0)

  call_73_out.io.In.elements("field1") <> Loop_0.io.OutLiveIn.elements("field2")(1)

  st_70.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field3")(0)

  call_73_out.io.In.elements("field0") <> Loop_0.io.OutLiveIn.elements("field3")(1)

  binaryOp_add3649.io.RightIO <> Loop_0.io.OutLiveIn.elements("field4")(0)

  ld_71.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field5")(0)

  st_81.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field5")(1)

  st_74.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field6")(0)

  call_76_out.io.In.elements("field0") <> Loop_0.io.OutLiveIn.elements("field6")(1)

  st_75.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field7")(0)

  call_76_out.io.In.elements("field1") <> Loop_0.io.OutLiveIn.elements("field7")(1)

  Gep_arrayidx40.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(0)

  Gep_arrayidx3045.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(1)

  Gep_arrayidx3750.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(2)

  Gep_arrayidx4455.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(3)

  Gep_arrayidx5561.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(4)

  Gep_arrayidx6266.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(5)

  binaryOp_add2439.io.RightIO <> Loop_0.io.OutLiveIn.elements("field13")(0)

  binaryOp_add5460.io.RightIO <> Loop_0.io.OutLiveIn.elements("field14")(0)

  binaryOp_add6165.io.RightIO <> Loop_0.io.OutLiveIn.elements("field15")(0)

  binaryOp_add2944.io.RightIO <> Loop_0.io.OutLiveIn.elements("field16")(0)

  binaryOp_add1832.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  binaryOp_add1630.io.RightIO <> Loop_1.io.OutLiveIn.elements("field17")(0)

  binaryOp_mul1731.io.RightIO <> Loop_1.io.OutLiveIn.elements("field18")(0)

  binaryOp_add1123.io.LeftIO <> Loop_2.io.OutLiveIn.elements("field0")(0)

  binaryOp_sub1022.io.RightIO <> Loop_2.io.OutLiveIn.elements("field18")(0)

  icmp_cmp727.io.RightIO <> Loop_2.io.OutLiveIn.elements("field19")(0)

  phi_lgyy_s1_x_015721.io.InData(0) <> Loop_2.io.OutLiveIn.elements("field20")(0)

  Gep_arrayidx6824.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field21")(0)

  phi_lgyy_s1_y_015813.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  binaryOp_sub314.io.RightIO <> Loop_3.io.OutLiveIn.elements("field1")(0)

  icmp_cmp19.io.RightIO <> Loop_3.io.OutLiveIn.elements("field1")(1)

  binaryOp_add416.io.LeftIO <> Loop_3.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc82.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc7534.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc7826.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_inc8118.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi_lgyy_s1_box__x_015537.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi_lgyy_s1_box__y_015629.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phi_lgyy_s1_x_015721.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phi_lgyy_s1_y_015813.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  const2.io.enable <> bb_entry0.io.Out(2)

  alloca__2890.io.enable <> bb_entry0.io.Out(3)


  alloca__2911.io.enable <> bb_entry0.io.Out(4)


  alloca__2922.io.enable <> bb_entry0.io.Out(5)


  alloca__2933.io.enable <> bb_entry0.io.Out(6)


  binaryOp_sub4.io.enable <> bb_entry0.io.Out(7)


  binaryOp_add5.io.enable <> bb_entry0.io.Out(8)


  binaryOp_add16.io.enable <> bb_entry0.io.Out(9)


  bitcast_7.io.enable <> bb_entry0.io.Out(10)


  bitcast_8.io.enable <> bb_entry0.io.Out(11)


  bitcast_9.io.enable <> bb_entry0.io.Out(12)


  bitcast_10.io.enable <> bb_entry0.io.Out(13)


  br_11.io.enable <> bb_entry0.io.Out(14)


  ret_12.io.In.enable <> bb_for_cond_cleanup1.io.Out(0)


  const3.io.enable <> bb_for_body2.io.Out(0)

  phi_lgyy_s1_y_015813.io.enable <> bb_for_body2.io.Out(1)


  binaryOp_sub314.io.enable <> bb_for_body2.io.Out(2)


  binaryOp_mul15.io.enable <> bb_for_body2.io.Out(3)


  binaryOp_add416.io.enable <> bb_for_body2.io.Out(4)


  br_17.io.enable <> bb_for_body2.io.Out(5)


  const4.io.enable <> bb_for_cond_cleanup83.io.Out(0)

  binaryOp_inc8118.io.enable <> bb_for_cond_cleanup83.io.Out(1)


  icmp_cmp19.io.enable <> bb_for_cond_cleanup83.io.Out(2)


  br_20.io.enable <> bb_for_cond_cleanup83.io.Out(3)


  phi_lgyy_s1_x_015721.io.enable <> bb_for_body94.io.Out(0)


  binaryOp_sub1022.io.enable <> bb_for_body94.io.Out(1)


  binaryOp_add1123.io.enable <> bb_for_body94.io.Out(2)


  Gep_arrayidx6824.io.enable <> bb_for_body94.io.Out(3)


  br_25.io.enable <> bb_for_body94.io.Out(4)


  const5.io.enable <> bb_for_cond_cleanup145.io.Out(0)

  binaryOp_inc7826.io.enable <> bb_for_cond_cleanup145.io.Out(1)


  icmp_cmp727.io.enable <> bb_for_cond_cleanup145.io.Out(2)


  br_28.io.enable <> bb_for_cond_cleanup145.io.Out(3)


  const6.io.enable <> bb_for_body156.io.Out(0)

  phi_lgyy_s1_box__y_015629.io.enable <> bb_for_body156.io.Out(1)


  binaryOp_add1630.io.enable <> bb_for_body156.io.Out(2)


  binaryOp_mul1731.io.enable <> bb_for_body156.io.Out(3)


  binaryOp_add1832.io.enable <> bb_for_body156.io.Out(4)


  br_33.io.enable <> bb_for_body156.io.Out(5)


  const7.io.enable <> bb_for_cond_cleanup217.io.Out(0)

  const8.io.enable <> bb_for_cond_cleanup217.io.Out(1)

  binaryOp_inc7534.io.enable <> bb_for_cond_cleanup217.io.Out(2)


  icmp_exitcond15935.io.enable <> bb_for_cond_cleanup217.io.Out(3)


  br_36.io.enable <> bb_for_cond_cleanup217.io.Out(4)


  const9.io.enable <> bb_for_body228.io.Out(0)

  const10.io.enable <> bb_for_body228.io.Out(1)

  const11.io.enable <> bb_for_body228.io.Out(2)

  const12.io.enable <> bb_for_body228.io.Out(3)

  const13.io.enable <> bb_for_body228.io.Out(4)

  const14.io.enable <> bb_for_body228.io.Out(5)

  const15.io.enable <> bb_for_body228.io.Out(6)

  const16.io.enable <> bb_for_body228.io.Out(7)

  phi_lgyy_s1_box__x_015537.io.enable <> bb_for_body228.io.Out(8)


  binaryOp_add2338.io.enable <> bb_for_body228.io.Out(9)


  binaryOp_add2439.io.enable <> bb_for_body228.io.Out(10)


  Gep_arrayidx40.io.enable <> bb_for_body228.io.Out(11)


  ld_41.io.enable <> bb_for_body228.io.Out(12)


  sextconv2542.io.enable <> bb_for_body228.io.Out(13)


  binaryOp_mul2743.io.enable <> bb_for_body228.io.Out(14)


  binaryOp_add2944.io.enable <> bb_for_body228.io.Out(15)


  Gep_arrayidx3045.io.enable <> bb_for_body228.io.Out(16)


  ld_46.io.enable <> bb_for_body228.io.Out(17)


  sextconv3347.io.enable <> bb_for_body228.io.Out(18)


  binaryOp_add3448.io.enable <> bb_for_body228.io.Out(19)


  binaryOp_add3649.io.enable <> bb_for_body228.io.Out(20)


  Gep_arrayidx3750.io.enable <> bb_for_body228.io.Out(21)


  ld_51.io.enable <> bb_for_body228.io.Out(22)


  sextconv4052.io.enable <> bb_for_body228.io.Out(23)


  binaryOp_sub4153.io.enable <> bb_for_body228.io.Out(24)


  binaryOp_add4354.io.enable <> bb_for_body228.io.Out(25)


  Gep_arrayidx4455.io.enable <> bb_for_body228.io.Out(26)


  ld_56.io.enable <> bb_for_body228.io.Out(27)


  sextconv4657.io.enable <> bb_for_body228.io.Out(28)


  binaryOp_mul4858.io.enable <> bb_for_body228.io.Out(29)


  binaryOp_sub5259.io.enable <> bb_for_body228.io.Out(30)


  binaryOp_add5460.io.enable <> bb_for_body228.io.Out(31)


  Gep_arrayidx5561.io.enable <> bb_for_body228.io.Out(32)


  ld_62.io.enable <> bb_for_body228.io.Out(33)


  sextconv5863.io.enable <> bb_for_body228.io.Out(34)


  binaryOp_add5964.io.enable <> bb_for_body228.io.Out(35)


  binaryOp_add6165.io.enable <> bb_for_body228.io.Out(36)


  Gep_arrayidx6266.io.enable <> bb_for_body228.io.Out(37)


  ld_67.io.enable <> bb_for_body228.io.Out(38)


  sextconv6568.io.enable <> bb_for_body228.io.Out(39)


  binaryOp_sub6669.io.enable <> bb_for_body228.io.Out(40)


  st_70.io.enable <> bb_for_body228.io.Out(41)


  ld_71.io.enable <> bb_for_body228.io.Out(42)


  st_72.io.enable <> bb_for_body228.io.Out(43)


  call_73_in.io.enable <> bb_for_body228.io.Out(45)

  call_73_out.io.enable <> bb_for_body228.io.Out(44)


  st_74.io.enable <> bb_for_body228.io.Out(46)


  st_75.io.enable <> bb_for_body228.io.Out(47)


  call_76_in.io.enable <> bb_for_body228.io.Out(49)

  call_76_out.io.enable <> bb_for_body228.io.Out(48)


  sextconv7077.io.enable <> bb_for_body228.io.Out(50)


  binaryOp_mul7178.io.enable <> bb_for_body228.io.Out(51)


  binaryOp_79.io.enable <> bb_for_body228.io.Out(52)


  binaryOp_add7280.io.enable <> bb_for_body228.io.Out(53)


  st_81.io.enable <> bb_for_body228.io.Out(54)


  binaryOp_inc82.io.enable <> bb_for_body228.io.Out(55)


  icmp_exitcond83.io.enable <> bb_for_body228.io.Out(56)


  br_84.io.enable <> bb_for_body228.io.Out(57)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_lgyy_s1_y_015813.io.Mask <> bb_for_body2.io.MaskBB(0)

  phi_lgyy_s1_x_015721.io.Mask <> bb_for_body94.io.MaskBB(0)

  phi_lgyy_s1_box__y_015629.io.Mask <> bb_for_body156.io.MaskBB(0)

  phi_lgyy_s1_box__x_015537.io.Mask <> bb_for_body228.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */

  alloca__2890.io.allocaInputIO.bits.size      := 1.U
  alloca__2890.io.allocaInputIO.bits.numByte   := 2.U
  alloca__2890.io.allocaInputIO.bits.predicate := true.B
  alloca__2890.io.allocaInputIO.bits.valid     := true.B
  alloca__2890.io.allocaInputIO.valid          := true.B



  alloca__2911.io.allocaInputIO.bits.size      := 1.U
  alloca__2911.io.allocaInputIO.bits.numByte   := 2.U
  alloca__2911.io.allocaInputIO.bits.predicate := true.B
  alloca__2911.io.allocaInputIO.bits.valid     := true.B
  alloca__2911.io.allocaInputIO.valid          := true.B



  alloca__2922.io.allocaInputIO.bits.size      := 1.U
  alloca__2922.io.allocaInputIO.bits.numByte   := 2.U
  alloca__2922.io.allocaInputIO.bits.predicate := true.B
  alloca__2922.io.allocaInputIO.bits.valid     := true.B
  alloca__2922.io.allocaInputIO.valid          := true.B



  alloca__2933.io.allocaInputIO.bits.size      := 1.U
  alloca__2933.io.allocaInputIO.bits.numByte   := 2.U
  alloca__2933.io.allocaInputIO.bits.predicate := true.B
  alloca__2933.io.allocaInputIO.bits.valid     := true.B
  alloca__2933.io.allocaInputIO.valid          := true.B





  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  StackPointer.io.InData(0) <> alloca__2890.io.allocaReqIO

  alloca__2890.io.allocaRespIO <> StackPointer.io.OutData(0)

  StackPointer.io.InData(1) <> alloca__2911.io.allocaReqIO

  alloca__2911.io.allocaRespIO <> StackPointer.io.OutData(1)

  StackPointer.io.InData(2) <> alloca__2922.io.allocaReqIO

  alloca__2922.io.allocaRespIO <> StackPointer.io.OutData(2)

  StackPointer.io.InData(3) <> alloca__2933.io.allocaReqIO

  alloca__2933.io.allocaRespIO <> StackPointer.io.OutData(3)

  MemCtrl.io.ReadIn(0) <> ld_41.io.memReq

  ld_41.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_46.io.memReq

  ld_46.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_51.io.memReq

  ld_51.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.ReadIn(3) <> ld_56.io.memReq

  ld_56.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.ReadIn(4) <> ld_62.io.memReq

  ld_62.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.ReadIn(5) <> ld_67.io.memReq

  ld_67.io.memResp <> MemCtrl.io.ReadOut(5)

  MemCtrl.io.WriteIn(0) <> st_70.io.memReq

  st_70.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(6) <> ld_71.io.memReq

  ld_71.io.memResp <> MemCtrl.io.ReadOut(6)

  MemCtrl.io.WriteIn(1) <> st_72.io.memReq

  st_72.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.WriteIn(2) <> st_74.io.memReq

  st_74.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.WriteIn(3) <> st_75.io.memReq

  st_75.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.WriteIn(4) <> st_81.io.memReq

  st_81.io.memResp <> MemCtrl.io.WriteOut(4)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_sub4.io.LeftIO <> const0.io.Out

  binaryOp_add5.io.RightIO <> const1.io.Out

  binaryOp_add16.io.RightIO <> const2.io.Out

  binaryOp_mul15.io.RightIO <> const3.io.Out

  binaryOp_inc8118.io.RightIO <> const4.io.Out

  binaryOp_inc7826.io.RightIO <> const5.io.Out

  phi_lgyy_s1_box__y_015629.io.InData(0) <> const6.io.Out

  binaryOp_inc7534.io.RightIO <> const7.io.Out

  icmp_exitcond15935.io.RightIO <> const8.io.Out

  phi_lgyy_s1_box__x_015537.io.InData(0) <> const9.io.Out

  binaryOp_mul2743.io.RightIO <> const10.io.Out

  binaryOp_mul4858.io.RightIO <> const11.io.Out

  st_72.io.inData <> const12.io.Out

  st_75.io.inData <> const13.io.Out

  binaryOp_79.io.RightIO <> const14.io.Out

  binaryOp_inc82.io.RightIO <> const15.io.Out

  icmp_exitcond83.io.RightIO <> const16.io.Out

  bitcast_7.io.Input <> alloca__2890.io.Out(1)

  bitcast_8.io.Input <> alloca__2911.io.Out(1)

  bitcast_9.io.Input <> alloca__2922.io.Out(1)

  bitcast_10.io.Input <> alloca__2933.io.Out(1)

  binaryOp_sub314.io.LeftIO <> phi_lgyy_s1_y_015813.io.Out(1)

  binaryOp_inc8118.io.LeftIO <> phi_lgyy_s1_y_015813.io.Out(2)

  icmp_cmp19.io.LeftIO <> phi_lgyy_s1_y_015813.io.Out(3)

  binaryOp_mul15.io.LeftIO <> binaryOp_sub314.io.Out(0)

  binaryOp_add416.io.RightIO <> binaryOp_mul15.io.Out(0)

  br_20.io.CmpIO <> icmp_cmp19.io.Out(0)

  binaryOp_sub1022.io.LeftIO <> phi_lgyy_s1_x_015721.io.Out(0)

  binaryOp_add1123.io.RightIO <> phi_lgyy_s1_x_015721.io.Out(1)

  binaryOp_inc7826.io.LeftIO <> phi_lgyy_s1_x_015721.io.Out(2)

  icmp_cmp727.io.LeftIO <> phi_lgyy_s1_x_015721.io.Out(3)

  Gep_arrayidx6824.io.idx(0) <> binaryOp_add1123.io.Out(0)

  br_28.io.CmpIO <> icmp_cmp727.io.Out(0)

  binaryOp_add1630.io.LeftIO <> phi_lgyy_s1_box__y_015629.io.Out(0)

  binaryOp_inc7534.io.LeftIO <> phi_lgyy_s1_box__y_015629.io.Out(1)

  binaryOp_mul1731.io.LeftIO <> binaryOp_add1630.io.Out(0)

  binaryOp_add1832.io.RightIO <> binaryOp_mul1731.io.Out(0)

  icmp_exitcond15935.io.LeftIO <> binaryOp_inc7534.io.Out(1)

  br_36.io.CmpIO <> icmp_exitcond15935.io.Out(0)

  binaryOp_add2338.io.RightIO <> phi_lgyy_s1_box__x_015537.io.Out(0)

  binaryOp_inc82.io.LeftIO <> phi_lgyy_s1_box__x_015537.io.Out(1)

  binaryOp_add2439.io.LeftIO <> binaryOp_add2338.io.Out(0)

  binaryOp_add2944.io.LeftIO <> binaryOp_add2338.io.Out(1)

  binaryOp_add3649.io.LeftIO <> binaryOp_add2338.io.Out(2)

  binaryOp_add4354.io.LeftIO <> binaryOp_add2338.io.Out(3)

  binaryOp_add5460.io.LeftIO <> binaryOp_add2338.io.Out(4)

  binaryOp_add6165.io.LeftIO <> binaryOp_add2338.io.Out(5)

  Gep_arrayidx40.io.idx(0) <> binaryOp_add2439.io.Out(0)

  ld_41.io.GepAddr <> Gep_arrayidx40.io.Out(0)

  sextconv2542.io.Input <> ld_41.io.Out(0)

  binaryOp_mul2743.io.LeftIO <> sextconv2542.io.Out(0)

  binaryOp_add3448.io.LeftIO <> binaryOp_mul2743.io.Out(0)

  Gep_arrayidx3045.io.idx(0) <> binaryOp_add2944.io.Out(0)

  ld_46.io.GepAddr <> Gep_arrayidx3045.io.Out(0)

  sextconv3347.io.Input <> ld_46.io.Out(0)

  binaryOp_add3448.io.RightIO <> sextconv3347.io.Out(0)

  binaryOp_sub4153.io.LeftIO <> binaryOp_add3448.io.Out(0)

  Gep_arrayidx3750.io.idx(0) <> binaryOp_add3649.io.Out(0)

  ld_51.io.GepAddr <> Gep_arrayidx3750.io.Out(0)

  sextconv4052.io.Input <> ld_51.io.Out(0)

  binaryOp_sub4153.io.RightIO <> sextconv4052.io.Out(0)

  binaryOp_sub5259.io.LeftIO <> binaryOp_sub4153.io.Out(0)

  Gep_arrayidx4455.io.idx(0) <> binaryOp_add4354.io.Out(0)

  ld_56.io.GepAddr <> Gep_arrayidx4455.io.Out(0)

  sextconv4657.io.Input <> ld_56.io.Out(0)

  binaryOp_mul4858.io.LeftIO <> sextconv4657.io.Out(0)

  binaryOp_sub5259.io.RightIO <> binaryOp_mul4858.io.Out(0)

  binaryOp_add5964.io.LeftIO <> binaryOp_sub5259.io.Out(0)

  Gep_arrayidx5561.io.idx(0) <> binaryOp_add5460.io.Out(0)

  ld_62.io.GepAddr <> Gep_arrayidx5561.io.Out(0)

  sextconv5863.io.Input <> ld_62.io.Out(0)

  binaryOp_add5964.io.RightIO <> sextconv5863.io.Out(0)

  binaryOp_sub6669.io.LeftIO <> binaryOp_add5964.io.Out(0)

  Gep_arrayidx6266.io.idx(0) <> binaryOp_add6165.io.Out(0)

  ld_67.io.GepAddr <> Gep_arrayidx6266.io.Out(0)

  sextconv6568.io.Input <> ld_67.io.Out(0)

  binaryOp_sub6669.io.RightIO <> sextconv6568.io.Out(0)

  st_70.io.inData <> binaryOp_sub6669.io.Out(0)

  binaryOp_add7280.io.RightIO <> ld_71.io.Out(0)

  st_74.io.inData <> call_73_in.io.Out.data("field0")

  sextconv7077.io.Input <> call_76_in.io.Out.data("field0")

  binaryOp_mul7178.io.LeftIO <> sextconv7077.io.Out(0)

  binaryOp_mul7178.io.LeftIO <> sextconv7077.io.Out(0)

  binaryOp_79.io.LeftIO <> binaryOp_mul7178.io.Out(0)

  binaryOp_add7280.io.LeftIO <> binaryOp_79.io.Out(0)

  st_81.io.inData <> binaryOp_add7280.io.Out(0)

  icmp_exitcond83.io.LeftIO <> binaryOp_inc82.io.Out(1)

  br_84.io.CmpIO <> icmp_exitcond83.io.Out(0)

  binaryOp_sub4.io.RightIO <> InputSplitter.io.Out.data.elements("field11")(1)

  binaryOp_add5.io.LeftIO <> InputSplitter.io.Out.data.elements("field11")(2)

  binaryOp_add16.io.LeftIO <> InputSplitter.io.Out.data.elements("field12")(1)

  st_70.io.Out(0).ready := true.B

  st_72.io.Out(0).ready := true.B

  st_74.io.Out(0).ready := true.B

  st_75.io.Out(0).ready := true.B

  st_81.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_73_in.io.In <> io.call_73_in

  io.call_73_out <> call_73_out.io.Out(0)

  br_84.io.PredOp(0) <> call_73_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_76_in.io.In <> io.call_76_in

  io.call_76_out <> call_76_out.io.Out(0)

  br_84.io.PredOp(0) <> call_76_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_12.io.Out

}

import java.io.{File, FileWriter}

object extract_function_harrisTop extends App {
  val dir = new File("RTL/extract_function_harrisTop");
  dir.mkdirs
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new extract_function_harrisDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

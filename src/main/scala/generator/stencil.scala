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

abstract class stencilSerialDFIO(implicit val p: Parameters) extends Module with HasAccelParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class stencilSerialDF(implicit p: Parameters) extends stencilSerialDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 3, NWrites = 2)
  (WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 3, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 2, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(2, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 12, NumPhi = 1, BID = 2))

  val bb_for_cond_cleanup33 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 12, BID = 3))

  val bb_for_body44 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup75 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 5))

  val bb_for_body86 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 1, BID = 6))

  val bb_if_then137 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 7, BID = 7))

  val bb_if_end198 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 5, BID = 8))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !36, !UID !37, !BB_UID !38
  val br_0 = Module(new UBranchNode(ID = 0))

  //  ret void, !dbg !39, !UID !40, !BB_UID !41
  val ret_1 = Module(new RetNode2(retTypes = List(), ID = 1))

  //  %pos.060 = phi i32 [ 0, %entry ], [ %inc32, %for.cond.cleanup3 ], !UID !42
  val phipos_0602 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 5, ID = 2, Res = true))

  //  %div = lshr i32 %pos.060, 2, !dbg !43, !UID !44
  val binaryOp_div3 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "lshr")(sign = false, Debug = false))

  //  %and = and i32 %pos.060, 3, !dbg !46, !UID !47
  val binaryOp_and4 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "and")(sign = false, Debug = false))

  //  %add = add nsw i32 %div, -1, !UID !50
  val binaryOp_add5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false, Debug = false))

  //  %add9 = add nsw i32 %and, -1, !UID !51
  val binaryOp_add96 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx17 = getelementptr inbounds i32, i32* %out, i32 %pos.060, !UID !52
  val Gep_arrayidx177 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 7)(ElementSize = 4, ArraySize = List()))

  //  br label %for.body4, !dbg !53, !UID !54, !BB_UID !55
  val br_8 = Module(new UBranchNode(ID = 8))

  //  %arrayidx25 = getelementptr inbounds i32, i32* %out, i32 %pos.060, !dbg !56, !UID !57
  val Gep_arrayidx259 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 9)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx25, align 4, !dbg !56, !tbaa !58, !UID !62
  val ld_10 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 10, RouteID = 0))

  //  %add26 = add i32 %0, 9, !dbg !63, !UID !64
  val binaryOp_add2611 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign = false, Debug = false))

  //  %div27 = udiv i32 %add26, 9, !dbg !65, !UID !66
  val binaryOp_div2712 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "udiv")(sign = false, Debug = false))

  //  store i32 %div27, i32* %arrayidx25, align 4, !dbg !67, !tbaa !58, !UID !68
  val st_13 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 13, RouteID = 0))

  //  %inc32 = add nuw nsw i32 %pos.060, 1, !dbg !69, !UID !70
  val binaryOp_inc3214 = Module(new ComputeNode(NumOuts = 2, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %exitcond62 = icmp eq i32 %inc32, 16, !dbg !71, !UID !72
  val icmp_exitcond6215 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond62, label %for.cond.cleanup, label %for.body, !dbg !36, !llvm.loop !73, !UID !75, !BB_UID !76
  val br_16 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 16))

  //  %nr.059 = phi i32 [ 0, %for.body ], [ %inc21, %for.cond.cleanup7 ], !UID !77
  val phinr_05917 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 17, Res = true))

  //  %sub = add nsw i32 %add, %nr.059, !UID !79
  val binaryOp_sub18 = Module(new ComputeNode(NumOuts = 2, ID = 18, opCode = "add")(sign = false, Debug = false))

  //  %mul = shl i32 %sub, 2, !UID !80
  val binaryOp_mul19 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "shl")(sign = false, Debug = false))

  //  br label %for.body8, !dbg !81, !UID !82, !BB_UID !83
  val br_20 = Module(new UBranchNode(ID = 20))

  //  %inc21 = add nuw nsw i32 %nr.059, 1, !dbg !84, !UID !85
  val binaryOp_inc2121 = Module(new ComputeNode(NumOuts = 2, ID = 21, opCode = "add")(sign = false, Debug = false))

  //  %exitcond61 = icmp eq i32 %inc21, 3, !dbg !86, !UID !87
  val icmp_exitcond6122 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond61, label %for.cond.cleanup3, label %for.body4, !dbg !53, !llvm.loop !88, !UID !90, !BB_UID !91
  val br_23 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 23))

  //  %nc.058 = phi i32 [ 0, %for.body4 ], [ %inc, %if.end19 ], !UID !92
  val phinc_05824 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 24, Res = true))

  //  %sub10 = add nsw i32 %add9, %nc.058, !dbg !94, !UID !95
  val binaryOp_sub1025 = Module(new ComputeNode(NumOuts = 2, ID = 25, opCode = "add")(sign = false, Debug = false))

  //  %1 = or i32 %sub10, %sub, !dbg !97, !UID !98
  val binaryOp_26 = Module(new ComputeNode(NumOuts = 1, ID = 26, opCode = "or")(sign = false, Debug = false))

  //  %2 = icmp ult i32 %1, 4, !dbg !97, !UID !99
  val icmp_27 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "ult")(sign = false, Debug = false))

  //  br i1 %2, label %if.then13, label %if.end19, !dbg !97, !UID !100, !BB_UID !101
  val br_28 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 28))

  //  %add14 = add i32 %sub10, %mul, !dbg !102, !UID !107
  val binaryOp_add1429 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %in, i32 %add14, !dbg !108, !UID !109
  val Gep_arrayidx30 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 30)(ElementSize = 4, ArraySize = List()))

  //  %3 = load i32, i32* %arrayidx, align 4, !dbg !108, !tbaa !58, !UID !110
  val ld_31 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 31, RouteID = 1))

  //  %4 = load i32, i32* %arrayidx17, align 4, !dbg !111, !tbaa !58, !UID !112
  val ld_32 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 32, RouteID = 2))

  //  %add18 = add i32 %4, %3, !dbg !111, !UID !113
  val binaryOp_add1833 = Module(new ComputeNode(NumOuts = 1, ID = 33, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add18, i32* %arrayidx17, align 4, !dbg !111, !tbaa !58, !UID !114
  val st_34 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 34, RouteID = 1))

  //  br label %if.end19, !dbg !115, !UID !116, !BB_UID !117
  val br_35 = Module(new UBranchNode(ID = 35))

  //  %inc = add nuw nsw i32 %nc.058, 1, !dbg !118, !UID !119
  val binaryOp_inc36 = Module(new ComputeNode(NumOuts = 2, ID = 36, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, 3, !dbg !120, !UID !121
  val icmp_exitcond37 = Module(new ComputeNode(NumOuts = 1, ID = 37, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup7, label %for.body8, !dbg !81, !llvm.loop !122, !UID !124, !BB_UID !125
  val br_38 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 38))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 2
  val const1 = Module(new ConstFastNode(value = 2, ID = 1))

  //i32 3
  val const2 = Module(new ConstFastNode(value = 3, ID = 2))

  //i32 -1
  val const3 = Module(new ConstFastNode(value = -1, ID = 3))

  //i32 -1
  val const4 = Module(new ConstFastNode(value = -1, ID = 4))

  //i32 9
  val const5 = Module(new ConstFastNode(value = 9, ID = 5))

  //i32 9
  val const6 = Module(new ConstFastNode(value = 9, ID = 6))

  //i32 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i32 16
  val const8 = Module(new ConstFastNode(value = 16, ID = 8))

  //i32 0
  val const9 = Module(new ConstFastNode(value = 0, ID = 9))

  //i32 2
  val const10 = Module(new ConstFastNode(value = 2, ID = 10))

  //i32 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i32 3
  val const12 = Module(new ConstFastNode(value = 3, ID = 12))

  //i32 0
  val const13 = Module(new ConstFastNode(value = 0, ID = 13))

  //i32 4
  val const14 = Module(new ConstFastNode(value = 4, ID = 14))

  //i32 1
  val const15 = Module(new ConstFastNode(value = 1, ID = 15))

  //i32 3
  val const16 = Module(new ConstFastNode(value = 3, ID = 16))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_if_then137.io.predicateIn(0) <> br_28.io.TrueOutput(0)

  bb_if_end198.io.predicateIn(1) <> br_28.io.FalseOutput(0)

  bb_if_end198.io.predicateIn(0) <> br_35.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup1.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body2.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup33.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body44.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body44.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup75.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body86.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body86.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_20.io.Out(0)

  Loop_0.io.loopBack(0) <> br_38.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_38.io.TrueOutput(0)

  Loop_1.io.enable <> br_8.io.Out(0)

  Loop_1.io.loopBack(0) <> br_23.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_23.io.TrueOutput(0)

  Loop_2.io.enable <> br_0.io.Out(0)

  Loop_2.io.loopBack(0) <> br_16.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_16.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_sub18.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_mul19.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_add5.io.Out(0)

  Loop_1.io.InLiveIn(1) <> binaryOp_add96.io.Out(0)

  Loop_1.io.InLiveIn(2) <> Gep_arrayidx177.io.Out(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_2.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_26.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add1429.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_sub1025.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  ld_32.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field3")(0)

  st_34.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field3")(1)

  Gep_arrayidx30.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  binaryOp_sub18.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx177.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx259.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field0")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc36.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc2121.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc3214.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phinc_05824.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phinr_05917.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phipos_0602.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  ret_1.io.In.enable <> bb_for_cond_cleanup1.io.Out(0)


  const0.io.enable <> bb_for_body2.io.Out(0)

  const1.io.enable <> bb_for_body2.io.Out(1)

  const2.io.enable <> bb_for_body2.io.Out(2)

  const3.io.enable <> bb_for_body2.io.Out(3)

  const4.io.enable <> bb_for_body2.io.Out(4)

  phipos_0602.io.enable <> bb_for_body2.io.Out(5)


  binaryOp_div3.io.enable <> bb_for_body2.io.Out(6)


  binaryOp_and4.io.enable <> bb_for_body2.io.Out(7)


  binaryOp_add5.io.enable <> bb_for_body2.io.Out(8)


  binaryOp_add96.io.enable <> bb_for_body2.io.Out(9)


  Gep_arrayidx177.io.enable <> bb_for_body2.io.Out(10)


  br_8.io.enable <> bb_for_body2.io.Out(11)


  const5.io.enable <> bb_for_cond_cleanup33.io.Out(0)

  const6.io.enable <> bb_for_cond_cleanup33.io.Out(1)

  const7.io.enable <> bb_for_cond_cleanup33.io.Out(2)

  const8.io.enable <> bb_for_cond_cleanup33.io.Out(3)

  Gep_arrayidx259.io.enable <> bb_for_cond_cleanup33.io.Out(4)


  ld_10.io.enable <> bb_for_cond_cleanup33.io.Out(5)


  binaryOp_add2611.io.enable <> bb_for_cond_cleanup33.io.Out(6)


  binaryOp_div2712.io.enable <> bb_for_cond_cleanup33.io.Out(7)


  st_13.io.enable <> bb_for_cond_cleanup33.io.Out(8)


  binaryOp_inc3214.io.enable <> bb_for_cond_cleanup33.io.Out(9)


  icmp_exitcond6215.io.enable <> bb_for_cond_cleanup33.io.Out(10)


  br_16.io.enable <> bb_for_cond_cleanup33.io.Out(11)


  const9.io.enable <> bb_for_body44.io.Out(0)

  const10.io.enable <> bb_for_body44.io.Out(1)

  phinr_05917.io.enable <> bb_for_body44.io.Out(2)


  binaryOp_sub18.io.enable <> bb_for_body44.io.Out(3)


  binaryOp_mul19.io.enable <> bb_for_body44.io.Out(4)


  br_20.io.enable <> bb_for_body44.io.Out(5)


  const11.io.enable <> bb_for_cond_cleanup75.io.Out(0)

  const12.io.enable <> bb_for_cond_cleanup75.io.Out(1)

  binaryOp_inc2121.io.enable <> bb_for_cond_cleanup75.io.Out(2)


  icmp_exitcond6122.io.enable <> bb_for_cond_cleanup75.io.Out(3)


  br_23.io.enable <> bb_for_cond_cleanup75.io.Out(4)


  const13.io.enable <> bb_for_body86.io.Out(0)

  const14.io.enable <> bb_for_body86.io.Out(1)

  phinc_05824.io.enable <> bb_for_body86.io.Out(2)


  binaryOp_sub1025.io.enable <> bb_for_body86.io.Out(3)


  binaryOp_26.io.enable <> bb_for_body86.io.Out(4)


  icmp_27.io.enable <> bb_for_body86.io.Out(5)


  br_28.io.enable <> bb_for_body86.io.Out(6)


  binaryOp_add1429.io.enable <> bb_if_then137.io.Out(0)


  Gep_arrayidx30.io.enable <> bb_if_then137.io.Out(1)


  ld_31.io.enable <> bb_if_then137.io.Out(2)


  ld_32.io.enable <> bb_if_then137.io.Out(3)


  binaryOp_add1833.io.enable <> bb_if_then137.io.Out(4)


  st_34.io.enable <> bb_if_then137.io.Out(5)


  br_35.io.enable <> bb_if_then137.io.Out(6)


  const15.io.enable <> bb_if_end198.io.Out(0)

  const16.io.enable <> bb_if_end198.io.Out(1)

  binaryOp_inc36.io.enable <> bb_if_end198.io.Out(2)


  icmp_exitcond37.io.enable <> bb_if_end198.io.Out(3)


  br_38.io.enable <> bb_if_end198.io.Out(4)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phipos_0602.io.Mask <> bb_for_body2.io.MaskBB(0)

  phinr_05917.io.Mask <> bb_for_body44.io.MaskBB(0)

  phinc_05824.io.Mask <> bb_for_body86.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_10.io.memReq

  ld_10.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_13.io.memReq

  st_13.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_31.io.memReq

  ld_31.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_32.io.memReq

  ld_32.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(1) <> st_34.io.memReq

  st_34.io.memResp <> MemCtrl.io.WriteOut(1)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phipos_0602.io.InData(0) <> const0.io.Out

  binaryOp_div3.io.RightIO <> const1.io.Out

  binaryOp_and4.io.RightIO <> const2.io.Out

  binaryOp_add5.io.RightIO <> const3.io.Out

  binaryOp_add96.io.RightIO <> const4.io.Out

  binaryOp_add2611.io.RightIO <> const5.io.Out

  binaryOp_div2712.io.RightIO <> const6.io.Out

  binaryOp_inc3214.io.RightIO <> const7.io.Out

  icmp_exitcond6215.io.RightIO <> const8.io.Out

  phinr_05917.io.InData(0) <> const9.io.Out

  binaryOp_mul19.io.RightIO <> const10.io.Out

  binaryOp_inc2121.io.RightIO <> const11.io.Out

  icmp_exitcond6122.io.RightIO <> const12.io.Out

  phinc_05824.io.InData(0) <> const13.io.Out

  icmp_27.io.RightIO <> const14.io.Out

  binaryOp_inc36.io.RightIO <> const15.io.Out

  icmp_exitcond37.io.RightIO <> const16.io.Out

  binaryOp_div3.io.LeftIO <> phipos_0602.io.Out(0)

  binaryOp_and4.io.LeftIO <> phipos_0602.io.Out(1)

  Gep_arrayidx177.io.idx(0) <> phipos_0602.io.Out(2)

  Gep_arrayidx259.io.idx(0) <> phipos_0602.io.Out(3)

  binaryOp_inc3214.io.LeftIO <> phipos_0602.io.Out(4)

  binaryOp_add5.io.LeftIO <> binaryOp_div3.io.Out(0)

  binaryOp_add96.io.LeftIO <> binaryOp_and4.io.Out(0)

  ld_10.io.GepAddr <> Gep_arrayidx259.io.Out(0)

  st_13.io.GepAddr <> Gep_arrayidx259.io.Out(1)

  binaryOp_add2611.io.LeftIO <> ld_10.io.Out(0)

  binaryOp_div2712.io.LeftIO <> binaryOp_add2611.io.Out(0)

  st_13.io.inData <> binaryOp_div2712.io.Out(0)

  icmp_exitcond6215.io.LeftIO <> binaryOp_inc3214.io.Out(1)

  br_16.io.CmpIO <> icmp_exitcond6215.io.Out(0)

  binaryOp_sub18.io.RightIO <> phinr_05917.io.Out(0)

  binaryOp_inc2121.io.LeftIO <> phinr_05917.io.Out(1)

  binaryOp_mul19.io.LeftIO <> binaryOp_sub18.io.Out(1)

  icmp_exitcond6122.io.LeftIO <> binaryOp_inc2121.io.Out(1)

  br_23.io.CmpIO <> icmp_exitcond6122.io.Out(0)

  binaryOp_sub1025.io.RightIO <> phinc_05824.io.Out(0)

  binaryOp_inc36.io.LeftIO <> phinc_05824.io.Out(1)

  binaryOp_26.io.LeftIO <> binaryOp_sub1025.io.Out(0)

  binaryOp_add1429.io.LeftIO <> binaryOp_sub1025.io.Out(1)

  icmp_27.io.LeftIO <> binaryOp_26.io.Out(0)

  br_28.io.CmpIO <> icmp_27.io.Out(0)

  Gep_arrayidx30.io.idx(0) <> binaryOp_add1429.io.Out(0)

  ld_31.io.GepAddr <> Gep_arrayidx30.io.Out(0)

  binaryOp_add1833.io.RightIO <> ld_31.io.Out(0)

  binaryOp_add1833.io.LeftIO <> ld_32.io.Out(0)

  st_34.io.inData <> binaryOp_add1833.io.Out(0)

  icmp_exitcond37.io.LeftIO <> binaryOp_inc36.io.Out(1)

  br_38.io.CmpIO <> icmp_exitcond37.io.Out(0)

  st_13.io.Out(0).ready := true.B

  st_34.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_1.io.Out

}

import java.io.{File, FileWriter}

object stencilSerialTop extends App {
  val dir = new File("RTL/stencilSerialTop");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new stencilSerialDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

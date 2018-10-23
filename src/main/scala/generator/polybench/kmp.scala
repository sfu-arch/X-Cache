package dataflow

import FPU._
import accel._
import arbiters._
import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import config._
import control._
import interfaces._
import junctions._
import loop._
import memory._
import muxes._
import node._
import org.scalatest._
import regfile._
import stack._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class kmpDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class kmpDF(implicit p: Parameters) extends kmpDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=16, NWrites=6)
		 (WControl=new WriteMemoryController(NumOps=6, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=16, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(9,1,5,2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockO1(NumIns=List(1,1,1,1), NumOuts = 2, NumExits=1, ID = 0))

  val Loop_1 = Module(new LoopBlockO1(NumIns=List(2,1,2,2), NumOuts = 0, NumExits=1, ID = 1))

//  val Loop_2 = Module(new LoopBlockO1(NumIns=List(), NumOuts = 0, NumExits=0, ID = 2))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 26, BID = 0))

  val bb_1 = Module(new LoopFastHead(NumOuts = 1, NumPhi=0, BID = 1))

  val bb_2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 2))

  val bb_3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 18, NumPhi=2, BID = 3))

  val bb_4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 4))

  val bb_5 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 5))

  val bb_6 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi=2, BID = 6))

  val bb_7 = Module(new LoopHead(NumOuts = 14, NumPhi=2, BID = 7))

  val bb_8 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 8))

  val bb_9 = Module(new LoopHead(NumOuts = 11, NumPhi=1, BID = 9))

  val bb_10 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 10))

  val bb_11 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi=2, BID = 11))

  val bb_12 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 10, BID = 12))

  val bb_13 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi=1, BID = 13))

  val bb_14 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 14))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  store i32 0, i32* %3, align 4, !tbaa !2
  val st_0 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=0, RouteID=0))

  //  store i32 0, i32* %2, align 4, !tbaa !2
  val st_1 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=1, RouteID=1))

  //  %5 = load i8, i8* %0, align 1, !tbaa !6
  val ld_2 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=2, RouteID=0))

  //  %6 = getelementptr inbounds i8, i8* %0, i64 1
  val Gep_3 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=3)(ElementSize = 1, ArraySize = List()))

  //  %7 = load i8, i8* %6, align 1, !tbaa !6
  val ld_4 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=4, RouteID=1))

  //  %8 = icmp eq i8 %5, %7
  val icmp_5 = Module(new IcmpNode(NumOuts = 3, ID = 5, opCode = "eq")(sign=false))

  //  %9 = getelementptr inbounds i32, i32* %2, i64 1
  val Gep_6 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=6)(ElementSize = 4, ArraySize = List()))

  //  %10 = zext i1 %8 to i32
  val sext7 = Module(new ZextNode())

  //  store i32 %10, i32* %9, align 4, !tbaa !2
  val st_8 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=8, RouteID=2))

  //  %11 = xor i1 %8, true
  val binaryOp_9 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "xor")(sign=false))

  //  %12 = zext i1 %8 to i64
  val sext10 = Module(new ZextNode())

  //  %13 = getelementptr inbounds i8, i8* %0, i64 %12
  val Gep_11 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=11)(ElementSize = 1, ArraySize = List()))

  //  %14 = load i8, i8* %13, align 1, !tbaa !6
  val ld_12 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=12, RouteID=2))

  //  %15 = getelementptr inbounds i8, i8* %0, i64 2
  val Gep_13 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=13)(ElementSize = 1, ArraySize = List()))

  //  %16 = load i8, i8* %15, align 1, !tbaa !6
  val ld_14 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2, ID=14, RouteID=3))

  //  %17 = icmp eq i8 %14, %16
  val icmp_15 = Module(new IcmpNode(NumOuts = 2, ID = 15, opCode = "eq")(sign=false))

  //  %18 = or i1 %17, %11
  val binaryOp_16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "or")(sign=false))

  //  %19 = getelementptr inbounds i32, i32* %2, i64 2
  val Gep_17 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=17)(ElementSize = 4, ArraySize = List()))

  //  br i1 %18, label %29, label %21
  val br_18 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, ID = 18))

  //  br label %20
  val br_19 = Module(new UBranchNode(NumOuts=2, ID = 19))

  //  %22 = load i32, i32* %19, align 4, !tbaa !2
  val ld_20 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=3, ID=20, RouteID=4))

  //  %23 = icmp slt i32 %22, 1
  val icmp_21 = Module(new IcmpNode(NumOuts = 1, ID = 21, opCode = "ult")(sign=false))

  //  %24 = sext i32 %22 to i64
  val sext22 = Module(new SextNode())

  //  %25 = getelementptr inbounds i8, i8* %0, i64 %24
  val Gep_23 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=23)(ElementSize = 1, ArraySize = List()))

  //  %26 = load i8, i8* %25, align 1, !tbaa !6
  val ld_24 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=24, RouteID=5))

  //  %27 = icmp eq i8 %26, %16
  val icmp_25 = Module(new IcmpNode(NumOuts = 2, ID = 25, opCode = "eq")(sign=false))

  //  %28 = or i1 %23, %27
  val binaryOp_26 = Module(new ComputeNode(NumOuts = 1, ID = 26, opCode = "or")(sign=false))

  //  br i1 %28, label %29, label %51
  val br_27 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, ID = 27))

  //  %30 = phi i32 [ %22, %21 ], [ %10, %4 ]
  val phi28 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 28))

  //  %31 = phi i1 [ %27, %21 ], [ %17, %4 ]
  val phi29 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 29))

  //  %32 = zext i1 %31 to i32
  val sext30 = Module(new ZextNode())

  //  %33 = add nsw i32 %32, %30
  val binaryOp_31 = Module(new ComputeNode(NumOuts = 4, ID = 31, opCode = "add")(sign=false))

  //  store i32 %33, i32* %19, align 4, !tbaa !2
  val st_32 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=32, RouteID=3))

  //  %34 = icmp slt i32 %33, 1
  val icmp_33 = Module(new IcmpNode(NumOuts = 1, ID = 33, opCode = "ult")(sign=false))

  //  %35 = sext i32 %33 to i64
  val sext34 = Module(new SextNode())

  //  %36 = getelementptr inbounds i8, i8* %0, i64 %35
  val Gep_35 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=35)(ElementSize = 1, ArraySize = List()))

  //  %37 = load i8, i8* %36, align 1, !tbaa !6
  val ld_36 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=36, RouteID=6))

  //  %38 = getelementptr inbounds i8, i8* %0, i64 3
  val Gep_37 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=37)(ElementSize = 1, ArraySize = List()))

  //  %39 = load i8, i8* %38, align 1, !tbaa !6
  val ld_38 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2, ID=38, RouteID=7))

  //  %40 = icmp eq i8 %37, %39
  val icmp_39 = Module(new IcmpNode(NumOuts = 2, ID = 39, opCode = "eq")(sign=false))

  //  %41 = or i1 %34, %40
  val binaryOp_40 = Module(new ComputeNode(NumOuts = 1, ID = 40, opCode = "or")(sign=false))

  //  %42 = getelementptr inbounds i32, i32* %2, i64 3
  val Gep_41 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=41)(ElementSize = 4, ArraySize = List()))

  //  br i1 %41, label %52, label %43
  val br_42 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, ID = 42))

  //  %44 = load i32, i32* %42, align 4, !tbaa !2
  val ld_43 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=3, ID=43, RouteID=8))

  //  %45 = icmp slt i32 %44, 1
  val icmp_44 = Module(new IcmpNode(NumOuts = 1, ID = 44, opCode = "ult")(sign=false))

  //  %46 = sext i32 %44 to i64
  val sext45 = Module(new SextNode())

  //  %47 = getelementptr inbounds i8, i8* %0, i64 %46
  val Gep_46 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=46)(ElementSize = 1, ArraySize = List()))

  //  %48 = load i8, i8* %47, align 1, !tbaa !6
  val ld_47 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=47, RouteID=9))

  //  %49 = icmp eq i8 %48, %39
  val icmp_48 = Module(new IcmpNode(NumOuts = 2, ID = 48, opCode = "eq")(sign=false))

  //  %50 = or i1 %45, %49
  val binaryOp_49 = Module(new ComputeNode(NumOuts = 1, ID = 49, opCode = "or")(sign=false))

  //  br i1 %50, label %52, label %51
  val br_50 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, ID = 50))

  //  br label %20
  val br_51 = Module(new UBranchNode(ID = 51))

  //  %53 = phi i32 [ %44, %43 ], [ %33, %29 ]
  val phi52 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 52))

  //  %54 = phi i1 [ %49, %43 ], [ %40, %29 ]
  val phi53 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 53))

  //  %55 = zext i1 %54 to i32
  val sext54 = Module(new ZextNode())

  //  %56 = add nsw i32 %55, %53
  val binaryOp_55 = Module(new ComputeNode(NumOuts = 1, ID = 55, opCode = "add")(sign=false))

  //  store i32 %56, i32* %42, align 4, !tbaa !2
  val st_56 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=56, RouteID=4))

  //  br label %57
  val br_57 = Module(new UBranchNode(ID = 57))

  //  %58 = phi i64 [ 0, %52 ], [ %95, %93 ]
  val phi58 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 2, ID = 58))

  //  %59 = phi i32 [ 0, %52 ], [ %94, %93 ]
  val phi59 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 3, ID = 59))

  //  %60 = icmp slt i32 %59, 1
  val icmp_60 = Module(new IcmpNode(NumOuts = 1, ID = 60, opCode = "ult")(sign=false))

  //  %61 = sext i32 %59 to i64
  val sext61 = Module(new SextNode())

  //  %62 = getelementptr inbounds i8, i8* %0, i64 %61
  val Gep_62 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=62)(ElementSize = 1, ArraySize = List()))

  //  %63 = load i8, i8* %62, align 1, !tbaa !6
  val ld_63 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=63, RouteID=10))

  //  %64 = getelementptr inbounds i8, i8* %1, i64 %58
  val Gep_64 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=64)(ElementSize = 1, ArraySize = List()))

  //  %65 = load i8, i8* %64, align 1, !tbaa !6
  val ld_65 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2, ID=65, RouteID=11))

  //  %66 = icmp eq i8 %63, %65
  val icmp_66 = Module(new IcmpNode(NumOuts = 2, ID = 66, opCode = "eq")(sign=false))

  //  %67 = or i1 %60, %66
  val binaryOp_67 = Module(new ComputeNode(NumOuts = 1, ID = 67, opCode = "or")(sign=false))

  //  br i1 %67, label %80, label %68
  val br_68 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, ID = 68))

  //  br label %69
  val br_69 = Module(new UBranchNode(ID = 69))

  //  %70 = phi i64 [ %74, %69 ], [ %61, %68 ]
  val phi70 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 70))

  //  %71 = getelementptr inbounds i32, i32* %2, i64 %70
  val Gep_71 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=71)(ElementSize = 4, ArraySize = List()))

  //  %72 = load i32, i32* %71, align 4, !tbaa !2
  val ld_72 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=3, ID=72, RouteID=12))

  //  %73 = icmp slt i32 %72, 1
  val icmp_73 = Module(new IcmpNode(NumOuts = 1, ID = 73, opCode = "ult")(sign=false))

  //  %74 = sext i32 %72 to i64
  val sext74 = Module(new SextNode())

  //  %75 = getelementptr inbounds i8, i8* %0, i64 %74
  val Gep_75 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=75)(ElementSize = 1, ArraySize = List()))

  //  %76 = load i8, i8* %75, align 1, !tbaa !6
  val ld_76 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=76, RouteID=13))

  //  %77 = icmp eq i8 %76, %65
  val icmp_77 = Module(new IcmpNode(NumOuts = 2, ID = 77, opCode = "eq")(sign=false))

  //  %78 = or i1 %73, %77
  val binaryOp_78 = Module(new ComputeNode(NumOuts = 1, ID = 78, opCode = "or")(sign=false))

  //  br i1 %78, label %79, label %69
  val br_79 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 2, ID = 79))

  //  br label %80
  val br_80 = Module(new UBranchNode(ID = 80))

  //  %81 = phi i32 [ %59, %57 ], [ %72, %79 ]
  val phi81 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 81))

  //  %82 = phi i1 [ %66, %57 ], [ %77, %79 ]
  val phi82 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 82))

  //  %83 = zext i1 %82 to i32
  val sext83 = Module(new ZextNode())

  //  %84 = add nsw i32 %83, %81
  val binaryOp_84 = Module(new ComputeNode(NumOuts = 3, ID = 84, opCode = "add")(sign=false))

  //  %85 = icmp sgt i32 %84, 3
  val icmp_85 = Module(new IcmpNode(NumOuts = 1, ID = 85, opCode = "ugt")(sign=false))

  //  br i1 %85, label %86, label %93
  val br_86 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, ID = 86))

  //  %87 = load i32, i32* %3, align 4, !tbaa !2
  val ld_87 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=87, RouteID=14))

  //  %88 = add nsw i32 %87, 1
  val binaryOp_88 = Module(new ComputeNode(NumOuts = 1, ID = 88, opCode = "add")(sign=false))

  //  store i32 %88, i32* %3, align 4, !tbaa !2
  val st_89 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=89, RouteID=5))

  //  %89 = add nsw i32 %84, -1
  val binaryOp_90 = Module(new ComputeNode(NumOuts = 1, ID = 90, opCode = "add")(sign=false))

  //  %90 = sext i32 %89 to i64
  val sext91 = Module(new SextNode())

  //  %91 = getelementptr inbounds i32, i32* %2, i64 %90
  val Gep_92 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=92)(ElementSize = 4, ArraySize = List()))

  //  %92 = load i32, i32* %91, align 4, !tbaa !2
  val ld_93 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=93, RouteID=15))

  //  br label %93
  val br_94 = Module(new UBranchNode(NumPredOps=1, ID = 94))

  //  %94 = phi i32 [ %92, %86 ], [ %84, %80 ]
  val phi95 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 95))

  //  %95 = add nuw nsw i64 %58, 1
  val binaryOp_96 = Module(new ComputeNode(NumOuts = 2, ID = 96, opCode = "add")(sign=false))

  //  %96 = icmp eq i64 %95, 32411
  val icmp_97 = Module(new IcmpNode(NumOuts = 1, ID = 97, opCode = "eq")(sign=false))

  //  br i1 %96, label %97, label %57
  val br_98 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 2, ID = 98))

  //  ret i32 0
  val ret_99 = Module(new RetNode2(retTypes=List(32), ID = 99))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstNode(value = 0, ID = 1))

  //i64 1
  val const2 = Module(new ConstNode(value = 1, ID = 2))

  //i64 1
  val const3 = Module(new ConstNode(value = 1, ID = 3))

  //i1 true
  val const4 = Module(new ConstNode(value = -1, ID = 4))

  //i64 2
  val const5 = Module(new ConstNode(value = 2, ID = 5))

  //i64 2
  val const6 = Module(new ConstNode(value = 2, ID = 6))

  //i32 1
  val const7 = Module(new ConstNode(value = 1, ID = 7))

  //i32 1
  val const8 = Module(new ConstNode(value = 1, ID = 8))

  //i64 3
  val const9 = Module(new ConstNode(value = 3, ID = 9))

  //i64 3
  val const10 = Module(new ConstNode(value = 3, ID = 10))

  //i32 1
  val const11 = Module(new ConstNode(value = 1, ID = 11))

  //i64 0
  val const12 = Module(new ConstNode(value = 0, ID = 12))

  //i32 0
  val const13 = Module(new ConstNode(value = 0, ID = 13))

  //i32 1
  val const14 = Module(new ConstNode(value = 1, ID = 14))

  //i32 1
  val const15 = Module(new ConstNode(value = 1, ID = 15))

  //i32 3
  val const16 = Module(new ConstNode(value = 3, ID = 16))

  //i32 1
  val const17 = Module(new ConstNode(value = 1, ID = 17))

  //i32 -1
  val const18 = Module(new ConstNode(value = -1, ID = 18))

  //i64 1
  val const19 = Module(new ConstNode(value = 1, ID = 19))

  //i64 32411
  val const20 = Module(new ConstNode(value = 32411, ID = 20))

  //i32 0
  val const21 = Module(new ConstNode(value = 0, ID = 21))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_1.io.activate <> br_19.io.Out(0)

//  bb_1.io.loopBack <> Loop_2.io.activate
  bb_1.io.loopBack.bits := ControlBundle.default
  bb_1.io.loopBack.valid := true.B

  bb_2.io.predicateIn <> br_18.io.FalseOutput(0)

  bb_3.io.predicateIn(0) <> br_18.io.TrueOutput(0)

  bb_3.io.predicateIn(1) <> br_27.io.TrueOutput(0)

  bb_4.io.predicateIn <> br_42.io.FalseOutput(0)

  bb_5.io.predicateIn <> br_27.io.FalseOutput(0)

  bb_5.io.predicateIn <> br_50.io.FalseOutput(0)

  bb_6.io.predicateIn(0) <> br_42.io.TrueOutput(0)

  bb_6.io.predicateIn(1) <> br_50.io.TrueOutput(0)

  bb_7.io.activate <> Loop_1.io.activate

  bb_7.io.loopBack <> br_98.io.FalseOutput(0)

  bb_8.io.predicateIn <> br_68.io.FalseOutput(0)

  bb_9.io.activate <> Loop_0.io.activate

  bb_9.io.loopBack <> br_79.io.FalseOutput(0)

  bb_10.io.predicateIn <> Loop_0.io.endEnable

  bb_11.io.predicateIn(0) <> br_68.io.TrueOutput(0)

  bb_11.io.predicateIn(1) <> br_80.io.Out(0)

  bb_12.io.predicateIn <> br_86.io.TrueOutput(0)

  bb_13.io.predicateIn(0) <> br_86.io.FalseOutput(0)

  bb_13.io.predicateIn(1) <> br_94.io.Out(0)

  bb_14.io.predicateIn <> Loop_1.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_69.io.Out(0)

  Loop_0.io.latchEnable <> br_79.io.FalseOutput(1)

  Loop_0.io.loopExit(0) <> br_79.io.TrueOutput(0)

  Loop_1.io.enable <> br_57.io.Out(0)

  Loop_1.io.latchEnable <> br_98.io.FalseOutput(1)

  Loop_1.io.loopExit(0) <> br_98.io.TrueOutput(0)

//  Loop_2.io.enable <> br_51.io.Out(0)
  br_51.io.Out(0).ready := true.B

//  Loop_2.io.latchEnable <> br_19.io.Out(1)
  br_19.io.Out(1).ready := true.B



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_94.io.PredOp(0) <> st_89.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> sext61.io.Out

  Loop_0.io.In(1) <> Loop_1.io.liveIn.data("field2")(0)

  Loop_0.io.In(2) <> Loop_1.io.liveIn.data("field0")(1)

  Loop_0.io.In(3) <> ld_65.io.Out(1)

  Loop_1.io.In(0) <> InputSplitter.io.Out.data("field0")(8)

  Loop_1.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_1.io.In(2) <> InputSplitter.io.Out.data("field2")(4)

  Loop_1.io.In(3) <> InputSplitter.io.Out.data("field3")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi70.io.InData(1) <> Loop_0.io.liveIn.data("field0")(0)

  Gep_71.io.baseAddress <> Loop_0.io.liveIn.data("field1")(0)

  Gep_75.io.baseAddress <> Loop_0.io.liveIn.data("field2")(0)

  icmp_77.io.RightIO <> Loop_0.io.liveIn.data("field3")(0)

  Gep_62.io.baseAddress <> Loop_1.io.liveIn.data("field0")(0)

  Gep_64.io.baseAddress <> Loop_1.io.liveIn.data("field1")(0)

  Gep_92.io.baseAddress <> Loop_1.io.liveIn.data("field2")(1)

  ld_87.io.GepAddr <> Loop_1.io.liveIn.data("field3")(0)

  st_89.io.GepAddr <> Loop_1.io.liveIn.data("field3")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.liveOut(0) <> ld_72.io.Out(2)

  Loop_0.io.liveOut(0) <> icmp_77.io.Out(1)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_0.io.Out(0)

  const1.io.enable <> bb_0.io.Out(1)

  const2.io.enable <> bb_0.io.Out(2)

  const3.io.enable <> bb_0.io.Out(3)

  const4.io.enable <> bb_0.io.Out(4)

  const5.io.enable <> bb_0.io.Out(5)

  const6.io.enable <> bb_0.io.Out(6)

  st_0.io.enable <> bb_0.io.Out(7)

  st_1.io.enable <> bb_0.io.Out(8)

  ld_2.io.enable <> bb_0.io.Out(9)

  Gep_3.io.enable <> bb_0.io.Out(10)

  ld_4.io.enable <> bb_0.io.Out(11)

  icmp_5.io.enable <> bb_0.io.Out(12)

  Gep_6.io.enable <> bb_0.io.Out(13)

  sext7.io.enable <> bb_0.io.Out(14)

  st_8.io.enable <> bb_0.io.Out(15)

  binaryOp_9.io.enable <> bb_0.io.Out(16)

  sext10.io.enable <> bb_0.io.Out(17)

  Gep_11.io.enable <> bb_0.io.Out(18)

  ld_12.io.enable <> bb_0.io.Out(19)

  Gep_13.io.enable <> bb_0.io.Out(20)

  ld_14.io.enable <> bb_0.io.Out(21)

  icmp_15.io.enable <> bb_0.io.Out(22)

  binaryOp_16.io.enable <> bb_0.io.Out(23)

  Gep_17.io.enable <> bb_0.io.Out(24)

  br_18.io.enable <> bb_0.io.Out(25)


  br_19.io.enable <> bb_1.io.Out(0)


  const7.io.enable <> bb_2.io.Out(0)

  ld_20.io.enable <> bb_2.io.Out(1)

  icmp_21.io.enable <> bb_2.io.Out(2)

  sext22.io.enable <> bb_2.io.Out(3)

  Gep_23.io.enable <> bb_2.io.Out(4)

  ld_24.io.enable <> bb_2.io.Out(5)

  icmp_25.io.enable <> bb_2.io.Out(6)

  binaryOp_26.io.enable <> bb_2.io.Out(7)

  br_27.io.enable <> bb_2.io.Out(8)


  const8.io.enable <> bb_3.io.Out(0)

  const9.io.enable <> bb_3.io.Out(1)

  const10.io.enable <> bb_3.io.Out(2)

  phi28.io.enable <> bb_3.io.Out(3)

  phi29.io.enable <> bb_3.io.Out(4)

  sext30.io.enable <> bb_3.io.Out(5)

  binaryOp_31.io.enable <> bb_3.io.Out(6)

  st_32.io.enable <> bb_3.io.Out(7)

  icmp_33.io.enable <> bb_3.io.Out(8)

  sext34.io.enable <> bb_3.io.Out(9)

  Gep_35.io.enable <> bb_3.io.Out(10)

  ld_36.io.enable <> bb_3.io.Out(11)

  Gep_37.io.enable <> bb_3.io.Out(12)

  ld_38.io.enable <> bb_3.io.Out(13)

  icmp_39.io.enable <> bb_3.io.Out(14)

  binaryOp_40.io.enable <> bb_3.io.Out(15)

  Gep_41.io.enable <> bb_3.io.Out(16)

  br_42.io.enable <> bb_3.io.Out(17)


  const11.io.enable <> bb_4.io.Out(0)

  ld_43.io.enable <> bb_4.io.Out(1)

  icmp_44.io.enable <> bb_4.io.Out(2)

  sext45.io.enable <> bb_4.io.Out(3)

  Gep_46.io.enable <> bb_4.io.Out(4)

  ld_47.io.enable <> bb_4.io.Out(5)

  icmp_48.io.enable <> bb_4.io.Out(6)

  binaryOp_49.io.enable <> bb_4.io.Out(7)

  br_50.io.enable <> bb_4.io.Out(8)


  br_51.io.enable <> bb_5.io.Out(0)


  phi52.io.enable <> bb_6.io.Out(0)

  phi53.io.enable <> bb_6.io.Out(1)

  sext54.io.enable <> bb_6.io.Out(2)

  binaryOp_55.io.enable <> bb_6.io.Out(3)

  st_56.io.enable <> bb_6.io.Out(4)

  br_57.io.enable <> bb_6.io.Out(5)


  const12.io.enable <> bb_7.io.Out(0)

  const13.io.enable <> bb_7.io.Out(1)

  const14.io.enable <> bb_7.io.Out(2)

  phi58.io.enable <> bb_7.io.Out(3)

  phi59.io.enable <> bb_7.io.Out(4)

  icmp_60.io.enable <> bb_7.io.Out(5)

  sext61.io.enable <> bb_7.io.Out(6)

  Gep_62.io.enable <> bb_7.io.Out(7)

  ld_63.io.enable <> bb_7.io.Out(8)

  Gep_64.io.enable <> bb_7.io.Out(9)

  ld_65.io.enable <> bb_7.io.Out(10)

  icmp_66.io.enable <> bb_7.io.Out(11)

  binaryOp_67.io.enable <> bb_7.io.Out(12)

  br_68.io.enable <> bb_7.io.Out(13)


  br_69.io.enable <> bb_8.io.Out(0)


  const15.io.enable <> bb_9.io.Out(0)

  phi70.io.enable <> bb_9.io.Out(1)

  Gep_71.io.enable <> bb_9.io.Out(2)

  ld_72.io.enable <> bb_9.io.Out(3)

  icmp_73.io.enable <> bb_9.io.Out(4)

  sext74.io.enable <> bb_9.io.Out(5)

  Gep_75.io.enable <> bb_9.io.Out(6)

  ld_76.io.enable <> bb_9.io.Out(7)

  icmp_77.io.enable <> bb_9.io.Out(8)

  binaryOp_78.io.enable <> bb_9.io.Out(9)

  br_79.io.enable <> bb_9.io.Out(10)


  br_80.io.enable <> bb_10.io.Out(0)


  const16.io.enable <> bb_11.io.Out(0)

  phi81.io.enable <> bb_11.io.Out(1)

  phi82.io.enable <> bb_11.io.Out(2)

  sext83.io.enable <> bb_11.io.Out(3)

  binaryOp_84.io.enable <> bb_11.io.Out(4)

  icmp_85.io.enable <> bb_11.io.Out(5)

  br_86.io.enable <> bb_11.io.Out(6)


  const17.io.enable <> bb_12.io.Out(0)

  const18.io.enable <> bb_12.io.Out(1)

  ld_87.io.enable <> bb_12.io.Out(2)

  binaryOp_88.io.enable <> bb_12.io.Out(3)

  st_89.io.enable <> bb_12.io.Out(4)

  binaryOp_90.io.enable <> bb_12.io.Out(5)

  sext91.io.enable <> bb_12.io.Out(6)

  Gep_92.io.enable <> bb_12.io.Out(7)

  ld_93.io.enable <> bb_12.io.Out(8)

  br_94.io.enable <> bb_12.io.Out(9)


  const19.io.enable <> bb_13.io.Out(0)

  const20.io.enable <> bb_13.io.Out(1)

  phi95.io.enable <> bb_13.io.Out(2)

  binaryOp_96.io.enable <> bb_13.io.Out(3)

  icmp_97.io.enable <> bb_13.io.Out(4)

  br_98.io.enable <> bb_13.io.Out(5)


  const21.io.enable <> bb_14.io.Out(0)

  ret_99.io.In.enable <> bb_14.io.Out(1)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi28.io.Mask <> bb_3.io.MaskBB(0)

  phi29.io.Mask <> bb_3.io.MaskBB(1)

  phi52.io.Mask <> bb_6.io.MaskBB(0)

  phi53.io.Mask <> bb_6.io.MaskBB(1)

  phi58.io.Mask <> bb_7.io.MaskBB(0)

  phi59.io.Mask <> bb_7.io.MaskBB(1)

  phi70.io.Mask <> bb_9.io.MaskBB(0)

  phi81.io.Mask <> bb_11.io.MaskBB(0)

  phi82.io.Mask <> bb_11.io.MaskBB(1)

  phi95.io.Mask <> bb_13.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.WriteIn(0) <> st_0.io.memReq

  st_0.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.WriteIn(1) <> st_1.io.memReq

  st_1.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(0) <> ld_2.io.memReq

  ld_2.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_4.io.memReq

  ld_4.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(2) <> st_8.io.memReq

  st_8.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.ReadIn(2) <> ld_12.io.memReq

  ld_12.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.ReadIn(3) <> ld_14.io.memReq

  ld_14.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.ReadIn(4) <> ld_20.io.memReq

  ld_20.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.ReadIn(5) <> ld_24.io.memReq

  ld_24.io.memResp <> MemCtrl.io.ReadOut(5)

  MemCtrl.io.WriteIn(3) <> st_32.io.memReq

  st_32.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.ReadIn(6) <> ld_36.io.memReq

  ld_36.io.memResp <> MemCtrl.io.ReadOut(6)

  MemCtrl.io.ReadIn(7) <> ld_38.io.memReq

  ld_38.io.memResp <> MemCtrl.io.ReadOut(7)

  MemCtrl.io.ReadIn(8) <> ld_43.io.memReq

  ld_43.io.memResp <> MemCtrl.io.ReadOut(8)

  MemCtrl.io.ReadIn(9) <> ld_47.io.memReq

  ld_47.io.memResp <> MemCtrl.io.ReadOut(9)

  MemCtrl.io.WriteIn(4) <> st_56.io.memReq

  st_56.io.memResp <> MemCtrl.io.WriteOut(4)

  MemCtrl.io.ReadIn(10) <> ld_63.io.memReq

  ld_63.io.memResp <> MemCtrl.io.ReadOut(10)

  MemCtrl.io.ReadIn(11) <> ld_65.io.memReq

  ld_65.io.memResp <> MemCtrl.io.ReadOut(11)

  MemCtrl.io.ReadIn(12) <> ld_72.io.memReq

  ld_72.io.memResp <> MemCtrl.io.ReadOut(12)

  MemCtrl.io.ReadIn(13) <> ld_76.io.memReq

  ld_76.io.memResp <> MemCtrl.io.ReadOut(13)

  MemCtrl.io.ReadIn(14) <> ld_87.io.memReq

  ld_87.io.memResp <> MemCtrl.io.ReadOut(14)

  MemCtrl.io.WriteIn(5) <> st_89.io.memReq

  st_89.io.memResp <> MemCtrl.io.WriteOut(5)

  MemCtrl.io.ReadIn(15) <> ld_93.io.memReq

  ld_93.io.memResp <> MemCtrl.io.ReadOut(15)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  st_0.io.inData <> const0.io.Out

  st_1.io.inData <> const1.io.Out

  Gep_3.io.idx(0) <> const2.io.Out

  Gep_6.io.idx(0) <> const3.io.Out

  binaryOp_9.io.RightIO <> const4.io.Out

  Gep_13.io.idx(0) <> const5.io.Out

  Gep_17.io.idx(0) <> const6.io.Out

  icmp_21.io.RightIO <> const7.io.Out

  icmp_33.io.RightIO <> const8.io.Out

  Gep_37.io.idx(0) <> const9.io.Out

  Gep_41.io.idx(0) <> const10.io.Out

  icmp_44.io.RightIO <> const11.io.Out

  phi58.io.InData(0) <> const12.io.Out

  phi59.io.InData(0) <> const13.io.Out

  icmp_60.io.RightIO <> const14.io.Out

  icmp_73.io.RightIO <> const15.io.Out

  icmp_85.io.RightIO <> const16.io.Out

  binaryOp_88.io.RightIO <> const17.io.Out

  binaryOp_90.io.RightIO <> const18.io.Out

  binaryOp_96.io.RightIO <> const19.io.Out

  icmp_97.io.RightIO <> const20.io.Out

  ret_99.io.In.data("field0") <> const21.io.Out

  icmp_5.io.LeftIO <> ld_2.io.Out(0)

  ld_4.io.GepAddr <> Gep_3.io.Out(0)

  icmp_5.io.RightIO <> ld_4.io.Out(0)

  sext7.io.Input <> icmp_5.io.Out(0)

  binaryOp_9.io.LeftIO <> icmp_5.io.Out(1)

  sext10.io.Input <> icmp_5.io.Out(2)

  st_8.io.GepAddr <> Gep_6.io.Out(0)

  st_8.io.inData <> sext7.io.Out

  phi28.io.InData(1) <> sext7.io.Out

  binaryOp_16.io.RightIO <> binaryOp_9.io.Out(0)

  Gep_11.io.idx(0) <> sext10.io.Out

  ld_12.io.GepAddr <> Gep_11.io.Out(0)

  icmp_15.io.LeftIO <> ld_12.io.Out(0)

  ld_14.io.GepAddr <> Gep_13.io.Out(0)

  icmp_15.io.RightIO <> ld_14.io.Out(0)

  icmp_25.io.RightIO <> ld_14.io.Out(1)

  binaryOp_16.io.LeftIO <> icmp_15.io.Out(0)

  phi29.io.InData(1) <> icmp_15.io.Out(1)

  br_18.io.CmpIO <> binaryOp_16.io.Out(0)

  ld_20.io.GepAddr <> Gep_17.io.Out(0)

  st_32.io.GepAddr <> Gep_17.io.Out(1)

  icmp_21.io.LeftIO <> ld_20.io.Out(0)

  sext22.io.Input <> ld_20.io.Out(1)

  phi28.io.InData(0) <> ld_20.io.Out(2)

  binaryOp_26.io.LeftIO <> icmp_21.io.Out(0)

  Gep_23.io.idx(0) <> sext22.io.Out

  ld_24.io.GepAddr <> Gep_23.io.Out(0)

  icmp_25.io.LeftIO <> ld_24.io.Out(0)

  binaryOp_26.io.RightIO <> icmp_25.io.Out(0)

  phi29.io.InData(0) <> icmp_25.io.Out(1)

  br_27.io.CmpIO <> binaryOp_26.io.Out(0)

  binaryOp_31.io.RightIO <> phi28.io.Out(0)

  sext30.io.Input <> phi29.io.Out(0)

  binaryOp_31.io.LeftIO <> sext30.io.Out

  st_32.io.inData <> binaryOp_31.io.Out(0)

  icmp_33.io.LeftIO <> binaryOp_31.io.Out(1)

  sext34.io.Input <> binaryOp_31.io.Out(2)

  phi52.io.InData(1) <> binaryOp_31.io.Out(3)

  binaryOp_40.io.LeftIO <> icmp_33.io.Out(0)

  Gep_35.io.idx(0) <> sext34.io.Out

  ld_36.io.GepAddr <> Gep_35.io.Out(0)

  icmp_39.io.LeftIO <> ld_36.io.Out(0)

  ld_38.io.GepAddr <> Gep_37.io.Out(0)

  icmp_39.io.RightIO <> ld_38.io.Out(0)

  icmp_48.io.RightIO <> ld_38.io.Out(1)

  binaryOp_40.io.RightIO <> icmp_39.io.Out(0)

  phi53.io.InData(1) <> icmp_39.io.Out(1)

  br_42.io.CmpIO <> binaryOp_40.io.Out(0)

  ld_43.io.GepAddr <> Gep_41.io.Out(0)

  st_56.io.GepAddr <> Gep_41.io.Out(1)

  icmp_44.io.LeftIO <> ld_43.io.Out(0)

  sext45.io.Input <> ld_43.io.Out(1)

  phi52.io.InData(0) <> ld_43.io.Out(2)

  binaryOp_49.io.LeftIO <> icmp_44.io.Out(0)

  Gep_46.io.idx(0) <> sext45.io.Out

  ld_47.io.GepAddr <> Gep_46.io.Out(0)

  icmp_48.io.LeftIO <> ld_47.io.Out(0)

  binaryOp_49.io.RightIO <> icmp_48.io.Out(0)

  phi53.io.InData(0) <> icmp_48.io.Out(1)

  br_50.io.CmpIO <> binaryOp_49.io.Out(0)

  binaryOp_55.io.RightIO <> phi52.io.Out(0)

  sext54.io.Input <> phi53.io.Out(0)

  binaryOp_55.io.LeftIO <> sext54.io.Out

  st_56.io.inData <> binaryOp_55.io.Out(0)

  Gep_64.io.idx(0) <> phi58.io.Out(0)

  binaryOp_96.io.LeftIO <> phi58.io.Out(1)

  icmp_60.io.LeftIO <> phi59.io.Out(0)

  sext61.io.Input <> phi59.io.Out(1)

  phi81.io.InData(0) <> phi59.io.Out(2)

  binaryOp_67.io.LeftIO <> icmp_60.io.Out(0)

  Gep_62.io.idx(0) <> sext61.io.Out

  ld_63.io.GepAddr <> Gep_62.io.Out(0)

  icmp_66.io.LeftIO <> ld_63.io.Out(0)

  ld_65.io.GepAddr <> Gep_64.io.Out(0)

  icmp_66.io.RightIO <> ld_65.io.Out(0)

  binaryOp_67.io.RightIO <> icmp_66.io.Out(0)

  phi82.io.InData(0) <> icmp_66.io.Out(1)

  br_68.io.CmpIO <> binaryOp_67.io.Out(0)

  Gep_71.io.idx(0) <> phi70.io.Out(0)

  ld_72.io.GepAddr <> Gep_71.io.Out(0)

  icmp_73.io.LeftIO <> ld_72.io.Out(0)

  sext74.io.Input <> ld_72.io.Out(1)

  binaryOp_78.io.LeftIO <> icmp_73.io.Out(0)

  phi70.io.InData(0) <> sext74.io.Out

  Gep_75.io.idx(0) <> sext74.io.Out

  ld_76.io.GepAddr <> Gep_75.io.Out(0)

  icmp_77.io.LeftIO <> ld_76.io.Out(0)

  binaryOp_78.io.RightIO <> icmp_77.io.Out(0)

  br_79.io.CmpIO <> binaryOp_78.io.Out(0)

  binaryOp_84.io.RightIO <> phi81.io.Out(0)

  sext83.io.Input <> phi82.io.Out(0)

  binaryOp_84.io.LeftIO <> sext83.io.Out

  icmp_85.io.LeftIO <> binaryOp_84.io.Out(0)

  binaryOp_90.io.LeftIO <> binaryOp_84.io.Out(1)

  phi95.io.InData(1) <> binaryOp_84.io.Out(2)

  br_86.io.CmpIO <> icmp_85.io.Out(0)

  binaryOp_88.io.LeftIO <> ld_87.io.Out(0)

  st_89.io.inData <> binaryOp_88.io.Out(0)

  sext91.io.Input <> binaryOp_90.io.Out(0)

  Gep_92.io.idx(0) <> sext91.io.Out

  ld_93.io.GepAddr <> Gep_92.io.Out(0)

  phi95.io.InData(0) <> ld_93.io.Out(0)

  phi59.io.InData(1) <> phi95.io.Out(0)

  phi58.io.InData(1) <> binaryOp_96.io.Out(0)

  icmp_97.io.LeftIO <> binaryOp_96.io.Out(1)

  br_98.io.CmpIO <> icmp_97.io.Out(0)

  phi81.io.InData(1) <> Loop_0.io.Out(0)

  phi82.io.InData(1) <> Loop_0.io.Out(0)

  ld_2.io.GepAddr <> InputSplitter.io.Out.data("field0")(0)

  Gep_3.io.baseAddress <> InputSplitter.io.Out.data("field0")(1)

  Gep_11.io.baseAddress <> InputSplitter.io.Out.data("field0")(2)

  Gep_13.io.baseAddress <> InputSplitter.io.Out.data("field0")(3)

  Gep_23.io.baseAddress <> InputSplitter.io.Out.data("field0")(4)

  Gep_35.io.baseAddress <> InputSplitter.io.Out.data("field0")(5)

  Gep_37.io.baseAddress <> InputSplitter.io.Out.data("field0")(6)

  Gep_46.io.baseAddress <> InputSplitter.io.Out.data("field0")(7)

  st_1.io.GepAddr <> InputSplitter.io.Out.data("field2")(0)

  Gep_6.io.baseAddress <> InputSplitter.io.Out.data("field2")(1)

  Gep_17.io.baseAddress <> InputSplitter.io.Out.data("field2")(2)

  Gep_41.io.baseAddress <> InputSplitter.io.Out.data("field2")(3)

  st_0.io.GepAddr <> InputSplitter.io.Out.data("field3")(0)

  st_0.io.Out(0).ready := true.B

  st_1.io.Out(0).ready := true.B

  st_8.io.Out(0).ready := true.B

  st_32.io.Out(0).ready := true.B

  st_56.io.Out(0).ready := true.B

  st_89.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_99.io.Out

}

import java.io.{File, FileWriter}
object kmpMain extends App {
  val dir = new File("RTL/kmp") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new kmpDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

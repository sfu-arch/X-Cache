package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import config._
import control.{BasicBlockNoMaskNode, BasicBlockNode}
import util._
import interfaces._
import regfile._
import memory._
import arbiters._
import node._

/**
  * This Object should be initialize at the first step
  * It contains all the transformation from indecies to their module's name
  */

object Data___offload_func_5_FlowParam{
  val my_for_cond5_preheader_pred = Map(
    "active" -> 0
  )

  val ret_fail_pred = Map(
    "m_58" -> 0
  )

  val g_pred = Map(
    "m_58" -> 0
  )

  val m_58_brn_bb = Map(
    "ret_fail" -> 0,
    "g" -> 1
  )

  val my_for_cond5_preheader_activate = Map(
    "m_0" -> 0,
    "m_1" -> 1,
    "m_2" -> 2,
    "m_3" -> 3,
    "m_4" -> 4,
    "m_5" -> 5,
    "m_6" -> 6,
    "m_7" -> 7,
    "m_8" -> 8,
    "m_9" -> 9,
    "m_10" -> 10,
    "m_11" -> 11,
    "m_12" -> 12,
    "m_13" -> 13,
    "m_14" -> 14,
    "m_15" -> 15,
    "m_16" -> 16,
    "m_17" -> 17,
    "m_18" -> 18,
    "m_19" -> 19,
    "m_20" -> 20,
    "m_21" -> 21,
    "m_22" -> 22,
    "m_23" -> 23,
    "m_24" -> 24,
    "m_25" -> 25,
    "m_26" -> 26,
    "m_27" -> 27,
    "m_28" -> 28,
    "m_29" -> 29,
    "m_30" -> 30,
    "m_31" -> 31,
    "m_32" -> 32,
    "m_33" -> 33,
    "m_34" -> 34,
    "m_35" -> 35,
    "m_36" -> 36,
    "m_37" -> 37,
    "m_38" -> 38,
    "m_39" -> 39,
    "m_40" -> 40,
    "m_41" -> 41,
    "m_42" -> 42,
    "m_43" -> 43,
    "m_44" -> 44,
    "m_45" -> 45,
    "m_46" -> 46,
    "m_47" -> 47,
    "m_48" -> 48,
    "m_49" -> 49,
    "m_50" -> 50,
    "m_51" -> 51,
    "m_52" -> 52,
    "m_53" -> 53,
    "m_54" -> 54,
    "m_55" -> 55,
    "m_56" -> 56,
    "m_57" -> 57,
    "m_58" -> 58
  )

  val g_activate = Map(
    "m_59" -> 0,
    "m_60" -> 1,
    "m_61" -> 2
  )

  val ret_fail_activate = Map(
    "m_62" -> 0
  )

  val m_0_in = Map( 
    "data_1" -> 0,
    "data_0" -> 1
  )

  val m_1_in = Map( 
    "data_2" -> 0,
    "m_0" -> 0
  )

  val m_2_in = Map( 
    "m_1" -> 0
  )

  val m_3_in = Map( 
    "data_3" -> 0
  )

  val m_4_in = Map( 
    "m_3" -> 0,
    "m_2" -> 0
  )

  val m_5_in = Map( 
    "m_0" -> 1
  )

  val m_6_in = Map( 
    "data_2" -> 0,
    "m_5" -> 0
  )

  val m_7_in = Map( 
    "m_6" -> 0
  )

  val m_8_in = Map( 
    "data_4" -> 0
  )

  val m_9_in = Map( 
    "m_8" -> 0,
    "m_7" -> 0
  )

  val m_10_in = Map( 
    "m_9" -> 0,
    "m_4" -> 0
  )

  val m_11_in = Map( 
    "m_0" -> 2
  )

  val m_12_in = Map( 
    "data_2" -> 0,
    "m_11" -> 0
  )

  val m_13_in = Map( 
    "m_12" -> 0
  )

  val m_14_in = Map( 
    "data_5" -> 0
  )

  val m_15_in = Map( 
    "m_14" -> 0,
    "m_13" -> 0
  )

  val m_16_in = Map( 
    "m_10" -> 0,
    "m_15" -> 0
  )

  val m_17_in = Map( 
    "data_6" -> 0,
    "data_0" -> 1
  )

  val m_18_in = Map( 
    "data_2" -> 0,
    "m_17" -> 0
  )

  val m_19_in = Map( 
    "m_18" -> 0
  )

  val m_20_in = Map( 
    "data_7" -> 0
  )

  val m_21_in = Map( 
    "m_20" -> 0,
    "m_19" -> 0
  )

  val m_22_in = Map( 
    "m_16" -> 0,
    "m_21" -> 0
  )

  val m_23_in = Map( 
    "m_17" -> 1
  )

  val m_24_in = Map( 
    "data_2" -> 0,
    "m_23" -> 0
  )

  val m_25_in = Map( 
    "m_24" -> 0
  )

  val m_26_in = Map( 
    "data_8" -> 0
  )

  val m_27_in = Map( 
    "m_26" -> 0,
    "m_25" -> 0
  )

  val m_28_in = Map( 
    "m_22" -> 0,
    "m_27" -> 0
  )

  val m_29_in = Map( 
    "m_17" -> 2
  )

  val m_30_in = Map( 
    "data_2" -> 0,
    "m_29" -> 0
  )

  val m_31_in = Map( 
    "m_30" -> 0
  )

  val m_32_in = Map( 
    "data_9" -> 0
  )

  val m_33_in = Map( 
    "m_32" -> 0,
    "m_31" -> 0
  )

  val m_34_in = Map( 
    "m_28" -> 0,
    "m_33" -> 0
  )

  val m_35_in = Map( 
    "data_10" -> 0,
    "data_0" -> 1
  )

  val m_36_in = Map( 
    "data_2" -> 0,
    "m_35" -> 0
  )

  val m_37_in = Map( 
    "m_36" -> 0
  )

  val m_38_in = Map( 
    "data_11" -> 0
  )

  val m_39_in = Map( 
    "m_38" -> 0,
    "m_37" -> 0
  )

  val m_40_in = Map( 
    "m_34" -> 0,
    "m_39" -> 0
  )

  val m_41_in = Map( 
    "m_35" -> 1
  )

  val m_42_in = Map( 
    "data_2" -> 0,
    "m_41" -> 0
  )

  val m_43_in = Map( 
    "m_42" -> 0
  )

  val m_44_in = Map( 
    "data_12" -> 0
  )

  val m_45_in = Map( 
    "m_44" -> 0,
    "m_43" -> 0
  )

  val m_46_in = Map( 
    "m_40" -> 0,
    "m_45" -> 0
  )

  val m_47_in = Map( 
    "m_35" -> 2
  )

  val m_48_in = Map( 
    "data_2" -> 0,
    "m_47" -> 0
  )

  val m_49_in = Map( 
    "m_48" -> 0
  )

  val m_50_in = Map( 
    "data_13" -> 0
  )

  val m_51_in = Map( 
    "m_50" -> 0,
    "m_49" -> 0
  )

  val m_52_in = Map( 
    "m_46" -> 0,
    "m_51" -> 0
  )

  val m_53_in = Map( 
    "m_52" -> 0,
    "data_14" -> 1
  )

  val m_54_in = Map( 
    "data_15" -> 0,
    "m_0" -> 3
  )

  val m_55_in = Map( 
    "m_53" -> 0,
    "m_54" -> 0
  )

  val m_56_in = Map( 
    "data_0" -> 0
  )

  val m_57_in = Map( 
    "m_56" -> 0,
    "data_16" -> 1
  )

  val m_59_in = Map( 
    "data_18" -> 0
  )

  val m_60_in = Map( 
    "m_56" -> 1,
    "m_59" -> 0
  )

  val m_61_in = Map(
  )

  val m_62_in = Map(
  )

}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class offload_func_5DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val data_0 = Flipped(Decoupled(new DataBundle))
    val data_1 = Flipped(Decoupled(new DataBundle))
    val data_2 = Flipped(Decoupled(new DataBundle))
    val data_3 = Flipped(Decoupled(new DataBundle))
    val data_4 = Flipped(Decoupled(new DataBundle))
    val data_5 = Flipped(Decoupled(new DataBundle))
    val data_6 = Flipped(Decoupled(new DataBundle))
    val data_7 = Flipped(Decoupled(new DataBundle))
    val data_8 = Flipped(Decoupled(new DataBundle))
    val data_9 = Flipped(Decoupled(new DataBundle))
    val data_10 = Flipped(Decoupled(new DataBundle))
    val data_11 = Flipped(Decoupled(new DataBundle))
    val data_12 = Flipped(Decoupled(new DataBundle))
    val data_13 = Flipped(Decoupled(new DataBundle))
    val data_14 = Flipped(Decoupled(new DataBundle))
    val data_15 = Flipped(Decoupled(new DataBundle))
    val data_16 = Flipped(Decoupled(new DataBundle))
    val data_17 = Flipped(Decoupled(new DataBundle))
    val data_18 = Flipped(Decoupled(new DataBundle))
    val pred = Decoupled(new Bool())
    val start = Input(new Bool())
    val result = Decoupled(new DataBundle)
  })
}

class offload_func_5DF(implicit p: Parameters) extends offload_func_5DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */




  /* ================================================================== *
   *                   PRINTING BASICBLOCKS                             *
   * ================================================================== */

  //Initializing BasicBlocks: 
  val my_for_cond5_preheader = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 59, BID = 0)(p))
  val g = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 1)(p))
  val ret_fail = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2)(p))





  /* ================================================================== *
   *                   PRINTING STACKFILE                               *
   * ================================================================== */

	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=18,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=18,BaseSize=2,NumEntries=2)))


  /* ================================================================== *
   *                   PRINTING INSTURCTIONS                            *
   * ================================================================== */

  //Initializing Instructions: 
  // my_for.cond5.preheader: 

  //  %2 = add nsw i32 %mul21.in, %iCol.06.in, !UID !5
  val m_0 = Module (new ComputeNode(NumOuts = 4, ID = 0, opCode = "add")(sign=false)(p))

  //  %3 = getelementptr inbounds i32, i32* %imIn.in, i32 %2, !UID !6
  val m_1 = Module (new GepOneNode(NumOuts = 1, ID = 1)(numByte1 = 0)(p))

  //  %4 = load i32, i32* %3, align 4, !UID !7
  val m_2 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=2,RouteID=0))

  //  %5 = load i32, i32* %k.in, align 4, !UID !8
  val m_3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=3,RouteID=1))

  //  %6 = mul i32 %5, %4, !UID !9
  val m_4 = Module (new ComputeNode(NumOuts = 1, ID = 4, opCode = "mul")(sign=false)(p))

  //  %7 = add nsw i32 %2, 1, !UID !10
  val m_5 = Module (new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign=false)(p))

  //  %8 = getelementptr inbounds i32, i32* %imIn.in, i32 %7, !UID !11
  val m_6 = Module (new GepOneNode(NumOuts = 1, ID = 6)(numByte1 = 0)(p))

  //  %9 = load i32, i32* %8, align 4, !UID !12
  val m_7 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=2))

  //  %10 = load i32, i32* %arrayidx15.1.in, align 4, !UID !13
  val m_8 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=8,RouteID=3))

  //  %11 = mul i32 %10, %9, !UID !14
  val m_9 = Module (new ComputeNode(NumOuts = 1, ID = 9, opCode = "mul")(sign=false)(p))

  //  %12 = add i32 %11, %6, !UID !15
  val m_10 = Module (new ComputeNode(NumOuts = 1, ID = 10, opCode = "add")(sign=false)(p))

  //  %13 = add nsw i32 %2, 2, !UID !16
  val m_11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign=false)(p))

  //  %14 = getelementptr inbounds i32, i32* %imIn.in, i32 %13, !UID !17
  val m_12 = Module (new GepOneNode(NumOuts = 1, ID = 12)(numByte1 = 0)(p))

  //  %15 = load i32, i32* %14, align 4, !UID !18
  val m_13 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=13,RouteID=4))

  //  %16 = load i32, i32* %arrayidx15.2.in, align 4, !UID !19
  val m_14 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=14,RouteID=5))

  //  %17 = mul i32 %16, %15, !UID !20
  val m_15 = Module (new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign=false)(p))

  //  %18 = add i32 %12, %17, !UID !21
  val m_16 = Module (new ComputeNode(NumOuts = 1, ID = 16, opCode = "add")(sign=false)(p))

  //  %19 = add nsw i32 %mul.1.in, %iCol.06.in, !UID !22
  val m_17 = Module (new ComputeNode(NumOuts = 3, ID = 17, opCode = "add")(sign=false)(p))

  //  %20 = getelementptr inbounds i32, i32* %imIn.in, i32 %19, !UID !23
  val m_18 = Module (new GepOneNode(NumOuts = 1, ID = 18)(numByte1 = 0)(p))

  //  %21 = load i32, i32* %20, align 4, !UID !24
  val m_19 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=19,RouteID=6))

  //  %22 = load i32, i32* %arrayidx15.114.in, align 4, !UID !25
  val m_20 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=20,RouteID=7))

  //  %23 = mul i32 %22, %21, !UID !26
  val m_21 = Module (new ComputeNode(NumOuts = 1, ID = 21, opCode = "mul")(sign=false)(p))

  //  %24 = add i32 %18, %23, !UID !27
  val m_22 = Module (new ComputeNode(NumOuts = 1, ID = 22, opCode = "add")(sign=false)(p))

  //  %25 = add nsw i32 %19, 1, !UID !28
  val m_23 = Module (new ComputeNode(NumOuts = 1, ID = 23, opCode = "add")(sign=false)(p))

  //  %26 = getelementptr inbounds i32, i32* %imIn.in, i32 %25, !UID !29
  val m_24 = Module (new GepOneNode(NumOuts = 1, ID = 24)(numByte1 = 0)(p))

  //  %27 = load i32, i32* %26, align 4, !UID !30
  val m_25 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=25,RouteID=8))

  //  %28 = load i32, i32* %arrayidx15.1.1.in, align 4, !UID !31
  val m_26 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=26,RouteID=9))

  //  %29 = mul i32 %28, %27, !UID !32
  val m_27 = Module (new ComputeNode(NumOuts = 1, ID = 27, opCode = "mul")(sign=false)(p))

  //  %30 = add i32 %24, %29, !UID !33
  val m_28 = Module (new ComputeNode(NumOuts = 1, ID = 28, opCode = "add")(sign=false)(p))

  //  %31 = add nsw i32 %19, 2, !UID !34
  val m_29 = Module (new ComputeNode(NumOuts = 1, ID = 29, opCode = "add")(sign=false)(p))

  //  %32 = getelementptr inbounds i32, i32* %imIn.in, i32 %31, !UID !35
  val m_30 = Module (new GepOneNode(NumOuts = 1, ID = 30)(numByte1 = 0)(p))

  //  %33 = load i32, i32* %32, align 4, !UID !36
  val m_31 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=31,RouteID=10))

  //  %34 = load i32, i32* %arrayidx15.2.1.in, align 4, !UID !37
  val m_32 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=32,RouteID=11))

  //  %35 = mul i32 %34, %33, !UID !38
  val m_33 = Module (new ComputeNode(NumOuts = 1, ID = 33, opCode = "mul")(sign=false)(p))

  //  %36 = add i32 %30, %35, !UID !39
  val m_34 = Module (new ComputeNode(NumOuts = 1, ID = 34, opCode = "add")(sign=false)(p))

  //  %37 = add nsw i32 %mul.2.in, %iCol.06.in, !UID !40
  val m_35 = Module (new ComputeNode(NumOuts = 3, ID = 35, opCode = "add")(sign=false)(p))

  //  %38 = getelementptr inbounds i32, i32* %imIn.in, i32 %37, !UID !41
  val m_36 = Module (new GepOneNode(NumOuts = 1, ID = 36)(numByte1 = 0)(p))

  //  %39 = load i32, i32* %38, align 4, !UID !42
  val m_37 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=37,RouteID=12))

  //  %40 = load i32, i32* %arrayidx15.218.in, align 4, !UID !43
  val m_38 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=38,RouteID=13))

  //  %41 = mul i32 %40, %39, !UID !44
  val m_39 = Module (new ComputeNode(NumOuts = 1, ID = 39, opCode = "mul")(sign=false)(p))

  //  %42 = add i32 %36, %41, !UID !45
  val m_40 = Module (new ComputeNode(NumOuts = 1, ID = 40, opCode = "add")(sign=false)(p))

  //  %43 = add nsw i32 %37, 1, !UID !46
  val m_41 = Module (new ComputeNode(NumOuts = 1, ID = 41, opCode = "add")(sign=false)(p))

  //  %44 = getelementptr inbounds i32, i32* %imIn.in, i32 %43, !UID !47
  val m_42 = Module (new GepOneNode(NumOuts = 1, ID = 42)(numByte1 = 0)(p))

  //  %45 = load i32, i32* %44, align 4, !UID !48
  val m_43 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=43,RouteID=14))

  //  %46 = load i32, i32* %arrayidx15.1.2.in, align 4, !UID !49
  val m_44 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=44,RouteID=15))

  //  %47 = mul i32 %46, %45, !UID !50
  val m_45 = Module (new ComputeNode(NumOuts = 1, ID = 45, opCode = "mul")(sign=false)(p))

  //  %48 = add i32 %42, %47, !UID !51
  val m_46 = Module (new ComputeNode(NumOuts = 1, ID = 46, opCode = "add")(sign=false)(p))

  //  %49 = add nsw i32 %37, 2, !UID !52
  val m_47 = Module (new ComputeNode(NumOuts = 1, ID = 47, opCode = "add")(sign=false)(p))

  //  %50 = getelementptr inbounds i32, i32* %imIn.in, i32 %49, !UID !53
  val m_48 = Module (new GepOneNode(NumOuts = 1, ID = 48)(numByte1 = 0)(p))

  //  %51 = load i32, i32* %50, align 4, !UID !54
  val m_49 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=49,RouteID=16))

  //  %52 = load i32, i32* %arrayidx15.2.2.in, align 4, !UID !55
  val m_50 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=50,RouteID=17))

  //  %53 = mul i32 %52, %51, !UID !56
  val m_51 = Module (new ComputeNode(NumOuts = 1, ID = 51, opCode = "mul")(sign=false)(p))

  //  %54 = add i32 %48, %53, !UID !57
  val m_52 = Module (new ComputeNode(NumOuts = 1, ID = 52, opCode = "add")(sign=false)(p))

  //  %55 = udiv i32 %54, %weight.in, !UID !58
  val m_53 = Module (new ComputeNode(NumOuts = 1, ID = 53, opCode = "udiv")(sign=false)(p))

  //  %56 = getelementptr inbounds i32, i32* %imOut.in, i32 %2, !UID !59
  val m_54 = Module (new GepOneNode(NumOuts = 1, ID = 54)(numByte1 = 0)(p))

  //  store i32 %55, i32* %56, align 4, !UID !60
  val m_55 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=55,RouteID=0))

  //  %57 = add nuw nsw i32 %iCol.06.in, 1, !UID !61
  val m_56 = Module (new ComputeNode(NumOuts = 2, ID = 56, opCode = "add")(sign=false)(p))

  //  %58 = icmp eq i32 %57, %sub2.in, !UID !62
  val m_57 = Module (new IcmpNode(NumOuts = 1, ID = 57, opCode = "EQ")(sign=false)(p))

  //  br i1 %58, label %ret.fail, label %g, !UID !63, !BB_UID !64
  val m_58 = Module (new CBranchNode(ID = 58)(p))

  // g: 

  //  %59 = getelementptr <{ i32 }>, <{ i32 }>* %1, i32 0, i32 0, !UID !65
  val m_59 = Module (new GepTwoNode(NumOuts = 1, ID = 59)(numByte1 = 4, numByte2 = 0)(p))

  //  store i32 %57, i32* %59, align 4, !UID !66, !LO !67
  val m_60 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=60,RouteID=1))

  //  ret i1 true, !UID !68, !BB_UID !69
  //val m_61 = Module (new RetNode(ID = 61)(p))

  // ret.fail: 

  //  ret i1 false, !UID !70, !BB_UID !71
  //val m_62 = Module (new RetNode(ID = 62)(p))






  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */

  /**
    * Instantiating parameters
    */
  val param = Data___offload_func_5_FlowParam




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO PREDICATE INSTRUCTIONS *
   * ================================================================== */

  /**
     * Connecting basic blocks to predicate instructions
     */

  //We always ground entry BasicBlock
  my_for_cond5_preheader.io.predicateIn(param.my_for_cond5_preheader_pred("active")).bits  := ControlBundle.Activate
  my_for_cond5_preheader.io.predicateIn(param.my_for_cond5_preheader_pred("active")).valid := true.B

  /**
    * Connecting basic blocks to predicate instructions
    */
  //Connecting m_58 to ret_fail
  ret_fail.io.predicateIn(param.ret_fail_pred("m_58")) <> m_58.io.Out(param.m_58_brn_bb("ret_fail"))

  //Connecting m_58 to g
  g.io.predicateIn(param.g_pred("m_58")) <> m_58.io.Out(param.m_58_brn_bb("g"))




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO INSTRUCTIONS           *
   * ================================================================== */

  /**
    * Wireing enable signals to the instructions
    */
  //Wiring enable signals
  m_0.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_0"))
  m_1.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_1"))
  m_2.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_2"))
  m_3.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_3"))
  m_4.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_4"))
  m_5.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_5"))
  m_6.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_6"))
  m_7.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_7"))
  m_8.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_8"))
  m_9.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_9"))
  m_10.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_10"))
  m_11.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_11"))
  m_12.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_12"))
  m_13.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_13"))
  m_14.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_14"))
  m_15.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_15"))
  m_16.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_16"))
  m_17.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_17"))
  m_18.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_18"))
  m_19.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_19"))
  m_20.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_20"))
  m_21.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_21"))
  m_22.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_22"))
  m_23.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_23"))
  m_24.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_24"))
  m_25.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_25"))
  m_26.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_26"))
  m_27.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_27"))
  m_28.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_28"))
  m_29.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_29"))
  m_30.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_30"))
  m_31.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_31"))
  m_32.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_32"))
  m_33.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_33"))
  m_34.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_34"))
  m_35.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_35"))
  m_36.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_36"))
  m_37.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_37"))
  m_38.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_38"))
  m_39.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_39"))
  m_40.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_40"))
  m_41.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_41"))
  m_42.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_42"))
  m_43.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_43"))
  m_44.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_44"))
  m_45.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_45"))
  m_46.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_46"))
  m_47.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_47"))
  m_48.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_48"))
  m_49.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_49"))
  m_50.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_50"))
  m_51.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_51"))
  m_52.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_52"))
  m_53.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_53"))
  m_54.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_54"))
  m_55.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_55"))
  m_56.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_56"))
  m_57.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_57"))
  m_58.io.enable <> my_for_cond5_preheader.io.Out(param.my_for_cond5_preheader_activate("m_58"))

  m_59.io.enable <> g.io.Out(param.g_activate("m_59"))
  m_60.io.enable <> g.io.Out(param.g_activate("m_60"))
  //m_61.io.enable <> g.io.Out(param.g_activate("m_61"))

  //m_62.io.enable <> ret_fail.io.Out(param.ret_fail_activate("m_62"))




  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */

  /**
    * Connecting PHI nodes
    */
  //Connect PHI node
  //There is no PHI node




  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */

  /**
    * Connecting Dataflow signals
    */
  // Wiring Binary instruction to the function argument
  m_0.io.LeftIO <> io.data_1


  // Wiring Binary instruction to the function argument
  m_0.io.RightIO <> io.data_0


  // Wiring GEP instruction to the function argument
  m_1.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_1.io.idx1 <> m_0.io.Out(param.m_1_in("m_0"))

  // Wiring Load instruction to the parent instruction
  m_2.io.GepAddr <> m_1.io.Out(param.m_2_in("m_1"))
  m_2.io.memResp  <> StackFile.io.ReadOut(0)
  StackFile.io.ReadIn(0) <> m_2.io.memReq


  // Wiring Load instruction to the function argument
  m_3.io.GepAddr <> io.data_3
  m_3.io.memResp <> StackFile.io.ReadOut(1)
  StackFile.io.ReadIn(1) <> m_3.io.memReq


  // Wiring instructions
  m_4.io.LeftIO <> m_3.io.Out(param.m_4_in("m_3"))

  // Wiring instructions
  m_4.io.RightIO <> m_2.io.Out(param.m_4_in("m_2"))

  // Wiring instructions
  m_5.io.LeftIO <> m_0.io.Out(param.m_5_in("m_0"))

  // Wiring constant
  m_5.io.RightIO.bits.data := 1.U
  m_5.io.RightIO.bits.predicate := true.B
  m_5.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  m_6.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_6.io.idx1 <> m_5.io.Out(param.m_6_in("m_5"))

  // Wiring Load instruction to the parent instruction
  m_7.io.GepAddr <> m_6.io.Out(param.m_7_in("m_6"))
  m_7.io.memResp  <> StackFile.io.ReadOut(2)
  StackFile.io.ReadIn(2) <> m_7.io.memReq


  // Wiring Load instruction to the function argument
  m_8.io.GepAddr <> io.data_4
  m_8.io.memResp <> StackFile.io.ReadOut(3)
  StackFile.io.ReadIn(3) <> m_8.io.memReq


  // Wiring instructions
  m_9.io.LeftIO <> m_8.io.Out(param.m_9_in("m_8"))

  // Wiring instructions
  m_9.io.RightIO <> m_7.io.Out(param.m_9_in("m_7"))

  // Wiring instructions
  m_10.io.LeftIO <> m_9.io.Out(param.m_10_in("m_9"))

  // Wiring instructions
  m_10.io.RightIO <> m_4.io.Out(param.m_10_in("m_4"))

  // Wiring instructions
  m_11.io.LeftIO <> m_0.io.Out(param.m_11_in("m_0"))

  // Wiring constant
  m_11.io.RightIO.bits.data := 2.U
  m_11.io.RightIO.bits.predicate := true.B
  m_11.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  m_12.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_12.io.idx1 <> m_11.io.Out(param.m_12_in("m_11"))

  // Wiring Load instruction to the parent instruction
  m_13.io.GepAddr <> m_12.io.Out(param.m_13_in("m_12"))
  m_13.io.memResp  <> StackFile.io.ReadOut(4)
  StackFile.io.ReadIn(4) <> m_13.io.memReq


  // Wiring Load instruction to the function argument
  m_14.io.GepAddr <> io.data_5
  m_14.io.memResp <> StackFile.io.ReadOut(5)
  StackFile.io.ReadIn(5) <> m_14.io.memReq


  // Wiring instructions
  m_15.io.LeftIO <> m_14.io.Out(param.m_15_in("m_14"))

  // Wiring instructions
  m_15.io.RightIO <> m_13.io.Out(param.m_15_in("m_13"))

  // Wiring instructions
  m_16.io.LeftIO <> m_10.io.Out(param.m_16_in("m_10"))

  // Wiring instructions
  m_16.io.RightIO <> m_15.io.Out(param.m_16_in("m_15"))

  // Wiring Binary instruction to the function argument
  m_17.io.LeftIO <> io.data_6


  // Wiring Binary instruction to the function argument
  m_17.io.RightIO <> io.data_0


  // Wiring GEP instruction to the function argument
  m_18.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_18.io.idx1 <> m_17.io.Out(param.m_18_in("m_17"))

  // Wiring Load instruction to the parent instruction
  m_19.io.GepAddr <> m_18.io.Out(param.m_19_in("m_18"))
  m_19.io.memResp  <> StackFile.io.ReadOut(6)
  StackFile.io.ReadIn(6) <> m_19.io.memReq


  // Wiring Load instruction to the function argument
  m_20.io.GepAddr <> io.data_7
  m_20.io.memResp <> StackFile.io.ReadOut(7)
  StackFile.io.ReadIn(7) <> m_20.io.memReq


  // Wiring instructions
  m_21.io.LeftIO <> m_20.io.Out(param.m_21_in("m_20"))

  // Wiring instructions
  m_21.io.RightIO <> m_19.io.Out(param.m_21_in("m_19"))

  // Wiring instructions
  m_22.io.LeftIO <> m_16.io.Out(param.m_22_in("m_16"))

  // Wiring instructions
  m_22.io.RightIO <> m_21.io.Out(param.m_22_in("m_21"))

  // Wiring instructions
  m_23.io.LeftIO <> m_17.io.Out(param.m_23_in("m_17"))

  // Wiring constant
  m_23.io.RightIO.bits.data := 1.U
  m_23.io.RightIO.bits.predicate := true.B
  m_23.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  m_24.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_24.io.idx1 <> m_23.io.Out(param.m_24_in("m_23"))

  // Wiring Load instruction to the parent instruction
  m_25.io.GepAddr <> m_24.io.Out(param.m_25_in("m_24"))
  m_25.io.memResp  <> StackFile.io.ReadOut(8)
  StackFile.io.ReadIn(8) <> m_25.io.memReq


  // Wiring Load instruction to the function argument
  m_26.io.GepAddr <> io.data_8
  m_26.io.memResp <> StackFile.io.ReadOut(9)
  StackFile.io.ReadIn(9) <> m_26.io.memReq


  // Wiring instructions
  m_27.io.LeftIO <> m_26.io.Out(param.m_27_in("m_26"))

  // Wiring instructions
  m_27.io.RightIO <> m_25.io.Out(param.m_27_in("m_25"))

  // Wiring instructions
  m_28.io.LeftIO <> m_22.io.Out(param.m_28_in("m_22"))

  // Wiring instructions
  m_28.io.RightIO <> m_27.io.Out(param.m_28_in("m_27"))

  // Wiring instructions
  m_29.io.LeftIO <> m_17.io.Out(param.m_29_in("m_17"))

  // Wiring constant
  m_29.io.RightIO.bits.data := 2.U
  m_29.io.RightIO.bits.predicate := true.B
  m_29.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  m_30.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_30.io.idx1 <> m_29.io.Out(param.m_30_in("m_29"))

  // Wiring Load instruction to the parent instruction
  m_31.io.GepAddr <> m_30.io.Out(param.m_31_in("m_30"))
  m_31.io.memResp  <> StackFile.io.ReadOut(10)
  StackFile.io.ReadIn(10) <> m_31.io.memReq


  // Wiring Load instruction to the function argument
  m_32.io.GepAddr <> io.data_9
  m_32.io.memResp <> StackFile.io.ReadOut(11)
  StackFile.io.ReadIn(11) <> m_32.io.memReq


  // Wiring instructions
  m_33.io.LeftIO <> m_32.io.Out(param.m_33_in("m_32"))

  // Wiring instructions
  m_33.io.RightIO <> m_31.io.Out(param.m_33_in("m_31"))

  // Wiring instructions
  m_34.io.LeftIO <> m_28.io.Out(param.m_34_in("m_28"))

  // Wiring instructions
  m_34.io.RightIO <> m_33.io.Out(param.m_34_in("m_33"))

  // Wiring Binary instruction to the function argument
  m_35.io.LeftIO <> io.data_10


  // Wiring Binary instruction to the function argument
  m_35.io.RightIO <> io.data_0


  // Wiring GEP instruction to the function argument
  m_36.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_36.io.idx1 <> m_35.io.Out(param.m_36_in("m_35"))

  // Wiring Load instruction to the parent instruction
  m_37.io.GepAddr <> m_36.io.Out(param.m_37_in("m_36"))
  m_37.io.memResp  <> StackFile.io.ReadOut(12)
  StackFile.io.ReadIn(12) <> m_37.io.memReq


  // Wiring Load instruction to the function argument
  m_38.io.GepAddr <> io.data_11
  m_38.io.memResp <> StackFile.io.ReadOut(13)
  StackFile.io.ReadIn(13) <> m_38.io.memReq


  // Wiring instructions
  m_39.io.LeftIO <> m_38.io.Out(param.m_39_in("m_38"))

  // Wiring instructions
  m_39.io.RightIO <> m_37.io.Out(param.m_39_in("m_37"))

  // Wiring instructions
  m_40.io.LeftIO <> m_34.io.Out(param.m_40_in("m_34"))

  // Wiring instructions
  m_40.io.RightIO <> m_39.io.Out(param.m_40_in("m_39"))

  // Wiring instructions
  m_41.io.LeftIO <> m_35.io.Out(param.m_41_in("m_35"))

  // Wiring constant
  m_41.io.RightIO.bits.data := 1.U
  m_41.io.RightIO.bits.predicate := true.B
  m_41.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  m_42.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_42.io.idx1 <> m_41.io.Out(param.m_42_in("m_41"))

  // Wiring Load instruction to the parent instruction
  m_43.io.GepAddr <> m_42.io.Out(param.m_43_in("m_42"))
  m_43.io.memResp  <> StackFile.io.ReadOut(14)
  StackFile.io.ReadIn(14) <> m_43.io.memReq


  // Wiring Load instruction to the function argument
  m_44.io.GepAddr <> io.data_12
  m_44.io.memResp <> StackFile.io.ReadOut(15)
  StackFile.io.ReadIn(15) <> m_44.io.memReq


  // Wiring instructions
  m_45.io.LeftIO <> m_44.io.Out(param.m_45_in("m_44"))

  // Wiring instructions
  m_45.io.RightIO <> m_43.io.Out(param.m_45_in("m_43"))

  // Wiring instructions
  m_46.io.LeftIO <> m_40.io.Out(param.m_46_in("m_40"))

  // Wiring instructions
  m_46.io.RightIO <> m_45.io.Out(param.m_46_in("m_45"))

  // Wiring instructions
  m_47.io.LeftIO <> m_35.io.Out(param.m_47_in("m_35"))

  // Wiring constant
  m_47.io.RightIO.bits.data := 2.U
  m_47.io.RightIO.bits.predicate := true.B
  m_47.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  m_48.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the parent instruction
  m_48.io.idx1 <> m_47.io.Out(param.m_48_in("m_47"))

  // Wiring Load instruction to the parent instruction
  m_49.io.GepAddr <> m_48.io.Out(param.m_49_in("m_48"))
  m_49.io.memResp  <> StackFile.io.ReadOut(16)
  StackFile.io.ReadIn(16) <> m_49.io.memReq


  // Wiring Load instruction to the function argument
  m_50.io.GepAddr <> io.data_13
  m_50.io.memResp <> StackFile.io.ReadOut(17)
  StackFile.io.ReadIn(17) <> m_50.io.memReq


  // Wiring instructions
  m_51.io.LeftIO <> m_50.io.Out(param.m_51_in("m_50"))

  // Wiring instructions
  m_51.io.RightIO <> m_49.io.Out(param.m_51_in("m_49"))

  // Wiring instructions
  m_52.io.LeftIO <> m_46.io.Out(param.m_52_in("m_46"))

  // Wiring instructions
  m_52.io.RightIO <> m_51.io.Out(param.m_52_in("m_51"))

  // Wiring instructions
  m_53.io.LeftIO <> m_52.io.Out(param.m_53_in("m_52"))

  // Wiring Binary instruction to the function argument
  m_53.io.RightIO <> io.data_14


  // Wiring GEP instruction to the function argument
  m_54.io.baseAddress <> io.data_15

  // Wiring GEP instruction to the parent instruction
  m_54.io.idx1 <> m_0.io.Out(param.m_54_in("m_0"))

  // Wiring Store instruction to the parent instruction
  m_55.io.GepAddr <> m_53.io.Out(param.m_55_in("m_53"))
  m_55.io.inData <> m_54.io.Out(param.m_55_in("m_54"))
  m_55.io.memResp  <> StackFile.io.WriteOut(0)
  StackFile.io.WriteIn(0) <> m_55.io.memReq
  m_55.io.Out(0).ready := true.B


  // Wiring Binary instruction to the function argument
  m_56.io.LeftIO <> io.data_0


  // Wiring constant
  m_56.io.RightIO.bits.data := 1.U
  m_56.io.RightIO.bits.predicate := true.B
  m_56.io.RightIO.valid := true.B

  // Wiring instructions
  m_57.io.LeftIO <> m_56.io.Out(param.m_57_in("m_56"))

  // Wiring Binary instruction to the function argument
  m_57.io.RightIO <> io.data_16


  // Wiring GEP instruction to the function argument
  m_59.io.baseAddress <> io.data_18

  // Wiring GEP instruction to the Constant
  m_59.io.idx1.valid :=  true.B
  m_59.io.idx1.bits.predicate :=  true.B
  m_59.io.idx1.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  m_59.io.idx2.valid :=  true.B
  m_59.io.idx2.bits.predicate :=  true.B
  m_59.io.idx2.bits.data :=  0.U

  // Wiring Store instruction to the parent instruction
  m_60.io.GepAddr <> m_56.io.Out(param.m_60_in("m_56"))
  m_60.io.inData <> m_59.io.Out(param.m_60_in("m_59"))
  m_60.io.memResp  <> StackFile.io.WriteOut(1)
  StackFile.io.WriteIn(1) <> m_60.io.memReq
  m_60.io.Out(0).ready := true.B


}

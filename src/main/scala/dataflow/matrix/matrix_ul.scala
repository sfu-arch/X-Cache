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
import accel._

/**
  * This Object should be initialize at the first step
  * It contains all the transformation from indecies to their module's name
  */

object Data___offload_func_3_FlowParam{
  val my___unk__0_pred = Map(
    "active" -> 0
  )

  val ret_fail_pred = Map(
    "m_33" -> 0
  )

  val g_pred = Map(
    "m_33" -> 0
  )

  val m_33_brn_bb = Map(
    "ret_fail" -> 0,
    "g" -> 1
  )

  val my___unk__0_activate = Map(
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
    "m_33" -> 33
  )

  val g_activate = Map(
    "m_34" -> 0,
    "m_35" -> 1,
    "m_36" -> 2
  )

  val ret_fail_activate = Map(
    "m_37" -> 0
  )

  val m_0_in = Map( 
    "data_1" -> 0,
    "data_0" -> 1
  )

  val m_1_in = Map( 
    "data_2" -> 0,
    "data_0" -> 1
  )

  val m_2_in = Map( 
    "data_3" -> 0,
    "data_0" -> 1
  )

  val m_3_in = Map( 
    "m_0" -> 0
  )

  val m_4_in = Map( 
    "m_3" -> 0
  )

  val m_5_in = Map( 
    "m_1" -> 0
  )

  val m_6_in = Map( 
    "m_5" -> 0
  )

  val m_7_in = Map( 
    "m_6" -> 0,
    "m_4" -> 0
  )

  val m_8_in = Map( 
    "m_2" -> 0
  )

  val m_9_in = Map( 
    "m_7" -> 0,
    "m_8" -> 0
  )

  val m_10_in = Map( 
    "m_3" -> 1
  )

  val m_11_in = Map( 
    "m_10" -> 0
  )

  val m_12_in = Map( 
    "m_5" -> 1
  )

  val m_13_in = Map( 
    "m_12" -> 0
  )

  val m_14_in = Map( 
    "m_13" -> 0,
    "m_11" -> 0
  )

  val m_15_in = Map( 
    "m_8" -> 1
  )

  val m_16_in = Map( 
    "m_14" -> 0,
    "m_15" -> 0
  )

  val m_17_in = Map( 
    "m_3" -> 2
  )

  val m_18_in = Map( 
    "m_17" -> 0
  )

  val m_19_in = Map( 
    "m_5" -> 2
  )

  val m_20_in = Map( 
    "m_19" -> 0
  )

  val m_21_in = Map( 
    "m_20" -> 0,
    "m_18" -> 0
  )

  val m_22_in = Map( 
    "m_8" -> 2
  )

  val m_23_in = Map( 
    "m_21" -> 0,
    "m_22" -> 0
  )

  val m_24_in = Map( 
    "m_3" -> 3
  )

  val m_25_in = Map( 
    "m_24" -> 0
  )

  val m_26_in = Map( 
    "m_5" -> 3
  )

  val m_27_in = Map( 
    "m_26" -> 0
  )

  val m_28_in = Map( 
    "m_27" -> 0,
    "m_25" -> 0
  )

  val m_29_in = Map( 
    "m_8" -> 3
  )

  val m_30_in = Map( 
    "m_28" -> 0,
    "m_29" -> 0
  )

  val m_31_in = Map( 
    "data_0" -> 0
  )

  val m_32_in = Map( 
    "m_31" -> 0
  )

  val m_34_in = Map( 
    "data_5" -> 0
  )

  val m_35_in = Map( 
    "m_31" -> 1,
    "m_34" -> 0
  )

  val m_36_in = Map(
  )

  val m_37_in = Map(
  )

}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class __offload_func_3DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val data_0 = Flipped(Decoupled(new DataBundle))
    val data_1 = Flipped(Decoupled(new DataBundle))
    val data_2 = Flipped(Decoupled(new DataBundle))
    val data_3 = Flipped(Decoupled(new DataBundle))
    val data_4 = Flipped(Decoupled(new DataBundle))
    val data_5 = Flipped(Decoupled(new DataBundle))
    val CacheReq = Decoupled(new CacheReq)
    val CacheResp = Flipped(Valid(new CacheRespT))
    val pred = Decoupled(new Bool())
    val start = Input(new Bool())
    val result = Decoupled(new DataBundle)
  })
}

class __offload_func_3DF(implicit p: Parameters) extends __offload_func_3DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */




  /* ================================================================== *
   *                   PRINTING BASICBLOCKS                             *
   * ================================================================== */

  //Initializing BasicBlocks: 
  val my___unk__0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 34, BID = 0)(p))
  val g = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 1)(p))
  val ret_fail = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2)(p))





  /* ================================================================== *
   *                   PRINTING STACKFILE                               *
   * ================================================================== */

//	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=11,NWrites=5)
	val StackFile = Module(new UnifiedController(ID=0,Size=32,NReads=11,NWrites=5)
		            (WControl=new WriteMemoryController(NumOps=5,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=11,BaseSize=2,NumEntries=2))
			    (RWArbiter=new ReadWriteArbiter()))

        io.CacheReq <> StackFile.io.CacheReq
        StackFile.io.CacheResp <>io.CacheResp

  /* ================================================================== *
   *                   PRINTING INSTURCTIONS                            *
   * ================================================================== */

  //Initializing Instructions: 
  // my___unk__0: 

  //  %2 = getelementptr inbounds i32*, i32** %vr.0, i32 %i.02.i.in, !UID !5
  val m_0 = Module (new GepOneNode(NumOuts = 1, ID = 0)(numByte1 = 0)(p))

  //  %3 = getelementptr inbounds i32*, i32** %vr.1, i32 %i.02.i.in, !UID !6
  val m_1 = Module (new GepOneNode(NumOuts = 1, ID = 1)(numByte1 = 0)(p))

  //  %4 = getelementptr inbounds i32*, i32** %vr.2, i32 %i.02.i.in, !UID !7
  val m_2 = Module (new GepOneNode(NumOuts = 1, ID = 2)(numByte1 = 0)(p))

  //  %5 = load i32*, i32** %2, align 4, !UID !8
  val m_3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=4,ID=3,RouteID=0))

  //  %6 = load i32, i32* %5, align 4, !UID !9
  val m_4 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=4,RouteID=1))

  //  %7 = load i32*, i32** %3, align 4, !UID !10
  val m_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=4,ID=5,RouteID=2))

  //  %8 = load i32, i32* %7, align 4, !UID !11
  val m_6 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=6,RouteID=3))

  //  %9 = add nsw i32 %8, %6, !UID !12
  val m_7 = Module (new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign=false)(p))

  //  %10 = load i32*, i32** %4, align 4, !UID !13
  val m_8 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=4,ID=8,RouteID=4))

  //  store i32 %9, i32* %10, align 4, !UID !14
  val m_9 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=9,RouteID=11))

  //  %11 = getelementptr inbounds i32, i32* %5, i32 1, !UID !15
  val m_10 = Module (new GepOneNode(NumOuts = 1, ID = 10)(numByte1 = 4)(p))

  //  %12 = load i32, i32* %11, align 4, !UID !16
  val m_11 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=11,RouteID=5))

  //  %13 = getelementptr inbounds i32, i32* %7, i32 1, !UID !17
  val m_12 = Module (new GepOneNode(NumOuts = 1, ID = 12)(numByte1 = 4)(p))

  //  %14 = load i32, i32* %13, align 4, !UID !18
  val m_13 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=13,RouteID=6))

  //  %15 = add nsw i32 %14, %12, !UID !19
  val m_14 = Module (new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign=false)(p))

  //  %16 = getelementptr inbounds i32, i32* %10, i32 1, !UID !20
  val m_15 = Module (new GepOneNode(NumOuts = 1, ID = 15)(numByte1 = 4)(p))

  //  store i32 %15, i32* %16, align 4, !UID !21
  val m_16 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=16,RouteID=11))

  //  %17 = getelementptr inbounds i32, i32* %5, i32 2, !UID !22
  val m_17 = Module (new GepOneNode(NumOuts = 1, ID = 17)(numByte1 = 4)(p))

  //  %18 = load i32, i32* %17, align 4, !UID !23
  val m_18 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=18,RouteID=7))

  //  %19 = getelementptr inbounds i32, i32* %7, i32 2, !UID !24
  val m_19 = Module (new GepOneNode(NumOuts = 1, ID = 19)(numByte1 = 4)(p))

  //  %20 = load i32, i32* %19, align 4, !UID !25
  val m_20 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=20,RouteID=8))

  //  %21 = add nsw i32 %20, %18, !UID !26
  val m_21 = Module (new ComputeNode(NumOuts = 1, ID = 21, opCode = "add")(sign=false)(p))

  //  %22 = getelementptr inbounds i32, i32* %10, i32 2, !UID !27
  val m_22 = Module (new GepOneNode(NumOuts = 1, ID = 22)(numByte1 = 4)(p))

  //  store i32 %21, i32* %22, align 4, !UID !28
  val m_23 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=23,RouteID=11))

  //  %23 = getelementptr inbounds i32, i32* %5, i32 3, !UID !29
  val m_24 = Module (new GepOneNode(NumOuts = 1, ID = 24)(numByte1 = 4)(p))

  //  %24 = load i32, i32* %23, align 4, !UID !30
  val m_25 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=25,RouteID=9))

  //  %25 = getelementptr inbounds i32, i32* %7, i32 3, !UID !31
  val m_26 = Module (new GepOneNode(NumOuts = 1, ID = 26)(numByte1 = 4)(p))

  //  %26 = load i32, i32* %25, align 4, !UID !32
  val m_27 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=27,RouteID=10))

  //  %27 = add nsw i32 %26, %24, !UID !33
  val m_28 = Module (new ComputeNode(NumOuts = 1, ID = 28, opCode = "add")(sign=false)(p))

  //  %28 = getelementptr inbounds i32, i32* %10, i32 3, !UID !34
  val m_29 = Module (new GepOneNode(NumOuts = 1, ID = 29)(numByte1 = 4)(p))

  //  store i32 %27, i32* %28, align 4, !UID !35
  val m_30 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=30,RouteID=11))

  //  %29 = add nuw nsw i32 %i.02.i.in, 1, !UID !36
  val m_31 = Module (new ComputeNode(NumOuts = 2, ID = 31, opCode = "add")(sign=false)(p))

  //  %30 = icmp eq i32 %29, 4, !UID !37
  val m_32 = Module (new IcmpNode(NumOuts = 1, ID = 32, opCode = "EQ")(sign=false)(p))

  //  br i1 %30, label %ret.fail, label %g, !UID !38, !BB_UID !39
  val m_33 = Module (new CBranchNode(ID = 33)(p))

  // g: 

  //  %31 = getelementptr <{ i32 }>, <{ i32 }>* %1, i32 0, i32 0, !UID !40
  val m_34 = Module (new GepTwoNode(NumOuts = 1, ID = 34)(numByte1 = 4, numByte2 = 0)(p))

  //  store i32 %29, i32* %31, align 4, !UID !41, !LO !42
  val m_35 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=35,RouteID=11))

  //  ret i1 true, !UID !43, !BB_UID !44
  //val m_36 = Module (new RetNode(ID = 36)(p))

  // ret.fail: 

  //  ret i1 false, !UID !45, !BB_UID !46
  //val m_37 = Module (new RetNode(ID = 37)(p))






  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */

  /**
    * Instantiating parameters
    */
  val param = Data___offload_func_3_FlowParam




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO PREDICATE INSTRUCTIONS *
   * ================================================================== */

  /**
     * Connecting basic blocks to predicate instructions
     */

  //We always ground entry BasicBlock
  my___unk__0.io.predicateIn(param.my___unk__0_pred("active")).bits  := ControlBundle.Activate
  my___unk__0.io.predicateIn(param.my___unk__0_pred("active")).valid := true.B

  /**
    * Connecting basic blocks to predicate instructions
    */
  //Connecting m_33 to ret_fail
  ret_fail.io.predicateIn(param.ret_fail_pred("m_33")) <> m_33.io.Out(param.m_33_brn_bb("ret_fail"))

  //Connecting m_33 to g
  g.io.predicateIn(param.g_pred("m_33")) <> m_33.io.Out(param.m_33_brn_bb("g"))




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO INSTRUCTIONS           *
   * ================================================================== */

  /**
    * Wireing enable signals to the instructions
    */
  //Wiring enable signals
  m_0.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_0"))
  m_1.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_1"))
  m_2.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_2"))
  m_3.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_3"))
  m_4.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_4"))
  m_5.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_5"))
  m_6.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_6"))
  m_7.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_7"))
  m_8.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_8"))
  m_9.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_9"))
  m_10.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_10"))
  m_11.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_11"))
  m_12.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_12"))
  m_13.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_13"))
  m_14.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_14"))
  m_15.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_15"))
  m_16.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_16"))
  m_17.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_17"))
  m_18.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_18"))
  m_19.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_19"))
  m_20.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_20"))
  m_21.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_21"))
  m_22.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_22"))
  m_23.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_23"))
  m_24.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_24"))
  m_25.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_25"))
  m_26.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_26"))
  m_27.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_27"))
  m_28.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_28"))
  m_29.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_29"))
  m_30.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_30"))
  m_31.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_31"))
  m_32.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_32"))
  m_33.io.enable <> my___unk__0.io.Out(param.my___unk__0_activate("m_33"))

  m_34.io.enable <> g.io.Out(param.g_activate("m_34"))
  m_35.io.enable <> g.io.Out(param.g_activate("m_35"))
  //m_36.io.enable <> g.io.Out(param.g_activate("m_36"))

  //m_37.io.enable <> ret_fail.io.Out(param.ret_fail_activate("m_37"))




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
  // Wiring GEP instruction to the function argument
  m_0.io.baseAddress <> io.data_1

  // Wiring GEP instruction to the function argument
  m_0.io.baseAddress <> io.data_1

  // Wiring GEP instruction to the function argument
  m_1.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the function argument
  m_1.io.baseAddress <> io.data_2

  // Wiring GEP instruction to the function argument
  m_2.io.baseAddress <> io.data_3

  // Wiring GEP instruction to the function argument
  m_2.io.baseAddress <> io.data_3

  // Wiring Load instruction to the parent instruction
  m_3.io.GepAddr <> m_0.io.Out(param.m_3_in("m_0"))
  m_3.io.memResp  <> StackFile.io.ReadOut(0)
  StackFile.io.ReadIn(0) <> m_3.io.memReq


  // Wiring Load instruction to the parent instruction
  m_4.io.GepAddr <> m_3.io.Out(param.m_4_in("m_3"))
  m_4.io.memResp  <> StackFile.io.ReadOut(1)
  StackFile.io.ReadIn(1) <> m_4.io.memReq


  // Wiring Load instruction to the parent instruction
  m_5.io.GepAddr <> m_1.io.Out(param.m_5_in("m_1"))
  m_5.io.memResp  <> StackFile.io.ReadOut(2)
  StackFile.io.ReadIn(2) <> m_5.io.memReq


  // Wiring Load instruction to the parent instruction
  m_6.io.GepAddr <> m_5.io.Out(param.m_6_in("m_5"))
  m_6.io.memResp  <> StackFile.io.ReadOut(3)
  StackFile.io.ReadIn(3) <> m_6.io.memReq


  // Wiring instructions
  m_7.io.LeftIO <> m_6.io.Out(param.m_7_in("m_6"))

  // Wiring instructions
  m_7.io.RightIO <> m_4.io.Out(param.m_7_in("m_4"))

  // Wiring Load instruction to the parent instruction
  m_8.io.GepAddr <> m_2.io.Out(param.m_8_in("m_2"))
  m_8.io.memResp  <> StackFile.io.ReadOut(4)
  StackFile.io.ReadIn(4) <> m_8.io.memReq


  // Wiring Store instruction to the parent instruction
  m_9.io.inData <> m_7.io.Out(param.m_9_in("m_7"))
  m_9.io.memResp  <> StackFile.io.WriteOut(0)
  StackFile.io.WriteIn(0) <> m_9.io.memReq


  // Wiring Store instruction to the parent instruction
  m_9.io.inData <> m_8.io.Out(param.m_9_in("m_8"))
  m_9.io.memResp  <> StackFile.io.WriteOut(0)
  StackFile.io.WriteIn(0) <> m_9.io.memReq


  // Wiring GEP instruction to the parent instruction
  m_10.io.baseAddress <> m_3.io.Out(param.m_10_in("m_3"))

  // Wiring GEP instruction to the Constant
  m_10.io.idx1.valid :=  true.B
  m_10.io.idx1.bits.predicate :=  true.B
  m_10.io.idx1.bits.data :=  1.U

  // Wiring Load instruction to the parent instruction
  m_11.io.GepAddr <> m_10.io.Out(param.m_11_in("m_10"))
  m_11.io.memResp  <> StackFile.io.ReadOut(5)
  StackFile.io.ReadIn(5) <> m_11.io.memReq


  // Wiring GEP instruction to the parent instruction
  m_12.io.baseAddress <> m_5.io.Out(param.m_12_in("m_5"))

  // Wiring GEP instruction to the Constant
  m_12.io.idx1.valid :=  true.B
  m_12.io.idx1.bits.predicate :=  true.B
  m_12.io.idx1.bits.data :=  1.U

  // Wiring Load instruction to the parent instruction
  m_13.io.GepAddr <> m_12.io.Out(param.m_13_in("m_12"))
  m_13.io.memResp  <> StackFile.io.ReadOut(6)
  StackFile.io.ReadIn(6) <> m_13.io.memReq


  // Wiring instructions
  m_14.io.LeftIO <> m_13.io.Out(param.m_14_in("m_13"))

  // Wiring instructions
  m_14.io.RightIO <> m_11.io.Out(param.m_14_in("m_11"))

  // Wiring GEP instruction to the parent instruction
  m_15.io.baseAddress <> m_8.io.Out(param.m_15_in("m_8"))

  // Wiring GEP instruction to the Constant
  m_15.io.idx1.valid :=  true.B
  m_15.io.idx1.bits.predicate :=  true.B
  m_15.io.idx1.bits.data :=  1.U

  // Wiring Store instruction to the parent instruction
  m_16.io.inData <> m_14.io.Out(param.m_16_in("m_14"))
  m_16.io.memResp  <> StackFile.io.WriteOut(1)
  StackFile.io.WriteIn(1) <> m_16.io.memReq


  // Wiring Store instruction to the parent instruction
  m_16.io.inData <> m_15.io.Out(param.m_16_in("m_15"))
  m_16.io.memResp  <> StackFile.io.WriteOut(1)
  StackFile.io.WriteIn(1) <> m_16.io.memReq


  // Wiring GEP instruction to the parent instruction
  m_17.io.baseAddress <> m_3.io.Out(param.m_17_in("m_3"))

  // Wiring GEP instruction to the Constant
  m_17.io.idx1.valid :=  true.B
  m_17.io.idx1.bits.predicate :=  true.B
  m_17.io.idx1.bits.data :=  2.U

  // Wiring Load instruction to the parent instruction
  m_18.io.GepAddr <> m_17.io.Out(param.m_18_in("m_17"))
  m_18.io.memResp  <> StackFile.io.ReadOut(7)
  StackFile.io.ReadIn(7) <> m_18.io.memReq


  // Wiring GEP instruction to the parent instruction
  m_19.io.baseAddress <> m_5.io.Out(param.m_19_in("m_5"))

  // Wiring GEP instruction to the Constant
  m_19.io.idx1.valid :=  true.B
  m_19.io.idx1.bits.predicate :=  true.B
  m_19.io.idx1.bits.data :=  2.U

  // Wiring Load instruction to the parent instruction
  m_20.io.GepAddr <> m_19.io.Out(param.m_20_in("m_19"))
  m_20.io.memResp  <> StackFile.io.ReadOut(8)
  StackFile.io.ReadIn(8) <> m_20.io.memReq


  // Wiring instructions
  m_21.io.LeftIO <> m_20.io.Out(param.m_21_in("m_20"))

  // Wiring instructions
  m_21.io.RightIO <> m_18.io.Out(param.m_21_in("m_18"))

  // Wiring GEP instruction to the parent instruction
  m_22.io.baseAddress <> m_8.io.Out(param.m_22_in("m_8"))

  // Wiring GEP instruction to the Constant
  m_22.io.idx1.valid :=  true.B
  m_22.io.idx1.bits.predicate :=  true.B
  m_22.io.idx1.bits.data :=  2.U

  // Wiring Store instruction to the parent instruction
  m_23.io.inData <> m_21.io.Out(param.m_23_in("m_21"))
  m_23.io.memResp  <> StackFile.io.WriteOut(2)
  StackFile.io.WriteIn(2) <> m_23.io.memReq


  // Wiring Store instruction to the parent instruction
  m_23.io.inData <> m_22.io.Out(param.m_23_in("m_22"))
  m_23.io.memResp  <> StackFile.io.WriteOut(2)
  StackFile.io.WriteIn(2) <> m_23.io.memReq


  // Wiring GEP instruction to the parent instruction
  m_24.io.baseAddress <> m_3.io.Out(param.m_24_in("m_3"))

  // Wiring GEP instruction to the Constant
  m_24.io.idx1.valid :=  true.B
  m_24.io.idx1.bits.predicate :=  true.B
  m_24.io.idx1.bits.data :=  3.U

  // Wiring Load instruction to the parent instruction
  m_25.io.GepAddr <> m_24.io.Out(param.m_25_in("m_24"))
  m_25.io.memResp  <> StackFile.io.ReadOut(9)
  StackFile.io.ReadIn(9) <> m_25.io.memReq


  // Wiring GEP instruction to the parent instruction
  m_26.io.baseAddress <> m_5.io.Out(param.m_26_in("m_5"))

  // Wiring GEP instruction to the Constant
  m_26.io.idx1.valid :=  true.B
  m_26.io.idx1.bits.predicate :=  true.B
  m_26.io.idx1.bits.data :=  3.U

  // Wiring Load instruction to the parent instruction
  m_27.io.GepAddr <> m_26.io.Out(param.m_27_in("m_26"))
  m_27.io.memResp  <> StackFile.io.ReadOut(10)
  StackFile.io.ReadIn(10) <> m_27.io.memReq


  // Wiring instructions
  m_28.io.LeftIO <> m_27.io.Out(param.m_28_in("m_27"))

  // Wiring instructions
  m_28.io.RightIO <> m_25.io.Out(param.m_28_in("m_25"))

  // Wiring GEP instruction to the parent instruction
  m_29.io.baseAddress <> m_8.io.Out(param.m_29_in("m_8"))

  // Wiring GEP instruction to the Constant
  m_29.io.idx1.valid :=  true.B
  m_29.io.idx1.bits.predicate :=  true.B
  m_29.io.idx1.bits.data :=  3.U

  // Wiring Store instruction to the parent instruction
  m_30.io.inData <> m_28.io.Out(param.m_30_in("m_28"))
  m_30.io.memResp  <> StackFile.io.WriteOut(3)
  StackFile.io.WriteIn(3) <> m_30.io.memReq


  // Wiring Store instruction to the parent instruction
  m_30.io.inData <> m_29.io.Out(param.m_30_in("m_29"))
  m_30.io.memResp  <> StackFile.io.WriteOut(3)
  StackFile.io.WriteIn(3) <> m_30.io.memReq


  // Wiring Binary instruction to the function argument
  m_31.io.LeftIO <> io.data_0


  // Wiring constant
  m_31.io.RightIO.bits.data := 1.U
  m_31.io.RightIO.bits.predicate := true.B
  m_31.io.RightIO.valid := true.B

  // Wiring instructions
  m_32.io.LeftIO <> m_31.io.Out(param.m_32_in("m_31"))

  // Wiring constant
  m_32.io.RightIO.bits.data := 4.U
  m_32.io.RightIO.bits.predicate := true.B
  m_32.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  m_34.io.baseAddress <> io.data_5

  // Wiring GEP instruction to the Constant
  m_34.io.idx1.valid :=  true.B
  m_34.io.idx1.bits.predicate :=  true.B
  m_34.io.idx1.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  m_34.io.idx2.valid :=  true.B
  m_34.io.idx2.bits.predicate :=  true.B
  m_34.io.idx2.bits.data :=  0.U

  // Wiring Store instruction to the parent instruction
  m_35.io.inData <> m_31.io.Out(param.m_35_in("m_31"))
  m_35.io.memResp  <> StackFile.io.WriteOut(4)
  StackFile.io.WriteIn(4) <> m_35.io.memReq


  // Wiring Store instruction to the parent instruction
  m_35.io.inData <> m_34.io.Out(param.m_35_in("m_34"))
  m_35.io.memResp  <> StackFile.io.WriteOut(4)
  StackFile.io.WriteIn(4) <> m_35.io.memReq


}

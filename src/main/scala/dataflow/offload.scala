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

object DataFlowParam{



  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

  val entry_pred = Map(
    "active" -> 0
  )

  val g_pred = Map(
    "m_10" -> 0
  )

  val ret_fail_pred = Map(
    "m_10" -> 0
  )

  val m_10_brn_bb = Map(
    "g" -> 0,
    "ret_fail" -> 1
  )

  val entry_activate = Map(
    "m_0" -> 0,
    "" -> 1,
    "m_1" -> 2,
    "m_2" -> 3,
    "m_3" -> 4,
    "m_4" -> 5,
    "m_5" -> 6,
    "m_6" -> 7,
    "m_7" -> 8,
    "m_8" -> 9,
    "m_9" -> 10,
    "m_10" -> 11
  )

  val g_activate = Map(
    "m_11" -> 0,
    "m_12" -> 1,
    "m_13" -> 2
  )

  val ret_fail_activate = Map(
    "m_14" -> 0
  )

  val m_0_in = Map( 
    "data_0" -> 0
  )

  val m_1_in = Map( 
    "data_1" -> 0
  )

  val m_2_in = Map( 
    "m_1" -> 0
  )

  val m_3_in = Map( 
    "m_2" -> 0
  )

  val m_4_in = Map( 
    "m_3" -> 0
  )

  val m_5_in = Map( 
    "data_2" -> 0
  )

  val m_6_in = Map( 
    "m_5" -> 0
  )

  val m_7_in = Map( 
    "m_6" -> 0
  )

  val m_8_in = Map( 
    "data_3" -> 0
  )

  val m_9_in = Map( 
    "m_6" -> 0,
    "m_8" -> 0
  )

  val m_11_in = Map( 
    "data_5" -> 0
  )

  val m_12_in = Map( 
    "m_6" -> 0,
    "m_11" -> 0
  )

  val m_13_in = Map(
  )

  val m_14_in = Map(
  )

}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */

abstract class __offload_func_0DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val data_0 = Flipped(Decoupled(new DataBundle))
    val data_1 = Flipped(Decoupled(new DataBundle))
    val data_2 = Flipped(Decoupled(new DataBundle))
    val data_3 = Flipped(Decoupled(new DataBundle))
    val data_4 = Flipped(Decoupled(new DataBundle))
    val data_5 = Flipped(Decoupled(new DataBundle))
    val pred = Decoupled(new Bool())
    val start = Input(new Bool())
    val result = Decoupled(new DataBundle)
  })
}

class __offload_func_0DF(implicit p: Parameters) extends __offload_func_0DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING BASICBLOCKS                             *
   * ================================================================== */

  //Initializing BasicBlocks: 
  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 12, BID = 0)(p))
  val g = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 1)(p))
  val ret_fail = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2)(p))





  /* ================================================================== *
   *                   PRINTING STACKFILE                               *
   * ================================================================== */

	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=5,NWrites=5)
		            (WControl=new WriteMemoryController(NumOps=5,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=5,BaseSize=2,NumEntries=2)))


  /* ================================================================== *
   *                   PRINTING INSTURCTIONS                            *
   * ================================================================== */

  //Initializing Instructions: 
  // my_.lr.ph: 

  //  %2 = sext i32 %vr.0 to i64, !UID !4
  val m_0 = Module(new SextNode(SrcW = 32, DesW = 64, NumOuts=1))

  //  %3 = load i32*, i32** %vr.1, align 8, !UID !6
  val m_1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))

  //  %4 = getelementptr inbounds i32, i32* %3, i64 %2, !UID !7
  val m_2 = Module (new GepOneNode(NumOuts = 1, ID = 2)(numByte1 = 0)(p))

  //  %5 = load i32, i32* %4, align 4, !UID !8
  val m_3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=3,RouteID=1))

  //  %6 = add nsw i32 %5, 1, !UID !9
  val m_4 = Module (new ComputeNode(NumOuts = 1, ID = 4, opCode = "add")(sign=false)(p))

  //  %7 = load i32, i32* %i.in, align 4, !UID !10
  val m_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=5,RouteID=2))

  //  %8 = add nsw i32 %7, 1, !UID !11
  val m_6 = Module (new ComputeNode(NumOuts = 3, ID = 6, opCode = "add")(sign=false)(p))

  //  store i32 %8, i32* %i.in, align 4, !UID !12
  val m_7 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=4))

  //  %9 = load i32, i32* %vr.2, align 4, !UID !13
  val m_8 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=8,RouteID=3))

  //  %10 = icmp slt i32 %8, %9, !UID !14
  val m_9 = Module (new IcmpNode(NumOuts = 1, ID = 9, opCode = "SLT")(sign=false)(p))

  //  br i1 %10, label %g, label %ret.fail, !UID !15, !BB_UID !16
  val m_10 = Module (new CBranchNode(ID = 10)(p))

  // g: 

  //  %11 = getelementptr <{ i32 }>, <{ i32 }>* %1, i64 0, i32 0, !UID !17
  val m_11 = Module (new GepTwoNode(NumOuts = 1, ID = 11)(numByte1 = 4, numByte2 = 0)(p))

  //  store i32 %8, i32* %11, align 4, !UID !18, !LO !19
  val m_12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=12,RouteID=4))

  //  ret i1 true, !UID !20, !BB_UID !21
//  val m_13 = Module (new RetNode(ID = 13)(p))

  // ret.fail: 

  //  ret i1 false, !UID !22, !BB_UID !23
//  val m_14 = Module (new RetNode(ID = 14)(p))






  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */

  /**
    * Instantiating parameters
    */
  val param = DataFlowParam




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO PREDICATE INSTRUCTIONS *
   * ================================================================== */

  /**
     * Connecting basic blocks to predicate instructions
     */

  //We always ground entry BasicBlock
  entry.io.predicateIn(param.entry_pred("active")).bits  := ControlBundle.Activate
  entry.io.predicateIn(param.entry_pred("active")).valid := true.B

  /**
    * Connecting basic blocks to predicate instructions
    */
  //Connecting m_10 to g
  g.io.predicateIn(param.g_pred("m_10")) <> m_10.io.Out(param.m_10_brn_bb("g"))

  //Connecting m_10 to ret_fail
  ret_fail.io.predicateIn(param.ret_fail_pred("m_10")) <> m_10.io.Out(param.m_10_brn_bb("ret_fail"))




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO INSTRUCTIONS           *
   * ================================================================== */

  /**
    * Wireing enable signals to the instructions
    */
  //Wiring enable signals
  m_0.io.enable <> entry.io.Out(param.entry_activate("m_0"))
  m_1.io.enable <> entry.io.Out(param.entry_activate("m_1"))
  m_2.io.enable <> entry.io.Out(param.entry_activate("m_2"))
  m_3.io.enable <> entry.io.Out(param.entry_activate("m_3"))
  m_4.io.enable <> entry.io.Out(param.entry_activate("m_4"))
  m_5.io.enable <> entry.io.Out(param.entry_activate("m_5"))
  m_6.io.enable <> entry.io.Out(param.entry_activate("m_6"))
  m_7.io.enable <> entry.io.Out(param.entry_activate("m_7"))
  m_8.io.enable <> entry.io.Out(param.entry_activate("m_8"))
  m_9.io.enable <> entry.io.Out(param.entry_activate("m_9"))
  m_10.io.enable <> entry.io.Out(param.entry_activate("m_10"))

  m_11.io.enable <> g.io.Out(param.g_activate("m_11"))
  m_12.io.enable <> g.io.Out(param.g_activate("m_12"))


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
  // Wiring SEXT instruction to the function argument
  //m_0.io.Input <> io.data_0

  // Wiring Load instruction to the function argument
  m_1.io.GepAddr <> io.data_1
  m_1.io.memResp <> StackFile.io.ReadOut(0)
  StackFile.io.ReadIn(0) <> m_1.io.memReq


  // Wiring GEP instruction to the parent instruction
  m_2.io.baseAddress <> m_1.io.Out(param.m_2_in("m_1"))

  // Wiring GEP instruction to the parent instruction
  m_2.io.idx1 <> io.data_0 
  //m_2.io.idx1 <> m_0.io.Out(param.m_2_in("m_0"))

  // Wiring Load instruction to the parent instruction
  m_3.io.GepAddr <> m_2.io.Out(param.m_3_in("m_2"))
  m_3.io.memResp  <> StackFile.io.ReadOut(1)
  StackFile.io.ReadIn(1) <> m_3.io.memReq


  // Wiring instructions
  m_4.io.LeftIO <> m_3.io.Out(param.m_4_in("m_3"))

  // Wiring constant
  m_4.io.RightIO.bits.data := 1.U
  m_4.io.RightIO.bits.predicate := true.B
  m_4.io.RightIO.valid := true.B

  // Wiring Load instruction to the function argument
  m_5.io.GepAddr <> io.data_2
  m_5.io.memResp <> StackFile.io.ReadOut(2)
  StackFile.io.ReadIn(2) <> m_5.io.memReq


  // Wiring instructions
  m_6.io.LeftIO <> m_5.io.Out(param.m_6_in("m_5"))

  // Wiring constant
  m_6.io.RightIO.bits.data := 1.U
  m_6.io.RightIO.bits.predicate := true.B
  m_6.io.RightIO.valid := true.B

  // Wiring Store instruction to the parent instruction
  m_7.io.inData <> m_6.io.Out(param.m_7_in("m_6"))
  m_7.io.memResp  <> StackFile.io.WriteOut(4)
  StackFile.io.WriteIn(4) <> m_7.io.memReq


  // Wiring Store instruction to the function argument
  m_7.io.GepAddr <> io.data_2
  m_7.io.memResp  <> StackFile.io.WriteOut(4)
  StackFile.io.WriteIn(4) <> m_7.io.memReq


  // Wiring Load instruction to the function argument
  m_8.io.GepAddr <> io.data_3
  m_8.io.memResp <> StackFile.io.ReadOut(3)
  StackFile.io.ReadIn(3) <> m_8.io.memReq


  // Wiring instructions
  m_9.io.LeftIO <> m_6.io.Out(param.m_9_in("m_6"))

  // Wiring instructions
  m_9.io.RightIO <> m_8.io.Out(param.m_9_in("m_8"))

  // Wiring GEP instruction to the function argument
  m_11.io.baseAddress <> io.data_5

  // Wiring GEP instruction to the Constant
  m_11.io.idx1.valid :=  true.B
  m_11.io.idx1.bits.predicate :=  true.B
  m_11.io.idx1.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  m_11.io.idx2.valid :=  true.B
  m_11.io.idx2.bits.predicate :=  true.B
  m_11.io.idx2.bits.data :=  0.U

  // Wiring Store instruction to the parent instruction
  m_12.io.inData <> m_6.io.Out(param.m_12_in("m_6"))
  m_12.io.memResp  <> StackFile.io.WriteOut(4)
  StackFile.io.WriteIn(4) <> m_12.io.memReq


  // Wiring Store instruction to the parent instruction
  m_12.io.inData <> m_11.io.Out(param.m_12_in("m_11"))
  m_12.io.memResp  <> StackFile.io.WriteOut(4)
  StackFile.io.WriteIn(4) <> m_12.io.memReq


}

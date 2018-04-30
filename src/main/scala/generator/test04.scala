package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import config._
import control._
import util._
import interfaces._
import regfile._
import memory._
import stack._
import arbiters._
import loop._
import accel._
import node._
import junctions._


/**
  * This Object should be initialized at the first step
  * It contains all the transformation from indices to their module's name
  */

object Data_test04_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_if_else_pred = Map(
    "br7" -> 0
  )


  val bb_Minim_Loop_pred = Map(
    "br0" -> 0
  )


  val bb_while_cond_pred = Map(
    "br1" -> 0,
    "br14" -> 1
  )


  val bb_if_end_pred = Map(
    "br9" -> 0,
    "br11" -> 1
  )


  val bb_while_body_pred = Map(
    "br5" -> 0
  )


  val bb_while_end_pred = Map(
    "br5" -> 0
  )


  val bb_if_then_pred = Map(
    "br7" -> 0
  )


  val br0_brn_bb = Map(
    "bb_Minim_Loop" -> 0
  )


  val br1_brn_bb = Map(
    "bb_while_cond" -> 0
  )


  val br5_brn_bb = Map(
    "bb_while_body" -> 0,
    "bb_while_end" -> 1
  )


  val br7_brn_bb = Map(
    "bb_if_then" -> 0,
    "bb_if_else" -> 1
  )


  val br9_brn_bb = Map(
    "bb_if_end" -> 0
  )


  val br11_brn_bb = Map(
    "bb_if_end" -> 0
  )


  val br14_brn_bb = Map(
    "bb_while_cond" -> 0
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_Minim_Loop_activate = Map(
    "br1" -> 0
  )


  val bb_while_cond_activate = Map(
    "phi2" -> 0,
    "phi3" -> 1,
    "icmp4" -> 2,
    "br5" -> 3
  )


  val bb_while_body_activate = Map(
    "icmp6" -> 0,
    "br7" -> 1
  )


  val bb_if_then_activate = Map(
    "sub8" -> 0,
    "br9" -> 1
  )


  val bb_if_else_activate = Map(
    "sub10" -> 0,
    "br11" -> 1
  )


  val bb_if_end_activate = Map(
    "phi12" -> 0,
    "phi13" -> 1,
    "br14" -> 2
  )


  val bb_while_end_activate = Map(
    "ret15" -> 0
  )


  val phi2_phi_in = Map(
    "field1" -> 0,
    "phi12" -> 1
  )


  val phi3_phi_in = Map(
    "field0" -> 0,
    "phi13" -> 1
  )


  val phi12_phi_in = Map(
    "phi2" -> 0,
    "sub10" -> 1
  )


  val phi13_phi_in = Map(
    "sub8" -> 0,
    "phi3" -> 1
  )


  //  %b.addr.0 = phi i32 [ %b, %Minim_Loop ], [ %b.addr.1, %if.end ], !UID !13, !ScalaLabel !14
  val phi2_in = Map(
    "field1" -> 0,
    "phi12" -> 0
  )


  //  %a.addr.0 = phi i32 [ %a, %Minim_Loop ], [ %a.addr.1, %if.end ], !UID !15, !ScalaLabel !16
  val phi3_in = Map(
    "field0" -> 0,
    "phi13" -> 0
  )


  //  %cmp = icmp ne i32 %a.addr.0, %b.addr.0, !UID !17, !ScalaLabel !18
  val icmp4_in = Map(
    "phi3" -> 0,
    "phi2" -> 0
  )


  //  br i1 %cmp, label %while.body, label %while.end, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br5_in = Map(
    "icmp4" -> 0
  )


  //  %cmp1 = icmp sgt i32 %a.addr.0, %b.addr.0, !UID !22, !ScalaLabel !23
  val icmp6_in = Map(
    "phi3" -> 1,
    "phi2" -> 1
  )


  //  br i1 %cmp1, label %if.then, label %if.else, !UID !24, !BB_UID !25, !ScalaLabel !26
  val br7_in = Map(
    "icmp6" -> 0
  )


  //  %sub = sub nsw i32 %a.addr.0, %b.addr.0, !UID !27, !ScalaLabel !28
  val sub8_in = Map(
    "phi3" -> 2,
    "phi2" -> 2
  )


  //  %sub2 = sub nsw i32 %b.addr.0, %a.addr.0, !UID !32, !ScalaLabel !33
  val sub10_in = Map(
    "phi2" -> 3,
    "phi3" -> 3
  )


  //  %b.addr.1 = phi i32 [ %b.addr.0, %if.then ], [ %sub2, %if.else ], !UID !37, !ScalaLabel !38
  val phi12_in = Map(
    "phi2" -> 4,
    "sub10" -> 0
  )


  //  %a.addr.1 = phi i32 [ %sub, %if.then ], [ %a.addr.0, %if.else ], !UID !39, !ScalaLabel !40
  val phi13_in = Map(
    "sub8" -> 0,
    "phi3" -> 4
  )


  //  ret void, !UID !51, !BB_UID !52, !ScalaLabel !53
  val ret15_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test04DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class test04DF(implicit p: Parameters) extends test04DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=2,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCall(List(32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_4_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_4_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))

//  val loop_L_4_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0)) //Manual
//  val loop_L_4_liveIN_3 = Module(new LiveInNode(NumOuts = 1, ID = 0)) //Manaul



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_Minim_Loop = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_while_cond = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 4, NumPhi = 2, BID = 2))

  val bb_while_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 4))

  val bb_if_else = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5))

  val bb_if_end = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 2, BID = 6))

  val bb_while_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 7))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %Minim_Loop, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  Minim_Loop:

  //  br label %while.cond, !UID !10, !BB_UID !11, !ScalaLabel !12
  val br1 = Module (new UBranchNode(ID = 1))

  // [BasicBlock]  while.cond:

  //  %b.addr.0 = phi i32 [ %b, %Minim_Loop ], [ %b.addr.1, %if.end ], !UID !13, !ScalaLabel !14
  val phi2 = Module (new PhiNode(NumInputs = 2, NumOuts = 5, ID = 2))


  //  %a.addr.0 = phi i32 [ %a, %Minim_Loop ], [ %a.addr.1, %if.end ], !UID !15, !ScalaLabel !16
  val phi3 = Module (new PhiNode(NumInputs = 2, NumOuts = 5, ID = 3))


  //  %cmp = icmp ne i32 %a.addr.0, %b.addr.0, !UID !17, !ScalaLabel !18
  val icmp4 = Module (new IcmpNode(NumOuts = 1, ID = 4, opCode = "NE")(sign=false))


  //  br i1 %cmp, label %while.body, label %while.end, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br5 = Module (new CBranchNode(ID = 5))

  // [BasicBlock]  while.body:

  //  %cmp1 = icmp sgt i32 %a.addr.0, %b.addr.0, !UID !22, !ScalaLabel !23
  val icmp6 = Module (new IcmpNode(NumOuts = 1, ID = 6, opCode = "UGT")(sign=false))


  //  br i1 %cmp1, label %if.then, label %if.else, !UID !24, !BB_UID !25, !ScalaLabel !26
  val br7 = Module (new CBranchNode(ID = 7))

  // [BasicBlock]  if.then:

  //  %sub = sub nsw i32 %a.addr.0, %b.addr.0, !UID !27, !ScalaLabel !28
  val sub8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "sub")(sign=false))


  //  br label %if.end, !UID !29, !BB_UID !30, !ScalaLabel !31
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  if.else:

  //  %sub2 = sub nsw i32 %b.addr.0, %a.addr.0, !UID !32, !ScalaLabel !33
  val sub10 = Module (new ComputeNode(NumOuts = 1, ID = 10, opCode = "sub")(sign=false))


  //  br label %if.end, !UID !34, !BB_UID !35, !ScalaLabel !36
  val br11 = Module (new UBranchNode(ID = 11))

  // [BasicBlock]  if.end:

  //  %b.addr.1 = phi i32 [ %b.addr.0, %if.then ], [ %sub2, %if.else ], !UID !37, !ScalaLabel !38
  val phi12 = Module (new PhiNode(NumInputs = 2, NumOuts = 1, ID = 12))


  //  %a.addr.1 = phi i32 [ %sub, %if.then ], [ %a.addr.0, %if.else ], !UID !39, !ScalaLabel !40
  val phi13 = Module (new PhiNode(NumInputs = 2, NumOuts = 1, ID = 13))


  //  br label %while.cond, !llvm.loop !41, !UID !48, !BB_UID !49, !ScalaLabel !50
  val br14 = Module (new UBranchNode(ID = 14))

  // [BasicBlock]  while.end:

  //  ret void, !UID !51, !BB_UID !52, !ScalaLabel !53
  val ret15 = Module(new RetNode(retTypes=List(32), ID=15))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test04_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_entry.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br0 to bb_Minim_Loop
  bb_Minim_Loop.io.predicateIn <> br0.io.Out(param.br0_brn_bb("bb_Minim_Loop"))


  //Connecting br1 to bb_while_cond
  bb_while_cond.io.predicateIn(param.bb_while_cond_pred("br1")) <> br1.io.Out(param.br1_brn_bb("bb_while_cond"))


  //Connecting br5 to bb_while_body
  bb_while_body.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_while_body"))


  //Connecting br5 to bb_while_end
  bb_while_end.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_while_end"))


  //Connecting br7 to bb_if_then
  bb_if_then.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_if_then"))


  //Connecting br7 to bb_if_else
  bb_if_else.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_if_else"))


  //Connecting br9 to bb_if_end
  //bb_if_end.io.predicateIn(param.bb_if_end_pred("br9")) <> br9.io.Out(param.br9_brn_bb("bb_if_end"))
  bb_if_end.io.predicateIn(param.bb_if_end_pred("br9")) <> br11.io.Out(param.br11_brn_bb("bb_if_end"))


  //Connecting br11 to bb_if_end
  //bb_if_end.io.predicateIn(param.bb_if_end_pred("br11")) <> br11.io.Out(param.br11_brn_bb("bb_if_end"))
  bb_if_end.io.predicateIn(param.bb_if_end_pred("br11")) <> br9.io.Out(param.br9_brn_bb("bb_if_end"))


  //Connecting br14 to bb_while_cond
  bb_while_cond.io.predicateIn(param.bb_while_cond_pred("br14")) <> br14.io.Out(param.br14_brn_bb("bb_while_cond"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  br1.io.enable <> bb_Minim_Loop.io.Out(param.bb_Minim_Loop_activate("br1"))



  phi2.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("phi2"))

  phi3.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("phi3"))

  icmp4.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("icmp4"))

  br5.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("br5"))



  icmp6.io.enable <> bb_while_body.io.Out(param.bb_while_body_activate("icmp6"))

  br7.io.enable <> bb_while_body.io.Out(param.bb_while_body_activate("br7"))



  sub8.io.enable <> bb_if_then.io.Out(param.bb_if_then_activate("sub8"))

  br9.io.enable <> bb_if_then.io.Out(param.bb_if_then_activate("br9"))



  sub10.io.enable <> bb_if_else.io.Out(param.bb_if_else_activate("sub10"))

  br11.io.enable <> bb_if_else.io.Out(param.bb_if_else_activate("br11"))



  phi12.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("phi12"))

  phi13.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("phi13"))

  br14.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("br14"))



  ret15.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("ret15"))

  loop_L_4_liveIN_0.io.enable <> bb_while_end.io.Out(1)
  loop_L_4_liveIN_1.io.enable <> bb_while_end.io.Out(2)




  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32 %a
  loop_L_4_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32 %b
  loop_L_4_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")


  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  // Wiring Live in to PHI node

  phi2.io.InData(param.phi2_phi_in("field1")) <> loop_L_4_liveIN_1.io.Out(param.phi2_in("field1"))

  phi2.io.InData(param.phi2_phi_in("phi12")) <> phi12.io.Out(param.phi2_in("phi12"))

  // Wiring Live in to PHI node

  phi3.io.InData(param.phi3_phi_in("field0")) <> loop_L_4_liveIN_0.io.Out(param.phi3_in("field0"))

  phi3.io.InData(param.phi3_phi_in("phi13")) <> phi13.io.Out(param.phi3_in("phi13"))

  phi12.io.InData(param.phi12_phi_in("phi2")) <> phi2.io.Out(param.phi12_in("phi2"))

  phi12.io.InData(param.phi12_phi_in("sub10")) <> sub10.io.Out(param.phi12_in("sub10"))

  phi13.io.InData(param.phi13_phi_in("sub8")) <> sub8.io.Out(param.phi13_in("sub8"))

  phi13.io.InData(param.phi13_phi_in("phi3")) <> phi3.io.Out(param.phi13_in("phi3"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi2.io.Mask <> bb_while_cond.io.MaskBB(0)

  phi3.io.Mask <> bb_while_cond.io.MaskBB(1)

  phi12.io.Mask <> bb_if_end.io.MaskBB(0)

  phi13.io.Mask <> bb_if_end.io.MaskBB(1)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp4.io.LeftIO <> phi3.io.Out(param.icmp4_in("phi3"))

  // Wiring instructions
  icmp4.io.RightIO <> phi2.io.Out(param.icmp4_in("phi2"))

  // Wiring Branch instruction
  br5.io.CmpIO <> icmp4.io.Out(param.br5_in("icmp4"))

  // Wiring instructions
  icmp6.io.LeftIO <> phi3.io.Out(param.icmp6_in("phi3"))

  // Wiring instructions
  icmp6.io.RightIO <> phi2.io.Out(param.icmp6_in("phi2"))

  // Wiring Branch instruction
  br7.io.CmpIO <> icmp6.io.Out(param.br7_in("icmp6"))

  // Wiring instructions
  sub8.io.LeftIO <> phi3.io.Out(param.sub8_in("phi3"))

  // Wiring instructions
  sub8.io.RightIO <> phi2.io.Out(param.sub8_in("phi2"))

  // Wiring instructions
  sub10.io.LeftIO <> phi2.io.Out(param.sub10_in("phi2"))

  // Wiring instructions
  sub10.io.RightIO <> phi3.io.Out(param.sub10_in("phi3"))

  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret15.io.In.data("field0").bits.data := 1.U
  
  ret15.io.In.data("field0").valid := true.B
  io.out <> ret15.io.Out


}

import java.io.{File, FileWriter}
object test04Main extends App {
  val dir = new File("RTL/test04") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test04DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


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

object Data_test12_FlowParam {

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_for_cond_pred = Map(
    "br0" -> 0,
    "br16" -> 1
  )


  val bb_for_inc5_pred = Map(
    "br14" -> 0
  )


  val bb_for_body_pred = Map(
    "br4" -> 0
  )


  val bb_for_end7_pred = Map(
    "br4" -> 0
  )


  val bb_for_cond1_pred = Map(
    "br5" -> 0,
    "br13" -> 1
  )


  val bb_for_inc_pred = Map(
    "br11" -> 0
  )


  val bb_for_body3_pred = Map(
    "br9" -> 0
  )


  val bb_for_end_pred = Map(
    "br9" -> 0
  )


  val br0_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val br4_brn_bb = Map(
    "bb_for_body" -> 0,
    "bb_for_end7" -> 1
  )


  val br5_brn_bb = Map(
    "bb_for_cond1" -> 0
  )


  val br9_brn_bb = Map(
    "bb_for_body3" -> 0,
    "bb_for_end" -> 1
  )


  val br11_brn_bb = Map(
    "bb_for_inc" -> 0
  )


  val br13_brn_bb = Map(
    "bb_for_cond1" -> 0
  )


  val br14_brn_bb = Map(
    "bb_for_inc5" -> 0
  )


  val br16_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_for_cond_activate = Map(
    "phi1" -> 0,
    "phi2" -> 1,
    "icmp3" -> 2,
    "br4" -> 3
  )


  val bb_for_body_activate = Map(
    "br5" -> 0
  )


  val bb_for_cond1_activate = Map(
    "phi6" -> 0,
    "phi7" -> 1,
    "icmp8" -> 2,
    "br9" -> 3
  )


  val bb_for_body3_activate = Map(
    "add10" -> 0,
    "br11" -> 1
  )


  val bb_for_inc_activate = Map(
    "add12" -> 0,
    "br13" -> 1
  )


  val bb_for_end_activate = Map(
    "br14" -> 0
  )


  val bb_for_inc5_activate = Map(
    "add15" -> 0,
    "br16" -> 1
  )


  val bb_for_end7_activate = Map(
    "ret17" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add15" -> 1
  )


  val phi2_phi_in = Map(
    "field0" -> 0,
    "phi6" -> 1
  )


  val phi6_phi_in = Map(
    "phi2" -> 0,
    "add10" -> 1
  )


  val phi7_phi_in = Map(
    "const_0" -> 0,
    "add12" -> 1
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc6, %for.inc5 ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add15" -> 0
  )


  //  %foo.0 = phi i32 [ %j, %entry ], [ %foo.1, %for.inc5 ], !UID !12, !ScalaLabel !13
  val phi2_in = Map(
    "field0" -> 0,
    "phi6" -> 0
  )


  //  %cmp = icmp ult i32 %i.0, 5, !UID !14, !ScalaLabel !15
  val icmp3_in = Map(
    "phi1" -> 0
  )


  //  br i1 %cmp, label %for.body, label %for.end7, !UID !16, !BB_UID !17, !ScalaLabel !18
  val br4_in = Map(
    "icmp3" -> 0
  )


  //  %foo.1 = phi i32 [ %foo.0, %for.body ], [ %inc, %for.inc ], !UID !22, !ScalaLabel !23
  val phi6_in = Map(
    "phi2" -> 0,
    "add10" -> 0
  )


  //  %k.0 = phi i32 [ 0, %for.body ], [ %inc4, %for.inc ], !UID !24, !ScalaLabel !25
  val phi7_in = Map(
    "add12" -> 0
  )


  //  %cmp2 = icmp ult i32 %k.0, 5, !UID !26, !ScalaLabel !27
  val icmp8_in = Map(
    "phi7" -> 0
  )


  //  br i1 %cmp2, label %for.body3, label %for.end, !UID !28, !BB_UID !29, !ScalaLabel !30
  val br9_in = Map(
    "icmp8" -> 0
  )


  //  %inc = add i32 %foo.1, 1, !UID !31, !ScalaLabel !32
  val add10_in = Map(
    "phi6" -> 1
  )


  //  %inc4 = add i32 %k.0, 1, !UID !36, !ScalaLabel !37
  val add12_in = Map(
    "phi7" -> 1
  )


  //  %inc6 = add i32 %i.0, 1, !UID !55, !ScalaLabel !56
  val add15_in = Map(
    "phi1" -> 1
  )


  //  ret i32 %foo.0, !UID !63, !BB_UID !64, !ScalaLabel !65
  val ret17_in = Map(
    "phi2" -> 1
  )


}


/* ================================================================== *
 *                   PRINTING PORTS DEFINITION                        *
 * ================================================================== */


abstract class test12DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}


/* ================================================================== *
 *                   PRINTING MODULE DEFINITION                       *
 * ================================================================== */


class test12DF(implicit p: Parameters) extends test12DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


  val StackPointer = Module(new Stack(NumOps = 1))

  val RegisterFile = Module(new TypeStackFile(ID = 0, Size = 32, NReads = 2, NWrites = 2)
  (WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2)))

  val CacheMem = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 2)
  (WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_6_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_5_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))

  val loop_L6_liveOut_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0, Desc = "bb_entry")(p))

  val bb_for_cond = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 2, BID = 1, Desc = "bb_for_cond")(p))

  val bb_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2, Desc = "bb_for_body")(p))

  val bb_for_cond1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 2, BID = 3, Desc = "bb_for_cond1")(p))

  val bb_for_body3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 4, Desc = "bb_for_body3")(p))

  val bb_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5, Desc = "bb_for_inc")(p))

  val bb_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 6, Desc = "bb_for_end")(p))

  val bb_for_inc5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 7, Desc = "bb_for_inc5")(p))

  val bb_for_end7 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 8, Desc = "bb_for_end7")(p))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %for.cond, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module(new UBranchNode(ID = 0, Desc = "br0")(p))


  // [BasicBlock]  for.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc6, %for.inc5 ], !UID !10, !ScalaLabel !11
  val phi1 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1, Desc = "phi1")(p))


  //  %foo.0 = phi i32 [ %j, %entry ], [ %foo.1, %for.inc5 ], !UID !12, !ScalaLabel !13
  val phi2 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 2, Desc = "phi2")(p))


  //  %cmp = icmp ult i32 %i.0, 5, !UID !14, !ScalaLabel !15
  val icmp3 = Module(new IcmpNode(NumOuts = 1, ID = 3, opCode = "ULT", Desc = "icmp3")(sign = false)(p))


  //  br i1 %cmp, label %for.body, label %for.end7, !UID !16, !BB_UID !17, !ScalaLabel !18
  val br4 = Module(new CBranchNode(ID = 4, Desc = "br4")(p))

  val bb_for_cond_expand = Module(new ExpandNode(NumOuts = 2, ID = 0)(new ControlBundle))


  // [BasicBlock]  for.body:

  //  br label %for.cond1, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br5 = Module(new UBranchNode(ID = 5, Desc = "br5")(p))


  // [BasicBlock]  for.cond1:

  //  %foo.1 = phi i32 [ %foo.0, %for.body ], [ %inc, %for.inc ], !UID !22, !ScalaLabel !23
  val phi6 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 6, Desc = "phi6")(p))


  //  %k.0 = phi i32 [ 0, %for.body ], [ %inc4, %for.inc ], !UID !24, !ScalaLabel !25
  val phi7 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 7, Desc = "phi7")(p))


  //  %cmp2 = icmp ult i32 %k.0, 5, !UID !26, !ScalaLabel !27
  val icmp8 = Module(new IcmpNode(NumOuts = 1, ID = 8, opCode = "ULT", Desc = "icmp8")(sign = false)(p))


  //  br i1 %cmp2, label %for.body3, label %for.end, !UID !28, !BB_UID !29, !ScalaLabel !30
  val br9 = Module(new CBranchNode(ID = 9, Desc = "br9")(p))

  val bb_for_cond1_expand = Module(new ExpandNode(NumOuts = 2, ID = 0)(new ControlBundle))


  // [BasicBlock]  for.body3:

  //  %inc = add i32 %foo.1, 1, !UID !31, !ScalaLabel !32
  val add10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "add", Desc = "add10")(sign = false)(p))


  //  br label %for.inc, !UID !33, !BB_UID !34, !ScalaLabel !35
  val br11 = Module(new UBranchNode(ID = 11, Desc = "br11")(p))


  // [BasicBlock]  for.inc:

  //  %inc4 = add i32 %k.0, 1, !UID !36, !ScalaLabel !37
  val add12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "add", Desc = "add12")(sign = false)(p))


  //  br label %for.cond1, !llvm.loop !38, !UID !49, !BB_UID !50, !ScalaLabel !51
  val br13 = Module(new UBranchNode(ID = 13, Desc = "br13", NumOuts = 1)(p))


  // [BasicBlock]  for.end:

  //  br label %for.inc5, !UID !52, !BB_UID !53, !ScalaLabel !54
  val br14 = Module(new UBranchNode(ID = 14, Desc = "br14")(p))


  // [BasicBlock]  for.inc5:

  //  %inc6 = add i32 %i.0, 1, !UID !55, !ScalaLabel !56
  val add15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "add", Desc = "add15")(sign = false)(p))


  //  br label %for.cond, !llvm.loop !57, !UID !60, !BB_UID !61, !ScalaLabel !62
  val br16 = Module(new UBranchNode(ID = 16, Desc = "br16", NumOuts = 1)(p))


  // [BasicBlock]  for.end7:

  //  ret i32 %foo.0, !UID !63, !BB_UID !64, !ScalaLabel !65
  val ret17 = Module(new RetNode(NumPredIn = 1, retTypes = List(32), ID = 17, Desc = "ret17"))


  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test12_FlowParam


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
    * Connecting basic blocks to predicate instructions
    */


  //  bb_entry.io.predicateIn(0) <> InputSplitter.io.Out.enable
  bb_entry.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br0 to bb_for_cond
  bb_for_cond.io.predicateIn(param.bb_for_cond_pred("br0")) <> br0.io.Out(param.br0_brn_bb("bb_for_cond"))


  //Connecting br4 to bb_for_body
  //  bb_for_body.io.predicateIn(param.bb_for_body_pred("br4")) <> br4.io.Out(param.br4_brn_bb("bb_for_body"))
  bb_for_body.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_for_body"))


  //Connecting br4 to bb_for_end7
  //  bb_for_cond_expand.io.InData <> br4.io.Out(param.br4_brn_bb("bb_for_end7"))
  //  bb_for_end7.io.predicateIn(param.bb_for_end7_pred("br4")) <> bb_for_cond_expand.io.Out(0)
  //  bb_for_end7.io.predicateIn <> bb_for_cond_expand.io.Out(0)
  bb_for_end7.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_for_end7"))


  //Connecting br5 to bb_for_cond1
  bb_for_cond1.io.predicateIn(param.bb_for_cond1_pred("br5")) <> br5.io.Out(param.br5_brn_bb("bb_for_cond1"))


  //Connecting br9 to bb_for_body3
  //  bb_for_body3.io.predicateIn(param.bb_for_body3_pred("br9")) <> br9.io.Out(param.br9_brn_bb("bb_for_body3"))
  bb_for_body3.io.predicateIn <> br9.io.Out(param.br9_brn_bb("bb_for_body3"))


  //Connecting br9 to bb_for_end
  //  bb_for_cond1_expand.io.InData <> br9.io.Out(param.br9_brn_bb("bb_for_end"))
  //  bb_for_cond1_expand.io.InData <> br9.io.Out(param.br9_brn_bb("bb_for_end"))
  //  bb_for_end.io.predicateIn(param.bb_for_end_pred("br9")) <> bb_for_cond1_expand.io.Out(0)
  bb_for_end.io.predicateIn <> br9.io.Out(1)


  //Connecting br11 to bb_for_inc
  //  bb_for_inc.io.predicateIn(param.bb_for_inc_pred("br11")) <> br11.io.Out(param.br11_brn_bb("bb_for_inc"))
  bb_for_inc.io.predicateIn <> br11.io.Out(param.br11_brn_bb("bb_for_inc"))


  //Connecting br13 to bb_for_cond1
  bb_for_cond1.io.predicateIn(param.bb_for_cond1_pred("br13")) <> br13.io.Out(param.br13_brn_bb("bb_for_cond1"))


  //Connecting br14 to bb_for_inc5
  //  bb_for_inc5.io.predicateIn(param.bb_for_inc5_pred("br14")) <> br14.io.Out(param.br14_brn_bb("bb_for_inc5"))
  bb_for_inc5.io.predicateIn <> br14.io.Out(param.br14_brn_bb("bb_for_inc5"))


  //Connecting br16 to bb_for_cond
  bb_for_cond.io.predicateIn(param.bb_for_cond_pred("br16")) <> br16.io.Out(param.br16_brn_bb("bb_for_cond"))


  // There is no detach instruction


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */


  loop_L_6_liveIN_0.io.enable <> bb_for_end.io.Out(1)
  loop_L_5_liveIN_0.io.enable <> bb_for_end7.io.Out(1)
  loop_L6_liveOut_0.io.enable <> bb_for_inc5.io.Out(2)

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))


  phi1.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("phi1"))

  phi2.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("phi2"))

  icmp3.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("icmp3"))

  br4.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("br4"))


  br5.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("br5"))


  phi6.io.enable <> bb_for_cond1.io.Out(param.bb_for_cond1_activate("phi6"))

  phi7.io.enable <> bb_for_cond1.io.Out(param.bb_for_cond1_activate("phi7"))

  icmp8.io.enable <> bb_for_cond1.io.Out(param.bb_for_cond1_activate("icmp8"))

  br9.io.enable <> bb_for_cond1.io.Out(param.bb_for_cond1_activate("br9"))


  add10.io.enable <> bb_for_body3.io.Out(param.bb_for_body3_activate("add10"))

  br11.io.enable <> bb_for_body3.io.Out(param.bb_for_body3_activate("br11"))


  add12.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("add12"))

  br13.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("br13"))


  br14.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("br14"))


  add15.io.enable <> bb_for_inc5.io.Out(param.bb_for_inc5_activate("add15"))

  br16.io.enable <> bb_for_inc5.io.Out(param.bb_for_inc5_activate("br16"))


  ret17.io.enable <> bb_for_end7.io.Out(param.bb_for_end7_activate("ret17"))


  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.InData(param.phi1_phi_in("const_0")).bits.data := 0.U
  phi1.io.InData(param.phi1_phi_in("const_0")).bits.predicate := true.B
  phi1.io.InData(param.phi1_phi_in("const_0")).valid := true.B

  phi1.io.InData(param.phi1_phi_in("add15")) <> add15.io.Out(param.phi1_in("add15"))

  // Wiring Live in to PHI node

  phi2.io.InData(param.phi2_phi_in("field0")) <> loop_L_5_liveIN_0.io.Out(param.phi2_in("field0"))

  //  phi2.io.InData(param.phi2_phi_in("phi6")) <> phi6.io.Out(param.phi2_in("phi6"))
  loop_L6_liveOut_0.io.InData <> phi6.io.Out(0)
  phi2.io.InData(param.phi2_phi_in("phi6")) <> loop_L6_liveOut_0.io.Out(0)

  // Wiring Live in to PHI node

  phi6.io.InData(param.phi6_phi_in("phi2")) <> loop_L_6_liveIN_0.io.Out(param.phi6_in("phi2"))

  phi6.io.InData(param.phi6_phi_in("add10")) <> add10.io.Out(param.phi6_in("add10"))

  phi7.io.InData(param.phi7_phi_in("const_0")).bits.data := 0.U
  phi7.io.InData(param.phi7_phi_in("const_0")).bits.predicate := true.B
  phi7.io.InData(param.phi7_phi_in("const_0")).valid := true.B

  phi7.io.InData(param.phi7_phi_in("add12")) <> add12.io.Out(param.phi7_in("add12"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node
  phi1.io.Mask <> bb_for_cond.io.MaskBB(0)

  phi2.io.Mask <> bb_for_cond.io.MaskBB(1)

  phi6.io.Mask <> bb_for_cond1.io.MaskBB(0)

  phi7.io.Mask <> bb_for_cond1.io.MaskBB(1)


  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting instruction to the loop header
  //  %foo.0 = phi i32 [ %j, %entry ], [ %foo.1, %for.inc5 ], !UID !12, !ScalaLabel !13
  loop_L_6_liveIN_0.io.InData <> phi2.io.Out(param.phi6_in("phi2"))

  // Connecting function argument to the loop header
  //i32 %j
  loop_L_5_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")


  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp3.io.LeftIO <> phi1.io.Out(param.icmp3_in("phi1"))

  // Wiring constant
  icmp3.io.RightIO.bits.data := 5.U
  icmp3.io.RightIO.bits.predicate := true.B
  icmp3.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br4.io.CmpIO <> icmp3.io.Out(param.br4_in("icmp3"))

  // Wiring instructions
  icmp8.io.LeftIO <> phi7.io.Out(param.icmp8_in("phi7"))

  // Wiring constant
  icmp8.io.RightIO.bits.data := 5.U
  icmp8.io.RightIO.bits.predicate := true.B
  icmp8.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br9.io.CmpIO <> icmp8.io.Out(param.br9_in("icmp8"))

  // Wiring instructions
  add10.io.LeftIO <> phi6.io.Out(param.add10_in("phi6"))

  // Wiring constant
  add10.io.RightIO.bits.data := 1.U
  add10.io.RightIO.bits.predicate := true.B
  add10.io.RightIO.valid := true.B

  // Wiring instructions
  add12.io.LeftIO <> phi7.io.Out(param.add12_in("phi7"))

  // Wiring constant
  add12.io.RightIO.bits.data := 1.U
  add12.io.RightIO.bits.predicate := true.B
  add12.io.RightIO.valid := true.B

  // Wiring instructions
  add15.io.LeftIO <> phi1.io.Out(param.add15_in("phi1"))

  // Wiring constant
  add15.io.RightIO.bits.data := 1.U
  add15.io.RightIO.bits.predicate := true.B
  add15.io.RightIO.valid := true.B

  // Wiring return instruction
  ret17.io.predicateIn(0).bits.control := true.B
  ret17.io.predicateIn(0).bits.taskID := 0.U
  ret17.io.predicateIn(0).valid := true.B
  ret17.io.In.data("field0") <> phi2.io.Out(param.ret17_in("phi2"))
  io.out <> ret17.io.Out


}

import java.io.{File, FileWriter}

object test12Main extends App {
  val dir = new File("RTL/test12");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test12DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


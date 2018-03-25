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

object Data_cilk_for_test06_detach_FlowParam {

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_pfor_cond2_pred = Map(
    "br0" -> 0,
    "br6" -> 1
  )


  val bb_my_pfor_detach4_pred = Map(
    "br3" -> 0
  )


  val bb_my_pfor_end_pred = Map(
    "br3" -> 0
  )


  val bb_my_pfor_preattach11_pred = Map(
    "br8" -> 0
  )


  val br0_brn_bb = Map(
    "bb_my_pfor_cond2" -> 0
  )


  val br3_brn_bb = Map(
    "bb_my_pfor_detach4" -> 0,
    "bb_my_pfor_end" -> 1
  )


  val br6_brn_bb = Map(
    "bb_my_pfor_cond2" -> 0
  )


  val br8_brn_bb = Map(
    "bb_my_pfor_preattach11" -> 0
  )


  val bb_my_pfor_inc_pred = Map(
    "detach4" -> 0
  )


  val bb_my_offload_pfor_body5_pred = Map(
    "detach4" -> 0
  )


  val detach4_brn_bb = Map(
    "bb_my_offload_pfor_body5" -> 0,
    "bb_my_pfor_inc" -> 1
  )


  val bb_my_pfor_body_activate = Map(
    "br0" -> 0
  )


  val bb_my_pfor_cond2_activate = Map(
    "phi1" -> 0,
    "icmp2" -> 1,
    "br3" -> 2
  )


  val bb_my_pfor_detach4_activate = Map(
    "detach4" -> 0
  )


  val bb_my_pfor_inc_activate = Map(
    "add5" -> 0,
    "br6" -> 1
  )


  val bb_my_pfor_end_activate = Map(
    "sync7" -> 0
  )


  val bb_my_pfor_end_continue_activate = Map(
    "br8" -> 0
  )


  val bb_my_pfor_preattach11_activate = Map(
    "ret9" -> 0
  )


  val bb_my_offload_pfor_body5_activate = Map(
    "call10" -> 0,
    "reattach11" -> 1
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add5" -> 1
  )


  //  %0 = phi i32 [ 0, %my_pfor.body ], [ %2, %my_pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add5" -> 0
  )


  //  %1 = icmp slt i32 %0, 5, !UID !12, !ScalaLabel !13
  val icmp2_in = Map(
    "phi1" -> 0
  )


  //  br i1 %1, label %my_pfor.detach4, label %my_pfor.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  detach label %my_offload.pfor.body5, label %my_pfor.inc, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %2 = add nsw i32 %0, 1, !UID !20, !ScalaLabel !21
  val add5_in = Map(
    "phi1" -> 1
  )


  //  sync label %my_pfor.end.continue, !UID !41, !BB_UID !42, !ScalaLabel !43
  val sync7_in = Map(
    "" -> 2
  )


  //  ret void, !UID !47, !BB_UID !48, !ScalaLabel !49
  val ret9_in = Map(

  )


  //  call void @cilk_for_test06_detach_2([5 x i32]* %a.in, i32 %i.0.in, i32 %0, [5 x i32]* %b.in, [5 x i32]* %c.in), !UID !50, !ScalaLabel !51
  val call10_in = Map(
    "field0" -> 0,
    "field1" -> 0,
    "phi1" -> 2,
    "field2" -> 0,
    "field3" -> 0,
    "" -> 3
  )


  //  reattach label %my_pfor.inc, !UID !52, !BB_UID !53, !ScalaLabel !54
  val reattach11_in = Map(
    "" -> 4
  )


}


/* ================================================================== *
 *                   PRINTING PORTS DEFINITION                        *
 * ================================================================== */


abstract class cilk_for_test06_detachDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val call10_out = Decoupled(new Call(List(32, 32, 32, 32, 32)))
    val call10_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}


/* ================================================================== *
 *                   PRINTING MODULE DEFINITION                       *
 * ================================================================== */


class cilk_for_test06_detachDF(implicit p: Parameters) extends cilk_for_test06_detachDFIO()(p) {


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

  val InputSplitter = Module(new SplitCall(List(32, 32, 32, 32)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_6_liveIN_0 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_1 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_2 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_3 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))


  val loop_L_6_liveIN_TMP_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_TMP_1 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_TMP_2 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_TMP_3 = Module(new LiveOutNode(NumOuts = 1, ID = 0))

  val loop_L_6_live_Control_0 = Module(new LiveOutControlNode(NumOuts = 1, ID = 0))

  val loop_L_6_liveIN_4 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_5 = Module(new LiveOutNode(NumOuts = 1, ID = 0))

  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 0))

  //  val bb_my_pfor_cond2 = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 1))
  val bb_my_pfor_cond2 = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_my_pfor_detach4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_my_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 9, BID = 3))

  val bb_my_pfor_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 4))

  val bb_my_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 5))

  val bb_my_pfor_preattach11 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_my_offload_pfor_body5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 7))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  br label %my_pfor.cond2, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module(new UBranchNode(ID = 0))

  // [BasicBlock]  my_pfor.cond2:

  //  %0 = phi i32 [ 0, %my_pfor.body ], [ %2, %my_pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1 = Module(new PhiNode(NumInputs = 2, NumOuts = 3, ID = 1))


  //  %1 = icmp slt i32 %0, 5, !UID !12, !ScalaLabel !13
  val icmp2 = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign = false))


  //  br i1 %1, label %my_pfor.detach4, label %my_pfor.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module(new CBranchNode(ID = 3))

  // [BasicBlock]  my_pfor.detach4:

  //  detach label %my_offload.pfor.body5, label %my_pfor.inc, !UID !17, !BB_UID !18, !ScalaLabel !19
  //  val detach4 = Module(new Detach(ID = 4))
  val detach4 = Module(new DetachNode(NumOuts = 3, ID = 4))

  // [BasicBlock]  my_pfor.inc:

  //  %2 = add nsw i32 %0, 1, !UID !20, !ScalaLabel !21
  val add5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false))


  //  br label %my_pfor.cond2, !llvm.loop !22, !UID !38, !BB_UID !39, !ScalaLabel !40
  val br6 = Module(new UBranchNode(ID = 6))

  // [BasicBlock]  my_pfor.end:

  //  sync label %my_pfor.end.continue, !UID !41, !BB_UID !42, !ScalaLabel !43
  //  val sync7 = Module(new Sync(ID = 7, NumOuts = 1, NumInc = 1, NumDec = 1))
  val sync7 = Module(new SyncNode(ID = 7, NumOuts = 1))

  // [BasicBlock]  my_pfor.end.continue:

  //  br label %my_pfor.preattach11, !UID !44, !BB_UID !45, !ScalaLabel !46
  val br8 = Module(new UBranchNode(ID = 8))

  // [BasicBlock]  my_pfor.preattach11:

  //  ret void, !UID !47, !BB_UID !48, !ScalaLabel !49
  val ret9 = Module(new RetNode(NumPredIn = 1, retTypes = List(32), ID = 9))

  // [BasicBlock]  my_offload.pfor.body5:

  //  call void @cilk_for_test06_detach_2([5 x i32]* %a.in, i32 %i.0.in, i32 %0, [5 x i32]* %b.in, [5 x i32]* %c.in), !UID !50, !ScalaLabel !51
  val call10 = Module(new CallNode(ID = 10, argTypes = List(32, 32, 32, 32, 32), retTypes = List(32)))


  //  reattach label %my_pfor.inc, !UID !52, !BB_UID !53, !ScalaLabel !54
  val reattach11 = Module(new ReattachNode(NumPredIn = 1, ID = 11))


  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test06_detach_FlowParam


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
    * Connecting basic blocks to predicate instructions
    */


  bb_my_pfor_body.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br0 to bb_my_pfor_cond2
  //  bb_my_pfor_cond2.io.predicateIn(param.bb_my_pfor_cond2_pred("br0")) <> br0.io.Out(param.br0_brn_bb("bb_my_pfor_cond2"))
  bb_my_pfor_cond2.io.activate <> br0.io.Out(param.br0_brn_bb("bb_my_pfor_cond2"))


  //Connecting br3 to bb_my_pfor_detach4
  bb_my_pfor_detach4.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_detach4"))


  //Connecting br3 to bb_my_pfor_end
  bb_my_pfor_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_end"))


  //Connecting br6 to bb_my_pfor_cond2
  bb_my_pfor_cond2.io.loopBack <> br6.io.Out(param.br6_brn_bb("bb_my_pfor_cond2"))

  //  bb_my_pfor_cond2.io.endLoop <> bb_my_pfor_end.io.Out(5)


  //Connecting br8 to bb_my_pfor_preattach
  bb_my_pfor_preattach11.io.predicateIn <> br8.io.Out(param.br8_brn_bb("bb_my_pfor_preattach11"))
  // Manually re-wired.  Want re-attach controlled by sub-block returns only (otherwise it stalls loop)
  //bb_my_pfor_preattach11.io.predicateIn.valid := true.B
  //bb_my_pfor_preattach11.io.predicateIn.bits.control := true.B
  //br8.io.Out(0).ready := true.B


  //Connecting detach4 to bb_my_offload_pfor_body5
  bb_my_offload_pfor_body5.io.predicateIn <> detach4.io.Out(0)


  //Connecting detach4 to bb_my_pfor_inc
  bb_my_pfor_inc.io.predicateIn <> detach4.io.Out(1)

  bb_my_pfor_end_continue.io.predicateIn <> sync7.io.Out(0) // added manually


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("br0"))


  phi1.io.enable <> bb_my_pfor_cond2.io.Out(param.bb_my_pfor_cond2_activate("phi1"))

  icmp2.io.enable <> bb_my_pfor_cond2.io.Out(param.bb_my_pfor_cond2_activate("icmp2"))

  br3.io.enable <> bb_my_pfor_cond2.io.Out(param.bb_my_pfor_cond2_activate("br3"))


  detach4.io.enable <> bb_my_pfor_detach4.io.Out(param.bb_my_pfor_detach4_activate("detach4"))


  add5.io.enable <> bb_my_pfor_inc.io.Out(param.bb_my_pfor_inc_activate("add5"))

  br6.io.enable <> bb_my_pfor_inc.io.Out(param.bb_my_pfor_inc_activate("br6"))

  //  bb_my_offload_pfor_body5.io.Out(0).ready := true.B // Manually added


  //  sync7.io.enable <> bb_my_pfor_end.io.Out(param.bb_my_pfor_end_activate("sync7"))


  loop_L_6_liveIN_0.io.enable <> bb_my_pfor_body.io.Out(1)
  loop_L_6_liveIN_1.io.enable <> bb_my_pfor_body.io.Out(2)
  loop_L_6_liveIN_2.io.enable <> bb_my_pfor_body.io.Out(3)
  loop_L_6_liveIN_3.io.enable <> bb_my_pfor_body.io.Out(4)

  loop_L_6_liveIN_0.io.Invalid <> bb_my_pfor_end.io.Out(0)
  loop_L_6_liveIN_1.io.Invalid <> bb_my_pfor_end.io.Out(1)
  loop_L_6_liveIN_2.io.Invalid <> bb_my_pfor_end.io.Out(2)
  loop_L_6_liveIN_3.io.Invalid <> bb_my_pfor_end.io.Out(3)


  loop_L_6_liveIN_4.io.enable <> bb_my_pfor_inc.io.Out(2)
  loop_L_6_liveIN_5.io.enable <> bb_my_pfor_inc.io.Out(3)

  loop_L_6_liveIN_TMP_0.io.enable <> bb_my_pfor_inc.io.Out(4)
  loop_L_6_liveIN_TMP_1.io.enable <> bb_my_pfor_inc.io.Out(5)
  loop_L_6_liveIN_TMP_2.io.enable <> bb_my_pfor_inc.io.Out(6)
  loop_L_6_liveIN_TMP_3.io.enable <> bb_my_pfor_inc.io.Out(7)
  loop_L_6_live_Control_0.io.enable <> bb_my_pfor_inc.io.Out(8)


  br8.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("br8"))


  ret9.io.enable <> bb_my_pfor_preattach11.io.Out(param.bb_my_pfor_preattach11_activate("ret9"))


  loop_L_6_live_Control_0.io.InData <> bb_my_offload_pfor_body5.io.Out(0)
  call10.io.In.enable <> loop_L_6_live_Control_0.io.Out(0)
  //  call10.io.In.enable <> bb_my_offload_pfor_body5.io.Out(0)

  reattach11.io.enable <> bb_my_offload_pfor_body5.io.Out(1)
  //  reattach11.io.enable.enq(ControlBundle.active()) // always enabled
  //  reattach11.io.enable <> bb_my_offload_pfor_body5.io.Out(1) // always enabled
  //  bb_my_offload_pfor_body5.io.Out(param.bb_my_offload_pfor_body5_activate("reattach11")).ready := true.B


  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //[5 x i32]* %a.in
  loop_L_6_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32 %i.0.in
  loop_L_6_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //[5 x i32]* %b.in
  loop_L_6_liveIN_2.io.InData <> InputSplitter.io.Out.data("field2")

  // Connecting function argument to the loop header
  //[5 x i32]* %c.in
  loop_L_6_liveIN_3.io.InData <> InputSplitter.io.Out.data("field3")


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

  phi1.io.InData(param.phi1_phi_in("add5")) <> add5.io.Out(param.phi1_in("add5"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_my_pfor_cond2.io.MaskBB(0)


  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp2.io.LeftIO <> phi1.io.Out(0)

  // Wiring constant
  icmp2.io.RightIO.bits.data := 5.U
  icmp2.io.RightIO.bits.predicate := true.B
  icmp2.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring instructions
  //  add5.io.LeftIO <> phi1.io.Out(param.add5_in("phi1"))
  loop_L_6_liveIN_4.io.InData <> phi1.io.Out(1)
  add5.io.LeftIO <> loop_L_6_liveIN_4.io.Out(0)

  // Wiring constant
  add5.io.RightIO.bits.data := 1.U
  add5.io.RightIO.bits.predicate := true.B
  add5.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  ret9.io.predicateIn(0).bits.control := true.B
  ret9.io.predicateIn(0).bits.taskID := 0.U
  ret9.io.predicateIn(0).valid := true.B
  ret9.io.In.data("field0").bits.data := 1.U
  ret9.io.In.data("field0").bits.predicate := true.B
  ret9.io.In.data("field0").valid := true.B
  io.out <> ret9.io.Out


  // Wiring Call to I/O
  io.call10_out <> call10.io.callOut
  call10.io.retIn <> io.call10_in
  call10.io.Out.enable.ready := true.B // Manual fix

  //  call10.io.Out.enable <> bb_my_offload_pfor_body5.io.Out(0)
  //  reattach11.io.enable <> call10.io.Out.enable

  // Wiring Call instruction to the loop header
  loop_L_6_liveIN_TMP_0.io.InData <> loop_L_6_liveIN_0.io.Out(0)
  call10.io.In.data("field0") <> loop_L_6_liveIN_TMP_0.io.Out(0)

  // Wiring Call instruction to the loop header
  loop_L_6_liveIN_TMP_1.io.InData <> loop_L_6_liveIN_1.io.Out(0)
  call10.io.In.data("field1") <> loop_L_6_liveIN_TMP_1.io.Out(0)

  // Wiring Call instruction to the loop header
  //  call10.io.In.data("field2") <> phi1.io.Out(param.call10_in("phi1")) // Manually added
  loop_L_6_liveIN_5.io.InData <> phi1.io.Out(2)
  call10.io.In.data("field2") <> loop_L_6_liveIN_5.io.Out(0)

  // Wiring Call instruction to the loop header
  loop_L_6_liveIN_TMP_2.io.InData <> loop_L_6_liveIN_2.io.Out(0)
  call10.io.In.data("field3") <> loop_L_6_liveIN_TMP_2.io.Out(0)

  // Wiring Call instruction to the loop header
  loop_L_6_liveIN_TMP_3.io.InData <> loop_L_6_liveIN_3.io.Out(0)
  call10.io.In.data("field4") <> loop_L_6_liveIN_TMP_3.io.Out(0)

  //  call10.io.Out.enable.ready := true.B // Manual fix

  // Reattach (Manual add)
  reattach11.io.dataIn <> call10.io.Out.data("field0")

  // Sync (Manual add)
  sync7.io.incIn <> detach4.io.Out(2)
  sync7.io.decIn <> reattach11.io.Out(0)


}

import java.io.{File, FileWriter}

object cilk_for_test06_detachMain extends App {
  val dir = new File("RTL/cilk_for_test06_detach");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test06_detachDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


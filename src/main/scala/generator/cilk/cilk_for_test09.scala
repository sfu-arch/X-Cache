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

object Data_cilk_for_test09_FlowParam {

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_pfor_preattach_pred = Map(
    "br11" -> 0
  )


  val bb_pfor_cond_pred = Map(
    "br2" -> 0,
    "br14" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br5" -> 0
  )


  val bb_pfor_end_pred = Map(
    "br5" -> 0
  )


  val br2_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val br5_brn_bb = Map(
    "bb_pfor_detach" -> 0,
    "bb_pfor_end" -> 1
  )


  val br11_brn_bb = Map(
    "bb_pfor_preattach" -> 0
  )


  val br14_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val bb_pfor_inc_pred = Map(
    "detach6" -> 0
  )


  val bb_pfor_body_pred = Map(
    "detach6" -> 0
  )


  val detach6_brn_bb = Map(
    "bb_pfor_body" -> 0,
    "bb_pfor_inc" -> 1
  )


  val bb_entry_activate = Map(
    "alloca0" -> 0,
    "store1" -> 1,
    "br2" -> 2
  )


  val bb_pfor_cond_activate = Map(
    "phi3" -> 0,
    "icmp4" -> 1,
    "br5" -> 2
  )


  val bb_pfor_detach_activate = Map(
    "detach6" -> 0
  )


  val bb_pfor_body_activate = Map(
    "call7" -> 0,
    "load8" -> 1,
    "add9" -> 2,
    "store10" -> 3,
    "br11" -> 4
  )


  val bb_pfor_preattach_activate = Map(
    "reattach12" -> 0
  )


  val bb_pfor_inc_activate = Map(
    "add13" -> 0,
    "br14" -> 1
  )


  val bb_pfor_end_activate = Map(
    "sync15" -> 0
  )


  val bb_pfor_end_continue_activate = Map(
    "load16" -> 0,
    "ret17" -> 1
  )


  val phi3_phi_in = Map(
    "const_0" -> 0,
    "add13" -> 1
  )


  //  %a = alloca i32, align 4, !UID !7, !ScalaLabel !8
  val alloca0_in = Map(

  )


  //  store i32 0, i32* %a, align 4, !UID !9, !ScalaLabel !10
  val store1_in = Map(
    "alloca0" -> 0
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !14, !ScalaLabel !15
  val phi3_in = Map(
    "add13" -> 0
  )


  //  %cmp = icmp slt i32 %i.0, %m, !UID !16, !ScalaLabel !17
  val icmp4_in = Map(
    "phi3" -> 0,
    "field0" -> 0
  )


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5_in = Map(
    "icmp4" -> 0
  )


  //  detach label %pfor.body, label %pfor.inc, !UID !21, !BB_UID !22, !ScalaLabel !23
  val detach6_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %call = call i32 @cilk_for_test09_inner(i32 %n), !UID !24, !ScalaLabel !25
  val call7_in = Map(
    "field1" -> 0,
    "" -> 2
  )


  //  %0 = load i32, i32* %a, align 4, !UID !26, !ScalaLabel !27
  val load8_in = Map(
    "alloca0" -> 1
  )


  //  %add = add nsw i32 %0, %call, !UID !28, !ScalaLabel !29
  val add9_in = Map(
    "load8" -> 0,
    "call7" -> 0
  )


  //  store i32 %add, i32* %a, align 4, !UID !30, !ScalaLabel !31
  val store10_in = Map(
    "add9" -> 0,
    "alloca0" -> 2
  )


  //  reattach label %pfor.inc, !UID !35, !BB_UID !36, !ScalaLabel !37
  val reattach12_in = Map(
    "" -> 3
  )


  //  %inc = add nsw i32 %i.0, 1, !UID !38, !ScalaLabel !39
  val add13_in = Map(
    "phi3" -> 1
  )


  //  sync label %pfor.end.continue, !UID !52, !BB_UID !53, !ScalaLabel !54
  val sync15_in = Map(
    "" -> 4
  )


  //  %1 = load i32, i32* %a, align 4, !UID !55, !ScalaLabel !56
  val load16_in = Map(
    "alloca0" -> 3
  )


  //  ret i32 %1, !UID !57, !BB_UID !58, !ScalaLabel !59
  val ret17_in = Map(
    "load16" -> 0
  )


}


/* ================================================================== *
 *                   PRINTING PORTS DEFINITION                        *
 * ================================================================== */


abstract class cilk_for_test09DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val call7_out = Decoupled(new Call(List(32)))
    val call7_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}


/* ================================================================== *
 *                   PRINTING MODULE DEFINITION                       *
 * ================================================================== */


class cilk_for_test09DF(implicit p: Parameters) extends cilk_for_test09DFIO()(p) {


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

  val InputSplitter = Module(new SplitCall(List(32, 32)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 2, ID = 0))


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_pfor_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_pfor_detach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 3))

  val bb_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 5))

  val bb_pfor_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 6))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 7))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %a = alloca i32, align 4, !UID !7, !ScalaLabel !8
  val alloca0 = Module(new AllocaNode(NumOuts = 4, RouteID = 0, ID = 0))


  //  store i32 0, i32* %a, align 4, !UID !9, !ScalaLabel !10
  val store1 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 1, RouteID = 0))


  //  br label %pfor.cond, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br2 = Module(new UBranchNode(ID = 2))

  // [BasicBlock]  pfor.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !14, !ScalaLabel !15
  val phi3 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 3))


  //  %cmp = icmp slt i32 %i.0, %m, !UID !16, !ScalaLabel !17
  val icmp4 = Module(new IcmpNode(NumOuts = 1, ID = 4, opCode = "ULT")(sign = false))


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5 = Module(new CBranchNode(ID = 5))

  // [BasicBlock]  pfor.detach:

  //  detach label %pfor.body, label %pfor.inc, !UID !21, !BB_UID !22, !ScalaLabel !23
  val detach6 = Module(new Detach(ID = 6))

  // [BasicBlock]  pfor.body:

  //  %call = call i32 @cilk_for_test09_inner(i32 %n), !UID !24, !ScalaLabel !25
  val call7 = Module(new CallNode(ID = 7, argTypes = List(32), retTypes = List(32)))


  //  %0 = load i32, i32* %a, align 4, !UID !26, !ScalaLabel !27
  val load8 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 8, RouteID = 0))


  //  %add = add nsw i32 %0, %call, !UID !28, !ScalaLabel !29
  val add9 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign = false))


  //  store i32 %add, i32* %a, align 4, !UID !30, !ScalaLabel !31
  val store10 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 10, RouteID = 1))


  //  br label %pfor.preattach, !UID !32, !BB_UID !33, !ScalaLabel !34
  val br11 = Module(new UBranchNode(ID = 11))

  // [BasicBlock]  pfor.preattach:

  //  reattach label %pfor.inc, !UID !35, !BB_UID !36, !ScalaLabel !37
  val reattach12 = Module(new Reattach(NumPredOps = 1, ID = 12))

  // [BasicBlock]  pfor.inc:

  //  %inc = add nsw i32 %i.0, 1, !UID !38, !ScalaLabel !39
  val add13 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign = false))


  //  br label %pfor.cond, !llvm.loop !40, !UID !49, !BB_UID !50, !ScalaLabel !51
  val br14 = Module(new UBranchNode(ID = 14))

  // [BasicBlock]  pfor.end:

  //  sync label %pfor.end.continue, !UID !52, !BB_UID !53, !ScalaLabel !54
  val sync15 = Module(new Sync(ID = 15, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  pfor.end.continue:

  //  %1 = load i32, i32* %a, align 4, !UID !55, !ScalaLabel !56
  val load16 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 16, RouteID = 1))


  //  ret i32 %1, !UID !57, !BB_UID !58, !ScalaLabel !59
  val ret17 = Module(new RetNode(retTypes = List(32), ID = 17))


  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test09_FlowParam


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

  //Connecting br2 to bb_pfor_cond
  bb_pfor_cond.io.activate <> br2.io.Out(param.br2_brn_bb("bb_pfor_cond"))


  //Connecting br5 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_pfor_detach"))


  //Connecting br5 to bb_pfor_end
  bb_pfor_end.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_pfor_end"))


  //Connecting br11 to bb_pfor_preattach
  bb_pfor_preattach.io.predicateIn <> br11.io.Out(param.br11_brn_bb("bb_pfor_preattach"))


  //Connecting br14 to bb_pfor_cond
  bb_pfor_cond.io.loopBack <> br14.io.Out(param.br14_brn_bb("bb_pfor_cond"))


  //Connecting detach6 to bb_pfor_body
  bb_pfor_body.io.predicateIn <> detach6.io.Out(param.detach6_brn_bb("bb_pfor_body"))


  //Connecting detach6 to bb_pfor_inc
  bb_pfor_inc.io.predicateIn <> detach6.io.Out(param.detach6_brn_bb("bb_pfor_inc"))


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  alloca0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("alloca0"))

  store1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store1"))

  br2.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br2"))


  phi3.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("phi3"))

  icmp4.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("icmp4"))

  br5.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("br5"))


  detach6.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach6"))


  call7.io.In.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("call7"))

  load8.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("load8"))

  add9.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("add9"))

  store10.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("store10"))

  br11.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("br11"))


  //  reattach12.io.enable <> bb_pfor_preattach.io.Out(param.bb_pfor_preattach_activate("reattach12"))
  reattach12.io.enable.enq(ControlBundle.active()) // always enabled
  bb_pfor_preattach.io.Out(param.bb_pfor_preattach_activate("reattach12")).ready := true.B


  add13.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("add13"))

  br14.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("br14"))


  sync15.io.enable <> bb_pfor_end.io.Out(param.bb_pfor_end_activate("sync15"))

  loop_L_0_liveIN_0.io.enable <> bb_pfor_end.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_pfor_end.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_pfor_end.io.Out(3)


  bb_pfor_end_continue.io.predicateIn <> sync15.io.Out(0) // added manually


  load16.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("load16"))

  ret17.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("ret17"))


  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32 %m
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32 %n
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting instruction to the loop header
  //  %a = alloca i32, align 4, !UID !7, !ScalaLabel !8
  loop_L_0_liveIN_2.io.InData <> alloca0.io.Out(param.store10_in("alloca0"))


  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi3.io.InData(param.phi3_phi_in("const_0")).bits.data := 0.U
  phi3.io.InData(param.phi3_phi_in("const_0")).bits.predicate := true.B
  phi3.io.InData(param.phi3_phi_in("const_0")).valid := true.B

  phi3.io.InData(param.phi3_phi_in("add13")) <> add13.io.Out(param.phi3_in("add13"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi3.io.Mask <> bb_pfor_cond.io.MaskBB(0)


  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Alloca instructions with Static inputs
  alloca0.io.allocaInputIO.bits.size := 1.U
  alloca0.io.allocaInputIO.bits.numByte := 4.U
  alloca0.io.allocaInputIO.bits.predicate := true.B
  alloca0.io.allocaInputIO.bits.valid := true.B
  alloca0.io.allocaInputIO.valid := true.B

  // Connecting Alloca to Stack
  StackPointer.io.InData(0) <> alloca0.io.allocaReqIO
  alloca0.io.allocaRespIO <> StackPointer.io.OutData(0)


  // Wiring constant instructions to store
  store1.io.inData.bits.data := 0.U
  store1.io.inData.bits.predicate := true.B
  store1.io.inData.valid := true.B


  // Wiring Store instruction to the parent instruction
  store1.io.GepAddr <> alloca0.io.Out(param.store1_in("alloca0"))
  store1.io.memResp <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store1.io.memReq
  store1.io.Out(0).ready := true.B


  // Wiring instructions
  icmp4.io.LeftIO <> phi3.io.Out(param.icmp4_in("phi3"))

  // Wiring Binary instruction to the loop header
  icmp4.io.RightIO <> loop_L_0_liveIN_0.io.Out(param.icmp4_in("field0"))

  // Wiring Branch instruction
  br5.io.CmpIO <> icmp4.io.Out(param.br5_in("icmp4"))

  // Wiring Call to I/O
  io.call7_out <> call7.io.callOut
  call7.io.retIn <> io.call7_in
  call7.io.Out.enable.ready := true.B
  // Wiring Call instruction to the loop header
  call7.io.In.data("field0") <> loop_L_0_liveIN_1.io.Out(param.call7_in("field1")) // Manually fixed


  // Wiring Load instruction to the loop latch
  load8.io.GepAddr <> loop_L_0_liveIN_2.io.Out(param.load8_in("alloca0")) // Manually fixed
  load8.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load8.io.memReq


  // Wiring instructions
  add9.io.LeftIO <> load8.io.Out(param.add9_in("load8"))

  // Wiring instructions
  add9.io.RightIO <> call7.io.Out.data("field0") // Manually fixed

  store10.io.inData <> add9.io.Out(param.store10_in("add9"))


  // Wiring Store instruction to the parent instruction
  store10.io.GepAddr <> alloca0.io.Out(param.store10_in("alloca0"))
  store10.io.memResp <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store10.io.memReq
  store10.io.Out(0).ready := true.B


  // Wiring instructions
  add13.io.LeftIO <> phi3.io.Out(param.add13_in("phi3"))

  // Wiring constant
  add13.io.RightIO.bits.data := 1.U
  add13.io.RightIO.bits.predicate := true.B
  add13.io.RightIO.valid := true.B

  // Wiring Load instruction to another instruction
  load16.io.GepAddr <> alloca0.io.Out(param.load16_in("alloca0"))


  // Reattach (Manual add)
  reattach12.io.predicateIn(0) <> call7.io.Out.data("field0")

  // Sync (Manual add)
  sync15.io.incIn(0) <> detach6.io.Out(2)
  sync15.io.decIn(0) <> reattach12.io.Out(0)


  // Wiring return instruction
  
  
  
  ret17.io.In.data("field0") <> load16.io.Out(param.ret17_in("load16"))
  io.out <> ret17.io.Out


}

import java.io.{File, FileWriter}

object cilk_for_test09Main extends App {
  val dir = new File("RTL/cilk_for_test09");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test09DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


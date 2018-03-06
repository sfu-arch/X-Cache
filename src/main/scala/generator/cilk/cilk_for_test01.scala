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

object Data_cilk_for_test01_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_pfor_cond_pred = Map(
    "br0" -> 0,
    "br14" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br3" -> 0
  )


  val bb_pfor_end_pred = Map(
    "br3" -> 0
  )


  val bb_pfor_preattach_pred = Map(
    "br11" -> 0
  )


  val br0_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val br3_brn_bb = Map(
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
    "detach4" -> 0
  )


  val bb_pfor_body_pred = Map(
    "detach4" -> 0
  )


  val detach4_brn_bb = Map(
    "bb_pfor_body" -> 0,
    "bb_pfor_inc" -> 1
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_pfor_cond_activate = Map(
    "phi1" -> 0,
    "icmp2" -> 1,
    "br3" -> 2
  )


  val bb_pfor_detach_activate = Map(
    "detach4" -> 0
  )


  val bb_pfor_body_activate = Map(
    "bitcast5" -> 0,
    "getelementptr6" -> 1,
    "load7" -> 2,
    "mul8" -> 3,
    "getelementptr9" -> 4,
    "store10" -> 5,
    "br11" -> 6
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
    "ret16" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add13" -> 1
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add13" -> 0
  )


  //  %cmp = icmp slt i32 %i.0, 5, !UID !12, !ScalaLabel !13
  val icmp2_in = Map(
    "phi1" -> 0
  )


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  detach label %pfor.body, label %pfor.inc, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %0 = bitcast i32 undef to i32, !UID !20, !ScalaLabel !21
  val bitcast5_in = Map(
    "" -> 2
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !22, !ScalaLabel !23
  val getelementptr6_in = Map(
    "field0" -> 0,
    "phi1" -> 1
  )


  //  %1 = load i32, i32* %arrayidx, align 4, !UID !24, !ScalaLabel !25
  val load7_in = Map(
    "getelementptr6" -> 0
  )


  //  %mul = mul i32 %1, 2, !UID !26, !ScalaLabel !27
  val mul8_in = Map(
    "load7" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i.0, !UID !28, !ScalaLabel !29
  val getelementptr9_in = Map(
    "field1" -> 0,
    "phi1" -> 2
  )


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !30, !ScalaLabel !31
  val store10_in = Map(
    "mul8" -> 0,
    "getelementptr9" -> 0
  )


  //  reattach label %pfor.inc, !UID !35, !BB_UID !36, !ScalaLabel !37
  val reattach12_in = Map(
    "" -> 3
  )


  //  %inc = add nsw i32 %i.0, 1, !UID !38, !ScalaLabel !39
  val add13_in = Map(
    "phi1" -> 3
  )


  //  sync label %pfor.end.continue, !UID !53, !BB_UID !54, !ScalaLabel !55
  val sync15_in = Map(
    "" -> 4
  )


  //  ret i32 1, !UID !56, !BB_UID !57, !ScalaLabel !58
  val ret16_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test01DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test01DF(implicit p: Parameters) extends cilk_for_test01DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=1,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=1,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=1,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_5_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_5_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0, Desc = "bb_entry")(p))

  val bb_pfor_cond = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 1, Desc = "bb_pfor_cond")(p))

  val bb_pfor_detach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2, Desc = "bb_pfor_detach")(p))

  val bb_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 3, Desc = "bb_pfor_body")(p))

  val bb_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4, Desc = "bb_pfor_preattach")(p))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5, Desc = "bb_pfor_inc")(p))

  val bb_pfor_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6, Desc = "bb_pfor_end")(p))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7, Desc = "bb_pfor_end_continue")(p))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %pfor.cond, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0, Desc = "br0")(p))



  // [BasicBlock]  pfor.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 4, ID = 1, Desc = "phi1")(p))


  //  %cmp = icmp slt i32 %i.0, 5, !UID !12, !ScalaLabel !13
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT", Desc = "icmp2")(sign=false)(p))


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new CBranchNode(ID = 3, Desc = "br3")(p))

  val bb_pfor_cond_expand = Module(new ExpandNode(NumOuts=3, ID=0)(new ControlBundle))



  // [BasicBlock]  pfor.detach:

  //  detach label %pfor.body, label %pfor.inc, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4 = Module(new Detach(ID = 4, Desc = "detach4")(p))



  // [BasicBlock]  pfor.body:

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !22, !ScalaLabel !23
  val getelementptr6 = Module (new GepOneNode(NumOuts = 1, ID = 6, Desc = "getelementptr6")(numByte1 = 1)(p))


  //  %1 = load i32, i32* %arrayidx, align 4, !UID !24, !ScalaLabel !25
  val load7 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=0,Desc="load7"))


  //  %mul = mul i32 %1, 2, !UID !26, !ScalaLabel !27
  val mul8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "mul", Desc = "mul8")(sign=false)(p))


  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i.0, !UID !28, !ScalaLabel !29
  val getelementptr9 = Module (new GepOneNode(NumOuts = 1, ID = 9, Desc = "getelementptr9")(numByte1 = 1)(p))


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !30, !ScalaLabel !31
  val store10 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=10,RouteID=0,Desc="store10"))


  //  br label %pfor.preattach, !UID !32, !BB_UID !33, !ScalaLabel !34
  val br11 = Module (new UBranchNode(ID = 11, Desc = "br11")(p))



  // [BasicBlock]  pfor.preattach:

  //  reattach label %pfor.inc, !UID !35, !BB_UID !36, !ScalaLabel !37
  val reattach12 = Module(new Reattach(NumPredIn=1, ID=12, Desc = "reattach12")(p))



  // [BasicBlock]  pfor.inc:

  //  %inc = add nsw i32 %i.0, 1, !UID !38, !ScalaLabel !39
  val add13 = Module (new ComputeNode(NumOuts = 1, ID = 13, opCode = "add", Desc = "add13")(sign=false)(p))


  //  br label %pfor.cond, !llvm.loop !40, !UID !50, !BB_UID !51, !ScalaLabel !52
  val br14 = Module (new UBranchNode(ID = 14, Desc = "br14")(p))



  // [BasicBlock]  pfor.end:

  //  sync label %pfor.end.continue, !UID !53, !BB_UID !54, !ScalaLabel !55
  val sync15 = Module(new Sync(ID = 15, NumOuts = 1, NumInc = 1, NumDec = 1, Desc = "sync15")(p)) // Manually changed to sync



  // [BasicBlock]  pfor.end.continue:

  //  ret i32 1, !UID !56, !BB_UID !57, !ScalaLabel !58
  val ret16 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=16, Desc="ret16"))







  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test01_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_entry.io.predicateIn(0) <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br0 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br0")) <> br0.io.Out(param.br0_brn_bb("bb_pfor_cond"))


  //Connecting br3 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn(param.bb_pfor_detach_pred("br3")) <> br3.io.Out(param.br3_brn_bb("bb_pfor_detach"))


  //Connecting br3 to bb_pfor_end
  bb_pfor_cond_expand.io.InData <> br3.io.Out(param.br3_brn_bb("bb_pfor_end"))
  bb_pfor_end.io.predicateIn(param.bb_pfor_end_pred("br3")) <> bb_pfor_cond_expand.io.Out(0)


  //Connecting br11 to bb_pfor_preattach
  bb_pfor_preattach.io.predicateIn(param.bb_pfor_preattach_pred("br11")) <> br11.io.Out(param.br11_brn_bb("bb_pfor_preattach"))


  //Connecting br14 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br14")) <> br14.io.Out(param.br14_brn_bb("bb_pfor_cond"))


  //Connecting detach4 to bb_pfor_body
  bb_pfor_body.io.predicateIn(param.bb_pfor_body_pred("detach4")) <> detach4.io.Out(param.detach4_brn_bb("bb_pfor_body"))


  //Connecting detach4 to bb_pfor_inc
  bb_pfor_inc.io.predicateIn(param.bb_pfor_inc_pred("detach4")) <> detach4.io.Out(param.detach4_brn_bb("bb_pfor_inc"))

  bb_pfor_end_continue.io.predicateIn(0) <> sync15.io.Out(0) // Manually added



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  phi1.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("phi1"))

  icmp2.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("icmp2"))

  br3.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("br3"))

  bb_pfor_cond_expand.io.enable <> bb_pfor_cond.io.Out(5)

  loop_L_5_liveIN_0.io.enable <> bb_pfor_cond.io.Out(3)
  loop_L_5_liveIN_1.io.enable <> bb_pfor_cond.io.Out(4)

  loop_L_5_liveIN_0.io.Finish <> bb_pfor_cond_expand.io.Out(1)
  loop_L_5_liveIN_1.io.Finish <> bb_pfor_cond_expand.io.Out(2)



  detach4.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach4"))


  bb_pfor_body.io.Out(param.bb_pfor_body_activate("bitcast5")).ready := true.B

  getelementptr6.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("getelementptr6"))

  load7.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("load7"))

  mul8.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("mul8"))

  getelementptr9.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("getelementptr9"))

  store10.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("store10"))

  br11.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("br11"))



  reattach12.io.enable <> bb_pfor_preattach.io.Out(param.bb_pfor_preattach_activate("reattach12"))



  add13.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("add13"))

  br14.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("br14"))



  sync15.io.enable <> bb_pfor_end.io.Out(param.bb_pfor_end_activate("sync15"))



  ret16.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("ret16"))





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

  phi1.io.InData(param.phi1_phi_in("add13")) <> add13.io.Out(param.phi1_in("add13"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_pfor_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %a
  loop_L_5_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32* %b
  loop_L_5_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp2.io.LeftIO <> phi1.io.Out(param.icmp2_in("phi1"))

  // Wiring constant
  icmp2.io.RightIO.bits.data := 5.U
  icmp2.io.RightIO.bits.predicate := true.B
  icmp2.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring GEP instruction to the loop header
  getelementptr6.io.baseAddress <> loop_L_5_liveIN_0.io.Out(param.getelementptr6_in("field0"))

  // Wiring GEP instruction to the parent instruction
  getelementptr6.io.idx1 <> phi1.io.Out(param.getelementptr6_in("phi1"))


  // Wiring Load instruction to the parent instruction
  load7.io.GepAddr <> getelementptr6.io.Out(param.load7_in("getelementptr6"))
  load7.io.memResp <> RegisterFile.io.ReadOut(0)
  RegisterFile.io.ReadIn(0) <> load7.io.memReq




  // Wiring instructions
  mul8.io.LeftIO <> load7.io.Out(param.mul8_in("load7"))

  // Wiring constant
  mul8.io.RightIO.bits.data := 2.U
  mul8.io.RightIO.bits.predicate := true.B
  mul8.io.RightIO.valid := true.B

  // Wiring GEP instruction to the loop header
  getelementptr9.io.baseAddress <> loop_L_5_liveIN_1.io.Out(param.getelementptr9_in("field1"))

  // Wiring GEP instruction to the parent instruction
  getelementptr9.io.idx1 <> phi1.io.Out(param.getelementptr9_in("phi1"))


  store10.io.inData <> mul8.io.Out(param.store10_in("mul8"))



  // Wiring Store instruction to the parent instruction
  store10.io.GepAddr <> getelementptr9.io.Out(param.store10_in("getelementptr9"))
  store10.io.memResp  <> RegisterFile.io.WriteOut(0)
  RegisterFile.io.WriteIn(0) <> store10.io.memReq
  // store10.io.Out(0).ready := true.B  // Manual delete

  // Reattach (Manual add)
  reattach12.io.predicateIn(0) <> store10.io.Out(0)

  // Sync (Manual add)
  sync15.io.incIn(0) <> detach4.io.Out(2)
  sync15.io.decIn(0) <> reattach12.io.Out(0)



  // Wiring instructions
  add13.io.LeftIO <> phi1.io.Out(param.add13_in("phi1"))

  // Wiring constant
  add13.io.RightIO.bits.data := 1.U
  add13.io.RightIO.bits.predicate := true.B
  add13.io.RightIO.valid := true.B

  // Wiring return instruction
  ret16.io.predicateIn(0).bits.control := true.B
  ret16.io.predicateIn(0).bits.taskID := 0.U
  ret16.io.predicateIn(0).valid := true.B
  ret16.io.In.data("field0").bits.data := 1.U
  ret16.io.In.data("field0").bits.predicate := true.B
  ret16.io.In.data("field0").valid := true.B
  io.out <> ret16.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test01Main extends App {
  val dir = new File("RTL/cilk_for_test01") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test01DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


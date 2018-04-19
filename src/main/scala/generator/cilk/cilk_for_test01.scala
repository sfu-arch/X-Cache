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
    "br13" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br3" -> 0
  )


  val bb_pfor_end_pred = Map(
    "br3" -> 0
  )


  val bb_pfor_preattach_pred = Map(
    "br10" -> 0
  )


  val br0_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val br3_brn_bb = Map(
    "bb_pfor_detach" -> 0,
    "bb_pfor_end" -> 1
  )


  val br10_brn_bb = Map(
    "bb_pfor_preattach" -> 0
  )


  val br13_brn_bb = Map(
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
    "getelementptr5" -> 0,
    "load6" -> 1,
    "mul7" -> 2,
    "getelementptr8" -> 3,
    "store9" -> 4,
    "br10" -> 5
  )


  val bb_pfor_preattach_activate = Map(
    "reattach11" -> 0
  )


  val bb_pfor_inc_activate = Map(
    "add12" -> 0,
    "br13" -> 1
  )


  val bb_pfor_end_activate = Map(
    "sync14" -> 0
  )


  val bb_pfor_end_continue_activate = Map(
    "ret15" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add12" -> 1
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add12" -> 0
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


  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !20, !ScalaLabel !21
  val getelementptr5_in = Map(
    "field0" -> 0,
    "phi1" -> 1
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !22, !ScalaLabel !23
  val load6_in = Map(
    "getelementptr5" -> 0
  )


  //  %mul = mul i32 %0, 2, !UID !24, !ScalaLabel !25
  val mul7_in = Map(
    "load6" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i.0, !UID !26, !ScalaLabel !27
  val getelementptr8_in = Map(
    "field1" -> 0,
    "phi1" -> 2
  )


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !28, !ScalaLabel !29
  val store9_in = Map(
    "mul7" -> 0,
    "getelementptr8" -> 0
  )


  //  reattach label %pfor.inc, !UID !33, !BB_UID !34, !ScalaLabel !35
  val reattach11_in = Map(
    "" -> 2
  )


  //  %inc = add nsw i32 %i.0, 1, !UID !36, !ScalaLabel !37
  val add12_in = Map(
    "phi1" -> 3
  )


  //  sync label %pfor.end.continue, !UID !51, !BB_UID !52, !ScalaLabel !53
  val sync14_in = Map(
    "" -> 3
  )


  //  ret i32 1, !UID !54, !BB_UID !55, !ScalaLabel !56
  val ret15_in = Map(

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

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_pfor_cond = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_pfor_detach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 3))

  val bb_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 5))

  val bb_pfor_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 6))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %pfor.cond, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  pfor.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 4, ID = 1))


  //  %cmp = icmp slt i32 %i.0, 5, !UID !12, !ScalaLabel !13
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new CBranchNode(ID = 3))

  // [BasicBlock]  pfor.detach:

  //  detach label %pfor.body, label %pfor.inc, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4 = Module(new Detach(ID = 4))

  // [BasicBlock]  pfor.body:

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !20, !ScalaLabel !21
  val getelementptr5 = Module (new GepOneNode(NumOuts = 1, ID = 5)(numByte1 = 1))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !22, !ScalaLabel !23
  val load6 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=6,RouteID=0))


  //  %mul = mul i32 %0, 2, !UID !24, !ScalaLabel !25
  val mul7 = Module (new ComputeNode(NumOuts = 1, ID = 7, opCode = "mul")(sign=false))


  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i.0, !UID !26, !ScalaLabel !27
  val getelementptr8 = Module (new GepOneNode(NumOuts = 1, ID = 8)(numByte1 = 1))


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !28, !ScalaLabel !29
  val store9 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=9,RouteID=0))


  //  br label %pfor.preattach, !UID !30, !BB_UID !31, !ScalaLabel !32
  val br10 = Module (new UBranchNode(ID = 10))

  // [BasicBlock]  pfor.preattach:

  //  reattach label %pfor.inc, !UID !33, !BB_UID !34, !ScalaLabel !35
  val reattach11 = Module(new Reattach(NumPredOps=1, ID=11))

  // [BasicBlock]  pfor.inc:

  //  %inc = add nsw i32 %i.0, 1, !UID !36, !ScalaLabel !37
  val add12 = Module (new ComputeNode(NumOuts = 1, ID = 12, opCode = "add")(sign=false))


  //  br label %pfor.cond, !llvm.loop !38, !UID !48, !BB_UID !49, !ScalaLabel !50
  val br13 = Module (new UBranchNode(ID = 13))

  // [BasicBlock]  pfor.end:

  //  sync label %pfor.end.continue, !UID !51, !BB_UID !52, !ScalaLabel !53
  val sync14 = Module(new Sync(ID = 14,NumInc=1, NumDec=1,NumOuts =1))

  // [BasicBlock]  pfor.end.continue:

  //  ret i32 1, !UID !54, !BB_UID !55, !ScalaLabel !56
  val ret15 = Module(new RetNode(retTypes=List(32), ID=15))





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


  bb_entry.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br0 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br0")) <> br0.io.Out(param.br0_brn_bb("bb_pfor_cond"))


  //Connecting br3 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_pfor_detach"))


  //Connecting br3 to bb_pfor_end
  bb_pfor_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_pfor_end"))


  //Connecting br10 to bb_pfor_preattach
  bb_pfor_preattach.io.predicateIn <> br10.io.Out(param.br10_brn_bb("bb_pfor_preattach"))


  //Connecting br13 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br13")) <> br13.io.Out(param.br13_brn_bb("bb_pfor_cond"))


  //Connecting detach4 to bb_pfor_body
  bb_pfor_body.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_pfor_body"))


  //Connecting detach4 to bb_pfor_inc
  bb_pfor_inc.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_pfor_inc"))

  bb_pfor_end_continue.io.predicateIn <> sync14.io.Out(0) // Manually added



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



  detach4.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach4"))



  getelementptr5.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("getelementptr5"))

  load6.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("load6"))

  mul7.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("mul7"))

  getelementptr8.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("getelementptr8"))

  store9.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("store9"))

  br10.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("br10"))



  reattach11.io.enable <> bb_pfor_preattach.io.Out(param.bb_pfor_preattach_activate("reattach11"))



  add12.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("add12"))

  br13.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("br13"))



  sync14.io.enable <> bb_pfor_end.io.Out(param.bb_pfor_end_activate("sync14"))

  loop_L_5_liveIN_0.io.enable <> bb_pfor_end.io.Out(1)
  loop_L_5_liveIN_1.io.enable <> bb_pfor_end.io.Out(2)




  ret15.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("ret15"))





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
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.InData(param.phi1_phi_in("const_0")).bits.data := 0.U
  phi1.io.InData(param.phi1_phi_in("const_0")).bits.predicate := true.B
  phi1.io.InData(param.phi1_phi_in("const_0")).valid := true.B

  phi1.io.InData(param.phi1_phi_in("add12")) <> add12.io.Out(param.phi1_in("add12"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_pfor_cond.io.MaskBB(0)



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
  getelementptr5.io.baseAddress <> loop_L_5_liveIN_0.io.Out(param.getelementptr5_in("field0"))

  // Wiring GEP instruction to the parent instruction
  getelementptr5.io.idx1 <> phi1.io.Out(param.getelementptr5_in("phi1"))


  // Wiring Load instruction to the parent instruction
  load6.io.GepAddr <> getelementptr5.io.Out(param.load6_in("getelementptr5"))
  load6.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load6.io.memReq




  // Wiring instructions
  mul7.io.LeftIO <> load6.io.Out(param.mul7_in("load6"))

  // Wiring constant
  mul7.io.RightIO.bits.data := 2.U
  mul7.io.RightIO.bits.predicate := true.B
  mul7.io.RightIO.valid := true.B

  // Wiring GEP instruction to the loop header
  getelementptr8.io.baseAddress <> loop_L_5_liveIN_1.io.Out(param.getelementptr8_in("field1"))

  // Wiring GEP instruction to the parent instruction
  getelementptr8.io.idx1 <> phi1.io.Out(param.getelementptr8_in("phi1"))


  store9.io.inData <> mul7.io.Out(param.store9_in("mul7"))



  // Wiring Store instruction to the parent instruction
  store9.io.GepAddr <> getelementptr8.io.Out(param.store9_in("getelementptr8"))
  store9.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store9.io.memReq
//  store9.io.Out(0).ready := true.B // Manual delete


  // Reattach (Manual add)
  reattach11.io.predicateIn(0) <> store9.io.Out(0)

  // Sync (Manual add)
  sync14.io.incIn(0) <> detach4.io.Out(2)
  sync14.io.decIn(0) <> reattach11.io.Out(0)

  // Wiring instructions
  add12.io.LeftIO <> phi1.io.Out(param.add12_in("phi1"))

  // Wiring constant
  add12.io.RightIO.bits.data := 1.U
  add12.io.RightIO.bits.predicate := true.B
  add12.io.RightIO.valid := true.B

  // Wiring return instruction
  
  
  
  ret15.io.In.data("field0").bits.data := 1.U
  
  ret15.io.In.data("field0").valid := true.B
  io.out <> ret15.io.Out


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


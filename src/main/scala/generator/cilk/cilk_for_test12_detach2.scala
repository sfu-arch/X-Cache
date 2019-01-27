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

object Data_cilk_for_test12_detach2_FlowParam{

  val bb_my_pfor_body5_pred = Map(
    "active" -> 0
  )


  val bb_my_pfor_end_pred = Map(
    "br3" -> 0
  )


  val bb_my_pfor_preattach14_pred = Map(
    "br13" -> 0
  )


  val bb_my_pfor_cond7_pred = Map(
    "br0" -> 0,
    "br6" -> 1
  )


  val bb_my_pfor_detach9_pred = Map(
    "br3" -> 0
  )


  val br0_brn_bb = Map(
    "bb_my_pfor_cond7" -> 0
  )


  val br3_brn_bb = Map(
    "bb_my_pfor_detach9" -> 0,
    "bb_my_pfor_end" -> 1
  )


  val br6_brn_bb = Map(
    "bb_my_pfor_cond7" -> 0
  )


  val br13_brn_bb = Map(
    "bb_my_pfor_preattach14" -> 0
  )


  val bb_my_pfor_inc_pred = Map(
    "detach4" -> 0
  )


  val bb_my_offload_pfor_body10_pred = Map(
    "detach4" -> 0
  )


  val detach4_brn_bb = Map(
    "bb_my_offload_pfor_body10" -> 0,
    "bb_my_pfor_inc" -> 1
  )


  val bb_my_pfor_body5_activate = Map(
    "br0" -> 0
  )


  val bb_my_pfor_cond7_activate = Map(
    "phi1" -> 0,
    "icmp2" -> 1,
    "br3" -> 2
  )


  val bb_my_pfor_detach9_activate = Map(
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
    "sub8" -> 0,
    "getelementptr9" -> 1,
    "load10" -> 2,
    "add11" -> 3,
    "store12" -> 4,
    "br13" -> 5
  )


  val bb_my_pfor_preattach14_activate = Map(
    "ret14" -> 0
  )


  val bb_my_offload_pfor_body10_activate = Map(
    "call15" -> 0,
    "reattach16" -> 1
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add5" -> 1
  )


  //  %0 = phi i32 [ 0, %my_pfor.body5 ], [ %2, %my_pfor.inc ], !UID !5, !ScalaLabel !6
  val phi1_in = Map(
    "add5" -> 0
  )


  //  %1 = icmp ult i32 %0, %n.in, !UID !7, !ScalaLabel !8
  val icmp2_in = Map(
    "phi1" -> 0,
    "field0" -> 0
  )


  //  br i1 %1, label %my_pfor.detach9, label %my_pfor.end, !UID !9, !BB_UID !10, !ScalaLabel !11
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  detach label %my_offload.pfor.body10, label %my_pfor.inc, !UID !12, !BB_UID !13, !ScalaLabel !14
  val detach4_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %2 = add i32 %0, 1, !UID !15, !ScalaLabel !16
  val add5_in = Map(
    "phi1" -> 1
  )


  //  sync label %my_pfor.end.continue, !UID !22, !BB_UID !23, !ScalaLabel !24
  val sync7_in = Map(
    "" -> 2
  )


  //  %3 = sub i32 %n.in, 1, !UID !25, !ScalaLabel !26
  val sub8_in = Map(
    "field0" -> 1
  )


  //  %4 = getelementptr inbounds i32, i32* %a.in, i32 %3, !UID !27, !ScalaLabel !28
  val getelementptr9_in = Map(
    "field1" -> 0,
    "sub8" -> 0
  )


  //  %5 = load i32, i32* %4, align 4, !UID !29, !ScalaLabel !30
  val load10_in = Map(
    "getelementptr9" -> 0
  )


  //  %6 = add i32 %5, 1, !UID !31, !ScalaLabel !32
  val add11_in = Map(
    "load10" -> 0
  )


  //  store i32 %6, i32* %4, align 4, !UID !33, !ScalaLabel !34
  val store12_in = Map(
    "add11" -> 0,
    "getelementptr9" -> 1
  )


  //  ret void, !UID !38, !BB_UID !39, !ScalaLabel !40
  val ret14_in = Map(

  )


  //  call void @cilk_for_test12_detach3(i32* %a.in, i32 %0), !UID !41, !ScalaLabel !42
  val call15_in = Map(
    "field1" -> 1,
    "phi1" -> 2,
    "" -> 3
  )


  //  reattach label %my_pfor.inc, !UID !43, !BB_UID !44, !ScalaLabel !45
  val reattach16_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test12_detach2DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call15_out = Decoupled(new Call(List(32,32)))
    val call15_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test12_detach2DF(implicit p: Parameters) extends cilk_for_test12_detach2DFIO()(p) {



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

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,2)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */
  val lb_L_0 = Module(new LoopBlock(ID=999,NumIns=List(1,1),NumOuts=0,NumExits=1));


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_my_pfor_cond7 = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_my_pfor_detach9 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_my_pfor_inc = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 2, BID = 3))

  val bb_my_pfor_end = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 4)) // Manual

  val bb_my_pfor_end_continue = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 6, BID = 5))

  val bb_my_pfor_preattach14 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_my_offload_pfor_body10 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 7))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body5:

  //  br label %my_pfor.cond7, !UID !2, !BB_UID !3, !ScalaLabel !4
  val br0 = Module (new UBranchFastNode(ID = 0))

  // [BasicBlock]  my_pfor.cond7:

  //  %0 = phi i32 [ 0, %my_pfor.body5 ], [ %2, %my_pfor.inc ], !UID !5, !ScalaLabel !6
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 1))


  //  %1 = icmp ult i32 %0, %n.in, !UID !7, !ScalaLabel !8
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %1, label %my_pfor.detach9, label %my_pfor.end, !UID !9, !BB_UID !10, !ScalaLabel !11
  val br3 = Module (new CBranchFastNode(ID = 3))

  // [BasicBlock]  my_pfor.detach9:

  //  detach label %my_offload.pfor.body10, label %my_pfor.inc, !UID !12, !BB_UID !13, !ScalaLabel !14
  val detach4 = Module(new DetachFast(ID = 4))

  // [BasicBlock]  my_pfor.inc:

  //  %2 = add i32 %0, 1, !UID !15, !ScalaLabel !16
  val add5 = Module (new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign=false))


  //  br label %my_pfor.cond7, !llvm.loop !17, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br6 = Module (new UBranchFastNode(ID = 6))

  // [BasicBlock]  my_pfor.end:

  //  sync label %my_pfor.end.continue, !UID !22, !BB_UID !23, !ScalaLabel !24
  val sync7 = Module(new SyncTC(ID = 7, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  my_pfor.end.continue:

  //  %3 = sub i32 %n.in, 1, !UID !25, !ScalaLabel !26
  val sub8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "sub")(sign=false))


  //  %4 = getelementptr inbounds i32, i32* %a.in, i32 %3, !UID !27, !ScalaLabel !28
  val getelementptr9 = Module (new GepOneNode(NumOuts = 2, ID = 9)(numByte1 = 4))


  //  %5 = load i32, i32* %4, align 4, !UID !29, !ScalaLabel !30
  val load10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=10,RouteID=0))


  //  %6 = add i32 %5, 1, !UID !31, !ScalaLabel !32
  val add11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign=false))


  //  store i32 %6, i32* %4, align 4, !UID !33, !ScalaLabel !34
  val store12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=12,RouteID=0))


  //  br label %my_pfor.preattach14, !UID !35, !BB_UID !36, !ScalaLabel !37
  val br13 = Module (new UBranchFastNode(ID = 13))

  // [BasicBlock]  my_pfor.preattach14:

  //  ret void, !UID !38, !BB_UID !39, !ScalaLabel !40
  val ret14 = Module(new RetNode(retTypes=List(32), ID=14))

  // [BasicBlock]  my_offload.pfor.body10:

  //  call void @cilk_for_test12_detach3(i32* %a.in, i32 %0), !UID !41, !ScalaLabel !42
//  val call15 = Module(new CallNode(ID=15,argTypes=List(32,32),retTypes=List(32)))
  val callout15 = Module(new CallOutNode(ID=4,NumSuccOps=1,argTypes=List(32,32))) // Manually changed
  val callin15 = Module(new CallInNode(ID=499,argTypes=List(32)))


  //  reattach label %my_pfor.inc, !UID !43, !BB_UID !44, !ScalaLabel !45
  val reattach16 = Module(new Reattach(NumPredOps=1, ID=16))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test12_detach2_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_my_pfor_body5.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br0 to bb_my_pfor_cond7
//  bb_my_pfor_cond7.io.activate <> br0.io.Out(param.br0_brn_bb("bb_my_pfor_cond7"))
  lb_L_0.io.enable <> br0.io.Out(param.br0_brn_bb("bb_my_pfor_cond7"))  // manually added

  bb_my_pfor_cond7.io.activate <> lb_L_0.io.activate // manually corrected


  //Connecting br3 to bb_my_pfor_detach9
  bb_my_pfor_detach9.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_detach9"))


  //Connecting br3 to bb_my_pfor_end
//  bb_my_pfor_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_end"))
  lb_L_0.io.loopExit(0) <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_end")) // Manual
  bb_my_pfor_end.io.predicateIn <> lb_L_0.io.endEnable


  //Connecting br6 to bb_my_pfor_cond7
  bb_my_pfor_cond7.io.loopBack <> br6.io.Out(param.br6_brn_bb("bb_my_pfor_cond7"))
//  lb_L_0.io.latchEnable   <> br6.io.Out(1) // manual
  lb_L_0.io.latchEnable   <> callout15.io.SuccOp(0) // manual


  //Connecting br13 to bb_my_pfor_preattach14
  bb_my_pfor_preattach14.io.predicateIn <> br13.io.Out(param.br13_brn_bb("bb_my_pfor_preattach14"))


  //Connecting detach4 to bb_my_offload_pfor_body10
  bb_my_offload_pfor_body10.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_my_offload_pfor_body10"))


  //Connecting detach4 to bb_my_pfor_inc
  bb_my_pfor_inc.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_my_pfor_inc"))

  bb_my_pfor_end_continue.io.predicateIn <> sync7.io.Out(0) // added manually



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("br0"))



  phi1.io.enable <> bb_my_pfor_cond7.io.Out(param.bb_my_pfor_cond7_activate("phi1"))

  icmp2.io.enable <> bb_my_pfor_cond7.io.Out(param.bb_my_pfor_cond7_activate("icmp2"))

  br3.io.enable <> bb_my_pfor_cond7.io.Out(param.bb_my_pfor_cond7_activate("br3"))



  detach4.io.enable <> bb_my_pfor_detach9.io.Out(param.bb_my_pfor_detach9_activate("detach4"))



  add5.io.enable <> bb_my_pfor_inc.io.Out(param.bb_my_pfor_inc_activate("add5"))

  br6.io.enable <> bb_my_pfor_inc.io.Out(param.bb_my_pfor_inc_activate("br6"))



  sync7.io.enable <> bb_my_pfor_end.io.Out(param.bb_my_pfor_end_activate("sync7"))



  sub8.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("sub8"))

  getelementptr9.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("getelementptr9"))

  load10.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("load10"))

  add11.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("add11"))

  store12.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("store12"))

  br13.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("br13"))



  ret14.io.enable <> bb_my_pfor_preattach14.io.Out(param.bb_my_pfor_preattach14_activate("ret14"))



  callout15.io.enable <> bb_my_offload_pfor_body10.io.Out(param.bb_my_offload_pfor_body10_activate("call15"))
  callin15.io.enable.enq(ControlBundle.active())


  reattach16.io.enable.enq(ControlBundle.active())// <> bb_my_offload_pfor_body10.io.Out(param.bb_my_offload_pfor_body10_activate("reattach16"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */

  // Connecting function argument to the loop header
  //i32 %n.in
  lb_L_0.io.In(0) <> InputSplitter.io.Out.data.elements("field0")(0)

  // Connecting function argument to the loop header
  //i32* %a.in
  lb_L_0.io.In(1) <> InputSplitter.io.Out.data.elements("field1")(0)


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

  phi1.io.Mask <> bb_my_pfor_cond7.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp2.io.LeftIO <> phi1.io.Out(param.icmp2_in("phi1"))

  // Wiring Binary instruction to the loop header
  icmp2.io.RightIO <> lb_L_0.io.liveIn.elements("field0")(0)

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring instructions
  add5.io.LeftIO <> phi1.io.Out(param.add5_in("phi1"))

  // Wiring constant
  add5.io.RightIO.bits.data := 1.U
  add5.io.RightIO.bits.predicate := true.B
  add5.io.RightIO.valid := true.B

  // Wiring Binary instruction to the function argument
  sub8.io.LeftIO <> InputSplitter.io.Out.data.elements("field0")(1)

  // Wiring constant
  sub8.io.RightIO.bits.data := 1.U
  sub8.io.RightIO.bits.predicate := true.B
  sub8.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr9.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(1)

  // Wiring GEP instruction to the parent instruction
  getelementptr9.io.idx1 <> sub8.io.Out(param.getelementptr9_in("sub8"))


  // Wiring Load instruction to the parent instruction
  load10.io.GepAddr <> getelementptr9.io.Out(param.load10_in("getelementptr9"))
  load10.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load10.io.memReq




  // Wiring instructions
  add11.io.LeftIO <> load10.io.Out(param.add11_in("load10"))

  // Wiring constant
  add11.io.RightIO.bits.data := 1.U
  add11.io.RightIO.bits.predicate := true.B
  add11.io.RightIO.valid := true.B

  store12.io.inData <> add11.io.Out(param.store12_in("add11"))



  // Wiring Store instruction to the parent instruction
  store12.io.GepAddr <> getelementptr9.io.Out(param.store12_in("getelementptr9"))
  store12.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store12.io.memReq
  //store12.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  
  
  
/*
  ret14.io.In.data("field0").bits.data := 1.U
  
  ret14.io.In.data("field0").valid := true.B
  */
  ret14.io.In.elements("field0") <> store12.io.Out(0)
  io.out <> ret14.io.Out


  // Wiring Call to I/O
  callout15.io.In("field0") <> lb_L_0.io.liveIn.elements("field1")(0) // manual
  callout15.io.In("field1") <> phi1.io.Out(param.call15_in("phi1")) // manual
  io.call15_out <> callout15.io.Out(0)

  callin15.io.In <> io.call15_in
  callin15.io.Out.enable.ready := true.B

  // Reattach (Manual add)
  reattach16.io.predicateIn(0) <> callin15.io.Out.data("field0")

  // Sync (Manual add)
  sync7.io.incIn(0) <> detach4.io.Out(2)
  sync7.io.decIn(0) <> reattach16.io.Out(0)


}

import java.io.{File, FileWriter}
object cilk_for_test12_detach2Main extends App {
  val dir = new File("RTL/cilk_for_test12_detach2") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test12_detach2DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


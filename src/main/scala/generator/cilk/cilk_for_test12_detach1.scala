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

object Data_cilk_for_test12_detach1_FlowParam{

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


  val bb_my_pfor_end17_pred = Map(
    "br3" -> 0
  )


  val bb_my_pfor_preattach22_pred = Map(
    "br16" -> 0
  )


  val br0_brn_bb = Map(
    "bb_my_pfor_cond2" -> 0
  )


  val br3_brn_bb = Map(
    "bb_my_pfor_detach4" -> 0,
    "bb_my_pfor_end17" -> 1
  )


  val br6_brn_bb = Map(
    "bb_my_pfor_cond2" -> 0
  )


  val br16_brn_bb = Map(
    "bb_my_pfor_preattach22" -> 0
  )


  val bb_my_pfor_inc15_pred = Map(
    "detach4" -> 0
  )


  val bb_my_offload_pfor_body5_pred = Map(
    "detach4" -> 0
  )


  val detach4_brn_bb = Map(
    "bb_my_offload_pfor_body5" -> 0,
    "bb_my_pfor_inc15" -> 1
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


  val bb_my_pfor_inc15_activate = Map(
    "add5" -> 0,
    "br6" -> 1
  )


  val bb_my_pfor_end17_activate = Map(
    "sync7" -> 0
  )


  val bb_my_pfor_end_continue18_activate = Map(
    "sub8" -> 0,
    "getelementptr9" -> 1,
    "load10" -> 2,
    "add11" -> 3,
    "store12" -> 4,
    "load13" -> 5,
    "add14" -> 6,
    "store15" -> 7,
    "br16" -> 8
  )


  val bb_my_pfor_preattach22_activate = Map(
    "ret17" -> 0
  )


  val bb_my_offload_pfor_body5_activate = Map(
    "call18" -> 0,
    "reattach19" -> 1
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add5" -> 1
  )


  //  %0 = phi i32 [ 0, %my_pfor.body ], [ %2, %my_pfor.inc15 ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add5" -> 0
  )


  //  %1 = icmp ult i32 %0, %n.in, !UID !12, !ScalaLabel !13
  val icmp2_in = Map(
    "phi1" -> 0,
    "field0" -> 0
  )


  //  br i1 %1, label %my_pfor.detach4, label %my_pfor.end17, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  detach label %my_offload.pfor.body5, label %my_pfor.inc15, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %2 = add i32 %0, 1, !UID !20, !ScalaLabel !21
  val add5_in = Map(
    "phi1" -> 1
  )


  //  sync label %my_pfor.end.continue18, !UID !38, !BB_UID !39, !ScalaLabel !40
  val sync7_in = Map(
    "" -> 2
  )


  //  %3 = sub i32 %n.in, 1, !UID !41, !ScalaLabel !42
  val sub8_in = Map(
    "field0" -> 1
  )


  //  %4 = getelementptr inbounds i32, i32* %a.in, i32 %3, !UID !43, !ScalaLabel !44
  val getelementptr9_in = Map(
    "field1" -> 0,
    "sub8" -> 0
  )


  //  %5 = load i32, i32* %4, align 4, !UID !45, !ScalaLabel !46
  val load10_in = Map(
    "getelementptr9" -> 0
  )


  //  %6 = add i32 %5, 1, !UID !47, !ScalaLabel !48
  val add11_in = Map(
    "load10" -> 0
  )


  //  store i32 %6, i32* %4, align 4, !UID !49, !ScalaLabel !50
  val store12_in = Map(
    "add11" -> 0,
    "getelementptr9" -> 1
  )


  //  %7 = load i32, i32* %result.in, align 4, !UID !51, !ScalaLabel !52
  val load13_in = Map(
    "field2" -> 0
  )


  //  %8 = add i32 %7, %5, !UID !53, !ScalaLabel !54
  val add14_in = Map(
    "load13" -> 0,
    "load10" -> 1
  )


  //  store i32 %8, i32* %result.in, align 4, !UID !55, !ScalaLabel !56
  val store15_in = Map(
    "add14" -> 0,
    "field2" -> 1
  )


  //  ret void, !UID !60, !BB_UID !61, !ScalaLabel !62
  val ret17_in = Map(

  )


  //  call void @cilk_for_test12_detach2(i32 %n.in, i32* %a.in), !UID !63, !ScalaLabel !64
  val call18_in = Map(
    "field0" -> 2,
    "field1" -> 1,
    "" -> 3
  )


  //  reattach label %my_pfor.inc15, !UID !65, !BB_UID !66, !ScalaLabel !67
  val reattach19_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test12_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val call18_out = Decoupled(new Call(List(32,32)))
    val call18_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test12_detach1DF(implicit p: Parameters) extends cilk_for_test12_detach1DFIO()(p) {



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

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCallNew(List(2,2,2)))
  InputSplitter.io.In <> io.in

  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */
  val lb_L_0 = Module(new LoopBlock(ID=999,NumIns=List(2,1),NumOuts=0,NumExits=1));


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_my_pfor_cond2 = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_my_pfor_detach4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_my_pfor_inc15 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 3))

  val bb_my_pfor_end17 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4)) // Manual

  val bb_my_pfor_end_continue18 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 5))

  val bb_my_pfor_preattach22 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_my_offload_pfor_body5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  br label %my_pfor.cond2, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchFastNode(ID = 0))

  // [BasicBlock]  my_pfor.cond2:

  //  %0 = phi i32 [ 0, %my_pfor.body ], [ %2, %my_pfor.inc15 ], !UID !10, !ScalaLabel !11
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))


  //  %1 = icmp ult i32 %0, %n.in, !UID !12, !ScalaLabel !13
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %1, label %my_pfor.detach4, label %my_pfor.end17, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new CBranchFastNode(ID = 3))

  // [BasicBlock]  my_pfor.detach4:

  //  detach label %my_offload.pfor.body5, label %my_pfor.inc15, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4 = Module(new DetachFast(ID = 4))

  // [BasicBlock]  my_pfor.inc15:

  //  %2 = add i32 %0, 1, !UID !20, !ScalaLabel !21
  val add5 = Module (new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign=false))


  //  br label %my_pfor.cond2, !llvm.loop !22, !UID !35, !BB_UID !36, !ScalaLabel !37
  val br6 = Module (new UBranchFastNode(ID = 6))

  // [BasicBlock]  my_pfor.end17:

  //  sync label %my_pfor.end.continue18, !UID !38, !BB_UID !39, !ScalaLabel !40
  val sync7 = Module(new SyncTC(ID = 7, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  my_pfor.end.continue18:

  //  %3 = sub i32 %n.in, 1, !UID !41, !ScalaLabel !42
  val sub8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "sub")(sign=false))


  //  %4 = getelementptr inbounds i32, i32* %a.in, i32 %3, !UID !43, !ScalaLabel !44
  val getelementptr9 = Module (new GepOneNode(NumOuts = 2, ID = 9)(numByte1 = 4))


  //  %5 = load i32, i32* %4, align 4, !UID !45, !ScalaLabel !46
  val load10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2,ID=10,RouteID=0))


  //  %6 = add i32 %5, 1, !UID !47, !ScalaLabel !48
  val add11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign=false))


  //  store i32 %6, i32* %4, align 4, !UID !49, !ScalaLabel !50
  val store12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=12,RouteID=0))


  //  %7 = load i32, i32* %result.in, align 4, !UID !51, !ScalaLabel !52
  val load13 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=13,RouteID=1))


  //  %8 = add i32 %7, %5, !UID !53, !ScalaLabel !54
  val add14 = Module (new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign=false))


  //  store i32 %8, i32* %result.in, align 4, !UID !55, !ScalaLabel !56
  val store15 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=15,RouteID=1))


  //  br label %my_pfor.preattach22, !UID !57, !BB_UID !58, !ScalaLabel !59
  val br16 = Module (new UBranchFastNode(ID = 16))

  // [BasicBlock]  my_pfor.preattach22:

  //  ret void, !UID !60, !BB_UID !61, !ScalaLabel !62
  val ret17 = Module(new RetNode(retTypes=List(32), ID=17))

  // [BasicBlock]  my_offload.pfor.body5:

  //  call void @cilk_for_test12_detach2(i32 %n.in, i32* %a.in), !UID !63, !ScalaLabel !64
//  val call18 = Module(new CallNode(ID=18,argTypes=List(32,32),retTypes=List(32)))
  val callout18 = Module(new CallOutNode(ID=4,NumSuccOps=1,argTypes=List(32,32))) // Manually changed
  val callin18 = Module(new CallInNode(ID=499,argTypes=List(32)))


  //  reattach label %my_pfor.inc15, !UID !65, !BB_UID !66, !ScalaLabel !67
  val reattach19 = Module(new Reattach(NumPredOps=1, ID=19))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test12_detach1_FlowParam



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
//  bb_my_pfor_cond2.io.activate <> br0.io.Out(param.br0_brn_bb("bb_my_pfor_cond2"))
  lb_L_0.io.enable <> br0.io.Out(param.br0_brn_bb("bb_my_pfor_cond2"))  // manually added

  bb_my_pfor_cond2.io.activate <> lb_L_0.io.activate // manually corrected


  //Connecting br3 to bb_my_pfor_detach4
  bb_my_pfor_detach4.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_detach4"))


  //Connecting br3 to bb_my_pfor_end17
//  bb_my_pfor_end17.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_end17"))
  lb_L_0.io.loopExit(0) <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_end17")) // Manual
  bb_my_pfor_end17.io.predicateIn <> lb_L_0.io.endEnable


  //Connecting br6 to bb_my_pfor_cond2
  bb_my_pfor_cond2.io.loopBack <> br6.io.Out(param.br6_brn_bb("bb_my_pfor_cond2"))
//  lb_L_0.io.latchEnable   <> br6.io.Out(1) // manual
  lb_L_0.io.latchEnable   <> callout18.io.SuccOp(0) // manual


  //Connecting br16 to bb_my_pfor_preattach22
  bb_my_pfor_preattach22.io.predicateIn <> br16.io.Out(param.br16_brn_bb("bb_my_pfor_preattach22"))


  //Connecting detach4 to bb_my_offload_pfor_body5
  bb_my_offload_pfor_body5.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_my_offload_pfor_body5"))


  //Connecting detach4 to bb_my_pfor_inc15
  bb_my_pfor_inc15.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_my_pfor_inc15"))

  bb_my_pfor_end_continue18.io.predicateIn <> sync7.io.Out(0) // added manually



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



  add5.io.enable <> bb_my_pfor_inc15.io.Out(param.bb_my_pfor_inc15_activate("add5"))

  br6.io.enable <> bb_my_pfor_inc15.io.Out(param.bb_my_pfor_inc15_activate("br6"))



  sync7.io.enable <> bb_my_pfor_end17.io.Out(param.bb_my_pfor_end17_activate("sync7"))
/*
  loop_L_0_liveIN_0.io.enable <> bb_my_pfor_end17.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_my_pfor_end17.io.Out(2)
*/



  sub8.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("sub8"))

  getelementptr9.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("getelementptr9"))

  load10.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("load10"))

  add11.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("add11"))

  store12.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("store12"))

  load13.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("load13"))

  add14.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("add14"))

  store15.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("store15"))

  br16.io.enable <> bb_my_pfor_end_continue18.io.Out(param.bb_my_pfor_end_continue18_activate("br16"))



  ret17.io.enable <> bb_my_pfor_preattach22.io.Out(param.bb_my_pfor_preattach22_activate("ret17"))



  callout18.io.enable <> bb_my_offload_pfor_body5.io.Out(param.bb_my_offload_pfor_body5_activate("call18"))
  callin18.io.enable.enq(ControlBundle.active())


  reattach19.io.enable.enq(ControlBundle.active())// <> bb_my_offload_pfor_body5.io.Out(param.bb_my_offload_pfor_body5_activate("reattach19"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */

/*
  // Connecting function argument to the loop header
  //i32 %n.in
  loop_L_0_liveIN_0.io.InData <> field0_expand.io.Out(0)

  // Connecting function argument to the loop header
  //i32* %a.in
  loop_L_0_liveIN_1.io.InData <> field1_expand.io.Out(0)
*/
  // Connecting function argument to the loop header
  //i32 %n.in
  lb_L_0.io.In(0) <> InputSplitter.io.Out.data("field0")(0)// field0_expand.io.Out(0)

  // Connecting function argument to the loop header
  //i32* %a.in
  lb_L_0.io.In(1) <> InputSplitter.io.Out.data("field1")(0)//field1_expand.io.Out(0)


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
  icmp2.io.LeftIO <> phi1.io.Out(param.icmp2_in("phi1"))

  // Wiring Binary instruction to the loop header
  icmp2.io.RightIO <> lb_L_0.io.liveIn.data("field0")(0)

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring instructions
  add5.io.LeftIO <> phi1.io.Out(param.add5_in("phi1"))

  // Wiring constant
  add5.io.RightIO.bits.data := 1.U
  add5.io.RightIO.bits.predicate := true.B
  add5.io.RightIO.valid := true.B

  // Wiring Binary instruction to the function argument
  sub8.io.LeftIO <> InputSplitter.io.Out.data("field0")(1)//field0_expand.io.Out(1)

  // Wiring constant
  sub8.io.RightIO.bits.data := 1.U
  sub8.io.RightIO.bits.predicate := true.B
  sub8.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr9.io.baseAddress <> InputSplitter.io.Out.data("field1")(1)//field1_expand.io.Out(1)

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
  store12.io.Out(0).ready := true.B



  // Wiring Load instruction to the function argument
  load13.io.GepAddr <>  InputSplitter.io.Out.data("field2")(0)//field2_expand.io.Out(0)
  load13.io.memResp <> CacheMem.io.ReadOut(1) // manual
  CacheMem.io.ReadIn(1) <> load13.io.memReq  // manual



  // Wiring instructions
  add14.io.LeftIO <> load13.io.Out(param.add14_in("load13"))

  // Wiring instructions
  add14.io.RightIO <> load10.io.Out(param.add14_in("load10"))

  store15.io.inData <> add14.io.Out(param.store15_in("add14"))



  // Wiring Store instruction to the function argument
  store15.io.GepAddr <>  InputSplitter.io.Out.data("field2")(1)//field2_expand.io.Out(1)
  store15.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store15.io.memReq
//  store15.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  
  
  
/*
  ret17.io.In.data("field0").bits.data := 1.U
  
  ret17.io.In.data("field0").valid := true.B
*/
  ret17.io.In.data("field0") <> store15.io.Out(0)
  io.out <> ret17.io.Out


  // Wiring Call to I/O
  callout18.io.In("field0") <> lb_L_0.io.liveIn.data("field0")(1) // manual
  callout18.io.In("field1") <> lb_L_0.io.liveIn.data("field1")(0) // manual

  io.call18_out <> callout18.io.Out(0)
  callin18.io.Out.enable.ready := true.B

  callin18.io.In <> io.call18_in

  // Reattach (Manual add)
  reattach19.io.predicateIn(0) <> callin18.io.Out.data("field0")

  // Sync (Manual add)
  sync7.io.incIn(0) <> detach4.io.Out(2)
  sync7.io.decIn(0) <> reattach19.io.Out(0)


}

import java.io.{File, FileWriter}
object cilk_for_test12_detach1Main extends App {
  val dir = new File("RTL/cilk_for_test12_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test12_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


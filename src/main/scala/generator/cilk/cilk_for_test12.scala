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

object Data_cilk_for_test12_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_pfor_cond_pred = Map(
    "br2" -> 0,
    "br8" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br5" -> 0
  )


  val bb_pfor_end25_pred = Map(
    "br5" -> 0
  )


  val br2_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val br5_brn_bb = Map(
    "bb_pfor_detach" -> 0,
    "bb_pfor_end25" -> 1
  )


  val br8_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val bb_pfor_inc23_pred = Map(
    "detach6" -> 0
  )


  val bb_offload_pfor_body_pred = Map(
    "detach6" -> 0
  )


  val detach6_brn_bb = Map(
    "bb_offload_pfor_body" -> 0,
    "bb_pfor_inc23" -> 1
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


  val bb_pfor_inc23_activate = Map(
    "add7" -> 0,
    "br8" -> 1
  )


  val bb_pfor_end25_activate = Map(
    "sync9" -> 0
  )


  val bb_pfor_end_continue26_activate = Map(
    "load10" -> 0,
    "sdiv11" -> 1,
    "store12" -> 2,
    "load13" -> 3,
    "ret14" -> 4
  )


  val bb_offload_pfor_body_activate = Map(
    "call15" -> 0,
    "reattach16" -> 1
  )


  val phi3_phi_in = Map(
    "const_0" -> 0,
    "add7" -> 1
  )


  //  %result = alloca i32, align 4, !UID !7, !ScalaLabel !8
  val alloca0_in = Map(

  )


  //  store i32 0, i32* %result, align 4, !UID !9, !ScalaLabel !10
  val store1_in = Map(
    "alloca0" -> 0
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc24, %pfor.inc23 ], !UID !14, !ScalaLabel !15
  val phi3_in = Map(
    "add7" -> 0
  )


  //  %cmp = icmp ult i32 %i.0, 3, !UID !16, !ScalaLabel !17
  val icmp4_in = Map(
    "phi3" -> 0
  )


  //  br i1 %cmp, label %pfor.detach, label %pfor.end25, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5_in = Map(
    "icmp4" -> 0
  )


  //  detach label %offload.pfor.body, label %pfor.inc23, !UID !21, !BB_UID !22, !ScalaLabel !23
  val detach6_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %inc24 = add i32 %i.0, 1, !UID !24, !ScalaLabel !25
  val add7_in = Map(
    "phi3" -> 1
  )


  //  sync label %pfor.end.continue26, !UID !39, !BB_UID !40, !ScalaLabel !41
  val sync9_in = Map(
    "" -> 2
  )


  //  %0 = load i32, i32* %result, align 4, !UID !42, !ScalaLabel !43
  val load10_in = Map(
    "alloca0" -> 1
  )


  //  %div = sdiv i32 %0, 2, !UID !44, !ScalaLabel !45
  val sdiv11_in = Map(
    "load10" -> 0
  )


  //  store i32 %div, i32* %result, align 4, !UID !46, !ScalaLabel !47
  val store12_in = Map(
    "sdiv11" -> 0,
    "alloca0" -> 2
  )


  //  %1 = load i32, i32* %result, align 4, !UID !48, !ScalaLabel !49
  val load13_in = Map(
    "alloca0" -> 3
  )


  //  ret i32 %1, !UID !50, !BB_UID !51, !ScalaLabel !52
  val ret14_in = Map(
    "load13" -> 0
  )


  //  call void @cilk_for_test12_detach1(i32 %n, i32* %a, i32* %result), !UID !53, !ScalaLabel !54
  val call15_in = Map(
    "field1" -> 0,
    "field0" -> 0,
    "alloca0" -> 4,
    "" -> 3
  )


  //  reattach label %pfor.inc23, !UID !55, !BB_UID !56, !ScalaLabel !57
  val reattach16_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test12DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call15_out = Decoupled(new Call(List(32,32,32)))
    val call15_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test12DF(implicit p: Parameters) extends cilk_for_test12DFIO()(p) {



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

  val InputSplitter = Module(new SplitCallNew(List(1,1)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */
  val lb_L_0 = Module(new LoopBlock(ID=999,NumIns=3,NumOuts=0,NumExits=1));


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_pfor_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_pfor_detach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_inc23 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 3))

  val bb_pfor_end25 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 4))

  val bb_pfor_end_continue26 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 5))

  val bb_offload_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %result = alloca i32, align 4, !UID !7, !ScalaLabel !8
  val alloca0 = Module(new AllocaNode(NumOuts=5, RouteID=0, ID=0))


  //  store i32 0, i32* %result, align 4, !UID !9, !ScalaLabel !10
  val store1 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))


  //  br label %pfor.cond, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br2 = Module (new UBranchFastNode(ID = 2))

  // [BasicBlock]  pfor.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc24, %pfor.inc23 ], !UID !14, !ScalaLabel !15
  val phi3 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 3))


  //  %cmp = icmp ult i32 %i.0, 3, !UID !16, !ScalaLabel !17
  val icmp4 = Module (new IcmpNode(NumOuts = 1, ID = 4, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %pfor.detach, label %pfor.end25, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5 = Module (new CBranchFastNode(ID = 5))

  // [BasicBlock]  pfor.detach:

  //  detach label %offload.pfor.body, label %pfor.inc23, !UID !21, !BB_UID !22, !ScalaLabel !23
  val detach6 = Module(new DetachFast(ID = 6))

  // [BasicBlock]  pfor.inc23:

  //  %inc24 = add i32 %i.0, 1, !UID !24, !ScalaLabel !25
  val add7 = Module (new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign=false))


  //  br label %pfor.cond, !llvm.loop !26, !UID !36, !BB_UID !37, !ScalaLabel !38
  val br8 = Module (new UBranchFastNode(ID = 8))

  // [BasicBlock]  pfor.end25:

  //  sync label %pfor.end.continue26, !UID !39, !BB_UID !40, !ScalaLabel !41
  val sync9 = Module(new SyncTC(ID = 9, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  pfor.end.continue26:

  //  %0 = load i32, i32* %result, align 4, !UID !42, !ScalaLabel !43
  val load10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=10,RouteID=0))


  //  %div = sdiv i32 %0, 2, !UID !44, !ScalaLabel !45
  val sdiv11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "udiv")(sign=false))


  //  store i32 %div, i32* %result, align 4, !UID !46, !ScalaLabel !47
  val store12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=12,RouteID=1))


  //  %1 = load i32, i32* %result, align 4, !UID !48, !ScalaLabel !49
  val load13 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=13,RouteID=1))


  //  ret i32 %1, !UID !50, !BB_UID !51, !ScalaLabel !52
  val ret14 = Module(new RetNode(retTypes=List(32), ID=14))

  // [BasicBlock]  offload.pfor.body:

  //  call void @cilk_for_test12_detach1(i32 %n, i32* %a, i32* %result), !UID !53, !ScalaLabel !54
//  val call15 = Module(new CallNode(ID=15,argTypes=List(32,32,32),retTypes=List(32)))
  val callout15 = Module(new CallOutNode(ID=4,NumSuccOps=1,argTypes=List(32,32,32))) // Manually changed
  val callin15 = Module(new CallInNode(ID=499,argTypes=List(32)))


  //  reattach label %pfor.inc23, !UID !55, !BB_UID !56, !ScalaLabel !57
  val reattach16 = Module(new Reattach(NumPredOps=1, ID=16))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test12_FlowParam



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
//  bb_pfor_cond.io.activate <> br2.io.Out(param.br2_brn_bb("bb_pfor_cond"))
  lb_L_0.io.enable <> br2.io.Out(param.br2_brn_bb("bb_pfor_cond"))  // manually added

  bb_pfor_cond.io.activate <> lb_L_0.io.activate // manually corrected


  //Connecting br5 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_pfor_detach"))


  //Connecting br5 to bb_pfor_end25
//  bb_pfor_end25.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_pfor_end25"))
  lb_L_0.io.loopExit(0) <> br5.io.Out(param.br5_brn_bb("bb_pfor_end25")) // Manual
  bb_pfor_end25.io.predicateIn <> lb_L_0.io.endEnable


  //Connecting br8 to bb_pfor_cond
  bb_pfor_cond.io.loopBack <> br8.io.Out(param.br8_brn_bb("bb_pfor_cond"))
//  lb_L_0.io.latchEnable   <> br8.io.Out(1) // manual
  lb_L_0.io.latchEnable   <> callout15.io.SuccOp(0) // manual


  //Connecting detach6 to bb_offload_pfor_body
  bb_offload_pfor_body.io.predicateIn <> detach6.io.Out(param.detach6_brn_bb("bb_offload_pfor_body"))


  //Connecting detach6 to bb_pfor_inc23
  bb_pfor_inc23.io.predicateIn <> detach6.io.Out(param.detach6_brn_bb("bb_pfor_inc23"))

  bb_pfor_end_continue26.io.predicateIn <> sync9.io.Out(0) // added manually



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



  add7.io.enable <> bb_pfor_inc23.io.Out(param.bb_pfor_inc23_activate("add7"))

  br8.io.enable <> bb_pfor_inc23.io.Out(param.bb_pfor_inc23_activate("br8"))



  sync9.io.enable <> bb_pfor_end25.io.Out(param.bb_pfor_end25_activate("sync9"))

/*
  loop_L_0_liveIN_0.io.enable <> bb_pfor_end25.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_pfor_end25.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_pfor_end25.io.Out(3)
*/



  load10.io.enable <> bb_pfor_end_continue26.io.Out(param.bb_pfor_end_continue26_activate("load10"))

  sdiv11.io.enable <> bb_pfor_end_continue26.io.Out(param.bb_pfor_end_continue26_activate("sdiv11"))

  store12.io.enable <> bb_pfor_end_continue26.io.Out(param.bb_pfor_end_continue26_activate("store12"))

  load13.io.enable <> bb_pfor_end_continue26.io.Out(param.bb_pfor_end_continue26_activate("load13"))

  ret14.io.enable <> bb_pfor_end_continue26.io.Out(param.bb_pfor_end_continue26_activate("ret14"))



//  call15.io.In.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("call15"))
  callout15.io.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("call15"))
  callin15.io.enable.enq(ControlBundle.active())

//  reattach16.io.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach16"))
  reattach16.io.enable.enq(ControlBundle.active())




  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */

/*
  // Connecting function argument to the loop header
  //i32* %a
  loop_L_0_liveIN_0.io.InData <> field0_expand.io.Out(0)

  // Connecting function argument to the loop header
  //i32 %n
  loop_L_0_liveIN_1.io.InData <> field1_expand.io.Out(0)

  // Connecting instruction to the loop header
  //  %result = alloca i32, align 4, !UID !7, !ScalaLabel !8
  loop_L_0_liveIN_2.io.InData <> alloca0.io.Out(param.call15_in("alloca0"))
*/
  // Connecting function argument to the loop header
  //i32* %a
  lb_L_0.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  // Connecting function argument to the loop header
  //i32 %n
  lb_L_0.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  // Connecting instruction to the loop header
  //  %result = alloca i32, align 4, !UID !7, !ScalaLabel !8
  lb_L_0.io.In(2) <> alloca0.io.Out(param.call15_in("alloca0"))


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

  phi3.io.InData(param.phi3_phi_in("add7")) <> add7.io.Out(param.phi3_in("add7"))

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
  alloca0.io.allocaInputIO.bits.size      := 1.U
  alloca0.io.allocaInputIO.bits.numByte   := 4.U
  alloca0.io.allocaInputIO.bits.predicate := true.B
  alloca0.io.allocaInputIO.bits.valid     := true.B
  alloca0.io.allocaInputIO.valid          := true.B

  // Connecting Alloca to Stack
  StackPointer.io.InData(0) <> alloca0.io.allocaReqIO
  alloca0.io.allocaRespIO <> StackPointer.io.OutData(0)


  // Wiring constant instructions to store
  store1.io.inData.bits.data := 0.U
  store1.io.inData.bits.predicate := true.B
  store1.io.inData.valid := true.B



  // Wiring Store instruction to the parent instruction
  store1.io.GepAddr <> alloca0.io.Out(param.store1_in("alloca0"))
  store1.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store1.io.memReq
  store1.io.Out(0).ready := true.B



  // Wiring instructions
  icmp4.io.LeftIO <> phi3.io.Out(param.icmp4_in("phi3"))

  // Wiring constant
  icmp4.io.RightIO.bits.data := 3.U
  icmp4.io.RightIO.bits.predicate := true.B
  icmp4.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br5.io.CmpIO <> icmp4.io.Out(param.br5_in("icmp4"))

  // Wiring instructions
  add7.io.LeftIO <> phi3.io.Out(param.add7_in("phi3"))

  // Wiring constant
  add7.io.RightIO.bits.data := 1.U
  add7.io.RightIO.bits.predicate := true.B
  add7.io.RightIO.valid := true.B

  // Wiring Load instruction to another instruction
  load10.io.GepAddr <> alloca0.io.Out(param.load10_in("alloca0"))
  load10.io.memResp <> CacheMem.io.ReadOut(0)  // Manually added
  CacheMem.io.ReadIn(0) <> load10.io.memReq    // Manually added





  // Wiring instructions
  sdiv11.io.LeftIO <> load10.io.Out(param.sdiv11_in("load10"))

  // Wiring constant
  sdiv11.io.RightIO.bits.data := 2.U
  sdiv11.io.RightIO.bits.predicate := true.B
  sdiv11.io.RightIO.valid := true.B

  store12.io.inData <> sdiv11.io.Out(param.store12_in("sdiv11"))



  // Wiring Store instruction to the parent instruction
  store12.io.GepAddr <> alloca0.io.Out(param.store12_in("alloca0"))
  store12.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store12.io.memReq
  store12.io.Out(0).ready := true.B



  // Wiring Load instruction to another instruction
  load13.io.GepAddr <> alloca0.io.Out(param.load13_in("alloca0"))
  load13.io.memResp <> CacheMem.io.ReadOut(1)  // Manually added
  CacheMem.io.ReadIn(1) <> load13.io.memReq    // Manually added
  load13.io.PredOp(0) <> store12.io.SuccOp(0)



  // Wiring return instruction
  ret14.io.In.data("field0") <> load13.io.Out(param.ret14_in("load13"))
  io.out <> ret14.io.Out


  callout15.io.In("field0") <> lb_L_0.io.liveIn(1) // manual %n
  callout15.io.In("field1") <> lb_L_0.io.liveIn(0) // manual %a
  callout15.io.In("field2") <> lb_L_0.io.liveIn(2) // manual %alloc
  io.call15_out <> callout15.io.Out(0)
  callin15.io.Out.enable.ready := true.B

  callin15.io.In <> io.call15_in

  // Reattach (Manual add)
  reattach16.io.predicateIn(0) <> callin15.io.Out.data("field0")

  // Sync (Manual add)
  sync9.io.incIn(0) <> detach6.io.Out(2)
  sync9.io.decIn(0) <> reattach16.io.Out(0)


}

import java.io.{File, FileWriter}
object cilk_for_test12Main extends App {
  val dir = new File("RTL/cilk_for_test12") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test12DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


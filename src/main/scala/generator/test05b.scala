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

object Data_test05b_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_for_cond_pred = Map(
    "br0" -> 0,
    "br7" -> 1
  )


  val bb_for_inc_pred = Map(
    "br5" -> 0
  )


  val bb_for_body_pred = Map(
    "br3" -> 0
  )


  val bb_for_end_pred = Map(
    "br3" -> 0
  )


  val br0_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val br3_brn_bb = Map(
    "bb_for_body" -> 0,
    "bb_for_end" -> 1
  )


  val br5_brn_bb = Map(
    "bb_for_inc" -> 0
  )


  val br7_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_for_cond_activate = Map(
    "phi1" -> 0,
    "icmp2" -> 1,
    "br3" -> 2
  )


  val bb_for_body_activate = Map(
    "call4" -> 0,
    "br5" -> 1
  )


  val bb_for_inc_activate = Map(
    "add6" -> 0,
    "br7" -> 1
  )


  val bb_for_end_activate = Map(
    "sub8" -> 0,
    "getelementptr9" -> 1,
    "load10" -> 2,
    "add11" -> 3,
    "store12" -> 4,
    "sub13" -> 5,
    "getelementptr14" -> 6,
    "load15" -> 7,
    "ret16" -> 8
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add6" -> 1
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !5, !ScalaLabel !6
  val phi1_in = Map(
    "add6" -> 0
  )


  //  %cmp = icmp ult i32 %i.0, %n, !UID !7, !ScalaLabel !8
  val icmp2_in = Map(
    "phi1" -> 0,
    "field1" -> 0
  )


  //  br i1 %cmp, label %for.body, label %for.end, !UID !9, !BB_UID !10, !ScalaLabel !11
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  %call = call i32 @test05(i32* %a, i32 %n), !UID !12, !ScalaLabel !13
  val call4_in = Map(
    "field0" -> 0,
    "field1" -> 1,
    "" -> 0
  )


  //  %inc = add i32 %i.0, 1, !UID !17, !ScalaLabel !18
  val add6_in = Map(
    "phi1" -> 1
  )


  //  %sub = sub i32 %n, 1, !UID !22, !ScalaLabel !23
  val sub8_in = Map(
    "field1" -> 2
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %sub, !UID !24, !ScalaLabel !25
  val getelementptr9_in = Map(
    "field0" -> 1,
    "sub8" -> 0
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !26, !ScalaLabel !27
  val load10_in = Map(
    "getelementptr9" -> 0
  )


  //  %inc1 = add i32 %0, 1, !UID !28, !ScalaLabel !29
  val add11_in = Map(
    "load10" -> 0
  )


  //  store i32 %inc1, i32* %arrayidx, align 4, !UID !30, !ScalaLabel !31
  val store12_in = Map(
    "add11" -> 0,
    "getelementptr9" -> 1
  )


  //  %sub2 = sub i32 %n, 1, !UID !32, !ScalaLabel !33
  val sub13_in = Map(
    "field1" -> 3
  )


  //  %arrayidx3 = getelementptr inbounds i32, i32* %a, i32 %sub2, !UID !34, !ScalaLabel !35
  val getelementptr14_in = Map(
    "field0" -> 2,
    "sub13" -> 0
  )


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !36, !ScalaLabel !37
  val load15_in = Map(
    "getelementptr14" -> 0
  )


  //  ret i32 %1, !UID !38, !BB_UID !39, !ScalaLabel !40
  val ret16_in = Map(
    "load15" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test05bDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call4_out = Decoupled(new Call(List(32,32)))
    val call4_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class test05bDF(implicit p: Parameters) extends test05bDFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


  val StackPointer = Module(new Stack(NumOps = 1))

  val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=1)
                (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
                (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2)))

  val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=2,NWrites=1)
                (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
                (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
                (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32)))
  InputSplitter.io.In <> io.in

  val field0_expand = Module(new ExpandNode(NumOuts=3,ID=100)(new DataBundle))
  field0_expand.io.enable.valid := true.B
  field0_expand.io.enable.bits.control := true.B
  field0_expand.io.InData <> InputSplitter.io.Out.data("field0")


  val field1_expand = Module(new ExpandNode(NumOuts=3,ID=101)(new DataBundle))
  field1_expand.io.enable.valid := true.B
  field1_expand.io.enable.bits.control := true.B
  field1_expand.io.InData <> InputSplitter.io.Out.data("field1")




  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

/*
  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 2, ID = 0))
*/

  val lb_L_0 = Module(new LoopBlock(ID=999,NumIns=2,NumOuts=1,NumExits=1));





  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 2))

  val bb_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 4))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %for.cond, !UID !2, !BB_UID !3, !ScalaLabel !4
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  for.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !5, !ScalaLabel !6
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))


  //  %cmp = icmp ult i32 %i.0, %n, !UID !7, !ScalaLabel !8
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %for.body, label %for.end, !UID !9, !BB_UID !10, !ScalaLabel !11
  val br3 = Module (new CBranchNode(ID = 3))

  // [BasicBlock]  for.body:

  //  %call = call i32 @test05(i32* %a, i32 %n), !UID !12, !ScalaLabel !13
  val callout4 = Module(new CallOutNode(ID=4,argTypes=List(32,32)))
  val callin4 = Module(new CallInNode(ID=499,argTypes=List(32)))


  //  br label %for.inc, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br5 = Module (new UBranchNode(ID = 5, NumPredOps = 1))

  // [BasicBlock]  for.inc:

  //  %inc = add i32 %i.0, 1, !UID !17, !ScalaLabel !18
  val add6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))


  //  br label %for.cond, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br7 = Module (new UBranchNode(ID = 7, NumOuts=2)) // Manually changed NumOuts=2

  // [BasicBlock]  for.end:

  //  %sub = sub i32 %n, 1, !UID !22, !ScalaLabel !23
  val sub8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "sub")(sign=false))


  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %sub, !UID !24, !ScalaLabel !25
  val getelementptr9 = Module (new GepOneNode(NumOuts = 2, ID = 9)(numByte1 = 4))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !26, !ScalaLabel !27
  val load10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=10,RouteID=0))


  //  %inc1 = add i32 %0, 1, !UID !28, !ScalaLabel !29
  val add11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign=false))


  //  store i32 %inc1, i32* %arrayidx, align 4, !UID !30, !ScalaLabel !31
  val store12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=12,RouteID=0))


  //  %sub2 = sub i32 %n, 1, !UID !32, !ScalaLabel !33
  val sub13 = Module (new ComputeNode(NumOuts = 1, ID = 13, opCode = "sub")(sign=false))


  //  %arrayidx3 = getelementptr inbounds i32, i32* %a, i32 %sub2, !UID !34, !ScalaLabel !35
  val getelementptr14 = Module (new GepOneNode(NumOuts = 1, ID = 14)(numByte1 = 4))


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !36, !ScalaLabel !37
  val load15 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=15,RouteID=1))


  //  ret i32 %1, !UID !38, !BB_UID !39, !ScalaLabel !40
  val ret16 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=16))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test05b_FlowParam



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

  //Connecting br0 to bb_for_cond
  lb_L_0.io.enable <> br0.io.Out(param.br0_brn_bb("bb_for_cond"))  // manually added

  bb_for_cond.io.activate <> lb_L_0.io.activate // manually corrected


  //Connecting br3 to bb_for_body
  bb_for_body.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_for_body"))


  //Connecting br3 to bb_for_end
  //bb_for_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_for_end"))
  lb_L_0.io.loopExit(0) <> br3.io.Out(param.br3_brn_bb("bb_for_end")) // Manual
  bb_for_end.io.predicateIn <> lb_L_0.io.endEnable

  // Tie off live outs (Manual)
  lb_L_0.io.liveOut(0).valid := true.B
  lb_L_0.io.Out(0).ready := true.B

  //Connecting br5 to bb_for_inc
  bb_for_inc.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_for_inc"))


  //Connecting br7 to bb_for_cond
//  bb_for_cond.io.predicateIn(param.bb_for_cond_pred("br7")) <> br7.io.Out(param.br7_brn_bb("bb_for_cond"))
  bb_for_cond.io.loopBack <> br7.io.Out(0)
  lb_L_0.io.latchEnable   <> br7.io.Out(1) // manual



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  phi1.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("phi1"))

  icmp2.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("icmp2"))

  br3.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("br3"))



  callout4.io.In.enable <> bb_for_body.io.Out(param.bb_for_body_activate("call4"))
  callin4.io.enable <> bb_for_body.io.Out(2) // Manual

  br5.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("br5"))
  br5.io.PredOp(0) <> callin4.io.Out.enable


  add6.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("add6"))

  br7.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("br7"))



  sub8.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("sub8"))

  getelementptr9.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("getelementptr9"))

  load10.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("load10"))

  add11.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("add11"))

  store12.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("store12"))

  sub13.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("sub13"))

  getelementptr14.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("getelementptr14"))

  load15.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("load15"))

  ret16.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("ret16"))

  /*
  loop_L_0_liveIN_0.io.enable <> bb_for_end.io.Out(9)
  loop_L_0_liveIN_1.io.enable <> bb_for_end.io.Out(10)
*/





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %a
  lb_L_0.io.In(0) <> field0_expand.io.Out(0) // manual

  // Connecting function argument to the loop header
  //i32 %n
  lb_L_0.io.In(1) <> field1_expand.io.Out(0) // manual



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

  phi1.io.InData(param.phi1_phi_in("add6")) <> add6.io.Out(param.phi1_in("add6"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_for_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp2.io.LeftIO <> phi1.io.Out(param.icmp2_in("phi1"))

  // Wiring Binary instruction to the loop header
  icmp2.io.RightIO <> lb_L_0.io.liveIn(1) // manual

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring Call to I/O
  callout4.io.In.data("field0") <> lb_L_0.io.liveIn(0) // manual
  callout4.io.In.data("field1") <> lb_L_0.io.liveIn(1) // manual
  io.call4_out <> callout4.io.Out

  callin4.io.In <> io.call4_in
//  callin4.io.Out.enable.ready := true.B // Manual fix
  callin4.io.Out.data("field0").ready := true.B // Manual fix


  // Wiring instructions
  add6.io.LeftIO <> phi1.io.Out(param.add6_in("phi1"))

  // Wiring constant
  add6.io.RightIO.bits.data := 1.U
  add6.io.RightIO.bits.predicate := true.B
  add6.io.RightIO.valid := true.B

  // Wiring Binary instruction to the function argument
  sub8.io.LeftIO <> field1_expand.io.Out(1)

  // Wiring constant
  sub8.io.RightIO.bits.data := 1.U
  sub8.io.RightIO.bits.predicate := true.B
  sub8.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr9.io.baseAddress <> field0_expand.io.Out(1)

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



  // Wiring Binary instruction to the function argument
  sub13.io.LeftIO <> field1_expand.io.Out(2)

  // Wiring constant
  sub13.io.RightIO.bits.data := 1.U
  sub13.io.RightIO.bits.predicate := true.B
  sub13.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr14.io.baseAddress <> field0_expand.io.Out(2)

  // Wiring GEP instruction to the parent instruction
  getelementptr14.io.idx1 <> sub13.io.Out(param.getelementptr14_in("sub13"))


  // Wiring Load instruction to the parent instruction
  load15.io.GepAddr <> getelementptr14.io.Out(param.load15_in("getelementptr14"))
  load15.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load15.io.memReq
  load15.io.PredOp(0) <> store12.io.SuccOp(0) // added manually




  // Wiring return instruction
  ret16.io.predicateIn(0).bits.control := true.B
  ret16.io.predicateIn(0).bits.taskID := 0.U
  ret16.io.predicateIn(0).valid := true.B
  ret16.io.In.data("field0") <> load15.io.Out(param.ret16_in("load15"))
  io.out <> ret16.io.Out


}

import java.io.{File, FileWriter}
object test05bMain extends App {
  val dir = new File("RTL/test05b") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test05bDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


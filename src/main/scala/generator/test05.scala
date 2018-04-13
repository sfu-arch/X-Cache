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

object Data_test05_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_for_cond_pred = Map(
    "br0" -> 0,
    "br11" -> 1
  )


  val bb_for_inc_pred = Map(
    "br9" -> 0
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


  val br9_brn_bb = Map(
    "bb_for_inc" -> 0
  )


  val br11_brn_bb = Map(
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
    "getelementptr4" -> 0,
    "load5" -> 1,
    "mul6" -> 2,
    "getelementptr7" -> 3,
    "store8" -> 4,
    "br9" -> 5
  )


  val bb_for_inc_activate = Map(
    "add10" -> 0,
    "br11" -> 1
  )


  val bb_for_end_activate = Map(
    "sub12" -> 0,
    "getelementptr13" -> 1,
    "load14" -> 2,
    "add15" -> 3,
    "store16" -> 4,
    "sub17" -> 5,
    "getelementptr18" -> 6,
    "load19" -> 7,
    "ret20" -> 8
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add10" -> 1
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !5, !ScalaLabel !6
  val phi1_in = Map(
    "add10" -> 0
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


  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !12, !ScalaLabel !13
  val getelementptr4_in = Map(
    "field0" -> 0,
    "phi1" -> 1
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !14, !ScalaLabel !15
  val load5_in = Map(
    "getelementptr4" -> 0
  )


  //  %mul = mul i32 2, %0, !UID !16, !ScalaLabel !17
  val mul6_in = Map(
    "load5" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !18, !ScalaLabel !19
  val getelementptr7_in = Map(
    "field0" -> 1,
    "phi1" -> 2
  )


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !20, !ScalaLabel !21
  val store8_in = Map(
    "mul6" -> 0,
    "getelementptr7" -> 0
  )


  //  %inc = add i32 %i.0, 1, !UID !25, !ScalaLabel !26
  val add10_in = Map(
    "phi1" -> 3
  )


  //  %sub = sub i32 %n, 1, !UID !30, !ScalaLabel !31
  val sub12_in = Map(
    "field1" -> 1
  )


  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i32 %sub, !UID !32, !ScalaLabel !33
  val getelementptr13_in = Map(
    "field0" -> 2,
    "sub12" -> 0
  )


  //  %1 = load i32, i32* %arrayidx2, align 4, !UID !34, !ScalaLabel !35
  val load14_in = Map(
    "getelementptr13" -> 0
  )


  //  %inc3 = add i32 %1, 1, !UID !36, !ScalaLabel !37
  val add15_in = Map(
    "load14" -> 0
  )


  //  store i32 %inc3, i32* %arrayidx2, align 4, !UID !38, !ScalaLabel !39
  val store16_in = Map(
    "add15" -> 0,
    "getelementptr13" -> 1
  )


  //  %sub4 = sub i32 %n, 1, !UID !40, !ScalaLabel !41
  val sub17_in = Map(
    "field1" -> 2
  )


  //  %arrayidx5 = getelementptr inbounds i32, i32* %a, i32 %sub4, !UID !42, !ScalaLabel !43
  val getelementptr18_in = Map(
    "field0" -> 3,
    "sub17" -> 0
  )


  //  %2 = load i32, i32* %arrayidx5, align 4, !UID !44, !ScalaLabel !45
  val load19_in = Map(
    "getelementptr18" -> 0
  )


  //  ret i32 %2, !UID !46, !BB_UID !47, !ScalaLabel !48
  val ret20_in = Map(
    "load19" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test05DFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class test05DF(implicit p: Parameters) extends test05DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


  val StackPointer = Module(new Stack(NumOps = 1))

  val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=3,NWrites=2)
                (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
                (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2)))

  val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=3,NWrites=2)
                (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
                (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
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
  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 2, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
*/
  val lb_L_0 = Module(new LoopBlock(ID=999,NumIns=2,NumOuts=0,NumExits=1));


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 2))

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
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 4, ID = 1))


  //  %cmp = icmp ult i32 %i.0, %n, !UID !7, !ScalaLabel !8
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %for.body, label %for.end, !UID !9, !BB_UID !10, !ScalaLabel !11
  val br3 = Module (new CBranchNode(ID = 3))

  // [BasicBlock]  for.body:

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !12, !ScalaLabel !13
  val getelementptr4 = Module (new GepOneNode(NumOuts = 1, ID = 4)(numByte1 = 4))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !14, !ScalaLabel !15
  val load5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=5,RouteID=0))


  //  %mul = mul i32 2, %0, !UID !16, !ScalaLabel !17
  val mul6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign=false))


  //  %arrayidx1 = getelementptr inbounds i32, i32* %a, i32 %i.0, !UID !18, !ScalaLabel !19
  val getelementptr7 = Module (new GepOneNode(NumOuts = 1, ID = 7)(numByte1 = 4))


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !20, !ScalaLabel !21
  val store8 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=8,RouteID=0))


  //  br label %for.inc, !UID !22, !BB_UID !23, !ScalaLabel !24
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  for.inc:

  //  %inc = add i32 %i.0, 1, !UID !25, !ScalaLabel !26
  val add10 = Module (new ComputeNode(NumOuts = 1, ID = 10, opCode = "add")(sign=false))


  //  br label %for.cond, !UID !27, !BB_UID !28, !ScalaLabel !29
  val br11 = Module (new UBranchNode(ID = 11,NumOuts=2)) // manually added NumOuts=2

  // [BasicBlock]  for.end:

  //  %sub = sub i32 %n, 1, !UID !30, !ScalaLabel !31
  val sub12 = Module (new ComputeNode(NumOuts = 1, ID = 12, opCode = "sub")(sign=false))


  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i32 %sub, !UID !32, !ScalaLabel !33
  val getelementptr13 = Module (new GepOneNode(NumOuts = 2, ID = 13)(numByte1 = 4))


  //  %1 = load i32, i32* %arrayidx2, align 4, !UID !34, !ScalaLabel !35
  val load14 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=14,RouteID=1))


  //  %inc3 = add i32 %1, 1, !UID !36, !ScalaLabel !37
  val add15 = Module (new ComputeNode(NumOuts = 1, ID = 15, opCode = "add")(sign=false))


  //  store i32 %inc3, i32* %arrayidx2, align 4, !UID !38, !ScalaLabel !39
  val store16 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=16,RouteID=1))


  //  %sub4 = sub i32 %n, 1, !UID !40, !ScalaLabel !41
  val sub17 = Module (new ComputeNode(NumOuts = 1, ID = 17, opCode = "sub")(sign=false))


  //  %arrayidx5 = getelementptr inbounds i32, i32* %a, i32 %sub4, !UID !42, !ScalaLabel !43
  val getelementptr18 = Module (new GepOneNode(NumOuts = 1, ID = 18)(numByte1 = 4))


  //  %2 = load i32, i32* %arrayidx5, align 4, !UID !44, !ScalaLabel !45
  val load19 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=19,RouteID=2))


  //  ret i32 %2, !UID !46, !BB_UID !47, !ScalaLabel !48
  val ret20 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=20))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test05_FlowParam



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
//  bb_for_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_for_end"))
  lb_L_0.io.loopExit(0) <> br3.io.Out(param.br3_brn_bb("bb_for_end"))
  bb_for_end.io.predicateIn <> lb_L_0.io.endEnable

  // Tie off live outs
  //lb_L_0.io.liveOut(0).valid := true.B
  //lb_L_0.io.Out(0).ready := true.B

  //Connecting br9 to bb_for_inc
  bb_for_inc.io.predicateIn <> br9.io.Out(param.br9_brn_bb("bb_for_inc"))


  //Connecting br11 to bb_for_cond
  bb_for_cond.io.loopBack <> br11.io.Out(0)
  lb_L_0.io.latchEnable   <> br11.io.Out(1)


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



  getelementptr4.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("getelementptr4"))

  load5.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("load5"))

  mul6.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("mul6"))

  getelementptr7.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("getelementptr7"))

  store8.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("store8"))

  br9.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("br9"))



  add10.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("add10"))

  br11.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("br11"))



  sub12.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("sub12"))

  getelementptr13.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("getelementptr13"))

  load14.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("load14"))

  add15.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("add15"))

  store16.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("store16"))

  sub17.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("sub17"))

  getelementptr18.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("getelementptr18"))

  load19.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("load19"))

  ret20.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("ret20"))

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

  phi1.io.InData(param.phi1_phi_in("add10")) <> add10.io.Out(param.phi1_in("add10"))

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

  // Wiring GEP instruction to the loop header
  getelementptr4.io.baseAddress <> lb_L_0.io.liveIn(0) // manual

  // Wiring GEP instruction to the parent instruction
  getelementptr4.io.idx1 <> phi1.io.Out(param.getelementptr4_in("phi1"))


  // Wiring Load instruction to the parent instruction
  load5.io.GepAddr <> getelementptr4.io.Out(param.load5_in("getelementptr4"))
  load5.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load5.io.memReq




  // Wiring constant
  mul6.io.LeftIO.bits.data := 2.U
  mul6.io.LeftIO.bits.predicate := true.B
  mul6.io.LeftIO.valid := true.B

  // Wiring instructions
  mul6.io.RightIO <> load5.io.Out(param.mul6_in("load5"))

  // Wiring GEP instruction to the loop header
  getelementptr7.io.baseAddress <> lb_L_0.io.liveIn(0) // manual

  // Wiring GEP instruction to the parent instruction
  getelementptr7.io.idx1 <> phi1.io.Out(param.getelementptr7_in("phi1"))


  store8.io.inData <> mul6.io.Out(param.store8_in("mul6"))



  // Wiring Store instruction to the parent instruction
  store8.io.GepAddr <> getelementptr7.io.Out(param.store8_in("getelementptr7"))
  store8.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store8.io.memReq
  store8.io.Out(0).ready := true.B



  // Wiring instructions
  add10.io.LeftIO <> phi1.io.Out(param.add10_in("phi1"))

  // Wiring constant
  add10.io.RightIO.bits.data := 1.U
  add10.io.RightIO.bits.predicate := true.B
  add10.io.RightIO.valid := true.B

  // Wiring Binary instruction to the function argument
  sub12.io.LeftIO <> field1_expand.io.Out(1)

  // Wiring constant
  sub12.io.RightIO.bits.data := 1.U
  sub12.io.RightIO.bits.predicate := true.B
  sub12.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr13.io.baseAddress <> field0_expand.io.Out(1)

  // Wiring GEP instruction to the parent instruction
  getelementptr13.io.idx1 <> sub12.io.Out(param.getelementptr13_in("sub12"))


  // Wiring Load instruction to the parent instruction
  load14.io.GepAddr <> getelementptr13.io.Out(param.load14_in("getelementptr13"))
  load14.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load14.io.memReq




  // Wiring instructions
  add15.io.LeftIO <> load14.io.Out(param.add15_in("load14"))

  // Wiring constant
  add15.io.RightIO.bits.data := 1.U
  add15.io.RightIO.bits.predicate := true.B
  add15.io.RightIO.valid := true.B

  store16.io.inData <> add15.io.Out(param.store16_in("add15"))



  // Wiring Store instruction to the parent instruction
  store16.io.GepAddr <> getelementptr13.io.Out(param.store16_in("getelementptr13"))
  store16.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store16.io.memReq
  store16.io.Out(0).ready := true.B



  // Wiring Binary instruction to the function argument
  sub17.io.LeftIO <> field1_expand.io.Out(2)

  // Wiring constant
  sub17.io.RightIO.bits.data := 1.U
  sub17.io.RightIO.bits.predicate := true.B
  sub17.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr18.io.baseAddress <> field0_expand.io.Out(2)

  // Wiring GEP instruction to the parent instruction
  getelementptr18.io.idx1 <> sub17.io.Out(param.getelementptr18_in("sub17"))


  // Wiring Load instruction to the parent instruction
  load19.io.GepAddr <> getelementptr18.io.Out(param.load19_in("getelementptr18"))
  load19.io.memResp <> CacheMem.io.ReadOut(2)
  CacheMem.io.ReadIn(2) <> load19.io.memReq
  load19.io.PredOp(0) <> store16.io.SuccOp(0) // added manually



  // Wiring return instruction
  ret20.io.predicateIn(0).bits.control := true.B
  ret20.io.predicateIn(0).bits.taskID := 0.U
  ret20.io.predicateIn(0).valid := true.B
  ret20.io.In.data("field0") <> load19.io.Out(param.ret20_in("load19"))
  io.out <> ret20.io.Out


}

import java.io.{File, FileWriter}
object test05Main extends App {
  val dir = new File("RTL/test05") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test05DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


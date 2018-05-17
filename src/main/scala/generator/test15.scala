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

object Data_test15_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_for_cond_pred = Map(
    "br0" -> 0,
    "br37" -> 1
  )


  val bb_for_inc16_pred = Map(
    "br35" -> 0
  )


  val bb_for_body_pred = Map(
    "br4" -> 0
  )


  val bb_for_end18_pred = Map(
    "br4" -> 0
  )


  val bb_for_cond1_pred = Map(
    "br5" -> 0,
    "br28" -> 1
  )


  val bb_for_inc10_pred = Map(
    "br26" -> 0
  )


  val bb_for_body3_pred = Map(
    "br8" -> 0
  )


  val bb_for_end12_pred = Map(
    "br8" -> 0
  )


  val bb_for_cond4_pred = Map(
    "br9" -> 0,
    "br20" -> 1
  )


  val bb_for_inc_pred = Map(
    "br18" -> 0
  )


  val bb_for_body6_pred = Map(
    "br12" -> 0
  )


  val bb_for_end_pred = Map(
    "br12" -> 0
  )


  val br0_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val br4_brn_bb = Map(
    "bb_for_body" -> 0,
    "bb_for_end18" -> 1
  )


  val br5_brn_bb = Map(
    "bb_for_cond1" -> 0
  )


  val br8_brn_bb = Map(
    "bb_for_body3" -> 0,
    "bb_for_end12" -> 1
  )


  val br9_brn_bb = Map(
    "bb_for_cond4" -> 0
  )


  val br12_brn_bb = Map(
    "bb_for_body6" -> 0,
    "bb_for_end" -> 1
  )


  val br18_brn_bb = Map(
    "bb_for_inc" -> 0
  )


  val br20_brn_bb = Map(
    "bb_for_cond4" -> 0
  )


  val br26_brn_bb = Map(
    "bb_for_inc10" -> 0
  )


  val br28_brn_bb = Map(
    "bb_for_cond1" -> 0
  )


  val br35_brn_bb = Map(
    "bb_for_inc16" -> 0
  )


  val br37_brn_bb = Map(
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
    "icmp7" -> 1,
    "br8" -> 2
  )


  val bb_for_body3_activate = Map(
    "br9" -> 0
  )


  val bb_for_cond4_activate = Map(
    "phi10" -> 0,
    "icmp11" -> 1,
    "br12" -> 2
  )


  val bb_for_body6_activate = Map(
    "getelementptr13" -> 0,
    "load14" -> 1,
    "mul15" -> 2,
    "getelementptr16" -> 3,
    "store17" -> 4,
    "br18" -> 5
  )


  val bb_for_inc_activate = Map(
    "add19" -> 0,
    "br20" -> 1
  )


  val bb_for_end_activate = Map(
    "sub21" -> 0,
    "getelementptr22" -> 1,
    "load23" -> 2,
    "add24" -> 3,
    "store25" -> 4,
    "br26" -> 5
  )


  val bb_for_inc10_activate = Map(
    "add27" -> 0,
    "br28" -> 1
  )


  val bb_for_end12_activate = Map(
    "sub29" -> 0,
    "getelementptr30" -> 1,
    "load31" -> 2,
    "add32" -> 3,
    "store33" -> 4,
    "add34" -> 5,
    "br35" -> 6
  )


  val bb_for_inc16_activate = Map(
    "add36" -> 0,
    "br37" -> 1
  )


  val bb_for_end18_activate = Map(
    "sdiv38" -> 0,
    "ret39" -> 1
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add36" -> 1
  )


  val phi2_phi_in = Map(
    "const_0" -> 0,
    "add34" -> 1
  )


  val phi6_phi_in = Map(
    "const_0" -> 0,
    "add27" -> 1
  )


  val phi10_phi_in = Map(
    "const_0" -> 0,
    "add19" -> 1
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc17, %for.inc16 ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add36" -> 0
  )


  //  %result.0 = phi i32 [ 0, %entry ], [ %add, %for.inc16 ], !UID !12, !ScalaLabel !13
  val phi2_in = Map(
    "add34" -> 0
  )


  //  %cmp = icmp ult i32 %i.0, 3, !UID !14, !ScalaLabel !15
  val icmp3_in = Map(
    "phi1" -> 0
  )


  //  br i1 %cmp, label %for.body, label %for.end18, !UID !16, !BB_UID !17, !ScalaLabel !18
  val br4_in = Map(
    "icmp3" -> 0
  )


  //  %j.0 = phi i32 [ 0, %for.body ], [ %inc11, %for.inc10 ], !UID !22, !ScalaLabel !23
  val phi6_in = Map(
    "add27" -> 0
  )


  //  %cmp2 = icmp ult i32 %j.0, %n, !UID !24, !ScalaLabel !25
  val icmp7_in = Map(
    "phi6" -> 0,
    "field1" -> 0
  )


  //  br i1 %cmp2, label %for.body3, label %for.end12, !UID !26, !BB_UID !27, !ScalaLabel !28
  val br8_in = Map(
    "icmp7" -> 0
  )


  //  %k.0 = phi i32 [ 0, %for.body3 ], [ %inc, %for.inc ], !UID !32, !ScalaLabel !33
  val phi10_in = Map(
    "add19" -> 0
  )


  //  %cmp5 = icmp ult i32 %k.0, %n, !UID !34, !ScalaLabel !35
  val icmp11_in = Map(
    "phi10" -> 0,
    "field1" -> 1
  )


  //  br i1 %cmp5, label %for.body6, label %for.end, !UID !36, !BB_UID !37, !ScalaLabel !38
  val br12_in = Map(
    "icmp11" -> 0
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %k.0, !UID !39, !ScalaLabel !40
  val getelementptr13_in = Map(
    "field0" -> 0,
    "phi10" -> 1
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !41, !ScalaLabel !42
  val load14_in = Map(
    "getelementptr13" -> 0
  )


  //  %mul = mul i32 2, %0, !UID !43, !ScalaLabel !44
  val mul15_in = Map(
    "load14" -> 0
  )


  //  %arrayidx7 = getelementptr inbounds i32, i32* %a, i32 %k.0, !UID !45, !ScalaLabel !46
  val getelementptr16_in = Map(
    "field0" -> 1,
    "phi10" -> 2
  )


  //  store i32 %mul, i32* %arrayidx7, align 4, !UID !47, !ScalaLabel !48
  val store17_in = Map(
    "mul15" -> 0,
    "getelementptr16" -> 0
  )


  //  %inc = add i32 %k.0, 1, !UID !52, !ScalaLabel !53
  val add19_in = Map(
    "phi10" -> 3
  )


  //  %sub = sub i32 %n, 1, !UID !72, !ScalaLabel !73
  val sub21_in = Map(
    "field1" -> 2
  )


  //  %arrayidx8 = getelementptr inbounds i32, i32* %a, i32 %sub, !UID !74, !ScalaLabel !75
  val getelementptr22_in = Map(
    "field0" -> 2,
    "sub21" -> 0
  )


  //  %1 = load i32, i32* %arrayidx8, align 4, !UID !76, !ScalaLabel !77
  val load23_in = Map(
    "getelementptr22" -> 0
  )


  //  %inc9 = add i32 %1, 1, !UID !78, !ScalaLabel !79
  val add24_in = Map(
    "load23" -> 0
  )


  //  store i32 %inc9, i32* %arrayidx8, align 4, !UID !80, !ScalaLabel !81
  val store25_in = Map(
    "add24" -> 0,
    "getelementptr22" -> 1
  )


  //  %inc11 = add i32 %j.0, 1, !UID !85, !ScalaLabel !86
  val add27_in = Map(
    "phi6" -> 1
  )


  //  %sub13 = sub i32 %n, 1, !UID !93, !ScalaLabel !94
  val sub29_in = Map(
    "field1" -> 3
  )


  //  %arrayidx14 = getelementptr inbounds i32, i32* %a, i32 %sub13, !UID !95, !ScalaLabel !96
  val getelementptr30_in = Map(
    "field0" -> 3,
    "sub29" -> 0
  )


  //  %2 = load i32, i32* %arrayidx14, align 4, !UID !97, !ScalaLabel !98
  val load31_in = Map(
    "getelementptr30" -> 0
  )


  //  %inc15 = add i32 %2, 1, !UID !99, !ScalaLabel !100
  val add32_in = Map(
    "load31" -> 0
  )


  //  store i32 %inc15, i32* %arrayidx14, align 4, !UID !101, !ScalaLabel !102
  val store33_in = Map(
    "add32" -> 0,
    "getelementptr30" -> 1
  )


  //  %add = add i32 %result.0, %2, !UID !103, !ScalaLabel !104
  val add34_in = Map(
    "phi2" -> 0,
    "load31" -> 1
  )


  //  %inc17 = add i32 %i.0, 1, !UID !108, !ScalaLabel !109
  val add36_in = Map(
    "phi1" -> 1
  )


  //  %div = sdiv i32 %result.0, 2, !UID !116, !ScalaLabel !117
  val sdiv38_in = Map(
    "phi2" -> 1
  )


  //  ret i32 %div, !UID !118, !BB_UID !119, !ScalaLabel !120
  val ret39_in = Map(
    "sdiv38" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test15DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class test15DF(implicit p: Parameters) extends test15DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


  val StackPointer = Module(new Stack(NumOps = 1))

  val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=3,NWrites=3)
                (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
                (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2)))

  val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=3,NWrites=3)
                (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
                (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
                (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val lb_L_0 = Module(new LoopBlock(ID=999,NumIns=List(2,1),NumOuts=0,NumExits=1)) //@todo Fix NumExits
  val lb_L_1 = Module(new LoopBlock(ID=999,NumIns=List(2,3),NumOuts=0,NumExits=1)) //@todo Fix NumExits
  val lb_L_2 = Module(new LoopBlock(ID=999,NumIns=List(2,2),NumOuts=1,NumExits=1)) //@todo Fix NumExits


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond = Module(new LoopHead(NumOuts = 4, NumPhi = 2, BID = 1))

  val bb_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond1 = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 3))

  val bb_for_body3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_for_cond4 = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 5))

  val bb_for_body6 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 6))

  val bb_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 7))

  val bb_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 8))

  val bb_for_inc10 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 9))

  val bb_for_end12 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 10))

  val bb_for_inc16 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 11))

  val bb_for_end18 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 12))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %for.cond, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchFastNode(ID = 0))

  // [BasicBlock]  for.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc17, %for.inc16 ], !UID !10, !ScalaLabel !11
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))


  //  %result.0 = phi i32 [ 0, %entry ], [ %add, %for.inc16 ], !UID !12, !ScalaLabel !13
  val phi2 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 2))


  //  %cmp = icmp ult i32 %i.0, 3, !UID !14, !ScalaLabel !15
  val icmp3 = Module (new IcmpNode(NumOuts = 1, ID = 3, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %for.body, label %for.end18, !UID !16, !BB_UID !17, !ScalaLabel !18
  val br4 = Module (new CBranchFastNode(ID = 4))

  // [BasicBlock]  for.body:

  //  br label %for.cond1, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br5 = Module (new UBranchFastNode(ID = 5))

  // [BasicBlock]  for.cond1:

  //  %j.0 = phi i32 [ 0, %for.body ], [ %inc11, %for.inc10 ], !UID !22, !ScalaLabel !23
  val phi6 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 6))


  //  %cmp2 = icmp ult i32 %j.0, %n, !UID !24, !ScalaLabel !25
  val icmp7 = Module (new IcmpNode(NumOuts = 1, ID = 7, opCode = "ULT")(sign=false))


  //  br i1 %cmp2, label %for.body3, label %for.end12, !UID !26, !BB_UID !27, !ScalaLabel !28
  val br8 = Module (new CBranchFastNode(ID = 8))

  // [BasicBlock]  for.body3:

  //  br label %for.cond4, !UID !29, !BB_UID !30, !ScalaLabel !31
  val br9 = Module (new UBranchFastNode(ID = 9))

  // [BasicBlock]  for.cond4:

  //  %k.0 = phi i32 [ 0, %for.body3 ], [ %inc, %for.inc ], !UID !32, !ScalaLabel !33
  val phi10 = Module (new PhiNode(NumInputs = 2, NumOuts = 4, ID = 10))


  //  %cmp5 = icmp ult i32 %k.0, %n, !UID !34, !ScalaLabel !35
  val icmp11 = Module (new IcmpNode(NumOuts = 1, ID = 11, opCode = "ULT")(sign=false))


  //  br i1 %cmp5, label %for.body6, label %for.end, !UID !36, !BB_UID !37, !ScalaLabel !38
  val br12 = Module (new CBranchFastNode(ID = 12))

  // [BasicBlock]  for.body6:

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %k.0, !UID !39, !ScalaLabel !40
  val getelementptr13 = Module (new GepOneNode(NumOuts = 1, ID = 13)(numByte1 = 4))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !41, !ScalaLabel !42
  val load14 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=14,RouteID=0))


  //  %mul = mul i32 2, %0, !UID !43, !ScalaLabel !44
  val mul15 = Module (new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign=false))


  //  %arrayidx7 = getelementptr inbounds i32, i32* %a, i32 %k.0, !UID !45, !ScalaLabel !46
  val getelementptr16 = Module (new GepOneNode(NumOuts = 1, ID = 16)(numByte1 = 4))


  //  store i32 %mul, i32* %arrayidx7, align 4, !UID !47, !ScalaLabel !48
  val store17 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=17,RouteID=0))


  //  br label %for.inc, !UID !49, !BB_UID !50, !ScalaLabel !51
  val br18 = Module (new UBranchFastNode(ID = 18))

  // [BasicBlock]  for.inc:

  //  %inc = add i32 %k.0, 1, !UID !52, !ScalaLabel !53
  val add19 = Module (new ComputeNode(NumOuts = 1, ID = 19, opCode = "add")(sign=false))


  //  br label %for.cond4, !llvm.loop !54, !UID !69, !BB_UID !70, !ScalaLabel !71
  val br20 = Module (new UBranchFastNode(ID = 20))

  // [BasicBlock]  for.end:

  //  %sub = sub i32 %n, 1, !UID !72, !ScalaLabel !73
  val sub21 = Module (new ComputeNode(NumOuts = 1, ID = 21, opCode = "sub")(sign=false))


  //  %arrayidx8 = getelementptr inbounds i32, i32* %a, i32 %sub, !UID !74, !ScalaLabel !75
  val getelementptr22 = Module (new GepOneNode(NumOuts = 2, ID = 22)(numByte1 = 4))


  //  %1 = load i32, i32* %arrayidx8, align 4, !UID !76, !ScalaLabel !77
  val load23 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=23,RouteID=1))


  //  %inc9 = add i32 %1, 1, !UID !78, !ScalaLabel !79
  val add24 = Module (new ComputeNode(NumOuts = 1, ID = 24, opCode = "add")(sign=false))


  //  store i32 %inc9, i32* %arrayidx8, align 4, !UID !80, !ScalaLabel !81
  val store25 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=25,RouteID=1))


  //  br label %for.inc10, !UID !82, !BB_UID !83, !ScalaLabel !84
  val br26 = Module (new UBranchFastNode(ID = 26))

  // [BasicBlock]  for.inc10:

  //  %inc11 = add i32 %j.0, 1, !UID !85, !ScalaLabel !86
  val add27 = Module (new ComputeNode(NumOuts = 1, ID = 27, opCode = "add")(sign=false))


  //  br label %for.cond1, !llvm.loop !87, !UID !90, !BB_UID !91, !ScalaLabel !92
  val br28 = Module (new UBranchFastNode(ID = 28))

  // [BasicBlock]  for.end12:

  //  %sub13 = sub i32 %n, 1, !UID !93, !ScalaLabel !94
  val sub29 = Module (new ComputeNode(NumOuts = 1, ID = 29, opCode = "sub")(sign=false))


  //  %arrayidx14 = getelementptr inbounds i32, i32* %a, i32 %sub13, !UID !95, !ScalaLabel !96
  val getelementptr30 = Module (new GepOneNode(NumOuts = 2, ID = 30)(numByte1 = 4))


  //  %2 = load i32, i32* %arrayidx14, align 4, !UID !97, !ScalaLabel !98
  val load31 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2,ID=31,RouteID=2))


  //  %inc15 = add i32 %2, 1, !UID !99, !ScalaLabel !100
  val add32 = Module (new ComputeNode(NumOuts = 1, ID = 32, opCode = "add")(sign=false))


  //  store i32 %inc15, i32* %arrayidx14, align 4, !UID !101, !ScalaLabel !102
  val store33 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=33,RouteID=2))


  //  %add = add i32 %result.0, %2, !UID !103, !ScalaLabel !104
  val add34 = Module (new ComputeNode(NumOuts = 1, ID = 34, opCode = "add")(sign=false))


  //  br label %for.inc16, !UID !105, !BB_UID !106, !ScalaLabel !107
  val br35 = Module (new UBranchFastNode(ID = 35))

  // [BasicBlock]  for.inc16:

  //  %inc17 = add i32 %i.0, 1, !UID !108, !ScalaLabel !109
  val add36 = Module (new ComputeNode(NumOuts = 1, ID = 36, opCode = "add")(sign=false))


  //  br label %for.cond, !llvm.loop !110, !UID !113, !BB_UID !114, !ScalaLabel !115
  val br37 = Module (new UBranchFastNode(ID = 37))

  // [BasicBlock]  for.end18:

  //  %div = sdiv i32 %result.0, 2, !UID !116, !ScalaLabel !117
  val sdiv38 = Module (new ComputeNode(NumOuts = 1, ID = 38, opCode = "udiv")(sign=false))


  //  ret i32 %div, !UID !118, !BB_UID !119, !ScalaLabel !120
  val ret39 = Module(new RetNode(retTypes=List(32), ID=39))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test15_FlowParam



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
  lb_L_2.io.enable <> br0.io.Out(param.br0_brn_bb("bb_for_cond"))
  bb_for_cond.io.activate <> lb_L_2.io.activate

  //Connecting br4 to bb_for_body
  bb_for_body.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_for_body"))


  //Connecting br4 to bb_for_end18
  lb_L_2.io.loopExit(0) <> br4.io.Out(param.br4_brn_bb("bb_for_end18"))
  bb_for_end18.io.predicateIn <> lb_L_2.io.endEnable

  //Connecting br5 to bb_for_cond1
  lb_L_1.io.enable <> br5.io.Out(param.br5_brn_bb("bb_for_cond1"))
  bb_for_cond1.io.activate <> lb_L_1.io.activate

  //Connecting br8 to bb_for_body3
  bb_for_body3.io.predicateIn <> br8.io.Out(param.br8_brn_bb("bb_for_body3"))


  //Connecting br8 to bb_for_end12
  lb_L_1.io.loopExit(0) <> br8.io.Out(param.br8_brn_bb("bb_for_end12"))
  bb_for_end12.io.predicateIn <> lb_L_1.io.endEnable

  //Connecting br9 to bb_for_cond4
  lb_L_0.io.enable <> br9.io.Out(param.br9_brn_bb("bb_for_cond4"))
  bb_for_cond4.io.activate <> lb_L_0.io.activate

  //Connecting br12 to bb_for_body6
  bb_for_body6.io.predicateIn <> br12.io.Out(param.br12_brn_bb("bb_for_body6"))


  //Connecting br12 to bb_for_end
  lb_L_0.io.loopExit(0) <> br12.io.Out(param.br12_brn_bb("bb_for_end"))
  bb_for_end.io.predicateIn <> lb_L_0.io.endEnable

  //Connecting br18 to bb_for_inc
  bb_for_inc.io.predicateIn <> br18.io.Out(param.br18_brn_bb("bb_for_inc"))


  //Connecting br20 to bb_for_cond4
  bb_for_cond4.io.loopBack <> br20.io.Out(0)
  lb_L_0.io.latchEnable <>  store17.io.SuccOp(0)// Manually connect to Call instruction


  //Connecting br26 to bb_for_inc10
  bb_for_inc10.io.predicateIn <> br26.io.Out(param.br26_brn_bb("bb_for_inc10"))


  //Connecting br28 to bb_for_cond1
  bb_for_cond1.io.loopBack <> br28.io.Out(0)
  lb_L_1.io.latchEnable <> store25.io.SuccOp(0) // Manually connect to Call instruction


  //Connecting br35 to bb_for_inc16
  bb_for_inc16.io.predicateIn <> br35.io.Out(param.br35_brn_bb("bb_for_inc16"))


  //Connecting br37 to bb_for_cond
  bb_for_cond.io.loopBack <> br37.io.Out(0)
  lb_L_2.io.latchEnable <>  store33.io.SuccOp(0) // Manually connect to Call instruction


  // There is no detach instruction



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  phi1.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("phi1"))

  phi2.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("phi2"))

  icmp3.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("icmp3"))

  br4.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("br4"))



  br5.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("br5"))



  phi6.io.enable <> bb_for_cond1.io.Out(param.bb_for_cond1_activate("phi6"))

  icmp7.io.enable <> bb_for_cond1.io.Out(param.bb_for_cond1_activate("icmp7"))

  br8.io.enable <> bb_for_cond1.io.Out(param.bb_for_cond1_activate("br8"))



  br9.io.enable <> bb_for_body3.io.Out(param.bb_for_body3_activate("br9"))



  phi10.io.enable <> bb_for_cond4.io.Out(param.bb_for_cond4_activate("phi10"))

  icmp11.io.enable <> bb_for_cond4.io.Out(param.bb_for_cond4_activate("icmp11"))

  br12.io.enable <> bb_for_cond4.io.Out(param.bb_for_cond4_activate("br12"))



  getelementptr13.io.enable <> bb_for_body6.io.Out(param.bb_for_body6_activate("getelementptr13"))

  load14.io.enable <> bb_for_body6.io.Out(param.bb_for_body6_activate("load14"))

  mul15.io.enable <> bb_for_body6.io.Out(param.bb_for_body6_activate("mul15"))

  getelementptr16.io.enable <> bb_for_body6.io.Out(param.bb_for_body6_activate("getelementptr16"))

  store17.io.enable <> bb_for_body6.io.Out(param.bb_for_body6_activate("store17"))

  br18.io.enable <> bb_for_body6.io.Out(param.bb_for_body6_activate("br18"))



  add19.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("add19"))

  br20.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("br20"))



  sub21.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("sub21"))

  getelementptr22.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("getelementptr22"))

  load23.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("load23"))

  add24.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("add24"))

  store25.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("store25"))

  br26.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("br26"))



  add27.io.enable <> bb_for_inc10.io.Out(param.bb_for_inc10_activate("add27"))

  br28.io.enable <> bb_for_inc10.io.Out(param.bb_for_inc10_activate("br28"))



  sub29.io.enable <> bb_for_end12.io.Out(param.bb_for_end12_activate("sub29"))

  getelementptr30.io.enable <> bb_for_end12.io.Out(param.bb_for_end12_activate("getelementptr30"))

  load31.io.enable <> bb_for_end12.io.Out(param.bb_for_end12_activate("load31"))

  add32.io.enable <> bb_for_end12.io.Out(param.bb_for_end12_activate("add32"))

  store33.io.enable <> bb_for_end12.io.Out(param.bb_for_end12_activate("store33"))

  add34.io.enable <> bb_for_end12.io.Out(param.bb_for_end12_activate("add34"))

  br35.io.enable <> bb_for_end12.io.Out(param.bb_for_end12_activate("br35"))



  add36.io.enable <> bb_for_inc16.io.Out(param.bb_for_inc16_activate("add36"))

  br37.io.enable <> bb_for_inc16.io.Out(param.bb_for_inc16_activate("br37"))



  sdiv38.io.enable <> bb_for_end18.io.Out(param.bb_for_end18_activate("sdiv38"))

  ret39.io.enable <> bb_for_end18.io.Out(param.bb_for_end18_activate("ret39"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %a
  lb_L_0.io.In(0) <> lb_L_1.io.liveIn.data("field0")(0) //InputSplitter.io.Out.data("field0")(0)

  // Connecting function argument to the loop header
  //i32 %n
  lb_L_0.io.In(1) <> lb_L_1.io.liveIn.data("field1")(0) //InputSplitter.io.Out.data("field1")(0)

  // Connecting function argument to the loop header
  //i32* %a
  lb_L_1.io.In(0) <> lb_L_2.io.liveIn.data("field0")(0) // InputSplitter.io.Out.data("field0")(1)

  // Connecting function argument to the loop header
  //i32 %n
  lb_L_1.io.In(1) <> lb_L_2.io.liveIn.data("field1")(0) // InputSplitter.io.Out.data("field1")(1)

  // Connecting function argument to the loop header
  //i32* %a
  lb_L_2.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  // Connecting function argument to the loop header
  //i32 %n
  lb_L_2.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  // Connecting instruction to the loop header
  //  %result.0 = phi i32 [ 0, %entry ], [ %add, %for.inc16 ], !UID !12, !ScalaLabel !13
  lb_L_2.io.liveOut(0) <> phi2.io.Out(param.sdiv38_in("phi2"))



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

  phi1.io.InData(param.phi1_phi_in("add36")) <> add36.io.Out(param.phi1_in("add36"))

  phi2.io.InData(param.phi2_phi_in("const_0")).bits.data := 0.U
  phi2.io.InData(param.phi2_phi_in("const_0")).bits.predicate := true.B
  phi2.io.InData(param.phi2_phi_in("const_0")).valid := true.B

  phi2.io.InData(param.phi2_phi_in("add34")) <> add34.io.Out(param.phi2_in("add34"))

  phi6.io.InData(param.phi6_phi_in("const_0")).bits.data := 0.U
  phi6.io.InData(param.phi6_phi_in("const_0")).bits.predicate := true.B
  phi6.io.InData(param.phi6_phi_in("const_0")).valid := true.B

  phi6.io.InData(param.phi6_phi_in("add27")) <> add27.io.Out(param.phi6_in("add27"))

  phi10.io.InData(param.phi10_phi_in("const_0")).bits.data := 0.U
  phi10.io.InData(param.phi10_phi_in("const_0")).bits.predicate := true.B
  phi10.io.InData(param.phi10_phi_in("const_0")).valid := true.B

  phi10.io.InData(param.phi10_phi_in("add19")) <> add19.io.Out(param.phi10_in("add19"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_for_cond.io.MaskBB(0)

  phi2.io.Mask <> bb_for_cond.io.MaskBB(1)

  phi6.io.Mask <> bb_for_cond1.io.MaskBB(0)

  phi10.io.Mask <> bb_for_cond4.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp3.io.LeftIO <> phi1.io.Out(param.icmp3_in("phi1"))

  // Wiring constant
  icmp3.io.RightIO.bits.data := 3.U
  icmp3.io.RightIO.bits.predicate := true.B
  icmp3.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br4.io.CmpIO <> icmp3.io.Out(param.br4_in("icmp3"))

  // Wiring instructions
  icmp7.io.LeftIO <> phi6.io.Out(param.icmp7_in("phi6"))

  // Wiring Binary instruction to the loop header
  icmp7.io.RightIO <> lb_L_1.io.liveIn.data("field1")(1)

  // Wiring Branch instruction
  br8.io.CmpIO <> icmp7.io.Out(param.br8_in("icmp7"))

  // Wiring instructions
  icmp11.io.LeftIO <> phi10.io.Out(param.icmp11_in("phi10"))

  // Wiring Binary instruction to the loop header
  icmp11.io.RightIO <> lb_L_0.io.liveIn.data("field1")(0)

  // Wiring Branch instruction
  br12.io.CmpIO <> icmp11.io.Out(param.br12_in("icmp11"))

  // Wiring GEP instruction to the loop header
  getelementptr13.io.baseAddress <> lb_L_0.io.liveIn.data("field0")(0)

  // Wiring GEP instruction to the parent instruction
  getelementptr13.io.idx1 <> phi10.io.Out(param.getelementptr13_in("phi10"))


  // Wiring Load instruction to the parent instruction
  load14.io.GepAddr <> getelementptr13.io.Out(param.load14_in("getelementptr13"))
  load14.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load14.io.memReq




  // Wiring constant
  mul15.io.LeftIO.bits.data := 2.U
  mul15.io.LeftIO.bits.predicate := true.B
  mul15.io.LeftIO.valid := true.B

  // Wiring instructions
  mul15.io.RightIO <> load14.io.Out(param.mul15_in("load14"))

  // Wiring GEP instruction to the loop header
  getelementptr16.io.baseAddress <> lb_L_0.io.liveIn.data("field0")(1)

  // Wiring GEP instruction to the parent instruction
  getelementptr16.io.idx1 <> phi10.io.Out(param.getelementptr16_in("phi10"))


  store17.io.inData <> mul15.io.Out(param.store17_in("mul15"))



  // Wiring Store instruction to the parent instruction
  store17.io.GepAddr <> getelementptr16.io.Out(param.store17_in("getelementptr16"))
  store17.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store17.io.memReq
  store17.io.Out(0).ready := true.B



  // Wiring instructions
  add19.io.LeftIO <> phi10.io.Out(param.add19_in("phi10"))

  // Wiring constant
  add19.io.RightIO.bits.data := 1.U
  add19.io.RightIO.bits.predicate := true.B
  add19.io.RightIO.valid := true.B

  // Wiring Binary instruction to the loop header
  sub21.io.LeftIO <>lb_L_1.io.liveIn.data("field1")(2)

  // Wiring constant
  sub21.io.RightIO.bits.data := 1.U
  sub21.io.RightIO.bits.predicate := true.B
  sub21.io.RightIO.valid := true.B

  // Wiring GEP instruction to the loop header
  getelementptr22.io.baseAddress <> lb_L_1.io.liveIn.data("field0")(1)

  // Wiring GEP instruction to the parent instruction
  getelementptr22.io.idx1 <> sub21.io.Out(param.getelementptr22_in("sub21"))


  // Wiring Load instruction to the parent instruction
  load23.io.GepAddr <> getelementptr22.io.Out(param.load23_in("getelementptr22"))
  load23.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load23.io.memReq




  // Wiring instructions
  add24.io.LeftIO <> load23.io.Out(param.add24_in("load23"))

  // Wiring constant
  add24.io.RightIO.bits.data := 1.U
  add24.io.RightIO.bits.predicate := true.B
  add24.io.RightIO.valid := true.B

  store25.io.inData <> add24.io.Out(param.store25_in("add24"))



  // Wiring Store instruction to the parent instruction
  store25.io.GepAddr <> getelementptr22.io.Out(param.store25_in("getelementptr22"))
  store25.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store25.io.memReq
  store25.io.Out(0).ready := true.B



  // Wiring instructions
  add27.io.LeftIO <> phi6.io.Out(param.add27_in("phi6"))

  // Wiring constant
  add27.io.RightIO.bits.data := 1.U
  add27.io.RightIO.bits.predicate := true.B
  add27.io.RightIO.valid := true.B

  // Wiring Binary instruction to the loop header
  sub29.io.LeftIO <> lb_L_2.io.liveIn.data("field1")(1)

  // Wiring constant
  sub29.io.RightIO.bits.data := 1.U
  sub29.io.RightIO.bits.predicate := true.B
  sub29.io.RightIO.valid := true.B

  // Wiring GEP instruction to the loop header
  getelementptr30.io.baseAddress <> lb_L_2.io.liveIn.data("field0")(1)

  // Wiring GEP instruction to the parent instruction
  getelementptr30.io.idx1 <> sub29.io.Out(param.getelementptr30_in("sub29"))


  // Wiring Load instruction to the parent instruction
  load31.io.GepAddr <> getelementptr30.io.Out(param.load31_in("getelementptr30"))
  load31.io.memResp <> CacheMem.io.ReadOut(2)
  CacheMem.io.ReadIn(2) <> load31.io.memReq




  // Wiring instructions
  add32.io.LeftIO <> load31.io.Out(param.add32_in("load31"))

  // Wiring constant
  add32.io.RightIO.bits.data := 1.U
  add32.io.RightIO.bits.predicate := true.B
  add32.io.RightIO.valid := true.B

  store33.io.inData <> add32.io.Out(param.store33_in("add32"))



  // Wiring Store instruction to the parent instruction
  store33.io.GepAddr <> getelementptr30.io.Out(param.store33_in("getelementptr30"))
  store33.io.memResp  <> CacheMem.io.WriteOut(2)
  CacheMem.io.WriteIn(2) <> store33.io.memReq
  store33.io.Out(0).ready := true.B



  // Wiring instructions
  add34.io.LeftIO <> phi2.io.Out(param.add34_in("phi2"))

  // Wiring instructions
  add34.io.RightIO <> load31.io.Out(param.add34_in("load31"))

  // Wiring instructions
  add36.io.LeftIO <> phi1.io.Out(param.add36_in("phi1"))

  // Wiring constant
  add36.io.RightIO.bits.data := 1.U
  add36.io.RightIO.bits.predicate := true.B
  add36.io.RightIO.valid := true.B

  // Wiring instructions
  sdiv38.io.LeftIO <> lb_L_2.io.Out(0)

  // Wiring constant
  sdiv38.io.RightIO.bits.data := 2.U
  sdiv38.io.RightIO.bits.predicate := true.B
  sdiv38.io.RightIO.valid := true.B

  // Wiring return instruction
  ret39.io.In.data("field0") <> sdiv38.io.Out(param.ret39_in("sdiv38"))
  io.out <> ret39.io.Out


}

import java.io.{File, FileWriter}
object test15Main extends App {
  val dir = new File("RTL/test15") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test15DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


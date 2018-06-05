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

object Data_S4_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_while_cond_pred = Map(
    "br0" -> 0,
    "br31" -> 1
  )


  val bb_while_end_pred = Map(
    "br12" -> 0
  )


  val bb_while_body_pred = Map(
    "br6" -> 0
  )


  val bb_while_end16_pred = Map(
    "br6" -> 0
  )


  val bb_while_cond1_pred = Map(
    "br7" -> 0,
    "br13" -> 1
  )


  val bb_while_body5_pred = Map(
    "br12" -> 0
  )


  val br0_brn_bb = Map(
    "bb_while_cond" -> 0
  )


  val br6_brn_bb = Map(
    "bb_while_body" -> 0,
    "bb_while_end16" -> 1
  )


  val br7_brn_bb = Map(
    "bb_while_cond1" -> 0
  )


  val br12_brn_bb = Map(
    "bb_while_body5" -> 0,
    "bb_while_end" -> 1
  )


  val br13_brn_bb = Map(
    "bb_while_cond1" -> 0
  )


  val br31_brn_bb = Map(
    "bb_while_cond" -> 0
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_while_cond_activate = Map(
    "phi1" -> 0,
    "urem2" -> 1,
    "getelementptr3" -> 2,
    "load4" -> 3,
    "icmp5" -> 4,
    "br6" -> 5
  )


  val bb_while_body_activate = Map(
    "br7" -> 0
  )


  val bb_while_cond1_activate = Map(
    "urem8" -> 0,
    "getelementptr9" -> 1,
    "load10" -> 2,
    "icmp11" -> 3,
    "br12" -> 4
  )


  val bb_while_body5_activate = Map(
    "br13" -> 0
  )


  val bb_while_end_activate = Map(
    "urem14" -> 0,
    "getelementptr15" -> 1,
    "load16" -> 2,
    "getelementptr17" -> 3,
    "load18" -> 4,
    "getelementptr19" -> 5,
    "store20" -> 6,
    "add21" -> 7,
    "getelementptr22" -> 8,
    "load23" -> 9,
    "add24" -> 10,
    "getelementptr25" -> 11,
    "store26" -> 12,
    "urem27" -> 13,
    "getelementptr28" -> 14,
    "store29" -> 15,
    "add30" -> 16,
    "br31" -> 17
  )


  val bb_while_end16_activate = Map(
    "ret32" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add30" -> 1
  )


  //  %iter.0 = phi i32 [ 0, %entry ], [ %add15, %while.end ], !UID !19, !ScalaLabel !20
  val phi1_in = Map(
    "add30" -> 0
  )


  //  %rem = urem i32 %iter.0, 100, !UID !21, !ScalaLabel !22
  val urem2_in = Map(
    "phi1" -> 0
  )


  //  %arrayidx = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !23, !ScalaLabel !24
  val getelementptr3_in = Map(
    "" -> 0,
    "urem2" -> 0
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !25, !ScalaLabel !26
  val load4_in = Map(
    "getelementptr3" -> 0
  )


  //  %cmp = icmp ne i32 %0, -10, !UID !27, !ScalaLabel !28
  val icmp5_in = Map(
    "load4" -> 0
  )


  //  br i1 %cmp, label %while.body, label %while.end16, !UID !29, !BB_UID !30, !ScalaLabel !31
  val br6_in = Map(
    "icmp5" -> 0
  )


  //  %rem2 = urem i32 %iter.0, 100, !UID !35, !ScalaLabel !36
  val urem8_in = Map(
    "phi1" -> 1
  )


  //  %arrayidx3 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem2, !UID !37, !ScalaLabel !38
  val getelementptr9_in = Map(
    "" -> 1,
    "urem8" -> 0
  )


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !39, !ScalaLabel !40
  val load10_in = Map(
    "getelementptr9" -> 0
  )


  //  %cmp4 = icmp eq i32 %1, -1, !UID !41, !ScalaLabel !42
  val icmp11_in = Map(
    "load10" -> 0
  )


  //  br i1 %cmp4, label %while.body5, label %while.end, !UID !43, !BB_UID !44, !ScalaLabel !45
  val br12_in = Map(
    "icmp11" -> 0
  )


  //  %rem6 = urem i32 %iter.0, 100, !UID !57, !ScalaLabel !58
  val urem14_in = Map(
    "phi1" -> 2
  )


  //  %arrayidx7 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem6, !UID !59, !ScalaLabel !60
  val getelementptr15_in = Map(
    "" -> 2,
    "urem14" -> 0
  )


  //  %2 = load i32, i32* %arrayidx7, align 4, !UID !61, !ScalaLabel !62
  val load16_in = Map(
    "getelementptr15" -> 0
  )


  //  %arrayidx8 = getelementptr inbounds i32, i32* %x, i32 %2, !UID !63, !ScalaLabel !64
  val getelementptr17_in = Map(
    "field0" -> 0,
    "load16" -> 0
  )


  //  %3 = load i32, i32* %arrayidx8, align 4, !UID !65, !ScalaLabel !66
  val load18_in = Map(
    "getelementptr17" -> 0
  )


  //  %arrayidx9 = getelementptr inbounds i32, i32* %y, i32 %2, !UID !67, !ScalaLabel !68
  val getelementptr19_in = Map(
    "field1" -> 0,
    "load16" -> 1
  )


  //  store i32 %3, i32* %arrayidx9, align 4, !UID !69, !ScalaLabel !70
  val store20_in = Map(
    "load18" -> 0,
    "getelementptr19" -> 0
  )


  //  %add = add i32 %2, 1, !UID !71, !ScalaLabel !72
  val add21_in = Map(
    "load16" -> 2
  )


  //  %arrayidx10 = getelementptr inbounds i32, i32* %x, i32 %add, !UID !73, !ScalaLabel !74
  val getelementptr22_in = Map(
    "field0" -> 1,
    "add21" -> 0
  )


  //  %4 = load i32, i32* %arrayidx10, align 4, !UID !75, !ScalaLabel !76
  val load23_in = Map(
    "getelementptr22" -> 0
  )


  //  %add11 = add i32 %2, 1, !UID !77, !ScalaLabel !78
  val add24_in = Map(
    "load16" -> 3
  )


  //  %arrayidx12 = getelementptr inbounds i32, i32* %y, i32 %add11, !UID !79, !ScalaLabel !80
  val getelementptr25_in = Map(
    "field1" -> 1,
    "add24" -> 0
  )


  //  store i32 %4, i32* %arrayidx12, align 4, !UID !81, !ScalaLabel !82
  val store26_in = Map(
    "load23" -> 0,
    "getelementptr25" -> 0
  )


  //  %rem13 = urem i32 %iter.0, 100, !UID !83, !ScalaLabel !84
  val urem27_in = Map(
    "phi1" -> 3
  )


  //  %arrayidx14 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem13, !UID !85, !ScalaLabel !86
  val getelementptr28_in = Map(
    "" -> 3,
    "urem27" -> 0
  )


  //  store i32 -1, i32* %arrayidx14, align 4, !UID !87, !ScalaLabel !88
  val store29_in = Map(
    "getelementptr28" -> 0
  )


  //  %add15 = add i32 %iter.0, 1, !UID !89, !ScalaLabel !90
  val add30_in = Map(
    "phi1" -> 4
  )


  //  ret void, !UID !97, !BB_UID !98, !ScalaLabel !99
  val ret32_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class S4DFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class S4DF(implicit p: Parameters) extends S4DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=5,NWrites=3)
		            (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=5,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=4096,NReads=5,NWrites=3)
		            (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=5,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 4, ID = 0))


  val loop_L_1_liveIN_0 = Module(new LiveInNode(NumOuts = 2, ID = 0))
  val loop_L_1_liveIN_1 = Module(new LiveInNode(NumOuts = 2, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_while_cond = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 1))

  val bb_while_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_while_cond1 = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 5, NumPhi=1, BID = 3)) // manually changed

  val bb_while_body5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_while_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 19, BID = 5))

  val bb_while_end16 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 6))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %while.cond, !UID !16, !BB_UID !17, !ScalaLabel !18
  val br0 = Module (new UBranchFastNode(ID = 0))

  // [BasicBlock]  while.cond:

  //  %iter.0 = phi i32 [ 0, %entry ], [ %add15, %while.end ], !UID !19, !ScalaLabel !20
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))


  //  %rem = urem i32 %iter.0, 100, !UID !21, !ScalaLabel !22
  val urem2 = Module (new ComputeNode(NumOuts = 1, ID = 2, opCode = "urem")(sign=false))


  //  %arrayidx = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !23, !ScalaLabel !24
  val getelementptr3 = Module (new GepTwoNode(NumOuts = 1, ID = 3)(numByte1 = 4, numByte2 = 4))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !25, !ScalaLabel !26
  val load4 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=4,RouteID=0))


  //  %cmp = icmp ne i32 %0, -10, !UID !27, !ScalaLabel !28
  val icmp5 = Module (new IcmpNode(NumOuts = 1, ID = 5, opCode = "NE")(sign=false))


  //  br i1 %cmp, label %while.body, label %while.end16, !UID !29, !BB_UID !30, !ScalaLabel !31
  val br6 = Module (new CBranchNode(ID = 6))

  // [BasicBlock]  while.body:

  //  br label %while.cond1, !UID !32, !BB_UID !33, !ScalaLabel !34
  val br7 = Module (new UBranchFastNode(ID = 7))

  // [BasicBlock]  while.cond1:

  //  %rem2 = urem i32 %iter.0, 100, !UID !35, !ScalaLabel !36
  val urem8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "urem")(sign=false))


  //  %arrayidx3 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem2, !UID !37, !ScalaLabel !38
  val getelementptr9 = Module (new GepTwoNode(NumOuts = 1, ID = 9)(numByte1 = 4, numByte2 = 4))


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !39, !ScalaLabel !40
  val load10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=10,RouteID=1))


  //  %cmp4 = icmp eq i32 %1, -1, !UID !41, !ScalaLabel !42
  val icmp11 = Module (new IcmpNode(NumOuts = 1, ID = 11, opCode = "EQ")(sign=false))


  //  br i1 %cmp4, label %while.body5, label %while.end, !UID !43, !BB_UID !44, !ScalaLabel !45
  val br12 = Module (new CBranchNode(ID = 12))

  // [BasicBlock]  while.body5:

  //  br label %while.cond1, !llvm.loop !46, !UID !54, !BB_UID !55, !ScalaLabel !56
  val br13 = Module (new UBranchFastNode(ID = 13))

  // [BasicBlock]  while.end:

  //  %rem6 = urem i32 %iter.0, 100, !UID !57, !ScalaLabel !58
  val urem14 = Module (new ComputeNode(NumOuts = 1, ID = 14, opCode = "urem")(sign=false))


  //  %arrayidx7 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem6, !UID !59, !ScalaLabel !60
  val getelementptr15 = Module (new GepTwoNode(NumOuts = 1, ID = 15)(numByte1 = 4, numByte2 = 4))


  //  %2 = load i32, i32* %arrayidx7, align 4, !UID !61, !ScalaLabel !62
  val load16 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=4,ID=16,RouteID=2))


  //  %arrayidx8 = getelementptr inbounds i32, i32* %x, i32 %2, !UID !63, !ScalaLabel !64
  val getelementptr17 = Module (new GepOneNode(NumOuts = 1, ID = 17)(numByte1 = 4))


  //  %3 = load i32, i32* %arrayidx8, align 4, !UID !65, !ScalaLabel !66
  val load18 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=18,RouteID=3))


  //  %arrayidx9 = getelementptr inbounds i32, i32* %y, i32 %2, !UID !67, !ScalaLabel !68
  val getelementptr19 = Module (new GepOneNode(NumOuts = 1, ID = 19)(numByte1 = 4))


  //  store i32 %3, i32* %arrayidx9, align 4, !UID !69, !ScalaLabel !70
  val store20 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=20,RouteID=0))


  //  %add = add i32 %2, 1, !UID !71, !ScalaLabel !72
  val add21 = Module (new ComputeNode(NumOuts = 1, ID = 21, opCode = "add")(sign=false))


  //  %arrayidx10 = getelementptr inbounds i32, i32* %x, i32 %add, !UID !73, !ScalaLabel !74
  val getelementptr22 = Module (new GepOneNode(NumOuts = 1, ID = 22)(numByte1 = 4))


  //  %4 = load i32, i32* %arrayidx10, align 4, !UID !75, !ScalaLabel !76
  val load23 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=23,RouteID=4))


  //  %add11 = add i32 %2, 1, !UID !77, !ScalaLabel !78
  val add24 = Module (new ComputeNode(NumOuts = 1, ID = 24, opCode = "add")(sign=false))


  //  %arrayidx12 = getelementptr inbounds i32, i32* %y, i32 %add11, !UID !79, !ScalaLabel !80
  val getelementptr25 = Module (new GepOneNode(NumOuts = 1, ID = 25)(numByte1 = 4))


  //  store i32 %4, i32* %arrayidx12, align 4, !UID !81, !ScalaLabel !82
  val store26 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=26,RouteID=1))


  //  %rem13 = urem i32 %iter.0, 100, !UID !83, !ScalaLabel !84
  val urem27 = Module (new ComputeNode(NumOuts = 1, ID = 27, opCode = "urem")(sign=false))


  //  %arrayidx14 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem13, !UID !85, !ScalaLabel !86
  val getelementptr28 = Module (new GepTwoNode(NumOuts = 1, ID = 28)(numByte1 = 4, numByte2 = 4))


  //  store i32 -1, i32* %arrayidx14, align 4, !UID !87, !ScalaLabel !88
  val store29 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=29,RouteID=2))


  //  %add15 = add i32 %iter.0, 1, !UID !89, !ScalaLabel !90
  val add30 = Module (new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign=false))


  //  br label %while.cond, !llvm.loop !91, !UID !94, !BB_UID !95, !ScalaLabel !96
  val br31 = Module (new UBranchFastNode(ID = 31))

  // [BasicBlock]  while.end16:

  //  ret void, !UID !97, !BB_UID !98, !ScalaLabel !99
  val ret32 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=32))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_S4_FlowParam



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

  //Connecting br0 to bb_while_cond
  bb_while_cond.io.predicateIn(param.bb_while_cond_pred("br0")) <> br0.io.Out(param.br0_brn_bb("bb_while_cond"))


  //Connecting br6 to bb_while_body
  bb_while_body.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_while_body"))


  //Connecting br6 to bb_while_end16
  bb_while_end16.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_while_end16"))


  //Connecting br7 to bb_while_cond1
  bb_while_cond1.io.predicateIn(0) <> br7.io.Out(param.br7_brn_bb("bb_while_cond1")) // Manually corrected


  //Connecting br12 to bb_while_body5
  bb_while_body5.io.predicateIn <> br12.io.Out(param.br12_brn_bb("bb_while_body5"))


  //Connecting br12 to bb_while_end
  bb_while_end.io.predicateIn <> br12.io.Out(param.br12_brn_bb("bb_while_end"))


  //Connecting br13 to bb_while_cond1
  bb_while_cond1.io.predicateIn(1) <> br13.io.Out(param.br13_brn_bb("bb_while_cond1")) // Manually corrected
  bb_while_cond1.io.MaskBB(0).ready := true.B // Manually added

  //Connecting br31 to bb_while_cond
  bb_while_cond.io.predicateIn(param.bb_while_cond_pred("br31")) <> br31.io.Out(param.br31_brn_bb("bb_while_cond"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  phi1.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("phi1"))

  urem2.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("urem2"))

  getelementptr3.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("getelementptr3"))

  load4.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("load4"))

  icmp5.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("icmp5"))

  br6.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("br6"))



  br7.io.enable <> bb_while_body.io.Out(param.bb_while_body_activate("br7"))



  urem8.io.enable <> bb_while_cond1.io.Out(param.bb_while_cond1_activate("urem8"))

  getelementptr9.io.enable <> bb_while_cond1.io.Out(param.bb_while_cond1_activate("getelementptr9"))

  load10.io.enable <> bb_while_cond1.io.Out(param.bb_while_cond1_activate("load10"))

  icmp11.io.enable <> bb_while_cond1.io.Out(param.bb_while_cond1_activate("icmp11"))

  br12.io.enable <> bb_while_cond1.io.Out(param.bb_while_cond1_activate("br12"))



  br13.io.enable <> bb_while_body5.io.Out(param.bb_while_body5_activate("br13"))



  urem14.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("urem14"))

  getelementptr15.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("getelementptr15"))

  load16.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("load16"))

  getelementptr17.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("getelementptr17"))

  load18.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("load18"))

  getelementptr19.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("getelementptr19"))

  store20.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("store20"))

  add21.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("add21"))

  getelementptr22.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("getelementptr22"))

  load23.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("load23"))

  add24.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("add24"))

  getelementptr25.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("getelementptr25"))

  store26.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("store26"))

  urem27.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("urem27"))

  getelementptr28.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("getelementptr28"))

  store29.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("store29"))

  add30.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("add30"))

  br31.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("br31"))

  loop_L_0_liveIN_0.io.enable <> bb_while_end.io.Out(18)




  ret32.io.enable <> bb_while_end16.io.Out(param.bb_while_end16_activate("ret32"))

  loop_L_1_liveIN_0.io.enable <> bb_while_end16.io.Out(1)
  loop_L_1_liveIN_1.io.enable <> bb_while_end16.io.Out(2)






  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting instruction to the loop header
  //  %iter.0 = phi i32 [ 0, %entry ], [ %add15, %while.end ], !UID !19, !ScalaLabel !20
  loop_L_0_liveIN_0.io.InData <> phi1.io.Out(param.urem8_in("phi1"))

  // Connecting function argument to the loop header
  //i32* %x
  loop_L_1_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32* %y
  loop_L_1_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")



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

  phi1.io.InData(param.phi1_phi_in("add30")) <> add30.io.Out(param.phi1_in("add30"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_while_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  urem2.io.LeftIO <> phi1.io.Out(param.urem2_in("phi1"))

  // Wiring constant
  urem2.io.RightIO.bits.data := 100.U
  urem2.io.RightIO.bits.predicate := true.B
  urem2.io.RightIO.valid := true.B

  // Manually added.  Pointer to q[] which is global
  getelementptr3.io.baseAddress.valid := true.B
  getelementptr3.io.baseAddress.bits.predicate :=  true.B
  getelementptr3.io.baseAddress.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  getelementptr3.io.idx1.valid :=  true.B
  getelementptr3.io.idx1.bits.predicate :=  true.B
  getelementptr3.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the parent instruction
  getelementptr3.io.idx2 <> urem2.io.Out(param.getelementptr3_in("urem2"))


  // Wiring Load instruction to the parent instruction
  load4.io.GepAddr <> getelementptr3.io.Out(param.load4_in("getelementptr3"))
  load4.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load4.io.memReq




  // Wiring instructions
  icmp5.io.LeftIO <> load4.io.Out(param.icmp5_in("load4"))

  // Wiring constant
  icmp5.io.RightIO.bits.data := (0.U(32.W)-10.U(32.W))
  icmp5.io.RightIO.bits.predicate := true.B
  icmp5.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br6.io.CmpIO <> icmp5.io.Out(param.br6_in("icmp5"))

  // Wiring instructions
  urem8.io.LeftIO <> loop_L_0_liveIN_0.io.Out(0)

  // Wiring constant
  urem8.io.RightIO.bits.data := 100.U
  urem8.io.RightIO.bits.predicate := true.B
  urem8.io.RightIO.valid := true.B

  // Manually added.  Pointer to q[] which is global
  getelementptr9.io.baseAddress.valid := true.B
  getelementptr9.io.baseAddress.bits.predicate :=  true.B
  getelementptr9.io.baseAddress.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  getelementptr9.io.idx1.valid :=  true.B
  getelementptr9.io.idx1.bits.predicate :=  true.B
  getelementptr9.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the parent instruction
  getelementptr9.io.idx2 <> urem8.io.Out(param.getelementptr9_in("urem8"))


  // Wiring Load instruction to the parent instruction
  load10.io.GepAddr <> getelementptr9.io.Out(param.load10_in("getelementptr9"))
  load10.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load10.io.memReq




  // Wiring instructions
  icmp11.io.LeftIO <> load10.io.Out(param.icmp11_in("load10"))

  // Wiring constant
  icmp11.io.RightIO.bits.data := (0.U(32.W)-1.U(32.W))
  icmp11.io.RightIO.bits.predicate := true.B
  icmp11.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br12.io.CmpIO <> icmp11.io.Out(param.br12_in("icmp11"))

  // Wiring instructions
  urem14.io.LeftIO <> loop_L_0_liveIN_0.io.Out(1)

  // Wiring constant
  urem14.io.RightIO.bits.data := 100.U
  urem14.io.RightIO.bits.predicate := true.B
  urem14.io.RightIO.valid := true.B

  // Manually added.  Pointer to q[] which is global
  getelementptr15.io.baseAddress.valid := true.B
  getelementptr15.io.baseAddress.bits.predicate :=  true.B
  getelementptr15.io.baseAddress.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  getelementptr15.io.idx1.valid :=  true.B
  getelementptr15.io.idx1.bits.predicate :=  true.B
  getelementptr15.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the parent instruction
  getelementptr15.io.idx2 <> urem14.io.Out(param.getelementptr15_in("urem14"))


  // Wiring Load instruction to the parent instruction
  load16.io.GepAddr <> getelementptr15.io.Out(param.load16_in("getelementptr15"))
  load16.io.memResp <> CacheMem.io.ReadOut(2)
  CacheMem.io.ReadIn(2) <> load16.io.memReq




  // Wiring GEP instruction to the loop header
  getelementptr17.io.baseAddress <> loop_L_1_liveIN_0.io.Out(param.getelementptr17_in("field0"))

  // Wiring GEP instruction to the parent instruction
  getelementptr17.io.idx1 <> load16.io.Out(param.getelementptr17_in("load16"))


  // Wiring Load instruction to the parent instruction
  load18.io.GepAddr <> getelementptr17.io.Out(param.load18_in("getelementptr17"))
  load18.io.memResp <> CacheMem.io.ReadOut(3)
  CacheMem.io.ReadIn(3) <> load18.io.memReq




  // Wiring GEP instruction to the loop header
  getelementptr19.io.baseAddress <> loop_L_1_liveIN_1.io.Out(param.getelementptr19_in("field1"))

  // Wiring GEP instruction to the parent instruction
  getelementptr19.io.idx1 <> load16.io.Out(param.getelementptr19_in("load16"))


  store20.io.inData <> load18.io.Out(param.store20_in("load18"))



  // Wiring Store instruction to the parent instruction
  store20.io.GepAddr <> getelementptr19.io.Out(param.store20_in("getelementptr19"))
  store20.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store20.io.memReq
  store20.io.Out(0).ready := true.B



  // Wiring instructions
  add21.io.LeftIO <> load16.io.Out(param.add21_in("load16"))

  // Wiring constant
  add21.io.RightIO.bits.data := 1.U
  add21.io.RightIO.bits.predicate := true.B
  add21.io.RightIO.valid := true.B

  // Wiring GEP instruction to the loop header
  getelementptr22.io.baseAddress <> loop_L_1_liveIN_0.io.Out(param.getelementptr22_in("field0"))

  // Wiring GEP instruction to the parent instruction
  getelementptr22.io.idx1 <> add21.io.Out(param.getelementptr22_in("add21"))


  // Wiring Load instruction to the parent instruction
  load23.io.GepAddr <> getelementptr22.io.Out(param.load23_in("getelementptr22"))
  load23.io.memResp <> CacheMem.io.ReadOut(4)
  CacheMem.io.ReadIn(4) <> load23.io.memReq




  // Wiring instructions
  add24.io.LeftIO <> load16.io.Out(param.add24_in("load16"))

  // Wiring constant
  add24.io.RightIO.bits.data := 1.U
  add24.io.RightIO.bits.predicate := true.B
  add24.io.RightIO.valid := true.B

  // Wiring GEP instruction to the loop header
  getelementptr25.io.baseAddress <> loop_L_1_liveIN_1.io.Out(param.getelementptr25_in("field1"))

  // Wiring GEP instruction to the parent instruction
  getelementptr25.io.idx1 <> add24.io.Out(param.getelementptr25_in("add24"))


  store26.io.inData <> load23.io.Out(param.store26_in("load23"))



  // Wiring Store instruction to the parent instruction
  store26.io.GepAddr <> getelementptr25.io.Out(param.store26_in("getelementptr25"))
  store26.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store26.io.memReq
  store26.io.Out(0).ready := true.B



  // Wiring instructions
  urem27.io.LeftIO <> loop_L_0_liveIN_0.io.Out(2)

  // Wiring constant
  urem27.io.RightIO.bits.data := 100.U
  urem27.io.RightIO.bits.predicate := true.B
  urem27.io.RightIO.valid := true.B

  // Manually added.  Pointer to q[] which is global
  getelementptr28.io.baseAddress.valid := true.B
  getelementptr28.io.baseAddress.bits.predicate :=  true.B
  getelementptr28.io.baseAddress.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  getelementptr28.io.idx1.valid :=  true.B
  getelementptr28.io.idx1.bits.predicate :=  true.B
  getelementptr28.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the parent instruction
  getelementptr28.io.idx2 <> urem27.io.Out(param.getelementptr28_in("urem27"))


  // Wiring constant instructions to store
  store29.io.inData.bits.data := (0.U(32.W)-1.U(32.W))
  store29.io.inData.bits.predicate := true.B
  store29.io.inData.valid := true.B



  // Wiring Store instruction to the parent instruction
  store29.io.GepAddr <> getelementptr28.io.Out(param.store29_in("getelementptr28"))
  store29.io.memResp  <> CacheMem.io.WriteOut(2)
  CacheMem.io.WriteIn(2) <> store29.io.memReq
  store29.io.Out(0).ready := true.B



  // Wiring instructions
  add30.io.LeftIO <> loop_L_0_liveIN_0.io.Out(3)

  // Wiring constant
  add30.io.RightIO.bits.data := 1.U
  add30.io.RightIO.bits.predicate := true.B
  add30.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  ret32.io.predicateIn(0).bits.control := true.B
  ret32.io.predicateIn(0).bits.taskID := 0.U
  ret32.io.predicateIn(0).valid := true.B
  ret32.io.In.data("field0").bits.data := 1.U
  ret32.io.In.data("field0").bits.predicate := true.B
  ret32.io.In.data("field0").valid := true.B
  io.out <> ret32.io.Out


}

import java.io.{File, FileWriter}
object S4Main extends App {
  val dir = new File("RTL/S4") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new S4DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


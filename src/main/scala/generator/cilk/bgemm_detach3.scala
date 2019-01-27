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

object Data_bgemm_detach3_FlowParam{

  val bb_my_pfor_body15_pred = Map(
    "active" -> 0
  )


  val bb_my_loopk_pred = Map(
    "br0" -> 0
  )


  val bb_my_for_cond_pred = Map(
    "br1" -> 0,
    "br33" -> 1
  )


  val bb_my_for_inc33_pred = Map(
    "br31" -> 0
  )


  val bb_my_for_body_pred = Map(
    "br4" -> 0
  )


  val bb_my_for_end35_pred = Map(
    "br4" -> 0
  )


  val bb_my_loopj_pred = Map(
    "br12" -> 0
  )


  val bb_my_for_cond22_pred = Map(
    "br13" -> 0,
    "br30" -> 1
  )


  val bb_my_for_inc_pred = Map(
    "br28" -> 0
  )


  val bb_my_for_body24_pred = Map(
    "br16" -> 0
  )


  val bb_my_for_end_pred = Map(
    "br16" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br34" -> 0
  )


  val br0_brn_bb = Map(
    "bb_my_loopk" -> 0
  )


  val br1_brn_bb = Map(
    "bb_my_for_cond" -> 0
  )


  val br4_brn_bb = Map(
    "bb_my_for_body" -> 0,
    "bb_my_for_end35" -> 1
  )


  val br12_brn_bb = Map(
    "bb_my_loopj" -> 0
  )


  val br13_brn_bb = Map(
    "bb_my_for_cond22" -> 0
  )


  val br16_brn_bb = Map(
    "bb_my_for_body24" -> 0,
    "bb_my_for_end" -> 1
  )


  val br28_brn_bb = Map(
    "bb_my_for_inc" -> 0
  )


  val br30_brn_bb = Map(
    "bb_my_for_cond22" -> 0
  )


  val br31_brn_bb = Map(
    "bb_my_for_inc33" -> 0
  )


  val br33_brn_bb = Map(
    "bb_my_for_cond" -> 0
  )


  val br34_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val bb_my_pfor_body15_activate = Map(
    "br0" -> 0
  )


  val bb_my_loopk_activate = Map(
    "br1" -> 0
  )


  val bb_my_for_cond_activate = Map(
    "phi2" -> 0,
    "icmp3" -> 1,
    "br4" -> 2
  )


  val bb_my_for_body_activate = Map(
    "mul5" -> 0,
    "add6" -> 1,
    "mul7" -> 2,
    "add8" -> 3,
    "add9" -> 4,
    "getelementptr10" -> 5,
    "load11" -> 6,
    "br12" -> 7
  )


  val bb_my_loopj_activate = Map(
    "br13" -> 0
  )


  val bb_my_for_cond22_activate = Map(
    "phi14" -> 0,
    "icmp15" -> 1,
    "br16" -> 2
  )


  val bb_my_for_body24_activate = Map(
    "add17" -> 0,
    "add18" -> 1,
    "getelementptr19" -> 2,
    "load20" -> 3,
    "mul21" -> 4,
    "add22" -> 5,
    "add23" -> 6,
    "getelementptr24" -> 7,
    "load25" -> 8,
    "add26" -> 9,
    "store27" -> 10,
    "br28" -> 11
  )


  val bb_my_for_inc_activate = Map(
    "add29" -> 0,
    "br30" -> 1
  )


  val bb_my_for_end_activate = Map(
    "br31" -> 0
  )


  val bb_my_for_inc33_activate = Map(
    "add32" -> 0,
    "br33" -> 1
  )


  val bb_my_for_end35_activate = Map(
    "br34" -> 0
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret35" -> 0
  )


  val phi2_phi_in = Map(
    "const_0" -> 0,
    "add32" -> 1
  )


  val phi14_phi_in = Map(
    "const_0" -> 0,
    "add29" -> 1
  )


  //  %0 = phi i32 [ 0, %my_loopk ], [ %22, %my_for.inc33 ], !UID !13, !ScalaLabel !14
  val phi2_in = Map(
    "add32" -> 0
  )


  //  %1 = icmp slt i32 %0, 2, !UID !15, !ScalaLabel !16
  val icmp3_in = Map(
    "phi2" -> 0
  )


  //  br i1 %1, label %my_for.body, label %my_for.end35, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br4_in = Map(
    "icmp3" -> 0
  )


  //  %2 = mul nsw i32 %i.0.in, 4, !UID !20, !ScalaLabel !21
  val mul5_in = Map(
    "field0" -> 0
  )


  //  %3 = add nsw i32 %0, %add10.in, !UID !22, !ScalaLabel !23
  val add6_in = Map(
    "phi2" -> 1,
    "field1" -> 0
  )


  //  %4 = mul nsw i32 %3, 4, !UID !24, !ScalaLabel !25
  val mul7_in = Map(
    "add6" -> 0
  )


  //  %5 = add nsw i32 %2, %0, !UID !26, !ScalaLabel !27
  val add8_in = Map(
    "mul5" -> 0,
    "phi2" -> 2
  )


  //  %6 = add nsw i32 %5, %add10.in, !UID !28, !ScalaLabel !29
  val add9_in = Map(
    "add8" -> 0,
    "field1" -> 1
  )


  //  %7 = getelementptr inbounds i32, i32* %m1.in, i32 %6, !UID !30, !ScalaLabel !31
  val getelementptr10_in = Map(
    "field2" -> 0,
    "add9" -> 0
  )


  //  %8 = load i32, i32* %7, align 4, !UID !32, !ScalaLabel !33
  val load11_in = Map(
    "getelementptr10" -> 0
  )


  //  %9 = phi i32 [ 0, %my_loopj ], [ %21, %my_for.inc ], !UID !40, !ScalaLabel !41
  val phi14_in = Map(
    "add29" -> 0
  )


  //  %10 = icmp slt i32 %9, 2, !UID !42, !ScalaLabel !43
  val icmp15_in = Map(
    "phi14" -> 0
  )


  //  br i1 %10, label %my_for.body24, label %my_for.end, !UID !44, !BB_UID !45, !ScalaLabel !46
  val br16_in = Map(
    "icmp15" -> 0
  )


  //  %11 = add nsw i32 %4, %9, !UID !47, !ScalaLabel !48
  val add17_in = Map(
    "mul7" -> 0,
    "phi14" -> 1
  )


  //  %12 = add nsw i32 %11, %add.in, !UID !49, !ScalaLabel !50
  val add18_in = Map(
    "add17" -> 0,
    "field3" -> 0
  )


  //  %13 = getelementptr inbounds i32, i32* %m2.in, i32 %12, !UID !51, !ScalaLabel !52
  val getelementptr19_in = Map(
    "field4" -> 0,
    "add18" -> 0
  )


  //  %14 = load i32, i32* %13, align 4, !UID !53, !ScalaLabel !54
  val load20_in = Map(
    "getelementptr19" -> 0
  )


  //  %15 = mul nsw i32 %8, %14, !UID !55, !ScalaLabel !56
  val mul21_in = Map(
    "load11" -> 0,
    "load20" -> 0
  )


  //  %16 = add nsw i32 %2, %9, !UID !57, !ScalaLabel !58
  val add22_in = Map(
    "mul5" -> 1,
    "phi14" -> 2
  )


  //  %17 = add nsw i32 %16, %add.in, !UID !59, !ScalaLabel !60
  val add23_in = Map(
    "add22" -> 0,
    "field3" -> 1
  )


  //  %18 = getelementptr inbounds i32, i32* %prod.in, i32 %17, !UID !61, !ScalaLabel !62
  val getelementptr24_in = Map(
    "field5" -> 0,
    "add23" -> 0
  )


  //  %19 = load i32, i32* %18, align 4, !UID !63, !ScalaLabel !64
  val load25_in = Map(
    "getelementptr24" -> 0
  )


  //  %20 = add nsw i32 %19, %15, !UID !65, !ScalaLabel !66
  val add26_in = Map(
    "load25" -> 0,
    "mul21" -> 0
  )


  //  store i32 %20, i32* %18, align 4, !UID !67, !ScalaLabel !68
  val store27_in = Map(
    "add26" -> 0,
    "getelementptr24" -> 1
  )


  //  %21 = add nsw i32 %9, 1, !UID !72, !ScalaLabel !73
  val add29_in = Map(
    "phi14" -> 3
  )


  //  %22 = add nsw i32 %0, 1, !UID !103, !ScalaLabel !104
  val add32_in = Map(
    "phi2" -> 3
  )


  //  ret void, !UID !114, !BB_UID !115, !ScalaLabel !116
  val ret35_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class bgemm_detach3DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32,32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class bgemm_detach3DF(implicit p: Parameters) extends bgemm_detach3DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=3,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=3,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCall(List(32,32,32,32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 2, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_3 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_4 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_5 = Module(new LiveInNode(NumOuts = 1, ID = 0))


  val loop_L_1_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_1_liveIN_1 = Module(new LiveInNode(NumOuts = 2, ID = 0))
  val loop_L_1_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body15 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_my_loopk = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_my_for_cond = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 2))

  val bb_my_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 8, BID = 3))

  val bb_my_loopj = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_my_for_cond22 = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 5))

  val bb_my_for_body24 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 12, BID = 6))

  val bb_my_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 7))

  val bb_my_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 8))

  val bb_my_for_inc33 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 9))

  val bb_my_for_end35 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 10))

  val bb_my_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 11))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body15:

  //  br label %my_loopk, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  my_loopk:

  //  br label %my_for.cond, !UID !10, !BB_UID !11, !ScalaLabel !12
  val br1 = Module (new UBranchNode(ID = 1))

  // [BasicBlock]  my_for.cond:

  //  %0 = phi i32 [ 0, %my_loopk ], [ %22, %my_for.inc33 ], !UID !13, !ScalaLabel !14
  val phi2 = Module (new PhiNode(NumInputs = 2, NumOuts = 4, ID = 2))


  //  %1 = icmp slt i32 %0, 2, !UID !15, !ScalaLabel !16
  val icmp3 = Module (new IcmpNode(NumOuts = 1, ID = 3, opCode = "ULT")(sign=false))


  //  br i1 %1, label %my_for.body, label %my_for.end35, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br4 = Module (new CBranchNode(ID = 4))

  // [BasicBlock]  my_for.body:

  //  %2 = mul nsw i32 %i.0.in, 4, !UID !20, !ScalaLabel !21
  val mul5 = Module (new ComputeNode(NumOuts = 2, ID = 5, opCode = "mul")(sign=false))


  //  %3 = add nsw i32 %0, %add10.in, !UID !22, !ScalaLabel !23
  val add6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))


  //  %4 = mul nsw i32 %3, 4, !UID !24, !ScalaLabel !25
  val mul7 = Module (new ComputeNode(NumOuts = 1, ID = 7, opCode = "mul")(sign=false))


  //  %5 = add nsw i32 %2, %0, !UID !26, !ScalaLabel !27
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  %6 = add nsw i32 %5, %add10.in, !UID !28, !ScalaLabel !29
  val add9 = Module (new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign=false))


  //  %7 = getelementptr inbounds i32, i32* %m1.in, i32 %6, !UID !30, !ScalaLabel !31
  val getelementptr10 = Module (new GepOneNode(NumOuts = 1, ID = 10)(numByte1 = 1))


  //  %8 = load i32, i32* %7, align 4, !UID !32, !ScalaLabel !33
  val load11 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=11,RouteID=0))


  //  br label %my_loopj, !UID !34, !BB_UID !35, !ScalaLabel !36
  val br12 = Module (new UBranchNode(ID = 12))

  // [BasicBlock]  my_loopj:

  //  br label %my_for.cond22, !UID !37, !BB_UID !38, !ScalaLabel !39
  val br13 = Module (new UBranchNode(ID = 13))

  // [BasicBlock]  my_for.cond22:

  //  %9 = phi i32 [ 0, %my_loopj ], [ %21, %my_for.inc ], !UID !40, !ScalaLabel !41
  val phi14 = Module (new PhiNode(NumInputs = 2, NumOuts = 4, ID = 14))


  //  %10 = icmp slt i32 %9, 2, !UID !42, !ScalaLabel !43
  val icmp15 = Module (new IcmpNode(NumOuts = 1, ID = 15, opCode = "ULT")(sign=false))


  //  br i1 %10, label %my_for.body24, label %my_for.end, !UID !44, !BB_UID !45, !ScalaLabel !46
  val br16 = Module (new CBranchNode(ID = 16))

  // [BasicBlock]  my_for.body24:

  //  %11 = add nsw i32 %4, %9, !UID !47, !ScalaLabel !48
  val add17 = Module (new ComputeNode(NumOuts = 1, ID = 17, opCode = "add")(sign=false))


  //  %12 = add nsw i32 %11, %add.in, !UID !49, !ScalaLabel !50
  val add18 = Module (new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign=false))


  //  %13 = getelementptr inbounds i32, i32* %m2.in, i32 %12, !UID !51, !ScalaLabel !52
  val getelementptr19 = Module (new GepOneNode(NumOuts = 1, ID = 19)(numByte1 = 1))


  //  %14 = load i32, i32* %13, align 4, !UID !53, !ScalaLabel !54
  val load20 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=20,RouteID=1))


  //  %15 = mul nsw i32 %8, %14, !UID !55, !ScalaLabel !56
  val mul21 = Module (new ComputeNode(NumOuts = 1, ID = 21, opCode = "mul")(sign=false))


  //  %16 = add nsw i32 %2, %9, !UID !57, !ScalaLabel !58
  val add22 = Module (new ComputeNode(NumOuts = 1, ID = 22, opCode = "add")(sign=false))


  //  %17 = add nsw i32 %16, %add.in, !UID !59, !ScalaLabel !60
  val add23 = Module (new ComputeNode(NumOuts = 1, ID = 23, opCode = "add")(sign=false))


  //  %18 = getelementptr inbounds i32, i32* %prod.in, i32 %17, !UID !61, !ScalaLabel !62
  val getelementptr24 = Module (new GepOneNode(NumOuts = 2, ID = 24)(numByte1 = 1))


  //  %19 = load i32, i32* %18, align 4, !UID !63, !ScalaLabel !64
  val load25 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=25,RouteID=2))


  //  %20 = add nsw i32 %19, %15, !UID !65, !ScalaLabel !66
  val add26 = Module (new ComputeNode(NumOuts = 1, ID = 26, opCode = "add")(sign=false))


  //  store i32 %20, i32* %18, align 4, !UID !67, !ScalaLabel !68
  val store27 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=27,RouteID=0))


  //  br label %my_for.inc, !UID !69, !BB_UID !70, !ScalaLabel !71
  val br28 = Module (new UBranchNode(ID = 28))

  // [BasicBlock]  my_for.inc:

  //  %21 = add nsw i32 %9, 1, !UID !72, !ScalaLabel !73
  val add29 = Module (new ComputeNode(NumOuts = 1, ID = 29, opCode = "add")(sign=false))


  //  br label %my_for.cond22, !llvm.loop !74, !UID !97, !BB_UID !98, !ScalaLabel !99
  val br30 = Module (new UBranchNode(ID = 30))

  // [BasicBlock]  my_for.end:

  //  br label %my_for.inc33, !UID !100, !BB_UID !101, !ScalaLabel !102
  val br31 = Module (new UBranchNode(ID = 31))

  // [BasicBlock]  my_for.inc33:

  //  %22 = add nsw i32 %0, 1, !UID !103, !ScalaLabel !104
  val add32 = Module (new ComputeNode(NumOuts = 1, ID = 32, opCode = "add")(sign=false))


  //  br label %my_for.cond, !llvm.loop !105, !UID !108, !BB_UID !109, !ScalaLabel !110
  val br33 = Module (new UBranchNode(ID = 33))

  // [BasicBlock]  my_for.end35:

  //  br label %my_pfor.preattach, !UID !111, !BB_UID !112, !ScalaLabel !113
  val br34 = Module (new UBranchNode(ID = 34))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !114, !BB_UID !115, !ScalaLabel !116
  val ret35 = Module(new RetNode(retTypes=List(32), ID=35))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_bgemm_detach3_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_my_pfor_body15.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br0 to bb_my_loopk
  bb_my_loopk.io.predicateIn <> br0.io.Out(param.br0_brn_bb("bb_my_loopk"))


  //Connecting br1 to bb_my_for_cond
  bb_my_for_cond.io.predicateIn(param.bb_my_for_cond_pred("br1")) <> br1.io.Out(param.br1_brn_bb("bb_my_for_cond"))


  //Connecting br4 to bb_my_for_body
  bb_my_for_body.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_my_for_body"))


  //Connecting br4 to bb_my_for_end35
  bb_my_for_end35.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_my_for_end35"))


  //Connecting br12 to bb_my_loopj
  bb_my_loopj.io.predicateIn <> br12.io.Out(param.br12_brn_bb("bb_my_loopj"))


  //Connecting br13 to bb_my_for_cond22
  bb_my_for_cond22.io.predicateIn(param.bb_my_for_cond22_pred("br13")) <> br13.io.Out(param.br13_brn_bb("bb_my_for_cond22"))


  //Connecting br16 to bb_my_for_body24
  bb_my_for_body24.io.predicateIn <> br16.io.Out(param.br16_brn_bb("bb_my_for_body24"))


  //Connecting br16 to bb_my_for_end
  bb_my_for_end.io.predicateIn <> br16.io.Out(param.br16_brn_bb("bb_my_for_end"))


  //Connecting br28 to bb_my_for_inc
  bb_my_for_inc.io.predicateIn <> br28.io.Out(param.br28_brn_bb("bb_my_for_inc"))


  //Connecting br30 to bb_my_for_cond22
  bb_my_for_cond22.io.predicateIn(param.bb_my_for_cond22_pred("br30")) <> br30.io.Out(param.br30_brn_bb("bb_my_for_cond22"))


  //Connecting br31 to bb_my_for_inc33
  bb_my_for_inc33.io.predicateIn <> br31.io.Out(param.br31_brn_bb("bb_my_for_inc33"))


  //Connecting br33 to bb_my_for_cond
  bb_my_for_cond.io.predicateIn(param.bb_my_for_cond_pred("br33")) <> br33.io.Out(param.br33_brn_bb("bb_my_for_cond"))


  //Connecting br34 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn <> br34.io.Out(param.br34_brn_bb("bb_my_pfor_preattach"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_my_pfor_body15.io.Out(param.bb_my_pfor_body15_activate("br0"))



  br1.io.enable <> bb_my_loopk.io.Out(param.bb_my_loopk_activate("br1"))



  phi2.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("phi2"))

  icmp3.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("icmp3"))

  br4.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("br4"))



  mul5.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("mul5"))

  add6.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("add6"))

  mul7.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("mul7"))

  add8.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("add8"))

  add9.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("add9"))

  getelementptr10.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("getelementptr10"))

  load11.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("load11"))

  br12.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("br12"))



  br13.io.enable <> bb_my_loopj.io.Out(param.bb_my_loopj_activate("br13"))



  phi14.io.enable <> bb_my_for_cond22.io.Out(param.bb_my_for_cond22_activate("phi14"))

  icmp15.io.enable <> bb_my_for_cond22.io.Out(param.bb_my_for_cond22_activate("icmp15"))

  br16.io.enable <> bb_my_for_cond22.io.Out(param.bb_my_for_cond22_activate("br16"))



  add17.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("add17"))

  add18.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("add18"))

  getelementptr19.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("getelementptr19"))

  load20.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("load20"))

  mul21.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("mul21"))

  add22.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("add22"))

  add23.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("add23"))

  getelementptr24.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("getelementptr24"))

  load25.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("load25"))

  add26.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("add26"))

  store27.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("store27"))

  br28.io.enable <> bb_my_for_body24.io.Out(param.bb_my_for_body24_activate("br28"))



  add29.io.enable <> bb_my_for_inc.io.Out(param.bb_my_for_inc_activate("add29"))

  br30.io.enable <> bb_my_for_inc.io.Out(param.bb_my_for_inc_activate("br30"))



  br31.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("br31"))

  loop_L_0_liveIN_0.io.enable <> bb_my_for_end.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_my_for_end.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_my_for_end.io.Out(3)
  loop_L_0_liveIN_3.io.enable <> bb_my_for_end.io.Out(4)
  loop_L_0_liveIN_4.io.enable <> bb_my_for_end.io.Out(5)
  loop_L_0_liveIN_5.io.enable <> bb_my_for_end.io.Out(6)




  add32.io.enable <> bb_my_for_inc33.io.Out(param.bb_my_for_inc33_activate("add32"))

  br33.io.enable <> bb_my_for_inc33.io.Out(param.bb_my_for_inc33_activate("br33"))



  br34.io.enable <> bb_my_for_end35.io.Out(param.bb_my_for_end35_activate("br34"))

  loop_L_1_liveIN_0.io.enable <> bb_my_for_end35.io.Out(1)
  loop_L_1_liveIN_1.io.enable <> bb_my_for_end35.io.Out(2)
  loop_L_1_liveIN_2.io.enable <> bb_my_for_end35.io.Out(3)




  ret35.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret35"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32 %add.in
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field3")

  // Connecting function argument to the loop header
  //i32* %m2.in
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field4")

  // Connecting function argument to the loop header
  //i32* %prod.in
  loop_L_0_liveIN_2.io.InData <> InputSplitter.io.Out.data("field5")

  // Connecting instruction to the loop header
  //  %8 = load i32, i32* %7, align 4, !UID !32, !ScalaLabel !33
  loop_L_0_liveIN_3.io.InData <> load11.io.Out(param.mul21_in("load11"))

  // Connecting instruction to the loop header
  //  %2 = mul nsw i32 %i.0.in, 4, !UID !20, !ScalaLabel !21
  loop_L_0_liveIN_4.io.InData <> mul5.io.Out(param.add22_in("mul5"))

  // Connecting instruction to the loop header
  //  %4 = mul nsw i32 %3, 4, !UID !24, !ScalaLabel !25
  loop_L_0_liveIN_5.io.InData <> mul7.io.Out(param.add17_in("mul7"))

  // Connecting function argument to the loop header
  //i32 %i.0.in
  loop_L_1_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32 %add10.in
  loop_L_1_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32* %m1.in
  loop_L_1_liveIN_2.io.InData <> InputSplitter.io.Out.data("field2")



  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi2.io.InData(param.phi2_phi_in("const_0")).bits.data := 0.U
  phi2.io.InData(param.phi2_phi_in("const_0")).bits.predicate := true.B
  phi2.io.InData(param.phi2_phi_in("const_0")).valid := true.B

  phi2.io.InData(param.phi2_phi_in("add32")) <> add32.io.Out(param.phi2_in("add32"))

  phi14.io.InData(param.phi14_phi_in("const_0")).bits.data := 0.U
  phi14.io.InData(param.phi14_phi_in("const_0")).bits.predicate := true.B
  phi14.io.InData(param.phi14_phi_in("const_0")).valid := true.B

  phi14.io.InData(param.phi14_phi_in("add29")) <> add29.io.Out(param.phi14_in("add29"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi2.io.Mask <> bb_my_for_cond.io.MaskBB(0)

  phi14.io.Mask <> bb_my_for_cond22.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp3.io.LeftIO <> phi2.io.Out(param.icmp3_in("phi2"))

  // Wiring constant
  icmp3.io.RightIO.bits.data := 2.U
  icmp3.io.RightIO.bits.predicate := true.B
  icmp3.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br4.io.CmpIO <> icmp3.io.Out(param.br4_in("icmp3"))

  // Wiring Binary instruction to the loop header
  mul5.io.LeftIO <>loop_L_0_liveIN_0.io.Out(param.mul5_in("field0"))

  // Wiring constant
  mul5.io.RightIO.bits.data := 4.U
  mul5.io.RightIO.bits.predicate := true.B
  mul5.io.RightIO.valid := true.B

  // Wiring instructions
  add6.io.LeftIO <> phi2.io.Out(param.add6_in("phi2"))

  // Wiring Binary instruction to the loop header
  add6.io.RightIO <> loop_L_0_liveIN_1.io.Out(param.add6_in("field1"))

  // Wiring instructions
  mul7.io.LeftIO <> add6.io.Out(param.mul7_in("add6"))

  // Wiring constant
  mul7.io.RightIO.bits.data := 4.U
  mul7.io.RightIO.bits.predicate := true.B
  mul7.io.RightIO.valid := true.B

  // Wiring instructions
  add8.io.LeftIO <> mul5.io.Out(param.add8_in("mul5"))

  // Wiring instructions
  add8.io.RightIO <> phi2.io.Out(param.add8_in("phi2"))

  // Wiring instructions
  add9.io.LeftIO <> add8.io.Out(param.add9_in("add8"))

  // Wiring Binary instruction to the loop header
  add9.io.RightIO <> loop_L_0_liveIN_0.io.Out(param.add9_in("field1"))

  // Wiring GEP instruction to the loop header
  getelementptr10.io.baseAddress <> loop_L_0_liveIN_2.io.Out(param.getelementptr10_in("field2"))

  // Wiring GEP instruction to the parent instruction
  getelementptr10.io.idx1 <> add9.io.Out(param.getelementptr10_in("add9"))


  // Wiring Load instruction to the parent instruction
  load11.io.GepAddr <> getelementptr10.io.Out(param.load11_in("getelementptr10"))
  load11.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load11.io.memReq




  // Wiring instructions
  icmp15.io.LeftIO <> phi14.io.Out(param.icmp15_in("phi14"))

  // Wiring constant
  icmp15.io.RightIO.bits.data := 2.U
  icmp15.io.RightIO.bits.predicate := true.B
  icmp15.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br16.io.CmpIO <> icmp15.io.Out(param.br16_in("icmp15"))

  // Wiring instructions
  add17.io.LeftIO <> loop_L_0_liveIN_5.io.Out(param.add17_in("mul7"))

  // Wiring instructions
  add17.io.RightIO <> phi14.io.Out(param.add17_in("phi14"))

  // Wiring instructions
  add18.io.LeftIO <> add17.io.Out(param.add18_in("add17"))

  // Wiring Binary instruction to the loop header
  add18.io.RightIO <> loop_L_0_liveIN_0.io.Out(param.add18_in("field3"))

  // Wiring GEP instruction to the loop header
  getelementptr19.io.baseAddress <> loop_L_0_liveIN_1.io.Out(param.getelementptr19_in("field4"))

  // Wiring GEP instruction to the parent instruction
  getelementptr19.io.idx1 <> add18.io.Out(param.getelementptr19_in("add18"))


  // Wiring Load instruction to the parent instruction
  load20.io.GepAddr <> getelementptr19.io.Out(param.load20_in("getelementptr19"))
  load20.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load20.io.memReq




  // Wiring instructions
  mul21.io.LeftIO <> loop_L_0_liveIN_3.io.Out(param.mul21_in("load11"))

  // Wiring instructions
  mul21.io.RightIO <> load20.io.Out(param.mul21_in("load20"))

  // Wiring instructions
  add22.io.LeftIO <> loop_L_0_liveIN_4.io.Out(param.add22_in("mul5"))

  // Wiring instructions
  add22.io.RightIO <> phi14.io.Out(param.add22_in("phi14"))

  // Wiring instructions
  add23.io.LeftIO <> add22.io.Out(param.add23_in("add22"))

  // Wiring Binary instruction to the loop header
  add23.io.RightIO <> loop_L_0_liveIN_0.io.Out(param.add23_in("field3"))

  // Wiring GEP instruction to the loop header
  getelementptr24.io.baseAddress <> loop_L_0_liveIN_2.io.Out(param.getelementptr24_in("field5"))

  // Wiring GEP instruction to the parent instruction
  getelementptr24.io.idx1 <> add23.io.Out(param.getelementptr24_in("add23"))


  // Wiring Load instruction to the parent instruction
  load25.io.GepAddr <> getelementptr24.io.Out(param.load25_in("getelementptr24"))
  load25.io.memResp <> CacheMem.io.ReadOut(2)
  CacheMem.io.ReadIn(2) <> load25.io.memReq




  // Wiring instructions
  add26.io.LeftIO <> load25.io.Out(param.add26_in("load25"))

  // Wiring instructions
  add26.io.RightIO <> mul21.io.Out(param.add26_in("mul21"))

  store27.io.inData <> add26.io.Out(param.store27_in("add26"))



  // Wiring Store instruction to the parent instruction
  store27.io.GepAddr <> getelementptr24.io.Out(param.store27_in("getelementptr24"))
  store27.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store27.io.memReq
  store27.io.Out(0).ready := true.B



  // Wiring instructions
  add29.io.LeftIO <> phi14.io.Out(param.add29_in("phi14"))

  // Wiring constant
  add29.io.RightIO.bits.data := 1.U
  add29.io.RightIO.bits.predicate := true.B
  add29.io.RightIO.valid := true.B

  // Wiring instructions
  add32.io.LeftIO <> phi2.io.Out(param.add32_in("phi2"))

  // Wiring constant
  add32.io.RightIO.bits.data := 1.U
  add32.io.RightIO.bits.predicate := true.B
  add32.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret35.io.In.elements("field0").bits.data := 1.U
  ret35.io.In.elements("field0").bits.predicate := true.B
  ret35.io.In.elements("field0").valid := true.B
  io.out <> ret35.io.Out


}

import java.io.{File, FileWriter}
object bgemm_detach3Main extends App {
  val dir = new File("RTL/bgemm_detach3") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new bgemm_detach3DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


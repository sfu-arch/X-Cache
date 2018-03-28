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

object Data_stencil_inner_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_if_then_pred = Map(
    "br9" -> 0
  )


  val bb_if_end11_pred = Map(
    "br9" -> 0,
    "br23" -> 1
  )


  val bb_for_cond_pred = Map(
    "br0" -> 0,
    "br26" -> 1
  )


  val bb_for_inc_pred = Map(
    "br24" -> 0
  )


  val bb_for_body_pred = Map(
    "br3" -> 0
  )


  val bb_for_end_pred = Map(
    "br3" -> 0
  )


  val bb_if_then5_pred = Map(
    "br11" -> 0
  )


  val bb_if_end_pred = Map(
    "br11" -> 0,
    "br22" -> 1
  )


  val br0_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val br3_brn_bb = Map(
    "bb_for_body" -> 0,
    "bb_for_end" -> 1
  )


  val br9_brn_bb = Map(
    "bb_if_then" -> 0,
    "bb_if_end11" -> 1
  )


  val br11_brn_bb = Map(
    "bb_if_then5" -> 0,
    "bb_if_end" -> 1
  )


  val br22_brn_bb = Map(
    "bb_if_end" -> 0
  )


  val br23_brn_bb = Map(
    "bb_if_end11" -> 0
  )


  val br24_brn_bb = Map(
    "bb_for_inc" -> 0
  )


  val br26_brn_bb = Map(
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
    "add4" -> 0,
    "sub5" -> 1,
    "add6" -> 2,
    "sub7" -> 3,
    "icmp8" -> 4,
    "br9" -> 5
  )


  val bb_if_then_activate = Map(
    "icmp10" -> 0,
    "br11" -> 1
  )


  val bb_if_then5_activate = Map(
    "mul12" -> 0,
    "add13" -> 1,
    "getelementptr14" -> 2,
    "load15" -> 3,
    "mul16" -> 4,
    "add17" -> 5,
    "getelementptr18" -> 6,
    "load19" -> 7,
    "add20" -> 8,
    "store21" -> 9,
    "br22" -> 10
  )


  val bb_if_end_activate = Map(
    "br23" -> 0
  )


  val bb_if_end11_activate = Map(
    "br24" -> 0
  )


  val bb_for_inc_activate = Map(
    "add25" -> 0,
    "br26" -> 1
  )


  val bb_for_end_activate = Map(
    "ret27" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add25" -> 1
  )


  //  %nc.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add25" -> 0
  )


  //  %cmp = icmp ule i32 %nc.0, 2, !UID !12, !ScalaLabel !13
  val icmp2_in = Map(
    "phi1" -> 0
  )


  //  br i1 %cmp, label %for.body, label %for.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  %add = add i32 %i, %nr, !UID !17, !ScalaLabel !18
  val add4_in = Map(
    "field2" -> 0,
    "field4" -> 0
  )


  //  %sub = sub i32 %add, 1, !UID !19, !ScalaLabel !20
  val sub5_in = Map(
    "add4" -> 0
  )


  //  %add1 = add i32 %j, %nc.0, !UID !21, !ScalaLabel !22
  val add6_in = Map(
    "field3" -> 0,
    "phi1" -> 1
  )


  //  %sub2 = sub i32 %add1, 1, !UID !23, !ScalaLabel !24
  val sub7_in = Map(
    "add6" -> 0
  )


  //  %cmp3 = icmp ult i32 %sub, 4, !UID !25, !ScalaLabel !26
  val icmp8_in = Map(
    "sub5" -> 0
  )


  //  br i1 %cmp3, label %if.then, label %if.end11, !UID !27, !BB_UID !28, !ScalaLabel !29
  val br9_in = Map(
    "icmp8" -> 0
  )


  //  %cmp4 = icmp ult i32 %sub2, 4, !UID !30, !ScalaLabel !31
  val icmp10_in = Map(
    "sub7" -> 0
  )


  //  br i1 %cmp4, label %if.then5, label %if.end, !UID !32, !BB_UID !33, !ScalaLabel !34
  val br11_in = Map(
    "icmp10" -> 0
  )


  //  %mul = mul i32 %sub, 4, !UID !35, !ScalaLabel !36
  val mul12_in = Map(
    "sub5" -> 1
  )


  //  %add6 = add i32 %mul, %sub2, !UID !37, !ScalaLabel !38
  val add13_in = Map(
    "mul12" -> 0,
    "sub7" -> 1
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %in, i32 %add6, !UID !39, !ScalaLabel !40
  val getelementptr14_in = Map(
    "field0" -> 0,
    "add13" -> 0
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !41, !ScalaLabel !42
  val load15_in = Map(
    "getelementptr14" -> 0
  )


  //  %mul7 = mul i32 %i, 4, !UID !43, !ScalaLabel !44
  val mul16_in = Map(
    "field2" -> 1
  )


  //  %add8 = add i32 %mul7, %j, !UID !45, !ScalaLabel !46
  val add17_in = Map(
    "mul16" -> 0,
    "field3" -> 1
  )


  //  %arrayidx9 = getelementptr inbounds i32, i32* %out, i32 %add8, !UID !47, !ScalaLabel !48
  val getelementptr18_in = Map(
    "field1" -> 0,
    "add17" -> 0
  )


  //  %1 = load i32, i32* %arrayidx9, align 4, !UID !49, !ScalaLabel !50
  val load19_in = Map(
    "getelementptr18" -> 0
  )


  //  %add10 = add i32 %1, %0, !UID !51, !ScalaLabel !52
  val add20_in = Map(
    "load19" -> 0,
    "load15" -> 0
  )


  //  store i32 %add10, i32* %arrayidx9, align 4, !UID !53, !ScalaLabel !54
  val store21_in = Map(
    "add20" -> 0,
    "getelementptr18" -> 1
  )


  //  %inc = add i32 %nc.0, 1, !UID !64, !ScalaLabel !65
  val add25_in = Map(
    "phi1" -> 2
  )


  //  ret void, !UID !78, !BB_UID !79, !ScalaLabel !80
  val ret27_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class stencil_innerDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class stencil_innerDF(implicit p: Parameters) extends stencil_innerDFIO()(p) {



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

  val InputSplitter = Module(new SplitCall(List(32,32,32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 2, ID = 0))
  val loop_L_0_liveIN_3 = Module(new LiveInNode(NumOuts = 2, ID = 0))
  val loop_L_0_liveIN_4 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 2))

  val bb_if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_if_then5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 4))

  val bb_if_end = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 1, NumPhi=1,BID = 5))

  val bb_if_end11 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 1, NumPhi=1,BID = 6))

  val bb_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 7))

  val bb_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 8))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %for.cond, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  for.cond:

  //  %nc.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !10, !ScalaLabel !11
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 1))


  //  %cmp = icmp ule i32 %nc.0, 2, !UID !12, !ScalaLabel !13
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULE")(sign=false))


  //  br i1 %cmp, label %for.body, label %for.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new CBranchNode(ID = 3))

  // [BasicBlock]  for.body:

  //  %add = add i32 %i, %nr, !UID !17, !ScalaLabel !18
  val add4 = Module (new ComputeNode(NumOuts = 1, ID = 4, opCode = "add")(sign=false))


  //  %sub = sub i32 %add, 1, !UID !19, !ScalaLabel !20
  val sub5 = Module (new ComputeNode(NumOuts = 2, ID = 5, opCode = "sub")(sign=false))


  //  %add1 = add i32 %j, %nc.0, !UID !21, !ScalaLabel !22
  val add6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))


  //  %sub2 = sub i32 %add1, 1, !UID !23, !ScalaLabel !24
  val sub7 = Module (new ComputeNode(NumOuts = 2, ID = 7, opCode = "sub")(sign=false))


  //  %cmp3 = icmp ult i32 %sub, 4, !UID !25, !ScalaLabel !26
  val icmp8 = Module (new IcmpNode(NumOuts = 1, ID = 8, opCode = "ULT")(sign=false))


  //  br i1 %cmp3, label %if.then, label %if.end11, !UID !27, !BB_UID !28, !ScalaLabel !29
  val br9 = Module (new CBranchNode(ID = 9))

  // [BasicBlock]  if.then:

  //  %cmp4 = icmp ult i32 %sub2, 4, !UID !30, !ScalaLabel !31
  val icmp10 = Module (new IcmpNode(NumOuts = 1, ID = 10, opCode = "ULT")(sign=false))


  //  br i1 %cmp4, label %if.then5, label %if.end, !UID !32, !BB_UID !33, !ScalaLabel !34
  val br11 = Module (new CBranchNode(ID = 11))

  // [BasicBlock]  if.then5:

  //  %mul = mul i32 %sub, 4, !UID !35, !ScalaLabel !36
  val mul12 = Module (new ComputeNode(NumOuts = 1, ID = 12, opCode = "mul")(sign=false))


  //  %add6 = add i32 %mul, %sub2, !UID !37, !ScalaLabel !38
  val add13 = Module (new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign=false))


  //  %arrayidx = getelementptr inbounds i32, i32* %in, i32 %add6, !UID !39, !ScalaLabel !40
  val getelementptr14 = Module (new GepOneNode(NumOuts = 1, ID = 14)(numByte1 = 4))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !41, !ScalaLabel !42
  val load15 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=15,RouteID=0))


  //  %mul7 = mul i32 %i, 4, !UID !43, !ScalaLabel !44
  val mul16 = Module (new ComputeNode(NumOuts = 1, ID = 16, opCode = "mul")(sign=false))


  //  %add8 = add i32 %mul7, %j, !UID !45, !ScalaLabel !46
  val add17 = Module (new ComputeNode(NumOuts = 1, ID = 17, opCode = "add")(sign=false))


  //  %arrayidx9 = getelementptr inbounds i32, i32* %out, i32 %add8, !UID !47, !ScalaLabel !48
  val getelementptr18 = Module (new GepOneNode(NumOuts = 2, ID = 18)(numByte1 = 4))


  //  %1 = load i32, i32* %arrayidx9, align 4, !UID !49, !ScalaLabel !50
  val load19 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=19,RouteID=1))


  //  %add10 = add i32 %1, %0, !UID !51, !ScalaLabel !52
  val add20 = Module (new ComputeNode(NumOuts = 1, ID = 20, opCode = "add")(sign=false))


  //  store i32 %add10, i32* %arrayidx9, align 4, !UID !53, !ScalaLabel !54
  val store21 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=21,RouteID=0))


  //  br label %if.end, !UID !55, !BB_UID !56, !ScalaLabel !57
  val br22 = Module (new UBranchNode(ID = 22))

  // [BasicBlock]  if.end:

  //  br label %if.end11, !UID !58, !BB_UID !59, !ScalaLabel !60
  val br23 = Module (new UBranchNode(ID = 23))

  // [BasicBlock]  if.end11:

  //  br label %for.inc, !UID !61, !BB_UID !62, !ScalaLabel !63
  val br24 = Module (new UBranchNode(ID = 24))

  // [BasicBlock]  for.inc:

  //  %inc = add i32 %nc.0, 1, !UID !64, !ScalaLabel !65
  val add25 = Module (new ComputeNode(NumOuts = 1, ID = 25, opCode = "add")(sign=false))


  //  br label %for.cond, !llvm.loop !66, !UID !75, !BB_UID !76, !ScalaLabel !77
  val br26 = Module (new UBranchNode(ID = 26))

  // [BasicBlock]  for.end:

  //  ret void, !UID !78, !BB_UID !79, !ScalaLabel !80
  val ret27 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=27))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_stencil_inner_FlowParam



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
  bb_for_cond.io.activate <> br0.io.Out(param.br0_brn_bb("bb_for_cond"))


  //Connecting br3 to bb_for_body
  bb_for_body.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_for_body"))


  //Connecting br3 to bb_for_end
  bb_for_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_for_end"))


  //Connecting br9 to bb_if_then
  bb_if_then.io.predicateIn <> br9.io.Out(param.br9_brn_bb("bb_if_then"))


  //Connecting br9 to bb_if_end11
  bb_if_end11.io.predicateIn(0) <> br9.io.Out(param.br9_brn_bb("bb_if_end11"))


  //Connecting br11 to bb_if_then5
  bb_if_then5.io.predicateIn <> br11.io.Out(param.br11_brn_bb("bb_if_then5"))


  //Connecting br11 to bb_if_end
  bb_if_end.io.predicateIn(0) <> br11.io.Out(param.br11_brn_bb("bb_if_end"))


  //Connecting br22 to bb_if_end
  bb_if_end.io.predicateIn(1) <> br22.io.Out(param.br22_brn_bb("bb_if_end"))
  bb_if_end.io.MaskBB(0).ready := true.B

  //Connecting br23 to bb_if_end11
  bb_if_end11.io.predicateIn(1) <> br23.io.Out(param.br23_brn_bb("bb_if_end11"))
  bb_if_end11.io.MaskBB(0).ready := true.B


  //Connecting br24 to bb_for_inc
  bb_for_inc.io.predicateIn <> br24.io.Out(param.br24_brn_bb("bb_for_inc"))


  //Connecting br26 to bb_for_cond
  bb_for_cond.io.loopBack <> br26.io.Out(param.br26_brn_bb("bb_for_cond"))



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



  add4.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("add4"))

  sub5.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("sub5"))

  add6.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("add6"))

  sub7.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("sub7"))

  icmp8.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("icmp8"))

  br9.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("br9"))



  icmp10.io.enable <> bb_if_then.io.Out(param.bb_if_then_activate("icmp10"))

  br11.io.enable <> bb_if_then.io.Out(param.bb_if_then_activate("br11"))



  mul12.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("mul12"))

  add13.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("add13"))

  getelementptr14.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("getelementptr14"))

  load15.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("load15"))

  mul16.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("mul16"))

  add17.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("add17"))

  getelementptr18.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("getelementptr18"))

  load19.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("load19"))

  add20.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("add20"))

  store21.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("store21"))

  br22.io.enable <> bb_if_then5.io.Out(param.bb_if_then5_activate("br22"))



  br23.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("br23"))



  br24.io.enable <> bb_if_end11.io.Out(param.bb_if_end11_activate("br24"))



  add25.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("add25"))

  br26.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("br26"))



  ret27.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("ret27"))

  loop_L_0_liveIN_0.io.enable <> bb_for_end.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_for_end.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_for_end.io.Out(3)
  loop_L_0_liveIN_3.io.enable <> bb_for_end.io.Out(4)
  loop_L_0_liveIN_4.io.enable <> bb_for_end.io.Out(5)






  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %in
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32* %out
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32 %i
  loop_L_0_liveIN_2.io.InData <> InputSplitter.io.Out.data("field2")

  // Connecting function argument to the loop header
  //i32 %j
  loop_L_0_liveIN_3.io.InData <> InputSplitter.io.Out.data("field3")

  // Connecting function argument to the loop header
  //i32 %nr
  loop_L_0_liveIN_4.io.InData <> InputSplitter.io.Out.data("field4")



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

  phi1.io.InData(param.phi1_phi_in("add25")) <> add25.io.Out(param.phi1_in("add25"))

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

  // Wiring constant
  icmp2.io.RightIO.bits.data := 2.U
  icmp2.io.RightIO.bits.predicate := true.B
  icmp2.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring Binary instruction to the loop header
  add4.io.LeftIO <> loop_L_0_liveIN_2.io.Out(0)

  // Wiring Binary instruction to the loop header
  add4.io.RightIO <> loop_L_0_liveIN_4.io.Out(0)

  // Wiring instructions
  sub5.io.LeftIO <> add4.io.Out(param.sub5_in("add4"))

  // Wiring constant
  sub5.io.RightIO.bits.data := 1.U
  sub5.io.RightIO.bits.predicate := true.B
  sub5.io.RightIO.valid := true.B

  // Wiring Binary instruction to the loop header
  add6.io.LeftIO <>loop_L_0_liveIN_3.io.Out(0)

  // Wiring instructions
  add6.io.RightIO <> phi1.io.Out(param.add6_in("phi1"))

  // Wiring instructions
  sub7.io.LeftIO <> add6.io.Out(param.sub7_in("add6"))

  // Wiring constant
  sub7.io.RightIO.bits.data := 1.U
  sub7.io.RightIO.bits.predicate := true.B
  sub7.io.RightIO.valid := true.B

  // Wiring instructions
  icmp8.io.LeftIO <> sub5.io.Out(param.icmp8_in("sub5"))

  // Wiring constant
  icmp8.io.RightIO.bits.data := 4.U
  icmp8.io.RightIO.bits.predicate := true.B
  icmp8.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br9.io.CmpIO <> icmp8.io.Out(param.br9_in("icmp8"))

  // Wiring instructions
  icmp10.io.LeftIO <> sub7.io.Out(param.icmp10_in("sub7"))

  // Wiring constant
  icmp10.io.RightIO.bits.data := 4.U
  icmp10.io.RightIO.bits.predicate := true.B
  icmp10.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br11.io.CmpIO <> icmp10.io.Out(param.br11_in("icmp10"))

  // Wiring instructions
  mul12.io.LeftIO <> sub5.io.Out(param.mul12_in("sub5"))

  // Wiring constant
  mul12.io.RightIO.bits.data := 4.U
  mul12.io.RightIO.bits.predicate := true.B
  mul12.io.RightIO.valid := true.B

  // Wiring instructions
  add13.io.LeftIO <> mul12.io.Out(param.add13_in("mul12"))

  // Wiring instructions
  add13.io.RightIO <> sub7.io.Out(param.add13_in("sub7"))

  // Wiring GEP instruction to the loop header
  getelementptr14.io.baseAddress <> loop_L_0_liveIN_0.io.Out(0)

  // Wiring GEP instruction to the parent instruction
  getelementptr14.io.idx1 <> add13.io.Out(param.getelementptr14_in("add13"))


  // Wiring Load instruction to the parent instruction
  load15.io.GepAddr <> getelementptr14.io.Out(param.load15_in("getelementptr14"))
  load15.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load15.io.memReq




  // Wiring Binary instruction to the loop header
  mul16.io.LeftIO <>loop_L_0_liveIN_2.io.Out(1) // Manual fix

  // Wiring constant
  mul16.io.RightIO.bits.data := 4.U
  mul16.io.RightIO.bits.predicate := true.B
  mul16.io.RightIO.valid := true.B

  // Wiring instructions
  add17.io.LeftIO <> mul16.io.Out(param.add17_in("mul16"))

  // Wiring Binary instruction to the loop header
  add17.io.RightIO <> loop_L_0_liveIN_3.io.Out(1) // Manual fix

  // Wiring GEP instruction to the loop header
  getelementptr18.io.baseAddress <> loop_L_0_liveIN_1.io.Out(param.getelementptr18_in("field1"))

  // Wiring GEP instruction to the parent instruction
  getelementptr18.io.idx1 <> add17.io.Out(param.getelementptr18_in("add17"))


  // Wiring Load instruction to the parent instruction
  load19.io.GepAddr <> getelementptr18.io.Out(param.load19_in("getelementptr18"))
  load19.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load19.io.memReq




  // Wiring instructions
  add20.io.LeftIO <> load19.io.Out(param.add20_in("load19"))

  // Wiring instructions
  add20.io.RightIO <> load15.io.Out(param.add20_in("load15"))

  store21.io.inData <> add20.io.Out(param.store21_in("add20"))



  // Wiring Store instruction to the parent instruction
  store21.io.GepAddr <> getelementptr18.io.Out(param.store21_in("getelementptr18"))
  store21.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store21.io.memReq
  store21.io.Out(0).ready := true.B



  // Wiring instructions
  add25.io.LeftIO <> phi1.io.Out(param.add25_in("phi1"))

  // Wiring constant
  add25.io.RightIO.bits.data := 1.U
  add25.io.RightIO.bits.predicate := true.B
  add25.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  ret27.io.predicateIn(0).bits.control := true.B
  ret27.io.predicateIn(0).bits.taskID := 0.U
  ret27.io.predicateIn(0).valid := true.B
  ret27.io.In.data("field0").bits.data := 1.U
  ret27.io.In.data("field0").bits.predicate := true.B
  ret27.io.In.data("field0").valid := true.B
  io.out <> ret27.io.Out


}

import java.io.{File, FileWriter}
object stencil_innerMain extends App {
  val dir = new File("RTL/stencil_inner") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new stencil_innerDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


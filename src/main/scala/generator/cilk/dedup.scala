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

object Data_dedup_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_while_cond_pred = Map(
    "br3" -> 0,
    "br15" -> 1
  )


  val bb_while_body_pred = Map(
    "br9" -> 0
  )


  val bb_while_end_pred = Map(
    "br9" -> 0
  )


  val br3_brn_bb = Map(
    "bb_while_cond" -> 0
  )


  val br9_brn_bb = Map(
    "bb_while_body" -> 0,
    "bb_while_end" -> 1
  )


  val br15_brn_bb = Map(
    "bb_while_cond" -> 0
  )


  val bb_det_achd1_pred = Map(
    "detach10" -> 0
  )


  val bb_det_achd_pred = Map(
    "detach0" -> 0
  )


  val bb_det_cont_pred = Map(
    "detach0" -> 0
  )


  val bb_det_cont2_pred = Map(
    "detach10" -> 0
  )


  val detach0_brn_bb = Map(
    "bb_det_achd" -> 0,
    "bb_det_cont" -> 1
  )


  val detach10_brn_bb = Map(
    "bb_det_achd1" -> 0,
    "bb_det_cont2" -> 1
  )


  val bb_entry_activate = Map(
    "detach0" -> 0
  )


  val bb_det_achd_activate = Map(
    "call1" -> 0,
    "reattach2" -> 1
  )


  val bb_det_cont_activate = Map(
    "br3" -> 0
  )


  val bb_while_cond_activate = Map(
    "phi4" -> 0,
    "phi5" -> 1,
    "getelementptr6" -> 2,
    "load7" -> 3,
    "icmp8" -> 4,
    "br9" -> 5
  )


  val bb_while_body_activate = Map(
    "detach10" -> 0
  )


  val bb_det_achd1_activate = Map(
    "call11" -> 0,
    "reattach12" -> 1
  )


  val bb_det_cont2_activate = Map(
    "add13" -> 0,
    "add14" -> 1,
    "br15" -> 2
  )


  val bb_while_end_activate = Map(
    "urem16" -> 0,
    "getelementptr17" -> 1,
    "store18" -> 2,
    "ret19" -> 3
  )


  val phi4_phi_in = Map(
    "const_0" -> 0,
    "add13" -> 1
  )


  val phi5_phi_in = Map(
    "const_0" -> 0,
    "add14" -> 1
  )


  //  detach label %det.achd, label %det.cont, !UID !16, !BB_UID !17, !ScalaLabel !18
  val detach0_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  call void @S4(i32* %x, i32* %y), !UID !19, !ScalaLabel !20
  val call1_in = Map(
    "field0" -> 0,
    "field1" -> 0,
    "" -> 2
  )


  //  reattach label %det.cont, !UID !21, !BB_UID !22, !ScalaLabel !23
  val reattach2_in = Map(
    "" -> 3
  )


  //  %pos.0 = phi i32 [ 0, %det.cont ], [ %add, %det.cont2 ], !UID !27, !ScalaLabel !28
  val phi4_in = Map(
    "add13" -> 0
  )


  //  %iter.0 = phi i32 [ 0, %det.cont ], [ %inc, %det.cont2 ], !UID !29, !ScalaLabel !30
  val phi5_in = Map(
    "add14" -> 0
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %x, i32 %pos.0, !UID !31, !ScalaLabel !32
  val getelementptr6_in = Map(
    "field0" -> 1,
    "phi4" -> 0
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !33, !ScalaLabel !34
  val load7_in = Map(
    "getelementptr6" -> 0
  )


  //  %cmp = icmp ne i32 %0, 0, !UID !35, !ScalaLabel !36
  val icmp8_in = Map(
    "load7" -> 0
  )


  //  br i1 %cmp, label %while.body, label %while.end, !UID !37, !BB_UID !38, !ScalaLabel !39
  val br9_in = Map(
    "icmp8" -> 0
  )


  //  detach label %det.achd1, label %det.cont2, !UID !40, !BB_UID !41, !ScalaLabel !42
  val detach10_in = Map(
    "" -> 4,
    "" -> 5
  )


  //  call void @S2(i32* %x, i32 %pos.0, i32 %iter.0), !UID !43, !ScalaLabel !44
  val call11_in = Map(
    "field0" -> 2,
    "phi4" -> 1,
    "phi5" -> 0,
    "" -> 6
  )


  //  reattach label %det.cont2, !UID !45, !BB_UID !46, !ScalaLabel !47
  val reattach12_in = Map(
    "" -> 7
  )


  //  %add = add nsw i32 %pos.0, 2, !UID !48, !ScalaLabel !49
  val add13_in = Map(
    "phi4" -> 2
  )


  //  %inc = add i32 %iter.0, 1, !UID !50, !ScalaLabel !51
  val add14_in = Map(
    "phi5" -> 1
  )


  //  %rem = urem i32 %iter.0, 100, !UID !62, !ScalaLabel !63
  val urem16_in = Map(
    "phi5" -> 2
  )


  //  %arrayidx3 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !64, !ScalaLabel !65
  val getelementptr17_in = Map(
    "" -> 8,
    "urem16" -> 0
  )


  //  store i32 -10, i32* %arrayidx3, align 4, !UID !66, !ScalaLabel !67
  val store18_in = Map(
    "getelementptr17" -> 0
  )


  //  ret void, !UID !68, !BB_UID !69, !ScalaLabel !70
  val ret19_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class dedupDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call1_out = Decoupled(new Call(List(32,32)))
    val call1_in = Flipped(Decoupled(new Call(List(32))))
    val call11_out = Decoupled(new Call(List(32,32,32)))
    val call11_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class dedupDF(implicit p: Parameters) extends dedupDFIO()(p) {



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

  // Manually added
  val field0_expand = Module(new ExpandNode(NumOuts=2,ID=100)(new DataBundle))
  field0_expand.io.enable.valid := true.B
  field0_expand.io.enable.bits.control := true.B
  field0_expand.io.InData <> InputSplitter.io.Out.data("field0")



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 2, ID = 0))

  //val loop_L_0_LiveOut_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_det_achd = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 1))

  val bb_det_cont = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 2))

  val bb_while_cond = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 6, NumPhi = 2, BID = 3))

  val bb_while_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_det_achd1 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5))

  val bb_det_cont2 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 3, BID = 6))

  val bb_while_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 7)) // Manually corrected






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  detach label %det.achd, label %det.cont, !UID !16, !BB_UID !17, !ScalaLabel !18
  val detach0 = Module(new Detach(ID = 0))

  // [BasicBlock]  det.achd:

  //  call void @S4(i32* %x, i32* %y), !UID !19, !ScalaLabel !20
  val call1 = Module(new CallNode(ID=1,argTypes=List(32,32),retTypes=List(32)))


  //  reattach label %det.cont, !UID !21, !BB_UID !22, !ScalaLabel !23
  val reattach2 = Module(new Reattach(NumPredIn=1, ID=2))

  // [BasicBlock]  det.cont:

  //  br label %while.cond, !UID !24, !BB_UID !25, !ScalaLabel !26
  val br3 = Module (new UBranchNode(ID = 3))

  // [BasicBlock]  while.cond:

  //  %pos.0 = phi i32 [ 0, %det.cont ], [ %add, %det.cont2 ], !UID !27, !ScalaLabel !28
  val phi4 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 4))


  //  %iter.0 = phi i32 [ 0, %det.cont ], [ %inc, %det.cont2 ], !UID !29, !ScalaLabel !30
  val phi5 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 5))


  //  %arrayidx = getelementptr inbounds i32, i32* %x, i32 %pos.0, !UID !31, !ScalaLabel !32
  val getelementptr6 = Module (new GepOneNode(NumOuts = 1, ID = 6)(numByte1 = 4))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !33, !ScalaLabel !34
  val load7 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=0))


  //  %cmp = icmp ne i32 %0, 0, !UID !35, !ScalaLabel !36
  val icmp8 = Module (new IcmpNode(NumOuts = 1, ID = 8, opCode = "NE")(sign=false))


  //  br i1 %cmp, label %while.body, label %while.end, !UID !37, !BB_UID !38, !ScalaLabel !39
  val br9 = Module (new CBranchFastNode(ID = 9))

  // [BasicBlock]  while.body:

  //  detach label %det.achd1, label %det.cont2, !UID !40, !BB_UID !41, !ScalaLabel !42
  val detach10 = Module(new Detach(ID = 10))

  // [BasicBlock]  det.achd1:

  //  call void @S2(i32* %x, i32 %pos.0, i32 %iter.0), !UID !43, !ScalaLabel !44
  val call11 = Module(new CallNode(ID=11,argTypes=List(32,32,32),retTypes=List(32)))


  //  reattach label %det.cont2, !UID !45, !BB_UID !46, !ScalaLabel !47
  val reattach12 = Module(new Reattach(NumPredIn=1, ID=12))

  // [BasicBlock]  det.cont2:

  //  %add = add nsw i32 %pos.0, 2, !UID !48, !ScalaLabel !49
  val add13 = Module (new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign=false))


  //  %inc = add i32 %iter.0, 1, !UID !50, !ScalaLabel !51
  val add14 = Module (new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign=false))


  //  br label %while.cond, !llvm.loop !52, !UID !59, !BB_UID !60, !ScalaLabel !61
  val br15 = Module (new UBranchFastNode(ID = 15))

  // [BasicBlock]  while.end:

  //  %rem = urem i32 %iter.0, 100, !UID !62, !ScalaLabel !63
  val urem16 = Module (new ComputeNode(NumOuts = 1, ID = 16, opCode = "urem")(sign=false))


  //  %arrayidx3 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !64, !ScalaLabel !65
  val getelementptr17 = Module (new GepTwoNode(NumOuts = 1, ID = 17)(numByte1 = 4, numByte2 = 4))


  //  store i32 -10, i32* %arrayidx3, align 4, !UID !66, !ScalaLabel !67
  val store18 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=18,RouteID=0))


  //  ret void, !UID !68, !BB_UID !69, !ScalaLabel !70
  val ret19 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=19))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_dedup_FlowParam



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

  //Connecting br3 to bb_while_cond
  bb_while_cond.io.predicateIn(param.bb_while_cond_pred("br3")) <> br3.io.Out(param.br3_brn_bb("bb_while_cond"))


  //Connecting br9 to bb_while_body
  bb_while_body.io.predicateIn <> br9.io.Out(param.br9_brn_bb("bb_while_body"))


  //Connecting br9 to bb_while_end
  bb_while_end.io.predicateIn <> br9.io.Out(param.br9_brn_bb("bb_while_end"))


  //Connecting br15 to bb_while_cond
  bb_while_cond.io.predicateIn(param.bb_while_cond_pred("br15")) <> br15.io.Out(param.br15_brn_bb("bb_while_cond"))


  //Connecting detach0 to bb_det_achd
  bb_det_achd.io.predicateIn <> detach0.io.Out(param.detach0_brn_bb("bb_det_achd"))


  //Connecting detach0 to bb_det_cont
  bb_det_cont.io.predicateIn <> detach0.io.Out(param.detach0_brn_bb("bb_det_cont"))


  //Connecting detach10 to bb_det_achd1
  bb_det_achd1.io.predicateIn <> detach10.io.Out(param.detach10_brn_bb("bb_det_achd1"))


  //Connecting detach10 to bb_det_cont2
  bb_det_cont2.io.predicateIn <> detach10.io.Out(param.detach10_brn_bb("bb_det_cont2"))




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  detach0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("detach0"))



  call1.io.In.enable <> bb_det_achd.io.Out(param.bb_det_achd_activate("call1"))

  // Manual fix.  Reattach should only depend on its increment/decr inputs. If it is connected to the BB
  // Enable it will stall the loop
//  reattach2.io.enable <> bb_det_achd.io.Out(param.bb_det_achd_activate("reattach2"))
  reattach2.io.enable.enq(ControlBundle.active())
  bb_det_achd.io.Out(param.bb_det_achd_activate("reattach2")).ready := true.B


  br3.io.enable <> bb_det_cont.io.Out(param.bb_det_cont_activate("br3"))



  phi4.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("phi4"))

  phi5.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("phi5"))

  getelementptr6.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("getelementptr6"))

  load7.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("load7"))

  icmp8.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("icmp8"))

  br9.io.enable <> bb_while_cond.io.Out(param.bb_while_cond_activate("br9"))



  detach10.io.enable <> bb_while_body.io.Out(param.bb_while_body_activate("detach10"))



  call11.io.In.enable <> bb_det_achd1.io.Out(param.bb_det_achd1_activate("call11"))

  // Manual fix.  Reattach should only depend on its increment/decr inputs. If it is connected to the BB
  // Enable it will stall the loop
//  reattach12.io.enable <> bb_det_achd1.io.Out(param.bb_det_achd1_activate("reattach12"))
  reattach12.io.enable.enq(ControlBundle.active())  // always enabled
  bb_det_achd1.io.Out(param.bb_det_achd1_activate("reattach12")).ready := true.B



  add13.io.enable <> bb_det_cont2.io.Out(param.bb_det_cont2_activate("add13"))

  add14.io.enable <> bb_det_cont2.io.Out(param.bb_det_cont2_activate("add14"))

  br15.io.enable <> bb_det_cont2.io.Out(param.bb_det_cont2_activate("br15"))



  urem16.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("urem16"))

  getelementptr17.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("getelementptr17"))

  store18.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("store18"))

  ret19.io.enable <> bb_while_end.io.Out(param.bb_while_end_activate("ret19"))

  loop_L_0_liveIN_0.io.enable <> bb_while_end.io.Out(4)

  //loop_L_0_LiveOut_0.io.enable <> bb_while_end.io.Out(5) // Manually removed





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %x
  loop_L_0_liveIN_0.io.InData <> field0_expand.io.Out(0)



  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi4.io.InData(param.phi4_phi_in("const_0")).bits.data := 0.U
  phi4.io.InData(param.phi4_phi_in("const_0")).bits.predicate := true.B
  phi4.io.InData(param.phi4_phi_in("const_0")).valid := true.B

  phi4.io.InData(param.phi4_phi_in("add13")) <> add13.io.Out(param.phi4_in("add13"))

  phi5.io.InData(param.phi5_phi_in("const_0")).bits.data := 0.U
  phi5.io.InData(param.phi5_phi_in("const_0")).bits.predicate := true.B
  phi5.io.InData(param.phi5_phi_in("const_0")).valid := true.B

  phi5.io.InData(param.phi5_phi_in("add14")) <> add14.io.Out(param.phi5_in("add14"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi4.io.Mask <> bb_while_cond.io.MaskBB(0)

  phi5.io.Mask <> bb_while_cond.io.MaskBB(1)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Call to I/O
  io.call1_out <> call1.io.callOut
  call1.io.retIn <> io.call1_in
  call1.io.Out.enable.ready := true.B // Manual fix
  // Wiring Call to the function argument
  call1.io.In.data("field0") <> field0_expand.io.Out(1)

  // Wiring Call to the function argument
  call1.io.In.data("field1") <> InputSplitter.io.Out.data("field1")



  // Wiring GEP instruction to the loop header
  getelementptr6.io.baseAddress <> loop_L_0_liveIN_0.io.Out(param.getelementptr6_in("field0"))

  // Wiring GEP instruction to the parent instruction
  getelementptr6.io.idx1 <> phi4.io.Out(param.getelementptr6_in("phi4"))


  // Wiring Load instruction to the parent instruction
  load7.io.GepAddr <> getelementptr6.io.Out(param.load7_in("getelementptr6"))
  load7.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load7.io.memReq




  // Wiring instructions
  icmp8.io.LeftIO <> load7.io.Out(param.icmp8_in("load7"))

  // Wiring constant
  icmp8.io.RightIO.bits.data := 0.U
  icmp8.io.RightIO.bits.predicate := true.B
  icmp8.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br9.io.CmpIO <> icmp8.io.Out(param.br9_in("icmp8"))

  // Wiring Call to I/O
  io.call11_out <> call11.io.callOut
  call11.io.retIn <> io.call11_in
  call11.io.Out.enable.ready := true.B // Manual fix
  // Wiring Call instruction to the loop header
  call11.io.In.data("field0") <> loop_L_0_liveIN_0.io.Out(0)

  // Wiring Call instruction to the loop header
  call11.io.In.data("field1") <> phi4.io.Out(param.call11_in("phi4"))

  // Wiring Call instruction to the loop header
  call11.io.In.data("field2") <> phi5.io.Out(param.call11_in("phi5"))




  // Wiring instructions
  add13.io.LeftIO <> phi4.io.Out(param.add13_in("phi4"))

  // Wiring constant
  add13.io.RightIO.bits.data := 2.U
  add13.io.RightIO.bits.predicate := true.B
  add13.io.RightIO.valid := true.B

  // Wiring instructions
  add14.io.LeftIO <> phi5.io.Out(param.add14_in("phi5"))

  // Wiring constant
  add14.io.RightIO.bits.data := 1.U
  add14.io.RightIO.bits.predicate := true.B
  add14.io.RightIO.valid := true.B

  // Wiring instructions
  urem16.io.LeftIO <> phi5.io.Out(param.urem16_in("phi5"))

  // Wiring constant
  urem16.io.RightIO.bits.data := 100.U
  urem16.io.RightIO.bits.predicate := true.B
  urem16.io.RightIO.valid := true.B


  // Manually added.  Pointer to q[] which is global
  getelementptr17.io.baseAddress.valid := true.B
  getelementptr17.io.baseAddress.bits.predicate :=  true.B
  getelementptr17.io.baseAddress.bits.data :=  0.U

  // Wiring GEP instruction to the Constant
  getelementptr17.io.idx1.valid :=  true.B
  getelementptr17.io.idx1.bits.predicate :=  true.B
  getelementptr17.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the parent instruction
  getelementptr17.io.idx2 <> urem16.io.Out(param.getelementptr17_in("urem16"))


  // Wiring constant instructions to store
  store18.io.inData.bits.data := (0.U(32.W)-10.U(32.W))
  store18.io.inData.bits.predicate := true.B
  store18.io.inData.valid := true.B



  // Wiring Store instruction to the parent instruction
  store18.io.GepAddr <> getelementptr17.io.Out(param.store18_in("getelementptr17"))
  store18.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store18.io.memReq
  store18.io.Out(0).ready := true.B

  reattach2.io.predicateIn(0) <> call1.io.Out.data("field0")
  reattach12.io.predicateIn(0) <> call11.io.Out.data("field0")

  // No sync so detach/reattach are tied off
  detach0.io.Out(2).ready := true.B
  detach10.io.Out(2).ready := true.B
  reattach2.io.Out(0).ready := true.B
  reattach12.io.Out(0).ready := true.B

  /**
    * Connecting Dataflow signals
    */
  ret19.io.predicateIn(0).bits.control := true.B
  ret19.io.predicateIn(0).bits.taskID := 0.U
  ret19.io.predicateIn(0).valid := true.B
  ret19.io.In.data("field0").bits.data := 1.U
  ret19.io.In.data("field0").bits.predicate := true.B
  ret19.io.In.data("field0").valid := true.B
  io.out <> ret19.io.Out


}

class dedupTopIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class dedupTop()(implicit p: Parameters) extends dedupTopIO  {

  val S2Tiles = 1
  val S3Tiles = 3
  val S4Tiles = 1
  val dedup = Module(new dedupDF())
  val dedup_S2 = for (i <- 0 until S2Tiles) yield {
    val S2 = Module(new S2DF())
    S2
  }
  val dedup_S3 = for (i <- 0 until S3Tiles) yield {
    val S3 = Module(new S3DF())
    S3
  }
  val dedup_S4 = for (i <- 0 until S4Tiles) yield {
    val S4 = Module(new S4DF())
    S4
  }
  val S2TC = Module(new TaskController(List(32,32,32), List(32), 1, S2Tiles))
  val S3TC = Module(new TaskController(List(32,32,32), List(32), S2Tiles, S3Tiles))
  val S4TC = Module(new TaskController(List(32,32), List(32), 1, S4Tiles))

  // Connect cache interfaces to a cache arbiter
  val CacheArbiter = Module(new CacheArbiter(1+S2Tiles+S3Tiles+S4Tiles))
  CacheArbiter.io.cpu.CacheReq(0) <> dedup.io.CacheReq
  dedup.io.CacheResp <> CacheArbiter.io.cpu.CacheResp(0)
  for (i <- 0 until S2Tiles) {
    CacheArbiter.io.cpu.CacheReq(i+1) <> dedup_S2(i).io.CacheReq
    dedup_S2(i).io.CacheResp <> CacheArbiter.io.cpu.CacheResp(i+1)
  }
  for (i <- 0 until S3Tiles) {
    CacheArbiter.io.cpu.CacheReq(i+1+S2Tiles) <> dedup_S3(i).io.CacheReq
    dedup_S3(i).io.CacheResp <> CacheArbiter.io.cpu.CacheResp(i+1+S2Tiles)
  }
  for (i <- 0 until S4Tiles) {
    CacheArbiter.io.cpu.CacheReq(i+1+S2Tiles+S3Tiles) <> dedup_S4(i).io.CacheReq
    dedup_S4(i).io.CacheResp <> CacheArbiter.io.cpu.CacheResp(i+1+S2Tiles+S3Tiles)
  }
  io.CacheReq <> CacheArbiter.io.cache.CacheReq
  CacheArbiter.io.cache.CacheResp <> io.CacheResp

  // tester to dedup
  dedup.io.in <> io.in

  // Task Controllers
  S4TC.io.parentIn(0) <> dedup.io.call1_out
  dedup.io.call1_in <> S4TC.io.parentOut(0)
  for(i <- 0 until S4Tiles) {
    dedup_S4(i).io.in <> S4TC.io.childOut(i)
    S4TC.io.childIn(i) <> dedup_S4(i).io.out
  }

  S2TC.io.parentIn(0) <> dedup.io.call11_out
  dedup.io.call11_in <> S2TC.io.parentOut(0)
  for(i <- 0 until S2Tiles) {
    dedup_S2(i).io.in <> S2TC.io.childOut(i)
    S2TC.io.childIn(i) <> dedup_S2(i).io.out
    S3TC.io.parentIn(i) <> dedup_S2(i).io.call13_out
    dedup_S2(i).io.call13_in <> S3TC.io.parentOut(i)
  }
  for(i <- 0 until S3Tiles) {
    dedup_S3(i).io.in <> S3TC.io.childOut(i)
    S3TC.io.childIn(i) <> dedup_S3(i).io.out
  }


  // dedup to tester
  io.out <> dedup.io.out

}

import java.io.{File, FileWriter}
object dedupMain extends App {
  val dir = new File("RTL/dedup") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new dedupTop()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


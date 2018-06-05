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

object Data_S2_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_if_then3_pred = Map(
    "br11" -> 0
  )


  val bb_if_then_pred = Map(
    "br6" -> 0
  )


  val bb_if_else_pred = Map(
    "br6" -> 0
  )


  val bb_if_end_pred = Map(
    "br7" -> 0,
    "br8" -> 1
  )


  val bb_if_else4_pred = Map(
    "br11" -> 0
  )


  val bb_if_end6_pred = Map(
    "br15" -> 0,
    "br19" -> 1
  )


  val br6_brn_bb = Map(
    "bb_if_then" -> 0,
    "bb_if_else" -> 1
  )


  val br7_brn_bb = Map(
    "bb_if_end" -> 0
  )


  val br8_brn_bb = Map(
    "bb_if_end" -> 0
  )


  val br11_brn_bb = Map(
    "bb_if_then3" -> 1,
    "bb_if_else4" -> 0
  )


  val br15_brn_bb = Map(
    "bb_if_end6" -> 0
  )


  val br19_brn_bb = Map(
    "bb_if_end6" -> 0
  )


  val bb_det_achd_pred = Map(
    "detach12" -> 0
  )


  val bb_det_cont_pred = Map(
    "detach12" -> 0
  )


  val detach12_brn_bb = Map(
    "bb_det_achd" -> 0,
    "bb_det_cont" -> 1
  )


  val bb_entry_activate = Map(
    "getelementptr0" -> 0,
    "getelementptr1" -> 1,
    "load2" -> 2,
    "getelementptr3" -> 3,
    "load4" -> 4,
    "icmp5" -> 5,
    "br6" -> 6
  )


  val bb_if_then_activate = Map(
    "br7" -> 0
  )


  val bb_if_else_activate = Map(
    "br8" -> 0
  )


  val bb_if_end_activate = Map(
    "phi9" -> 0,
    "icmp10" -> 1,
    "br11" -> 2
  )


  val bb_if_then3_activate = Map(
    "detach12" -> 0
  )


  val bb_det_achd_activate = Map(
    "call13" -> 0,
    "reattach14" -> 1
  )


  val bb_det_cont_activate = Map(
    "br15" -> 0
  )


  val bb_if_else4_activate = Map(
    "urem16" -> 0,
    "getelementptr17" -> 1,
    "store18" -> 2,
    "br19" -> 3
  )


  val bb_if_end6_activate = Map(
    "ret20" -> 0
  )


  val phi9_phi_in = Map(
    "const_0" -> 0,
    "const_1" -> 1
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %x, i32 %pos, !UID !16, !ScalaLabel !17
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds i32, i32* %arrayidx, i32 0, !UID !18, !ScalaLabel !19
  val getelementptr1_in = Map(
    "getelementptr0" -> 0
  )


  //  %0 = load i32, i32* %arrayidx1, align 4, !UID !20, !ScalaLabel !21
  val load2_in = Map(
    "getelementptr1" -> 0
  )


  //  %arrayidx2 = getelementptr inbounds i32, i32* %arrayidx, i32 1, !UID !22, !ScalaLabel !23
  val getelementptr3_in = Map(
    "getelementptr0" -> 1
  )


  //  %1 = load i32, i32* %arrayidx2, align 4, !UID !24, !ScalaLabel !25
  val load4_in = Map(
    "getelementptr3" -> 0
  )


  //  %cmp = icmp eq i32 %0, %1, !UID !26, !ScalaLabel !27
  val icmp5_in = Map(
    "load2" -> 0,
    "load4" -> 0
  )


  //  br i1 %cmp, label %if.then, label %if.else, !UID !28, !BB_UID !29, !ScalaLabel !30
  val br6_in = Map(
    "icmp5" -> 0
  )


  //  %is_dup.0 = phi i32 [ 1, %if.then ], [ 0, %if.else ], !UID !37, !ScalaLabel !38
  val phi9_in = Map(

  )


  //  %tobool = icmp ne i32 %is_dup.0, 0, !UID !39, !ScalaLabel !40
  val icmp10_in = Map(
    "phi9" -> 0
  )


  //  br i1 %tobool, label %if.then3, label %if.else4, !UID !41, !BB_UID !42, !ScalaLabel !43
  val br11_in = Map(
    "icmp10" -> 0
  )


  //  detach label %det.achd, label %det.cont, !UID !44, !BB_UID !45, !ScalaLabel !46
  val detach12_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  call void @S3(i32* %arrayidx, i32 %pos, i32 %iter), !UID !47, !ScalaLabel !48
  val call13_in = Map(
    "getelementptr0" -> 2,
    "field1" -> 1,
    "field2" -> 0,
    "" -> 2
  )


  //  reattach label %det.cont, !UID !49, !BB_UID !50, !ScalaLabel !51
  val reattach14_in = Map(
    "" -> 3
  )


  //  %rem = urem i32 %iter, 100, !UID !55, !ScalaLabel !56
  val urem16_in = Map(
    "field2" -> 1
  )


  //  %arrayidx5 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !57, !ScalaLabel !58
  val getelementptr17_in = Map(
    "" -> 4,
    "urem16" -> 0
  )


  //  store i32 %pos, i32* %arrayidx5, align 4, !UID !59, !ScalaLabel !60
  val store18_in = Map(
    "field1" -> 2,
    "getelementptr17" -> 0
  )


  //  ret void, !UID !64, !BB_UID !65, !ScalaLabel !66
  val ret20_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class S2DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val call13_out = Decoupled(new Call(List(32,32,32)))
    val call13_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class S2DF(implicit p: Parameters) extends S2DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=4096,NReads=2,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32,32)))
  InputSplitter.io.In <> io.in

  // Manually added
  val field1_expand = Module(new ExpandNode(NumOuts=3,ID=100)(new DataBundle))
  field1_expand.io.enable.valid := true.B
  field1_expand.io.enable.bits.control := true.B
  field1_expand.io.InData <> InputSplitter.io.Out.data("field1")

  // Manually added
  val field2_expand = Module(new ExpandNode(NumOuts=2,ID=101)(new DataBundle))
  field2_expand.io.enable.valid := true.B
  field2_expand.io.enable.bits.control := true.B
  field2_expand.io.InData <> InputSplitter.io.Out.data("field2")


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 0))

  val bb_if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_if_else = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_if_end = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 3))

  val bb_if_then3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_det_achd = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5))

  val bb_det_cont = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 6))

  val bb_if_else4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 7))

  val bb_if_end6 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 1, NumPhi = 1, BID = 8))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %arrayidx = getelementptr inbounds i32, i32* %x, i32 %pos, !UID !16, !ScalaLabel !17
  val getelementptr0 = Module (new GepOneNode(NumOuts = 3, ID = 0)(numByte1 = 4))


  //  %arrayidx1 = getelementptr inbounds i32, i32* %arrayidx, i32 0, !UID !18, !ScalaLabel !19
  val getelementptr1 = Module (new GepOneNode(NumOuts = 1, ID = 1)(numByte1 = 4))


  //  %0 = load i32, i32* %arrayidx1, align 4, !UID !20, !ScalaLabel !21
  val load2 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=2,RouteID=0))


  //  %arrayidx2 = getelementptr inbounds i32, i32* %arrayidx, i32 1, !UID !22, !ScalaLabel !23
  val getelementptr3 = Module (new GepOneNode(NumOuts = 1, ID = 3)(numByte1 = 4))


  //  %1 = load i32, i32* %arrayidx2, align 4, !UID !24, !ScalaLabel !25
  val load4 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=4,RouteID=1))


  //  %cmp = icmp eq i32 %0, %1, !UID !26, !ScalaLabel !27
  val icmp5 = Module (new IcmpNode(NumOuts = 1, ID = 5, opCode = "EQ")(sign=false))


  //  br i1 %cmp, label %if.then, label %if.else, !UID !28, !BB_UID !29, !ScalaLabel !30
  val br6 = Module (new CBranchFastNode(ID = 6))

  // [BasicBlock]  if.then:

  //  br label %if.end, !UID !31, !BB_UID !32, !ScalaLabel !33
  val br7 = Module (new UBranchFastNode(ID = 7))

  // [BasicBlock]  if.else:

  //  br label %if.end, !UID !34, !BB_UID !35, !ScalaLabel !36
  val br8 = Module (new UBranchFastNode(ID = 8))

  // [BasicBlock]  if.end:

  //  %is_dup.0 = phi i32 [ 1, %if.then ], [ 0, %if.else ], !UID !37, !ScalaLabel !38
  val phi9 = Module (new PhiNode(NumInputs = 2, NumOuts = 1, ID = 9))


  //  %tobool = icmp ne i32 %is_dup.0, 0, !UID !39, !ScalaLabel !40
  val icmp10 = Module (new IcmpNode(NumOuts = 1, ID = 10, opCode = "NE")(sign=false))


  //  br i1 %tobool, label %if.then3, label %if.else4, !UID !41, !BB_UID !42, !ScalaLabel !43
  val br11 = Module (new CBranchFastNode(ID = 11))

  // [BasicBlock]  if.then3:

  //  detach label %det.achd, label %det.cont, !UID !44, !BB_UID !45, !ScalaLabel !46
  val detach12 = Module(new Detach(ID = 12))

  // [BasicBlock]  det.achd:

  //  call void @S3(i32* %arrayidx, i32 %pos, i32 %iter), !UID !47, !ScalaLabel !48
  val call13 = Module(new CallNode(ID=13,argTypes=List(32,32,32),retTypes=List(32)))


  //  reattach label %det.cont, !UID !49, !BB_UID !50, !ScalaLabel !51
  val reattach14 = Module(new Reattach(NumPredIn=1, ID=14))

  // [BasicBlock]  det.cont:

  //  br label %if.end6, !UID !52, !BB_UID !53, !ScalaLabel !54
  val br15 = Module (new UBranchFastNode(ID = 15))

  // [BasicBlock]  if.else4:

  //  %rem = urem i32 %iter, 100, !UID !55, !ScalaLabel !56
  val urem16 = Module (new ComputeNode(NumOuts = 1, ID = 16, opCode = "urem")(sign=false))


  //  %arrayidx5 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !57, !ScalaLabel !58
  val getelementptr17 = Module (new GepTwoNode(NumOuts = 1, ID = 17)(numByte1 = 4, numByte2 = 4))


  //  store i32 %pos, i32* %arrayidx5, align 4, !UID !59, !ScalaLabel !60
  val store18 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=18,RouteID=0))


  //  br label %if.end6, !UID !61, !BB_UID !62, !ScalaLabel !63
  val br19 = Module (new UBranchFastNode(ID = 19))

  // [BasicBlock]  if.end6:

  //  ret void, !UID !64, !BB_UID !65, !ScalaLabel !66
  val ret20 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=20))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_S2_FlowParam



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

  //Connecting br6 to bb_if_then
  bb_if_then.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_if_then"))


  //Connecting br6 to bb_if_else
  bb_if_else.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_if_else"))


  //Connecting br7 to bb_if_end
  bb_if_end.io.predicateIn(param.bb_if_end_pred("br7")) <> br7.io.Out(param.br7_brn_bb("bb_if_end"))


  //Connecting br8 to bb_if_end
  bb_if_end.io.predicateIn(param.bb_if_end_pred("br8")) <> br8.io.Out(param.br8_brn_bb("bb_if_end"))


  //Connecting br11 to bb_if_then3
  bb_if_then3.io.predicateIn <> br11.io.Out(param.br11_brn_bb("bb_if_then3"))


  //Connecting br11 to bb_if_else4
  bb_if_else4.io.predicateIn <> br11.io.Out(param.br11_brn_bb("bb_if_else4"))


  //Connecting br15 to bb_if_end6
  bb_if_end6.io.predicateIn(0) <> br15.io.Out(param.br15_brn_bb("bb_if_end6")) // Manually fixed


  //Connecting br19 to bb_if_end6
  bb_if_end6.io.predicateIn(1) <> br19.io.Out(param.br19_brn_bb("bb_if_end6")) // Manually fixed
  bb_if_end6.io.MaskBB(0).ready := true.B // manually added


  //Connecting detach12 to bb_det_achd
  bb_det_achd.io.predicateIn <> detach12.io.Out(param.detach12_brn_bb("bb_det_achd"))


  //Connecting detach12 to bb_det_cont
  bb_det_cont.io.predicateIn <> detach12.io.Out(param.detach12_brn_bb("bb_det_cont"))




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr0"))

  getelementptr1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr1"))

  load2.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load2"))

  getelementptr3.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr3"))

  load4.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load4"))

  icmp5.io.enable <> bb_entry.io.Out(param.bb_entry_activate("icmp5"))

  br6.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br6"))



  br7.io.enable <> bb_if_then.io.Out(param.bb_if_then_activate("br7"))



  br8.io.enable <> bb_if_else.io.Out(param.bb_if_else_activate("br8"))



  phi9.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("phi9"))

  icmp10.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("icmp10"))

  br11.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("br11"))



  detach12.io.enable <> bb_if_then3.io.Out(param.bb_if_then3_activate("detach12"))



  call13.io.In.enable <> bb_det_achd.io.Out(param.bb_det_achd_activate("call13"))

  // Manual fix.  Reattach should only depend on its increment/decr inputs. If it is connected to the BB
  // Enable it will stall the loop
//  reattach14.io.enable <> bb_det_achd.io.Out(param.bb_det_achd_activate("reattach14"))
  reattach14.io.enable.enq(ControlBundle.active())  // always enabled
  bb_det_achd.io.Out(param.bb_det_achd_activate("reattach14")).ready := true.B




  br15.io.enable <> bb_det_cont.io.Out(param.bb_det_cont_activate("br15"))



  urem16.io.enable <> bb_if_else4.io.Out(param.bb_if_else4_activate("urem16"))

  getelementptr17.io.enable <> bb_if_else4.io.Out(param.bb_if_else4_activate("getelementptr17"))

  store18.io.enable <> bb_if_else4.io.Out(param.bb_if_else4_activate("store18"))

  br19.io.enable <> bb_if_else4.io.Out(param.bb_if_else4_activate("br19"))



  ret20.io.enable <> bb_if_end6.io.Out(param.bb_if_end6_activate("ret20"))


  // No sync so detach/reattach are tied off
  detach12.io.Out(2).ready := true.B
  reattach14.io.Out(0).ready := true.B


  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  //Function doesn't have any for loop


  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi9.io.InData(param.phi9_phi_in("const_0")).bits.data := 1.U
  phi9.io.InData(param.phi9_phi_in("const_0")).bits.predicate := true.B
  phi9.io.InData(param.phi9_phi_in("const_0")).valid := true.B

  phi9.io.InData(param.phi9_phi_in("const_1")).bits.data := 0.U
  phi9.io.InData(param.phi9_phi_in("const_1")).bits.predicate := true.B
  phi9.io.InData(param.phi9_phi_in("const_1")).valid := true.B

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi9.io.Mask <> bb_if_end.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring GEP instruction to the function argument
  getelementptr0.io.baseAddress <> InputSplitter.io.Out.data("field0")


  // Wiring GEP instruction to the function argument
  getelementptr0.io.idx1 <> field1_expand.io.Out(0)


  // Wiring GEP instruction to the parent instruction
  getelementptr1.io.baseAddress <> getelementptr0.io.Out(param.getelementptr1_in("getelementptr0"))


  // Wiring GEP instruction to the Constant
  getelementptr1.io.idx1.valid :=  true.B
  getelementptr1.io.idx1.bits.predicate :=  true.B
  getelementptr1.io.idx1.bits.data :=  0.U


  // Wiring Load instruction to the parent instruction
  load2.io.GepAddr <> getelementptr1.io.Out(param.load2_in("getelementptr1"))
  load2.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load2.io.memReq




  // Wiring GEP instruction to the parent instruction
  getelementptr3.io.baseAddress <> getelementptr0.io.Out(param.getelementptr3_in("getelementptr0"))


  // Wiring GEP instruction to the Constant
  getelementptr3.io.idx1.valid :=  true.B
  getelementptr3.io.idx1.bits.predicate :=  true.B
  getelementptr3.io.idx1.bits.data :=  1.U


  // Wiring Load instruction to the parent instruction
  load4.io.GepAddr <> getelementptr3.io.Out(param.load4_in("getelementptr3"))
  load4.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load4.io.memReq




  // Wiring instructions
  icmp5.io.LeftIO <> load2.io.Out(param.icmp5_in("load2"))

  // Wiring instructions
  icmp5.io.RightIO <> load4.io.Out(param.icmp5_in("load4"))

  // Wiring Branch instruction
  br6.io.CmpIO <> icmp5.io.Out(param.br6_in("icmp5"))

  // Wiring instructions
  icmp10.io.LeftIO <> phi9.io.Out(param.icmp10_in("phi9"))

  // Wiring constant
  icmp10.io.RightIO.bits.data := 0.U
  icmp10.io.RightIO.bits.predicate := true.B
  icmp10.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br11.io.CmpIO <> icmp10.io.Out(param.br11_in("icmp10"))

  // Wiring Call to I/O
  io.call13_out <> call13.io.callOut
  call13.io.retIn <> io.call13_in
  call13.io.Out.enable.ready := true.B // Manual fix
  call13.io.Out.data("field0").ready := true.B // Manual fix
  // Wiring instructions
  call13.io.In.data("field0") <> getelementptr0.io.Out(param.call13_in("getelementptr0"))

  // Wiring Call to the function argument
  call13.io.In.data("field1") <> field1_expand.io.Out(1)

  // Wiring Call to the function argument
  call13.io.In.data("field2") <> field2_expand.io.Out(0)



  // Wiring Binary instruction to the function argument
  urem16.io.LeftIO <> field2_expand.io.Out(1)

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


  // Wiring Store instruction to the function argument
  store18.io.inData <> field1_expand.io.Out(2)



  // Wiring Store instruction to the parent instruction
  store18.io.GepAddr <> getelementptr17.io.Out(param.store18_in("getelementptr17"))
  store18.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store18.io.memReq
  store18.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  ret20.io.predicateIn(0).bits.control := true.B
  ret20.io.predicateIn(0).bits.taskID := 0.U
  ret20.io.predicateIn(0).valid := true.B
  ret20.io.In.data("field0").bits.data := 1.U
  ret20.io.In.data("field0").bits.predicate := true.B
  ret20.io.In.data("field0").valid := true.B
  io.out <> ret20.io.Out


}

import java.io.{File, FileWriter}
object S2Main extends App {
  val dir = new File("RTL/S2") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new S2DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


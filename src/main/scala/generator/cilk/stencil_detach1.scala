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

object Data_stencil_detach1_FlowParam {

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_for_cond_pred = Map(
    "br2" -> 0,
    "br9" -> 1
  )


  val bb_my_for_inc_pred = Map(
    "br7" -> 0
  )


  val bb_my_for_body_pred = Map(
    "br5" -> 0
  )


  val bb_my_for_end_pred = Map(
    "br5" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br20" -> 0
  )


  val br2_brn_bb = Map(
    "bb_my_for_cond" -> 0
  )


  val br5_brn_bb = Map(
    "bb_my_for_body" -> 0,
    "bb_my_for_end" -> 1
  )


  val br7_brn_bb = Map(
    "bb_my_for_inc" -> 0
  )


  val br9_brn_bb = Map(
    "bb_my_for_cond" -> 0
  )


  val br20_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val bb_my_pfor_body_activate = Map(
    "udiv0" -> 0,
    "and1" -> 1,
    "br2" -> 2
  )


  val bb_my_for_cond_activate = Map(
    "phi3" -> 0,
    "icmp4" -> 1,
    "br5" -> 2
  )


  val bb_my_for_body_activate = Map(
    "call6" -> 0,
    "br7" -> 1
  )


  val bb_my_for_inc_activate = Map(
    "add8" -> 0,
    "br9" -> 1
  )


  val bb_my_for_end_activate = Map(
    "mul10" -> 0,
    "add11" -> 1,
    "getelementptr12" -> 2,
    "load13" -> 3,
    "add14" -> 4,
    "udiv15" -> 5,
    "mul16" -> 6,
    "add17" -> 7,
    "getelementptr18" -> 8,
    "store19" -> 9,
    "br20" -> 10
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret21" -> 0
  )


  val phi3_phi_in = Map(
    "const_0" -> 0,
    "add8" -> 1
  )


  //  %0 = udiv i32 %pos.0.in, 4, !UID !7, !ScalaLabel !8
  val udiv0_in = Map(
    "field0" -> 0
  )


  //  %1 = and i32 %pos.0.in, 3, !UID !9, !ScalaLabel !10
  val and1_in = Map(
    "field0" -> 1
  )


  //  %2 = phi i32 [ 0, %my_pfor.body ], [ %4, %my_for.inc ], !UID !14, !ScalaLabel !15
  val phi3_in = Map(
    "add8" -> 0
  )


  //  %3 = icmp ule i32 %2, 2, !UID !16, !ScalaLabel !17
  val icmp4_in = Map(
    "phi3" -> 0
  )


  //  br i1 %3, label %my_for.body, label %my_for.end, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5_in = Map(
    "icmp4" -> 0
  )


  //  call void @stencil_inner(i32* %in.in, i32* %out.in, i32 %0, i32 %1, i32 %2), !UID !21, !ScalaLabel !22
  val call6_in = Map(
    "field1" -> 0,
    "field2" -> 0,
    "udiv0" -> 0,
    "and1" -> 0,
    "phi3" -> 1,
    "" -> 0
  )


  //  %4 = add i32 %2, 1, !UID !26, !ScalaLabel !27
  val add8_in = Map(
    "phi3" -> 2
  )


  //  %5 = mul i32 %0, 4, !UID !43, !ScalaLabel !44
  val mul10_in = Map(
    "udiv0" -> 1
  )


  //  %6 = add i32 %5, %1, !UID !45, !ScalaLabel !46
  val add11_in = Map(
    "mul10" -> 0,
    "and1" -> 1
  )


  //  %7 = getelementptr inbounds i32, i32* %out.in, i32 %6, !UID !47, !ScalaLabel !48
  val getelementptr12_in = Map(
    "field2" -> 1,
    "add11" -> 0
  )


  //  %8 = load i32, i32* %7, align 4, !UID !49, !ScalaLabel !50
  val load13_in = Map(
    "getelementptr12" -> 0
  )


  //  %9 = add i32 %8, 9, !UID !51, !ScalaLabel !52
  val add14_in = Map(
    "load13" -> 0
  )


  //  %10 = udiv i32 %9, 9, !UID !53, !ScalaLabel !54
  val udiv15_in = Map(
    "add14" -> 0
  )


  //  %11 = mul i32 %0, 4, !UID !55, !ScalaLabel !56
  val mul16_in = Map(
    "udiv0" -> 2
  )


  //  %12 = add i32 %11, %1, !UID !57, !ScalaLabel !58
  val add17_in = Map(
    "mul16" -> 0,
    "and1" -> 2
  )


  //  %13 = getelementptr inbounds i32, i32* %out.in, i32 %12, !UID !59, !ScalaLabel !60
  val getelementptr18_in = Map(
    "field2" -> 2,
    "add17" -> 0
  )


  //  store i32 %10, i32* %13, align 4, !UID !61, !ScalaLabel !62
  val store19_in = Map(
    "udiv15" -> 0,
    "getelementptr18" -> 0
  )


  //  ret void, !UID !66, !BB_UID !67, !ScalaLabel !68
  val ret21_in = Map(

  )


}


/* ================================================================== *
 *                   PRINTING PORTS DEFINITION                        *
 * ================================================================== */


abstract class stencil_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val call6_out = Decoupled(new Call(List(32, 32, 32, 32, 32)))
    val call6_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheResp))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}


/* ================================================================== *
 *                   PRINTING MODULE DEFINITION                       *
 * ================================================================== */


class stencil_detach1DF(implicit p: Parameters) extends stencil_detach1DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


  val StackPointer = Module(new Stack(NumOps = 1))

  val RegisterFile = Module(new TypeStackFile(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2)))

  val CacheMem = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32, 32, 32)))
  InputSplitter.io.In <> io.in

  // Manually added
  val field0_expand = Module(new ExpandNode(NumOuts = 2, ID = 100)(new DataBundle))
  field0_expand.io.enable.valid := true.B
  field0_expand.io.enable.bits.control := true.B
  field0_expand.io.InData <> InputSplitter.io.Out.data("field0")

  // Manually added
  val field2_expand = Module(new ExpandNode(NumOuts = 3, ID = 101)(new DataBundle))
  field2_expand.io.enable.valid := true.B
  field2_expand.io.enable.bits.control := true.B
  field2_expand.io.InData <> InputSplitter.io.Out.data("field2")


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_3 = Module(new LiveInNewNode(NumOuts = 1, ID = 0))

  val loop_L_0_liveIN_DELAY_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_DELAY_1 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_DELAY_2 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_DELAY_3 = Module(new LiveOutNode(NumOuts = 1, ID = 0))

  val loop_L_0_live_Control_0 = Module(new LiveOutControlNode(NumOuts = 1, ID = 0))

  //  val loop_L_0_LiveOut_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))

  val phi_L_DELAY_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
  val phi_L_DELAY_1 = Module(new LiveOutNode(NumOuts = 1, ID = 0))

  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 0))

  val bb_my_for_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_my_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 2))

  val bb_my_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_tmp_my_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 4))

  val bb_my_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 4))

  val bb_my_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 5))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = udiv i32 %pos.0.in, 4, !UID !7, !ScalaLabel !8
  val udiv0 = Module(new ComputeNode(NumOuts = 3, ID = 0, opCode = "udiv")(sign = false))


  //  %1 = and i32 %pos.0.in, 3, !UID !9, !ScalaLabel !10
  val and1 = Module(new ComputeNode(NumOuts = 3, ID = 1, opCode = "and")(sign = false))


  //  br label %my_for.cond, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br2 = Module(new UBranchNode(ID = 2))

  // [BasicBlock]  my_for.cond:

  //  %2 = phi i32 [ 0, %my_pfor.body ], [ %4, %my_for.inc ], !UID !14, !ScalaLabel !15
  val phi3 = Module(new PhiNode(NumInputs = 2, NumOuts = 3, ID = 3))


  //  %3 = icmp ule i32 %2, 2, !UID !16, !ScalaLabel !17
  val icmp4 = Module(new IcmpNode(NumOuts = 1, ID = 4, opCode = "ULE")(sign = false))


  //  br i1 %3, label %my_for.body, label %my_for.end, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5 = Module(new CBranchNode(ID = 5))

  // [BasicBlock]  my_for.body:

  //  call void @stencil_inner(i32* %in.in, i32* %out.in, i32 %0, i32 %1, i32 %2), !UID !21, !ScalaLabel !22
  val call6 = Module(new CallNode(ID = 6, argTypes = List(32, 32, 32, 32, 32), retTypes = List(32)))


  //  br label %my_for.inc, !UID !23, !BB_UID !24, !ScalaLabel !25
  val br7 = Module(new UBranchNode(ID = 7))

  // [BasicBlock]  my_for.inc:

  //  %4 = add i32 %2, 1, !UID !26, !ScalaLabel !27
  val add8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign = false))


  //  br label %my_for.cond, !llvm.loop !28, !UID !40, !BB_UID !41, !ScalaLabel !42
  val br9 = Module(new UBranchNode(ID = 9))

  // [BasicBlock]  my_for.end:

  //  %5 = mul i32 %0, 4, !UID !43, !ScalaLabel !44
  val mul10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "mul")(sign = false))


  //  %6 = add i32 %5, %1, !UID !45, !ScalaLabel !46
  val add11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign = false))


  //  %7 = getelementptr inbounds i32, i32* %out.in, i32 %6, !UID !47, !ScalaLabel !48
  val getelementptr12 = Module(new GepOneNode(NumOuts = 1, ID = 12)(numByte1 = 4))


  //  %8 = load i32, i32* %7, align 4, !UID !49, !ScalaLabel !50
  val load13 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 13, RouteID = 0))


  //  %9 = add i32 %8, 9, !UID !51, !ScalaLabel !52
  val add14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign = false))


  //  %10 = udiv i32 %9, 9, !UID !53, !ScalaLabel !54
  val udiv15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "udiv")(sign = false))


  //  %11 = mul i32 %0, 4, !UID !55, !ScalaLabel !56
  val mul16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "mul")(sign = false))


  //  %12 = add i32 %11, %1, !UID !57, !ScalaLabel !58
  val add17 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "add")(sign = false))


  //  %13 = getelementptr inbounds i32, i32* %out.in, i32 %12, !UID !59, !ScalaLabel !60
  val getelementptr18 = Module(new GepOneNode(NumOuts = 1, ID = 18)(numByte1 = 4))


  //  store i32 %10, i32* %13, align 4, !UID !61, !ScalaLabel !62
  val store19 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 19, RouteID = 0))


  //  br label %my_pfor.preattach, !UID !63, !BB_UID !64, !ScalaLabel !65
  val br20 = Module(new UBranchNode(ID = 20))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !66, !BB_UID !67, !ScalaLabel !68
  val ret21 = Module(new RetNode(retTypes = List(32), ID = 21))

  val tmp_ubranch = Module(new UBranchEndNode(ID = 22))


  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_stencil_detach1_FlowParam


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

  //Connecting br2 to bb_my_for_cond
  bb_my_for_cond.io.activate <> br2.io.Out(param.br2_brn_bb("bb_my_for_cond"))


  //Connecting br5 to bb_my_for_body
  bb_my_for_body.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_my_for_body"))


  //Connecting br5 to bb_my_for_end
  //  bb_my_for_end.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_my_for_end"))

  bb_tmp_my_for_end.io.predicateIn <> br5.io.Out(1)

  tmp_ubranch.io.enable <> bb_tmp_my_for_end.io.Out(0)

  bb_my_for_end.io.predicateIn <> tmp_ubranch.io.Out(0)


  //Connecting br7 to bb_my_for_inc
  bb_my_for_inc.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_my_for_inc"))


  //Connecting br9 to bb_my_for_cond
  bb_my_for_cond.io.loopBack <> br9.io.Out(param.br9_brn_bb("bb_my_for_cond"))


  //Connecting br20 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn <> br20.io.Out(param.br20_brn_bb("bb_my_pfor_preattach"))


  // There is no detach instruction


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  udiv0.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("udiv0"))

  and1.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("and1"))

  br2.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("br2"))


  phi3.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("phi3"))

  icmp4.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("icmp4"))

  br5.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("br5"))


  //  call6.io.In.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("call6"))
  loop_L_0_live_Control_0.io.InData <> bb_my_for_body.io.Out(0)
  call6.io.In.enable <> loop_L_0_live_Control_0.io.Out(0)
  //  call6.io.In.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("call6"))

  br7.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("br7"))


  add8.io.enable <> bb_my_for_inc.io.Out(param.bb_my_for_inc_activate("add8"))

  br9.io.enable <> bb_my_for_inc.io.Out(param.bb_my_for_inc_activate("br9"))


  mul10.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("mul10"))

  add11.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("add11"))

  getelementptr12.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("getelementptr12"))

  load13.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("load13"))

  add14.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("add14"))

  udiv15.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("udiv15"))

  mul16.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("mul16"))

  add17.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("add17"))

  getelementptr18.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("getelementptr18"))

  store19.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("store19"))

  br20.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("br20"))

  loop_L_0_liveIN_0.io.enable <> bb_my_pfor_body.io.Out(3)
  loop_L_0_liveIN_1.io.enable <> bb_my_pfor_body.io.Out(4)
  loop_L_0_liveIN_2.io.enable <> bb_my_pfor_body.io.Out(5)
  loop_L_0_liveIN_3.io.enable <> bb_my_pfor_body.io.Out(6)

  loop_L_0_liveIN_0.io.Invalid <> bb_tmp_my_for_end.io.Out(1)
  loop_L_0_liveIN_1.io.Invalid <> bb_tmp_my_for_end.io.Out(2)
  loop_L_0_liveIN_2.io.Invalid <> bb_tmp_my_for_end.io.Out(3)
  loop_L_0_liveIN_3.io.Invalid <> bb_tmp_my_for_end.io.Out(4)

  loop_L_0_liveIN_DELAY_0.io.enable <> bb_my_for_body.io.Out(2)
  loop_L_0_liveIN_DELAY_1.io.enable <> bb_my_for_body.io.Out(3)
  loop_L_0_liveIN_DELAY_2.io.enable <> bb_my_for_body.io.Out(4)
  loop_L_0_liveIN_DELAY_3.io.enable <> bb_my_for_body.io.Out(5)
  loop_L_0_live_Control_0.io.enable <> bb_my_for_body.io.Out(6)

  //  loop_L_0_LiveOut_0.io.enable <> bb_tmp_my_for_end.io.Out(5)

  phi_L_DELAY_0.io.enable <> bb_my_for_body.io.Out(7)
  phi_L_DELAY_1.io.enable <> bb_my_for_body.io.Out(8)

  ret21.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret21"))


  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %in.in
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32* %out.in
  loop_L_0_liveIN_1.io.InData <> field2_expand.io.Out(0)

  // Connecting instruction to the loop header
  //  %0 = udiv i32 %pos.0.in, 4, !UID !7, !ScalaLabel !8
  loop_L_0_liveIN_2.io.InData <> udiv0.io.Out(param.call6_in("udiv0"))

  // Connecting instruction to the loop header
  //  %1 = and i32 %pos.0.in, 3, !UID !9, !ScalaLabel !10
  loop_L_0_liveIN_3.io.InData <> and1.io.Out(param.call6_in("and1"))


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

  phi3.io.InData(param.phi3_phi_in("add8")) <> add8.io.Out(param.phi3_in("add8"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi3.io.Mask <> bb_my_for_cond.io.MaskBB(0)


  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Binary instruction to the function argument
  udiv0.io.LeftIO <> field0_expand.io.Out(0)

  // Wiring constant
  udiv0.io.RightIO.bits.data := 4.U
  udiv0.io.RightIO.bits.predicate := true.B
  udiv0.io.RightIO.valid := true.B

  // Wiring Binary instruction to the function argument
  and1.io.LeftIO <> field0_expand.io.Out(1)

  // Wiring constant
  and1.io.RightIO.bits.data := 3.U
  and1.io.RightIO.bits.predicate := true.B
  and1.io.RightIO.valid := true.B

  // Wiring instructions
  icmp4.io.LeftIO <> phi3.io.Out(param.icmp4_in("phi3"))

  // Wiring constant
  icmp4.io.RightIO.bits.data := 2.U
  icmp4.io.RightIO.bits.predicate := true.B
  icmp4.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br5.io.CmpIO <> icmp4.io.Out(param.br5_in("icmp4"))

  // Wiring Call to I/O
  io.call6_out <> call6.io.callOut
  call6.io.retIn <> io.call6_in

  call6.io.Out.enable.ready := true.B // Manual fix


  //  loop_L_0_LiveOut_0.io.InData <> call6.io.Out.data("field0")
  //  loop_L_0_LiveOut_0.io.Out(0).ready := true.B // Manual fix
  call6.io.Out.data("field0").ready := true.B

  // Wiring Call instruction to the loop header
  //  call6.io.In.data("field0") <> loop_L_0_liveIN_0.io.Out(param.call6_in("field1"))
  loop_L_0_liveIN_DELAY_0.io.InData <> loop_L_0_liveIN_0.io.Out(0)
  call6.io.In.data("field0") <> loop_L_0_liveIN_DELAY_0.io.Out(0)

  // Wiring Call instruction to the loop header
  //  call6.io.In.data("field1") <> loop_L_0_liveIN_1.io.Out(param.call6_in("field2"))
  loop_L_0_liveIN_DELAY_1.io.InData <> loop_L_0_liveIN_1.io.Out(0)
  call6.io.In.data("field1") <> loop_L_0_liveIN_DELAY_1.io.Out(0)

  // Wiring Call instruction to the loop header
  //  call6.io.In.data("field2") <> loop_L_0_liveIN_2.io.Out(param.call6_in("udiv0"))
  loop_L_0_liveIN_DELAY_2.io.InData <> loop_L_0_liveIN_2.io.Out(0)
  call6.io.In.data("field2") <> loop_L_0_liveIN_DELAY_2.io.Out(0)

  // Wiring Call instruction to the loop header
  //  call6.io.In.data("field3") <> loop_L_0_liveIN_3.io.Out(param.call6_in("and1"))
  loop_L_0_liveIN_DELAY_3.io.InData <> loop_L_0_liveIN_3.io.Out(0)
  call6.io.In.data("field3") <> loop_L_0_liveIN_DELAY_3.io.Out(0)

  // Wiring Call instruction to the loop header
  //  call6.io.In.data("field4") <> phi3.io.Out(param.call6_in("phi3"))
  phi_L_DELAY_0.io.InData <> phi3.io.Out(1)
  call6.io.In.data("field4") <> phi_L_DELAY_0.io.Out(0)


  // Wiring instructions
  //  add8.io.LeftIO <> phi3.io.Out(param.add8_in("phi3"))
  phi_L_DELAY_1.io.InData <> phi3.io.Out(2)
  add8.io.LeftIO <> phi_L_DELAY_1.io.Out(0)

  // Wiring constant
  add8.io.RightIO.bits.data := 1.U
  add8.io.RightIO.bits.predicate := true.B
  add8.io.RightIO.valid := true.B

  // Wiring instructions
  mul10.io.LeftIO <> udiv0.io.Out(param.mul10_in("udiv0"))

  // Wiring constant
  mul10.io.RightIO.bits.data := 4.U
  mul10.io.RightIO.bits.predicate := true.B
  mul10.io.RightIO.valid := true.B

  // Wiring instructions
  add11.io.LeftIO <> mul10.io.Out(param.add11_in("mul10"))

  // Wiring instructions
  add11.io.RightIO <> and1.io.Out(param.add11_in("and1"))

  // Wiring GEP instruction to the function argument
  getelementptr12.io.baseAddress <> field2_expand.io.Out(1)


  // Wiring GEP instruction to the parent instruction
  getelementptr12.io.idx1 <> add11.io.Out(param.getelementptr12_in("add11"))


  // Wiring Load instruction to the parent instruction
  load13.io.GepAddr <> getelementptr12.io.Out(param.load13_in("getelementptr12"))
  load13.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load13.io.memReq


  // Wiring instructions
  add14.io.LeftIO <> load13.io.Out(param.add14_in("load13"))

  // Wiring constant
  add14.io.RightIO.bits.data := 9.U
  add14.io.RightIO.bits.predicate := true.B
  add14.io.RightIO.valid := true.B

  // Wiring instructions
  udiv15.io.LeftIO <> add14.io.Out(param.udiv15_in("add14"))

  // Wiring constant
  udiv15.io.RightIO.bits.data := 9.U
  udiv15.io.RightIO.bits.predicate := true.B
  udiv15.io.RightIO.valid := true.B

  // Wiring instructions
  mul16.io.LeftIO <> udiv0.io.Out(param.mul16_in("udiv0"))

  // Wiring constant
  mul16.io.RightIO.bits.data := 4.U
  mul16.io.RightIO.bits.predicate := true.B
  mul16.io.RightIO.valid := true.B

  // Wiring instructions
  add17.io.LeftIO <> mul16.io.Out(param.add17_in("mul16"))

  // Wiring instructions
  add17.io.RightIO <> and1.io.Out(param.add17_in("and1"))

  // Wiring GEP instruction to the function argument
  getelementptr18.io.baseAddress <> field2_expand.io.Out(2)


  // Wiring GEP instruction to the parent instruction
  getelementptr18.io.idx1 <> add17.io.Out(param.getelementptr18_in("add17"))


  store19.io.inData <> udiv15.io.Out(param.store19_in("udiv15"))


  // Wiring Store instruction to the parent instruction
  store19.io.GepAddr <> getelementptr18.io.Out(param.store19_in("getelementptr18"))
  store19.io.memResp <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store19.io.memReq
  //store19.io.Out(0).ready := true.B

  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret21.io.In.data("field0") <> store19.io.Out(0)
  //  ret21.io.In.data("field0").bits.data := 1.U
  //  ret21.io.In.data("field0").bits.predicate := true.B
  //  ret21.io.In.data("field0").valid := true.B
  io.out <> ret21.io.Out

}

import java.io.{File, FileWriter}

object stencil_detach1Main extends App {
  val dir = new File("RTL/stencil_detach1");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new stencil_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


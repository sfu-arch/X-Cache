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

object Data_bgemm_detach2_FlowParam{

  val bb_my_pfor_body8_pred = Map(
    "active" -> 0
  )


  val bb_my_loopi_pred = Map(
    "br2" -> 0
  )


  val bb_my_pfor_cond12_pred = Map(
    "br3" -> 0,
    "br9" -> 1
  )


  val bb_my_pfor_detach14_pred = Map(
    "br6" -> 0
  )


  val bb_my_pfor_end_pred = Map(
    "br6" -> 0
  )


  val bb_my_pfor_preattach37_pred = Map(
    "br11" -> 0
  )


  val br2_brn_bb = Map(
    "bb_my_loopi" -> 0
  )


  val br3_brn_bb = Map(
    "bb_my_pfor_cond12" -> 0
  )


  val br6_brn_bb = Map(
    "bb_my_pfor_detach14" -> 0,
    "bb_my_pfor_end" -> 1
  )


  val br9_brn_bb = Map(
    "bb_my_pfor_cond12" -> 0
  )


  val br11_brn_bb = Map(
    "bb_my_pfor_preattach37" -> 0
  )


  val bb_my_pfor_inc_pred = Map(
    "detach7" -> 0
  )


  val bb_my_offload_pfor_body15_pred = Map(
    "detach7" -> 0
  )


  val detach7_brn_bb = Map(
    "bb_my_offload_pfor_body15" -> 0,
    "bb_my_pfor_inc" -> 1
  )


  val bb_my_pfor_body8_activate = Map(
    "mul0" -> 0,
    "add1" -> 1,
    "br2" -> 2
  )


  val bb_my_loopi_activate = Map(
    "br3" -> 0
  )


  val bb_my_pfor_cond12_activate = Map(
    "phi4" -> 0,
    "icmp5" -> 1,
    "br6" -> 2
  )


  val bb_my_pfor_detach14_activate = Map(
    "detach7" -> 0
  )


  val bb_my_pfor_inc_activate = Map(
    "add8" -> 0,
    "br9" -> 1
  )


  val bb_my_pfor_end_activate = Map(
    "sync10" -> 0
  )


  val bb_my_pfor_end_continue_activate = Map(
    "br11" -> 0
  )


  val bb_my_pfor_preattach37_activate = Map(
    "ret12" -> 0
  )


  val bb_my_offload_pfor_body15_activate = Map(
    "call13" -> 0,
    "reattach14" -> 1
  )


  val phi4_phi_in = Map(
    "const_0" -> 0,
    "add8" -> 1
  )


  //  %0 = mul nsw i32 %__begin3.0.in, 2, !UID !7, !ScalaLabel !8
  val mul0_in = Map(
    "field0" -> 0
  )


  //  %1 = add nsw i32 0, %0, !UID !9, !ScalaLabel !10
  val add1_in = Map(
    "mul0" -> 0
  )


  //  %2 = phi i32 [ 0, %my_loopi ], [ %4, %my_pfor.inc ], !UID !17, !ScalaLabel !18
  val phi4_in = Map(
    "add8" -> 0
  )


  //  %3 = icmp slt i32 %2, 4, !UID !19, !ScalaLabel !20
  val icmp5_in = Map(
    "phi4" -> 0
  )


  //  br i1 %3, label %my_pfor.detach14, label %my_pfor.end, !UID !21, !BB_UID !22, !ScalaLabel !23
  val br6_in = Map(
    "icmp5" -> 0
  )


  //  detach label %my_offload.pfor.body15, label %my_pfor.inc, !UID !24, !BB_UID !25, !ScalaLabel !26
  val detach7_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %4 = add nsw i32 %2, 1, !UID !27, !ScalaLabel !28
  val add8_in = Map(
    "phi4" -> 1
  )


  //  sync label %my_pfor.end.continue, !UID !50, !BB_UID !51, !ScalaLabel !52
  val sync10_in = Map(
    "" -> 2
  )


  //  ret void, !UID !56, !BB_UID !57, !ScalaLabel !58
  val ret12_in = Map(

  )


  //  call void @bgemm_detach3(i32 %2, i32 %1, i32* %m1.in, i32 %add.in, i32* %m2.in, i32* %prod.in), !UID !59, !ScalaLabel !60
  val call13_in = Map(
    "phi4" -> 2,
    "add1" -> 0,
    "field1" -> 0,
    "field2" -> 0,
    "field3" -> 0,
    "field4" -> 0,
    "" -> 3
  )


  //  reattach label %my_pfor.inc, !UID !61, !BB_UID !62, !ScalaLabel !63
  val reattach14_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class bgemm_detach2DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32,32))))
    val call13_out = Decoupled(new Call(List(32,32,32,32,32,32)))
    val call13_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class bgemm_detach2DF(implicit p: Parameters) extends bgemm_detach2DFIO()(p) {



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

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCall(List(32,32,32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_3 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_4 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body8 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_my_loopi = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_my_pfor_cond12 = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 2))

  val bb_my_pfor_detach14 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_my_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 4))

  val bb_my_pfor_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 5))

  val bb_my_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_my_pfor_preattach37 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7))

  val bb_my_offload_pfor_body15 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 8))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body8:

  //  %0 = mul nsw i32 %__begin3.0.in, 2, !UID !7, !ScalaLabel !8
  val mul0 = Module (new ComputeNode(NumOuts = 1, ID = 0, opCode = "mul")(sign=false))


  //  %1 = add nsw i32 0, %0, !UID !9, !ScalaLabel !10
  val add1 = Module (new ComputeNode(NumOuts = 1, ID = 1, opCode = "add")(sign=false))


  //  br label %my_loopi, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br2 = Module (new UBranchNode(ID = 2))

  // [BasicBlock]  my_loopi:

  //  br label %my_pfor.cond12, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new UBranchNode(ID = 3))

  // [BasicBlock]  my_pfor.cond12:

  //  %2 = phi i32 [ 0, %my_loopi ], [ %4, %my_pfor.inc ], !UID !17, !ScalaLabel !18
  val phi4 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 4))


  //  %3 = icmp slt i32 %2, 4, !UID !19, !ScalaLabel !20
  val icmp5 = Module (new IcmpNode(NumOuts = 1, ID = 5, opCode = "ULT")(sign=false))


  //  br i1 %3, label %my_pfor.detach14, label %my_pfor.end, !UID !21, !BB_UID !22, !ScalaLabel !23
  val br6 = Module (new CBranchNode(ID = 6))

  // [BasicBlock]  my_pfor.detach14:

  //  detach label %my_offload.pfor.body15, label %my_pfor.inc, !UID !24, !BB_UID !25, !ScalaLabel !26
  val detach7 = Module(new Detach(ID = 7))

  // [BasicBlock]  my_pfor.inc:

  //  %4 = add nsw i32 %2, 1, !UID !27, !ScalaLabel !28
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  br label %my_pfor.cond12, !llvm.loop !29, !UID !47, !BB_UID !48, !ScalaLabel !49
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  my_pfor.end:

  //  sync label %my_pfor.end.continue, !UID !50, !BB_UID !51, !ScalaLabel !52
  val sync10 = Module(new Sync(ID = 10, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  my_pfor.end.continue:

  //  br label %my_pfor.preattach37, !UID !53, !BB_UID !54, !ScalaLabel !55
  val br11 = Module (new UBranchNode(ID = 11))

  // [BasicBlock]  my_pfor.preattach37:

  //  ret void, !UID !56, !BB_UID !57, !ScalaLabel !58
  val ret12 = Module(new RetNode(retTypes=List(32), ID=12))

  // [BasicBlock]  my_offload.pfor.body15:

  //  call void @bgemm_detach3(i32 %2, i32 %1, i32* %m1.in, i32 %add.in, i32* %m2.in, i32* %prod.in), !UID !59, !ScalaLabel !60
  val call13 = Module(new CallNode(ID=13,argTypes=List(32,32,32,32,32,32),retTypes=List(32)))


  //  reattach label %my_pfor.inc, !UID !61, !BB_UID !62, !ScalaLabel !63
  val reattach14 = Module(new Reattach(NumPredOps=1, ID=14))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_bgemm_detach2_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_my_pfor_body8.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br2 to bb_my_loopi
  bb_my_loopi.io.predicateIn <> br2.io.Out(param.br2_brn_bb("bb_my_loopi"))


  //Connecting br3 to bb_my_pfor_cond12
  bb_my_pfor_cond12.io.predicateIn(param.bb_my_pfor_cond12_pred("br3")) <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_cond12"))


  //Connecting br6 to bb_my_pfor_detach14
  bb_my_pfor_detach14.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_my_pfor_detach14"))


  //Connecting br6 to bb_my_pfor_end
  bb_my_pfor_end.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_my_pfor_end"))


  //Connecting br9 to bb_my_pfor_cond12
  bb_my_pfor_cond12.io.predicateIn(param.bb_my_pfor_cond12_pred("br9")) <> br9.io.Out(param.br9_brn_bb("bb_my_pfor_cond12"))


  //Connecting br11 to bb_my_pfor_preattach37
  bb_my_pfor_preattach37.io.predicateIn <> br11.io.Out(param.br11_brn_bb("bb_my_pfor_preattach37"))


  //Connecting detach7 to bb_my_offload_pfor_body15
  bb_my_offload_pfor_body15.io.predicateIn <> detach7.io.Out(param.detach7_brn_bb("bb_my_offload_pfor_body15"))


  //Connecting detach7 to bb_my_pfor_inc
  bb_my_pfor_inc.io.predicateIn <> detach7.io.Out(param.detach7_brn_bb("bb_my_pfor_inc"))




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  mul0.io.enable <> bb_my_pfor_body8.io.Out(param.bb_my_pfor_body8_activate("mul0"))

  add1.io.enable <> bb_my_pfor_body8.io.Out(param.bb_my_pfor_body8_activate("add1"))

  br2.io.enable <> bb_my_pfor_body8.io.Out(param.bb_my_pfor_body8_activate("br2"))



  br3.io.enable <> bb_my_loopi.io.Out(param.bb_my_loopi_activate("br3"))



  phi4.io.enable <> bb_my_pfor_cond12.io.Out(param.bb_my_pfor_cond12_activate("phi4"))

  icmp5.io.enable <> bb_my_pfor_cond12.io.Out(param.bb_my_pfor_cond12_activate("icmp5"))

  br6.io.enable <> bb_my_pfor_cond12.io.Out(param.bb_my_pfor_cond12_activate("br6"))



  detach7.io.enable <> bb_my_pfor_detach14.io.Out(param.bb_my_pfor_detach14_activate("detach7"))



  add8.io.enable <> bb_my_pfor_inc.io.Out(param.bb_my_pfor_inc_activate("add8"))

  br9.io.enable <> bb_my_pfor_inc.io.Out(param.bb_my_pfor_inc_activate("br9"))



  sync10.io.enable <> bb_my_pfor_end.io.Out(param.bb_my_pfor_end_activate("sync10"))

  loop_L_0_liveIN_0.io.enable <> bb_my_pfor_end.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_my_pfor_end.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_my_pfor_end.io.Out(3)
  loop_L_0_liveIN_3.io.enable <> bb_my_pfor_end.io.Out(4)
  loop_L_0_liveIN_4.io.enable <> bb_my_pfor_end.io.Out(5)




  br11.io.enable <> bb_my_pfor_end_continue.io.Out(param.bb_my_pfor_end_continue_activate("br11"))



  ret12.io.enable <> bb_my_pfor_preattach37.io.Out(param.bb_my_pfor_preattach37_activate("ret12"))



  call13.io.In.enable <> bb_my_offload_pfor_body15.io.Out(param.bb_my_offload_pfor_body15_activate("call13"))

  reattach14.io.enable <> bb_my_offload_pfor_body15.io.Out(param.bb_my_offload_pfor_body15_activate("reattach14"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %m1.in
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32 %add.in
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field2")

  // Connecting instruction to the loop header
  //  %1 = add nsw i32 0, %0, !UID !9, !ScalaLabel !10
  loop_L_0_liveIN_2.io.InData <> add1.io.Out(param.call13_in("add1"))

  // Connecting function argument to the loop header
  //i32* %m2.in
  loop_L_0_liveIN_3.io.InData <> InputSplitter.io.Out.data("field3")

  // Connecting function argument to the loop header
  //i32* %prod.in
  loop_L_0_liveIN_4.io.InData <> InputSplitter.io.Out.data("field4")



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

  phi4.io.InData(param.phi4_phi_in("add8")) <> add8.io.Out(param.phi4_in("add8"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi4.io.Mask <> bb_my_pfor_cond12.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Binary instruction to the function argument
  mul0.io.LeftIO <> InputSplitter.io.Out.data("field0")

  // Wiring constant
  mul0.io.RightIO.bits.data := 2.U
  mul0.io.RightIO.bits.predicate := true.B
  mul0.io.RightIO.valid := true.B

  // Wiring constant
  add1.io.LeftIO.bits.data := 0.U
  add1.io.LeftIO.bits.predicate := true.B
  add1.io.LeftIO.valid := true.B

  // Wiring instructions
  add1.io.RightIO <> mul0.io.Out(param.add1_in("mul0"))

  // Wiring instructions
  icmp5.io.LeftIO <> phi4.io.Out(param.icmp5_in("phi4"))

  // Wiring constant
  icmp5.io.RightIO.bits.data := 4.U
  icmp5.io.RightIO.bits.predicate := true.B
  icmp5.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br6.io.CmpIO <> icmp5.io.Out(param.br6_in("icmp5"))

  // Wiring instructions
  add8.io.LeftIO <> phi4.io.Out(param.add8_in("phi4"))

  // Wiring constant
  add8.io.RightIO.bits.data := 1.U
  add8.io.RightIO.bits.predicate := true.B
  add8.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret12.io.In.elements("field0").bits.data := 1.U
  ret12.io.In.elements("field0").bits.predicate := true.B
  ret12.io.In.elements("field0").valid := true.B
  io.out <> ret12.io.Out


  // Wiring Call to I/O
  io.call13_out <> call13.io.callOut
  call13.io.retIn <> io.call13_in
  call13.io.Out.enable.ready := true.B // Manual fix
  // Wiring instructions
  call13.io.In.data("field0") <> phi4.io.Out(param.call13_in("phi4"))

  // Wiring instructions
  call13.io.In.data("field1") <> add1.io.Out(param.call13_in("add1"))

  // Wiring Call to the function argument
  call13.io.In.data("field2") <> InputSplitter.io.Out.data("field1")

  // Wiring Call to the function argument
  call13.io.In.data("field3") <> InputSplitter.io.Out.data("field2")

  // Wiring Call to the function argument
  call13.io.In.data("field4") <> InputSplitter.io.Out.data("field3")

  // Wiring Call to the function argument
  call13.io.In.data("field5") <> InputSplitter.io.Out.data("field4")



}

import java.io.{File, FileWriter}
object bgemm_detach2Main extends App {
  val dir = new File("RTL/bgemm_detach2") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new bgemm_detach2DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


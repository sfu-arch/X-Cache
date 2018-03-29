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

object Data_bgemm_detach1_FlowParam{

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_loopkk_pred = Map(
    "br2" -> 0
  )


  val bb_my_pfor_cond5_pred = Map(
    "br3" -> 0,
    "br9" -> 1
  )


  val bb_my_pfor_detach7_pred = Map(
    "br6" -> 0
  )


  val bb_my_pfor_end40_pred = Map(
    "br6" -> 0
  )


  val bb_my_pfor_preattach42_pred = Map(
    "br11" -> 0
  )


  val br2_brn_bb = Map(
    "bb_my_loopkk" -> 0
  )


  val br3_brn_bb = Map(
    "bb_my_pfor_cond5" -> 0
  )


  val br6_brn_bb = Map(
    "bb_my_pfor_detach7" -> 0,
    "bb_my_pfor_end40" -> 1
  )


  val br9_brn_bb = Map(
    "bb_my_pfor_cond5" -> 0
  )


  val br11_brn_bb = Map(
    "bb_my_pfor_preattach42" -> 0
  )


  val bb_my_pfor_inc38_pred = Map(
    "detach7" -> 0
  )


  val bb_my_offload_pfor_body8_pred = Map(
    "detach7" -> 0
  )


  val detach7_brn_bb = Map(
    "bb_my_offload_pfor_body8" -> 0,
    "bb_my_pfor_inc38" -> 1
  )


  val bb_my_pfor_body_activate = Map(
    "mul0" -> 0,
    "add1" -> 1,
    "br2" -> 2
  )


  val bb_my_loopkk_activate = Map(
    "br3" -> 0
  )


  val bb_my_pfor_cond5_activate = Map(
    "phi4" -> 0,
    "icmp5" -> 1,
    "br6" -> 2
  )


  val bb_my_pfor_detach7_activate = Map(
    "detach7" -> 0
  )


  val bb_my_pfor_inc38_activate = Map(
    "add8" -> 0,
    "br9" -> 1
  )


  val bb_my_pfor_end40_activate = Map(
    "sync10" -> 0
  )


  val bb_my_pfor_end_continue41_activate = Map(
    "br11" -> 0
  )


  val bb_my_pfor_preattach42_activate = Map(
    "ret12" -> 0
  )


  val bb_my_offload_pfor_body8_activate = Map(
    "call13" -> 0,
    "reattach14" -> 1
  )


  val phi4_phi_in = Map(
    "const_0" -> 0,
    "add8" -> 1
  )


  //  %0 = mul nsw i32 %__begin.0.in, 2, !UID !7, !ScalaLabel !8
  val mul0_in = Map(
    "field0" -> 0
  )


  //  %1 = add nsw i32 0, %0, !UID !9, !ScalaLabel !10
  val add1_in = Map(
    "mul0" -> 0
  )


  //  %2 = phi i32 [ 0, %my_loopkk ], [ %4, %my_pfor.inc38 ], !UID !17, !ScalaLabel !18
  val phi4_in = Map(
    "add8" -> 0
  )


  //  %3 = icmp slt i32 %2, 2, !UID !19, !ScalaLabel !20
  val icmp5_in = Map(
    "phi4" -> 0
  )


  //  br i1 %3, label %my_pfor.detach7, label %my_pfor.end40, !UID !21, !BB_UID !22, !ScalaLabel !23
  val br6_in = Map(
    "icmp5" -> 0
  )


  //  detach label %my_offload.pfor.body8, label %my_pfor.inc38, !UID !24, !BB_UID !25, !ScalaLabel !26
  val detach7_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %4 = add nsw i32 %2, 1, !UID !27, !ScalaLabel !28
  val add8_in = Map(
    "phi4" -> 1
  )


  //  sync label %my_pfor.end.continue41, !UID !46, !BB_UID !47, !ScalaLabel !48
  val sync10_in = Map(
    "" -> 2
  )


  //  ret void, !UID !52, !BB_UID !53, !ScalaLabel !54
  val ret12_in = Map(

  )


  //  call void @bgemm_detach2(i32 %2, i32* %m1.in, i32 %1, i32* %m2.in, i32* %prod.in), !UID !55, !ScalaLabel !56
  val call13_in = Map(
    "phi4" -> 2,
    "field1" -> 0,
    "add1" -> 0,
    "field2" -> 0,
    "field3" -> 0,
    "" -> 3
  )


  //  reattach label %my_pfor.inc38, !UID !57, !BB_UID !58, !ScalaLabel !59
  val reattach14_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class bgemm_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val call13_out = Decoupled(new Call(List(32,32,32,32,32)))
    val call13_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class bgemm_detach1DF(implicit p: Parameters) extends bgemm_detach1DFIO()(p) {



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

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_3 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_my_loopkk = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_my_pfor_cond5 = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 2))

  val bb_my_pfor_detach7 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_my_pfor_inc38 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 4))

  val bb_my_pfor_end40 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 5))

  val bb_my_pfor_end_continue41 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_my_pfor_preattach42 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7))

  val bb_my_offload_pfor_body8 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 8))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = mul nsw i32 %__begin.0.in, 2, !UID !7, !ScalaLabel !8
  val mul0 = Module (new ComputeNode(NumOuts = 1, ID = 0, opCode = "mul")(sign=false))


  //  %1 = add nsw i32 0, %0, !UID !9, !ScalaLabel !10
  val add1 = Module (new ComputeNode(NumOuts = 1, ID = 1, opCode = "add")(sign=false))


  //  br label %my_loopkk, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br2 = Module (new UBranchNode(ID = 2))

  // [BasicBlock]  my_loopkk:

  //  br label %my_pfor.cond5, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new UBranchNode(ID = 3))

  // [BasicBlock]  my_pfor.cond5:

  //  %2 = phi i32 [ 0, %my_loopkk ], [ %4, %my_pfor.inc38 ], !UID !17, !ScalaLabel !18
  val phi4 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 4))


  //  %3 = icmp slt i32 %2, 2, !UID !19, !ScalaLabel !20
  val icmp5 = Module (new IcmpNode(NumOuts = 1, ID = 5, opCode = "ULT")(sign=false))


  //  br i1 %3, label %my_pfor.detach7, label %my_pfor.end40, !UID !21, !BB_UID !22, !ScalaLabel !23
  val br6 = Module (new CBranchNode(ID = 6))

  // [BasicBlock]  my_pfor.detach7:

  //  detach label %my_offload.pfor.body8, label %my_pfor.inc38, !UID !24, !BB_UID !25, !ScalaLabel !26
  val detach7 = Module(new Detach(ID = 7))

  // [BasicBlock]  my_pfor.inc38:

  //  %4 = add nsw i32 %2, 1, !UID !27, !ScalaLabel !28
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  br label %my_pfor.cond5, !llvm.loop !29, !UID !43, !BB_UID !44, !ScalaLabel !45
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  my_pfor.end40:

  //  sync label %my_pfor.end.continue41, !UID !46, !BB_UID !47, !ScalaLabel !48
  val sync10 = Module(new Sync(ID = 10, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  my_pfor.end.continue41:

  //  br label %my_pfor.preattach42, !UID !49, !BB_UID !50, !ScalaLabel !51
  val br11 = Module (new UBranchNode(ID = 11))

  // [BasicBlock]  my_pfor.preattach42:

  //  ret void, !UID !52, !BB_UID !53, !ScalaLabel !54
  val ret12 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=12))

  // [BasicBlock]  my_offload.pfor.body8:

  //  call void @bgemm_detach2(i32 %2, i32* %m1.in, i32 %1, i32* %m2.in, i32* %prod.in), !UID !55, !ScalaLabel !56
  val call13 = Module(new CallNode(ID=13,argTypes=List(32,32,32,32,32),retTypes=List(32)))


  //  reattach label %my_pfor.inc38, !UID !57, !BB_UID !58, !ScalaLabel !59
  val reattach14 = Module(new Reattach(NumPredIn=1, ID=14))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_bgemm_detach1_FlowParam



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

  //Connecting br2 to bb_my_loopkk
  bb_my_loopkk.io.predicateIn <> br2.io.Out(param.br2_brn_bb("bb_my_loopkk"))


  //Connecting br3 to bb_my_pfor_cond5
  bb_my_pfor_cond5.io.predicateIn(param.bb_my_pfor_cond5_pred("br3")) <> br3.io.Out(param.br3_brn_bb("bb_my_pfor_cond5"))


  //Connecting br6 to bb_my_pfor_detach7
  bb_my_pfor_detach7.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_my_pfor_detach7"))


  //Connecting br6 to bb_my_pfor_end40
  bb_my_pfor_end40.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_my_pfor_end40"))


  //Connecting br9 to bb_my_pfor_cond5
  bb_my_pfor_cond5.io.predicateIn(param.bb_my_pfor_cond5_pred("br9")) <> br9.io.Out(param.br9_brn_bb("bb_my_pfor_cond5"))


  //Connecting br11 to bb_my_pfor_preattach42
  bb_my_pfor_preattach42.io.predicateIn <> br11.io.Out(param.br11_brn_bb("bb_my_pfor_preattach42"))


  //Connecting detach7 to bb_my_offload_pfor_body8
  bb_my_offload_pfor_body8.io.predicateIn <> detach7.io.Out(param.detach7_brn_bb("bb_my_offload_pfor_body8"))


  //Connecting detach7 to bb_my_pfor_inc38
  bb_my_pfor_inc38.io.predicateIn <> detach7.io.Out(param.detach7_brn_bb("bb_my_pfor_inc38"))




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  mul0.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("mul0"))

  add1.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("add1"))

  br2.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("br2"))



  br3.io.enable <> bb_my_loopkk.io.Out(param.bb_my_loopkk_activate("br3"))



  phi4.io.enable <> bb_my_pfor_cond5.io.Out(param.bb_my_pfor_cond5_activate("phi4"))

  icmp5.io.enable <> bb_my_pfor_cond5.io.Out(param.bb_my_pfor_cond5_activate("icmp5"))

  br6.io.enable <> bb_my_pfor_cond5.io.Out(param.bb_my_pfor_cond5_activate("br6"))



  detach7.io.enable <> bb_my_pfor_detach7.io.Out(param.bb_my_pfor_detach7_activate("detach7"))



  add8.io.enable <> bb_my_pfor_inc38.io.Out(param.bb_my_pfor_inc38_activate("add8"))

  br9.io.enable <> bb_my_pfor_inc38.io.Out(param.bb_my_pfor_inc38_activate("br9"))



  sync10.io.enable <> bb_my_pfor_end40.io.Out(param.bb_my_pfor_end40_activate("sync10"))

  loop_L_0_liveIN_0.io.enable <> bb_my_pfor_end40.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_my_pfor_end40.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_my_pfor_end40.io.Out(3)
  loop_L_0_liveIN_3.io.enable <> bb_my_pfor_end40.io.Out(4)




  br11.io.enable <> bb_my_pfor_end_continue41.io.Out(param.bb_my_pfor_end_continue41_activate("br11"))



  ret12.io.enable <> bb_my_pfor_preattach42.io.Out(param.bb_my_pfor_preattach42_activate("ret12"))



  call13.io.In.enable <> bb_my_offload_pfor_body8.io.Out(param.bb_my_offload_pfor_body8_activate("call13"))

  reattach14.io.enable <> bb_my_offload_pfor_body8.io.Out(param.bb_my_offload_pfor_body8_activate("reattach14"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %m1.in
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32* %m2.in
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field2")

  // Connecting function argument to the loop header
  //i32* %prod.in
  loop_L_0_liveIN_2.io.InData <> InputSplitter.io.Out.data("field3")

  // Connecting instruction to the loop header
  //  %1 = add nsw i32 0, %0, !UID !9, !ScalaLabel !10
  loop_L_0_liveIN_3.io.InData <> add1.io.Out(param.call13_in("add1"))



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

  phi4.io.Mask <> bb_my_pfor_cond5.io.MaskBB(0)



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
  icmp5.io.RightIO.bits.data := 2.U
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
  ret12.io.predicateIn(0).bits.control := true.B
  ret12.io.predicateIn(0).bits.taskID := 0.U
  ret12.io.predicateIn(0).valid := true.B
  ret12.io.In.data("field0").bits.data := 1.U
  ret12.io.In.data("field0").bits.predicate := true.B
  ret12.io.In.data("field0").valid := true.B
  io.out <> ret12.io.Out


  // Wiring Call to I/O
  io.call13_out <> call13.io.callOut
  call13.io.retIn <> io.call13_in
  call13.io.Out.enable.ready := true.B // Manual fix
  // Wiring instructions
  call13.io.In.data("field0") <> phi4.io.Out(param.call13_in("phi4"))

  // Wiring Call to the function argument
  call13.io.In.data("field1") <> InputSplitter.io.Out.data("field1")

  // Wiring instructions
  call13.io.In.data("field2") <> add1.io.Out(param.call13_in("add1"))

  // Wiring Call to the function argument
  call13.io.In.data("field3") <> InputSplitter.io.Out.data("field2")

  // Wiring Call to the function argument
  call13.io.In.data("field4") <> InputSplitter.io.Out.data("field3")



}

import java.io.{File, FileWriter}
object bgemm_detach1Main extends App {
  val dir = new File("RTL/bgemm_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new bgemm_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


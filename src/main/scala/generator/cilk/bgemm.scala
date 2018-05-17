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

object Data_bgemm_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_loopjj_pred = Map(
    "br0" -> 0
  )


  val bb_pfor_cond_pred = Map(
    "br1" -> 0,
    "br7" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br4" -> 0
  )


  val bb_pfor_end45_pred = Map(
    "br4" -> 0
  )


  val br0_brn_bb = Map(
    "bb_loopjj" -> 0
  )


  val br1_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val br4_brn_bb = Map(
    "bb_pfor_detach" -> 0,
    "bb_pfor_end45" -> 1
  )


  val br7_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val bb_pfor_inc43_pred = Map(
    "detach5" -> 0
  )


  val bb_offload_pfor_body_pred = Map(
    "detach5" -> 0
  )


  val detach5_brn_bb = Map(
    "bb_offload_pfor_body" -> 0,
    "bb_pfor_inc43" -> 1
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_loopjj_activate = Map(
    "br1" -> 0
  )


  val bb_pfor_cond_activate = Map(
    "phi2" -> 0,
    "icmp3" -> 1,
    "br4" -> 2
  )


  val bb_pfor_detach_activate = Map(
    "detach5" -> 0
  )


  val bb_pfor_inc43_activate = Map(
    "add6" -> 0,
    "br7" -> 1
  )


  val bb_pfor_end45_activate = Map(
    "sync8" -> 0
  )


  val bb_pfor_end_continue46_activate = Map(
    "ret9" -> 0
  )


  val bb_offload_pfor_body_activate = Map(
    "call10" -> 0,
    "reattach11" -> 1
  )


  val phi2_phi_in = Map(
    "const_0" -> 0,
    "add6" -> 1
  )


  //  %__begin.0 = phi i32 [ 0, %loopjj ], [ %inc44, %pfor.inc43 ], !UID !13, !ScalaLabel !14
  val phi2_in = Map(
    "add6" -> 0
  )


  //  %cmp = icmp slt i32 %__begin.0, 2, !UID !15, !ScalaLabel !16
  val icmp3_in = Map(
    "phi2" -> 0
  )


  //  br i1 %cmp, label %pfor.detach, label %pfor.end45, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br4_in = Map(
    "icmp3" -> 0
  )


  //  detach label %offload.pfor.body, label %pfor.inc43, !UID !20, !BB_UID !21, !ScalaLabel !22
  val detach5_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %inc44 = add nsw i32 %__begin.0, 1, !UID !23, !ScalaLabel !24
  val add6_in = Map(
    "phi2" -> 1
  )


  //  sync label %pfor.end.continue46, !UID !38, !BB_UID !39, !ScalaLabel !40
  val sync8_in = Map(
    "" -> 2
  )


  //  ret void, !UID !41, !BB_UID !42, !ScalaLabel !43
  val ret9_in = Map(

  )


  //  call void @bgemm_detach1(i32 %__begin.0, i32* %m1, i32* %m2, i32* %prod), !UID !44, !ScalaLabel !45
  val call10_in = Map(
    "phi2" -> 2,
    "field0" -> 0,
    "field1" -> 0,
    "field2" -> 0,
    "" -> 3
  )


  //  reattach label %pfor.inc43, !UID !46, !BB_UID !47, !ScalaLabel !48
  val reattach11_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class bgemmDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val call10_out = Decoupled(new Call(List(32,32,32,32)))
    val call10_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class bgemmDF(implicit p: Parameters) extends bgemmDFIO()(p) {



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

  val InputSplitter = Module(new SplitCall(List(32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_loopjj = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_pfor_cond = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 2))

  val bb_pfor_detach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_pfor_inc43 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 4))

  val bb_pfor_end45 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_pfor_end_continue46 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_offload_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 7))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %loopjj, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  loopjj:

  //  br label %pfor.cond, !UID !10, !BB_UID !11, !ScalaLabel !12
  val br1 = Module (new UBranchNode(ID = 1))

  // [BasicBlock]  pfor.cond:

  //  %__begin.0 = phi i32 [ 0, %loopjj ], [ %inc44, %pfor.inc43 ], !UID !13, !ScalaLabel !14
  val phi2 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 2))


  //  %cmp = icmp slt i32 %__begin.0, 2, !UID !15, !ScalaLabel !16
  val icmp3 = Module (new IcmpNode(NumOuts = 1, ID = 3, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %pfor.detach, label %pfor.end45, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br4 = Module (new CBranchNode(ID = 4))

  // [BasicBlock]  pfor.detach:

  //  detach label %offload.pfor.body, label %pfor.inc43, !UID !20, !BB_UID !21, !ScalaLabel !22
  val detach5 = Module(new Detach(ID = 5))

  // [BasicBlock]  pfor.inc43:

  //  %inc44 = add nsw i32 %__begin.0, 1, !UID !23, !ScalaLabel !24
  val add6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))


  //  br label %pfor.cond, !llvm.loop !25, !UID !35, !BB_UID !36, !ScalaLabel !37
  val br7 = Module (new UBranchNode(ID = 7))

  // [BasicBlock]  pfor.end45:

  //  sync label %pfor.end.continue46, !UID !38, !BB_UID !39, !ScalaLabel !40
  val sync8 = Module(new Sync(ID = 8, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  pfor.end.continue46:

  //  ret void, !UID !41, !BB_UID !42, !ScalaLabel !43
  val ret9 = Module(new RetNode(retTypes=List(32), ID=9))

  // [BasicBlock]  offload.pfor.body:

  //  call void @bgemm_detach1(i32 %__begin.0, i32* %m1, i32* %m2, i32* %prod), !UID !44, !ScalaLabel !45
  val call10 = Module(new CallNode(ID=10,argTypes=List(32,32,32,32),retTypes=List(32)))


  //  reattach label %pfor.inc43, !UID !46, !BB_UID !47, !ScalaLabel !48
  val reattach11 = Module(new Reattach(NumPredOps=1, ID=11))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_bgemm_FlowParam



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

  //Connecting br0 to bb_loopjj
  bb_loopjj.io.predicateIn <> br0.io.Out(param.br0_brn_bb("bb_loopjj"))


  //Connecting br1 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br1")) <> br1.io.Out(param.br1_brn_bb("bb_pfor_cond"))


  //Connecting br4 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_pfor_detach"))


  //Connecting br4 to bb_pfor_end45
  bb_pfor_end45.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_pfor_end45"))


  //Connecting br7 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br7")) <> br7.io.Out(param.br7_brn_bb("bb_pfor_cond"))


  //Connecting detach5 to bb_offload_pfor_body
  bb_offload_pfor_body.io.predicateIn <> detach5.io.Out(param.detach5_brn_bb("bb_offload_pfor_body"))


  //Connecting detach5 to bb_pfor_inc43
  bb_pfor_inc43.io.predicateIn <> detach5.io.Out(param.detach5_brn_bb("bb_pfor_inc43"))




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  br1.io.enable <> bb_loopjj.io.Out(param.bb_loopjj_activate("br1"))



  phi2.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("phi2"))

  icmp3.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("icmp3"))

  br4.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("br4"))



  detach5.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach5"))



  add6.io.enable <> bb_pfor_inc43.io.Out(param.bb_pfor_inc43_activate("add6"))

  br7.io.enable <> bb_pfor_inc43.io.Out(param.bb_pfor_inc43_activate("br7"))



  sync8.io.enable <> bb_pfor_end45.io.Out(param.bb_pfor_end45_activate("sync8"))

  loop_L_0_liveIN_0.io.enable <> bb_pfor_end45.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_pfor_end45.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_pfor_end45.io.Out(3)




  ret9.io.enable <> bb_pfor_end_continue46.io.Out(param.bb_pfor_end_continue46_activate("ret9"))



  call10.io.In.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("call10"))

  reattach11.io.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach11"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %m1
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32* %m2
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32* %prod
  loop_L_0_liveIN_2.io.InData <> InputSplitter.io.Out.data("field2")



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

  phi2.io.InData(param.phi2_phi_in("add6")) <> add6.io.Out(param.phi2_in("add6"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi2.io.Mask <> bb_pfor_cond.io.MaskBB(0)



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

  // Wiring instructions
  add6.io.LeftIO <> phi2.io.Out(param.add6_in("phi2"))

  // Wiring constant
  add6.io.RightIO.bits.data := 1.U
  add6.io.RightIO.bits.predicate := true.B
  add6.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret9.io.In.data("field0").bits.data := 1.U
  ret9.io.In.data("field0").bits.predicate := true.B
  ret9.io.In.data("field0").valid := true.B
  io.out <> ret9.io.Out


  // Wiring Call to I/O
  io.call10_out <> call10.io.callOut
  call10.io.retIn <> io.call10_in
  call10.io.Out.enable.ready := true.B // Manual fix
  // Wiring instructions
  call10.io.In.data("field0") <> phi2.io.Out(param.call10_in("phi2"))

  // Wiring Call to the function argument
  call10.io.In.data("field1") <> InputSplitter.io.Out.data("field0")

  // Wiring Call to the function argument
  call10.io.In.data("field2") <> InputSplitter.io.Out.data("field1")

  // Wiring Call to the function argument
  call10.io.In.data("field3") <> InputSplitter.io.Out.data("field2")



}

import java.io.{File, FileWriter}
object bgemmMain extends App {
  val dir = new File("RTL/bgemm") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new bgemmDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


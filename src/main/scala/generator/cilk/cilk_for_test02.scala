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

object Data_cilk_for_test02_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_pfor_cond_pred = Map(
    "br0" -> 0,
    "br9" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br3" -> 0
  )


  val bb_pfor_end_pred = Map(
    "br3" -> 0
  )


  val bb_pfor_preattach_pred = Map(
    "br6" -> 0
  )


  val br0_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val br3_brn_bb = Map(
    "bb_pfor_detach" -> 0,
    "bb_pfor_end" -> 1
  )


  val br6_brn_bb = Map(
    "bb_pfor_preattach" -> 0
  )


  val br9_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val bb_pfor_inc_pred = Map(
    "detach4" -> 0
  )


  val bb_pfor_body_pred = Map(
    "detach4" -> 0
  )


  val detach4_brn_bb = Map(
    "bb_pfor_body" -> 0,
    "bb_pfor_inc" -> 1
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_pfor_cond_activate = Map(
    "phi1" -> 0,
    "icmp2" -> 1,
    "br3" -> 2
  )


  val bb_pfor_detach_activate = Map(
    "detach4" -> 0
  )


  val bb_pfor_body_activate = Map(
    "call5" -> 0,
    "br6" -> 1
  )


  val bb_pfor_preattach_activate = Map(
    "reattach7" -> 0
  )


  val bb_pfor_inc_activate = Map(
    "add8" -> 0,
    "br9" -> 1
  )


  val bb_pfor_end_activate = Map(
    "sync10" -> 0
  )


  val bb_pfor_end_continue_activate = Map(
    "ret11" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add8" -> 1
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add8" -> 0
  )


  //  %cmp = icmp ult i32 %i.0, 5, !UID !12, !ScalaLabel !13
  val icmp2_in = Map(
    "phi1" -> 0
  )


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  detach label %pfor.body, label %pfor.inc, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  call void @cilk_for_test02_mul(i32* %a, i32* %b, i32 %i.0), !UID !20, !ScalaLabel !21
  val call5_in = Map(
    "field0" -> 0,
    "field1" -> 0,
    "phi1" -> 1,
    "" -> 2
  )


  //  reattach label %pfor.inc, !UID !25, !BB_UID !26, !ScalaLabel !27
  val reattach7_in = Map(
    "" -> 3
  )


  //  %inc = add i32 %i.0, 1, !UID !28, !ScalaLabel !29
  val add8_in = Map(
    "phi1" -> 2
  )


  //  sync label %pfor.end.continue, !UID !43, !BB_UID !44, !ScalaLabel !45
  val sync10_in = Map(
    "" -> 4
  )


  //  ret void, !UID !46, !BB_UID !47, !ScalaLabel !48
  val ret11_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test02DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call5_out = Decoupled(new Call(List(32,32,32)))
    val call5_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test02DF(implicit p: Parameters) extends cilk_for_test02DFIO()(p) {



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

  val InputSplitter = Module(new SplitCall(List(32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_10_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_10_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_pfor_cond = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_pfor_detach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 5))

  val bb_pfor_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 6))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %pfor.cond, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  pfor.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !10, !ScalaLabel !11
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 3, ID = 1))


  //  %cmp = icmp ult i32 %i.0, 5, !UID !12, !ScalaLabel !13
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new CBranchNode(ID = 3))

  // [BasicBlock]  pfor.detach:

  //  detach label %pfor.body, label %pfor.inc, !UID !17, !BB_UID !18, !ScalaLabel !19
  val detach4 = Module(new Detach(ID = 4))

  // [BasicBlock]  pfor.body:

  //  call void @cilk_for_test02_mul(i32* %a, i32* %b, i32 %i.0), !UID !20, !ScalaLabel !21
  val call5 = Module(new CallNode(ID=5,argTypes=List(32,32,32),retTypes=List(32)))


  //  br label %pfor.preattach, !UID !22, !BB_UID !23, !ScalaLabel !24
  val br6 = Module (new UBranchNode(ID = 6))

  // [BasicBlock]  pfor.preattach:

  //  reattach label %pfor.inc, !UID !25, !BB_UID !26, !ScalaLabel !27
  val reattach7 = Module(new Reattach(NumPredOps=1, ID=7))

  // [BasicBlock]  pfor.inc:

  //  %inc = add i32 %i.0, 1, !UID !28, !ScalaLabel !29
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  br label %pfor.cond, !llvm.loop !30, !UID !40, !BB_UID !41, !ScalaLabel !42
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  pfor.end:

  //  sync label %pfor.end.continue, !UID !43, !BB_UID !44, !ScalaLabel !45
  val sync10 = Module(new Sync(ID = 10, NumInc=1, NumDec=1, NumOuts=1))

  // [BasicBlock]  pfor.end.continue:

  //  ret void, !UID !46, !BB_UID !47, !ScalaLabel !48
  val ret11 = Module(new RetNode(retTypes=List(32), ID=11))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test02_FlowParam



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

  //Connecting br0 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br0")) <> br0.io.Out(param.br0_brn_bb("bb_pfor_cond"))


  //Connecting br3 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_pfor_detach"))


  //Connecting br3 to bb_pfor_end
  bb_pfor_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_pfor_end"))


  //Connecting br6 to bb_pfor_preattach
  bb_pfor_preattach.io.predicateIn <> br6.io.Out(param.br6_brn_bb("bb_pfor_preattach"))


  //Connecting br9 to bb_pfor_cond
  bb_pfor_cond.io.predicateIn(param.bb_pfor_cond_pred("br9")) <> br9.io.Out(param.br9_brn_bb("bb_pfor_cond"))


  //Connecting detach4 to bb_pfor_body
  bb_pfor_body.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_pfor_body"))


  //Connecting detach4 to bb_pfor_inc
  bb_pfor_inc.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_pfor_inc"))

  bb_pfor_end_continue.io.predicateIn <> sync10.io.Out(0) // added manually



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  phi1.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("phi1"))

  icmp2.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("icmp2"))

  br3.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("br3"))



  detach4.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach4"))



  call5.io.In.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("call5"))

  br6.io.enable <> bb_pfor_body.io.Out(param.bb_pfor_body_activate("br6"))


  bb_pfor_body.io.Out(0).ready := true.B // Manually added

  reattach7.io.enable <> bb_pfor_preattach.io.Out(param.bb_pfor_preattach_activate("reattach7"))



  add8.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("add8"))

  br9.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("br9"))



  sync10.io.enable <> bb_pfor_end.io.Out(param.bb_pfor_end_activate("sync10"))

  loop_L_10_liveIN_0.io.enable <> bb_pfor_end.io.Out(1)
  loop_L_10_liveIN_1.io.enable <> bb_pfor_end.io.Out(2)




  ret11.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("ret11"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %a
  loop_L_10_liveIN_0.io.InData <> InputSplitter.io.Out.data.elements("field0")

  // Connecting function argument to the loop header
  //i32* %b
  loop_L_10_liveIN_1.io.InData <> InputSplitter.io.Out.data.elements("field1")



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

  phi1.io.InData(param.phi1_phi_in("add8")) <> add8.io.Out(param.phi1_in("add8"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_pfor_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp2.io.LeftIO <> phi1.io.Out(param.icmp2_in("phi1"))

  // Wiring constant
  icmp2.io.RightIO.bits.data := 5.U
  icmp2.io.RightIO.bits.predicate := true.B
  icmp2.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring Call to I/O
  io.call5_out <> call5.io.callOut
  call5.io.retIn <> io.call5_in
  call5.io.Out.enable.ready := true.B // Manual fix
  // Wiring Call instruction to the loop header
  call5.io.In.data.elements("field0") <>loop_L_10_liveIN_0.io.Out(param.call5_in("field0")) // Manually corrected

  // Wiring Call instruction to the loop header
  call5.io.In.data.elements("field1") <>loop_L_10_liveIN_1.io.Out(param.call5_in("field1"))

  // Wiring Call instruction to the loop header
  call5.io.In.data.elements("field2") <>phi1.io.Out(param.call5_in("phi1")) // Manually corrected

  // Reattach (Manual add)
  reattach7.io.predicateIn(0) <> call5.io.Out.data.elements("field0")

  // Sync (Manual add)
  sync10.io.incIn(0) <> detach4.io.Out(2)
  sync10.io.decIn(0) <> reattach7.io.Out(0)


  // Wiring instructions
  add8.io.LeftIO <> phi1.io.Out(param.add8_in("phi1"))

  // Wiring constant
  add8.io.RightIO.bits.data := 1.U
  add8.io.RightIO.bits.predicate := true.B
  add8.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret11.io.In.elements("field0").bits.data := 1.U
  ret11.io.In.elements("field0").bits.predicate := true.B
  ret11.io.In.elements("field0").valid := true.B
  io.out <> ret11.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test02Main extends App {
  val dir = new File("RTL/cilk_for_test02") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test02DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


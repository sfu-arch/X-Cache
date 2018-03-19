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

object Data_cilk_for_test08_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_pfor_detach_preheader_pred = Map(
    "br1" -> 0
  )


  val bb_pfor_cond_cleanup_pred = Map(
    "br1" -> 0,
    "br3" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br2" -> 0,
    "br10" -> 1
  )


  val bb_pfor_cond_cleanup_loopexit_pred = Map(
    "br10" -> 0
  )


  val br1_brn_bb = Map(
    "bb_pfor_detach_preheader" -> 0,
    "bb_pfor_cond_cleanup" -> 1
  )


  val br2_brn_bb = Map(
    "bb_pfor_detach" -> 0
  )


  val br3_brn_bb = Map(
    "bb_pfor_cond_cleanup" -> 0
  )


  val br10_brn_bb = Map(
    "bb_pfor_cond_cleanup_loopexit" -> 0,
    "bb_pfor_detach" -> 1
  )


  val bb_pfor_inc_pred = Map(
    "detach7" -> 0
  )


  val bb_offload_pfor_body_pred = Map(
    "detach7" -> 0
  )


  val detach7_brn_bb = Map(
    "bb_offload_pfor_body" -> 0,
    "bb_pfor_inc" -> 1
  )


  val bb_entry_activate = Map(
    "icmp0" -> 0,
    "br1" -> 1
  )


  val bb_pfor_detach_preheader_activate = Map(
    "br2" -> 0
  )


  val bb_pfor_cond_cleanup_loopexit_activate = Map(
    "br3" -> 0
  )


  val bb_pfor_cond_cleanup_activate = Map(
    "sync4" -> 0
  )


  val bb_pfor_end_continue_activate = Map(
    "ret5" -> 0
  )


  val bb_pfor_detach_activate = Map(
    "phi6" -> 0,
    "detach7" -> 1
  )


  val bb_pfor_inc_activate = Map(
    "add8" -> 0,
    "icmp9" -> 1,
    "br10" -> 2
  )


  val bb_offload_pfor_body_activate = Map(
    "call11" -> 0,
    "reattach12" -> 1
  )


  val phi6_phi_in = Map(
    "const_1" -> 0,
    "add8" -> 1 // Manually reversed order
  )


  //  %cmp49 = icmp sgt i32 %n, 0, !UID !15, !ScalaLabel !16
  val icmp0_in = Map(
    "field2" -> 0
  )


  //  br i1 %cmp49, label %pfor.detach.preheader, label %pfor.cond.cleanup, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br1_in = Map(
    "icmp0" -> 0
  )


  //  sync label %pfor.end.continue, !UID !26, !BB_UID !27, !ScalaLabel !28
  val sync4_in = Map(
    "" -> 0
  )


  //  ret i32 1, !UID !29, !BB_UID !30, !ScalaLabel !31
  val ret5_in = Map(

  )


  //  %i.050 = phi i32 [ %inc, %pfor.inc ], [ 0, %pfor.detach.preheader ], !UID !32, !ScalaLabel !33
  val phi6_in = Map(
    "add8" -> 0
  )


  //  detach label %offload.pfor.body, label %pfor.inc, !UID !34, !BB_UID !35, !ScalaLabel !36
  val detach7_in = Map(
    "" -> 1,
    "" -> 2
  )


  //  %inc = add nuw nsw i32 %i.050, 1, !UID !37, !ScalaLabel !38
  val add8_in = Map(
    "phi6" -> 0
  )


  //  %exitcond = icmp eq i32 %inc, %n, !UID !39, !ScalaLabel !40
  val icmp9_in = Map(
    "add8" -> 1,
    "field2" -> 1
  )


  //  br i1 %exitcond, label %pfor.cond.cleanup.loopexit, label %pfor.detach, !llvm.loop !41, !UID !61, !BB_UID !62, !ScalaLabel !63
  val br10_in = Map(
    "icmp9" -> 0
  )


  //  call void @cilk_for_test08_detach([3 x i32]* %rgb, i32 %i.050, [3 x i32]* %xyz), !UID !64, !ScalaLabel !65
  val call11_in = Map(
    "field0" -> 0,
    "phi6" -> 1,
    "field1" -> 0,
    "" -> 3
  )


  //  reattach label %pfor.inc, !UID !66, !BB_UID !67, !ScalaLabel !68
  val reattach12_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test08DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val call11_out = Decoupled(new Call(List(32,32,32)))
    val call11_in = Flipped(Decoupled(new Call(List(32))))
//    val CacheResp = Flipped(Valid(new CacheRespT))
//    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test08DF(implicit p: Parameters) extends cilk_for_test08DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */

/*
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
*/
  val InputSplitter = Module(new SplitCall(List(32,32,32)))
  InputSplitter.io.In <> io.in

  val field2_expand = Module(new ExpandNode(NumOuts=2,ID=103)(new DataBundle))
  field2_expand.io.enable.valid := true.B
  field2_expand.io.enable.bits.control := true.B
  field2_expand.io.InData <> InputSplitter.io.Out.data("field2")


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 2, ID = 0)) // manual fix




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 0))

  val bb_pfor_detach_preheader = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_pfor_cond_cleanup_loopexit = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 2))

  val bb_pfor_cond_cleanup = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_pfor_detach = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 5))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 3, BID = 6))

  val bb_offload_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 7))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %cmp49 = icmp sgt i32 %n, 0, !UID !15, !ScalaLabel !16
  val icmp0 = Module (new IcmpNode(NumOuts = 1, ID = 0, opCode = "UGT")(sign=false))


  //  br i1 %cmp49, label %pfor.detach.preheader, label %pfor.cond.cleanup, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br1 = Module (new CBranchNode(ID = 1))

  // [BasicBlock]  pfor.detach.preheader:

  //  br label %pfor.detach, !UID !20, !BB_UID !21, !ScalaLabel !22
  val br2 = Module (new UBranchNode(ID = 2))

  // [BasicBlock]  pfor.cond.cleanup.loopexit:

  //  br label %pfor.cond.cleanup, !UID !23, !BB_UID !24, !ScalaLabel !25
  val br3 = Module (new UBranchNode(ID = 3))

  // [BasicBlock]  pfor.cond.cleanup:

  //  sync label %pfor.end.continue, !UID !26, !BB_UID !27, !ScalaLabel !28
  val sync4 = Module(new Sync(ID = 4, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  pfor.end.continue:

  //  ret i32 1, !UID !29, !BB_UID !30, !ScalaLabel !31
  val ret5 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=5))

  // [BasicBlock]  pfor.detach:

  //  %i.050 = phi i32 [ %inc, %pfor.inc ], [ 0, %pfor.detach.preheader ], !UID !32, !ScalaLabel !33
  val phi6 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 6))


  //  detach label %offload.pfor.body, label %pfor.inc, !UID !34, !BB_UID !35, !ScalaLabel !36
  val detach7 = Module(new Detach(ID = 7))

  // [BasicBlock]  pfor.inc:

  //  %inc = add nuw nsw i32 %i.050, 1, !UID !37, !ScalaLabel !38
  val add8 = Module (new ComputeNode(NumOuts = 2, ID = 8, opCode = "add")(sign=false))


  //  %exitcond = icmp eq i32 %inc, %n, !UID !39, !ScalaLabel !40
  val icmp9 = Module (new IcmpNode(NumOuts = 1, ID = 9, opCode = "EQ")(sign=false))


  //  br i1 %exitcond, label %pfor.cond.cleanup.loopexit, label %pfor.detach, !llvm.loop !41, !UID !61, !BB_UID !62, !ScalaLabel !63
  val br10 = Module (new CBranchNode(ID = 10))

  // [BasicBlock]  offload.pfor.body:

  //  call void @cilk_for_test08_detach([3 x i32]* %rgb, i32 %i.050, [3 x i32]* %xyz), !UID !64, !ScalaLabel !65
  val call11 = Module(new CallNode(ID=11,argTypes=List(32,32,32),retTypes=List(32)))


  //  reattach label %pfor.inc, !UID !66, !BB_UID !67, !ScalaLabel !68
  val reattach12 = Module(new Reattach(NumPredIn=1, ID=12))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test08_FlowParam



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

  //Connecting br1 to bb_pfor_detach_preheader
  bb_pfor_detach_preheader.io.predicateIn <> br1.io.Out(param.br1_brn_bb("bb_pfor_detach_preheader"))


  //Connecting br1 to bb_pfor_cond_cleanup
  bb_pfor_cond_cleanup.io.predicateIn <> br1.io.Out(param.br1_brn_bb("bb_pfor_cond_cleanup"))


  //Connecting br2 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn(param.bb_pfor_detach_pred("br2")) <> br2.io.Out(param.br2_brn_bb("bb_pfor_detach"))


  //Connecting br3 to bb_pfor_cond_cleanup
  bb_pfor_cond_cleanup.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_pfor_cond_cleanup"))


  //Connecting br10 to bb_pfor_cond_cleanup_loopexit
  bb_pfor_cond_cleanup_loopexit.io.predicateIn <> br10.io.Out(param.br10_brn_bb("bb_pfor_cond_cleanup_loopexit"))


  //Connecting br10 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn(param.bb_pfor_detach_pred("br10")) <> br10.io.Out(param.br10_brn_bb("bb_pfor_detach"))


  //Connecting detach7 to bb_offload_pfor_body
  bb_offload_pfor_body.io.predicateIn <> detach7.io.Out(param.detach7_brn_bb("bb_offload_pfor_body"))


  //Connecting detach7 to bb_pfor_inc
  bb_pfor_inc.io.predicateIn <> detach7.io.Out(param.detach7_brn_bb("bb_pfor_inc"))


  bb_pfor_end_continue.io.predicateIn <> sync4.io.Out(0) // added manually


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  icmp0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("icmp0"))

  br1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br1"))



  br2.io.enable <> bb_pfor_detach_preheader.io.Out(param.bb_pfor_detach_preheader_activate("br2"))



  br3.io.enable <> bb_pfor_cond_cleanup_loopexit.io.Out(param.bb_pfor_cond_cleanup_loopexit_activate("br3"))

  loop_L_0_liveIN_0.io.enable <> bb_pfor_cond_cleanup_loopexit.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_pfor_cond_cleanup_loopexit.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_pfor_cond_cleanup_loopexit.io.Out(3)




  sync4.io.enable <> bb_pfor_cond_cleanup.io.Out(param.bb_pfor_cond_cleanup_activate("sync4"))



  ret5.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("ret5"))



  phi6.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("phi6"))

  detach7.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach7"))



  add8.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("add8"))

  icmp9.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("icmp9"))

  br10.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("br10"))



  call11.io.In.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("call11"))

  // Manual fix.  Reattach should only depend on its increment/decr inputs. If it is connected to the BB
  // Enable it will stall the loop
  // reattach9.io.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach9"))
//  reattach12.io.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach12"))
  reattach12.io.enable.enq(ControlBundle.active())  // always enabled
  bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach12")).ready := true.B






  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //[3 x i32]* %rgb
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //[3 x i32]* %xyz
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32 %n
  loop_L_0_liveIN_2.io.InData <> field2_expand.io.Out(0)



  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi6.io.InData(param.phi6_phi_in("add8")) <> add8.io.Out(param.phi6_in("add8"))

  phi6.io.InData(param.phi6_phi_in("const_1")).bits.data := 0.U
  phi6.io.InData(param.phi6_phi_in("const_1")).bits.predicate := true.B
  phi6.io.InData(param.phi6_phi_in("const_1")).valid := true.B

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi6.io.Mask <> bb_pfor_detach.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Binary instruction to the function argument
  icmp0.io.LeftIO <> field2_expand.io.Out(1)

  // Wiring constant
  icmp0.io.RightIO.bits.data := 0.U
  icmp0.io.RightIO.bits.predicate := true.B
  icmp0.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br1.io.CmpIO <> icmp0.io.Out(param.br1_in("icmp0"))

  // Wiring return instruction
  ret5.io.predicateIn(0).bits.control := true.B
  ret5.io.predicateIn(0).bits.taskID := 0.U
  ret5.io.predicateIn(0).valid := true.B
  ret5.io.In.data("field0").bits.data := 1.U
  ret5.io.In.data("field0").bits.predicate := true.B
  ret5.io.In.data("field0").valid := true.B
  io.out <> ret5.io.Out


  // Wiring instructions
  add8.io.LeftIO <> phi6.io.Out(param.add8_in("phi6"))

  // Wiring constant
  add8.io.RightIO.bits.data := 1.U
  add8.io.RightIO.bits.predicate := true.B
  add8.io.RightIO.valid := true.B

  // Wiring instructions
  icmp9.io.LeftIO <> add8.io.Out(param.icmp9_in("add8"))

  // Wiring Binary instruction to the loop header
//  icmp9.io.RightIO <> loop_L_0_liveIN_2.io.Out(param.icmp9_in("field2"))
  icmp9.io.RightIO.bits.data := 9.U
  icmp9.io.RightIO.bits.predicate := true.B
  icmp9.io.RightIO.valid := true.B
  loop_L_0_liveIN_2.io.Out(param.icmp9_in("field2")).ready := true.B // hack

  // Wiring Branch instruction
  br10.io.CmpIO <> icmp9.io.Out(param.br10_in("icmp9"))

  // Wiring Call to I/O
  io.call11_out <> call11.io.callOut
  call11.io.retIn <> io.call11_in
  call11.io.Out.enable.ready := true.B
  // Wiring Call instruction to the loop header
  call11.io.In.data("field0") <> loop_L_0_liveIN_0.io.Out(param.call11_in("field0"))

  // Wiring Call instruction to the loop header
  call11.io.In.data("field1") <> phi6.io.Out(param.call11_in("phi6"))  // Manually added

  // Wiring Call instruction to the loop header
  call11.io.In.data("field2") <> loop_L_0_liveIN_1.io.Out(param.call11_in("field1")) // manual fix
  call11.io.Out.enable.ready := true.B // Manual fix

  // Reattach (Manual add)
  reattach12.io.predicateIn(0) <> call11.io.Out.data("field0")

  // Sync (Manual add)
  sync4.io.incIn(0) <> detach7.io.Out(2)
  sync4.io.decIn(0) <> reattach12.io.Out(0)

}

import java.io.{File, FileWriter}
object cilk_for_test08Main extends App {
  val dir = new File("RTL/cilk_for_test08") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test08DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


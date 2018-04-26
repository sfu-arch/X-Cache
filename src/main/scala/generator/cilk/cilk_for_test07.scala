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

object Data_cilk_for_test07_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_pfor_detach_pred = Map(
    "br0" -> 0,
    "br7" -> 1
  )


  val bb_pfor_cond_cleanup_pred = Map(
    "br7" -> 0
  )


  val br0_brn_bb = Map(
    "bb_pfor_detach" -> 0
  )


  val br7_brn_bb = Map(
    "bb_pfor_cond_cleanup" -> 0,
    "bb_pfor_detach" -> 1
  )


  val bb_pfor_inc_pred = Map(
    "detach4" -> 0
  )


  val bb_offload_pfor_body_pred = Map(
    "detach4" -> 0
  )


  val detach4_brn_bb = Map(
    "bb_offload_pfor_body" -> 0,
    "bb_pfor_inc" -> 1
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_pfor_cond_cleanup_activate = Map(
    "sync1" -> 0
  )


  val bb_pfor_end_continue_activate = Map(
    "ret2" -> 0
  )


  val bb_pfor_detach_activate = Map(
    "phi3" -> 0,
    "detach4" -> 1
  )


  val bb_pfor_inc_activate = Map(
    "add5" -> 0,
    "icmp6" -> 1,
    "br7" -> 2
  )


  val bb_offload_pfor_body_activate = Map(
    "call8" -> 0,
    "reattach9" -> 1
  )


  val phi3_phi_in = Map(
    "const_0" -> 0,
    "add5" -> 1
  )


  //  sync label %pfor.end.continue, !UID !10, !BB_UID !11, !ScalaLabel !12
  val sync1_in = Map(
    "" -> 0
  )


  //  ret i32 1, !UID !13, !BB_UID !14, !ScalaLabel !15
  val ret2_in = Map(

  )


  //  %i.032 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !16, !ScalaLabel !17
  val phi3_in = Map(
    "add5" -> 0
  )


  //  detach label %offload.pfor.body, label %pfor.inc, !UID !18, !BB_UID !19, !ScalaLabel !20
  val detach4_in = Map(
    "" -> 1,
    "" -> 2
  )


  //  %inc = add nuw nsw i32 %i.032, 1, !UID !21, !ScalaLabel !22
  val add5_in = Map(
    "phi3" -> 0
  )


  //  %exitcond = icmp eq i32 %inc, 9, !UID !23, !ScalaLabel !24
  val icmp6_in = Map(
    "add5" -> 1
  )


  //  br i1 %exitcond, label %pfor.cond.cleanup, label %pfor.detach, !llvm.loop !25, !UID !50, !BB_UID !51, !ScalaLabel !52
  val br7_in = Map(
    "icmp6" -> 0
  )


  //  call void @cilk_for_test07_detach([2 x i32]* %p1, i32 %i.032, [2 x i32]* %p2, i32* %d), !UID !53, !ScalaLabel !54
  val call8_in = Map(
    "field0" -> 0,
    "phi3" -> 1,
    "field1" -> 0,
    "field2" -> 0,
    "" -> 3
  )


  //  reattach label %pfor.inc, !UID !55, !BB_UID !56, !ScalaLabel !57
  val reattach9_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test07DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val call8_out = Decoupled(new Call(List(32,32,32,32)))
    val call8_in = Flipped(Decoupled(new Call(List(32))))
//    val CacheResp = Flipped(Valid(new CacheResp))
//    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test07DF(implicit p: Parameters) extends cilk_for_test07DFIO()(p) {



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



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_6_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_6_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_pfor_cond_cleanup = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 1))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_detach = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 3))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 3, BID = 4))

  val bb_offload_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %pfor.detach, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchFastNode(ID = 0))

  // [BasicBlock]  pfor.cond.cleanup:

  //  sync label %pfor.end.continue, !UID !10, !BB_UID !11, !ScalaLabel !12
  val sync1 = Module(new Sync(ID = 1, NumOuts = 1, NumInc = 1, NumDec = 1))  // Manual fix

  // [BasicBlock]  pfor.end.continue:

  //  ret i32 1, !UID !13, !BB_UID !14, !ScalaLabel !15
  val ret2 = Module(new RetNode(retTypes=List(32), ID=2))

  // [BasicBlock]  pfor.detach:

  //  %i.032 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !16, !ScalaLabel !17
  val phi3 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 3))


  //  detach label %offload.pfor.body, label %pfor.inc, !UID !18, !BB_UID !19, !ScalaLabel !20
  val detach4 = Module(new DetachFast(ID = 4))

  // [BasicBlock]  pfor.inc:

  //  %inc = add nuw nsw i32 %i.032, 1, !UID !21, !ScalaLabel !22
  val add5 = Module (new ComputeNode(NumOuts = 2, ID = 5, opCode = "add")(sign=false))


  //  %exitcond = icmp eq i32 %inc, 9, !UID !23, !ScalaLabel !24
  val icmp6 = Module (new IcmpNode(NumOuts = 1, ID = 6, opCode = "EQ")(sign=false))


  //  br i1 %exitcond, label %pfor.cond.cleanup, label %pfor.detach, !llvm.loop !25, !UID !50, !BB_UID !51, !ScalaLabel !52
  val br7 = Module (new CBranchNode(ID = 7))

  // [BasicBlock]  offload.pfor.body:

  //  call void @cilk_for_test07_detach([2 x i32]* %p1, i32 %i.032, [2 x i32]* %p2, i32* %d), !UID !53, !ScalaLabel !54
  val call8 = Module(new CallNode(ID=8,argTypes=List(32,32,32,32),retTypes=List(32)))


  //  reattach label %pfor.inc, !UID !55, !BB_UID !56, !ScalaLabel !57
  val reattach9 = Module(new Reattach(NumPredOps=1, ID=9))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test07_FlowParam



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

  //Connecting br0 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn(param.bb_pfor_detach_pred("br0")) <> br0.io.Out(param.br0_brn_bb("bb_pfor_detach"))


  //Connecting br7 to bb_pfor_cond_cleanup
  bb_pfor_cond_cleanup.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_pfor_cond_cleanup"))


  //Connecting br7 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn(param.bb_pfor_detach_pred("br7")) <> br7.io.Out(param.br7_brn_bb("bb_pfor_detach"))


  //Connecting detach4 to bb_offload_pfor_body
  bb_offload_pfor_body.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_offload_pfor_body"))


  //Connecting detach4 to bb_pfor_inc
  bb_pfor_inc.io.predicateIn <> detach4.io.Out(param.detach4_brn_bb("bb_pfor_inc"))

  bb_pfor_end_continue.io.predicateIn <> sync1.io.Out(0) // added manually


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))


  sync1.io.enable <> bb_pfor_cond_cleanup.io.Out(param.bb_pfor_cond_cleanup_activate("sync1"))

  loop_L_6_liveIN_0.io.enable <> bb_pfor_cond_cleanup.io.Out(1)
  loop_L_6_liveIN_1.io.enable <> bb_pfor_cond_cleanup.io.Out(2)
  loop_L_6_liveIN_2.io.enable <> bb_pfor_cond_cleanup.io.Out(3)




  ret2.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("ret2"))



  phi3.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("phi3"))

  detach4.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach4"))



  add5.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("add5"))

  icmp6.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("icmp6"))

  br7.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("br7"))



  call8.io.In.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("call8"))

  // Manual fix.  Reattach should only depend on its increment/decr inputs. If it is connected to the BB
  // Enable it will stall the loop
  // reattach9.io.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach9"))
  reattach9.io.enable.enq(ControlBundle.active())  // always enabled
  bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach9")).ready := true.B





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //[2 x i32]* %p1
  loop_L_6_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //[2 x i32]* %p2
  loop_L_6_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting function argument to the loop header
  //i32* %d
  loop_L_6_liveIN_2.io.InData <> InputSplitter.io.Out.data("field2")



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

  phi3.io.InData(param.phi3_phi_in("add5")) <> add5.io.Out(param.phi3_in("add5"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi3.io.Mask <> bb_pfor_detach.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring return instruction
  
  
  
  ret2.io.In.data("field0").bits.data := 1.U
  ret2.io.In.data("field0").bits.predicate := true.B
  ret2.io.In.data("field0").valid := true.B
  io.out <> ret2.io.Out


  // Wiring instructions
  add5.io.LeftIO <> phi3.io.Out(param.add5_in("phi3"))

  // Wiring constant
  add5.io.RightIO.bits.data := 1.U
  add5.io.RightIO.bits.predicate := true.B
  add5.io.RightIO.valid := true.B

  // Wiring instructions
  icmp6.io.LeftIO <> add5.io.Out(param.icmp6_in("add5"))

  // Wiring constant
  icmp6.io.RightIO.bits.data := 9.U
  icmp6.io.RightIO.bits.predicate := true.B
  icmp6.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br7.io.CmpIO <> icmp6.io.Out(param.br7_in("icmp6"))

  // Wiring Call to I/O
  io.call8_out <> call8.io.callOut
  call8.io.retIn <> io.call8_in
  call8.io.Out.enable.ready := true.B // Manual fix
  // Wiring Call instruction to the loop header
  call8.io.In.data("field0") <> loop_L_6_liveIN_0.io.Out(param.call8_in("field0"))

  // Wiring Call instruction to the loop header
  call8.io.In.data("field1") <> phi3.io.Out(param.call8_in("phi3"))  // Manually added

  // Wiring Call instruction to the loop header
  call8.io.In.data("field2") <> loop_L_6_liveIN_1.io.Out(param.call8_in("field1"))

  // Wiring Call instruction to the loop header
  call8.io.In.data("field3") <> loop_L_6_liveIN_2.io.Out(param.call8_in("field2"))
  call8.io.Out.enable.ready := true.B // Manual fix

  // Reattach (Manual add)
  reattach9.io.predicateIn(0) <> call8.io.Out.data("field0")

  // Sync (Manual add)
  sync1.io.incIn(0) <> detach4.io.Out(2)
  sync1.io.decIn(0) <> reattach9.io.Out(0)

}

import java.io.{File, FileWriter}
object cilk_for_test07Main extends App {
  val dir = new File("RTL/cilk_for_test07") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test07DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


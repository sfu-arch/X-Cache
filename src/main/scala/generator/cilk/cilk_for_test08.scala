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


  //  sync label %pfor.end.continue, !UID !18, !BB_UID !19, !ScalaLabel !20
  val sync1_in = Map(
    "" -> 0
  )


  //  ret i32 1, !UID !21, !BB_UID !22, !ScalaLabel !23
  val ret2_in = Map(

  )


  //  %i.049 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !24, !ScalaLabel !25
  val phi3_in = Map(
    "add5" -> 0
  )


  //  detach label %offload.pfor.body, label %pfor.inc, !UID !26, !BB_UID !27, !ScalaLabel !28
  val detach4_in = Map(
    "" -> 1,
    "" -> 2
  )


  //  %inc = add nuw nsw i32 %i.049, 1, !UID !29, !ScalaLabel !30
  val add5_in = Map(
    "phi3" -> 0
  )


  //  %exitcond = icmp eq i32 %inc, 8, !UID !31, !ScalaLabel !32
  val icmp6_in = Map(
    "add5" -> 1
  )


  //  br i1 %exitcond, label %pfor.cond.cleanup, label %pfor.detach, !llvm.loop !33, !UID !52, !BB_UID !53, !ScalaLabel !54
  val br7_in = Map(
    "icmp6" -> 0
  )


  //  call void @cilk_for_test08_detach([3 x i32]* %rgb, i32 %i.049, [3 x i32]* %xyz), !UID !55, !ScalaLabel !56
  val call8_in = Map(
    "field0" -> 0,
    "phi3" -> 1,
    "field1" -> 0,
    "" -> 3
  )


  //  reattach label %pfor.inc, !UID !57, !BB_UID !58, !ScalaLabel !59
  val reattach9_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test08DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call8_out = Decoupled(new Call(List(32,32,32)))
    val call8_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
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

  val InputSplitter = Module(new SplitCall(List(32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_pfor_cond_cleanup = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 1))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_detach = Module(new BasicBlockLoopHeadNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 3))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 3, BID = 4))

  val bb_offload_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %pfor.detach, !UID !15, !BB_UID !16, !ScalaLabel !17
  val br0 = Module (new UBranchFastNode(ID = 0))

  // [BasicBlock]  pfor.cond.cleanup:

  //  sync label %pfor.end.continue, !UID !18, !BB_UID !19, !ScalaLabel !20
  val sync1 = Module(new Sync(ID = 1, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  pfor.end.continue:

  //  ret i32 1, !UID !21, !BB_UID !22, !ScalaLabel !23
  val ret2 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=2))

  // [BasicBlock]  pfor.detach:

  //  %i.049 = phi i32 [ 0, %entry ], [ %inc, %pfor.inc ], !UID !24, !ScalaLabel !25
  val phi3 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 3))


  //  detach label %offload.pfor.body, label %pfor.inc, !UID !26, !BB_UID !27, !ScalaLabel !28
  val detach4 = Module(new Detach(ID = 4))

  // [BasicBlock]  pfor.inc:

  //  %inc = add nuw nsw i32 %i.049, 1, !UID !29, !ScalaLabel !30
  val add5 = Module (new ComputeNode(NumOuts = 2, ID = 5, opCode = "add")(sign=false))


  //  %exitcond = icmp eq i32 %inc, 8, !UID !31, !ScalaLabel !32
  val icmp6 = Module (new IcmpNode(NumOuts = 1, ID = 6, opCode = "EQ")(sign=false))


  //  br i1 %exitcond, label %pfor.cond.cleanup, label %pfor.detach, !llvm.loop !33, !UID !52, !BB_UID !53, !ScalaLabel !54
  val br7 = Module (new CBranchNode(ID = 7))

  // [BasicBlock]  offload.pfor.body:

  //  call void @cilk_for_test08_detach([3 x i32]* %rgb, i32 %i.049, [3 x i32]* %xyz), !UID !55, !ScalaLabel !56
  val call8 = Module(new CallNode(ID=8,argTypes=List(32,32,32),retTypes=List(32)))


  //  reattach label %pfor.inc, !UID !57, !BB_UID !58, !ScalaLabel !59
  val reattach9 = Module(new Reattach(NumPredIn=1, ID=9))





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

  loop_L_0_liveIN_0.io.enable <> bb_pfor_cond_cleanup.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_pfor_cond_cleanup.io.Out(2)




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
  //[3 x i32]* %rgb
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //[3 x i32]* %xyz
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")



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
  ret2.io.predicateIn(0).bits.control := true.B
  ret2.io.predicateIn(0).bits.taskID := 0.U
  ret2.io.predicateIn(0).valid := true.B
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
  icmp6.io.RightIO.bits.data := 8.U
  icmp6.io.RightIO.bits.predicate := true.B
  icmp6.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br7.io.CmpIO <> icmp6.io.Out(param.br7_in("icmp6"))

  // Wiring Call to I/O
  io.call8_out <> call8.io.callOut
  call8.io.retIn <> io.call8_in
  call8.io.Out.enable.ready := true.B
  // Wiring Call instruction to the loop header
  call8.io.In.data("field0") <>loop_L_0_liveIN_0.io.Out(param.call8_in("field0"))

  // Wiring Call instruction to the loop header
  call8.io.In.data("field1") <>phi3.io.Out(param.call8_in("phi3"))  // Manually added

  // Wiring Call instruction to the loop header
  call8.io.In.data("field2") <>loop_L_0_liveIN_1.io.Out(param.call8_in("field1"))

  // Reattach (Manual add)
  reattach9.io.predicateIn(0) <> call8.io.Out.data("field0")

  // Sync (Manual add)
  sync1.io.incIn(0) <> detach4.io.Out(2)
  sync1.io.decIn(0) <> reattach9.io.Out(0)

}
abstract class cilk_for_test08TopIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test08TopA(implicit p: Parameters) extends cilk_for_test08TopIO()(p) {

  val cilk_for_test08_detach = Module(new cilk_for_test08_detachDF())
  val cilk_for_test08 = Module(new cilk_for_test08DF())

  cilk_for_test08.io.in <> io.in
  cilk_for_test08_detach.io.CacheResp <> io.CacheResp
  cilk_for_test08_detach.io.in <> cilk_for_test08.io.call8_out
  cilk_for_test08.io.call8_in <> cilk_for_test08_detach.io.out
  io.CacheReq <> cilk_for_test08_detach.io.CacheReq
  io.out <> cilk_for_test08.io.out
}

class cilk_for_test08TopB(implicit p: Parameters) extends cilk_for_test08TopIO()(p) {

  val children = 1
  val TaskControllerModule = Module(new TaskController(List(32,32,32), List(32), 1, children))
  val cilk_for_test08 = Module(new cilk_for_test08DF())

  val cilk_for_test08_detach = for (i <- 0 until children) yield {
    val foo = Module(new cilk_for_test08_detachDF())
    foo
  }

  val CacheArbiter = Module(new CacheArbiter(children))
  for (i <- 0 until children) {
    CacheArbiter.io.cpu.CacheReq(i) <> cilk_for_test08_detach(i).io.CacheReq
    cilk_for_test08_detach(i).io.CacheResp <> CacheArbiter.io.cpu.CacheResp(i)
  }
  io.CacheReq <> CacheArbiter.io.cache.CacheReq
  CacheArbiter.io.cache.CacheResp <>  io.CacheResp
  // tester to cilk_for_test02
  cilk_for_test08.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> cilk_for_test08.io.call8_out

  // task controller to sub-task cilk_for_test08_detach
  for (i <- 0 until children ) {
    cilk_for_test08_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> cilk_for_test08_detach(i).io.out
  }

  // Task controller to cilk_for_test02
  cilk_for_test08.io.call8_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> cilk_for_test08.io.out

}


class cilk_for_test08TopC(implicit p: Parameters) extends cilk_for_test08TopIO()(p) {

  val children = 3
  val TaskControllerModule = Module(new TaskController(List(32,32,32), List(32), 1, children))
  val cilk_for_test08 = Module(new cilk_for_test08DF())

  val cilk_for_test08_detach = for (i <- 0 until children) yield {
    val foo = Module(new cilk_for_test08_detachDF())
    foo
  }

  val CacheArbiter = Module(new CacheArbiter(children))
  for (i <- 0 until children) {
    CacheArbiter.io.cpu.CacheReq(i) <> cilk_for_test08_detach(i).io.CacheReq
    cilk_for_test08_detach(i).io.CacheResp <> CacheArbiter.io.cpu.CacheResp(i)
  }
  io.CacheReq <> CacheArbiter.io.cache.CacheReq
  CacheArbiter.io.cache.CacheResp <>  io.CacheResp

  cilk_for_test08.io.in <> io.in

  TaskControllerModule.io.parentIn(0) <> cilk_for_test08.io.call8_out

  for (i <- 0 until children ) {
    cilk_for_test08_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> cilk_for_test08_detach(i).io.out
  }

  cilk_for_test08.io.call8_in <> TaskControllerModule.io.parentOut(0)

  io.out <> cilk_for_test08.io.out

}

import java.io.{File, FileWriter}
object cilk_for_test08Main extends App {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val dirA = new File("RTL/cilk_for_test08TopA") ; dirA.mkdirs
  val chirrtlA = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test08TopA()))
  val verilogFileA = new File(dirA, s"${chirrtlA.main}.v")
  val verilogWriterA = new FileWriter(verilogFileA)
  val compileResultA = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtlA, firrtl.ChirrtlForm))
  val compiledStuffA = compileResultA.getEmittedCircuit
  verilogWriterA.write(compiledStuffA.value)
  verilogWriterA.close()

  val dirB = new File("RTL/cilk_for_test08TopB") ; dirB.mkdirs
  val chirrtlB = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test08TopB()))
  val verilogFileB = new File(dirB, s"${chirrtlB.main}.v")
  val verilogWriterB = new FileWriter(verilogFileB)
  val compileResultB = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtlB, firrtl.ChirrtlForm))
  val compiledStuffB = compileResultB.getEmittedCircuit
  verilogWriterB.write(compiledStuffB.value)
  verilogWriterB.close()

  val dirC = new File("RTL/cilk_for_test08TopC") ; dirC.mkdirs
  val chirrtlC = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test08TopC()))
  val verilogFileC = new File(dirC, s"${chirrtlC.main}.v")
  val verilogWriterC = new FileWriter(verilogFileC)
  val compileResultC = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtlC, firrtl.ChirrtlForm))
  val compiledStuffC = compileResultC.getEmittedCircuit
  verilogWriterC.write(compiledStuffC.value)
  verilogWriterC.close()
}


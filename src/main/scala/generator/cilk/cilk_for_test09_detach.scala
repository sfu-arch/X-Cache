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

object Data_cilk_for_test09_detach_FlowParam{

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_for_cond_pred = Map(
    "br0" -> 0,
    "br9" -> 1
  )


  val bb_my_for_inc_pred = Map(
    "br7" -> 0
  )


  val bb_my_for_body_pred = Map(
    "br3" -> 0
  )


  val bb_my_for_end_pred = Map(
    "br3" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br10" -> 0
  )


  val br0_brn_bb = Map(
    "bb_my_for_cond" -> 0
  )


  val br3_brn_bb = Map(
    "bb_my_for_body" -> 0,
    "bb_my_for_end" -> 1
  )


  val br7_brn_bb = Map(
    "bb_my_for_inc" -> 0
  )


  val br9_brn_bb = Map(
    "bb_my_for_cond" -> 0
  )


  val br10_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val bb_my_pfor_body_activate = Map(
    "br0" -> 0
  )


  val bb_my_for_cond_activate = Map(
    "phi1" -> 0,
    "icmp2" -> 1,
    "br3" -> 2
  )


  val bb_my_for_body_activate = Map(
    "load4" -> 0,
    "add5" -> 1,
    "store6" -> 2,
    "br7" -> 3
  )


  val bb_my_for_inc_activate = Map(
    "add8" -> 0,
    "br9" -> 1
  )


  val bb_my_for_end_activate = Map(
    "br10" -> 0
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret11" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add8" -> 1
  )


  //  %0 = phi i32 [ 0, %my_pfor.body ], [ %4, %my_for.inc ], !UID !10, !ScalaLabel !11
  val phi1_in = Map(
    "add8" -> 0
  )


  //  %1 = icmp slt i32 %0, %n.in, !UID !12, !ScalaLabel !13
  val icmp2_in = Map(
    "phi1" -> 0,
    "field0" -> 0
  )


  //  br i1 %1, label %my_for.body, label %my_for.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  %2 = load i32, i32* %a.in, align 4, !UID !17, !ScalaLabel !18
  val load4_in = Map(
    "field1" -> 0
  )


  //  %3 = add nsw i32 %2, 1, !UID !19, !ScalaLabel !20
  val add5_in = Map(
    "load4" -> 0
  )


  //  store i32 %3, i32* %a.in, align 4, !UID !21, !ScalaLabel !22
  val store6_in = Map(
    "add5" -> 0,
    "field1" -> 1
  )


  //  %4 = add nsw i32 %0, 1, !UID !26, !ScalaLabel !27
  val add8_in = Map(
    "phi1" -> 1
  )


  //  ret void, !UID !45, !BB_UID !46, !ScalaLabel !47
  val ret11_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test09_detachDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test09_detachDF(implicit p: Parameters) extends cilk_for_test09_detachDFIO()(p) {



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



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 2, ID = 0))
//  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_my_for_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_my_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 2))

  val bb_my_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_my_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 4))

  val bb_my_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 5))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  br label %my_for.cond, !UID !7, !BB_UID !8, !ScalaLabel !9
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  my_for.cond:

  //  %0 = phi i32 [ 0, %my_pfor.body ], [ %4, %my_for.inc ], !UID !10, !ScalaLabel !11
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))


  //  %1 = icmp slt i32 %0, %n.in, !UID !12, !ScalaLabel !13
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %1, label %my_for.body, label %my_for.end, !UID !14, !BB_UID !15, !ScalaLabel !16
  val br3 = Module (new CBranchNode(ID = 3))

  // [BasicBlock]  my_for.body:

  //  %2 = load i32, i32* %a.in, align 4, !UID !17, !ScalaLabel !18
  val load4 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=4,RouteID=0))


  //  %3 = add nsw i32 %2, 1, !UID !19, !ScalaLabel !20
  val add5 = Module (new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign=false))


  //  store i32 %3, i32* %a.in, align 4, !UID !21, !ScalaLabel !22
  val store6 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=6,RouteID=0))


  //  br label %my_for.inc, !UID !23, !BB_UID !24, !ScalaLabel !25
  val br7 = Module (new UBranchNode(ID = 7))

  // [BasicBlock]  my_for.inc:

  //  %4 = add nsw i32 %0, 1, !UID !26, !ScalaLabel !27
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  br label %my_for.cond, !llvm.loop !28, !UID !39, !BB_UID !40, !ScalaLabel !41
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  my_for.end:

  //  br label %my_pfor.preattach, !UID !42, !BB_UID !43, !ScalaLabel !44
  val br10 = Module (new UBranchNode(ID = 10))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !45, !BB_UID !46, !ScalaLabel !47
  val ret11 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=11))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test09_detach_FlowParam



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

  //Connecting br0 to bb_my_for_cond
  bb_my_for_cond.io.activate <> br0.io.Out(param.br0_brn_bb("bb_my_for_cond"))


  //Connecting br3 to bb_my_for_body
  bb_my_for_body.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_for_body"))


  //Connecting br3 to bb_my_for_end
  bb_my_for_end.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_for_end"))


  //Connecting br7 to bb_my_for_inc
  bb_my_for_inc.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_my_for_inc"))


  //Connecting br9 to bb_my_for_cond
  bb_my_for_cond.io.loopBack <> br9.io.Out(param.br9_brn_bb("bb_my_for_cond"))


  //Connecting br10 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn <> br10.io.Out(param.br10_brn_bb("bb_my_pfor_preattach"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("br0"))



  phi1.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("phi1"))

  icmp2.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("icmp2"))

  br3.io.enable <> bb_my_for_cond.io.Out(param.bb_my_for_cond_activate("br3"))



  load4.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("load4"))

  add5.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("add5"))

  store6.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("store6"))

  br7.io.enable <> bb_my_for_body.io.Out(param.bb_my_for_body_activate("br7"))



  add8.io.enable <> bb_my_for_inc.io.Out(param.bb_my_for_inc_activate("add8"))

  br9.io.enable <> bb_my_for_inc.io.Out(param.bb_my_for_inc_activate("br9"))



  br10.io.enable <> bb_my_for_end.io.Out(param.bb_my_for_end_activate("br10"))

  loop_L_0_liveIN_0.io.enable <> bb_my_for_end.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_my_for_end.io.Out(2)




  ret11.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret11"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32 %n.in
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32* %a.in
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")



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

  phi1.io.Mask <> bb_my_for_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp2.io.LeftIO <> phi1.io.Out(param.icmp2_in("phi1"))

  // Wiring Binary instruction to the loop header
  icmp2.io.RightIO <> loop_L_0_liveIN_0.io.Out(param.icmp2_in("field0"))

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring Load instruction to the loop latch
  load4.io.GepAddr <> loop_L_0_liveIN_1.io.Out(param.load4_in("field1"))
  load4.io.memResp <> RegisterFile.io.ReadOut(0)
  RegisterFile.io.ReadIn(0) <> load4.io.memReq



  // Wiring instructions
  add5.io.LeftIO <> load4.io.Out(param.add5_in("load4"))

  // Wiring constant
  add5.io.RightIO.bits.data := 1.U
  add5.io.RightIO.bits.predicate := true.B
  add5.io.RightIO.valid := true.B

  store6.io.inData <> add5.io.Out(param.store6_in("add5"))



  // Wiring Store instruction to the function argument
  store6.io.GepAddr <> loop_L_0_liveIN_1.io.Out(1)  // manually hookedup
  store6.io.memResp  <> RegisterFile.io.WriteOut(0)
  RegisterFile.io.WriteIn(0) <> store6.io.memReq
  store6.io.Out(0).ready := true.B



  // Wiring instructions
//  loop_L_0_liveIN_2.io.InData <> phi1.io.Out(param.add8_in("phi1"))
//  loop_L_0_liveIN_2.io.enable <> bb_my_for_inc.io.Out(2)

  add8.io.LeftIO <> phi1.io.Out(param.add8_in("phi1"))
//  add8.io.LeftIO <> loop_L_0_liveIN_2.io.Out(0)

  // Wiring constant
  add8.io.RightIO.bits.data := 1.U
  add8.io.RightIO.bits.predicate := true.B
  add8.io.RightIO.valid := true.B

  /**
    * Connecting Dataflow signals
    */
  ret11.io.predicateIn(0).bits.control := true.B
  ret11.io.predicateIn(0).bits.taskID := 0.U
  ret11.io.predicateIn(0).valid := true.B
  ret11.io.In.data("field0").bits.data := 1.U
  ret11.io.In.data("field0").bits.predicate := true.B
  ret11.io.In.data("field0").valid := true.B
//  ret11.io.In.data("field0") <> store6.io.Out(0)
  io.out <> ret11.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test09_detachMain extends App {
  val dir = new File("RTL/cilk_for_test09_detach") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test09_detachDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


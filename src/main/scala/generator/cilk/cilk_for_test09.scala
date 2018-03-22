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

object Data_cilk_for_test09_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_pfor_cond_pred = Map(
    "br2" -> 0,
    "br8" -> 1
  )


  val bb_pfor_detach_pred = Map(
    "br5" -> 0
  )


  val bb_pfor_end_pred = Map(
    "br5" -> 0
  )


  val br2_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val br5_brn_bb = Map(
    "bb_pfor_detach" -> 0,
    "bb_pfor_end" -> 1
  )


  val br8_brn_bb = Map(
    "bb_pfor_cond" -> 0
  )


  val bb_pfor_inc_pred = Map(
    "detach6" -> 0
  )


  val bb_offload_pfor_body_pred = Map(
    "detach6" -> 0
  )


  val detach6_brn_bb = Map(
    "bb_offload_pfor_body" -> 0,
    "bb_pfor_inc" -> 1
  )


  val bb_entry_activate = Map(
    "alloca0" -> 0,
    "store1" -> 1,
    "br2" -> 2
  )


  val bb_pfor_cond_activate = Map(
    "phi3" -> 0,
    "icmp4" -> 1,
    "br5" -> 2
  )


  val bb_pfor_detach_activate = Map(
    "detach6" -> 0
  )


  val bb_pfor_inc_activate = Map(
    "add7" -> 0,
    "br8" -> 1
  )


  val bb_pfor_end_activate = Map(
    "sync9" -> 0
  )


  val bb_pfor_end_continue_activate = Map(
    "load10" -> 0,
    "ret11" -> 1
  )


  val bb_offload_pfor_body_activate = Map(
    "call12" -> 0,
    "reattach13" -> 1
  )


  val phi3_phi_in = Map(
    "const_0" -> 0,
    "add7" -> 1
  )


  //  %a = alloca i32, align 4, !UID !7, !ScalaLabel !8
  val alloca0_in = Map(

  )


  //  store i32 0, i32* %a, align 4, !UID !9, !ScalaLabel !10
  val store1_in = Map(
    "alloca0" -> 0
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc3, %pfor.inc ], !UID !14, !ScalaLabel !15
  val phi3_in = Map(
    "add7" -> 0
  )


  //  %cmp = icmp slt i32 %i.0, %m, !UID !16, !ScalaLabel !17
  val icmp4_in = Map(
    "phi3" -> 0,
    "field0" -> 0
  )


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5_in = Map(
    "icmp4" -> 0
  )


  //  detach label %offload.pfor.body, label %pfor.inc, !UID !21, !BB_UID !22, !ScalaLabel !23
  val detach6_in = Map(
    "" -> 0,
    "" -> 1
  )


  //  %inc3 = add nsw i32 %i.0, 1, !UID !24, !ScalaLabel !25
  val add7_in = Map(
    "phi3" -> 1
  )


  //  sync label %pfor.end.continue, !UID !38, !BB_UID !39, !ScalaLabel !40
  val sync9_in = Map(
    "" -> 2
  )


  //  %0 = load i32, i32* %a, align 4, !UID !41, !ScalaLabel !42
  val load10_in = Map(
    "alloca0" -> 1
  )


  //  ret i32 %0, !UID !43, !BB_UID !44, !ScalaLabel !45
  val ret11_in = Map(
    "load10" -> 0
  )


  //  call void @cilk_for_test09_detach(i32 %n, i32* %a), !UID !46, !ScalaLabel !47
  val call12_in = Map(
    "field1" -> 0,
    "alloca0" -> 2,
    "" -> 3
  )


  //  reattach label %pfor.inc, !UID !48, !BB_UID !49, !ScalaLabel !50
  val reattach13_in = Map(
    "" -> 4
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test09DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call12_out = Decoupled(new Call(List(32,32)))
    val call12_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test09DF(implicit p: Parameters) extends cilk_for_test09DFIO()(p) {



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
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_2 = Module(new LiveInNode(NumOuts = 1, ID = 0))




  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_pfor_cond = Module(new LoopHead(NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_pfor_detach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_inc = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 2, BID = 3))

  val bb_pfor_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 4))

  val bb_pfor_end_continue = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5))

  val bb_offload_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 6))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %a = alloca i32, align 4, !UID !7, !ScalaLabel !8
  val alloca0 = Module(new AllocaNode(NumOuts=3, RouteID=0, ID=0))


  //  store i32 0, i32* %a, align 4, !UID !9, !ScalaLabel !10
  val store1 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))


  //  br label %pfor.cond, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br2 = Module (new UBranchNode(ID = 2))

  // [BasicBlock]  pfor.cond:

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc3, %pfor.inc ], !UID !14, !ScalaLabel !15
  val phi3 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 3))


  //  %cmp = icmp slt i32 %i.0, %m, !UID !16, !ScalaLabel !17
  val icmp4 = Module (new IcmpNode(NumOuts = 1, ID = 4, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %pfor.detach, label %pfor.end, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5 = Module (new CBranchNode(ID = 5))

  // [BasicBlock]  pfor.detach:

  //  detach label %offload.pfor.body, label %pfor.inc, !UID !21, !BB_UID !22, !ScalaLabel !23
  val detach6 = Module(new Detach(ID = 6))

  // [BasicBlock]  pfor.inc:

  //  %inc3 = add nsw i32 %i.0, 1, !UID !24, !ScalaLabel !25
  val add7 = Module (new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign=false))


  //  br label %pfor.cond, !llvm.loop !26, !UID !35, !BB_UID !36, !ScalaLabel !37
  val br8 = Module (new UBranchNode(ID = 8))

  // [BasicBlock]  pfor.end:

  //  sync label %pfor.end.continue, !UID !38, !BB_UID !39, !ScalaLabel !40
  val sync9 = Module(new Sync(ID = 9, NumOuts = 1, NumInc = 1, NumDec = 1))

  // [BasicBlock]  pfor.end.continue:

  //  %0 = load i32, i32* %a, align 4, !UID !41, !ScalaLabel !42
  val load10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=10,RouteID=0))


  //  ret i32 %0, !UID !43, !BB_UID !44, !ScalaLabel !45
  val ret11 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=11))

  // [BasicBlock]  offload.pfor.body:

  //  call void @cilk_for_test09_detach(i32 %n, i32* %a), !UID !46, !ScalaLabel !47
  val call12 = Module(new CallNode(ID=12,argTypes=List(32,32),retTypes=List(32)))


  //  reattach label %pfor.inc, !UID !48, !BB_UID !49, !ScalaLabel !50
  val reattach13 = Module(new Reattach(NumPredIn=1, ID=13))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test09_FlowParam



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

  //Connecting br2 to bb_pfor_cond
  bb_pfor_cond.io.activate <> br2.io.Out(param.br2_brn_bb("bb_pfor_cond"))


  //Connecting br5 to bb_pfor_detach
  bb_pfor_detach.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_pfor_detach"))


  //Connecting br5 to bb_pfor_end
  bb_pfor_end.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_pfor_end"))


  //Connecting br8 to bb_pfor_cond
  bb_pfor_cond.io.loopBack <> br8.io.Out(param.br8_brn_bb("bb_pfor_cond"))


  //Connecting detach6 to bb_offload_pfor_body
  bb_offload_pfor_body.io.predicateIn <> detach6.io.Out(param.detach6_brn_bb("bb_offload_pfor_body"))


  //Connecting detach6 to bb_pfor_inc
  bb_pfor_inc.io.predicateIn <> detach6.io.Out(param.detach6_brn_bb("bb_pfor_inc"))




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  alloca0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("alloca0"))

  store1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store1"))

  br2.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br2"))



  phi3.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("phi3"))

  icmp4.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("icmp4"))

  br5.io.enable <> bb_pfor_cond.io.Out(param.bb_pfor_cond_activate("br5"))



  detach6.io.enable <> bb_pfor_detach.io.Out(param.bb_pfor_detach_activate("detach6"))



  add7.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("add7"))

  br8.io.enable <> bb_pfor_inc.io.Out(param.bb_pfor_inc_activate("br8"))



  sync9.io.enable <> bb_pfor_end.io.Out(param.bb_pfor_end_activate("sync9"))

  loop_L_0_liveIN_0.io.enable <> bb_pfor_end.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_pfor_end.io.Out(2)
  loop_L_0_liveIN_2.io.enable <> bb_pfor_end.io.Out(3)


  bb_pfor_end_continue.io.predicateIn <> sync9.io.Out(0) // added manually


  load10.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("load10"))

  ret11.io.enable <> bb_pfor_end_continue.io.Out(param.bb_pfor_end_continue_activate("ret11"))



  call12.io.In.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("call12"))

  reattach13.io.enable <> bb_offload_pfor_body.io.Out(param.bb_offload_pfor_body_activate("reattach13"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32 %m
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32 %n
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")

  // Connecting instruction to the loop header
  //  %a = alloca i32, align 4, !UID !7, !ScalaLabel !8
  loop_L_0_liveIN_2.io.InData <> alloca0.io.Out(param.call12_in("alloca0"))



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

  phi3.io.InData(param.phi3_phi_in("add7")) <> add7.io.Out(param.phi3_in("add7"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi3.io.Mask <> bb_pfor_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Alloca instructions with Static inputs
  alloca0.io.allocaInputIO.bits.size      := 1.U
  alloca0.io.allocaInputIO.bits.numByte   := 4.U
  alloca0.io.allocaInputIO.bits.predicate := true.B
  alloca0.io.allocaInputIO.bits.valid     := true.B
  alloca0.io.allocaInputIO.valid          := true.B

  // Connecting Alloca to Stack
  StackPointer.io.InData(0) <> alloca0.io.allocaReqIO
  alloca0.io.allocaRespIO <> StackPointer.io.OutData(0)


  // Wiring constant instructions to store
  store1.io.inData.bits.data := 0.U
  store1.io.inData.bits.predicate := true.B
  store1.io.inData.valid := true.B



  // Wiring Store instruction to the parent instruction
  store1.io.GepAddr <> alloca0.io.Out(param.store1_in("alloca0"))
  store1.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store1.io.memReq
  store1.io.Out(0).ready := true.B



  // Wiring instructions
  icmp4.io.LeftIO <> phi3.io.Out(param.icmp4_in("phi3"))

  // Wiring Binary instruction to the loop header
  icmp4.io.RightIO <> loop_L_0_liveIN_0.io.Out(param.icmp4_in("field0"))

  // Wiring Branch instruction
  br5.io.CmpIO <> icmp4.io.Out(param.br5_in("icmp4"))

  // Wiring instructions
  add7.io.LeftIO <> phi3.io.Out(param.add7_in("phi3"))

  // Wiring constant
  add7.io.RightIO.bits.data := 1.U
  add7.io.RightIO.bits.predicate := true.B
  add7.io.RightIO.valid := true.B

  // Wiring Load instruction to another instruction
  load10.io.GepAddr <> alloca0.io.Out(param.load10_in("alloca0"))
  load10.io.memResp <> RegisterFile.io.ReadOut(0)  // Manually added
  RegisterFile.io.ReadIn(0) <> load10.io.memReq    // Manually added


  // Reattach (Manual add)
  reattach13.io.predicateIn(0) <> call12.io.Out.data("field0")

  // Sync (Manual add)
  sync9.io.incIn(0) <> detach6.io.Out(2)
  sync9.io.decIn(0) <> reattach13 .io.Out(0)


  // Wiring return instruction
  ret11.io.predicateIn(0).bits.control := true.B
  ret11.io.predicateIn(0).bits.taskID := 0.U
  ret11.io.predicateIn(0).valid := true.B
  ret11.io.In.data("field0") <> load10.io.Out(param.ret11_in("load10"))
  io.out <> ret11.io.Out


  // Wiring Call to I/O
  io.call12_out <> call12.io.callOut
  call12.io.retIn <> io.call12_in
  call12.io.Out.enable.ready := true.B // Manual fix
  // Wiring Call instruction to the loop header
  call12.io.In.data("field0") <> loop_L_0_liveIN_1.io.Out(0)

  // Wiring Call instruction to the loop header
  call12.io.In.data("field1") <> loop_L_0_liveIN_2.io.Out(0)



}

import java.io.{File, FileWriter}
object cilk_for_test09Main extends App {
  val dir = new File("RTL/cilk_for_test09") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test09DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


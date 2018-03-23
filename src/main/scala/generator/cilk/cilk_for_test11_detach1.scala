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

object Data_cilk_for_test11_detach1_FlowParam{

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_if_then_pred = Map(
    "br5" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br5" -> 0,
    "br8" -> 1
  )


  val br5_brn_bb = Map(
    "bb_my_if_then" -> 0,
    "bb_my_pfor_preattach" -> 1
  )


  val br8_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val bb_my_pfor_body_activate = Map(
    "getelementptr0" -> 0,
    "load1" -> 1,
    "getelementptr2" -> 2,
    "load3" -> 3,
    "icmp4" -> 4,
    "br5" -> 5
  )


  val bb_my_if_then_activate = Map(
    "store6" -> 0,
    "store7" -> 1,
    "br8" -> 2
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret9" -> 0
  )


  //  %0 = getelementptr inbounds %struct.Edge, %struct.Edge* %E.in, i32 %i.018.in, i32 0, !UID !2, !ScalaLabel !3
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %1 = load i32, i32* %0, align 4, !tbaa !4, !UID !9, !ScalaLabel !10
  val load1_in = Map(
    "getelementptr0" -> 0
  )


  //  %2 = getelementptr inbounds %struct.Edge, %struct.Edge* %E.in, i32 %i.018.in, i32 1, !UID !11, !ScalaLabel !12
  val getelementptr2_in = Map(
    "field0" -> 1,
    "field1" -> 1
  )


  //  %3 = load i32, i32* %2, align 4, !tbaa !13, !UID !14, !ScalaLabel !15
  val load3_in = Map(
    "getelementptr2" -> 0
  )


  //  %4 = icmp sgt i32 %1, %3, !UID !16, !ScalaLabel !17
  val icmp4_in = Map(
    "load1" -> 0,
    "load3" -> 0
  )


  //  br i1 %4, label %my_if.then, label %my_pfor.preattach, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5_in = Map(
    "icmp4" -> 0
  )


  //  store i32 %3, i32* %0, align 4, !tbaa !21, !UID !22, !ScalaLabel !23
  val store6_in = Map(
    "load3" -> 1,
    "getelementptr0" -> 1
  )


  //  store i32 %1, i32* %2, align 4, !tbaa !21, !UID !24, !ScalaLabel !25
  val store7_in = Map(
    "load1" -> 1,
    "getelementptr2" -> 1
  )


  //  ret void, !UID !29, !BB_UID !30, !ScalaLabel !31
  val ret9_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test11_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class cilk_for_test11_detach1DF(implicit p: Parameters) extends cilk_for_test11_detach1DFIO()(p) {



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


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 0))

  val bb_my_if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 1))

  val bb_my_pfor_preattach = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 1, NumPhi=2,BID = 2))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = getelementptr inbounds %struct.Edge, %struct.Edge* %E.in, i32 %i.018.in, i32 0, !UID !2, !ScalaLabel !3
  val getelementptr0 = Module (new GepTwoNode(NumOuts = 2, ID = 0)(numByte1 = 8, numByte2 = 4))


  //  %1 = load i32, i32* %0, align 4, !tbaa !4, !UID !9, !ScalaLabel !10
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2,ID=1,RouteID=0))


  //  %2 = getelementptr inbounds %struct.Edge, %struct.Edge* %E.in, i32 %i.018.in, i32 1, !UID !11, !ScalaLabel !12
  val getelementptr2 = Module (new GepTwoNode(NumOuts = 2, ID = 2)(numByte1 = 8, numByte2 = 4))


  //  %3 = load i32, i32* %2, align 4, !tbaa !13, !UID !14, !ScalaLabel !15
  val load3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2,ID=3,RouteID=1))


  //  %4 = icmp sgt i32 %1, %3, !UID !16, !ScalaLabel !17
  val icmp4 = Module (new IcmpNode(NumOuts = 1, ID = 4, opCode = "UGT")(sign=false))


  //  br i1 %4, label %my_if.then, label %my_pfor.preattach, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5 = Module (new CBranchNode(ID = 5))

  // [BasicBlock]  my_if.then:

  //  store i32 %3, i32* %0, align 4, !tbaa !21, !UID !22, !ScalaLabel !23
  val store6 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=6,RouteID=0))


  //  store i32 %1, i32* %2, align 4, !tbaa !21, !UID !24, !ScalaLabel !25
  val store7 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=1))


  //  br label %my_pfor.preattach, !UID !26, !BB_UID !27, !ScalaLabel !28
  val br8 = Module (new UBranchNode(ID = 8))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !29, !BB_UID !30, !ScalaLabel !31
  val ret9 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=9))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test11_detach1_FlowParam



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

  //Connecting br5 to bb_my_if_then
  bb_my_if_then.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_my_if_then"))


  //Connecting br5 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn(0) <> br5.io.Out(param.br5_brn_bb("bb_my_pfor_preattach"))


  //Connecting br8 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn(1) <> br8.io.Out(param.br8_brn_bb("bb_my_pfor_preattach"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("getelementptr0"))

  load1.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("load1"))

  getelementptr2.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("getelementptr2"))

  load3.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("load3"))

  icmp4.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("icmp4"))

  br5.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("br5"))



  store6.io.enable <> bb_my_if_then.io.Out(param.bb_my_if_then_activate("store6"))

  store7.io.enable <> bb_my_if_then.io.Out(param.bb_my_if_then_activate("store7"))

  br8.io.enable <> bb_my_if_then.io.Out(param.bb_my_if_then_activate("br8"))



  ret9.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret9"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  //Function doesn't have any for loop


  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node
  // There is no PHI node


  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring GEP instruction to the function argument
  getelementptr0.io.baseAddress <> InputSplitter.io.Out.data("field0")


  // Wiring GEP instruction to the function argument
  getelementptr0.io.idx1 <> InputSplitter.io.Out.data("field1")


  // Wiring GEP instruction to the Constant
  getelementptr0.io.idx2.valid :=  true.B
  getelementptr0.io.idx2.bits.predicate :=  true.B
  getelementptr0.io.idx2.bits.data :=  0.U


  // Wiring Load instruction to the parent instruction
  load1.io.GepAddr <> getelementptr0.io.Out(param.load1_in("getelementptr0"))
  load1.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load1.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr2.io.baseAddress <> InputSplitter.io.Out.data("field0")


  // Wiring GEP instruction to the function argument
  getelementptr2.io.idx1 <> InputSplitter.io.Out.data("field1")


  // Wiring GEP instruction to the Constant
  getelementptr2.io.idx2.valid :=  true.B
  getelementptr2.io.idx2.bits.predicate :=  true.B
  getelementptr2.io.idx2.bits.data :=  1.U


  // Wiring Load instruction to the parent instruction
  load3.io.GepAddr <> getelementptr2.io.Out(param.load3_in("getelementptr2"))
  load3.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load3.io.memReq




  // Wiring instructions
  icmp4.io.LeftIO <> load1.io.Out(param.icmp4_in("load1"))

  // Wiring instructions
  icmp4.io.RightIO <> load3.io.Out(param.icmp4_in("load3"))

  // Wiring Branch instruction
  br5.io.CmpIO <> icmp4.io.Out(param.br5_in("icmp4"))

  store6.io.inData <> load3.io.Out(param.store6_in("load3"))



  // Wiring Store instruction to the parent instruction
  store6.io.GepAddr <> getelementptr0.io.Out(param.store6_in("getelementptr0"))
  store6.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store6.io.memReq
  store6.io.Out(0).ready := true.B



  store7.io.inData <> load1.io.Out(param.store7_in("load1"))



  // Wiring Store instruction to the parent instruction
  store7.io.GepAddr <> getelementptr2.io.Out(param.store7_in("getelementptr2"))
  store7.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store7.io.memReq
  store7.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  ret9.io.predicateIn(0).bits.control := true.B
  ret9.io.predicateIn(0).bits.taskID := 0.U
  ret9.io.predicateIn(0).valid := true.B
  ret9.io.In.data("field0").bits.data := 1.U
  ret9.io.In.data("field0").bits.predicate := true.B
  ret9.io.In.data("field0").valid := true.B
  io.out <> ret9.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test11_detach1Main extends App {
  val dir = new File("RTL/cilk_for_test11_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test11_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


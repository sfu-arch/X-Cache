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

object Data_cilk_for_test12_detach3_FlowParam{

  val bb_my_pfor_body10_pred = Map(
    "active" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br5" -> 0
  )


  val br5_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val bb_my_pfor_body10_activate = Map(
    "getelementptr0" -> 0,
    "load1" -> 1,
    "mul2" -> 2,
    "getelementptr3" -> 3,
    "store4" -> 4,
    "br5" -> 5
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret6" -> 0
  )


  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %k.0.in, !UID !7, !ScalaLabel !8
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %1 = load i32, i32* %0, align 4, !UID !9, !ScalaLabel !10
  val load1_in = Map(
    "getelementptr0" -> 0
  )


  //  %2 = mul i32 2, %1, !UID !11, !ScalaLabel !12
  val mul2_in = Map(
    "load1" -> 0
  )


  //  %3 = getelementptr inbounds i32, i32* %a.in, i32 %k.0.in, !UID !13, !ScalaLabel !14
  val getelementptr3_in = Map(
    "field0" -> 1,
    "field1" -> 1
  )


  //  store i32 %2, i32* %3, align 4, !UID !15, !ScalaLabel !16
  val store4_in = Map(
    "mul2" -> 0,
    "getelementptr3" -> 0
  )


  //  ret void, !UID !20, !BB_UID !21, !ScalaLabel !22
  val ret6_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test12_detach3DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test12_detach3DF(implicit p: Parameters) extends cilk_for_test12_detach3DFIO()(p) {



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

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,2)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body10 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 0))

  val bb_my_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body10:

  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %k.0.in, !UID !7, !ScalaLabel !8
  val getelementptr0 = Module (new GepOneNode(NumOuts = 1, ID = 0)(numByte1 = 4))


  //  %1 = load i32, i32* %0, align 4, !UID !9, !ScalaLabel !10
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))


  //  %2 = mul i32 2, %1, !UID !11, !ScalaLabel !12
  val mul2 = Module (new ComputeNode(NumOuts = 1, ID = 2, opCode = "mul")(sign=false))


  //  %3 = getelementptr inbounds i32, i32* %a.in, i32 %k.0.in, !UID !13, !ScalaLabel !14
  val getelementptr3 = Module (new GepOneNode(NumOuts = 1, ID = 3)(numByte1 = 4))


  //  store i32 %2, i32* %3, align 4, !UID !15, !ScalaLabel !16
  val store4 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=4,RouteID=0))


  //  br label %my_pfor.preattach, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br5 = Module (new UBranchNode(ID = 5))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !20, !BB_UID !21, !ScalaLabel !22
  val ret6 = Module(new RetNode(retTypes=List(32), ID=6))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test12_detach3_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_my_pfor_body10.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br5 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_my_pfor_preattach"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_my_pfor_body10.io.Out(param.bb_my_pfor_body10_activate("getelementptr0"))

  load1.io.enable <> bb_my_pfor_body10.io.Out(param.bb_my_pfor_body10_activate("load1"))

  mul2.io.enable <> bb_my_pfor_body10.io.Out(param.bb_my_pfor_body10_activate("mul2"))

  getelementptr3.io.enable <> bb_my_pfor_body10.io.Out(param.bb_my_pfor_body10_activate("getelementptr3"))

  store4.io.enable <> bb_my_pfor_body10.io.Out(param.bb_my_pfor_body10_activate("store4"))

  br5.io.enable <> bb_my_pfor_body10.io.Out(param.bb_my_pfor_body10_activate("br5"))



  ret6.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret6"))





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
  getelementptr0.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  // Wiring GEP instruction to the function argument
  getelementptr0.io.idx1 <> InputSplitter.io.Out.data("field1")(0)

  // Wiring Load instruction to the parent instruction
  load1.io.GepAddr <> getelementptr0.io.Out(param.load1_in("getelementptr0"))
  load1.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load1.io.memReq




  // Wiring constant
  mul2.io.LeftIO.bits.data := 2.U
  mul2.io.LeftIO.bits.predicate := true.B
  mul2.io.LeftIO.valid := true.B

  // Wiring instructions
  mul2.io.RightIO <> load1.io.Out(param.mul2_in("load1"))

  // Wiring GEP instruction to the function argument
  getelementptr3.io.baseAddress <> InputSplitter.io.Out.data("field0")(1)

  // Wiring GEP instruction to the function argument
  getelementptr3.io.idx1 <> InputSplitter.io.Out.data("field1")(1)

  store4.io.inData <> mul2.io.Out(param.store4_in("mul2"))



  // Wiring Store instruction to the parent instruction
  store4.io.GepAddr <> getelementptr3.io.Out(param.store4_in("getelementptr3"))
  store4.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store4.io.memReq
//  store4.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  
  
  
/*
  ret6.io.In.data("field0").bits.data := 1.U
  
  ret6.io.In.data("field0").valid := true.B
  */
  ret6.io.In.data("field0") <> store4.io.Out(0)

  io.out <> ret6.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test12_detach3Main extends App {
  val dir = new File("RTL/cilk_for_test12_detach3") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test12_detach3DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


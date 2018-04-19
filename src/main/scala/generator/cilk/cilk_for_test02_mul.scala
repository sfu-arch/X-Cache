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

object Data_cilk_for_test02_mul_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_entry_activate = Map(
    "getelementptr0" -> 0,
    "load1" -> 1,
    "mul2" -> 2,
    "getelementptr3" -> 3,
    "store4" -> 4,
    "ret5" -> 5
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i, !UID !7, !ScalaLabel !8
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field2" -> 0
  )


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !9, !ScalaLabel !10
  val load1_in = Map(
    "getelementptr0" -> 0
  )


  //  %mul = mul i32 %0, 2, !UID !11, !ScalaLabel !12
  val mul2_in = Map(
    "load1" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i, !UID !13, !ScalaLabel !14
  val getelementptr3_in = Map(
    "field1" -> 0,
    "field2" -> 1
  )


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !15, !ScalaLabel !16
  val store4_in = Map(
    "mul2" -> 0,
    "getelementptr3" -> 0
  )


  //  ret void, !UID !17, !BB_UID !18, !ScalaLabel !19
  val ret5_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test02_mulDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test02_mulDF(implicit p: Parameters) extends cilk_for_test02_mulDFIO() {



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

  val InputSplitter = Module(new SplitCall(List(32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 0))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i, !UID !7, !ScalaLabel !8
  val getelementptr0 = Module (new GepOneNode(NumOuts = 1, ID = 0)(numByte1 = 1))


  //  %0 = load i32, i32* %arrayidx, align 4, !UID !9, !ScalaLabel !10
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))


  //  %mul = mul i32 %0, 2, !UID !11, !ScalaLabel !12
  val mul2 = Module (new ComputeNode(NumOuts = 1, ID = 2, opCode = "mul")(sign=false))


  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i, !UID !13, !ScalaLabel !14
  val getelementptr3 = Module (new GepOneNode(NumOuts = 1, ID = 3)(numByte1 = 1))


  //  store i32 %mul, i32* %arrayidx1, align 4, !UID !15, !ScalaLabel !16
  val store4 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=4,RouteID=0))


  //  ret void, !UID !17, !BB_UID !18, !ScalaLabel !19
  val ret5 = Module(new RetNode(retTypes=List(32), ID=5))







  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test02_mul_FlowParam



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


  // There is no branch instruction



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr0"))

  load1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load1"))

  mul2.io.enable <> bb_entry.io.Out(param.bb_entry_activate("mul2"))

  getelementptr3.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr3"))

  store4.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store4"))

  ret5.io.enable <> bb_entry.io.Out(param.bb_entry_activate("ret5"))





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
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  //Function doesn't have any for loop


  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring GEP instruction to the function argument
  getelementptr0.io.baseAddress <> InputSplitter.io.Out.data("field0")

  val field2_expand = Module(new ExpandNode(NumOuts=2, ID=99)(new DataBundle)) // Manual
  field2_expand.io.enable.valid := true.B
  field2_expand.io.enable.bits.control := true.B
  field2_expand.io.InData <> InputSplitter.io.Out.data("field2")

  // Wiring GEP instruction to the function argument
  getelementptr0.io.idx1 <> field2_expand.io.Out(0) // Manual


  // Wiring Load instruction to the parent instruction
  load1.io.GepAddr <> getelementptr0.io.Out(param.load1_in("getelementptr0"))
  load1.io.memResp <> RegisterFile.io.ReadOut(0)
  RegisterFile.io.ReadIn(0) <> load1.io.memReq




  // Wiring instructions
  mul2.io.LeftIO <> load1.io.Out(param.mul2_in("load1"))

  // Wiring constant
  mul2.io.RightIO.bits.data := 2.U
  mul2.io.RightIO.bits.predicate := true.B
  mul2.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr3.io.baseAddress <> InputSplitter.io.Out.data("field1")


  // Wiring GEP instruction to the function argument
  getelementptr3.io.idx1 <> field2_expand.io.Out(1) // Manual


  store4.io.inData <> mul2.io.Out(param.store4_in("mul2"))



  // Wiring Store instruction to the parent instruction
  store4.io.GepAddr <> getelementptr3.io.Out(param.store4_in("getelementptr3"))
  store4.io.memResp  <> RegisterFile.io.WriteOut(0)
  RegisterFile.io.WriteIn(0) <> store4.io.memReq
//  store4.io.Out(0).ready := true.B  // Manually removed



  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret5.io.In.data("field0")<> store4.io.Out(0)
  io.out <> ret5.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test02_mulMain extends App {
  val dir = new File("RTL/cilk_for_test02_mul") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test02_mulDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


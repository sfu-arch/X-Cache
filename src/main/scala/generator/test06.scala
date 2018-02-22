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

object Data_test06_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_entry_activate = Map(
    "alloca0" -> 0,
    "getelementptr1" -> 1,
    "store2" -> 2,
    "getelementptr3" -> 3,
    "store4" -> 4,
    "getelementptr5" -> 5,
    "load6" -> 6,
    "getelementptr7" -> 7,
    "load8" -> 8,
    "add9" -> 9,
    "ret10" -> 10
  )


  //  %sum = alloca [2 x i32], align 4, !UID !7, !ScalaLabel !8
  val alloca0_in = Map(

  )


  //  %arrayidx = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 0, !UID !9, !ScalaLabel !10
  val getelementptr1_in = Map(
    "alloca0" -> 0
  )


  //  store i32 %a, i32* %arrayidx, align 4, !UID !11, !ScalaLabel !12
  val store2_in = Map(
    "field0" -> 0,
    "getelementptr1" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 1, !UID !13, !ScalaLabel !14
  val getelementptr3_in = Map(
    "alloca0" -> 1
  )


  //  store i32 %b, i32* %arrayidx1, align 4, !UID !15, !ScalaLabel !16
  val store4_in = Map(
    "field1" -> 0,
    "getelementptr3" -> 0
  )


  //  %arrayidx2 = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 0, !UID !17, !ScalaLabel !18
  val getelementptr5_in = Map(
    "alloca0" -> 2
  )


  //  %0 = load i32, i32* %arrayidx2, align 4, !UID !19, !ScalaLabel !20
  val load6_in = Map(
    "getelementptr5" -> 0
  )


  //  %arrayidx3 = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 1, !UID !21, !ScalaLabel !22
  val getelementptr7_in = Map(
    "alloca0" -> 3
  )


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !23, !ScalaLabel !24
  val load8_in = Map(
    "getelementptr7" -> 0
  )


  //  %add = add nsw i32 %0, %1, !UID !25, !ScalaLabel !26
  val add9_in = Map(
    "load6" -> 0,
    "load8" -> 0
  )


  //  ret i32 %add, !UID !27, !BB_UID !28, !ScalaLabel !29
  val ret10_in = Map(
    "add9" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test06DFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class test06DF(implicit p: Parameters) extends test06DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=4,NReads=2,NWrites=2)
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

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 0)(p))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %sum = alloca [2 x i32], align 4, !UID !7, !ScalaLabel !8
  val alloca0 = Module(new AllocaNode(NumOuts=4, RouteID=0, ID=0))


  //  %arrayidx = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 0, !UID !9, !ScalaLabel !10
  val getelementptr1 = Module (new GepTwoNode(NumOuts = 1, ID = 1)(numByte1 = 8, numByte2 = 0)(p))


  //  store i32 %a, i32* %arrayidx, align 4, !UID !11, !ScalaLabel !12
  val store2 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=2,RouteID=0))


  //  %arrayidx1 = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 1, !UID !13, !ScalaLabel !14
  val getelementptr3 = Module (new GepTwoNode(NumOuts = 1, ID = 3)(numByte1 = 8, numByte2 = 4)(p))


  //  store i32 %b, i32* %arrayidx1, align 4, !UID !15, !ScalaLabel !16
  val store4 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=4,RouteID=1))


  //  %arrayidx2 = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 0, !UID !17, !ScalaLabel !18
  val getelementptr5 = Module (new GepTwoNode(NumOuts = 1, ID = 5)(numByte1 = 8, numByte2 = 0)(p))


  //  %0 = load i32, i32* %arrayidx2, align 4, !UID !19, !ScalaLabel !20
  val load6 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=6,RouteID=0))


  //  %arrayidx3 = getelementptr inbounds [2 x i32], [2 x i32]* %sum, i32 0, i32 1, !UID !21, !ScalaLabel !22
  val getelementptr7 = Module (new GepTwoNode(NumOuts = 1, ID = 7)(numByte1 = 8, numByte2 = 4)(p))


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !23, !ScalaLabel !24
  val load8 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=8,RouteID=1))


  //  %add = add nsw i32 %0, %1, !UID !25, !ScalaLabel !26
  val add9 = Module (new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign=false)(p))


  //  ret i32 %add, !UID !27, !BB_UID !28, !ScalaLabel !29
  val ret10 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=10))







  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test06_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_entry.io.predicateIn(0) <> InputSplitter.io.Out.enable

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

  alloca0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("alloca0"))

  getelementptr1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr1"))

  store2.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store2"))

  getelementptr3.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr3"))

  store4.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store4"))

  getelementptr5.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr5"))

  load6.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load6"))

  getelementptr7.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr7"))

  load8.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load8"))

  add9.io.enable <> bb_entry.io.Out(param.bb_entry_activate("add9"))

  ret10.io.enable <> bb_entry.io.Out(param.bb_entry_activate("ret10"))





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

  // Wiring Alloca instructions with Static inputs
  alloca0.io.allocaInputIO.bits.size      := 1.U
  alloca0.io.allocaInputIO.bits.numByte   := 8.U
  alloca0.io.allocaInputIO.bits.predicate := true.B
  alloca0.io.allocaInputIO.bits.valid     := true.B
  alloca0.io.allocaInputIO.valid          := true.B

  // Connecting Alloca to Stack
  StackPointer.io.InData(0) <> alloca0.io.allocaReqIO
  alloca0.io.allocaRespIO <> StackPointer.io.OutData(0)


  // Wiring GEP instruction to the parent instruction
  getelementptr1.io.baseAddress <> alloca0.io.Out(param.getelementptr1_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr1.io.idx1.valid :=  true.B
  getelementptr1.io.idx1.bits.predicate :=  true.B
  getelementptr1.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr1.io.idx2.valid :=  true.B
  getelementptr1.io.idx2.bits.predicate :=  true.B
  getelementptr1.io.idx2.bits.data :=  0.U


  // Wiring Store instruction to the function argument
  store2.io.inData <> InputSplitter.io.Out.data("field0")



  // Wiring Store instruction to the parent instruction
  store2.io.GepAddr <> getelementptr1.io.Out(param.store2_in("getelementptr1"))
  store2.io.memResp  <> RegisterFile.io.WriteOut(0)
  RegisterFile.io.WriteIn(0) <> store2.io.memReq
  store2.io.Out(0).ready := true.B



  // Wiring GEP instruction to the parent instruction
  getelementptr3.io.baseAddress <> alloca0.io.Out(param.getelementptr3_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr3.io.idx1.valid :=  true.B
  getelementptr3.io.idx1.bits.predicate :=  true.B
  getelementptr3.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr3.io.idx2.valid :=  true.B
  getelementptr3.io.idx2.bits.predicate :=  true.B
  getelementptr3.io.idx2.bits.data :=  1.U


  // Wiring Store instruction to the function argument
  store4.io.inData <> InputSplitter.io.Out.data("field1")


  // Wiring Store instruction to the parent instruction
  store4.io.GepAddr <> getelementptr3.io.Out(param.store4_in("getelementptr3"))
  store4.io.memResp  <> RegisterFile.io.WriteOut(1)

  RegisterFile.io.WriteIn(1) <> store4.io.memReq
  store4.io.Out(0).ready := true.B



  // Wiring GEP instruction to the parent instruction
  getelementptr5.io.baseAddress <> alloca0.io.Out(param.getelementptr5_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr5.io.idx1.valid :=  true.B
  getelementptr5.io.idx1.bits.predicate :=  true.B
  getelementptr5.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr5.io.idx2.valid :=  true.B
  getelementptr5.io.idx2.bits.predicate :=  true.B
  getelementptr5.io.idx2.bits.data :=  0.U


  // Wiring Load instruction to the parent instruction
  load6.io.GepAddr <> getelementptr5.io.Out(param.load6_in("getelementptr5"))
  load6.io.memResp <> RegisterFile.io.ReadOut(0)
  load6.io.PredOp(0) <> store2.io.SuccOp(0)
  RegisterFile.io.ReadIn(0) <> load6.io.memReq




  // Wiring GEP instruction to the parent instruction
  getelementptr7.io.baseAddress <> alloca0.io.Out(param.getelementptr7_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr7.io.idx1.valid :=  true.B
  getelementptr7.io.idx1.bits.predicate :=  true.B
  getelementptr7.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr7.io.idx2.valid :=  true.B
  getelementptr7.io.idx2.bits.predicate :=  true.B
  getelementptr7.io.idx2.bits.data :=  1.U


  // Wiring Load instruction to the parent instruction
  load8.io.GepAddr <> getelementptr7.io.Out(param.load8_in("getelementptr7"))
  load8.io.memResp <> RegisterFile.io.ReadOut(1)
  load8.io.PredOp(0) <> store4.io.SuccOp(0)
  RegisterFile.io.ReadIn(1) <> load8.io.memReq




  // Wiring instructions
  add9.io.LeftIO <> load6.io.Out(param.add9_in("load6"))

  // Wiring instructions
  add9.io.RightIO <> load8.io.Out(param.add9_in("load8"))

  // Wiring return instruction
  ret10.io.predicateIn(0).bits.control := true.B
  ret10.io.predicateIn(0).bits.taskID := 0.U
  ret10.io.predicateIn(0).valid := true.B
  ret10.io.In.data("field0") <> add9.io.Out(param.ret10_in("add9"))
  io.out <> ret10.io.Out


}

import java.io.{File, FileWriter}
object test06Main extends App {
  val dir = new File("RTL/test06") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test06DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


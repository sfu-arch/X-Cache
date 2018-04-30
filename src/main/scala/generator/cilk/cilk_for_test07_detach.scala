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

object Data_cilk_for_test07_detach_FlowParam{

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_pfor_body_activate = Map(
    "getelementptr0" -> 0,
    "load1" -> 1,
    "getelementptr2" -> 2,
    "load3" -> 3,
    "getelementptr4" -> 4,
    "load5" -> 5,
    "getelementptr6" -> 6,
    "load7" -> 7,
    "sub8" -> 8,
    "mul9" -> 9,
    "sub10" -> 10,
    "mul11" -> 11,
    "add12" -> 12,
    "getelementptr13" -> 13,
    "store14" -> 14,
    "ret15" -> 15
  )


  //  %0 = getelementptr inbounds [2 x i32], [2 x i32]* %p1.in, i32 %i.032.in, i32 0, !UID !7, !ScalaLabel !8
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %1 = load i32, i32* %0, align 4, !tbaa !9, !UID !13, !ScalaLabel !14
  val load1_in = Map(
    "getelementptr0" -> 0
  )


  //  %2 = getelementptr inbounds [2 x i32], [2 x i32]* %p1.in, i32 %i.032.in, i32 1, !UID !15, !ScalaLabel !16
  val getelementptr2_in = Map(
    "field0" -> 1,
    "field1" -> 1
  )


  //  %3 = load i32, i32* %2, align 4, !tbaa !9, !UID !17, !ScalaLabel !18
  val load3_in = Map(
    "getelementptr2" -> 0
  )


  //  %4 = getelementptr inbounds [2 x i32], [2 x i32]* %p2.in, i32 %i.032.in, i32 0, !UID !19, !ScalaLabel !20
  val getelementptr4_in = Map(
    "field2" -> 0,
    "field1" -> 2
  )


  //  %5 = load i32, i32* %4, align 4, !tbaa !9, !UID !21, !ScalaLabel !22
  val load5_in = Map(
    "getelementptr4" -> 0
  )


  //  %6 = getelementptr inbounds [2 x i32], [2 x i32]* %p2.in, i32 %i.032.in, i32 1, !UID !23, !ScalaLabel !24
  val getelementptr6_in = Map(
    "field2" -> 1,
    "field1" -> 3
  )


  //  %7 = load i32, i32* %6, align 4, !tbaa !9, !UID !25, !ScalaLabel !26
  val load7_in = Map(
    "getelementptr6" -> 0
  )


  //  %8 = sub nsw i32 %5, %1, !UID !27, !ScalaLabel !28
  val sub8_in = Map(
    "load5" -> 0,
    "load1" -> 0
  )


  //  %9 = mul nsw i32 %8, %8, !UID !29, !ScalaLabel !30
  val mul9_in = Map(
    "sub8" -> 0,
    "sub8" -> 1
  )


  //  %10 = sub nsw i32 %7, %3, !UID !31, !ScalaLabel !32
  val sub10_in = Map(
    "load7" -> 0,
    "load3" -> 0
  )


  //  %11 = mul nsw i32 %10, %10, !UID !33, !ScalaLabel !34
  val mul11_in = Map(
    "sub10" -> 0,
    "sub10" -> 1
  )


  //  %12 = add nuw nsw i32 %11, %9, !UID !35, !ScalaLabel !36
  val add12_in = Map(
    "mul11" -> 0,
    "mul9" -> 0
  )


  //  %13 = getelementptr inbounds i32, i32* %d.in, i32 %i.032.in, !UID !37, !ScalaLabel !38
  val getelementptr13_in = Map(
    "field3" -> 0,
    "field1" -> 4
  )


  //  store i32 %12, i32* %13, align 4, !tbaa !9, !UID !39, !ScalaLabel !40
  val store14_in = Map(
    "add12" -> 0,
    "getelementptr13" -> 0
  )


  //  ret void, !UID !41, !BB_UID !42, !ScalaLabel !43
  val ret15_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test07_detachDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class cilk_for_test07_detachDF(implicit p: Parameters) extends cilk_for_test07_detachDFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val CacheMem = Module(new UnifiedController(ID=0,Size=1024,NReads=4,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=4,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCall(List(32,32,32,32)))
  InputSplitter.io.In <> io.in

  // Manually added
  val field0_expand = Module(new ExpandNode(NumOuts=2,ID=101)(new DataBundle))
  field0_expand.io.enable.valid := true.B
  field0_expand.io.enable.bits.control := true.B
  field0_expand.io.InData <> InputSplitter.io.Out.data("field0")

  val field1_expand = Module(new ExpandNode(NumOuts=5,ID=102)(new DataBundle))
  field1_expand.io.enable.valid := true.B
  field1_expand.io.enable.bits.control := true.B
  field1_expand.io.InData <> InputSplitter.io.Out.data("field1")

  val field2_expand = Module(new ExpandNode(NumOuts=2,ID=103)(new DataBundle))
  field2_expand.io.enable.valid := true.B
  field2_expand.io.enable.bits.control := true.B
  field2_expand.io.InData <> InputSplitter.io.Out.data("field2")


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 0))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = getelementptr inbounds [2 x i32], [2 x i32]* %p1.in, i32 %i.032.in, i32 0, !UID !7, !ScalaLabel !8
  val getelementptr0 = Module (new GepTwoNode(NumOuts = 1, ID = 0)(numByte1 = 8, numByte2 = 4)) // Manual fix


  //  %1 = load i32, i32* %0, align 4, !tbaa !9, !UID !13, !ScalaLabel !14
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))


  //  %2 = getelementptr inbounds [2 x i32], [2 x i32]* %p1.in, i32 %i.032.in, i32 1, !UID !15, !ScalaLabel !16
  val getelementptr2 = Module (new GepTwoNode(NumOuts = 1, ID = 2)(numByte1 = 8, numByte2 = 4)) // Manual fix


  //  %3 = load i32, i32* %2, align 4, !tbaa !9, !UID !17, !ScalaLabel !18
  val load3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=3,RouteID=1))


  //  %4 = getelementptr inbounds [2 x i32], [2 x i32]* %p2.in, i32 %i.032.in, i32 0, !UID !19, !ScalaLabel !20
  val getelementptr4 = Module (new GepTwoNode(NumOuts = 1, ID = 4)(numByte1 = 8, numByte2 = 4)) // Manual fix


  //  %5 = load i32, i32* %4, align 4, !tbaa !9, !UID !21, !ScalaLabel !22
  val load5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=5,RouteID=2))


  //  %6 = getelementptr inbounds [2 x i32], [2 x i32]* %p2.in, i32 %i.032.in, i32 1, !UID !23, !ScalaLabel !24
  val getelementptr6 = Module (new GepTwoNode(NumOuts = 1, ID = 6)(numByte1 = 8, numByte2 = 4)) // Manual fix


  //  %7 = load i32, i32* %6, align 4, !tbaa !9, !UID !25, !ScalaLabel !26
  val load7 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=3))


  //  %8 = sub nsw i32 %5, %1, !UID !27, !ScalaLabel !28
  val sub8 = Module (new ComputeNode(NumOuts = 2, ID = 8, opCode = "sub")(sign=false))


  //  %9 = mul nsw i32 %8, %8, !UID !29, !ScalaLabel !30
  val mul9 = Module (new ComputeNode(NumOuts = 1, ID = 9, opCode = "mul")(sign=false))


  //  %10 = sub nsw i32 %7, %3, !UID !31, !ScalaLabel !32
  val sub10 = Module (new ComputeNode(NumOuts = 2, ID = 10, opCode = "sub")(sign=false))


  //  %11 = mul nsw i32 %10, %10, !UID !33, !ScalaLabel !34
  val mul11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "mul")(sign=false))


  //  %12 = add nuw nsw i32 %11, %9, !UID !35, !ScalaLabel !36
  val add12 = Module (new ComputeNode(NumOuts = 1, ID = 12, opCode = "add")(sign=false))


  //  %13 = getelementptr inbounds i32, i32* %d.in, i32 %i.032.in, !UID !37, !ScalaLabel !38
  val getelementptr13 = Module (new GepOneNode(NumOuts = 1, ID = 13)(numByte1 = 4)) // Manual fix


  //  store i32 %12, i32* %13, align 4, !tbaa !9, !UID !39, !ScalaLabel !40
  val store14 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=14,RouteID=0))


  //  ret void, !UID !41, !BB_UID !42, !ScalaLabel !43
  val ret15 = Module(new RetNode(retTypes=List(32), ID=15))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test07_detach_FlowParam



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


  // There is no branch instruction



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_my_pfor_body.io.Out(0)
  getelementptr2.io.enable <> bb_my_pfor_body.io.Out(1)
  getelementptr4.io.enable <> bb_my_pfor_body.io.Out(2)
  getelementptr6.io.enable <> bb_my_pfor_body.io.Out(3)
  getelementptr13.io.enable <> bb_my_pfor_body.io.Out(4)

  load1.io.enable.enq(ControlBundle.active())
  load3.io.enable.enq(ControlBundle.active())
  load5.io.enable.enq(ControlBundle.active())
  load7.io.enable.enq(ControlBundle.active())
  sub8.io.enable.enq(ControlBundle.active())
  mul9.io.enable.enq(ControlBundle.active())
  sub10.io.enable.enq(ControlBundle.active())
  mul11.io.enable.enq(ControlBundle.active())
  add12.io.enable.enq(ControlBundle.active())
  store14.io.enable.enq(ControlBundle.active())
  ret15.io.enable.enq(ControlBundle.active())





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
  getelementptr0.io.baseAddress <> field0_expand.io.Out(0)


  // Wiring GEP instruction to the function argument
  getelementptr0.io.idx1 <> field1_expand.io.Out(0)


  // Wiring GEP instruction to the Constant
  getelementptr0.io.idx2.valid :=  true.B
  getelementptr0.io.idx2.bits.predicate :=  true.B
  getelementptr0.io.idx2.bits.data :=  0.U


  // Wiring Load instruction to the parent instruction
  load1.io.GepAddr <> getelementptr0.io.Out(param.load1_in("getelementptr0"))
  load1.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load1.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr2.io.baseAddress <> field0_expand.io.Out(1)


  // Wiring GEP instruction to the function argument
  getelementptr2.io.idx1 <> field1_expand.io.Out(1)


  // Wiring GEP instruction to the Constant
  getelementptr2.io.idx2.valid :=  true.B
  getelementptr2.io.idx2.bits.predicate :=  true.B
  getelementptr2.io.idx2.bits.data :=  1.U


  // Wiring Load instruction to the parent instruction
  load3.io.GepAddr <> getelementptr2.io.Out(param.load3_in("getelementptr2"))
  load3.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load3.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr4.io.baseAddress <> field2_expand.io.Out(0)


  // Wiring GEP instruction to the function argument
  getelementptr4.io.idx1 <> field1_expand.io.Out(2)


  // Wiring GEP instruction to the Constant
  getelementptr4.io.idx2.valid :=  true.B
  getelementptr4.io.idx2.bits.predicate :=  true.B
  getelementptr4.io.idx2.bits.data :=  0.U


  // Wiring Load instruction to the parent instruction
  load5.io.GepAddr <> getelementptr4.io.Out(param.load5_in("getelementptr4"))
  load5.io.memResp <> CacheMem.io.ReadOut(2)
  CacheMem.io.ReadIn(2) <> load5.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr6.io.baseAddress <> field2_expand.io.Out(1)


  // Wiring GEP instruction to the function argument
  getelementptr6.io.idx1 <> field1_expand.io.Out(3)


  // Wiring GEP instruction to the Constant
  getelementptr6.io.idx2.valid :=  true.B
  getelementptr6.io.idx2.bits.predicate :=  true.B
  getelementptr6.io.idx2.bits.data :=  1.U


  // Wiring Load instruction to the parent instruction
  load7.io.GepAddr <> getelementptr6.io.Out(param.load7_in("getelementptr6"))
  load7.io.memResp <> CacheMem.io.ReadOut(3)
  CacheMem.io.ReadIn(3) <> load7.io.memReq




  // Wiring instructions
  sub8.io.LeftIO <> load5.io.Out(param.sub8_in("load5"))

  // Wiring instructions
  sub8.io.RightIO <> load1.io.Out(param.sub8_in("load1"))

  // Wiring instructions
  mul9.io.LeftIO <> sub8.io.Out(0) // Manually fixed

  // Wiring instructions
  mul9.io.RightIO <> sub8.io.Out(1) // Manually fixed

  // Wiring instructions
  sub10.io.LeftIO <> load7.io.Out(param.sub10_in("load7"))

  // Wiring instructions
  sub10.io.RightIO <> load3.io.Out(param.sub10_in("load3"))

  // Wiring instructions
  mul11.io.LeftIO <> sub10.io.Out(0)  // Manually fixed

  // Wiring instructions
  mul11.io.RightIO <> sub10.io.Out(1)  // Manually fixed

  // Wiring instructions
  add12.io.LeftIO <> mul11.io.Out(param.add12_in("mul11"))

  // Wiring instructions
  add12.io.RightIO <> mul9.io.Out(param.add12_in("mul9"))

  // Wiring GEP instruction to the function argument
  getelementptr13.io.baseAddress <> InputSplitter.io.Out.data("field3")


  // Wiring GEP instruction to the function argument
  getelementptr13.io.idx1 <> field1_expand.io.Out(4)


  store14.io.inData <> add12.io.Out(param.store14_in("add12"))



  // Wiring Store instruction to the parent instruction
  store14.io.GepAddr <> getelementptr13.io.Out(param.store14_in("getelementptr13"))
  store14.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store14.io.memReq
  //store14.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret15.io.In.data("field0") <> store14.io.Out(0)  // Manually connected
  //ret15.io.In.data("field0").bits.data := 1.U
  //ret15.io.In.data("field0").bits.predicate := true.B
  //ret15.io.In.data("field0").valid := true.B
  io.out <> ret15.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test07_detachMain extends App {
  val dir = new File("RTL/cilk_for_test07_detach") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test07_detachDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


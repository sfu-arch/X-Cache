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

object Data_cilk_for_test08_detach_FlowParam{

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
    "mul6" -> 6,
    "mul7" -> 7,
    "add8" -> 8,
    "mul9" -> 9,
    "add10" -> 10,
    "ashr11" -> 11,
    "getelementptr12" -> 12,
    "store13" -> 13,
    "mul14" -> 14,
    "mul15" -> 15,
    "add16" -> 16,
    "mul17" -> 17,
    "add18" -> 18,
    "ashr19" -> 19,
    "getelementptr20" -> 20,
    "store21" -> 21,
    "mul22" -> 22,
    "mul23" -> 23,
    "add24" -> 24,
    "mul25" -> 25,
    "add26" -> 26,
    "ashr27" -> 27,
    "getelementptr28" -> 28,
    "store29" -> 29,
    "ret30" -> 30
  )


  //  %0 = getelementptr inbounds [3 x i32], [3 x i32]* %rgb.in, i32 %i.049.in, i32 0, !UID !15, !ScalaLabel !16
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %1 = load i32, i32* %0, align 4, !tbaa !17, !UID !21, !ScalaLabel !22
  val load1_in = Map(
    "getelementptr0" -> 0
  )


  //  %2 = getelementptr inbounds [3 x i32], [3 x i32]* %rgb.in, i32 %i.049.in, i32 1, !UID !23, !ScalaLabel !24
  val getelementptr2_in = Map(
    "field0" -> 1,
    "field1" -> 1
  )


  //  %3 = load i32, i32* %2, align 4, !tbaa !17, !UID !25, !ScalaLabel !26
  val load3_in = Map(
    "getelementptr2" -> 0
  )


  //  %4 = getelementptr inbounds [3 x i32], [3 x i32]* %rgb.in, i32 %i.049.in, i32 2, !UID !27, !ScalaLabel !28
  val getelementptr4_in = Map(
    "field0" -> 2,
    "field1" -> 2
  )


  //  %5 = load i32, i32* %4, align 4, !tbaa !17, !UID !29, !ScalaLabel !30
  val load5_in = Map(
    "getelementptr4" -> 0
  )


  //  %6 = mul nsw i32 %1, 27030, !UID !31, !ScalaLabel !32
  val mul6_in = Map(
    "load1" -> 0
  )


  //  %7 = mul nsw i32 %3, 23434, !UID !33, !ScalaLabel !34
  val mul7_in = Map(
    "load3" -> 0
  )


  //  %8 = add nsw i32 %7, %6, !UID !35, !ScalaLabel !36
  val add8_in = Map(
    "mul7" -> 0,
    "mul6" -> 0
  )


  //  %9 = mul nsw i32 %5, 11825, !UID !37, !ScalaLabel !38
  val mul9_in = Map(
    "load5" -> 0
  )


  //  %10 = add nsw i32 %8, %9, !UID !39, !ScalaLabel !40
  val add10_in = Map(
    "add8" -> 0,
    "mul9" -> 0
  )


  //  %11 = ashr i32 %10, 16, !UID !41, !ScalaLabel !42
  val ashr11_in = Map(
    "add10" -> 0
  )


  //  %12 = getelementptr inbounds [3 x i32], [3 x i32]* %xyz.in, i32 %i.049.in, i32 0, !UID !43, !ScalaLabel !44
  val getelementptr12_in = Map(
    "field2" -> 0,
    "field1" -> 3
  )


  //  store i32 %11, i32* %12, align 4, !tbaa !17, !UID !45, !ScalaLabel !46
  val store13_in = Map(
    "ashr11" -> 0,
    "getelementptr12" -> 0
  )


  //  %13 = mul nsw i32 %1, 13937, !UID !47, !ScalaLabel !48
  val mul14_in = Map(
    "load1" -> 1
  )


  //  %14 = mul nsw i32 %3, 46868, !UID !49, !ScalaLabel !50
  val mul15_in = Map(
    "load3" -> 1
  )


  //  %15 = add nsw i32 %14, %13, !UID !51, !ScalaLabel !52
  val add16_in = Map(
    "mul15" -> 0,
    "mul14" -> 0
  )


  //  %16 = mul nsw i32 %5, 4730, !UID !53, !ScalaLabel !54
  val mul17_in = Map(
    "load5" -> 1
  )


  //  %17 = add nsw i32 %15, %16, !UID !55, !ScalaLabel !56
  val add18_in = Map(
    "add16" -> 0,
    "mul17" -> 0
  )


  //  %18 = ashr i32 %17, 16, !UID !57, !ScalaLabel !58
  val ashr19_in = Map(
    "add18" -> 0
  )


  //  %19 = getelementptr inbounds [3 x i32], [3 x i32]* %xyz.in, i32 %i.049.in, i32 1, !UID !59, !ScalaLabel !60
  val getelementptr20_in = Map(
    "field2" -> 1,
    "field1" -> 4
  )


  //  store i32 %18, i32* %19, align 4, !tbaa !17, !UID !61, !ScalaLabel !62
  val store21_in = Map(
    "ashr19" -> 0,
    "getelementptr20" -> 0
  )


  //  %20 = mul nsw i32 %1, 1267, !UID !63, !ScalaLabel !64
  val mul22_in = Map(
    "load1" -> 2
  )


  //  %21 = mul nsw i32 %3, 7811, !UID !65, !ScalaLabel !66
  val mul23_in = Map(
    "load3" -> 2
  )


  //  %22 = add nsw i32 %21, %20, !UID !67, !ScalaLabel !68
  val add24_in = Map(
    "mul23" -> 0,
    "mul22" -> 0
  )


  //  %23 = mul nsw i32 %5, 62279, !UID !69, !ScalaLabel !70
  val mul25_in = Map(
    "load5" -> 2
  )


  //  %24 = add nsw i32 %22, %23, !UID !71, !ScalaLabel !72
  val add26_in = Map(
    "add24" -> 0,
    "mul25" -> 0
  )


  //  %25 = ashr i32 %24, 16, !UID !73, !ScalaLabel !74
  val ashr27_in = Map(
    "add26" -> 0
  )


  //  %26 = getelementptr inbounds [3 x i32], [3 x i32]* %xyz.in, i32 %i.049.in, i32 2, !UID !75, !ScalaLabel !76
  val getelementptr28_in = Map(
    "field2" -> 2,
    "field1" -> 5
  )


  //  store i32 %25, i32* %26, align 4, !tbaa !17, !UID !77, !ScalaLabel !78
  val store29_in = Map(
    "ashr27" -> 0,
    "getelementptr28" -> 0
  )


  //  ret void, !UID !79, !BB_UID !80, !ScalaLabel !81
  val ret30_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test08_detachDFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class cilk_for_test08_detachDF(implicit p: Parameters) extends cilk_for_test08_detachDFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=3,NWrites=3)
		            (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=3,NWrites=3)
		            (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32,32)))
  InputSplitter.io.In <> io.in

  // Manually added
  val field0_expand = Module(new ExpandNode(NumOuts=3,ID=101)(new DataBundle))
  field0_expand.io.enable.valid := true.B
  field0_expand.io.enable.bits.control := true.B
  field0_expand.io.InData <> InputSplitter.io.Out.data("field0")

  val field1_expand = Module(new ExpandNode(NumOuts=6,ID=102)(new DataBundle))
  field1_expand.io.enable.valid := true.B
  field1_expand.io.enable.bits.control := true.B
  field1_expand.io.InData <> InputSplitter.io.Out.data("field1")

  val field2_expand = Module(new ExpandNode(NumOuts=3,ID=103)(new DataBundle))
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

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 0))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = getelementptr inbounds [3 x i32], [3 x i32]* %rgb.in, i32 %i.050.in, i32 0, !UID !15, !ScalaLabel !16
  val getelementptr0 = Module (new GepTwoNode(NumOuts = 1, ID = 0)(numByte1 = 12, numByte2 = 4)) // Manual fix


  //  %1 = load i32, i32* %0, align 4, !tbaa !17, !UID !21, !ScalaLabel !22
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=3,ID=1,RouteID=0))


  //  %2 = getelementptr inbounds [3 x i32], [3 x i32]* %rgb.in, i32 %i.050.in, i32 1, !UID !23, !ScalaLabel !24
  val getelementptr2 = Module (new GepTwoNode(NumOuts = 1, ID = 2)(numByte1 = 12, numByte2 = 4))


  //  %3 = load i32, i32* %2, align 4, !tbaa !17, !UID !25, !ScalaLabel !26
  val load3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=3,ID=3,RouteID=1))


  //  %4 = getelementptr inbounds [3 x i32], [3 x i32]* %rgb.in, i32 %i.050.in, i32 2, !UID !27, !ScalaLabel !28
  val getelementptr4 = Module (new GepTwoNode(NumOuts = 1, ID = 4)(numByte1 = 12, numByte2 = 4))


  //  %5 = load i32, i32* %4, align 4, !tbaa !17, !UID !29, !ScalaLabel !30
  val load5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=3,ID=5,RouteID=2))


  //  %6 = mul nsw i32 %1, 27030, !UID !31, !ScalaLabel !32
  val mul6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign=false))


  //  %7 = mul nsw i32 %3, 23434, !UID !33, !ScalaLabel !34
  val mul7 = Module (new ComputeNode(NumOuts = 1, ID = 7, opCode = "mul")(sign=false))


  //  %8 = add nsw i32 %7, %6, !UID !35, !ScalaLabel !36
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  %9 = mul nsw i32 %5, 11825, !UID !37, !ScalaLabel !38
  val mul9 = Module (new ComputeNode(NumOuts = 1, ID = 9, opCode = "mul")(sign=false))


  //  %10 = add nsw i32 %8, %9, !UID !39, !ScalaLabel !40
  val add10 = Module (new ComputeNode(NumOuts = 1, ID = 10, opCode = "add")(sign=false))


  //  %11 = ashr i32 %10, 16, !UID !41, !ScalaLabel !42
  val ashr11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "lshr")(sign=false))  // Manually changed to lshr


  //  %12 = getelementptr inbounds [3 x i32], [3 x i32]* %xyz.in, i32 %i.050.in, i32 0, !UID !43, !ScalaLabel !44
  val getelementptr12 = Module (new GepTwoNode(NumOuts = 1, ID = 12)(numByte1 = 12, numByte2 = 4))


  //  store i32 %11, i32* %12, align 4, !tbaa !17, !UID !45, !ScalaLabel !46
  val store13 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=13,RouteID=0))


  //  %13 = mul nsw i32 %1, 13937, !UID !47, !ScalaLabel !48
  val mul14 = Module (new ComputeNode(NumOuts = 1, ID = 14, opCode = "mul")(sign=false))


  //  %14 = mul nsw i32 %3, 46868, !UID !49, !ScalaLabel !50
  val mul15 = Module (new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign=false))


  //  %15 = add nsw i32 %14, %13, !UID !51, !ScalaLabel !52
  val add16 = Module (new ComputeNode(NumOuts = 1, ID = 16, opCode = "add")(sign=false))


  //  %16 = mul nsw i32 %5, 4730, !UID !53, !ScalaLabel !54
  val mul17 = Module (new ComputeNode(NumOuts = 1, ID = 17, opCode = "mul")(sign=false))


  //  %17 = add nsw i32 %15, %16, !UID !55, !ScalaLabel !56
  val add18 = Module (new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign=false))


  //  %18 = ashr i32 %17, 16, !UID !57, !ScalaLabel !58
  val ashr19 = Module (new ComputeNode(NumOuts = 1, ID = 19, opCode = "lshr")(sign=false)) // Manually changed to lshr


  //  %19 = getelementptr inbounds [3 x i32], [3 x i32]* %xyz.in, i32 %i.050.in, i32 1, !UID !59, !ScalaLabel !60
  val getelementptr20 = Module (new GepTwoNode(NumOuts = 1, ID = 20)(numByte1 = 12, numByte2 = 4))


  //  store i32 %18, i32* %19, align 4, !tbaa !17, !UID !61, !ScalaLabel !62
  val store21 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=21,RouteID=1))


  //  %20 = mul nsw i32 %1, 1267, !UID !63, !ScalaLabel !64
  val mul22 = Module (new ComputeNode(NumOuts = 1, ID = 22, opCode = "mul")(sign=false))


  //  %21 = mul nsw i32 %3, 7811, !UID !65, !ScalaLabel !66
  val mul23 = Module (new ComputeNode(NumOuts = 1, ID = 23, opCode = "mul")(sign=false))


  //  %22 = add nsw i32 %21, %20, !UID !67, !ScalaLabel !68
  val add24 = Module (new ComputeNode(NumOuts = 1, ID = 24, opCode = "add")(sign=false))


  //  %23 = mul nsw i32 %5, 62279, !UID !69, !ScalaLabel !70
  val mul25 = Module (new ComputeNode(NumOuts = 1, ID = 25, opCode = "mul")(sign=false))


  //  %24 = add nsw i32 %22, %23, !UID !71, !ScalaLabel !72
  val add26 = Module (new ComputeNode(NumOuts = 1, ID = 26, opCode = "add")(sign=false))


  //  %25 = ashr i32 %24, 16, !UID !73, !ScalaLabel !74
  val ashr27 = Module (new ComputeNode(NumOuts = 1, ID = 27, opCode = "lshr")(sign=false)) // Manually changed to lshr


  //  %26 = getelementptr inbounds [3 x i32], [3 x i32]* %xyz.in, i32 %i.050.in, i32 2, !UID !75, !ScalaLabel !76
  val getelementptr28 = Module (new GepTwoNode(NumOuts = 1, ID = 28)(numByte1 = 12, numByte2 = 4))


  //  store i32 %25, i32* %26, align 4, !tbaa !17, !UID !77, !ScalaLabel !78
  val store29 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=29,RouteID=2))


  //  ret void, !UID !79, !BB_UID !80, !ScalaLabel !81
  val ret30 = Module(new RetNode(retTypes=List(32), ID=30))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test08_detach_FlowParam



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
  getelementptr12.io.enable <> bb_my_pfor_body.io.Out(3)
  getelementptr20.io.enable <> bb_my_pfor_body.io.Out(4)
  getelementptr28.io.enable <> bb_my_pfor_body.io.Out(5)


  load1.io.enable.enq(ControlBundle.active())
  load3.io.enable.enq(ControlBundle.active())
  load5.io.enable.enq(ControlBundle.active())
  mul6.io.enable.enq(ControlBundle.active())
  mul7.io.enable.enq(ControlBundle.active())
  add8.io.enable.enq(ControlBundle.active())
  mul9.io.enable.enq(ControlBundle.active())
  add10.io.enable.enq(ControlBundle.active())
  ashr11.io.enable.enq(ControlBundle.active())
  store13.io.enable.enq(ControlBundle.active())
  mul14.io.enable.enq(ControlBundle.active())
  mul15.io.enable.enq(ControlBundle.active())
  add16.io.enable.enq(ControlBundle.active())
  mul17.io.enable.enq(ControlBundle.active())
  add18.io.enable.enq(ControlBundle.active())
  ashr19.io.enable.enq(ControlBundle.active())
  store21.io.enable.enq(ControlBundle.active())
  mul22.io.enable.enq(ControlBundle.active())
  mul23.io.enable.enq(ControlBundle.active())
  add24.io.enable.enq(ControlBundle.active())
  mul25.io.enable.enq(ControlBundle.active())
  add26.io.enable.enq(ControlBundle.active())
  ashr27.io.enable.enq(ControlBundle.active())
  store29.io.enable.enq(ControlBundle.active())
  ret30.io.enable.enq(ControlBundle.active())


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
  getelementptr0.io.baseAddress <> field0_expand.io.Out(1)


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
  getelementptr2.io.baseAddress <> field0_expand.io.Out(2)


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
  getelementptr4.io.baseAddress <> field0_expand.io.Out(0)


  // Wiring GEP instruction to the function argument
  getelementptr4.io.idx1 <> field1_expand.io.Out(2)


  // Wiring GEP instruction to the Constant
  getelementptr4.io.idx2.valid :=  true.B
  getelementptr4.io.idx2.bits.predicate :=  true.B
  getelementptr4.io.idx2.bits.data :=  2.U


  // Wiring Load instruction to the parent instruction
  load5.io.GepAddr <> getelementptr4.io.Out(param.load5_in("getelementptr4"))
  load5.io.memResp <> CacheMem.io.ReadOut(2)
  CacheMem.io.ReadIn(2) <> load5.io.memReq




  // Wiring instructions
  mul6.io.LeftIO <> load1.io.Out(param.mul6_in("load1"))

  // Wiring constant
  mul6.io.RightIO.bits.data := 27030.U
  mul6.io.RightIO.bits.predicate := true.B
  mul6.io.RightIO.valid := true.B

  // Wiring instructions
  mul7.io.LeftIO <> load3.io.Out(param.mul7_in("load3"))

  // Wiring constant
  mul7.io.RightIO.bits.data := 23434.U
  mul7.io.RightIO.bits.predicate := true.B
  mul7.io.RightIO.valid := true.B

  // Wiring instructions
  add8.io.LeftIO <> mul7.io.Out(param.add8_in("mul7"))

  // Wiring instructions
  add8.io.RightIO <> mul6.io.Out(param.add8_in("mul6"))

  // Wiring instructions
  mul9.io.LeftIO <> load5.io.Out(param.mul9_in("load5"))

  // Wiring constant
  mul9.io.RightIO.bits.data := 11825.U
  mul9.io.RightIO.bits.predicate := true.B
  mul9.io.RightIO.valid := true.B

  // Wiring instructions
  add10.io.LeftIO <> add8.io.Out(param.add10_in("add8"))

  // Wiring instructions
  add10.io.RightIO <> mul9.io.Out(param.add10_in("mul9"))

  // Wiring instructions
  ashr11.io.LeftIO <> add10.io.Out(param.ashr11_in("add10"))

  // Wiring constant
  ashr11.io.RightIO.bits.data := 16.U
  ashr11.io.RightIO.bits.predicate := true.B
  ashr11.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr12.io.baseAddress <> field2_expand.io.Out(0)


  // Wiring GEP instruction to the function argument
  getelementptr12.io.idx1 <> field1_expand.io.Out(3)


  // Wiring GEP instruction to the Constant
  getelementptr12.io.idx2.valid :=  true.B
  getelementptr12.io.idx2.bits.predicate :=  true.B
  getelementptr12.io.idx2.bits.data :=  0.U


  store13.io.inData <> ashr11.io.Out(param.store13_in("ashr11"))



  // Wiring Store instruction to the parent instruction
  store13.io.GepAddr <> getelementptr12.io.Out(param.store13_in("getelementptr12"))
  store13.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store13.io.memReq
  store13.io.Out(0).ready := true.B



  // Wiring instructions
  mul14.io.LeftIO <> load1.io.Out(param.mul14_in("load1"))

  // Wiring constant
  mul14.io.RightIO.bits.data := 13937.U
  mul14.io.RightIO.bits.predicate := true.B
  mul14.io.RightIO.valid := true.B

  // Wiring instructions
  mul15.io.LeftIO <> load3.io.Out(param.mul15_in("load3"))

  // Wiring constant
  mul15.io.RightIO.bits.data := 46868.U
  mul15.io.RightIO.bits.predicate := true.B
  mul15.io.RightIO.valid := true.B

  // Wiring instructions
  add16.io.LeftIO <> mul15.io.Out(param.add16_in("mul15"))

  // Wiring instructions
  add16.io.RightIO <> mul14.io.Out(param.add16_in("mul14"))

  // Wiring instructions
  mul17.io.LeftIO <> load5.io.Out(param.mul17_in("load5"))

  // Wiring constant
  mul17.io.RightIO.bits.data := 4730.U
  mul17.io.RightIO.bits.predicate := true.B
  mul17.io.RightIO.valid := true.B

  // Wiring instructions
  add18.io.LeftIO <> add16.io.Out(param.add18_in("add16"))

  // Wiring instructions
  add18.io.RightIO <> mul17.io.Out(param.add18_in("mul17"))

  // Wiring instructions
  ashr19.io.LeftIO <> add18.io.Out(param.ashr19_in("add18"))

  // Wiring constant
  ashr19.io.RightIO.bits.data := 16.U
  ashr19.io.RightIO.bits.predicate := true.B
  ashr19.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr20.io.baseAddress <> field2_expand.io.Out(1)


  // Wiring GEP instruction to the function argument
  getelementptr20.io.idx1 <> field1_expand.io.Out(4)


  // Wiring GEP instruction to the Constant
  getelementptr20.io.idx2.valid :=  true.B
  getelementptr20.io.idx2.bits.predicate :=  true.B
  getelementptr20.io.idx2.bits.data :=  1.U


  store21.io.inData <> ashr19.io.Out(param.store21_in("ashr19"))



  // Wiring Store instruction to the parent instruction
  store21.io.GepAddr <> getelementptr20.io.Out(param.store21_in("getelementptr20"))
  store21.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store21.io.memReq
  store21.io.Out(0).ready := true.B



  // Wiring instructions
  mul22.io.LeftIO <> load1.io.Out(param.mul22_in("load1"))

  // Wiring constant
  mul22.io.RightIO.bits.data := 1267.U
  mul22.io.RightIO.bits.predicate := true.B
  mul22.io.RightIO.valid := true.B

  // Wiring instructions
  mul23.io.LeftIO <> load3.io.Out(param.mul23_in("load3"))

  // Wiring constant
  mul23.io.RightIO.bits.data := 7811.U
  mul23.io.RightIO.bits.predicate := true.B
  mul23.io.RightIO.valid := true.B

  // Wiring instructions
  add24.io.LeftIO <> mul23.io.Out(param.add24_in("mul23"))

  // Wiring instructions
  add24.io.RightIO <> mul22.io.Out(param.add24_in("mul22"))

  // Wiring instructions
  mul25.io.LeftIO <> load5.io.Out(param.mul25_in("load5"))

  // Wiring constant
  mul25.io.RightIO.bits.data := 62279.U
  mul25.io.RightIO.bits.predicate := true.B
  mul25.io.RightIO.valid := true.B

  // Wiring instructions
  add26.io.LeftIO <> add24.io.Out(param.add26_in("add24"))

  // Wiring instructions
  add26.io.RightIO <> mul25.io.Out(param.add26_in("mul25"))

  // Wiring instructions
  ashr27.io.LeftIO <> add26.io.Out(param.ashr27_in("add26"))

  // Wiring constant
  ashr27.io.RightIO.bits.data := 16.U
  ashr27.io.RightIO.bits.predicate := true.B
  ashr27.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr28.io.baseAddress <> field2_expand.io.Out(2)


  // Wiring GEP instruction to the function argument
  getelementptr28.io.idx1 <> field1_expand.io.Out(5)


  // Wiring GEP instruction to the Constant
  getelementptr28.io.idx2.valid :=  true.B
  getelementptr28.io.idx2.bits.predicate :=  true.B
  getelementptr28.io.idx2.bits.data :=  2.U


  store29.io.inData <> ashr27.io.Out(param.store29_in("ashr27"))



  // Wiring Store instruction to the parent instruction
  store29.io.GepAddr <> getelementptr28.io.Out(param.store29_in("getelementptr28"))
  store29.io.memResp  <> CacheMem.io.WriteOut(2)
  CacheMem.io.WriteIn(2) <> store29.io.memReq
  store29.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret30.io.In.data("field0") <> store29.io.Out(0)  // Manually connected
//  ret30.io.In.data("field0").bits.data := 1.U
//  ret30.io.In.data("field0").bits.predicate := true.B
//  ret30.io.In.data("field0").valid := true.B
  io.out <> ret30.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test08_detachMain extends App {
  val dir = new File("RTL/cilk_for_test08_detach") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test08_detachDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


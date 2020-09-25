//
//package tensorKernels
//
//import chisel3._
//import chisel3.util._
//import config._
//import dnn.memory._
//import dnn.types.{OperatorDot, OperatorReduction}
//import node.Shapes
//import shell._
////import vta.util.config._
//
//
///** TensorLoad.
//  *
//  * Load 1D and 2D tensors from main memory (DRAM) to input/weight
//  * scratchpads (SRAM). Also, there is support for zero padding, while
//  * doing the load. Zero-padding works on the y and x axis, and it is
//  * managed by TensorPadCtrl. The TensorDataCtrl is in charge of
//  * handling the way tensors are stored on the scratchpads.
//  */
//class MVM_BlockIO(NumRows: Int, vecType: String = "none", memTensorType: String = "none")(implicit val p: Parameters)
//  extends Module {
//  val tpMem = new TensorParams(memTensorType)
//  val tpVec = new TensorParams(vecType)
//
//  val mp = p(ShellKey).memParams
//  val io = IO(new Bundle {
//    val start = Input(Bool())
//    val done = Output(Bool())
//
//    val in1_BaseAddr = Input(UInt(mp.addrBits.W))
//    val in2_BaseAddr = Input(UInt(mp.addrBits.W))
//
//    val outBaseAddr = Input(UInt(mp.addrBits.W))
//
//    val len = Input(UInt(mp.addrBits.W))
//
//    val vme_rd = Vec(NumRows, new VMEReadMaster)
//    //    val vme_wr = Vec(NumRows, new VMEWriteMaster)
//    val vme_wr = new VMEWriteMaster
//
//
//    val inDMA_time = Output(UInt(mp.addrBits.W))
//    val outDMA_time = Output(UInt(mp.addrBits.W))
//    val merge_time = Output(UInt(mp.addrBits.W))
//  })
//}
//
//class MVM_Block[L <: Shapes : OperatorDot : OperatorReduction]
//(NumRows: Int, vecType: String = "none", memTensorType: String = "none")
//(vecShape: => L)(implicit p: Parameters)
//  extends MVM_BlockIO(NumRows, vecType, memTensorType)(p) {
//
//  val inDMA1 =  Module(new inDMA_act_HWC(NumRows = 1, 1, memTensorType)(vecShape))
//  val inDMA2 =  Module(new inDMA_act_HWC(NumRows = 1, 1, memTensorType)(vecShape))
//
//  //val ShapeTransformer = Module(new PWShapeTransformer(NumRows = 2, 1, 20, memTensorType)(vecShape))
//
//
//  val Dispatcher = Module(new Dispatcher(numSeg = 8 , doneTh = 100))
//
//  val outDMA = Module(new outDMA_act(NumRows = 2, 20, memTensorType))
//
//  val readTensorCnt = Counter(tpMem.memDepth)
//
//  val sIdle :: sInRead :: sExec :: Nil = Enum(3)
//  val state = RegInit(sIdle)
//
//  io.done := false.B
//  Dispatcher.io.start := false.B
//
//  val inDMA_time = Counter(2000)
//  val outDMA_time = Counter(2000)
//  val merge_time = Counter(2000)
//
//  io.inDMA_time := inDMA_time.value
//  io.outDMA_time := outDMA_time.value
//  io.merge_time := merge_time.value
//
//  /* ================================================================== *
//    *                      inDMA_acts & loadNodes                       *
//    * ================================================================== */
//
//  inDMA1.io.start := io.start
//  inDMA1.io.rowWidth := io.len
//  inDMA1.io.depth := 1.U
//  inDMA1.io.baddr := io.in1_BaseAddr
//
//  inDMA2.io.start := io.start
//  inDMA2.io.rowWidth := io.len
//  inDMA2.io.depth := 1.U
//  inDMA2.io.baddr := io.in2_BaseAddr
//
//  //ShapeTransformer.io.rowWidth := io.len
//  //ShapeTransformer.io.depth := 1.U
//
//  //inDMA1.io.tensor(0) <> ShapeTransformer.io.tensor(0)
//  //inDMA2.io.tensor(0) <> ShapeTransformer.io.tensor(1)
//  io.vme_rd(0) <> inDMA1.io.vme_rd(0)
//  io.vme_rd(1) <> inDMA2.io.vme_rd(0)
//
//
//  io.vme_wr <> outDMA.io.vme_wr(0)
//  //  for (i <- 0 until NumRows) {
//  //    io.vme_wr(i) <> outDMA.io.vme_wr(i)
//  //    outDMA.io.last(i) := Merger.io.last
//  //  }
//
//  outDMA.io.last(0) := Dispatcher.io.done
//  //  outDMA.io.last(1) := false.B
//
//  val inDMA_doneR = for (i <- 0 until 2) yield {
//    val doneReg = RegInit(init = false.B)
//    doneReg
//  }
//
//  //ShapeTransformer.io.start := inDMA_doneR.reduceLeft(_ && _)
//
//  when (inDMA_doneR.reduceLeft(_ && _)) {
//    inDMA_doneR.foreach(a => a := false.B)
//  }
//
//  when(inDMA1.io.done) {inDMA_doneR(0) := true.B}
//  when(inDMA2.io.done) {inDMA_doneR(1) := true.B}
//
//  outDMA.io.start := Dispatcher.io.done
//
//  /* ================================================================== *
//    *                        loadNodes & mac1Ds                         *
//    * ================================================================== */
//
//  //  Merger.io.in1 <> ShapeTransformer.io.Out(0)(0)
//  //  Merger.io.in2 <> ShapeTransformer.io.Out(1)(0)
//  //Merger.io.in1.valid := ShapeTransformer.io.Out(0)(0).valid
//  //ShapeTransformer.io.Out(0)(0).ready := Merger.io.in1.ready
//  Dispatcher.io.colPtr := inDMA1.io.tensor(0).asUInt()
//  Dispatcher.io.rowPtr := inDMA2.io.tensor(0).asUInt()
////  Merger.io.in1.bits.row := 0.U
////  Merger.io.in1.bits.col := 0.U
////  Merger.io.in1.bits.valid := true.B
//
//  //Merger.io.in2.valid := ShapeTransformer.io.Out(1)(0).valid
//  //ShapeTransformer.io.Out(1)(0).ready := Merger.io.in2.ready
//  //Merger.io.in2.bits.data := ShapeTransformer.io.Out(1)(0).bits.data
//  //Merger.io.in2.bits.row := 0.U
//  //Merger.io.in2.bits.col := 0.U
//  //Merger.io.in2.bits.valid := true.B
//
//
//  outDMA.io.baddr := io.outBaseAddr
//  outDMA.io.rowWidth := io.len * 2.U
//
//  //  outDMA.io.in(0) <> Merger.io.out1
//  //  outDMA.io.in(1) <> Merger.io.out2
//  //Merger.io.out.ready := outDMA.io.in(0).ready
//  //outDMA.io.in(0).valid := Merger.io.out.valid
//  outDMA.io.in(0).bits.data := Dispatcher.io.rowSegSize
//  outDMA.io.in(1).bits.data := Dispatcher.io.colSegSize
//  outDMA.io.in(0).bits.predicate := true.B
//  outDMA.io.in(0).bits.valid := true.B
//  outDMA.io.in(0).bits.taskID := 0.U
//
//  //  Merger.io.out2.ready := outDMA.io.in(1).ready
//  //  outDMA.io.in(1).valid := Merger.io.out2.valid
//  //  outDMA.io.in(1).bits.data := Merger.io.out2.bits.data
//  //  outDMA.io.in(1).bits.predicate := true.B
//  //  outDMA.io.in(1).bits.valid := true.B
//  //  outDMA.io.in(1).bits.taskID := 0.U
//
//
//
//  /* ================================================================== *
//      *                        Done Signal                              *
//      * ================================================================== */
//
//  when(state === sIdle){
//    inDMA_time.value := 0.U
//    outDMA_time.value := 0.U
//    merge_time.value := 0.U
//  }
//
//  when(state === sInRead) {inDMA_time.inc()}
//  when(state === sExec) {merge_time.inc()}
//
//  switch(state) {
//    is(sIdle) {
//      when(io.start) {
//        inDMA1.io.start := true.B
//        inDMA2.io.start := true.B
//        state := sInRead
//      }
//    }
//    is(sInRead) {
//      when(inDMA_doneR.reduceLeft(_ && _)){
//        state := sExec
//        Dispatcher.io.start := true.B
//      }
//    }
//    is(sExec){
//      when(outDMA.io.done) {
//        io.done := true.B
//        state := sIdle
//      }
//
//    }
//  }
//}

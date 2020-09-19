
package tensorKernels


import chisel3._
import chisel3.util._
import config.{COLLEN, Parameters, ROWLEN, XLEN}
import interfaces.CustomDataBundle

class DispatcherIO()(implicit val p: Parameters) extends Module {
  val io = IO(new Bundle {

    val start = Input(Bool( ))
    val done = Output(Bool())


    val colPtr = Flipped(Decoupled((UInt(p(COLLEN).W))))
    val rowPtr = Flipped(Decoupled((UInt(p(ROWLEN).W))))
    val segThreshold = Flipped(Decoupled((UInt(p(XLEN).W))))

    val colSegAddr = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
    val colSegSize = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
    val rowSegAddr = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
    val rowSegSize = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
    val destNum = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))

  })
}

class Dispatcher(numSeg: Int , doneTh: Int)(implicit p: Parameters)
  extends DispatcherIO()(p) {
  require(numSeg > 0, "Number of segments must be a positive number")
  val newColPtr = Reg(UInt(p(XLEN).W))
  val oldColPtr = Reg(UInt(p(XLEN).W))

  val newRowPtr = Reg(UInt(p(XLEN).W))
  val oldRowPtr = Reg(UInt(p(XLEN).W))

  val rowDiff = Reg(UInt(p(XLEN).W))
  val colDiff = Reg(UInt(p(XLEN).W))

  val numMul = Reg(UInt(( p(XLEN)).W))
  val sigma = Reg(UInt((p(XLEN)).W))
  val sigmaOld = Reg(UInt((p(XLEN)).W))

  val rowDiffBuf = Reg(UInt(( p(XLEN)).W))
  val colDiffBuf = Reg(UInt(( p(XLEN)).W))

  val rowDiffSum = Reg(UInt(( p(XLEN)).W))
  val colDiffSum = Reg(UInt(( p(XLEN)).W))
  // outSigma is unnecessary; it's just for debugging
  val outSigma = Reg(UInt((p(XLEN)).W))

  val outColSize = Reg(UInt((p(XLEN)).W))
  val outRowSize = Reg(UInt((p(XLEN)).W))

  val loadColRow = Wire(Bool())
  val rstColRowSum = Wire(Bool())
  val rstSigma = Wire (Bool())
  val loadOutSigma = Wire (Bool())
  val rstOutSigma = Wire (Bool())
  val selZero = Wire(Bool())
  val loadColPtrReg = Wire(Bool())
  val loadRowPtrReg = Wire(Bool())
  val countUp = Wire (Bool())

  val partialSigma = Mux( selZero , sigma , 0.U)

  rstSigma := false.B
  loadOutSigma := false.B
  rstOutSigma := false.B
  selZero := false.B
  rstColRowSum := false.B
  loadColRow := false.B
  io.done := false.B

  newColPtr := io.colPtr
  newRowPtr := io.rowPtr

  oldColPtr := newColPtr
  oldRowPtr := newRowPtr

  rowDiff := newRowPtr - oldRowPtr
  colDiff := newColPtr - oldColPtr

  rowDiffBuf := rowDiff
  colDiffBuf := colDiff

  colDiffSum := colDiffSum + rowDiffBuf
  rowDiffSum := rowDiffSum + colDiffBuf


  numMul := rowDiff * colDiff
  sigma := numMul + partialSigma
  sigmaOld := sigma


  val (numberSeg, doneRR) = Counter(countUp , numSeg)
  val (doneCounter, done) = Counter(true.B , doneTh)

  io.rowSegSize.bits.data := outRowSize
  io.colSegSize.bits.data := outColSize
  io.destNum.bits.data := numberSeg
  io.colSegAddr.bits.data := 0.U
  io.rowSegAddr.bits.data := 0.U


  when (rstSigma){
    sigma := 0.U

  }

  when (loadOutSigma){
    outSigma := sigmaOld
  }

  when (rstOutSigma){
    outSigma := 0.U
  }

  when (loadColRow) {
    outColSize := colDiffSum
    outRowSize := rowDiffSum
  }

  when (rstColRowSum) {
    rowDiffSum := 0.U
    colDiffSum := 0.U
  }

  val sIdle :: sCheckWithTh :: sDone:: Nil = Enum(3)
  val state = RegInit(sIdle)

  switch (state){
    is (sIdle){
      when (io.start)
      {
        state := sCheckWithTh
      }
    }

    is (sCheckWithTh){
      when(sigma + numMul > io.segThreshold.asUInt() ) {
        loadOutSigma := true.B
        rstSigma := true.B
        selZero := true.B
        loadColRow:= true.B
        rstColRowSum := true.B
        printf(p" sigma = ${sigma} outRow = ${outRowSize}  outCol = ${outColSize}")
      }
      when(done === 1.U)
      {
        state := sDone
      }
    }
    is (sDone){
      io.done := true.B
      state := sIdle
    }
  }




}
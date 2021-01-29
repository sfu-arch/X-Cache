package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config.{WithAccelConfig, _}
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

class TBE (implicit p: Parameters)  extends AXIAccelBundle with HasCacheAccelParams {
  val state = new State()
//  val data = UInt (xlen.W)
  val way = UInt ((wayLen + 1).W)
//  val addr = UInt (addrLen.W)
//  val cmdPtr = UInt (nCom.W)

}
object TBE {

  def default (implicit p: Parameters): TBE = {
    val tbe = Wire(new TBE)
    tbe.state := State.default
//    tbe.data := 0.U
//    tbe.addr := 0.U
    tbe.way := tbe.nWays.U
//    tbe.cmdPtr := 0.U
    tbe
  }
}

class TBETableIO (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {


  val write = Vec( nParal, Flipped(Valid(new Bundle {
    val addr = (UInt(xlen.W))
    val command = (UInt())
    val mask = (UInt())
    val inputTBE= (new TBE)
  })))

  val read = Flipped(Valid(new Bundle {
    val addr = (UInt(xlen.W))
  }))

  val outputTBE = Valid(new TBE)

}

class   TBETable(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelParams {


  val (idle :: alloc :: dealloc :: read :: write :: Nil) = Enum(5)
  val (maskAll :: maskState :: maskWay :: Nil) = Enum(3)

  val io = IO(new TBETableIO())

  val TBEMemory = RegInit(VecInit(Seq.fill(tbeDepth)(TBE.default)))
  val TBEValid = RegInit(VecInit(Seq.fill(tbeDepth)(false.B)))
  val TBEAddr = RegInit(VecInit(Seq.fill(tbeDepth)((0.U(accelParams.addrLen.W)))))

  val isAlloc = Wire(Vec(nParal, Bool()))
  val isDealloc = Wire(Vec(nParal, Bool()))
  val isRead = Wire(Bool())
  val isWrite = Wire(Vec(nParal, Bool()))

  val idxAlloc = Wire(UInt((log2Ceil(tbeDepth) + 1).W))
  val idxRead = Wire(UInt((log2Ceil(tbeDepth) + 1).W))
  val idxUpdate = Wire(Vec(nParal, UInt((log2Ceil(tbeDepth) + 1).W)))

  val idxReadValid = Wire(Bool())

  val allocLine = Module(new FindEmptyLine(tbeDepth, (log2Ceil(tbeDepth))))
  allocLine.io.data := TBEValid
  idxAlloc := allocLine.io.value

  val finder = for (i <- 0 until nParal + 1) yield {
    val Finder = Module(new Find(UInt(), UInt(addrLen.W), tbeDepth, (log2Ceil(tbeDepth))))
    Finder
  }

  finder(nParal).io.key := addrNoOffset(io.read.bits.addr)
  finder(nParal).io.data := TBEAddr
  finder(nParal).io.valid := TBEValid
  idxRead := finder(nParal).io.value.bits
  idxReadValid := (finder(nParal).io.value.bits === true.B)


  for (i <- 0 until nParal)  {
    finder(i).io.key := addrNoOffset(io.write(i).bits.addr)
    finder(i).io.data := TBEAddr
    finder(i).io.valid := TBEValid

    idxUpdate(i) := finder(i).io.value.bits
  }

//  when(isAlloc | isWrite) {
//    inputTBEReg := io.inputTBE
//    inputAddrReg := io.addr
//  }


//  for (i <- 0 until nParal) yield {
//    when(isAlloc(i)) {
//
//
//    }
//
//
//    }.elsewhen(isDealloc | isRead | isWrite) {
//    for (i <- 0 until tbeDepth) yield {
//      when(TBEAddr(i) === io.addr & TBEValid(i) === true.B) {
//        idx := i.asUInt()
//      }
//    }
//  }
//  for (i <- 0 until nParal) yield {

  for (i <- 0 until nParal)  {

    when ((isAlloc(i))){
      TBEMemory(idxAlloc) := io.write(i).bits.inputTBE
      TBEAddr(idxAlloc) := addrNoOffset(io.write(i).bits.addr)
      TBEValid(idxAlloc) := true.B
    }.elsewhen((isDealloc(i))){
      TBEValid(idxUpdate(i)) := false.B
      TBEMemory(idxUpdate(i)) := TBE.default
      TBEAddr(idxUpdate(i)) := 0.U
    }.elsewhen((isWrite(i))){
      when((io.write(i).bits.mask=== maskWay)) {
        TBEMemory(idxUpdate(i)).way := io.write(i).bits.inputTBE.way
      }.elsewhen((io.write(i).bits.mask === maskState)){
        TBEMemory(idxUpdate(i)).state := io.write(i).bits.inputTBE.state
      }.otherwise{
        TBEMemory(idxUpdate(i)) := io.write(i).bits.inputTBE
      }
  //    TBEValid(idxReg) := true.B
    }
  }

  io.outputTBE.valid := (idxReadValid & isRead)
  io.outputTBE.bits := Mux(idxReadValid , TBEMemory(idxRead), TBE.default)

  for (i <- 0 until nParal)  {

    isAlloc(i) := Mux(io.write(i).bits.command === alloc, true.B, false.B)
    isDealloc(i) := Mux(io.write(i).bits.command === dealloc, true.B, false.B)
    isWrite(i) := Mux(io.write(i).bits.command === write, true.B, false.B)
  }
  isRead := io.read.valid

}


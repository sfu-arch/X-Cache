package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum


class TBE (implicit p: Parameters)  extends AXIAccelBundle with HasCacheAccelParams {
  val state = new State()
//  val data = UInt (xlen.W)
  val way = UInt (wayLen.W)
//  val addr = UInt (addrLen.W)
//  val cmdPtr = UInt (nCom.W)

}
object TBE {

  def default (implicit p: Parameters): TBE = {
    val tbe = Wire(new TBE)
    tbe.state := State.default
//    tbe.data := 0.U
//    tbe.addr := 0.U
    tbe.way := 0.U
//    tbe.cmdPtr := 0.U
    tbe
  }
}

class TBETableIO (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {

  val addr = Input(UInt(xlen.W))
  val command = Input(UInt())
  val inputTBE= Input(new TBE)
  val outputTBE = Decoupled(new TBE)


}

class   TBETable(implicit  val p: Parameters) extends Module
  with HasAccelShellParams
  with HasAccelParams {


  val (idle:: alloc :: dealloc :: read :: Nil) = Enum(4)

  val io = IO(new TBETableIO())

  val TBEMemory = RegInit(VecInit(Seq.fill(tbeDepth)(TBE.default)))
  val TBEValid = RegInit(VecInit(Seq.fill(tbeDepth)(false.B)))
  val TBEAddr = RegInit(VecInit(Seq.fill(tbeDepth)((0.U))))


  val isAlloc = Wire(Bool())
  val isDealloc = Wire(Bool())
  val isRead = Wire(Bool())
//  val inc = Wire(Bool())


  //val (idx,full) = Counter (inc, tbetbeDepth)
  val idx = Wire(UInt((log2Ceil(tbeDepth) + 1).W))
  val idxReg = Reg(UInt((log2Ceil(tbeDepth) + 1).W))
  val idxValid = Wire(Bool())

  idxReg := idx
  idx := tbeDepth.U
  idxValid := !(idx === tbeDepth.U )

  when(isAlloc) {
    for (i <- 0 until tbeDepth) {
      when(TBEValid(i) === false.B) {
        idx := i.asUInt()
      }
    }

  }.elsewhen(isDealloc | isRead) {
    for (i <- 0 until tbeDepth) yield {
      when(TBEAddr(i) === io.addr & TBEValid(i) === true.B) {
        idx := i.asUInt()
      }
    }
  }


  when (RegNext(isAlloc)){
    TBEMemory(idxReg) := io.inputTBE
    TBEAddr(idxReg) := io.addr
    TBEValid(idxReg) := true.B
  }.elsewhen(RegNext(isDealloc)){
    TBEValid(idxReg) := false.B
  }

  io.outputTBE.valid := idxValid
  io.outputTBE.bits := Mux(idxValid , TBEMemory(idx), TBE.default)



  isAlloc := Mux( io.command === alloc, true.B, false.B)
  isDealloc :=  Mux(io.command === dealloc, true.B, false.B)
  isRead := Mux(io.command === read, true.B, false.B)


}
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

  val addr = Input(UInt(xlen.W))
  val command = Input(UInt())
  val mask = Input(UInt())
  val inputTBE= Input(new TBE)
  val outputTBE = Decoupled(new TBE)

}

class   TBETable(implicit  val p: Parameters) extends Module
  with HasAccelShellParams
  with HasAccelParams {


  val (idle:: alloc :: dealloc :: read :: write :: Nil) = Enum(5)
  val (maskAll :: maskState :: maskWay :: Nil) = Enum(3)

  val io = IO(new TBETableIO())

  val TBEMemory = RegInit(VecInit(Seq.fill(tbeDepth)(TBE.default)))
  val TBEValid = RegInit(VecInit(Seq.fill(tbeDepth)(false.B)))
  val TBEAddr = RegInit(VecInit(Seq.fill(tbeDepth)((0.U(accelParams.addrLen.W)))))


  val isAlloc = Wire(Bool())
  val isDealloc = Wire(Bool())
  val isRead = Wire(Bool())
  val isWrite = Wire(Bool())
//  val inc = Wire(Bool())


  //val (idx,full) = Counter (inc, tbetbeDepth)
  val idx = Wire(UInt((log2Ceil(tbeDepth) + 1).W))
  val idxReg = Reg(UInt((log2Ceil(tbeDepth) + 1).W))
  val idxValid = Wire(Bool())

  val inputTBEReg = Reg (new TBE)
  val inputAddrReg = Reg(UInt())
  idxReg := idx
  idx := tbeDepth.U
  idxValid := !(idx === tbeDepth.U )

  when(isAlloc | isWrite){
    inputTBEReg := io.inputTBE
    inputAddrReg := io.addr
  }

  when(isAlloc) {
    for (i <- 0 until tbeDepth) {
      when(TBEValid(i) === false.B) {
        idx := i.asUInt()
      }
    }

  }.elsewhen(isDealloc | isRead | isWrite) {
    for (i <- 0 until tbeDepth) yield {
      when(TBEAddr(i) === io.addr & TBEValid(i) === true.B) {
        idx := i.asUInt()
      }
    }
  }


  when (RegNext(isAlloc)){
    TBEMemory(idxReg) := inputTBEReg
    TBEAddr(idxReg) := inputAddrReg
    TBEValid(idxReg) := true.B
  }.elsewhen(RegNext(isDealloc)){
    TBEValid(idxReg) := false.B
    TBEMemory(idxReg) := TBE.default
    TBEAddr(idxReg) := 0.U
  }.elsewhen(RegNext(isWrite)){
    when(RegNext(io.mask === maskWay)) {
      TBEMemory(idxReg).way := inputTBEReg.way
    }.elsewhen(RegNext(io.mask === maskState)){
      TBEMemory(idxReg).state := inputTBEReg.state
    }.otherwise{
      TBEMemory(idxReg) := inputTBEReg
    }
//    TBEValid(idxReg) := true.B
  }

  io.outputTBE.valid := (idxValid & isRead)
  io.outputTBE.bits := Mux(idxValid , TBEMemory(idx), TBE.default)

  isAlloc := Mux( io.command === alloc, true.B, false.B)
  isDealloc :=  Mux(io.command === dealloc, true.B, false.B)
  isRead := Mux(io.command === read, true.B, false.B)
  isWrite := Mux(io.command === write, true.B, false.B)

}


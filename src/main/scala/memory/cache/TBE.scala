package memGen.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import memGen.config._
import chisel3.util._
import chisel3.util.Enum

class TBE (implicit p: Parameters)  extends AXIAccelBundle with HasCacheAccelParams {
  val state = new State()
  val way = UInt ((wayLen + 1).W)
  val fields = Vec(nTBEFields, UInt(TBEFieldWidth.W))

  val fieldLen = log2Ceil(nTBEFields + 1)
  val cmdLen = log2Ceil(nTBECmds)

}
object TBE {

  def default (implicit p: Parameters): TBE = {
    val tbe = Wire(new TBE)
    tbe.state := State.default
    tbe.way := tbe.nWays.U

    (0 until tbe.nTBEFields).map(i => tbe.fields(i) := 0.U(tbe.TBEFieldWidth.W))

    tbe
  }

}

class TBETableIO (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {


  val write = Vec( nParal, Flipped(Valid(new Bundle {
    val addr = (UInt(xlen.W))
    val command = (UInt(TBE.default.cmdLen.W))
    val mask = UInt(nTBEFields.W)
    val inputTBE= (new TBE)
  })))

  val read = Flipped(Valid(new Bundle {
    val addr = (UInt(xlen.W))
  }))

  val outputTBE = Decoupled(new TBE)
  val isFull = Output(Bool())
}

class   TBETable(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelParams {

  val maskFixed = nTBEFields

  val ( read :: alloc :: dealloc :: write :: Nil) = Enum(4)

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

  val counter = RegInit(0.U((log2Ceil(tbeDepth) + 2).W))

  val idxReadValid = Wire(Bool())

  val allocLine = Module(new FindEmptyLine(tbeDepth, (log2Ceil(tbeDepth))))
  allocLine.io.data := TBEValid
  idxAlloc := allocLine.io.value.bits

  val finder = for (i <- 0 until nParal + 1) yield {
    val Finder = Module(new Find(UInt(), UInt(addrLen.W), tbeDepth, (log2Ceil(tbeDepth))))
    Finder
  }

  io.isFull := ((counter > (tbeDepth.U - 3.U)) & isRead & !finder(nParal).io.value.valid) 

  finder(nParal).io.key := addrNoOffset(io.read.bits.addr)
  finder(nParal).io.data := TBEAddr
  finder(nParal).io.valid := TBEValid
  idxRead := finder(nParal).io.value.bits
  idxReadValid := (finder(nParal).io.value.valid && isRead)


  for (i <- 0 until nParal)  {
    finder(i).io.key := addrNoOffset(io.write(i).bits.addr)
    finder(i).io.data := TBEAddr
    finder(i).io.valid := TBEValid

    idxUpdate(i) := finder(i).io.value.bits
  }

  for (i <- 0 until nParal)  {  

    when ((isAlloc(i))){
      TBEMemory(idxAlloc) := io.write(i).bits.inputTBE
      TBEAddr(idxAlloc) := addrNoOffset(io.write(i).bits.addr)
      TBEValid(idxAlloc) := true.B
      counter := counter + 1.U
    }.elsewhen((isDealloc(i)) && finder(i).io.value.valid){
      TBEValid(idxUpdate(i)) := false.B
      TBEMemory(idxUpdate(i)) := TBE.default
      TBEAddr(idxUpdate(i)) := 0.U
      counter := counter - 1.U
    }.elsewhen((isWrite(i)) && finder(i).io.value.valid){
      when((io.write(i).bits.mask =/= maskFixed.U)) {
        TBEMemory(idxUpdate(i)).fields(io.write(i).bits.mask) := io.write(i).bits.inputTBE.fields(io.write(i).bits.mask)
        printf(p"TBE Field Check ${ TBEMemory(idxUpdate(i)).fields(io.write(i).bits.mask)}\n")
      }.otherwise{
        TBEMemory(idxUpdate(i)).way := io.write(i).bits.inputTBE.way
        TBEMemory(idxUpdate(i)).state := io.write(i).bits.inputTBE.state
      }
  //    TBEValid(idxReg) := true.B
    }
  }

  io.outputTBE.valid := (idxReadValid)
  io.outputTBE.bits := Mux(idxReadValid , TBEMemory(idxRead), TBE.default)

  for (i <- 0 until nParal)  {

    isAlloc(i) := io.write(i).bits.command === alloc && io.write(i).fire()
    isDealloc(i) := io.write(i).bits.command === dealloc && io.write(i).fire()
    isWrite(i) := io.write(i).bits.command === write  && io.write(i).fire()
  }
  isRead := io.read.valid
}


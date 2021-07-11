package memGen.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import chisel3.util.Arbiter
import memGen.interfaces._
import memGen.interfaces.axi._
import memGen.junctions._
import memGen.shell._

class MetaData(implicit p: Parameters) extends AXIAccelBundle with HasCacheAccelParams {
  val tag = UInt(taglen.W)
}

class State(implicit p: Parameters) extends AXIAccelBundle with HasCacheAccelParams {
  val state = UInt(stateLen.W)
}

object MetaData  {

  def apply(tag_len: Int)(implicit p: Parameters): MetaData = {
    val wire = Wire(new MetaData)
    wire.tag := tag_len.U
    wire
  }

  def apply(tag_len: UInt)(implicit p: Parameters): MetaData = {
    val wire = Wire(new MetaData)
    wire.tag := tag_len
    wire
  }

  def default(implicit p: Parameters): MetaData = {
    val wire = Wire(new MetaData)
    wire.tag := 0.U
    wire
  }
}

object State {

  def apply(state_len: Int)(implicit p: Parameters): State = {
    val wire = Wire(new State)
    wire.state := state_len.U
    wire
  }

  def default(implicit p: Parameters): State= {
    val wire = Wire(new State)
    wire.state := 0.U
    wire
  }
}

class Gem5Cache (val ID:Int = 0, val debug: Boolean = false)(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val biPassModule = Module(new bipassLD())

  val io = IO(new Bundle {
    val cpu =  Vec(nParal, new CacheCPUIO)
    val probe =  new ProbeUnitIO // last line for probing

    val bipassLD = (new Bundle() {
      val in = Flipped(Valid( new Bundle{
        val addr = (UInt(addrLen.W))
        val way = UInt((wayLen + 1).W)
      }))
      val out = Valid(biPassModule.io.out.bits.cloneType)
    })
  })


  val dataMemory = Module(new MemBank(UInt(xlen.W))(xlen, addrLen, nWords, nSets * nWays, wordLen))
  val metaMemory = Module(new MemBank(new MetaData())(xlen, setLen, nWays, nSets, wayLen))

  val cacheLogic = for (i <- 0 until nParal) yield {
    val CacheLogic = Module(new Gem5CacheLogic(ID = i))
    CacheLogic
  }

  val probeUnit = Module (new ProbeUnit)

  val validBits = Module(new paralReg(Bool(), nSets * nWays, nParal + 1, nRead = 1))
  val validTagBits = Module(new paralReg(Bool(), nSets * nWays, nParal + 1, nWays))
//  val dirtyBits = Module(new paralReg(Bool(), nSets * nWays, nParal + 1, nWays))

   val metaWrArb = Module (new Arbiter(cacheLogic(0).io.metaMem.write.bits.cloneType, n = nParal))


  biPassModule.io.in  <> io.bipassLD.in
  biPassModule.io.out <> io.bipassLD.out

  biPassModule.io.dataMem                <> dataMemory.io.read
  probeUnit.io.metaMem.read <> metaMemory.io.read
  probeUnit.io.dataMem.read <> DontCare // should be connected to bipassLD

  for (i <- 0 until nParal) {
    cacheLogic(i).io.dataMem.read <> DontCare
    cacheLogic(i).io.metaMem.read <> DontCare
    dataMemory.io.write           <> cacheLogic(i).io.dataMem.write
    metaWrArb.io.in(i).bits.bank          := cacheLogic(i).io.metaMem.write.bits.bank
    metaWrArb.io.in(i).bits.address       := cacheLogic(i).io.metaMem.write.bits.address
    metaWrArb.io.in(i).bits.inputValue    := cacheLogic(i).io.metaMem.write.bits.inputValue
    metaWrArb.io.in(i).valid    := cacheLogic(i).io.metaMem.write.valid


  }

  metaMemory.io.write.bits.bank    := metaWrArb.io.out.bits.bank 
  metaMemory.io.write.bits.address    := metaWrArb.io.out.bits.address   
  metaMemory.io.write.bits.inputValue := metaWrArb.io.out.bits.inputValue
  metaMemory.io.write.valid := metaWrArb.io.out.valid
  metaWrArb.io.out.ready := true.B
    //    metaMemory.io.inputValue <> cacheLogic(i).io.metaMem.outputValue
//    cacheLogic(i).io.metaMem.bank <> metaMemory.io.bank
//    cacheLogic(i).io.metaMem.address <> metaMemory.io.address
//    cacheLogic(i).io.metaMem.isRead <> metaMemory.io.isRead
//    dataMemory.io.valid := cacheLogic(i).io.dataMem.valid
//    metaMemory.io.valid := cacheLogic(i).io.metaMem.valid
  for (i <- 0 until nParal) {

    cacheLogic(i).io.validBits <> validBits.io.port(i)
    cacheLogic(i).io.validTagBits <> validTagBits.io.port(i)
    this.io.cpu(i) <> cacheLogic(i).io.cpu
  }

  probeUnit.io.validBits <> validBits.io.port(nParal)
  probeUnit.io.validTagBits <> validTagBits.io.port(nParal)
  this.io.probe <> probeUnit.io.cpu

}

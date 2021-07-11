package memGen.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.util._
import memGen.interfaces._
import chisel3.util.experimental._
import memGen.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile

import memGen.junctions._
import memGen.shell._



class ProbeUnitIO(implicit p: Parameters) extends DandelionGenericParameterizedBundle(p) {

  val req = Flipped(Decoupled(new MemReq))
  val resp = (Valid(new MemResp))
}

// @todo bipass for reading
class ProbeUnit(val ID:Int = 0)(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val io = IO(new Bundle {

    val cpu = new ProbeUnitIO

    val metaMem =  Flipped (new MemBankIO(new MetaData())(dataLen = xlen, addrLen = setLen, banks = nWays, bankDepth = nSets, bankLen = wayLen))
    val dataMem =  Flipped (new MemBankIO(UInt(xlen.W)) (dataLen = xlen, addrLen= addrLen, banks =nWords, bankDepth= nSets * nWays, bankLen = nWords))

    val validBits = new RegIO(Bool(), nRead = 1)
    val validTagBits = new RegIO(Bool(), nWays)

  })
  val decoder = Module(new Decoder(nSigs))
  val cacheID = WireInit(ID.U(8.W))

  io.cpu <> DontCare
  io.metaMem <> DontCare
  io.dataMem <> DontCare

  val Axi_param = memParams

  // cache states

  val (sigLoadWays :: sigFindInSet ::sigAddrToWay :: sigPrepMDRead ::sigPrepMDWrite:: sigAllocate :: sigDeallocate :: sigWrite :: sigRead :: sigDataReq:: Nil) = Enum(nSigs)

  val addr_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  val cpu_data = RegInit(0.U(io.cpu.req.bits.data.getWidth.W))
  val cpu_mask = RegInit(0.U(io.cpu.req.bits.mask.getWidth.W))
  val cpu_command = RegInit(0.U(io.cpu.req.bits.command.getWidth.W))

  val tag = RegInit(0.U(taglen.W))
  val set = RegInit(0.U(setLen.W))
  val wayInput = RegInit(nWays.U((wayLen + 1).W))
  val replaceWayInput = RegInit(nWays.U((wayLen + 1).W))

  val way = WireInit(0.U((wayLen + 1).W))

  val dataBuffer = RegInit(VecInit(Seq.fill(nData)(0.U(xlen.W))))

  val findInSetSig = Wire(Bool())
  val addrToLocSig = Wire(Bool())
  val dataValidCheckSig = Wire(Bool())

  val start = WireInit(VecInit(Seq.fill(nCom)(false.B)))
  val res = WireInit(VecInit(Seq.fill(nCom)(false.B)))
  val done = WireInit(false.B)
  val dataValid = WireInit(false.B)

  // Counters
  require(nData > 0)

  val signals = Wire(Vec(nSigs, Bool()))

  val loadWaysMeta = Wire(Bool())
  val loadLineData = Wire(Bool())
  val loadDataBuffer = Wire(Bool())

  val waysInASet = Reg(Vec(nWays, new MetaData()))

  val addrReadValid = Wire(Bool())
  val dataReadReady = Wire(Bool())

  val addrWriteValid = Wire(Bool())
  val dataWriteValid = Wire(Bool())

  val wayInvalid = Wire(Bool())


  val readMetaData = Wire(Bool())
  val targetWayReg = RegInit(nWays.U((wayLen + 1).W))
  val targetWayWire = WireInit(nWays.U((wayLen + 1).W))

  val hit = Wire(Bool())

  /********************************************************************************/
  // Signals
  val allocate = WireInit(signals(sigAllocate))
  val prepMDRead = WireInit(signals(sigPrepMDRead))
  val prepMDWrite = WireInit(signals(sigPrepMDWrite))
  val deallocate = WireInit(signals(sigDeallocate))
  val addrToWaySig = WireInit(signals(sigAddrToWay))
  val writeSig = WireInit(signals(sigWrite))
  val readSig = WireInit(signals(sigRead))
  val dataReq = WireInit(signals(sigDataReq))

  loadWaysMeta := signals(sigLoadWays)
  findInSetSig := signals(sigFindInSet)
  /********************************************************************************/

  /********************************************************************************/
  // Latching
  when(io.cpu.req.fire()) {
    addr_reg := io.cpu.req.bits.addr
    cpu_data := io.cpu.req.bits.data
    cpu_mask := io.cpu.req.bits.mask
    cpu_command := io.cpu.req.bits.command
    tag := addrToTag(io.cpu.req.bits.addr)
    set := addrToSet(io.cpu.req.bits.addr)
    wayInput := io.cpu.req.bits.way
    replaceWayInput := io.cpu.req.bits.replaceWay
  }

  dataValid := io.validBits.read.out.asUInt()
  io.validBits.read.in.bits.addr := set * nSets.U + way
  io.validBits.read.in.valid := dataValidCheckSig

  /********************************************************************************/



  /********************************************************************************/
  //Decoder
  decoder.io.inAction := cpu_command
  signals := decoder.io.outSignals
  /********************************************************************************/

  io.cpu.req.ready := true.B

  loadLineData := false.B
  addrToLocSig := false.B
  dataValidCheckSig := false.B

  addrWriteValid := false.B
  dataWriteValid := false.B
  addrReadValid := false.B
  dataReadReady := false.B
  loadDataBuffer := false.B

  def prepForRead[T <: Data](D: MemBankIO[T]): Unit = {
    D.read.in.valid := true.B

    D match {
      case io.dataMem => {
        D.read.in.bits.address := set * nSets.U + way
        D.read.in.bits.bank := 0.U
      }
      case io.metaMem => {
        D.read.in.bits.bank := 0.U
        D.read.in.bits.address := set
      }
    }
  }

  def prepForWrite[T <: Data](D: MemBankIO[T]): Unit = {
    D.write.valid := true.B

    D match {
      case io.dataMem => {
        D.write.bits.address := set * nSets.U + way
        D.write.bits.bank := 1.U << nWords.U - 1.U
      }
      case io.metaMem => {
        D.write.bits.bank := 1.U << way
        D.write.bits.address := set
      }
    }
  }

  val MD = Wire(new MetaData())
  MD.tag := tag
  io.metaMem.write.valid := false.B
  io.dataMem.write.valid := false.B
  io.metaMem.read.in.valid := false.B
  io.dataMem.read.in.valid := false.B

  wayInvalid := (wayInput === nWays.U)

  val (missCount, _) = Counter( !hit, 1000)
  val (hitCount,  _) = Counter( hit , 1000)
  hit := (readSig & !wayInvalid)

  readMetaData := !(sigAllocate | sigDeallocate)

  val emptyLine = Module(new FindEmptyLine(nWays, addrLen))
  emptyLine.io.data := io.validTagBits.read.out

  val tagFinder = Module(new Find(new MetaData(), new MetaData(),nWays, addrLen))
  tagFinder.io.key := MD
  tagFinder.io.data := io.metaMem.read.outputValue
  tagFinder.io.valid := io.validTagBits.read.out

  when(addrToWaySig | (findInSetSig & loadWaysMeta)){
    io.validTagBits.read.in.bits.addr := set * nWays.U
  }.otherwise{
    io.validTagBits.read.in.bits.addr := 0.U
  }

  io.validTagBits.read.in.valid := addrToWaySig  | (findInSetSig & loadWaysMeta) // probing and allocating


  when (addrToWaySig) {
    way := replaceWayInput
  }.elsewhen(!wayInvalid){
    way := wayInput
  }.otherwise{
    way := nWays.U
  }

  when(prepMDRead){
    prepForRead(io.metaMem)
  }.elsewhen(prepMDWrite){
    prepForWrite(io.metaMem)
  }

  when(writeSig){
    prepForWrite(io.dataMem)
  }.elsewhen(readSig){
    prepForRead(io.dataMem)
  }

  when(writeSig){
    io.dataMem.write.bits.inputValue := cpu_data.asTypeOf(Vec(nWords, UInt(xlen.W)))
  }.otherwise{
    io.dataMem.write.bits.inputValue := DontCare
  }

  io.validBits.write.valid := writeSig
  io.validBits.write.bits.addr := (set * nWays.U + way)
  io.validBits.write.bits.value := writeSig
//
//  when(writeSig){
//    io.validBits.write.bits.value := true.B
//  }

  when (readSig){
    dataBuffer := io.dataMem.read.outputValue
  }.otherwise{
    dataBuffer := dataBuffer
  }
  io.metaMem.write.bits.inputValue:= DontCare

  when(prepMDWrite){
      io.metaMem.write.bits.inputValue(way) := MD
  }.otherwise{
      io.metaMem.write.bits.inputValue := DontCare
  }

  when((findInSetSig & loadWaysMeta)) { // probing way
    targetWayWire := Mux(tagFinder.io.value.valid, tagFinder.io.value.bits, nWays.U)
  }.elsewhen(addrToWaySig) { // allocate
    targetWayWire := way
  }.otherwise{
    targetWayReg := targetWayReg // @todo no completed
  }
  targetWayReg := targetWayWire

//  when(allocate | deallocate){
//    io.metaMem.isRead := false.B
//  }.otherwise{
//    io.metaMem.isRead := true.B
//  }

  when(loadWaysMeta){
    waysInASet := io.metaMem.read.outputValue
  }.otherwise{
    waysInASet := waysInASet
  }

  io.validTagBits.write.bits.addr := (set * nWays.U + way)
  io.validTagBits.write.valid := allocate | deallocate
  io.validTagBits.write.bits.value := allocate | !deallocate
//  when(allocate ) {
//    validTagBits := true.B
//  }.elsewhen(deallocate){
//    validTagBits(set * nWays.U + way) := false.B
//  }

  io.cpu.resp.bits.way := targetWayWire
  io.cpu.resp.bits.data := Cat(dataBuffer)
  io.cpu.resp.bits.iswrite := RegNext(readSig)
  io.cpu.resp.valid    := addrToWaySig | (findInSetSig & loadWaysMeta) | RegNext(readSig) // @todo should be changed to sth more generic

  when(true.B){

    when(addrToWaySig && !emptyLine.io.value.valid ){
      printf(p"Replacement in Set: ${set}, Way: ${way}, Addr: ${addr_reg}\n")
    }

  }
  val boreWire = WireInit((addrToWaySig && !emptyLine.io.value.valid))
      // BoringUtils.addSource(boreWire, "numRepl")

    // when(cacheID =/= nParal.U && io.cpu.req.fire()){
    //   printf(p"Req for Cache_Node: ${cacheID} in Set: ${set}, Way: ${way}, Addr: ${addr_reg}\n")
    // }

}

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

trait HasCacheAccelParams extends HasAccelParams with HasAccelShellParams {

  // val (memType :: cacheType :: Nil) = Enum(2)
  //Caused Weird error!!

  val Events = accelParams.Events
  val States = accelParams.States

  val nWays = accelParams.nways
  val nSets = accelParams.nsets
  val nCache = accelParams.nCache


  val tbeDepth = accelParams.tbeDepth
  val lockDepth = accelParams.lockDepth

  val nTBEFields = 3
  val nTBECmds = 4
  val TBEFieldWidth = 32

  override val addrLen = accelParams.addrlen
  val dataLen = accelParams.dataLen
  val eventLen = Events.eventLen
  val stateLen = States.stateLen

  //  val nStates = States.stateLen
  val nCom = 8
  val nParal = accelParams.nParal
  val pcLen = 16

  val wBytes = xlen >> 3
  val nWords = accelParams.nWords // 4

  val bBytes = accelParams.cacheBlockBytes // 32 Bytes (  nWords * Xlen)
  val bBits = bSize
  val blen = log2Ceil(nWords) // 2

  val byteOffsetBits = log2Ceil(wBytes) //
  val wordLen = byteOffsetBits
  
  val nData = bBits / memParams.dataBits
  val dataBeats = nData

  val setLen = log2Ceil(nSets)
  val wayLen = log2Ceil(nWays) + 1
  val cacheLen = log2Ceil(nSets*nWays)

  val taglen = addrLen - (setLen + blen + byteOffsetBits ) // 3 + 2

  val replacementPolicy = "lru"

  val actionTypeLen = ActionList.typeLen
  val nSigs = ActionList.actionLen - actionTypeLen
  val actionLen = ActionList.actionLen

  val opcodeWidth :Int = 0
  val funcWidth: Int = 3
  val regFileSize: Int = 4
  val regFileLen = log2Ceil(regFileSize)
  val instructionWidth: Int = nSigs


  def addrToSet(addr: UInt): UInt = {
    val set = addr(setLen + blen + byteOffsetBits - 1, blen + byteOffsetBits )
    set.asUInt()
  }

  def addrNoOffset (addr: UInt) :UInt  ={
    val addrNoOff = addr >> (blen + byteOffsetBits)
    addrNoOff.asUInt()
  }

  def sigToAction(sigs : Bits) :UInt = sigs.asUInt()(nSigs - 1, 0)
  def sigToActType(sigs : Bits) :UInt = sigs.asUInt()(nSigs + actionTypeLen - 1, nSigs)

  def sigToTBEOp2(sigs : Bits) : UInt = sigs.asUInt()(2*TBE.default.fieldLen + TBE.default.cmdLen - 1 , TBE.default.fieldLen + TBE.default.cmdLen)
  def sigToTBEOp1(sigs : Bits) : UInt = sigs.asUInt()(TBE.default.fieldLen + TBE.default.cmdLen  - 1, TBE.default.cmdLen)
  def sigToTBECmd(sigs : Bits) : UInt = sigs.asUInt()(TBE.default.cmdLen - 1, 0)

  def sigToState (sigs :Bits) : UInt = sigs.asUInt()(States.stateLen - 1, 0)

  def sigToCompOpSel1(sigs:Bits): UInt = sigs.asUInt()(0,0)
  def sigToCompOpSel2(sigs:Bits): UInt = sigs.asUInt()(2,1)

  def sigToDQSrc(sigs:Bits): UInt = sigs.asUInt()(0,0)
  def sigToDQRegSrc(sigs:Bits): UInt = sigs.asUInt()(regFileLen,1)

}

class CacheCPUIO(implicit p: Parameters) extends DandelionGenericParameterizedBundle(p) {
  val abort = Input(Bool())
  val flush = Input(Bool())
  val flush_done = Output(Bool())
  val req = Flipped(Decoupled(new MemReq))
  val resp = (Valid(new MemResp))
}

// @todo bipass for reading
class Gem5CacheLogic(val ID:Int = 0)(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val io = IO(new Bundle {

    val cpu = new CacheCPUIO

    val metaMem =  Flipped (new MemBankIO(new MetaData())(dataLen = xlen, addrLen = setLen, banks = nWays, bankDepth = nSets, bankLen = wayLen))
    val dataMem =  Flipped (new MemBankIO(UInt(xlen.W)) (dataLen = xlen, addrLen= addrLen, banks =nWords, bankDepth= nSets * nWays, bankLen = nWords))

    val validBits = new RegIO(Bool(), nRead = 1)
    val validTagBits = new RegIO(Bool(), nWays)
//    val dirtyBits = new RegIO(Bool(), nWays)

  })
  val decoder = Module(new Decoder)
  val cacheID = WireInit(ID.U(8.W))

  io.cpu <> DontCare
  io.metaMem <> DontCare
  io.dataMem <> DontCare

  val Axi_param = memParams

  // cache states

  val (sigLoadWays :: sigFindInSet ::sigAddrToWay :: sigPrepMDRead ::sigPrepMDWrite:: sigAllocate :: sigDeallocate :: sigWrite :: sigRead :: sigDataReq:: Nil) = Enum(nSigs)

  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK :: s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val st = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)

  val cNone :: cProbe :: cAlloc :: cDeAlloc :: cIntRead :: cExtRead :: cSetState :: cIntWrite :: Nil = Enum(nCom)

  val (stIntRdIdle :: stIntRdLookMeta :: stIntRdPassData :: Nil) = Enum(3)
  val stIntRdReg = RegInit(stIntRdIdle)

  val (stIntWrIdle :: stIntWrAddr :: stIntWrData :: Nil) = Enum(3)
  val stIntWrReg = RegInit(stIntWrIdle)

  //Flush Logic
  val dirty_block = nSets

  val addr_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  val cpu_data = RegInit(0.U(io.cpu.req.bits.data.getWidth.W))
  val cpu_mask = RegInit(0.U(io.cpu.req.bits.mask.getWidth.W))
  val cpu_iswrite = RegInit(0.U(io.cpu.req.bits.iswrite.getWidth.W))
  val cpu_command = RegInit(0.U(io.cpu.req.bits.command.getWidth.W))
  val count_set = RegInit(false.B)

  val tag = RegInit(0.U(taglen.W))
  val set = RegInit(0.U(setLen.W))
  val wayInput = RegInit(nWays.U((wayLen + 1).W))
  val replaceWayInput = RegInit(nWays.U((wayLen + 1).W))

  val offset = RegInit(0.U(byteOffsetBits.W))
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
  //  val loadFromMemSig = Wire(Bool())
  val loadDataBuffer = Wire(Bool())

  val waysInASet = Reg(Vec(nWays, new MetaData()))
//  val cacheLine = Reg(Vec(nWords, UInt(xlen.W)))

  val addrReadValid = Wire(Bool())
  val dataReadReady = Wire(Bool())

  val addrWriteValid = Wire(Bool())
  val dataWriteValid = Wire(Bool())

  val wayInvalid = Wire(Bool())

  val is_idle = st === s_IDLE
  val is_read = st === s_READ_CACHE
  val is_write = st === s_WRITE_CACHE
  val is_alloc = st === s_REFILL //&& read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)

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
    cpu_iswrite := io.cpu.req.bits.iswrite
    tag := addrToTag(io.cpu.req.bits.addr)
    set := addrToSet(io.cpu.req.bits.addr)
    offset := addrToOffset(io.cpu.req.bits.addr)
    wayInput := io.cpu.req.bits.way
    replaceWayInput := io.cpu.req.bits.replaceWay
  }
//
//  when(loadLineData) {
//    cacheLine := io.dataMem.read.outputValue
//  }

//  when(dataValidCheckSig) {
//    dataValid := validBits(set * nSets.U + way)
//  }

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


  //  val (block_count, block_wrap) = Counter(flush_state === s_flush_BLOCK, nWords)
  //  val (way_count, way_wrap) = Counter(flush_state === s_flush_START, nWays)
  //  val (set_count, set_wrap) = Counter(flush_state === s_flush_START && way_wrap , nSets)
  //  val block_addr_tag_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  //  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
  //  val block_rmeta = RegInit(init = MetaData.default)
  //  val flush_mode = RegInit(false.B)


  //  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
  //  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
  //  val ren_reg = RegNext(ren)
  //  val rmeta = RegNext(next = metaMem.read(addrToSet(io.cpu.req.bits.addr))(0), init = MetaData.default)
  //  val rdata = RegNext(next = cache_block, init = 0.U(cache_block_size.W))
  //  val rdata_buf = RegEnable(rdata, ren_reg)
  //  val refill_buf = RegInit(VecInit(Seq.fill(nData)(0.U(Axi_param.dataBits.W))))
  //  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))
  //  hit := (valid(set) & rmeta.tag === tag)

  def addrToOffset(addr: UInt): UInt = {
    val offset = addr(blen + byteOffsetBits - 1, byteOffsetBits)
    offset.asUInt()
  }

  def addrToTag(addr: UInt): UInt = {
    val tag = Wire(UInt(taglen.W))
    tag := (addr(addrLen - 1, setLen + blen + byteOffsetBits))
    tag.asUInt()
  }

//  def addrToWay(set: UInt): (UInt) = {
//    val way = Wire(UInt((nWays + 1).W))
//    way := nWays.U
//    for (i <- 0 until nWays) {
//      when(validTag(set * nWays.U + i.U) === false.B) {
//        way := i.asUInt()
//      }
//    }
//    (way.asUInt())
//  }


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
//        D.write.bits.bank :=
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
  // when(io.in)
  // printf(p"way ${way}\n")

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

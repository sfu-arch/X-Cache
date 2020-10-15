package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.axi._
import dandelion.junctions._
import dandelion.shell._


trait HasCacheAccelParams extends HasAccelParams with HasAccelShellParams {

  val nWays = accelParams.nways
  val nSets = accelParams.nsets
  val nStates = accelParams.nstates
  val addrLen = accelParams.addrlen
  val nCom = accelParams.nCommand

  val stateLen = log2Ceil(nStates)
  val wBytes = xlen / 8
  val bBytes = accelParams.cacheBlockBytes
  val bBits = bBytes << 3
  val blen = log2Ceil(bBytes)
  val nData = bBits / memParams.dataBits
  val dataBeats = nData

  val setLen = log2Ceil(nSets)
  val wayLen = log2Ceil(nWays)

  val nWords = bBits / xlen
  val wordLen = log2Ceil(nWords)
  //val taglen = xlen - (setLen + blen)
  val taglen = addrLen - (setLen + blen + wBytes )

  val byteOffsetBits = log2Ceil(wBytes)
  //

}

class CacheCPUIO(implicit p: Parameters) extends DandelionGenericParameterizedBundle(p) {
  val abort = Input(Bool())
  val flush = Input(Bool())
  val flush_done = Output(Bool())
  val req = Flipped(Decoupled(new MemReq))
  val resp = Output(Valid(new MemResp))
}

class CacheBankedMemIO[T <: Data](D: T, nInput :Int) (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {


  val bank = Output(UInt(wayLen.W))
  val address = Output(UInt(setLen.W))
  val isRead = Output(Bool())
  val outputValue = Output(D.cloneType)
  val inputValue = Input(Vec(nInput, D.cloneType))
  val valid = Input(Bool())
}

class StateMemIO() (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {

  val isSet = Output(Bool())
  val stateIn = Input(new State)
  val stateOut = Output(new State)
  val addr = Output(UInt(addrLen.W))

}
class Gem5CacheLogic(val ID:Int = 0)(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val io = IO(new Bundle {
    //@todo ports for state and metadata -- Done
    val cpu = new CacheCPUIO
    val mem = new AXIMaster(memParams)
    val dataMem = new CacheBankedMemIO(UInt(xlen.W), nWords)
    val metaMem = new CacheBankedMemIO(new MetaData(), nWays)
    val stateMem = new StateMemIO()

  })

  io.cpu <> DontCare
  io.mem <> DontCare
  io.metaMem <> DontCare
  io.dataMem <> DontCare
  io.stateMem <> DontCare

  val Axi_param = memParams

  // cache states
  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK :: s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val st = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)

  //Flush Logic
  val dirty_block = nSets

  // memory
  val valid = VecInit(Seq.fill(nSets)( 0.U(nWays.W)))
  val validTag =VecInit(Seq.fill(nSets)( 0.U(nWays.W)))
  val dirty = VecInit(Seq.fill(nSets)( 0.U(nWays.W)))


//  //  val metaMem =  Wire(Vec(nWays, (Vec(nSets, (new MetaData)))))
//  //  val metaMem = VecInit(Seq.fill(nWays) (Mem((nSets),  new MetaData())))
//  val metaMem= Mem( nSets, (Vec(nWays, (new MetaData))))
//  val dataMem = Seq.fill(nWords) {
//    Mem(nSets * nWays, Vec(wBytes, UInt(8.W)))
//  }


  def addrToSet(addr: UInt): UInt = {
    val set = addr(setLen + blen + wBytes, blen + wBytes + 1)
    set.asUInt()
  }

  def addrToOffset(addr: UInt): UInt = {
    val offset = addr(blen - 1, byteOffsetBits)
    offset.asUInt()
  }

  def addrToTag(addr: UInt): UInt = {
    val tag = Wire(UInt(taglen.W))
    tag := (addr(addrLen - 1, setLen + blen + wBytes + 1))
    tag.asUInt()
  }

  def addrToLoc(set: UInt): (UInt) = {
    //    val selFlag = Wire(Bool())
    //    selFlag := false.B
    val way = Wire(UInt(nWays.W))
    way := nWays.U
    for (i <- 0 until nWays) {
      when(validTag(set)(i,i) === 0.U(1.W)) {
        way := i.asUInt()
      }
    }
    (way.asUInt())
  }

  def tagValidation(set: UInt, way: UInt): Bool={
    validTag(set).bitSet(way, true.B)
    true.B
  }

  def tagInvalidation(set: UInt, way: UInt): Bool= {
    validTag(set).bitSet(way, false.B)
    true.B
  }


  val addr_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  val cpu_data = RegInit(0.U(io.cpu.req.bits.data.getWidth.W))
  val cpu_mask = RegInit(0.U(io.cpu.req.bits.mask.getWidth.W))
  val cpu_tag = RegInit(0.U(io.cpu.req.bits.tag.getWidth.W))
  val cpu_iswrite = RegInit(0.U(io.cpu.req.bits.iswrite.getWidth.W))
  val cpu_command = RegInit(0.U(io.cpu.req.bits.command.getWidth.W))
  val count_set = RegInit(false.B)

  val tag = RegInit(0.U(taglen.W))
  val set = RegInit(0.U(setLen.W))
  val offset = RegInit(0.U(byteOffsetBits.W))
  val way = RegInit(0.U((nWays+1).W))
  val state = RegInit(State.default)

  val findInSetSig = Wire(Bool())
  val addrToLocSig = Wire (Bool())


  when(io.cpu.req.fire()) {
    addr_reg := io.cpu.req.bits.addr
    cpu_command := io.cpu.req.bits.command
    cpu_tag := io.cpu.req.bits.tag
    cpu_data := io.cpu.req.bits.data
    cpu_mask := io.cpu.req.bits.mask
    cpu_iswrite := io.cpu.req.bits.iswrite
    tag := addrToTag(io.cpu.req.bits.addr)
    set := addrToSet(io.cpu.req.bits.addr)
    offset := addrToOffset(io.cpu.req.bits.addr)
  }

  val loadWaysMeta = Wire(Bool())
  val loadLineData = Wire(Bool())
  val loadState = Wire (Bool())

  val waysInASet = Reg(Vec(nWays, new MetaData()))
  val cacheLine = Reg(Vec(nWords, UInt(xlen.W)))

  when (findInSetSig){
    way := findInSet(set,tag)
  }.elsewhen(!findInSetSig & addrToLocSig){
    way := addrToLoc(set)
  }

//  val cache_block_size = bBits
  when (loadWaysMeta){
    waysInASet := io.metaMem.inputValue
  }

  when (loadLineData){
    cacheLine := io.dataMem.inputValue
  }

  when (loadState){
    state := io.stateMem.stateIn
  }

  // Counters
  require(nData > 0)
  val (read_count, read_wrap_out) = Counter(io.mem.r.fire(), nData)
  val (write_count, write_wrap_out) = Counter(io.mem.w.fire(), nData)
  //  val (block_count, block_wrap) = Counter(flush_state === s_flush_BLOCK, nWords)
//  val (way_count, way_wrap) = Counter(flush_state === s_flush_START, nWays)
//  val (set_count, set_wrap) = Counter(flush_state === s_flush_START && way_wrap , nSets)


  //val block_addr_tag_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
//  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
//  val block_rmeta = RegInit(init = MetaData.default)
//  val flush_mode = RegInit(false.B)

  val is_idle = st === s_IDLE
  val is_read = st === s_READ_CACHE
  val is_write = st === s_WRITE_CACHE
  val is_alloc = st === s_REFILL && read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)

//  val hit = Wire(Bool())
//  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
//  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
//  val ren_reg = RegNext(ren)

//  val rmeta = RegNext(next = metaMem.read(addrToSet(io.cpu.req.bits.addr))(0), init = MetaData.default)
//  val rdata = RegNext(next = cache_block, init = 0.U(cache_block_size.W))
//  val rdata_buf = RegEnable(rdata, ren_reg)
//  val refill_buf = RegInit(VecInit(Seq.fill(nData)(0.U(Axi_param.dataBits.W))))
//  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))

//  hit := (valid(set) & rmeta.tag === tag)

  io.cpu.req.ready := true.B
  loadWaysMeta := false.B
  loadLineData := false.B
  findInSetSig := false.B
  addrToLocSig := false.B
  loadState := false.B


  def prepForRead[T <: Data] (D: CacheBankedMemIO[T]): Unit ={
    D.isRead := true.B

    D match {
      case io.dataMem =>{
        loadLineData := true.B
        D.address := set*nSets.U + way
        D.bank := 0.U
      }
      case io.metaMem => {
        loadWaysMeta := true.B
        D.bank := 0.U
        D.address := set
      }
    }
  }


  def findInSet(set: UInt, tag: UInt): UInt={
    val MD = Wire(new MetaData())
    MD.tag := tag
    val wayWire = Wire(UInt((nWays+1).W))
    wayWire := nWays.U
    prepForRead(io.metaMem)
//    loadWaysMeta := true.B
//    io.metaMem.bank := 0.U
//    io.metaMem.address := set
//    io.metaMem.isRead := true.B
    for (i <- 0 until nWays) yield {
      when(validTag(set)(i, i).asUInt() === 1.U && waysInASet(i).tag.asUInt() === MD.tag) {
        wayWire := i.asUInt()
      }
    }
    wayWire.asUInt()
  }
  def tagging(tag: UInt, set: UInt, way: UInt):Bool= {
    val MD = Wire(new MetaData())
    MD.tag := tag
    io.metaMem.bank := (way)
    io.metaMem.address := (set)
    io.metaMem.isRead := false.B
    io.metaMem.outputValue := MD
    tagValidation(set, way)
  }

  def detaggin(set: UInt, way: UInt): Bool= {
    tagInvalidation(set, way)
  }

  def allocate(set: UInt, tag: UInt): Bool = {
    val res = Wire(Bool())
    when(way === nWays.U) { //means error in finding a way
      res := false.B
      addrToLocSig := false.B
    }.otherwise {
      tagging(tag, set, way)
      res := true.B
      addrToLocSig := true.B
    }
    res.asBool()
  }

  def deallocate(set: UInt, tag:UInt): Bool= {
    val res = Wire(Bool())
    findInSetSig := true.B
      when(way === nWays.U) {
        res :=false.B
      }.otherwise {
        res := detaggin(set, way)
      }
    res.asBool()
  }



  //  def Probing (addr:UInt): (UInt,UInt) ={
  //    val set = addrToSet(addr)
  //    val way = rplPolicy (set)
  //    (set, way)
  //  }
  def ReadInternal(set: UInt, way: UInt): Unit= {
    findInSetSig := true.B
    prepForRead(io.dataMem)
//    io.dataMem.address := set*nSets.U + way
//    io.dataMem.bank := 0.U
//    io.dataMem.isRead := true.B
  }

  def SetState (state: State): Unit = {
    val addr = Wire(UInt(addrLen.W))
    addr := set*nSets.U + way
    io.stateMem.addr:= addr
    io.stateMem.stateOut := state
    io.stateMem.isSet := true.B
  }

  def GetState (): Unit ={
    val addr = Wire(UInt(addrLen.W))
    addr := set*nSets.U + way
    io.stateMem.addr := addr
    io.stateMem.addr := false.B
    loadState := true.B
  }






  val cNone :: cAlloc :: cDealloc :: cGetState :: cSetState :: cReadInternal :: cWrite::Nil = Enum(nCom)
//
  io.cpu.resp.valid := false.B
//
  switch(cpu_command) {

    is(cAlloc) {
      val res = allocate(set, tag)
      io.cpu.resp.valid := res
    }
    is(cDealloc){
      val res = deallocate(set, tag)
      io.cpu.resp.valid := res
    }
    is (cReadInternal){
      st := s_READ_CACHE
      switch(st){
        is (s_READ_CACHE){
          ReadInternal (set, way)
          when(io.dataMem.valid){
             loadLineData := true.B
             io.cpu.resp.valid := true.B
             st := is_idle
          }.otherwise{
             st := s_READ_CACHE
          }
      }
      }
    }

    is ()

//    is (cWrite){
//      val res = W
//    }


  }
//
//
//
//  // Read Mux
//  io.cpu.resp.bits.data := VecInit.tabulate(nWords)(i => read((i + 1) * xlen - 1, i * xlen))(offset)
//  io.cpu.resp.bits.tag := cpu_tag
//  io.cpu.resp.bits.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
//  io.cpu.resp.bits.iswrite := cpu_iswrite
//
//  //Extra input needs to be removed
//  //Extra input needs to be remove
//  io.cpu.resp.bits.tile := 0.U
//
//  // Can be: 1)Write hit, 2)Read hit, 3)Read miss
//  //  io.cpu.resp.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
//  //  io.cpu.req.ready := is_idle || (state === s_READ_CACHE && hit)
//
//
//
//  val wmeta = MetaData(tag)
//
//  //   is_alloc means cache hit, hence, if is_alloc is false, it means our mask should consider only the modified byte
//  //   and since wdata is masked with wmask we only duplicate the write data, otherwise, since mask doesn't do anything
//  //   with write data we make the proper wdata using refill_buf
//  val wmask = Mux(!is_alloc, (cpu_mask << Cat(offset, 0.U(byteOffsetBits.W))).asUInt.zext, (-1).asSInt()).asUInt()
//  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
//    if (refill_buf.size == 1) io.mem.r.bits.data
//    else Cat(io.mem.r.bits.data, Cat(refill_buf.init.reverse)))
//
//
//  when(wen) {
//    //      valid := valid.bitSet(set_reg, true.B)
//    //      dirty := dirty.bitSet(set_reg, !is_alloc)
//
//    //        dirty_mem(dirty_block) := dirty_mem(dirty_block).bitSet(dirty_offset, !is_alloc)
//
//    //    when(is_alloc) {
//    //      metaMem.write(set, wmeta)
//    //    }
//    dataMem.zipWithIndex foreach { case (mem, i) =>
//      val data = VecInit.tabulate(wBytes)(k => wdata(i * xlen + (k + 1) * 8 - 1, i * xlen + k * 8))
//      mem.write(set, data, (wmask((i + 1) * wBytes - 1, i * wBytes)).asBools)
//      mem suggestName s"dataMem_${i}"
//    }
//  }
//
//  //First elemt is ID and it looks like ID tags a memory request
//  // ID : Is AXI request ID
//  // @todo: Change @id value for each cache
//
//  // AXI constants - statically defined
//  io.mem.setConst()
//
//  // read request

//
//  //   read data
//  io.mem.r.ready := state === s_REFILL
//  when(io.mem.r.fire()) {
//    refill_buf(read_count) := io.mem.r.bits.data
//  }
//
//
//  //  // Info
//  val is_block_dirty = valid(set_count)(way_count) && dirty(set_count)(way_count)
//  val block_addr = Cat(block_rmeta.tag, set_count - 1.U) << blen.U
//
//  // write addr
//  io.mem.aw.bits.addr := Cat(rmeta.tag, set) << blen.U
//  io.mem.aw.bits.addr := Mux(flush_mode, block_addr, Cat(rmeta.tag, set) << blen.U)
//  io.mem.aw.bits.len := (nData - 1).U
//  io.mem.aw.valid := false.B
//
//  // Write resp
//  io.mem.w.bits.data := VecInit.tabulate(dataBeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)
//  io.mem.w.bits.data :=
//    Mux(flush_mode,
//      VecInit.tabulate(nData)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count),
//      VecInit.tabulate(nData)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count))
//
//  io.mem.w.bits.last := write_wrap_out
//  io.mem.w.valid := false.B
//  io.mem.b.ready := false.B
//
//  io.cpu.flush_done := false.B
//
//  /**
//    * Dumping cache state should add for debugging purpose
//    */
//
//  //   io.state := state.asUInt()
//  // Cache FSM
//  val countOn = true.B // increment counter every clock cycle
//  val (counterValue, counterWrap) = Counter(countOn, 64 * 1024)
//  //clockCycles := clockCycles + 1.U
//
//  //  val is_dirty = valid(set) && dirty(set)
//  val is_dirty = false.B
//  switch(state) {
//    is(s_IDLE) {
//      when(io.cpu.req.valid && false.B) {
//        //state := Mux(io.cpu.req.bits.iswrite, s_WRITE_CACHE, s_READ_CACHE)
//        when(io.cpu.req.bits.iswrite) {
//          state := s_WRITE_CACHE
//          if (debug) {
//            printf("\nSTORE START: %d\n", counterValue)
//          }
//        }.otherwise {
//          state := s_READ_CACHE
//          if (debug) {
//            printf("\nLOAD START:  %d\n", counterValue)
//          }
//        }
//      }
//    }
//    is(s_READ_CACHE) {
//      when(hit) {
//        when(io.cpu.req.valid) {
//          when(io.cpu.req.bits.iswrite) {
//            state := s_WRITE_CACHE
//          }.otherwise {
//            state := s_READ_CACHE
//          }
//        }.otherwise {
//          state := s_IDLE
//          if (debug) {
//            printf("\nLOAD END: %d\n", counterValue)
//          }
//        }
//      }.otherwise {
//        io.mem.aw.valid := is_dirty
//        io.mem.ar.valid := !is_dirty
//        when(io.mem.aw.fire()) {
//          state := s_WRITE_BACK
//        }.elsewhen(io.mem.ar.fire()) {
//          state := s_REFILL
//        }
//      }
//    }
//    is(s_WRITE_CACHE) {
//      when(hit || is_alloc_reg || io.cpu.abort) {
//        state := s_IDLE
//        io.cpu.resp.valid := true.B
//        if (debug) {
//          printf("\nSTORE END: %d\n", counterValue)
//        }
//      }.otherwise {
//        if (debug) {
//          printf("\nSTORE MISS: %d\n", counterValue)
//        }
//        io.mem.aw.valid := is_dirty
//        io.mem.ar.valid := !is_dirty
//        when(io.mem.aw.fire()) {
//          state := s_WRITE_BACK
//        }.elsewhen(io.mem.ar.fire()) {
//          state := s_REFILL
//        }
//      }
//    }
//    is(s_WRITE_BACK) {
//      io.mem.w.valid := true.B
//      when(write_wrap_out) {
//        state := s_WRITE_ACK
//      }
//    }
//    is(s_WRITE_ACK) {
//      io.mem.b.ready := true.B
//      when(io.mem.b.fire()) {
//        state := s_REFILL_READY
//      }
//    }
//    is(s_REFILL_READY) {
//      io.mem.ar.valid := true.B
//      when(io.mem.ar.fire()) {
//        state := s_REFILL
//      }
//    }
//    is(s_REFILL) {
//      if (debug) {
//        printf(p"state: Refill\n")
//      }
//      when(read_wrap_out) {
//        state := Mux(cpu_iswrite.asBool(), s_WRITE_CACHE, s_IDLE)
//        when(!cpu_iswrite) {
//          if (debug) {
//            printf("\nLOAD END: %d\n", counterValue)
//          }
//        }
//      }
//    }
//  }
//
//  //Flush state machine
//  switch(flush_state) {
//    is(s_flush_IDLE) {
//      when(io.cpu.flush) {
//        flush_state := s_flush_START
//        flush_mode := true.B
//      }
//    }
//    is(s_flush_START) {
//      when(set_wrap) {
//        io.cpu.flush_done := true.B
//        flush_mode := false.B
//        flush_state := s_flush_IDLE
//      }.elsewhen(is_block_dirty) {
//        flush_state := s_flush_ADDR
//        //        block_rmeta := metaMem.read(set_count)
//      }
//    }
//    is(s_flush_ADDR) {
//      /**
//        * When cycle delay to read from metaMem
//        */
//      flush_state := s_flush_WRITE
//    }
//    is(s_flush_WRITE) {
//      if (clog) {
//        printf(p"Dirty block address: 0x${Hexadecimal(block_addr)}\n")
//        printf(p"Dirty block data: 0x${Hexadecimal(dirty_cache_block)}\n")
//      }
//
//      io.mem.aw.valid := true.B
//      io.mem.ar.valid := false.B
//
//      when(io.mem.aw.fire()) {
//        flush_state := s_flush_WRITE_BACK
//      }
//    }
//    is(s_flush_WRITE_BACK) {
//      if (clog) {
//        printf(p"Write data: ${VecInit.tabulate(nData)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)}\n")
//      }
//      io.mem.w.valid := true.B
//      when(write_wrap_out) {
//        flush_state := s_flush_WRITE_ACK
//      }
//    }
//    is(s_flush_WRITE_ACK) {
//      io.mem.b.ready := true.B
//      when(io.mem.b.fire()) {
//        flush_state := s_flush_START
//      }
//    }
//  }
}
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

  val io = IO(new Bundle {
    //@todo ports for state and metadata
    val cpu = new CacheCPUIO
    val mem = new AXIMaster(memParams)
  })

  val Axi_param = memParams

  val metaMemory = Module(new MemBank(new MetaData())(xlen, setLen, nWays, nSets, wayLen))
  val dataMemory = Module(new MemBank( UInt(xlen.W)) (xlen, addrLen, nWords, nSets * nWays, wordLen))
  val cacheLogic = Module(new Gem5CacheLogic())

//  cacheLogic.io.metaMem.inputValue <> metaMemory.io.outputValue
//  cacheLogic.io.metaMem.inputValue <> dataMemory.io.outputValue
//
//  cacheLogic.io.metaMem.outputValue <> metaMemory.io.inputValue
//  cacheLogic.io.metaMem.outputValue <> dataMemory.io.inputValue

//  cacheLogic.io.dataMem <> dataMemory.io

  cacheLogic.io.metaMem.inputValue <> metaMemory.io.outputValue
  metaMemory.io.inputValue <> cacheLogic.io.metaMem.outputValue
  cacheLogic.io.metaMem.bank <> metaMemory.io.bank
  cacheLogic.io.metaMem.address <> metaMemory.io.address
  cacheLogic.io.metaMem.isRead <>metaMemory.io.isRead
  dataMemory.io.valid :=cacheLogic.io.dataMem.valid
  metaMemory.io.valid := cacheLogic.io.metaMem.valid

  dataMemory.io <> DontCare
  cacheLogic.io.dataMem <> DontCare
  this.io.mem <> DontCare
  this.io.cpu <> DontCare

  this.io.cpu <> cacheLogic.io.cpu
  this.io.mem <> cacheLogic.io.mem

}

/*
class SimpleCache(val ID: Int = 0, val debug: Boolean = false)(implicit val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {
  val io = IO(new Bundle {
    val cpu = new CacheCPUIO
    val mem = new AXIMaster(memParams)
  })

  val Axi_param = memParams

  // cache states
  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK :: s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val state = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)

  //Flush Logic
  val dirty_block = nSets


  // memory
  val valid = RegInit(0.U(nSets.W))
  val dirty = RegInit(0.U(nSets.W))
  val metaMem = Mem(nSets, new MetaData)
  val dataMem = Seq.fill(nWords)(Mem(nSets, Vec(wBytes, UInt(8.W))))

  val addr_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  val cpu_data = RegInit(0.U(io.cpu.req.bits.data.getWidth.W))
  val cpu_mask = RegInit(0.U(io.cpu.req.bits.mask.getWidth.W))
  val cpu_tag = RegInit(0.U(io.cpu.req.bits.tag.getWidth.W))
  val cpu_iswrite = RegInit(0.U(io.cpu.req.bits.iswrite.getWidth.W))

  val count_set = RegInit(false.B)

  // Counters
  require(dataBeats > 0)
  val (read_count, read_wrap_out) = Counter(io.mem.r.fire(), dataBeats)
  val (write_count, write_wrap_out) = Counter(io.mem.w.fire(), dataBeats)

  //  val (block_count, block_wrap) = Counter(flush_state === s_flush_BLOCK, nWords)
  val (set_count, set_wrap) = Counter(flush_state === s_flush_START, nSets)

  //val block_addr_tag_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  //@todo get ride of -1
  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
  val block_rmeta = RegInit(init = MetaData.default)
  val flush_mode = RegInit(false.B)

  val is_idle = state === s_IDLE
  val is_read = state === s_READ_CACHE
  val is_write = state === s_WRITE_CACHE
  val is_alloc = state === s_REFILL && read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)

  val hit = Wire(Bool())
  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
  val ren_reg = RegNext(ren)

  val addr = io.cpu.req.bits.addr
  val tag = addr(xlen - 1, slen + blen)
  val idx = addr(slen + blen - 1, blen)
  val off = addr(blen - 1, byteOffsetBits)

  val tag_reg = addr_reg(xlen - 1, slen + blen)
  val idx_reg = addr_reg(slen + blen - 1, blen)
  val off_reg = addr_reg(blen - 1, byteOffsetBits)


  val cache_block = Cat((dataMem map (_.read(idx).asUInt)).reverse)
  val cache_block_size = bBytes * 8
  val rmeta = RegNext(next = metaMem.read(idx), init = MetaData.default)
  val rdata = RegNext(next = cache_block, init = 0.U(cache_block_size.W))
  val rdata_buf = RegEnable(rdata, ren_reg)
  val refill_buf = RegInit(VecInit(Seq.fill(dataBeats)(0.U(Axi_param.dataBits.W))))
  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))

  hit := valid(idx_reg) && rmeta.tag === tag_reg

  // Read Mux
  io.cpu.resp.bits.data := VecInit.tabulate(nWords)(i => read((i + 1) * xlen - 1, i * xlen))(off_reg)
  io.cpu.resp.bits.tag := cpu_tag
  io.cpu.resp.bits.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
  io.cpu.resp.bits.iswrite := cpu_iswrite

  //Extra input needs to be removed
  io.cpu.resp.bits.tile := 0.U

  // Can be: 1)Write hit, 2)Read hit, 3)Read miss
  io.cpu.resp.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
  io.cpu.req.ready := is_idle || (state === s_READ_CACHE && hit)

  // Latch the cpu request
  when(io.cpu.req.fire()) {
    addr_reg := addr
    cpu_tag := io.cpu.req.bits.tag
    cpu_data := io.cpu.req.bits.data
    cpu_mask := io.cpu.req.bits.mask
    cpu_iswrite := io.cpu.req.bits.iswrite
  }

  val wmeta = MetaData(tag_reg)

  // is_alloc means cache hit, hence, if is_alloc is false, it means our mask should consider only the modified byte
  // and since wdata is masked with wmask we only duplicate the write data, otherwise, since mask doesn't do anything
  // with write data we make the proper wdata using refill_buf
  val wmask = Mux(!is_alloc, (cpu_mask << Cat(off_reg, 0.U(byteOffsetBits.W))).asUInt.zext, (-1).asSInt()).asUInt()
  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
    if (refill_buf.size == 1) io.mem.r.bits.data
    else Cat(io.mem.r.bits.data, Cat(refill_buf.init.reverse)))


  when(wen) {
    valid := valid.bitSet(idx_reg, true.B)
    dirty := dirty.bitSet(idx_reg, !is_alloc)

    //    dirty_mem(dirty_block) := dirty_mem(dirty_block).bitSet(dirty_offset, !is_alloc)

    when(is_alloc) {
      metaMem.write(idx_reg, wmeta)
    }
    dataMem.zipWithIndex foreach { case (mem, i) =>
      val data = VecInit.tabulate(wBytes)(k => wdata(i * xlen + (k + 1) * 8 - 1, i * xlen + k * 8))
      mem.write(idx_reg, data, (wmask((i + 1) * wBytes - 1, i * wBytes)).asBools)
      mem suggestName s"dataMem_${i}"
    }
  }

  //First elemt is ID and it looks like ID tags a memory request
  // ID : Is AXI request ID
  // @todo: Change @id value for each cache

  // AXI constants - statically defined
  io.mem.setConst()

  // read request
  io.mem.ar.bits.addr := Cat(tag_reg, idx_reg) << blen.U
  io.mem.ar.bits.len := (dataBeats - 1).U
  io.mem.ar.valid := false.B

  // read data
  io.mem.r.ready := state === s_REFILL
  when(io.mem.r.fire()) {
    refill_buf(read_count) := io.mem.r.bits.data
  }


  // Info
  val is_block_dirty = valid(set_count) && dirty(set_count)
  val block_addr = Cat(block_rmeta.tag, set_count - 1.U) << blen.U

  // write addr
  //io.mem.aw.bits.addr := Cat(rmeta.tag, idx_reg) << blen.U
  io.mem.aw.bits.addr := Mux(flush_mode, block_addr, Cat(rmeta.tag, idx_reg) << blen.U)
  io.mem.aw.bits.len := (dataBeats - 1).U
  io.mem.aw.valid := false.B

  // Write resp
  //io.mem.w.bits.data := VecInit.tabulate(dataBeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)
  io.mem.w.bits.data :=
    Mux(flush_mode,
      VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count),
      VecInit.tabulate(dataBeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count))

  io.mem.w.bits.last := write_wrap_out
  io.mem.w.valid := false.B
  io.mem.b.ready := false.B

  io.cpu.flush_done := false.B

  /**
   * Dumping cache state should add for debugging purpose
   */
  // io.stat := state.asUInt()
  // Cache FSM
  val countOn = true.B // increment counter every clock cycle
  val (counterValue, counterWrap) = Counter(countOn, 64 * 1024)
  //clockCycles := clockCycles + 1.U

  val is_dirty = valid(idx_reg) && dirty(idx_reg)
  switch(state) {
    is(s_IDLE) {
      when(io.cpu.req.valid) {
        //state := Mux(io.cpu.req.bits.iswrite, s_WRITE_CACHE, s_READ_CACHE)
        when(io.cpu.req.bits.iswrite) {
          state := s_WRITE_CACHE
          if (debug) {
            printf("\nSTORE START: %d\n", counterValue)
          }
        }.otherwise {
          state := s_READ_CACHE
          if (debug) {
            printf("\nLOAD START:  %d\n", counterValue)
          }
        }
      }
    }
    is(s_READ_CACHE) {
      when(hit) {
        when(io.cpu.req.valid) {
          when(io.cpu.req.bits.iswrite) {
            state := s_WRITE_CACHE
          }.otherwise {
            state := s_READ_CACHE
          }
        }.otherwise {
          state := s_IDLE
          if (debug) {
            printf("\nLOAD END: %d\n", counterValue)
          }
        }
      }.otherwise {
        io.mem.aw.valid := is_dirty
        io.mem.ar.valid := !is_dirty
        when(io.mem.aw.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.ar.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_CACHE) {
      when(hit || is_alloc_reg || io.cpu.abort) {
        state := s_IDLE
        io.cpu.resp.valid := true.B
        if (debug) {
          printf("\nSTORE END: %d\n", counterValue)
        }
      }.otherwise {
        if (debug) {
          printf("\nSTORE MISS: %d\n", counterValue)
        }
        io.mem.aw.valid := is_dirty
        io.mem.ar.valid := !is_dirty
        when(io.mem.aw.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.ar.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_BACK) {
      io.mem.w.valid := true.B
      when(write_wrap_out) {
        state := s_WRITE_ACK
      }
    }
    is(s_WRITE_ACK) {
      io.mem.b.ready := true.B
      when(io.mem.b.fire()) {
        state := s_REFILL_READY
      }
    }
    is(s_REFILL_READY) {
      io.mem.ar.valid := true.B
      when(io.mem.ar.fire()) {
        state := s_REFILL
      }
    }
    is(s_REFILL) {
      if (debug) {
        printf(p"state: Refill\n")
      }
      when(read_wrap_out) {
        state := Mux(cpu_iswrite.asBool(), s_WRITE_CACHE, s_IDLE)
        when(!cpu_iswrite) {
          if (debug) {
            printf("\nLOAD END: %d\n", counterValue)
          }
        }
      }
    }
  }

  //Flush state machine
  switch(flush_state) {
    is(s_flush_IDLE) {
      when(io.cpu.flush) {
        flush_state := s_flush_START
        flush_mode := true.B
      }
    }
    is(s_flush_START) {
      when(set_wrap) {
        io.cpu.flush_done := true.B
        flush_mode := false.B
        flush_state := s_flush_IDLE
      }.elsewhen(is_block_dirty) {
        flush_state := s_flush_ADDR
        block_rmeta := metaMem.read(set_count)
      }
    }
    is(s_flush_ADDR) {
      /**
       * When cycle delay to read from metaMem
       */
      flush_state := s_flush_WRITE
    }
    is(s_flush_WRITE) {
      if (clog) {
        printf(p"Dirty block address: 0x${Hexadecimal(block_addr)}\n")
        printf(p"Dirty block data: 0x${Hexadecimal(dirty_cache_block)}\n")
      }

      io.mem.aw.valid := true.B
      io.mem.ar.valid := false.B

      when(io.mem.aw.fire()) {
        flush_state := s_flush_WRITE_BACK
      }
    }
    is(s_flush_WRITE_BACK) {
      if (clog) {
        printf(p"Write data: ${VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)}\n")
      }
      io.mem.w.valid := true.B
      when(write_wrap_out) {
        flush_state := s_flush_WRITE_ACK
      }
    }
    is(s_flush_WRITE_ACK) {
      io.mem.b.ready := true.B
      when(io.mem.b.fire()) {
        flush_state := s_flush_START
      }
    }
  }


}


/**
 * A simple cache implementation with AXIMaster implementation
 * NOTE: There is a limitation in the cache behavior.
 * You shouldn't make a new request valid untill the previous request is became valid
 * otherwise, the cache behaviour is unpredictable
 *
 * @param ID
 * @param debug
 * @param p
 */


class ReferenceCache(val ID: Int = 0, val debug: Boolean = false)(implicit val p: Parameters) extends Module
  with HasAccelParams
  with HasCacheAccelParams
  with HasAccelShellParams {
  //import Chisel._ // FIXME: read enable signals for memories are broken by new chisel

  val io = IO(new Bundle {
    val cpu = new CacheCPUIO
    val mem = new AXIMaster(memParams)
  })

  val Axi_param = memParams

  // cache states
  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK ::
    s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val state = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)
  val flush_mode = RegInit(false.B)

  // memory
  val v = RegInit(0.U(nSets.W))
  val d = RegInit(0.U(nSets.W))
  val metaMem = SyncReadMem(nSets, new MetaData)
  val dataMem = Seq.fill(nWords)(SyncReadMem(nSets, Vec(wBytes, UInt(8.W))))
  //val metaMem  = SeqMem(nSets, new MetaData) //Chisel2
  //val dataMem  = Seq.fill(nWords)(SeqMem(nSets, Vec(wBytes, UInt(8.W)))) // Chisel2

  val addr_reg = Reg(io.cpu.req.bits.addr.cloneType)
  val cpu_tag_reg = Reg(io.cpu.req.bits.tag.cloneType)
  val cpu_iswrite_reg = Reg(io.cpu.req.bits.iswrite.cloneType)
  val cpu_tile_reg = Reg(io.cpu.req.bits.tile.cloneType)
  val cpu_data = Reg(io.cpu.req.bits.data.cloneType)
  val cpu_mask = Reg(io.cpu.req.bits.mask.cloneType)

  // Counters
  require(dataBeats > 0)
  val (read_count, read_wrap_out) = Counter(io.mem.r.fire(), dataBeats)
  val (write_count, write_wrap_out) = Counter(io.mem.w.fire(), dataBeats)

  val (set_count, set_wrap) = Counter(flush_state === s_flush_START, nSets)
  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
  val block_rmeta = Reg(new MetaData)
  val read_rmeta = Wire(new MetaData)


  val is_idle = state === s_IDLE
  val is_read = state === s_READ_CACHE
  val is_write = state === s_WRITE_CACHE
  val is_alloc = state === s_REFILL && read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)

  val hit = Wire(Bool())
  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
  val ren_reg = RegNext(ren)

  val addr = io.cpu.req.bits.addr
  val idx = addr(slen + blen - 1, blen)
  val tag_reg = addr_reg(xlen - 1, slen + blen)
  val idx_reg = addr_reg(slen + blen - 1, blen)
  val off_reg = addr_reg(blen - 1, byteOffsetBits)

  val rmeta = metaMem.read(idx, ren)
  val rdata = Cat((dataMem map (_.read(idx, ren).asUInt)).reverse)
  val rdata_buf = RegEnable(rdata, ren_reg)
  //val refill_buf = Reg(Vec(dataBeats, UInt(nastiXDataBits.W)))
  val refill_buf = Reg(Vec(dataBeats, UInt(Axi_param.dataBits.W)))
  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))

  hit := v(idx_reg) && rmeta.tag === tag_reg

  io.cpu.req.ready := is_idle || (state === s_READ_CACHE && hit)

  // Read Mux
  io.cpu.resp.bits.data := VecInit.tabulate(nWords)(i => read((i + 1) * xlen - 1, i * xlen))(off_reg)
  io.cpu.resp.valid := is_idle || is_read && hit || is_alloc_reg && !cpu_mask.orR

  io.cpu.resp.bits.tile := 0.U
  io.cpu.resp.bits.valid := true.B
  io.cpu.resp.bits.tag := cpu_tag_reg
  io.cpu.resp.bits.iswrite := cpu_iswrite_reg

  when(io.cpu.req.fire) {
    cpu_tag_reg := io.cpu.req.bits.tag
    cpu_iswrite_reg := io.cpu.req.bits.iswrite
    cpu_tile_reg := io.cpu.req.bits.tile
  }

  when(io.cpu.resp.valid) {
    addr_reg := addr
    cpu_data := io.cpu.req.bits.data
    cpu_mask := io.cpu.req.bits.mask
  }

  val wmeta = Wire(new MetaData)
  wmeta.tag := tag_reg

  val wmask = Mux(!is_alloc, (cpu_mask << Cat(off_reg, 0.U(byteOffsetBits.W))).asUInt.zext(), (-1).S)
  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
    if (refill_buf.size == 1) io.mem.r.bits.data
    else Cat(io.mem.r.bits.data, Cat(refill_buf.init.reverse)))
  when(wen) {
    v := v.bitSet(idx_reg, true.B)
    d := d.bitSet(idx_reg, !is_alloc)
    when(is_alloc) {
      metaMem.write(idx_reg, wmeta)
    }
    dataMem.zipWithIndex foreach { case (mem, i) =>
      val data = VecInit.tabulate(wBytes)(k => wdata(i * xlen + (k + 1) * 8 - 1, i * xlen + k * 8))
      mem.write(idx_reg, data, wmask((i + 1) * wBytes - 1, i * wBytes).asBools())
      mem suggestName s"dataMem_${i}"
    }
  }

  if (clog) {
    when(wen && is_alloc) {
      printf(p"Write meta: idx:${idx_reg.asUInt()} -- data:${wmeta.tag}\n")
      printf(p"Write meta: addr: ${addr_reg}\n")
    }
  }

  io.mem.ar.bits := NastiReadAddressChannel(
    0.U, (Cat(tag_reg, idx_reg) << blen.U).asUInt(), log2Up(Axi_param.dataBits / 8).U, (dataBeats - 1).U)
  io.mem.ar.valid := false.B
  // read data
  io.mem.r.ready := state === s_REFILL
  when(io.mem.r.fire()) {
    refill_buf(read_count) := io.mem.r.bits.data
  }

  // Info
  val is_block_dirty = v(set_count) && d(set_count)
  val block_addr = Cat(block_rmeta.tag, set_count - 1.U) << blen.U
  read_rmeta := metaMem.read(set_count, (is_block_dirty) && (flush_state === s_flush_START))
  block_rmeta := read_rmeta

  if (clog) {
    when((is_block_dirty) && (flush_state === s_flush_START)) {
      printf(p"Read value of ${set_count} -- ${read_rmeta}\n")
    }
  }

  // write addr
  io.mem.aw.bits := NastiWriteAddressChannel(
    0.U, Mux(flush_mode, block_addr, Cat(rmeta.tag, idx_reg) << blen.U).asUInt(), log2Up(Axi_param.dataBits / 8).U, (dataBeats - 1).U)
  io.mem.aw.valid := false.B
  // write data
  io.mem.w.bits := NastiWriteDataChannel(
    Mux(flush_mode,
      VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count),
      VecInit.tabulate(dataBeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)),
    None, write_wrap_out)
  io.mem.w.valid := false.B
  // write resp
  io.mem.b.ready := false.B
  io.cpu.flush_done := false.B

  // Cache FSM
  val is_dirty = v(idx_reg) && d(idx_reg)
  switch(state) {
    is(s_IDLE) {
      when(io.cpu.req.valid) {
        state := Mux(io.cpu.req.bits.mask.orR, s_WRITE_CACHE, s_READ_CACHE)
      }
    }
    is(s_READ_CACHE) {
      when(hit) {
        when(io.cpu.req.valid) {
          state := Mux(io.cpu.req.bits.mask.orR, s_WRITE_CACHE, s_READ_CACHE)
        }.otherwise {
          state := s_IDLE
        }
      }.otherwise {
        io.mem.aw.valid := is_dirty
        io.mem.ar.valid := !is_dirty
        when(io.mem.aw.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.ar.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_CACHE) {
      when(hit || is_alloc_reg || io.cpu.abort) {
        state := s_IDLE
      }.otherwise {
        io.mem.aw.valid := is_dirty
        io.mem.ar.valid := !is_dirty
        when(io.mem.aw.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.ar.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_BACK) {
      io.mem.w.valid := true.B
      when(write_wrap_out) {
        state := s_WRITE_ACK
      }
    }
    is(s_WRITE_ACK) {
      io.mem.b.ready := true.B
      when(io.mem.b.fire()) {
        state := s_REFILL_READY
      }
    }
    is(s_REFILL_READY) {
      io.mem.ar.valid := true.B
      when(io.mem.ar.fire()) {
        state := s_REFILL
      }
    }
    is(s_REFILL) {
      when(read_wrap_out) {
        state := Mux(cpu_mask.orR, s_WRITE_CACHE, s_IDLE)
      }
    }
  }

  //Flush state machine
  switch(flush_state) {
    is(s_flush_IDLE) {
      when(io.cpu.flush) {
        flush_state := s_flush_START
        flush_mode := true.B
      }
    }
    is(s_flush_START) {
      when(set_wrap) {
        io.cpu.flush_done := true.B
        flush_mode := false.B
        flush_state := s_flush_IDLE
      }.elsewhen(is_block_dirty) {
        flush_state := s_flush_ADDR
        if (clog) {
          printf(p"[START] Read value of ${set_count} -- ${read_rmeta}\n")
          printf(p"[START] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
        }
      }
    }
    is(s_flush_ADDR) {
      /**
       * There is a bug in sequential memory of chisel that doesn't provide data in the same cycle
       */
      if (clog) {
        printf(p"[ADDR] Read value of ${set_count} -- ${read_rmeta}\n")
        printf(p"[ADDR] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
      }
      flush_state := s_flush_WRITE
    }
    is(s_flush_WRITE) {
      if (clog) {
        printf(p"[WRITE] Read value of ${set_count} -- ${read_rmeta}\n")
        printf(p"[WRITE] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
        printf(p"SetCount:[${set_count - 1.U}] -- block_rmeta[${block_rmeta.tag}]\n")
        printf(p"Dirty block address: 0x${Hexadecimal(block_addr)}\n")
        printf(p"Dirty block data: 0x${Hexadecimal(dirty_cache_block)}\n")
        printf(p"Read value of ${set_count} -- ${read_rmeta.tag}\n")
      }

      io.mem.aw.valid := true.B
      io.mem.ar.valid := false.B

      when(io.mem.aw.fire()) {
        flush_state := s_flush_WRITE_BACK
      }
    }
    is(s_flush_WRITE_BACK) {
      if (clog) {
        printf(p"Write data: ${VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)}\n")
      }
      io.mem.w.valid := true.B
      when(write_wrap_out) {
        flush_state := s_flush_WRITE_ACK
      }
    }
    is(s_flush_WRITE_ACK) {
      io.mem.b.ready := true.B
      when(io.mem.b.fire()) {
        flush_state := s_flush_START
      }
    }
  }
}

/**
 * This is a simple cache with DME interface instead of AXIMaster
 *
 * @param ID
 * @param debug
 * @param p
 */
class DMECache(val ID: Int = 0, val debug: Boolean = false)(implicit val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val io = IO(new Bundle {
    val cpu = new CacheCPUIO
    val mem = new DMEMaster
  })

  val Axi_param = memParams

  // cache states
  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK ::
    s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val state = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)
  val flush_mode = RegInit(false.B)

  // memory
  val v = RegInit(0.U(nSets.W))
  val d = RegInit(0.U(nSets.W))
  val metaMem = SyncReadMem(nSets, new MetaData)
  val dataMem = Seq.fill(nWords)(SyncReadMem(nSets, Vec(wBytes, UInt(8.W))))

  val addr_reg = Reg(io.cpu.req.bits.addr.cloneType)
  val cpu_tag_reg = Reg(io.cpu.req.bits.tag.cloneType)
  val cpu_iswrite_reg = Reg(io.cpu.req.bits.iswrite.cloneType)
  val cpu_tile_reg = Reg(io.cpu.req.bits.tile.cloneType)
  val cpu_data = Reg(io.cpu.req.bits.data.cloneType)
  val cpu_mask = Reg(io.cpu.req.bits.mask.cloneType)

  // Counters
  require(dataBeats > 0)
  val (read_count, read_wrap_out) = Counter(io.mem.rd.data.fire(), dataBeats)
  val (write_count, write_wrap_out) = Counter(io.mem.wr.data.fire(), dataBeats)

  val (set_count, set_wrap) = Counter(flush_state === s_flush_START, nSets)
  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
  val block_rmeta = Reg(new MetaData)
  val read_rmeta = Wire(new MetaData)


  val is_idle = state === s_IDLE
  val is_read = state === s_READ_CACHE
  val is_write = state === s_WRITE_CACHE
  val is_alloc = state === s_REFILL && read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)

  val hit = Wire(Bool())
  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
  val ren_reg = RegNext(ren)

  val addr = io.cpu.req.bits.addr
  val idx = addr(slen + blen - 1, blen)
  val tag_reg = addr_reg(xlen - 1, slen + blen)
  val idx_reg = addr_reg(slen + blen - 1, blen)
  val off_reg = addr_reg(blen - 1, byteOffsetBits)

  val rmeta = metaMem.read(idx, ren)
  val rdata = Cat((dataMem map (_.read(idx, ren).asUInt)).reverse)
  val rdata_buf = RegEnable(rdata, ren_reg)
  val refill_buf = Reg(Vec(dataBeats, UInt(Axi_param.dataBits.W)))
  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))

  hit := v(idx_reg) && rmeta.tag === tag_reg

  io.cpu.req.ready := is_idle || (state === s_READ_CACHE && hit)

  // Read Mux
  io.cpu.resp.bits.data := VecInit.tabulate(nWords)(i => read((i + 1) * xlen - 1, i * xlen))(off_reg)
  io.cpu.resp.valid := is_idle || is_read && hit || is_alloc_reg && !cpu_mask.orR

  io.cpu.resp.bits.tile := 0.U
  io.cpu.resp.bits.valid := true.B
  io.cpu.resp.bits.tag := cpu_tag_reg
  io.cpu.resp.bits.iswrite := cpu_iswrite_reg

  when(io.cpu.req.fire) {
    cpu_tag_reg := io.cpu.req.bits.tag
    cpu_iswrite_reg := io.cpu.req.bits.iswrite
    cpu_tile_reg := io.cpu.req.bits.tile
  }

  when(io.cpu.resp.valid) {
    addr_reg := addr
    cpu_data := io.cpu.req.bits.data
    cpu_mask := io.cpu.req.bits.mask
  }

  val wmeta = Wire(new MetaData)
  wmeta.tag := tag_reg

  val wmask = Mux(!is_alloc, (cpu_mask << Cat(off_reg, 0.U(byteOffsetBits.W))).asUInt.zext(), (-1).S)
  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
    if (refill_buf.size == 1) io.mem.rd.data.bits
    else Cat(io.mem.rd.data.bits, Cat(refill_buf.init.reverse)))
  when(wen) {
    v := v.bitSet(idx_reg, true.B)
    d := d.bitSet(idx_reg, !is_alloc)
    when(is_alloc) {
      metaMem.write(idx_reg, wmeta)
    }
    dataMem.zipWithIndex foreach { case (mem, i) =>
      val data = VecInit.tabulate(wBytes)(k => wdata(i * xlen + (k + 1) * 8 - 1, i * xlen + k * 8))
      mem.write(idx_reg, data, wmask((i + 1) * wBytes - 1, i * wBytes).asBools())
      mem suggestName s"dataMem_${i}"
    }
  }

  if (clog) {
    when(wen && is_alloc) {
      printf(p"Write meta: idx:${idx_reg.asUInt()} -- data:${wmeta.tag}\n")
      printf(p"Write meta: addr: ${addr_reg}\n")
    }
  }

  io.mem.rd.cmd.bits.addr := Cat(tag_reg, idx_reg) << blen.U
  io.mem.rd.cmd.bits.len := (dataBeats - 1).U
  io.mem.rd.cmd.valid := false.B

  // read data
  io.mem.rd.data.ready := state === s_REFILL
  when(io.mem.rd.data.fire()) {
    refill_buf(read_count) := io.mem.rd.data.bits
  }

  // Info
  val is_block_dirty = v(set_count) && d(set_count)
  val block_addr = Cat(block_rmeta.tag, set_count - 1.U) << blen.U
  read_rmeta := metaMem.read(set_count, (is_block_dirty) && (flush_state === s_flush_START))
  block_rmeta := read_rmeta

  if (clog) {
    when((is_block_dirty) && (flush_state === s_flush_START)) {
      printf(p"Read value of ${set_count} -- ${read_rmeta}\n")
    }
  }

  // write addr
  io.mem.wr.cmd.bits.addr := Mux(flush_mode, block_addr, Cat(rmeta.tag, idx_reg) << blen.U)
  io.mem.wr.cmd.bits.len := (dataBeats - 1).U
  io.mem.wr.cmd.valid := false.B

  // write data
  io.mem.wr.data.bits := Mux(flush_mode,
    VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count),
    VecInit.tabulate(dataBeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count))
  io.mem.wr.data.valid := false.B

  // write resp
  io.cpu.flush_done := false.B

  // Cache FSM
  val is_dirty = v(idx_reg) && d(idx_reg)
  switch(state) {
    is(s_IDLE) {
      when(io.cpu.req.valid) {
        state := Mux(io.cpu.req.bits.mask.orR, s_WRITE_CACHE, s_READ_CACHE)
      }
    }
    is(s_READ_CACHE) {
      when(hit) {
        when(io.cpu.req.valid) {
          state := Mux(io.cpu.req.bits.mask.orR, s_WRITE_CACHE, s_READ_CACHE)
        }.otherwise {
          state := s_IDLE
        }
      }.otherwise {
        io.mem.wr.cmd.valid := is_dirty
        io.mem.rd.cmd.valid := !is_dirty
        when(io.mem.wr.cmd.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.rd.cmd.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_CACHE) {
      when(hit || is_alloc_reg || io.cpu.abort) {
        state := s_IDLE
      }.otherwise {
        io.mem.wr.cmd.valid := is_dirty
        io.mem.rd.cmd.valid := !is_dirty
        when(io.mem.wr.cmd.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.rd.cmd.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_BACK) {
      io.mem.wr.data.valid := true.B
      when(write_wrap_out) {
        state := s_WRITE_ACK
      }
    }
    is(s_WRITE_ACK) {
      when(io.mem.wr.ack) {
        state := s_REFILL_READY
      }
    }
    is(s_REFILL_READY) {
      io.mem.rd.cmd.valid := true.B
      when(io.mem.rd.cmd.fire()) {
        state := s_REFILL
      }
    }
    is(s_REFILL) {
      when(read_wrap_out) {
        state := Mux(cpu_mask.orR, s_WRITE_CACHE, s_IDLE)
      }
    }
  }

  //Flush state machine
  switch(flush_state) {
    is(s_flush_IDLE) {
      when(io.cpu.flush) {
        flush_state := s_flush_START
        flush_mode := true.B
      }
    }
    is(s_flush_START) {
      when(set_wrap) {
        io.cpu.flush_done := true.B
        flush_mode := false.B
        flush_state := s_flush_IDLE
      }.elsewhen(is_block_dirty) {
        flush_state := s_flush_ADDR
        if (clog) {
          printf(p"[START] Read value of ${set_count} -- ${read_rmeta}\n")
          printf(p"[START] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
        }
      }
    }
    is(s_flush_ADDR) {
      /**
       * There is a bug in sequential memory of chisel that doesn't provide data in the same cycle
       */
      if (clog) {
        printf(p"[ADDR] Read value of ${set_count} -- ${read_rmeta}\n")
        printf(p"[ADDR] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
      }
      flush_state := s_flush_WRITE
    }
    is(s_flush_WRITE) {
      if (clog) {
        printf(p"[WRITE] Read value of ${set_count} -- ${read_rmeta}\n")
        printf(p"[WRITE] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
        printf(p"SetCount:[${set_count - 1.U}] -- block_rmeta[${block_rmeta.tag}]\n")
        printf(p"Dirty block address: 0x${Hexadecimal(block_addr)}\n")
        printf(p"Dirty block data: 0x${Hexadecimal(dirty_cache_block)}\n")
        printf(p"Read value of ${set_count} -- ${read_rmeta.tag}\n")
      }

      io.mem.wr.cmd.valid := true.B
      io.mem.rd.cmd.valid := false.B

      when(io.mem.wr.cmd.fire()) {
        flush_state := s_flush_WRITE_BACK
      }
    }
    is(s_flush_WRITE_BACK) {
      if (clog) {
        printf(p"Write data: ${VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)}\n")
      }
      io.mem.wr.data.valid := true.B
      when(write_wrap_out) {
        flush_state := s_flush_WRITE_ACK
      }
    }
    is(s_flush_WRITE_ACK) {
      when(io.mem.wr.ack) {
        flush_state := s_flush_START
      }
    }
  }
}

*/

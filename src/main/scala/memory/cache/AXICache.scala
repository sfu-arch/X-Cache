// See LICENSE for license details.
package dandelion.memory.cache

import chisel3._
import chisel3.util._
import dandelion.config._
import dandelion.interfaces._
import dandelion.interfaces.axi._
import dandelion.shell.{ShellKey}


trait CacheAccelParams extends HasAccelParams {
  val nWays = nways
  val nSets = nsets
  val bBytes = cacheBlockBytes
  val bBits = bBytes << 3
  val blen = log2Ceil(bBytes)
  val slen = log2Ceil(nSets)
  val taglen = xlen - (slen + blen)
  val nWords = bBits / xlen
  val wBytes = xlen / 8
  val byteOffsetBits = log2Ceil(wBytes)
  val databeats = bBits / p(ShellKey).memParams.dataBits
}


class CacheCPUIO(implicit p: Parameters) extends GenericParameterizedBundle(p) {
  val abort = Input(Bool())
  val flush = Input(Bool())
  val flush_done = Output(Bool())
  val req = Flipped(Decoupled(new MemReq))
  val resp = Output(Valid(new MemResp))
}


class MetaData(implicit p: Parameters) extends AXIAccelBundle with CacheAccelParams {
  val tag = UInt(taglen.W)
}

object MetaData {

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


class SimpleCache(val ID: Int = 0, val debug: Boolean = false)(implicit val p: Parameters) extends Module with CacheAccelParams {
  val io = IO(new Bundle {
    val cpu = new CacheCPUIO
    val mem = new AXIMaster(p(ShellKey).memParams)
  })

  val Axi_param = p(ShellKey).memParams

  // cache states
  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK :: s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val state = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_BLOCK :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(7)
  val flush_state = RegInit(s_flush_IDLE)

  //Flush Logic
  val dirty_offset_size = 16
  val dirty_block_size = nSets / dirty_offset_size
  val dirty_mem = RegInit(VecInit(Seq.fill(dirty_block_size)(0.U(dirty_offset_size.W))))


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

  // Counters
  require(databeats > 0)
  val (read_count, read_wrap_out) = Counter(io.mem.r.fire(), databeats)
  val (write_count, write_wrap_out) = Counter(io.mem.w.fire(), databeats)

  val (flush_count, flush_wrap) = Counter(flush_state === s_flush_START, dirty_block_size)
  val (flush_block_count, flush_block_wrap) = Counter(flush_state === s_flush_BLOCK, dirty_offset_size)

  val dirty_block_reg = RegInit(0.U(log2Ceil(dirty_block_size).W))
  val dirty_block_offset_reg = RegInit(0.U(log2Ceil(dirty_offset_size).W))
  val dirty_block_tag_reg = RegInit(0.U((xlen - (slen + blen)).W))
  val dirty_block_addr = RegInit(0.U(xlen.W))

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

  val dirty_block = idx(log2Ceil(dirty_block_size) + log2Ceil(dirty_offset_size) - 1, log2Ceil(dirty_offset_size))
  val dirty_offset = idx(log2Ceil(dirty_offset_size) - 1, 0)

  val cache_block = Cat((dataMem map (_.read(idx).asUInt)).reverse)
  val cache_block_size = cacheBlockBytes * 8
  val rmeta = RegNext(next = metaMem.read(idx), init = MetaData.default)
  val rdata = RegNext(next = cache_block, init = 0.U(cache_block_size.W))
  val rdata_buf = RegEnable(rdata, ren_reg)
  val refill_buf = RegInit(VecInit(Seq.fill(databeats)(0.U(Axi_param.dataBits.W))))
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

    dirty_mem(dirty_block) := dirty_mem(dirty_block).bitSet(dirty_offset, !is_alloc)

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
  io.mem.ar.bits.len := (databeats - 1).U
  io.mem.ar.valid := false.B

  // read data
  io.mem.r.ready := state === s_REFILL
  when(io.mem.r.fire()) {
    refill_buf(read_count) := io.mem.r.bits.data
  }

  // write addr
  io.mem.aw.bits.addr := Cat(rmeta.tag, idx_reg) << blen.U
  io.mem.aw.bits.len := (databeats - 1).U
  io.mem.aw.valid := false.B

  // Write resp
  io.mem.w.bits.data := VecInit.tabulate(databeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)
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
  // Info

  //Flush state machine
  switch(flush_state) {
    is(s_flush_IDLE) {
      when(io.cpu.flush) {
        flush_state := s_flush_START
      }
    }
    is(s_flush_START) {
      val dirty_valid = dirty_mem(flush_count).orR()
      when(flush_wrap) {
        io.cpu.flush_done := true.B
        flush_state := s_flush_IDLE
      }
      when(dirty_valid) {
        flush_state := s_flush_BLOCK
        dirty_block_reg := flush_count
      }
    }
    is(s_flush_BLOCK) {
      val dirty_block_value = dirty_mem(dirty_block_reg)(flush_block_count)

      //Need to create the dirty address
      when(dirty_block_value) {
        dirty_block_tag_reg := metaMem.read(idx).tag
        dirty_block_offset_reg := flush_block_count
        flush_state := s_flush_ADDR
      }

      when(flush_block_wrap) {
        flush_state := s_flush_START
      }
    }
    is(s_flush_ADDR) {
      flush_state := s_flush_WRITE

      // This is the address of dirty block
      //val dirty_addr = Cat(dirty_block_tag_reg, dirty_block_reg, dirty_block_offset_reg, 0.U(blen.W))
      dirty_block_addr := Cat(dirty_block_tag_reg, dirty_block_reg, dirty_block_offset_reg) << blen.U
      ren_reg := true.B
    }
    is(s_flush_WRITE){
      printf("Dirty block address: %d\n", dirty_block_addr)

      io.mem.aw.bits.addr := dirty_block_addr

      io.mem.aw.valid := true.B
      io.mem.ar.valid := false.B

      when(io.mem.aw.fire()) {
        flush_state := s_flush_WRITE_BACK
      }
    }
    is(s_flush_WRITE_BACK) {
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


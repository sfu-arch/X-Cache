package dandelion.memory.cache

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util.{Valid, _}
import chisel3.util._
import dandelion.config.{AXIAccelBundle, HasAccelShellParams}
import dandelion.memory.cache.HasCacheAccelParams


class DecoderIO (nSigs: Int)(implicit val p:Parameters) extends Bundle {

    val inAction = Input(UInt(nSigs.W))
    val outSignals = Output(Vec(nSigs, Bool()))

}

class Decoder (implicit val p:Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

    val io = IO(new DecoderIO(nSigs))
    io.outSignals := io.inAction.asBools()
}
class Find[T1 <: Data , T2 <: Data]( K: T1, D:T2, depth: Int, outLen: Int = 32) (implicit val p:Parameters) extends Module {

    val io = IO(new Bundle {
        val key = Input(D.cloneType)
        val data = Input(Vec(depth, D.cloneType))
        val valid = Input(Vec(depth, Bool()))
        val value = Valid(UInt(outLen.W))
    })

    val bitmap = Wire(UInt(depth.W))
    val idx = Wire(UInt(outLen.W))

    bitmap := Cat(io.data.map(addr => (addr.asUInt() === io.key.asUInt() )).reverse)
    idx := OHToUInt((bitmap & io.valid.asUInt()))

    io.value.bits := idx
    io.value.valid := bitmap =/= 0.U
}
class FindEmptyLine(depth: Int, outLen: Int) (implicit val p:Parameters) extends Module {

    val io = IO(new Bundle {
        //        val key = Input(D.cloneType)
        val data = Input(Vec(depth, Bool()))
        val value = Output(UInt(outLen.W))
    })

    val idx = Wire(UInt(outLen.W))

    (0 until depth).foldLeft(when(false.B) {}) {
        case (whenContext, searchIdx) =>
            whenContext.elsewhen(io.data(searchIdx) === false.B) {
                idx := searchIdx.U
            }
    }
    io.value := idx


}

class port[T1 <: Data, T2 <: Data](D: T1, O: T2 )(addrLen: Int)(implicit val p :Parameters) extends Bundle
  with HasCacheAccelParams {

    val in = Flipped(Valid(new Bundle {
        val addr = UInt(addrLen.W)
        val data = D.cloneType
    }))

    val out = Valid(O.cloneType)

    override def cloneType: this.type =  new port(D,C,O)(addrLen).asInstanceOf[this.type]
}


class lockVectorIO (implicit val p :Parameters) extends Bundle
with HasCacheAccelParams {

//    val inAddressWrite = Flipped(Valid(UInt(addrLen.W)))
//
//    val lockCMD = Input(Bool())
//    val isLocked  = Valid(Bool())

    val lock = new portWithCMD(UInt(addrLen.W), Bool(), Bool())(addrLen)
    val unLock = lock.cloneType
}

class lockVector (lockVecDepth :Int = 8)(implicit val p :Parameters) extends Module
with HasCacheAccelParams {

    val LOCK = true
    val UNLOCK = false

    val io = IO (new lockVectorIO())


    val addrVec = RegInit(VecInit(Seq.fill(lockVecDepth)(0.U(addrLen.W))))
    val valid = RegInit(VecInit(Seq.fill(lockVecDepth)(false.B)))


    val bitmapProbe =  Wire(UInt(lockVecDepth.W))
    val bitmapUnlock = Wire(bitmapProbe.cloneType)

    val idxLocking = Wire(UInt(lockVecDepth.W))
    val idxProbe = Wire(idxLocking.cloneType)
    val idxUnlock = Wire(idxLocking.cloneType)

//    io.isLocked.bits := addrVec.map( addr => (io.inAddress.bits === addr)).foldLeft(0.U)({
//        case (res, bit) =>
//         Cat(res,bit)
//    } )

//    def idxFinder(comp: UInt) : UInt = Cat( addrVec.map( addr => (addr === comp)))

    val isLocked = Wire(Bool())

    val probe =   WireInit(io.lock.in.fire() && (io.lock.in.bits.cmd === LOCK.B))
    val write =   WireInit(!isLocked && io.lock.in.fire() && (io.lock.in.bits.cmd === LOCK.B))
    val erase =   WireInit(isLocked && io.unLock.in.fire() && (io.unLock.in.bits.cmd === UNLOCK.B))

    bitmapProbe := (Cat( addrVec.map( addr => (addr === io.lock.in.bits.addr)).reverse))
    bitmapUnlock := (Cat( addrVec.map( addr => (addr === io.unLock.in.bits.addr)).reverse))

    idxProbe := OHToUInt((bitmapProbe & valid.asUInt())) // exactly one bit should be one otherwise OHToUInt won't work
    idxUnlock := OHToUInt((bitmapUnlock & valid.asUInt())) // exactly one bit should be one otherwise OHToUInt won't work

    idxLocking := lockVecDepth.U

    (0 until lockVecDepth).foldLeft(when(false.B) {}) {
        case (whenContext, line) =>
            whenContext.elsewhen(valid(line) === false.B) {
                idxLocking := line.U
            }
    }
    printf(p"idxProbe ${idxProbe}\n")

    isLocked := (bitmapProbe =/= 0.U) && (valid(idxProbe) === true.B)
    io.lock.out.bits := isLocked
    io.lock.out.valid := probe

    io.unLock.out := DontCare

    when(write) {
        addrVec(idxLocking) := io.lock.in.bits.addr
    }

    when(write){
        valid(idxLocking) := true.B
    }

    when(erase){
        valid(idxUnlock) := false.B
    }
}

class stateMemIO (implicit val p: Parameters) extends Bundle
with HasCacheAccelParams{

    val in = Flipped(Valid(new Bundle() {
        val state = new State()
        val addr = UInt(addrLen.W)
        val way  = UInt(wayLen.W)
        val isSet = Bool()
    } ))
    val out = Valid(new State())
}

class stateMem (implicit val p: Parameters) extends Module
  with HasCacheAccelParams{

    val io = IO (new stateMemIO())

    val states = RegInit(VecInit(Seq.fill(nSets*nWays)( (State.default))))

    val isSet = io.in.fire() & io.in.bits.isSet & io.in.bits.way =/= nWays.U
    val isGet = io.in.fire() & !io.in.bits.isSet & io.in.bits.way =/= nWays.U // third one might be unnecessary
    val idx = Wire(UInt(cacheLen.W))

    idx :=  Mux(io.in.bits.way =/= nWays.U, addrToSet(io.in.bits.addr) * nWays.U + io.in.bits.way, 0.U)

    io.out.bits := states(idx)
    io.out.valid := isGet


    when(isSet){
        states(idx) := io.in.bits.state
    }

  }
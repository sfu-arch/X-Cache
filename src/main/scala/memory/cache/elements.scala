package dandelion.memory.cache

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util.{Valid, _}
import dandelion.config.HasAccelShellParams
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

class lockVectorIO (implicit val p :Parameters) extends Bundle
with HasCacheAccelParams {

    val inAddress = Flipped(Valid(UInt(addrLen.W)))
    val lockCMD = Input(Bool())
    val isLocked  = Valid(Bool())

}

class lockVector (lockVecDepth :Int = 8)(implicit val p :Parameters) extends Module
with HasCacheAccelParams {


    val io = IO (new lockVectorIO())
    val addrVec = RegInit(VecInit(Seq.fill(lockVecDepth)(0.U(addrLen.W))))
    val valid = RegInit(VecInit(Seq.fill(lockVecDepth)(false.B)))

    val addrReg = Reg(UInt(addrLen.W))

    val bitmapProbe =  Wire(UInt(lockVecDepth.W))
    val idxLocking = Wire(UInt(lockVecDepth.W))
    val idxProbe = Wire(UInt(lockVecDepth.W))

    when(io.inAddress.fire()){
        addrReg := io.inAddress.bits
    }
//    io.isLocked.bits :=Mux(io.inAddress.fire(), OHToUInt(addrVec.map((_ === io.inAddress.bits))), 0.U)
//    io.isLocked.bits := addrVec.map( addr => (io.inAddress.bits === addr)).foldLeft(0.U)({
//        case (res, bit) =>
//         Cat(res,bit)
//    } )

//    def idxFinder(comp: UInt) : UInt = Cat( addrVec.map( addr => (addr === comp)))

    val isLocked = Wire(Bool())

    val probe =   WireInit(io.inAddress.fire() && (io.lockCMD === true.B))
    val write =   WireInit(!isLocked && io.inAddress.fire() && (io.lockCMD === true.B))
    val erase =   WireInit(isLocked && io.inAddress.fire() && (io.lockCMD === false.B))

    bitmapProbe := (Cat( addrVec.map( addr => (addr === io.inAddress.bits)).reverse))
    idxProbe := OHToUInt((bitmapProbe & valid.asUInt())) // exactly one bit should be one otherwise OHToUInt won't work
    val idxUnlocking = WireInit(idxProbe)

    idxLocking := lockVecDepth.U

    (0 until lockVecDepth).foldLeft(when(false.B) {}) {
        case (whenContext, line) =>
            whenContext.elsewhen(valid(line) === false.B) {
                idxLocking := line.U
            }
    }


    printf(p"checkLock ${idxProbe}\n")

    isLocked := (bitmapProbe =/= 0.U) && (valid(idxProbe) === true.B)
    io.isLocked.bits := isLocked
    io.isLocked.valid := probe

    when(write) {
        addrVec(idxLocking) := io.inAddress.bits
    }

    when(write){
        valid(idxLocking) := true.B
    }.elsewhen(erase){
        valid(idxUnlocking) := false.B
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
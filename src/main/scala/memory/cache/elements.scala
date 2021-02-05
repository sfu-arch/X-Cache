package dandelion.memory.cache

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util.{Valid, _}
import chisel3.util._
import chisel3.util.Cat
import dandelion.interfaces.Action
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
    io.value.valid := (bitmap & io.valid.asUInt()) =/= 0.U
}

class FindEmptyLine(depth: Int, outLen: Int) (implicit val p:Parameters) extends Module {

    val io = IO(new Bundle {
        val data = Input(Vec(depth, Bool()))
        val value = Valid(UInt(outLen.W))
    })

    io.value.valid := false.B

    val idx = WireInit(depth.U((outLen + 1).W))

    (0 until depth).foldLeft(when(false.B) {}) {
        case (whenContext, searchIdx) =>
            whenContext.elsewhen(io.data(searchIdx) === false.B) {
                idx := searchIdx.U
                io.value.valid := true.B
            }
    }
    io.value.bits := idx


}
class portNoAddr[T1 <: Data, T2 <: Data](D: T1, O: T2 )(implicit val p :Parameters) extends Bundle
  with HasCacheAccelParams {

    val in = Flipped(Valid(new Bundle {
        val data = D.cloneType
    }))

    val out = Valid(O.cloneType)

    override def cloneType: this.type =  new portNoAddr(D,O).asInstanceOf[this.type]
}

class port[T1 <: Data, T2 <: Data](D: T1, O: T2 )(addrLen: Int)(implicit val p :Parameters) extends Bundle
  with HasCacheAccelParams {

    val in = Flipped(Valid(new Bundle {
        val addr = UInt(addrLen.W)
        val data = D.cloneType
    }))

    val out = Valid(O.cloneType)

    override def cloneType: this.type =  new port(D,O)(addrLen).asInstanceOf[this.type]
}

class portWithCMD[T1 <: Data, T2 <: Data, T3 <: Data](D: T1, C: T2, O: T3 )(addrLen: Int)(override implicit val p :Parameters)
  extends port(D,O)(addrLen)(p)
with HasCacheAccelParams {

    override val in = Flipped(Valid(new Bundle {
        val addr = UInt(addrLen.W)
        val data = D.cloneType
        val cmd  = C.cloneType
    }))

    override def cloneType: this.type =  new portWithCMD(D,C,O)(addrLen).asInstanceOf[this.type]
}

class paralRegIO [T <: Data](gen: T, size: Int, pDegree: Int, nRead:Int)(implicit val p: Parameters) extends Bundle
with HasCacheAccelParams  {


    val port = Vec(pDegree, new Bundle {
        val write = Flipped(Valid(new Bundle {
            val addr = (UInt(xlen.W))
            val value = (gen.cloneType)
        }))

        val read = new Bundle {
            val in = Flipped(Valid(new Bundle {
                val addr = (UInt(xlen.W))
            }))
            val out = Vec(nRead, Output(gen.cloneType))
        }
    })
}

class paralReg[T <: Data](gen: T, size: Int, pDegree: Int, nRead:Int)(implicit val p: Parameters) extends Module
with HasCacheAccelParams {


    val io = IO( new paralRegIO(gen, size, pDegree, nRead))
    val content = RegInit(VecInit(Seq.fill(size)(0.U))) // @todo should be generic

    val readEn = Wire(Vec(pDegree, Bool()))
    val writeEn = Wire(Vec(pDegree, Bool()))

    for (i <- 0 until pDegree) {
        readEn(i) := io.port(i).read.in.fire()
        writeEn(i) := io.port(i).write.fire()


        when(readEn(i)) {
            io.port(i).read.out := (Cat((0 until nRead).map( j => content(io.port(i).read.in.bits.addr + j.asUInt())).reverse )).asTypeOf(Vec(nRead, gen.cloneType))
        }.otherwise{
            io.port(i).read.out := (Cat((0 until nRead).map( j => 0.U)).asTypeOf(Vec(nRead, gen.cloneType)))
        }

        when(writeEn(i)) {
            content(io.port(i).write.bits.addr) := io.port(i).write.bits.value
        }
    }
}


class lockVectorIO (implicit val p :Parameters) extends Bundle
with HasCacheAccelParams {

    val lock = new portWithCMD(UInt(addrLen.W), Bool(), Bool())(addrLen)
    val unLock = Vec(nParal, lock.cloneType)
}

class lockVector (lockVecDepth :Int = 8)(implicit val p :Parameters) extends Module
with HasCacheAccelParams {

    val LOCK = true
    val UNLOCK = false

    val io = IO (new lockVectorIO())

    val addrVec = RegInit(VecInit(Seq.fill(lockVecDepth)(0.U(addrLen.W))))
    val valid = RegInit(VecInit(Seq.fill(lockVecDepth)(false.B)))

    val bitmapProbe =  Wire(UInt(lockVecDepth.W))

    val idxLocking = Wire(UInt(lockVecDepth.W))
    val idxProbe = Wire(idxLocking.cloneType)
    val idxUnlock = Wire(Vec(nParal, idxLocking.cloneType))

    val finder = for (i <- 0 until nParal) yield{
        val Finder = Module(new Find(UInt(addrLen.W), UInt(addrLen.W), lockVecDepth, log2Ceil(lockVecDepth)))
        Finder
    }
//    io.isLocked.bits := addrVec.map( addr => (io.inAddress.bits === addr)).foldLeft(0.U)({
//        case (res, bit) =>
//         Cat(res,bit)
//    } )

//    def idxFinder(comp: UInt) : UInt = Cat( addrVec.map( addr => (addr === comp)))

    val isLocked = Wire(Bool())

    val probe =   WireInit(io.lock.in.fire() && (io.lock.in.bits.cmd === LOCK.B))
    val write =   WireInit(!isLocked && io.lock.in.fire() && (io.lock.in.bits.cmd === LOCK.B))
    val erase =   Wire(Vec(nParal, Bool()))

    //
    bitmapProbe := (Cat( addrVec.map( addr => (addr === addrNoOffset(io.lock.in.bits.addr))).reverse))
    idxProbe := OHToUInt((bitmapProbe & valid.asUInt())) // exactly one bit should be one otherwise OHToUInt won't work
    idxLocking := lockVecDepth.U


    for (i <- 0 until nParal) {
        erase(i) := (isLocked && io.unLock(i).in.fire() && (io.unLock(i).in.bits.cmd === UNLOCK.B))

        finder(i).io.data := addrVec
        finder(i).io.key := addrNoOffset(io.unLock(i).in.bits.addr)
        finder(i).io.valid := valid

        idxUnlock(i) := finder(i).io.value.bits
        io.unLock(i).out := DontCare

    }
    for (i <- 0 until nParal) {
        when(erase(i) & finder(i).io.value.valid) {
            valid(idxUnlock(i)) := false.B
        }
    }

    (0 until lockVecDepth).foldLeft(when(false.B) {}) {
        case (whenContext, line) =>
            whenContext.elsewhen(valid(line) === false.B) {
                idxLocking := line.U
            }
    }

    isLocked := ((bitmapProbe & valid.asUInt()) =/= 0.U)
    io.lock.out.bits := isLocked
    io.lock.out.valid := probe

    when(write) {
        addrVec(idxLocking) := addrNoOffset(io.lock.in.bits.addr)
    }

    when(write){
        valid(idxLocking) := true.B
    }
}

class stateMemIO (implicit val p: Parameters) extends Bundle
with HasCacheAccelParams{

    val in = Vec ((nParal + 1), Flipped(Valid(new Bundle() {
        val state = new State()
        val addr = UInt(addrLen.W)
        val way  = UInt(wayLen.W)
        val isSet = Bool()
    } )))
    val out = Valid(new State())
}

/* the last in port is used for getting */
class stateMem (implicit val p: Parameters) extends Module
  with HasCacheAccelParams{

    val io = IO (new stateMemIO())

    val states = RegInit(VecInit(Seq.fill(nSets*nWays)( (State.default))))

    val isSet = Wire(Vec(nParal, Bool()))
    val isGet = io.in(nParal).fire() & !io.in(nParal).bits.isSet & io.in(nParal).bits.way =/= nWays.U // third one might be unnecessary

    val idxGet = Wire(UInt(cacheLen.W))
    val idxSet =  Wire(Vec(nParal, UInt(cacheLen.W)))

    for (i <- 0 until nParal) {
        isSet  (i) := io.in(i).fire() & io.in(i).bits.isSet & io.in(i).bits.way =/= nWays.U
        idxSet (i) := Mux(io.in(i).bits.way =/= nWays.U, addrToSet(io.in(i).bits.addr) * nWays.U + io.in(i).bits.way, 0.U)

    }
    for (i <- 0 until nParal) {
        when(isSet(i)) {
            states(idxSet(i)) := io.in(i).bits.state
        }
    }

    idxGet :=  Mux(io.in(nParal).bits.way =/= nWays.U, addrToSet(io.in(nParal).bits.addr) * nWays.U + io.in(nParal).bits.way, 0.U)
    io.out.bits := states(idxGet)
    io.out.valid := isGet

  }

class CacheBundle (implicit p:Parameters) extends AXIAccelBundle
  with HasCacheAccelParams {

    val addr = UInt(addrLen.W)
    val way  = UInt(wayLen.W)
}

class PCBundle (implicit p:Parameters) extends CacheBundle
  with HasCacheAccelParams {

    val pc = UInt(pcLen.W)
    val valid = Bool()
}

class ActionBundle (implicit p:Parameters) extends CacheBundle
  with HasCacheAccelParams {

    val action = new Action()
}

object PCBundle {

    def default (implicit p:Parameters): PCBundle =  {
        val pcContent = Wire(new PCBundle())
        pcContent.addr := 0.U
        pcContent.pc := 0.U
        pcContent.valid := false.B
        pcContent.way := pcContent.nWays.U
        pcContent

    }
}

class PCIO ( implicit val p:Parameters) extends Bundle
with HasCacheAccelParams{

    val write = Flipped(Decoupled(new PCBundle))

    val read = Vec(nParal ,  new portNoAddr(new PCBundle, new PCBundle))
    val isFull = Output(Bool())

}

class PC (implicit val p :Parameters) extends Module
with HasCacheAccelParams{

   val io = IO (new PCIO())

    val pcContent =  RegInit(VecInit(Seq.fill(nParal)(PCBundle.default)))

    val write = WireInit (io.write.fire())

    val findNewLine = Module(new FindEmptyLine(nParal,log2Ceil(nParal + 1)))
    findNewLine.io.data := pcContent.map(_.valid).toVector
    val writeIdx = WireInit(findNewLine.io.value.bits)

    for (i <-  0 until nParal){

        io.read(i).out.bits.way  := pcContent(i).way
        io.read(i).out.bits.addr := pcContent(i).addr
        io.read(i).out.bits.pc   := pcContent(i).pc
        io.read(i).out.bits.valid := pcContent(i).valid
        io.read(i).out.valid     := pcContent(i).valid
    }
    for (i <- 0 until nParal){
        when(!write){
            pcContent(i).pc := io.read(i).in.bits.data.pc
            pcContent(i).valid := io.read(i).in.bits.data.valid
        }
    }

    when( write){
        pcContent(writeIdx).valid := true.B
        pcContent(writeIdx).pc := io.write.bits.pc
        pcContent(writeIdx).addr := io.write.bits.addr
        pcContent(writeIdx).way := io.write.bits.way
    }

    io.write.ready := findNewLine.io.value.valid
    io.isFull := !io.write.ready
}

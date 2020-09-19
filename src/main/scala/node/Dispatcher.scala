
package tensorKernels


import chisel3._
import chisel3.util._
//import config.{COLLEN, Parameters, ROWLEN, XLEN}
import interfaces.CustomDataBundle
import muxes.{Demux, Mux}

class DispatcherIO [T <: Data] (gen: T, nOut: Int)(implicit val p: Parameters) extends Module {
  val io = IO(new Bundle {

//    val start = Input(Bool( ))
//    val done = Output(Bool())


    val in = Input (new CustomDataBundle(gen))
    val out  = Output (Vec (nOut, new CustomDataBundle(gen)))
    val sel = Input (UInt())

//    val rowPtr = Flipped(Decoupled((UInt(p(ROWLEN).W))))
//    val segThreshold = Flipped(Decoupled((UInt(p(XLEN).W))))
//
//    val colSegAddr = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
//    val colSegSize = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
//    val rowSegAddr = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
//    val rowSegSize = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))
//    val destNum = Decoupled(new CustomDataBundle(UInt(p(XLEN).W)))

  })
}

class Dispatcher[ T <: Data] (gen : T, nOut: Int, nBuff: Int) (implicit p: Parameters)
  extends DispatcherIO(gen, nOut )(p) {
  //require(numSeg > 0, "Number of segments must be a positive number")
  val demux = Module (new Demux (new CustomDataBundle(gen) , Nops = nOut))

  val queueOut = for ( i <- 0 until nOut) yield {
    val q = Module (new Queue( new CustomDataBundle(gen), entries = nBuff, pipe = true))
    q
  }
  val queueIn = Module (new Queue (new CustomDataBundle(gen), entries = 1, pipe = true))
  queueIn.io.enq.bits := io.in.data.asTypeOf(gen)
  queueIn.io.deq.bits := demux.io.input.data.asTypeOf(gen)

  for ( i <- 0 until nOut){
    queueOut(i).io.enq.bits := demux.io.outputs(i).data.asTypeOf(gen)
    queueOut(i).io.enq.valid := true.B
    queueOut(i).io.deq.bits := io.out(i).data

  }
}
package memory.mmu

import chisel3._
import chisel3.Module
import config._
import util._
import interfaces._

/**
  * Created by vnaveen0 on 2/6/17.
  */

//TODO : Add interface to memory currently the request is just routed back
// Convert VA to Physical address
// Masking etc


/**
  * Data bundle between dataflow nodes.
  * @note 2 fields
  *  data : U(xlen.W)
  *  predicate : Bool
  *  chosen
  * @param p : implicit
  * @return
  */
class ReadReqDataBundle (NReads: Int) (implicit p: Parameters) extends CoreBundle()(p){
  // Data packet
  val bits = new ReadReq()
  val valid     = Bool()
  val chosen = UInt(log2Ceil(NReads).W)
}
object ReadReqDataBundle {
  def default(NReads: Int)(implicit p: Parameters): ReadReqDataBundle = {
    val wire = Wire(new ReadReqDataBundle(NReads))
    wire.bits      := 0.U
    wire.chosen  := 0.U
    wire.valid     := false.B
    wire
  }
}




class MmuInIO[T <: Data](gen: T, n: Int) extends Bundle {
  val in = Flipped(Decoupled(gen))
  val chosen = Input(UInt(log2Ceil(n).W))
}

class MmuOutIO[T<: Data](gen: T, n:Int ) extends Bundle {
  val out = Decoupled(gen)
  val chosen = Output(UInt(log2Ceil(n).W))
}


abstract class MmuIO(val NReads :Int, NWrites: Int
                    )(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    val readReq   = Flipped(Decoupled(new MmuInIO(new ReadReq(), NReads)))
    val readResp  = Decoupled(new MmuOutIO(new ReadResp(),NReads))

    val writeReq  = Flipped(Decoupled(new MmuInIO(new WriteReq(),NWrites)))
    val writeResp = Decoupled(new MmuOutIO(new WriteResp(), NWrites))

  })
}

class MMU(NReads: Int, NWrites: Int)(implicit p: Parameters) extends MmuIO(NReads, NWrites)(p) {

  //TODO
  //declare registers for req and send appropriate values to responses

  val read_req_R = RegInit(ReadReqDataBundle.default(NReads))

  // Input Handshaking
  io.readReq.ready := ~read_req_R.valid

  when( io.readReq.fire()) {
    read_req_R.valid := true.B
    read_req_R.bits := io.readReq.bits.in
    read_req_R.chosen := io.readReq.bits.chosen
  }

  //Output Handshaking

  io.readResp.bits := read_req_R.bits
  io.readResp := read_req_R.valid

  when(io.readResp.fire()) {
    read_req_R.valid := false.B
  }



}


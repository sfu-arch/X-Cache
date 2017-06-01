package interfaces


import chisel3._
import chisel3.util._
import config._

// Alloca instruction gets only number of bytes need to be allocated
//class AllocaIn(implicit p: Parameters) extends CoreBundle()(p) {
 //val size = UInt((xlen-10).W)
//}

// Alloca instruction returns base address of stack
//class AllocaOut(implicit p: Parameters) extends CoreBundle()(p) {
 //val address = UInt((xlen-10).W)
//}

// Maximum of 16MB Stack Array.
// alloca indicates id of stack object and returns address back.
// Can be any of the 4MB regions
class AllocaReq(implicit p: Parameters) extends CoreBundle()(p) {
 val node = UInt(16.W)
 val size = UInt((xlen-10).W)
}

// ptr is the address returned back to the alloca call.
// Valid and Data flipped.
class AllocaResp(implicit p: Parameters) extends CoreBundle()(p) {
 val ptr    = UInt(xlen.W)
 //val valid  = Bool()
}

// Read interface into Scratchpad stack
//  address: Word aligned address to read from
//  node : dataflow node id to return data to
class ReadReq(implicit p: Parameters) extends CoreBundle()(p) {
 val address  = UInt(xlen.W)
 val node     = UInt(16.W)
}

//  data : data returned from scratchpad
class ReadResp(implicit p: Parameters) extends CoreBundle()(p) {
 val data  = UInt(xlen.W)
 val valid = Bool()
}

// Write interface into scratchpad.
// Word aligned to write to
// Node performing the write 
// Mask indicates which bytes to update.
class WriteReq (implicit p: Parameters) extends CoreBundle()(p) {
  val address = UInt ((xlen-10).W)
  val data    = UInt (xlen.W)
  val mask    = UInt ((xlen/8).W)
  val node    = UInt (16.W)
  val Typ     = UInt (8.W)
}

// Explicitly indicate done flag
class WriteResp (implicit p: Parameters) extends CoreBundle()(p) {
  val done  =  Bool()
  val valid =  Bool()
}


class RvIO(implicit  val p: Parameters) extends Bundle with CoreParams{
  override def cloneType = new RvIO().asInstanceOf[this.type]

  val ready = Input(Bool())
  val valid = Output(Bool())
  val bits  = Output(UInt(xlen.W))
}

class RvAckIO(implicit  val p: Parameters) extends Bundle with CoreParams{
  override def cloneType = new RvAckIO().asInstanceOf[this.type]
  def fire(): Bool = ready && valid

  val ready = Input(Bool())
  val valid = Output(Bool())
}

//class RelayNode output
class RelayOutput(implicit  val p: Parameters) extends Bundle with CoreParams{
  override def cloneType = new RelayOutput().asInstanceOf[this.type]

  val DataNode  = Decoupled(UInt(xlen.W))
  val TokenNode = Input(UInt(tlen.W))
}

class HandShakingIO(val NumPredMemOps :Int = 1, val NumSuccMemOps : Int = 1, val NumOuts : Int = 1)(implicit val p: Parameters) extends Bundle with CoreParams{
  override def cloneType = new HandShakingIO().asInstanceOf[this.type]
  // Generic Pipeline IO
 // Predecessor Ordering
 val PredMemOp = Vec(NumPredMemOps, Flipped(new RvAckIO()))
 // Successor Ordering
 val SuccMemOp = Vec(NumSuccMemOps, new RvAckIO())
 // Output IO
 val Out   = Vec(NumOuts, Decoupled(UInt(xlen.W)))
}

package interfaces


import chisel3._
import chisel3.util._
import config._



// Maximum of 16MB Stack Array.
// alloca indicates id of stack object and returns address back.
// Can be any of the 4MB regions. Size is over provisioned
class AllocaReq(implicit p: Parameters) extends CoreBundle()(p) {
 val node = UInt(16.W)
 val size = UInt(xlen.W)
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

/**
 * Write request to memory
 * @param p [description]
 * @return [description]
 */
// 
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


class RvIO (implicit  val p: Parameters) extends Bundle with CoreParams{
  override def cloneType = new RvIO().asInstanceOf[this.type]

  val ready = Input(Bool())
  val valid = Output(Bool())
  val bits  = Output(UInt(xlen.W))
}

/**
 * RvAckIO
 * 
 * 
 * @param p : implicit
 * @return RvAckIO for ordering and serializing ops in the dataflow
 */
class RvAckIO (implicit  val p: Parameters) extends Bundle with CoreParams{
  override def cloneType = new RvAckIO().asInstanceOf[this.type]
  def fire(): Bool = ready && valid

  val ready = Input(Bool())
  val valid = Output(Bool())
  val token = Output(UInt(32.W))
}

//class RelayNode output
class RelayOutput (implicit p: Parameters) extends CoreBundle()(p){
  override def cloneType = new RelayOutput().asInstanceOf[this.type]

  val DataNode  = Decoupled(UInt(xlen.W))
  val TokenNode = Input(UInt(tlen.W))
}


/**
 * Data bundle between dataflow nodes.
 * @note 2 fields 
 *  data : U(xlen.W) 
 *  predicate : Bool
 * @param p : implicit
 * @return 
 */
class DataIO (implicit p: Parameters) extends CoreBundle()(p){
  // Data packet
  val data = UInt((xlen.W))
  val predicate = Bool()
}

object DataIO {
   def default(implicit p: Parameters): DataIO = {
    val wire = Wire(new DataIO)
    wire.data := 0.U
    wire.predicate := false.B
    wire
  }
}

/**
 * Handshaking IO.
 * @note IO Bundle for Handshaking
 * PredMemOp: Vector of RvAckIOs
 * SuccMemOp: Vector of RvAckIOs
 * Out      : Vector of Outputs
 * @param NumPredMemOps  Number of parents
 * @param NumSuccMemOps  Number of successors
 * @param NumOuts       Number of outputs
 * 
 */
class HandShakingIO(val NumPredMemOps :Int, val NumSuccMemOps : Int, val NumOuts : Int)(implicit p: Parameters) extends CoreBundle()(p){
 // Predecessor Ordering
 val PredMemOp = Vec(NumPredMemOps, Flipped(new RvAckIO()))
 // Successor Ordering
 val SuccMemOp = Vec(NumSuccMemOps, new RvAckIO())
 // Output IO
 val Out   = Vec(NumOuts, Decoupled(UInt(xlen.W)))
}

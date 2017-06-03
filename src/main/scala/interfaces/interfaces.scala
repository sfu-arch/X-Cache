package interfaces


import chisel3._
import chisel3.util._
import config._

/*==============================================================================
=            Notes 
           1. AVOID DECLARING IOs, DECLARE BUNDLES. Create IOs within your node.
           2.             =
==============================================================================*/




// Maximum of 16MB Stack Array.
// alloca indicates id of stack object and returns address back.
// Can be any of the 4MB regions. Size is over provisioned
class AllocaReq(implicit p: Parameters) extends CoreBundle()(p) {
 val node = UInt(glen.W)
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
 val node     = UInt(glen.W)
}

//  data : data returned from scratchpad
class ReadResp(implicit p: Parameters) extends CoreBundle()(p) {
 val data  = UInt(xlen.W)
 val valid = Bool()
}

/**
 * Write request to memory
 * @param p [description]
 * @return  [description]
 */
// 
// Word aligned to write to
// Node performing the write 
// Mask indicates which bytes to update.
class WriteReq (implicit p: Parameters) extends CoreBundle()(p) {
  val address = UInt ((xlen-10).W)
  val data    = UInt (xlen.W)
  val mask    = UInt ((xlen/8).W)
  val node    = UInt (glen.W)
  val Typ     = UInt (8.W)
}

// Explicitly indicate done flag
class WriteResp (implicit p: Parameters) extends CoreBundle()(p) {
  val done  =  Bool()
  val valid =  Bool()
}


class RvIO (implicit p: Parameters) extends CoreBundle()(p) {
  override def cloneType = new RvIO().asInstanceOf[this.type]

  val ready = Input(Bool())
  val valid = Output(Bool())
  val bits  = Output(UInt(xlen.W))
}

/**
 * RvAckIO
 * RvAckIO for ordering and serializing ops in the dataflow
 * @note 3 fields
 * ready, valid and token(32.W)
 * @param p : implicit
 * 
 */
// TO BE RETIRED
class RvAckIO (implicit p: Parameters) extends CoreBundle()(p) {
  override def cloneType = new RvAckIO().asInstanceOf[this.type]
  def fire(): Bool = ready && valid

  val ready = Input(Bool())
  val valid = Output(Bool())
}



// object RvAckIO {
//    def default(implicit p: Parameters): RvAckIO = {
//     val wire = Wire(new RvAckIOIO)
//     wire.valid := false.B
//     wire.ready := false.B
//     wire.predicate := false.B
//     wire
//   }
// }

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
class DataBundle (implicit p: Parameters) extends CoreBundle()(p){
  // Data packet
  val data      = UInt((xlen.W))
  val predicate = Bool()
  val valid     = Bool()
}

object DataBundle {
   def default(implicit p: Parameters): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data      := 0.U
    wire.predicate := false.B
    wire.valid     := false.B
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
class HandShakingIO (val NumPredMemOps :Int, val NumSuccMemOps : Int, val NumOuts : Int)(implicit p: Parameters) extends CoreBundle()(p){
  // Predicate enable
  val enable    = Flipped(Decoupled(Bool()))
  // Predecessor Ordering
  val PredMemOp = Vec(NumPredMemOps, Flipped(new RvAckIO()))
  // Successor Ordering
  val SuccMemOp = Vec(NumSuccMemOps, new RvAckIO())
  // Output IO
  val Out   = Vec(NumOuts, Decoupled(DataBundle.default))
}

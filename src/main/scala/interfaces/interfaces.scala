package interfaces


import chisel3._
import chisel3.util._
import config._
import utility.Constants._

/*==============================================================================
=            Notes
           1. AVOID DECLARING IOs, DECLARE BUNDLES. Create IOs within your node.
           2.             =
==============================================================================*/

trait ValidT extends CoreBundle() {
val valid = Bool ()
}


trait RouteID extends CoreBundle() {
val RouteID = UInt (glen.W)
}

// Maximum of 16MB Stack Array.
// alloca indicates id of stack object and returns address back.
// Can be any of the 4MB regions. Size is over provisioned
class AllocaReq(implicit p: Parameters) extends CoreBundle()(p) {
  val node = UInt(glen.W)
  val size = UInt(xlen.W)
}

// ptr is the address returned back to the alloca call.
// Valid and Data flipped.
class AllocaResp(implicit p: Parameters) extends CoreBundle()(p) with ValidT {
  val ptr = UInt(xlen.W)
  //val valid  = Bool()
}

// Read interface into Scratchpad stack
//  address: Word aligned address to read from
//  node : dataflow node id to return data to
class ReadReq(implicit p: Parameters) 
  extends RouteID {
  val address = UInt(xlen.W)
  val node = UInt(glen.W)
  val Typ = UInt(8.W)

}

object ReadReq {
  def default(implicit p: Parameters): ReadReq = {
    val wire = Wire(new ReadReq)
    wire.address := 0.U
    wire.node := 0.U
    wire.RouteID := 0.U
    wire.Typ := MT_W
    wire
  }
}


//  data : data returned from scratchpad
class ReadResp(implicit p: Parameters)
  extends ValidT
    with RouteID {
  val data = UInt(xlen.W)
}

object ReadResp {
  def default(implicit p: Parameters): ReadResp = {
    val wire = Wire(new ReadResp)
    wire.data := 0.U
    wire.RouteID := 0.U
    wire.valid := false.B
    wire
  }
}

/**
  * Write request to memory
  *
  * @param p [description]
  * @return [description]
  */
//
// Word aligned to write to
// Node performing the write
// Mask indicates which bytes to update.
class WriteReq(implicit p: Parameters) 
extends RouteID {
  val address = UInt((xlen - 10).W)
  val data = UInt(xlen.W)
  val mask = UInt((xlen / 8).W)
  val node = UInt(glen.W)
  val Typ = UInt(8.W)
}

object WriteReq {
  def default(implicit p: Parameters): WriteReq = {
    val wire = Wire(new WriteReq)
    wire.address := 0.U
    wire.data    := 0.U
    wire.mask    := 0.U
    wire.node    := 0.U
    wire.RouteID := 0.U
    wire.Typ     := MT_W
    wire
  }
}

// Explicitly indicate done flag
class WriteResp(implicit p: Parameters)
  extends ValidT
    with RouteID {
  val done = Bool()
}

/**
  * @note Implements ordering between dataflow ops.
  * @param predicate : predicate bit indicating if operations can continue
  *
  */
class AckBundle(implicit p: Parameters) extends CoreBundle()(p) {
  val token = UInt(tlen.W)
  val predicate = Bool()
}

object AckBundle {
  def default(implicit p: Parameters): AckBundle = {
    val wire = Wire(new AckBundle)
    wire.token := 0.U
    wire.predicate := false.B
    wire
  }
}


//class RelayNode output
class RelayOutput(implicit p: Parameters) extends CoreBundle()(p) {
  override def cloneType = new RelayOutput().asInstanceOf[this.type]

  val DataNode = Decoupled(UInt(xlen.W))
  val TokenNode = Input(UInt(tlen.W))
}

/**
  * Data bundle between dataflow nodes.
  *
  * @note 2 fields
  *       data : U(xlen.W)
  *       predicate : Bool
  * @param p : implicit
  * @return
  */
class DataBundle(implicit p: Parameters) extends CoreBundle()(p) {
  // Data packet
  val data = UInt(xlen.W)
  val predicate = Bool()
  val valid = Bool()
}


object DataBundle {
  def default(implicit p: Parameters): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data := 0.U
    wire.predicate := false.B
    wire.valid := false.B
    wire
  }
}

class TypBundle(implicit p: Parameters) extends CoreBundle()(p) {
  // Type Packet
  val data = UInt(Typ_SZ.W)
  val predicate = Bool()
  val valid = Bool()
}


object TypBundle {
  def default(implicit p: Parameters): TypBundle = {
    val wire = Wire(new TypBundle)
    wire.data := 0.U
    wire.predicate := false.B
    wire.valid := false.B
    wire
  }
}
/**
  * Control bundle between branch and
  * basicblock nodes
  *
  * control  : Bool
  */
class ControlBundle(implicit p: Parameters) extends CoreBundle()(p) {
  //Control packet
  val control = Bool()
}

object ControlBundle {
  def default(implicit p: Parameters): ControlBundle = {
    val wire = Wire(new ControlBundle)
    wire.control := false.B
    wire
  }

  def Activate(implicit p : Parameters): ControlBundle = {
    val wire = Wire(new ControlBundle)
    wire.control := true.B
    wire
  }
}



/**
  * Custom Data bundle between dataflow nodes.
  * @param len number of bits
  * @note 2 fields
  *       data : U(len.W)
  *       predicate : Bool
  * @return
  */
class CustomDataBundle(len : Int)(implicit p: Parameters) extends  CoreBundle()(p){
  // Data packet
  val data = UInt(len.W)
  val predicate = Bool()
  val valid = Bool()
}

object CustomDataBundle {
  def default(bitLen : Int)(implicit p : Parameters): CustomDataBundle = {
    val wire = Wire(new CustomDataBundle(len = bitLen))
    wire.data := 0.U
    wire.predicate := false.B
    wire.valid := false.B
    wire
  }
}

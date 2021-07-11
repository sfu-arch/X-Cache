
package memGen.interfaces


import chisel3._
import chisel3.util.{Decoupled, log2Ceil}
import chipsalliance.rocketchip.config._
import utility.Constants._
import memGen.config._
import chipsalliance.rocketchip.config._
import memGen.memory.cache.{ActionList, HasCacheAccelParams}

import scala.collection.immutable.ListMap

/*==============================================================================
=            Notes
           1. AVOID DECLARING IOs, DECLARE BUNDLES. Create IOs within your node.
           2.             =
==============================================================================*/

abstract class CoreBundle (implicit val p:Parameters) extends ParameterizedBundle()(p) with HasCacheAccelParams

trait Event extends CoreBundle{
  val event = UInt(eventLen.W)
}
trait Addr extends CoreBundle{
  val addr = UInt(addrLen.W)
}

class InstBundle (implicit p: Parameters) extends Event with Addr{

  val data = UInt(bBits.W)
  override def cloneType: this.type = new InstBundle().asInstanceOf[this.type]
}

object InstBundle {
  def default(implicit p:Parameters):InstBundle={
    val inst = Wire(new InstBundle())
    inst.addr  := 0.U
    inst.event := 0.U
    inst.data  := 0.U
    inst
  }
}

class Action(implicit p:Parameters) extends CoreBundle{
  val signals = UInt(nSigs.W)
  val actionType = UInt(actionTypeLen.W)
  override def cloneType: this.type = new Action().asInstanceOf[this.type]

}

trait ValidT extends AccelBundle {
  val valid = Bool()
}

class MemReq(implicit p: Parameters) extends AccelBundle()(p) {
  val addr = UInt(addrLen.W)
  val data = UInt(bSize.W)
  val mask = UInt((xlen / 8).W)
  val command = UInt(ActionList.nSigs.W)
  val way = UInt((log2Ceil(accelParams.nways) + 1).W)
  val replaceWay = way.cloneType

    def clone_and_set_tile_id(tile: UInt): MemReq = {
    val wire = Wire(new MemReq())
    wire.addr := this.addr
    wire.data := this.data
    wire.mask := this.mask
    wire.command := this.command
    wire.way := this.way
    wire.replaceWay := this.replaceWay
    wire
  }
}

object MemReq {
  def default(implicit p: Parameters): MemReq = {
    val wire = Wire(new MemReq())
    wire.addr := 0.U
    wire.data := 0.U
    wire.mask := 0.U
    wire.command := 0.U
    wire.way := 0.U
    wire.replaceWay := 0.U
    wire
  }
}

class MemResp(implicit p: Parameters) extends AccelBundle()(p) with ValidT {
  val data = UInt(bSize.W)
  val iswrite = Bool()
  val tile = UInt(xlen.W)
  val way = UInt((log2Ceil(accelParams.nways) + 1).W)

  def clone_and_set_tile_id(tile: UInt): MemResp = {
    val wire = Wire(new MemResp())
    wire.data := this.data
    wire.iswrite := this.iswrite
    wire.way := this.way
    wire.tile := tile
    wire
  }
}

object MemResp {
  def default(implicit p: Parameters): MemResp = {
    val wire = Wire(new MemResp())
    wire.valid := false.B
    wire.data := 0.U
    wire.iswrite := false.B
    wire.tile := 0.U
    wire.way := 0.U
    wire
  }
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
//class DataBundle(implicit p: Parameters) extends ValidT with PredicateT
class DataBundle(implicit p: Parameters) extends AccelBundle {
  // Data packet
  val data = UInt(xlen.W)

}

object DataBundle {

  def apply(data: UInt = 0.U, taskID: UInt = 0.U)(implicit p: Parameters): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data := data
    wire
  }

  def apply(data: UInt, taskID: UInt, predicate: UInt)(implicit p: Parameters): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data := data
    wire
  }


  def default(implicit p: Parameters): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data := 0.U
    wire
  }

  def active(data: UInt = 0.U)(implicit p: Parameters): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data := data
    wire
  }

  def deactivate(data: UInt = 0.U)(implicit p: Parameters): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data := data
    wire
  }
}


/**
  * Control bundle between branch and
  * basicblock nodes
  *
  * control  : Bool
  */
class ControlBundle(implicit p: Parameters) extends AccelBundle()(p) {
  //Control packet
  val control = Bool()

  def asDataBundle(): DataBundle = {
    val wire = Wire(new DataBundle)
    wire.data := this.control.asUInt()
    wire
  }
}

object ControlBundle {
  def default(implicit p: Parameters): ControlBundle = {
    val wire = Wire(new ControlBundle)
    wire.control := false.B
    wire
  }

  def default(control: Bool, task: UInt)(implicit p: Parameters): ControlBundle = {
    val wire = Wire(new ControlBundle)
    wire.control := control
    wire
  }

  def active(taskID: UInt = 0.U)(implicit p: Parameters): ControlBundle = {
    val wire = Wire(new ControlBundle)
    wire.control := true.B
    wire
  }

  def deactivate(taskID: UInt = 0.U)(implicit p: Parameters): ControlBundle = {
    val wire = Wire(new ControlBundle)
    wire.control := false.B
    wire
  }


  def apply(control: Bool = false.B, taskID: UInt = 0.U)(implicit p: Parameters): ControlBundle = {
    val wire = Wire(new ControlBundle)
    wire.control := control
    wire
  }

}


/**
  * Custom Data bundle between dataflow nodes.
  *
  * @note 2 fields
  *       data : U(len.W)
  *       predicate : Bool
  * @return
  */
class CustomDataBundle[T <: Data](gen: T = UInt(32.W))(implicit p: Parameters) extends AccelBundle()(p) {
  // Data packet
  val data = gen.cloneType
  val predicate = Bool()

  override def cloneType: this.type = new CustomDataBundle(gen).asInstanceOf[this.type]
}

object CustomDataBundle {
  def apply[T <: Data](gen: T)(implicit p: Parameters): CustomDataBundle[T] = new CustomDataBundle(gen)

  def default[T <: Data](gen: T)(implicit p: Parameters): CustomDataBundle[T] = {
    val wire = Wire(new CustomDataBundle(gen))
    wire.data := 0.U.asTypeOf(gen)
    wire.predicate := false.B
    wire
  }
}

/**
  * Bundle with variable (parameterizable) types and/or widths
  * VariableData   - bundle of DataBundles of different widths
  * VariableCustom - bundle of completely different types
  * These classes create a record with a configurable set of fields like:
  * "data0"
  * "data1", etc.
  * The bundle fields can either be of type CustomDataBundle[T] (any type) or of
  * DataBundle depending on class used.
  *
  * Examples:
  * var foo = VariableData(List(32, 16, 8))
  * foo("field0") is DataBundle with UInt(32.W)
  * foo("field1") is DataBundle with UInt(16.W)
  * foo("field2") is DataBundle with UInt(8.W)
  * var foo = VariableCustom(List(Int(32.W), UInt(16.W), Bool())
  * foo("field0") is CustomDataBundle with Int(32.W)
  * foo("field1") is CustomDataBundle with UInt(16.W)
  * foo("field2") is CustomDataBundle with Bool()
  *
  */


// Bundle of DataBundles with data width specified by the argTypes parameter
class VariableData(val argTypes: Seq[Int])(implicit p: Parameters) extends Record {

  var elts = Seq.tabulate(argTypes.length) {
    i =>
      s"field$i" -> new DataBundle()(
        p.alterPartial({ case DandelionConfigKey => p(DandelionConfigKey).copy(dataLen = argTypes(i)) })
      )
  }
  override val elements = ListMap(elts map { case (field, elt) => field -> elt.cloneType }: _*)

  def apply(elt: String) = elements(elt)

  override def cloneType = new VariableData(argTypes).asInstanceOf[this.type]
}

// Bundle of Decoupled DataBundles with data width specified by the argTypes parameter
class VariableDecoupledData(val argTypes: Seq[Int])(implicit p: Parameters) extends Record {
  var elts = Seq.tabulate(argTypes.length) {
    i =>
      s"field$i" -> Decoupled(new DataBundle()(
        p.alterPartial({ case DandelionConfigKey => p(DandelionConfigKey).copy(dataLen = argTypes(i)) })
      )
      )
  }
  override val elements = ListMap(elts map { case (field, elt) => field -> elt.cloneType }: _*)

  def apply(elt: String) = elements(elt)

  override def cloneType = new VariableDecoupledData(argTypes).asInstanceOf[this.type]
}

// Bundle of Decoupled DataBundle Vectors. Data width is default. Intended for use on outputs
// of a block (i.e. configurable number of output with configurable number of copies of each output)
class VariableDecoupledVec(val argTypes: Seq[Int])(implicit p: Parameters) extends Record {
  var elts = Seq.tabulate(argTypes.length) {
    i => s"field$i" -> Vec(argTypes(i), Decoupled(new DataBundle()(p)))
  }
  override val elements = ListMap(elts map { case (field, elt) => field -> elt.cloneType }: _*)

  def apply(elt: String) = elements(elt)

  override def cloneType = new VariableDecoupledVec(argTypes).asInstanceOf[this.type]
}

// Call type that wraps an enable and variable DataBundle together
class Call(val argTypes: Seq[Int])(implicit p: Parameters) extends AccelBundle() {
  val enable = new ControlBundle
  val data = new VariableData(argTypes)

  override def cloneType = new Call(argTypes).asInstanceOf[this.type]
}

// Call type that wraps an enable and variable DataBundle together
class CallDCR(val ptrsArgTypes: Seq[Int],
              val valsArgTypes: Seq[Int])(implicit p: Parameters) extends AccelBundle() {
  val enable   = new ControlBundle
  val dataPtrs = new VariableData(ptrsArgTypes)
  val dataVals = new VariableData(valsArgTypes)

  override def cloneType = new CallDCR(ptrsArgTypes, valsArgTypes).asInstanceOf[this.type]
}

// Call type that wraps a decoupled enable and decoupled variable data bundle together
class CallDecoupled(val argTypes: Seq[Int])(implicit p: Parameters) extends AccelBundle() {
  val enable = Decoupled(new ControlBundle)
  val data = new VariableDecoupledData(argTypes)

  override def cloneType = new CallDecoupled(argTypes).asInstanceOf[this.type]
}

// Call type that wraps a decoupled enable and decoupled vector DataBundle together
class CallDecoupledVec(val argTypes: Seq[Int])(implicit p: Parameters) extends AccelBundle() {
  val enable = Decoupled(new ControlBundle)
  val data = new VariableDecoupledVec(argTypes)

  override def cloneType = new CallDecoupledVec(argTypes).asInstanceOf[this.type]
}


// Call type that wraps a decoupled enable and decoupled variable data bundle together
class CallDCRDecoupled(val ptrsArgTypes: Seq[Int],
                       val valsArgTypes: Seq[Int])(implicit p: Parameters) extends AccelBundle() {
  val enable = Decoupled(new ControlBundle)
  val dataPtrs = new VariableDecoupledData(ptrsArgTypes)
  val dataVals = new VariableDecoupledData(valsArgTypes)

  override def cloneType = new CallDCRDecoupled(ptrsArgTypes, valsArgTypes).asInstanceOf[this.type]
}

// Call type that wraps a decoupled enable and decoupled vector DataBundle together
class CallDCRDecoupledVec(val ptrsArgTypes: Seq[Int],
                          val valsArgTypes: Seq[Int])(implicit p: Parameters) extends AccelBundle() {
  val enable = Decoupled(new ControlBundle)
  val dataPtrs = new VariableDecoupledVec(ptrsArgTypes)
  val dataVals = new VariableDecoupledVec(valsArgTypes)

  override def cloneType = new CallDCRDecoupledVec(ptrsArgTypes, valsArgTypes).asInstanceOf[this.type]
}


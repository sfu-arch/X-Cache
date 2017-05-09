package node


import chisel3._
import chisel3.util._
import config._
import util._

class MiniConfig extends Config((site, here, up) => {
    // Core
    case XLEN => 32
    case Trace => true
    // case BuildALU    => (p: Parameters) => Module(new ALUArea()(p))
    // case BuildImmGen => (p: Parameters) => Module(new ImmGenWire()(p))
    // case BuildBrCond => (p: Parameters) => Module(new BrCondArea()(p))
  }
)

case object XLEN extends Field[Int]
case object Trace extends Field[Boolean]

abstract trait CoreParams {
  implicit val p: Parameters
  val xlen = p(XLEN)
}

abstract class CoreBundle(implicit val p: Parameters) extends ParameterizedBundle()(p) with CoreParams


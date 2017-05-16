package config


import chisel3._
import chisel3.util._
import config._
import util._
//import examples._
import regfile._

case object XLEN extends Field[Int]
case object Trace extends Field[Boolean]
case object BuildRFile extends Field[Parameters => AbstractRFile]


abstract trait CoreParams {
  implicit val p: Parameters
  val xlen = p(XLEN)
}

abstract class CoreBundle(implicit val p: Parameters) extends ParameterizedBundle()(p) with CoreParams


class MiniConfig extends Config((site, here, up) => {
    // Core
    case XLEN => 32
    case Trace => true
    case BuildRFile    => (p: Parameters) => Module(new RFile(32)(p))

   
  }
)

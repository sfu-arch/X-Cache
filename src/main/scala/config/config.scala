package config


import chisel3._
import chisel3.util._
import config._
import util._
//import examples._
import regfile._
import  junctions._
import accel._

case object XLEN       extends Field[Int]
case object TLEN       extends Field[Int]
case object GLEN       extends Field[Int]
case object RDMSHRLEN  extends Field[Int]
case object WRMSHRLEN  extends Field[Int]
case object TYPSZ      extends Field[Int]
case object VERBOSITY  extends Field[String]
case object COMPONENTS extends Field[String]
case object TRACE      extends Field[Boolean]
case object BuildRFile extends Field[Parameters => AbstractRFile]


abstract trait CoreParams {
  implicit val p: Parameters
  val xlen    = p(XLEN)
  val tlen    = p(TLEN)
  val glen    = p(GLEN)
  val Typ_SZ  = p(TYPSZ)
  val Beats   = Typ_SZ/xlen
  val rdmshrlen = p(RDMSHRLEN)
  val wrmshrlen = p(WRMSHRLEN)

  // Debugging dumps
  val log     = p(TRACE)
  val verb    = p(VERBOSITY)
  val comp    = p(COMPONENTS)

}

abstract class CoreBundle(implicit val p: Parameters) extends ParameterizedBundle()(p) with CoreParams


class MiniConfig extends Config((site, here, up) => {
    // Core
    case XLEN       => 32
    case TLEN       => 32
    case GLEN       => 16
    // Size of read MSHR table bits 
    case RDMSHRLEN  => 1
    // Size of write MSHR table bits 
    case WRMSHRLEN  => 1
    case TYPSZ      => 32
    case VERBOSITY  => "high"
    case COMPONENTS => "None"
    // Max size of type memory system may see
    case TRACE      => true
    case BuildRFile => (p: Parameters) => Module(new RFile(32)(p))

    //-------------------------
    // Cache
    case NWays => 1 // TODO: set-associative
    case NSets => 256
    case CacheBlockBytes => 4 * (here(XLEN) >> 3) // 4 x 32 bits = 16B
    // NastiIO
    case NastiKey => new NastiParameters(
      idBits   = 12,
      dataBits = 32,
      addrBits = 32)
  }
)

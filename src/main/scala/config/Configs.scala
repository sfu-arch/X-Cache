package dandelion.config


import chisel3._
import regfile._
import dandelion.junctions._
import dandelion.fpu._
import dandelion.accel._

case class AccelParams(
                        dataLen: Int = 32,
                        taskLen: Int = 5,
                        groupLen: Int = 16,
                        mshrLen: Int = 8,
                        typeSize: Int = 64,
                        verbosity: String = "low",
                        components: String = "",
                        printLog: Boolean = false,
                        printCLog: Boolean = false
                      ) {
  var xlen: Int = dataLen
  val tlen: Int = taskLen
  val glen: Int = groupLen
  val TypSZ: Int = typeSize
  //  val beats: Int
  val mshrlen: Int = mshrLen
  val Ftyp = dataLen match {
    case 64 => FType.D
    case 32 => FType.S
    case 16 => FType.H
  }

  //Cache
  val nways = 1 // TODO: set-associative
  val nsets = 256
  val cacheBlockBytes = 4 * (xlen >> 3) // 4 x 64 bits = 32B

  // Debugging dumps
  val log = printLog
  val clog = printCLog
  val verb = verbosity
  val comp = components

}


case object AccelConfig extends Field[AccelParams]

class WithAccelConfig(params : AccelParams = AccelParams ()) extends Config((site, here, up) => {
  // Core
  case AccelConfig => params

  // NastiIO
  case NastiKey => new NastiParameters(
    idBits = 12,
    dataBits = 32,
    addrBits = 33)

}

)

abstract trait HasAccelParams {
  implicit val p: Parameters
  val xlen = p(AccelConfig).xlen
  val tlen = p(AccelConfig).tlen
  val glen = p(AccelConfig).glen
  val mshrlen = p(AccelConfig).mshrlen
  val typesize = p(AccelConfig).typeSize
  val beats = typesize / xlen
  val ftyp = p(AccelConfig).Ftyp
  val log = p(AccelConfig).log
  val clog = p(AccelConfig).clog
  val verb = p(AccelConfig).verb
  val comp = p(AccelConfig).comp

  val nways = p(AccelConfig).nways
  val nsets = p(AccelConfig).nsets
  val cacheBlockBytes = p(AccelConfig).cacheBlockBytes
}

abstract class AccelBundle(implicit val p: Parameters) extends ParameterizedBundle()(p) with HasAccelParams
abstract class AXIAccelBundle(implicit val p: Parameters) extends GenericParameterizedBundle(p) with HasAccelParams



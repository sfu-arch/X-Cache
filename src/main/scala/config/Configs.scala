package dandelion.config

import chisel3._
import chipsalliance.rocketchip.config._
import dandelion.fpu.FType
import dandelion.junctions.{NastiKey, NastiParameters}
import dandelion.util.{DandelionGenericParameterizedBundle, DandelionParameterizedBundle}


trait AccelParams {

  var xlen: Int
  val tlen: Int
  val glen: Int
  val typeSize: Int
  val beats: Int
  val mshrLen: Int
  val fType: FType

  //Cache
  val nways: Int
  val nsets: Int

  def cacheBlockBytes: Int

  // Debugging dumps
  val log: Boolean
  val clog: Boolean
  val verb: String
  val comp: String
}

//case object AccelConfig extends Field[AccelParams]
case object DandelionConfigKey extends Field[DandelionAccelParams]

case class DandelionAccelParams(
                                 dataLen: Int = 32,
                                 taskLen: Int = 5,
                                 groupLen: Int = 16,
                                 mshrLen: Int = 8,
                                 tSize: Int = 64,
                                 verbosity: String = "low",
                                 components: String = "",
                                 printLog: Boolean = false,
                                 printCLog: Boolean = false
                               ) extends AccelParams {
  var xlen: Int = dataLen
  val tlen: Int = taskLen
  val glen: Int = groupLen
  val typeSize: Int = tSize
  val beats: Int = 0
  val mshrlen: Int = mshrLen
  val fType = dataLen match {
    case 64 => FType.D
    case 32 => FType.S
    case 16 => FType.H
  }

  //Cache
  val nways = 1 // TODO: set-associative
  val nsets = 256

  def cacheBlockBytes: Int = 4 * (xlen >> 3) // 4 x 64 bits = 32B

  // Debugging dumps
  val log: Boolean = printLog
  val clog: Boolean = printCLog
  val verb: String = verbosity
  val comp: String = components

}


//trait HasAccelParams extends DandelionAccelParams


class WithAccelConfig(inParams: DandelionAccelParams = DandelionAccelParams()) extends Config((site, here, up) => {
  // Core
  case DandelionConfigKey => inParams

  // NastiIO
  case NastiKey => new NastiParameters(
    idBits = 12,
    dataBits = 32,
    addrBits = 33)

}

)

trait HasAccelParams {
  implicit val p: Parameters
  def accelParams: DandelionAccelParams = p(DandelionConfigKey)
  val xlen = accelParams.xlen
  val tlen = accelParams.tlen
  val glen = accelParams.glen
  val mshrLen = accelParams.mshrLen
  val typeSize = accelParams.typeSize
  val beats = typeSize / xlen
  val fType = accelParams.fType
  val log = accelParams.log
  val clog = accelParams.clog
  val verb = accelParams.verb
  val comp = accelParams.comp

  val nways = accelParams.nways
  val nsets = accelParams.nsets
  val cacheBlockBytes = accelParams.cacheBlockBytes
}

abstract class AccelBundle(implicit val p: Parameters) extends DandelionParameterizedBundle()(p)
  with HasAccelParams

abstract class AXIAccelBundle(implicit val p: Parameters) extends DandelionGenericParameterizedBundle(p)
  with HasAccelParams

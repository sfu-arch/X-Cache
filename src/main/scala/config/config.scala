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
  val xlen: Int = dataLen
  val tlen: Int = taskLen
  val glen: Int = groupLen
  val Typ_SZ: Int = typeSize
  //  val Beats: Int
  val mshrlen: Int = mshrLen
  val Ftyp = dataLen match {
    case 64 => FType.D
    case 32 => FType.S
    case 16 => FType.H
  }

  //Cache
  val nways = 1 // TODO: set-associative
  val nsets = 256
  val CacheBlockBytes = 4 * (xlen >> 3) // 4 x 64 bits = 32B

  // Debugging dumps
  val log = printLog
  val clog = printCLog
  val verb = verbosity
  val comp = components

}

abstract class AccelBundle(implicit val p: Parameters) extends ParameterizedBundle()(p) with AccelParams
abstract class AXIAccelBundle(implicit val p: Parameters) extends GenericParameterizedBundle(p) with AccelParams


case object DandelionAccelConfig extends Field[AccelParams]

class WithAccelConfig extends Config((site, here, up) => {
  // Core
  case DandelionAccelConfig => AccelParams(
    dataLen =  32
  )

  // NastiIO
  //  case NastiKey => new NastiParameters(
  //    idBits = 12,
  //    dataBits = 32,
  //    addrBits = 32)

}

)

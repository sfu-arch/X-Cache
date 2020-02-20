package dandelion.config

import chisel3._
import chipsalliance.rocketchip.config._
import dandelion.fpu.FType
import dandelion.interfaces.axi.AXIParams
import dandelion.junctions.{NastiKey, NastiParameters}
import dandelion.util.{DandelionGenericParameterizedBundle, DandelionParameterizedBundle}


trait AccelParams {

  var xlen: Int
  var ylen: Int
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

/**
  * VCR parameters.
  * These parameters are used on VCR interfaces and modules.
  */
trait VCRParams {
  val nCtrl: Int
  val nECnt: Int
  val nVals: Int
  val nPtrs: Int
  val regBits: Int
}

/**
  * VME parameters.
  * These parameters are used on VME interfaces and modules.
  */
trait VMEParams {
  val nReadClients: Int
  val nWriteClients: Int
}


case class DandelionAccelParams(
                                 dataLen: Int = 32,
                                 addrLen: Int = 32,
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
  var ylen: Int = addrLen
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

/**
  * VCR parameters.
  * These parameters are used on VCR interfaces and modules.
  */
case class DandelionVCRParams(numCtrl: Int = 1,
                              numEvent: Int = 1,
                              numVals: Int = 2,
                              numPtrs: Int = 2,
                              numRets: Int = 0) {
  val nCtrl = numCtrl
  val nECnt = numEvent + numRets
  val nVals = numVals
  val nPtrs = numPtrs
  val regBits = 32
}

/**
  * VME parameters.
  * These parameters are used on VME interfaces and modules.
  */
case class DandelionVMEParams(numRead: Int = 5,
                              numWrite: Int = 1) {
  val nReadClients: Int = numRead
  val nWriteClients: Int = numWrite
  require(nReadClients > 0,
    s"\n\n[VTA] [VMEParams] nReadClients must be larger than 0\n\n")
  require(
    nWriteClients == 1,
    s"\n\n[VTA] [VMEParams] nWriteClients must be 1, only one-write-client support atm\n\n")
}


/** Shell parameters. */
case class ShellParams(
                        val hostParams: AXIParams,
                        val memParams: AXIParams,
                        val vcrParams: DandelionVCRParams,
                        val vmeParams: DandelionVMEParams
                      )


case object DandelionConfigKey extends Field[DandelionAccelParams]

case object VCRKey extends Field[DandelionVCRParams]

case object VMEKey extends Field[DandelionVMEParams]

case object HostParamKey extends Field[AXIParams]

case object MemParamKey extends Field[AXIParams]


class WithAccelConfig(inParams: DandelionAccelParams = DandelionAccelParams())
  extends Config((site, here, up) => {
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
  val ylen = accelParams.ylen
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

trait HasAccelShellParams {
  implicit val p: Parameters

  def vcrParams: DandelionVCRParams = p(VCRKey)

  def vmeParams: DandelionVMEParams = p(VMEKey)

  def hostParams: AXIParams = p(HostParamKey)

  def memParams: AXIParams = p(MemParamKey)

}

abstract class AccelBundle(implicit val p: Parameters) extends DandelionParameterizedBundle()(p)
  with HasAccelParams

abstract class AXIAccelBundle(implicit val p: Parameters) extends DandelionGenericParameterizedBundle(p)
  with HasAccelParams

package dandelion.config

import chisel3._
import chisel3.util.Enum
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
trait DCRParams {
  val nCtrl: Int
  val nECnt: Int
  val nVals: Int
  val nPtrs: Int
  val regBits: Int
}

/**
  * DME parameters.
  * These parameters are used on DME interfaces and modules.
  */
trait DMEParams {
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
                                 printMemLog: Boolean = false,
                                 printCLog: Boolean = false,
                                 cacheNWays: Int = 8,
                                 cacheNSets: Int = 256,
                                 cacheNState:Int = 16,
                                 cacheAddrLen:Int = 32,
                                 nCom:Int = 6
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
  val nways = cacheNWays
  val nsets = cacheNSets
  val nstates = cacheNState
  val addrlen = cacheAddrLen
  val nCommand = nCom
  var comlen:Int = math.ceil(math.log(nCommand)/math.log(2)).toInt

    def cacheBlockBytes: Int = 4 * (xlen >> 3) // 4 x 32 bits = 16B
//   def cacheBlockBytes: Int = xlen >> 3
  // Debugging dumps
  val log: Boolean = printLog
  val memLog: Boolean = printMemLog
  val clog: Boolean = printCLog
  val verb: String = verbosity
  val comp: String = components

}

/**
  * VCR parameters.
  * These parameters are used on VCR interfaces and modules.
  */
case class DandelionDCRParams(numCtrl: Int = 1,
                              numEvent: Int = 1,
                              numVals: Int = 2,
                              numPtrs: Int = 4,
                              numRets: Int = 0) {
  val nCtrl = numCtrl
  val nECnt = numEvent + numRets
  val nVals = numVals
  val nPtrs = numPtrs
  val regBits = 32
}

/**
  * DME parameters.
  * These parameters are used on DME interfaces and modules.
  */
case class DandelionDMEParams(numRead: Int = 1,
                              numWrite: Int = 1) {
  val nReadClients: Int = numRead
  val nWriteClients: Int = numWrite
  require(nReadClients > 0,
    s"\n\n[Dandelion] [DMEParams] nReadClients must be larger than 0\n\n")
  require(
    nWriteClients > 0,
    s"\n\n[Dandelion] [DMEParams] nWriteClients must be larger than 0\n\n")
}


/** Shell parameters. */
case class ShellParams(
                        val hostParams: AXIParams,
                        val memParams: AXIParams,
                        val vcrParams: DandelionDCRParams,
                        val dmeParams: DandelionDMEParams
                      )


case object DandelionConfigKey extends Field[DandelionAccelParams]

case object DCRKey extends Field[DandelionDCRParams]

case object DMEKey extends Field[DandelionDMEParams]

case object HostParamKey extends Field[AXIParams]

case object MemParamKey extends Field[AXIParams]


class WithAccelConfig(inParams: DandelionAccelParams = DandelionAccelParams())
  extends Config((site, here, up) => {
    // Core
    case DandelionConfigKey => inParams
  }
  )


/**
  * Please note that the dLen from WithSimShellConfig should be the same value as
  * AXI -- memParams:dataBits
  *
  * @param vcrParams
  * @param dmeParams
  * @param hostParams
  * @param memParams
  */
class WithTestConfig(vcrParams: DandelionDCRParams = DandelionDCRParams(),
                      dmeParams: DandelionDMEParams = DandelionDMEParams(),
                      hostParams: AXIParams = AXIParams(
                        addrBits = 16, dataBits = 32, idBits = 13, lenBits = 4),
                      memParams: AXIParams = AXIParams(
                        addrBits = 32, dataBits = 32, userBits = 5,
                        lenBits = 4, // limit to 16 beats, instead of 256 beats in AXI4
                        coherent = true),
                      nastiParams: NastiParameters = NastiParameters(dataBits = 32, addrBits = 32, idBits = 13))
  extends Config((site, here, up) => {
    // Core
    case DCRKey => vcrParams
    case DMEKey => dmeParams
    case HostParamKey => hostParams
    case MemParamKey => memParams
    case NastiKey => nastiParams
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
  val memLog = accelParams.memLog
  val clog = accelParams.clog
  val verb = accelParams.verb
  val comp = accelParams.comp
//  val comlen = accelParams.comlen


}

trait HasAccelShellParams {
  implicit val p: Parameters

  def dcrParams: DandelionDCRParams = p(DCRKey)

  def dmeParams: DandelionDMEParams = p(DMEKey)

  def hostParams: AXIParams = p(HostParamKey)

  def memParams: AXIParams = p(MemParamKey)

  def nastiParams: NastiParameters = p(NastiKey)

}

abstract class AccelBundle(implicit val p: Parameters) extends DandelionParameterizedBundle()(p)
  with HasAccelParams

abstract class AXIAccelBundle(implicit val p: Parameters) extends DandelionGenericParameterizedBundle(p)
  with HasAccelParams

package memGen.config

import chisel3._
import chisel3.util.Enum
import chipsalliance.rocketchip.config._
import memGen.fpu.FType
import memGen.interfaces.axi.AXIParams
import memGen.junctions.{NastiKey, NastiParameters}
import memGen.util.{DandelionGenericParameterizedBundle, DandelionParameterizedBundle}


trait AccelParams {

  var xlen: Int
  var ylen: Int
  val tlen: Int
  val glen: Int
  val typeSize: Int
  val beats: Int
  val mshrLen: Int
  val fType: FType


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
                                 dataLen: Int = 64,
                                 addrLen: Int = 32,
                                 taskLen: Int = 5,
                                 groupLen: Int = 16,
                                 mshrLen: Int = 16,
                                 tSize: Int = 64,
                                 verbosity: String = "low",
                                 components: String = "",
                                 printLog: Boolean = false,
                                 printMemLog: Boolean = false,
                                 printCLog: Boolean = false,
                                 cacheNState:Int = 8,
                                 cacheAddrLen:Int = 32,
                                 nSigs:Int = 10,
                                 actionLen: Int = 10 + 2,
                                 cacheNWays: Int = 0,
                                 cacheNSets: Int = 0,
                                 tbeSize:Int = 0,
                                 lockSize: Int = 0,
                                 nparal: Int = 1,
                                 ncache: Int = 0,
                                 nword: Int = 0

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
    case default => FType.D
  }

  //Cache
  val nways = cacheNWays
  val nsets = cacheNSets
  val tbeDepth = tbeSize
  val lockDepth = lockSize
  val nParal = nparal
  val nCache = ncache
  val nWords = nword



  val nstates = cacheNState
  val addrlen = cacheAddrLen

//  val nCommand = nCom
//  var comlen:Int = math.ceil(math.log(nCommand)/math.log(2)).toInt
//  val nSigs = nSigs

    def cacheBlockBytes: Int = nWords * (xlen >> 3) // 4 x 32 bits = 16B
  val cacheBlockBits = cacheBlockBytes << 3
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

class ParameterizedBundle(implicit p: Parameters) extends Bundle {
     override def cloneType = {
         try {
             this.getClass.getConstructors.head.newInstance(p).asInstanceOf[this.type]
           } catch {
          case e: java.lang.IllegalArgumentException =>
                throw new Exception("Unable to use ParamaterizedBundle.cloneType on " +
                 this.getClass + ", probably because " + this.getClass +
                                "() takes more than one argument.  Consider overriding " +
                                "cloneType() on " + this.getClass, e)
      }
     }
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
                        addrBits = 32, dataBits = 64, idBits = 13, lenBits = 4),
                      memParams: AXIParams = AXIParams(
                        addrBits = 32, dataBits = 64, userBits = 5,
                        lenBits = 16, // limit to 16 beats, instead of 256 beats in AXI4
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
  val addrLen = accelParams.addrLen
  val bSize = accelParams.cacheBlockBits
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
  val nSigs = accelParams.nSigs




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

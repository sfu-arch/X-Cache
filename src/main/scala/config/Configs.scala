package memGen.config

import chisel3._
import chisel3.util.Enum
import chipsalliance.rocketchip.config._
import memGen.interfaces.axi.AXIParams
import memGen.junctions.{NastiKey, NastiParameters}
import memGen.memory.cache.{NextRoutine, RoutineROMDasxArray, RoutineROMWalker, RoutineRom, nextRoutineDASX, nextRoutineWalker}
import memGen.util.{DandelionGenericParameterizedBundle, DandelionParameterizedBundle}


trait AccelParams {

  var xlen: Int

  def cacheBlockBytes: Int

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
                                 cacheNState:Int = 8,
                                 cacheAddrLen:Int = 32,
                                 cacheNWays: Int = 0,
                                 cacheNSets: Int = 0,
                                 tbeSize:Int = 0,
                                 lockSize: Int = 0,
                                 nparal: Int = 1,
                                 ncache: Int = 0,
                                 nword: Int = 0,
                                 bm: String =""

                               ) extends AccelParams {

  var xlen: Int = dataLen

  //Cache
  val nways = cacheNWays
  val nsets = cacheNSets
  val tbeDepth = tbeSize
  val lockDepth = lockSize
  val nParal = nparal
  val nCache = ncache
  val nWords = nword

  val benchmark = bm

  val Events = SelectEvents(benchmark)
  val States = SelectStates(benchmark)

  val nstates = cacheNState
  val addrlen = cacheAddrLen

  def cacheBlockBytes: Int = nWords * (xlen >> 3) // 2 x 32 bits = 8B
  val cacheBlockBits = cacheBlockBytes << 3




  def SelectEvents (BM : String): EventList ={
    BM.toLowerCase() match {
      case "walker" =>
        EventsWalker
      case "dasxarray" =>
        EventsDasx
      case "sparch" =>
        EventsSpArch
      case "gp" =>
        EventsGP
      case "syn" =>
        EventsSyn
    }
  }

  def SelectStates (BM : String): StateList ={
    BM.toLowerCase() match {
      case "walker" =>
        StatesWalker
      case "dasxarray" =>
        StatesDasx
      case "sparch" =>
        StatesSpArch
      case "gp" =>
        StatesGP
      case "syn" =>
        StatesSyn
    }
  }





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

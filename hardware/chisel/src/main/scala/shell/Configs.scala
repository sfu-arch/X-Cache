package sim.shell

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces.axi._
import dandelion.junctions._


/** SimDefaultConfig. Shell configuration for simulation */

/**
 * Please note that the dLen from WithSimShellConfig should be the same value as
 * AXI -- memParams:dataBits
 *
 * @param vcrParams
 * @param vmeParams
 * @param hostParams
 * @param memParams
 */
class WithShellConfig(vcrParams: DandelionVCRParams = DandelionVCRParams(),
                      vmeParams: DandelionVMEParams = DandelionVMEParams(),
                      hostParams: AXIParams = AXIParams(
                        addrBits = 16, dataBits = 32, idBits = 13, lenBits = 4),
                      memParams: AXIParams = AXIParams(
                        addrBits = 32, dataBits = 64, userBits = 5,
                        lenBits = 4, // limit to 16 beats, instead of 256 beats in AXI4
                        coherent = true),
                      nastiParams: NastiParameters = NastiParameters(dataBits = 64, addrBits = 32, idBits = 13))
  extends Config((site, here, up) => {
    // Core
    case VCRKey => vcrParams
    case VMEKey => vmeParams
    case HostParamKey => hostParams
    case MemParamKey => memParams
    case NastiKey => nastiParams
  }
  )

class WithSimShellConfig(dLen: Int = 64, pLog: Boolean = false, cLog: Boolean = false)
                        (nPtrs: Int = 2, nVals: Int = 1, nRets: Int = 0, nCtrl: Int = 1, nEvent: Int = 1) extends Config(
  new WithAccelConfig(DandelionAccelParams(dataLen = dLen, printLog = pLog, printCLog = cLog)) ++
    new WithShellConfig(DandelionVCRParams(numCtrl = nCtrl, numEvent = nEvent, numPtrs = nPtrs, numVals = nVals, numRets = nRets)))


/**
 * F1 Config
 * AXI -- memParams:dataBits
 *
 * @param vcrParams
 * @param vmeParams
 * @param hostParams
 * @param memParams
 */
class WithF1Config(vcrParams: DandelionVCRParams = DandelionVCRParams(),
                   vmeParams: DandelionVMEParams = DandelionVMEParams(),
                   hostParams: AXIParams = AXIParams(
                     addrBits = 32, dataBits = 32, idBits = 13, lenBits = 8),
                   memParams: AXIParams = AXIParams(
                     addrBits = 64, dataBits = 512, userBits = 10,
                     lenBits = 8, // limit to 16 beats, instead of 256 beats in AXI4
                     coherent = false),
                   nastiParams: NastiParameters = NastiParameters(dataBits = 64, addrBits = 32, idBits = 13))
  extends Config((site, here, up) => {
    // Core
    case VCRKey => vcrParams
    case VMEKey => vmeParams
    case HostParamKey => hostParams
    case MemParamKey => memParams
    case NastiKey => nastiParams
  }
  )

class WithF1ShellConfig(dLen: Int = 64, pLog: Boolean = false)
                       (nPtrs: Int = 4, nVals: Int = 2, nRets: Int = 1, nCtrl: Int = 1, nEvent: Int = 1) extends Config(
  new WithAccelConfig(DandelionAccelParams(dataLen = dLen, printLog = pLog)) ++
    new WithF1Config(DandelionVCRParams(numCtrl = nCtrl, numEvent = nEvent, numPtrs = nPtrs, numVals = nVals, numRets = nRets)))


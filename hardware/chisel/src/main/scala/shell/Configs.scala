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
 * @param dmeParams
 * @param hostParams
 * @param memParams
 */
class WithShellConfig(vcrParams: DandelionDCRParams = DandelionDCRParams(),
                      dmeParams: DandelionDMEParams = DandelionDMEParams(),
                      hostParams: AXIParams = AXIParams(
                        addrBits = 16, dataBits = 32, idBits = 13, lenBits = 4),
                      memParams: AXIParams = AXIParams(
                        addrBits = 32, dataBits = 64, userBits = 5,
                        lenBits = 4, // limit to 16 beats, instead of 256 beats in AXI4
                        coherent = true),
                      nastiParams: NastiParameters = NastiParameters(dataBits = 64, addrBits = 32, idBits = 13),
                      dbgParams: DebugParams = DebugParams())
  extends Config((site, here, up) => {
    // Core
    case DCRKey => vcrParams
    case DMEKey => dmeParams
    case HostParamKey => hostParams
    case MemParamKey => memParams
    case NastiKey => nastiParams
    case DebugParamKey => dbgParams
  }
  )


/**
 *
 * @param dLen    Accelerator data length
 * @param pLog    Accelerator simulation print logs
 * @param cLog    Accelerator's cache log prints
 * @param nPtrs   Number of Accelerator's input pointers
 * @param nVals   Number of Accelerator's input values
 * @param nRets   Number of Accelerator's returns
 * @param nEvents Number of Accelerator's event counters
 * @param nCtrls  Number of Accelerator's control registers
 *
 *                The input to DME parameters for write is number of
 *                debug nodes plus one for cache
 */
class WithSimShellConfig(dLen: Int = 64, pLog: Boolean = false, cLog: Boolean = false)
                        (nPtrs: Int, nVals: Int, nRets: Int, nEvents: Int, nCtrls: Int) extends Config(
  new WithAccelConfig(DandelionAccelParams(dataLen = dLen, printLog = pLog, printCLog = cLog)) ++
    new WithShellConfig(vcrParams = DandelionDCRParams(numCtrl = nCtrls, numEvent = nEvents, numPtrs = nPtrs, numVals = nVals, numRets = nRets)))

/**
 *
 * @param dLen    Accelerator data length
 * @param pLog    Accelerator simulation print logs
 * @param cLog    Accelerator's cache log prints
 * @param nPtrs   Number of Accelerator's input pointers
 * @param nVals   Number of Accelerator's input values
 * @param nRets   Number of Accelerator's returns
 * @param nEvents Number of Accelerator's event counters
 * @param nCtrls  Number of Accelerator's control registers
 * @param nDbgs   Default value for nDbgs is 0
 *
 *                The input to DME parameters for write is number of
 *                debug nodes plus one for cache
 */
class WithDebugSimShellConfig(dLen: Int = 64, pLog: Boolean = false, cLog: Boolean = false)
                             (nPtrs: Int, nVals: Int, nRets: Int, nEvents: Int, nCtrls: Int, nDbgs: Int = 0) extends Config(
  new WithAccelConfig(DandelionAccelParams(dataLen = dLen, printLog = pLog, printCLog = cLog)) ++
    new WithShellConfig(vcrParams = DandelionDCRParams(numCtrl = nCtrls, numEvent = nEvents, numPtrs = nPtrs + nDbgs, numVals = nVals, numRets = nRets),
      dmeParams = DandelionDMEParams(numRead = 1, numWrite = 1 + nDbgs), dbgParams = DebugParams(len_data = dLen)))


/**
 * F1 Config
 * AXI -- memParams:dataBits
 *
 * @param vcrParams
 * @param dmeParams
 * @param hostParams
 * @param memParams
 */
class WithF1Config(vcrParams: DandelionDCRParams = DandelionDCRParams(),
                     dmeParams: DandelionDMEParams = DandelionDMEParams(),
                     hostParams: AXIParams = AXIParams(
                       addrBits = 16, dataBits = 32, idBits = 13, lenBits = 4),
                     memParams: AXIParams = AXIParams(
                       addrBits = 64, dataBits = 512, userBits = 5,
                       lenBits = 8, // limit to 16 beats, instead of 256 beats in AXI4
                       coherent = true),
                     nastiParams: NastiParameters = NastiParameters(dataBits = 512, addrBits = 64, idBits = 13),
                     dbgParams: DebugParams = DebugParams())
  extends Config((site, here, up) => {
    // Core
    case DCRKey => vcrParams
    case DMEKey => dmeParams
    case HostParamKey => hostParams
    case MemParamKey => memParams
    case NastiKey => nastiParams
    case DebugParamKey => dbgParams
  }
  )

class WithF1ShellConfig(dLen: Int = 64, pLog: Boolean = false, cLog: Boolean = false)
                         (nPtrs: Int, nVals: Int, nRets: Int, nEvents: Int, nCtrls: Int, nDbgs: Int = 0) extends Config(
  new WithAccelConfig(DandelionAccelParams(dataLen = dLen, printLog = pLog, printCLog = cLog)) ++
    new WithF1Config(vcrParams = DandelionDCRParams(numCtrl = nCtrls, numEvent = nEvents, numPtrs = nPtrs + nDbgs, numVals = nVals, numRets = nRets),
      dmeParams = DandelionDMEParams(numRead = 1, numWrite = 1 + nDbgs), dbgParams = DebugParams(len_data = dLen)))


/**
 * F1 Config
 * AXI -- memParams:dataBits
 *
 * @param vcrParams
 * @param dmeParams
 * @param hostParams
 * @param memParams
 */
class WithDe10Config(vcrParams: DandelionDCRParams = DandelionDCRParams(),
                     dmeParams: DandelionDMEParams = DandelionDMEParams(),
                     hostParams: AXIParams = AXIParams(
                       addrBits = 16, dataBits = 32, idBits = 13, lenBits = 4),
                     memParams: AXIParams = AXIParams(
                       addrBits = 32, dataBits = 64, userBits = 5,
                       lenBits = 8, // limit to 16 beats, instead of 256 beats in AXI4
                       coherent = true),
                     nastiParams: NastiParameters = NastiParameters(dataBits = 64, addrBits = 32, idBits = 13),
                     dbgParams: DebugParams = DebugParams())
  extends Config((site, here, up) => {
    // Core
    case DCRKey => vcrParams
    case DMEKey => dmeParams
    case HostParamKey => hostParams
    case MemParamKey => memParams
    case NastiKey => nastiParams
    case DebugParamKey => dbgParams
  }
  )

class WithDe10ShellConfig(dLen: Int = 64, pLog: Boolean = false, cLog: Boolean = false)
                         (nPtrs: Int, nVals: Int, nRets: Int, nEvents: Int, nCtrls: Int, nDbgs: Int = 0) extends Config(
  new WithAccelConfig(DandelionAccelParams(dataLen = dLen, printLog = pLog, printCLog = cLog)) ++
    new WithDe10Config(vcrParams = DandelionDCRParams(numCtrl = nCtrls, numEvent = nEvents, numPtrs = nPtrs + nDbgs, numVals = nVals, numRets = nRets),
      dmeParams = DandelionDMEParams(numRead = 1, numWrite = 1 + nDbgs), dbgParams = DebugParams(len_data = dLen)))


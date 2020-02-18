package sim.shell

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces.axi._


/** SimDefaultConfig. Shell configuration for simulation */
class WithShellConfig(vcrParams: DandelionVCRParams = DandelionVCRParams(),
                      vmeParams: DandelionVMEParams = DandelionVMEParams(),
                      hostParams: AXIParams = AXIParams(
                        addrBits = 16, dataBits = 32, idBits = 13, lenBits = 4),
                      memParams: AXIParams = AXIParams(
                        addrBits = 32, dataBits = 32, userBits = 5,
                        lenBits = 4, // limit to 16 beats, instead of 256 beats in AXI4
                        coherent = true))
  extends Config((site, here, up) => {
    // Core
    case VCRKey => vcrParams
    case VMEKey => vmeParams
    case HostParamKey => hostParams
    case MemParamKey => memParams
  }
  )

class WithSimShellConfig(dLen: Int = 64, pLog: Boolean = false)
                        (nPtrs: Int = 2, nVals: Int = 1, nRets: Int = 0, nCtrl: Int = 1, nEvent: Int = 1) extends Config(
  new WithAccelConfig(DandelionAccelParams(dataLen = dLen, printLog = pLog)) ++
    new WithShellConfig(DandelionVCRParams(numCtrl = nCtrl, numEvent = nEvent, numPtrs = nPtrs, numVals = nVals, numRets = nRets)))


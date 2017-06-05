// See LICENSE for license details.

package accel

import chisel3.Module
import config._
import junctions._


class AcceleratorConfig extends Config((site, here, up) => {
    // Core
    case XLEN => 32
    case TLEN => 32
    case GLEN => 16
    case Trace => true
    
    // Cache
    case NWays => 1 // TODO: set-associative
    case NSets => 256 
    case CacheBlockBytes => 4 * (here(XLEN) >> 3) // 4 x 32 bits = 16B
    // NastiIO
    case NastiKey => new NastiParameters(
      idBits   = 12,
      dataBits = 64,
      addrBits = here(XLEN))
  }
)

class CacheConfig extends Config((site, here, up) => {
      case NSets => 256
}
)


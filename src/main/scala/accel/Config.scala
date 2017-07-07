// See LICENSE for license details.

package accel

import chisel3.Module
import config._
import junctions._


//class AcceleratorConfig extends MiniConfig()

class CacheConfig extends Config((site, here, up) => {
      case NSets => 256
}
)



package memGen.memory.cache


import chisel3._
import chisel3.util._

object States{

  val StateArray = Map(
    (s"I", 0x00),
    (s"S", 0x01),
    (s"M",  0x02),
    (s"ID", 0x03),
    (s"IS", 0x04),
    (s"IM", 0x05),
    (s"D", 0x06)

  )
  val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)


}


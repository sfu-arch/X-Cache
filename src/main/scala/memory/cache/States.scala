
package memGen.config


import chisel3._
import chisel3.util._

object StatesLDST{

  val StateArray = Map(
    (s"I", 0x00),
    (s"S", 0x01),
    (s"M",  0x02),
    (s"ID", 0x03),
    (s"IS", 0x04),
    (s"IM", 0x05),
    (s"D", 0x06),
    (s"E", 0x07)


  )
  val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)

}

object StatesWalker{

  val StateArray = Map(
    (s"I", 0x00),
    (s"ID", 0x01),
    (s"V", 0x02)
  )
  val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)

  val ValidState = StateArray("V")   // for hit
  val InvalidState = StateArray("I") // for miss

}



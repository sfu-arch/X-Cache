
package memGen.config


import chisel3._
import chisel3.util._

abstract class StateList{

  val StateArray = Map[String,Int] ()
  val stateLen = 0
  val ValidState = 0
  val InvalidState = 0

}
object StatesLDST extends  StateList {

  override val StateArray = Map(
    (s"I", 0x00),
    (s"S", 0x01),
    (s"M",  0x02),
    (s"ID", 0x03),
    (s"IS", 0x04),
    (s"IM", 0x05),
    (s"D", 0x06),
    (s"E", 0x07)


  )
  override  val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)

}

object StatesWalker extends StateList{

  override val StateArray = Map(
    (s"I", 0x00),
    (s"IB", 0x01),
    (s"ID", 0x02),
    (s"V", 0x03)
  )
  override val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)

  override val ValidState = StateArray("V")   // for hit
  override val InvalidState = StateArray("I") // for miss

}

object StatesDasx extends StateList{

  override val StateArray = Map(
    (s"I", 0x00),
    (s"IC", 0x01),
    (s"V", 0x02),
    (s"E", 0x03),
    (s"IP", 0x04)

  )
  override val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)
  override val ValidState = StateArray("V")   // for hit
  override val InvalidState = StateArray("I") // for miss

}

object StatesGP extends StateList{

  override val StateArray = Map(
    (s"I", 0x00),
    (s"IU", 0x01),
    (s"V", 0x02),

  )
  override val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)
  override val ValidState = StateArray("V")   // for hit
  override val InvalidState = StateArray("I") // for miss

}
object StatesSpArch extends StateList{

  override val StateArray = Map(
    (s"I", 0x00),
    (s"IC", 0x01),
    (s"V", 0x02),
    (s"IP", 0x04)

  )
  override val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)
  override val ValidState = StateArray("V")   // for hit
  override val InvalidState = StateArray("I") // for miss

}


}
object StatesSyn extends StateList{

  override val StateArray = Map(
    (s"I", 0x00),
    (s"IV", 0x01),
    (s"VD", 0x02),
    (s"D", 0x03)

  )
  override val stateLen =  if (StateArray.size == 1 ) 1 else log2Ceil(StateArray.size)
  override val ValidState = StateArray("D")   // for hit
  override val InvalidState = StateArray("I") // for miss

}

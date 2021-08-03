package memGen.config
import chisel3._
import chisel3.util._


abstract class EventList  {
  val EventArray = Map[String,Int] ()
  val eventLen = 0
  val HitEvent = 0
}
object EventsLDST extends EventList {

//  val EventArray = Map[String, Int]()
  override val EventArray = Map(
                        (s"LOAD", 0x00),
                        (s"STORE", 0x01),
    (s"DATA",0x02),
    (s"NOP", 0x03)

  )
  override val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

}

object EventsWalker extends EventList {

  override val EventArray = Map(
    (s"FIND", 0x00),
    (s"DATA",0x01),
    (s"NOP", 0x02)

  )
  override val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

  override val HitEvent = EventArray("FIND")

}

object EventsDasx extends EventList {

  override val EventArray = Map(
    (s"LOAD", 0x00),
    (s"STORE", 0x01),
    (s"DATA",0x02),
    (s"COLLECT", 0x03),
    (s"PREP",0x04),
    (s"NOP", 0x5)

  )
  override val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

  override val HitEvent = EventArray("LOAD")

}







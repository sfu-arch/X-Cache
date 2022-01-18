package memGen.config
import chisel3._
import chisel3.util._


abstract class EventList  {
  val EventArray = Map[String,Int] ()
  val eventLen = 0
  val HitEvent = Array[Int]()
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

  override val HitEvent = Array(EventArray("FIND"))

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

  override val HitEvent = Array(EventArray("LOAD"), EventArray("PREP"), EventArray("COLLECT"))

}
object EventsGP extends EventList {

  override val EventArray = Map(
    (s"INIT", 0x00),
    (s"UPDATE", 0x01),
    (s"DATA",0x02),

  )
  override val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

  override val HitEvent = Array(EventArray("INIT"),EventArray("UPDATE"))

}
object EventsSpArch extends EventList {

  override val EventArray = Map(
    (s"COLLECT", 0x00),
    (s"PREP", 0x01),
    (s"DATA",0x02),

  )
  override val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

  override val HitEvent = Array(EventArray("PREP"), EventArray("COLLECT"))

}

object EventsSyn extends EventList {

  override val EventArray = Map(
    (s"IND", 0x00),
    (s"DRAM", 0x01),
    (s"DATA",0x02),
    (s"PROD",0x03),
    (s"AGEN",0x05),


  )
  override val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

  override val HitEvent = Array(EventArray("IND"))

}







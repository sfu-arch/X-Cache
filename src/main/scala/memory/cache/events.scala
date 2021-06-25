package memGen.config
import chisel3._
import chisel3.util._



object EventsLDST {

//  val EventArray = Map[String, Int]()
  val EventArray = Map(
                        (s"LOAD", 0x00),
                        (s"STORE", 0x01),
    (s"DATA",0x02),
    (s"NOP", 0x03)

  )
  val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

}

object EventsWalker {

  val EventArray = Map(
    (s"FIND", 0x00),
    (s"DATA",0x01),
    (s"NOP", 0x02)

  )
  val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)

}







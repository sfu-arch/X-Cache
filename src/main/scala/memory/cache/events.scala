package memGen.memory.cache
import chisel3._
import chisel3.util._


object Events {



//  val EventArray = Map[String, Int]()

  val EventArray = Map(
                        (s"LOAD", 0x00),
                        (s"STORE", 0x01),
    (s"DATA",0x02),
    (s"NOP", 0x03)

  )

  val eventLen =  if (EventArray.size == 1 ) 1 else log2Ceil(EventArray.size)




  //  def LOAD_I = 0x00
//  def LOAD_M = 0x01

}





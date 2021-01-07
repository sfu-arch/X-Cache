package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

object ActionList {

    val actions = Map[String, Bits](

        ("Allocate" ,   "b10000110100".U) ,
        ("ReadInt" ,    "b10100000000".U),
        ("Probe",       "b10000001011".U),
        ("Aloc" ,       "b10000110100".U),
        ("DAloc",       "b10001000000".U),
        ("WrInt",       "b10010000000".U),
        ("RdInt",       "b10100000000".U),
        ("DataRQ",      "b11000000000".U),
        ("Nop",         "b10000000000".U),
        ("AllocateTBE", "b00000000001".U),
        ("DeallocateTBE","b00000000010".U)/*,*/
//        ("ReadTBE",     "b00000000011".U)





    )


    // fill the input table
}

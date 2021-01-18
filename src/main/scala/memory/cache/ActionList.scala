package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

object ActionList {

    val actionLen = 12
    val actions = Map[String, Bits](

        ("Allocate" ,   "b010000110100".U(actionLen.W)) ,
        ("ReadInt" ,    "b010100000000".U),
        ("Probe",       "b010000001011".U),
        ("Aloc" ,       "b010000110100".U),
        ("DAloc",       "b010001000000".U),
        ("WrInt",       "b010010000000".U),
        ("RdInt",       "b010100000000".U),
        ("DataRQ",      "b011000000000".U),
        ("Nop",         "b010000000000".U),
        ("AllocateTBE", "b000000000001".U),
        ("DeallocateTBE", "b000000000010".U),
          ("SetState",   "b110000000000".U)
//        ("ReadTBE",     "b00000000011".U)





    )


    // fill the input table
}

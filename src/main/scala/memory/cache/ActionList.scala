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

        ("Allocate" ,   "b000000110100".U(actionLen.W)) ,
        ("ReadInt" ,    "b000100000000".U),
        ("Probe",       "b000000001011".U),
        ("Aloc" ,       "b000000110100".U),
        ("DAloc",       "b000001000000".U),
        ("WrInt",       "b000010000000".U),
        ("RdInt",       "b000100000000".U),
        ("DataRQ",      "b001000000000".U),
        ("Nop",         "b000000000000".U),
        ("AllocateTBE", "b010000000001".U),
        ("DeallocateTBE", "b010000000010".U),
          ("SetState",   "b110000000000".U)
//        ("ReadTBE",     "b00000000011".U)





    )


    // fill the input table
}

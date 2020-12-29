package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

object ActionList {

    val actions = Map[String, Bits](

        ("Allocate" ,"b0000110100".U) ,
        ("ReadInt" , "b0100000000".U)




    )


    // fill the input table
}

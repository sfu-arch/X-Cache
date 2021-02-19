package memGen.memory.message

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.util._
import memGen.memory.cache._
import memGen.interfaces._
import memGen.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile

trait MessageParams extends HasAccelParams with HasCacheAccelParams{
    val srcLen  = 3
    val dstLen  = 3
    val instLen = 8

}

class IntraNodeBundle (implicit val p : Parameters) extends Bundle with
MessageParams{
    val addr = UInt(addrLen.W)
    val inst = UInt(instLen.W)
    val data = UInt (dataLen.W)

}

class MessageBundle (implicit p :Parameters) extends IntraNodeBundle()(p){
    val src = UInt(srcLen.W)
    val dst = UInt(dstLen.W)
}





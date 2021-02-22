package memGen.memory.message

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.util._
import memGen.interfaces._
import memGen.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile


class PacketizerIO [T<: IntraNodeBundle](MB : T = IntraNodeBundle) (implicit val p: Parameters) extends Bundle with MessageParams {

    val input = Flipped(Valid(MB.cloneType))
    val output = Valid(MB.cloneType)
}


class Packetizer [T <: IntraNodeBundle](MB : T = IntraNodeBundle ) (implicit val p: Parameters) extends Module with MessageParams{
    val io = IO(new PacketizerIO(MB))

    io.output.bits <> io.input.bits

}

class Depacketizer [T <: IntraNodeBundle](MB : T) (implicit val p: Parameters) extends Module with MessageParams{
    val io = IO(new PacketizerIO(MB))

    io.output.bits <> io.input.bits

}


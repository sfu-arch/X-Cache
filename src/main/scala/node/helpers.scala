package node

import chisel3._
import chisel3.util._
import utility.Constants._

object WriteOut {

  def apply(dataout: UInt, sel: UInt, address: UInt): UInt = {
    val byte_alignment = address(1, 0)
    val bit_alignment = Cat(byte_alignment, UInt(0, 3))
    val out = dataout << bit_alignment
    printf("%d", out)
    return out
  }
}

object ReadByteMask {
  def apply(sel: UInt, address: UInt): UInt = {
    val wordmask = Typ2ByteMask(sel)
    val alignment = address(1, 0)
    val mask = (wordmask << alignment)
    return mask
  }
}

object Typ2BitMask {
  def apply(sel: UInt): UInt = {
    val mask = Mux(sel === MT_H.asUInt || sel === MT_HU.asUInt, Fill(16, 1.U),
      Mux(sel === MT_B.asUInt || sel === MT_BU.asUInt, Fill(8, 1.U),
        Mux(sel === MT_W.asUInt || sel === MT_WU.asUInt, Fill(32, 1.U),
          Fill(64, 1.U))))
    return mask
  }
}


object ReadBitMask {
  def apply(sel: UInt, address: UInt): UInt = {
    val wordmask = Typ2BitMask(sel)
    val alignment = Cat(address(1, 0), UInt(0, 3))
    val mask = (wordmask << alignment)
    return mask
  }
}


object Typ2ByteMask {
  def apply(sel: UInt): UInt = {
    val mask = Mux(sel === MT_H.asUInt || sel === MT_HU.asUInt, Fill(2, 1.U),
      Mux(sel === MT_B.asUInt || sel === MT_BU.asUInt, Fill(1, 1.U),
        Mux(sel === MT_W.asUInt || sel === MT_WU.asUInt, Fill(4, 1.U),
          Fill(8, 1.U))))
    return mask
  }

  /**
    * @todo fix the xlen parameter for the apply value
    */
  //object Data2Sign
  //{
  //def apply(data: Bits, typ: Bits) : Bits =
  //{
  ////@todo check whether casting Bits to UInt doesn't introduce bug
  //val out = Mux(typ.asUInt === MT_H,  Cat(Fill(xlen-16, data(15)),  data(15,0)),
  //Mux(typ.asUInt === MT_HU, Cat(Fill(xlen-16, UInt(0x0)), data(15,0)),
  //Mux(typ.asUInt === MT_B,  Cat(Fill(xlen-8, data(7)),    data(7,0)),
  //Mux(typ.asUInt === MT_BU, Cat(Fill(xlen-8, UInt(0x0)), data(7,0)),
  //data(xlen-1,0)))))

  //return out
  //}
  //}

}

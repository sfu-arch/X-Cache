// See LICENSE for license details.

package regfile

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}


class RegFileIO [T<: Data] (gen: T) extends Bundle {
  val raddr1 = Input(UInt(5.W))
  val rdata1 = Output(gen)
  val wen    = Input(Bool())
  val waddr  = Input(UInt(5.W))
  val wdata  = Input(gen)

  override def cloneType: this.type = new RegFileIO(gen).asInstanceOf[this.type]


}

class RegFile[T <: Data](gen: T, size: Int) extends Module {
  val io = IO(new RegFileIO(gen))
  val regs = SyncReadMem(size, gen)
  io.rdata1 := Mux(io.raddr1.orR, regs(io.raddr1), 0.U)
  // io.rdata2 := Mux(io.raddr2.orR, regs(io.raddr2), 0.U)
  when(io.wen & io.waddr.orR) {
    regs(io.waddr) := io.wdata
  }
}

// class ScratchPadIO (Size: Int, Xlen: Int) extends RegFileIO(UInt(width=Xlen))  {
  

//    // Specifies the stack object that needs to be accessed and the byte within it
//   // There are a total of 32 stack objects possible. 2^5.
//   val SID  = Input(UInt(width = log2Ceil(Size)))

//   // This specifies the byte address within the scratchpad
// }


// // Size. Maximum number of stack objects of a certain size.
// // Entries. Number of entries in 

// class ScratchPad(Size: Int, Entries: Int, Width: Int) extends Module {

//   val io = IO(new ScratchPadIO(Size = Size, Xlen = Width))

//   // This is a single scratchpad. Need to specify a vector of these. 
//   // Parameterize the number of entries
//   // Alignment we will have to be careful as we want to specify appropriate byte masks
  


//   // [AUTO] This declaration has to be auto generated based on number of stack objects
  
//   val RegFileIOs = Vec.fill(Size)(Module(new RegFile(UInt(width = Width), Size)).io)

//   val SIDx = io.SID

//   RegFileIOs(SIDx).raddr1  := io.raddr1
//   io.rdata1 := RegFileIOs(SIDx).rdata1 
//   RegFileIOs(SIDx).wen  := io.wen
//   RegFileIOs(SIDx).waddr  := io.waddr
//   RegFileIOs(SIDx).wdata  := io.wdata

//    // printf(p"\n RegFileIOs = $RegFileIOs") 
  
//  }

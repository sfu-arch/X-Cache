// See LICENSE for license details.

package regfile

import chisel3._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._
import chipsalliance.rocketchip.config._





// Tester.
class InRegTester(regFile: InputRegFile)(implicit p: Parameters) extends PeekPokeTester(regFile)  {

  poke(regFile.io.Data.ready, 0.U)
  println(s"regOutput: ${peek(regFile.io.Data)}")
  step(1)

  poke(regFile.io.Data.ready, 1.U)
  println(s"regOutput: ${peek(regFile.io.Data)}")
  step(1)

  println(s"regOutput: ${peek(regFile.io.Data)}")
  step(1)

  println(s"regOutput: ${peek(regFile.io.Data)}")
  step(1)

  println(s"regOutput: ${peek(regFile.io.Data)}")

 }




class regFileTests extends  FlatSpec with Matchers {
   implicit val p = new WithAccelConfig
  it should "InReg tester" in {
     chisel3.iotesters.Driver(() => new InputRegFile(Array(3.U, 4.U, 5.U))(p)) { c =>
       new InRegTester(c)
     } should be(true)
   }
 }



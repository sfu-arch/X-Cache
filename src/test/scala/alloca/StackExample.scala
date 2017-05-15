// See LICENSE for license details.

package alloca

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

//import node._
//import dataflow._
import alloca._

import muxes._
import config._
import util._
import interfaces._





// Tester.
 class StackTester(stack: newCentralStack)(implicit p: config.Parameters) extends PeekPokeTester(stack)  {

     poke(stack.io.AllocaIn(0).valid,1.U)
     poke(stack.io.AllocaIn(0).bits.size,10.U)
     poke(stack.io.AllocaIn(0).bits.node,1.U)

     poke(stack.io.AllocaIn(1).valid,1.U)
     poke(stack.io.AllocaIn(1).bits.size,20.U)
     poke(stack.io.AllocaIn(0).bits.node,5.U)

    
     println(s"io.in.bits idx:0 : io.out.bits: ${peek(stack.io.AllocaIn(0))} io.out: ${peek(stack.io.AllocaOut(0))}, valid: ${peek(stack.io.Valids(0))}")
     println(s"io.in.bits idx:1 : io.out.bits: ${peek(stack.io.AllocaIn(1))} io.out: ${peek(stack.io.AllocaOut(1))}, valid: ${peek(stack.io.Valids(1))}")
     step(1)

     poke(stack.io.AllocaIn(0).valid,0.U)
     println(s"io.in.bits idx:0 : io.out.bits: ${peek(stack.io.AllocaIn(0))} io.out: ${peek(stack.io.AllocaOut(0))}, valid: ${peek(stack.io.Valids(0))}")
     println(s"io.in.bits idx:1 : io.out.bits: ${peek(stack.io.AllocaIn(1))} io.out: ${peek(stack.io.AllocaOut(1))}, valid: ${peek(stack.io.Valids(1))}")
     step(1)

     poke(stack.io.AllocaIn(1).valid,0.U)
     println(s"io.in.bits idx:0 : io.out.bits: ${peek(stack.io.AllocaIn(0))} io.out: ${peek(stack.io.AllocaOut(0))}, valid: ${peek(stack.io.Valids(0))}")
     println(s"io.in.bits idx:1 : io.out.bits: ${peek(stack.io.AllocaIn(1))} io.out: ${peek(stack.io.AllocaOut(1))}, valid: ${peek(stack.io.Valids(1))}")
     step(1)

     println(s"io.in.bits idx:0 : io.out.bits: ${peek(stack.io.AllocaIn(0))} io.out: ${peek(stack.io.AllocaOut(0))}, valid: ${peek(stack.io.Valids(0))}")
     println(s"io.in.bits idx:1 : io.out.bits: ${peek(stack.io.AllocaIn(1))} io.out: ${peek(stack.io.AllocaOut(1))}, valid: ${peek(stack.io.Valids(1))}")
 }




 class StackTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Alloca tester" in {
     chisel3.iotesters.Driver(() => new newCentralStack) { c =>
       new StackTester(c)
     } should be(true)
   }
 }

//class DataflowTests (DF: DataFlow) extends PeekPokeTester(DF)  {


  //for( t <- 0 to 1 ){

    //val rdn1 = rnd.nextInt(32)
    //val rdn2 = rnd.nextInt(32)
    //val rdn3 = rnd.nextInt(32)
    //println(s"In1: ${rdn1}, In2: ${rdn2}, In3: ${rdn3}")

    //poke(DF.io.In1.bits,rdn1)
    //poke(DF.io.In2.bits,rdn2)
    //poke(DF.io.In3.bits,rdn3)
    //poke(DF.io.In1.valid,0)
    //poke(DF.io.In2.valid,0)
    //poke(DF.io.In3.valid,0)
    //poke(DF.io.Out1.ready,1)

    //step(1)
    //println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${rdn1+rdn2-rdn3}")

    //poke(DF.io.In1.valid,1)
    //poke(DF.io.In2.valid,1)
    //poke(DF.io.In3.valid,1)

    //step(1)
    //println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    //step(1)

    //println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    //step(1)
    //println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    //step(1)
    //println(s"Output is: ${peek(DF.io.Out1.bits)}, Valid bit: ${peek(DF.io.Out1.valid)} Res should be ${3+4-5}")
    
    //expect(DF.io.Out1.bits, rdn1+rdn2-rdn3)
  //}

//}


//class DecoupledNodeTester extends FlatSpec with Matchers {
  //behavior of "DecoupledNodeSpec"

  //val xLen = 32

  //it should "compute dataflow {(3+4) * 5} excellently" in {
    //chisel3.iotesters.Driver(() => new DataFlow(xLen)) { c =>
      //new DataflowTests(c)
      //} should be(true)
    //}
  //}

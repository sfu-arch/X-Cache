package dataflow.fuse

/**
  * Created by vnaveen0 on 26/6/17.
  */

import chisel3._
import chisel3.util._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, FreeSpec, Matchers}
import node._
import dataflow._
import muxes._
import config._
import util._
import interfaces._
import dataflow._
import firrtl_interpreter.InterpreterOptions




// Tester.
class computeF04PTester(df: ComputeFuse04PDF)
                  (implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.data0.bits.data, 4.U)
  poke(df.io.data0.valid, false.B)
  poke(df.io.data0.bits.predicate, true.B)

  poke(df.io.data1.bits.data, 5.U)
  poke(df.io.data1.valid, false.B)
  poke(df.io.data1.bits.predicate, true.B)

  poke(df.io.data2.bits.data, 6.U)
  poke(df.io.data2.valid, false.B)
  poke(df.io.data2.bits.predicate, true.B)

  poke(df.io.data3.bits.data, 7.U)
  poke(df.io.data3.valid, false.B)
  poke(df.io.data3.bits.predicate, true.B)

  poke(df.io.data4.bits.data, 1.U)
  poke(df.io.data4.valid, false.B)
  poke(df.io.data4.bits.predicate, true.B)

  poke(df.io.data5.bits.data, 1.U)
  poke(df.io.data5.valid, false.B)
  poke(df.io.data5.bits.predicate, true.B)

  poke(df.io.enable.bits.control, false.B)
  poke(df.io.enable.valid, false.B)

  poke(df.io.dataOut.ready, true.B)
  println(s"Output: ${peek(df.io.dataOut)}\n")

  step(1)

  poke(df.io.data0.valid, true.B)
  poke(df.io.data1.valid, true.B)
  poke(df.io.data2.valid, true.B)
  poke(df.io.data3.valid, true.B)
  poke(df.io.data4.valid, true.B)
  poke(df.io.data5.valid, true.B)
  poke(df.io.enable.bits.control, true.B)
  poke(df.io.enable.valid, true.B)

  println(s"Output: ${peek(df.io.dataOut)}\n")

  for(i <- 0 until 20){
    println(s"Output: ${peek(df.io.dataOut)}")

    if(peek(df.io.dataOut.valid) == 1)
      poke(df.io.dataOut.ready, false.B)


    if((peek(df.io.dataOut.valid) == 1))
      println(s"Finish: ${i}\n")

    println(s"t: ${i}\n ------------------------")
    step(1)
  }
}

class ComputeF04PTests extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Not fuse tester" in {
    chisel3.iotesters.Driver.execute(Array("--backend-name", "verilator", "--target-dir", "test_run_dir"),
      () => new ComputeFuse04PDF()) {
      c => new computeF04PTester(c)
    } should be(true)
  }

}


/**
  * @note different example of test cases
  */
//class VerilogTests extends  FlatSpec with Matchers {
//   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//   it should "Dataflow tester" in {
//      chisel3.iotesters.Driver.execute(Array("--backend-name", "firrtl", "--target-dir", "test_run_dir"), () => new Compute01DF()){
//       c => new compute01Tester(c)
//     } should be(true)
//   }

//   it should "run firrtl via direct option configuration" in {
//     val manager = new TesterOptionsManager {
//       testerOptions = TesterOptions(backendName = "firrtl")
//       interpreterOptions = InterpreterOptions(writeVCD = true, setVerbose = true)
//     }
//
//     val args = Array("--backend-name", "verilator", "--fint-write-vcd")
//     chisel3.iotesters.Driver.execute(args, () => new Compute01DF()) {
//       c => new compute01Tester(c)
//     }
//   }
// }

//object Compute01DFVerilog extends App {
  //implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  //chisel3.iotesters.Driver.execute(args, () => new Compute01DF()(p))
  //{ c => new compute01Tester(c)  }
//}

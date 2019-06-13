package dandelion.dataflow.tests

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._





// Tester.
class Add01Tester(df: Add01DF)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  println(s"t:-2 -------------------------")
  poke(df.io.Data0.bits.data, 5.U)
  poke(df.io.Data0.valid, false.B)
  poke(df.io.Data0.bits.predicate, false.B)

  poke(df.io.Data1.bits.data, 4.U)
  poke(df.io.Data1.valid, false.B)
  poke(df.io.Data1.bits.predicate, false.B)


  step(1)

  println(s"t:-1 -------------------------\n")
  poke(df.io.Data0.valid, true.B)
  poke(df.io.Data0.bits.predicate, true.B)

  poke(df.io.Data1.valid, true.B)
  poke(df.io.Data1.bits.predicate, true.B)

    poke(df.io.result.ready, true.B)
    poke(df.io.pred.ready, true.B)

  for( i <- 0 until 10){

    if(i== 0) {
      poke(df.io.start, true.B)
    }
    else {
      poke(df.io.start, false.B)
    }


    println(s"Output: ${peek(df.io.result)}\n")
    println(s"Pred  : ${peek(df.io.pred)}\n")


    println(s"Output: ${peek(df.io.result)}\n")
    println(s"Pred  : ${peek(df.io.pred)}\n")
    step(1)

    println(s"t:${i} -------------------------\n")
  }

}




class Add01Tests extends  FlatSpec with Matchers {
   implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow sample 01 tester" in {
     chisel3.iotesters.Driver(() => new Add01DF()(p)) {
       c => new Add01Tester(c)
     } should be(true)
   }
 }




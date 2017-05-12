package utils

/**
  * Created by nvedula on 9/5/17.
  */

import chisel3.iotesters.Driver
import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}
//import TutorialRunner
import amin._

object Launcher {

  val xLen = 32
  val examples = Map(

//      "Adder" -> { (backendName: String) =>
//        Driver(() => new FullAdder(), backendName) {
//          (c) => new FullAdderTests(c)
//        }
//      },
//    "Node" -> { (backendName: String) =>
//        Driver(() => new Node(32), backendName) {
//          (c) => new NodeTests(c)
//        }
//      },
//
     "StoreNode" -> { (backendName: String) =>
        Driver(() => new StoreNode(xLen), backendName) {
          (c) => new StoreNodeTests(c)
        }
      }

  )
  def main(args: Array[String]): Unit = {
    TutorialRunner(examples, args)
  }
}


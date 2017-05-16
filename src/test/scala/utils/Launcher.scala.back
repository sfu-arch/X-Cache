package utils

/**
  * Created by nvedula on 9/5/17.
  */

import chisel3.iotesters.Driver
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import dummy_nodes._
import amin._
import mem_dataflow._

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
    },
    "MemDataFlow" -> { (backendName: String) =>
      Driver(() => new MemDataFlow(xLen), backendName) {
        (c) => new LoadNodeTests1(c)
      }
    },
    "LoadNode" -> { (backendname: String) =>
      Driver(() => new LoadNode(32), backendname) {
        (c) => new LoadNodeTests(c)
      }
    },

    "DummyGEP" -> { (backendname: String) =>
      Driver(() => new DummyGEP(32), backendname) {
        (c) => new DummyGEPTests(c)
      }
    }



  )
  def main(args: Array[String]): Unit = {
    TutorialRunner(examples, args)
  }
}


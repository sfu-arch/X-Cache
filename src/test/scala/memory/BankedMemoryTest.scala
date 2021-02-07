//package dandelion.memory.cache
//
//import java.io.PrintWriter
//import java.io.File
//
//import chisel3._
//import chisel3.tester._
//import chisel3.Module
//import util._
//import dandelion.interfaces._
//import dandelion.memory._
//import dandelion.dataflow.cache.test_cache01DF
//import chipsalliance.rocketchip.config._
//import dandelion.config._
//import chisel3.iotesters._
//import dandelion.memory.cache.{HasCacheAccelParams, Gem5Cache}
//import org.scalatest.{FlatSpec, Matchers,FreeSpec}
//
//
//abstract class MemBankTest extends FreeSpec with ChiselScalatestTester {
//
//  implicit val p: Parameters
//
//  def accelParams: DandelionAccelParams = p(DandelionConfigKey)
//    "MemBankSimulation should work" in {
//    test(new MemBank(0.U)()) { dut =>
//      // write values into memory
//      dut.io.isRead.poke(false.B)
//      for (bank <- 0 until dut.io.banks) {
//        for (address <- 0 until dut.io.bankDepth) {
//          dut.clock.step()
//          dut.io.bank.poke(bank.U)
//          dut.io.address.poke(address.U)
//          dut.io.inputValue.poke((bank * 1000 + address).U)
//        }
//      }
//
//      dut.clock.step()
//      // read values out of memory banks
//      dut.io.isRead.poke(true.B)
//      for (bank <- 0 until dut.io.banks) {
//        for (address <- 0 until dut.io.bankDepth) {
//          dut.clock.step()
//          dut.io.bank.poke(bank.U)
//          dut.io.address.poke(address.U)
//          println(f"Bank $bank%3d at address $address%3d contains ${dut.io.outputValue.peek().litValue()}%6d")
//        }
//      }
//    }
//  }
//}
package junctions

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FreeSpec, Matchers}
import scala.collection.immutable.ListMap
import interfaces._
import config.MiniConfig

class CombineCustomTester(c: CombineCustom) extends PeekPokeTester(c) {

  step(1)
  poke(c.io.In("field0").valid, false.B)
  poke(c.io.In("field0").bits.data, 0.U)
  poke(c.io.In("field1").valid, false.B)
  poke(c.io.In("field1").bits.data, 0.U)

  poke(c.io.In("field2").valid, false.B)
  poke(c.io.In("field2").bits.data, 0.U)
  expect(c.io.Out.bits("field0").data, false.B)
  expect(c.io.Out.bits("field1").data, 0.U)
  expect(c.io.Out.bits("field2").data, 0.U)
  step(1)
  poke(c.io.In("field0").valid, true.B)
  poke(c.io.In("field0").bits.data, true.B)
  poke(c.io.In("field1").valid, true.B)
  poke(c.io.In("field1").bits.data, 1.U)
  poke(c.io.In("field2").valid, true.B)
  poke(c.io.In("field2").bits.data, 2.U)
  step(1)
  poke(c.io.In("field0").valid, false.B)
  poke(c.io.In("field0").bits.data, false.B)
  poke(c.io.In("field1").valid, false.B)
  poke(c.io.In("field1").bits.data, 0.U)
  poke(c.io.In("field2").valid, false.B)
  poke(c.io.In("field2").bits.data, 0.U)
  expect(c.io.Out.valid, true.B)
  expect(c.io.Out.bits("field0").data, true.B)
  expect(c.io.Out.bits("field1").data, 1.U)
  expect(c.io.Out.bits("field2").data, 2.U)
  step(1)
  poke(c.io.Out.ready, true.B)
  step(1)
  expect(c.io.Out.valid, false.B)
  expect(c.io.Out.bits("field0").data, true.B)
  expect(c.io.Out.bits("field1").data, 1.U)
  expect(c.io.Out.bits("field2").data, 2.U)

  for (i <- 0 until 10) {
    step(1)
  }

}


class CombineDataTester(c: CombineData) extends PeekPokeTester(c) {

  step(1)
  poke(c.io.In("field0").valid, false.B)
  poke(c.io.In("field0").bits.data, 0.U)
  poke(c.io.In("field1").valid, false.B)
  poke(c.io.In("field1").bits.data, 0.U)

  poke(c.io.In("field2").valid, false.B)
  poke(c.io.In("field2").bits.data, 0.U)
  expect(c.io.Out.bits("field0").data, false.B)
  expect(c.io.Out.bits("field1").data, 0.U)
  expect(c.io.Out.bits("field2").data, 0.U)
  step(1)
  poke(c.io.In("field0").valid, true.B)
  poke(c.io.In("field0").bits.data, true.B)
  poke(c.io.In("field1").valid, true.B)
  poke(c.io.In("field1").bits.data, 1.U)
  poke(c.io.In("field2").valid, true.B)
  poke(c.io.In("field2").bits.data, 2.U)
  step(1)
  poke(c.io.In("field0").valid, false.B)
  poke(c.io.In("field0").bits.data, false.B)
  poke(c.io.In("field1").valid, false.B)
  poke(c.io.In("field1").bits.data, 0.U)
  poke(c.io.In("field2").valid, false.B)
  poke(c.io.In("field2").bits.data, 0.U)
  expect(c.io.Out.valid, true.B)
  expect(c.io.Out.bits("field0").data, true.B)
  expect(c.io.Out.bits("field1").data, 1.U)
  expect(c.io.Out.bits("field2").data, 2.U)
  step(1)
  poke(c.io.Out.ready, true.B)
  step(1)
  expect(c.io.Out.valid, false.B)
  expect(c.io.Out.bits("field0").data, true.B)
  expect(c.io.Out.bits("field1").data, 1.U)
  expect(c.io.Out.bits("field2").data, 2.U)

  for (i <- 0 until 10) {
    step(1)
  }

}

class CombineDecoupledTests extends FreeSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  "Combine discrete CustomDataBundle I/O into single Decoupled IO bundle" in {
    chisel3.iotesters.Driver.execute(
      Array(//"-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001")
      , () => new CombineCustom(List(UInt(32.W), UInt(16.W), UInt(8.W)))) { c =>
      new CombineCustomTester(c)
    } should be (true)
  }
  "Combine discrete DataBundle I/O into single Decoupled IO bundle" in {
    chisel3.iotesters.Driver.execute(
      Array(//"-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001")
      , () => new CombineData(List(32, 16, 8))) { c =>
      new CombineDataTester(c)
    } should be (true)
  }
}

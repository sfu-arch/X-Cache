// See LICENSE for license details.

package accel

import java.io.{File, FileWriter}

import accel.coredf._
import config._

object Main extends App {
  val dir = new File("accel_rtl") ; dir.mkdirs
  implicit val p = config.Parameters.root((new AccelConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(new Core())))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
  firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}

object Main2 extends App {
  val dir = new File("RTL/TestCacheDF") ; dir.mkdirs
  implicit val p = config.Parameters.root((new AccelConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(new TestCacheDF())))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
  firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}

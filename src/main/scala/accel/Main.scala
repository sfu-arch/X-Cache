// See LICENSE for license details.

package accel

import java.io.{File, FileWriter}

import accel.coredf.TestCore
import config._

object Main extends App {
  val dir = new File(args(0)) ; dir.mkdirs
  implicit val p = config.Parameters.root((new AcceleratorConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(new  Core())))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
  firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}

/*
object Main extends App {
  val dir = new File(args(0)) ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Cache()))
  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
  firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}
*/

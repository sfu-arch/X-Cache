// See LICENSE for license details.

package accel

import java.io.{File, FileWriter}

import accel.coredf._
import config._
import dataflow._

object CoreMain extends App {
  val dir = new File("accel_rtl") ; dir.mkdirs
  implicit val p = config.Parameters.root((new AccelConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(3,3,new Core(3,3))))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
  firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}

object TestCacheMain extends App {
  val dir = new File("RTL/TestCacheDF") ; dir.mkdirs
  implicit val p = config.Parameters.root((new AccelConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(3,3,new TestCacheDF(3,3))))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
  firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}

object FilterDFMain extends App {
  val dir = new File("RTL/FilterDF") ; dir.mkdirs
  implicit val p = config.Parameters.root((new AccelConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(18,3,new FilterDFCore(18,3))))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
  firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}

object VecFilterDFMain extends App {
  val dir = new File("RTL/VecFilterDF") ; dir.mkdirs
  implicit val p = config.Parameters.root((new VecFilterDFConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(6,4,new VecFilterDFCore(6,4))))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
    firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}

object VecFilterNoKernDFMain extends App {
  val dir = new File("RTL/VecFilterNoKernDF") ; dir.mkdirs
  implicit val p = config.Parameters.root((new VecFilterDFConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new Accelerator(12,4,new VecFilterNoKernDFCore(12,4))))

  val verilog = new FileWriter(new File(dir, s"${chirrtl.main}.v"))
  new firrtl.VerilogCompiler compile (
    firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm), verilog)
  verilog.close
}


package test

import chisel3.MultiIOModule
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.generator._
import dandelion.accel.{DandelionAccelDCRModule, DandelionAccelDebugModule, DandelionAccelModule}


/**
 * This is an abstract class that contains all the dandelion test cases
 *
 * @param p
 * @tparam T
 */
abstract class DandelionTestDCRAccel[T <: DandelionAccelDCRModule](implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams

object DandelionTestDCRAccel {

  /**
   * Please make sure to add your new test case in the apply function
   *
   * @param testName
   * @param p
   * @return
   */
  def apply(testName: String)(implicit p: Parameters): DandelionAccelDCRModule = {
    testName match {
      case "test04" => new test04DF()
      case "test05" => new test05DF()
      case "test06" => new test06DF()
      case "test07" => new test07DF()
      case "test08" => new test08DF()
      case "test09" => new test09DF()
      case _ => throw new Exception(s"[EXCEPTION] The accel's name is not defined -- " +
        s"Please check the accel name you have passed: ${testName}")
    }
  }

}


/**
 * This is an abstract class that contains all the dandelion test cases
 *
 * @param p
 * @tparam T
 */
abstract class DandelionTestDebugDCRAccel[T <: DandelionAccelDebugModule](implicit val p: Parameters) extends MultiIOModule with HasAccelShellParams

object DandelionTestDebugDCRAccel {

  /**
   * Please make sure to add your new test case in the apply function
   *
   * @param testName
   * @param p
   * @return
   */
  def apply(testName: String, numDbgs: Int, boreIDsList: Seq[Int])(implicit p: Parameters): (() => DandelionAccelDCRModule, () => DandelionAccelDebugModule) = {
    testName match {

      //TestCases
      case "test01" => (() => new test01DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test02" => (() => new test02DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test03" => (() => new test03DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test04" => (() => new test04DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test05" => (() => new test05DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test06" => (() => new test06DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test07" => (() => new test07DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test08" => (() => new test08DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test09" => (() => new test09DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test10" => (() => new test10RootDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test11" => (() => new test11RootDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test12" => (() => new test12RootDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test13" => (() => new test13RootDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test14" => (() => new test14RootDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test15" => (() => new test15RootDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test16" => (() => new test16RootDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))

      //Real examples
      case "relu" => (() => new reluDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "stencil" => (() => new stencilDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "saxpy" => (() => new saxpyDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "conv2d" => (() => new conv2dDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "conv2dUnroll" => (() => new conv2dUnrollDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "spmvCRS" => (() => new spmvCRSDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "fftStrided" => (() => new fftDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "bbgemm" => (() => new bbgemmDF(), () => new DebugBufferWriters(numDbgs, boreIDsList))


      case _ => throw new Exception(s"[EXCEPTION] The accel's name is not defined -- " +
        s"Please check the accel name you have passed: ${testName}")
    }
  }

}

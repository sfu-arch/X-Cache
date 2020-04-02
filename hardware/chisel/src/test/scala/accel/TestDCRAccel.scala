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
      case "test04" => (() => new test04DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case "test05" => (() => new test05DF(), () => new DebugBufferWriters(numDbgs, boreIDsList))
      case _ => throw new Exception(s"[EXCEPTION] The accel's name is not defined -- " +
        s"Please check the accel name you have passed: ${testName}")
    }
  }

}

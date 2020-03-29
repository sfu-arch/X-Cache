package dandelion.accel

import chipsalliance.rocketchip.config.Parameters
import chisel3.{Data, Flipped, Module}
import chisel3.util.{Decoupled, Valid}
import dandelion.config.{AccelBundle, HasAccelParams}
import dandelion.interfaces.{CallDCR, Call, MemReq, MemResp}


/**
 * Global definition for dandelion accelerators
 * @param ArgsIn
 * @param Returns
 * @param p
 * @tparam T
 */
class DandelionAccelIO[T <: Data](val ArgsIn: Seq[Int],
                                  val Returns: Seq[Int])(implicit p: Parameters)
  extends AccelBundle()(p) {
  val in = Flipped(Decoupled(new Call(ArgsIn)))
  val MemResp = Flipped(Valid(new MemResp))
  val MemReq = Decoupled(new MemReq)
  val out = Decoupled(new Call(Returns))

  override def cloneType = new DandelionAccelIO(ArgsIn, Returns)(p).asInstanceOf[this.type]

}


abstract class DandelionAccelModule(val ArgsIn: Seq[Int],
                                    val Returns: Seq[Int])(implicit val p: Parameters) extends Module with HasAccelParams {
  override lazy val io = IO(new DandelionAccelIO(ArgsIn, Returns))
}


/**
 * Global definition for dandelion accelerators
 *
 * @param PtrsIn
 * @param ValsIn
 * @param RetsOut
 * @param p
 * @tparam T
 */
class DandelionAccelDCRIO[T <: Data](val PtrsIn: Seq[Int],
                                  val ValsIn: Seq[Int],
                                  val RetsOut: Seq[Int])(implicit p: Parameters)
  extends AccelBundle()(p) {
  val in = Flipped(Decoupled(new CallDCR(PtrsIn, ValsIn)))
  val MemResp = Flipped(Valid(new MemResp))
  val MemReq = Decoupled(new MemReq)
  val out = Decoupled(new Call(RetsOut))

  override def cloneType = new DandelionAccelDCRIO(PtrsIn, ValsIn, RetsOut)(p).asInstanceOf[this.type]

}


abstract class DandelionAccelDCRModule(val PtrsIn: Seq[Int],
                                    val ValsIn: Seq[Int],
                                    val RetsOut: Seq[Int])(implicit val p: Parameters) extends Module with HasAccelParams {
  override lazy val io = IO(new DandelionAccelDCRIO(PtrsIn, ValsIn, RetsOut))
}

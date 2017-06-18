/*==================================================
=            Errata MT_D accesses not supported     =
===================================================*/

package utility

import chisel3._
/**
 * @todo   MT_D double access not supported yet.
 * 
 */
trait MemoryOpConstants 
{
   val MT_X  = 0.U(3.W)
   val MT_B  = 1.U(3.W)
   val MT_H  = 2.U(3.W)
   val MT_W  = 3.U(3.W)
   val MT_D  = 4.U(3.W)
   val MT_BU = 5.U(3.W)
   val MT_HU = 6.U(3.W)
   val MT_WU = 7.U(3.W)
   // Maximum size of access type
   val MT_MAX_SIZE = 2
}

  object Constants  extends MemoryOpConstants 
{


}
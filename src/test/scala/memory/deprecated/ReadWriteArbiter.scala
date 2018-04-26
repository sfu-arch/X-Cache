package memory.deprecated

/**
  * Created by vnaveen0 on 9/7/17.
  */

import chisel3._
import chisel3.testers._
import chisel3.util._
import config._
import memory.ReadWriteArbiter
/*
class ReadWriteArbiterTests(c: => ReadWriteArbiter) (implicit p: config.Parameters)
  extends BasicTester {

  val dut = Module(c)
  // -- IO ---
  //    val ReadCacheReq = Decoupled(new CacheReq)
  //    val ReadCacheResp = Flipped(Valid(new CacheResp))
  //    val WriteCacheReq = Decoupled(new CacheReq)
  //    val WriteCacheResp = Flipped(Valid(new CacheResp))
  //    val CacheReq = Decoupled(new CacheReq)
  //    val CacheResp = Flipped(Valid(new CacheResp))

  val sIdle :: sReq :: sDone :: Nil = Enum(3)
  val state = RegInit(sIdle)
  val isSt_R = RegInit(false.B)
  val tag_R = RegInit(0.U)

  val (testCnt, testDone) = Counter(sIdle === sReq, 2)
  // -- --------------------------------------------------
  dut.io.CacheReq.ready := true.B

  switch(state) {
    is(sIdle) {

      printf("\n -----------------------------------------\n ")
      printf(p" state == Idle ")

      //Store Req

      when(dut.io.WriteCacheReq.ready) {

        // dont use printf s inside when
        printf(p" WriteCacheReq Ready ")
        dut.io.WriteCacheReq.valid        := true.B
        dut.io.WriteCacheReq.bits.addr    := 23.U
        dut.io.WriteCacheReq.bits.data    := 4.U
        dut.io.WriteCacheReq.bits.iswrite := true.B
        dut.io.WriteCacheReq.bits.tag     := 1.U
      }
        .otherwise {
          dut.io.WriteCacheReq.valid := false.B
        }



      when(dut.io.ReadCacheReq.ready) {

        printf(" ReadCacheReq Ready ")
        //Load Req
        dut.io.ReadCacheReq.valid := true.B
        dut.io.ReadCacheReq.bits.addr := 123.U
        dut.io.ReadCacheReq.bits.data := 432232424.U //garbage
        dut.io.ReadCacheReq.bits.iswrite := false.B
        dut.io.ReadCacheReq.bits.tag := 1.U

      }
        .otherwise {
          dut.io.ReadCacheReq.valid  := false.B
        }


      when(dut.io.ReadCacheReq.ready || dut.io.WriteCacheReq.ready) {
        state := sReq
        }

      when(dut.io.CacheReq.valid) {
        isSt_R := dut.io.CacheReq.bits.iswrite
        tag_R := dut.io.CacheReq.bits.tag
      }

      printf("\n io.CacheReq.valid : %x ", dut.io.CacheReq.valid)
      printf(" io.CacheReq.isSt : %x ", dut.io.CacheReq.bits.iswrite)
      printf(" io.CacheReq.tag : %x \n", dut.io.CacheReq.bits.tag)

      printf("ReadCacheReq.ready: %x ", dut.io.ReadCacheReq.ready)
      printf("ReadCacheReq.valid: %x \n", dut.io.ReadCacheReq.valid)

      printf("WriteCacheReq.ready: %x  ", dut.io.WriteCacheReq.ready)
      printf("WriteCacheReq.valid: %x \n ", dut.io.WriteCacheReq.valid)

    }

    is(sReq) {
      println(" \n state == sReq \n ")
//      dut.io.CacheResp.data     := 34.U
//      dut.io.CacheResp.isSt     := isSt_R // will depend on Ld/St
//      dut.io.CacheResp.valid    := true.B
//      dut.io.CacheResp.tag      := tag_R
//      state := sDone
    }

    is(sDone) {

      println(" \n state == sDone \n ")
      state := sIdle
      stop();
      stop
    }
  }

//        printf(" ^^^^^^^^^^ io.CacheReq.valid : ")

//  when(state === sIdle) {
//    when(c.io.CacheReq.valid) {
//      printf(" ^^^^^^^^^^ io.CacheReq.valid : ")
//    }
//    printf(" ^^^^^^^^^^ io.CacheReq.valid : 0x%x.", dut.io.CacheReq.valid)
//    printf(s"ReadCacheReq.ready: %x ", dut.io.ReadCacheReq.ready)
//    printf(s"ReadCacheReq.valid: %x ", dut.io.ReadCacheReq.valid)
//
//    printf(s"WriteCacheReq.ready: %x  ", dut.io.WriteCacheReq.ready)
//    printf(s"WriteCacheReq.valid: %x  ", dut.io.WriteCacheReq.valid)
//
//
//    printf(s" ^^^^^   ^^^ io.CacheReq.Valid: %x ", dut.io.CacheReq.valid)
//    printf(s"io.CacheReq.addr: %x ", dut.io.CacheReq.bits.addr)
//    printf(s"io.CacheReq.data: %x ", dut.io.CacheReq.bits.data)
//    printf(s"io.CacheReq.tag:  %x ", dut.io.CacheReq.bits.tag)
//    printf(s"io.CacheReq.mask: %x ", dut.io.CacheReq.bits.mask)
//    printf(s"io.CacheReq.iswrite: %x ", dut.io.CacheReq.bits.iswrite)
//
//
//    printf(s" Read/Write Response ----------")
//    printf(s"io.write_resp.valid: %x ", dut.io.WriteCacheResp.valid)
//    printf(s"io.read_resp.valid:  %x ", dut.io.ReadCacheResp.valid)
//  }

//  when(testDone) {
//    stop();
//    stop()
//  }

}

////  switch(state) {
////    is(sIdle) {
////
////      printf(" state == Idle ")
//////      when(dut.io.CacheReq.ready) {
//////
//////        printf(" CacheReq Ready ")
//////        //Load Req
//////        dut.io.ReadCacheReq.valid         := true.B
//////        dut.io.ReadCacheReq.bits.addr     := 123.U
//////        dut.io.ReadCacheReq.bits.data     := 432232424.U //garbage
//////        dut.io.ReadCacheReq.bits.iswrite  := false.B
//////        dut.io.ReadCacheReq.bits.tag      := 1.U
//////
//////        //Store Req
//////        dut.io.WriteCacheReq.valid        := true.B
//////        dut.io.WriteCacheReq.bits.addr    := 23.U
//////        dut.io.WriteCacheReq.bits.data    := 4.U
//////        dut.io.WriteCacheReq.bits.iswrite := true.B
//////        dut.io.WriteCacheReq.bits.tag     := 1.U
//////        state := sReq
//////      }
//////        .otherwise {
//////          dut.io.ReadCacheReq.valid  := false.B
//////          dut.io.WriteCacheReq.valid := false.B
//////        }
////
////    }
////
////    is(sReq) {
////      println(s" ^^^^  ^^^ Receiving Response ")
//////      dut.io.CacheResp.data     := 34.U
//////      dut.io.CacheResp.isSt     := isSt_R // will depend on Ld/St
//////      dut.io.CacheResp.valid    := true.B
//////      dut.io.CacheResp.tag      := tag_R
//////
//////      state := sDone
////    }
////
////    is(sDone) {
//////      println(s"io.read_resp.data:    ${dut.io.ReadCacheResp.data}")
//////      println(s"io.read_resp.tag:     ${dut.io.ReadCacheResp.tag}")
//////
//////      println(s"io.write_resp.data:  ${dut.io.WriteCacheResp.data}")
//////      println(s"io.write_resp.tag:   ${dut.io.WriteCacheResp.tag}")
//////
//////      state := sIdle
////      stop();
////      stop
////    }
////  }
//
//
//
//  when(dut.io.CacheReq.valid) {
//
//    printf(s" ^^^^^   ^^^ io.CacheReq.Valid: ")
//    printf(s" ^^^^^   ^^^ Sending Cache Req io.CacheReq.Valid: %x", dut.io.CacheReq.valid)
//    isSt_R := dut.io.CacheReq.bits.iswrite
//    tag_R  := dut.io.CacheReq.bits.tag
//
//  }
//
//

//
//
//  // Connections
//  dut.io.CacheReq.ready := (state === sIdle)
//}






class ReadWriteArbiterTester extends org.scalatest.FlatSpec {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  "ReadWriteArbiter" should "pass" in {
    assert(TesterDriver execute (() => new ReadWriteArbiterTests(new ReadWriteArbiter())))
  }
}
*/
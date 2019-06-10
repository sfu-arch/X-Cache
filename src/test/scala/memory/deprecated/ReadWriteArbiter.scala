package memory.deprecated

/**
  * Created by vnaveen0 on 9/7/17.
  */

import chisel3._
import chisel3.testers._
import chisel3.util._
import dandelion.config._
import memory.ReadWriteArbiter
/*
class ReadWriteArbiterTests(c: => ReadWriteArbiter) (implicit p: Parameters)
  extends BasicTester {

  val dut = Module(c)
  // -- IO ---
  //    val ReadMemReq = Decoupled(new MemReq)
  //    val ReadMemResp = Flipped(Valid(new MemResp))
  //    val WriteMemReq = Decoupled(new MemReq)
  //    val WriteMemResp = Flipped(Valid(new MemResp))
  //    val MemReq = Decoupled(new MemReq)
  //    val MemResp = Flipped(Valid(new MemResp))

  val sIdle :: sReq :: sDone :: Nil = Enum(3)
  val state = RegInit(sIdle)
  val isSt_R = RegInit(false.B)
  val tag_R = RegInit(0.U)

  val (testCnt, testDone) = Counter(sIdle === sReq, 2)
  // -- --------------------------------------------------
  dut.io.MemReq.ready := true.B

  switch(state) {
    is(sIdle) {

      printf("\n -----------------------------------------\n ")
      printf(p" state == Idle ")

      //Store Req

      when(dut.io.WriteMemReq.ready) {

        // dont use printf s inside when
        printf(p" WriteMemReq Ready ")
        dut.io.WriteMemReq.valid        := true.B
        dut.io.WriteMemReq.bits.addr    := 23.U
        dut.io.WriteMemReq.bits.data    := 4.U
        dut.io.WriteMemReq.bits.iswrite := true.B
        dut.io.WriteMemReq.bits.tag     := 1.U
      }
        .otherwise {
          dut.io.WriteMemReq.valid := false.B
        }



      when(dut.io.ReadMemReq.ready) {

        printf(" ReadMemReq Ready ")
        //Load Req
        dut.io.ReadMemReq.valid := true.B
        dut.io.ReadMemReq.bits.addr := 123.U
        dut.io.ReadMemReq.bits.data := 432232424.U //garbage
        dut.io.ReadMemReq.bits.iswrite := false.B
        dut.io.ReadMemReq.bits.tag := 1.U

      }
        .otherwise {
          dut.io.ReadMemReq.valid  := false.B
        }


      when(dut.io.ReadMemReq.ready || dut.io.WriteMemReq.ready) {
        state := sReq
        }

      when(dut.io.MemReq.valid) {
        isSt_R := dut.io.MemReq.bits.iswrite
        tag_R := dut.io.MemReq.bits.tag
      }

      printf("\n io.MemReq.valid : %x ", dut.io.MemReq.valid)
      printf(" io.MemReq.isSt : %x ", dut.io.MemReq.bits.iswrite)
      printf(" io.MemReq.tag : %x \n", dut.io.MemReq.bits.tag)

      printf("ReadMemReq.ready: %x ", dut.io.ReadMemReq.ready)
      printf("ReadMemReq.valid: %x \n", dut.io.ReadMemReq.valid)

      printf("WriteMemReq.ready: %x  ", dut.io.WriteMemReq.ready)
      printf("WriteMemReq.valid: %x \n ", dut.io.WriteMemReq.valid)

    }

    is(sReq) {
      println(" \n state == sReq \n ")
//      dut.io.MemResp.data     := 34.U
//      dut.io.MemResp.isSt     := isSt_R // will depend on Ld/St
//      dut.io.MemResp.valid    := true.B
//      dut.io.MemResp.tag      := tag_R
//      state := sDone
    }

    is(sDone) {

      println(" \n state == sDone \n ")
      state := sIdle
      stop();
      stop
    }
  }

//        printf(" ^^^^^^^^^^ io.MemReq.valid : ")

//  when(state === sIdle) {
//    when(c.io.MemReq.valid) {
//      printf(" ^^^^^^^^^^ io.MemReq.valid : ")
//    }
//    printf(" ^^^^^^^^^^ io.MemReq.valid : 0x%x.", dut.io.MemReq.valid)
//    printf(s"ReadMemReq.ready: %x ", dut.io.ReadMemReq.ready)
//    printf(s"ReadMemReq.valid: %x ", dut.io.ReadMemReq.valid)
//
//    printf(s"WriteMemReq.ready: %x  ", dut.io.WriteMemReq.ready)
//    printf(s"WriteMemReq.valid: %x  ", dut.io.WriteMemReq.valid)
//
//
//    printf(s" ^^^^^   ^^^ io.MemReq.Valid: %x ", dut.io.MemReq.valid)
//    printf(s"io.MemReq.addr: %x ", dut.io.MemReq.bits.addr)
//    printf(s"io.MemReq.data: %x ", dut.io.MemReq.bits.data)
//    printf(s"io.MemReq.tag:  %x ", dut.io.MemReq.bits.tag)
//    printf(s"io.MemReq.mask: %x ", dut.io.MemReq.bits.mask)
//    printf(s"io.MemReq.iswrite: %x ", dut.io.MemReq.bits.iswrite)
//
//
//    printf(s" Read/Write Response ----------")
//    printf(s"io.write_resp.valid: %x ", dut.io.WriteMemResp.valid)
//    printf(s"io.read_resp.valid:  %x ", dut.io.ReadMemResp.valid)
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
//////      when(dut.io.MemReq.ready) {
//////
//////        printf(" MemReq Ready ")
//////        //Load Req
//////        dut.io.ReadMemReq.valid         := true.B
//////        dut.io.ReadMemReq.bits.addr     := 123.U
//////        dut.io.ReadMemReq.bits.data     := 432232424.U //garbage
//////        dut.io.ReadMemReq.bits.iswrite  := false.B
//////        dut.io.ReadMemReq.bits.tag      := 1.U
//////
//////        //Store Req
//////        dut.io.WriteMemReq.valid        := true.B
//////        dut.io.WriteMemReq.bits.addr    := 23.U
//////        dut.io.WriteMemReq.bits.data    := 4.U
//////        dut.io.WriteMemReq.bits.iswrite := true.B
//////        dut.io.WriteMemReq.bits.tag     := 1.U
//////        state := sReq
//////      }
//////        .otherwise {
//////          dut.io.ReadMemReq.valid  := false.B
//////          dut.io.WriteMemReq.valid := false.B
//////        }
////
////    }
////
////    is(sReq) {
////      println(s" ^^^^  ^^^ Receiving Response ")
//////      dut.io.MemResp.data     := 34.U
//////      dut.io.MemResp.isSt     := isSt_R // will depend on Ld/St
//////      dut.io.MemResp.valid    := true.B
//////      dut.io.MemResp.tag      := tag_R
//////
//////      state := sDone
////    }
////
////    is(sDone) {
//////      println(s"io.read_resp.data:    ${dut.io.ReadMemResp.data}")
//////      println(s"io.read_resp.tag:     ${dut.io.ReadMemResp.tag}")
//////
//////      println(s"io.write_resp.data:  ${dut.io.WriteMemResp.data}")
//////      println(s"io.write_resp.tag:   ${dut.io.WriteMemResp.tag}")
//////
//////      state := sIdle
////      stop();
////      stop
////    }
////  }
//
//
//
//  when(dut.io.MemReq.valid) {
//
//    printf(s" ^^^^^   ^^^ io.MemReq.Valid: ")
//    printf(s" ^^^^^   ^^^ Sending Cache Req io.MemReq.Valid: %x", dut.io.MemReq.valid)
//    isSt_R := dut.io.MemReq.bits.iswrite
//    tag_R  := dut.io.MemReq.bits.tag
//
//  }
//
//

//
//
//  // Connections
//  dut.io.MemReq.ready := (state === sIdle)
//}






class ReadWriteArbiterTester extends org.scalatest.FlatSpec {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  "ReadWriteArbiter" should "pass" in {
    assert(TesterDriver execute (() => new ReadWriteArbiterTests(new ReadWriteArbiter())))
  }
}
*/

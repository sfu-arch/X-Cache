module DCRF1(
  input         clock,
  input         reset,
  input  [15:0] io_host_addr,
  input  [31:0] io_host_wdata,
  input         io_host_wr,
  input         io_host_rd,
  output        io_host_ack,
  output [31:0] io_host_rdata,
  output        io_dcr_launch,
  input         io_dcr_finish,
  input         io_dcr_ecnt_0_valid,
  input  [31:0] io_dcr_ecnt_0_bits,
  output [31:0] io_dcr_vals_0,
  output [31:0] io_dcr_vals_1,
  output [63:0] io_dcr_ptrs_0,
  output [63:0] io_dcr_ptrs_1,
  output [63:0] io_dcr_ptrs_2,
  output [63:0] io_dcr_ptrs_3,
  output [63:0] io_dcr_ptrs_4,
  output [63:0] io_dcr_ptrs_5,
  output [63:0] io_dcr_ptrs_6
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_21;
`endif // RANDOMIZE_REG_INIT
  reg [15:0] waddr; // @[DCRF1.scala 65:22]
  reg [1:0] wstate; // @[DCRF1.scala 68:23]
  reg [1:0] rstate; // @[DCRF1.scala 72:23]
  reg [31:0] rdata; // @[DCRF1.scala 73:22]
  reg [31:0] reg_0; // @[DCRF1.scala 79:37]
  reg [31:0] reg_1; // @[DCRF1.scala 79:37]
  reg [31:0] reg_2; // @[DCRF1.scala 79:37]
  reg [31:0] reg_3; // @[DCRF1.scala 79:37]
  reg [31:0] reg_4; // @[DCRF1.scala 79:37]
  reg [31:0] reg_5; // @[DCRF1.scala 79:37]
  reg [31:0] reg_6; // @[DCRF1.scala 79:37]
  reg [31:0] reg_7; // @[DCRF1.scala 79:37]
  reg [31:0] reg_8; // @[DCRF1.scala 79:37]
  reg [31:0] reg_9; // @[DCRF1.scala 79:37]
  reg [31:0] reg_10; // @[DCRF1.scala 79:37]
  reg [31:0] reg_11; // @[DCRF1.scala 79:37]
  reg [31:0] reg_12; // @[DCRF1.scala 79:37]
  reg [31:0] reg_13; // @[DCRF1.scala 79:37]
  reg [31:0] reg_14; // @[DCRF1.scala 79:37]
  reg [31:0] reg_15; // @[DCRF1.scala 79:37]
  reg [31:0] reg_16; // @[DCRF1.scala 79:37]
  reg [31:0] reg_17; // @[DCRF1.scala 79:37]
  wire  isWriteData = wstate == 2'h1; // @[DCRF1.scala 89:28]
  wire  _T = 2'h0 == wstate; // @[Conditional.scala 37:30]
  wire  _T_1 = 2'h1 == wstate; // @[Conditional.scala 37:30]
  wire  _T_2 = 2'h2 == wstate; // @[Conditional.scala 37:30]
  wire  _GEN_5 = _T_1 ? 1'h0 : _T_2; // @[Conditional.scala 39:67]
  wire  _GEN_8 = _T ? 1'h0 : _GEN_5; // @[Conditional.scala 40:58]
  wire  _T_3 = 2'h0 == rstate; // @[Conditional.scala 37:30]
  wire  _T_4 = 2'h1 == rstate; // @[Conditional.scala 37:30]
  wire  _T_5 = 2'h2 == rstate; // @[Conditional.scala 37:30]
  wire  _GEN_11 = _T_5 | _GEN_8; // @[Conditional.scala 39:67]
  wire  _GEN_13 = _T_4 ? _GEN_8 : _GEN_11; // @[Conditional.scala 39:67]
  wire  _T_6 = 16'h500 == waddr; // @[DCRF1.scala 125:38]
  wire  _T_7 = io_host_wr & _T_6; // @[DCRF1.scala 125:25]
  wire  _T_8 = _T_7 & isWriteData; // @[DCRF1.scala 125:48]
  wire  _T_9 = 16'h504 == waddr; // @[DCRF1.scala 132:45]
  wire  _T_10 = io_host_wr & _T_9; // @[DCRF1.scala 132:27]
  wire  _T_11 = 16'h508 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_12 = io_host_wr & _T_11; // @[DCRF1.scala 138:21]
  wire  _T_13 = _T_12 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_15 = ~reset; // @[DCRF1.scala 139:13]
  wire  _T_16 = 16'h50c == waddr; // @[DCRF1.scala 138:39]
  wire  _T_17 = io_host_wr & _T_16; // @[DCRF1.scala 138:21]
  wire  _T_18 = _T_17 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_21 = 16'h510 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_22 = io_host_wr & _T_21; // @[DCRF1.scala 138:21]
  wire  _T_23 = _T_22 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_26 = 16'h514 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_27 = io_host_wr & _T_26; // @[DCRF1.scala 138:21]
  wire  _T_28 = _T_27 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_31 = 16'h518 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_32 = io_host_wr & _T_31; // @[DCRF1.scala 138:21]
  wire  _T_33 = _T_32 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_36 = 16'h51c == waddr; // @[DCRF1.scala 138:39]
  wire  _T_37 = io_host_wr & _T_36; // @[DCRF1.scala 138:21]
  wire  _T_38 = _T_37 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_41 = 16'h520 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_42 = io_host_wr & _T_41; // @[DCRF1.scala 138:21]
  wire  _T_43 = _T_42 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_46 = 16'h524 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_47 = io_host_wr & _T_46; // @[DCRF1.scala 138:21]
  wire  _T_48 = _T_47 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_51 = 16'h528 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_52 = io_host_wr & _T_51; // @[DCRF1.scala 138:21]
  wire  _T_53 = _T_52 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_56 = 16'h52c == waddr; // @[DCRF1.scala 138:39]
  wire  _T_57 = io_host_wr & _T_56; // @[DCRF1.scala 138:21]
  wire  _T_58 = _T_57 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_61 = 16'h530 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_62 = io_host_wr & _T_61; // @[DCRF1.scala 138:21]
  wire  _T_63 = _T_62 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_66 = 16'h534 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_67 = io_host_wr & _T_66; // @[DCRF1.scala 138:21]
  wire  _T_68 = _T_67 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_71 = 16'h538 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_72 = io_host_wr & _T_71; // @[DCRF1.scala 138:21]
  wire  _T_73 = _T_72 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_76 = 16'h53c == waddr; // @[DCRF1.scala 138:39]
  wire  _T_77 = io_host_wr & _T_76; // @[DCRF1.scala 138:21]
  wire  _T_78 = _T_77 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_81 = 16'h540 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_82 = io_host_wr & _T_81; // @[DCRF1.scala 138:21]
  wire  _T_83 = _T_82 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_86 = 16'h544 == waddr; // @[DCRF1.scala 138:39]
  wire  _T_87 = io_host_wr & _T_86; // @[DCRF1.scala 138:21]
  wire  _T_88 = _T_87 & isWriteData; // @[DCRF1.scala 138:49]
  wire  _T_91 = 16'h500 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_93 = 16'h504 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_95 = 16'h508 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_97 = 16'h50c == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_99 = 16'h510 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_101 = 16'h514 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_103 = 16'h518 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_105 = 16'h51c == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_107 = 16'h520 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_109 = 16'h524 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_111 = 16'h528 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_113 = 16'h52c == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_115 = 16'h530 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_117 = 16'h534 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_119 = 16'h538 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_121 = 16'h53c == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_123 = 16'h540 == io_host_addr; // @[Mux.scala 80:60]
  wire  _T_125 = 16'h544 == io_host_addr; // @[Mux.scala 80:60]
  assign io_host_ack = _T_3 ? _GEN_8 : _GEN_13; // @[DCRF1.scala 86:15 DCRF1.scala 104:19 DCRF1.scala 119:19]
  assign io_host_rdata = rdata; // @[DCRF1.scala 87:17]
  assign io_dcr_launch = reg_0[0]; // @[DCRF1.scala 148:17]
  assign io_dcr_vals_0 = reg_2; // @[DCRF1.scala 151:20]
  assign io_dcr_vals_1 = reg_3; // @[DCRF1.scala 151:20]
  assign io_dcr_ptrs_0 = {reg_5,reg_4}; // @[DCRF1.scala 160:22]
  assign io_dcr_ptrs_1 = {reg_7,reg_6}; // @[DCRF1.scala 160:22]
  assign io_dcr_ptrs_2 = {reg_9,reg_8}; // @[DCRF1.scala 160:22]
  assign io_dcr_ptrs_3 = {reg_11,reg_10}; // @[DCRF1.scala 160:22]
  assign io_dcr_ptrs_4 = {reg_13,reg_12}; // @[DCRF1.scala 160:22]
  assign io_dcr_ptrs_5 = {reg_15,reg_14}; // @[DCRF1.scala 160:22]
  assign io_dcr_ptrs_6 = {reg_17,reg_16}; // @[DCRF1.scala 160:22]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  waddr = _RAND_0[15:0];
  _RAND_1 = {1{`RANDOM}};
  wstate = _RAND_1[1:0];
  _RAND_2 = {1{`RANDOM}};
  rstate = _RAND_2[1:0];
  _RAND_3 = {1{`RANDOM}};
  rdata = _RAND_3[31:0];
  _RAND_4 = {1{`RANDOM}};
  reg_0 = _RAND_4[31:0];
  _RAND_5 = {1{`RANDOM}};
  reg_1 = _RAND_5[31:0];
  _RAND_6 = {1{`RANDOM}};
  reg_2 = _RAND_6[31:0];
  _RAND_7 = {1{`RANDOM}};
  reg_3 = _RAND_7[31:0];
  _RAND_8 = {1{`RANDOM}};
  reg_4 = _RAND_8[31:0];
  _RAND_9 = {1{`RANDOM}};
  reg_5 = _RAND_9[31:0];
  _RAND_10 = {1{`RANDOM}};
  reg_6 = _RAND_10[31:0];
  _RAND_11 = {1{`RANDOM}};
  reg_7 = _RAND_11[31:0];
  _RAND_12 = {1{`RANDOM}};
  reg_8 = _RAND_12[31:0];
  _RAND_13 = {1{`RANDOM}};
  reg_9 = _RAND_13[31:0];
  _RAND_14 = {1{`RANDOM}};
  reg_10 = _RAND_14[31:0];
  _RAND_15 = {1{`RANDOM}};
  reg_11 = _RAND_15[31:0];
  _RAND_16 = {1{`RANDOM}};
  reg_12 = _RAND_16[31:0];
  _RAND_17 = {1{`RANDOM}};
  reg_13 = _RAND_17[31:0];
  _RAND_18 = {1{`RANDOM}};
  reg_14 = _RAND_18[31:0];
  _RAND_19 = {1{`RANDOM}};
  reg_15 = _RAND_19[31:0];
  _RAND_20 = {1{`RANDOM}};
  reg_16 = _RAND_20[31:0];
  _RAND_21 = {1{`RANDOM}};
  reg_17 = _RAND_21[31:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      waddr <= 16'hffff;
    end else if (_T) begin
      if (io_host_wr) begin
        waddr <= io_host_addr;
      end
    end
    if (reset) begin
      wstate <= 2'h0;
    end else if (_T) begin
      if (io_host_wr) begin
        wstate <= 2'h1;
      end
    end else if (_T_1) begin
      wstate <= 2'h2;
    end else if (_T_2) begin
      wstate <= 2'h0;
    end
    if (reset) begin
      rstate <= 2'h0;
    end else if (_T_3) begin
      if (io_host_rd) begin
        rstate <= 2'h1;
      end
    end else if (_T_4) begin
      rstate <= 2'h2;
    end else if (_T_5) begin
      rstate <= 2'h0;
    end
    if (reset) begin
      rdata <= 32'h0;
    end else if (io_host_rd) begin
      if (_T_125) begin
        rdata <= reg_17;
      end else if (_T_123) begin
        rdata <= reg_16;
      end else if (_T_121) begin
        rdata <= reg_15;
      end else if (_T_119) begin
        rdata <= reg_14;
      end else if (_T_117) begin
        rdata <= reg_13;
      end else if (_T_115) begin
        rdata <= reg_12;
      end else if (_T_113) begin
        rdata <= reg_11;
      end else if (_T_111) begin
        rdata <= reg_10;
      end else if (_T_109) begin
        rdata <= reg_9;
      end else if (_T_107) begin
        rdata <= reg_8;
      end else if (_T_105) begin
        rdata <= reg_7;
      end else if (_T_103) begin
        rdata <= reg_6;
      end else if (_T_101) begin
        rdata <= reg_5;
      end else if (_T_99) begin
        rdata <= reg_4;
      end else if (_T_97) begin
        rdata <= reg_3;
      end else if (_T_95) begin
        rdata <= reg_2;
      end else if (_T_93) begin
        rdata <= reg_1;
      end else if (_T_91) begin
        rdata <= reg_0;
      end else begin
        rdata <= 32'h0;
      end
    end
    if (reset) begin
      reg_0 <= 32'h0;
    end else if (io_dcr_finish) begin
      reg_0 <= 32'h2;
    end else if (_T_8) begin
      reg_0 <= io_host_wdata;
    end
    if (reset) begin
      reg_1 <= 32'h0;
    end else if (io_dcr_ecnt_0_valid) begin
      reg_1 <= io_dcr_ecnt_0_bits;
    end else if (_T_10) begin
      reg_1 <= io_host_wdata;
    end
    if (reset) begin
      reg_2 <= 32'h0;
    end else if (_T_13) begin
      reg_2 <= io_host_wdata;
    end
    if (reset) begin
      reg_3 <= 32'h0;
    end else if (_T_18) begin
      reg_3 <= io_host_wdata;
    end
    if (reset) begin
      reg_4 <= 32'h0;
    end else if (_T_23) begin
      reg_4 <= io_host_wdata;
    end
    if (reset) begin
      reg_5 <= 32'h0;
    end else if (_T_28) begin
      reg_5 <= io_host_wdata;
    end
    if (reset) begin
      reg_6 <= 32'h0;
    end else if (_T_33) begin
      reg_6 <= io_host_wdata;
    end
    if (reset) begin
      reg_7 <= 32'h0;
    end else if (_T_38) begin
      reg_7 <= io_host_wdata;
    end
    if (reset) begin
      reg_8 <= 32'h0;
    end else if (_T_43) begin
      reg_8 <= io_host_wdata;
    end
    if (reset) begin
      reg_9 <= 32'h0;
    end else if (_T_48) begin
      reg_9 <= io_host_wdata;
    end
    if (reset) begin
      reg_10 <= 32'h0;
    end else if (_T_53) begin
      reg_10 <= io_host_wdata;
    end
    if (reset) begin
      reg_11 <= 32'h0;
    end else if (_T_58) begin
      reg_11 <= io_host_wdata;
    end
    if (reset) begin
      reg_12 <= 32'h0;
    end else if (_T_63) begin
      reg_12 <= io_host_wdata;
    end
    if (reset) begin
      reg_13 <= 32'h0;
    end else if (_T_68) begin
      reg_13 <= io_host_wdata;
    end
    if (reset) begin
      reg_14 <= 32'h0;
    end else if (_T_73) begin
      reg_14 <= io_host_wdata;
    end
    if (reset) begin
      reg_15 <= 32'h0;
    end else if (_T_78) begin
      reg_15 <= io_host_wdata;
    end
    if (reset) begin
      reg_16 <= 32'h0;
    end else if (_T_83) begin
      reg_16 <= io_host_wdata;
    end
    if (reset) begin
      reg_17 <= 32'h0;
    end else if (_T_88) begin
      reg_17 <= io_host_wdata;
    end
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_13 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_18 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_23 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_28 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_33 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_38 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_43 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_48 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_53 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_58 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_63 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_68 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_73 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_78 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_83 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_T_88 & _T_15) begin
          $fwrite(32'h80000002,"Write add: %d : %d\n",waddr,io_host_wdata); // @[DCRF1.scala 139:13]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
  end
endmodule
module Arbiter(
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [63:0] io_in_0_bits_addr,
  input         io_out_ready,
  output        io_out_valid,
  output [63:0] io_out_bits_addr
);
  assign io_in_0_ready = io_out_ready; // @[Arbiter.scala 134:14]
  assign io_out_valid = io_in_0_valid; // @[Arbiter.scala 135:16]
  assign io_out_bits_addr = io_in_0_bits_addr; // @[Arbiter.scala 124:15]
endmodule
module Arbiter_1(
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [63:0] io_in_0_bits_addr,
  output        io_in_1_ready,
  input         io_in_1_valid,
  input  [63:0] io_in_1_bits_addr,
  input  [7:0]  io_in_1_bits_len,
  output        io_in_2_ready,
  input         io_in_2_valid,
  input  [63:0] io_in_2_bits_addr,
  input  [7:0]  io_in_2_bits_len,
  output        io_in_3_ready,
  input         io_in_3_valid,
  input  [63:0] io_in_3_bits_addr,
  input  [7:0]  io_in_3_bits_len,
  output        io_in_4_ready,
  input         io_in_4_valid,
  input  [63:0] io_in_4_bits_addr,
  input  [7:0]  io_in_4_bits_len,
  output        io_in_5_ready,
  input         io_in_5_valid,
  input  [63:0] io_in_5_bits_addr,
  input  [7:0]  io_in_5_bits_len,
  input         io_out_ready,
  output        io_out_valid,
  output [63:0] io_out_bits_addr,
  output [7:0]  io_out_bits_len,
  output [2:0]  io_chosen
);
  wire [2:0] _GEN_0 = io_in_4_valid ? 3'h4 : 3'h5; // @[Arbiter.scala 126:27]
  wire [7:0] _GEN_1 = io_in_4_valid ? io_in_4_bits_len : io_in_5_bits_len; // @[Arbiter.scala 126:27]
  wire [63:0] _GEN_2 = io_in_4_valid ? io_in_4_bits_addr : io_in_5_bits_addr; // @[Arbiter.scala 126:27]
  wire [2:0] _GEN_3 = io_in_3_valid ? 3'h3 : _GEN_0; // @[Arbiter.scala 126:27]
  wire [7:0] _GEN_4 = io_in_3_valid ? io_in_3_bits_len : _GEN_1; // @[Arbiter.scala 126:27]
  wire [63:0] _GEN_5 = io_in_3_valid ? io_in_3_bits_addr : _GEN_2; // @[Arbiter.scala 126:27]
  wire [2:0] _GEN_6 = io_in_2_valid ? 3'h2 : _GEN_3; // @[Arbiter.scala 126:27]
  wire [7:0] _GEN_7 = io_in_2_valid ? io_in_2_bits_len : _GEN_4; // @[Arbiter.scala 126:27]
  wire [63:0] _GEN_8 = io_in_2_valid ? io_in_2_bits_addr : _GEN_5; // @[Arbiter.scala 126:27]
  wire [2:0] _GEN_9 = io_in_1_valid ? 3'h1 : _GEN_6; // @[Arbiter.scala 126:27]
  wire [7:0] _GEN_10 = io_in_1_valid ? io_in_1_bits_len : _GEN_7; // @[Arbiter.scala 126:27]
  wire [63:0] _GEN_11 = io_in_1_valid ? io_in_1_bits_addr : _GEN_8; // @[Arbiter.scala 126:27]
  wire  _T = io_in_0_valid | io_in_1_valid; // @[Arbiter.scala 31:68]
  wire  _T_1 = _T | io_in_2_valid; // @[Arbiter.scala 31:68]
  wire  _T_2 = _T_1 | io_in_3_valid; // @[Arbiter.scala 31:68]
  wire  _T_3 = _T_2 | io_in_4_valid; // @[Arbiter.scala 31:68]
  wire  grant_1 = ~io_in_0_valid; // @[Arbiter.scala 31:78]
  wire  grant_2 = ~_T; // @[Arbiter.scala 31:78]
  wire  grant_3 = ~_T_1; // @[Arbiter.scala 31:78]
  wire  grant_4 = ~_T_2; // @[Arbiter.scala 31:78]
  wire  grant_5 = ~_T_3; // @[Arbiter.scala 31:78]
  wire  _T_10 = ~grant_5; // @[Arbiter.scala 135:19]
  assign io_in_0_ready = io_out_ready; // @[Arbiter.scala 134:14]
  assign io_in_1_ready = grant_1 & io_out_ready; // @[Arbiter.scala 134:14]
  assign io_in_2_ready = grant_2 & io_out_ready; // @[Arbiter.scala 134:14]
  assign io_in_3_ready = grant_3 & io_out_ready; // @[Arbiter.scala 134:14]
  assign io_in_4_ready = grant_4 & io_out_ready; // @[Arbiter.scala 134:14]
  assign io_in_5_ready = grant_5 & io_out_ready; // @[Arbiter.scala 134:14]
  assign io_out_valid = _T_10 | io_in_5_valid; // @[Arbiter.scala 135:16]
  assign io_out_bits_addr = io_in_0_valid ? io_in_0_bits_addr : _GEN_11; // @[Arbiter.scala 124:15 Arbiter.scala 128:19 Arbiter.scala 128:19 Arbiter.scala 128:19 Arbiter.scala 128:19 Arbiter.scala 128:19]
  assign io_out_bits_len = io_in_0_valid ? 8'h0 : _GEN_10; // @[Arbiter.scala 124:15 Arbiter.scala 128:19 Arbiter.scala 128:19 Arbiter.scala 128:19 Arbiter.scala 128:19 Arbiter.scala 128:19]
  assign io_chosen = io_in_0_valid ? 3'h0 : _GEN_9; // @[Arbiter.scala 123:13 Arbiter.scala 127:17 Arbiter.scala 127:17 Arbiter.scala 127:17 Arbiter.scala 127:17 Arbiter.scala 127:17]
endmodule
module DME(
  input          clock,
  input          reset,
  input          io_mem_aw_ready,
  output         io_mem_aw_valid,
  output [63:0]  io_mem_aw_bits_addr,
  output [7:0]   io_mem_aw_bits_len,
  input          io_mem_w_ready,
  output         io_mem_w_valid,
  output [511:0] io_mem_w_bits_data,
  output         io_mem_w_bits_last,
  output         io_mem_b_ready,
  input          io_mem_b_valid,
  input          io_mem_ar_ready,
  output         io_mem_ar_valid,
  output [63:0]  io_mem_ar_bits_addr,
  output         io_mem_r_ready,
  input          io_mem_r_valid,
  input  [511:0] io_mem_r_bits_data,
  input          io_mem_r_bits_last,
  output         io_dme_rd_0_cmd_ready,
  input          io_dme_rd_0_cmd_valid,
  input  [63:0]  io_dme_rd_0_cmd_bits_addr,
  input          io_dme_rd_0_data_ready,
  output         io_dme_rd_0_data_valid,
  output [511:0] io_dme_rd_0_data_bits,
  output         io_dme_wr_0_cmd_ready,
  input          io_dme_wr_0_cmd_valid,
  input  [63:0]  io_dme_wr_0_cmd_bits_addr,
  output         io_dme_wr_0_data_ready,
  input          io_dme_wr_0_data_valid,
  input  [511:0] io_dme_wr_0_data_bits,
  output         io_dme_wr_0_ack,
  output         io_dme_wr_1_cmd_ready,
  input          io_dme_wr_1_cmd_valid,
  input  [63:0]  io_dme_wr_1_cmd_bits_addr,
  input  [7:0]   io_dme_wr_1_cmd_bits_len,
  output         io_dme_wr_1_data_ready,
  input          io_dme_wr_1_data_valid,
  input  [511:0] io_dme_wr_1_data_bits,
  output         io_dme_wr_1_ack,
  output         io_dme_wr_2_cmd_ready,
  input          io_dme_wr_2_cmd_valid,
  input  [63:0]  io_dme_wr_2_cmd_bits_addr,
  input  [7:0]   io_dme_wr_2_cmd_bits_len,
  output         io_dme_wr_2_data_ready,
  input          io_dme_wr_2_data_valid,
  input  [511:0] io_dme_wr_2_data_bits,
  output         io_dme_wr_2_ack,
  output         io_dme_wr_3_cmd_ready,
  input          io_dme_wr_3_cmd_valid,
  input  [63:0]  io_dme_wr_3_cmd_bits_addr,
  input  [7:0]   io_dme_wr_3_cmd_bits_len,
  output         io_dme_wr_3_data_ready,
  input          io_dme_wr_3_data_valid,
  input  [511:0] io_dme_wr_3_data_bits,
  output         io_dme_wr_3_ack,
  output         io_dme_wr_4_cmd_ready,
  input          io_dme_wr_4_cmd_valid,
  input  [63:0]  io_dme_wr_4_cmd_bits_addr,
  input  [7:0]   io_dme_wr_4_cmd_bits_len,
  output         io_dme_wr_4_data_ready,
  input          io_dme_wr_4_data_valid,
  input  [511:0] io_dme_wr_4_data_bits,
  output         io_dme_wr_4_ack,
  output         io_dme_wr_5_cmd_ready,
  input          io_dme_wr_5_cmd_valid,
  input  [63:0]  io_dme_wr_5_cmd_bits_addr,
  input  [7:0]   io_dme_wr_5_cmd_bits_len,
  output         io_dme_wr_5_data_ready,
  input          io_dme_wr_5_data_valid,
  input  [511:0] io_dme_wr_5_data_bits,
  output         io_dme_wr_5_ack
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
`endif // RANDOMIZE_REG_INIT
  wire  rd_arb_io_in_0_ready; // @[DME.scala 130:22]
  wire  rd_arb_io_in_0_valid; // @[DME.scala 130:22]
  wire [63:0] rd_arb_io_in_0_bits_addr; // @[DME.scala 130:22]
  wire  rd_arb_io_out_ready; // @[DME.scala 130:22]
  wire  rd_arb_io_out_valid; // @[DME.scala 130:22]
  wire [63:0] rd_arb_io_out_bits_addr; // @[DME.scala 130:22]
  wire  wr_arb_io_in_0_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_in_0_valid; // @[DME.scala 160:22]
  wire [63:0] wr_arb_io_in_0_bits_addr; // @[DME.scala 160:22]
  wire  wr_arb_io_in_1_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_in_1_valid; // @[DME.scala 160:22]
  wire [63:0] wr_arb_io_in_1_bits_addr; // @[DME.scala 160:22]
  wire [7:0] wr_arb_io_in_1_bits_len; // @[DME.scala 160:22]
  wire  wr_arb_io_in_2_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_in_2_valid; // @[DME.scala 160:22]
  wire [63:0] wr_arb_io_in_2_bits_addr; // @[DME.scala 160:22]
  wire [7:0] wr_arb_io_in_2_bits_len; // @[DME.scala 160:22]
  wire  wr_arb_io_in_3_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_in_3_valid; // @[DME.scala 160:22]
  wire [63:0] wr_arb_io_in_3_bits_addr; // @[DME.scala 160:22]
  wire [7:0] wr_arb_io_in_3_bits_len; // @[DME.scala 160:22]
  wire  wr_arb_io_in_4_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_in_4_valid; // @[DME.scala 160:22]
  wire [63:0] wr_arb_io_in_4_bits_addr; // @[DME.scala 160:22]
  wire [7:0] wr_arb_io_in_4_bits_len; // @[DME.scala 160:22]
  wire  wr_arb_io_in_5_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_in_5_valid; // @[DME.scala 160:22]
  wire [63:0] wr_arb_io_in_5_bits_addr; // @[DME.scala 160:22]
  wire [7:0] wr_arb_io_in_5_bits_len; // @[DME.scala 160:22]
  wire  wr_arb_io_out_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_out_valid; // @[DME.scala 160:22]
  wire [63:0] wr_arb_io_out_bits_addr; // @[DME.scala 160:22]
  wire [7:0] wr_arb_io_out_bits_len; // @[DME.scala 160:22]
  wire [2:0] wr_arb_io_chosen; // @[DME.scala 160:22]
  wire  _T = rd_arb_io_out_ready & rd_arb_io_out_valid; // @[Decoupled.scala 40:37]
  reg [1:0] rstate; // @[DME.scala 138:23]
  wire  _T_1 = 2'h0 == rstate; // @[Conditional.scala 37:30]
  wire  _T_2 = 2'h1 == rstate; // @[Conditional.scala 37:30]
  wire  _T_3 = 2'h2 == rstate; // @[Conditional.scala 37:30]
  wire  _T_4 = io_mem_r_ready & io_mem_r_valid; // @[Decoupled.scala 40:37]
  wire  _T_5 = _T_4 & io_mem_r_bits_last; // @[DME.scala 152:28]
  wire  _T_6 = wr_arb_io_out_ready & wr_arb_io_out_valid; // @[Decoupled.scala 40:37]
  reg [2:0] wr_arb_chosen; // @[Reg.scala 15:16]
  reg [1:0] wstate; // @[DME.scala 168:23]
  reg [7:0] wr_cnt; // @[DME.scala 171:23]
  wire  _T_7 = wstate == 2'h0; // @[DME.scala 174:15]
  wire  _T_8 = io_mem_w_ready & io_mem_w_valid; // @[Decoupled.scala 40:37]
  wire [7:0] _T_10 = wr_cnt + 8'h1; // @[DME.scala 177:22]
  wire  _T_11 = 2'h0 == wstate; // @[Conditional.scala 37:30]
  wire  _T_12 = 2'h1 == wstate; // @[Conditional.scala 37:30]
  wire  _T_13 = 2'h2 == wstate; // @[Conditional.scala 37:30]
  wire [7:0] _GEN_23 = 3'h1 == wr_arb_chosen ? io_dme_wr_1_cmd_bits_len : 8'h0; // @[DME.scala 193:45]
  wire  _GEN_25 = 3'h1 == wr_arb_chosen ? io_dme_wr_1_data_valid : io_dme_wr_0_data_valid; // @[DME.scala 193:45]
  wire [511:0] _GEN_26 = 3'h1 == wr_arb_chosen ? io_dme_wr_1_data_bits : io_dme_wr_0_data_bits; // @[DME.scala 193:45]
  wire [7:0] _GEN_31 = 3'h2 == wr_arb_chosen ? io_dme_wr_2_cmd_bits_len : _GEN_23; // @[DME.scala 193:45]
  wire  _GEN_33 = 3'h2 == wr_arb_chosen ? io_dme_wr_2_data_valid : _GEN_25; // @[DME.scala 193:45]
  wire [511:0] _GEN_34 = 3'h2 == wr_arb_chosen ? io_dme_wr_2_data_bits : _GEN_26; // @[DME.scala 193:45]
  wire [7:0] _GEN_39 = 3'h3 == wr_arb_chosen ? io_dme_wr_3_cmd_bits_len : _GEN_31; // @[DME.scala 193:45]
  wire  _GEN_41 = 3'h3 == wr_arb_chosen ? io_dme_wr_3_data_valid : _GEN_33; // @[DME.scala 193:45]
  wire [511:0] _GEN_42 = 3'h3 == wr_arb_chosen ? io_dme_wr_3_data_bits : _GEN_34; // @[DME.scala 193:45]
  wire [7:0] _GEN_47 = 3'h4 == wr_arb_chosen ? io_dme_wr_4_cmd_bits_len : _GEN_39; // @[DME.scala 193:45]
  wire  _GEN_49 = 3'h4 == wr_arb_chosen ? io_dme_wr_4_data_valid : _GEN_41; // @[DME.scala 193:45]
  wire [511:0] _GEN_50 = 3'h4 == wr_arb_chosen ? io_dme_wr_4_data_bits : _GEN_42; // @[DME.scala 193:45]
  wire [7:0] _GEN_55 = 3'h5 == wr_arb_chosen ? io_dme_wr_5_cmd_bits_len : _GEN_47; // @[DME.scala 193:45]
  wire  _GEN_57 = 3'h5 == wr_arb_chosen ? io_dme_wr_5_data_valid : _GEN_49; // @[DME.scala 193:45]
  wire  _T_14 = _GEN_57 & io_mem_w_ready; // @[DME.scala 193:45]
  wire  _T_15 = wr_cnt == _GEN_55; // @[DME.scala 193:73]
  wire  _T_16 = _T_14 & _T_15; // @[DME.scala 193:63]
  wire  _T_17 = 2'h3 == wstate; // @[Conditional.scala 37:30]
  reg [63:0] rd_addr; // @[Reg.scala 27:20]
  reg [7:0] wr_len; // @[Reg.scala 27:20]
  reg [63:0] wr_addr; // @[Reg.scala 27:20]
  wire  _T_26 = wr_arb_chosen == 3'h0; // @[DME.scala 221:40]
  wire  _T_27 = io_mem_b_ready & io_mem_b_valid; // @[Decoupled.scala 40:37]
  wire  _T_30 = wstate == 2'h2; // @[DME.scala 222:67]
  wire  _T_31 = _T_26 & _T_30; // @[DME.scala 222:56]
  wire  _T_33 = wr_arb_chosen == 3'h1; // @[DME.scala 221:40]
  wire  _T_38 = _T_33 & _T_30; // @[DME.scala 222:56]
  wire  _T_40 = wr_arb_chosen == 3'h2; // @[DME.scala 221:40]
  wire  _T_45 = _T_40 & _T_30; // @[DME.scala 222:56]
  wire  _T_47 = wr_arb_chosen == 3'h3; // @[DME.scala 221:40]
  wire  _T_52 = _T_47 & _T_30; // @[DME.scala 222:56]
  wire  _T_54 = wr_arb_chosen == 3'h4; // @[DME.scala 221:40]
  wire  _T_59 = _T_54 & _T_30; // @[DME.scala 222:56]
  wire  _T_61 = wr_arb_chosen == 3'h5; // @[DME.scala 221:40]
  wire  _T_66 = _T_61 & _T_30; // @[DME.scala 222:56]
  wire  _T_74 = rstate == 2'h2; // @[DME.scala 240:28]
  Arbiter rd_arb ( // @[DME.scala 130:22]
    .io_in_0_ready(rd_arb_io_in_0_ready),
    .io_in_0_valid(rd_arb_io_in_0_valid),
    .io_in_0_bits_addr(rd_arb_io_in_0_bits_addr),
    .io_out_ready(rd_arb_io_out_ready),
    .io_out_valid(rd_arb_io_out_valid),
    .io_out_bits_addr(rd_arb_io_out_bits_addr)
  );
  Arbiter_1 wr_arb ( // @[DME.scala 160:22]
    .io_in_0_ready(wr_arb_io_in_0_ready),
    .io_in_0_valid(wr_arb_io_in_0_valid),
    .io_in_0_bits_addr(wr_arb_io_in_0_bits_addr),
    .io_in_1_ready(wr_arb_io_in_1_ready),
    .io_in_1_valid(wr_arb_io_in_1_valid),
    .io_in_1_bits_addr(wr_arb_io_in_1_bits_addr),
    .io_in_1_bits_len(wr_arb_io_in_1_bits_len),
    .io_in_2_ready(wr_arb_io_in_2_ready),
    .io_in_2_valid(wr_arb_io_in_2_valid),
    .io_in_2_bits_addr(wr_arb_io_in_2_bits_addr),
    .io_in_2_bits_len(wr_arb_io_in_2_bits_len),
    .io_in_3_ready(wr_arb_io_in_3_ready),
    .io_in_3_valid(wr_arb_io_in_3_valid),
    .io_in_3_bits_addr(wr_arb_io_in_3_bits_addr),
    .io_in_3_bits_len(wr_arb_io_in_3_bits_len),
    .io_in_4_ready(wr_arb_io_in_4_ready),
    .io_in_4_valid(wr_arb_io_in_4_valid),
    .io_in_4_bits_addr(wr_arb_io_in_4_bits_addr),
    .io_in_4_bits_len(wr_arb_io_in_4_bits_len),
    .io_in_5_ready(wr_arb_io_in_5_ready),
    .io_in_5_valid(wr_arb_io_in_5_valid),
    .io_in_5_bits_addr(wr_arb_io_in_5_bits_addr),
    .io_in_5_bits_len(wr_arb_io_in_5_bits_len),
    .io_out_ready(wr_arb_io_out_ready),
    .io_out_valid(wr_arb_io_out_valid),
    .io_out_bits_addr(wr_arb_io_out_bits_addr),
    .io_out_bits_len(wr_arb_io_out_bits_len),
    .io_chosen(wr_arb_io_chosen)
  );
  assign io_mem_aw_valid = wstate == 2'h1; // @[DME.scala 226:19]
  assign io_mem_aw_bits_addr = wr_addr; // @[DME.scala 227:23]
  assign io_mem_aw_bits_len = wr_len; // @[DME.scala 228:22]
  assign io_mem_w_valid = _T_30 & _GEN_57; // @[DME.scala 230:18]
  assign io_mem_w_bits_data = 3'h5 == wr_arb_chosen ? io_dme_wr_5_data_bits : _GEN_50; // @[DME.scala 231:22]
  assign io_mem_w_bits_last = wr_cnt == _GEN_55; // @[DME.scala 232:22]
  assign io_mem_b_ready = wstate == 2'h3; // @[DME.scala 234:18]
  assign io_mem_ar_valid = rstate == 2'h1; // @[DME.scala 236:19]
  assign io_mem_ar_bits_addr = rd_addr; // @[DME.scala 237:23]
  assign io_mem_r_ready = _T_74 & io_dme_rd_0_data_ready; // @[DME.scala 240:18]
  assign io_dme_rd_0_cmd_ready = rd_arb_io_in_0_ready; // @[DME.scala 134:21]
  assign io_dme_rd_0_data_valid = io_mem_r_valid; // @[DME.scala 215:29]
  assign io_dme_rd_0_data_bits = io_mem_r_bits_data; // @[DME.scala 216:28]
  assign io_dme_wr_0_cmd_ready = wr_arb_io_in_0_ready; // @[DME.scala 164:21]
  assign io_dme_wr_0_data_ready = _T_31 & io_mem_w_ready; // @[DME.scala 222:29]
  assign io_dme_wr_0_ack = _T_26 & _T_27; // @[DME.scala 221:22]
  assign io_dme_wr_1_cmd_ready = wr_arb_io_in_1_ready; // @[DME.scala 164:21]
  assign io_dme_wr_1_data_ready = _T_38 & io_mem_w_ready; // @[DME.scala 222:29]
  assign io_dme_wr_1_ack = _T_33 & _T_27; // @[DME.scala 221:22]
  assign io_dme_wr_2_cmd_ready = wr_arb_io_in_2_ready; // @[DME.scala 164:21]
  assign io_dme_wr_2_data_ready = _T_45 & io_mem_w_ready; // @[DME.scala 222:29]
  assign io_dme_wr_2_ack = _T_40 & _T_27; // @[DME.scala 221:22]
  assign io_dme_wr_3_cmd_ready = wr_arb_io_in_3_ready; // @[DME.scala 164:21]
  assign io_dme_wr_3_data_ready = _T_52 & io_mem_w_ready; // @[DME.scala 222:29]
  assign io_dme_wr_3_ack = _T_47 & _T_27; // @[DME.scala 221:22]
  assign io_dme_wr_4_cmd_ready = wr_arb_io_in_4_ready; // @[DME.scala 164:21]
  assign io_dme_wr_4_data_ready = _T_59 & io_mem_w_ready; // @[DME.scala 222:29]
  assign io_dme_wr_4_ack = _T_54 & _T_27; // @[DME.scala 221:22]
  assign io_dme_wr_5_cmd_ready = wr_arb_io_in_5_ready; // @[DME.scala 164:21]
  assign io_dme_wr_5_data_ready = _T_66 & io_mem_w_ready; // @[DME.scala 222:29]
  assign io_dme_wr_5_ack = _T_61 & _T_27; // @[DME.scala 221:22]
  assign rd_arb_io_in_0_valid = io_dme_rd_0_cmd_valid; // @[DME.scala 134:21]
  assign rd_arb_io_in_0_bits_addr = io_dme_rd_0_cmd_bits_addr; // @[DME.scala 134:21]
  assign rd_arb_io_out_ready = rstate == 2'h0; // @[DME.scala 210:23]
  assign wr_arb_io_in_0_valid = io_dme_wr_0_cmd_valid; // @[DME.scala 164:21]
  assign wr_arb_io_in_0_bits_addr = io_dme_wr_0_cmd_bits_addr; // @[DME.scala 164:21]
  assign wr_arb_io_in_1_valid = io_dme_wr_1_cmd_valid; // @[DME.scala 164:21]
  assign wr_arb_io_in_1_bits_addr = io_dme_wr_1_cmd_bits_addr; // @[DME.scala 164:21]
  assign wr_arb_io_in_1_bits_len = io_dme_wr_1_cmd_bits_len; // @[DME.scala 164:21]
  assign wr_arb_io_in_2_valid = io_dme_wr_2_cmd_valid; // @[DME.scala 164:21]
  assign wr_arb_io_in_2_bits_addr = io_dme_wr_2_cmd_bits_addr; // @[DME.scala 164:21]
  assign wr_arb_io_in_2_bits_len = io_dme_wr_2_cmd_bits_len; // @[DME.scala 164:21]
  assign wr_arb_io_in_3_valid = io_dme_wr_3_cmd_valid; // @[DME.scala 164:21]
  assign wr_arb_io_in_3_bits_addr = io_dme_wr_3_cmd_bits_addr; // @[DME.scala 164:21]
  assign wr_arb_io_in_3_bits_len = io_dme_wr_3_cmd_bits_len; // @[DME.scala 164:21]
  assign wr_arb_io_in_4_valid = io_dme_wr_4_cmd_valid; // @[DME.scala 164:21]
  assign wr_arb_io_in_4_bits_addr = io_dme_wr_4_cmd_bits_addr; // @[DME.scala 164:21]
  assign wr_arb_io_in_4_bits_len = io_dme_wr_4_cmd_bits_len; // @[DME.scala 164:21]
  assign wr_arb_io_in_5_valid = io_dme_wr_5_cmd_valid; // @[DME.scala 164:21]
  assign wr_arb_io_in_5_bits_addr = io_dme_wr_5_cmd_bits_addr; // @[DME.scala 164:21]
  assign wr_arb_io_in_5_bits_len = io_dme_wr_5_cmd_bits_len; // @[DME.scala 164:21]
  assign wr_arb_io_out_ready = wstate == 2'h0; // @[DME.scala 211:23]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  rstate = _RAND_0[1:0];
  _RAND_1 = {1{`RANDOM}};
  wr_arb_chosen = _RAND_1[2:0];
  _RAND_2 = {1{`RANDOM}};
  wstate = _RAND_2[1:0];
  _RAND_3 = {1{`RANDOM}};
  wr_cnt = _RAND_3[7:0];
  _RAND_4 = {2{`RANDOM}};
  rd_addr = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  wr_len = _RAND_5[7:0];
  _RAND_6 = {2{`RANDOM}};
  wr_addr = _RAND_6[63:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      rstate <= 2'h0;
    end else if (_T_1) begin
      if (rd_arb_io_out_valid) begin
        rstate <= 2'h1;
      end
    end else if (_T_2) begin
      if (io_mem_ar_ready) begin
        rstate <= 2'h2;
      end
    end else if (_T_3) begin
      if (_T_5) begin
        rstate <= 2'h0;
      end
    end
    if (_T_6) begin
      wr_arb_chosen <= wr_arb_io_chosen;
    end
    if (reset) begin
      wstate <= 2'h0;
    end else if (_T_11) begin
      if (wr_arb_io_out_valid) begin
        wstate <= 2'h1;
      end
    end else if (_T_12) begin
      if (io_mem_aw_ready) begin
        wstate <= 2'h2;
      end
    end else if (_T_13) begin
      if (_T_16) begin
        wstate <= 2'h3;
      end
    end else if (_T_17) begin
      if (io_mem_b_valid) begin
        wstate <= 2'h0;
      end
    end
    if (reset) begin
      wr_cnt <= 8'h0;
    end else if (_T_7) begin
      wr_cnt <= 8'h0;
    end else if (_T_8) begin
      wr_cnt <= _T_10;
    end
    if (reset) begin
      rd_addr <= 64'h0;
    end else if (_T) begin
      rd_addr <= rd_arb_io_out_bits_addr;
    end
    if (reset) begin
      wr_len <= 8'h0;
    end else if (_T_6) begin
      wr_len <= wr_arb_io_out_bits_len;
    end
    if (reset) begin
      wr_addr <= 64'h0;
    end else if (_T_6) begin
      wr_addr <= wr_arb_io_out_bits_addr;
    end
  end
endmodule
module DMECache(
  input          clock,
  input          reset,
  input          io_cpu_flush,
  output         io_cpu_flush_done,
  output         io_cpu_req_ready,
  input          io_cpu_req_valid,
  input  [63:0]  io_cpu_req_bits_addr,
  input  [63:0]  io_cpu_req_bits_data,
  input  [7:0]   io_cpu_req_bits_mask,
  input  [7:0]   io_cpu_req_bits_tag,
  output         io_cpu_resp_valid,
  output [63:0]  io_cpu_resp_bits_data,
  output [7:0]   io_cpu_resp_bits_tag,
  input          io_mem_rd_cmd_ready,
  output         io_mem_rd_cmd_valid,
  output [63:0]  io_mem_rd_cmd_bits_addr,
  output         io_mem_rd_data_ready,
  input          io_mem_rd_data_valid,
  input  [511:0] io_mem_rd_data_bits,
  input          io_mem_wr_cmd_ready,
  output         io_mem_wr_cmd_valid,
  output [63:0]  io_mem_wr_cmd_bits_addr,
  input          io_mem_wr_data_ready,
  output         io_mem_wr_data_valid,
  output [511:0] io_mem_wr_data_bits,
  input          io_mem_wr_ack
);
`ifdef RANDOMIZE_MEM_INIT
  reg [63:0] _RAND_0;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_21;
  reg [31:0] _RAND_25;
  reg [31:0] _RAND_29;
  reg [31:0] _RAND_33;
  reg [31:0] _RAND_37;
  reg [31:0] _RAND_41;
  reg [31:0] _RAND_45;
  reg [31:0] _RAND_49;
  reg [31:0] _RAND_53;
  reg [31:0] _RAND_57;
  reg [31:0] _RAND_61;
  reg [31:0] _RAND_65;
  reg [31:0] _RAND_69;
  reg [31:0] _RAND_73;
  reg [31:0] _RAND_77;
  reg [31:0] _RAND_81;
  reg [31:0] _RAND_85;
  reg [31:0] _RAND_89;
  reg [31:0] _RAND_93;
  reg [31:0] _RAND_97;
  reg [31:0] _RAND_101;
  reg [31:0] _RAND_105;
  reg [31:0] _RAND_109;
  reg [31:0] _RAND_113;
  reg [31:0] _RAND_117;
  reg [31:0] _RAND_121;
  reg [31:0] _RAND_125;
  reg [31:0] _RAND_129;
  reg [31:0] _RAND_133;
  reg [31:0] _RAND_137;
  reg [31:0] _RAND_141;
  reg [31:0] _RAND_145;
  reg [31:0] _RAND_149;
  reg [31:0] _RAND_153;
  reg [31:0] _RAND_157;
  reg [31:0] _RAND_161;
  reg [31:0] _RAND_165;
  reg [31:0] _RAND_169;
  reg [31:0] _RAND_173;
  reg [31:0] _RAND_177;
  reg [31:0] _RAND_181;
  reg [31:0] _RAND_185;
  reg [31:0] _RAND_189;
  reg [31:0] _RAND_193;
  reg [31:0] _RAND_197;
  reg [31:0] _RAND_201;
  reg [31:0] _RAND_205;
  reg [31:0] _RAND_209;
  reg [31:0] _RAND_213;
  reg [31:0] _RAND_217;
  reg [31:0] _RAND_221;
  reg [31:0] _RAND_225;
  reg [31:0] _RAND_229;
  reg [31:0] _RAND_233;
  reg [31:0] _RAND_237;
  reg [31:0] _RAND_241;
  reg [31:0] _RAND_245;
  reg [31:0] _RAND_249;
  reg [31:0] _RAND_253;
  reg [31:0] _RAND_257;
`endif // RANDOMIZE_MEM_INIT
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_22;
  reg [31:0] _RAND_23;
  reg [31:0] _RAND_24;
  reg [31:0] _RAND_26;
  reg [31:0] _RAND_27;
  reg [31:0] _RAND_28;
  reg [31:0] _RAND_30;
  reg [31:0] _RAND_31;
  reg [31:0] _RAND_32;
  reg [31:0] _RAND_34;
  reg [31:0] _RAND_35;
  reg [31:0] _RAND_36;
  reg [31:0] _RAND_38;
  reg [31:0] _RAND_39;
  reg [31:0] _RAND_40;
  reg [31:0] _RAND_42;
  reg [31:0] _RAND_43;
  reg [31:0] _RAND_44;
  reg [31:0] _RAND_46;
  reg [31:0] _RAND_47;
  reg [31:0] _RAND_48;
  reg [31:0] _RAND_50;
  reg [31:0] _RAND_51;
  reg [31:0] _RAND_52;
  reg [31:0] _RAND_54;
  reg [31:0] _RAND_55;
  reg [31:0] _RAND_56;
  reg [31:0] _RAND_58;
  reg [31:0] _RAND_59;
  reg [31:0] _RAND_60;
  reg [31:0] _RAND_62;
  reg [31:0] _RAND_63;
  reg [31:0] _RAND_64;
  reg [31:0] _RAND_66;
  reg [31:0] _RAND_67;
  reg [31:0] _RAND_68;
  reg [31:0] _RAND_70;
  reg [31:0] _RAND_71;
  reg [31:0] _RAND_72;
  reg [31:0] _RAND_74;
  reg [31:0] _RAND_75;
  reg [31:0] _RAND_76;
  reg [31:0] _RAND_78;
  reg [31:0] _RAND_79;
  reg [31:0] _RAND_80;
  reg [31:0] _RAND_82;
  reg [31:0] _RAND_83;
  reg [31:0] _RAND_84;
  reg [31:0] _RAND_86;
  reg [31:0] _RAND_87;
  reg [31:0] _RAND_88;
  reg [31:0] _RAND_90;
  reg [31:0] _RAND_91;
  reg [31:0] _RAND_92;
  reg [31:0] _RAND_94;
  reg [31:0] _RAND_95;
  reg [31:0] _RAND_96;
  reg [31:0] _RAND_98;
  reg [31:0] _RAND_99;
  reg [31:0] _RAND_100;
  reg [31:0] _RAND_102;
  reg [31:0] _RAND_103;
  reg [31:0] _RAND_104;
  reg [31:0] _RAND_106;
  reg [31:0] _RAND_107;
  reg [31:0] _RAND_108;
  reg [31:0] _RAND_110;
  reg [31:0] _RAND_111;
  reg [31:0] _RAND_112;
  reg [31:0] _RAND_114;
  reg [31:0] _RAND_115;
  reg [31:0] _RAND_116;
  reg [31:0] _RAND_118;
  reg [31:0] _RAND_119;
  reg [31:0] _RAND_120;
  reg [31:0] _RAND_122;
  reg [31:0] _RAND_123;
  reg [31:0] _RAND_124;
  reg [31:0] _RAND_126;
  reg [31:0] _RAND_127;
  reg [31:0] _RAND_128;
  reg [31:0] _RAND_130;
  reg [31:0] _RAND_131;
  reg [31:0] _RAND_132;
  reg [31:0] _RAND_134;
  reg [31:0] _RAND_135;
  reg [31:0] _RAND_136;
  reg [31:0] _RAND_138;
  reg [31:0] _RAND_139;
  reg [31:0] _RAND_140;
  reg [31:0] _RAND_142;
  reg [31:0] _RAND_143;
  reg [31:0] _RAND_144;
  reg [31:0] _RAND_146;
  reg [31:0] _RAND_147;
  reg [31:0] _RAND_148;
  reg [31:0] _RAND_150;
  reg [31:0] _RAND_151;
  reg [31:0] _RAND_152;
  reg [31:0] _RAND_154;
  reg [31:0] _RAND_155;
  reg [31:0] _RAND_156;
  reg [31:0] _RAND_158;
  reg [31:0] _RAND_159;
  reg [31:0] _RAND_160;
  reg [31:0] _RAND_162;
  reg [31:0] _RAND_163;
  reg [31:0] _RAND_164;
  reg [31:0] _RAND_166;
  reg [31:0] _RAND_167;
  reg [31:0] _RAND_168;
  reg [31:0] _RAND_170;
  reg [31:0] _RAND_171;
  reg [31:0] _RAND_172;
  reg [31:0] _RAND_174;
  reg [31:0] _RAND_175;
  reg [31:0] _RAND_176;
  reg [31:0] _RAND_178;
  reg [31:0] _RAND_179;
  reg [31:0] _RAND_180;
  reg [31:0] _RAND_182;
  reg [31:0] _RAND_183;
  reg [31:0] _RAND_184;
  reg [31:0] _RAND_186;
  reg [31:0] _RAND_187;
  reg [31:0] _RAND_188;
  reg [31:0] _RAND_190;
  reg [31:0] _RAND_191;
  reg [31:0] _RAND_192;
  reg [31:0] _RAND_194;
  reg [31:0] _RAND_195;
  reg [31:0] _RAND_196;
  reg [31:0] _RAND_198;
  reg [31:0] _RAND_199;
  reg [31:0] _RAND_200;
  reg [31:0] _RAND_202;
  reg [31:0] _RAND_203;
  reg [31:0] _RAND_204;
  reg [31:0] _RAND_206;
  reg [31:0] _RAND_207;
  reg [31:0] _RAND_208;
  reg [31:0] _RAND_210;
  reg [31:0] _RAND_211;
  reg [31:0] _RAND_212;
  reg [31:0] _RAND_214;
  reg [31:0] _RAND_215;
  reg [31:0] _RAND_216;
  reg [31:0] _RAND_218;
  reg [31:0] _RAND_219;
  reg [31:0] _RAND_220;
  reg [31:0] _RAND_222;
  reg [31:0] _RAND_223;
  reg [31:0] _RAND_224;
  reg [31:0] _RAND_226;
  reg [31:0] _RAND_227;
  reg [31:0] _RAND_228;
  reg [31:0] _RAND_230;
  reg [31:0] _RAND_231;
  reg [31:0] _RAND_232;
  reg [31:0] _RAND_234;
  reg [31:0] _RAND_235;
  reg [31:0] _RAND_236;
  reg [31:0] _RAND_238;
  reg [31:0] _RAND_239;
  reg [31:0] _RAND_240;
  reg [31:0] _RAND_242;
  reg [31:0] _RAND_243;
  reg [31:0] _RAND_244;
  reg [31:0] _RAND_246;
  reg [31:0] _RAND_247;
  reg [31:0] _RAND_248;
  reg [31:0] _RAND_250;
  reg [31:0] _RAND_251;
  reg [31:0] _RAND_252;
  reg [31:0] _RAND_254;
  reg [31:0] _RAND_255;
  reg [31:0] _RAND_256;
  reg [31:0] _RAND_258;
  reg [31:0] _RAND_259;
  reg [31:0] _RAND_260;
  reg [31:0] _RAND_261;
  reg [31:0] _RAND_262;
  reg [31:0] _RAND_263;
  reg [255:0] _RAND_264;
  reg [255:0] _RAND_265;
  reg [63:0] _RAND_266;
  reg [31:0] _RAND_267;
  reg [63:0] _RAND_268;
  reg [31:0] _RAND_269;
  reg [31:0] _RAND_270;
  reg [63:0] _RAND_271;
  reg [31:0] _RAND_272;
  reg [31:0] _RAND_273;
  reg [511:0] _RAND_274;
  reg [511:0] _RAND_275;
`endif // RANDOMIZE_REG_INIT
  reg [49:0] metaMem_tag [0:255]; // @[AXICache.scala 720:28]
  wire [49:0] metaMem_tag_rmeta_data; // @[AXICache.scala 720:28]
  wire [7:0] metaMem_tag_rmeta_addr; // @[AXICache.scala 720:28]
  wire [49:0] metaMem_tag__T_411_data; // @[AXICache.scala 720:28]
  wire [7:0] metaMem_tag__T_411_addr; // @[AXICache.scala 720:28]
  wire [49:0] metaMem_tag__T_242_data; // @[AXICache.scala 720:28]
  wire [7:0] metaMem_tag__T_242_addr; // @[AXICache.scala 720:28]
  wire  metaMem_tag__T_242_mask; // @[AXICache.scala 720:28]
  wire  metaMem_tag__T_242_en; // @[AXICache.scala 720:28]
  reg  metaMem_tag_rmeta_en_pipe_0;
  reg [7:0] metaMem_tag_rmeta_addr_pipe_0;
  reg  metaMem_tag__T_411_en_pipe_0;
  reg [7:0] metaMem_tag__T_411_addr_pipe_0;
  reg [7:0] dataMem_0_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_0__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_0__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_0__T_8_addr_pipe_0;
  reg  dataMem_0_0__T_106_en_pipe_0;
  reg [7:0] dataMem_0_0__T_106_addr_pipe_0;
  reg [7:0] dataMem_0_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_1__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_1__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_1__T_8_addr_pipe_0;
  reg  dataMem_0_1__T_106_en_pipe_0;
  reg [7:0] dataMem_0_1__T_106_addr_pipe_0;
  reg [7:0] dataMem_0_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_2__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_2__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_2__T_8_addr_pipe_0;
  reg  dataMem_0_2__T_106_en_pipe_0;
  reg [7:0] dataMem_0_2__T_106_addr_pipe_0;
  reg [7:0] dataMem_0_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_3__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_3__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_3__T_8_addr_pipe_0;
  reg  dataMem_0_3__T_106_en_pipe_0;
  reg [7:0] dataMem_0_3__T_106_addr_pipe_0;
  reg [7:0] dataMem_0_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_4__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_4__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_4__T_8_addr_pipe_0;
  reg  dataMem_0_4__T_106_en_pipe_0;
  reg [7:0] dataMem_0_4__T_106_addr_pipe_0;
  reg [7:0] dataMem_0_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_5__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_5__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_5__T_8_addr_pipe_0;
  reg  dataMem_0_5__T_106_en_pipe_0;
  reg [7:0] dataMem_0_5__T_106_addr_pipe_0;
  reg [7:0] dataMem_0_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_6__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_6__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_6__T_8_addr_pipe_0;
  reg  dataMem_0_6__T_106_en_pipe_0;
  reg [7:0] dataMem_0_6__T_106_addr_pipe_0;
  reg [7:0] dataMem_0_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_8_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_8_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_106_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_106_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_261_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_261_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_7__T_261_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_7__T_261_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_7__T_8_addr_pipe_0;
  reg  dataMem_0_7__T_106_en_pipe_0;
  reg [7:0] dataMem_0_7__T_106_addr_pipe_0;
  reg [7:0] dataMem_1_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_0__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_0__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_0__T_18_addr_pipe_0;
  reg  dataMem_1_0__T_117_en_pipe_0;
  reg [7:0] dataMem_1_0__T_117_addr_pipe_0;
  reg [7:0] dataMem_1_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_1__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_1__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_1__T_18_addr_pipe_0;
  reg  dataMem_1_1__T_117_en_pipe_0;
  reg [7:0] dataMem_1_1__T_117_addr_pipe_0;
  reg [7:0] dataMem_1_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_2__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_2__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_2__T_18_addr_pipe_0;
  reg  dataMem_1_2__T_117_en_pipe_0;
  reg [7:0] dataMem_1_2__T_117_addr_pipe_0;
  reg [7:0] dataMem_1_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_3__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_3__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_3__T_18_addr_pipe_0;
  reg  dataMem_1_3__T_117_en_pipe_0;
  reg [7:0] dataMem_1_3__T_117_addr_pipe_0;
  reg [7:0] dataMem_1_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_4__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_4__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_4__T_18_addr_pipe_0;
  reg  dataMem_1_4__T_117_en_pipe_0;
  reg [7:0] dataMem_1_4__T_117_addr_pipe_0;
  reg [7:0] dataMem_1_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_5__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_5__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_5__T_18_addr_pipe_0;
  reg  dataMem_1_5__T_117_en_pipe_0;
  reg [7:0] dataMem_1_5__T_117_addr_pipe_0;
  reg [7:0] dataMem_1_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_6__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_6__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_6__T_18_addr_pipe_0;
  reg  dataMem_1_6__T_117_en_pipe_0;
  reg [7:0] dataMem_1_6__T_117_addr_pipe_0;
  reg [7:0] dataMem_1_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_18_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_18_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_117_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_117_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_280_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_280_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_7__T_280_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_7__T_280_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_7__T_18_addr_pipe_0;
  reg  dataMem_1_7__T_117_en_pipe_0;
  reg [7:0] dataMem_1_7__T_117_addr_pipe_0;
  reg [7:0] dataMem_2_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_0__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_0__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_0__T_28_addr_pipe_0;
  reg  dataMem_2_0__T_128_en_pipe_0;
  reg [7:0] dataMem_2_0__T_128_addr_pipe_0;
  reg [7:0] dataMem_2_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_1__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_1__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_1__T_28_addr_pipe_0;
  reg  dataMem_2_1__T_128_en_pipe_0;
  reg [7:0] dataMem_2_1__T_128_addr_pipe_0;
  reg [7:0] dataMem_2_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_2__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_2__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_2__T_28_addr_pipe_0;
  reg  dataMem_2_2__T_128_en_pipe_0;
  reg [7:0] dataMem_2_2__T_128_addr_pipe_0;
  reg [7:0] dataMem_2_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_3__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_3__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_3__T_28_addr_pipe_0;
  reg  dataMem_2_3__T_128_en_pipe_0;
  reg [7:0] dataMem_2_3__T_128_addr_pipe_0;
  reg [7:0] dataMem_2_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_4__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_4__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_4__T_28_addr_pipe_0;
  reg  dataMem_2_4__T_128_en_pipe_0;
  reg [7:0] dataMem_2_4__T_128_addr_pipe_0;
  reg [7:0] dataMem_2_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_5__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_5__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_5__T_28_addr_pipe_0;
  reg  dataMem_2_5__T_128_en_pipe_0;
  reg [7:0] dataMem_2_5__T_128_addr_pipe_0;
  reg [7:0] dataMem_2_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_6__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_6__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_6__T_28_addr_pipe_0;
  reg  dataMem_2_6__T_128_en_pipe_0;
  reg [7:0] dataMem_2_6__T_128_addr_pipe_0;
  reg [7:0] dataMem_2_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_28_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_28_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_128_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_128_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_299_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_299_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_7__T_299_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_7__T_299_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_7__T_28_addr_pipe_0;
  reg  dataMem_2_7__T_128_en_pipe_0;
  reg [7:0] dataMem_2_7__T_128_addr_pipe_0;
  reg [7:0] dataMem_3_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_0__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_0__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_0__T_38_addr_pipe_0;
  reg  dataMem_3_0__T_139_en_pipe_0;
  reg [7:0] dataMem_3_0__T_139_addr_pipe_0;
  reg [7:0] dataMem_3_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_1__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_1__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_1__T_38_addr_pipe_0;
  reg  dataMem_3_1__T_139_en_pipe_0;
  reg [7:0] dataMem_3_1__T_139_addr_pipe_0;
  reg [7:0] dataMem_3_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_2__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_2__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_2__T_38_addr_pipe_0;
  reg  dataMem_3_2__T_139_en_pipe_0;
  reg [7:0] dataMem_3_2__T_139_addr_pipe_0;
  reg [7:0] dataMem_3_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_3__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_3__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_3__T_38_addr_pipe_0;
  reg  dataMem_3_3__T_139_en_pipe_0;
  reg [7:0] dataMem_3_3__T_139_addr_pipe_0;
  reg [7:0] dataMem_3_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_4__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_4__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_4__T_38_addr_pipe_0;
  reg  dataMem_3_4__T_139_en_pipe_0;
  reg [7:0] dataMem_3_4__T_139_addr_pipe_0;
  reg [7:0] dataMem_3_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_5__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_5__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_5__T_38_addr_pipe_0;
  reg  dataMem_3_5__T_139_en_pipe_0;
  reg [7:0] dataMem_3_5__T_139_addr_pipe_0;
  reg [7:0] dataMem_3_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_6__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_6__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_6__T_38_addr_pipe_0;
  reg  dataMem_3_6__T_139_en_pipe_0;
  reg [7:0] dataMem_3_6__T_139_addr_pipe_0;
  reg [7:0] dataMem_3_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_38_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_38_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_139_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_139_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_318_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_318_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_7__T_318_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_7__T_318_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_7__T_38_addr_pipe_0;
  reg  dataMem_3_7__T_139_en_pipe_0;
  reg [7:0] dataMem_3_7__T_139_addr_pipe_0;
  reg [7:0] dataMem_4_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_0__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_0__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_0__T_48_addr_pipe_0;
  reg  dataMem_4_0__T_150_en_pipe_0;
  reg [7:0] dataMem_4_0__T_150_addr_pipe_0;
  reg [7:0] dataMem_4_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_1__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_1__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_1__T_48_addr_pipe_0;
  reg  dataMem_4_1__T_150_en_pipe_0;
  reg [7:0] dataMem_4_1__T_150_addr_pipe_0;
  reg [7:0] dataMem_4_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_2__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_2__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_2__T_48_addr_pipe_0;
  reg  dataMem_4_2__T_150_en_pipe_0;
  reg [7:0] dataMem_4_2__T_150_addr_pipe_0;
  reg [7:0] dataMem_4_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_3__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_3__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_3__T_48_addr_pipe_0;
  reg  dataMem_4_3__T_150_en_pipe_0;
  reg [7:0] dataMem_4_3__T_150_addr_pipe_0;
  reg [7:0] dataMem_4_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_4__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_4__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_4__T_48_addr_pipe_0;
  reg  dataMem_4_4__T_150_en_pipe_0;
  reg [7:0] dataMem_4_4__T_150_addr_pipe_0;
  reg [7:0] dataMem_4_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_5__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_5__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_5__T_48_addr_pipe_0;
  reg  dataMem_4_5__T_150_en_pipe_0;
  reg [7:0] dataMem_4_5__T_150_addr_pipe_0;
  reg [7:0] dataMem_4_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_6__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_6__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_6__T_48_addr_pipe_0;
  reg  dataMem_4_6__T_150_en_pipe_0;
  reg [7:0] dataMem_4_6__T_150_addr_pipe_0;
  reg [7:0] dataMem_4_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_48_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_48_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_150_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_150_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_337_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_337_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_7__T_337_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_7__T_337_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_7__T_48_addr_pipe_0;
  reg  dataMem_4_7__T_150_en_pipe_0;
  reg [7:0] dataMem_4_7__T_150_addr_pipe_0;
  reg [7:0] dataMem_5_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_0__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_0__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_0__T_58_addr_pipe_0;
  reg  dataMem_5_0__T_161_en_pipe_0;
  reg [7:0] dataMem_5_0__T_161_addr_pipe_0;
  reg [7:0] dataMem_5_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_1__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_1__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_1__T_58_addr_pipe_0;
  reg  dataMem_5_1__T_161_en_pipe_0;
  reg [7:0] dataMem_5_1__T_161_addr_pipe_0;
  reg [7:0] dataMem_5_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_2__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_2__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_2__T_58_addr_pipe_0;
  reg  dataMem_5_2__T_161_en_pipe_0;
  reg [7:0] dataMem_5_2__T_161_addr_pipe_0;
  reg [7:0] dataMem_5_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_3__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_3__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_3__T_58_addr_pipe_0;
  reg  dataMem_5_3__T_161_en_pipe_0;
  reg [7:0] dataMem_5_3__T_161_addr_pipe_0;
  reg [7:0] dataMem_5_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_4__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_4__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_4__T_58_addr_pipe_0;
  reg  dataMem_5_4__T_161_en_pipe_0;
  reg [7:0] dataMem_5_4__T_161_addr_pipe_0;
  reg [7:0] dataMem_5_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_5__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_5__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_5__T_58_addr_pipe_0;
  reg  dataMem_5_5__T_161_en_pipe_0;
  reg [7:0] dataMem_5_5__T_161_addr_pipe_0;
  reg [7:0] dataMem_5_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_6__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_6__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_6__T_58_addr_pipe_0;
  reg  dataMem_5_6__T_161_en_pipe_0;
  reg [7:0] dataMem_5_6__T_161_addr_pipe_0;
  reg [7:0] dataMem_5_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_58_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_58_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_161_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_161_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_356_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_356_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_7__T_356_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_7__T_356_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_7__T_58_addr_pipe_0;
  reg  dataMem_5_7__T_161_en_pipe_0;
  reg [7:0] dataMem_5_7__T_161_addr_pipe_0;
  reg [7:0] dataMem_6_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_0__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_0__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_0__T_68_addr_pipe_0;
  reg  dataMem_6_0__T_172_en_pipe_0;
  reg [7:0] dataMem_6_0__T_172_addr_pipe_0;
  reg [7:0] dataMem_6_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_1__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_1__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_1__T_68_addr_pipe_0;
  reg  dataMem_6_1__T_172_en_pipe_0;
  reg [7:0] dataMem_6_1__T_172_addr_pipe_0;
  reg [7:0] dataMem_6_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_2__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_2__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_2__T_68_addr_pipe_0;
  reg  dataMem_6_2__T_172_en_pipe_0;
  reg [7:0] dataMem_6_2__T_172_addr_pipe_0;
  reg [7:0] dataMem_6_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_3__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_3__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_3__T_68_addr_pipe_0;
  reg  dataMem_6_3__T_172_en_pipe_0;
  reg [7:0] dataMem_6_3__T_172_addr_pipe_0;
  reg [7:0] dataMem_6_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_4__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_4__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_4__T_68_addr_pipe_0;
  reg  dataMem_6_4__T_172_en_pipe_0;
  reg [7:0] dataMem_6_4__T_172_addr_pipe_0;
  reg [7:0] dataMem_6_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_5__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_5__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_5__T_68_addr_pipe_0;
  reg  dataMem_6_5__T_172_en_pipe_0;
  reg [7:0] dataMem_6_5__T_172_addr_pipe_0;
  reg [7:0] dataMem_6_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_6__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_6__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_6__T_68_addr_pipe_0;
  reg  dataMem_6_6__T_172_en_pipe_0;
  reg [7:0] dataMem_6_6__T_172_addr_pipe_0;
  reg [7:0] dataMem_6_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_68_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_68_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_172_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_172_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_375_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_375_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_7__T_375_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_7__T_375_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_7__T_68_addr_pipe_0;
  reg  dataMem_6_7__T_172_en_pipe_0;
  reg [7:0] dataMem_6_7__T_172_addr_pipe_0;
  reg [7:0] dataMem_7_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_0__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_0__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_0__T_78_addr_pipe_0;
  reg  dataMem_7_0__T_183_en_pipe_0;
  reg [7:0] dataMem_7_0__T_183_addr_pipe_0;
  reg [7:0] dataMem_7_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_1__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_1__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_1__T_78_addr_pipe_0;
  reg  dataMem_7_1__T_183_en_pipe_0;
  reg [7:0] dataMem_7_1__T_183_addr_pipe_0;
  reg [7:0] dataMem_7_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_2__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_2__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_2__T_78_addr_pipe_0;
  reg  dataMem_7_2__T_183_en_pipe_0;
  reg [7:0] dataMem_7_2__T_183_addr_pipe_0;
  reg [7:0] dataMem_7_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_3__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_3__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_3__T_78_addr_pipe_0;
  reg  dataMem_7_3__T_183_en_pipe_0;
  reg [7:0] dataMem_7_3__T_183_addr_pipe_0;
  reg [7:0] dataMem_7_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_4__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_4__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_4__T_78_addr_pipe_0;
  reg  dataMem_7_4__T_183_en_pipe_0;
  reg [7:0] dataMem_7_4__T_183_addr_pipe_0;
  reg [7:0] dataMem_7_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_5__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_5__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_5__T_78_addr_pipe_0;
  reg  dataMem_7_5__T_183_en_pipe_0;
  reg [7:0] dataMem_7_5__T_183_addr_pipe_0;
  reg [7:0] dataMem_7_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_6__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_6__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_6__T_78_addr_pipe_0;
  reg  dataMem_7_6__T_183_en_pipe_0;
  reg [7:0] dataMem_7_6__T_183_addr_pipe_0;
  reg [7:0] dataMem_7_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_78_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_78_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_183_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_183_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_394_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_394_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_7__T_394_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_7__T_394_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_7__T_78_addr_pipe_0;
  reg  dataMem_7_7__T_183_en_pipe_0;
  reg [7:0] dataMem_7_7__T_183_addr_pipe_0;
  reg [2:0] state; // @[AXICache.scala 711:22]
  reg [2:0] flush_state; // @[AXICache.scala 714:28]
  reg  flush_mode; // @[AXICache.scala 715:27]
  reg [255:0] v; // @[AXICache.scala 718:18]
  reg [255:0] d; // @[AXICache.scala 719:18]
  reg [63:0] addr_reg; // @[AXICache.scala 723:21]
  reg [7:0] cpu_tag_reg; // @[AXICache.scala 724:24]
  reg [63:0] cpu_data; // @[AXICache.scala 726:21]
  reg [7:0] cpu_mask; // @[AXICache.scala 727:21]
  wire  read_wrap_out = io_mem_rd_data_ready & io_mem_rd_data_valid; // @[Decoupled.scala 40:37]
  wire  write_wrap_out = io_mem_wr_data_ready & io_mem_wr_data_valid; // @[Decoupled.scala 40:37]
  wire  _T_2 = flush_state == 3'h1; // @[AXICache.scala 734:51]
  reg [7:0] set_count; // @[Counter.scala 29:33]
  wire  _T_3 = set_count == 8'hff; // @[Counter.scala 38:24]
  wire [7:0] _T_5 = set_count + 8'h1; // @[Counter.scala 39:22]
  wire  set_wrap = _T_2 & _T_3; // @[Counter.scala 67:17]
  wire [7:0] _T_7 = set_count - 8'h1; // @[AXICache.scala 735:62]
  wire [63:0] _T_15 = {dataMem_0_7__T_8_data,dataMem_0_6__T_8_data,dataMem_0_5__T_8_data,dataMem_0_4__T_8_data,dataMem_0_3__T_8_data,dataMem_0_2__T_8_data,dataMem_0_1__T_8_data,dataMem_0_0__T_8_data}; // @[AXICache.scala 735:69]
  wire [63:0] _T_35 = {dataMem_2_7__T_28_data,dataMem_2_6__T_28_data,dataMem_2_5__T_28_data,dataMem_2_4__T_28_data,dataMem_2_3__T_28_data,dataMem_2_2__T_28_data,dataMem_2_1__T_28_data,dataMem_2_0__T_28_data}; // @[AXICache.scala 735:69]
  wire [63:0] _T_55 = {dataMem_4_7__T_48_data,dataMem_4_6__T_48_data,dataMem_4_5__T_48_data,dataMem_4_4__T_48_data,dataMem_4_3__T_48_data,dataMem_4_2__T_48_data,dataMem_4_1__T_48_data,dataMem_4_0__T_48_data}; // @[AXICache.scala 735:69]
  wire [63:0] _T_75 = {dataMem_6_7__T_68_data,dataMem_6_6__T_68_data,dataMem_6_5__T_68_data,dataMem_6_4__T_68_data,dataMem_6_3__T_68_data,dataMem_6_2__T_68_data,dataMem_6_1__T_68_data,dataMem_6_0__T_68_data}; // @[AXICache.scala 735:69]
  wire [127:0] _T_86 = {dataMem_1_7__T_18_data,dataMem_1_6__T_18_data,dataMem_1_5__T_18_data,dataMem_1_4__T_18_data,dataMem_1_3__T_18_data,dataMem_1_2__T_18_data,dataMem_1_1__T_18_data,dataMem_1_0__T_18_data,_T_15}; // @[Cat.scala 29:58]
  wire [255:0] _T_88 = {dataMem_3_7__T_38_data,dataMem_3_6__T_38_data,dataMem_3_5__T_38_data,dataMem_3_4__T_38_data,dataMem_3_3__T_38_data,dataMem_3_2__T_38_data,dataMem_3_1__T_38_data,dataMem_3_0__T_38_data,_T_35,_T_86}; // @[Cat.scala 29:58]
  wire [127:0] _T_89 = {dataMem_5_7__T_58_data,dataMem_5_6__T_58_data,dataMem_5_5__T_58_data,dataMem_5_4__T_58_data,dataMem_5_3__T_58_data,dataMem_5_2__T_58_data,dataMem_5_1__T_58_data,dataMem_5_0__T_58_data,_T_55}; // @[Cat.scala 29:58]
  wire [255:0] _T_91 = {dataMem_7_7__T_78_data,dataMem_7_6__T_78_data,dataMem_7_5__T_78_data,dataMem_7_4__T_78_data,dataMem_7_3__T_78_data,dataMem_7_2__T_78_data,dataMem_7_1__T_78_data,dataMem_7_0__T_78_data,_T_75,_T_89}; // @[Cat.scala 29:58]
  wire [511:0] dirty_cache_block = {_T_91,_T_88}; // @[Cat.scala 29:58]
  reg [49:0] block_rmeta_tag; // @[AXICache.scala 736:24]
  wire  is_idle = state == 3'h0; // @[AXICache.scala 740:23]
  wire  is_read = state == 3'h1; // @[AXICache.scala 741:23]
  wire  is_write = state == 3'h2; // @[AXICache.scala 742:24]
  wire  _T_92 = state == 3'h6; // @[AXICache.scala 743:24]
  wire  is_alloc = _T_92 & read_wrap_out; // @[AXICache.scala 743:37]
  reg  is_alloc_reg; // @[AXICache.scala 744:29]
  wire [7:0] idx_reg = addr_reg[13:6]; // @[AXICache.scala 754:25]
  wire [255:0] _T_198 = v >> idx_reg; // @[AXICache.scala 763:11]
  wire [49:0] tag_reg = addr_reg[63:14]; // @[AXICache.scala 753:25]
  wire  _T_200 = metaMem_tag_rmeta_data == tag_reg; // @[AXICache.scala 763:34]
  wire  hit = _T_198[0] & _T_200; // @[AXICache.scala 763:21]
  wire  _T_93 = hit | is_alloc_reg; // @[AXICache.scala 747:30]
  wire  _T_94 = is_write & _T_93; // @[AXICache.scala 747:22]
  wire  wen = _T_94 | is_alloc; // @[AXICache.scala 747:64]
  wire  _T_97 = ~wen; // @[AXICache.scala 748:13]
  wire  _T_98 = is_idle | is_read; // @[AXICache.scala 748:30]
  wire  _T_99 = _T_97 & _T_98; // @[AXICache.scala 748:18]
  reg  ren_reg; // @[AXICache.scala 749:24]
  wire [2:0] off_reg = addr_reg[5:3]; // @[AXICache.scala 755:25]
  wire [63:0] _T_113 = {dataMem_0_7__T_106_data,dataMem_0_6__T_106_data,dataMem_0_5__T_106_data,dataMem_0_4__T_106_data,dataMem_0_3__T_106_data,dataMem_0_2__T_106_data,dataMem_0_1__T_106_data,dataMem_0_0__T_106_data}; // @[AXICache.scala 758:50]
  wire [63:0] _T_135 = {dataMem_2_7__T_128_data,dataMem_2_6__T_128_data,dataMem_2_5__T_128_data,dataMem_2_4__T_128_data,dataMem_2_3__T_128_data,dataMem_2_2__T_128_data,dataMem_2_1__T_128_data,dataMem_2_0__T_128_data}; // @[AXICache.scala 758:50]
  wire [63:0] _T_157 = {dataMem_4_7__T_150_data,dataMem_4_6__T_150_data,dataMem_4_5__T_150_data,dataMem_4_4__T_150_data,dataMem_4_3__T_150_data,dataMem_4_2__T_150_data,dataMem_4_1__T_150_data,dataMem_4_0__T_150_data}; // @[AXICache.scala 758:50]
  wire [63:0] _T_179 = {dataMem_6_7__T_172_data,dataMem_6_6__T_172_data,dataMem_6_5__T_172_data,dataMem_6_4__T_172_data,dataMem_6_3__T_172_data,dataMem_6_2__T_172_data,dataMem_6_1__T_172_data,dataMem_6_0__T_172_data}; // @[AXICache.scala 758:50]
  wire [127:0] _T_191 = {dataMem_1_7__T_117_data,dataMem_1_6__T_117_data,dataMem_1_5__T_117_data,dataMem_1_4__T_117_data,dataMem_1_3__T_117_data,dataMem_1_2__T_117_data,dataMem_1_1__T_117_data,dataMem_1_0__T_117_data,_T_113}; // @[Cat.scala 29:58]
  wire [255:0] _T_193 = {dataMem_3_7__T_139_data,dataMem_3_6__T_139_data,dataMem_3_5__T_139_data,dataMem_3_4__T_139_data,dataMem_3_3__T_139_data,dataMem_3_2__T_139_data,dataMem_3_1__T_139_data,dataMem_3_0__T_139_data,_T_135,_T_191}; // @[Cat.scala 29:58]
  wire [127:0] _T_194 = {dataMem_5_7__T_161_data,dataMem_5_6__T_161_data,dataMem_5_5__T_161_data,dataMem_5_4__T_161_data,dataMem_5_3__T_161_data,dataMem_5_2__T_161_data,dataMem_5_1__T_161_data,dataMem_5_0__T_161_data,_T_157}; // @[Cat.scala 29:58]
  wire [255:0] _T_196 = {dataMem_7_7__T_183_data,dataMem_7_6__T_183_data,dataMem_7_5__T_183_data,dataMem_7_4__T_183_data,dataMem_7_3__T_183_data,dataMem_7_2__T_183_data,dataMem_7_1__T_183_data,dataMem_7_0__T_183_data,_T_179,_T_194}; // @[Cat.scala 29:58]
  wire [511:0] rdata = {_T_196,_T_193}; // @[Cat.scala 29:58]
  reg [511:0] rdata_buf; // @[Reg.scala 15:16]
  wire [511:0] _GEN_16 = ren_reg ? rdata : rdata_buf; // @[Reg.scala 16:19]
  reg [511:0] refill_buf_0; // @[AXICache.scala 760:23]
  wire [511:0] read = is_alloc_reg ? refill_buf_0 : _GEN_16; // @[AXICache.scala 761:17]
  wire  _T_203 = is_read & hit; // @[AXICache.scala 765:58]
  wire  _T_204 = is_idle | _T_203; // @[AXICache.scala 765:31]
  wire [63:0] _GEN_18 = 3'h1 == off_reg ? read[127:64] : read[63:0]; // @[AXICache.scala 768:25]
  wire [63:0] _GEN_19 = 3'h2 == off_reg ? read[191:128] : _GEN_18; // @[AXICache.scala 768:25]
  wire [63:0] _GEN_20 = 3'h3 == off_reg ? read[255:192] : _GEN_19; // @[AXICache.scala 768:25]
  wire [63:0] _GEN_21 = 3'h4 == off_reg ? read[319:256] : _GEN_20; // @[AXICache.scala 768:25]
  wire [63:0] _GEN_22 = 3'h5 == off_reg ? read[383:320] : _GEN_21; // @[AXICache.scala 768:25]
  wire [63:0] _GEN_23 = 3'h6 == off_reg ? read[447:384] : _GEN_22; // @[AXICache.scala 768:25]
  wire  _T_216 = |cpu_mask; // @[AXICache.scala 769:79]
  wire  _T_217 = ~_T_216; // @[AXICache.scala 769:69]
  wire  _T_218 = is_alloc_reg & _T_217; // @[AXICache.scala 769:66]
  wire  _T_220 = io_cpu_req_ready & io_cpu_req_valid; // @[Decoupled.scala 40:37]
  wire  _T_221 = ~is_alloc; // @[AXICache.scala 788:19]
  wire [5:0] _T_222 = {off_reg,3'h0}; // @[Cat.scala 29:58]
  wire [70:0] _GEN_374 = {{63'd0}, cpu_mask}; // @[AXICache.scala 788:40]
  wire [70:0] _T_223 = _GEN_374 << _T_222; // @[AXICache.scala 788:40]
  wire [71:0] _T_224 = {1'b0,$signed(_T_223)}; // @[AXICache.scala 788:91]
  wire [71:0] wmask = _T_221 ? $signed(_T_224) : $signed(-72'sh1); // @[AXICache.scala 788:18]
  wire [511:0] _T_228 = {cpu_data,cpu_data,cpu_data,cpu_data,cpu_data,cpu_data,cpu_data,cpu_data}; // @[Cat.scala 29:58]
  wire [511:0] wdata = _T_221 ? _T_228 : io_mem_rd_data_bits; // @[AXICache.scala 789:18]
  wire [255:0] _T_229 = 256'h1 << idx_reg; // @[AXICache.scala 793:18]
  wire [255:0] _T_230 = v | _T_229; // @[AXICache.scala 793:18]
  wire [255:0] _T_237 = d | _T_229; // @[AXICache.scala 794:18]
  wire [255:0] _T_238 = ~d; // @[AXICache.scala 794:18]
  wire [255:0] _T_239 = _T_238 | _T_229; // @[AXICache.scala 794:18]
  wire [255:0] _T_240 = ~_T_239; // @[AXICache.scala 794:18]
  wire [57:0] _T_395 = {tag_reg,idx_reg}; // @[Cat.scala 29:58]
  wire [63:0] _GEN_375 = {_T_395, 6'h0}; // @[AXICache.scala 812:52]
  wire [64:0] _T_396 = {{1'd0}, _GEN_375}; // @[AXICache.scala 812:52]
  wire [255:0] _T_399 = v >> set_count; // @[AXICache.scala 823:25]
  wire [255:0] _T_401 = d >> set_count; // @[AXICache.scala 823:41]
  wire  is_block_dirty = _T_399[0] & _T_401[0]; // @[AXICache.scala 823:37]
  wire [57:0] _T_405 = {block_rmeta_tag,_T_7}; // @[Cat.scala 29:58]
  wire [63:0] _GEN_376 = {_T_405, 6'h0}; // @[AXICache.scala 824:58]
  wire [64:0] block_addr = {{1'd0}, _GEN_376}; // @[AXICache.scala 824:58]
  wire [57:0] _T_412 = {metaMem_tag_rmeta_data,idx_reg}; // @[Cat.scala 29:58]
  wire [63:0] _GEN_377 = {_T_412, 6'h0}; // @[AXICache.scala 835:82]
  wire [64:0] _T_413 = {{1'd0}, _GEN_377}; // @[AXICache.scala 835:82]
  wire [64:0] _T_414 = flush_mode ? block_addr : _T_413; // @[AXICache.scala 835:33]
  wire [255:0] _T_422 = d >> idx_reg; // @[AXICache.scala 853:33]
  wire  is_dirty = _T_198[0] & _T_422[0]; // @[AXICache.scala 853:29]
  wire  _T_424 = 3'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_425 = |io_cpu_req_bits_mask; // @[AXICache.scala 857:43]
  wire  _T_427 = 3'h1 == state; // @[Conditional.scala 37:30]
  wire  _T_430 = ~is_dirty; // @[AXICache.scala 869:32]
  wire  _T_431 = io_mem_wr_cmd_ready & io_mem_wr_cmd_valid; // @[Decoupled.scala 40:37]
  wire  _T_432 = io_mem_rd_cmd_ready & io_mem_rd_cmd_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_311 = hit ? 1'h0 : is_dirty; // @[AXICache.scala 861:17]
  wire  _GEN_312 = hit ? 1'h0 : _T_430; // @[AXICache.scala 861:17]
  wire  _T_433 = 3'h2 == state; // @[Conditional.scala 37:30]
  wire  _GEN_316 = _T_93 ? 1'h0 : is_dirty; // @[AXICache.scala 878:49]
  wire  _GEN_317 = _T_93 ? 1'h0 : _T_430; // @[AXICache.scala 878:49]
  wire  _T_439 = 3'h3 == state; // @[Conditional.scala 37:30]
  wire  _T_440 = 3'h4 == state; // @[Conditional.scala 37:30]
  wire  _T_441 = 3'h5 == state; // @[Conditional.scala 37:30]
  wire  _T_443 = 3'h6 == state; // @[Conditional.scala 37:30]
  wire  _GEN_326 = _T_440 ? 1'h0 : _T_441; // @[Conditional.scala 39:67]
  wire  _GEN_329 = _T_439 ? 1'h0 : _GEN_326; // @[Conditional.scala 39:67]
  wire  _GEN_331 = _T_433 & _GEN_316; // @[Conditional.scala 39:67]
  wire  _GEN_332 = _T_433 ? _GEN_317 : _GEN_329; // @[Conditional.scala 39:67]
  wire  _GEN_333 = _T_433 ? 1'h0 : _T_439; // @[Conditional.scala 39:67]
  wire  _GEN_335 = _T_427 ? _GEN_311 : _GEN_331; // @[Conditional.scala 39:67]
  wire  _GEN_336 = _T_427 ? _GEN_312 : _GEN_332; // @[Conditional.scala 39:67]
  wire  _GEN_337 = _T_427 ? 1'h0 : _GEN_333; // @[Conditional.scala 39:67]
  wire  _GEN_339 = _T_424 ? 1'h0 : _GEN_335; // @[Conditional.scala 40:58]
  wire  _GEN_340 = _T_424 ? 1'h0 : _GEN_336; // @[Conditional.scala 40:58]
  wire  _GEN_341 = _T_424 ? 1'h0 : _GEN_337; // @[Conditional.scala 40:58]
  wire  _T_446 = 3'h0 == flush_state; // @[Conditional.scala 37:30]
  wire  _GEN_343 = io_cpu_flush | flush_mode; // @[AXICache.scala 917:26]
  wire  _T_447 = 3'h1 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_448 = 3'h2 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_449 = 3'h3 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_451 = 3'h4 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_452 = 3'h5 == flush_state; // @[Conditional.scala 37:30]
  wire  _GEN_352 = _T_451 | _GEN_341; // @[Conditional.scala 39:67]
  wire  _GEN_354 = _T_449 | _GEN_339; // @[Conditional.scala 39:67]
  wire  _GEN_355 = _T_449 ? 1'h0 : _GEN_340; // @[Conditional.scala 39:67]
  wire  _GEN_357 = _T_449 ? _GEN_341 : _GEN_352; // @[Conditional.scala 39:67]
  wire  _GEN_359 = _T_448 ? _GEN_339 : _GEN_354; // @[Conditional.scala 39:67]
  wire  _GEN_360 = _T_448 ? _GEN_340 : _GEN_355; // @[Conditional.scala 39:67]
  wire  _GEN_361 = _T_448 ? _GEN_341 : _GEN_357; // @[Conditional.scala 39:67]
  wire  _GEN_362 = _T_447 & set_wrap; // @[Conditional.scala 39:67]
  wire  _GEN_365 = _T_447 ? _GEN_339 : _GEN_359; // @[Conditional.scala 39:67]
  wire  _GEN_366 = _T_447 ? _GEN_340 : _GEN_360; // @[Conditional.scala 39:67]
  wire  _GEN_367 = _T_447 ? _GEN_341 : _GEN_361; // @[Conditional.scala 39:67]
  assign metaMem_tag_rmeta_addr = metaMem_tag_rmeta_addr_pipe_0;
  assign metaMem_tag_rmeta_data = metaMem_tag[metaMem_tag_rmeta_addr]; // @[AXICache.scala 720:28]
  assign metaMem_tag__T_411_addr = metaMem_tag__T_411_addr_pipe_0;
  assign metaMem_tag__T_411_data = metaMem_tag[metaMem_tag__T_411_addr]; // @[AXICache.scala 720:28]
  assign metaMem_tag__T_242_data = addr_reg[63:14];
  assign metaMem_tag__T_242_addr = addr_reg[13:6];
  assign metaMem_tag__T_242_mask = 1'h1;
  assign metaMem_tag__T_242_en = wen & is_alloc;
  assign dataMem_0_0__T_8_addr = dataMem_0_0__T_8_addr_pipe_0;
  assign dataMem_0_0__T_8_data = dataMem_0_0[dataMem_0_0__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_0__T_106_addr = dataMem_0_0__T_106_addr_pipe_0;
  assign dataMem_0_0__T_106_data = dataMem_0_0[dataMem_0_0__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_0__T_261_data = wdata[7:0];
  assign dataMem_0_0__T_261_addr = addr_reg[13:6];
  assign dataMem_0_0__T_261_mask = wmask[0];
  assign dataMem_0_0__T_261_en = _T_94 | is_alloc;
  assign dataMem_0_1__T_8_addr = dataMem_0_1__T_8_addr_pipe_0;
  assign dataMem_0_1__T_8_data = dataMem_0_1[dataMem_0_1__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_1__T_106_addr = dataMem_0_1__T_106_addr_pipe_0;
  assign dataMem_0_1__T_106_data = dataMem_0_1[dataMem_0_1__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_1__T_261_data = wdata[15:8];
  assign dataMem_0_1__T_261_addr = addr_reg[13:6];
  assign dataMem_0_1__T_261_mask = wmask[1];
  assign dataMem_0_1__T_261_en = _T_94 | is_alloc;
  assign dataMem_0_2__T_8_addr = dataMem_0_2__T_8_addr_pipe_0;
  assign dataMem_0_2__T_8_data = dataMem_0_2[dataMem_0_2__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_2__T_106_addr = dataMem_0_2__T_106_addr_pipe_0;
  assign dataMem_0_2__T_106_data = dataMem_0_2[dataMem_0_2__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_2__T_261_data = wdata[23:16];
  assign dataMem_0_2__T_261_addr = addr_reg[13:6];
  assign dataMem_0_2__T_261_mask = wmask[2];
  assign dataMem_0_2__T_261_en = _T_94 | is_alloc;
  assign dataMem_0_3__T_8_addr = dataMem_0_3__T_8_addr_pipe_0;
  assign dataMem_0_3__T_8_data = dataMem_0_3[dataMem_0_3__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_3__T_106_addr = dataMem_0_3__T_106_addr_pipe_0;
  assign dataMem_0_3__T_106_data = dataMem_0_3[dataMem_0_3__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_3__T_261_data = wdata[31:24];
  assign dataMem_0_3__T_261_addr = addr_reg[13:6];
  assign dataMem_0_3__T_261_mask = wmask[3];
  assign dataMem_0_3__T_261_en = _T_94 | is_alloc;
  assign dataMem_0_4__T_8_addr = dataMem_0_4__T_8_addr_pipe_0;
  assign dataMem_0_4__T_8_data = dataMem_0_4[dataMem_0_4__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_4__T_106_addr = dataMem_0_4__T_106_addr_pipe_0;
  assign dataMem_0_4__T_106_data = dataMem_0_4[dataMem_0_4__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_4__T_261_data = wdata[39:32];
  assign dataMem_0_4__T_261_addr = addr_reg[13:6];
  assign dataMem_0_4__T_261_mask = wmask[4];
  assign dataMem_0_4__T_261_en = _T_94 | is_alloc;
  assign dataMem_0_5__T_8_addr = dataMem_0_5__T_8_addr_pipe_0;
  assign dataMem_0_5__T_8_data = dataMem_0_5[dataMem_0_5__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_5__T_106_addr = dataMem_0_5__T_106_addr_pipe_0;
  assign dataMem_0_5__T_106_data = dataMem_0_5[dataMem_0_5__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_5__T_261_data = wdata[47:40];
  assign dataMem_0_5__T_261_addr = addr_reg[13:6];
  assign dataMem_0_5__T_261_mask = wmask[5];
  assign dataMem_0_5__T_261_en = _T_94 | is_alloc;
  assign dataMem_0_6__T_8_addr = dataMem_0_6__T_8_addr_pipe_0;
  assign dataMem_0_6__T_8_data = dataMem_0_6[dataMem_0_6__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_6__T_106_addr = dataMem_0_6__T_106_addr_pipe_0;
  assign dataMem_0_6__T_106_data = dataMem_0_6[dataMem_0_6__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_6__T_261_data = wdata[55:48];
  assign dataMem_0_6__T_261_addr = addr_reg[13:6];
  assign dataMem_0_6__T_261_mask = wmask[6];
  assign dataMem_0_6__T_261_en = _T_94 | is_alloc;
  assign dataMem_0_7__T_8_addr = dataMem_0_7__T_8_addr_pipe_0;
  assign dataMem_0_7__T_8_data = dataMem_0_7[dataMem_0_7__T_8_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_7__T_106_addr = dataMem_0_7__T_106_addr_pipe_0;
  assign dataMem_0_7__T_106_data = dataMem_0_7[dataMem_0_7__T_106_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_7__T_261_data = wdata[63:56];
  assign dataMem_0_7__T_261_addr = addr_reg[13:6];
  assign dataMem_0_7__T_261_mask = wmask[7];
  assign dataMem_0_7__T_261_en = _T_94 | is_alloc;
  assign dataMem_1_0__T_18_addr = dataMem_1_0__T_18_addr_pipe_0;
  assign dataMem_1_0__T_18_data = dataMem_1_0[dataMem_1_0__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_0__T_117_addr = dataMem_1_0__T_117_addr_pipe_0;
  assign dataMem_1_0__T_117_data = dataMem_1_0[dataMem_1_0__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_0__T_280_data = wdata[71:64];
  assign dataMem_1_0__T_280_addr = addr_reg[13:6];
  assign dataMem_1_0__T_280_mask = wmask[8];
  assign dataMem_1_0__T_280_en = _T_94 | is_alloc;
  assign dataMem_1_1__T_18_addr = dataMem_1_1__T_18_addr_pipe_0;
  assign dataMem_1_1__T_18_data = dataMem_1_1[dataMem_1_1__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_1__T_117_addr = dataMem_1_1__T_117_addr_pipe_0;
  assign dataMem_1_1__T_117_data = dataMem_1_1[dataMem_1_1__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_1__T_280_data = wdata[79:72];
  assign dataMem_1_1__T_280_addr = addr_reg[13:6];
  assign dataMem_1_1__T_280_mask = wmask[9];
  assign dataMem_1_1__T_280_en = _T_94 | is_alloc;
  assign dataMem_1_2__T_18_addr = dataMem_1_2__T_18_addr_pipe_0;
  assign dataMem_1_2__T_18_data = dataMem_1_2[dataMem_1_2__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_2__T_117_addr = dataMem_1_2__T_117_addr_pipe_0;
  assign dataMem_1_2__T_117_data = dataMem_1_2[dataMem_1_2__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_2__T_280_data = wdata[87:80];
  assign dataMem_1_2__T_280_addr = addr_reg[13:6];
  assign dataMem_1_2__T_280_mask = wmask[10];
  assign dataMem_1_2__T_280_en = _T_94 | is_alloc;
  assign dataMem_1_3__T_18_addr = dataMem_1_3__T_18_addr_pipe_0;
  assign dataMem_1_3__T_18_data = dataMem_1_3[dataMem_1_3__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_3__T_117_addr = dataMem_1_3__T_117_addr_pipe_0;
  assign dataMem_1_3__T_117_data = dataMem_1_3[dataMem_1_3__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_3__T_280_data = wdata[95:88];
  assign dataMem_1_3__T_280_addr = addr_reg[13:6];
  assign dataMem_1_3__T_280_mask = wmask[11];
  assign dataMem_1_3__T_280_en = _T_94 | is_alloc;
  assign dataMem_1_4__T_18_addr = dataMem_1_4__T_18_addr_pipe_0;
  assign dataMem_1_4__T_18_data = dataMem_1_4[dataMem_1_4__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_4__T_117_addr = dataMem_1_4__T_117_addr_pipe_0;
  assign dataMem_1_4__T_117_data = dataMem_1_4[dataMem_1_4__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_4__T_280_data = wdata[103:96];
  assign dataMem_1_4__T_280_addr = addr_reg[13:6];
  assign dataMem_1_4__T_280_mask = wmask[12];
  assign dataMem_1_4__T_280_en = _T_94 | is_alloc;
  assign dataMem_1_5__T_18_addr = dataMem_1_5__T_18_addr_pipe_0;
  assign dataMem_1_5__T_18_data = dataMem_1_5[dataMem_1_5__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_5__T_117_addr = dataMem_1_5__T_117_addr_pipe_0;
  assign dataMem_1_5__T_117_data = dataMem_1_5[dataMem_1_5__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_5__T_280_data = wdata[111:104];
  assign dataMem_1_5__T_280_addr = addr_reg[13:6];
  assign dataMem_1_5__T_280_mask = wmask[13];
  assign dataMem_1_5__T_280_en = _T_94 | is_alloc;
  assign dataMem_1_6__T_18_addr = dataMem_1_6__T_18_addr_pipe_0;
  assign dataMem_1_6__T_18_data = dataMem_1_6[dataMem_1_6__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_6__T_117_addr = dataMem_1_6__T_117_addr_pipe_0;
  assign dataMem_1_6__T_117_data = dataMem_1_6[dataMem_1_6__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_6__T_280_data = wdata[119:112];
  assign dataMem_1_6__T_280_addr = addr_reg[13:6];
  assign dataMem_1_6__T_280_mask = wmask[14];
  assign dataMem_1_6__T_280_en = _T_94 | is_alloc;
  assign dataMem_1_7__T_18_addr = dataMem_1_7__T_18_addr_pipe_0;
  assign dataMem_1_7__T_18_data = dataMem_1_7[dataMem_1_7__T_18_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_7__T_117_addr = dataMem_1_7__T_117_addr_pipe_0;
  assign dataMem_1_7__T_117_data = dataMem_1_7[dataMem_1_7__T_117_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_7__T_280_data = wdata[127:120];
  assign dataMem_1_7__T_280_addr = addr_reg[13:6];
  assign dataMem_1_7__T_280_mask = wmask[15];
  assign dataMem_1_7__T_280_en = _T_94 | is_alloc;
  assign dataMem_2_0__T_28_addr = dataMem_2_0__T_28_addr_pipe_0;
  assign dataMem_2_0__T_28_data = dataMem_2_0[dataMem_2_0__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_0__T_128_addr = dataMem_2_0__T_128_addr_pipe_0;
  assign dataMem_2_0__T_128_data = dataMem_2_0[dataMem_2_0__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_0__T_299_data = wdata[135:128];
  assign dataMem_2_0__T_299_addr = addr_reg[13:6];
  assign dataMem_2_0__T_299_mask = wmask[16];
  assign dataMem_2_0__T_299_en = _T_94 | is_alloc;
  assign dataMem_2_1__T_28_addr = dataMem_2_1__T_28_addr_pipe_0;
  assign dataMem_2_1__T_28_data = dataMem_2_1[dataMem_2_1__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_1__T_128_addr = dataMem_2_1__T_128_addr_pipe_0;
  assign dataMem_2_1__T_128_data = dataMem_2_1[dataMem_2_1__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_1__T_299_data = wdata[143:136];
  assign dataMem_2_1__T_299_addr = addr_reg[13:6];
  assign dataMem_2_1__T_299_mask = wmask[17];
  assign dataMem_2_1__T_299_en = _T_94 | is_alloc;
  assign dataMem_2_2__T_28_addr = dataMem_2_2__T_28_addr_pipe_0;
  assign dataMem_2_2__T_28_data = dataMem_2_2[dataMem_2_2__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_2__T_128_addr = dataMem_2_2__T_128_addr_pipe_0;
  assign dataMem_2_2__T_128_data = dataMem_2_2[dataMem_2_2__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_2__T_299_data = wdata[151:144];
  assign dataMem_2_2__T_299_addr = addr_reg[13:6];
  assign dataMem_2_2__T_299_mask = wmask[18];
  assign dataMem_2_2__T_299_en = _T_94 | is_alloc;
  assign dataMem_2_3__T_28_addr = dataMem_2_3__T_28_addr_pipe_0;
  assign dataMem_2_3__T_28_data = dataMem_2_3[dataMem_2_3__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_3__T_128_addr = dataMem_2_3__T_128_addr_pipe_0;
  assign dataMem_2_3__T_128_data = dataMem_2_3[dataMem_2_3__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_3__T_299_data = wdata[159:152];
  assign dataMem_2_3__T_299_addr = addr_reg[13:6];
  assign dataMem_2_3__T_299_mask = wmask[19];
  assign dataMem_2_3__T_299_en = _T_94 | is_alloc;
  assign dataMem_2_4__T_28_addr = dataMem_2_4__T_28_addr_pipe_0;
  assign dataMem_2_4__T_28_data = dataMem_2_4[dataMem_2_4__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_4__T_128_addr = dataMem_2_4__T_128_addr_pipe_0;
  assign dataMem_2_4__T_128_data = dataMem_2_4[dataMem_2_4__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_4__T_299_data = wdata[167:160];
  assign dataMem_2_4__T_299_addr = addr_reg[13:6];
  assign dataMem_2_4__T_299_mask = wmask[20];
  assign dataMem_2_4__T_299_en = _T_94 | is_alloc;
  assign dataMem_2_5__T_28_addr = dataMem_2_5__T_28_addr_pipe_0;
  assign dataMem_2_5__T_28_data = dataMem_2_5[dataMem_2_5__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_5__T_128_addr = dataMem_2_5__T_128_addr_pipe_0;
  assign dataMem_2_5__T_128_data = dataMem_2_5[dataMem_2_5__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_5__T_299_data = wdata[175:168];
  assign dataMem_2_5__T_299_addr = addr_reg[13:6];
  assign dataMem_2_5__T_299_mask = wmask[21];
  assign dataMem_2_5__T_299_en = _T_94 | is_alloc;
  assign dataMem_2_6__T_28_addr = dataMem_2_6__T_28_addr_pipe_0;
  assign dataMem_2_6__T_28_data = dataMem_2_6[dataMem_2_6__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_6__T_128_addr = dataMem_2_6__T_128_addr_pipe_0;
  assign dataMem_2_6__T_128_data = dataMem_2_6[dataMem_2_6__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_6__T_299_data = wdata[183:176];
  assign dataMem_2_6__T_299_addr = addr_reg[13:6];
  assign dataMem_2_6__T_299_mask = wmask[22];
  assign dataMem_2_6__T_299_en = _T_94 | is_alloc;
  assign dataMem_2_7__T_28_addr = dataMem_2_7__T_28_addr_pipe_0;
  assign dataMem_2_7__T_28_data = dataMem_2_7[dataMem_2_7__T_28_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_7__T_128_addr = dataMem_2_7__T_128_addr_pipe_0;
  assign dataMem_2_7__T_128_data = dataMem_2_7[dataMem_2_7__T_128_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_7__T_299_data = wdata[191:184];
  assign dataMem_2_7__T_299_addr = addr_reg[13:6];
  assign dataMem_2_7__T_299_mask = wmask[23];
  assign dataMem_2_7__T_299_en = _T_94 | is_alloc;
  assign dataMem_3_0__T_38_addr = dataMem_3_0__T_38_addr_pipe_0;
  assign dataMem_3_0__T_38_data = dataMem_3_0[dataMem_3_0__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_0__T_139_addr = dataMem_3_0__T_139_addr_pipe_0;
  assign dataMem_3_0__T_139_data = dataMem_3_0[dataMem_3_0__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_0__T_318_data = wdata[199:192];
  assign dataMem_3_0__T_318_addr = addr_reg[13:6];
  assign dataMem_3_0__T_318_mask = wmask[24];
  assign dataMem_3_0__T_318_en = _T_94 | is_alloc;
  assign dataMem_3_1__T_38_addr = dataMem_3_1__T_38_addr_pipe_0;
  assign dataMem_3_1__T_38_data = dataMem_3_1[dataMem_3_1__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_1__T_139_addr = dataMem_3_1__T_139_addr_pipe_0;
  assign dataMem_3_1__T_139_data = dataMem_3_1[dataMem_3_1__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_1__T_318_data = wdata[207:200];
  assign dataMem_3_1__T_318_addr = addr_reg[13:6];
  assign dataMem_3_1__T_318_mask = wmask[25];
  assign dataMem_3_1__T_318_en = _T_94 | is_alloc;
  assign dataMem_3_2__T_38_addr = dataMem_3_2__T_38_addr_pipe_0;
  assign dataMem_3_2__T_38_data = dataMem_3_2[dataMem_3_2__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_2__T_139_addr = dataMem_3_2__T_139_addr_pipe_0;
  assign dataMem_3_2__T_139_data = dataMem_3_2[dataMem_3_2__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_2__T_318_data = wdata[215:208];
  assign dataMem_3_2__T_318_addr = addr_reg[13:6];
  assign dataMem_3_2__T_318_mask = wmask[26];
  assign dataMem_3_2__T_318_en = _T_94 | is_alloc;
  assign dataMem_3_3__T_38_addr = dataMem_3_3__T_38_addr_pipe_0;
  assign dataMem_3_3__T_38_data = dataMem_3_3[dataMem_3_3__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_3__T_139_addr = dataMem_3_3__T_139_addr_pipe_0;
  assign dataMem_3_3__T_139_data = dataMem_3_3[dataMem_3_3__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_3__T_318_data = wdata[223:216];
  assign dataMem_3_3__T_318_addr = addr_reg[13:6];
  assign dataMem_3_3__T_318_mask = wmask[27];
  assign dataMem_3_3__T_318_en = _T_94 | is_alloc;
  assign dataMem_3_4__T_38_addr = dataMem_3_4__T_38_addr_pipe_0;
  assign dataMem_3_4__T_38_data = dataMem_3_4[dataMem_3_4__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_4__T_139_addr = dataMem_3_4__T_139_addr_pipe_0;
  assign dataMem_3_4__T_139_data = dataMem_3_4[dataMem_3_4__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_4__T_318_data = wdata[231:224];
  assign dataMem_3_4__T_318_addr = addr_reg[13:6];
  assign dataMem_3_4__T_318_mask = wmask[28];
  assign dataMem_3_4__T_318_en = _T_94 | is_alloc;
  assign dataMem_3_5__T_38_addr = dataMem_3_5__T_38_addr_pipe_0;
  assign dataMem_3_5__T_38_data = dataMem_3_5[dataMem_3_5__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_5__T_139_addr = dataMem_3_5__T_139_addr_pipe_0;
  assign dataMem_3_5__T_139_data = dataMem_3_5[dataMem_3_5__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_5__T_318_data = wdata[239:232];
  assign dataMem_3_5__T_318_addr = addr_reg[13:6];
  assign dataMem_3_5__T_318_mask = wmask[29];
  assign dataMem_3_5__T_318_en = _T_94 | is_alloc;
  assign dataMem_3_6__T_38_addr = dataMem_3_6__T_38_addr_pipe_0;
  assign dataMem_3_6__T_38_data = dataMem_3_6[dataMem_3_6__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_6__T_139_addr = dataMem_3_6__T_139_addr_pipe_0;
  assign dataMem_3_6__T_139_data = dataMem_3_6[dataMem_3_6__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_6__T_318_data = wdata[247:240];
  assign dataMem_3_6__T_318_addr = addr_reg[13:6];
  assign dataMem_3_6__T_318_mask = wmask[30];
  assign dataMem_3_6__T_318_en = _T_94 | is_alloc;
  assign dataMem_3_7__T_38_addr = dataMem_3_7__T_38_addr_pipe_0;
  assign dataMem_3_7__T_38_data = dataMem_3_7[dataMem_3_7__T_38_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_7__T_139_addr = dataMem_3_7__T_139_addr_pipe_0;
  assign dataMem_3_7__T_139_data = dataMem_3_7[dataMem_3_7__T_139_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_7__T_318_data = wdata[255:248];
  assign dataMem_3_7__T_318_addr = addr_reg[13:6];
  assign dataMem_3_7__T_318_mask = wmask[31];
  assign dataMem_3_7__T_318_en = _T_94 | is_alloc;
  assign dataMem_4_0__T_48_addr = dataMem_4_0__T_48_addr_pipe_0;
  assign dataMem_4_0__T_48_data = dataMem_4_0[dataMem_4_0__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_0__T_150_addr = dataMem_4_0__T_150_addr_pipe_0;
  assign dataMem_4_0__T_150_data = dataMem_4_0[dataMem_4_0__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_0__T_337_data = wdata[263:256];
  assign dataMem_4_0__T_337_addr = addr_reg[13:6];
  assign dataMem_4_0__T_337_mask = wmask[32];
  assign dataMem_4_0__T_337_en = _T_94 | is_alloc;
  assign dataMem_4_1__T_48_addr = dataMem_4_1__T_48_addr_pipe_0;
  assign dataMem_4_1__T_48_data = dataMem_4_1[dataMem_4_1__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_1__T_150_addr = dataMem_4_1__T_150_addr_pipe_0;
  assign dataMem_4_1__T_150_data = dataMem_4_1[dataMem_4_1__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_1__T_337_data = wdata[271:264];
  assign dataMem_4_1__T_337_addr = addr_reg[13:6];
  assign dataMem_4_1__T_337_mask = wmask[33];
  assign dataMem_4_1__T_337_en = _T_94 | is_alloc;
  assign dataMem_4_2__T_48_addr = dataMem_4_2__T_48_addr_pipe_0;
  assign dataMem_4_2__T_48_data = dataMem_4_2[dataMem_4_2__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_2__T_150_addr = dataMem_4_2__T_150_addr_pipe_0;
  assign dataMem_4_2__T_150_data = dataMem_4_2[dataMem_4_2__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_2__T_337_data = wdata[279:272];
  assign dataMem_4_2__T_337_addr = addr_reg[13:6];
  assign dataMem_4_2__T_337_mask = wmask[34];
  assign dataMem_4_2__T_337_en = _T_94 | is_alloc;
  assign dataMem_4_3__T_48_addr = dataMem_4_3__T_48_addr_pipe_0;
  assign dataMem_4_3__T_48_data = dataMem_4_3[dataMem_4_3__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_3__T_150_addr = dataMem_4_3__T_150_addr_pipe_0;
  assign dataMem_4_3__T_150_data = dataMem_4_3[dataMem_4_3__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_3__T_337_data = wdata[287:280];
  assign dataMem_4_3__T_337_addr = addr_reg[13:6];
  assign dataMem_4_3__T_337_mask = wmask[35];
  assign dataMem_4_3__T_337_en = _T_94 | is_alloc;
  assign dataMem_4_4__T_48_addr = dataMem_4_4__T_48_addr_pipe_0;
  assign dataMem_4_4__T_48_data = dataMem_4_4[dataMem_4_4__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_4__T_150_addr = dataMem_4_4__T_150_addr_pipe_0;
  assign dataMem_4_4__T_150_data = dataMem_4_4[dataMem_4_4__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_4__T_337_data = wdata[295:288];
  assign dataMem_4_4__T_337_addr = addr_reg[13:6];
  assign dataMem_4_4__T_337_mask = wmask[36];
  assign dataMem_4_4__T_337_en = _T_94 | is_alloc;
  assign dataMem_4_5__T_48_addr = dataMem_4_5__T_48_addr_pipe_0;
  assign dataMem_4_5__T_48_data = dataMem_4_5[dataMem_4_5__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_5__T_150_addr = dataMem_4_5__T_150_addr_pipe_0;
  assign dataMem_4_5__T_150_data = dataMem_4_5[dataMem_4_5__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_5__T_337_data = wdata[303:296];
  assign dataMem_4_5__T_337_addr = addr_reg[13:6];
  assign dataMem_4_5__T_337_mask = wmask[37];
  assign dataMem_4_5__T_337_en = _T_94 | is_alloc;
  assign dataMem_4_6__T_48_addr = dataMem_4_6__T_48_addr_pipe_0;
  assign dataMem_4_6__T_48_data = dataMem_4_6[dataMem_4_6__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_6__T_150_addr = dataMem_4_6__T_150_addr_pipe_0;
  assign dataMem_4_6__T_150_data = dataMem_4_6[dataMem_4_6__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_6__T_337_data = wdata[311:304];
  assign dataMem_4_6__T_337_addr = addr_reg[13:6];
  assign dataMem_4_6__T_337_mask = wmask[38];
  assign dataMem_4_6__T_337_en = _T_94 | is_alloc;
  assign dataMem_4_7__T_48_addr = dataMem_4_7__T_48_addr_pipe_0;
  assign dataMem_4_7__T_48_data = dataMem_4_7[dataMem_4_7__T_48_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_7__T_150_addr = dataMem_4_7__T_150_addr_pipe_0;
  assign dataMem_4_7__T_150_data = dataMem_4_7[dataMem_4_7__T_150_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_7__T_337_data = wdata[319:312];
  assign dataMem_4_7__T_337_addr = addr_reg[13:6];
  assign dataMem_4_7__T_337_mask = wmask[39];
  assign dataMem_4_7__T_337_en = _T_94 | is_alloc;
  assign dataMem_5_0__T_58_addr = dataMem_5_0__T_58_addr_pipe_0;
  assign dataMem_5_0__T_58_data = dataMem_5_0[dataMem_5_0__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_0__T_161_addr = dataMem_5_0__T_161_addr_pipe_0;
  assign dataMem_5_0__T_161_data = dataMem_5_0[dataMem_5_0__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_0__T_356_data = wdata[327:320];
  assign dataMem_5_0__T_356_addr = addr_reg[13:6];
  assign dataMem_5_0__T_356_mask = wmask[40];
  assign dataMem_5_0__T_356_en = _T_94 | is_alloc;
  assign dataMem_5_1__T_58_addr = dataMem_5_1__T_58_addr_pipe_0;
  assign dataMem_5_1__T_58_data = dataMem_5_1[dataMem_5_1__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_1__T_161_addr = dataMem_5_1__T_161_addr_pipe_0;
  assign dataMem_5_1__T_161_data = dataMem_5_1[dataMem_5_1__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_1__T_356_data = wdata[335:328];
  assign dataMem_5_1__T_356_addr = addr_reg[13:6];
  assign dataMem_5_1__T_356_mask = wmask[41];
  assign dataMem_5_1__T_356_en = _T_94 | is_alloc;
  assign dataMem_5_2__T_58_addr = dataMem_5_2__T_58_addr_pipe_0;
  assign dataMem_5_2__T_58_data = dataMem_5_2[dataMem_5_2__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_2__T_161_addr = dataMem_5_2__T_161_addr_pipe_0;
  assign dataMem_5_2__T_161_data = dataMem_5_2[dataMem_5_2__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_2__T_356_data = wdata[343:336];
  assign dataMem_5_2__T_356_addr = addr_reg[13:6];
  assign dataMem_5_2__T_356_mask = wmask[42];
  assign dataMem_5_2__T_356_en = _T_94 | is_alloc;
  assign dataMem_5_3__T_58_addr = dataMem_5_3__T_58_addr_pipe_0;
  assign dataMem_5_3__T_58_data = dataMem_5_3[dataMem_5_3__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_3__T_161_addr = dataMem_5_3__T_161_addr_pipe_0;
  assign dataMem_5_3__T_161_data = dataMem_5_3[dataMem_5_3__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_3__T_356_data = wdata[351:344];
  assign dataMem_5_3__T_356_addr = addr_reg[13:6];
  assign dataMem_5_3__T_356_mask = wmask[43];
  assign dataMem_5_3__T_356_en = _T_94 | is_alloc;
  assign dataMem_5_4__T_58_addr = dataMem_5_4__T_58_addr_pipe_0;
  assign dataMem_5_4__T_58_data = dataMem_5_4[dataMem_5_4__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_4__T_161_addr = dataMem_5_4__T_161_addr_pipe_0;
  assign dataMem_5_4__T_161_data = dataMem_5_4[dataMem_5_4__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_4__T_356_data = wdata[359:352];
  assign dataMem_5_4__T_356_addr = addr_reg[13:6];
  assign dataMem_5_4__T_356_mask = wmask[44];
  assign dataMem_5_4__T_356_en = _T_94 | is_alloc;
  assign dataMem_5_5__T_58_addr = dataMem_5_5__T_58_addr_pipe_0;
  assign dataMem_5_5__T_58_data = dataMem_5_5[dataMem_5_5__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_5__T_161_addr = dataMem_5_5__T_161_addr_pipe_0;
  assign dataMem_5_5__T_161_data = dataMem_5_5[dataMem_5_5__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_5__T_356_data = wdata[367:360];
  assign dataMem_5_5__T_356_addr = addr_reg[13:6];
  assign dataMem_5_5__T_356_mask = wmask[45];
  assign dataMem_5_5__T_356_en = _T_94 | is_alloc;
  assign dataMem_5_6__T_58_addr = dataMem_5_6__T_58_addr_pipe_0;
  assign dataMem_5_6__T_58_data = dataMem_5_6[dataMem_5_6__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_6__T_161_addr = dataMem_5_6__T_161_addr_pipe_0;
  assign dataMem_5_6__T_161_data = dataMem_5_6[dataMem_5_6__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_6__T_356_data = wdata[375:368];
  assign dataMem_5_6__T_356_addr = addr_reg[13:6];
  assign dataMem_5_6__T_356_mask = wmask[46];
  assign dataMem_5_6__T_356_en = _T_94 | is_alloc;
  assign dataMem_5_7__T_58_addr = dataMem_5_7__T_58_addr_pipe_0;
  assign dataMem_5_7__T_58_data = dataMem_5_7[dataMem_5_7__T_58_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_7__T_161_addr = dataMem_5_7__T_161_addr_pipe_0;
  assign dataMem_5_7__T_161_data = dataMem_5_7[dataMem_5_7__T_161_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_7__T_356_data = wdata[383:376];
  assign dataMem_5_7__T_356_addr = addr_reg[13:6];
  assign dataMem_5_7__T_356_mask = wmask[47];
  assign dataMem_5_7__T_356_en = _T_94 | is_alloc;
  assign dataMem_6_0__T_68_addr = dataMem_6_0__T_68_addr_pipe_0;
  assign dataMem_6_0__T_68_data = dataMem_6_0[dataMem_6_0__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_0__T_172_addr = dataMem_6_0__T_172_addr_pipe_0;
  assign dataMem_6_0__T_172_data = dataMem_6_0[dataMem_6_0__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_0__T_375_data = wdata[391:384];
  assign dataMem_6_0__T_375_addr = addr_reg[13:6];
  assign dataMem_6_0__T_375_mask = wmask[48];
  assign dataMem_6_0__T_375_en = _T_94 | is_alloc;
  assign dataMem_6_1__T_68_addr = dataMem_6_1__T_68_addr_pipe_0;
  assign dataMem_6_1__T_68_data = dataMem_6_1[dataMem_6_1__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_1__T_172_addr = dataMem_6_1__T_172_addr_pipe_0;
  assign dataMem_6_1__T_172_data = dataMem_6_1[dataMem_6_1__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_1__T_375_data = wdata[399:392];
  assign dataMem_6_1__T_375_addr = addr_reg[13:6];
  assign dataMem_6_1__T_375_mask = wmask[49];
  assign dataMem_6_1__T_375_en = _T_94 | is_alloc;
  assign dataMem_6_2__T_68_addr = dataMem_6_2__T_68_addr_pipe_0;
  assign dataMem_6_2__T_68_data = dataMem_6_2[dataMem_6_2__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_2__T_172_addr = dataMem_6_2__T_172_addr_pipe_0;
  assign dataMem_6_2__T_172_data = dataMem_6_2[dataMem_6_2__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_2__T_375_data = wdata[407:400];
  assign dataMem_6_2__T_375_addr = addr_reg[13:6];
  assign dataMem_6_2__T_375_mask = wmask[50];
  assign dataMem_6_2__T_375_en = _T_94 | is_alloc;
  assign dataMem_6_3__T_68_addr = dataMem_6_3__T_68_addr_pipe_0;
  assign dataMem_6_3__T_68_data = dataMem_6_3[dataMem_6_3__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_3__T_172_addr = dataMem_6_3__T_172_addr_pipe_0;
  assign dataMem_6_3__T_172_data = dataMem_6_3[dataMem_6_3__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_3__T_375_data = wdata[415:408];
  assign dataMem_6_3__T_375_addr = addr_reg[13:6];
  assign dataMem_6_3__T_375_mask = wmask[51];
  assign dataMem_6_3__T_375_en = _T_94 | is_alloc;
  assign dataMem_6_4__T_68_addr = dataMem_6_4__T_68_addr_pipe_0;
  assign dataMem_6_4__T_68_data = dataMem_6_4[dataMem_6_4__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_4__T_172_addr = dataMem_6_4__T_172_addr_pipe_0;
  assign dataMem_6_4__T_172_data = dataMem_6_4[dataMem_6_4__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_4__T_375_data = wdata[423:416];
  assign dataMem_6_4__T_375_addr = addr_reg[13:6];
  assign dataMem_6_4__T_375_mask = wmask[52];
  assign dataMem_6_4__T_375_en = _T_94 | is_alloc;
  assign dataMem_6_5__T_68_addr = dataMem_6_5__T_68_addr_pipe_0;
  assign dataMem_6_5__T_68_data = dataMem_6_5[dataMem_6_5__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_5__T_172_addr = dataMem_6_5__T_172_addr_pipe_0;
  assign dataMem_6_5__T_172_data = dataMem_6_5[dataMem_6_5__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_5__T_375_data = wdata[431:424];
  assign dataMem_6_5__T_375_addr = addr_reg[13:6];
  assign dataMem_6_5__T_375_mask = wmask[53];
  assign dataMem_6_5__T_375_en = _T_94 | is_alloc;
  assign dataMem_6_6__T_68_addr = dataMem_6_6__T_68_addr_pipe_0;
  assign dataMem_6_6__T_68_data = dataMem_6_6[dataMem_6_6__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_6__T_172_addr = dataMem_6_6__T_172_addr_pipe_0;
  assign dataMem_6_6__T_172_data = dataMem_6_6[dataMem_6_6__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_6__T_375_data = wdata[439:432];
  assign dataMem_6_6__T_375_addr = addr_reg[13:6];
  assign dataMem_6_6__T_375_mask = wmask[54];
  assign dataMem_6_6__T_375_en = _T_94 | is_alloc;
  assign dataMem_6_7__T_68_addr = dataMem_6_7__T_68_addr_pipe_0;
  assign dataMem_6_7__T_68_data = dataMem_6_7[dataMem_6_7__T_68_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_7__T_172_addr = dataMem_6_7__T_172_addr_pipe_0;
  assign dataMem_6_7__T_172_data = dataMem_6_7[dataMem_6_7__T_172_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_7__T_375_data = wdata[447:440];
  assign dataMem_6_7__T_375_addr = addr_reg[13:6];
  assign dataMem_6_7__T_375_mask = wmask[55];
  assign dataMem_6_7__T_375_en = _T_94 | is_alloc;
  assign dataMem_7_0__T_78_addr = dataMem_7_0__T_78_addr_pipe_0;
  assign dataMem_7_0__T_78_data = dataMem_7_0[dataMem_7_0__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_0__T_183_addr = dataMem_7_0__T_183_addr_pipe_0;
  assign dataMem_7_0__T_183_data = dataMem_7_0[dataMem_7_0__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_0__T_394_data = wdata[455:448];
  assign dataMem_7_0__T_394_addr = addr_reg[13:6];
  assign dataMem_7_0__T_394_mask = wmask[56];
  assign dataMem_7_0__T_394_en = _T_94 | is_alloc;
  assign dataMem_7_1__T_78_addr = dataMem_7_1__T_78_addr_pipe_0;
  assign dataMem_7_1__T_78_data = dataMem_7_1[dataMem_7_1__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_1__T_183_addr = dataMem_7_1__T_183_addr_pipe_0;
  assign dataMem_7_1__T_183_data = dataMem_7_1[dataMem_7_1__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_1__T_394_data = wdata[463:456];
  assign dataMem_7_1__T_394_addr = addr_reg[13:6];
  assign dataMem_7_1__T_394_mask = wmask[57];
  assign dataMem_7_1__T_394_en = _T_94 | is_alloc;
  assign dataMem_7_2__T_78_addr = dataMem_7_2__T_78_addr_pipe_0;
  assign dataMem_7_2__T_78_data = dataMem_7_2[dataMem_7_2__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_2__T_183_addr = dataMem_7_2__T_183_addr_pipe_0;
  assign dataMem_7_2__T_183_data = dataMem_7_2[dataMem_7_2__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_2__T_394_data = wdata[471:464];
  assign dataMem_7_2__T_394_addr = addr_reg[13:6];
  assign dataMem_7_2__T_394_mask = wmask[58];
  assign dataMem_7_2__T_394_en = _T_94 | is_alloc;
  assign dataMem_7_3__T_78_addr = dataMem_7_3__T_78_addr_pipe_0;
  assign dataMem_7_3__T_78_data = dataMem_7_3[dataMem_7_3__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_3__T_183_addr = dataMem_7_3__T_183_addr_pipe_0;
  assign dataMem_7_3__T_183_data = dataMem_7_3[dataMem_7_3__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_3__T_394_data = wdata[479:472];
  assign dataMem_7_3__T_394_addr = addr_reg[13:6];
  assign dataMem_7_3__T_394_mask = wmask[59];
  assign dataMem_7_3__T_394_en = _T_94 | is_alloc;
  assign dataMem_7_4__T_78_addr = dataMem_7_4__T_78_addr_pipe_0;
  assign dataMem_7_4__T_78_data = dataMem_7_4[dataMem_7_4__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_4__T_183_addr = dataMem_7_4__T_183_addr_pipe_0;
  assign dataMem_7_4__T_183_data = dataMem_7_4[dataMem_7_4__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_4__T_394_data = wdata[487:480];
  assign dataMem_7_4__T_394_addr = addr_reg[13:6];
  assign dataMem_7_4__T_394_mask = wmask[60];
  assign dataMem_7_4__T_394_en = _T_94 | is_alloc;
  assign dataMem_7_5__T_78_addr = dataMem_7_5__T_78_addr_pipe_0;
  assign dataMem_7_5__T_78_data = dataMem_7_5[dataMem_7_5__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_5__T_183_addr = dataMem_7_5__T_183_addr_pipe_0;
  assign dataMem_7_5__T_183_data = dataMem_7_5[dataMem_7_5__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_5__T_394_data = wdata[495:488];
  assign dataMem_7_5__T_394_addr = addr_reg[13:6];
  assign dataMem_7_5__T_394_mask = wmask[61];
  assign dataMem_7_5__T_394_en = _T_94 | is_alloc;
  assign dataMem_7_6__T_78_addr = dataMem_7_6__T_78_addr_pipe_0;
  assign dataMem_7_6__T_78_data = dataMem_7_6[dataMem_7_6__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_6__T_183_addr = dataMem_7_6__T_183_addr_pipe_0;
  assign dataMem_7_6__T_183_data = dataMem_7_6[dataMem_7_6__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_6__T_394_data = wdata[503:496];
  assign dataMem_7_6__T_394_addr = addr_reg[13:6];
  assign dataMem_7_6__T_394_mask = wmask[62];
  assign dataMem_7_6__T_394_en = _T_94 | is_alloc;
  assign dataMem_7_7__T_78_addr = dataMem_7_7__T_78_addr_pipe_0;
  assign dataMem_7_7__T_78_data = dataMem_7_7[dataMem_7_7__T_78_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_7__T_183_addr = dataMem_7_7__T_183_addr_pipe_0;
  assign dataMem_7_7__T_183_data = dataMem_7_7[dataMem_7_7__T_183_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_7__T_394_data = wdata[511:504];
  assign dataMem_7_7__T_394_addr = addr_reg[13:6];
  assign dataMem_7_7__T_394_mask = wmask[63];
  assign dataMem_7_7__T_394_en = _T_94 | is_alloc;
  assign io_cpu_flush_done = _T_446 ? 1'h0 : _GEN_362; // @[AXICache.scala 850:21 AXICache.scala 924:27]
  assign io_cpu_req_ready = is_idle | _T_203; // @[AXICache.scala 765:20]
  assign io_cpu_resp_valid = _T_204 | _T_218; // @[AXICache.scala 769:21]
  assign io_cpu_resp_bits_data = 3'h7 == off_reg ? read[511:448] : _GEN_23; // @[AXICache.scala 768:25]
  assign io_cpu_resp_bits_tag = cpu_tag_reg; // @[AXICache.scala 771:24]
  assign io_mem_rd_cmd_valid = _T_446 ? _GEN_340 : _GEN_366; // @[AXICache.scala 814:23 AXICache.scala 869:29 AXICache.scala 882:29 AXICache.scala 902:27 AXICache.scala 956:27]
  assign io_mem_rd_cmd_bits_addr = _T_396[63:0]; // @[AXICache.scala 812:27]
  assign io_mem_rd_data_ready = state == 3'h6; // @[AXICache.scala 817:24]
  assign io_mem_wr_cmd_valid = _T_446 ? _GEN_339 : _GEN_365; // @[AXICache.scala 837:23 AXICache.scala 868:29 AXICache.scala 881:29 AXICache.scala 955:27]
  assign io_mem_wr_cmd_bits_addr = _T_414[63:0]; // @[AXICache.scala 835:27]
  assign io_mem_wr_data_valid = _T_446 ? _GEN_341 : _GEN_367; // @[AXICache.scala 847:24 AXICache.scala 891:28 AXICache.scala 966:28]
  assign io_mem_wr_data_bits = flush_mode ? dirty_cache_block : read; // @[AXICache.scala 840:23]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_MEM_INIT
  _RAND_0 = {2{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    metaMem_tag[initvar] = _RAND_0[49:0];
  _RAND_5 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_0[initvar] = _RAND_5[7:0];
  _RAND_9 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_1[initvar] = _RAND_9[7:0];
  _RAND_13 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_2[initvar] = _RAND_13[7:0];
  _RAND_17 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_3[initvar] = _RAND_17[7:0];
  _RAND_21 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_4[initvar] = _RAND_21[7:0];
  _RAND_25 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_5[initvar] = _RAND_25[7:0];
  _RAND_29 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_6[initvar] = _RAND_29[7:0];
  _RAND_33 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_7[initvar] = _RAND_33[7:0];
  _RAND_37 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_0[initvar] = _RAND_37[7:0];
  _RAND_41 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_1[initvar] = _RAND_41[7:0];
  _RAND_45 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_2[initvar] = _RAND_45[7:0];
  _RAND_49 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_3[initvar] = _RAND_49[7:0];
  _RAND_53 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_4[initvar] = _RAND_53[7:0];
  _RAND_57 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_5[initvar] = _RAND_57[7:0];
  _RAND_61 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_6[initvar] = _RAND_61[7:0];
  _RAND_65 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_7[initvar] = _RAND_65[7:0];
  _RAND_69 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_0[initvar] = _RAND_69[7:0];
  _RAND_73 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_1[initvar] = _RAND_73[7:0];
  _RAND_77 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_2[initvar] = _RAND_77[7:0];
  _RAND_81 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_3[initvar] = _RAND_81[7:0];
  _RAND_85 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_4[initvar] = _RAND_85[7:0];
  _RAND_89 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_5[initvar] = _RAND_89[7:0];
  _RAND_93 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_6[initvar] = _RAND_93[7:0];
  _RAND_97 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_7[initvar] = _RAND_97[7:0];
  _RAND_101 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_0[initvar] = _RAND_101[7:0];
  _RAND_105 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_1[initvar] = _RAND_105[7:0];
  _RAND_109 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_2[initvar] = _RAND_109[7:0];
  _RAND_113 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_3[initvar] = _RAND_113[7:0];
  _RAND_117 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_4[initvar] = _RAND_117[7:0];
  _RAND_121 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_5[initvar] = _RAND_121[7:0];
  _RAND_125 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_6[initvar] = _RAND_125[7:0];
  _RAND_129 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_7[initvar] = _RAND_129[7:0];
  _RAND_133 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_0[initvar] = _RAND_133[7:0];
  _RAND_137 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_1[initvar] = _RAND_137[7:0];
  _RAND_141 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_2[initvar] = _RAND_141[7:0];
  _RAND_145 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_3[initvar] = _RAND_145[7:0];
  _RAND_149 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_4[initvar] = _RAND_149[7:0];
  _RAND_153 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_5[initvar] = _RAND_153[7:0];
  _RAND_157 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_6[initvar] = _RAND_157[7:0];
  _RAND_161 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_7[initvar] = _RAND_161[7:0];
  _RAND_165 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_0[initvar] = _RAND_165[7:0];
  _RAND_169 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_1[initvar] = _RAND_169[7:0];
  _RAND_173 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_2[initvar] = _RAND_173[7:0];
  _RAND_177 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_3[initvar] = _RAND_177[7:0];
  _RAND_181 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_4[initvar] = _RAND_181[7:0];
  _RAND_185 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_5[initvar] = _RAND_185[7:0];
  _RAND_189 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_6[initvar] = _RAND_189[7:0];
  _RAND_193 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_7[initvar] = _RAND_193[7:0];
  _RAND_197 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_0[initvar] = _RAND_197[7:0];
  _RAND_201 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_1[initvar] = _RAND_201[7:0];
  _RAND_205 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_2[initvar] = _RAND_205[7:0];
  _RAND_209 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_3[initvar] = _RAND_209[7:0];
  _RAND_213 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_4[initvar] = _RAND_213[7:0];
  _RAND_217 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_5[initvar] = _RAND_217[7:0];
  _RAND_221 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_6[initvar] = _RAND_221[7:0];
  _RAND_225 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_7[initvar] = _RAND_225[7:0];
  _RAND_229 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_0[initvar] = _RAND_229[7:0];
  _RAND_233 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_1[initvar] = _RAND_233[7:0];
  _RAND_237 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_2[initvar] = _RAND_237[7:0];
  _RAND_241 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_3[initvar] = _RAND_241[7:0];
  _RAND_245 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_4[initvar] = _RAND_245[7:0];
  _RAND_249 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_5[initvar] = _RAND_249[7:0];
  _RAND_253 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_6[initvar] = _RAND_253[7:0];
  _RAND_257 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_7[initvar] = _RAND_257[7:0];
`endif // RANDOMIZE_MEM_INIT
`ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  metaMem_tag_rmeta_en_pipe_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  metaMem_tag_rmeta_addr_pipe_0 = _RAND_2[7:0];
  _RAND_3 = {1{`RANDOM}};
  metaMem_tag__T_411_en_pipe_0 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  metaMem_tag__T_411_addr_pipe_0 = _RAND_4[7:0];
  _RAND_6 = {1{`RANDOM}};
  dataMem_0_0__T_8_addr_pipe_0 = _RAND_6[7:0];
  _RAND_7 = {1{`RANDOM}};
  dataMem_0_0__T_106_en_pipe_0 = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  dataMem_0_0__T_106_addr_pipe_0 = _RAND_8[7:0];
  _RAND_10 = {1{`RANDOM}};
  dataMem_0_1__T_8_addr_pipe_0 = _RAND_10[7:0];
  _RAND_11 = {1{`RANDOM}};
  dataMem_0_1__T_106_en_pipe_0 = _RAND_11[0:0];
  _RAND_12 = {1{`RANDOM}};
  dataMem_0_1__T_106_addr_pipe_0 = _RAND_12[7:0];
  _RAND_14 = {1{`RANDOM}};
  dataMem_0_2__T_8_addr_pipe_0 = _RAND_14[7:0];
  _RAND_15 = {1{`RANDOM}};
  dataMem_0_2__T_106_en_pipe_0 = _RAND_15[0:0];
  _RAND_16 = {1{`RANDOM}};
  dataMem_0_2__T_106_addr_pipe_0 = _RAND_16[7:0];
  _RAND_18 = {1{`RANDOM}};
  dataMem_0_3__T_8_addr_pipe_0 = _RAND_18[7:0];
  _RAND_19 = {1{`RANDOM}};
  dataMem_0_3__T_106_en_pipe_0 = _RAND_19[0:0];
  _RAND_20 = {1{`RANDOM}};
  dataMem_0_3__T_106_addr_pipe_0 = _RAND_20[7:0];
  _RAND_22 = {1{`RANDOM}};
  dataMem_0_4__T_8_addr_pipe_0 = _RAND_22[7:0];
  _RAND_23 = {1{`RANDOM}};
  dataMem_0_4__T_106_en_pipe_0 = _RAND_23[0:0];
  _RAND_24 = {1{`RANDOM}};
  dataMem_0_4__T_106_addr_pipe_0 = _RAND_24[7:0];
  _RAND_26 = {1{`RANDOM}};
  dataMem_0_5__T_8_addr_pipe_0 = _RAND_26[7:0];
  _RAND_27 = {1{`RANDOM}};
  dataMem_0_5__T_106_en_pipe_0 = _RAND_27[0:0];
  _RAND_28 = {1{`RANDOM}};
  dataMem_0_5__T_106_addr_pipe_0 = _RAND_28[7:0];
  _RAND_30 = {1{`RANDOM}};
  dataMem_0_6__T_8_addr_pipe_0 = _RAND_30[7:0];
  _RAND_31 = {1{`RANDOM}};
  dataMem_0_6__T_106_en_pipe_0 = _RAND_31[0:0];
  _RAND_32 = {1{`RANDOM}};
  dataMem_0_6__T_106_addr_pipe_0 = _RAND_32[7:0];
  _RAND_34 = {1{`RANDOM}};
  dataMem_0_7__T_8_addr_pipe_0 = _RAND_34[7:0];
  _RAND_35 = {1{`RANDOM}};
  dataMem_0_7__T_106_en_pipe_0 = _RAND_35[0:0];
  _RAND_36 = {1{`RANDOM}};
  dataMem_0_7__T_106_addr_pipe_0 = _RAND_36[7:0];
  _RAND_38 = {1{`RANDOM}};
  dataMem_1_0__T_18_addr_pipe_0 = _RAND_38[7:0];
  _RAND_39 = {1{`RANDOM}};
  dataMem_1_0__T_117_en_pipe_0 = _RAND_39[0:0];
  _RAND_40 = {1{`RANDOM}};
  dataMem_1_0__T_117_addr_pipe_0 = _RAND_40[7:0];
  _RAND_42 = {1{`RANDOM}};
  dataMem_1_1__T_18_addr_pipe_0 = _RAND_42[7:0];
  _RAND_43 = {1{`RANDOM}};
  dataMem_1_1__T_117_en_pipe_0 = _RAND_43[0:0];
  _RAND_44 = {1{`RANDOM}};
  dataMem_1_1__T_117_addr_pipe_0 = _RAND_44[7:0];
  _RAND_46 = {1{`RANDOM}};
  dataMem_1_2__T_18_addr_pipe_0 = _RAND_46[7:0];
  _RAND_47 = {1{`RANDOM}};
  dataMem_1_2__T_117_en_pipe_0 = _RAND_47[0:0];
  _RAND_48 = {1{`RANDOM}};
  dataMem_1_2__T_117_addr_pipe_0 = _RAND_48[7:0];
  _RAND_50 = {1{`RANDOM}};
  dataMem_1_3__T_18_addr_pipe_0 = _RAND_50[7:0];
  _RAND_51 = {1{`RANDOM}};
  dataMem_1_3__T_117_en_pipe_0 = _RAND_51[0:0];
  _RAND_52 = {1{`RANDOM}};
  dataMem_1_3__T_117_addr_pipe_0 = _RAND_52[7:0];
  _RAND_54 = {1{`RANDOM}};
  dataMem_1_4__T_18_addr_pipe_0 = _RAND_54[7:0];
  _RAND_55 = {1{`RANDOM}};
  dataMem_1_4__T_117_en_pipe_0 = _RAND_55[0:0];
  _RAND_56 = {1{`RANDOM}};
  dataMem_1_4__T_117_addr_pipe_0 = _RAND_56[7:0];
  _RAND_58 = {1{`RANDOM}};
  dataMem_1_5__T_18_addr_pipe_0 = _RAND_58[7:0];
  _RAND_59 = {1{`RANDOM}};
  dataMem_1_5__T_117_en_pipe_0 = _RAND_59[0:0];
  _RAND_60 = {1{`RANDOM}};
  dataMem_1_5__T_117_addr_pipe_0 = _RAND_60[7:0];
  _RAND_62 = {1{`RANDOM}};
  dataMem_1_6__T_18_addr_pipe_0 = _RAND_62[7:0];
  _RAND_63 = {1{`RANDOM}};
  dataMem_1_6__T_117_en_pipe_0 = _RAND_63[0:0];
  _RAND_64 = {1{`RANDOM}};
  dataMem_1_6__T_117_addr_pipe_0 = _RAND_64[7:0];
  _RAND_66 = {1{`RANDOM}};
  dataMem_1_7__T_18_addr_pipe_0 = _RAND_66[7:0];
  _RAND_67 = {1{`RANDOM}};
  dataMem_1_7__T_117_en_pipe_0 = _RAND_67[0:0];
  _RAND_68 = {1{`RANDOM}};
  dataMem_1_7__T_117_addr_pipe_0 = _RAND_68[7:0];
  _RAND_70 = {1{`RANDOM}};
  dataMem_2_0__T_28_addr_pipe_0 = _RAND_70[7:0];
  _RAND_71 = {1{`RANDOM}};
  dataMem_2_0__T_128_en_pipe_0 = _RAND_71[0:0];
  _RAND_72 = {1{`RANDOM}};
  dataMem_2_0__T_128_addr_pipe_0 = _RAND_72[7:0];
  _RAND_74 = {1{`RANDOM}};
  dataMem_2_1__T_28_addr_pipe_0 = _RAND_74[7:0];
  _RAND_75 = {1{`RANDOM}};
  dataMem_2_1__T_128_en_pipe_0 = _RAND_75[0:0];
  _RAND_76 = {1{`RANDOM}};
  dataMem_2_1__T_128_addr_pipe_0 = _RAND_76[7:0];
  _RAND_78 = {1{`RANDOM}};
  dataMem_2_2__T_28_addr_pipe_0 = _RAND_78[7:0];
  _RAND_79 = {1{`RANDOM}};
  dataMem_2_2__T_128_en_pipe_0 = _RAND_79[0:0];
  _RAND_80 = {1{`RANDOM}};
  dataMem_2_2__T_128_addr_pipe_0 = _RAND_80[7:0];
  _RAND_82 = {1{`RANDOM}};
  dataMem_2_3__T_28_addr_pipe_0 = _RAND_82[7:0];
  _RAND_83 = {1{`RANDOM}};
  dataMem_2_3__T_128_en_pipe_0 = _RAND_83[0:0];
  _RAND_84 = {1{`RANDOM}};
  dataMem_2_3__T_128_addr_pipe_0 = _RAND_84[7:0];
  _RAND_86 = {1{`RANDOM}};
  dataMem_2_4__T_28_addr_pipe_0 = _RAND_86[7:0];
  _RAND_87 = {1{`RANDOM}};
  dataMem_2_4__T_128_en_pipe_0 = _RAND_87[0:0];
  _RAND_88 = {1{`RANDOM}};
  dataMem_2_4__T_128_addr_pipe_0 = _RAND_88[7:0];
  _RAND_90 = {1{`RANDOM}};
  dataMem_2_5__T_28_addr_pipe_0 = _RAND_90[7:0];
  _RAND_91 = {1{`RANDOM}};
  dataMem_2_5__T_128_en_pipe_0 = _RAND_91[0:0];
  _RAND_92 = {1{`RANDOM}};
  dataMem_2_5__T_128_addr_pipe_0 = _RAND_92[7:0];
  _RAND_94 = {1{`RANDOM}};
  dataMem_2_6__T_28_addr_pipe_0 = _RAND_94[7:0];
  _RAND_95 = {1{`RANDOM}};
  dataMem_2_6__T_128_en_pipe_0 = _RAND_95[0:0];
  _RAND_96 = {1{`RANDOM}};
  dataMem_2_6__T_128_addr_pipe_0 = _RAND_96[7:0];
  _RAND_98 = {1{`RANDOM}};
  dataMem_2_7__T_28_addr_pipe_0 = _RAND_98[7:0];
  _RAND_99 = {1{`RANDOM}};
  dataMem_2_7__T_128_en_pipe_0 = _RAND_99[0:0];
  _RAND_100 = {1{`RANDOM}};
  dataMem_2_7__T_128_addr_pipe_0 = _RAND_100[7:0];
  _RAND_102 = {1{`RANDOM}};
  dataMem_3_0__T_38_addr_pipe_0 = _RAND_102[7:0];
  _RAND_103 = {1{`RANDOM}};
  dataMem_3_0__T_139_en_pipe_0 = _RAND_103[0:0];
  _RAND_104 = {1{`RANDOM}};
  dataMem_3_0__T_139_addr_pipe_0 = _RAND_104[7:0];
  _RAND_106 = {1{`RANDOM}};
  dataMem_3_1__T_38_addr_pipe_0 = _RAND_106[7:0];
  _RAND_107 = {1{`RANDOM}};
  dataMem_3_1__T_139_en_pipe_0 = _RAND_107[0:0];
  _RAND_108 = {1{`RANDOM}};
  dataMem_3_1__T_139_addr_pipe_0 = _RAND_108[7:0];
  _RAND_110 = {1{`RANDOM}};
  dataMem_3_2__T_38_addr_pipe_0 = _RAND_110[7:0];
  _RAND_111 = {1{`RANDOM}};
  dataMem_3_2__T_139_en_pipe_0 = _RAND_111[0:0];
  _RAND_112 = {1{`RANDOM}};
  dataMem_3_2__T_139_addr_pipe_0 = _RAND_112[7:0];
  _RAND_114 = {1{`RANDOM}};
  dataMem_3_3__T_38_addr_pipe_0 = _RAND_114[7:0];
  _RAND_115 = {1{`RANDOM}};
  dataMem_3_3__T_139_en_pipe_0 = _RAND_115[0:0];
  _RAND_116 = {1{`RANDOM}};
  dataMem_3_3__T_139_addr_pipe_0 = _RAND_116[7:0];
  _RAND_118 = {1{`RANDOM}};
  dataMem_3_4__T_38_addr_pipe_0 = _RAND_118[7:0];
  _RAND_119 = {1{`RANDOM}};
  dataMem_3_4__T_139_en_pipe_0 = _RAND_119[0:0];
  _RAND_120 = {1{`RANDOM}};
  dataMem_3_4__T_139_addr_pipe_0 = _RAND_120[7:0];
  _RAND_122 = {1{`RANDOM}};
  dataMem_3_5__T_38_addr_pipe_0 = _RAND_122[7:0];
  _RAND_123 = {1{`RANDOM}};
  dataMem_3_5__T_139_en_pipe_0 = _RAND_123[0:0];
  _RAND_124 = {1{`RANDOM}};
  dataMem_3_5__T_139_addr_pipe_0 = _RAND_124[7:0];
  _RAND_126 = {1{`RANDOM}};
  dataMem_3_6__T_38_addr_pipe_0 = _RAND_126[7:0];
  _RAND_127 = {1{`RANDOM}};
  dataMem_3_6__T_139_en_pipe_0 = _RAND_127[0:0];
  _RAND_128 = {1{`RANDOM}};
  dataMem_3_6__T_139_addr_pipe_0 = _RAND_128[7:0];
  _RAND_130 = {1{`RANDOM}};
  dataMem_3_7__T_38_addr_pipe_0 = _RAND_130[7:0];
  _RAND_131 = {1{`RANDOM}};
  dataMem_3_7__T_139_en_pipe_0 = _RAND_131[0:0];
  _RAND_132 = {1{`RANDOM}};
  dataMem_3_7__T_139_addr_pipe_0 = _RAND_132[7:0];
  _RAND_134 = {1{`RANDOM}};
  dataMem_4_0__T_48_addr_pipe_0 = _RAND_134[7:0];
  _RAND_135 = {1{`RANDOM}};
  dataMem_4_0__T_150_en_pipe_0 = _RAND_135[0:0];
  _RAND_136 = {1{`RANDOM}};
  dataMem_4_0__T_150_addr_pipe_0 = _RAND_136[7:0];
  _RAND_138 = {1{`RANDOM}};
  dataMem_4_1__T_48_addr_pipe_0 = _RAND_138[7:0];
  _RAND_139 = {1{`RANDOM}};
  dataMem_4_1__T_150_en_pipe_0 = _RAND_139[0:0];
  _RAND_140 = {1{`RANDOM}};
  dataMem_4_1__T_150_addr_pipe_0 = _RAND_140[7:0];
  _RAND_142 = {1{`RANDOM}};
  dataMem_4_2__T_48_addr_pipe_0 = _RAND_142[7:0];
  _RAND_143 = {1{`RANDOM}};
  dataMem_4_2__T_150_en_pipe_0 = _RAND_143[0:0];
  _RAND_144 = {1{`RANDOM}};
  dataMem_4_2__T_150_addr_pipe_0 = _RAND_144[7:0];
  _RAND_146 = {1{`RANDOM}};
  dataMem_4_3__T_48_addr_pipe_0 = _RAND_146[7:0];
  _RAND_147 = {1{`RANDOM}};
  dataMem_4_3__T_150_en_pipe_0 = _RAND_147[0:0];
  _RAND_148 = {1{`RANDOM}};
  dataMem_4_3__T_150_addr_pipe_0 = _RAND_148[7:0];
  _RAND_150 = {1{`RANDOM}};
  dataMem_4_4__T_48_addr_pipe_0 = _RAND_150[7:0];
  _RAND_151 = {1{`RANDOM}};
  dataMem_4_4__T_150_en_pipe_0 = _RAND_151[0:0];
  _RAND_152 = {1{`RANDOM}};
  dataMem_4_4__T_150_addr_pipe_0 = _RAND_152[7:0];
  _RAND_154 = {1{`RANDOM}};
  dataMem_4_5__T_48_addr_pipe_0 = _RAND_154[7:0];
  _RAND_155 = {1{`RANDOM}};
  dataMem_4_5__T_150_en_pipe_0 = _RAND_155[0:0];
  _RAND_156 = {1{`RANDOM}};
  dataMem_4_5__T_150_addr_pipe_0 = _RAND_156[7:0];
  _RAND_158 = {1{`RANDOM}};
  dataMem_4_6__T_48_addr_pipe_0 = _RAND_158[7:0];
  _RAND_159 = {1{`RANDOM}};
  dataMem_4_6__T_150_en_pipe_0 = _RAND_159[0:0];
  _RAND_160 = {1{`RANDOM}};
  dataMem_4_6__T_150_addr_pipe_0 = _RAND_160[7:0];
  _RAND_162 = {1{`RANDOM}};
  dataMem_4_7__T_48_addr_pipe_0 = _RAND_162[7:0];
  _RAND_163 = {1{`RANDOM}};
  dataMem_4_7__T_150_en_pipe_0 = _RAND_163[0:0];
  _RAND_164 = {1{`RANDOM}};
  dataMem_4_7__T_150_addr_pipe_0 = _RAND_164[7:0];
  _RAND_166 = {1{`RANDOM}};
  dataMem_5_0__T_58_addr_pipe_0 = _RAND_166[7:0];
  _RAND_167 = {1{`RANDOM}};
  dataMem_5_0__T_161_en_pipe_0 = _RAND_167[0:0];
  _RAND_168 = {1{`RANDOM}};
  dataMem_5_0__T_161_addr_pipe_0 = _RAND_168[7:0];
  _RAND_170 = {1{`RANDOM}};
  dataMem_5_1__T_58_addr_pipe_0 = _RAND_170[7:0];
  _RAND_171 = {1{`RANDOM}};
  dataMem_5_1__T_161_en_pipe_0 = _RAND_171[0:0];
  _RAND_172 = {1{`RANDOM}};
  dataMem_5_1__T_161_addr_pipe_0 = _RAND_172[7:0];
  _RAND_174 = {1{`RANDOM}};
  dataMem_5_2__T_58_addr_pipe_0 = _RAND_174[7:0];
  _RAND_175 = {1{`RANDOM}};
  dataMem_5_2__T_161_en_pipe_0 = _RAND_175[0:0];
  _RAND_176 = {1{`RANDOM}};
  dataMem_5_2__T_161_addr_pipe_0 = _RAND_176[7:0];
  _RAND_178 = {1{`RANDOM}};
  dataMem_5_3__T_58_addr_pipe_0 = _RAND_178[7:0];
  _RAND_179 = {1{`RANDOM}};
  dataMem_5_3__T_161_en_pipe_0 = _RAND_179[0:0];
  _RAND_180 = {1{`RANDOM}};
  dataMem_5_3__T_161_addr_pipe_0 = _RAND_180[7:0];
  _RAND_182 = {1{`RANDOM}};
  dataMem_5_4__T_58_addr_pipe_0 = _RAND_182[7:0];
  _RAND_183 = {1{`RANDOM}};
  dataMem_5_4__T_161_en_pipe_0 = _RAND_183[0:0];
  _RAND_184 = {1{`RANDOM}};
  dataMem_5_4__T_161_addr_pipe_0 = _RAND_184[7:0];
  _RAND_186 = {1{`RANDOM}};
  dataMem_5_5__T_58_addr_pipe_0 = _RAND_186[7:0];
  _RAND_187 = {1{`RANDOM}};
  dataMem_5_5__T_161_en_pipe_0 = _RAND_187[0:0];
  _RAND_188 = {1{`RANDOM}};
  dataMem_5_5__T_161_addr_pipe_0 = _RAND_188[7:0];
  _RAND_190 = {1{`RANDOM}};
  dataMem_5_6__T_58_addr_pipe_0 = _RAND_190[7:0];
  _RAND_191 = {1{`RANDOM}};
  dataMem_5_6__T_161_en_pipe_0 = _RAND_191[0:0];
  _RAND_192 = {1{`RANDOM}};
  dataMem_5_6__T_161_addr_pipe_0 = _RAND_192[7:0];
  _RAND_194 = {1{`RANDOM}};
  dataMem_5_7__T_58_addr_pipe_0 = _RAND_194[7:0];
  _RAND_195 = {1{`RANDOM}};
  dataMem_5_7__T_161_en_pipe_0 = _RAND_195[0:0];
  _RAND_196 = {1{`RANDOM}};
  dataMem_5_7__T_161_addr_pipe_0 = _RAND_196[7:0];
  _RAND_198 = {1{`RANDOM}};
  dataMem_6_0__T_68_addr_pipe_0 = _RAND_198[7:0];
  _RAND_199 = {1{`RANDOM}};
  dataMem_6_0__T_172_en_pipe_0 = _RAND_199[0:0];
  _RAND_200 = {1{`RANDOM}};
  dataMem_6_0__T_172_addr_pipe_0 = _RAND_200[7:0];
  _RAND_202 = {1{`RANDOM}};
  dataMem_6_1__T_68_addr_pipe_0 = _RAND_202[7:0];
  _RAND_203 = {1{`RANDOM}};
  dataMem_6_1__T_172_en_pipe_0 = _RAND_203[0:0];
  _RAND_204 = {1{`RANDOM}};
  dataMem_6_1__T_172_addr_pipe_0 = _RAND_204[7:0];
  _RAND_206 = {1{`RANDOM}};
  dataMem_6_2__T_68_addr_pipe_0 = _RAND_206[7:0];
  _RAND_207 = {1{`RANDOM}};
  dataMem_6_2__T_172_en_pipe_0 = _RAND_207[0:0];
  _RAND_208 = {1{`RANDOM}};
  dataMem_6_2__T_172_addr_pipe_0 = _RAND_208[7:0];
  _RAND_210 = {1{`RANDOM}};
  dataMem_6_3__T_68_addr_pipe_0 = _RAND_210[7:0];
  _RAND_211 = {1{`RANDOM}};
  dataMem_6_3__T_172_en_pipe_0 = _RAND_211[0:0];
  _RAND_212 = {1{`RANDOM}};
  dataMem_6_3__T_172_addr_pipe_0 = _RAND_212[7:0];
  _RAND_214 = {1{`RANDOM}};
  dataMem_6_4__T_68_addr_pipe_0 = _RAND_214[7:0];
  _RAND_215 = {1{`RANDOM}};
  dataMem_6_4__T_172_en_pipe_0 = _RAND_215[0:0];
  _RAND_216 = {1{`RANDOM}};
  dataMem_6_4__T_172_addr_pipe_0 = _RAND_216[7:0];
  _RAND_218 = {1{`RANDOM}};
  dataMem_6_5__T_68_addr_pipe_0 = _RAND_218[7:0];
  _RAND_219 = {1{`RANDOM}};
  dataMem_6_5__T_172_en_pipe_0 = _RAND_219[0:0];
  _RAND_220 = {1{`RANDOM}};
  dataMem_6_5__T_172_addr_pipe_0 = _RAND_220[7:0];
  _RAND_222 = {1{`RANDOM}};
  dataMem_6_6__T_68_addr_pipe_0 = _RAND_222[7:0];
  _RAND_223 = {1{`RANDOM}};
  dataMem_6_6__T_172_en_pipe_0 = _RAND_223[0:0];
  _RAND_224 = {1{`RANDOM}};
  dataMem_6_6__T_172_addr_pipe_0 = _RAND_224[7:0];
  _RAND_226 = {1{`RANDOM}};
  dataMem_6_7__T_68_addr_pipe_0 = _RAND_226[7:0];
  _RAND_227 = {1{`RANDOM}};
  dataMem_6_7__T_172_en_pipe_0 = _RAND_227[0:0];
  _RAND_228 = {1{`RANDOM}};
  dataMem_6_7__T_172_addr_pipe_0 = _RAND_228[7:0];
  _RAND_230 = {1{`RANDOM}};
  dataMem_7_0__T_78_addr_pipe_0 = _RAND_230[7:0];
  _RAND_231 = {1{`RANDOM}};
  dataMem_7_0__T_183_en_pipe_0 = _RAND_231[0:0];
  _RAND_232 = {1{`RANDOM}};
  dataMem_7_0__T_183_addr_pipe_0 = _RAND_232[7:0];
  _RAND_234 = {1{`RANDOM}};
  dataMem_7_1__T_78_addr_pipe_0 = _RAND_234[7:0];
  _RAND_235 = {1{`RANDOM}};
  dataMem_7_1__T_183_en_pipe_0 = _RAND_235[0:0];
  _RAND_236 = {1{`RANDOM}};
  dataMem_7_1__T_183_addr_pipe_0 = _RAND_236[7:0];
  _RAND_238 = {1{`RANDOM}};
  dataMem_7_2__T_78_addr_pipe_0 = _RAND_238[7:0];
  _RAND_239 = {1{`RANDOM}};
  dataMem_7_2__T_183_en_pipe_0 = _RAND_239[0:0];
  _RAND_240 = {1{`RANDOM}};
  dataMem_7_2__T_183_addr_pipe_0 = _RAND_240[7:0];
  _RAND_242 = {1{`RANDOM}};
  dataMem_7_3__T_78_addr_pipe_0 = _RAND_242[7:0];
  _RAND_243 = {1{`RANDOM}};
  dataMem_7_3__T_183_en_pipe_0 = _RAND_243[0:0];
  _RAND_244 = {1{`RANDOM}};
  dataMem_7_3__T_183_addr_pipe_0 = _RAND_244[7:0];
  _RAND_246 = {1{`RANDOM}};
  dataMem_7_4__T_78_addr_pipe_0 = _RAND_246[7:0];
  _RAND_247 = {1{`RANDOM}};
  dataMem_7_4__T_183_en_pipe_0 = _RAND_247[0:0];
  _RAND_248 = {1{`RANDOM}};
  dataMem_7_4__T_183_addr_pipe_0 = _RAND_248[7:0];
  _RAND_250 = {1{`RANDOM}};
  dataMem_7_5__T_78_addr_pipe_0 = _RAND_250[7:0];
  _RAND_251 = {1{`RANDOM}};
  dataMem_7_5__T_183_en_pipe_0 = _RAND_251[0:0];
  _RAND_252 = {1{`RANDOM}};
  dataMem_7_5__T_183_addr_pipe_0 = _RAND_252[7:0];
  _RAND_254 = {1{`RANDOM}};
  dataMem_7_6__T_78_addr_pipe_0 = _RAND_254[7:0];
  _RAND_255 = {1{`RANDOM}};
  dataMem_7_6__T_183_en_pipe_0 = _RAND_255[0:0];
  _RAND_256 = {1{`RANDOM}};
  dataMem_7_6__T_183_addr_pipe_0 = _RAND_256[7:0];
  _RAND_258 = {1{`RANDOM}};
  dataMem_7_7__T_78_addr_pipe_0 = _RAND_258[7:0];
  _RAND_259 = {1{`RANDOM}};
  dataMem_7_7__T_183_en_pipe_0 = _RAND_259[0:0];
  _RAND_260 = {1{`RANDOM}};
  dataMem_7_7__T_183_addr_pipe_0 = _RAND_260[7:0];
  _RAND_261 = {1{`RANDOM}};
  state = _RAND_261[2:0];
  _RAND_262 = {1{`RANDOM}};
  flush_state = _RAND_262[2:0];
  _RAND_263 = {1{`RANDOM}};
  flush_mode = _RAND_263[0:0];
  _RAND_264 = {8{`RANDOM}};
  v = _RAND_264[255:0];
  _RAND_265 = {8{`RANDOM}};
  d = _RAND_265[255:0];
  _RAND_266 = {2{`RANDOM}};
  addr_reg = _RAND_266[63:0];
  _RAND_267 = {1{`RANDOM}};
  cpu_tag_reg = _RAND_267[7:0];
  _RAND_268 = {2{`RANDOM}};
  cpu_data = _RAND_268[63:0];
  _RAND_269 = {1{`RANDOM}};
  cpu_mask = _RAND_269[7:0];
  _RAND_270 = {1{`RANDOM}};
  set_count = _RAND_270[7:0];
  _RAND_271 = {2{`RANDOM}};
  block_rmeta_tag = _RAND_271[49:0];
  _RAND_272 = {1{`RANDOM}};
  is_alloc_reg = _RAND_272[0:0];
  _RAND_273 = {1{`RANDOM}};
  ren_reg = _RAND_273[0:0];
  _RAND_274 = {16{`RANDOM}};
  rdata_buf = _RAND_274[511:0];
  _RAND_275 = {16{`RANDOM}};
  refill_buf_0 = _RAND_275[511:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if(metaMem_tag__T_242_en & metaMem_tag__T_242_mask) begin
      metaMem_tag[metaMem_tag__T_242_addr] <= metaMem_tag__T_242_data; // @[AXICache.scala 720:28]
    end
    metaMem_tag_rmeta_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      metaMem_tag_rmeta_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    metaMem_tag__T_411_en_pipe_0 <= is_block_dirty & _T_2;
    if (is_block_dirty & _T_2) begin
      metaMem_tag__T_411_addr_pipe_0 <= set_count;
    end
    if(dataMem_0_0__T_261_en & dataMem_0_0__T_261_mask) begin
      dataMem_0_0[dataMem_0_0__T_261_addr] <= dataMem_0_0__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_0__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_0__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_0__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_0_1__T_261_en & dataMem_0_1__T_261_mask) begin
      dataMem_0_1[dataMem_0_1__T_261_addr] <= dataMem_0_1__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_1__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_1__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_1__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_0_2__T_261_en & dataMem_0_2__T_261_mask) begin
      dataMem_0_2[dataMem_0_2__T_261_addr] <= dataMem_0_2__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_2__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_2__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_2__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_0_3__T_261_en & dataMem_0_3__T_261_mask) begin
      dataMem_0_3[dataMem_0_3__T_261_addr] <= dataMem_0_3__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_3__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_3__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_3__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_0_4__T_261_en & dataMem_0_4__T_261_mask) begin
      dataMem_0_4[dataMem_0_4__T_261_addr] <= dataMem_0_4__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_4__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_4__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_4__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_0_5__T_261_en & dataMem_0_5__T_261_mask) begin
      dataMem_0_5[dataMem_0_5__T_261_addr] <= dataMem_0_5__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_5__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_5__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_5__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_0_6__T_261_en & dataMem_0_6__T_261_mask) begin
      dataMem_0_6[dataMem_0_6__T_261_addr] <= dataMem_0_6__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_6__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_6__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_6__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_0_7__T_261_en & dataMem_0_7__T_261_mask) begin
      dataMem_0_7[dataMem_0_7__T_261_addr] <= dataMem_0_7__T_261_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_7__T_8_addr_pipe_0 <= set_count - 8'h1;
    dataMem_0_7__T_106_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_0_7__T_106_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_0__T_280_en & dataMem_1_0__T_280_mask) begin
      dataMem_1_0[dataMem_1_0__T_280_addr] <= dataMem_1_0__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_0__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_0__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_0__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_1__T_280_en & dataMem_1_1__T_280_mask) begin
      dataMem_1_1[dataMem_1_1__T_280_addr] <= dataMem_1_1__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_1__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_1__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_1__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_2__T_280_en & dataMem_1_2__T_280_mask) begin
      dataMem_1_2[dataMem_1_2__T_280_addr] <= dataMem_1_2__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_2__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_2__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_2__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_3__T_280_en & dataMem_1_3__T_280_mask) begin
      dataMem_1_3[dataMem_1_3__T_280_addr] <= dataMem_1_3__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_3__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_3__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_3__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_4__T_280_en & dataMem_1_4__T_280_mask) begin
      dataMem_1_4[dataMem_1_4__T_280_addr] <= dataMem_1_4__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_4__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_4__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_4__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_5__T_280_en & dataMem_1_5__T_280_mask) begin
      dataMem_1_5[dataMem_1_5__T_280_addr] <= dataMem_1_5__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_5__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_5__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_5__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_6__T_280_en & dataMem_1_6__T_280_mask) begin
      dataMem_1_6[dataMem_1_6__T_280_addr] <= dataMem_1_6__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_6__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_6__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_6__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_1_7__T_280_en & dataMem_1_7__T_280_mask) begin
      dataMem_1_7[dataMem_1_7__T_280_addr] <= dataMem_1_7__T_280_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_7__T_18_addr_pipe_0 <= set_count - 8'h1;
    dataMem_1_7__T_117_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_1_7__T_117_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_0__T_299_en & dataMem_2_0__T_299_mask) begin
      dataMem_2_0[dataMem_2_0__T_299_addr] <= dataMem_2_0__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_0__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_0__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_0__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_1__T_299_en & dataMem_2_1__T_299_mask) begin
      dataMem_2_1[dataMem_2_1__T_299_addr] <= dataMem_2_1__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_1__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_1__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_1__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_2__T_299_en & dataMem_2_2__T_299_mask) begin
      dataMem_2_2[dataMem_2_2__T_299_addr] <= dataMem_2_2__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_2__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_2__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_2__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_3__T_299_en & dataMem_2_3__T_299_mask) begin
      dataMem_2_3[dataMem_2_3__T_299_addr] <= dataMem_2_3__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_3__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_3__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_3__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_4__T_299_en & dataMem_2_4__T_299_mask) begin
      dataMem_2_4[dataMem_2_4__T_299_addr] <= dataMem_2_4__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_4__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_4__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_4__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_5__T_299_en & dataMem_2_5__T_299_mask) begin
      dataMem_2_5[dataMem_2_5__T_299_addr] <= dataMem_2_5__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_5__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_5__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_5__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_6__T_299_en & dataMem_2_6__T_299_mask) begin
      dataMem_2_6[dataMem_2_6__T_299_addr] <= dataMem_2_6__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_6__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_6__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_6__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_2_7__T_299_en & dataMem_2_7__T_299_mask) begin
      dataMem_2_7[dataMem_2_7__T_299_addr] <= dataMem_2_7__T_299_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_7__T_28_addr_pipe_0 <= set_count - 8'h1;
    dataMem_2_7__T_128_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_2_7__T_128_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_0__T_318_en & dataMem_3_0__T_318_mask) begin
      dataMem_3_0[dataMem_3_0__T_318_addr] <= dataMem_3_0__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_0__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_0__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_0__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_1__T_318_en & dataMem_3_1__T_318_mask) begin
      dataMem_3_1[dataMem_3_1__T_318_addr] <= dataMem_3_1__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_1__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_1__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_1__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_2__T_318_en & dataMem_3_2__T_318_mask) begin
      dataMem_3_2[dataMem_3_2__T_318_addr] <= dataMem_3_2__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_2__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_2__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_2__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_3__T_318_en & dataMem_3_3__T_318_mask) begin
      dataMem_3_3[dataMem_3_3__T_318_addr] <= dataMem_3_3__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_3__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_3__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_3__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_4__T_318_en & dataMem_3_4__T_318_mask) begin
      dataMem_3_4[dataMem_3_4__T_318_addr] <= dataMem_3_4__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_4__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_4__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_4__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_5__T_318_en & dataMem_3_5__T_318_mask) begin
      dataMem_3_5[dataMem_3_5__T_318_addr] <= dataMem_3_5__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_5__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_5__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_5__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_6__T_318_en & dataMem_3_6__T_318_mask) begin
      dataMem_3_6[dataMem_3_6__T_318_addr] <= dataMem_3_6__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_6__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_6__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_6__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_3_7__T_318_en & dataMem_3_7__T_318_mask) begin
      dataMem_3_7[dataMem_3_7__T_318_addr] <= dataMem_3_7__T_318_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_7__T_38_addr_pipe_0 <= set_count - 8'h1;
    dataMem_3_7__T_139_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_3_7__T_139_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_0__T_337_en & dataMem_4_0__T_337_mask) begin
      dataMem_4_0[dataMem_4_0__T_337_addr] <= dataMem_4_0__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_0__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_0__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_0__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_1__T_337_en & dataMem_4_1__T_337_mask) begin
      dataMem_4_1[dataMem_4_1__T_337_addr] <= dataMem_4_1__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_1__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_1__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_1__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_2__T_337_en & dataMem_4_2__T_337_mask) begin
      dataMem_4_2[dataMem_4_2__T_337_addr] <= dataMem_4_2__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_2__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_2__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_2__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_3__T_337_en & dataMem_4_3__T_337_mask) begin
      dataMem_4_3[dataMem_4_3__T_337_addr] <= dataMem_4_3__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_3__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_3__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_3__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_4__T_337_en & dataMem_4_4__T_337_mask) begin
      dataMem_4_4[dataMem_4_4__T_337_addr] <= dataMem_4_4__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_4__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_4__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_4__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_5__T_337_en & dataMem_4_5__T_337_mask) begin
      dataMem_4_5[dataMem_4_5__T_337_addr] <= dataMem_4_5__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_5__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_5__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_5__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_6__T_337_en & dataMem_4_6__T_337_mask) begin
      dataMem_4_6[dataMem_4_6__T_337_addr] <= dataMem_4_6__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_6__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_6__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_6__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_4_7__T_337_en & dataMem_4_7__T_337_mask) begin
      dataMem_4_7[dataMem_4_7__T_337_addr] <= dataMem_4_7__T_337_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_7__T_48_addr_pipe_0 <= set_count - 8'h1;
    dataMem_4_7__T_150_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_4_7__T_150_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_0__T_356_en & dataMem_5_0__T_356_mask) begin
      dataMem_5_0[dataMem_5_0__T_356_addr] <= dataMem_5_0__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_0__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_0__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_0__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_1__T_356_en & dataMem_5_1__T_356_mask) begin
      dataMem_5_1[dataMem_5_1__T_356_addr] <= dataMem_5_1__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_1__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_1__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_1__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_2__T_356_en & dataMem_5_2__T_356_mask) begin
      dataMem_5_2[dataMem_5_2__T_356_addr] <= dataMem_5_2__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_2__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_2__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_2__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_3__T_356_en & dataMem_5_3__T_356_mask) begin
      dataMem_5_3[dataMem_5_3__T_356_addr] <= dataMem_5_3__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_3__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_3__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_3__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_4__T_356_en & dataMem_5_4__T_356_mask) begin
      dataMem_5_4[dataMem_5_4__T_356_addr] <= dataMem_5_4__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_4__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_4__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_4__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_5__T_356_en & dataMem_5_5__T_356_mask) begin
      dataMem_5_5[dataMem_5_5__T_356_addr] <= dataMem_5_5__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_5__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_5__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_5__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_6__T_356_en & dataMem_5_6__T_356_mask) begin
      dataMem_5_6[dataMem_5_6__T_356_addr] <= dataMem_5_6__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_6__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_6__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_6__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_5_7__T_356_en & dataMem_5_7__T_356_mask) begin
      dataMem_5_7[dataMem_5_7__T_356_addr] <= dataMem_5_7__T_356_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_7__T_58_addr_pipe_0 <= set_count - 8'h1;
    dataMem_5_7__T_161_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_5_7__T_161_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_0__T_375_en & dataMem_6_0__T_375_mask) begin
      dataMem_6_0[dataMem_6_0__T_375_addr] <= dataMem_6_0__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_0__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_0__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_0__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_1__T_375_en & dataMem_6_1__T_375_mask) begin
      dataMem_6_1[dataMem_6_1__T_375_addr] <= dataMem_6_1__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_1__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_1__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_1__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_2__T_375_en & dataMem_6_2__T_375_mask) begin
      dataMem_6_2[dataMem_6_2__T_375_addr] <= dataMem_6_2__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_2__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_2__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_2__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_3__T_375_en & dataMem_6_3__T_375_mask) begin
      dataMem_6_3[dataMem_6_3__T_375_addr] <= dataMem_6_3__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_3__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_3__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_3__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_4__T_375_en & dataMem_6_4__T_375_mask) begin
      dataMem_6_4[dataMem_6_4__T_375_addr] <= dataMem_6_4__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_4__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_4__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_4__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_5__T_375_en & dataMem_6_5__T_375_mask) begin
      dataMem_6_5[dataMem_6_5__T_375_addr] <= dataMem_6_5__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_5__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_5__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_5__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_6__T_375_en & dataMem_6_6__T_375_mask) begin
      dataMem_6_6[dataMem_6_6__T_375_addr] <= dataMem_6_6__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_6__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_6__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_6__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_6_7__T_375_en & dataMem_6_7__T_375_mask) begin
      dataMem_6_7[dataMem_6_7__T_375_addr] <= dataMem_6_7__T_375_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_7__T_68_addr_pipe_0 <= set_count - 8'h1;
    dataMem_6_7__T_172_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_6_7__T_172_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_0__T_394_en & dataMem_7_0__T_394_mask) begin
      dataMem_7_0[dataMem_7_0__T_394_addr] <= dataMem_7_0__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_0__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_0__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_0__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_1__T_394_en & dataMem_7_1__T_394_mask) begin
      dataMem_7_1[dataMem_7_1__T_394_addr] <= dataMem_7_1__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_1__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_1__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_1__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_2__T_394_en & dataMem_7_2__T_394_mask) begin
      dataMem_7_2[dataMem_7_2__T_394_addr] <= dataMem_7_2__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_2__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_2__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_2__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_3__T_394_en & dataMem_7_3__T_394_mask) begin
      dataMem_7_3[dataMem_7_3__T_394_addr] <= dataMem_7_3__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_3__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_3__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_3__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_4__T_394_en & dataMem_7_4__T_394_mask) begin
      dataMem_7_4[dataMem_7_4__T_394_addr] <= dataMem_7_4__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_4__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_4__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_4__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_5__T_394_en & dataMem_7_5__T_394_mask) begin
      dataMem_7_5[dataMem_7_5__T_394_addr] <= dataMem_7_5__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_5__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_5__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_5__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_6__T_394_en & dataMem_7_6__T_394_mask) begin
      dataMem_7_6[dataMem_7_6__T_394_addr] <= dataMem_7_6__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_6__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_6__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_6__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if(dataMem_7_7__T_394_en & dataMem_7_7__T_394_mask) begin
      dataMem_7_7[dataMem_7_7__T_394_addr] <= dataMem_7_7__T_394_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_7__T_78_addr_pipe_0 <= set_count - 8'h1;
    dataMem_7_7__T_183_en_pipe_0 <= _T_99 & io_cpu_req_valid;
    if (_T_99 & io_cpu_req_valid) begin
      dataMem_7_7__T_183_addr_pipe_0 <= io_cpu_req_bits_addr[13:6];
    end
    if (reset) begin
      state <= 3'h0;
    end else if (_T_424) begin
      if (io_cpu_req_valid) begin
        if (_T_425) begin
          state <= 3'h2;
        end else begin
          state <= 3'h1;
        end
      end
    end else if (_T_427) begin
      if (hit) begin
        if (io_cpu_req_valid) begin
          if (_T_425) begin
            state <= 3'h2;
          end else begin
            state <= 3'h1;
          end
        end else begin
          state <= 3'h0;
        end
      end else if (_T_431) begin
        state <= 3'h3;
      end else if (_T_432) begin
        state <= 3'h6;
      end
    end else if (_T_433) begin
      if (_T_93) begin
        state <= 3'h0;
      end else if (_T_431) begin
        state <= 3'h3;
      end else if (_T_432) begin
        state <= 3'h6;
      end
    end else if (_T_439) begin
      if (write_wrap_out) begin
        state <= 3'h4;
      end
    end else if (_T_440) begin
      if (io_mem_wr_ack) begin
        state <= 3'h5;
      end
    end else if (_T_441) begin
      if (_T_432) begin
        state <= 3'h6;
      end
    end else if (_T_443) begin
      if (read_wrap_out) begin
        if (_T_216) begin
          state <= 3'h2;
        end else begin
          state <= 3'h0;
        end
      end
    end
    if (reset) begin
      flush_state <= 3'h0;
    end else if (_T_446) begin
      if (io_cpu_flush) begin
        flush_state <= 3'h1;
      end
    end else if (_T_447) begin
      if (set_wrap) begin
        flush_state <= 3'h0;
      end else if (is_block_dirty) begin
        flush_state <= 3'h2;
      end
    end else if (_T_448) begin
      flush_state <= 3'h3;
    end else if (_T_449) begin
      if (_T_431) begin
        flush_state <= 3'h4;
      end
    end else if (_T_451) begin
      if (write_wrap_out) begin
        flush_state <= 3'h5;
      end
    end else if (_T_452) begin
      if (io_mem_wr_ack) begin
        flush_state <= 3'h1;
      end
    end
    if (reset) begin
      flush_mode <= 1'h0;
    end else if (_T_446) begin
      flush_mode <= _GEN_343;
    end else if (_T_447) begin
      if (set_wrap) begin
        flush_mode <= 1'h0;
      end
    end
    if (reset) begin
      v <= 256'h0;
    end else if (wen) begin
      v <= _T_230;
    end
    if (reset) begin
      d <= 256'h0;
    end else if (wen) begin
      if (_T_221) begin
        d <= _T_237;
      end else begin
        d <= _T_240;
      end
    end
    if (io_cpu_resp_valid) begin
      addr_reg <= io_cpu_req_bits_addr;
    end
    if (_T_220) begin
      cpu_tag_reg <= io_cpu_req_bits_tag;
    end
    if (io_cpu_resp_valid) begin
      cpu_data <= io_cpu_req_bits_data;
    end
    if (io_cpu_resp_valid) begin
      cpu_mask <= io_cpu_req_bits_mask;
    end
    if (reset) begin
      set_count <= 8'h0;
    end else if (_T_2) begin
      set_count <= _T_5;
    end
    block_rmeta_tag <= metaMem_tag__T_411_data;
    is_alloc_reg <= _T_92 & read_wrap_out;
    ren_reg <= _T_99 & io_cpu_req_valid;
    if (ren_reg) begin
      rdata_buf <= rdata;
    end
    if (read_wrap_out) begin
      refill_buf_0 <= io_mem_rd_data_bits;
    end
  end
endmodule
module Arbiter_2(
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [63:0] io_in_0_bits_addr,
  output        io_in_1_ready,
  input         io_in_1_valid,
  input  [63:0] io_in_1_bits_addr,
  output        io_in_2_ready,
  input         io_in_2_valid,
  input  [63:0] io_in_2_bits_addr,
  input  [63:0] io_in_2_bits_data,
  input         io_out_ready,
  output        io_out_valid,
  output [63:0] io_out_bits_addr,
  output [63:0] io_out_bits_data,
  output [7:0]  io_out_bits_mask,
  output [7:0]  io_out_bits_tag,
  output [1:0]  io_chosen
);
  wire [1:0] _GEN_0 = io_in_1_valid ? 2'h1 : 2'h2; // @[Arbiter.scala 126:27]
  wire [7:0] _GEN_3 = io_in_1_valid ? 8'h1 : 8'h2; // @[Arbiter.scala 126:27]
  wire [7:0] _GEN_4 = io_in_1_valid ? 8'h0 : 8'hff; // @[Arbiter.scala 126:27]
  wire [63:0] _GEN_5 = io_in_1_valid ? 64'h0 : io_in_2_bits_data; // @[Arbiter.scala 126:27]
  wire [63:0] _GEN_6 = io_in_1_valid ? io_in_1_bits_addr : io_in_2_bits_addr; // @[Arbiter.scala 126:27]
  wire  _T = io_in_0_valid | io_in_1_valid; // @[Arbiter.scala 31:68]
  wire  grant_1 = ~io_in_0_valid; // @[Arbiter.scala 31:78]
  wire  grant_2 = ~_T; // @[Arbiter.scala 31:78]
  wire  _T_4 = ~grant_2; // @[Arbiter.scala 135:19]
  assign io_in_0_ready = io_out_ready; // @[Arbiter.scala 134:14]
  assign io_in_1_ready = grant_1 & io_out_ready; // @[Arbiter.scala 134:14]
  assign io_in_2_ready = grant_2 & io_out_ready; // @[Arbiter.scala 134:14]
  assign io_out_valid = _T_4 | io_in_2_valid; // @[Arbiter.scala 135:16]
  assign io_out_bits_addr = io_in_0_valid ? io_in_0_bits_addr : _GEN_6; // @[Arbiter.scala 124:15 Arbiter.scala 128:19 Arbiter.scala 128:19]
  assign io_out_bits_data = io_in_0_valid ? 64'h0 : _GEN_5; // @[Arbiter.scala 124:15 Arbiter.scala 128:19 Arbiter.scala 128:19]
  assign io_out_bits_mask = io_in_0_valid ? 8'h0 : _GEN_4; // @[Arbiter.scala 124:15 Arbiter.scala 128:19 Arbiter.scala 128:19]
  assign io_out_bits_tag = io_in_0_valid ? 8'h0 : _GEN_3; // @[Arbiter.scala 124:15 Arbiter.scala 128:19 Arbiter.scala 128:19]
  assign io_chosen = io_in_0_valid ? 2'h0 : _GEN_0; // @[Arbiter.scala 123:13 Arbiter.scala 127:17 Arbiter.scala 127:17]
endmodule
module CacheMemoryEngine(
  input         clock,
  input         reset,
  output        io_rd_mem_0_MemReq_ready,
  input         io_rd_mem_0_MemReq_valid,
  input  [63:0] io_rd_mem_0_MemReq_bits_addr,
  output        io_rd_mem_0_MemResp_valid,
  output [63:0] io_rd_mem_0_MemResp_bits_data,
  output        io_rd_mem_1_MemReq_ready,
  input         io_rd_mem_1_MemReq_valid,
  input  [63:0] io_rd_mem_1_MemReq_bits_addr,
  output        io_rd_mem_1_MemResp_valid,
  output [63:0] io_rd_mem_1_MemResp_bits_data,
  output        io_wr_mem_0_MemReq_ready,
  input         io_wr_mem_0_MemReq_valid,
  input  [63:0] io_wr_mem_0_MemReq_bits_addr,
  input  [63:0] io_wr_mem_0_MemReq_bits_data,
  output        io_wr_mem_0_MemResp_valid,
  input         io_cache_MemReq_ready,
  output        io_cache_MemReq_valid,
  output [63:0] io_cache_MemReq_bits_addr,
  output [63:0] io_cache_MemReq_bits_data,
  output [7:0]  io_cache_MemReq_bits_mask,
  output [7:0]  io_cache_MemReq_bits_tag,
  input         io_cache_MemResp_valid,
  input  [63:0] io_cache_MemResp_bits_data,
  input  [7:0]  io_cache_MemResp_bits_tag
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [63:0] _RAND_2;
  reg [63:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
`endif // RANDOMIZE_REG_INIT
  wire  in_arb_io_in_0_ready; // @[CacheMemoryEngine.scala 79:22]
  wire  in_arb_io_in_0_valid; // @[CacheMemoryEngine.scala 79:22]
  wire [63:0] in_arb_io_in_0_bits_addr; // @[CacheMemoryEngine.scala 79:22]
  wire  in_arb_io_in_1_ready; // @[CacheMemoryEngine.scala 79:22]
  wire  in_arb_io_in_1_valid; // @[CacheMemoryEngine.scala 79:22]
  wire [63:0] in_arb_io_in_1_bits_addr; // @[CacheMemoryEngine.scala 79:22]
  wire  in_arb_io_in_2_ready; // @[CacheMemoryEngine.scala 79:22]
  wire  in_arb_io_in_2_valid; // @[CacheMemoryEngine.scala 79:22]
  wire [63:0] in_arb_io_in_2_bits_addr; // @[CacheMemoryEngine.scala 79:22]
  wire [63:0] in_arb_io_in_2_bits_data; // @[CacheMemoryEngine.scala 79:22]
  wire  in_arb_io_out_ready; // @[CacheMemoryEngine.scala 79:22]
  wire  in_arb_io_out_valid; // @[CacheMemoryEngine.scala 79:22]
  wire [63:0] in_arb_io_out_bits_addr; // @[CacheMemoryEngine.scala 79:22]
  wire [63:0] in_arb_io_out_bits_data; // @[CacheMemoryEngine.scala 79:22]
  wire [7:0] in_arb_io_out_bits_mask; // @[CacheMemoryEngine.scala 79:22]
  wire [7:0] in_arb_io_out_bits_tag; // @[CacheMemoryEngine.scala 79:22]
  wire [1:0] in_arb_io_chosen; // @[CacheMemoryEngine.scala 79:22]
  wire  _T = in_arb_io_out_ready & in_arb_io_out_valid; // @[Decoupled.scala 40:37]
  reg [1:0] in_arb_chosen; // @[Reg.scala 15:16]
  reg [1:0] mstate; // @[CacheMemoryEngine.scala 91:23]
  wire  _T_1 = 2'h0 == mstate; // @[Conditional.scala 37:30]
  wire  _T_2 = 2'h1 == mstate; // @[Conditional.scala 37:30]
  wire  _T_3 = 2'h2 == mstate; // @[Conditional.scala 37:30]
  wire [7:0] _GEN_13 = {{6'd0}, in_arb_chosen}; // @[CacheMemoryEngine.scala 105:52]
  wire  _T_4 = _GEN_13 == io_cache_MemResp_bits_tag; // @[CacheMemoryEngine.scala 105:52]
  wire  _T_5 = io_cache_MemResp_valid & _T_4; // @[CacheMemoryEngine.scala 105:35]
  wire  _T_6 = in_arb_chosen == 2'h0; // @[CacheMemoryEngine.scala 112:50]
  wire  _T_8 = _T_6 & _T_4; // @[CacheMemoryEngine.scala 112:59]
  wire  _T_9 = _T_8 & io_cache_MemResp_valid; // @[CacheMemoryEngine.scala 113:53]
  wire  _T_10 = mstate == 2'h2; // @[CacheMemoryEngine.scala 115:15]
  wire  _T_12 = in_arb_chosen == 2'h1; // @[CacheMemoryEngine.scala 112:50]
  wire  _T_14 = _T_12 & _T_4; // @[CacheMemoryEngine.scala 112:59]
  wire  _T_15 = _T_14 & io_cache_MemResp_valid; // @[CacheMemoryEngine.scala 113:53]
  wire  _T_18 = in_arb_chosen == 2'h2; // @[CacheMemoryEngine.scala 120:60]
  wire  _T_20 = _T_18 & _T_4; // @[CacheMemoryEngine.scala 120:69]
  wire  _T_21 = _T_20 & io_cache_MemResp_valid; // @[CacheMemoryEngine.scala 121:53]
  reg [63:0] in_data_reg_addr; // @[Reg.scala 27:20]
  reg [63:0] in_data_reg_data; // @[Reg.scala 27:20]
  reg [7:0] in_data_reg_mask; // @[Reg.scala 27:20]
  reg [7:0] in_data_reg_tag; // @[Reg.scala 27:20]
  Arbiter_2 in_arb ( // @[CacheMemoryEngine.scala 79:22]
    .io_in_0_ready(in_arb_io_in_0_ready),
    .io_in_0_valid(in_arb_io_in_0_valid),
    .io_in_0_bits_addr(in_arb_io_in_0_bits_addr),
    .io_in_1_ready(in_arb_io_in_1_ready),
    .io_in_1_valid(in_arb_io_in_1_valid),
    .io_in_1_bits_addr(in_arb_io_in_1_bits_addr),
    .io_in_2_ready(in_arb_io_in_2_ready),
    .io_in_2_valid(in_arb_io_in_2_valid),
    .io_in_2_bits_addr(in_arb_io_in_2_bits_addr),
    .io_in_2_bits_data(in_arb_io_in_2_bits_data),
    .io_out_ready(in_arb_io_out_ready),
    .io_out_valid(in_arb_io_out_valid),
    .io_out_bits_addr(in_arb_io_out_bits_addr),
    .io_out_bits_data(in_arb_io_out_bits_data),
    .io_out_bits_mask(in_arb_io_out_bits_mask),
    .io_out_bits_tag(in_arb_io_out_bits_tag),
    .io_chosen(in_arb_io_chosen)
  );
  assign io_rd_mem_0_MemReq_ready = in_arb_io_in_0_ready; // @[CacheMemoryEngine.scala 83:21]
  assign io_rd_mem_0_MemResp_valid = _T_9 & _T_10; // @[CacheMemoryEngine.scala 112:32]
  assign io_rd_mem_0_MemResp_bits_data = io_cache_MemResp_bits_data; // @[CacheMemoryEngine.scala 116:31]
  assign io_rd_mem_1_MemReq_ready = in_arb_io_in_1_ready; // @[CacheMemoryEngine.scala 83:21]
  assign io_rd_mem_1_MemResp_valid = _T_15 & _T_10; // @[CacheMemoryEngine.scala 112:32]
  assign io_rd_mem_1_MemResp_bits_data = io_cache_MemResp_bits_data; // @[CacheMemoryEngine.scala 116:31]
  assign io_wr_mem_0_MemReq_ready = in_arb_io_in_2_ready; // @[CacheMemoryEngine.scala 87:31]
  assign io_wr_mem_0_MemResp_valid = _T_21 & _T_10; // @[CacheMemoryEngine.scala 120:42]
  assign io_cache_MemReq_valid = mstate == 2'h1; // @[CacheMemoryEngine.scala 130:25]
  assign io_cache_MemReq_bits_addr = in_data_reg_addr; // @[CacheMemoryEngine.scala 131:24]
  assign io_cache_MemReq_bits_data = in_data_reg_data; // @[CacheMemoryEngine.scala 131:24]
  assign io_cache_MemReq_bits_mask = in_data_reg_mask; // @[CacheMemoryEngine.scala 131:24]
  assign io_cache_MemReq_bits_tag = in_data_reg_tag; // @[CacheMemoryEngine.scala 131:24]
  assign in_arb_io_in_0_valid = io_rd_mem_0_MemReq_valid; // @[CacheMemoryEngine.scala 83:21]
  assign in_arb_io_in_0_bits_addr = io_rd_mem_0_MemReq_bits_addr; // @[CacheMemoryEngine.scala 83:21]
  assign in_arb_io_in_1_valid = io_rd_mem_1_MemReq_valid; // @[CacheMemoryEngine.scala 83:21]
  assign in_arb_io_in_1_bits_addr = io_rd_mem_1_MemReq_bits_addr; // @[CacheMemoryEngine.scala 83:21]
  assign in_arb_io_in_2_valid = io_wr_mem_0_MemReq_valid; // @[CacheMemoryEngine.scala 87:31]
  assign in_arb_io_in_2_bits_addr = io_wr_mem_0_MemReq_bits_addr; // @[CacheMemoryEngine.scala 87:31]
  assign in_arb_io_in_2_bits_data = io_wr_mem_0_MemReq_bits_data; // @[CacheMemoryEngine.scala 87:31]
  assign in_arb_io_out_ready = mstate == 2'h0; // @[CacheMemoryEngine.scala 129:23]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  in_arb_chosen = _RAND_0[1:0];
  _RAND_1 = {1{`RANDOM}};
  mstate = _RAND_1[1:0];
  _RAND_2 = {2{`RANDOM}};
  in_data_reg_addr = _RAND_2[63:0];
  _RAND_3 = {2{`RANDOM}};
  in_data_reg_data = _RAND_3[63:0];
  _RAND_4 = {1{`RANDOM}};
  in_data_reg_mask = _RAND_4[7:0];
  _RAND_5 = {1{`RANDOM}};
  in_data_reg_tag = _RAND_5[7:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (_T) begin
      in_arb_chosen <= in_arb_io_chosen;
    end
    if (reset) begin
      mstate <= 2'h0;
    end else if (_T_1) begin
      if (in_arb_io_out_valid) begin
        mstate <= 2'h1;
      end
    end else if (_T_2) begin
      if (io_cache_MemReq_ready) begin
        mstate <= 2'h2;
      end
    end else if (_T_3) begin
      if (_T_5) begin
        mstate <= 2'h0;
      end
    end
    if (reset) begin
      in_data_reg_addr <= 64'h0;
    end else if (_T) begin
      in_data_reg_addr <= in_arb_io_out_bits_addr;
    end
    if (reset) begin
      in_data_reg_data <= 64'h0;
    end else if (_T) begin
      in_data_reg_data <= in_arb_io_out_bits_data;
    end
    if (reset) begin
      in_data_reg_mask <= 8'h0;
    end else if (_T) begin
      in_data_reg_mask <= in_arb_io_out_bits_mask;
    end
    if (reset) begin
      in_data_reg_tag <= 8'h0;
    end else if (_T) begin
      in_data_reg_tag <= in_arb_io_out_bits_tag;
    end
  end
endmodule
module SplitCallDCR(
  input         clock,
  input         reset,
  output        io_In_ready,
  input         io_In_valid,
  input  [63:0] io_In_bits_dataPtrs_field1_data,
  input  [63:0] io_In_bits_dataPtrs_field0_data,
  input  [63:0] io_In_bits_dataVals_field1_data,
  input  [63:0] io_In_bits_dataVals_field0_data,
  input         io_Out_enable_ready,
  output        io_Out_enable_valid,
  output        io_Out_enable_bits_control,
  input         io_Out_dataPtrs_field1_0_ready,
  output        io_Out_dataPtrs_field1_0_valid,
  output [63:0] io_Out_dataPtrs_field1_0_bits_data,
  input         io_Out_dataPtrs_field0_0_ready,
  output        io_Out_dataPtrs_field0_0_valid,
  output [63:0] io_Out_dataPtrs_field0_0_bits_data,
  input         io_Out_dataVals_field1_0_ready,
  output        io_Out_dataVals_field1_0_valid,
  output [63:0] io_Out_dataVals_field1_0_bits_data,
  input         io_Out_dataVals_field0_0_ready,
  output        io_Out_dataVals_field0_0_valid,
  output [63:0] io_Out_dataVals_field0_0_bits_data,
  input         io_Out_dataVals_field0_1_ready,
  output        io_Out_dataVals_field0_1_valid,
  output [63:0] io_Out_dataVals_field0_1_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [63:0] _RAND_1;
  reg [63:0] _RAND_2;
  reg [63:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
`endif // RANDOMIZE_REG_INIT
  reg  inputReg_enable_control; // @[SplitDecoupled.scala 220:26]
  reg [63:0] inputReg_dataPtrs_field1_data; // @[SplitDecoupled.scala 220:26]
  reg [63:0] inputReg_dataPtrs_field0_data; // @[SplitDecoupled.scala 220:26]
  reg [63:0] inputReg_dataVals_field1_data; // @[SplitDecoupled.scala 220:26]
  reg [63:0] inputReg_dataVals_field0_data; // @[SplitDecoupled.scala 220:26]
  reg  enableValidReg; // @[SplitDecoupled.scala 222:31]
  reg  outputPtrsValidReg_0_0; // @[SplitDecoupled.scala 225:53]
  reg  outputPtrsValidReg_1_0; // @[SplitDecoupled.scala 225:53]
  reg  outputValsValidReg_0_0; // @[SplitDecoupled.scala 230:53]
  reg  outputValsValidReg_0_1; // @[SplitDecoupled.scala 230:53]
  reg  outputValsValidReg_1_0; // @[SplitDecoupled.scala 230:53]
  wire  valsValid_0 = outputValsValidReg_0_0 | outputValsValidReg_0_1; // @[SplitDecoupled.scala 241:52]
  reg  state; // @[SplitDecoupled.scala 260:22]
  wire  _T_1 = ~state; // @[SplitDecoupled.scala 262:24]
  wire  _T_3 = io_In_ready & io_In_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_0 = _T_3 | state; // @[SplitDecoupled.scala 266:27]
  wire  _GEN_2 = _T_3 | inputReg_enable_control; // @[SplitDecoupled.scala 266:27]
  wire  _T_5 = outputPtrsValidReg_0_0 & outputPtrsValidReg_1_0; // @[SplitDecoupled.scala 247:31]
  wire  _T_6 = ~_T_5; // @[SplitDecoupled.scala 247:7]
  wire  _T_7 = valsValid_0 & outputValsValidReg_1_0; // @[SplitDecoupled.scala 254:31]
  wire  _T_8 = ~_T_7; // @[SplitDecoupled.scala 254:7]
  wire  _T_9 = _T_6 & _T_8; // @[SplitDecoupled.scala 272:25]
  wire  _T_10 = ~enableValidReg; // @[SplitDecoupled.scala 272:43]
  wire  _T_11 = _T_9 & _T_10; // @[SplitDecoupled.scala 272:40]
  wire  _T_13 = io_In_valid & _T_1; // @[SplitDecoupled.scala 280:24]
  wire  _GEN_34 = _T_13 | outputPtrsValidReg_0_0; // @[SplitDecoupled.scala 280:45]
  wire  _T_15 = state & io_Out_dataPtrs_field0_0_ready; // @[SplitDecoupled.scala 283:32]
  wire  _GEN_36 = _T_13 | outputPtrsValidReg_1_0; // @[SplitDecoupled.scala 280:45]
  wire  _T_19 = state & io_Out_dataPtrs_field1_0_ready; // @[SplitDecoupled.scala 283:32]
  wire  _GEN_38 = _T_13 | outputValsValidReg_0_0; // @[SplitDecoupled.scala 293:45]
  wire  _T_23 = state & io_Out_dataVals_field0_0_ready; // @[SplitDecoupled.scala 296:32]
  wire  _GEN_40 = _T_13 | outputValsValidReg_0_1; // @[SplitDecoupled.scala 293:45]
  wire  _T_27 = state & io_Out_dataVals_field0_1_ready; // @[SplitDecoupled.scala 296:32]
  wire  _GEN_42 = _T_13 | outputValsValidReg_1_0; // @[SplitDecoupled.scala 293:45]
  wire  _T_31 = state & io_Out_dataVals_field1_0_ready; // @[SplitDecoupled.scala 296:32]
  wire  _GEN_44 = _T_13 | enableValidReg; // @[SplitDecoupled.scala 305:41]
  wire  _T_35 = state & io_Out_enable_ready; // @[SplitDecoupled.scala 308:28]
  assign io_In_ready = ~state; // @[SplitDecoupled.scala 262:15]
  assign io_Out_enable_valid = enableValidReg; // @[SplitDecoupled.scala 312:23]
  assign io_Out_enable_bits_control = inputReg_enable_control; // @[SplitDecoupled.scala 313:22]
  assign io_Out_dataPtrs_field1_0_valid = outputPtrsValidReg_1_0; // @[SplitDecoupled.scala 286:44]
  assign io_Out_dataPtrs_field1_0_bits_data = inputReg_dataPtrs_field1_data; // @[SplitDecoupled.scala 287:43]
  assign io_Out_dataPtrs_field0_0_valid = outputPtrsValidReg_0_0; // @[SplitDecoupled.scala 286:44]
  assign io_Out_dataPtrs_field0_0_bits_data = inputReg_dataPtrs_field0_data; // @[SplitDecoupled.scala 287:43]
  assign io_Out_dataVals_field1_0_valid = outputValsValidReg_1_0; // @[SplitDecoupled.scala 299:44]
  assign io_Out_dataVals_field1_0_bits_data = inputReg_dataVals_field1_data; // @[SplitDecoupled.scala 300:43]
  assign io_Out_dataVals_field0_0_valid = outputValsValidReg_0_0; // @[SplitDecoupled.scala 299:44]
  assign io_Out_dataVals_field0_0_bits_data = inputReg_dataVals_field0_data; // @[SplitDecoupled.scala 300:43]
  assign io_Out_dataVals_field0_1_valid = outputValsValidReg_0_1; // @[SplitDecoupled.scala 299:44]
  assign io_Out_dataVals_field0_1_bits_data = inputReg_dataVals_field0_data; // @[SplitDecoupled.scala 300:43]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  inputReg_enable_control = _RAND_0[0:0];
  _RAND_1 = {2{`RANDOM}};
  inputReg_dataPtrs_field1_data = _RAND_1[63:0];
  _RAND_2 = {2{`RANDOM}};
  inputReg_dataPtrs_field0_data = _RAND_2[63:0];
  _RAND_3 = {2{`RANDOM}};
  inputReg_dataVals_field1_data = _RAND_3[63:0];
  _RAND_4 = {2{`RANDOM}};
  inputReg_dataVals_field0_data = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  enableValidReg = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  outputPtrsValidReg_0_0 = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  outputPtrsValidReg_1_0 = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  outputValsValidReg_0_0 = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  outputValsValidReg_0_1 = _RAND_9[0:0];
  _RAND_10 = {1{`RANDOM}};
  outputValsValidReg_1_0 = _RAND_10[0:0];
  _RAND_11 = {1{`RANDOM}};
  state = _RAND_11[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      inputReg_enable_control <= 1'h0;
    end else if (_T_1) begin
      inputReg_enable_control <= _GEN_2;
    end
    if (reset) begin
      inputReg_dataPtrs_field1_data <= 64'h0;
    end else if (_T_1) begin
      if (_T_3) begin
        inputReg_dataPtrs_field1_data <= io_In_bits_dataPtrs_field1_data;
      end
    end
    if (reset) begin
      inputReg_dataPtrs_field0_data <= 64'h0;
    end else if (_T_1) begin
      if (_T_3) begin
        inputReg_dataPtrs_field0_data <= io_In_bits_dataPtrs_field0_data;
      end
    end
    if (reset) begin
      inputReg_dataVals_field1_data <= 64'h0;
    end else if (_T_1) begin
      if (_T_3) begin
        inputReg_dataVals_field1_data <= io_In_bits_dataVals_field1_data;
      end
    end
    if (reset) begin
      inputReg_dataVals_field0_data <= 64'h0;
    end else if (_T_1) begin
      if (_T_3) begin
        inputReg_dataVals_field0_data <= io_In_bits_dataVals_field0_data;
      end
    end
    if (reset) begin
      enableValidReg <= 1'h0;
    end else if (_T_35) begin
      enableValidReg <= 1'h0;
    end else begin
      enableValidReg <= _GEN_44;
    end
    if (reset) begin
      outputPtrsValidReg_0_0 <= 1'h0;
    end else if (_T_15) begin
      outputPtrsValidReg_0_0 <= 1'h0;
    end else begin
      outputPtrsValidReg_0_0 <= _GEN_34;
    end
    if (reset) begin
      outputPtrsValidReg_1_0 <= 1'h0;
    end else if (_T_19) begin
      outputPtrsValidReg_1_0 <= 1'h0;
    end else begin
      outputPtrsValidReg_1_0 <= _GEN_36;
    end
    if (reset) begin
      outputValsValidReg_0_0 <= 1'h0;
    end else if (_T_23) begin
      outputValsValidReg_0_0 <= 1'h0;
    end else begin
      outputValsValidReg_0_0 <= _GEN_38;
    end
    if (reset) begin
      outputValsValidReg_0_1 <= 1'h0;
    end else if (_T_27) begin
      outputValsValidReg_0_1 <= 1'h0;
    end else begin
      outputValsValidReg_0_1 <= _GEN_40;
    end
    if (reset) begin
      outputValsValidReg_1_0 <= 1'h0;
    end else if (_T_31) begin
      outputValsValidReg_1_0 <= 1'h0;
    end else begin
      outputValsValidReg_1_0 <= _GEN_42;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_1) begin
      state <= _GEN_0;
    end else if (state) begin
      if (_T_11) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module LoopBlockNode(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  output        io_InLiveIn_0_ready,
  input         io_InLiveIn_0_valid,
  input  [63:0] io_InLiveIn_0_bits_data,
  output        io_InLiveIn_1_ready,
  input         io_InLiveIn_1_valid,
  input  [63:0] io_InLiveIn_1_bits_data,
  output        io_InLiveIn_2_ready,
  input         io_InLiveIn_2_valid,
  input  [63:0] io_InLiveIn_2_bits_data,
  output        io_InLiveIn_3_ready,
  input         io_InLiveIn_3_valid,
  input  [63:0] io_InLiveIn_3_bits_data,
  input         io_OutLiveIn_field3_0_ready,
  output        io_OutLiveIn_field3_0_valid,
  output [63:0] io_OutLiveIn_field3_0_bits_data,
  input         io_OutLiveIn_field2_0_ready,
  output        io_OutLiveIn_field2_0_valid,
  output [63:0] io_OutLiveIn_field2_0_bits_data,
  input         io_OutLiveIn_field1_0_ready,
  output        io_OutLiveIn_field1_0_valid,
  output [63:0] io_OutLiveIn_field1_0_bits_data,
  input         io_OutLiveIn_field0_0_ready,
  output        io_OutLiveIn_field0_0_valid,
  output [63:0] io_OutLiveIn_field0_0_bits_data,
  input         io_activate_loop_start_ready,
  output        io_activate_loop_start_valid,
  output        io_activate_loop_start_bits_control,
  input         io_activate_loop_back_ready,
  output        io_activate_loop_back_valid,
  output        io_activate_loop_back_bits_control,
  output        io_loopBack_0_ready,
  input         io_loopBack_0_valid,
  input         io_loopBack_0_bits_control,
  output        io_loopFinish_0_ready,
  input         io_loopFinish_0_valid,
  input         io_loopFinish_0_bits_control,
  output        io_CarryDepenIn_0_ready,
  input         io_CarryDepenIn_0_valid,
  input  [63:0] io_CarryDepenIn_0_bits_data,
  input         io_CarryDepenOut_field0_0_ready,
  output        io_CarryDepenOut_field0_0_valid,
  output [63:0] io_CarryDepenOut_field0_0_bits_data,
  input         io_loopExit_0_ready,
  output        io_loopExit_0_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [63:0] _RAND_7;
  reg [63:0] _RAND_8;
  reg [63:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [63:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_21;
  reg [31:0] _RAND_22;
  reg [31:0] _RAND_23;
  reg [31:0] _RAND_24;
  reg [31:0] _RAND_25;
  reg [31:0] _RAND_26;
  reg [31:0] _RAND_27;
  reg [31:0] _RAND_28;
  reg [31:0] _RAND_29;
  reg [31:0] _RAND_30;
  reg [31:0] _RAND_31;
`endif // RANDOMIZE_REG_INIT
  reg  enable_R_control; // @[LoopBlock.scala 531:25]
  reg  enable_valid_R; // @[LoopBlock.scala 532:31]
  reg  loop_back_R_0_control; // @[LoopBlock.scala 534:50]
  reg  loop_back_valid_R_0; // @[LoopBlock.scala 535:56]
  reg  loop_finish_R_0_control; // @[LoopBlock.scala 537:54]
  reg  loop_finish_valid_R_0; // @[LoopBlock.scala 538:60]
  reg [63:0] in_live_in_R_0_data; // @[LoopBlock.scala 540:53]
  reg [63:0] in_live_in_R_1_data; // @[LoopBlock.scala 540:53]
  reg [63:0] in_live_in_R_2_data; // @[LoopBlock.scala 540:53]
  reg [63:0] in_live_in_R_3_data; // @[LoopBlock.scala 540:53]
  reg  in_live_in_valid_R_0; // @[LoopBlock.scala 541:59]
  reg  in_live_in_valid_R_1; // @[LoopBlock.scala 541:59]
  reg  in_live_in_valid_R_2; // @[LoopBlock.scala 541:59]
  reg  in_live_in_valid_R_3; // @[LoopBlock.scala 541:59]
  reg [63:0] in_carry_in_R_0_data; // @[LoopBlock.scala 543:56]
  reg  in_carry_in_valid_R_0; // @[LoopBlock.scala 544:62]
  reg  out_live_in_valid_R_0_0; // @[LoopBlock.scala 556:47]
  reg  out_live_in_valid_R_1_0; // @[LoopBlock.scala 556:47]
  reg  out_live_in_valid_R_2_0; // @[LoopBlock.scala 556:47]
  reg  out_live_in_valid_R_3_0; // @[LoopBlock.scala 556:47]
  reg  out_live_in_fire_R_0_0; // @[LoopBlock.scala 560:47]
  reg  out_live_in_fire_R_1_0; // @[LoopBlock.scala 560:47]
  reg  out_live_in_fire_R_2_0; // @[LoopBlock.scala 560:47]
  reg  out_live_in_fire_R_3_0; // @[LoopBlock.scala 560:47]
  reg  out_carry_out_valid_R_0_0; // @[LoopBlock.scala 576:44]
  reg  active_loop_start_R_control; // @[LoopBlock.scala 584:36]
  reg  active_loop_start_valid_R; // @[LoopBlock.scala 585:42]
  reg  active_loop_back_R_control; // @[LoopBlock.scala 587:35]
  reg  active_loop_back_valid_R; // @[LoopBlock.scala 588:41]
  reg  loop_exit_valid_R_0; // @[LoopBlock.scala 591:53]
  reg  loop_exit_fire_R_0; // @[LoopBlock.scala 592:52]
  wire  _T_16 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_5 = _T_16 | enable_valid_R; // @[LoopBlock.scala 599:26]
  wire  _T_18 = io_loopBack_0_ready & io_loopBack_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_7 = _T_18 ? io_loopBack_0_bits_control : loop_back_R_0_control; // @[LoopBlock.scala 606:33]
  wire  _GEN_9 = _T_18 | loop_back_valid_R_0; // @[LoopBlock.scala 606:33]
  wire  _T_20 = io_loopFinish_0_ready & io_loopFinish_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_20 ? io_loopFinish_0_bits_control : loop_finish_R_0_control; // @[LoopBlock.scala 615:35]
  wire  _GEN_13 = _T_20 | loop_finish_valid_R_0; // @[LoopBlock.scala 615:35]
  wire  _T_22 = io_InLiveIn_0_ready & io_InLiveIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_17 = _T_22 | in_live_in_valid_R_0; // @[LoopBlock.scala 626:33]
  wire  _T_24 = io_InLiveIn_1_ready & io_InLiveIn_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_21 = _T_24 | in_live_in_valid_R_1; // @[LoopBlock.scala 626:33]
  wire  _T_26 = io_InLiveIn_2_ready & io_InLiveIn_2_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_25 = _T_26 | in_live_in_valid_R_2; // @[LoopBlock.scala 626:33]
  wire  _T_28 = io_InLiveIn_3_ready & io_InLiveIn_3_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_29 = _T_28 | in_live_in_valid_R_3; // @[LoopBlock.scala 626:33]
  wire  _T_30 = io_CarryDepenIn_0_ready & io_CarryDepenIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_33 = _T_30 | in_carry_in_valid_R_0; // @[LoopBlock.scala 644:37]
  wire  _T_31 = io_activate_loop_start_ready & io_activate_loop_start_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_34 = _T_31 ? 1'h0 : active_loop_start_valid_R; // @[LoopBlock.scala 707:39]
  wire  _T_32 = io_activate_loop_back_ready & io_activate_loop_back_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_35 = _T_32 ? 1'h0 : active_loop_back_valid_R; // @[LoopBlock.scala 711:38]
  wire  _T_33 = io_loopExit_0_ready & io_loopExit_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_36 = _T_33 ? 1'h0 : loop_exit_valid_R_0; // @[LoopBlock.scala 716:33]
  wire  _GEN_37 = _T_33 | loop_exit_fire_R_0; // @[LoopBlock.scala 716:33]
  wire  _T_34 = io_OutLiveIn_field0_0_ready & io_OutLiveIn_field0_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_38 = _T_34 ? 1'h0 : out_live_in_valid_R_0_0; // @[LoopBlock.scala 725:57]
  wire  _GEN_39 = _T_34 | out_live_in_fire_R_0_0; // @[LoopBlock.scala 725:57]
  wire  _T_35 = io_OutLiveIn_field1_0_ready & io_OutLiveIn_field1_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_40 = _T_35 ? 1'h0 : out_live_in_valid_R_1_0; // @[LoopBlock.scala 725:57]
  wire  _GEN_41 = _T_35 | out_live_in_fire_R_1_0; // @[LoopBlock.scala 725:57]
  wire  _T_36 = io_OutLiveIn_field2_0_ready & io_OutLiveIn_field2_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_42 = _T_36 ? 1'h0 : out_live_in_valid_R_2_0; // @[LoopBlock.scala 725:57]
  wire  _GEN_43 = _T_36 | out_live_in_fire_R_2_0; // @[LoopBlock.scala 725:57]
  wire  _T_37 = io_OutLiveIn_field3_0_ready & io_OutLiveIn_field3_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_44 = _T_37 ? 1'h0 : out_live_in_valid_R_3_0; // @[LoopBlock.scala 725:57]
  wire  _GEN_45 = _T_37 | out_live_in_fire_R_3_0; // @[LoopBlock.scala 725:57]
  wire  _T_38 = io_CarryDepenOut_field0_0_ready & io_CarryDepenOut_field0_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_46 = _T_38 ? 1'h0 : out_carry_out_valid_R_0_0; // @[LoopBlock.scala 745:61]
  reg [1:0] state; // @[LoopBlock.scala 864:22]
  wire  _T_42 = 2'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_43 = in_live_in_valid_R_0 & in_live_in_valid_R_1; // @[LoopBlock.scala 768:35]
  wire  _T_44 = _T_43 & in_live_in_valid_R_2; // @[LoopBlock.scala 768:35]
  wire  _T_45 = _T_44 & in_live_in_valid_R_3; // @[LoopBlock.scala 768:35]
  wire  _T_46 = _T_45 & enable_valid_R; // @[LoopBlock.scala 906:28]
  wire  _GEN_48 = enable_R_control | _GEN_38; // @[LoopBlock.scala 907:26]
  wire  _GEN_49 = enable_R_control | _GEN_40; // @[LoopBlock.scala 907:26]
  wire  _GEN_50 = enable_R_control | _GEN_42; // @[LoopBlock.scala 907:26]
  wire  _GEN_51 = enable_R_control | _GEN_44; // @[LoopBlock.scala 907:26]
  wire  _GEN_52 = enable_R_control | _GEN_46; // @[LoopBlock.scala 907:26]
  wire  _GEN_54 = enable_R_control | active_loop_start_R_control; // @[LoopBlock.scala 907:26]
  wire  _GEN_56 = enable_R_control | _GEN_34; // @[LoopBlock.scala 907:26]
  wire  _GEN_60 = enable_R_control | _GEN_35; // @[LoopBlock.scala 907:26]
  wire  _T_50 = 2'h1 == state; // @[Conditional.scala 37:30]
  wire  _T_51 = loop_back_valid_R_0 & loop_finish_valid_R_0; // @[LoopBlock.scala 937:30]
  wire  _T_53 = out_live_in_fire_R_0_0 & out_live_in_fire_R_1_0; // @[LoopBlock.scala 831:26]
  wire  _T_54 = _T_53 & out_live_in_fire_R_2_0; // @[LoopBlock.scala 831:26]
  wire  _T_55 = _T_54 & out_live_in_fire_R_3_0; // @[LoopBlock.scala 831:26]
  wire  _T_56 = _T_51 & _T_55; // @[LoopBlock.scala 938:29]
  wire  _GEN_84 = loop_finish_R_0_control | _GEN_36; // @[LoopBlock.scala 974:64]
  wire  _GEN_89 = loop_finish_R_0_control ? 1'h0 : active_loop_back_R_control; // @[LoopBlock.scala 974:64]
  wire  _GEN_98 = loop_back_R_0_control | _GEN_34; // @[LoopBlock.scala 941:56]
  wire  _GEN_100 = loop_back_R_0_control | _GEN_89; // @[LoopBlock.scala 941:56]
  wire  _GEN_102 = loop_back_R_0_control | _GEN_35; // @[LoopBlock.scala 941:56]
  wire  _GEN_108 = loop_back_R_0_control | _GEN_38; // @[LoopBlock.scala 941:56]
  wire  _GEN_109 = loop_back_R_0_control | _GEN_40; // @[LoopBlock.scala 941:56]
  wire  _GEN_110 = loop_back_R_0_control | _GEN_42; // @[LoopBlock.scala 941:56]
  wire  _GEN_111 = loop_back_R_0_control | _GEN_44; // @[LoopBlock.scala 941:56]
  wire  _GEN_112 = loop_back_R_0_control | _GEN_46; // @[LoopBlock.scala 941:56]
  wire  _T_66 = 2'h2 == state; // @[Conditional.scala 37:30]
  assign io_enable_ready = ~enable_valid_R; // @[LoopBlock.scala 598:19]
  assign io_InLiveIn_0_ready = ~in_live_in_valid_R_0; // @[LoopBlock.scala 625:26]
  assign io_InLiveIn_1_ready = ~in_live_in_valid_R_1; // @[LoopBlock.scala 625:26]
  assign io_InLiveIn_2_ready = ~in_live_in_valid_R_2; // @[LoopBlock.scala 625:26]
  assign io_InLiveIn_3_ready = ~in_live_in_valid_R_3; // @[LoopBlock.scala 625:26]
  assign io_OutLiveIn_field3_0_valid = out_live_in_valid_R_3_0; // @[LoopBlock.scala 668:50]
  assign io_OutLiveIn_field3_0_bits_data = in_live_in_R_3_data; // @[LoopBlock.scala 667:49]
  assign io_OutLiveIn_field2_0_valid = out_live_in_valid_R_2_0; // @[LoopBlock.scala 668:50]
  assign io_OutLiveIn_field2_0_bits_data = in_live_in_R_2_data; // @[LoopBlock.scala 667:49]
  assign io_OutLiveIn_field1_0_valid = out_live_in_valid_R_1_0; // @[LoopBlock.scala 668:50]
  assign io_OutLiveIn_field1_0_bits_data = in_live_in_R_1_data; // @[LoopBlock.scala 667:49]
  assign io_OutLiveIn_field0_0_valid = out_live_in_valid_R_0_0; // @[LoopBlock.scala 668:50]
  assign io_OutLiveIn_field0_0_bits_data = in_live_in_R_0_data; // @[LoopBlock.scala 667:49]
  assign io_activate_loop_start_valid = active_loop_start_valid_R; // @[LoopBlock.scala 692:32]
  assign io_activate_loop_start_bits_control = active_loop_start_R_control; // @[LoopBlock.scala 691:31]
  assign io_activate_loop_back_valid = active_loop_back_valid_R; // @[LoopBlock.scala 695:31]
  assign io_activate_loop_back_bits_control = active_loop_back_R_control; // @[LoopBlock.scala 694:30]
  assign io_loopBack_0_ready = ~loop_back_valid_R_0; // @[LoopBlock.scala 605:26]
  assign io_loopFinish_0_ready = ~loop_finish_valid_R_0; // @[LoopBlock.scala 614:28]
  assign io_CarryDepenIn_0_ready = ~in_carry_in_valid_R_0; // @[LoopBlock.scala 643:30]
  assign io_CarryDepenOut_field0_0_valid = out_carry_out_valid_R_0_0; // @[LoopBlock.scala 684:54]
  assign io_CarryDepenOut_field0_0_bits_data = in_carry_in_R_0_data; // @[LoopBlock.scala 683:53]
  assign io_loopExit_0_valid = loop_exit_valid_R_0; // @[LoopBlock.scala 699:26]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  loop_back_R_0_control = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  loop_back_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  loop_finish_R_0_control = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  loop_finish_valid_R_0 = _RAND_5[0:0];
  _RAND_6 = {2{`RANDOM}};
  in_live_in_R_0_data = _RAND_6[63:0];
  _RAND_7 = {2{`RANDOM}};
  in_live_in_R_1_data = _RAND_7[63:0];
  _RAND_8 = {2{`RANDOM}};
  in_live_in_R_2_data = _RAND_8[63:0];
  _RAND_9 = {2{`RANDOM}};
  in_live_in_R_3_data = _RAND_9[63:0];
  _RAND_10 = {1{`RANDOM}};
  in_live_in_valid_R_0 = _RAND_10[0:0];
  _RAND_11 = {1{`RANDOM}};
  in_live_in_valid_R_1 = _RAND_11[0:0];
  _RAND_12 = {1{`RANDOM}};
  in_live_in_valid_R_2 = _RAND_12[0:0];
  _RAND_13 = {1{`RANDOM}};
  in_live_in_valid_R_3 = _RAND_13[0:0];
  _RAND_14 = {2{`RANDOM}};
  in_carry_in_R_0_data = _RAND_14[63:0];
  _RAND_15 = {1{`RANDOM}};
  in_carry_in_valid_R_0 = _RAND_15[0:0];
  _RAND_16 = {1{`RANDOM}};
  out_live_in_valid_R_0_0 = _RAND_16[0:0];
  _RAND_17 = {1{`RANDOM}};
  out_live_in_valid_R_1_0 = _RAND_17[0:0];
  _RAND_18 = {1{`RANDOM}};
  out_live_in_valid_R_2_0 = _RAND_18[0:0];
  _RAND_19 = {1{`RANDOM}};
  out_live_in_valid_R_3_0 = _RAND_19[0:0];
  _RAND_20 = {1{`RANDOM}};
  out_live_in_fire_R_0_0 = _RAND_20[0:0];
  _RAND_21 = {1{`RANDOM}};
  out_live_in_fire_R_1_0 = _RAND_21[0:0];
  _RAND_22 = {1{`RANDOM}};
  out_live_in_fire_R_2_0 = _RAND_22[0:0];
  _RAND_23 = {1{`RANDOM}};
  out_live_in_fire_R_3_0 = _RAND_23[0:0];
  _RAND_24 = {1{`RANDOM}};
  out_carry_out_valid_R_0_0 = _RAND_24[0:0];
  _RAND_25 = {1{`RANDOM}};
  active_loop_start_R_control = _RAND_25[0:0];
  _RAND_26 = {1{`RANDOM}};
  active_loop_start_valid_R = _RAND_26[0:0];
  _RAND_27 = {1{`RANDOM}};
  active_loop_back_R_control = _RAND_27[0:0];
  _RAND_28 = {1{`RANDOM}};
  active_loop_back_valid_R = _RAND_28[0:0];
  _RAND_29 = {1{`RANDOM}};
  loop_exit_valid_R_0 = _RAND_29[0:0];
  _RAND_30 = {1{`RANDOM}};
  loop_exit_fire_R_0 = _RAND_30[0:0];
  _RAND_31 = {1{`RANDOM}};
  state = _RAND_31[1:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_42) begin
      if (_T_16) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_50) begin
      if (_T_16) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        enable_R_control <= 1'h0;
      end else if (_T_16) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_16) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_42) begin
      enable_valid_R <= _GEN_5;
    end else if (_T_50) begin
      enable_valid_R <= _GEN_5;
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        enable_valid_R <= 1'h0;
      end else begin
        enable_valid_R <= _GEN_5;
      end
    end else begin
      enable_valid_R <= _GEN_5;
    end
    if (reset) begin
      loop_back_R_0_control <= 1'h0;
    end else if (_T_42) begin
      if (_T_18) begin
        loop_back_R_0_control <= io_loopBack_0_bits_control;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          loop_back_R_0_control <= 1'h0;
        end else if (_T_18) begin
          loop_back_R_0_control <= io_loopBack_0_bits_control;
        end
      end else if (_T_18) begin
        loop_back_R_0_control <= io_loopBack_0_bits_control;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        loop_back_R_0_control <= 1'h0;
      end else if (_T_18) begin
        loop_back_R_0_control <= io_loopBack_0_bits_control;
      end
    end else begin
      loop_back_R_0_control <= _GEN_7;
    end
    if (reset) begin
      loop_back_valid_R_0 <= 1'h0;
    end else if (_T_42) begin
      loop_back_valid_R_0 <= _GEN_9;
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          loop_back_valid_R_0 <= 1'h0;
        end else begin
          loop_back_valid_R_0 <= _GEN_9;
        end
      end else begin
        loop_back_valid_R_0 <= _GEN_9;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        loop_back_valid_R_0 <= 1'h0;
      end else begin
        loop_back_valid_R_0 <= _GEN_9;
      end
    end else begin
      loop_back_valid_R_0 <= _GEN_9;
    end
    if (reset) begin
      loop_finish_R_0_control <= 1'h0;
    end else if (_T_42) begin
      if (_T_20) begin
        loop_finish_R_0_control <= io_loopFinish_0_bits_control;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          loop_finish_R_0_control <= 1'h0;
        end else if (_T_20) begin
          loop_finish_R_0_control <= io_loopFinish_0_bits_control;
        end
      end else if (_T_20) begin
        loop_finish_R_0_control <= io_loopFinish_0_bits_control;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        loop_finish_R_0_control <= 1'h0;
      end else if (_T_20) begin
        loop_finish_R_0_control <= io_loopFinish_0_bits_control;
      end
    end else begin
      loop_finish_R_0_control <= _GEN_11;
    end
    if (reset) begin
      loop_finish_valid_R_0 <= 1'h0;
    end else if (_T_42) begin
      loop_finish_valid_R_0 <= _GEN_13;
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          loop_finish_valid_R_0 <= 1'h0;
        end else begin
          loop_finish_valid_R_0 <= _GEN_13;
        end
      end else begin
        loop_finish_valid_R_0 <= _GEN_13;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        loop_finish_valid_R_0 <= 1'h0;
      end else begin
        loop_finish_valid_R_0 <= _GEN_13;
      end
    end else begin
      loop_finish_valid_R_0 <= _GEN_13;
    end
    if (reset) begin
      in_live_in_R_0_data <= 64'h0;
    end else if (_T_42) begin
      if (_T_22) begin
        in_live_in_R_0_data <= io_InLiveIn_0_bits_data;
      end
    end else if (_T_50) begin
      if (_T_22) begin
        in_live_in_R_0_data <= io_InLiveIn_0_bits_data;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_R_0_data <= 64'h0;
      end else if (_T_22) begin
        in_live_in_R_0_data <= io_InLiveIn_0_bits_data;
      end
    end else if (_T_22) begin
      in_live_in_R_0_data <= io_InLiveIn_0_bits_data;
    end
    if (reset) begin
      in_live_in_R_1_data <= 64'h0;
    end else if (_T_42) begin
      if (_T_24) begin
        in_live_in_R_1_data <= io_InLiveIn_1_bits_data;
      end
    end else if (_T_50) begin
      if (_T_24) begin
        in_live_in_R_1_data <= io_InLiveIn_1_bits_data;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_R_1_data <= 64'h0;
      end else if (_T_24) begin
        in_live_in_R_1_data <= io_InLiveIn_1_bits_data;
      end
    end else if (_T_24) begin
      in_live_in_R_1_data <= io_InLiveIn_1_bits_data;
    end
    if (reset) begin
      in_live_in_R_2_data <= 64'h0;
    end else if (_T_42) begin
      if (_T_26) begin
        in_live_in_R_2_data <= io_InLiveIn_2_bits_data;
      end
    end else if (_T_50) begin
      if (_T_26) begin
        in_live_in_R_2_data <= io_InLiveIn_2_bits_data;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_R_2_data <= 64'h0;
      end else if (_T_26) begin
        in_live_in_R_2_data <= io_InLiveIn_2_bits_data;
      end
    end else if (_T_26) begin
      in_live_in_R_2_data <= io_InLiveIn_2_bits_data;
    end
    if (reset) begin
      in_live_in_R_3_data <= 64'h0;
    end else if (_T_42) begin
      if (_T_28) begin
        in_live_in_R_3_data <= io_InLiveIn_3_bits_data;
      end
    end else if (_T_50) begin
      if (_T_28) begin
        in_live_in_R_3_data <= io_InLiveIn_3_bits_data;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_R_3_data <= 64'h0;
      end else if (_T_28) begin
        in_live_in_R_3_data <= io_InLiveIn_3_bits_data;
      end
    end else if (_T_28) begin
      in_live_in_R_3_data <= io_InLiveIn_3_bits_data;
    end
    if (reset) begin
      in_live_in_valid_R_0 <= 1'h0;
    end else if (_T_42) begin
      in_live_in_valid_R_0 <= _GEN_17;
    end else if (_T_50) begin
      in_live_in_valid_R_0 <= _GEN_17;
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_valid_R_0 <= 1'h0;
      end else begin
        in_live_in_valid_R_0 <= _GEN_17;
      end
    end else begin
      in_live_in_valid_R_0 <= _GEN_17;
    end
    if (reset) begin
      in_live_in_valid_R_1 <= 1'h0;
    end else if (_T_42) begin
      in_live_in_valid_R_1 <= _GEN_21;
    end else if (_T_50) begin
      in_live_in_valid_R_1 <= _GEN_21;
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_valid_R_1 <= 1'h0;
      end else begin
        in_live_in_valid_R_1 <= _GEN_21;
      end
    end else begin
      in_live_in_valid_R_1 <= _GEN_21;
    end
    if (reset) begin
      in_live_in_valid_R_2 <= 1'h0;
    end else if (_T_42) begin
      in_live_in_valid_R_2 <= _GEN_25;
    end else if (_T_50) begin
      in_live_in_valid_R_2 <= _GEN_25;
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_valid_R_2 <= 1'h0;
      end else begin
        in_live_in_valid_R_2 <= _GEN_25;
      end
    end else begin
      in_live_in_valid_R_2 <= _GEN_25;
    end
    if (reset) begin
      in_live_in_valid_R_3 <= 1'h0;
    end else if (_T_42) begin
      in_live_in_valid_R_3 <= _GEN_29;
    end else if (_T_50) begin
      in_live_in_valid_R_3 <= _GEN_29;
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_live_in_valid_R_3 <= 1'h0;
      end else begin
        in_live_in_valid_R_3 <= _GEN_29;
      end
    end else begin
      in_live_in_valid_R_3 <= _GEN_29;
    end
    if (reset) begin
      in_carry_in_R_0_data <= 64'h0;
    end else if (_T_30) begin
      in_carry_in_R_0_data <= io_CarryDepenIn_0_bits_data;
    end
    if (reset) begin
      in_carry_in_valid_R_0 <= 1'h0;
    end else if (_T_42) begin
      in_carry_in_valid_R_0 <= _GEN_33;
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          in_carry_in_valid_R_0 <= 1'h0;
        end else begin
          in_carry_in_valid_R_0 <= _GEN_33;
        end
      end else begin
        in_carry_in_valid_R_0 <= _GEN_33;
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        in_carry_in_valid_R_0 <= 1'h0;
      end else begin
        in_carry_in_valid_R_0 <= _GEN_33;
      end
    end else begin
      in_carry_in_valid_R_0 <= _GEN_33;
    end
    if (reset) begin
      out_live_in_valid_R_0_0 <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        out_live_in_valid_R_0_0 <= _GEN_48;
      end else if (_T_34) begin
        out_live_in_valid_R_0_0 <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        out_live_in_valid_R_0_0 <= _GEN_108;
      end else if (_T_34) begin
        out_live_in_valid_R_0_0 <= 1'h0;
      end
    end else if (_T_34) begin
      out_live_in_valid_R_0_0 <= 1'h0;
    end
    if (reset) begin
      out_live_in_valid_R_1_0 <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        out_live_in_valid_R_1_0 <= _GEN_49;
      end else if (_T_35) begin
        out_live_in_valid_R_1_0 <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        out_live_in_valid_R_1_0 <= _GEN_109;
      end else if (_T_35) begin
        out_live_in_valid_R_1_0 <= 1'h0;
      end
    end else if (_T_35) begin
      out_live_in_valid_R_1_0 <= 1'h0;
    end
    if (reset) begin
      out_live_in_valid_R_2_0 <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        out_live_in_valid_R_2_0 <= _GEN_50;
      end else if (_T_36) begin
        out_live_in_valid_R_2_0 <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        out_live_in_valid_R_2_0 <= _GEN_110;
      end else if (_T_36) begin
        out_live_in_valid_R_2_0 <= 1'h0;
      end
    end else if (_T_36) begin
      out_live_in_valid_R_2_0 <= 1'h0;
    end
    if (reset) begin
      out_live_in_valid_R_3_0 <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        out_live_in_valid_R_3_0 <= _GEN_51;
      end else if (_T_37) begin
        out_live_in_valid_R_3_0 <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        out_live_in_valid_R_3_0 <= _GEN_111;
      end else if (_T_37) begin
        out_live_in_valid_R_3_0 <= 1'h0;
      end
    end else if (_T_37) begin
      out_live_in_valid_R_3_0 <= 1'h0;
    end
    if (reset) begin
      out_live_in_fire_R_0_0 <= 1'h0;
    end else if (_T_42) begin
      out_live_in_fire_R_0_0 <= _GEN_39;
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          out_live_in_fire_R_0_0 <= 1'h0;
        end else begin
          out_live_in_fire_R_0_0 <= _GEN_39;
        end
      end else begin
        out_live_in_fire_R_0_0 <= _GEN_39;
      end
    end else begin
      out_live_in_fire_R_0_0 <= _GEN_39;
    end
    if (reset) begin
      out_live_in_fire_R_1_0 <= 1'h0;
    end else if (_T_42) begin
      out_live_in_fire_R_1_0 <= _GEN_41;
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          out_live_in_fire_R_1_0 <= 1'h0;
        end else begin
          out_live_in_fire_R_1_0 <= _GEN_41;
        end
      end else begin
        out_live_in_fire_R_1_0 <= _GEN_41;
      end
    end else begin
      out_live_in_fire_R_1_0 <= _GEN_41;
    end
    if (reset) begin
      out_live_in_fire_R_2_0 <= 1'h0;
    end else if (_T_42) begin
      out_live_in_fire_R_2_0 <= _GEN_43;
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          out_live_in_fire_R_2_0 <= 1'h0;
        end else begin
          out_live_in_fire_R_2_0 <= _GEN_43;
        end
      end else begin
        out_live_in_fire_R_2_0 <= _GEN_43;
      end
    end else begin
      out_live_in_fire_R_2_0 <= _GEN_43;
    end
    if (reset) begin
      out_live_in_fire_R_3_0 <= 1'h0;
    end else if (_T_42) begin
      out_live_in_fire_R_3_0 <= _GEN_45;
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          out_live_in_fire_R_3_0 <= 1'h0;
        end else begin
          out_live_in_fire_R_3_0 <= _GEN_45;
        end
      end else begin
        out_live_in_fire_R_3_0 <= _GEN_45;
      end
    end else begin
      out_live_in_fire_R_3_0 <= _GEN_45;
    end
    if (reset) begin
      out_carry_out_valid_R_0_0 <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        out_carry_out_valid_R_0_0 <= _GEN_52;
      end else if (_T_38) begin
        out_carry_out_valid_R_0_0 <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        out_carry_out_valid_R_0_0 <= _GEN_112;
      end else if (_T_38) begin
        out_carry_out_valid_R_0_0 <= 1'h0;
      end
    end else if (_T_38) begin
      out_carry_out_valid_R_0_0 <= 1'h0;
    end
    if (reset) begin
      active_loop_start_R_control <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        active_loop_start_R_control <= _GEN_54;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          active_loop_start_R_control <= 1'h0;
        end else if (loop_finish_R_0_control) begin
          active_loop_start_R_control <= 1'h0;
        end
      end
    end
    if (reset) begin
      active_loop_start_valid_R <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        active_loop_start_valid_R <= _GEN_56;
      end else if (_T_31) begin
        active_loop_start_valid_R <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        active_loop_start_valid_R <= _GEN_98;
      end else if (_T_31) begin
        active_loop_start_valid_R <= 1'h0;
      end
    end else if (_T_31) begin
      active_loop_start_valid_R <= 1'h0;
    end
    if (reset) begin
      active_loop_back_R_control <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        if (enable_R_control) begin
          active_loop_back_R_control <= 1'h0;
        end
      end
    end else if (_T_50) begin
      if (_T_56) begin
        active_loop_back_R_control <= _GEN_100;
      end
    end
    if (reset) begin
      active_loop_back_valid_R <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        active_loop_back_valid_R <= _GEN_60;
      end else if (_T_32) begin
        active_loop_back_valid_R <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        active_loop_back_valid_R <= _GEN_102;
      end else if (_T_32) begin
        active_loop_back_valid_R <= 1'h0;
      end
    end else if (_T_32) begin
      active_loop_back_valid_R <= 1'h0;
    end
    if (reset) begin
      loop_exit_valid_R_0 <= 1'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        if (enable_R_control) begin
          if (_T_33) begin
            loop_exit_valid_R_0 <= 1'h0;
          end
        end else begin
          loop_exit_valid_R_0 <= 1'h1;
        end
      end else if (_T_33) begin
        loop_exit_valid_R_0 <= 1'h0;
      end
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          if (_T_33) begin
            loop_exit_valid_R_0 <= 1'h0;
          end
        end else begin
          loop_exit_valid_R_0 <= _GEN_84;
        end
      end else if (_T_33) begin
        loop_exit_valid_R_0 <= 1'h0;
      end
    end else begin
      loop_exit_valid_R_0 <= _GEN_36;
    end
    if (reset) begin
      loop_exit_fire_R_0 <= 1'h0;
    end else begin
      loop_exit_fire_R_0 <= _GEN_37;
    end
    if (reset) begin
      state <= 2'h0;
    end else if (_T_42) begin
      if (_T_46) begin
        if (enable_R_control) begin
          state <= 2'h1;
        end else begin
          state <= 2'h2;
        end
      end
    end else if (_T_50) begin
      if (_T_56) begin
        if (loop_back_R_0_control) begin
          state <= 2'h1;
        end else if (loop_finish_R_0_control) begin
          state <= 2'h2;
        end
      end
    end else if (_T_66) begin
      if (loop_exit_fire_R_0) begin
        state <= 2'h0;
      end
    end
  end
endmodule
module BasicBlockNoMaskFastNode(
  input   clock,
  input   reset,
  output  io_predicateIn_0_ready,
  input   io_predicateIn_0_valid,
  input   io_predicateIn_0_bits_control,
  input   io_Out_0_ready,
  output  io_Out_0_valid,
  input   io_Out_1_ready,
  output  io_Out_1_valid,
  output  io_Out_1_bits_control,
  input   io_Out_2_ready,
  output  io_Out_2_valid,
  output  io_Out_2_bits_control
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
`endif // RANDOMIZE_REG_INIT
  reg  in_data_R_0_control; // @[BasicBlock.scala 224:46]
  reg  in_data_valid_R_0; // @[BasicBlock.scala 225:52]
  reg  output_valid_R_0; // @[BasicBlock.scala 228:49]
  reg  output_valid_R_1; // @[BasicBlock.scala 228:49]
  reg  output_valid_R_2; // @[BasicBlock.scala 228:49]
  reg  output_fire_R_0; // @[BasicBlock.scala 229:48]
  reg  output_fire_R_1; // @[BasicBlock.scala 229:48]
  reg  output_fire_R_2; // @[BasicBlock.scala 229:48]
  wire  _T_7 = io_predicateIn_0_ready & io_predicateIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_5 = _T_7 | in_data_valid_R_0; // @[BasicBlock.scala 234:36]
  wire  _T_8 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_6 = _T_8 | output_fire_R_0; // @[BasicBlock.scala 246:28]
  wire  _T_9 = io_Out_1_ready & io_Out_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_8 = _T_9 | output_fire_R_1; // @[BasicBlock.scala 246:28]
  wire  _T_10 = io_Out_2_ready & io_Out_2_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_10 = _T_10 | output_fire_R_2; // @[BasicBlock.scala 246:28]
  wire  out_fire_mask_0 = output_fire_R_0 | _T_8; // @[BasicBlock.scala 258:85]
  wire  out_fire_mask_1 = output_fire_R_1 | _T_9; // @[BasicBlock.scala 258:85]
  wire  out_fire_mask_2 = output_fire_R_2 | _T_10; // @[BasicBlock.scala 258:85]
  reg  state; // @[BasicBlock.scala 289:22]
  wire  _T_21 = ~state; // @[Conditional.scala 37:30]
  wire  _T_25 = _T_8 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _T_26 = _T_9 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _T_27 = _T_10 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _GEN_12 = _GEN_5 | output_valid_R_0; // @[BasicBlock.scala 301:9]
  wire  _GEN_13 = _GEN_5 | output_valid_R_1; // @[BasicBlock.scala 301:9]
  wire  _GEN_14 = _GEN_5 | output_valid_R_2; // @[BasicBlock.scala 301:9]
  wire  _GEN_18 = _GEN_5 | state; // @[BasicBlock.scala 301:9]
  wire  _T_29 = out_fire_mask_0 & out_fire_mask_1; // @[BasicBlock.scala 317:35]
  wire  _T_30 = _T_29 & out_fire_mask_2; // @[BasicBlock.scala 317:35]
  assign io_predicateIn_0_ready = ~in_data_valid_R_0; // @[BasicBlock.scala 233:29]
  assign io_Out_0_valid = _T_21 ? _GEN_12 : output_valid_R_0; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
  assign io_Out_1_valid = _T_21 ? _GEN_13 : output_valid_R_1; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
  assign io_Out_1_bits_control = _T_7 ? io_predicateIn_0_bits_control : in_data_R_0_control; // @[BasicBlock.scala 279:22]
  assign io_Out_2_valid = _T_21 ? _GEN_14 : output_valid_R_2; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
  assign io_Out_2_bits_control = _T_7 ? io_predicateIn_0_bits_control : in_data_R_0_control; // @[BasicBlock.scala 279:22]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  in_data_R_0_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  output_valid_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  output_valid_R_1 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  output_valid_R_2 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  output_fire_R_0 = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  output_fire_R_1 = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  output_fire_R_2 = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  state = _RAND_8[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      in_data_R_0_control <= 1'h0;
    end else if (_T_21) begin
      if (_T_7) begin
        in_data_R_0_control <= io_predicateIn_0_bits_control;
      end
    end else if (state) begin
      if (_T_30) begin
        in_data_R_0_control <= 1'h0;
      end else if (_T_7) begin
        in_data_R_0_control <= io_predicateIn_0_bits_control;
      end
    end else if (_T_7) begin
      in_data_R_0_control <= io_predicateIn_0_bits_control;
    end
    if (reset) begin
      in_data_valid_R_0 <= 1'h0;
    end else if (_T_21) begin
      in_data_valid_R_0 <= _GEN_5;
    end else if (state) begin
      if (_T_30) begin
        in_data_valid_R_0 <= 1'h0;
      end else begin
        in_data_valid_R_0 <= _GEN_5;
      end
    end else begin
      in_data_valid_R_0 <= _GEN_5;
    end
    if (reset) begin
      output_valid_R_0 <= 1'h0;
    end else if (_T_21) begin
      if (_GEN_5) begin
        output_valid_R_0 <= _T_25;
      end else if (_T_8) begin
        output_valid_R_0 <= 1'h0;
      end
    end else if (_T_8) begin
      output_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      output_valid_R_1 <= 1'h0;
    end else if (_T_21) begin
      if (_GEN_5) begin
        output_valid_R_1 <= _T_26;
      end else if (_T_9) begin
        output_valid_R_1 <= 1'h0;
      end
    end else if (_T_9) begin
      output_valid_R_1 <= 1'h0;
    end
    if (reset) begin
      output_valid_R_2 <= 1'h0;
    end else if (_T_21) begin
      if (_GEN_5) begin
        output_valid_R_2 <= _T_27;
      end else if (_T_10) begin
        output_valid_R_2 <= 1'h0;
      end
    end else if (_T_10) begin
      output_valid_R_2 <= 1'h0;
    end
    if (reset) begin
      output_fire_R_0 <= 1'h0;
    end else if (_T_21) begin
      output_fire_R_0 <= _GEN_6;
    end else if (state) begin
      if (_T_30) begin
        output_fire_R_0 <= 1'h0;
      end else begin
        output_fire_R_0 <= _GEN_6;
      end
    end else begin
      output_fire_R_0 <= _GEN_6;
    end
    if (reset) begin
      output_fire_R_1 <= 1'h0;
    end else if (_T_21) begin
      output_fire_R_1 <= _GEN_8;
    end else if (state) begin
      if (_T_30) begin
        output_fire_R_1 <= 1'h0;
      end else begin
        output_fire_R_1 <= _GEN_8;
      end
    end else begin
      output_fire_R_1 <= _GEN_8;
    end
    if (reset) begin
      output_fire_R_2 <= 1'h0;
    end else if (_T_21) begin
      output_fire_R_2 <= _GEN_10;
    end else if (state) begin
      if (_T_30) begin
        output_fire_R_2 <= 1'h0;
      end else begin
        output_fire_R_2 <= _GEN_10;
      end
    end else begin
      output_fire_R_2 <= _GEN_10;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_21) begin
      state <= _GEN_18;
    end else if (state) begin
      if (_T_30) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module BasicBlockNoMaskFastNode_1(
  input   clock,
  input   reset,
  output  io_predicateIn_0_ready,
  input   io_predicateIn_0_valid,
  input   io_predicateIn_0_bits_control,
  input   io_Out_0_ready,
  output  io_Out_0_valid,
  input   io_Out_1_ready,
  output  io_Out_1_valid,
  output  io_Out_1_bits_control
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
`endif // RANDOMIZE_REG_INIT
  reg  in_data_R_0_control; // @[BasicBlock.scala 224:46]
  reg  in_data_valid_R_0; // @[BasicBlock.scala 225:52]
  reg  output_valid_R_0; // @[BasicBlock.scala 228:49]
  reg  output_valid_R_1; // @[BasicBlock.scala 228:49]
  reg  output_fire_R_0; // @[BasicBlock.scala 229:48]
  reg  output_fire_R_1; // @[BasicBlock.scala 229:48]
  wire  _T_7 = io_predicateIn_0_ready & io_predicateIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_5 = _T_7 | in_data_valid_R_0; // @[BasicBlock.scala 234:36]
  wire  _T_8 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_6 = _T_8 | output_fire_R_0; // @[BasicBlock.scala 246:28]
  wire  _T_9 = io_Out_1_ready & io_Out_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_8 = _T_9 | output_fire_R_1; // @[BasicBlock.scala 246:28]
  wire  out_fire_mask_0 = output_fire_R_0 | _T_8; // @[BasicBlock.scala 258:85]
  wire  out_fire_mask_1 = output_fire_R_1 | _T_9; // @[BasicBlock.scala 258:85]
  reg  state; // @[BasicBlock.scala 289:22]
  wire  _T_18 = ~state; // @[Conditional.scala 37:30]
  wire  _T_21 = _T_8 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _T_22 = _T_9 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _GEN_10 = _GEN_5 | output_valid_R_0; // @[BasicBlock.scala 301:9]
  wire  _GEN_11 = _GEN_5 | output_valid_R_1; // @[BasicBlock.scala 301:9]
  wire  _GEN_14 = _GEN_5 | state; // @[BasicBlock.scala 301:9]
  wire  _T_24 = out_fire_mask_0 & out_fire_mask_1; // @[BasicBlock.scala 317:35]
  assign io_predicateIn_0_ready = ~in_data_valid_R_0; // @[BasicBlock.scala 233:29]
  assign io_Out_0_valid = _T_18 ? _GEN_10 : output_valid_R_0; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
  assign io_Out_1_valid = _T_18 ? _GEN_11 : output_valid_R_1; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
  assign io_Out_1_bits_control = _T_7 ? io_predicateIn_0_bits_control : in_data_R_0_control; // @[BasicBlock.scala 279:22]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  in_data_R_0_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  output_valid_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  output_valid_R_1 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  output_fire_R_0 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  output_fire_R_1 = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  state = _RAND_6[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      in_data_R_0_control <= 1'h0;
    end else if (_T_18) begin
      if (_T_7) begin
        in_data_R_0_control <= io_predicateIn_0_bits_control;
      end
    end else if (state) begin
      if (_T_24) begin
        in_data_R_0_control <= 1'h0;
      end else if (_T_7) begin
        in_data_R_0_control <= io_predicateIn_0_bits_control;
      end
    end else if (_T_7) begin
      in_data_R_0_control <= io_predicateIn_0_bits_control;
    end
    if (reset) begin
      in_data_valid_R_0 <= 1'h0;
    end else if (_T_18) begin
      in_data_valid_R_0 <= _GEN_5;
    end else if (state) begin
      if (_T_24) begin
        in_data_valid_R_0 <= 1'h0;
      end else begin
        in_data_valid_R_0 <= _GEN_5;
      end
    end else begin
      in_data_valid_R_0 <= _GEN_5;
    end
    if (reset) begin
      output_valid_R_0 <= 1'h0;
    end else if (_T_18) begin
      if (_GEN_5) begin
        output_valid_R_0 <= _T_21;
      end else if (_T_8) begin
        output_valid_R_0 <= 1'h0;
      end
    end else if (_T_8) begin
      output_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      output_valid_R_1 <= 1'h0;
    end else if (_T_18) begin
      if (_GEN_5) begin
        output_valid_R_1 <= _T_22;
      end else if (_T_9) begin
        output_valid_R_1 <= 1'h0;
      end
    end else if (_T_9) begin
      output_valid_R_1 <= 1'h0;
    end
    if (reset) begin
      output_fire_R_0 <= 1'h0;
    end else if (_T_18) begin
      output_fire_R_0 <= _GEN_6;
    end else if (state) begin
      if (_T_24) begin
        output_fire_R_0 <= 1'h0;
      end else begin
        output_fire_R_0 <= _GEN_6;
      end
    end else begin
      output_fire_R_0 <= _GEN_6;
    end
    if (reset) begin
      output_fire_R_1 <= 1'h0;
    end else if (_T_18) begin
      output_fire_R_1 <= _GEN_8;
    end else if (state) begin
      if (_T_24) begin
        output_fire_R_1 <= 1'h0;
      end else begin
        output_fire_R_1 <= _GEN_8;
      end
    end else begin
      output_fire_R_1 <= _GEN_8;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_18) begin
      state <= _GEN_14;
    end else if (state) begin
      if (_T_24) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module BasicBlockNoMaskFastNode_2(
  input   clock,
  input   reset,
  output  io_predicateIn_0_ready,
  input   io_predicateIn_0_valid,
  input   io_Out_0_ready,
  output  io_Out_0_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
`endif // RANDOMIZE_REG_INIT
  reg  in_data_valid_R_0; // @[BasicBlock.scala 225:52]
  reg  output_valid_R_0; // @[BasicBlock.scala 228:49]
  reg  output_fire_R_0; // @[BasicBlock.scala 229:48]
  wire  _T_7 = io_predicateIn_0_ready & io_predicateIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_5 = _T_7 | in_data_valid_R_0; // @[BasicBlock.scala 234:36]
  wire  _T_8 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_6 = _T_8 | output_fire_R_0; // @[BasicBlock.scala 246:28]
  wire  out_fire_mask_0 = output_fire_R_0 | _T_8; // @[BasicBlock.scala 258:85]
  reg  state; // @[BasicBlock.scala 289:22]
  wire  _T_15 = ~state; // @[Conditional.scala 37:30]
  wire  _T_17 = _T_8 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _GEN_8 = _GEN_5 | output_valid_R_0; // @[BasicBlock.scala 301:9]
  wire  _GEN_10 = _GEN_5 | state; // @[BasicBlock.scala 301:9]
  assign io_predicateIn_0_ready = ~in_data_valid_R_0; // @[BasicBlock.scala 233:29]
  assign io_Out_0_valid = _T_15 ? _GEN_8 : output_valid_R_0; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  output_valid_R_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  output_fire_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  state = _RAND_3[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      in_data_valid_R_0 <= 1'h0;
    end else if (_T_15) begin
      in_data_valid_R_0 <= _GEN_5;
    end else if (state) begin
      if (out_fire_mask_0) begin
        in_data_valid_R_0 <= 1'h0;
      end else begin
        in_data_valid_R_0 <= _GEN_5;
      end
    end else begin
      in_data_valid_R_0 <= _GEN_5;
    end
    if (reset) begin
      output_valid_R_0 <= 1'h0;
    end else if (_T_15) begin
      if (_GEN_5) begin
        output_valid_R_0 <= _T_17;
      end else if (_T_8) begin
        output_valid_R_0 <= 1'h0;
      end
    end else if (_T_8) begin
      output_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      output_fire_R_0 <= 1'h0;
    end else if (_T_15) begin
      output_fire_R_0 <= _GEN_6;
    end else if (state) begin
      if (out_fire_mask_0) begin
        output_fire_R_0 <= 1'h0;
      end else begin
        output_fire_R_0 <= _GEN_6;
      end
    end else begin
      output_fire_R_0 <= _GEN_6;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_15) begin
      state <= _GEN_10;
    end else if (state) begin
      if (out_fire_mask_0) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module BasicBlockNoMaskFastNode_3(
  input   clock,
  input   reset,
  output  io_predicateIn_0_ready,
  input   io_predicateIn_0_valid,
  output  io_predicateIn_1_ready,
  input   io_predicateIn_1_valid,
  input   io_Out_0_ready,
  output  io_Out_0_valid,
  input   io_Out_1_ready,
  output  io_Out_1_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
`endif // RANDOMIZE_REG_INIT
  reg  in_data_valid_R_0; // @[BasicBlock.scala 225:52]
  reg  in_data_valid_R_1; // @[BasicBlock.scala 225:52]
  reg  output_valid_R_0; // @[BasicBlock.scala 228:49]
  reg  output_valid_R_1; // @[BasicBlock.scala 228:49]
  reg  output_fire_R_0; // @[BasicBlock.scala 229:48]
  reg  output_fire_R_1; // @[BasicBlock.scala 229:48]
  wire  _T_8 = io_predicateIn_0_ready & io_predicateIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_5 = _T_8 | in_data_valid_R_0; // @[BasicBlock.scala 234:36]
  wire  _T_10 = io_predicateIn_1_ready & io_predicateIn_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_9 = _T_10 | in_data_valid_R_1; // @[BasicBlock.scala 234:36]
  wire  _T_13 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_10 = _T_13 | output_fire_R_0; // @[BasicBlock.scala 246:28]
  wire  _T_14 = io_Out_1_ready & io_Out_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_12 = _T_14 | output_fire_R_1; // @[BasicBlock.scala 246:28]
  wire  out_fire_mask_0 = output_fire_R_0 | _T_13; // @[BasicBlock.scala 258:85]
  wire  out_fire_mask_1 = output_fire_R_1 | _T_14; // @[BasicBlock.scala 258:85]
  reg  state; // @[BasicBlock.scala 289:22]
  wire  _T_30 = ~state; // @[Conditional.scala 37:30]
  wire  _T_31 = _GEN_5 & _GEN_9; // @[BasicBlock.scala 296:41]
  wire  _T_34 = _T_13 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _T_35 = _T_14 ^ 1'h1; // @[BasicBlock.scala 306:81]
  wire  _GEN_14 = _T_31 | output_valid_R_0; // @[BasicBlock.scala 301:9]
  wire  _GEN_15 = _T_31 | output_valid_R_1; // @[BasicBlock.scala 301:9]
  wire  _GEN_18 = _T_31 | state; // @[BasicBlock.scala 301:9]
  wire  _T_37 = out_fire_mask_0 & out_fire_mask_1; // @[BasicBlock.scala 317:35]
  assign io_predicateIn_0_ready = ~in_data_valid_R_0; // @[BasicBlock.scala 233:29]
  assign io_predicateIn_1_ready = ~in_data_valid_R_1; // @[BasicBlock.scala 233:29]
  assign io_Out_0_valid = _T_30 ? _GEN_14 : output_valid_R_0; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
  assign io_Out_1_valid = _T_30 ? _GEN_15 : output_valid_R_1; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  in_data_valid_R_1 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  output_valid_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  output_valid_R_1 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  output_fire_R_0 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  output_fire_R_1 = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  state = _RAND_6[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      in_data_valid_R_0 <= 1'h0;
    end else if (_T_30) begin
      in_data_valid_R_0 <= _GEN_5;
    end else if (state) begin
      if (_T_37) begin
        in_data_valid_R_0 <= 1'h0;
      end else begin
        in_data_valid_R_0 <= _GEN_5;
      end
    end else begin
      in_data_valid_R_0 <= _GEN_5;
    end
    if (reset) begin
      in_data_valid_R_1 <= 1'h0;
    end else if (_T_30) begin
      in_data_valid_R_1 <= _GEN_9;
    end else if (state) begin
      if (_T_37) begin
        in_data_valid_R_1 <= 1'h0;
      end else begin
        in_data_valid_R_1 <= _GEN_9;
      end
    end else begin
      in_data_valid_R_1 <= _GEN_9;
    end
    if (reset) begin
      output_valid_R_0 <= 1'h0;
    end else if (_T_30) begin
      if (_T_31) begin
        output_valid_R_0 <= _T_34;
      end else if (_T_13) begin
        output_valid_R_0 <= 1'h0;
      end
    end else if (_T_13) begin
      output_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      output_valid_R_1 <= 1'h0;
    end else if (_T_30) begin
      if (_T_31) begin
        output_valid_R_1 <= _T_35;
      end else if (_T_14) begin
        output_valid_R_1 <= 1'h0;
      end
    end else if (_T_14) begin
      output_valid_R_1 <= 1'h0;
    end
    if (reset) begin
      output_fire_R_0 <= 1'h0;
    end else if (_T_30) begin
      output_fire_R_0 <= _GEN_10;
    end else if (state) begin
      if (_T_37) begin
        output_fire_R_0 <= 1'h0;
      end else begin
        output_fire_R_0 <= _GEN_10;
      end
    end else begin
      output_fire_R_0 <= _GEN_10;
    end
    if (reset) begin
      output_fire_R_1 <= 1'h0;
    end else if (_T_30) begin
      output_fire_R_1 <= _GEN_12;
    end else if (state) begin
      if (_T_37) begin
        output_fire_R_1 <= 1'h0;
      end else begin
        output_fire_R_1 <= _GEN_12;
      end
    end else begin
      output_fire_R_1 <= _GEN_12;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_30) begin
      state <= _GEN_18;
    end else if (state) begin
      if (_T_37) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module BasicBlockNode(
  input        clock,
  input        reset,
  input        io_MaskBB_0_ready,
  output       io_MaskBB_0_valid,
  output [1:0] io_MaskBB_0_bits,
  input        io_Out_0_ready,
  output       io_Out_0_valid,
  input        io_Out_1_ready,
  output       io_Out_1_valid,
  input        io_Out_2_ready,
  output       io_Out_2_valid,
  output       io_Out_2_bits_control,
  input        io_Out_3_ready,
  output       io_Out_3_valid,
  input        io_Out_4_ready,
  output       io_Out_4_valid,
  output       io_Out_4_bits_control,
  input        io_Out_5_ready,
  output       io_Out_5_valid,
  output       io_Out_5_bits_control,
  input        io_Out_6_ready,
  output       io_Out_6_valid,
  input        io_Out_7_ready,
  output       io_Out_7_valid,
  output       io_Out_7_bits_control,
  input        io_Out_8_ready,
  output       io_Out_8_valid,
  output       io_Out_8_bits_control,
  input        io_Out_9_ready,
  output       io_Out_9_valid,
  output       io_Out_9_bits_control,
  input        io_Out_10_ready,
  output       io_Out_10_valid,
  output       io_Out_10_bits_control,
  input        io_Out_11_ready,
  output       io_Out_11_valid,
  output       io_Out_11_bits_control,
  input        io_Out_12_ready,
  output       io_Out_12_valid,
  output       io_Out_12_bits_control,
  output       io_predicateIn_0_ready,
  input        io_predicateIn_0_valid,
  input        io_predicateIn_0_bits_control,
  output       io_predicateIn_1_ready,
  input        io_predicateIn_1_valid,
  input        io_predicateIn_1_bits_control
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_21;
  reg [31:0] _RAND_22;
  reg [31:0] _RAND_23;
  reg [31:0] _RAND_24;
  reg [31:0] _RAND_25;
  reg [31:0] _RAND_26;
  reg [31:0] _RAND_27;
  reg [31:0] _RAND_28;
  reg [31:0] _RAND_29;
  reg [31:0] _RAND_30;
  reg [31:0] _RAND_31;
  reg [31:0] _RAND_32;
  reg [31:0] _RAND_33;
`endif // RANDOMIZE_REG_INIT
  reg  out_ready_R_0; // @[HandShaking.scala 780:28]
  reg  out_ready_R_1; // @[HandShaking.scala 780:28]
  reg  out_ready_R_2; // @[HandShaking.scala 780:28]
  reg  out_ready_R_3; // @[HandShaking.scala 780:28]
  reg  out_ready_R_4; // @[HandShaking.scala 780:28]
  reg  out_ready_R_5; // @[HandShaking.scala 780:28]
  reg  out_ready_R_6; // @[HandShaking.scala 780:28]
  reg  out_ready_R_7; // @[HandShaking.scala 780:28]
  reg  out_ready_R_8; // @[HandShaking.scala 780:28]
  reg  out_ready_R_9; // @[HandShaking.scala 780:28]
  reg  out_ready_R_10; // @[HandShaking.scala 780:28]
  reg  out_ready_R_11; // @[HandShaking.scala 780:28]
  reg  out_ready_R_12; // @[HandShaking.scala 780:28]
  reg  out_valid_R_0; // @[HandShaking.scala 781:28]
  reg  out_valid_R_1; // @[HandShaking.scala 781:28]
  reg  out_valid_R_2; // @[HandShaking.scala 781:28]
  reg  out_valid_R_3; // @[HandShaking.scala 781:28]
  reg  out_valid_R_4; // @[HandShaking.scala 781:28]
  reg  out_valid_R_5; // @[HandShaking.scala 781:28]
  reg  out_valid_R_6; // @[HandShaking.scala 781:28]
  reg  out_valid_R_7; // @[HandShaking.scala 781:28]
  reg  out_valid_R_8; // @[HandShaking.scala 781:28]
  reg  out_valid_R_9; // @[HandShaking.scala 781:28]
  reg  out_valid_R_10; // @[HandShaking.scala 781:28]
  reg  out_valid_R_11; // @[HandShaking.scala 781:28]
  reg  out_valid_R_12; // @[HandShaking.scala 781:28]
  reg  mask_valid_R_0; // @[HandShaking.scala 785:46]
  wire  _T_2 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_1 = _T_2 ? 1'h0 : out_valid_R_0; // @[HandShaking.scala 794:29]
  wire  _T_3 = io_Out_1_ready & io_Out_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_3 = _T_3 ? 1'h0 : out_valid_R_1; // @[HandShaking.scala 794:29]
  wire  _T_4 = io_Out_2_ready & io_Out_2_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_5 = _T_4 ? 1'h0 : out_valid_R_2; // @[HandShaking.scala 794:29]
  wire  _T_5 = io_Out_3_ready & io_Out_3_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_7 = _T_5 ? 1'h0 : out_valid_R_3; // @[HandShaking.scala 794:29]
  wire  _T_6 = io_Out_4_ready & io_Out_4_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_9 = _T_6 ? 1'h0 : out_valid_R_4; // @[HandShaking.scala 794:29]
  wire  _T_7 = io_Out_5_ready & io_Out_5_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_7 ? 1'h0 : out_valid_R_5; // @[HandShaking.scala 794:29]
  wire  _T_8 = io_Out_6_ready & io_Out_6_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_13 = _T_8 ? 1'h0 : out_valid_R_6; // @[HandShaking.scala 794:29]
  wire  _T_9 = io_Out_7_ready & io_Out_7_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_9 ? 1'h0 : out_valid_R_7; // @[HandShaking.scala 794:29]
  wire  _T_10 = io_Out_8_ready & io_Out_8_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_17 = _T_10 ? 1'h0 : out_valid_R_8; // @[HandShaking.scala 794:29]
  wire  _T_11 = io_Out_9_ready & io_Out_9_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_19 = _T_11 ? 1'h0 : out_valid_R_9; // @[HandShaking.scala 794:29]
  wire  _T_12 = io_Out_10_ready & io_Out_10_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_21 = _T_12 ? 1'h0 : out_valid_R_10; // @[HandShaking.scala 794:29]
  wire  _T_13 = io_Out_11_ready & io_Out_11_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_23 = _T_13 ? 1'h0 : out_valid_R_11; // @[HandShaking.scala 794:29]
  wire  _T_14 = io_Out_12_ready & io_Out_12_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_25 = _T_14 ? 1'h0 : out_valid_R_12; // @[HandShaking.scala 794:29]
  wire  _T_15 = io_MaskBB_0_ready & io_MaskBB_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_27 = _T_15 ? 1'h0 : mask_valid_R_0; // @[HandShaking.scala 805:32]
  reg  predicate_in_R_0_control; // @[BasicBlock.scala 65:51]
  reg  predicate_in_R_1_control; // @[BasicBlock.scala 65:51]
  reg  predicate_control_R_0; // @[BasicBlock.scala 66:36]
  reg  predicate_control_R_1; // @[BasicBlock.scala 66:36]
  reg  predicate_valid_R_0; // @[BasicBlock.scala 67:54]
  reg  predicate_valid_R_1; // @[BasicBlock.scala 67:54]
  reg  state; // @[BasicBlock.scala 70:22]
  wire  _T_23 = io_predicateIn_0_ready & io_predicateIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_24 = io_predicateIn_1_ready & io_predicateIn_1_valid; // @[Decoupled.scala 40:37]
  wire  _T_25 = _T_23 | predicate_valid_R_0; // @[BasicBlock.scala 80:91]
  wire  _T_26 = _T_24 | predicate_valid_R_1; // @[BasicBlock.scala 80:91]
  wire  start = _T_25 & _T_26; // @[BasicBlock.scala 80:107]
  wire  _T_32 = ~state; // @[Conditional.scala 37:30]
  wire  _GEN_40 = start | _GEN_1; // @[BasicBlock.scala 115:19]
  wire  _GEN_41 = start | _GEN_3; // @[BasicBlock.scala 115:19]
  wire  _GEN_42 = start | _GEN_5; // @[BasicBlock.scala 115:19]
  wire  _GEN_43 = start | _GEN_7; // @[BasicBlock.scala 115:19]
  wire  _GEN_44 = start | _GEN_9; // @[BasicBlock.scala 115:19]
  wire  _GEN_45 = start | _GEN_11; // @[BasicBlock.scala 115:19]
  wire  _GEN_46 = start | _GEN_13; // @[BasicBlock.scala 115:19]
  wire  _GEN_47 = start | _GEN_15; // @[BasicBlock.scala 115:19]
  wire  _GEN_48 = start | _GEN_17; // @[BasicBlock.scala 115:19]
  wire  _GEN_49 = start | _GEN_19; // @[BasicBlock.scala 115:19]
  wire  _GEN_50 = start | _GEN_21; // @[BasicBlock.scala 115:19]
  wire  _GEN_51 = start | _GEN_23; // @[BasicBlock.scala 115:19]
  wire  _GEN_52 = start | _GEN_25; // @[BasicBlock.scala 115:19]
  wire  _GEN_53 = start | _GEN_27; // @[BasicBlock.scala 115:19]
  wire  _GEN_54 = start | state; // @[BasicBlock.scala 115:19]
  wire [5:0] _T_39 = {out_ready_R_5,out_ready_R_4,out_ready_R_3,out_ready_R_2,out_ready_R_1,out_ready_R_0}; // @[HandShaking.scala 834:17]
  wire [12:0] _T_46 = {out_ready_R_12,out_ready_R_11,out_ready_R_10,out_ready_R_9,out_ready_R_8,out_ready_R_7,out_ready_R_6,_T_39}; // @[HandShaking.scala 834:17]
  wire  _T_47 = &_T_46; // @[HandShaking.scala 834:24]
  assign io_MaskBB_0_valid = mask_valid_R_0; // @[HandShaking.scala 804:24]
  assign io_MaskBB_0_bits = {predicate_control_R_1,predicate_control_R_0}; // @[BasicBlock.scala 105:23]
  assign io_Out_0_valid = out_valid_R_0; // @[HandShaking.scala 793:21]
  assign io_Out_1_valid = out_valid_R_1; // @[HandShaking.scala 793:21]
  assign io_Out_2_valid = out_valid_R_2; // @[HandShaking.scala 793:21]
  assign io_Out_2_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_3_valid = out_valid_R_3; // @[HandShaking.scala 793:21]
  assign io_Out_4_valid = out_valid_R_4; // @[HandShaking.scala 793:21]
  assign io_Out_4_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_5_valid = out_valid_R_5; // @[HandShaking.scala 793:21]
  assign io_Out_5_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_6_valid = out_valid_R_6; // @[HandShaking.scala 793:21]
  assign io_Out_7_valid = out_valid_R_7; // @[HandShaking.scala 793:21]
  assign io_Out_7_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_8_valid = out_valid_R_8; // @[HandShaking.scala 793:21]
  assign io_Out_8_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_9_valid = out_valid_R_9; // @[HandShaking.scala 793:21]
  assign io_Out_9_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_10_valid = out_valid_R_10; // @[HandShaking.scala 793:21]
  assign io_Out_10_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_11_valid = out_valid_R_11; // @[HandShaking.scala 793:21]
  assign io_Out_11_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_Out_12_valid = out_valid_R_12; // @[HandShaking.scala 793:21]
  assign io_Out_12_bits_control = predicate_in_R_0_control | predicate_in_R_1_control; // @[BasicBlock.scala 98:28]
  assign io_predicateIn_0_ready = ~predicate_valid_R_0; // @[BasicBlock.scala 88:29]
  assign io_predicateIn_1_ready = ~predicate_valid_R_1; // @[BasicBlock.scala 88:29]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  out_ready_R_1 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_2 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_ready_R_3 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  out_ready_R_4 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  out_ready_R_5 = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  out_ready_R_6 = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  out_ready_R_7 = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  out_ready_R_8 = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  out_ready_R_9 = _RAND_9[0:0];
  _RAND_10 = {1{`RANDOM}};
  out_ready_R_10 = _RAND_10[0:0];
  _RAND_11 = {1{`RANDOM}};
  out_ready_R_11 = _RAND_11[0:0];
  _RAND_12 = {1{`RANDOM}};
  out_ready_R_12 = _RAND_12[0:0];
  _RAND_13 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_13[0:0];
  _RAND_14 = {1{`RANDOM}};
  out_valid_R_1 = _RAND_14[0:0];
  _RAND_15 = {1{`RANDOM}};
  out_valid_R_2 = _RAND_15[0:0];
  _RAND_16 = {1{`RANDOM}};
  out_valid_R_3 = _RAND_16[0:0];
  _RAND_17 = {1{`RANDOM}};
  out_valid_R_4 = _RAND_17[0:0];
  _RAND_18 = {1{`RANDOM}};
  out_valid_R_5 = _RAND_18[0:0];
  _RAND_19 = {1{`RANDOM}};
  out_valid_R_6 = _RAND_19[0:0];
  _RAND_20 = {1{`RANDOM}};
  out_valid_R_7 = _RAND_20[0:0];
  _RAND_21 = {1{`RANDOM}};
  out_valid_R_8 = _RAND_21[0:0];
  _RAND_22 = {1{`RANDOM}};
  out_valid_R_9 = _RAND_22[0:0];
  _RAND_23 = {1{`RANDOM}};
  out_valid_R_10 = _RAND_23[0:0];
  _RAND_24 = {1{`RANDOM}};
  out_valid_R_11 = _RAND_24[0:0];
  _RAND_25 = {1{`RANDOM}};
  out_valid_R_12 = _RAND_25[0:0];
  _RAND_26 = {1{`RANDOM}};
  mask_valid_R_0 = _RAND_26[0:0];
  _RAND_27 = {1{`RANDOM}};
  predicate_in_R_0_control = _RAND_27[0:0];
  _RAND_28 = {1{`RANDOM}};
  predicate_in_R_1_control = _RAND_28[0:0];
  _RAND_29 = {1{`RANDOM}};
  predicate_control_R_0 = _RAND_29[0:0];
  _RAND_30 = {1{`RANDOM}};
  predicate_control_R_1 = _RAND_30[0:0];
  _RAND_31 = {1{`RANDOM}};
  predicate_valid_R_0 = _RAND_31[0:0];
  _RAND_32 = {1{`RANDOM}};
  predicate_valid_R_1 = _RAND_32[0:0];
  _RAND_33 = {1{`RANDOM}};
  state = _RAND_33[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_32) begin
      if (_T_2) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_2) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_2) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_ready_R_1 <= 1'h0;
    end else if (_T_32) begin
      if (_T_3) begin
        out_ready_R_1 <= io_Out_1_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_1 <= 1'h0;
      end else if (_T_3) begin
        out_ready_R_1 <= io_Out_1_ready;
      end
    end else if (_T_3) begin
      out_ready_R_1 <= io_Out_1_ready;
    end
    if (reset) begin
      out_ready_R_2 <= 1'h0;
    end else if (_T_32) begin
      if (_T_4) begin
        out_ready_R_2 <= io_Out_2_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_2 <= 1'h0;
      end else if (_T_4) begin
        out_ready_R_2 <= io_Out_2_ready;
      end
    end else if (_T_4) begin
      out_ready_R_2 <= io_Out_2_ready;
    end
    if (reset) begin
      out_ready_R_3 <= 1'h0;
    end else if (_T_32) begin
      if (_T_5) begin
        out_ready_R_3 <= io_Out_3_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_3 <= 1'h0;
      end else if (_T_5) begin
        out_ready_R_3 <= io_Out_3_ready;
      end
    end else if (_T_5) begin
      out_ready_R_3 <= io_Out_3_ready;
    end
    if (reset) begin
      out_ready_R_4 <= 1'h0;
    end else if (_T_32) begin
      if (_T_6) begin
        out_ready_R_4 <= io_Out_4_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_4 <= 1'h0;
      end else if (_T_6) begin
        out_ready_R_4 <= io_Out_4_ready;
      end
    end else if (_T_6) begin
      out_ready_R_4 <= io_Out_4_ready;
    end
    if (reset) begin
      out_ready_R_5 <= 1'h0;
    end else if (_T_32) begin
      if (_T_7) begin
        out_ready_R_5 <= io_Out_5_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_5 <= 1'h0;
      end else if (_T_7) begin
        out_ready_R_5 <= io_Out_5_ready;
      end
    end else if (_T_7) begin
      out_ready_R_5 <= io_Out_5_ready;
    end
    if (reset) begin
      out_ready_R_6 <= 1'h0;
    end else if (_T_32) begin
      if (_T_8) begin
        out_ready_R_6 <= io_Out_6_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_6 <= 1'h0;
      end else if (_T_8) begin
        out_ready_R_6 <= io_Out_6_ready;
      end
    end else if (_T_8) begin
      out_ready_R_6 <= io_Out_6_ready;
    end
    if (reset) begin
      out_ready_R_7 <= 1'h0;
    end else if (_T_32) begin
      if (_T_9) begin
        out_ready_R_7 <= io_Out_7_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_7 <= 1'h0;
      end else if (_T_9) begin
        out_ready_R_7 <= io_Out_7_ready;
      end
    end else if (_T_9) begin
      out_ready_R_7 <= io_Out_7_ready;
    end
    if (reset) begin
      out_ready_R_8 <= 1'h0;
    end else if (_T_32) begin
      if (_T_10) begin
        out_ready_R_8 <= io_Out_8_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_8 <= 1'h0;
      end else if (_T_10) begin
        out_ready_R_8 <= io_Out_8_ready;
      end
    end else if (_T_10) begin
      out_ready_R_8 <= io_Out_8_ready;
    end
    if (reset) begin
      out_ready_R_9 <= 1'h0;
    end else if (_T_32) begin
      if (_T_11) begin
        out_ready_R_9 <= io_Out_9_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_9 <= 1'h0;
      end else if (_T_11) begin
        out_ready_R_9 <= io_Out_9_ready;
      end
    end else if (_T_11) begin
      out_ready_R_9 <= io_Out_9_ready;
    end
    if (reset) begin
      out_ready_R_10 <= 1'h0;
    end else if (_T_32) begin
      if (_T_12) begin
        out_ready_R_10 <= io_Out_10_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_10 <= 1'h0;
      end else if (_T_12) begin
        out_ready_R_10 <= io_Out_10_ready;
      end
    end else if (_T_12) begin
      out_ready_R_10 <= io_Out_10_ready;
    end
    if (reset) begin
      out_ready_R_11 <= 1'h0;
    end else if (_T_32) begin
      if (_T_13) begin
        out_ready_R_11 <= io_Out_11_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_11 <= 1'h0;
      end else if (_T_13) begin
        out_ready_R_11 <= io_Out_11_ready;
      end
    end else if (_T_13) begin
      out_ready_R_11 <= io_Out_11_ready;
    end
    if (reset) begin
      out_ready_R_12 <= 1'h0;
    end else if (_T_32) begin
      if (_T_14) begin
        out_ready_R_12 <= io_Out_12_ready;
      end
    end else if (state) begin
      if (_T_47) begin
        out_ready_R_12 <= 1'h0;
      end else if (_T_14) begin
        out_ready_R_12 <= io_Out_12_ready;
      end
    end else if (_T_14) begin
      out_ready_R_12 <= io_Out_12_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_0 <= _GEN_40;
    end else if (_T_2) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_1 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_1 <= _GEN_41;
    end else if (_T_3) begin
      out_valid_R_1 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_2 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_2 <= _GEN_42;
    end else if (_T_4) begin
      out_valid_R_2 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_3 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_3 <= _GEN_43;
    end else if (_T_5) begin
      out_valid_R_3 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_4 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_4 <= _GEN_44;
    end else if (_T_6) begin
      out_valid_R_4 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_5 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_5 <= _GEN_45;
    end else if (_T_7) begin
      out_valid_R_5 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_6 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_6 <= _GEN_46;
    end else if (_T_8) begin
      out_valid_R_6 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_7 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_7 <= _GEN_47;
    end else if (_T_9) begin
      out_valid_R_7 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_8 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_8 <= _GEN_48;
    end else if (_T_10) begin
      out_valid_R_8 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_9 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_9 <= _GEN_49;
    end else if (_T_11) begin
      out_valid_R_9 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_10 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_10 <= _GEN_50;
    end else if (_T_12) begin
      out_valid_R_10 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_11 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_11 <= _GEN_51;
    end else if (_T_13) begin
      out_valid_R_11 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_12 <= 1'h0;
    end else if (_T_32) begin
      out_valid_R_12 <= _GEN_52;
    end else if (_T_14) begin
      out_valid_R_12 <= 1'h0;
    end
    if (reset) begin
      mask_valid_R_0 <= 1'h0;
    end else if (_T_32) begin
      mask_valid_R_0 <= _GEN_53;
    end else if (_T_15) begin
      mask_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      predicate_in_R_0_control <= 1'h0;
    end else if (_T_23) begin
      predicate_in_R_0_control <= io_predicateIn_0_bits_control;
    end
    if (reset) begin
      predicate_in_R_1_control <= 1'h0;
    end else if (_T_24) begin
      predicate_in_R_1_control <= io_predicateIn_1_bits_control;
    end
    if (reset) begin
      predicate_control_R_0 <= 1'h0;
    end else if (_T_23) begin
      predicate_control_R_0 <= io_predicateIn_0_bits_control;
    end
    if (reset) begin
      predicate_control_R_1 <= 1'h0;
    end else if (_T_24) begin
      predicate_control_R_1 <= io_predicateIn_1_bits_control;
    end
    if (reset) begin
      predicate_valid_R_0 <= 1'h0;
    end else if (_T_32) begin
      predicate_valid_R_0 <= _T_25;
    end else if (state) begin
      if (_T_47) begin
        predicate_valid_R_0 <= 1'h0;
      end else begin
        predicate_valid_R_0 <= _T_25;
      end
    end else begin
      predicate_valid_R_0 <= _T_25;
    end
    if (reset) begin
      predicate_valid_R_1 <= 1'h0;
    end else if (_T_32) begin
      predicate_valid_R_1 <= _T_26;
    end else if (state) begin
      if (_T_47) begin
        predicate_valid_R_1 <= 1'h0;
      end else begin
        predicate_valid_R_1 <= _T_26;
      end
    end else begin
      predicate_valid_R_1 <= _T_26;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_32) begin
      state <= _GEN_54;
    end else if (state) begin
      if (_T_47) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module UALU(
  input  [63:0] io_in1,
  output [63:0] io_out
);
  wire  _T_31 = $signed(io_in1) > 64'sh0; // @[Alu.scala 213:38]
  assign io_out = {{63'd0}, _T_31}; // @[Alu.scala 235:10]
endmodule
module ComputeNode(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  output        io_LeftIO_ready,
  input         io_LeftIO_valid,
  input  [63:0] io_LeftIO_bits_data,
  output        io_RightIO_ready,
  input         io_RightIO_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [63:0] _RAND_8;
  reg [31:0] _RAND_9;
`endif // RANDOMIZE_REG_INIT
  wire [63:0] FU_io_in1; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_out; // @[ComputeNode.scala 61:18]
  reg  enable_R_control; // @[HandShaking.scala 181:31]
  reg  enable_valid_R; // @[HandShaking.scala 182:31]
  reg  out_ready_R_0; // @[HandShaking.scala 185:46]
  reg  out_valid_R_0; // @[HandShaking.scala 186:46]
  wire  _T_1 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_3 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] left_R_data; // @[ComputeNode.scala 53:23]
  reg  left_valid_R; // @[ComputeNode.scala 54:29]
  reg  right_valid_R; // @[ComputeNode.scala 58:30]
  reg  state; // @[ComputeNode.scala 64:22]
  reg [63:0] out_data_R; // @[ComputeNode.scala 89:27]
  wire  _T_12 = io_LeftIO_ready & io_LeftIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_12 | left_valid_R; // @[ComputeNode.scala 105:26]
  wire  _T_14 = io_RightIO_ready & io_RightIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_14 | right_valid_R; // @[ComputeNode.scala 111:27]
  wire  _T_32 = ~state; // @[Conditional.scala 37:30]
  wire  _T_33 = enable_valid_R & left_valid_R; // @[ComputeNode.scala 147:27]
  wire  _T_34 = _T_33 & right_valid_R; // @[ComputeNode.scala 147:43]
  reg [9:0] guard_index; // @[Counter.scala 29:33]
  wire [63:0] _GEN_20 = 10'h1 == guard_index ? 64'h1 : 64'h0; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_21 = 10'h2 == guard_index ? 64'h2 : _GEN_20; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_22 = 10'h3 == guard_index ? 64'h3 : _GEN_21; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_23 = 10'h4 == guard_index ? 64'h4 : _GEN_22; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_24 = 10'h5 == guard_index ? 64'h5 : _GEN_23; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_25 = 10'h6 == guard_index ? 64'h6 : _GEN_24; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_26 = 10'h7 == guard_index ? 64'h7 : _GEN_25; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_27 = 10'h8 == guard_index ? 64'h8 : _GEN_26; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_28 = 10'h9 == guard_index ? 64'h9 : _GEN_27; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_29 = 10'ha == guard_index ? 64'ha : _GEN_28; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_30 = 10'hb == guard_index ? 64'hb : _GEN_29; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_31 = 10'hc == guard_index ? 64'hc : _GEN_30; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_32 = 10'hd == guard_index ? 64'hd : _GEN_31; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_33 = 10'he == guard_index ? 64'he : _GEN_32; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_34 = 10'hf == guard_index ? 64'hf : _GEN_33; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_35 = 10'h10 == guard_index ? 64'h10 : _GEN_34; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_36 = 10'h11 == guard_index ? 64'h11 : _GEN_35; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_37 = 10'h12 == guard_index ? 64'h12 : _GEN_36; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_38 = 10'h13 == guard_index ? 64'h13 : _GEN_37; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_39 = 10'h14 == guard_index ? 64'h14 : _GEN_38; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_40 = 10'h15 == guard_index ? 64'h15 : _GEN_39; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_41 = 10'h16 == guard_index ? 64'h16 : _GEN_40; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_42 = 10'h17 == guard_index ? 64'h17 : _GEN_41; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_43 = 10'h18 == guard_index ? 64'h18 : _GEN_42; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_44 = 10'h19 == guard_index ? 64'h19 : _GEN_43; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_45 = 10'h1a == guard_index ? 64'h1a : _GEN_44; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_46 = 10'h1b == guard_index ? 64'h1b : _GEN_45; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_47 = 10'h1c == guard_index ? 64'h1c : _GEN_46; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_48 = 10'h1d == guard_index ? 64'h1d : _GEN_47; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_49 = 10'h1e == guard_index ? 64'h1e : _GEN_48; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_50 = 10'h1f == guard_index ? 64'h1f : _GEN_49; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_51 = 10'h20 == guard_index ? 64'h20 : _GEN_50; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_52 = 10'h21 == guard_index ? 64'h21 : _GEN_51; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_53 = 10'h22 == guard_index ? 64'h22 : _GEN_52; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_54 = 10'h23 == guard_index ? 64'h23 : _GEN_53; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_55 = 10'h24 == guard_index ? 64'h24 : _GEN_54; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_56 = 10'h25 == guard_index ? 64'h25 : _GEN_55; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_57 = 10'h26 == guard_index ? 64'h26 : _GEN_56; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_58 = 10'h27 == guard_index ? 64'h27 : _GEN_57; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_59 = 10'h28 == guard_index ? 64'h28 : _GEN_58; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_60 = 10'h29 == guard_index ? 64'h29 : _GEN_59; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_61 = 10'h2a == guard_index ? 64'h2a : _GEN_60; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_62 = 10'h2b == guard_index ? 64'h2b : _GEN_61; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_63 = 10'h2c == guard_index ? 64'h2c : _GEN_62; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_64 = 10'h2d == guard_index ? 64'h2d : _GEN_63; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_65 = 10'h2e == guard_index ? 64'h2e : _GEN_64; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_66 = 10'h2f == guard_index ? 64'h2f : _GEN_65; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_67 = 10'h30 == guard_index ? 64'h30 : _GEN_66; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_68 = 10'h31 == guard_index ? 64'h31 : _GEN_67; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_69 = 10'h32 == guard_index ? 64'h32 : _GEN_68; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_70 = 10'h33 == guard_index ? 64'h33 : _GEN_69; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_71 = 10'h34 == guard_index ? 64'h34 : _GEN_70; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_72 = 10'h35 == guard_index ? 64'h35 : _GEN_71; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_73 = 10'h36 == guard_index ? 64'h36 : _GEN_72; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_74 = 10'h37 == guard_index ? 64'h37 : _GEN_73; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_75 = 10'h38 == guard_index ? 64'h38 : _GEN_74; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_76 = 10'h39 == guard_index ? 64'h39 : _GEN_75; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_77 = 10'h3a == guard_index ? 64'h3a : _GEN_76; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_78 = 10'h3b == guard_index ? 64'h3b : _GEN_77; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_79 = 10'h3c == guard_index ? 64'h3c : _GEN_78; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_80 = 10'h3d == guard_index ? 64'h3d : _GEN_79; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_81 = 10'h3e == guard_index ? 64'h3e : _GEN_80; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_82 = 10'h3f == guard_index ? 64'h3f : _GEN_81; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_83 = 10'h40 == guard_index ? 64'h40 : _GEN_82; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_84 = 10'h41 == guard_index ? 64'h41 : _GEN_83; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_85 = 10'h42 == guard_index ? 64'h42 : _GEN_84; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_86 = 10'h43 == guard_index ? 64'h43 : _GEN_85; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_87 = 10'h44 == guard_index ? 64'h44 : _GEN_86; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_88 = 10'h45 == guard_index ? 64'h45 : _GEN_87; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_89 = 10'h46 == guard_index ? 64'h46 : _GEN_88; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_90 = 10'h47 == guard_index ? 64'h47 : _GEN_89; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_91 = 10'h48 == guard_index ? 64'h48 : _GEN_90; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_92 = 10'h49 == guard_index ? 64'h49 : _GEN_91; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_93 = 10'h4a == guard_index ? 64'h4a : _GEN_92; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_94 = 10'h4b == guard_index ? 64'h4b : _GEN_93; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_95 = 10'h4c == guard_index ? 64'h4c : _GEN_94; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_96 = 10'h4d == guard_index ? 64'h4d : _GEN_95; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_97 = 10'h4e == guard_index ? 64'h4e : _GEN_96; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_98 = 10'h4f == guard_index ? 64'h4f : _GEN_97; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_99 = 10'h50 == guard_index ? 64'h50 : _GEN_98; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_100 = 10'h51 == guard_index ? 64'h51 : _GEN_99; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_101 = 10'h52 == guard_index ? 64'h52 : _GEN_100; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_102 = 10'h53 == guard_index ? 64'h53 : _GEN_101; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_103 = 10'h54 == guard_index ? 64'h54 : _GEN_102; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_104 = 10'h55 == guard_index ? 64'h55 : _GEN_103; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_105 = 10'h56 == guard_index ? 64'h56 : _GEN_104; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_106 = 10'h57 == guard_index ? 64'h57 : _GEN_105; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_107 = 10'h58 == guard_index ? 64'h58 : _GEN_106; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_108 = 10'h59 == guard_index ? 64'h59 : _GEN_107; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_109 = 10'h5a == guard_index ? 64'h5a : _GEN_108; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_110 = 10'h5b == guard_index ? 64'h5b : _GEN_109; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_111 = 10'h5c == guard_index ? 64'h5c : _GEN_110; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_112 = 10'h5d == guard_index ? 64'h5d : _GEN_111; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_113 = 10'h5e == guard_index ? 64'h5e : _GEN_112; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_114 = 10'h5f == guard_index ? 64'h5f : _GEN_113; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_115 = 10'h60 == guard_index ? 64'h60 : _GEN_114; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_116 = 10'h61 == guard_index ? 64'h61 : _GEN_115; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_117 = 10'h62 == guard_index ? 64'h62 : _GEN_116; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_118 = 10'h63 == guard_index ? 64'h63 : _GEN_117; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_119 = 10'h64 == guard_index ? 64'h64 : _GEN_118; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_120 = 10'h65 == guard_index ? 64'h65 : _GEN_119; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_121 = 10'h66 == guard_index ? 64'h66 : _GEN_120; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_122 = 10'h67 == guard_index ? 64'h67 : _GEN_121; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_123 = 10'h68 == guard_index ? 64'h68 : _GEN_122; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_124 = 10'h69 == guard_index ? 64'h69 : _GEN_123; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_125 = 10'h6a == guard_index ? 64'h6a : _GEN_124; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_126 = 10'h6b == guard_index ? 64'h6b : _GEN_125; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_127 = 10'h6c == guard_index ? 64'h6c : _GEN_126; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_128 = 10'h6d == guard_index ? 64'h6d : _GEN_127; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_129 = 10'h6e == guard_index ? 64'h6e : _GEN_128; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_130 = 10'h6f == guard_index ? 64'h6f : _GEN_129; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_131 = 10'h70 == guard_index ? 64'h70 : _GEN_130; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_132 = 10'h71 == guard_index ? 64'h71 : _GEN_131; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_133 = 10'h72 == guard_index ? 64'h72 : _GEN_132; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_134 = 10'h73 == guard_index ? 64'h73 : _GEN_133; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_135 = 10'h74 == guard_index ? 64'h74 : _GEN_134; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_136 = 10'h75 == guard_index ? 64'h75 : _GEN_135; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_137 = 10'h76 == guard_index ? 64'h76 : _GEN_136; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_138 = 10'h77 == guard_index ? 64'h77 : _GEN_137; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_139 = 10'h78 == guard_index ? 64'h78 : _GEN_138; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_140 = 10'h79 == guard_index ? 64'h79 : _GEN_139; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_141 = 10'h7a == guard_index ? 64'h7a : _GEN_140; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_142 = 10'h7b == guard_index ? 64'h7b : _GEN_141; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_143 = 10'h7c == guard_index ? 64'h7c : _GEN_142; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_144 = 10'h7d == guard_index ? 64'h7d : _GEN_143; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_145 = 10'h7e == guard_index ? 64'h7e : _GEN_144; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_146 = 10'h7f == guard_index ? 64'h7f : _GEN_145; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_147 = 10'h80 == guard_index ? 64'h80 : _GEN_146; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_148 = 10'h81 == guard_index ? 64'h81 : _GEN_147; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_149 = 10'h82 == guard_index ? 64'h82 : _GEN_148; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_150 = 10'h83 == guard_index ? 64'h83 : _GEN_149; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_151 = 10'h84 == guard_index ? 64'h84 : _GEN_150; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_152 = 10'h85 == guard_index ? 64'h85 : _GEN_151; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_153 = 10'h86 == guard_index ? 64'h86 : _GEN_152; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_154 = 10'h87 == guard_index ? 64'h87 : _GEN_153; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_155 = 10'h88 == guard_index ? 64'h88 : _GEN_154; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_156 = 10'h89 == guard_index ? 64'h89 : _GEN_155; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_157 = 10'h8a == guard_index ? 64'h8a : _GEN_156; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_158 = 10'h8b == guard_index ? 64'h8b : _GEN_157; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_159 = 10'h8c == guard_index ? 64'h8c : _GEN_158; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_160 = 10'h8d == guard_index ? 64'h8d : _GEN_159; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_161 = 10'h8e == guard_index ? 64'h8e : _GEN_160; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_162 = 10'h8f == guard_index ? 64'h8f : _GEN_161; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_163 = 10'h90 == guard_index ? 64'h90 : _GEN_162; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_164 = 10'h91 == guard_index ? 64'h91 : _GEN_163; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_165 = 10'h92 == guard_index ? 64'h92 : _GEN_164; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_166 = 10'h93 == guard_index ? 64'h93 : _GEN_165; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_167 = 10'h94 == guard_index ? 64'h94 : _GEN_166; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_168 = 10'h95 == guard_index ? 64'h95 : _GEN_167; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_169 = 10'h96 == guard_index ? 64'h96 : _GEN_168; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_170 = 10'h97 == guard_index ? 64'h97 : _GEN_169; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_171 = 10'h98 == guard_index ? 64'h98 : _GEN_170; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_172 = 10'h99 == guard_index ? 64'h99 : _GEN_171; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_173 = 10'h9a == guard_index ? 64'h9a : _GEN_172; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_174 = 10'h9b == guard_index ? 64'h9b : _GEN_173; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_175 = 10'h9c == guard_index ? 64'h9c : _GEN_174; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_176 = 10'h9d == guard_index ? 64'h9d : _GEN_175; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_177 = 10'h9e == guard_index ? 64'h9e : _GEN_176; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_178 = 10'h9f == guard_index ? 64'h9f : _GEN_177; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_179 = 10'ha0 == guard_index ? 64'ha0 : _GEN_178; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_180 = 10'ha1 == guard_index ? 64'ha1 : _GEN_179; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_181 = 10'ha2 == guard_index ? 64'ha2 : _GEN_180; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_182 = 10'ha3 == guard_index ? 64'ha3 : _GEN_181; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_183 = 10'ha4 == guard_index ? 64'ha4 : _GEN_182; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_184 = 10'ha5 == guard_index ? 64'ha5 : _GEN_183; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_185 = 10'ha6 == guard_index ? 64'ha6 : _GEN_184; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_186 = 10'ha7 == guard_index ? 64'ha7 : _GEN_185; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_187 = 10'ha8 == guard_index ? 64'ha8 : _GEN_186; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_188 = 10'ha9 == guard_index ? 64'ha9 : _GEN_187; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_189 = 10'haa == guard_index ? 64'haa : _GEN_188; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_190 = 10'hab == guard_index ? 64'hab : _GEN_189; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_191 = 10'hac == guard_index ? 64'hac : _GEN_190; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_192 = 10'had == guard_index ? 64'had : _GEN_191; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_193 = 10'hae == guard_index ? 64'hae : _GEN_192; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_194 = 10'haf == guard_index ? 64'haf : _GEN_193; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_195 = 10'hb0 == guard_index ? 64'hb0 : _GEN_194; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_196 = 10'hb1 == guard_index ? 64'hb1 : _GEN_195; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_197 = 10'hb2 == guard_index ? 64'hb2 : _GEN_196; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_198 = 10'hb3 == guard_index ? 64'hb3 : _GEN_197; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_199 = 10'hb4 == guard_index ? 64'hb4 : _GEN_198; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_200 = 10'hb5 == guard_index ? 64'hb5 : _GEN_199; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_201 = 10'hb6 == guard_index ? 64'hb6 : _GEN_200; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_202 = 10'hb7 == guard_index ? 64'hb7 : _GEN_201; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_203 = 10'hb8 == guard_index ? 64'hb8 : _GEN_202; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_204 = 10'hb9 == guard_index ? 64'hb9 : _GEN_203; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_205 = 10'hba == guard_index ? 64'hba : _GEN_204; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_206 = 10'hbb == guard_index ? 64'hbb : _GEN_205; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_207 = 10'hbc == guard_index ? 64'hbc : _GEN_206; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_208 = 10'hbd == guard_index ? 64'hbd : _GEN_207; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_209 = 10'hbe == guard_index ? 64'hbe : _GEN_208; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_210 = 10'hbf == guard_index ? 64'hbf : _GEN_209; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_211 = 10'hc0 == guard_index ? 64'hc0 : _GEN_210; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_212 = 10'hc1 == guard_index ? 64'hc1 : _GEN_211; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_213 = 10'hc2 == guard_index ? 64'hc2 : _GEN_212; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_214 = 10'hc3 == guard_index ? 64'hc3 : _GEN_213; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_215 = 10'hc4 == guard_index ? 64'hc4 : _GEN_214; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_216 = 10'hc5 == guard_index ? 64'hc5 : _GEN_215; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_217 = 10'hc6 == guard_index ? 64'hc6 : _GEN_216; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_218 = 10'hc7 == guard_index ? 64'hc7 : _GEN_217; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_219 = 10'hc8 == guard_index ? 64'hc8 : _GEN_218; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_220 = 10'hc9 == guard_index ? 64'hc9 : _GEN_219; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_221 = 10'hca == guard_index ? 64'hca : _GEN_220; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_222 = 10'hcb == guard_index ? 64'hcb : _GEN_221; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_223 = 10'hcc == guard_index ? 64'hcc : _GEN_222; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_224 = 10'hcd == guard_index ? 64'hcd : _GEN_223; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_225 = 10'hce == guard_index ? 64'hce : _GEN_224; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_226 = 10'hcf == guard_index ? 64'hcf : _GEN_225; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_227 = 10'hd0 == guard_index ? 64'hd0 : _GEN_226; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_228 = 10'hd1 == guard_index ? 64'hd1 : _GEN_227; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_229 = 10'hd2 == guard_index ? 64'hd2 : _GEN_228; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_230 = 10'hd3 == guard_index ? 64'hd3 : _GEN_229; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_231 = 10'hd4 == guard_index ? 64'hd4 : _GEN_230; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_232 = 10'hd5 == guard_index ? 64'hd5 : _GEN_231; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_233 = 10'hd6 == guard_index ? 64'hd6 : _GEN_232; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_234 = 10'hd7 == guard_index ? 64'hd7 : _GEN_233; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_235 = 10'hd8 == guard_index ? 64'hd8 : _GEN_234; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_236 = 10'hd9 == guard_index ? 64'hd9 : _GEN_235; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_237 = 10'hda == guard_index ? 64'hda : _GEN_236; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_238 = 10'hdb == guard_index ? 64'hdb : _GEN_237; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_239 = 10'hdc == guard_index ? 64'hdc : _GEN_238; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_240 = 10'hdd == guard_index ? 64'hdd : _GEN_239; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_241 = 10'hde == guard_index ? 64'hde : _GEN_240; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_242 = 10'hdf == guard_index ? 64'hdf : _GEN_241; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_243 = 10'he0 == guard_index ? 64'he0 : _GEN_242; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_244 = 10'he1 == guard_index ? 64'he1 : _GEN_243; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_245 = 10'he2 == guard_index ? 64'he2 : _GEN_244; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_246 = 10'he3 == guard_index ? 64'he3 : _GEN_245; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_247 = 10'he4 == guard_index ? 64'he4 : _GEN_246; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_248 = 10'he5 == guard_index ? 64'he5 : _GEN_247; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_249 = 10'he6 == guard_index ? 64'he6 : _GEN_248; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_250 = 10'he7 == guard_index ? 64'he7 : _GEN_249; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_251 = 10'he8 == guard_index ? 64'he8 : _GEN_250; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_252 = 10'he9 == guard_index ? 64'he9 : _GEN_251; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_253 = 10'hea == guard_index ? 64'hea : _GEN_252; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_254 = 10'heb == guard_index ? 64'heb : _GEN_253; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_255 = 10'hec == guard_index ? 64'hec : _GEN_254; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_256 = 10'hed == guard_index ? 64'hed : _GEN_255; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_257 = 10'hee == guard_index ? 64'hee : _GEN_256; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_258 = 10'hef == guard_index ? 64'hef : _GEN_257; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_259 = 10'hf0 == guard_index ? 64'hf0 : _GEN_258; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_260 = 10'hf1 == guard_index ? 64'hf1 : _GEN_259; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_261 = 10'hf2 == guard_index ? 64'hf2 : _GEN_260; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_262 = 10'hf3 == guard_index ? 64'hf3 : _GEN_261; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_263 = 10'hf4 == guard_index ? 64'hf4 : _GEN_262; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_264 = 10'hf5 == guard_index ? 64'hf5 : _GEN_263; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_265 = 10'hf6 == guard_index ? 64'hf6 : _GEN_264; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_266 = 10'hf7 == guard_index ? 64'hf7 : _GEN_265; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_267 = 10'hf8 == guard_index ? 64'hf8 : _GEN_266; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_268 = 10'hf9 == guard_index ? 64'hf9 : _GEN_267; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_269 = 10'hfa == guard_index ? 64'hfa : _GEN_268; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_270 = 10'hfb == guard_index ? 64'hfb : _GEN_269; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_271 = 10'hfc == guard_index ? 64'hfc : _GEN_270; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_272 = 10'hfd == guard_index ? 64'hfd : _GEN_271; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_273 = 10'hfe == guard_index ? 64'hfe : _GEN_272; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_274 = 10'hff == guard_index ? 64'hff : _GEN_273; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_275 = 10'h100 == guard_index ? 64'h100 : _GEN_274; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_276 = 10'h101 == guard_index ? 64'h101 : _GEN_275; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_277 = 10'h102 == guard_index ? 64'h102 : _GEN_276; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_278 = 10'h103 == guard_index ? 64'h103 : _GEN_277; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_279 = 10'h104 == guard_index ? 64'h104 : _GEN_278; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_280 = 10'h105 == guard_index ? 64'h105 : _GEN_279; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_281 = 10'h106 == guard_index ? 64'h106 : _GEN_280; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_282 = 10'h107 == guard_index ? 64'h107 : _GEN_281; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_283 = 10'h108 == guard_index ? 64'h108 : _GEN_282; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_284 = 10'h109 == guard_index ? 64'h109 : _GEN_283; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_285 = 10'h10a == guard_index ? 64'h10a : _GEN_284; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_286 = 10'h10b == guard_index ? 64'h10b : _GEN_285; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_287 = 10'h10c == guard_index ? 64'h10c : _GEN_286; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_288 = 10'h10d == guard_index ? 64'h10d : _GEN_287; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_289 = 10'h10e == guard_index ? 64'h10e : _GEN_288; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_290 = 10'h10f == guard_index ? 64'h10f : _GEN_289; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_291 = 10'h110 == guard_index ? 64'h110 : _GEN_290; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_292 = 10'h111 == guard_index ? 64'h111 : _GEN_291; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_293 = 10'h112 == guard_index ? 64'h112 : _GEN_292; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_294 = 10'h113 == guard_index ? 64'h113 : _GEN_293; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_295 = 10'h114 == guard_index ? 64'h114 : _GEN_294; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_296 = 10'h115 == guard_index ? 64'h115 : _GEN_295; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_297 = 10'h116 == guard_index ? 64'h116 : _GEN_296; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_298 = 10'h117 == guard_index ? 64'h117 : _GEN_297; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_299 = 10'h118 == guard_index ? 64'h118 : _GEN_298; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_300 = 10'h119 == guard_index ? 64'h119 : _GEN_299; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_301 = 10'h11a == guard_index ? 64'h11a : _GEN_300; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_302 = 10'h11b == guard_index ? 64'h11b : _GEN_301; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_303 = 10'h11c == guard_index ? 64'h11c : _GEN_302; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_304 = 10'h11d == guard_index ? 64'h11d : _GEN_303; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_305 = 10'h11e == guard_index ? 64'h11e : _GEN_304; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_306 = 10'h11f == guard_index ? 64'h11f : _GEN_305; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_307 = 10'h120 == guard_index ? 64'h120 : _GEN_306; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_308 = 10'h121 == guard_index ? 64'h121 : _GEN_307; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_309 = 10'h122 == guard_index ? 64'h122 : _GEN_308; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_310 = 10'h123 == guard_index ? 64'h123 : _GEN_309; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_311 = 10'h124 == guard_index ? 64'h124 : _GEN_310; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_312 = 10'h125 == guard_index ? 64'h125 : _GEN_311; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_313 = 10'h126 == guard_index ? 64'h126 : _GEN_312; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_314 = 10'h127 == guard_index ? 64'h127 : _GEN_313; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_315 = 10'h128 == guard_index ? 64'h128 : _GEN_314; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_316 = 10'h129 == guard_index ? 64'h129 : _GEN_315; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_317 = 10'h12a == guard_index ? 64'h12a : _GEN_316; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_318 = 10'h12b == guard_index ? 64'h12b : _GEN_317; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_319 = 10'h12c == guard_index ? 64'h12c : _GEN_318; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_320 = 10'h12d == guard_index ? 64'h12d : _GEN_319; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_321 = 10'h12e == guard_index ? 64'h12e : _GEN_320; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_322 = 10'h12f == guard_index ? 64'h12f : _GEN_321; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_323 = 10'h130 == guard_index ? 64'h130 : _GEN_322; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_324 = 10'h131 == guard_index ? 64'h131 : _GEN_323; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_325 = 10'h132 == guard_index ? 64'h132 : _GEN_324; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_326 = 10'h133 == guard_index ? 64'h133 : _GEN_325; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_327 = 10'h134 == guard_index ? 64'h134 : _GEN_326; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_328 = 10'h135 == guard_index ? 64'h135 : _GEN_327; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_329 = 10'h136 == guard_index ? 64'h136 : _GEN_328; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_330 = 10'h137 == guard_index ? 64'h137 : _GEN_329; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_331 = 10'h138 == guard_index ? 64'h138 : _GEN_330; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_332 = 10'h139 == guard_index ? 64'h139 : _GEN_331; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_333 = 10'h13a == guard_index ? 64'h13a : _GEN_332; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_334 = 10'h13b == guard_index ? 64'h13b : _GEN_333; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_335 = 10'h13c == guard_index ? 64'h13c : _GEN_334; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_336 = 10'h13d == guard_index ? 64'h13d : _GEN_335; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_337 = 10'h13e == guard_index ? 64'h13e : _GEN_336; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_338 = 10'h13f == guard_index ? 64'h13f : _GEN_337; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_339 = 10'h140 == guard_index ? 64'h140 : _GEN_338; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_340 = 10'h141 == guard_index ? 64'h141 : _GEN_339; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_341 = 10'h142 == guard_index ? 64'h142 : _GEN_340; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_342 = 10'h143 == guard_index ? 64'h143 : _GEN_341; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_343 = 10'h144 == guard_index ? 64'h144 : _GEN_342; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_344 = 10'h145 == guard_index ? 64'h145 : _GEN_343; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_345 = 10'h146 == guard_index ? 64'h146 : _GEN_344; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_346 = 10'h147 == guard_index ? 64'h147 : _GEN_345; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_347 = 10'h148 == guard_index ? 64'h148 : _GEN_346; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_348 = 10'h149 == guard_index ? 64'h149 : _GEN_347; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_349 = 10'h14a == guard_index ? 64'h14a : _GEN_348; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_350 = 10'h14b == guard_index ? 64'h14b : _GEN_349; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_351 = 10'h14c == guard_index ? 64'h14c : _GEN_350; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_352 = 10'h14d == guard_index ? 64'h14d : _GEN_351; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_353 = 10'h14e == guard_index ? 64'h14e : _GEN_352; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_354 = 10'h14f == guard_index ? 64'h14f : _GEN_353; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_355 = 10'h150 == guard_index ? 64'h150 : _GEN_354; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_356 = 10'h151 == guard_index ? 64'h151 : _GEN_355; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_357 = 10'h152 == guard_index ? 64'h152 : _GEN_356; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_358 = 10'h153 == guard_index ? 64'h153 : _GEN_357; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_359 = 10'h154 == guard_index ? 64'h154 : _GEN_358; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_360 = 10'h155 == guard_index ? 64'h155 : _GEN_359; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_361 = 10'h156 == guard_index ? 64'h156 : _GEN_360; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_362 = 10'h157 == guard_index ? 64'h157 : _GEN_361; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_363 = 10'h158 == guard_index ? 64'h158 : _GEN_362; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_364 = 10'h159 == guard_index ? 64'h159 : _GEN_363; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_365 = 10'h15a == guard_index ? 64'h15a : _GEN_364; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_366 = 10'h15b == guard_index ? 64'h15b : _GEN_365; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_367 = 10'h15c == guard_index ? 64'h15c : _GEN_366; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_368 = 10'h15d == guard_index ? 64'h15d : _GEN_367; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_369 = 10'h15e == guard_index ? 64'h15e : _GEN_368; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_370 = 10'h15f == guard_index ? 64'h15f : _GEN_369; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_371 = 10'h160 == guard_index ? 64'h160 : _GEN_370; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_372 = 10'h161 == guard_index ? 64'h161 : _GEN_371; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_373 = 10'h162 == guard_index ? 64'h162 : _GEN_372; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_374 = 10'h163 == guard_index ? 64'h163 : _GEN_373; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_375 = 10'h164 == guard_index ? 64'h164 : _GEN_374; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_376 = 10'h165 == guard_index ? 64'h165 : _GEN_375; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_377 = 10'h166 == guard_index ? 64'h166 : _GEN_376; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_378 = 10'h167 == guard_index ? 64'h167 : _GEN_377; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_379 = 10'h168 == guard_index ? 64'h168 : _GEN_378; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_380 = 10'h169 == guard_index ? 64'h169 : _GEN_379; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_381 = 10'h16a == guard_index ? 64'h16a : _GEN_380; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_382 = 10'h16b == guard_index ? 64'h16b : _GEN_381; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_383 = 10'h16c == guard_index ? 64'h16c : _GEN_382; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_384 = 10'h16d == guard_index ? 64'h16d : _GEN_383; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_385 = 10'h16e == guard_index ? 64'h16e : _GEN_384; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_386 = 10'h16f == guard_index ? 64'h16f : _GEN_385; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_387 = 10'h170 == guard_index ? 64'h170 : _GEN_386; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_388 = 10'h171 == guard_index ? 64'h171 : _GEN_387; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_389 = 10'h172 == guard_index ? 64'h172 : _GEN_388; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_390 = 10'h173 == guard_index ? 64'h173 : _GEN_389; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_391 = 10'h174 == guard_index ? 64'h174 : _GEN_390; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_392 = 10'h175 == guard_index ? 64'h175 : _GEN_391; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_393 = 10'h176 == guard_index ? 64'h176 : _GEN_392; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_394 = 10'h177 == guard_index ? 64'h177 : _GEN_393; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_395 = 10'h178 == guard_index ? 64'h178 : _GEN_394; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_396 = 10'h179 == guard_index ? 64'h179 : _GEN_395; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_397 = 10'h17a == guard_index ? 64'h17a : _GEN_396; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_398 = 10'h17b == guard_index ? 64'h17b : _GEN_397; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_399 = 10'h17c == guard_index ? 64'h17c : _GEN_398; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_400 = 10'h17d == guard_index ? 64'h17d : _GEN_399; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_401 = 10'h17e == guard_index ? 64'h17e : _GEN_400; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_402 = 10'h17f == guard_index ? 64'h17f : _GEN_401; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_403 = 10'h180 == guard_index ? 64'h180 : _GEN_402; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_404 = 10'h181 == guard_index ? 64'h181 : _GEN_403; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_405 = 10'h182 == guard_index ? 64'h182 : _GEN_404; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_406 = 10'h183 == guard_index ? 64'h183 : _GEN_405; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_407 = 10'h184 == guard_index ? 64'h184 : _GEN_406; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_408 = 10'h185 == guard_index ? 64'h185 : _GEN_407; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_409 = 10'h186 == guard_index ? 64'h186 : _GEN_408; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_410 = 10'h187 == guard_index ? 64'h187 : _GEN_409; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_411 = 10'h188 == guard_index ? 64'h188 : _GEN_410; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_412 = 10'h189 == guard_index ? 64'h189 : _GEN_411; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_413 = 10'h18a == guard_index ? 64'h18a : _GEN_412; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_414 = 10'h18b == guard_index ? 64'h18b : _GEN_413; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_415 = 10'h18c == guard_index ? 64'h18c : _GEN_414; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_416 = 10'h18d == guard_index ? 64'h18d : _GEN_415; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_417 = 10'h18e == guard_index ? 64'h18e : _GEN_416; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_418 = 10'h18f == guard_index ? 64'h18f : _GEN_417; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_419 = 10'h190 == guard_index ? 64'h190 : _GEN_418; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_420 = 10'h191 == guard_index ? 64'h191 : _GEN_419; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_421 = 10'h192 == guard_index ? 64'h192 : _GEN_420; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_422 = 10'h193 == guard_index ? 64'h193 : _GEN_421; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_423 = 10'h194 == guard_index ? 64'h194 : _GEN_422; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_424 = 10'h195 == guard_index ? 64'h195 : _GEN_423; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_425 = 10'h196 == guard_index ? 64'h196 : _GEN_424; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_426 = 10'h197 == guard_index ? 64'h197 : _GEN_425; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_427 = 10'h198 == guard_index ? 64'h198 : _GEN_426; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_428 = 10'h199 == guard_index ? 64'h199 : _GEN_427; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_429 = 10'h19a == guard_index ? 64'h19a : _GEN_428; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_430 = 10'h19b == guard_index ? 64'h19b : _GEN_429; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_431 = 10'h19c == guard_index ? 64'h19c : _GEN_430; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_432 = 10'h19d == guard_index ? 64'h19d : _GEN_431; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_433 = 10'h19e == guard_index ? 64'h19e : _GEN_432; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_434 = 10'h19f == guard_index ? 64'h19f : _GEN_433; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_435 = 10'h1a0 == guard_index ? 64'h1a0 : _GEN_434; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_436 = 10'h1a1 == guard_index ? 64'h1a1 : _GEN_435; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_437 = 10'h1a2 == guard_index ? 64'h1a2 : _GEN_436; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_438 = 10'h1a3 == guard_index ? 64'h1a3 : _GEN_437; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_439 = 10'h1a4 == guard_index ? 64'h1a4 : _GEN_438; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_440 = 10'h1a5 == guard_index ? 64'h1a5 : _GEN_439; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_441 = 10'h1a6 == guard_index ? 64'h1a6 : _GEN_440; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_442 = 10'h1a7 == guard_index ? 64'h1a7 : _GEN_441; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_443 = 10'h1a8 == guard_index ? 64'h1a8 : _GEN_442; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_444 = 10'h1a9 == guard_index ? 64'h1a9 : _GEN_443; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_445 = 10'h1aa == guard_index ? 64'h1aa : _GEN_444; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_446 = 10'h1ab == guard_index ? 64'h1ab : _GEN_445; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_447 = 10'h1ac == guard_index ? 64'h1ac : _GEN_446; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_448 = 10'h1ad == guard_index ? 64'h1ad : _GEN_447; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_449 = 10'h1ae == guard_index ? 64'h1ae : _GEN_448; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_450 = 10'h1af == guard_index ? 64'h1af : _GEN_449; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_451 = 10'h1b0 == guard_index ? 64'h1b0 : _GEN_450; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_452 = 10'h1b1 == guard_index ? 64'h1b1 : _GEN_451; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_453 = 10'h1b2 == guard_index ? 64'h1b2 : _GEN_452; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_454 = 10'h1b3 == guard_index ? 64'h1b3 : _GEN_453; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_455 = 10'h1b4 == guard_index ? 64'h1b4 : _GEN_454; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_456 = 10'h1b5 == guard_index ? 64'h1b5 : _GEN_455; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_457 = 10'h1b6 == guard_index ? 64'h1b6 : _GEN_456; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_458 = 10'h1b7 == guard_index ? 64'h1b7 : _GEN_457; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_459 = 10'h1b8 == guard_index ? 64'h1b8 : _GEN_458; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_460 = 10'h1b9 == guard_index ? 64'h1b9 : _GEN_459; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_461 = 10'h1ba == guard_index ? 64'h1ba : _GEN_460; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_462 = 10'h1bb == guard_index ? 64'h1bb : _GEN_461; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_463 = 10'h1bc == guard_index ? 64'h1bc : _GEN_462; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_464 = 10'h1bd == guard_index ? 64'h1bd : _GEN_463; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_465 = 10'h1be == guard_index ? 64'h1be : _GEN_464; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_466 = 10'h1bf == guard_index ? 64'h1bf : _GEN_465; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_467 = 10'h1c0 == guard_index ? 64'h1c0 : _GEN_466; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_468 = 10'h1c1 == guard_index ? 64'h1c1 : _GEN_467; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_469 = 10'h1c2 == guard_index ? 64'h1c2 : _GEN_468; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_470 = 10'h1c3 == guard_index ? 64'h1c3 : _GEN_469; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_471 = 10'h1c4 == guard_index ? 64'h1c4 : _GEN_470; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_472 = 10'h1c5 == guard_index ? 64'h1c5 : _GEN_471; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_473 = 10'h1c6 == guard_index ? 64'h1c6 : _GEN_472; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_474 = 10'h1c7 == guard_index ? 64'h1c7 : _GEN_473; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_475 = 10'h1c8 == guard_index ? 64'h1c8 : _GEN_474; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_476 = 10'h1c9 == guard_index ? 64'h1c9 : _GEN_475; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_477 = 10'h1ca == guard_index ? 64'h1ca : _GEN_476; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_478 = 10'h1cb == guard_index ? 64'h1cb : _GEN_477; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_479 = 10'h1cc == guard_index ? 64'h1cc : _GEN_478; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_480 = 10'h1cd == guard_index ? 64'h1cd : _GEN_479; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_481 = 10'h1ce == guard_index ? 64'h1ce : _GEN_480; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_482 = 10'h1cf == guard_index ? 64'h1cf : _GEN_481; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_483 = 10'h1d0 == guard_index ? 64'h1d0 : _GEN_482; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_484 = 10'h1d1 == guard_index ? 64'h1d1 : _GEN_483; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_485 = 10'h1d2 == guard_index ? 64'h1d2 : _GEN_484; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_486 = 10'h1d3 == guard_index ? 64'h1d3 : _GEN_485; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_487 = 10'h1d4 == guard_index ? 64'h1d4 : _GEN_486; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_488 = 10'h1d5 == guard_index ? 64'h1d5 : _GEN_487; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_489 = 10'h1d6 == guard_index ? 64'h1d6 : _GEN_488; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_490 = 10'h1d7 == guard_index ? 64'h1d7 : _GEN_489; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_491 = 10'h1d8 == guard_index ? 64'h1d8 : _GEN_490; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_492 = 10'h1d9 == guard_index ? 64'h1d9 : _GEN_491; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_493 = 10'h1da == guard_index ? 64'h1da : _GEN_492; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_494 = 10'h1db == guard_index ? 64'h1db : _GEN_493; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_495 = 10'h1dc == guard_index ? 64'h1dc : _GEN_494; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_496 = 10'h1dd == guard_index ? 64'h1dd : _GEN_495; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_497 = 10'h1de == guard_index ? 64'h1de : _GEN_496; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_498 = 10'h1df == guard_index ? 64'h1df : _GEN_497; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_499 = 10'h1e0 == guard_index ? 64'h1e0 : _GEN_498; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_500 = 10'h1e1 == guard_index ? 64'h1e1 : _GEN_499; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_501 = 10'h1e2 == guard_index ? 64'h1e2 : _GEN_500; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_502 = 10'h1e3 == guard_index ? 64'h1e3 : _GEN_501; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_503 = 10'h1e4 == guard_index ? 64'h1e4 : _GEN_502; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_504 = 10'h1e5 == guard_index ? 64'h1e5 : _GEN_503; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_505 = 10'h1e6 == guard_index ? 64'h1e6 : _GEN_504; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_506 = 10'h1e7 == guard_index ? 64'h1e7 : _GEN_505; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_507 = 10'h1e8 == guard_index ? 64'h1e8 : _GEN_506; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_508 = 10'h1e9 == guard_index ? 64'h1e9 : _GEN_507; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_509 = 10'h1ea == guard_index ? 64'h1ea : _GEN_508; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_510 = 10'h1eb == guard_index ? 64'h1eb : _GEN_509; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_511 = 10'h1ec == guard_index ? 64'h1ec : _GEN_510; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_512 = 10'h1ed == guard_index ? 64'h1ed : _GEN_511; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_513 = 10'h1ee == guard_index ? 64'h1ee : _GEN_512; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_514 = 10'h1ef == guard_index ? 64'h1ef : _GEN_513; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_515 = 10'h1f0 == guard_index ? 64'h1f0 : _GEN_514; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_516 = 10'h1f1 == guard_index ? 64'h1f1 : _GEN_515; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_517 = 10'h1f2 == guard_index ? 64'h1f2 : _GEN_516; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_518 = 10'h1f3 == guard_index ? 64'h1f3 : _GEN_517; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_519 = 10'h1f4 == guard_index ? 64'h1f4 : _GEN_518; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_520 = 10'h1f5 == guard_index ? 64'h1f5 : _GEN_519; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_521 = 10'h1f6 == guard_index ? 64'h1f6 : _GEN_520; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_522 = 10'h1f7 == guard_index ? 64'h1f7 : _GEN_521; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_523 = 10'h1f8 == guard_index ? 64'h1f8 : _GEN_522; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_524 = 10'h1f9 == guard_index ? 64'h1f9 : _GEN_523; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_525 = 10'h1fa == guard_index ? 64'h1fa : _GEN_524; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_526 = 10'h1fb == guard_index ? 64'h1fb : _GEN_525; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_527 = 10'h1fc == guard_index ? 64'h1fc : _GEN_526; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_528 = 10'h1fd == guard_index ? 64'h1fd : _GEN_527; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_529 = 10'h1fe == guard_index ? 64'h1fe : _GEN_528; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_530 = 10'h1ff == guard_index ? 64'h1ff : _GEN_529; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_531 = 10'h200 == guard_index ? 64'h200 : _GEN_530; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_532 = 10'h201 == guard_index ? 64'h201 : _GEN_531; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_533 = 10'h202 == guard_index ? 64'h202 : _GEN_532; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_534 = 10'h203 == guard_index ? 64'h203 : _GEN_533; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_535 = 10'h204 == guard_index ? 64'h204 : _GEN_534; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_536 = 10'h205 == guard_index ? 64'h205 : _GEN_535; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_537 = 10'h206 == guard_index ? 64'h206 : _GEN_536; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_538 = 10'h207 == guard_index ? 64'h207 : _GEN_537; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_539 = 10'h208 == guard_index ? 64'h208 : _GEN_538; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_540 = 10'h209 == guard_index ? 64'h209 : _GEN_539; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_541 = 10'h20a == guard_index ? 64'h20a : _GEN_540; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_542 = 10'h20b == guard_index ? 64'h20b : _GEN_541; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_543 = 10'h20c == guard_index ? 64'h20c : _GEN_542; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_544 = 10'h20d == guard_index ? 64'h20d : _GEN_543; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_545 = 10'h20e == guard_index ? 64'h20e : _GEN_544; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_546 = 10'h20f == guard_index ? 64'h20f : _GEN_545; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_547 = 10'h210 == guard_index ? 64'h210 : _GEN_546; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_548 = 10'h211 == guard_index ? 64'h211 : _GEN_547; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_549 = 10'h212 == guard_index ? 64'h212 : _GEN_548; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_550 = 10'h213 == guard_index ? 64'h213 : _GEN_549; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_551 = 10'h214 == guard_index ? 64'h214 : _GEN_550; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_552 = 10'h215 == guard_index ? 64'h215 : _GEN_551; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_553 = 10'h216 == guard_index ? 64'h216 : _GEN_552; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_554 = 10'h217 == guard_index ? 64'h217 : _GEN_553; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_555 = 10'h218 == guard_index ? 64'h218 : _GEN_554; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_556 = 10'h219 == guard_index ? 64'h219 : _GEN_555; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_557 = 10'h21a == guard_index ? 64'h21a : _GEN_556; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_558 = 10'h21b == guard_index ? 64'h21b : _GEN_557; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_559 = 10'h21c == guard_index ? 64'h21c : _GEN_558; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_560 = 10'h21d == guard_index ? 64'h21d : _GEN_559; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_561 = 10'h21e == guard_index ? 64'h21e : _GEN_560; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_562 = 10'h21f == guard_index ? 64'h21f : _GEN_561; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_563 = 10'h220 == guard_index ? 64'h220 : _GEN_562; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_564 = 10'h221 == guard_index ? 64'h221 : _GEN_563; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_565 = 10'h222 == guard_index ? 64'h222 : _GEN_564; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_566 = 10'h223 == guard_index ? 64'h223 : _GEN_565; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_567 = 10'h224 == guard_index ? 64'h224 : _GEN_566; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_568 = 10'h225 == guard_index ? 64'h225 : _GEN_567; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_569 = 10'h226 == guard_index ? 64'h226 : _GEN_568; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_570 = 10'h227 == guard_index ? 64'h227 : _GEN_569; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_571 = 10'h228 == guard_index ? 64'h228 : _GEN_570; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_572 = 10'h229 == guard_index ? 64'h229 : _GEN_571; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_573 = 10'h22a == guard_index ? 64'h22a : _GEN_572; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_574 = 10'h22b == guard_index ? 64'h22b : _GEN_573; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_575 = 10'h22c == guard_index ? 64'h22c : _GEN_574; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_576 = 10'h22d == guard_index ? 64'h22d : _GEN_575; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_577 = 10'h22e == guard_index ? 64'h22e : _GEN_576; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_578 = 10'h22f == guard_index ? 64'h22f : _GEN_577; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_579 = 10'h230 == guard_index ? 64'h230 : _GEN_578; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_580 = 10'h231 == guard_index ? 64'h231 : _GEN_579; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_581 = 10'h232 == guard_index ? 64'h232 : _GEN_580; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_582 = 10'h233 == guard_index ? 64'h233 : _GEN_581; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_583 = 10'h234 == guard_index ? 64'h234 : _GEN_582; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_584 = 10'h235 == guard_index ? 64'h235 : _GEN_583; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_585 = 10'h236 == guard_index ? 64'h236 : _GEN_584; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_586 = 10'h237 == guard_index ? 64'h237 : _GEN_585; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_587 = 10'h238 == guard_index ? 64'h238 : _GEN_586; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_588 = 10'h239 == guard_index ? 64'h239 : _GEN_587; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_589 = 10'h23a == guard_index ? 64'h23a : _GEN_588; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_590 = 10'h23b == guard_index ? 64'h23b : _GEN_589; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_591 = 10'h23c == guard_index ? 64'h23c : _GEN_590; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_592 = 10'h23d == guard_index ? 64'h23d : _GEN_591; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_593 = 10'h23e == guard_index ? 64'h23e : _GEN_592; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_594 = 10'h23f == guard_index ? 64'h23f : _GEN_593; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_595 = 10'h240 == guard_index ? 64'h240 : _GEN_594; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_596 = 10'h241 == guard_index ? 64'h241 : _GEN_595; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_597 = 10'h242 == guard_index ? 64'h242 : _GEN_596; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_598 = 10'h243 == guard_index ? 64'h243 : _GEN_597; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_599 = 10'h244 == guard_index ? 64'h244 : _GEN_598; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_600 = 10'h245 == guard_index ? 64'h245 : _GEN_599; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_601 = 10'h246 == guard_index ? 64'h246 : _GEN_600; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_602 = 10'h247 == guard_index ? 64'h247 : _GEN_601; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_603 = 10'h248 == guard_index ? 64'h248 : _GEN_602; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_604 = 10'h249 == guard_index ? 64'h249 : _GEN_603; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_605 = 10'h24a == guard_index ? 64'h24a : _GEN_604; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_606 = 10'h24b == guard_index ? 64'h24b : _GEN_605; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_607 = 10'h24c == guard_index ? 64'h24c : _GEN_606; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_608 = 10'h24d == guard_index ? 64'h24d : _GEN_607; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_609 = 10'h24e == guard_index ? 64'h24e : _GEN_608; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_610 = 10'h24f == guard_index ? 64'h24f : _GEN_609; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_611 = 10'h250 == guard_index ? 64'h250 : _GEN_610; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_612 = 10'h251 == guard_index ? 64'h251 : _GEN_611; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_613 = 10'h252 == guard_index ? 64'h252 : _GEN_612; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_614 = 10'h253 == guard_index ? 64'h253 : _GEN_613; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_615 = 10'h254 == guard_index ? 64'h254 : _GEN_614; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_616 = 10'h255 == guard_index ? 64'h255 : _GEN_615; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_617 = 10'h256 == guard_index ? 64'h256 : _GEN_616; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_618 = 10'h257 == guard_index ? 64'h257 : _GEN_617; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_619 = 10'h258 == guard_index ? 64'h258 : _GEN_618; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_620 = 10'h259 == guard_index ? 64'h259 : _GEN_619; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_621 = 10'h25a == guard_index ? 64'h25a : _GEN_620; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_622 = 10'h25b == guard_index ? 64'h25b : _GEN_621; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_623 = 10'h25c == guard_index ? 64'h25c : _GEN_622; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_624 = 10'h25d == guard_index ? 64'h25d : _GEN_623; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_625 = 10'h25e == guard_index ? 64'h25e : _GEN_624; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_626 = 10'h25f == guard_index ? 64'h25f : _GEN_625; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_627 = 10'h260 == guard_index ? 64'h260 : _GEN_626; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_628 = 10'h261 == guard_index ? 64'h261 : _GEN_627; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_629 = 10'h262 == guard_index ? 64'h262 : _GEN_628; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_630 = 10'h263 == guard_index ? 64'h263 : _GEN_629; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_631 = 10'h264 == guard_index ? 64'h264 : _GEN_630; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_632 = 10'h265 == guard_index ? 64'h265 : _GEN_631; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_633 = 10'h266 == guard_index ? 64'h266 : _GEN_632; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_634 = 10'h267 == guard_index ? 64'h267 : _GEN_633; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_635 = 10'h268 == guard_index ? 64'h268 : _GEN_634; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_636 = 10'h269 == guard_index ? 64'h269 : _GEN_635; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_637 = 10'h26a == guard_index ? 64'h26a : _GEN_636; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_638 = 10'h26b == guard_index ? 64'h26b : _GEN_637; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_639 = 10'h26c == guard_index ? 64'h26c : _GEN_638; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_640 = 10'h26d == guard_index ? 64'h26d : _GEN_639; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_641 = 10'h26e == guard_index ? 64'h26e : _GEN_640; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_642 = 10'h26f == guard_index ? 64'h26f : _GEN_641; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_643 = 10'h270 == guard_index ? 64'h270 : _GEN_642; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_644 = 10'h271 == guard_index ? 64'h271 : _GEN_643; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_645 = 10'h272 == guard_index ? 64'h272 : _GEN_644; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_646 = 10'h273 == guard_index ? 64'h273 : _GEN_645; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_647 = 10'h274 == guard_index ? 64'h274 : _GEN_646; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_648 = 10'h275 == guard_index ? 64'h275 : _GEN_647; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_649 = 10'h276 == guard_index ? 64'h276 : _GEN_648; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_650 = 10'h277 == guard_index ? 64'h277 : _GEN_649; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_651 = 10'h278 == guard_index ? 64'h278 : _GEN_650; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_652 = 10'h279 == guard_index ? 64'h279 : _GEN_651; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_653 = 10'h27a == guard_index ? 64'h27a : _GEN_652; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_654 = 10'h27b == guard_index ? 64'h27b : _GEN_653; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_655 = 10'h27c == guard_index ? 64'h27c : _GEN_654; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_656 = 10'h27d == guard_index ? 64'h27d : _GEN_655; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_657 = 10'h27e == guard_index ? 64'h27e : _GEN_656; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_658 = 10'h27f == guard_index ? 64'h27f : _GEN_657; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_659 = 10'h280 == guard_index ? 64'h280 : _GEN_658; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_660 = 10'h281 == guard_index ? 64'h281 : _GEN_659; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_661 = 10'h282 == guard_index ? 64'h282 : _GEN_660; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_662 = 10'h283 == guard_index ? 64'h283 : _GEN_661; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_663 = 10'h284 == guard_index ? 64'h284 : _GEN_662; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_664 = 10'h285 == guard_index ? 64'h285 : _GEN_663; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_665 = 10'h286 == guard_index ? 64'h286 : _GEN_664; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_666 = 10'h287 == guard_index ? 64'h287 : _GEN_665; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_667 = 10'h288 == guard_index ? 64'h288 : _GEN_666; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_668 = 10'h289 == guard_index ? 64'h289 : _GEN_667; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_669 = 10'h28a == guard_index ? 64'h28a : _GEN_668; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_670 = 10'h28b == guard_index ? 64'h28b : _GEN_669; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_671 = 10'h28c == guard_index ? 64'h28c : _GEN_670; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_672 = 10'h28d == guard_index ? 64'h28d : _GEN_671; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_673 = 10'h28e == guard_index ? 64'h28e : _GEN_672; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_674 = 10'h28f == guard_index ? 64'h28f : _GEN_673; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_675 = 10'h290 == guard_index ? 64'h290 : _GEN_674; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_676 = 10'h291 == guard_index ? 64'h291 : _GEN_675; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_677 = 10'h292 == guard_index ? 64'h292 : _GEN_676; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_678 = 10'h293 == guard_index ? 64'h293 : _GEN_677; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_679 = 10'h294 == guard_index ? 64'h294 : _GEN_678; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_680 = 10'h295 == guard_index ? 64'h295 : _GEN_679; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_681 = 10'h296 == guard_index ? 64'h296 : _GEN_680; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_682 = 10'h297 == guard_index ? 64'h297 : _GEN_681; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_683 = 10'h298 == guard_index ? 64'h298 : _GEN_682; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_684 = 10'h299 == guard_index ? 64'h299 : _GEN_683; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_685 = 10'h29a == guard_index ? 64'h29a : _GEN_684; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_686 = 10'h29b == guard_index ? 64'h29b : _GEN_685; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_687 = 10'h29c == guard_index ? 64'h29c : _GEN_686; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_688 = 10'h29d == guard_index ? 64'h29d : _GEN_687; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_689 = 10'h29e == guard_index ? 64'h29e : _GEN_688; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_690 = 10'h29f == guard_index ? 64'h29f : _GEN_689; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_691 = 10'h2a0 == guard_index ? 64'h2a0 : _GEN_690; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_692 = 10'h2a1 == guard_index ? 64'h2a1 : _GEN_691; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_693 = 10'h2a2 == guard_index ? 64'h2a2 : _GEN_692; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_694 = 10'h2a3 == guard_index ? 64'h2a3 : _GEN_693; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_695 = 10'h2a4 == guard_index ? 64'h2a4 : _GEN_694; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_696 = 10'h2a5 == guard_index ? 64'h2a5 : _GEN_695; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_697 = 10'h2a6 == guard_index ? 64'h2a6 : _GEN_696; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_698 = 10'h2a7 == guard_index ? 64'h2a7 : _GEN_697; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_699 = 10'h2a8 == guard_index ? 64'h2a8 : _GEN_698; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_700 = 10'h2a9 == guard_index ? 64'h2a9 : _GEN_699; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_701 = 10'h2aa == guard_index ? 64'h2aa : _GEN_700; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_702 = 10'h2ab == guard_index ? 64'h2ab : _GEN_701; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_703 = 10'h2ac == guard_index ? 64'h2ac : _GEN_702; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_704 = 10'h2ad == guard_index ? 64'h2ad : _GEN_703; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_705 = 10'h2ae == guard_index ? 64'h2ae : _GEN_704; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_706 = 10'h2af == guard_index ? 64'h2af : _GEN_705; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_707 = 10'h2b0 == guard_index ? 64'h2b0 : _GEN_706; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_708 = 10'h2b1 == guard_index ? 64'h2b1 : _GEN_707; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_709 = 10'h2b2 == guard_index ? 64'h2b2 : _GEN_708; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_710 = 10'h2b3 == guard_index ? 64'h2b3 : _GEN_709; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_711 = 10'h2b4 == guard_index ? 64'h2b4 : _GEN_710; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_712 = 10'h2b5 == guard_index ? 64'h2b5 : _GEN_711; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_713 = 10'h2b6 == guard_index ? 64'h2b6 : _GEN_712; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_714 = 10'h2b7 == guard_index ? 64'h2b7 : _GEN_713; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_715 = 10'h2b8 == guard_index ? 64'h2b8 : _GEN_714; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_716 = 10'h2b9 == guard_index ? 64'h2b9 : _GEN_715; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_717 = 10'h2ba == guard_index ? 64'h2ba : _GEN_716; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_718 = 10'h2bb == guard_index ? 64'h2bb : _GEN_717; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_719 = 10'h2bc == guard_index ? 64'h2bc : _GEN_718; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_720 = 10'h2bd == guard_index ? 64'h2bd : _GEN_719; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_721 = 10'h2be == guard_index ? 64'h2be : _GEN_720; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_722 = 10'h2bf == guard_index ? 64'h2bf : _GEN_721; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_723 = 10'h2c0 == guard_index ? 64'h2c0 : _GEN_722; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_724 = 10'h2c1 == guard_index ? 64'h2c1 : _GEN_723; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_725 = 10'h2c2 == guard_index ? 64'h2c2 : _GEN_724; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_726 = 10'h2c3 == guard_index ? 64'h2c3 : _GEN_725; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_727 = 10'h2c4 == guard_index ? 64'h2c4 : _GEN_726; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_728 = 10'h2c5 == guard_index ? 64'h2c5 : _GEN_727; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_729 = 10'h2c6 == guard_index ? 64'h2c6 : _GEN_728; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_730 = 10'h2c7 == guard_index ? 64'h2c7 : _GEN_729; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_731 = 10'h2c8 == guard_index ? 64'h2c8 : _GEN_730; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_732 = 10'h2c9 == guard_index ? 64'h2c9 : _GEN_731; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_733 = 10'h2ca == guard_index ? 64'h2ca : _GEN_732; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_734 = 10'h2cb == guard_index ? 64'h2cb : _GEN_733; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_735 = 10'h2cc == guard_index ? 64'h2cc : _GEN_734; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_736 = 10'h2cd == guard_index ? 64'h2cd : _GEN_735; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_737 = 10'h2ce == guard_index ? 64'h2ce : _GEN_736; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_738 = 10'h2cf == guard_index ? 64'h2cf : _GEN_737; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_739 = 10'h2d0 == guard_index ? 64'h2d0 : _GEN_738; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_740 = 10'h2d1 == guard_index ? 64'h2d1 : _GEN_739; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_741 = 10'h2d2 == guard_index ? 64'h2d2 : _GEN_740; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_742 = 10'h2d3 == guard_index ? 64'h2d3 : _GEN_741; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_743 = 10'h2d4 == guard_index ? 64'h2d4 : _GEN_742; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_744 = 10'h2d5 == guard_index ? 64'h2d5 : _GEN_743; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_745 = 10'h2d6 == guard_index ? 64'h2d6 : _GEN_744; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_746 = 10'h2d7 == guard_index ? 64'h2d7 : _GEN_745; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_747 = 10'h2d8 == guard_index ? 64'h2d8 : _GEN_746; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_748 = 10'h2d9 == guard_index ? 64'h2d9 : _GEN_747; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_749 = 10'h2da == guard_index ? 64'h2da : _GEN_748; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_750 = 10'h2db == guard_index ? 64'h2db : _GEN_749; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_751 = 10'h2dc == guard_index ? 64'h2dc : _GEN_750; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_752 = 10'h2dd == guard_index ? 64'h2dd : _GEN_751; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_753 = 10'h2de == guard_index ? 64'h2de : _GEN_752; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_754 = 10'h2df == guard_index ? 64'h2df : _GEN_753; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_755 = 10'h2e0 == guard_index ? 64'h2e0 : _GEN_754; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_756 = 10'h2e1 == guard_index ? 64'h2e1 : _GEN_755; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_757 = 10'h2e2 == guard_index ? 64'h2e2 : _GEN_756; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_758 = 10'h2e3 == guard_index ? 64'h2e3 : _GEN_757; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_759 = 10'h2e4 == guard_index ? 64'h2e4 : _GEN_758; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_760 = 10'h2e5 == guard_index ? 64'h2e5 : _GEN_759; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_761 = 10'h2e6 == guard_index ? 64'h2e6 : _GEN_760; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_762 = 10'h2e7 == guard_index ? 64'h2e7 : _GEN_761; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_763 = 10'h2e8 == guard_index ? 64'h2e8 : _GEN_762; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_764 = 10'h2e9 == guard_index ? 64'h2e9 : _GEN_763; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_765 = 10'h2ea == guard_index ? 64'h2ea : _GEN_764; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_766 = 10'h2eb == guard_index ? 64'h2eb : _GEN_765; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_767 = 10'h2ec == guard_index ? 64'h2ec : _GEN_766; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_768 = 10'h2ed == guard_index ? 64'h2ed : _GEN_767; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_769 = 10'h2ee == guard_index ? 64'h2ee : _GEN_768; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_770 = 10'h2ef == guard_index ? 64'h2ef : _GEN_769; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_771 = 10'h2f0 == guard_index ? 64'h2f0 : _GEN_770; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_772 = 10'h2f1 == guard_index ? 64'h2f1 : _GEN_771; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_773 = 10'h2f2 == guard_index ? 64'h2f2 : _GEN_772; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_774 = 10'h2f3 == guard_index ? 64'h2f3 : _GEN_773; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_775 = 10'h2f4 == guard_index ? 64'h2f4 : _GEN_774; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_776 = 10'h2f5 == guard_index ? 64'h2f5 : _GEN_775; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_777 = 10'h2f6 == guard_index ? 64'h2f6 : _GEN_776; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_778 = 10'h2f7 == guard_index ? 64'h2f7 : _GEN_777; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_779 = 10'h2f8 == guard_index ? 64'h2f8 : _GEN_778; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_780 = 10'h2f9 == guard_index ? 64'h2f9 : _GEN_779; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_781 = 10'h2fa == guard_index ? 64'h2fa : _GEN_780; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_782 = 10'h2fb == guard_index ? 64'h2fb : _GEN_781; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_783 = 10'h2fc == guard_index ? 64'h2fc : _GEN_782; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_784 = 10'h2fd == guard_index ? 64'h2fd : _GEN_783; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_785 = 10'h2fe == guard_index ? 64'h2fe : _GEN_784; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_786 = 10'h2ff == guard_index ? 64'h2ff : _GEN_785; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_787 = 10'h300 == guard_index ? 64'h300 : _GEN_786; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_788 = 10'h301 == guard_index ? 64'h301 : _GEN_787; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_789 = 10'h302 == guard_index ? 64'h302 : _GEN_788; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_790 = 10'h303 == guard_index ? 64'h303 : _GEN_789; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_791 = 10'h304 == guard_index ? 64'h304 : _GEN_790; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_792 = 10'h305 == guard_index ? 64'h305 : _GEN_791; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_793 = 10'h306 == guard_index ? 64'h306 : _GEN_792; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_794 = 10'h307 == guard_index ? 64'h307 : _GEN_793; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_795 = 10'h308 == guard_index ? 64'h308 : _GEN_794; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_796 = 10'h309 == guard_index ? 64'h309 : _GEN_795; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_797 = 10'h30a == guard_index ? 64'h30a : _GEN_796; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_798 = 10'h30b == guard_index ? 64'h30b : _GEN_797; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_799 = 10'h30c == guard_index ? 64'h30c : _GEN_798; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_800 = 10'h30d == guard_index ? 64'h30d : _GEN_799; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_801 = 10'h30e == guard_index ? 64'h30e : _GEN_800; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_802 = 10'h30f == guard_index ? 64'h30f : _GEN_801; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_803 = 10'h310 == guard_index ? 64'h310 : _GEN_802; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_804 = 10'h311 == guard_index ? 64'h311 : _GEN_803; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_805 = 10'h312 == guard_index ? 64'h312 : _GEN_804; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_806 = 10'h313 == guard_index ? 64'h313 : _GEN_805; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_807 = 10'h314 == guard_index ? 64'h314 : _GEN_806; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_808 = 10'h315 == guard_index ? 64'h315 : _GEN_807; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_809 = 10'h316 == guard_index ? 64'h316 : _GEN_808; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_810 = 10'h317 == guard_index ? 64'h317 : _GEN_809; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_811 = 10'h318 == guard_index ? 64'h318 : _GEN_810; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_812 = 10'h319 == guard_index ? 64'h319 : _GEN_811; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_813 = 10'h31a == guard_index ? 64'h31a : _GEN_812; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_814 = 10'h31b == guard_index ? 64'h31b : _GEN_813; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_815 = 10'h31c == guard_index ? 64'h31c : _GEN_814; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_816 = 10'h31d == guard_index ? 64'h31d : _GEN_815; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_817 = 10'h31e == guard_index ? 64'h31e : _GEN_816; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_818 = 10'h31f == guard_index ? 64'h31f : _GEN_817; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_819 = 10'h320 == guard_index ? 64'h320 : _GEN_818; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_820 = 10'h321 == guard_index ? 64'h321 : _GEN_819; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_821 = 10'h322 == guard_index ? 64'h322 : _GEN_820; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_822 = 10'h323 == guard_index ? 64'h323 : _GEN_821; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_823 = 10'h324 == guard_index ? 64'h324 : _GEN_822; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_824 = 10'h325 == guard_index ? 64'h325 : _GEN_823; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_825 = 10'h326 == guard_index ? 64'h326 : _GEN_824; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_826 = 10'h327 == guard_index ? 64'h327 : _GEN_825; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_827 = 10'h328 == guard_index ? 64'h328 : _GEN_826; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_828 = 10'h329 == guard_index ? 64'h329 : _GEN_827; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_829 = 10'h32a == guard_index ? 64'h32a : _GEN_828; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_830 = 10'h32b == guard_index ? 64'h32b : _GEN_829; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_831 = 10'h32c == guard_index ? 64'h32c : _GEN_830; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_832 = 10'h32d == guard_index ? 64'h32d : _GEN_831; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_833 = 10'h32e == guard_index ? 64'h32e : _GEN_832; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_834 = 10'h32f == guard_index ? 64'h32f : _GEN_833; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_835 = 10'h330 == guard_index ? 64'h330 : _GEN_834; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_836 = 10'h331 == guard_index ? 64'h331 : _GEN_835; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_837 = 10'h332 == guard_index ? 64'h332 : _GEN_836; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_838 = 10'h333 == guard_index ? 64'h333 : _GEN_837; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_839 = 10'h334 == guard_index ? 64'h334 : _GEN_838; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_840 = 10'h335 == guard_index ? 64'h335 : _GEN_839; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_841 = 10'h336 == guard_index ? 64'h336 : _GEN_840; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_842 = 10'h337 == guard_index ? 64'h337 : _GEN_841; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_843 = 10'h338 == guard_index ? 64'h338 : _GEN_842; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_844 = 10'h339 == guard_index ? 64'h339 : _GEN_843; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_845 = 10'h33a == guard_index ? 64'h33a : _GEN_844; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_846 = 10'h33b == guard_index ? 64'h33b : _GEN_845; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_847 = 10'h33c == guard_index ? 64'h33c : _GEN_846; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_848 = 10'h33d == guard_index ? 64'h33d : _GEN_847; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_849 = 10'h33e == guard_index ? 64'h33e : _GEN_848; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_850 = 10'h33f == guard_index ? 64'h33f : _GEN_849; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_851 = 10'h340 == guard_index ? 64'h340 : _GEN_850; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_852 = 10'h341 == guard_index ? 64'h341 : _GEN_851; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_853 = 10'h342 == guard_index ? 64'h342 : _GEN_852; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_854 = 10'h343 == guard_index ? 64'h343 : _GEN_853; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_855 = 10'h344 == guard_index ? 64'h344 : _GEN_854; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_856 = 10'h345 == guard_index ? 64'h345 : _GEN_855; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_857 = 10'h346 == guard_index ? 64'h346 : _GEN_856; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_858 = 10'h347 == guard_index ? 64'h347 : _GEN_857; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_859 = 10'h348 == guard_index ? 64'h348 : _GEN_858; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_860 = 10'h349 == guard_index ? 64'h349 : _GEN_859; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_861 = 10'h34a == guard_index ? 64'h34a : _GEN_860; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_862 = 10'h34b == guard_index ? 64'h34b : _GEN_861; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_863 = 10'h34c == guard_index ? 64'h34c : _GEN_862; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_864 = 10'h34d == guard_index ? 64'h34d : _GEN_863; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_865 = 10'h34e == guard_index ? 64'h34e : _GEN_864; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_866 = 10'h34f == guard_index ? 64'h34f : _GEN_865; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_867 = 10'h350 == guard_index ? 64'h350 : _GEN_866; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_868 = 10'h351 == guard_index ? 64'h351 : _GEN_867; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_869 = 10'h352 == guard_index ? 64'h352 : _GEN_868; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_870 = 10'h353 == guard_index ? 64'h353 : _GEN_869; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_871 = 10'h354 == guard_index ? 64'h354 : _GEN_870; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_872 = 10'h355 == guard_index ? 64'h355 : _GEN_871; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_873 = 10'h356 == guard_index ? 64'h356 : _GEN_872; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_874 = 10'h357 == guard_index ? 64'h357 : _GEN_873; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_875 = 10'h358 == guard_index ? 64'h358 : _GEN_874; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_876 = 10'h359 == guard_index ? 64'h359 : _GEN_875; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_877 = 10'h35a == guard_index ? 64'h35a : _GEN_876; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_878 = 10'h35b == guard_index ? 64'h35b : _GEN_877; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_879 = 10'h35c == guard_index ? 64'h35c : _GEN_878; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_880 = 10'h35d == guard_index ? 64'h35d : _GEN_879; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_881 = 10'h35e == guard_index ? 64'h35e : _GEN_880; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_882 = 10'h35f == guard_index ? 64'h35f : _GEN_881; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_883 = 10'h360 == guard_index ? 64'h360 : _GEN_882; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_884 = 10'h361 == guard_index ? 64'h361 : _GEN_883; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_885 = 10'h362 == guard_index ? 64'h362 : _GEN_884; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_886 = 10'h363 == guard_index ? 64'h363 : _GEN_885; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_887 = 10'h364 == guard_index ? 64'h364 : _GEN_886; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_888 = 10'h365 == guard_index ? 64'h365 : _GEN_887; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_889 = 10'h366 == guard_index ? 64'h366 : _GEN_888; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_890 = 10'h367 == guard_index ? 64'h367 : _GEN_889; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_891 = 10'h368 == guard_index ? 64'h368 : _GEN_890; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_892 = 10'h369 == guard_index ? 64'h369 : _GEN_891; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_893 = 10'h36a == guard_index ? 64'h36a : _GEN_892; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_894 = 10'h36b == guard_index ? 64'h36b : _GEN_893; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_895 = 10'h36c == guard_index ? 64'h36c : _GEN_894; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_896 = 10'h36d == guard_index ? 64'h36d : _GEN_895; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_897 = 10'h36e == guard_index ? 64'h36e : _GEN_896; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_898 = 10'h36f == guard_index ? 64'h36f : _GEN_897; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_899 = 10'h370 == guard_index ? 64'h370 : _GEN_898; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_900 = 10'h371 == guard_index ? 64'h371 : _GEN_899; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_901 = 10'h372 == guard_index ? 64'h372 : _GEN_900; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_902 = 10'h373 == guard_index ? 64'h373 : _GEN_901; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_903 = 10'h374 == guard_index ? 64'h374 : _GEN_902; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_904 = 10'h375 == guard_index ? 64'h375 : _GEN_903; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_905 = 10'h376 == guard_index ? 64'h376 : _GEN_904; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_906 = 10'h377 == guard_index ? 64'h377 : _GEN_905; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_907 = 10'h378 == guard_index ? 64'h378 : _GEN_906; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_908 = 10'h379 == guard_index ? 64'h379 : _GEN_907; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_909 = 10'h37a == guard_index ? 64'h37a : _GEN_908; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_910 = 10'h37b == guard_index ? 64'h37b : _GEN_909; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_911 = 10'h37c == guard_index ? 64'h37c : _GEN_910; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_912 = 10'h37d == guard_index ? 64'h37d : _GEN_911; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_913 = 10'h37e == guard_index ? 64'h37e : _GEN_912; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_914 = 10'h37f == guard_index ? 64'h37f : _GEN_913; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_915 = 10'h380 == guard_index ? 64'h380 : _GEN_914; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_916 = 10'h381 == guard_index ? 64'h381 : _GEN_915; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_917 = 10'h382 == guard_index ? 64'h382 : _GEN_916; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_918 = 10'h383 == guard_index ? 64'h383 : _GEN_917; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_919 = 10'h384 == guard_index ? 64'h384 : _GEN_918; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_920 = 10'h385 == guard_index ? 64'h385 : _GEN_919; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_921 = 10'h386 == guard_index ? 64'h386 : _GEN_920; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_922 = 10'h387 == guard_index ? 64'h387 : _GEN_921; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_923 = 10'h388 == guard_index ? 64'h388 : _GEN_922; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_924 = 10'h389 == guard_index ? 64'h389 : _GEN_923; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_925 = 10'h38a == guard_index ? 64'h38a : _GEN_924; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_926 = 10'h38b == guard_index ? 64'h38b : _GEN_925; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_927 = 10'h38c == guard_index ? 64'h38c : _GEN_926; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_928 = 10'h38d == guard_index ? 64'h38d : _GEN_927; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_929 = 10'h38e == guard_index ? 64'h38e : _GEN_928; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_930 = 10'h38f == guard_index ? 64'h38f : _GEN_929; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_931 = 10'h390 == guard_index ? 64'h390 : _GEN_930; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_932 = 10'h391 == guard_index ? 64'h391 : _GEN_931; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_933 = 10'h392 == guard_index ? 64'h392 : _GEN_932; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_934 = 10'h393 == guard_index ? 64'h393 : _GEN_933; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_935 = 10'h394 == guard_index ? 64'h394 : _GEN_934; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_936 = 10'h395 == guard_index ? 64'h395 : _GEN_935; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_937 = 10'h396 == guard_index ? 64'h396 : _GEN_936; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_938 = 10'h397 == guard_index ? 64'h397 : _GEN_937; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_939 = 10'h398 == guard_index ? 64'h398 : _GEN_938; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_940 = 10'h399 == guard_index ? 64'h399 : _GEN_939; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_941 = 10'h39a == guard_index ? 64'h39a : _GEN_940; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_942 = 10'h39b == guard_index ? 64'h39b : _GEN_941; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_943 = 10'h39c == guard_index ? 64'h39c : _GEN_942; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_944 = 10'h39d == guard_index ? 64'h39d : _GEN_943; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_945 = 10'h39e == guard_index ? 64'h39e : _GEN_944; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_946 = 10'h39f == guard_index ? 64'h39f : _GEN_945; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_947 = 10'h3a0 == guard_index ? 64'h3a0 : _GEN_946; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_948 = 10'h3a1 == guard_index ? 64'h3a1 : _GEN_947; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_949 = 10'h3a2 == guard_index ? 64'h3a2 : _GEN_948; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_950 = 10'h3a3 == guard_index ? 64'h3a3 : _GEN_949; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_951 = 10'h3a4 == guard_index ? 64'h3a4 : _GEN_950; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_952 = 10'h3a5 == guard_index ? 64'h3a5 : _GEN_951; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_953 = 10'h3a6 == guard_index ? 64'h3a6 : _GEN_952; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_954 = 10'h3a7 == guard_index ? 64'h3a7 : _GEN_953; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_955 = 10'h3a8 == guard_index ? 64'h3a8 : _GEN_954; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_956 = 10'h3a9 == guard_index ? 64'h3a9 : _GEN_955; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_957 = 10'h3aa == guard_index ? 64'h3aa : _GEN_956; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_958 = 10'h3ab == guard_index ? 64'h3ab : _GEN_957; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_959 = 10'h3ac == guard_index ? 64'h3ac : _GEN_958; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_960 = 10'h3ad == guard_index ? 64'h3ad : _GEN_959; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_961 = 10'h3ae == guard_index ? 64'h3ae : _GEN_960; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_962 = 10'h3af == guard_index ? 64'h3af : _GEN_961; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_963 = 10'h3b0 == guard_index ? 64'h3b0 : _GEN_962; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_964 = 10'h3b1 == guard_index ? 64'h3b1 : _GEN_963; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_965 = 10'h3b2 == guard_index ? 64'h3b2 : _GEN_964; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_966 = 10'h3b3 == guard_index ? 64'h3b3 : _GEN_965; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_967 = 10'h3b4 == guard_index ? 64'h3b4 : _GEN_966; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_968 = 10'h3b5 == guard_index ? 64'h3b5 : _GEN_967; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_969 = 10'h3b6 == guard_index ? 64'h3b6 : _GEN_968; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_970 = 10'h3b7 == guard_index ? 64'h3b7 : _GEN_969; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_971 = 10'h3b8 == guard_index ? 64'h3b8 : _GEN_970; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_972 = 10'h3b9 == guard_index ? 64'h3b9 : _GEN_971; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_973 = 10'h3ba == guard_index ? 64'h3ba : _GEN_972; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_974 = 10'h3bb == guard_index ? 64'h3bb : _GEN_973; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_975 = 10'h3bc == guard_index ? 64'h3bc : _GEN_974; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_976 = 10'h3bd == guard_index ? 64'h3bd : _GEN_975; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_977 = 10'h3be == guard_index ? 64'h3be : _GEN_976; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_978 = 10'h3bf == guard_index ? 64'h3bf : _GEN_977; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_979 = 10'h3c0 == guard_index ? 64'h3c0 : _GEN_978; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_980 = 10'h3c1 == guard_index ? 64'h3c1 : _GEN_979; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_981 = 10'h3c2 == guard_index ? 64'h3c2 : _GEN_980; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_982 = 10'h3c3 == guard_index ? 64'h3c3 : _GEN_981; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_983 = 10'h3c4 == guard_index ? 64'h3c4 : _GEN_982; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_984 = 10'h3c5 == guard_index ? 64'h3c5 : _GEN_983; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_985 = 10'h3c6 == guard_index ? 64'h3c6 : _GEN_984; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_986 = 10'h3c7 == guard_index ? 64'h3c7 : _GEN_985; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_987 = 10'h3c8 == guard_index ? 64'h3c8 : _GEN_986; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_988 = 10'h3c9 == guard_index ? 64'h3c9 : _GEN_987; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_989 = 10'h3ca == guard_index ? 64'h3ca : _GEN_988; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_990 = 10'h3cb == guard_index ? 64'h3cb : _GEN_989; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_991 = 10'h3cc == guard_index ? 64'h3cc : _GEN_990; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_992 = 10'h3cd == guard_index ? 64'h3cd : _GEN_991; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_993 = 10'h3ce == guard_index ? 64'h3ce : _GEN_992; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_994 = 10'h3cf == guard_index ? 64'h3cf : _GEN_993; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_995 = 10'h3d0 == guard_index ? 64'h3d0 : _GEN_994; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_996 = 10'h3d1 == guard_index ? 64'h3d1 : _GEN_995; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_997 = 10'h3d2 == guard_index ? 64'h3d2 : _GEN_996; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_998 = 10'h3d3 == guard_index ? 64'h3d3 : _GEN_997; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_999 = 10'h3d4 == guard_index ? 64'h3d4 : _GEN_998; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1000 = 10'h3d5 == guard_index ? 64'h3d5 : _GEN_999; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1001 = 10'h3d6 == guard_index ? 64'h3d6 : _GEN_1000; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1002 = 10'h3d7 == guard_index ? 64'h3d7 : _GEN_1001; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1003 = 10'h3d8 == guard_index ? 64'h3d8 : _GEN_1002; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1004 = 10'h3d9 == guard_index ? 64'h3d9 : _GEN_1003; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1005 = 10'h3da == guard_index ? 64'h3da : _GEN_1004; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1006 = 10'h3db == guard_index ? 64'h3db : _GEN_1005; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1007 = 10'h3dc == guard_index ? 64'h3dc : _GEN_1006; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1008 = 10'h3dd == guard_index ? 64'h3dd : _GEN_1007; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1009 = 10'h3de == guard_index ? 64'h3de : _GEN_1008; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1010 = 10'h3df == guard_index ? 64'h3df : _GEN_1009; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1011 = 10'h3e0 == guard_index ? 64'h3e0 : _GEN_1010; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1012 = 10'h3e1 == guard_index ? 64'h3e1 : _GEN_1011; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1013 = 10'h3e2 == guard_index ? 64'h3e2 : _GEN_1012; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1014 = 10'h3e3 == guard_index ? 64'h3e3 : _GEN_1013; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1015 = 10'h3e4 == guard_index ? 64'h3e4 : _GEN_1014; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1016 = 10'h3e5 == guard_index ? 64'h3e5 : _GEN_1015; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1017 = 10'h3e6 == guard_index ? 64'h3e6 : _GEN_1016; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1018 = 10'h3e7 == guard_index ? 64'h3e7 : _GEN_1017; // @[ComputeNode.scala 155:26]
  wire  _T_36 = FU_io_out != _GEN_1018; // @[ComputeNode.scala 155:26]
  wire  _T_22 = right_valid_R & left_valid_R; // @[ComputeNode.scala 71:19]
  wire  _T_23 = enable_valid_R & _T_22; // @[ComputeNode.scala 75:20]
  wire  _T_24 = _T_23 & enable_R_control; // @[ComputeNode.scala 75:38]
  wire  _T_26 = _T_24 & _T_32; // @[ComputeNode.scala 75:58]
  wire  _T_28 = guard_index == 10'h3e7; // @[Counter.scala 38:24]
  wire [9:0] _T_30 = guard_index + 10'h1; // @[Counter.scala 39:22]
  wire [63:0] _T_38_data = FU_io_out; // @[interfaces.scala 289:20 interfaces.scala 290:15]
  wire [63:0] _GEN_1021 = _T_36 ? _GEN_1018 : _T_38_data; // @[ComputeNode.scala 155:61]
  wire  _T_40 = _T_1 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire [63:0] _GEN_1026 = _T_34 ? _GEN_1021 : out_data_R; // @[ComputeNode.scala 147:81]
  wire  _GEN_1029 = _T_34 | out_valid_R_0; // @[ComputeNode.scala 147:81]
  wire  _GEN_1033 = _T_34 | state; // @[ComputeNode.scala 147:81]
  wire  _T_43 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  UALU FU ( // @[ComputeNode.scala 61:18]
    .io_in1(FU_io_in1),
    .io_out(FU_io_out)
  );
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 205:19]
  assign io_Out_0_valid = _T_32 ? _GEN_1029 : out_valid_R_0; // @[HandShaking.scala 194:21 ComputeNode.scala 172:32]
  assign io_Out_0_bits_data = _T_32 ? _GEN_1026 : out_data_R; // @[ComputeNode.scala 137:25 ComputeNode.scala 158:35 ComputeNode.scala 166:35]
  assign io_LeftIO_ready = ~left_valid_R; // @[ComputeNode.scala 104:19]
  assign io_RightIO_ready = ~right_valid_R; // @[ComputeNode.scala 110:20]
  assign FU_io_in1 = left_R_data; // @[ComputeNode.scala 101:13]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {2{`RANDOM}};
  left_R_data = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  left_valid_R = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  right_valid_R = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  state = _RAND_7[0:0];
  _RAND_8 = {2{`RANDOM}};
  out_data_R = _RAND_8[63:0];
  _RAND_9 = {1{`RANDOM}};
  guard_index = _RAND_9[9:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_3) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_43) begin
        enable_valid_R <= 1'h0;
      end else if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_3) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_32) begin
      if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_43) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_1) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        out_valid_R_0 <= _T_40;
      end else if (_T_1) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_1) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      left_R_data <= 64'h0;
    end else if (_T_12) begin
      left_R_data <= io_LeftIO_bits_data;
    end
    if (reset) begin
      left_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        left_valid_R <= 1'h0;
      end else begin
        left_valid_R <= _GEN_11;
      end
    end else begin
      left_valid_R <= _GEN_11;
    end
    if (reset) begin
      right_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        right_valid_R <= 1'h0;
      end else begin
        right_valid_R <= _GEN_15;
      end
    end else begin
      right_valid_R <= _GEN_15;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_32) begin
      state <= _GEN_1033;
    end else if (state) begin
      if (_T_43) begin
        state <= 1'h0;
      end
    end
    if (reset) begin
      out_data_R <= 64'h0;
    end else if (_T_32) begin
      if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (state) begin
      if (_T_43) begin
        out_data_R <= 64'h0;
      end else if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (enable_R_control) begin
      out_data_R <= FU_io_out;
    end else begin
      out_data_R <= 64'h0;
    end
    if (reset) begin
      guard_index <= 10'h0;
    end else if (_T_26) begin
      if (_T_28) begin
        guard_index <= 10'h0;
      end else begin
        guard_index <= _T_30;
      end
    end
  end
endmodule
module CBranchNodeVariable(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  output        io_CmpIO_ready,
  input         io_CmpIO_valid,
  input  [63:0] io_CmpIO_bits_data,
  input         io_TrueOutput_0_ready,
  output        io_TrueOutput_0_valid,
  output        io_TrueOutput_0_bits_control,
  input         io_FalseOutput_0_ready,
  output        io_FalseOutput_0_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
`endif // RANDOMIZE_REG_INIT
  reg  cmp_R_control; // @[BranchNode.scala 1182:22]
  reg  cmp_valid; // @[BranchNode.scala 1183:26]
  reg  enable_R_control; // @[BranchNode.scala 1186:25]
  reg  enable_valid_R; // @[BranchNode.scala 1187:31]
  reg  output_true_R_control; // @[BranchNode.scala 1193:30]
  reg  output_true_valid_R_0; // @[BranchNode.scala 1194:54]
  reg  fire_true_R_0; // @[BranchNode.scala 1195:46]
  reg  output_false_valid_R_0; // @[BranchNode.scala 1198:56]
  reg  fire_false_R_0; // @[BranchNode.scala 1199:48]
  wire  _T_9 = io_CmpIO_ready & io_CmpIO_valid; // @[Decoupled.scala 40:37]
  wire  _T_10 = |io_CmpIO_bits_data; // @[BranchNode.scala 1207:44]
  wire  _GEN_4 = _T_9 | cmp_valid; // @[BranchNode.scala 1206:23]
  wire  _T_12 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_8 = _T_12 | enable_valid_R; // @[BranchNode.scala 1232:24]
  wire  true_output = enable_R_control & cmp_R_control; // @[BranchNode.scala 1238:38]
  wire  _T_14 = io_TrueOutput_0_ready & io_TrueOutput_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_9 = _T_14 | fire_true_R_0; // @[BranchNode.scala 1250:33]
  wire  _GEN_10 = _T_14 ? 1'h0 : output_true_valid_R_0; // @[BranchNode.scala 1250:33]
  wire  _T_15 = io_FalseOutput_0_ready & io_FalseOutput_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_15 | fire_false_R_0; // @[BranchNode.scala 1266:34]
  wire  _GEN_12 = _T_15 ? 1'h0 : output_false_valid_R_0; // @[BranchNode.scala 1266:34]
  reg  state; // @[BranchNode.scala 1278:22]
  wire  _T_16 = ~state; // @[Conditional.scala 37:30]
  wire  _T_17 = enable_valid_R & cmp_valid; // @[BranchNode.scala 1283:27]
  wire  _GEN_13 = _T_17 | _GEN_10; // @[BranchNode.scala 1283:65]
  wire  _GEN_14 = _T_17 | _GEN_12; // @[BranchNode.scala 1283:65]
  wire  _GEN_15 = _T_17 | state; // @[BranchNode.scala 1283:65]
  wire  _T_20 = fire_true_R_0 & fire_false_R_0; // @[BranchNode.scala 1313:27]
  assign io_enable_ready = ~enable_valid_R; // @[BranchNode.scala 1231:19]
  assign io_CmpIO_ready = ~cmp_valid; // @[BranchNode.scala 1205:18]
  assign io_TrueOutput_0_valid = output_true_valid_R_0; // @[BranchNode.scala 1246:28]
  assign io_TrueOutput_0_bits_control = output_true_R_control; // @[BranchNode.scala 1245:27]
  assign io_FalseOutput_0_valid = output_false_valid_R_0; // @[BranchNode.scala 1262:29]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  cmp_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  cmp_valid = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  enable_R_control = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  enable_valid_R = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  output_true_R_control = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  output_true_valid_R_0 = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  fire_true_R_0 = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  output_false_valid_R_0 = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  fire_false_R_0 = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  state = _RAND_9[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      cmp_R_control <= 1'h0;
    end else if (_T_16) begin
      if (_T_9) begin
        cmp_R_control <= _T_10;
      end
    end else if (state) begin
      if (_T_20) begin
        cmp_R_control <= 1'h0;
      end else if (_T_9) begin
        cmp_R_control <= _T_10;
      end
    end else if (_T_9) begin
      cmp_R_control <= _T_10;
    end
    if (reset) begin
      cmp_valid <= 1'h0;
    end else if (_T_16) begin
      cmp_valid <= _GEN_4;
    end else if (state) begin
      if (_T_20) begin
        cmp_valid <= 1'h0;
      end else begin
        cmp_valid <= _GEN_4;
      end
    end else begin
      cmp_valid <= _GEN_4;
    end
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_16) begin
      if (_T_12) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (state) begin
      if (_T_20) begin
        enable_R_control <= 1'h0;
      end else if (_T_12) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_12) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_16) begin
      enable_valid_R <= _GEN_8;
    end else if (state) begin
      if (_T_20) begin
        enable_valid_R <= 1'h0;
      end else begin
        enable_valid_R <= _GEN_8;
      end
    end else begin
      enable_valid_R <= _GEN_8;
    end
    if (reset) begin
      output_true_R_control <= 1'h0;
    end else if (_T_16) begin
      output_true_R_control <= true_output;
    end else if (state) begin
      if (_T_20) begin
        output_true_R_control <= 1'h0;
      end else begin
        output_true_R_control <= true_output;
      end
    end else begin
      output_true_R_control <= true_output;
    end
    if (reset) begin
      output_true_valid_R_0 <= 1'h0;
    end else if (_T_16) begin
      output_true_valid_R_0 <= _GEN_13;
    end else if (state) begin
      if (_T_20) begin
        output_true_valid_R_0 <= 1'h0;
      end else if (_T_14) begin
        output_true_valid_R_0 <= 1'h0;
      end
    end else if (_T_14) begin
      output_true_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      fire_true_R_0 <= 1'h0;
    end else if (_T_16) begin
      fire_true_R_0 <= _GEN_9;
    end else if (state) begin
      if (_T_20) begin
        fire_true_R_0 <= 1'h0;
      end else begin
        fire_true_R_0 <= _GEN_9;
      end
    end else begin
      fire_true_R_0 <= _GEN_9;
    end
    if (reset) begin
      output_false_valid_R_0 <= 1'h0;
    end else if (_T_16) begin
      output_false_valid_R_0 <= _GEN_14;
    end else if (state) begin
      if (_T_20) begin
        output_false_valid_R_0 <= 1'h0;
      end else if (_T_15) begin
        output_false_valid_R_0 <= 1'h0;
      end
    end else if (_T_15) begin
      output_false_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      fire_false_R_0 <= 1'h0;
    end else if (_T_16) begin
      fire_false_R_0 <= _GEN_11;
    end else if (state) begin
      if (_T_20) begin
        fire_false_R_0 <= 1'h0;
      end else begin
        fire_false_R_0 <= _GEN_11;
      end
    end else begin
      fire_false_R_0 <= _GEN_11;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_16) begin
      state <= _GEN_15;
    end else if (state) begin
      if (_T_20) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module ZextNode(
  input         clock,
  input         reset,
  output        io_Input_ready,
  input         io_Input_valid,
  input  [63:0] io_Input_bits_data,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [63:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
`endif // RANDOMIZE_REG_INIT
  reg [63:0] input_R_data; // @[ZextNode.scala 42:24]
  reg  input_valid_R; // @[ZextNode.scala 43:30]
  reg  enable_valid_R; // @[ZextNode.scala 46:31]
  reg  output_valid_R_0; // @[ZextNode.scala 48:49]
  reg  fire_R_0; // @[ZextNode.scala 50:41]
  wire  _T_7 = io_Input_ready & io_Input_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_5 = _T_7 | input_valid_R; // @[ZextNode.scala 60:25]
  wire  _T_9 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_9 = _T_9 | enable_valid_R; // @[ZextNode.scala 66:26]
  wire  _T_11 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_10 = _T_11 ? 1'h0 : output_valid_R_0; // @[ZextNode.scala 80:26]
  wire  _GEN_11 = _T_11 | fire_R_0; // @[ZextNode.scala 80:26]
  wire  fire_mask_0 = fire_R_0 | _T_11; // @[ZextNode.scala 86:74]
  reg  state; // @[ZextNode.scala 101:22]
  wire  _T_13 = ~state; // @[Conditional.scala 37:30]
  wire  _T_15 = enable_valid_R | _T_9; // @[ZextNode.scala 89:20]
  wire  _T_17 = input_valid_R | _T_7; // @[ZextNode.scala 93:19]
  wire  _T_18 = _T_15 & _T_17; // @[ZextNode.scala 106:28]
  wire  _GEN_12 = _T_18 | output_valid_R_0; // @[ZextNode.scala 106:47]
  wire  _GEN_13 = _T_18 | _GEN_10; // @[ZextNode.scala 106:47]
  wire  _GEN_14 = _T_18 | state; // @[ZextNode.scala 106:47]
  assign io_Input_ready = ~input_valid_R; // @[ZextNode.scala 59:18]
  assign io_enable_ready = ~enable_valid_R; // @[ZextNode.scala 65:19]
  assign io_Out_0_valid = _T_13 ? _GEN_12 : output_valid_R_0; // @[ZextNode.scala 76:21 ZextNode.scala 108:32]
  assign io_Out_0_bits_data = _T_7 ? io_Input_bits_data : input_R_data; // @[ZextNode.scala 75:20]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {2{`RANDOM}};
  input_R_data = _RAND_0[63:0];
  _RAND_1 = {1{`RANDOM}};
  input_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  enable_valid_R = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  output_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  fire_R_0 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  state = _RAND_5[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      input_R_data <= 64'h0;
    end else if (_T_13) begin
      if (_T_7) begin
        input_R_data <= io_Input_bits_data;
      end
    end else if (state) begin
      if (fire_mask_0) begin
        input_R_data <= 64'h0;
      end else if (_T_7) begin
        input_R_data <= io_Input_bits_data;
      end
    end else if (_T_7) begin
      input_R_data <= io_Input_bits_data;
    end
    if (reset) begin
      input_valid_R <= 1'h0;
    end else if (_T_13) begin
      input_valid_R <= _GEN_5;
    end else if (state) begin
      if (fire_mask_0) begin
        input_valid_R <= 1'h0;
      end else begin
        input_valid_R <= _GEN_5;
      end
    end else begin
      input_valid_R <= _GEN_5;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_13) begin
      enable_valid_R <= _GEN_9;
    end else if (state) begin
      if (fire_mask_0) begin
        enable_valid_R <= 1'h0;
      end else begin
        enable_valid_R <= _GEN_9;
      end
    end else begin
      enable_valid_R <= _GEN_9;
    end
    if (reset) begin
      output_valid_R_0 <= 1'h0;
    end else if (_T_13) begin
      output_valid_R_0 <= _GEN_13;
    end else if (state) begin
      if (fire_mask_0) begin
        output_valid_R_0 <= 1'h0;
      end else if (_T_11) begin
        output_valid_R_0 <= 1'h0;
      end
    end else if (_T_11) begin
      output_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      fire_R_0 <= 1'h0;
    end else if (_T_13) begin
      fire_R_0 <= _GEN_11;
    end else if (state) begin
      if (fire_mask_0) begin
        fire_R_0 <= 1'h0;
      end else begin
        fire_R_0 <= _GEN_11;
      end
    end else begin
      fire_R_0 <= _GEN_11;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_13) begin
      state <= _GEN_14;
    end else if (state) begin
      if (fire_mask_0) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module UBranchNode(
  input   clock,
  input   reset,
  output  io_enable_ready,
  input   io_enable_valid,
  input   io_enable_bits_control,
  input   io_Out_0_ready,
  output  io_Out_0_valid,
  output  io_Out_0_bits_control
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
`endif // RANDOMIZE_REG_INIT
  reg  enable_R_control; // @[HandShaking.scala 592:31]
  reg  enable_valid_R; // @[HandShaking.scala 593:31]
  reg  out_ready_R_0; // @[HandShaking.scala 605:28]
  reg  out_valid_R_0; // @[HandShaking.scala 606:28]
  wire  _T_4 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_6 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg  state; // @[BranchNode.scala 588:22]
  wire  _T_11 = ~state; // @[Conditional.scala 37:30]
  wire  _T_14 = _T_4 ^ 1'h1; // @[HandShaking.scala 729:72]
  wire  _GEN_8 = enable_valid_R | state; // @[BranchNode.scala 611:46]
  wire  _GEN_10 = enable_valid_R | out_valid_R_0; // @[BranchNode.scala 611:46]
  wire  _T_16 = &out_ready_R_0; // @[HandShaking.scala 725:24]
  wire  _T_17 = &io_Out_0_ready; // @[HandShaking.scala 725:50]
  wire  _T_18 = _T_16 | _T_17; // @[HandShaking.scala 725:29]
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 650:19]
  assign io_Out_0_valid = _T_11 ? _GEN_10 : out_valid_R_0; // @[HandShaking.scala 630:21 BranchNode.scala 614:32]
  assign io_Out_0_bits_control = enable_R_control; // @[BranchNode.scala 607:25]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  state = _RAND_4[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_11) begin
      if (_T_6) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (state) begin
      if (_T_18) begin
        enable_R_control <= 1'h0;
      end else if (_T_6) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_6) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_11) begin
      if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_18) begin
        enable_valid_R <= 1'h0;
      end else if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_6) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_11) begin
      if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_18) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_4) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_11) begin
      if (enable_valid_R) begin
        out_valid_R_0 <= _T_14;
      end else if (_T_4) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_4) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_11) begin
      state <= _GEN_8;
    end else if (state) begin
      if (_T_18) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module UBranchNode_1(
  input   clock,
  input   reset,
  output  io_enable_ready,
  input   io_enable_valid,
  input   io_Out_0_ready,
  output  io_Out_0_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
`endif // RANDOMIZE_REG_INIT
  reg  enable_valid_R; // @[HandShaking.scala 593:31]
  reg  out_ready_R_0; // @[HandShaking.scala 605:28]
  reg  out_valid_R_0; // @[HandShaking.scala 606:28]
  wire  _T_4 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_6 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg  state; // @[BranchNode.scala 588:22]
  wire  _T_11 = ~state; // @[Conditional.scala 37:30]
  wire  _T_14 = _T_4 ^ 1'h1; // @[HandShaking.scala 729:72]
  wire  _GEN_8 = enable_valid_R | state; // @[BranchNode.scala 611:46]
  wire  _GEN_10 = enable_valid_R | out_valid_R_0; // @[BranchNode.scala 611:46]
  wire  _T_16 = &out_ready_R_0; // @[HandShaking.scala 725:24]
  wire  _T_17 = &io_Out_0_ready; // @[HandShaking.scala 725:50]
  wire  _T_18 = _T_16 | _T_17; // @[HandShaking.scala 725:29]
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 650:19]
  assign io_Out_0_valid = _T_11 ? _GEN_10 : out_valid_R_0; // @[HandShaking.scala 630:21 BranchNode.scala 614:32]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_valid_R = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  state = _RAND_3[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_11) begin
      if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_18) begin
        enable_valid_R <= 1'h0;
      end else if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_6) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_11) begin
      if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_18) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_4) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_11) begin
      if (enable_valid_R) begin
        out_valid_R_0 <= _T_14;
      end else if (_T_4) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_4) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_11) begin
      state <= _GEN_8;
    end else if (state) begin
      if (_T_18) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module RetNode2(
  input   clock,
  input   reset,
  output  io_In_enable_ready,
  input   io_In_enable_valid,
  output  io_In_data_field0_ready,
  input   io_In_data_field0_valid,
  input   io_Out_ready,
  output  io_Out_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
`endif // RANDOMIZE_REG_INIT
  reg  state; // @[RetNode.scala 141:22]
  reg  enable_valid_R; // @[RetNode.scala 144:31]
  reg  in_data_valid_R_0; // @[RetNode.scala 147:58]
  reg  out_ready_R; // @[RetNode.scala 151:28]
  reg  out_valid_R; // @[RetNode.scala 152:28]
  wire  _T_6 = io_In_enable_ready & io_In_enable_valid; // @[Decoupled.scala 40:37]
  wire  _T_8 = io_In_data_field0_ready & io_In_data_field0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_9 = _T_8 | in_data_valid_R_0; // @[RetNode.scala 172:41]
  wire  _T_9 = io_Out_ready & io_Out_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_12 = _T_9 ? 1'h0 : out_valid_R; // @[RetNode.scala 194:23]
  wire  _T_10 = ~state; // @[Conditional.scala 37:30]
  wire  _GEN_13 = in_data_valid_R_0 | _GEN_12; // @[RetNode.scala 203:27]
  wire  _GEN_14 = in_data_valid_R_0 | state; // @[RetNode.scala 203:27]
  assign io_In_enable_ready = ~enable_valid_R; // @[RetNode.scala 163:22]
  assign io_In_data_field0_ready = ~in_data_valid_R_0; // @[RetNode.scala 171:34]
  assign io_Out_valid = out_valid_R; // @[RetNode.scala 180:16]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  state = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_ready_R = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  out_valid_R = _RAND_4[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      state <= 1'h0;
    end else if (_T_10) begin
      if (enable_valid_R) begin
        state <= _GEN_14;
      end
    end else if (state) begin
      if (out_ready_R) begin
        state <= 1'h0;
      end
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_10) begin
      if (_T_6) begin
        enable_valid_R <= io_In_enable_valid;
      end
    end else if (state) begin
      if (out_ready_R) begin
        enable_valid_R <= 1'h0;
      end else if (_T_6) begin
        enable_valid_R <= io_In_enable_valid;
      end
    end else if (_T_6) begin
      enable_valid_R <= io_In_enable_valid;
    end
    if (reset) begin
      in_data_valid_R_0 <= 1'h0;
    end else if (_T_10) begin
      in_data_valid_R_0 <= _GEN_9;
    end else if (state) begin
      if (out_ready_R) begin
        in_data_valid_R_0 <= 1'h0;
      end else begin
        in_data_valid_R_0 <= _GEN_9;
      end
    end else begin
      in_data_valid_R_0 <= _GEN_9;
    end
    if (reset) begin
      out_ready_R <= 1'h0;
    end else if (_T_10) begin
      if (_T_9) begin
        out_ready_R <= io_Out_ready;
      end
    end else if (state) begin
      if (out_ready_R) begin
        out_ready_R <= 1'h0;
      end else if (_T_9) begin
        out_ready_R <= io_Out_ready;
      end
    end else if (_T_9) begin
      out_ready_R <= io_Out_ready;
    end
    if (reset) begin
      out_valid_R <= 1'h0;
    end else if (_T_10) begin
      if (enable_valid_R) begin
        out_valid_R <= _GEN_13;
      end else if (_T_9) begin
        out_valid_R <= 1'h0;
      end
    end else if (state) begin
      if (out_ready_R) begin
        out_valid_R <= 1'h0;
      end else if (_T_9) begin
        out_valid_R <= 1'h0;
      end
    end else if (_T_9) begin
      out_valid_R <= 1'h0;
    end
  end
endmodule
module PhiFastNode(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  output        io_InData_0_ready,
  input         io_InData_0_valid,
  output        io_InData_1_ready,
  input         io_InData_1_valid,
  input  [63:0] io_InData_1_bits_data,
  output        io_Mask_ready,
  input         io_Mask_valid,
  input  [1:0]  io_Mask_bits,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  input         io_Out_1_ready,
  output        io_Out_1_valid,
  output [63:0] io_Out_1_bits_data,
  input         io_Out_2_ready,
  output        io_Out_2_valid,
  output [63:0] io_Out_2_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [63:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
`endif // RANDOMIZE_REG_INIT
  reg [63:0] in_data_R_1_data; // @[PhiNode.scala 203:26]
  reg  in_data_valid_R_0; // @[PhiNode.scala 204:32]
  reg  in_data_valid_R_1; // @[PhiNode.scala 204:32]
  reg  enable_R_control; // @[PhiNode.scala 207:25]
  reg  enable_valid_R; // @[PhiNode.scala 208:31]
  reg [1:0] mask_R; // @[PhiNode.scala 211:23]
  reg  mask_valid_R; // @[PhiNode.scala 212:29]
  reg [1:0] state; // @[PhiNode.scala 216:22]
  reg  out_valid_R_0; // @[PhiNode.scala 219:49]
  reg  out_valid_R_1; // @[PhiNode.scala 219:49]
  reg  out_valid_R_2; // @[PhiNode.scala 219:49]
  reg  fire_R_0; // @[PhiNode.scala 221:44]
  reg  fire_R_1; // @[PhiNode.scala 221:44]
  reg  fire_R_2; // @[PhiNode.scala 221:44]
  wire  _T_10 = io_Mask_ready & io_Mask_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_3 = _T_10 | mask_valid_R; // @[PhiNode.scala 239:24]
  wire  _T_12 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_7 = _T_12 | enable_valid_R; // @[PhiNode.scala 246:26]
  wire  _T_14 = io_InData_0_ready & io_InData_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_14 | in_data_valid_R_0; // @[PhiNode.scala 254:29]
  wire  _T_16 = io_InData_1_ready & io_InData_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_16 | in_data_valid_R_1; // @[PhiNode.scala 254:29]
  wire [1:0] _T_19 = {mask_R[0],mask_R[1]}; // @[Cat.scala 29:58]
  wire  sel = _T_19[1]; // @[CircuitMath.scala 30:8]
  wire  _T_20 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_16 = _T_20 | fire_R_0; // @[PhiNode.scala 276:26]
  wire  _GEN_17 = _T_20 ? 1'h0 : out_valid_R_0; // @[PhiNode.scala 276:26]
  wire  _T_21 = io_Out_1_ready & io_Out_1_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_18 = _T_21 | fire_R_1; // @[PhiNode.scala 276:26]
  wire  _GEN_19 = _T_21 ? 1'h0 : out_valid_R_1; // @[PhiNode.scala 276:26]
  wire  _T_22 = io_Out_2_ready & io_Out_2_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_20 = _T_22 | fire_R_2; // @[PhiNode.scala 276:26]
  wire  _GEN_21 = _T_22 ? 1'h0 : out_valid_R_2; // @[PhiNode.scala 276:26]
  wire  fire_mask_0 = fire_R_0 | _T_20; // @[PhiNode.scala 283:74]
  wire  fire_mask_1 = fire_R_1 | _T_21; // @[PhiNode.scala 283:74]
  wire  fire_mask_2 = fire_R_2 | _T_22; // @[PhiNode.scala 283:74]
  wire [63:0] _GEN_30 = sel ? in_data_R_1_data : 64'h0; // @[PhiNode.scala 312:12]
  wire  _T_43 = 2'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_44 = in_data_valid_R_0 & in_data_valid_R_1; // @[PhiNode.scala 286:30]
  wire  _T_45 = enable_valid_R & _T_44; // @[PhiNode.scala 327:27]
  wire  _GEN_1036 = _T_45 | _GEN_17; // @[PhiNode.scala 327:66]
  wire  _GEN_1037 = _T_45 | _GEN_19; // @[PhiNode.scala 327:66]
  wire  _GEN_1038 = _T_45 | _GEN_21; // @[PhiNode.scala 327:66]
  wire  _T_48 = 2'h1 == state; // @[Conditional.scala 37:30]
  wire  _T_49 = fire_mask_0 & fire_mask_1; // @[PhiNode.scala 364:31]
  wire  _T_50 = _T_49 & fire_mask_2; // @[PhiNode.scala 364:31]
  wire  _T_54 = 2'h2 == state; // @[Conditional.scala 37:30]
  wire [63:0] _GEN_1079 = _T_54 ? 64'h0 : _GEN_30; // @[Conditional.scala 39:67]
  wire [63:0] _GEN_1125 = _T_48 ? _GEN_30 : _GEN_1079; // @[Conditional.scala 39:67]
  assign io_enable_ready = ~enable_valid_R; // @[PhiNode.scala 245:19]
  assign io_InData_0_ready = ~in_data_valid_R_0; // @[PhiNode.scala 253:24]
  assign io_InData_1_ready = ~in_data_valid_R_1; // @[PhiNode.scala 253:24]
  assign io_Mask_ready = ~mask_valid_R; // @[PhiNode.scala 238:17]
  assign io_Out_0_valid = out_valid_R_0; // @[PhiNode.scala 322:21]
  assign io_Out_0_bits_data = _T_43 ? _GEN_30 : _GEN_1125; // @[PhiNode.scala 321:20 PhiNode.scala 392:42]
  assign io_Out_1_valid = out_valid_R_1; // @[PhiNode.scala 322:21]
  assign io_Out_1_bits_data = _T_43 ? _GEN_30 : _GEN_1125; // @[PhiNode.scala 321:20 PhiNode.scala 392:42]
  assign io_Out_2_valid = out_valid_R_2; // @[PhiNode.scala 322:21]
  assign io_Out_2_bits_data = _T_43 ? _GEN_30 : _GEN_1125; // @[PhiNode.scala 321:20 PhiNode.scala 392:42]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {2{`RANDOM}};
  in_data_R_1_data = _RAND_0[63:0];
  _RAND_1 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  in_data_valid_R_1 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  enable_R_control = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  enable_valid_R = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  mask_R = _RAND_5[1:0];
  _RAND_6 = {1{`RANDOM}};
  mask_valid_R = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  state = _RAND_7[1:0];
  _RAND_8 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  out_valid_R_1 = _RAND_9[0:0];
  _RAND_10 = {1{`RANDOM}};
  out_valid_R_2 = _RAND_10[0:0];
  _RAND_11 = {1{`RANDOM}};
  fire_R_0 = _RAND_11[0:0];
  _RAND_12 = {1{`RANDOM}};
  fire_R_1 = _RAND_12[0:0];
  _RAND_13 = {1{`RANDOM}};
  fire_R_2 = _RAND_13[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      in_data_R_1_data <= 64'h0;
    end else if (_T_43) begin
      if (_T_16) begin
        in_data_R_1_data <= io_InData_1_bits_data;
      end
    end else if (_T_48) begin
      if (_T_50) begin
        in_data_R_1_data <= 64'h0;
      end else if (_T_16) begin
        in_data_R_1_data <= io_InData_1_bits_data;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        in_data_R_1_data <= 64'h0;
      end else if (_T_16) begin
        in_data_R_1_data <= io_InData_1_bits_data;
      end
    end else if (_T_16) begin
      in_data_R_1_data <= io_InData_1_bits_data;
    end
    if (reset) begin
      in_data_valid_R_0 <= 1'h0;
    end else if (_T_43) begin
      in_data_valid_R_0 <= _GEN_11;
    end else if (_T_48) begin
      if (_T_50) begin
        in_data_valid_R_0 <= 1'h0;
      end else begin
        in_data_valid_R_0 <= _GEN_11;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        in_data_valid_R_0 <= 1'h0;
      end else begin
        in_data_valid_R_0 <= _GEN_11;
      end
    end else begin
      in_data_valid_R_0 <= _GEN_11;
    end
    if (reset) begin
      in_data_valid_R_1 <= 1'h0;
    end else if (_T_43) begin
      in_data_valid_R_1 <= _GEN_15;
    end else if (_T_48) begin
      if (_T_50) begin
        in_data_valid_R_1 <= 1'h0;
      end else begin
        in_data_valid_R_1 <= _GEN_15;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        in_data_valid_R_1 <= 1'h0;
      end else begin
        in_data_valid_R_1 <= _GEN_15;
      end
    end else begin
      in_data_valid_R_1 <= _GEN_15;
    end
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_43) begin
      if (_T_12) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_48) begin
      if (_T_50) begin
        enable_R_control <= 1'h0;
      end else if (_T_12) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        enable_R_control <= 1'h0;
      end else if (_T_12) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_12) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_43) begin
      enable_valid_R <= _GEN_7;
    end else if (_T_48) begin
      if (_T_50) begin
        enable_valid_R <= 1'h0;
      end else begin
        enable_valid_R <= _GEN_7;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        enable_valid_R <= 1'h0;
      end else begin
        enable_valid_R <= _GEN_7;
      end
    end else begin
      enable_valid_R <= _GEN_7;
    end
    if (reset) begin
      mask_R <= 2'h0;
    end else if (_T_43) begin
      if (_T_10) begin
        mask_R <= io_Mask_bits;
      end
    end else if (_T_48) begin
      if (_T_50) begin
        mask_R <= 2'h0;
      end else if (_T_10) begin
        mask_R <= io_Mask_bits;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        mask_R <= 2'h0;
      end else if (_T_10) begin
        mask_R <= io_Mask_bits;
      end
    end else if (_T_10) begin
      mask_R <= io_Mask_bits;
    end
    if (reset) begin
      mask_valid_R <= 1'h0;
    end else if (_T_43) begin
      mask_valid_R <= _GEN_3;
    end else if (_T_48) begin
      if (_T_50) begin
        mask_valid_R <= 1'h0;
      end else begin
        mask_valid_R <= _GEN_3;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        mask_valid_R <= 1'h0;
      end else begin
        mask_valid_R <= _GEN_3;
      end
    end else begin
      mask_valid_R <= _GEN_3;
    end
    if (reset) begin
      state <= 2'h0;
    end else if (_T_43) begin
      if (_T_45) begin
        if (enable_R_control) begin
          state <= 2'h1;
        end else begin
          state <= 2'h2;
        end
      end
    end else if (_T_48) begin
      if (_T_50) begin
        state <= 2'h0;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        state <= 2'h0;
      end
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_43) begin
      out_valid_R_0 <= _GEN_1036;
    end else if (_T_20) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_1 <= 1'h0;
    end else if (_T_43) begin
      out_valid_R_1 <= _GEN_1037;
    end else if (_T_21) begin
      out_valid_R_1 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_2 <= 1'h0;
    end else if (_T_43) begin
      out_valid_R_2 <= _GEN_1038;
    end else if (_T_22) begin
      out_valid_R_2 <= 1'h0;
    end
    if (reset) begin
      fire_R_0 <= 1'h0;
    end else if (_T_43) begin
      fire_R_0 <= _GEN_16;
    end else if (_T_48) begin
      if (_T_50) begin
        fire_R_0 <= 1'h0;
      end else begin
        fire_R_0 <= _GEN_16;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        fire_R_0 <= 1'h0;
      end else begin
        fire_R_0 <= _GEN_16;
      end
    end else begin
      fire_R_0 <= _GEN_16;
    end
    if (reset) begin
      fire_R_1 <= 1'h0;
    end else if (_T_43) begin
      fire_R_1 <= _GEN_18;
    end else if (_T_48) begin
      if (_T_50) begin
        fire_R_1 <= 1'h0;
      end else begin
        fire_R_1 <= _GEN_18;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        fire_R_1 <= 1'h0;
      end else begin
        fire_R_1 <= _GEN_18;
      end
    end else begin
      fire_R_1 <= _GEN_18;
    end
    if (reset) begin
      fire_R_2 <= 1'h0;
    end else if (_T_43) begin
      fire_R_2 <= _GEN_20;
    end else if (_T_48) begin
      if (_T_50) begin
        fire_R_2 <= 1'h0;
      end else begin
        fire_R_2 <= _GEN_20;
      end
    end else if (_T_54) begin
      if (_T_50) begin
        fire_R_2 <= 1'h0;
      end else begin
        fire_R_2 <= _GEN_20;
      end
    end else begin
      fire_R_2 <= _GEN_20;
    end
  end
endmodule
module GepNode(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  output        io_baseAddress_ready,
  input         io_baseAddress_valid,
  input  [63:0] io_baseAddress_bits_data,
  output        io_idx_0_ready,
  input         io_idx_0_valid,
  input  [63:0] io_idx_0_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [63:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [63:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
`endif // RANDOMIZE_REG_INIT
  reg  enable_valid_R; // @[HandShaking.scala 182:31]
  reg  out_ready_R_0; // @[HandShaking.scala 185:46]
  reg  out_valid_R_0; // @[HandShaking.scala 186:46]
  wire  _T_1 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_3 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] base_addr_R_data; // @[GepNode.scala 884:28]
  reg  base_addr_valid_R; // @[GepNode.scala 885:34]
  reg [63:0] idx_R_0_data; // @[GepNode.scala 888:39]
  reg  idx_valid_R_0; // @[GepNode.scala 889:45]
  reg  state; // @[GepNode.scala 893:22]
  wire  _T_11 = io_baseAddress_ready & io_baseAddress_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_11 | base_addr_valid_R; // @[GepNode.scala 909:31]
  wire  _T_13 = io_idx_0_ready & io_idx_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_13 | idx_valid_R_0; // @[GepNode.scala 916:28]
  wire [67:0] seek_value = idx_R_0_data * 64'h8; // @[GepNode.scala 924:21]
  wire [67:0] _GEN_52 = {{4'd0}, base_addr_R_data}; // @[GepNode.scala 932:35]
  wire [67:0] data_out = _GEN_52 + seek_value; // @[GepNode.scala 932:35]
  wire  _T_15 = ~state; // @[Conditional.scala 37:30]
  wire  _T_16 = enable_valid_R & base_addr_valid_R; // @[GepNode.scala 948:27]
  wire  _T_17 = _T_16 & idx_valid_R_0; // @[GepNode.scala 948:48]
  wire  _T_19 = _T_1 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire  _GEN_17 = _T_17 | state; // @[GepNode.scala 948:78]
  wire  _T_22 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 205:19]
  assign io_Out_0_valid = out_valid_R_0; // @[HandShaking.scala 194:21]
  assign io_Out_0_bits_data = data_out[63:0]; // @[GepNode.scala 936:25]
  assign io_baseAddress_ready = ~base_addr_valid_R; // @[GepNode.scala 908:24]
  assign io_idx_0_ready = ~idx_valid_R_0; // @[GepNode.scala 915:21]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_valid_R = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_2[0:0];
  _RAND_3 = {2{`RANDOM}};
  base_addr_R_data = _RAND_3[63:0];
  _RAND_4 = {1{`RANDOM}};
  base_addr_valid_R = _RAND_4[0:0];
  _RAND_5 = {2{`RANDOM}};
  idx_R_0_data = _RAND_5[63:0];
  _RAND_6 = {1{`RANDOM}};
  idx_valid_R_0 = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  state = _RAND_7[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_15) begin
      if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_22) begin
        enable_valid_R <= 1'h0;
      end else if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_3) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_15) begin
      if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_22) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_1) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_15) begin
      if (_T_17) begin
        out_valid_R_0 <= _T_19;
      end else if (_T_1) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_1) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      base_addr_R_data <= 64'h0;
    end else if (_T_15) begin
      if (_T_11) begin
        base_addr_R_data <= io_baseAddress_bits_data;
      end
    end else if (state) begin
      if (_T_22) begin
        base_addr_R_data <= 64'h0;
      end else if (_T_11) begin
        base_addr_R_data <= io_baseAddress_bits_data;
      end
    end else if (_T_11) begin
      base_addr_R_data <= io_baseAddress_bits_data;
    end
    if (reset) begin
      base_addr_valid_R <= 1'h0;
    end else if (_T_15) begin
      base_addr_valid_R <= _GEN_11;
    end else if (state) begin
      if (_T_22) begin
        base_addr_valid_R <= 1'h0;
      end else begin
        base_addr_valid_R <= _GEN_11;
      end
    end else begin
      base_addr_valid_R <= _GEN_11;
    end
    if (reset) begin
      idx_R_0_data <= 64'h0;
    end else if (_T_15) begin
      if (_T_13) begin
        idx_R_0_data <= io_idx_0_bits_data;
      end
    end else if (state) begin
      if (_T_22) begin
        idx_R_0_data <= 64'h0;
      end else if (_T_13) begin
        idx_R_0_data <= io_idx_0_bits_data;
      end
    end else if (_T_13) begin
      idx_R_0_data <= io_idx_0_bits_data;
    end
    if (reset) begin
      idx_valid_R_0 <= 1'h0;
    end else if (_T_15) begin
      idx_valid_R_0 <= _GEN_15;
    end else if (state) begin
      if (_T_22) begin
        idx_valid_R_0 <= 1'h0;
      end else begin
        idx_valid_R_0 <= _GEN_15;
      end
    end else begin
      idx_valid_R_0 <= _GEN_15;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_15) begin
      state <= _GEN_17;
    end else if (state) begin
      if (_T_22) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module UnTypLoadCache(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  output        io_GepAddr_ready,
  input         io_GepAddr_valid,
  input  [63:0] io_GepAddr_bits_data,
  input         io_MemReq_ready,
  output        io_MemReq_valid,
  output [63:0] io_MemReq_bits_addr,
  input         io_MemResp_valid,
  input  [63:0] io_MemResp_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [31:0] _RAND_7;
`endif // RANDOMIZE_REG_INIT
  reg  enable_R_control; // @[HandShaking.scala 592:31]
  reg  enable_valid_R; // @[HandShaking.scala 593:31]
  reg  out_ready_R_0; // @[HandShaking.scala 605:28]
  reg  out_valid_R_0; // @[HandShaking.scala 606:28]
  wire  _T_4 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_1 = _T_4 ? 1'h0 : out_valid_R_0; // @[HandShaking.scala 632:29]
  wire  _T_6 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] addr_R_data; // @[LoadCache.scala 58:23]
  reg  addr_valid_R; // @[LoadCache.scala 59:29]
  reg [63:0] data_R_data; // @[LoadCache.scala 62:23]
  reg [1:0] state; // @[LoadCache.scala 67:22]
  wire  _T_14 = io_GepAddr_ready & io_GepAddr_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_14 | addr_valid_R; // @[LoadCache.scala 76:27]
  wire  _T_15 = enable_valid_R & addr_valid_R; // @[LoadCache.scala 95:20]
  wire  _T_16 = _T_15 & enable_R_control; // @[LoadCache.scala 95:36]
  wire  _T_23 = &out_ready_R_0; // @[HandShaking.scala 725:24]
  wire  _T_24 = &io_Out_0_ready; // @[HandShaking.scala 725:50]
  wire  _T_25 = _T_23 | _T_24; // @[HandShaking.scala 725:29]
  wire  _T_44 = 2'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_49 = _T_4 ^ 1'h1; // @[HandShaking.scala 729:72]
  wire  _T_50 = 2'h1 == state; // @[Conditional.scala 37:30]
  wire  _GEN_25 = io_MemResp_valid | _GEN_1; // @[LoadCache.scala 214:30]
  wire  _T_51 = 2'h2 == state; // @[Conditional.scala 37:30]
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 650:19]
  assign io_Out_0_valid = out_valid_R_0; // @[HandShaking.scala 630:21]
  assign io_Out_0_bits_data = data_R_data; // @[LoadCache.scala 158:20]
  assign io_GepAddr_ready = ~addr_valid_R; // @[LoadCache.scala 75:20]
  assign io_MemReq_valid = _T_44 & _T_16; // @[LoadCache.scala 162:19 LoadCache.scala 185:27]
  assign io_MemReq_bits_addr = addr_R_data; // @[LoadCache.scala 164:23]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {2{`RANDOM}};
  addr_R_data = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  addr_valid_R = _RAND_5[0:0];
  _RAND_6 = {2{`RANDOM}};
  data_R_data = _RAND_6[63:0];
  _RAND_7 = {1{`RANDOM}};
  state = _RAND_7[1:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_6) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_44) begin
      if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_50) begin
      if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        enable_valid_R <= 1'h0;
      end else if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_6) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_44) begin
      if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_50) begin
      if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_4) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_44) begin
      if (_T_15) begin
        if (enable_R_control) begin
          if (_T_4) begin
            out_valid_R_0 <= 1'h0;
          end
        end else begin
          out_valid_R_0 <= _T_49;
        end
      end else if (_T_4) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_50) begin
      out_valid_R_0 <= _GEN_25;
    end else if (_T_4) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      addr_R_data <= 64'h0;
    end else if (_T_44) begin
      if (_T_14) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_50) begin
      if (_T_14) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        addr_R_data <= 64'h0;
      end else if (_T_14) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_14) begin
      addr_R_data <= io_GepAddr_bits_data;
    end
    if (reset) begin
      addr_valid_R <= 1'h0;
    end else if (_T_44) begin
      addr_valid_R <= _GEN_11;
    end else if (_T_50) begin
      addr_valid_R <= _GEN_11;
    end else if (_T_51) begin
      if (_T_25) begin
        addr_valid_R <= 1'h0;
      end else begin
        addr_valid_R <= _GEN_11;
      end
    end else begin
      addr_valid_R <= _GEN_11;
    end
    if (reset) begin
      data_R_data <= 64'h0;
    end else if (!(_T_44)) begin
      if (_T_50) begin
        if (io_MemResp_valid) begin
          data_R_data <= io_MemResp_bits_data;
        end
      end else if (_T_51) begin
        if (_T_25) begin
          data_R_data <= 64'h0;
        end
      end
    end
    if (reset) begin
      state <= 2'h0;
    end else if (_T_44) begin
      if (_T_15) begin
        if (enable_R_control) begin
          if (io_MemReq_ready) begin
            state <= 2'h1;
          end
        end else begin
          state <= 2'h2;
        end
      end
    end else if (_T_50) begin
      if (io_MemResp_valid) begin
        state <= 2'h2;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        state <= 2'h0;
      end
    end
  end
endmodule
module UALU_1(
  input  [63:0] io_in1,
  input  [63:0] io_in2,
  output [63:0] io_out
);
  wire [127:0] _T_24 = io_in1 * io_in2; // @[Alu.scala 194:32]
  assign io_out = _T_24[63:0]; // @[Alu.scala 235:10]
endmodule
module ComputeNode_1(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  output        io_LeftIO_ready,
  input         io_LeftIO_valid,
  input  [63:0] io_LeftIO_bits_data,
  output        io_RightIO_ready,
  input         io_RightIO_valid,
  input  [63:0] io_RightIO_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [63:0] _RAND_9;
  reg [31:0] _RAND_10;
`endif // RANDOMIZE_REG_INIT
  wire [63:0] FU_io_in1; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_in2; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_out; // @[ComputeNode.scala 61:18]
  reg  enable_R_control; // @[HandShaking.scala 181:31]
  reg  enable_valid_R; // @[HandShaking.scala 182:31]
  reg  out_ready_R_0; // @[HandShaking.scala 185:46]
  reg  out_valid_R_0; // @[HandShaking.scala 186:46]
  wire  _T_1 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_3 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] left_R_data; // @[ComputeNode.scala 53:23]
  reg  left_valid_R; // @[ComputeNode.scala 54:29]
  reg [63:0] right_R_data; // @[ComputeNode.scala 57:24]
  reg  right_valid_R; // @[ComputeNode.scala 58:30]
  reg  state; // @[ComputeNode.scala 64:22]
  reg [63:0] out_data_R; // @[ComputeNode.scala 89:27]
  wire  _T_12 = io_LeftIO_ready & io_LeftIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_12 | left_valid_R; // @[ComputeNode.scala 105:26]
  wire  _T_14 = io_RightIO_ready & io_RightIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_14 | right_valid_R; // @[ComputeNode.scala 111:27]
  wire  _T_32 = ~state; // @[Conditional.scala 37:30]
  wire  _T_33 = enable_valid_R & left_valid_R; // @[ComputeNode.scala 147:27]
  wire  _T_34 = _T_33 & right_valid_R; // @[ComputeNode.scala 147:43]
  reg [9:0] guard_index; // @[Counter.scala 29:33]
  wire [63:0] _GEN_20 = 10'h1 == guard_index ? 64'h1 : 64'h0; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_21 = 10'h2 == guard_index ? 64'h2 : _GEN_20; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_22 = 10'h3 == guard_index ? 64'h3 : _GEN_21; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_23 = 10'h4 == guard_index ? 64'h4 : _GEN_22; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_24 = 10'h5 == guard_index ? 64'h5 : _GEN_23; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_25 = 10'h6 == guard_index ? 64'h6 : _GEN_24; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_26 = 10'h7 == guard_index ? 64'h7 : _GEN_25; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_27 = 10'h8 == guard_index ? 64'h8 : _GEN_26; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_28 = 10'h9 == guard_index ? 64'h9 : _GEN_27; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_29 = 10'ha == guard_index ? 64'ha : _GEN_28; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_30 = 10'hb == guard_index ? 64'hb : _GEN_29; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_31 = 10'hc == guard_index ? 64'hc : _GEN_30; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_32 = 10'hd == guard_index ? 64'hd : _GEN_31; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_33 = 10'he == guard_index ? 64'he : _GEN_32; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_34 = 10'hf == guard_index ? 64'hf : _GEN_33; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_35 = 10'h10 == guard_index ? 64'h10 : _GEN_34; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_36 = 10'h11 == guard_index ? 64'h11 : _GEN_35; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_37 = 10'h12 == guard_index ? 64'h12 : _GEN_36; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_38 = 10'h13 == guard_index ? 64'h13 : _GEN_37; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_39 = 10'h14 == guard_index ? 64'h14 : _GEN_38; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_40 = 10'h15 == guard_index ? 64'h15 : _GEN_39; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_41 = 10'h16 == guard_index ? 64'h16 : _GEN_40; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_42 = 10'h17 == guard_index ? 64'h17 : _GEN_41; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_43 = 10'h18 == guard_index ? 64'h18 : _GEN_42; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_44 = 10'h19 == guard_index ? 64'h19 : _GEN_43; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_45 = 10'h1a == guard_index ? 64'h1a : _GEN_44; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_46 = 10'h1b == guard_index ? 64'h1b : _GEN_45; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_47 = 10'h1c == guard_index ? 64'h1c : _GEN_46; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_48 = 10'h1d == guard_index ? 64'h1d : _GEN_47; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_49 = 10'h1e == guard_index ? 64'h1e : _GEN_48; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_50 = 10'h1f == guard_index ? 64'h1f : _GEN_49; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_51 = 10'h20 == guard_index ? 64'h20 : _GEN_50; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_52 = 10'h21 == guard_index ? 64'h21 : _GEN_51; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_53 = 10'h22 == guard_index ? 64'h22 : _GEN_52; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_54 = 10'h23 == guard_index ? 64'h23 : _GEN_53; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_55 = 10'h24 == guard_index ? 64'h24 : _GEN_54; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_56 = 10'h25 == guard_index ? 64'h25 : _GEN_55; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_57 = 10'h26 == guard_index ? 64'h26 : _GEN_56; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_58 = 10'h27 == guard_index ? 64'h27 : _GEN_57; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_59 = 10'h28 == guard_index ? 64'h28 : _GEN_58; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_60 = 10'h29 == guard_index ? 64'h29 : _GEN_59; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_61 = 10'h2a == guard_index ? 64'h2a : _GEN_60; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_62 = 10'h2b == guard_index ? 64'h2b : _GEN_61; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_63 = 10'h2c == guard_index ? 64'h2c : _GEN_62; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_64 = 10'h2d == guard_index ? 64'h2d : _GEN_63; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_65 = 10'h2e == guard_index ? 64'h2e : _GEN_64; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_66 = 10'h2f == guard_index ? 64'h2f : _GEN_65; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_67 = 10'h30 == guard_index ? 64'h30 : _GEN_66; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_68 = 10'h31 == guard_index ? 64'h31 : _GEN_67; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_69 = 10'h32 == guard_index ? 64'h32 : _GEN_68; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_70 = 10'h33 == guard_index ? 64'h33 : _GEN_69; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_71 = 10'h34 == guard_index ? 64'h34 : _GEN_70; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_72 = 10'h35 == guard_index ? 64'h35 : _GEN_71; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_73 = 10'h36 == guard_index ? 64'h36 : _GEN_72; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_74 = 10'h37 == guard_index ? 64'h37 : _GEN_73; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_75 = 10'h38 == guard_index ? 64'h38 : _GEN_74; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_76 = 10'h39 == guard_index ? 64'h39 : _GEN_75; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_77 = 10'h3a == guard_index ? 64'h3a : _GEN_76; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_78 = 10'h3b == guard_index ? 64'h3b : _GEN_77; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_79 = 10'h3c == guard_index ? 64'h3c : _GEN_78; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_80 = 10'h3d == guard_index ? 64'h3d : _GEN_79; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_81 = 10'h3e == guard_index ? 64'h3e : _GEN_80; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_82 = 10'h3f == guard_index ? 64'h3f : _GEN_81; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_83 = 10'h40 == guard_index ? 64'h40 : _GEN_82; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_84 = 10'h41 == guard_index ? 64'h41 : _GEN_83; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_85 = 10'h42 == guard_index ? 64'h42 : _GEN_84; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_86 = 10'h43 == guard_index ? 64'h43 : _GEN_85; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_87 = 10'h44 == guard_index ? 64'h44 : _GEN_86; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_88 = 10'h45 == guard_index ? 64'h45 : _GEN_87; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_89 = 10'h46 == guard_index ? 64'h46 : _GEN_88; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_90 = 10'h47 == guard_index ? 64'h47 : _GEN_89; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_91 = 10'h48 == guard_index ? 64'h48 : _GEN_90; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_92 = 10'h49 == guard_index ? 64'h49 : _GEN_91; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_93 = 10'h4a == guard_index ? 64'h4a : _GEN_92; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_94 = 10'h4b == guard_index ? 64'h4b : _GEN_93; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_95 = 10'h4c == guard_index ? 64'h4c : _GEN_94; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_96 = 10'h4d == guard_index ? 64'h4d : _GEN_95; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_97 = 10'h4e == guard_index ? 64'h4e : _GEN_96; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_98 = 10'h4f == guard_index ? 64'h4f : _GEN_97; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_99 = 10'h50 == guard_index ? 64'h50 : _GEN_98; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_100 = 10'h51 == guard_index ? 64'h51 : _GEN_99; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_101 = 10'h52 == guard_index ? 64'h52 : _GEN_100; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_102 = 10'h53 == guard_index ? 64'h53 : _GEN_101; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_103 = 10'h54 == guard_index ? 64'h54 : _GEN_102; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_104 = 10'h55 == guard_index ? 64'h55 : _GEN_103; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_105 = 10'h56 == guard_index ? 64'h56 : _GEN_104; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_106 = 10'h57 == guard_index ? 64'h57 : _GEN_105; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_107 = 10'h58 == guard_index ? 64'h58 : _GEN_106; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_108 = 10'h59 == guard_index ? 64'h59 : _GEN_107; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_109 = 10'h5a == guard_index ? 64'h5a : _GEN_108; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_110 = 10'h5b == guard_index ? 64'h5b : _GEN_109; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_111 = 10'h5c == guard_index ? 64'h5c : _GEN_110; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_112 = 10'h5d == guard_index ? 64'h5d : _GEN_111; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_113 = 10'h5e == guard_index ? 64'h5e : _GEN_112; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_114 = 10'h5f == guard_index ? 64'h5f : _GEN_113; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_115 = 10'h60 == guard_index ? 64'h60 : _GEN_114; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_116 = 10'h61 == guard_index ? 64'h61 : _GEN_115; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_117 = 10'h62 == guard_index ? 64'h62 : _GEN_116; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_118 = 10'h63 == guard_index ? 64'h63 : _GEN_117; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_119 = 10'h64 == guard_index ? 64'h64 : _GEN_118; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_120 = 10'h65 == guard_index ? 64'h65 : _GEN_119; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_121 = 10'h66 == guard_index ? 64'h66 : _GEN_120; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_122 = 10'h67 == guard_index ? 64'h67 : _GEN_121; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_123 = 10'h68 == guard_index ? 64'h68 : _GEN_122; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_124 = 10'h69 == guard_index ? 64'h69 : _GEN_123; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_125 = 10'h6a == guard_index ? 64'h6a : _GEN_124; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_126 = 10'h6b == guard_index ? 64'h6b : _GEN_125; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_127 = 10'h6c == guard_index ? 64'h6c : _GEN_126; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_128 = 10'h6d == guard_index ? 64'h6d : _GEN_127; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_129 = 10'h6e == guard_index ? 64'h6e : _GEN_128; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_130 = 10'h6f == guard_index ? 64'h6f : _GEN_129; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_131 = 10'h70 == guard_index ? 64'h70 : _GEN_130; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_132 = 10'h71 == guard_index ? 64'h71 : _GEN_131; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_133 = 10'h72 == guard_index ? 64'h72 : _GEN_132; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_134 = 10'h73 == guard_index ? 64'h73 : _GEN_133; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_135 = 10'h74 == guard_index ? 64'h74 : _GEN_134; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_136 = 10'h75 == guard_index ? 64'h75 : _GEN_135; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_137 = 10'h76 == guard_index ? 64'h76 : _GEN_136; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_138 = 10'h77 == guard_index ? 64'h77 : _GEN_137; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_139 = 10'h78 == guard_index ? 64'h78 : _GEN_138; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_140 = 10'h79 == guard_index ? 64'h79 : _GEN_139; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_141 = 10'h7a == guard_index ? 64'h7a : _GEN_140; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_142 = 10'h7b == guard_index ? 64'h7b : _GEN_141; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_143 = 10'h7c == guard_index ? 64'h7c : _GEN_142; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_144 = 10'h7d == guard_index ? 64'h7d : _GEN_143; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_145 = 10'h7e == guard_index ? 64'h7e : _GEN_144; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_146 = 10'h7f == guard_index ? 64'h7f : _GEN_145; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_147 = 10'h80 == guard_index ? 64'h80 : _GEN_146; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_148 = 10'h81 == guard_index ? 64'h81 : _GEN_147; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_149 = 10'h82 == guard_index ? 64'h82 : _GEN_148; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_150 = 10'h83 == guard_index ? 64'h83 : _GEN_149; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_151 = 10'h84 == guard_index ? 64'h84 : _GEN_150; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_152 = 10'h85 == guard_index ? 64'h85 : _GEN_151; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_153 = 10'h86 == guard_index ? 64'h86 : _GEN_152; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_154 = 10'h87 == guard_index ? 64'h87 : _GEN_153; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_155 = 10'h88 == guard_index ? 64'h88 : _GEN_154; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_156 = 10'h89 == guard_index ? 64'h89 : _GEN_155; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_157 = 10'h8a == guard_index ? 64'h8a : _GEN_156; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_158 = 10'h8b == guard_index ? 64'h8b : _GEN_157; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_159 = 10'h8c == guard_index ? 64'h8c : _GEN_158; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_160 = 10'h8d == guard_index ? 64'h8d : _GEN_159; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_161 = 10'h8e == guard_index ? 64'h8e : _GEN_160; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_162 = 10'h8f == guard_index ? 64'h8f : _GEN_161; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_163 = 10'h90 == guard_index ? 64'h90 : _GEN_162; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_164 = 10'h91 == guard_index ? 64'h91 : _GEN_163; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_165 = 10'h92 == guard_index ? 64'h92 : _GEN_164; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_166 = 10'h93 == guard_index ? 64'h93 : _GEN_165; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_167 = 10'h94 == guard_index ? 64'h94 : _GEN_166; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_168 = 10'h95 == guard_index ? 64'h95 : _GEN_167; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_169 = 10'h96 == guard_index ? 64'h96 : _GEN_168; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_170 = 10'h97 == guard_index ? 64'h97 : _GEN_169; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_171 = 10'h98 == guard_index ? 64'h98 : _GEN_170; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_172 = 10'h99 == guard_index ? 64'h99 : _GEN_171; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_173 = 10'h9a == guard_index ? 64'h9a : _GEN_172; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_174 = 10'h9b == guard_index ? 64'h9b : _GEN_173; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_175 = 10'h9c == guard_index ? 64'h9c : _GEN_174; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_176 = 10'h9d == guard_index ? 64'h9d : _GEN_175; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_177 = 10'h9e == guard_index ? 64'h9e : _GEN_176; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_178 = 10'h9f == guard_index ? 64'h9f : _GEN_177; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_179 = 10'ha0 == guard_index ? 64'ha0 : _GEN_178; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_180 = 10'ha1 == guard_index ? 64'ha1 : _GEN_179; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_181 = 10'ha2 == guard_index ? 64'ha2 : _GEN_180; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_182 = 10'ha3 == guard_index ? 64'ha3 : _GEN_181; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_183 = 10'ha4 == guard_index ? 64'ha4 : _GEN_182; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_184 = 10'ha5 == guard_index ? 64'ha5 : _GEN_183; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_185 = 10'ha6 == guard_index ? 64'ha6 : _GEN_184; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_186 = 10'ha7 == guard_index ? 64'ha7 : _GEN_185; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_187 = 10'ha8 == guard_index ? 64'ha8 : _GEN_186; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_188 = 10'ha9 == guard_index ? 64'ha9 : _GEN_187; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_189 = 10'haa == guard_index ? 64'haa : _GEN_188; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_190 = 10'hab == guard_index ? 64'hab : _GEN_189; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_191 = 10'hac == guard_index ? 64'hac : _GEN_190; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_192 = 10'had == guard_index ? 64'had : _GEN_191; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_193 = 10'hae == guard_index ? 64'hae : _GEN_192; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_194 = 10'haf == guard_index ? 64'haf : _GEN_193; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_195 = 10'hb0 == guard_index ? 64'hb0 : _GEN_194; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_196 = 10'hb1 == guard_index ? 64'hb1 : _GEN_195; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_197 = 10'hb2 == guard_index ? 64'hb2 : _GEN_196; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_198 = 10'hb3 == guard_index ? 64'hb3 : _GEN_197; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_199 = 10'hb4 == guard_index ? 64'hb4 : _GEN_198; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_200 = 10'hb5 == guard_index ? 64'hb5 : _GEN_199; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_201 = 10'hb6 == guard_index ? 64'hb6 : _GEN_200; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_202 = 10'hb7 == guard_index ? 64'hb7 : _GEN_201; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_203 = 10'hb8 == guard_index ? 64'hb8 : _GEN_202; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_204 = 10'hb9 == guard_index ? 64'hb9 : _GEN_203; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_205 = 10'hba == guard_index ? 64'hba : _GEN_204; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_206 = 10'hbb == guard_index ? 64'hbb : _GEN_205; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_207 = 10'hbc == guard_index ? 64'hbc : _GEN_206; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_208 = 10'hbd == guard_index ? 64'hbd : _GEN_207; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_209 = 10'hbe == guard_index ? 64'hbe : _GEN_208; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_210 = 10'hbf == guard_index ? 64'hbf : _GEN_209; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_211 = 10'hc0 == guard_index ? 64'hc0 : _GEN_210; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_212 = 10'hc1 == guard_index ? 64'hc1 : _GEN_211; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_213 = 10'hc2 == guard_index ? 64'hc2 : _GEN_212; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_214 = 10'hc3 == guard_index ? 64'hc3 : _GEN_213; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_215 = 10'hc4 == guard_index ? 64'hc4 : _GEN_214; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_216 = 10'hc5 == guard_index ? 64'hc5 : _GEN_215; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_217 = 10'hc6 == guard_index ? 64'hc6 : _GEN_216; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_218 = 10'hc7 == guard_index ? 64'hc7 : _GEN_217; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_219 = 10'hc8 == guard_index ? 64'hc8 : _GEN_218; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_220 = 10'hc9 == guard_index ? 64'hc9 : _GEN_219; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_221 = 10'hca == guard_index ? 64'hca : _GEN_220; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_222 = 10'hcb == guard_index ? 64'hcb : _GEN_221; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_223 = 10'hcc == guard_index ? 64'hcc : _GEN_222; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_224 = 10'hcd == guard_index ? 64'hcd : _GEN_223; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_225 = 10'hce == guard_index ? 64'hce : _GEN_224; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_226 = 10'hcf == guard_index ? 64'hcf : _GEN_225; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_227 = 10'hd0 == guard_index ? 64'hd0 : _GEN_226; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_228 = 10'hd1 == guard_index ? 64'hd1 : _GEN_227; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_229 = 10'hd2 == guard_index ? 64'hd2 : _GEN_228; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_230 = 10'hd3 == guard_index ? 64'hd3 : _GEN_229; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_231 = 10'hd4 == guard_index ? 64'hd4 : _GEN_230; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_232 = 10'hd5 == guard_index ? 64'hd5 : _GEN_231; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_233 = 10'hd6 == guard_index ? 64'hd6 : _GEN_232; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_234 = 10'hd7 == guard_index ? 64'hd7 : _GEN_233; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_235 = 10'hd8 == guard_index ? 64'hd8 : _GEN_234; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_236 = 10'hd9 == guard_index ? 64'hd9 : _GEN_235; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_237 = 10'hda == guard_index ? 64'hda : _GEN_236; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_238 = 10'hdb == guard_index ? 64'hdb : _GEN_237; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_239 = 10'hdc == guard_index ? 64'hdc : _GEN_238; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_240 = 10'hdd == guard_index ? 64'hdd : _GEN_239; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_241 = 10'hde == guard_index ? 64'hde : _GEN_240; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_242 = 10'hdf == guard_index ? 64'hdf : _GEN_241; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_243 = 10'he0 == guard_index ? 64'he0 : _GEN_242; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_244 = 10'he1 == guard_index ? 64'he1 : _GEN_243; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_245 = 10'he2 == guard_index ? 64'he2 : _GEN_244; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_246 = 10'he3 == guard_index ? 64'he3 : _GEN_245; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_247 = 10'he4 == guard_index ? 64'he4 : _GEN_246; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_248 = 10'he5 == guard_index ? 64'he5 : _GEN_247; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_249 = 10'he6 == guard_index ? 64'he6 : _GEN_248; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_250 = 10'he7 == guard_index ? 64'he7 : _GEN_249; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_251 = 10'he8 == guard_index ? 64'he8 : _GEN_250; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_252 = 10'he9 == guard_index ? 64'he9 : _GEN_251; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_253 = 10'hea == guard_index ? 64'hea : _GEN_252; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_254 = 10'heb == guard_index ? 64'heb : _GEN_253; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_255 = 10'hec == guard_index ? 64'hec : _GEN_254; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_256 = 10'hed == guard_index ? 64'hed : _GEN_255; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_257 = 10'hee == guard_index ? 64'hee : _GEN_256; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_258 = 10'hef == guard_index ? 64'hef : _GEN_257; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_259 = 10'hf0 == guard_index ? 64'hf0 : _GEN_258; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_260 = 10'hf1 == guard_index ? 64'hf1 : _GEN_259; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_261 = 10'hf2 == guard_index ? 64'hf2 : _GEN_260; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_262 = 10'hf3 == guard_index ? 64'hf3 : _GEN_261; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_263 = 10'hf4 == guard_index ? 64'hf4 : _GEN_262; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_264 = 10'hf5 == guard_index ? 64'hf5 : _GEN_263; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_265 = 10'hf6 == guard_index ? 64'hf6 : _GEN_264; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_266 = 10'hf7 == guard_index ? 64'hf7 : _GEN_265; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_267 = 10'hf8 == guard_index ? 64'hf8 : _GEN_266; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_268 = 10'hf9 == guard_index ? 64'hf9 : _GEN_267; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_269 = 10'hfa == guard_index ? 64'hfa : _GEN_268; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_270 = 10'hfb == guard_index ? 64'hfb : _GEN_269; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_271 = 10'hfc == guard_index ? 64'hfc : _GEN_270; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_272 = 10'hfd == guard_index ? 64'hfd : _GEN_271; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_273 = 10'hfe == guard_index ? 64'hfe : _GEN_272; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_274 = 10'hff == guard_index ? 64'hff : _GEN_273; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_275 = 10'h100 == guard_index ? 64'h100 : _GEN_274; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_276 = 10'h101 == guard_index ? 64'h101 : _GEN_275; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_277 = 10'h102 == guard_index ? 64'h102 : _GEN_276; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_278 = 10'h103 == guard_index ? 64'h103 : _GEN_277; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_279 = 10'h104 == guard_index ? 64'h104 : _GEN_278; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_280 = 10'h105 == guard_index ? 64'h105 : _GEN_279; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_281 = 10'h106 == guard_index ? 64'h106 : _GEN_280; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_282 = 10'h107 == guard_index ? 64'h107 : _GEN_281; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_283 = 10'h108 == guard_index ? 64'h108 : _GEN_282; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_284 = 10'h109 == guard_index ? 64'h109 : _GEN_283; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_285 = 10'h10a == guard_index ? 64'h10a : _GEN_284; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_286 = 10'h10b == guard_index ? 64'h10b : _GEN_285; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_287 = 10'h10c == guard_index ? 64'h10c : _GEN_286; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_288 = 10'h10d == guard_index ? 64'h10d : _GEN_287; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_289 = 10'h10e == guard_index ? 64'h10e : _GEN_288; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_290 = 10'h10f == guard_index ? 64'h10f : _GEN_289; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_291 = 10'h110 == guard_index ? 64'h110 : _GEN_290; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_292 = 10'h111 == guard_index ? 64'h111 : _GEN_291; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_293 = 10'h112 == guard_index ? 64'h112 : _GEN_292; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_294 = 10'h113 == guard_index ? 64'h113 : _GEN_293; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_295 = 10'h114 == guard_index ? 64'h114 : _GEN_294; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_296 = 10'h115 == guard_index ? 64'h115 : _GEN_295; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_297 = 10'h116 == guard_index ? 64'h116 : _GEN_296; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_298 = 10'h117 == guard_index ? 64'h117 : _GEN_297; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_299 = 10'h118 == guard_index ? 64'h118 : _GEN_298; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_300 = 10'h119 == guard_index ? 64'h119 : _GEN_299; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_301 = 10'h11a == guard_index ? 64'h11a : _GEN_300; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_302 = 10'h11b == guard_index ? 64'h11b : _GEN_301; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_303 = 10'h11c == guard_index ? 64'h11c : _GEN_302; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_304 = 10'h11d == guard_index ? 64'h11d : _GEN_303; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_305 = 10'h11e == guard_index ? 64'h11e : _GEN_304; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_306 = 10'h11f == guard_index ? 64'h11f : _GEN_305; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_307 = 10'h120 == guard_index ? 64'h120 : _GEN_306; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_308 = 10'h121 == guard_index ? 64'h121 : _GEN_307; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_309 = 10'h122 == guard_index ? 64'h122 : _GEN_308; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_310 = 10'h123 == guard_index ? 64'h123 : _GEN_309; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_311 = 10'h124 == guard_index ? 64'h124 : _GEN_310; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_312 = 10'h125 == guard_index ? 64'h125 : _GEN_311; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_313 = 10'h126 == guard_index ? 64'h126 : _GEN_312; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_314 = 10'h127 == guard_index ? 64'h127 : _GEN_313; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_315 = 10'h128 == guard_index ? 64'h128 : _GEN_314; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_316 = 10'h129 == guard_index ? 64'h129 : _GEN_315; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_317 = 10'h12a == guard_index ? 64'h12a : _GEN_316; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_318 = 10'h12b == guard_index ? 64'h12b : _GEN_317; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_319 = 10'h12c == guard_index ? 64'h12c : _GEN_318; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_320 = 10'h12d == guard_index ? 64'h12d : _GEN_319; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_321 = 10'h12e == guard_index ? 64'h12e : _GEN_320; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_322 = 10'h12f == guard_index ? 64'h12f : _GEN_321; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_323 = 10'h130 == guard_index ? 64'h130 : _GEN_322; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_324 = 10'h131 == guard_index ? 64'h131 : _GEN_323; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_325 = 10'h132 == guard_index ? 64'h132 : _GEN_324; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_326 = 10'h133 == guard_index ? 64'h133 : _GEN_325; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_327 = 10'h134 == guard_index ? 64'h134 : _GEN_326; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_328 = 10'h135 == guard_index ? 64'h135 : _GEN_327; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_329 = 10'h136 == guard_index ? 64'h136 : _GEN_328; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_330 = 10'h137 == guard_index ? 64'h137 : _GEN_329; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_331 = 10'h138 == guard_index ? 64'h138 : _GEN_330; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_332 = 10'h139 == guard_index ? 64'h139 : _GEN_331; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_333 = 10'h13a == guard_index ? 64'h13a : _GEN_332; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_334 = 10'h13b == guard_index ? 64'h13b : _GEN_333; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_335 = 10'h13c == guard_index ? 64'h13c : _GEN_334; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_336 = 10'h13d == guard_index ? 64'h13d : _GEN_335; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_337 = 10'h13e == guard_index ? 64'h13e : _GEN_336; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_338 = 10'h13f == guard_index ? 64'h13f : _GEN_337; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_339 = 10'h140 == guard_index ? 64'h140 : _GEN_338; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_340 = 10'h141 == guard_index ? 64'h141 : _GEN_339; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_341 = 10'h142 == guard_index ? 64'h142 : _GEN_340; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_342 = 10'h143 == guard_index ? 64'h143 : _GEN_341; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_343 = 10'h144 == guard_index ? 64'h144 : _GEN_342; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_344 = 10'h145 == guard_index ? 64'h145 : _GEN_343; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_345 = 10'h146 == guard_index ? 64'h146 : _GEN_344; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_346 = 10'h147 == guard_index ? 64'h147 : _GEN_345; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_347 = 10'h148 == guard_index ? 64'h148 : _GEN_346; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_348 = 10'h149 == guard_index ? 64'h149 : _GEN_347; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_349 = 10'h14a == guard_index ? 64'h14a : _GEN_348; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_350 = 10'h14b == guard_index ? 64'h14b : _GEN_349; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_351 = 10'h14c == guard_index ? 64'h14c : _GEN_350; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_352 = 10'h14d == guard_index ? 64'h14d : _GEN_351; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_353 = 10'h14e == guard_index ? 64'h14e : _GEN_352; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_354 = 10'h14f == guard_index ? 64'h14f : _GEN_353; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_355 = 10'h150 == guard_index ? 64'h150 : _GEN_354; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_356 = 10'h151 == guard_index ? 64'h151 : _GEN_355; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_357 = 10'h152 == guard_index ? 64'h152 : _GEN_356; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_358 = 10'h153 == guard_index ? 64'h153 : _GEN_357; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_359 = 10'h154 == guard_index ? 64'h154 : _GEN_358; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_360 = 10'h155 == guard_index ? 64'h155 : _GEN_359; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_361 = 10'h156 == guard_index ? 64'h156 : _GEN_360; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_362 = 10'h157 == guard_index ? 64'h157 : _GEN_361; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_363 = 10'h158 == guard_index ? 64'h158 : _GEN_362; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_364 = 10'h159 == guard_index ? 64'h159 : _GEN_363; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_365 = 10'h15a == guard_index ? 64'h15a : _GEN_364; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_366 = 10'h15b == guard_index ? 64'h15b : _GEN_365; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_367 = 10'h15c == guard_index ? 64'h15c : _GEN_366; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_368 = 10'h15d == guard_index ? 64'h15d : _GEN_367; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_369 = 10'h15e == guard_index ? 64'h15e : _GEN_368; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_370 = 10'h15f == guard_index ? 64'h15f : _GEN_369; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_371 = 10'h160 == guard_index ? 64'h160 : _GEN_370; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_372 = 10'h161 == guard_index ? 64'h161 : _GEN_371; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_373 = 10'h162 == guard_index ? 64'h162 : _GEN_372; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_374 = 10'h163 == guard_index ? 64'h163 : _GEN_373; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_375 = 10'h164 == guard_index ? 64'h164 : _GEN_374; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_376 = 10'h165 == guard_index ? 64'h165 : _GEN_375; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_377 = 10'h166 == guard_index ? 64'h166 : _GEN_376; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_378 = 10'h167 == guard_index ? 64'h167 : _GEN_377; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_379 = 10'h168 == guard_index ? 64'h168 : _GEN_378; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_380 = 10'h169 == guard_index ? 64'h169 : _GEN_379; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_381 = 10'h16a == guard_index ? 64'h16a : _GEN_380; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_382 = 10'h16b == guard_index ? 64'h16b : _GEN_381; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_383 = 10'h16c == guard_index ? 64'h16c : _GEN_382; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_384 = 10'h16d == guard_index ? 64'h16d : _GEN_383; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_385 = 10'h16e == guard_index ? 64'h16e : _GEN_384; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_386 = 10'h16f == guard_index ? 64'h16f : _GEN_385; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_387 = 10'h170 == guard_index ? 64'h170 : _GEN_386; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_388 = 10'h171 == guard_index ? 64'h171 : _GEN_387; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_389 = 10'h172 == guard_index ? 64'h172 : _GEN_388; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_390 = 10'h173 == guard_index ? 64'h173 : _GEN_389; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_391 = 10'h174 == guard_index ? 64'h174 : _GEN_390; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_392 = 10'h175 == guard_index ? 64'h175 : _GEN_391; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_393 = 10'h176 == guard_index ? 64'h176 : _GEN_392; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_394 = 10'h177 == guard_index ? 64'h177 : _GEN_393; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_395 = 10'h178 == guard_index ? 64'h178 : _GEN_394; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_396 = 10'h179 == guard_index ? 64'h179 : _GEN_395; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_397 = 10'h17a == guard_index ? 64'h17a : _GEN_396; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_398 = 10'h17b == guard_index ? 64'h17b : _GEN_397; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_399 = 10'h17c == guard_index ? 64'h17c : _GEN_398; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_400 = 10'h17d == guard_index ? 64'h17d : _GEN_399; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_401 = 10'h17e == guard_index ? 64'h17e : _GEN_400; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_402 = 10'h17f == guard_index ? 64'h17f : _GEN_401; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_403 = 10'h180 == guard_index ? 64'h180 : _GEN_402; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_404 = 10'h181 == guard_index ? 64'h181 : _GEN_403; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_405 = 10'h182 == guard_index ? 64'h182 : _GEN_404; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_406 = 10'h183 == guard_index ? 64'h183 : _GEN_405; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_407 = 10'h184 == guard_index ? 64'h184 : _GEN_406; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_408 = 10'h185 == guard_index ? 64'h185 : _GEN_407; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_409 = 10'h186 == guard_index ? 64'h186 : _GEN_408; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_410 = 10'h187 == guard_index ? 64'h187 : _GEN_409; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_411 = 10'h188 == guard_index ? 64'h188 : _GEN_410; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_412 = 10'h189 == guard_index ? 64'h189 : _GEN_411; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_413 = 10'h18a == guard_index ? 64'h18a : _GEN_412; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_414 = 10'h18b == guard_index ? 64'h18b : _GEN_413; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_415 = 10'h18c == guard_index ? 64'h18c : _GEN_414; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_416 = 10'h18d == guard_index ? 64'h18d : _GEN_415; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_417 = 10'h18e == guard_index ? 64'h18e : _GEN_416; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_418 = 10'h18f == guard_index ? 64'h18f : _GEN_417; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_419 = 10'h190 == guard_index ? 64'h190 : _GEN_418; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_420 = 10'h191 == guard_index ? 64'h191 : _GEN_419; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_421 = 10'h192 == guard_index ? 64'h192 : _GEN_420; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_422 = 10'h193 == guard_index ? 64'h193 : _GEN_421; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_423 = 10'h194 == guard_index ? 64'h194 : _GEN_422; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_424 = 10'h195 == guard_index ? 64'h195 : _GEN_423; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_425 = 10'h196 == guard_index ? 64'h196 : _GEN_424; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_426 = 10'h197 == guard_index ? 64'h197 : _GEN_425; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_427 = 10'h198 == guard_index ? 64'h198 : _GEN_426; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_428 = 10'h199 == guard_index ? 64'h199 : _GEN_427; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_429 = 10'h19a == guard_index ? 64'h19a : _GEN_428; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_430 = 10'h19b == guard_index ? 64'h19b : _GEN_429; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_431 = 10'h19c == guard_index ? 64'h19c : _GEN_430; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_432 = 10'h19d == guard_index ? 64'h19d : _GEN_431; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_433 = 10'h19e == guard_index ? 64'h19e : _GEN_432; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_434 = 10'h19f == guard_index ? 64'h19f : _GEN_433; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_435 = 10'h1a0 == guard_index ? 64'h1a0 : _GEN_434; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_436 = 10'h1a1 == guard_index ? 64'h1a1 : _GEN_435; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_437 = 10'h1a2 == guard_index ? 64'h1a2 : _GEN_436; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_438 = 10'h1a3 == guard_index ? 64'h1a3 : _GEN_437; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_439 = 10'h1a4 == guard_index ? 64'h1a4 : _GEN_438; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_440 = 10'h1a5 == guard_index ? 64'h1a5 : _GEN_439; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_441 = 10'h1a6 == guard_index ? 64'h1a6 : _GEN_440; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_442 = 10'h1a7 == guard_index ? 64'h1a7 : _GEN_441; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_443 = 10'h1a8 == guard_index ? 64'h1a8 : _GEN_442; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_444 = 10'h1a9 == guard_index ? 64'h1a9 : _GEN_443; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_445 = 10'h1aa == guard_index ? 64'h1aa : _GEN_444; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_446 = 10'h1ab == guard_index ? 64'h1ab : _GEN_445; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_447 = 10'h1ac == guard_index ? 64'h1ac : _GEN_446; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_448 = 10'h1ad == guard_index ? 64'h1ad : _GEN_447; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_449 = 10'h1ae == guard_index ? 64'h1ae : _GEN_448; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_450 = 10'h1af == guard_index ? 64'h1af : _GEN_449; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_451 = 10'h1b0 == guard_index ? 64'h1b0 : _GEN_450; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_452 = 10'h1b1 == guard_index ? 64'h1b1 : _GEN_451; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_453 = 10'h1b2 == guard_index ? 64'h1b2 : _GEN_452; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_454 = 10'h1b3 == guard_index ? 64'h1b3 : _GEN_453; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_455 = 10'h1b4 == guard_index ? 64'h1b4 : _GEN_454; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_456 = 10'h1b5 == guard_index ? 64'h1b5 : _GEN_455; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_457 = 10'h1b6 == guard_index ? 64'h1b6 : _GEN_456; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_458 = 10'h1b7 == guard_index ? 64'h1b7 : _GEN_457; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_459 = 10'h1b8 == guard_index ? 64'h1b8 : _GEN_458; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_460 = 10'h1b9 == guard_index ? 64'h1b9 : _GEN_459; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_461 = 10'h1ba == guard_index ? 64'h1ba : _GEN_460; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_462 = 10'h1bb == guard_index ? 64'h1bb : _GEN_461; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_463 = 10'h1bc == guard_index ? 64'h1bc : _GEN_462; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_464 = 10'h1bd == guard_index ? 64'h1bd : _GEN_463; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_465 = 10'h1be == guard_index ? 64'h1be : _GEN_464; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_466 = 10'h1bf == guard_index ? 64'h1bf : _GEN_465; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_467 = 10'h1c0 == guard_index ? 64'h1c0 : _GEN_466; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_468 = 10'h1c1 == guard_index ? 64'h1c1 : _GEN_467; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_469 = 10'h1c2 == guard_index ? 64'h1c2 : _GEN_468; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_470 = 10'h1c3 == guard_index ? 64'h1c3 : _GEN_469; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_471 = 10'h1c4 == guard_index ? 64'h1c4 : _GEN_470; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_472 = 10'h1c5 == guard_index ? 64'h1c5 : _GEN_471; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_473 = 10'h1c6 == guard_index ? 64'h1c6 : _GEN_472; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_474 = 10'h1c7 == guard_index ? 64'h1c7 : _GEN_473; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_475 = 10'h1c8 == guard_index ? 64'h1c8 : _GEN_474; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_476 = 10'h1c9 == guard_index ? 64'h1c9 : _GEN_475; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_477 = 10'h1ca == guard_index ? 64'h1ca : _GEN_476; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_478 = 10'h1cb == guard_index ? 64'h1cb : _GEN_477; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_479 = 10'h1cc == guard_index ? 64'h1cc : _GEN_478; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_480 = 10'h1cd == guard_index ? 64'h1cd : _GEN_479; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_481 = 10'h1ce == guard_index ? 64'h1ce : _GEN_480; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_482 = 10'h1cf == guard_index ? 64'h1cf : _GEN_481; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_483 = 10'h1d0 == guard_index ? 64'h1d0 : _GEN_482; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_484 = 10'h1d1 == guard_index ? 64'h1d1 : _GEN_483; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_485 = 10'h1d2 == guard_index ? 64'h1d2 : _GEN_484; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_486 = 10'h1d3 == guard_index ? 64'h1d3 : _GEN_485; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_487 = 10'h1d4 == guard_index ? 64'h1d4 : _GEN_486; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_488 = 10'h1d5 == guard_index ? 64'h1d5 : _GEN_487; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_489 = 10'h1d6 == guard_index ? 64'h1d6 : _GEN_488; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_490 = 10'h1d7 == guard_index ? 64'h1d7 : _GEN_489; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_491 = 10'h1d8 == guard_index ? 64'h1d8 : _GEN_490; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_492 = 10'h1d9 == guard_index ? 64'h1d9 : _GEN_491; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_493 = 10'h1da == guard_index ? 64'h1da : _GEN_492; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_494 = 10'h1db == guard_index ? 64'h1db : _GEN_493; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_495 = 10'h1dc == guard_index ? 64'h1dc : _GEN_494; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_496 = 10'h1dd == guard_index ? 64'h1dd : _GEN_495; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_497 = 10'h1de == guard_index ? 64'h1de : _GEN_496; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_498 = 10'h1df == guard_index ? 64'h1df : _GEN_497; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_499 = 10'h1e0 == guard_index ? 64'h1e0 : _GEN_498; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_500 = 10'h1e1 == guard_index ? 64'h1e1 : _GEN_499; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_501 = 10'h1e2 == guard_index ? 64'h1e2 : _GEN_500; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_502 = 10'h1e3 == guard_index ? 64'h1e3 : _GEN_501; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_503 = 10'h1e4 == guard_index ? 64'h1e4 : _GEN_502; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_504 = 10'h1e5 == guard_index ? 64'h1e5 : _GEN_503; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_505 = 10'h1e6 == guard_index ? 64'h1e6 : _GEN_504; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_506 = 10'h1e7 == guard_index ? 64'h1e7 : _GEN_505; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_507 = 10'h1e8 == guard_index ? 64'h1e8 : _GEN_506; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_508 = 10'h1e9 == guard_index ? 64'h1e9 : _GEN_507; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_509 = 10'h1ea == guard_index ? 64'h1ea : _GEN_508; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_510 = 10'h1eb == guard_index ? 64'h1eb : _GEN_509; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_511 = 10'h1ec == guard_index ? 64'h1ec : _GEN_510; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_512 = 10'h1ed == guard_index ? 64'h1ed : _GEN_511; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_513 = 10'h1ee == guard_index ? 64'h1ee : _GEN_512; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_514 = 10'h1ef == guard_index ? 64'h1ef : _GEN_513; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_515 = 10'h1f0 == guard_index ? 64'h1f0 : _GEN_514; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_516 = 10'h1f1 == guard_index ? 64'h1f1 : _GEN_515; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_517 = 10'h1f2 == guard_index ? 64'h1f2 : _GEN_516; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_518 = 10'h1f3 == guard_index ? 64'h1f3 : _GEN_517; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_519 = 10'h1f4 == guard_index ? 64'h1f4 : _GEN_518; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_520 = 10'h1f5 == guard_index ? 64'h1f5 : _GEN_519; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_521 = 10'h1f6 == guard_index ? 64'h1f6 : _GEN_520; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_522 = 10'h1f7 == guard_index ? 64'h1f7 : _GEN_521; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_523 = 10'h1f8 == guard_index ? 64'h1f8 : _GEN_522; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_524 = 10'h1f9 == guard_index ? 64'h1f9 : _GEN_523; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_525 = 10'h1fa == guard_index ? 64'h1fa : _GEN_524; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_526 = 10'h1fb == guard_index ? 64'h1fb : _GEN_525; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_527 = 10'h1fc == guard_index ? 64'h1fc : _GEN_526; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_528 = 10'h1fd == guard_index ? 64'h1fd : _GEN_527; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_529 = 10'h1fe == guard_index ? 64'h1fe : _GEN_528; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_530 = 10'h1ff == guard_index ? 64'h1ff : _GEN_529; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_531 = 10'h200 == guard_index ? 64'h200 : _GEN_530; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_532 = 10'h201 == guard_index ? 64'h201 : _GEN_531; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_533 = 10'h202 == guard_index ? 64'h202 : _GEN_532; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_534 = 10'h203 == guard_index ? 64'h203 : _GEN_533; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_535 = 10'h204 == guard_index ? 64'h204 : _GEN_534; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_536 = 10'h205 == guard_index ? 64'h205 : _GEN_535; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_537 = 10'h206 == guard_index ? 64'h206 : _GEN_536; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_538 = 10'h207 == guard_index ? 64'h207 : _GEN_537; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_539 = 10'h208 == guard_index ? 64'h208 : _GEN_538; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_540 = 10'h209 == guard_index ? 64'h209 : _GEN_539; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_541 = 10'h20a == guard_index ? 64'h20a : _GEN_540; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_542 = 10'h20b == guard_index ? 64'h20b : _GEN_541; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_543 = 10'h20c == guard_index ? 64'h20c : _GEN_542; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_544 = 10'h20d == guard_index ? 64'h20d : _GEN_543; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_545 = 10'h20e == guard_index ? 64'h20e : _GEN_544; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_546 = 10'h20f == guard_index ? 64'h20f : _GEN_545; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_547 = 10'h210 == guard_index ? 64'h210 : _GEN_546; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_548 = 10'h211 == guard_index ? 64'h211 : _GEN_547; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_549 = 10'h212 == guard_index ? 64'h212 : _GEN_548; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_550 = 10'h213 == guard_index ? 64'h213 : _GEN_549; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_551 = 10'h214 == guard_index ? 64'h214 : _GEN_550; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_552 = 10'h215 == guard_index ? 64'h215 : _GEN_551; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_553 = 10'h216 == guard_index ? 64'h216 : _GEN_552; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_554 = 10'h217 == guard_index ? 64'h217 : _GEN_553; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_555 = 10'h218 == guard_index ? 64'h218 : _GEN_554; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_556 = 10'h219 == guard_index ? 64'h219 : _GEN_555; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_557 = 10'h21a == guard_index ? 64'h21a : _GEN_556; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_558 = 10'h21b == guard_index ? 64'h21b : _GEN_557; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_559 = 10'h21c == guard_index ? 64'h21c : _GEN_558; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_560 = 10'h21d == guard_index ? 64'h21d : _GEN_559; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_561 = 10'h21e == guard_index ? 64'h21e : _GEN_560; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_562 = 10'h21f == guard_index ? 64'h21f : _GEN_561; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_563 = 10'h220 == guard_index ? 64'h220 : _GEN_562; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_564 = 10'h221 == guard_index ? 64'h221 : _GEN_563; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_565 = 10'h222 == guard_index ? 64'h222 : _GEN_564; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_566 = 10'h223 == guard_index ? 64'h223 : _GEN_565; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_567 = 10'h224 == guard_index ? 64'h224 : _GEN_566; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_568 = 10'h225 == guard_index ? 64'h225 : _GEN_567; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_569 = 10'h226 == guard_index ? 64'h226 : _GEN_568; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_570 = 10'h227 == guard_index ? 64'h227 : _GEN_569; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_571 = 10'h228 == guard_index ? 64'h228 : _GEN_570; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_572 = 10'h229 == guard_index ? 64'h229 : _GEN_571; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_573 = 10'h22a == guard_index ? 64'h22a : _GEN_572; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_574 = 10'h22b == guard_index ? 64'h22b : _GEN_573; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_575 = 10'h22c == guard_index ? 64'h22c : _GEN_574; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_576 = 10'h22d == guard_index ? 64'h22d : _GEN_575; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_577 = 10'h22e == guard_index ? 64'h22e : _GEN_576; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_578 = 10'h22f == guard_index ? 64'h22f : _GEN_577; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_579 = 10'h230 == guard_index ? 64'h230 : _GEN_578; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_580 = 10'h231 == guard_index ? 64'h231 : _GEN_579; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_581 = 10'h232 == guard_index ? 64'h232 : _GEN_580; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_582 = 10'h233 == guard_index ? 64'h233 : _GEN_581; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_583 = 10'h234 == guard_index ? 64'h234 : _GEN_582; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_584 = 10'h235 == guard_index ? 64'h235 : _GEN_583; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_585 = 10'h236 == guard_index ? 64'h236 : _GEN_584; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_586 = 10'h237 == guard_index ? 64'h237 : _GEN_585; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_587 = 10'h238 == guard_index ? 64'h238 : _GEN_586; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_588 = 10'h239 == guard_index ? 64'h239 : _GEN_587; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_589 = 10'h23a == guard_index ? 64'h23a : _GEN_588; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_590 = 10'h23b == guard_index ? 64'h23b : _GEN_589; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_591 = 10'h23c == guard_index ? 64'h23c : _GEN_590; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_592 = 10'h23d == guard_index ? 64'h23d : _GEN_591; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_593 = 10'h23e == guard_index ? 64'h23e : _GEN_592; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_594 = 10'h23f == guard_index ? 64'h23f : _GEN_593; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_595 = 10'h240 == guard_index ? 64'h240 : _GEN_594; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_596 = 10'h241 == guard_index ? 64'h241 : _GEN_595; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_597 = 10'h242 == guard_index ? 64'h242 : _GEN_596; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_598 = 10'h243 == guard_index ? 64'h243 : _GEN_597; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_599 = 10'h244 == guard_index ? 64'h244 : _GEN_598; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_600 = 10'h245 == guard_index ? 64'h245 : _GEN_599; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_601 = 10'h246 == guard_index ? 64'h246 : _GEN_600; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_602 = 10'h247 == guard_index ? 64'h247 : _GEN_601; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_603 = 10'h248 == guard_index ? 64'h248 : _GEN_602; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_604 = 10'h249 == guard_index ? 64'h249 : _GEN_603; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_605 = 10'h24a == guard_index ? 64'h24a : _GEN_604; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_606 = 10'h24b == guard_index ? 64'h24b : _GEN_605; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_607 = 10'h24c == guard_index ? 64'h24c : _GEN_606; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_608 = 10'h24d == guard_index ? 64'h24d : _GEN_607; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_609 = 10'h24e == guard_index ? 64'h24e : _GEN_608; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_610 = 10'h24f == guard_index ? 64'h24f : _GEN_609; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_611 = 10'h250 == guard_index ? 64'h250 : _GEN_610; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_612 = 10'h251 == guard_index ? 64'h251 : _GEN_611; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_613 = 10'h252 == guard_index ? 64'h252 : _GEN_612; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_614 = 10'h253 == guard_index ? 64'h253 : _GEN_613; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_615 = 10'h254 == guard_index ? 64'h254 : _GEN_614; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_616 = 10'h255 == guard_index ? 64'h255 : _GEN_615; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_617 = 10'h256 == guard_index ? 64'h256 : _GEN_616; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_618 = 10'h257 == guard_index ? 64'h257 : _GEN_617; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_619 = 10'h258 == guard_index ? 64'h258 : _GEN_618; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_620 = 10'h259 == guard_index ? 64'h259 : _GEN_619; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_621 = 10'h25a == guard_index ? 64'h25a : _GEN_620; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_622 = 10'h25b == guard_index ? 64'h25b : _GEN_621; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_623 = 10'h25c == guard_index ? 64'h25c : _GEN_622; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_624 = 10'h25d == guard_index ? 64'h25d : _GEN_623; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_625 = 10'h25e == guard_index ? 64'h25e : _GEN_624; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_626 = 10'h25f == guard_index ? 64'h25f : _GEN_625; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_627 = 10'h260 == guard_index ? 64'h260 : _GEN_626; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_628 = 10'h261 == guard_index ? 64'h261 : _GEN_627; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_629 = 10'h262 == guard_index ? 64'h262 : _GEN_628; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_630 = 10'h263 == guard_index ? 64'h263 : _GEN_629; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_631 = 10'h264 == guard_index ? 64'h264 : _GEN_630; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_632 = 10'h265 == guard_index ? 64'h265 : _GEN_631; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_633 = 10'h266 == guard_index ? 64'h266 : _GEN_632; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_634 = 10'h267 == guard_index ? 64'h267 : _GEN_633; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_635 = 10'h268 == guard_index ? 64'h268 : _GEN_634; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_636 = 10'h269 == guard_index ? 64'h269 : _GEN_635; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_637 = 10'h26a == guard_index ? 64'h26a : _GEN_636; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_638 = 10'h26b == guard_index ? 64'h26b : _GEN_637; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_639 = 10'h26c == guard_index ? 64'h26c : _GEN_638; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_640 = 10'h26d == guard_index ? 64'h26d : _GEN_639; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_641 = 10'h26e == guard_index ? 64'h26e : _GEN_640; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_642 = 10'h26f == guard_index ? 64'h26f : _GEN_641; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_643 = 10'h270 == guard_index ? 64'h270 : _GEN_642; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_644 = 10'h271 == guard_index ? 64'h271 : _GEN_643; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_645 = 10'h272 == guard_index ? 64'h272 : _GEN_644; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_646 = 10'h273 == guard_index ? 64'h273 : _GEN_645; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_647 = 10'h274 == guard_index ? 64'h274 : _GEN_646; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_648 = 10'h275 == guard_index ? 64'h275 : _GEN_647; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_649 = 10'h276 == guard_index ? 64'h276 : _GEN_648; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_650 = 10'h277 == guard_index ? 64'h277 : _GEN_649; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_651 = 10'h278 == guard_index ? 64'h278 : _GEN_650; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_652 = 10'h279 == guard_index ? 64'h279 : _GEN_651; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_653 = 10'h27a == guard_index ? 64'h27a : _GEN_652; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_654 = 10'h27b == guard_index ? 64'h27b : _GEN_653; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_655 = 10'h27c == guard_index ? 64'h27c : _GEN_654; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_656 = 10'h27d == guard_index ? 64'h27d : _GEN_655; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_657 = 10'h27e == guard_index ? 64'h27e : _GEN_656; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_658 = 10'h27f == guard_index ? 64'h27f : _GEN_657; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_659 = 10'h280 == guard_index ? 64'h280 : _GEN_658; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_660 = 10'h281 == guard_index ? 64'h281 : _GEN_659; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_661 = 10'h282 == guard_index ? 64'h282 : _GEN_660; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_662 = 10'h283 == guard_index ? 64'h283 : _GEN_661; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_663 = 10'h284 == guard_index ? 64'h284 : _GEN_662; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_664 = 10'h285 == guard_index ? 64'h285 : _GEN_663; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_665 = 10'h286 == guard_index ? 64'h286 : _GEN_664; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_666 = 10'h287 == guard_index ? 64'h287 : _GEN_665; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_667 = 10'h288 == guard_index ? 64'h288 : _GEN_666; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_668 = 10'h289 == guard_index ? 64'h289 : _GEN_667; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_669 = 10'h28a == guard_index ? 64'h28a : _GEN_668; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_670 = 10'h28b == guard_index ? 64'h28b : _GEN_669; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_671 = 10'h28c == guard_index ? 64'h28c : _GEN_670; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_672 = 10'h28d == guard_index ? 64'h28d : _GEN_671; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_673 = 10'h28e == guard_index ? 64'h28e : _GEN_672; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_674 = 10'h28f == guard_index ? 64'h28f : _GEN_673; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_675 = 10'h290 == guard_index ? 64'h290 : _GEN_674; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_676 = 10'h291 == guard_index ? 64'h291 : _GEN_675; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_677 = 10'h292 == guard_index ? 64'h292 : _GEN_676; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_678 = 10'h293 == guard_index ? 64'h293 : _GEN_677; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_679 = 10'h294 == guard_index ? 64'h294 : _GEN_678; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_680 = 10'h295 == guard_index ? 64'h295 : _GEN_679; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_681 = 10'h296 == guard_index ? 64'h296 : _GEN_680; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_682 = 10'h297 == guard_index ? 64'h297 : _GEN_681; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_683 = 10'h298 == guard_index ? 64'h298 : _GEN_682; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_684 = 10'h299 == guard_index ? 64'h299 : _GEN_683; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_685 = 10'h29a == guard_index ? 64'h29a : _GEN_684; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_686 = 10'h29b == guard_index ? 64'h29b : _GEN_685; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_687 = 10'h29c == guard_index ? 64'h29c : _GEN_686; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_688 = 10'h29d == guard_index ? 64'h29d : _GEN_687; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_689 = 10'h29e == guard_index ? 64'h29e : _GEN_688; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_690 = 10'h29f == guard_index ? 64'h29f : _GEN_689; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_691 = 10'h2a0 == guard_index ? 64'h2a0 : _GEN_690; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_692 = 10'h2a1 == guard_index ? 64'h2a1 : _GEN_691; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_693 = 10'h2a2 == guard_index ? 64'h2a2 : _GEN_692; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_694 = 10'h2a3 == guard_index ? 64'h2a3 : _GEN_693; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_695 = 10'h2a4 == guard_index ? 64'h2a4 : _GEN_694; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_696 = 10'h2a5 == guard_index ? 64'h2a5 : _GEN_695; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_697 = 10'h2a6 == guard_index ? 64'h2a6 : _GEN_696; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_698 = 10'h2a7 == guard_index ? 64'h2a7 : _GEN_697; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_699 = 10'h2a8 == guard_index ? 64'h2a8 : _GEN_698; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_700 = 10'h2a9 == guard_index ? 64'h2a9 : _GEN_699; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_701 = 10'h2aa == guard_index ? 64'h2aa : _GEN_700; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_702 = 10'h2ab == guard_index ? 64'h2ab : _GEN_701; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_703 = 10'h2ac == guard_index ? 64'h2ac : _GEN_702; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_704 = 10'h2ad == guard_index ? 64'h2ad : _GEN_703; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_705 = 10'h2ae == guard_index ? 64'h2ae : _GEN_704; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_706 = 10'h2af == guard_index ? 64'h2af : _GEN_705; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_707 = 10'h2b0 == guard_index ? 64'h2b0 : _GEN_706; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_708 = 10'h2b1 == guard_index ? 64'h2b1 : _GEN_707; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_709 = 10'h2b2 == guard_index ? 64'h2b2 : _GEN_708; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_710 = 10'h2b3 == guard_index ? 64'h2b3 : _GEN_709; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_711 = 10'h2b4 == guard_index ? 64'h2b4 : _GEN_710; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_712 = 10'h2b5 == guard_index ? 64'h2b5 : _GEN_711; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_713 = 10'h2b6 == guard_index ? 64'h2b6 : _GEN_712; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_714 = 10'h2b7 == guard_index ? 64'h2b7 : _GEN_713; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_715 = 10'h2b8 == guard_index ? 64'h2b8 : _GEN_714; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_716 = 10'h2b9 == guard_index ? 64'h2b9 : _GEN_715; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_717 = 10'h2ba == guard_index ? 64'h2ba : _GEN_716; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_718 = 10'h2bb == guard_index ? 64'h2bb : _GEN_717; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_719 = 10'h2bc == guard_index ? 64'h2bc : _GEN_718; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_720 = 10'h2bd == guard_index ? 64'h2bd : _GEN_719; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_721 = 10'h2be == guard_index ? 64'h2be : _GEN_720; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_722 = 10'h2bf == guard_index ? 64'h2bf : _GEN_721; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_723 = 10'h2c0 == guard_index ? 64'h2c0 : _GEN_722; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_724 = 10'h2c1 == guard_index ? 64'h2c1 : _GEN_723; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_725 = 10'h2c2 == guard_index ? 64'h2c2 : _GEN_724; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_726 = 10'h2c3 == guard_index ? 64'h2c3 : _GEN_725; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_727 = 10'h2c4 == guard_index ? 64'h2c4 : _GEN_726; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_728 = 10'h2c5 == guard_index ? 64'h2c5 : _GEN_727; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_729 = 10'h2c6 == guard_index ? 64'h2c6 : _GEN_728; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_730 = 10'h2c7 == guard_index ? 64'h2c7 : _GEN_729; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_731 = 10'h2c8 == guard_index ? 64'h2c8 : _GEN_730; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_732 = 10'h2c9 == guard_index ? 64'h2c9 : _GEN_731; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_733 = 10'h2ca == guard_index ? 64'h2ca : _GEN_732; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_734 = 10'h2cb == guard_index ? 64'h2cb : _GEN_733; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_735 = 10'h2cc == guard_index ? 64'h2cc : _GEN_734; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_736 = 10'h2cd == guard_index ? 64'h2cd : _GEN_735; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_737 = 10'h2ce == guard_index ? 64'h2ce : _GEN_736; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_738 = 10'h2cf == guard_index ? 64'h2cf : _GEN_737; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_739 = 10'h2d0 == guard_index ? 64'h2d0 : _GEN_738; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_740 = 10'h2d1 == guard_index ? 64'h2d1 : _GEN_739; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_741 = 10'h2d2 == guard_index ? 64'h2d2 : _GEN_740; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_742 = 10'h2d3 == guard_index ? 64'h2d3 : _GEN_741; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_743 = 10'h2d4 == guard_index ? 64'h2d4 : _GEN_742; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_744 = 10'h2d5 == guard_index ? 64'h2d5 : _GEN_743; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_745 = 10'h2d6 == guard_index ? 64'h2d6 : _GEN_744; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_746 = 10'h2d7 == guard_index ? 64'h2d7 : _GEN_745; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_747 = 10'h2d8 == guard_index ? 64'h2d8 : _GEN_746; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_748 = 10'h2d9 == guard_index ? 64'h2d9 : _GEN_747; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_749 = 10'h2da == guard_index ? 64'h2da : _GEN_748; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_750 = 10'h2db == guard_index ? 64'h2db : _GEN_749; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_751 = 10'h2dc == guard_index ? 64'h2dc : _GEN_750; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_752 = 10'h2dd == guard_index ? 64'h2dd : _GEN_751; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_753 = 10'h2de == guard_index ? 64'h2de : _GEN_752; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_754 = 10'h2df == guard_index ? 64'h2df : _GEN_753; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_755 = 10'h2e0 == guard_index ? 64'h2e0 : _GEN_754; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_756 = 10'h2e1 == guard_index ? 64'h2e1 : _GEN_755; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_757 = 10'h2e2 == guard_index ? 64'h2e2 : _GEN_756; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_758 = 10'h2e3 == guard_index ? 64'h2e3 : _GEN_757; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_759 = 10'h2e4 == guard_index ? 64'h2e4 : _GEN_758; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_760 = 10'h2e5 == guard_index ? 64'h2e5 : _GEN_759; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_761 = 10'h2e6 == guard_index ? 64'h2e6 : _GEN_760; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_762 = 10'h2e7 == guard_index ? 64'h2e7 : _GEN_761; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_763 = 10'h2e8 == guard_index ? 64'h2e8 : _GEN_762; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_764 = 10'h2e9 == guard_index ? 64'h2e9 : _GEN_763; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_765 = 10'h2ea == guard_index ? 64'h2ea : _GEN_764; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_766 = 10'h2eb == guard_index ? 64'h2eb : _GEN_765; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_767 = 10'h2ec == guard_index ? 64'h2ec : _GEN_766; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_768 = 10'h2ed == guard_index ? 64'h2ed : _GEN_767; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_769 = 10'h2ee == guard_index ? 64'h2ee : _GEN_768; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_770 = 10'h2ef == guard_index ? 64'h2ef : _GEN_769; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_771 = 10'h2f0 == guard_index ? 64'h2f0 : _GEN_770; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_772 = 10'h2f1 == guard_index ? 64'h2f1 : _GEN_771; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_773 = 10'h2f2 == guard_index ? 64'h2f2 : _GEN_772; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_774 = 10'h2f3 == guard_index ? 64'h2f3 : _GEN_773; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_775 = 10'h2f4 == guard_index ? 64'h2f4 : _GEN_774; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_776 = 10'h2f5 == guard_index ? 64'h2f5 : _GEN_775; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_777 = 10'h2f6 == guard_index ? 64'h2f6 : _GEN_776; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_778 = 10'h2f7 == guard_index ? 64'h2f7 : _GEN_777; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_779 = 10'h2f8 == guard_index ? 64'h2f8 : _GEN_778; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_780 = 10'h2f9 == guard_index ? 64'h2f9 : _GEN_779; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_781 = 10'h2fa == guard_index ? 64'h2fa : _GEN_780; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_782 = 10'h2fb == guard_index ? 64'h2fb : _GEN_781; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_783 = 10'h2fc == guard_index ? 64'h2fc : _GEN_782; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_784 = 10'h2fd == guard_index ? 64'h2fd : _GEN_783; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_785 = 10'h2fe == guard_index ? 64'h2fe : _GEN_784; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_786 = 10'h2ff == guard_index ? 64'h2ff : _GEN_785; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_787 = 10'h300 == guard_index ? 64'h300 : _GEN_786; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_788 = 10'h301 == guard_index ? 64'h301 : _GEN_787; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_789 = 10'h302 == guard_index ? 64'h302 : _GEN_788; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_790 = 10'h303 == guard_index ? 64'h303 : _GEN_789; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_791 = 10'h304 == guard_index ? 64'h304 : _GEN_790; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_792 = 10'h305 == guard_index ? 64'h305 : _GEN_791; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_793 = 10'h306 == guard_index ? 64'h306 : _GEN_792; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_794 = 10'h307 == guard_index ? 64'h307 : _GEN_793; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_795 = 10'h308 == guard_index ? 64'h308 : _GEN_794; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_796 = 10'h309 == guard_index ? 64'h309 : _GEN_795; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_797 = 10'h30a == guard_index ? 64'h30a : _GEN_796; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_798 = 10'h30b == guard_index ? 64'h30b : _GEN_797; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_799 = 10'h30c == guard_index ? 64'h30c : _GEN_798; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_800 = 10'h30d == guard_index ? 64'h30d : _GEN_799; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_801 = 10'h30e == guard_index ? 64'h30e : _GEN_800; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_802 = 10'h30f == guard_index ? 64'h30f : _GEN_801; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_803 = 10'h310 == guard_index ? 64'h310 : _GEN_802; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_804 = 10'h311 == guard_index ? 64'h311 : _GEN_803; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_805 = 10'h312 == guard_index ? 64'h312 : _GEN_804; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_806 = 10'h313 == guard_index ? 64'h313 : _GEN_805; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_807 = 10'h314 == guard_index ? 64'h314 : _GEN_806; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_808 = 10'h315 == guard_index ? 64'h315 : _GEN_807; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_809 = 10'h316 == guard_index ? 64'h316 : _GEN_808; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_810 = 10'h317 == guard_index ? 64'h317 : _GEN_809; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_811 = 10'h318 == guard_index ? 64'h318 : _GEN_810; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_812 = 10'h319 == guard_index ? 64'h319 : _GEN_811; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_813 = 10'h31a == guard_index ? 64'h31a : _GEN_812; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_814 = 10'h31b == guard_index ? 64'h31b : _GEN_813; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_815 = 10'h31c == guard_index ? 64'h31c : _GEN_814; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_816 = 10'h31d == guard_index ? 64'h31d : _GEN_815; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_817 = 10'h31e == guard_index ? 64'h31e : _GEN_816; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_818 = 10'h31f == guard_index ? 64'h31f : _GEN_817; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_819 = 10'h320 == guard_index ? 64'h320 : _GEN_818; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_820 = 10'h321 == guard_index ? 64'h321 : _GEN_819; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_821 = 10'h322 == guard_index ? 64'h322 : _GEN_820; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_822 = 10'h323 == guard_index ? 64'h323 : _GEN_821; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_823 = 10'h324 == guard_index ? 64'h324 : _GEN_822; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_824 = 10'h325 == guard_index ? 64'h325 : _GEN_823; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_825 = 10'h326 == guard_index ? 64'h326 : _GEN_824; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_826 = 10'h327 == guard_index ? 64'h327 : _GEN_825; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_827 = 10'h328 == guard_index ? 64'h328 : _GEN_826; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_828 = 10'h329 == guard_index ? 64'h329 : _GEN_827; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_829 = 10'h32a == guard_index ? 64'h32a : _GEN_828; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_830 = 10'h32b == guard_index ? 64'h32b : _GEN_829; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_831 = 10'h32c == guard_index ? 64'h32c : _GEN_830; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_832 = 10'h32d == guard_index ? 64'h32d : _GEN_831; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_833 = 10'h32e == guard_index ? 64'h32e : _GEN_832; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_834 = 10'h32f == guard_index ? 64'h32f : _GEN_833; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_835 = 10'h330 == guard_index ? 64'h330 : _GEN_834; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_836 = 10'h331 == guard_index ? 64'h331 : _GEN_835; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_837 = 10'h332 == guard_index ? 64'h332 : _GEN_836; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_838 = 10'h333 == guard_index ? 64'h333 : _GEN_837; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_839 = 10'h334 == guard_index ? 64'h334 : _GEN_838; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_840 = 10'h335 == guard_index ? 64'h335 : _GEN_839; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_841 = 10'h336 == guard_index ? 64'h336 : _GEN_840; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_842 = 10'h337 == guard_index ? 64'h337 : _GEN_841; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_843 = 10'h338 == guard_index ? 64'h338 : _GEN_842; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_844 = 10'h339 == guard_index ? 64'h339 : _GEN_843; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_845 = 10'h33a == guard_index ? 64'h33a : _GEN_844; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_846 = 10'h33b == guard_index ? 64'h33b : _GEN_845; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_847 = 10'h33c == guard_index ? 64'h33c : _GEN_846; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_848 = 10'h33d == guard_index ? 64'h33d : _GEN_847; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_849 = 10'h33e == guard_index ? 64'h33e : _GEN_848; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_850 = 10'h33f == guard_index ? 64'h33f : _GEN_849; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_851 = 10'h340 == guard_index ? 64'h340 : _GEN_850; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_852 = 10'h341 == guard_index ? 64'h341 : _GEN_851; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_853 = 10'h342 == guard_index ? 64'h342 : _GEN_852; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_854 = 10'h343 == guard_index ? 64'h343 : _GEN_853; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_855 = 10'h344 == guard_index ? 64'h344 : _GEN_854; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_856 = 10'h345 == guard_index ? 64'h345 : _GEN_855; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_857 = 10'h346 == guard_index ? 64'h346 : _GEN_856; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_858 = 10'h347 == guard_index ? 64'h347 : _GEN_857; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_859 = 10'h348 == guard_index ? 64'h348 : _GEN_858; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_860 = 10'h349 == guard_index ? 64'h349 : _GEN_859; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_861 = 10'h34a == guard_index ? 64'h34a : _GEN_860; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_862 = 10'h34b == guard_index ? 64'h34b : _GEN_861; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_863 = 10'h34c == guard_index ? 64'h34c : _GEN_862; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_864 = 10'h34d == guard_index ? 64'h34d : _GEN_863; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_865 = 10'h34e == guard_index ? 64'h34e : _GEN_864; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_866 = 10'h34f == guard_index ? 64'h34f : _GEN_865; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_867 = 10'h350 == guard_index ? 64'h350 : _GEN_866; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_868 = 10'h351 == guard_index ? 64'h351 : _GEN_867; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_869 = 10'h352 == guard_index ? 64'h352 : _GEN_868; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_870 = 10'h353 == guard_index ? 64'h353 : _GEN_869; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_871 = 10'h354 == guard_index ? 64'h354 : _GEN_870; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_872 = 10'h355 == guard_index ? 64'h355 : _GEN_871; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_873 = 10'h356 == guard_index ? 64'h356 : _GEN_872; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_874 = 10'h357 == guard_index ? 64'h357 : _GEN_873; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_875 = 10'h358 == guard_index ? 64'h358 : _GEN_874; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_876 = 10'h359 == guard_index ? 64'h359 : _GEN_875; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_877 = 10'h35a == guard_index ? 64'h35a : _GEN_876; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_878 = 10'h35b == guard_index ? 64'h35b : _GEN_877; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_879 = 10'h35c == guard_index ? 64'h35c : _GEN_878; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_880 = 10'h35d == guard_index ? 64'h35d : _GEN_879; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_881 = 10'h35e == guard_index ? 64'h35e : _GEN_880; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_882 = 10'h35f == guard_index ? 64'h35f : _GEN_881; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_883 = 10'h360 == guard_index ? 64'h360 : _GEN_882; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_884 = 10'h361 == guard_index ? 64'h361 : _GEN_883; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_885 = 10'h362 == guard_index ? 64'h362 : _GEN_884; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_886 = 10'h363 == guard_index ? 64'h363 : _GEN_885; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_887 = 10'h364 == guard_index ? 64'h364 : _GEN_886; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_888 = 10'h365 == guard_index ? 64'h365 : _GEN_887; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_889 = 10'h366 == guard_index ? 64'h366 : _GEN_888; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_890 = 10'h367 == guard_index ? 64'h367 : _GEN_889; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_891 = 10'h368 == guard_index ? 64'h368 : _GEN_890; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_892 = 10'h369 == guard_index ? 64'h369 : _GEN_891; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_893 = 10'h36a == guard_index ? 64'h36a : _GEN_892; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_894 = 10'h36b == guard_index ? 64'h36b : _GEN_893; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_895 = 10'h36c == guard_index ? 64'h36c : _GEN_894; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_896 = 10'h36d == guard_index ? 64'h36d : _GEN_895; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_897 = 10'h36e == guard_index ? 64'h36e : _GEN_896; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_898 = 10'h36f == guard_index ? 64'h36f : _GEN_897; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_899 = 10'h370 == guard_index ? 64'h370 : _GEN_898; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_900 = 10'h371 == guard_index ? 64'h371 : _GEN_899; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_901 = 10'h372 == guard_index ? 64'h372 : _GEN_900; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_902 = 10'h373 == guard_index ? 64'h373 : _GEN_901; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_903 = 10'h374 == guard_index ? 64'h374 : _GEN_902; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_904 = 10'h375 == guard_index ? 64'h375 : _GEN_903; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_905 = 10'h376 == guard_index ? 64'h376 : _GEN_904; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_906 = 10'h377 == guard_index ? 64'h377 : _GEN_905; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_907 = 10'h378 == guard_index ? 64'h378 : _GEN_906; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_908 = 10'h379 == guard_index ? 64'h379 : _GEN_907; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_909 = 10'h37a == guard_index ? 64'h37a : _GEN_908; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_910 = 10'h37b == guard_index ? 64'h37b : _GEN_909; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_911 = 10'h37c == guard_index ? 64'h37c : _GEN_910; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_912 = 10'h37d == guard_index ? 64'h37d : _GEN_911; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_913 = 10'h37e == guard_index ? 64'h37e : _GEN_912; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_914 = 10'h37f == guard_index ? 64'h37f : _GEN_913; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_915 = 10'h380 == guard_index ? 64'h380 : _GEN_914; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_916 = 10'h381 == guard_index ? 64'h381 : _GEN_915; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_917 = 10'h382 == guard_index ? 64'h382 : _GEN_916; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_918 = 10'h383 == guard_index ? 64'h383 : _GEN_917; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_919 = 10'h384 == guard_index ? 64'h384 : _GEN_918; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_920 = 10'h385 == guard_index ? 64'h385 : _GEN_919; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_921 = 10'h386 == guard_index ? 64'h386 : _GEN_920; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_922 = 10'h387 == guard_index ? 64'h387 : _GEN_921; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_923 = 10'h388 == guard_index ? 64'h388 : _GEN_922; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_924 = 10'h389 == guard_index ? 64'h389 : _GEN_923; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_925 = 10'h38a == guard_index ? 64'h38a : _GEN_924; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_926 = 10'h38b == guard_index ? 64'h38b : _GEN_925; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_927 = 10'h38c == guard_index ? 64'h38c : _GEN_926; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_928 = 10'h38d == guard_index ? 64'h38d : _GEN_927; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_929 = 10'h38e == guard_index ? 64'h38e : _GEN_928; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_930 = 10'h38f == guard_index ? 64'h38f : _GEN_929; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_931 = 10'h390 == guard_index ? 64'h390 : _GEN_930; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_932 = 10'h391 == guard_index ? 64'h391 : _GEN_931; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_933 = 10'h392 == guard_index ? 64'h392 : _GEN_932; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_934 = 10'h393 == guard_index ? 64'h393 : _GEN_933; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_935 = 10'h394 == guard_index ? 64'h394 : _GEN_934; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_936 = 10'h395 == guard_index ? 64'h395 : _GEN_935; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_937 = 10'h396 == guard_index ? 64'h396 : _GEN_936; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_938 = 10'h397 == guard_index ? 64'h397 : _GEN_937; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_939 = 10'h398 == guard_index ? 64'h398 : _GEN_938; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_940 = 10'h399 == guard_index ? 64'h399 : _GEN_939; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_941 = 10'h39a == guard_index ? 64'h39a : _GEN_940; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_942 = 10'h39b == guard_index ? 64'h39b : _GEN_941; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_943 = 10'h39c == guard_index ? 64'h39c : _GEN_942; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_944 = 10'h39d == guard_index ? 64'h39d : _GEN_943; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_945 = 10'h39e == guard_index ? 64'h39e : _GEN_944; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_946 = 10'h39f == guard_index ? 64'h39f : _GEN_945; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_947 = 10'h3a0 == guard_index ? 64'h3a0 : _GEN_946; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_948 = 10'h3a1 == guard_index ? 64'h3a1 : _GEN_947; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_949 = 10'h3a2 == guard_index ? 64'h3a2 : _GEN_948; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_950 = 10'h3a3 == guard_index ? 64'h3a3 : _GEN_949; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_951 = 10'h3a4 == guard_index ? 64'h3a4 : _GEN_950; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_952 = 10'h3a5 == guard_index ? 64'h3a5 : _GEN_951; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_953 = 10'h3a6 == guard_index ? 64'h3a6 : _GEN_952; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_954 = 10'h3a7 == guard_index ? 64'h3a7 : _GEN_953; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_955 = 10'h3a8 == guard_index ? 64'h3a8 : _GEN_954; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_956 = 10'h3a9 == guard_index ? 64'h3a9 : _GEN_955; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_957 = 10'h3aa == guard_index ? 64'h3aa : _GEN_956; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_958 = 10'h3ab == guard_index ? 64'h3ab : _GEN_957; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_959 = 10'h3ac == guard_index ? 64'h3ac : _GEN_958; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_960 = 10'h3ad == guard_index ? 64'h3ad : _GEN_959; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_961 = 10'h3ae == guard_index ? 64'h3ae : _GEN_960; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_962 = 10'h3af == guard_index ? 64'h3af : _GEN_961; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_963 = 10'h3b0 == guard_index ? 64'h3b0 : _GEN_962; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_964 = 10'h3b1 == guard_index ? 64'h3b1 : _GEN_963; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_965 = 10'h3b2 == guard_index ? 64'h3b2 : _GEN_964; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_966 = 10'h3b3 == guard_index ? 64'h3b3 : _GEN_965; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_967 = 10'h3b4 == guard_index ? 64'h3b4 : _GEN_966; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_968 = 10'h3b5 == guard_index ? 64'h3b5 : _GEN_967; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_969 = 10'h3b6 == guard_index ? 64'h3b6 : _GEN_968; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_970 = 10'h3b7 == guard_index ? 64'h3b7 : _GEN_969; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_971 = 10'h3b8 == guard_index ? 64'h3b8 : _GEN_970; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_972 = 10'h3b9 == guard_index ? 64'h3b9 : _GEN_971; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_973 = 10'h3ba == guard_index ? 64'h3ba : _GEN_972; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_974 = 10'h3bb == guard_index ? 64'h3bb : _GEN_973; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_975 = 10'h3bc == guard_index ? 64'h3bc : _GEN_974; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_976 = 10'h3bd == guard_index ? 64'h3bd : _GEN_975; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_977 = 10'h3be == guard_index ? 64'h3be : _GEN_976; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_978 = 10'h3bf == guard_index ? 64'h3bf : _GEN_977; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_979 = 10'h3c0 == guard_index ? 64'h3c0 : _GEN_978; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_980 = 10'h3c1 == guard_index ? 64'h3c1 : _GEN_979; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_981 = 10'h3c2 == guard_index ? 64'h3c2 : _GEN_980; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_982 = 10'h3c3 == guard_index ? 64'h3c3 : _GEN_981; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_983 = 10'h3c4 == guard_index ? 64'h3c4 : _GEN_982; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_984 = 10'h3c5 == guard_index ? 64'h3c5 : _GEN_983; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_985 = 10'h3c6 == guard_index ? 64'h3c6 : _GEN_984; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_986 = 10'h3c7 == guard_index ? 64'h3c7 : _GEN_985; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_987 = 10'h3c8 == guard_index ? 64'h3c8 : _GEN_986; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_988 = 10'h3c9 == guard_index ? 64'h3c9 : _GEN_987; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_989 = 10'h3ca == guard_index ? 64'h3ca : _GEN_988; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_990 = 10'h3cb == guard_index ? 64'h3cb : _GEN_989; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_991 = 10'h3cc == guard_index ? 64'h3cc : _GEN_990; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_992 = 10'h3cd == guard_index ? 64'h3cd : _GEN_991; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_993 = 10'h3ce == guard_index ? 64'h3ce : _GEN_992; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_994 = 10'h3cf == guard_index ? 64'h3cf : _GEN_993; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_995 = 10'h3d0 == guard_index ? 64'h3d0 : _GEN_994; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_996 = 10'h3d1 == guard_index ? 64'h3d1 : _GEN_995; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_997 = 10'h3d2 == guard_index ? 64'h3d2 : _GEN_996; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_998 = 10'h3d3 == guard_index ? 64'h3d3 : _GEN_997; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_999 = 10'h3d4 == guard_index ? 64'h3d4 : _GEN_998; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1000 = 10'h3d5 == guard_index ? 64'h3d5 : _GEN_999; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1001 = 10'h3d6 == guard_index ? 64'h3d6 : _GEN_1000; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1002 = 10'h3d7 == guard_index ? 64'h3d7 : _GEN_1001; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1003 = 10'h3d8 == guard_index ? 64'h3d8 : _GEN_1002; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1004 = 10'h3d9 == guard_index ? 64'h3d9 : _GEN_1003; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1005 = 10'h3da == guard_index ? 64'h3da : _GEN_1004; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1006 = 10'h3db == guard_index ? 64'h3db : _GEN_1005; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1007 = 10'h3dc == guard_index ? 64'h3dc : _GEN_1006; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1008 = 10'h3dd == guard_index ? 64'h3dd : _GEN_1007; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1009 = 10'h3de == guard_index ? 64'h3de : _GEN_1008; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1010 = 10'h3df == guard_index ? 64'h3df : _GEN_1009; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1011 = 10'h3e0 == guard_index ? 64'h3e0 : _GEN_1010; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1012 = 10'h3e1 == guard_index ? 64'h3e1 : _GEN_1011; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1013 = 10'h3e2 == guard_index ? 64'h3e2 : _GEN_1012; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1014 = 10'h3e3 == guard_index ? 64'h3e3 : _GEN_1013; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1015 = 10'h3e4 == guard_index ? 64'h3e4 : _GEN_1014; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1016 = 10'h3e5 == guard_index ? 64'h3e5 : _GEN_1015; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1017 = 10'h3e6 == guard_index ? 64'h3e6 : _GEN_1016; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1018 = 10'h3e7 == guard_index ? 64'h3e7 : _GEN_1017; // @[ComputeNode.scala 155:26]
  wire  _T_36 = FU_io_out != _GEN_1018; // @[ComputeNode.scala 155:26]
  wire  _T_22 = right_valid_R & left_valid_R; // @[ComputeNode.scala 71:19]
  wire  _T_23 = enable_valid_R & _T_22; // @[ComputeNode.scala 75:20]
  wire  _T_24 = _T_23 & enable_R_control; // @[ComputeNode.scala 75:38]
  wire  _T_26 = _T_24 & _T_32; // @[ComputeNode.scala 75:58]
  wire  _T_28 = guard_index == 10'h3e7; // @[Counter.scala 38:24]
  wire [9:0] _T_30 = guard_index + 10'h1; // @[Counter.scala 39:22]
  wire [63:0] _T_38_data = FU_io_out; // @[interfaces.scala 289:20 interfaces.scala 290:15]
  wire [63:0] _GEN_1021 = _T_36 ? _GEN_1018 : _T_38_data; // @[ComputeNode.scala 155:61]
  wire  _T_40 = _T_1 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire [63:0] _GEN_1026 = _T_34 ? _GEN_1021 : out_data_R; // @[ComputeNode.scala 147:81]
  wire  _GEN_1029 = _T_34 | out_valid_R_0; // @[ComputeNode.scala 147:81]
  wire  _GEN_1033 = _T_34 | state; // @[ComputeNode.scala 147:81]
  wire  _T_43 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  UALU_1 FU ( // @[ComputeNode.scala 61:18]
    .io_in1(FU_io_in1),
    .io_in2(FU_io_in2),
    .io_out(FU_io_out)
  );
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 205:19]
  assign io_Out_0_valid = _T_32 ? _GEN_1029 : out_valid_R_0; // @[HandShaking.scala 194:21 ComputeNode.scala 172:32]
  assign io_Out_0_bits_data = _T_32 ? _GEN_1026 : out_data_R; // @[ComputeNode.scala 137:25 ComputeNode.scala 158:35 ComputeNode.scala 166:35]
  assign io_LeftIO_ready = ~left_valid_R; // @[ComputeNode.scala 104:19]
  assign io_RightIO_ready = ~right_valid_R; // @[ComputeNode.scala 110:20]
  assign FU_io_in1 = left_R_data; // @[ComputeNode.scala 101:13]
  assign FU_io_in2 = right_R_data; // @[ComputeNode.scala 102:13]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {2{`RANDOM}};
  left_R_data = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  left_valid_R = _RAND_5[0:0];
  _RAND_6 = {2{`RANDOM}};
  right_R_data = _RAND_6[63:0];
  _RAND_7 = {1{`RANDOM}};
  right_valid_R = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  state = _RAND_8[0:0];
  _RAND_9 = {2{`RANDOM}};
  out_data_R = _RAND_9[63:0];
  _RAND_10 = {1{`RANDOM}};
  guard_index = _RAND_10[9:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_3) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_43) begin
        enable_valid_R <= 1'h0;
      end else if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_3) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_32) begin
      if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_43) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_1) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        out_valid_R_0 <= _T_40;
      end else if (_T_1) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_1) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      left_R_data <= 64'h0;
    end else if (_T_12) begin
      left_R_data <= io_LeftIO_bits_data;
    end
    if (reset) begin
      left_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        left_valid_R <= 1'h0;
      end else begin
        left_valid_R <= _GEN_11;
      end
    end else begin
      left_valid_R <= _GEN_11;
    end
    if (reset) begin
      right_R_data <= 64'h0;
    end else if (_T_14) begin
      right_R_data <= io_RightIO_bits_data;
    end
    if (reset) begin
      right_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        right_valid_R <= 1'h0;
      end else begin
        right_valid_R <= _GEN_15;
      end
    end else begin
      right_valid_R <= _GEN_15;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_32) begin
      state <= _GEN_1033;
    end else if (state) begin
      if (_T_43) begin
        state <= 1'h0;
      end
    end
    if (reset) begin
      out_data_R <= 64'h0;
    end else if (_T_32) begin
      if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (state) begin
      if (_T_43) begin
        out_data_R <= 64'h0;
      end else if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (enable_R_control) begin
      out_data_R <= FU_io_out;
    end else begin
      out_data_R <= 64'h0;
    end
    if (reset) begin
      guard_index <= 10'h0;
    end else if (_T_26) begin
      if (_T_28) begin
        guard_index <= 10'h0;
      end else begin
        guard_index <= _T_30;
      end
    end
  end
endmodule
module GepNode_1(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  input         io_Out_1_ready,
  output        io_Out_1_valid,
  output [63:0] io_Out_1_bits_data,
  output        io_baseAddress_ready,
  input         io_baseAddress_valid,
  input  [63:0] io_baseAddress_bits_data,
  output        io_idx_0_ready,
  input         io_idx_0_valid,
  input  [63:0] io_idx_0_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [63:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [63:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
`endif // RANDOMIZE_REG_INIT
  reg  enable_valid_R; // @[HandShaking.scala 182:31]
  reg  out_ready_R_0; // @[HandShaking.scala 185:46]
  reg  out_ready_R_1; // @[HandShaking.scala 185:46]
  reg  out_valid_R_0; // @[HandShaking.scala 186:46]
  reg  out_valid_R_1; // @[HandShaking.scala 186:46]
  wire  _T_1 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_2 = io_Out_1_ready & io_Out_1_valid; // @[Decoupled.scala 40:37]
  wire  _T_4 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] base_addr_R_data; // @[GepNode.scala 884:28]
  reg  base_addr_valid_R; // @[GepNode.scala 885:34]
  reg [63:0] idx_R_0_data; // @[GepNode.scala 888:39]
  reg  idx_valid_R_0; // @[GepNode.scala 889:45]
  reg  state; // @[GepNode.scala 893:22]
  wire  _T_12 = io_baseAddress_ready & io_baseAddress_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_13 = _T_12 | base_addr_valid_R; // @[GepNode.scala 909:31]
  wire  _T_14 = io_idx_0_ready & io_idx_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_17 = _T_14 | idx_valid_R_0; // @[GepNode.scala 916:28]
  wire [67:0] seek_value = idx_R_0_data * 64'h8; // @[GepNode.scala 924:21]
  wire [67:0] _GEN_59 = {{4'd0}, base_addr_R_data}; // @[GepNode.scala 932:35]
  wire [67:0] data_out = _GEN_59 + seek_value; // @[GepNode.scala 932:35]
  wire  _T_16 = ~state; // @[Conditional.scala 37:30]
  wire  _T_17 = enable_valid_R & base_addr_valid_R; // @[GepNode.scala 948:27]
  wire  _T_18 = _T_17 & idx_valid_R_0; // @[GepNode.scala 948:48]
  wire  _T_21 = _T_1 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire  _T_22 = _T_2 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire  _GEN_20 = _T_18 | state; // @[GepNode.scala 948:78]
  wire  _T_26 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  wire  _T_27 = out_ready_R_1 | _T_2; // @[HandShaking.scala 251:83]
  wire  _T_28 = _T_26 & _T_27; // @[HandShaking.scala 252:27]
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 205:19]
  assign io_Out_0_valid = out_valid_R_0; // @[HandShaking.scala 194:21]
  assign io_Out_0_bits_data = data_out[63:0]; // @[GepNode.scala 936:25]
  assign io_Out_1_valid = out_valid_R_1; // @[HandShaking.scala 194:21]
  assign io_Out_1_bits_data = data_out[63:0]; // @[GepNode.scala 936:25]
  assign io_baseAddress_ready = ~base_addr_valid_R; // @[GepNode.scala 908:24]
  assign io_idx_0_ready = ~idx_valid_R_0; // @[GepNode.scala 915:21]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_valid_R = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_1 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  out_valid_R_1 = _RAND_4[0:0];
  _RAND_5 = {2{`RANDOM}};
  base_addr_R_data = _RAND_5[63:0];
  _RAND_6 = {1{`RANDOM}};
  base_addr_valid_R = _RAND_6[0:0];
  _RAND_7 = {2{`RANDOM}};
  idx_R_0_data = _RAND_7[63:0];
  _RAND_8 = {1{`RANDOM}};
  idx_valid_R_0 = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  state = _RAND_9[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_16) begin
      if (_T_4) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_28) begin
        enable_valid_R <= 1'h0;
      end else if (_T_4) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_4) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_16) begin
      if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_28) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_1) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_ready_R_1 <= 1'h0;
    end else if (_T_16) begin
      if (_T_2) begin
        out_ready_R_1 <= io_Out_1_ready;
      end
    end else if (state) begin
      if (_T_28) begin
        out_ready_R_1 <= 1'h0;
      end else if (_T_2) begin
        out_ready_R_1 <= io_Out_1_ready;
      end
    end else if (_T_2) begin
      out_ready_R_1 <= io_Out_1_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_16) begin
      if (_T_18) begin
        out_valid_R_0 <= _T_21;
      end else if (_T_1) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_1) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_1 <= 1'h0;
    end else if (_T_16) begin
      if (_T_18) begin
        out_valid_R_1 <= _T_22;
      end else if (_T_2) begin
        out_valid_R_1 <= 1'h0;
      end
    end else if (_T_2) begin
      out_valid_R_1 <= 1'h0;
    end
    if (reset) begin
      base_addr_R_data <= 64'h0;
    end else if (_T_16) begin
      if (_T_12) begin
        base_addr_R_data <= io_baseAddress_bits_data;
      end
    end else if (state) begin
      if (_T_28) begin
        base_addr_R_data <= 64'h0;
      end else if (_T_12) begin
        base_addr_R_data <= io_baseAddress_bits_data;
      end
    end else if (_T_12) begin
      base_addr_R_data <= io_baseAddress_bits_data;
    end
    if (reset) begin
      base_addr_valid_R <= 1'h0;
    end else if (_T_16) begin
      base_addr_valid_R <= _GEN_13;
    end else if (state) begin
      if (_T_28) begin
        base_addr_valid_R <= 1'h0;
      end else begin
        base_addr_valid_R <= _GEN_13;
      end
    end else begin
      base_addr_valid_R <= _GEN_13;
    end
    if (reset) begin
      idx_R_0_data <= 64'h0;
    end else if (_T_16) begin
      if (_T_14) begin
        idx_R_0_data <= io_idx_0_bits_data;
      end
    end else if (state) begin
      if (_T_28) begin
        idx_R_0_data <= 64'h0;
      end else if (_T_14) begin
        idx_R_0_data <= io_idx_0_bits_data;
      end
    end else if (_T_14) begin
      idx_R_0_data <= io_idx_0_bits_data;
    end
    if (reset) begin
      idx_valid_R_0 <= 1'h0;
    end else if (_T_16) begin
      idx_valid_R_0 <= _GEN_17;
    end else if (state) begin
      if (_T_28) begin
        idx_valid_R_0 <= 1'h0;
      end else begin
        idx_valid_R_0 <= _GEN_17;
      end
    end else begin
      idx_valid_R_0 <= _GEN_17;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_16) begin
      state <= _GEN_20;
    end else if (state) begin
      if (_T_28) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module UnTypLoadCache_1(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  output        io_GepAddr_ready,
  input         io_GepAddr_valid,
  input  [63:0] io_GepAddr_bits_data,
  input         io_MemReq_ready,
  output        io_MemReq_valid,
  output [63:0] io_MemReq_bits_addr,
  input         io_MemResp_valid,
  input  [63:0] io_MemResp_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [31:0] _RAND_7;
`endif // RANDOMIZE_REG_INIT
  reg  enable_R_control; // @[HandShaking.scala 592:31]
  reg  enable_valid_R; // @[HandShaking.scala 593:31]
  reg  out_ready_R_0; // @[HandShaking.scala 605:28]
  reg  out_valid_R_0; // @[HandShaking.scala 606:28]
  wire  _T_4 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_1 = _T_4 ? 1'h0 : out_valid_R_0; // @[HandShaking.scala 632:29]
  wire  _T_6 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] addr_R_data; // @[LoadCache.scala 58:23]
  reg  addr_valid_R; // @[LoadCache.scala 59:29]
  reg [63:0] data_R_data; // @[LoadCache.scala 62:23]
  reg [1:0] state; // @[LoadCache.scala 67:22]
  wire  _T_14 = io_GepAddr_ready & io_GepAddr_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_14 | addr_valid_R; // @[LoadCache.scala 76:27]
  wire  _T_15 = enable_valid_R & addr_valid_R; // @[LoadCache.scala 95:20]
  wire  _T_16 = _T_15 & enable_R_control; // @[LoadCache.scala 95:36]
  wire  _T_23 = &out_ready_R_0; // @[HandShaking.scala 725:24]
  wire  _T_24 = &io_Out_0_ready; // @[HandShaking.scala 725:50]
  wire  _T_25 = _T_23 | _T_24; // @[HandShaking.scala 725:29]
  wire  _T_44 = 2'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_49 = _T_4 ^ 1'h1; // @[HandShaking.scala 729:72]
  wire  _T_50 = 2'h1 == state; // @[Conditional.scala 37:30]
  wire  _GEN_25 = io_MemResp_valid | _GEN_1; // @[LoadCache.scala 214:30]
  wire  _T_51 = 2'h2 == state; // @[Conditional.scala 37:30]
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 650:19]
  assign io_Out_0_valid = out_valid_R_0; // @[HandShaking.scala 630:21]
  assign io_Out_0_bits_data = data_R_data; // @[LoadCache.scala 158:20]
  assign io_GepAddr_ready = ~addr_valid_R; // @[LoadCache.scala 75:20]
  assign io_MemReq_valid = _T_44 & _T_16; // @[LoadCache.scala 162:19 LoadCache.scala 185:27]
  assign io_MemReq_bits_addr = addr_R_data; // @[LoadCache.scala 164:23]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {2{`RANDOM}};
  addr_R_data = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  addr_valid_R = _RAND_5[0:0];
  _RAND_6 = {2{`RANDOM}};
  data_R_data = _RAND_6[63:0];
  _RAND_7 = {1{`RANDOM}};
  state = _RAND_7[1:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_6) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_44) begin
      if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_50) begin
      if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        enable_valid_R <= 1'h0;
      end else if (_T_6) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_6) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_44) begin
      if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_50) begin
      if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_4) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_4) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_44) begin
      if (_T_15) begin
        if (enable_R_control) begin
          if (_T_4) begin
            out_valid_R_0 <= 1'h0;
          end
        end else begin
          out_valid_R_0 <= _T_49;
        end
      end else if (_T_4) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_50) begin
      out_valid_R_0 <= _GEN_25;
    end else if (_T_4) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      addr_R_data <= 64'h0;
    end else if (_T_44) begin
      if (_T_14) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_50) begin
      if (_T_14) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        addr_R_data <= 64'h0;
      end else if (_T_14) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_14) begin
      addr_R_data <= io_GepAddr_bits_data;
    end
    if (reset) begin
      addr_valid_R <= 1'h0;
    end else if (_T_44) begin
      addr_valid_R <= _GEN_11;
    end else if (_T_50) begin
      addr_valid_R <= _GEN_11;
    end else if (_T_51) begin
      if (_T_25) begin
        addr_valid_R <= 1'h0;
      end else begin
        addr_valid_R <= _GEN_11;
      end
    end else begin
      addr_valid_R <= _GEN_11;
    end
    if (reset) begin
      data_R_data <= 64'h0;
    end else if (!(_T_44)) begin
      if (_T_50) begin
        if (io_MemResp_valid) begin
          data_R_data <= io_MemResp_bits_data;
        end
      end else if (_T_51) begin
        if (_T_25) begin
          data_R_data <= 64'h0;
        end
      end
    end
    if (reset) begin
      state <= 2'h0;
    end else if (_T_44) begin
      if (_T_15) begin
        if (enable_R_control) begin
          if (io_MemReq_ready) begin
            state <= 2'h1;
          end
        end else begin
          state <= 2'h2;
        end
      end
    end else if (_T_50) begin
      if (io_MemResp_valid) begin
        state <= 2'h2;
      end
    end else if (_T_51) begin
      if (_T_25) begin
        state <= 2'h0;
      end
    end
  end
endmodule
module UALU_2(
  input  [63:0] io_in1,
  input  [63:0] io_in2,
  output [63:0] io_out
);
  assign io_out = io_in1 + io_in2; // @[Alu.scala 235:10]
endmodule
module ComputeNode_2(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  output        io_LeftIO_ready,
  input         io_LeftIO_valid,
  input  [63:0] io_LeftIO_bits_data,
  output        io_RightIO_ready,
  input         io_RightIO_valid,
  input  [63:0] io_RightIO_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [63:0] _RAND_9;
  reg [31:0] _RAND_10;
`endif // RANDOMIZE_REG_INIT
  wire [63:0] FU_io_in1; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_in2; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_out; // @[ComputeNode.scala 61:18]
  reg  enable_R_control; // @[HandShaking.scala 181:31]
  reg  enable_valid_R; // @[HandShaking.scala 182:31]
  reg  out_ready_R_0; // @[HandShaking.scala 185:46]
  reg  out_valid_R_0; // @[HandShaking.scala 186:46]
  wire  _T_1 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_3 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] left_R_data; // @[ComputeNode.scala 53:23]
  reg  left_valid_R; // @[ComputeNode.scala 54:29]
  reg [63:0] right_R_data; // @[ComputeNode.scala 57:24]
  reg  right_valid_R; // @[ComputeNode.scala 58:30]
  reg  state; // @[ComputeNode.scala 64:22]
  reg [63:0] out_data_R; // @[ComputeNode.scala 89:27]
  wire  _T_12 = io_LeftIO_ready & io_LeftIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_12 | left_valid_R; // @[ComputeNode.scala 105:26]
  wire  _T_14 = io_RightIO_ready & io_RightIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_14 | right_valid_R; // @[ComputeNode.scala 111:27]
  wire  _T_32 = ~state; // @[Conditional.scala 37:30]
  wire  _T_33 = enable_valid_R & left_valid_R; // @[ComputeNode.scala 147:27]
  wire  _T_34 = _T_33 & right_valid_R; // @[ComputeNode.scala 147:43]
  reg [9:0] guard_index; // @[Counter.scala 29:33]
  wire [63:0] _GEN_20 = 10'h1 == guard_index ? 64'h1 : 64'h0; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_21 = 10'h2 == guard_index ? 64'h2 : _GEN_20; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_22 = 10'h3 == guard_index ? 64'h3 : _GEN_21; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_23 = 10'h4 == guard_index ? 64'h4 : _GEN_22; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_24 = 10'h5 == guard_index ? 64'h5 : _GEN_23; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_25 = 10'h6 == guard_index ? 64'h6 : _GEN_24; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_26 = 10'h7 == guard_index ? 64'h7 : _GEN_25; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_27 = 10'h8 == guard_index ? 64'h8 : _GEN_26; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_28 = 10'h9 == guard_index ? 64'h9 : _GEN_27; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_29 = 10'ha == guard_index ? 64'ha : _GEN_28; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_30 = 10'hb == guard_index ? 64'hb : _GEN_29; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_31 = 10'hc == guard_index ? 64'hc : _GEN_30; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_32 = 10'hd == guard_index ? 64'hd : _GEN_31; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_33 = 10'he == guard_index ? 64'he : _GEN_32; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_34 = 10'hf == guard_index ? 64'hf : _GEN_33; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_35 = 10'h10 == guard_index ? 64'h10 : _GEN_34; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_36 = 10'h11 == guard_index ? 64'h11 : _GEN_35; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_37 = 10'h12 == guard_index ? 64'h12 : _GEN_36; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_38 = 10'h13 == guard_index ? 64'h13 : _GEN_37; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_39 = 10'h14 == guard_index ? 64'h14 : _GEN_38; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_40 = 10'h15 == guard_index ? 64'h15 : _GEN_39; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_41 = 10'h16 == guard_index ? 64'h16 : _GEN_40; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_42 = 10'h17 == guard_index ? 64'h17 : _GEN_41; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_43 = 10'h18 == guard_index ? 64'h18 : _GEN_42; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_44 = 10'h19 == guard_index ? 64'h19 : _GEN_43; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_45 = 10'h1a == guard_index ? 64'h1a : _GEN_44; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_46 = 10'h1b == guard_index ? 64'h1b : _GEN_45; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_47 = 10'h1c == guard_index ? 64'h1c : _GEN_46; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_48 = 10'h1d == guard_index ? 64'h1d : _GEN_47; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_49 = 10'h1e == guard_index ? 64'h1e : _GEN_48; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_50 = 10'h1f == guard_index ? 64'h1f : _GEN_49; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_51 = 10'h20 == guard_index ? 64'h20 : _GEN_50; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_52 = 10'h21 == guard_index ? 64'h21 : _GEN_51; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_53 = 10'h22 == guard_index ? 64'h22 : _GEN_52; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_54 = 10'h23 == guard_index ? 64'h23 : _GEN_53; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_55 = 10'h24 == guard_index ? 64'h24 : _GEN_54; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_56 = 10'h25 == guard_index ? 64'h25 : _GEN_55; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_57 = 10'h26 == guard_index ? 64'h26 : _GEN_56; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_58 = 10'h27 == guard_index ? 64'h27 : _GEN_57; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_59 = 10'h28 == guard_index ? 64'h28 : _GEN_58; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_60 = 10'h29 == guard_index ? 64'h29 : _GEN_59; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_61 = 10'h2a == guard_index ? 64'h2a : _GEN_60; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_62 = 10'h2b == guard_index ? 64'h2b : _GEN_61; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_63 = 10'h2c == guard_index ? 64'h2c : _GEN_62; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_64 = 10'h2d == guard_index ? 64'h2d : _GEN_63; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_65 = 10'h2e == guard_index ? 64'h2e : _GEN_64; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_66 = 10'h2f == guard_index ? 64'h2f : _GEN_65; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_67 = 10'h30 == guard_index ? 64'h30 : _GEN_66; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_68 = 10'h31 == guard_index ? 64'h31 : _GEN_67; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_69 = 10'h32 == guard_index ? 64'h32 : _GEN_68; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_70 = 10'h33 == guard_index ? 64'h33 : _GEN_69; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_71 = 10'h34 == guard_index ? 64'h34 : _GEN_70; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_72 = 10'h35 == guard_index ? 64'h35 : _GEN_71; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_73 = 10'h36 == guard_index ? 64'h36 : _GEN_72; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_74 = 10'h37 == guard_index ? 64'h37 : _GEN_73; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_75 = 10'h38 == guard_index ? 64'h38 : _GEN_74; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_76 = 10'h39 == guard_index ? 64'h39 : _GEN_75; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_77 = 10'h3a == guard_index ? 64'h3a : _GEN_76; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_78 = 10'h3b == guard_index ? 64'h3b : _GEN_77; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_79 = 10'h3c == guard_index ? 64'h3c : _GEN_78; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_80 = 10'h3d == guard_index ? 64'h3d : _GEN_79; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_81 = 10'h3e == guard_index ? 64'h3e : _GEN_80; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_82 = 10'h3f == guard_index ? 64'h3f : _GEN_81; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_83 = 10'h40 == guard_index ? 64'h40 : _GEN_82; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_84 = 10'h41 == guard_index ? 64'h41 : _GEN_83; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_85 = 10'h42 == guard_index ? 64'h42 : _GEN_84; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_86 = 10'h43 == guard_index ? 64'h43 : _GEN_85; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_87 = 10'h44 == guard_index ? 64'h44 : _GEN_86; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_88 = 10'h45 == guard_index ? 64'h45 : _GEN_87; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_89 = 10'h46 == guard_index ? 64'h46 : _GEN_88; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_90 = 10'h47 == guard_index ? 64'h47 : _GEN_89; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_91 = 10'h48 == guard_index ? 64'h48 : _GEN_90; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_92 = 10'h49 == guard_index ? 64'h49 : _GEN_91; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_93 = 10'h4a == guard_index ? 64'h4a : _GEN_92; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_94 = 10'h4b == guard_index ? 64'h4b : _GEN_93; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_95 = 10'h4c == guard_index ? 64'h4c : _GEN_94; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_96 = 10'h4d == guard_index ? 64'h4d : _GEN_95; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_97 = 10'h4e == guard_index ? 64'h4e : _GEN_96; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_98 = 10'h4f == guard_index ? 64'h4f : _GEN_97; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_99 = 10'h50 == guard_index ? 64'h50 : _GEN_98; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_100 = 10'h51 == guard_index ? 64'h51 : _GEN_99; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_101 = 10'h52 == guard_index ? 64'h52 : _GEN_100; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_102 = 10'h53 == guard_index ? 64'h53 : _GEN_101; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_103 = 10'h54 == guard_index ? 64'h54 : _GEN_102; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_104 = 10'h55 == guard_index ? 64'h55 : _GEN_103; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_105 = 10'h56 == guard_index ? 64'h56 : _GEN_104; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_106 = 10'h57 == guard_index ? 64'h57 : _GEN_105; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_107 = 10'h58 == guard_index ? 64'h58 : _GEN_106; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_108 = 10'h59 == guard_index ? 64'h59 : _GEN_107; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_109 = 10'h5a == guard_index ? 64'h5a : _GEN_108; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_110 = 10'h5b == guard_index ? 64'h5b : _GEN_109; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_111 = 10'h5c == guard_index ? 64'h5c : _GEN_110; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_112 = 10'h5d == guard_index ? 64'h5d : _GEN_111; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_113 = 10'h5e == guard_index ? 64'h5e : _GEN_112; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_114 = 10'h5f == guard_index ? 64'h5f : _GEN_113; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_115 = 10'h60 == guard_index ? 64'h60 : _GEN_114; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_116 = 10'h61 == guard_index ? 64'h61 : _GEN_115; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_117 = 10'h62 == guard_index ? 64'h62 : _GEN_116; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_118 = 10'h63 == guard_index ? 64'h63 : _GEN_117; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_119 = 10'h64 == guard_index ? 64'h64 : _GEN_118; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_120 = 10'h65 == guard_index ? 64'h65 : _GEN_119; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_121 = 10'h66 == guard_index ? 64'h66 : _GEN_120; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_122 = 10'h67 == guard_index ? 64'h67 : _GEN_121; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_123 = 10'h68 == guard_index ? 64'h68 : _GEN_122; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_124 = 10'h69 == guard_index ? 64'h69 : _GEN_123; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_125 = 10'h6a == guard_index ? 64'h6a : _GEN_124; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_126 = 10'h6b == guard_index ? 64'h6b : _GEN_125; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_127 = 10'h6c == guard_index ? 64'h6c : _GEN_126; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_128 = 10'h6d == guard_index ? 64'h6d : _GEN_127; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_129 = 10'h6e == guard_index ? 64'h6e : _GEN_128; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_130 = 10'h6f == guard_index ? 64'h6f : _GEN_129; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_131 = 10'h70 == guard_index ? 64'h70 : _GEN_130; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_132 = 10'h71 == guard_index ? 64'h71 : _GEN_131; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_133 = 10'h72 == guard_index ? 64'h72 : _GEN_132; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_134 = 10'h73 == guard_index ? 64'h73 : _GEN_133; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_135 = 10'h74 == guard_index ? 64'h74 : _GEN_134; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_136 = 10'h75 == guard_index ? 64'h75 : _GEN_135; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_137 = 10'h76 == guard_index ? 64'h76 : _GEN_136; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_138 = 10'h77 == guard_index ? 64'h77 : _GEN_137; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_139 = 10'h78 == guard_index ? 64'h78 : _GEN_138; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_140 = 10'h79 == guard_index ? 64'h79 : _GEN_139; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_141 = 10'h7a == guard_index ? 64'h7a : _GEN_140; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_142 = 10'h7b == guard_index ? 64'h7b : _GEN_141; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_143 = 10'h7c == guard_index ? 64'h7c : _GEN_142; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_144 = 10'h7d == guard_index ? 64'h7d : _GEN_143; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_145 = 10'h7e == guard_index ? 64'h7e : _GEN_144; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_146 = 10'h7f == guard_index ? 64'h7f : _GEN_145; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_147 = 10'h80 == guard_index ? 64'h80 : _GEN_146; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_148 = 10'h81 == guard_index ? 64'h81 : _GEN_147; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_149 = 10'h82 == guard_index ? 64'h82 : _GEN_148; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_150 = 10'h83 == guard_index ? 64'h83 : _GEN_149; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_151 = 10'h84 == guard_index ? 64'h84 : _GEN_150; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_152 = 10'h85 == guard_index ? 64'h85 : _GEN_151; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_153 = 10'h86 == guard_index ? 64'h86 : _GEN_152; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_154 = 10'h87 == guard_index ? 64'h87 : _GEN_153; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_155 = 10'h88 == guard_index ? 64'h88 : _GEN_154; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_156 = 10'h89 == guard_index ? 64'h89 : _GEN_155; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_157 = 10'h8a == guard_index ? 64'h8a : _GEN_156; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_158 = 10'h8b == guard_index ? 64'h8b : _GEN_157; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_159 = 10'h8c == guard_index ? 64'h8c : _GEN_158; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_160 = 10'h8d == guard_index ? 64'h8d : _GEN_159; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_161 = 10'h8e == guard_index ? 64'h8e : _GEN_160; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_162 = 10'h8f == guard_index ? 64'h8f : _GEN_161; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_163 = 10'h90 == guard_index ? 64'h90 : _GEN_162; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_164 = 10'h91 == guard_index ? 64'h91 : _GEN_163; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_165 = 10'h92 == guard_index ? 64'h92 : _GEN_164; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_166 = 10'h93 == guard_index ? 64'h93 : _GEN_165; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_167 = 10'h94 == guard_index ? 64'h94 : _GEN_166; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_168 = 10'h95 == guard_index ? 64'h95 : _GEN_167; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_169 = 10'h96 == guard_index ? 64'h96 : _GEN_168; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_170 = 10'h97 == guard_index ? 64'h97 : _GEN_169; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_171 = 10'h98 == guard_index ? 64'h98 : _GEN_170; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_172 = 10'h99 == guard_index ? 64'h99 : _GEN_171; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_173 = 10'h9a == guard_index ? 64'h9a : _GEN_172; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_174 = 10'h9b == guard_index ? 64'h9b : _GEN_173; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_175 = 10'h9c == guard_index ? 64'h9c : _GEN_174; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_176 = 10'h9d == guard_index ? 64'h9d : _GEN_175; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_177 = 10'h9e == guard_index ? 64'h9e : _GEN_176; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_178 = 10'h9f == guard_index ? 64'h9f : _GEN_177; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_179 = 10'ha0 == guard_index ? 64'ha0 : _GEN_178; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_180 = 10'ha1 == guard_index ? 64'ha1 : _GEN_179; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_181 = 10'ha2 == guard_index ? 64'ha2 : _GEN_180; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_182 = 10'ha3 == guard_index ? 64'ha3 : _GEN_181; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_183 = 10'ha4 == guard_index ? 64'ha4 : _GEN_182; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_184 = 10'ha5 == guard_index ? 64'ha5 : _GEN_183; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_185 = 10'ha6 == guard_index ? 64'ha6 : _GEN_184; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_186 = 10'ha7 == guard_index ? 64'ha7 : _GEN_185; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_187 = 10'ha8 == guard_index ? 64'ha8 : _GEN_186; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_188 = 10'ha9 == guard_index ? 64'ha9 : _GEN_187; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_189 = 10'haa == guard_index ? 64'haa : _GEN_188; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_190 = 10'hab == guard_index ? 64'hab : _GEN_189; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_191 = 10'hac == guard_index ? 64'hac : _GEN_190; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_192 = 10'had == guard_index ? 64'had : _GEN_191; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_193 = 10'hae == guard_index ? 64'hae : _GEN_192; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_194 = 10'haf == guard_index ? 64'haf : _GEN_193; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_195 = 10'hb0 == guard_index ? 64'hb0 : _GEN_194; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_196 = 10'hb1 == guard_index ? 64'hb1 : _GEN_195; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_197 = 10'hb2 == guard_index ? 64'hb2 : _GEN_196; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_198 = 10'hb3 == guard_index ? 64'hb3 : _GEN_197; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_199 = 10'hb4 == guard_index ? 64'hb4 : _GEN_198; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_200 = 10'hb5 == guard_index ? 64'hb5 : _GEN_199; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_201 = 10'hb6 == guard_index ? 64'hb6 : _GEN_200; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_202 = 10'hb7 == guard_index ? 64'hb7 : _GEN_201; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_203 = 10'hb8 == guard_index ? 64'hb8 : _GEN_202; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_204 = 10'hb9 == guard_index ? 64'hb9 : _GEN_203; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_205 = 10'hba == guard_index ? 64'hba : _GEN_204; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_206 = 10'hbb == guard_index ? 64'hbb : _GEN_205; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_207 = 10'hbc == guard_index ? 64'hbc : _GEN_206; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_208 = 10'hbd == guard_index ? 64'hbd : _GEN_207; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_209 = 10'hbe == guard_index ? 64'hbe : _GEN_208; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_210 = 10'hbf == guard_index ? 64'hbf : _GEN_209; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_211 = 10'hc0 == guard_index ? 64'hc0 : _GEN_210; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_212 = 10'hc1 == guard_index ? 64'hc1 : _GEN_211; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_213 = 10'hc2 == guard_index ? 64'hc2 : _GEN_212; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_214 = 10'hc3 == guard_index ? 64'hc3 : _GEN_213; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_215 = 10'hc4 == guard_index ? 64'hc4 : _GEN_214; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_216 = 10'hc5 == guard_index ? 64'hc5 : _GEN_215; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_217 = 10'hc6 == guard_index ? 64'hc6 : _GEN_216; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_218 = 10'hc7 == guard_index ? 64'hc7 : _GEN_217; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_219 = 10'hc8 == guard_index ? 64'hc8 : _GEN_218; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_220 = 10'hc9 == guard_index ? 64'hc9 : _GEN_219; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_221 = 10'hca == guard_index ? 64'hca : _GEN_220; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_222 = 10'hcb == guard_index ? 64'hcb : _GEN_221; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_223 = 10'hcc == guard_index ? 64'hcc : _GEN_222; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_224 = 10'hcd == guard_index ? 64'hcd : _GEN_223; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_225 = 10'hce == guard_index ? 64'hce : _GEN_224; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_226 = 10'hcf == guard_index ? 64'hcf : _GEN_225; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_227 = 10'hd0 == guard_index ? 64'hd0 : _GEN_226; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_228 = 10'hd1 == guard_index ? 64'hd1 : _GEN_227; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_229 = 10'hd2 == guard_index ? 64'hd2 : _GEN_228; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_230 = 10'hd3 == guard_index ? 64'hd3 : _GEN_229; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_231 = 10'hd4 == guard_index ? 64'hd4 : _GEN_230; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_232 = 10'hd5 == guard_index ? 64'hd5 : _GEN_231; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_233 = 10'hd6 == guard_index ? 64'hd6 : _GEN_232; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_234 = 10'hd7 == guard_index ? 64'hd7 : _GEN_233; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_235 = 10'hd8 == guard_index ? 64'hd8 : _GEN_234; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_236 = 10'hd9 == guard_index ? 64'hd9 : _GEN_235; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_237 = 10'hda == guard_index ? 64'hda : _GEN_236; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_238 = 10'hdb == guard_index ? 64'hdb : _GEN_237; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_239 = 10'hdc == guard_index ? 64'hdc : _GEN_238; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_240 = 10'hdd == guard_index ? 64'hdd : _GEN_239; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_241 = 10'hde == guard_index ? 64'hde : _GEN_240; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_242 = 10'hdf == guard_index ? 64'hdf : _GEN_241; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_243 = 10'he0 == guard_index ? 64'he0 : _GEN_242; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_244 = 10'he1 == guard_index ? 64'he1 : _GEN_243; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_245 = 10'he2 == guard_index ? 64'he2 : _GEN_244; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_246 = 10'he3 == guard_index ? 64'he3 : _GEN_245; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_247 = 10'he4 == guard_index ? 64'he4 : _GEN_246; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_248 = 10'he5 == guard_index ? 64'he5 : _GEN_247; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_249 = 10'he6 == guard_index ? 64'he6 : _GEN_248; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_250 = 10'he7 == guard_index ? 64'he7 : _GEN_249; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_251 = 10'he8 == guard_index ? 64'he8 : _GEN_250; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_252 = 10'he9 == guard_index ? 64'he9 : _GEN_251; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_253 = 10'hea == guard_index ? 64'hea : _GEN_252; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_254 = 10'heb == guard_index ? 64'heb : _GEN_253; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_255 = 10'hec == guard_index ? 64'hec : _GEN_254; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_256 = 10'hed == guard_index ? 64'hed : _GEN_255; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_257 = 10'hee == guard_index ? 64'hee : _GEN_256; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_258 = 10'hef == guard_index ? 64'hef : _GEN_257; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_259 = 10'hf0 == guard_index ? 64'hf0 : _GEN_258; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_260 = 10'hf1 == guard_index ? 64'hf1 : _GEN_259; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_261 = 10'hf2 == guard_index ? 64'hf2 : _GEN_260; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_262 = 10'hf3 == guard_index ? 64'hf3 : _GEN_261; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_263 = 10'hf4 == guard_index ? 64'hf4 : _GEN_262; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_264 = 10'hf5 == guard_index ? 64'hf5 : _GEN_263; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_265 = 10'hf6 == guard_index ? 64'hf6 : _GEN_264; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_266 = 10'hf7 == guard_index ? 64'hf7 : _GEN_265; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_267 = 10'hf8 == guard_index ? 64'hf8 : _GEN_266; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_268 = 10'hf9 == guard_index ? 64'hf9 : _GEN_267; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_269 = 10'hfa == guard_index ? 64'hfa : _GEN_268; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_270 = 10'hfb == guard_index ? 64'hfb : _GEN_269; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_271 = 10'hfc == guard_index ? 64'hfc : _GEN_270; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_272 = 10'hfd == guard_index ? 64'hfd : _GEN_271; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_273 = 10'hfe == guard_index ? 64'hfe : _GEN_272; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_274 = 10'hff == guard_index ? 64'hff : _GEN_273; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_275 = 10'h100 == guard_index ? 64'h100 : _GEN_274; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_276 = 10'h101 == guard_index ? 64'h101 : _GEN_275; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_277 = 10'h102 == guard_index ? 64'h102 : _GEN_276; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_278 = 10'h103 == guard_index ? 64'h103 : _GEN_277; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_279 = 10'h104 == guard_index ? 64'h104 : _GEN_278; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_280 = 10'h105 == guard_index ? 64'h105 : _GEN_279; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_281 = 10'h106 == guard_index ? 64'h106 : _GEN_280; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_282 = 10'h107 == guard_index ? 64'h107 : _GEN_281; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_283 = 10'h108 == guard_index ? 64'h108 : _GEN_282; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_284 = 10'h109 == guard_index ? 64'h109 : _GEN_283; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_285 = 10'h10a == guard_index ? 64'h10a : _GEN_284; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_286 = 10'h10b == guard_index ? 64'h10b : _GEN_285; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_287 = 10'h10c == guard_index ? 64'h10c : _GEN_286; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_288 = 10'h10d == guard_index ? 64'h10d : _GEN_287; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_289 = 10'h10e == guard_index ? 64'h10e : _GEN_288; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_290 = 10'h10f == guard_index ? 64'h10f : _GEN_289; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_291 = 10'h110 == guard_index ? 64'h110 : _GEN_290; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_292 = 10'h111 == guard_index ? 64'h111 : _GEN_291; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_293 = 10'h112 == guard_index ? 64'h112 : _GEN_292; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_294 = 10'h113 == guard_index ? 64'h113 : _GEN_293; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_295 = 10'h114 == guard_index ? 64'h114 : _GEN_294; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_296 = 10'h115 == guard_index ? 64'h115 : _GEN_295; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_297 = 10'h116 == guard_index ? 64'h116 : _GEN_296; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_298 = 10'h117 == guard_index ? 64'h117 : _GEN_297; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_299 = 10'h118 == guard_index ? 64'h118 : _GEN_298; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_300 = 10'h119 == guard_index ? 64'h119 : _GEN_299; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_301 = 10'h11a == guard_index ? 64'h11a : _GEN_300; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_302 = 10'h11b == guard_index ? 64'h11b : _GEN_301; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_303 = 10'h11c == guard_index ? 64'h11c : _GEN_302; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_304 = 10'h11d == guard_index ? 64'h11d : _GEN_303; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_305 = 10'h11e == guard_index ? 64'h11e : _GEN_304; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_306 = 10'h11f == guard_index ? 64'h11f : _GEN_305; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_307 = 10'h120 == guard_index ? 64'h120 : _GEN_306; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_308 = 10'h121 == guard_index ? 64'h121 : _GEN_307; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_309 = 10'h122 == guard_index ? 64'h122 : _GEN_308; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_310 = 10'h123 == guard_index ? 64'h123 : _GEN_309; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_311 = 10'h124 == guard_index ? 64'h124 : _GEN_310; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_312 = 10'h125 == guard_index ? 64'h125 : _GEN_311; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_313 = 10'h126 == guard_index ? 64'h126 : _GEN_312; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_314 = 10'h127 == guard_index ? 64'h127 : _GEN_313; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_315 = 10'h128 == guard_index ? 64'h128 : _GEN_314; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_316 = 10'h129 == guard_index ? 64'h129 : _GEN_315; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_317 = 10'h12a == guard_index ? 64'h12a : _GEN_316; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_318 = 10'h12b == guard_index ? 64'h12b : _GEN_317; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_319 = 10'h12c == guard_index ? 64'h12c : _GEN_318; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_320 = 10'h12d == guard_index ? 64'h12d : _GEN_319; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_321 = 10'h12e == guard_index ? 64'h12e : _GEN_320; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_322 = 10'h12f == guard_index ? 64'h12f : _GEN_321; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_323 = 10'h130 == guard_index ? 64'h130 : _GEN_322; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_324 = 10'h131 == guard_index ? 64'h131 : _GEN_323; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_325 = 10'h132 == guard_index ? 64'h132 : _GEN_324; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_326 = 10'h133 == guard_index ? 64'h133 : _GEN_325; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_327 = 10'h134 == guard_index ? 64'h134 : _GEN_326; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_328 = 10'h135 == guard_index ? 64'h135 : _GEN_327; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_329 = 10'h136 == guard_index ? 64'h136 : _GEN_328; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_330 = 10'h137 == guard_index ? 64'h137 : _GEN_329; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_331 = 10'h138 == guard_index ? 64'h138 : _GEN_330; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_332 = 10'h139 == guard_index ? 64'h139 : _GEN_331; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_333 = 10'h13a == guard_index ? 64'h13a : _GEN_332; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_334 = 10'h13b == guard_index ? 64'h13b : _GEN_333; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_335 = 10'h13c == guard_index ? 64'h13c : _GEN_334; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_336 = 10'h13d == guard_index ? 64'h13d : _GEN_335; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_337 = 10'h13e == guard_index ? 64'h13e : _GEN_336; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_338 = 10'h13f == guard_index ? 64'h13f : _GEN_337; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_339 = 10'h140 == guard_index ? 64'h140 : _GEN_338; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_340 = 10'h141 == guard_index ? 64'h141 : _GEN_339; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_341 = 10'h142 == guard_index ? 64'h142 : _GEN_340; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_342 = 10'h143 == guard_index ? 64'h143 : _GEN_341; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_343 = 10'h144 == guard_index ? 64'h144 : _GEN_342; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_344 = 10'h145 == guard_index ? 64'h145 : _GEN_343; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_345 = 10'h146 == guard_index ? 64'h146 : _GEN_344; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_346 = 10'h147 == guard_index ? 64'h147 : _GEN_345; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_347 = 10'h148 == guard_index ? 64'h148 : _GEN_346; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_348 = 10'h149 == guard_index ? 64'h149 : _GEN_347; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_349 = 10'h14a == guard_index ? 64'h14a : _GEN_348; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_350 = 10'h14b == guard_index ? 64'h14b : _GEN_349; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_351 = 10'h14c == guard_index ? 64'h14c : _GEN_350; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_352 = 10'h14d == guard_index ? 64'h14d : _GEN_351; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_353 = 10'h14e == guard_index ? 64'h14e : _GEN_352; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_354 = 10'h14f == guard_index ? 64'h14f : _GEN_353; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_355 = 10'h150 == guard_index ? 64'h150 : _GEN_354; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_356 = 10'h151 == guard_index ? 64'h151 : _GEN_355; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_357 = 10'h152 == guard_index ? 64'h152 : _GEN_356; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_358 = 10'h153 == guard_index ? 64'h153 : _GEN_357; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_359 = 10'h154 == guard_index ? 64'h154 : _GEN_358; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_360 = 10'h155 == guard_index ? 64'h155 : _GEN_359; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_361 = 10'h156 == guard_index ? 64'h156 : _GEN_360; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_362 = 10'h157 == guard_index ? 64'h157 : _GEN_361; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_363 = 10'h158 == guard_index ? 64'h158 : _GEN_362; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_364 = 10'h159 == guard_index ? 64'h159 : _GEN_363; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_365 = 10'h15a == guard_index ? 64'h15a : _GEN_364; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_366 = 10'h15b == guard_index ? 64'h15b : _GEN_365; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_367 = 10'h15c == guard_index ? 64'h15c : _GEN_366; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_368 = 10'h15d == guard_index ? 64'h15d : _GEN_367; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_369 = 10'h15e == guard_index ? 64'h15e : _GEN_368; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_370 = 10'h15f == guard_index ? 64'h15f : _GEN_369; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_371 = 10'h160 == guard_index ? 64'h160 : _GEN_370; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_372 = 10'h161 == guard_index ? 64'h161 : _GEN_371; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_373 = 10'h162 == guard_index ? 64'h162 : _GEN_372; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_374 = 10'h163 == guard_index ? 64'h163 : _GEN_373; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_375 = 10'h164 == guard_index ? 64'h164 : _GEN_374; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_376 = 10'h165 == guard_index ? 64'h165 : _GEN_375; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_377 = 10'h166 == guard_index ? 64'h166 : _GEN_376; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_378 = 10'h167 == guard_index ? 64'h167 : _GEN_377; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_379 = 10'h168 == guard_index ? 64'h168 : _GEN_378; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_380 = 10'h169 == guard_index ? 64'h169 : _GEN_379; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_381 = 10'h16a == guard_index ? 64'h16a : _GEN_380; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_382 = 10'h16b == guard_index ? 64'h16b : _GEN_381; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_383 = 10'h16c == guard_index ? 64'h16c : _GEN_382; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_384 = 10'h16d == guard_index ? 64'h16d : _GEN_383; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_385 = 10'h16e == guard_index ? 64'h16e : _GEN_384; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_386 = 10'h16f == guard_index ? 64'h16f : _GEN_385; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_387 = 10'h170 == guard_index ? 64'h170 : _GEN_386; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_388 = 10'h171 == guard_index ? 64'h171 : _GEN_387; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_389 = 10'h172 == guard_index ? 64'h172 : _GEN_388; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_390 = 10'h173 == guard_index ? 64'h173 : _GEN_389; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_391 = 10'h174 == guard_index ? 64'h174 : _GEN_390; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_392 = 10'h175 == guard_index ? 64'h175 : _GEN_391; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_393 = 10'h176 == guard_index ? 64'h176 : _GEN_392; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_394 = 10'h177 == guard_index ? 64'h177 : _GEN_393; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_395 = 10'h178 == guard_index ? 64'h178 : _GEN_394; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_396 = 10'h179 == guard_index ? 64'h179 : _GEN_395; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_397 = 10'h17a == guard_index ? 64'h17a : _GEN_396; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_398 = 10'h17b == guard_index ? 64'h17b : _GEN_397; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_399 = 10'h17c == guard_index ? 64'h17c : _GEN_398; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_400 = 10'h17d == guard_index ? 64'h17d : _GEN_399; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_401 = 10'h17e == guard_index ? 64'h17e : _GEN_400; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_402 = 10'h17f == guard_index ? 64'h17f : _GEN_401; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_403 = 10'h180 == guard_index ? 64'h180 : _GEN_402; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_404 = 10'h181 == guard_index ? 64'h181 : _GEN_403; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_405 = 10'h182 == guard_index ? 64'h182 : _GEN_404; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_406 = 10'h183 == guard_index ? 64'h183 : _GEN_405; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_407 = 10'h184 == guard_index ? 64'h184 : _GEN_406; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_408 = 10'h185 == guard_index ? 64'h185 : _GEN_407; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_409 = 10'h186 == guard_index ? 64'h186 : _GEN_408; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_410 = 10'h187 == guard_index ? 64'h187 : _GEN_409; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_411 = 10'h188 == guard_index ? 64'h188 : _GEN_410; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_412 = 10'h189 == guard_index ? 64'h189 : _GEN_411; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_413 = 10'h18a == guard_index ? 64'h18a : _GEN_412; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_414 = 10'h18b == guard_index ? 64'h18b : _GEN_413; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_415 = 10'h18c == guard_index ? 64'h18c : _GEN_414; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_416 = 10'h18d == guard_index ? 64'h18d : _GEN_415; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_417 = 10'h18e == guard_index ? 64'h18e : _GEN_416; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_418 = 10'h18f == guard_index ? 64'h18f : _GEN_417; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_419 = 10'h190 == guard_index ? 64'h190 : _GEN_418; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_420 = 10'h191 == guard_index ? 64'h191 : _GEN_419; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_421 = 10'h192 == guard_index ? 64'h192 : _GEN_420; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_422 = 10'h193 == guard_index ? 64'h193 : _GEN_421; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_423 = 10'h194 == guard_index ? 64'h194 : _GEN_422; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_424 = 10'h195 == guard_index ? 64'h195 : _GEN_423; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_425 = 10'h196 == guard_index ? 64'h196 : _GEN_424; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_426 = 10'h197 == guard_index ? 64'h197 : _GEN_425; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_427 = 10'h198 == guard_index ? 64'h198 : _GEN_426; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_428 = 10'h199 == guard_index ? 64'h199 : _GEN_427; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_429 = 10'h19a == guard_index ? 64'h19a : _GEN_428; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_430 = 10'h19b == guard_index ? 64'h19b : _GEN_429; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_431 = 10'h19c == guard_index ? 64'h19c : _GEN_430; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_432 = 10'h19d == guard_index ? 64'h19d : _GEN_431; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_433 = 10'h19e == guard_index ? 64'h19e : _GEN_432; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_434 = 10'h19f == guard_index ? 64'h19f : _GEN_433; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_435 = 10'h1a0 == guard_index ? 64'h1a0 : _GEN_434; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_436 = 10'h1a1 == guard_index ? 64'h1a1 : _GEN_435; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_437 = 10'h1a2 == guard_index ? 64'h1a2 : _GEN_436; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_438 = 10'h1a3 == guard_index ? 64'h1a3 : _GEN_437; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_439 = 10'h1a4 == guard_index ? 64'h1a4 : _GEN_438; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_440 = 10'h1a5 == guard_index ? 64'h1a5 : _GEN_439; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_441 = 10'h1a6 == guard_index ? 64'h1a6 : _GEN_440; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_442 = 10'h1a7 == guard_index ? 64'h1a7 : _GEN_441; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_443 = 10'h1a8 == guard_index ? 64'h1a8 : _GEN_442; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_444 = 10'h1a9 == guard_index ? 64'h1a9 : _GEN_443; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_445 = 10'h1aa == guard_index ? 64'h1aa : _GEN_444; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_446 = 10'h1ab == guard_index ? 64'h1ab : _GEN_445; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_447 = 10'h1ac == guard_index ? 64'h1ac : _GEN_446; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_448 = 10'h1ad == guard_index ? 64'h1ad : _GEN_447; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_449 = 10'h1ae == guard_index ? 64'h1ae : _GEN_448; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_450 = 10'h1af == guard_index ? 64'h1af : _GEN_449; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_451 = 10'h1b0 == guard_index ? 64'h1b0 : _GEN_450; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_452 = 10'h1b1 == guard_index ? 64'h1b1 : _GEN_451; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_453 = 10'h1b2 == guard_index ? 64'h1b2 : _GEN_452; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_454 = 10'h1b3 == guard_index ? 64'h1b3 : _GEN_453; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_455 = 10'h1b4 == guard_index ? 64'h1b4 : _GEN_454; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_456 = 10'h1b5 == guard_index ? 64'h1b5 : _GEN_455; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_457 = 10'h1b6 == guard_index ? 64'h1b6 : _GEN_456; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_458 = 10'h1b7 == guard_index ? 64'h1b7 : _GEN_457; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_459 = 10'h1b8 == guard_index ? 64'h1b8 : _GEN_458; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_460 = 10'h1b9 == guard_index ? 64'h1b9 : _GEN_459; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_461 = 10'h1ba == guard_index ? 64'h1ba : _GEN_460; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_462 = 10'h1bb == guard_index ? 64'h1bb : _GEN_461; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_463 = 10'h1bc == guard_index ? 64'h1bc : _GEN_462; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_464 = 10'h1bd == guard_index ? 64'h1bd : _GEN_463; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_465 = 10'h1be == guard_index ? 64'h1be : _GEN_464; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_466 = 10'h1bf == guard_index ? 64'h1bf : _GEN_465; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_467 = 10'h1c0 == guard_index ? 64'h1c0 : _GEN_466; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_468 = 10'h1c1 == guard_index ? 64'h1c1 : _GEN_467; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_469 = 10'h1c2 == guard_index ? 64'h1c2 : _GEN_468; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_470 = 10'h1c3 == guard_index ? 64'h1c3 : _GEN_469; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_471 = 10'h1c4 == guard_index ? 64'h1c4 : _GEN_470; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_472 = 10'h1c5 == guard_index ? 64'h1c5 : _GEN_471; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_473 = 10'h1c6 == guard_index ? 64'h1c6 : _GEN_472; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_474 = 10'h1c7 == guard_index ? 64'h1c7 : _GEN_473; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_475 = 10'h1c8 == guard_index ? 64'h1c8 : _GEN_474; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_476 = 10'h1c9 == guard_index ? 64'h1c9 : _GEN_475; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_477 = 10'h1ca == guard_index ? 64'h1ca : _GEN_476; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_478 = 10'h1cb == guard_index ? 64'h1cb : _GEN_477; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_479 = 10'h1cc == guard_index ? 64'h1cc : _GEN_478; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_480 = 10'h1cd == guard_index ? 64'h1cd : _GEN_479; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_481 = 10'h1ce == guard_index ? 64'h1ce : _GEN_480; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_482 = 10'h1cf == guard_index ? 64'h1cf : _GEN_481; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_483 = 10'h1d0 == guard_index ? 64'h1d0 : _GEN_482; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_484 = 10'h1d1 == guard_index ? 64'h1d1 : _GEN_483; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_485 = 10'h1d2 == guard_index ? 64'h1d2 : _GEN_484; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_486 = 10'h1d3 == guard_index ? 64'h1d3 : _GEN_485; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_487 = 10'h1d4 == guard_index ? 64'h1d4 : _GEN_486; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_488 = 10'h1d5 == guard_index ? 64'h1d5 : _GEN_487; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_489 = 10'h1d6 == guard_index ? 64'h1d6 : _GEN_488; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_490 = 10'h1d7 == guard_index ? 64'h1d7 : _GEN_489; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_491 = 10'h1d8 == guard_index ? 64'h1d8 : _GEN_490; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_492 = 10'h1d9 == guard_index ? 64'h1d9 : _GEN_491; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_493 = 10'h1da == guard_index ? 64'h1da : _GEN_492; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_494 = 10'h1db == guard_index ? 64'h1db : _GEN_493; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_495 = 10'h1dc == guard_index ? 64'h1dc : _GEN_494; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_496 = 10'h1dd == guard_index ? 64'h1dd : _GEN_495; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_497 = 10'h1de == guard_index ? 64'h1de : _GEN_496; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_498 = 10'h1df == guard_index ? 64'h1df : _GEN_497; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_499 = 10'h1e0 == guard_index ? 64'h1e0 : _GEN_498; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_500 = 10'h1e1 == guard_index ? 64'h1e1 : _GEN_499; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_501 = 10'h1e2 == guard_index ? 64'h1e2 : _GEN_500; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_502 = 10'h1e3 == guard_index ? 64'h1e3 : _GEN_501; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_503 = 10'h1e4 == guard_index ? 64'h1e4 : _GEN_502; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_504 = 10'h1e5 == guard_index ? 64'h1e5 : _GEN_503; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_505 = 10'h1e6 == guard_index ? 64'h1e6 : _GEN_504; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_506 = 10'h1e7 == guard_index ? 64'h1e7 : _GEN_505; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_507 = 10'h1e8 == guard_index ? 64'h1e8 : _GEN_506; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_508 = 10'h1e9 == guard_index ? 64'h1e9 : _GEN_507; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_509 = 10'h1ea == guard_index ? 64'h1ea : _GEN_508; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_510 = 10'h1eb == guard_index ? 64'h1eb : _GEN_509; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_511 = 10'h1ec == guard_index ? 64'h1ec : _GEN_510; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_512 = 10'h1ed == guard_index ? 64'h1ed : _GEN_511; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_513 = 10'h1ee == guard_index ? 64'h1ee : _GEN_512; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_514 = 10'h1ef == guard_index ? 64'h1ef : _GEN_513; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_515 = 10'h1f0 == guard_index ? 64'h1f0 : _GEN_514; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_516 = 10'h1f1 == guard_index ? 64'h1f1 : _GEN_515; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_517 = 10'h1f2 == guard_index ? 64'h1f2 : _GEN_516; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_518 = 10'h1f3 == guard_index ? 64'h1f3 : _GEN_517; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_519 = 10'h1f4 == guard_index ? 64'h1f4 : _GEN_518; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_520 = 10'h1f5 == guard_index ? 64'h1f5 : _GEN_519; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_521 = 10'h1f6 == guard_index ? 64'h1f6 : _GEN_520; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_522 = 10'h1f7 == guard_index ? 64'h1f7 : _GEN_521; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_523 = 10'h1f8 == guard_index ? 64'h1f8 : _GEN_522; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_524 = 10'h1f9 == guard_index ? 64'h1f9 : _GEN_523; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_525 = 10'h1fa == guard_index ? 64'h1fa : _GEN_524; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_526 = 10'h1fb == guard_index ? 64'h1fb : _GEN_525; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_527 = 10'h1fc == guard_index ? 64'h1fc : _GEN_526; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_528 = 10'h1fd == guard_index ? 64'h1fd : _GEN_527; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_529 = 10'h1fe == guard_index ? 64'h1fe : _GEN_528; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_530 = 10'h1ff == guard_index ? 64'h1ff : _GEN_529; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_531 = 10'h200 == guard_index ? 64'h200 : _GEN_530; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_532 = 10'h201 == guard_index ? 64'h201 : _GEN_531; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_533 = 10'h202 == guard_index ? 64'h202 : _GEN_532; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_534 = 10'h203 == guard_index ? 64'h203 : _GEN_533; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_535 = 10'h204 == guard_index ? 64'h204 : _GEN_534; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_536 = 10'h205 == guard_index ? 64'h205 : _GEN_535; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_537 = 10'h206 == guard_index ? 64'h206 : _GEN_536; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_538 = 10'h207 == guard_index ? 64'h207 : _GEN_537; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_539 = 10'h208 == guard_index ? 64'h208 : _GEN_538; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_540 = 10'h209 == guard_index ? 64'h209 : _GEN_539; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_541 = 10'h20a == guard_index ? 64'h20a : _GEN_540; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_542 = 10'h20b == guard_index ? 64'h20b : _GEN_541; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_543 = 10'h20c == guard_index ? 64'h20c : _GEN_542; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_544 = 10'h20d == guard_index ? 64'h20d : _GEN_543; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_545 = 10'h20e == guard_index ? 64'h20e : _GEN_544; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_546 = 10'h20f == guard_index ? 64'h20f : _GEN_545; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_547 = 10'h210 == guard_index ? 64'h210 : _GEN_546; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_548 = 10'h211 == guard_index ? 64'h211 : _GEN_547; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_549 = 10'h212 == guard_index ? 64'h212 : _GEN_548; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_550 = 10'h213 == guard_index ? 64'h213 : _GEN_549; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_551 = 10'h214 == guard_index ? 64'h214 : _GEN_550; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_552 = 10'h215 == guard_index ? 64'h215 : _GEN_551; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_553 = 10'h216 == guard_index ? 64'h216 : _GEN_552; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_554 = 10'h217 == guard_index ? 64'h217 : _GEN_553; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_555 = 10'h218 == guard_index ? 64'h218 : _GEN_554; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_556 = 10'h219 == guard_index ? 64'h219 : _GEN_555; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_557 = 10'h21a == guard_index ? 64'h21a : _GEN_556; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_558 = 10'h21b == guard_index ? 64'h21b : _GEN_557; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_559 = 10'h21c == guard_index ? 64'h21c : _GEN_558; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_560 = 10'h21d == guard_index ? 64'h21d : _GEN_559; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_561 = 10'h21e == guard_index ? 64'h21e : _GEN_560; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_562 = 10'h21f == guard_index ? 64'h21f : _GEN_561; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_563 = 10'h220 == guard_index ? 64'h220 : _GEN_562; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_564 = 10'h221 == guard_index ? 64'h221 : _GEN_563; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_565 = 10'h222 == guard_index ? 64'h222 : _GEN_564; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_566 = 10'h223 == guard_index ? 64'h223 : _GEN_565; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_567 = 10'h224 == guard_index ? 64'h224 : _GEN_566; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_568 = 10'h225 == guard_index ? 64'h225 : _GEN_567; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_569 = 10'h226 == guard_index ? 64'h226 : _GEN_568; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_570 = 10'h227 == guard_index ? 64'h227 : _GEN_569; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_571 = 10'h228 == guard_index ? 64'h228 : _GEN_570; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_572 = 10'h229 == guard_index ? 64'h229 : _GEN_571; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_573 = 10'h22a == guard_index ? 64'h22a : _GEN_572; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_574 = 10'h22b == guard_index ? 64'h22b : _GEN_573; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_575 = 10'h22c == guard_index ? 64'h22c : _GEN_574; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_576 = 10'h22d == guard_index ? 64'h22d : _GEN_575; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_577 = 10'h22e == guard_index ? 64'h22e : _GEN_576; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_578 = 10'h22f == guard_index ? 64'h22f : _GEN_577; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_579 = 10'h230 == guard_index ? 64'h230 : _GEN_578; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_580 = 10'h231 == guard_index ? 64'h231 : _GEN_579; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_581 = 10'h232 == guard_index ? 64'h232 : _GEN_580; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_582 = 10'h233 == guard_index ? 64'h233 : _GEN_581; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_583 = 10'h234 == guard_index ? 64'h234 : _GEN_582; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_584 = 10'h235 == guard_index ? 64'h235 : _GEN_583; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_585 = 10'h236 == guard_index ? 64'h236 : _GEN_584; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_586 = 10'h237 == guard_index ? 64'h237 : _GEN_585; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_587 = 10'h238 == guard_index ? 64'h238 : _GEN_586; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_588 = 10'h239 == guard_index ? 64'h239 : _GEN_587; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_589 = 10'h23a == guard_index ? 64'h23a : _GEN_588; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_590 = 10'h23b == guard_index ? 64'h23b : _GEN_589; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_591 = 10'h23c == guard_index ? 64'h23c : _GEN_590; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_592 = 10'h23d == guard_index ? 64'h23d : _GEN_591; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_593 = 10'h23e == guard_index ? 64'h23e : _GEN_592; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_594 = 10'h23f == guard_index ? 64'h23f : _GEN_593; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_595 = 10'h240 == guard_index ? 64'h240 : _GEN_594; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_596 = 10'h241 == guard_index ? 64'h241 : _GEN_595; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_597 = 10'h242 == guard_index ? 64'h242 : _GEN_596; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_598 = 10'h243 == guard_index ? 64'h243 : _GEN_597; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_599 = 10'h244 == guard_index ? 64'h244 : _GEN_598; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_600 = 10'h245 == guard_index ? 64'h245 : _GEN_599; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_601 = 10'h246 == guard_index ? 64'h246 : _GEN_600; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_602 = 10'h247 == guard_index ? 64'h247 : _GEN_601; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_603 = 10'h248 == guard_index ? 64'h248 : _GEN_602; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_604 = 10'h249 == guard_index ? 64'h249 : _GEN_603; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_605 = 10'h24a == guard_index ? 64'h24a : _GEN_604; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_606 = 10'h24b == guard_index ? 64'h24b : _GEN_605; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_607 = 10'h24c == guard_index ? 64'h24c : _GEN_606; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_608 = 10'h24d == guard_index ? 64'h24d : _GEN_607; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_609 = 10'h24e == guard_index ? 64'h24e : _GEN_608; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_610 = 10'h24f == guard_index ? 64'h24f : _GEN_609; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_611 = 10'h250 == guard_index ? 64'h250 : _GEN_610; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_612 = 10'h251 == guard_index ? 64'h251 : _GEN_611; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_613 = 10'h252 == guard_index ? 64'h252 : _GEN_612; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_614 = 10'h253 == guard_index ? 64'h253 : _GEN_613; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_615 = 10'h254 == guard_index ? 64'h254 : _GEN_614; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_616 = 10'h255 == guard_index ? 64'h255 : _GEN_615; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_617 = 10'h256 == guard_index ? 64'h256 : _GEN_616; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_618 = 10'h257 == guard_index ? 64'h257 : _GEN_617; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_619 = 10'h258 == guard_index ? 64'h258 : _GEN_618; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_620 = 10'h259 == guard_index ? 64'h259 : _GEN_619; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_621 = 10'h25a == guard_index ? 64'h25a : _GEN_620; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_622 = 10'h25b == guard_index ? 64'h25b : _GEN_621; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_623 = 10'h25c == guard_index ? 64'h25c : _GEN_622; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_624 = 10'h25d == guard_index ? 64'h25d : _GEN_623; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_625 = 10'h25e == guard_index ? 64'h25e : _GEN_624; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_626 = 10'h25f == guard_index ? 64'h25f : _GEN_625; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_627 = 10'h260 == guard_index ? 64'h260 : _GEN_626; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_628 = 10'h261 == guard_index ? 64'h261 : _GEN_627; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_629 = 10'h262 == guard_index ? 64'h262 : _GEN_628; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_630 = 10'h263 == guard_index ? 64'h263 : _GEN_629; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_631 = 10'h264 == guard_index ? 64'h264 : _GEN_630; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_632 = 10'h265 == guard_index ? 64'h265 : _GEN_631; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_633 = 10'h266 == guard_index ? 64'h266 : _GEN_632; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_634 = 10'h267 == guard_index ? 64'h267 : _GEN_633; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_635 = 10'h268 == guard_index ? 64'h268 : _GEN_634; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_636 = 10'h269 == guard_index ? 64'h269 : _GEN_635; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_637 = 10'h26a == guard_index ? 64'h26a : _GEN_636; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_638 = 10'h26b == guard_index ? 64'h26b : _GEN_637; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_639 = 10'h26c == guard_index ? 64'h26c : _GEN_638; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_640 = 10'h26d == guard_index ? 64'h26d : _GEN_639; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_641 = 10'h26e == guard_index ? 64'h26e : _GEN_640; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_642 = 10'h26f == guard_index ? 64'h26f : _GEN_641; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_643 = 10'h270 == guard_index ? 64'h270 : _GEN_642; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_644 = 10'h271 == guard_index ? 64'h271 : _GEN_643; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_645 = 10'h272 == guard_index ? 64'h272 : _GEN_644; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_646 = 10'h273 == guard_index ? 64'h273 : _GEN_645; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_647 = 10'h274 == guard_index ? 64'h274 : _GEN_646; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_648 = 10'h275 == guard_index ? 64'h275 : _GEN_647; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_649 = 10'h276 == guard_index ? 64'h276 : _GEN_648; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_650 = 10'h277 == guard_index ? 64'h277 : _GEN_649; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_651 = 10'h278 == guard_index ? 64'h278 : _GEN_650; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_652 = 10'h279 == guard_index ? 64'h279 : _GEN_651; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_653 = 10'h27a == guard_index ? 64'h27a : _GEN_652; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_654 = 10'h27b == guard_index ? 64'h27b : _GEN_653; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_655 = 10'h27c == guard_index ? 64'h27c : _GEN_654; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_656 = 10'h27d == guard_index ? 64'h27d : _GEN_655; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_657 = 10'h27e == guard_index ? 64'h27e : _GEN_656; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_658 = 10'h27f == guard_index ? 64'h27f : _GEN_657; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_659 = 10'h280 == guard_index ? 64'h280 : _GEN_658; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_660 = 10'h281 == guard_index ? 64'h281 : _GEN_659; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_661 = 10'h282 == guard_index ? 64'h282 : _GEN_660; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_662 = 10'h283 == guard_index ? 64'h283 : _GEN_661; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_663 = 10'h284 == guard_index ? 64'h284 : _GEN_662; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_664 = 10'h285 == guard_index ? 64'h285 : _GEN_663; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_665 = 10'h286 == guard_index ? 64'h286 : _GEN_664; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_666 = 10'h287 == guard_index ? 64'h287 : _GEN_665; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_667 = 10'h288 == guard_index ? 64'h288 : _GEN_666; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_668 = 10'h289 == guard_index ? 64'h289 : _GEN_667; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_669 = 10'h28a == guard_index ? 64'h28a : _GEN_668; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_670 = 10'h28b == guard_index ? 64'h28b : _GEN_669; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_671 = 10'h28c == guard_index ? 64'h28c : _GEN_670; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_672 = 10'h28d == guard_index ? 64'h28d : _GEN_671; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_673 = 10'h28e == guard_index ? 64'h28e : _GEN_672; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_674 = 10'h28f == guard_index ? 64'h28f : _GEN_673; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_675 = 10'h290 == guard_index ? 64'h290 : _GEN_674; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_676 = 10'h291 == guard_index ? 64'h291 : _GEN_675; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_677 = 10'h292 == guard_index ? 64'h292 : _GEN_676; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_678 = 10'h293 == guard_index ? 64'h293 : _GEN_677; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_679 = 10'h294 == guard_index ? 64'h294 : _GEN_678; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_680 = 10'h295 == guard_index ? 64'h295 : _GEN_679; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_681 = 10'h296 == guard_index ? 64'h296 : _GEN_680; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_682 = 10'h297 == guard_index ? 64'h297 : _GEN_681; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_683 = 10'h298 == guard_index ? 64'h298 : _GEN_682; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_684 = 10'h299 == guard_index ? 64'h299 : _GEN_683; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_685 = 10'h29a == guard_index ? 64'h29a : _GEN_684; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_686 = 10'h29b == guard_index ? 64'h29b : _GEN_685; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_687 = 10'h29c == guard_index ? 64'h29c : _GEN_686; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_688 = 10'h29d == guard_index ? 64'h29d : _GEN_687; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_689 = 10'h29e == guard_index ? 64'h29e : _GEN_688; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_690 = 10'h29f == guard_index ? 64'h29f : _GEN_689; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_691 = 10'h2a0 == guard_index ? 64'h2a0 : _GEN_690; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_692 = 10'h2a1 == guard_index ? 64'h2a1 : _GEN_691; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_693 = 10'h2a2 == guard_index ? 64'h2a2 : _GEN_692; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_694 = 10'h2a3 == guard_index ? 64'h2a3 : _GEN_693; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_695 = 10'h2a4 == guard_index ? 64'h2a4 : _GEN_694; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_696 = 10'h2a5 == guard_index ? 64'h2a5 : _GEN_695; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_697 = 10'h2a6 == guard_index ? 64'h2a6 : _GEN_696; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_698 = 10'h2a7 == guard_index ? 64'h2a7 : _GEN_697; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_699 = 10'h2a8 == guard_index ? 64'h2a8 : _GEN_698; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_700 = 10'h2a9 == guard_index ? 64'h2a9 : _GEN_699; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_701 = 10'h2aa == guard_index ? 64'h2aa : _GEN_700; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_702 = 10'h2ab == guard_index ? 64'h2ab : _GEN_701; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_703 = 10'h2ac == guard_index ? 64'h2ac : _GEN_702; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_704 = 10'h2ad == guard_index ? 64'h2ad : _GEN_703; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_705 = 10'h2ae == guard_index ? 64'h2ae : _GEN_704; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_706 = 10'h2af == guard_index ? 64'h2af : _GEN_705; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_707 = 10'h2b0 == guard_index ? 64'h2b0 : _GEN_706; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_708 = 10'h2b1 == guard_index ? 64'h2b1 : _GEN_707; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_709 = 10'h2b2 == guard_index ? 64'h2b2 : _GEN_708; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_710 = 10'h2b3 == guard_index ? 64'h2b3 : _GEN_709; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_711 = 10'h2b4 == guard_index ? 64'h2b4 : _GEN_710; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_712 = 10'h2b5 == guard_index ? 64'h2b5 : _GEN_711; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_713 = 10'h2b6 == guard_index ? 64'h2b6 : _GEN_712; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_714 = 10'h2b7 == guard_index ? 64'h2b7 : _GEN_713; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_715 = 10'h2b8 == guard_index ? 64'h2b8 : _GEN_714; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_716 = 10'h2b9 == guard_index ? 64'h2b9 : _GEN_715; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_717 = 10'h2ba == guard_index ? 64'h2ba : _GEN_716; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_718 = 10'h2bb == guard_index ? 64'h2bb : _GEN_717; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_719 = 10'h2bc == guard_index ? 64'h2bc : _GEN_718; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_720 = 10'h2bd == guard_index ? 64'h2bd : _GEN_719; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_721 = 10'h2be == guard_index ? 64'h2be : _GEN_720; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_722 = 10'h2bf == guard_index ? 64'h2bf : _GEN_721; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_723 = 10'h2c0 == guard_index ? 64'h2c0 : _GEN_722; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_724 = 10'h2c1 == guard_index ? 64'h2c1 : _GEN_723; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_725 = 10'h2c2 == guard_index ? 64'h2c2 : _GEN_724; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_726 = 10'h2c3 == guard_index ? 64'h2c3 : _GEN_725; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_727 = 10'h2c4 == guard_index ? 64'h2c4 : _GEN_726; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_728 = 10'h2c5 == guard_index ? 64'h2c5 : _GEN_727; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_729 = 10'h2c6 == guard_index ? 64'h2c6 : _GEN_728; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_730 = 10'h2c7 == guard_index ? 64'h2c7 : _GEN_729; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_731 = 10'h2c8 == guard_index ? 64'h2c8 : _GEN_730; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_732 = 10'h2c9 == guard_index ? 64'h2c9 : _GEN_731; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_733 = 10'h2ca == guard_index ? 64'h2ca : _GEN_732; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_734 = 10'h2cb == guard_index ? 64'h2cb : _GEN_733; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_735 = 10'h2cc == guard_index ? 64'h2cc : _GEN_734; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_736 = 10'h2cd == guard_index ? 64'h2cd : _GEN_735; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_737 = 10'h2ce == guard_index ? 64'h2ce : _GEN_736; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_738 = 10'h2cf == guard_index ? 64'h2cf : _GEN_737; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_739 = 10'h2d0 == guard_index ? 64'h2d0 : _GEN_738; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_740 = 10'h2d1 == guard_index ? 64'h2d1 : _GEN_739; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_741 = 10'h2d2 == guard_index ? 64'h2d2 : _GEN_740; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_742 = 10'h2d3 == guard_index ? 64'h2d3 : _GEN_741; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_743 = 10'h2d4 == guard_index ? 64'h2d4 : _GEN_742; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_744 = 10'h2d5 == guard_index ? 64'h2d5 : _GEN_743; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_745 = 10'h2d6 == guard_index ? 64'h2d6 : _GEN_744; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_746 = 10'h2d7 == guard_index ? 64'h2d7 : _GEN_745; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_747 = 10'h2d8 == guard_index ? 64'h2d8 : _GEN_746; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_748 = 10'h2d9 == guard_index ? 64'h2d9 : _GEN_747; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_749 = 10'h2da == guard_index ? 64'h2da : _GEN_748; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_750 = 10'h2db == guard_index ? 64'h2db : _GEN_749; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_751 = 10'h2dc == guard_index ? 64'h2dc : _GEN_750; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_752 = 10'h2dd == guard_index ? 64'h2dd : _GEN_751; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_753 = 10'h2de == guard_index ? 64'h2de : _GEN_752; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_754 = 10'h2df == guard_index ? 64'h2df : _GEN_753; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_755 = 10'h2e0 == guard_index ? 64'h2e0 : _GEN_754; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_756 = 10'h2e1 == guard_index ? 64'h2e1 : _GEN_755; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_757 = 10'h2e2 == guard_index ? 64'h2e2 : _GEN_756; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_758 = 10'h2e3 == guard_index ? 64'h2e3 : _GEN_757; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_759 = 10'h2e4 == guard_index ? 64'h2e4 : _GEN_758; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_760 = 10'h2e5 == guard_index ? 64'h2e5 : _GEN_759; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_761 = 10'h2e6 == guard_index ? 64'h2e6 : _GEN_760; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_762 = 10'h2e7 == guard_index ? 64'h2e7 : _GEN_761; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_763 = 10'h2e8 == guard_index ? 64'h2e8 : _GEN_762; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_764 = 10'h2e9 == guard_index ? 64'h2e9 : _GEN_763; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_765 = 10'h2ea == guard_index ? 64'h2ea : _GEN_764; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_766 = 10'h2eb == guard_index ? 64'h2eb : _GEN_765; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_767 = 10'h2ec == guard_index ? 64'h2ec : _GEN_766; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_768 = 10'h2ed == guard_index ? 64'h2ed : _GEN_767; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_769 = 10'h2ee == guard_index ? 64'h2ee : _GEN_768; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_770 = 10'h2ef == guard_index ? 64'h2ef : _GEN_769; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_771 = 10'h2f0 == guard_index ? 64'h2f0 : _GEN_770; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_772 = 10'h2f1 == guard_index ? 64'h2f1 : _GEN_771; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_773 = 10'h2f2 == guard_index ? 64'h2f2 : _GEN_772; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_774 = 10'h2f3 == guard_index ? 64'h2f3 : _GEN_773; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_775 = 10'h2f4 == guard_index ? 64'h2f4 : _GEN_774; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_776 = 10'h2f5 == guard_index ? 64'h2f5 : _GEN_775; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_777 = 10'h2f6 == guard_index ? 64'h2f6 : _GEN_776; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_778 = 10'h2f7 == guard_index ? 64'h2f7 : _GEN_777; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_779 = 10'h2f8 == guard_index ? 64'h2f8 : _GEN_778; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_780 = 10'h2f9 == guard_index ? 64'h2f9 : _GEN_779; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_781 = 10'h2fa == guard_index ? 64'h2fa : _GEN_780; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_782 = 10'h2fb == guard_index ? 64'h2fb : _GEN_781; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_783 = 10'h2fc == guard_index ? 64'h2fc : _GEN_782; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_784 = 10'h2fd == guard_index ? 64'h2fd : _GEN_783; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_785 = 10'h2fe == guard_index ? 64'h2fe : _GEN_784; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_786 = 10'h2ff == guard_index ? 64'h2ff : _GEN_785; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_787 = 10'h300 == guard_index ? 64'h300 : _GEN_786; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_788 = 10'h301 == guard_index ? 64'h301 : _GEN_787; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_789 = 10'h302 == guard_index ? 64'h302 : _GEN_788; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_790 = 10'h303 == guard_index ? 64'h303 : _GEN_789; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_791 = 10'h304 == guard_index ? 64'h304 : _GEN_790; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_792 = 10'h305 == guard_index ? 64'h305 : _GEN_791; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_793 = 10'h306 == guard_index ? 64'h306 : _GEN_792; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_794 = 10'h307 == guard_index ? 64'h307 : _GEN_793; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_795 = 10'h308 == guard_index ? 64'h308 : _GEN_794; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_796 = 10'h309 == guard_index ? 64'h309 : _GEN_795; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_797 = 10'h30a == guard_index ? 64'h30a : _GEN_796; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_798 = 10'h30b == guard_index ? 64'h30b : _GEN_797; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_799 = 10'h30c == guard_index ? 64'h30c : _GEN_798; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_800 = 10'h30d == guard_index ? 64'h30d : _GEN_799; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_801 = 10'h30e == guard_index ? 64'h30e : _GEN_800; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_802 = 10'h30f == guard_index ? 64'h30f : _GEN_801; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_803 = 10'h310 == guard_index ? 64'h310 : _GEN_802; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_804 = 10'h311 == guard_index ? 64'h311 : _GEN_803; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_805 = 10'h312 == guard_index ? 64'h312 : _GEN_804; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_806 = 10'h313 == guard_index ? 64'h313 : _GEN_805; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_807 = 10'h314 == guard_index ? 64'h314 : _GEN_806; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_808 = 10'h315 == guard_index ? 64'h315 : _GEN_807; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_809 = 10'h316 == guard_index ? 64'h316 : _GEN_808; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_810 = 10'h317 == guard_index ? 64'h317 : _GEN_809; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_811 = 10'h318 == guard_index ? 64'h318 : _GEN_810; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_812 = 10'h319 == guard_index ? 64'h319 : _GEN_811; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_813 = 10'h31a == guard_index ? 64'h31a : _GEN_812; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_814 = 10'h31b == guard_index ? 64'h31b : _GEN_813; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_815 = 10'h31c == guard_index ? 64'h31c : _GEN_814; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_816 = 10'h31d == guard_index ? 64'h31d : _GEN_815; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_817 = 10'h31e == guard_index ? 64'h31e : _GEN_816; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_818 = 10'h31f == guard_index ? 64'h31f : _GEN_817; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_819 = 10'h320 == guard_index ? 64'h320 : _GEN_818; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_820 = 10'h321 == guard_index ? 64'h321 : _GEN_819; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_821 = 10'h322 == guard_index ? 64'h322 : _GEN_820; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_822 = 10'h323 == guard_index ? 64'h323 : _GEN_821; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_823 = 10'h324 == guard_index ? 64'h324 : _GEN_822; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_824 = 10'h325 == guard_index ? 64'h325 : _GEN_823; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_825 = 10'h326 == guard_index ? 64'h326 : _GEN_824; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_826 = 10'h327 == guard_index ? 64'h327 : _GEN_825; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_827 = 10'h328 == guard_index ? 64'h328 : _GEN_826; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_828 = 10'h329 == guard_index ? 64'h329 : _GEN_827; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_829 = 10'h32a == guard_index ? 64'h32a : _GEN_828; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_830 = 10'h32b == guard_index ? 64'h32b : _GEN_829; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_831 = 10'h32c == guard_index ? 64'h32c : _GEN_830; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_832 = 10'h32d == guard_index ? 64'h32d : _GEN_831; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_833 = 10'h32e == guard_index ? 64'h32e : _GEN_832; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_834 = 10'h32f == guard_index ? 64'h32f : _GEN_833; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_835 = 10'h330 == guard_index ? 64'h330 : _GEN_834; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_836 = 10'h331 == guard_index ? 64'h331 : _GEN_835; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_837 = 10'h332 == guard_index ? 64'h332 : _GEN_836; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_838 = 10'h333 == guard_index ? 64'h333 : _GEN_837; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_839 = 10'h334 == guard_index ? 64'h334 : _GEN_838; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_840 = 10'h335 == guard_index ? 64'h335 : _GEN_839; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_841 = 10'h336 == guard_index ? 64'h336 : _GEN_840; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_842 = 10'h337 == guard_index ? 64'h337 : _GEN_841; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_843 = 10'h338 == guard_index ? 64'h338 : _GEN_842; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_844 = 10'h339 == guard_index ? 64'h339 : _GEN_843; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_845 = 10'h33a == guard_index ? 64'h33a : _GEN_844; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_846 = 10'h33b == guard_index ? 64'h33b : _GEN_845; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_847 = 10'h33c == guard_index ? 64'h33c : _GEN_846; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_848 = 10'h33d == guard_index ? 64'h33d : _GEN_847; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_849 = 10'h33e == guard_index ? 64'h33e : _GEN_848; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_850 = 10'h33f == guard_index ? 64'h33f : _GEN_849; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_851 = 10'h340 == guard_index ? 64'h340 : _GEN_850; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_852 = 10'h341 == guard_index ? 64'h341 : _GEN_851; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_853 = 10'h342 == guard_index ? 64'h342 : _GEN_852; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_854 = 10'h343 == guard_index ? 64'h343 : _GEN_853; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_855 = 10'h344 == guard_index ? 64'h344 : _GEN_854; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_856 = 10'h345 == guard_index ? 64'h345 : _GEN_855; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_857 = 10'h346 == guard_index ? 64'h346 : _GEN_856; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_858 = 10'h347 == guard_index ? 64'h347 : _GEN_857; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_859 = 10'h348 == guard_index ? 64'h348 : _GEN_858; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_860 = 10'h349 == guard_index ? 64'h349 : _GEN_859; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_861 = 10'h34a == guard_index ? 64'h34a : _GEN_860; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_862 = 10'h34b == guard_index ? 64'h34b : _GEN_861; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_863 = 10'h34c == guard_index ? 64'h34c : _GEN_862; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_864 = 10'h34d == guard_index ? 64'h34d : _GEN_863; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_865 = 10'h34e == guard_index ? 64'h34e : _GEN_864; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_866 = 10'h34f == guard_index ? 64'h34f : _GEN_865; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_867 = 10'h350 == guard_index ? 64'h350 : _GEN_866; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_868 = 10'h351 == guard_index ? 64'h351 : _GEN_867; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_869 = 10'h352 == guard_index ? 64'h352 : _GEN_868; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_870 = 10'h353 == guard_index ? 64'h353 : _GEN_869; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_871 = 10'h354 == guard_index ? 64'h354 : _GEN_870; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_872 = 10'h355 == guard_index ? 64'h355 : _GEN_871; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_873 = 10'h356 == guard_index ? 64'h356 : _GEN_872; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_874 = 10'h357 == guard_index ? 64'h357 : _GEN_873; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_875 = 10'h358 == guard_index ? 64'h358 : _GEN_874; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_876 = 10'h359 == guard_index ? 64'h359 : _GEN_875; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_877 = 10'h35a == guard_index ? 64'h35a : _GEN_876; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_878 = 10'h35b == guard_index ? 64'h35b : _GEN_877; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_879 = 10'h35c == guard_index ? 64'h35c : _GEN_878; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_880 = 10'h35d == guard_index ? 64'h35d : _GEN_879; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_881 = 10'h35e == guard_index ? 64'h35e : _GEN_880; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_882 = 10'h35f == guard_index ? 64'h35f : _GEN_881; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_883 = 10'h360 == guard_index ? 64'h360 : _GEN_882; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_884 = 10'h361 == guard_index ? 64'h361 : _GEN_883; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_885 = 10'h362 == guard_index ? 64'h362 : _GEN_884; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_886 = 10'h363 == guard_index ? 64'h363 : _GEN_885; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_887 = 10'h364 == guard_index ? 64'h364 : _GEN_886; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_888 = 10'h365 == guard_index ? 64'h365 : _GEN_887; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_889 = 10'h366 == guard_index ? 64'h366 : _GEN_888; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_890 = 10'h367 == guard_index ? 64'h367 : _GEN_889; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_891 = 10'h368 == guard_index ? 64'h368 : _GEN_890; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_892 = 10'h369 == guard_index ? 64'h369 : _GEN_891; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_893 = 10'h36a == guard_index ? 64'h36a : _GEN_892; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_894 = 10'h36b == guard_index ? 64'h36b : _GEN_893; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_895 = 10'h36c == guard_index ? 64'h36c : _GEN_894; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_896 = 10'h36d == guard_index ? 64'h36d : _GEN_895; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_897 = 10'h36e == guard_index ? 64'h36e : _GEN_896; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_898 = 10'h36f == guard_index ? 64'h36f : _GEN_897; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_899 = 10'h370 == guard_index ? 64'h370 : _GEN_898; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_900 = 10'h371 == guard_index ? 64'h371 : _GEN_899; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_901 = 10'h372 == guard_index ? 64'h372 : _GEN_900; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_902 = 10'h373 == guard_index ? 64'h373 : _GEN_901; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_903 = 10'h374 == guard_index ? 64'h374 : _GEN_902; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_904 = 10'h375 == guard_index ? 64'h375 : _GEN_903; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_905 = 10'h376 == guard_index ? 64'h376 : _GEN_904; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_906 = 10'h377 == guard_index ? 64'h377 : _GEN_905; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_907 = 10'h378 == guard_index ? 64'h378 : _GEN_906; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_908 = 10'h379 == guard_index ? 64'h379 : _GEN_907; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_909 = 10'h37a == guard_index ? 64'h37a : _GEN_908; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_910 = 10'h37b == guard_index ? 64'h37b : _GEN_909; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_911 = 10'h37c == guard_index ? 64'h37c : _GEN_910; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_912 = 10'h37d == guard_index ? 64'h37d : _GEN_911; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_913 = 10'h37e == guard_index ? 64'h37e : _GEN_912; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_914 = 10'h37f == guard_index ? 64'h37f : _GEN_913; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_915 = 10'h380 == guard_index ? 64'h380 : _GEN_914; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_916 = 10'h381 == guard_index ? 64'h381 : _GEN_915; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_917 = 10'h382 == guard_index ? 64'h382 : _GEN_916; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_918 = 10'h383 == guard_index ? 64'h383 : _GEN_917; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_919 = 10'h384 == guard_index ? 64'h384 : _GEN_918; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_920 = 10'h385 == guard_index ? 64'h385 : _GEN_919; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_921 = 10'h386 == guard_index ? 64'h386 : _GEN_920; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_922 = 10'h387 == guard_index ? 64'h387 : _GEN_921; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_923 = 10'h388 == guard_index ? 64'h388 : _GEN_922; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_924 = 10'h389 == guard_index ? 64'h389 : _GEN_923; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_925 = 10'h38a == guard_index ? 64'h38a : _GEN_924; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_926 = 10'h38b == guard_index ? 64'h38b : _GEN_925; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_927 = 10'h38c == guard_index ? 64'h38c : _GEN_926; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_928 = 10'h38d == guard_index ? 64'h38d : _GEN_927; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_929 = 10'h38e == guard_index ? 64'h38e : _GEN_928; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_930 = 10'h38f == guard_index ? 64'h38f : _GEN_929; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_931 = 10'h390 == guard_index ? 64'h390 : _GEN_930; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_932 = 10'h391 == guard_index ? 64'h391 : _GEN_931; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_933 = 10'h392 == guard_index ? 64'h392 : _GEN_932; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_934 = 10'h393 == guard_index ? 64'h393 : _GEN_933; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_935 = 10'h394 == guard_index ? 64'h394 : _GEN_934; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_936 = 10'h395 == guard_index ? 64'h395 : _GEN_935; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_937 = 10'h396 == guard_index ? 64'h396 : _GEN_936; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_938 = 10'h397 == guard_index ? 64'h397 : _GEN_937; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_939 = 10'h398 == guard_index ? 64'h398 : _GEN_938; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_940 = 10'h399 == guard_index ? 64'h399 : _GEN_939; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_941 = 10'h39a == guard_index ? 64'h39a : _GEN_940; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_942 = 10'h39b == guard_index ? 64'h39b : _GEN_941; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_943 = 10'h39c == guard_index ? 64'h39c : _GEN_942; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_944 = 10'h39d == guard_index ? 64'h39d : _GEN_943; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_945 = 10'h39e == guard_index ? 64'h39e : _GEN_944; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_946 = 10'h39f == guard_index ? 64'h39f : _GEN_945; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_947 = 10'h3a0 == guard_index ? 64'h3a0 : _GEN_946; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_948 = 10'h3a1 == guard_index ? 64'h3a1 : _GEN_947; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_949 = 10'h3a2 == guard_index ? 64'h3a2 : _GEN_948; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_950 = 10'h3a3 == guard_index ? 64'h3a3 : _GEN_949; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_951 = 10'h3a4 == guard_index ? 64'h3a4 : _GEN_950; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_952 = 10'h3a5 == guard_index ? 64'h3a5 : _GEN_951; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_953 = 10'h3a6 == guard_index ? 64'h3a6 : _GEN_952; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_954 = 10'h3a7 == guard_index ? 64'h3a7 : _GEN_953; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_955 = 10'h3a8 == guard_index ? 64'h3a8 : _GEN_954; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_956 = 10'h3a9 == guard_index ? 64'h3a9 : _GEN_955; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_957 = 10'h3aa == guard_index ? 64'h3aa : _GEN_956; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_958 = 10'h3ab == guard_index ? 64'h3ab : _GEN_957; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_959 = 10'h3ac == guard_index ? 64'h3ac : _GEN_958; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_960 = 10'h3ad == guard_index ? 64'h3ad : _GEN_959; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_961 = 10'h3ae == guard_index ? 64'h3ae : _GEN_960; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_962 = 10'h3af == guard_index ? 64'h3af : _GEN_961; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_963 = 10'h3b0 == guard_index ? 64'h3b0 : _GEN_962; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_964 = 10'h3b1 == guard_index ? 64'h3b1 : _GEN_963; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_965 = 10'h3b2 == guard_index ? 64'h3b2 : _GEN_964; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_966 = 10'h3b3 == guard_index ? 64'h3b3 : _GEN_965; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_967 = 10'h3b4 == guard_index ? 64'h3b4 : _GEN_966; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_968 = 10'h3b5 == guard_index ? 64'h3b5 : _GEN_967; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_969 = 10'h3b6 == guard_index ? 64'h3b6 : _GEN_968; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_970 = 10'h3b7 == guard_index ? 64'h3b7 : _GEN_969; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_971 = 10'h3b8 == guard_index ? 64'h3b8 : _GEN_970; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_972 = 10'h3b9 == guard_index ? 64'h3b9 : _GEN_971; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_973 = 10'h3ba == guard_index ? 64'h3ba : _GEN_972; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_974 = 10'h3bb == guard_index ? 64'h3bb : _GEN_973; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_975 = 10'h3bc == guard_index ? 64'h3bc : _GEN_974; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_976 = 10'h3bd == guard_index ? 64'h3bd : _GEN_975; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_977 = 10'h3be == guard_index ? 64'h3be : _GEN_976; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_978 = 10'h3bf == guard_index ? 64'h3bf : _GEN_977; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_979 = 10'h3c0 == guard_index ? 64'h3c0 : _GEN_978; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_980 = 10'h3c1 == guard_index ? 64'h3c1 : _GEN_979; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_981 = 10'h3c2 == guard_index ? 64'h3c2 : _GEN_980; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_982 = 10'h3c3 == guard_index ? 64'h3c3 : _GEN_981; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_983 = 10'h3c4 == guard_index ? 64'h3c4 : _GEN_982; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_984 = 10'h3c5 == guard_index ? 64'h3c5 : _GEN_983; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_985 = 10'h3c6 == guard_index ? 64'h3c6 : _GEN_984; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_986 = 10'h3c7 == guard_index ? 64'h3c7 : _GEN_985; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_987 = 10'h3c8 == guard_index ? 64'h3c8 : _GEN_986; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_988 = 10'h3c9 == guard_index ? 64'h3c9 : _GEN_987; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_989 = 10'h3ca == guard_index ? 64'h3ca : _GEN_988; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_990 = 10'h3cb == guard_index ? 64'h3cb : _GEN_989; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_991 = 10'h3cc == guard_index ? 64'h3cc : _GEN_990; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_992 = 10'h3cd == guard_index ? 64'h3cd : _GEN_991; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_993 = 10'h3ce == guard_index ? 64'h3ce : _GEN_992; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_994 = 10'h3cf == guard_index ? 64'h3cf : _GEN_993; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_995 = 10'h3d0 == guard_index ? 64'h3d0 : _GEN_994; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_996 = 10'h3d1 == guard_index ? 64'h3d1 : _GEN_995; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_997 = 10'h3d2 == guard_index ? 64'h3d2 : _GEN_996; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_998 = 10'h3d3 == guard_index ? 64'h3d3 : _GEN_997; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_999 = 10'h3d4 == guard_index ? 64'h3d4 : _GEN_998; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1000 = 10'h3d5 == guard_index ? 64'h3d5 : _GEN_999; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1001 = 10'h3d6 == guard_index ? 64'h3d6 : _GEN_1000; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1002 = 10'h3d7 == guard_index ? 64'h3d7 : _GEN_1001; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1003 = 10'h3d8 == guard_index ? 64'h3d8 : _GEN_1002; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1004 = 10'h3d9 == guard_index ? 64'h3d9 : _GEN_1003; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1005 = 10'h3da == guard_index ? 64'h3da : _GEN_1004; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1006 = 10'h3db == guard_index ? 64'h3db : _GEN_1005; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1007 = 10'h3dc == guard_index ? 64'h3dc : _GEN_1006; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1008 = 10'h3dd == guard_index ? 64'h3dd : _GEN_1007; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1009 = 10'h3de == guard_index ? 64'h3de : _GEN_1008; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1010 = 10'h3df == guard_index ? 64'h3df : _GEN_1009; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1011 = 10'h3e0 == guard_index ? 64'h3e0 : _GEN_1010; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1012 = 10'h3e1 == guard_index ? 64'h3e1 : _GEN_1011; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1013 = 10'h3e2 == guard_index ? 64'h3e2 : _GEN_1012; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1014 = 10'h3e3 == guard_index ? 64'h3e3 : _GEN_1013; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1015 = 10'h3e4 == guard_index ? 64'h3e4 : _GEN_1014; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1016 = 10'h3e5 == guard_index ? 64'h3e5 : _GEN_1015; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1017 = 10'h3e6 == guard_index ? 64'h3e6 : _GEN_1016; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1018 = 10'h3e7 == guard_index ? 64'h3e7 : _GEN_1017; // @[ComputeNode.scala 155:26]
  wire  _T_36 = FU_io_out != _GEN_1018; // @[ComputeNode.scala 155:26]
  wire  _T_22 = right_valid_R & left_valid_R; // @[ComputeNode.scala 71:19]
  wire  _T_23 = enable_valid_R & _T_22; // @[ComputeNode.scala 75:20]
  wire  _T_24 = _T_23 & enable_R_control; // @[ComputeNode.scala 75:38]
  wire  _T_26 = _T_24 & _T_32; // @[ComputeNode.scala 75:58]
  wire  _T_28 = guard_index == 10'h3e7; // @[Counter.scala 38:24]
  wire [9:0] _T_30 = guard_index + 10'h1; // @[Counter.scala 39:22]
  wire [63:0] _T_38_data = FU_io_out; // @[interfaces.scala 289:20 interfaces.scala 290:15]
  wire [63:0] _GEN_1021 = _T_36 ? _GEN_1018 : _T_38_data; // @[ComputeNode.scala 155:61]
  wire  _T_40 = _T_1 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire [63:0] _GEN_1026 = _T_34 ? _GEN_1021 : out_data_R; // @[ComputeNode.scala 147:81]
  wire  _GEN_1029 = _T_34 | out_valid_R_0; // @[ComputeNode.scala 147:81]
  wire  _GEN_1033 = _T_34 | state; // @[ComputeNode.scala 147:81]
  wire  _T_43 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  UALU_2 FU ( // @[ComputeNode.scala 61:18]
    .io_in1(FU_io_in1),
    .io_in2(FU_io_in2),
    .io_out(FU_io_out)
  );
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 205:19]
  assign io_Out_0_valid = _T_32 ? _GEN_1029 : out_valid_R_0; // @[HandShaking.scala 194:21 ComputeNode.scala 172:32]
  assign io_Out_0_bits_data = _T_32 ? _GEN_1026 : out_data_R; // @[ComputeNode.scala 137:25 ComputeNode.scala 158:35 ComputeNode.scala 166:35]
  assign io_LeftIO_ready = ~left_valid_R; // @[ComputeNode.scala 104:19]
  assign io_RightIO_ready = ~right_valid_R; // @[ComputeNode.scala 110:20]
  assign FU_io_in1 = left_R_data; // @[ComputeNode.scala 101:13]
  assign FU_io_in2 = right_R_data; // @[ComputeNode.scala 102:13]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {2{`RANDOM}};
  left_R_data = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  left_valid_R = _RAND_5[0:0];
  _RAND_6 = {2{`RANDOM}};
  right_R_data = _RAND_6[63:0];
  _RAND_7 = {1{`RANDOM}};
  right_valid_R = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  state = _RAND_8[0:0];
  _RAND_9 = {2{`RANDOM}};
  out_data_R = _RAND_9[63:0];
  _RAND_10 = {1{`RANDOM}};
  guard_index = _RAND_10[9:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_3) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_43) begin
        enable_valid_R <= 1'h0;
      end else if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_3) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_32) begin
      if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_43) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_1) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        out_valid_R_0 <= _T_40;
      end else if (_T_1) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_1) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      left_R_data <= 64'h0;
    end else if (_T_12) begin
      left_R_data <= io_LeftIO_bits_data;
    end
    if (reset) begin
      left_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        left_valid_R <= 1'h0;
      end else begin
        left_valid_R <= _GEN_11;
      end
    end else begin
      left_valid_R <= _GEN_11;
    end
    if (reset) begin
      right_R_data <= 64'h0;
    end else if (_T_14) begin
      right_R_data <= io_RightIO_bits_data;
    end
    if (reset) begin
      right_valid_R <= 1'h0;
    end else if (_T_32) begin
      if (_T_34) begin
        right_valid_R <= 1'h0;
      end else begin
        right_valid_R <= _GEN_15;
      end
    end else begin
      right_valid_R <= _GEN_15;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_32) begin
      state <= _GEN_1033;
    end else if (state) begin
      if (_T_43) begin
        state <= 1'h0;
      end
    end
    if (reset) begin
      out_data_R <= 64'h0;
    end else if (_T_32) begin
      if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (state) begin
      if (_T_43) begin
        out_data_R <= 64'h0;
      end else if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (enable_R_control) begin
      out_data_R <= FU_io_out;
    end else begin
      out_data_R <= 64'h0;
    end
    if (reset) begin
      guard_index <= 10'h0;
    end else if (_T_26) begin
      if (_T_28) begin
        guard_index <= 10'h0;
      end else begin
        guard_index <= _T_30;
      end
    end
  end
endmodule
module UnTypStoreCache(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_SuccOp_0_ready,
  output        io_SuccOp_0_valid,
  output        io_GepAddr_ready,
  input         io_GepAddr_valid,
  input  [63:0] io_GepAddr_bits_data,
  output        io_inData_ready,
  input         io_inData_valid,
  input  [63:0] io_inData_bits_data,
  input         io_MemReq_ready,
  output        io_MemReq_valid,
  output [63:0] io_MemReq_bits_addr,
  output [63:0] io_MemReq_bits_data,
  input         io_MemResp_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [63:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
`endif // RANDOMIZE_REG_INIT
  reg  enable_R_control; // @[HandShaking.scala 592:31]
  reg  enable_valid_R; // @[HandShaking.scala 593:31]
  reg  succ_ready_R_0; // @[HandShaking.scala 600:51]
  reg  succ_valid_R_0; // @[HandShaking.scala 601:51]
  wire  _T_5 = io_SuccOp_0_ready & io_SuccOp_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_1 = _T_5 ? 1'h0 : succ_valid_R_0; // @[HandShaking.scala 622:32]
  wire  _T_8 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] addr_R_data; // @[StoreCache.scala 59:23]
  reg [63:0] data_R_data; // @[StoreCache.scala 60:23]
  reg  addr_valid_R; // @[StoreCache.scala 61:29]
  reg  data_valid_R; // @[StoreCache.scala 62:29]
  reg [1:0] state; // @[StoreCache.scala 66:22]
  wire  _T_18 = io_GepAddr_ready & io_GepAddr_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_13 = _T_18 | addr_valid_R; // @[StoreCache.scala 80:27]
  wire  _T_19 = io_inData_ready & io_inData_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_17 = _T_19 | data_valid_R; // @[StoreCache.scala 85:26]
  wire  mem_req_fire = addr_valid_R & data_valid_R; // @[StoreCache.scala 102:51]
  wire  _T_38 = 2'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_40 = data_valid_R & addr_valid_R; // @[StoreCache.scala 154:27]
  wire  _T_41 = enable_R_control & mem_req_fire; // @[StoreCache.scala 155:33]
  wire  _GEN_28 = _T_40 & _T_41; // @[StoreCache.scala 154:44]
  wire  _GEN_33 = enable_valid_R & _GEN_28; // @[StoreCache.scala 153:51]
  wire  _T_44 = 2'h1 == state; // @[Conditional.scala 37:30]
  wire  _GEN_38 = io_MemResp_valid | _GEN_1; // @[StoreCache.scala 188:30]
  wire  _T_47 = 2'h2 == state; // @[Conditional.scala 37:30]
  wire  _T_49 = &succ_ready_R_0; // @[HandShaking.scala 707:36]
  wire  _T_51 = &io_SuccOp_0_ready; // @[HandShaking.scala 707:72]
  wire  _T_52 = _T_49 | _T_51; // @[HandShaking.scala 707:41]
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 650:19]
  assign io_SuccOp_0_valid = succ_valid_R_0; // @[HandShaking.scala 619:24]
  assign io_GepAddr_ready = ~addr_valid_R; // @[StoreCache.scala 75:20 StoreCache.scala 79:20]
  assign io_inData_ready = ~data_valid_R; // @[StoreCache.scala 76:19]
  assign io_MemReq_valid = _T_38 & _GEN_33; // @[StoreCache.scala 145:19 StoreCache.scala 156:29]
  assign io_MemReq_bits_addr = addr_R_data; // @[StoreCache.scala 139:23]
  assign io_MemReq_bits_data = data_R_data; // @[StoreCache.scala 140:23]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  succ_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  succ_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {2{`RANDOM}};
  addr_R_data = _RAND_4[63:0];
  _RAND_5 = {2{`RANDOM}};
  data_R_data = _RAND_5[63:0];
  _RAND_6 = {1{`RANDOM}};
  addr_valid_R = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  data_valid_R = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  state = _RAND_8[1:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_8) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_38) begin
      if (_T_8) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_44) begin
      if (_T_8) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_47) begin
      if (_T_52) begin
        enable_valid_R <= 1'h0;
      end else if (_T_8) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_8) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      succ_ready_R_0 <= 1'h0;
    end else if (_T_38) begin
      if (_T_5) begin
        succ_ready_R_0 <= io_SuccOp_0_ready;
      end
    end else if (_T_44) begin
      if (_T_5) begin
        succ_ready_R_0 <= io_SuccOp_0_ready;
      end
    end else if (_T_47) begin
      if (_T_52) begin
        succ_ready_R_0 <= 1'h0;
      end else if (_T_5) begin
        succ_ready_R_0 <= io_SuccOp_0_ready;
      end
    end else if (_T_5) begin
      succ_ready_R_0 <= io_SuccOp_0_ready;
    end
    if (reset) begin
      succ_valid_R_0 <= 1'h0;
    end else if (_T_38) begin
      if (enable_valid_R) begin
        if (_T_40) begin
          if (_T_41) begin
            if (_T_5) begin
              succ_valid_R_0 <= 1'h0;
            end
          end else begin
            succ_valid_R_0 <= 1'h1;
          end
        end else if (_T_5) begin
          succ_valid_R_0 <= 1'h0;
        end
      end else if (_T_5) begin
        succ_valid_R_0 <= 1'h0;
      end
    end else if (_T_44) begin
      succ_valid_R_0 <= _GEN_38;
    end else if (_T_5) begin
      succ_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      addr_R_data <= 64'h0;
    end else if (_T_38) begin
      if (_T_18) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_44) begin
      if (_T_18) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_47) begin
      if (_T_52) begin
        addr_R_data <= 64'h0;
      end else if (_T_18) begin
        addr_R_data <= io_GepAddr_bits_data;
      end
    end else if (_T_18) begin
      addr_R_data <= io_GepAddr_bits_data;
    end
    if (reset) begin
      data_R_data <= 64'h0;
    end else if (_T_38) begin
      if (_T_19) begin
        data_R_data <= io_inData_bits_data;
      end
    end else if (_T_44) begin
      if (_T_19) begin
        data_R_data <= io_inData_bits_data;
      end
    end else if (_T_47) begin
      if (_T_52) begin
        data_R_data <= 64'h0;
      end else if (_T_19) begin
        data_R_data <= io_inData_bits_data;
      end
    end else if (_T_19) begin
      data_R_data <= io_inData_bits_data;
    end
    if (reset) begin
      addr_valid_R <= 1'h0;
    end else if (_T_38) begin
      addr_valid_R <= _GEN_13;
    end else if (_T_44) begin
      addr_valid_R <= _GEN_13;
    end else if (_T_47) begin
      if (_T_52) begin
        addr_valid_R <= 1'h0;
      end else begin
        addr_valid_R <= _GEN_13;
      end
    end else begin
      addr_valid_R <= _GEN_13;
    end
    if (reset) begin
      data_valid_R <= 1'h0;
    end else if (_T_38) begin
      data_valid_R <= _GEN_17;
    end else if (_T_44) begin
      data_valid_R <= _GEN_17;
    end else if (_T_47) begin
      if (_T_52) begin
        data_valid_R <= 1'h0;
      end else begin
        data_valid_R <= _GEN_17;
      end
    end else begin
      data_valid_R <= _GEN_17;
    end
    if (reset) begin
      state <= 2'h0;
    end else if (_T_38) begin
      if (enable_valid_R) begin
        if (_T_40) begin
          if (_T_41) begin
            if (io_MemReq_ready) begin
              state <= 2'h1;
            end
          end else begin
            state <= 2'h2;
          end
        end
      end
    end else if (_T_44) begin
      if (io_MemResp_valid) begin
        state <= 2'h2;
      end
    end else if (_T_47) begin
      if (_T_52) begin
        state <= 2'h0;
      end
    end
  end
endmodule
module ComputeNode_3(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  input         io_Out_1_ready,
  output        io_Out_1_valid,
  output [63:0] io_Out_1_bits_data,
  output        io_LeftIO_ready,
  input         io_LeftIO_valid,
  input  [63:0] io_LeftIO_bits_data,
  output        io_RightIO_ready,
  input         io_RightIO_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [63:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [63:0] _RAND_11;
  reg [31:0] _RAND_12;
`endif // RANDOMIZE_REG_INIT
  wire [63:0] FU_io_in1; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_in2; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_out; // @[ComputeNode.scala 61:18]
  reg  enable_R_control; // @[HandShaking.scala 181:31]
  reg  enable_valid_R; // @[HandShaking.scala 182:31]
  reg  out_ready_R_0; // @[HandShaking.scala 185:46]
  reg  out_ready_R_1; // @[HandShaking.scala 185:46]
  reg  out_valid_R_0; // @[HandShaking.scala 186:46]
  reg  out_valid_R_1; // @[HandShaking.scala 186:46]
  wire  _T_1 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_2 = io_Out_1_ready & io_Out_1_valid; // @[Decoupled.scala 40:37]
  wire  _T_4 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] left_R_data; // @[ComputeNode.scala 53:23]
  reg  left_valid_R; // @[ComputeNode.scala 54:29]
  reg [63:0] right_R_data; // @[ComputeNode.scala 57:24]
  reg  right_valid_R; // @[ComputeNode.scala 58:30]
  reg  state; // @[ComputeNode.scala 64:22]
  reg [63:0] out_data_R; // @[ComputeNode.scala 89:27]
  wire  _T_13 = io_LeftIO_ready & io_LeftIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_13 = _T_13 | left_valid_R; // @[ComputeNode.scala 105:26]
  wire  _T_15 = io_RightIO_ready & io_RightIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_17 = _T_15 | right_valid_R; // @[ComputeNode.scala 111:27]
  wire  _T_34 = ~state; // @[Conditional.scala 37:30]
  wire  _T_35 = enable_valid_R & left_valid_R; // @[ComputeNode.scala 147:27]
  wire  _T_36 = _T_35 & right_valid_R; // @[ComputeNode.scala 147:43]
  reg [9:0] guard_index; // @[Counter.scala 29:33]
  wire [63:0] _GEN_22 = 10'h1 == guard_index ? 64'h1 : 64'h0; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_23 = 10'h2 == guard_index ? 64'h2 : _GEN_22; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_24 = 10'h3 == guard_index ? 64'h3 : _GEN_23; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_25 = 10'h4 == guard_index ? 64'h4 : _GEN_24; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_26 = 10'h5 == guard_index ? 64'h5 : _GEN_25; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_27 = 10'h6 == guard_index ? 64'h6 : _GEN_26; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_28 = 10'h7 == guard_index ? 64'h7 : _GEN_27; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_29 = 10'h8 == guard_index ? 64'h8 : _GEN_28; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_30 = 10'h9 == guard_index ? 64'h9 : _GEN_29; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_31 = 10'ha == guard_index ? 64'ha : _GEN_30; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_32 = 10'hb == guard_index ? 64'hb : _GEN_31; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_33 = 10'hc == guard_index ? 64'hc : _GEN_32; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_34 = 10'hd == guard_index ? 64'hd : _GEN_33; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_35 = 10'he == guard_index ? 64'he : _GEN_34; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_36 = 10'hf == guard_index ? 64'hf : _GEN_35; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_37 = 10'h10 == guard_index ? 64'h10 : _GEN_36; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_38 = 10'h11 == guard_index ? 64'h11 : _GEN_37; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_39 = 10'h12 == guard_index ? 64'h12 : _GEN_38; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_40 = 10'h13 == guard_index ? 64'h13 : _GEN_39; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_41 = 10'h14 == guard_index ? 64'h14 : _GEN_40; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_42 = 10'h15 == guard_index ? 64'h15 : _GEN_41; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_43 = 10'h16 == guard_index ? 64'h16 : _GEN_42; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_44 = 10'h17 == guard_index ? 64'h17 : _GEN_43; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_45 = 10'h18 == guard_index ? 64'h18 : _GEN_44; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_46 = 10'h19 == guard_index ? 64'h19 : _GEN_45; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_47 = 10'h1a == guard_index ? 64'h1a : _GEN_46; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_48 = 10'h1b == guard_index ? 64'h1b : _GEN_47; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_49 = 10'h1c == guard_index ? 64'h1c : _GEN_48; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_50 = 10'h1d == guard_index ? 64'h1d : _GEN_49; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_51 = 10'h1e == guard_index ? 64'h1e : _GEN_50; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_52 = 10'h1f == guard_index ? 64'h1f : _GEN_51; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_53 = 10'h20 == guard_index ? 64'h20 : _GEN_52; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_54 = 10'h21 == guard_index ? 64'h21 : _GEN_53; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_55 = 10'h22 == guard_index ? 64'h22 : _GEN_54; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_56 = 10'h23 == guard_index ? 64'h23 : _GEN_55; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_57 = 10'h24 == guard_index ? 64'h24 : _GEN_56; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_58 = 10'h25 == guard_index ? 64'h25 : _GEN_57; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_59 = 10'h26 == guard_index ? 64'h26 : _GEN_58; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_60 = 10'h27 == guard_index ? 64'h27 : _GEN_59; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_61 = 10'h28 == guard_index ? 64'h28 : _GEN_60; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_62 = 10'h29 == guard_index ? 64'h29 : _GEN_61; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_63 = 10'h2a == guard_index ? 64'h2a : _GEN_62; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_64 = 10'h2b == guard_index ? 64'h2b : _GEN_63; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_65 = 10'h2c == guard_index ? 64'h2c : _GEN_64; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_66 = 10'h2d == guard_index ? 64'h2d : _GEN_65; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_67 = 10'h2e == guard_index ? 64'h2e : _GEN_66; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_68 = 10'h2f == guard_index ? 64'h2f : _GEN_67; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_69 = 10'h30 == guard_index ? 64'h30 : _GEN_68; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_70 = 10'h31 == guard_index ? 64'h31 : _GEN_69; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_71 = 10'h32 == guard_index ? 64'h32 : _GEN_70; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_72 = 10'h33 == guard_index ? 64'h33 : _GEN_71; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_73 = 10'h34 == guard_index ? 64'h34 : _GEN_72; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_74 = 10'h35 == guard_index ? 64'h35 : _GEN_73; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_75 = 10'h36 == guard_index ? 64'h36 : _GEN_74; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_76 = 10'h37 == guard_index ? 64'h37 : _GEN_75; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_77 = 10'h38 == guard_index ? 64'h38 : _GEN_76; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_78 = 10'h39 == guard_index ? 64'h39 : _GEN_77; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_79 = 10'h3a == guard_index ? 64'h3a : _GEN_78; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_80 = 10'h3b == guard_index ? 64'h3b : _GEN_79; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_81 = 10'h3c == guard_index ? 64'h3c : _GEN_80; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_82 = 10'h3d == guard_index ? 64'h3d : _GEN_81; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_83 = 10'h3e == guard_index ? 64'h3e : _GEN_82; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_84 = 10'h3f == guard_index ? 64'h3f : _GEN_83; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_85 = 10'h40 == guard_index ? 64'h40 : _GEN_84; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_86 = 10'h41 == guard_index ? 64'h41 : _GEN_85; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_87 = 10'h42 == guard_index ? 64'h42 : _GEN_86; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_88 = 10'h43 == guard_index ? 64'h43 : _GEN_87; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_89 = 10'h44 == guard_index ? 64'h44 : _GEN_88; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_90 = 10'h45 == guard_index ? 64'h45 : _GEN_89; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_91 = 10'h46 == guard_index ? 64'h46 : _GEN_90; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_92 = 10'h47 == guard_index ? 64'h47 : _GEN_91; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_93 = 10'h48 == guard_index ? 64'h48 : _GEN_92; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_94 = 10'h49 == guard_index ? 64'h49 : _GEN_93; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_95 = 10'h4a == guard_index ? 64'h4a : _GEN_94; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_96 = 10'h4b == guard_index ? 64'h4b : _GEN_95; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_97 = 10'h4c == guard_index ? 64'h4c : _GEN_96; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_98 = 10'h4d == guard_index ? 64'h4d : _GEN_97; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_99 = 10'h4e == guard_index ? 64'h4e : _GEN_98; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_100 = 10'h4f == guard_index ? 64'h4f : _GEN_99; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_101 = 10'h50 == guard_index ? 64'h50 : _GEN_100; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_102 = 10'h51 == guard_index ? 64'h51 : _GEN_101; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_103 = 10'h52 == guard_index ? 64'h52 : _GEN_102; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_104 = 10'h53 == guard_index ? 64'h53 : _GEN_103; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_105 = 10'h54 == guard_index ? 64'h54 : _GEN_104; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_106 = 10'h55 == guard_index ? 64'h55 : _GEN_105; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_107 = 10'h56 == guard_index ? 64'h56 : _GEN_106; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_108 = 10'h57 == guard_index ? 64'h57 : _GEN_107; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_109 = 10'h58 == guard_index ? 64'h58 : _GEN_108; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_110 = 10'h59 == guard_index ? 64'h59 : _GEN_109; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_111 = 10'h5a == guard_index ? 64'h5a : _GEN_110; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_112 = 10'h5b == guard_index ? 64'h5b : _GEN_111; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_113 = 10'h5c == guard_index ? 64'h5c : _GEN_112; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_114 = 10'h5d == guard_index ? 64'h5d : _GEN_113; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_115 = 10'h5e == guard_index ? 64'h5e : _GEN_114; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_116 = 10'h5f == guard_index ? 64'h5f : _GEN_115; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_117 = 10'h60 == guard_index ? 64'h60 : _GEN_116; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_118 = 10'h61 == guard_index ? 64'h61 : _GEN_117; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_119 = 10'h62 == guard_index ? 64'h62 : _GEN_118; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_120 = 10'h63 == guard_index ? 64'h63 : _GEN_119; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_121 = 10'h64 == guard_index ? 64'h64 : _GEN_120; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_122 = 10'h65 == guard_index ? 64'h65 : _GEN_121; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_123 = 10'h66 == guard_index ? 64'h66 : _GEN_122; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_124 = 10'h67 == guard_index ? 64'h67 : _GEN_123; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_125 = 10'h68 == guard_index ? 64'h68 : _GEN_124; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_126 = 10'h69 == guard_index ? 64'h69 : _GEN_125; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_127 = 10'h6a == guard_index ? 64'h6a : _GEN_126; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_128 = 10'h6b == guard_index ? 64'h6b : _GEN_127; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_129 = 10'h6c == guard_index ? 64'h6c : _GEN_128; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_130 = 10'h6d == guard_index ? 64'h6d : _GEN_129; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_131 = 10'h6e == guard_index ? 64'h6e : _GEN_130; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_132 = 10'h6f == guard_index ? 64'h6f : _GEN_131; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_133 = 10'h70 == guard_index ? 64'h70 : _GEN_132; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_134 = 10'h71 == guard_index ? 64'h71 : _GEN_133; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_135 = 10'h72 == guard_index ? 64'h72 : _GEN_134; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_136 = 10'h73 == guard_index ? 64'h73 : _GEN_135; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_137 = 10'h74 == guard_index ? 64'h74 : _GEN_136; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_138 = 10'h75 == guard_index ? 64'h75 : _GEN_137; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_139 = 10'h76 == guard_index ? 64'h76 : _GEN_138; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_140 = 10'h77 == guard_index ? 64'h77 : _GEN_139; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_141 = 10'h78 == guard_index ? 64'h78 : _GEN_140; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_142 = 10'h79 == guard_index ? 64'h79 : _GEN_141; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_143 = 10'h7a == guard_index ? 64'h7a : _GEN_142; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_144 = 10'h7b == guard_index ? 64'h7b : _GEN_143; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_145 = 10'h7c == guard_index ? 64'h7c : _GEN_144; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_146 = 10'h7d == guard_index ? 64'h7d : _GEN_145; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_147 = 10'h7e == guard_index ? 64'h7e : _GEN_146; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_148 = 10'h7f == guard_index ? 64'h7f : _GEN_147; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_149 = 10'h80 == guard_index ? 64'h80 : _GEN_148; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_150 = 10'h81 == guard_index ? 64'h81 : _GEN_149; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_151 = 10'h82 == guard_index ? 64'h82 : _GEN_150; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_152 = 10'h83 == guard_index ? 64'h83 : _GEN_151; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_153 = 10'h84 == guard_index ? 64'h84 : _GEN_152; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_154 = 10'h85 == guard_index ? 64'h85 : _GEN_153; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_155 = 10'h86 == guard_index ? 64'h86 : _GEN_154; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_156 = 10'h87 == guard_index ? 64'h87 : _GEN_155; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_157 = 10'h88 == guard_index ? 64'h88 : _GEN_156; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_158 = 10'h89 == guard_index ? 64'h89 : _GEN_157; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_159 = 10'h8a == guard_index ? 64'h8a : _GEN_158; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_160 = 10'h8b == guard_index ? 64'h8b : _GEN_159; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_161 = 10'h8c == guard_index ? 64'h8c : _GEN_160; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_162 = 10'h8d == guard_index ? 64'h8d : _GEN_161; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_163 = 10'h8e == guard_index ? 64'h8e : _GEN_162; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_164 = 10'h8f == guard_index ? 64'h8f : _GEN_163; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_165 = 10'h90 == guard_index ? 64'h90 : _GEN_164; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_166 = 10'h91 == guard_index ? 64'h91 : _GEN_165; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_167 = 10'h92 == guard_index ? 64'h92 : _GEN_166; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_168 = 10'h93 == guard_index ? 64'h93 : _GEN_167; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_169 = 10'h94 == guard_index ? 64'h94 : _GEN_168; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_170 = 10'h95 == guard_index ? 64'h95 : _GEN_169; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_171 = 10'h96 == guard_index ? 64'h96 : _GEN_170; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_172 = 10'h97 == guard_index ? 64'h97 : _GEN_171; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_173 = 10'h98 == guard_index ? 64'h98 : _GEN_172; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_174 = 10'h99 == guard_index ? 64'h99 : _GEN_173; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_175 = 10'h9a == guard_index ? 64'h9a : _GEN_174; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_176 = 10'h9b == guard_index ? 64'h9b : _GEN_175; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_177 = 10'h9c == guard_index ? 64'h9c : _GEN_176; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_178 = 10'h9d == guard_index ? 64'h9d : _GEN_177; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_179 = 10'h9e == guard_index ? 64'h9e : _GEN_178; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_180 = 10'h9f == guard_index ? 64'h9f : _GEN_179; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_181 = 10'ha0 == guard_index ? 64'ha0 : _GEN_180; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_182 = 10'ha1 == guard_index ? 64'ha1 : _GEN_181; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_183 = 10'ha2 == guard_index ? 64'ha2 : _GEN_182; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_184 = 10'ha3 == guard_index ? 64'ha3 : _GEN_183; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_185 = 10'ha4 == guard_index ? 64'ha4 : _GEN_184; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_186 = 10'ha5 == guard_index ? 64'ha5 : _GEN_185; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_187 = 10'ha6 == guard_index ? 64'ha6 : _GEN_186; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_188 = 10'ha7 == guard_index ? 64'ha7 : _GEN_187; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_189 = 10'ha8 == guard_index ? 64'ha8 : _GEN_188; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_190 = 10'ha9 == guard_index ? 64'ha9 : _GEN_189; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_191 = 10'haa == guard_index ? 64'haa : _GEN_190; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_192 = 10'hab == guard_index ? 64'hab : _GEN_191; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_193 = 10'hac == guard_index ? 64'hac : _GEN_192; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_194 = 10'had == guard_index ? 64'had : _GEN_193; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_195 = 10'hae == guard_index ? 64'hae : _GEN_194; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_196 = 10'haf == guard_index ? 64'haf : _GEN_195; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_197 = 10'hb0 == guard_index ? 64'hb0 : _GEN_196; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_198 = 10'hb1 == guard_index ? 64'hb1 : _GEN_197; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_199 = 10'hb2 == guard_index ? 64'hb2 : _GEN_198; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_200 = 10'hb3 == guard_index ? 64'hb3 : _GEN_199; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_201 = 10'hb4 == guard_index ? 64'hb4 : _GEN_200; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_202 = 10'hb5 == guard_index ? 64'hb5 : _GEN_201; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_203 = 10'hb6 == guard_index ? 64'hb6 : _GEN_202; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_204 = 10'hb7 == guard_index ? 64'hb7 : _GEN_203; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_205 = 10'hb8 == guard_index ? 64'hb8 : _GEN_204; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_206 = 10'hb9 == guard_index ? 64'hb9 : _GEN_205; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_207 = 10'hba == guard_index ? 64'hba : _GEN_206; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_208 = 10'hbb == guard_index ? 64'hbb : _GEN_207; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_209 = 10'hbc == guard_index ? 64'hbc : _GEN_208; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_210 = 10'hbd == guard_index ? 64'hbd : _GEN_209; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_211 = 10'hbe == guard_index ? 64'hbe : _GEN_210; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_212 = 10'hbf == guard_index ? 64'hbf : _GEN_211; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_213 = 10'hc0 == guard_index ? 64'hc0 : _GEN_212; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_214 = 10'hc1 == guard_index ? 64'hc1 : _GEN_213; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_215 = 10'hc2 == guard_index ? 64'hc2 : _GEN_214; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_216 = 10'hc3 == guard_index ? 64'hc3 : _GEN_215; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_217 = 10'hc4 == guard_index ? 64'hc4 : _GEN_216; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_218 = 10'hc5 == guard_index ? 64'hc5 : _GEN_217; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_219 = 10'hc6 == guard_index ? 64'hc6 : _GEN_218; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_220 = 10'hc7 == guard_index ? 64'hc7 : _GEN_219; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_221 = 10'hc8 == guard_index ? 64'hc8 : _GEN_220; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_222 = 10'hc9 == guard_index ? 64'hc9 : _GEN_221; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_223 = 10'hca == guard_index ? 64'hca : _GEN_222; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_224 = 10'hcb == guard_index ? 64'hcb : _GEN_223; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_225 = 10'hcc == guard_index ? 64'hcc : _GEN_224; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_226 = 10'hcd == guard_index ? 64'hcd : _GEN_225; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_227 = 10'hce == guard_index ? 64'hce : _GEN_226; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_228 = 10'hcf == guard_index ? 64'hcf : _GEN_227; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_229 = 10'hd0 == guard_index ? 64'hd0 : _GEN_228; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_230 = 10'hd1 == guard_index ? 64'hd1 : _GEN_229; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_231 = 10'hd2 == guard_index ? 64'hd2 : _GEN_230; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_232 = 10'hd3 == guard_index ? 64'hd3 : _GEN_231; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_233 = 10'hd4 == guard_index ? 64'hd4 : _GEN_232; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_234 = 10'hd5 == guard_index ? 64'hd5 : _GEN_233; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_235 = 10'hd6 == guard_index ? 64'hd6 : _GEN_234; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_236 = 10'hd7 == guard_index ? 64'hd7 : _GEN_235; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_237 = 10'hd8 == guard_index ? 64'hd8 : _GEN_236; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_238 = 10'hd9 == guard_index ? 64'hd9 : _GEN_237; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_239 = 10'hda == guard_index ? 64'hda : _GEN_238; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_240 = 10'hdb == guard_index ? 64'hdb : _GEN_239; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_241 = 10'hdc == guard_index ? 64'hdc : _GEN_240; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_242 = 10'hdd == guard_index ? 64'hdd : _GEN_241; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_243 = 10'hde == guard_index ? 64'hde : _GEN_242; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_244 = 10'hdf == guard_index ? 64'hdf : _GEN_243; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_245 = 10'he0 == guard_index ? 64'he0 : _GEN_244; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_246 = 10'he1 == guard_index ? 64'he1 : _GEN_245; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_247 = 10'he2 == guard_index ? 64'he2 : _GEN_246; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_248 = 10'he3 == guard_index ? 64'he3 : _GEN_247; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_249 = 10'he4 == guard_index ? 64'he4 : _GEN_248; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_250 = 10'he5 == guard_index ? 64'he5 : _GEN_249; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_251 = 10'he6 == guard_index ? 64'he6 : _GEN_250; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_252 = 10'he7 == guard_index ? 64'he7 : _GEN_251; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_253 = 10'he8 == guard_index ? 64'he8 : _GEN_252; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_254 = 10'he9 == guard_index ? 64'he9 : _GEN_253; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_255 = 10'hea == guard_index ? 64'hea : _GEN_254; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_256 = 10'heb == guard_index ? 64'heb : _GEN_255; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_257 = 10'hec == guard_index ? 64'hec : _GEN_256; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_258 = 10'hed == guard_index ? 64'hed : _GEN_257; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_259 = 10'hee == guard_index ? 64'hee : _GEN_258; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_260 = 10'hef == guard_index ? 64'hef : _GEN_259; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_261 = 10'hf0 == guard_index ? 64'hf0 : _GEN_260; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_262 = 10'hf1 == guard_index ? 64'hf1 : _GEN_261; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_263 = 10'hf2 == guard_index ? 64'hf2 : _GEN_262; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_264 = 10'hf3 == guard_index ? 64'hf3 : _GEN_263; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_265 = 10'hf4 == guard_index ? 64'hf4 : _GEN_264; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_266 = 10'hf5 == guard_index ? 64'hf5 : _GEN_265; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_267 = 10'hf6 == guard_index ? 64'hf6 : _GEN_266; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_268 = 10'hf7 == guard_index ? 64'hf7 : _GEN_267; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_269 = 10'hf8 == guard_index ? 64'hf8 : _GEN_268; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_270 = 10'hf9 == guard_index ? 64'hf9 : _GEN_269; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_271 = 10'hfa == guard_index ? 64'hfa : _GEN_270; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_272 = 10'hfb == guard_index ? 64'hfb : _GEN_271; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_273 = 10'hfc == guard_index ? 64'hfc : _GEN_272; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_274 = 10'hfd == guard_index ? 64'hfd : _GEN_273; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_275 = 10'hfe == guard_index ? 64'hfe : _GEN_274; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_276 = 10'hff == guard_index ? 64'hff : _GEN_275; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_277 = 10'h100 == guard_index ? 64'h100 : _GEN_276; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_278 = 10'h101 == guard_index ? 64'h101 : _GEN_277; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_279 = 10'h102 == guard_index ? 64'h102 : _GEN_278; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_280 = 10'h103 == guard_index ? 64'h103 : _GEN_279; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_281 = 10'h104 == guard_index ? 64'h104 : _GEN_280; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_282 = 10'h105 == guard_index ? 64'h105 : _GEN_281; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_283 = 10'h106 == guard_index ? 64'h106 : _GEN_282; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_284 = 10'h107 == guard_index ? 64'h107 : _GEN_283; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_285 = 10'h108 == guard_index ? 64'h108 : _GEN_284; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_286 = 10'h109 == guard_index ? 64'h109 : _GEN_285; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_287 = 10'h10a == guard_index ? 64'h10a : _GEN_286; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_288 = 10'h10b == guard_index ? 64'h10b : _GEN_287; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_289 = 10'h10c == guard_index ? 64'h10c : _GEN_288; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_290 = 10'h10d == guard_index ? 64'h10d : _GEN_289; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_291 = 10'h10e == guard_index ? 64'h10e : _GEN_290; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_292 = 10'h10f == guard_index ? 64'h10f : _GEN_291; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_293 = 10'h110 == guard_index ? 64'h110 : _GEN_292; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_294 = 10'h111 == guard_index ? 64'h111 : _GEN_293; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_295 = 10'h112 == guard_index ? 64'h112 : _GEN_294; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_296 = 10'h113 == guard_index ? 64'h113 : _GEN_295; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_297 = 10'h114 == guard_index ? 64'h114 : _GEN_296; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_298 = 10'h115 == guard_index ? 64'h115 : _GEN_297; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_299 = 10'h116 == guard_index ? 64'h116 : _GEN_298; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_300 = 10'h117 == guard_index ? 64'h117 : _GEN_299; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_301 = 10'h118 == guard_index ? 64'h118 : _GEN_300; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_302 = 10'h119 == guard_index ? 64'h119 : _GEN_301; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_303 = 10'h11a == guard_index ? 64'h11a : _GEN_302; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_304 = 10'h11b == guard_index ? 64'h11b : _GEN_303; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_305 = 10'h11c == guard_index ? 64'h11c : _GEN_304; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_306 = 10'h11d == guard_index ? 64'h11d : _GEN_305; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_307 = 10'h11e == guard_index ? 64'h11e : _GEN_306; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_308 = 10'h11f == guard_index ? 64'h11f : _GEN_307; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_309 = 10'h120 == guard_index ? 64'h120 : _GEN_308; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_310 = 10'h121 == guard_index ? 64'h121 : _GEN_309; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_311 = 10'h122 == guard_index ? 64'h122 : _GEN_310; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_312 = 10'h123 == guard_index ? 64'h123 : _GEN_311; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_313 = 10'h124 == guard_index ? 64'h124 : _GEN_312; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_314 = 10'h125 == guard_index ? 64'h125 : _GEN_313; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_315 = 10'h126 == guard_index ? 64'h126 : _GEN_314; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_316 = 10'h127 == guard_index ? 64'h127 : _GEN_315; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_317 = 10'h128 == guard_index ? 64'h128 : _GEN_316; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_318 = 10'h129 == guard_index ? 64'h129 : _GEN_317; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_319 = 10'h12a == guard_index ? 64'h12a : _GEN_318; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_320 = 10'h12b == guard_index ? 64'h12b : _GEN_319; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_321 = 10'h12c == guard_index ? 64'h12c : _GEN_320; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_322 = 10'h12d == guard_index ? 64'h12d : _GEN_321; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_323 = 10'h12e == guard_index ? 64'h12e : _GEN_322; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_324 = 10'h12f == guard_index ? 64'h12f : _GEN_323; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_325 = 10'h130 == guard_index ? 64'h130 : _GEN_324; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_326 = 10'h131 == guard_index ? 64'h131 : _GEN_325; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_327 = 10'h132 == guard_index ? 64'h132 : _GEN_326; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_328 = 10'h133 == guard_index ? 64'h133 : _GEN_327; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_329 = 10'h134 == guard_index ? 64'h134 : _GEN_328; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_330 = 10'h135 == guard_index ? 64'h135 : _GEN_329; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_331 = 10'h136 == guard_index ? 64'h136 : _GEN_330; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_332 = 10'h137 == guard_index ? 64'h137 : _GEN_331; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_333 = 10'h138 == guard_index ? 64'h138 : _GEN_332; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_334 = 10'h139 == guard_index ? 64'h139 : _GEN_333; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_335 = 10'h13a == guard_index ? 64'h13a : _GEN_334; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_336 = 10'h13b == guard_index ? 64'h13b : _GEN_335; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_337 = 10'h13c == guard_index ? 64'h13c : _GEN_336; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_338 = 10'h13d == guard_index ? 64'h13d : _GEN_337; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_339 = 10'h13e == guard_index ? 64'h13e : _GEN_338; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_340 = 10'h13f == guard_index ? 64'h13f : _GEN_339; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_341 = 10'h140 == guard_index ? 64'h140 : _GEN_340; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_342 = 10'h141 == guard_index ? 64'h141 : _GEN_341; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_343 = 10'h142 == guard_index ? 64'h142 : _GEN_342; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_344 = 10'h143 == guard_index ? 64'h143 : _GEN_343; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_345 = 10'h144 == guard_index ? 64'h144 : _GEN_344; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_346 = 10'h145 == guard_index ? 64'h145 : _GEN_345; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_347 = 10'h146 == guard_index ? 64'h146 : _GEN_346; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_348 = 10'h147 == guard_index ? 64'h147 : _GEN_347; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_349 = 10'h148 == guard_index ? 64'h148 : _GEN_348; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_350 = 10'h149 == guard_index ? 64'h149 : _GEN_349; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_351 = 10'h14a == guard_index ? 64'h14a : _GEN_350; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_352 = 10'h14b == guard_index ? 64'h14b : _GEN_351; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_353 = 10'h14c == guard_index ? 64'h14c : _GEN_352; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_354 = 10'h14d == guard_index ? 64'h14d : _GEN_353; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_355 = 10'h14e == guard_index ? 64'h14e : _GEN_354; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_356 = 10'h14f == guard_index ? 64'h14f : _GEN_355; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_357 = 10'h150 == guard_index ? 64'h150 : _GEN_356; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_358 = 10'h151 == guard_index ? 64'h151 : _GEN_357; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_359 = 10'h152 == guard_index ? 64'h152 : _GEN_358; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_360 = 10'h153 == guard_index ? 64'h153 : _GEN_359; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_361 = 10'h154 == guard_index ? 64'h154 : _GEN_360; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_362 = 10'h155 == guard_index ? 64'h155 : _GEN_361; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_363 = 10'h156 == guard_index ? 64'h156 : _GEN_362; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_364 = 10'h157 == guard_index ? 64'h157 : _GEN_363; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_365 = 10'h158 == guard_index ? 64'h158 : _GEN_364; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_366 = 10'h159 == guard_index ? 64'h159 : _GEN_365; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_367 = 10'h15a == guard_index ? 64'h15a : _GEN_366; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_368 = 10'h15b == guard_index ? 64'h15b : _GEN_367; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_369 = 10'h15c == guard_index ? 64'h15c : _GEN_368; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_370 = 10'h15d == guard_index ? 64'h15d : _GEN_369; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_371 = 10'h15e == guard_index ? 64'h15e : _GEN_370; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_372 = 10'h15f == guard_index ? 64'h15f : _GEN_371; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_373 = 10'h160 == guard_index ? 64'h160 : _GEN_372; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_374 = 10'h161 == guard_index ? 64'h161 : _GEN_373; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_375 = 10'h162 == guard_index ? 64'h162 : _GEN_374; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_376 = 10'h163 == guard_index ? 64'h163 : _GEN_375; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_377 = 10'h164 == guard_index ? 64'h164 : _GEN_376; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_378 = 10'h165 == guard_index ? 64'h165 : _GEN_377; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_379 = 10'h166 == guard_index ? 64'h166 : _GEN_378; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_380 = 10'h167 == guard_index ? 64'h167 : _GEN_379; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_381 = 10'h168 == guard_index ? 64'h168 : _GEN_380; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_382 = 10'h169 == guard_index ? 64'h169 : _GEN_381; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_383 = 10'h16a == guard_index ? 64'h16a : _GEN_382; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_384 = 10'h16b == guard_index ? 64'h16b : _GEN_383; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_385 = 10'h16c == guard_index ? 64'h16c : _GEN_384; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_386 = 10'h16d == guard_index ? 64'h16d : _GEN_385; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_387 = 10'h16e == guard_index ? 64'h16e : _GEN_386; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_388 = 10'h16f == guard_index ? 64'h16f : _GEN_387; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_389 = 10'h170 == guard_index ? 64'h170 : _GEN_388; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_390 = 10'h171 == guard_index ? 64'h171 : _GEN_389; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_391 = 10'h172 == guard_index ? 64'h172 : _GEN_390; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_392 = 10'h173 == guard_index ? 64'h173 : _GEN_391; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_393 = 10'h174 == guard_index ? 64'h174 : _GEN_392; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_394 = 10'h175 == guard_index ? 64'h175 : _GEN_393; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_395 = 10'h176 == guard_index ? 64'h176 : _GEN_394; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_396 = 10'h177 == guard_index ? 64'h177 : _GEN_395; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_397 = 10'h178 == guard_index ? 64'h178 : _GEN_396; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_398 = 10'h179 == guard_index ? 64'h179 : _GEN_397; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_399 = 10'h17a == guard_index ? 64'h17a : _GEN_398; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_400 = 10'h17b == guard_index ? 64'h17b : _GEN_399; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_401 = 10'h17c == guard_index ? 64'h17c : _GEN_400; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_402 = 10'h17d == guard_index ? 64'h17d : _GEN_401; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_403 = 10'h17e == guard_index ? 64'h17e : _GEN_402; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_404 = 10'h17f == guard_index ? 64'h17f : _GEN_403; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_405 = 10'h180 == guard_index ? 64'h180 : _GEN_404; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_406 = 10'h181 == guard_index ? 64'h181 : _GEN_405; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_407 = 10'h182 == guard_index ? 64'h182 : _GEN_406; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_408 = 10'h183 == guard_index ? 64'h183 : _GEN_407; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_409 = 10'h184 == guard_index ? 64'h184 : _GEN_408; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_410 = 10'h185 == guard_index ? 64'h185 : _GEN_409; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_411 = 10'h186 == guard_index ? 64'h186 : _GEN_410; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_412 = 10'h187 == guard_index ? 64'h187 : _GEN_411; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_413 = 10'h188 == guard_index ? 64'h188 : _GEN_412; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_414 = 10'h189 == guard_index ? 64'h189 : _GEN_413; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_415 = 10'h18a == guard_index ? 64'h18a : _GEN_414; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_416 = 10'h18b == guard_index ? 64'h18b : _GEN_415; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_417 = 10'h18c == guard_index ? 64'h18c : _GEN_416; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_418 = 10'h18d == guard_index ? 64'h18d : _GEN_417; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_419 = 10'h18e == guard_index ? 64'h18e : _GEN_418; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_420 = 10'h18f == guard_index ? 64'h18f : _GEN_419; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_421 = 10'h190 == guard_index ? 64'h190 : _GEN_420; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_422 = 10'h191 == guard_index ? 64'h191 : _GEN_421; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_423 = 10'h192 == guard_index ? 64'h192 : _GEN_422; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_424 = 10'h193 == guard_index ? 64'h193 : _GEN_423; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_425 = 10'h194 == guard_index ? 64'h194 : _GEN_424; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_426 = 10'h195 == guard_index ? 64'h195 : _GEN_425; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_427 = 10'h196 == guard_index ? 64'h196 : _GEN_426; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_428 = 10'h197 == guard_index ? 64'h197 : _GEN_427; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_429 = 10'h198 == guard_index ? 64'h198 : _GEN_428; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_430 = 10'h199 == guard_index ? 64'h199 : _GEN_429; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_431 = 10'h19a == guard_index ? 64'h19a : _GEN_430; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_432 = 10'h19b == guard_index ? 64'h19b : _GEN_431; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_433 = 10'h19c == guard_index ? 64'h19c : _GEN_432; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_434 = 10'h19d == guard_index ? 64'h19d : _GEN_433; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_435 = 10'h19e == guard_index ? 64'h19e : _GEN_434; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_436 = 10'h19f == guard_index ? 64'h19f : _GEN_435; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_437 = 10'h1a0 == guard_index ? 64'h1a0 : _GEN_436; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_438 = 10'h1a1 == guard_index ? 64'h1a1 : _GEN_437; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_439 = 10'h1a2 == guard_index ? 64'h1a2 : _GEN_438; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_440 = 10'h1a3 == guard_index ? 64'h1a3 : _GEN_439; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_441 = 10'h1a4 == guard_index ? 64'h1a4 : _GEN_440; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_442 = 10'h1a5 == guard_index ? 64'h1a5 : _GEN_441; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_443 = 10'h1a6 == guard_index ? 64'h1a6 : _GEN_442; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_444 = 10'h1a7 == guard_index ? 64'h1a7 : _GEN_443; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_445 = 10'h1a8 == guard_index ? 64'h1a8 : _GEN_444; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_446 = 10'h1a9 == guard_index ? 64'h1a9 : _GEN_445; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_447 = 10'h1aa == guard_index ? 64'h1aa : _GEN_446; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_448 = 10'h1ab == guard_index ? 64'h1ab : _GEN_447; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_449 = 10'h1ac == guard_index ? 64'h1ac : _GEN_448; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_450 = 10'h1ad == guard_index ? 64'h1ad : _GEN_449; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_451 = 10'h1ae == guard_index ? 64'h1ae : _GEN_450; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_452 = 10'h1af == guard_index ? 64'h1af : _GEN_451; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_453 = 10'h1b0 == guard_index ? 64'h1b0 : _GEN_452; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_454 = 10'h1b1 == guard_index ? 64'h1b1 : _GEN_453; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_455 = 10'h1b2 == guard_index ? 64'h1b2 : _GEN_454; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_456 = 10'h1b3 == guard_index ? 64'h1b3 : _GEN_455; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_457 = 10'h1b4 == guard_index ? 64'h1b4 : _GEN_456; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_458 = 10'h1b5 == guard_index ? 64'h1b5 : _GEN_457; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_459 = 10'h1b6 == guard_index ? 64'h1b6 : _GEN_458; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_460 = 10'h1b7 == guard_index ? 64'h1b7 : _GEN_459; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_461 = 10'h1b8 == guard_index ? 64'h1b8 : _GEN_460; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_462 = 10'h1b9 == guard_index ? 64'h1b9 : _GEN_461; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_463 = 10'h1ba == guard_index ? 64'h1ba : _GEN_462; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_464 = 10'h1bb == guard_index ? 64'h1bb : _GEN_463; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_465 = 10'h1bc == guard_index ? 64'h1bc : _GEN_464; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_466 = 10'h1bd == guard_index ? 64'h1bd : _GEN_465; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_467 = 10'h1be == guard_index ? 64'h1be : _GEN_466; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_468 = 10'h1bf == guard_index ? 64'h1bf : _GEN_467; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_469 = 10'h1c0 == guard_index ? 64'h1c0 : _GEN_468; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_470 = 10'h1c1 == guard_index ? 64'h1c1 : _GEN_469; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_471 = 10'h1c2 == guard_index ? 64'h1c2 : _GEN_470; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_472 = 10'h1c3 == guard_index ? 64'h1c3 : _GEN_471; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_473 = 10'h1c4 == guard_index ? 64'h1c4 : _GEN_472; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_474 = 10'h1c5 == guard_index ? 64'h1c5 : _GEN_473; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_475 = 10'h1c6 == guard_index ? 64'h1c6 : _GEN_474; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_476 = 10'h1c7 == guard_index ? 64'h1c7 : _GEN_475; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_477 = 10'h1c8 == guard_index ? 64'h1c8 : _GEN_476; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_478 = 10'h1c9 == guard_index ? 64'h1c9 : _GEN_477; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_479 = 10'h1ca == guard_index ? 64'h1ca : _GEN_478; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_480 = 10'h1cb == guard_index ? 64'h1cb : _GEN_479; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_481 = 10'h1cc == guard_index ? 64'h1cc : _GEN_480; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_482 = 10'h1cd == guard_index ? 64'h1cd : _GEN_481; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_483 = 10'h1ce == guard_index ? 64'h1ce : _GEN_482; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_484 = 10'h1cf == guard_index ? 64'h1cf : _GEN_483; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_485 = 10'h1d0 == guard_index ? 64'h1d0 : _GEN_484; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_486 = 10'h1d1 == guard_index ? 64'h1d1 : _GEN_485; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_487 = 10'h1d2 == guard_index ? 64'h1d2 : _GEN_486; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_488 = 10'h1d3 == guard_index ? 64'h1d3 : _GEN_487; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_489 = 10'h1d4 == guard_index ? 64'h1d4 : _GEN_488; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_490 = 10'h1d5 == guard_index ? 64'h1d5 : _GEN_489; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_491 = 10'h1d6 == guard_index ? 64'h1d6 : _GEN_490; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_492 = 10'h1d7 == guard_index ? 64'h1d7 : _GEN_491; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_493 = 10'h1d8 == guard_index ? 64'h1d8 : _GEN_492; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_494 = 10'h1d9 == guard_index ? 64'h1d9 : _GEN_493; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_495 = 10'h1da == guard_index ? 64'h1da : _GEN_494; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_496 = 10'h1db == guard_index ? 64'h1db : _GEN_495; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_497 = 10'h1dc == guard_index ? 64'h1dc : _GEN_496; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_498 = 10'h1dd == guard_index ? 64'h1dd : _GEN_497; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_499 = 10'h1de == guard_index ? 64'h1de : _GEN_498; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_500 = 10'h1df == guard_index ? 64'h1df : _GEN_499; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_501 = 10'h1e0 == guard_index ? 64'h1e0 : _GEN_500; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_502 = 10'h1e1 == guard_index ? 64'h1e1 : _GEN_501; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_503 = 10'h1e2 == guard_index ? 64'h1e2 : _GEN_502; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_504 = 10'h1e3 == guard_index ? 64'h1e3 : _GEN_503; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_505 = 10'h1e4 == guard_index ? 64'h1e4 : _GEN_504; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_506 = 10'h1e5 == guard_index ? 64'h1e5 : _GEN_505; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_507 = 10'h1e6 == guard_index ? 64'h1e6 : _GEN_506; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_508 = 10'h1e7 == guard_index ? 64'h1e7 : _GEN_507; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_509 = 10'h1e8 == guard_index ? 64'h1e8 : _GEN_508; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_510 = 10'h1e9 == guard_index ? 64'h1e9 : _GEN_509; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_511 = 10'h1ea == guard_index ? 64'h1ea : _GEN_510; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_512 = 10'h1eb == guard_index ? 64'h1eb : _GEN_511; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_513 = 10'h1ec == guard_index ? 64'h1ec : _GEN_512; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_514 = 10'h1ed == guard_index ? 64'h1ed : _GEN_513; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_515 = 10'h1ee == guard_index ? 64'h1ee : _GEN_514; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_516 = 10'h1ef == guard_index ? 64'h1ef : _GEN_515; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_517 = 10'h1f0 == guard_index ? 64'h1f0 : _GEN_516; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_518 = 10'h1f1 == guard_index ? 64'h1f1 : _GEN_517; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_519 = 10'h1f2 == guard_index ? 64'h1f2 : _GEN_518; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_520 = 10'h1f3 == guard_index ? 64'h1f3 : _GEN_519; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_521 = 10'h1f4 == guard_index ? 64'h1f4 : _GEN_520; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_522 = 10'h1f5 == guard_index ? 64'h1f5 : _GEN_521; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_523 = 10'h1f6 == guard_index ? 64'h1f6 : _GEN_522; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_524 = 10'h1f7 == guard_index ? 64'h1f7 : _GEN_523; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_525 = 10'h1f8 == guard_index ? 64'h1f8 : _GEN_524; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_526 = 10'h1f9 == guard_index ? 64'h1f9 : _GEN_525; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_527 = 10'h1fa == guard_index ? 64'h1fa : _GEN_526; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_528 = 10'h1fb == guard_index ? 64'h1fb : _GEN_527; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_529 = 10'h1fc == guard_index ? 64'h1fc : _GEN_528; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_530 = 10'h1fd == guard_index ? 64'h1fd : _GEN_529; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_531 = 10'h1fe == guard_index ? 64'h1fe : _GEN_530; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_532 = 10'h1ff == guard_index ? 64'h1ff : _GEN_531; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_533 = 10'h200 == guard_index ? 64'h200 : _GEN_532; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_534 = 10'h201 == guard_index ? 64'h201 : _GEN_533; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_535 = 10'h202 == guard_index ? 64'h202 : _GEN_534; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_536 = 10'h203 == guard_index ? 64'h203 : _GEN_535; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_537 = 10'h204 == guard_index ? 64'h204 : _GEN_536; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_538 = 10'h205 == guard_index ? 64'h205 : _GEN_537; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_539 = 10'h206 == guard_index ? 64'h206 : _GEN_538; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_540 = 10'h207 == guard_index ? 64'h207 : _GEN_539; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_541 = 10'h208 == guard_index ? 64'h208 : _GEN_540; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_542 = 10'h209 == guard_index ? 64'h209 : _GEN_541; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_543 = 10'h20a == guard_index ? 64'h20a : _GEN_542; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_544 = 10'h20b == guard_index ? 64'h20b : _GEN_543; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_545 = 10'h20c == guard_index ? 64'h20c : _GEN_544; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_546 = 10'h20d == guard_index ? 64'h20d : _GEN_545; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_547 = 10'h20e == guard_index ? 64'h20e : _GEN_546; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_548 = 10'h20f == guard_index ? 64'h20f : _GEN_547; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_549 = 10'h210 == guard_index ? 64'h210 : _GEN_548; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_550 = 10'h211 == guard_index ? 64'h211 : _GEN_549; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_551 = 10'h212 == guard_index ? 64'h212 : _GEN_550; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_552 = 10'h213 == guard_index ? 64'h213 : _GEN_551; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_553 = 10'h214 == guard_index ? 64'h214 : _GEN_552; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_554 = 10'h215 == guard_index ? 64'h215 : _GEN_553; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_555 = 10'h216 == guard_index ? 64'h216 : _GEN_554; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_556 = 10'h217 == guard_index ? 64'h217 : _GEN_555; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_557 = 10'h218 == guard_index ? 64'h218 : _GEN_556; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_558 = 10'h219 == guard_index ? 64'h219 : _GEN_557; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_559 = 10'h21a == guard_index ? 64'h21a : _GEN_558; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_560 = 10'h21b == guard_index ? 64'h21b : _GEN_559; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_561 = 10'h21c == guard_index ? 64'h21c : _GEN_560; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_562 = 10'h21d == guard_index ? 64'h21d : _GEN_561; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_563 = 10'h21e == guard_index ? 64'h21e : _GEN_562; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_564 = 10'h21f == guard_index ? 64'h21f : _GEN_563; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_565 = 10'h220 == guard_index ? 64'h220 : _GEN_564; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_566 = 10'h221 == guard_index ? 64'h221 : _GEN_565; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_567 = 10'h222 == guard_index ? 64'h222 : _GEN_566; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_568 = 10'h223 == guard_index ? 64'h223 : _GEN_567; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_569 = 10'h224 == guard_index ? 64'h224 : _GEN_568; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_570 = 10'h225 == guard_index ? 64'h225 : _GEN_569; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_571 = 10'h226 == guard_index ? 64'h226 : _GEN_570; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_572 = 10'h227 == guard_index ? 64'h227 : _GEN_571; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_573 = 10'h228 == guard_index ? 64'h228 : _GEN_572; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_574 = 10'h229 == guard_index ? 64'h229 : _GEN_573; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_575 = 10'h22a == guard_index ? 64'h22a : _GEN_574; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_576 = 10'h22b == guard_index ? 64'h22b : _GEN_575; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_577 = 10'h22c == guard_index ? 64'h22c : _GEN_576; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_578 = 10'h22d == guard_index ? 64'h22d : _GEN_577; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_579 = 10'h22e == guard_index ? 64'h22e : _GEN_578; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_580 = 10'h22f == guard_index ? 64'h22f : _GEN_579; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_581 = 10'h230 == guard_index ? 64'h230 : _GEN_580; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_582 = 10'h231 == guard_index ? 64'h231 : _GEN_581; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_583 = 10'h232 == guard_index ? 64'h232 : _GEN_582; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_584 = 10'h233 == guard_index ? 64'h233 : _GEN_583; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_585 = 10'h234 == guard_index ? 64'h234 : _GEN_584; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_586 = 10'h235 == guard_index ? 64'h235 : _GEN_585; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_587 = 10'h236 == guard_index ? 64'h236 : _GEN_586; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_588 = 10'h237 == guard_index ? 64'h237 : _GEN_587; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_589 = 10'h238 == guard_index ? 64'h238 : _GEN_588; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_590 = 10'h239 == guard_index ? 64'h239 : _GEN_589; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_591 = 10'h23a == guard_index ? 64'h23a : _GEN_590; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_592 = 10'h23b == guard_index ? 64'h23b : _GEN_591; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_593 = 10'h23c == guard_index ? 64'h23c : _GEN_592; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_594 = 10'h23d == guard_index ? 64'h23d : _GEN_593; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_595 = 10'h23e == guard_index ? 64'h23e : _GEN_594; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_596 = 10'h23f == guard_index ? 64'h23f : _GEN_595; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_597 = 10'h240 == guard_index ? 64'h240 : _GEN_596; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_598 = 10'h241 == guard_index ? 64'h241 : _GEN_597; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_599 = 10'h242 == guard_index ? 64'h242 : _GEN_598; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_600 = 10'h243 == guard_index ? 64'h243 : _GEN_599; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_601 = 10'h244 == guard_index ? 64'h244 : _GEN_600; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_602 = 10'h245 == guard_index ? 64'h245 : _GEN_601; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_603 = 10'h246 == guard_index ? 64'h246 : _GEN_602; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_604 = 10'h247 == guard_index ? 64'h247 : _GEN_603; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_605 = 10'h248 == guard_index ? 64'h248 : _GEN_604; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_606 = 10'h249 == guard_index ? 64'h249 : _GEN_605; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_607 = 10'h24a == guard_index ? 64'h24a : _GEN_606; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_608 = 10'h24b == guard_index ? 64'h24b : _GEN_607; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_609 = 10'h24c == guard_index ? 64'h24c : _GEN_608; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_610 = 10'h24d == guard_index ? 64'h24d : _GEN_609; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_611 = 10'h24e == guard_index ? 64'h24e : _GEN_610; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_612 = 10'h24f == guard_index ? 64'h24f : _GEN_611; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_613 = 10'h250 == guard_index ? 64'h250 : _GEN_612; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_614 = 10'h251 == guard_index ? 64'h251 : _GEN_613; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_615 = 10'h252 == guard_index ? 64'h252 : _GEN_614; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_616 = 10'h253 == guard_index ? 64'h253 : _GEN_615; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_617 = 10'h254 == guard_index ? 64'h254 : _GEN_616; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_618 = 10'h255 == guard_index ? 64'h255 : _GEN_617; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_619 = 10'h256 == guard_index ? 64'h256 : _GEN_618; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_620 = 10'h257 == guard_index ? 64'h257 : _GEN_619; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_621 = 10'h258 == guard_index ? 64'h258 : _GEN_620; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_622 = 10'h259 == guard_index ? 64'h259 : _GEN_621; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_623 = 10'h25a == guard_index ? 64'h25a : _GEN_622; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_624 = 10'h25b == guard_index ? 64'h25b : _GEN_623; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_625 = 10'h25c == guard_index ? 64'h25c : _GEN_624; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_626 = 10'h25d == guard_index ? 64'h25d : _GEN_625; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_627 = 10'h25e == guard_index ? 64'h25e : _GEN_626; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_628 = 10'h25f == guard_index ? 64'h25f : _GEN_627; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_629 = 10'h260 == guard_index ? 64'h260 : _GEN_628; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_630 = 10'h261 == guard_index ? 64'h261 : _GEN_629; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_631 = 10'h262 == guard_index ? 64'h262 : _GEN_630; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_632 = 10'h263 == guard_index ? 64'h263 : _GEN_631; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_633 = 10'h264 == guard_index ? 64'h264 : _GEN_632; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_634 = 10'h265 == guard_index ? 64'h265 : _GEN_633; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_635 = 10'h266 == guard_index ? 64'h266 : _GEN_634; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_636 = 10'h267 == guard_index ? 64'h267 : _GEN_635; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_637 = 10'h268 == guard_index ? 64'h268 : _GEN_636; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_638 = 10'h269 == guard_index ? 64'h269 : _GEN_637; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_639 = 10'h26a == guard_index ? 64'h26a : _GEN_638; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_640 = 10'h26b == guard_index ? 64'h26b : _GEN_639; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_641 = 10'h26c == guard_index ? 64'h26c : _GEN_640; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_642 = 10'h26d == guard_index ? 64'h26d : _GEN_641; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_643 = 10'h26e == guard_index ? 64'h26e : _GEN_642; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_644 = 10'h26f == guard_index ? 64'h26f : _GEN_643; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_645 = 10'h270 == guard_index ? 64'h270 : _GEN_644; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_646 = 10'h271 == guard_index ? 64'h271 : _GEN_645; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_647 = 10'h272 == guard_index ? 64'h272 : _GEN_646; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_648 = 10'h273 == guard_index ? 64'h273 : _GEN_647; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_649 = 10'h274 == guard_index ? 64'h274 : _GEN_648; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_650 = 10'h275 == guard_index ? 64'h275 : _GEN_649; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_651 = 10'h276 == guard_index ? 64'h276 : _GEN_650; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_652 = 10'h277 == guard_index ? 64'h277 : _GEN_651; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_653 = 10'h278 == guard_index ? 64'h278 : _GEN_652; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_654 = 10'h279 == guard_index ? 64'h279 : _GEN_653; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_655 = 10'h27a == guard_index ? 64'h27a : _GEN_654; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_656 = 10'h27b == guard_index ? 64'h27b : _GEN_655; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_657 = 10'h27c == guard_index ? 64'h27c : _GEN_656; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_658 = 10'h27d == guard_index ? 64'h27d : _GEN_657; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_659 = 10'h27e == guard_index ? 64'h27e : _GEN_658; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_660 = 10'h27f == guard_index ? 64'h27f : _GEN_659; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_661 = 10'h280 == guard_index ? 64'h280 : _GEN_660; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_662 = 10'h281 == guard_index ? 64'h281 : _GEN_661; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_663 = 10'h282 == guard_index ? 64'h282 : _GEN_662; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_664 = 10'h283 == guard_index ? 64'h283 : _GEN_663; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_665 = 10'h284 == guard_index ? 64'h284 : _GEN_664; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_666 = 10'h285 == guard_index ? 64'h285 : _GEN_665; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_667 = 10'h286 == guard_index ? 64'h286 : _GEN_666; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_668 = 10'h287 == guard_index ? 64'h287 : _GEN_667; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_669 = 10'h288 == guard_index ? 64'h288 : _GEN_668; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_670 = 10'h289 == guard_index ? 64'h289 : _GEN_669; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_671 = 10'h28a == guard_index ? 64'h28a : _GEN_670; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_672 = 10'h28b == guard_index ? 64'h28b : _GEN_671; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_673 = 10'h28c == guard_index ? 64'h28c : _GEN_672; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_674 = 10'h28d == guard_index ? 64'h28d : _GEN_673; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_675 = 10'h28e == guard_index ? 64'h28e : _GEN_674; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_676 = 10'h28f == guard_index ? 64'h28f : _GEN_675; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_677 = 10'h290 == guard_index ? 64'h290 : _GEN_676; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_678 = 10'h291 == guard_index ? 64'h291 : _GEN_677; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_679 = 10'h292 == guard_index ? 64'h292 : _GEN_678; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_680 = 10'h293 == guard_index ? 64'h293 : _GEN_679; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_681 = 10'h294 == guard_index ? 64'h294 : _GEN_680; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_682 = 10'h295 == guard_index ? 64'h295 : _GEN_681; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_683 = 10'h296 == guard_index ? 64'h296 : _GEN_682; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_684 = 10'h297 == guard_index ? 64'h297 : _GEN_683; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_685 = 10'h298 == guard_index ? 64'h298 : _GEN_684; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_686 = 10'h299 == guard_index ? 64'h299 : _GEN_685; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_687 = 10'h29a == guard_index ? 64'h29a : _GEN_686; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_688 = 10'h29b == guard_index ? 64'h29b : _GEN_687; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_689 = 10'h29c == guard_index ? 64'h29c : _GEN_688; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_690 = 10'h29d == guard_index ? 64'h29d : _GEN_689; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_691 = 10'h29e == guard_index ? 64'h29e : _GEN_690; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_692 = 10'h29f == guard_index ? 64'h29f : _GEN_691; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_693 = 10'h2a0 == guard_index ? 64'h2a0 : _GEN_692; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_694 = 10'h2a1 == guard_index ? 64'h2a1 : _GEN_693; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_695 = 10'h2a2 == guard_index ? 64'h2a2 : _GEN_694; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_696 = 10'h2a3 == guard_index ? 64'h2a3 : _GEN_695; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_697 = 10'h2a4 == guard_index ? 64'h2a4 : _GEN_696; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_698 = 10'h2a5 == guard_index ? 64'h2a5 : _GEN_697; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_699 = 10'h2a6 == guard_index ? 64'h2a6 : _GEN_698; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_700 = 10'h2a7 == guard_index ? 64'h2a7 : _GEN_699; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_701 = 10'h2a8 == guard_index ? 64'h2a8 : _GEN_700; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_702 = 10'h2a9 == guard_index ? 64'h2a9 : _GEN_701; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_703 = 10'h2aa == guard_index ? 64'h2aa : _GEN_702; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_704 = 10'h2ab == guard_index ? 64'h2ab : _GEN_703; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_705 = 10'h2ac == guard_index ? 64'h2ac : _GEN_704; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_706 = 10'h2ad == guard_index ? 64'h2ad : _GEN_705; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_707 = 10'h2ae == guard_index ? 64'h2ae : _GEN_706; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_708 = 10'h2af == guard_index ? 64'h2af : _GEN_707; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_709 = 10'h2b0 == guard_index ? 64'h2b0 : _GEN_708; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_710 = 10'h2b1 == guard_index ? 64'h2b1 : _GEN_709; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_711 = 10'h2b2 == guard_index ? 64'h2b2 : _GEN_710; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_712 = 10'h2b3 == guard_index ? 64'h2b3 : _GEN_711; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_713 = 10'h2b4 == guard_index ? 64'h2b4 : _GEN_712; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_714 = 10'h2b5 == guard_index ? 64'h2b5 : _GEN_713; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_715 = 10'h2b6 == guard_index ? 64'h2b6 : _GEN_714; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_716 = 10'h2b7 == guard_index ? 64'h2b7 : _GEN_715; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_717 = 10'h2b8 == guard_index ? 64'h2b8 : _GEN_716; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_718 = 10'h2b9 == guard_index ? 64'h2b9 : _GEN_717; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_719 = 10'h2ba == guard_index ? 64'h2ba : _GEN_718; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_720 = 10'h2bb == guard_index ? 64'h2bb : _GEN_719; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_721 = 10'h2bc == guard_index ? 64'h2bc : _GEN_720; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_722 = 10'h2bd == guard_index ? 64'h2bd : _GEN_721; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_723 = 10'h2be == guard_index ? 64'h2be : _GEN_722; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_724 = 10'h2bf == guard_index ? 64'h2bf : _GEN_723; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_725 = 10'h2c0 == guard_index ? 64'h2c0 : _GEN_724; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_726 = 10'h2c1 == guard_index ? 64'h2c1 : _GEN_725; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_727 = 10'h2c2 == guard_index ? 64'h2c2 : _GEN_726; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_728 = 10'h2c3 == guard_index ? 64'h2c3 : _GEN_727; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_729 = 10'h2c4 == guard_index ? 64'h2c4 : _GEN_728; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_730 = 10'h2c5 == guard_index ? 64'h2c5 : _GEN_729; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_731 = 10'h2c6 == guard_index ? 64'h2c6 : _GEN_730; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_732 = 10'h2c7 == guard_index ? 64'h2c7 : _GEN_731; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_733 = 10'h2c8 == guard_index ? 64'h2c8 : _GEN_732; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_734 = 10'h2c9 == guard_index ? 64'h2c9 : _GEN_733; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_735 = 10'h2ca == guard_index ? 64'h2ca : _GEN_734; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_736 = 10'h2cb == guard_index ? 64'h2cb : _GEN_735; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_737 = 10'h2cc == guard_index ? 64'h2cc : _GEN_736; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_738 = 10'h2cd == guard_index ? 64'h2cd : _GEN_737; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_739 = 10'h2ce == guard_index ? 64'h2ce : _GEN_738; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_740 = 10'h2cf == guard_index ? 64'h2cf : _GEN_739; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_741 = 10'h2d0 == guard_index ? 64'h2d0 : _GEN_740; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_742 = 10'h2d1 == guard_index ? 64'h2d1 : _GEN_741; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_743 = 10'h2d2 == guard_index ? 64'h2d2 : _GEN_742; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_744 = 10'h2d3 == guard_index ? 64'h2d3 : _GEN_743; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_745 = 10'h2d4 == guard_index ? 64'h2d4 : _GEN_744; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_746 = 10'h2d5 == guard_index ? 64'h2d5 : _GEN_745; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_747 = 10'h2d6 == guard_index ? 64'h2d6 : _GEN_746; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_748 = 10'h2d7 == guard_index ? 64'h2d7 : _GEN_747; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_749 = 10'h2d8 == guard_index ? 64'h2d8 : _GEN_748; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_750 = 10'h2d9 == guard_index ? 64'h2d9 : _GEN_749; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_751 = 10'h2da == guard_index ? 64'h2da : _GEN_750; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_752 = 10'h2db == guard_index ? 64'h2db : _GEN_751; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_753 = 10'h2dc == guard_index ? 64'h2dc : _GEN_752; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_754 = 10'h2dd == guard_index ? 64'h2dd : _GEN_753; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_755 = 10'h2de == guard_index ? 64'h2de : _GEN_754; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_756 = 10'h2df == guard_index ? 64'h2df : _GEN_755; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_757 = 10'h2e0 == guard_index ? 64'h2e0 : _GEN_756; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_758 = 10'h2e1 == guard_index ? 64'h2e1 : _GEN_757; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_759 = 10'h2e2 == guard_index ? 64'h2e2 : _GEN_758; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_760 = 10'h2e3 == guard_index ? 64'h2e3 : _GEN_759; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_761 = 10'h2e4 == guard_index ? 64'h2e4 : _GEN_760; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_762 = 10'h2e5 == guard_index ? 64'h2e5 : _GEN_761; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_763 = 10'h2e6 == guard_index ? 64'h2e6 : _GEN_762; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_764 = 10'h2e7 == guard_index ? 64'h2e7 : _GEN_763; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_765 = 10'h2e8 == guard_index ? 64'h2e8 : _GEN_764; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_766 = 10'h2e9 == guard_index ? 64'h2e9 : _GEN_765; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_767 = 10'h2ea == guard_index ? 64'h2ea : _GEN_766; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_768 = 10'h2eb == guard_index ? 64'h2eb : _GEN_767; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_769 = 10'h2ec == guard_index ? 64'h2ec : _GEN_768; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_770 = 10'h2ed == guard_index ? 64'h2ed : _GEN_769; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_771 = 10'h2ee == guard_index ? 64'h2ee : _GEN_770; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_772 = 10'h2ef == guard_index ? 64'h2ef : _GEN_771; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_773 = 10'h2f0 == guard_index ? 64'h2f0 : _GEN_772; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_774 = 10'h2f1 == guard_index ? 64'h2f1 : _GEN_773; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_775 = 10'h2f2 == guard_index ? 64'h2f2 : _GEN_774; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_776 = 10'h2f3 == guard_index ? 64'h2f3 : _GEN_775; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_777 = 10'h2f4 == guard_index ? 64'h2f4 : _GEN_776; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_778 = 10'h2f5 == guard_index ? 64'h2f5 : _GEN_777; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_779 = 10'h2f6 == guard_index ? 64'h2f6 : _GEN_778; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_780 = 10'h2f7 == guard_index ? 64'h2f7 : _GEN_779; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_781 = 10'h2f8 == guard_index ? 64'h2f8 : _GEN_780; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_782 = 10'h2f9 == guard_index ? 64'h2f9 : _GEN_781; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_783 = 10'h2fa == guard_index ? 64'h2fa : _GEN_782; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_784 = 10'h2fb == guard_index ? 64'h2fb : _GEN_783; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_785 = 10'h2fc == guard_index ? 64'h2fc : _GEN_784; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_786 = 10'h2fd == guard_index ? 64'h2fd : _GEN_785; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_787 = 10'h2fe == guard_index ? 64'h2fe : _GEN_786; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_788 = 10'h2ff == guard_index ? 64'h2ff : _GEN_787; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_789 = 10'h300 == guard_index ? 64'h300 : _GEN_788; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_790 = 10'h301 == guard_index ? 64'h301 : _GEN_789; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_791 = 10'h302 == guard_index ? 64'h302 : _GEN_790; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_792 = 10'h303 == guard_index ? 64'h303 : _GEN_791; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_793 = 10'h304 == guard_index ? 64'h304 : _GEN_792; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_794 = 10'h305 == guard_index ? 64'h305 : _GEN_793; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_795 = 10'h306 == guard_index ? 64'h306 : _GEN_794; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_796 = 10'h307 == guard_index ? 64'h307 : _GEN_795; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_797 = 10'h308 == guard_index ? 64'h308 : _GEN_796; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_798 = 10'h309 == guard_index ? 64'h309 : _GEN_797; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_799 = 10'h30a == guard_index ? 64'h30a : _GEN_798; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_800 = 10'h30b == guard_index ? 64'h30b : _GEN_799; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_801 = 10'h30c == guard_index ? 64'h30c : _GEN_800; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_802 = 10'h30d == guard_index ? 64'h30d : _GEN_801; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_803 = 10'h30e == guard_index ? 64'h30e : _GEN_802; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_804 = 10'h30f == guard_index ? 64'h30f : _GEN_803; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_805 = 10'h310 == guard_index ? 64'h310 : _GEN_804; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_806 = 10'h311 == guard_index ? 64'h311 : _GEN_805; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_807 = 10'h312 == guard_index ? 64'h312 : _GEN_806; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_808 = 10'h313 == guard_index ? 64'h313 : _GEN_807; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_809 = 10'h314 == guard_index ? 64'h314 : _GEN_808; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_810 = 10'h315 == guard_index ? 64'h315 : _GEN_809; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_811 = 10'h316 == guard_index ? 64'h316 : _GEN_810; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_812 = 10'h317 == guard_index ? 64'h317 : _GEN_811; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_813 = 10'h318 == guard_index ? 64'h318 : _GEN_812; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_814 = 10'h319 == guard_index ? 64'h319 : _GEN_813; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_815 = 10'h31a == guard_index ? 64'h31a : _GEN_814; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_816 = 10'h31b == guard_index ? 64'h31b : _GEN_815; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_817 = 10'h31c == guard_index ? 64'h31c : _GEN_816; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_818 = 10'h31d == guard_index ? 64'h31d : _GEN_817; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_819 = 10'h31e == guard_index ? 64'h31e : _GEN_818; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_820 = 10'h31f == guard_index ? 64'h31f : _GEN_819; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_821 = 10'h320 == guard_index ? 64'h320 : _GEN_820; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_822 = 10'h321 == guard_index ? 64'h321 : _GEN_821; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_823 = 10'h322 == guard_index ? 64'h322 : _GEN_822; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_824 = 10'h323 == guard_index ? 64'h323 : _GEN_823; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_825 = 10'h324 == guard_index ? 64'h324 : _GEN_824; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_826 = 10'h325 == guard_index ? 64'h325 : _GEN_825; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_827 = 10'h326 == guard_index ? 64'h326 : _GEN_826; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_828 = 10'h327 == guard_index ? 64'h327 : _GEN_827; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_829 = 10'h328 == guard_index ? 64'h328 : _GEN_828; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_830 = 10'h329 == guard_index ? 64'h329 : _GEN_829; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_831 = 10'h32a == guard_index ? 64'h32a : _GEN_830; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_832 = 10'h32b == guard_index ? 64'h32b : _GEN_831; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_833 = 10'h32c == guard_index ? 64'h32c : _GEN_832; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_834 = 10'h32d == guard_index ? 64'h32d : _GEN_833; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_835 = 10'h32e == guard_index ? 64'h32e : _GEN_834; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_836 = 10'h32f == guard_index ? 64'h32f : _GEN_835; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_837 = 10'h330 == guard_index ? 64'h330 : _GEN_836; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_838 = 10'h331 == guard_index ? 64'h331 : _GEN_837; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_839 = 10'h332 == guard_index ? 64'h332 : _GEN_838; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_840 = 10'h333 == guard_index ? 64'h333 : _GEN_839; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_841 = 10'h334 == guard_index ? 64'h334 : _GEN_840; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_842 = 10'h335 == guard_index ? 64'h335 : _GEN_841; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_843 = 10'h336 == guard_index ? 64'h336 : _GEN_842; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_844 = 10'h337 == guard_index ? 64'h337 : _GEN_843; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_845 = 10'h338 == guard_index ? 64'h338 : _GEN_844; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_846 = 10'h339 == guard_index ? 64'h339 : _GEN_845; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_847 = 10'h33a == guard_index ? 64'h33a : _GEN_846; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_848 = 10'h33b == guard_index ? 64'h33b : _GEN_847; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_849 = 10'h33c == guard_index ? 64'h33c : _GEN_848; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_850 = 10'h33d == guard_index ? 64'h33d : _GEN_849; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_851 = 10'h33e == guard_index ? 64'h33e : _GEN_850; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_852 = 10'h33f == guard_index ? 64'h33f : _GEN_851; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_853 = 10'h340 == guard_index ? 64'h340 : _GEN_852; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_854 = 10'h341 == guard_index ? 64'h341 : _GEN_853; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_855 = 10'h342 == guard_index ? 64'h342 : _GEN_854; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_856 = 10'h343 == guard_index ? 64'h343 : _GEN_855; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_857 = 10'h344 == guard_index ? 64'h344 : _GEN_856; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_858 = 10'h345 == guard_index ? 64'h345 : _GEN_857; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_859 = 10'h346 == guard_index ? 64'h346 : _GEN_858; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_860 = 10'h347 == guard_index ? 64'h347 : _GEN_859; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_861 = 10'h348 == guard_index ? 64'h348 : _GEN_860; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_862 = 10'h349 == guard_index ? 64'h349 : _GEN_861; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_863 = 10'h34a == guard_index ? 64'h34a : _GEN_862; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_864 = 10'h34b == guard_index ? 64'h34b : _GEN_863; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_865 = 10'h34c == guard_index ? 64'h34c : _GEN_864; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_866 = 10'h34d == guard_index ? 64'h34d : _GEN_865; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_867 = 10'h34e == guard_index ? 64'h34e : _GEN_866; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_868 = 10'h34f == guard_index ? 64'h34f : _GEN_867; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_869 = 10'h350 == guard_index ? 64'h350 : _GEN_868; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_870 = 10'h351 == guard_index ? 64'h351 : _GEN_869; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_871 = 10'h352 == guard_index ? 64'h352 : _GEN_870; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_872 = 10'h353 == guard_index ? 64'h353 : _GEN_871; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_873 = 10'h354 == guard_index ? 64'h354 : _GEN_872; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_874 = 10'h355 == guard_index ? 64'h355 : _GEN_873; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_875 = 10'h356 == guard_index ? 64'h356 : _GEN_874; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_876 = 10'h357 == guard_index ? 64'h357 : _GEN_875; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_877 = 10'h358 == guard_index ? 64'h358 : _GEN_876; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_878 = 10'h359 == guard_index ? 64'h359 : _GEN_877; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_879 = 10'h35a == guard_index ? 64'h35a : _GEN_878; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_880 = 10'h35b == guard_index ? 64'h35b : _GEN_879; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_881 = 10'h35c == guard_index ? 64'h35c : _GEN_880; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_882 = 10'h35d == guard_index ? 64'h35d : _GEN_881; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_883 = 10'h35e == guard_index ? 64'h35e : _GEN_882; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_884 = 10'h35f == guard_index ? 64'h35f : _GEN_883; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_885 = 10'h360 == guard_index ? 64'h360 : _GEN_884; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_886 = 10'h361 == guard_index ? 64'h361 : _GEN_885; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_887 = 10'h362 == guard_index ? 64'h362 : _GEN_886; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_888 = 10'h363 == guard_index ? 64'h363 : _GEN_887; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_889 = 10'h364 == guard_index ? 64'h364 : _GEN_888; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_890 = 10'h365 == guard_index ? 64'h365 : _GEN_889; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_891 = 10'h366 == guard_index ? 64'h366 : _GEN_890; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_892 = 10'h367 == guard_index ? 64'h367 : _GEN_891; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_893 = 10'h368 == guard_index ? 64'h368 : _GEN_892; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_894 = 10'h369 == guard_index ? 64'h369 : _GEN_893; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_895 = 10'h36a == guard_index ? 64'h36a : _GEN_894; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_896 = 10'h36b == guard_index ? 64'h36b : _GEN_895; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_897 = 10'h36c == guard_index ? 64'h36c : _GEN_896; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_898 = 10'h36d == guard_index ? 64'h36d : _GEN_897; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_899 = 10'h36e == guard_index ? 64'h36e : _GEN_898; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_900 = 10'h36f == guard_index ? 64'h36f : _GEN_899; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_901 = 10'h370 == guard_index ? 64'h370 : _GEN_900; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_902 = 10'h371 == guard_index ? 64'h371 : _GEN_901; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_903 = 10'h372 == guard_index ? 64'h372 : _GEN_902; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_904 = 10'h373 == guard_index ? 64'h373 : _GEN_903; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_905 = 10'h374 == guard_index ? 64'h374 : _GEN_904; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_906 = 10'h375 == guard_index ? 64'h375 : _GEN_905; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_907 = 10'h376 == guard_index ? 64'h376 : _GEN_906; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_908 = 10'h377 == guard_index ? 64'h377 : _GEN_907; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_909 = 10'h378 == guard_index ? 64'h378 : _GEN_908; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_910 = 10'h379 == guard_index ? 64'h379 : _GEN_909; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_911 = 10'h37a == guard_index ? 64'h37a : _GEN_910; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_912 = 10'h37b == guard_index ? 64'h37b : _GEN_911; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_913 = 10'h37c == guard_index ? 64'h37c : _GEN_912; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_914 = 10'h37d == guard_index ? 64'h37d : _GEN_913; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_915 = 10'h37e == guard_index ? 64'h37e : _GEN_914; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_916 = 10'h37f == guard_index ? 64'h37f : _GEN_915; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_917 = 10'h380 == guard_index ? 64'h380 : _GEN_916; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_918 = 10'h381 == guard_index ? 64'h381 : _GEN_917; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_919 = 10'h382 == guard_index ? 64'h382 : _GEN_918; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_920 = 10'h383 == guard_index ? 64'h383 : _GEN_919; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_921 = 10'h384 == guard_index ? 64'h384 : _GEN_920; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_922 = 10'h385 == guard_index ? 64'h385 : _GEN_921; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_923 = 10'h386 == guard_index ? 64'h386 : _GEN_922; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_924 = 10'h387 == guard_index ? 64'h387 : _GEN_923; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_925 = 10'h388 == guard_index ? 64'h388 : _GEN_924; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_926 = 10'h389 == guard_index ? 64'h389 : _GEN_925; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_927 = 10'h38a == guard_index ? 64'h38a : _GEN_926; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_928 = 10'h38b == guard_index ? 64'h38b : _GEN_927; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_929 = 10'h38c == guard_index ? 64'h38c : _GEN_928; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_930 = 10'h38d == guard_index ? 64'h38d : _GEN_929; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_931 = 10'h38e == guard_index ? 64'h38e : _GEN_930; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_932 = 10'h38f == guard_index ? 64'h38f : _GEN_931; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_933 = 10'h390 == guard_index ? 64'h390 : _GEN_932; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_934 = 10'h391 == guard_index ? 64'h391 : _GEN_933; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_935 = 10'h392 == guard_index ? 64'h392 : _GEN_934; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_936 = 10'h393 == guard_index ? 64'h393 : _GEN_935; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_937 = 10'h394 == guard_index ? 64'h394 : _GEN_936; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_938 = 10'h395 == guard_index ? 64'h395 : _GEN_937; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_939 = 10'h396 == guard_index ? 64'h396 : _GEN_938; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_940 = 10'h397 == guard_index ? 64'h397 : _GEN_939; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_941 = 10'h398 == guard_index ? 64'h398 : _GEN_940; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_942 = 10'h399 == guard_index ? 64'h399 : _GEN_941; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_943 = 10'h39a == guard_index ? 64'h39a : _GEN_942; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_944 = 10'h39b == guard_index ? 64'h39b : _GEN_943; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_945 = 10'h39c == guard_index ? 64'h39c : _GEN_944; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_946 = 10'h39d == guard_index ? 64'h39d : _GEN_945; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_947 = 10'h39e == guard_index ? 64'h39e : _GEN_946; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_948 = 10'h39f == guard_index ? 64'h39f : _GEN_947; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_949 = 10'h3a0 == guard_index ? 64'h3a0 : _GEN_948; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_950 = 10'h3a1 == guard_index ? 64'h3a1 : _GEN_949; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_951 = 10'h3a2 == guard_index ? 64'h3a2 : _GEN_950; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_952 = 10'h3a3 == guard_index ? 64'h3a3 : _GEN_951; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_953 = 10'h3a4 == guard_index ? 64'h3a4 : _GEN_952; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_954 = 10'h3a5 == guard_index ? 64'h3a5 : _GEN_953; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_955 = 10'h3a6 == guard_index ? 64'h3a6 : _GEN_954; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_956 = 10'h3a7 == guard_index ? 64'h3a7 : _GEN_955; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_957 = 10'h3a8 == guard_index ? 64'h3a8 : _GEN_956; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_958 = 10'h3a9 == guard_index ? 64'h3a9 : _GEN_957; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_959 = 10'h3aa == guard_index ? 64'h3aa : _GEN_958; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_960 = 10'h3ab == guard_index ? 64'h3ab : _GEN_959; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_961 = 10'h3ac == guard_index ? 64'h3ac : _GEN_960; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_962 = 10'h3ad == guard_index ? 64'h3ad : _GEN_961; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_963 = 10'h3ae == guard_index ? 64'h3ae : _GEN_962; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_964 = 10'h3af == guard_index ? 64'h3af : _GEN_963; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_965 = 10'h3b0 == guard_index ? 64'h3b0 : _GEN_964; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_966 = 10'h3b1 == guard_index ? 64'h3b1 : _GEN_965; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_967 = 10'h3b2 == guard_index ? 64'h3b2 : _GEN_966; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_968 = 10'h3b3 == guard_index ? 64'h3b3 : _GEN_967; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_969 = 10'h3b4 == guard_index ? 64'h3b4 : _GEN_968; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_970 = 10'h3b5 == guard_index ? 64'h3b5 : _GEN_969; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_971 = 10'h3b6 == guard_index ? 64'h3b6 : _GEN_970; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_972 = 10'h3b7 == guard_index ? 64'h3b7 : _GEN_971; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_973 = 10'h3b8 == guard_index ? 64'h3b8 : _GEN_972; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_974 = 10'h3b9 == guard_index ? 64'h3b9 : _GEN_973; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_975 = 10'h3ba == guard_index ? 64'h3ba : _GEN_974; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_976 = 10'h3bb == guard_index ? 64'h3bb : _GEN_975; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_977 = 10'h3bc == guard_index ? 64'h3bc : _GEN_976; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_978 = 10'h3bd == guard_index ? 64'h3bd : _GEN_977; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_979 = 10'h3be == guard_index ? 64'h3be : _GEN_978; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_980 = 10'h3bf == guard_index ? 64'h3bf : _GEN_979; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_981 = 10'h3c0 == guard_index ? 64'h3c0 : _GEN_980; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_982 = 10'h3c1 == guard_index ? 64'h3c1 : _GEN_981; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_983 = 10'h3c2 == guard_index ? 64'h3c2 : _GEN_982; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_984 = 10'h3c3 == guard_index ? 64'h3c3 : _GEN_983; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_985 = 10'h3c4 == guard_index ? 64'h3c4 : _GEN_984; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_986 = 10'h3c5 == guard_index ? 64'h3c5 : _GEN_985; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_987 = 10'h3c6 == guard_index ? 64'h3c6 : _GEN_986; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_988 = 10'h3c7 == guard_index ? 64'h3c7 : _GEN_987; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_989 = 10'h3c8 == guard_index ? 64'h3c8 : _GEN_988; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_990 = 10'h3c9 == guard_index ? 64'h3c9 : _GEN_989; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_991 = 10'h3ca == guard_index ? 64'h3ca : _GEN_990; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_992 = 10'h3cb == guard_index ? 64'h3cb : _GEN_991; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_993 = 10'h3cc == guard_index ? 64'h3cc : _GEN_992; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_994 = 10'h3cd == guard_index ? 64'h3cd : _GEN_993; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_995 = 10'h3ce == guard_index ? 64'h3ce : _GEN_994; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_996 = 10'h3cf == guard_index ? 64'h3cf : _GEN_995; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_997 = 10'h3d0 == guard_index ? 64'h3d0 : _GEN_996; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_998 = 10'h3d1 == guard_index ? 64'h3d1 : _GEN_997; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_999 = 10'h3d2 == guard_index ? 64'h3d2 : _GEN_998; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1000 = 10'h3d3 == guard_index ? 64'h3d3 : _GEN_999; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1001 = 10'h3d4 == guard_index ? 64'h3d4 : _GEN_1000; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1002 = 10'h3d5 == guard_index ? 64'h3d5 : _GEN_1001; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1003 = 10'h3d6 == guard_index ? 64'h3d6 : _GEN_1002; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1004 = 10'h3d7 == guard_index ? 64'h3d7 : _GEN_1003; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1005 = 10'h3d8 == guard_index ? 64'h3d8 : _GEN_1004; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1006 = 10'h3d9 == guard_index ? 64'h3d9 : _GEN_1005; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1007 = 10'h3da == guard_index ? 64'h3da : _GEN_1006; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1008 = 10'h3db == guard_index ? 64'h3db : _GEN_1007; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1009 = 10'h3dc == guard_index ? 64'h3dc : _GEN_1008; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1010 = 10'h3dd == guard_index ? 64'h3dd : _GEN_1009; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1011 = 10'h3de == guard_index ? 64'h3de : _GEN_1010; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1012 = 10'h3df == guard_index ? 64'h3df : _GEN_1011; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1013 = 10'h3e0 == guard_index ? 64'h3e0 : _GEN_1012; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1014 = 10'h3e1 == guard_index ? 64'h3e1 : _GEN_1013; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1015 = 10'h3e2 == guard_index ? 64'h3e2 : _GEN_1014; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1016 = 10'h3e3 == guard_index ? 64'h3e3 : _GEN_1015; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1017 = 10'h3e4 == guard_index ? 64'h3e4 : _GEN_1016; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1018 = 10'h3e5 == guard_index ? 64'h3e5 : _GEN_1017; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1019 = 10'h3e6 == guard_index ? 64'h3e6 : _GEN_1018; // @[ComputeNode.scala 155:26]
  wire [63:0] _GEN_1020 = 10'h3e7 == guard_index ? 64'h3e7 : _GEN_1019; // @[ComputeNode.scala 155:26]
  wire  _T_38 = FU_io_out != _GEN_1020; // @[ComputeNode.scala 155:26]
  wire  _T_23 = right_valid_R & left_valid_R; // @[ComputeNode.scala 71:19]
  wire  _T_24 = enable_valid_R & _T_23; // @[ComputeNode.scala 75:20]
  wire  _T_25 = _T_24 & enable_R_control; // @[ComputeNode.scala 75:38]
  wire  _T_27 = _T_25 & _T_34; // @[ComputeNode.scala 75:58]
  wire  _T_29 = guard_index == 10'h3e7; // @[Counter.scala 38:24]
  wire [9:0] _T_31 = guard_index + 10'h1; // @[Counter.scala 39:22]
  wire [63:0] _T_41_data = FU_io_out; // @[interfaces.scala 289:20 interfaces.scala 290:15]
  wire [63:0] _GEN_1023 = _T_38 ? _GEN_1020 : _T_41_data; // @[ComputeNode.scala 155:61]
  wire  _T_45 = _T_1 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire  _T_46 = _T_2 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire [63:0] _GEN_1031 = _T_36 ? _GEN_1023 : out_data_R; // @[ComputeNode.scala 147:81]
  wire  _GEN_1037 = _T_36 | out_valid_R_0; // @[ComputeNode.scala 147:81]
  wire  _GEN_1038 = _T_36 | out_valid_R_1; // @[ComputeNode.scala 147:81]
  wire  _GEN_1043 = _T_36 | state; // @[ComputeNode.scala 147:81]
  wire  _T_50 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  wire  _T_51 = out_ready_R_1 | _T_2; // @[HandShaking.scala 251:83]
  wire  _T_52 = _T_50 & _T_51; // @[HandShaking.scala 252:27]
  UALU_2 FU ( // @[ComputeNode.scala 61:18]
    .io_in1(FU_io_in1),
    .io_in2(FU_io_in2),
    .io_out(FU_io_out)
  );
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 205:19]
  assign io_Out_0_valid = _T_34 ? _GEN_1037 : out_valid_R_0; // @[HandShaking.scala 194:21 ComputeNode.scala 172:32]
  assign io_Out_0_bits_data = _T_34 ? _GEN_1031 : out_data_R; // @[ComputeNode.scala 137:25 ComputeNode.scala 158:35 ComputeNode.scala 166:35]
  assign io_Out_1_valid = _T_34 ? _GEN_1038 : out_valid_R_1; // @[HandShaking.scala 194:21 ComputeNode.scala 172:32]
  assign io_Out_1_bits_data = _T_34 ? _GEN_1031 : out_data_R; // @[ComputeNode.scala 137:25 ComputeNode.scala 158:35 ComputeNode.scala 166:35]
  assign io_LeftIO_ready = ~left_valid_R; // @[ComputeNode.scala 104:19]
  assign io_RightIO_ready = ~right_valid_R; // @[ComputeNode.scala 110:20]
  assign FU_io_in1 = left_R_data; // @[ComputeNode.scala 101:13]
  assign FU_io_in2 = right_R_data; // @[ComputeNode.scala 102:13]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_ready_R_1 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  out_valid_R_1 = _RAND_5[0:0];
  _RAND_6 = {2{`RANDOM}};
  left_R_data = _RAND_6[63:0];
  _RAND_7 = {1{`RANDOM}};
  left_valid_R = _RAND_7[0:0];
  _RAND_8 = {2{`RANDOM}};
  right_R_data = _RAND_8[63:0];
  _RAND_9 = {1{`RANDOM}};
  right_valid_R = _RAND_9[0:0];
  _RAND_10 = {1{`RANDOM}};
  state = _RAND_10[0:0];
  _RAND_11 = {2{`RANDOM}};
  out_data_R = _RAND_11[63:0];
  _RAND_12 = {1{`RANDOM}};
  guard_index = _RAND_12[9:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_4) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_34) begin
      if (_T_4) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_52) begin
        enable_valid_R <= 1'h0;
      end else if (_T_4) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_4) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_34) begin
      if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_52) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_1) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_ready_R_1 <= 1'h0;
    end else if (_T_34) begin
      if (_T_2) begin
        out_ready_R_1 <= io_Out_1_ready;
      end
    end else if (state) begin
      if (_T_52) begin
        out_ready_R_1 <= 1'h0;
      end else if (_T_2) begin
        out_ready_R_1 <= io_Out_1_ready;
      end
    end else if (_T_2) begin
      out_ready_R_1 <= io_Out_1_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_34) begin
      if (_T_36) begin
        out_valid_R_0 <= _T_45;
      end else if (_T_1) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_1) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      out_valid_R_1 <= 1'h0;
    end else if (_T_34) begin
      if (_T_36) begin
        out_valid_R_1 <= _T_46;
      end else if (_T_2) begin
        out_valid_R_1 <= 1'h0;
      end
    end else if (_T_2) begin
      out_valid_R_1 <= 1'h0;
    end
    if (reset) begin
      left_R_data <= 64'h0;
    end else if (_T_13) begin
      left_R_data <= io_LeftIO_bits_data;
    end
    if (reset) begin
      left_valid_R <= 1'h0;
    end else if (_T_34) begin
      if (_T_36) begin
        left_valid_R <= 1'h0;
      end else begin
        left_valid_R <= _GEN_13;
      end
    end else begin
      left_valid_R <= _GEN_13;
    end
    if (reset) begin
      right_R_data <= 64'h0;
    end else if (_T_15) begin
      right_R_data <= 64'h1;
    end
    if (reset) begin
      right_valid_R <= 1'h0;
    end else if (_T_34) begin
      if (_T_36) begin
        right_valid_R <= 1'h0;
      end else begin
        right_valid_R <= _GEN_17;
      end
    end else begin
      right_valid_R <= _GEN_17;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_34) begin
      state <= _GEN_1043;
    end else if (state) begin
      if (_T_52) begin
        state <= 1'h0;
      end
    end
    if (reset) begin
      out_data_R <= 64'h0;
    end else if (_T_34) begin
      if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (state) begin
      if (_T_52) begin
        out_data_R <= 64'h0;
      end else if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (enable_R_control) begin
      out_data_R <= FU_io_out;
    end else begin
      out_data_R <= 64'h0;
    end
    if (reset) begin
      guard_index <= 10'h0;
    end else if (_T_27) begin
      if (_T_29) begin
        guard_index <= 10'h0;
      end else begin
        guard_index <= _T_31;
      end
    end
  end
endmodule
module UALU_4(
  input  [63:0] io_in1,
  input  [63:0] io_in2,
  output [63:0] io_out
);
  wire  _T_21 = io_in1 == io_in2; // @[Alu.scala 189:38]
  assign io_out = {{63'd0}, _T_21}; // @[Alu.scala 235:10]
endmodule
module ComputeNode_4(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  input         io_Out_0_ready,
  output        io_Out_0_valid,
  output [63:0] io_Out_0_bits_data,
  output        io_LeftIO_ready,
  input         io_LeftIO_valid,
  input  [63:0] io_LeftIO_bits_data,
  output        io_RightIO_ready,
  input         io_RightIO_valid,
  input  [63:0] io_RightIO_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [63:0] _RAND_9;
`endif // RANDOMIZE_REG_INIT
  wire [63:0] FU_io_in1; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_in2; // @[ComputeNode.scala 61:18]
  wire [63:0] FU_io_out; // @[ComputeNode.scala 61:18]
  reg  enable_R_control; // @[HandShaking.scala 181:31]
  reg  enable_valid_R; // @[HandShaking.scala 182:31]
  reg  out_ready_R_0; // @[HandShaking.scala 185:46]
  reg  out_valid_R_0; // @[HandShaking.scala 186:46]
  wire  _T_1 = io_Out_0_ready & io_Out_0_valid; // @[Decoupled.scala 40:37]
  wire  _T_3 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  reg [63:0] left_R_data; // @[ComputeNode.scala 53:23]
  reg  left_valid_R; // @[ComputeNode.scala 54:29]
  reg [63:0] right_R_data; // @[ComputeNode.scala 57:24]
  reg  right_valid_R; // @[ComputeNode.scala 58:30]
  reg  state; // @[ComputeNode.scala 64:22]
  reg [63:0] out_data_R; // @[ComputeNode.scala 89:27]
  wire  _T_12 = io_LeftIO_ready & io_LeftIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_11 = _T_12 | left_valid_R; // @[ComputeNode.scala 105:26]
  wire  _T_14 = io_RightIO_ready & io_RightIO_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_14 | right_valid_R; // @[ComputeNode.scala 111:27]
  wire  _T_22 = ~state; // @[ComputeNode.scala 75:67]
  wire  _T_27 = enable_valid_R & left_valid_R; // @[ComputeNode.scala 147:27]
  wire  _T_28 = _T_27 & right_valid_R; // @[ComputeNode.scala 147:43]
  wire  _T_32 = _T_1 ^ 1'h1; // @[HandShaking.scala 274:72]
  wire [63:0] _T_30_data = FU_io_out; // @[interfaces.scala 289:20 interfaces.scala 290:15]
  wire [63:0] _GEN_17 = _T_28 ? _T_30_data : out_data_R; // @[ComputeNode.scala 147:81]
  wire  _GEN_20 = _T_28 | out_valid_R_0; // @[ComputeNode.scala 147:81]
  wire  _GEN_24 = _T_28 | state; // @[ComputeNode.scala 147:81]
  wire  _T_35 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  UALU_4 FU ( // @[ComputeNode.scala 61:18]
    .io_in1(FU_io_in1),
    .io_in2(FU_io_in2),
    .io_out(FU_io_out)
  );
  assign io_enable_ready = ~enable_valid_R; // @[HandShaking.scala 205:19]
  assign io_Out_0_valid = _T_22 ? _GEN_20 : out_valid_R_0; // @[HandShaking.scala 194:21 ComputeNode.scala 172:32]
  assign io_Out_0_bits_data = _T_22 ? _GEN_17 : out_data_R; // @[ComputeNode.scala 137:25 ComputeNode.scala 170:33]
  assign io_LeftIO_ready = ~left_valid_R; // @[ComputeNode.scala 104:19]
  assign io_RightIO_ready = ~right_valid_R; // @[ComputeNode.scala 110:20]
  assign FU_io_in1 = left_R_data; // @[ComputeNode.scala 101:13]
  assign FU_io_in2 = right_R_data; // @[ComputeNode.scala 102:13]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  enable_valid_R = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  out_ready_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  out_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {2{`RANDOM}};
  left_R_data = _RAND_4[63:0];
  _RAND_5 = {1{`RANDOM}};
  left_valid_R = _RAND_5[0:0];
  _RAND_6 = {2{`RANDOM}};
  right_R_data = _RAND_6[63:0];
  _RAND_7 = {1{`RANDOM}};
  right_valid_R = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  state = _RAND_8[0:0];
  _RAND_9 = {2{`RANDOM}};
  out_data_R = _RAND_9[63:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_3) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_22) begin
      if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (state) begin
      if (_T_35) begin
        enable_valid_R <= 1'h0;
      end else if (_T_3) begin
        enable_valid_R <= io_enable_valid;
      end
    end else if (_T_3) begin
      enable_valid_R <= io_enable_valid;
    end
    if (reset) begin
      out_ready_R_0 <= 1'h0;
    end else if (_T_22) begin
      if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (state) begin
      if (_T_35) begin
        out_ready_R_0 <= 1'h0;
      end else if (_T_1) begin
        out_ready_R_0 <= io_Out_0_ready;
      end
    end else if (_T_1) begin
      out_ready_R_0 <= io_Out_0_ready;
    end
    if (reset) begin
      out_valid_R_0 <= 1'h0;
    end else if (_T_22) begin
      if (_T_28) begin
        out_valid_R_0 <= _T_32;
      end else if (_T_1) begin
        out_valid_R_0 <= 1'h0;
      end
    end else if (_T_1) begin
      out_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      left_R_data <= 64'h0;
    end else if (_T_12) begin
      left_R_data <= io_LeftIO_bits_data;
    end
    if (reset) begin
      left_valid_R <= 1'h0;
    end else if (_T_22) begin
      if (_T_28) begin
        left_valid_R <= 1'h0;
      end else begin
        left_valid_R <= _GEN_11;
      end
    end else begin
      left_valid_R <= _GEN_11;
    end
    if (reset) begin
      right_R_data <= 64'h0;
    end else if (_T_14) begin
      right_R_data <= io_RightIO_bits_data;
    end
    if (reset) begin
      right_valid_R <= 1'h0;
    end else if (_T_22) begin
      if (_T_28) begin
        right_valid_R <= 1'h0;
      end else begin
        right_valid_R <= _GEN_15;
      end
    end else begin
      right_valid_R <= _GEN_15;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_22) begin
      state <= _GEN_24;
    end else if (state) begin
      if (_T_35) begin
        state <= 1'h0;
      end
    end
    if (reset) begin
      out_data_R <= 64'h0;
    end else if (_T_22) begin
      if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (state) begin
      if (_T_35) begin
        out_data_R <= 64'h0;
      end else if (enable_R_control) begin
        out_data_R <= FU_io_out;
      end else begin
        out_data_R <= 64'h0;
      end
    end else if (enable_R_control) begin
      out_data_R <= FU_io_out;
    end else begin
      out_data_R <= 64'h0;
    end
  end
endmodule
module CBranchNodeVariable_1(
  input         clock,
  input         reset,
  output        io_enable_ready,
  input         io_enable_valid,
  input         io_enable_bits_control,
  output        io_CmpIO_ready,
  input         io_CmpIO_valid,
  input  [63:0] io_CmpIO_bits_data,
  output        io_PredOp_0_ready,
  input         io_PredOp_0_valid,
  input         io_TrueOutput_0_ready,
  output        io_TrueOutput_0_valid,
  output        io_TrueOutput_0_bits_control,
  input         io_FalseOutput_0_ready,
  output        io_FalseOutput_0_valid,
  output        io_FalseOutput_0_bits_control
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
`endif // RANDOMIZE_REG_INIT
  reg  cmp_R_control; // @[BranchNode.scala 1182:22]
  reg  cmp_valid; // @[BranchNode.scala 1183:26]
  reg  enable_R_control; // @[BranchNode.scala 1186:25]
  reg  enable_valid_R; // @[BranchNode.scala 1187:31]
  reg  predecessor_valid_R_0; // @[BranchNode.scala 1191:61]
  reg  output_true_R_control; // @[BranchNode.scala 1193:30]
  reg  output_true_valid_R_0; // @[BranchNode.scala 1194:54]
  reg  fire_true_R_0; // @[BranchNode.scala 1195:46]
  reg  output_false_R_control; // @[BranchNode.scala 1197:31]
  reg  output_false_valid_R_0; // @[BranchNode.scala 1198:56]
  reg  fire_false_R_0; // @[BranchNode.scala 1199:48]
  wire  _T_10 = io_CmpIO_ready & io_CmpIO_valid; // @[Decoupled.scala 40:37]
  wire  _T_11 = |io_CmpIO_bits_data; // @[BranchNode.scala 1207:44]
  wire  _GEN_4 = _T_10 | cmp_valid; // @[BranchNode.scala 1206:23]
  wire  _T_13 = io_PredOp_0_ready & io_PredOp_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_8 = _T_13 | predecessor_valid_R_0; // @[BranchNode.scala 1214:29]
  wire  _T_15 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_12 = _T_15 | enable_valid_R; // @[BranchNode.scala 1232:24]
  wire  true_output = enable_R_control & cmp_R_control; // @[BranchNode.scala 1238:38]
  wire  _T_16 = ~cmp_R_control; // @[BranchNode.scala 1239:43]
  wire  false_output = enable_R_control & _T_16; // @[BranchNode.scala 1239:39]
  wire  _T_17 = io_TrueOutput_0_ready & io_TrueOutput_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_13 = _T_17 | fire_true_R_0; // @[BranchNode.scala 1250:33]
  wire  _GEN_14 = _T_17 ? 1'h0 : output_true_valid_R_0; // @[BranchNode.scala 1250:33]
  wire  _T_18 = io_FalseOutput_0_ready & io_FalseOutput_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_15 = _T_18 | fire_false_R_0; // @[BranchNode.scala 1266:34]
  wire  _GEN_16 = _T_18 ? 1'h0 : output_false_valid_R_0; // @[BranchNode.scala 1266:34]
  reg  state; // @[BranchNode.scala 1278:22]
  wire  _T_19 = ~state; // @[Conditional.scala 37:30]
  wire  _T_20 = enable_valid_R & cmp_valid; // @[BranchNode.scala 1283:27]
  wire  _T_21 = _T_20 & predecessor_valid_R_0; // @[BranchNode.scala 1283:40]
  wire  _GEN_17 = _T_21 | _GEN_14; // @[BranchNode.scala 1283:65]
  wire  _GEN_18 = _T_21 | _GEN_16; // @[BranchNode.scala 1283:65]
  wire  _GEN_19 = _T_21 | state; // @[BranchNode.scala 1283:65]
  wire  _T_23 = fire_true_R_0 & fire_false_R_0; // @[BranchNode.scala 1313:27]
  assign io_enable_ready = ~enable_valid_R; // @[BranchNode.scala 1231:19]
  assign io_CmpIO_ready = ~cmp_valid; // @[BranchNode.scala 1205:18]
  assign io_PredOp_0_ready = ~predecessor_valid_R_0; // @[BranchNode.scala 1213:24]
  assign io_TrueOutput_0_valid = output_true_valid_R_0; // @[BranchNode.scala 1246:28]
  assign io_TrueOutput_0_bits_control = output_true_R_control; // @[BranchNode.scala 1245:27]
  assign io_FalseOutput_0_valid = output_false_valid_R_0; // @[BranchNode.scala 1262:29]
  assign io_FalseOutput_0_bits_control = output_false_R_control; // @[BranchNode.scala 1261:28]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  cmp_R_control = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  cmp_valid = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  enable_R_control = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  enable_valid_R = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  predecessor_valid_R_0 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  output_true_R_control = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  output_true_valid_R_0 = _RAND_6[0:0];
  _RAND_7 = {1{`RANDOM}};
  fire_true_R_0 = _RAND_7[0:0];
  _RAND_8 = {1{`RANDOM}};
  output_false_R_control = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  output_false_valid_R_0 = _RAND_9[0:0];
  _RAND_10 = {1{`RANDOM}};
  fire_false_R_0 = _RAND_10[0:0];
  _RAND_11 = {1{`RANDOM}};
  state = _RAND_11[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      cmp_R_control <= 1'h0;
    end else if (_T_19) begin
      if (_T_10) begin
        cmp_R_control <= _T_11;
      end
    end else if (state) begin
      if (_T_23) begin
        cmp_R_control <= 1'h0;
      end else if (_T_10) begin
        cmp_R_control <= _T_11;
      end
    end else if (_T_10) begin
      cmp_R_control <= _T_11;
    end
    if (reset) begin
      cmp_valid <= 1'h0;
    end else if (_T_19) begin
      cmp_valid <= _GEN_4;
    end else if (state) begin
      if (_T_23) begin
        cmp_valid <= 1'h0;
      end else begin
        cmp_valid <= _GEN_4;
      end
    end else begin
      cmp_valid <= _GEN_4;
    end
    if (reset) begin
      enable_R_control <= 1'h0;
    end else if (_T_19) begin
      if (_T_15) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (state) begin
      if (_T_23) begin
        enable_R_control <= 1'h0;
      end else if (_T_15) begin
        enable_R_control <= io_enable_bits_control;
      end
    end else if (_T_15) begin
      enable_R_control <= io_enable_bits_control;
    end
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_19) begin
      enable_valid_R <= _GEN_12;
    end else if (state) begin
      if (_T_23) begin
        enable_valid_R <= 1'h0;
      end else begin
        enable_valid_R <= _GEN_12;
      end
    end else begin
      enable_valid_R <= _GEN_12;
    end
    if (reset) begin
      predecessor_valid_R_0 <= 1'h0;
    end else if (_T_19) begin
      predecessor_valid_R_0 <= _GEN_8;
    end else if (state) begin
      if (_T_23) begin
        predecessor_valid_R_0 <= 1'h0;
      end else begin
        predecessor_valid_R_0 <= _GEN_8;
      end
    end else begin
      predecessor_valid_R_0 <= _GEN_8;
    end
    if (reset) begin
      output_true_R_control <= 1'h0;
    end else if (_T_19) begin
      output_true_R_control <= true_output;
    end else if (state) begin
      if (_T_23) begin
        output_true_R_control <= 1'h0;
      end else begin
        output_true_R_control <= true_output;
      end
    end else begin
      output_true_R_control <= true_output;
    end
    if (reset) begin
      output_true_valid_R_0 <= 1'h0;
    end else if (_T_19) begin
      output_true_valid_R_0 <= _GEN_17;
    end else if (state) begin
      if (_T_23) begin
        output_true_valid_R_0 <= 1'h0;
      end else if (_T_17) begin
        output_true_valid_R_0 <= 1'h0;
      end
    end else if (_T_17) begin
      output_true_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      fire_true_R_0 <= 1'h0;
    end else if (_T_19) begin
      fire_true_R_0 <= _GEN_13;
    end else if (state) begin
      if (_T_23) begin
        fire_true_R_0 <= 1'h0;
      end else begin
        fire_true_R_0 <= _GEN_13;
      end
    end else begin
      fire_true_R_0 <= _GEN_13;
    end
    if (reset) begin
      output_false_R_control <= 1'h0;
    end else if (_T_19) begin
      output_false_R_control <= false_output;
    end else if (state) begin
      if (_T_23) begin
        output_false_R_control <= 1'h0;
      end else begin
        output_false_R_control <= false_output;
      end
    end else begin
      output_false_R_control <= false_output;
    end
    if (reset) begin
      output_false_valid_R_0 <= 1'h0;
    end else if (_T_19) begin
      output_false_valid_R_0 <= _GEN_18;
    end else if (state) begin
      if (_T_23) begin
        output_false_valid_R_0 <= 1'h0;
      end else if (_T_18) begin
        output_false_valid_R_0 <= 1'h0;
      end
    end else if (_T_18) begin
      output_false_valid_R_0 <= 1'h0;
    end
    if (reset) begin
      fire_false_R_0 <= 1'h0;
    end else if (_T_19) begin
      fire_false_R_0 <= _GEN_15;
    end else if (state) begin
      if (_T_23) begin
        fire_false_R_0 <= 1'h0;
      end else begin
        fire_false_R_0 <= _GEN_15;
      end
    end else begin
      fire_false_R_0 <= _GEN_15;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_19) begin
      state <= _GEN_19;
    end else if (state) begin
      if (_T_23) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module ConstFastNode(
  input   clock,
  input   reset,
  output  io_enable_ready,
  input   io_enable_valid,
  input   io_Out_ready,
  output  io_Out_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
`endif // RANDOMIZE_REG_INIT
  reg  enable_valid_R; // @[ConstNode.scala 114:31]
  reg  state; // @[ConstNode.scala 135:22]
  wire  _T_7 = ~state; // @[Conditional.scala 37:30]
  wire  _T_8 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  wire  _T_9 = io_Out_ready & io_Out_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_7 = _T_8 | enable_valid_R; // @[ConstNode.scala 139:30]
  assign io_enable_ready = ~enable_valid_R; // @[ConstNode.scala 126:19]
  assign io_Out_valid = _T_7 ? _GEN_7 : enable_valid_R; // @[ConstNode.scala 127:16 ConstNode.scala 140:22]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_valid_R = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  state = _RAND_1[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_7) begin
      if (_T_8) begin
        if (!(_T_9)) begin
          enable_valid_R <= 1'h1;
        end
      end
    end else if (state) begin
      if (_T_9) begin
        enable_valid_R <= 1'h0;
      end
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_7) begin
      if (_T_8) begin
        if (_T_9) begin
          state <= 1'h0;
        end else begin
          state <= 1'h1;
        end
      end
    end else if (state) begin
      if (_T_9) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module ConstFastNode_1(
  input   clock,
  input   reset,
  output  io_enable_ready,
  input   io_enable_valid,
  input   io_Out_ready,
  output  io_Out_valid
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
`endif // RANDOMIZE_REG_INIT
  reg  enable_valid_R; // @[ConstNode.scala 114:31]
  reg  state; // @[ConstNode.scala 135:22]
  wire  _T_7 = ~state; // @[Conditional.scala 37:30]
  wire  _T_8 = io_enable_ready & io_enable_valid; // @[Decoupled.scala 40:37]
  wire  _T_9 = io_Out_ready & io_Out_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_7 = _T_8 | enable_valid_R; // @[ConstNode.scala 139:30]
  assign io_enable_ready = ~enable_valid_R; // @[ConstNode.scala 126:19]
  assign io_Out_valid = _T_7 ? _GEN_7 : enable_valid_R; // @[ConstNode.scala 127:16 ConstNode.scala 140:22]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  enable_valid_R = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  state = _RAND_1[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      enable_valid_R <= 1'h0;
    end else if (_T_7) begin
      if (_T_8) begin
        if (!(_T_9)) begin
          enable_valid_R <= 1'h1;
        end
      end
    end else if (state) begin
      if (_T_9) begin
        enable_valid_R <= 1'h0;
      end
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_7) begin
      if (_T_8) begin
        if (_T_9) begin
          state <= 1'h0;
        end else begin
          state <= 1'h1;
        end
      end
    end else if (state) begin
      if (_T_9) begin
        state <= 1'h0;
      end
    end
  end
endmodule
module saxpyDF(
  input         clock,
  input         reset,
  output        io_in_ready,
  input         io_in_valid,
  input  [63:0] io_in_bits_dataPtrs_field1_data,
  input  [63:0] io_in_bits_dataPtrs_field0_data,
  input  [63:0] io_in_bits_dataVals_field1_data,
  input  [63:0] io_in_bits_dataVals_field0_data,
  input         io_MemResp_valid,
  input  [63:0] io_MemResp_bits_data,
  input  [7:0]  io_MemResp_bits_tag,
  input         io_MemReq_ready,
  output        io_MemReq_valid,
  output [63:0] io_MemReq_bits_addr,
  output [63:0] io_MemReq_bits_data,
  output [7:0]  io_MemReq_bits_mask,
  output [7:0]  io_MemReq_bits_tag,
  input         io_out_ready,
  output        io_out_valid
);
  wire  mem_ctrl_cache_clock; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_reset; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_rd_mem_0_MemReq_ready; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_rd_mem_0_MemReq_valid; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_rd_mem_0_MemReq_bits_addr; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_rd_mem_0_MemResp_valid; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_rd_mem_0_MemResp_bits_data; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_rd_mem_1_MemReq_ready; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_rd_mem_1_MemReq_valid; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_rd_mem_1_MemReq_bits_addr; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_rd_mem_1_MemResp_valid; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_rd_mem_1_MemResp_bits_data; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_wr_mem_0_MemReq_ready; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_wr_mem_0_MemReq_valid; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_wr_mem_0_MemReq_bits_addr; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_wr_mem_0_MemReq_bits_data; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_wr_mem_0_MemResp_valid; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_cache_MemReq_ready; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_cache_MemReq_valid; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_cache_MemReq_bits_addr; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_cache_MemReq_bits_data; // @[saxpy.scala 35:30]
  wire [7:0] mem_ctrl_cache_io_cache_MemReq_bits_mask; // @[saxpy.scala 35:30]
  wire [7:0] mem_ctrl_cache_io_cache_MemReq_bits_tag; // @[saxpy.scala 35:30]
  wire  mem_ctrl_cache_io_cache_MemResp_valid; // @[saxpy.scala 35:30]
  wire [63:0] mem_ctrl_cache_io_cache_MemResp_bits_data; // @[saxpy.scala 35:30]
  wire [7:0] mem_ctrl_cache_io_cache_MemResp_bits_tag; // @[saxpy.scala 35:30]
  wire  ArgSplitter_clock; // @[saxpy.scala 40:27]
  wire  ArgSplitter_reset; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_In_ready; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_In_valid; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_In_bits_dataPtrs_field1_data; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_In_bits_dataPtrs_field0_data; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_In_bits_dataVals_field1_data; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_In_bits_dataVals_field0_data; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_enable_ready; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_enable_valid; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_enable_bits_control; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataPtrs_field1_0_ready; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataPtrs_field1_0_valid; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_Out_dataPtrs_field1_0_bits_data; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataPtrs_field0_0_ready; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataPtrs_field0_0_valid; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_Out_dataPtrs_field0_0_bits_data; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataVals_field1_0_ready; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataVals_field1_0_valid; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_Out_dataVals_field1_0_bits_data; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataVals_field0_0_ready; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataVals_field0_0_valid; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_Out_dataVals_field0_0_bits_data; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataVals_field0_1_ready; // @[saxpy.scala 40:27]
  wire  ArgSplitter_io_Out_dataVals_field0_1_valid; // @[saxpy.scala 40:27]
  wire [63:0] ArgSplitter_io_Out_dataVals_field0_1_bits_data; // @[saxpy.scala 40:27]
  wire  Loop_0_clock; // @[saxpy.scala 49:22]
  wire  Loop_0_reset; // @[saxpy.scala 49:22]
  wire  Loop_0_io_enable_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_enable_valid; // @[saxpy.scala 49:22]
  wire  Loop_0_io_enable_bits_control; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_0_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_InLiveIn_0_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_1_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_1_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_InLiveIn_1_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_2_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_2_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_InLiveIn_2_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_3_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_InLiveIn_3_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_InLiveIn_3_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field3_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field3_0_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_OutLiveIn_field3_0_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field2_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field2_0_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_OutLiveIn_field2_0_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field1_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field1_0_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_OutLiveIn_field1_0_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field0_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_OutLiveIn_field0_0_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_OutLiveIn_field0_0_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_activate_loop_start_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_activate_loop_start_valid; // @[saxpy.scala 49:22]
  wire  Loop_0_io_activate_loop_start_bits_control; // @[saxpy.scala 49:22]
  wire  Loop_0_io_activate_loop_back_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_activate_loop_back_valid; // @[saxpy.scala 49:22]
  wire  Loop_0_io_activate_loop_back_bits_control; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopBack_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopBack_0_valid; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopBack_0_bits_control; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopFinish_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopFinish_0_valid; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopFinish_0_bits_control; // @[saxpy.scala 49:22]
  wire  Loop_0_io_CarryDepenIn_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_CarryDepenIn_0_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_CarryDepenIn_0_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_CarryDepenOut_field0_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_CarryDepenOut_field0_0_valid; // @[saxpy.scala 49:22]
  wire [63:0] Loop_0_io_CarryDepenOut_field0_0_bits_data; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopExit_0_ready; // @[saxpy.scala 49:22]
  wire  Loop_0_io_loopExit_0_valid; // @[saxpy.scala 49:22]
  wire  bb_entry1_clock; // @[saxpy.scala 57:25]
  wire  bb_entry1_reset; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_predicateIn_0_ready; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_predicateIn_0_valid; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_predicateIn_0_bits_control; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_0_ready; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_0_valid; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_1_ready; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_1_valid; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_1_bits_control; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_2_ready; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_2_valid; // @[saxpy.scala 57:25]
  wire  bb_entry1_io_Out_2_bits_control; // @[saxpy.scala 57:25]
  wire  bb_for_body_lr_ph4_clock; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_reset; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_predicateIn_0_ready; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_predicateIn_0_valid; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_predicateIn_0_bits_control; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_Out_0_ready; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_Out_0_valid; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_Out_1_ready; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_Out_1_valid; // @[saxpy.scala 59:34]
  wire  bb_for_body_lr_ph4_io_Out_1_bits_control; // @[saxpy.scala 59:34]
  wire  bb_for_cond_cleanup_loopexit7_clock; // @[saxpy.scala 61:45]
  wire  bb_for_cond_cleanup_loopexit7_reset; // @[saxpy.scala 61:45]
  wire  bb_for_cond_cleanup_loopexit7_io_predicateIn_0_ready; // @[saxpy.scala 61:45]
  wire  bb_for_cond_cleanup_loopexit7_io_predicateIn_0_valid; // @[saxpy.scala 61:45]
  wire  bb_for_cond_cleanup_loopexit7_io_Out_0_ready; // @[saxpy.scala 61:45]
  wire  bb_for_cond_cleanup_loopexit7_io_Out_0_valid; // @[saxpy.scala 61:45]
  wire  bb_for_cond_cleanup9_clock; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_reset; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_predicateIn_0_ready; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_predicateIn_0_valid; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_predicateIn_1_ready; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_predicateIn_1_valid; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_Out_0_ready; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_Out_0_valid; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_Out_1_ready; // @[saxpy.scala 63:36]
  wire  bb_for_cond_cleanup9_io_Out_1_valid; // @[saxpy.scala 63:36]
  wire  bb_for_body11_clock; // @[saxpy.scala 65:29]
  wire  bb_for_body11_reset; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_MaskBB_0_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_MaskBB_0_valid; // @[saxpy.scala 65:29]
  wire [1:0] bb_for_body11_io_MaskBB_0_bits; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_0_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_0_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_1_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_1_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_2_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_2_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_2_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_3_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_3_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_4_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_4_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_4_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_5_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_5_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_5_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_6_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_6_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_7_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_7_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_7_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_8_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_8_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_8_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_9_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_9_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_9_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_10_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_10_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_10_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_11_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_11_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_11_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_12_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_12_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_Out_12_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_predicateIn_0_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_predicateIn_0_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_predicateIn_0_bits_control; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_predicateIn_1_ready; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_predicateIn_1_valid; // @[saxpy.scala 65:29]
  wire  bb_for_body11_io_predicateIn_1_bits_control; // @[saxpy.scala 65:29]
  wire  icmp_cmp110_clock; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_reset; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_enable_ready; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_enable_valid; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_enable_bits_control; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_Out_0_ready; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_Out_0_valid; // @[saxpy.scala 74:27]
  wire [63:0] icmp_cmp110_io_Out_0_bits_data; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_LeftIO_ready; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_LeftIO_valid; // @[saxpy.scala 74:27]
  wire [63:0] icmp_cmp110_io_LeftIO_bits_data; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_RightIO_ready; // @[saxpy.scala 74:27]
  wire  icmp_cmp110_io_RightIO_valid; // @[saxpy.scala 74:27]
  wire  br_3_clock; // @[saxpy.scala 77:20]
  wire  br_3_reset; // @[saxpy.scala 77:20]
  wire  br_3_io_enable_ready; // @[saxpy.scala 77:20]
  wire  br_3_io_enable_valid; // @[saxpy.scala 77:20]
  wire  br_3_io_enable_bits_control; // @[saxpy.scala 77:20]
  wire  br_3_io_CmpIO_ready; // @[saxpy.scala 77:20]
  wire  br_3_io_CmpIO_valid; // @[saxpy.scala 77:20]
  wire [63:0] br_3_io_CmpIO_bits_data; // @[saxpy.scala 77:20]
  wire  br_3_io_TrueOutput_0_ready; // @[saxpy.scala 77:20]
  wire  br_3_io_TrueOutput_0_valid; // @[saxpy.scala 77:20]
  wire  br_3_io_TrueOutput_0_bits_control; // @[saxpy.scala 77:20]
  wire  br_3_io_FalseOutput_0_ready; // @[saxpy.scala 77:20]
  wire  br_3_io_FalseOutput_0_valid; // @[saxpy.scala 77:20]
  wire  sextwide_trip_count5_clock; // @[saxpy.scala 80:36]
  wire  sextwide_trip_count5_reset; // @[saxpy.scala 80:36]
  wire  sextwide_trip_count5_io_Input_ready; // @[saxpy.scala 80:36]
  wire  sextwide_trip_count5_io_Input_valid; // @[saxpy.scala 80:36]
  wire [63:0] sextwide_trip_count5_io_Input_bits_data; // @[saxpy.scala 80:36]
  wire  sextwide_trip_count5_io_enable_ready; // @[saxpy.scala 80:36]
  wire  sextwide_trip_count5_io_enable_valid; // @[saxpy.scala 80:36]
  wire  sextwide_trip_count5_io_Out_0_ready; // @[saxpy.scala 80:36]
  wire  sextwide_trip_count5_io_Out_0_valid; // @[saxpy.scala 80:36]
  wire [63:0] sextwide_trip_count5_io_Out_0_bits_data; // @[saxpy.scala 80:36]
  wire  br_6_clock; // @[saxpy.scala 83:20]
  wire  br_6_reset; // @[saxpy.scala 83:20]
  wire  br_6_io_enable_ready; // @[saxpy.scala 83:20]
  wire  br_6_io_enable_valid; // @[saxpy.scala 83:20]
  wire  br_6_io_enable_bits_control; // @[saxpy.scala 83:20]
  wire  br_6_io_Out_0_ready; // @[saxpy.scala 83:20]
  wire  br_6_io_Out_0_valid; // @[saxpy.scala 83:20]
  wire  br_6_io_Out_0_bits_control; // @[saxpy.scala 83:20]
  wire  br_8_clock; // @[saxpy.scala 86:20]
  wire  br_8_reset; // @[saxpy.scala 86:20]
  wire  br_8_io_enable_ready; // @[saxpy.scala 86:20]
  wire  br_8_io_enable_valid; // @[saxpy.scala 86:20]
  wire  br_8_io_Out_0_ready; // @[saxpy.scala 86:20]
  wire  br_8_io_Out_0_valid; // @[saxpy.scala 86:20]
  wire  ret_10_clock; // @[saxpy.scala 89:22]
  wire  ret_10_reset; // @[saxpy.scala 89:22]
  wire  ret_10_io_In_enable_ready; // @[saxpy.scala 89:22]
  wire  ret_10_io_In_enable_valid; // @[saxpy.scala 89:22]
  wire  ret_10_io_In_data_field0_ready; // @[saxpy.scala 89:22]
  wire  ret_10_io_In_data_field0_valid; // @[saxpy.scala 89:22]
  wire  ret_10_io_Out_ready; // @[saxpy.scala 89:22]
  wire  ret_10_io_Out_valid; // @[saxpy.scala 89:22]
  wire  phiindvars_iv12_clock; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_reset; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_enable_ready; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_enable_valid; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_enable_bits_control; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_InData_0_ready; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_InData_0_valid; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_InData_1_ready; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_InData_1_valid; // @[saxpy.scala 92:31]
  wire [63:0] phiindvars_iv12_io_InData_1_bits_data; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Mask_ready; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Mask_valid; // @[saxpy.scala 92:31]
  wire [1:0] phiindvars_iv12_io_Mask_bits; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Out_0_ready; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Out_0_valid; // @[saxpy.scala 92:31]
  wire [63:0] phiindvars_iv12_io_Out_0_bits_data; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Out_1_ready; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Out_1_valid; // @[saxpy.scala 92:31]
  wire [63:0] phiindvars_iv12_io_Out_1_bits_data; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Out_2_ready; // @[saxpy.scala 92:31]
  wire  phiindvars_iv12_io_Out_2_valid; // @[saxpy.scala 92:31]
  wire [63:0] phiindvars_iv12_io_Out_2_bits_data; // @[saxpy.scala 92:31]
  wire  Gep_arrayidx13_clock; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_reset; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_enable_ready; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_enable_valid; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_Out_0_ready; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_Out_0_valid; // @[saxpy.scala 95:30]
  wire [63:0] Gep_arrayidx13_io_Out_0_bits_data; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_baseAddress_ready; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_baseAddress_valid; // @[saxpy.scala 95:30]
  wire [63:0] Gep_arrayidx13_io_baseAddress_bits_data; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_idx_0_ready; // @[saxpy.scala 95:30]
  wire  Gep_arrayidx13_io_idx_0_valid; // @[saxpy.scala 95:30]
  wire [63:0] Gep_arrayidx13_io_idx_0_bits_data; // @[saxpy.scala 95:30]
  wire  ld_14_clock; // @[saxpy.scala 98:21]
  wire  ld_14_reset; // @[saxpy.scala 98:21]
  wire  ld_14_io_enable_ready; // @[saxpy.scala 98:21]
  wire  ld_14_io_enable_valid; // @[saxpy.scala 98:21]
  wire  ld_14_io_enable_bits_control; // @[saxpy.scala 98:21]
  wire  ld_14_io_Out_0_ready; // @[saxpy.scala 98:21]
  wire  ld_14_io_Out_0_valid; // @[saxpy.scala 98:21]
  wire [63:0] ld_14_io_Out_0_bits_data; // @[saxpy.scala 98:21]
  wire  ld_14_io_GepAddr_ready; // @[saxpy.scala 98:21]
  wire  ld_14_io_GepAddr_valid; // @[saxpy.scala 98:21]
  wire [63:0] ld_14_io_GepAddr_bits_data; // @[saxpy.scala 98:21]
  wire  ld_14_io_MemReq_ready; // @[saxpy.scala 98:21]
  wire  ld_14_io_MemReq_valid; // @[saxpy.scala 98:21]
  wire [63:0] ld_14_io_MemReq_bits_addr; // @[saxpy.scala 98:21]
  wire  ld_14_io_MemResp_valid; // @[saxpy.scala 98:21]
  wire [63:0] ld_14_io_MemResp_bits_data; // @[saxpy.scala 98:21]
  wire  binaryOp_mul15_clock; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_reset; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_enable_ready; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_enable_valid; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_enable_bits_control; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_Out_0_ready; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_Out_0_valid; // @[saxpy.scala 101:30]
  wire [63:0] binaryOp_mul15_io_Out_0_bits_data; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_LeftIO_ready; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_LeftIO_valid; // @[saxpy.scala 101:30]
  wire [63:0] binaryOp_mul15_io_LeftIO_bits_data; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_RightIO_ready; // @[saxpy.scala 101:30]
  wire  binaryOp_mul15_io_RightIO_valid; // @[saxpy.scala 101:30]
  wire [63:0] binaryOp_mul15_io_RightIO_bits_data; // @[saxpy.scala 101:30]
  wire  Gep_arrayidx216_clock; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_reset; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_enable_ready; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_enable_valid; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_Out_0_ready; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_Out_0_valid; // @[saxpy.scala 104:31]
  wire [63:0] Gep_arrayidx216_io_Out_0_bits_data; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_Out_1_ready; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_Out_1_valid; // @[saxpy.scala 104:31]
  wire [63:0] Gep_arrayidx216_io_Out_1_bits_data; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_baseAddress_ready; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_baseAddress_valid; // @[saxpy.scala 104:31]
  wire [63:0] Gep_arrayidx216_io_baseAddress_bits_data; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_idx_0_ready; // @[saxpy.scala 104:31]
  wire  Gep_arrayidx216_io_idx_0_valid; // @[saxpy.scala 104:31]
  wire [63:0] Gep_arrayidx216_io_idx_0_bits_data; // @[saxpy.scala 104:31]
  wire  ld_17_clock; // @[saxpy.scala 107:21]
  wire  ld_17_reset; // @[saxpy.scala 107:21]
  wire  ld_17_io_enable_ready; // @[saxpy.scala 107:21]
  wire  ld_17_io_enable_valid; // @[saxpy.scala 107:21]
  wire  ld_17_io_enable_bits_control; // @[saxpy.scala 107:21]
  wire  ld_17_io_Out_0_ready; // @[saxpy.scala 107:21]
  wire  ld_17_io_Out_0_valid; // @[saxpy.scala 107:21]
  wire [63:0] ld_17_io_Out_0_bits_data; // @[saxpy.scala 107:21]
  wire  ld_17_io_GepAddr_ready; // @[saxpy.scala 107:21]
  wire  ld_17_io_GepAddr_valid; // @[saxpy.scala 107:21]
  wire [63:0] ld_17_io_GepAddr_bits_data; // @[saxpy.scala 107:21]
  wire  ld_17_io_MemReq_ready; // @[saxpy.scala 107:21]
  wire  ld_17_io_MemReq_valid; // @[saxpy.scala 107:21]
  wire [63:0] ld_17_io_MemReq_bits_addr; // @[saxpy.scala 107:21]
  wire  ld_17_io_MemResp_valid; // @[saxpy.scala 107:21]
  wire [63:0] ld_17_io_MemResp_bits_data; // @[saxpy.scala 107:21]
  wire  binaryOp_add18_clock; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_reset; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_enable_ready; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_enable_valid; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_enable_bits_control; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_Out_0_ready; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_Out_0_valid; // @[saxpy.scala 110:30]
  wire [63:0] binaryOp_add18_io_Out_0_bits_data; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_LeftIO_ready; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_LeftIO_valid; // @[saxpy.scala 110:30]
  wire [63:0] binaryOp_add18_io_LeftIO_bits_data; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_RightIO_ready; // @[saxpy.scala 110:30]
  wire  binaryOp_add18_io_RightIO_valid; // @[saxpy.scala 110:30]
  wire [63:0] binaryOp_add18_io_RightIO_bits_data; // @[saxpy.scala 110:30]
  wire  st_19_clock; // @[saxpy.scala 113:21]
  wire  st_19_reset; // @[saxpy.scala 113:21]
  wire  st_19_io_enable_ready; // @[saxpy.scala 113:21]
  wire  st_19_io_enable_valid; // @[saxpy.scala 113:21]
  wire  st_19_io_enable_bits_control; // @[saxpy.scala 113:21]
  wire  st_19_io_SuccOp_0_ready; // @[saxpy.scala 113:21]
  wire  st_19_io_SuccOp_0_valid; // @[saxpy.scala 113:21]
  wire  st_19_io_GepAddr_ready; // @[saxpy.scala 113:21]
  wire  st_19_io_GepAddr_valid; // @[saxpy.scala 113:21]
  wire [63:0] st_19_io_GepAddr_bits_data; // @[saxpy.scala 113:21]
  wire  st_19_io_inData_ready; // @[saxpy.scala 113:21]
  wire  st_19_io_inData_valid; // @[saxpy.scala 113:21]
  wire [63:0] st_19_io_inData_bits_data; // @[saxpy.scala 113:21]
  wire  st_19_io_MemReq_ready; // @[saxpy.scala 113:21]
  wire  st_19_io_MemReq_valid; // @[saxpy.scala 113:21]
  wire [63:0] st_19_io_MemReq_bits_addr; // @[saxpy.scala 113:21]
  wire [63:0] st_19_io_MemReq_bits_data; // @[saxpy.scala 113:21]
  wire  st_19_io_MemResp_valid; // @[saxpy.scala 113:21]
  wire  binaryOp_indvars_iv_next20_clock; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_reset; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_enable_ready; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_enable_valid; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_enable_bits_control; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_Out_0_ready; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_Out_0_valid; // @[saxpy.scala 116:42]
  wire [63:0] binaryOp_indvars_iv_next20_io_Out_0_bits_data; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_Out_1_ready; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_Out_1_valid; // @[saxpy.scala 116:42]
  wire [63:0] binaryOp_indvars_iv_next20_io_Out_1_bits_data; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_LeftIO_ready; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_LeftIO_valid; // @[saxpy.scala 116:42]
  wire [63:0] binaryOp_indvars_iv_next20_io_LeftIO_bits_data; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_RightIO_ready; // @[saxpy.scala 116:42]
  wire  binaryOp_indvars_iv_next20_io_RightIO_valid; // @[saxpy.scala 116:42]
  wire  icmp_exitcond15_clock; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_reset; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_enable_ready; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_enable_valid; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_enable_bits_control; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_Out_0_ready; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_Out_0_valid; // @[saxpy.scala 119:31]
  wire [63:0] icmp_exitcond15_io_Out_0_bits_data; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_LeftIO_ready; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_LeftIO_valid; // @[saxpy.scala 119:31]
  wire [63:0] icmp_exitcond15_io_LeftIO_bits_data; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_RightIO_ready; // @[saxpy.scala 119:31]
  wire  icmp_exitcond15_io_RightIO_valid; // @[saxpy.scala 119:31]
  wire [63:0] icmp_exitcond15_io_RightIO_bits_data; // @[saxpy.scala 119:31]
  wire  br_22_clock; // @[saxpy.scala 122:21]
  wire  br_22_reset; // @[saxpy.scala 122:21]
  wire  br_22_io_enable_ready; // @[saxpy.scala 122:21]
  wire  br_22_io_enable_valid; // @[saxpy.scala 122:21]
  wire  br_22_io_enable_bits_control; // @[saxpy.scala 122:21]
  wire  br_22_io_CmpIO_ready; // @[saxpy.scala 122:21]
  wire  br_22_io_CmpIO_valid; // @[saxpy.scala 122:21]
  wire [63:0] br_22_io_CmpIO_bits_data; // @[saxpy.scala 122:21]
  wire  br_22_io_PredOp_0_ready; // @[saxpy.scala 122:21]
  wire  br_22_io_PredOp_0_valid; // @[saxpy.scala 122:21]
  wire  br_22_io_TrueOutput_0_ready; // @[saxpy.scala 122:21]
  wire  br_22_io_TrueOutput_0_valid; // @[saxpy.scala 122:21]
  wire  br_22_io_TrueOutput_0_bits_control; // @[saxpy.scala 122:21]
  wire  br_22_io_FalseOutput_0_ready; // @[saxpy.scala 122:21]
  wire  br_22_io_FalseOutput_0_valid; // @[saxpy.scala 122:21]
  wire  br_22_io_FalseOutput_0_bits_control; // @[saxpy.scala 122:21]
  wire  const0_clock; // @[saxpy.scala 131:22]
  wire  const0_reset; // @[saxpy.scala 131:22]
  wire  const0_io_enable_ready; // @[saxpy.scala 131:22]
  wire  const0_io_enable_valid; // @[saxpy.scala 131:22]
  wire  const0_io_Out_ready; // @[saxpy.scala 131:22]
  wire  const0_io_Out_valid; // @[saxpy.scala 131:22]
  wire  const1_clock; // @[saxpy.scala 134:22]
  wire  const1_reset; // @[saxpy.scala 134:22]
  wire  const1_io_enable_ready; // @[saxpy.scala 134:22]
  wire  const1_io_enable_valid; // @[saxpy.scala 134:22]
  wire  const1_io_Out_ready; // @[saxpy.scala 134:22]
  wire  const1_io_Out_valid; // @[saxpy.scala 134:22]
  wire  const2_clock; // @[saxpy.scala 137:22]
  wire  const2_reset; // @[saxpy.scala 137:22]
  wire  const2_io_enable_ready; // @[saxpy.scala 137:22]
  wire  const2_io_enable_valid; // @[saxpy.scala 137:22]
  wire  const2_io_Out_ready; // @[saxpy.scala 137:22]
  wire  const2_io_Out_valid; // @[saxpy.scala 137:22]
  wire  const3_clock; // @[saxpy.scala 140:22]
  wire  const3_reset; // @[saxpy.scala 140:22]
  wire  const3_io_enable_ready; // @[saxpy.scala 140:22]
  wire  const3_io_enable_valid; // @[saxpy.scala 140:22]
  wire  const3_io_Out_ready; // @[saxpy.scala 140:22]
  wire  const3_io_Out_valid; // @[saxpy.scala 140:22]
  CacheMemoryEngine mem_ctrl_cache ( // @[saxpy.scala 35:30]
    .clock(mem_ctrl_cache_clock),
    .reset(mem_ctrl_cache_reset),
    .io_rd_mem_0_MemReq_ready(mem_ctrl_cache_io_rd_mem_0_MemReq_ready),
    .io_rd_mem_0_MemReq_valid(mem_ctrl_cache_io_rd_mem_0_MemReq_valid),
    .io_rd_mem_0_MemReq_bits_addr(mem_ctrl_cache_io_rd_mem_0_MemReq_bits_addr),
    .io_rd_mem_0_MemResp_valid(mem_ctrl_cache_io_rd_mem_0_MemResp_valid),
    .io_rd_mem_0_MemResp_bits_data(mem_ctrl_cache_io_rd_mem_0_MemResp_bits_data),
    .io_rd_mem_1_MemReq_ready(mem_ctrl_cache_io_rd_mem_1_MemReq_ready),
    .io_rd_mem_1_MemReq_valid(mem_ctrl_cache_io_rd_mem_1_MemReq_valid),
    .io_rd_mem_1_MemReq_bits_addr(mem_ctrl_cache_io_rd_mem_1_MemReq_bits_addr),
    .io_rd_mem_1_MemResp_valid(mem_ctrl_cache_io_rd_mem_1_MemResp_valid),
    .io_rd_mem_1_MemResp_bits_data(mem_ctrl_cache_io_rd_mem_1_MemResp_bits_data),
    .io_wr_mem_0_MemReq_ready(mem_ctrl_cache_io_wr_mem_0_MemReq_ready),
    .io_wr_mem_0_MemReq_valid(mem_ctrl_cache_io_wr_mem_0_MemReq_valid),
    .io_wr_mem_0_MemReq_bits_addr(mem_ctrl_cache_io_wr_mem_0_MemReq_bits_addr),
    .io_wr_mem_0_MemReq_bits_data(mem_ctrl_cache_io_wr_mem_0_MemReq_bits_data),
    .io_wr_mem_0_MemResp_valid(mem_ctrl_cache_io_wr_mem_0_MemResp_valid),
    .io_cache_MemReq_ready(mem_ctrl_cache_io_cache_MemReq_ready),
    .io_cache_MemReq_valid(mem_ctrl_cache_io_cache_MemReq_valid),
    .io_cache_MemReq_bits_addr(mem_ctrl_cache_io_cache_MemReq_bits_addr),
    .io_cache_MemReq_bits_data(mem_ctrl_cache_io_cache_MemReq_bits_data),
    .io_cache_MemReq_bits_mask(mem_ctrl_cache_io_cache_MemReq_bits_mask),
    .io_cache_MemReq_bits_tag(mem_ctrl_cache_io_cache_MemReq_bits_tag),
    .io_cache_MemResp_valid(mem_ctrl_cache_io_cache_MemResp_valid),
    .io_cache_MemResp_bits_data(mem_ctrl_cache_io_cache_MemResp_bits_data),
    .io_cache_MemResp_bits_tag(mem_ctrl_cache_io_cache_MemResp_bits_tag)
  );
  SplitCallDCR ArgSplitter ( // @[saxpy.scala 40:27]
    .clock(ArgSplitter_clock),
    .reset(ArgSplitter_reset),
    .io_In_ready(ArgSplitter_io_In_ready),
    .io_In_valid(ArgSplitter_io_In_valid),
    .io_In_bits_dataPtrs_field1_data(ArgSplitter_io_In_bits_dataPtrs_field1_data),
    .io_In_bits_dataPtrs_field0_data(ArgSplitter_io_In_bits_dataPtrs_field0_data),
    .io_In_bits_dataVals_field1_data(ArgSplitter_io_In_bits_dataVals_field1_data),
    .io_In_bits_dataVals_field0_data(ArgSplitter_io_In_bits_dataVals_field0_data),
    .io_Out_enable_ready(ArgSplitter_io_Out_enable_ready),
    .io_Out_enable_valid(ArgSplitter_io_Out_enable_valid),
    .io_Out_enable_bits_control(ArgSplitter_io_Out_enable_bits_control),
    .io_Out_dataPtrs_field1_0_ready(ArgSplitter_io_Out_dataPtrs_field1_0_ready),
    .io_Out_dataPtrs_field1_0_valid(ArgSplitter_io_Out_dataPtrs_field1_0_valid),
    .io_Out_dataPtrs_field1_0_bits_data(ArgSplitter_io_Out_dataPtrs_field1_0_bits_data),
    .io_Out_dataPtrs_field0_0_ready(ArgSplitter_io_Out_dataPtrs_field0_0_ready),
    .io_Out_dataPtrs_field0_0_valid(ArgSplitter_io_Out_dataPtrs_field0_0_valid),
    .io_Out_dataPtrs_field0_0_bits_data(ArgSplitter_io_Out_dataPtrs_field0_0_bits_data),
    .io_Out_dataVals_field1_0_ready(ArgSplitter_io_Out_dataVals_field1_0_ready),
    .io_Out_dataVals_field1_0_valid(ArgSplitter_io_Out_dataVals_field1_0_valid),
    .io_Out_dataVals_field1_0_bits_data(ArgSplitter_io_Out_dataVals_field1_0_bits_data),
    .io_Out_dataVals_field0_0_ready(ArgSplitter_io_Out_dataVals_field0_0_ready),
    .io_Out_dataVals_field0_0_valid(ArgSplitter_io_Out_dataVals_field0_0_valid),
    .io_Out_dataVals_field0_0_bits_data(ArgSplitter_io_Out_dataVals_field0_0_bits_data),
    .io_Out_dataVals_field0_1_ready(ArgSplitter_io_Out_dataVals_field0_1_ready),
    .io_Out_dataVals_field0_1_valid(ArgSplitter_io_Out_dataVals_field0_1_valid),
    .io_Out_dataVals_field0_1_bits_data(ArgSplitter_io_Out_dataVals_field0_1_bits_data)
  );
  LoopBlockNode Loop_0 ( // @[saxpy.scala 49:22]
    .clock(Loop_0_clock),
    .reset(Loop_0_reset),
    .io_enable_ready(Loop_0_io_enable_ready),
    .io_enable_valid(Loop_0_io_enable_valid),
    .io_enable_bits_control(Loop_0_io_enable_bits_control),
    .io_InLiveIn_0_ready(Loop_0_io_InLiveIn_0_ready),
    .io_InLiveIn_0_valid(Loop_0_io_InLiveIn_0_valid),
    .io_InLiveIn_0_bits_data(Loop_0_io_InLiveIn_0_bits_data),
    .io_InLiveIn_1_ready(Loop_0_io_InLiveIn_1_ready),
    .io_InLiveIn_1_valid(Loop_0_io_InLiveIn_1_valid),
    .io_InLiveIn_1_bits_data(Loop_0_io_InLiveIn_1_bits_data),
    .io_InLiveIn_2_ready(Loop_0_io_InLiveIn_2_ready),
    .io_InLiveIn_2_valid(Loop_0_io_InLiveIn_2_valid),
    .io_InLiveIn_2_bits_data(Loop_0_io_InLiveIn_2_bits_data),
    .io_InLiveIn_3_ready(Loop_0_io_InLiveIn_3_ready),
    .io_InLiveIn_3_valid(Loop_0_io_InLiveIn_3_valid),
    .io_InLiveIn_3_bits_data(Loop_0_io_InLiveIn_3_bits_data),
    .io_OutLiveIn_field3_0_ready(Loop_0_io_OutLiveIn_field3_0_ready),
    .io_OutLiveIn_field3_0_valid(Loop_0_io_OutLiveIn_field3_0_valid),
    .io_OutLiveIn_field3_0_bits_data(Loop_0_io_OutLiveIn_field3_0_bits_data),
    .io_OutLiveIn_field2_0_ready(Loop_0_io_OutLiveIn_field2_0_ready),
    .io_OutLiveIn_field2_0_valid(Loop_0_io_OutLiveIn_field2_0_valid),
    .io_OutLiveIn_field2_0_bits_data(Loop_0_io_OutLiveIn_field2_0_bits_data),
    .io_OutLiveIn_field1_0_ready(Loop_0_io_OutLiveIn_field1_0_ready),
    .io_OutLiveIn_field1_0_valid(Loop_0_io_OutLiveIn_field1_0_valid),
    .io_OutLiveIn_field1_0_bits_data(Loop_0_io_OutLiveIn_field1_0_bits_data),
    .io_OutLiveIn_field0_0_ready(Loop_0_io_OutLiveIn_field0_0_ready),
    .io_OutLiveIn_field0_0_valid(Loop_0_io_OutLiveIn_field0_0_valid),
    .io_OutLiveIn_field0_0_bits_data(Loop_0_io_OutLiveIn_field0_0_bits_data),
    .io_activate_loop_start_ready(Loop_0_io_activate_loop_start_ready),
    .io_activate_loop_start_valid(Loop_0_io_activate_loop_start_valid),
    .io_activate_loop_start_bits_control(Loop_0_io_activate_loop_start_bits_control),
    .io_activate_loop_back_ready(Loop_0_io_activate_loop_back_ready),
    .io_activate_loop_back_valid(Loop_0_io_activate_loop_back_valid),
    .io_activate_loop_back_bits_control(Loop_0_io_activate_loop_back_bits_control),
    .io_loopBack_0_ready(Loop_0_io_loopBack_0_ready),
    .io_loopBack_0_valid(Loop_0_io_loopBack_0_valid),
    .io_loopBack_0_bits_control(Loop_0_io_loopBack_0_bits_control),
    .io_loopFinish_0_ready(Loop_0_io_loopFinish_0_ready),
    .io_loopFinish_0_valid(Loop_0_io_loopFinish_0_valid),
    .io_loopFinish_0_bits_control(Loop_0_io_loopFinish_0_bits_control),
    .io_CarryDepenIn_0_ready(Loop_0_io_CarryDepenIn_0_ready),
    .io_CarryDepenIn_0_valid(Loop_0_io_CarryDepenIn_0_valid),
    .io_CarryDepenIn_0_bits_data(Loop_0_io_CarryDepenIn_0_bits_data),
    .io_CarryDepenOut_field0_0_ready(Loop_0_io_CarryDepenOut_field0_0_ready),
    .io_CarryDepenOut_field0_0_valid(Loop_0_io_CarryDepenOut_field0_0_valid),
    .io_CarryDepenOut_field0_0_bits_data(Loop_0_io_CarryDepenOut_field0_0_bits_data),
    .io_loopExit_0_ready(Loop_0_io_loopExit_0_ready),
    .io_loopExit_0_valid(Loop_0_io_loopExit_0_valid)
  );
  BasicBlockNoMaskFastNode bb_entry1 ( // @[saxpy.scala 57:25]
    .clock(bb_entry1_clock),
    .reset(bb_entry1_reset),
    .io_predicateIn_0_ready(bb_entry1_io_predicateIn_0_ready),
    .io_predicateIn_0_valid(bb_entry1_io_predicateIn_0_valid),
    .io_predicateIn_0_bits_control(bb_entry1_io_predicateIn_0_bits_control),
    .io_Out_0_ready(bb_entry1_io_Out_0_ready),
    .io_Out_0_valid(bb_entry1_io_Out_0_valid),
    .io_Out_1_ready(bb_entry1_io_Out_1_ready),
    .io_Out_1_valid(bb_entry1_io_Out_1_valid),
    .io_Out_1_bits_control(bb_entry1_io_Out_1_bits_control),
    .io_Out_2_ready(bb_entry1_io_Out_2_ready),
    .io_Out_2_valid(bb_entry1_io_Out_2_valid),
    .io_Out_2_bits_control(bb_entry1_io_Out_2_bits_control)
  );
  BasicBlockNoMaskFastNode_1 bb_for_body_lr_ph4 ( // @[saxpy.scala 59:34]
    .clock(bb_for_body_lr_ph4_clock),
    .reset(bb_for_body_lr_ph4_reset),
    .io_predicateIn_0_ready(bb_for_body_lr_ph4_io_predicateIn_0_ready),
    .io_predicateIn_0_valid(bb_for_body_lr_ph4_io_predicateIn_0_valid),
    .io_predicateIn_0_bits_control(bb_for_body_lr_ph4_io_predicateIn_0_bits_control),
    .io_Out_0_ready(bb_for_body_lr_ph4_io_Out_0_ready),
    .io_Out_0_valid(bb_for_body_lr_ph4_io_Out_0_valid),
    .io_Out_1_ready(bb_for_body_lr_ph4_io_Out_1_ready),
    .io_Out_1_valid(bb_for_body_lr_ph4_io_Out_1_valid),
    .io_Out_1_bits_control(bb_for_body_lr_ph4_io_Out_1_bits_control)
  );
  BasicBlockNoMaskFastNode_2 bb_for_cond_cleanup_loopexit7 ( // @[saxpy.scala 61:45]
    .clock(bb_for_cond_cleanup_loopexit7_clock),
    .reset(bb_for_cond_cleanup_loopexit7_reset),
    .io_predicateIn_0_ready(bb_for_cond_cleanup_loopexit7_io_predicateIn_0_ready),
    .io_predicateIn_0_valid(bb_for_cond_cleanup_loopexit7_io_predicateIn_0_valid),
    .io_Out_0_ready(bb_for_cond_cleanup_loopexit7_io_Out_0_ready),
    .io_Out_0_valid(bb_for_cond_cleanup_loopexit7_io_Out_0_valid)
  );
  BasicBlockNoMaskFastNode_3 bb_for_cond_cleanup9 ( // @[saxpy.scala 63:36]
    .clock(bb_for_cond_cleanup9_clock),
    .reset(bb_for_cond_cleanup9_reset),
    .io_predicateIn_0_ready(bb_for_cond_cleanup9_io_predicateIn_0_ready),
    .io_predicateIn_0_valid(bb_for_cond_cleanup9_io_predicateIn_0_valid),
    .io_predicateIn_1_ready(bb_for_cond_cleanup9_io_predicateIn_1_ready),
    .io_predicateIn_1_valid(bb_for_cond_cleanup9_io_predicateIn_1_valid),
    .io_Out_0_ready(bb_for_cond_cleanup9_io_Out_0_ready),
    .io_Out_0_valid(bb_for_cond_cleanup9_io_Out_0_valid),
    .io_Out_1_ready(bb_for_cond_cleanup9_io_Out_1_ready),
    .io_Out_1_valid(bb_for_cond_cleanup9_io_Out_1_valid)
  );
  BasicBlockNode bb_for_body11 ( // @[saxpy.scala 65:29]
    .clock(bb_for_body11_clock),
    .reset(bb_for_body11_reset),
    .io_MaskBB_0_ready(bb_for_body11_io_MaskBB_0_ready),
    .io_MaskBB_0_valid(bb_for_body11_io_MaskBB_0_valid),
    .io_MaskBB_0_bits(bb_for_body11_io_MaskBB_0_bits),
    .io_Out_0_ready(bb_for_body11_io_Out_0_ready),
    .io_Out_0_valid(bb_for_body11_io_Out_0_valid),
    .io_Out_1_ready(bb_for_body11_io_Out_1_ready),
    .io_Out_1_valid(bb_for_body11_io_Out_1_valid),
    .io_Out_2_ready(bb_for_body11_io_Out_2_ready),
    .io_Out_2_valid(bb_for_body11_io_Out_2_valid),
    .io_Out_2_bits_control(bb_for_body11_io_Out_2_bits_control),
    .io_Out_3_ready(bb_for_body11_io_Out_3_ready),
    .io_Out_3_valid(bb_for_body11_io_Out_3_valid),
    .io_Out_4_ready(bb_for_body11_io_Out_4_ready),
    .io_Out_4_valid(bb_for_body11_io_Out_4_valid),
    .io_Out_4_bits_control(bb_for_body11_io_Out_4_bits_control),
    .io_Out_5_ready(bb_for_body11_io_Out_5_ready),
    .io_Out_5_valid(bb_for_body11_io_Out_5_valid),
    .io_Out_5_bits_control(bb_for_body11_io_Out_5_bits_control),
    .io_Out_6_ready(bb_for_body11_io_Out_6_ready),
    .io_Out_6_valid(bb_for_body11_io_Out_6_valid),
    .io_Out_7_ready(bb_for_body11_io_Out_7_ready),
    .io_Out_7_valid(bb_for_body11_io_Out_7_valid),
    .io_Out_7_bits_control(bb_for_body11_io_Out_7_bits_control),
    .io_Out_8_ready(bb_for_body11_io_Out_8_ready),
    .io_Out_8_valid(bb_for_body11_io_Out_8_valid),
    .io_Out_8_bits_control(bb_for_body11_io_Out_8_bits_control),
    .io_Out_9_ready(bb_for_body11_io_Out_9_ready),
    .io_Out_9_valid(bb_for_body11_io_Out_9_valid),
    .io_Out_9_bits_control(bb_for_body11_io_Out_9_bits_control),
    .io_Out_10_ready(bb_for_body11_io_Out_10_ready),
    .io_Out_10_valid(bb_for_body11_io_Out_10_valid),
    .io_Out_10_bits_control(bb_for_body11_io_Out_10_bits_control),
    .io_Out_11_ready(bb_for_body11_io_Out_11_ready),
    .io_Out_11_valid(bb_for_body11_io_Out_11_valid),
    .io_Out_11_bits_control(bb_for_body11_io_Out_11_bits_control),
    .io_Out_12_ready(bb_for_body11_io_Out_12_ready),
    .io_Out_12_valid(bb_for_body11_io_Out_12_valid),
    .io_Out_12_bits_control(bb_for_body11_io_Out_12_bits_control),
    .io_predicateIn_0_ready(bb_for_body11_io_predicateIn_0_ready),
    .io_predicateIn_0_valid(bb_for_body11_io_predicateIn_0_valid),
    .io_predicateIn_0_bits_control(bb_for_body11_io_predicateIn_0_bits_control),
    .io_predicateIn_1_ready(bb_for_body11_io_predicateIn_1_ready),
    .io_predicateIn_1_valid(bb_for_body11_io_predicateIn_1_valid),
    .io_predicateIn_1_bits_control(bb_for_body11_io_predicateIn_1_bits_control)
  );
  ComputeNode icmp_cmp110 ( // @[saxpy.scala 74:27]
    .clock(icmp_cmp110_clock),
    .reset(icmp_cmp110_reset),
    .io_enable_ready(icmp_cmp110_io_enable_ready),
    .io_enable_valid(icmp_cmp110_io_enable_valid),
    .io_enable_bits_control(icmp_cmp110_io_enable_bits_control),
    .io_Out_0_ready(icmp_cmp110_io_Out_0_ready),
    .io_Out_0_valid(icmp_cmp110_io_Out_0_valid),
    .io_Out_0_bits_data(icmp_cmp110_io_Out_0_bits_data),
    .io_LeftIO_ready(icmp_cmp110_io_LeftIO_ready),
    .io_LeftIO_valid(icmp_cmp110_io_LeftIO_valid),
    .io_LeftIO_bits_data(icmp_cmp110_io_LeftIO_bits_data),
    .io_RightIO_ready(icmp_cmp110_io_RightIO_ready),
    .io_RightIO_valid(icmp_cmp110_io_RightIO_valid)
  );
  CBranchNodeVariable br_3 ( // @[saxpy.scala 77:20]
    .clock(br_3_clock),
    .reset(br_3_reset),
    .io_enable_ready(br_3_io_enable_ready),
    .io_enable_valid(br_3_io_enable_valid),
    .io_enable_bits_control(br_3_io_enable_bits_control),
    .io_CmpIO_ready(br_3_io_CmpIO_ready),
    .io_CmpIO_valid(br_3_io_CmpIO_valid),
    .io_CmpIO_bits_data(br_3_io_CmpIO_bits_data),
    .io_TrueOutput_0_ready(br_3_io_TrueOutput_0_ready),
    .io_TrueOutput_0_valid(br_3_io_TrueOutput_0_valid),
    .io_TrueOutput_0_bits_control(br_3_io_TrueOutput_0_bits_control),
    .io_FalseOutput_0_ready(br_3_io_FalseOutput_0_ready),
    .io_FalseOutput_0_valid(br_3_io_FalseOutput_0_valid)
  );
  ZextNode sextwide_trip_count5 ( // @[saxpy.scala 80:36]
    .clock(sextwide_trip_count5_clock),
    .reset(sextwide_trip_count5_reset),
    .io_Input_ready(sextwide_trip_count5_io_Input_ready),
    .io_Input_valid(sextwide_trip_count5_io_Input_valid),
    .io_Input_bits_data(sextwide_trip_count5_io_Input_bits_data),
    .io_enable_ready(sextwide_trip_count5_io_enable_ready),
    .io_enable_valid(sextwide_trip_count5_io_enable_valid),
    .io_Out_0_ready(sextwide_trip_count5_io_Out_0_ready),
    .io_Out_0_valid(sextwide_trip_count5_io_Out_0_valid),
    .io_Out_0_bits_data(sextwide_trip_count5_io_Out_0_bits_data)
  );
  UBranchNode br_6 ( // @[saxpy.scala 83:20]
    .clock(br_6_clock),
    .reset(br_6_reset),
    .io_enable_ready(br_6_io_enable_ready),
    .io_enable_valid(br_6_io_enable_valid),
    .io_enable_bits_control(br_6_io_enable_bits_control),
    .io_Out_0_ready(br_6_io_Out_0_ready),
    .io_Out_0_valid(br_6_io_Out_0_valid),
    .io_Out_0_bits_control(br_6_io_Out_0_bits_control)
  );
  UBranchNode_1 br_8 ( // @[saxpy.scala 86:20]
    .clock(br_8_clock),
    .reset(br_8_reset),
    .io_enable_ready(br_8_io_enable_ready),
    .io_enable_valid(br_8_io_enable_valid),
    .io_Out_0_ready(br_8_io_Out_0_ready),
    .io_Out_0_valid(br_8_io_Out_0_valid)
  );
  RetNode2 ret_10 ( // @[saxpy.scala 89:22]
    .clock(ret_10_clock),
    .reset(ret_10_reset),
    .io_In_enable_ready(ret_10_io_In_enable_ready),
    .io_In_enable_valid(ret_10_io_In_enable_valid),
    .io_In_data_field0_ready(ret_10_io_In_data_field0_ready),
    .io_In_data_field0_valid(ret_10_io_In_data_field0_valid),
    .io_Out_ready(ret_10_io_Out_ready),
    .io_Out_valid(ret_10_io_Out_valid)
  );
  PhiFastNode phiindvars_iv12 ( // @[saxpy.scala 92:31]
    .clock(phiindvars_iv12_clock),
    .reset(phiindvars_iv12_reset),
    .io_enable_ready(phiindvars_iv12_io_enable_ready),
    .io_enable_valid(phiindvars_iv12_io_enable_valid),
    .io_enable_bits_control(phiindvars_iv12_io_enable_bits_control),
    .io_InData_0_ready(phiindvars_iv12_io_InData_0_ready),
    .io_InData_0_valid(phiindvars_iv12_io_InData_0_valid),
    .io_InData_1_ready(phiindvars_iv12_io_InData_1_ready),
    .io_InData_1_valid(phiindvars_iv12_io_InData_1_valid),
    .io_InData_1_bits_data(phiindvars_iv12_io_InData_1_bits_data),
    .io_Mask_ready(phiindvars_iv12_io_Mask_ready),
    .io_Mask_valid(phiindvars_iv12_io_Mask_valid),
    .io_Mask_bits(phiindvars_iv12_io_Mask_bits),
    .io_Out_0_ready(phiindvars_iv12_io_Out_0_ready),
    .io_Out_0_valid(phiindvars_iv12_io_Out_0_valid),
    .io_Out_0_bits_data(phiindvars_iv12_io_Out_0_bits_data),
    .io_Out_1_ready(phiindvars_iv12_io_Out_1_ready),
    .io_Out_1_valid(phiindvars_iv12_io_Out_1_valid),
    .io_Out_1_bits_data(phiindvars_iv12_io_Out_1_bits_data),
    .io_Out_2_ready(phiindvars_iv12_io_Out_2_ready),
    .io_Out_2_valid(phiindvars_iv12_io_Out_2_valid),
    .io_Out_2_bits_data(phiindvars_iv12_io_Out_2_bits_data)
  );
  GepNode Gep_arrayidx13 ( // @[saxpy.scala 95:30]
    .clock(Gep_arrayidx13_clock),
    .reset(Gep_arrayidx13_reset),
    .io_enable_ready(Gep_arrayidx13_io_enable_ready),
    .io_enable_valid(Gep_arrayidx13_io_enable_valid),
    .io_Out_0_ready(Gep_arrayidx13_io_Out_0_ready),
    .io_Out_0_valid(Gep_arrayidx13_io_Out_0_valid),
    .io_Out_0_bits_data(Gep_arrayidx13_io_Out_0_bits_data),
    .io_baseAddress_ready(Gep_arrayidx13_io_baseAddress_ready),
    .io_baseAddress_valid(Gep_arrayidx13_io_baseAddress_valid),
    .io_baseAddress_bits_data(Gep_arrayidx13_io_baseAddress_bits_data),
    .io_idx_0_ready(Gep_arrayidx13_io_idx_0_ready),
    .io_idx_0_valid(Gep_arrayidx13_io_idx_0_valid),
    .io_idx_0_bits_data(Gep_arrayidx13_io_idx_0_bits_data)
  );
  UnTypLoadCache ld_14 ( // @[saxpy.scala 98:21]
    .clock(ld_14_clock),
    .reset(ld_14_reset),
    .io_enable_ready(ld_14_io_enable_ready),
    .io_enable_valid(ld_14_io_enable_valid),
    .io_enable_bits_control(ld_14_io_enable_bits_control),
    .io_Out_0_ready(ld_14_io_Out_0_ready),
    .io_Out_0_valid(ld_14_io_Out_0_valid),
    .io_Out_0_bits_data(ld_14_io_Out_0_bits_data),
    .io_GepAddr_ready(ld_14_io_GepAddr_ready),
    .io_GepAddr_valid(ld_14_io_GepAddr_valid),
    .io_GepAddr_bits_data(ld_14_io_GepAddr_bits_data),
    .io_MemReq_ready(ld_14_io_MemReq_ready),
    .io_MemReq_valid(ld_14_io_MemReq_valid),
    .io_MemReq_bits_addr(ld_14_io_MemReq_bits_addr),
    .io_MemResp_valid(ld_14_io_MemResp_valid),
    .io_MemResp_bits_data(ld_14_io_MemResp_bits_data)
  );
  ComputeNode_1 binaryOp_mul15 ( // @[saxpy.scala 101:30]
    .clock(binaryOp_mul15_clock),
    .reset(binaryOp_mul15_reset),
    .io_enable_ready(binaryOp_mul15_io_enable_ready),
    .io_enable_valid(binaryOp_mul15_io_enable_valid),
    .io_enable_bits_control(binaryOp_mul15_io_enable_bits_control),
    .io_Out_0_ready(binaryOp_mul15_io_Out_0_ready),
    .io_Out_0_valid(binaryOp_mul15_io_Out_0_valid),
    .io_Out_0_bits_data(binaryOp_mul15_io_Out_0_bits_data),
    .io_LeftIO_ready(binaryOp_mul15_io_LeftIO_ready),
    .io_LeftIO_valid(binaryOp_mul15_io_LeftIO_valid),
    .io_LeftIO_bits_data(binaryOp_mul15_io_LeftIO_bits_data),
    .io_RightIO_ready(binaryOp_mul15_io_RightIO_ready),
    .io_RightIO_valid(binaryOp_mul15_io_RightIO_valid),
    .io_RightIO_bits_data(binaryOp_mul15_io_RightIO_bits_data)
  );
  GepNode_1 Gep_arrayidx216 ( // @[saxpy.scala 104:31]
    .clock(Gep_arrayidx216_clock),
    .reset(Gep_arrayidx216_reset),
    .io_enable_ready(Gep_arrayidx216_io_enable_ready),
    .io_enable_valid(Gep_arrayidx216_io_enable_valid),
    .io_Out_0_ready(Gep_arrayidx216_io_Out_0_ready),
    .io_Out_0_valid(Gep_arrayidx216_io_Out_0_valid),
    .io_Out_0_bits_data(Gep_arrayidx216_io_Out_0_bits_data),
    .io_Out_1_ready(Gep_arrayidx216_io_Out_1_ready),
    .io_Out_1_valid(Gep_arrayidx216_io_Out_1_valid),
    .io_Out_1_bits_data(Gep_arrayidx216_io_Out_1_bits_data),
    .io_baseAddress_ready(Gep_arrayidx216_io_baseAddress_ready),
    .io_baseAddress_valid(Gep_arrayidx216_io_baseAddress_valid),
    .io_baseAddress_bits_data(Gep_arrayidx216_io_baseAddress_bits_data),
    .io_idx_0_ready(Gep_arrayidx216_io_idx_0_ready),
    .io_idx_0_valid(Gep_arrayidx216_io_idx_0_valid),
    .io_idx_0_bits_data(Gep_arrayidx216_io_idx_0_bits_data)
  );
  UnTypLoadCache_1 ld_17 ( // @[saxpy.scala 107:21]
    .clock(ld_17_clock),
    .reset(ld_17_reset),
    .io_enable_ready(ld_17_io_enable_ready),
    .io_enable_valid(ld_17_io_enable_valid),
    .io_enable_bits_control(ld_17_io_enable_bits_control),
    .io_Out_0_ready(ld_17_io_Out_0_ready),
    .io_Out_0_valid(ld_17_io_Out_0_valid),
    .io_Out_0_bits_data(ld_17_io_Out_0_bits_data),
    .io_GepAddr_ready(ld_17_io_GepAddr_ready),
    .io_GepAddr_valid(ld_17_io_GepAddr_valid),
    .io_GepAddr_bits_data(ld_17_io_GepAddr_bits_data),
    .io_MemReq_ready(ld_17_io_MemReq_ready),
    .io_MemReq_valid(ld_17_io_MemReq_valid),
    .io_MemReq_bits_addr(ld_17_io_MemReq_bits_addr),
    .io_MemResp_valid(ld_17_io_MemResp_valid),
    .io_MemResp_bits_data(ld_17_io_MemResp_bits_data)
  );
  ComputeNode_2 binaryOp_add18 ( // @[saxpy.scala 110:30]
    .clock(binaryOp_add18_clock),
    .reset(binaryOp_add18_reset),
    .io_enable_ready(binaryOp_add18_io_enable_ready),
    .io_enable_valid(binaryOp_add18_io_enable_valid),
    .io_enable_bits_control(binaryOp_add18_io_enable_bits_control),
    .io_Out_0_ready(binaryOp_add18_io_Out_0_ready),
    .io_Out_0_valid(binaryOp_add18_io_Out_0_valid),
    .io_Out_0_bits_data(binaryOp_add18_io_Out_0_bits_data),
    .io_LeftIO_ready(binaryOp_add18_io_LeftIO_ready),
    .io_LeftIO_valid(binaryOp_add18_io_LeftIO_valid),
    .io_LeftIO_bits_data(binaryOp_add18_io_LeftIO_bits_data),
    .io_RightIO_ready(binaryOp_add18_io_RightIO_ready),
    .io_RightIO_valid(binaryOp_add18_io_RightIO_valid),
    .io_RightIO_bits_data(binaryOp_add18_io_RightIO_bits_data)
  );
  UnTypStoreCache st_19 ( // @[saxpy.scala 113:21]
    .clock(st_19_clock),
    .reset(st_19_reset),
    .io_enable_ready(st_19_io_enable_ready),
    .io_enable_valid(st_19_io_enable_valid),
    .io_enable_bits_control(st_19_io_enable_bits_control),
    .io_SuccOp_0_ready(st_19_io_SuccOp_0_ready),
    .io_SuccOp_0_valid(st_19_io_SuccOp_0_valid),
    .io_GepAddr_ready(st_19_io_GepAddr_ready),
    .io_GepAddr_valid(st_19_io_GepAddr_valid),
    .io_GepAddr_bits_data(st_19_io_GepAddr_bits_data),
    .io_inData_ready(st_19_io_inData_ready),
    .io_inData_valid(st_19_io_inData_valid),
    .io_inData_bits_data(st_19_io_inData_bits_data),
    .io_MemReq_ready(st_19_io_MemReq_ready),
    .io_MemReq_valid(st_19_io_MemReq_valid),
    .io_MemReq_bits_addr(st_19_io_MemReq_bits_addr),
    .io_MemReq_bits_data(st_19_io_MemReq_bits_data),
    .io_MemResp_valid(st_19_io_MemResp_valid)
  );
  ComputeNode_3 binaryOp_indvars_iv_next20 ( // @[saxpy.scala 116:42]
    .clock(binaryOp_indvars_iv_next20_clock),
    .reset(binaryOp_indvars_iv_next20_reset),
    .io_enable_ready(binaryOp_indvars_iv_next20_io_enable_ready),
    .io_enable_valid(binaryOp_indvars_iv_next20_io_enable_valid),
    .io_enable_bits_control(binaryOp_indvars_iv_next20_io_enable_bits_control),
    .io_Out_0_ready(binaryOp_indvars_iv_next20_io_Out_0_ready),
    .io_Out_0_valid(binaryOp_indvars_iv_next20_io_Out_0_valid),
    .io_Out_0_bits_data(binaryOp_indvars_iv_next20_io_Out_0_bits_data),
    .io_Out_1_ready(binaryOp_indvars_iv_next20_io_Out_1_ready),
    .io_Out_1_valid(binaryOp_indvars_iv_next20_io_Out_1_valid),
    .io_Out_1_bits_data(binaryOp_indvars_iv_next20_io_Out_1_bits_data),
    .io_LeftIO_ready(binaryOp_indvars_iv_next20_io_LeftIO_ready),
    .io_LeftIO_valid(binaryOp_indvars_iv_next20_io_LeftIO_valid),
    .io_LeftIO_bits_data(binaryOp_indvars_iv_next20_io_LeftIO_bits_data),
    .io_RightIO_ready(binaryOp_indvars_iv_next20_io_RightIO_ready),
    .io_RightIO_valid(binaryOp_indvars_iv_next20_io_RightIO_valid)
  );
  ComputeNode_4 icmp_exitcond15 ( // @[saxpy.scala 119:31]
    .clock(icmp_exitcond15_clock),
    .reset(icmp_exitcond15_reset),
    .io_enable_ready(icmp_exitcond15_io_enable_ready),
    .io_enable_valid(icmp_exitcond15_io_enable_valid),
    .io_enable_bits_control(icmp_exitcond15_io_enable_bits_control),
    .io_Out_0_ready(icmp_exitcond15_io_Out_0_ready),
    .io_Out_0_valid(icmp_exitcond15_io_Out_0_valid),
    .io_Out_0_bits_data(icmp_exitcond15_io_Out_0_bits_data),
    .io_LeftIO_ready(icmp_exitcond15_io_LeftIO_ready),
    .io_LeftIO_valid(icmp_exitcond15_io_LeftIO_valid),
    .io_LeftIO_bits_data(icmp_exitcond15_io_LeftIO_bits_data),
    .io_RightIO_ready(icmp_exitcond15_io_RightIO_ready),
    .io_RightIO_valid(icmp_exitcond15_io_RightIO_valid),
    .io_RightIO_bits_data(icmp_exitcond15_io_RightIO_bits_data)
  );
  CBranchNodeVariable_1 br_22 ( // @[saxpy.scala 122:21]
    .clock(br_22_clock),
    .reset(br_22_reset),
    .io_enable_ready(br_22_io_enable_ready),
    .io_enable_valid(br_22_io_enable_valid),
    .io_enable_bits_control(br_22_io_enable_bits_control),
    .io_CmpIO_ready(br_22_io_CmpIO_ready),
    .io_CmpIO_valid(br_22_io_CmpIO_valid),
    .io_CmpIO_bits_data(br_22_io_CmpIO_bits_data),
    .io_PredOp_0_ready(br_22_io_PredOp_0_ready),
    .io_PredOp_0_valid(br_22_io_PredOp_0_valid),
    .io_TrueOutput_0_ready(br_22_io_TrueOutput_0_ready),
    .io_TrueOutput_0_valid(br_22_io_TrueOutput_0_valid),
    .io_TrueOutput_0_bits_control(br_22_io_TrueOutput_0_bits_control),
    .io_FalseOutput_0_ready(br_22_io_FalseOutput_0_ready),
    .io_FalseOutput_0_valid(br_22_io_FalseOutput_0_valid),
    .io_FalseOutput_0_bits_control(br_22_io_FalseOutput_0_bits_control)
  );
  ConstFastNode const0 ( // @[saxpy.scala 131:22]
    .clock(const0_clock),
    .reset(const0_reset),
    .io_enable_ready(const0_io_enable_ready),
    .io_enable_valid(const0_io_enable_valid),
    .io_Out_ready(const0_io_Out_ready),
    .io_Out_valid(const0_io_Out_valid)
  );
  ConstFastNode_1 const1 ( // @[saxpy.scala 134:22]
    .clock(const1_clock),
    .reset(const1_reset),
    .io_enable_ready(const1_io_enable_ready),
    .io_enable_valid(const1_io_enable_valid),
    .io_Out_ready(const1_io_Out_ready),
    .io_Out_valid(const1_io_Out_valid)
  );
  ConstFastNode const2 ( // @[saxpy.scala 137:22]
    .clock(const2_clock),
    .reset(const2_reset),
    .io_enable_ready(const2_io_enable_ready),
    .io_enable_valid(const2_io_enable_valid),
    .io_Out_ready(const2_io_Out_ready),
    .io_Out_valid(const2_io_Out_valid)
  );
  ConstFastNode_1 const3 ( // @[saxpy.scala 140:22]
    .clock(const3_clock),
    .reset(const3_reset),
    .io_enable_ready(const3_io_enable_ready),
    .io_enable_valid(const3_io_enable_valid),
    .io_Out_ready(const3_io_Out_ready),
    .io_Out_valid(const3_io_Out_valid)
  );
  assign io_in_ready = ArgSplitter_io_In_ready; // @[saxpy.scala 41:21]
  assign io_MemReq_valid = mem_ctrl_cache_io_cache_MemReq_valid; // @[saxpy.scala 37:13]
  assign io_MemReq_bits_addr = mem_ctrl_cache_io_cache_MemReq_bits_addr; // @[saxpy.scala 37:13]
  assign io_MemReq_bits_data = mem_ctrl_cache_io_cache_MemReq_bits_data; // @[saxpy.scala 37:13]
  assign io_MemReq_bits_mask = mem_ctrl_cache_io_cache_MemReq_bits_mask; // @[saxpy.scala 37:13]
  assign io_MemReq_bits_tag = mem_ctrl_cache_io_cache_MemReq_bits_tag; // @[saxpy.scala 37:13]
  assign io_out_valid = ret_10_io_Out_valid; // @[saxpy.scala 400:10]
  assign mem_ctrl_cache_clock = clock;
  assign mem_ctrl_cache_reset = reset;
  assign mem_ctrl_cache_io_rd_mem_0_MemReq_valid = ld_14_io_MemReq_valid; // @[saxpy.scala 327:38]
  assign mem_ctrl_cache_io_rd_mem_0_MemReq_bits_addr = ld_14_io_MemReq_bits_addr; // @[saxpy.scala 327:38]
  assign mem_ctrl_cache_io_rd_mem_1_MemReq_valid = ld_17_io_MemReq_valid; // @[saxpy.scala 329:38]
  assign mem_ctrl_cache_io_rd_mem_1_MemReq_bits_addr = ld_17_io_MemReq_bits_addr; // @[saxpy.scala 329:38]
  assign mem_ctrl_cache_io_wr_mem_0_MemReq_valid = st_19_io_MemReq_valid; // @[saxpy.scala 331:38]
  assign mem_ctrl_cache_io_wr_mem_0_MemReq_bits_addr = st_19_io_MemReq_bits_addr; // @[saxpy.scala 331:38]
  assign mem_ctrl_cache_io_wr_mem_0_MemReq_bits_data = st_19_io_MemReq_bits_data; // @[saxpy.scala 331:38]
  assign mem_ctrl_cache_io_cache_MemReq_ready = io_MemReq_ready; // @[saxpy.scala 37:13]
  assign mem_ctrl_cache_io_cache_MemResp_valid = io_MemResp_valid; // @[saxpy.scala 38:35]
  assign mem_ctrl_cache_io_cache_MemResp_bits_data = io_MemResp_bits_data; // @[saxpy.scala 38:35]
  assign mem_ctrl_cache_io_cache_MemResp_bits_tag = io_MemResp_bits_tag; // @[saxpy.scala 38:35]
  assign ArgSplitter_clock = clock;
  assign ArgSplitter_reset = reset;
  assign ArgSplitter_io_In_valid = io_in_valid; // @[saxpy.scala 41:21]
  assign ArgSplitter_io_In_bits_dataPtrs_field1_data = io_in_bits_dataPtrs_field1_data; // @[saxpy.scala 41:21]
  assign ArgSplitter_io_In_bits_dataPtrs_field0_data = io_in_bits_dataPtrs_field0_data; // @[saxpy.scala 41:21]
  assign ArgSplitter_io_In_bits_dataVals_field1_data = io_in_bits_dataVals_field1_data; // @[saxpy.scala 41:21]
  assign ArgSplitter_io_In_bits_dataVals_field0_data = io_in_bits_dataVals_field0_data; // @[saxpy.scala 41:21]
  assign ArgSplitter_io_Out_enable_ready = bb_entry1_io_predicateIn_0_ready; // @[saxpy.scala 148:31]
  assign ArgSplitter_io_Out_dataPtrs_field1_0_ready = Loop_0_io_InLiveIn_2_ready; // @[saxpy.scala 202:25]
  assign ArgSplitter_io_Out_dataPtrs_field0_0_ready = Loop_0_io_InLiveIn_0_ready; // @[saxpy.scala 198:25]
  assign ArgSplitter_io_Out_dataVals_field1_0_ready = Loop_0_io_InLiveIn_1_ready; // @[saxpy.scala 200:25]
  assign ArgSplitter_io_Out_dataVals_field0_0_ready = icmp_cmp110_io_LeftIO_ready; // @[saxpy.scala 380:25]
  assign ArgSplitter_io_Out_dataVals_field0_1_ready = sextwide_trip_count5_io_Input_ready; // @[saxpy.scala 382:33]
  assign Loop_0_clock = clock;
  assign Loop_0_reset = reset;
  assign Loop_0_io_enable_valid = br_6_io_Out_0_valid; // @[saxpy.scala 180:20]
  assign Loop_0_io_enable_bits_control = br_6_io_Out_0_bits_control; // @[saxpy.scala 180:20]
  assign Loop_0_io_InLiveIn_0_valid = ArgSplitter_io_Out_dataPtrs_field0_0_valid; // @[saxpy.scala 198:25]
  assign Loop_0_io_InLiveIn_0_bits_data = ArgSplitter_io_Out_dataPtrs_field0_0_bits_data; // @[saxpy.scala 198:25]
  assign Loop_0_io_InLiveIn_1_valid = ArgSplitter_io_Out_dataVals_field1_0_valid; // @[saxpy.scala 200:25]
  assign Loop_0_io_InLiveIn_1_bits_data = ArgSplitter_io_Out_dataVals_field1_0_bits_data; // @[saxpy.scala 200:25]
  assign Loop_0_io_InLiveIn_2_valid = ArgSplitter_io_Out_dataPtrs_field1_0_valid; // @[saxpy.scala 202:25]
  assign Loop_0_io_InLiveIn_2_bits_data = ArgSplitter_io_Out_dataPtrs_field1_0_bits_data; // @[saxpy.scala 202:25]
  assign Loop_0_io_InLiveIn_3_valid = sextwide_trip_count5_io_Out_0_valid; // @[saxpy.scala 204:25]
  assign Loop_0_io_InLiveIn_3_bits_data = sextwide_trip_count5_io_Out_0_bits_data; // @[saxpy.scala 204:25]
  assign Loop_0_io_OutLiveIn_field3_0_ready = icmp_exitcond15_io_RightIO_ready; // @[saxpy.scala 218:30]
  assign Loop_0_io_OutLiveIn_field2_0_ready = Gep_arrayidx216_io_baseAddress_ready; // @[saxpy.scala 216:34]
  assign Loop_0_io_OutLiveIn_field1_0_ready = binaryOp_mul15_io_RightIO_ready; // @[saxpy.scala 214:29]
  assign Loop_0_io_OutLiveIn_field0_0_ready = Gep_arrayidx13_io_baseAddress_ready; // @[saxpy.scala 212:33]
  assign Loop_0_io_activate_loop_start_ready = bb_for_body11_io_predicateIn_1_ready; // @[saxpy.scala 164:35]
  assign Loop_0_io_activate_loop_back_ready = bb_for_body11_io_predicateIn_0_ready; // @[saxpy.scala 166:35]
  assign Loop_0_io_loopBack_0_valid = br_22_io_FalseOutput_0_valid; // @[saxpy.scala 182:25]
  assign Loop_0_io_loopBack_0_bits_control = br_22_io_FalseOutput_0_bits_control; // @[saxpy.scala 182:25]
  assign Loop_0_io_loopFinish_0_valid = br_22_io_TrueOutput_0_valid; // @[saxpy.scala 184:27]
  assign Loop_0_io_loopFinish_0_bits_control = br_22_io_TrueOutput_0_bits_control; // @[saxpy.scala 184:27]
  assign Loop_0_io_CarryDepenIn_0_valid = binaryOp_indvars_iv_next20_io_Out_0_valid; // @[saxpy.scala 238:29]
  assign Loop_0_io_CarryDepenIn_0_bits_data = binaryOp_indvars_iv_next20_io_Out_0_bits_data; // @[saxpy.scala 238:29]
  assign Loop_0_io_CarryDepenOut_field0_0_ready = phiindvars_iv12_io_InData_1_ready; // @[saxpy.scala 246:32]
  assign Loop_0_io_loopExit_0_ready = bb_for_cond_cleanup_loopexit7_io_predicateIn_0_ready; // @[saxpy.scala 162:51]
  assign bb_entry1_clock = clock;
  assign bb_entry1_reset = reset;
  assign bb_entry1_io_predicateIn_0_valid = ArgSplitter_io_Out_enable_valid; // @[saxpy.scala 148:31]
  assign bb_entry1_io_predicateIn_0_bits_control = ArgSplitter_io_Out_enable_bits_control; // @[saxpy.scala 148:31]
  assign bb_entry1_io_Out_0_ready = const0_io_enable_ready; // @[saxpy.scala 254:20]
  assign bb_entry1_io_Out_1_ready = icmp_cmp110_io_enable_ready; // @[saxpy.scala 256:25]
  assign bb_entry1_io_Out_2_ready = br_3_io_enable_ready; // @[saxpy.scala 259:18]
  assign bb_for_body_lr_ph4_clock = clock;
  assign bb_for_body_lr_ph4_reset = reset;
  assign bb_for_body_lr_ph4_io_predicateIn_0_valid = br_3_io_TrueOutput_0_valid; // @[saxpy.scala 150:40]
  assign bb_for_body_lr_ph4_io_predicateIn_0_bits_control = br_3_io_TrueOutput_0_bits_control; // @[saxpy.scala 150:40]
  assign bb_for_body_lr_ph4_io_Out_0_ready = sextwide_trip_count5_io_enable_ready; // @[saxpy.scala 262:34]
  assign bb_for_body_lr_ph4_io_Out_1_ready = br_6_io_enable_ready; // @[saxpy.scala 265:18]
  assign bb_for_cond_cleanup_loopexit7_clock = clock;
  assign bb_for_cond_cleanup_loopexit7_reset = reset;
  assign bb_for_cond_cleanup_loopexit7_io_predicateIn_0_valid = Loop_0_io_loopExit_0_valid; // @[saxpy.scala 162:51]
  assign bb_for_cond_cleanup_loopexit7_io_Out_0_ready = br_8_io_enable_ready; // @[saxpy.scala 268:18]
  assign bb_for_cond_cleanup9_clock = clock;
  assign bb_for_cond_cleanup9_reset = reset;
  assign bb_for_cond_cleanup9_io_predicateIn_0_valid = br_8_io_Out_0_valid; // @[saxpy.scala 154:42]
  assign bb_for_cond_cleanup9_io_predicateIn_1_valid = br_3_io_FalseOutput_0_valid; // @[saxpy.scala 152:42]
  assign bb_for_cond_cleanup9_io_Out_0_ready = const1_io_enable_ready; // @[saxpy.scala 271:20]
  assign bb_for_cond_cleanup9_io_Out_1_ready = ret_10_io_In_enable_ready; // @[saxpy.scala 273:23]
  assign bb_for_body11_clock = clock;
  assign bb_for_body11_reset = reset;
  assign bb_for_body11_io_MaskBB_0_ready = phiindvars_iv12_io_Mask_ready; // @[saxpy.scala 319:27]
  assign bb_for_body11_io_Out_0_ready = const2_io_enable_ready; // @[saxpy.scala 276:20]
  assign bb_for_body11_io_Out_1_ready = const3_io_enable_ready; // @[saxpy.scala 278:20]
  assign bb_for_body11_io_Out_2_ready = phiindvars_iv12_io_enable_ready; // @[saxpy.scala 280:29]
  assign bb_for_body11_io_Out_3_ready = Gep_arrayidx13_io_enable_ready; // @[saxpy.scala 283:28]
  assign bb_for_body11_io_Out_4_ready = ld_14_io_enable_ready; // @[saxpy.scala 286:19]
  assign bb_for_body11_io_Out_5_ready = binaryOp_mul15_io_enable_ready; // @[saxpy.scala 289:28]
  assign bb_for_body11_io_Out_6_ready = Gep_arrayidx216_io_enable_ready; // @[saxpy.scala 292:29]
  assign bb_for_body11_io_Out_7_ready = ld_17_io_enable_ready; // @[saxpy.scala 295:19]
  assign bb_for_body11_io_Out_8_ready = binaryOp_add18_io_enable_ready; // @[saxpy.scala 298:28]
  assign bb_for_body11_io_Out_9_ready = st_19_io_enable_ready; // @[saxpy.scala 301:19]
  assign bb_for_body11_io_Out_10_ready = binaryOp_indvars_iv_next20_io_enable_ready; // @[saxpy.scala 304:40]
  assign bb_for_body11_io_Out_11_ready = icmp_exitcond15_io_enable_ready; // @[saxpy.scala 307:29]
  assign bb_for_body11_io_Out_12_ready = br_22_io_enable_ready; // @[saxpy.scala 310:19]
  assign bb_for_body11_io_predicateIn_0_valid = Loop_0_io_activate_loop_back_valid; // @[saxpy.scala 166:35]
  assign bb_for_body11_io_predicateIn_0_bits_control = Loop_0_io_activate_loop_back_bits_control; // @[saxpy.scala 166:35]
  assign bb_for_body11_io_predicateIn_1_valid = Loop_0_io_activate_loop_start_valid; // @[saxpy.scala 164:35]
  assign bb_for_body11_io_predicateIn_1_bits_control = Loop_0_io_activate_loop_start_bits_control; // @[saxpy.scala 164:35]
  assign icmp_cmp110_clock = clock;
  assign icmp_cmp110_reset = reset;
  assign icmp_cmp110_io_enable_valid = bb_entry1_io_Out_1_valid; // @[saxpy.scala 256:25]
  assign icmp_cmp110_io_enable_bits_control = bb_entry1_io_Out_1_bits_control; // @[saxpy.scala 256:25]
  assign icmp_cmp110_io_Out_0_ready = br_3_io_CmpIO_ready; // @[saxpy.scala 354:17]
  assign icmp_cmp110_io_LeftIO_valid = ArgSplitter_io_Out_dataVals_field0_0_valid; // @[saxpy.scala 380:25]
  assign icmp_cmp110_io_LeftIO_bits_data = ArgSplitter_io_Out_dataVals_field0_0_bits_data; // @[saxpy.scala 380:25]
  assign icmp_cmp110_io_RightIO_valid = const0_io_Out_valid; // @[saxpy.scala 346:26]
  assign br_3_clock = clock;
  assign br_3_reset = reset;
  assign br_3_io_enable_valid = bb_entry1_io_Out_2_valid; // @[saxpy.scala 259:18]
  assign br_3_io_enable_bits_control = bb_entry1_io_Out_2_bits_control; // @[saxpy.scala 259:18]
  assign br_3_io_CmpIO_valid = icmp_cmp110_io_Out_0_valid; // @[saxpy.scala 354:17]
  assign br_3_io_CmpIO_bits_data = icmp_cmp110_io_Out_0_bits_data; // @[saxpy.scala 354:17]
  assign br_3_io_TrueOutput_0_ready = bb_for_body_lr_ph4_io_predicateIn_0_ready; // @[saxpy.scala 150:40]
  assign br_3_io_FalseOutput_0_ready = bb_for_cond_cleanup9_io_predicateIn_1_ready; // @[saxpy.scala 152:42]
  assign sextwide_trip_count5_clock = clock;
  assign sextwide_trip_count5_reset = reset;
  assign sextwide_trip_count5_io_Input_valid = ArgSplitter_io_Out_dataVals_field0_1_valid; // @[saxpy.scala 382:33]
  assign sextwide_trip_count5_io_Input_bits_data = ArgSplitter_io_Out_dataVals_field0_1_bits_data; // @[saxpy.scala 382:33]
  assign sextwide_trip_count5_io_enable_valid = bb_for_body_lr_ph4_io_Out_0_valid; // @[saxpy.scala 262:34]
  assign sextwide_trip_count5_io_Out_0_ready = Loop_0_io_InLiveIn_3_ready; // @[saxpy.scala 204:25]
  assign br_6_clock = clock;
  assign br_6_reset = reset;
  assign br_6_io_enable_valid = bb_for_body_lr_ph4_io_Out_1_valid; // @[saxpy.scala 265:18]
  assign br_6_io_enable_bits_control = bb_for_body_lr_ph4_io_Out_1_bits_control; // @[saxpy.scala 265:18]
  assign br_6_io_Out_0_ready = Loop_0_io_enable_ready; // @[saxpy.scala 180:20]
  assign br_8_clock = clock;
  assign br_8_reset = reset;
  assign br_8_io_enable_valid = bb_for_cond_cleanup_loopexit7_io_Out_0_valid; // @[saxpy.scala 268:18]
  assign br_8_io_Out_0_ready = bb_for_cond_cleanup9_io_predicateIn_0_ready; // @[saxpy.scala 154:42]
  assign ret_10_clock = clock;
  assign ret_10_reset = reset;
  assign ret_10_io_In_enable_valid = bb_for_cond_cleanup9_io_Out_1_valid; // @[saxpy.scala 273:23]
  assign ret_10_io_In_data_field0_valid = const1_io_Out_valid; // @[saxpy.scala 348:31]
  assign ret_10_io_Out_ready = io_out_ready; // @[saxpy.scala 400:10]
  assign phiindvars_iv12_clock = clock;
  assign phiindvars_iv12_reset = reset;
  assign phiindvars_iv12_io_enable_valid = bb_for_body11_io_Out_2_valid; // @[saxpy.scala 280:29]
  assign phiindvars_iv12_io_enable_bits_control = bb_for_body11_io_Out_2_bits_control; // @[saxpy.scala 280:29]
  assign phiindvars_iv12_io_InData_0_valid = const2_io_Out_valid; // @[saxpy.scala 350:32]
  assign phiindvars_iv12_io_InData_1_valid = Loop_0_io_CarryDepenOut_field0_0_valid; // @[saxpy.scala 246:32]
  assign phiindvars_iv12_io_InData_1_bits_data = Loop_0_io_CarryDepenOut_field0_0_bits_data; // @[saxpy.scala 246:32]
  assign phiindvars_iv12_io_Mask_valid = bb_for_body11_io_MaskBB_0_valid; // @[saxpy.scala 319:27]
  assign phiindvars_iv12_io_Mask_bits = bb_for_body11_io_MaskBB_0_bits; // @[saxpy.scala 319:27]
  assign phiindvars_iv12_io_Out_0_ready = Gep_arrayidx13_io_idx_0_ready; // @[saxpy.scala 356:28]
  assign phiindvars_iv12_io_Out_1_ready = Gep_arrayidx216_io_idx_0_ready; // @[saxpy.scala 358:29]
  assign phiindvars_iv12_io_Out_2_ready = binaryOp_indvars_iv_next20_io_LeftIO_ready; // @[saxpy.scala 360:40]
  assign Gep_arrayidx13_clock = clock;
  assign Gep_arrayidx13_reset = reset;
  assign Gep_arrayidx13_io_enable_valid = bb_for_body11_io_Out_3_valid; // @[saxpy.scala 283:28]
  assign Gep_arrayidx13_io_Out_0_ready = ld_14_io_GepAddr_ready; // @[saxpy.scala 362:20]
  assign Gep_arrayidx13_io_baseAddress_valid = Loop_0_io_OutLiveIn_field0_0_valid; // @[saxpy.scala 212:33]
  assign Gep_arrayidx13_io_baseAddress_bits_data = Loop_0_io_OutLiveIn_field0_0_bits_data; // @[saxpy.scala 212:33]
  assign Gep_arrayidx13_io_idx_0_valid = phiindvars_iv12_io_Out_0_valid; // @[saxpy.scala 356:28]
  assign Gep_arrayidx13_io_idx_0_bits_data = phiindvars_iv12_io_Out_0_bits_data; // @[saxpy.scala 356:28]
  assign ld_14_clock = clock;
  assign ld_14_reset = reset;
  assign ld_14_io_enable_valid = bb_for_body11_io_Out_4_valid; // @[saxpy.scala 286:19]
  assign ld_14_io_enable_bits_control = bb_for_body11_io_Out_4_bits_control; // @[saxpy.scala 286:19]
  assign ld_14_io_Out_0_ready = binaryOp_mul15_io_LeftIO_ready; // @[saxpy.scala 364:28]
  assign ld_14_io_GepAddr_valid = Gep_arrayidx13_io_Out_0_valid; // @[saxpy.scala 362:20]
  assign ld_14_io_GepAddr_bits_data = Gep_arrayidx13_io_Out_0_bits_data; // @[saxpy.scala 362:20]
  assign ld_14_io_MemReq_ready = mem_ctrl_cache_io_rd_mem_0_MemReq_ready; // @[saxpy.scala 327:38]
  assign ld_14_io_MemResp_valid = mem_ctrl_cache_io_rd_mem_0_MemResp_valid; // @[saxpy.scala 328:20]
  assign ld_14_io_MemResp_bits_data = mem_ctrl_cache_io_rd_mem_0_MemResp_bits_data; // @[saxpy.scala 328:20]
  assign binaryOp_mul15_clock = clock;
  assign binaryOp_mul15_reset = reset;
  assign binaryOp_mul15_io_enable_valid = bb_for_body11_io_Out_5_valid; // @[saxpy.scala 289:28]
  assign binaryOp_mul15_io_enable_bits_control = bb_for_body11_io_Out_5_bits_control; // @[saxpy.scala 289:28]
  assign binaryOp_mul15_io_Out_0_ready = binaryOp_add18_io_LeftIO_ready; // @[saxpy.scala 366:28]
  assign binaryOp_mul15_io_LeftIO_valid = ld_14_io_Out_0_valid; // @[saxpy.scala 364:28]
  assign binaryOp_mul15_io_LeftIO_bits_data = ld_14_io_Out_0_bits_data; // @[saxpy.scala 364:28]
  assign binaryOp_mul15_io_RightIO_valid = Loop_0_io_OutLiveIn_field1_0_valid; // @[saxpy.scala 214:29]
  assign binaryOp_mul15_io_RightIO_bits_data = Loop_0_io_OutLiveIn_field1_0_bits_data; // @[saxpy.scala 214:29]
  assign Gep_arrayidx216_clock = clock;
  assign Gep_arrayidx216_reset = reset;
  assign Gep_arrayidx216_io_enable_valid = bb_for_body11_io_Out_6_valid; // @[saxpy.scala 292:29]
  assign Gep_arrayidx216_io_Out_0_ready = ld_17_io_GepAddr_ready; // @[saxpy.scala 368:20]
  assign Gep_arrayidx216_io_Out_1_ready = st_19_io_GepAddr_ready; // @[saxpy.scala 370:20]
  assign Gep_arrayidx216_io_baseAddress_valid = Loop_0_io_OutLiveIn_field2_0_valid; // @[saxpy.scala 216:34]
  assign Gep_arrayidx216_io_baseAddress_bits_data = Loop_0_io_OutLiveIn_field2_0_bits_data; // @[saxpy.scala 216:34]
  assign Gep_arrayidx216_io_idx_0_valid = phiindvars_iv12_io_Out_1_valid; // @[saxpy.scala 358:29]
  assign Gep_arrayidx216_io_idx_0_bits_data = phiindvars_iv12_io_Out_1_bits_data; // @[saxpy.scala 358:29]
  assign ld_17_clock = clock;
  assign ld_17_reset = reset;
  assign ld_17_io_enable_valid = bb_for_body11_io_Out_7_valid; // @[saxpy.scala 295:19]
  assign ld_17_io_enable_bits_control = bb_for_body11_io_Out_7_bits_control; // @[saxpy.scala 295:19]
  assign ld_17_io_Out_0_ready = binaryOp_add18_io_RightIO_ready; // @[saxpy.scala 372:29]
  assign ld_17_io_GepAddr_valid = Gep_arrayidx216_io_Out_0_valid; // @[saxpy.scala 368:20]
  assign ld_17_io_GepAddr_bits_data = Gep_arrayidx216_io_Out_0_bits_data; // @[saxpy.scala 368:20]
  assign ld_17_io_MemReq_ready = mem_ctrl_cache_io_rd_mem_1_MemReq_ready; // @[saxpy.scala 329:38]
  assign ld_17_io_MemResp_valid = mem_ctrl_cache_io_rd_mem_1_MemResp_valid; // @[saxpy.scala 330:20]
  assign ld_17_io_MemResp_bits_data = mem_ctrl_cache_io_rd_mem_1_MemResp_bits_data; // @[saxpy.scala 330:20]
  assign binaryOp_add18_clock = clock;
  assign binaryOp_add18_reset = reset;
  assign binaryOp_add18_io_enable_valid = bb_for_body11_io_Out_8_valid; // @[saxpy.scala 298:28]
  assign binaryOp_add18_io_enable_bits_control = bb_for_body11_io_Out_8_bits_control; // @[saxpy.scala 298:28]
  assign binaryOp_add18_io_Out_0_ready = st_19_io_inData_ready; // @[saxpy.scala 374:19]
  assign binaryOp_add18_io_LeftIO_valid = binaryOp_mul15_io_Out_0_valid; // @[saxpy.scala 366:28]
  assign binaryOp_add18_io_LeftIO_bits_data = binaryOp_mul15_io_Out_0_bits_data; // @[saxpy.scala 366:28]
  assign binaryOp_add18_io_RightIO_valid = ld_17_io_Out_0_valid; // @[saxpy.scala 372:29]
  assign binaryOp_add18_io_RightIO_bits_data = ld_17_io_Out_0_bits_data; // @[saxpy.scala 372:29]
  assign st_19_clock = clock;
  assign st_19_reset = reset;
  assign st_19_io_enable_valid = bb_for_body11_io_Out_9_valid; // @[saxpy.scala 301:19]
  assign st_19_io_enable_bits_control = bb_for_body11_io_Out_9_bits_control; // @[saxpy.scala 301:19]
  assign st_19_io_SuccOp_0_ready = br_22_io_PredOp_0_ready; // @[saxpy.scala 392:22]
  assign st_19_io_GepAddr_valid = Gep_arrayidx216_io_Out_1_valid; // @[saxpy.scala 370:20]
  assign st_19_io_GepAddr_bits_data = Gep_arrayidx216_io_Out_1_bits_data; // @[saxpy.scala 370:20]
  assign st_19_io_inData_valid = binaryOp_add18_io_Out_0_valid; // @[saxpy.scala 374:19]
  assign st_19_io_inData_bits_data = binaryOp_add18_io_Out_0_bits_data; // @[saxpy.scala 374:19]
  assign st_19_io_MemReq_ready = mem_ctrl_cache_io_wr_mem_0_MemReq_ready; // @[saxpy.scala 331:38]
  assign st_19_io_MemResp_valid = mem_ctrl_cache_io_wr_mem_0_MemResp_valid; // @[saxpy.scala 332:20]
  assign binaryOp_indvars_iv_next20_clock = clock;
  assign binaryOp_indvars_iv_next20_reset = reset;
  assign binaryOp_indvars_iv_next20_io_enable_valid = bb_for_body11_io_Out_10_valid; // @[saxpy.scala 304:40]
  assign binaryOp_indvars_iv_next20_io_enable_bits_control = bb_for_body11_io_Out_10_bits_control; // @[saxpy.scala 304:40]
  assign binaryOp_indvars_iv_next20_io_Out_0_ready = Loop_0_io_CarryDepenIn_0_ready; // @[saxpy.scala 238:29]
  assign binaryOp_indvars_iv_next20_io_Out_1_ready = icmp_exitcond15_io_LeftIO_ready; // @[saxpy.scala 376:29]
  assign binaryOp_indvars_iv_next20_io_LeftIO_valid = phiindvars_iv12_io_Out_2_valid; // @[saxpy.scala 360:40]
  assign binaryOp_indvars_iv_next20_io_LeftIO_bits_data = phiindvars_iv12_io_Out_2_bits_data; // @[saxpy.scala 360:40]
  assign binaryOp_indvars_iv_next20_io_RightIO_valid = const3_io_Out_valid; // @[saxpy.scala 352:41]
  assign icmp_exitcond15_clock = clock;
  assign icmp_exitcond15_reset = reset;
  assign icmp_exitcond15_io_enable_valid = bb_for_body11_io_Out_11_valid; // @[saxpy.scala 307:29]
  assign icmp_exitcond15_io_enable_bits_control = bb_for_body11_io_Out_11_bits_control; // @[saxpy.scala 307:29]
  assign icmp_exitcond15_io_Out_0_ready = br_22_io_CmpIO_ready; // @[saxpy.scala 378:18]
  assign icmp_exitcond15_io_LeftIO_valid = binaryOp_indvars_iv_next20_io_Out_1_valid; // @[saxpy.scala 376:29]
  assign icmp_exitcond15_io_LeftIO_bits_data = binaryOp_indvars_iv_next20_io_Out_1_bits_data; // @[saxpy.scala 376:29]
  assign icmp_exitcond15_io_RightIO_valid = Loop_0_io_OutLiveIn_field3_0_valid; // @[saxpy.scala 218:30]
  assign icmp_exitcond15_io_RightIO_bits_data = Loop_0_io_OutLiveIn_field3_0_bits_data; // @[saxpy.scala 218:30]
  assign br_22_clock = clock;
  assign br_22_reset = reset;
  assign br_22_io_enable_valid = bb_for_body11_io_Out_12_valid; // @[saxpy.scala 310:19]
  assign br_22_io_enable_bits_control = bb_for_body11_io_Out_12_bits_control; // @[saxpy.scala 310:19]
  assign br_22_io_CmpIO_valid = icmp_exitcond15_io_Out_0_valid; // @[saxpy.scala 378:18]
  assign br_22_io_CmpIO_bits_data = icmp_exitcond15_io_Out_0_bits_data; // @[saxpy.scala 378:18]
  assign br_22_io_PredOp_0_valid = st_19_io_SuccOp_0_valid; // @[saxpy.scala 392:22]
  assign br_22_io_TrueOutput_0_ready = Loop_0_io_loopFinish_0_ready; // @[saxpy.scala 184:27]
  assign br_22_io_FalseOutput_0_ready = Loop_0_io_loopBack_0_ready; // @[saxpy.scala 182:25]
  assign const0_clock = clock;
  assign const0_reset = reset;
  assign const0_io_enable_valid = bb_entry1_io_Out_0_valid; // @[saxpy.scala 254:20]
  assign const0_io_Out_ready = icmp_cmp110_io_RightIO_ready; // @[saxpy.scala 346:26]
  assign const1_clock = clock;
  assign const1_reset = reset;
  assign const1_io_enable_valid = bb_for_cond_cleanup9_io_Out_0_valid; // @[saxpy.scala 271:20]
  assign const1_io_Out_ready = ret_10_io_In_data_field0_ready; // @[saxpy.scala 348:31]
  assign const2_clock = clock;
  assign const2_reset = reset;
  assign const2_io_enable_valid = bb_for_body11_io_Out_0_valid; // @[saxpy.scala 276:20]
  assign const2_io_Out_ready = phiindvars_iv12_io_InData_0_ready; // @[saxpy.scala 350:32]
  assign const3_clock = clock;
  assign const3_reset = reset;
  assign const3_io_enable_valid = bb_for_body11_io_Out_1_valid; // @[saxpy.scala 278:20]
  assign const3_io_Out_ready = binaryOp_indvars_iv_next20_io_RightIO_ready; // @[saxpy.scala 352:41]
endmodule
module Queue_2(
  input         clock,
  input         reset,
  input         io_enq_valid,
  input         io_deq_ready,
  output        io_deq_valid,
  output [63:0] io_deq_bits,
  output [6:0]  io_count
);
`ifdef RANDOMIZE_GARBAGE_ASSIGN
  reg [63:0] _RAND_1;
`endif // RANDOMIZE_GARBAGE_ASSIGN
`ifdef RANDOMIZE_MEM_INIT
  reg [63:0] _RAND_0;
`endif // RANDOMIZE_MEM_INIT
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_2;
`endif // RANDOMIZE_REG_INIT
  reg [63:0] ram [0:99]; // @[Decoupled.scala 218:16]
  wire [63:0] ram__T_11_data; // @[Decoupled.scala 218:16]
  wire [6:0] ram__T_11_addr; // @[Decoupled.scala 218:16]
  wire [63:0] ram__T_3_data; // @[Decoupled.scala 218:16]
  wire [6:0] ram__T_3_addr; // @[Decoupled.scala 218:16]
  wire  ram__T_3_mask; // @[Decoupled.scala 218:16]
  wire  ram__T_3_en; // @[Decoupled.scala 218:16]
  reg [6:0] deq_ptr_value; // @[Counter.scala 29:33]
  wire  ptr_match = 7'h0 == deq_ptr_value; // @[Decoupled.scala 223:33]
  wire  do_deq = io_deq_ready & io_deq_valid; // @[Decoupled.scala 40:37]
  wire  wrap_1 = deq_ptr_value == 7'h63; // @[Counter.scala 38:24]
  wire [6:0] _T_7 = deq_ptr_value + 7'h1; // @[Counter.scala 39:22]
  wire [6:0] ptr_diff = 7'h0 - deq_ptr_value; // @[Decoupled.scala 257:32]
  wire  _T_14 = deq_ptr_value > 7'h0; // @[Decoupled.scala 264:39]
  wire [6:0] _T_16 = 7'h64 + ptr_diff; // @[Decoupled.scala 265:38]
  wire [6:0] _T_17 = _T_14 ? _T_16 : ptr_diff; // @[Decoupled.scala 264:24]
  assign ram__T_11_addr = deq_ptr_value;
  `ifndef RANDOMIZE_GARBAGE_ASSIGN
  assign ram__T_11_data = ram[ram__T_11_addr]; // @[Decoupled.scala 218:16]
  `else
  assign ram__T_11_data = ram__T_11_addr >= 7'h64 ? _RAND_1[63:0] : ram[ram__T_11_addr]; // @[Decoupled.scala 218:16]
  `endif // RANDOMIZE_GARBAGE_ASSIGN
  assign ram__T_3_data = 64'h0;
  assign ram__T_3_addr = 7'h0;
  assign ram__T_3_mask = 1'h1;
  assign ram__T_3_en = 1'h0;
  assign io_deq_valid = ~ptr_match; // @[Decoupled.scala 240:16]
  assign io_deq_bits = ram__T_11_data; // @[Decoupled.scala 242:15]
  assign io_count = ptr_match ? 7'h0 : _T_17; // @[Decoupled.scala 261:14]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_GARBAGE_ASSIGN
  _RAND_1 = {2{`RANDOM}};
`endif // RANDOMIZE_GARBAGE_ASSIGN
`ifdef RANDOMIZE_MEM_INIT
  _RAND_0 = {2{`RANDOM}};
  for (initvar = 0; initvar < 100; initvar = initvar+1)
    ram[initvar] = _RAND_0[63:0];
`endif // RANDOMIZE_MEM_INIT
`ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  deq_ptr_value = _RAND_2[6:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if(ram__T_3_en & ram__T_3_mask) begin
      ram[ram__T_3_addr] <= ram__T_3_data; // @[Decoupled.scala 218:16]
    end
    if (reset) begin
      deq_ptr_value <= 7'h0;
    end else if (do_deq) begin
      if (wrap_1) begin
        deq_ptr_value <= 7'h0;
      end else begin
        deq_ptr_value <= _T_7;
      end
    end
  end
endmodule
module DebugVMEBufferNode(
  input          clock,
  input          reset,
  input  [63:0]  io_addrDebug,
  input          io_vmeOut_cmd_ready,
  output         io_vmeOut_cmd_valid,
  output [63:0]  io_vmeOut_cmd_bits_addr,
  output [7:0]   io_vmeOut_cmd_bits_len,
  input          io_vmeOut_data_ready,
  output         io_vmeOut_data_valid,
  output [511:0] io_vmeOut_data_bits,
  input          io_vmeOut_ack
);
`ifdef RANDOMIZE_REG_INIT
  reg [63:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
`endif // RANDOMIZE_REG_INIT
  wire  LogData_clock; // @[DebugStore.scala 335:23]
  wire  LogData_reset; // @[DebugStore.scala 335:23]
  wire  LogData_io_enq_valid; // @[DebugStore.scala 335:23]
  wire  LogData_io_deq_ready; // @[DebugStore.scala 335:23]
  wire  LogData_io_deq_valid; // @[DebugStore.scala 335:23]
  wire [63:0] LogData_io_deq_bits; // @[DebugStore.scala 335:23]
  wire [6:0] LogData_io_count; // @[DebugStore.scala 335:23]
  reg [63:0] addr_debug_reg; // @[DebugStore.scala 330:31]
  reg [1:0] wState; // @[DebugStore.scala 332:23]
  reg [6:0] queue_count; // @[DebugStore.scala 338:28]
  wire  _T_4 = LogData_io_enq_valid; // @[Decoupled.scala 40:37]
  wire [6:0] _T_6 = queue_count + 7'h1; // @[DebugStore.scala 340:32]
  wire [6:0] _T_15 = queue_count - 7'h1; // @[DebugStore.scala 352:41]
  wire  _T_17 = 2'h0 == wState; // @[Conditional.scala 37:30]
  wire [6:0] _T_19 = 7'h64 / 7'h2; // @[DebugStore.scala 358:98]
  wire  _T_20 = LogData_io_count == _T_19; // @[DebugStore.scala 358:82]
  wire  _T_22 = 2'h1 == wState; // @[Conditional.scala 37:30]
  wire  _T_23 = io_vmeOut_cmd_ready & io_vmeOut_cmd_valid; // @[Decoupled.scala 40:37]
  wire  _T_24 = 2'h2 == wState; // @[Conditional.scala 37:30]
  wire [10:0] _T_25 = queue_count * 7'h8; // @[DebugStore.scala 371:57]
  wire [63:0] _GEN_20 = {{53'd0}, _T_25}; // @[DebugStore.scala 371:42]
  wire [63:0] _T_27 = addr_debug_reg + _GEN_20; // @[DebugStore.scala 371:42]
  wire  _T_28 = wState == 2'h2; // @[DebugStore.scala 381:15]
  wire [63:0] _GEN_17 = _T_28 ? LogData_io_deq_bits : 64'h0; // @[DebugStore.scala 381:26]
  Queue_2 LogData ( // @[DebugStore.scala 335:23]
    .clock(LogData_clock),
    .reset(LogData_reset),
    .io_enq_valid(LogData_io_enq_valid),
    .io_deq_ready(LogData_io_deq_ready),
    .io_deq_valid(LogData_io_deq_valid),
    .io_deq_bits(LogData_io_deq_bits),
    .io_count(LogData_io_count)
  );
  assign io_vmeOut_cmd_valid = wState == 2'h1; // @[DebugStore.scala 353:23]
  assign io_vmeOut_cmd_bits_addr = io_addrDebug + addr_debug_reg; // @[DebugStore.scala 351:27]
  assign io_vmeOut_cmd_bits_len = {{1'd0}, _T_15}; // @[DebugStore.scala 352:26]
  assign io_vmeOut_data_valid = _T_28 & LogData_io_deq_valid; // @[DebugStore.scala 378:24 DebugStore.scala 383:26]
  assign io_vmeOut_data_bits = {{448'd0}, _GEN_17}; // @[DebugStore.scala 377:23 DebugStore.scala 382:25]
  assign LogData_clock = clock;
  assign LogData_reset = reset;
  assign LogData_io_enq_valid = 1'h0; // @[DebugStore.scala 348:24]
  assign LogData_io_deq_ready = _T_28 & io_vmeOut_data_ready; // @[DebugStore.scala 379:24 DebugStore.scala 384:26]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {2{`RANDOM}};
  addr_debug_reg = _RAND_0[63:0];
  _RAND_1 = {1{`RANDOM}};
  wState = _RAND_1[1:0];
  _RAND_2 = {1{`RANDOM}};
  queue_count = _RAND_2[6:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      addr_debug_reg <= 64'h0;
    end else if (!(_T_17)) begin
      if (!(_T_22)) begin
        if (_T_24) begin
          if (io_vmeOut_ack) begin
            addr_debug_reg <= _T_27;
          end
        end
      end
    end
    if (reset) begin
      wState <= 2'h0;
    end else if (_T_17) begin
      if (_T_20) begin
        wState <= 2'h1;
      end
    end else if (_T_22) begin
      if (_T_23) begin
        wState <= 2'h2;
      end
    end else if (_T_24) begin
      if (io_vmeOut_ack) begin
        wState <= 2'h0;
      end
    end
    if (reset) begin
      queue_count <= 7'h0;
    end else if (_T_17) begin
      if (_T_4) begin
        queue_count <= _T_6;
      end
    end else if (_T_22) begin
      if (_T_4) begin
        queue_count <= _T_6;
      end
    end else if (_T_24) begin
      if (io_vmeOut_ack) begin
        queue_count <= 7'h0;
      end else if (_T_4) begin
        queue_count <= _T_6;
      end
    end else if (_T_4) begin
      queue_count <= _T_6;
    end
  end
endmodule
module DebugBufferWriters(
  input          clock,
  input          reset,
  input  [63:0]  io_addrDebug_0,
  input  [63:0]  io_addrDebug_1,
  input  [63:0]  io_addrDebug_2,
  input  [63:0]  io_addrDebug_3,
  input  [63:0]  io_addrDebug_4,
  input          io_vmeOut_0_cmd_ready,
  output         io_vmeOut_0_cmd_valid,
  output [63:0]  io_vmeOut_0_cmd_bits_addr,
  output [7:0]   io_vmeOut_0_cmd_bits_len,
  input          io_vmeOut_0_data_ready,
  output         io_vmeOut_0_data_valid,
  output [511:0] io_vmeOut_0_data_bits,
  input          io_vmeOut_0_ack,
  input          io_vmeOut_1_cmd_ready,
  output         io_vmeOut_1_cmd_valid,
  output [63:0]  io_vmeOut_1_cmd_bits_addr,
  output [7:0]   io_vmeOut_1_cmd_bits_len,
  input          io_vmeOut_1_data_ready,
  output         io_vmeOut_1_data_valid,
  output [511:0] io_vmeOut_1_data_bits,
  input          io_vmeOut_1_ack,
  input          io_vmeOut_2_cmd_ready,
  output         io_vmeOut_2_cmd_valid,
  output [63:0]  io_vmeOut_2_cmd_bits_addr,
  output [7:0]   io_vmeOut_2_cmd_bits_len,
  input          io_vmeOut_2_data_ready,
  output         io_vmeOut_2_data_valid,
  output [511:0] io_vmeOut_2_data_bits,
  input          io_vmeOut_2_ack,
  input          io_vmeOut_3_cmd_ready,
  output         io_vmeOut_3_cmd_valid,
  output [63:0]  io_vmeOut_3_cmd_bits_addr,
  output [7:0]   io_vmeOut_3_cmd_bits_len,
  input          io_vmeOut_3_data_ready,
  output         io_vmeOut_3_data_valid,
  output [511:0] io_vmeOut_3_data_bits,
  input          io_vmeOut_3_ack,
  input          io_vmeOut_4_cmd_ready,
  output         io_vmeOut_4_cmd_valid,
  output [63:0]  io_vmeOut_4_cmd_bits_addr,
  output [7:0]   io_vmeOut_4_cmd_bits_len,
  input          io_vmeOut_4_data_ready,
  output         io_vmeOut_4_data_valid,
  output [511:0] io_vmeOut_4_data_bits,
  input          io_vmeOut_4_ack
);
  wire  buffers_0_clock; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_0_reset; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_0_io_addrDebug; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_0_io_vmeOut_cmd_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_0_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_0_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 23:52]
  wire [7:0] buffers_0_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_0_io_vmeOut_data_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_0_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 23:52]
  wire [511:0] buffers_0_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_0_io_vmeOut_ack; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_1_clock; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_1_reset; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_1_io_addrDebug; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_1_io_vmeOut_cmd_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_1_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_1_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 23:52]
  wire [7:0] buffers_1_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_1_io_vmeOut_data_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_1_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 23:52]
  wire [511:0] buffers_1_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_1_io_vmeOut_ack; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_2_clock; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_2_reset; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_2_io_addrDebug; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_2_io_vmeOut_cmd_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_2_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_2_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 23:52]
  wire [7:0] buffers_2_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_2_io_vmeOut_data_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_2_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 23:52]
  wire [511:0] buffers_2_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_2_io_vmeOut_ack; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_3_clock; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_3_reset; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_3_io_addrDebug; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_3_io_vmeOut_cmd_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_3_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_3_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 23:52]
  wire [7:0] buffers_3_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_3_io_vmeOut_data_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_3_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 23:52]
  wire [511:0] buffers_3_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_3_io_vmeOut_ack; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_4_clock; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_4_reset; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_4_io_addrDebug; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_4_io_vmeOut_cmd_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_4_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 23:52]
  wire [63:0] buffers_4_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 23:52]
  wire [7:0] buffers_4_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_4_io_vmeOut_data_ready; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_4_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 23:52]
  wire [511:0] buffers_4_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 23:52]
  wire  buffers_4_io_vmeOut_ack; // @[DebugBufferWriters.scala 23:52]
  DebugVMEBufferNode buffers_0 ( // @[DebugBufferWriters.scala 23:52]
    .clock(buffers_0_clock),
    .reset(buffers_0_reset),
    .io_addrDebug(buffers_0_io_addrDebug),
    .io_vmeOut_cmd_ready(buffers_0_io_vmeOut_cmd_ready),
    .io_vmeOut_cmd_valid(buffers_0_io_vmeOut_cmd_valid),
    .io_vmeOut_cmd_bits_addr(buffers_0_io_vmeOut_cmd_bits_addr),
    .io_vmeOut_cmd_bits_len(buffers_0_io_vmeOut_cmd_bits_len),
    .io_vmeOut_data_ready(buffers_0_io_vmeOut_data_ready),
    .io_vmeOut_data_valid(buffers_0_io_vmeOut_data_valid),
    .io_vmeOut_data_bits(buffers_0_io_vmeOut_data_bits),
    .io_vmeOut_ack(buffers_0_io_vmeOut_ack)
  );
  DebugVMEBufferNode buffers_1 ( // @[DebugBufferWriters.scala 23:52]
    .clock(buffers_1_clock),
    .reset(buffers_1_reset),
    .io_addrDebug(buffers_1_io_addrDebug),
    .io_vmeOut_cmd_ready(buffers_1_io_vmeOut_cmd_ready),
    .io_vmeOut_cmd_valid(buffers_1_io_vmeOut_cmd_valid),
    .io_vmeOut_cmd_bits_addr(buffers_1_io_vmeOut_cmd_bits_addr),
    .io_vmeOut_cmd_bits_len(buffers_1_io_vmeOut_cmd_bits_len),
    .io_vmeOut_data_ready(buffers_1_io_vmeOut_data_ready),
    .io_vmeOut_data_valid(buffers_1_io_vmeOut_data_valid),
    .io_vmeOut_data_bits(buffers_1_io_vmeOut_data_bits),
    .io_vmeOut_ack(buffers_1_io_vmeOut_ack)
  );
  DebugVMEBufferNode buffers_2 ( // @[DebugBufferWriters.scala 23:52]
    .clock(buffers_2_clock),
    .reset(buffers_2_reset),
    .io_addrDebug(buffers_2_io_addrDebug),
    .io_vmeOut_cmd_ready(buffers_2_io_vmeOut_cmd_ready),
    .io_vmeOut_cmd_valid(buffers_2_io_vmeOut_cmd_valid),
    .io_vmeOut_cmd_bits_addr(buffers_2_io_vmeOut_cmd_bits_addr),
    .io_vmeOut_cmd_bits_len(buffers_2_io_vmeOut_cmd_bits_len),
    .io_vmeOut_data_ready(buffers_2_io_vmeOut_data_ready),
    .io_vmeOut_data_valid(buffers_2_io_vmeOut_data_valid),
    .io_vmeOut_data_bits(buffers_2_io_vmeOut_data_bits),
    .io_vmeOut_ack(buffers_2_io_vmeOut_ack)
  );
  DebugVMEBufferNode buffers_3 ( // @[DebugBufferWriters.scala 23:52]
    .clock(buffers_3_clock),
    .reset(buffers_3_reset),
    .io_addrDebug(buffers_3_io_addrDebug),
    .io_vmeOut_cmd_ready(buffers_3_io_vmeOut_cmd_ready),
    .io_vmeOut_cmd_valid(buffers_3_io_vmeOut_cmd_valid),
    .io_vmeOut_cmd_bits_addr(buffers_3_io_vmeOut_cmd_bits_addr),
    .io_vmeOut_cmd_bits_len(buffers_3_io_vmeOut_cmd_bits_len),
    .io_vmeOut_data_ready(buffers_3_io_vmeOut_data_ready),
    .io_vmeOut_data_valid(buffers_3_io_vmeOut_data_valid),
    .io_vmeOut_data_bits(buffers_3_io_vmeOut_data_bits),
    .io_vmeOut_ack(buffers_3_io_vmeOut_ack)
  );
  DebugVMEBufferNode buffers_4 ( // @[DebugBufferWriters.scala 23:52]
    .clock(buffers_4_clock),
    .reset(buffers_4_reset),
    .io_addrDebug(buffers_4_io_addrDebug),
    .io_vmeOut_cmd_ready(buffers_4_io_vmeOut_cmd_ready),
    .io_vmeOut_cmd_valid(buffers_4_io_vmeOut_cmd_valid),
    .io_vmeOut_cmd_bits_addr(buffers_4_io_vmeOut_cmd_bits_addr),
    .io_vmeOut_cmd_bits_len(buffers_4_io_vmeOut_cmd_bits_len),
    .io_vmeOut_data_ready(buffers_4_io_vmeOut_data_ready),
    .io_vmeOut_data_valid(buffers_4_io_vmeOut_data_valid),
    .io_vmeOut_data_bits(buffers_4_io_vmeOut_data_bits),
    .io_vmeOut_ack(buffers_4_io_vmeOut_ack)
  );
  assign io_vmeOut_0_cmd_valid = buffers_0_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_0_cmd_bits_addr = buffers_0_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_0_cmd_bits_len = buffers_0_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_0_data_valid = buffers_0_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_0_data_bits = buffers_0_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_1_cmd_valid = buffers_1_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_1_cmd_bits_addr = buffers_1_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_1_cmd_bits_len = buffers_1_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_1_data_valid = buffers_1_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_1_data_bits = buffers_1_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_2_cmd_valid = buffers_2_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_2_cmd_bits_addr = buffers_2_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_2_cmd_bits_len = buffers_2_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_2_data_valid = buffers_2_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_2_data_bits = buffers_2_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_3_cmd_valid = buffers_3_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_3_cmd_bits_addr = buffers_3_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_3_cmd_bits_len = buffers_3_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_3_data_valid = buffers_3_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_3_data_bits = buffers_3_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_4_cmd_valid = buffers_4_io_vmeOut_cmd_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_4_cmd_bits_addr = buffers_4_io_vmeOut_cmd_bits_addr; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_4_cmd_bits_len = buffers_4_io_vmeOut_cmd_bits_len; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_4_data_valid = buffers_4_io_vmeOut_data_valid; // @[DebugBufferWriters.scala 48:18]
  assign io_vmeOut_4_data_bits = buffers_4_io_vmeOut_data_bits; // @[DebugBufferWriters.scala 48:18]
  assign buffers_0_clock = clock;
  assign buffers_0_reset = reset;
  assign buffers_0_io_addrDebug = io_addrDebug_0; // @[DebugBufferWriters.scala 47:29]
  assign buffers_0_io_vmeOut_cmd_ready = io_vmeOut_0_cmd_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_0_io_vmeOut_data_ready = io_vmeOut_0_data_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_0_io_vmeOut_ack = io_vmeOut_0_ack; // @[DebugBufferWriters.scala 48:18]
  assign buffers_1_clock = clock;
  assign buffers_1_reset = reset;
  assign buffers_1_io_addrDebug = io_addrDebug_1; // @[DebugBufferWriters.scala 47:29]
  assign buffers_1_io_vmeOut_cmd_ready = io_vmeOut_1_cmd_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_1_io_vmeOut_data_ready = io_vmeOut_1_data_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_1_io_vmeOut_ack = io_vmeOut_1_ack; // @[DebugBufferWriters.scala 48:18]
  assign buffers_2_clock = clock;
  assign buffers_2_reset = reset;
  assign buffers_2_io_addrDebug = io_addrDebug_2; // @[DebugBufferWriters.scala 47:29]
  assign buffers_2_io_vmeOut_cmd_ready = io_vmeOut_2_cmd_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_2_io_vmeOut_data_ready = io_vmeOut_2_data_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_2_io_vmeOut_ack = io_vmeOut_2_ack; // @[DebugBufferWriters.scala 48:18]
  assign buffers_3_clock = clock;
  assign buffers_3_reset = reset;
  assign buffers_3_io_addrDebug = io_addrDebug_3; // @[DebugBufferWriters.scala 47:29]
  assign buffers_3_io_vmeOut_cmd_ready = io_vmeOut_3_cmd_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_3_io_vmeOut_data_ready = io_vmeOut_3_data_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_3_io_vmeOut_ack = io_vmeOut_3_ack; // @[DebugBufferWriters.scala 48:18]
  assign buffers_4_clock = clock;
  assign buffers_4_reset = reset;
  assign buffers_4_io_addrDebug = io_addrDebug_4; // @[DebugBufferWriters.scala 47:29]
  assign buffers_4_io_vmeOut_cmd_ready = io_vmeOut_4_cmd_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_4_io_vmeOut_data_ready = io_vmeOut_4_data_ready; // @[DebugBufferWriters.scala 48:18]
  assign buffers_4_io_vmeOut_ack = io_vmeOut_4_ack; // @[DebugBufferWriters.scala 48:18]
endmodule
module DandelionDebugFPGAShell(
  input          clock,
  input          reset,
  input  [15:0]  io_host_addr,
  input  [31:0]  io_host_wdata,
  input          io_host_wr,
  input          io_host_rd,
  output         io_host_ack,
  output [31:0]  io_host_rdata,
  input          io_mem_aw_ready,
  output         io_mem_aw_valid,
  output [63:0]  io_mem_aw_bits_addr,
  output [7:0]   io_mem_aw_bits_len,
  input          io_mem_w_ready,
  output         io_mem_w_valid,
  output [511:0] io_mem_w_bits_data,
  output         io_mem_w_bits_last,
  input          io_mem_b_valid,
  input          io_mem_ar_ready,
  output         io_mem_ar_valid,
  output [63:0]  io_mem_ar_bits_addr,
  output         io_mem_r_ready,
  input          io_mem_r_valid,
  input  [511:0] io_mem_r_bits_data,
  input          io_mem_r_bits_last
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [63:0] _RAND_2;
  reg [63:0] _RAND_3;
  reg [63:0] _RAND_4;
  reg [63:0] _RAND_5;
  reg [63:0] _RAND_6;
  reg [63:0] _RAND_7;
  reg [63:0] _RAND_8;
  reg [63:0] _RAND_9;
  reg [63:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
`endif // RANDOMIZE_REG_INIT
  wire  dcr_clock; // @[DandelionShell.scala 819:19]
  wire  dcr_reset; // @[DandelionShell.scala 819:19]
  wire [15:0] dcr_io_host_addr; // @[DandelionShell.scala 819:19]
  wire [31:0] dcr_io_host_wdata; // @[DandelionShell.scala 819:19]
  wire  dcr_io_host_wr; // @[DandelionShell.scala 819:19]
  wire  dcr_io_host_rd; // @[DandelionShell.scala 819:19]
  wire  dcr_io_host_ack; // @[DandelionShell.scala 819:19]
  wire [31:0] dcr_io_host_rdata; // @[DandelionShell.scala 819:19]
  wire  dcr_io_dcr_launch; // @[DandelionShell.scala 819:19]
  wire  dcr_io_dcr_finish; // @[DandelionShell.scala 819:19]
  wire  dcr_io_dcr_ecnt_0_valid; // @[DandelionShell.scala 819:19]
  wire [31:0] dcr_io_dcr_ecnt_0_bits; // @[DandelionShell.scala 819:19]
  wire [31:0] dcr_io_dcr_vals_0; // @[DandelionShell.scala 819:19]
  wire [31:0] dcr_io_dcr_vals_1; // @[DandelionShell.scala 819:19]
  wire [63:0] dcr_io_dcr_ptrs_0; // @[DandelionShell.scala 819:19]
  wire [63:0] dcr_io_dcr_ptrs_1; // @[DandelionShell.scala 819:19]
  wire [63:0] dcr_io_dcr_ptrs_2; // @[DandelionShell.scala 819:19]
  wire [63:0] dcr_io_dcr_ptrs_3; // @[DandelionShell.scala 819:19]
  wire [63:0] dcr_io_dcr_ptrs_4; // @[DandelionShell.scala 819:19]
  wire [63:0] dcr_io_dcr_ptrs_5; // @[DandelionShell.scala 819:19]
  wire [63:0] dcr_io_dcr_ptrs_6; // @[DandelionShell.scala 819:19]
  wire  dmem_clock; // @[DandelionShell.scala 820:20]
  wire  dmem_reset; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_aw_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_aw_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_mem_aw_bits_addr; // @[DandelionShell.scala 820:20]
  wire [7:0] dmem_io_mem_aw_bits_len; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_w_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_w_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_mem_w_bits_data; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_w_bits_last; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_b_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_b_valid; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_ar_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_ar_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_mem_ar_bits_addr; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_r_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_r_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_mem_r_bits_data; // @[DandelionShell.scala 820:20]
  wire  dmem_io_mem_r_bits_last; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_rd_0_cmd_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_rd_0_cmd_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_dme_rd_0_cmd_bits_addr; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_rd_0_data_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_rd_0_data_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_dme_rd_0_data_bits; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_0_cmd_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_0_cmd_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_dme_wr_0_cmd_bits_addr; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_0_data_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_0_data_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_dme_wr_0_data_bits; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_0_ack; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_1_cmd_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_1_cmd_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_dme_wr_1_cmd_bits_addr; // @[DandelionShell.scala 820:20]
  wire [7:0] dmem_io_dme_wr_1_cmd_bits_len; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_1_data_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_1_data_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_dme_wr_1_data_bits; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_1_ack; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_2_cmd_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_2_cmd_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_dme_wr_2_cmd_bits_addr; // @[DandelionShell.scala 820:20]
  wire [7:0] dmem_io_dme_wr_2_cmd_bits_len; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_2_data_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_2_data_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_dme_wr_2_data_bits; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_2_ack; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_3_cmd_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_3_cmd_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_dme_wr_3_cmd_bits_addr; // @[DandelionShell.scala 820:20]
  wire [7:0] dmem_io_dme_wr_3_cmd_bits_len; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_3_data_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_3_data_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_dme_wr_3_data_bits; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_3_ack; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_4_cmd_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_4_cmd_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_dme_wr_4_cmd_bits_addr; // @[DandelionShell.scala 820:20]
  wire [7:0] dmem_io_dme_wr_4_cmd_bits_len; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_4_data_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_4_data_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_dme_wr_4_data_bits; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_4_ack; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_5_cmd_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_5_cmd_valid; // @[DandelionShell.scala 820:20]
  wire [63:0] dmem_io_dme_wr_5_cmd_bits_addr; // @[DandelionShell.scala 820:20]
  wire [7:0] dmem_io_dme_wr_5_cmd_bits_len; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_5_data_ready; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_5_data_valid; // @[DandelionShell.scala 820:20]
  wire [511:0] dmem_io_dme_wr_5_data_bits; // @[DandelionShell.scala 820:20]
  wire  dmem_io_dme_wr_5_ack; // @[DandelionShell.scala 820:20]
  wire  cache_clock; // @[DandelionShell.scala 821:21]
  wire  cache_reset; // @[DandelionShell.scala 821:21]
  wire  cache_io_cpu_flush; // @[DandelionShell.scala 821:21]
  wire  cache_io_cpu_flush_done; // @[DandelionShell.scala 821:21]
  wire  cache_io_cpu_req_ready; // @[DandelionShell.scala 821:21]
  wire  cache_io_cpu_req_valid; // @[DandelionShell.scala 821:21]
  wire [63:0] cache_io_cpu_req_bits_addr; // @[DandelionShell.scala 821:21]
  wire [63:0] cache_io_cpu_req_bits_data; // @[DandelionShell.scala 821:21]
  wire [7:0] cache_io_cpu_req_bits_mask; // @[DandelionShell.scala 821:21]
  wire [7:0] cache_io_cpu_req_bits_tag; // @[DandelionShell.scala 821:21]
  wire  cache_io_cpu_resp_valid; // @[DandelionShell.scala 821:21]
  wire [63:0] cache_io_cpu_resp_bits_data; // @[DandelionShell.scala 821:21]
  wire [7:0] cache_io_cpu_resp_bits_tag; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_rd_cmd_ready; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_rd_cmd_valid; // @[DandelionShell.scala 821:21]
  wire [63:0] cache_io_mem_rd_cmd_bits_addr; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_rd_data_ready; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_rd_data_valid; // @[DandelionShell.scala 821:21]
  wire [511:0] cache_io_mem_rd_data_bits; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_wr_cmd_ready; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_wr_cmd_valid; // @[DandelionShell.scala 821:21]
  wire [63:0] cache_io_mem_wr_cmd_bits_addr; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_wr_data_ready; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_wr_data_valid; // @[DandelionShell.scala 821:21]
  wire [511:0] cache_io_mem_wr_data_bits; // @[DandelionShell.scala 821:21]
  wire  cache_io_mem_wr_ack; // @[DandelionShell.scala 821:21]
  wire  accel_clock; // @[DandelionShell.scala 824:21]
  wire  accel_reset; // @[DandelionShell.scala 824:21]
  wire  accel_io_in_ready; // @[DandelionShell.scala 824:21]
  wire  accel_io_in_valid; // @[DandelionShell.scala 824:21]
  wire [63:0] accel_io_in_bits_dataPtrs_field1_data; // @[DandelionShell.scala 824:21]
  wire [63:0] accel_io_in_bits_dataPtrs_field0_data; // @[DandelionShell.scala 824:21]
  wire [63:0] accel_io_in_bits_dataVals_field1_data; // @[DandelionShell.scala 824:21]
  wire [63:0] accel_io_in_bits_dataVals_field0_data; // @[DandelionShell.scala 824:21]
  wire  accel_io_MemResp_valid; // @[DandelionShell.scala 824:21]
  wire [63:0] accel_io_MemResp_bits_data; // @[DandelionShell.scala 824:21]
  wire [7:0] accel_io_MemResp_bits_tag; // @[DandelionShell.scala 824:21]
  wire  accel_io_MemReq_ready; // @[DandelionShell.scala 824:21]
  wire  accel_io_MemReq_valid; // @[DandelionShell.scala 824:21]
  wire [63:0] accel_io_MemReq_bits_addr; // @[DandelionShell.scala 824:21]
  wire [63:0] accel_io_MemReq_bits_data; // @[DandelionShell.scala 824:21]
  wire [7:0] accel_io_MemReq_bits_mask; // @[DandelionShell.scala 824:21]
  wire [7:0] accel_io_MemReq_bits_tag; // @[DandelionShell.scala 824:21]
  wire  accel_io_out_ready; // @[DandelionShell.scala 824:21]
  wire  accel_io_out_valid; // @[DandelionShell.scala 824:21]
  wire  debug_module_clock; // @[DandelionShell.scala 826:50]
  wire  debug_module_reset; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_addrDebug_0; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_addrDebug_1; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_addrDebug_2; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_addrDebug_3; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_addrDebug_4; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_0_cmd_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_0_cmd_valid; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_vmeOut_0_cmd_bits_addr; // @[DandelionShell.scala 826:50]
  wire [7:0] debug_module_io_vmeOut_0_cmd_bits_len; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_0_data_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_0_data_valid; // @[DandelionShell.scala 826:50]
  wire [511:0] debug_module_io_vmeOut_0_data_bits; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_0_ack; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_1_cmd_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_1_cmd_valid; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_vmeOut_1_cmd_bits_addr; // @[DandelionShell.scala 826:50]
  wire [7:0] debug_module_io_vmeOut_1_cmd_bits_len; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_1_data_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_1_data_valid; // @[DandelionShell.scala 826:50]
  wire [511:0] debug_module_io_vmeOut_1_data_bits; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_1_ack; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_2_cmd_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_2_cmd_valid; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_vmeOut_2_cmd_bits_addr; // @[DandelionShell.scala 826:50]
  wire [7:0] debug_module_io_vmeOut_2_cmd_bits_len; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_2_data_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_2_data_valid; // @[DandelionShell.scala 826:50]
  wire [511:0] debug_module_io_vmeOut_2_data_bits; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_2_ack; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_3_cmd_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_3_cmd_valid; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_vmeOut_3_cmd_bits_addr; // @[DandelionShell.scala 826:50]
  wire [7:0] debug_module_io_vmeOut_3_cmd_bits_len; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_3_data_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_3_data_valid; // @[DandelionShell.scala 826:50]
  wire [511:0] debug_module_io_vmeOut_3_data_bits; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_3_ack; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_4_cmd_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_4_cmd_valid; // @[DandelionShell.scala 826:50]
  wire [63:0] debug_module_io_vmeOut_4_cmd_bits_addr; // @[DandelionShell.scala 826:50]
  wire [7:0] debug_module_io_vmeOut_4_cmd_bits_len; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_4_data_ready; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_4_data_valid; // @[DandelionShell.scala 826:50]
  wire [511:0] debug_module_io_vmeOut_4_data_bits; // @[DandelionShell.scala 826:50]
  wire  debug_module_io_vmeOut_4_ack; // @[DandelionShell.scala 826:50]
  reg [1:0] state; // @[DandelionShell.scala 845:22]
  reg [31:0] cycles; // @[DandelionShell.scala 846:23]
  wire  _T = state == 2'h0; // @[DandelionShell.scala 851:14]
  wire  _T_1 = state != 2'h2; // @[DandelionShell.scala 853:20]
  wire [31:0] _T_3 = cycles + 32'h1; // @[DandelionShell.scala 854:22]
  reg [63:0] ptrs_0; // @[Reg.scala 27:20]
  reg [63:0] ptrs_1; // @[Reg.scala 27:20]
  reg [63:0] ptrs_2; // @[Reg.scala 27:20]
  reg [63:0] ptrs_3; // @[Reg.scala 27:20]
  reg [63:0] ptrs_4; // @[Reg.scala 27:20]
  reg [63:0] ptrs_5; // @[Reg.scala 27:20]
  reg [63:0] ptrs_6; // @[Reg.scala 27:20]
  reg [63:0] vals_0; // @[Reg.scala 27:20]
  reg [63:0] vals_1; // @[Reg.scala 27:20]
  wire  _T_18 = state == 2'h2; // @[DandelionShell.scala 902:31]
  reg  dme_ack_0; // @[DandelionShell.scala 913:46]
  reg  dme_ack_1; // @[DandelionShell.scala 913:46]
  reg  dme_ack_2; // @[DandelionShell.scala 913:46]
  reg  dme_ack_3; // @[DandelionShell.scala 913:46]
  reg  dme_ack_4; // @[DandelionShell.scala 913:46]
  wire  _GEN_11 = dmem_io_dme_wr_1_ack | dme_ack_0; // @[DandelionShell.scala 915:37]
  wire  _GEN_12 = dmem_io_dme_wr_2_ack | dme_ack_1; // @[DandelionShell.scala 915:37]
  wire  _GEN_13 = dmem_io_dme_wr_3_ack | dme_ack_2; // @[DandelionShell.scala 915:37]
  wire  _GEN_14 = dmem_io_dme_wr_4_ack | dme_ack_3; // @[DandelionShell.scala 915:37]
  wire  _GEN_15 = dmem_io_dme_wr_5_ack | dme_ack_4; // @[DandelionShell.scala 915:37]
  reg  cache_done; // @[DandelionShell.scala 928:27]
  wire  _GEN_16 = cache_io_cpu_flush_done | cache_done; // @[DandelionShell.scala 930:35]
  wire  _T_21 = 2'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_23 = ~reset; // @[DandelionShell.scala 938:15]
  wire  _T_46 = accel_io_in_ready & accel_io_in_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_19 = dcr_io_dcr_launch; // @[DandelionShell.scala 937:31]
  wire  _T_47 = 2'h1 == state; // @[Conditional.scala 37:30]
  wire  _T_48 = accel_io_out_ready & accel_io_out_valid; // @[Decoupled.scala 40:37]
  wire  _T_49 = 2'h2 == state; // @[Conditional.scala 37:30]
  wire  _T_50 = dme_ack_0 & dme_ack_1; // @[DandelionShell.scala 924:32]
  wire  _T_51 = _T_50 & dme_ack_2; // @[DandelionShell.scala 924:32]
  wire  _T_52 = _T_51 & dme_ack_3; // @[DandelionShell.scala 924:32]
  wire  _T_53 = _T_52 & dme_ack_4; // @[DandelionShell.scala 924:32]
  wire  _T_54 = cache_done & _T_53; // @[DandelionShell.scala 959:23]
  wire  _T_55 = 2'h3 == state; // @[Conditional.scala 37:30]
  wire  _GEN_28 = _T_21 & dcr_io_dcr_launch; // @[DandelionShell.scala 938:15]
  DCRF1 dcr ( // @[DandelionShell.scala 819:19]
    .clock(dcr_clock),
    .reset(dcr_reset),
    .io_host_addr(dcr_io_host_addr),
    .io_host_wdata(dcr_io_host_wdata),
    .io_host_wr(dcr_io_host_wr),
    .io_host_rd(dcr_io_host_rd),
    .io_host_ack(dcr_io_host_ack),
    .io_host_rdata(dcr_io_host_rdata),
    .io_dcr_launch(dcr_io_dcr_launch),
    .io_dcr_finish(dcr_io_dcr_finish),
    .io_dcr_ecnt_0_valid(dcr_io_dcr_ecnt_0_valid),
    .io_dcr_ecnt_0_bits(dcr_io_dcr_ecnt_0_bits),
    .io_dcr_vals_0(dcr_io_dcr_vals_0),
    .io_dcr_vals_1(dcr_io_dcr_vals_1),
    .io_dcr_ptrs_0(dcr_io_dcr_ptrs_0),
    .io_dcr_ptrs_1(dcr_io_dcr_ptrs_1),
    .io_dcr_ptrs_2(dcr_io_dcr_ptrs_2),
    .io_dcr_ptrs_3(dcr_io_dcr_ptrs_3),
    .io_dcr_ptrs_4(dcr_io_dcr_ptrs_4),
    .io_dcr_ptrs_5(dcr_io_dcr_ptrs_5),
    .io_dcr_ptrs_6(dcr_io_dcr_ptrs_6)
  );
  DME dmem ( // @[DandelionShell.scala 820:20]
    .clock(dmem_clock),
    .reset(dmem_reset),
    .io_mem_aw_ready(dmem_io_mem_aw_ready),
    .io_mem_aw_valid(dmem_io_mem_aw_valid),
    .io_mem_aw_bits_addr(dmem_io_mem_aw_bits_addr),
    .io_mem_aw_bits_len(dmem_io_mem_aw_bits_len),
    .io_mem_w_ready(dmem_io_mem_w_ready),
    .io_mem_w_valid(dmem_io_mem_w_valid),
    .io_mem_w_bits_data(dmem_io_mem_w_bits_data),
    .io_mem_w_bits_last(dmem_io_mem_w_bits_last),
    .io_mem_b_ready(dmem_io_mem_b_ready),
    .io_mem_b_valid(dmem_io_mem_b_valid),
    .io_mem_ar_ready(dmem_io_mem_ar_ready),
    .io_mem_ar_valid(dmem_io_mem_ar_valid),
    .io_mem_ar_bits_addr(dmem_io_mem_ar_bits_addr),
    .io_mem_r_ready(dmem_io_mem_r_ready),
    .io_mem_r_valid(dmem_io_mem_r_valid),
    .io_mem_r_bits_data(dmem_io_mem_r_bits_data),
    .io_mem_r_bits_last(dmem_io_mem_r_bits_last),
    .io_dme_rd_0_cmd_ready(dmem_io_dme_rd_0_cmd_ready),
    .io_dme_rd_0_cmd_valid(dmem_io_dme_rd_0_cmd_valid),
    .io_dme_rd_0_cmd_bits_addr(dmem_io_dme_rd_0_cmd_bits_addr),
    .io_dme_rd_0_data_ready(dmem_io_dme_rd_0_data_ready),
    .io_dme_rd_0_data_valid(dmem_io_dme_rd_0_data_valid),
    .io_dme_rd_0_data_bits(dmem_io_dme_rd_0_data_bits),
    .io_dme_wr_0_cmd_ready(dmem_io_dme_wr_0_cmd_ready),
    .io_dme_wr_0_cmd_valid(dmem_io_dme_wr_0_cmd_valid),
    .io_dme_wr_0_cmd_bits_addr(dmem_io_dme_wr_0_cmd_bits_addr),
    .io_dme_wr_0_data_ready(dmem_io_dme_wr_0_data_ready),
    .io_dme_wr_0_data_valid(dmem_io_dme_wr_0_data_valid),
    .io_dme_wr_0_data_bits(dmem_io_dme_wr_0_data_bits),
    .io_dme_wr_0_ack(dmem_io_dme_wr_0_ack),
    .io_dme_wr_1_cmd_ready(dmem_io_dme_wr_1_cmd_ready),
    .io_dme_wr_1_cmd_valid(dmem_io_dme_wr_1_cmd_valid),
    .io_dme_wr_1_cmd_bits_addr(dmem_io_dme_wr_1_cmd_bits_addr),
    .io_dme_wr_1_cmd_bits_len(dmem_io_dme_wr_1_cmd_bits_len),
    .io_dme_wr_1_data_ready(dmem_io_dme_wr_1_data_ready),
    .io_dme_wr_1_data_valid(dmem_io_dme_wr_1_data_valid),
    .io_dme_wr_1_data_bits(dmem_io_dme_wr_1_data_bits),
    .io_dme_wr_1_ack(dmem_io_dme_wr_1_ack),
    .io_dme_wr_2_cmd_ready(dmem_io_dme_wr_2_cmd_ready),
    .io_dme_wr_2_cmd_valid(dmem_io_dme_wr_2_cmd_valid),
    .io_dme_wr_2_cmd_bits_addr(dmem_io_dme_wr_2_cmd_bits_addr),
    .io_dme_wr_2_cmd_bits_len(dmem_io_dme_wr_2_cmd_bits_len),
    .io_dme_wr_2_data_ready(dmem_io_dme_wr_2_data_ready),
    .io_dme_wr_2_data_valid(dmem_io_dme_wr_2_data_valid),
    .io_dme_wr_2_data_bits(dmem_io_dme_wr_2_data_bits),
    .io_dme_wr_2_ack(dmem_io_dme_wr_2_ack),
    .io_dme_wr_3_cmd_ready(dmem_io_dme_wr_3_cmd_ready),
    .io_dme_wr_3_cmd_valid(dmem_io_dme_wr_3_cmd_valid),
    .io_dme_wr_3_cmd_bits_addr(dmem_io_dme_wr_3_cmd_bits_addr),
    .io_dme_wr_3_cmd_bits_len(dmem_io_dme_wr_3_cmd_bits_len),
    .io_dme_wr_3_data_ready(dmem_io_dme_wr_3_data_ready),
    .io_dme_wr_3_data_valid(dmem_io_dme_wr_3_data_valid),
    .io_dme_wr_3_data_bits(dmem_io_dme_wr_3_data_bits),
    .io_dme_wr_3_ack(dmem_io_dme_wr_3_ack),
    .io_dme_wr_4_cmd_ready(dmem_io_dme_wr_4_cmd_ready),
    .io_dme_wr_4_cmd_valid(dmem_io_dme_wr_4_cmd_valid),
    .io_dme_wr_4_cmd_bits_addr(dmem_io_dme_wr_4_cmd_bits_addr),
    .io_dme_wr_4_cmd_bits_len(dmem_io_dme_wr_4_cmd_bits_len),
    .io_dme_wr_4_data_ready(dmem_io_dme_wr_4_data_ready),
    .io_dme_wr_4_data_valid(dmem_io_dme_wr_4_data_valid),
    .io_dme_wr_4_data_bits(dmem_io_dme_wr_4_data_bits),
    .io_dme_wr_4_ack(dmem_io_dme_wr_4_ack),
    .io_dme_wr_5_cmd_ready(dmem_io_dme_wr_5_cmd_ready),
    .io_dme_wr_5_cmd_valid(dmem_io_dme_wr_5_cmd_valid),
    .io_dme_wr_5_cmd_bits_addr(dmem_io_dme_wr_5_cmd_bits_addr),
    .io_dme_wr_5_cmd_bits_len(dmem_io_dme_wr_5_cmd_bits_len),
    .io_dme_wr_5_data_ready(dmem_io_dme_wr_5_data_ready),
    .io_dme_wr_5_data_valid(dmem_io_dme_wr_5_data_valid),
    .io_dme_wr_5_data_bits(dmem_io_dme_wr_5_data_bits),
    .io_dme_wr_5_ack(dmem_io_dme_wr_5_ack)
  );
  DMECache cache ( // @[DandelionShell.scala 821:21]
    .clock(cache_clock),
    .reset(cache_reset),
    .io_cpu_flush(cache_io_cpu_flush),
    .io_cpu_flush_done(cache_io_cpu_flush_done),
    .io_cpu_req_ready(cache_io_cpu_req_ready),
    .io_cpu_req_valid(cache_io_cpu_req_valid),
    .io_cpu_req_bits_addr(cache_io_cpu_req_bits_addr),
    .io_cpu_req_bits_data(cache_io_cpu_req_bits_data),
    .io_cpu_req_bits_mask(cache_io_cpu_req_bits_mask),
    .io_cpu_req_bits_tag(cache_io_cpu_req_bits_tag),
    .io_cpu_resp_valid(cache_io_cpu_resp_valid),
    .io_cpu_resp_bits_data(cache_io_cpu_resp_bits_data),
    .io_cpu_resp_bits_tag(cache_io_cpu_resp_bits_tag),
    .io_mem_rd_cmd_ready(cache_io_mem_rd_cmd_ready),
    .io_mem_rd_cmd_valid(cache_io_mem_rd_cmd_valid),
    .io_mem_rd_cmd_bits_addr(cache_io_mem_rd_cmd_bits_addr),
    .io_mem_rd_data_ready(cache_io_mem_rd_data_ready),
    .io_mem_rd_data_valid(cache_io_mem_rd_data_valid),
    .io_mem_rd_data_bits(cache_io_mem_rd_data_bits),
    .io_mem_wr_cmd_ready(cache_io_mem_wr_cmd_ready),
    .io_mem_wr_cmd_valid(cache_io_mem_wr_cmd_valid),
    .io_mem_wr_cmd_bits_addr(cache_io_mem_wr_cmd_bits_addr),
    .io_mem_wr_data_ready(cache_io_mem_wr_data_ready),
    .io_mem_wr_data_valid(cache_io_mem_wr_data_valid),
    .io_mem_wr_data_bits(cache_io_mem_wr_data_bits),
    .io_mem_wr_ack(cache_io_mem_wr_ack)
  );
  saxpyDF accel ( // @[DandelionShell.scala 824:21]
    .clock(accel_clock),
    .reset(accel_reset),
    .io_in_ready(accel_io_in_ready),
    .io_in_valid(accel_io_in_valid),
    .io_in_bits_dataPtrs_field1_data(accel_io_in_bits_dataPtrs_field1_data),
    .io_in_bits_dataPtrs_field0_data(accel_io_in_bits_dataPtrs_field0_data),
    .io_in_bits_dataVals_field1_data(accel_io_in_bits_dataVals_field1_data),
    .io_in_bits_dataVals_field0_data(accel_io_in_bits_dataVals_field0_data),
    .io_MemResp_valid(accel_io_MemResp_valid),
    .io_MemResp_bits_data(accel_io_MemResp_bits_data),
    .io_MemResp_bits_tag(accel_io_MemResp_bits_tag),
    .io_MemReq_ready(accel_io_MemReq_ready),
    .io_MemReq_valid(accel_io_MemReq_valid),
    .io_MemReq_bits_addr(accel_io_MemReq_bits_addr),
    .io_MemReq_bits_data(accel_io_MemReq_bits_data),
    .io_MemReq_bits_mask(accel_io_MemReq_bits_mask),
    .io_MemReq_bits_tag(accel_io_MemReq_bits_tag),
    .io_out_ready(accel_io_out_ready),
    .io_out_valid(accel_io_out_valid)
  );
  DebugBufferWriters debug_module ( // @[DandelionShell.scala 826:50]
    .clock(debug_module_clock),
    .reset(debug_module_reset),
    .io_addrDebug_0(debug_module_io_addrDebug_0),
    .io_addrDebug_1(debug_module_io_addrDebug_1),
    .io_addrDebug_2(debug_module_io_addrDebug_2),
    .io_addrDebug_3(debug_module_io_addrDebug_3),
    .io_addrDebug_4(debug_module_io_addrDebug_4),
    .io_vmeOut_0_cmd_ready(debug_module_io_vmeOut_0_cmd_ready),
    .io_vmeOut_0_cmd_valid(debug_module_io_vmeOut_0_cmd_valid),
    .io_vmeOut_0_cmd_bits_addr(debug_module_io_vmeOut_0_cmd_bits_addr),
    .io_vmeOut_0_cmd_bits_len(debug_module_io_vmeOut_0_cmd_bits_len),
    .io_vmeOut_0_data_ready(debug_module_io_vmeOut_0_data_ready),
    .io_vmeOut_0_data_valid(debug_module_io_vmeOut_0_data_valid),
    .io_vmeOut_0_data_bits(debug_module_io_vmeOut_0_data_bits),
    .io_vmeOut_0_ack(debug_module_io_vmeOut_0_ack),
    .io_vmeOut_1_cmd_ready(debug_module_io_vmeOut_1_cmd_ready),
    .io_vmeOut_1_cmd_valid(debug_module_io_vmeOut_1_cmd_valid),
    .io_vmeOut_1_cmd_bits_addr(debug_module_io_vmeOut_1_cmd_bits_addr),
    .io_vmeOut_1_cmd_bits_len(debug_module_io_vmeOut_1_cmd_bits_len),
    .io_vmeOut_1_data_ready(debug_module_io_vmeOut_1_data_ready),
    .io_vmeOut_1_data_valid(debug_module_io_vmeOut_1_data_valid),
    .io_vmeOut_1_data_bits(debug_module_io_vmeOut_1_data_bits),
    .io_vmeOut_1_ack(debug_module_io_vmeOut_1_ack),
    .io_vmeOut_2_cmd_ready(debug_module_io_vmeOut_2_cmd_ready),
    .io_vmeOut_2_cmd_valid(debug_module_io_vmeOut_2_cmd_valid),
    .io_vmeOut_2_cmd_bits_addr(debug_module_io_vmeOut_2_cmd_bits_addr),
    .io_vmeOut_2_cmd_bits_len(debug_module_io_vmeOut_2_cmd_bits_len),
    .io_vmeOut_2_data_ready(debug_module_io_vmeOut_2_data_ready),
    .io_vmeOut_2_data_valid(debug_module_io_vmeOut_2_data_valid),
    .io_vmeOut_2_data_bits(debug_module_io_vmeOut_2_data_bits),
    .io_vmeOut_2_ack(debug_module_io_vmeOut_2_ack),
    .io_vmeOut_3_cmd_ready(debug_module_io_vmeOut_3_cmd_ready),
    .io_vmeOut_3_cmd_valid(debug_module_io_vmeOut_3_cmd_valid),
    .io_vmeOut_3_cmd_bits_addr(debug_module_io_vmeOut_3_cmd_bits_addr),
    .io_vmeOut_3_cmd_bits_len(debug_module_io_vmeOut_3_cmd_bits_len),
    .io_vmeOut_3_data_ready(debug_module_io_vmeOut_3_data_ready),
    .io_vmeOut_3_data_valid(debug_module_io_vmeOut_3_data_valid),
    .io_vmeOut_3_data_bits(debug_module_io_vmeOut_3_data_bits),
    .io_vmeOut_3_ack(debug_module_io_vmeOut_3_ack),
    .io_vmeOut_4_cmd_ready(debug_module_io_vmeOut_4_cmd_ready),
    .io_vmeOut_4_cmd_valid(debug_module_io_vmeOut_4_cmd_valid),
    .io_vmeOut_4_cmd_bits_addr(debug_module_io_vmeOut_4_cmd_bits_addr),
    .io_vmeOut_4_cmd_bits_len(debug_module_io_vmeOut_4_cmd_bits_len),
    .io_vmeOut_4_data_ready(debug_module_io_vmeOut_4_data_ready),
    .io_vmeOut_4_data_valid(debug_module_io_vmeOut_4_data_valid),
    .io_vmeOut_4_data_bits(debug_module_io_vmeOut_4_data_bits),
    .io_vmeOut_4_ack(debug_module_io_vmeOut_4_ack)
  );
  assign io_host_ack = dcr_io_host_ack; // @[DandelionShell.scala 972:11]
  assign io_host_rdata = dcr_io_host_rdata; // @[DandelionShell.scala 972:11]
  assign io_mem_aw_valid = dmem_io_mem_aw_valid; // @[DandelionShell.scala 971:10]
  assign io_mem_aw_bits_addr = dmem_io_mem_aw_bits_addr; // @[DandelionShell.scala 971:10]
  assign io_mem_aw_bits_len = dmem_io_mem_aw_bits_len; // @[DandelionShell.scala 971:10]
  assign io_mem_w_valid = dmem_io_mem_w_valid; // @[DandelionShell.scala 971:10]
  assign io_mem_w_bits_data = dmem_io_mem_w_bits_data; // @[DandelionShell.scala 971:10]
  assign io_mem_w_bits_last = dmem_io_mem_w_bits_last; // @[DandelionShell.scala 971:10]
  assign io_mem_ar_valid = dmem_io_mem_ar_valid; // @[DandelionShell.scala 971:10]
  assign io_mem_ar_bits_addr = dmem_io_mem_ar_bits_addr; // @[DandelionShell.scala 971:10]
  assign io_mem_r_ready = dmem_io_mem_r_ready; // @[DandelionShell.scala 971:10]
  assign dcr_clock = clock;
  assign dcr_reset = reset;
  assign dcr_io_host_addr = io_host_addr; // @[DandelionShell.scala 972:11]
  assign dcr_io_host_wdata = io_host_wdata; // @[DandelionShell.scala 972:11]
  assign dcr_io_host_wr = io_host_wr; // @[DandelionShell.scala 972:11]
  assign dcr_io_host_rd = io_host_rd; // @[DandelionShell.scala 972:11]
  assign dcr_io_dcr_finish = state == 2'h3; // @[DandelionShell.scala 969:21]
  assign dcr_io_dcr_ecnt_0_valid = state == 2'h3; // @[DandelionShell.scala 861:28]
  assign dcr_io_dcr_ecnt_0_bits = cycles; // @[DandelionShell.scala 862:27]
  assign dmem_clock = clock;
  assign dmem_reset = reset;
  assign dmem_io_mem_aw_ready = io_mem_aw_ready; // @[DandelionShell.scala 971:10]
  assign dmem_io_mem_w_ready = io_mem_w_ready; // @[DandelionShell.scala 971:10]
  assign dmem_io_mem_b_valid = io_mem_b_valid; // @[DandelionShell.scala 971:10]
  assign dmem_io_mem_ar_ready = io_mem_ar_ready; // @[DandelionShell.scala 971:10]
  assign dmem_io_mem_r_valid = io_mem_r_valid; // @[DandelionShell.scala 971:10]
  assign dmem_io_mem_r_bits_data = io_mem_r_bits_data; // @[DandelionShell.scala 971:10]
  assign dmem_io_mem_r_bits_last = io_mem_r_bits_last; // @[DandelionShell.scala 971:10]
  assign dmem_io_dme_rd_0_cmd_valid = cache_io_mem_rd_cmd_valid; // @[DandelionShell.scala 835:21]
  assign dmem_io_dme_rd_0_cmd_bits_addr = cache_io_mem_rd_cmd_bits_addr; // @[DandelionShell.scala 835:21]
  assign dmem_io_dme_rd_0_data_ready = cache_io_mem_rd_data_ready; // @[DandelionShell.scala 835:21]
  assign dmem_io_dme_wr_0_cmd_valid = cache_io_mem_wr_cmd_valid; // @[DandelionShell.scala 836:21]
  assign dmem_io_dme_wr_0_cmd_bits_addr = cache_io_mem_wr_cmd_bits_addr; // @[DandelionShell.scala 836:21]
  assign dmem_io_dme_wr_0_data_valid = cache_io_mem_wr_data_valid; // @[DandelionShell.scala 836:21]
  assign dmem_io_dme_wr_0_data_bits = cache_io_mem_wr_data_bits; // @[DandelionShell.scala 836:21]
  assign dmem_io_dme_wr_1_cmd_valid = debug_module_io_vmeOut_0_cmd_valid; // @[DandelionShell.scala 906:37]
  assign dmem_io_dme_wr_1_cmd_bits_addr = debug_module_io_vmeOut_0_cmd_bits_addr; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_1_cmd_bits_len = debug_module_io_vmeOut_0_cmd_bits_len; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_1_data_valid = debug_module_io_vmeOut_0_data_valid; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_1_data_bits = debug_module_io_vmeOut_0_data_bits; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_2_cmd_valid = debug_module_io_vmeOut_1_cmd_valid; // @[DandelionShell.scala 906:37]
  assign dmem_io_dme_wr_2_cmd_bits_addr = debug_module_io_vmeOut_1_cmd_bits_addr; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_2_cmd_bits_len = debug_module_io_vmeOut_1_cmd_bits_len; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_2_data_valid = debug_module_io_vmeOut_1_data_valid; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_2_data_bits = debug_module_io_vmeOut_1_data_bits; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_3_cmd_valid = debug_module_io_vmeOut_2_cmd_valid; // @[DandelionShell.scala 906:37]
  assign dmem_io_dme_wr_3_cmd_bits_addr = debug_module_io_vmeOut_2_cmd_bits_addr; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_3_cmd_bits_len = debug_module_io_vmeOut_2_cmd_bits_len; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_3_data_valid = debug_module_io_vmeOut_2_data_valid; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_3_data_bits = debug_module_io_vmeOut_2_data_bits; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_4_cmd_valid = debug_module_io_vmeOut_3_cmd_valid; // @[DandelionShell.scala 906:37]
  assign dmem_io_dme_wr_4_cmd_bits_addr = debug_module_io_vmeOut_3_cmd_bits_addr; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_4_cmd_bits_len = debug_module_io_vmeOut_3_cmd_bits_len; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_4_data_valid = debug_module_io_vmeOut_3_data_valid; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_4_data_bits = debug_module_io_vmeOut_3_data_bits; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_5_cmd_valid = debug_module_io_vmeOut_4_cmd_valid; // @[DandelionShell.scala 906:37]
  assign dmem_io_dme_wr_5_cmd_bits_addr = debug_module_io_vmeOut_4_cmd_bits_addr; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_5_cmd_bits_len = debug_module_io_vmeOut_4_cmd_bits_len; // @[DandelionShell.scala 905:36]
  assign dmem_io_dme_wr_5_data_valid = debug_module_io_vmeOut_4_data_valid; // @[DandelionShell.scala 909:32]
  assign dmem_io_dme_wr_5_data_bits = debug_module_io_vmeOut_4_data_bits; // @[DandelionShell.scala 909:32]
  assign cache_clock = clock;
  assign cache_reset = reset;
  assign cache_io_cpu_flush = state == 2'h2; // @[DandelionShell.scala 902:22]
  assign cache_io_cpu_req_valid = accel_io_MemReq_valid; // @[DandelionShell.scala 828:20]
  assign cache_io_cpu_req_bits_addr = accel_io_MemReq_bits_addr; // @[DandelionShell.scala 828:20]
  assign cache_io_cpu_req_bits_data = accel_io_MemReq_bits_data; // @[DandelionShell.scala 828:20]
  assign cache_io_cpu_req_bits_mask = accel_io_MemReq_bits_mask; // @[DandelionShell.scala 828:20]
  assign cache_io_cpu_req_bits_tag = accel_io_MemReq_bits_tag; // @[DandelionShell.scala 828:20]
  assign cache_io_mem_rd_cmd_ready = dmem_io_dme_rd_0_cmd_ready; // @[DandelionShell.scala 835:21]
  assign cache_io_mem_rd_data_valid = dmem_io_dme_rd_0_data_valid; // @[DandelionShell.scala 835:21]
  assign cache_io_mem_rd_data_bits = dmem_io_dme_rd_0_data_bits; // @[DandelionShell.scala 835:21]
  assign cache_io_mem_wr_cmd_ready = dmem_io_dme_wr_0_cmd_ready; // @[DandelionShell.scala 836:21]
  assign cache_io_mem_wr_data_ready = dmem_io_dme_wr_0_data_ready; // @[DandelionShell.scala 836:21]
  assign cache_io_mem_wr_ack = dmem_io_dme_wr_0_ack; // @[DandelionShell.scala 836:21]
  assign accel_clock = clock;
  assign accel_reset = reset;
  assign accel_io_in_valid = _T_21 & _GEN_19; // @[DandelionShell.scala 899:21 DandelionShell.scala 947:27]
  assign accel_io_in_bits_dataPtrs_field1_data = ptrs_1; // @[DandelionShell.scala 881:45]
  assign accel_io_in_bits_dataPtrs_field0_data = ptrs_0; // @[DandelionShell.scala 881:45]
  assign accel_io_in_bits_dataVals_field1_data = vals_1; // @[DandelionShell.scala 893:45]
  assign accel_io_in_bits_dataVals_field0_data = vals_0; // @[DandelionShell.scala 893:45]
  assign accel_io_MemResp_valid = cache_io_cpu_resp_valid; // @[DandelionShell.scala 829:20]
  assign accel_io_MemResp_bits_data = cache_io_cpu_resp_bits_data; // @[DandelionShell.scala 829:20]
  assign accel_io_MemResp_bits_tag = cache_io_cpu_resp_bits_tag; // @[DandelionShell.scala 829:20]
  assign accel_io_MemReq_ready = cache_io_cpu_req_ready; // @[DandelionShell.scala 828:20]
  assign accel_io_out_ready = state == 2'h1; // @[DandelionShell.scala 900:22]
  assign debug_module_clock = clock;
  assign debug_module_reset = reset;
  assign debug_module_io_addrDebug_0 = dcr_io_dcr_ptrs_2; // @[DandelionShell.scala 888:38]
  assign debug_module_io_addrDebug_1 = dcr_io_dcr_ptrs_3; // @[DandelionShell.scala 888:38]
  assign debug_module_io_addrDebug_2 = dcr_io_dcr_ptrs_4; // @[DandelionShell.scala 888:38]
  assign debug_module_io_addrDebug_3 = dcr_io_dcr_ptrs_5; // @[DandelionShell.scala 888:38]
  assign debug_module_io_addrDebug_4 = dcr_io_dcr_ptrs_6; // @[DandelionShell.scala 888:38]
  assign debug_module_io_vmeOut_0_cmd_ready = dmem_io_dme_wr_1_cmd_ready; // @[DandelionShell.scala 907:45]
  assign debug_module_io_vmeOut_0_data_ready = dmem_io_dme_wr_1_data_ready; // @[DandelionShell.scala 909:32]
  assign debug_module_io_vmeOut_0_ack = dmem_io_dme_wr_1_ack; // @[DandelionShell.scala 910:39]
  assign debug_module_io_vmeOut_1_cmd_ready = dmem_io_dme_wr_2_cmd_ready; // @[DandelionShell.scala 907:45]
  assign debug_module_io_vmeOut_1_data_ready = dmem_io_dme_wr_2_data_ready; // @[DandelionShell.scala 909:32]
  assign debug_module_io_vmeOut_1_ack = dmem_io_dme_wr_2_ack; // @[DandelionShell.scala 910:39]
  assign debug_module_io_vmeOut_2_cmd_ready = dmem_io_dme_wr_3_cmd_ready; // @[DandelionShell.scala 907:45]
  assign debug_module_io_vmeOut_2_data_ready = dmem_io_dme_wr_3_data_ready; // @[DandelionShell.scala 909:32]
  assign debug_module_io_vmeOut_2_ack = dmem_io_dme_wr_3_ack; // @[DandelionShell.scala 910:39]
  assign debug_module_io_vmeOut_3_cmd_ready = dmem_io_dme_wr_4_cmd_ready; // @[DandelionShell.scala 907:45]
  assign debug_module_io_vmeOut_3_data_ready = dmem_io_dme_wr_4_data_ready; // @[DandelionShell.scala 909:32]
  assign debug_module_io_vmeOut_3_ack = dmem_io_dme_wr_4_ack; // @[DandelionShell.scala 910:39]
  assign debug_module_io_vmeOut_4_cmd_ready = dmem_io_dme_wr_5_cmd_ready; // @[DandelionShell.scala 907:45]
  assign debug_module_io_vmeOut_4_data_ready = dmem_io_dme_wr_5_data_ready; // @[DandelionShell.scala 909:32]
  assign debug_module_io_vmeOut_4_ack = dmem_io_dme_wr_5_ack; // @[DandelionShell.scala 910:39]
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  state = _RAND_0[1:0];
  _RAND_1 = {1{`RANDOM}};
  cycles = _RAND_1[31:0];
  _RAND_2 = {2{`RANDOM}};
  ptrs_0 = _RAND_2[63:0];
  _RAND_3 = {2{`RANDOM}};
  ptrs_1 = _RAND_3[63:0];
  _RAND_4 = {2{`RANDOM}};
  ptrs_2 = _RAND_4[63:0];
  _RAND_5 = {2{`RANDOM}};
  ptrs_3 = _RAND_5[63:0];
  _RAND_6 = {2{`RANDOM}};
  ptrs_4 = _RAND_6[63:0];
  _RAND_7 = {2{`RANDOM}};
  ptrs_5 = _RAND_7[63:0];
  _RAND_8 = {2{`RANDOM}};
  ptrs_6 = _RAND_8[63:0];
  _RAND_9 = {2{`RANDOM}};
  vals_0 = _RAND_9[63:0];
  _RAND_10 = {2{`RANDOM}};
  vals_1 = _RAND_10[63:0];
  _RAND_11 = {1{`RANDOM}};
  dme_ack_0 = _RAND_11[0:0];
  _RAND_12 = {1{`RANDOM}};
  dme_ack_1 = _RAND_12[0:0];
  _RAND_13 = {1{`RANDOM}};
  dme_ack_2 = _RAND_13[0:0];
  _RAND_14 = {1{`RANDOM}};
  dme_ack_3 = _RAND_14[0:0];
  _RAND_15 = {1{`RANDOM}};
  dme_ack_4 = _RAND_15[0:0];
  _RAND_16 = {1{`RANDOM}};
  cache_done = _RAND_16[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      state <= 2'h0;
    end else if (_T_21) begin
      if (dcr_io_dcr_launch) begin
        if (_T_46) begin
          state <= 2'h1;
        end
      end
    end else if (_T_47) begin
      if (_T_48) begin
        state <= 2'h2;
      end
    end else if (_T_49) begin
      if (_T_54) begin
        state <= 2'h3;
      end
    end else if (_T_55) begin
      state <= 2'h0;
    end
    if (reset) begin
      cycles <= 32'h0;
    end else if (_T) begin
      cycles <= 32'h0;
    end else if (_T_1) begin
      cycles <= _T_3;
    end
    if (reset) begin
      ptrs_0 <= 64'h0;
    end else if (_T) begin
      ptrs_0 <= dcr_io_dcr_ptrs_0;
    end
    if (reset) begin
      ptrs_1 <= 64'h0;
    end else if (_T) begin
      ptrs_1 <= dcr_io_dcr_ptrs_1;
    end
    if (reset) begin
      ptrs_2 <= 64'h0;
    end else if (_T) begin
      ptrs_2 <= dcr_io_dcr_ptrs_2;
    end
    if (reset) begin
      ptrs_3 <= 64'h0;
    end else if (_T) begin
      ptrs_3 <= dcr_io_dcr_ptrs_3;
    end
    if (reset) begin
      ptrs_4 <= 64'h0;
    end else if (_T) begin
      ptrs_4 <= dcr_io_dcr_ptrs_4;
    end
    if (reset) begin
      ptrs_5 <= 64'h0;
    end else if (_T) begin
      ptrs_5 <= dcr_io_dcr_ptrs_5;
    end
    if (reset) begin
      ptrs_6 <= 64'h0;
    end else if (_T) begin
      ptrs_6 <= dcr_io_dcr_ptrs_6;
    end
    if (reset) begin
      vals_0 <= 64'h0;
    end else if (_T) begin
      vals_0 <= {{32'd0}, dcr_io_dcr_vals_0};
    end
    if (reset) begin
      vals_1 <= 64'h0;
    end else if (_T) begin
      vals_1 <= {{32'd0}, dcr_io_dcr_vals_1};
    end
    if (reset) begin
      dme_ack_0 <= 1'h0;
    end else begin
      dme_ack_0 <= _GEN_11;
    end
    if (reset) begin
      dme_ack_1 <= 1'h0;
    end else begin
      dme_ack_1 <= _GEN_12;
    end
    if (reset) begin
      dme_ack_2 <= 1'h0;
    end else begin
      dme_ack_2 <= _GEN_13;
    end
    if (reset) begin
      dme_ack_3 <= 1'h0;
    end else begin
      dme_ack_3 <= _GEN_14;
    end
    if (reset) begin
      dme_ack_4 <= 1'h0;
    end else begin
      dme_ack_4 <= _GEN_15;
    end
    if (reset) begin
      cache_done <= 1'h0;
    end else if (_T_18) begin
      cache_done <= _GEN_16;
    end
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"Ptrs: "); // @[DandelionShell.scala 938:15]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"ptr(0): 0x%x, ",ptrs_0); // @[DandelionShell.scala 939:46]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"ptr(1): 0x%x, ",ptrs_1); // @[DandelionShell.scala 939:46]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"ptr(2): 0x%x, ",ptrs_2); // @[DandelionShell.scala 939:46]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"ptr(3): 0x%x, ",ptrs_3); // @[DandelionShell.scala 939:46]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"ptr(4): 0x%x, ",ptrs_4); // @[DandelionShell.scala 939:46]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"ptr(5): 0x%x, ",ptrs_5); // @[DandelionShell.scala 939:46]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"ptr(6): 0x%x, ",ptrs_6); // @[DandelionShell.scala 939:46]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"\nVals: "); // @[DandelionShell.scala 940:15]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"val(0): 0x%x, ",vals_0); // @[DandelionShell.scala 942:48]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"val(1): 0x%x, ",vals_1); // @[DandelionShell.scala 942:48]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_28 & _T_23) begin
          $fwrite(32'h80000002,"\n"); // @[DandelionShell.scala 946:15]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
  end
endmodule
module F1Shell(
  input          ap_clk,
  input          ap_rst_n,
  output         cl_axi_mstr_bus_AWVALID,
  input          cl_axi_mstr_bus_AWREADY,
  output [63:0]  cl_axi_mstr_bus_AWADDR,
  output [15:0]  cl_axi_mstr_bus_AWID,
  output [9:0]   cl_axi_mstr_bus_AWUSER,
  output [7:0]   cl_axi_mstr_bus_AWLEN,
  output [2:0]   cl_axi_mstr_bus_AWSIZE,
  output [1:0]   cl_axi_mstr_bus_AWBURST,
  output [1:0]   cl_axi_mstr_bus_AWLOCK,
  output [3:0]   cl_axi_mstr_bus_AWCACHE,
  output [2:0]   cl_axi_mstr_bus_AWPROT,
  output [3:0]   cl_axi_mstr_bus_AWQOS,
  output [3:0]   cl_axi_mstr_bus_AWREGION,
  output         cl_axi_mstr_bus_WVALID,
  input          cl_axi_mstr_bus_WREADY,
  output [511:0] cl_axi_mstr_bus_WDATA,
  output [63:0]  cl_axi_mstr_bus_WSTRB,
  output         cl_axi_mstr_bus_WLAST,
  output [15:0]  cl_axi_mstr_bus_WID,
  output [9:0]   cl_axi_mstr_bus_WUSER,
  input          cl_axi_mstr_bus_BVALID,
  output         cl_axi_mstr_bus_BREADY,
  input  [1:0]   cl_axi_mstr_bus_BRESP,
  input  [15:0]  cl_axi_mstr_bus_BID,
  input  [9:0]   cl_axi_mstr_bus_BUSER,
  output         cl_axi_mstr_bus_ARVALID,
  input          cl_axi_mstr_bus_ARREADY,
  output [63:0]  cl_axi_mstr_bus_ARADDR,
  output [15:0]  cl_axi_mstr_bus_ARID,
  output [9:0]   cl_axi_mstr_bus_ARUSER,
  output [7:0]   cl_axi_mstr_bus_ARLEN,
  output [2:0]   cl_axi_mstr_bus_ARSIZE,
  output [1:0]   cl_axi_mstr_bus_ARBURST,
  output [1:0]   cl_axi_mstr_bus_ARLOCK,
  output [3:0]   cl_axi_mstr_bus_ARCACHE,
  output [2:0]   cl_axi_mstr_bus_ARPROT,
  output [3:0]   cl_axi_mstr_bus_ARQOS,
  output [3:0]   cl_axi_mstr_bus_ARREGION,
  input          cl_axi_mstr_bus_RVALID,
  output         cl_axi_mstr_bus_RREADY,
  input  [511:0] cl_axi_mstr_bus_RDATA,
  input  [1:0]   cl_axi_mstr_bus_RRESP,
  input          cl_axi_mstr_bus_RLAST,
  input  [15:0]  cl_axi_mstr_bus_RID,
  input  [9:0]   cl_axi_mstr_bus_RUSER,
  input  [15:0]  axi_mstr_cfg_bus_addr,
  input  [31:0]  axi_mstr_cfg_bus_wdata,
  input          axi_mstr_cfg_bus_wr,
  input          axi_mstr_cfg_bus_rd,
  output         axi_mstr_cfg_bus_ack,
  output [31:0]  axi_mstr_cfg_bus_rdata
);
  wire  shell_clock; // @[XilinxShell.scala 33:11]
  wire  shell_reset; // @[XilinxShell.scala 33:11]
  wire [15:0] shell_io_host_addr; // @[XilinxShell.scala 33:11]
  wire [31:0] shell_io_host_wdata; // @[XilinxShell.scala 33:11]
  wire  shell_io_host_wr; // @[XilinxShell.scala 33:11]
  wire  shell_io_host_rd; // @[XilinxShell.scala 33:11]
  wire  shell_io_host_ack; // @[XilinxShell.scala 33:11]
  wire [31:0] shell_io_host_rdata; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_aw_ready; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_aw_valid; // @[XilinxShell.scala 33:11]
  wire [63:0] shell_io_mem_aw_bits_addr; // @[XilinxShell.scala 33:11]
  wire [7:0] shell_io_mem_aw_bits_len; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_w_ready; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_w_valid; // @[XilinxShell.scala 33:11]
  wire [511:0] shell_io_mem_w_bits_data; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_w_bits_last; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_b_valid; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_ar_ready; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_ar_valid; // @[XilinxShell.scala 33:11]
  wire [63:0] shell_io_mem_ar_bits_addr; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_r_ready; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_r_valid; // @[XilinxShell.scala 33:11]
  wire [511:0] shell_io_mem_r_bits_data; // @[XilinxShell.scala 33:11]
  wire  shell_io_mem_r_bits_last; // @[XilinxShell.scala 33:11]
  DandelionDebugFPGAShell shell ( // @[XilinxShell.scala 33:11]
    .clock(shell_clock),
    .reset(shell_reset),
    .io_host_addr(shell_io_host_addr),
    .io_host_wdata(shell_io_host_wdata),
    .io_host_wr(shell_io_host_wr),
    .io_host_rd(shell_io_host_rd),
    .io_host_ack(shell_io_host_ack),
    .io_host_rdata(shell_io_host_rdata),
    .io_mem_aw_ready(shell_io_mem_aw_ready),
    .io_mem_aw_valid(shell_io_mem_aw_valid),
    .io_mem_aw_bits_addr(shell_io_mem_aw_bits_addr),
    .io_mem_aw_bits_len(shell_io_mem_aw_bits_len),
    .io_mem_w_ready(shell_io_mem_w_ready),
    .io_mem_w_valid(shell_io_mem_w_valid),
    .io_mem_w_bits_data(shell_io_mem_w_bits_data),
    .io_mem_w_bits_last(shell_io_mem_w_bits_last),
    .io_mem_b_valid(shell_io_mem_b_valid),
    .io_mem_ar_ready(shell_io_mem_ar_ready),
    .io_mem_ar_valid(shell_io_mem_ar_valid),
    .io_mem_ar_bits_addr(shell_io_mem_ar_bits_addr),
    .io_mem_r_ready(shell_io_mem_r_ready),
    .io_mem_r_valid(shell_io_mem_r_valid),
    .io_mem_r_bits_data(shell_io_mem_r_bits_data),
    .io_mem_r_bits_last(shell_io_mem_r_bits_last)
  );
  assign cl_axi_mstr_bus_AWVALID = shell_io_mem_aw_valid; // @[XilinxShell.scala 40:27]
  assign cl_axi_mstr_bus_AWADDR = shell_io_mem_aw_bits_addr; // @[XilinxShell.scala 42:26]
  assign cl_axi_mstr_bus_AWID = 16'h0; // @[XilinxShell.scala 43:24]
  assign cl_axi_mstr_bus_AWUSER = 10'h0; // @[XilinxShell.scala 44:26]
  assign cl_axi_mstr_bus_AWLEN = shell_io_mem_aw_bits_len; // @[XilinxShell.scala 45:25]
  assign cl_axi_mstr_bus_AWSIZE = 3'h6; // @[XilinxShell.scala 46:26]
  assign cl_axi_mstr_bus_AWBURST = 2'h1; // @[XilinxShell.scala 47:27]
  assign cl_axi_mstr_bus_AWLOCK = 2'h0; // @[XilinxShell.scala 48:26]
  assign cl_axi_mstr_bus_AWCACHE = 4'h3; // @[XilinxShell.scala 49:27]
  assign cl_axi_mstr_bus_AWPROT = 3'h0; // @[XilinxShell.scala 50:26]
  assign cl_axi_mstr_bus_AWQOS = 4'h0; // @[XilinxShell.scala 51:25]
  assign cl_axi_mstr_bus_AWREGION = 4'h0; // @[XilinxShell.scala 52:28]
  assign cl_axi_mstr_bus_WVALID = shell_io_mem_w_valid; // @[XilinxShell.scala 54:26]
  assign cl_axi_mstr_bus_WDATA = shell_io_mem_w_bits_data; // @[XilinxShell.scala 56:25]
  assign cl_axi_mstr_bus_WSTRB = 64'hffffffffffffffff; // @[XilinxShell.scala 57:25]
  assign cl_axi_mstr_bus_WLAST = shell_io_mem_w_bits_last; // @[XilinxShell.scala 58:25]
  assign cl_axi_mstr_bus_WID = 16'h0; // @[XilinxShell.scala 59:23]
  assign cl_axi_mstr_bus_WUSER = 10'h0; // @[XilinxShell.scala 60:25]
  assign cl_axi_mstr_bus_BREADY = shell_io_mem_b_valid; // @[XilinxShell.scala 63:26]
  assign cl_axi_mstr_bus_ARVALID = shell_io_mem_ar_valid; // @[XilinxShell.scala 68:27]
  assign cl_axi_mstr_bus_ARADDR = shell_io_mem_ar_bits_addr; // @[XilinxShell.scala 70:26]
  assign cl_axi_mstr_bus_ARID = 16'h0; // @[XilinxShell.scala 71:24]
  assign cl_axi_mstr_bus_ARUSER = 10'h0; // @[XilinxShell.scala 72:26]
  assign cl_axi_mstr_bus_ARLEN = 8'h0; // @[XilinxShell.scala 73:25]
  assign cl_axi_mstr_bus_ARSIZE = 3'h6; // @[XilinxShell.scala 74:26]
  assign cl_axi_mstr_bus_ARBURST = 2'h1; // @[XilinxShell.scala 75:27]
  assign cl_axi_mstr_bus_ARLOCK = 2'h0; // @[XilinxShell.scala 76:26]
  assign cl_axi_mstr_bus_ARCACHE = 4'h3; // @[XilinxShell.scala 77:27]
  assign cl_axi_mstr_bus_ARPROT = 3'h0; // @[XilinxShell.scala 78:26]
  assign cl_axi_mstr_bus_ARQOS = 4'h0; // @[XilinxShell.scala 79:25]
  assign cl_axi_mstr_bus_ARREGION = 4'h0; // @[XilinxShell.scala 80:28]
  assign cl_axi_mstr_bus_RREADY = shell_io_mem_r_ready; // @[XilinxShell.scala 83:26]
  assign axi_mstr_cfg_bus_ack = shell_io_host_ack; // @[XilinxShell.scala 95:24]
  assign axi_mstr_cfg_bus_rdata = shell_io_host_rdata; // @[XilinxShell.scala 96:26]
  assign shell_clock = ap_clk;
  assign shell_reset = ~ap_rst_n;
  assign shell_io_host_addr = axi_mstr_cfg_bus_addr; // @[XilinxShell.scala 91:22]
  assign shell_io_host_wdata = axi_mstr_cfg_bus_wdata; // @[XilinxShell.scala 92:23]
  assign shell_io_host_wr = axi_mstr_cfg_bus_wr; // @[XilinxShell.scala 93:20]
  assign shell_io_host_rd = axi_mstr_cfg_bus_rd; // @[XilinxShell.scala 94:20]
  assign shell_io_mem_aw_ready = cl_axi_mstr_bus_AWREADY; // @[XilinxShell.scala 41:25]
  assign shell_io_mem_w_ready = cl_axi_mstr_bus_WREADY; // @[XilinxShell.scala 55:24]
  assign shell_io_mem_b_valid = cl_axi_mstr_bus_BVALID; // @[XilinxShell.scala 62:24]
  assign shell_io_mem_ar_ready = cl_axi_mstr_bus_ARREADY; // @[XilinxShell.scala 69:25]
  assign shell_io_mem_r_valid = cl_axi_mstr_bus_RVALID; // @[XilinxShell.scala 82:24]
  assign shell_io_mem_r_bits_data = cl_axi_mstr_bus_RDATA; // @[XilinxShell.scala 84:28]
  assign shell_io_mem_r_bits_last = cl_axi_mstr_bus_RLAST; // @[XilinxShell.scala 86:28]
endmodule

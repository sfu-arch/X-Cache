module DCR(
  input         clock,
  input         reset,
  output        io_host_aw_ready,
  input         io_host_aw_valid,
  input  [15:0] io_host_aw_bits_addr,
  output        io_host_w_ready,
  input         io_host_w_valid,
  input  [31:0] io_host_w_bits_data,
  input         io_host_b_ready,
  output        io_host_b_valid,
  output        io_host_ar_ready,
  input         io_host_ar_valid,
  input  [15:0] io_host_ar_bits_addr,
  input         io_host_r_ready,
  output        io_host_r_valid,
  output [31:0] io_host_r_bits_data,
  output        io_dcr_launch,
  input         io_dcr_finish,
  input         io_dcr_ecnt_0_valid,
  input  [31:0] io_dcr_ecnt_0_bits,
  input         io_dcr_ecnt_1_valid,
  input  [31:0] io_dcr_ecnt_1_bits,
  output [31:0] io_dcr_vals_0,
  output [31:0] io_dcr_vals_1
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
  reg [15:0] waddr; // @[DCR.scala 88:22]
  reg [1:0] wstate; // @[DCR.scala 91:23]
  reg  rstate; // @[DCR.scala 95:23]
  reg [31:0] rdata; // @[DCR.scala 96:22]
  reg [31:0] reg_0; // @[DCR.scala 102:37]
  reg [31:0] reg_1; // @[DCR.scala 102:37]
  reg [31:0] reg_2; // @[DCR.scala 102:37]
  reg [31:0] reg_3; // @[DCR.scala 102:37]
  reg [31:0] reg_4; // @[DCR.scala 102:37]
  wire  _T = 2'h0 == wstate; // @[Conditional.scala 37:30]
  wire  _T_1 = 2'h1 == wstate; // @[Conditional.scala 37:30]
  wire  _T_2 = 2'h2 == wstate; // @[Conditional.scala 37:30]
  wire  _T_3 = io_host_aw_ready & io_host_aw_valid; // @[Decoupled.scala 40:37]
  wire  _T_7 = ~rstate; // @[Conditional.scala 37:30]
  wire  _GEN_7 = io_host_ar_valid | rstate; // @[DCR.scala 138:30]
  wire  _T_11 = io_host_w_ready & io_host_w_valid; // @[Decoupled.scala 40:37]
  wire  _T_12 = 16'h0 == waddr; // @[DCR.scala 156:44]
  wire  _T_13 = _T_11 & _T_12; // @[DCR.scala 156:31]
  wire  _T_15 = 16'h4 == waddr; // @[DCR.scala 163:51]
  wire  _T_16 = _T_11 & _T_15; // @[DCR.scala 163:33]
  wire  _T_18 = 16'h8 == waddr; // @[DCR.scala 163:51]
  wire  _T_19 = _T_11 & _T_18; // @[DCR.scala 163:33]
  wire  _T_21 = 16'hc == waddr; // @[DCR.scala 169:45]
  wire  _T_22 = _T_11 & _T_21; // @[DCR.scala 169:27]
  wire  _T_24 = 16'h10 == waddr; // @[DCR.scala 169:45]
  wire  _T_25 = _T_11 & _T_24; // @[DCR.scala 169:27]
  wire  _T_26 = io_host_ar_ready & io_host_ar_valid; // @[Decoupled.scala 40:37]
  wire  _T_27 = 16'h0 == io_host_ar_bits_addr; // @[Mux.scala 80:60]
  wire  _T_29 = 16'h4 == io_host_ar_bits_addr; // @[Mux.scala 80:60]
  wire  _T_31 = 16'h8 == io_host_ar_bits_addr; // @[Mux.scala 80:60]
  wire  _T_33 = 16'hc == io_host_ar_bits_addr; // @[Mux.scala 80:60]
  wire  _T_35 = 16'h10 == io_host_ar_bits_addr; // @[Mux.scala 80:60]
  assign io_host_aw_ready = wstate == 2'h0; // @[DCR.scala 131:20]
  assign io_host_w_ready = wstate == 2'h1; // @[DCR.scala 132:19]
  assign io_host_b_valid = wstate == 2'h2; // @[DCR.scala 133:19]
  assign io_host_ar_ready = ~rstate; // @[DCR.scala 149:20]
  assign io_host_r_valid = rstate; // @[DCR.scala 150:19]
  assign io_host_r_bits_data = rdata; // @[DCR.scala 151:23]
  assign io_dcr_launch = reg_0[0]; // @[DCR.scala 178:17]
  assign io_dcr_vals_0 = reg_3; // @[DCR.scala 181:20]
  assign io_dcr_vals_1 = reg_4; // @[DCR.scala 181:20]
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
  rstate = _RAND_2[0:0];
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
    end else if (_T_3) begin
      waddr <= io_host_aw_bits_addr;
    end
    if (reset) begin
      wstate <= 2'h0;
    end else if (_T) begin
      if (io_host_aw_valid) begin
        wstate <= 2'h1;
      end
    end else if (_T_1) begin
      if (io_host_w_valid) begin
        wstate <= 2'h2;
      end
    end else if (_T_2) begin
      if (io_host_b_ready) begin
        wstate <= 2'h0;
      end
    end
    if (reset) begin
      rstate <= 1'h0;
    end else if (_T_7) begin
      rstate <= _GEN_7;
    end else if (rstate) begin
      if (io_host_r_ready) begin
        rstate <= 1'h0;
      end
    end
    if (reset) begin
      rdata <= 32'h0;
    end else if (_T_26) begin
      if (_T_35) begin
        rdata <= reg_4;
      end else if (_T_33) begin
        rdata <= reg_3;
      end else if (_T_31) begin
        rdata <= reg_2;
      end else if (_T_29) begin
        rdata <= reg_1;
      end else if (_T_27) begin
        rdata <= reg_0;
      end else begin
        rdata <= 32'h0;
      end
    end
    if (reset) begin
      reg_0 <= 32'h0;
    end else if (io_dcr_finish) begin
      reg_0 <= 32'h2;
    end else if (_T_13) begin
      reg_0 <= io_host_w_bits_data;
    end
    if (reset) begin
      reg_1 <= 32'h0;
    end else if (io_dcr_ecnt_0_valid) begin
      reg_1 <= io_dcr_ecnt_0_bits;
    end else if (_T_16) begin
      reg_1 <= io_host_w_bits_data;
    end
    if (reset) begin
      reg_2 <= 32'h0;
    end else if (io_dcr_ecnt_1_valid) begin
      reg_2 <= io_dcr_ecnt_1_bits;
    end else if (_T_19) begin
      reg_2 <= io_host_w_bits_data;
    end
    if (reset) begin
      reg_3 <= 32'h0;
    end else if (_T_22) begin
      reg_3 <= io_host_w_bits_data;
    end
    if (reset) begin
      reg_4 <= 32'h0;
    end else if (_T_25) begin
      reg_4 <= io_host_w_bits_data;
    end
  end
endmodule
module Arbiter(
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [31:0] io_in_0_bits_addr,
  input         io_out_ready,
  output        io_out_valid,
  output [31:0] io_out_bits_addr
);
  assign io_in_0_ready = io_out_ready; // @[Arbiter.scala 134:14]
  assign io_out_valid = io_in_0_valid; // @[Arbiter.scala 135:16]
  assign io_out_bits_addr = io_in_0_bits_addr; // @[Arbiter.scala 124:15]
endmodule
module DME(
  input         clock,
  input         reset,
  input         io_mem_aw_ready,
  output        io_mem_aw_valid,
  output [31:0] io_mem_aw_bits_addr,
  output [7:0]  io_mem_aw_bits_len,
  input         io_mem_w_ready,
  output        io_mem_w_valid,
  output [63:0] io_mem_w_bits_data,
  output        io_mem_w_bits_last,
  output        io_mem_b_ready,
  input         io_mem_b_valid,
  input         io_mem_ar_ready,
  output        io_mem_ar_valid,
  output [31:0] io_mem_ar_bits_addr,
  output [7:0]  io_mem_ar_bits_len,
  output        io_mem_r_ready,
  input         io_mem_r_valid,
  input  [63:0] io_mem_r_bits_data,
  input         io_mem_r_bits_last,
  output        io_dme_rd_0_cmd_ready,
  input         io_dme_rd_0_cmd_valid,
  input         io_dme_rd_0_data_ready,
  output        io_dme_rd_0_data_valid,
  output [63:0] io_dme_rd_0_data_bits,
  output        io_dme_wr_0_cmd_ready,
  input         io_dme_wr_0_cmd_valid,
  input  [31:0] io_dme_wr_0_cmd_bits_addr,
  output        io_dme_wr_0_data_ready,
  input         io_dme_wr_0_data_valid,
  input  [63:0] io_dme_wr_0_data_bits,
  output        io_dme_wr_0_ack
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
  wire  rd_arb_io_in_0_ready; // @[DME.scala 130:22]
  wire  rd_arb_io_in_0_valid; // @[DME.scala 130:22]
  wire [31:0] rd_arb_io_in_0_bits_addr; // @[DME.scala 130:22]
  wire  rd_arb_io_out_ready; // @[DME.scala 130:22]
  wire  rd_arb_io_out_valid; // @[DME.scala 130:22]
  wire [31:0] rd_arb_io_out_bits_addr; // @[DME.scala 130:22]
  wire  wr_arb_io_in_0_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_in_0_valid; // @[DME.scala 160:22]
  wire [31:0] wr_arb_io_in_0_bits_addr; // @[DME.scala 160:22]
  wire  wr_arb_io_out_ready; // @[DME.scala 160:22]
  wire  wr_arb_io_out_valid; // @[DME.scala 160:22]
  wire [31:0] wr_arb_io_out_bits_addr; // @[DME.scala 160:22]
  wire  _T = rd_arb_io_out_ready & rd_arb_io_out_valid; // @[Decoupled.scala 40:37]
  reg [1:0] rstate; // @[DME.scala 138:23]
  wire  _T_1 = 2'h0 == rstate; // @[Conditional.scala 37:30]
  wire  _T_2 = 2'h1 == rstate; // @[Conditional.scala 37:30]
  wire  _T_3 = 2'h2 == rstate; // @[Conditional.scala 37:30]
  wire  _T_4 = io_mem_r_ready & io_mem_r_valid; // @[Decoupled.scala 40:37]
  wire  _T_5 = _T_4 & io_mem_r_bits_last; // @[DME.scala 152:28]
  wire  _T_6 = wr_arb_io_out_ready & wr_arb_io_out_valid; // @[Decoupled.scala 40:37]
  reg [1:0] wstate; // @[DME.scala 168:23]
  reg [7:0] wr_cnt; // @[DME.scala 171:23]
  wire  _T_7 = wstate == 2'h0; // @[DME.scala 174:15]
  wire  _T_8 = io_mem_w_ready & io_mem_w_valid; // @[Decoupled.scala 40:37]
  wire [7:0] _T_10 = wr_cnt + 8'h1; // @[DME.scala 177:22]
  wire  _T_11 = 2'h0 == wstate; // @[Conditional.scala 37:30]
  wire  _T_12 = 2'h1 == wstate; // @[Conditional.scala 37:30]
  wire  _T_13 = 2'h2 == wstate; // @[Conditional.scala 37:30]
  wire  _T_14 = io_dme_wr_0_data_valid & io_mem_w_ready; // @[DME.scala 193:45]
  wire  _T_15 = wr_cnt == 8'h7; // @[DME.scala 193:73]
  wire  _T_16 = _T_14 & _T_15; // @[DME.scala 193:63]
  wire  _T_17 = 2'h3 == wstate; // @[Conditional.scala 37:30]
  reg [7:0] rd_len; // @[Reg.scala 27:20]
  reg [31:0] rd_addr; // @[Reg.scala 27:20]
  reg [7:0] wr_len; // @[Reg.scala 27:20]
  reg [31:0] wr_addr; // @[Reg.scala 27:20]
  wire  _T_30 = wstate == 2'h2; // @[DME.scala 222:67]
  wire  _T_39 = rstate == 2'h2; // @[DME.scala 240:28]
  Arbiter rd_arb ( // @[DME.scala 130:22]
    .io_in_0_ready(rd_arb_io_in_0_ready),
    .io_in_0_valid(rd_arb_io_in_0_valid),
    .io_in_0_bits_addr(rd_arb_io_in_0_bits_addr),
    .io_out_ready(rd_arb_io_out_ready),
    .io_out_valid(rd_arb_io_out_valid),
    .io_out_bits_addr(rd_arb_io_out_bits_addr)
  );
  Arbiter wr_arb ( // @[DME.scala 160:22]
    .io_in_0_ready(wr_arb_io_in_0_ready),
    .io_in_0_valid(wr_arb_io_in_0_valid),
    .io_in_0_bits_addr(wr_arb_io_in_0_bits_addr),
    .io_out_ready(wr_arb_io_out_ready),
    .io_out_valid(wr_arb_io_out_valid),
    .io_out_bits_addr(wr_arb_io_out_bits_addr)
  );
  assign io_mem_aw_valid = wstate == 2'h1; // @[DME.scala 226:19]
  assign io_mem_aw_bits_addr = wr_addr; // @[DME.scala 227:23]
  assign io_mem_aw_bits_len = wr_len; // @[DME.scala 228:22]
  assign io_mem_w_valid = _T_30 & io_dme_wr_0_data_valid; // @[DME.scala 230:18]
  assign io_mem_w_bits_data = io_dme_wr_0_data_bits; // @[DME.scala 231:22]
  assign io_mem_w_bits_last = wr_cnt == 8'h7; // @[DME.scala 232:22]
  assign io_mem_b_ready = wstate == 2'h3; // @[DME.scala 234:18]
  assign io_mem_ar_valid = rstate == 2'h1; // @[DME.scala 236:19]
  assign io_mem_ar_bits_addr = rd_addr; // @[DME.scala 237:23]
  assign io_mem_ar_bits_len = rd_len; // @[DME.scala 238:22]
  assign io_mem_r_ready = _T_39 & io_dme_rd_0_data_ready; // @[DME.scala 240:18]
  assign io_dme_rd_0_cmd_ready = rd_arb_io_in_0_ready; // @[DME.scala 134:21]
  assign io_dme_rd_0_data_valid = io_mem_r_valid; // @[DME.scala 215:29]
  assign io_dme_rd_0_data_bits = io_mem_r_bits_data; // @[DME.scala 216:28]
  assign io_dme_wr_0_cmd_ready = wr_arb_io_in_0_ready; // @[DME.scala 164:21]
  assign io_dme_wr_0_data_ready = _T_30 & io_mem_w_ready; // @[DME.scala 222:29]
  assign io_dme_wr_0_ack = io_mem_b_ready & io_mem_b_valid; // @[DME.scala 221:22]
  assign rd_arb_io_in_0_valid = io_dme_rd_0_cmd_valid; // @[DME.scala 134:21]
  assign rd_arb_io_in_0_bits_addr = 32'h0; // @[DME.scala 134:21]
  assign rd_arb_io_out_ready = rstate == 2'h0; // @[DME.scala 210:23]
  assign wr_arb_io_in_0_valid = io_dme_wr_0_cmd_valid; // @[DME.scala 164:21]
  assign wr_arb_io_in_0_bits_addr = io_dme_wr_0_cmd_bits_addr; // @[DME.scala 164:21]
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
  wstate = _RAND_1[1:0];
  _RAND_2 = {1{`RANDOM}};
  wr_cnt = _RAND_2[7:0];
  _RAND_3 = {1{`RANDOM}};
  rd_len = _RAND_3[7:0];
  _RAND_4 = {1{`RANDOM}};
  rd_addr = _RAND_4[31:0];
  _RAND_5 = {1{`RANDOM}};
  wr_len = _RAND_5[7:0];
  _RAND_6 = {1{`RANDOM}};
  wr_addr = _RAND_6[31:0];
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
      rd_len <= 8'h0;
    end else if (_T) begin
      rd_len <= 8'h7;
    end
    if (reset) begin
      rd_addr <= 32'h0;
    end else if (_T) begin
      rd_addr <= rd_arb_io_out_bits_addr;
    end
    if (reset) begin
      wr_len <= 8'h0;
    end else if (_T_6) begin
      wr_len <= 8'h7;
    end
    if (reset) begin
      wr_addr <= 32'h0;
    end else if (_T_6) begin
      wr_addr <= wr_arb_io_out_bits_addr;
    end
  end
endmodule
module DMECache(
  input         clock,
  input         reset,
  input         io_cpu_flush,
  output        io_cpu_flush_done,
  input         io_mem_rd_cmd_ready,
  output        io_mem_rd_cmd_valid,
  output        io_mem_rd_data_ready,
  input         io_mem_rd_data_valid,
  input  [63:0] io_mem_rd_data_bits,
  input         io_mem_wr_cmd_ready,
  output        io_mem_wr_cmd_valid,
  output [31:0] io_mem_wr_cmd_bits_addr,
  input         io_mem_wr_data_ready,
  output        io_mem_wr_data_valid,
  output [63:0] io_mem_wr_data_bits,
  input         io_mem_wr_ack
);
`ifdef RANDOMIZE_MEM_INIT
  reg [63:0] _RAND_0;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_21;
  reg [31:0] _RAND_23;
  reg [31:0] _RAND_25;
  reg [31:0] _RAND_27;
  reg [31:0] _RAND_29;
  reg [31:0] _RAND_31;
  reg [31:0] _RAND_33;
  reg [31:0] _RAND_35;
  reg [31:0] _RAND_37;
  reg [31:0] _RAND_39;
  reg [31:0] _RAND_41;
  reg [31:0] _RAND_43;
  reg [31:0] _RAND_45;
  reg [31:0] _RAND_47;
  reg [31:0] _RAND_49;
  reg [31:0] _RAND_51;
  reg [31:0] _RAND_53;
  reg [31:0] _RAND_55;
  reg [31:0] _RAND_57;
  reg [31:0] _RAND_59;
  reg [31:0] _RAND_61;
  reg [31:0] _RAND_63;
  reg [31:0] _RAND_65;
  reg [31:0] _RAND_67;
  reg [31:0] _RAND_69;
  reg [31:0] _RAND_71;
  reg [31:0] _RAND_73;
  reg [31:0] _RAND_75;
  reg [31:0] _RAND_77;
  reg [31:0] _RAND_79;
  reg [31:0] _RAND_81;
  reg [31:0] _RAND_83;
  reg [31:0] _RAND_85;
  reg [31:0] _RAND_87;
  reg [31:0] _RAND_89;
  reg [31:0] _RAND_91;
  reg [31:0] _RAND_93;
  reg [31:0] _RAND_95;
  reg [31:0] _RAND_97;
  reg [31:0] _RAND_99;
  reg [31:0] _RAND_101;
  reg [31:0] _RAND_103;
  reg [31:0] _RAND_105;
  reg [31:0] _RAND_107;
  reg [31:0] _RAND_109;
  reg [31:0] _RAND_111;
  reg [31:0] _RAND_113;
  reg [31:0] _RAND_115;
  reg [31:0] _RAND_117;
  reg [31:0] _RAND_119;
  reg [31:0] _RAND_121;
  reg [31:0] _RAND_123;
  reg [31:0] _RAND_125;
  reg [31:0] _RAND_127;
  reg [31:0] _RAND_129;
`endif // RANDOMIZE_MEM_INIT
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_22;
  reg [31:0] _RAND_24;
  reg [31:0] _RAND_26;
  reg [31:0] _RAND_28;
  reg [31:0] _RAND_30;
  reg [31:0] _RAND_32;
  reg [31:0] _RAND_34;
  reg [31:0] _RAND_36;
  reg [31:0] _RAND_38;
  reg [31:0] _RAND_40;
  reg [31:0] _RAND_42;
  reg [31:0] _RAND_44;
  reg [31:0] _RAND_46;
  reg [31:0] _RAND_48;
  reg [31:0] _RAND_50;
  reg [31:0] _RAND_52;
  reg [31:0] _RAND_54;
  reg [31:0] _RAND_56;
  reg [31:0] _RAND_58;
  reg [31:0] _RAND_60;
  reg [31:0] _RAND_62;
  reg [31:0] _RAND_64;
  reg [31:0] _RAND_66;
  reg [31:0] _RAND_68;
  reg [31:0] _RAND_70;
  reg [31:0] _RAND_72;
  reg [31:0] _RAND_74;
  reg [31:0] _RAND_76;
  reg [31:0] _RAND_78;
  reg [31:0] _RAND_80;
  reg [31:0] _RAND_82;
  reg [31:0] _RAND_84;
  reg [31:0] _RAND_86;
  reg [31:0] _RAND_88;
  reg [31:0] _RAND_90;
  reg [31:0] _RAND_92;
  reg [31:0] _RAND_94;
  reg [31:0] _RAND_96;
  reg [31:0] _RAND_98;
  reg [31:0] _RAND_100;
  reg [31:0] _RAND_102;
  reg [31:0] _RAND_104;
  reg [31:0] _RAND_106;
  reg [31:0] _RAND_108;
  reg [31:0] _RAND_110;
  reg [31:0] _RAND_112;
  reg [31:0] _RAND_114;
  reg [31:0] _RAND_116;
  reg [31:0] _RAND_118;
  reg [31:0] _RAND_120;
  reg [31:0] _RAND_122;
  reg [31:0] _RAND_124;
  reg [31:0] _RAND_126;
  reg [31:0] _RAND_128;
  reg [31:0] _RAND_130;
  reg [31:0] _RAND_131;
  reg [31:0] _RAND_132;
  reg [31:0] _RAND_133;
  reg [255:0] _RAND_134;
  reg [255:0] _RAND_135;
  reg [31:0] _RAND_136;
  reg [31:0] _RAND_137;
  reg [31:0] _RAND_138;
  reg [63:0] _RAND_139;
  reg [31:0] _RAND_140;
  reg [63:0] _RAND_141;
  reg [63:0] _RAND_142;
  reg [63:0] _RAND_143;
  reg [63:0] _RAND_144;
  reg [63:0] _RAND_145;
  reg [63:0] _RAND_146;
  reg [63:0] _RAND_147;
  reg [63:0] _RAND_148;
`endif // RANDOMIZE_REG_INIT
  reg [49:0] metaMem_tag [0:255]; // @[AXICache.scala 720:28]
  wire [49:0] metaMem_tag_rmeta_data; // @[AXICache.scala 720:28]
  wire [7:0] metaMem_tag_rmeta_addr; // @[AXICache.scala 720:28]
  wire [49:0] metaMem_tag__T_431_data; // @[AXICache.scala 720:28]
  wire [7:0] metaMem_tag__T_431_addr; // @[AXICache.scala 720:28]
  wire [49:0] metaMem_tag__T_262_data; // @[AXICache.scala 720:28]
  wire [7:0] metaMem_tag__T_262_addr; // @[AXICache.scala 720:28]
  wire  metaMem_tag__T_262_mask; // @[AXICache.scala 720:28]
  wire  metaMem_tag__T_262_en; // @[AXICache.scala 720:28]
  reg  metaMem_tag__T_431_en_pipe_0;
  reg [7:0] metaMem_tag__T_431_addr_pipe_0;
  reg [7:0] dataMem_0_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_0__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_0__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_0__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_0__T_14_addr_pipe_0;
  reg [7:0] dataMem_0_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_1__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_1__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_1__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_1__T_14_addr_pipe_0;
  reg [7:0] dataMem_0_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_2__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_2__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_2__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_2__T_14_addr_pipe_0;
  reg [7:0] dataMem_0_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_3__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_3__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_3__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_3__T_14_addr_pipe_0;
  reg [7:0] dataMem_0_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_4__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_4__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_4__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_4__T_14_addr_pipe_0;
  reg [7:0] dataMem_0_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_5__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_5__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_5__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_5__T_14_addr_pipe_0;
  reg [7:0] dataMem_0_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_6__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_6__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_6__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_6__T_14_addr_pipe_0;
  reg [7:0] dataMem_0_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_14_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_14_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_112_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_112_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_281_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_0_7__T_281_addr; // @[AXICache.scala 721:45]
  wire  dataMem_0_7__T_281_mask; // @[AXICache.scala 721:45]
  wire  dataMem_0_7__T_281_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_0_7__T_14_addr_pipe_0;
  reg [7:0] dataMem_1_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_0__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_0__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_0__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_0__T_24_addr_pipe_0;
  reg [7:0] dataMem_1_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_1__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_1__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_1__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_1__T_24_addr_pipe_0;
  reg [7:0] dataMem_1_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_2__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_2__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_2__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_2__T_24_addr_pipe_0;
  reg [7:0] dataMem_1_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_3__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_3__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_3__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_3__T_24_addr_pipe_0;
  reg [7:0] dataMem_1_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_4__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_4__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_4__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_4__T_24_addr_pipe_0;
  reg [7:0] dataMem_1_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_5__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_5__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_5__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_5__T_24_addr_pipe_0;
  reg [7:0] dataMem_1_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_6__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_6__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_6__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_6__T_24_addr_pipe_0;
  reg [7:0] dataMem_1_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_24_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_24_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_123_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_123_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_300_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_1_7__T_300_addr; // @[AXICache.scala 721:45]
  wire  dataMem_1_7__T_300_mask; // @[AXICache.scala 721:45]
  wire  dataMem_1_7__T_300_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_1_7__T_24_addr_pipe_0;
  reg [7:0] dataMem_2_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_0__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_0__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_0__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_0__T_34_addr_pipe_0;
  reg [7:0] dataMem_2_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_1__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_1__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_1__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_1__T_34_addr_pipe_0;
  reg [7:0] dataMem_2_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_2__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_2__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_2__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_2__T_34_addr_pipe_0;
  reg [7:0] dataMem_2_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_3__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_3__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_3__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_3__T_34_addr_pipe_0;
  reg [7:0] dataMem_2_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_4__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_4__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_4__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_4__T_34_addr_pipe_0;
  reg [7:0] dataMem_2_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_5__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_5__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_5__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_5__T_34_addr_pipe_0;
  reg [7:0] dataMem_2_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_6__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_6__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_6__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_6__T_34_addr_pipe_0;
  reg [7:0] dataMem_2_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_34_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_34_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_134_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_134_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_319_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_2_7__T_319_addr; // @[AXICache.scala 721:45]
  wire  dataMem_2_7__T_319_mask; // @[AXICache.scala 721:45]
  wire  dataMem_2_7__T_319_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_2_7__T_34_addr_pipe_0;
  reg [7:0] dataMem_3_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_0__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_0__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_0__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_0__T_44_addr_pipe_0;
  reg [7:0] dataMem_3_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_1__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_1__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_1__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_1__T_44_addr_pipe_0;
  reg [7:0] dataMem_3_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_2__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_2__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_2__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_2__T_44_addr_pipe_0;
  reg [7:0] dataMem_3_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_3__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_3__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_3__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_3__T_44_addr_pipe_0;
  reg [7:0] dataMem_3_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_4__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_4__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_4__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_4__T_44_addr_pipe_0;
  reg [7:0] dataMem_3_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_5__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_5__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_5__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_5__T_44_addr_pipe_0;
  reg [7:0] dataMem_3_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_6__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_6__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_6__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_6__T_44_addr_pipe_0;
  reg [7:0] dataMem_3_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_44_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_44_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_145_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_145_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_338_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_3_7__T_338_addr; // @[AXICache.scala 721:45]
  wire  dataMem_3_7__T_338_mask; // @[AXICache.scala 721:45]
  wire  dataMem_3_7__T_338_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_3_7__T_44_addr_pipe_0;
  reg [7:0] dataMem_4_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_0__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_0__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_0__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_0__T_54_addr_pipe_0;
  reg [7:0] dataMem_4_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_1__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_1__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_1__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_1__T_54_addr_pipe_0;
  reg [7:0] dataMem_4_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_2__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_2__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_2__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_2__T_54_addr_pipe_0;
  reg [7:0] dataMem_4_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_3__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_3__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_3__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_3__T_54_addr_pipe_0;
  reg [7:0] dataMem_4_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_4__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_4__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_4__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_4__T_54_addr_pipe_0;
  reg [7:0] dataMem_4_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_5__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_5__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_5__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_5__T_54_addr_pipe_0;
  reg [7:0] dataMem_4_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_6__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_6__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_6__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_6__T_54_addr_pipe_0;
  reg [7:0] dataMem_4_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_54_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_54_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_156_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_156_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_357_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_4_7__T_357_addr; // @[AXICache.scala 721:45]
  wire  dataMem_4_7__T_357_mask; // @[AXICache.scala 721:45]
  wire  dataMem_4_7__T_357_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_4_7__T_54_addr_pipe_0;
  reg [7:0] dataMem_5_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_0__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_0__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_0__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_0__T_64_addr_pipe_0;
  reg [7:0] dataMem_5_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_1__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_1__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_1__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_1__T_64_addr_pipe_0;
  reg [7:0] dataMem_5_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_2__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_2__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_2__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_2__T_64_addr_pipe_0;
  reg [7:0] dataMem_5_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_3__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_3__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_3__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_3__T_64_addr_pipe_0;
  reg [7:0] dataMem_5_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_4__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_4__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_4__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_4__T_64_addr_pipe_0;
  reg [7:0] dataMem_5_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_5__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_5__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_5__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_5__T_64_addr_pipe_0;
  reg [7:0] dataMem_5_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_6__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_6__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_6__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_6__T_64_addr_pipe_0;
  reg [7:0] dataMem_5_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_64_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_64_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_167_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_167_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_376_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_5_7__T_376_addr; // @[AXICache.scala 721:45]
  wire  dataMem_5_7__T_376_mask; // @[AXICache.scala 721:45]
  wire  dataMem_5_7__T_376_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_5_7__T_64_addr_pipe_0;
  reg [7:0] dataMem_6_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_0__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_0__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_0__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_0__T_74_addr_pipe_0;
  reg [7:0] dataMem_6_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_1__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_1__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_1__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_1__T_74_addr_pipe_0;
  reg [7:0] dataMem_6_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_2__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_2__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_2__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_2__T_74_addr_pipe_0;
  reg [7:0] dataMem_6_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_3__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_3__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_3__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_3__T_74_addr_pipe_0;
  reg [7:0] dataMem_6_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_4__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_4__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_4__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_4__T_74_addr_pipe_0;
  reg [7:0] dataMem_6_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_5__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_5__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_5__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_5__T_74_addr_pipe_0;
  reg [7:0] dataMem_6_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_6__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_6__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_6__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_6__T_74_addr_pipe_0;
  reg [7:0] dataMem_6_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_74_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_74_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_178_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_178_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_395_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_6_7__T_395_addr; // @[AXICache.scala 721:45]
  wire  dataMem_6_7__T_395_mask; // @[AXICache.scala 721:45]
  wire  dataMem_6_7__T_395_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_6_7__T_74_addr_pipe_0;
  reg [7:0] dataMem_7_0 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_0__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_0__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_0__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_0__T_84_addr_pipe_0;
  reg [7:0] dataMem_7_1 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_1__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_1__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_1__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_1__T_84_addr_pipe_0;
  reg [7:0] dataMem_7_2 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_2__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_2__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_2__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_2__T_84_addr_pipe_0;
  reg [7:0] dataMem_7_3 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_3__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_3__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_3__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_3__T_84_addr_pipe_0;
  reg [7:0] dataMem_7_4 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_4__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_4__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_4__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_4__T_84_addr_pipe_0;
  reg [7:0] dataMem_7_5 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_5__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_5__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_5__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_5__T_84_addr_pipe_0;
  reg [7:0] dataMem_7_6 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_6__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_6__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_6__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_6__T_84_addr_pipe_0;
  reg [7:0] dataMem_7_7 [0:255]; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_84_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_84_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_189_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_189_addr; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_414_data; // @[AXICache.scala 721:45]
  wire [7:0] dataMem_7_7__T_414_addr; // @[AXICache.scala 721:45]
  wire  dataMem_7_7__T_414_mask; // @[AXICache.scala 721:45]
  wire  dataMem_7_7__T_414_en; // @[AXICache.scala 721:45]
  reg [7:0] dataMem_7_7__T_84_addr_pipe_0;
  reg [2:0] state; // @[AXICache.scala 711:22]
  reg [2:0] flush_state; // @[AXICache.scala 714:28]
  reg  flush_mode; // @[AXICache.scala 715:27]
  reg [255:0] v; // @[AXICache.scala 718:18]
  reg [255:0] d; // @[AXICache.scala 719:18]
  wire  _T = io_mem_rd_data_ready & io_mem_rd_data_valid; // @[Decoupled.scala 40:37]
  reg [2:0] read_count; // @[Counter.scala 29:33]
  wire  _T_1 = read_count == 3'h7; // @[Counter.scala 38:24]
  wire [2:0] _T_3 = read_count + 3'h1; // @[Counter.scala 39:22]
  wire  read_wrap_out = _T & _T_1; // @[Counter.scala 67:17]
  wire  _T_4 = io_mem_wr_data_ready & io_mem_wr_data_valid; // @[Decoupled.scala 40:37]
  reg [2:0] write_count; // @[Counter.scala 29:33]
  wire  _T_5 = write_count == 3'h7; // @[Counter.scala 38:24]
  wire [2:0] _T_7 = write_count + 3'h1; // @[Counter.scala 39:22]
  wire  write_wrap_out = _T_4 & _T_5; // @[Counter.scala 67:17]
  wire  _T_8 = flush_state == 3'h1; // @[AXICache.scala 734:51]
  reg [7:0] set_count; // @[Counter.scala 29:33]
  wire  _T_9 = set_count == 8'hff; // @[Counter.scala 38:24]
  wire [7:0] _T_11 = set_count + 8'h1; // @[Counter.scala 39:22]
  wire  set_wrap = _T_8 & _T_9; // @[Counter.scala 67:17]
  wire [7:0] _T_13 = set_count - 8'h1; // @[AXICache.scala 735:62]
  wire [63:0] _T_21 = {dataMem_0_7__T_14_data,dataMem_0_6__T_14_data,dataMem_0_5__T_14_data,dataMem_0_4__T_14_data,dataMem_0_3__T_14_data,dataMem_0_2__T_14_data,dataMem_0_1__T_14_data,dataMem_0_0__T_14_data}; // @[AXICache.scala 735:69]
  wire [63:0] _T_41 = {dataMem_2_7__T_34_data,dataMem_2_6__T_34_data,dataMem_2_5__T_34_data,dataMem_2_4__T_34_data,dataMem_2_3__T_34_data,dataMem_2_2__T_34_data,dataMem_2_1__T_34_data,dataMem_2_0__T_34_data}; // @[AXICache.scala 735:69]
  wire [63:0] _T_61 = {dataMem_4_7__T_54_data,dataMem_4_6__T_54_data,dataMem_4_5__T_54_data,dataMem_4_4__T_54_data,dataMem_4_3__T_54_data,dataMem_4_2__T_54_data,dataMem_4_1__T_54_data,dataMem_4_0__T_54_data}; // @[AXICache.scala 735:69]
  wire [63:0] _T_81 = {dataMem_6_7__T_74_data,dataMem_6_6__T_74_data,dataMem_6_5__T_74_data,dataMem_6_4__T_74_data,dataMem_6_3__T_74_data,dataMem_6_2__T_74_data,dataMem_6_1__T_74_data,dataMem_6_0__T_74_data}; // @[AXICache.scala 735:69]
  wire [127:0] _T_92 = {dataMem_1_7__T_24_data,dataMem_1_6__T_24_data,dataMem_1_5__T_24_data,dataMem_1_4__T_24_data,dataMem_1_3__T_24_data,dataMem_1_2__T_24_data,dataMem_1_1__T_24_data,dataMem_1_0__T_24_data,_T_21}; // @[Cat.scala 29:58]
  wire [255:0] _T_94 = {dataMem_3_7__T_44_data,dataMem_3_6__T_44_data,dataMem_3_5__T_44_data,dataMem_3_4__T_44_data,dataMem_3_3__T_44_data,dataMem_3_2__T_44_data,dataMem_3_1__T_44_data,dataMem_3_0__T_44_data,_T_41,_T_92}; // @[Cat.scala 29:58]
  wire [127:0] _T_95 = {dataMem_5_7__T_64_data,dataMem_5_6__T_64_data,dataMem_5_5__T_64_data,dataMem_5_4__T_64_data,dataMem_5_3__T_64_data,dataMem_5_2__T_64_data,dataMem_5_1__T_64_data,dataMem_5_0__T_64_data,_T_61}; // @[Cat.scala 29:58]
  wire [255:0] _T_97 = {dataMem_7_7__T_84_data,dataMem_7_6__T_84_data,dataMem_7_5__T_84_data,dataMem_7_4__T_84_data,dataMem_7_3__T_84_data,dataMem_7_2__T_84_data,dataMem_7_1__T_84_data,dataMem_7_0__T_84_data,_T_81,_T_95}; // @[Cat.scala 29:58]
  wire [511:0] dirty_cache_block = {_T_97,_T_94}; // @[Cat.scala 29:58]
  reg [49:0] block_rmeta_tag; // @[AXICache.scala 736:24]
  wire  is_write = state == 3'h2; // @[AXICache.scala 742:24]
  wire  _T_98 = state == 3'h6; // @[AXICache.scala 743:24]
  wire  is_alloc = _T_98 & read_wrap_out; // @[AXICache.scala 743:37]
  reg  is_alloc_reg; // @[AXICache.scala 744:29]
  wire  _T_213 = metaMem_tag_rmeta_data == 50'h0; // @[AXICache.scala 763:34]
  wire  hit = v[0] & _T_213; // @[AXICache.scala 763:21]
  wire  _T_99 = hit | is_alloc_reg; // @[AXICache.scala 747:30]
  wire  _T_100 = is_write & _T_99; // @[AXICache.scala 747:22]
  wire  wen = _T_100 | is_alloc; // @[AXICache.scala 747:64]
  reg [63:0] refill_buf_0; // @[AXICache.scala 760:23]
  reg [63:0] refill_buf_1; // @[AXICache.scala 760:23]
  reg [63:0] refill_buf_2; // @[AXICache.scala 760:23]
  reg [63:0] refill_buf_3; // @[AXICache.scala 760:23]
  reg [63:0] refill_buf_4; // @[AXICache.scala 760:23]
  reg [63:0] refill_buf_5; // @[AXICache.scala 760:23]
  reg [63:0] refill_buf_6; // @[AXICache.scala 760:23]
  reg [63:0] refill_buf_7; // @[AXICache.scala 760:23]
  wire [511:0] _T_209 = {refill_buf_7,refill_buf_6,refill_buf_5,refill_buf_4,refill_buf_3,refill_buf_2,refill_buf_1,refill_buf_0}; // @[AXICache.scala 761:43]
  wire [511:0] read = is_alloc_reg ? _T_209 : 512'h0; // @[AXICache.scala 761:17]
  wire  _T_234 = ~is_alloc; // @[AXICache.scala 788:19]
  wire [71:0] _T_237 = {1'b0,$signed(71'h0)}; // @[AXICache.scala 788:91]
  wire [71:0] wmask = _T_234 ? $signed(_T_237) : $signed(-72'sh1); // @[AXICache.scala 788:18]
  wire [511:0] _T_248 = {io_mem_rd_data_bits,refill_buf_6,refill_buf_5,refill_buf_4,refill_buf_3,refill_buf_2,refill_buf_1,refill_buf_0}; // @[Cat.scala 29:58]
  wire [511:0] wdata = _T_234 ? 512'h0 : _T_248; // @[AXICache.scala 789:18]
  wire [255:0] _T_250 = v | 256'h1; // @[AXICache.scala 793:18]
  wire [255:0] _T_257 = d | 256'h1; // @[AXICache.scala 794:18]
  wire [255:0] _T_258 = ~d; // @[AXICache.scala 794:18]
  wire [255:0] _T_259 = _T_258 | 256'h1; // @[AXICache.scala 794:18]
  wire [255:0] _T_260 = ~_T_259; // @[AXICache.scala 794:18]
  wire [255:0] _T_419 = v >> set_count; // @[AXICache.scala 823:25]
  wire [255:0] _T_421 = d >> set_count; // @[AXICache.scala 823:41]
  wire  is_block_dirty = _T_419[0] & _T_421[0]; // @[AXICache.scala 823:37]
  wire [57:0] _T_425 = {block_rmeta_tag,_T_13}; // @[Cat.scala 29:58]
  wire [63:0] _GEN_407 = {_T_425, 6'h0}; // @[AXICache.scala 824:58]
  wire [64:0] block_addr = {{1'd0}, _GEN_407}; // @[AXICache.scala 824:58]
  wire [57:0] _T_432 = {metaMem_tag_rmeta_data,8'h0}; // @[Cat.scala 29:58]
  wire [63:0] _GEN_408 = {_T_432, 6'h0}; // @[AXICache.scala 835:82]
  wire [64:0] _T_433 = {{1'd0}, _GEN_408}; // @[AXICache.scala 835:82]
  wire [64:0] _T_434 = flush_mode ? block_addr : _T_433; // @[AXICache.scala 835:33]
  wire [63:0] _GEN_324 = 3'h1 == write_count ? dirty_cache_block[127:64] : dirty_cache_block[63:0]; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_325 = 3'h2 == write_count ? dirty_cache_block[191:128] : _GEN_324; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_326 = 3'h3 == write_count ? dirty_cache_block[255:192] : _GEN_325; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_327 = 3'h4 == write_count ? dirty_cache_block[319:256] : _GEN_326; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_328 = 3'h5 == write_count ? dirty_cache_block[383:320] : _GEN_327; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_329 = 3'h6 == write_count ? dirty_cache_block[447:384] : _GEN_328; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_330 = 3'h7 == write_count ? dirty_cache_block[511:448] : _GEN_329; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_332 = 3'h1 == write_count ? read[127:64] : read[63:0]; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_333 = 3'h2 == write_count ? read[191:128] : _GEN_332; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_334 = 3'h3 == write_count ? read[255:192] : _GEN_333; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_335 = 3'h4 == write_count ? read[319:256] : _GEN_334; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_336 = 3'h5 == write_count ? read[383:320] : _GEN_335; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_337 = 3'h6 == write_count ? read[447:384] : _GEN_336; // @[AXICache.scala 840:29]
  wire [63:0] _GEN_338 = 3'h7 == write_count ? read[511:448] : _GEN_337; // @[AXICache.scala 840:29]
  wire  is_dirty = v[0] & d[0]; // @[AXICache.scala 853:29]
  wire  _T_458 = 3'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_461 = 3'h1 == state; // @[Conditional.scala 37:30]
  wire  _T_464 = ~is_dirty; // @[AXICache.scala 869:32]
  wire  _T_465 = io_mem_wr_cmd_ready & io_mem_wr_cmd_valid; // @[Decoupled.scala 40:37]
  wire  _T_466 = io_mem_rd_cmd_ready & io_mem_rd_cmd_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_344 = hit ? 1'h0 : is_dirty; // @[AXICache.scala 861:17]
  wire  _GEN_345 = hit ? 1'h0 : _T_464; // @[AXICache.scala 861:17]
  wire  _T_467 = 3'h2 == state; // @[Conditional.scala 37:30]
  wire  _GEN_349 = _T_99 ? 1'h0 : is_dirty; // @[AXICache.scala 878:49]
  wire  _GEN_350 = _T_99 ? 1'h0 : _T_464; // @[AXICache.scala 878:49]
  wire  _T_473 = 3'h3 == state; // @[Conditional.scala 37:30]
  wire  _T_474 = 3'h4 == state; // @[Conditional.scala 37:30]
  wire  _T_475 = 3'h5 == state; // @[Conditional.scala 37:30]
  wire  _T_477 = 3'h6 == state; // @[Conditional.scala 37:30]
  wire  _GEN_359 = _T_474 ? 1'h0 : _T_475; // @[Conditional.scala 39:67]
  wire  _GEN_362 = _T_473 ? 1'h0 : _GEN_359; // @[Conditional.scala 39:67]
  wire  _GEN_364 = _T_467 & _GEN_349; // @[Conditional.scala 39:67]
  wire  _GEN_365 = _T_467 ? _GEN_350 : _GEN_362; // @[Conditional.scala 39:67]
  wire  _GEN_366 = _T_467 ? 1'h0 : _T_473; // @[Conditional.scala 39:67]
  wire  _GEN_368 = _T_461 ? _GEN_344 : _GEN_364; // @[Conditional.scala 39:67]
  wire  _GEN_369 = _T_461 ? _GEN_345 : _GEN_365; // @[Conditional.scala 39:67]
  wire  _GEN_370 = _T_461 ? 1'h0 : _GEN_366; // @[Conditional.scala 39:67]
  wire  _GEN_372 = _T_458 ? 1'h0 : _GEN_368; // @[Conditional.scala 40:58]
  wire  _GEN_373 = _T_458 ? 1'h0 : _GEN_369; // @[Conditional.scala 40:58]
  wire  _GEN_374 = _T_458 ? 1'h0 : _GEN_370; // @[Conditional.scala 40:58]
  wire  _T_480 = 3'h0 == flush_state; // @[Conditional.scala 37:30]
  wire  _GEN_376 = io_cpu_flush | flush_mode; // @[AXICache.scala 917:26]
  wire  _T_481 = 3'h1 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_482 = 3'h2 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_483 = 3'h3 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_485 = 3'h4 == flush_state; // @[Conditional.scala 37:30]
  wire  _T_486 = 3'h5 == flush_state; // @[Conditional.scala 37:30]
  wire  _GEN_385 = _T_485 | _GEN_374; // @[Conditional.scala 39:67]
  wire  _GEN_387 = _T_483 | _GEN_372; // @[Conditional.scala 39:67]
  wire  _GEN_388 = _T_483 ? 1'h0 : _GEN_373; // @[Conditional.scala 39:67]
  wire  _GEN_390 = _T_483 ? _GEN_374 : _GEN_385; // @[Conditional.scala 39:67]
  wire  _GEN_392 = _T_482 ? _GEN_372 : _GEN_387; // @[Conditional.scala 39:67]
  wire  _GEN_393 = _T_482 ? _GEN_373 : _GEN_388; // @[Conditional.scala 39:67]
  wire  _GEN_394 = _T_482 ? _GEN_374 : _GEN_390; // @[Conditional.scala 39:67]
  wire  _GEN_395 = _T_481 & set_wrap; // @[Conditional.scala 39:67]
  wire  _GEN_398 = _T_481 ? _GEN_372 : _GEN_392; // @[Conditional.scala 39:67]
  wire  _GEN_399 = _T_481 ? _GEN_373 : _GEN_393; // @[Conditional.scala 39:67]
  wire  _GEN_400 = _T_481 ? _GEN_374 : _GEN_394; // @[Conditional.scala 39:67]
  assign metaMem_tag_rmeta_addr = 8'h0;
  assign metaMem_tag_rmeta_data = metaMem_tag[metaMem_tag_rmeta_addr]; // @[AXICache.scala 720:28]
  assign metaMem_tag__T_431_addr = metaMem_tag__T_431_addr_pipe_0;
  assign metaMem_tag__T_431_data = metaMem_tag[metaMem_tag__T_431_addr]; // @[AXICache.scala 720:28]
  assign metaMem_tag__T_262_data = 50'h0;
  assign metaMem_tag__T_262_addr = 8'h0;
  assign metaMem_tag__T_262_mask = 1'h1;
  assign metaMem_tag__T_262_en = wen & is_alloc;
  assign dataMem_0_0__T_14_addr = dataMem_0_0__T_14_addr_pipe_0;
  assign dataMem_0_0__T_14_data = dataMem_0_0[dataMem_0_0__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_0__T_112_addr = 8'h0;
  assign dataMem_0_0__T_112_data = dataMem_0_0[dataMem_0_0__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_0__T_281_data = wdata[7:0];
  assign dataMem_0_0__T_281_addr = 8'h0;
  assign dataMem_0_0__T_281_mask = wmask[0];
  assign dataMem_0_0__T_281_en = _T_100 | is_alloc;
  assign dataMem_0_1__T_14_addr = dataMem_0_1__T_14_addr_pipe_0;
  assign dataMem_0_1__T_14_data = dataMem_0_1[dataMem_0_1__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_1__T_112_addr = 8'h0;
  assign dataMem_0_1__T_112_data = dataMem_0_1[dataMem_0_1__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_1__T_281_data = wdata[15:8];
  assign dataMem_0_1__T_281_addr = 8'h0;
  assign dataMem_0_1__T_281_mask = wmask[1];
  assign dataMem_0_1__T_281_en = _T_100 | is_alloc;
  assign dataMem_0_2__T_14_addr = dataMem_0_2__T_14_addr_pipe_0;
  assign dataMem_0_2__T_14_data = dataMem_0_2[dataMem_0_2__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_2__T_112_addr = 8'h0;
  assign dataMem_0_2__T_112_data = dataMem_0_2[dataMem_0_2__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_2__T_281_data = wdata[23:16];
  assign dataMem_0_2__T_281_addr = 8'h0;
  assign dataMem_0_2__T_281_mask = wmask[2];
  assign dataMem_0_2__T_281_en = _T_100 | is_alloc;
  assign dataMem_0_3__T_14_addr = dataMem_0_3__T_14_addr_pipe_0;
  assign dataMem_0_3__T_14_data = dataMem_0_3[dataMem_0_3__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_3__T_112_addr = 8'h0;
  assign dataMem_0_3__T_112_data = dataMem_0_3[dataMem_0_3__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_3__T_281_data = wdata[31:24];
  assign dataMem_0_3__T_281_addr = 8'h0;
  assign dataMem_0_3__T_281_mask = wmask[3];
  assign dataMem_0_3__T_281_en = _T_100 | is_alloc;
  assign dataMem_0_4__T_14_addr = dataMem_0_4__T_14_addr_pipe_0;
  assign dataMem_0_4__T_14_data = dataMem_0_4[dataMem_0_4__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_4__T_112_addr = 8'h0;
  assign dataMem_0_4__T_112_data = dataMem_0_4[dataMem_0_4__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_4__T_281_data = wdata[39:32];
  assign dataMem_0_4__T_281_addr = 8'h0;
  assign dataMem_0_4__T_281_mask = wmask[4];
  assign dataMem_0_4__T_281_en = _T_100 | is_alloc;
  assign dataMem_0_5__T_14_addr = dataMem_0_5__T_14_addr_pipe_0;
  assign dataMem_0_5__T_14_data = dataMem_0_5[dataMem_0_5__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_5__T_112_addr = 8'h0;
  assign dataMem_0_5__T_112_data = dataMem_0_5[dataMem_0_5__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_5__T_281_data = wdata[47:40];
  assign dataMem_0_5__T_281_addr = 8'h0;
  assign dataMem_0_5__T_281_mask = wmask[5];
  assign dataMem_0_5__T_281_en = _T_100 | is_alloc;
  assign dataMem_0_6__T_14_addr = dataMem_0_6__T_14_addr_pipe_0;
  assign dataMem_0_6__T_14_data = dataMem_0_6[dataMem_0_6__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_6__T_112_addr = 8'h0;
  assign dataMem_0_6__T_112_data = dataMem_0_6[dataMem_0_6__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_6__T_281_data = wdata[55:48];
  assign dataMem_0_6__T_281_addr = 8'h0;
  assign dataMem_0_6__T_281_mask = wmask[6];
  assign dataMem_0_6__T_281_en = _T_100 | is_alloc;
  assign dataMem_0_7__T_14_addr = dataMem_0_7__T_14_addr_pipe_0;
  assign dataMem_0_7__T_14_data = dataMem_0_7[dataMem_0_7__T_14_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_7__T_112_addr = 8'h0;
  assign dataMem_0_7__T_112_data = dataMem_0_7[dataMem_0_7__T_112_addr]; // @[AXICache.scala 721:45]
  assign dataMem_0_7__T_281_data = wdata[63:56];
  assign dataMem_0_7__T_281_addr = 8'h0;
  assign dataMem_0_7__T_281_mask = wmask[7];
  assign dataMem_0_7__T_281_en = _T_100 | is_alloc;
  assign dataMem_1_0__T_24_addr = dataMem_1_0__T_24_addr_pipe_0;
  assign dataMem_1_0__T_24_data = dataMem_1_0[dataMem_1_0__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_0__T_123_addr = 8'h0;
  assign dataMem_1_0__T_123_data = dataMem_1_0[dataMem_1_0__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_0__T_300_data = wdata[71:64];
  assign dataMem_1_0__T_300_addr = 8'h0;
  assign dataMem_1_0__T_300_mask = wmask[8];
  assign dataMem_1_0__T_300_en = _T_100 | is_alloc;
  assign dataMem_1_1__T_24_addr = dataMem_1_1__T_24_addr_pipe_0;
  assign dataMem_1_1__T_24_data = dataMem_1_1[dataMem_1_1__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_1__T_123_addr = 8'h0;
  assign dataMem_1_1__T_123_data = dataMem_1_1[dataMem_1_1__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_1__T_300_data = wdata[79:72];
  assign dataMem_1_1__T_300_addr = 8'h0;
  assign dataMem_1_1__T_300_mask = wmask[9];
  assign dataMem_1_1__T_300_en = _T_100 | is_alloc;
  assign dataMem_1_2__T_24_addr = dataMem_1_2__T_24_addr_pipe_0;
  assign dataMem_1_2__T_24_data = dataMem_1_2[dataMem_1_2__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_2__T_123_addr = 8'h0;
  assign dataMem_1_2__T_123_data = dataMem_1_2[dataMem_1_2__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_2__T_300_data = wdata[87:80];
  assign dataMem_1_2__T_300_addr = 8'h0;
  assign dataMem_1_2__T_300_mask = wmask[10];
  assign dataMem_1_2__T_300_en = _T_100 | is_alloc;
  assign dataMem_1_3__T_24_addr = dataMem_1_3__T_24_addr_pipe_0;
  assign dataMem_1_3__T_24_data = dataMem_1_3[dataMem_1_3__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_3__T_123_addr = 8'h0;
  assign dataMem_1_3__T_123_data = dataMem_1_3[dataMem_1_3__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_3__T_300_data = wdata[95:88];
  assign dataMem_1_3__T_300_addr = 8'h0;
  assign dataMem_1_3__T_300_mask = wmask[11];
  assign dataMem_1_3__T_300_en = _T_100 | is_alloc;
  assign dataMem_1_4__T_24_addr = dataMem_1_4__T_24_addr_pipe_0;
  assign dataMem_1_4__T_24_data = dataMem_1_4[dataMem_1_4__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_4__T_123_addr = 8'h0;
  assign dataMem_1_4__T_123_data = dataMem_1_4[dataMem_1_4__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_4__T_300_data = wdata[103:96];
  assign dataMem_1_4__T_300_addr = 8'h0;
  assign dataMem_1_4__T_300_mask = wmask[12];
  assign dataMem_1_4__T_300_en = _T_100 | is_alloc;
  assign dataMem_1_5__T_24_addr = dataMem_1_5__T_24_addr_pipe_0;
  assign dataMem_1_5__T_24_data = dataMem_1_5[dataMem_1_5__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_5__T_123_addr = 8'h0;
  assign dataMem_1_5__T_123_data = dataMem_1_5[dataMem_1_5__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_5__T_300_data = wdata[111:104];
  assign dataMem_1_5__T_300_addr = 8'h0;
  assign dataMem_1_5__T_300_mask = wmask[13];
  assign dataMem_1_5__T_300_en = _T_100 | is_alloc;
  assign dataMem_1_6__T_24_addr = dataMem_1_6__T_24_addr_pipe_0;
  assign dataMem_1_6__T_24_data = dataMem_1_6[dataMem_1_6__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_6__T_123_addr = 8'h0;
  assign dataMem_1_6__T_123_data = dataMem_1_6[dataMem_1_6__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_6__T_300_data = wdata[119:112];
  assign dataMem_1_6__T_300_addr = 8'h0;
  assign dataMem_1_6__T_300_mask = wmask[14];
  assign dataMem_1_6__T_300_en = _T_100 | is_alloc;
  assign dataMem_1_7__T_24_addr = dataMem_1_7__T_24_addr_pipe_0;
  assign dataMem_1_7__T_24_data = dataMem_1_7[dataMem_1_7__T_24_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_7__T_123_addr = 8'h0;
  assign dataMem_1_7__T_123_data = dataMem_1_7[dataMem_1_7__T_123_addr]; // @[AXICache.scala 721:45]
  assign dataMem_1_7__T_300_data = wdata[127:120];
  assign dataMem_1_7__T_300_addr = 8'h0;
  assign dataMem_1_7__T_300_mask = wmask[15];
  assign dataMem_1_7__T_300_en = _T_100 | is_alloc;
  assign dataMem_2_0__T_34_addr = dataMem_2_0__T_34_addr_pipe_0;
  assign dataMem_2_0__T_34_data = dataMem_2_0[dataMem_2_0__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_0__T_134_addr = 8'h0;
  assign dataMem_2_0__T_134_data = dataMem_2_0[dataMem_2_0__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_0__T_319_data = wdata[135:128];
  assign dataMem_2_0__T_319_addr = 8'h0;
  assign dataMem_2_0__T_319_mask = wmask[16];
  assign dataMem_2_0__T_319_en = _T_100 | is_alloc;
  assign dataMem_2_1__T_34_addr = dataMem_2_1__T_34_addr_pipe_0;
  assign dataMem_2_1__T_34_data = dataMem_2_1[dataMem_2_1__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_1__T_134_addr = 8'h0;
  assign dataMem_2_1__T_134_data = dataMem_2_1[dataMem_2_1__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_1__T_319_data = wdata[143:136];
  assign dataMem_2_1__T_319_addr = 8'h0;
  assign dataMem_2_1__T_319_mask = wmask[17];
  assign dataMem_2_1__T_319_en = _T_100 | is_alloc;
  assign dataMem_2_2__T_34_addr = dataMem_2_2__T_34_addr_pipe_0;
  assign dataMem_2_2__T_34_data = dataMem_2_2[dataMem_2_2__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_2__T_134_addr = 8'h0;
  assign dataMem_2_2__T_134_data = dataMem_2_2[dataMem_2_2__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_2__T_319_data = wdata[151:144];
  assign dataMem_2_2__T_319_addr = 8'h0;
  assign dataMem_2_2__T_319_mask = wmask[18];
  assign dataMem_2_2__T_319_en = _T_100 | is_alloc;
  assign dataMem_2_3__T_34_addr = dataMem_2_3__T_34_addr_pipe_0;
  assign dataMem_2_3__T_34_data = dataMem_2_3[dataMem_2_3__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_3__T_134_addr = 8'h0;
  assign dataMem_2_3__T_134_data = dataMem_2_3[dataMem_2_3__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_3__T_319_data = wdata[159:152];
  assign dataMem_2_3__T_319_addr = 8'h0;
  assign dataMem_2_3__T_319_mask = wmask[19];
  assign dataMem_2_3__T_319_en = _T_100 | is_alloc;
  assign dataMem_2_4__T_34_addr = dataMem_2_4__T_34_addr_pipe_0;
  assign dataMem_2_4__T_34_data = dataMem_2_4[dataMem_2_4__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_4__T_134_addr = 8'h0;
  assign dataMem_2_4__T_134_data = dataMem_2_4[dataMem_2_4__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_4__T_319_data = wdata[167:160];
  assign dataMem_2_4__T_319_addr = 8'h0;
  assign dataMem_2_4__T_319_mask = wmask[20];
  assign dataMem_2_4__T_319_en = _T_100 | is_alloc;
  assign dataMem_2_5__T_34_addr = dataMem_2_5__T_34_addr_pipe_0;
  assign dataMem_2_5__T_34_data = dataMem_2_5[dataMem_2_5__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_5__T_134_addr = 8'h0;
  assign dataMem_2_5__T_134_data = dataMem_2_5[dataMem_2_5__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_5__T_319_data = wdata[175:168];
  assign dataMem_2_5__T_319_addr = 8'h0;
  assign dataMem_2_5__T_319_mask = wmask[21];
  assign dataMem_2_5__T_319_en = _T_100 | is_alloc;
  assign dataMem_2_6__T_34_addr = dataMem_2_6__T_34_addr_pipe_0;
  assign dataMem_2_6__T_34_data = dataMem_2_6[dataMem_2_6__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_6__T_134_addr = 8'h0;
  assign dataMem_2_6__T_134_data = dataMem_2_6[dataMem_2_6__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_6__T_319_data = wdata[183:176];
  assign dataMem_2_6__T_319_addr = 8'h0;
  assign dataMem_2_6__T_319_mask = wmask[22];
  assign dataMem_2_6__T_319_en = _T_100 | is_alloc;
  assign dataMem_2_7__T_34_addr = dataMem_2_7__T_34_addr_pipe_0;
  assign dataMem_2_7__T_34_data = dataMem_2_7[dataMem_2_7__T_34_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_7__T_134_addr = 8'h0;
  assign dataMem_2_7__T_134_data = dataMem_2_7[dataMem_2_7__T_134_addr]; // @[AXICache.scala 721:45]
  assign dataMem_2_7__T_319_data = wdata[191:184];
  assign dataMem_2_7__T_319_addr = 8'h0;
  assign dataMem_2_7__T_319_mask = wmask[23];
  assign dataMem_2_7__T_319_en = _T_100 | is_alloc;
  assign dataMem_3_0__T_44_addr = dataMem_3_0__T_44_addr_pipe_0;
  assign dataMem_3_0__T_44_data = dataMem_3_0[dataMem_3_0__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_0__T_145_addr = 8'h0;
  assign dataMem_3_0__T_145_data = dataMem_3_0[dataMem_3_0__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_0__T_338_data = wdata[199:192];
  assign dataMem_3_0__T_338_addr = 8'h0;
  assign dataMem_3_0__T_338_mask = wmask[24];
  assign dataMem_3_0__T_338_en = _T_100 | is_alloc;
  assign dataMem_3_1__T_44_addr = dataMem_3_1__T_44_addr_pipe_0;
  assign dataMem_3_1__T_44_data = dataMem_3_1[dataMem_3_1__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_1__T_145_addr = 8'h0;
  assign dataMem_3_1__T_145_data = dataMem_3_1[dataMem_3_1__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_1__T_338_data = wdata[207:200];
  assign dataMem_3_1__T_338_addr = 8'h0;
  assign dataMem_3_1__T_338_mask = wmask[25];
  assign dataMem_3_1__T_338_en = _T_100 | is_alloc;
  assign dataMem_3_2__T_44_addr = dataMem_3_2__T_44_addr_pipe_0;
  assign dataMem_3_2__T_44_data = dataMem_3_2[dataMem_3_2__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_2__T_145_addr = 8'h0;
  assign dataMem_3_2__T_145_data = dataMem_3_2[dataMem_3_2__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_2__T_338_data = wdata[215:208];
  assign dataMem_3_2__T_338_addr = 8'h0;
  assign dataMem_3_2__T_338_mask = wmask[26];
  assign dataMem_3_2__T_338_en = _T_100 | is_alloc;
  assign dataMem_3_3__T_44_addr = dataMem_3_3__T_44_addr_pipe_0;
  assign dataMem_3_3__T_44_data = dataMem_3_3[dataMem_3_3__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_3__T_145_addr = 8'h0;
  assign dataMem_3_3__T_145_data = dataMem_3_3[dataMem_3_3__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_3__T_338_data = wdata[223:216];
  assign dataMem_3_3__T_338_addr = 8'h0;
  assign dataMem_3_3__T_338_mask = wmask[27];
  assign dataMem_3_3__T_338_en = _T_100 | is_alloc;
  assign dataMem_3_4__T_44_addr = dataMem_3_4__T_44_addr_pipe_0;
  assign dataMem_3_4__T_44_data = dataMem_3_4[dataMem_3_4__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_4__T_145_addr = 8'h0;
  assign dataMem_3_4__T_145_data = dataMem_3_4[dataMem_3_4__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_4__T_338_data = wdata[231:224];
  assign dataMem_3_4__T_338_addr = 8'h0;
  assign dataMem_3_4__T_338_mask = wmask[28];
  assign dataMem_3_4__T_338_en = _T_100 | is_alloc;
  assign dataMem_3_5__T_44_addr = dataMem_3_5__T_44_addr_pipe_0;
  assign dataMem_3_5__T_44_data = dataMem_3_5[dataMem_3_5__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_5__T_145_addr = 8'h0;
  assign dataMem_3_5__T_145_data = dataMem_3_5[dataMem_3_5__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_5__T_338_data = wdata[239:232];
  assign dataMem_3_5__T_338_addr = 8'h0;
  assign dataMem_3_5__T_338_mask = wmask[29];
  assign dataMem_3_5__T_338_en = _T_100 | is_alloc;
  assign dataMem_3_6__T_44_addr = dataMem_3_6__T_44_addr_pipe_0;
  assign dataMem_3_6__T_44_data = dataMem_3_6[dataMem_3_6__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_6__T_145_addr = 8'h0;
  assign dataMem_3_6__T_145_data = dataMem_3_6[dataMem_3_6__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_6__T_338_data = wdata[247:240];
  assign dataMem_3_6__T_338_addr = 8'h0;
  assign dataMem_3_6__T_338_mask = wmask[30];
  assign dataMem_3_6__T_338_en = _T_100 | is_alloc;
  assign dataMem_3_7__T_44_addr = dataMem_3_7__T_44_addr_pipe_0;
  assign dataMem_3_7__T_44_data = dataMem_3_7[dataMem_3_7__T_44_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_7__T_145_addr = 8'h0;
  assign dataMem_3_7__T_145_data = dataMem_3_7[dataMem_3_7__T_145_addr]; // @[AXICache.scala 721:45]
  assign dataMem_3_7__T_338_data = wdata[255:248];
  assign dataMem_3_7__T_338_addr = 8'h0;
  assign dataMem_3_7__T_338_mask = wmask[31];
  assign dataMem_3_7__T_338_en = _T_100 | is_alloc;
  assign dataMem_4_0__T_54_addr = dataMem_4_0__T_54_addr_pipe_0;
  assign dataMem_4_0__T_54_data = dataMem_4_0[dataMem_4_0__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_0__T_156_addr = 8'h0;
  assign dataMem_4_0__T_156_data = dataMem_4_0[dataMem_4_0__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_0__T_357_data = wdata[263:256];
  assign dataMem_4_0__T_357_addr = 8'h0;
  assign dataMem_4_0__T_357_mask = wmask[32];
  assign dataMem_4_0__T_357_en = _T_100 | is_alloc;
  assign dataMem_4_1__T_54_addr = dataMem_4_1__T_54_addr_pipe_0;
  assign dataMem_4_1__T_54_data = dataMem_4_1[dataMem_4_1__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_1__T_156_addr = 8'h0;
  assign dataMem_4_1__T_156_data = dataMem_4_1[dataMem_4_1__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_1__T_357_data = wdata[271:264];
  assign dataMem_4_1__T_357_addr = 8'h0;
  assign dataMem_4_1__T_357_mask = wmask[33];
  assign dataMem_4_1__T_357_en = _T_100 | is_alloc;
  assign dataMem_4_2__T_54_addr = dataMem_4_2__T_54_addr_pipe_0;
  assign dataMem_4_2__T_54_data = dataMem_4_2[dataMem_4_2__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_2__T_156_addr = 8'h0;
  assign dataMem_4_2__T_156_data = dataMem_4_2[dataMem_4_2__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_2__T_357_data = wdata[279:272];
  assign dataMem_4_2__T_357_addr = 8'h0;
  assign dataMem_4_2__T_357_mask = wmask[34];
  assign dataMem_4_2__T_357_en = _T_100 | is_alloc;
  assign dataMem_4_3__T_54_addr = dataMem_4_3__T_54_addr_pipe_0;
  assign dataMem_4_3__T_54_data = dataMem_4_3[dataMem_4_3__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_3__T_156_addr = 8'h0;
  assign dataMem_4_3__T_156_data = dataMem_4_3[dataMem_4_3__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_3__T_357_data = wdata[287:280];
  assign dataMem_4_3__T_357_addr = 8'h0;
  assign dataMem_4_3__T_357_mask = wmask[35];
  assign dataMem_4_3__T_357_en = _T_100 | is_alloc;
  assign dataMem_4_4__T_54_addr = dataMem_4_4__T_54_addr_pipe_0;
  assign dataMem_4_4__T_54_data = dataMem_4_4[dataMem_4_4__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_4__T_156_addr = 8'h0;
  assign dataMem_4_4__T_156_data = dataMem_4_4[dataMem_4_4__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_4__T_357_data = wdata[295:288];
  assign dataMem_4_4__T_357_addr = 8'h0;
  assign dataMem_4_4__T_357_mask = wmask[36];
  assign dataMem_4_4__T_357_en = _T_100 | is_alloc;
  assign dataMem_4_5__T_54_addr = dataMem_4_5__T_54_addr_pipe_0;
  assign dataMem_4_5__T_54_data = dataMem_4_5[dataMem_4_5__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_5__T_156_addr = 8'h0;
  assign dataMem_4_5__T_156_data = dataMem_4_5[dataMem_4_5__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_5__T_357_data = wdata[303:296];
  assign dataMem_4_5__T_357_addr = 8'h0;
  assign dataMem_4_5__T_357_mask = wmask[37];
  assign dataMem_4_5__T_357_en = _T_100 | is_alloc;
  assign dataMem_4_6__T_54_addr = dataMem_4_6__T_54_addr_pipe_0;
  assign dataMem_4_6__T_54_data = dataMem_4_6[dataMem_4_6__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_6__T_156_addr = 8'h0;
  assign dataMem_4_6__T_156_data = dataMem_4_6[dataMem_4_6__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_6__T_357_data = wdata[311:304];
  assign dataMem_4_6__T_357_addr = 8'h0;
  assign dataMem_4_6__T_357_mask = wmask[38];
  assign dataMem_4_6__T_357_en = _T_100 | is_alloc;
  assign dataMem_4_7__T_54_addr = dataMem_4_7__T_54_addr_pipe_0;
  assign dataMem_4_7__T_54_data = dataMem_4_7[dataMem_4_7__T_54_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_7__T_156_addr = 8'h0;
  assign dataMem_4_7__T_156_data = dataMem_4_7[dataMem_4_7__T_156_addr]; // @[AXICache.scala 721:45]
  assign dataMem_4_7__T_357_data = wdata[319:312];
  assign dataMem_4_7__T_357_addr = 8'h0;
  assign dataMem_4_7__T_357_mask = wmask[39];
  assign dataMem_4_7__T_357_en = _T_100 | is_alloc;
  assign dataMem_5_0__T_64_addr = dataMem_5_0__T_64_addr_pipe_0;
  assign dataMem_5_0__T_64_data = dataMem_5_0[dataMem_5_0__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_0__T_167_addr = 8'h0;
  assign dataMem_5_0__T_167_data = dataMem_5_0[dataMem_5_0__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_0__T_376_data = wdata[327:320];
  assign dataMem_5_0__T_376_addr = 8'h0;
  assign dataMem_5_0__T_376_mask = wmask[40];
  assign dataMem_5_0__T_376_en = _T_100 | is_alloc;
  assign dataMem_5_1__T_64_addr = dataMem_5_1__T_64_addr_pipe_0;
  assign dataMem_5_1__T_64_data = dataMem_5_1[dataMem_5_1__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_1__T_167_addr = 8'h0;
  assign dataMem_5_1__T_167_data = dataMem_5_1[dataMem_5_1__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_1__T_376_data = wdata[335:328];
  assign dataMem_5_1__T_376_addr = 8'h0;
  assign dataMem_5_1__T_376_mask = wmask[41];
  assign dataMem_5_1__T_376_en = _T_100 | is_alloc;
  assign dataMem_5_2__T_64_addr = dataMem_5_2__T_64_addr_pipe_0;
  assign dataMem_5_2__T_64_data = dataMem_5_2[dataMem_5_2__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_2__T_167_addr = 8'h0;
  assign dataMem_5_2__T_167_data = dataMem_5_2[dataMem_5_2__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_2__T_376_data = wdata[343:336];
  assign dataMem_5_2__T_376_addr = 8'h0;
  assign dataMem_5_2__T_376_mask = wmask[42];
  assign dataMem_5_2__T_376_en = _T_100 | is_alloc;
  assign dataMem_5_3__T_64_addr = dataMem_5_3__T_64_addr_pipe_0;
  assign dataMem_5_3__T_64_data = dataMem_5_3[dataMem_5_3__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_3__T_167_addr = 8'h0;
  assign dataMem_5_3__T_167_data = dataMem_5_3[dataMem_5_3__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_3__T_376_data = wdata[351:344];
  assign dataMem_5_3__T_376_addr = 8'h0;
  assign dataMem_5_3__T_376_mask = wmask[43];
  assign dataMem_5_3__T_376_en = _T_100 | is_alloc;
  assign dataMem_5_4__T_64_addr = dataMem_5_4__T_64_addr_pipe_0;
  assign dataMem_5_4__T_64_data = dataMem_5_4[dataMem_5_4__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_4__T_167_addr = 8'h0;
  assign dataMem_5_4__T_167_data = dataMem_5_4[dataMem_5_4__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_4__T_376_data = wdata[359:352];
  assign dataMem_5_4__T_376_addr = 8'h0;
  assign dataMem_5_4__T_376_mask = wmask[44];
  assign dataMem_5_4__T_376_en = _T_100 | is_alloc;
  assign dataMem_5_5__T_64_addr = dataMem_5_5__T_64_addr_pipe_0;
  assign dataMem_5_5__T_64_data = dataMem_5_5[dataMem_5_5__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_5__T_167_addr = 8'h0;
  assign dataMem_5_5__T_167_data = dataMem_5_5[dataMem_5_5__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_5__T_376_data = wdata[367:360];
  assign dataMem_5_5__T_376_addr = 8'h0;
  assign dataMem_5_5__T_376_mask = wmask[45];
  assign dataMem_5_5__T_376_en = _T_100 | is_alloc;
  assign dataMem_5_6__T_64_addr = dataMem_5_6__T_64_addr_pipe_0;
  assign dataMem_5_6__T_64_data = dataMem_5_6[dataMem_5_6__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_6__T_167_addr = 8'h0;
  assign dataMem_5_6__T_167_data = dataMem_5_6[dataMem_5_6__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_6__T_376_data = wdata[375:368];
  assign dataMem_5_6__T_376_addr = 8'h0;
  assign dataMem_5_6__T_376_mask = wmask[46];
  assign dataMem_5_6__T_376_en = _T_100 | is_alloc;
  assign dataMem_5_7__T_64_addr = dataMem_5_7__T_64_addr_pipe_0;
  assign dataMem_5_7__T_64_data = dataMem_5_7[dataMem_5_7__T_64_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_7__T_167_addr = 8'h0;
  assign dataMem_5_7__T_167_data = dataMem_5_7[dataMem_5_7__T_167_addr]; // @[AXICache.scala 721:45]
  assign dataMem_5_7__T_376_data = wdata[383:376];
  assign dataMem_5_7__T_376_addr = 8'h0;
  assign dataMem_5_7__T_376_mask = wmask[47];
  assign dataMem_5_7__T_376_en = _T_100 | is_alloc;
  assign dataMem_6_0__T_74_addr = dataMem_6_0__T_74_addr_pipe_0;
  assign dataMem_6_0__T_74_data = dataMem_6_0[dataMem_6_0__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_0__T_178_addr = 8'h0;
  assign dataMem_6_0__T_178_data = dataMem_6_0[dataMem_6_0__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_0__T_395_data = wdata[391:384];
  assign dataMem_6_0__T_395_addr = 8'h0;
  assign dataMem_6_0__T_395_mask = wmask[48];
  assign dataMem_6_0__T_395_en = _T_100 | is_alloc;
  assign dataMem_6_1__T_74_addr = dataMem_6_1__T_74_addr_pipe_0;
  assign dataMem_6_1__T_74_data = dataMem_6_1[dataMem_6_1__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_1__T_178_addr = 8'h0;
  assign dataMem_6_1__T_178_data = dataMem_6_1[dataMem_6_1__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_1__T_395_data = wdata[399:392];
  assign dataMem_6_1__T_395_addr = 8'h0;
  assign dataMem_6_1__T_395_mask = wmask[49];
  assign dataMem_6_1__T_395_en = _T_100 | is_alloc;
  assign dataMem_6_2__T_74_addr = dataMem_6_2__T_74_addr_pipe_0;
  assign dataMem_6_2__T_74_data = dataMem_6_2[dataMem_6_2__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_2__T_178_addr = 8'h0;
  assign dataMem_6_2__T_178_data = dataMem_6_2[dataMem_6_2__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_2__T_395_data = wdata[407:400];
  assign dataMem_6_2__T_395_addr = 8'h0;
  assign dataMem_6_2__T_395_mask = wmask[50];
  assign dataMem_6_2__T_395_en = _T_100 | is_alloc;
  assign dataMem_6_3__T_74_addr = dataMem_6_3__T_74_addr_pipe_0;
  assign dataMem_6_3__T_74_data = dataMem_6_3[dataMem_6_3__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_3__T_178_addr = 8'h0;
  assign dataMem_6_3__T_178_data = dataMem_6_3[dataMem_6_3__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_3__T_395_data = wdata[415:408];
  assign dataMem_6_3__T_395_addr = 8'h0;
  assign dataMem_6_3__T_395_mask = wmask[51];
  assign dataMem_6_3__T_395_en = _T_100 | is_alloc;
  assign dataMem_6_4__T_74_addr = dataMem_6_4__T_74_addr_pipe_0;
  assign dataMem_6_4__T_74_data = dataMem_6_4[dataMem_6_4__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_4__T_178_addr = 8'h0;
  assign dataMem_6_4__T_178_data = dataMem_6_4[dataMem_6_4__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_4__T_395_data = wdata[423:416];
  assign dataMem_6_4__T_395_addr = 8'h0;
  assign dataMem_6_4__T_395_mask = wmask[52];
  assign dataMem_6_4__T_395_en = _T_100 | is_alloc;
  assign dataMem_6_5__T_74_addr = dataMem_6_5__T_74_addr_pipe_0;
  assign dataMem_6_5__T_74_data = dataMem_6_5[dataMem_6_5__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_5__T_178_addr = 8'h0;
  assign dataMem_6_5__T_178_data = dataMem_6_5[dataMem_6_5__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_5__T_395_data = wdata[431:424];
  assign dataMem_6_5__T_395_addr = 8'h0;
  assign dataMem_6_5__T_395_mask = wmask[53];
  assign dataMem_6_5__T_395_en = _T_100 | is_alloc;
  assign dataMem_6_6__T_74_addr = dataMem_6_6__T_74_addr_pipe_0;
  assign dataMem_6_6__T_74_data = dataMem_6_6[dataMem_6_6__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_6__T_178_addr = 8'h0;
  assign dataMem_6_6__T_178_data = dataMem_6_6[dataMem_6_6__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_6__T_395_data = wdata[439:432];
  assign dataMem_6_6__T_395_addr = 8'h0;
  assign dataMem_6_6__T_395_mask = wmask[54];
  assign dataMem_6_6__T_395_en = _T_100 | is_alloc;
  assign dataMem_6_7__T_74_addr = dataMem_6_7__T_74_addr_pipe_0;
  assign dataMem_6_7__T_74_data = dataMem_6_7[dataMem_6_7__T_74_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_7__T_178_addr = 8'h0;
  assign dataMem_6_7__T_178_data = dataMem_6_7[dataMem_6_7__T_178_addr]; // @[AXICache.scala 721:45]
  assign dataMem_6_7__T_395_data = wdata[447:440];
  assign dataMem_6_7__T_395_addr = 8'h0;
  assign dataMem_6_7__T_395_mask = wmask[55];
  assign dataMem_6_7__T_395_en = _T_100 | is_alloc;
  assign dataMem_7_0__T_84_addr = dataMem_7_0__T_84_addr_pipe_0;
  assign dataMem_7_0__T_84_data = dataMem_7_0[dataMem_7_0__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_0__T_189_addr = 8'h0;
  assign dataMem_7_0__T_189_data = dataMem_7_0[dataMem_7_0__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_0__T_414_data = wdata[455:448];
  assign dataMem_7_0__T_414_addr = 8'h0;
  assign dataMem_7_0__T_414_mask = wmask[56];
  assign dataMem_7_0__T_414_en = _T_100 | is_alloc;
  assign dataMem_7_1__T_84_addr = dataMem_7_1__T_84_addr_pipe_0;
  assign dataMem_7_1__T_84_data = dataMem_7_1[dataMem_7_1__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_1__T_189_addr = 8'h0;
  assign dataMem_7_1__T_189_data = dataMem_7_1[dataMem_7_1__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_1__T_414_data = wdata[463:456];
  assign dataMem_7_1__T_414_addr = 8'h0;
  assign dataMem_7_1__T_414_mask = wmask[57];
  assign dataMem_7_1__T_414_en = _T_100 | is_alloc;
  assign dataMem_7_2__T_84_addr = dataMem_7_2__T_84_addr_pipe_0;
  assign dataMem_7_2__T_84_data = dataMem_7_2[dataMem_7_2__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_2__T_189_addr = 8'h0;
  assign dataMem_7_2__T_189_data = dataMem_7_2[dataMem_7_2__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_2__T_414_data = wdata[471:464];
  assign dataMem_7_2__T_414_addr = 8'h0;
  assign dataMem_7_2__T_414_mask = wmask[58];
  assign dataMem_7_2__T_414_en = _T_100 | is_alloc;
  assign dataMem_7_3__T_84_addr = dataMem_7_3__T_84_addr_pipe_0;
  assign dataMem_7_3__T_84_data = dataMem_7_3[dataMem_7_3__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_3__T_189_addr = 8'h0;
  assign dataMem_7_3__T_189_data = dataMem_7_3[dataMem_7_3__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_3__T_414_data = wdata[479:472];
  assign dataMem_7_3__T_414_addr = 8'h0;
  assign dataMem_7_3__T_414_mask = wmask[59];
  assign dataMem_7_3__T_414_en = _T_100 | is_alloc;
  assign dataMem_7_4__T_84_addr = dataMem_7_4__T_84_addr_pipe_0;
  assign dataMem_7_4__T_84_data = dataMem_7_4[dataMem_7_4__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_4__T_189_addr = 8'h0;
  assign dataMem_7_4__T_189_data = dataMem_7_4[dataMem_7_4__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_4__T_414_data = wdata[487:480];
  assign dataMem_7_4__T_414_addr = 8'h0;
  assign dataMem_7_4__T_414_mask = wmask[60];
  assign dataMem_7_4__T_414_en = _T_100 | is_alloc;
  assign dataMem_7_5__T_84_addr = dataMem_7_5__T_84_addr_pipe_0;
  assign dataMem_7_5__T_84_data = dataMem_7_5[dataMem_7_5__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_5__T_189_addr = 8'h0;
  assign dataMem_7_5__T_189_data = dataMem_7_5[dataMem_7_5__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_5__T_414_data = wdata[495:488];
  assign dataMem_7_5__T_414_addr = 8'h0;
  assign dataMem_7_5__T_414_mask = wmask[61];
  assign dataMem_7_5__T_414_en = _T_100 | is_alloc;
  assign dataMem_7_6__T_84_addr = dataMem_7_6__T_84_addr_pipe_0;
  assign dataMem_7_6__T_84_data = dataMem_7_6[dataMem_7_6__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_6__T_189_addr = 8'h0;
  assign dataMem_7_6__T_189_data = dataMem_7_6[dataMem_7_6__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_6__T_414_data = wdata[503:496];
  assign dataMem_7_6__T_414_addr = 8'h0;
  assign dataMem_7_6__T_414_mask = wmask[62];
  assign dataMem_7_6__T_414_en = _T_100 | is_alloc;
  assign dataMem_7_7__T_84_addr = dataMem_7_7__T_84_addr_pipe_0;
  assign dataMem_7_7__T_84_data = dataMem_7_7[dataMem_7_7__T_84_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_7__T_189_addr = 8'h0;
  assign dataMem_7_7__T_189_data = dataMem_7_7[dataMem_7_7__T_189_addr]; // @[AXICache.scala 721:45]
  assign dataMem_7_7__T_414_data = wdata[511:504];
  assign dataMem_7_7__T_414_addr = 8'h0;
  assign dataMem_7_7__T_414_mask = wmask[63];
  assign dataMem_7_7__T_414_en = _T_100 | is_alloc;
  assign io_cpu_flush_done = _T_480 ? 1'h0 : _GEN_395; // @[AXICache.scala 850:21 AXICache.scala 924:27]
  assign io_mem_rd_cmd_valid = _T_480 ? _GEN_373 : _GEN_399; // @[AXICache.scala 814:23 AXICache.scala 869:29 AXICache.scala 882:29 AXICache.scala 902:27 AXICache.scala 956:27]
  assign io_mem_rd_data_ready = state == 3'h6; // @[AXICache.scala 817:24]
  assign io_mem_wr_cmd_valid = _T_480 ? _GEN_372 : _GEN_398; // @[AXICache.scala 837:23 AXICache.scala 868:29 AXICache.scala 881:29 AXICache.scala 955:27]
  assign io_mem_wr_cmd_bits_addr = _T_434[31:0]; // @[AXICache.scala 835:27]
  assign io_mem_wr_data_valid = _T_480 ? _GEN_374 : _GEN_400; // @[AXICache.scala 847:24 AXICache.scala 891:28 AXICache.scala 966:28]
  assign io_mem_wr_data_bits = flush_mode ? _GEN_330 : _GEN_338; // @[AXICache.scala 840:23]
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
  _RAND_3 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_0[initvar] = _RAND_3[7:0];
  _RAND_5 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_1[initvar] = _RAND_5[7:0];
  _RAND_7 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_2[initvar] = _RAND_7[7:0];
  _RAND_9 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_3[initvar] = _RAND_9[7:0];
  _RAND_11 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_4[initvar] = _RAND_11[7:0];
  _RAND_13 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_5[initvar] = _RAND_13[7:0];
  _RAND_15 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_6[initvar] = _RAND_15[7:0];
  _RAND_17 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_0_7[initvar] = _RAND_17[7:0];
  _RAND_19 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_0[initvar] = _RAND_19[7:0];
  _RAND_21 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_1[initvar] = _RAND_21[7:0];
  _RAND_23 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_2[initvar] = _RAND_23[7:0];
  _RAND_25 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_3[initvar] = _RAND_25[7:0];
  _RAND_27 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_4[initvar] = _RAND_27[7:0];
  _RAND_29 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_5[initvar] = _RAND_29[7:0];
  _RAND_31 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_6[initvar] = _RAND_31[7:0];
  _RAND_33 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_1_7[initvar] = _RAND_33[7:0];
  _RAND_35 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_0[initvar] = _RAND_35[7:0];
  _RAND_37 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_1[initvar] = _RAND_37[7:0];
  _RAND_39 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_2[initvar] = _RAND_39[7:0];
  _RAND_41 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_3[initvar] = _RAND_41[7:0];
  _RAND_43 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_4[initvar] = _RAND_43[7:0];
  _RAND_45 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_5[initvar] = _RAND_45[7:0];
  _RAND_47 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_6[initvar] = _RAND_47[7:0];
  _RAND_49 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_2_7[initvar] = _RAND_49[7:0];
  _RAND_51 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_0[initvar] = _RAND_51[7:0];
  _RAND_53 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_1[initvar] = _RAND_53[7:0];
  _RAND_55 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_2[initvar] = _RAND_55[7:0];
  _RAND_57 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_3[initvar] = _RAND_57[7:0];
  _RAND_59 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_4[initvar] = _RAND_59[7:0];
  _RAND_61 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_5[initvar] = _RAND_61[7:0];
  _RAND_63 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_6[initvar] = _RAND_63[7:0];
  _RAND_65 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_3_7[initvar] = _RAND_65[7:0];
  _RAND_67 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_0[initvar] = _RAND_67[7:0];
  _RAND_69 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_1[initvar] = _RAND_69[7:0];
  _RAND_71 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_2[initvar] = _RAND_71[7:0];
  _RAND_73 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_3[initvar] = _RAND_73[7:0];
  _RAND_75 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_4[initvar] = _RAND_75[7:0];
  _RAND_77 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_5[initvar] = _RAND_77[7:0];
  _RAND_79 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_6[initvar] = _RAND_79[7:0];
  _RAND_81 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_4_7[initvar] = _RAND_81[7:0];
  _RAND_83 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_0[initvar] = _RAND_83[7:0];
  _RAND_85 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_1[initvar] = _RAND_85[7:0];
  _RAND_87 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_2[initvar] = _RAND_87[7:0];
  _RAND_89 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_3[initvar] = _RAND_89[7:0];
  _RAND_91 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_4[initvar] = _RAND_91[7:0];
  _RAND_93 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_5[initvar] = _RAND_93[7:0];
  _RAND_95 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_6[initvar] = _RAND_95[7:0];
  _RAND_97 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_5_7[initvar] = _RAND_97[7:0];
  _RAND_99 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_0[initvar] = _RAND_99[7:0];
  _RAND_101 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_1[initvar] = _RAND_101[7:0];
  _RAND_103 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_2[initvar] = _RAND_103[7:0];
  _RAND_105 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_3[initvar] = _RAND_105[7:0];
  _RAND_107 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_4[initvar] = _RAND_107[7:0];
  _RAND_109 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_5[initvar] = _RAND_109[7:0];
  _RAND_111 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_6[initvar] = _RAND_111[7:0];
  _RAND_113 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_6_7[initvar] = _RAND_113[7:0];
  _RAND_115 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_0[initvar] = _RAND_115[7:0];
  _RAND_117 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_1[initvar] = _RAND_117[7:0];
  _RAND_119 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_2[initvar] = _RAND_119[7:0];
  _RAND_121 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_3[initvar] = _RAND_121[7:0];
  _RAND_123 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_4[initvar] = _RAND_123[7:0];
  _RAND_125 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_5[initvar] = _RAND_125[7:0];
  _RAND_127 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_6[initvar] = _RAND_127[7:0];
  _RAND_129 = {1{`RANDOM}};
  for (initvar = 0; initvar < 256; initvar = initvar+1)
    dataMem_7_7[initvar] = _RAND_129[7:0];
`endif // RANDOMIZE_MEM_INIT
`ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  metaMem_tag__T_431_en_pipe_0 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  metaMem_tag__T_431_addr_pipe_0 = _RAND_2[7:0];
  _RAND_4 = {1{`RANDOM}};
  dataMem_0_0__T_14_addr_pipe_0 = _RAND_4[7:0];
  _RAND_6 = {1{`RANDOM}};
  dataMem_0_1__T_14_addr_pipe_0 = _RAND_6[7:0];
  _RAND_8 = {1{`RANDOM}};
  dataMem_0_2__T_14_addr_pipe_0 = _RAND_8[7:0];
  _RAND_10 = {1{`RANDOM}};
  dataMem_0_3__T_14_addr_pipe_0 = _RAND_10[7:0];
  _RAND_12 = {1{`RANDOM}};
  dataMem_0_4__T_14_addr_pipe_0 = _RAND_12[7:0];
  _RAND_14 = {1{`RANDOM}};
  dataMem_0_5__T_14_addr_pipe_0 = _RAND_14[7:0];
  _RAND_16 = {1{`RANDOM}};
  dataMem_0_6__T_14_addr_pipe_0 = _RAND_16[7:0];
  _RAND_18 = {1{`RANDOM}};
  dataMem_0_7__T_14_addr_pipe_0 = _RAND_18[7:0];
  _RAND_20 = {1{`RANDOM}};
  dataMem_1_0__T_24_addr_pipe_0 = _RAND_20[7:0];
  _RAND_22 = {1{`RANDOM}};
  dataMem_1_1__T_24_addr_pipe_0 = _RAND_22[7:0];
  _RAND_24 = {1{`RANDOM}};
  dataMem_1_2__T_24_addr_pipe_0 = _RAND_24[7:0];
  _RAND_26 = {1{`RANDOM}};
  dataMem_1_3__T_24_addr_pipe_0 = _RAND_26[7:0];
  _RAND_28 = {1{`RANDOM}};
  dataMem_1_4__T_24_addr_pipe_0 = _RAND_28[7:0];
  _RAND_30 = {1{`RANDOM}};
  dataMem_1_5__T_24_addr_pipe_0 = _RAND_30[7:0];
  _RAND_32 = {1{`RANDOM}};
  dataMem_1_6__T_24_addr_pipe_0 = _RAND_32[7:0];
  _RAND_34 = {1{`RANDOM}};
  dataMem_1_7__T_24_addr_pipe_0 = _RAND_34[7:0];
  _RAND_36 = {1{`RANDOM}};
  dataMem_2_0__T_34_addr_pipe_0 = _RAND_36[7:0];
  _RAND_38 = {1{`RANDOM}};
  dataMem_2_1__T_34_addr_pipe_0 = _RAND_38[7:0];
  _RAND_40 = {1{`RANDOM}};
  dataMem_2_2__T_34_addr_pipe_0 = _RAND_40[7:0];
  _RAND_42 = {1{`RANDOM}};
  dataMem_2_3__T_34_addr_pipe_0 = _RAND_42[7:0];
  _RAND_44 = {1{`RANDOM}};
  dataMem_2_4__T_34_addr_pipe_0 = _RAND_44[7:0];
  _RAND_46 = {1{`RANDOM}};
  dataMem_2_5__T_34_addr_pipe_0 = _RAND_46[7:0];
  _RAND_48 = {1{`RANDOM}};
  dataMem_2_6__T_34_addr_pipe_0 = _RAND_48[7:0];
  _RAND_50 = {1{`RANDOM}};
  dataMem_2_7__T_34_addr_pipe_0 = _RAND_50[7:0];
  _RAND_52 = {1{`RANDOM}};
  dataMem_3_0__T_44_addr_pipe_0 = _RAND_52[7:0];
  _RAND_54 = {1{`RANDOM}};
  dataMem_3_1__T_44_addr_pipe_0 = _RAND_54[7:0];
  _RAND_56 = {1{`RANDOM}};
  dataMem_3_2__T_44_addr_pipe_0 = _RAND_56[7:0];
  _RAND_58 = {1{`RANDOM}};
  dataMem_3_3__T_44_addr_pipe_0 = _RAND_58[7:0];
  _RAND_60 = {1{`RANDOM}};
  dataMem_3_4__T_44_addr_pipe_0 = _RAND_60[7:0];
  _RAND_62 = {1{`RANDOM}};
  dataMem_3_5__T_44_addr_pipe_0 = _RAND_62[7:0];
  _RAND_64 = {1{`RANDOM}};
  dataMem_3_6__T_44_addr_pipe_0 = _RAND_64[7:0];
  _RAND_66 = {1{`RANDOM}};
  dataMem_3_7__T_44_addr_pipe_0 = _RAND_66[7:0];
  _RAND_68 = {1{`RANDOM}};
  dataMem_4_0__T_54_addr_pipe_0 = _RAND_68[7:0];
  _RAND_70 = {1{`RANDOM}};
  dataMem_4_1__T_54_addr_pipe_0 = _RAND_70[7:0];
  _RAND_72 = {1{`RANDOM}};
  dataMem_4_2__T_54_addr_pipe_0 = _RAND_72[7:0];
  _RAND_74 = {1{`RANDOM}};
  dataMem_4_3__T_54_addr_pipe_0 = _RAND_74[7:0];
  _RAND_76 = {1{`RANDOM}};
  dataMem_4_4__T_54_addr_pipe_0 = _RAND_76[7:0];
  _RAND_78 = {1{`RANDOM}};
  dataMem_4_5__T_54_addr_pipe_0 = _RAND_78[7:0];
  _RAND_80 = {1{`RANDOM}};
  dataMem_4_6__T_54_addr_pipe_0 = _RAND_80[7:0];
  _RAND_82 = {1{`RANDOM}};
  dataMem_4_7__T_54_addr_pipe_0 = _RAND_82[7:0];
  _RAND_84 = {1{`RANDOM}};
  dataMem_5_0__T_64_addr_pipe_0 = _RAND_84[7:0];
  _RAND_86 = {1{`RANDOM}};
  dataMem_5_1__T_64_addr_pipe_0 = _RAND_86[7:0];
  _RAND_88 = {1{`RANDOM}};
  dataMem_5_2__T_64_addr_pipe_0 = _RAND_88[7:0];
  _RAND_90 = {1{`RANDOM}};
  dataMem_5_3__T_64_addr_pipe_0 = _RAND_90[7:0];
  _RAND_92 = {1{`RANDOM}};
  dataMem_5_4__T_64_addr_pipe_0 = _RAND_92[7:0];
  _RAND_94 = {1{`RANDOM}};
  dataMem_5_5__T_64_addr_pipe_0 = _RAND_94[7:0];
  _RAND_96 = {1{`RANDOM}};
  dataMem_5_6__T_64_addr_pipe_0 = _RAND_96[7:0];
  _RAND_98 = {1{`RANDOM}};
  dataMem_5_7__T_64_addr_pipe_0 = _RAND_98[7:0];
  _RAND_100 = {1{`RANDOM}};
  dataMem_6_0__T_74_addr_pipe_0 = _RAND_100[7:0];
  _RAND_102 = {1{`RANDOM}};
  dataMem_6_1__T_74_addr_pipe_0 = _RAND_102[7:0];
  _RAND_104 = {1{`RANDOM}};
  dataMem_6_2__T_74_addr_pipe_0 = _RAND_104[7:0];
  _RAND_106 = {1{`RANDOM}};
  dataMem_6_3__T_74_addr_pipe_0 = _RAND_106[7:0];
  _RAND_108 = {1{`RANDOM}};
  dataMem_6_4__T_74_addr_pipe_0 = _RAND_108[7:0];
  _RAND_110 = {1{`RANDOM}};
  dataMem_6_5__T_74_addr_pipe_0 = _RAND_110[7:0];
  _RAND_112 = {1{`RANDOM}};
  dataMem_6_6__T_74_addr_pipe_0 = _RAND_112[7:0];
  _RAND_114 = {1{`RANDOM}};
  dataMem_6_7__T_74_addr_pipe_0 = _RAND_114[7:0];
  _RAND_116 = {1{`RANDOM}};
  dataMem_7_0__T_84_addr_pipe_0 = _RAND_116[7:0];
  _RAND_118 = {1{`RANDOM}};
  dataMem_7_1__T_84_addr_pipe_0 = _RAND_118[7:0];
  _RAND_120 = {1{`RANDOM}};
  dataMem_7_2__T_84_addr_pipe_0 = _RAND_120[7:0];
  _RAND_122 = {1{`RANDOM}};
  dataMem_7_3__T_84_addr_pipe_0 = _RAND_122[7:0];
  _RAND_124 = {1{`RANDOM}};
  dataMem_7_4__T_84_addr_pipe_0 = _RAND_124[7:0];
  _RAND_126 = {1{`RANDOM}};
  dataMem_7_5__T_84_addr_pipe_0 = _RAND_126[7:0];
  _RAND_128 = {1{`RANDOM}};
  dataMem_7_6__T_84_addr_pipe_0 = _RAND_128[7:0];
  _RAND_130 = {1{`RANDOM}};
  dataMem_7_7__T_84_addr_pipe_0 = _RAND_130[7:0];
  _RAND_131 = {1{`RANDOM}};
  state = _RAND_131[2:0];
  _RAND_132 = {1{`RANDOM}};
  flush_state = _RAND_132[2:0];
  _RAND_133 = {1{`RANDOM}};
  flush_mode = _RAND_133[0:0];
  _RAND_134 = {8{`RANDOM}};
  v = _RAND_134[255:0];
  _RAND_135 = {8{`RANDOM}};
  d = _RAND_135[255:0];
  _RAND_136 = {1{`RANDOM}};
  read_count = _RAND_136[2:0];
  _RAND_137 = {1{`RANDOM}};
  write_count = _RAND_137[2:0];
  _RAND_138 = {1{`RANDOM}};
  set_count = _RAND_138[7:0];
  _RAND_139 = {2{`RANDOM}};
  block_rmeta_tag = _RAND_139[49:0];
  _RAND_140 = {1{`RANDOM}};
  is_alloc_reg = _RAND_140[0:0];
  _RAND_141 = {2{`RANDOM}};
  refill_buf_0 = _RAND_141[63:0];
  _RAND_142 = {2{`RANDOM}};
  refill_buf_1 = _RAND_142[63:0];
  _RAND_143 = {2{`RANDOM}};
  refill_buf_2 = _RAND_143[63:0];
  _RAND_144 = {2{`RANDOM}};
  refill_buf_3 = _RAND_144[63:0];
  _RAND_145 = {2{`RANDOM}};
  refill_buf_4 = _RAND_145[63:0];
  _RAND_146 = {2{`RANDOM}};
  refill_buf_5 = _RAND_146[63:0];
  _RAND_147 = {2{`RANDOM}};
  refill_buf_6 = _RAND_147[63:0];
  _RAND_148 = {2{`RANDOM}};
  refill_buf_7 = _RAND_148[63:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if(metaMem_tag__T_262_en & metaMem_tag__T_262_mask) begin
      metaMem_tag[metaMem_tag__T_262_addr] <= metaMem_tag__T_262_data; // @[AXICache.scala 720:28]
    end
    metaMem_tag__T_431_en_pipe_0 <= is_block_dirty & _T_8;
    if (is_block_dirty & _T_8) begin
      metaMem_tag__T_431_addr_pipe_0 <= set_count;
    end
    if(dataMem_0_0__T_281_en & dataMem_0_0__T_281_mask) begin
      dataMem_0_0[dataMem_0_0__T_281_addr] <= dataMem_0_0__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_0__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_0_1__T_281_en & dataMem_0_1__T_281_mask) begin
      dataMem_0_1[dataMem_0_1__T_281_addr] <= dataMem_0_1__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_1__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_0_2__T_281_en & dataMem_0_2__T_281_mask) begin
      dataMem_0_2[dataMem_0_2__T_281_addr] <= dataMem_0_2__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_2__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_0_3__T_281_en & dataMem_0_3__T_281_mask) begin
      dataMem_0_3[dataMem_0_3__T_281_addr] <= dataMem_0_3__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_3__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_0_4__T_281_en & dataMem_0_4__T_281_mask) begin
      dataMem_0_4[dataMem_0_4__T_281_addr] <= dataMem_0_4__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_4__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_0_5__T_281_en & dataMem_0_5__T_281_mask) begin
      dataMem_0_5[dataMem_0_5__T_281_addr] <= dataMem_0_5__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_5__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_0_6__T_281_en & dataMem_0_6__T_281_mask) begin
      dataMem_0_6[dataMem_0_6__T_281_addr] <= dataMem_0_6__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_6__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_0_7__T_281_en & dataMem_0_7__T_281_mask) begin
      dataMem_0_7[dataMem_0_7__T_281_addr] <= dataMem_0_7__T_281_data; // @[AXICache.scala 721:45]
    end
    dataMem_0_7__T_14_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_0__T_300_en & dataMem_1_0__T_300_mask) begin
      dataMem_1_0[dataMem_1_0__T_300_addr] <= dataMem_1_0__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_0__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_1__T_300_en & dataMem_1_1__T_300_mask) begin
      dataMem_1_1[dataMem_1_1__T_300_addr] <= dataMem_1_1__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_1__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_2__T_300_en & dataMem_1_2__T_300_mask) begin
      dataMem_1_2[dataMem_1_2__T_300_addr] <= dataMem_1_2__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_2__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_3__T_300_en & dataMem_1_3__T_300_mask) begin
      dataMem_1_3[dataMem_1_3__T_300_addr] <= dataMem_1_3__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_3__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_4__T_300_en & dataMem_1_4__T_300_mask) begin
      dataMem_1_4[dataMem_1_4__T_300_addr] <= dataMem_1_4__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_4__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_5__T_300_en & dataMem_1_5__T_300_mask) begin
      dataMem_1_5[dataMem_1_5__T_300_addr] <= dataMem_1_5__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_5__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_6__T_300_en & dataMem_1_6__T_300_mask) begin
      dataMem_1_6[dataMem_1_6__T_300_addr] <= dataMem_1_6__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_6__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_1_7__T_300_en & dataMem_1_7__T_300_mask) begin
      dataMem_1_7[dataMem_1_7__T_300_addr] <= dataMem_1_7__T_300_data; // @[AXICache.scala 721:45]
    end
    dataMem_1_7__T_24_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_0__T_319_en & dataMem_2_0__T_319_mask) begin
      dataMem_2_0[dataMem_2_0__T_319_addr] <= dataMem_2_0__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_0__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_1__T_319_en & dataMem_2_1__T_319_mask) begin
      dataMem_2_1[dataMem_2_1__T_319_addr] <= dataMem_2_1__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_1__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_2__T_319_en & dataMem_2_2__T_319_mask) begin
      dataMem_2_2[dataMem_2_2__T_319_addr] <= dataMem_2_2__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_2__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_3__T_319_en & dataMem_2_3__T_319_mask) begin
      dataMem_2_3[dataMem_2_3__T_319_addr] <= dataMem_2_3__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_3__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_4__T_319_en & dataMem_2_4__T_319_mask) begin
      dataMem_2_4[dataMem_2_4__T_319_addr] <= dataMem_2_4__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_4__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_5__T_319_en & dataMem_2_5__T_319_mask) begin
      dataMem_2_5[dataMem_2_5__T_319_addr] <= dataMem_2_5__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_5__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_6__T_319_en & dataMem_2_6__T_319_mask) begin
      dataMem_2_6[dataMem_2_6__T_319_addr] <= dataMem_2_6__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_6__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_2_7__T_319_en & dataMem_2_7__T_319_mask) begin
      dataMem_2_7[dataMem_2_7__T_319_addr] <= dataMem_2_7__T_319_data; // @[AXICache.scala 721:45]
    end
    dataMem_2_7__T_34_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_0__T_338_en & dataMem_3_0__T_338_mask) begin
      dataMem_3_0[dataMem_3_0__T_338_addr] <= dataMem_3_0__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_0__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_1__T_338_en & dataMem_3_1__T_338_mask) begin
      dataMem_3_1[dataMem_3_1__T_338_addr] <= dataMem_3_1__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_1__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_2__T_338_en & dataMem_3_2__T_338_mask) begin
      dataMem_3_2[dataMem_3_2__T_338_addr] <= dataMem_3_2__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_2__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_3__T_338_en & dataMem_3_3__T_338_mask) begin
      dataMem_3_3[dataMem_3_3__T_338_addr] <= dataMem_3_3__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_3__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_4__T_338_en & dataMem_3_4__T_338_mask) begin
      dataMem_3_4[dataMem_3_4__T_338_addr] <= dataMem_3_4__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_4__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_5__T_338_en & dataMem_3_5__T_338_mask) begin
      dataMem_3_5[dataMem_3_5__T_338_addr] <= dataMem_3_5__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_5__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_6__T_338_en & dataMem_3_6__T_338_mask) begin
      dataMem_3_6[dataMem_3_6__T_338_addr] <= dataMem_3_6__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_6__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_3_7__T_338_en & dataMem_3_7__T_338_mask) begin
      dataMem_3_7[dataMem_3_7__T_338_addr] <= dataMem_3_7__T_338_data; // @[AXICache.scala 721:45]
    end
    dataMem_3_7__T_44_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_0__T_357_en & dataMem_4_0__T_357_mask) begin
      dataMem_4_0[dataMem_4_0__T_357_addr] <= dataMem_4_0__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_0__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_1__T_357_en & dataMem_4_1__T_357_mask) begin
      dataMem_4_1[dataMem_4_1__T_357_addr] <= dataMem_4_1__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_1__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_2__T_357_en & dataMem_4_2__T_357_mask) begin
      dataMem_4_2[dataMem_4_2__T_357_addr] <= dataMem_4_2__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_2__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_3__T_357_en & dataMem_4_3__T_357_mask) begin
      dataMem_4_3[dataMem_4_3__T_357_addr] <= dataMem_4_3__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_3__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_4__T_357_en & dataMem_4_4__T_357_mask) begin
      dataMem_4_4[dataMem_4_4__T_357_addr] <= dataMem_4_4__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_4__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_5__T_357_en & dataMem_4_5__T_357_mask) begin
      dataMem_4_5[dataMem_4_5__T_357_addr] <= dataMem_4_5__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_5__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_6__T_357_en & dataMem_4_6__T_357_mask) begin
      dataMem_4_6[dataMem_4_6__T_357_addr] <= dataMem_4_6__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_6__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_4_7__T_357_en & dataMem_4_7__T_357_mask) begin
      dataMem_4_7[dataMem_4_7__T_357_addr] <= dataMem_4_7__T_357_data; // @[AXICache.scala 721:45]
    end
    dataMem_4_7__T_54_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_0__T_376_en & dataMem_5_0__T_376_mask) begin
      dataMem_5_0[dataMem_5_0__T_376_addr] <= dataMem_5_0__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_0__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_1__T_376_en & dataMem_5_1__T_376_mask) begin
      dataMem_5_1[dataMem_5_1__T_376_addr] <= dataMem_5_1__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_1__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_2__T_376_en & dataMem_5_2__T_376_mask) begin
      dataMem_5_2[dataMem_5_2__T_376_addr] <= dataMem_5_2__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_2__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_3__T_376_en & dataMem_5_3__T_376_mask) begin
      dataMem_5_3[dataMem_5_3__T_376_addr] <= dataMem_5_3__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_3__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_4__T_376_en & dataMem_5_4__T_376_mask) begin
      dataMem_5_4[dataMem_5_4__T_376_addr] <= dataMem_5_4__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_4__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_5__T_376_en & dataMem_5_5__T_376_mask) begin
      dataMem_5_5[dataMem_5_5__T_376_addr] <= dataMem_5_5__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_5__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_6__T_376_en & dataMem_5_6__T_376_mask) begin
      dataMem_5_6[dataMem_5_6__T_376_addr] <= dataMem_5_6__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_6__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_5_7__T_376_en & dataMem_5_7__T_376_mask) begin
      dataMem_5_7[dataMem_5_7__T_376_addr] <= dataMem_5_7__T_376_data; // @[AXICache.scala 721:45]
    end
    dataMem_5_7__T_64_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_0__T_395_en & dataMem_6_0__T_395_mask) begin
      dataMem_6_0[dataMem_6_0__T_395_addr] <= dataMem_6_0__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_0__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_1__T_395_en & dataMem_6_1__T_395_mask) begin
      dataMem_6_1[dataMem_6_1__T_395_addr] <= dataMem_6_1__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_1__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_2__T_395_en & dataMem_6_2__T_395_mask) begin
      dataMem_6_2[dataMem_6_2__T_395_addr] <= dataMem_6_2__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_2__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_3__T_395_en & dataMem_6_3__T_395_mask) begin
      dataMem_6_3[dataMem_6_3__T_395_addr] <= dataMem_6_3__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_3__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_4__T_395_en & dataMem_6_4__T_395_mask) begin
      dataMem_6_4[dataMem_6_4__T_395_addr] <= dataMem_6_4__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_4__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_5__T_395_en & dataMem_6_5__T_395_mask) begin
      dataMem_6_5[dataMem_6_5__T_395_addr] <= dataMem_6_5__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_5__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_6__T_395_en & dataMem_6_6__T_395_mask) begin
      dataMem_6_6[dataMem_6_6__T_395_addr] <= dataMem_6_6__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_6__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_6_7__T_395_en & dataMem_6_7__T_395_mask) begin
      dataMem_6_7[dataMem_6_7__T_395_addr] <= dataMem_6_7__T_395_data; // @[AXICache.scala 721:45]
    end
    dataMem_6_7__T_74_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_0__T_414_en & dataMem_7_0__T_414_mask) begin
      dataMem_7_0[dataMem_7_0__T_414_addr] <= dataMem_7_0__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_0__T_84_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_1__T_414_en & dataMem_7_1__T_414_mask) begin
      dataMem_7_1[dataMem_7_1__T_414_addr] <= dataMem_7_1__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_1__T_84_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_2__T_414_en & dataMem_7_2__T_414_mask) begin
      dataMem_7_2[dataMem_7_2__T_414_addr] <= dataMem_7_2__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_2__T_84_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_3__T_414_en & dataMem_7_3__T_414_mask) begin
      dataMem_7_3[dataMem_7_3__T_414_addr] <= dataMem_7_3__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_3__T_84_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_4__T_414_en & dataMem_7_4__T_414_mask) begin
      dataMem_7_4[dataMem_7_4__T_414_addr] <= dataMem_7_4__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_4__T_84_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_5__T_414_en & dataMem_7_5__T_414_mask) begin
      dataMem_7_5[dataMem_7_5__T_414_addr] <= dataMem_7_5__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_5__T_84_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_6__T_414_en & dataMem_7_6__T_414_mask) begin
      dataMem_7_6[dataMem_7_6__T_414_addr] <= dataMem_7_6__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_6__T_84_addr_pipe_0 <= set_count - 8'h1;
    if(dataMem_7_7__T_414_en & dataMem_7_7__T_414_mask) begin
      dataMem_7_7[dataMem_7_7__T_414_addr] <= dataMem_7_7__T_414_data; // @[AXICache.scala 721:45]
    end
    dataMem_7_7__T_84_addr_pipe_0 <= set_count - 8'h1;
    if (reset) begin
      state <= 3'h0;
    end else if (!(_T_458)) begin
      if (_T_461) begin
        if (hit) begin
          state <= 3'h0;
        end else if (_T_465) begin
          state <= 3'h3;
        end else if (_T_466) begin
          state <= 3'h6;
        end
      end else if (_T_467) begin
        if (_T_99) begin
          state <= 3'h0;
        end else if (_T_465) begin
          state <= 3'h3;
        end else if (_T_466) begin
          state <= 3'h6;
        end
      end else if (_T_473) begin
        if (write_wrap_out) begin
          state <= 3'h4;
        end
      end else if (_T_474) begin
        if (io_mem_wr_ack) begin
          state <= 3'h5;
        end
      end else if (_T_475) begin
        if (_T_466) begin
          state <= 3'h6;
        end
      end else if (_T_477) begin
        if (read_wrap_out) begin
          state <= 3'h0;
        end
      end
    end
    if (reset) begin
      flush_state <= 3'h0;
    end else if (_T_480) begin
      if (io_cpu_flush) begin
        flush_state <= 3'h1;
      end
    end else if (_T_481) begin
      if (set_wrap) begin
        flush_state <= 3'h0;
      end else if (is_block_dirty) begin
        flush_state <= 3'h2;
      end
    end else if (_T_482) begin
      flush_state <= 3'h3;
    end else if (_T_483) begin
      if (_T_465) begin
        flush_state <= 3'h4;
      end
    end else if (_T_485) begin
      if (write_wrap_out) begin
        flush_state <= 3'h5;
      end
    end else if (_T_486) begin
      if (io_mem_wr_ack) begin
        flush_state <= 3'h1;
      end
    end
    if (reset) begin
      flush_mode <= 1'h0;
    end else if (_T_480) begin
      flush_mode <= _GEN_376;
    end else if (_T_481) begin
      if (set_wrap) begin
        flush_mode <= 1'h0;
      end
    end
    if (reset) begin
      v <= 256'h0;
    end else if (wen) begin
      v <= _T_250;
    end
    if (reset) begin
      d <= 256'h0;
    end else if (wen) begin
      if (_T_234) begin
        d <= _T_257;
      end else begin
        d <= _T_260;
      end
    end
    if (reset) begin
      read_count <= 3'h0;
    end else if (_T) begin
      read_count <= _T_3;
    end
    if (reset) begin
      write_count <= 3'h0;
    end else if (_T_4) begin
      write_count <= _T_7;
    end
    if (reset) begin
      set_count <= 8'h0;
    end else if (_T_8) begin
      set_count <= _T_11;
    end
    block_rmeta_tag <= metaMem_tag__T_431_data;
    is_alloc_reg <= _T_98 & read_wrap_out;
    if (_T) begin
      if (3'h0 == read_count) begin
        refill_buf_0 <= io_mem_rd_data_bits;
      end
    end
    if (_T) begin
      if (3'h1 == read_count) begin
        refill_buf_1 <= io_mem_rd_data_bits;
      end
    end
    if (_T) begin
      if (3'h2 == read_count) begin
        refill_buf_2 <= io_mem_rd_data_bits;
      end
    end
    if (_T) begin
      if (3'h3 == read_count) begin
        refill_buf_3 <= io_mem_rd_data_bits;
      end
    end
    if (_T) begin
      if (3'h4 == read_count) begin
        refill_buf_4 <= io_mem_rd_data_bits;
      end
    end
    if (_T) begin
      if (3'h5 == read_count) begin
        refill_buf_5 <= io_mem_rd_data_bits;
      end
    end
    if (_T) begin
      if (3'h6 == read_count) begin
        refill_buf_6 <= io_mem_rd_data_bits;
      end
    end
    if (_T) begin
      if (3'h7 == read_count) begin
        refill_buf_7 <= io_mem_rd_data_bits;
      end
    end
  end
endmodule
module SplitCallDCR(
  input         clock,
  input         reset,
  output        io_In_ready,
  input         io_In_valid,
  input  [63:0] io_In_bits_dataVals_field1_data,
  input  [63:0] io_In_bits_dataVals_field0_data,
  input         io_Out_enable_ready,
  output        io_Out_enable_valid,
  output        io_Out_enable_bits_control,
  input         io_Out_dataVals_field1_0_ready,
  output        io_Out_dataVals_field1_0_valid,
  output [63:0] io_Out_dataVals_field1_0_bits_data,
  input         io_Out_dataVals_field0_0_ready,
  output        io_Out_dataVals_field0_0_valid,
  output [63:0] io_Out_dataVals_field0_0_bits_data
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [63:0] _RAND_1;
  reg [63:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
`endif // RANDOMIZE_REG_INIT
  reg  inputReg_enable_control; // @[SplitDecoupled.scala 220:26]
  reg [63:0] inputReg_dataVals_field1_data; // @[SplitDecoupled.scala 220:26]
  reg [63:0] inputReg_dataVals_field0_data; // @[SplitDecoupled.scala 220:26]
  reg  enableValidReg; // @[SplitDecoupled.scala 222:31]
  reg  outputValsValidReg_0_0; // @[SplitDecoupled.scala 230:53]
  reg  outputValsValidReg_1_0; // @[SplitDecoupled.scala 230:53]
  reg  state; // @[SplitDecoupled.scala 260:22]
  wire  _T_1 = ~state; // @[SplitDecoupled.scala 262:24]
  wire  _T_3 = io_In_ready & io_In_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_0 = _T_3 | state; // @[SplitDecoupled.scala 266:27]
  wire  _GEN_2 = _T_3 | inputReg_enable_control; // @[SplitDecoupled.scala 266:27]
  wire  _T_5 = outputValsValidReg_0_0 & outputValsValidReg_1_0; // @[SplitDecoupled.scala 254:31]
  wire  _T_6 = ~_T_5; // @[SplitDecoupled.scala 254:7]
  wire  _T_8 = ~enableValidReg; // @[SplitDecoupled.scala 272:43]
  wire  _T_9 = _T_6 & _T_8; // @[SplitDecoupled.scala 272:40]
  wire  _T_11 = io_In_valid & _T_1; // @[SplitDecoupled.scala 293:24]
  wire  _GEN_22 = _T_11 | outputValsValidReg_0_0; // @[SplitDecoupled.scala 293:45]
  wire  _T_13 = state & io_Out_dataVals_field0_0_ready; // @[SplitDecoupled.scala 296:32]
  wire  _GEN_24 = _T_11 | outputValsValidReg_1_0; // @[SplitDecoupled.scala 293:45]
  wire  _T_17 = state & io_Out_dataVals_field1_0_ready; // @[SplitDecoupled.scala 296:32]
  wire  _GEN_26 = _T_11 | enableValidReg; // @[SplitDecoupled.scala 305:41]
  wire  _T_21 = state & io_Out_enable_ready; // @[SplitDecoupled.scala 308:28]
  assign io_In_ready = ~state; // @[SplitDecoupled.scala 262:15]
  assign io_Out_enable_valid = enableValidReg; // @[SplitDecoupled.scala 312:23]
  assign io_Out_enable_bits_control = inputReg_enable_control; // @[SplitDecoupled.scala 313:22]
  assign io_Out_dataVals_field1_0_valid = outputValsValidReg_1_0; // @[SplitDecoupled.scala 299:44]
  assign io_Out_dataVals_field1_0_bits_data = inputReg_dataVals_field1_data; // @[SplitDecoupled.scala 300:43]
  assign io_Out_dataVals_field0_0_valid = outputValsValidReg_0_0; // @[SplitDecoupled.scala 299:44]
  assign io_Out_dataVals_field0_0_bits_data = inputReg_dataVals_field0_data; // @[SplitDecoupled.scala 300:43]
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
  inputReg_dataVals_field1_data = _RAND_1[63:0];
  _RAND_2 = {2{`RANDOM}};
  inputReg_dataVals_field0_data = _RAND_2[63:0];
  _RAND_3 = {1{`RANDOM}};
  enableValidReg = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  outputValsValidReg_0_0 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  outputValsValidReg_1_0 = _RAND_5[0:0];
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
      inputReg_enable_control <= 1'h0;
    end else if (_T_1) begin
      inputReg_enable_control <= _GEN_2;
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
    end else if (_T_21) begin
      enableValidReg <= 1'h0;
    end else begin
      enableValidReg <= _GEN_26;
    end
    if (reset) begin
      outputValsValidReg_0_0 <= 1'h0;
    end else if (_T_13) begin
      outputValsValidReg_0_0 <= 1'h0;
    end else begin
      outputValsValidReg_0_0 <= _GEN_22;
    end
    if (reset) begin
      outputValsValidReg_1_0 <= 1'h0;
    end else if (_T_17) begin
      outputValsValidReg_1_0 <= 1'h0;
    end else begin
      outputValsValidReg_1_0 <= _GEN_24;
    end
    if (reset) begin
      state <= 1'h0;
    end else if (_T_1) begin
      state <= _GEN_0;
    end else if (state) begin
      if (_T_9) begin
        state <= 1'h0;
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
  output  io_Out_0_bits_control,
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
  reg [31:0] _RAND_7;
`endif // RANDOMIZE_REG_INIT
  reg [14:0] cycleCount; // @[Counter.scala 29:33]
  wire [14:0] _T_3 = cycleCount + 15'h1; // @[Counter.scala 39:22]
  reg  in_data_R_0_control; // @[BasicBlock.scala 224:46]
  reg  in_data_valid_R_0; // @[BasicBlock.scala 225:52]
  reg  output_valid_R_0; // @[BasicBlock.scala 228:49]
  reg  output_valid_R_1; // @[BasicBlock.scala 228:49]
  reg  output_fire_R_0; // @[BasicBlock.scala 229:48]
  reg  output_fire_R_1; // @[BasicBlock.scala 229:48]
  wire  _T_7 = io_predicateIn_0_ready & io_predicateIn_0_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_3 = _T_7 ? io_predicateIn_0_bits_control : in_data_R_0_control; // @[BasicBlock.scala 234:36]
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
  wire  _T_24 = ~reset; // @[BasicBlock.scala 311:17]
  wire  _GEN_10 = _GEN_5 | output_valid_R_0; // @[BasicBlock.scala 301:9]
  wire  _GEN_11 = _GEN_5 | output_valid_R_1; // @[BasicBlock.scala 301:9]
  wire  _GEN_14 = _GEN_5 | state; // @[BasicBlock.scala 301:9]
  wire  _T_26 = out_fire_mask_0 & out_fire_mask_1; // @[BasicBlock.scala 317:35]
  wire  _GEN_40 = _T_18 & _GEN_5; // @[BasicBlock.scala 311:17]
  assign io_predicateIn_0_ready = ~in_data_valid_R_0; // @[BasicBlock.scala 233:29]
  assign io_Out_0_valid = _T_18 ? _GEN_10 : output_valid_R_0; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
  assign io_Out_0_bits_control = _T_7 ? io_predicateIn_0_bits_control : in_data_R_0_control; // @[BasicBlock.scala 279:22]
  assign io_Out_1_valid = _T_18 ? _GEN_11 : output_valid_R_1; // @[BasicBlock.scala 284:21 BasicBlock.scala 303:34]
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
  cycleCount = _RAND_0[14:0];
  _RAND_1 = {1{`RANDOM}};
  in_data_R_0_control = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  output_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  output_valid_R_1 = _RAND_4[0:0];
  _RAND_5 = {1{`RANDOM}};
  output_fire_R_0 = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  output_fire_R_1 = _RAND_6[0:0];
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
      cycleCount <= 15'h0;
    end else begin
      cycleCount <= _T_3;
    end
    if (reset) begin
      in_data_R_0_control <= 1'h0;
    end else if (_T_18) begin
      if (_T_7) begin
        in_data_R_0_control <= io_predicateIn_0_bits_control;
      end
    end else if (state) begin
      if (_T_26) begin
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
      if (_T_26) begin
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
      if (_T_26) begin
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
      if (_T_26) begin
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
      if (_T_26) begin
        state <= 1'h0;
      end
    end
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_40 & _T_24) begin
          $fwrite(32'h80000002,"[LOG] [Test01] [TID: %d] [BB] [bb_entry0] [Out: %d] [Cycle: %d]\n",5'h0,_GEN_3,cycleCount); // @[BasicBlock.scala 311:17]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
  end
endmodule
module UALU(
  input  [63:0] io_in1,
  input  [63:0] io_in2,
  output [63:0] io_out
);
  wire [127:0] _T_24 = io_in1 * io_in2; // @[Alu.scala 194:32]
  assign io_out = _T_24[63:0]; // @[Alu.scala 235:10]
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
  input         io_RightIO_valid,
  input  [63:0] io_RightIO_bits_data
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
  reg [63:0] _RAND_10;
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
  reg [14:0] cycleCount; // @[Counter.scala 29:33]
  wire [14:0] _T_7 = cycleCount + 15'h1; // @[Counter.scala 39:22]
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
  wire  _T_34 = ~reset; // @[ComputeNode.scala 178:17]
  wire [63:0] _T_30_data = FU_io_out; // @[interfaces.scala 289:20 interfaces.scala 290:15]
  wire [63:0] _GEN_17 = _T_28 ? _T_30_data : out_data_R; // @[ComputeNode.scala 147:81]
  wire  _GEN_20 = _T_28 | out_valid_R_0; // @[ComputeNode.scala 147:81]
  wire  _GEN_24 = _T_28 | state; // @[ComputeNode.scala 147:81]
  wire  _T_37 = out_ready_R_0 | _T_1; // @[HandShaking.scala 251:83]
  wire  _GEN_47 = _T_22 & _T_28; // @[ComputeNode.scala 178:17]
  UALU FU ( // @[ComputeNode.scala 61:18]
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
  _RAND_4 = {1{`RANDOM}};
  cycleCount = _RAND_4[14:0];
  _RAND_5 = {2{`RANDOM}};
  left_R_data = _RAND_5[63:0];
  _RAND_6 = {1{`RANDOM}};
  left_valid_R = _RAND_6[0:0];
  _RAND_7 = {2{`RANDOM}};
  right_R_data = _RAND_7[63:0];
  _RAND_8 = {1{`RANDOM}};
  right_valid_R = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  state = _RAND_9[0:0];
  _RAND_10 = {2{`RANDOM}};
  out_data_R = _RAND_10[63:0];
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
      if (_T_37) begin
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
      if (_T_37) begin
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
      cycleCount <= 15'h0;
    end else begin
      cycleCount <= _T_7;
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
      if (_T_37) begin
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
      if (_T_37) begin
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
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_47 & _T_34) begin
          $fwrite(32'h80000002,"[LOG] [Test01] [TID: %d] [COMPUTE] [binaryOp_mul0] [Pred: %d] [In(0): 0x%x] [In(1) 0x%x] [Out: 0x%x] [OpCode: mul] [Cycle: %d]\n",5'h0,enable_R_control,left_R_data,right_R_data,FU_io_out,cycleCount); // @[ComputeNode.scala 178:17]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
  end
endmodule
module RetNode2(
  input         clock,
  input         reset,
  output        io_In_enable_ready,
  input         io_In_enable_valid,
  output        io_In_data_field0_ready,
  input         io_In_data_field0_valid,
  input  [31:0] io_In_data_field0_bits_data,
  input         io_Out_ready,
  output        io_Out_valid,
  output [31:0] io_Out_bits_data_field0_data
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
  reg [14:0] cycleCount; // @[Counter.scala 29:33]
  wire [14:0] _T_3 = cycleCount + 15'h1; // @[Counter.scala 39:22]
  reg  state; // @[RetNode.scala 141:22]
  reg  enable_valid_R; // @[RetNode.scala 144:31]
  reg  in_data_valid_R_0; // @[RetNode.scala 147:58]
  reg [31:0] output_R_data_field0_data; // @[RetNode.scala 150:25]
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
  wire  _T_13 = ~reset; // @[RetNode.scala 221:17]
  wire  _GEN_32 = ~_T_10; // @[RetNode.scala 221:17]
  wire  _GEN_33 = _GEN_32 & state; // @[RetNode.scala 221:17]
  wire  _GEN_34 = _GEN_33 & out_ready_R; // @[RetNode.scala 221:17]
  assign io_In_enable_ready = ~enable_valid_R; // @[RetNode.scala 163:22]
  assign io_In_data_field0_ready = ~in_data_valid_R_0; // @[RetNode.scala 171:34]
  assign io_Out_valid = out_valid_R; // @[RetNode.scala 180:16]
  assign io_Out_bits_data_field0_data = output_R_data_field0_data; // @[RetNode.scala 179:15]
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
  cycleCount = _RAND_0[14:0];
  _RAND_1 = {1{`RANDOM}};
  state = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  enable_valid_R = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  in_data_valid_R_0 = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  output_R_data_field0_data = _RAND_4[31:0];
  _RAND_5 = {1{`RANDOM}};
  out_ready_R = _RAND_5[0:0];
  _RAND_6 = {1{`RANDOM}};
  out_valid_R = _RAND_6[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
  always @(posedge clock) begin
    if (reset) begin
      cycleCount <= 15'h0;
    end else begin
      cycleCount <= _T_3;
    end
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
      output_R_data_field0_data <= 32'h0;
    end else if (_T_8) begin
      output_R_data_field0_data <= io_In_data_field0_bits_data;
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
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_34 & _T_13) begin
          $fwrite(32'h80000002,"[LOG] [Test01] [TID: %d] [ret_1] [Cycle: %d]\n",5'h0,cycleCount); // @[RetNode.scala 221:17]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
  end
endmodule
module test01DF(
  input         clock,
  input         reset,
  output        io_in_ready,
  input         io_in_valid,
  input  [63:0] io_in_bits_dataVals_field1_data,
  input  [63:0] io_in_bits_dataVals_field0_data,
  input         io_out_ready,
  output        io_out_valid,
  output [63:0] io_out_bits_data_field0_data
);
  wire  ArgSplitter_clock; // @[test01.scala 38:27]
  wire  ArgSplitter_reset; // @[test01.scala 38:27]
  wire  ArgSplitter_io_In_ready; // @[test01.scala 38:27]
  wire  ArgSplitter_io_In_valid; // @[test01.scala 38:27]
  wire [63:0] ArgSplitter_io_In_bits_dataVals_field1_data; // @[test01.scala 38:27]
  wire [63:0] ArgSplitter_io_In_bits_dataVals_field0_data; // @[test01.scala 38:27]
  wire  ArgSplitter_io_Out_enable_ready; // @[test01.scala 38:27]
  wire  ArgSplitter_io_Out_enable_valid; // @[test01.scala 38:27]
  wire  ArgSplitter_io_Out_enable_bits_control; // @[test01.scala 38:27]
  wire  ArgSplitter_io_Out_dataVals_field1_0_ready; // @[test01.scala 38:27]
  wire  ArgSplitter_io_Out_dataVals_field1_0_valid; // @[test01.scala 38:27]
  wire [63:0] ArgSplitter_io_Out_dataVals_field1_0_bits_data; // @[test01.scala 38:27]
  wire  ArgSplitter_io_Out_dataVals_field0_0_ready; // @[test01.scala 38:27]
  wire  ArgSplitter_io_Out_dataVals_field0_0_valid; // @[test01.scala 38:27]
  wire [63:0] ArgSplitter_io_Out_dataVals_field0_0_bits_data; // @[test01.scala 38:27]
  wire  bb_entry0_clock; // @[test01.scala 53:25]
  wire  bb_entry0_reset; // @[test01.scala 53:25]
  wire  bb_entry0_io_predicateIn_0_ready; // @[test01.scala 53:25]
  wire  bb_entry0_io_predicateIn_0_valid; // @[test01.scala 53:25]
  wire  bb_entry0_io_predicateIn_0_bits_control; // @[test01.scala 53:25]
  wire  bb_entry0_io_Out_0_ready; // @[test01.scala 53:25]
  wire  bb_entry0_io_Out_0_valid; // @[test01.scala 53:25]
  wire  bb_entry0_io_Out_0_bits_control; // @[test01.scala 53:25]
  wire  bb_entry0_io_Out_1_ready; // @[test01.scala 53:25]
  wire  bb_entry0_io_Out_1_valid; // @[test01.scala 53:25]
  wire  binaryOp_mul0_clock; // @[test01.scala 62:29]
  wire  binaryOp_mul0_reset; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_enable_ready; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_enable_valid; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_enable_bits_control; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_Out_0_ready; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_Out_0_valid; // @[test01.scala 62:29]
  wire [63:0] binaryOp_mul0_io_Out_0_bits_data; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_LeftIO_ready; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_LeftIO_valid; // @[test01.scala 62:29]
  wire [63:0] binaryOp_mul0_io_LeftIO_bits_data; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_RightIO_ready; // @[test01.scala 62:29]
  wire  binaryOp_mul0_io_RightIO_valid; // @[test01.scala 62:29]
  wire [63:0] binaryOp_mul0_io_RightIO_bits_data; // @[test01.scala 62:29]
  wire  ret_1_clock; // @[test01.scala 65:21]
  wire  ret_1_reset; // @[test01.scala 65:21]
  wire  ret_1_io_In_enable_ready; // @[test01.scala 65:21]
  wire  ret_1_io_In_enable_valid; // @[test01.scala 65:21]
  wire  ret_1_io_In_data_field0_ready; // @[test01.scala 65:21]
  wire  ret_1_io_In_data_field0_valid; // @[test01.scala 65:21]
  wire [31:0] ret_1_io_In_data_field0_bits_data; // @[test01.scala 65:21]
  wire  ret_1_io_Out_ready; // @[test01.scala 65:21]
  wire  ret_1_io_Out_valid; // @[test01.scala 65:21]
  wire [31:0] ret_1_io_Out_bits_data_field0_data; // @[test01.scala 65:21]
  SplitCallDCR ArgSplitter ( // @[test01.scala 38:27]
    .clock(ArgSplitter_clock),
    .reset(ArgSplitter_reset),
    .io_In_ready(ArgSplitter_io_In_ready),
    .io_In_valid(ArgSplitter_io_In_valid),
    .io_In_bits_dataVals_field1_data(ArgSplitter_io_In_bits_dataVals_field1_data),
    .io_In_bits_dataVals_field0_data(ArgSplitter_io_In_bits_dataVals_field0_data),
    .io_Out_enable_ready(ArgSplitter_io_Out_enable_ready),
    .io_Out_enable_valid(ArgSplitter_io_Out_enable_valid),
    .io_Out_enable_bits_control(ArgSplitter_io_Out_enable_bits_control),
    .io_Out_dataVals_field1_0_ready(ArgSplitter_io_Out_dataVals_field1_0_ready),
    .io_Out_dataVals_field1_0_valid(ArgSplitter_io_Out_dataVals_field1_0_valid),
    .io_Out_dataVals_field1_0_bits_data(ArgSplitter_io_Out_dataVals_field1_0_bits_data),
    .io_Out_dataVals_field0_0_ready(ArgSplitter_io_Out_dataVals_field0_0_ready),
    .io_Out_dataVals_field0_0_valid(ArgSplitter_io_Out_dataVals_field0_0_valid),
    .io_Out_dataVals_field0_0_bits_data(ArgSplitter_io_Out_dataVals_field0_0_bits_data)
  );
  BasicBlockNoMaskFastNode bb_entry0 ( // @[test01.scala 53:25]
    .clock(bb_entry0_clock),
    .reset(bb_entry0_reset),
    .io_predicateIn_0_ready(bb_entry0_io_predicateIn_0_ready),
    .io_predicateIn_0_valid(bb_entry0_io_predicateIn_0_valid),
    .io_predicateIn_0_bits_control(bb_entry0_io_predicateIn_0_bits_control),
    .io_Out_0_ready(bb_entry0_io_Out_0_ready),
    .io_Out_0_valid(bb_entry0_io_Out_0_valid),
    .io_Out_0_bits_control(bb_entry0_io_Out_0_bits_control),
    .io_Out_1_ready(bb_entry0_io_Out_1_ready),
    .io_Out_1_valid(bb_entry0_io_Out_1_valid)
  );
  ComputeNode binaryOp_mul0 ( // @[test01.scala 62:29]
    .clock(binaryOp_mul0_clock),
    .reset(binaryOp_mul0_reset),
    .io_enable_ready(binaryOp_mul0_io_enable_ready),
    .io_enable_valid(binaryOp_mul0_io_enable_valid),
    .io_enable_bits_control(binaryOp_mul0_io_enable_bits_control),
    .io_Out_0_ready(binaryOp_mul0_io_Out_0_ready),
    .io_Out_0_valid(binaryOp_mul0_io_Out_0_valid),
    .io_Out_0_bits_data(binaryOp_mul0_io_Out_0_bits_data),
    .io_LeftIO_ready(binaryOp_mul0_io_LeftIO_ready),
    .io_LeftIO_valid(binaryOp_mul0_io_LeftIO_valid),
    .io_LeftIO_bits_data(binaryOp_mul0_io_LeftIO_bits_data),
    .io_RightIO_ready(binaryOp_mul0_io_RightIO_ready),
    .io_RightIO_valid(binaryOp_mul0_io_RightIO_valid),
    .io_RightIO_bits_data(binaryOp_mul0_io_RightIO_bits_data)
  );
  RetNode2 ret_1 ( // @[test01.scala 65:21]
    .clock(ret_1_clock),
    .reset(ret_1_reset),
    .io_In_enable_ready(ret_1_io_In_enable_ready),
    .io_In_enable_valid(ret_1_io_In_enable_valid),
    .io_In_data_field0_ready(ret_1_io_In_data_field0_ready),
    .io_In_data_field0_valid(ret_1_io_In_data_field0_valid),
    .io_In_data_field0_bits_data(ret_1_io_In_data_field0_bits_data),
    .io_Out_ready(ret_1_io_Out_ready),
    .io_Out_valid(ret_1_io_Out_valid),
    .io_Out_bits_data_field0_data(ret_1_io_Out_bits_data_field0_data)
  );
  assign io_in_ready = ArgSplitter_io_In_ready; // @[test01.scala 39:21]
  assign io_out_valid = ret_1_io_Out_valid; // @[test01.scala 195:10]
  assign io_out_bits_data_field0_data = {{32'd0}, ret_1_io_Out_bits_data_field0_data}; // @[test01.scala 195:10]
  assign ArgSplitter_clock = clock;
  assign ArgSplitter_reset = reset;
  assign ArgSplitter_io_In_valid = io_in_valid; // @[test01.scala 39:21]
  assign ArgSplitter_io_In_bits_dataVals_field1_data = io_in_bits_dataVals_field1_data; // @[test01.scala 39:21]
  assign ArgSplitter_io_In_bits_dataVals_field0_data = io_in_bits_dataVals_field0_data; // @[test01.scala 39:21]
  assign ArgSplitter_io_Out_enable_ready = bb_entry0_io_predicateIn_0_ready; // @[test01.scala 79:31]
  assign ArgSplitter_io_Out_dataVals_field1_0_ready = binaryOp_mul0_io_LeftIO_ready; // @[test01.scala 181:27]
  assign ArgSplitter_io_Out_dataVals_field0_0_ready = binaryOp_mul0_io_RightIO_ready; // @[test01.scala 179:28]
  assign bb_entry0_clock = clock;
  assign bb_entry0_reset = reset;
  assign bb_entry0_io_predicateIn_0_valid = ArgSplitter_io_Out_enable_valid; // @[test01.scala 79:31]
  assign bb_entry0_io_predicateIn_0_bits_control = ArgSplitter_io_Out_enable_bits_control; // @[test01.scala 79:31]
  assign bb_entry0_io_Out_0_ready = binaryOp_mul0_io_enable_ready; // @[test01.scala 147:27]
  assign bb_entry0_io_Out_1_ready = ret_1_io_In_enable_ready; // @[test01.scala 150:22]
  assign binaryOp_mul0_clock = clock;
  assign binaryOp_mul0_reset = reset;
  assign binaryOp_mul0_io_enable_valid = bb_entry0_io_Out_0_valid; // @[test01.scala 147:27]
  assign binaryOp_mul0_io_enable_bits_control = bb_entry0_io_Out_0_bits_control; // @[test01.scala 147:27]
  assign binaryOp_mul0_io_Out_0_ready = ret_1_io_In_data_field0_ready; // @[test01.scala 177:30]
  assign binaryOp_mul0_io_LeftIO_valid = ArgSplitter_io_Out_dataVals_field1_0_valid; // @[test01.scala 181:27]
  assign binaryOp_mul0_io_LeftIO_bits_data = ArgSplitter_io_Out_dataVals_field1_0_bits_data; // @[test01.scala 181:27]
  assign binaryOp_mul0_io_RightIO_valid = ArgSplitter_io_Out_dataVals_field0_0_valid; // @[test01.scala 179:28]
  assign binaryOp_mul0_io_RightIO_bits_data = ArgSplitter_io_Out_dataVals_field0_0_bits_data; // @[test01.scala 179:28]
  assign ret_1_clock = clock;
  assign ret_1_reset = reset;
  assign ret_1_io_In_enable_valid = bb_entry0_io_Out_1_valid; // @[test01.scala 150:22]
  assign ret_1_io_In_data_field0_valid = binaryOp_mul0_io_Out_0_valid; // @[test01.scala 177:30]
  assign ret_1_io_In_data_field0_bits_data = binaryOp_mul0_io_Out_0_bits_data[31:0]; // @[test01.scala 177:30]
  assign ret_1_io_Out_ready = io_out_ready; // @[test01.scala 195:10]
endmodule
module test01RootDF(
  input         clock,
  input         reset,
  output        io_in_ready,
  input         io_in_valid,
  input  [63:0] io_in_bits_dataVals_field1_data,
  input  [63:0] io_in_bits_dataVals_field0_data,
  input         io_out_ready,
  output        io_out_valid,
  output [63:0] io_out_bits_data_field0_data
);
  wire  test01_clock; // @[test01_root.scala 21:23]
  wire  test01_reset; // @[test01_root.scala 21:23]
  wire  test01_io_in_ready; // @[test01_root.scala 21:23]
  wire  test01_io_in_valid; // @[test01_root.scala 21:23]
  wire [63:0] test01_io_in_bits_dataVals_field1_data; // @[test01_root.scala 21:23]
  wire [63:0] test01_io_in_bits_dataVals_field0_data; // @[test01_root.scala 21:23]
  wire  test01_io_out_ready; // @[test01_root.scala 21:23]
  wire  test01_io_out_valid; // @[test01_root.scala 21:23]
  wire [63:0] test01_io_out_bits_data_field0_data; // @[test01_root.scala 21:23]
  test01DF test01 ( // @[test01_root.scala 21:23]
    .clock(test01_clock),
    .reset(test01_reset),
    .io_in_ready(test01_io_in_ready),
    .io_in_valid(test01_io_in_valid),
    .io_in_bits_dataVals_field1_data(test01_io_in_bits_dataVals_field1_data),
    .io_in_bits_dataVals_field0_data(test01_io_in_bits_dataVals_field0_data),
    .io_out_ready(test01_io_out_ready),
    .io_out_valid(test01_io_out_valid),
    .io_out_bits_data_field0_data(test01_io_out_bits_data_field0_data)
  );
  assign io_in_ready = test01_io_in_ready; // @[test01_root.scala 23:16]
  assign io_out_valid = test01_io_out_valid; // @[test01_root.scala 24:10]
  assign io_out_bits_data_field0_data = test01_io_out_bits_data_field0_data; // @[test01_root.scala 24:10]
  assign test01_clock = clock;
  assign test01_reset = reset;
  assign test01_io_in_valid = io_in_valid; // @[test01_root.scala 23:16]
  assign test01_io_in_bits_dataVals_field1_data = io_in_bits_dataVals_field1_data; // @[test01_root.scala 23:16]
  assign test01_io_in_bits_dataVals_field0_data = io_in_bits_dataVals_field0_data; // @[test01_root.scala 23:16]
  assign test01_io_out_ready = io_out_ready; // @[test01_root.scala 24:10]
endmodule
module DandelionDebugFPGAShell(
  input         clock,
  input         reset,
  output        io_host_aw_ready,
  input         io_host_aw_valid,
  input  [15:0] io_host_aw_bits_addr,
  input  [12:0] io_host_aw_bits_id,
  input  [9:0]  io_host_aw_bits_user,
  input  [3:0]  io_host_aw_bits_len,
  input  [2:0]  io_host_aw_bits_size,
  input  [1:0]  io_host_aw_bits_burst,
  input  [1:0]  io_host_aw_bits_lock,
  input  [3:0]  io_host_aw_bits_cache,
  input  [2:0]  io_host_aw_bits_prot,
  input  [3:0]  io_host_aw_bits_qos,
  input  [3:0]  io_host_aw_bits_region,
  output        io_host_w_ready,
  input         io_host_w_valid,
  input  [31:0] io_host_w_bits_data,
  input  [3:0]  io_host_w_bits_strb,
  input         io_host_w_bits_last,
  input  [12:0] io_host_w_bits_id,
  input  [9:0]  io_host_w_bits_user,
  input         io_host_b_ready,
  output        io_host_b_valid,
  output [1:0]  io_host_b_bits_resp,
  output [12:0] io_host_b_bits_id,
  output [9:0]  io_host_b_bits_user,
  output        io_host_ar_ready,
  input         io_host_ar_valid,
  input  [15:0] io_host_ar_bits_addr,
  input  [12:0] io_host_ar_bits_id,
  input  [9:0]  io_host_ar_bits_user,
  input  [3:0]  io_host_ar_bits_len,
  input  [2:0]  io_host_ar_bits_size,
  input  [1:0]  io_host_ar_bits_burst,
  input  [1:0]  io_host_ar_bits_lock,
  input  [3:0]  io_host_ar_bits_cache,
  input  [2:0]  io_host_ar_bits_prot,
  input  [3:0]  io_host_ar_bits_qos,
  input  [3:0]  io_host_ar_bits_region,
  input         io_host_r_ready,
  output        io_host_r_valid,
  output [31:0] io_host_r_bits_data,
  output [1:0]  io_host_r_bits_resp,
  output        io_host_r_bits_last,
  output [12:0] io_host_r_bits_id,
  output [9:0]  io_host_r_bits_user,
  input         io_mem_aw_ready,
  output        io_mem_aw_valid,
  output [31:0] io_mem_aw_bits_addr,
  output [15:0] io_mem_aw_bits_id,
  output [4:0]  io_mem_aw_bits_user,
  output [7:0]  io_mem_aw_bits_len,
  output [2:0]  io_mem_aw_bits_size,
  output [1:0]  io_mem_aw_bits_burst,
  output [1:0]  io_mem_aw_bits_lock,
  output [3:0]  io_mem_aw_bits_cache,
  output [2:0]  io_mem_aw_bits_prot,
  output [3:0]  io_mem_aw_bits_qos,
  output [3:0]  io_mem_aw_bits_region,
  input         io_mem_w_ready,
  output        io_mem_w_valid,
  output [63:0] io_mem_w_bits_data,
  output [7:0]  io_mem_w_bits_strb,
  output        io_mem_w_bits_last,
  output [15:0] io_mem_w_bits_id,
  output [4:0]  io_mem_w_bits_user,
  output        io_mem_b_ready,
  input         io_mem_b_valid,
  input  [1:0]  io_mem_b_bits_resp,
  input  [15:0] io_mem_b_bits_id,
  input  [4:0]  io_mem_b_bits_user,
  input         io_mem_ar_ready,
  output        io_mem_ar_valid,
  output [31:0] io_mem_ar_bits_addr,
  output [15:0] io_mem_ar_bits_id,
  output [4:0]  io_mem_ar_bits_user,
  output [7:0]  io_mem_ar_bits_len,
  output [2:0]  io_mem_ar_bits_size,
  output [1:0]  io_mem_ar_bits_burst,
  output [1:0]  io_mem_ar_bits_lock,
  output [3:0]  io_mem_ar_bits_cache,
  output [2:0]  io_mem_ar_bits_prot,
  output [3:0]  io_mem_ar_bits_qos,
  output [3:0]  io_mem_ar_bits_region,
  output        io_mem_r_ready,
  input         io_mem_r_valid,
  input  [63:0] io_mem_r_bits_data,
  input  [1:0]  io_mem_r_bits_resp,
  input         io_mem_r_bits_last,
  input  [15:0] io_mem_r_bits_id,
  input  [4:0]  io_mem_r_bits_user
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [63:0] _RAND_2;
  reg [63:0] _RAND_3;
  reg [31:0] _RAND_4;
`endif // RANDOMIZE_REG_INIT
  wire  dcr_clock; // @[DandelionShell.scala 817:19]
  wire  dcr_reset; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_aw_ready; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_aw_valid; // @[DandelionShell.scala 817:19]
  wire [15:0] dcr_io_host_aw_bits_addr; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_w_ready; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_w_valid; // @[DandelionShell.scala 817:19]
  wire [31:0] dcr_io_host_w_bits_data; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_b_ready; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_b_valid; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_ar_ready; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_ar_valid; // @[DandelionShell.scala 817:19]
  wire [15:0] dcr_io_host_ar_bits_addr; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_r_ready; // @[DandelionShell.scala 817:19]
  wire  dcr_io_host_r_valid; // @[DandelionShell.scala 817:19]
  wire [31:0] dcr_io_host_r_bits_data; // @[DandelionShell.scala 817:19]
  wire  dcr_io_dcr_launch; // @[DandelionShell.scala 817:19]
  wire  dcr_io_dcr_finish; // @[DandelionShell.scala 817:19]
  wire  dcr_io_dcr_ecnt_0_valid; // @[DandelionShell.scala 817:19]
  wire [31:0] dcr_io_dcr_ecnt_0_bits; // @[DandelionShell.scala 817:19]
  wire  dcr_io_dcr_ecnt_1_valid; // @[DandelionShell.scala 817:19]
  wire [31:0] dcr_io_dcr_ecnt_1_bits; // @[DandelionShell.scala 817:19]
  wire [31:0] dcr_io_dcr_vals_0; // @[DandelionShell.scala 817:19]
  wire [31:0] dcr_io_dcr_vals_1; // @[DandelionShell.scala 817:19]
  wire  dmem_clock; // @[DandelionShell.scala 818:20]
  wire  dmem_reset; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_aw_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_aw_valid; // @[DandelionShell.scala 818:20]
  wire [31:0] dmem_io_mem_aw_bits_addr; // @[DandelionShell.scala 818:20]
  wire [7:0] dmem_io_mem_aw_bits_len; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_w_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_w_valid; // @[DandelionShell.scala 818:20]
  wire [63:0] dmem_io_mem_w_bits_data; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_w_bits_last; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_b_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_b_valid; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_ar_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_ar_valid; // @[DandelionShell.scala 818:20]
  wire [31:0] dmem_io_mem_ar_bits_addr; // @[DandelionShell.scala 818:20]
  wire [7:0] dmem_io_mem_ar_bits_len; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_r_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_r_valid; // @[DandelionShell.scala 818:20]
  wire [63:0] dmem_io_mem_r_bits_data; // @[DandelionShell.scala 818:20]
  wire  dmem_io_mem_r_bits_last; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_rd_0_cmd_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_rd_0_cmd_valid; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_rd_0_data_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_rd_0_data_valid; // @[DandelionShell.scala 818:20]
  wire [63:0] dmem_io_dme_rd_0_data_bits; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_wr_0_cmd_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_wr_0_cmd_valid; // @[DandelionShell.scala 818:20]
  wire [31:0] dmem_io_dme_wr_0_cmd_bits_addr; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_wr_0_data_ready; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_wr_0_data_valid; // @[DandelionShell.scala 818:20]
  wire [63:0] dmem_io_dme_wr_0_data_bits; // @[DandelionShell.scala 818:20]
  wire  dmem_io_dme_wr_0_ack; // @[DandelionShell.scala 818:20]
  wire  cache_clock; // @[DandelionShell.scala 819:21]
  wire  cache_reset; // @[DandelionShell.scala 819:21]
  wire  cache_io_cpu_flush; // @[DandelionShell.scala 819:21]
  wire  cache_io_cpu_flush_done; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_rd_cmd_ready; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_rd_cmd_valid; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_rd_data_ready; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_rd_data_valid; // @[DandelionShell.scala 819:21]
  wire [63:0] cache_io_mem_rd_data_bits; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_wr_cmd_ready; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_wr_cmd_valid; // @[DandelionShell.scala 819:21]
  wire [31:0] cache_io_mem_wr_cmd_bits_addr; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_wr_data_ready; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_wr_data_valid; // @[DandelionShell.scala 819:21]
  wire [63:0] cache_io_mem_wr_data_bits; // @[DandelionShell.scala 819:21]
  wire  cache_io_mem_wr_ack; // @[DandelionShell.scala 819:21]
  wire  accel_clock; // @[DandelionShell.scala 822:21]
  wire  accel_reset; // @[DandelionShell.scala 822:21]
  wire  accel_io_in_ready; // @[DandelionShell.scala 822:21]
  wire  accel_io_in_valid; // @[DandelionShell.scala 822:21]
  wire [63:0] accel_io_in_bits_dataVals_field1_data; // @[DandelionShell.scala 822:21]
  wire [63:0] accel_io_in_bits_dataVals_field0_data; // @[DandelionShell.scala 822:21]
  wire  accel_io_out_ready; // @[DandelionShell.scala 822:21]
  wire  accel_io_out_valid; // @[DandelionShell.scala 822:21]
  wire [63:0] accel_io_out_bits_data_field0_data; // @[DandelionShell.scala 822:21]
  reg [1:0] state; // @[DandelionShell.scala 843:22]
  reg [31:0] cycles; // @[DandelionShell.scala 844:23]
  wire  _T = state == 2'h0; // @[DandelionShell.scala 849:14]
  wire  _T_1 = state != 2'h2; // @[DandelionShell.scala 851:20]
  wire [31:0] _T_3 = cycles + 32'h1; // @[DandelionShell.scala 852:22]
  reg [63:0] vals_0; // @[Reg.scala 27:20]
  reg [63:0] vals_1; // @[Reg.scala 27:20]
  wire  _T_9 = state == 2'h2; // @[DandelionShell.scala 900:31]
  reg  cache_done; // @[DandelionShell.scala 926:27]
  wire  _GEN_4 = cache_io_cpu_flush_done | cache_done; // @[DandelionShell.scala 928:35]
  wire  _T_11 = 2'h0 == state; // @[Conditional.scala 37:30]
  wire  _T_13 = ~reset; // @[DandelionShell.scala 936:15]
  wire  _T_22 = accel_io_in_ready & accel_io_in_valid; // @[Decoupled.scala 40:37]
  wire  _GEN_7 = dcr_io_dcr_launch; // @[DandelionShell.scala 935:31]
  wire  _T_23 = 2'h1 == state; // @[Conditional.scala 37:30]
  wire  _T_24 = accel_io_out_ready & accel_io_out_valid; // @[Decoupled.scala 40:37]
  wire  _T_25 = 2'h2 == state; // @[Conditional.scala 37:30]
  wire  _T_27 = 2'h3 == state; // @[Conditional.scala 37:30]
  wire  _GEN_16 = _T_11 & dcr_io_dcr_launch; // @[DandelionShell.scala 936:15]
  DCR dcr ( // @[DandelionShell.scala 817:19]
    .clock(dcr_clock),
    .reset(dcr_reset),
    .io_host_aw_ready(dcr_io_host_aw_ready),
    .io_host_aw_valid(dcr_io_host_aw_valid),
    .io_host_aw_bits_addr(dcr_io_host_aw_bits_addr),
    .io_host_w_ready(dcr_io_host_w_ready),
    .io_host_w_valid(dcr_io_host_w_valid),
    .io_host_w_bits_data(dcr_io_host_w_bits_data),
    .io_host_b_ready(dcr_io_host_b_ready),
    .io_host_b_valid(dcr_io_host_b_valid),
    .io_host_ar_ready(dcr_io_host_ar_ready),
    .io_host_ar_valid(dcr_io_host_ar_valid),
    .io_host_ar_bits_addr(dcr_io_host_ar_bits_addr),
    .io_host_r_ready(dcr_io_host_r_ready),
    .io_host_r_valid(dcr_io_host_r_valid),
    .io_host_r_bits_data(dcr_io_host_r_bits_data),
    .io_dcr_launch(dcr_io_dcr_launch),
    .io_dcr_finish(dcr_io_dcr_finish),
    .io_dcr_ecnt_0_valid(dcr_io_dcr_ecnt_0_valid),
    .io_dcr_ecnt_0_bits(dcr_io_dcr_ecnt_0_bits),
    .io_dcr_ecnt_1_valid(dcr_io_dcr_ecnt_1_valid),
    .io_dcr_ecnt_1_bits(dcr_io_dcr_ecnt_1_bits),
    .io_dcr_vals_0(dcr_io_dcr_vals_0),
    .io_dcr_vals_1(dcr_io_dcr_vals_1)
  );
  DME dmem ( // @[DandelionShell.scala 818:20]
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
    .io_mem_ar_bits_len(dmem_io_mem_ar_bits_len),
    .io_mem_r_ready(dmem_io_mem_r_ready),
    .io_mem_r_valid(dmem_io_mem_r_valid),
    .io_mem_r_bits_data(dmem_io_mem_r_bits_data),
    .io_mem_r_bits_last(dmem_io_mem_r_bits_last),
    .io_dme_rd_0_cmd_ready(dmem_io_dme_rd_0_cmd_ready),
    .io_dme_rd_0_cmd_valid(dmem_io_dme_rd_0_cmd_valid),
    .io_dme_rd_0_data_ready(dmem_io_dme_rd_0_data_ready),
    .io_dme_rd_0_data_valid(dmem_io_dme_rd_0_data_valid),
    .io_dme_rd_0_data_bits(dmem_io_dme_rd_0_data_bits),
    .io_dme_wr_0_cmd_ready(dmem_io_dme_wr_0_cmd_ready),
    .io_dme_wr_0_cmd_valid(dmem_io_dme_wr_0_cmd_valid),
    .io_dme_wr_0_cmd_bits_addr(dmem_io_dme_wr_0_cmd_bits_addr),
    .io_dme_wr_0_data_ready(dmem_io_dme_wr_0_data_ready),
    .io_dme_wr_0_data_valid(dmem_io_dme_wr_0_data_valid),
    .io_dme_wr_0_data_bits(dmem_io_dme_wr_0_data_bits),
    .io_dme_wr_0_ack(dmem_io_dme_wr_0_ack)
  );
  DMECache cache ( // @[DandelionShell.scala 819:21]
    .clock(cache_clock),
    .reset(cache_reset),
    .io_cpu_flush(cache_io_cpu_flush),
    .io_cpu_flush_done(cache_io_cpu_flush_done),
    .io_mem_rd_cmd_ready(cache_io_mem_rd_cmd_ready),
    .io_mem_rd_cmd_valid(cache_io_mem_rd_cmd_valid),
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
  test01RootDF accel ( // @[DandelionShell.scala 822:21]
    .clock(accel_clock),
    .reset(accel_reset),
    .io_in_ready(accel_io_in_ready),
    .io_in_valid(accel_io_in_valid),
    .io_in_bits_dataVals_field1_data(accel_io_in_bits_dataVals_field1_data),
    .io_in_bits_dataVals_field0_data(accel_io_in_bits_dataVals_field0_data),
    .io_out_ready(accel_io_out_ready),
    .io_out_valid(accel_io_out_valid),
    .io_out_bits_data_field0_data(accel_io_out_bits_data_field0_data)
  );
  assign io_host_aw_ready = dcr_io_host_aw_ready; // @[DandelionShell.scala 970:11]
  assign io_host_w_ready = dcr_io_host_w_ready; // @[DandelionShell.scala 970:11]
  assign io_host_b_valid = dcr_io_host_b_valid; // @[DandelionShell.scala 970:11]
  assign io_host_b_bits_resp = 2'h0; // @[DandelionShell.scala 970:11]
  assign io_host_b_bits_id = io_host_w_bits_id; // @[DandelionShell.scala 973:21]
  assign io_host_b_bits_user = 10'h0;
  assign io_host_ar_ready = dcr_io_host_ar_ready; // @[DandelionShell.scala 970:11]
  assign io_host_r_valid = dcr_io_host_r_valid; // @[DandelionShell.scala 970:11]
  assign io_host_r_bits_data = dcr_io_host_r_bits_data; // @[DandelionShell.scala 970:11]
  assign io_host_r_bits_resp = 2'h0; // @[DandelionShell.scala 970:11]
  assign io_host_r_bits_last = 1'h1; // @[DandelionShell.scala 978:23]
  assign io_host_r_bits_id = io_host_ar_bits_id; // @[DandelionShell.scala 974:21]
  assign io_host_r_bits_user = 10'h0;
  assign io_mem_aw_valid = dmem_io_mem_aw_valid; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_addr = dmem_io_mem_aw_bits_addr; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_id = 16'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_user = 5'h1; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_len = dmem_io_mem_aw_bits_len; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_size = 3'h3; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_burst = 2'h1; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_lock = 2'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_cache = 4'hf; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_prot = 3'h4; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_qos = 4'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_aw_bits_region = 4'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_w_valid = dmem_io_mem_w_valid; // @[DandelionShell.scala 969:10]
  assign io_mem_w_bits_data = dmem_io_mem_w_bits_data; // @[DandelionShell.scala 969:10]
  assign io_mem_w_bits_strb = 8'hff; // @[DandelionShell.scala 969:10]
  assign io_mem_w_bits_last = dmem_io_mem_w_bits_last; // @[DandelionShell.scala 969:10]
  assign io_mem_w_bits_id = 16'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_w_bits_user = 5'h1; // @[DandelionShell.scala 969:10]
  assign io_mem_b_ready = dmem_io_mem_b_ready; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_valid = dmem_io_mem_ar_valid; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_addr = dmem_io_mem_ar_bits_addr; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_id = 16'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_user = 5'h1; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_len = dmem_io_mem_ar_bits_len; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_size = 3'h3; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_burst = 2'h1; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_lock = 2'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_cache = 4'hf; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_prot = 3'h4; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_qos = 4'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_ar_bits_region = 4'h0; // @[DandelionShell.scala 969:10]
  assign io_mem_r_ready = dmem_io_mem_r_ready; // @[DandelionShell.scala 969:10]
  assign dcr_clock = clock;
  assign dcr_reset = reset;
  assign dcr_io_host_aw_valid = io_host_aw_valid; // @[DandelionShell.scala 970:11]
  assign dcr_io_host_aw_bits_addr = io_host_aw_bits_addr; // @[DandelionShell.scala 970:11]
  assign dcr_io_host_w_valid = io_host_w_valid; // @[DandelionShell.scala 970:11]
  assign dcr_io_host_w_bits_data = io_host_w_bits_data; // @[DandelionShell.scala 970:11]
  assign dcr_io_host_b_ready = io_host_b_ready; // @[DandelionShell.scala 970:11]
  assign dcr_io_host_ar_valid = io_host_ar_valid; // @[DandelionShell.scala 970:11]
  assign dcr_io_host_ar_bits_addr = io_host_ar_bits_addr; // @[DandelionShell.scala 970:11]
  assign dcr_io_host_r_ready = io_host_r_ready; // @[DandelionShell.scala 970:11]
  assign dcr_io_dcr_finish = state == 2'h3; // @[DandelionShell.scala 967:21]
  assign dcr_io_dcr_ecnt_0_valid = state == 2'h3; // @[DandelionShell.scala 859:28]
  assign dcr_io_dcr_ecnt_0_bits = cycles; // @[DandelionShell.scala 860:27]
  assign dcr_io_dcr_ecnt_1_valid = accel_io_out_valid; // @[DandelionShell.scala 864:30]
  assign dcr_io_dcr_ecnt_1_bits = accel_io_out_bits_data_field0_data[31:0]; // @[DandelionShell.scala 863:29]
  assign dmem_clock = clock;
  assign dmem_reset = reset;
  assign dmem_io_mem_aw_ready = io_mem_aw_ready; // @[DandelionShell.scala 969:10]
  assign dmem_io_mem_w_ready = io_mem_w_ready; // @[DandelionShell.scala 969:10]
  assign dmem_io_mem_b_valid = io_mem_b_valid; // @[DandelionShell.scala 969:10]
  assign dmem_io_mem_ar_ready = io_mem_ar_ready; // @[DandelionShell.scala 969:10]
  assign dmem_io_mem_r_valid = io_mem_r_valid; // @[DandelionShell.scala 969:10]
  assign dmem_io_mem_r_bits_data = io_mem_r_bits_data; // @[DandelionShell.scala 969:10]
  assign dmem_io_mem_r_bits_last = io_mem_r_bits_last; // @[DandelionShell.scala 969:10]
  assign dmem_io_dme_rd_0_cmd_valid = cache_io_mem_rd_cmd_valid; // @[DandelionShell.scala 833:21]
  assign dmem_io_dme_rd_0_data_ready = cache_io_mem_rd_data_ready; // @[DandelionShell.scala 833:21]
  assign dmem_io_dme_wr_0_cmd_valid = cache_io_mem_wr_cmd_valid; // @[DandelionShell.scala 834:21]
  assign dmem_io_dme_wr_0_cmd_bits_addr = cache_io_mem_wr_cmd_bits_addr; // @[DandelionShell.scala 834:21]
  assign dmem_io_dme_wr_0_data_valid = cache_io_mem_wr_data_valid; // @[DandelionShell.scala 834:21]
  assign dmem_io_dme_wr_0_data_bits = cache_io_mem_wr_data_bits; // @[DandelionShell.scala 834:21]
  assign cache_clock = clock;
  assign cache_reset = reset;
  assign cache_io_cpu_flush = state == 2'h2; // @[DandelionShell.scala 900:22]
  assign cache_io_mem_rd_cmd_ready = dmem_io_dme_rd_0_cmd_ready; // @[DandelionShell.scala 833:21]
  assign cache_io_mem_rd_data_valid = dmem_io_dme_rd_0_data_valid; // @[DandelionShell.scala 833:21]
  assign cache_io_mem_rd_data_bits = dmem_io_dme_rd_0_data_bits; // @[DandelionShell.scala 833:21]
  assign cache_io_mem_wr_cmd_ready = dmem_io_dme_wr_0_cmd_ready; // @[DandelionShell.scala 834:21]
  assign cache_io_mem_wr_data_ready = dmem_io_dme_wr_0_data_ready; // @[DandelionShell.scala 834:21]
  assign cache_io_mem_wr_ack = dmem_io_dme_wr_0_ack; // @[DandelionShell.scala 834:21]
  assign accel_clock = clock;
  assign accel_reset = reset;
  assign accel_io_in_valid = _T_11 & _GEN_7; // @[DandelionShell.scala 897:21 DandelionShell.scala 945:27]
  assign accel_io_in_bits_dataVals_field1_data = vals_1; // @[DandelionShell.scala 891:45]
  assign accel_io_in_bits_dataVals_field0_data = vals_0; // @[DandelionShell.scala 891:45]
  assign accel_io_out_ready = state == 2'h1; // @[DandelionShell.scala 898:22]
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
  vals_0 = _RAND_2[63:0];
  _RAND_3 = {2{`RANDOM}};
  vals_1 = _RAND_3[63:0];
  _RAND_4 = {1{`RANDOM}};
  cache_done = _RAND_4[0:0];
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
    end else if (_T_11) begin
      if (dcr_io_dcr_launch) begin
        if (_T_22) begin
          state <= 2'h1;
        end
      end
    end else if (_T_23) begin
      if (_T_24) begin
        state <= 2'h2;
      end
    end else if (_T_25) begin
      if (cache_done) begin
        state <= 2'h3;
      end
    end else if (_T_27) begin
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
      cache_done <= 1'h0;
    end else if (_T_9) begin
      cache_done <= _GEN_4;
    end
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_16 & _T_13) begin
          $fwrite(32'h80000002,"Ptrs: "); // @[DandelionShell.scala 936:15]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_16 & _T_13) begin
          $fwrite(32'h80000002,"\nVals: "); // @[DandelionShell.scala 938:15]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_16 & _T_13) begin
          $fwrite(32'h80000002,"val(0): 0x%x, ",vals_0); // @[DandelionShell.scala 940:48]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_16 & _T_13) begin
          $fwrite(32'h80000002,"val(1): 0x%x, ",vals_1); // @[DandelionShell.scala 940:48]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
    `ifndef SYNTHESIS
    `ifdef PRINTF_COND
      if (`PRINTF_COND) begin
    `endif
        if (_GEN_16 & _T_13) begin
          $fwrite(32'h80000002,"\n"); // @[DandelionShell.scala 944:15]
        end
    `ifdef PRINTF_COND
      end
    `endif
    `endif // SYNTHESIS
  end
endmodule

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

module RFile(
  input         clock,
  input  [1:0]  io_raddr1,
  output [15:0] io_rdata1,
  input  [1:0]  io_raddr2,
  input         io_wen,
  input  [1:0]  io_waddr,
  input  [15:0] io_wdata,
  input  [1:0]  io_wmask
);
  reg [7:0] regs_0 [0:3];
  reg [31:0] _RAND_0;
  wire [7:0] regs_0__T_20_data;
  wire [1:0] regs_0__T_20_addr;
  wire [7:0] regs_0__T_33_data;
  wire [1:0] regs_0__T_33_addr;
  wire [7:0] regs_0__T_58_data;
  wire [1:0] regs_0__T_58_addr;
  wire  regs_0__T_58_mask;
  wire  regs_0__T_58_en;
  reg [1:0] regs_0__T_20_addr_pipe_0;
  reg [31:0] _RAND_1;
  reg [1:0] regs_0__T_33_addr_pipe_0;
  reg [31:0] _RAND_2;
  reg [7:0] regs_1 [0:3];
  reg [31:0] _RAND_3;
  wire [7:0] regs_1__T_20_data;
  wire [1:0] regs_1__T_20_addr;
  wire [7:0] regs_1__T_33_data;
  wire [1:0] regs_1__T_33_addr;
  wire [7:0] regs_1__T_58_data;
  wire [1:0] regs_1__T_58_addr;
  wire  regs_1__T_58_mask;
  wire  regs_1__T_58_en;
  reg [1:0] regs_1__T_20_addr_pipe_0;
  reg [31:0] _RAND_4;
  reg [1:0] regs_1__T_33_addr_pipe_0;
  reg [31:0] _RAND_5;
  wire  _T_19;
  wire [15:0] _T_28;
  wire [15:0] _T_30;
  wire  _T_45;
  wire  _T_46;
  wire [7:0] _T_47;
  wire [7:0] _T_48;
  wire [7:0] _T_51_0;
  wire [7:0] _T_51_1;
  wire  _T_56;
  wire  _T_57;
  wire  _GEN_8;
  wire  _GEN_10;
  assign io_rdata1 = _T_30;
  assign regs_0__T_20_addr = regs_0__T_20_addr_pipe_0;
  assign regs_0__T_20_data = regs_0[regs_0__T_20_addr];
  assign regs_0__T_33_addr = regs_0__T_33_addr_pipe_0;
  assign regs_0__T_33_data = regs_0[regs_0__T_33_addr];
  assign regs_0__T_58_data = _T_51_0;
  assign regs_0__T_58_addr = io_waddr;
  assign regs_0__T_58_mask = _GEN_8;
  assign regs_0__T_58_en = _T_46;
  assign regs_1__T_20_addr = regs_1__T_20_addr_pipe_0;
  assign regs_1__T_20_data = regs_1[regs_1__T_20_addr];
  assign regs_1__T_33_addr = regs_1__T_33_addr_pipe_0;
  assign regs_1__T_33_data = regs_1[regs_1__T_33_addr];
  assign regs_1__T_58_data = _T_51_1;
  assign regs_1__T_58_addr = io_waddr;
  assign regs_1__T_58_mask = _GEN_10;
  assign regs_1__T_58_en = _T_46;
  assign _T_19 = io_raddr1 != 2'h0;
  assign _T_28 = {regs_1__T_20_data,regs_0__T_20_data};
  assign _T_30 = _T_19 ? _T_28 : 16'h0;
  assign _T_45 = io_waddr != 2'h0;
  assign _T_46 = io_wen & _T_45;
  assign _T_47 = io_wdata[7:0];
  assign _T_48 = io_wdata[15:8];
  assign _T_51_0 = _T_47;
  assign _T_51_1 = _T_48;
  assign _T_56 = io_wmask[0];
  assign _T_57 = io_wmask[1];
  assign _GEN_8 = _T_46 ? _T_56 : 1'h0;
  assign _GEN_10 = _T_46 ? _T_57 : 1'h0;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  _RAND_0 = {1{$random}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 4; initvar = initvar+1)
    regs_0[initvar] = _RAND_0[7:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  regs_0__T_20_addr_pipe_0 = _RAND_1[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  regs_0__T_33_addr_pipe_0 = _RAND_2[1:0];
  `endif // RANDOMIZE_REG_INIT
  _RAND_3 = {1{$random}};
  `ifdef RANDOMIZE_MEM_INIT
  for (initvar = 0; initvar < 4; initvar = initvar+1)
    regs_1[initvar] = _RAND_3[7:0];
  `endif // RANDOMIZE_MEM_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{$random}};
  regs_1__T_20_addr_pipe_0 = _RAND_4[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{$random}};
  regs_1__T_33_addr_pipe_0 = _RAND_5[1:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if(regs_0__T_58_en & regs_0__T_58_mask) begin
      regs_0[regs_0__T_58_addr] <= regs_0__T_58_data;
    end
    regs_0__T_20_addr_pipe_0 <= io_raddr1;
    regs_0__T_33_addr_pipe_0 <= io_raddr2;
    if(regs_1__T_58_en & regs_1__T_58_mask) begin
      regs_1[regs_1__T_58_addr] <= regs_1__T_58_data;
    end
    regs_1__T_20_addr_pipe_0 <= io_raddr1;
    regs_1__T_33_addr_pipe_0 <= io_raddr2;
  end
endmodule
module LockingRRArbiter(
  input         clock,
  input         reset,
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [15:0] io_in_0_bits_RouteID,
  input  [5:0]  io_in_0_bits_address,
  input  [15:0] io_in_0_bits_data,
  input  [1:0]  io_in_0_bits_mask,
  input         io_in_1_valid,
  input  [15:0] io_in_1_bits_RouteID,
  input  [5:0]  io_in_1_bits_address,
  input  [15:0] io_in_1_bits_data,
  input  [1:0]  io_in_1_bits_mask,
  input         io_out_ready,
  output        io_out_valid,
  output [15:0] io_out_bits_RouteID,
  output [5:0]  io_out_bits_address,
  output [15:0] io_out_bits_data,
  output [1:0]  io_out_bits_mask,
  output        io_chosen
);
  wire  choice;
  wire  _GEN_0_valid;
  wire  _GEN_8;
  wire [15:0] _GEN_9;
  wire [5:0] _GEN_10;
  wire [15:0] _GEN_11;
  wire [1:0] _GEN_12;
  wire [1:0] _GEN_3_bits_mask;
  wire [15:0] _GEN_4_bits_data;
  wire [5:0] _GEN_5_bits_address;
  wire [15:0] _GEN_6_bits_RouteID;
  reg  value;
  reg [31:0] _RAND_0;
  reg  _T_62;
  reg [31:0] _RAND_1;
  wire  _T_66;
  wire [1:0] _T_71;
  wire  _T_72;
  wire  _GEN_15;
  wire  _GEN_16;
  wire  _GEN_17;
  reg  lastGrant;
  reg [31:0] _RAND_2;
  wire  _GEN_18;
  wire  grantMask_1;
  wire  validMask_1;
  wire  _T_83;
  wire  _T_91;
  wire  _T_92;
  wire  _T_93;
  wire  _GEN_19;
  wire  _GEN_20;
  assign io_in_0_ready = _T_93;
  assign io_out_valid = _GEN_0_valid;
  assign io_out_bits_RouteID = _GEN_6_bits_RouteID;
  assign io_out_bits_address = _GEN_5_bits_address;
  assign io_out_bits_data = _GEN_4_bits_data;
  assign io_out_bits_mask = _GEN_3_bits_mask;
  assign io_chosen = _GEN_17;
  assign choice = _GEN_20;
  assign _GEN_0_valid = _GEN_8;
  assign _GEN_8 = io_chosen ? io_in_1_valid : io_in_0_valid;
  assign _GEN_9 = io_chosen ? io_in_1_bits_RouteID : io_in_0_bits_RouteID;
  assign _GEN_10 = io_chosen ? io_in_1_bits_address : io_in_0_bits_address;
  assign _GEN_11 = io_chosen ? io_in_1_bits_data : io_in_0_bits_data;
  assign _GEN_12 = io_chosen ? io_in_1_bits_mask : io_in_0_bits_mask;
  assign _GEN_3_bits_mask = _GEN_12;
  assign _GEN_4_bits_data = _GEN_11;
  assign _GEN_5_bits_address = _GEN_10;
  assign _GEN_6_bits_RouteID = _GEN_9;
  assign _T_66 = io_out_ready & io_out_valid;
  assign _T_71 = value + 1'h1;
  assign _T_72 = _T_71[0:0];
  assign _GEN_15 = _T_66 ? io_chosen : _T_62;
  assign _GEN_16 = _T_66 ? _T_72 : value;
  assign _GEN_17 = value ? _T_62 : choice;
  assign _GEN_18 = _T_66 ? io_chosen : lastGrant;
  assign grantMask_1 = 1'h1 > lastGrant;
  assign validMask_1 = io_in_1_valid & grantMask_1;
  assign _T_83 = validMask_1 == 1'h0;
  assign _T_91 = _T_62 == 1'h0;
  assign _T_92 = value ? _T_91 : _T_83;
  assign _T_93 = _T_92 & io_out_ready;
  assign _GEN_19 = io_in_0_valid ? 1'h0 : 1'h1;
  assign _GEN_20 = validMask_1 ? 1'h1 : _GEN_19;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  value = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  _T_62 = _RAND_1[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  lastGrant = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      value <= 1'h0;
    end else begin
      if (_T_66) begin
        value <= _T_72;
      end
    end
    if (_T_66) begin
      _T_62 <= io_chosen;
    end
    if (_T_66) begin
      lastGrant <= io_chosen;
    end
  end
endmodule
module ArbiterTree(
  input         clock,
  input         reset,
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [15:0] io_in_0_bits_RouteID,
  input  [5:0]  io_in_0_bits_address,
  input  [15:0] io_in_0_bits_data,
  input  [1:0]  io_in_0_bits_mask,
  input         io_out_ready,
  output        io_out_valid,
  output [15:0] io_out_bits_RouteID,
  output [5:0]  io_out_bits_address,
  output [15:0] io_out_bits_data,
  output [1:0]  io_out_bits_mask
);
  wire  LockingRRArbiter_clock;
  wire  LockingRRArbiter_reset;
  wire  LockingRRArbiter_io_in_0_ready;
  wire  LockingRRArbiter_io_in_0_valid;
  wire [15:0] LockingRRArbiter_io_in_0_bits_RouteID;
  wire [5:0] LockingRRArbiter_io_in_0_bits_address;
  wire [15:0] LockingRRArbiter_io_in_0_bits_data;
  wire [1:0] LockingRRArbiter_io_in_0_bits_mask;
  wire  LockingRRArbiter_io_in_1_valid;
  wire [15:0] LockingRRArbiter_io_in_1_bits_RouteID;
  wire [5:0] LockingRRArbiter_io_in_1_bits_address;
  wire [15:0] LockingRRArbiter_io_in_1_bits_data;
  wire [1:0] LockingRRArbiter_io_in_1_bits_mask;
  wire  LockingRRArbiter_io_out_ready;
  wire  LockingRRArbiter_io_out_valid;
  wire [15:0] LockingRRArbiter_io_out_bits_RouteID;
  wire [5:0] LockingRRArbiter_io_out_bits_address;
  wire [15:0] LockingRRArbiter_io_out_bits_data;
  wire [1:0] LockingRRArbiter_io_out_bits_mask;
  wire  LockingRRArbiter_io_chosen;
  LockingRRArbiter LockingRRArbiter (
    .clock(LockingRRArbiter_clock),
    .reset(LockingRRArbiter_reset),
    .io_in_0_ready(LockingRRArbiter_io_in_0_ready),
    .io_in_0_valid(LockingRRArbiter_io_in_0_valid),
    .io_in_0_bits_RouteID(LockingRRArbiter_io_in_0_bits_RouteID),
    .io_in_0_bits_address(LockingRRArbiter_io_in_0_bits_address),
    .io_in_0_bits_data(LockingRRArbiter_io_in_0_bits_data),
    .io_in_0_bits_mask(LockingRRArbiter_io_in_0_bits_mask),
    .io_in_1_valid(LockingRRArbiter_io_in_1_valid),
    .io_in_1_bits_RouteID(LockingRRArbiter_io_in_1_bits_RouteID),
    .io_in_1_bits_address(LockingRRArbiter_io_in_1_bits_address),
    .io_in_1_bits_data(LockingRRArbiter_io_in_1_bits_data),
    .io_in_1_bits_mask(LockingRRArbiter_io_in_1_bits_mask),
    .io_out_ready(LockingRRArbiter_io_out_ready),
    .io_out_valid(LockingRRArbiter_io_out_valid),
    .io_out_bits_RouteID(LockingRRArbiter_io_out_bits_RouteID),
    .io_out_bits_address(LockingRRArbiter_io_out_bits_address),
    .io_out_bits_data(LockingRRArbiter_io_out_bits_data),
    .io_out_bits_mask(LockingRRArbiter_io_out_bits_mask),
    .io_chosen(LockingRRArbiter_io_chosen)
  );
  assign io_in_0_ready = LockingRRArbiter_io_in_0_ready;
  assign io_out_valid = LockingRRArbiter_io_out_valid;
  assign io_out_bits_RouteID = LockingRRArbiter_io_out_bits_RouteID;
  assign io_out_bits_address = LockingRRArbiter_io_out_bits_address;
  assign io_out_bits_data = LockingRRArbiter_io_out_bits_data;
  assign io_out_bits_mask = LockingRRArbiter_io_out_bits_mask;
  assign LockingRRArbiter_clock = clock;
  assign LockingRRArbiter_reset = reset;
  assign LockingRRArbiter_io_in_0_valid = io_in_0_valid;
  assign LockingRRArbiter_io_in_0_bits_RouteID = io_in_0_bits_RouteID;
  assign LockingRRArbiter_io_in_0_bits_address = io_in_0_bits_address;
  assign LockingRRArbiter_io_in_0_bits_data = io_in_0_bits_data;
  assign LockingRRArbiter_io_in_0_bits_mask = io_in_0_bits_mask;
  assign LockingRRArbiter_io_in_1_valid = 1'h0;
  assign LockingRRArbiter_io_in_1_bits_RouteID = 16'h0;
  assign LockingRRArbiter_io_in_1_bits_address = 6'h0;
  assign LockingRRArbiter_io_in_1_bits_data = 16'h0;
  assign LockingRRArbiter_io_in_1_bits_mask = 2'h0;
  assign LockingRRArbiter_io_out_ready = io_out_ready;
endmodule
module LockingRRArbiter_1(
  output  io_in_0_ready,
  input   io_in_0_valid,
  input   io_out_ready,
  output  io_out_valid
);
  assign io_in_0_ready = io_out_ready;
  assign io_out_valid = io_in_0_valid;
endmodule
module RRArbiter(
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [15:0] io_in_0_bits_addr,
  input  [15:0] io_in_0_bits_data,
  input  [1:0]  io_in_0_bits_mask,
  input         io_out_ready,
  output        io_out_valid,
  output [15:0] io_out_bits_addr,
  output [15:0] io_out_bits_data,
  output [1:0]  io_out_bits_mask
);
  assign io_in_0_ready = io_out_ready;
  assign io_out_valid = io_in_0_valid;
  assign io_out_bits_addr = io_in_0_bits_addr;
  assign io_out_bits_data = io_in_0_bits_data;
  assign io_out_bits_mask = io_in_0_bits_mask;
endmodule
module Demux(
  input   io_en,
  output  io_outputs_0_valid
);
  wire  _T_16;
  wire  _GEN_3;
  assign io_outputs_0_valid = _GEN_3;
  assign _T_16 = io_en == 1'h0;
  assign _GEN_3 = _T_16 ? 1'h0 : 1'h1;
endmodule
module RRArbiter_1(
  output        io_in_0_ready,
  input         io_in_0_valid,
  input         io_in_0_bits_valid,
  input  [15:0] io_in_0_bits_RouteID,
  input         io_in_0_bits_done,
  input         io_out_ready,
  output        io_out_valid,
  output        io_out_bits_valid,
  output [15:0] io_out_bits_RouteID,
  output        io_out_bits_done
);
  assign io_in_0_ready = io_out_ready;
  assign io_out_valid = io_in_0_valid;
  assign io_out_bits_valid = io_in_0_bits_valid;
  assign io_out_bits_RouteID = io_in_0_bits_RouteID;
  assign io_out_bits_done = io_in_0_bits_done;
endmodule
module Demux_1(
  input         io_en,
  input         io_input_valid,
  input  [15:0] io_input_RouteID,
  input         io_input_done,
  input         io_sel,
  output        io_outputs_0_valid,
  output [15:0] io_outputs_0_RouteID,
  output        io_outputs_0_done
);
  wire  _GEN_0;
  wire [15:0] _GEN_1;
  wire  _GEN_2;
  wire  _GEN_8;
  wire  _GEN_3;
  wire  _GEN_10;
  wire  _T_16;
  wire  _GEN_18;
  assign io_outputs_0_valid = _GEN_18;
  assign io_outputs_0_RouteID = _GEN_1;
  assign io_outputs_0_done = _GEN_0;
  assign _GEN_0 = io_input_done;
  assign _GEN_1 = io_input_RouteID;
  assign _GEN_2 = io_input_valid;
  assign _GEN_8 = 1'h0 == io_sel ? _GEN_2 : 1'h0;
  assign _GEN_3 = 1'h1;
  assign _GEN_10 = 1'h0 == io_sel ? _GEN_3 : _GEN_8;
  assign _T_16 = io_en == 1'h0;
  assign _GEN_18 = _T_16 ? 1'h0 : _GEN_10;
endmodule
module DeMuxTree(
  output        io_outputs_0_valid,
  output [15:0] io_outputs_0_RouteID,
  output        io_outputs_0_done,
  input         io_input_valid,
  input  [15:0] io_input_RouteID,
  input         io_input_done,
  input         io_enable
);
  wire  Demux_io_en;
  wire  Demux_io_input_valid;
  wire [15:0] Demux_io_input_RouteID;
  wire  Demux_io_input_done;
  wire  Demux_io_sel;
  wire  Demux_io_outputs_0_valid;
  wire [15:0] Demux_io_outputs_0_RouteID;
  wire  Demux_io_outputs_0_done;
  wire  _T_6;
  Demux_1 Demux (
    .io_en(Demux_io_en),
    .io_input_valid(Demux_io_input_valid),
    .io_input_RouteID(Demux_io_input_RouteID),
    .io_input_done(Demux_io_input_done),
    .io_sel(Demux_io_sel),
    .io_outputs_0_valid(Demux_io_outputs_0_valid),
    .io_outputs_0_RouteID(Demux_io_outputs_0_RouteID),
    .io_outputs_0_done(Demux_io_outputs_0_done)
  );
  assign io_outputs_0_valid = Demux_io_outputs_0_valid;
  assign io_outputs_0_RouteID = Demux_io_outputs_0_RouteID;
  assign io_outputs_0_done = Demux_io_outputs_0_done;
  assign Demux_io_en = io_enable;
  assign Demux_io_input_valid = io_input_valid;
  assign Demux_io_input_RouteID = io_input_RouteID;
  assign Demux_io_input_done = io_input_done;
  assign Demux_io_sel = _T_6;
  assign _T_6 = io_input_RouteID[0];
endmodule
module WriteTypTableEntry(
  input         clock,
  input         reset,
  output        io_NodeReq_ready,
  input         io_NodeReq_valid,
  input  [15:0] io_NodeReq_bits_RouteID,
  input  [5:0]  io_NodeReq_bits_address,
  input  [15:0] io_NodeReq_bits_data,
  input  [1:0]  io_NodeReq_bits_mask,
  input         io_MemReq_ready,
  output        io_MemReq_valid,
  output [15:0] io_MemReq_bits_addr,
  output [15:0] io_MemReq_bits_data,
  output [1:0]  io_MemReq_bits_mask,
  input         io_MemResp_valid,
  input         io_output_ready,
  output        io_output_valid,
  output        io_output_bits_valid,
  output [15:0] io_output_bits_RouteID,
  output        io_output_bits_done,
  output        io_free
);
  wire [15:0] _T_29_RouteID;
  reg [15:0] request_R_RouteID;
  reg [31:0] _RAND_0;
  reg [15:0] ReqAddress;
  reg [31:0] _RAND_1;
  reg [20:0] inptr;
  reg [31:0] _RAND_2;
  reg [1:0] sendptr;
  reg [31:0] _RAND_3;
  reg [1:0] recvptr;
  reg [31:0] _RAND_4;
  wire [15:0] _T_62_0;
  wire [15:0] _T_62_1;
  reg [15:0] linebuffer_0;
  reg [31:0] _RAND_5;
  reg [15:0] linebuffer_1;
  reg [31:0] _RAND_6;
  wire [1:0] _T_89_0;
  wire [1:0] _T_89_1;
  reg [1:0] linemask_0;
  reg [31:0] _RAND_7;
  reg [1:0] linemask_1;
  reg [31:0] _RAND_8;
  reg [1:0] state;
  reg [31:0] _RAND_9;
  wire  _T_114;
  wire  _T_117;
  wire  _T_120;
  wire  _T_123;
  wire [4:0] _T_124;
  wire [5:0] _GEN_45;
  wire [5:0] _T_125;
  wire  _T_129;
  wire [15:0] _GEN_0;
  wire [15:0] _GEN_4;
  wire [15:0] _GEN_5;
  wire [1:0] _GEN_1;
  wire [1:0] _GEN_6;
  wire [1:0] _GEN_7;
  wire [21:0] _T_135;
  wire [20:0] _T_136;
  wire [15:0] _GEN_13;
  wire [15:0] _GEN_14;
  wire [15:0] _GEN_15;
  wire [15:0] _GEN_16;
  wire [1:0] _GEN_17;
  wire [1:0] _GEN_18;
  wire [20:0] _GEN_19;
  wire [20:0] _GEN_46;
  wire  _T_137;
  wire [2:0] _T_139;
  wire [15:0] _GEN_47;
  wire [16:0] _T_140;
  wire [15:0] _T_141;
  wire  _T_145;
  wire [15:0] _GEN_2;
  wire [15:0] _GEN_20;
  wire [1:0] _GEN_3;
  wire [1:0] _GEN_21;
  wire  _T_152;
  wire [2:0] _T_154;
  wire [1:0] _T_155;
  wire [1:0] _GEN_22;
  wire [1:0] _GEN_29;
  wire [2:0] _T_159;
  wire [1:0] _T_160;
  wire  _T_162;
  wire [1:0] _T_163;
  wire [1:0] _GEN_30;
  wire [1:0] _GEN_31;
  wire  _T_167;
  wire [1:0] _GEN_32;
  wire [20:0] _GEN_33;
  wire [1:0] _GEN_34;
  wire [1:0] _GEN_35;
  wire [1:0] _GEN_40;
  wire [20:0] _GEN_41;
  wire [1:0] _GEN_42;
  wire [1:0] _GEN_43;
  assign io_NodeReq_ready = _T_114;
  assign io_MemReq_valid = _T_137;
  assign io_MemReq_bits_addr = _T_141;
  assign io_MemReq_bits_data = _GEN_2;
  assign io_MemReq_bits_mask = _GEN_3;
  assign io_output_valid = _T_117;
  assign io_output_bits_valid = 1'h0;
  assign io_output_bits_RouteID = request_R_RouteID;
  assign io_output_bits_done = 1'h1;
  assign io_free = _T_114;
  assign _T_29_RouteID = 16'h0;
  assign _T_62_0 = 16'h0;
  assign _T_62_1 = 16'h0;
  assign _T_89_0 = 2'h0;
  assign _T_89_1 = 2'h0;
  assign _T_114 = inptr != 21'h2;
  assign _T_117 = state == 2'h3;
  assign _T_120 = io_NodeReq_ready & io_NodeReq_valid;
  assign _T_123 = _T_120 & _T_114;
  assign _T_124 = io_NodeReq_bits_address[5:1];
  assign _GEN_45 = {{1'd0}, _T_124};
  assign _T_125 = _GEN_45 << 1;
  assign _T_129 = inptr[0];
  assign _GEN_0 = io_NodeReq_bits_data;
  assign _GEN_4 = 1'h0 == _T_129 ? _GEN_0 : linebuffer_0;
  assign _GEN_5 = _T_129 ? _GEN_0 : linebuffer_1;
  assign _GEN_1 = io_NodeReq_bits_mask;
  assign _GEN_6 = 1'h0 == _T_129 ? _GEN_1 : linemask_0;
  assign _GEN_7 = _T_129 ? _GEN_1 : linemask_1;
  assign _T_135 = inptr + 21'h1;
  assign _T_136 = _T_135[20:0];
  assign _GEN_13 = _T_123 ? io_NodeReq_bits_RouteID : request_R_RouteID;
  assign _GEN_14 = _T_123 ? {{10'd0}, _T_125} : ReqAddress;
  assign _GEN_15 = _T_123 ? _GEN_4 : linebuffer_0;
  assign _GEN_16 = _T_123 ? _GEN_5 : linebuffer_1;
  assign _GEN_17 = _T_123 ? _GEN_6 : linemask_0;
  assign _GEN_18 = _T_123 ? _GEN_7 : linemask_1;
  assign _GEN_19 = _T_123 ? _T_136 : inptr;
  assign _GEN_46 = {{19'd0}, sendptr};
  assign _T_137 = _GEN_46 != inptr;
  assign _T_139 = {sendptr,1'h0};
  assign _GEN_47 = {{13'd0}, _T_139};
  assign _T_140 = ReqAddress + _GEN_47;
  assign _T_141 = _T_140[15:0];
  assign _T_145 = sendptr[0];
  assign _GEN_2 = _GEN_20;
  assign _GEN_20 = _T_145 ? linebuffer_1 : linebuffer_0;
  assign _GEN_3 = _GEN_21;
  assign _GEN_21 = _T_145 ? linemask_1 : linemask_0;
  assign _T_152 = io_MemReq_ready & io_MemReq_valid;
  assign _T_154 = sendptr + 2'h1;
  assign _T_155 = _T_154[1:0];
  assign _GEN_22 = _T_152 ? _T_155 : sendptr;
  assign _GEN_29 = _T_137 ? _GEN_22 : sendptr;
  assign _T_159 = recvptr + 2'h1;
  assign _T_160 = _T_159[1:0];
  assign _T_162 = recvptr == 2'h1;
  assign _T_163 = _T_162 ? 2'h3 : 2'h1;
  assign _GEN_30 = io_MemResp_valid ? _T_160 : recvptr;
  assign _GEN_31 = io_MemResp_valid ? _T_163 : state;
  assign _T_167 = io_output_ready & io_output_valid;
  assign _GEN_32 = _T_167 ? 2'h0 : _GEN_31;
  assign _GEN_33 = _T_167 ? 21'h0 : _GEN_19;
  assign _GEN_34 = _T_167 ? 2'h0 : _GEN_29;
  assign _GEN_35 = _T_167 ? 2'h0 : _GEN_30;
  assign _GEN_40 = _T_117 ? _GEN_32 : _GEN_31;
  assign _GEN_41 = _T_117 ? _GEN_33 : _GEN_19;
  assign _GEN_42 = _T_117 ? _GEN_34 : _GEN_29;
  assign _GEN_43 = _T_117 ? _GEN_35 : _GEN_30;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  request_R_RouteID = _RAND_0[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  ReqAddress = _RAND_1[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  inptr = _RAND_2[20:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{$random}};
  sendptr = _RAND_3[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{$random}};
  recvptr = _RAND_4[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{$random}};
  linebuffer_0 = _RAND_5[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{$random}};
  linebuffer_1 = _RAND_6[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{$random}};
  linemask_0 = _RAND_7[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{$random}};
  linemask_1 = _RAND_8[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{$random}};
  state = _RAND_9[1:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      request_R_RouteID <= _T_29_RouteID;
    end else begin
      if (_T_123) begin
        request_R_RouteID <= io_NodeReq_bits_RouteID;
      end
    end
    if (reset) begin
      ReqAddress <= 16'h0;
    end else begin
      if (_T_123) begin
        ReqAddress <= {{10'd0}, _T_125};
      end
    end
    if (reset) begin
      inptr <= 21'h0;
    end else begin
      if (_T_117) begin
        if (_T_167) begin
          inptr <= 21'h0;
        end else begin
          if (_T_123) begin
            inptr <= _T_136;
          end
        end
      end else begin
        if (_T_123) begin
          inptr <= _T_136;
        end
      end
    end
    if (reset) begin
      sendptr <= 2'h0;
    end else begin
      if (_T_117) begin
        if (_T_167) begin
          sendptr <= 2'h0;
        end else begin
          if (_T_137) begin
            if (_T_152) begin
              sendptr <= _T_155;
            end
          end
        end
      end else begin
        if (_T_137) begin
          if (_T_152) begin
            sendptr <= _T_155;
          end
        end
      end
    end
    if (reset) begin
      recvptr <= 2'h0;
    end else begin
      if (_T_117) begin
        if (_T_167) begin
          recvptr <= 2'h0;
        end else begin
          if (io_MemResp_valid) begin
            recvptr <= _T_160;
          end
        end
      end else begin
        if (io_MemResp_valid) begin
          recvptr <= _T_160;
        end
      end
    end
    if (reset) begin
      linebuffer_0 <= _T_62_0;
    end else begin
      if (_T_123) begin
        if (1'h0 == _T_129) begin
          linebuffer_0 <= _GEN_0;
        end
      end
    end
    if (reset) begin
      linebuffer_1 <= _T_62_1;
    end else begin
      if (_T_123) begin
        if (_T_129) begin
          linebuffer_1 <= _GEN_0;
        end
      end
    end
    if (reset) begin
      linemask_0 <= _T_89_0;
    end else begin
      if (_T_123) begin
        if (1'h0 == _T_129) begin
          linemask_0 <= _GEN_1;
        end
      end
    end
    if (reset) begin
      linemask_1 <= _T_89_1;
    end else begin
      if (_T_123) begin
        if (_T_129) begin
          linemask_1 <= _GEN_1;
        end
      end
    end
    if (reset) begin
      state <= 2'h0;
    end else begin
      if (_T_117) begin
        if (_T_167) begin
          state <= 2'h0;
        end else begin
          if (io_MemResp_valid) begin
            if (_T_162) begin
              state <= 2'h3;
            end else begin
              state <= 2'h1;
            end
          end
        end
      end else begin
        if (io_MemResp_valid) begin
          if (_T_162) begin
            state <= 2'h3;
          end else begin
            state <= 2'h1;
          end
        end
      end
    end
  end
endmodule
module WriteTypMemoryController(
  input         clock,
  input         reset,
  output        io_WriteIn_0_ready,
  input         io_WriteIn_0_valid,
  input  [15:0] io_WriteIn_0_bits_RouteID,
  input  [5:0]  io_WriteIn_0_bits_address,
  input  [15:0] io_WriteIn_0_bits_data,
  input  [1:0]  io_WriteIn_0_bits_mask,
  output        io_WriteOut_0_valid,
  output [15:0] io_WriteOut_0_RouteID,
  output        io_WriteOut_0_done,
  input         io_CacheReq_ready,
  output        io_CacheReq_valid,
  output [15:0] io_CacheReq_bits_addr,
  output [15:0] io_CacheReq_bits_data,
  output [1:0]  io_CacheReq_bits_mask,
  input         io_CacheResp_valid
);
  wire  in_arb_clock;
  wire  in_arb_reset;
  wire  in_arb_io_in_0_ready;
  wire  in_arb_io_in_0_valid;
  wire [15:0] in_arb_io_in_0_bits_RouteID;
  wire [5:0] in_arb_io_in_0_bits_address;
  wire [15:0] in_arb_io_in_0_bits_data;
  wire [1:0] in_arb_io_in_0_bits_mask;
  wire  in_arb_io_out_ready;
  wire  in_arb_io_out_valid;
  wire [15:0] in_arb_io_out_bits_RouteID;
  wire [5:0] in_arb_io_out_bits_address;
  wire [15:0] in_arb_io_out_bits_data;
  wire [1:0] in_arb_io_out_bits_mask;
  wire  alloc_arb_io_in_0_ready;
  wire  alloc_arb_io_in_0_valid;
  wire  alloc_arb_io_out_ready;
  wire  alloc_arb_io_out_valid;
  wire  cachereq_arb_io_in_0_ready;
  wire  cachereq_arb_io_in_0_valid;
  wire [15:0] cachereq_arb_io_in_0_bits_addr;
  wire [15:0] cachereq_arb_io_in_0_bits_data;
  wire [1:0] cachereq_arb_io_in_0_bits_mask;
  wire  cachereq_arb_io_out_ready;
  wire  cachereq_arb_io_out_valid;
  wire [15:0] cachereq_arb_io_out_bits_addr;
  wire [15:0] cachereq_arb_io_out_bits_data;
  wire [1:0] cachereq_arb_io_out_bits_mask;
  wire  cacheresp_demux_io_en;
  wire  cacheresp_demux_io_outputs_0_valid;
  wire  out_arb_io_in_0_ready;
  wire  out_arb_io_in_0_valid;
  wire  out_arb_io_in_0_bits_valid;
  wire [15:0] out_arb_io_in_0_bits_RouteID;
  wire  out_arb_io_in_0_bits_done;
  wire  out_arb_io_out_ready;
  wire  out_arb_io_out_valid;
  wire  out_arb_io_out_bits_valid;
  wire [15:0] out_arb_io_out_bits_RouteID;
  wire  out_arb_io_out_bits_done;
  wire  out_demux_io_outputs_0_valid;
  wire [15:0] out_demux_io_outputs_0_RouteID;
  wire  out_demux_io_outputs_0_done;
  wire  out_demux_io_input_valid;
  wire [15:0] out_demux_io_input_RouteID;
  wire  out_demux_io_input_done;
  wire  out_demux_io_enable;
  wire  WriteTable_0_clock;
  wire  WriteTable_0_reset;
  wire  WriteTable_0_io_NodeReq_ready;
  wire  WriteTable_0_io_NodeReq_valid;
  wire [15:0] WriteTable_0_io_NodeReq_bits_RouteID;
  wire [5:0] WriteTable_0_io_NodeReq_bits_address;
  wire [15:0] WriteTable_0_io_NodeReq_bits_data;
  wire [1:0] WriteTable_0_io_NodeReq_bits_mask;
  wire  WriteTable_0_io_MemReq_ready;
  wire  WriteTable_0_io_MemReq_valid;
  wire [15:0] WriteTable_0_io_MemReq_bits_addr;
  wire [15:0] WriteTable_0_io_MemReq_bits_data;
  wire [1:0] WriteTable_0_io_MemReq_bits_mask;
  wire  WriteTable_0_io_MemResp_valid;
  wire  WriteTable_0_io_output_ready;
  wire  WriteTable_0_io_output_valid;
  wire  WriteTable_0_io_output_bits_valid;
  wire [15:0] WriteTable_0_io_output_bits_RouteID;
  wire  WriteTable_0_io_output_bits_done;
  wire  WriteTable_0_io_free;
  wire  _T_37;
  wire  _T_38;
  wire  _T_39;
  wire  _T_41;
  ArbiterTree in_arb (
    .clock(in_arb_clock),
    .reset(in_arb_reset),
    .io_in_0_ready(in_arb_io_in_0_ready),
    .io_in_0_valid(in_arb_io_in_0_valid),
    .io_in_0_bits_RouteID(in_arb_io_in_0_bits_RouteID),
    .io_in_0_bits_address(in_arb_io_in_0_bits_address),
    .io_in_0_bits_data(in_arb_io_in_0_bits_data),
    .io_in_0_bits_mask(in_arb_io_in_0_bits_mask),
    .io_out_ready(in_arb_io_out_ready),
    .io_out_valid(in_arb_io_out_valid),
    .io_out_bits_RouteID(in_arb_io_out_bits_RouteID),
    .io_out_bits_address(in_arb_io_out_bits_address),
    .io_out_bits_data(in_arb_io_out_bits_data),
    .io_out_bits_mask(in_arb_io_out_bits_mask)
  );
  LockingRRArbiter_1 alloc_arb (
    .io_in_0_ready(alloc_arb_io_in_0_ready),
    .io_in_0_valid(alloc_arb_io_in_0_valid),
    .io_out_ready(alloc_arb_io_out_ready),
    .io_out_valid(alloc_arb_io_out_valid)
  );
  RRArbiter cachereq_arb (
    .io_in_0_ready(cachereq_arb_io_in_0_ready),
    .io_in_0_valid(cachereq_arb_io_in_0_valid),
    .io_in_0_bits_addr(cachereq_arb_io_in_0_bits_addr),
    .io_in_0_bits_data(cachereq_arb_io_in_0_bits_data),
    .io_in_0_bits_mask(cachereq_arb_io_in_0_bits_mask),
    .io_out_ready(cachereq_arb_io_out_ready),
    .io_out_valid(cachereq_arb_io_out_valid),
    .io_out_bits_addr(cachereq_arb_io_out_bits_addr),
    .io_out_bits_data(cachereq_arb_io_out_bits_data),
    .io_out_bits_mask(cachereq_arb_io_out_bits_mask)
  );
  Demux cacheresp_demux (
    .io_en(cacheresp_demux_io_en),
    .io_outputs_0_valid(cacheresp_demux_io_outputs_0_valid)
  );
  RRArbiter_1 out_arb (
    .io_in_0_ready(out_arb_io_in_0_ready),
    .io_in_0_valid(out_arb_io_in_0_valid),
    .io_in_0_bits_valid(out_arb_io_in_0_bits_valid),
    .io_in_0_bits_RouteID(out_arb_io_in_0_bits_RouteID),
    .io_in_0_bits_done(out_arb_io_in_0_bits_done),
    .io_out_ready(out_arb_io_out_ready),
    .io_out_valid(out_arb_io_out_valid),
    .io_out_bits_valid(out_arb_io_out_bits_valid),
    .io_out_bits_RouteID(out_arb_io_out_bits_RouteID),
    .io_out_bits_done(out_arb_io_out_bits_done)
  );
  DeMuxTree out_demux (
    .io_outputs_0_valid(out_demux_io_outputs_0_valid),
    .io_outputs_0_RouteID(out_demux_io_outputs_0_RouteID),
    .io_outputs_0_done(out_demux_io_outputs_0_done),
    .io_input_valid(out_demux_io_input_valid),
    .io_input_RouteID(out_demux_io_input_RouteID),
    .io_input_done(out_demux_io_input_done),
    .io_enable(out_demux_io_enable)
  );
  WriteTypTableEntry WriteTable_0 (
    .clock(WriteTable_0_clock),
    .reset(WriteTable_0_reset),
    .io_NodeReq_ready(WriteTable_0_io_NodeReq_ready),
    .io_NodeReq_valid(WriteTable_0_io_NodeReq_valid),
    .io_NodeReq_bits_RouteID(WriteTable_0_io_NodeReq_bits_RouteID),
    .io_NodeReq_bits_address(WriteTable_0_io_NodeReq_bits_address),
    .io_NodeReq_bits_data(WriteTable_0_io_NodeReq_bits_data),
    .io_NodeReq_bits_mask(WriteTable_0_io_NodeReq_bits_mask),
    .io_MemReq_ready(WriteTable_0_io_MemReq_ready),
    .io_MemReq_valid(WriteTable_0_io_MemReq_valid),
    .io_MemReq_bits_addr(WriteTable_0_io_MemReq_bits_addr),
    .io_MemReq_bits_data(WriteTable_0_io_MemReq_bits_data),
    .io_MemReq_bits_mask(WriteTable_0_io_MemReq_bits_mask),
    .io_MemResp_valid(WriteTable_0_io_MemResp_valid),
    .io_output_ready(WriteTable_0_io_output_ready),
    .io_output_valid(WriteTable_0_io_output_valid),
    .io_output_bits_valid(WriteTable_0_io_output_bits_valid),
    .io_output_bits_RouteID(WriteTable_0_io_output_bits_RouteID),
    .io_output_bits_done(WriteTable_0_io_output_bits_done),
    .io_free(WriteTable_0_io_free)
  );
  assign io_WriteIn_0_ready = in_arb_io_in_0_ready;
  assign io_WriteOut_0_valid = out_demux_io_outputs_0_valid;
  assign io_WriteOut_0_RouteID = out_demux_io_outputs_0_RouteID;
  assign io_WriteOut_0_done = out_demux_io_outputs_0_done;
  assign io_CacheReq_valid = cachereq_arb_io_out_valid;
  assign io_CacheReq_bits_addr = cachereq_arb_io_out_bits_addr;
  assign io_CacheReq_bits_data = cachereq_arb_io_out_bits_data;
  assign io_CacheReq_bits_mask = cachereq_arb_io_out_bits_mask;
  assign in_arb_clock = clock;
  assign in_arb_reset = reset;
  assign in_arb_io_in_0_valid = io_WriteIn_0_valid;
  assign in_arb_io_in_0_bits_RouteID = io_WriteIn_0_bits_RouteID;
  assign in_arb_io_in_0_bits_address = io_WriteIn_0_bits_address;
  assign in_arb_io_in_0_bits_data = io_WriteIn_0_bits_data;
  assign in_arb_io_in_0_bits_mask = io_WriteIn_0_bits_mask;
  assign in_arb_io_out_ready = alloc_arb_io_out_valid;
  assign alloc_arb_io_in_0_valid = WriteTable_0_io_free;
  assign alloc_arb_io_out_ready = in_arb_io_out_valid;
  assign cachereq_arb_io_in_0_valid = WriteTable_0_io_MemReq_valid;
  assign cachereq_arb_io_in_0_bits_addr = WriteTable_0_io_MemReq_bits_addr;
  assign cachereq_arb_io_in_0_bits_data = WriteTable_0_io_MemReq_bits_data;
  assign cachereq_arb_io_in_0_bits_mask = WriteTable_0_io_MemReq_bits_mask;
  assign cachereq_arb_io_out_ready = io_CacheReq_ready;
  assign cacheresp_demux_io_en = io_CacheResp_valid;
  assign out_arb_io_in_0_valid = WriteTable_0_io_output_valid;
  assign out_arb_io_in_0_bits_valid = WriteTable_0_io_output_bits_valid;
  assign out_arb_io_in_0_bits_RouteID = WriteTable_0_io_output_bits_RouteID;
  assign out_arb_io_in_0_bits_done = WriteTable_0_io_output_bits_done;
  assign out_arb_io_out_ready = 1'h1;
  assign out_demux_io_input_valid = out_arb_io_out_bits_valid;
  assign out_demux_io_input_RouteID = out_arb_io_out_bits_RouteID;
  assign out_demux_io_input_done = out_arb_io_out_bits_done;
  assign out_demux_io_enable = _T_41;
  assign WriteTable_0_clock = clock;
  assign WriteTable_0_reset = reset;
  assign WriteTable_0_io_NodeReq_valid = _T_39;
  assign WriteTable_0_io_NodeReq_bits_RouteID = in_arb_io_out_bits_RouteID;
  assign WriteTable_0_io_NodeReq_bits_address = in_arb_io_out_bits_address;
  assign WriteTable_0_io_NodeReq_bits_data = in_arb_io_out_bits_data;
  assign WriteTable_0_io_NodeReq_bits_mask = in_arb_io_out_bits_mask;
  assign WriteTable_0_io_MemReq_ready = cachereq_arb_io_in_0_ready;
  assign WriteTable_0_io_MemResp_valid = cacheresp_demux_io_outputs_0_valid;
  assign WriteTable_0_io_output_ready = out_arb_io_in_0_ready;
  assign _T_37 = alloc_arb_io_in_0_ready & alloc_arb_io_in_0_valid;
  assign _T_38 = in_arb_io_out_ready & in_arb_io_out_valid;
  assign _T_39 = _T_37 & _T_38;
  assign _T_41 = out_arb_io_out_ready & out_arb_io_out_valid;
endmodule
module LockingRRArbiter_2(
  input         clock,
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [15:0] io_in_0_bits_RouteID,
  input  [15:0] io_in_0_bits_address,
  output        io_in_1_ready,
  input         io_in_1_valid,
  input  [15:0] io_in_1_bits_RouteID,
  input  [15:0] io_in_1_bits_address,
  input         io_out_ready,
  output        io_out_valid,
  output [15:0] io_out_bits_RouteID,
  output [15:0] io_out_bits_address,
  output        io_chosen
);
  wire  choice;
  wire  _GEN_0_valid;
  wire  _GEN_6;
  wire [15:0] _GEN_7;
  wire [15:0] _GEN_8;
  wire [15:0] _GEN_3_bits_address;
  wire [15:0] _GEN_4_bits_RouteID;
  wire  _T_60;
  reg  lastGrant;
  reg [31:0] _RAND_0;
  wire  _GEN_11;
  wire  grantMask_1;
  wire  validMask_1;
  wire  _T_65;
  wire  _T_69;
  wire  _T_71;
  wire  _T_75;
  wire  _T_76;
  wire  _T_77;
  wire  _GEN_12;
  wire  _GEN_13;
  assign io_in_0_ready = _T_76;
  assign io_in_1_ready = _T_77;
  assign io_out_valid = _GEN_0_valid;
  assign io_out_bits_RouteID = _GEN_4_bits_RouteID;
  assign io_out_bits_address = _GEN_3_bits_address;
  assign io_chosen = choice;
  assign choice = _GEN_13;
  assign _GEN_0_valid = _GEN_6;
  assign _GEN_6 = io_chosen ? io_in_1_valid : io_in_0_valid;
  assign _GEN_7 = io_chosen ? io_in_1_bits_RouteID : io_in_0_bits_RouteID;
  assign _GEN_8 = io_chosen ? io_in_1_bits_address : io_in_0_bits_address;
  assign _GEN_3_bits_address = _GEN_8;
  assign _GEN_4_bits_RouteID = _GEN_7;
  assign _T_60 = io_out_ready & io_out_valid;
  assign _GEN_11 = _T_60 ? io_chosen : lastGrant;
  assign grantMask_1 = 1'h1 > lastGrant;
  assign validMask_1 = io_in_1_valid & grantMask_1;
  assign _T_65 = validMask_1 | io_in_0_valid;
  assign _T_69 = validMask_1 == 1'h0;
  assign _T_71 = _T_65 == 1'h0;
  assign _T_75 = grantMask_1 | _T_71;
  assign _T_76 = _T_69 & io_out_ready;
  assign _T_77 = _T_75 & io_out_ready;
  assign _GEN_12 = io_in_0_valid ? 1'h0 : 1'h1;
  assign _GEN_13 = validMask_1 ? 1'h1 : _GEN_12;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  lastGrant = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (_T_60) begin
      lastGrant <= io_chosen;
    end
  end
endmodule
module ArbiterTree_1(
  input         clock,
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [15:0] io_in_0_bits_RouteID,
  input  [15:0] io_in_0_bits_address,
  output        io_in_1_ready,
  input         io_in_1_valid,
  input  [15:0] io_in_1_bits_RouteID,
  input  [15:0] io_in_1_bits_address,
  input         io_out_ready,
  output        io_out_valid,
  output [15:0] io_out_bits_RouteID,
  output [15:0] io_out_bits_address
);
  wire  LockingRRArbiter_clock;
  wire  LockingRRArbiter_io_in_0_ready;
  wire  LockingRRArbiter_io_in_0_valid;
  wire [15:0] LockingRRArbiter_io_in_0_bits_RouteID;
  wire [15:0] LockingRRArbiter_io_in_0_bits_address;
  wire  LockingRRArbiter_io_in_1_ready;
  wire  LockingRRArbiter_io_in_1_valid;
  wire [15:0] LockingRRArbiter_io_in_1_bits_RouteID;
  wire [15:0] LockingRRArbiter_io_in_1_bits_address;
  wire  LockingRRArbiter_io_out_ready;
  wire  LockingRRArbiter_io_out_valid;
  wire [15:0] LockingRRArbiter_io_out_bits_RouteID;
  wire [15:0] LockingRRArbiter_io_out_bits_address;
  wire  LockingRRArbiter_io_chosen;
  LockingRRArbiter_2 LockingRRArbiter (
    .clock(LockingRRArbiter_clock),
    .io_in_0_ready(LockingRRArbiter_io_in_0_ready),
    .io_in_0_valid(LockingRRArbiter_io_in_0_valid),
    .io_in_0_bits_RouteID(LockingRRArbiter_io_in_0_bits_RouteID),
    .io_in_0_bits_address(LockingRRArbiter_io_in_0_bits_address),
    .io_in_1_ready(LockingRRArbiter_io_in_1_ready),
    .io_in_1_valid(LockingRRArbiter_io_in_1_valid),
    .io_in_1_bits_RouteID(LockingRRArbiter_io_in_1_bits_RouteID),
    .io_in_1_bits_address(LockingRRArbiter_io_in_1_bits_address),
    .io_out_ready(LockingRRArbiter_io_out_ready),
    .io_out_valid(LockingRRArbiter_io_out_valid),
    .io_out_bits_RouteID(LockingRRArbiter_io_out_bits_RouteID),
    .io_out_bits_address(LockingRRArbiter_io_out_bits_address),
    .io_chosen(LockingRRArbiter_io_chosen)
  );
  assign io_in_0_ready = LockingRRArbiter_io_in_0_ready;
  assign io_in_1_ready = LockingRRArbiter_io_in_1_ready;
  assign io_out_valid = LockingRRArbiter_io_out_valid;
  assign io_out_bits_RouteID = LockingRRArbiter_io_out_bits_RouteID;
  assign io_out_bits_address = LockingRRArbiter_io_out_bits_address;
  assign LockingRRArbiter_clock = clock;
  assign LockingRRArbiter_io_in_0_valid = io_in_0_valid;
  assign LockingRRArbiter_io_in_0_bits_RouteID = io_in_0_bits_RouteID;
  assign LockingRRArbiter_io_in_0_bits_address = io_in_0_bits_address;
  assign LockingRRArbiter_io_in_1_valid = io_in_1_valid;
  assign LockingRRArbiter_io_in_1_bits_RouteID = io_in_1_bits_RouteID;
  assign LockingRRArbiter_io_in_1_bits_address = io_in_1_bits_address;
  assign LockingRRArbiter_io_out_ready = io_out_ready;
endmodule
module Arbiter(
  output  io_in_0_ready,
  input   io_in_0_valid,
  output  io_in_1_ready,
  input   io_in_1_valid,
  input   io_out_ready,
  output  io_out_valid
);
  wire  _T_47;
  wire  _T_49;
  wire  _T_51;
  wire  _T_52;
  assign io_in_0_ready = io_out_ready;
  assign io_in_1_ready = _T_49;
  assign io_out_valid = _T_52;
  assign _T_47 = io_in_0_valid == 1'h0;
  assign _T_49 = _T_47 & io_out_ready;
  assign _T_51 = _T_47 == 1'h0;
  assign _T_52 = _T_51 | io_in_1_valid;
endmodule
module LockingRRArbiter_3(
  input         clock,
  input         reset,
  output        io_in_0_ready,
  input         io_in_0_valid,
  input  [15:0] io_in_0_bits_addr,
  input  [7:0]  io_in_0_bits_tag,
  output        io_in_1_ready,
  input         io_in_1_valid,
  input  [15:0] io_in_1_bits_addr,
  input  [7:0]  io_in_1_bits_tag,
  input         io_out_ready,
  output        io_out_valid,
  output [15:0] io_out_bits_addr,
  output [7:0]  io_out_bits_tag,
  output        io_chosen
);
  wire  choice;
  wire  _GEN_0_valid;
  wire  _GEN_7;
  wire [15:0] _GEN_8;
  wire [7:0] _GEN_11;
  wire [7:0] _GEN_2_bits_tag;
  wire [15:0] _GEN_5_bits_addr;
  reg  value;
  reg [31:0] _RAND_0;
  reg  _T_62;
  reg [31:0] _RAND_1;
  wire  _T_66;
  wire [1:0] _T_71;
  wire  _T_72;
  wire  _GEN_13;
  wire  _GEN_14;
  wire  _GEN_15;
  reg  lastGrant;
  reg [31:0] _RAND_2;
  wire  _GEN_16;
  wire  grantMask_1;
  wire  validMask_1;
  wire  _T_79;
  wire  _T_83;
  wire  _T_85;
  wire  _T_89;
  wire  _T_91;
  wire  _T_92;
  wire  _T_93;
  wire  _T_96;
  wire  _T_97;
  wire  _GEN_17;
  wire  _GEN_18;
  assign io_in_0_ready = _T_93;
  assign io_in_1_ready = _T_97;
  assign io_out_valid = _GEN_0_valid;
  assign io_out_bits_addr = _GEN_5_bits_addr;
  assign io_out_bits_tag = _GEN_2_bits_tag;
  assign io_chosen = _GEN_15;
  assign choice = _GEN_18;
  assign _GEN_0_valid = _GEN_7;
  assign _GEN_7 = io_chosen ? io_in_1_valid : io_in_0_valid;
  assign _GEN_8 = io_chosen ? io_in_1_bits_addr : io_in_0_bits_addr;
  assign _GEN_11 = io_chosen ? io_in_1_bits_tag : io_in_0_bits_tag;
  assign _GEN_2_bits_tag = _GEN_11;
  assign _GEN_5_bits_addr = _GEN_8;
  assign _T_66 = io_out_ready & io_out_valid;
  assign _T_71 = value + 1'h1;
  assign _T_72 = _T_71[0:0];
  assign _GEN_13 = _T_66 ? io_chosen : _T_62;
  assign _GEN_14 = _T_66 ? _T_72 : value;
  assign _GEN_15 = value ? _T_62 : choice;
  assign _GEN_16 = _T_66 ? io_chosen : lastGrant;
  assign grantMask_1 = 1'h1 > lastGrant;
  assign validMask_1 = io_in_1_valid & grantMask_1;
  assign _T_79 = validMask_1 | io_in_0_valid;
  assign _T_83 = validMask_1 == 1'h0;
  assign _T_85 = _T_79 == 1'h0;
  assign _T_89 = grantMask_1 | _T_85;
  assign _T_91 = _T_62 == 1'h0;
  assign _T_92 = value ? _T_91 : _T_83;
  assign _T_93 = _T_92 & io_out_ready;
  assign _T_96 = value ? _T_62 : _T_89;
  assign _T_97 = _T_96 & io_out_ready;
  assign _GEN_17 = io_in_0_valid ? 1'h0 : 1'h1;
  assign _GEN_18 = validMask_1 ? 1'h1 : _GEN_17;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  value = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  _T_62 = _RAND_1[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  lastGrant = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      value <= 1'h0;
    end else begin
      if (_T_66) begin
        value <= _T_72;
      end
    end
    if (_T_66) begin
      _T_62 <= io_chosen;
    end
    if (_T_66) begin
      lastGrant <= io_chosen;
    end
  end
endmodule
module Demux_2(
  input         io_en,
  input         io_input_valid,
  input  [15:0] io_input_data,
  input         io_sel,
  output        io_outputs_0_valid,
  output [15:0] io_outputs_0_data,
  output        io_outputs_1_valid,
  output [15:0] io_outputs_1_data
);
  wire [15:0] _GEN_1;
  wire  _GEN_2;
  wire  _GEN_8;
  wire  _GEN_9;
  wire  _GEN_3;
  wire  _GEN_10;
  wire  _GEN_11;
  wire  _T_16;
  wire  _GEN_18;
  wire  _GEN_19;
  assign io_outputs_0_valid = _GEN_18;
  assign io_outputs_0_data = _GEN_1;
  assign io_outputs_1_valid = _GEN_19;
  assign io_outputs_1_data = _GEN_1;
  assign _GEN_1 = io_input_data;
  assign _GEN_2 = io_input_valid;
  assign _GEN_8 = 1'h0 == io_sel ? _GEN_2 : 1'h0;
  assign _GEN_9 = io_sel ? _GEN_2 : 1'h0;
  assign _GEN_3 = 1'h1;
  assign _GEN_10 = 1'h0 == io_sel ? _GEN_3 : _GEN_8;
  assign _GEN_11 = io_sel ? _GEN_3 : _GEN_9;
  assign _T_16 = io_en == 1'h0;
  assign _GEN_18 = _T_16 ? 1'h0 : _GEN_10;
  assign _GEN_19 = _T_16 ? 1'h0 : _GEN_11;
endmodule
module LockingRRArbiter_4(
  input         clock,
  input         reset,
  output        io_in_0_ready,
  input         io_in_0_valid,
  input         io_in_0_bits_valid,
  input  [15:0] io_in_0_bits_RouteID,
  input  [15:0] io_in_0_bits_data,
  output        io_in_1_ready,
  input         io_in_1_valid,
  input         io_in_1_bits_valid,
  input  [15:0] io_in_1_bits_RouteID,
  input  [15:0] io_in_1_bits_data,
  input         io_out_ready,
  output        io_out_valid,
  output        io_out_bits_valid,
  output [15:0] io_out_bits_RouteID,
  output [15:0] io_out_bits_data,
  output        io_chosen
);
  wire  choice;
  wire  _GEN_0_valid;
  wire  _GEN_5;
  wire  _GEN_6;
  wire [15:0] _GEN_7;
  wire [15:0] _GEN_8;
  wire [15:0] _GEN_1_bits_data;
  wire [15:0] _GEN_2_bits_RouteID;
  wire  _GEN_3_bits_valid;
  reg  value;
  reg [31:0] _RAND_0;
  reg  _T_62;
  reg [31:0] _RAND_1;
  wire  _T_66;
  wire [1:0] _T_71;
  wire  _T_72;
  wire  _GEN_9;
  wire  _GEN_10;
  wire  _GEN_11;
  reg  lastGrant;
  reg [31:0] _RAND_2;
  wire  _GEN_12;
  wire  grantMask_1;
  wire  validMask_1;
  wire  _T_79;
  wire  _T_83;
  wire  _T_85;
  wire  _T_89;
  wire  _T_91;
  wire  _T_92;
  wire  _T_93;
  wire  _T_96;
  wire  _T_97;
  wire  _GEN_13;
  wire  _GEN_14;
  assign io_in_0_ready = _T_93;
  assign io_in_1_ready = _T_97;
  assign io_out_valid = _GEN_0_valid;
  assign io_out_bits_valid = _GEN_3_bits_valid;
  assign io_out_bits_RouteID = _GEN_2_bits_RouteID;
  assign io_out_bits_data = _GEN_1_bits_data;
  assign io_chosen = _GEN_11;
  assign choice = _GEN_14;
  assign _GEN_0_valid = _GEN_5;
  assign _GEN_5 = io_chosen ? io_in_1_valid : io_in_0_valid;
  assign _GEN_6 = io_chosen ? io_in_1_bits_valid : io_in_0_bits_valid;
  assign _GEN_7 = io_chosen ? io_in_1_bits_RouteID : io_in_0_bits_RouteID;
  assign _GEN_8 = io_chosen ? io_in_1_bits_data : io_in_0_bits_data;
  assign _GEN_1_bits_data = _GEN_8;
  assign _GEN_2_bits_RouteID = _GEN_7;
  assign _GEN_3_bits_valid = _GEN_6;
  assign _T_66 = io_out_ready & io_out_valid;
  assign _T_71 = value + 1'h1;
  assign _T_72 = _T_71[0:0];
  assign _GEN_9 = _T_66 ? io_chosen : _T_62;
  assign _GEN_10 = _T_66 ? _T_72 : value;
  assign _GEN_11 = value ? _T_62 : choice;
  assign _GEN_12 = _T_66 ? io_chosen : lastGrant;
  assign grantMask_1 = 1'h1 > lastGrant;
  assign validMask_1 = io_in_1_valid & grantMask_1;
  assign _T_79 = validMask_1 | io_in_0_valid;
  assign _T_83 = validMask_1 == 1'h0;
  assign _T_85 = _T_79 == 1'h0;
  assign _T_89 = grantMask_1 | _T_85;
  assign _T_91 = _T_62 == 1'h0;
  assign _T_92 = value ? _T_91 : _T_83;
  assign _T_93 = _T_92 & io_out_ready;
  assign _T_96 = value ? _T_62 : _T_89;
  assign _T_97 = _T_96 & io_out_ready;
  assign _GEN_13 = io_in_0_valid ? 1'h0 : 1'h1;
  assign _GEN_14 = validMask_1 ? 1'h1 : _GEN_13;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  value = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  _T_62 = _RAND_1[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  lastGrant = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      value <= 1'h0;
    end else begin
      if (_T_66) begin
        value <= _T_72;
      end
    end
    if (_T_66) begin
      _T_62 <= io_chosen;
    end
    if (_T_66) begin
      lastGrant <= io_chosen;
    end
  end
endmodule
module Demux_3(
  input         io_en,
  input         io_input_valid,
  input  [15:0] io_input_RouteID,
  input  [15:0] io_input_data,
  input         io_sel,
  output        io_outputs_0_valid,
  output [15:0] io_outputs_0_RouteID,
  output [15:0] io_outputs_0_data,
  output        io_outputs_1_valid,
  output [15:0] io_outputs_1_RouteID,
  output [15:0] io_outputs_1_data
);
  wire [15:0] _GEN_0;
  wire [15:0] _GEN_1;
  wire  _GEN_2;
  wire  _GEN_8;
  wire  _GEN_9;
  wire  _GEN_3;
  wire  _GEN_10;
  wire  _GEN_11;
  wire  _T_16;
  wire  _GEN_18;
  wire  _GEN_19;
  assign io_outputs_0_valid = _GEN_18;
  assign io_outputs_0_RouteID = _GEN_1;
  assign io_outputs_0_data = _GEN_0;
  assign io_outputs_1_valid = _GEN_19;
  assign io_outputs_1_RouteID = _GEN_1;
  assign io_outputs_1_data = _GEN_0;
  assign _GEN_0 = io_input_data;
  assign _GEN_1 = io_input_RouteID;
  assign _GEN_2 = io_input_valid;
  assign _GEN_8 = 1'h0 == io_sel ? _GEN_2 : 1'h0;
  assign _GEN_9 = io_sel ? _GEN_2 : 1'h0;
  assign _GEN_3 = 1'h1;
  assign _GEN_10 = 1'h0 == io_sel ? _GEN_3 : _GEN_8;
  assign _GEN_11 = io_sel ? _GEN_3 : _GEN_9;
  assign _T_16 = io_en == 1'h0;
  assign _GEN_18 = _T_16 ? 1'h0 : _GEN_10;
  assign _GEN_19 = _T_16 ? 1'h0 : _GEN_11;
endmodule
module DeMuxTree_1(
  output        io_outputs_0_valid,
  output [15:0] io_outputs_0_RouteID,
  output [15:0] io_outputs_0_data,
  output        io_outputs_1_valid,
  output [15:0] io_outputs_1_RouteID,
  output [15:0] io_outputs_1_data,
  input         io_input_valid,
  input  [15:0] io_input_RouteID,
  input  [15:0] io_input_data,
  input         io_enable
);
  wire  Demux_io_en;
  wire  Demux_io_input_valid;
  wire [15:0] Demux_io_input_RouteID;
  wire [15:0] Demux_io_input_data;
  wire  Demux_io_sel;
  wire  Demux_io_outputs_0_valid;
  wire [15:0] Demux_io_outputs_0_RouteID;
  wire [15:0] Demux_io_outputs_0_data;
  wire  Demux_io_outputs_1_valid;
  wire [15:0] Demux_io_outputs_1_RouteID;
  wire [15:0] Demux_io_outputs_1_data;
  wire  _T_6;
  Demux_3 Demux (
    .io_en(Demux_io_en),
    .io_input_valid(Demux_io_input_valid),
    .io_input_RouteID(Demux_io_input_RouteID),
    .io_input_data(Demux_io_input_data),
    .io_sel(Demux_io_sel),
    .io_outputs_0_valid(Demux_io_outputs_0_valid),
    .io_outputs_0_RouteID(Demux_io_outputs_0_RouteID),
    .io_outputs_0_data(Demux_io_outputs_0_data),
    .io_outputs_1_valid(Demux_io_outputs_1_valid),
    .io_outputs_1_RouteID(Demux_io_outputs_1_RouteID),
    .io_outputs_1_data(Demux_io_outputs_1_data)
  );
  assign io_outputs_0_valid = Demux_io_outputs_0_valid;
  assign io_outputs_0_RouteID = Demux_io_outputs_0_RouteID;
  assign io_outputs_0_data = Demux_io_outputs_0_data;
  assign io_outputs_1_valid = Demux_io_outputs_1_valid;
  assign io_outputs_1_RouteID = Demux_io_outputs_1_RouteID;
  assign io_outputs_1_data = Demux_io_outputs_1_data;
  assign Demux_io_en = io_enable;
  assign Demux_io_input_valid = io_input_valid;
  assign Demux_io_input_RouteID = io_input_RouteID;
  assign Demux_io_input_data = io_input_data;
  assign Demux_io_sel = _T_6;
  assign _T_6 = io_input_RouteID[0];
endmodule
module ReadTypTableEntry(
  input         clock,
  input         reset,
  output        io_NodeReq_ready,
  input         io_NodeReq_valid,
  input  [15:0] io_NodeReq_bits_RouteID,
  input  [15:0] io_NodeReq_bits_address,
  input         io_MemReq_ready,
  output        io_MemReq_valid,
  output [15:0] io_MemReq_bits_addr,
  output [7:0]  io_MemReq_bits_tag,
  input         io_MemResp_valid,
  input  [15:0] io_MemResp_data,
  input         io_output_ready,
  output        io_output_valid,
  output        io_output_bits_valid,
  output [15:0] io_output_bits_RouteID,
  output [15:0] io_output_bits_data,
  output        io_free
);
  reg  ID;
  reg [31:0] _RAND_0;
  wire [15:0] _T_29_RouteID;
  reg [15:0] request_R_RouteID;
  reg [31:0] _RAND_1;
  reg  request_valid_R;
  reg [31:0] _RAND_2;
  reg [15:0] ReqAddress;
  reg [31:0] _RAND_3;
  reg [1:0] outptr;
  reg [31:0] _RAND_4;
  reg [1:0] sendptr;
  reg [31:0] _RAND_5;
  reg [1:0] recvptr;
  reg [31:0] _RAND_6;
  wire [15:0] _T_53_0;
  wire [15:0] _T_53_1;
  wire [15:0] _T_53_2;
  reg [15:0] linebuffer_0;
  reg [31:0] _RAND_7;
  reg [15:0] linebuffer_1;
  reg [31:0] _RAND_8;
  reg [15:0] linebuffer_2;
  reg [31:0] _RAND_9;
  wire  _T_83;
  wire  _T_88;
  wire [14:0] _T_90;
  wire [15:0] _GEN_34;
  wire [15:0] _T_91;
  wire [15:0] _GEN_5;
  wire  _GEN_6;
  wire [15:0] _GEN_7;
  wire  _T_93;
  wire  _T_96;
  wire [2:0] _T_98;
  wire [15:0] _GEN_35;
  wire [16:0] _T_99;
  wire [15:0] _T_100;
  wire  _T_102;
  wire [2:0] _T_104;
  wire [1:0] _T_105;
  wire [1:0] _GEN_8;
  wire [1:0] _GEN_12;
  wire [15:0] _GEN_0;
  wire [15:0] _GEN_13;
  wire [15:0] _GEN_14;
  wire [15:0] _GEN_15;
  wire [2:0] _T_112;
  wire [1:0] _T_113;
  wire [15:0] _GEN_16;
  wire [15:0] _GEN_17;
  wire [15:0] _GEN_18;
  wire [1:0] _GEN_19;
  wire  _T_114;
  wire [15:0] _GEN_1;
  wire [15:0] _GEN_20;
  wire [15:0] _GEN_21;
  wire  _T_119;
  wire  _T_121;
  wire  _T_122;
  wire [2:0] _T_124;
  wire [1:0] _T_125;
  wire [1:0] _GEN_22;
  wire  _T_128;
  wire  _T_129;
  wire  _T_131;
  wire  _T_132;
  wire [1:0] _GEN_23;
  wire [1:0] _GEN_24;
  wire [1:0] _GEN_25;
  wire  _GEN_26;
  wire [1:0] _GEN_30;
  wire [1:0] _GEN_31;
  wire [1:0] _GEN_32;
  wire  _GEN_33;
  assign io_NodeReq_ready = _T_83;
  assign io_MemReq_valid = _T_96;
  assign io_MemReq_bits_addr = _T_100;
  assign io_MemReq_bits_tag = {{7'd0}, ID};
  assign io_output_valid = _T_114;
  assign io_output_bits_valid = 1'h0;
  assign io_output_bits_RouteID = request_R_RouteID;
  assign io_output_bits_data = _GEN_1;
  assign io_free = _T_83;
  assign _T_29_RouteID = 16'h0;
  assign _T_53_0 = 16'h0;
  assign _T_53_1 = 16'h0;
  assign _T_53_2 = 16'h0;
  assign _T_83 = ~ request_valid_R;
  assign _T_88 = io_NodeReq_ready & io_NodeReq_valid;
  assign _T_90 = io_NodeReq_bits_address[15:1];
  assign _GEN_34 = {{1'd0}, _T_90};
  assign _T_91 = _GEN_34 << 1;
  assign _GEN_5 = _T_88 ? io_NodeReq_bits_RouteID : request_R_RouteID;
  assign _GEN_6 = _T_88 ? 1'h1 : request_valid_R;
  assign _GEN_7 = _T_88 ? _T_91 : ReqAddress;
  assign _T_93 = sendptr != 2'h2;
  assign _T_96 = _T_93 & request_valid_R;
  assign _T_98 = {sendptr,1'h0};
  assign _GEN_35 = {{13'd0}, _T_98};
  assign _T_99 = ReqAddress + _GEN_35;
  assign _T_100 = _T_99[15:0];
  assign _T_102 = io_MemReq_ready & io_MemReq_valid;
  assign _T_104 = sendptr + 2'h1;
  assign _T_105 = _T_104[1:0];
  assign _GEN_8 = _T_102 ? _T_105 : sendptr;
  assign _GEN_12 = _T_96 ? _GEN_8 : sendptr;
  assign _GEN_0 = io_MemResp_data;
  assign _GEN_13 = 2'h0 == recvptr ? _GEN_0 : linebuffer_0;
  assign _GEN_14 = 2'h1 == recvptr ? _GEN_0 : linebuffer_1;
  assign _GEN_15 = 2'h2 == recvptr ? _GEN_0 : linebuffer_2;
  assign _T_112 = recvptr + 2'h1;
  assign _T_113 = _T_112[1:0];
  assign _GEN_16 = io_MemResp_valid ? _GEN_13 : linebuffer_0;
  assign _GEN_17 = io_MemResp_valid ? _GEN_14 : linebuffer_1;
  assign _GEN_18 = io_MemResp_valid ? _GEN_15 : linebuffer_2;
  assign _GEN_19 = io_MemResp_valid ? _T_113 : recvptr;
  assign _T_114 = outptr != recvptr;
  assign _GEN_1 = _GEN_21;
  assign _GEN_20 = 2'h1 == outptr ? linebuffer_1 : linebuffer_0;
  assign _GEN_21 = 2'h2 == outptr ? linebuffer_2 : _GEN_20;
  assign _T_119 = io_output_ready & io_output_valid;
  assign _T_121 = outptr != 2'h1;
  assign _T_122 = _T_119 & _T_121;
  assign _T_124 = outptr + 2'h1;
  assign _T_125 = _T_124[1:0];
  assign _GEN_22 = _T_122 ? _T_125 : outptr;
  assign _T_128 = outptr == 2'h1;
  assign _T_129 = _T_119 & _T_128;
  assign _T_131 = _T_122 == 1'h0;
  assign _T_132 = _T_131 & _T_129;
  assign _GEN_23 = _T_132 ? 2'h0 : _GEN_22;
  assign _GEN_24 = _T_132 ? 2'h0 : _GEN_12;
  assign _GEN_25 = _T_132 ? 2'h0 : _GEN_19;
  assign _GEN_26 = _T_132 ? 1'h0 : _GEN_6;
  assign _GEN_30 = _T_114 ? _GEN_23 : outptr;
  assign _GEN_31 = _T_114 ? _GEN_24 : _GEN_12;
  assign _GEN_32 = _T_114 ? _GEN_25 : _GEN_19;
  assign _GEN_33 = _T_114 ? _GEN_26 : _GEN_6;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  ID = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  request_R_RouteID = _RAND_1[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  request_valid_R = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{$random}};
  ReqAddress = _RAND_3[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{$random}};
  outptr = _RAND_4[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{$random}};
  sendptr = _RAND_5[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{$random}};
  recvptr = _RAND_6[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{$random}};
  linebuffer_0 = _RAND_7[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{$random}};
  linebuffer_1 = _RAND_8[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{$random}};
  linebuffer_2 = _RAND_9[15:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      ID <= 1'h0;
    end
    if (reset) begin
      request_R_RouteID <= _T_29_RouteID;
    end else begin
      if (_T_88) begin
        request_R_RouteID <= io_NodeReq_bits_RouteID;
      end
    end
    if (reset) begin
      request_valid_R <= 1'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          request_valid_R <= 1'h0;
        end else begin
          if (_T_88) begin
            request_valid_R <= 1'h1;
          end
        end
      end else begin
        if (_T_88) begin
          request_valid_R <= 1'h1;
        end
      end
    end
    if (reset) begin
      ReqAddress <= 16'h0;
    end else begin
      if (_T_88) begin
        ReqAddress <= _T_91;
      end
    end
    if (reset) begin
      outptr <= 2'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          outptr <= 2'h0;
        end else begin
          if (_T_122) begin
            outptr <= _T_125;
          end
        end
      end
    end
    if (reset) begin
      sendptr <= 2'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          sendptr <= 2'h0;
        end else begin
          if (_T_96) begin
            if (_T_102) begin
              sendptr <= _T_105;
            end
          end
        end
      end else begin
        if (_T_96) begin
          if (_T_102) begin
            sendptr <= _T_105;
          end
        end
      end
    end
    if (reset) begin
      recvptr <= 2'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          recvptr <= 2'h0;
        end else begin
          if (io_MemResp_valid) begin
            recvptr <= _T_113;
          end
        end
      end else begin
        if (io_MemResp_valid) begin
          recvptr <= _T_113;
        end
      end
    end
    if (reset) begin
      linebuffer_0 <= _T_53_0;
    end else begin
      if (io_MemResp_valid) begin
        if (2'h0 == recvptr) begin
          linebuffer_0 <= _GEN_0;
        end
      end
    end
    if (reset) begin
      linebuffer_1 <= _T_53_1;
    end else begin
      if (io_MemResp_valid) begin
        if (2'h1 == recvptr) begin
          linebuffer_1 <= _GEN_0;
        end
      end
    end
    if (reset) begin
      linebuffer_2 <= _T_53_2;
    end else begin
      if (io_MemResp_valid) begin
        if (2'h2 == recvptr) begin
          linebuffer_2 <= _GEN_0;
        end
      end
    end
  end
endmodule
module ReadTypTableEntry_1(
  input         clock,
  input         reset,
  output        io_NodeReq_ready,
  input         io_NodeReq_valid,
  input  [15:0] io_NodeReq_bits_RouteID,
  input  [15:0] io_NodeReq_bits_address,
  input         io_MemReq_ready,
  output        io_MemReq_valid,
  output [15:0] io_MemReq_bits_addr,
  output [7:0]  io_MemReq_bits_tag,
  input         io_MemResp_valid,
  input  [15:0] io_MemResp_data,
  input         io_output_ready,
  output        io_output_valid,
  output        io_output_bits_valid,
  output [15:0] io_output_bits_RouteID,
  output [15:0] io_output_bits_data,
  output        io_free
);
  reg  ID;
  reg [31:0] _RAND_0;
  wire [15:0] _T_29_RouteID;
  reg [15:0] request_R_RouteID;
  reg [31:0] _RAND_1;
  reg  request_valid_R;
  reg [31:0] _RAND_2;
  reg [15:0] ReqAddress;
  reg [31:0] _RAND_3;
  reg [1:0] outptr;
  reg [31:0] _RAND_4;
  reg [1:0] sendptr;
  reg [31:0] _RAND_5;
  reg [1:0] recvptr;
  reg [31:0] _RAND_6;
  wire [15:0] _T_53_0;
  wire [15:0] _T_53_1;
  wire [15:0] _T_53_2;
  reg [15:0] linebuffer_0;
  reg [31:0] _RAND_7;
  reg [15:0] linebuffer_1;
  reg [31:0] _RAND_8;
  reg [15:0] linebuffer_2;
  reg [31:0] _RAND_9;
  wire  _T_83;
  wire  _T_88;
  wire [14:0] _T_90;
  wire [15:0] _GEN_34;
  wire [15:0] _T_91;
  wire [15:0] _GEN_5;
  wire  _GEN_6;
  wire [15:0] _GEN_7;
  wire  _T_93;
  wire  _T_96;
  wire [2:0] _T_98;
  wire [15:0] _GEN_35;
  wire [16:0] _T_99;
  wire [15:0] _T_100;
  wire  _T_102;
  wire [2:0] _T_104;
  wire [1:0] _T_105;
  wire [1:0] _GEN_8;
  wire [1:0] _GEN_12;
  wire [15:0] _GEN_0;
  wire [15:0] _GEN_13;
  wire [15:0] _GEN_14;
  wire [15:0] _GEN_15;
  wire [2:0] _T_112;
  wire [1:0] _T_113;
  wire [15:0] _GEN_16;
  wire [15:0] _GEN_17;
  wire [15:0] _GEN_18;
  wire [1:0] _GEN_19;
  wire  _T_114;
  wire [15:0] _GEN_1;
  wire [15:0] _GEN_20;
  wire [15:0] _GEN_21;
  wire  _T_119;
  wire  _T_121;
  wire  _T_122;
  wire [2:0] _T_124;
  wire [1:0] _T_125;
  wire [1:0] _GEN_22;
  wire  _T_128;
  wire  _T_129;
  wire  _T_131;
  wire  _T_132;
  wire [1:0] _GEN_23;
  wire [1:0] _GEN_24;
  wire [1:0] _GEN_25;
  wire  _GEN_26;
  wire [1:0] _GEN_30;
  wire [1:0] _GEN_31;
  wire [1:0] _GEN_32;
  wire  _GEN_33;
  assign io_NodeReq_ready = _T_83;
  assign io_MemReq_valid = _T_96;
  assign io_MemReq_bits_addr = _T_100;
  assign io_MemReq_bits_tag = {{7'd0}, ID};
  assign io_output_valid = _T_114;
  assign io_output_bits_valid = 1'h0;
  assign io_output_bits_RouteID = request_R_RouteID;
  assign io_output_bits_data = _GEN_1;
  assign io_free = _T_83;
  assign _T_29_RouteID = 16'h0;
  assign _T_53_0 = 16'h0;
  assign _T_53_1 = 16'h0;
  assign _T_53_2 = 16'h0;
  assign _T_83 = ~ request_valid_R;
  assign _T_88 = io_NodeReq_ready & io_NodeReq_valid;
  assign _T_90 = io_NodeReq_bits_address[15:1];
  assign _GEN_34 = {{1'd0}, _T_90};
  assign _T_91 = _GEN_34 << 1;
  assign _GEN_5 = _T_88 ? io_NodeReq_bits_RouteID : request_R_RouteID;
  assign _GEN_6 = _T_88 ? 1'h1 : request_valid_R;
  assign _GEN_7 = _T_88 ? _T_91 : ReqAddress;
  assign _T_93 = sendptr != 2'h2;
  assign _T_96 = _T_93 & request_valid_R;
  assign _T_98 = {sendptr,1'h0};
  assign _GEN_35 = {{13'd0}, _T_98};
  assign _T_99 = ReqAddress + _GEN_35;
  assign _T_100 = _T_99[15:0];
  assign _T_102 = io_MemReq_ready & io_MemReq_valid;
  assign _T_104 = sendptr + 2'h1;
  assign _T_105 = _T_104[1:0];
  assign _GEN_8 = _T_102 ? _T_105 : sendptr;
  assign _GEN_12 = _T_96 ? _GEN_8 : sendptr;
  assign _GEN_0 = io_MemResp_data;
  assign _GEN_13 = 2'h0 == recvptr ? _GEN_0 : linebuffer_0;
  assign _GEN_14 = 2'h1 == recvptr ? _GEN_0 : linebuffer_1;
  assign _GEN_15 = 2'h2 == recvptr ? _GEN_0 : linebuffer_2;
  assign _T_112 = recvptr + 2'h1;
  assign _T_113 = _T_112[1:0];
  assign _GEN_16 = io_MemResp_valid ? _GEN_13 : linebuffer_0;
  assign _GEN_17 = io_MemResp_valid ? _GEN_14 : linebuffer_1;
  assign _GEN_18 = io_MemResp_valid ? _GEN_15 : linebuffer_2;
  assign _GEN_19 = io_MemResp_valid ? _T_113 : recvptr;
  assign _T_114 = outptr != recvptr;
  assign _GEN_1 = _GEN_21;
  assign _GEN_20 = 2'h1 == outptr ? linebuffer_1 : linebuffer_0;
  assign _GEN_21 = 2'h2 == outptr ? linebuffer_2 : _GEN_20;
  assign _T_119 = io_output_ready & io_output_valid;
  assign _T_121 = outptr != 2'h1;
  assign _T_122 = _T_119 & _T_121;
  assign _T_124 = outptr + 2'h1;
  assign _T_125 = _T_124[1:0];
  assign _GEN_22 = _T_122 ? _T_125 : outptr;
  assign _T_128 = outptr == 2'h1;
  assign _T_129 = _T_119 & _T_128;
  assign _T_131 = _T_122 == 1'h0;
  assign _T_132 = _T_131 & _T_129;
  assign _GEN_23 = _T_132 ? 2'h0 : _GEN_22;
  assign _GEN_24 = _T_132 ? 2'h0 : _GEN_12;
  assign _GEN_25 = _T_132 ? 2'h0 : _GEN_19;
  assign _GEN_26 = _T_132 ? 1'h0 : _GEN_6;
  assign _GEN_30 = _T_114 ? _GEN_23 : outptr;
  assign _GEN_31 = _T_114 ? _GEN_24 : _GEN_12;
  assign _GEN_32 = _T_114 ? _GEN_25 : _GEN_19;
  assign _GEN_33 = _T_114 ? _GEN_26 : _GEN_6;
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  ID = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  request_R_RouteID = _RAND_1[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  request_valid_R = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{$random}};
  ReqAddress = _RAND_3[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{$random}};
  outptr = _RAND_4[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{$random}};
  sendptr = _RAND_5[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{$random}};
  recvptr = _RAND_6[1:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{$random}};
  linebuffer_0 = _RAND_7[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{$random}};
  linebuffer_1 = _RAND_8[15:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{$random}};
  linebuffer_2 = _RAND_9[15:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      ID <= 1'h1;
    end
    if (reset) begin
      request_R_RouteID <= _T_29_RouteID;
    end else begin
      if (_T_88) begin
        request_R_RouteID <= io_NodeReq_bits_RouteID;
      end
    end
    if (reset) begin
      request_valid_R <= 1'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          request_valid_R <= 1'h0;
        end else begin
          if (_T_88) begin
            request_valid_R <= 1'h1;
          end
        end
      end else begin
        if (_T_88) begin
          request_valid_R <= 1'h1;
        end
      end
    end
    if (reset) begin
      ReqAddress <= 16'h0;
    end else begin
      if (_T_88) begin
        ReqAddress <= _T_91;
      end
    end
    if (reset) begin
      outptr <= 2'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          outptr <= 2'h0;
        end else begin
          if (_T_122) begin
            outptr <= _T_125;
          end
        end
      end
    end
    if (reset) begin
      sendptr <= 2'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          sendptr <= 2'h0;
        end else begin
          if (_T_96) begin
            if (_T_102) begin
              sendptr <= _T_105;
            end
          end
        end
      end else begin
        if (_T_96) begin
          if (_T_102) begin
            sendptr <= _T_105;
          end
        end
      end
    end
    if (reset) begin
      recvptr <= 2'h0;
    end else begin
      if (_T_114) begin
        if (_T_132) begin
          recvptr <= 2'h0;
        end else begin
          if (io_MemResp_valid) begin
            recvptr <= _T_113;
          end
        end
      end else begin
        if (io_MemResp_valid) begin
          recvptr <= _T_113;
        end
      end
    end
    if (reset) begin
      linebuffer_0 <= _T_53_0;
    end else begin
      if (io_MemResp_valid) begin
        if (2'h0 == recvptr) begin
          linebuffer_0 <= _GEN_0;
        end
      end
    end
    if (reset) begin
      linebuffer_1 <= _T_53_1;
    end else begin
      if (io_MemResp_valid) begin
        if (2'h1 == recvptr) begin
          linebuffer_1 <= _GEN_0;
        end
      end
    end
    if (reset) begin
      linebuffer_2 <= _T_53_2;
    end else begin
      if (io_MemResp_valid) begin
        if (2'h2 == recvptr) begin
          linebuffer_2 <= _GEN_0;
        end
      end
    end
  end
endmodule
module ReadTypMemoryController(
  input         clock,
  input         reset,
  output        io_ReadIn_0_ready,
  input         io_ReadIn_0_valid,
  input  [15:0] io_ReadIn_0_bits_RouteID,
  input  [15:0] io_ReadIn_0_bits_address,
  output        io_ReadIn_1_ready,
  input         io_ReadIn_1_valid,
  input  [15:0] io_ReadIn_1_bits_RouteID,
  input  [15:0] io_ReadIn_1_bits_address,
  output        io_ReadOut_0_valid,
  output [15:0] io_ReadOut_0_RouteID,
  output [15:0] io_ReadOut_0_data,
  output        io_ReadOut_1_valid,
  output [15:0] io_ReadOut_1_RouteID,
  output [15:0] io_ReadOut_1_data,
  input         io_CacheReq_ready,
  output        io_CacheReq_valid,
  output [15:0] io_CacheReq_bits_addr,
  output [7:0]  io_CacheReq_bits_tag,
  input         io_CacheResp_valid,
  input         io_CacheResp_bits_valid,
  input  [15:0] io_CacheResp_bits_data,
  input  [7:0]  io_CacheResp_bits_tag
);
  wire  in_arb_clock;
  wire  in_arb_io_in_0_ready;
  wire  in_arb_io_in_0_valid;
  wire [15:0] in_arb_io_in_0_bits_RouteID;
  wire [15:0] in_arb_io_in_0_bits_address;
  wire  in_arb_io_in_1_ready;
  wire  in_arb_io_in_1_valid;
  wire [15:0] in_arb_io_in_1_bits_RouteID;
  wire [15:0] in_arb_io_in_1_bits_address;
  wire  in_arb_io_out_ready;
  wire  in_arb_io_out_valid;
  wire [15:0] in_arb_io_out_bits_RouteID;
  wire [15:0] in_arb_io_out_bits_address;
  wire  alloc_arb_io_in_0_ready;
  wire  alloc_arb_io_in_0_valid;
  wire  alloc_arb_io_in_1_ready;
  wire  alloc_arb_io_in_1_valid;
  wire  alloc_arb_io_out_ready;
  wire  alloc_arb_io_out_valid;
  wire  cachereq_arb_clock;
  wire  cachereq_arb_reset;
  wire  cachereq_arb_io_in_0_ready;
  wire  cachereq_arb_io_in_0_valid;
  wire [15:0] cachereq_arb_io_in_0_bits_addr;
  wire [7:0] cachereq_arb_io_in_0_bits_tag;
  wire  cachereq_arb_io_in_1_ready;
  wire  cachereq_arb_io_in_1_valid;
  wire [15:0] cachereq_arb_io_in_1_bits_addr;
  wire [7:0] cachereq_arb_io_in_1_bits_tag;
  wire  cachereq_arb_io_out_ready;
  wire  cachereq_arb_io_out_valid;
  wire [15:0] cachereq_arb_io_out_bits_addr;
  wire [7:0] cachereq_arb_io_out_bits_tag;
  wire  cachereq_arb_io_chosen;
  wire  cacheresp_demux_io_en;
  wire  cacheresp_demux_io_input_valid;
  wire [15:0] cacheresp_demux_io_input_data;
  wire  cacheresp_demux_io_sel;
  wire  cacheresp_demux_io_outputs_0_valid;
  wire [15:0] cacheresp_demux_io_outputs_0_data;
  wire  cacheresp_demux_io_outputs_1_valid;
  wire [15:0] cacheresp_demux_io_outputs_1_data;
  wire  out_arb_clock;
  wire  out_arb_reset;
  wire  out_arb_io_in_0_ready;
  wire  out_arb_io_in_0_valid;
  wire  out_arb_io_in_0_bits_valid;
  wire [15:0] out_arb_io_in_0_bits_RouteID;
  wire [15:0] out_arb_io_in_0_bits_data;
  wire  out_arb_io_in_1_ready;
  wire  out_arb_io_in_1_valid;
  wire  out_arb_io_in_1_bits_valid;
  wire [15:0] out_arb_io_in_1_bits_RouteID;
  wire [15:0] out_arb_io_in_1_bits_data;
  wire  out_arb_io_out_ready;
  wire  out_arb_io_out_valid;
  wire  out_arb_io_out_bits_valid;
  wire [15:0] out_arb_io_out_bits_RouteID;
  wire [15:0] out_arb_io_out_bits_data;
  wire  out_arb_io_chosen;
  wire  out_demux_io_outputs_0_valid;
  wire [15:0] out_demux_io_outputs_0_RouteID;
  wire [15:0] out_demux_io_outputs_0_data;
  wire  out_demux_io_outputs_1_valid;
  wire [15:0] out_demux_io_outputs_1_RouteID;
  wire [15:0] out_demux_io_outputs_1_data;
  wire  out_demux_io_input_valid;
  wire [15:0] out_demux_io_input_RouteID;
  wire [15:0] out_demux_io_input_data;
  wire  out_demux_io_enable;
  wire  ReadTable_0_clock;
  wire  ReadTable_0_reset;
  wire  ReadTable_0_io_NodeReq_ready;
  wire  ReadTable_0_io_NodeReq_valid;
  wire [15:0] ReadTable_0_io_NodeReq_bits_RouteID;
  wire [15:0] ReadTable_0_io_NodeReq_bits_address;
  wire  ReadTable_0_io_MemReq_ready;
  wire  ReadTable_0_io_MemReq_valid;
  wire [15:0] ReadTable_0_io_MemReq_bits_addr;
  wire [7:0] ReadTable_0_io_MemReq_bits_tag;
  wire  ReadTable_0_io_MemResp_valid;
  wire [15:0] ReadTable_0_io_MemResp_data;
  wire  ReadTable_0_io_output_ready;
  wire  ReadTable_0_io_output_valid;
  wire  ReadTable_0_io_output_bits_valid;
  wire [15:0] ReadTable_0_io_output_bits_RouteID;
  wire [15:0] ReadTable_0_io_output_bits_data;
  wire  ReadTable_0_io_free;
  wire  ReadTable_1_clock;
  wire  ReadTable_1_reset;
  wire  ReadTable_1_io_NodeReq_ready;
  wire  ReadTable_1_io_NodeReq_valid;
  wire [15:0] ReadTable_1_io_NodeReq_bits_RouteID;
  wire [15:0] ReadTable_1_io_NodeReq_bits_address;
  wire  ReadTable_1_io_MemReq_ready;
  wire  ReadTable_1_io_MemReq_valid;
  wire [15:0] ReadTable_1_io_MemReq_bits_addr;
  wire [7:0] ReadTable_1_io_MemReq_bits_tag;
  wire  ReadTable_1_io_MemResp_valid;
  wire [15:0] ReadTable_1_io_MemResp_data;
  wire  ReadTable_1_io_output_ready;
  wire  ReadTable_1_io_output_valid;
  wire  ReadTable_1_io_output_bits_valid;
  wire [15:0] ReadTable_1_io_output_bits_RouteID;
  wire [15:0] ReadTable_1_io_output_bits_data;
  wire  ReadTable_1_io_free;
  wire  _T_41;
  ArbiterTree_1 in_arb (
    .clock(in_arb_clock),
    .io_in_0_ready(in_arb_io_in_0_ready),
    .io_in_0_valid(in_arb_io_in_0_valid),
    .io_in_0_bits_RouteID(in_arb_io_in_0_bits_RouteID),
    .io_in_0_bits_address(in_arb_io_in_0_bits_address),
    .io_in_1_ready(in_arb_io_in_1_ready),
    .io_in_1_valid(in_arb_io_in_1_valid),
    .io_in_1_bits_RouteID(in_arb_io_in_1_bits_RouteID),
    .io_in_1_bits_address(in_arb_io_in_1_bits_address),
    .io_out_ready(in_arb_io_out_ready),
    .io_out_valid(in_arb_io_out_valid),
    .io_out_bits_RouteID(in_arb_io_out_bits_RouteID),
    .io_out_bits_address(in_arb_io_out_bits_address)
  );
  Arbiter alloc_arb (
    .io_in_0_ready(alloc_arb_io_in_0_ready),
    .io_in_0_valid(alloc_arb_io_in_0_valid),
    .io_in_1_ready(alloc_arb_io_in_1_ready),
    .io_in_1_valid(alloc_arb_io_in_1_valid),
    .io_out_ready(alloc_arb_io_out_ready),
    .io_out_valid(alloc_arb_io_out_valid)
  );
  LockingRRArbiter_3 cachereq_arb (
    .clock(cachereq_arb_clock),
    .reset(cachereq_arb_reset),
    .io_in_0_ready(cachereq_arb_io_in_0_ready),
    .io_in_0_valid(cachereq_arb_io_in_0_valid),
    .io_in_0_bits_addr(cachereq_arb_io_in_0_bits_addr),
    .io_in_0_bits_tag(cachereq_arb_io_in_0_bits_tag),
    .io_in_1_ready(cachereq_arb_io_in_1_ready),
    .io_in_1_valid(cachereq_arb_io_in_1_valid),
    .io_in_1_bits_addr(cachereq_arb_io_in_1_bits_addr),
    .io_in_1_bits_tag(cachereq_arb_io_in_1_bits_tag),
    .io_out_ready(cachereq_arb_io_out_ready),
    .io_out_valid(cachereq_arb_io_out_valid),
    .io_out_bits_addr(cachereq_arb_io_out_bits_addr),
    .io_out_bits_tag(cachereq_arb_io_out_bits_tag),
    .io_chosen(cachereq_arb_io_chosen)
  );
  Demux_2 cacheresp_demux (
    .io_en(cacheresp_demux_io_en),
    .io_input_valid(cacheresp_demux_io_input_valid),
    .io_input_data(cacheresp_demux_io_input_data),
    .io_sel(cacheresp_demux_io_sel),
    .io_outputs_0_valid(cacheresp_demux_io_outputs_0_valid),
    .io_outputs_0_data(cacheresp_demux_io_outputs_0_data),
    .io_outputs_1_valid(cacheresp_demux_io_outputs_1_valid),
    .io_outputs_1_data(cacheresp_demux_io_outputs_1_data)
  );
  LockingRRArbiter_4 out_arb (
    .clock(out_arb_clock),
    .reset(out_arb_reset),
    .io_in_0_ready(out_arb_io_in_0_ready),
    .io_in_0_valid(out_arb_io_in_0_valid),
    .io_in_0_bits_valid(out_arb_io_in_0_bits_valid),
    .io_in_0_bits_RouteID(out_arb_io_in_0_bits_RouteID),
    .io_in_0_bits_data(out_arb_io_in_0_bits_data),
    .io_in_1_ready(out_arb_io_in_1_ready),
    .io_in_1_valid(out_arb_io_in_1_valid),
    .io_in_1_bits_valid(out_arb_io_in_1_bits_valid),
    .io_in_1_bits_RouteID(out_arb_io_in_1_bits_RouteID),
    .io_in_1_bits_data(out_arb_io_in_1_bits_data),
    .io_out_ready(out_arb_io_out_ready),
    .io_out_valid(out_arb_io_out_valid),
    .io_out_bits_valid(out_arb_io_out_bits_valid),
    .io_out_bits_RouteID(out_arb_io_out_bits_RouteID),
    .io_out_bits_data(out_arb_io_out_bits_data),
    .io_chosen(out_arb_io_chosen)
  );
  DeMuxTree_1 out_demux (
    .io_outputs_0_valid(out_demux_io_outputs_0_valid),
    .io_outputs_0_RouteID(out_demux_io_outputs_0_RouteID),
    .io_outputs_0_data(out_demux_io_outputs_0_data),
    .io_outputs_1_valid(out_demux_io_outputs_1_valid),
    .io_outputs_1_RouteID(out_demux_io_outputs_1_RouteID),
    .io_outputs_1_data(out_demux_io_outputs_1_data),
    .io_input_valid(out_demux_io_input_valid),
    .io_input_RouteID(out_demux_io_input_RouteID),
    .io_input_data(out_demux_io_input_data),
    .io_enable(out_demux_io_enable)
  );
  ReadTypTableEntry ReadTable_0 (
    .clock(ReadTable_0_clock),
    .reset(ReadTable_0_reset),
    .io_NodeReq_ready(ReadTable_0_io_NodeReq_ready),
    .io_NodeReq_valid(ReadTable_0_io_NodeReq_valid),
    .io_NodeReq_bits_RouteID(ReadTable_0_io_NodeReq_bits_RouteID),
    .io_NodeReq_bits_address(ReadTable_0_io_NodeReq_bits_address),
    .io_MemReq_ready(ReadTable_0_io_MemReq_ready),
    .io_MemReq_valid(ReadTable_0_io_MemReq_valid),
    .io_MemReq_bits_addr(ReadTable_0_io_MemReq_bits_addr),
    .io_MemReq_bits_tag(ReadTable_0_io_MemReq_bits_tag),
    .io_MemResp_valid(ReadTable_0_io_MemResp_valid),
    .io_MemResp_data(ReadTable_0_io_MemResp_data),
    .io_output_ready(ReadTable_0_io_output_ready),
    .io_output_valid(ReadTable_0_io_output_valid),
    .io_output_bits_valid(ReadTable_0_io_output_bits_valid),
    .io_output_bits_RouteID(ReadTable_0_io_output_bits_RouteID),
    .io_output_bits_data(ReadTable_0_io_output_bits_data),
    .io_free(ReadTable_0_io_free)
  );
  ReadTypTableEntry_1 ReadTable_1 (
    .clock(ReadTable_1_clock),
    .reset(ReadTable_1_reset),
    .io_NodeReq_ready(ReadTable_1_io_NodeReq_ready),
    .io_NodeReq_valid(ReadTable_1_io_NodeReq_valid),
    .io_NodeReq_bits_RouteID(ReadTable_1_io_NodeReq_bits_RouteID),
    .io_NodeReq_bits_address(ReadTable_1_io_NodeReq_bits_address),
    .io_MemReq_ready(ReadTable_1_io_MemReq_ready),
    .io_MemReq_valid(ReadTable_1_io_MemReq_valid),
    .io_MemReq_bits_addr(ReadTable_1_io_MemReq_bits_addr),
    .io_MemReq_bits_tag(ReadTable_1_io_MemReq_bits_tag),
    .io_MemResp_valid(ReadTable_1_io_MemResp_valid),
    .io_MemResp_data(ReadTable_1_io_MemResp_data),
    .io_output_ready(ReadTable_1_io_output_ready),
    .io_output_valid(ReadTable_1_io_output_valid),
    .io_output_bits_valid(ReadTable_1_io_output_bits_valid),
    .io_output_bits_RouteID(ReadTable_1_io_output_bits_RouteID),
    .io_output_bits_data(ReadTable_1_io_output_bits_data),
    .io_free(ReadTable_1_io_free)
  );
  assign io_ReadIn_0_ready = in_arb_io_in_0_ready;
  assign io_ReadIn_1_ready = in_arb_io_in_1_ready;
  assign io_ReadOut_0_valid = out_demux_io_outputs_0_valid;
  assign io_ReadOut_0_RouteID = out_demux_io_outputs_0_RouteID;
  assign io_ReadOut_0_data = out_demux_io_outputs_0_data;
  assign io_ReadOut_1_valid = out_demux_io_outputs_1_valid;
  assign io_ReadOut_1_RouteID = out_demux_io_outputs_1_RouteID;
  assign io_ReadOut_1_data = out_demux_io_outputs_1_data;
  assign io_CacheReq_valid = cachereq_arb_io_out_valid;
  assign io_CacheReq_bits_addr = cachereq_arb_io_out_bits_addr;
  assign io_CacheReq_bits_tag = cachereq_arb_io_out_bits_tag;
  assign in_arb_clock = clock;
  assign in_arb_io_in_0_valid = io_ReadIn_0_valid;
  assign in_arb_io_in_0_bits_RouteID = io_ReadIn_0_bits_RouteID;
  assign in_arb_io_in_0_bits_address = io_ReadIn_0_bits_address;
  assign in_arb_io_in_1_valid = io_ReadIn_1_valid;
  assign in_arb_io_in_1_bits_RouteID = io_ReadIn_1_bits_RouteID;
  assign in_arb_io_in_1_bits_address = io_ReadIn_1_bits_address;
  assign in_arb_io_out_ready = alloc_arb_io_out_valid;
  assign alloc_arb_io_in_0_valid = ReadTable_0_io_free;
  assign alloc_arb_io_in_1_valid = ReadTable_1_io_free;
  assign alloc_arb_io_out_ready = in_arb_io_out_valid;
  assign cachereq_arb_clock = clock;
  assign cachereq_arb_reset = reset;
  assign cachereq_arb_io_in_0_valid = ReadTable_0_io_MemReq_valid;
  assign cachereq_arb_io_in_0_bits_addr = ReadTable_0_io_MemReq_bits_addr;
  assign cachereq_arb_io_in_0_bits_tag = ReadTable_0_io_MemReq_bits_tag;
  assign cachereq_arb_io_in_1_valid = ReadTable_1_io_MemReq_valid;
  assign cachereq_arb_io_in_1_bits_addr = ReadTable_1_io_MemReq_bits_addr;
  assign cachereq_arb_io_in_1_bits_tag = ReadTable_1_io_MemReq_bits_tag;
  assign cachereq_arb_io_out_ready = io_CacheReq_ready;
  assign cacheresp_demux_io_en = io_CacheResp_valid;
  assign cacheresp_demux_io_input_valid = io_CacheResp_bits_valid;
  assign cacheresp_demux_io_input_data = io_CacheResp_bits_data;
  assign cacheresp_demux_io_sel = io_CacheResp_bits_tag[0];
  assign out_arb_clock = clock;
  assign out_arb_reset = reset;
  assign out_arb_io_in_0_valid = ReadTable_0_io_output_valid;
  assign out_arb_io_in_0_bits_valid = ReadTable_0_io_output_bits_valid;
  assign out_arb_io_in_0_bits_RouteID = ReadTable_0_io_output_bits_RouteID;
  assign out_arb_io_in_0_bits_data = ReadTable_0_io_output_bits_data;
  assign out_arb_io_in_1_valid = ReadTable_1_io_output_valid;
  assign out_arb_io_in_1_bits_valid = ReadTable_1_io_output_bits_valid;
  assign out_arb_io_in_1_bits_RouteID = ReadTable_1_io_output_bits_RouteID;
  assign out_arb_io_in_1_bits_data = ReadTable_1_io_output_bits_data;
  assign out_arb_io_out_ready = 1'h1;
  assign out_demux_io_input_valid = out_arb_io_out_bits_valid;
  assign out_demux_io_input_RouteID = out_arb_io_out_bits_RouteID;
  assign out_demux_io_input_data = out_arb_io_out_bits_data;
  assign out_demux_io_enable = _T_41;
  assign ReadTable_0_clock = clock;
  assign ReadTable_0_reset = reset;
  assign ReadTable_0_io_NodeReq_valid = alloc_arb_io_in_0_ready;
  assign ReadTable_0_io_NodeReq_bits_RouteID = in_arb_io_out_bits_RouteID;
  assign ReadTable_0_io_NodeReq_bits_address = in_arb_io_out_bits_address;
  assign ReadTable_0_io_MemReq_ready = cachereq_arb_io_in_0_ready;
  assign ReadTable_0_io_MemResp_valid = cacheresp_demux_io_outputs_0_valid;
  assign ReadTable_0_io_MemResp_data = cacheresp_demux_io_outputs_0_data;
  assign ReadTable_0_io_output_ready = out_arb_io_in_0_ready;
  assign ReadTable_1_clock = clock;
  assign ReadTable_1_reset = reset;
  assign ReadTable_1_io_NodeReq_valid = alloc_arb_io_in_1_ready;
  assign ReadTable_1_io_NodeReq_bits_RouteID = in_arb_io_out_bits_RouteID;
  assign ReadTable_1_io_NodeReq_bits_address = in_arb_io_out_bits_address;
  assign ReadTable_1_io_MemReq_ready = cachereq_arb_io_in_1_ready;
  assign ReadTable_1_io_MemResp_valid = cacheresp_demux_io_outputs_1_valid;
  assign ReadTable_1_io_MemResp_data = cacheresp_demux_io_outputs_1_data;
  assign ReadTable_1_io_output_ready = out_arb_io_in_1_ready;
  assign _T_41 = out_arb_io_out_ready & out_arb_io_out_valid;
endmodule
module TypeStackFile(
  input         clock,
  input         reset,
  output        io_WriteIn_0_ready,
  input         io_WriteIn_0_valid,
  input  [15:0] io_WriteIn_0_bits_RouteID,
  input  [5:0]  io_WriteIn_0_bits_address,
  input  [15:0] io_WriteIn_0_bits_data,
  input  [1:0]  io_WriteIn_0_bits_mask,
  input  [15:0] io_WriteIn_0_bits_node,
  input  [7:0]  io_WriteIn_0_bits_Typ,
  output        io_WriteOut_0_valid,
  output [15:0] io_WriteOut_0_RouteID,
  output        io_WriteOut_0_done,
  output        io_ReadIn_0_ready,
  input         io_ReadIn_0_valid,
  input  [15:0] io_ReadIn_0_bits_RouteID,
  input  [15:0] io_ReadIn_0_bits_address,
  input  [15:0] io_ReadIn_0_bits_node,
  input  [7:0]  io_ReadIn_0_bits_Typ,
  output        io_ReadIn_1_ready,
  input         io_ReadIn_1_valid,
  input  [15:0] io_ReadIn_1_bits_RouteID,
  input  [15:0] io_ReadIn_1_bits_address,
  input  [15:0] io_ReadIn_1_bits_node,
  input  [7:0]  io_ReadIn_1_bits_Typ,
  output        io_ReadOut_0_valid,
  output [15:0] io_ReadOut_0_RouteID,
  output [15:0] io_ReadOut_0_data,
  output        io_ReadOut_1_valid,
  output [15:0] io_ReadOut_1_RouteID,
  output [15:0] io_ReadOut_1_data
);
  wire  RegFile_clock;
  wire [1:0] RegFile_io_raddr1;
  wire [15:0] RegFile_io_rdata1;
  wire [1:0] RegFile_io_raddr2;
  wire  RegFile_io_wen;
  wire [1:0] RegFile_io_waddr;
  wire [15:0] RegFile_io_wdata;
  wire [1:0] RegFile_io_wmask;
  wire  WriteController_clock;
  wire  WriteController_reset;
  wire  WriteController_io_WriteIn_0_ready;
  wire  WriteController_io_WriteIn_0_valid;
  wire [15:0] WriteController_io_WriteIn_0_bits_RouteID;
  wire [5:0] WriteController_io_WriteIn_0_bits_address;
  wire [15:0] WriteController_io_WriteIn_0_bits_data;
  wire [1:0] WriteController_io_WriteIn_0_bits_mask;
  wire  WriteController_io_WriteOut_0_valid;
  wire [15:0] WriteController_io_WriteOut_0_RouteID;
  wire  WriteController_io_WriteOut_0_done;
  wire  WriteController_io_CacheReq_ready;
  wire  WriteController_io_CacheReq_valid;
  wire [15:0] WriteController_io_CacheReq_bits_addr;
  wire [15:0] WriteController_io_CacheReq_bits_data;
  wire [1:0] WriteController_io_CacheReq_bits_mask;
  wire  WriteController_io_CacheResp_valid;
  wire  ReadController_clock;
  wire  ReadController_reset;
  wire  ReadController_io_ReadIn_0_ready;
  wire  ReadController_io_ReadIn_0_valid;
  wire [15:0] ReadController_io_ReadIn_0_bits_RouteID;
  wire [15:0] ReadController_io_ReadIn_0_bits_address;
  wire  ReadController_io_ReadIn_1_ready;
  wire  ReadController_io_ReadIn_1_valid;
  wire [15:0] ReadController_io_ReadIn_1_bits_RouteID;
  wire [15:0] ReadController_io_ReadIn_1_bits_address;
  wire  ReadController_io_ReadOut_0_valid;
  wire [15:0] ReadController_io_ReadOut_0_RouteID;
  wire [15:0] ReadController_io_ReadOut_0_data;
  wire  ReadController_io_ReadOut_1_valid;
  wire [15:0] ReadController_io_ReadOut_1_RouteID;
  wire [15:0] ReadController_io_ReadOut_1_data;
  wire  ReadController_io_CacheReq_ready;
  wire  ReadController_io_CacheReq_valid;
  wire [15:0] ReadController_io_CacheReq_bits_addr;
  wire [7:0] ReadController_io_CacheReq_bits_tag;
  wire  ReadController_io_CacheResp_valid;
  wire  ReadController_io_CacheResp_bits_valid;
  wire [15:0] ReadController_io_CacheResp_bits_data;
  wire [7:0] ReadController_io_CacheResp_bits_tag;
  wire  _T_45;
  reg  WriteValid;
  reg [31:0] _RAND_0;
  reg [7:0] ReadReq_tag;
  reg [31:0] _RAND_1;
  wire  _T_49;
  reg  ReadValid;
  reg [31:0] _RAND_2;
  wire [1:0] _T_56;
  wire [1:0] _T_58;
  RFile RegFile (
    .clock(RegFile_clock),
    .io_raddr1(RegFile_io_raddr1),
    .io_rdata1(RegFile_io_rdata1),
    .io_raddr2(RegFile_io_raddr2),
    .io_wen(RegFile_io_wen),
    .io_waddr(RegFile_io_waddr),
    .io_wdata(RegFile_io_wdata),
    .io_wmask(RegFile_io_wmask)
  );
  WriteTypMemoryController WriteController (
    .clock(WriteController_clock),
    .reset(WriteController_reset),
    .io_WriteIn_0_ready(WriteController_io_WriteIn_0_ready),
    .io_WriteIn_0_valid(WriteController_io_WriteIn_0_valid),
    .io_WriteIn_0_bits_RouteID(WriteController_io_WriteIn_0_bits_RouteID),
    .io_WriteIn_0_bits_address(WriteController_io_WriteIn_0_bits_address),
    .io_WriteIn_0_bits_data(WriteController_io_WriteIn_0_bits_data),
    .io_WriteIn_0_bits_mask(WriteController_io_WriteIn_0_bits_mask),
    .io_WriteOut_0_valid(WriteController_io_WriteOut_0_valid),
    .io_WriteOut_0_RouteID(WriteController_io_WriteOut_0_RouteID),
    .io_WriteOut_0_done(WriteController_io_WriteOut_0_done),
    .io_CacheReq_ready(WriteController_io_CacheReq_ready),
    .io_CacheReq_valid(WriteController_io_CacheReq_valid),
    .io_CacheReq_bits_addr(WriteController_io_CacheReq_bits_addr),
    .io_CacheReq_bits_data(WriteController_io_CacheReq_bits_data),
    .io_CacheReq_bits_mask(WriteController_io_CacheReq_bits_mask),
    .io_CacheResp_valid(WriteController_io_CacheResp_valid)
  );
  ReadTypMemoryController ReadController (
    .clock(ReadController_clock),
    .reset(ReadController_reset),
    .io_ReadIn_0_ready(ReadController_io_ReadIn_0_ready),
    .io_ReadIn_0_valid(ReadController_io_ReadIn_0_valid),
    .io_ReadIn_0_bits_RouteID(ReadController_io_ReadIn_0_bits_RouteID),
    .io_ReadIn_0_bits_address(ReadController_io_ReadIn_0_bits_address),
    .io_ReadIn_1_ready(ReadController_io_ReadIn_1_ready),
    .io_ReadIn_1_valid(ReadController_io_ReadIn_1_valid),
    .io_ReadIn_1_bits_RouteID(ReadController_io_ReadIn_1_bits_RouteID),
    .io_ReadIn_1_bits_address(ReadController_io_ReadIn_1_bits_address),
    .io_ReadOut_0_valid(ReadController_io_ReadOut_0_valid),
    .io_ReadOut_0_RouteID(ReadController_io_ReadOut_0_RouteID),
    .io_ReadOut_0_data(ReadController_io_ReadOut_0_data),
    .io_ReadOut_1_valid(ReadController_io_ReadOut_1_valid),
    .io_ReadOut_1_RouteID(ReadController_io_ReadOut_1_RouteID),
    .io_ReadOut_1_data(ReadController_io_ReadOut_1_data),
    .io_CacheReq_ready(ReadController_io_CacheReq_ready),
    .io_CacheReq_valid(ReadController_io_CacheReq_valid),
    .io_CacheReq_bits_addr(ReadController_io_CacheReq_bits_addr),
    .io_CacheReq_bits_tag(ReadController_io_CacheReq_bits_tag),
    .io_CacheResp_valid(ReadController_io_CacheResp_valid),
    .io_CacheResp_bits_valid(ReadController_io_CacheResp_bits_valid),
    .io_CacheResp_bits_data(ReadController_io_CacheResp_bits_data),
    .io_CacheResp_bits_tag(ReadController_io_CacheResp_bits_tag)
  );
  assign io_WriteIn_0_ready = WriteController_io_WriteIn_0_ready;
  assign io_WriteOut_0_valid = WriteController_io_WriteOut_0_valid;
  assign io_WriteOut_0_RouteID = WriteController_io_WriteOut_0_RouteID;
  assign io_WriteOut_0_done = WriteController_io_WriteOut_0_done;
  assign io_ReadIn_0_ready = ReadController_io_ReadIn_0_ready;
  assign io_ReadIn_1_ready = ReadController_io_ReadIn_1_ready;
  assign io_ReadOut_0_valid = ReadController_io_ReadOut_0_valid;
  assign io_ReadOut_0_RouteID = ReadController_io_ReadOut_0_RouteID;
  assign io_ReadOut_0_data = ReadController_io_ReadOut_0_data;
  assign io_ReadOut_1_valid = ReadController_io_ReadOut_1_valid;
  assign io_ReadOut_1_RouteID = ReadController_io_ReadOut_1_RouteID;
  assign io_ReadOut_1_data = ReadController_io_ReadOut_1_data;
  assign RegFile_clock = clock;
  assign RegFile_io_raddr1 = _T_58;
  assign RegFile_io_raddr2 = 2'h0;
  assign RegFile_io_wen = _T_45;
  assign RegFile_io_waddr = _T_56;
  assign RegFile_io_wdata = WriteController_io_CacheReq_bits_data;
  assign RegFile_io_wmask = WriteController_io_CacheReq_bits_mask;
  assign WriteController_clock = clock;
  assign WriteController_reset = reset;
  assign WriteController_io_WriteIn_0_valid = io_WriteIn_0_valid;
  assign WriteController_io_WriteIn_0_bits_RouteID = io_WriteIn_0_bits_RouteID;
  assign WriteController_io_WriteIn_0_bits_address = io_WriteIn_0_bits_address;
  assign WriteController_io_WriteIn_0_bits_data = io_WriteIn_0_bits_data;
  assign WriteController_io_WriteIn_0_bits_mask = io_WriteIn_0_bits_mask;
  assign WriteController_io_CacheReq_ready = 1'h1;
  assign WriteController_io_CacheResp_valid = WriteValid;
  assign ReadController_clock = clock;
  assign ReadController_reset = reset;
  assign ReadController_io_ReadIn_0_valid = io_ReadIn_0_valid;
  assign ReadController_io_ReadIn_0_bits_RouteID = io_ReadIn_0_bits_RouteID;
  assign ReadController_io_ReadIn_0_bits_address = io_ReadIn_0_bits_address;
  assign ReadController_io_ReadIn_1_valid = io_ReadIn_1_valid;
  assign ReadController_io_ReadIn_1_bits_RouteID = io_ReadIn_1_bits_RouteID;
  assign ReadController_io_ReadIn_1_bits_address = io_ReadIn_1_bits_address;
  assign ReadController_io_CacheReq_ready = 1'h1;
  assign ReadController_io_CacheResp_valid = ReadValid;
  assign ReadController_io_CacheResp_bits_valid = 1'h0;
  assign ReadController_io_CacheResp_bits_data = RegFile_io_rdata1;
  assign ReadController_io_CacheResp_bits_tag = ReadReq_tag;
  assign _T_45 = WriteController_io_CacheReq_ready & WriteController_io_CacheReq_valid;
  assign _T_49 = ReadController_io_CacheReq_ready & ReadController_io_CacheReq_valid;
  assign _T_56 = WriteController_io_CacheReq_bits_addr[2:1];
  assign _T_58 = ReadController_io_CacheReq_bits_addr[2:1];
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifndef verilator
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{$random}};
  WriteValid = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{$random}};
  ReadReq_tag = _RAND_1[7:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{$random}};
  ReadValid = _RAND_2[0:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      WriteValid <= 1'h0;
    end else begin
      WriteValid <= _T_45;
    end
    ReadReq_tag <= ReadController_io_CacheReq_bits_tag;
    if (reset) begin
      ReadValid <= 1'h0;
    end else begin
      ReadValid <= _T_49;
    end
  end
endmodule

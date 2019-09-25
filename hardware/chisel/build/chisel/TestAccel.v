module VTASimShell( // @[:@27.2]
  input         clock, // @[:@28.4]
  input         reset, // @[:@29.4]
  output        host_req_valid, // @[:@30.4]
  output        host_req_opcode, // @[:@30.4]
  output [7:0]  host_req_addr, // @[:@30.4]
  output [31:0] host_req_value, // @[:@30.4]
  input         host_req_deq, // @[:@30.4]
  input         host_resp_valid, // @[:@30.4]
  input  [31:0] host_resp_bits, // @[:@30.4]
  input         mem_req_valid, // @[:@31.4]
  input         mem_req_opcode, // @[:@31.4]
  input  [63:0] mem_req_addr, // @[:@31.4]
  input         mem_wr_valid, // @[:@31.4]
  input  [63:0] mem_wr_bits, // @[:@31.4]
  input         mem_rd_ready, // @[:@31.4]
  output        mem_rd_valid, // @[:@31.4]
  output [63:0] mem_rd_bits, // @[:@31.4]
  input         sim_clock, // @[:@32.4]
  output        sim_wait // @[:@33.4]
);
  wire  mod_sim_dpi_wait; // @[TestAccel.scala 37:23:@35.4]
  wire  mod_sim_reset; // @[TestAccel.scala 37:23:@35.4]
  wire  mod_sim_clock; // @[TestAccel.scala 37:23:@35.4]
  wire  mod_host_dpi_req_valid; // @[TestAccel.scala 38:24:@39.4]
  wire  mod_host_dpi_req_opcode; // @[TestAccel.scala 38:24:@39.4]
  wire [7:0] mod_host_dpi_req_addr; // @[TestAccel.scala 38:24:@39.4]
  wire [31:0] mod_host_dpi_req_value; // @[TestAccel.scala 38:24:@39.4]
  wire  mod_host_dpi_req_deq; // @[TestAccel.scala 38:24:@39.4]
  wire  mod_host_dpi_resp_valid; // @[TestAccel.scala 38:24:@39.4]
  wire [31:0] mod_host_dpi_resp_bits; // @[TestAccel.scala 38:24:@39.4]
  wire  mod_host_reset; // @[TestAccel.scala 38:24:@39.4]
  wire  mod_host_clock; // @[TestAccel.scala 38:24:@39.4]
  wire  mod_mem_dpi_req_valid; // @[TestAccel.scala 39:23:@43.4]
  wire  mod_mem_dpi_req_opcode; // @[TestAccel.scala 39:23:@43.4]
  wire [7:0] mod_mem_dpi_req_len; // @[TestAccel.scala 39:23:@43.4]
  wire [63:0] mod_mem_dpi_req_addr; // @[TestAccel.scala 39:23:@43.4]
  wire  mod_mem_dpi_wr_valid; // @[TestAccel.scala 39:23:@43.4]
  wire [63:0] mod_mem_dpi_wr_bits; // @[TestAccel.scala 39:23:@43.4]
  wire  mod_mem_dpi_rd_ready; // @[TestAccel.scala 39:23:@43.4]
  wire  mod_mem_dpi_rd_valid; // @[TestAccel.scala 39:23:@43.4]
  wire [63:0] mod_mem_dpi_rd_bits; // @[TestAccel.scala 39:23:@43.4]
  wire  mod_mem_reset; // @[TestAccel.scala 39:23:@43.4]
  wire  mod_mem_clock; // @[TestAccel.scala 39:23:@43.4]
  VTASimDPI mod_sim ( // @[TestAccel.scala 37:23:@35.4]
    .dpi_wait(mod_sim_dpi_wait),
    .reset(mod_sim_reset),
    .clock(mod_sim_clock)
  );
  VTAHostDPI mod_host ( // @[TestAccel.scala 38:24:@39.4]
    .dpi_req_valid(mod_host_dpi_req_valid),
    .dpi_req_opcode(mod_host_dpi_req_opcode),
    .dpi_req_addr(mod_host_dpi_req_addr),
    .dpi_req_value(mod_host_dpi_req_value),
    .dpi_req_deq(mod_host_dpi_req_deq),
    .dpi_resp_valid(mod_host_dpi_resp_valid),
    .dpi_resp_bits(mod_host_dpi_resp_bits),
    .reset(mod_host_reset),
    .clock(mod_host_clock)
  );
  VTAMemDPI mod_mem ( // @[TestAccel.scala 39:23:@43.4]
    .dpi_req_valid(mod_mem_dpi_req_valid),
    .dpi_req_opcode(mod_mem_dpi_req_opcode),
    .dpi_req_len(mod_mem_dpi_req_len),
    .dpi_req_addr(mod_mem_dpi_req_addr),
    .dpi_wr_valid(mod_mem_dpi_wr_valid),
    .dpi_wr_bits(mod_mem_dpi_wr_bits),
    .dpi_rd_ready(mod_mem_dpi_rd_ready),
    .dpi_rd_valid(mod_mem_dpi_rd_valid),
    .dpi_rd_bits(mod_mem_dpi_rd_bits),
    .reset(mod_mem_reset),
    .clock(mod_mem_clock)
  );
  assign host_req_valid = mod_host_dpi_req_valid; // @[TestAccel.scala 45:8:@66.4]
  assign host_req_opcode = mod_host_dpi_req_opcode; // @[TestAccel.scala 45:8:@65.4]
  assign host_req_addr = mod_host_dpi_req_addr; // @[TestAccel.scala 45:8:@64.4]
  assign host_req_value = mod_host_dpi_req_value; // @[TestAccel.scala 45:8:@63.4]
  assign mem_rd_valid = mod_mem_dpi_rd_valid; // @[TestAccel.scala 42:18:@50.4]
  assign mem_rd_bits = mod_mem_dpi_rd_bits; // @[TestAccel.scala 42:18:@49.4]
  assign sim_wait = mod_sim_dpi_wait; // @[TestAccel.scala 48:12:@69.4]
  assign mod_sim_reset = reset; // @[TestAccel.scala 47:20:@68.4]
  assign mod_sim_clock = sim_clock; // @[TestAccel.scala 46:20:@67.4]
  assign mod_host_dpi_req_deq = host_req_deq; // @[TestAccel.scala 45:8:@62.4]
  assign mod_host_dpi_resp_valid = host_resp_valid; // @[TestAccel.scala 45:8:@61.4]
  assign mod_host_dpi_resp_bits = host_resp_bits; // @[TestAccel.scala 45:8:@60.4]
  assign mod_host_reset = reset; // @[TestAccel.scala 44:21:@59.4]
  assign mod_host_clock = clock; // @[TestAccel.scala 43:21:@58.4]
  assign mod_mem_dpi_req_valid = mem_req_valid; // @[TestAccel.scala 42:18:@57.4]
  assign mod_mem_dpi_req_opcode = mem_req_opcode; // @[TestAccel.scala 42:18:@56.4]
  assign mod_mem_dpi_req_len = 8'h0; // @[TestAccel.scala 42:18:@55.4]
  assign mod_mem_dpi_req_addr = mem_req_addr; // @[TestAccel.scala 42:18:@54.4]
  assign mod_mem_dpi_wr_valid = mem_wr_valid; // @[TestAccel.scala 42:18:@53.4]
  assign mod_mem_dpi_wr_bits = mem_wr_bits; // @[TestAccel.scala 42:18:@52.4]
  assign mod_mem_dpi_rd_ready = mem_rd_ready; // @[TestAccel.scala 42:18:@51.4]
  assign mod_mem_reset = reset; // @[TestAccel.scala 41:20:@48.4]
  assign mod_mem_clock = clock; // @[TestAccel.scala 40:20:@47.4]
endmodule
module RegFile( // @[:@71.2]
  input         clock, // @[:@72.4]
  input         reset, // @[:@73.4]
  output        io_launch, // @[:@74.4]
  input         io_finish, // @[:@74.4]
  input         io_ecnt_0_valid, // @[:@74.4]
  input  [31:0] io_ecnt_0_bits, // @[:@74.4]
  output [31:0] io_vals_0, // @[:@74.4]
  output [31:0] io_vals_1, // @[:@74.4]
  output [63:0] io_ptrs_0, // @[:@74.4]
  output [63:0] io_ptrs_1, // @[:@74.4]
  input         io_host_req_valid, // @[:@74.4]
  input         io_host_req_opcode, // @[:@74.4]
  input  [7:0]  io_host_req_addr, // @[:@74.4]
  input  [31:0] io_host_req_value, // @[:@74.4]
  output        io_host_req_deq, // @[:@74.4]
  output        io_host_resp_valid, // @[:@74.4]
  output [31:0] io_host_resp_bits // @[:@74.4]
);
  reg  state; // @[RegFile.scala 60:22:@76.4]
  reg [31:0] _RAND_0;
  wire  _T_68; // @[Conditional.scala 37:30:@77.4]
  wire  _T_70; // @[RegFile.scala 64:33:@79.6]
  wire  _T_71; // @[RegFile.scala 64:30:@80.6]
  wire  _GEN_0; // @[RegFile.scala 64:54:@81.6]
  wire  _GEN_1; // @[Conditional.scala 39:67:@87.6]
  wire  _GEN_2; // @[Conditional.scala 40:58:@78.4]
  wire  _T_73; // @[RegFile.scala 73:28:@90.4]
  wire  _T_74; // @[RegFile.scala 73:38:@91.4]
  reg [31:0] reg_0; // @[RegFile.scala 77:29:@95.4]
  reg [31:0] _RAND_1;
  reg [31:0] reg_1; // @[RegFile.scala 77:29:@98.4]
  reg [31:0] _RAND_2;
  reg [31:0] reg_2; // @[RegFile.scala 77:29:@101.4]
  reg [31:0] _RAND_3;
  reg [31:0] reg_3; // @[RegFile.scala 77:29:@104.4]
  reg [31:0] _RAND_4;
  reg [31:0] reg_4; // @[RegFile.scala 77:29:@107.4]
  reg [31:0] _RAND_5;
  reg [31:0] reg_5; // @[RegFile.scala 77:29:@110.4]
  reg [31:0] _RAND_6;
  reg [31:0] reg_6; // @[RegFile.scala 77:29:@113.4]
  reg [31:0] _RAND_7;
  reg [31:0] reg_7; // @[RegFile.scala 77:29:@116.4]
  reg [31:0] _RAND_8;
  wire  _T_126; // @[RegFile.scala 86:51:@123.6]
  wire  _T_128; // @[RegFile.scala 87:37:@124.6]
  wire  _T_129; // @[RegFile.scala 87:24:@125.6]
  wire [31:0] _GEN_3; // @[RegFile.scala 87:59:@126.6]
  wire [31:0] _GEN_4; // @[RegFile.scala 84:19:@117.4]
  wire  _T_134; // @[RegFile.scala 95:44:@136.6]
  wire  _T_135; // @[RegFile.scala 95:26:@137.6]
  wire [31:0] _GEN_5; // @[RegFile.scala 95:66:@138.6]
  wire [31:0] _GEN_6; // @[RegFile.scala 92:28:@129.4]
  wire  _T_140; // @[RegFile.scala 103:46:@144.4]
  wire  _T_141; // @[RegFile.scala 103:28:@145.4]
  wire [31:0] _GEN_7; // @[RegFile.scala 103:68:@146.4]
  wire  _T_146; // @[RegFile.scala 103:46:@152.4]
  wire  _T_147; // @[RegFile.scala 103:28:@153.4]
  wire [31:0] _GEN_8; // @[RegFile.scala 103:68:@154.4]
  wire  _T_152; // @[RegFile.scala 103:46:@160.4]
  wire  _T_153; // @[RegFile.scala 103:28:@161.4]
  wire [31:0] _GEN_9; // @[RegFile.scala 103:68:@162.4]
  wire  _T_158; // @[RegFile.scala 103:46:@168.4]
  wire  _T_159; // @[RegFile.scala 103:28:@169.4]
  wire [31:0] _GEN_10; // @[RegFile.scala 103:68:@170.4]
  wire  _T_164; // @[RegFile.scala 103:46:@176.4]
  wire  _T_165; // @[RegFile.scala 103:28:@177.4]
  wire [31:0] _GEN_11; // @[RegFile.scala 103:68:@178.4]
  wire  _T_170; // @[RegFile.scala 103:46:@184.4]
  wire  _T_171; // @[RegFile.scala 103:28:@185.4]
  wire [31:0] _GEN_12; // @[RegFile.scala 103:68:@186.4]
  reg [31:0] rdata; // @[RegFile.scala 108:22:@191.4]
  reg [31:0] _RAND_9;
  wire  _T_181; // @[RegFile.scala 109:45:@195.4]
  wire [31:0] _T_184; // @[Mux.scala 46:16:@198.6]
  wire [31:0] _T_186; // @[Mux.scala 46:16:@200.6]
  wire [31:0] _T_188; // @[Mux.scala 46:16:@202.6]
  wire [31:0] _T_190; // @[Mux.scala 46:16:@204.6]
  wire [31:0] _T_192; // @[Mux.scala 46:16:@206.6]
  wire [31:0] _T_194; // @[Mux.scala 46:16:@208.6]
  wire [31:0] _T_196; // @[Mux.scala 46:16:@210.6]
  wire [31:0] _T_198; // @[Mux.scala 46:16:@212.6]
  wire [31:0] _GEN_13; // @[RegFile.scala 109:69:@196.4]
  assign _T_68 = 1'h0 == state; // @[Conditional.scala 37:30:@77.4]
  assign _T_70 = io_host_req_opcode == 1'h0; // @[RegFile.scala 64:33:@79.6]
  assign _T_71 = io_host_req_valid & _T_70; // @[RegFile.scala 64:30:@80.6]
  assign _GEN_0 = _T_71 ? 1'h1 : state; // @[RegFile.scala 64:54:@81.6]
  assign _GEN_1 = state ? 1'h0 : state; // @[Conditional.scala 39:67:@87.6]
  assign _GEN_2 = _T_68 ? _GEN_0 : _GEN_1; // @[Conditional.scala 40:58:@78.4]
  assign _T_73 = state == 1'h0; // @[RegFile.scala 73:28:@90.4]
  assign _T_74 = _T_73 & io_host_req_valid; // @[RegFile.scala 73:38:@91.4]
  assign _T_126 = _T_74 & io_host_req_opcode; // @[RegFile.scala 86:51:@123.6]
  assign _T_128 = 8'h0 == io_host_req_addr; // @[RegFile.scala 87:37:@124.6]
  assign _T_129 = _T_126 & _T_128; // @[RegFile.scala 87:24:@125.6]
  assign _GEN_3 = _T_129 ? io_host_req_value : reg_0; // @[RegFile.scala 87:59:@126.6]
  assign _GEN_4 = io_finish ? 32'h2 : _GEN_3; // @[RegFile.scala 84:19:@117.4]
  assign _T_134 = 8'h4 == io_host_req_addr; // @[RegFile.scala 95:44:@136.6]
  assign _T_135 = _T_126 & _T_134; // @[RegFile.scala 95:26:@137.6]
  assign _GEN_5 = _T_135 ? io_host_req_value : reg_1; // @[RegFile.scala 95:66:@138.6]
  assign _GEN_6 = io_ecnt_0_valid ? io_ecnt_0_bits : _GEN_5; // @[RegFile.scala 92:28:@129.4]
  assign _T_140 = 8'h8 == io_host_req_addr; // @[RegFile.scala 103:46:@144.4]
  assign _T_141 = _T_126 & _T_140; // @[RegFile.scala 103:28:@145.4]
  assign _GEN_7 = _T_141 ? io_host_req_value : reg_2; // @[RegFile.scala 103:68:@146.4]
  assign _T_146 = 8'hc == io_host_req_addr; // @[RegFile.scala 103:46:@152.4]
  assign _T_147 = _T_126 & _T_146; // @[RegFile.scala 103:28:@153.4]
  assign _GEN_8 = _T_147 ? io_host_req_value : reg_3; // @[RegFile.scala 103:68:@154.4]
  assign _T_152 = 8'h10 == io_host_req_addr; // @[RegFile.scala 103:46:@160.4]
  assign _T_153 = _T_126 & _T_152; // @[RegFile.scala 103:28:@161.4]
  assign _GEN_9 = _T_153 ? io_host_req_value : reg_4; // @[RegFile.scala 103:68:@162.4]
  assign _T_158 = 8'h14 == io_host_req_addr; // @[RegFile.scala 103:46:@168.4]
  assign _T_159 = _T_126 & _T_158; // @[RegFile.scala 103:28:@169.4]
  assign _GEN_10 = _T_159 ? io_host_req_value : reg_5; // @[RegFile.scala 103:68:@170.4]
  assign _T_164 = 8'h18 == io_host_req_addr; // @[RegFile.scala 103:46:@176.4]
  assign _T_165 = _T_126 & _T_164; // @[RegFile.scala 103:28:@177.4]
  assign _GEN_11 = _T_165 ? io_host_req_value : reg_6; // @[RegFile.scala 103:68:@178.4]
  assign _T_170 = 8'h1c == io_host_req_addr; // @[RegFile.scala 103:46:@184.4]
  assign _T_171 = _T_126 & _T_170; // @[RegFile.scala 103:28:@185.4]
  assign _GEN_12 = _T_171 ? io_host_req_value : reg_7; // @[RegFile.scala 103:68:@186.4]
  assign _T_181 = _T_74 & _T_70; // @[RegFile.scala 109:45:@195.4]
  assign _T_184 = _T_170 ? reg_7 : 32'h0; // @[Mux.scala 46:16:@198.6]
  assign _T_186 = _T_164 ? reg_6 : _T_184; // @[Mux.scala 46:16:@200.6]
  assign _T_188 = _T_158 ? reg_5 : _T_186; // @[Mux.scala 46:16:@202.6]
  assign _T_190 = _T_152 ? reg_4 : _T_188; // @[Mux.scala 46:16:@204.6]
  assign _T_192 = _T_146 ? reg_3 : _T_190; // @[Mux.scala 46:16:@206.6]
  assign _T_194 = _T_140 ? reg_2 : _T_192; // @[Mux.scala 46:16:@208.6]
  assign _T_196 = _T_134 ? reg_1 : _T_194; // @[Mux.scala 46:16:@210.6]
  assign _T_198 = _T_128 ? reg_0 : _T_196; // @[Mux.scala 46:16:@212.6]
  assign _GEN_13 = _T_181 ? _T_198 : rdata; // @[RegFile.scala 109:69:@196.4]
  assign io_launch = reg_0[0]; // @[RegFile.scala 116:13:@219.4]
  assign io_vals_0 = reg_2; // @[RegFile.scala 119:16:@220.4]
  assign io_vals_1 = reg_3; // @[RegFile.scala 119:16:@221.4]
  assign io_ptrs_0 = {reg_5,reg_4}; // @[RegFile.scala 123:16:@223.4]
  assign io_ptrs_1 = {reg_7,reg_6}; // @[RegFile.scala 123:16:@225.4]
  assign io_host_req_deq = _T_73 & io_host_req_valid; // @[RegFile.scala 73:19:@92.4]
  assign io_host_resp_valid = state; // @[RegFile.scala 113:22:@216.4]
  assign io_host_resp_bits = rdata; // @[RegFile.scala 114:21:@217.4]
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
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  state = _RAND_0[0:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  reg_0 = _RAND_1[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {1{`RANDOM}};
  reg_1 = _RAND_2[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  reg_2 = _RAND_3[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {1{`RANDOM}};
  reg_3 = _RAND_4[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {1{`RANDOM}};
  reg_4 = _RAND_5[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_6 = {1{`RANDOM}};
  reg_5 = _RAND_6[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_7 = {1{`RANDOM}};
  reg_6 = _RAND_7[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_8 = {1{`RANDOM}};
  reg_7 = _RAND_8[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_9 = {1{`RANDOM}};
  rdata = _RAND_9[31:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      state <= 1'h0;
    end else begin
      if (_T_68) begin
        if (_T_71) begin
          state <= 1'h1;
        end
      end else begin
        if (state) begin
          state <= 1'h0;
        end
      end
    end
    if (reset) begin
      reg_0 <= 32'h0;
    end else begin
      if (io_finish) begin
        reg_0 <= 32'h2;
      end else begin
        if (_T_129) begin
          reg_0 <= io_host_req_value;
        end
      end
    end
    if (reset) begin
      reg_1 <= 32'h0;
    end else begin
      if (io_ecnt_0_valid) begin
        reg_1 <= io_ecnt_0_bits;
      end else begin
        if (_T_135) begin
          reg_1 <= io_host_req_value;
        end
      end
    end
    if (reset) begin
      reg_2 <= 32'h0;
    end else begin
      if (_T_141) begin
        reg_2 <= io_host_req_value;
      end
    end
    if (reset) begin
      reg_3 <= 32'h0;
    end else begin
      if (_T_147) begin
        reg_3 <= io_host_req_value;
      end
    end
    if (reset) begin
      reg_4 <= 32'h0;
    end else begin
      if (_T_153) begin
        reg_4 <= io_host_req_value;
      end
    end
    if (reset) begin
      reg_5 <= 32'h0;
    end else begin
      if (_T_159) begin
        reg_5 <= io_host_req_value;
      end
    end
    if (reset) begin
      reg_6 <= 32'h0;
    end else begin
      if (_T_165) begin
        reg_6 <= io_host_req_value;
      end
    end
    if (reset) begin
      reg_7 <= 32'h0;
    end else begin
      if (_T_171) begin
        reg_7 <= io_host_req_value;
      end
    end
    if (reset) begin
      rdata <= 32'h0;
    end else begin
      if (_T_181) begin
        if (_T_128) begin
          rdata <= reg_0;
        end else begin
          if (_T_134) begin
            rdata <= reg_1;
          end else begin
            if (_T_140) begin
              rdata <= reg_2;
            end else begin
              if (_T_146) begin
                rdata <= reg_3;
              end else begin
                if (_T_152) begin
                  rdata <= reg_4;
                end else begin
                  if (_T_158) begin
                    rdata <= reg_5;
                  end else begin
                    if (_T_164) begin
                      rdata <= reg_6;
                    end else begin
                      if (_T_170) begin
                        rdata <= reg_7;
                      end else begin
                        rdata <= 32'h0;
                      end
                    end
                  end
                end
              end
            end
          end
        end
      end
    end
  end
endmodule
module Compute( // @[:@227.2]
  input         clock, // @[:@228.4]
  input         reset, // @[:@229.4]
  input         io_launch, // @[:@230.4]
  output        io_finish, // @[:@230.4]
  output        io_ecnt_0_valid, // @[:@230.4]
  output [31:0] io_ecnt_0_bits, // @[:@230.4]
  input  [31:0] io_vals_0, // @[:@230.4]
  input  [31:0] io_vals_1, // @[:@230.4]
  input  [63:0] io_ptrs_0, // @[:@230.4]
  input  [63:0] io_ptrs_1, // @[:@230.4]
  output        io_mem_req_valid, // @[:@230.4]
  output        io_mem_req_opcode, // @[:@230.4]
  output [63:0] io_mem_req_addr, // @[:@230.4]
  output        io_mem_wr_valid, // @[:@230.4]
  output [63:0] io_mem_wr_bits, // @[:@230.4]
  output        io_mem_rd_ready, // @[:@230.4]
  input         io_mem_rd_valid, // @[:@230.4]
  input  [63:0] io_mem_rd_bits // @[:@230.4]
);
  reg [2:0] state; // @[Compute.scala 48:22:@232.4]
  reg [31:0] _RAND_0;
  reg [31:0] cycles; // @[Compute.scala 51:23:@233.4]
  reg [31:0] _RAND_1;
  reg [63:0] reg$; // @[Compute.scala 52:16:@234.4]
  reg [63:0] _RAND_2;
  reg [31:0] cnt; // @[Compute.scala 53:16:@235.4]
  reg [31:0] _RAND_3;
  reg [63:0] raddr; // @[Compute.scala 54:18:@236.4]
  reg [63:0] _RAND_4;
  reg [63:0] waddr; // @[Compute.scala 55:18:@237.4]
  reg [63:0] _RAND_5;
  wire  _T_80; // @[Conditional.scala 37:30:@238.4]
  wire [2:0] _GEN_0; // @[Compute.scala 59:23:@240.6]
  wire  _T_81; // @[Conditional.scala 37:30:@245.6]
  wire  _T_82; // @[Conditional.scala 37:30:@250.8]
  wire [2:0] _GEN_1; // @[Compute.scala 67:29:@252.10]
  wire  _T_83; // @[Conditional.scala 37:30:@257.10]
  wire  _T_84; // @[Conditional.scala 37:30:@262.12]
  wire [32:0] _T_86; // @[Compute.scala 75:28:@264.14]
  wire [32:0] _T_87; // @[Compute.scala 75:28:@265.14]
  wire [31:0] _T_88; // @[Compute.scala 75:28:@266.14]
  wire  _T_89; // @[Compute.scala 75:16:@267.14]
  wire [2:0] _GEN_2; // @[Compute.scala 75:36:@268.14]
  wire [2:0] _GEN_3; // @[Conditional.scala 39:67:@263.12]
  wire [2:0] _GEN_4; // @[Conditional.scala 39:67:@258.10]
  wire [2:0] _GEN_5; // @[Conditional.scala 39:67:@251.8]
  wire [2:0] _GEN_6; // @[Conditional.scala 39:67:@246.6]
  wire [2:0] _GEN_7; // @[Conditional.scala 40:58:@239.4]
  wire  _T_90; // @[Compute.scala 83:20:@275.4]
  wire  _T_96; // @[Compute.scala 86:14:@281.4]
  wire [32:0] _T_99; // @[Compute.scala 89:22:@286.6]
  wire [31:0] _T_100; // @[Compute.scala 89:22:@287.6]
  wire [31:0] _GEN_8; // @[Compute.scala 86:25:@282.4]
  wire [64:0] _T_104; // @[Compute.scala 100:20:@300.8]
  wire [63:0] _T_105; // @[Compute.scala 100:20:@301.8]
  wire [64:0] _T_107; // @[Compute.scala 101:20:@303.8]
  wire [63:0] _T_108; // @[Compute.scala 101:20:@304.8]
  wire [63:0] _GEN_9; // @[Compute.scala 99:36:@299.6]
  wire [63:0] _GEN_10; // @[Compute.scala 99:36:@299.6]
  wire  _T_109; // @[Compute.scala 105:29:@307.4]
  wire  _T_110; // @[Compute.scala 105:50:@308.4]
  wire  _T_116; // @[Compute.scala 111:14:@317.4]
  wire  _T_117; // @[Compute.scala 111:28:@318.4]
  wire [63:0] _GEN_16; // @[Compute.scala 112:27:@320.6]
  wire [64:0] _T_118; // @[Compute.scala 112:27:@320.6]
  wire [63:0] _T_119; // @[Compute.scala 112:27:@321.6]
  wire [32:0] _T_126; // @[Compute.scala 124:16:@336.8]
  wire [31:0] _T_127; // @[Compute.scala 124:16:@337.8]
  wire [31:0] _GEN_14; // @[Compute.scala 123:36:@335.6]
  assign _T_80 = 3'h0 == state; // @[Conditional.scala 37:30:@238.4]
  assign _GEN_0 = io_launch ? 3'h1 : state; // @[Compute.scala 59:23:@240.6]
  assign _T_81 = 3'h1 == state; // @[Conditional.scala 37:30:@245.6]
  assign _T_82 = 3'h2 == state; // @[Conditional.scala 37:30:@250.8]
  assign _GEN_1 = io_mem_rd_valid ? 3'h3 : state; // @[Compute.scala 67:29:@252.10]
  assign _T_83 = 3'h3 == state; // @[Conditional.scala 37:30:@257.10]
  assign _T_84 = 3'h4 == state; // @[Conditional.scala 37:30:@262.12]
  assign _T_86 = io_vals_1 - 32'h1; // @[Compute.scala 75:28:@264.14]
  assign _T_87 = $unsigned(_T_86); // @[Compute.scala 75:28:@265.14]
  assign _T_88 = _T_87[31:0]; // @[Compute.scala 75:28:@266.14]
  assign _T_89 = cnt == _T_88; // @[Compute.scala 75:16:@267.14]
  assign _GEN_2 = _T_89 ? 3'h0 : 3'h1; // @[Compute.scala 75:36:@268.14]
  assign _GEN_3 = _T_84 ? _GEN_2 : state; // @[Conditional.scala 39:67:@263.12]
  assign _GEN_4 = _T_83 ? 3'h4 : _GEN_3; // @[Conditional.scala 39:67:@258.10]
  assign _GEN_5 = _T_82 ? _GEN_1 : _GEN_4; // @[Conditional.scala 39:67:@251.8]
  assign _GEN_6 = _T_81 ? 3'h2 : _GEN_5; // @[Conditional.scala 39:67:@246.6]
  assign _GEN_7 = _T_80 ? _GEN_0 : _GEN_6; // @[Conditional.scala 40:58:@239.4]
  assign _T_90 = state == 3'h4; // @[Compute.scala 83:20:@275.4]
  assign _T_96 = state == 3'h0; // @[Compute.scala 86:14:@281.4]
  assign _T_99 = cycles + 32'h1; // @[Compute.scala 89:22:@286.6]
  assign _T_100 = cycles + 32'h1; // @[Compute.scala 89:22:@287.6]
  assign _GEN_8 = _T_96 ? 32'h0 : _T_100; // @[Compute.scala 86:25:@282.4]
  assign _T_104 = raddr + 64'h8; // @[Compute.scala 100:20:@300.8]
  assign _T_105 = raddr + 64'h8; // @[Compute.scala 100:20:@301.8]
  assign _T_107 = waddr + 64'h8; // @[Compute.scala 101:20:@303.8]
  assign _T_108 = waddr + 64'h8; // @[Compute.scala 101:20:@304.8]
  assign _GEN_9 = _T_90 ? _T_105 : raddr; // @[Compute.scala 99:36:@299.6]
  assign _GEN_10 = _T_90 ? _T_108 : waddr; // @[Compute.scala 99:36:@299.6]
  assign _T_109 = state == 3'h1; // @[Compute.scala 105:29:@307.4]
  assign _T_110 = state == 3'h3; // @[Compute.scala 105:50:@308.4]
  assign _T_116 = state == 3'h2; // @[Compute.scala 111:14:@317.4]
  assign _T_117 = _T_116 & io_mem_rd_valid; // @[Compute.scala 111:28:@318.4]
  assign _GEN_16 = {{32'd0}, io_vals_0}; // @[Compute.scala 112:27:@320.6]
  assign _T_118 = io_mem_rd_bits + _GEN_16; // @[Compute.scala 112:27:@320.6]
  assign _T_119 = io_mem_rd_bits + _GEN_16; // @[Compute.scala 112:27:@321.6]
  assign _T_126 = cnt + 32'h1; // @[Compute.scala 124:16:@336.8]
  assign _T_127 = cnt + 32'h1; // @[Compute.scala 124:16:@337.8]
  assign _GEN_14 = _T_90 ? _T_127 : cnt; // @[Compute.scala 123:36:@335.6]
  assign io_finish = _T_90 & _T_89; // @[Compute.scala 128:13:@340.4]
  assign io_ecnt_0_valid = _T_90 & _T_89; // @[Compute.scala 92:20:@290.4]
  assign io_ecnt_0_bits = cycles; // @[Compute.scala 93:19:@291.4]
  assign io_mem_req_valid = _T_109 | _T_110; // @[Compute.scala 105:20:@310.4]
  assign io_mem_req_opcode = state == 3'h3; // @[Compute.scala 106:21:@312.4]
  assign io_mem_req_addr = _T_109 ? raddr : waddr; // @[Compute.scala 108:19:@316.4]
  assign io_mem_wr_valid = state == 3'h4; // @[Compute.scala 117:19:@327.4]
  assign io_mem_wr_bits = reg$; // @[Compute.scala 118:18:@328.4]
  assign io_mem_rd_ready = state == 3'h2; // @[Compute.scala 114:19:@325.4]
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
`ifdef RANDOMIZE
  integer initvar;
  initial begin
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      #0.002 begin end
    `endif
  `ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  state = _RAND_0[2:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_1 = {1{`RANDOM}};
  cycles = _RAND_1[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_2 = {2{`RANDOM}};
  reg$ = _RAND_2[63:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_3 = {1{`RANDOM}};
  cnt = _RAND_3[31:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_4 = {2{`RANDOM}};
  raddr = _RAND_4[63:0];
  `endif // RANDOMIZE_REG_INIT
  `ifdef RANDOMIZE_REG_INIT
  _RAND_5 = {2{`RANDOM}};
  waddr = _RAND_5[63:0];
  `endif // RANDOMIZE_REG_INIT
  end
`endif // RANDOMIZE
  always @(posedge clock) begin
    if (reset) begin
      state <= 3'h0;
    end else begin
      if (_T_80) begin
        if (io_launch) begin
          state <= 3'h1;
        end
      end else begin
        if (_T_81) begin
          state <= 3'h2;
        end else begin
          if (_T_82) begin
            if (io_mem_rd_valid) begin
              state <= 3'h3;
            end
          end else begin
            if (_T_83) begin
              state <= 3'h4;
            end else begin
              if (_T_84) begin
                if (_T_89) begin
                  state <= 3'h0;
                end else begin
                  state <= 3'h1;
                end
              end
            end
          end
        end
      end
    end
    if (reset) begin
      cycles <= 32'h0;
    end else begin
      if (_T_96) begin
        cycles <= 32'h0;
      end else begin
        cycles <= _T_100;
      end
    end
    if (_T_117) begin
      reg$ <= _T_119;
    end
    if (_T_96) begin
      cnt <= 32'h0;
    end else begin
      if (_T_90) begin
        cnt <= _T_127;
      end
    end
    if (_T_96) begin
      raddr <= io_ptrs_0;
    end else begin
      if (_T_90) begin
        raddr <= _T_105;
      end
    end
    if (_T_96) begin
      waddr <= io_ptrs_1;
    end else begin
      if (_T_90) begin
        waddr <= _T_108;
      end
    end
  end
endmodule
module Accel( // @[:@342.2]
  input         clock, // @[:@343.4]
  input         reset, // @[:@344.4]
  input         io_host_req_valid, // @[:@345.4]
  input         io_host_req_opcode, // @[:@345.4]
  input  [7:0]  io_host_req_addr, // @[:@345.4]
  input  [31:0] io_host_req_value, // @[:@345.4]
  output        io_host_req_deq, // @[:@345.4]
  output        io_host_resp_valid, // @[:@345.4]
  output [31:0] io_host_resp_bits, // @[:@345.4]
  output        io_mem_req_valid, // @[:@345.4]
  output        io_mem_req_opcode, // @[:@345.4]
  output [63:0] io_mem_req_addr, // @[:@345.4]
  output        io_mem_wr_valid, // @[:@345.4]
  output [63:0] io_mem_wr_bits, // @[:@345.4]
  output        io_mem_rd_ready, // @[:@345.4]
  input         io_mem_rd_valid, // @[:@345.4]
  input  [63:0] io_mem_rd_bits // @[:@345.4]
);
  wire  rf_clock; // @[Accel.scala 53:18:@347.4]
  wire  rf_reset; // @[Accel.scala 53:18:@347.4]
  wire  rf_io_launch; // @[Accel.scala 53:18:@347.4]
  wire  rf_io_finish; // @[Accel.scala 53:18:@347.4]
  wire  rf_io_ecnt_0_valid; // @[Accel.scala 53:18:@347.4]
  wire [31:0] rf_io_ecnt_0_bits; // @[Accel.scala 53:18:@347.4]
  wire [31:0] rf_io_vals_0; // @[Accel.scala 53:18:@347.4]
  wire [31:0] rf_io_vals_1; // @[Accel.scala 53:18:@347.4]
  wire [63:0] rf_io_ptrs_0; // @[Accel.scala 53:18:@347.4]
  wire [63:0] rf_io_ptrs_1; // @[Accel.scala 53:18:@347.4]
  wire  rf_io_host_req_valid; // @[Accel.scala 53:18:@347.4]
  wire  rf_io_host_req_opcode; // @[Accel.scala 53:18:@347.4]
  wire [7:0] rf_io_host_req_addr; // @[Accel.scala 53:18:@347.4]
  wire [31:0] rf_io_host_req_value; // @[Accel.scala 53:18:@347.4]
  wire  rf_io_host_req_deq; // @[Accel.scala 53:18:@347.4]
  wire  rf_io_host_resp_valid; // @[Accel.scala 53:18:@347.4]
  wire [31:0] rf_io_host_resp_bits; // @[Accel.scala 53:18:@347.4]
  wire  ce_clock; // @[Accel.scala 54:18:@350.4]
  wire  ce_reset; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_launch; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_finish; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_ecnt_0_valid; // @[Accel.scala 54:18:@350.4]
  wire [31:0] ce_io_ecnt_0_bits; // @[Accel.scala 54:18:@350.4]
  wire [31:0] ce_io_vals_0; // @[Accel.scala 54:18:@350.4]
  wire [31:0] ce_io_vals_1; // @[Accel.scala 54:18:@350.4]
  wire [63:0] ce_io_ptrs_0; // @[Accel.scala 54:18:@350.4]
  wire [63:0] ce_io_ptrs_1; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_mem_req_valid; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_mem_req_opcode; // @[Accel.scala 54:18:@350.4]
  wire [63:0] ce_io_mem_req_addr; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_mem_wr_valid; // @[Accel.scala 54:18:@350.4]
  wire [63:0] ce_io_mem_wr_bits; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_mem_rd_ready; // @[Accel.scala 54:18:@350.4]
  wire  ce_io_mem_rd_valid; // @[Accel.scala 54:18:@350.4]
  wire [63:0] ce_io_mem_rd_bits; // @[Accel.scala 54:18:@350.4]
  RegFile rf ( // @[Accel.scala 53:18:@347.4]
    .clock(rf_clock),
    .reset(rf_reset),
    .io_launch(rf_io_launch),
    .io_finish(rf_io_finish),
    .io_ecnt_0_valid(rf_io_ecnt_0_valid),
    .io_ecnt_0_bits(rf_io_ecnt_0_bits),
    .io_vals_0(rf_io_vals_0),
    .io_vals_1(rf_io_vals_1),
    .io_ptrs_0(rf_io_ptrs_0),
    .io_ptrs_1(rf_io_ptrs_1),
    .io_host_req_valid(rf_io_host_req_valid),
    .io_host_req_opcode(rf_io_host_req_opcode),
    .io_host_req_addr(rf_io_host_req_addr),
    .io_host_req_value(rf_io_host_req_value),
    .io_host_req_deq(rf_io_host_req_deq),
    .io_host_resp_valid(rf_io_host_resp_valid),
    .io_host_resp_bits(rf_io_host_resp_bits)
  );
  Compute ce ( // @[Accel.scala 54:18:@350.4]
    .clock(ce_clock),
    .reset(ce_reset),
    .io_launch(ce_io_launch),
    .io_finish(ce_io_finish),
    .io_ecnt_0_valid(ce_io_ecnt_0_valid),
    .io_ecnt_0_bits(ce_io_ecnt_0_bits),
    .io_vals_0(ce_io_vals_0),
    .io_vals_1(ce_io_vals_1),
    .io_ptrs_0(ce_io_ptrs_0),
    .io_ptrs_1(ce_io_ptrs_1),
    .io_mem_req_valid(ce_io_mem_req_valid),
    .io_mem_req_opcode(ce_io_mem_req_opcode),
    .io_mem_req_addr(ce_io_mem_req_addr),
    .io_mem_wr_valid(ce_io_mem_wr_valid),
    .io_mem_wr_bits(ce_io_mem_wr_bits),
    .io_mem_rd_ready(ce_io_mem_rd_ready),
    .io_mem_rd_valid(ce_io_mem_rd_valid),
    .io_mem_rd_bits(ce_io_mem_rd_bits)
  );
  assign io_host_req_deq = rf_io_host_req_deq; // @[Accel.scala 55:14:@355.4]
  assign io_host_resp_valid = rf_io_host_resp_valid; // @[Accel.scala 55:14:@354.4]
  assign io_host_resp_bits = rf_io_host_resp_bits; // @[Accel.scala 55:14:@353.4]
  assign io_mem_req_valid = ce_io_mem_req_valid; // @[Accel.scala 56:10:@368.4]
  assign io_mem_req_opcode = ce_io_mem_req_opcode; // @[Accel.scala 56:10:@367.4]
  assign io_mem_req_addr = ce_io_mem_req_addr; // @[Accel.scala 56:10:@365.4]
  assign io_mem_wr_valid = ce_io_mem_wr_valid; // @[Accel.scala 56:10:@364.4]
  assign io_mem_wr_bits = ce_io_mem_wr_bits; // @[Accel.scala 56:10:@363.4]
  assign io_mem_rd_ready = ce_io_mem_rd_ready; // @[Accel.scala 56:10:@362.4]
  assign rf_clock = clock; // @[:@348.4]
  assign rf_reset = reset; // @[:@349.4]
  assign rf_io_finish = ce_io_finish; // @[Accel.scala 58:16:@370.4]
  assign rf_io_ecnt_0_valid = ce_io_ecnt_0_valid; // @[Accel.scala 59:14:@372.4]
  assign rf_io_ecnt_0_bits = ce_io_ecnt_0_bits; // @[Accel.scala 59:14:@371.4]
  assign rf_io_host_req_valid = io_host_req_valid; // @[Accel.scala 55:14:@359.4]
  assign rf_io_host_req_opcode = io_host_req_opcode; // @[Accel.scala 55:14:@358.4]
  assign rf_io_host_req_addr = io_host_req_addr; // @[Accel.scala 55:14:@357.4]
  assign rf_io_host_req_value = io_host_req_value; // @[Accel.scala 55:14:@356.4]
  assign ce_clock = clock; // @[:@351.4]
  assign ce_reset = reset; // @[:@352.4]
  assign ce_io_launch = rf_io_launch; // @[Accel.scala 57:16:@369.4]
  assign ce_io_vals_0 = rf_io_vals_0; // @[Accel.scala 60:14:@373.4]
  assign ce_io_vals_1 = rf_io_vals_1; // @[Accel.scala 60:14:@374.4]
  assign ce_io_ptrs_0 = rf_io_ptrs_0; // @[Accel.scala 61:14:@375.4]
  assign ce_io_ptrs_1 = rf_io_ptrs_1; // @[Accel.scala 61:14:@376.4]
  assign ce_io_mem_rd_valid = io_mem_rd_valid; // @[Accel.scala 56:10:@361.4]
  assign ce_io_mem_rd_bits = io_mem_rd_bits; // @[Accel.scala 56:10:@360.4]
endmodule
module TestAccel( // @[:@378.2]
  input   clock, // @[:@379.4]
  input   reset, // @[:@380.4]
  input   sim_clock, // @[:@381.4]
  output  sim_wait // @[:@382.4]
);
  wire  sim_shell_clock; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_reset; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_host_req_valid; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_host_req_opcode; // @[TestAccel.scala 59:25:@384.4]
  wire [7:0] sim_shell_host_req_addr; // @[TestAccel.scala 59:25:@384.4]
  wire [31:0] sim_shell_host_req_value; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_host_req_deq; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_host_resp_valid; // @[TestAccel.scala 59:25:@384.4]
  wire [31:0] sim_shell_host_resp_bits; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_mem_req_valid; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_mem_req_opcode; // @[TestAccel.scala 59:25:@384.4]
  wire [63:0] sim_shell_mem_req_addr; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_mem_wr_valid; // @[TestAccel.scala 59:25:@384.4]
  wire [63:0] sim_shell_mem_wr_bits; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_mem_rd_ready; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_mem_rd_valid; // @[TestAccel.scala 59:25:@384.4]
  wire [63:0] sim_shell_mem_rd_bits; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_sim_clock; // @[TestAccel.scala 59:25:@384.4]
  wire  sim_shell_sim_wait; // @[TestAccel.scala 59:25:@384.4]
  wire  vta_accel_clock; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_reset; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_host_req_valid; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_host_req_opcode; // @[TestAccel.scala 60:25:@387.4]
  wire [7:0] vta_accel_io_host_req_addr; // @[TestAccel.scala 60:25:@387.4]
  wire [31:0] vta_accel_io_host_req_value; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_host_req_deq; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_host_resp_valid; // @[TestAccel.scala 60:25:@387.4]
  wire [31:0] vta_accel_io_host_resp_bits; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_mem_req_valid; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_mem_req_opcode; // @[TestAccel.scala 60:25:@387.4]
  wire [63:0] vta_accel_io_mem_req_addr; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_mem_wr_valid; // @[TestAccel.scala 60:25:@387.4]
  wire [63:0] vta_accel_io_mem_wr_bits; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_mem_rd_ready; // @[TestAccel.scala 60:25:@387.4]
  wire  vta_accel_io_mem_rd_valid; // @[TestAccel.scala 60:25:@387.4]
  wire [63:0] vta_accel_io_mem_rd_bits; // @[TestAccel.scala 60:25:@387.4]
  VTASimShell sim_shell ( // @[TestAccel.scala 59:25:@384.4]
    .clock(sim_shell_clock),
    .reset(sim_shell_reset),
    .host_req_valid(sim_shell_host_req_valid),
    .host_req_opcode(sim_shell_host_req_opcode),
    .host_req_addr(sim_shell_host_req_addr),
    .host_req_value(sim_shell_host_req_value),
    .host_req_deq(sim_shell_host_req_deq),
    .host_resp_valid(sim_shell_host_resp_valid),
    .host_resp_bits(sim_shell_host_resp_bits),
    .mem_req_valid(sim_shell_mem_req_valid),
    .mem_req_opcode(sim_shell_mem_req_opcode),
    .mem_req_addr(sim_shell_mem_req_addr),
    .mem_wr_valid(sim_shell_mem_wr_valid),
    .mem_wr_bits(sim_shell_mem_wr_bits),
    .mem_rd_ready(sim_shell_mem_rd_ready),
    .mem_rd_valid(sim_shell_mem_rd_valid),
    .mem_rd_bits(sim_shell_mem_rd_bits),
    .sim_clock(sim_shell_sim_clock),
    .sim_wait(sim_shell_sim_wait)
  );
  Accel vta_accel ( // @[TestAccel.scala 60:25:@387.4]
    .clock(vta_accel_clock),
    .reset(vta_accel_reset),
    .io_host_req_valid(vta_accel_io_host_req_valid),
    .io_host_req_opcode(vta_accel_io_host_req_opcode),
    .io_host_req_addr(vta_accel_io_host_req_addr),
    .io_host_req_value(vta_accel_io_host_req_value),
    .io_host_req_deq(vta_accel_io_host_req_deq),
    .io_host_resp_valid(vta_accel_io_host_resp_valid),
    .io_host_resp_bits(vta_accel_io_host_resp_bits),
    .io_mem_req_valid(vta_accel_io_mem_req_valid),
    .io_mem_req_opcode(vta_accel_io_mem_req_opcode),
    .io_mem_req_addr(vta_accel_io_mem_req_addr),
    .io_mem_wr_valid(vta_accel_io_mem_wr_valid),
    .io_mem_wr_bits(vta_accel_io_mem_wr_bits),
    .io_mem_rd_ready(vta_accel_io_mem_rd_ready),
    .io_mem_rd_valid(vta_accel_io_mem_rd_valid),
    .io_mem_rd_bits(vta_accel_io_mem_rd_bits)
  );
  assign sim_wait = sim_shell_sim_wait; // @[TestAccel.scala 62:12:@391.4]
  assign sim_shell_clock = clock; // @[:@385.4]
  assign sim_shell_reset = reset; // @[:@386.4]
  assign sim_shell_host_req_deq = vta_accel_io_host_req_deq; // @[TestAccel.scala 64:21:@403.4]
  assign sim_shell_host_resp_valid = vta_accel_io_host_resp_valid; // @[TestAccel.scala 64:21:@402.4]
  assign sim_shell_host_resp_bits = vta_accel_io_host_resp_bits; // @[TestAccel.scala 64:21:@401.4]
  assign sim_shell_mem_req_valid = vta_accel_io_mem_req_valid; // @[TestAccel.scala 63:17:@400.4]
  assign sim_shell_mem_req_opcode = vta_accel_io_mem_req_opcode; // @[TestAccel.scala 63:17:@399.4]
  assign sim_shell_mem_req_addr = vta_accel_io_mem_req_addr; // @[TestAccel.scala 63:17:@397.4]
  assign sim_shell_mem_wr_valid = vta_accel_io_mem_wr_valid; // @[TestAccel.scala 63:17:@396.4]
  assign sim_shell_mem_wr_bits = vta_accel_io_mem_wr_bits; // @[TestAccel.scala 63:17:@395.4]
  assign sim_shell_mem_rd_ready = vta_accel_io_mem_rd_ready; // @[TestAccel.scala 63:17:@394.4]
  assign sim_shell_sim_clock = sim_clock; // @[TestAccel.scala 61:23:@390.4]
  assign vta_accel_clock = clock; // @[:@388.4]
  assign vta_accel_reset = reset; // @[:@389.4]
  assign vta_accel_io_host_req_valid = sim_shell_host_req_valid; // @[TestAccel.scala 64:21:@407.4]
  assign vta_accel_io_host_req_opcode = sim_shell_host_req_opcode; // @[TestAccel.scala 64:21:@406.4]
  assign vta_accel_io_host_req_addr = sim_shell_host_req_addr; // @[TestAccel.scala 64:21:@405.4]
  assign vta_accel_io_host_req_value = sim_shell_host_req_value; // @[TestAccel.scala 64:21:@404.4]
  assign vta_accel_io_mem_rd_valid = sim_shell_mem_rd_valid; // @[TestAccel.scala 63:17:@393.4]
  assign vta_accel_io_mem_rd_bits = sim_shell_mem_rd_bits; // @[TestAccel.scala 63:17:@392.4]
endmodule

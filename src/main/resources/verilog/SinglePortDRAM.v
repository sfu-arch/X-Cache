//Developed by Amirali

module SinglePortDRAM
#(
    parameter DATA = 32,
    parameter ADDR = 28
)
(
    input wire              clk,

    // Port 
    input wire              wr,
    input wire [ADDR - 1:0] addr,
    input wire [DATA - 1:0] din,
    output reg [DATA - 1:0] dout
);

    // Shared memory
    reg [DATA - 1:0] mem [(2 ** ADDR) - 1:0];

    initial $display("\nLoading DRAM ...");
    initial $readmemh("/Users/amirali/git/dataflow-lib/src/main/resources/verilog/memory_trace.mem", mem);

    integer          i;
    initial begin
        $display("rdata:");
        for (i = 0; i < 5; i = i + 1) begin
            $display("%d:%h", i, mem[i]);
        end
    end

    // Port A
    always @(posedge clk) begin
        dout <= mem[addr];
        if(wr) begin
            dout <= din;
            mem[addr] <= din;
        end
    end

endmodule

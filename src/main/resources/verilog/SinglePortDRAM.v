//Developed by Amirali

module SinglePortDRAM
#(
    parameter DATA = 32,
    parameter ADDR = 32,
    parameter MEM_SIZE = 10
)
(
    // Port
    input wire              clk,
    input wire              wr,
    input wire [ADDR - 1:0] addr,
    input wire [DATA - 1:0] din,
    output reg [DATA - 1:0] dout
);

    //memory
    reg [31 :0] mem [0 : 1024];

    initial begin
        //$display("\nLoading DRAM ...");
        $readmemh("/Users/amirali/git/dandelion-lib/src/main/resources/verilog/memory_trace.mem", mem);
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

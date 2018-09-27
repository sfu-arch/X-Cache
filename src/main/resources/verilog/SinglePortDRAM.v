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

    initial begin
        $display("Loading DRAM ...");
        $readmemh("/Users/amirali/git/dataflow-lib/test_run_dir/test18/dataflow.test18Tester11327156872/memory_trace.mem", mem);
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

// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Primary design header
//
// This header should be included by all source files instantiating the design.
// The class here is then constructed to instantiate the design.
// See the Verilator manual for examples.

#ifndef _VTestAccel_H_
#define _VTestAccel_H_

#include "verilated.h"
#include "VTestAccel__Dpi.h"

class VTestAccel__Syms;

//----------

VL_MODULE(VTestAccel) {
  public:
    
    // PORTS
    // The application code writes and reads these signals to
    // propagate new values into/out from the Verilated model.
    // Begin mtask footprint  all: 
    VL_IN8(clock,0,0);
    VL_IN8(sim_clock,0,0);
    VL_IN8(reset,0,0);
    VL_OUT8(sim_wait,0,0);
    
    // LOCAL SIGNALS
    // Internals; generally not touched by application code
    // Anonymous structures to workaround compiler member-count bugs
    struct {
	// Begin mtask footprint  all: 
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid,0,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode,0,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr,7,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_valid,0,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Freset,0,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fwait,7,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fexit,7,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__wait_reg,0,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freset,0,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_valid,7,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_opcode,7,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_addr,7,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Freset,0,0);
	VL_SIG8(TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_valid,7,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce_io_finish,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce_io_ecnt_0_valid,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT__state,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_68,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_71,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_129,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_135,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_141,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_147,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_153,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_159,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_165,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_171,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__rf__DOT___T_181,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce__DOT__state,2,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_80,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_81,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_82,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_83,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_84,0,0);
	VL_SIG8(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89,0,0);
	VL_SIG(TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value,31,0);
	VL_SIG(TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_value,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_0,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_1,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_1,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_2,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_2,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_3,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_4,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_4,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_5,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_5,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_6,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_6,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_7,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_7,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_8,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT__rdata,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_9,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_0,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__ce__DOT__cycles,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_1,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_3,31,0);
	VL_SIG(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_100,31,0);
    };
    struct {
	VL_SIG(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_127,31,0);
	VL_SIG64(TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_bits,63,0);
	VL_SIG64(TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_value,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT__reg__024,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_2,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_4,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_5,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_105,63,0);
	VL_SIG64(TestAccel__DOT__vta_accel__DOT__ce__DOT___T_108,63,0);
    };
    
    // LOCAL VARIABLES
    // Internals; generally not touched by application code
    // Begin mtask footprint  all: 
    VL_SIG8(__Vclklast__TOP__clock,0,0);
    VL_SIG8(__Vclklast__TOP__sim_clock,0,0);
    
    // INTERNAL VARIABLES
    // Internals; generally not touched by application code
    VTestAccel__Syms* __VlSymsp;  // Symbol table
    
    // PARAMETERS
    // Parameters marked /*verilator public*/ for use by application code
    
    // CONSTRUCTORS
  private:
    VL_UNCOPYABLE(VTestAccel);  ///< Copying not allowed
  public:
    /// Construct the model; called by application code
    /// The special name  may be used to make a wrapper with a
    /// single model invisible with respect to DPI scope names.
    VTestAccel(const char* name="TOP");
    /// Destroy the model; called (often implicitly) by application code
    ~VTestAccel();
    
    // API METHODS
    /// Evaluate the model.  Application must call when inputs change.
    void eval();
    /// Simulation complete, run final blocks.  Application must call on completion.
    void final();
    
    // INTERNAL METHODS
  private:
    static void _eval_initial_loop(VTestAccel__Syms* __restrict vlSymsp);
  public:
    void __Vconfigure(VTestAccel__Syms* symsp, bool first);
    void __Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI_TOP(CData& req_valid, CData& req_opcode, CData& req_addr, IData& req_value, CData req_deq, CData resp_valid, IData resp_value);
    void __Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI_TOP(CData req_valid, CData req_opcode, CData req_len, QData req_addr, CData wr_valid, QData wr_value, CData& rd_valid, QData& rd_value, CData rd_ready);
    void __Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI_TOP(CData& sim_wait, CData& sim_exit);
  private:
    static QData _change_request(VTestAccel__Syms* __restrict vlSymsp);
    static QData _change_request_1(VTestAccel__Syms* __restrict vlSymsp);
    void _ctor_var_reset();
  public:
    static void _eval(VTestAccel__Syms* __restrict vlSymsp);
  private:
#ifdef VL_DEBUG
    void _eval_debug_assertions();
#endif // VL_DEBUG
  public:
    static void _eval_initial(VTestAccel__Syms* __restrict vlSymsp);
    static void _eval_settle(VTestAccel__Syms* __restrict vlSymsp);
    static void _initial__TOP__4(VTestAccel__Syms* __restrict vlSymsp);
    static void _sequent__TOP__1(VTestAccel__Syms* __restrict vlSymsp);
    static void _sequent__TOP__2(VTestAccel__Syms* __restrict vlSymsp);
    static void _settle__TOP__3(VTestAccel__Syms* __restrict vlSymsp);
    static void _settle__TOP__5(VTestAccel__Syms* __restrict vlSymsp);
} VL_ATTR_ALIGNED(128);

#endif // guard

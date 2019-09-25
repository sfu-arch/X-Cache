// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VTestAccel.h for the primary calling header

#include "VTestAccel.h"        // For This
#include "VTestAccel__Syms.h"

#include "verilated_dpi.h"


//--------------------
// STATIC VARIABLES


//--------------------

VL_CTOR_IMP(VTestAccel) {
    VTestAccel__Syms* __restrict vlSymsp = __VlSymsp = new VTestAccel__Syms(this, name());
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Reset internal values
    
    // Reset structure values
    _ctor_var_reset();
}

void VTestAccel::__Vconfigure(VTestAccel__Syms* vlSymsp, bool first) {
    if (0 && first) {}  // Prevent unused
    this->__VlSymsp = vlSymsp;
}

VTestAccel::~VTestAccel() {
    delete __VlSymsp; __VlSymsp=NULL;
}

//--------------------
// Internal Methods

void VTestAccel::_settle__TOP__3(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_settle__TOP__3\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->sim_wait = vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__wait_reg;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_71 
	= ((IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid) 
	   & (~ (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode)));
}

void VTestAccel::_initial__TOP__4(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_initial__TOP__4\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    // INITIAL at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:554
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_0 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state 
	= (7U & vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_0);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_1 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cycles 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_1;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_2 
	= (((QData)((IData)(VL_RANDOM_I(32))) << 0x20U) 
	   | (QData)((IData)(VL_RANDOM_I(32))));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__reg__024 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_2;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_3 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_3;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_4 
	= (((QData)((IData)(VL_RANDOM_I(32))) << 0x20U) 
	   | (QData)((IData)(VL_RANDOM_I(32))));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_4;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_5 
	= (((QData)((IData)(VL_RANDOM_I(32))) << 0x20U) 
	   | (QData)((IData)(VL_RANDOM_I(32))));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_5;
    // INITIAL at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:252
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_0 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state 
	= (1U & vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_0);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_1 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_1;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_2 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_1 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_2;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_3 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_2 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_3;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_4 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_4;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_5 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_4 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_5;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_6 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_5 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_6;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_7 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_6 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_7;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_8 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_7 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_8;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_9 
	= VL_RANDOM_I(32);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__rdata 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_9;
}

void VTestAccel::_settle__TOP__5(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_settle__TOP__5\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_100 
	= ((IData)(1U) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cycles);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_105 
	= (VL_ULL(8) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_108 
	= (VL_ULL(8) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_127 
	= ((IData)(1U) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_80 
	= (0U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_81 
	= (1U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_82 
	= (2U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_83 
	= (3U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_84 
	= (4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89 
	= (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt 
	   == (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3 
	       - (IData)(1U)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_68 
	= (1U & (~ (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74 
	= ((~ (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state)) 
	   & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce_io_finish 
	= ((4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)) 
	   & (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce_io_ecnt_0_valid 
	= ((4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)) 
	   & (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_181 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74) 
	   & (~ (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74) 
	   & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_129 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (0U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_135 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (4U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_141 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (8U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_147 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (0xcU == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_153 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (0x10U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_159 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (0x14U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_165 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (0x18U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_171 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (0x1cU == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
}

void VTestAccel::_eval_initial(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_eval_initial\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->__Vclklast__TOP__clock = vlTOPp->clock;
    vlTOPp->__Vclklast__TOP__sim_clock = vlTOPp->sim_clock;
    vlTOPp->_initial__TOP__4(vlSymsp);
}

void VTestAccel::final() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::final\n"); );
    // Variables
    VTestAccel__Syms* __restrict vlSymsp = this->__VlSymsp;
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
}

void VTestAccel::_eval_settle(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_eval_settle\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->_settle__TOP__3(vlSymsp);
    vlTOPp->_settle__TOP__5(vlSymsp);
}

void VTestAccel::_ctor_var_reset() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_ctor_var_reset\n"); );
    // Body
    clock = VL_RAND_RESET_I(1);
    reset = VL_RAND_RESET_I(1);
    sim_clock = VL_RAND_RESET_I(1);
    sim_wait = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value = VL_RAND_RESET_I(32);
    TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_valid = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_bits = VL_RAND_RESET_Q(64);
    TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Freset = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fwait = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fexit = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__wait_reg = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freset = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_valid = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_opcode = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_addr = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_value = VL_RAND_RESET_I(32);
    TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Freset = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_valid = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_value = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce_io_finish = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce_io_ecnt_0_valid = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__state = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_0 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_68 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_71 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_1 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_1 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_2 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_2 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_3 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_4 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_4 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_5 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_5 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_6 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_6 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_7 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_7 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_8 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_129 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_135 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_141 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_147 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_153 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_159 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_165 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_171 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__rf__DOT__rdata = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___RAND_9 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__rf__DOT___T_181 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce__DOT__state = VL_RAND_RESET_I(3);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_0 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__ce__DOT__cycles = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_1 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__ce__DOT__reg__024 = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_2 = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_3 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_4 = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___RAND_5 = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_80 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_81 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_82 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_83 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_84 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89 = VL_RAND_RESET_I(1);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_100 = VL_RAND_RESET_I(32);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_105 = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_108 = VL_RAND_RESET_Q(64);
    TestAccel__DOT__vta_accel__DOT__ce__DOT___T_127 = VL_RAND_RESET_I(32);
}

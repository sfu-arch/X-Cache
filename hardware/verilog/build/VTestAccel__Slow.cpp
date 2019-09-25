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
    vlTOPp->sim_wait = vlTOPp->TestAccel__DOT__sim__DOT__wait_reg;
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/verilog/src/RegFile.v:86
    vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__state_n = 0U;
    if (vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__state_r) {
	if (vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__state_r) {
	    vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__state_n = 0U;
	}
    } else {
	if (((IData)(vlTOPp->TestAccel__DOT__host_req_valid) 
	     & (~ (IData)(vlTOPp->TestAccel__DOT__host_req_opcode)))) {
	    vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__state_n = 1U;
	}
    }
    vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__last 
	= ((4U == (IData)(vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_r)) 
	   & (vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__cnt 
	      == (vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__rf
		  [3U] - (IData)(1U))));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/verilog/src/Compute.v:86
    vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_n = 0U;
    if ((4U & (IData)(vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_r))) {
	if ((1U & (~ ((IData)(vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_r) 
		      >> 1U)))) {
	    if ((1U & (~ (IData)(vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_r)))) {
		vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_n 
		    = ((vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__cnt 
			== (vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__rf
			    [3U] - (IData)(1U))) ? 0U
		        : 1U);
	    }
	}
    } else {
	if ((2U & (IData)(vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_r))) {
	    vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_n 
		= ((1U & (IData)(vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_r))
		    ? 4U : ((IData)(vlTOPp->TestAccel__DOT__mem_rd_valid)
			     ? 3U : 2U));
	} else {
	    if ((1U & (IData)(vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_r))) {
		vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_n = 2U;
	    } else {
		if ((1U & vlTOPp->TestAccel__DOT__accel__DOT__rf__DOT__rf
		     [0U])) {
		    vlTOPp->TestAccel__DOT__accel__DOT__comp__DOT__state_n = 1U;
		}
	    }
	}
    }
}

void VTestAccel::_eval_initial(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_eval_initial\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->__Vclklast__TOP__clock = vlTOPp->clock;
    vlTOPp->__Vclklast__TOP__sim_clock = vlTOPp->sim_clock;
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
}

void VTestAccel::_ctor_var_reset() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_ctor_var_reset\n"); );
    // Body
    clock = VL_RAND_RESET_I(1);
    reset = VL_RAND_RESET_I(1);
    sim_clock = VL_RAND_RESET_I(1);
    sim_wait = VL_RAND_RESET_I(1);
    TestAccel__DOT__host_req_valid = VL_RAND_RESET_I(1);
    TestAccel__DOT__host_req_opcode = VL_RAND_RESET_I(1);
    TestAccel__DOT__host_req_addr = VL_RAND_RESET_I(8);
    TestAccel__DOT__host_req_value = VL_RAND_RESET_I(32);
    TestAccel__DOT__mem_rd_valid = VL_RAND_RESET_I(1);
    TestAccel__DOT__mem_rd_bits = VL_RAND_RESET_Q(64);
    TestAccel__DOT__sim__DOT_____05Freset = VL_RAND_RESET_I(1);
    TestAccel__DOT__sim__DOT_____05Fwait = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim__DOT_____05Fexit = VL_RAND_RESET_I(8);
    TestAccel__DOT__sim__DOT__wait_reg = VL_RAND_RESET_I(1);
    TestAccel__DOT__host__DOT_____05Freset = VL_RAND_RESET_I(1);
    TestAccel__DOT__host__DOT_____05Freq_valid = VL_RAND_RESET_I(8);
    TestAccel__DOT__host__DOT_____05Freq_opcode = VL_RAND_RESET_I(8);
    TestAccel__DOT__host__DOT_____05Freq_addr = VL_RAND_RESET_I(8);
    TestAccel__DOT__host__DOT_____05Freq_value = VL_RAND_RESET_I(32);
    TestAccel__DOT__mem__DOT_____05Freset = VL_RAND_RESET_I(1);
    TestAccel__DOT__mem__DOT_____05Frd_valid = VL_RAND_RESET_I(8);
    TestAccel__DOT__mem__DOT_____05Frd_value = VL_RAND_RESET_Q(64);
    TestAccel__DOT__accel__DOT__rf__DOT__state_n = VL_RAND_RESET_I(1);
    TestAccel__DOT__accel__DOT__rf__DOT__state_r = VL_RAND_RESET_I(1);
    { int __Vi0=0; for (; __Vi0<8; ++__Vi0) {
	    TestAccel__DOT__accel__DOT__rf__DOT__rf[__Vi0] = VL_RAND_RESET_I(32);
    }}
    TestAccel__DOT__accel__DOT__rf__DOT__rdata = VL_RAND_RESET_I(32);
    TestAccel__DOT__accel__DOT__comp__DOT__state_n = VL_RAND_RESET_I(3);
    TestAccel__DOT__accel__DOT__comp__DOT__state_r = VL_RAND_RESET_I(3);
    TestAccel__DOT__accel__DOT__comp__DOT__cnt = VL_RAND_RESET_I(32);
    TestAccel__DOT__accel__DOT__comp__DOT__data = VL_RAND_RESET_Q(64);
    TestAccel__DOT__accel__DOT__comp__DOT__raddr = VL_RAND_RESET_Q(64);
    TestAccel__DOT__accel__DOT__comp__DOT__waddr = VL_RAND_RESET_Q(64);
    TestAccel__DOT__accel__DOT__comp__DOT__last = VL_RAND_RESET_I(1);
    TestAccel__DOT__accel__DOT__comp__DOT__cycle_counter = VL_RAND_RESET_I(32);
}

// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VTestAccel.h for the primary calling header

#include "VTestAccel.h"        // For This
#include "VTestAccel__Syms.h"

#include "verilated_dpi.h"


//--------------------


void VTestAccel::eval() {
    VL_DEBUG_IF(VL_DBG_MSGF("+++++TOP Evaluate VTestAccel::eval\n"); );
    VTestAccel__Syms* __restrict vlSymsp = this->__VlSymsp;  // Setup global symbol table
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
#ifdef VL_DEBUG
    // Debug assertions
    _eval_debug_assertions();
#endif // VL_DEBUG
    // Initialize
    if (VL_UNLIKELY(!vlSymsp->__Vm_didInit)) _eval_initial_loop(vlSymsp);
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
	VL_DEBUG_IF(VL_DBG_MSGF("+ Clock loop\n"););
	_eval(vlSymsp);
	if (VL_UNLIKELY(++__VclockLoop > 100)) {
	    // About to fail, so enable debug to see what's not settling.
	    // Note you must run make with OPT=-DVL_DEBUG for debug prints.
	    int __Vsaved_debug = Verilated::debug();
	    Verilated::debug(1);
	    __Vchange = _change_request(vlSymsp);
	    Verilated::debug(__Vsaved_debug);
	    VL_FATAL_MT(__FILE__,__LINE__,__FILE__,"Verilated model didn't converge");
	} else {
	    __Vchange = _change_request(vlSymsp);
	}
    } while (VL_UNLIKELY(__Vchange));
}

void VTestAccel::_eval_initial_loop(VTestAccel__Syms* __restrict vlSymsp) {
    vlSymsp->__Vm_didInit = true;
    _eval_initial(vlSymsp);
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
	_eval_settle(vlSymsp);
	_eval(vlSymsp);
	if (VL_UNLIKELY(++__VclockLoop > 100)) {
	    // About to fail, so enable debug to see what's not settling.
	    // Note you must run make with OPT=-DVL_DEBUG for debug prints.
	    int __Vsaved_debug = Verilated::debug();
	    Verilated::debug(1);
	    __Vchange = _change_request(vlSymsp);
	    Verilated::debug(__Vsaved_debug);
	    VL_FATAL_MT(__FILE__,__LINE__,__FILE__,"Verilated model didn't DC converge");
	} else {
	    __Vchange = _change_request(vlSymsp);
	}
    } while (VL_UNLIKELY(__Vchange));
}

//--------------------
// Internal Methods

VL_INLINE_OPT void VTestAccel::__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI_TOP(CData& sim_wait, CData& sim_exit) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI_TOP\n"); );
    // Body
    unsigned char sim_wait__Vcvt;
    unsigned char sim_exit__Vcvt;
    VTASimDPI(&sim_wait__Vcvt, &sim_exit__Vcvt);
    sim_wait = (0xffU & sim_wait__Vcvt);
    sim_exit = (0xffU & sim_exit__Vcvt);
}

VL_INLINE_OPT void VTestAccel::__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI_TOP(CData& req_valid, CData& req_opcode, CData& req_addr, IData& req_value, CData req_deq, CData resp_valid, IData resp_value) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI_TOP\n"); );
    // Body
    unsigned char req_valid__Vcvt;
    unsigned char req_opcode__Vcvt;
    unsigned char req_addr__Vcvt;
    unsigned int req_value__Vcvt;
    unsigned char req_deq__Vcvt;
    req_deq__Vcvt = req_deq;
    unsigned char resp_valid__Vcvt;
    resp_valid__Vcvt = resp_valid;
    unsigned int resp_value__Vcvt;
    resp_value__Vcvt = resp_value;
    VTAHostDPI(&req_valid__Vcvt, &req_opcode__Vcvt, &req_addr__Vcvt, &req_value__Vcvt, req_deq__Vcvt, resp_valid__Vcvt, resp_value__Vcvt);
    req_valid = (0xffU & req_valid__Vcvt);
    req_opcode = (0xffU & req_opcode__Vcvt);
    req_addr = (0xffU & req_addr__Vcvt);
    req_value = req_value__Vcvt;
}

VL_INLINE_OPT void VTestAccel::__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI_TOP(CData req_valid, CData req_opcode, CData req_len, QData req_addr, CData wr_valid, QData wr_value, CData& rd_valid, QData& rd_value, CData rd_ready) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI_TOP\n"); );
    // Body
    unsigned char req_valid__Vcvt;
    req_valid__Vcvt = req_valid;
    unsigned char req_opcode__Vcvt;
    req_opcode__Vcvt = req_opcode;
    unsigned char req_len__Vcvt;
    req_len__Vcvt = req_len;
    unsigned long long req_addr__Vcvt;
    req_addr__Vcvt = req_addr;
    unsigned char wr_valid__Vcvt;
    wr_valid__Vcvt = wr_valid;
    unsigned long long wr_value__Vcvt;
    wr_value__Vcvt = wr_value;
    unsigned char rd_valid__Vcvt;
    unsigned long long rd_value__Vcvt;
    unsigned char rd_ready__Vcvt;
    rd_ready__Vcvt = rd_ready;
    VTAMemDPI(req_valid__Vcvt, req_opcode__Vcvt, req_len__Vcvt, req_addr__Vcvt, wr_valid__Vcvt, wr_value__Vcvt, &rd_valid__Vcvt, &rd_value__Vcvt, rd_ready__Vcvt);
    rd_valid = (0xffU & rd_valid__Vcvt);
    rd_value = rd_value__Vcvt;
}

VL_INLINE_OPT void VTestAccel::_sequent__TOP__1(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_sequent__TOP__1\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Variables
    // Begin mtask footprint  all: 
    VL_SIG8(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_valid,7,0);
    VL_SIG8(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_opcode,7,0);
    VL_SIG8(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_addr,7,0);
    VL_SIG8(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI__2__rd_valid,7,0);
    VL_SIG8(__Vdly__TestAccel__DOT__vta_accel__DOT__rf__DOT__state,0,0);
    VL_SIG(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_value,31,0);
    VL_SIG64(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI__2__rd_value,63,0);
    // Body
    __Vdly__TestAccel__DOT__vta_accel__DOT__rf__DOT__state 
	= vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state;
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAMemDPI.v:88
    if (((IData)(vlTOPp->reset) | (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Freset))) {
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_valid = 0U;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_value = VL_ULL(0);
    } else {
	vlTOPp->__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI_TOP(
										((1U 
										== (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)) 
										| (3U 
										== (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))), 
										(3U 
										== (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)), 0U, 
										((1U 
										== (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))
										 ? vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr
										 : vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr), 
										(4U 
										== (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)), vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__reg__024, __Vtask_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI__2__rd_valid, __Vtask_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI__2__rd_value, 
										(2U 
										== (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)));
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_valid 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI__2__rd_valid;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_value 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_mem__DOT__VTAMemDPI__2__rd_value;
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAHostDPI.v:79
    if (((IData)(vlTOPp->reset) | (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freset))) {
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_valid = 0U;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_opcode = 0U;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_addr = 0U;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_value = 0U;
    } else {
	vlTOPp->__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI_TOP(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_valid, __Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_opcode, __Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_addr, __Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_value, 
										((~ (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state)) 
										& (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid)), (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state), vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__rdata);
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_valid 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_valid;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_opcode 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_opcode;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_addr 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_addr;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_value 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_host__DOT__VTAHostDPI__1__req_value;
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAHostDPI.v:67
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode 
	= (1U & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_opcode));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:587
    if ((0U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt = 0U;
    } else {
	if ((4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt 
		= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_127;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	__Vdly__TestAccel__DOT__vta_accel__DOT__rf__DOT__state = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_68) {
	    if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_71) {
		__Vdly__TestAccel__DOT__vta_accel__DOT__rf__DOT__state = 1U;
	    }
	} else {
	    if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state) {
		__Vdly__TestAccel__DOT__vta_accel__DOT__rf__DOT__state = 0U;
	    }
	}
    }
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state 
	= __Vdly__TestAccel__DOT__vta_accel__DOT__rf__DOT__state;
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAMemDPI.v:68
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Freset 
	= vlTOPp->reset;
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:587
    if (((2U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)) 
	 & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_valid))) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__reg__024 
	    = (vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_bits 
	       + (QData)((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_2)));
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:587
    if ((0U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr 
	    = (((QData)((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_5)) 
		<< 0x20U) | (QData)((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_4)));
    } else {
	if ((4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr 
		= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_105;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:587
    if ((0U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr 
	    = (((QData)((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_7)) 
		<< 0x20U) | (QData)((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_6)));
    } else {
	if ((4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr 
		= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_108;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAHostDPI.v:61
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freset 
	= vlTOPp->reset;
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAHostDPI.v:67
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid 
	= (1U & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_valid));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__rdata = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_181) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__rdata 
		= ((0U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
		    ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0
		    : ((4U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
		        ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_1
		        : ((8U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
			    ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_2
			    : ((0xcU == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
			        ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3
			        : ((0x10U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
				    ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_4
				    : ((0x14U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
				        ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_5
				        : ((0x18U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
					    ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_6
					    : ((0x1cU 
						== (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr))
					        ? vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_7
					        : 0U))))))));
	}
    }
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_127 
	= ((IData)(1U) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_68 
	= (1U & (~ (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state)));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAMemDPI.v:74
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_bits 
	= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_value;
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_105 
	= (VL_ULL(8) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__raddr);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_108 
	= (VL_ULL(8) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__waddr);
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_71 
	= ((IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid) 
	   & (~ (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74 
	= ((~ (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__state)) 
	   & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_valid));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_2 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_141) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_2 
		= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_4 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_153) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_4 
		= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_5 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_159) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_5 
		= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_6 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_165) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_6 
		= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_7 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_171) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_7 
		= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAHostDPI.v:67
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr 
	= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_addr;
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_147) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3 
		= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	}
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_1 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce_io_ecnt_0_valid) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_1 
		= vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cycles;
	} else {
	    if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_135) {
		vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_1 
		    = vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	    }
	}
    }
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_181 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74) 
	   & (~ (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode)));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_74) 
	   & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_opcode));
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
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:587
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cycles 
	= ((IData)(vlTOPp->reset) ? 0U : ((0U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state))
					   ? 0U : vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_100));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_100 
	= ((IData)(1U) + vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cycles);
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:587
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_80) {
	    if ((1U & vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0)) {
		vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state = 1U;
	    }
	} else {
	    if (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_81) {
		vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state = 2U;
	    } else {
		if (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_82) {
		    if (vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_valid) {
			vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state = 3U;
		    }
		} else {
		    if (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_83) {
			vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state = 4U;
		    } else {
			if (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_84) {
			    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state 
				= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89)
				    ? 0U : 1U);
			}
		    }
		}
	    }
	}
    }
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89 
	= (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__cnt 
	   == (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_3 
	       - (IData)(1U)));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAMemDPI.v:74
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem_dpi_rd_valid 
	= (1U & (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_mem__DOT_____05Frd_valid));
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
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce_io_ecnt_0_valid 
	= ((4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)) 
	   & (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/TestAccel.v:301
    if (vlTOPp->reset) {
	vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0 = 0U;
    } else {
	if (vlTOPp->TestAccel__DOT__vta_accel__DOT__ce_io_finish) {
	    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0 = 2U;
	} else {
	    if (vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_129) {
		vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT__reg_0 
		    = vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value;
	    }
	}
    }
    vlTOPp->TestAccel__DOT__vta_accel__DOT__ce_io_finish 
	= ((4U == (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT__state)) 
	   & (IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__ce__DOT___T_89));
    vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_129 
	= ((IData)(vlTOPp->TestAccel__DOT__vta_accel__DOT__rf__DOT___T_126) 
	   & (0U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_addr)));
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTAHostDPI.v:67
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host_dpi_req_value 
	= vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_host__DOT_____05Freq_value;
}

VL_INLINE_OPT void VTestAccel::_sequent__TOP__2(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_sequent__TOP__2\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Variables
    // Begin mtask footprint  all: 
    VL_SIG8(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI__0__sim_wait,7,0);
    VL_SIG8(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI__0__sim_exit,7,0);
    // Body
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTASimDPI.v:72
    if (VL_UNLIKELY((1U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fexit)))) {
	VL_FINISH_MT("/home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTASimDPI.v",74,"");
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTASimDPI.v:46
    if (((IData)(vlTOPp->reset) | (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Freset))) {
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fwait = 0U;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fexit = 0U;
    } else {
	vlTOPp->__Vdpiimwrap_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI_TOP(__Vtask_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI__0__sim_wait, __Vtask_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI__0__sim_exit);
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fwait 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI__0__sim_wait;
	vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fexit 
	    = __Vtask_TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__VTASimDPI__0__sim_exit;
    }
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTASimDPI.v:60
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__wait_reg 
	= ((~ ((IData)(vlTOPp->reset) | (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Freset))) 
	   & (1U == (IData)(vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Fwait)));
    vlTOPp->sim_wait = vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT__wait_reg;
    // ALWAYS at /home/amirali/git/dandelion-sim/hardware/chisel/build/chisel/VTASimDPI.v:41
    vlTOPp->TestAccel__DOT__sim_shell__DOT__mod_sim__DOT_____05Freset 
	= vlTOPp->reset;
}

void VTestAccel::_eval(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_eval\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    if (((IData)(vlTOPp->clock) & (~ (IData)(vlTOPp->__Vclklast__TOP__clock)))) {
	vlTOPp->_sequent__TOP__1(vlSymsp);
    }
    if (((IData)(vlTOPp->sim_clock) & (~ (IData)(vlTOPp->__Vclklast__TOP__sim_clock)))) {
	vlTOPp->_sequent__TOP__2(vlSymsp);
    }
    // Final
    vlTOPp->__Vclklast__TOP__clock = vlTOPp->clock;
    vlTOPp->__Vclklast__TOP__sim_clock = vlTOPp->sim_clock;
}

VL_INLINE_OPT QData VTestAccel::_change_request(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_change_request\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    return (vlTOPp->_change_request_1(vlSymsp));
}

VL_INLINE_OPT QData VTestAccel::_change_request_1(VTestAccel__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_change_request_1\n"); );
    VTestAccel* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    // Change detection
    QData __req = false;  // Logically a bool
    return __req;
}

#ifdef VL_DEBUG
void VTestAccel::_eval_debug_assertions() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VTestAccel::_eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((clock & 0xfeU))) {
	Verilated::overWidthError("clock");}
    if (VL_UNLIKELY((reset & 0xfeU))) {
	Verilated::overWidthError("reset");}
    if (VL_UNLIKELY((sim_clock & 0xfeU))) {
	Verilated::overWidthError("sim_clock");}
}
#endif // VL_DEBUG

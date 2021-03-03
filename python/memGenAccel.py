import numpy as np
import platform
import dsim


mainMem = np.array([i for i in range (100000)], dtype = np.uint64)
localMem =  np.array([i for i in range (100000)], dtype = np.uint64)
if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

mainMem = dsim.DArray(mainMem ,  dsim.DArray.DType.UInt32)

nVals = 10 
input_inst = np.zeros(nVals, dtype=np.uint64)
input_addr = np.array([i*4 for i in range (nVals)], dtype = np.uint64)
input_data = np.zeros(nVals, dtype=np.uint64)

vals = [list(inst) for inst in zip(input_inst, input_addr, input_data)]
vals = [item  for sublist in vals for item in sublist  ]
print(vals)
                            #        inst|addr|data
#events = dsim.sim(ptrs = [mainMem ], vars= [0, 4, 2,
 #                                    0,128,2], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

events = dsim.sim(ptrs = [mainMem ], vars= vals, debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

#print("Cycle: " + str(events[0]))

#if events[1] == test01(5,3):
#    print("Success!\nRet: " + str(events[1]))
#else:
#    print("Failed!\nRet: " + str(events[1]))

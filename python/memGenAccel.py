import numpy as np
import platform
import dsim


a = np.array([i for i in range (1000)], dtype = np.uint32)
if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

a = dsim.DArray(a ,  dsim.DArray.DType.UInt32)
                            #        inst|addr|data
events = dsim.sim(ptrs = [a ], vars= [0, 4, 2,
                                      0,32,2], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

#print("Cycle: " + str(events[0]))

#if events[1] == test01(5,3):
#    print("Success!\nRet: " + str(events[1]))
#else:
#    print("Failed!\nRet: " + str(events[1]))

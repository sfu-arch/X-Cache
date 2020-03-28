import numpy as np
import platform
import dsim


# Debug container
a = np.zeros(20)
a_s = dsim.DArray(a)

if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


events = dsim.sim(ptrs = [], debugs = [a_s], vars= [3, 5, 1], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))
print("Ret: " + str(events[1]))
print(a_s.getData())

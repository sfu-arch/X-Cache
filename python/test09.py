import numpy as np
import platform
import dsim


Type = dsim.DArray.DType

a = np.array([1, 2, 3, 4, 5])
b = np.array([1, 2, 3, 4, 5])
c = np.array([0, 0, 0, 0, 0])

a_s = dsim.DArray(a, Type.DWord)
b_s = dsim.DArray(b, Type.DWord)
c_s = dsim.DArray(c, Type.DWord)

if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


events = dsim.sim(ptrs = [a_s, b_s, c_s], vars= [  ], numRets=1, numEvents=1, hwlib = hw_lib_path)

cycle = events[0]
print("Cycle: " + str(cycle))
print(a_s.getData())
print(b_s.getData())
print(c_s.getData())

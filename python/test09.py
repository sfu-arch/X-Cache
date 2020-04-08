import numpy as np
import platform
import dsim


Type = dsim.DArray.DType

a = np.array([1, 2, 3, 4, 5])
b = np.array([1, 2, 3, 4, 5])
c = np.array([0, 0, 0, 0, 0])

a_s = dsim.DArray(a, Type.UInt64)
b_s = dsim.DArray(b, Type.UInt64)
c_s = dsim.DArray(c, Type.UInt64)

if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


events = dsim.sim(ptrs = [a_s, b_s, c_s], vars= [  ], debugs = [], numRets=1, numEvents=1, hwlib = hw_lib_path)

cycle = events[0]
print("Cycle: " + str(cycle))
print(a_s.getData_UInt64())
print(b_s.getData_UInt64())
print(c_s.getData_UInt64())

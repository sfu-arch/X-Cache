import numpy as np
import platform
import dsim


a = np.array([1, 2, 3, 4, 5])
b = np.array([0, 0, 0, 0, 0])

a_s = dsim.DArray(a)
b_s = dsim.DArray(b)

if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


cycle = dsim.sim(ptrs = [a_s, b_s], vars= [5], rets=[0], hwlib = hw_lib_path)

# print("Cycle: " + str(cycle))
print(cycle)
print(a_s.getData())
print(b_s.getData())

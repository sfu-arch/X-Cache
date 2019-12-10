import numpy as np
import dsim


a = np.array([1, 2, 3, 4, 5])

a_s = dsim.DArray(a)

hw_lib_path = "./hardware/chisel/build/libhw.so"

cycle = dsim.sim(ptrs = [a_s], vars= [5], hwlib = hw_lib_path)

print("Cycle: " + str(cycle))
print(a_s.getData())

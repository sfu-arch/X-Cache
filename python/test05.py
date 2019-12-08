import numpy as np
import dsim


a = np.array([0, 1, 2, 3, 4, 5, 0, 0 ,0 ,0])
# b = np.array([0, 1, 2, 3, 4])
# c = np.array([0, 0, 0, 0, 0])

a_s = dsim.DArray(a)
# b_s = dsim.DArray(b)
# c_s = dsim.DArray(c)

hw_lib_path = "./hardware/chisel/build/libhw.so"

cycle = dsim.sim(ptrs = [a_s], vars= [], hwlib = hw_lib_path)

print("Cycle: " + str(cycle))
print(a_s.getData())

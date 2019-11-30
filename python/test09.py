import numpy as np
import dsim


a = np.array([0, 1, 2, 3, 4])
b = np.array([0, 1, 2, 3, 4])
c = np.array([0, 0, 0, 0, 0])

a_s = dsim.DArray(a)
b_s = dsim.DArray(b)
c_s = dsim.DArray(c)

cycle = dsim.sim(ptrs = [a_s, b_s, c_s], args = [5, 5])

print("Cycle: " + str(cycle))
print(c_s.getData())

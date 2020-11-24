import os, sys
import os.path as p
sys.path.append(os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))))

from math import isclose
import common as cm
import numpy as np
import platform
import dsim

Type = dsim.DArray.DType

dir_path = os.path.dirname(__file__)

input_data = cm.read_file(p.join(dir_path,'input.data'))
check_data = cm.read_file(p.join(dir_path, 'check.data'))

"""
The implementaiton is taken from Machsuite benchmark
"""

#### Large test
# row_size = 64
# col_size = 64
# N = row_size * col_size
# block_size = 8
# NUMOFBLOCKS = N/block_size/block_size

#### Medium test -- There are three errors in the output
# row_size = 32
# col_size = 32
# N = row_size * col_size
# block_size = 4
# NUMOFBLOCKS = N/block_size/block_size


#### Small test  -- Output completly matches
row_size = 16
col_size = 16
N = row_size * col_size
block_size = 2
NUMOFBLOCKS = N/block_size/block_size





def bbgemm(m1, m2):
    prod = [0] * N
    for jj in range(0, row_size, block_size):
        for kk in range(0, row_size, block_size):
            for i in range(0, row_size):
                for k in range(0, block_size):
                    i_row = i * row_size
                    k_row = (k + kk) * row_size
                    temp_x = m1[i_row + k + kk]
                    for j in range(0, block_size):
                        mul = temp_x * m2[k_row + j + jj]
                        prod[i_row + j + jj] = prod[i_row + j + jj] + mul
    
    return prod




# Input matrix
m1_s         = dsim.DArray(np.array(input_data[0], dtype=np.float64), Type.Double)
m2_s          = dsim.DArray(np.array(input_data[1], dtype=np.float64), Type.Double)

# Output matrix
prod_s          = dsim.DArray(np.zeros(N, dtype=np.float64), Type.Double)


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

time = cm.Timer()

time.start()
# Running Simulation
events = dsim.sim(ptrs = [m1_s, m2_s, prod_s], vars= [], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)
time.stop()


# Get software result
prod = bbgemm(input_data[0], input_data[1])



# cm.dump_output('sim_prod.data', prod_s.getData_Double())
# cm.dump_output('prod.data', prod)

print("Cycle: " + str(events[0]))
print("Simulation time: " + str(time.elapsed * 10**9) + "ns")
# if len(prod) == sum([1 for i, j in zip(prod_s.getData_Double(), prod) if isclose(i,j)]):
    # print("Final output matched -- Success!")
# else:
    # print("Final output doesn't match -- Failed!")





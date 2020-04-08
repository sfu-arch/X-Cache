import os, sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))))

from math import isclose
import common as cm
import numpy as np
import platform
import dsim

Type = dsim.DArray.DType

dir_path = os.path.dirname(__file__)

input_data = cm.read_file(dir_path + '/' +'input.data')
check_data = cm.read_file(dir_path + '/' +'check.data')

"""
The implementaiton is taken from Machsuite benchmark
"""

NNZ = 1666
N = 494

def spmvCRS(val, cols, rowDelimiters, vec):
    out = [0] * N
    for i in range(0, N):
        sum = 0
        Si = 0
        tmp_begin = rowDelimiters[i]
        tmp_end = rowDelimiters[i+1]
        for j in range(tmp_begin, tmp_end):
            Si = val[j] * vec[cols[j]]
            sum = sum + Si
        out[i] = sum

    return out


# Input matrix
val_s           = dsim.DArray(np.array(input_data[0], dtype=np.float64), Type.Double)
cols_s          = dsim.DArray(np.array(input_data[1], dtype=np.uint64), Type.UInt64)
rowDelimiters_s = dsim.DArray(np.array(input_data[2], dtype=np.uint64), Type.UInt64)
vec_s           = dsim.DArray(np.array(input_data[3], dtype=np.float64), Type.Double)

# Output matrix
mat_res = np.zeros(N, dtype=np.float64)
mat_res_s = dsim.DArray(mat_res, Type.UInt64)


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


# Running Simulation
events = dsim.sim(ptrs = [val_s, cols_s, rowDelimiters_s, vec_s, mat_res_s], vars= [], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

# Get software result
res = spmvCRS(input_data[0], input_data[1], input_data[2], input_data[3])


print("Cycle: " + str(events[0]))

cm.dump_output('sim.data', mat_res_s.getData_Double())

if len(res) == sum([1 for i, j in zip(mat_res_s.getData_Double(), check_data[0]) if isclose(i,j)]):
    print("Sucess!")
else:
    print("Failed!")


#if list(b_s.getData_UInt64()) == correct_res:
  # print("Input data:")
  # print(a)
  # print("stencil's output:")
  # print(correct_res)
  # print("Success!")
#else:
  # print(a_s.getData_UInt64())
  # print(b_s.getData_UInt64())
  # print("Failed!")

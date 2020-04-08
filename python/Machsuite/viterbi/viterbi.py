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


# Input matrix
real_s         = dsim.DArray(np.array(input_data[0], dtype=np.float64), Type.Double)
img_s          = dsim.DArray(np.array(input_data[1], dtype=np.float64), Type.Double)
real_twid_s    = dsim.DArray(np.array(input_data[2], dtype=np.float64), Type.Double)
img_twid_s     = dsim.DArray(np.array(input_data[3], dtype=np.float64), Type.Double)


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


# Running Simulation
events = dsim.sim(ptrs = [real_s, img_s, real_twid_s, img_twid_s], vars= [], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

# Get software result
data_x, data_y = fftStrided(input_data[0], input_data[1], input_data[2], input_data[3])


print("Cycle: " + str(events[0]))

cm.dump_output('sim_real.data', real_s.getData_Double())
cm.dump_output('sim_img.data', img_s.getData_Double())


if len(check_data[0]) == sum([1 for i, j in zip(real_s.getData_Double(), data_x) if isclose(i,j)]):
    print("Sucess real numbers match!")
    if len(check_data[1]) == sum([1 for i, j in zip(img_s.getData_Double(), data_y) if isclose(i,j)]):
        print("Sucess img numbers match!")
    else:
        print("Failed img numbers doesn't match")
else:
    print("Failed real number doesn't match!")
    

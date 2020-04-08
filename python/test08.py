import numpy as np
import platform
import dsim


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = list(range(0, 8))
a_s = dsim.DArray(val_a, dsim.DArray.DType.UInt64)
val_n = 8

events = dsim.sim(ptrs = [a_s], vars= [val_n], debugs = [], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))
print("Ret: " + str(events[1]))
print(a_s.getData_UInt64())

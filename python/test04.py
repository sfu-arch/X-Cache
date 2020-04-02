from common import bcolors
import numpy as np
import platform
import dsim

def test04(a, b, n):
    sum = a
    for i in range (0, n):
        sum =  (sum + a) * b
    return sum



# Debug container
a = np.zeros(20)
a_s = dsim.DArray(a, dsim.DArray.DType.DWord)

if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = 5
val_b = 3
val_n = 5

events = dsim.sim(ptrs = [], debugs = [a_s], vars= [val_a, val_b, val_n], numRets=1, numEvents=1, hwlib = hw_lib_path)

if events[1] == test04(val_a, val_b, val_n):
    print(bcolors.OKGREEN + "[Success] Ret: " + str(events[1]) + bcolors.ENDC)
else:
    print(bcolors.FAIL + "[Failed] Expected {0}, but Returned: {1}".format(str(test04(val_a, val_b, val_n)), str(events[1])) + bcolors.ENDC)


print(bcolors.OKBLUE + "Cycle: " + str(events[0]) + bcolors.ENDC)
print(bcolors.OKBLUE + "Ret: " + str(events[1]) + bcolors.ENDC)

print('Debug buffer1:')
print([ hex(int(x) & ((1 << 64) - 1)) for x in a_s.getData()])

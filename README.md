muIR-Sim
==========================

[![](https://tokei.rs/b1/github/sfu-arch/muir-sim)](https://github.com/sfu-arch/muir-sim)


µIR-sim is a new simulation environment that improves software and hardware integration and simulation accuracy compare to functional simulation. One of the goals of this framework is integration the hardware development process into the software stack from the beginning, allowing features to be incrementally implemented and evaluated as workloads evolve over time.
Under this environment, the hardware description is the actual specification. This reduces the burden of maintaining consistency between the specification written usually in a higher language such as C/C++ and the actual hardware design described in a language such as Verilog.
Moving to µIR-sim will allow us to have am ore fluid hardware-software specification, and invite more contributions to modify different layers of the stack.

Moreover, this integration provides a more accurate performance feedback, i.e. clock cycles, compared to the traditional functional model of a hardware accelerator.
This is because µIR-sim is based on an open-source hardware simulator called (Verilator)[https://www.veripool.org/wiki/verilator], which compiles Verilog designs down to C++ classes for cycle-accurate simulation.

Lastly, Verilator is already available in many Linux distributions, i.e. Ubuntu, and OSX via homebrew.

**This project is influenced by Tsim from TVM project and we are thankful to TVM team to opensource their code**

## Quick Start

**Step one:** Installing dependencies: Official supported environment for building and running muIR-sim is ubuntu 18.04. To start using muIR-sim you have to first install muIR-sim dependencies:

```bash
sudo apt-get install build-essential cmake libjsoncpp-dev  libncurses5-dev graphviz binutils-dev
sudo apt-get install gcc-8-multilib g++-8-multilib
```

**Step tow:** Is building hardware accelerator and get c++ simulation model so the python driver can run the simulation:

```bash
git clone https://github.com/sfu-arch/muir-sim.git
cd muir-sim
git submodule update --init --recursive
make chisel NPROCS=4 NUM_PTRS=0 NUM_VALS=2 NUM_RETS=1
```

**Step three:** Is building `src/driver.cc` using pip3 so our python script can start using our C++ driver for simulation:

```bash
pip3 install --user .
python3 python/test01.py
```


**Step four:** Running the simulation:

```bash
Stating MuIRSim...
Ptrs:
Vals: val(0):                    5, val(1):                    3,
[LOG] [Test01] [TID-> 0] [BB]   bb_entry0: Output [F] fired @    14
[LOG] [Test01] [TID-> 0] [COMPUTE] binaryOp_mul0: Output fired @    15, Value:                   15 (                   3 *                    5 )
[LOG] [Test01] [TID-> 0] [RET] ret_1: Output fired @    18
Cycle: 4
Success!
Ret: 15
```

To enable tracing of the output change `pLog` variable in *hardware/chisel/src/test/scala/accel/VCRAccel.scala* file to `true`:

```scala
  implicit val p =
    new WithSimShellConfig(dLen = 64, pLog = true)(nPtrs = num_ptrs, nVals = num_vals, nRets = num_returns, nEvent = num_events, nCtrl =  num_ctrl)
  chisel3.Driver.execute(args.take(4),
    () => new DandelionSimAccel(() => new test01DF())(num_ptrs, num_vals, num_returns, num_events, num_ctrl))
}

```


**NOTE:** In [dandelion-tutorial](https://github.com/amsharifian/dandelion-tutorial) you can find depth explanation about every pieces of **Dandelion** project, muIR-Sim, is one of the subprojects, and how to modify the driver and scala file for different simulation scenarios.


## Generating Custom Logic for real FPGAs

muIR-Sim repo provides different shell for FPGA SoC boards and Amazon AWS F1 instances along with the software simulation framework. The idea behind these shell is that the designer first test their implementation using muIR sim and then take the tested logic and put it on either FPGA SoC boards or Amazon AWS instances.

To generate verilog design of the accelerator for AWS for instance, you only need to run the following command:

```bash
make f1 TOP=DandelionF1Accel NPROCS=4 NUM_PTRS=0 NUM_VALS=2 NUM_RETS=1
```

## µIR-sim Design

µIR-sim uses Verilator to integrate µIR generated accelerators into overall system design and provides flexibility in the hardware language used to implement these designs. For example, on could use OpenCL, C/C++ or Chisel3 -- we mainly rely on Chisel3 -- to describe an accelerator design that would eventually be compiled down to Verilog, since it is the standard input language for FPGA/ASIC tools.
To manage runtime process we use Direct Programming Interface (DPI), supported by Verilator. DPI is part of the Verilog standard and a mechanism to support foreign programming languages.

We leverage these features available in Verilator to interface hardware designs from upper layers in the µIR stack such as drivers, runtime, etc (Code is borrowed from Tsim). In fact, we have developed all the glue layers to make this happen, including:

* **DPI module.** The dpi_module.cc is in charge of loading the shared library libHW.so that contains the hardware accelerator and the Verilator execution function.
As stated earlier, Verilator is used to compile the hardware accelerator from Verilog to C++.
Additionally, the DPI module provides an API that can be used by drivers to manage the accelerator by writing/reading registers and terminate (exit) the simulation.

* **Verilator execution function.** This function is called tsim.cc and it is used by Verilator to instantiate the accelerator, generate clock and reset signals, and dump simulation waveforms when it is enabled. The tsim.cc also contains function pointers to DPI functions which are implemented in the DPI module dpi_module.cc. This adds greater flexibility because the behavior of DPI functions can be modified by upper layers in the stack.

* **Hardware DPI modules.** Normally, a hardware accelerator interface can be simplified in two main components, one for control and another for data. The control interface is driven by a host CPU, whereas the data interface is connected to either external memories (DRAM) or internal memories in the form of scratchpads or caches.
There are two hardware modules written in Verilog implementing these two interfaces called VTAHostDPI.v and VTAMemDPI.v.
Accelerators implemented in Verilog can use these modules directly but we also provide Chisel3 wrappers BlackBox for accelerators described in this language.

* **Toy accelerator example.** To showcase the interaction between all of these components, we implemented an Add-by-one accelerator, in both Chisel3 and Verilog, together with a software driver called test_driver.cc.
Also, we provide cmake scripts for building everything automatically and a config.json file for managing accelerator and simulation options.

Finally, the following snippet shows how a python wrapper design simulation, based on the toy example, can invoke the simulation:

```python
import numpy as np
import platform
import dsim

def test01(a, b):
    return a * b


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


events = dsim.sim(ptrs = [  ], vars= [5, 3], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))

if events[1] == test01(5,3):
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nRet: " + str(events[1]))

```


## OSX Dependencies

Install `sbt` and `verilator` using [Homebrew](https://brew.sh/).

```bash
brew install verilator sbt
```

## Linux Dependencies

```bash
sudo apt-get update
sudo apt-get install -y python3 python3-dev python3-setuptools gcc libtinfo-dev zlib1g-dev build-essential cmake

```

Add `sbt` to package manager (Ubuntu).

```bash
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
sudo apt-get update
sudo apt-get install sbt
```

Install `sbt` and `verilator`.

```bash
sudo apt install verilator sbt
```

Verilator version check

```bash
verilator --version
```

the supported version of Verilator should be at least 4.012,
if homebrew (OSX) or package-manager (Linux) does not support that version,
please install Verilator 4.012 or later from binary or source base on following
instruction of Verilator wiki.  

https://www.veripool.org/projects/verilator/wiki/Installing


## Notes
* Some pointers
    * Verilog and Chisel3 tests in `python/`
    * Verilog accelerator backend `hardware/verilog`
    * Chisel3 accelerator backend `hardware/chisel`
    * Software C++ driver (backend) that handles the accelerator `src/driver.cc`
    * Software Python driver (frontend) that handles the accelerator `python/accel`


Authors:
========
* Amirali Sharifian (amiralis@sfu.ca)

Dandelion-Sim
==========================

Dandelion-sim is a new simulation environment that improves software and hardware integration and simulation accuracy compare to functional simulation. One of the goals of this framework is integration the hardware development process into the software stack from the beginning, allowing features to be incrementally implemented and evaluated as workloads evolve over time.
Under this environment, the hardware description is the actual specification. This reduces the burden of maintaining consistency between the specification written usually in a higher language such as C/C++ and the actual hardware design described in a language such as Verilog.
Moving to Dandelion-sim will allow us to have am ore fluid hardware-software specification, and invite more contributions to modify different layers of the stack.

Moreover, this integration provides a more accurate performance feedback, i.e. clock cycles, compared to the traditional functional model of a hardware accelerator.
This is because Dandelion-sim is based on an open-source hardware simulator called (Verilator)[https://www.veripool.org/wiki/verilator], which compiles Verilog designs down to C++ classes for cycle-accurate simulation.

Lastly, Verilator is already available in many Linux distributions, i.e. Ubuntu, and OSX via homebrew.

**This project is influenced by Tsim from TVM project and we are thankful to TVM team to opensource their code**

## Dandelion-sim Design

Dandelion-sim uses Verilator to integrate Dandelion generated accelerators into overall system design and provides flexibility in the hardware language used to implement these designs. For example, on could use OpenCL, C/C++ or Chisel3 -- we mainly rely on Chisel3 -- to describe an accelerator design that would eventually be compiled down to Verilog, since it is the standard input language for FPGA/ASIC tools.
To manage runtime process we use Direct Programming Interface (DPI), supported by Verilator. DPI is part of the Verilog standard and a mechanism to support foreign programming languages.

We leverage these features available in Verilator to interface hardware designs from upper layers in the Dandelion stack such as drivers, runtime, etc (Code is borrowed from Tsim). In fact, we have developed all the glue layers to make this happen, including:

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
a = np.array([0, 1, 2, 3, 4])
b = np.array([0, 1, 2, 3, 4])
c = np.array([0, 0, 0, 0, 0])

a_s = dsim.DArray(a)
b_s = dsim.DArray(b)
c_s = dsim.DArray(c)

hw_lib_path = "./hardware/chisel/build/libhw.so"

cycle = dsim.sim(ptrs = [a_s, b_s, c_s], vars= [5, 5], hwlib = hw_lib_path)

print("Cycle: " + str(cycle))
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

## Publishing Dandelion-Lib

Dandelion-Sim uses Dandelion-lib as a hardware library to build new hardware accelerators. Hence, you need to have Dandelion-Lib published locally in your path.

``` bash
git clone git@csil-git1.cs.surrey.sfu.ca:Dandelion/dandelion-lib.git
cd dandelion-lib
sbt publishLocal
```


## Running Dandelion-sim

* Publish Dandelion-sim

        git clone git@csil-git1.cs.surrey.sfu.ca:Dandelion/dandelion-lib.git
        cd dandelion-lib
        sbt publishLocal

* Build hardware accelerator library

        `make run_chisel NPROCS=4`

* Instal python binding:

        `pip3.7 install --user .`

* Run python test:

        `python3.7 python/test09.py`


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

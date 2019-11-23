export PYTHONPATH:=$(PWD)/python:$(PYTHONPATH)

OSNAME := $(shell uname)

RUNTIME_LIB = libtvm_runtime.so
ifeq ($(OSNAME), Linux)
	RUNTIME_LIB := libtvm_runtime.so
endif
ifeq ($(OSNAME), Darwin)
	RUNTIME_LIB := libtvm_runtime.dylib
endif

NPROCS:=1
BUILD_NAME = build
build_dir = $(abspath .)/$(BUILD_NAME)

.PHONY: clean tvm run_chisel

default: verilog driver
	python3 tests/python/verilog_accel.py

run_chisel: chisel driver tvm
	python3 tests/python/test03_accel.py

driver: | $(build_dir)
	cd $(build_dir) && cmake .. && make -j$(NPROCS)

$(build_dir):
	mkdir -p $@

verilog:
	make -C hardware/verilog

chisel:
	make -C hardware/chisel

tvm:
	cp $(build_dir)/tvm/src/tvm_runtime/$(RUNTIME_LIB) $(build_dir)
	cp $(build_dir)/tvm/src/tvm_runtime/$(RUNTIME_LIB) $(build_dir)/tvm/src
	cd $(build_dir)/tvm/src/tvm_runtime/python && python3 setup.py install --user

clean:
	-rm -rf $(build_dir)
	make -C hardware/chisel clean

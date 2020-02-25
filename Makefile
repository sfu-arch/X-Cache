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

.PHONY: clean chisel


driver: | $(build_dir)
	cd $(build_dir) && cmake .. && make -j$(NPROCS)

$(build_dir):
	mkdir -p $@

verilog:
	make -C hardware/verilog

chisel:
	make -C hardware/chisel

f1:
	make -C hardware/chisel verilog

clean:
	-rm -rf $(build_dir)
	make -C hardware/chisel clean

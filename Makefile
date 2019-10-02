export PYTHONPATH:=$(PWD)/python:$(PYTHONPATH)

NPROCS:=1
BUILD_NAME = build
build_dir = $(abspath .)/$(BUILD_NAME)

.PHONY: clean tvm

default: verilog driver
	python3 tests/python/verilog_accel.py

run_chisel: chisel driver tvm
	python3 tests/python/test14_accel.py

driver: | $(build_dir)
	cd $(build_dir) && cmake .. && make -j$(NPROCS)

$(build_dir):
	mkdir -p $@

verilog:
	make -C hardware/verilog

chisel:
	make -C hardware/chisel

tvm:
	tar xvf packages/tvm.tar.gz -C .
	cp $(build_dir)/tvm_runtime-prefix/src/tvm_runtime-build/libtvm_runtime.dylib python
	cd python && python3 setup.py install

clean:
	-rm -rf $(build_dir)
	make -C hardware/chisel clean

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

cmake_minimum_required(VERSION 3.8)
project(tsim C CXX)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

set(VTA_DIR ${CMAKE_CURRENT_SOURCE_DIR})

include_directories("${VTA_DIR}/include")
include_directories("${VTA_DIR}/include/vta")
include_directories("${VTA_DIR}/include/dlpack/include")
include_directories("${VTA_DIR}/include/dmlc/include")

set(CMAKE_C_FLAGS "-g -O2 -Wall -fPIC")
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "-g -O2 -Wall")

if (CMAKE_CXX_COMPILER_ID MATCHES "GNU" AND
    CMAKE_CXX_COMPILER_VERSION VERSION_GREATER 7.0)
  set(CMAKE_CXX_FLAGS "-faligned-new ${CMAKE_CXX_FLAGS}")
endif()


add_subdirectory(software)
#add_executable(tsim ${CMAKE_CURRENT_SOURCE_DIR}/src/driver_main.cc)
add_executable(tsim ${CMAKE_CURRENT_SOURCE_DIR}/src/driver_test09.cc)

target_link_libraries(tsim sw pthread dl)

# Adding pybind11
add_subdirectory(pybind11)
pybind11_add_module(dsim src/driver.cc)
target_link_libraries(dsim PRIVATE sw pthread dl)

#install(TARGETS sw DESTINATION ${VTA_DIR}/sw_build)

cmake_minimum_required(VERSION 3.8)
project(dandelion-sim)

set(PACKAGE_NAME dandelion)
set(PACKAGE_VERSION 0.1)
set(PACKAGE_STRING "${PACKAGE_NAME} ${PACKAGE_VERSION}")
set(PACKAGE_BUGREPORT "amiralis@sfu.ca")


set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

option(DSIM "Build dsim binary simulation" OFF)

set(DSIM_DIR ${CMAKE_CURRENT_SOURCE_DIR})

include_directories("${DSIM_DIR}/include")
include_directories("${DSIM_DIR}/include/vta")
include_directories("${DSIM_DIR}/include/dlpack/include")
include_directories("${DSIM_DIR}/include/dmlc/include")

set(CMAKE_C_FLAGS "-g -O2 -Wall -fPIC")
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "-g -O2 -Wall")

if (CMAKE_CXX_COMPILER_ID MATCHES "GNU" AND
    CMAKE_CXX_COMPILER_VERSION VERSION_GREATER 7.0)
  set(CMAKE_CXX_FLAGS "-faligned-new ${CMAKE_CXX_FLAGS}")
endif()


add_subdirectory(software)

if (DSIM)
    add_executable(tsim ${CMAKE_CURRENT_SOURCE_DIR}/src/driver_test09.cc)
    target_link_libraries(tsim sw pthread dl)
endif (DSIM)
unset(DSIM CACHE)

# Adding pybind11
add_subdirectory(pybind11)
pybind11_add_module(dsim src/driver.cc)
target_link_libraries(dsim PRIVATE sw pthread dl)

#install(TARGETS sw DESTINATION ${VTA_DIR}/sw_build)

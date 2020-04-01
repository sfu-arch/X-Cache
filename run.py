#!/usr/bin/env python
from __future__ import print_function
from __future__ import division
from __future__ import unicode_literals
from __future__ import absolute_import

import requests
from tempfile import mkdtemp
import argparse
import errno
import hashlib
import multiprocessing
import os
import os.path as p
import platform
import re
import shlex
import shutil
import subprocess
import sys
import sysconfig
import tarfile
from zipfile import ZipFile
import json


IS_64BIT = sys.maxsize > 2**32
PY_MAJOR, PY_MINOR = sys.version_info[0: 2]
PY_VERSION = sys.version_info[0: 3]
version = sys.version_info[0: 3]
if version < (3, 5, 1):
    sys.exit('YouCompleteMe requires Python >= 2.7.1 or >= 3.5.1; '
             'your version of Python is ' + sys.version)

DIR_OF_THIS_SCRIPT = p.dirname(p.abspath(__file__))
DIR_OF_OLD_LIBS = p.join(DIR_OF_THIS_SCRIPT, 'python')


NO_DYNAMIC_PYTHON_ERROR = (
    'ERROR: found static Python library ({library}) but a dynamic one is '
    'required. You must use a Python compiled with the {flag} flag. '
    'If using pyenv, you need to run the command:\n'
    '  export PYTHON_CONFIGURE_OPTS="{flag}"\n'
    'before installing a Python version.')
NO_PYTHON_LIBRARY_ERROR = 'ERROR: unable to find an appropriate Python library.'
NO_PYTHON_HEADERS_ERROR = 'ERROR: Python headers are missing in {include_dir}.'


class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


# Regular expressions used to find static and dynamic Python libraries.
# Notes:
#  - Python 3 library name may have an 'm' suffix on Unix platforms, for
#    instance libpython3.5m.so;
#  - the linker name (the soname without the version) does not always
#    exist so we look for the versioned names too;
#  - on Windows, the .lib extension is used instead of the .dll one. See
#    https://en.wikipedia.org/wiki/Dynamic-link_library#Import_libraries
STATIC_PYTHON_LIBRARY_REGEX = '^libpython{major}\\.{minor}m?\\.a$'
DYNAMIC_PYTHON_LIBRARY_REGEX = """
  ^(?:
  # Linux, BSD
  libpython{major}\\.{minor}m?\\.so(\\.\\d+)*|
  # OS X
  libpython{major}\\.{minor}m?\\.dylib|
  # Windows
  python{major}{minor}\\.lib|
  # Cygwin
  libpython{major}\\.{minor}\\.dll\\.a
  )$
"""


def dir_path(string):
    if os.path.isfile(string):
        return string
    else:
        raise NotADirectoryError(string)


def CheckCall(args, **kwargs):
    try:
        subprocess.check_call(args, **kwargs)
    except subprocess.CalledProcessError as error:
        sys.exit(error.returncode)


def FindExecutableOrDie(executable, message):
    path = FindExecutable(executable)

    if not path:
        sys.exit("ERROR: Unable to find executable '{}'. {}".format(
            executable,
            message))

    return path

# On Windows, distutils.spawn.find_executable only works for .exe files
# but .bat and .cmd files are also executables, so we use our own
# implementation.


def FindExecutable(executable):
    paths = os.environ['PATH'].split(os.pathsep)
    base, extension = os.path.splitext(executable)
    extensions = ['']

    for extension in extensions:
        executable_name = executable + extension
        if not os.path.isfile(executable_name):
            for path in paths:
                executable_path = os.path.join(path, executable_name)
                if os.path.isfile(executable_path):
                    return executable_path
        else:
            return executable_name
    return None


def ParseArguments():
    parser = argparse.ArgumentParser()
    parser.add_argument('--build-accel', action='store_true', dest='build',
                        help='Building accelerator model')
    parser.add_argument('--build-dsim', action='store_true', dest='dsim',
                        help='Building dsim library')
    parser.add_argument('--accel-config', action='store', dest='accel_config', type=dir_path,
                        help='The accelerator name that will we passed to hardware generator')
    args = parser.parse_args()

    return args


def BuildAccel(config):

    make_params = []
    with open(config, 'r') as configFile:
        configData = json.load(configFile)
        for config in configData['Accel']:
            if config == 'Build':
                for build in configData['Accel'][config]:
                    make_params += ["{}={} ".format(str(build),
                                                    str(configData['Accel'][config][build]))]
            else:
                if isinstance(configData['Accel'][config], list):
                    make_params += ["{}={} ".format(str(config),
                                                    ','.join(map(str, configData['Accel'][config])))]
                else:
                    make_params += ["{}={} ".format(str(config),
                                                    str(configData['Accel'][config]))]

        # print(make_params)
        print(bcolors.OKGREEN + " ".join(str(val)
                                         for val in ['make', 'chisel'] + make_params) + bcolors.ENDC)
        CheckCall(['make', 'chisel'] + make_params)


def BuildDsim():
    CheckCall([sys.executable, '-m', 'pip', 'install', '.'])


def Main():
    pybind11_file = p.join(DIR_OF_THIS_SCRIPT, 'pybind11', 'CMakeLists.txt')

    if not p.isfile(pybind11_file):
        sys.exit(
            'File {0} does not exist; you probably forgot to run:\n'
            '\tgit submodule update --init --recursive\n'.format(pybind11_file))

    args = ParseArguments()

    if args.dsim:
        BuildDsim()
    else:
        BuildAccel(args.accel_config)


if __name__ == "__main__":
    Main()

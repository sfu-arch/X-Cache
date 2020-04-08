class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


"""
Returining either int or float from string
"""
def num(s):
    try:
        return int(s)
    except ValueError:
        return float(s)


"""
Reading input data file
"""
def read_file(file_name):
    input_arrays = [ ]
    _array = [ ]
    first = True
    
    with open(file_name,'r+') as input_file:
        for line in input_file.readlines():
            if line.strip() == '%%':
                if first:
                    first = False
                    continue
    
                input_arrays.append(_array)
                _array = [ ]
                continue
            else:
                _array.append(num(line.strip()))
        input_arrays.append(_array)
    return input_arrays 


def dump_output(file_name, in_list):
    string_list = [str(x) for x in in_list]
    with open(file_name, "w") as outfile:
        outfile.write("\n".join(string_list))


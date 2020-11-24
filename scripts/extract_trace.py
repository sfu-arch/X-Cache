import argparse
import os
import re
import csv

"""
Node class
"""
class Node:
    def __init__(self, id, name):
        self.id = id
        self.name = name
        self.output = []
        self.cycle = []
        self.predicate = []

    def add_output(self, val):
        self.output.append(val)
    def add_cycle(self, val):
        self.cycle.append(val)
    def add_predicate(self, val):
        self.predicate.append(val)

    def __str__(self):
        return "Name: {}, ID: {}, Out: {}, Cycle: {}, Pred: {}".format(self.name, self.id, self.output, self.cycle, self.predicate)

"""
Check if the input value is hex, otherwise the value should be handeled differently
"""
def checkOutput(val):
    try:
        int(val, 0)
        return True
    except ValueError:
        return False


"""
Reading debug logs
"""
def read_log(log_path):

    node_list = []

    with open(log_path) as file:
        pattern = re.compile("\[Name:\s+(.*?)\]\s+\[B?ID:\s+(.*?)\]\s+\[Pred:\s+(.*?)\].*\[Out:\s+(.*?)\].*\[Cycle:\s+(.*?)\]")
        for line in file.readlines():
            p_result = pattern.search(line.rstrip())
            if p_result:

                new_node = list(filter(lambda x: x.name == p_result.group(1), node_list))

                # Check if node is already added
                if new_node:
                    if(checkOutput(p_result.group(4))):
                        new_node[0].output.append(int(p_result.group(4), 0))
                    else:
                        new_node[0].output.append([x.strip() for x in p_result.group(4).split('-')][0].split(":")[1])

                    new_node[0].predicate.append(p_result.group(3))
                    new_node[0].cycle.append(p_result.group(5))
                # Otherwise add output value
                else:
                    new_node = Node(p_result.group(2), p_result.group(1))
                    if(checkOutput(p_result.group(4))):
                        new_node.output.append(int(p_result.group(4), 0))
                    else:
                        new_node[0].output.append([x.strip() for x in p_result.group(4).split('-')][0].split(":")[1])

                    new_node.predicate.append(p_result.group(3))
                    new_node.cycle.append(p_result.group(5))
                    node_list.append(new_node)

    return node_list


def get_args():

    parser = argparse.ArgumentParser()

    parser.add_argument('-l', '--log', required=True, help="Input path of muir trace")
    parser.add_argument('-o', '--output', required=True, help="Output path for node values")
    parser.add_argument('-p', '--print', action='store_true', help="Print log elements")
    parser.add_argument('--dump_all', dest='dump', action='store_true', help="dump all the nodes")
    parser.add_argument('-n', '--nodes', dest='nodes', nargs='+', default=[], help="List nodes to dump")

    args = parser.parse_args()
    return args

if __name__ == "__main__":

    args = get_args()
    nodes = read_log(args.log)

    # Create output director

    if not os.path.exists(args.output):
        try:
            os.mkdir(args.output)
        except OSError:
            print ("Creation of the directory %s failed" % path)

    if args.dump:
        # Dump outputs for nod ids
        trace_node_data_file = open(args.output + "/node_data.trace", 'a+')
        trace_node_cycle_file = open(args.output + "/node_cycle.trace", 'a+')
        for node in nodes:
            # Write node data
            write = csv.writer(trace_node_data_file)
            write.writerow([node.id] + node.output)

            # Write node cycle
            write = csv.writer(trace_node_cycle_file)
            write.writerow([node.id] + node.cycle)

        trace_node_data_file.close()
        trace_node_cycle_file.close()
    else:
        # Dump outputs for nod ids
        trace_node_data_file = open(args.output + "/node_data.trace", 'a+')
        trace_node_cycle_file = open(args.output + "/node_cycle.trace", 'a+')
        for node_id in args.nodes:
            node_trace = list(filter(lambda x: x.id == node_id, nodes))
            if node_trace:
                # Write node data
                write = csv.writer(trace_node_data_file)
                write.writerow([node_id] + node_trace[0].output)

                # Write node cycle
                write = csv.writer(trace_node_cycle_file)
                write.writerow([node_id] + node_trace[0].cycle)

        trace_node_data_file.close()
        trace_node_cycle_file.close()

    if args.print:
        for node in nodes:
            print(node)

#!/usr/bin/python
import matplotlib
import seaborn as sns
import pprint
import argparse
import re
import matplotlib.pyplot as plt
import collections
import itertools
import sys

parser = argparse.ArgumentParser()
parser.add_argument('-i', '--in', dest='input', default='stdin', help='Input log file')
parser.add_argument('-c', '--colour', dest='colour', default='steelblue', help='Graph color')
parser.add_argument('-o', '--out', dest='out', help='Output image file')
parser.add_argument('-s', '--sort', dest='sort', default=1, help='Sort by 0=instance name, 1=earliest event.')
parser.add_argument('-f', '--filter', dest='filt', default='', help='Name filter string (e.g. bb for basic blocks')
args = parser.parse_args()

pp = pprint.PrettyPrinter(indent=2)
cmapd = matplotlib.colors.ListedColormap(sns.color_palette("Reds",64), "MyColors")

sns.set(style="white", context="talk")
sns.set_palette("deep", 32, 1)
palette = itertools.cycle(sns.color_palette())

# Read log file and extract all INFO events
eventList = []

if (args.input == "stdin"):
    f = sys.stdin
else:
    try:
        f = open(args.input,'r')
    except:
        print("Error: could not open file '%s'\n" % (args.input))
        parser.print_help()
        sys.exit(1)

for text in f:
    try:
        found = re.search('\[LOG\](.+'+args.filt+'.+?)$', text).group(1)
        eventList.append(found)
    except AttributeError:
        found = ''  # apply your error handling

f.close()

# Create a dictionary of output events types
# For each event type generate a list of when the event is active
activity = collections.defaultdict(list)
for event in eventList:
    fields = re.search('(.+):.+@(.+)', event)    # all events
#    fields = re.search('(bb_.+):.+@(.+)', event) # basic blocks only
    try:
        (node,cycle) = (fields.group(1),fields.group(2))
        activity[node].append((int(cycle),1))
    except AttributeError:
        newOutput = ''  # error handling

# sort activity by earliest event
orderedActivity = collections.OrderedDict(sorted(activity.items(), key=lambda t: t[int(args.sort)], reverse=True))

fig, ax = plt.subplots()
i = 10
yticklabels = []
yticks = []
for key, value in orderedActivity.items():
    ax.broken_barh(value, (i, 9), facecolors=args.colour)#facecolors=next(palette))
    yticklabels.append(key)
    yticks.append(i+5)
    i += 10

ax.set_ylim(5, i)
#ax.set_xlim(0, 200)
ax.set_xlabel('Clock Cycle')
ax.set_yticks(yticks)
ax.set_yticklabels(yticklabels)
ax.grid(True)

if args.out:
    plt.savefig(args.out)
else:
    plt.show()

print("Done")

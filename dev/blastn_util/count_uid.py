#!/usr/bin/env python

# This script is used to count unique ids in the query id column of blastall -m 8 (tabular) output.
# To use, just change the path of the file. So, fn = "[your/file/path]"

import sys, os
import re

fn = "/home/cahancock/finalproj/runs/crt-results/ncbi_bacteria_genome/all_bacteria_tabular.blastn"

fh = open(fn, "r")

found = set()

for line in fh:
	match = re.match(r"^(\S+)", line)
	found.add(match.group(1))

print len(found)
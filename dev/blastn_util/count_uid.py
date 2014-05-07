#!/usr/bin/env python

# This script is used to count thing it's changed often depending on the need at the time.
# To use, just change the path of the file. So, fn = "[your/file/path]"

import sys, os
import re

fn = "/home/cahancock/finalproj/runs/crt-results/ncbi_bacteria_genome/all_bacteria.out"

fh = open(fn, "r")

#found = set()
matches = 0

for line in fh:
	match = re.match(r"^[>](\S+)", line)
	if match:
		#found.add(match.group(1))
		matches = matches + 1

print matches #len(found)

fh.close()
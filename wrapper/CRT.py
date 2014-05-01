#!/usr/bin/env python

__version_major__ = 1
__version_minor__ = 0
__revision__ = 4
__build__ = "315CDB1"

import optparse
import sys, os
import tempfile
import subprocess

p = optparse.OptionParser(description = """CRT: wrapper for the command-line
'CRISPR Recognition Tool' (Bland et al., BMC Bioinformatics 2007 8(1):209""")

g = optparse.OptionGroup(p, "Input & Output")

g.add_option("-i", "--input", dest = "input_fn", nargs = 2, metavar = "FILENAME FORMAT",
	help = """Name of the sequences file to analyze and format of the file
(mandatory). Supported formats are abi, ace, clustal, embl, fasta, fastq,
genbank, ig, imgt, nexus, phd, phylip, pir, seqxml, sff, stockolm, swiss, tab,
qual and uniprot (see http://biopython.org/wiki/SeqIO#File_Formats).""")

g.add_option("-o", "--output", dest = "output_fn", metavar = "FILENAME",
	help = """Name of the CRT report file (mandatory)""")

g.add_option("--ignore-empty", dest = "ignore_empty", action = "store_true", default = False,
	help = """If set, the output file will not contain reports for sequences
in which no CRISPR was found.""")

p.add_option_group(g)

g = optparse.OptionGroup(p, "Options (CRT)")

g.add_option("--min-nr", dest = "CRT_minNR", type = "int", metavar = "INTEGER", default = 3,
	help = "Minimum number of repeats a CRISPR must contain (optional; default: %default)")

g.add_option("--min-rl", dest = "CRT_minRL", type = "int", metavar = "INTEGER", default = 19,
	help = "Minimum length of a CRISPR's repeated region (optional; default: %default)")

g.add_option("--max-rl", dest = "CRT_maxRL", type = "int", metavar = "INTEGER", default = 38,
	help = "Maximum length of a CRISPR's repeated region (optional; default: %default)")

g.add_option("--min-sl", dest = "CRT_minSL", type = "int", metavar = "INTEGER", default = 19,
	help = "Minimum length of a CRISPR's non-repeated (or spacer) region (optional; default: %default)")

g.add_option("--max-sl", dest = "CRT_maxSL", type = "int", metavar = "INTEGER", default = 48,
	help = "Maximum length of a CRISPR's non-repeated (or spacer) region (optional; default: %default)")

g.add_option("--search-wl", dest = "CRT_searchWL", type = "int", metavar = "INTEGER",
	help = "Length of search window used to discover CRISPRs (optional; range 6-9)")

p.add_option_group(g)

g = optparse.OptionGroup(p, "Options (Java)")

g.add_option("--java-executable", dest = "java_executable", metavar = "FILENAME", default = "java",
	help = "Java runtime executable (optional; default: '%default')")

g.add_option("--crt-jar", dest = "java_CRT_jar", metavar = "FILENAME", default = os.path.join(os.path.dirname(__file__), "crt-cli-48.jar"),
	help = "CRT jar filename (optional; default: '%default')")

p.add_option_group(g)

g = optparse.OptionGroup(p, "Display Options")

p.add_option("-v", "--version", dest = "display_version", action = "store_true", default = False,
	help = "Display the program version and exit")

g.add_option("-p", "--verbose", dest = "verbose", action="store_true", default = False,
	help = "Print each record id.")

p.add_option_group(g)

(p, a) = p.parse_args()

def error (msg):
	print >>sys.stderr, "ERROR: %s" % msg
	sys.exit(1)

if (p.display_version):
	print "%s.%srev%s" % (__version_major__, __version_minor__, __revision__)
	sys.exit(0)

if (p.input_fn == None):
	error("An input sequence filename is required.")

(input_fn, input_format) = p.input_fn
if (not os.path.exists(input_fn)):
	error("Input file '%s' not found." % input_fn)

if (p.output_fn == None):
	error("An output filename is required.")

p.output_fn = open(p.output_fn, 'w')

if (p.CRT_searchWL) and ((p.CRT_searchWL < 6) or (p.CRT_searchWL > 9)):
	error("Invalid value for --search-wl; must be between 6 and 9")

CRT_options = []
for option in dir(p):
	value = getattr(p, option)
	if (option.startswith("CRT_")) and (value != None):
		CRT_options.append("-%s %s" % (option[4:], value))

input_file = open(input_fn, "r")

#:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

try:
	from Bio import SeqIO

except ImportError:
	error("The Biopython library is required.")

def read (file_handle, format):
	try:
		#parser = SeqIO.parse(fn, format)
		parser = SeqIO.parse(file_handle, format)
		yield parser.next()

	except ValueError:
		error("Unknown sequence format '%s'." % format)

	for entry in parser:
		yield entry

# for each sequence in the input file,
for record in read(input_file, input_format.lower()):
	if(p.verbose):
		print record.id


	# create a temporary file and write
	# in it the sequence in FASTA format
	i_fh, i_fn = tempfile.mkstemp(text = True)
	i_fh = os.fdopen(i_fh, 'w')
	o_fn = i_fn + ".out"

	SeqIO.write([record], i_fh, "fasta")

	i_fh.close()

	# launch CRT on this temporary file
	cmd = "%s -cp %s crt %s %s %s 1> /dev/null" % (
		p.java_executable,
		p.java_CRT_jar,
		' '.join(CRT_options),
		i_fn, o_fn
	)

	try:
		exit_code = subprocess.call(cmd, shell = True)

		if (exit_code < 0):
			error("The CRT child process was terminated by signal %s" % -exit_code)

		elif (exit_code > 0):
			error("The CRT child process returned signal %s" % exit_code)

	except OSError as e:
		error("CRT execution failed. Reason: %s" % str(e))

	report = open(o_fn, 'r').readlines()

	# delete the temporary files
	os.remove(i_fn)
	os.remove(o_fn)

	# test if CRT reported at least one CRISPR
	if (p.ignore_empty):
		is_empty = False
		for line in report:
			if ("No CRISPR elements were found." in line):
				is_empty = True
				break

		if (is_empty):
			continue

	# write the results in the master output file
	for line in report:
		p.output_fn.write(line)

	p.output_fn.write('\n')

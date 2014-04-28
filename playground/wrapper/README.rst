CRT
===

**CRT** is a command-line utility that wraps the *'CRISPR Recognition Tool'* published by Bland et al. (BMC Bioinformatics 2007, 8(1):209) and available at http://www.room220.com/crt/

While the original CRT tool only accepts as input a FASTA-formatted sequence with only one sequence in it (typically a whole genome), the **CRT** wrapper accepts multi-sequence files in various formats (see `here <http://biopython.org/wiki/SeqIO#File_Formats>`_ for a complete list).

Version
--------------

The current version is 1.0rev4

Installation
------------

To install the **CRT** wrapper you must go through the following steps:

- ensure a Python interpreter (version 2.6 or 2.7; 3.0 and later are not supported yet) is installed in your system. Please refer to this `page <http://www.python.org/getit/>`_ if needed.
- ensure the `Biopython <http://biopython.org/>`_ library is installed.
- go to http://github.com/ajmazurie/CRT/zipball/master. A file named *ajmazurie-CRT-xxxx.zip* will download (xxxx will be replaced by a commit signature code).
- unzip this file.
- ensure the file *CRT/CRT* is in your `PATH <http://kb.iu.edu/data/acar.html>`_, either by moving it to a directory already in your PATH or by adding this directory to the PATH.
- the **CRT** wrapper should now be accessible from the command line; you can test it by typing *CRT --version*
- ensure the CRT jar file (available at http://www.room220.com/crt/) is in your system; it is advised to place it in the same directory as your *CRT* file.

Usage
-----

A list of options can be obtained by using the *--help* option::

	Usage: CRT [options]

	CRT: wrapper for the command-line 'CRISPR Recognition Tool' (Bland et al., BMC
	Bioinformatics 2007 8(1):209

	Options:
	  -h, --help            show this help message and exit
	  -v, --version         Display the program version and exit

	  Input & Output:
		-i FILENAME FORMAT, --input=FILENAME FORMAT
				Name of the sequences file to analyze and format of
				the file (mandatory). Supported formats are abi, ace,
				clustal, embl, fasta, fastq, genbank, ig, imgt, nexus,
				phd, phylip, pir, seqxml, sff, stockolm, swiss, tab,
				qual and uniprot (see
				http://biopython.org/wiki/SeqIO#File_Formats).
		-o FILENAME, --output=FILENAME
				Name of the CRT report file (mandatory)

	  Options (CRT):
		--min-nr=INTEGER    Minimum number of repeats a CRISPR must contain
				(optional; default: 3)
		--min-rl=INTEGER    Minimum length of a CRISPR's repeated region
				(optional; default: 19)
		--max-rl=INTEGER    Maximum length of a CRISPR's repeated region
				(optional; default: 38)
		--min-sl=INTEGER    Minimum length of a CRISPR's non-repeated (or spacer)
				region (optional; default: 19)
		--max-sl=INTEGER    Maximum length of a CRISPR's non-repeated (or spacer)
				region (optional; default: 48)
		--search-wl=INTEGER
				Length of search window used to discover CRISPRs
				(optional; range 6-9)

	  Options (Java):
		--java-executable=FILENAME
				Java runtime executable (optional; default: 'java')
		--crt-jar=FILENAME  CRT jar filename (optional; default: 'CRT1.2-CLI.jar')

A description of the meaning of the CRT options *--min-nr*, *--min-rl*, etc. can be found at the CRT website.

Example
-------

Example of use::

	CRT -i my_sequences.fasta -o annotated_CRISPRs.txt

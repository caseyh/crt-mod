#crt-mod

Project to modify the CRISPR Recognition Tool and related wrapper in order to make the results more usable in more contexts.

Added features, thus far:

- Output FASTA files
- Run in multiple threads (can run MUCH faster than other versions)
- Simplified distribution - a working crt.jar file is included.

##Child of CRT Wrapper
This is a command-line utility that goes a farther in terms of usability and performance than the original [CRT](https://github.com/ajmazurie/CRT) wrapper project.

**CRT** is a command-line utility that wraps the *'CRISPR Recognition Tool'* published by Bland et al. (BMC Bioinformatics 2007, 8(1):209) and available at http://www.room220.com/crt/

While the original CRT tool only accepts as input a FASTA-formatted sequence with only one sequence in it (typically a whole genome), the **CRT** wrapper accepts multi-sequence files in various formats (see [here](http://biopython.org/wiki/SeqIO#File_Formats>) for a complete list).

## Version

The current version is 2.0rev1. This is dependent on THIS project, not any predecessor.

##Installation

To install the **CRT** wrapper you must go through the following steps:

- Ensure a Python interpreter (ver 2.6 or 2.7; 3.0+ are not supported) is installed in your system. Get it [here](http://www.python.org/getit).
- Ensure the [Biopython](http://biopython.org) library is installed.
- Go to http://github.com/caseyh/crt-mod/zipball/master. A file named *caseyh-crt-mod-xxxx.zip* will download.
- Unzip this file.
- Ensure the file *crt-mod/crt-mod.py* is in your [PATH](http://kb.iu.edu/data/acar.html), either by moving it to a directory already in your PATH or by adding this directory to the PATH. Alternatively you can add it as an [alias](http://www.thegeekstuff.com/2010/04/unix-bash-alias-examples/).
- **crt-mod** should now be accessible from the command line; you can test it by typing *crt-mod --version*

##Usage
A list of options can be obtained by using the *--help* or *-h* option.
A description of the meaning of the CRT options *--min-nr*, *--min-rl*, etc. can be found at the CRT website.

##Example
Example of use:

    crt-mod -i my_sequences.fasta fasta -o annotated_CRISPRs.txt
    
or to output fasta files:
    
    crt-mod -i my_sequences.fasta fasta -o CRISPRS.fasta -f
    
or to set the number of running threads:

    crt-mod -i my_sequences.fasta fasta -o annotated_CRISPRs.txt -a 4
    
## Dev Folder
This contains things that were used or modified in development. For example, [dev/cli/src](dev/cli/src) includes the modified java source code necessary for this project. The [dev](dev) directory also contains various jars that were compiled to work in different java environments as a number of errors were encountered.
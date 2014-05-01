import java.util.Vector;
import java.io.*;

public class CRISPRFinder
{
	private String inputFileName;
	private String outputFileName;

 	private int screenDisplay;
	private int minNumRepeats;
    private int minRepeatLength;
    private int maxRepeatLength;
    private int minSpacerLength;
    private int maxSpacerLength;
    private int searchWindowLength;

   	DNASequence sequence = null;
   	int sequenceLength = 0;


	public CRISPRFinder(String _inputFileName, String _outputFileName, boolean _outputFasta, int _screenDisplay, int _minNumRepeats, int _minRepeatLength, int _maxRepeatLength, int _minSpacerLength, int _maxSpacerLength, int _searchWindowLength)
	{
		inputFileName = _inputFileName;
		outputFileName = _outputFileName;
		outputFasta = _outputFasta;

		screenDisplay = _screenDisplay;
		minNumRepeats = _minNumRepeats;
      	minRepeatLength = _minRepeatLength;
      	maxRepeatLength = _maxRepeatLength;
      	minSpacerLength = _minSpacerLength;
      	maxSpacerLength = _maxSpacerLength;
      	searchWindowLength = _searchWindowLength;
	}


	public void goCRISPRFinder()
	{	File inputFile = new File(inputFileName);
		if (inputFile.exists())
		{	System.out.println("\n\nReading file " + inputFile.getPath());
			FASTAReader fastaReader = new FASTAReader(inputFile.getPath());
			if (fastaReader.isFASTA())
			{	fastaReader.read();
				System.out.println("Reading file complete");
				sequence = new DNASequence(fastaReader.getSequence(), fastaReader.getHeader());
				System.out.println(sequence.getName());
				System.out.println(sequence.length() + " bases");

				try 
				{	findRepeats();
				}
				catch (Exception e)	{	System.out.println ("Error processing input file: " + inputFile.getPath() + ". Please, check contents.\n");	}
				/*
				if (!sequence.isDNASequence())
				{   System.out.println(sequence.getErrorLog());
				}
				else
					findRepeats();
				*/
			}
			else
			{   System.out.println("Not a valid FASTA file:  " + inputFile.getPath());
			}
		}
		else
		{   System.out.println("File name does not exist:  " + inputFile.getPath());
		}
	}


	private void findRepeats()
	{
		Vector CRISPRVector = new Vector();
		sequenceLength = sequence.length();
		int actualRepeatLength;
		boolean repeatsFound = false;

		CRISPR candidateCRISPR;
		String pattern;

		if ((searchWindowLength < 6) || (searchWindowLength > 9))
		{	searchWindowLength = 8;
			//let user know that window size has changed
		}

		double spacerToSpacerMaxSimilarity = 0.62;
		int spacerToSpacerLengthDiff = 12;
		int spacerToRepeatLengthDiff = 30;

		//the mumber of bases that can be skipped while we still guarantee that the entire search
		//window will at some point in its iteration thru the sequence will not miss a any repeat
		int skips = minRepeatLength - (2 * searchWindowLength - 1);
		if (skips < 1)
			skips = 1;

		System.out.println("Searching for repeats...");
		long repeatSearchStart = System.currentTimeMillis();

        SearchUtil searchUtil = new SearchUtil();

		int searchEnd = sequenceLength - maxRepeatLength - maxSpacerLength - searchWindowLength;
		for (int j = 0; j <= searchEnd; j = j + skips)
		{
      		candidateCRISPR = new CRISPR();

         	int beginSearch = j + minRepeatLength + minSpacerLength;
			int endSearch = j + maxRepeatLength + maxSpacerLength + searchWindowLength;

			if (endSearch > sequenceLength)
				endSearch = sequenceLength;

            if (endSearch < beginSearch)  //should never occur
            	endSearch = beginSearch;

            String text = sequence.substring(beginSearch, endSearch);
            pattern = sequence.substring(j, j + searchWindowLength);

			//if pattern is found, add it to candidate list and scan right for additional similarly spaced repeats
			int patternInTextIndex = searchUtil.boyer_mooreSearch(text, pattern);
			if (patternInTextIndex >= 0)
			{   candidateCRISPR.addRepeat(j);
				candidateCRISPR.addRepeat(beginSearch + patternInTextIndex);
				candidateCRISPR = CRISPRUtil.scanRight(candidateCRISPR, pattern, minSpacerLength, 24, searchUtil);
			}

           	if ( (candidateCRISPR.numRepeats() >= minNumRepeats) )  //make sure minNumRepeats is always at least 2
			{	candidateCRISPR = CRISPRUtil.getActualRepeatLength(candidateCRISPR, searchWindowLength, minSpacerLength);

                actualRepeatLength = candidateCRISPR.repeatLength();

				if ( (actualRepeatLength >= minRepeatLength) && (actualRepeatLength <= maxRepeatLength) )
				{	if (CRISPRUtil.hasNonRepeatingSpacers(candidateCRISPR, spacerToSpacerMaxSimilarity))
					{
						if (CRISPRUtil.hasSimilarlySizedSpacers(candidateCRISPR, spacerToSpacerLengthDiff, spacerToRepeatLengthDiff))
						{
							candidateCRISPR = CRISPRUtil.checkFlank("left", candidateCRISPR, minSpacerLength, 30, spacerToSpacerMaxSimilarity, .70);
							candidateCRISPR = CRISPRUtil.checkFlank("right", candidateCRISPR, minSpacerLength, 30, spacerToSpacerMaxSimilarity, .70);

							candidateCRISPR = CRISPRUtil.trim(candidateCRISPR, minRepeatLength);

							CRISPRVector.addElement(candidateCRISPR);
							repeatsFound = true;

							//we may skip current CRISPR (assuming CRISPRs are not interleaved)
							j = candidateCRISPR.end() + 1;
						}

					}
				}
			}
		}

		long repeatSearchEnd = System.currentTimeMillis();
		System.out.println("Time to search for repeats:  " + (repeatSearchEnd - repeatSearchStart) + " ms");
		System.out.println(CRISPRVector.size() + " possible CRISPR(s) found" + "\n");


		// ********************** Display CRISPR elements ********************** //
        // ********************************************************************* //
        try
		{	
			FileOutputStream outputFile;
			PrintStream out;
			
			if (screenDisplay == 1)
				out = System.out;
			else
			{	if (outputFileName.equals(""))
					outputFileName = "a.out";
					
					outputFile = new FileOutputStream(outputFileName);
					out = new PrintStream(outputFile);
			}

			// BEGIN PRINTING CRISPER INFORMATION TO FILE OR SCREEN DEPENDING ON OPTIONS

			if (!outputFasta) {
				out.print("ORGANISM:  ");
				out.print(sequence.getName() + "\n");
				out.print("Bases: " + sequence.length() + "\n\n\n");
			}
			

			if (repeatsFound)
			{
				int repeatLength, numRepeats, numSpacers;
				CRISPR currCRISPR;

				String repeat, spacer, prevSpacer;
				repeat = spacer = prevSpacer = "";

				if (outputFasta) {
					out.print(">");
					out.print(sequence.getName() + "\n");
				}

				//add 1 to each position, to offset programming languagues that begin at 0 rather than 1
				for (int k = 0; k < CRISPRVector.size(); k++)
				{	currCRISPR = (CRISPR)CRISPRVector.elementAt(k);
					if(!outputFasta) {
						out.print("CRISPR " + (k + 1) + "   Range: " + (currCRISPR.start() + 1) + " - " +  (currCRISPR.end() + 1) + "\n");
						out.print(currCRISPR.toString());
						out.print("Repeats: " + currCRISPR.numRepeats() + "\t" +  "Average Length: " + currCRISPR.averageRepeatLength() + "\t\t");
						out.print("Average Length: " +  currCRISPR.averageSpacerLength() + "\n\n");

						out.print("\n\n");
					}
					else {
						out.print(currCRISPR.toStringFasta());
					}
					
				}

				if(!outputFasta) {
					out.print("Time to find repeats: " + (repeatSearchEnd - repeatSearchStart) + " ms\n\n");
				}
			}

			if (!outputFasta) {
				out.print("\n");
				out.print("\n");
			}

			if (!repeatsFound && !outputFasta)
				out.print("No CRISPR elements were found.");

			out.close();
		}
		catch (Exception e)	{	System.err.println ("--Error writing to file-- \n");	}
	}
}
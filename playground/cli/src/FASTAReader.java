import java.util.*;
import java.io.*;

public class FASTAReader
{
	private String fileName;
   	private String sequence;
 	private String header;

    public FASTAReader(String f)
	{	fileName = f;
      	sequence = "";
     	header = "";
	}

	//does not check that all characters are ACGT
	public boolean isFASTA()
	{
		try
		{
			BufferedReader inputFile = new BufferedReader(new FileReader(fileName));

			String firstLine = inputFile.readLine();
			if (firstLine == null)
			{	inputFile.close();
				return false;
			}

			String secondLine = inputFile.readLine();
			if (secondLine == null)
			{	inputFile.close();
				return false;
			}

            if (firstLine.charAt(0) != '>')
			{	inputFile.close();
				return false;
			}

			inputFile.close();
		} catch (Exception e) { e.printStackTrace(); }

		return true;
	}

  	public void read()
	{
		try
		{
			BufferedReader inputFile = new BufferedReader(new FileReader(fileName));

			sequence = "";
			header = inputFile.readLine();

            StringBuffer sb = new StringBuffer();

			String currLine = inputFile.readLine();
			while(currLine != null)
			{	sb.append(currLine);
				currLine = inputFile.readLine();
			}

      		sequence = sb.toString();
			inputFile.close();
		} catch (Exception e) { e.printStackTrace(); }

	}

	public String getSequence()
	{	return sequence;
	}

   	public String getHeader()
	{	return header.substring(1, header.length());
	}
}

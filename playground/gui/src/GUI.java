import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.Vector;


public class GUI
{  public static void main(String[] args)
   {  JFrame frame = new SplitPaneFrame();

      frame.addWindowListener(
		  new WindowAdapter()
	      {  public void windowClosing(WindowEvent e)
	         {   System.exit(0);
	         }
	       }
	  );

	  frame.setTitle("CRISPR Recognition Tool (CRT) V1.0");
	  frame.setSize(850, 580);
      frame.show();
   }
}


class SplitPaneFrame extends JFrame
{  public SplitPaneFrame()
   {
	  /*************** menu bar section ***************/
	  /************************************************/
      JMenuBar bar = new JMenuBar();
      setJMenuBar(bar);

	 //**File
      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic('f');

	  JMenuItem openItem = new JMenuItem("Open");
	  openItem.setMnemonic('o');
	  openItem.addActionListener(new InputFileNameBrowseButtonHandler());
	  fileMenu.add(openItem);

      JMenuItem saveItem = new JMenuItem("Save");
      saveItem.setMnemonic('s');
      saveItem.addActionListener(new SaveAsTextHandler());
      fileMenu.add(saveItem);

      JMenuItem exitItem = new JMenuItem("Exit");
      exitItem.setMnemonic('x');
      exitItem.addActionListener(new ExitHandler());
      fileMenu.add(exitItem);
	  bar.add(fileMenu);

 	   //**Tools
      JMenu toolsMenu = new JMenu("Tools");
      toolsMenu.setMnemonic('t');

	  //search
	  JMenuItem searchItem = new JMenuItem("Search");
	  searchItem.setMnemonic('s');
	  searchItem.addActionListener(new FindRepeatsButtonHandler());
	  toolsMenu.add(searchItem);
	  bar.add(toolsMenu);


      /********** input file section **********/
      Font plain11 = new Font("", Font.PLAIN, 11);

      inputFileNameField = new JTextField( "", 13  );
      inputFileNameField.setFont(plain11);
      inputFileNameField.setEnabled(true);
      inputFileNameField.setToolTipText("Input File");
      inputFileNameBrowseButton = new JButton("Browse...");
      inputFileNameBrowseButton.setEnabled(true);
      inputFileNameBrowseButton.setFont(plain11);
      inputFileNameBrowseButton.setMargin(new Insets(3,3,3,3));
      inputFileNameBrowseButton.addActionListener(new InputFileNameBrowseButtonHandler());


      /********** settings **********/
      minNumRepeatsLabel = new JLabel();
      minNumRepeatsLabel.setFont(plain11);
      minNumRepeatsLabel.setText("Min Number Repeats      ");
      minNumRepeatsField = new JTextField( "3", 3  );
      minNumRepeatsField.setFont(plain11);
      minNumRepeatsField.setEnabled(true);

      minRepeatLengthLabel = new JLabel();
      minRepeatLengthLabel.setFont(plain11);
      minRepeatLengthLabel.setText("Min Repeat Length         ");
      minRepeatLengthField = new JTextField( "19", 3  );
      minRepeatLengthField.setFont(plain11);
      minRepeatLengthField.setEnabled(true);

      maxRepeatLengthLabel = new JLabel();
      maxRepeatLengthLabel.setFont(plain11);
      maxRepeatLengthLabel.setText("Max Repeat Length       ");
      maxRepeatLengthField = new JTextField( "38", 3  );
      maxRepeatLengthField.setFont(plain11);
      maxRepeatLengthField.setEnabled(true);

      searchWindowLengthLabel = new JLabel();
      searchWindowLengthLabel.setFont(plain11);
      searchWindowLengthLabel.setText("Search Window (6 - 9)  ");
      searchWindowLengthField = new JTextField( "8", 3  );
      searchWindowLengthField.setFont(plain11);
      searchWindowLengthField.setEnabled(true);

      minSpacerLengthLabel = new JLabel();
      minSpacerLengthLabel.setFont(plain11);
      minSpacerLengthLabel.setText("Min Spacer Length         ");
      minSpacerLengthField = new JTextField( "19", 3  );
      minSpacerLengthField.setFont(plain11);
      minSpacerLengthField.setEnabled(true);

      maxSpacerLengthLabel = new JLabel();
      maxSpacerLengthLabel.setFont(plain11);
      maxSpacerLengthLabel.setText("Max Spacer Length       ");
      maxSpacerLengthField = new JTextField( "48", 3  );
      maxSpacerLengthField.setFont(plain11);
      maxSpacerLengthField.setEnabled(true);


	  /********** bottom buttons **********/
	  JButton findRepeatsButton = new JButton("Find Repeats");
	  findRepeatsButton.setFont(plain11);
	  findRepeatsButton.setMargin(new Insets(3,3,3,3));
	  findRepeatsButton.addActionListener(new FindRepeatsButtonHandler());

	  JButton resetDefaultsButton = new JButton("Reset Defaults");
	  resetDefaultsButton.setFont(plain11);
	  resetDefaultsButton.setMargin(new Insets(3,3,3,3));
	  resetDefaultsButton.addActionListener(new ResetDefaultsButtonHandler());


	  //** panel1 will hold the input file section
	  JPanel panel1 = new JPanel();
	  panel1.setLayout(new GridBagLayout());

	  //** panel2 will hold the other input sections (repeats)
	  JPanel panel2 = new JPanel();
	  panel2.setLayout(new GridBagLayout());

	  //** panel3 will hold the other input sections (spacers)
	  JPanel panel3 = new JPanel();
	  panel3.setLayout(new GridBagLayout());

	  //** panel4 will hold the bottom buttons
	  JPanel panel4 = new JPanel();
	  panel4.setLayout(new GridBagLayout());

	  //** westPanel will hold panel1, panel2, panel4
	  //** and will appear on the west end of the screen
      JPanel westPanel = new JPanel();
      westPanel.setLayout(new GridBagLayout());

      //** constraints within panel1, panel2, panel3, panel4
      constraints = new GridBagConstraints();
      constraints.insets = new Insets(1,1,1,1);
      constraints.gridwidth = 1;
      constraints.gridheight = 1;
      constraints.gridx = 0;
      constraints.gridy = 0;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.fill = GridBagConstraints.HORIZONTAL;

	  //** constraints between panel1, panel2, panel3, panel4
	  sectionConstraints = new GridBagConstraints();
	  sectionConstraints.insets = new Insets(10,15,15,20);
      sectionConstraints.gridwidth = 1;
      sectionConstraints.gridheight = 1;
      sectionConstraints.gridx = 0;
      sectionConstraints.gridy = 0;
      sectionConstraints.anchor = GridBagConstraints.WEST;
      sectionConstraints.fill = GridBagConstraints.NONE;
      sectionConstraints.weightx = 0;
      sectionConstraints.weighty = 0;


      //** adds filename info to panel1
	  addComponents(panel1, inputFileNameField, inputFileNameBrowseButton);

	  //** adds components to panel2
      addComponents(panel2, minNumRepeatsLabel, minNumRepeatsField);
      addComponents(panel2, minRepeatLengthLabel, minRepeatLengthField);
	  addComponents(panel2, maxRepeatLengthLabel, maxRepeatLengthField);
	  addComponents(panel2, searchWindowLengthLabel, searchWindowLengthField);

	 //** add bottom buttons to panel3
      addComponents(panel3, minSpacerLengthLabel, minSpacerLengthField);
	  addComponents(panel3, maxSpacerLengthLabel, maxSpacerLengthField);

	  //** add bottom buttons to panel4
	  addComponents(panel4, findRepeatsButton, resetDefaultsButton);

	  //** add the three panels to the westPanel
	  addSection(westPanel, panel1);
	  addSection(westPanel, panel2);
	  addSection(westPanel, panel3);
	  addSection(westPanel, panel4);


	  /********** adds borders to each panel **********/
      BevelBorder bevelBorder = new BevelBorder(0);
      TitledBorder fileNameBorder = new TitledBorder(bevelBorder, "Input Data");
	  TitledBorder repeatSettingsBorder = new TitledBorder(bevelBorder, "Repeat Settings");
	  TitledBorder spacerSettingsBorder = new TitledBorder(bevelBorder, "Spacer Settings");

      panel1.setBorder(fileNameBorder);
      panel2.setBorder(repeatSettingsBorder);
      panel3.setBorder(spacerSettingsBorder);


	  //**  creates text area to hold output
	  Box box = Box.createHorizontalBox();
	  outputArea = new JTextArea("", 60, 35);
	  outputArea.setMargin(new Insets(6,10,5,5));
	  outputArea.setFont(new Font("", Font.PLAIN, 12));
	  box.add(new JScrollPane(outputArea));


      //** set up split panes to contain westPanel and outputArea
      splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, box);
      splitPane.setContinuousLayout(true);
      splitPane.setOneTouchExpandable(true);
      //splitPane.setDividerLocation(0.0);
      //splitPane.setLastDividerLocation(85);
      //splitPane.setDividerSize(20);
      getContentPane().add(splitPane, "Center");

   }  /********** end class SplitPaneFrame  **********/


  /********** private methods to add components to westPanel **********/
  /********************************************************************/
   private void addComponents(JPanel p, Component c1, Component c2)
   {  JPanel panel = new JPanel();

      GridBagConstraints gridConstr = new GridBagConstraints();
      gridConstr.insets = new Insets(3,3,3,3);
      gridConstr.gridwidth = 1;
      gridConstr.gridheight = 1;
      gridConstr.gridx = 0;
      gridConstr.gridy = 0;
      gridConstr.anchor = GridBagConstraints.WEST;
      gridConstr.fill = GridBagConstraints.NONE;
      gridConstr.weightx = 0;
      gridConstr.weighty = 0;

	  panel.setLayout(new GridBagLayout());

	  if (c1 != null)
	  	panel.add(c1, gridConstr);
	  gridConstr.gridx++;
	  if (c2 != null)
	  	panel.add(c2, gridConstr);

      p.add(panel, constraints);

      //advance row
      constraints.gridy++;
   }



   private void addSection(JPanel p, Component c)
   {  p.add(c, sectionConstraints);
      sectionConstraints.gridy++;
   }


   /********** additional private methods **********/
   private File openFile()
   {	JFileChooser fileChooser = new JFileChooser(fileToOpen);
   		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
   		int result = fileChooser.showOpenDialog(this);
   		if (result == JFileChooser.CANCEL_OPTION)
   			return null;
   		else
   		{	fileToOpen = fileChooser.getSelectedFile().getAbsolutePath();
			return fileChooser.getSelectedFile();
		}
	}

   private File getFile()
   {	JFileChooser fileChooser = new JFileChooser(".");
   		int result = fileChooser.showSaveDialog(this);
   		if (result == JFileChooser.CANCEL_OPTION)
   			return null;
   		else
   			return fileChooser.getSelectedFile();
	}


   /*** event listener classes to determine actions for various events ***/
   /**********************************************************************/
   class InputFileNameBrowseButtonHandler implements ActionListener
   {	public void actionPerformed(ActionEvent e)
   		{	File file = openFile();
   			if (file != null)
   				inputFileNameField.setText(file.getAbsolutePath());
   		}
   }


	class ExitHandler implements ActionListener
	{	public void	actionPerformed(ActionEvent event)
		{	System.exit(0);
		}
	}

	class ResetDefaultsButtonHandler implements ActionListener
	{	public void actionPerformed(ActionEvent event)
	        {
      			minNumRepeatsField.setText("3");
      			minRepeatLengthField.setText("19");
      			maxRepeatLengthField.setText("38");
      			searchWindowLengthField.setText("8");
      			minSpacerLengthField.setText("19");
      			maxSpacerLengthField.setText("48");
		}
	}

	class SaveAsTextHandler implements ActionListener
	{	public void actionPerformed(ActionEvent event)
		{	try
			{	File file = getFile();
				if (file != null)
				{	String fileName = file.getPath();
					OutputStream os = new FileOutputStream(fileName);
					PrintWriter out = new PrintWriter(os);
					outputArea.write(out);
					out.close();
				}

			}  catch (IOException e) { e.printStackTrace(); }
		}
	}


	class FindRepeatsButtonHandler implements ActionListener
	{	public void actionPerformed(ActionEvent event)
		{	outputArea.setText("");

          	String fileName = inputFileNameField.getText();
          	File inputFile = new File(fileName);

			if (inputFile.exists())
			{	System.out.println("\n\nReading file" + fileName);
				FASTAReader fastaReader = new FASTAReader(fileName);
				if (fastaReader.isFASTA())
				{	fastaReader.read();
					System.out.println("Reading file complete");
					sequence = new DNASequence(fastaReader.getSequence(), fastaReader.getHeader());
					findRepeats();
					/*
					if (!sequence.isDNASequence())
					{   outputArea.setText("");
						outputArea.append(sequence.getErrorLog());
					}
					else
						findRepeats();
					*/
				}
				else
				{   outputArea.setText("");
					outputArea.append("Not a valid FASTA file.");
				}
			}
			else
			{   outputArea.setText("");
				outputArea.append("File name does not exist.");
			}
		}
	}


	// ********************************************************************************** //
	// ************************* The Main Algorithm Begins Here ************************* //
	// ********************************************************************************** //
	private void findRepeats()
	{
		Vector CRISPRVector = new Vector();
		sequenceLength = sequence.length();
		int actualRepeatLength;
		boolean repeatsFound = false;

		CRISPR candidateCRISPR;
		String pattern;

		int minNumRepeats = Integer.parseInt(minNumRepeatsField.getText());
		int minRepeatLength = Integer.parseInt(minRepeatLengthField.getText());
		int maxRepeatLength = Integer.parseInt(maxRepeatLengthField.getText());
		int searchWindowLength = Integer.parseInt(searchWindowLengthField.getText());
		int minSpacerLength = Integer.parseInt(minSpacerLengthField.getText());
		int maxSpacerLength = Integer.parseInt(maxSpacerLengthField.getText());

		if ((searchWindowLength < 6) || (searchWindowLength > 9))
		{	searchWindowLength = 8;
			searchWindowLengthField.setText("8");
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

            if (endSearch < beginSearch)  //should nver occur
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
		System.out.println("Time to find repeats:  " + (repeatSearchEnd - repeatSearchStart) + " ms");
		//outputArea.append("Time to find repeats:  " + (repeatSearchEnd - repeatSearchStart) + " ms\n\n");

		outputArea.append("ORGANISM:  ");
		outputArea.append(sequence.getName() + "\n");
		outputArea.append("Bases:  " + sequence.length() + "\n\n\n");



		// ********************** Display CRISPR elements ********************** //
        // ********************************************************************* //
		if (repeatsFound)
		{
			int repeatLength, numRepeats, numSpacers;
			CRISPR currCRISPR;

			String repeat, spacer, prevSpacer;
			repeat = spacer = prevSpacer = "";

			//add 1 to each position, to offset programming languagues that begin at 0 rather than 1
			for (int k = 0; k < CRISPRVector.size(); k++)
			{	currCRISPR = (CRISPR)CRISPRVector.elementAt(k);
				outputArea.append("CRISPR  " + (k + 1) + "   Range: " + (currCRISPR.start() + 1) + " - " +  (currCRISPR.end() + 1) + "\n");
				outputArea.append(currCRISPR.toString());
				outputArea.append("Repeats:  " + currCRISPR.numRepeats() + "\t" +  "Average Repeat Length:  " + currCRISPR.averageRepeatLength() + "\t\t");
				outputArea.append("Average Spacer Length:  " +  currCRISPR.averageSpacerLength() + "\n\n");

				outputArea.append("\n\n");
			}
			outputArea.append("Time to find repeats:  " + (repeatSearchEnd - repeatSearchStart) + " ms\n\n");
		}

		outputArea.append("\n");
		outputArea.append("\n");
		if (!repeatsFound)
			outputArea.append("No CRISPR elements were found.");
	}

   private String fileToOpen = ".";
   DNASequence sequence = null;
   int sequenceLength = 0;

   private JLabel minNumRepeatsLabel, minRepeatLengthLabel, maxRepeatLengthLabel, searchWindowLengthLabel, minSpacerLengthLabel, maxSpacerLengthLabel;
   private JTextField inputFileNameField, minNumRepeatsField, minRepeatLengthField, maxRepeatLengthField, searchWindowLengthField, minSpacerLengthField, maxSpacerLengthField;
   private JButton inputFileNameBrowseButton;
   private JMenuItem outputText;
   private JTextArea outputArea;
   private JSplitPane splitPane;

   private GridBagConstraints constraints, sectionConstraints;
}

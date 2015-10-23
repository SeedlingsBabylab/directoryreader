import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DirectoryReader {

	static int spc_count=-1;
	static int targDir;
	static String mainDir;
	private static int checkVis,allFiles;
	private static String homeVis;
	private static String homeVisP;
	private static String homeVisPAud;
	private static String homeVisPVid;
	private static String homeVisC;
	private static String homeVisCAudA;
	private static String homeVisCAudT;
	private static String homeVisCVidA;
	private static String homeVisCVidT;
	private static String homeVisA;
	private static String homeVisAAud;
	private static String homeVisAVid;
	private static String[] homeVisit;
	private static String inLabVis;
	private static String inLabVisE;
	private static String inLabVisS;
	private static String inLabVisSA;
	private static String inLabVisSI;
	private static String[] inLabVisit;
	private static String getFiles;
	private static String notMoved="";//file format is not found
	private static String lastParent;
	private static int fileNum;
	private static int moved; //tracks if error moving, but file is found
	private static String movedTracker="";
	private static String lastParentMoved;
	private static ArrayList<String> movedFileList = new ArrayList<String>();

	static void setFiles(){
		//home visit folders
		homeVis = mainDir+"Home_Visit/";

		homeVisP = homeVis+"Processing/";
		homeVisPAud = homeVisP+"Audio_Files/";
		homeVisPVid = homeVisP+"Video_Files/";

		homeVisC = homeVis+"Coding/";
		homeVisCAudA = homeVisC+"Audio_Annotation/";
		homeVisCAudT = homeVisC+"Audio_Transcription/";
		homeVisCVidA = homeVisC+"Video_Annotation/";
		homeVisCVidT = homeVisC+"Video_Transcription/";

		homeVisA = homeVis+"Analysis/";
		homeVisAAud = homeVisA+"Audio_Analysis/";
		homeVisAVid = homeVisA+"Video_Analysis/";

		homeVisit=new String[]{homeVis,homeVisP,homeVisPAud,homeVisPVid,homeVisC,homeVisCAudA,homeVisCAudT,homeVisCVidA,homeVisCVidT,homeVisA,homeVisAAud,homeVisAVid};

		//in lab visit folders
		inLabVis = mainDir+"In-Lab_Visit/";

		inLabVisE = inLabVis+"Eyetracking/";

		inLabVisS = inLabVis+"Stimuli/";
		inLabVisSA = inLabVisS+"Audio_Stims/";
		inLabVisSI = inLabVisS+"Image_Stims/";

		inLabVisit = new String[]{inLabVis,inLabVisE,inLabVisS,inLabVisSA,inLabVisSI};
	}
//process gets all the files in the directory attached
	static void Process(File aFile) {
		spc_count++;
		String spcs = "";
		JDialog dialog = new JDialog();
		JLabel label = new JLabel("Please wait...");
		dialog.setLocationRelativeTo(null);
		dialog.setTitle("Status");
		dialog.add(label);
		dialog.pack();		
		dialog.setVisible(true);
		for (int i = 0; i < spc_count; i++)
			spcs += " ";
		if(aFile.isFile()){
			System.out.println(spcs + "[FILE] " + aFile.getName());
		checkFile(aFile);
		moveFile(aFile);
		if (fileNum==0){//if the is not identified (i.e. unknown file)
//			if (!(aFile.getName().substring(0, 1).matches("."))){
		if (!(aFile.getParent().equals(lastParent))){
			lastParent=aFile.getParent();
			notMoved = notMoved + "-----\n[DIR]: " + aFile.getParent()+"\n";
			}
		notMoved = notMoved + aFile.getName()+ "\n";
//		}
		}
		if (moved==0){// if the file is not moved (those that can't be replaced)
			if (!(aFile.getParent().equals(lastParentMoved))){
				lastParentMoved=aFile.getParent();
				movedTracker= movedTracker + "-----\n[DIR]: " + aFile.getParent()+"\n";
				}
			movedTracker = movedTracker + aFile.getName()+ "\n";
		}
		}
		else if (aFile.isDirectory()) {
			System.out.println(spcs + "[DIR] " + aFile.getName());
			if (aFile.getName().equals("corrupt")){ //if corrupt folder, moves this first
				if(aFile.renameTo(new File(homeVisPVid+aFile.getName()))){
					System.out.println("File moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			File[] listOfFiles = aFile.listFiles();
			if(listOfFiles!=null) {
				for (int i = 0; i < listOfFiles.length; i++){
					Process(listOfFiles[i]);
				}
			} else {
				System.out.println(spcs + " [ACCESS DENIED]");
			}
		}
		spc_count--;
		dialog.dispose();
		
	}
	//moveFile uses the the results of check file to move the checked file into the right directory
	static void moveFile(File file) {
		if (fileNum>0){//if it's a known file
			if (fileNum==8){
				if(file.renameTo(new File(inLabVisE+file.getName()))){//moves files with fileNum=8 into inLabVisE folder (established above)
					System.out.println("File moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			if (fileNum>=9&&fileNum<=15||fileNum==48){
				if(file.renameTo(new File(homeVisPVid+file.getName()))){
					System.out.println("File moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}

			else if ((fileNum>=17&&fileNum<=22)||fileNum==47){				
				if(file.renameTo(new File(homeVisPAud+file.getName()))){
					System.out.println("File moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}

			else if (fileNum==23||fileNum==24||fileNum==41||fileNum==49||fileNum==50){				
				if(file.renameTo(new File(homeVisCVidA+file.getName()))){
					System.out.println("File moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			else if (fileNum==29||fileNum==30||fileNum==31||fileNum==42||fileNum==51||fileNum==52){				
				if(file.renameTo(new File(homeVisCAudA+file.getName()))){
					System.out.println("File is moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			else if (fileNum>=32&&fileNum<=36||fileNum==38||fileNum==43||fileNum==44||fileNum==45){				
				if(file.renameTo(new File(homeVisAAud+file.getName()))){
					System.out.println("File is moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			else if (fileNum>=25&&fileNum<=28||fileNum==37){				
				if(file.renameTo(new File(homeVisAVid+file.getName()))){
					System.out.println("File is moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			else if (fileNum==39){				
				if(file.renameTo(new File(homeVisA+file.getName()))){
					System.out.println("File is moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			else if (fileNum==40){				
				if(file.renameTo(new File(inLabVisS+file.getName()))){
					System.out.println("File is moved successfully!");
					moved = 1;
				}else{
					System.out.println("File failed to move!");
					moved = 0;
				}
			}
			if(moved==1){
				movedFileList.add(file.getName());
			}
		}else{
		}
	}

	static void checkFile(File fileIn){
		fileNum=0;//file starts off as unknown
		moved=9;//file starts off as not moved
		//checks the file for the correct regex in these following statements, assigns fileNum if it's recognized
		if (fileIn.getName().matches("(?i:\\d+_\\d+.edf)")){
			fileNum=7;
		}else if (fileIn.getName().matches("(?i:\\d+_\\d+_datasource.xlsx)")){
			fileNum=8;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_datasource.xls)")){
			fileNum=8;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_main\\d*.mts)")){
			fileNum=9;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_main\\d*.mts.sfk)")){
			fileNum=9;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_up\\d*.mp4)")){
			fileNum=10;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_up\\d*.mp4.sfk)")){
			fileNum=10;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_parent\\d*.mp4)")){
			fileNum=48;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_parent\\d*.mp4.sfk)")){
			fileNum=48;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_down\\d*.mp4)")){
			fileNum=11;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+ms_down\\d*.mp4.sfk)")){
			fileNum=11;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_combined.veg.bak)")){
			fileNum=12;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_combined.veg)")){
			fileNum=13;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_video.mp4)")){
			fileNum=14;
		}
		else if (fileIn.getName().matches("(log.txt)")){
			fileNum=15;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+.wav)")){
			fileNum=17;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+.cha)")){
			fileNum=18;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+.cex)")){
			fileNum=19;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_audio.aup)")){
			fileNum=20;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_silences.txt)")){
			fileNum=21;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_silences_added.cex)")){
			fileNum=22;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_subregions.cex)")){
			fileNum=47;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+.opf)")){
			fileNum=23;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus.opf)")){
			fileNum=24;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_final.opf)")){
			fileNum=49;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus_final.opf)")){
			fileNum=50;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus_coder[a-zA-Z0-9]+.csv)")){
			fileNum=25;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_video_ready_for_check.csv)")){
			fileNum=26;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_video_check[a-zA-Z0-9]+.csv)")){
			fileNum=27;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_video_check[a-zA-Z0-9]+.xlsx)")){
			fileNum=28;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_video_check[a-zA-Z0-9]+.xls)")){
			fileNum=28;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+.cex)")){
			fileNum=29;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+.cha)")){
			fileNum=29;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_final.cex)")){
			fileNum=51;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_final.cha)")){
			fileNum=51;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_differences.cex)")){
			fileNum=30;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus.cex)")){
			fileNum=31;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus_final.cex)")){
			fileNum=52;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_stat.frq_consensus_coder[a-zA-Z0-9]+.xls)")){
			fileNum=32;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_stat.frq_consensus_coder[a-zA-Z0-9]+.xlsx)")){
			fileNum=32;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_stat.frq_consensus_coder[a-zA-Z0-9]+.csv)")){
			fileNum=32;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_audio_wordtable_coder[a-zA-Z0-9]+.txt)")){
			fileNum=33;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_audio_ready_for_check.csv)")){
			fileNum=34;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_audio_check[a-zA-Z0-9]+.csv)")){
			fileNum=35;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_audio_check[a-zA-Z0-9]+.xlsx)")){
			fileNum=36;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+_audio_check[a-zA-Z0-9]+.xls)")){
			fileNum=36;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_videocount.txt)")){
			fileNum=37;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_audiocount.txt)")){
			fileNum=38;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_unique.txt)")){
			fileNum=39;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_stimuli.xlsx)")){
			fileNum=40;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_stimuli.xls)")){
			fileNum=40;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_Video_Coding_Issues.docx)")){
			fileNum=41;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_Video_Coding_Issues.doc)")){
			fileNum=41;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_Audio_Coding_Issues.docx)")){
			fileNum=42;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_Audio_Coding_Issues.doc)")){
			fileNum=42;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus.frq.cex)")){
			fileNum=43;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus.frq.csv)")){
			fileNum=43;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_coder[a-zA-Z0-9]+.frq.cex)")){
			fileNum=43;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_consensus_coder[a-zA-Z0-9]+.csv)")){
			fileNum=44;
		}
		else if (fileIn.getName().matches("(?i:\\d+_\\d+_lena5min.csv)")){
			fileNum=45;
		}
	}
	static void createFiles(){
		File newFile;
		//if just home visit
		for (int i=0;i<homeVisit.length;i++){
			newFile = new File(homeVisit[i]);
			// if the directory does not exist, create it
			System.out.println(newFile.getAbsolutePath());
			if (!newFile.exists()) {
				System.out.println("creating directory: " + newFile.getPath());
				boolean result = false;

				try{
					newFile.mkdirs();
					result = true;
				} catch(SecurityException se){
					//handle it
				}        
				if(result) {    
					System.out.println("DIR created");  
				}
			}
		}
		//	//if in-lab visit
		if(checkVis==0){
			for (int i=0;i<inLabVisit.length;i++){
				newFile = new File(inLabVisit[i]);
				  // if the directory does not exist, create it
				  if (!newFile.exists()) {
				    System.out.println("creating directory: " + newFile.getPath());
				    boolean result = false;
		
				    try{
				        newFile.mkdirs();
				        result = true;
				     } catch(SecurityException se){
				        //handle it
				     }        
				     if(result) {    
				       System.out.println("DIR created");  
				     }
				  }
			}
		}
	}
	//creates all files in a loop
	static void createAllFiles(String mainDir2,String subNum){
		for (int month=6;month<19;month++){
		mainDir=mainDir2+subNum+"_"+String.format("%02d", month)+"/";
		setFiles();
		File newFile;
		//if just home visit
		if(month!=18){
		for (int i=0;i<homeVisit.length;i++){
			newFile = new File(homeVisit[i]);
			// if the directory does not exist, create it
			System.out.println(newFile.getAbsolutePath());
			if (!newFile.exists()) {
				System.out.println("creating directory: " + newFile.getPath());
				boolean result = false;

				try{
					newFile.mkdirs();
					result = true;
				} catch(SecurityException se){
					//handle it
				}        
				if(result) {    
					System.out.println("DIR created");  
				}
			}
		}
		}
		//	//if in-lab visit
		if(month%2==0){
			for (int i=0;i<inLabVisit.length;i++){
				newFile = new File(inLabVisit[i]);
				  // if the directory does not exist, create it
				  if (!newFile.exists()) {
				    System.out.println("creating directory: " + newFile.getPath());
				    boolean result = false;
		
				    try{
				        newFile.mkdirs();
				        result = true;
				     } catch(SecurityException se){
				        //handle it
				     }        
				     if(result) {    
				       System.out.println("DIR created");  
				     }
				  }
			}
		}
		}
	}
	public static void main(String[] args) {
		allFiles = JOptionPane.showConfirmDialog(
			    null,
			    "Create all files?",
			    "Directory Reader",
			    JOptionPane.YES_NO_OPTION);
		if (allFiles==0){//if all files
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("CREATE ALL FILES: Choose the directory to create a set of files");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " + chooser.getSelectedFile().getPath());
				String mainDir2 = chooser.getSelectedFile().getPath()+"/";
				mainDir2=mainDir2.replace("\\","/");
		        JTextField field1 = new JTextField();
		        JPanel panel = new JPanel(new GridLayout(0, 1));
		        panel.add(new JLabel("subID:"));
		        panel.add(field1);
		        int result = JOptionPane.showConfirmDialog(null, panel, "Test",
		            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        if (result == JOptionPane.OK_OPTION) {
//					createAllFiles(field1.getText());
		        	createAllFiles(mainDir2,field1.getText());
		        } else {
		            System.out.println("Cancelled");
		        }
				}
		}else{//if not creating all files
		checkVis = JOptionPane.showConfirmDialog(
				    null,
				    "Does this month include an in-lab visit?",
				    "Type of Visit",
				    JOptionPane.YES_NO_OPTION);
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("CONFIRM FILES: Choose the directory where you want to save the files");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile().getPath());
			mainDir=chooser.getSelectedFile().getPath()+"/";
			mainDir=mainDir.replace("\\","/");	
			setFiles();
			createFiles();

			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Choose the directory of files to organize");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " + chooser.getSelectedFile().getPath());
				getFiles=chooser.getSelectedFile().getPath()+"/";
				getFiles=getFiles.replace("\\","/");	
				File aFile = new File(getFiles);
				Process(aFile);//this starts the process
				// create a JTextArea
			      JTextArea textArea = new JTextArea(25, 50);//printing the results
			      ArrayList <String> listClone = new ArrayList<String>(); 
		           for (String string : movedFileList) {
		               if(string.toLowerCase().contains("_final".toLowerCase())){
		                   listClone.add(string);
		               }
		           }
		           String finalFiles ="The following final files were moved: \n\n";
		           for (String string : listClone){
			      finalFiles = finalFiles+string+"\n";
		           }
		          textArea.setText("\n\n");
			      textArea.setText(finalFiles+"\n\n Unknown Files \n\n"+notMoved+"\n\n Files not replaced: \n\n" + movedTracker);
			      textArea.setEditable(false);
			       
			      // wrap a scrollpane around it
			      JScrollPane scrollPane = new JScrollPane(textArea);
			       
			      // display them in a message dialog
			      JOptionPane.showMessageDialog(null, scrollPane);
			}else{
				System.out.println("No directory selected!");
			}
		} else {
			System.out.println("No Selection ");
		}
		}
	}

}
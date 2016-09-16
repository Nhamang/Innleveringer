package no.uio.ifi.alboc.chargenerator;

/*
 * module CharGenerator
 */

import java.io.*;
import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;

/*
 * Module for reading single characters.
 */
public class CharGenerator {
    public static char curC, nextC;
	
    private static LineNumberReader sourceFile = null;
    private static String sourceLine;
    private static int sourcePos;
	
    public static void init() {
	try {
	    sourceFile = new LineNumberReader(new FileReader(AlboC.sourceName));
	} catch (FileNotFoundException e) {
	    Error.error("Cannot read " + AlboC.sourceName + "!");
	}
	sourceLine = "";  sourcePos = 0;  curC = nextC = ' ';
	readNext();  readNext();
    }
	
    public static void finish() {
	if (sourceFile != null) {
	    try {
		sourceFile.close();
	    } catch (IOException e) {
		Error.error("Could not close source file!");
	    }
	}
    }
	//saw the discussion on the course forum. Found that setting sourceFile to null when readLine is null
	//then testing if the is null or not.
    public static boolean isMoreToRead() {
		if(sourceFile==null){
			return false;
		}else{
			return true;
		}
	//-- Must be changed in part 0:
	//return false;
    }
	
    public static int curLineNum() {
	return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }
	
    public static void readNext() {
	curC = nextC;
	if (! isMoreToRead()) return;
	/*Has to set nextC as  char from sourceFile, needs to check if more on line and set new line.
	can be done be testing position to the length of the line.
	*/
	//so the while loop will go again if line starts with #
	boolean comment=false;
	//Variable to let the nextToken to null 
	String appendS="     ";
	char [] lineArray;
	int lineNumber;
	while((sourceLine.length() <= sourcePos)&& sourcePos!=-1){
		try{
			sourceLine=sourceFile.readLine();
			if(sourceLine!=null){
				lineNumber=curLineNum();
				Log.noteSourceLine(curLineNum(), sourceLine);
				sourceLine+=appendS;
				lineArray=sourceLine.toCharArray();
			}else{
				//Set sorceFile to null so the isMoreToRead returns false
				sourceFile.close();
				sourceFile=null;
				sourceLine=appendS;
			}
		}catch(IOException e){
			Error.error("Error reading file");
		}
		//Checks if line is a comment line, 
		//if it is it sets position to same size as the line of the line
		//So the while loop will go again;
		sourcePos=0;
		if(sourceLine.charAt(0)=='#'){
			//sourcePos=-1;
			//comment=true;
			sourcePos=sourceLine.length()+1;
		}
		//sourcePos=0;
	}
	nextC=sourceLine.charAt(sourcePos);
	sourcePos+=1;
	//-- Must be changed in part 0:
    }
}

package no.uio.ifi.alboc.scanner;

/*
 * module Scanner
 */

import no.uio.ifi.alboc.chargenerator.CharGenerator;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * Module for forming characters into tokens.
 */
public class Scanner {
    public static Token curToken, nextToken;
    public static String curName, nextName;
    public static int curNum, nextNum;
    public static int curLine, nextLine;
	
    public static void init() {
		readNext();
		readNext();
	//-- Must be changed in part 0:
    }
	
	//There should not be any need for a finish on his class
    public static void finish() {
		//nextToken=eofToken;
	//-- Must be changed in part 0:
    }
	
    public static void readNext() {
	curToken = nextToken;  curName = nextName;  curNum = nextNum;
	curLine = nextLine;

	nextToken = null;
	//Rewrite to switch case?
	//Goes through all the possibilties the curC can be and sets nextToken
	while (nextToken == null) {
	    nextLine = CharGenerator.curLineNum();
		
		//Ignore c, nC
		char c = CharGenerator.curC;
		char nC= CharGenerator.nextC;
	    if (!CharGenerator.isMoreToRead()) {
			nextToken = eofToken;
	    }else if((CharGenerator.curC=='/') && (CharGenerator.nextC=='*')){
		//else if(c.equals('/') && nC.quals('*')){ 
		//Finds comment section 
			//Need the next two characters
			CharGenerator.readNext();
			CharGenerator.readNext();
			
			c=CharGenerator.curC;
			nC=CharGenerator.nextC;
			while((CharGenerator.curC!='*')||(CharGenerator.nextC!='/')){
				//Note to self: might need comment didn't end
				if(!CharGenerator.isMoreToRead()){
					Error.error("Comment never stopped");
				}
				CharGenerator.readNext();
				c=CharGenerator.curC;
				nC=CharGenerator.nextC;
			}
			//Comments have ended, need's two new char
			CharGenerator.readNext(); CharGenerator.readNext();
			}else if(CharGenerator.curC==' ' ||CharGenerator.curC=='\n'||CharGenerator.curC=='\t'){
				CharGenerator.readNext();
			/*}else if(CharGenerator.curC==' '){
				CharGenerator.readNext();
			*/
			}else if(CharGenerator.curC=='*'){//Checklist from here down
				nextToken=starToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='+'){
				nextToken=addToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='&'){
				nextToken=ampToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC==','){
				nextToken=commaToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='/'){
				nextToken=divideToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='<'){
				if(CharGenerator.nextC=='='){
					nextToken=lessEqualToken;
					CharGenerator.readNext();
					CharGenerator.readNext();
				}else {
					nextToken=lessToken;
					CharGenerator.readNext();
				}
			}else if(CharGenerator.curC=='>'){
				if(CharGenerator.nextC=='='){
					nextToken=greaterEqualToken;
					CharGenerator.readNext();
					CharGenerator.readNext();
				}else{
					nextToken=greaterToken;
					CharGenerator.readNext();				
				}
			}else if((""+CharGenerator.curC+CharGenerator.nextC).equals("!=")){
				nextToken=notEqualToken;
				CharGenerator.readNext();
				CharGenerator.readNext();
			}else if((""+CharGenerator.curC+CharGenerator.nextC).equals("==")){
				nextToken=equalToken;
				CharGenerator.readNext();
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='='){
				nextToken=assignToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='['){
				nextToken=leftBracketToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='{'){
				nextToken=leftCurlToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='('){
				nextToken=leftParToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC==']'){
				nextToken=rightBracketToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='}'){
				nextToken=rightCurlToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC==')'){
				nextToken=rightParToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC==';'){
				nextToken=semicolonToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='-'){
				nextToken=subtractToken;
				CharGenerator.readNext();
			}else if(CharGenerator.curC=='\''){
				nextToken=numberToken;
				nextNum=CharGenerator.nextC;
				CharGenerator.readNext();
				if(CharGenerator.nextC!='\''){
					Error.error("Started with \' but did not end");
				}
				CharGenerator.readNext();
				CharGenerator.readNext();
			}else if(Character.isDigit(CharGenerator.curC)){
				String digit="";
				nextToken=numberToken;
				while(Character.isDigit(CharGenerator.curC)){
					digit+=CharGenerator.curC;
					CharGenerator.readNext();
				}
				try{
					nextNum=Integer.parseInt(digit);
				}catch(NumberFormatException e){
					Error.error("Number to big");
				}
			}
		
		else{//For longer token, names or illegal characters
			if(isLetterAZ(CharGenerator.curC)){
				String s="";
				while(isLetterAZ(CharGenerator.curC)||CharGenerator.curC=='_'||Character.isDigit(CharGenerator.curC)){
					s+=CharGenerator.curC;
					CharGenerator.readNext();
				}
				if(s.equals("else")){
					nextToken=elseToken;
				}else if(s.equals("for")){
					nextToken=forToken;
				}else if(s.equals("if")){
					nextToken=ifToken;
				}else if(s.equals("int")){
					nextToken=intToken;
				}else if(s.equals("return")){
					nextToken=returnToken;
				}else if(s.equals("while")){
					nextToken=whileToken;
				}else{
					nextToken=nameToken;
					nextName=s;
				}
			}else 
			//-- Must be changed in part 0:
			{
				Error.error(nextLine,
					"Illegal symbol: '" + CharGenerator.curC + "'!");
			}
		}
	}
	Log.noteToken();
    }
	
    private static boolean isLetterAZ(char c) {
		if(((c>='a' && c<='z') || (c>='A' && c<='Z'))){
			return true;
		}else{
			return false;
		}
    }
	
    public static void check(Token t) {
	if (curToken != t)
	    Error.expected("A " + t);
    }
	
    public static void check(Token t1, Token t2) {
	if (curToken != t1 && curToken != t2)
	    Error.expected("A " + t1 + " or a " + t2);
    }
	
    public static void skip(Token t) {
	check(t);  readNext();
    }
	
    public static void skip(Token t1, Token t2) {
	check(t1,t2);  readNext();
    }
}

import java.io.*;
import java.util.*;
class oblig3{
	public static void main(String [] argv){
		program running;
	
		if(argv.length>=2){
			running = new program(-1, argv[0], argv[1]);
		}else if(argv.length==1 && argv[0].equals("test")){
			for(int i = 0; i<=9; i++){
				running = new program(i, "null","null");
			}
		}else{
			System.out.println("Usage: java oblig3 needle haystack");
			System.out.println("Run a test: oblig3 test");
		}
	}
}

class program{
	int antWildcard;
	char[] needle, hayStack;
	String output="", inneedle, inhaystack;
	String needles[] ={"bne", "bn_", "b__", "_ne", "__n", "b_e", "_n_", "___",  ""};
	String testHaystack = "fbnefhvbhhbsfbgftwcsdbbnevieuibsfbgftkbwovbbneoibibosfbgft";
	Integer [] index;
	
	program(int type, String needleFile, String textFile){
		if(type==9){
			needle="n__".toCharArray();
			hayStack="".toCharArray();
			inneedle="test needle"+type;
			inhaystack="test haystack"+type;
		}else if(type>=0){
			needle=testNeedle(type);
			hayStack=testHaystack.toCharArray();
			inneedle="test needle"+type;
			inhaystack="test haystack"+type;
		}else{
			needle=readNeedle(needleFile);
			hayStack=readHaystack(textFile);
			inneedle=(needleFile.replace(".txt", ""));
			inhaystack=(textFile.replace(".txt", ""));
		}
		
		antWildcard=countWildCards(needle);
		
		String killThis = killCode(needle, hayStack, antWildcard);
		if(killThis != null){
			output+=killThis;
			saveOutput(inneedle, inhaystack);
			return;
		}
		
		
		
		printInfo(needle, hayStack);
		output+="\n\n";
		
		index=searchText(needle, hayStack);
		
		printIndex(index, needle, hayStack);
		
		saveOutput(inneedle, inhaystack);
	}
	
	/**
	* Creates a BadCharShift array the size of 256.
	*
	* @param needles The needle used to create a BadCharShift Table
	* @return a badCharShift array.
	*/
	public int [] initBadshift(char [] needles){
		int [] tmpBad = new int[256];
		int antWild = countWildCards(needles);
		
		for(int i = 0; i<tmpBad.length; i++){
			tmpBad[i]=needles.length;
		}
		
		for(int i = 0; i<needles.length-1; i++){
			tmpBad[needles[i]] = ((needle.length-1)-i);
		}
		
		return tmpBad;
	}
	
	/**
	*	An extended Boyer Moore Horspool the accepts wildcards.
	*
	*	@param curNeedle the needle this algorithm is looking for
	*	@param curHaystack the haystack to search trough
	*	@return an integer array with all the positions of the needle
	*/
	public Integer [] searchText(char [] curNeedle, char [] curHaystack){
		
		List<Integer> found = new ArrayList<Integer>();
		//char [] curNeedle = "UCKY_".toCharArray();
		//char [] curHaystack = "MUCKYDUCKCLUCKDUCKYCLUCKYDUCKMUCKLUCKMCLUCKYDUCKCLUCKDUXCLUCKX".toCharArray();
		int offset = 0;
		int scan = 0;
		int last;
		//int last = curNeedle.length-1;
		int posWild=0; //The position of the last non-end wildcard
		int endWild=0;	//The amount of wildcards on the end of the needle
		int maxoffset;	
		
		
		int [] tmpBad = new int[256];
		for(int i = curNeedle.length-1; i>=0; i--){
			if(curNeedle[i]=='_'){
				endWild++;
			}
			else{
				break;
			}
		}

		String tmpNeedle = new String(curNeedle);
		tmpNeedle=tmpNeedle.substring(0, curNeedle.length-endWild); //new needle without wildcards on the end
		curNeedle=tmpNeedle.toCharArray();

		String tmpHaystack = new String(curHaystack);
		tmpHaystack = tmpHaystack.substring(0, curHaystack.length-endWild); //new haystack where last will match wildcards on end of needle
		curHaystack=tmpHaystack.toCharArray();
		
		//Finds the position of the last wildcard of the needle
		for(int i = 0; i<curNeedle.length; i++){
			if(curNeedle[i]=='_'){
				posWild=i;
			}
		}
		//Selution to a needle with only one char
		if(curNeedle.length==1){
			for(int i=0; i<tmpBad.length; i++){
				tmpBad[i]=1;
			}
		}else{
			for(int i = 0; i<tmpBad.length; i++){
				tmpBad[i]=curNeedle.length-posWild-1;
			}

		
			for(int i = posWild; i<curNeedle.length-1; i++){
				tmpBad[curNeedle[i]] = ((curNeedle.length-1)-i);
			}
		}
		
		
		maxoffset=curHaystack.length-curNeedle.length;
		last=curNeedle.length-1;
		while (offset<=maxoffset){
			//System.out.println("while");
			
			for(scan = last; curNeedle[scan] == curHaystack[scan+offset] || curNeedle[scan] == '_'; scan--){
				if(scan == 0){
					found.add(offset);
					break;
				}
			}
			/*
			if(tmpBad['_']<tmpBad[curHaystack[offset+last]]){
				offset+=tmpBad['_'];
			}else{
				offset+=tmpBad[curHaystack[offset + last]];
			}*/
			offset+=tmpBad[curHaystack[offset+last]];
		}
		
		return found.toArray(new Integer[found.size()]);
	}
	
	/**
	*	Prints all matches of the needle.
	*
	*	@param idx an array of all the positions the needle was found
	*	@param n the needle that was found.
	*	@param h the haystack that was searched.
	*/
	public void printIndex(Integer [] idx, char [] n, char [] h){
		System.out.println("");
		for(int i = 0; i< idx.length; i++){
			System.out.print("Found: ");
			for(int j = (int)idx[i]; j < ((int)idx[i]+n.length); j++){
				System.out.print(h[j]);
			}
			System.out.println(" on index: "+ idx[i]);
			
			output+="\n*--------------------------*";
			output+="\n|"+(i+1)+" Found: ";
			for(int j = (int)idx[i]; j < ((int)idx[i]+n.length); j++){
				output+=h[j];
			}
			output+=" on index: "+idx[i]+"|";
			output+="\n*--------------------------*";
		}
	}
	
	/**
	*	Tests if the code has to be ended and returns
	*	an error message if it should be killed.
	*	@param needles the needle that will be tested
	*	@param haystacks the haystack that will be tested
	*	@param the amount of wildcards
	*	@return String/null 
	*/
	public String killCode(char[] needles, char [] haystacks, int cards){
		if(needles.length == 0 || needles == null){
			System.out.println("Needle is empty or null");
			return ("Needle is empty or null");
		}else if(haystacks.length == 0 || haystacks == null){
			System.out.println("Haystack is empty or null");
			return ("Haystack is empty or null");
		}else if(needles.length == cards){
			System.out.println("Needle is only wildcards");
			return ("Needle is only wildcards");
		}else if(needles.length >= haystacks.length){
			System.out.println("Needle is the same size or bigger then haystack");
			return ("Needle is the same size or bigger then haystack");
		}
		
		return null;
	}
	
	/**
	*	Counts the amount of wildcards in a char array.
	*
	*	@param counting a char array to be counted for wildcards
	*	@return the number of wildcards
	*/
	public int countWildCards(char [] counting){
		int tmp=0;
		for(char c : counting){
			if(c=='_'){
				tmp++;
			}
		}
		
		return tmp;
	}

	/**
	*	prints the needle and the haystack
	*
	*	@param needle a char array with a needle.
	*	@param hayStack a char array with a haystack
	*/
	public void printInfo(char[] needle, char[] hayStack){
		
		System.out.print("\nNeedle: \n");
		output+="The needle was: \n";
		for(int i=0; i<needle.length; i++){
			System.out.print(needle[i]);
			output+=needle[i];
		}
		
		System.out.println("\n\nHaystack");
		output+="\n\nThe haystack was:\n";
		for(int i=0; i<hayStack.length; i++){
			System.out.print(hayStack[i]);
			output+=hayStack[i];
			if((i!=0)&&(i%50==0)){
				output+="\n";
			}
		}
		System.out.println("\n");
	}

	/**
	*	reads a needle from a file and 
	*
	*	@param filename the name of the needle file to open and read.
	*	@return char array containing needle
	*/
	public char [] readNeedle(String filename){
		try{
			
			File needleFile = new File(filename);
			Scanner needleReader = new Scanner(needleFile);
			
			String needleString = "";
			needleString+=needleReader.nextLine();
			while(needleReader.hasNextLine()){
				needleString+=" ";
				needleString+=needleReader.nextLine();
			}
			return needleString.toCharArray();
			
		}catch(FileNotFoundException e){
			System.out.println("Needle file was not found");
		}
		return null;
	}

	/**
	*	Reads haystack from file.
	*
	*	@param filename the file containg the haystack
	*	@return a char array with the haystack
	*/
	public char [] readHaystack(String filename){
		try{
			File haystackFile = new File(filename);
			Scanner haystackReader = new Scanner(haystackFile);
			String haystackString = "";
			haystackString+=haystackReader.nextLine();
			while(haystackReader.hasNextLine()){
				haystackString+=" ";
				haystackString+=haystackReader.nextLine();
			}
			return haystackString.toCharArray();
		}catch(FileNotFoundException e){
			System.out.println("Haystack file not found");
		}
		return null;
	}
	
	/**
	*	Saves the printed info on a text file.
	*
	*	@param s1 the name of the needle file
	*	@param s2 the name of the haystack file
	*/
	public void saveOutput(String s1, String s2){
		try{
			String outputFilename="Looking for "+s1+" in "+s2+".txt";
			File outFile = new File(outputFilename);
			FileWriter out = new FileWriter(outFile);
			out.write(output);
			out.close();
		}catch(IOException e){
			System.out.println("File not found");
		
		}
	}
	
	/**
	*	Gets a test needle and returns it in an array.
	*
	*	@param pos the position of a test needle
	*	@return a char array with the needle
	*/
	public char[] testNeedle(int pos){
		return needles[pos].toCharArray();
	}
}

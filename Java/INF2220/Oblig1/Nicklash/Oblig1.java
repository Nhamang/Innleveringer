import java.lang.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.*;

class oblig1{
	public static void main(String[]argv){
		spellcheck spell = new spellcheck();
		spell.menuloop();
	}	
}



class spellcheck{

	int treeDepth, depth;
	int antNodes=0;
	int antNodesCount=0;
	int antFound=0;
	char[] alphabet ="abcdefghijklmnopqrstuvwxyz".toCharArray();
	String lastWord="";
	String firstWord="";
	String outputText="";
	class Bnode{
		String word;
		Bnode left;
		Bnode right;
		int thisNode;
		public Bnode(String w){
			word=w;
			thisNode=antNodes;
			antNodes++;
		}
	}

	Bnode root;
	public void menuloop(){
		
		int choice=0;
		String word;
	
		
		Scanner tast = new Scanner(System.in, "UTF8");
		readTextFile();
	
		removeWord("familie");
		addWord("familie");
	
		while (choice!=1){
			printmenu();
			word=tast.next().toLowerCase();
			if(word.equalsIgnoreCase("q")){
				saveText();
				System.out.println("");
				System.out.println("Goodbye");
				choice=1;
			}else if(word.equalsIgnoreCase("print")){
				System.out.println(root.word);
			} else if(word.equalsIgnoreCase("+")){
				statistics();
			}else {
				if(isInt(word)==true){
					System.out.println("Please insert a word not numbers");
				}else{
					outputText+="\n";
					outputText+="\nYou entered: "+word;
					Bnode node = lookup(word);
					if(node!=null){
						outputText+="\nThe word entered was correct";
						System.out.println("\nThe word "+ node.word +" is spelled correct\n");
						outputText+="\n****************************************************";
					}else {
						long startTime=System.currentTimeMillis();
						String[]words=similarOne(word);
						outputText+="\nThe word you entered was not correct.";
						for(int i =0; i<words.length; i++){
							if(words[i]!=null){
								node=lookup(words[i]);
								if(node!=null){
									System.out.println("The word "+ node.word +" is a correct spelling when two of the letters where swapped around");
									outputText+="\nA similar word is: "+node.word+" when two of the letters where swapped around";
									antFound++;
								}
							}
						}
	
						words=similarTwo(word);
						for(int i =0; i<words.length; i++){
							if(words[i]!=null){
								node=lookup(words[i]);
								if(node!=null){
									System.out.println("The word "+ node.word +" is a correct spelling when a letter was swapped with another");
									outputText+="\nA similar word is: "+node.word+" when a letter was swapped with another";
									antFound++;
								}
							}
						}
	
					
						words=similarThree(word);
						for(int i =0; i<words.length; i++){
							if(words[i]!=null){
								node=lookup(words[i]);
								if(node!=null){
									System.out.println("The word "+ node.word +" is a correct spelling when a letter is removed");
									outputText+="\nA similar word is: "+node.word+" when a letter is removed";
									antFound++;
								}
							}
						}
	
					
						words=similarFour(word);
						for(int i =0; i<words.length; i++){
							if(words[i]!=null){
								node=lookup(words[i]);
								if(node!=null){
									System.out.println("The word "+ node.word +" is a correct spelling when a letter is added");
									outputText+="\nA similar word is: "+node.word+" when a letter is added";
									antFound++;
								}
							}
						}
						outputText+="\n";
	
						System.out.println("");
					
						long endTime=System.currentTimeMillis();
						long totalTime=endTime-startTime;
						System.out.println("It tok : "+ totalTime +" milliseconds to find similar word");
						outputText+="\nIt tok a total of "+ totalTime + "milliseconds to find similar words";
						System.out.println("and it found "+ antFound +" similar word");
						outputText+="\nand a total of "+antFound +" similar words was found";
						outputText+="\n****************************************************";
						antFound=0;
					}
				}
			}
		}
	}
	
	//Test if a string is only numbers
	public boolean isInt(String s){
		try{
			Integer.parseInt(s);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	/*Creates a new text file by the name utskrift_HH;mm;ss yyyy-MM-dd.
	*It the saves the string output to this file then closes it.
	*/
	public void saveText(){
		String date = new SimpleDateFormat("HH;mm;ss yyyy-MM-dd").format(Calendar.getInstance().getTime());
		String outname="Utskrift_"+date+".txt";
		try{
			File outFile = new File(outname);
			FileWriter fWriter = new FileWriter(outFile);
			PrintWriter writer=new PrintWriter(fWriter);
			writer.print(outputText);
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//Prints statistics to screen and string
	public void statistics(){
		outputText+="\n******************Statistics************************";
		outputText+="\n****************************************************";
		System.out.println("AntNode = "+ antNodes);
		outputText+=("\nAntall Noder = "+ antNodes);
		System.out.println("Depth of binary tree: "+ depth(root));
		outputText+=("\nDepth of this binary tree "+ depth(root));
		System.out.println("Average depth: "+ averageDepth());
		outputText+=("\nThe average depth is: "+ averageDepth());
		Bnode[] firstLast=firstLast(root);
		System.out.println("First word: "+ firstLast[0].word +". Last word: "+ firstLast[1].word);
		outputText+=("\nFirst Word: "+ firstLast[0].word +". Last word: "+ firstLast[1].word);
		System.out.println("");
		outputText+=("\n");
		int[]depths=new int[depth(root)];
		int tmpAnt=0;
		outputText+=("\nThese are the amount of nodes per depth:");
		for (int i=1; i<depths.length; i++){
			depths[i]=antOnDepth(root, i);
			System.out.println("Depth "+ i +" has "+ depths[i] +" nodes");
			outputText+=("\nDepth no: "+ i +" has "+ depths[i] +" nodes");
			tmpAnt+=depths[i];
		}
		System.out.println("ant: "+antNodes+" tmp: "+tmpAnt);
		System.out.println("");
	}
	
	public int antOnDepth(Bnode node, int i){
		if(node==null){
			return 0;
		}
		if(i==0){
			return 1;
		}
		
		return antOnDepth(node.left, i-1)+antOnDepth(node.right, i-1);
	}
	
	//Goes to the left and right most node for the smallest and largest word
	public Bnode[] firstLast(Bnode node){
		Bnode [] nodes = new Bnode[2];
		if(node==null){
			return null;
		}
		
		Bnode leftNode=node;
		Bnode rightNode=node;
		
		while (leftNode.left!=null){
			leftNode=leftNode.left;
		}
		nodes[0]=leftNode;
		while(rightNode.right!=null){
			rightNode=rightNode.right;
		}
		nodes[1]=rightNode;
		
		return nodes;
	}
	
	//Counts the amount of nodes
	public int antNodes(Bnode node, int nodes){
		if(node==null){
			return 0;
		}
		return nodes+antNodes(node.left, 1)+antNodes(node.right, 1);
	}
	
	//With depthSum it findes the average depth of this BST
	public double averageDepth(){
		if(root==null){
			return 0;
		}else{
			return depthSum(root, 1) / antNodes;
		}
	}
	
	double depthSum(Bnode node, int deep){
		if(node==null){
			return 0;
		}else return deep+depthSum(node.left, deep+1)+depthSum(node.right, deep+1);
	}
	
	//Finds the depth of the BST
	public int depth2(Bnode node){
		if(node==null){
			return 0;
		}
		
		int left = depth2(node.left);
		int right = depth2(node.right);
		
		int x = left > right ? left+1 : right+1;
		return x;
	}
	
	//Another function to find depth of BST
	public int depth(Bnode node){
		if(node!=null){
			treeDepth++;
			
			if(treeDepth>depth){
				depth=treeDepth;
			}
			depth(node.left);
			depth(node.right);
			
			treeDepth--;
		}
		return depth;
	}
	
	//Short function to use word to find a node with this word 
	public Bnode lookup(String w){
		Bnode node = lookup(w, root);
		return node;
	}
	
	//Uses a word to find node, but only under a specific node
	public Bnode lookup(String w, Bnode node){
		if(node==null){
			return null;
		}
		int compareNo=w.compareToIgnoreCase(node.word);
		if(compareNo<0){
			return lookup(w, node.left);
		}else if(compareNo>0){
			return lookup(w, node.right);
		}else {
			return node;
		}
	}
	
	//Simply prints a menu to screen
	public void printmenu(){
		System.out.println("Menu");
		System.out.println("Enter:");
		System.out.println("Any word other then keywords: Spellchecks the word");
		System.out.println("print: prints all the words in the database");
		System.out.println("+: Statistics.");
		System.out.println("q: Exit and save output file");
		System.out.println("");
		System.out.print(">  ");
	}
	
	//Reads from a text file and uses the function addWord to add to a BST
	public void readTextFile(){
		String filename="ordbok1.txt";
		File file = new File(filename);
		try {
			Scanner sc = new Scanner(file, "UTF8");
		
			while (sc.hasNextLine()){
				String thisLine=sc.nextLine();
				
				String splitLine[] = thisLine.split("\\s+");
				
				for(int i =0; i<(splitLine.length); i++){
					addWord(splitLine[i]);
					lastWord=splitLine[i];
				}
				
		
			}
			sc.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	//Adds word to a BST using a word
	public void addWord(String word){
		root = addWord(word, root);
	}
		
	//Adds word to BST under a specific node
	public Bnode addWord(String newWord, Bnode node){
		if (node==null){
			firstWord=newWord;
			return new Bnode(newWord);
		}
		int compareNo=newWord.compareToIgnoreCase(node.word);
		if(compareNo<0){
			node.left=addWord(newWord, node.left);
		}else if(compareNo>0){
			node.right=addWord(newWord, node.right);
		}
		return node;
	}
	
	//Removes a word from BST
	public void removeWord(String oldWord){
		root = removeWord(oldWord, root);
		
	}
	
	//Removes a word from BST under a specific node
	public Bnode removeWord(String w, Bnode node){
		if (node==null) return null;
		
		int compareNo=w.compareToIgnoreCase(node.word);
		
		if(compareNo<0){
			node.left = removeWord(w, node.left);
		}else if(compareNo>0){
			node.right = removeWord(w, node.right);
		}else{
			if(node.left == null){
				node=node.right;
			}else if(node.right==null){
				node=node.left;
			}
		}
		return node;
	}
	
	/**
	* This function takes in a String and sends it to the function swapOne.
	* it then returns array of strings containing similar words
	* @param word a string containing a word
	* @return	an array of strings		
	*/
	public String[] similarOne(String word){
		char[] word_array = word.toCharArray();
		char[] tmp;
		
		String[] words = new String[word_array.length-1];
		
		for(int i = 0; i < word_array.length-1; i++){
			tmp=word_array.clone();
			words[i] = swapOne(i, i+1, tmp);
		}
		return words;
		
	}
	
	/**
	* SwapOne takes in a word as a char array and two positions.
	* it swaps the letters of these positions with each other.
	*
	* @param	a	Position of a letter
	* @param	b	Position of another letter
	* @param	word	Original word
	* @return		A new word with the letters moved
	*/
	public String swapOne(int a, int b, char[] word){	
		char tmp = word[a];
		word[a]=word[b];
		word[b]=tmp;
		
		return new String(word);
	}
	
	/**
	* This function takes in a String and sends it to the function swapTwo.
	* it then returns array of strings containing similar words
	* @param word a string containing a word
	* @return	an array of strings		
	*/
	public String[] similarTwo(String word){
		char [] word_array = word.toCharArray();
		char [] tmp;
		
		String [] words = new String[((word_array.length)*alphabet.length)];
		
		
		for(int i=0; i<word_array.length;i++){
			for(int j=0; j<alphabet.length; j++){
				tmp=word_array.clone();
				words[(j+(alphabet.length*i))]=swapTwo(i, j, tmp);
			}
		}
		return words;
	}
	
	/**
	* SwapTwo takes in a word as a char array and two positions.
	* it swaps the letter in the first position with a letter from the alphabet.
	*
	* @param	a	Position of a letter
	* @param	b	Position of a letter in the alphabet
	* @param	word	Original word
	* @return		A new word with the letter replaced
	*/
	public String swapTwo(int a, int b, char[] word){
		word[a]=alphabet[b];
		return new String(word);
	}
		
	/**
	* This function takes in a String and sends it to the function swapThree.
	* it then returns array of strings containing similar words
	* 
	* @param word a string containing a word
	* @return	an array of strings		
	*/
	public String[] similarThree(String word){
		char [] word_array = word.toCharArray();
		char [] tmp;
		
		String [] words = new String[((word_array.length))];
		
		
		for(int i=0; i<word_array.length;i++){
			tmp=word_array.clone();
			words[i]=swapThree(i, tmp);

		}
		return words;
	}
	
	/**
	* SwapThree takes in a word as a char array and a positions.
	* it removes the letter of the position.
	*
	* @param	a	Position of a letter
	* @param	word	Original word
	* @return		A new word with the letters moved
	*/
	public String swapThree(int a, char[]word){
		char[]tmp = new char[word.length-1];
		
		for (int i=0; i<a; i++){
			tmp[i]=word[i];
		}
		for(int i=a; i< tmp.length; i++){
			tmp[i]=word[i+1];
		}
		return new String(tmp);
	}
	
	/**
	* This function takes in a String and sends it to the function swapFour.
	* it then returns array of strings containing similar words
	* 
	* @param word a string containing a word
	* @return	an array of strings		
	*/
	public String[] similarFour(String word){
		char [] word_array = word.toCharArray();
		char [] tmp;
		
		String [] words = new String[((word_array.length+1)*alphabet.length)];
		
		for(int i=0; i<word_array.length+1;i++){
			for(int j=0; j<alphabet.length; j++){
				tmp=word_array.clone();
				words[(j+(alphabet.length*i))]=swapFour(i, j, tmp);
			}
		}
		return words;
	}
	
	/**
	* SwapFour takes in a word as a char array and two positions.
	* it adds a letter  from the alphabet.
	*
	* @param	a	Position of a letter
	* @param	b	Position of letter in the alphabet
	* @param	word	Original word
	* @return		A new word with the letters moved
	*/
	public String swapFour(int a, int b, char[]word){
		char[]tmp = new char[word.length+1];
		
		if(a==(word.length+1)){
			tmp[tmp.length+1]=alphabet[b];
		}
		for (int i=0; i<a; i++){
			tmp[i]=word[i];
		}
		tmp[a]=alphabet[b];
		for(int i=a; i< word.length; i++){
			tmp[i+1]=word[i];
		}
		
		return new String(tmp);
	}
}

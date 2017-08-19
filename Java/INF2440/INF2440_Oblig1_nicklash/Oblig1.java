import java.util.*;
import java.util.concurrent.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class Oblig1{
	public static void main(String [] args){
		if(args.length < 1 || args.length > 2){
			System.out.println("Run: \n>oblig1 <amount>");
		}else{
			int n = Integer.parseInt(args[0]);
			int p = Runtime.getRuntime().availableProcessors();
			if(args.length==2){
				p = Integer.parseInt(args[1]);
			}
			
			int [] mainArray = makeArray(n);
			int [] seqArray = mainArray.clone();
			int [] parArray = mainArray.clone();
			int [] sortArray = mainArray.clone();
			
			int arrayLength = 9;
			
			double [] seqTime = new double [arrayLength];
			double [] parTime = new double [arrayLength];
			double [] sortTime = new double [arrayLength];
			
			double totalPar = 0.0;
			double totalSeq = 0.0;
			double totalSort = 0.0;
			double seqMedian = 0.0;
			double parMedian = 0.0;
			double sortMedian = 0.0;
			double speedup = 0.0;
			
			
			Program seq;
			Program par;
			
			String output = "";
			
			/*
			System.out.println("\nBefore\n");
			for(int i = 0; i< k; i++){
				System.out.print(mainArray[i]+ " ");
				if(i == 50){
					System.out.println("");
				}
				
			}
			System.out.println("\nAfter\n");
			*/
			
			//collecting data
			for(int i = 0; i<seqTime.length; i++){
				
				seqArray = mainArray.clone();
				parArray = mainArray.clone();
				sortArray = mainArray.clone();
				
				seq = new Program(n, seqArray);
				par = new Program(n, parArray, p);
				
				seqTime[i] = seq.utforSeq();
				parTime[i] = par.utforPar();
				sortTime[i] = useSort(sortArray.clone());
				
				totalSeq += seqTime[i];
				totalPar += parTime[i];
				totalSort += sortTime[i];
			}
			
			System.out.println("");
			
			Arrays.sort(sortArray);
			
			/*
			System.out.println("Sort: ");
			for(int i = n-1; i>=n-50; i--){
				System.out.print(sortArray[i] +" ");
			}
			*/
			
			seqMedian = (totalSeq/(seqTime.length));
			parMedian = (totalPar/(parTime.length));
			sortMedian = (totalSort/(sortTime.length));
			
			//information printing and storing
			System.out.println("\nn: "+ n +" antall kjerner: "+ p);
			output+=("\nn: "+ n +" antall kjerner: "+ p +"\n");
			for(int i = 0; i<seqTime.length; i++){
				String seqRes = String.format("%2.2f", (float)seqTime[i]);
				String parRes = String.format("%2.2f", (float)parTime[i]);
				String sortRes = String.format("%2.2f", (float)sortTime[i]);
				System.out.println("\n" +(i+1) +". Sekvensiell tid: "+ seqRes +"ms. Arrays.sort: "+ sortRes +"ms. Parallell tid: "+ parRes);
				output+=("\n" +(i+1) +". Sekvensiell tid: "+ seqRes +"ms. Arrays.sort: "+ sortRes +"ms. Parallell tid: "+ parRes + "\n");
			}
			
			String seqResMed = String.format("%2.2f", (float)seqMedian);
			String parResMed = String.format("%2.2f", (float)parMedian);
			String sortResMed = String.format("%2.2f", (float)sortMedian);
			System.out.println("\nGjennomsnitt. Sekvensiell: "+ seqResMed +"ms. Arrays.sort: "+ sortResMed +"ms. Parallell: "+ parResMed);
			output+=("\nGjennomsnitt. Sekvensiell: "+ seqResMed +"ms. Arrays.sort: "+ sortResMed +"ms. Parallell: "+ parResMed +"\n");
			
			speedup=(seqMedian/parMedian);
			String speedupRes = String.format("%2.2f", (float)speedup);
			
			System.out.println("Speedup: "+ speedupRes);
			output+=("Speedup: "+ speedupRes);
			
			saveOutput(n, p, output);
		}
		
	}
	
	/**
	* Runs the built in Arrays.sort() function
	* so it can time it for reference
	* @param	arr		the array to be sorted.
	* @return			the time it tok in milliseconds
	*/
	static double useSort(int [] arr){
		long start = System.nanoTime();
		Arrays.sort(arr);
		long end = System.nanoTime();
		
		return (end-start)/1000000.0;
		
	}
	
	/**
	* creates a new int array with a given amount for
	* random numbers.
	* 
	* @param	n	The size of the array
	* @return		An array with random numbers
	*/
	static int [] makeArray(int n){
		Random rnd = new Random(97361);
		int[] A = new int[n];
		for(int i = 0; i<n; i++){
			A[i]=rnd.nextInt(n);
		}
		return A;
	}
	
	/**
	* Saves all the information to a .txt file. uses array size
	* and amount of processors for the file name.
	*
	* @param 	n		the array size.
	* @param 	pros	the computers/given processors.
	* @param 	out		the string to be written to a file. 
	*/
	static void saveOutput(int n, int pros, String out){
		String newFile = ("Sorting n; "+ n +" on "+ pros +" threads.txt");
		
		try{
			File outFile = new File(newFile);
			
			outFile.delete();
			boolean bool = outFile.createNewFile();
			
			FileWriter writeTo = new FileWriter(outFile);
			
			writeTo.write(out);
			writeTo.close();
			
			System.out.println("Saved as: "+ newFile);
		}catch (IOException e){
			System.out.println("Couldn't open/create file");
		}
	}
}

/**
* The class containing the the sorting function.
*/
class Program{
	int n, antPros;
	int []a; 
	int []b;
	
	CyclicBarrier waitPar;
	CyclicBarrier finishPar;
	
	Program(int k, int A[]){
		n = k;
		a = A;
		b = A.clone();
		Arrays.sort(b);
	}
	
	Program(int k, int A[], int p){
		n = k;
		a = A;
		b = A.clone();
		Arrays.sort(b);
		antPros = p;
	}
	
	void testArrays(int [] seqArr){
		int j = b.length-1;
		//System.out.println("");
		for(int i = 0; i<50; i++){
			if(seqArr[i] != b[j-i]){
				System.out.println("A2: " + seqArr[i] +" and sorted: "+ b[j-i]);
			}
		}

	}
	
	/**
	* Runs the sorting function isort50 sequentially while timing it.
	* 
	* @return	Time it took
	*/
	double utforSeq(){
		long startTime = System.nanoTime();
		
		isort50(a, 0, 49, true);
		isort50(a, 50, n-1, false);
		
		long endTime = System.nanoTime();
		
		/*
		System.out.println("Seq: ");
		for(int i = 0; i<50; i++){
			System.out.print(a[i] +" ");
			if(i==50){
				System.out.println("\nHit 50\n");
			}
		}
		*/
		
		testArrays(a);
		
		double send = (endTime-startTime)/1000000.0;
		return send;
	}
	
	/**
	* Creates Threads to sort in parallel.
	* It uses two cyclic barriers to do so, one to know that all
	* are waiting to merge and one to tell that it is finished.
	*
	* @return	The time it took
	*/
	double utforPar(){
		
		long startTime = System.nanoTime();
		
		waitPar = new CyclicBarrier(antPros+1);
		finishPar = new CyclicBarrier(antPros+1);
		
		for(int idx = 0; idx < antPros; idx++){
			new Thread(new parSorter(idx, a)).start();
		}
		
		try{
			waitPar.await();
			finishPar.await();
		}catch(Exception e){
			return 0;
		}
		
		
		long endTime = System.nanoTime();
		
		testArrays(a);
		
		/*
		System.out.println("\nPar:\n");
		for(int i = 0; i<50; i++){
			System.out.print(a[i] +" ");
			
			if(i==50){
				System.out.println("\nHit 50\n");
			}
		}
		*/
		
		double send = (endTime-startTime)/1000000.0;
		return send;
	}
	/**
	* A modified version of insertion sort
	* it sorts with highest at the first slot. 
	* It can sort in two ways, one sorts all from h to v
	* or sorts all from h to v into the 50 slots before h.
	* 
	* @param	a	the array to sort.
	* @param	h	the beginning.
	* @param	v	the end.
	* @param	firstTime	Indicator for which part to run.
	*
	*/
	void isort50(int [] a, int h, int v, boolean firstTime){
		int i, t;
		
		if(firstTime){
			
			for(int k = h; k <= v; k++){
				t = a[k];
				i = k;
				
				while(i>h && t >= a[i-1]){
					a[i]=a[i-1];
					i--;
				}
				a[i]=t;
			}
		}else{
			
			int start = h-1;
			int stop = h-50;
			
			for(int k = h; k <= v; k++){
				if(a[k] > a[start]){
					i = start;
					t = a[k];
					a[k] = a[start];
					while(i > stop && t >= a[i-1]){
						a[i]=a[i-1];
						i--;
					}
					a[i]=t;
				}
			}
		}
	}
	
	/**
	* puts together the "arrays" sorted to put the largest in space 0-49.
	* Done by going through the intervals that where sorted and testing them like "normal"
	* with 49-0.
	* @param	a	the array to sort.
	*/
	void assemble50(int [] a){
		int t, k;
		int inter = n/antPros; //the parallel interval
		
		for(int i = inter; (i+49) < n; i+=inter){
			for(int j = i; j <=(i+49); j++){
				if(a[j]>=a[49]){
					k = 49;
					t = a[j];
					a[j]=a[49];
					
					while(k > 0 && t >=a[k-1]){
						a[k] = a[k-1];
						k--;
					}
					a[k] = t;
				}else{
					break;
				}
			}
		}
		
	}
	
	/**
	* Starts the function isort50 multiple times in relation to
	* array length and amount of processors.
	*/
	class parSorter implements Runnable {
		int id, inter;
		int [] a;
		
		parSorter(int i, int[] A){
			id = i;
			a = A;
			inter = (n/antPros);
		}
		
		public void run(){
			
			int start = inter*id;
			int end = inter*(id+1)-1;
			//runs the
			
			
			if(id == antPros-1){
				isort50(a, start, start+49, true);
				isort50(a, start+50, n-1, false);
			}else{
				isort50(a, start, start+49, true);
				isort50(a, start+50, end, false);
			}
			try{
				waitPar.await(); //Waits for all possesses to be done with the complete array 
				
				//Makes sure the merge only runs once
				if(id == antPros-1){
					assemble50(a);
				}
				
				finishPar.await(); //waits for assemble to be done
			}catch(Exception e){
				return;
			}
		}
	}
}
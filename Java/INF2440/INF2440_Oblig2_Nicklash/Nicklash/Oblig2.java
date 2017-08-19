import java.util.*;
import java.util.concurrent.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

///--------------------------------------------------------
//
//     Fil: EratosthenesSil.java
//     implements bit-array (Boolean) for prime numbers
//     written by:  Arne Maus , Univ of Oslo, 2013, 2015
//
//--------------------------------------------------------

class Oblig2{
	public static void main(String [] args){
		int n = Integer.parseInt(args[0]);
		int k = Runtime.getRuntime().availableProcessors();
		if(args.length >= 2){
			k = Integer.parseInt(args[1]);
		}
		
		System.out.println("max primtall m: "+ n);
		
		ErathostenesSil seq = new ErathostenesSil(n);
		double seqPrimeTime = seq.getPrimeTime();
		double seqFactorTime = seq.getFactorTime();
		
		ErathostenesSil par = new ErathostenesSil(n, k);
		double parPrimeTime = par.getPrimeTime();
		double parFactorTime = par.getFactorTime();
		
		System.out.println("Speedup prime: "+ (seqPrimeTime/parPrimeTime));
		System.out.println("Speedup factor: "+ seqFactorTime/parFactorTime);
	}
}

/**
* Implements the bitArray of length 'bitLen' [0..bitLen ]
*   1 - true (is prime number)
*   0 - false
*  can be used up to 2 G Bits (integer range)
*/
class ErathostenesSil
{
	byte [] bitArr;
    long m;
	int bitLen, cores;
	int  maxPrime;
	final  int [] bitMask = {1,2,4,8,16,32,64,128};
	final  int [] bitMask2 = {255-1,255-2,255-4,255-8,255-16,255-32,255-64,255-128};
	CyclicBarrier barrier;
	ArrayList<Long> allFactors;
	double primeTime, factorTime;
	int numPrimes;

	/**
	*	Starts a sequential version of ErathostenesSil
	*
	*	@param	max		largest possible prime
	*/
	 ErathostenesSil (int max) {
	    
	    bitLen = max;
	    m = (long) max * (long) max;
        maxPrime = maxPrime;
		bitArr = new byte [(bitLen/16)+1];
		
		long start = System.nanoTime();
		setAllPrime();
		generatePrimesByEratosthenes(bitLen);
		long end = System.nanoTime();
		
		numPrimes = numberOfPrimesLess(max);
		primeTime = (end - start)/1000000.0;
		System.out.println("\n----------Sequential----------");
		System.out.println("Generated all primes <= "+ max +" on "+ primeTime +"ms");
		System.out.println("That is "+ (end - start)/numPrimes +"ns per prime");
		System.out.println("there are "+ numPrimes +" prime numbers <"+ max +" which is "+ (((float) numPrimes)*100)/((float) bitLen) +"%");
		System.out.println("");

		
		ArrayList <Long> factors;
		start = System.nanoTime();
		
		for(long i = (m-1); i>=(m-100); i--){
			
		    factors = factorize(i);
		    
			System.out.print("The prime factors of "+ i +" are ");
			for(long f : factors){
				System.out.print(" "+ f);
			}
				
			System.out.println("");
		    
		}
		end = System.nanoTime();
		
		System.out.println("");
		factorTime = (end-start)/1000000.0;
		System.out.println("it took "+ factorTime +"ms to factorize and print 100 numbers");
		System.out.println("");
		//printAllPrimes();
		
	} // end konstruktor PrimeArray
	  
	/**
	*	Sets and start a parallel version of ErathostenesSil
	*	
	*	@param	max		largest possible prime number
	*	@param	k		the amount of threads to use
	*/
	  ErathostenesSil (int max, int k) {
		
	    bitLen = max;
	    m = (long) max * (long) max;
		this.cores = k;
        maxPrime = maxPrime;
		bitArr = new byte [(bitLen/16)+1];
		allFactors = new ArrayList<Long>();
		
		long startTime, endTime;
		long totFactorTime = 0;
		
		
		int stop = (int) Math.sqrt(bitLen);
		int start = 0;
		int intervall = (max - stop)/cores;
		
		startTime = System.nanoTime();
		setAllPrime();
		generatePrimesByEratosthenes(stop);
		
		start = stop;
		stop = start + intervall;
		barrier = new CyclicBarrier(cores+1);
		
		for(int i = 0; i<cores; i++){
			if(i == cores-1){
				new Thread(new parWorker(i, start, max, max)).start();
			}else{
				new Thread(new parWorker(i, start, stop, max)).start();
			}
			start = stop;
			stop = start + intervall;
		}
		
		try{
			barrier.await();
		}catch(Exception e){
			return;
		}
		
		endTime = System.nanoTime();
		primeTime = (endTime-startTime)/1000000.0;
		numPrimes = numberOfPrimesLess(max);
		
		System.out.println("\n----------Parallel----------");
		System.out.println("Generated all primes <= "+ max +" on "+ primeTime +"ms");
		System.out.println("That is "+ (endTime - startTime)/numPrimes +"ns per prime");
		System.out.println("there are "+ numPrimes +" prime numbers <"+ max +" which is "+ (((float) numPrimes)*100)/((float) bitLen) +"%");
		System.out.println("");
		
		barrier = new CyclicBarrier(cores+1);
		int sqrtM;
		long allF;
		startTime = System.nanoTime();
		
		for(long i = m-1; i>=(m-100); i--){
		
			sqrtM = (int) Math.sqrt(i);
			intervall = sqrtM/cores;
			allF=i; //used to find rest prime over the square root of i
			start = 2;
			stop = intervall;
			
			
			for(int j = 0; j<cores; j++){
				if(j == cores-1){
					new Thread(new parFactor(j, start, sqrtM+1, i)).start();
					
				}else{
					new Thread(new parFactor(j, start, stop, i)).start();
					
				}
				start = stop-1;
				stop += intervall;
			}
			try{
				barrier.await();
			}catch(Exception e){
				return;
			}
			
			
			System.out.print("The factors of "+ i + " are: ");
			for(long l : allFactors){
				allF=allF/l;
				System.out.print(l + " ");
					
			}
			
			if((allF)!=0 && (allF!=1)){
				System.out.print((allF));
			}
			System.out.println("");
			
			allFactors = new ArrayList<Long>();
		}
		
		endTime = System.nanoTime();
		factorTime = ((endTime - startTime)/1000000.0);
		
		System.out.println("");
		System.out.println("it took "+ factorTime +"ms to factorize and print 100 numbers");
		System.out.println("");
		
		
		//printAllPrimes();
      } // end konstruktor PrimeArray
	
	/**
	*	Gets the time it took to generate all the prime numbers
	*
	*	@return		the time it took to generate prime numbers
	*/
	public double getPrimeTime(){
		return primeTime;
	}
	
	/**
	*	Get the time it took to factorize 100 numbers
	*
	*	@return		the time it took to factorize
	*/
	public double getFactorTime(){
		return factorTime;
	}
	
	/**
	*	Sets all odd numbers as prime numbers so the non primes 
	*	can be crossed out later.
	*
	*/
	void setAllPrime() {

		for (int i = 0; i < bitArr.length; i++) {
		   bitArr[i] = (byte)255;
	    }
	}

	/**
	*	sets the given number as not prime
	*
	*	@param	i	the number to be set
	*/
      void setNotPrime(int i)
      { bitArr[i/16] &= bitMask2[(i%16)>>1]; }

	
	/**
	*	Tells if a number is prime or not. does it by first checking if it is
	*	the number 2 and then chacks if the number is an even number.
	*	If nether the returns the value of the index in the array.
	*
	*	@param	i	the index to check for prime status
	*	@return		the prime status of the number
	*/
      boolean isPrime (int i) {
		  //System.out.println("isPrime("+ i +")");
		  if (i == 2 ) return true;
		  if ((i&1) == 0) return false;
		  else  return (bitArr[i>>4] & bitMask[(i&15)>>1]) != 0;
	  }

	/**
	*	Factorizes a number by testing it to all primes under its square root
	*	
	*	@param	num		the number to factorize
	*	@return			an arraylist of the prime factors
	*/
	
	  ArrayList<Long> factorize (long num) {
		  ArrayList <Long> fakt = new ArrayList <Long>();
		  int pCand = 2;
		  int maks = (int) Math.sqrt(num*1.0) + 1;
		  
		  while (num > 1 & pCand < maks) {

			  while ( num % pCand == 0){
				
			     fakt.add((long) pCand);
			     num /= pCand;
			  }
			  pCand = nextPrime(pCand);
			 // maks = (int) Math.sqrt(num*1.0) +1;
		  }
		if (pCand>=maks) fakt.add(num);
		  
        return fakt;
	  } // end factorize

	/**
	*	A modified version of the factorize method to
	*	work between two points. 
	*
	*	@param	num		the number to factorize
	*	@param	begin	the first possible prime number
	*	@param	end		the last possible prime number
	*	@param	id		the id if the thread
	*	@return			an arraylist with its factors
	*/
	  ArrayList<Long> parFactorizer(long num, int begin, int end, int id){
		  ArrayList<Long> fakt = new ArrayList<Long>();
		  int pCand = 2;
		  int maxEnd = (int) Math.sqrt(num*1.0)+1;
		  if(begin != 2){
			  pCand = begin;
			  if(!isPrime(pCand)){
				  pCand=nextPrime(pCand);
			  }
		  }
		  
		  while(pCand > 1 && pCand < end){
			while(num%pCand == 0){
			  fakt.add((long) pCand);
			  
			  num /= pCand;
			}
		  pCand = nextPrime(pCand);
		  }
		  /*
		  if(id==0){
			  if(num>=maxEnd){
				  //System.out.println("2. Thread: "+ id +" added "+ num);
				  fakt.add(num);
			  }
		  }
		  */
		  return fakt;
	  }
		
	/**
	*	Gets the prime number after the given number.
	*
	*	@return		the next prime number
	*/
      int nextPrime(int i) {
	  // returns next prime number after number 'i'

	     int k ;
	     if ((i&1)==0) k =i+1; // if i is even, start at i+1
	     else k = i+2;       // next possible prime
 	     while (!isPrime(k)) k+=2;
	     return k;

	 } // end nextPrime

	/**
	*	gets the last possible prime number in the byte array 
	*	by starting at bitLen and working backwards
	*
	*	return		the last prime number
	*/
	 int lastPrime() {
		 int j = ((bitLen>>1)<<1) -1;
		 while (! isPrime(j) ) j-=2;
		 return j;
	 }// end lastPrime

	 long largestLongFactorizedSafe () {
		 long l;
		  int i,j = ((bitLen>>1)<<1) -1;
		  while (! isPrime(j) ) j-=2;
		  i = j-2;
		  while (! isPrime(i) ) i-=2;
		  return (long)i*(long)j;
	  }// end largestLongFactorizedSafe

	/**
	*	Prints all the prime numbers found under the number given by user
	*/
	void printAllPrimes(){
		 for ( int i = 2; i <= bitLen; i++)
		  if (isPrime(i)) System.out.println(" "+i);
	 } // end printAllPrimes

	 /**
	*	Counts all the prime number under a given number
	*	
	*	@param	n	the number the prime should be less then.
	*/
	 int numberOfPrimesLess(int n){
		 int num = 2;  // we know 2 and 3 are primes
		 int p;

		 for (p=nextPrime(3) ; p < n;p = nextPrime(p) ){
			 num++;
		 }
		 return num;
	 } // end numberOfPrimesLess


	/**
	*	Generates all the primes under a given number using an old mathematical algorithm
	*	created by Eratosthenes between 275-194 BC.
	*
	*	@param		the last possible prime;
	*/
    void generatePrimesByEratosthenes(int stop) {
		int m = 3, m2=6,mm =9;     // next prime
	    setNotPrime(1);      // 1 is not a prime
		
	    while ( mm < stop) {
	 		m2 = m+m;
	 		for ( int k = mm; k <stop; k +=m2){
	 			setNotPrime(k);
	 		}
	 		m = nextPrime(m);
	 		mm= m*m;
	 	}
	} // end generatePrimesByEratosthenes
	
	/**
	*	A modified version Erathostenes sieve 
	*	to find the primes between two points.
	*
	*	@param	start	the first number to testing
	*	@param	stop	the last number to test
	*	@param	maxEnd	the largest possible prime to find.
	*/
	void parSieve(int start, int stop, int maxEnd){
		if((start & 1) == 0){
			start+=1;
		}
		
		int prime, tmp, sqrtPrime;
		prime = nextPrime(2);
		
		while(prime <= maxEnd){
			sqrtPrime = (prime * prime);
			if(sqrtPrime > stop){
				break;
			}else if(!(sqrtPrime >= start && sqrtPrime < stop)){
				tmp = start/prime;
				if(tmp*prime >= start){
					sqrtPrime = tmp*prime;
				}else{
					sqrtPrime = (tmp*prime)+prime;
				}
			}
			
			if((sqrtPrime & 1) == 0){
				sqrtPrime+=prime;
			}
			
			for(int i = sqrtPrime; i < stop; i += (prime*2)){
				setNotPrime(i);
			}
			prime = nextPrime(prime);
		}
	}
	
	/**
	*	Starts parFactorizer's with necessary parameters 
	*/
	class parFactor implements Runnable{
		int id, start, stop;
		long number;
		ArrayList<Long> myArrayList;
		
		parFactor(int id, int start, int stop, long numb){
			this.id = id;
			this.start = start;
			this.stop = stop;
			this.number = numb;
			myArrayList = new ArrayList<>();
		}
		
		public ArrayList<Long> getArrayList(){
			return myArrayList;
		}
		
		public void run(){
			myArrayList = parFactorizer(number, start, stop, id);
			allFactors.addAll(myArrayList);
			try{
				barrier.await();
			}catch(Exception e){
				return;
			}
		}
	}
	
	/**
	*	Starts parSieve's with necessary parameters
	*
	*/
	class parWorker implements Runnable{
		
		int id, start, stop, maxPos;
		parWorker(int id, int start, int stop, int max){
			this.id = id;
			this.start = start;
			this.stop = stop;
			maxPos = max;
			
		}
		
		public void run(){
			parSieve(start, stop, maxPos);
			
			try{
				barrier.await();
			}catch(Exception e){
				return;
			}
		}
	}
} // end class ErathostenesSil


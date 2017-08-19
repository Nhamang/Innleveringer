import java.util.Random;
import java.util.concurrent.CyclicBarrier;

class Oblig4 {
	public static void main(String [] args) {
		(new radixSort(args[0])).utfor();
	}
}

class radixSort {
	int [] a, b;
	int n, k;

	radixSort(String size) {
		n = Integer.parseInt(size);
		k = Runtime.getRuntime().availableProcessors();
		a = new int[n];
		java.util.Random r = new java.util.Random(123);
		for(int i = 0; i < a.length; i++) a[i] = r.nextInt(a.length);
	}

	public void utfor() {
		SekvensiellRadix seq;
		ParallellRadix par;
		double seqTime, parTime;

		seqTime = System.nanoTime();
		(new SekvensiellRadix()).radix2(a);
		seqTime = System.nanoTime()-seqTime;
		seqTime = seqTime/1000000.0;
		par = new ParallellRadix(n, k);
		parTime = System.nanoTime();
		par.utforPar();
		parTime = System.nanoTime() - parTime;
		parTime = parTime/1000000.0;
		
		double SpeedUp = seqTime/parTime;
		System.out.println("seq: "+ seqTime +" parTime: "+ parTime +" speedup: "+ SpeedUp);

	}
}

class ParallellRadix {
	CyclicBarrier barrierWait, barrierFinish;
	int [] maxArray, a, b;
	int [][] globalCount;
	int cores;

	ParallellRadix(int n, int k) {
		barrierFinish = new CyclicBarrier(k+1);
		barrierWait = new CyclicBarrier(k);
		a = new int[n];
		b = new int[n];
		maxArray = new int[k];
		globalCount = new int[k][];
		cores = k;
		java.util.Random r = new java.util.Random(123);
		for(int i = 0; i < a.length; i++) a[i] = r.nextInt(a.length);

	}

	public void utforPar() {
		for(int i = 0; i < cores; i++) {
			(new Thread(new Threads(i))).start();
		}
		try{
			barrierFinish.await();
		} catch(Exception e){}

		for (int i = 1;i<a.length;i++)
		   	if (a[i-1] > a[i] ){ System.out.println("Error: a["+(i-1)+"] = "+a[i-1]+ "> a["+i+"] = "+a[i]);}

	}

	class Threads implements Runnable {
		int id;
		int [] localCount;
		int venstre, hoyre;
		int bit1, bit2, maks;

		Threads(int id) {
			this.id = id;
		}

		private void finnLokalMaks() {
			int tall = (a.length/cores);
			venstre = tall*id;
			maks = a[venstre];
			hoyre = venstre+tall;
			if(id == cores-1) hoyre = a.length;

			for(int i = venstre+1; i < hoyre; i++) {
				if(a[i] > maks) maks = a[i];
			}
			maxArray[id] = maks;
		}

		private void parallellRadix() {
			int numBit = 2;
			while(maks >= (1L<<numBit)) numBit++;

			bit1 = numBit/2;
			bit2 = numBit-bit1;

			parallellRadixSort(a, b, bit1, 0);

			try {
				barrierWait.await();
			} catch (Exception e) {return;}

			parallellRadixSort(b, a, bit2, bit1);
		}

		private void parallellRadixSort(int [] a, int [] b, int maskLen, int shift) {
			int acumVal = 0, t, n = a.length;
			int mask = (1<<maskLen)-1;
			localCount = new int[mask+1];

			// B)
			for(int i = venstre; i < hoyre; i++) {
				localCount[(a[i] >> shift) & mask]++;
			}
			globalCount[id] = localCount;

			try {
				barrierWait.await();
			} catch(Exception e) {return;}

			localCount = new int[mask+1];

			// C)
			for(int i = 0; i < localCount.length; i++) {
				for(int j = 0; j < id; j++) {
					acumVal += globalCount[j][i];
				}

				localCount[i] = acumVal;

				for(int j = id; j < cores; j++) {
					acumVal += globalCount[j][i];
				}
			}

			// D)
			for(int i = venstre; i < hoyre; i++) {
				b[localCount[(a[i]>>shift) & mask]++] = a[i];
			}
		}

		private void mergeMax() {
			for(int i = 0; i < maxArray.length; i++) {
				if(maxArray[i] > maks) {
					maks = maxArray[i];
				}
			}
		}

		public void run() {
			finnLokalMaks();
			try {
				barrierWait.await();
			} catch(Exception e) {return;}

			mergeMax();

			parallellRadix();

			try {
				barrierFinish.await();
			} catch(Exception e) { return;}

		}
	}
}

class SekvensiellRadix {
	static void radix2(int [] a) {
      // 2 digit radixSort: a[]
		int max = a[0], numBit = 2, n =a.length;
		// a) finn max verdi i a[]
		for (int i = 1 ; i < n ; i++)
           if (a[i] > max) max = a[i];

		while (max >= (1L<<numBit) )numBit++; // antall siffer i max

      // bestem antall bit i siffer1 og siffer2
        int bit1 = numBit/2,
            bit2 = numBit-bit1;
		int[] b = new int [n];
		radixSort( a,b, bit1, 0);    // første siffer fra a[] til b[]
		radixSort( b,a, bit2, bit1);// andre siffer, tilbake fra b[] til a[]
	} // end


	/** Sort a[] on one digit ; number of bits = maskLen, shiftet up ‘shift’ bits */
	static void radixSort ( int [] a, int [] b, int maskLen, int shift){
		int acumVal = 0, j, n = a.length;
		int mask = (1<<maskLen) -1;
		int [] count = new int [mask+1];

		// b) count=the frequency of each radix value in a
		for (int i = 0; i < n; i++) {
			count[(a[i]>> shift) & mask]++;
		}

		// c) Add up in 'count' - accumulated values
		for (int i = 0; i <= mask; i++) {
			j = count[i];
			count[i] = acumVal;
            acumVal += j;
		}
		// d) move numbers in sorted order a to b
		for (int i = 0; i < n; i++) {
			b[count[(a[i]>>shift) & mask]++] = a[i];
		}
	}// end radixSort
}
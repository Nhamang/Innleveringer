import java.util.*;


class Oblig3 {
	
	int n;
	int [] x;
	int [] y;
	int maxlvl;
	NPunkter np;
	IntList points;
	IntList allPoints;
	
	int MAX_X, MAX_Y;
	
	Oblig3(int n){
		this.n = n;
		x = new int [n];
		y = new int [n];
		np = new NPunkter(n);
		np.fyllArrayer(x, y);
		MAX_X = MAX_Y = np.maxXY;
		points = new IntList(n);
		//TegnUt t = new TegnUt(this, points);
	}
	
	Oblig3(int n, double k){
		this.n = n;
		x = new int [n];
		y = new int [n];
		np = new NPunkter(n);
		np.fyllArrayer(x, y);
		double tmpMax = Math.log(k)/Math.log(2);
		maxlvl = (int) tmpMax;
		MAX_X = MAX_Y = np.maxXY;
		allPoints = new IntList(n);
		//TegnUt t = new TegnUt(this, points);
	}
	
	public static void main(String [] args){
		int k = Runtime.getRuntime().availableProcessors();
		for(int i = 100; i <= 10000000; i*=10){
			Oblig3 o = new Oblig3(i);
			
			System.out.println("\n n: " + i +" cores: "+ k);
			
			double seqTime = o.utforSeq();
			System.out.println("Sekvensiell tid: "+ seqTime);
			
			o = new Oblig3(i, k);
			double parTime = o.utforPar();
		
			System.out.println("Parallel tid: "+ parTime);
			System.out.println("Speedup: "+ (seqTime/parTime));
		}
		
	}
	
	/**
	*	kaller på de riktige metodene for en sekvensiell kjoring
	*
	*	@return		Tiden det tok aa kjore
	*/
	public double utforSeq(){
		
		long start = System.nanoTime();
		seqMethod();
		long end = System.nanoTime();
		System.out.println("\nSek");
		
		/*
		for(int i = 0; i < points.size(); i++){
			System.out.print(points.get(i) +" ");
		}
		*/
		
		return (double) (end-start)/1000000.0;
	}

	//if(d == 0 && i != p1 && i != p2 && inside(p1, p2, i))
	//		legg til neq men ikke test som p3
	//if(d < 0)
	//if(d<tmpd)
		
	/**
	*	Finner max og min x. Bruker det for å finne storste negative og storste positive
	*	sender det til rukursiv metode
	*	
	*/
	private void seqMethod() {
		
		int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE;
		int maxIdx = 0, minIdx = 0;
		for(int i = 0; i < n; i++){
			
			if(x[i] > maxX){
				maxX = x[i];
				maxIdx = i;
			}
			if(x[i] < minX){
				minX = x[i];
				minIdx = i;
			}
		}
		
		
		//ArrayList<Integer> right = new ArrayList<Integer>();
		//ArrayList<Integer> left = new ArrayList<Integer>();
		IntList right = new IntList(n);
		IntList left = new IntList(n);
		
		int a, b, c;
		double d, tmpd = 1.0, tmpd2 = -1.0;
		int p3 = 0, p4 = 0;
		
		a = y[maxIdx] - y[minIdx];
		b = x[minIdx] - x[maxIdx];
		c = (y[minIdx] * x[maxIdx]) - (y[maxIdx] * x[minIdx]);
		
		for(int i = 0; i < n; i++){
			if(i != maxIdx && i != minIdx){
				d = (double) (a*x[i]) + (b*y[i]) + c;
				//System.out.println("i: "+ i +" d:"+ d);
				if(d <= 0){
					right.add(i);
					if(d <= tmpd){
						p3 = i;
						tmpd = d;
					}
				}
				if(d >= 0){
					left.add(i);
					if(d >= tmpd2){
						p4 = i;
						tmpd2 = d;
					}
				}
			}
		}
		
		
		
		points.add(maxIdx);
		seqRec(maxIdx, minIdx, p3, right);
		points.add(minIdx);
		seqRec(minIdx, maxIdx, p4, left);
		
		
	}
	
	/**
	*	Finner storst negativ punkt fra p1 til p3 og fra p3 til p2.
	*	sender denne infoen videre i to nye versjoner av seg selv
	*
	*	@param	p1	max fra forrige runde
	*	@param	p2	min fra forrige runde
	*	@param	p3	storst negativ fra forrige runde
	*	@param	am	liste med alle negative denne skal igjennom
	*/
	public void seqRec(int p1, int p2, int p3, IntList am){
		
		if(am.isEmpty() || p3 == -1){
			return;
		}
		
		int a1, b1, c1, a2, b2, c2, newP3 = -1, newP4 = -1;
		a1 = y[p1] - y[p3];
		b1 = x[p3] - x[p1];
		c1 = (y[p3] * x[p1]) -(y[p1] * x[p3]);
		
		//ArrayList<Integer> left = new ArrayList<Integer>();
		//ArrayList<Integer> right = new ArrayList<Integer>();
		IntList left = new IntList(n);
		IntList right = new IntList(n);
		
		a2 = y[p3] - y[p2];
		b2 = x[p2] - x[p3];
		c2 = (y[p2] * x[p3]) - (y[p3] * x[p2]);
		
		int d1 = 0;
		int d2 = 0; 
		int tmpD1 = 1;
		int tmpD2 = 1;
		
		
		for(int j = 0; j < am.size(); j++){
			int i = am.get(j);
			//am.remove(0);
			
			d1 = (a1*x[i]) + (b1*y[i]) + c1;
			d2 = (a2*x[i]) + (b2*y[i]) + c2;
			
			if(i == p1 || i == p3){
				continue;
			}
			if(d1 <= 0){
				if(d1 == 0 && !inside(p1, p3, i)){
					continue;
				}
				if(d1 < tmpD1){
					tmpD1 = d1;
					newP3 = i;
				}
				left.add(i);
			}
			
			if(i == p3 || i == p2){
				continue;
			}
			if(d2 <= 0){
				
				if(d2 == 0 && !inside(p3, p2, i)){
					continue;
				}
				if(d2 < tmpD2){
					tmpD2 = d2;
					newP4 = i;
				}
				right.add(i);
			}
		}

		seqRec(p1, p3, newP3, left);
		if(p3 != -1){
			points.add(p3);
		}
		seqRec(p3, p2, newP4, right);
		
		
	}
	
	/**************************************/
	//			end seq
	/**************************************/
		
	/**
	*	Tester om et punkt er innenfor x og y kordinatene for to andre punkter
	*
	*	@param	p1	et av punktene som et punkt maa vaere imellom
	*	@param	p2	et av punktene som et punkt maa vaere imellom
	*	@param	i	punktet vi skal teste
	*/
	public boolean inside(int p1, int p2, int i){
		int maxX, minX, minY, maxY;
		if(x[p1] >= x[p2]){
			maxX = x[p1];
			minX = x[p2];
		}else{
			maxX = x[p2];
			minX = x[p1];
		}
		
		if(y[p1] >= y[p2]){
			maxY = y[p1];
			minY = y[p2];
		}else{
			maxY = y[p2];
			minY = y[p1];
		}
		
		if((x[i] <= maxX && x[i] >= minX) && (y[i] <= maxY && y[i] >= minY)){
			return true;
		}
		
		return false;
	}
	
	/**************************************/
	//			Start par
	/**************************************/
	
	/**
	*	Starter de riktige metodene som trengs for en parallel kjoring
	*
	*	@return		Tiden det tok
	*/
	public double utforPar(){
		long start = System.nanoTime();
		
		parMethod();
		
		long end = System.nanoTime();
		
		/*
		System.out.println("\nPar:");
		for(int i = 0; i < allPoints.size(); i++){
			System.out.print(allPoints.get(i) +" ");
		}
		*/
		
		return (double) (end-start) / 1000000.0;
	}

	
	/**
	*	Finner min og max x, bruker det punktet for aa finne storste negativ og positive
	*	punkt. sender det videre til en rekursiv metode
	*
	*/
	public void parMethod(){
		int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE;
		int maxIdx = 0, minIdx = 0;
		for(int i = 0; i < n; i++){
			
			if(x[i] > maxX){
				maxX = x[i];
				maxIdx = i;
			}
			if(x[i] < minX){
				minX = x[i];
				minIdx = i;
			}
		}
		
		/*
		ArrayList<Integer> right = new ArrayList<Integer>();
		ArrayList<Integer> left = new ArrayList<Integer>();
		*/
		
		IntList right = new IntList(n);
		IntList left = new IntList(n);
		
		int a, b, c;
		double d, tmpd = 1.0, tmpd2 = -1.0;
		int p3 = 0, p4 = 0;
		
		a = y[maxIdx] - y[minIdx];
		b = x[minIdx] - x[maxIdx];
		c = (y[minIdx] * x[maxIdx]) - (y[maxIdx] * x[minIdx]);
		
		for(int i = 0; i < n; i++){
			if(i != maxIdx && i != minIdx){
				d = (double) (a*x[i]) + (b*y[i]) + c;

				if(d <= 0){
					right.add(i);
					if(d <= tmpd){
						p3 = i;
						tmpd = d;
					}
				}
				if(d >= 0){
					left.add(i);
					if(d >= tmpd2){
						p4 = i;
						tmpd2 = d;
					}
				}
			}
		}
		
		IntList myLeftSet = new IntList(n);
		IntList myRightSet = new IntList(n);
		//kall på rekursiv
		allPoints.add(maxIdx);
		Thread t1 = new Thread(new parWorker(maxIdx, minIdx, p3, right, 0, myLeftSet));
		
		try{
			t1.start();
			t1.join();	 
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		for(int i = 0; i< myLeftSet.size(); i++){
				allPoints.add(myLeftSet.get(i));
		}
		allPoints.add(minIdx);
		
		t1 = new Thread(new parWorker(minIdx, maxIdx, p4, left, 0, myRightSet));
		
		try{
			t1.start();
			t1.join();	 
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		for(int i = 0; i< myRightSet.size(); i++){
			allPoints.add(myRightSet.get(i));
		}
	}
	
	/**
	*	Bruker to punkter for å finne storste negativ punkt fra en liste
	*	og sender det videre, enten til to nye parRec eller til to parSeqRec
	*
	*	@param	p1		max x fra forrige runde
	*	@param	p2		min x fra forrige runde
	*	@param	p3		Storste negativ fra forrige runde
	*	@param	am		listen med alle negativ fra forrige runde
	*	@param	lvl		denne gjennomgangen sitt nivå
	*/
	public void parRec(int p1, int p2, int p3, IntList am, int lvl, IntList mySet){
		if(am.isEmpty() || p3 == -1){
			return;
		}
		
		int a1, b1, c1, a2, b2, c2, newP3 = -1, newP4 = -1;
		a1 = y[p1] - y[p3];
		b1 = x[p3] - x[p1];
		c1 = (y[p3] * x[p1]) -(y[p1] * x[p3]);
		
		//ArrayList<Integer> left = new ArrayList<Integer>();
		//ArrayList<Integer> right = new ArrayList<Integer>();
		IntList left = new IntList(n);
		IntList right = new IntList(n);
		
		a2 = y[p3] - y[p2];
		b2 = x[p2] - x[p3];
		c2 = (y[p2] * x[p3]) - (y[p3] * x[p2]);
		
		int d1 = 0;
		int d2 = 0; 
		int tmpD1 = 1;
		int tmpD2 = 1;
		
		
		for(int j = 0; j < am.size(); j++){
			int i = am.get(j);
			
			
			d1 = (a1*x[i]) + (b1*y[i]) + c1;
			d2 = (a2*x[i]) + (b2*y[i]) + c2;
			
			if(i == p1 || i == p3){
				continue;
			}
			if(d1 <= 0){
				if(d1 == 0 && !inside(p1, p3, i)){
					continue;
				}
				if(d1 < tmpD1){
					tmpD1 = d1;
					newP3 = i;
				}
				left.add(i);
			}
			
			if(i == p3 || i == p2){
				continue;
			}
			if(d2 <= 0){
				if(d2 == 0 && !inside(p3, p2, i)){
					continue;
				}
				if(d2 < tmpD2){
					tmpD2 = d2;
					newP4 = i;
				}
				right.add(i);
			}
		}
		
		if(lvl < maxlvl){
			//kall på nye parRec
			lvl+=1;
			IntList myLeftSet = new IntList(n);
			IntList myRightSet = new IntList(n);
			Thread t1 = new Thread(new parWorker(p1, p2, newP3, left, lvl, myLeftSet));
			Thread t2 = new Thread(new parWorker(p3, p2, newP4, right, lvl, myRightSet));
			
			try{
				t1.start();
				t2.start();
				t1.join();
				for(int i = 0; i< myLeftSet.size(); i++){
					mySet.add(myLeftSet.get(i));
				}
				mySet.add(p3);
				t2.join();
				for(int i = 0; i< myRightSet.size(); i++){
					mySet.add(myRightSet.get(i));
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}else{
			//kall på sekvensiell
			IntList myLeftSet = new IntList(n);
			parSeqRec(p1, p3, newP3, left, myLeftSet);
			for(int i = 0; i< myLeftSet.size(); i++){
				mySet.add(myLeftSet.get(i));
			}
			mySet.add(p3);
			myLeftSet = new IntList(n);
			parSeqRec(p3, p2, newP4, right, mySet);
			for(int i = 0; i< myLeftSet.size(); i++){
				mySet.add(myLeftSet.get(i));
			}
		}
	}
	
	/**
	*	Bruker to punkter for å finne storste negativ punkt fra en liste
	*	og sender det videre, enten til to nye parRec eller til to parSeqRec
	*
	*	@param	p1		max x fra forrige runde
	*	@param	p2		min x fra forrige runde
	*	@param	p3		Storste negativ fra forrige runde
	*	@param	am		listen med alle negativ fra forrige runde
	*	@param	mySet	listen punktene skal legges i
	*/
	public void parSeqRec(int p1, int p2, int p3, IntList am, IntList mySet){
		if(am.isEmpty() || p3 == -1){
			return;
		}
		
		//System.out.println("parSeqRec");
		int a1, b1, c1, a2, b2, c2, newP3 = -1, newP4 = -1;
		a1 = y[p1] - y[p3];
		b1 = x[p3] - x[p1];
		c1 = (y[p3] * x[p1]) -(y[p1] * x[p3]);
		
		//ArrayList<Integer> left = new ArrayList<Integer>();
		//ArrayList<Integer> right = new ArrayList<Integer>();
		IntList right = new IntList(n);
		IntList left = new IntList(n);
		
		a2 = y[p3] - y[p2];
		b2 = x[p2] - x[p3];
		c2 = (y[p2] * x[p3]) - (y[p3] * x[p2]);
		
		int d1 = 0;
		int d2 = 0; 
		int tmpD1 = 1;
		int tmpD2 = 1;
		
		
		for(int j = 0; j < am.size(); j++){
			int i = am.get(j);
			
			d1 = (a1*x[i]) + (b1*y[i]) + c1;
			d2 = (a2*x[i]) + (b2*y[i]) + c2;
			
			if(i == p1 || i == p3){
				continue;
			}
			if(d1 <= 0){
				if(d1 == 0 && !inside(p1, p3, i)){
					continue;
				}
				if(d1 < tmpD1){
					tmpD1 = d1;
					newP3 = i;
				}
				left.add(i);
			}
			
			if(i == p3 || i == p2){
				continue;
			}
			if(d2 <= 0){
				if(d2 == 0 && !inside(p3, p2, i)){
					continue;
				}
				if(d2 < tmpD2){
					tmpD2 = d2;
					newP4 = i;
				}
				
				right.add(i);
			}
		}
	
		parSeqRec(p1, p3, newP3, left, mySet);
		if(p3 != -1){
			mySet.add(p3);
		}
		parSeqRec(p3, p2, newP4, right, mySet);
	}
	
	class parWorker implements Runnable{
		int p1, p2, p3, lvl;
		IntList right, mySet; 
		parWorker(int p1, int p2, int p3, IntList right, int lvl, IntList mySet){
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
			this.right = right;
			this.lvl = lvl;
			this.mySet = mySet;
		}
		
		public void run(){
			parRec(p1, p2, p3, right, lvl, mySet);
		}
	}
}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Sort{
	public static void main(String [] args) throws FileNotFoundException {
		long startTime = System.nanoTime();//setter start tiden.
		if(args.length == 3) {//sjekker om det somm skrives inn mmed java Sort er på lengden 3
			
			int antTrad = Integer.parseInt(args[0]);//setter antall tråder til det første som er skrevet inn
			String inFile = args[1];
			String outFile = args[2];
			
			Scanner skanner = new Scanner(new File(inFile));
			int antOrd = Integer.parseInt(skanner.nextLine());//Leser inn første linje å setter antall ord.
			Broker<String> broker = new Broker <String>(antOrd);
			int linesToRead = antOrd / antTrad + antOrd % antTrad;
			
			if(linesToRead < 1){
				antTrad = 1;
			}
			
			System.out.println(linesToRead);
			int tradStart = 0;
			
			while(tradStart < antTrad) {
				List<String> list = new ArrayList<String>(linesToRead);
				int linesRead = 0;
				
				while (linesRead < linesToRead && skanner.hasNext()){//går igjennom hele filen og leger ordene til list.
					String s = skanner.nextLine();
					if(s != null && s.length() > 0){
						list.add(s);//leger ordet til listen
					}
					linesRead++;
				}
				
				QuickSort<String> trad = new QuickSort<String>(list, broker);
				trad.start();
				tradStart++;
			}
			while(!broker.isFinished()){
				try{
					Thread.sleep(500);
				} catch (InterruptedException e){
					
				}
			}
			
			List<String> mergedList = broker.getMergedList();
			writeToFile(outFile, mergedList);
		}
		else{
			System.out.println("Usage: java sort.class <antTrad> <inFile> <outFile>");
		}
		long endTime = System.nanoTime();
		double fullTime = ((endTime - startTime)*1e9);//Rekner ut tid.
		System.out.println("Tid: "+ fullTime);
	}
	private static void writeToFile(String outFile, List<String> mergedList){
		PrintWriter printWriter = null;
		
		try{
			printWriter = new PrintWriter(outFile);
			int linesWritten = 0;
			for(String s : mergedList){//går igjennom å skriver til fil.
				printWriter.write(s + " ");
				if(linesWritten % 100 == 0) {
					printWriter.flush();
				}
			}
		} catch (FileNotFoundException e){
			throw new RuntimeException("unable to write to file: "+ outFile);
		} finally {
			if(printWriter != null){
				printWriter.close();
			}
		}
	}
}

class Broker<T extends Comparable>{
	private int expectedSize;
	private List <T> mergedList = null;
	private static final Object LOCK = new Object();
	
	Broker(int expectedSize) {
		this.expectedSize = expectedSize;
	}
	public List<T> getMergedList(){
		return mergedList;
	}
	public boolean isFinished(){
		if(mergedList == null){
			return false;
		}
		if(mergedList.size() == expectedSize){
			return true;
		}
		return false;
	}
	
	public void addFinished(List<T> sortedList){
		Merger<T> merger = null;
		synchronized (LOCK) {
			if(mergedList == null){
				mergedList = sortedList;
				
			}
			else {
				merger = new Merger<T>(mergedList, sortedList, this);
				mergedList = null;
			}
		}
		if(merger != null){
			merger.start();
		}
	}

}

class Merger<T extends Comparable> extends Thread {

	private List<T> venstre;
	private List<T> hoyre;
	private Broker<T> broker;
	
	public Merger(List<T> venstre, List<T> hoyre, Broker<T> broker){
		this.venstre = venstre;
		this.hoyre = hoyre;
		this.broker = broker;
	}
	@Override
	public void run() {
		merge(broker);
		System.out.println("Merger: "+ this.getId());
	}
	public void merge (Broker<T> broker){
		List<T> list = new ArrayList<T>();
		int v = 0;
		int h = 0;
		
		while (v < venstre.size() && h < hoyre.size()){
			if(venstre.get(v).compareTo(hoyre.get(h))<0){
				list.add(venstre.get(v));
				v++;
			}
			else {
				list.add(hoyre.get(h));//Legger til høyre
				h++;
			}
		}
		for (int i = v; i < venstre.size(); i++){
			list.add(venstre.get(i));
			
		}
		for(int i = h; i < hoyre.size(); i++){
			list.add(hoyre.get(i));
		}
		broker.addFinished(list);
	}

}
class QuickSort<T extends Comparable> extends Thread {

	List<T> listeTilSortering;
	Broker<T> broker;
	
	QuickSort(List<T> listeTilSortering, Broker broker){
		this.broker = broker;
		this.listeTilSortering = listeTilSortering;
	}
	public void run(){
		List<T> sortedList = doSort(listeTilSortering);
		broker.addFinished(sortedList);
		System.out.println("QuickSort: " + this.getId());
		
	}
	
	List<T> doSort(List<T> listeTilSortering){
		if(listeTilSortering.size() <= 1){
			return listeTilSortering;
		}
		int pivotIndex = findPivot(listeTilSortering);
		T pivot = listeTilSortering.remove(pivotIndex);
		
		List<T> lesser = new ArrayList<T>();
		List<T> higher = new ArrayList<T>();
		
		for(T t : listeTilSortering) {
			if(t.compareTo(pivot) < 0){
				lesser.add(t);
			}
			else {
				higher.add(t);
			}
			
		}
		return concanate(doSort(lesser), pivot, doSort(higher));
		
	}
	private List<T> concanate(List<T> lesser, T pivot, List<T> higher){
		lesser.add(pivot);
		lesser.addAll(higher);
		return lesser;
	}
	private int findPivot(List<T> listeTilSortering){
		if(listeTilSortering.size() < 2){
			return 0;
		
		}
		else {
			return listeTilSortering.size() / 2;
		}
	}

}
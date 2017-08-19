
public class IntList {
	int n, counter;
	int [] a;
	
	IntList(int n){
		this.n = n;
		this.counter = 0;
		this.a = new int [n*2];
	}
	
	public void add(int x){
		a[counter] = x;
		counter+=1;
	}
	
	public int size(){
		return counter;
	}
	
	public boolean isEmpty(){
		return counter == 0;
	}
	
	public int get(int idx){
		return a[idx];
	}
}

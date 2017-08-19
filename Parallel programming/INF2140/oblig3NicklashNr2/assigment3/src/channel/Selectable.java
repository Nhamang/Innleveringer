package channel;
class Selectable {
	/*
	 * An action is enabled if both the internal and external conditions
	 * are satisfied. The conditions can only be tested if they are up to date,
	 * which is expressed by the boolean ready-variables.
	 */
	private boolean extGuard=false;
	private boolean intGuard=false;
	private boolean intReady=false;
	private boolean extReady=false;
	private Select mySelector;

	public synchronized void setSelect(Select s){ 
		System.out.println("Setting the selector selector");  
		mySelector = s;
	}

	public synchronized void clearReady(){
		intReady = false; 
		extReady=false;
	}

	public synchronized boolean testReady(){
		return (intReady && extReady);
	}

	public synchronized boolean testGuard() {
		return(extGuard && intGuard);
	}

	/*
	 * Note that in the methods below, synchronizing the whole method may 
	 * result in a deadlock, since mySelector may need to test guards in 
	 * this object while the object signals mySelector.
	 */
	public  void updateExternal(boolean b){
		synchronized(this){extReady = true; extGuard = b;} 
		if (mySelector != null){
			mySelector.signal();
			}
	}

	public  void updateInternal(boolean b) throws InterruptedException {
		synchronized(this){ 
			intReady = true; 
			intGuard = b; 
			}
		
		if (mySelector != null){
			mySelector.signal();
			}
	}

}

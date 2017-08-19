class patient extends Thread {
	
	String pasientID;
	secretary s;
	
	public patient(secretary s, String pasientID){
		this.pasientID = pasientID;
		this.s = s;
	}
	
	
	public void run() {
		try{
			while(true){
				
				s.venterom(pasientID);
				Thread.sleep(1000);
			}		
		}	
		catch(InterruptedException e){
			System.err.println("Pasient feil");
		}
	}
	
}
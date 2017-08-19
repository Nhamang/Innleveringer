class dentist extends Thread {
	secretary s;
	
	private int legeID;
	Clinic clin;
	
	public dentist(secretary s, int legeID, Clinic clin){
		this.s = s;
		this.legeID = legeID;
		this.clin = clin;
	}
	
	public void run(){
		try{
			while(true){
				s.legeRom(legeID);
				Thread.sleep(6000);
			}
		}
		catch(InterruptedException e){
			System.err.println("lege feil");
		}
	}
}
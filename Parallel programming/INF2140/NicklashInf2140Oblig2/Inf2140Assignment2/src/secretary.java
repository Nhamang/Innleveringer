import java.awt.Color;

class secretary {
	
	Clinic clin;
	String verd;
	String txt;

	chair chair;
	chair c;
	chair leg;
	
	public secretary(Clinic clin){
		this.clin = clin;
	}
	
	synchronized void venterom(String pasID) throws InterruptedException{
		
		chair = null;
		txt = "Tatt av pasient: "+ pasID;
		
		chair = clin.tiljenglig(txt);
		
		if(chair != null){
			chair.settVerdi(txt);
			
			chair.byttFarge(new Color(200, 0, 0));//har ikke bestemt farge
			
			notifyAll();
			
		}else{
			wait();
		}
	}
	
	synchronized void legeRom(int legeID) throws InterruptedException{
		verd = "";
		c = null;
		leg = null;
		
		c = clin.hentStol();
		
		if(c != null){
			clin.antOptatt--;
			leg = clin.hentLege(legeID);
			verd = c.hentVerdi();
			leg.settVerdi(verd);
			c.settVerdi("Ledig");
			c.byttFarge(new Color (0, 200, 0));//har ikke bestemt farge
			leg.byttFarge(new Color(200, 0, 0));//har ikke bestemt farge
			
			
			notifyAll();
		}else{
			wait();
		}
	}
	
}
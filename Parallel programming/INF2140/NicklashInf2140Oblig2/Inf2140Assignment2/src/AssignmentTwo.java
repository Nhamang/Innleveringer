import java.awt.*;
import java.applet.*;

public class AssignmentTwo extends Applet{
	
	int antStol = 4;
	int antPas = 20;
	int antLege = 2;
	
	
	String pasientID;
	Clinic clin;
	secretary s;
	
	public void init(){
		clin = new Clinic(antStol, antLege);
		
		add(clin);
		
		s = new secretary(clin);
		
		setBackground(Color.white);
		
	}
	
	public void start(){
		int i = 0;
		
		
		while(i < antLege){
			new dentist(s, i, clin).start();
			i++;
		}
		
		int j = 0;
		while(j < antPas){
			pasientID = Integer.toString(j+1);
			new patient(s, pasientID).start();
			j++;
		}
		
	}
	
	
}





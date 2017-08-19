import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.BoxLayout;


import java.util.Random;




public class Clinic extends JPanel{
	
	Font text = new Font("Times New Roman", Font.ITALIC, 26);
	
	int antStol;
	int antLege;
	int antOptatt;
	
	chair [] c;
	chair [] legeStol;
	
	
	JPanel panelStol;
	JPanel legePanel;
	
	public Clinic (int antStol, int antLege){
	
		this.antStol = antStol;
		this.antLege = antLege;
		c = new chair[antStol];
		legeStol = new chair[antLege];
		
		
		frame();
		
		int i = 0;
		while(i < c.length){
			c[i] = new chair();
			
			String mld = "Vente plass nummer: " + (i+1);
			c[i].settTekst(mld);
			panelStol.add(c[i]);
			i++;
		}
		int j = 0;
		
		while (j < legeStol.length){
			legeStol[j] = new chair();
			String mld = "Tannlege nummer: "+ (j+1);
			legeStol[j].settTekst(mld);
			legePanel.add(legeStol[j]);
			j++;
			
		}
	}
	
	
	
	public void frame(){
		
		panelStol = new JPanel(new GridLayout(2, antStol));
		panelStol.setBackground(Color.blue);
		
		legePanel = new JPanel(new GridLayout(2, antLege));
		legePanel.setBackground(Color.blue);
		
		JPanel txt = new JPanel();
		JPanel txt2 = new JPanel();
		JPanel txt3 = new JPanel(new BorderLayout());
		txt.add(new JLabel("venterom"));
		txt2.add(new JLabel("Lege Rom."));
		txt3.add(txt, BorderLayout.WEST);
		txt3.add(txt2, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		
		panel.setBackground(Color.blue);
		panel.setLayout(new BorderLayout(50, 0));
		panel.add(legePanel, BorderLayout.EAST);
		panel.add(panelStol, BorderLayout.WEST);
		panel.add(txt3, BorderLayout.NORTH);
		
		add(panel);
		
	}
	
	
	public chair hentLege(int legeID){
		return legeStol[legeID];
	}
	
	public int hentVentePasient(){
		return antOptatt;
	}
	
	public chair tiljenglig(String verdi){
		int i = 0;
		
		while(i<c.length){
			if(venterom(verdi) == false && legeRom(verdi) == false && (c[i].sjekkVerdi()) == true){
				antOptatt++;
				return c[i];
				
			}
			i++;
		}
		return null;
		
	}
	
	public boolean venterom(String verdi){
		int i = 0;
		int teller = 0;
		
		while(i < c.length){
			if((c[i].hentVerdi()).equals(verdi)){
				teller++;
			}
			i++;
		}
		if(teller > 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean legeRom(String verdi){
		int i = 0;
		int teller = 0;
		
		while(i < legeStol.length){
			if((legeStol[i].hentVerdi()).equals(verdi)){
				teller++;
			}
			i++;
		}
		if(teller > 0){
			return true;
		}else{
			return false;
			}
	}
	
	
	public chair hentStol(){
		if(antOptatt != 0){
			Random r = new Random();
			
			int tilfeldig = r.nextInt(antStol);
			
			while(true){
				if((c[tilfeldig].sjekkVerdi()) == false){
					return c[tilfeldig];
				}
				tilfeldig = r.nextInt(antStol);
				
				}
			}
			else{
				return null;
			}
			
		}
		
		
}

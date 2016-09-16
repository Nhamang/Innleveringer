
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import java.awt.BorderLayout;				
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

*/
/**Programmet vil bare løse den første linjen.
 * @author Nicklash
 */
class SudokuBuffer {
	
	int antallLosninger = 0;
	int visLosninger = 0;
	ArrayList<int[][]> losninger = new ArrayList<int[][]>();
	
	public int antallLosninger(){
		return antallLosninger;
		
	}
	public void insert (int [][] losning){
		if(antallLosninger() < 500)
			losninger.add(losning);
		
		antallLosninger++;
		}
	public int [][] get() {
		if(visLosninger == 500)
			return null;
		
		visLosninger++;
		return losninger.get(visLosninger-1);
	}
}

public class Sudoku extends JFrame implements ActionListener {
	
	private final int RUTE_STRELSE = 50;//Storrelsen til hver rute
	private final int PLASS_TOPP = 50;
	
	private JTextField[][] brett;
	private int dimensjon;
	private int vertikalAntall;
	private int horisontalAntall;
	
		Brett b;
		SudokuBuffer buffer;
		
		// Skaper et brett med knapper på toppen.
		
		public Sudoku(int dim, int hd, int br, Brett b, SudokuBuffer buffer){
			this.b = b;
			this.buffer = buffer;
			dimensjon = dim;
			vertikalAntall = hd;
			horisontalAntall = br;
			
			brett = new JTextField[dimensjon][dimensjon];
			
			setPreferredSize(new Dimension(dimensjon * RUTE_STRELSE, dimensjon * RUTE_STRELSE + PLASS_TOPP));
			
			setTitle("Sudoku "+ dimensjon +" X "+ dimensjon);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setLayout(new BorderLayout());
			
			JPanel knappePanel = lagKnapper();
			JPanel brettPanel = lagBrettet();
			
			getContentPane().add(knappePanel, BorderLayout.NORTH);
			getContentPane().add(brettPanel, BorderLayout.CENTER);
			pack();
			setVisible(true);
						
		}
		
		// lager panel med alle rutene
		private JPanel lagBrettet(){
			
			int topp, venstre;
			JPanel brettPanel = new JPanel();
			brettPanel.setLayout(new GridLayout(dimensjon, dimensjon));
			brettPanel.setAlignmentX(CENTER_ALIGNMENT);
			brettPanel.setAlignmentY(CENTER_ALIGNMENT);
			setPreferredSize(new Dimension(new Dimension(dimensjon * RUTE_STRELSE, dimensjon * RUTE_STRELSE)));
			
			for(int i = 0; i < dimensjon; i++){
				topp = (i % vertikalAntall == 0 && i != 0) ? 4 : 1;
				for(int j = 0; j < dimensjon; j++){
					venstre = (j % horisontalAntall == 0 && j !=0) ? 4 : 1;
					
					JTextField ruten = new JTextField();
					ruten.setBorder(BorderFactory.createMatteBorder(topp, venstre, 1, 1, Color.black));
					ruten.setHorizontalAlignment(SwingConstants.CENTER);
					ruten.setPreferredSize(new Dimension(RUTE_STRELSE, RUTE_STRELSE));
					ruten.setText(b.ruter[i][j].getVerdi() +"");
					brett[i][j] = ruten;
					brettPanel.add(ruten);
					
				}
			}
			return brettPanel;
		}
		
		JButton nesteKnapp;
		JButton finnSvarKnapp;
		
		private JPanel lagKnapper(){
			JPanel knappPanel = new JPanel();
			knappPanel.setLayout(new BoxLayout(knappPanel, BoxLayout.X_AXIS));
			finnSvarKnapp = new JButton("Finn Losning(er)");
			nesteKnapp = new JButton("Neste losning");
			knappPanel.add(finnSvarKnapp);
			knappPanel.add(nesteKnapp);
				finnSvarKnapp.addActionListener(this);
				nesteKnapp.addActionListener(this);
			
				return knappPanel;
		}
		
		boolean funnet = false;
		
		
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == finnSvarKnapp){
				if(!funnet) {
					b.finnLosninger();
					funnet = true;
				}
				else{
					JOptionPane.showMessageDialog(this, "Losning er allerede funnet");
					
				}
			}
			
			if(e.getSource() == nesteKnapp){
				if(buffer.antallLosninger() == 0){
					JOptionPane.showMessageDialog(this, "Ingen losning funnet");
				}
				else if(buffer.antallLosninger() == buffer.visLosninger){
					JOptionPane.showMessageDialog(this, "Kommet til enden av losningene");
				}
				else {
					int[][] nesteLosning = buffer.get();
					for(int i = 0; i < nesteLosning.length; i++){
						for(int j = 0; j <nesteLosning[i].length; j++){
							brett[i][j].setText(nesteLosning[i][j] + "");
					}
				}
			}
		}
}

		//Hoved klassen
		public static void main (String [] args) {
			Brett b;
			
			if(args.length > 0){
				if(args.length > 1){
					b = new Brett(args[0], args[1]);
					b.finnLosninger();
					b.write(args[1]);
					
				}
				else{
					b = new Brett(args[0], "");
					b.opprettGUI();
					
				}
			}
			else {
				b = new Brett("", "");
				b.opprettGUI();
			}
		}
}

class Brett{
	Rute[][] ruter;
	Rad[] rad;
	Kolonne [] kol;
	Boks [] boks;
	
	SudokuBuffer buffer = new SudokuBuffer();
	
	int teller;
	int y;
	int x;
	int d;
	
	
	
	Brett(String filnavn, String outputfil){
		if(filnavn.equals("")){
			JFileChooser velger = new JFileChooser();
			if(velger.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				filnavn = velger.getSelectedFile().getPath();
				
			}
		}
	/**
	 * Leser fra en fil og legger infoen inn i de riktige Arrayene
	 */
		try {
			
			Scanner sc = new Scanner(new File(filnavn));
			
			d = Integer.parseInt(sc.nextLine());
			y = Integer.parseInt(sc.nextLine());
			x = Integer.parseInt(sc.nextLine());
			
			ruter = new Rute[d][d];
			rad = new Rad[d];
			kol = new Kolonne[d];
			boks = new Boks[x * y];
			
			for (int i = 0; i<d; i++){
				rad[i] = new Rad(d);
				kol[i] = new Kolonne(d);
				boks[i] = new Boks(d);
			}
			int i = 0;
			
			/**Putter verdien der den skal
			 * 
			 */
			
			while (sc.hasNextLine()){
				String linje = sc.nextLine(); 
					
					for(int j = 0; j<d; j++){
						ruter[i][j] = new Rute (rad[i], kol[i], boks[((i/y) * y) + j/x], 0, d);
						if(linje.charAt(j) != '.'){
							int v = (Character.getNumericValue(linje.charAt(j)));
							ruter[i][j] = new Rute (rad[i], kol[i], boks[((i/y) * y) + j/x], v, d);
						}
						/*else if(linje.charAt(j) == '.'){
							ruter[i][j] = new tomRute (rad[i], kol[i], boks[((i/y) * y) + j/x], d);
						}*/
						kol[j].leggTilRute(ruter[i][j]);
						rad[i].leggTilRute(ruter[i][j]);
						boks[((i / y) * y) + j/x].leggTilRute(ruter[i][j]);
						
						ruter[i][j].kol = kol[j];
						ruter[i][j].rad = rad[i];
						ruter[i][j].boks = boks[((i / y) * y) + j/x];
					}
					i++;
				}
				settNestePeker();
			}
			
			catch (FileNotFoundException e){
				System.out.println("Fil finnes ikke"+ e.getMessage());
			}
			
			if(!(outputfil.equals(""))){
				write(outputfil);
			}
		}
	/**
	 * Skaper en GUI fra infoen fra filen.
	 */
	public void opprettGUI(){
		new Sudoku(d, y, x, this, buffer);
	}
		/**Skriver ut en fil med Løsningene, gir en bekreftelse at filen har blitt skrevet.
		 * 
		 */
		public void write(String outputfil){
			
			try{
				File fil = new File(outputfil);
				PrintWriter skriver = new PrintWriter(new FileOutputStream(fil), true);
				
				for(int i = 0; i < buffer.losninger.size(); i++){
					int[][] array = buffer.losninger.get(i);
					skriver.print((i+1) +": ");
					for (int j = 0; j<array.length; j++){
						for(int k = 0; k<array[j].length; k++){
							skriver.print(array[j][k] + "");
						}
						skriver.print("// ");
					}
					skriver.print("");
				}
				skriver.close();
				JOptionPane.showMessageDialog(null,  "Datafil skrevet");
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null, "Kunne ikke skrive fil");
			}
		}
		
		/**
		 * Setter peker til neste rute
		 */
		public void settNestePeker() {
			for(int i = 0; i<ruter.length; i++){
				for(int j = 0; j< ruter[i].length; j++){
					if(j+1 == d){
						if((j+1) != d){
						ruter[i][j].setNext(ruter[i+1][0]);
						}
					}
					else{
						ruter[i][j].setNext(ruter[i][j+1]);
					}
				}
			}
		}
		
		/**Finner løsninger, tester dem og printer dem ut.
		 * 
		 */
		public void finnLosninger(){
			ruter[0][0].settTallMegOgResten(this);
			
			testLosning();
		}
		/**
		 * Lagrer losningene til bufferen så vi kan hente dem når de trengs.
		 */
		public void lagreLosning(){
			int[][] enLosning = new int[d][d];
			
			for(int i = 0; i<ruter.length; i++){
				for(int j= 0; j< ruter[i].length; j++){
					enLosning[i][j] = ruter[i][j].getVerdi();
				}
			}
			buffer.insert(enLosning);
		}
		//Tester og printer ut så mange læsninger den klarer
		public void testLosning() {
			System.out.println("Totalt antall losninger"+ buffer.antallLosninger());
			System.out.println("Storrelse paa brett "+ d +"x"+ d);
			for(int i = 0; i<buffer.losninger.size(); i++){
				int[][] array = buffer.losninger.get(i);
				for (int j = 0; j<array.length; j++){
					for(int k = 0; k<array[j].length; k++){
						System.out.print(array[j][k]);
					}
					System.out.println("");
				}
		
			}
			
		}
		
	
		

//Karateristikkene til rad, kolonne og boks.
class Karateristikk{
	ArrayList<Rute> ruter;
	
	public boolean leggTilRute(Rute r){
		return ruter.add(r);
	}
	
	public boolean lovVerdi (int v){
		int i = 0;
		while(ruter.size() > i){
			if(ruter.get(i).getVerdi() == v){
				return false;
			}
			i++;
		}
		return true;
	}
	
}

class Kolonne extends Karateristikk{
	Kolonne (int d) {
		ruter = new ArrayList<Rute>();
	}
}

class Rad extends Karateristikk{
	Rad (int d) {
		ruter = new ArrayList<Rute>();
	}
}

class Boks extends Karateristikk{
	Boks (int d) {
		ruter = new ArrayList<Rute>();
		}
	}

//Hoved klassen til ruter.
class hovedRute {
	
	int verdi;
	int dimensjon;
	Rute nesteRute;
	Rad rad;
	Kolonne kol;
	Boks boks;
	
	public int getVerdi(){
		return verdi;
	}
	//sjekker om verdien i ikke er i raden, kolonnen eller boksen.
	public boolean lovVerdi(int v){
		if(rad.lovVerdi(v) && kol.lovVerdi(v) && boks.lovVerdi(v)){
			return true;
		}
		return false;
	}
	
	public void setNext (Rute r) {
		this.nesteRute = r;
	}
	
	public Rute getNext() {
		return nesteRute;
	}
	
	private void setVerdi(int v){
		this.verdi = v;
	}
	//Putter inn nummeret til rute
	public void settTallMegOgResten(Brett b) {
		if (verdi == 0) {
			for (int i = 1; i <= dimensjon; i++){
				if(lovVerdi(i)){
					setVerdi((char)i);
					if(nesteRute == null){
						b.lagreLosning();
						setVerdi(0);
					}
					else {
						nesteRute.settTallMegOgResten(b);
						setVerdi(0);
					}
				}
			}
		}
		else {
			if(nesteRute == null){
				b.lagreLosning();
			}
			else {
				nesteRute.settTallMegOgResten(b);
			}
		}
	}
	}
//Lager en ferdigfylt rute
class Rute extends hovedRute {//Lager en ferdig fylt Rute.
	
	Rute(Rad r, Kolonne k, Boks b, int v, int d){
		this.rad = r;
		this.kol = k;
		this.boks = b;
		this.verdi = v;
		this.dimensjon = d;
	}
	
	/*public int getVerdi(){
		return verdi;
	}
	
	public boolean lovVerdi(int v){
		if(rad.lovVerdi(v) && kol.lovVerdi(v) && boks.lovVerdi(v)){
			return true;
		}
		return false;
	}
	
	public void setNext (Rute r) {
		this.nesteRute = r;
	}
	public Rute getNext() {
		return nesteRute;
	}
	private void setVerdi(int v){
		this.verdi = v;
	}
	
	public void settTallMegOgResten(Brett b) {
		if (verdi == 0) {
			for (int i = 1; i <= dimensjon; i++){
				if(lovVerdi(i)){
					setVerdi((char)i);
					if(nesteRute == null){
						b.lagreLosning();
						setVerdi(0);
					}
					else {
						nesteRute.settTallMegOgResten(b);
						setVerdi(0);
					}
				}
			}
		}
		else {
			if(nesteRute == null){
				b.lagreLosning();
			}
			else {
				nesteRute.settTallMegOgResten(b);
			}
		}
	}*/
	
	
}
//lager en tom rute
class tomRute extends hovedRute {
	tomRute(Rad r, Kolonne k, Boks b, int d){
		this.rad = r;
		this.kol = k;
		this.boks = b;
		this.verdi = 0;
		this.dimensjon = d;
	}
}


}

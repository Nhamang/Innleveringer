import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

class chair extends Canvas {
	
	Font f = new Font("Times", Font.BOLD, 16);
	Font f2 = new Font("Times New Roman", Font.ITALIC, 16);
	
	Color txtC = new Color(0, 200, 0);
	String txt;
	String tittel;
	
	public chair(){
		super();
		tittel = "";
		txt = "Ledig";
		setSize(250, 200);
	}
	


	public void settTekst(String mld) {
		this.tittel = mld;
		repaint();
	}
	
	public void settVerdi(String txt) {
		this.txt = txt;
		repaint();
	}
	
	public String hentVerdi() {
		return txt;
	}

	public void byttFarge(Color color){
		txtC = color;
		repaint();
	}


	public boolean sjekkVerdi() {
		if(txt.equals("Ledig")){
			return true;
		}
		return false;
	}

	public void paint(Graphics g){
		g.setColor(Color.black);
		g.drawRect(0, 0, 250, 300);
		g.fillRect(0, 0, 250, 300);
		
		g.setColor(new Color(230, 230, 255));//har ikke bestemt farge
		g.fillRect(5, 5, 240, 140);
		g.setColor(Color.black);
		
		
		g.setFont(f);
		FontMetrics fontM = g.getFontMetrics();
		
		
		int w = fontM.stringWidth(tittel);
		int x = (getSize().width - w)/2;
		int h = fontM.getHeight();
		int y = h;
		
		
		
		g.drawString(tittel, x, y);
		g.drawLine(x, y+3, x+w, y+3);
		
		
		g.setFont(f2);
		fontM = g.getFontMetrics();
		String s = txt;
		
		w = fontM.stringWidth(s);
		h = fontM.getHeight();
		x = (getSize().width - w)/2;
		y = (getSize().height + h)/2;
		
		g.setColor(txtC);
		g.drawString(s, x, y);
				
	}
	
}
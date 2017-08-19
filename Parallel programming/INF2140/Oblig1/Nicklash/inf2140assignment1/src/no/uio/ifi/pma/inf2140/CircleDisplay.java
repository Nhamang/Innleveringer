package no.uio.ifi.pma.inf2140;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

/*
 * This class is used to display a dynamic circle when FM radio scanning and the status of the corresponding frequency 
 */
@SuppressWarnings("serial")
class CircleDisplay extends Canvas implements IRadioDisplay {
    int angle_ = 0;
    String title_;
    String status_;
    Color arcColor_ = Color.blue;
    int segStart_ = 9999;
    int segEnd_ = 9999;
    Color segColor_ = Color.yellow;

    Font f1 = new Font("Times",Font.BOLD,24);
    Font f2 = new Font("Helvetica",Font.ITALIC+Font.BOLD,20);
    
    final static int Cx = 20;
    final static int Cy = 65;

    CircleDisplay(String title, Color c) {
        super();
        title_=title;
        status_ = "OFF";
        setSize(200,150);
        arcColor_=c;
  	}

    public void setColor(Color c){
        setBackground(c);
        repaint();
    }
    
    public void setText(String s){
    	status_ = s;
    	repaint();
    }
    
    public void setAngle(int a){
        angle_ = a;
        repaint();
    }

    public int getAngle() {
    	return angle_;
    }
    
    public void setSegment(int start, int end, Color c) {
        segStart_ = start;
        segEnd_ = end;
        segColor_ = c;
    }

    public void paint(Graphics g){
        update(g);
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public synchronized void update(Graphics g){
        Dimension d = getSize();
	    if ((offscreen == null) || (d.width != offscreensize.width)
	                            || (d.height != offscreensize.height)) {
	        offscreen = createImage(d.width, d.height);
	        offscreensize = d;
	        offgraphics = offscreen.getGraphics();
	        offgraphics.setFont(getFont());
 	    }
	    offgraphics.setColor(getBackground());
	    offgraphics.fillRect(0, 0, d.width, d.height);

             // Display the title
         offgraphics.setColor(Color.black);
         offgraphics.setFont(f1);
         FontMetrics fm = offgraphics.getFontMetrics();
         int w = fm.stringWidth(title_);
         int h = fm.getHeight();
         int x = (getSize().width - w)/2;
         int y = h;
         offgraphics.drawString(title_, x, y);
         offgraphics.drawLine(x,y+3,x+w,y+3);
         
         offgraphics.setFont(f2);
         fm = offgraphics.getFontMetrics();
         
         int w1 = fm.stringWidth(status_);
         int h1 = fm.getHeight();
         int x1 = (getSize().width - x - w1 + 30);
         int y1 = y + h + 10;
         offgraphics.drawString(status_, x1, y1);
         offgraphics.drawLine(x1,y1+3,x1+w1,y1+3);
         
         // Display the arc
         if (angle_>0) {
             if (angle_<segStart_ || segStart_==segEnd_) {
                offgraphics.setColor(arcColor_);
                offgraphics.fillArc(Cx,Cy,90,90,0,angle_);
             } else if ( angle_>=segStart_ && angle_<segEnd_) {
                offgraphics.setColor(arcColor_);
                offgraphics.fillArc(Cx,Cy,90,90,0,segStart_);
                if (angle_-segStart_>0) {
                    offgraphics.setColor(segColor_);
                    offgraphics.fillArc(Cx,Cy,90,90,segStart_,angle_-segStart_);
                }
             } else  {
                offgraphics.setColor(arcColor_);
                offgraphics.fillArc(Cx,Cy,90,90,0,segStart_);
                offgraphics.setColor(segColor_);
                offgraphics.fillArc(Cx,Cy,90,90,segStart_,segEnd_-segStart_);
                if (angle_-segEnd_>0){
                    offgraphics.setColor(arcColor_);
                    offgraphics.fillArc(Cx,Cy,90,90,segEnd_,angle_-segEnd_);
                }
             }
         }
         g.drawImage(offscreen, 0, 0, null);
    }
}


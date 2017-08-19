package no.uio.ifi.pma.inf2140;



import java.applet.Applet;
import java.awt.Color;
@SuppressWarnings("serial")
public class AssignmentOne extends Applet {
	RadioPanel A;

  public void init() {
    A = new RadioPanel("Radio FM",Color.blue);
    add(A);
	setBackground(Color.lightGray);
  }

  public void start() {
    A.start();
  }

  public void stop() {
    A.stop();
  }
}
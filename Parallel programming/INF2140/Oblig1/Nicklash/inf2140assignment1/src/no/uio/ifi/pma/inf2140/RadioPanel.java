package no.uio.ifi.pma.inf2140;


import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.JPanel;


/*
 * This class is GUI class which contains the components of FM radio (dynamic and static components). The business logic of the system is stimulated here.'
 * Interactions between GUI components as well as events handling are captured.    
 */

/********************************************************/

@SuppressWarnings("serial")
public class RadioPanel extends JPanel {

	 IRadioController controller;
	 CircleDisplay circledisplay_;
	
    Button scan;
    Button lock;
    Button end, on, off, reset;
    StringCanvas display;
    Rotator rotator;
    int angle_=0;				// Angle of tuning
    final static int step = 9;	// corresponds to 1 frequency: 1 MHz = 9 degree
	final static int highest = 108; // Highest frequency
	final static int lowest = 88; 	// Lowest frequency of FM radio


    public RadioPanel(String title, Color c) {
        super();
        // Set up Buttons
        this.setFont(new Font("Helvetica",Font.BOLD,14));
        JPanel p = new JPanel();
        p.setBackground(Color.gray);
        p.add(scan=new Button("Scan"));
        scan.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            controller.eventScan();
          }
        });
		
        p.add(lock=new Button("Lock"));
		
        lock.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            controller.eventLock();
          }
        });

        
        JPanel p1 = new JPanel();
        p1.setBackground(Color.gray);
        p1.setLayout(new BoxLayout(p1, BoxLayout.PAGE_AXIS));
        p1.add(on=new Button("ON"));
		
        on.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            controller.eventOn();
          }
        });
		
        p1.add(off=new Button("OFF"));
		
        off.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            controller.eventOff();
          }
        });

        p1.add(reset=new Button("Reset"));
		
        reset.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           controller.eventReset();
          }
        });
        
        
        JPanel t = new JPanel();
        t.setLayout(new BorderLayout());
        t. add("South",p);
       
        //setLocation(getSize().width /2, 0);        
       // add(p1, BorderLayout.WEST);
        circledisplay_ = new CircleDisplay(title,c);
        circledisplay_.setColor(Color.white);
        circledisplay_.setAngle(0);
        t.add("North",circledisplay_);
        
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(t);
        add(p1);
		//setBackground(Color.lightGray);
        
        rotator = new Rotator(this);
        rotator.start();
        controller = new RadioController(circledisplay_,rotator);
    }

    public synchronized void rotate() {
        angle_=(circledisplay_.getAngle()+step)%180;
        circledisplay_.setAngle(angle_);
        circledisplay_.setText((highest - angle_/9) + "MHz");
    }

    public void start() {
    }

    public void stop() {
    	rotator.interrupt();
    }

}

/********************************************************/


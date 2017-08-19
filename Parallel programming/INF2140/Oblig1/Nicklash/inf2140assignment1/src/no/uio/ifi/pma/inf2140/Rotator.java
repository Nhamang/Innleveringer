
package no.uio.ifi.pma.inf2140;

class Rotator extends Thread implements IRotator {
	final static int highest = 108; // Highest frequency
	final static int lowest = 88; 	// Lowest frequency of FM radio
	RadioPanel display;
	boolean scanning = false;
    int rate = 500;					// Rate of tuning
    int angle_=0;				// Angle of tuning
    
	

	public synchronized void startScanning() { 
		scanning = true; 
//		this.notify();
		}
	public synchronized void stopScanning() { 
		scanning = false;
	}
	
	public Rotator(RadioPanel display) {
		this.display = display;
	}
	
	public void run() {
		try {
			while (true) {
				if(scanning) {
					display.rotate();
				}
				Thread.sleep(rate);
//				this.wait();
			}
		} catch(InterruptedException e) {}
	}
}
package no.uio.ifi.pma.inf2140;

import java.awt.Color;

class RadioController implements IRadioController {

    int on = 0;
    public IRadioDisplay rd;
    public IRotator rt;

	
    RadioController(IRadioDisplay display, IRotator rotator) {
	this.rd = display;
	this.rt = rotator;
    }

    public void eventLock() {
    	if(on ==1 ){
    		
    	rt.stopScanning();
    	}	
    }	

    public void eventScan() {
	if(on == 1){
		rt.startScanning();
	}

    }
    
    public void eventOn() {
	if(on == 0){
	    rd.setColor(Color.red);
	    rd.setText("ON");
	    on = 1;
	  }
    }
    
    public void eventOff() {
    	if(on == 1){
    		rd.setColor(Color.white);
    		rd.setText("OFF");
    		on = 0;
    		rt.stopScanning();
    		rd.setAngle(-1);
    	}
    }
    
    public void eventReset() {
    	if(on ==1){
    		rd.setText("ON");
    		rt.stopScanning();
    		rd.setAngle(-1);
    	}
    }
}
    



/****************************************************************************/

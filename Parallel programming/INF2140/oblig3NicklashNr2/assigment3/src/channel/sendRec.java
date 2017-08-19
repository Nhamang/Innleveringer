package channel;

class sender implements Runnable{

	
	private int id, nr, bit, valg, mottatBit, xId;
	private final int send, motta;
	
	private Select sendSelect;
	private Selectable a, d;
	private UnreliableChannel<String> k, l;

	private boolean igang, klar;

	public sender(int bit, Select sendSelect, Selectable a, Selectable d, UnreliableChannel<String> k, UnreliableChannel<String> l){
		klar = true;
		this.bit = bit;
		send = 1;
		motta = 0;
		this.sendSelect = sendSelect;
		this.a = a;
		this.d = d;
		this.k = k;
		this.l = l;
	}

	public void run(){
		while(true){
			try{
				valg = sendSelect.choose();
			}catch (InterruptedException e){

			}
			id = hentID();
			if(valg == send && id!= 0){
				try{
					dataSend(id);
					a.updateExternal(true);
					d.updateExternal(true);

					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else if(valg == motta && id !=0){
				try{
					String s = l.receive();

					mottatBit = (int) (s.charAt(0) - 48);
					xId = (int) (s.charAt(2) - 48);

					a.updateExternal(true);
					d.updateExternal(true);
					Thread.sleep(500);

					if(mottatBit == bit && xId == id){
						bit = (bit+1)%2;
						oppdaterP(false);
						if(id == 1){
							id++;
						}
						else id--;
					}
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}




	private int hentID(){
		return id;
	}
	private void dataSend(int id){
		try {
			k.send(nr+","+bit+","+id);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private synchronized void oppdaterP(boolean b) {
		igang = false;
		notifyAll();

	}

	public void start() {
		// TODO Auto-generated method stub
		a.updateExternal(true);
		d.updateExternal(false);
	}

	public synchronized void sjekkKlar(){
		// TODO Auto-generated method stub
		while(!klar){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public synchronized void oppdaterKlar(boolean status) {
		// TODO Auto-generated method stub
		klar = status;
		notifyAll();
	}

	public synchronized void settiden(int id) {
		// TODO Auto-generated method stub
		this.id = id;
		
	}

	public void settnr(int nr2) {
		// TODO Auto-generated method stub
		nr = nr2;
		System.out.println("s"+id+"in_msg."+nr);
		igang = true;
	}

	public synchronized void sjekkP() {
		// TODO Auto-generated method stub
		while(igang){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}


}

class reciver implements Runnable{

	private String data, dataUt;

	private int bit, bit2, send, mottat, nr, nr2, nrUt, id, id2, mottakerId, valg;

	private Select recSelect;
	UnreliableChannel<String> k, l;
	private Selectable b, c;


	public reciver(Select recSelect, Selectable b, Selectable c, UnreliableChannel<String> k, UnreliableChannel<String> l) {
		// TODO Auto-generated constructor stub
		
		dataUt = null;
		bit = 1;
		send = 0;
		mottat = 1;
		this.recSelect = recSelect;
		this.b = b;
		this.c = c;
		this.k = k;
		this.l = l;
	}

	public void run(){
		// TODO Auto-generated method stub
		while(true){
			valg = -1;
			try {
				valg = recSelect.choose();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(valg == send){
				try{
					sendB();
				

				b.updateExternal(true);
				c.updateExternal(false);

				Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(valg == mottat){
				try{
				mData();
				b.updateExternal(true);
				c.updateExternal(true);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
//henter data over k. receives data from k.
	private void mData() throws InterruptedException {
		// TODO Auto-generated method stub
		data = k.receive();
		nr2 = (int)(data.charAt(0)-48);//gjør om fra tekst til tall. converts from text to number.
		id2 = (int) (data.charAt(4)-48);
		bit2 = (int) (data.charAt(2)-48);
		
		if(bit!=bit2){
			nr = nr2;
			id = id2;
			bit=bit2;
			oppDataUt(id+","+nr);
			
			
		}
		
		
	}
//oppdaterer dataen som skal ut. updates data output
	void oppDataUt(String string) {
		// TODO Auto-generated method stub
		dataUt = string;
		notifyAll();
	}
	//sender bit over l. sends bit over l
	private void sendB() {
		// TODO Auto-generated method stub
		try {
			l.send(bit+","+id);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void start() {
		b.updateExternal(true);
		c.updateExternal(false);

	}

	public  synchronized int sjekkData(superRec superRec) {
		// TODO Auto-generated method stub
		while(dataUt == null){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		mottakerId = (int)(dataUt.charAt(0)-48);
		
		if(superRec.hentid() == mottakerId){
			nrUt = (int)(dataUt.charAt(2)-48);
			return nrUt;
		}
		else return -1;
		
	}

	public void godkjent(int id) {
		// TODO Auto-generated method stub
		System.out.println("R"+id+".in_ack");
	}
/*
	public void opdata(Object object) {
		// TODO Auto-generated method stub
		
	}*/

}

class superSend extends Thread{
	
	private sender sender;
	
	private int id, nr, spec;
	
	public superSend(sender send, int id){
		// TODO Auto-generated constructor stub
		this.sender = send;
		this.id = id;
		spec = 5;
		nr = 0;	
	}
	/*
	void run(){
		sender.settiden(id);
		nr = nr%spec;
		sender.settnr(nr);
	}*/
	
	public void run(){
		
		sender.sjekkKlar();
		sender.oppdaterKlar(false);
		sender.settiden(id);
		sender.settnr(nr);
		System.out.println("punkt3");
		System.out.println("s"+id+".out_ack");
		
		nr = (nr+1)%spec;
		
		sender.sjekkP();
		
		sender.oppdaterKlar(true);
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}

class superRec extends Thread{
	
	int nr, nr2, id;
	
	reciver reciver;
	
	public superRec(reciver rec, int id){
		nr = 0;
		this.id = id;
		this.reciver = rec;
		
	}
	
	public int hentid(){
		return id;
	}
	
	void dataSend(int nr){
		this.nr = nr;
		System.out.println("punkt2");
	}
	
	public void run(){
		while(true){
			nr2 = reciver.sjekkData(this);
			if(nr2 != -1){
				System.out.println("punkt1");
				reciver.godkjent(id);
			}
			reciver.oppDataUt(null);
		}
	}

}
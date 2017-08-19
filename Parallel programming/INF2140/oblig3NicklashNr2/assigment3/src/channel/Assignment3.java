package channel;

public class Assignment3 {
	public static void main(String [] args) throws InterruptedException{
		
		//skaper a, b, c og d. creates a, b, c and d
		Selectable a = new Selectable();
		Selectable b = new Selectable();
		Selectable c = new Selectable();
		Selectable d = new Selectable();
		
		
		Select sendSelect = new Select();
		Select recSelect = new Select();
		
		//skaper kanalene K og L. Creates channels K and L
		UnreliableChannel<String> k = new UnreliableChannel<String>("K", a, b);
		UnreliableChannel<String> l = new UnreliableChannel<String>("L", c, d);	
		

		
		sender s = new sender(0, sendSelect, a,  d,  k, l);
		reciver r = new reciver(recSelect, b, c, k, l);
		
		
		
		s.start();
		r.start();
		
		//setter opp s1, s2, r1 og r2. Setup s1, s2, r1 and r2
		superSend s1 = new superSend(s, 1);
		superSend s2 = new superSend(s, 2);
		superRec r1 = new superRec(r, 1);
		superRec r2 = new superRec(r, 2);
		
		Thread s_T = new Thread(s);
		Thread k_T = new Thread(k);
		Thread r_T = new Thread(r);
		Thread l_T = new Thread(l);
		
		
		sendSelect.add(d);
		sendSelect.add(a);
		recSelect.add(c);
		recSelect.add(b);
		
		s1.start();
		s2.start();
		
		k_T.start();
		l_T.start();
		
		Thread.sleep(500);
		
		s_T.start();
		
		Thread.sleep(500);
		
		r_T.start();
		
		r1.start();
		r2.start();
		
		
		
		}
}

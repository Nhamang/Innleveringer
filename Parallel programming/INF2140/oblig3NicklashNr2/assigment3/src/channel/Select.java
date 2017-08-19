package channel;

import java.util.*;

/*********************Select*****************************/
// implements choice
class Select {
	/*
	 * A select object has a list of Selectable objects, and can select 
	 * one of these when all are ready to make a choice. After the choice
	 * has been made, all the Selectable objects must become available
	 * for a new choice by updating their internal and external guards.
	 */
	private ArrayList<Selectable> list = new ArrayList<Selectable>(2);

	public synchronized void add(Selectable s) {
		list.add(s);
		s.setSelect(this);
	}

	/*
	 * Allow Selectables to signal that they have become ready
	 */
	public synchronized void signal(){ 
		notifyAll();
		}

	private synchronized boolean readyAll(){
		boolean ready=true;
		for (Selectable s:list){ 
			ready = (ready && s.testReady()); 
		}
		return ready;
	}

	/*
	 * Test Selectable objects, return one which is enabled
	 */
	private synchronized int testAll() throws InterruptedException  {
		int i = 1;
		for (Selectable s:list){
			if (s.testGuard()) { 
				s.clearReady(); 
				return i; 
			}
			++i;
		}
		return 0;
	}

	/*
	 * This method can get suspended if
	 * (a) not all Selectable objects are ready to make a choice
	 * (b) no Selectable object is enabled
	 */
	public synchronized int choose() throws InterruptedException {
		int readyIndex = 0;
		while (readyIndex==0) {
			if (!readyAll()){ 
				wait(); 
			}  // Not ready to make a choice
			readyIndex=testAll();
			if (readyIndex==0) { 
				wait(); 
			} // No choice is enabled
		}
		return readyIndex;
	}
}





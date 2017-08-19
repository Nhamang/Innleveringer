package inf1010.assignment;

import java.util.*;
import inf1010.lib.two.IfiCollection;

public class OrderedSet<E extends Comparable<? super E>> implements
							     IfiCollection<E> {

    Node forsteNode;
    int antall = 0;

	/**
	 * Legger til en ny node.
	 * @param E
	 */
    public boolean add(E e){
	if (e == null) {
	    throw new NullPointerException();
	} else if (forsteNode == null) {
	    forsteNode = new Node();
	    forsteNode.data = e;
	    antall++;
	    return true;
	}
	if (contains(e)) {
	    return false;
	} else {
	    Node temp = forsteNode;
	    Node forrige = null;
	    while (temp != null) {
		if (temp.data.compareTo(e) > 0) {
		    Node ny = new Node();
		    ny.data = e;
		    ny.nesteNode = temp;
		    if (forrige == null) {
			forsteNode = ny;
		    } else {
			forrige.nesteNode = ny;
		    }
		    antall++;
		    return true;
		}
		forrige = temp;
		temp = temp.nesteNode;
	    }
	    forrige.nesteNode = new Node();
	    forrige.nesteNode.data = e;
	    antall++;
	    return true;
	}
    }




	/**
	 * Ser om en node finnes
	 * @param dataen til noden som skal sjekkes om finnes
	 */
    public boolean contains(E e) {

	Node temp = forsteNode;
	if(temp == null) {
	    return false;
	}
	if (temp.data.compareTo(e) == 0) {
	    return true;
	}
	temp = forsteNode.nesteNode;
	while (temp != null) {
	    if (temp.data.compareTo(e) == 0) {
		return true;
	    }
	    temp = temp.nesteNode;
	}
	return false;
    }

 
	/**
	 * fjerner en node
	 * sjekker om det finnes noen noder hvis det er noen noder leter den etter den noden som har samme data, hvis den noden finnes fjernes den.
	 * @param Dataen som skal fjernes.
	 * @return false hvis det ikke finnes noen, true hvis den finnes og blir fjernet.
	 */
    public boolean remove(E e) {
	if (forsteNode == null) {
	    return false;
	}
	if (forsteNode.data.compareTo(e) == 0) {
	    forsteNode = forsteNode.nesteNode;
	    antall--;
	    return true;
	}
	Node temp = forsteNode;
	while(temp.nesteNode != null) {
	    if (temp.nesteNode.data.compareTo(e) == 0) {
		temp.nesteNode = temp.nesteNode.nesteNode;
		antall--;
		return true;
	    }
	    temp = temp.nesteNode;
	}
	return false;
    }


	/**
	 * gir antall noder
	 * @return antall noder
	 */
    public int size() {
	return antall;
	//throw new UnsupportedOperationException();
    }
    
    

	/**
	 * Sjekker om det finnes noen noder
	 * Detaljert beskrivelse av hva og hvordan metoden gjør det den gjør.
	 *
	 * @return true med tekst hvis den er tom eller false med tekst hvis det finnes noder
	 */
    public boolean isEmpty() {
	Node temp = forsteNode;
	if(temp == null){
	    System.out.println("denne listen er tom");
	    return true;
	}
	else {
	    System.out.println("Det finnes elementer i denne listen");
	    return false;
	}
	//	throw new UnsupportedOperationException();
    }

	/**
	 * fjerner alle nodene og setter antall til null.
	 */
    public void clear() {
	forsteNode = null;
	antall = 0;
	//throw new UnsupportedOperationException();
    }

	/**
	 * Henter noden med indexen
	 * Sjekker først om index er større en mengden noder eller mindre en null
	 * deretter sjekker den om teller er mindre en index, teller øker og node går videre viden er mindre helt til teller er større en index.
	 *
	 * @param Index nummeret.
	 * @return Dataen til noden på index plass
	 */
    public E get(int index) {
	int teller = 0;
	Node temp = forsteNode;
	if (index >= size() || index < 0) {
	    throw new IndexOutOfBoundsException();
	}
	while (index > teller) {
	    temp = temp.nesteNode;
	    teller++;
	}
	return temp.data;
    }

	/**
	 * legger E til et Array
	 * legger en og en node til et array
	 *
	 * @param Arraynavn
	 * @return Arrayet
	 */
    public E[] toArray(E[] a) {
	Node temp = forsteNode;

	for(int i = 0; i < a.length; i++){
	
	    if(temp == null){
		a[i]= null;
	    }
	    else{
		    
		a[i] = temp.data;
		temp = temp.nesteNode;

	    }
	}
	return a;
    
	//throw new UnsupportedOperationException();
    }	   
	
    public Iterator<E> iterator() {
	return new IteratorList();
    }

    class IteratorList implements Iterator<E> {
	Node temp = forsteNode;
	Node forrige = null;
	int pos = 0;
	boolean bruktIterator = true;
	boolean forste = true;

	/**
	 * Sjekker om det finnes en nesteNode
	 *
	 * @return om det er noe nesteNode
	 */
	public boolean hasNext() {
	    if (pos == 0) {
		return temp != null;
	    } else {
		return temp.nesteNode != null;
	    }
	    
	}
	/**
	 * Kort oppsummering av hva metoden gjør.
	 * Detaljert beskrivelse av hva og hvordan metoden gjør det den gjør.
	 *
	 * @param param1 et parameter som brukes til noe.
	 * @param param2 et parameter som brukes til noe annet.
	 * @throws SomeException hvis noe går galt.
	 * @return noe som ble beregnet ut fra parametrene som ble gitt.
	 */
	public E next() {
	    if(hasNext()){
		
		if (pos == 0) {
		//if(forste){
		    forste = true;
		    if (temp != null) {
			bruktIterator = false;
			pos++;
			return temp.data;
		    } else {
			throw new NoSuchElementException();
		    }
		} else {
		    forste = false;
		    if (temp.nesteNode != null) {
			forrige = temp;
			temp = temp.nesteNode;
			bruktIterator = false;
			pos++;
			return temp.data;
		    } else {
			throw new NoSuchElementException();
		    }
		}
	    }
	    else {
		throw new NoSuchElementException();
	    }
	}
	



	/**
	 * Fjerner den node.
	 * Detaljert beskrivelse av hva og hvordan metoden gjør det den gjør.
	 *
	 */
	public void remove() {
	    if (bruktIterator) {
		throw new IllegalStateException();
	    }
	    
	    //if (pos == 1) {
	    if (forste){
	    	if (forsteNode != null) {
		    forsteNode = forsteNode.nesteNode;
		temp = forsteNode;
		    antall--;
		    pos--;
		    bruktIterator = true;
	    	}
	    	forste = false;
	    } else {
	    	forrige.nesteNode = temp.nesteNode;
	    	temp = forrige;
	    	antall--;
	    	pos--;
	    	bruktIterator = true;
	    }
	    
	}
    }


    class Node {
	Node nesteNode;
	E data;
    }

}

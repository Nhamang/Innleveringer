import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Oblig1 {

    public static void main(String[] argumenter) throws Exception {  
	if (argumenter.length == 0) {
	    System.out.println("INF1010 2012 - Obligatorisk oppgave 1");
	    System.out.println("Bruk:");
	    System.out.println("Kjore testene: java Oblig1 test");
	    System.out.println("Kjore programmet: java Oblig1 program");
	}
	else if (argumenter[0].equals("test")) {
			
	    System.out.println("Editer Oblig1.java og kompiler med Oblig1Test.java for aa kjore testene.");
			
	    //Fjern kommentartegnene for det f√∏lgende for √• kunne kj√∏re testene i Oblig1Test.java:
	    
	    PersonListe personlist = new PersonListe();			
	    Oblig1Test tester = new Oblig1Test(personlist);
	    tester.testivei();
	    
	}
	else if (argumenter[0].equals("program")) {
	    System.out.println("Skriv kode for √• starte programmet her.");
	    PersonListe p = new PersonListe();
	    p.meny();
	}
	else {
	    System.out.println("INF1010 2012 - Obligatorisk oppgave 1"); 
            System.out.println("Bruk:"); 
            System.out.println("Kjore testene: java Oblig1 test"); 
            System.out.println("Kjore programmet: java Oblig1 program"); 
	} 
    }
}

class Person {
    String navn, tlfnr;
    String [] epostadr;
    Person nestePerson;
    Venn venner;
    private int antVenner = 0;

    Person(String navn, String tlfnr, String [] epostadr){
	this.navn = navn;
	this.tlfnr = tlfnr;
	this.epostadr = epostadr;
    }
    public String hentNavn(){
	return navn;
    }

    public String hentTlfnr(){
	return tlfnr;
    }

        
    public String [] hentEpostadr(){
	    return epostadr;
    }

    public boolean nyVenn(Person p){
	if(finnVenn(p.hentNavn())){
	    return false;
	}

	Venn v = new Venn(p);
	if(venner == null){
	    venner = v;
	    antVenner++;
	}
	else {
	    v.next = venner;
	    venner = v;
	    antVenner++;
	}
	return true;
    }

    public boolean fjernVenn(Person p){
	if(!finnVenn(p.hentNavn())){
	    return false;
	}
	Venn forrgie = null;
	Venn naa = venner;

	while (naa != null){
	    if (naa.vennerPerson.hentNavn().equals(p.hentNavn())){

		if(naa == venner) {
		    if(naa.next == null){
			venner = null;
			antVenner--;
			return true;
		    }
		    else {
			venner = naa.next;
			antVenner--;
			return true;
		    }
		}
		else {
		    if(naa.next == null){
			forrgie.next = null;
			antVenner--;
			return true;
		    }
		    
		    forrgie.next = naa.next;
		    antVenner--;
		    return true;
		}
	    }
	    forrgie = naa;
	    naa = naa.next;
	}
	return false;
    }
    public int hentAntall() {
	return antVenner;
    }
    public Person [] hentVenner(){//feil.
	int idx = 0;
	Person [] hentVenn = new Person[hentAntall()];

	Venn temp = venner;

	while(temp != null){
	    hentVenn[idx] = temp.vennerPerson;
	    temp = temp.next;
	    idx++;
	}
	return hentVenn;
    }
    public boolean finnVenn(String navn){
	Venn temp = venner;

	while(temp != null){
	    if(temp.vennerPerson.hentNavn().equals(navn)){
		return true;
	    }
	    temp = temp.next;
	}
	return false;
    }
    public boolean redTlf (String tlfnr){
	this.tlfnr = tlfnr;
	return true;
    }
    
}

class Venn {
    public Person vennerPerson;
    public Venn next;

    public Venn(Person p){
	vennerPerson = p;
    }
    
}

class PersonListe {
    
    private int antallPersoner = 0;
    Person forste;
    Person siste;

    public boolean leggTilPerson(String navn, String tlfnr, String[] epostadr){

	Person p = new Person (navn, tlfnr, epostadr);

	if (hentPerson(navn) != null){
	    return false;
	}
	if (forste == null){
	    forste = p;
	    siste = p;
	    antallPersoner++;
	    return true;
	}
	p.nestePerson = forste;
	forste = p;
	antallPersoner++;

	return true;
    }

    public boolean redTlf(String navn, String tlfnr){

	Person hovedPerson = hentPerson(navn);
	if (hovedPerson == null){
	    return false;
	}
	return hovedPerson.redTlf(tlfnr);
    }
    
    public int hentAntall(){
	return antallPersoner;
    }
    
    public boolean fjernPerson(String navn){
	Person hovedPerson = hentPerson(navn);
	if (hovedPerson == null){
	    return false;
	}

	Person[] vennerArray = hovedPerson.hentVenner();
	for (int i = 0; i<vennerArray.length; i++){
	    vennerArray[i].fjernVenn(hovedPerson);
	}

	Person forrgie = null;
	Person naa = forste;

	while(naa != null){
	    if(naa.hentNavn().equals(navn)){
		if(naa == forste){

		    forste = forste.nestePerson;
		    antallPersoner--;
		    return true;
		}
		else {
		    if (naa.nestePerson == null){
			forrgie.nestePerson = null;
			siste = forrgie;
			antallPersoner--;
			return true;
		    }

		    forrgie.nestePerson = naa.nestePerson;
		    antallPersoner--;
		    return true;
		}
	    }
	    forrgie = naa;
	    naa =  naa.nestePerson;
	}
	return false;
    }
 
    public boolean nyVenn(String navn, String vnavn){
	
	Person hovedPerson = hentPerson(navn);
	Person vennerPerson = hentPerson(vnavn);
	if(hovedPerson == null || vennerPerson == null){
	    return false;
	}
	
	return hovedPerson.nyVenn(vennerPerson) && vennerPerson.nyVenn(hovedPerson);
    }

    public boolean fjernVenn (String navn, String vnavn){
	Person hovedPerson = hentPerson(navn);
	Person vennerPerson = hentPerson(vnavn);
    
	if(hovedPerson == null || vennerPerson == null) {
	    return false;
	}
	return hovedPerson.fjernVenn(vennerPerson) && vennerPerson.fjernVenn(hovedPerson);
    }


    public Person [] hentPersoner(){
    
	Person [] hentP = new Person[antallPersoner];
	if(forste == null){
	    return hentP;
	}
	Person temp = forste;
    
	for (int i = 0; i<hentP.length; i++){
	    hentP[i] = temp;
	    temp = temp.nestePerson;
	}
	return hentP;
    }

    /**
     * Hent et personobjekt
     * @param navn   navnet til personobjektet som skal hentes
     * @return       personobjekt med navnet som skulle hentes
     */
    public Person hentPerson(String navn){
    
	if(forste == null){
	    return null;

	}
	Person temp = forste;
	while(temp!= null){
	    if (temp.hentNavn().equals(navn)){
		return temp;
	    }
	    temp = temp.nestePerson;
	}
	return null;
    }

    void meny() {

	Scanner tast = new Scanner(System.in);
	System.out.println("skriv Hjelp for menyen");
	
	while(true){
	    System.out.println(" ");
	    System.out.print("Ord>");
	    String valg = tast.nextLine();

	    String [] k; 
	    String [] deltMeny = valg.split(" ");
        
	    if(deltMeny[0].equalsIgnoreCase("nyperson")){
		k = new String [deltMeny.length -3];
		for(int i =0; i < k.length; i++){
		    k[i] = deltMeny[i+3];
		}

		leggTilPerson(deltMeny[1], deltMeny[2], k);

		System.out.println("Personens navn:                      "+ deltMeny[1]);
		System.out.println("Telefon nummer:               "+ deltMeny[2]);
		for(int i = 0; i <k.length; i++){
		    System.out.println("Epost:                     "+ deltMeny[i+3]);
		}
	    }
	    else if(deltMeny[0].equalsIgnoreCase("tlf")){
		redTlf (deltMeny[1], deltMeny[2]);
		System.out.println("Personen "+ deltMeny[1] +" sitt tlf er nÂ "+ deltMeny[2]);

	    }

	    else if (deltMeny[0].equalsIgnoreCase("Hjelp")){
		System.out.println("nyperson <navn> <tlfnr> <e-postadr1> <e-postadr2>\n"+
				   "tlf <navn> <tlfnr>\n"+
				   "fjern <navn>\n"+
				   "venner <navn> <vennens navn>\n"+
				   "uvenner <navn> <ikke-vennens navn>\n"+
				   "vis <navn>\n"+
				   "alle\n"+
				   "tilfil <filnavn>\n"+
				   "frafil <filnavn>\n"+
				   "hjelp\n"+
				   "slutt");
	    }





	    else if(deltMeny[0].equalsIgnoreCase("fjern")){
		fjernPerson(deltMeny[1]);

		System.out.println("Ferdig");
		System.out.println("person fjernet:  "+ deltMeny[1]);

	    }




	    else if(deltMeny[0].equalsIgnoreCase("venner")){
		nyVenn(deltMeny[1], deltMeny[2]);
		System.out.println(deltMeny[1] +" og "+ deltMeny[2] +" er naa venner");

	    }





	    else if(deltMeny[0].equalsIgnoreCase("uvenner")){//feil
		fjernVenn (deltMeny[1], deltMeny[2]);
		System.out.println(deltMeny[1] +" og "+ deltMeny[2] +" er ikke lenger venn");

	    }





	    else if(deltMeny[0].equalsIgnoreCase("vis")){

		String navn = deltMeny[1];
		Person p = hentPerson(navn);

		if (p != null){

		    Person [] venner = p.hentVenner();
		    String [] epostadr = p.hentEpostadr();
		    System.out.println("Navn:            "+ p.hentNavn());
		    System.out.println("Tlf:             "+ p.hentTlfnr());
		    for (int j = 0; j < epostadr.length; j++){
			System.out.println("Epostadr:        "+ epostadr[j]);
		    } 
		    System.out.println("Venner(" + venner.length +"):");

		    for (int i = 0; i<venner.length; i++){
			System.out.println(venner[i].hentNavn());
		    }

		}


		else {
		    System.out.println("Person ikke funnet");
		}
	    }







	    else if (deltMeny[0].equalsIgnoreCase("alle")){

		Person [] personer = hentPersoner();

		System.out.println("Alle personer");

		for(int i = 0; i<personer.length; i++){
		    System.out.println(personer[i].hentNavn()); 
		}
	    }


	    else if (deltMeny[0].equalsIgnoreCase("tilfil")){
		
		try {
		    PrintWriter print = new PrintWriter(new File(deltMeny[1]));
		    Person [] personer = hentPersoner();
		    print.println(personer.length);
		    for (Person p:personer){
			print.print("nyperson "+ p.hentNavn() +" "+ p.hentTlfnr() +" ");
			for(int i = 0; i < p.epostadr.length; i++){
			    print.print(p.epostadr[i] +" ");
			}
			print.println();
		    }

		    print.println();

		    for (Person p: personer){

			Person[] venner = p.hentVenner();
			for (Person p2: venner){
			    print.println("Venner "+ p.hentNavn() +" "+ p2.hentNavn());
			}
		    }
		    print.close();
		}
		catch (FileNotFoundException e){
		    System.out.println("En feil skjedde");
		}
	    }
		
		
	    else if (deltMeny[0].equalsIgnoreCase("frafil")){//feil

		try {
		    Scanner fra = new Scanner (new File(deltMeny[1]));

		    Person [] person = hentPersoner();
		    String tekst = fra.nextLine();
		    int idx = Integer.parseInt(tekst);

		    for (int i = 0; i< idx; i++){
			String[] nypers = fra.nextLine().split(" ");
			String navn = nypers[1];
			String tlfnr = nypers[2];
			String [] epost = new String [nypers.length-3];
			int ant = 0;
			if(nypers.length > 3){
			    for (int j = 3; j < nypers.length; j++){
				epost[j-3] = nypers[j];
			    }
			}

			
			leggTilPerson(navn, tlfnr, epost);
		    }

		    fra.nextLine();
		    while(fra.hasNext()){
			String[] nyVenner = fra.nextLine().split(" ");
			String navn = nyVenner[1];
			String vnavn = nyVenner [2];
			nyVenn(navn, vnavn);
		    }
		    System.out.println("ferdig");
	       
		    
		}
		catch (FileNotFoundException e) {
		    System.out.println("finner ikke filen");
		}
		
	    }
	    else if(deltMeny[0].equalsIgnoreCase("slutt")){
		System.out.println("avlutter");
		break;
	    }
	    else {
		    System.out.println("Feil. Pr¯v pÂ nytt");
		}
	}
    }
}
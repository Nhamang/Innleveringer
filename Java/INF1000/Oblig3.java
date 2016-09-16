/*Antok i oppg 4. at personen ikke fikk tilbake noe av saldoen sin, Mener ikke deloppgavene manglet noe. Har ingen sporsmal.
 */

import easyIO.*;
import java.io.*;
public class Oblig3 {
    public static void main(String[] agrs){
	hybelhus h = new hybelhus();
	System.out.println("********Velkommen til Graa's Hybler*******");
	h.ordrelokke();
	h.tomSkjerm();
	System.out.println("Takk for at du Brukte dette programmet");
	
    }
}//slutt klassen Oblig3


class student {//Starter klassen student som blir kalt frem i classen hybel.
    String navn;
    int saldo;
}//slutt klassen student

class hybel {//Starter klassenn hybel som senere blir brukt som array.
    student leietager;
    int husleie;
}//slutt klassen hybel

class hybelhus {
    In tast = new In();
    Out skjerm = new Out();
    final String filnavn = "hybeldata.txt";//leger en konstant for hybeldata s[ den blir skrevet riktig hver gang den brukes
    final String tomhybel = "TOM HYBEL";
    hybel [][] hybler = new hybel [3][6];//lager en array av klassen hyyybel
	
    final int depo = 10000;
    int mnd;
    int ar;
    int totmnd;
    int fortjen;

    hybelhus() {//De neste linjene leser en fil og tilforer navn, hybel og saldo til riktig arrayslot.
	In infil = new In(filnavn);
	mnd = infil.inInt(" ;");//semikoloen sier hvor i teksten den skal skille mellom mnd,ar osv.
	ar = infil.inInt(" ;");
	fortjen = infil.inInt(" ;");
	totmnd = infil.inInt("\n ");

	while (!infil.endOfFile()){

	    int etg = infil.inInt(" ;")-1;
	    int bokstav = infil.inChar(" ;");
	    int rom = (int) (bokstav-'A');//bruker ASCII systemet for å gjøre en bokstav til et tall, funker også andre vei
	    int saldo = infil.inInt(" ;");
	    String navn = infil.inLine();
	    hybler[etg][rom] = new hybel();
	    hybler[etg][rom].leietager = new student();
	    hybler[etg][rom].leietager.navn = navn;
	    hybler[etg][rom].leietager.saldo = saldo;

	}
	infil.close();// lukker filen
    	    

	for (int etg = 0; etg < 3; etg++){//Linjene angir leie til hyblene og skiller mellom 1-2 og 3 etg.
	    for(int rom = 0; rom < 6; rom++){
		if(etg != 2){
		    hybler[etg][rom].husleie = 6000;
		}
		else {
		    hybler[etg][rom].husleie = 7000;
		}
	    }
	}
    }

    void ordrelokke(){
	int ordre = 0;
	while (ordre !=7){

	    System.out.println("1: Skriv ut oversikt");
	    System.out.println("2: Registrer ny leietager");
	    System.out.println("3: Registrer betaling fra leietager");
	    System.out.println("4: Registrer frivillig utflytting");
	    System.out.println("5: MÃ¥nedskjÃ¸ring av husleie");
	    System.out.println("6: Kast ut leietagere");
	    System.out.println("7: Avslutt");
	    System.out.print("Hva onsker du aa utfore: ");
	    ordre = tast.inInt();
   
	switch (ordre){
	case 1: skrivOversikt(); break;
	case 2: registrerNyLeietager(); break;
	case 3: registrerBetaling(); break;
	case 4: registrerUtflytning(); break;
	case 5: manedskjoring(); break;
	case 6: kastUt(); break;
	case 7: avslutt(); break;
	default:
	    System.out.println("Velg mellom 1 og 7");
	}}}

    void skrivOversikt() {
	String navn;
	System.out.println("Hybel             Leietager                      Saldo");
	System.out.println("-------           -------------------            ------------");

	for(int etg = 0; etg < 3; etg++){
	    for(int rom = 0; rom < 6; rom++){
		char bokstav = (char) (rom + 'A');
		skjerm.out((etg+1) +""+ bokstav, 20);

		if(!hybler[etg][rom].leietager.navn.equals(tomhybel))//sjekker om navnet er tom hybel og hvis det ikker s[ printer den ut navnet.
		    navn = hybler[etg][rom].leietager.navn;
		else 
		    navn = " ( LEDIG ) ";

		skjerm.out(navn, 30);
		System.out.println(hybler[etg][rom].leietager.saldo);
	    }
	}
		System.out.println("mnd/ar, og driftstid:     "+ mnd +"/"+ ar +",  "+ totmnd +" mnd i drift.");
		System.out.println("Fortjeneste:   "+ fortjen +"kr.");
	    
	
    }

    void registrerNyLeietager(){
	tomSkjerm();
	boolean answ = false;
	for(int etg = 0; etg < 3; etg++){
	    for(int rom = 0; rom < 6; rom++){
		char bokstav = (char)(rom + 'A');
		if (hybler[etg][rom].leietager.navn.equals(tomhybel)){//Sjekker om det er noen ledige hybler og printer ut hvilkene.
		    System.out.println((etg + 1) +""+ bokstav +" er ledig");
		    answ = true;
		}
	    }
	}
	if(!answ) {//hvis det ikke er noen ledige hybler gir den mld og returnerer til valg.
	    System.out.println("Det er ingen ledige hybler");
	    return;
	}
	System.out.println("\nRegistrer ny leietager ");
	System.out.println("-------------------------");
	System.out.print("Velg etg. fra 1-3: ");
	int etg = tast.inInt("\r\n")-1;
	System.out.print("velg et rom A-F: ");
	String sBokstav = tast.inWord("\r\n").toUpperCase();

	char bokstav = sBokstav.charAt(0);
	int rom = (int) (bokstav - 'A');//gjor om fra bokstav til tall for arrayen

	System.out.print("Navn: ");
	String navn = tast.inWord("\r\n");

	student s = hybler[etg][rom].leietager;
	if(s.navn.equals(tomhybel)){//sjekker om hybelen er ledig.
	    s.navn = navn;
	    s.saldo = depo - hybler[etg][rom].husleie;
	    fortjen += hybler[etg][rom].husleie;
	    tomSkjerm();
	    System.out.print("Hybel: "+ (etg+1) +""+ bokstav +"   ");
	    System.out.print("Navn: "+ navn +"   ");
	    System.out.println("Saldo: "+ s.saldo);
	    System.out.println("Leietager er na registrert");
	}
	else {
	    System.out.println("Denne hybelen er opptatt");
	}
    }




    void registrerBetaling(){
	tomSkjerm();
	boolean answ = false;
	System.out.println("Registrer betaling");
	System.out.println("Hybletg. 1-3: ");
	int etg = tast.inInt("\n\r")-1;
	System.out.println("leilighet (A-F): ");
	String shybelbokstav = tast.inWord("\n\r").toUpperCase();

	char hybelbokstav = shybelbokstav.charAt(0);
	int b =  (int)(hybelbokstav - 'A');//gjjjjjor om fra bokstav til tall

	System.out.print("Belop: ");
	int belop = tast.inInt("\n\r");
	System.out.println("Er du sikker pa at det er riktig hybel Ja/Nei");
	String svar = tast.inWord();

	if(svar.equalsIgnoreCase("nei")){
	    answ = true;
	    System.out.println("-------");
	}
	else if(!hybler[etg][b].leietager.navn.equals(tomhybel)){
	    answ = true;
	    hybler[etg][b].leietager.saldo += belop;
	    System.out.println("Registrert!");
	}
	else {
	    System.out.println("Ingen leier dette rommet");
	}
    }

    void registrerUtflytning(){
	tomSkjerm();
	boolean answ = false;
	System.out.println("Hvem vil flytte ut");
	System.out.print("NAVN: ");
	String navn = tast.inWord("\n\r").toUpperCase();

	for (int etg = 0; etg < 3; etg++){
	    for (int rom = 0; rom < 6; rom++){

		student s = hybler[etg][rom].leietager;

		if (s.navn.equalsIgnoreCase(navn)){//tester om navnet eksisterer i systemet.
		   	
		    String navn2 = s.navn;
		    if(s.saldo > 0){//sjekker om personen skylder   penger.
			s.navn = tomhybel;
			int midlertid = s.saldo;
			s.saldo = 0;
			char bokstav = (char)(rom + 'A');//gjor om fra tall til bokstav
			System.out.println(navn +" har na flyttet ut av "+ (etg+1) +"" + bokstav +" og fÃ¥tt tilbake "+ midlertid +"kr");
			answ = true;//gjor answ om til true.
		    }

		    else {
			char bokstav = (char)(rom + 'A');
			System.out.println(navn +" Far ikke lov til og flytte ut av "+ (etg+1) +""+ bokstav);
		    }

		   
		}
		  
	    }
	}
	if (!answ){//gir mld hvis personen ikke bor der
	    System.out.println("Denne personen bor ikke her");
		}
    }




    void manedskjoring(){
	int utgift = 0;
	int inntekt = 0;

	System.out.println("Maandeskjoring av husleie ");
	System.out.println("**************************");
	System.out.println("Onsker du aa utfore mnd. kjoring for "+ mnd +"/"+ ar +" (JA/NEI)");
	String svar = tast.inWord("\n\r");

	if (svar.equalsIgnoreCase("ja")){
	    mnd++;
	    totmnd++;
	    if(mnd == 13){//gjor om 13 mnd til nytt aar
		mnd = 1;
		ar++;
	    }
	    for (int etg = 0; etg < 3; etg++){
		utgift = utgift + 2100;//leger til 2100 til utgiftene for alle etg
		for (int rom = 0; rom < 6; rom++) {
		    utgift = utgift + 1400;//leger til 1400 til utgifter for alle rom

		    student s = hybler[etg][rom].leietager;
		    if(!s.navn.equals(tomhybel)){
			int b = hybler[etg][rom].husleie;
			s.saldo -= b;
			if (s.saldo >= 0){
			    inntekt += b;
			}
			else {
			    if ((s.saldo + b) > 0){
				inntekt += (b + s.saldo);
			    }
			}
		    }
		}
	    }
	    int mndfor = inntekt - utgift;
	    fortjen += mndfor;
	    int snitt = fortjen / totmnd;
	    tomSkjerm();
	    System.out.println("***************************************");
	    System.out.println("mnd/ar og driftstid: "+ mnd +"/"+ ar +" og "+ totmnd +" mnd i drift");
	    System.out.println("Fortjenesten denne mnd "+ mndfor);
	    System.out.println("Totalfortjeneste: "+ fortjen);
	    System.out.println("Gjennomsnitt fortjeneste denne mnd: "+ snitt);

	}
    }

    void kastUt(){
	tomSkjerm();
	boolean sjekk = false;
	System.out.println("kast ut leietager");

	for(int etg = 0; etg < 3; etg++){
	    for(int rom = 0; rom < 6; rom++){

		student s = hybler[etg][rom].leietager;

		if(!s.navn.equals(tomhybel) && s.saldo < -hybler[etg][rom].husleie){//sjekker om hybelen er ledig og hvis det bor noen der om de skylder mer en een mnd leie.

		    int temp = -s.saldo;//gjor om gjeeelden til et posetivt tall
		    int krav = temp + 3000;
		    temp += (3000 / 2);
		    fortjen += temp;
		    sjekk = true;//hvis none blir kastet ut blir boolean om til true

		    tilkallHole (etg, rom, krav);
		}
	    }
	}
	if(!sjekk){//hvis boolean ikke er blitt true kj;rer if setningen.
	    System.out.println("ingen har blitt kastet ut");
	     
	    
	}
    }

    void tilkallHole (int etg, int rom, int krav) {
	student s = hybler[etg][rom].leietager;
	Out utfil = new Out("Hole.txt", true);//skaper filen hole.txt

	char bokstav = (char) (rom + 'A');//gjor om fra tall til bokstav.

	utfil.outln(s.navn +" har blitt kastet ut fra "+ (etg+1) +""+ bokstav +" og ma betale "+ krav +" kroner i krav");

	System.out.println(s.navn +" har blitt kastet ut fra "+ (etg+1) +"" + bokstav +" og maa betale "+ krav +" kroner");
	
	s.navn = tomhybel;// setter navnet til personen som ble kastet ut tilbake til tomhybel
	s.saldo = 0;// setter saldoen tilbake til 0.

	utfil.close();//lukker filen hole.txt
    }




    void avslutt(){

	String navn;
	int saldo;

	Out utfil = new Out(filnavn);//lagrer info til filen hybeldata.txt
	utfil.outln(mnd +";"+ ar +";"+ fortjen +";" + totmnd);

	for(int etg = 0; etg < 3; etg++){
	    for(int rom = 0; rom < 6; rom++){

		student s = hybler[etg][rom].leietager;
		char bokstav = (char) (rom + 'A');

		if (s.navn.equals(tomhybel)){
		    navn = tomhybel;
		    saldo = 0;
		}
		else {
		    navn = s.navn;
		    saldo = s.saldo;
		}
		utfil.outln((etg+1) +";"+ bokstav +";"+ saldo +";"+ navn);
	    }
	}
	utfil.close();
    }


    void tomSkjerm (){//lager 50 tomme linjer, som kan brukes igjennom programmet.
	for (int i = 0; i < 50; i++){
	    System.out.println(" ");
	}

    }
}//slutt klassen

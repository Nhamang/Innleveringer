import easyIO.*;

class Oblig2 {
    public static void main(String [] agrs){
	System.out.println("Velkommen til felt oversikt");
	
	Olje ol = new Olje(); //skaper ny klasse av Olje
	ol.ordrelokke();//starter på Olje sin ordrelokke
    }
}
class Olje
{
    final int radAntall = 1;
    final int kolAntall = 1;

	
    String eier [][] = new String [radAntall][kolAntall];
    double utvunnet [][] = new double[radAntall][kolAntall];
	
    String firmanavn;

    int rad = 0; int kolnr = 0;

    In tast = new In();

    void ordrelokke()
    {
	int ordre = 0;
	while (ordre != 6) {
	    System.out.println("1: Kjop et felt.");	
	    System.out.println("2: Annuller et kjop");
	    System.out.println("3: Lag et oversiktskart.");
	    System.out.println("4: Oppdater oljeutvinning.");
	    System.out.println("5: Finn summen av oljeutvinning.");
	    System.out.println("6: Avslutt.");
	    System.out.println("Hva er din ordre");

	    ordre = tast.inInt();
		
	    switch (ordre) {//bruker switch til å velge metode
	    case 1: kjopEtFelt(); break;
	    case 2: annullerEtKjop(); break;
	    case 3: lagKart(); break;
	    case 4: oppdaterOljeutvinning(); break;
	    case 5: finnSum(); break;
	    case 6: avslutt(); break;
	    default: break;
	    }
	}
    }
	

    boolean ledigFelt() {//sjekker om feltet er ledig
	for (rad  = 0; rad < radAntall; rad++){
	    for (kolnr = 0; kolnr < kolAntall; kolnr++){
		if (eier[rad][kolnr] == null){		
		    return true;
		}
	   
	    }
	} 
	return false;
	}

    void kjopEtFelt() {
	tomSkjerm();//bruker tom skjerm som er en metode i bunn av koden
	if (ledigFelt() == true);
	System.out.println("disse feltene er ledige:");
	int teller = 0;
	    for (rad = 0; rad < radAntall; rad++){
		for (kolnr = 0; kolnr < kolAntall; kolnr++){
		    if (teller%7 == 0)
		    System.out.println(" ");
	
		    if (eier[rad][kolnr] == null) {
		    
			System.out.print("(" + (rad + 1) + " - "+ (kolnr + 1) +")");
			teller++;
		    }
		
		    }
		/*if (ledigFelt() == false);
		  System.out.println("det er ingen ledige felt");*/
	    }
    
	System.out.print("\nHvilket felt vil du kjope, separer med bindestrek");
	System.out.println(" 1-8, 1-20");
	System.out.println("Skriv et høyere tall for aa avslutte.");
	rad = tast.inInt("-") - 1;
	kolnr = tast.inInt("-") - 1;
	if (rad >= radAntall || kolnr >= kolAntall) {

	    System.out.println("Ugyldig felt");
	}
	else if (eier [rad][kolnr] != null){
	    System.out.println("dette feltet er opptatt");
	}
	else {
	    System.out.println("Hvilket selvskap vil kjÃ¸pe feltet");
	    eier[rad][kolnr] = tast.inLine();
	    tomSkjerm();	    
	    System.out.println("feltet "+ (rad + 1) +" - "+(kolnr + 1) +" er nÃ¥ solgt til "+ eier[rad][kolnr]);
	}
	    }
    void annullerEtKjop() {
	tomSkjerm();
	System.out.println("Hvilket selvskap skal annullere et felt");
	firmanavn = tast.inLine();

	System.out.println("Hvilket felt vil "+ firmanavn +" annullere");
	System.out.println("1 - 8, 1 - 20. separer med bindestrek");
	rad = tast.inInt("-") - 1;
	kolnr = tast.inInt("-") - 1;
	if (rad >= radAntall || kolnr >= kolAntall){
	    System.out.println("felt ugyldig avslutter annullering");
	}
	else if (eier[rad][kolnr] == null) {
	    System.out.println("Feltet er ikke eid av noen selvskap");
	}
	else if (eier[rad][kolnr].equals(firmanavn)){
	    eier[rad][kolnr] = null;
	    tomSkjerm();
	    System.out.println(firmanavn +" eier ikke "+ (rad + 1) +"-"+ (kolnr + 1) +" lenger");
	}
	else  {	
	    tomSkjerm();
	    System.out.println("Firmanavn og Felt matcher ikke");
	}
    }
	
    void lagKart() {//printer ut et kart over felt
	tomSkjerm();
	System.out.println("             Felt Kart");
	System.out.println("   1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20");
	for (rad = 0; rad < radAntall; rad++){
	    System.out.print((rad + 1));
	    System.out.print("]");
	    for (kolnr = 0; kolnr < kolAntall; kolnr++){
		if (eier[rad][kolnr] == null) {
		    System.out.print(" - ");}
		else { 
		    System.out.print(" X ");
		}
	    }
	    System.out.println("[]");
	}
    } 



    void oppdaterOljeutvinning() {
	tomSkjerm();	
	System.out.println("Hvilket felt skal oppdateres");
	rad = tast.inInt("-") - 1; kolnr = tast.inInt("-") - 1;
	if (rad >= radAntall || kolnr >= radAntall){
	    System.out.println("Ugyldig felt");
	}
	else if (eier[rad][kolnr] != null){
	    System.out.println("Dette feltet er eid av "+ eier[rad][kolnr]);
	    System.out.println("Hvor mye har "+ eier[rad][kolnr] +" unvunnet fra felt "+(rad + 1) +"-"+ (kolnr + 1));    
	    System.out.println("Hvis dette er feil tast 0");
		utvunnet[rad][kolnr] += tast.inDouble();
		tomSkjerm();	    
		System.out.println("Selvskapet "+ eier[rad][kolnr] +" har nå utvunnet "+ utvunnet[rad][kolnr] +" fat i feltet "+(rad + 1) +"-"+ (kolnr + 1));
		}
	else {
	    System.out.println("Det er ingen selvskap som eier dette feltet");
	}
    }
 




    void finnSum(){
    int sum = 0;
    for (rad = 0; rad < radAntall; rad++){
	for (kolnr = 0; kolnr < kolAntall; kolnr++){
	    sum += utvunnet[rad][kolnr];
		
	}
    }
    System.out.println("Summen av unvunnet fat er "+ sum);
    }
    

    void avslutt() {
	tomSkjerm();
    System.out.println("Takk for at du brukte dette programmet");	    
    }

    
void tomSkjerm() {//skriver ut 50 tomme linjer for en regn og ryddig skjerm.
    Out skjerm = new Out();
    for (int linje = 0; linje < 50; linje++) {
	skjerm.outln(" ");
    }
}
}
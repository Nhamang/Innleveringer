import easyIO.*;// Bibliotek lagd av UiO
import java.util.*;//henter inn hashmap
class Oblig4{
    public static void main (String[] agrs){
	System.out.println("Velkommen til Nicklas's filmregister");
	register r = new register();
	r.ordrelokke();
	System.out.println("Takk for att du brukte Nicklas's filmregister");
    }
}
//lager en person klasse
class person {
    String kode;
    String navn;

    String filmerRegissert;
    String filmerSplit;
}
//lager en film klasse
class film {
    String kode;
    String tittel;
    String regissor;
    int aar;
    String skuespiller;
    String sjanger;
    String extra;
}

class Tiaar {
    String b;
    String aar2;
    String tittel2;
}

class register {
    In tast = new In();

    HashMap<String, person> personer = new HashMap<String, person>();
    HashMap<String, film> filmer = new HashMap<String, film>();
    HashMap<String, TiAar> tiAar = new HashMap<String, TiAar>();
    String[]tiaar = new String[14];

    register () {
	In fil = new In("persondata.txt");
	fil.inLine();
	while (!fil.endOfFile()) {
	    String skode = fil.inWord();
	    String navn = fil.inLine();

	    person p = new person();
	    p.kode = skode;
	    p.navn = navn;
	    personer.put(skode, p);
	}
	fil.close();

	fil = new In("filmdata.txt");
	fil.inLine();
	while (!fil.endOfFile()) {

	    String linje = fil.inLine();
	    String[] felt = linje.split("\t");

	   
	    String filmKode = felt[0];
	    String filmTittel = felt[1];
	    int filmAar = Integer.parseInt(felt[2]);
	    String filmRegissor = felt[3];
	    String filmSkuespiller = felt[4];
	    String filmSjanger = felt[5];
	    String filmExtra = null;
	    if (felt.length > 6){
		filmExtra = felt[6];
	    }
	    
	    film f = new film();
	    f.kode = filmKode;
	    f.tittel = filmTittel;
	    f.aar = filmAar;
	    f.regissor = filmRegissor;
	    f.skuespiller = filmSkuespiller;
	    f.sjanger = filmSjanger;
	    f.extra = filmExtra;
	    filmer.put(filmKode, f);

	}
	fil.close();
    }

    void ordrelokke() {
	String ordre = "";
	ordre = tast.readLine();
	char char0 = 'm';
	meny();
	X();
	while (!ordre.equals("q")) {

	    System.out.print("Kommando ('m' = meny): ");
	    ordre = tast.readLine();
	
	    int ordreLengde = ordre.trim().length();
	    if (ordreLengde > 0) {
		char0 = ordre.charAt(0);
	    }
	    if (ordreLengde == 1 && char0 == 'm') {
		meny();
	    }
	    else if (ordreLengde == 1 && char0 == 's'){
		statistikk(ordre);
	    }
	    else if (ordreLengde >= 4 && (char0 >= 'A' && char0 <= 'Z')){
		infoFilm(ordre);
	    }
	    else if (ordreLengde >= 3 && ordre.charAt(0) >= 'A' && ordre.charAt(0) <= 'Z'){
		finnFilm(ordre);
	    }
	    else if (ordreLengde >= 4 && ordre.charAt(0) >= 'a' && ordre.charAt(0) <= 'z') {
		infoPerson(ordre);
	    }
	    else if (ordreLengde >= 3 && ordre.charAt(0) >= 'a' && ordre.charAt(0) <= 'z') {
		finnPerson(ordre);
	    }
	    else if (ordreLengde == 5 && ordre.matches("[0-9][0-9][0-9]0s") && ordre.endsWith("s")){
	      visTiaar(ordre);
	    }
	}
    }

    
    void meny(){
	tomSkjerm();
	System.out.println("m      = Vis denne menyen\n"+
			   "s      = Vis statistikk\n"+
			   "Aaa1   = Vis info om en film\n"+
			   "Aaa    = Finn film\n"+
			   "tom1   = Vis info om en person\n"+
			   "tom    = Finn person\n"+
			   "2000s  = Vis info om et tiår\n"+
			   //"2010   = [ Ekstraoppgave: Vis info om et aar ]\n"+
			   //":a     = [ Ekstraoppgave: Vis info om en sjanger ]\n"+
			   "q      = Avslutt\n");
	
}
    void X(){
	Iterator i = filmer.keySet().iterator();
	while(i.hasNext()){
	    
	    String x = (String)i.next();
	    int z = filmer.get(x).aar;
	    
	    z = z/10;
	    z = z -188;
	    
	    tiaar[z] = tiaar[z] + x + ";";
	}
    }

    void statistikk(String ordre){

	for (int i = 1; i < tiaar.length; i++){
	    System.out.println("Tiaaret "+ ((i + 188) * 10) +" hadde "+ tiaar[i].split(";").length +" filmer");
	}
	    System.out.println("*****************Statistikk******************");
	    System.out.println("***Det er totalt "+ filmer.size() +"filmer***");
	    System.out.println("*********************************************");
    }
 


    
    void infoFilm (String ordre) {
    	tomSkjerm();
    	System.out.println("Info om en film");
	boolean answ = false;
	for (film f:filmer.values()){
	    if (f.kode.contains(ordre)){
		    answ = true;
	    }
	}
	if (!answ){
	    System.out.println("Ikke funnet");
	    return;
	}
	
	System.out.println("Tittel: "+ filmer.get(ordre).tittel);
	System.out.println("Aar: "+ filmer.get(ordre).aar);
	System.out.println("Sjanger: "+ filmer.get(ordre).sjanger);
	String regi = filmer.get(ordre).regissor;
	System.out.println("Regissor: "+ personer.get(regi).navn);
	String skue = filmer.get(ordre).skuespiller;
	String[] actor = skue.split(";");

	for (int i = 0; i < actor.length; i++){
	    if (personer.containsKey(actor[i])){
		System.out.println(personer.get(actor[i]).navn);
	    }
	    else{
		System.out.println(false);
	    }
	}
    }

			       
	


    
    void finnFilm (String ordre) {
	System.out.println("finnFilm");
	boolean answ = false;
	for (int i = 1; filmer.containsKey(ordre + i); i++){
	    answ = true;
	    System.out.println(filmer.get(ordre + i).kode +" "+ filmer.get(ordre + i).tittel);
	}
	if (!answ){
	    System.out.println("Fant ikke");
	    }
    }
    
    void infoPerson (String ordre){
	tomSkjerm();
	System.out.println("Info om en person");
	boolean answ = false;
	for(person p:personer.values()){
	    if (p.kode.contains(ordre)){
		answ = true;
	    }
	}
	if (!answ){
	    System.out.println("Ikke Funnet");
	}
	System.out.println("Navn: "+ personer.get(ordre).navn);

	for(film f:filmer.values()){

	    if (f.skuespiller.contains(ordre)){
		System.out.println("Spilte i "+ f.tittel +" i "+ f.aar);
	    }
	    if (f.regissor.contains(ordre)){
		System.out.println("regissorte "+ f.regissor +" i "+ f.aar);
	    }
	}
	    System.out.println("Navn: "+ personer.get(ordre).navn);
	    
    }
    
	void finnPerson (String ordre){
	    boolean answ = false;
	    System.out.println("finnPerson");
	    for (int i = 1; personer.containsKey(ordre + i); i++){
		answ = true;
		System.out.println(personer.get(ordre + i).kode +" "+ personer.get(ordre + i).navn);
	    }
	    if(!answ){
		System.out.println("Fant ingen");
		}
	}
    
	void visTiaar (String ordre){
	    System.out.println("Tiaar");
	    String k = ordre;
	    String kom = ordre.substring(0, 3);
	    System.out.println("De beste filmene fra "+ kom +"-tallet");

	    for (int i = 0; i < 10; i++){
	    } 
	}
    void tomSkjerm (){
	int i;
	i=0;
	while (i <= 50){
	    System.out.println("");
	    i++;
	}
    }
}
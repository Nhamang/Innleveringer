package inf1010.assignment;


public class Person implements comperable<Person> {
    
String name;
String email;
String userName;

/**
 * Sjekker om denne personen er lik den samme som den annen.
 *
 * @param Den personen som skal sammenlignes
 * @return et nummer.
 */
public int compareTo(Person p){
	return name.compareTo(p.getName());
	
}

/**
 * leveren navnet på denne personen.
 *
 * @return personens navn.
 */
public String getName(){
	return name;
}


/**
 * Gir denne personens epost.
 *
 * @return Eposten til denne personen
 */
public String getEmail(){
	return email;
}


/**
 * Gir denne personens brukernavn.
 *
 * @return brukernavnet til denne personen
 * */
public String getUsername(){
	return userName;

}

}

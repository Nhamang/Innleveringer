package inf1010.assignment;


public class Group implements Comparable<Group> {

private IfiCollection<Student> students = new IfiCollection<Student>();
	
private int number;
TeachingAssistant teachingAssistant;


private Subject subject;


int compareTo(Group other){
	return number - other.number;
}


/**
 * Legger til en elev
 *
 * @param Eleven som skal legges til
 * @return legger til en elev.
 */
public boolean addStudent(Student s){
	return students.add(s);

}


/**
 * Gir nummeret til denne gruppen
 * 
 * @return gruppenummeret
 */
public int getNumber(){
	return number;
}


/**
 * Gir elev mengden
 * 
 * @return Mengden elever i denne gruppen.
 */
public int getStudentBodySize(){
    return students.size();
}


/**
 * putter elevene til denne gruppen i et array
 * går igjennom alle elevene til denne gruppen i et array
 *
 * @return Arrayet
 */
public Student [] getStudents(){
	Student [] gruppeStudenter = new Student[students.size];
	int teller = 0;
	for(Student s : students){
		studentGruppe[teller] = s;
		teller++;
	}
	return gruppeStudenter;

}

/**
 * gir emne
 * 
 * @return emne
 */
public Subject getSubject(){
	return subject;
	
}


/**
 * gir lærerassistenten
 * 
 * @return Lærerassistenten
 */
public TeachingAssistant getTeachingAssistant(){
	return teachingAssistent;
}

}

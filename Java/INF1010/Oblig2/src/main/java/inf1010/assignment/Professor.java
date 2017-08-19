package inf1010.assignment;



public class Professor extends person implements Teacher{
    
    Subject subject;

    /**
     * Leverer de eleven som denne læreren har
     *
     * @return studenter som har denne læreren.
     */
public Student [] getStudent(){
	return subject.getStudents();
	
}
/**
 * Henter enmet til læreren.
 *
 * @return Det emnet denne læreren har
 */
public Subject getSubject(){
	return subject;
	
}
	

}

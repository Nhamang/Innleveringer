package inf1010.assignment;
import java.util.*;

import javax.swing.GroupLayout.Group;
public class Subject implements Comparable<Subject> {
	private IfiCollection<Group> groups = new IfiCollection<Group>(); 
	
	private Teacher lecturer;
	Subject subject;
	private String courseCode;
	
	/**
	 * Legger til en elev til denne gruppen
	 *
	 * @param Studenten som skal legges til
	 * @param  gruppe nummeret
	 * @return legger til en elev til denne gruppen
	 */
	boolean enrollStudent(Student s, int group){
		Group g = groups.get((group-1));
		
				return g.addStudents(s);
		
	}
	/**
	 * Sammenligner denne studenten med en annen
	 *
	 * @param eleven som skal sammenlignes
	 * @return et tall
	 */
	int compareTo(subject s){
		return courseCode.compareTo(s.courseCode);
	}
	/**
	 * gir kurskoden
	 * 
	 * @return kurskoden
	 */
	String getCourseCode(){
		return courseCode;
	}
	/**
	 * gir Hovedlæreren til dette emnet
	 * @return hovedlæreren til dette emnet
	 */
	Teacher getLecturer(){
		return lecturer;
	}
	
	
	/**
	 * henter mengden elever i dettet emnet
	 * går igjennom alle gruppene til dette emnet og legger til studentBodySize til en teller.
	 *
	 * @return Telleren
	 */
	int getStudentBodySize(){
		int teller = 0;
		
		for(Group g: groups){
			teller += g.getStudentBodySize;
		}
		return teller;
		
	}
	/**
	 * Lager et array med alle studenten til dette emnet.
	 * Itererer Group så går igjennom det og putter elev dataen i et array.
	 *
	 * @return Arrayet
	 */
	Student [] getStudents(){
		Student [] studSub = new Student [getStudentBodySize];
		
		
		Iterator<Group> iter = groups.iterator();
		Student[] temp = iter.next().getStudents();
		while(iter.hasNext()){
			
			for(int p = 0; p < studSub.length; p++)
				studSub[i++] = temp[t];
		}
		return studSub;
	}
	/**
	 * gir emnet
	 * @return Emnet
	 */
	Subject getSubject(){
		return subject;
	}
	/**
	 * Henter lærerene til dette faget
	 * itererer Gruop og går igjennom den å legger infoen til et array.
	 *
	 * @return Arrayet
	 */
	Teacher [] getTeachers(){
		int i = 0;
		Teacher [] teaSub = new Teacher [i];
		
		
		Iterator<Group> iter = groups.iterator();
	
		while(iter.hasNext()){
				teaSub[i] = it.next().getTeachingAssistant();
				i++;
		}
		
		teasub[i] = lecturer;
		return teaSub;
		
	}
	


}

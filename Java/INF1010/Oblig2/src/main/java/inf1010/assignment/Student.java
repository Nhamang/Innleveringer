package inf1010.assignment;



class Student extends Person{
    IfiCollection<Group> groups = new IfiCollection<Group>();
    
	/**
	 * Putter elevents grupper i et array.
	 * Går igjennom alle gruppene til denne personen å putter dem i et array
	 *
	 * @return Array med gruppene
	 */ 
Group [] getGroups(){
	Group [] studentGruppe = new group[groups.size()];
	int teller = 0;
	for(Group g : groups){
		studentGruppe[teller] = g;
		teller++;
	}
	return studentGruppe;
}

/**
	 * Putter elevens lærere i et array.
	 * Går igjennom alle lærerene til denne personen å putter dem i et array
	 *
	 * @return Array med lærerene
 */
Teacher [] getTeachers(){
	
	Teacher [] studTea = new Teacher [groups.size()];
	int t = 0;
	while(t<studentGruppe.length){
		studTea[t] = studentGruppe[i].getTeachingAssistant();
		t++;
	}
	return studTea;
}

/**
	 * Putter elevens emner i et array.
	 * Går igjennom alle emnene til denne personen å putter dem i et array
	 *
	 * @return Array med emnene
 */
Subject [] getSubjects(){
	Subject [] studEmne = new Subject [groups.size()];
	int i = 0;
	while(i<studentGruppe.length){
		studEmne[i] = studentGruppe[i].getSubject();
		i++;
	}
	return studEmne;
}

}

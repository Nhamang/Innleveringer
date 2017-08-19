package inf1010.assignment;


class TeachingAssistant extends Student implements Teacher {
	private Group group;
	
	/**
	 * henter gruppen som denne personen er læreren til.
	 *
	 * @return Gruppen til denne læreren 
	 */
	boolean getGroup(){
		return group;
	}
	/**
	 * leverer denne læreren
	 * @return Studenten som har denne læreren
	 */
	boolean getStudent(){
		return group.getStudents();
	}
	/**
	 * leverer et emne.
	 *
	 * @return Emnet
	 */
	boolean getSubject(){
		return group.getSubject();
		
	}

}
    



package inf1010.assignment;

import inf1010.lib.two.IfiCollection;
import inf1010.lib.two.ShellBase;

public class Shell extends shellBase{
	
	public static void main(String[] args) {
		//Shell s = new Shell();
		//s.createTestDatabase();
		//s.inputLoop();
	}

	public Shell() {
	    IfiCollection<Subject> subjects = new IfiCollection<Subject>();
	    IfiCollection<Person> people = new IfiCollection<Person>();
		
	    
	    
	    /**
	     * legger en gruppe til et fag.
	     * finner faget med kurskoden og legger en gruppe til.
	     *
	     * @param subject sin courseCode
	     * @param teachingAssistant username
	     */
		protected void addGroup(String subject, String teachingAssistant){
			
			for(Subject s : subjects){
				if(subject.compareTo(s.courseCode) == 0){
					for (TeachingAssistant t : people){
						if(t.username.compareTo(teachingAssistant)){
						Group g;
						g.teachingAssistant = t;
						s.groups.add(g);
						}	
					}
				}		
			}		
		}
		
		
		/**
		 * Legger til en professor.
		 *
		 * @param Professor navn
		 * @param professor username
		 * @param professor email.
		 */
		protected void addProfessor (String name, String username, String email){
			professor p;
			p.name = name;
			p.username = username;
			p.email = email;
			
			people.add(p);
			System.out.println(name +" "+ username +" "+ email +" er nå lagt til som lærer");
			
		}
		
		/**
		 * Kort oppsummering av hva metoden gjør.
		 * Detaljert beskrivelse av hva og hvordan metoden gjør det den gjør.
		 *
		 * @param Student navn.
		 * @param Student brukernavn.
		 * @param Student epost
		 */
		protected void addStudent(String name, String username, String email){
			
			student s;
			
			s.name = name;
			s.username = username;
			s.email = email;
			
			people.add(s);
			System.out.println(name +" "+ username +" "+ email +" er nå lagt til som elev");			
			
		}
		
		/**
		 * Legger til et emne.
		 * lager et emne og finner læreren og legger han/hun til.
		 *
		 * @param Kurskode.
		 * @param professor sitt brukernavn.
		 */
		protected void addSubject(String courseCode, String professor){
			
			subject su;
			su.courseCode = courseCode;
			for(Professor p : people){
				if (p.username.compareTo(professor) == 0){
					su.lecturer = p;
					}
				}
			subjects.add(su);
			System.out.println("Gruppen med koden: "+ courseCode +" er nå lagt til med "+ professor +" som lærer");
			
			
			
		}
		
		/**
		 * legger til en Lærer assistent
		 * Detaljert beskrivelse av hva og hvordan metoden gjør det den gjør.
		 *
		 * @param Navn
		 * @param Brukernavn.
		 * @param Epost.
		 */
		protected void addTeachingAssistant(String name, String username, String email){
			
			teachingAssistant t;
			t.name = name;
			t.username = username;
			t.email = email;
			
			people.add(t);
			System.out.println(name +" "+ username +" "+ email +" er nå lagt til som lærerasistent");
		}
		
		/**
		 * legger en person til en gruppe.
		 * Sjekker først om personen finnes, så om emnet finnes og om gruppen finnes hvis så legger den til eleven i denne gruppen.
		 *
		 * @param Student brukernavn
		 * @param Emne sin kurskode
		 * @param Gruppen sit nummer
		 */
		protected void enrollStudent(String student, String subject, int group){
			
			
			for(Student s : people){
				if (s.username.compareTo(student) == 0){
					for(subject su : subjects){
						if (su.courseCode.compareTo(subject) == 0){
							for(Group g : su.groups){
								if(g.number.compareTo(group) == 0){
									g.enrollStudent(s);
									System.out.println(student +" er nå lagt til gruppen "+ group +" i faget "+ subject);
								}
							}
						}
					}
				}
			}
			
			
		}
		
		/**
		 * Gir ut navnet på alle personer og emner
		 * Går igjennom alle personer og alle amner å priner ut navnet
		 *
		 */
		protected void list(){
			
			for(Person p : people){
				System.out.println(p.name);
				
			}
			for(Subject s: subjects){
				System.out.println(s.courseCode);
				
			}
			
		}
		
		/**
		 * Henter en person å gir ut info om den personen.
		 * Metoden går igjennom alle personer å sjekker om den har samme brukernavn.
		 *
		 * @param Person sitt brukernavn.
		 */
		protected void showPerson(String username){
			
			for (Student s : peolpe){
				if(username.compareTo(s.username) = 0){
					System.out.print("Student navn:"+ s.name +", Brukernavn: "+ s.username +" og e-post: "+ s.email);
					s.getSubjects;
					for(int i = 0; i<studEmne.length; i++){
						System.out.println(username +" sitt emne: "+ studEmne[i]);
					}
					}
				System.out.println("");
				}
			for (Professor p : people){
				if(username.compareTo(p.username) == 0){
					System.out.print("Lærer navn: "+ p.name +", Brukernavn: "+ p.username +" og epost: "+ p.email);
					System.out.println(p.subject.courseCode);
				}
			}
			for (TeacherAssistant t : people){
				if(username.compareTo(t.username) == 0){
					System.out.print("Lærerassistent navn: "+ t.name +", Brukernavn: "+ t.username +" og epost: "+ t.email);
					System.out.println("emne: "+ t.getSubject);
				}
				
			}
			}
			
			
			
		}
		
		/**
		 * Viser info til et emne.
		 * går igjennom alle emnene for og finne det riktige går dermed igjennom alle lærere, elever og grupper og printer dem ut   
		 *
		 * @param kurskode
		 */
		protected void showSubject(String courseCode){
			
			for (Subject s : subjects){
				if(s.courseCode.compareTo(courseCode) = 0){
					s.getTeachers();
					s.getStudents();
					for(int i = 0; i < teaSub.length; i++){
						system.out.println("Lærer: "+ teaSub[i]);
					}
					for (int i = 0; i < studSub.length; i++){
						system.out.println("Elev: "+ studSub[i]);
					}
					for (group g : s.groups){
						System.out.println("gruppe med nummer: "+ g.number);
					}
				}
		}
	}
}
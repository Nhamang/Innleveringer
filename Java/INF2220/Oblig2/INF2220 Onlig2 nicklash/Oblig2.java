import java.io.*;
import java.util.*;

/**
* This program creates a "graph" for a project from a text file. 
* It runs trough the project to find out if it can be done
* and don't run in a loop. If there is no loop the "best/fastest" time
* for the project to be completed. The earliest-, latest start and the slack
* per task. All is printed step by step on the screen. 
*
* @author Nicklash
* @verion 1.0
*/
class oblig2{
	public static void main(String [] argv){
		if(argv.length==2){
			String file=argv[0];
			int men = Integer.parseInt(argv[1]);
			if(men==999){
				//System.out.println("check 1");
				new planner(file, men);
			}else{
				System.out.println("Optional not done");
			}
		}else{
			System.out.println("Usage: java Oblig2 filename manpower");
		}
	}
}


class planner{
	int men, antTask, fastest, tmpFastest;
	String filename;
	String output="";
	String output2="";
	boolean [] visited;
	//ArrayList<Task> Tasks;
	Task [] Tasks;
	
	planner(String filename, int men){
		this.men=men;
		this.filename=filename;
		
		readFile();
		
		Stack<Task> stack = findCycle();
		
		if(!stack.isEmpty()){
			
			System.out.println("Has a cycle");
			output+="\nHas a cycle";
			System.out.println("");
			output+="\n";
			
			Stack<Task> turned = flipStack(stack);
			
			while(!turned.isEmpty()){
				Task t = turned.pop();
				output+=" "+t.id+" ";
				System.out.print(" "+ t.id +" ");
				
			}
			saveOutput();
			return;
		}
		
		//findTopsort();
		findEarliest();
		printTime();
		findLatest();
		
		output+="\nFastest time: "+ fastest +".";
		System.out.println("\nFastest time: "+ fastest +"\n");
		
		printTasks();
		saveOutput();
	}
	
	
	/**
	* Flips a stack so the first element is the last and vice versa.
	* @param 	s 	The stack that will be flipped.
	* @return		A new Stack that contains the elements of (@param s) in reverse order
	*/
	private Stack <Task> flipStack(Stack <Task> s){
		Stack<Task> tmpStack = new Stack<Task>();
		
		while(!s.isEmpty()){
			tmpStack.push(s.pop());
		}
		return tmpStack;
	}
	
	
	/**
	* Sets all the values for visited to false;
	*/
	void resetVisited(){
		for(int i=0; i<visited.length; i++){
			visited[i]=false;
		}
	}
	
	
	/**
	* Runs trough all the Tasks to set there cntPredecessors to 0.
	* it then increases it by 1 per edge.
	*/
	public void resetPredecessors(){
		
		for (Task t : Tasks){
			if (t!=null){
				t.cntPredecessors=0;
			}
		}
		
		for(Task t : Tasks){
			if(t!=null){
				for(Edge e : t.outEdges){
					e.in.addPred();
				}
			}
		}
	}
	
	/*--------------------------------------------------------*/
	/*                 Start find time methods                */
	/*--------------------------------------------------------*/
	
	/**
	* In cooperation with method DFS it finds a possible cycle
	* O(T*DFS)
	* 
	* @return	a stack with a possible cycle.
	*/
	private Stack <Task> findCycle(){
	
		resetPredecessors();
		resetVisited();
		Stack <Task> tmpStack=new Stack<Task>();
		int cnt =0;
		
		for(int i=1; i<Tasks.length; i++){
			if(Tasks[i].cntPredecessors==0){
				boolean cycle = DFS(Tasks[i], tmpStack);
				if(cycle){
					return tmpStack;
				}			
			}
		}
		return tmpStack;
	}
	
	/**
	*	Checks if a task has already been visited, if not it continues trough the edges 
	*	till a cycle is found or all the edges have been visited.
	*	
	*	O(|T|+|E|)
	* 	Source: http://en.wikipedia.org/wiki/Depth-first_search#Pseudocode
	*	
	*	@param	t	current task to be tested
	*	@param	tmpStack	the stack for the printing of a cycle 
	*/
	private boolean DFS(Task t, Stack <Task> tmpStack){
		
		if(tmpStack.contains(t)){
			tmpStack.push(t);
			return true;
		}
		
		tmpStack.push(t);
		
		for(Edge e : t.outEdges){
			if(!visited[e.in.id] && DFS(e.in, tmpStack)){
				return true;
			}
		}
		tmpStack.pop();
		visited[t.id]=true;
		return false;
	}
	
    /**
	*	In cooperation with findEarliestRec it finds all the tasks earliest possible starts.
	*	By sending all Tasks with cntPredecessors equals zero to findEarliestRec
	*	O(T*E*findEarliestRec)
	*/
	void findEarliest(){
		for(int i = 1; i<Tasks.length; i++){
			if(Tasks[i].cntPredecessors==0){
				for(Edge e : Tasks[i].outEdges){
					findEarliestRec(e.in, 0, Tasks[i].time);
				}
			}
		}
	}
	
	/**
	*	Finds adds the previous tasks earliest with its time to find this tasks earliest.
	*	Keeps going till there is no more outEdges, when that happens it finds the best completion time
	*	
	*	O(E)
	*	
	*	@param	t	the task who's earliest time should be found
	*	@param	early	the earliest start for the previous task
	* 	@param	time	the time of the previous task
	*/
	void findEarliestRec(Task t, int early, int time){
		
		int tmpEarly = early+time;
		
		if(tmpEarly>t.earliestStart){
			t.earliestStart=tmpEarly;
			
		}else{
			tmpEarly=t.earliestStart;
		}
		
		
		if(t.outEdges.isEmpty()){
			if((t.earliestStart+t.time)>fastest){
				fastest=t.earliestStart+t.time;
			}
		}
		
		
		for(Edge e : t.outEdges){
			findEarliestRec(e.in, tmpEarly, t.time);
		}
	
	}
	
	/**
	*	Finds latest start off all tasks with zero predecessors. by sending
	*	them to int version.
	*/
	void findLatest(){
		for(int i = 1; i<Tasks.length; i++){
			
			if(Tasks[i].cntPredecessors==0){
				
				output2+="\n\nvisiting: "+Tasks[i].id;
				
				for(Edge e : Tasks[i].outEdges){
					
					int tmpLatest = findLatest(e.in);
					int tmpLatest1 = tmpLatest - Tasks[i].time;
					output2+="\n\nre-visiting: "+e.in.id;
					
					if(tmpLatest1<Tasks[i].latestStart){
						Tasks[i].latestStart=tmpLatest1;
					}
					output2+="\nTask: "+Tasks[i].id+" Latest: "+ Tasks[i].latestStart;
				}
			}
		}
	}
	
	
	/**
	*	Runs to the end of the graph to find the latestStart which it sends back
	*	to find predecessors latestStart. 
	*	@param	t	Task who's latestStart will be found
	*	@return		Tasks latestStart
	*/
	private int findLatest(Task t){
		
		output2+="\nvisiting: "+t.id+"\n";
		
		if(t.outEdges.isEmpty()){
			t.latestStart=fastest-t.time;
			output2+="\nTask: "+t.id+" Latest: "+t.latestStart;
			return t.latestStart;
		}
		
		int tmpLatestTime=t.latestStart;
		
		for(Edge e : t.outEdges){
		
			int tmpLatest=findLatest(e.in);
			tmpLatest-=e.in.time;
			
			output2+="\n\nre-visiting: "+t.id;
			output2+="\nTesting: "+tmpLatest+" to: "+tmpLatestTime;
			
			if(tmpLatest<tmpLatestTime && tmpLatest >=t.earliestStart){
				tmpLatestTime=tmpLatest;
				output2+="\nChanged: "+t.id+" to "+tmpLatestTime;
			}if(tmpLatest<=t.earliestStart){
				tmpLatestTime=t.earliestStart;
			}
		}
		t.latestStart=tmpLatestTime;
		output2+="\nTask: "+t.id+" Latest: "+ t.latestStart;
		return t.latestStart;
	}
	
	
	/*--------------------------------------------------------*/
	/*                   Start print methods                  */
	/*--------------------------------------------------------*/

	/**
	*	Prints the steps of the best possible schedule 
	*/
	void printTime(){
		
		int fastest=0;
		int finished=1;
		int currentTime=-1;
		int men = 0;
		boolean print=true;
		
		System.out.println("Time: 0");
		output+="\nTime: 0";
		
		while(finished<Tasks.length){
			currentTime+=1;

			for(int i = 1; i<Tasks.length; i++){
				if(((Tasks[i].earliestStart)+(Tasks[i].time))==currentTime){
					if(!print) {
						System.out.println("Time: "+currentTime); 
						output+="\nTime: "+currentTime;
						print=true;
					}
					
					System.out.println("\tFinished: "+ Tasks[i].id);
					output+="\n\tFinished: "+ Tasks[i].id;
					men-=Tasks[i].staff;
					finished+=1;
				}
				
				if(Tasks[i].earliestStart==currentTime){
					if(!print){
						System.out.println("Time: "+currentTime);
						output+="\nTime: "+ currentTime;
						print=true;
					}
					System.out.println("\tStarting: "+Tasks[i].id);
					output+="\n\tStarting: "+ Tasks[i].id;
					men+=Tasks[i].staff;
				}
				
			}
			
			if(print){
				if(men!=0){
					System.out.println("Staff: "+ men);
					output+="\nStaff: "+ men;
					output+="\n";
				}
			}
			print=false;
		}
		output+="\n";
	}
	
	/**
	*	Prints all the needed info of every Task in the array.
	*/
	void printTasks(){
		
		Stack <Integer> critical = new Stack<Integer>();
		
		for(int i = 1; i<Tasks.length; i++){
			
			if(Tasks[i]!=null){
				
				output+="\n*-----------------------------------*\n";
				System.out.println("\nTask id: "+Tasks[i].id+".");
				output+="\nTask id: "+ Tasks[i].id;
				
				System.out.println("Name: "+Tasks[i].name+".");
				output+="\nName: "+Tasks[i].name;
				
				System.out.println("Time: "+Tasks[i].time+".");
				output+="\nTime: "+Tasks[i].time;
				
				System.out.println("Staff: "+Tasks[i].staff+".");
				output+="\nStaff: "+Tasks[i].staff;
				
				int slack = Tasks[i].latestStart-Tasks[i].earliestStart;
				
				if(slack==0){
					critical.push(Tasks[i].id);
				}
				
				System.out.println("Slack: "+slack+".");
				output+="\nSlack "+slack;
				
				System.out.println("Earliest Start: "+Tasks[i].earliestStart+".");
				output+="\nEarliest Start: "+Tasks[i].earliestStart;
				
				System.out.println("Latest Start: "+Tasks[i].latestStart+".");
				output+="\nLastest Start: "+Tasks[i].latestStart;
				
				System.out.print("Task dependent on this: ");
				output+="\nTasks dependent on this: ";
				
				for(Edge e : Tasks[i].outEdges){
					output+=" "+e.in.id+" ";
					System.out.print(e.in.id+" ");
				}
				System.out.println("");
			}
		}
		
		System.out.println("");
		System.out.println("These Tasks are critical: ");
		output+="\n\nThese Tasks are critical: \n";
		
		while(!critical.isEmpty()){
			int tmp = critical.pop();
			System.out.print(" "+tmp);
			output+=" "+tmp;
		}
	}
	
	
	/*--------------------------------------------------------*/
	/*              Start file management methods             */
	/*--------------------------------------------------------*/
	
	/**
	*	Reads the text file, creates an array and adds all values.
	*/
	public void readFile(){
		
		try{
			
			Scanner reader = new Scanner(new File(filename));
			int antTask=Integer.parseInt(reader.nextLine());
			createEmpty(antTask);
			visited = new boolean[antTask+1];
			String thisLine = reader.nextLine();
			int i=1, cnt=0;
			
			while(reader.hasNextLine()&&cnt<antTask){
				
				cnt++;
				thisLine=reader.nextLine();
				String []splitLine=thisLine.split("\\s+");
				int id=Integer.parseInt(splitLine[0]);
				String name=splitLine[1];
				int time=Integer.parseInt(splitLine[2]);
				int manpower=Integer.parseInt(splitLine[3]);
				Tasks[id].setNewValues(id, name, time, manpower);
				
				if(Integer.parseInt(splitLine[4])!=0){
					int j=4;
					while(Integer.parseInt(splitLine[j])!=0){
						//System.out.println("Adding edges");
						Tasks[Integer.parseInt(splitLine[j])].addEdge(Integer.parseInt(splitLine[j]), id);
						Tasks[id].addPred();
						j++;
					}
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("Cannot open file");
		}
	}
	
	/**
	*	Saves all the text that appeared on the screen to a text file.
	*/
	private void saveOutput(){
		
		String newFile= filename.replace(".txt", "");
		String outputFileName=newFile+"_output.txt";
		
		try{
			
			File outputFile= new File(outputFileName);
			outputFile.createNewFile();
			FileWriter writeTo = new FileWriter(outputFile);
			writeTo.write(output);
			writeTo.close();
		
		}catch(IOException e){
			System.out.println("Could not open/create file");
		}
	}
	
	
	/**
	*	initiates the array to given size and fills it with empty Tasks
	*	
	*	@param size	The size of the array
	*/
	private void createEmpty(int size){
		
		Tasks = new Task[size+1];
		int i = 0;
		
		while(i<size+1){
			Tasks[i]=new Task();
			i+=1;
		}
	}
	
	
	/*--------------------------------------------------------*/
	/*                   Start inner classes                  */
	/*--------------------------------------------------------*/
	
	/**
	*	
	*/
	class Task implements Comparable<Task>{
		
		int id, time, staff, slack;
		String name;
		int earliestStart, earliestEnd; 
		int latestStart = Integer.MAX_VALUE;
		ArrayList<Edge> outEdges= new ArrayList<Edge>();
		int cntPredecessors;
		
		/**
		*	Adds new values to this task
		*/
		public void setNewValues(int id, String name, int time, int staff){
			this.id=id;
			this.name=name;
			this.time=time;
			this.staff=staff;
		}
		
		public int compareTo(Task t){
			if(earliestEnd>t.earliestEnd){
				return 1;
			}else if(earliestEnd<t.earliestEnd){
				return -1;
			}else{
				return 0;
			}
		}
		
		/**
		*	Adds the Tasks dependent on this Task
		*/
		public void addEdge(int id1, int id2){
			outEdges.add(new Edge(Tasks[id1], Tasks[id2]));
		}
			
		/**
		*	increases the amount of Predecessors
		*/
		public void addPred(){
			cntPredecessors+=1;
		}
	}

	
	/**
	*
	*/
	class Edge {
		Task out;
		Task in;
	
		Edge(Task one, Task two){
			out = one;
			in = two;
		}
	}
	
	/*--------------------------------------------------------*/
	/*                     Start old code                     */
	/*--------------------------------------------------------*/
	
	/**
	* No longer in use as it does not store cycle, but choose to keep.
	*/
	boolean findCycleEx(){
		if(Tasks==null){
			return false;
		}
		long counter=1;
		Queue<Task> q;
		resetPredecessors();
		
		q=new LinkedList<Task>();
		
		for(int i = 1; i<Tasks.length; i++){
			if(Tasks[i].cntPredecessors==0){
				q.offer(Tasks[i]);
			}
		}
		if(q.size()==0){
			return true;
		}
		
		for(counter = 1; !q.isEmpty(); counter++){
			Task t = q.poll();
			for(Edge e: t.outEdges){
				Task tk = e.in;
				if(--tk.cntPredecessors==0){
					q.offer(tk);
				}
			}
		}
		
		if(counter != Tasks.length){
			return true;
		}
		return false;
		
	}
	
	
	void findLatestEx(){
		if(Tasks==null){
			return;
		}
		
		for (int i=1;i<Tasks.length; i++){
			int tmpLatest;
			if(Tasks[i].outEdges.isEmpty()){
				Tasks[i].latestStart=fastest-Tasks[i].time;
			}else{
				for(Edge e : Tasks[i].outEdges){
					tmpLatest=e.in.latestStart-Tasks[i].time;
					//Tasks[i].latestStart=tmpLatest;
					if(tmpLatest<Tasks[i].latestStart){
						Tasks[i].latestStart=tmpLatest;
					}
				}
			}
		}
	}
	
	void findEarliestEx(){
		if(Tasks==null){
			return;
		}
		
		int tmpEarly=0;
		for(int i = 1; i<Tasks.length; i++){
			tmpEarly=0;
			if(Tasks[i].cntPredecessors==0){
				Tasks[i].earliestStart=0;
				for(Edge e : Tasks[i].outEdges){
					tmpEarly=Tasks[i].earliestStart+Tasks[i].time;
					if(e.in.earliestStart<tmpEarly){
						e.in.earliestStart=tmpEarly;	
					}
				}
			}else{
				for(Edge e : Tasks[i].outEdges){
					tmpEarly=Tasks[i].earliestStart+Tasks[i].time;
					if(e.in.earliestStart<tmpEarly){
						e.in.earliestStart=tmpEarly;
					}
				}
			}
		}
	}
	
	/**
	*	Not in use due to found nearly complete online
	*/
	private void findTopsort(){
		
		resetPredecessors();
		
		PriorityQueue<Task> q = new PriorityQueue<Task>();
		int men=0, currentTime=0;
		
		System.out.println("Time: 0");
		output+="\nTime: 0";
		
		for(int i = 1; i<Tasks.length; i++){
			if(Tasks[i].cntPredecessors==0){
				
				Tasks[i].earliestEnd=currentTime+Tasks[i].time;
				q.offer(Tasks[i]);
				
				System.out.println("\tStarting: "+Tasks[i].id);
				output+="\n\tStarting: "+Tasks[i].id;
				
				men+=Tasks[i].staff;
			
			}
		}
		System.out.println("Staff: "+men);
		
		while(!q.isEmpty()){
			Task t = q.remove();
			//currentTime+=t.time;
			System.out.println("Time: "+t.earliestEnd);
			output+="\nTime: "+t.earliestEnd;
			
			System.out.println("\tFinishing: "+t.id);
			output+="\n\tFinished: "+t.id;
			
			fastest=t.earliestEnd;
			men-=t.staff;
			
			for(Edge e: t.outEdges){
				--e.in.cntPredecessors;
				if(e.in.cntPredecessors==0){
					System.out.println("\tStarting: "+e.in.id);
					output+="\n\tStarting: "+e.in.id;
					
					e.in.earliestEnd=t.earliestEnd+e.in.time;
					e.in.earliestStart=t.earliestEnd;
					men+=e.in.staff;
					q.offer(e.in);
				}
			}
			System.out.println("Staff: "+men);
			output+="\nStaff: "+men;
			
			System.out.println("");
			output+="\n";
		}
	}
	
}

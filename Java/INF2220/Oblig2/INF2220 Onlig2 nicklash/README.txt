The code finds and prints possible cycles, finds "best" time 
then prints a schedule for reaching that time.

Compile and run (Tested on w8):
java Oblig2.java
javac oblig2 "filename" "manpower"


Assumptions:
The printing of the schedule achieve and the finding of the best time
does not have to be done in the same method. 

Credit:
Found help for DFS from the pseudo code at:  
http://en.wikipedia.org/wiki/Depth-first_search#Pseudocode

Found all I needed for Top sort from:
http://codereview.stackexchange.com/questions/44689/topological-sort-in-java
changed the queue from LinkedList to priorityQueue where the earliest time a task could end being
the factor to compare. But even if the top sort is faster I choose to use another method. 
One I created and didn't find almost complete on the internet.

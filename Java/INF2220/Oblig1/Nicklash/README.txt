Function addWord and removeWord could also be in the node class, but I choose to have it inn the spellcheck class.
I choose to make a date alteration to the "utskrift" fil.


Changes:
Changed startTime, endTime and totalTime from double to long.
Removed æø and å from the alphabet.
Added new comments.
Added safeguard to skip null word.

New info:
The program does not remove possible duplicates. This can happen in cases where wrong word contains same letter next to each other 
or removing a letter and changing a letter gives the same. the assignment text says nothing about these cases.

Testing:
Tested on Windows 8.1 with: "javac Oblig1.java && java oblig1" without æø and å.
Tested on Ubuntu with: "javac Oblig1.java && java oblig1" with æø and å

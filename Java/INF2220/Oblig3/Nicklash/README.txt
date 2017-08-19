1.
My algorithm works like the the original Boyer moore horspool algorithm.
It is merely extended with an extra test in the for loop for backwards pattern matching.
It also adds the smallest of '_' and haystack[offest+last] to offset.

2.
compile: "javac Oblig3.java"
run program w/testcase: "java oblig3 test"
run program: "java oblig3 needlefile haystackfile"

3.
Oblig3.java includes the main method.

4.
I assumed we should work from existing BMH string matching

5.


6.
Everything work's.

7.
found the unedited algorithm at http://heim.ifi.uio.no/bjarneh/other/inf2220/textalgorithms/TextAlgorithms.java.html 

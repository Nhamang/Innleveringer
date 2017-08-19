#Navn: Nicklas M. Hamang Brukernavn: Nicklash
#input: -optinal filname word
#!/usr/bin/env python
import sys, re, getopt

#henter valgfrie argumenter.
try:
    opt, argv = getopt.getopt(sys.argv[1:], 'ib', ['insensitive', 'boundaries'])
    filnavn=argv[0]
    ord=argv[1]
except:
    print("Error")
    sys.exit()

infil= open(filnavn, 'r').read() #leser filen
flag=0
ord2=ord #lagrer extra variabel som trengs i -b
#går igjennom valgfrie variabler 
for valg, value in opt:
    if valg in('-b', '--boundaries'):
        ord2=(r"\b%s\b"%ord) #hvis -b leger til boundaries på begge sider av ordet
    elif valg in('-i', '--insensitive'):
        flag=1 #endrer flag som skal brukes senere



if flag==1:
    ant=len(re.findall(ord2, infil, re.I)) #bruker re.I for case insensitive
    print("Filen: %s inneholder ordet: %s (case insensitive) %d ganger" %(filnavn, ord, ant))

else:
    ant=len(re.findall(ord2, infil))
    print("Filen: %s inneholder ordet: %s %d ganger" %(filnavn, ord, ant))

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke4>python count_words.py football.txt ball
#Filen: football.txt inneholder ordet: ball 3 ganger
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke4>python count_words.py -i football.txt ball
#Filen: football.txt inneholder ordet: goal (case insensitive) 3 ganger
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke4>python count_words.py -i -b football.txt ball
#Filen: football.txt inneholder ordet: goal (case insensitive) 2 ganger
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke4>python count_words.py -b football.txt ball
#Filen: football.txt inneholder ordet: goal 1 ganger

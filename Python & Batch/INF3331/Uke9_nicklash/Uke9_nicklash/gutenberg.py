#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import re, sys, time

t0=time.clock()

try:
    infil=sys.argv[1]
    try:
        outfil=sys.argv[2]
    except:
        outfil="Counted_words.dat"
except:
    infil=input('input file name: ')
    outfil=input('What do you want to output fil to be named: ')

string=""
count=0
words={}

inn=open(infil, 'r').read()

pattern=(r'\w+')
r=re.compile(pattern)

for matches in r.finditer(inn):
    match=matches.group()

    if match in words:
        words[match]+=1
    else:
        words[match]=1
sortert= sorted(words.items())

string+=(infil)
string+=("")
string+=("word\t\t\tant\n")

for i in sortert:
    #print("%s\t\t\t%d"%(i[0], i[1]))
    string+=("%s\t\t\t%d\n"%(i[0], i[1]))
    count+=1

t1=time.clock()

string+=("")
string+=("Count: %d\n" %count)
string+=("Time: %g\n"%(t1-t0))
print("Wrote to: %s\n"%outfil)

out=open(outfil, 'w')
out.write(string)
out.close()

print("done")
#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke9>python gutenberg.py filmdata.txt
#
#word			ant
#0			2382
#000			13
#01			3
#09			1
#1			62
#10			103
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#zul1                    1
#zul2                    2
#zuster                  2
#zvo1                    1
#zweier                  1
#zyg1                    1
#zyg2                    1
#
#Count: 61218
#Time: 170.445
#Wrote to: Counted_words.dat

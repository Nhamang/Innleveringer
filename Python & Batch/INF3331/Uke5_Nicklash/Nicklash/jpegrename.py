#navn: Nicklas M Hamang Brukernavn: Nicklash

import sys, re, os
filnavn=sys.argv[1]
infil=open(filnavn, 'r').read()
#leser in første linja for navnet
navn=open(filnavn, 'r').readline().split(": ")[1].split(".jpg")[0]

def gettime(infil):
	#finner hvor det er xxxx:xx:xx xx:xx:xx 
    pattern=r'(\d{4}):(\d{2}):(\d{2})\s+(\d{2}):(\d{2}):(\d{2})' 
    match=re.findall(pattern, infil)
    resultat=match[1]#henter fil Date/time, [0] hadde hentet file date
    #dato="_".join(resultat[0:3])#fjerner behovet for tupler
    #tid="_".join(resultat[3:6])
    #datotid=dato+"__"+tid
    return ("_".join(resultat[0:3]), "_".join(resultat[3:6]))#setter sammen dato'en og tiden med _  mellom      

def prefix(datotid, navn):
    utnavn=""
	#setter sammen dato og tid
    for i in datotid:
        utnavn+=i+"__"
    utnavn+=navn
    return (utnavn)

datotid=gettime(infil)
ferdignavn=prefix(datotid, (navn+".jpg"))
print ("Navnet er nå: %s"%ferdignavn) #Printer ut det nye navnet
os.rename(filnavn, ferdignavn) #Bytter navnet på filen
"""
Runtime ex.
C:\Users\Nicklas\Desktop\Uke5 dir
 Volume in drive C is Acer
 Volume Serial Number is 2622-3AC5

 Directory of C:\Users\Nicklas\Desktop\Uke5

01.10.2013  16:29    <DIR>          .
01.10.2013  16:29    <DIR>          ..
29.09.2013  00:42               435 jhead - Kopi.sample
29.09.2013  00:42               435 jhead.sample
01.10.2013  16:26               608 join.py
01.10.2013  16:23             1 026 jpegrename.py
30.09.2013  11:35               613 my_script.py
01.10.2013  16:20               727 xygenerator.py
               6 File(s)          3 844 bytes
               2 Dir(s)   2 489 876 480 bytes free

C:\Users\Nicklas\Desktop\Uke5>python jpegrename.py jhead.sample
Navnet er nå: 2002_05_19__18_10_03__tmp2.jpg

C:\Users\Nicklas\Desktop\Uke5>dir
 Volume in drive C is Acer
 Volume Serial Number is 2622-3AC5

 Directory of C:\Users\Nicklas\Desktop\Uke5

01.10.2013  16:29    <DIR>          .
01.10.2013  16:29    <DIR>          ..
29.09.2013  00:42               435 2002_05_19__18_10_03__tmp2.jpg
29.09.2013  00:42               435 jhead - Kopi.sample
01.10.2013  16:26               608 join.py
01.10.2013  16:23             1 026 jpegrename.py
30.09.2013  11:35               613 my_script.py
01.10.2013  16:20               727 xygenerator.py
               6 File(s)          3 844 bytes
"""
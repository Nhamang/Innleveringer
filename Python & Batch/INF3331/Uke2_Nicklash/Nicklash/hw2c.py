#Navn: Nicklas M. Hamang Brukernavn: Nicklash
from math import log
import sys
i=1
print("Hello world. ")
while i<=len(sys.argv[1:]):
    r=float(sys.argv[i])
    if (r>0):
        print("ln(%g)=%.4f"%(r, log(r)))
    else:
        print("ln(%g): illegal"%r)
    i+=1

#Runtime ex.

#C:\Users~~\Uke2>python hw2c.py 6 4 -6 -4
#Hello world.
#ln(6)=1.7918
#ln(4)=1.3863
#ln(-6): illegal
#ln(-4): illegal
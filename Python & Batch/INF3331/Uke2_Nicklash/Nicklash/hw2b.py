#Navn: Nicklas M. Hamang Brukernavn: Nicklash
from math import sin
import sys
i=1
print("Hello world. ")
while i<=len(sys.argv[1:]):
    r=float(sys.argv[i])
    print("sin(%g)=%.4f"%(r, sin(r)))
    i+=1

#Runtime ex.
#C:\Users~~\Uke2>python hw2b.py 6 4 5
#Hello world.
#sin(6)=-0.2794
#sin(4)=-0.7568
#sin(5)=-0.9589

#Navn: Nicklas M. Hamang Brukernavn: Nicklash
from math import sin
import sys
i=0
print("Hello world. ")
for i in sys.argv[1:]:
    r=float(i)
    print("sin(%g)=%.4f"%(r, sin(r)))


#Runtime ex.
#C:\Users~~\Uke2>python hw2a.py 6 4 22
#
#Hello world.
#sin(6)=-0.2794
#sin(4)=-0.7568
#sin(22)=-0.0089

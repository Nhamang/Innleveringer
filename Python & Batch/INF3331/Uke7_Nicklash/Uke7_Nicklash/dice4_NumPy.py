#Nicklas M. Hamang Brukernavn: Nicklash
#Sammarbeidet tett med Nicolas Lopez, nicolael
#!/usr/bin/env python
import numpy as np
import sys, time

t0=time.clock()

n=int(sys.argv[1])

dice=np.random.randint(1, high=7, size=(n*4)) #Lager array'et
dice.shape=(n, 4)#endrer til nX4 dimensjons array
sum2 = np.sum(dice, axis=1)#Summerer alle rader
sum3 = np.sum(sum2<9)


pot=0
potp=sum3*10
pot+=potp
pot-=n

t1=time.clock()

if pot<0:
    print("Du burde ikke spille")
if pot>0:
    print("Du burde spille")
print("Tid: %g"%(t1-t0))

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke7>python dice4_NumPy.py 3000
#Du burde ikke spille
#Tid: 0.000580256

#Nicklas M. Hamang Brukernavn: Nicklash
#Sammarbeidet tett med Nicolas Lopez, nicolael
#!/usr/bin/env python

import numpy as np
import sys, time

t0=time.clock()
n=int(sys.argv[1])

#Lager dice array
dice1= np.random.randint(1, high=7, size=n)
dice2= np.random.randint(1, high=7, size=n)
#Summerer
sumert=np.sum(((dice1==6)+(dice2==6)))

p=float(sumert/n)
s='%'
pros=p*100
t1=time.clock()
print("n=%d, p=%g, %s=%g%s" %(n, p, s, pros, s))
print("Tid: %g"%(t1-t0))


#Bruk denne, er endret og funker
#from numpy import *
#
#n=int(sys.argv[1])
#
#dice1= random.randint(1, high=7, size=n)
#dice2= random.randint(1, high=7, size=n)
#dice=(dice1==6)+(dice2==6)
#sum2=sum(dice)
#
#p=float(sum2/n


#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke7>python dice2_NumPy.py 3000
#n=3000, p=0.305333, %=30.5333%
#Tid: 0.00148716

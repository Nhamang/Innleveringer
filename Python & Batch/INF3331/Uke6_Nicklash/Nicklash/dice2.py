#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import random, sys

n=float(sys.argv[1])
teller=0
sum=0


while teller<n:
    if(random.randint(1, 6)==6 or random.randint(1, 6)==6):
        sum+=1
    teller+=1
p=sum/float(n)
p2=p*100
s='%'
print("n=%d, p=%.3f %s=%g%s"%(n, p,s,p2,s))

"""
C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke6>python dice2.py 3000
n=3000, p=0.307 %=30.7%

C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke6>python dice2.py 3000
n=3000, p=0.314 %=31.3667%

C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke6>python dice2.py 3000
n=3000, p=0.306 %=30.6333%
"""

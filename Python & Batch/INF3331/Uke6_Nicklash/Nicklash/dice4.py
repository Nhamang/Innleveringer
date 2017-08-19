import random, sys

n=float(sys.argv[1])
teller=0
pot=0


while teller<n:
    pot-=1
    if((random.randint(1, 6) + random.randint(1, 6)+random.randint(1, 6)\
        + random.randint(1, 6))<9):
        pot+=10
    teller+=1

if pot>0:
    print("Du burde spille")
if pot<0:
    print("Du burde ikke spille")
if pot==0:
    print("Du går i null")

"""
C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke6>python dice4.py 3000
Du burde ikke spille

C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke6>python dice4.py 3000
Du burde ikke spille

C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke6>python dice4.py 3000
Du burde ikke spille
"""

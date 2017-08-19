#Nicklas M. Hamang Brukernavn: Nicklash
#Sammarbeidet tett med Nicolas Lopez, nicolael
#!/usr/bin/env python
from numpy import *

#Test conditions
x = arange(2,10,2) 
y=0.0

#Metode med navn fra oppgvetekst
def initial_condition(x):
    
    #Tester om det er en float som blir sendt in
    if type(x)==float:
        x=3.0
        return x

    #Kjører igjennom numpy array'et
    else:
        n = len(x)
        x = [0]*n
        for i in range(n):
            x[i] = 3
        return x

#Printer ur før og etter
print (x)
print (initial_condition(x))
print(y)
print(initial_condition(y))


#Runtime ex
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke7>python vectorize_function.py
#[2 4 6 8]
#[3, 3, 3, 3]
#0.0
#3.0

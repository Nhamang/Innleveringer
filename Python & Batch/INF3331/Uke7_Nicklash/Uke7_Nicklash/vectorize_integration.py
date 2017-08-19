#Nicklas M. Hamang Brukernavn: Nicklash
#Sammarbeidet tett med Nicolas Lopez, nicolael
#!/usr/bin/env python
from numpy import *
import time
t0=time.clock()

def f_x(f, a, b, n):
    h = (b - a) / n
    s = f(a) + f(b)
    for i in range(1, n):
        s += 2 * f(a + i * h)
    return s * h / 2

print (f_x(lambda x:x**5, 2.0, 12.0, 1000))
t1=time.clock()
print("Tid: %g" %(t1-t0))


#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke7>python vectorize_integration.py
#
#497654.19666654995
#Tid: 0.00500741

#!/usr/bin/env python
#navn: Nicklas M. Hamang Brukernavn: Nicklash
import numpy as np
#orginalen setter y=x som gjør at x også blir påvirket
x = np.linspace(0, 1, 3)
y = 2*x + 1
#y = x;  y*=2;  y += 1
z = 4*x - 4
#z = x;  z*=4;  z -= 4

print (x, y, z)

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke8>python NumPy_assignment.py
#[ 0.   0.5  1. ] [ 1.  2.  3.] [-4. -2.  0.]

#Navn: Nicklas M. Hamang Brukernavn: Nicklash
#!/usr/bin/env python
from numpy import *

A = array([[1, 2, 3], [4, 5, 7], [6, 8, 10]], float)
b = array([-3, -2, -1], float)

print(dot(A, b))

#runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke8>python matvec.py
#[-10. -29. -44.]

#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import sys, re
real = r"\s*(?P<number>-?(\d+(\.\d*)?|\d*\.\d+)([eE][+\-]?\d+)?)\s*"
c = re.compile(real)
some_interval = "[3.58652e+05 , 6E+09]"
groups = c.findall(some_interval)
#upper og lower prøvde å hente fra plass 1 mens tellet vi letteretter er i plass 0
lower = float(groups[0][c.groupindex['number']-1])
upper = float(groups[1][c.groupindex['number']-1])
print (lower)
print (upper)

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke4>python findallerror.py
#358652.0
#6000000000.0
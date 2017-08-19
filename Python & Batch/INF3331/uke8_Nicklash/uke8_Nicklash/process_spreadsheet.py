#!/usr/bin/env python
#Navn: Nicklas M. Hamang Brukernavn: Nicklash
#Input: Inn filnavn valgfritt: ut filnavn
#output: fil med navnet som ble gitt eller default spread_done.dat
#som inneholder onsket output.


import sys
from numpy import *

try:
    infil=sys.argv[1]
    try:
        oname=sys.argv[2]
    except:
        oname="spread_done.dat"
except:
    infil=input("Input filename: ")
    oname=input('Outputname: ')

f=open(infil, 'r')
line=f.readlines()
f.close()

#oname="spread_done"
ofile=open(oname, 'w')

names=[]
numbers=[]
string =""
for i in line:
    lines=i.split(',')
    name=lines[0]
    names.append(name)
    
    num=array(lines[1:], float)
    numbers.append(num)

sums=sum(numbers, axis=1)
i=0

while i <len(numbers):
    string+=("%s : %g\n" %(names[i], sums[i]))
    i+=1

print(string)
print("\nStored in %s"%oname)
ofile.write(string)
ofile.close()

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke8>python process_spreadsheet.py s
#pread.dat
#"activity 1" : 2719
#"activity 2" : 128
#"activity 3" : 365.5
#
#
#Stored in spread_done.dat

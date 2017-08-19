#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import re
loop1='[0:12]'
loop2='[0:12, 4]'

r1=r'\[(.+):(.+?),?(.*)\]'
r2=r'\[(.+):(.+),?(.*)\]'
r3=r'\[(.+):([^,]+),?(.*)\]'

print("loop1:")
print (re.search(r1, loop1).groups())
print (re.search(r2, loop1).groups())
print ("r3: ")
print (re.search(r3, loop1).groups())

print("loop2: ")
print (re.search(r1, loop2).groups())
print (re.search(r2, loop2).groups())
print("r3: ")
print (re.search(r3, loop2).groups())


#r1 funker ikke for noen av loop'ene. r2 funker for loop1 men ikke loop2
#
#runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke4>python loop_regex.py
#
#loop1:
#('0', '1', '2')
#('0', '12', '')
#r3:
#('0', '12', '')
#loop2:
#('0', '1', '2, 4')
#('0', '12, 4', '')
#r3:
#('0', '12', ' 4')

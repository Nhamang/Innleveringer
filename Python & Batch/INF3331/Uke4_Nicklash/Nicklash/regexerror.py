#Navn: Nicklas M. Hamang Brukernavn: Nicklash
#!/usr/bin/env python
"""Find all numbers in a string."""
import sys, re
r = r"([+\-]?\d\.\d+[Ee][+\-]\d\d?|[+\-]?\.\d+|[+\-]?\d+[.]?\d*)" #Skrevet om r fra hva jeg har lagret som r2

r2 =r"([+\-]?\d+\.?\d*|[+\-]?\.\d+|[+\-]?\d\.\d+[Ee][+\-]\d\d?)"
c = re.compile(r)
s = "an array: (1)=3.9836, (2)=4.3E-09, (3)=8766, (4)=.549"
numbers = c.findall(s)
# make dictionary a, where a[1]=3.9836 and so on:
a = {}
for i in range(0,len(numbers)-1,2):
    a[int(numbers[i])] = float(numbers[i+1])
sorted_keys = list(a.keys())
sorted_keys.sort()
for index in sorted_keys:
    print ("[%d]=%g" % (index,a[index]))

#runtime ex.
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke4>
#
#[1]=3.9836
#[2]=4.3e-09
#[3]=8766
#[4]=0.549


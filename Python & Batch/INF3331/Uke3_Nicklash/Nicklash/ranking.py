#!/usr/bin/env python
#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import sys, os, re

tekst=""
file= open(sys.argv[1], 'r')
CPUinfo = {}
for line in file:
    match=re.search(r"CPU-time:\s+((\d+\.\d* |\d*\.\d+)|(\d+))\s+(.*)" , line) #bruker Regex ril å finne CPU infoen
    if match:
        tmpi=match.group(1)#setter in CPU-tid som index plass
        tmp=match.group() #setter tmp til til hele groupen
        CPUinfo[tmpi]=tmp

#går igjennom et sortert CPUinfo array med match.group(1) som float key og match.group() som value og printer den ut
#Oppgaven sier ikke noe om skriv ut listen men valgte å legge til fordet
for key, value in sorted((float(k),v) for k,v in CPUinfo.items()):
    print ("%s" %(value))
    tekst+=("%s +\n" %(value))

#Valgte å legge til en skriv til fil
skrivfil=open('CPU.dat', 'w')
skrivfil.write(tekst)
skrivfil.close

#Runtime ex.
#C:\Users~~\Skole\INF3331\Uke3>python ranking.py 
#CPU-time:   5.41   g77 -O3 -ffast-math -funroll-loops original (optimal?) code
#CPU-time:   5.55   g77 -O3 original (optimal?) code
#CPU-time:   5.62   g77 -O2 original (optimal?) code
#CPU-time:   6.02   f95 -O3 original (optimal?) code
#CPU-time:   6.04   f95 -O1 original (optimal?) code
#CPU-time:   7.22   g77 -O3 -ffast-math -funroll-loops traversing the array rowwise
#CPU-time:   7.27   f95 -O2 traversing the array rowwise
#CPU-time:   7.30   g77 -O3 traversing the array rowwise
#CPU-time:   7.31   f95 -O3 traversing the array rowwise
#CPU-time:   7.37   g77 -O2 traversing the array rowwise
#CPU-time:   7.67   g77 -O1 original (optimal?) code
#CPU-time:   8.26   g77 -O1 traversing the array rowwise
#CPU-time:   9.45   f95 -O3 if-test inside the main loop
#CPU-time:   9.47   f95 -O1 if-test inside the main loop
#CPU-time:   9.52   f95 -O2 if-test inside the main loop
#CPU-time:   9.67   g77 -O3 -ffast-math -funroll-loops if-test inside the main loop
#CPU-time:   9.87   g77 -O3 if-test inside the main loop
#CPU-time:   9.92   g77 -O2 if-test inside the main loop
#Fortsetter men tar bare med 0-10

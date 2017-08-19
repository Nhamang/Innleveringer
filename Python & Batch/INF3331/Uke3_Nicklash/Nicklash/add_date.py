#!/usr/bin/env python
#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import os, sys
from time import strftime, gmtime

filnavn=sys.argv[1] #Setter filnavnet
split=os.path.splitext(filnavn) #splitter filnavn og filtype
navn=split[0]
type=split[1]
dato=strftime("_%b%d_%Y", gmtime())#lager en string med dagens dato
print("%s er nå %s" %(filnavn, (navn+dato+type))) #rinter en fullført melding
os.rename(filnavn, navn+dato+type) #endrer filnavnet
#Oppgave teksten sier ikke noe om at det skal bare skrive ut eller
#om den skal endre filnavet så gjør begge


#Runtime ex.
#C:\Users~~\Skole\INF3331\Uke3>python add_date.py gammel.txt
#
#gammel.txt er nå gammel_Sep17_2013.txt
#

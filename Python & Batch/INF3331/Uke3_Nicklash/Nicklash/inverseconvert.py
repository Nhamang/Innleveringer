#!/usr/bin/env python
#Navn: Nicklas M. Hamang Brukernavn: Nicklash
#Input: outfil valgfritt infil1 infil2 infil3 infiln
#ouput: outfil.dat
import sys

utfil=sys.argv[1]   #Filnavnet på utfilen
comment=sys.argv[2] #valgfritt info 
infiler=sys.argv[3:] #alle input filene

tekst="some comment line \n" #Stringen om sakl printes på utfilen
tekst+=(comment+ "\n")

#Skriver filnavnet  på toppen av kolonnen 
for file in infiler:
    tekst+=("\t\t"+ (file[:-4]))
tekst+=("\n")

#Skriver ut all infoen under filnavnet
teller = 1
plass = 0
while teller==1:
    for i in infiler:
        fil=open(i, 'r')
        linje=fil.readlines()
        lengde=len(linje)-1
        if lengde<plass: #stanser når det ikke er fler linjer
            teller+=1
            break
        #print(linje[plass].rstrip())
        tekst+=(linje[plass].rstrip())
        
    tekst+="\n"
    plass+=1
	
print("Ferdig")

#Lagrer String til fil  og lukker filen
skrivfil=open(utfil+'.dat', 'w')
skrivfil.write(tekst)
skrivfil.close

#Runtime ex.
#C:\Users~~\Skole\INF3331\Uke3>python inverseconvert.py test3 5.0 tmp-model1.dat tmp-model2.dat tmp-measurements.dat
#
#some comment line 
#5.0
#			 tmp-model1 		 		tmp-model2 	  			tmp-measurements 
#           0  1.00000e-01           0  1.00000e+00           0  0.00000e+00
#         1.5  1.00000e-01         1.5  1.88000e-01         1.5  1.00000e-01
#           3  2.00000e-01           3  2.50000e-01           3  2.00000e-01

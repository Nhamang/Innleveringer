#Navn: Nicklas M. Hamang Brukernavn: nicklash
import random
import sys
n=int(sys.argv[1])
i=0
sum=0
avg=0
while i<n:
	r=random.uniform(-1, 1)
	print("%.4f"%r)
	sum=sum+r
	i+=1

avg=sum/n
print("Gjennomsnitt: %.4f" %avg)

#runtime ex.
#
#C:\Users~~\Uke2>python averagerandom.py 5
#0.9370
#0.2221
#-0.0223
#-0.9104
#0.2178
#Gjennomsnitt: 0.0888
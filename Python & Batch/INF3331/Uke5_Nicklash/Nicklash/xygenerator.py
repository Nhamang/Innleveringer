#navn: Nicklas M Hamang Brukernavn: Nicklash
#input: start:stop,step f(x) utput: f(x) for hvert steg f.o.m start t.o.m stop
import sys
from math import *
def main(inp, f):
	input=inp
	#De neste linjene splitter den første inputen på de riktige stedene
	start=float(input.split(":")[0])
	stopstep=input.split(":")[1]
	stop=float(stopstep.split(",")[0])
	step=float(stopstep.split(",")[1])
	#Leser in funksjonen
	func=f

	while start <= stop:
		x=float(start)
		y=float(eval(func))#Evaluerer funksjonen
		print("%g, %g"%(x, y))
		start+=step

if __name__ == "__main__":
	inp=sys.argv[1]
	f=sys.argv[2]
	main(inp, f)
#Runtime ex.
#
#C:\Users\Nicklas\Desktop\Uke5>python xygenerator.py 0:10,2 x*sin(x)
#0, 0
#2, 1.81859
#4, -3.02721
#6, -1.67649
#8, 7.91487
#10, -5.44021
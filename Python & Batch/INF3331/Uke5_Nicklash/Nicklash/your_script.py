"""
@author: Nicklas M. Hamang
@author: Nicklash

This program takes a in a start number,
a stop number and amount for next number, and then a function
Using this to generate the function for each step.
example input: 1;100,2 x*tan(x) 
"""
import sys
from math import *
def main(inp, f):

	"""
	This method takes in the arguments split them
	then generates answer
	
	@param inp:
	@type imp: Str
	@param f
	@type f: Str
	"""
	input=inp
	#De neste linjene splitter den forste inputen paa de riktige stedene
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
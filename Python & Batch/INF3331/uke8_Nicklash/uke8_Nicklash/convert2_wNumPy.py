#navn: Nicklas M. Hamang Brukernavn: Nicklash
#!/usr/bin/env python
import sys, math, string, time
from numpy import *
usage = 'Usage: %s infile' % sys.argv[0]
t0=time.clock()
try:
    infilename = sys.argv[1]
except:
    infilename = input("Input filename: ")

# load file into a list of lines
f = open(infilename, 'r'); lines = f.readlines(); f.close()

# the second line contains the increment in t:
dt = float(lines[1])

# the third line contains the name of the time series:
ynames = lines[2].split()

# store y data in a dictionary of lists of floats:
y = {}           # declare empty dictionary
for name in ynames:
    y[name] = array([]) # empty list (of y values of a time series)

# load data from the rest of the lines:
for line in lines[3:]:
    yvalues = [float(yi) for yi in line.split()]
    # alternative:  yvalues = map(float, line.split())
    if len(yvalues) == 0: continue  # skip blank lines
    i = 0  # counter for yvalues
    for name in ynames:
        d=array([yvalues[i]])
        y[name] = append(y[name], d); i += 1

print ('y dictionary:\n', y)

# write out 2-column files with t and y[name] for each name:
for name in y.keys():
    ofile = open(name+'.dat', 'w')
    for k in range(len(y[name])):
        ofile.write('%12g %12.5e\n' % (k*dt, y[name][k]))
    ofile.close()

t1=time.clock()
print("")
print("Tid: %g"%(t1-t0))

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke8\Convert>python convert2_wNumPy.
#py hmt.out
#
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#
#         1.12076000e-03,   1.09715000e-03,   1.07404000e-03,
#         1.05142000e-03,   1.02927000e-03,   1.00759000e-03,
#         9.86379000e-04,   9.65623000e-04,   9.45316000e-04,
#         9.25451000e-04,   9.06022000e-04,   8.87022000e-04,
#         8.68444000e-04,   8.50281000e-04,   8.32527000e-04,
#         8.15176000e-04,   7.98221000e-04,   7.81657000e-04,
#         7.65475000e-04,   7.49672000e-04,   7.34239000e-04,
#         7.19173000e-04,   7.04465000e-04,   6.90112000e-04,
#         6.76106000e-04])}
#
#Tid: 3.87556

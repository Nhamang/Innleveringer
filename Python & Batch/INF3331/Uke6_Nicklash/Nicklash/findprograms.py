#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import os, sys

def findprograms(program):
    pro={}

    path = os.environ['PATH']
    paths=path.split(os.pathsep)


    for prog in program:
        path_prog= None
        for x in paths:
            if os.path.isdir(x):
                ful=os.path.join(x, prog)#Setter opp full direction
                if sys.platform[:3]=="win": #sjekker om programmet er kjort på en windows pc
                    for extention in ".exe", ".bat":
                        if os.path.isfile(ful+extention):#legger på file extention
                            path_prog=x
                else:
                    if os.path.isfile(ful):
                        path_prog =x
        if path_prog:
                pro[prog]=True
        else:
                pro[prog]=False
    return pro
                

#List fra oppgavetexten.
programs = {
  'gnuplot'  : 'plotting program',
  'gs'       : 'ghostscript, ps/pdf interpreter and previewer',
  'f2py'     : 'generator for Python interfaces to F77',
  'swig'     : 'generator for Python interfaces to C/C++',
  'convert'  : 'image conversion, part of the ImageMagick package',
  }


installed = findprograms(programs.keys())
#For loop og sjekk fra oppgave teksten
for program in installed.keys():
    if installed[program]:
        print ("You have %s (%s)" % (program, programs[program]))
    else:
        print ("*** Program %s was not found on the system" % (program,))

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke6>python findprograms.py
#*** Program gs was not found on the system
#You have convert (image conversion, part of the ImageMagick package)
#*** Program swig was not found on the system
#*** Program f2py was not found on the system
#*** Program gnuplot was not found on the system

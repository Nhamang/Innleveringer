#Navn: Nicklas M. Hamang Brukernavn: Nicklash
import sys, getopt, os, shutil, time, tempfile

#Henter hva som skal gjøres
opt, args = getopt.getopt(sys.argv[1:], "p:m:a:r")


path=''
maxsize=''
maxtime=''
delete=False
MB=maxsize*1048576.0
dag=maxtime*3600*24

#lager "soppelkassen"
trbin=os.path.join(tempfile.gettempdir(), 'trash')

#Sjekker input og lagrer variabler
for v, value in opt:
    if v in ('-p'):
        path=value
    elif v in ('-m'):
        maxsize=eval(value)
    elif v in ('-a'):
        maxtime=eval(value)
    elif v in ('-r'):
        delete=True


#Sjekker om file(r/n) med storrelsen og/eller tiden ekisterer
def sjekk(file, maxsize, maxtime):
    if(MB >= os.path.getsize(file)):
        if(time.time()-dag<=os.path.getatime(file)):
            return True

#Går igjennom dir.
for dirr, dirn, filen in os.walk(path):
    for x in filen:
        fil=os.path.join(dirr, x)
        if(sjekk(fil, maxsize, maxtime)==True):
            if(delete):
                if not os.path.exists(trbin):
                    os.mkdir(trbin)
                shutil.copy(fil, os.path.join(trbin, x))
                os.remove(fil)
                print("Flyttet filen %s til %s"%(fil, trbin))
        
"""
Runtime mangler
"""

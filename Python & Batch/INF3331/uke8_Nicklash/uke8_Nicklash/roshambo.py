#navn: Nicklas M. Hamang Brukernavn: Nicklash
#!/usr/bin/env python
import random
import sys, signal

runder=int(input("How meny rounds? "))

def play(wins):
    hscore=0
    cscore=0
    t=0
    while t<runder:
        handf=input("(R)ock, (P)paper or (S)cissors? ")
        computer=random.choice(('Rock', 'Paper','Scissors'))
        hand=''
        if handf in('Pp'):
            hand='Paper'
        elif handf in('Rr'):
            hand='Rock'
        elif handf in ('Ss'):
            hand='Scissors'
    
        if ((hand=='Rock' and computer=='Scissors')or \
             (hand=='Scissors' and computer=='Paper')or \
             (hand=='Paper' and computer=='Rock')):

            hscore+=1
            print("")
            print("Human: %s     Computer %s" %(hand, computer))
            print('')
            print("Human wins Score: Human %d, computer %d "%(hscore, cscore))
        elif((hand=='Scissors' and computer=='Rock')or \
             (hand=='Paper' and computer=='Scissors')or \
             (hand=='Rock' and computer=='Paper')):
            cscore+=1
            print("")
            print("Human: %s     Computer %s" %(hand, computer))
            print('')
            print("Computer wins Score: Human %d, computer %d "%(hscore, cscore))
        else:
            print("Draw: human %s   computer %s"%(hand, computer))
        t=max(hscore, cscore)

    print("Final score: Human %d   computer %d"%(hscore, cscore))

#def signal_handler(signal, frame):
#    print ("Bye")
#    sys.exit()
#
#signal.signal(signal.SIGINT, signal_handler)


try:
    play(runder)
except EOFError:
    print('bye')
    sys.exit()

#Runtime ex.
#
#C:\Users\Nicklas\Google Drive\Skole\INF3331\Uke8>python roshambo.py
#How meny rounds? 2
#(R)ock, (P)paper or (S)cissors? r
#
#Human: Rock     Computer Paper
#
#Computer wins Score: Human 0, computer 1
#(R)ock, (P)paper or (S)cissors? p
#
#Human: Paper     Computer Scissors
#
#Computer wins Score: Human 0, computer 2
#Final score: Human 0   computer 2


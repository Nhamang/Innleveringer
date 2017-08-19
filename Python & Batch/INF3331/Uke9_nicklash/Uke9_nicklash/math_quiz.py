#Navn: Nicklas M. Hamang Bruker: Nicklash
from random import randint
import sys        
    
def play():
    
    n=int(input("How many rounds do you wish to play? "))
    print("")
    
    lvl=input("What level do you wish to play \n(B)eginner, (I)ntermediate, (A)vanced? ")
    print("")
    lvlstring=''

    if lvl in ("Bb"):
        x=10
        lvlstring='Beginner'
    elif lvl in ("Ii"):
        x=25
        lvlstring='Intermediate'
    elif lvl in ("Aa"):
        x=100
        lvlstring='Advanced'
    else:
        x=10

    t= input("What do yo want to use \n(A)ddition, (S)ubtraction, (M)ultiplication or Mi(X)ed? ")
    print("")
    correct = 0

    for i in range(n):
        n1 = randint(1, x)
        n2 = randint(1, x)
        prod = 0
        op=''

        if t in ('Aa'):
            prod=int(n1+n2)
            op="plus"
        elif t in ('Ss'):
            prod=int(n1-n2)
            op="minus"
        elif t in ('Mm'):
            prod=int(n1*n2)
            op="times"
        elif t in('Xx'):
            y=randint(1,3)
            if y==1:
                prod=int(n1+n2)
                op="plus"
            elif y==2:
                prod=int(n1-n2)
                op="minus"
            elif y==3:
                prod=int(n1*n2)
                op="times"
            

        try:
            ans = int(input("What's %d %s %d? " % (n1, op, n2)))
        except:
            ans=0
        if ans == prod:
            print ("That's right -- well done.\n")
            correct = correct + 1
        else:
            print ("No, I'm afraid the answer is %d.\n" % prod)


    onerd=int(n/3)
    tword=int(onerd*2)
    
    print ("\nI asked you %d questions.  You got %d of them right, at the level %s" % (n, correct, lvlstring))
    if correct>tword:
        print ("Well done!")
    if correct<onerd:
        print("Please ask your math teacher for help")
    elif onerd>correct<tword:
        print("You need more practice")


    valg=input("Do you want to play again (Y)es/(N)o? ")

    if valg in ('Yy'):
        play()
    elif valg in('Nn'):
        print("exit")
        sys.exit()

if __name__=='__main__':
    play()

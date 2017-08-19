#Navn: Nicklas M. Hamang Brukernavn: Nicklash

def join(delimiter, *a):
    ex=""
    for i in range(len(a)):
        if isinstance(a[i], (list, tuple)): #sjekker om i er en liste eller tupler
            for j in a[i]:
                ex+=j#legger til string fra liste eller tupel plass j
                ex+=delimiter #legger til detimiter
        else:
            ex+=a[i]
            ex+=delimiter
    return ex

#Lister og tupler som er gitt av oppgave teksten
list1=['s1', 's2', 's3']
tuple1=('s4', 's5')
#Join som er gitt av oppgaveteksten
ex1=join(' ', 't1', 't2', list1, tuple1, 't3', 't4')
ex2=join(' # ', list1, 't0')

print (ex1)
print (ex2)

"""
Runtime ex.
C:\Users\Nicklas\Desktop\Uke5>python join.py
t1 t2 s1 s2 s3 s4 s5 t3 t4
s1 # s2 # s3 # t0 #
"""

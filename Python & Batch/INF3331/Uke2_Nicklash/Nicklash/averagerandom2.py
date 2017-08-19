#Navn: Nicklas M. Hamang Brukernavn: Nicklash
#!/usr/bin/ env python     # 1: Mellomrom mellom bin/ env
import sys, random
def compute(n):
     i = 0; s = 0
     while i <= n: #2: printer ut en for mye
         s += random.random()
          i += 1 #3: mellomrom foran i.
     return s/n

n = sys.argv[1] #4: Er nå string, må være (int)
print 'the average of %d random numbers is %g" % (n, compute(n)) 
#5: Bruke ' eller " men mellom hverandre

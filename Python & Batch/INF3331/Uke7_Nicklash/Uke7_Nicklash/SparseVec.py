#Nicklas M. Hamang Brukernavn: Nicklash
#Sammarbeidet tett med Nicolas Lopez, nicolael
#!/usr/bin/env python
from numpy import *
class SparseVec:

    def __init__(self, lengde):

        self.n=lengde
        self.nonzero={}
        #self.a = {}
        #i=0
        #while i < self.n:
        #    self.a[i]=0
        #    i+=1

    def __str__(self):
        string=''
        for index in range(self.n):
            if string:
                string+=' '
            value=0
            if index in self.nonzero.keys():
                value=self.nonzero[index]
            string+= ("[%d]=%g"%(index, value))
        return string
        

    def __add__(self, other):

        c=SparseVec(max(self.n, other.n))

        c.nonzero=self.nonzero

        for index in range(c.n):
            if index in other.nonzero.keys():
                if index in c.nonzero.keys():
                    c.nonzero[index]+=other.nonzero[index]
                else:
                    c.nonzero[index]=other.nonzero[index]
        return c





      #  c=SparseVec(max(self.n, other.n))

     #   for index in range(max(self.n, other.n)):
    #        if index in self.nonzero.keys():
   #             c.nonzero[index]+=self.nonzero[index]
  #          if index in other.nonzero.keys():
 #               c.nonzero[index]+=other.nonzero[index]
#        return c




        
      #  if self.n >= other.n:
     #       c=SparseVec(self.n)
    #        c.nonzero=self.nonzero.copy()
   #         for index, val in other.nonzero:
  #              if index in c.nonzero.keys():
 #                   c[index]+=val
#                else:
    #                c[index]=val
   #         return c
  #      if self.n < other.n:
 #           c=SparseVec(other.n)
#            c.nonzero=other.nonzero.copy()
#            for index, val in self.nonzero:
#               if index in c.nonzero.keys():
#                    c[index]+=val
#                else:
#                    c[index]=val
#            return c
        
    def __setitem__(self, i, k):
        if i <self.n:
            self.nonzero[i]=k
    def __getitem__(self, i):
        return self.nonzero.get(i, 0.0)

    def __iter__(self):
        self.index = 0
        return self

    def __next__(self):
        if self.index < self.n:
            i=0
            if self.index in self.nonzero.keys():
                i=self.nonzero[self.index]
            self.index+=1
            return i, self.index-1
        else:
            raise StopIteration
    
    def nonzeros(self):
        #string='{'
        #for i in self.a:
        #    if self.a[i]!=0:
        #        string += ("%d: %g, "%(i, self.a[i]))
        #        i+=1
        #string+='}'
        #return string

        
        #for i in self.a:
        #    if self.a[i]!=0:
        #        self.nonzero[i]=self.a[i]

        return self.nonzero
    
#Runtime ex.
#
#>>> from SparseVec import *
#>>> a=SparseVec(4)
#>>> a[2]=9.2
#>>> a[0]=-1
#>>> print(a)
#[0]=-1 [1]=0 [2]=9.2 [3]=0
#>>> print (a.nonzeros())
#{0: -1, 2: 9.2}
#>>> b=SparseVec(5)
#>>> b[1]=1
#>>>
#>>> print(b)
#[0]=0 [1]=1 [2]=0 [3]=0 [4]=0
#>>> print(b.nonzeros())
#{1: 1}
#>>> c=a+b
#>>> print(c)
#[0]=-1 [1]=1 [2]=9.2 [3]=0 [4]=0
#>>> print (c.nonzeros())
#{0: -1, 1: 1, 2: 9.2}
#>>> for ai, i in a:
#...     print ("a[%d]=%g "%(a, ai))
#a[0]=-1
#a[1]=1
#a[2]=9.2
#a[3]=0

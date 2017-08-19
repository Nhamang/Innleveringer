#Nicklas M. Hamang Brukernavn: Nicklash
#Sammarbeidet tett med Nicolas Lopez, nicolael
#!/usr/bin/env python
from math import sqrt 
class vec3d:

    def __init__(self, x, y, z):
        self.a={}
        self.a[0]=x
        self.a[1]=y
        self.a[2]=z

    def __str__(self):
        return("(%g, %g, %g)"%(self.a[0],self.a[1],self.a[2]))

    def __repr__(self):
        return("Vec3D(%g, %g, %g)"%(self.a[0],self.a[1],self.a[2]))

    def len(self):
        return sqrt(self.a[0]*self.a[0]+self.a[1]*self.a[1]+self.a[2]*self.a[2])

    def __getitem__(self, i):
        return self.a[i]

    def __setitem__(self, i, k):
        self.a[i]=k

    def __pow__(self, b):
        return((self.a[1]*b[2]-self.a[2]*b[1]),
               (self.a[2]*b[0]-self.a[0]*b[2]),
               (self.a[0]*b[1]-self.a[1]*b[0]))

    def __add__(self, b):
        if  (type(b)==type(self)):
            return(self.a[0]+b[0], self.a[1]+b[1], self.a[2]+b[2])
        else:
            return(self.a[0]+b, self.a[1]+b, self.a[2]+b)


    def __radd__(self, b):
        if(type(b)==type(self)):
            return(b[0]+self.a[0], b[1]+self.a[1], b[2]+self.a[2])
        else:
            return(b+self.a[0], b+self.a[1], b+self.a[2])


    def __sub__(self, b):
        if(type(b)==type(self)):
            return(self.a[0]-b[0], self.a[1]-b[1], self.a[2]-b[2])
        else:
            return(self.a[0]-b, self.a[1]-b,self.a[2]-b)


    def __rsub__(self, b):
        if(type(b)==type(self)):
            return(b[0]-self.a[0], b[1]-self.a[1], b[2]-self.a[2])
        else:
            return(b-self.a[0], b-self.a[1], b-self.a[2])


    def __mul__(self, b):
        if(type(b)==type(self)):
            return(self.a[0]*b[0]+self.a[1]*b[1]+self.a[2]*b[2])
        else:
            return(self.a[0]*b+self.a[1]*b+self.a[2]*b)
    def __rmul__(self, b):
        if(type(b)==type(self)):
            return(self.a[0]*b[0]+self.a[1]*b[1]+self.a[2]*b[2])
        else:
            return(self.a[0]*b+self.a[1]*b+self.a[2]*b)
            
    #Fiks
    def __div__(self, b):
        return(self.a[0]/b, self.a[1]/b, self.a[2]/b)
    def __rdiv__(self, b):
        return(self.a[0]/b, self.a[1]/b, self.a[2]/b)

#Runtime ex.
#
#>>> from vec3d_ext import *
#>>> u=vec3d(1, 0, 0)
#>>> v=vec3d(0, -0.2, 8)
#>>> a=1.2
#>>> u+v
#(1, -0.2, 8)
#>>> a+v
#(1.2, 1.0, 9.2)
#>>> v+a
#(1.2, 1.0, 9.2)
#>>> a-v
#(1.2, 1.4, -6.8)
#>>> v-a
#(-1.2, -1.4, 6.8)
#>>> a*v
#9.36
#>>> v*a
#9.36
#>>>v/a
#

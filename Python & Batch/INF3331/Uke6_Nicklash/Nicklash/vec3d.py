#Navn: Nicklas M. Hamang Brukernavn: Nicklash
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
            return(self.a[0]+b[0]), self.a[1]+b[1], self.a[2]+b[2])
    def __sub__(self, b):
        return(self.a[0]-b[0], self.a[1]-b[1], self.a[2]-b[2])
    def __mul__(self, b):
        return(self.a[0]*b[0]+self.a[1]*b[1]+self.a[2]*b[2])

"""
>>> from vec3d import vec3d
>>> u=vec3d(1, 0, 0)
>>> v=vec3d(0, 1, 0)
>>> str(u)
'1, 0, 0'
>>> repr(u)
'Vec3D(1, 0, 0)'
>>> u.len()
1.0
>>> u[1]
0
>>> v[2]=2.5
>>> print(u**v)
(0.0, -2.5, 1)
>>> u+v
(1, 1, 2.5)
>>> u-v
(1, -1, -2.5)
>>> u*v
0.0
>>>
"""

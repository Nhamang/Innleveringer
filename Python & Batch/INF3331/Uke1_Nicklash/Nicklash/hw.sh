#!/bin/bash
#Navn: Nicklas M. Hamang brukernavn: Nicklash
#Mattematisk input. Output er svaret på input
r=$1
s=`echo "scale=3; $1" | bc`
echo "Hello world $r=$s"
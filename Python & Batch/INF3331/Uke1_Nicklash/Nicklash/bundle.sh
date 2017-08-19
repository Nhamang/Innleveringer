#/bin/bash
#Brukernavn: Nicklas M. Hamang Brukernavn: Nicklash
#input: path/to/dir "*.*"
dir=$1
filer=`find $dir -name $2 -print`
for i in $filer; 
do echo "echo unpacking file $i"
echo "cat > $i <<EOF"
cat $i
echo "EOF"
done
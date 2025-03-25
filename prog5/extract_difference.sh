#!/bin/sh

lastEx=$(($1-1))

old=$(realpath "../prog"$lastEx"/i2Compiler/")
new=$(realpath "i2Compiler/")
diff=$(realpath "i2CompilerDiff/")

cd $new
for x in `find .`
    do
        if [ -d "$new/$x" ]; then
            echo "mkdir: " $diff/$x
            mkdir -p "$diff/$x"
        else
            if ! [ -f "$old/$x" ]; then
                echo "cp: " $new/$x $diff/$x
                cp $new/$x $diff/$x
            fi
        fi
done
cp $new"/src/Main.java" $diff"/src/Main.java"

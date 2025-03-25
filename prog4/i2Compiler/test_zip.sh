#!/bin/bash
# Unzip
echo "Unzipping '$1' to 'solution'"

# By default we are using 'jar' to unzip
mkdir -p solution
cd solution
jar xf ../$1
cd ..

# If you have 'unzip' installed you can use this simpler version
#unzip -q $1 -d solution

cd solution/i2Compiler

# Add bin dir
mkdir -p bin

# Compile
echo "Compiling..."
javac -d bin -sourcepath src src/Main.java
# Run
echo "Running on LR0-Grammar..."
java -cp bin Main tests/lr0_input.txt --tokens tests/lr0_tokens.txt --grammar tests/lr0_grammar.txt
echo "Running on SLR1-Grammar..."
java -cp bin Main tests/slr1_input.txt --tokens tests/slr1_tokens.txt --grammar tests/slr1_grammar.txt

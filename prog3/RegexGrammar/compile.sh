#!/bin/bash

echo "Deleting old .class files..."
find . -name '*.class' -delete

echo "Generating ANTLR4 Grammar..."
java -jar ./lib/antlr-4.7.1-complete.jar RegexGrammar.g4

echo "Compiling Sources..."
javac -cp ".:src:lib/antlr-4.7.1-complete.jar" ./src/*.java

echo "Running example, output should contain \"'ab' is a member of 'b*'? false\""
java -cp ".:src:lib/antlr-4.7.1-complete.jar" RegexGrammarRunner "a*" aa "b*" ab


@echo off

SET pathToJavaC=C:\Program Files\Java\jdk1.8.0_121\bin\javac.exe
SET pathToJar=C:\Program Files\Java\jdk1.8.0_121\bin\jar.exe

if not exist "solution" mkdir solution
REM Unzip
echo Unzipping '%1' to 'solution'
REM unzip $1 -d solution
cd solution
"%pathToJar%" xf ../%1
cd ..

cd solution\i2Compiler

REM Add bin dir
if not exist "bin" mkdir bin

REM Compile
echo Compiling...
"%pathToJavaC%" -d bin -sourcepath src src/Main.java

REM Run
echo Running on LR0-Grammar...
java -cp bin Main tests/lr0_input.txt --tokens tests/lr0_tokens.txt --grammar tests/lr0_grammar.txt
echo Running on SLR1-Grammar...
java -cp bin Main tests/slr1_input.txt --tokens tests/slr1_tokens.txt --grammar tests/slr1_grammar.txt

cd ..
cd ..
@echo off

SET pathToJavaC=C:\Program Files\Java\jdk1.8.0_121\bin\javac.exe
echo Using path to javac.exe: %pathToJavaC%

echo Deleting old .class files...
del "*.class" /s /f /q >nul 2>nul

echo Generating ANTLR4 Grammar...
java -jar .\lib\antlr-4.7.1-complete.jar .\RegexGrammar.g4

echo Compiling Sources...
"%pathToJavaC%" -cp ".;src;lib/antlr-4.7.1-complete.jar" .\src\*.java

echo Running example, output should contain "'ab' is a member of 'b*'? false"
java -cp ".;src;lib/antlr-4.7.1-complete.jar" RegexGrammarRunner "a*" aa "b*" ab

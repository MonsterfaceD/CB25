# NUMMER declares the exercise number (used to determine output filename)
NUMMER=3

.PHONY: default clean zip

default: cc2022_programming$(NUMMER).pdf cc2022_solution-prog$(NUMMER).pdf

cc2022_programming$(NUMMER).pdf: main.tex
	pdflatex --jobname=cc2022_programming${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\newif\ifsolution\solutionfalse''\newif\ifprog\progtrue''\input{main.tex}'

cc2022_solution-prog$(NUMMER).pdf: main.tex
	pdflatex --jobname=cc2022_solution-prog${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\newif\ifsolution\solutiontrue''\newif\ifprog\progtrue''\input{main.tex}'

zip: clean
	zip -r RegexGrammar.zip RegexGrammar
	zip -r RegexGrammarSolution.zip RegexGrammarSolution

compile:
	cd i2Compiler && \
	javac -d bin -sourcepath src src/Main.java
	cd i2CompilerSolution  && \
	javac -d bin -sourcepath src src/Main.java

clean:
	rm -f *.log *.aux *.dvi *.out *.ps *.pdf
	rm -fr RegexGrammar/build/*
	rm -fr RegexGrammar/target/*
	rm -fr RegexGrammar/RegexGrammar.interp
	rm -fr RegexGrammar/RegexGrammar.tokens
	rm -fr RegexGrammar/RegexGrammarBaseListener.java
	rm -fr RegexGrammar/RegexGrammarLexer.interp
	rm -fr RegexGrammar/RegexGrammarLexer.java
	rm -fr RegexGrammar/RegexGrammarLexer.tokens
	rm -fr RegexGrammar/RegexGrammarListener.java
	rm -fr RegexGrammar/RegexGrammarParser.java
	rm -fr RegexGrammar/*.class
	rm -fr RegexGrammar/src/*.class
	rm -fr RegexGrammarSolution/build/*
	rm -fr RegexGrammarSolution/target/*
	rm -fr RegexGrammarSolution/RegexGrammar.interp
	rm -fr RegexGrammarSolution/RegexGrammar.tokens
	rm -fr RegexGrammarSolution/RegexGrammarBaseListener.java
	rm -fr RegexGrammarSolution/RegexGrammarLexer.interp
	rm -fr RegexGrammarSolution/RegexGrammarLexer.java
	rm -fr RegexGrammarSolution/RegexGrammarLexer.tokens
	rm -fr RegexGrammarSolution/RegexGrammarListener.java
	rm -fr RegexGrammarSolution/RegexGrammarParser.java
	rm -fr RegexGrammarSolution/*.class
	rm -fr RegexGrammarSolution/src/*.class
	rm -f *.zip

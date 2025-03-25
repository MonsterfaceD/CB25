# NUMMER declares the exercise number (used to determine output filename)
NUMMER=6

.PHONY: default clean zip

default: cc2022_programming$(NUMMER).pdf cc2022_solution-prog$(NUMMER).pdf

cc2022_programming$(NUMMER).pdf: *.tex ../commands.tex
	pdflatex --jobname=cc2022_programming${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\newcommand{\isexercise}{}''\newcommand{\isprog}{}''\input{main.tex}'

cc2022_solution-prog$(NUMMER).pdf: *.tex ../commands.tex
	pdflatex --jobname=cc2022_solution-prog${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\newcommand{\isprog}{}''\input{main.tex}'

zip: clean
	zip -r i2Compiler.zip i2Compiler
	zip -r i2CompilerSolution.zip i2CompilerSolution
	./extract_difference.sh ${NUMMER}
	zip -r i2CompilerDiff.zip i2CompilerDiff

compile:
	cd i2Compiler && \
	javac -d bin -sourcepath src src/Main.java
	cd i2CompilerSolution  && \
	javac -d bin -sourcepath src src/Main.java

clean:
	rm -f *.log *.aux *.dvi *.out *.ps *.pdf
	rm -fr i2Compiler/bin/*
	rm -fr i2CompilerSolution/bin/*
	rm -fr i2CompilerDiff
	rm -f *.zip

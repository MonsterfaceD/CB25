# NUMMER declares the exercise number (used to determine output filename)
NUMMER=0

.PHONY: default clean

default: cc2020_exercise$(NUMMER).pdf cc2020_solution$(NUMMER).pdf

cc2020_exercise$(NUMMER).pdf: *.tex ../commands.tex
	pdflatex --jobname=cc2020_exercise${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\newif\ifsolution\solutionfalse''\newif\ifprog\progfalse''\input{main.tex}'

cc2020_solution$(NUMMER).pdf: *.tex ../commands.tex
	pdflatex --jobname=cc2020_solution${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\newif\ifsolution\solutiontrue''\newif\ifprog\progfalse''\input{main.tex}'

clean:
	rm -f *.log *.aux *.dvi *.out *.ps *.pdf

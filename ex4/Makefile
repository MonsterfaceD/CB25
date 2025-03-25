# NUMMER declares the exercise number (used to determine output filename)
NUMMER=4

.PHONY: default clean

default: cc2022_exercise$(NUMMER).pdf cc2022_solution$(NUMMER).pdf

cc2022_exercise$(NUMMER).pdf: *.tex tasks/*.tex ../commands.tex
	pdflatex --jobname=cc2022_exercise${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\newcommand{\isexercise}{}''\input{main.tex}'

cc2022_solution$(NUMMER).pdf: *.tex tasks/*.tex ../commands.tex
	pdflatex --jobname=cc2022_solution${NUMMER} '\newcommand{\exnumber}'"{${NUMMER}}"'\input{main.tex}'

live:
	latexmk -pvc -pdf --interaction=nonstopmode --jobname=cc2022_live_main -pdflatex="pdflatex -file-line-error %O %S" main.tex

clean:
	rm -f *.log *.aux *.dvi *.out *.ps *.pdf

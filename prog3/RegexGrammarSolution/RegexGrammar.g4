/**
 * Define a grammar called RegexGrammar.
 */
grammar RegexGrammar;

start : regex;

regex 	: LPAR regex RPAR			# group
  		| regex KLEENE				# kleene
  		| regex regex				# concat
  		| CEX+=regex OR CEX+=regex	# choice
  		| setdesc 					# set
  		| ID						# symb
  		| EPS						# eps
  		;         

setdesc : LBRACE (symbols+=ID ',')* symbols+=ID RBRACE		#setdecl
		| LPAR setdesc RPAR									#setgroup		
		| children+=setdesc SETUNION children+=setdesc		#union
		| children+=setdesc SETINTERSECT children+=setdesc	#intersect
		| lhs=setdesc SETMINUS rhs=setdesc					#minus
		;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

ID : [a-z];             

SETUNION : '|';
SETINTERSECT : '&';
SETMINUS : '-';

EPS : '_';
DEF : 'define';
SEMICOLON : ';';
LPAR : '(';
RPAR : ')';
LBRACE : '{';
RBRACE : '}';

KLEENE : '*';
OR : '+';
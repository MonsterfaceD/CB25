/**
 * Define a grammar called RegexGrammar.
 * This is just an example grammar which matches keyword hello followed by an identifier.
 */
grammar RegexGrammar;
r  : 'hello' ID ;         // match keyword hello followed by an identifier

ID : [a-z]+ ;             // match lower-case identifiers

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

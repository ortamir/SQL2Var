package Parser;

import java_cup.runtime.*;

%%
%public
%cup
%class Lexer
%type Token
%line
%scanerror RuntimeException

%{
	public int getLineNumber() { return yyline+1; }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]


/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

newline = [\n\r]
ID=[a-zA-Z_][a-zA-Z0-9_]*
NUMBER=[1-9][0-9]*|[0]
//STRING=\"[a-zA-Z]+\"

%%

"SELECT" 		{ return new Token(yyline, yytext(), SqlSym.SELECT); }
"UPDATE" 		{ return new Token(yyline, yytext(), SqlSym.UPDATE); }
"DELETE" 		{ return new Token(yyline, yytext(), SqlSym.DELETE); }
"MOVE" 			{ return new Token(yyline, yytext(), SqlSym.MOVE); }
"INSERT"		{ return new Token(yyline, yytext(), SqlSym.INSERT); }
"VALUES"		{ return new Token(yyline, yytext(), SqlSym.VALUES); }
"FROM" 			{ return new Token(yyline, yytext(), SqlSym.FROM); }
"WHERE" 		{ return new Token(yyline, yytext(), SqlSym.WHERE); }
"AND" 			{ return new Token(yyline, yytext(), SqlSym.AND); }
"OR" 			{ return new Token(yyline, yytext(), SqlSym.OR); }
"NOT" 			{ return new Token(yyline, yytext(), SqlSym.NOT); }
"IsEmpty" 		{ return new Token(yyline, yytext(), SqlSym.ISEMPTY); }
"IsNotEmpty"	{ return new Token(yyline, yytext(), SqlSym.ISNOTEMPTY); }
"SET" 			{ return new Token(yyline, yytext(), SqlSym.SET); }
"INTO" 			{ return new Token(yyline, yytext(), SqlSym.INTO); }
"IN" 			{ return new Token(yyline, yytext(), SqlSym.IN); }
"BOUNDED" 		{ return new Token(yyline, yytext(), SqlSym.BOUNDED); }
"CHOOSE" 		{ return new Token(yyline, yytext(), SqlSym.CHOOSE); }
"skip" 		{ return new Token(yyline, yytext(), SqlSym.SKIP); }
//"TABLES" 		{ return new Token(yyline, yytext(), SqlSym.TABLES); }
"if" 			{ return new Token(yyline, yytext(), SqlSym.IF); }
"else" 			{ return new Token(yyline, yytext(), SqlSym.ELSE); }
//"newUniqueId()" { return new Token(yyline, yytext(), SqlSym.NEWUNIQUEID); }
"."				{ return new Token(yyline, yytext(), SqlSym.DOT); }
","				{ return new Token(yyline, yytext(), SqlSym.COMMA); }
"*" 			{ return new Token(yyline, yytext(), SqlSym.STAR); }
"(" 			{ return new Token(yyline, yytext(), SqlSym.LPAREN); }
")" 			{ return new Token(yyline, yytext(), SqlSym.RPAREN); }
"=" 			{ return new Token(yyline, yytext(), SqlSym.EQ); }
"~"				{ return new Token(yyline, yytext(), SqlSym.NOT); }
"&"				{ return new Token(yyline, yytext(), SqlSym.LAND); }
"&&"			{ return new Token(yyline, yytext(), SqlSym.LAND); }
"|"				{ return new Token(yyline, yytext(), SqlSym.LOR); }
";"				{ return new Token(yyline, yytext(), SqlSym.SEMICOLON); }
"||"			{ return new Token(yyline, yytext(), SqlSym.LOR); }
"forall"		{ return new Token(yyline, yytext(), SqlSym.FORALL); }
"exists"		{ return new Token(yyline, yytext(), SqlSym.EXISTS); }
"requires"		{ return new Token(yyline, yytext(), SqlSym.REQUIRES); }
"ensures"		{ return new Token(yyline, yytext(), SqlSym.ENSURES); }
"->"			{ return new Token(yyline, yytext(), SqlSym.IMPLIES); }
"<->"			{ return new Token(yyline, yytext(), SqlSym.IFF); }
"!="			{ return new Token(yyline, yytext(), SqlSym.NEQ); }
":"				{ return new Token(yyline, yytext(), SqlSym.COLON); }
{Comment} 		{}
{NUMBER} 		{ return new Token(yyline, "NUMBER", SqlSym.NUMBER, new Integer(yytext())); }
{ID} 			{ return new Token(yyline, "ID", SqlSym.ID, yytext()); }
//{STRING} 		{ return new Token(yyline, "STRING", SqlSym.STRING, yytext()); }
\n 				{}
\r 				{}
" "				{}
"//".*			{newline} { }
. 				{ throw new RuntimeException("Illegal character at line " + (yyline+1) + " : '" + yytext() + "'"); }
<<EOF>> 		{ return new Token(yyline, "EOF", SqlSym.EOF); }
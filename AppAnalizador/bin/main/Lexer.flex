package main;
import compilerTools.Token;

%%
%class Lexer
%type Token
%line
%column
%{
    private Token token(String lexeme, String lexicalComp, int line, int column){
        return new Token(lexeme, lexicalComp, line+1, column+1);
    }
%}
L=[a-z]+
D=[0-9]+
espacio=[\f\t\r\n ]+
%%

/* Espacios en blanco */
{espacio} { /*Ignore*/ }

/* Comentarios */
( "//"(.)* ) { /*Ignore*/ }

/* Operador Igual */
( "=" ) {return token(yytext(), "tk_igual", yyline, yycolumn);}

/* Parentesis de apertura */
( "(" ) {return token(yytext(), "tk_parentesisAbre", yyline, yycolumn);}

/* Parentesis de cierre */
( ")" ) {return token(yytext(), "tk_parentesisCierra", yyline, yycolumn);}

/* Llave de apertura */
( "{" ) {return token(yytext(), "tk_llaveAbre", yyline, yycolumn);}

/* Llave de cierre */
( "}" ) {return token(yytext(), "tk_llaveCierra", yyline, yycolumn);}

/* Punto y coma */
( ";" ) {return token(yytext(), "tk_puntoComa", yyline, yycolumn);}

/* Coma */
( "," ) {return token(yytext(), "tk_coma", yyline, yycolumn);}

/* PALABRAS RESERVADAS */
( "INICIO" ) {return token(yytext(), "tk_inicio", yyline, yycolumn);}
( "FIN" ) {return token(yytext(), "tk_fin", yyline, yycolumn);}
( "LEER" ) {return token(yytext(), "tk_leer", yyline, yycolumn);}
( "IMPRIMIR" ) {return token(yytext(), "tk_imprimir", yyline, yycolumn);}
( "SUM" ) {return token(yytext(), "tk_sum", yyline, yycolumn);}
( "RES" ) {return token(yytext(), "tk_res", yyline, yycolumn);}
( "MUL" ) {return token(yytext(), "tk_mul", yyline, yycolumn);}
( "DIV" ) {return token(yytext(), "tk_div", yyline, yycolumn);}

/* Tipos de datos */
( ENTERO ) {return token(yytext(), "tk_entero", yyline, yycolumn);}
( FLOTANTE ) {return token(yytext(), "tk_flotante", yyline, yycolumn);}

/* Identificador */
{L}({L}|{D}){0,3} {return token(yytext(), "tk_identificador", yyline, yycolumn);}

/* Numero */
("-"?{D}{1,5}("."{D}{1,3})?) {return token(yytext(), "tk_numero", yyline, yycolumn);}

/* Error de análisis */
. { return token(yytext(), "ERROR", yyline, yycolumn); }
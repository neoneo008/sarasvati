grammar Predicate;

options {
   output=AST;
   ASTLabelType=CommonTree;
}

tokens {
  IF      =  'if';
  THEN    =  'then';
  ELSE    =  'else';
  OR      =  'or';
  AND     =  'and';
  NOT     =  'not';
  ACCEPT  =  'Accept';
  DISCARD =  'Discard';
  SKIP    =  'Skip';
  AFTER   =  'after';
  BEFORE  =  'before';
  DAY     =  'day';
  DAYS    =  'days';
  HOUR    =  'hour';
  HOURS   =  'hours';
  WEEK    =  'week';
  WEEKS   =  'weeks';
}

@header {
package com.googlecode.sarasvati.predicate;

import com.googlecode.sarasvati.GuardResponse;
import com.googlecode.sarasvati.SkipNodeGuardResponse;
import com.googlecode.sarasvati.util.SvUtil;
}

@lexer::header {
package com.googlecode.sarasvati.predicate;
}

program returns [PredicateStmt value]
         :  stmt EOF { $value = $stmt.value; }
         ;

stmt returns [PredicateStmt value]
         :  IF^ e=orExpr THEN! ifStmt=stmt ELSE! elseStmt=stmt {$value = new PredicateStmtIf( $e.value, $ifStmt.value, $elseStmt.value ); }
         |  result { $value = $result.value; }
         ;

orExpr returns [PredicateExpr value]
         :  left=andExpr { $value = $left.value; } ( OR^ right=andExpr { $value = new PredicateExprOr( $value, $right.value ); } )*
         ;

andExpr returns [PredicateExpr value]
         :  left=notExpr { $value = $left.value; } ( AND^ right=notExpr { $value = new PredicateExprAnd( $value, $right.value ); } )*
         ;

notExpr returns [PredicateExpr value]
         :  NOT expr { $value = new PredicateExprNot( $expr.value ); }
         |  expr { $value = $expr.value; }
         ;

expr returns [PredicateExpr value]
         :  ID { $value = new PredicateExprSymbol( $ID.text ); }
         |  '('! orExpr ')'! { $value = $orExpr.value; }
         ;

result returns [PredicateStmt value]
         :  guardResult { $value = new PredicateStmtResult( $guardResult.value ); }
         |  NUMBER      { $value = new PredicateStmtResult( Integer.parseInt( $NUMBER.text ) ); }
         |  ID          { $value = new PredicateStmtResult( $ID.text ); }
         |  STRING      { $value = new PredicateStmtResult( SvUtil.normalizeQuotedString( $STRING.text ) ); }
         |  dateResult  { $value = $dateResult.value; }
         ;

dateResult returns [PredicateStmt value]
         :  '('! dateSpec ')'! { $value = $dateSpec.value; }
         ;

dateSpec returns [PredicateStmt value]
         :  ID { $value = new PredicateStmtDateSymbol( $ID.text ); }
         |  NUMBER unit=(HOUR|HOURS|DAY|DAYS|WEEK|WEEKS) type=(BEFORE|AFTER) ID
            { $value = new PredicateStmtRelativeDate( Integer.parseInt( $NUMBER.text ), $unit.text, $type.text, $ID.text ); }
         ;

guardResult returns [GuardResponse value]
         :  ACCEPT   { $value = GuardResponse.ACCEPT_TOKEN_RESPONSE; }
         |  DISCARD  { $value = GuardResponse.DISCARD_TOKEN_RESPONSE; }
         |  SKIP^ ID { $value = new SkipNodeGuardResponse( $ID.text ); }
         |  SKIP     { $value = SkipNodeGuardResponse.DEFAULT_ARC_SKIP_NODE_RESPONSE; }
         ;

// ===========================================================================
// ================   LEXER   ================================================
// ===========================================================================

STRING   :  '"' ( '\\\"' | ~( '"' ) )* '"'
         ;

ID       :  LETTER ( LETTER | DIGIT | '.' )*
         ;

NUMBER   :  '-'? DIGIT+
         ;

fragment LETTER
         : LOWER | UPPER
         ;

fragment LOWER
         : 'a'..'z'
         ;

fragment UPPER
         : 'A'..'Z'
         ;

fragment DIGIT
         : '0'..'9'
         ;

NEWLINE  :   ('\r'? '\n')+ { $channel=HIDDEN; }
         ;

WHITESPACE
         : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ { $channel=HIDDEN; }
         ;

SINGLE_COMMENT
         : '//' ~('\r' | '\n')* NEWLINE { $channel=HIDDEN; }
         ;

MULTI_COMMENT options { greedy = false; }
         : '/*' .* '*/' NEWLINE? { $channel=HIDDEN; }
         ;

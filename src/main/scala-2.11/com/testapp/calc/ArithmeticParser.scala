package com.testapp.calc

import scala.util.parsing.combinator.JavaTokenParsers


sealed trait Operation

case object Add extends Operation

case object Sub extends Operation

case object Mul extends Operation

case object Div extends Operation


sealed trait Expression

case class BinaryOperator(operation: Operation, left: Expression, right: Expression) extends Expression

case class Num(d: Double) extends Expression


class ArithmeticParser extends JavaTokenParsers {

  /*
      Simple arithmetic expressions parser,
      based on Programming in Scala (second edition) by Martin Odersky
      Chapter 33: Combinator Parsing

      Used grammar:

      expr ::= term {"+" term | "-" term}.
      term ::= factor {"*" factor | "/" factor}.
      factor ::= floatingPointNumber | "(" expr ")".

      where {...} denotes repetition
   */


  lazy val expr: Parser[Expression] = term ~ rep("+" ~ term | "-" ~ term) ^^ {
    case left ~ right =>
      right.foldLeft(left) {
        case (e1, "+" ~ e2) => BinaryOperator(Add, e1, e2)
        case (e1, "-" ~ e2) => BinaryOperator(Sub, e1, e2)
      }
  }

  lazy val term: Parser[Expression] = factor ~ rep("*" ~ factor | "/" ~ factor) ^^ {
    case left ~ right => right.foldLeft(left) {
      case (e1, "*" ~ e2) => BinaryOperator(Mul, e1, e2)
      case (e1, "/" ~ e2) => BinaryOperator(Div, e1, e2)
    }
  }

  lazy val factor: Parser[Expression] = "(" ~> expr <~ ")" | floatingPointNumber ^^ { d => Num(d.toDouble) }

  def parse(expression: String): Option[Expression] = parseAll(expr, expression) match {
    case Success(matched, _) => Some(matched)
    case _ => None
  }
}

package com.testapp.calc

import scala.util.parsing.combinator.JavaTokenParsers


sealed abstract class Expression

case class Add(e1: Expression, e2: Expression) extends Expression

case class Sub(e1: Expression, e2: Expression) extends Expression

case class Mul(e1: Expression, e2: Expression) extends Expression

case class Div(e1: Expression, e2: Expression) extends Expression

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
        case (e1, "+" ~ e2) => Add(e1, e2)
        case (e1, "-" ~ e2) => Sub(e1, e2)
      }
  }

  lazy val term: Parser[Expression] = factor ~ rep("*" ~ factor | "/" ~ factor) ^^ {
    case left ~ right => right.foldLeft(left) {
      case (e1, "*" ~ e2) => Mul(e1, e2)
      case (e1, "/" ~ e2) => Div(e1, e2)
    }
  }

  lazy val factor: Parser[Expression] = "(" ~> expr <~ ")" | floatingPointNumber ^^ { d => Num(d.toDouble) }

  def parse(expression: String): ParseResult[Expression] = parseAll(expr, expression)
}

class Calc {
  def evaluate(e: Expression): Double =
    e match {
      case Add(e1: Expression, e2: Expression) => evaluate(e1) + evaluate(e2)
      case Sub(e1: Expression, e2: Expression) => evaluate(e1) - evaluate(e2)
      case Mul(e1: Expression, e2: Expression) => evaluate(e1) * evaluate(e2)
      case Div(e1: Expression, e2: Expression) => evaluate(e1) / evaluate(e2)
      case Num(d: Double) => d
    }
}

object ParseExpr extends ArithmeticParser {

  def main(args: Array[String]) {
    val calc = new Calc()
    val expression = "1+(1-2)*3"

    parse(expression) match {
      case Success(matched, _) =>
        println(matched)
        println(calc.evaluate(matched))
      case Failure(msg, _) => println("FAILURE: " + msg)
      case Error(msg, _) => println("ERROR: " + msg)
    }
  }
}

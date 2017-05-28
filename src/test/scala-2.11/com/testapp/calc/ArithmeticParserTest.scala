package com.testapp.calc

import org.scalatest.{FlatSpec, Matchers}

class ArithmeticParserTest extends FlatSpec with Matchers {

  val parser = new ArithmeticParser()

  "An ArithmeticParser" should "correctly parse valid expressions" in {

    val validExpressionsMap: Map[String, Option[Expression]] = Map(
      "1" -> Some(Num(1)),
      "-2.5" -> Some(Num(-2.5)),
      "(10)" -> Some(Num(10)),
      "1+2" -> Some(BinaryOperator(Add, Num(1), Num(2))),
      "1-2" -> Some(BinaryOperator(Sub, Num(1), Num(2))),
      "1*2" -> Some(BinaryOperator(Mul, Num(1), Num(2))),
      "1/2" -> Some(BinaryOperator(Div, Num(1), Num(2))),
      "(-1+2)" -> Some(BinaryOperator(Add, Num(-1), Num(2))),
      "1+(1+2)" -> Some(BinaryOperator(Add, Num(1), BinaryOperator(Add, Num(1), Num(2)))),
      "1 / 0.5" -> Some(BinaryOperator(Div, Num(1), Num(0.5))),
      "1+2*3" -> Some(BinaryOperator(Add, Num(1), BinaryOperator(Mul, Num(2), Num(3)))),
      "(-1+2)/(3*5)" -> Some(BinaryOperator(Div, BinaryOperator(Add, Num(-1), Num(2)), BinaryOperator(Mul, Num(3), Num(5)))),
      " (  1   +2   *   (-8) ) " -> Some(BinaryOperator(Add, Num(1), BinaryOperator(Mul, Num(2), Num(-8))))
    )

    for ((str, expr) <- validExpressionsMap){
      parser.parse(str) should be (expr)
    }
  }

  it should "return None for invalid expressions" in {
    val invalidExpressions: Seq[String] = List(
      "",
      "-",
      "+",
      "()",
      "5+",
      "(1",
      "a",
      "1+2*",
      "*1+2",
      "(1+2))",
      "4^2",
      "((1)+(2)",
      "1**2",
      "1243+*34"
    )

    for (str <- invalidExpressions){
      parser.parse(str) should be (None)
    }
  }
}

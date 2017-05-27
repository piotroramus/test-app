package com.testapp.calc

import akka.actor.{Actor, Props}
import com.testapp.calc.EvaluationActor.{GetResult, Response}

class EvaluationActor(expr: Expression) extends Actor {

  def evaluate(e: Expression): Double =
    e match {
      case Add(e1: Expression, e2: Expression) => evaluate(e1) + evaluate(e2)
      case Sub(e1: Expression, e2: Expression) => evaluate(e1) - evaluate(e2)
      case Mul(e1: Expression, e2: Expression) => evaluate(e1) * evaluate(e2)
      case Div(e1: Expression, e2: Expression) => evaluate(e1) / evaluate(e2)
      case Num(d: Double) => d
    }

  override def receive: Receive = {
    case GetResult => sender() ! Response(2.0)
  }
}

object EvaluationActor {
  def props(expr: Expression): Props = Props(new EvaluationActor(expr))

  case class Response(n: Double)

  case object GetResult

}

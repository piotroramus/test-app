package com.testapp.calc


import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.testapp.calc.EvaluationActor.{GetResult, Response}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

case class ValidationException(msg: String) extends Exception(msg)

class CalcService(implicit system: ActorSystem) {

  implicit val timeout = Timeout(3 seconds)

  val parser = new ArithmeticParser()
  def calculateExpression(expression: String)(implicit ec: ExecutionContext): Future[Double] =

    parser.parse(expression) match {
      case Some(expr) =>
        val evaluationActor = system.actorOf(EvaluationActor.props(expr))
        (evaluationActor ? GetResult).mapTo[Response].map(_.n)
      case None => Future.failed(ValidationException(s"Failed to validate expression: $expression"))
    }

}

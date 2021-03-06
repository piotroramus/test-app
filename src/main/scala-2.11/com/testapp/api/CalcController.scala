package com.testapp.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, path, post}
import akka.http.scaladsl.server.Route
import com.testapp.calc.CalcService

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

class CalcController(calcService: CalcService)(implicit ec: ExecutionContext) extends Controller with JsonSupport {

  val routes: Route =
    post {
      path("evaluate") {
        entity(as[StringExpression]) { expression =>

          val resultFuture = calcService.calculateExpression(expression.expression)
          onComplete(resultFuture) { result: Try[Double] =>
            result match {
              case Success(n) => complete(n.toString)
              case Failure(_) => complete(StatusCodes.UnprocessableEntity)
            }
          }
        }
      }
    }

}

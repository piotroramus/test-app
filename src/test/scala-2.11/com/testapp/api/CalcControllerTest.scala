package com.testapp.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.testapp.calc.CalcService
import org.scalatest.{Matchers, WordSpec}

class CalcControllerTest extends WordSpec with Matchers with ScalatestRouteTest with JsonSupport {

  val calcService = new CalcService()
  val calcController = new CalcController(calcService)
  val route: Route = calcController.routes

  "The CalcService " should {

    "return valid response for valid expressions" in {

      val expressionsMap: Map[String, String] = Map(
        "1" -> "1.0",
        "1.0" -> "1.0",
        "-3.400" -> "-3.4",
        "000005 + 1" -> "6.0",
        "1+2" -> "3.0",
        "(1+2)" -> "3.0",
        "1-3" -> "-2.0",
        "4*5" -> "20.0",
        "4/4" -> "1.0",
        "1 + 2 * 3" -> "7.0",
        "(1-1) * 5" -> "0.0",
        "2 + 5*3" -> "17.0",
        "(1-1)*2+3*(1-3+4)+10/2" -> "11.0",
        "   -2    +   1  * 0" -> "-2.0",
        "1/2" -> "0.5"
      )

      for ((request, response) <- expressionsMap)
        Post("/evaluate", StringExpression(request)) ~> route ~> check {
          responseAs[String] shouldEqual response
        }

    }

    "return ValidationError for invalid expressions" in {

      val invalidExpressions = Seq(
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

      for (invalidReq <- invalidExpressions)
        Post("/evaluate", StringExpression(invalidReq)) ~> route ~> check {
          status shouldEqual StatusCodes.UnprocessableEntity
        }
    }


  }

}

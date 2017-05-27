package com.testapp.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}


final case class StringExpression(expression: String)

final case class Result(result: Double)


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val expressionFormat: RootJsonFormat[StringExpression] = jsonFormat1(StringExpression)
  implicit val resultFormat: RootJsonFormat[Result] = jsonFormat1(Result)
}
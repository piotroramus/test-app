package com.testapp.calc

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class CalcService {


  def calculateExpression(expression: String)(implicit ec: ExecutionContext): Future[Double] =
  // for the time being simulate the calculations
    Future[Double] {
      // simulate parsing exception
      if (Random.nextBoolean())
        throw new Exception("OMG, FAIL!")
      2.0
    }

}

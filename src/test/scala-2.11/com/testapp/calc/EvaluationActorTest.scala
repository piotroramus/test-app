package com.testapp.calc

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.testapp.calc.EvaluationActor.{GetResult, Response}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class EvaluationActorTest extends TestKit(ActorSystem("calcTestSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An EvaluationActor " must {

    val parser = new ArithmeticParser()

    "respond with valid result for expression " in {

      val requestMap = Map(
        "1" -> Response(1.0),
        "1.0" -> Response(1.0),
        "-3.400" -> Response(-3.4),
        "000005 + 1" -> Response(6.0),
        "1+2" -> Response(3.0),
        "(1+2)" -> Response(3.0),
        "1-3" -> Response(-2.0),
        "4*5" -> Response(20.0),
        "4/4" -> Response(1.0),
        "1 + 2 * 3" -> Response(7.0),
        "(1-1) * 5" -> Response(0.0),
        "2 + 5*3" -> Response(17.0),
        "(1-1)*2+3*(1-3+4)+10/2" -> Response(11.0),
        "   -2    +   1  * 0" -> Response(-2.0),
        "1/2" -> Response(0.5)
      )

      for ((strExpr, response) <- requestMap)
        parser.parse(strExpr).map { expr =>
          val evaluationActor = system.actorOf(EvaluationActor.props(expr))
          evaluationActor ! GetResult
          expectMsg(response)
        }

    }
  }

}

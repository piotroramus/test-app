import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import spray.json._

import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}


// TODO: rethink naming
final case class ExpressionJSON(expression: String)

//# TODO: this can probably be moved somewhere else
final case class Result(result: Double)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val expressionFormat: RootJsonFormat[ExpressionJSON] = jsonFormat1(ExpressionJSON)
  implicit val resultFormat: RootJsonFormat[Result] = jsonFormat1(Result)
}

object WebServer extends StrictLogging with JsonSupport {


  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val host = "localhost"
    val port = 5555

    val routes =
      post {
        path("evaluate") {
          entity(as[ExpressionJSON]) { expression =>
            println("Received JSON: " + expression)
            println("Expression itself: " + expression.expression)
            val resultFuture = Future[Double] {
              // simulate parsing exception
              if (Random.nextBoolean())
                throw new Exception("OMG, FAIL!")
              2.0
            }
            onComplete(resultFuture) { result: Try[Double] =>
              result match {
                case Success(n) => complete(n.toString)
                case Failure(_) => complete(StatusCodes.UnprocessableEntity)
              }
            }
          }
        }
      }

    val binding = Http().bindAndHandle(routes, host, port)

    // TODO: logging
    println(s"Server online at http://$host:$port/\n")

    binding.onComplete {
      case Success(serverBinding) =>
        logger.info("Successful bind to address {}", serverBinding.localAddress)
      case Failure(throwable) =>
        logger.error("Binding failed", throwable)
    }

  }
}

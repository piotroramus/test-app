package com.testapp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.testapp.api.CalcController
import com.testapp.calc.CalcService
import com.testapp.utils.Settings
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

trait CalcRunner extends StrictLogging {

  implicit val system = ActorSystem(Settings.actorSystemName)
  implicit val materializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val host: String = Settings.host
  val port: Int = Settings.port

  val service = new CalcService()
  val calcController = new CalcController(service)
  val routes: Route = calcController.routes

  def run(): Unit = {

    val binding = Http().bindAndHandle(routes, host, port)
    binding.onComplete {
      case Success(serverBinding) =>
        logger.info("Server running at {}", serverBinding.localAddress)
      case Failure(throwable) =>
        logger.error("Failed to start server", throwable)
    }
  }
}

object Calc extends CalcRunner {
  def main(args: Array[String]): Unit = {
    run()
  }
}

package com.testapp.calc

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Stash}
import com.testapp.calc.EvaluationActor.{GetResult, Response}

class EvaluationActor(expr: Expression) extends Actor with Stash with ActorLogging {

  private def evaluate(op: Operation, left: Double, right: Double): Double =
    op match {
      case Add => left + right
      case Sub => left - right
      case Mul => left * right
      case Div => left / right
    }

  expr match {
    case BinaryOperator(op: Operation, left: Expression, right: Expression) =>
      val leftChild = context.actorOf(EvaluationActor.props(left))
      val rightChild = context.actorOf(EvaluationActor.props(right))
      context.become(expectChildrenResponse(leftChild, rightChild, op))
    case Num(n) =>
      context.parent ! Response(n)
      context.become(calculated(n))
  }

  def expectChildrenResponse(leftChild: ActorRef, rightChild: ActorRef, op: Operation): Receive = {
    case Response(n) if sender() == leftChild =>
      context.become(leftReceived(n, op))
    case Response(n) if sender() == rightChild =>
      context.become(rightReceived(n, op))
    case GetResult => stash()
  }

  def leftReceived(leftResult: Double, op: Operation): Receive = {
    case Response(rightResult) =>
      val result = evaluate(op, leftResult, rightResult)
      unstashAll()
      context.parent ! Response(result)
      context.become(calculated(result))
    case GetResult => stash()
  }

  def rightReceived(rightResult: Double, op: Operation): Receive = {
    case Response(leftResult) =>
      val result = evaluate(op, leftResult, rightResult)
      unstashAll()
      context.parent ! Response(result)
      context.become(calculated(result))
    case GetResult => stash()
  }

  def calculated(result: Double): Receive = {
    case GetResult =>
      sender() ! Response(result)
  }

  override def receive: Receive = {
    case m => log.warning(s"Received unexpected message: $m")
  }
}

object EvaluationActor {
  def props(expr: Expression): Props = Props(new EvaluationActor(expr))

  case class Response(n: Double)

  case object GetResult

}

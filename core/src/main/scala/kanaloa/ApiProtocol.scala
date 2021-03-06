package kanaloa

import akka.actor.ActorRef

import scala.concurrent.duration._

sealed trait WorkException

object ApiProtocol {
  sealed trait Request extends Product with Serializable
  sealed trait Response extends Product with Serializable

  /**
   *
   * @param replyTo the ref the reply will be sent to, if not set, it will use sender instead
   * @param sender
   */
  @SerialVersionUID(1L)
  case class QueryStatus(replyTo: Option[ActorRef] = None)(implicit sender: ActorRef) extends Request {
    def reply(msg: Any)(implicit replier: ActorRef): Unit = {
      replyTo.getOrElse(sender).!(msg)(replier)
    }
  }

  @SerialVersionUID(1L)
  case class ShutdownGracefully(reportBackTo: Option[ActorRef] = None, timeout: FiniteDuration = 3.minutes) extends Request

  @SerialVersionUID(1L)
  case object ShutdownSuccessfully extends Response

  @SerialVersionUID(1L)
  case object ShutdownForcefully extends Response

  @SerialVersionUID(1L)
  case class WorkRejected(reason: String) extends WorkException with Response

  @SerialVersionUID(1L)
  case class WorkFailed(reason: String) extends WorkException with Response

  @SerialVersionUID(1L)
  case class WorkTimedOut(reason: String) extends WorkException with Response

}

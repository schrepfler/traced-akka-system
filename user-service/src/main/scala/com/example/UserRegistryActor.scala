package com.example

//#user-registry-actor
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import spray.json.DefaultJsonProtocol._
import org.slf4j.{ILoggerFactory, LoggerFactory}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.util.{Failure, Success}

//#user-case-classes
final case class User(name: String, age: Int, countryOfResidence: String)
final case class Users(users: Seq[User])
//#user-case-classes

object UserRegistryActor {
  final case class ActionPerformed(description: String)
  final case object GetUsers
  final case class CreateUser(user: User)
  final case class GetUser(name: String)
  final case class DeleteUser(name: String)

  def props: Props = Props[UserRegistryActor]
}

class UserRegistryActor extends Actor with ActorLogging {
  import UserRegistryActor._
  implicit val system: ActorSystem = this.context.system
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()
  import DefaultJsonProtocol._

  implicit val userFormat = jsonFormat3(User)

  var users = Set.empty[User]

  override lazy val log = Logging(context.system, classOf[UserRoutes])

  def checkExternalUserExists(name: String): Future[User] = {

    val respEntity = for {
      response <- Http().singleRequest(HttpRequest(uri = "http://localhost:8080/"))
      entity <- Unmarshal(response.entity).to[User]
    } yield entity

    val payload = respEntity.andThen {
      case Success(entity) =>
        s"""{"content": "${entity}"}"""
      case Failure(ex) =>
        s"""{"error": "${ex.getMessage}"}"""
    }
    payload
  }

  def receive: Receive = {
    case GetUsers =>
      sender() ! Users(users.toSeq)
    case CreateUser(user) =>
      for {
        user <- checkExternalUserExists(user.name)
        _ = println(s"User $user externally verified.")
        userFound = true
        _ = users += user
        _ = sender() ! ActionPerformed(s"User ${user.name} created.")
      } yield userFound

    case GetUser(name) =>
      sender() ! users.find(_.name == name)
    case DeleteUser(name) =>
      users.find(_.name == name) foreach { user => users -= user }
      sender() ! ActionPerformed(s"User ${name} deleted.")
  }
}
//#user-registry-actor

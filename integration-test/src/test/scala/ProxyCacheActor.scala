// Akka-Http

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.{ClientTransport, Http}
import akka.pattern.ask
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.{ClientConnectionSettings, ConnectionPoolSettings}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import java.util.Locale.{getISOCountries => ISOCountries}
import java.util.concurrent.TimeUnit

import akka.util.Timeout
import akka.actor.Timers


import scala.concurrent.duration._
import akka.util.ByteString

import scala.concurrent.Future

object ProxyCacheActor {
  def props(): Props = Props[ProxyCacheActor]
}

//case class ProxyServer(countryCode: String, host: String, port: Int)

//case class Location(countryName: String, countryCode: String, instance: String, locale: String, language: String)

class ProxyCacheActor extends Actor with ActorLogging with Timers {

  import akka.pattern.pipe
  import context.dispatcher

  implicit val timeout = Timeout(5 seconds)

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)
  //  val proxyCache = Map[String, Vector[ProxyServer]]

  implicit val locationDecoder: Decoder[Location] = deriveDecoder

  override def preStart(): Unit = {
    println("ProxyCacheActor preStart")
    ISOCountries.foreach(isoCode => {
      timers.startSingleTimer(isoCode, isoCode, 500.millis)
//      scheduledTask = Scheduler.schedule( self, SendHeartbeat, 0, interval, TimeUnit.SECONDS )

//      val proxiesF = (self ? isoCode).mapTo[Vector[ProxyServer]]
//      proxiesF.map(proxies => proxies.map(ps => println(s"AAAA $ps")))
    })
  }

  def receive = {

    case countryCode: String => Future {
      require(ISOCountries.contains(countryCode))

      val settings = ConnectionPoolSettings(context.system)
        .withConnectionSettings(ClientConnectionSettings(context.system))

      http.singleRequest(HttpRequest(uri = s"https://www.proxy-list.download/api/v1/get?type=http&anon=elite&country=$countryCode"), settings = settings)
    }

    /**
     * Handles HTTP response body
     */
    case HttpResponse(StatusCodes.OK, headers, entity, _) => entity
      .dataBytes.
      runFold(ByteString(""))(_ ++ _)
      .foreach { body =>
        val proxies: Vector[ProxyServer] = body
          .utf8String
          .split("\n")
          .toVector
          .map(_.trim)
          .filter(_ != "")
          .flatMap(maybeProxyServer => maybeProxyServer match {
            case s"$host:$port " => Some(ProxyServer("", host, port.toInt))
            case _ => None
          })
        sender() ! proxies
      }

    case resp@HttpResponse(code, _, _, _) => {
      log.error("Request failed, response code: " + code)
      resp.discardEntityBytes()
      Future.failed(new RuntimeException(s"Request failed, response code $code"))
      }.pipeTo(sender())

    case _ => {
      log.error("Unhandled")
    }
  }

}
